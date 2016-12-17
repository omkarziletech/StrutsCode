package com.gp.cong.logisoft.bc.fcl;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.bc.notes.NotesBC;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerBC;
import com.gp.cong.logisoft.domain.CustGeneralDefault;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.Notes;
import com.gp.cong.logisoft.domain.Ports;
import com.gp.cong.logisoft.domain.PortsTemp;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.CustGeneralDefaultDAO;
import com.gp.cong.logisoft.hibernate.dao.CustomerAccountingDAO;
import com.gp.cong.logisoft.hibernate.dao.FclBuyCostDAO;
import com.gp.cong.logisoft.hibernate.dao.FclBuyDAO;
import com.gp.cong.logisoft.hibernate.dao.GeneralInformationDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.NotesDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.logisoft.util.StringFormatter;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.Charges;
import com.gp.cvst.logisoft.domain.CustAddress;
import com.gp.cvst.logisoft.domain.MultiQuote;
import com.gp.cvst.logisoft.domain.MultiQuoteCharges;
import com.gp.cvst.logisoft.domain.Quotation;
import com.gp.cvst.logisoft.domain.Zipcode;
import com.gp.cvst.logisoft.hibernate.dao.ChargesDAO;
import com.gp.cvst.logisoft.hibernate.dao.CustAddressDAO;
import com.gp.cvst.logisoft.hibernate.dao.HazmatMaterialDAO;
import com.gp.cvst.logisoft.hibernate.dao.MultiQuoteDao;
import com.gp.cvst.logisoft.hibernate.dao.QuotationDAO;
import com.gp.cvst.logisoft.hibernate.dao.ZipCodeDAO;
import com.logiware.common.dao.PropertyDAO;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;
import org.directwebremoting.WebContextFactory;
import org.json.JSONArray;

public class QuoteDwrBC {

    HttpServletRequest request;
    GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
    PortsDAO portsDAO = new PortsDAO();

    public GenericCode getCommodityCode(String comCode) throws Exception {
        genericCodeDAO = new GenericCodeDAO();
        GenericCode genericCode = new GenericCode();
        List commodityCodeList = genericCodeDAO.findForGenericCode(comCode);
        if (commodityCodeList != null && commodityCodeList.size() > 0) {
            genericCode = (GenericCode) commodityCodeList.get(0);
        }
        return genericCode;
    }

    public String validateCommodityCode(String commCode) throws Exception {
        String flagValue = "false";
        genericCodeDAO = new GenericCodeDAO();
        String field7 = genericCodeDAO.getFieldByCodeAndCodetypeId("4", commCode, "field7");
        if ("LCL".equals(field7)) {
            flagValue = "true";
        }
        return flagValue;
    }

    public TradingPartner getAgentForDestination(String destination, String importFlag) throws Exception {
        String type = "true".equalsIgnoreCase(importFlag) ? "I" : "F";
        TradingPartner tradingPartner = new TradingPartner();
        List portsList = portsDAO.getPortsForAgentInfoWithDefault(destination, type);
        if (!portsList.isEmpty()) {
            tradingPartner = (TradingPartner) portsList.get(0);
        }
        return tradingPartner;
    }

    public CustAddress getSSlineAcctNo(String acctName) throws Exception {
        CustAddressDAO custAddressDAO = new CustAddressDAO();
        String acctNme = acctName.replace("amp;", "&");
        List custList = custAddressDAO.getCustomerForLookUp(acctNme, "V%SS", "");
        CustAddress custAddress = (CustAddress) custList.get(0);
        return custAddress;
    }

    public String getPortsForAgentInfo(String destination) throws Exception {
        String empty = "";
        List portsList = portsDAO.getPortsForAgentInfo(destination);
        if (portsList.isEmpty()) {
            empty = "empty";
        } else {
            empty = "notEmpty";
        }
        return empty;
    }

    public CustAddress getClientDetails(String accountName, String accountNumber) throws Exception {
        CustAddressBC custAddressBC = new CustAddressBC();
        CustAddress custAddress = new CustAddress();
        custAddress = custAddressBC.getClientDetails(accountName, accountNumber);
        TradingPartnerBC tradingPartnerBC = new TradingPartnerBC();
        tradingPartnerBC.setSubType(custAddress);
        String clientTypes = "";
        String clientType[] = StringUtils.split(custAddress.getAcctType(), ",");
        if (clientType != null) {
            for (int i = 0; i < clientType.length; i++) {
                String clienttype = clientType[i];
                if (clienttype.equals("S")) {
                    clientTypes = clientTypes + "Shipper" + ",";
                }
                if (clienttype.equals("F")) {
                    clientTypes = clientTypes + "Forwarder" + ",";
                }
                if (clienttype.equals("C")) {
                    clientTypes = clientTypes + "Consignee" + ",";
                }
                if (clienttype.equals("N")) {
                    clientTypes = clientTypes + "Notify Party" + ",";
                }
                if (clienttype.equals("SS")) {
                    clientTypes = clientTypes + "Stream ShipeLine" + ",";
                }
                if (clienttype.equals("T")) {
                    clientTypes = clientTypes + "Truck Line" + ",";
                }
                if (clienttype.equals("A")) {
                    clientTypes = clientTypes + "Agent" + ",";
                }
                if (clienttype.equals("I")) {
                    clientTypes = clientTypes + "Import Agent" + ",";
                }
                if (clienttype.equals("E")) {
                    clientTypes = clientTypes + "Export Agent" + ",";
                }
                if (clienttype.equals("V")) {
                    if (CommonFunctions.isNotNull(custAddress.getSubType()) && !custAddress.getSubType().equals("0")) {
                        clientTypes = clientTypes + "Vendor" + "(" + custAddress.getSubType() + ")" + ",";
                    } else {
                        clientTypes = clientTypes + "Vendor" + ",";
                    }
                }
                if (clienttype.equals("O")) {
                    clientTypes = clientTypes + "Others" + ",";
                }
            }
        }
        custAddress.setClientTypeForDwr(clientTypes);
        return custAddress;
    }

    public String getMarkUpValue(String amount, String percentage,
            String minimum) throws Exception {
        NumberFormat numb = new DecimalFormat("###,###,##0.00");
        Double minimum1 = 0.00;
        Double percent = 0.00;
        String returnValue = "";
        int index = percentage.indexOf("%");
        if (index != -1) {
            String percentage1 = percentage.substring(0, index);
            percent = Double.parseDouble(percentage1);
        } else {
            percent = Double.parseDouble(percentage);
        }
        double markup = 0.00;
        double minAmount = 500.00;
        double minSell = 50.00;
        double amount1 = Double.parseDouble(amount);
        double amountToAdd = amount1 * (percent / 100);
        if (amountToAdd > minSell) {
            markup += amount1 + amountToAdd;
        } else if (amountToAdd < minSell) {
            markup = amount1 + minSell;
        } else if (amount1 < minAmount) {
            markup = amount1 + minSell;
        } else {
            markup = amount1 * (percent / 100);
            markup += amount1;
        }

        int index1 = minimum.indexOf("$");
        if (index1 != -1) {
            minimum1 = Double.parseDouble(minimum.substring(index1 + 1, minimum.length()));
        }
        if (markup < minimum1) {
            Double temp = Double.parseDouble(minimum.substring(index1 + 1,
                    minimum.length()));
            returnValue = numb.format(temp);
        } else {
            returnValue = numb.format(markup);
        }

        return returnValue;
    }

    public String getTypeOfMove(String origin, String destination) throws Exception {
        String terminal = "";
        String unLocationCode = "";
        String typeOfMove = "";
        if (origin != null) {
            terminal = StringFormatter.getTerminalFromInputStringr(origin);
            unLocationCode = StringFormatter.orgDestStringFormatter(origin);

        }

        List originList = portsDAO.findForUnlocCodeAndPortNameforPortsTemp(unLocationCode, terminal);
        PortsTemp portsTemp = (PortsTemp) originList.get(0);
        String destCode = "";
        String destName = "";
        if (destination != null) {
            destCode = StringFormatter.orgDestStringFormatter(destination);
            destName = StringFormatter.getTerminalFromInputStringr(destination);
        }
        List destList = portsDAO.findForUnlocCodeAndPortNameforPortsTemp(destCode, destName);
        PortsTemp portsTempDest = (PortsTemp) destList.get(0);
        if (portsTemp.getPortCity() != null
                && portsTemp.getPortCity().equals("Y")
                && portsTempDest.getPortCity() != null
                && portsTempDest.getPortCity().equals("Y")) {
            typeOfMove = "PORT TO PORT";
        } else {
            typeOfMove = "00";
        }
        return typeOfMove;
    }

    public void setRequest() throws Exception {
        request.setAttribute("QuickReview", "QuickReview");
    }

    public String saveIntermodelComments(String quoteId,
            String comments, String commentType) throws Exception {
        if (null != quoteId && !quoteId.trim().equals("")
                && null != comments
                && !comments.trim().equals("")) {
            QuotationDAO quotationDAO = new QuotationDAO();
            Quotation quotation = quotationDAO.findById(Integer.parseInt(quoteId));
            if ("localDrayageComments".equals(commentType)) {
                quotation.setIntermodelComments(comments);
                Notes notes = new Notes();
                notes.setModuleId("FILE");
                notes.setUpdateDate(new Date());
                notes.setNoteTpye("auto");
                notes.setNoteDesc("Zip is not necessary for Intermodal and Local Drayage for File No "
                        + quotation.getFileNo());
                notes.setUpdatedBy(quotation.getQuoteBy());
                if (quotation.getFileNo() != null) {
                    notes.setModuleRefId(quotation.getFileNo());
                }
                NotesDAO notesDAO = new NotesDAO();
                notesDAO.save(notes);
            } else {
                quotation.setDeliveryChargeComments(comments);
            }
        } else {
            comments = null;
        }
        return comments;
    }

    public String saveCommentsAboutAdjustment(String quoteId, String chargeId, String comments, String adjustAmtValue, String applyAllContainer) throws Exception {
        if (null != quoteId && !quoteId.trim().equals("")
                && null != comments
                && !comments.trim().equals("")) {
            Boolean isApplyToallContainerChecked = Boolean.parseBoolean(applyAllContainer);
            Double adjustment = Double.parseDouble(adjustAmtValue);
            if (isApplyToallContainerChecked) {
                ChargesDAO chargesDAO = new ChargesDAO();
                Charges charges = chargesDAO.findById(Integer.parseInt(chargeId));
                String chargeCodeDesc = charges.getChargeCodeDesc();
                chargesDAO.updateAdjustmentChargeComments(quoteId, chargeCodeDesc, comments, adjustment);
            } else {
                ChargesDAO chargesDAO = new ChargesDAO();
                Charges charges = chargesDAO.findById(Integer.parseInt(chargeId));
                charges.setAdjustmentChargeComments(comments);
                charges.setAdjestment(adjustment);
                chargesDAO.save(charges);
            }
        } else {
            comments = null;
        }
        return comments;
    }

    public String getIntermodelComments(String quoteId) throws Exception {
        String intermodelComments = null;
        if (null != quoteId && !quoteId.trim().equals("")) {
            QuotationDAO quotationDAO = new QuotationDAO();
            Quotation quotation = quotationDAO.findById(Integer.parseInt(quoteId));
            intermodelComments = null != quotation && null != quotation.getIntermodelComments() ? quotation.getIntermodelComments() : null;
        } else {
            intermodelComments = null;
        }
        return intermodelComments;
    }

    public String getDeliveryChargeComments(String quoteId) throws Exception {
        QuotationDAO quotationDAO = new QuotationDAO();
        Quotation quotation = quotationDAO.findById(Integer.parseInt(quoteId));
        return null != quotation && null != quotation.getDeliveryChargeComments() ? quotation.getDeliveryChargeComments() : "";
    }

    public String checkHazmat(String id) throws Exception {
        String result = "";
        if (id != null && !id.equalsIgnoreCase("")) {
            HazmatMaterialDAO materialDAO = new HazmatMaterialDAO();
            int bolID = Integer.parseInt(id);
            List hazMatList = materialDAO.findbydoctypeid1("Quote", bolID);
            if (hazMatList != null && hazMatList.size() > 0) {
                result = "true";
            } else {
                result = "false";
            }
        }
        return result;
    }

    public String isHazmat(String quoteId) throws Exception {
        String result = "N";
        if (null != quoteId && !quoteId.equalsIgnoreCase("")) {
            Quotation quotation = new QuotationDAO().findById(Integer.parseInt(quoteId));
            if (null != quotation && null != quotation.getHazmat()) {
                result = quotation.getHazmat();
            }
        }
        return result;
    }

    public void updateSessionForComparison(String fileNumber, HttpServletRequest request) throws Exception {
        if (null != fileNumber && !fileNumber.equals("")
                && fileNumber.length() > 3) {
            fileNumber = fileNumber.substring(3, fileNumber.length());
            HttpSession session = request.getSession();
            QuotationDAO quotationDAO = new QuotationDAO();
            Quotation quote = quotationDAO.getFileNoObject(fileNumber);
            if (null != quote && (null == quote.getBookedBy()
                    || quote.getBookedBy().equalsIgnoreCase(""))) {
                session.setAttribute(QuotationConstants.QUOTATIONOLD,
                        quote);
            }
        }
    }

    public String checkForTheRegion(String destination) throws Exception {
        String flag = "";
        String finalDestination = destination;
        String unlocCode = StringFormatter.orgDestStringFormatter(finalDestination);
        if (null != unlocCode && !unlocCode.equals("")) {
            String region = portsDAO.getRegionBasedOnDest(unlocCode);
            LoadLogisoftProperties loadLogisoftProperties = new LoadLogisoftProperties();
            String ffComRegions = loadLogisoftProperties.getProperty("regionsForFFCommision");
            String[] ffComRegionsArray = ffComRegions.split(",");
            if (null != region && !region.equals("")) {
                for (int i = 0; i < ffComRegionsArray.length; i++) {
                    if (ffComRegionsArray[i].trim().equalsIgnoreCase(region.trim())) {
                        flag = "true";
                    }
                }
            } else {
                flag = "false";
            }
        } else {
            flag = "false";
        }
        return flag;
    }

    public String checkForDisable(String accountNo) throws Exception {
        return new TradingPartnerDAO().chekForDisable(accountNo);
    }

    public boolean rateChangeAlert(String origin, String pol, String pod, String destination,
            String commodityId, String carrier, String quoteDate, String bookingDate,
            String fileNo, String hazmat, HttpServletRequest request) throws Exception {
        boolean result = false;
        MessageResources messageResources = CommonConstants.loadMessageResources();
        GenericCode genericCode = null;
        QuotationBC quotationBC = new QuotationBC();
        FclBuyDAO fclBuyDAO = new FclBuyDAO();
        QuotationDAO quotationDAO = new QuotationDAO();
        if (null != commodityId && !commodityId.equals("")) {
            genericCode = quotationBC.findForGenericCode(commodityId);
        }
        String carrier1 = "";
        origin = quotationBC.findForManagement(origin, null);
        destination = quotationBC.findForManagement(destination, null);
        if (carrier != null && carrier.contains("//")) {
            String tempCarrier[] = carrier.split("//");
            if (tempCarrier.length > 1) {
                carrier1 = tempCarrier[1];
            }
        }
        HashMap hashMap = new HashMap();
        HashMap unitHashMap = new HashMap();
        if (origin != null && destination != null && genericCode != null && carrier1 != null && quoteDate != null) {
            Ports destPort = new PortsDAO().findById(Integer.parseInt(destination));
            String region = null != destPort && null != destPort.getRegioncode() ? destPort.getRegioncode().getField3(): "";
            List otherCommodityList = new FclBuyCostDAO().getOtherCommodity(genericCode.getCode(), null);
            String baseCommodity = genericCode.getId().toString();
            if (CommonUtils.isNotEmpty(otherCommodityList)) {
                for (Object row : otherCommodityList) {
                    Object[] cols = (Object[]) row;
                    baseCommodity = null != cols[3] ? cols[3].toString() : "";
                    break;
                }
            }
            List fclBuyLIst = fclBuyDAO.findRates2(origin.trim(), destination.trim(), baseCommodity, carrier1, quoteDate, bookingDate);

            if (fclBuyLIst.isEmpty()) {
                result = true;
            }
            if (fclBuyLIst.size() > 0) {
                Quotation quotation = quotationDAO.getFileNoObject(fileNo);
                ChargesDAO chargesDAO = new ChargesDAO();
                List bookingFclUnitsList
                        = chargesDAO.getQuoteId(quotation.getQuoteId());
                for (Iterator iterator = bookingFclUnitsList.iterator(); iterator.hasNext();) {
                    Charges charges = (Charges) iterator.next();
                    if (charges.getUnitType() != null) {
                        if (unitHashMap.get(charges.getUnitType()) == null) {
                            unitHashMap.put(charges.getUnitType(), charges.getUnitType());
                        }
                        hashMap.put(charges.getChgCode() + "-" + charges.getUnitType(), charges);
                    } else {
                        hashMap.put(charges.getChgCode(), charges);
                    }
                }
                fclBuyLIst = fclBuyDAO.getSslineIdForBooking(fclBuyLIst, messageResources, hazmat, hashMap, unitHashMap, request, quotation, otherCommodityList, region);
            }

        }
        if (null != request.getAttribute("msg") && request.getAttribute("msg").equals("BkgRates")) {
            result = true;
        }
        return result;
    }
    
    public boolean rateChangeAlertForMultiQuote(String fileNo,String multiQuoteId,HttpServletRequest request) throws Exception {
        boolean result = false;
        MessageResources messageResources = CommonConstants.loadMessageResources();
        GenericCode genericCode = null;
//        QuotationBC quotationBC = new QuotationBC();
        FclBuyDAO fclBuyDAO = new FclBuyDAO();
//        QuotationDAO quotationDAO = new QuotationDAO();
        
         MultiQuote multiQuote = new MultiQuoteDao().findById(Long.parseLong(multiQuoteId));
//        if (null != multiQuote) {
            genericCode =new GenericCodeDAO().findById(Integer.parseInt(multiQuote.getCommodity()));
//        }
//        String carrier1 = "";
//        origin = quotationBC.findForManagement(origin, null);
//        destination = quotationBC.findForManagement(destination, null);
//        if (carrier != null && carrier.contains("//")) {
//            String tempCarrier[] = carrier.split("//");
//            if (tempCarrier.length > 1) {
//                carrier1 = tempCarrier[1];
//            }
//        }
        HashMap hashMap = new HashMap();
        HashMap unitHashMap = new HashMap();
        if (multiQuote.getOriginCode() != null && multiQuote.getDestinationCode() != null && genericCode != null && multiQuote.getCarrierNo() != null) {
            Ports destPort = new PortsDAO().findById(Integer.parseInt(multiQuote.getDestinationCode()));
            String region = null != destPort && null != destPort.getRegioncode() ? destPort.getRegioncode().getField3(): "";
            List otherCommodityList = new FclBuyCostDAO().getOtherCommodity(genericCode.getCode(), null);
            String baseCommodity = genericCode.getId().toString();
            if (CommonUtils.isNotEmpty(otherCommodityList)) {
                for (Object row : otherCommodityList) {
                    Object[] cols = (Object[]) row;
                    baseCommodity = null != cols[3] ? cols[3].toString() : "";
                    break;
                }
            }
            List fclBuyList = fclBuyDAO.findForSearchFclBuyRatesForCompressList2(multiQuote.getOriginCode(), multiQuote.getDestinationCode(), baseCommodity, multiQuote.getCarrierNo());

            if (fclBuyList.isEmpty()) {
                result = true;
            }
            if (fclBuyList.size() > 0) {
                Quotation quotation = new QuotationDAO().getFileNoObject(fileNo);
                List bookingFclUnitsList = multiQuote.getMultiQuoteCharges();
                for (Iterator iterator = bookingFclUnitsList.iterator(); iterator.hasNext();) {
                    MultiQuoteCharges charges = (MultiQuoteCharges) iterator.next();
                    if (charges.getUnitType() != null) {
                        if (unitHashMap.get(charges.getUnitType()) == null) {
                            unitHashMap.put(charges.getUnitType(), charges.getUnitType());
                        }
                        hashMap.put(charges.getChargeCodeDesc() + "-" + charges.getUnitType(), charges);
                    } else {
                        hashMap.put(charges.getChargeCodeDesc(), charges);
                    }
                }
                fclBuyList = fclBuyDAO.getSslineIdForBooking(fclBuyList, messageResources, multiQuote.getHazmat(), hashMap, unitHashMap, request, quotation, otherCommodityList, region);
            }

        }
        if (null != request.getAttribute("msg") && request.getAttribute("msg").equals("BkgRates")) {
            result = true;
        }
        return result;
    }

    public List getPackageType(String piece) throws Exception {
        Integer noOfPiece = CommonFunctions.isNotNull(piece) ? Integer.parseInt(piece) : 0;
        QuotationBC quotationBC = new QuotationBC();
        List packageTypeList = new ArrayList();
        if (noOfPiece > 1) {
            packageTypeList = quotationBC.getPackageType(49);

        } else {
            packageTypeList = quotationBC.getPackageTypeSingular(49);
        }
        return packageTypeList;
    }

    public GenericCode getChargeByChargeDescId(String chargeDescId) throws Exception {
        GenericCode gen = genericCodeDAO.findById(Integer.parseInt(chargeDescId));
        return gen;
    }

    public void getOriginsForDestination(String origin, String destination, String searchBy, String zip, String commcode, HttpServletRequest request) throws Exception {
        FclBuyDAO fclBuyDAO = new FclBuyDAO();
        String desc = "";
        String code = "";
        if (destination.contains("/")) {
            String array[] = destination.split("/");
            if (destination.equals("Coussol/Fos sur Mer/FRCOU")) {
                desc = array[0] + "/" + array[1];
            } else {
                desc = array[0];
            }
            if (destination.lastIndexOf("(") != -1) {
                code = destination.substring(destination.lastIndexOf("(") + 1, destination.lastIndexOf(")"));
            }
        }
        Map<String, Map<String, String>> list = fclBuyDAO.getOriginsByDestination(desc, code, "%", searchBy, commcode);
        request.setAttribute("listHeading", "Origin List");
        request.setAttribute("enableIms", LoadLogisoftProperties.getProperty("ims.enable"));
        request.setAttribute("route", "Destination");
        request.setAttribute("zip", zip);
        request.setAttribute("listSize", Math.round(list.size() / 4));
        String value = contructRegionListHTML(list, request, zip);
        request.setAttribute("originDestinationList", value);
    }

    public Object[] getQuickRatesOriginsForDestination(String origin, String destination, String searchBy, String comCode) throws Exception {
        FclBuyDAO fclBuyDAO = new FclBuyDAO();
        request = WebContextFactory.get().getHttpServletRequest();
        String desc = "";
        String code = "";
        if (destination.contains("/")) {
            String array[] = destination.split("/");
            if (destination.equals("Coussol/Fos sur Mer/FRCOU")) {
                desc = array[0] + "/" + array[1];
            } else {
                desc = array[0];
            }
            if (destination.lastIndexOf("(") != -1) {
                code = destination.substring(destination.lastIndexOf("(") + 1, destination.lastIndexOf(")"));
            }
        }
        Map<String, Map<String, String>> list = fclBuyDAO.getOriginsByDestination(desc, code, "%", searchBy, comCode);
        request.setAttribute("listHeading", "Origin List");
        request.setAttribute("route", "Destination");
        request.setAttribute("ratesFrom", "quickRates");
        request.setAttribute("listSize", Math.round(list.size() / 4));
        Object[] objectArry = new Object[2];
        Object[] key = list.keySet().toArray();
        if (null != list && list.size() == 1 && list.get(key[0]).size() == 1) {
            Object[] key1 = list.get(key[0]).keySet().toArray();
            objectArry[0] = list.get(key[0]).get(key1[0]);
            return objectArry;
        } else {
            String value = contructRegionListHTML(list, request, "");
            request.setAttribute("originDestinationList", value);
            objectArry[0] = WebContextFactory.get().forwardToString("/jsps/fclQuotes/OriginAndDestination.jsp");
            JSONArray map = (JSONArray) request.getAttribute("cityList");
            objectArry[1] = map.toString();
            return objectArry;
        }
    }

    public void getDestinationsForOrigin(String destination, String origin, String searchBy, String zip, String comCode, HttpServletRequest request) throws Exception {
        FclBuyDAO fclBuyDAO = new FclBuyDAO();
        JSONArray allCity = new JSONArray();
        String desc = "";
        String code = "";
        if (origin.contains("/")) {
            String array[] = origin.split("/");
            if (origin.equals("Minneapolis/St Paul Apt/(USMSP/MN)")
                    || origin.equals("Coussol/Fos sur Mer/(FRCOU)")) {
                desc = array[0] + "/" + array[1];
            } else {
                desc = array[0];
            }
            if (origin.lastIndexOf("(") != -1) {
                code = origin.substring(origin.lastIndexOf("(") + 1, origin.lastIndexOf(")"));
            }
        }
        Map<String, Map<String, String>> map = fclBuyDAO.getDestinationsByOrigin(desc, code, "%", searchBy, comCode);
        request.setAttribute("originDestinationList", map);
        request.setAttribute("enableIms", LoadLogisoftProperties.getProperty("ims.enable"));
        request.setAttribute("listHeading", "Destination List");
        request.setAttribute("route", "Origin");
        request.setAttribute("zip", zip);
        request.setAttribute("listSize", Math.round(map.size() / 4));
        request.setAttribute("cityList", allCity);
        String value = contructRegionListHTML(map, request, zip);
        request.setAttribute("originDestinationList", value);
    }

    private String contructRegionListHTML(Map<String, Map<String, String>> regionMap, HttpServletRequest request, String zip) throws Exception {
        StringBuilder html = new StringBuilder();
        int totalCount = 0;
        Set keySet = regionMap.keySet();
        JSONArray allCity = new JSONArray();
        TreeMap<Double, String> distanceMap = new TreeMap<Double, String>();
        Map<String, String> topFiveMap = new HashMap<String, String>();
        NumberFormat nf = new DecimalFormat("#0");
        if (CommonUtils.isNotEmpty(zip)) {
            int topFiveCount = 0;
            for (Object key : keySet) {
                totalCount = totalCount + regionMap.get(key).size();
                Map<String, String> region = regionMap.get(key);
                Set regionKeySet = region.keySet();
                for (Object regionKey : regionKeySet) {
                    String city = region.get(regionKey).substring(region.get(regionKey).indexOf("(") + 1, region.get(regionKey).indexOf(")"));
                    Double distance = calculateDistance(new ZipCodeDAO().findByZip(zip), city);
                    distanceMap.put(distance, region.get(regionKey));
                }
            }
            boolean isFirst = true;
            Double shortestDistance = 0d;
            for (Map.Entry<Double, String> entry : distanceMap.entrySet()) {
                Double miles = entry.getKey();
                if (miles != 0d) {
                    if (topFiveCount > 4) {
                        break;
                    } else {
                        if (isFirst) {
                            topFiveMap.put(entry.getValue(), "<font color='#347C2C'>" + entry.getValue() + " </font>");
                            shortestDistance = miles;
                            isFirst = false;
                        } else {
                            topFiveMap.put(entry.getValue(), "<font color='#C35617'>" + entry.getValue() + " </font>");
                        }
                    }
                    topFiveCount++;
                }
            }
            if (shortestDistance != 0d) {
                Zipcode zipCode = new ZipCodeDAO().findByZip(zip);
                String cyYardCity = new UnLocationDAO().getNearByCyYardCity(zip, shortestDistance);
                if (null != zipCode && CommonUtils.isNotEmpty(cyYardCity)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("<font color='red' style='font-weight: bold;font-family: Arial;font-size: 13px;'>***NO RAMP RATE AVAILABLE FROM THE CLOSEST CY IN <a style = 'background:yellow'>");
                    sb.append(cyYardCity);
                    sb.append(" FROM ");
                    sb.append(zipCode.getCity());
                    sb.append(".</a> YOU MUST CONTACT A STEAMSHIP LINE TO HANDLE THE <br> DOOR MOVE***</font><br></span>");
                    request.setAttribute("cyYardMessage", sb.toString());
                }
            }
        } else {
            for (Object key : keySet) {
                totalCount = totalCount + regionMap.get(key).size();
            }
        }
        totalCount = Math.round(totalCount / 4);
        html.append("<table align='left'>");
        html.append("<tr>");
        int count = 0;
        boolean isRegion = false;
        String city = null;
        for (Object key : keySet) {
            isRegion = true;
            Map<String, String> region = regionMap.get(key);
            Set regionKeySet = region.keySet();
            for (Object regionKey : regionKeySet) {
                if (count == totalCount + 1) {
                    count = 0;
                    html.append("</td></table>");
                }
                if (count == 0) {
                    html.append("<td align='top' height='100%' style='vertical-align:top'><table>");
                }
                if (isRegion) {
                    if (count == totalCount) {
                        count = 0;
                        html.append("</td></table>");
                        html.append("<td align='top' height='100%' style='vertical-align:top'><table>");
                    }
                    html.append("<tr class='textlabelsBold'> <td align='left' style='background:#DCDCDC;color:blue'><input type='checkbox' name='region' id='").append(key).append("' onclick=\"checkAndUnceckRegions('").append(key).append("','").append(key).append("')\">").append(key).append("</td></tr>");
                    isRegion = false;
                    count++;
                }
                city = region.get(regionKey);
                if (city.indexOf("/") > 0) {
                    city = region.get(regionKey).substring(0, region.get(regionKey).indexOf("/"));
                } else if (city.indexOf("(") > 0) {
                    city = region.get(regionKey).substring(0, region.get(regionKey).indexOf("("));
                }
                String origin = topFiveMap.get(region.get(regionKey));
                if (null != topFiveMap.get(region.get(regionKey))) {
                    html.append("<tr class='textlabelsBold'> <td align='left'><span class='top5'><input type='checkbox' name='originDestination' class='").append(key).append("' value='").append(regionKey).append("'></span><span id=\"").append(city).append("\">").append(origin);
                } else {
                    html.append("<tr class='textlabelsBold'> <td align='left'><input type='checkbox' name='originDestination' class='").append(key).append("' value='").append(regionKey).append("'><span id=\"").append(city).append("\">").append(region.get(regionKey));
                }
                if (CommonUtils.isNotEmpty(zip)) {
                    Double distance = calculateDistance(new ZipCodeDAO().findByZip(zip), region.get(regionKey).substring(region.get(regionKey).indexOf("(") + 1, region.get(regionKey).indexOf(")")));
                    if (distance != 0d) {
                        html.append("<span id='distance' style='color:black;background:#CCEBFF;align:right'>").append(nf.format(distance)).append(" mi</span>");
                    }
                }
                html.append("</span></td></tr>");
                allCity.put(region.get(regionKey));
                count++;
            }
        }
        allCity.put("ABC-123");
        request.setAttribute("cityList", allCity);
        html.append("</tr>");
        html.append("</table>");
        return html.toString();
    }

    public String[] getAllSynonymousCity(String destPortId, String originPortId) throws Exception {
        return portsDAO.getAllSynonymousCity(destPortId, originPortId);
    }

    public String getAllCountryPorts(String portId) throws Exception {
        return portsDAO.getAllCountryPorts(portId);
    }

    public String[] fillFreeFormat() throws Exception {
        return new FclBlBC().wordWrap(FclBlConstants.FREE_FORMAT_STRING);
    }

    public boolean validateSpecialEquipment(String quoteId, String unitType, String desc, String action) throws Exception {
        GenericCode genericCode = new GenericCodeDAO().findByCodeDesc(unitType);
        List chargesList = new ArrayList();
        if (CommonUtils.isNotEmpty(quoteId) && null != genericCode) {
            int id = Integer.parseInt(quoteId);
            if ("add".equalsIgnoreCase(action)) {
                chargesList = new ChargesDAO().findByPropertyUsingQuoteId("specialEquipmentUnit", genericCode.getCode(), "qouteId", id);
            } else {
                chargesList = new ChargesDAO().getSpecialEquipmentCharges(id, genericCode.getCode(), desc);
            }
        }
        if (!chargesList.isEmpty()) {
            return true;
        }
        return false;
    }

    public boolean validateSpecialEquipment(String quoteId) throws Exception {
        List chargesList = new ArrayList();
        if (CommonUtils.isNotEmpty(quoteId)) {
            chargesList = new ChargesDAO().getChargesforSpecialEquipment(Integer.parseInt(quoteId));
        }
        if (!chargesList.isEmpty()) {
            return true;
        }
        return false;
    }

    public List orderExpandList(List ratesList) throws Exception {
        TreeMap<String, Charges> map = new TreeMap<String, Charges>();
        List resultList = new ArrayList();
        for (Iterator it = ratesList.iterator(); it.hasNext();) {
            Charges charges = (Charges) it.next();
            String flag = "";
            String unitFlag = "";
            GenericCode genericCode = new GenericCodeDAO().findById(Integer.parseInt(charges.getUnitType()));
            if (CommonUtils.isNotEmpty(charges.getSpecialEquipmentUnit())) {
                unitFlag = charges.getSpecialEquipmentUnit();
                if ("OCNFRT".equals(charges.getChargeCodeDesc())) {
                    flag = "BA";
                } else if (CommonUtils.isNotEmpty(charges.getChargeFlag())) {
                    flag = charges.getChargeFlag();
                } else {
                    flag = "B";
                }
            } else {
                if (null != genericCode) {
                    unitFlag = genericCode.getCode() + genericCode.getCode();
                }
                if ("OCNFRT".equals(charges.getChargeCodeDesc())) {
                    flag = "AA";
                } else if (CommonUtils.isNotEmpty(charges.getChargeFlag())) {
                    flag = charges.getChargeFlag();
                } else {
                    flag = "A";
                }
            }
            map.put(charges.getUnitType() + flag + unitFlag + charges.getChargeCodeDesc(), charges);
        }
        Set keySet = map.keySet();
        for (Iterator it = keySet.iterator(); it.hasNext();) {
            String key = (String) it.next();
            resultList.add(map.get(key));
        }
        return resultList;
    }

    public CustGeneralDefault getDefaultDetails(String accountNumber) throws Exception {
        return new CustGeneralDefaultDAO().getGeneralDefaultByAccountNumber(accountNumber, "Y");
    }

    public String getDefaultDetailsAlert(String accountNumber) throws Exception {
        return new CustGeneralDefaultDAO().getDefaultDetailsAlert(accountNumber);
    }

    public String getCreditStatus(String accountNumber) throws Exception {
        return new CustomerAccountingDAO().getCreditStatus(accountNumber);
    }

    public boolean getChargesAvailability(String quoteId) throws Exception {
        if (CommonUtils.isNotEmpty(quoteId)) {
            List l = new ChargesDAO().findByProperty("qouteId", Integer.parseInt(quoteId));
            if (!l.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public void createSuspendedCreditNotes(String fileNo, String comments, HttpServletRequest request) throws Exception {
        User loginUser = (User) request.getSession().getAttribute("loginuser");
        Notes note = new Notes();
        note.setModuleId("FILE");
        note.setModuleRefId(fileNo);
        note.setNoteType(NotesConstants.NOTES_TYPE_MANUAL);
        note.setUpdateDate(new Date());
        note.setUpdatedBy(loginUser.getLoginName());
        note.setItemName("");
        note.setNoteDesc(comments.toUpperCase());
        new NotesDAO().save(note);
    }

    public List getCyYardCity() throws Exception {
        List<String> list = null;
        List l = new UnLocationDAO().findByProperty("cyYard", "Y");
        if (!l.isEmpty()) {
            list = new ArrayList<String>();
            for (Object object : l) {
                UnLocation unLocation = (UnLocation) object;
                String portId = new PortsDAO().getPortId(unLocation.getUnLocationCode());
                if (null != unLocation && null != unLocation.getUnLocationName()) {
                    if (null != unLocation.getStateId()) {
                        list.add(unLocation.getUnLocationName() + "/" + unLocation.getStateId().getCode() + "(" + unLocation.getUnLocationCode() + ")" + portId);
                    } else {
                        list.add(unLocation.getUnLocationName() + "(" + unLocation.getUnLocationCode() + ")" + portId);
                    }
                }
            }
        }
        return list;
    }

    public Double calculateDistance(Zipcode zip, String origin) throws Exception {
        UnLocation unLocation = new UnLocationDAO().getUnlocation(origin);
        if (null != unLocation && null != zip && CommonUtils.isNotEmpty(unLocation.getLat()) && CommonUtils.isNotEmpty(zip.getLat())) {
            return distFromDoor(zip, Double.parseDouble(unLocation.getLat()), Double.parseDouble(unLocation.getLng()));
        }
        return 0d;
    }

    public Double distFromDoor(Zipcode doorOrigin, Double lat, Double lng) throws Exception {
        DecimalFormat df = new DecimalFormat("#.##");
        if (null != lat && null != lng) {
            Double lat1 = Double.parseDouble(doorOrigin.getLat());
            Double lng1 = Double.parseDouble(doorOrigin.getLng());
            Double earthRadius = 3958.75;
            Double dLat = Math.toRadians(lat - lat1);
            Double dLng = Math.toRadians(lng - lng1);
            Double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                    + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat))
                    * Math.sin(dLng / 2) * Math.sin(dLng / 2);
            Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            Double dist = earthRadius * c;
            return dist;
        }
        return 0d;
    }

    public String getAdjustValue(String fileNumber) throws Exception {
        return new ChargesDAO().getAdjustedValue(fileNumber);
    }

    public List<LabelValueBean> getrampNvoMoveList() throws Exception {
        List<LabelValueBean> moveTypes = new ArrayList<LabelValueBean>();
        moveTypes.add(new LabelValueBean("Select", "00"));
        List<GenericCode> types = new GenericCodeDAO().getNvoMoveTypes(48, true);
        for (GenericCode genericCode : types) {
            moveTypes.add(new LabelValueBean(genericCode.getCodedesc(), genericCode.getCodedesc()));
        }
        return moveTypes;
    }

    public List<LabelValueBean> getNvoMoveList() throws Exception {
        List<LabelValueBean> moveTypes = new ArrayList<LabelValueBean>();
        moveTypes.add(new LabelValueBean("Select", "00"));
        List<GenericCode> types = new GenericCodeDAO().getNvoMoveTypes(48, false);
        for (GenericCode genericCode : types) {
            moveTypes.add(new LabelValueBean(genericCode.getCodedesc(), genericCode.getCodedesc()));
        }
        return moveTypes;
    }

    public String checkForCommodity(String commcode) throws Exception {
        String flagValue = "false";
        genericCodeDAO = new GenericCodeDAO();
        String field7 = genericCodeDAO.getByCodeAndCodetypeId("4", commcode, "field9");
        if ("Y".equals(field7)) {
            flagValue = "true";
        }
        return flagValue;
    }

    public String inlandCheck(String quoteId) throws Exception {
        return new QuotationDAO().getInland(quoteId);
    }

    public String intrmodRampCheck(String quoteId) throws Exception {
        String inland = "";
        Integer quotesId = Integer.parseInt(quoteId);
        QuotationBC quotationBC = new QuotationBC();
        if (CommonFunctions.isNotNullOrNotEmpty(quotationBC.cheackChargeCode("chgCode",
                "INTERMODAL RAMP", "qouteId", quotesId))) {
            inland = "Y";
        } else {
            inland = "N";
        }
        return inland;
    }

    // this method updates number of container, while changing the numbers field in import screen
    public void updateNoOfContainer(String unitValue, String quoteId, String noOfContainer)
            throws Exception {
        GenericCode genericCode = genericCodeDAO.findByCodeDesc(unitValue);
        new ChargesDAO().updateNoOfContainer(genericCode.getId(), quoteId, noOfContainer);
    }

    public String intRampCheck(String quoteId) throws Exception {
        String intramp = "";
        Integer quotesId = Integer.parseInt(quoteId);
        QuotationBC quotationBC = new QuotationBC();
        String checkInterModal = new PropertyDAO().getProperty("intermodal.missing.alert.enabled");  // adding for resticate the alert of intermodal
        if (CommonFunctions.isNotNullOrNotEmpty(quotationBC.cheackChargeCode("chargeCodeDesc", "INTRAMP", "qouteId", quotesId))) {
            intramp = "Y";
        } else {
            if (CommonUtils.isNotEmpty(checkInterModal) && ("ON".equalsIgnoreCase(checkInterModal) || "Y".equalsIgnoreCase(checkInterModal))) {
                intramp = "N";
            } else {
                intramp = "Y";
            }
        }
        return intramp;
    }

    public boolean checkCustomerNotes(String acctNo) throws Exception {
        return new NotesDAO().isCustomerNotes(acctNo);
    }

    public String isCommodityChangeApplyForThisCustomer(String acctNo) throws Exception {
        return new GeneralInformationDAO().isCommodityChangeApplyForThisCustomer(acctNo);
    }

    public boolean checkUnitType(String quoteId) throws Exception {
        List unitType = new ChargesDAO().getGroupByUnitType(Integer.parseInt(quoteId));
        return CommonUtils.isNotEmpty(unitType);
    }

    public void notesForSpotRates(String fileNo, String userName, String spotStatus) throws Exception {
        Quotation quotation = new QuotationDAO().findbyFileNo(fileNo);
        QuotationDAO quotationDAO = new QuotationDAO();
        if (spotStatus.equals("Y")) {
            ChargesDAO chargesDAO = new ChargesDAO();
            quotation.setSpotRate("Y");

            List<Charges> deferChargeList = chargesDAO.findByChargeCode(quotation.getQuoteId().toString(), "DEFER");
            for (Charges deferCharge : deferChargeList) {
                deferCharge.setStandardChk("on");
                deferCharge.setSpotRateAmt(deferCharge.getAmount());
                deferCharge.setSpotRateMarkUp(deferCharge.getMarkUp());
                chargesDAO.attachDirty(deferCharge);
            }

            new NotesBC().deleteAesDetails(fileNo, userName.toUpperCase(), "Spot Rate Changed from 'N' to 'Y'");
        } else {
            quotation.setSpotRate("N");
            quotationDAO.clearSpotCost(fileNo);
            new NotesBC().deleteAesDetails(fileNo, userName.toUpperCase(), "Spot Rate Changed from 'Y' to 'N'");

        }
        quotationDAO.update(quotation);
    }

    public String isNotSpotRate(String quoteId) throws Exception {
        return new ChargesDAO().isNotSpotRate(quoteId) ? "Spot Rate" : "No Spot Rate";
    }

    public String isContainDeferral(String quoteId) throws Exception {
        return new ChargesDAO().isContainDeferral(quoteId) ? "DEFERRAL" : "";
    }

    public String checkBrandForDestination(String destination) {
        String brandField = portsDAO.brandField(destination);
        return brandField;
    }
    
    public String checkBrandForClient(String acctno){
        String brandField = new TradingPartnerDAO().brandFieldForClient(acctno);
        return brandField;
    }
    public String checkBrandForQuote(String quoteNo) {
        String queryString = new QuotationDAO().brandValue(quoteNo);
        return queryString;

    }
    public String[] checkvendorForChassis(String fileNo) throws Exception{
        ChargesDAO chargesDao = new ChargesDAO();
         String[] chargesValues = chargesDao.checkvendorForChassisCharge(fileNo);
        return chargesValues;
     }
}
