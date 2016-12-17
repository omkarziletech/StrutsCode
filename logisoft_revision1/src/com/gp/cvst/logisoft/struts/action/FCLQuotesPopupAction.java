  /*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cvst.logisoft.struts.action;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.bc.fcl.BookingFclBC;
import com.gp.cong.logisoft.bc.fcl.QuotationBC;
import com.gp.cong.logisoft.bc.fcl.QuotationConstants;
import com.gp.cong.logisoft.beans.NonRates;
import com.gp.cong.logisoft.beans.Rates;
import com.gp.cong.logisoft.domain.FclBuy;
import com.gp.cong.logisoft.domain.FclBuyCost;
import com.gp.cong.logisoft.domain.FclBuyCostTypeRates;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.Ports;
import com.gp.cong.logisoft.hibernate.dao.FclBuyCostDAO;
import com.gp.cong.logisoft.hibernate.dao.FclBuyDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.util.DBUtil;
import com.gp.cvst.logisoft.beans.MultiChargesOrderBean;
import com.gp.cvst.logisoft.domain.Charges;
import com.gp.cvst.logisoft.domain.MultiQuote;
import com.gp.cvst.logisoft.domain.MultiQuoteCharges;
import com.gp.cvst.logisoft.domain.Quotation;
import com.gp.cvst.logisoft.hibernate.dao.ChargesDAO;
import com.gp.cvst.logisoft.hibernate.dao.MultiQuoteChargesDao;
import com.gp.cvst.logisoft.hibernate.dao.MultiQuoteDao;
import com.gp.cvst.logisoft.hibernate.dao.QuotationDAO;
import com.gp.cvst.logisoft.struts.form.fclQuotesPopupForm;
import com.logiware.action.EventAction;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

/**
 * MyEclipse Struts Creation date: 07-07-2008
 *
 * XDoclet definition:
 *
 * @struts.action path="/fclQuotes" name="fclQuotesPopupForm" scope="request"
 * validate="true"
 * @struts.action-forward name="success"
 * path="/jsps/fclQuotes/fclquotesPopup.jsp"
 */
public class FCLQuotesPopupAction extends EventAction {
    /*
     * Generated Methods
     * Method execute
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        fclQuotesPopupForm fclQuotesPopupForm = (fclQuotesPopupForm) form;// TODO Auto-generated method stub
        HttpSession session = request.getSession();
        super.registerEvent(form, request, response);
        String buttonValue = fclQuotesPopupForm.getButtonValue();
        String forwardName = "";
        GenericCode genericCode = null;
        FclBuyDAO fclBuyDAO = new FclBuyDAO();
        String noOfDays = fclQuotesPopupForm.getNoOfDays();
        String protsId = fclQuotesPopupForm.getPortId();
        String originId = fclQuotesPopupForm.getOriginId();
        String butval = fclQuotesPopupForm.getButval();
        String ssline = fclQuotesPopupForm.getSsline();
        QuotationBC quotationBC = new QuotationBC();
        request.setAttribute("butval", butval);
        String commId = fclQuotesPopupForm.getCommodId();
        String commdValue = fclQuotesPopupForm.getCommodityId();
        String rampCity = fclQuotesPopupForm.getRampCity();
        String cargoOrigins = fclQuotesPopupForm.getCargoOrigins();
        String destinationPort = fclQuotesPopupForm.getDestinationPort();
        QuotationDAO quotationDAO = new QuotationDAO();
        String hazmat = fclQuotesPopupForm.getHazmat();
        request.setAttribute("hazmat", hazmat);
        MessageResources messageResources = getResources(request);
        if (request.getParameter("QuickReview") != null && request.getParameter("QuickReview").equals("QuickReview")) {
            request.setAttribute("QuickReview", "hiddensubmit");
        }
        DBUtil dbUtil = new DBUtil();
        request.setAttribute("RATESLIST", dbUtil.getUnitListForFclaRates(new Integer(38), "yes", messageResources));
        List temList = new ArrayList();
        String Origin = null;
        String destination = null;

        request.setAttribute("fileNo", request.getParameter("fileNo"));
        request.setAttribute("cargoOrigins", cargoOrigins);
        request.setAttribute("destinationPort", destinationPort);
        request.setAttribute("commdValue", commdValue);
        request.setAttribute("rampCity", rampCity);

        if (request.getParameter("buttonValue") != null && (request.getParameter("buttonValue").equals("getRates")
                || request.getParameter("buttonValue").equals("getRatesBooking") || "getRatesCopyQuote".equals(request.getParameter("buttonValue")))) {
            buttonValue = request.getParameter("buttonValue");

            if (request.getParameter("noOfDays") != null) {
                noOfDays = request.getParameter("noOfDays");
            }
            request.setAttribute("noOfDays", noOfDays);
            if (request.getParameter("destn") != null) {
                destinationPort = request.getParameter("destn");
                request.setAttribute("pId", destinationPort);
            }
            if (request.getParameter("origin") != null) {
                cargoOrigins = request.getParameter("origin");
                request.setAttribute("oId", cargoOrigins);
            }

            if (request.getParameter("comid") != null) {
                commdValue = request.getParameter("comid");
                request.setAttribute("commdValue", commdValue);
                request.setAttribute("cId", commdValue);
            }

            if (request.getParameter("hazmat") != null) {
                hazmat = request.getParameter("hazmat");
                request.setAttribute("hazmat", hazmat);
            }
        }
        request.setAttribute("buttonValue", buttonValue);
        if (cargoOrigins != null) {
            // it will tokenize unlocation code and description and fetch the id of unlocation for origin.
            Origin = quotationBC.findForManagement(cargoOrigins, null);
        }
        // it will tokenize unlocation code and description and fetch the id of unlocation for destionation 
        destination = quotationBC.findForManagement(destinationPort, request);
        if (commdValue != null && !commdValue.equals("")) {
            genericCode = quotationBC.findForGenericCode(commdValue);
        }

        if (!buttonValue.equals("cancel")) {
            // if we hit on Get rates or quick rates this function wil get called.. 
            if (buttonValue.equals("getRates")) {
//                String DATE_FORMAT = "yyyy-MM-dd";
                Date d = new Date();
//                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT);
                Calendar c1 = Calendar.getInstance();
                int year = 1900 + d.getYear();
                int month = d.getMonth();
                c1.set(year, month, d.getDate()); // 1999 jan 20
                if (noOfDays == null) {
                    noOfDays = "30"; // currently not in used For ECI
                }
                if (noOfDays.length() != 0) {
                    c1.add(Calendar.DATE, Integer.parseInt(noOfDays)); // currently not in used For ECI
                }
                List fclBuyLIst = new ArrayList();
                // by passing these arguments we are the result from view.. for rates
                String destinationCode = "";
                String markUp = "markup1";
                if (CommonUtils.isNotEmpty(destinationPort) && destinationPort.lastIndexOf("(") != -1
                        && destinationPort.lastIndexOf(")") != -1) {
                    destinationCode = destinationPort.substring(destinationPort.lastIndexOf("(") + 1, destinationPort.lastIndexOf(")"));
                    Ports ports = new PortsDAO().getPorts(destinationCode);
                    if (null != ports && null != ports.getRegioncode() && "Y".equalsIgnoreCase(ports.getRegioncode().getField3())) {
                        markUp = "markup2";
                    }
                }
                List nonRatesList = fclBuyDAO.findRatesForOtherCommodity(genericCode.getId(), markUp);
                Integer comId = genericCode.getId();
                if (CommonUtils.isNotEmpty(nonRatesList)) {
                    for (Iterator iterator = nonRatesList.iterator(); iterator.hasNext();) {
                        NonRates nonRates = (NonRates) iterator.next();
                        comId = nonRates.getBaseCommodity();
                        break;
                    }
                }
                fclBuyLIst = fclBuyDAO.findRates(null!=Origin?Origin.trim():"", destination.trim(), comId.toString().trim());
                // setting into request for request to display on popup....
                request.setAttribute("cargoOrigins", cargoOrigins);
                request.setAttribute("destinationPort", destinationPort);
                request.setAttribute("commodity", genericCode.getCode());
                if (CommonUtils.isNotEmpty(nonRatesList)) {
                    fclBuyLIst = fclBuyDAO.addingAmountforOtherCommodity(fclBuyLIst, nonRatesList);
                }
                if (fclBuyLIst.size() > 0) {
                    List tempList = fclBuyDAO.getSslineId3(fclBuyLIst, c1.getTime(), messageResources, hazmat);
                    request.setAttribute("compressList", tempList);
                    List collapseList = fclBuyDAO.getSslineId4(fclBuyLIst, c1.getTime(), messageResources, hazmat);
                    request.setAttribute("collapseList", collapseList);// ocean list:-----------
                    temList = fclBuyDAO.getSslineId1(fclBuyLIst, c1.getTime(), messageResources, hazmat);
                    request.setAttribute(QuotationConstants.RATESLIST, temList);
                }
                Map fclBuyMap = new HashMap<Integer, FclBuy>();
                for (Iterator iter = fclBuyLIst.iterator(); iter.hasNext();) {
                    Rates fclBuy = (Rates) iter.next();
                    if (!fclBuyMap.containsKey(fclBuy.getFclStdId())) {
                        fclBuyMap.put(fclBuy.getFclStdId(), fclBuy);
                    }
                }
                request.setAttribute("fclBuyMap", fclBuyMap);
                String msg = "";
                if (temList.size() > 0) {
                    if (hazmat != null && hazmat.equals("Y")) {
                        request.setAttribute("msg", "Includes Hazardous");
                    }
                } else {
                    msg = "No Rates Exist for this Route Origin " + cargoOrigins + " and Destination " + destinationPort;
                    request.setAttribute("msg", msg);
                    request.setAttribute("cargoOrigins", cargoOrigins);
                    request.setAttribute("destinationPort", destinationPort);
                }
                request.setAttribute("pId", protsId);
                request.setAttribute("oId", originId);
                request.setAttribute("cId", commId);

            } else if (buttonValue.equals("getRatesBooking") || "getRatesCopyQuote".equals(buttonValue)) {
                
           if(request.getParameter("multiQuoteFlag") == null){ // -- for quote
                if (request.getParameter("destn") != null) {
                    destinationPort = request.getParameter("destn");
                }
                if (request.getParameter("origin") != null) {
                    cargoOrigins = request.getParameter("origin");
                }
                if (request.getParameter("copyQuote") != null) {
                    request.setAttribute("copyQuote", request.getParameter("copyQuote"));
                }
                request.setAttribute("cargoOrigins", cargoOrigins);
                request.setAttribute("destinationPort", destinationPort);
                String carrier = request.getParameter("carrier");
                request.setAttribute("carrier", carrier);

                String carrier1 = "";
                if (carrier != null && carrier.indexOf("//") != -1) {
                    String tempCarrier[] = carrier.split("//");
                    carrier1 = tempCarrier[1];
                }
                String quoteDate = request.getParameter("quoteDate");
                String bookingDate = request.getParameter("bookingDate");
                Map ofrAmountMap = quotationBC.getOFRAmountConvertToMap(request.getParameter("OFRRollUpAmount"));
                Map ofrAmountCollapseMap = quotationBC.getOFRAmountConvertToMap(request.getParameter("oceanFrightForCollapse"));
                String fileNo = request.getParameter("fileNo");
                request.setAttribute("fileNo", "04-" + fileNo);

                HashMap hashMap = new HashMap();
                HashMap unitHashMap = new HashMap();
                if (Origin != null && destination != null && genericCode != null && carrier1 != null && quoteDate != null) {
                    String markUp = "markup1";
                    if (CommonUtils.isNotEmpty(destinationPort) && destinationPort.lastIndexOf("(") != -1
                            && destinationPort.lastIndexOf(")") != -1) {
                        String destinationCode = destinationPort.substring(destinationPort.lastIndexOf("(") + 1, destinationPort.lastIndexOf(")"));
                        Ports ports = new PortsDAO().getPorts(destinationCode);
                        if (null != ports && null != ports.getRegioncode() && "Y".equalsIgnoreCase(ports.getRegioncode().getField3())) {
                            markUp = "markup2";
                        }
                    }
                    Integer comId = genericCode.getId();
                    List nonRatesList = fclBuyDAO.findRatesForOtherCommodity(genericCode.getId(), markUp);
                    if (CommonUtils.isNotEmpty(nonRatesList)) {
                        for (Iterator iterator = nonRatesList.iterator(); iterator.hasNext();) {
                            NonRates nonRates = (NonRates) iterator.next();
                            comId = nonRates.getBaseCommodity();
                            break;
                        }
                    }
                    List fclBuyLIst = fclBuyDAO.findRates2(Origin.trim(), destination.trim(), comId.toString(), carrier1, quoteDate, bookingDate);
                    if (fclBuyLIst.isEmpty()) {
                        request.setAttribute("msg", "BkgRates");
                    }
                    request.setAttribute("commodity", genericCode.getCode());
                    if (fclBuyLIst.size() > 0) {
                        List<FclBuyCost> tempList = null, collapseList = null;
                        List<FclBuyCost> listOfFclBuyCostForbooking = null;
                        List<FclBuyCost> list = new ArrayList<FclBuyCost>();
                        Ports destPort = new PortsDAO().findById(Integer.parseInt(destination.trim()));
                        String region = null != destPort && null != destPort.getRegioncode() ? destPort.getRegioncode().getField3(): "";
                        List otherCommodityList = new FclBuyCostDAO().getOtherCommodity(genericCode.getCode(), null);
                        if (request.getParameter("page") != null) {
                            BookingFclBC bookingFclBC = new BookingFclBC();
                            listOfFclBuyCostForbooking = bookingFclBC.getListOfCharges(fclBuyLIst, messageResources, hazmat, request, fileNo, otherCommodityList, region);
                            list.addAll(listOfFclBuyCostForbooking);
                            tempList = fclBuyDAO.getCompressListForBooking(list, messageResources, ofrAmountCollapseMap);
                            collapseList = fclBuyDAO.getCollapseListForBooking(list, messageResources, ofrAmountMap);
                        } else {
                            // getting call from quote
                            Quotation quotation = quotationDAO.getFileNoObject(fileNo);
                            ChargesDAO chargesDAO = new ChargesDAO();
                            List bookingFclUnitsList = chargesDAO.getQuoteId(quotation.getQuoteId());
                            request.setAttribute("quoteDate", quotation.getQuoteDate());
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
                            listOfFclBuyCostForbooking = fclBuyDAO.getSslineIdForBooking(fclBuyLIst, messageResources, hazmat, hashMap, unitHashMap, request, quotation, otherCommodityList, region);
                            list.addAll(listOfFclBuyCostForbooking);
                            tempList = fclBuyDAO.getCompressListForBooking(list, messageResources, ofrAmountCollapseMap);
                            collapseList = fclBuyDAO.getCollapseListForBooking(list, messageResources, ofrAmountMap);
                        }
                        LinkedList compressList = new LinkedList();
                        for (Iterator iterator = tempList.iterator(); iterator.hasNext();) {
                            FclBuyCost tempRates = (FclBuyCost) iterator.next();
                            if ("OCEAN FREIGHT".equalsIgnoreCase(tempRates.getCostCode())) {
                                compressList.add(0, tempRates);
                            } else {
                                compressList.add(tempRates);
                            }
                        }
                        //For rate change alert notes
                        LinkedList expandList = new LinkedList();
                        List<String> costCodeList = new ArrayList();
                        List<String> costTotalList = new ArrayList();
                        List<String> oldCostTotalList = new ArrayList<String>();
                        NumberFormat numberFormat = new DecimalFormat("###,###,##0.00");
                        if (null != listOfFclBuyCostForbooking) {
                            for (Iterator iterator = listOfFclBuyCostForbooking.iterator(); iterator.hasNext();) {
                                FclBuyCost tempRates = (FclBuyCost) iterator.next();
                                for (int i = 0; i < tempRates.getUnitTypeList().size(); i++) {
                                    FclBuyCostTypeRates fclBuyCostTypeRates = (FclBuyCostTypeRates) tempRates.getUnitTypeList().get(i);
                                    Double total = 0.00, oldAmountPlusMarkup = 0.00;
                                    String newTotal = "", oldTotal = "", markup = "", amount = "", oldAmount = "", oldMarkup = "";
                                    if (null != fclBuyCostTypeRates.getMarkup()) {
                                        markup = numberFormat.format(fclBuyCostTypeRates.getMarkup()).replace(",", "");
                                    }
                                    if (null != fclBuyCostTypeRates.getActiveAmt()) {
                                        amount = numberFormat.format(fclBuyCostTypeRates.getActiveAmt()).replace(",", "");
                                    }
                                    if (!amount.trim().equals("") || !markup.trim().equals("")) {
                                        total = Double.parseDouble(amount) + Double.parseDouble(markup);
                                        newTotal = numberFormat.format(total);
                                    }
                                    if (null != fclBuyCostTypeRates.getOldAmount()) {
                                        oldAmount = numberFormat.format(fclBuyCostTypeRates.getOldAmount()).replace(",", "");
                                    }
                                    if (null != fclBuyCostTypeRates.getOldMarkUp()) {
                                        oldMarkup = numberFormat.format(fclBuyCostTypeRates.getOldMarkUp()).replace(",", "");
                                    }
                                    if (!oldAmount.trim().equals("") || !oldMarkup.trim().equals("")) {
                                        oldAmountPlusMarkup = Double.parseDouble(oldAmount) + Double.parseDouble(oldMarkup);
                                        oldTotal = numberFormat.format(oldAmountPlusMarkup);
                                    }

                                    if ("OCEAN FREIGHT".equalsIgnoreCase(tempRates.getCostCode())) {
                                        expandList.add(0, tempRates);
                                    } else {
                                        expandList.add(tempRates);
                                    }
                                    if (CommonUtils.isEmpty(newTotal)) {
                                        newTotal = "0.00";
                                    }
                                    if (CommonUtils.isEmpty(oldTotal)) {
                                        oldTotal = "0.00";
                                    }
                                    costTotalList.add(newTotal);
                                    oldCostTotalList.add(oldTotal);
                                    costCodeList.add(tempRates.getCostId().getCode());
                                }
                            }
                        }
                        session.setAttribute("ratesChangeCostCodeList", costCodeList);
                        session.setAttribute("costTotalList", costTotalList);
                        session.setAttribute("oldCostTotalList", oldCostTotalList);
                        request.setAttribute("compressList", compressList);
                        request.setAttribute("collapseList", collapseList);
                        request.setAttribute(QuotationConstants.RATESLIST, expandList);

                        //:------------------------ for booking.........
                    } else {
                        List ratesPopupList = new ArrayList();
                        ratesPopupList.add("");
                        ratesPopupList.add("");
                        ratesPopupList.add("");
                        ratesPopupList.add("");
                        ratesPopupList.add("");
                        ratesPopupList.add("");
                        ratesPopupList.add("oldgetRatesBKG");
                        request.setAttribute("ratesPopupList", ratesPopupList);
                    }
                }
            }
            else if (request.getParameter("multiQuoteFlag").equalsIgnoreCase("multiQuote")) {// -- for multiQuote
                String fileNo = request.getParameter("fileNo");
                String multiQuoteId = request.getParameter("multiQuoteId");
                Quotation quotation = quotationDAO.getFileNoObject(fileNo);
                MultiQuote multiQuote = new MultiQuoteDao().findById(Long.parseLong(multiQuoteId));
                request.setAttribute("cargoOrigins", multiQuote.getOrigin());
                request.setAttribute("destinationPort", multiQuote.getDestination());
                genericCode = new GenericCodeDAO().findById(Integer.parseInt(multiQuote.getCommodity()));
                request.setAttribute("commdValue", genericCode.getCode());
                // Dublicate value
                request.setAttribute("oId", multiQuote.getOrigin());
                request.setAttribute("pId", multiQuote.getDestination());
                request.setAttribute("cId", genericCode.getCode());
                request.setAttribute("fileNo", "04-" + fileNo);
                request.setAttribute("quoteDate", quotation.getQuoteDate());
                request.setAttribute("destination", multiQuote.getDestination());
                request.setAttribute("commodity", genericCode.getCode());
                request.setAttribute("carrier", multiQuote.getCarrier()+"//"+multiQuote.getCarrierNo());
                request.setAttribute("multiQuoteId", multiQuote.getId());
               HashMap hashMap = new HashMap();
               HashMap unitHashMap = new HashMap();
               MultiQuoteChargesDao multiQuoteChargesDao = new MultiQuoteChargesDao();
               List<MultiChargesOrderBean> ocnfrt = multiQuoteChargesDao.getOceanfrt(multiQuote.getId());
               List<MultiChargesOrderBean> bundleCharges = multiQuoteChargesDao.getChargesListForPdf(multiQuote.getId());
               Map ofrAmountMap = quotationBC.getOcnfrtMultiQuote(ocnfrt);//Ocnfrt + Total all
               Map ofrAmountCollapseMap = quotationBC.getBundleOcnfrtMultiQuote(bundleCharges);//ocnfrt  bundle charge
               List bookingFclUnitsList = multiQuote.getMultiQuoteCharges();
               for (Iterator iterator = bookingFclUnitsList.iterator(); iterator.hasNext();) {
                   MultiQuoteCharges mQcharges = (MultiQuoteCharges) iterator.next();
                   if (mQcharges.getUnitType() != null) {
                       if (unitHashMap.get(mQcharges.getUnitType()) == null) {
                           unitHashMap.put(mQcharges.getUnitType(), mQcharges.getUnitType());
                       }
                       hashMap.put(mQcharges.getChargeCodeDesc() + "-" + mQcharges.getUnitType(), mQcharges);
                   } else {
                       hashMap.put(mQcharges.getChargeCodeDesc(), mQcharges);
                   }
               }

               List fclBuyLIst = fclBuyDAO.findForSearchFclBuyRatesForCompressList2(multiQuote.getOriginCode(), multiQuote.getDestinationCode(), multiQuote.getCommodity(), multiQuote.getCarrierNo());
               if (fclBuyLIst.isEmpty()) {
                   request.setAttribute("msg", "BkgRates");
               }
               if (fclBuyLIst.size() > 0) {
                   Ports destPort = new PortsDAO().findById(Integer.parseInt(multiQuote.getDestinationCode()));
                   String region = null != destPort && null != destPort.getRegioncode() ? destPort.getRegioncode().getField3() : "";
                   List otherCommodityList = new FclBuyCostDAO().getOtherCommodity(genericCode.getCode(), null);
                   List<FclBuyCost> tempList = null, collapseList = null;
                   List<FclBuyCost> listOfFclBuyCostForbooking = null;
                   List<FclBuyCost> list = new ArrayList<FclBuyCost>();
                   listOfFclBuyCostForbooking = fclBuyDAO.getSslineIdForBooking(fclBuyLIst, messageResources, multiQuote.getHazmat(), hashMap, unitHashMap, request, quotation, otherCommodityList, region);
                   list.addAll(listOfFclBuyCostForbooking);
                   tempList = fclBuyDAO.getCompressListForBooking(list, messageResources, ofrAmountCollapseMap);
                   collapseList = fclBuyDAO.getCollapseListForBooking(list, messageResources, ofrAmountMap);

                   LinkedList compressList = new LinkedList();
                   for (Iterator iterator = tempList.iterator(); iterator.hasNext();) {
                       FclBuyCost tempRates = (FclBuyCost) iterator.next();
                       if ("OCEAN FREIGHT".equalsIgnoreCase(tempRates.getCostCode())) {
                           compressList.add(0, tempRates);
                       } else {
                           compressList.add(tempRates);
                       }
                   }
                   //For rate change alert notes
                   LinkedList expandList = new LinkedList();
                   List<String> costCodeList = new ArrayList();
                   List<String> costTotalList = new ArrayList();
                   List<String> oldCostTotalList = new ArrayList<String>();
                   NumberFormat numberFormat = new DecimalFormat("###,###,##0.00");
                   if (null != listOfFclBuyCostForbooking) {
                       for (Iterator iterator = listOfFclBuyCostForbooking.iterator(); iterator.hasNext();) {
                           FclBuyCost tempRates = (FclBuyCost) iterator.next();
                           for (int i = 0; i < tempRates.getUnitTypeList().size(); i++) {
                               FclBuyCostTypeRates fclBuyCostTypeRates = (FclBuyCostTypeRates) tempRates.getUnitTypeList().get(i);
                               Double total = 0.00, oldAmountPlusMarkup = 0.00;
                               String newTotal = "", oldTotal = "", markup = "", amount = "", oldAmount = "", oldMarkup = "";
                               if (null != fclBuyCostTypeRates.getMarkup()) {
                                   markup = numberFormat.format(fclBuyCostTypeRates.getMarkup()).replace(",", "");
                               }
                               if (null != fclBuyCostTypeRates.getActiveAmt()) {
                                   amount = numberFormat.format(fclBuyCostTypeRates.getActiveAmt()).replace(",", "");
                               }
                               if (!amount.trim().equals("") || !markup.trim().equals("")) {
                                   total = Double.parseDouble(amount) + Double.parseDouble(markup);
                                   newTotal = numberFormat.format(total);
                               }
                               if (null != fclBuyCostTypeRates.getOldAmount()) {
                                   oldAmount = numberFormat.format(fclBuyCostTypeRates.getOldAmount()).replace(",", "");
                               }
                               if (null != fclBuyCostTypeRates.getOldMarkUp()) {
                                   oldMarkup = numberFormat.format(fclBuyCostTypeRates.getOldMarkUp()).replace(",", "");
                               }
                               if (!oldAmount.trim().equals("") || !oldMarkup.trim().equals("")) {
                                   oldAmountPlusMarkup = Double.parseDouble(oldAmount) + Double.parseDouble(oldMarkup);
                                   oldTotal = numberFormat.format(oldAmountPlusMarkup);
                               }

                               if ("OCEAN FREIGHT".equalsIgnoreCase(tempRates.getCostCode())) {
                                   expandList.add(0, tempRates);
                               } else {
                                   expandList.add(tempRates);
                               }
                               if (CommonUtils.isEmpty(newTotal)) {
                                   newTotal = "0.00";
                               }
                               if (CommonUtils.isEmpty(oldTotal)) {
                                   oldTotal = "0.00";
                               }
                               costTotalList.add(newTotal);
                               oldCostTotalList.add(oldTotal);
                               costCodeList.add(tempRates.getCostId().getCode());
                           }
                       }
                   }
                   session.setAttribute("ratesChangeCostCodeList", costCodeList);
                   session.setAttribute("costTotalList", costTotalList);
                   session.setAttribute("oldCostTotalList", oldCostTotalList);
                   request.setAttribute("compressList", compressList);
                   request.setAttribute("collapseList", collapseList);
                   request.setAttribute(QuotationConstants.RATESLIST, expandList);
                   }
                }
            }
        }
        if (buttonValue.equals("getindex") || buttonValue.equals("newgetRates")
                || buttonValue.equals("newgetRatesBKG") || buttonValue.equals("oldgetRatesBKG") || "getRateChangeCopyQuote".equals(buttonValue)) {
            List ratesPopupList = new ArrayList();
            ratesPopupList.add(ssline);
            ratesPopupList.add(fclQuotesPopupForm.getUnitselected());
            ratesPopupList.add(fclQuotesPopupForm.getSelectedCheck());
            ratesPopupList.add(fclQuotesPopupForm.getCargoOrigins());
            ratesPopupList.add(fclQuotesPopupForm.getDestinationPort().replace("'", "&&"));
            ratesPopupList.add(fclQuotesPopupForm.getCommodityId());
            ratesPopupList.add(buttonValue);
            request.setAttribute("ratesPopupList", ratesPopupList);
            request.setAttribute("buttonValue", "completed");
            if (fclQuotesPopupForm.getMultiQuoteId() != null && !fclQuotesPopupForm.getMultiQuoteId().isEmpty()) {//MultiQuote
            request.setAttribute("multiquote", fclQuotesPopupForm.getMultiQuoteId());
            } else {
            request.setAttribute("multiquote", null);
            }
        }
        if (buttonValue.equals("restart")) {
            request.setAttribute("QuickReview", "QuickReview");
        }
        forwardName = "success";
        return mapping.findForward(forwardName);
    }
}