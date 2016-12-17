/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.domestic;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.lcl.webservices.PrimaryFrieghtXmlParser;
import com.gp.cong.lcl.webservices.dom.Carrier;
import com.gp.cong.lcl.webservices.dom.Destination;
import com.gp.cong.lcl.webservices.dom.Origin;
import com.gp.cong.lcl.webservices.dom.Rates;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.ItemDAO;
import org.apache.struts.util.LabelValueBean;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.jobscheduler.EMailScheduler;
import com.gp.cong.logisoft.jobscheduler.MailPropertyReader;
import com.gp.cong.logisoft.reports.CTSQuotePdfCreator;
import com.gp.cong.logisoft.struts.action.lcl.LogiwareDispatchAction;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.beans.MailMessageVO;
import com.gp.cvst.logisoft.domain.CustAddress;
import com.gp.cvst.logisoft.hibernate.dao.CustAddressDAO;
import com.logiware.domestic.form.RateQuoteForm;
import java.io.File;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import java.util.*;

/**
 *
 * @author Sathiyapriya
 */
public class RateQuoteAction extends LogiwareDispatchAction {

    public ActionForward newQuote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("palletList",  new UserDAO().getPalletList());
        request.setAttribute("packageList",  new UserDAO().getPackageList());
        request.setAttribute("classList",  getClassesList());
        request.getSession().removeAttribute("quoteList");
        return mapping.findForward(SUCCESS);
    }
    public ActionForward saveQuote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RateQuoteForm ratequoteform = (RateQuoteForm) form;
        if(null != ratequoteform.getQuoteId()){
            DomesticRateQuote quote = new DomesticRateQuoteDAO().findById(Integer.parseInt(ratequoteform.getQuoteId()));
            if(null != quote){
                 List<DomesticRateCarrier> carrierList = new DomesticRateCarrierDAO().findByProperty("domesticRateQuote.id", quote.getId());
                 for (DomesticRateCarrier domesticRateCarrier : carrierList) {
                    if(null != ratequoteform.getCarrierName() && ratequoteform.getCarrierName().equalsIgnoreCase(domesticRateCarrier.getCarrierName())){
                        domesticRateCarrier.setRated(true);
                    }else{
                        domesticRateCarrier.setRated(false);
                    }
                    new DomesticRateCarrierDAO().update(domesticRateCarrier);
                }
                 request.setAttribute("quote", quote);
                 request.setAttribute("carrier", carrierList);
            }
        }
        return mapping.findForward(RATE_QUOTE);
    }
    public ActionForward getQuote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RateQuoteForm ratequoteform = (RateQuoteForm) form;
        if(null != ratequoteform.getShipmentId()){
            List<DomesticRateQuote> rateQuote = new DomesticRateQuoteDAO().findByProperty("shipmentId", ratequoteform.getShipmentId());
            if(!rateQuote.isEmpty()){
                DomesticRateQuote quote = rateQuote.get(0);
                 List<DomesticRateCarrier> carrierList = new DomesticRateCarrierDAO().findByProperty("domesticRateQuote.id", quote.getId());
                 request.setAttribute("quote", quote);
                 request.setAttribute("carrier", carrierList);
            }
        }
        return mapping.findForward(RATE_QUOTE);
    }
    public ActionForward editQuote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RateQuoteForm ratequoteform = (RateQuoteForm) form;
        if(null != ratequoteform.getQuoteId()){
            DomesticRateQuote quote = new DomesticRateQuoteDAO().findById(Integer.parseInt(ratequoteform.getQuoteId()));
            if(null != quote){
                 List<DomesticRateCarrier> carrierList = new DomesticRateCarrierDAO().findByProperty("domesticRateQuote.id", quote.getId());
                 request.setAttribute("quote", quote);
                 request.setAttribute("carrier", carrierList);
            }
        }
        return mapping.findForward(RATE_QUOTE);
    }
    public ActionForward searchQuote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RateQuoteForm ratequoteform = (RateQuoteForm) form;
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginuser");
        if(null != ratequoteform.getUserId()){
            List<DomesticRateQuote> quoteList = new DomesticRateQuoteDAO().searchDomesticQuote(loginUser, ratequoteform);
            session.setAttribute("quoteList", quoteList);
        }
        request.setAttribute("userNameList",  new UserDAO().getDomesticUser());
        return mapping.findForward(SEARCH_QUOTE);
    }
    public ActionForward bookQuote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RateQuoteForm ratequoteform = (RateQuoteForm) form;
        HttpSession session = request.getSession();
        if (null != ratequoteform.getQuoteId() && null != ratequoteform.getCarrierName()) {
            DomesticRateQuote domesticRateQuote = new DomesticRateQuoteDAO().findById(Integer.parseInt(ratequoteform.getQuoteId()));
            List<DomesticRateCarrier> carrierList = new DomesticRateCarrierDAO().findByProperty("domesticRateQuote.id", domesticRateQuote.getId());
            List<DomesticPurchaseOrder> orderList = new DomesticPurchaseOrderDAO().findByProperty("domesticRateQuote.id", domesticRateQuote.getId());
            DomesticRateCarrier rateCarrier = null;
            for (DomesticRateCarrier domesticRateCarrier : carrierList) {
                if(ratequoteform.getCarrierName().equals(domesticRateCarrier.getCarrierName())){
                    rateCarrier = domesticRateCarrier;
                    break;
                }
            }
            User loginUser = (User) session.getAttribute("loginuser");
            if (null != domesticRateQuote) {
                DomesticBooking booking = setDomesticBooking(domesticRateQuote,rateCarrier,orderList, ratequoteform, session);
                sendQuoteMail(domesticRateQuote, carrierList, orderList, ratequoteform, loginUser);
                request.setAttribute("quote", domesticRateQuote);
                request.setAttribute("carrier", carrierList);
                request.setAttribute("booking", booking);
                PrintWriter out = response.getWriter();
                out.print("available========" + new ItemDAO().getItemId("Domestic Move", "DMV"));
                out.flush();
                out.close();
            }
        }
	return null;
    }

    public ActionForward bookQuotePopUp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward(ADD_BOOKING);
    }

    public ActionForward rateQuote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RateQuoteForm ratequoteform = (RateQuoteForm) form;
        DomesticCTSWebService domesticwebservice = new DomesticCTSWebService();
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginuser");
        session.removeAttribute("ctsxml");
        String realPath = session.getServletContext().getRealPath("/xml/");
        String fileName = "ctsresponse" + session.getId() + ".xml";
        PrimaryFrieghtXmlParser ctsxml = domesticwebservice.processCTSWebService(realPath, fileName, ratequoteform);
        if(null != ctsxml.getXmlObjMap() && null != ctsxml.getXmlObjMap().get("error") 
                && CommonUtils.isNotEmpty(ctsxml.getXmlObjMap().get("error").toString()) && !"0".equals(ctsxml.getXmlObjMap().get("error").toString())){
            request.setAttribute("error", ctsxml.getXmlObjMap().get("error").toString());
        }else{
            setMeasures(ctsxml, ratequoteform);
            if(null != ctsxml.getXmlObjMap()){
                DomesticRateQuote domesticRateQuote = saveQuote(ctsxml, ratequoteform,loginUser);
                List<DomesticRateCarrier> carrierList = new DomesticRateCarrierDAO().findByProperty("domesticRateQuote.id", domesticRateQuote.getId());
                List<DomesticPurchaseOrder> orderList = new DomesticPurchaseOrderDAO().findByProperty("domesticRateQuote.id", domesticRateQuote.getId());
                sendQuoteMail(domesticRateQuote, carrierList, orderList, ratequoteform, loginUser);
                request.setAttribute("quote", domesticRateQuote);
                request.setAttribute("carrier", carrierList);
                ratequoteform.setShipmentId(domesticRateQuote.getShipmentId());
                List<DomesticRateQuote> quoteList = new DomesticRateQuoteDAO().searchDomesticQuote(loginUser, ratequoteform);
                session.setAttribute("quoteList", quoteList);
            }
        }
        return mapping.findForward(RATE_QUOTE);
    }

    public ActionForward preview(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginuser");
        RateQuoteForm ratequoteform = (RateQuoteForm) form;
        if (null != ratequoteform.getQuoteId()) {
            DomesticRateQuote domesticRateQuote = new DomesticRateQuoteDAO().findById(Integer.parseInt(ratequoteform.getQuoteId()));
            List<DomesticRateCarrier> carrierList = new DomesticRateCarrierDAO().findByProperty("domesticRateQuote.id", domesticRateQuote.getId());
            List<DomesticPurchaseOrder> orderList = new DomesticPurchaseOrderDAO().findByProperty("domesticRateQuote.id", domesticRateQuote.getId());
            String fileLocation = LoadLogisoftProperties.getProperty("reportLocation") + "/CTSQuote";
            File dir = new File(fileLocation);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String fileName = fileLocation + "/Quote_" + ratequoteform.getCarrierName() + ".pdf";
            String contextPath = this.servlet.getServletContext().getRealPath("/");
            new CTSQuotePdfCreator().createReport(domesticRateQuote,carrierList,orderList,loginUser, fileName, contextPath, ratequoteform.getCarrierName());
            PrintWriter out = response.getWriter();
            out.print(fileName);
            out.flush();
            out.close();
        }
        return null;
    }

    private DomesticBooking setDomesticBooking(DomesticRateQuote domesticRateQuote,DomesticRateCarrier carrier, List<DomesticPurchaseOrder> orderList,RateQuoteForm ratequoteform, HttpSession session) throws Exception {
            User loginUser = (User) session.getAttribute("loginuser");
            DomesticBooking booking = new DomesticBooking();
        if (null != loginUser) {
            booking.setBookingNumber(ratequoteform.getBookingNumber());
            booking.setCarrierName(carrier.getCarrierName());
            booking.setDestination(domesticRateQuote.getDestinationCity());
            booking.setOrigin(domesticRateQuote.getOriginCity());
            booking.setFromZip(domesticRateQuote.getOriginZip());
            booking.setToZip(domesticRateQuote.getDestinationZip());
            booking.setToState(domesticRateQuote.getDestinationState());
            booking.setFromState(domesticRateQuote.getOriginState());
            booking.setDirectInterline(carrier.getDirectInterline());
            booking.setExtraCharge(carrier.getExtraCharge());
            booking.setFuelCharge(carrier.getFuelCharge());
            booking.setFinalCharge(carrier.getFinalCharge());
            booking.setLineHual(carrier.getLineHual());
            booking.setEstimatedDays(carrier.getEstimatedDays());
            booking.setType(carrier.getType());
            booking.setBookedBy(loginUser);
            booking.setBookedOn(new Date());
            booking.setScac(carrier.getScac());
            booking.setOriginCode(carrier.getOriginCode());
            booking.setOriginName(carrier.getOriginName());
            booking.setOriginAddress(carrier.getOriginAddress());
            booking.setOriginCity(carrier.getOriginCity());
            booking.setOriginState(carrier.getOriginState());
            booking.setOriginZip(carrier.getOriginZip());
            booking.setOriginPhone(carrier.getOriginPhone());
            booking.setOriginFax(carrier.getOriginFax());
            booking.setDestinationCode(carrier.getDestinationCode());
            booking.setDestinationName(carrier.getDestinationName());
            booking.setDestinationAddress(carrier.getDestinationAddress());
            booking.setDestinationCity(carrier.getDestinationCity());
            booking.setDestinationState(carrier.getDestinationState());
            booking.setDestinationZip(carrier.getDestinationZip());
            booking.setDestinationPhone(carrier.getDestinationPhone());
            booking.setDestinationFax(carrier.getDestinationFax());
            booking.setBilltoName(LoadLogisoftProperties.getProperty("cts.billto.name"));
            booking.setBilltoAddress1(LoadLogisoftProperties.getProperty("cts.billto.address"));
            booking.setBilltoCity(LoadLogisoftProperties.getProperty("cts.billto.city"));
            booking.setBilltoState(LoadLogisoftProperties.getProperty("cts.billto.state"));
            booking.setBilltoZipcode(LoadLogisoftProperties.getProperty("cts.billto.zip"));
            TradingPartner tp = loginUser.getCtsAccount();
            try {
                booking.setCarrierNemonic(new GenericCodeDAO().getFieldByCodeAndCodetypeId("Scac Code", carrier.getScac(), "codedesc"));
                if(null != tp && null != tp.getAcctType()){
                    CustAddress custAddress = new CustAddressDAO().findByAccountNoAndPrime(tp.getAccountno());
                    if(tp.getAcctType().contains("S")){
                       booking.setShipperName(tp.getAccountName());
                       if(null != custAddress){
                           booking.setShipperAddress1(custAddress.getAddress1());
                           booking.setShipperCity(custAddress.getCity1());
                           booking.setShipperState(custAddress.getState());
                           booking.setShipperZipcode(custAddress.getZip());
                           booking.setShipperContactEmail(custAddress.getEmail1());
                           booking.setShipperContactFax(custAddress.getFax());
                           booking.setShipperContactPhone(custAddress.getPhone());
                           booking.setShipperContactName(custAddress.getContactName());
                       }
                    }else if(tp.getAcctType().contains("C")){
                       booking.setConsigneeName(tp.getAccountName());
                       if(null != custAddress){
                           booking.setConsigneeAddress1(custAddress.getAddress1());
                           booking.setConsigneeCity(custAddress.getCity1());
                           booking.setConsigneeState(custAddress.getState());
                           booking.setConsigneeZipcode(custAddress.getZip());
                           booking.setConsigneeContactEmail(custAddress.getEmail1());
                           booking.setConsigneeContactFax(custAddress.getFax());
                           booking.setConsigneeContactPhone(custAddress.getPhone());
                           booking.setConsigneeContactName(custAddress.getContactName());
                       }
                    }
                }
                new DomesticBookingDAO().save(booking);
                domesticRateQuote.setBookedOn(new Date());
                new DomesticRateQuoteDAO().update(domesticRateQuote);
                carrier.setRated(true);
                new DomesticRateCarrierDAO().update(carrier);
                if(null != orderList){
                    for (DomesticPurchaseOrder domesticPurchaseOrder : orderList) {
                        domesticPurchaseOrder.setBookingId(booking);
                        new DomesticPurchaseOrderDAO().update(domesticPurchaseOrder);
                    }
                }
//                List<DomesticCharges> chargesList = new DomesticChargesDAO().findByProperty("domesticRateCarrier.id", carrier.getId());
//                if(null != chargesList){
//                    for (DomesticCharges domesticCharges : chargesList) {
//                        domesticCharges.setBookingId(booking);
//                        new DomesticChargesDAO().update(domesticCharges);
//                    }
//                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            
        }
            return booking;
    }

    private void setMeasures(PrimaryFrieghtXmlParser ctsxml, RateQuoteForm ratequoteform) {
        Map<String, Integer> heightMap = new HashMap<String, Integer>();
        Map<String, Integer> lengthMap = new HashMap<String, Integer>();
        Map<String, Integer> widthMap = new HashMap<String, Integer>();
        Map<String, Integer> packageMap = new HashMap<String, Integer>();
        Map<String, Integer> palletMap = new HashMap<String, Integer>();
        Map<String, Double> cubicFeetMap = new HashMap<String, Double>();
        Map<String, String> classMap = new HashMap<String, String>();
        Map<String, String> palletTypeMap = new HashMap<String, String>();
        Map<String, String> packageTypeMap = new HashMap<String, String>();
        Map<String, Double> weightMap = new HashMap<String, Double>();
        if (CommonUtils.isNotEmpty(ratequoteform.getClass1())) {
            classMap.put("1", ratequoteform.getClass1());
            heightMap.put("1", ratequoteform.getHeight1());
            lengthMap.put("1", ratequoteform.getLength1());
            widthMap.put("1", ratequoteform.getWidth1());
            packageMap.put("1", ratequoteform.getPackage1());
            palletMap.put("1", ratequoteform.getPallet1());
            packageTypeMap.put("1", ratequoteform.getPackageType1());
            palletTypeMap.put("1", ratequoteform.getPalletType1());
            cubicFeetMap.put("1", ratequoteform.getCbmOrCft1());
            weightMap.put("1", Double.parseDouble(ratequoteform.getWeight1()));
        }
        if (CommonUtils.isNotEmpty(ratequoteform.getClass2())) {
            classMap.put("2", ratequoteform.getClass2());
            heightMap.put("2", ratequoteform.getHeight2());
            lengthMap.put("2", ratequoteform.getLength2());
            widthMap.put("2", ratequoteform.getWidth2());
            packageMap.put("2", ratequoteform.getPackage2());
            palletMap.put("2", ratequoteform.getPallet2());
            packageTypeMap.put("2", ratequoteform.getPackageType2());
            palletTypeMap.put("2", ratequoteform.getPalletType2());
            cubicFeetMap.put("2", ratequoteform.getCbmOrCft2());
            weightMap.put("2", Double.parseDouble(ratequoteform.getWeight2()));
        }
        if (CommonUtils.isNotEmpty(ratequoteform.getClass3())) {
            classMap.put("3", ratequoteform.getClass3());
            heightMap.put("3", ratequoteform.getHeight3());
            lengthMap.put("3", ratequoteform.getLength3());
            widthMap.put("3", ratequoteform.getWidth3());
            packageMap.put("3", ratequoteform.getPackage3());
            palletMap.put("3", ratequoteform.getPallet3());
            packageTypeMap.put("3", ratequoteform.getPackageType3());
            palletTypeMap.put("3", ratequoteform.getPalletType3());
            cubicFeetMap.put("3", ratequoteform.getCbmOrCft3());
            weightMap.put("3", Double.parseDouble(ratequoteform.getWeight3()));
        }
        if (CommonUtils.isNotEmpty(ratequoteform.getClass4())) {
            classMap.put("4", ratequoteform.getClass4());
            heightMap.put("4", ratequoteform.getHeight4());
            lengthMap.put("4", ratequoteform.getLength4());
            widthMap.put("4", ratequoteform.getWidth4());
            packageMap.put("4", ratequoteform.getPackage4());
            palletMap.put("4", ratequoteform.getPallet4());
            packageTypeMap.put("4", ratequoteform.getPackageType4());
            palletTypeMap.put("4", ratequoteform.getPalletType4());
            cubicFeetMap.put("4", ratequoteform.getCbmOrCft4());
            weightMap.put("4", Double.parseDouble(ratequoteform.getWeight4()));
        }
        if (CommonUtils.isNotEmpty(ratequoteform.getClass5())) {
            classMap.put("5", ratequoteform.getClass5());
            heightMap.put("5", ratequoteform.getHeight5());
            lengthMap.put("5", ratequoteform.getLength5());
            widthMap.put("5", ratequoteform.getWidth5());
            packageMap.put("5", ratequoteform.getPackage5());
            palletMap.put("5", ratequoteform.getPallet5());
            packageTypeMap.put("5", ratequoteform.getPackageType5());
            palletTypeMap.put("5", ratequoteform.getPalletType5());
            cubicFeetMap.put("5", ratequoteform.getCbmOrCft5());
            weightMap.put("5", Double.parseDouble(ratequoteform.getWeight5()));
        }
        if (CommonUtils.isNotEmpty(ratequoteform.getClass6())) {
            classMap.put("6", ratequoteform.getClass6());
            heightMap.put("6", ratequoteform.getHeight6());
            lengthMap.put("6", ratequoteform.getLength6());
            widthMap.put("6", ratequoteform.getWidth6());
            packageMap.put("6", ratequoteform.getPackage6());
            cubicFeetMap.put("6", ratequoteform.getCbmOrCft6());
            weightMap.put("6", Double.parseDouble(ratequoteform.getWeight6()));
            palletMap.put("6", ratequoteform.getPallet6());
            packageTypeMap.put("6", ratequoteform.getPackageType6());
            palletTypeMap.put("6", ratequoteform.getPalletType6());
        }
        ctsxml.setLengthMap(lengthMap);
        ctsxml.setWidthMap(widthMap);
        ctsxml.setHeightMap(heightMap);
        ctsxml.setPackageMap(packageMap);
        ctsxml.setPalletMap(palletMap);
        ctsxml.setWeightMap(weightMap);
        ctsxml.setClassMap(classMap);
        ctsxml.setCubicFeetMap(cubicFeetMap);
        ctsxml.setPackageTypeMap(packageTypeMap);
        ctsxml.setPalletTypeMap(palletTypeMap);
    }

    private DomesticRateQuote saveQuote(PrimaryFrieghtXmlParser ctsxml, RateQuoteForm ratequoteform,User loginUser) {
        Map<String,Carrier> carrierMap = ctsxml.getCarrierMap();
        Origin origin = (Origin) ctsxml.getXmlObjMap().get("origin");
        Rates rates = (Rates) ctsxml.getXmlObjMap().get("rates");
        Destination destination = (Destination) ctsxml.getXmlObjMap().get("destination");
        DomesticRateQuote domesticRateQuote = new DomesticRateQuote();
        if(null != rates){
            domesticRateQuote.setShipmentId(rates.getShipmentId());
            domesticRateQuote.setMiles(rates.getMiles());
            domesticRateQuote.setCube(Double.parseDouble(rates.getCubicFeet()));
            domesticRateQuote.setUnit(ratequoteform.getUnit());
            if(null != origin){
                domesticRateQuote.setOriginCity(origin.getCity());
                domesticRateQuote.setOriginState(origin.getState());
                domesticRateQuote.setOriginZip(origin.getZipCode());
            }
            if(null != destination){
                domesticRateQuote.setDestinationCity(destination.getCity());
                domesticRateQuote.setDestinationState(destination.getState());
                domesticRateQuote.setDestinationZip(destination.getZipCode());
            }
            try {
                domesticRateQuote.setShipDate(DateUtils.parseDate(ratequoteform.getShipDate(), "yyyy-MM-dd"));
            } catch (Exception ex) {
                System.out.println("Exception parse date");
                log.info("Exception parse date in RatesQuoteAction.java", ex);
            }
            domesticRateQuote.setRateBy(loginUser);
            domesticRateQuote.setRateOn(new Date());
            try {
                new DomesticRateQuoteDAO().save(domesticRateQuote);
            } catch (Exception ex) {
                log.info("Exception in RatesQuoteAction.java", ex);
            }
            //Purchase Order
            Map<String, String> classMap = ctsxml.getClassMap();
            Map<String, Double> weightMap = ctsxml.getWeightMap();
            Map<String, Integer> palletMap = ctsxml.getPalletMap();
            Map<String, Integer> packageMap = ctsxml.getPackageMap();
            Map<String, Integer> heightMap = ctsxml.getHeightMap();
            Map<String, Integer> lengthMap = ctsxml.getLengthMap();
            Map<String, Integer> widthMap = ctsxml.getWidthMap();
            Map<String, Double> cubeMap = ctsxml.getCubicFeetMap();
            Map<String, String> packageTypeMap = ctsxml.getPackageTypeMap();
            Map<String, String> palletTypeMap = ctsxml.getPalletTypeMap();
            for (Map.Entry<String, String> map : classMap.entrySet()) {
                DomesticPurchaseOrder purchaseOrder = new DomesticPurchaseOrder();
                purchaseOrder.setClasses(map.getValue());
                if (null != weightMap.get(map.getKey())) {
                    purchaseOrder.setWeight(weightMap.get(map.getKey()));
                }
                if (null != packageMap.get(map.getKey())) {
                    purchaseOrder.setPackageQuantity(packageMap.get(map.getKey()));
                }
                if (null != packageTypeMap.get(map.getKey())) {
                    purchaseOrder.setPackageType(packageTypeMap.get(map.getKey()));
                }
                if (null != heightMap.get(map.getKey())) {
                    purchaseOrder.setHeight(heightMap.get(map.getKey()));
                }
                if (null != lengthMap.get(map.getKey())) {
                    purchaseOrder.setLength(lengthMap.get(map.getKey()));
                }
                if (null != widthMap.get(map.getKey())) {
                    purchaseOrder.setWidth(widthMap.get(map.getKey()));
                }
                if (null != palletMap.get(map.getKey())) {
                    purchaseOrder.setPalletSlip(true);
                    purchaseOrder.setHandlingUnitQuantity(palletMap.get(map.getKey()));
                }
                if (null != palletTypeMap.get(map.getKey())) {
                    purchaseOrder.setHandlingUnitType(palletTypeMap.get(map.getKey()));
                }
                if (null != cubeMap.get(map.getKey())) {
                    purchaseOrder.setCube(cubeMap.get(map.getKey()));
                }
                purchaseOrder.setDomesticRateQuote(domesticRateQuote);
                try {
                    new DomesticPurchaseOrderDAO().save(purchaseOrder);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            for (Map.Entry<String, Carrier> map : carrierMap.entrySet()) {
                Carrier carrier = map.getValue();
                DomesticRateCarrier domesticRateCarrier = new DomesticRateCarrier();
                calculateCharge(domesticRateCarrier, loginUser, carrier);
                domesticRateCarrier.setCarrierName(carrier.getName());
                domesticRateCarrier.setDirectInterline(carrier.getRelation());
                domesticRateCarrier.setExtraAmount(Double.parseDouble(carrier.getAdditionalsc().getTotal()));
                domesticRateCarrier.setFuelAmount(Double.parseDouble(carrier.getFuelsurcharge()));
                domesticRateCarrier.setLineAmount(Double.parseDouble(carrier.getInitialcharges()));
                domesticRateCarrier.setEstimatedDays(Integer.parseInt(carrier.getDays()));
                domesticRateCarrier.setType(carrier.getService().getName());
                domesticRateCarrier.setScac(carrier.getScac());
                domesticRateCarrier.setOriginCode(carrier.getOriginCode());
                domesticRateCarrier.setOriginName(carrier.getOriginName());
                domesticRateCarrier.setOriginAddress(carrier.getOriginAddress());
                domesticRateCarrier.setOriginCity(carrier.getOriginCity());
                domesticRateCarrier.setOriginState(carrier.getOriginState());
                domesticRateCarrier.setOriginZip(carrier.getOriginZip());
                domesticRateCarrier.setOriginPhone(carrier.getOriginPhone());
                domesticRateCarrier.setOriginFax(carrier.getOriginFax());
                domesticRateCarrier.setDestinationCode(carrier.getDestinationCode());
                domesticRateCarrier.setDestinationName(carrier.getDestinationName());
                domesticRateCarrier.setDestinationAddress(carrier.getDestinationAddress());
                domesticRateCarrier.setDestinationCity(carrier.getDestinationCity());
                domesticRateCarrier.setDestinationState(carrier.getDestinationState());
                domesticRateCarrier.setDestinationZip(carrier.getDestinationZip());
                domesticRateCarrier.setDestinationPhone(carrier.getDestinationPhone());
                domesticRateCarrier.setDestinationFax(carrier.getDestinationFax());
                domesticRateCarrier.setDomesticRateQuote(domesticRateQuote);
                try {
                    new DomesticRateCarrierDAO().save(domesticRateCarrier);
                } catch (Exception ex) {
                    log.info("Exception in RatesQuoteAction.java", ex);
                }
//                Map<String, Double> chargeMap = carrier.getChargeMap();
//                if (!chargeMap.isEmpty()) {
//                    for (Map.Entry<String, Double> charge : chargeMap.entrySet()) {
//                    DomesticCharges charges = new DomesticCharges();
//                    charges.setChargeCode(charge.getKey());
//                    charges.setChargeAmount(charge.getValue());
//                    charges.setDomesticRateCarrier(domesticRateCarrier);
//                    try {
//                        new DomesticChargesDAO().save(charges);
//                    } catch (Exception ex) {
//                        Logger.getLogger(RateQuoteAction.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
//
//            }

            }
        }
        return domesticRateQuote;
    }
    public void sendQuoteMail(DomesticRateQuote domesticRateQuote,List<DomesticRateCarrier> carrierList,List<DomesticPurchaseOrder> orderList,RateQuoteForm ratequoteform,User loginUser) throws Exception{
        String fileLocation = LoadLogisoftProperties.getProperty("reportLocation") + "/CTSQuote";
        File dir = new File(fileLocation);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String fileName = fileLocation + "/Quote_" + domesticRateQuote.getShipmentId() + ".pdf";
        String contextPath = this.servlet.getServletContext().getRealPath("/");
        new CTSQuotePdfCreator().createReport(domesticRateQuote, carrierList,orderList, loginUser,fileName, contextPath, ratequoteform.getCarrierName());
        MailPropertyReader mailPropertyReader = new MailPropertyReader();
        Properties mailProperties = mailPropertyReader.getProperties();
        EMailScheduler scheduler = new EMailScheduler();
        StringBuilder htmlMessageBody = new StringBuilder();
        htmlMessageBody.append("Dear Customer").append(",<br>\n");
        if(ratequoteform.getBookingNumber() != null){
            htmlMessageBody.append("The quote has been booked with reference number :").append(ratequoteform.getBookingNumber());
        }else{
            htmlMessageBody.append("You have rated a quote from ").append(domesticRateQuote.getOriginZip()).append(" to ").append(domesticRateQuote.getDestinationZip());
        }
        String toAddress = CommonUtils.isNotEmpty(loginUser.getEmail())?loginUser.getEmail()+","+mailProperties.getProperty("mail.primaryfrieght.toaddress"):mailProperties.getProperty("mail.primaryfrieght.toaddress");
        MailMessageVO mailMessageVO = new MailMessageVO(ratequoteform.getCarrierName(), toAddress, mailProperties.getProperty("mail.primaryfrieght.from.name"), mailProperties.getProperty("mail.primaryfrieght.from"), null, null, mailProperties.getProperty("mail.primaryfrieght.subject"), htmlMessageBody.toString());
        scheduler.createHtmlEmail(null, fileName, mailMessageVO, null);
    }
    private void calculateCharge(DomesticRateCarrier rateCarrier,User user,Carrier carrier){
        Double extra = Double.parseDouble(carrier.getAdditionalsc().getTotal());
        Double fuel = Double.parseDouble(carrier.getFuelsurcharge());
        Double line = Double.parseDouble(carrier.getInitialcharges());
        Double total = Double.parseDouble(carrier.getFinalcharge());
        Double minAmount = 0d;
        Double fuelMarkUp = 0d;
        Double lineMarkUp = 0d;
        Double fuelMarkUpPercentage = 0d;
        Double lineMarkUpPercentage = 0d;
        Double flatFee = 0d;
        String rateType = "";
        if(CommonUtils.isNotEmpty(user.getLineMarkUp()) || CommonUtils.isNotEmpty(user.getFuelMarkUp()) || CommonUtils.isNotEmpty(user.getFlatFee())){
            if(CommonUtils.isNotEmpty(user.getLineMarkUp()) || CommonUtils.isNotEmpty(user.getFuelMarkUp())){
                if(CommonUtils.isNotEmpty(user.getLineMarkUp())){
                    lineMarkUpPercentage = user.getLineMarkUp();
                    lineMarkUp =line *(user.getLineMarkUp()/100);
                    line+=lineMarkUp;
                }
                if(CommonUtils.isNotEmpty(user.getFuelMarkUp())){
                    fuelMarkUpPercentage = user.getFuelMarkUp();
                    fuelMarkUp =fuel *(user.getFuelMarkUp()/100);
                    fuel+=fuelMarkUp;
                }
                total = fuel+line+extra;
                rateType = "P";
            }else{
                flatFee =user.getFlatFee();
                line+=flatFee;
                total = fuel+line+extra;
                rateType = "F";
            }
            minAmount = null != user.getMinAmount() ? user.getMinAmount():0d;
        }else if(user.getCtsAccount() != null){
            TradingPartner tp = user.getCtsAccount();
            if (CommonUtils.isNotEmpty(tp.getLineMarkUp()) || CommonUtils.isNotEmpty(tp.getFuelMarkUp()) || CommonUtils.isNotEmpty(tp.getFlatFee())) {
                if (CommonUtils.isNotEmpty(tp.getLineMarkUp()) || CommonUtils.isNotEmpty(tp.getFuelMarkUp())) {
                    if(CommonUtils.isNotEmpty(tp.getLineMarkUp())){
                        lineMarkUpPercentage = tp.getLineMarkUp();
                        lineMarkUp =line *(tp.getLineMarkUp()/100);
                        line+=lineMarkUp;
                    }
                    if(CommonUtils.isNotEmpty(tp.getFuelMarkUp())){
                        fuelMarkUpPercentage = tp.getFuelMarkUp();
                        fuelMarkUp =fuel *(tp.getFuelMarkUp()/100);
                        fuel+=fuelMarkUp;
                    }
                    total = fuel + line + extra;
                    rateType = "P";
                } else {
                    flatFee =tp.getFlatFee();
                    line+=flatFee;
                    total = fuel + line + extra;
                    rateType = "F";
                }
            }
            minAmount = null != tp.getMinAmount() ? tp.getMinAmount():0d;
        }
        if(minAmount > total){
            line = minAmount;
            total = minAmount;
            fuel = 0d;
            extra = 0d;
            rateType = "M"+rateType;
        }
        rateCarrier.setRateType(rateType);
        rateCarrier.setLineMarkUpCharge(lineMarkUp);
        rateCarrier.setFuelMarkUpCharge(fuelMarkUp);
        rateCarrier.setLineMarkUp(lineMarkUpPercentage);
        rateCarrier.setFuelMarkUp(fuelMarkUpPercentage);
        rateCarrier.setFlatFee(flatFee);
        rateCarrier.setMinAmount(minAmount);

        rateCarrier.setExtraCharge(extra);
        rateCarrier.setFuelCharge(fuel);
        rateCarrier.setFinalCharge(total);
        rateCarrier.setLineHual(line);
    }
     public List<LabelValueBean> getClassesList() {
         List<LabelValueBean> classes = new ArrayList<LabelValueBean>();
         classes.add(new LabelValueBean("Select",""));
         classes.add(new LabelValueBean("50","50"));
         classes.add(new LabelValueBean("55","55"));
         classes.add(new LabelValueBean("60","60"));
         classes.add(new LabelValueBean("65","65"));
         classes.add(new LabelValueBean("70","70"));
         classes.add(new LabelValueBean("77","77"));
         classes.add(new LabelValueBean("85","85"));
         classes.add(new LabelValueBean("92","92"));
         classes.add(new LabelValueBean("100","100"));
         classes.add(new LabelValueBean("110","110"));
         classes.add(new LabelValueBean("125","125"));
         classes.add(new LabelValueBean("150","150"));
         classes.add(new LabelValueBean("175","175"));
         classes.add(new LabelValueBean("200","200"));
         classes.add(new LabelValueBean("250","250"));
         classes.add(new LabelValueBean("300","300"));
         classes.add(new LabelValueBean("400","400"));
         classes.add(new LabelValueBean("500","500"));
         return classes;
     }
}
