/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.struts.action;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.bc.fcl.ImportBc;
import com.gp.cong.logisoft.bc.fcl.QuotationBC;
import com.gp.cong.logisoft.bc.notes.NotesBC;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.Ports;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.FclBuyCostDAO;
import com.gp.cong.logisoft.hibernate.dao.FclBuyDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.rates.Commodity;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.BookingfclUnits;
import com.gp.cvst.logisoft.domain.Charges;
import com.gp.cvst.logisoft.hibernate.dao.BookingfclUnitsDAO;
import com.gp.cvst.logisoft.hibernate.dao.ChargesDAO;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.gp.cvst.logisoft.struts.form.RateGridForm;
import com.gp.cvst.logisoft.util.DBUtil;
import com.logiware.hibernate.dao.TruckerRatesDAO;
import com.logiware.hibernate.domain.FclBuyOtherCommodity;
import com.logiware.ims.IMSServicePort;
import com.logiware.ims.IMSServiceService;
import com.logiware.ims.client.EmptyLocation;
import com.logiware.ims.client.Encrypt;
import com.logiware.ims.client.IMSQuote;
import com.logiware.ims.client.ImsModel;
import com.logiware.ims.client.ParseIMSXml;
import com.logiware.ims.client.ParseLocationXml;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

/**
 *
 * @author Logiware
 */
public class RatesGridAction extends Action {

    private static final Logger log = Logger.getLogger(RatesGridAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RateGridForm rateGridForm = (RateGridForm) form;
        StringBuilder urlBuilder = new StringBuilder();
        String originPort = request.getParameter("originPort");
        rateGridForm.setCommodity(request.getParameter("commodity"));
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loginuser");
        boolean importFlag = null != session.getAttribute(ImportBc.sessionName);
        String imsChecked = (String) request.getParameter("imsChecked");
        urlBuilder.append(request.getContextPath()).append("/rateGrid.do");
        urlBuilder.append("?action=").append(rateGridForm.getAction());
        urlBuilder.append("&destination=").append(request.getParameter("destination"));
        urlBuilder.append("&originName=").append(request.getParameter("originName"));
        urlBuilder.append("&destinationPort=").append(request.getParameter("destinationPort"));
        urlBuilder.append("&origin=").append(request.getParameter("origin"));
        urlBuilder.append("&bulletRates=").append(request.getParameter("bulletRates"));
        urlBuilder.append("&doorOrigin=").append(request.getParameter("doorOrigin"));
        urlBuilder.append("&hazardous=").append(request.getParameter("hazardous"));
        urlBuilder.append("&copyQuote=").append(request.getParameter("copyQuote"));
        urlBuilder.append("&originalCommodity=").append(request.getParameter("originalCommodity"));
        boolean isBulletRates = "true".equalsIgnoreCase(request.getParameter("bulletRates"));
        String originZip = rateGridForm.getOriginZip();
        String fileType = rateGridForm.getFileType();
        boolean isHaz = false;
        String companyCode = new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
        request.setAttribute("companyCode", companyCode);
        if (CommonUtils.isNotEmpty(rateGridForm.getHazardous()) && rateGridForm.getHazardous().equalsIgnoreCase("Y")) {
            isHaz = true;
        }
        if ("IMSQuote".equalsIgnoreCase(rateGridForm.getAction())) {
            try {
                session.removeAttribute("emptyLocationList");
                TruckerRatesDAO truckerRatesDAO = new TruckerRatesDAO();
                String doorOrigin = (String) request.getParameter("doorOrigin");
                String emptyPickUp = (String) request.getParameter("emptyPickUp");
                rateGridForm.setOrigin(null != doorOrigin ? doorOrigin.replace("/", ", ") : "");
                rateGridForm.setChargeId(null != request.getParameter("quoteId") ? (String) request.getParameter("quoteId") : "");
                rateGridForm.setHazardous(null != request.getParameter("hazardous") ? (String) request.getParameter("hazardous") : "N");
                List<IMSQuote> mergedList = new ArrayList<IMSQuote>();
                IMSServicePort imsService = new IMSServiceService().getIMSServicePort();
                String location = imsService.locEmpties(LoadLogisoftProperties.getProperty("ims.clientKey"), LoadLogisoftProperties.getProperty("ims.user"), Encrypt.encrypt(LoadLogisoftProperties.getProperty("ims.password"), LoadLogisoftProperties.getProperty("ims.privateKey")), originZip);
                String result = location.substring(location.indexOf("<result>") + 8, location.indexOf("</result>"));
                if (!"ok".equalsIgnoreCase(result) && null != doorOrigin) {
                    location = imsService.locEmpties(LoadLogisoftProperties.getProperty("ims.clientKey"), LoadLogisoftProperties.getProperty("ims.user"), Encrypt.encrypt(LoadLogisoftProperties.getProperty("ims.password"), LoadLogisoftProperties.getProperty("ims.privateKey")), doorOrigin.replace("/", ", "));
                    result = location.substring(location.indexOf("<result>") + 8, location.indexOf("</result>"));
                }
                if ("ok".equalsIgnoreCase(result)) {
                    File file = new File(LoadLogisoftProperties.getProperty("reportLocation") + "//Documents//IMSSERVICE//" + DateUtils.formatDate(new Date(), "yyyy/MM/dd"));
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    String fileName = LoadLogisoftProperties.getProperty("reportLocation") + "//Documents//IMSSERVICE//" + DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/" + originZip + "-" + DateUtils.formatDate(new Date(), "HHmmss") + ".xml";
                    FileOutputStream fout = new FileOutputStream(fileName);
                    BufferedOutputStream bout = new BufferedOutputStream(fout);
                    OutputStreamWriter out = new OutputStreamWriter(bout, "8859_1");
                    out.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
                    out.write(location);
                    out.flush();
                    out.close();
                    if (null != bout) {
                        bout.close();
                    }
                    if (null != bout) {
                        fout.close();
                    }
                    ParseLocationXml parseLocationXml = new ParseLocationXml();
                    List<EmptyLocation> locationList = parseLocationXml.parseXml(fileName);
                    if (CommonUtils.isNotEmpty(emptyPickUp) && null != emptyPickUp) {
                        request.setAttribute("emptyLocationListStyle", "readOnly");
                    }
                    session.setAttribute("emptyLocationList", new DBUtil().getEmptyLocationList(locationList));
                    File deleteFile = new File(fileName);
                    if (deleteFile.exists()) {
                        deleteFile.deleteOnExit();
                    }
                    boolean hasLocation = false;
                    if (null != emptyPickUp && emptyPickUp.contains("/")) {
                        String unlocationCode = emptyPickUp.substring(emptyPickUp.indexOf("(") + 1, emptyPickUp.indexOf(")"));
                        String portName = emptyPickUp.substring(0, emptyPickUp.indexOf("/") + 3).replace("/", ", ");
                        UnLocation origin = new UnLocationDAO().getUnlocation(unlocationCode);
                        String originCityName = emptyPickUp.substring(0, emptyPickUp.indexOf("/")).replaceAll("[^\\p{Alpha}\\p{Digit}]+", "");
                        boolean isAlternatePort = false;
                        if (null != origin && CommonUtils.isNotEmpty(origin.getAlternatePortName())) {
                            originCityName = origin.getAlternatePortName().replaceAll("[^\\p{Alpha}\\p{Digit}]+", "");
                            isAlternatePort = true;
                        }
                        for (EmptyLocation emptyLocation : locationList) {
                            String emptyCityName = emptyLocation.getLocationName().contains("(") ? emptyLocation.getLocationName().split("\\(")[0] : emptyLocation.getLocationName().split(",")[0];
                            if (originCityName.equalsIgnoreCase(emptyCityName.replaceAll("[^\\p{Alpha}\\p{Digit}]+", ""))) {
                                rateGridForm.setEmptyLocation(emptyLocation.getLocationName());
                                List<IMSQuote> truckerRatesList = truckerRatesDAO.getTruckerRates(originZip, unlocationCode, portName, isHaz, isAlternatePort ? originCityName : "");
                                if (CommonUtils.isNotEmpty(truckerRatesList)) {
                                    mergedList.addAll(truckerRatesList);
                                }
                                String quote = imsService.getQuote(LoadLogisoftProperties.getProperty("ims.clientKey"), LoadLogisoftProperties.getProperty("ims.user"), Encrypt.encrypt(LoadLogisoftProperties.getProperty("ims.password"), LoadLogisoftProperties.getProperty("ims.privateKey")), 1, "ALL", "D", "L", "L", importFlag ? "I" : "X", "ALL", "DV/HC", rateGridForm.getEmptyLocation(), rateGridForm.getOrigin(), rateGridForm.getEmptyLocation(), "", rateGridForm.getHazardous(), "N", "N");
                                result = quote.substring(quote.indexOf("<result>") + 8, quote.indexOf("<result>") + 10);
                                if ("ok".equalsIgnoreCase(result)) {
                                    hasLocation = true;
                                    fileName = LoadLogisoftProperties.getProperty("reportLocation") + "//Documents//IMSSERVICE//" + DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/ImsQuote-" + DateUtils.formatDate(new Date(), "HHmmss") + ".xml";
                                    fout = new FileOutputStream(fileName);
                                    bout = new BufferedOutputStream(fout);
                                    out = new OutputStreamWriter(bout, "8859_1");
                                    out.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
                                    out.write(quote);
                                    out.flush();
                                    out.close();
                                    if (null != bout) {
                                        bout.close();
                                    }
                                    if (null != bout) {
                                        fout.close();
                                    }
                                    ParseIMSXml parseIMSXml = new ParseIMSXml();
                                    List<IMSQuote> imsServiceQuotes = (List<IMSQuote>) parseIMSXml.parseXml(fileName);
                                    if (CommonUtils.isNotEmpty(imsServiceQuotes)) {
                                        mergedList.addAll(imsServiceQuotes);
                                    }
                                    request.setAttribute("hazardous", "Y".equals(rateGridForm.getHazardous()) ? "YES" : "NO");
                                    deleteFile = new File(fileName);
                                    if (deleteFile.exists()) {
                                        deleteFile.deleteOnExit();
                                    }
                                }
                            }
                        }
                    }
                    if (!hasLocation) {
                        String portRampCy = null;
                        boolean noEmptyPickUp = true;
                        if (null != emptyPickUp && emptyPickUp.indexOf("/") != -1) {
                            portRampCy = StringUtils.substringBefore(emptyPickUp, "/") + ", " + StringUtils.substringBefore(StringUtils.substringAfter(emptyPickUp, "/"), "/");
                            noEmptyPickUp = false;
                        }
                        for (EmptyLocation emptyLocation : locationList) {
                            if (noEmptyPickUp) {
                                portRampCy = emptyLocation.getLocationName();
                            }
                            String quote = imsService.getQuote(LoadLogisoftProperties.getProperty("ims.clientKey"), LoadLogisoftProperties.getProperty("ims.user"), Encrypt.encrypt(LoadLogisoftProperties.getProperty("ims.password"), LoadLogisoftProperties.getProperty("ims.privateKey")), 1, "ALL", "D", "L", "L", importFlag ? "I" : "X", "ALL", "DV/HC", portRampCy, originZip, emptyLocation.getLocationName(), "", rateGridForm.getHazardous(), "N", "N");
                            result = quote.substring(quote.indexOf("<result>") + 8, quote.indexOf("<result>") + 10);
                            if (!"ok".equalsIgnoreCase(result)) {
                                quote = imsService.getQuote(LoadLogisoftProperties.getProperty("ims.clientKey"), LoadLogisoftProperties.getProperty("ims.user"), Encrypt.encrypt(LoadLogisoftProperties.getProperty("ims.password"), LoadLogisoftProperties.getProperty("ims.privateKey")), 1, "ALL", "D", "L", "L", importFlag ? "I" : "X", "ALL", "DV/HC", portRampCy, rateGridForm.getOrigin(), emptyLocation.getLocationName(), "", rateGridForm.getHazardous(), "N", "N");
                                result = quote.substring(quote.indexOf("<result>") + 8, quote.indexOf("<result>") + 10);
                            }
                            if ("ok".equalsIgnoreCase(result)) {
                                String imsCityName = emptyLocation.getLocationName();
                                String emptyCityName = imsCityName.contains("(") ? imsCityName.split("\\(")[0] : imsCityName.split(",")[0];
                                String emptyStateCode = imsCityName.split(",")[1];
                                Ports ports = new PortsDAO().findByPortNameAndStateCode(emptyCityName, emptyStateCode);
                                if (null != ports) {
                                    String unlocationCode = ports.getUnLocationCode();
                                    String portName = ports.getPortname() + ", " + ports.getStateCode();
                                    List<IMSQuote> truckerRatesList = truckerRatesDAO.getTruckerRates(originZip, unlocationCode, portName, isHaz, "");
                                    if (CommonUtils.isNotEmpty(truckerRatesList)) {
                                        mergedList.addAll(truckerRatesList);
                                    }
                                }
                                rateGridForm.setEmptyLocation(emptyLocation.getLocationName());
                                hasLocation = true;
                                fileName = LoadLogisoftProperties.getProperty("reportLocation") + "//Documents//IMSSERVICE//" + DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/ImsQuote-" + DateUtils.formatDate(new Date(), "HHmmss") + ".xml";
                                fout = new FileOutputStream(fileName);
                                bout = new BufferedOutputStream(fout);
                                out = new OutputStreamWriter(bout, "8859_1");
                                out.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
                                out.write(quote);
                                out.flush();
                                out.close();
                                if (null != bout) {
                                    bout.close();
                                }
                                if (null != bout) {
                                    fout.close();
                                }
                                ParseIMSXml parseIMSXml = new ParseIMSXml();
                                List<IMSQuote> imsServiceQuotes = (List<IMSQuote>) parseIMSXml.parseXml(fileName);
                                if (CommonUtils.isNotEmpty(imsServiceQuotes)) {
                                    mergedList.addAll(imsServiceQuotes);
                                }
                                request.setAttribute("hazardous", "Y".equals(rateGridForm.getHazardous()) ? "YES" : "NO");
                                deleteFile = new File(fileName);
                                if (deleteFile.exists()) {
                                    deleteFile.deleteOnExit();
                                }
                                break;
                            }
                        }
                    }
                } else {
                    session.removeAttribute("emptyLocationList");
                    if ("error".equalsIgnoreCase(result) && location.contains("CDATA[") && location.indexOf("]]") > -1) {
                        request.setAttribute("LocErrosMessage", location.substring(location.indexOf("CDATA[") + 6, location.indexOf("]]")));
                    } else {
                        request.setAttribute("LocErrosMessage", "Empty PickUp Location does not exist");
                    }
                }
                if (CommonUtils.isNotEmpty(emptyPickUp)) {
                    String originTerminal = emptyPickUp.substring(0, emptyPickUp.lastIndexOf("/"));
                    originTerminal = originTerminal.replaceAll("/", ",").toUpperCase();
                    request.setAttribute("originTerminal", originTerminal);
                }
                if (CommonUtils.isNotEmpty(mergedList)) {
                    Collections.sort(mergedList);
                    request.setAttribute("imsQuoteList", mergedList);
                } else {
                    request.setAttribute("QuoteErrosMessage", "Inland Quote does not exist");
                }
                request.setAttribute("importFlag", importFlag);
            } catch (Exception e) {
                request.setAttribute("ErrosMessageIMS", "IMS Web Service is not currently available");
            }
            return mapping.findForward("imsQuote");
        } else if ("getImsQuote".equalsIgnoreCase(rateGridForm.getAction())) {
            String imsCityName = rateGridForm.getEmptyLocation();
            String emptyCityName = imsCityName.contains("(") ? imsCityName.split("\\(")[0] : imsCityName.split(",")[0];
            String emptyStateCode = imsCityName.split(",")[1];
            List<IMSQuote> mergedList = new ArrayList<IMSQuote>();
            Ports ports = new PortsDAO().findByPortNameAndStateCode(emptyCityName, emptyStateCode);
            if (null != ports) {
                String unlocationCode = ports.getUnLocationCode();
                String portName = ports.getPortname() + ", " + ports.getStateCode();
                List<IMSQuote> truckerRatesList = new TruckerRatesDAO().getTruckerRates(originZip, unlocationCode, portName, isHaz, "");
                if (CommonUtils.isNotEmpty(truckerRatesList)) {
                    mergedList.addAll(truckerRatesList);
                }
            }
            try {
                IMSServicePort imsService = new IMSServiceService().getIMSServicePort();
                String quote = imsService.getQuote(LoadLogisoftProperties.getProperty("ims.clientKey"), LoadLogisoftProperties.getProperty("ims.user"), Encrypt.encrypt(LoadLogisoftProperties.getProperty("ims.password"), LoadLogisoftProperties.getProperty("ims.privateKey")), 1, "ALL", "D", "L", "L", importFlag ? "I" : "X", "ALL", "DV/HC", rateGridForm.getEmptyLocation(), originZip, rateGridForm.getEmptyLocation(), "", rateGridForm.getHazardous(), "N", "N");
                String result = quote.substring(quote.indexOf("<result>") + 8, quote.indexOf("<result>") + 10);
                if (!"ok".equalsIgnoreCase(result)) {
                    quote = imsService.getQuote(LoadLogisoftProperties.getProperty("ims.clientKey"), LoadLogisoftProperties.getProperty("ims.user"), Encrypt.encrypt(LoadLogisoftProperties.getProperty("ims.password"), LoadLogisoftProperties.getProperty("ims.privateKey")), 1, "ALL", "D", "L", "L", importFlag ? "I" : "X", "ALL", "DV/HC", rateGridForm.getEmptyLocation(), rateGridForm.getOrigin(), rateGridForm.getEmptyLocation(), "", rateGridForm.getHazardous(), "N", "N");
                    result = quote.substring(quote.indexOf("<result>") + 8, quote.indexOf("<result>") + 10);
                }
                if ("ok".equalsIgnoreCase(result)) {
                    String fileName = LoadLogisoftProperties.getProperty("reportLocation") + "//Documents//IMSSERVICE//" + DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/ImsQuote-" + DateUtils.formatDate(new Date(), "HHmmss") + ".xml";
                    FileOutputStream fout = new FileOutputStream(fileName);
                    BufferedOutputStream bout = new BufferedOutputStream(fout);
                    OutputStreamWriter out = new OutputStreamWriter(bout, "8859_1");
                    out.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
                    out.write(quote);
                    out.flush();
                    out.close();
                    if (null != bout) {
                        bout.close();
                    }
                    if (null != bout) {
                        fout.close();
                    }
                    ParseIMSXml parseIMSXml = new ParseIMSXml();
                    List<IMSQuote> imsServiceQuotes = (List<IMSQuote>) parseIMSXml.parseXml(fileName);
                    if (CommonUtils.isNotEmpty(imsServiceQuotes)) {
                        mergedList.addAll(imsServiceQuotes);
                    }
                    request.setAttribute("hazardous", "Y".equals(rateGridForm.getHazardous()) ? "YES" : "NO");
                    File deleteFile = new File(fileName);
                    if (deleteFile.exists()) {
                        deleteFile.deleteOnExit();
                    }
                }
            } catch (Exception ex) {

            }
            if (CommonUtils.isNotEmpty(mergedList)) {
                Collections.sort(mergedList);
                request.setAttribute("imsQuoteList", mergedList);
            } else {
                request.setAttribute("QuoteErrosMessage", "Inland Quote does not exist");
            }
            return mapping.findForward("imsQuote");
        } else if ("saveImsQuote".equalsIgnoreCase(rateGridForm.getAction())) {
            if ("booking".equalsIgnoreCase(rateGridForm.getScreenName())) {
                new BookingfclUnitsDAO().deleteCharges(rateGridForm.getChargeId(), importFlag ? "DELIV" : "INLAND");
                List chargesList = null;
                if (importFlag) {
                    chargesList = new BookingfclUnitsDAO().getGroupByCharges(rateGridForm.getChargeId(), "OFIMP");
                } else {
                    chargesList = new BookingfclUnitsDAO().getGroupByCharges(rateGridForm.getChargeId(), "OCNFRT");
                }

                for (Object object : chargesList) {
                    BookingfclUnits fclUnits = (BookingfclUnits) object;
                    BookingfclUnits inlandCharges = new BookingfclUnits();
                    PropertyUtils.copyProperties(inlandCharges, fclUnits);
                    inlandCharges.setManualCharges("M");
                    inlandCharges.setNewFlag("new");
                    if (importFlag) {
                        inlandCharges.setChgCode("DELIVERY");
                        inlandCharges.setChargeCodeDesc("DELIV");
                    } else {
                        inlandCharges.setChgCode("INLAND");
                        inlandCharges.setChargeCodeDesc("INLAND");
                    }
                    inlandCharges.setCostType("FLAT RATE PER CONTAINER");
                    String truckerNumber = rateGridForm.getTruckerNumber();
                    inlandCharges.setAccountNo(truckerNumber);
                    inlandCharges.setAccountName(new TradingPartnerDAO().getAccountName(truckerNumber));
                    inlandCharges.setUpdateOn(new Date());
                    inlandCharges.setUpdateBy(user.getLoginName());
                    inlandCharges.setAmount(Double.parseDouble(rateGridForm.getBuy()));
                    inlandCharges.setComment(rateGridForm.getComment());
                    inlandCharges.setMarkUp(Double.parseDouble(rateGridForm.getSell()));
                    new BookingfclUnitsDAO().save(inlandCharges);
                    new NotesBC().saveNotesWhileAddingCharges("FILE", rateGridForm.getFileNo(), user.getLoginName(), inlandCharges);
                }
            } else {
                new ChargesDAO().deleteCharges(Integer.parseInt(rateGridForm.getChargeId()), importFlag ? "DELIV" : "INLAND");
                List chargesList = null;
                if (importFlag) {
                    chargesList = new ChargesDAO().getGroupByCharges(Integer.parseInt(rateGridForm.getChargeId()), "OFIMP");
                } else {
                    chargesList = new ChargesDAO().getGroupByCharges(Integer.parseInt(rateGridForm.getChargeId()), "OCNFRT");
                }
                for (Object object : chargesList) {
                    Charges charges = (Charges) object;
                    Charges inlandCharges = new Charges();
                    PropertyUtils.copyProperties(inlandCharges, charges);
                    inlandCharges.setInclude("on");
                    inlandCharges.setChargeFlag("M");
                    if (importFlag) {
                        inlandCharges.setChgCode("DELIVERY");
                        inlandCharges.setChargeCodeDesc("DELIV");
                    } else {
                        inlandCharges.setChgCode("INLAND");
                        inlandCharges.setChargeCodeDesc("INLAND");
                    }
                    inlandCharges.setCostType("FLAT RATE PER CONTAINER");
                    String truckerNumber = rateGridForm.getTruckerNumber();
                    inlandCharges.setAccountNo(truckerNumber);
                    inlandCharges.setAccountName(new TradingPartnerDAO().getAccountName(truckerNumber));
                    inlandCharges.setEfectiveDate(new Date());
                    inlandCharges.setUpdateOn(new Date());
                    inlandCharges.setUpdateBy(user.getLoginName());
                    inlandCharges.setAmount(Double.parseDouble(rateGridForm.getBuy()));
                    inlandCharges.setComment(rateGridForm.getComment());
                    inlandCharges.setMarkUp(Double.parseDouble(rateGridForm.getSell()));
                    inlandCharges.setUnitName(new GenericCodeDAO().findById(Integer.parseInt(charges.getUnitType())).getCodedesc());
                    new ChargesDAO().save(inlandCharges);
                    new NotesBC().saveNotesWhileAddingCharges("FILE", rateGridForm.getFileNo(), user.getLoginName(), inlandCharges);
                }
            }
            request.setAttribute("ImsAdded", "quote");
            request.setAttribute("importFlag", importFlag);
            return mapping.findForward("imsQuote");
        } else {
            String doorOrg = (String) request.getParameter("doorOrigin");
            doorOrg = null != doorOrg ? doorOrg.replace("/", ", ") : "";
            Map<String, ImsModel> imsQuoteMap = new HashMap<String, ImsModel>();
            Map<String, Double> distanceMap = new HashMap<String, Double>();
            Double dist = null;
            boolean hasIms = false;
            String markUp = "markup1";
            List list = new FclBuyCostDAO().getOtherCommodity(rateGridForm.getCommodity(), markUp);
            List<FclBuyOtherCommodity> otherCommodityList = new ArrayList<FclBuyOtherCommodity>();
            String baseCommodity = "";
            if (CommonUtils.isNotEmpty(list)) {
                for (int i = 0; i < list.size(); i++) {
                    Object[] object = (Object[]) list.get(i);
                    baseCommodity = null != object[3] ? object[3].toString() : "";
                    FclBuyOtherCommodity fclBuyOtherCommodity = new FclBuyOtherCommodity();
                    fclBuyOtherCommodity.setAddSub(null != object[0] ? object[0].toString() : "");
                    fclBuyOtherCommodity.setMarkUp(null != object[1] ? Double.parseDouble(object[1].toString()) : 0);
                    fclBuyOtherCommodity.setCostCode(null != object[2] ? new GenericCodeDAO().getFieldByCodeAndCodetypeId("Cost Code", object[2].toString(), "codedesc") : "");
                    fclBuyOtherCommodity.setBaseCommodityCode(null != object[3] ? object[3].toString() : "");
                    fclBuyOtherCommodity.setMarkUp2(null != object[4] ? Double.parseDouble(object[4].toString()) : 0);
                    otherCommodityList.add(fclBuyOtherCommodity);
                }
            }
            String[] city = {""};
            if ("Destination".equals(rateGridForm.getAction()) && !isBulletRates) {
                city = rateGridForm.getOrigin().split(",");
            } else if ("Origin".equals(rateGridForm.getAction()) && !isBulletRates) {
                city = rateGridForm.getDestination().split(",");
            }

            if (null != imsChecked && "checked".equalsIgnoreCase(imsChecked) && city.length < 11
                    && CommonUtils.isNotEmpty(rateGridForm.getZip()) && CommonUtils.isNotEmpty(rateGridForm.getDoorOrigin())
                    && CommonUtils.isNotEmpty(rateGridForm.getOrigin())) {
                hasIms = true;
                try {
                    IMSServicePort imsService = new IMSServiceService().getIMSServicePort();
                    String location = imsService.locEmpties(LoadLogisoftProperties.getProperty("ims.clientKey"), LoadLogisoftProperties.getProperty("ims.user"), Encrypt.encrypt(LoadLogisoftProperties.getProperty("ims.password"), LoadLogisoftProperties.getProperty("ims.privateKey")), rateGridForm.getZip());
                    String result = location.substring(location.indexOf("<result>") + 8, location.indexOf("</result>"));
                    if (!"ok".equalsIgnoreCase(result) && null != rateGridForm.getDoorOrigin()) {
                        location = imsService.locEmpties(LoadLogisoftProperties.getProperty("ims.clientKey"), LoadLogisoftProperties.getProperty("ims.user"), Encrypt.encrypt(LoadLogisoftProperties.getProperty("ims.password"), LoadLogisoftProperties.getProperty("ims.privateKey")), rateGridForm.getDoorOrigin().replace("/", ", "));
                        result = location.substring(location.indexOf("<result>") + 8, location.indexOf("</result>"));
                    }
                    if ("ok".equalsIgnoreCase(result)) {
                        File file = new File(LoadLogisoftProperties.getProperty("reportLocation") + "//Documents//IMSSERVICE//" + DateUtils.formatDate(new Date(), "yyyy/MM/dd"));
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        String fileName = LoadLogisoftProperties.getProperty("reportLocation") + "//Documents//IMSSERVICE//" + DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/" + rateGridForm.getZip() + "-" + DateUtils.formatDate(new Date(), "HHmmss") + ".xml";
                        FileOutputStream fout = new FileOutputStream(fileName);
                        BufferedOutputStream bout = new BufferedOutputStream(fout);
                        OutputStreamWriter out = new OutputStreamWriter(bout, "8859_1");
                        out.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
                        out.write(location);
                        out.flush();
                        out.close();
                        if (null != bout) {
                            bout.close();
                        }
                        if (null != bout) {
                            fout.close();
                        }
                        ParseLocationXml parseLocationXml = new ParseLocationXml();
                        List<EmptyLocation> locationList = parseLocationXml.parseXml(fileName);
                        UnLocationDAO unLocationDAO = new UnLocationDAO();
                        PortsDAO portsDAO = new PortsDAO();
                        String synonymousCities = "";

                        if ("Destination".equals(rateGridForm.getAction()) && !isBulletRates) {
                            synonymousCities = portsDAO.getAllSynonymousCities(rateGridForm.getOrigin());
                        } else if ("Origin".equals(rateGridForm.getAction()) && !isBulletRates) {
                            synonymousCities = portsDAO.getAllSynonymousCities(rateGridForm.getDestination());
                        }

                        if (CommonUtils.isNotEmpty(synonymousCities)) {
                            city = (String[]) ArrayUtils.addAll(city, StringUtils.split(synonymousCities, ","));
                        }

                        for (String value : city) {
                            value = value.trim();
                            String locationName = "";
                            String portName = "";
                            String unlocationCode = "";
                            String alternatePortName = "";
                            String imsKey = "";
                            int commaPosition;
                            if (city.length > 1) {
                                Ports ports = portsDAO.findById(Integer.parseInt(value));
                                if (null != ports && CommonUtils.isNotEmpty(ports.getPortname()) && CommonUtils.isNotEmpty(ports.getStateCode())) {
                                    unlocationCode = ports.getUnLocationCode();
                                    alternatePortName = unLocationDAO.getAlternatePortName(unlocationCode);
                                    portName = ports.getPortname() + ", " + ports.getStateCode();
                                    commaPosition = !alternatePortName.trim().isEmpty() ? alternatePortName.indexOf(",") : 0;
                                    alternatePortName = commaPosition > 0 ? alternatePortName.substring(0, commaPosition) : alternatePortName;
                                    locationName = !alternatePortName.trim().isEmpty() ? alternatePortName : ports.getPortname();
                                    imsKey = ports.getPortname();
                                }
                            } else {
                                if (value.indexOf("/") != -1) {
                                    locationName = value.substring(0, value.indexOf("/") + 3).replace("/", ", ");
                                    imsKey = value.substring(0, value.indexOf("/") + 3).replace("/", ", ");
                                } else {
                                    Ports ports = portsDAO.findById(Integer.parseInt(value));
                                    if (null != ports && CommonUtils.isNotEmpty(ports.getPortname()) && CommonUtils.isNotEmpty(ports.getStateCode())) {
                                        unlocationCode = ports.getUnLocationCode();
                                        alternatePortName = unLocationDAO.getAlternatePortName(unlocationCode);
                                        commaPosition = !alternatePortName.trim().isEmpty() ? alternatePortName.indexOf(",") : 0;
                                        alternatePortName = commaPosition > 0 ? alternatePortName.substring(0, commaPosition) : alternatePortName;
                                        locationName = !alternatePortName.trim().isEmpty() ? alternatePortName : ports.getPortname();
                                        portName = ports.getPortname() + ", " + ports.getStateCode();
                                        imsKey = ports.getPortname();
                                    }
                                }
                            }
                            List<IMSQuote> mergedList = new ArrayList<IMSQuote>();
                            String cityName = locationName.replaceAll("[^\\p{Alpha}\\p{Digit}]+", "");
                            for (EmptyLocation emptyLocation : locationList) {
                                String emptyCityName = emptyLocation.getLocationName().contains("(") ? emptyLocation.getLocationName().split("\\(")[0] : emptyLocation.getLocationName().split(",")[0];
                                if (cityName.equalsIgnoreCase(emptyCityName.replaceAll("[^\\p{Alpha}\\p{Digit}]+", ""))) {
                                    String quote = imsService.getQuote(LoadLogisoftProperties.getProperty("ims.clientKey"), LoadLogisoftProperties.getProperty("ims.user"), Encrypt.encrypt(LoadLogisoftProperties.getProperty("ims.password"), LoadLogisoftProperties.getProperty("ims.privateKey")), 1, "ALL", "D", "L", "L", importFlag ? "I" : "X", "ALL", "DV/HC", emptyLocation.getLocationName(), doorOrg, emptyLocation.getLocationName(), "", rateGridForm.getHazardous(), "N", "N");
                                    result = quote.substring(quote.indexOf("<result>") + 8, quote.indexOf("<result>") + 10);
                                    if ("ok".equalsIgnoreCase(result)) {
                                        String quoteFile = LoadLogisoftProperties.getProperty("reportLocation") + "//Documents//IMSSERVICE//" + DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/ImsQuote-" + DateUtils.formatDate(new Date(), "HHmmss") + ".xml";
                                        fout = new FileOutputStream(quoteFile);
                                        bout = new BufferedOutputStream(fout);
                                        out = new OutputStreamWriter(bout, "8859_1");
                                        out.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
                                        out.write(quote);
                                        out.flush();
                                        out.close();
                                        if (null != bout) {
                                            bout.close();
                                        }
                                        if (null != bout) {
                                            fout.close();
                                        }
                                        ParseIMSXml parseIMSXml = new ParseIMSXml();
                                        List<IMSQuote> imsServiceQuotes = (List<IMSQuote>) parseIMSXml.parseXml(quoteFile);
                                        if (CommonUtils.isNotEmpty(imsServiceQuotes)) {
                                            mergedList.addAll(imsServiceQuotes);
                                        }
                                        request.setAttribute("imsQuoteList", parseIMSXml.parseXml(quoteFile));
                                        File deleteFile = new File(quoteFile);
                                        if (deleteFile.exists()) {
                                            deleteFile.deleteOnExit();
                                        }
                                    }
                                }
                            }
                            if (CommonUtils.isNotEmpty(mergedList)) {
                                double previous = 0d;
                                boolean isFirst = true;
                                ImsModel imsModel = new ImsModel();
                                IMSQuote lowestQuote = null;
                                for (IMSQuote iMSQuote : mergedList) {
                                    double sell = Double.parseDouble(iMSQuote.getAllIn2Rate().replace("'", ""));
                                    if (isFirst) {
                                        lowestQuote = iMSQuote;
                                        previous = sell;
                                        isFirst = false;
                                    } else {
                                        if (sell < previous) {
                                            lowestQuote = iMSQuote;
                                            previous = sell;
                                        }
                                    }
                                }
                                imsModel.setLowestQuote(lowestQuote);
                                if (mergedList.contains(lowestQuote)) {
                                    mergedList.remove(lowestQuote);
                                }
                                if (CommonUtils.isNotEmpty(mergedList)) {
                                    imsModel.setAdditionalQuotes(mergedList);
                                }
                                imsModel.setLocation(imsKey.replace(", ", "/"));
                                imsQuoteMap.put(imsKey.replace(", ", "/"), imsModel);
                            }
                        }
                        File deleteFile = new File(fileName);
                        if (deleteFile.exists()) {
                            deleteFile.deleteOnExit();
                        }
                    }
                } catch (Exception e) {
                }

// Truckerlist
                TruckerRatesDAO truckerRatesDAO = new TruckerRatesDAO();
                UnLocationDAO unLocationDAO = new UnLocationDAO();
                PortsDAO portsDAO = new PortsDAO();
                String synonymousCities = "";

                if ("Destination".equals(rateGridForm.getAction()) && !isBulletRates) {
                    synonymousCities = portsDAO.getAllSynonymousCities(rateGridForm.getOrigin());
                } else if ("Origin".equals(rateGridForm.getAction()) && !isBulletRates) {
                    synonymousCities = portsDAO.getAllSynonymousCities(rateGridForm.getDestination());
                }

                if (CommonUtils.isNotEmpty(synonymousCities)) {
                    city = (String[]) ArrayUtils.addAll(city, StringUtils.split(synonymousCities, ","));
                }

                for (String value : city) {
                    value = value.trim();
                    String locationName = "";
                    String portName = "";
                    String unlocationCode = "";
                    String alternatePortName = "";
                    String imsKey = "";
                    int commaPosition;
                    if (city.length > 1) {
                        Ports ports = portsDAO.findById(Integer.parseInt(value));
                        if (null != ports && CommonUtils.isNotEmpty(ports.getPortname()) && CommonUtils.isNotEmpty(ports.getStateCode())) {
                            unlocationCode = ports.getUnLocationCode();
                            alternatePortName = unLocationDAO.getAlternatePortName(unlocationCode);
                            portName = ports.getPortname() + ", " + ports.getStateCode();
                            commaPosition = !alternatePortName.trim().isEmpty() ? alternatePortName.indexOf(",") : 0;
                            alternatePortName = commaPosition > 0 ? alternatePortName.substring(0, commaPosition) : alternatePortName;
                            locationName = !alternatePortName.trim().isEmpty() ? alternatePortName : ports.getPortname();
                            imsKey = ports.getPortname();
                        }
                    } else {
                        if (value.indexOf("/") != -1) {
                            locationName = value.substring(0, value.indexOf("/") + 3).replace("/", ", ");
                            imsKey = value.substring(0, value.indexOf("/") + 3).replace("/", ", ");
                        } else {
                            Ports ports = portsDAO.findById(Integer.parseInt(value));
                            if (null != ports && CommonUtils.isNotEmpty(ports.getPortname()) && CommonUtils.isNotEmpty(ports.getStateCode())) {
                                unlocationCode = ports.getUnLocationCode();
                                alternatePortName = unLocationDAO.getAlternatePortName(unlocationCode);
                                commaPosition = !alternatePortName.trim().isEmpty() ? alternatePortName.indexOf(",") : 0;
                                alternatePortName = commaPosition > 0 ? alternatePortName.substring(0, commaPosition) : alternatePortName;
                                locationName = !alternatePortName.trim().isEmpty() ? alternatePortName : ports.getPortname();
                                portName = ports.getPortname() + ", " + ports.getStateCode();
                                imsKey = ports.getPortname();
                            }
                        }
                    }

                    String zipCode = null != rateGridForm.getZip() ? rateGridForm.getZip() : "";
                    List<IMSQuote> mergedList = new ArrayList<IMSQuote>();
                    String cityName = locationName.replaceAll("[^\\p{Alpha}\\p{Digit}]+", "");
                    List<IMSQuote> truckerRatesList = truckerRatesDAO.getTruckerRates(zipCode, unlocationCode, portName, isHaz, alternatePortName);
                    if (CommonUtils.isNotEmpty(truckerRatesList)) {
                        mergedList.addAll(truckerRatesList);
                    }

                    if (CommonUtils.isNotEmpty(mergedList)) {
                        double previous = 0d;
                        boolean isFirst = true;
                        ImsModel imsModel = new ImsModel();
                        IMSQuote lowestQuote = null;
                        if (imsQuoteMap.containsKey(imsKey.replace(", ", "/"))) {
                            IMSQuote preLowestQuote = imsQuoteMap.get(imsKey.replace(", ", "/")).getLowestQuote();
                            if (null != preLowestQuote) {
                                mergedList.add(preLowestQuote);
                            }
                            List<IMSQuote> preList = imsQuoteMap.get(imsKey.replace(", ", "/")).getAdditionalQuotes();
                            if (null != preList) {
                                mergedList.addAll(preList);
                            }
                        }
                        for (IMSQuote iMSQuote : mergedList) {
                            double sell = Double.parseDouble(iMSQuote.getAllIn2Rate().replace("'", ""));
                            if (isFirst) {
                                lowestQuote = iMSQuote;
                                previous = sell;
                                isFirst = false;
                            } else {
                                if (sell < previous) {
                                    lowestQuote = iMSQuote;
                                    previous = sell;
                                }
                            }
                        }
                        imsModel.setLowestQuote(lowestQuote);
                        if (mergedList.contains(lowestQuote)) {
                            mergedList.remove(lowestQuote);
                        }
                        if (CommonUtils.isNotEmpty(mergedList)) {
                            imsModel.setAdditionalQuotes(mergedList);
                        }
                        imsModel.setLocation(imsKey.replace(", ", "/"));
                        imsQuoteMap.put(imsKey.replace(", ", "/"), imsModel);
                    }
                }
            } else if (null != imsChecked && "checked".equalsIgnoreCase(imsChecked)) {
                request.setAttribute("ImsError", "No Inland calculated for more than 10 cities");
            }
            if ("Origin".equals(rateGridForm.getAction())) {
                Map<String, Double> cityIds = new HashMap<String, Double>();
                request.setAttribute("doorLabel", "Door Destination");
                dist = null;
//                if (CommonUtils.isNotEmpty(rateGridForm.getDistances())) {
//                     String[] distances = rateGridForm.getDistances().split(",");
//                    if (null != city && city.length > 0) {
//                        for (String value : distances) {
//                            String[] cty = value.split("=");
//                            if (null != cty && cty.length == 2) {
//                                dist = Double.parseDouble(cty[1]);
//                                cityIds.put(cty[0], dist);
//                            }
//                        }
//                    }
//                }
//                if (CommonUtils.isNotEmpty(rateGridForm.getCityIds())) {
//                    if (null != city && city.length > 0) {
//                        for (String value : city) {
//                            String[] cty = value.split("==");
//                            if (null != cty && cty.length == 2) {
//                                try {
//                                    dist = cityIds.get(cty[1].trim());
//                                    dist = null == dist ? 0d : dist;
//                                    distanceMap.put(cty[0], dist);
//                                } catch (NumberFormatException ex) {
//                                    log.info("Error converting " + cty[1] + " into Double Value");
//                                }
//                            }
//                        }
//                    }
//                }
                if (null != rateGridForm.getDistances()) {
                    String[] distances = rateGridForm.getDistances().split(",");
                    if (null != distances && distances.length > 0) {
                        for (String value : distances) {
                            String[] distance = value.split("=");
                            if (null != distance && distance.length == 2) {
                                try {
                                    dist = Double.parseDouble(distance[1]);
                                    distanceMap.put(distance[0], dist);
                                } catch (NumberFormatException ex) {
                                    log.info("Error converting " + distance[1] + " into Double Value");
                                    distanceMap.put(distance[0], 0d);
                                }
                            }
                        }
                    }
                }
                request.setAttribute("routeTitle", "Origin");
                request.setAttribute("routeName", null != request.getParameter("originName") ? request.getParameter("originName") : rateGridForm.getOrigin());
                String origin = null;
                if (!isBulletRates) {
                    origin = new PortsDAO().getAllSynonymousCityforOrigin(rateGridForm.getOrigin());
                }
                if (CommonUtils.isEmpty(origin)) {
                    origin = new QuotationBC().findForManagement(rateGridForm.getOrigin(), null);
                }
                String destination = "";
                if (null != imsChecked && null != rateGridForm.getDestination() && !isBulletRates) {
                    String[] dest = rateGridForm.getDestination().split(",");
                    for (String string : dest) {
                        if (CommonUtils.isEmpty(destination)) {
                            destination = new PortsDAO().getAllSynonymousCityById(string);
                        } else {
                            destination = destination + "," + new PortsDAO().getAllSynonymousCityById(string);
                        }
                    }
                } else {
                    destination = new QuotationBC().findForManagementofDest(rateGridForm.getDestination(), null);
                }
                if (null == destination) {
                    destination = new QuotationBC().findForManagement(rateGridForm.getDestination(), null);
                }
                if (CommonUtils.isAllNotEmpty(rateGridForm.getOrigin(), request.getParameter("destinationPort"))) {
                    request.setAttribute("routeTitle", "Both");
                    if ("quickRates".equalsIgnoreCase(rateGridForm.getRatesFrom())) {
                        request.setAttribute("origin", rateGridForm.getOrigin());
                    } else {
                        request.setAttribute("origin", null != request.getParameter("originName") ? request.getParameter("originName") : rateGridForm.getOrigin());
                    }
                    request.setAttribute("destination", request.getParameter("destinationPort"));
                }
                rateGridForm.setOrigin(null != origin ? origin : rateGridForm.getOrigin());
                rateGridForm.setDestination(null != destination ? destination : rateGridForm.getDestination());
            } else if ("Destination".equals(rateGridForm.getAction())) {
                request.setAttribute("routeTitle", "Destination");
                request.setAttribute("routeName", rateGridForm.getDestination());
                request.setAttribute("region", rateGridForm.getRegion());
                request.setAttribute("doorLabel", "Door Origin");
                String destination = new QuotationBC().findForManagementofDest(rateGridForm.getDestination(), null);
                String origin = "";
                if (null != imsChecked && null != rateGridForm.getDestination() && !isBulletRates) {
                    String[] originCity = rateGridForm.getOrigin().split(",");
                    for (String string : originCity) {
                        if (CommonUtils.isEmpty(origin)) {
                            origin = new PortsDAO().getAllSynonymousCityById(string);
                        } else {
                            origin = origin + "," + new PortsDAO().getAllSynonymousCityById(string);
                        }
                    }
                }
                rateGridForm.setOrigin(null != origin && !"".equals(origin) ? origin : rateGridForm.getOrigin());
                rateGridForm.setDestination(null != destination ? destination : new QuotationBC().findForManagement(rateGridForm.getDestination(), null));
                dist = null;
                if (null != rateGridForm.getDistances()) {
                    String[] distances = rateGridForm.getDistances().split(",");
                    if (null != distances && distances.length > 0) {
                        for (String value : distances) {
                            String[] distance = value.split("=");
                            if (null != distance && distance.length == 2) {
                                try {
                                    dist = Double.parseDouble(distance[1]);
                                    distanceMap.put(distance[0], dist);
                                } catch (NumberFormatException ex) {
                                    log.info("Error converting " + distance[1] + " into Double Value");
                                    distanceMap.put(distance[0], 0d);
                                }
                            }
                        }
                    }
                }
            } else {
                request.setAttribute("routeTitle", "Both");
                request.setAttribute("origin", rateGridForm.getOrigin());
                request.setAttribute("destination", rateGridForm.getDestination());
                rateGridForm.setOrigin(new QuotationBC().findForManagement(rateGridForm.getOrigin(), null));
                rateGridForm.setDestination(new QuotationBC().findForManagement(rateGridForm.getDestination(), null));
                request.setAttribute("doorLabel", "Door Origin");
            }
            rateGridForm.setCommodity(new QuotationBC().findForGenericCode(rateGridForm.getCommodity()).getCode());
            String genericId = "";
            if (CommonUtils.isNotEmpty(otherCommodityList) && CommonUtils.isNotEmpty(baseCommodity)) {
                genericId = baseCommodity;
            } else {
                GenericCode genericCode = new GenericCodeDAO().getGenericCodeId("4", rateGridForm.getCommodity());
                if (null != genericCode) {
                    genericId = "" + genericCode.getId();
                }
            }
            List<String> bulletRateSslines = new ArrayList<String>();
            if (!isBulletRates) {
                List<LabelValueBean> bulletRatesList = new ArrayList<LabelValueBean>();
                String originalCommodityId = "";
                if (CommonUtils.isNotEmpty(otherCommodityList) && CommonUtils.isNotEmpty(baseCommodity)) {
                    originalCommodityId = "'" + baseCommodity + "'";
                    if (CommonUtils.isEmpty(request.getParameter("baseCommodity"))) {
                        urlBuilder.append("&baseCommodity=").append(baseCommodity);
                    }
                } else if (CommonUtils.isNotEmpty(request.getParameter("baseCommodity"))) {
                    originalCommodityId = "'" + request.getParameter("baseCommodity") + "'";
                }
                GenericCode genericCode = new GenericCodeDAO().getGenericCodeId("4", request.getParameter("originalCommodity"));
                if (null != genericCode) {
                    if (CommonUtils.isNotEmpty(originalCommodityId)) {
                        originalCommodityId = originalCommodityId + ",'" + genericCode.getId() + "'";
                    } else {
                        originalCommodityId = "'" + genericCode.getId() + "'";
                    }
                }
                Set<String> set = new HashSet<String>();
                if (null != originalCommodityId && !originalCommodityId.equals("")) {
                    String sslineNos = new FclBuyDAO().findMultipleSsl(rateGridForm.getOrigin(), rateGridForm.getDestination(), originalCommodityId, rateGridForm.showHazardous());
                    List<LabelValueBean> bulletRatesCommodities = new GenericCodeDAO().findForAllCommodityCode(sslineNos, rateGridForm.getOrigin(), rateGridForm.getDestination());
                    boolean notFound = true;
                    if (CommonUtils.isNotEmpty(bulletRatesCommodities)) {
                        for (LabelValueBean bean : bulletRatesCommodities) {
                            bulletRateSslines.add(bean.getValue());
                            if (!set.contains(bean.getLabel())) {
                                set.add(bean.getLabel());
                                bulletRatesList.add(new LabelValueBean(bean.getLabel(), StringUtils.substringBefore(bean.getLabel(), "<-->")));
                            }
                            if (notFound && null != genericCode && bean.getValue().equalsIgnoreCase(genericCode.getCode())) {
                                notFound = false;
                            }
                        }
                    }
                    if (notFound && null != genericCode) {
                        bulletRatesList.add(new LabelValueBean(genericCode.getCode() + "<-->" + genericCode.getCodedesc(), genericCode.getCode()));
                    }
                    request.setAttribute("bulletRatesList", bulletRatesList);
                }

                session.setAttribute("url", urlBuilder.toString());
            }
            Commodity commodity = new FclBuyDAO().findMultipleRates(rateGridForm.getOrigin(), rateGridForm.getDestination(), genericId,
                    rateGridForm.showHazardous(), rateGridForm.getAction(), distanceMap, otherCommodityList, imsQuoteMap, rateGridForm.getZip(),
                    originPort, request.getParameter("destinationPort"), request.getContextPath(), fileType);
            if (null != rateGridForm.getHazardous() && rateGridForm.getHazardous().equalsIgnoreCase("Y")) {
                request.setAttribute("HazardousMessage", "(Includes Hazardous)");
            }
            if ("quickRates".equalsIgnoreCase(rateGridForm.getRatesFrom())) {
                commodity.setQuickRates(true);
            } else if ("multiRates".equalsIgnoreCase(rateGridForm.getRatesFrom())) {
                commodity.setMultiRates(true);
            }
            String unlocationCode = "";
            String port = "";
            String destination = request.getParameter("destinationPort");
            if (CommonUtils.isNotEmpty(destination)) {
                unlocationCode = destination.substring(destination.indexOf("(") + 1, destination.indexOf(")"));
//            String dest1= doordest.substring(doordest.lastIndexOf("/")+1,doordest.indexOf("("));
            }
            int j = destination.indexOf("/");
            if (j != -1) {
                String a[] = destination.split("/");
                port = a[0];
            }
            commodity.setImsRates(hasIms);
            commodity.setCommodityName(rateGridForm.getCommodity());
            request.setAttribute("commodity", commodity);
            request.setAttribute("collapse", commodity.getCollapsedTable(destination, unlocationCode, port, fileType));
            if (commodity.isImsRates() && !imsQuoteMap.isEmpty()) {
                request.setAttribute("imsCollapse", commodity.getImsCollapsedTable(fileType));
            }
            request.setAttribute("cityList", commodity.getCityMap());
            long sessionMapKey = new Date().getTime();
            request.setAttribute("sessionMapKey", sessionMapKey);
            request.setAttribute("isImsRates", commodity.isImsRates());
            request.setAttribute("normal", commodity.getNormalTable(fileType));
            request.setAttribute("expand", commodity.getExpandedTableNormal(fileType));
            session.setAttribute("" + sessionMapKey, commodity.getSelectedList());
            session.setAttribute(sessionMapKey + "_collapse", commodity.getCityDetails());
            session.setAttribute(sessionMapKey + "_expand", commodity.getExpandCityDetails());
            session.setAttribute(sessionMapKey + "_normal", commodity.getNormalCityDetails());
            request.setAttribute("commodityCode", rateGridForm.getCommodity());
            if (CommonUtils.isNotEmpty(rateGridForm.getDoorOrigin()) && CommonUtils.isNotEmpty(rateGridForm.getZip())) {
                request.setAttribute("doorOrigin", rateGridForm.getDoorOrigin() + "(" + rateGridForm.getZip() + ")");
            } else {
                request.setAttribute("doorOrigin", rateGridForm.getDoorOrigin());
            }
            request.setAttribute("zip", rateGridForm.getZip());
            request.setAttribute("ratesFrom", rateGridForm.getRatesFrom());
            request.setAttribute("copyQuote", request.getParameter("copyQuote"));
            request.setAttribute("fileNo", null != rateGridForm.getFileNo() ? rateGridForm.getFileNo() : "NEW");
            return mapping.findForward("rateGrid");
        }
    }
}
