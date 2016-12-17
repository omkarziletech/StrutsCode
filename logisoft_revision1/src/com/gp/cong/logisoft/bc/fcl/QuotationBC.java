package com.gp.cong.logisoft.bc.fcl;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.bc.notes.NotesBC;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerBC;
import com.gp.cong.logisoft.beans.CostBean;
import com.gp.cong.logisoft.beans.GenericCodeCacheManager;
import com.gp.cong.logisoft.beans.NonRates;
import com.gp.cong.logisoft.beans.Rates;
import com.gp.cong.logisoft.domain.CustGeneralDefault;
import com.gp.cong.logisoft.domain.CustomerAddress;
import com.gp.cong.logisoft.domain.CustomerContact;
import com.gp.cong.logisoft.domain.FCLPortConfiguration;
import com.gp.cong.logisoft.domain.FclBuy;
import com.gp.cong.logisoft.domain.FclBuyCost;
import com.gp.cong.logisoft.domain.FclBuyCostTypeRates;
import com.gp.cong.logisoft.domain.FclConsolidator;
import com.gp.cong.logisoft.domain.FclOrgDestMiscData;
import com.gp.cong.logisoft.domain.GeneralInformation;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.Notes;
import com.gp.cong.logisoft.domain.Ports;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.TradingPartnerTemp;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.CustomerDAO;
import com.gp.cong.logisoft.hibernate.dao.FCLPortConfigurationDAO;
import com.gp.cong.logisoft.hibernate.dao.FclBuyCostDAO;
import com.gp.cong.logisoft.hibernate.dao.FclBuyCostTypeRatesDAO;
import com.gp.cong.logisoft.hibernate.dao.FclBuyDAO;
import com.gp.cong.logisoft.hibernate.dao.FclOrgDestMiscDataDAO;
import com.gp.cong.logisoft.hibernate.dao.GeneralInformationDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.NotesDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.logisoft.util.DBUtil;
import com.gp.cong.logisoft.util.StringFormatter;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.beans.MultiChargesOrderBean;
import com.gp.cvst.logisoft.domain.BookingFcl;
import com.gp.cvst.logisoft.domain.BookingfclUnits;
import com.gp.cvst.logisoft.domain.Charges;
import com.gp.cvst.logisoft.domain.CustAddress;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.FclBlCharges;
import com.gp.cvst.logisoft.domain.FclBlContainer;
import com.gp.cvst.logisoft.domain.FclBlCostCodes;
import com.gp.cvst.logisoft.domain.FileNumberForQuotaionBLBooking;
import com.gp.cvst.logisoft.domain.HazmatMaterial;
import com.gp.cvst.logisoft.domain.Quotation;
import com.gp.cvst.logisoft.hibernate.dao.BookingFclDAO;
import com.gp.cvst.logisoft.hibernate.dao.BookingfclUnitsDAO;
import com.gp.cvst.logisoft.hibernate.dao.ChargesDAO;
import com.gp.cvst.logisoft.hibernate.dao.CustAddressDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlDAO;
import com.gp.cvst.logisoft.hibernate.dao.HazmatMaterialDAO;
import com.gp.cvst.logisoft.hibernate.dao.QuotationDAO;
import com.gp.cvst.logisoft.hibernate.dao.RetAddDAO;
import com.gp.cvst.logisoft.struts.form.EditBookingsForm;
import com.gp.cvst.logisoft.struts.form.FCLHazMatForm;
import com.gp.cvst.logisoft.struts.form.QuotesForm;
import com.gp.cvst.logisoft.struts.form.SearchQuotationForm;
import com.logiware.common.dao.PropertyDAO;
import com.logiware.constants.ItemConstants;
import com.logiware.hibernate.domain.FclBuyOtherCommodity;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;
import org.hibernate.SQLQuery;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;

public class QuotationBC {

    CustAddressDAO custAddressDAO = new CustAddressDAO();
    CustomerDAO customerDAO = new CustomerDAO();
    UnLocationDAO unLocationDAO = new UnLocationDAO();
    GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
    PortsDAO portsDAO = new PortsDAO();
    DBUtil dBUtil = new DBUtil();
    FclBuyDAO fclBuyDAO = new FclBuyDAO();
    FclBlDAO fclBlDAO = new FclBlDAO();
    FclBuyCostDAO fclBuyCostDAO = new FclBuyCostDAO();
    QuotationDAO quotationDAO = new QuotationDAO();
    ChargesDAO chargesDAO = new ChargesDAO();
    Charges charges = new Charges();
    HazmatMaterialDAO hazmatMaterialDAO = new HazmatMaterialDAO();
    DBUtil dbUtil = new DBUtil();
    UnLocation unLocation = new UnLocation();
    HazmatMaterial hazmatMaterial = new HazmatMaterial();
    GenericCode genericCode = new GenericCode();
    BookingFcl bookingFCL = new BookingFcl();
    BookingFclDAO bookingFclDAO = new BookingFclDAO();
    BookingfclUnits bookingfclUnits = new BookingfclUnits();
    FclBuyCostTypeRatesDAO fclBuyCostTypeRatesDAO = new FclBuyCostTypeRatesDAO();
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    FclOrgDestMiscDataDAO fclOrgDestMiscDataDAO = new FclOrgDestMiscDataDAO();
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss:ms");
    SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
    NumberFormat numb = new DecimalFormat("###,###,##0.00");
    BookingfclUnitsDAO bookingFclUnitsDAO = new BookingfclUnitsDAO();
    NotesDAO notesDAO = new NotesDAO();
    StringFormatter stringFormatter = new StringFormatter();
    //BookingFclBC bookingFclBC=new BookingFclBC();
    Ports ports = new Ports();
    //to get the customer details using customer name
    boolean hasRateChange = false;

    public List<CustAddress> getCustomerList(String customerName) throws Exception {
        List<CustAddress> customerList = custAddressDAO.findBy(customerName, null, null, null);
        return customerList;
    }
    //	to get the customer details using customer no

    public List<CustAddress> getCustomerNoList(String customerNo) throws Exception {
        List<CustAddress> customerList = custAddressDAO.findBy(null, customerNo, null, null);
        return customerList;
    }

    //set the radio buttons and checkboxes for the quotation form is the value is null
    public QuotesForm getQuotesForm(QuotesForm quotesForm) throws Exception {
        String companyCode = new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
        if (quotesForm.getPrintDesc() == null) {
            quotesForm.setPrintDesc("on");
        }
        if (quotesForm.getFinalized() == null) {
            quotesForm.setFinalized("off");
        }
        if (quotesForm.getCarrierPrint() == null) {
            quotesForm.setCarrierPrint("on");
        }
        if (quotesForm.getCommodityPrint() == null) {
            quotesForm.setCommodityPrint("on");
        }
        if (quotesForm.getBreakBulk() == null) {
            quotesForm.setBreakBulk("N");
        }
        if (quotesForm.getSpecialequipment() == null) {
            quotesForm.setSpecialequipment("N");
        }
        if (quotesForm.getHazmat() == null) {
            quotesForm.setHazmat("N");
        }
        if (quotesForm.getSpotRate() == null) {
            quotesForm.setSpotRate("N");
        }
        if (quotesForm.getOutofgate() == null) {
            quotesForm.setOutofgate("N");
        }
        if (quotesForm.getLocaldryage() == null) {
            quotesForm.setLocaldryage("N");
        }
        if (quotesForm.getInland() == null) {
            quotesForm.setInland("N");
        }
        if (quotesForm.getDocCharge() == null) {
            quotesForm.setDocCharge("N");
        }
        if (quotesForm.getPierPass() == null) {
            quotesForm.setPierPass("N");
        }
         if (quotesForm.getChassisCharge() == null) {
            quotesForm.setChassisCharge("N");
        }
        if (quotesForm.getIntermodel() == null) {
            quotesForm.setIntermodel("N");
        }
        if (quotesForm.getInsurance() == null) {
            quotesForm.setInsurance("N");
        }
        if (quotesForm.getSoc() == null) {
            quotesForm.setSoc("N");
        }
        if (quotesForm.getCustomertoprovideSED() == null) {
            quotesForm.setCustomertoprovideSED("N");
        }
        if (quotesForm.getDeductFFcomm() == null) {
            quotesForm.setDeductFFcomm("N");
        }
        if (quotesForm.getRoutedAgentCheck() == null) {
            quotesForm.setRoutedAgentCheck("off");
        }
        if (quotesForm.getDirectConsignmntCheck() == null) {
            quotesForm.setDirectConsignmntCheck("off");
        }
        if (quotesForm.getLdprint() == null) {
            quotesForm.setLdprint("off");
        }
        if (quotesForm.getIdinclude() == null) {
            quotesForm.setIdinclude("off");
        }
        if (quotesForm.getIdprint() == null) {
            quotesForm.setIdprint("off");
        }
        if (quotesForm.getPrintDesc() == null) {
            quotesForm.setPrintDesc("on");
        }
        if (quotesForm.getInsureinclude() == null) {
            quotesForm.setInsureinclude("off");
        }
        if (quotesForm.getInsureprint() == null) {
            quotesForm.setInsureprint("off");
        }
        if (quotesForm.getRatesNonRates() == null) {
            quotesForm.setRatesNonRates("R");
        }
        if (quotesForm.getNewClient() == null) {
            quotesForm.setNewClient("off");
        }
        if (quotesForm.getClientConsigneeCheck() == null) {
            quotesForm.setClientConsigneeCheck("off");
        }
        if (quotesForm.getTotalCharges() == null || quotesForm.getTotalCharges().equals("")) {
            quotesForm.setTotalCharges("0.00");
        }
        if (quotesForm.getBaht() == null || quotesForm.getBaht().equals("")) {
            quotesForm.setBaht("0.00");
        }
        if (quotesForm.getBdt() == null || quotesForm.getBdt().equals("")) {
            quotesForm.setBdt("0.00");
        }
        if (quotesForm.getCyp() == null || quotesForm.getCyp().equals("")) {
            quotesForm.setCyp("0.00");
        }
        if (quotesForm.getEur() == null || quotesForm.getEur().equals("")) {
            quotesForm.setEur("0.00");
        }
        if (quotesForm.getHkd() == null || quotesForm.getHkd().equals("")) {
            quotesForm.setHkd("0.00");
        }
        if (quotesForm.getLkr() == null || quotesForm.getLkr().equals("")) {
            quotesForm.setLkr("0.00");
        }
        if (quotesForm.getNt() == null || quotesForm.getNt().equals("")) {
            quotesForm.setNt("0.00");
        }
        if (quotesForm.getPrs() == null || quotesForm.getPrs().equals("")) {
            quotesForm.setPrs("0.00");
        }
        if (quotesForm.getRmb() == null || quotesForm.getRmb().equals("")) {
            quotesForm.setRmb("0.00");
        }
        if (quotesForm.getWon() == null || quotesForm.getWon().equals("")) {
            quotesForm.setWon("0.00");
        }
        if (quotesForm.getYen() == null || quotesForm.getYen().equals("")) {
            quotesForm.setYen("0.00");
        }
        if (quotesForm.getMyr() == null || quotesForm.getMyr().equals("")) {
            quotesForm.setMyr("0.00");
        }
        if (quotesForm.getNht() == null || quotesForm.getNht().equals("")) {
            quotesForm.setNht("0.00");
        }
        if (quotesForm.getPkr() == null || quotesForm.getPkr().equals("")) {
            quotesForm.setPkr("0.00");
        }
        if (quotesForm.getRm() == null || quotesForm.getRm().equals("")) {
            quotesForm.setRm("0.00");
        }
        if (quotesForm.getSpo() == null || quotesForm.getSpo().equals("")) {
            quotesForm.setSpo("0.00");
        }
        if (quotesForm.getVnd() == null || quotesForm.getVnd().equals("")) {
            quotesForm.setVnd("0.00");
        }
        if (quotesForm.getInr() == null || quotesForm.getInr().equals("")) {
            quotesForm.setInr("0.00");
        }
        if ((quotesForm.getBrand() == null || quotesForm.getBrand().equals("")) && companyCode.equals("03")) {
            quotesForm.setBrand("Ecu Worldwide");
        } else if((quotesForm.getBrand() == null || quotesForm.getBrand().equals("")) && companyCode.equals("02")){
            quotesForm.setBrand("OTI");
        }

        quotesForm.setDefaultAgent("Y");
        return quotesForm;
    }

    public List<CostBean> getQuotationRates(QuotationDTO quotesForm, MessageResources messageResources, HttpServletRequest request) throws Exception {
        String Origin = null;
        String rampCity = null;
        String Destination = null;
        String pol = null;
        String pod = null;
        UnLocation refTerminal = null;
        UnLocation portsTemp = null;
        GenericCode genObj = null;
        String unlocationCode = "";
        String noOfDays = "";
        String isTerminal = "";
        String newrampCity = null;
        if (quotesForm.getNoOfDays() == null) {
            noOfDays = "30";
        }
        String DATE_FORMAT = "yyyy-MM-dd";
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        Calendar c1 = Calendar.getInstance();
        int year = 1900 + d.getYear();
        int month = d.getMonth();
        c1.set(year, month, d.getDate()); // 1999 jan 20
        if (noOfDays.length() != 0) {
            c1.add(Calendar.DATE, Integer.parseInt(noOfDays));
        }
        List<CostBean> temList = new ArrayList();

        Origin = quotesForm.getIsTerminal();
        isTerminal = quotesForm.getIsTerminal();
        if (Origin != null) {
            int i = Origin.indexOf("/");
            if (i != -1) {
                String a[] = Origin.split("/");
                Origin = a[0];
                unlocationCode = a[1];
            }

            List list = unLocationDAO.findForManagement(unlocationCode.trim(), Origin.trim());
            if (list != null && list.size() > 0) {
                refTerminal = (UnLocation) list.get(0);
                Origin = refTerminal.getId().toString();
            }
        }
        rampCity = quotesForm.getRampCity();
        newrampCity = quotesForm.getRampCity();
        if (rampCity != null) {
            int j = rampCity.indexOf("/");
            if (j != -1) {
                String a[] = rampCity.split("/");
                rampCity = a[0];

                unlocationCode = a[1];
            }
            List list1 = unLocationDAO.findForManagement(unlocationCode.trim(), rampCity.trim());
            if (list1 != null && list1.size() > 0) {
                refTerminal = (UnLocation) list1.get(0);
                rampCity = refTerminal.getId().toString();
            }
        }
        unlocationCode = "";
        pol = quotesForm.getPlaceofReceipt();
        int k = pol.indexOf("/");
        if (k != -1) {
            String a[] = pol.split("/");
            pol = a[0];
            unlocationCode = a[1];
        }

        List list2 = unLocationDAO.findForManagement(unlocationCode.trim(), pol.trim());
        if (list2 != null && list2.size() > 0) {
            refTerminal = (UnLocation) list2.get(0);
            pol = refTerminal.getId().toString();
        }
        unlocationCode = "";
        pod = quotesForm.getFinalDestination();
        int l = pod.indexOf("/");
        if (l != -1) {
            String a[] = pod.split("/");
            pod = a[0];
            unlocationCode = a[1];
        }
        List list3 = unLocationDAO.findForManagement(unlocationCode.trim(), pod.trim());
        if (list3 != null && list3.size() > 0) {
            portsTemp = (UnLocation) list3.get(0);
            pod = portsTemp.getId().toString();
        }
        unlocationCode = "";
        Destination = quotesForm.getPortofDischarge();
        int b = Destination.indexOf("/");
        if (b != -1) {
            String a[] = Destination.split("/");
            Destination = a[0];
            unlocationCode = a[1];
        }
        List list4 = unLocationDAO.findForManagement(unlocationCode.trim(), Destination.trim());
        if (list4 != null && list4.size() > 0) {
            portsTemp = (UnLocation) list4.get(0);
            Destination = portsTemp.getId().toString();
        }

        if (quotesForm.getCommcode() != null && !quotesForm.getCommcode().equals("") && !quotesForm.getCommcode().equals("%")) {
            List comList = genericCodeDAO.findForGenericCode(quotesForm.getCommcode());
            if (comList != null && comList.size() > 0) {
                genObj = (GenericCode) comList.get(0);
            }
        }
        if (quotesForm.getDescription() != null && !quotesForm.getDescription().equals("")) {
            List codeList = genericCodeDAO.findForAirRates(null, quotesForm.getDescription());
            if (codeList != null && codeList.size() > 0) {
                genObj = (GenericCode) codeList.get(0);
            }
        }
        if (quotesForm.getButtonValue().equals("popup1")) {
            //temList = fclBuyDAO.getSslineId(Origin,Destination,genObj.getId().toString(),c1.getTime(),messageResources);
        } else if (quotesForm.getButtonValue().equals("newgetRates")) {
            temList = fclBuyDAO.getSslineId2(Origin, Destination, quotesForm.getSsline(), genObj.getId().toString(), quotesForm.getSscode(), c1.getTime(), messageResources);
            if (temList.isEmpty()) {
                temList = fclBuyDAO.getSslineId2(rampCity, Destination, quotesForm.getSsline(), genObj.getId().toString(), quotesForm.getSscode(), c1.getTime(), messageResources);
                if (temList.isEmpty()) {
                    temList = fclBuyDAO.getSslineId2(pol, Destination, quotesForm.getSsline(), genObj.getId().toString(), quotesForm.getSscode(), c1.getTime(), messageResources);
                    if (temList.isEmpty()) {
                        temList = fclBuyDAO.getSslineId2(Origin, pod, quotesForm.getSsline(), genObj.getId().toString(), quotesForm.getSscode(), c1.getTime(), messageResources);
                        if (temList.isEmpty()) {
                            temList = fclBuyDAO.getSslineId2(rampCity, pod, quotesForm.getSsline(), genObj.getId().toString(), quotesForm.getSscode(), c1.getTime(), messageResources);
                            if (temList.isEmpty()) {
                                temList = fclBuyDAO.getSslineId2(pol, pod, quotesForm.getSsline(), genObj.getId().toString(), quotesForm.getSscode(), c1.getTime(), messageResources);
                                request.setAttribute("message", "Rate is based on for this SSL from " + quotesForm.getPlaceofReceipt() + " to " + quotesForm.getFinalDestination());
                            } else {
                                request.setAttribute("message", "Rate is based on for this SSL from " + isTerminal + " to " + quotesForm.getFinalDestination());
                            }
                        } else {
                            request.setAttribute("message", "Rate is based on for this SSL from " + newrampCity + " to " + quotesForm.getPortofDischarge());
                        }
                    } else {
                        request.setAttribute("message", "Rate is based on for this SSL from " + quotesForm.getPlaceofReceipt() + " to " + quotesForm.getPortofDischarge());
                    }
                } else {
                    request.setAttribute("message", "Rate is based on for this SSL from " + newrampCity + " to " + quotesForm.getPortofDischarge());
                }
            } else {
                request.setAttribute("message", "Rate is based on for this SSL from " + isTerminal + " to " + quotesForm.getPortofDischarge());
            }
            if (temList.isEmpty()) {
                request.setAttribute("message", "Rate  DO NOT Exist for this Origin and Destination");
            }
        }
        return temList;
    }

    //to get the rates from fcl rates table for the origin, destination and commodity
    public void getQuotationRates1(QuotationDTO quotesForm, MessageResources messageResources, HttpServletRequest request, Quotation quotation, String buttonValue, boolean isImsQuote) throws Exception {
        List deletedList = new ArrayList();
        HashMap<String, String> deletedChargesMap = new HashMap();
        HttpSession session = request.getSession(true);
        if (quotation.getQuoteId() != null && !"copyQuoteWithRate".equalsIgnoreCase(buttonValue)) {
            //--DELETING THE CHARGES COMING FROM RATES TABLE-----
            if ("hazmat".equalsIgnoreCase(buttonValue)) {
                deletedList = chargesDAO.getQuoteId1(quotation.getQuoteId());
            } else {
                deletedList = chargesDAO.getQuoteId(quotation.getQuoteId());
            }
            for (Iterator iter = deletedList.iterator(); iter.hasNext();) {
                Charges charges = (Charges) iter.next();
                boolean deleteFlag = true;
                if (!"hazmat".equalsIgnoreCase(buttonValue) && CommonUtils.isNotEmpty(charges.getAccountNo()) && charges.getAccountNo().equalsIgnoreCase(quotesForm.getSscode())) {
                    if (CommonUtils.isNotEmpty(charges.getChargeFlag()) && CommonUtils.isNotEmpty(charges.getUnitType()) && null != new GenericCodeDAO().findById(Integer.parseInt(charges.getUnitType()))) {
                        String unitType = new GenericCodeDAO().findById(Integer.parseInt(charges.getUnitType())).getCodedesc();
                        if (CommonUtils.isNotEmpty(unitType) && CommonUtils.isNotEmpty(quotesForm.getSelectedCheck()) && quotesForm.getSelectedCheck().contains(unitType)) {
                            deleteFlag = false;
                        }
                    }
                } else if ("hazmat".equalsIgnoreCase(buttonValue) && CommonUtils.isNotEmpty(charges.getSpecialEquipmentUnit())
                        && !"HAZFEE".equalsIgnoreCase(charges.getChargeCodeDesc())) {
                    deleteFlag = false;
                }

                if (deleteFlag) {
                    deletedChargesMap.put(charges.getChgCode() + "-" + charges.getUnitType(), charges.getComment());
                    chargesDAO.delete(charges);
                }
            }
        }
        List chargesList = new ArrayList();
        List flatRatePerContainerList = new ArrayList();
        List per2000kglbsList = new ArrayList();
        List perBlList = new ArrayList();
        List percentOfrList = new ArrayList();
        List percentDrayageList = new ArrayList();
        String origin = "";
        String destination = "";
        String commodity = "";
        if (quotesForm.getSelectedOrigin() != null) {
            origin = stringFormatter.findForManagement(quotesForm.getSelectedOrigin(), null);
        }
        if (quotesForm.getSelectedDestination() != null) {
            destination = stringFormatter.findForManagement(quotesForm.getSelectedDestination().replace("&&", "'"), null);
        }
        if (quotesForm.getSelectedComCode() != null && !quotesForm.getSelectedComCode().equals("")
                && !quotesForm.getSelectedComCode().equals("%")) {
            List comList = genericCodeDAO.findForGenericCode(quotesForm.getSelectedComCode());
            if (comList != null && comList.size() > 0) {
                GenericCode genObj = (GenericCode) comList.get(0);
                commodity = genObj.getId().toString();
                quotation.setCommcode(genObj);
                quotation.setDescription(genObj.getCodedesc());
            }
        }
        String unitType = quotesForm.getSelectedCheck();
        String unitTypes[] = unitType.split(",");
        if ("copyQuoteWithRate".equalsIgnoreCase(buttonValue) && CommonUtils.isNotEmpty(quotation.getFileType())
                && "I".equalsIgnoreCase(quotation.getFileType())) {
            quotation.setSelectedUnits(""); //-- Make selected units empty to uncheck the check boxes, for copied import file types--//
        } else {
            quotation.setSelectedUnits(unitType);
        }
        String markUp = "markup1";
        if (CommonUtils.isNotEmpty(destination) && destination.lastIndexOf("(") != -1
                && destination.lastIndexOf(")") != -1) {
            String destinationCode = destination.substring(destination.lastIndexOf("(") + 1, destination.lastIndexOf(")"));
            Ports ports = new PortsDAO().getPorts(destinationCode);
            if (null != ports && null != ports.getRegioncode() && "Y".equalsIgnoreCase(ports.getRegioncode().getField3())) {
                markUp = "markup2";
            }
        }
        List otherCommList = fclBuyCostDAO.getOtherCommodity(quotesForm.getSelectedComCode(), markUp);
        List<FclBuyOtherCommodity> otherCommodityList = new ArrayList<FclBuyOtherCommodity>();
        String baseCommodity = commodity;
        if (CommonUtils.isNotEmpty(otherCommList)) {
            for (int i = 0; i < otherCommList.size(); i++) {
                Object[] object = (Object[]) otherCommList.get(i);
                baseCommodity = null != object[3] ? object[3].toString() : "";
                FclBuyOtherCommodity fclBuyOtherCommodity = new FclBuyOtherCommodity();
                fclBuyOtherCommodity.setAddSub(null != object[0] ? object[0].toString() : "");
                fclBuyOtherCommodity.setMarkUp(null != object[1] ? Double.parseDouble(object[1].toString()) : 0);
                fclBuyOtherCommodity.setCostCode(null != object[2] ? genericCodeDAO.getFieldByCodeAndCodetypeId("Cost Code", object[2].toString(), "codedesc") : "");
                fclBuyOtherCommodity.setBaseCommodityCode(null != object[3] ? object[3].toString() : "");
                fclBuyOtherCommodity.setMarkUp2(null != object[4] ? Double.parseDouble(object[4].toString()) : 0);
                otherCommodityList.add(fclBuyOtherCommodity);
            }
        }
        List ratesList = fclBuyDAO.findForSearchFclBuyRatesForCompressList2(origin, destination, baseCommodity, quotesForm.getSscode());
        for (int i = 0; i < ratesList.size(); i++) {
            FclBuy fclBuy = (FclBuy) ratesList.get(i);
            if (fclBuy.getFclBuyCostsSet() != null) {
                Iterator iter = fclBuy.getFclBuyCostsSet().iterator();
                while (iter.hasNext()) {
                    FclBuyCost fclBuyCost = (FclBuyCost) iter.next();
                    if (fclBuyCost.getFclBuyUnitTypesSet() != null) {
                        Iterator iterator = fclBuyCost.getFclBuyUnitTypesSet().iterator();
                        while (iterator.hasNext()) {
                            FclBuyCostTypeRates fclBuyCostTypeRates = (FclBuyCostTypeRates) iterator.next();
                            Charges charges = new Charges();
                            charges.setNumber("1");
                            if (fclBuyCost.getCostId() != null) {
                                charges.setChargeCode(fclBuyCost.getCostId());
                                charges.setChargeCodeDesc(fclBuyCost.getCostId().getCode());
                                charges.setChgCode(fclBuyCost.getCostId().getCodedesc());
                            }
                            if (fclBuyCost.getContType() != null) {
                                charges.setCosttype(fclBuyCost.getContType());
                                charges.setCostType(fclBuyCost.getContType().getCodedesc());
                                if (fclBuyCost.getContType().getCodedesc().equalsIgnoreCase(messageResources.getMessage("FlatRatePerConatinerSize"))) {
                                    charges.setAmount(fclBuyCostTypeRates.getActiveAmt());
                                } else {
                                    charges.setAmount(fclBuyCostTypeRates.getRatAmount());
                                }
                                charges.setMinimum(fclBuyCostTypeRates.getMinimumAmt());
                            }
                            double markup = 0.00;
                            if (CommonUtils.isNotEmpty(otherCommodityList)) {
                                boolean noMarkup = true;
                                for (FclBuyOtherCommodity fclBuyOtherCommodity : otherCommodityList) {
                                    if (CommonUtils.isEqualIgnoreCase(charges.getChgCode(), fclBuyOtherCommodity.getCostCode())) {
                                        if ("A".equalsIgnoreCase(fclBuyOtherCommodity.getAddSub())) {
                                            Ports port = portsDAO.findById(fclBuy.getDestinationPort().getId());
                                            if (null != port && null != port.getRegioncode() && "Y".equalsIgnoreCase(port.getRegioncode().getField3())) {
                                                markup += fclBuyCostTypeRates.getMarkup() + fclBuyOtherCommodity.getMarkUp2();
                                            } else {
                                                markup += fclBuyCostTypeRates.getMarkup() + fclBuyOtherCommodity.getMarkUp();
                                            }
                                        } else {
                                            Ports port = portsDAO.findById(fclBuy.getDestinationPort().getId());
                                            if (null != port && null != port.getRegioncode() && "Y".equalsIgnoreCase(port.getRegioncode().getField3())) {
                                                markup += fclBuyCostTypeRates.getMarkup() - fclBuyOtherCommodity.getMarkUp2();
                                            } else {
                                                markup += fclBuyCostTypeRates.getMarkup() - fclBuyOtherCommodity.getMarkUp();
                                            }
                                        }
                                        noMarkup = false;
                                    }
                                }
                                if (noMarkup) {
                                    markup += fclBuyCostTypeRates.getMarkup();
                                }
                            } else {
                                markup += fclBuyCostTypeRates.getMarkup();
                            }
                            charges.setMarkUp(markup);
                            charges.setEfectiveDate(new Date());
                            if (fclBuy.getSslineNo() != null) {
                                charges.setAccountName(fclBuy.getSslineNo().getAccountName());
                                charges.setAccountNo(fclBuy.getSslineNo().getAccountno());
                            }
                            if (fclBuyCostTypeRates.getCurrency() != null) {
                                charges.setCurrency1(fclBuyCostTypeRates.getCurrency());
                                charges.setCurrecny(fclBuyCostTypeRates.getCurrency().getCodedesc());
                            }
                            boolean flag = false;
                            if (fclBuyCostTypeRates.getUnitType() != null) {
                                for (int j = 0; j < unitTypes.length; j++) {
                                    if (unitTypes[j].equals("E=45")) {
                                        unitTypes[j] = "E=45'102";
                                    }
                                    if (fclBuyCostTypeRates.getUnitType().getCodedesc().equals(unitTypes[j])) {
                                        flag = true;
                                        break;
                                    }
                                }
                                charges.setUnitType(fclBuyCostTypeRates.getUnitType().getId().toString());
                            }
                            if (charges.getChgCode() != null && (((charges.getChgCode().trim().equalsIgnoreCase(messageResources.getMessage("hazardousSurcharge"))
                                    || charges.getChgCode().trim().equalsIgnoreCase(messageResources.getMessage("hazardousSurchargeland"))
                                    || charges.getChgCode().trim().equalsIgnoreCase(messageResources.getMessage("hazardousSurchargesea"))
                                    || charges.getChgCode().trim().equalsIgnoreCase(messageResources.getMessage("hazfeecertun"))
                                    || charges.getChgCode().trim().equalsIgnoreCase(messageResources.getMessage("hazardcertificate"))
                                    || charges.getChargeCodeDesc().trim().equalsIgnoreCase(messageResources.getMessage("hazardous"))
                                    || charges.getChgCode().trim().equalsIgnoreCase(messageResources.getMessage("hazardousfee")))
                                    && quotesForm.getHazmat().equals("N")) || (charges.getChgCode().trim().equals(messageResources.getMessage("spclequipmentsurcharge"))
                                    && quotesForm.getSpclEqpmt().equals("N")) || (charges.getChargeCodeDesc() != null && charges.getChargeCodeDesc().trim().equals(messageResources.getMessage("soc"))
                                    && quotesForm.getSoc().equals("N")))) {
                            } else {
                                if (flag && charges.getCosttype().getCodedesc().trim().equalsIgnoreCase(messageResources.getMessage("FlatRatePerConatinerSize"))) {
                                    chargesList.add(charges);
                                } else if (charges.getCosttype().getCodedesc().equalsIgnoreCase(messageResources.getMessage("Flatratepercontainer"))) {
                                    flatRatePerContainerList.add(charges);
                                } else if (charges.getCosttype().getCodedesc().equalsIgnoreCase(messageResources.getMessage("perbl"))) {
                                    perBlList.add(charges);
                                } else if (charges.getCosttype().getCodedesc().equalsIgnoreCase(messageResources.getMessage("per1000kg"))
                                        || charges.getCosttype().getCodedesc().equalsIgnoreCase(messageResources.getMessage("per2000lbs"))) {
                                    per2000kglbsList.add(charges);
                                } else if (charges.getCosttype().getCodedesc().equalsIgnoreCase(messageResources.getMessage("percentofr"))) {
                                    percentOfrList.add(charges);
                                } else if (charges.getCosttype().getCodedesc().equalsIgnoreCase(messageResources.getMessage("percentdrayage"))) {
                                    percentDrayageList.add(charges);
                                }
                            }
                        }
                    }
                }
            }
            if (fclBuy.getPolCode() != null) {//---for pol---
                UnLocation polUnLocation = unLocationDAO.getUnlocation((fclBuy.getPolCode()));
                if (null != polUnLocation) {
                    List portsTempList = portsDAO.findForUnlocCodeAndPortNameForDestinationServiceBYPortCity(polUnLocation.getUnLocationCode(), polUnLocation.getUnLocationName());
                    if (!portsTempList.isEmpty() && portsTempList.size() > 0) {
                        if (polUnLocation.getCountryId() != null) {
                            if (polUnLocation.getStateId() != null) {
                                quotation.setPlor(polUnLocation.getUnLocationName() + "/" + polUnLocation.getStateId().getCode() + "/"
                                        + polUnLocation.getCountryId().getCodedesc() + "(" + polUnLocation.getUnLocationCode() + ")");
                            } else {
                                quotation.setPlor(polUnLocation.getUnLocationName() + "/" + polUnLocation.getCountryId().getCodedesc() + "(" + polUnLocation.getUnLocationCode()
                                        + ")");
                            }
                        } else {
                            if (polUnLocation.getStateId() != null) {
                                quotation.setPlor(polUnLocation.getUnLocationName() + "/" + polUnLocation.getStateId().getCode() + "/" + polUnLocation.getCountryId().getCodedesc() + "(" + polUnLocation.getUnLocationCode() + ")");
                            } else {
                                quotation.setPlor(polUnLocation.getUnLocationName() + "/" + polUnLocation.getCountryId().getCodedesc() + "(" + polUnLocation.getUnLocationCode()
                                        + ")");
                            }
                        }
                    }
                }
            }
            if (fclBuy.getPodCode() != null) {//--for pod---
                UnLocation podUnLocation = unLocationDAO.getUnlocation((fclBuy.getPodCode()));
                if (null != podUnLocation) {
                    List portsTempList = portsDAO.findForUnlocCodeAndPortNameForDestinationServiceBYPortCity(podUnLocation.getUnLocationCode(), podUnLocation.getUnLocationName());
                    if (!portsTempList.isEmpty() && portsTempList.size() > 0) {
                        if (podUnLocation.getCountryId() != null) {
                            if (podUnLocation.getStateId() != null) {
                                quotation.setFinaldestination(podUnLocation.getUnLocationName() + "/" + podUnLocation.getStateId().getCode() + "/"
                                        + podUnLocation.getCountryId().getCodedesc() + "(" + podUnLocation.getUnLocationCode() + ")");
                            } else {
                                quotation.setFinaldestination(podUnLocation.getUnLocationName() + "/"
                                        + podUnLocation.getCountryId().getCodedesc() + "(" + podUnLocation.getUnLocationCode() + ")");
                            }
                        } else {
                            if (podUnLocation.getStateId() != null) {
                                quotation.setFinaldestination(podUnLocation.getUnLocationName() + "/" + podUnLocation.getStateId().getCode() + "("
                                        + podUnLocation.getUnLocationCode() + ")");
                            } else {
                                quotation.setFinaldestination(podUnLocation.getUnLocationName() + "("
                                        + podUnLocation.getUnLocationCode() + ")");
                            }

                        }
                    }
                }
            }
            if (fclBuy.getPodCode() != null && fclBuy.getPodCode().equals(quotesForm.getSelectedDestination())) {
                quotation.setDestination_port(quotesForm.getSelectedDestination());//--for destination---
            } else {
                //quotation.setFinaldestination(quotesForm.getSelectedDestination());//---for pod----
            }
        }
        request.setAttribute("QuotationCodes", quotation);
        HashMap hashMap = new HashMap();
        List fclRatesList = new ArrayList();
        for (int i = 0; i < chargesList.size(); i++) {
            Charges charges = (Charges) chargesList.get(i);
            Charges prevCharges = new Charges();
            if (i != 0) {
                prevCharges = (Charges) chargesList.get(i - 1);
                if (prevCharges.getUnitType() != null && charges.getUnitType() != null
                        && !prevCharges.getUnitType().equals(charges.getUnitType())) {
                    for (int s = 0; s < flatRatePerContainerList.size(); s++) {
                        Charges flatCharges = new Charges();
                        flatCharges = (Charges) flatRatePerContainerList.get(s);
                        Charges newCharges = new Charges();
                        if (flatCharges.getUnitType() == null) {
                            PropertyUtils.copyProperties(newCharges, flatCharges);
                            newCharges.setNumber("1");
                            newCharges.setInclude("on");
                            newCharges.setPrint("on");

                            newCharges.setUnitType(prevCharges.getUnitType());
                            if (hashMap.get(newCharges.getUnitType() + "-" + newCharges.getChargeCodeDesc()) == null) {
                                fclRatesList.add(newCharges);
                                hashMap.put(newCharges.getUnitType() + "-" + newCharges.getChargeCodeDesc(), newCharges);
                            }

                        }
                    }
                }
            }
            fclRatesList.add(charges);
            hashMap.put(charges.getUnitType() + "-" + charges.getChargeCodeDesc(), charges);
        }
        if (fclRatesList.size() > 0) {
            Charges charges = (Charges) chargesList.get(chargesList.size() - 1);
            for (int i = 0; i < flatRatePerContainerList.size(); i++) {
                Charges fclCharges = (Charges) flatRatePerContainerList.get(i);
                Charges newCharges = new Charges();
                if (fclCharges.getUnitType() == null) {
                    PropertyUtils.copyProperties(newCharges, fclCharges);
                    newCharges.setNumber("1");
                    newCharges.setInclude("on");
                    newCharges.setPrint("on");
                    newCharges.setUnitType(charges.getUnitType());
                    if (hashMap.get(newCharges.getUnitType() + "-" + newCharges.getChargeCodeDesc()) == null) {
                        fclRatesList.add(newCharges);
                        hashMap.put(newCharges.getUnitType() + "-" + newCharges.getChargeCodeDesc(), newCharges);
                    }
                }
            }
        }
        for (int i = 0; i < percentOfrList.size(); i++) {
            Charges charges = (Charges) percentOfrList.get(i);
            for (int j = 0; j < fclRatesList.size(); j++) {
                Charges prevCharges = (Charges) fclRatesList.get(j);
                if (charges.getChargeCode().getCodedesc().equalsIgnoreCase(messageResources.getMessage("oceanfreight"))) {
                    if (charges.getUnitType().equals(prevCharges.getUnitType())) {
                        charges.setAmount(prevCharges.getAmount() * charges.getAmount() / 100);
                        fclRatesList.add(charges);
                        break;
                    }
                }
            }
        }

        for (int i = 0; i < percentDrayageList.size(); i++) {
            Charges charges = (Charges) percentDrayageList.get(i);
            for (int j = 0; j < fclRatesList.size(); j++) {
                Charges prevCharges = (Charges) fclRatesList.get(j);
                if (charges.getChargeCode().getCodedesc().equalsIgnoreCase(messageResources.getMessage("dray"))) {
                    if (charges.getUnitType().equals(prevCharges.getUnitType())) {
                        charges.setAmount(prevCharges.getAmount() * charges.getAmount() / 100);
                        fclRatesList.add(charges);
                        break;
                    }
                }
            }
        }
        quotationDAO.save(quotation);
        boolean OfrPresent = false;
        for (Iterator iter = fclRatesList.iterator(); iter.hasNext();) {
            Charges charges = (Charges) iter.next();
            Charges newCharges = new Charges();
            PropertyUtils.copyProperties(newCharges, charges);
            newCharges.setQouteId(quotation.getQuoteId());
            newCharges.setId(null);
            newCharges.setStandardCharge("Y");
            if (charges.getChargeCodeDesc().equalsIgnoreCase("OCNFRT") || charges.getChargeCodeDesc().equalsIgnoreCase("OFIMP")) {
                OfrPresent = true;
            }
            if ("hazmat".equals(buttonValue) && "HAZFEE".equals(newCharges.getChargeCodeDesc()) && "Y".equalsIgnoreCase(quotation.getSpecialequipment())) {
                if (CommonUtils.isNotEmpty(newCharges.getUnitType())) {
                    GenericCode genericCode = genericCodeDAO.findById(Integer.parseInt(newCharges.getUnitType()));
                    if (null != genericCode) {
                        List l = chargesDAO.checkSpecialEquipmentHazmatCharges(quotation.getQuoteId(), newCharges.getUnitType(), newCharges.getSpecialEquipmentUnit(), "HAZMAT");
                        if (!l.isEmpty()) {
                            for (Iterator it = l.iterator(); it.hasNext();) {
                                Charges hazmatCharge = (Charges) it.next();
                                hazmatCharge.setAmount(newCharges.getAmount());
                                hazmatCharge.setMarkUp(newCharges.getMarkUp());
                            }
                        } else {
                            chargesDAO.saveManualHazmatSpecialEquipmentCharges(newCharges, genericCode.getCode());
                        }
                    }
                }
            }
            if ("hazmat".equals(buttonValue) && "Y".equalsIgnoreCase(quotation.getSpecialequipment())) {
                List l = chargesDAO.getStandardCharge(quotation.getQuoteId(), newCharges.getUnitType(), newCharges.getChargeCodeDesc());
                if (l.isEmpty()) {
                    restoreTheCommentsOfCharges(deletedChargesMap, newCharges);
                    chargesDAO.save(newCharges);
                }
            } else {
                restoreTheCommentsOfCharges(deletedChargesMap, newCharges);
                chargesDAO.save(newCharges);
            }
            //---METHOD FOR RESTORING THE COMMENTS FOR CHARGES COMING FROM RATES WHILE HAZMAT IS ADDED-----
        }
        if (!OfrPresent) {
            Iterator iter = fclRatesList.iterator();
            while (iter.hasNext()) {
                Charges newCharges = new Charges();
                Charges charges = (Charges) iter.next();
                PropertyUtils.copyProperties(newCharges, charges);
                newCharges.setQouteId(quotation.getQuoteId());
                newCharges.setChgCode("OCEAN FREIGHT");
                newCharges.setChargeCodeDesc("OCNFRT");
                newCharges.setId(null);
                newCharges.setAmount(0.00);
                newCharges.setMarkUp(0.00);
                newCharges.setStandardCharge("Y");
                chargesDAO.save(newCharges);
            }
        }

        for (Iterator iter = perBlList.iterator(); iter.hasNext();) {
            Charges charges = (Charges) iter.next();
            charges.setQouteId(quotation.getQuoteId());
            charges.setStandardCharge("Y");
            chargesDAO.save(charges);
        }
        for (Iterator iter = per2000kglbsList.iterator(); iter.hasNext();) {
            Charges charges = (Charges) iter.next();
            charges.setQouteId(quotation.getQuoteId());
            charges.setStandardCharge("Y");
            chargesDAO.save(charges);
        }
        String imsTrucker = quotesForm.getImsTrucker();
        String imsBuy = quotesForm.getImsBuy();
        String imsSell = quotesForm.getImsSell();
        String imsQuoteNo = quotesForm.getImsQuoteNo();
        String imsLocation = quotesForm.getImsLocation();
        if (null != quotation && CommonUtils.isNotEmpty(imsTrucker) && isImsQuote) {
            User user = (User) session.getAttribute("loginuser");
            new ChargesDAO().deleteCharges(quotation.getQuoteId(), "INLAND");
            TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
            List<Charges> list = new ChargesDAO().getGroupByCharges(quotation.getQuoteId());
            for (Charges ocnfrtCharges : list) {
                Charges inlandCharges = new Charges();
                PropertyUtils.copyProperties(inlandCharges, ocnfrtCharges);
                inlandCharges.setInclude("on");
                inlandCharges.setChargeFlag("M");
                if (isImsQuote && quotation.getFileType().equalsIgnoreCase("I")) {
                    inlandCharges.setChgCode("DELIVERY");
                    inlandCharges.setChargeCodeDesc("DELIV");
                } else {
                    inlandCharges.setChgCode("INLAND");
                    inlandCharges.setChargeCodeDesc("INLAND");
                }
                String truckerName = tradingPartnerDAO.getAccountName(imsTrucker);
                inlandCharges.setAccountNo(imsTrucker);
                inlandCharges.setAccountName(truckerName);
                inlandCharges.setCostType("FLAT RATE PER CONTAINER");
                inlandCharges.setEfectiveDate(new Date());
                inlandCharges.setUpdateOn(new Date());
                inlandCharges.setUpdateBy(user.getLoginName());
                inlandCharges.setAmount(Double.parseDouble(imsBuy));
                String quoteNumber = CommonUtils.isNotEmpty(imsQuoteNo) && CommonUtils.isNotEqualIgnoreCase(imsQuoteNo, "null") ? ("--" + imsQuoteNo) : "";
                inlandCharges.setComment(imsLocation + quoteNumber + "(" + user.getLoginName() + "-" + DateUtils.formatDate(new Date(), "MM/dd/yyyy") + ").");
                inlandCharges.setMarkUp(Double.parseDouble(imsSell));
                inlandCharges.setUnitName(new GenericCodeDAO().findById(Integer.parseInt(ocnfrtCharges.getUnitType())).getCodedesc());
                new ChargesDAO().save(inlandCharges);
                quotation.setInland("Y");
                new QuotationDAO().update(quotation);
                new NotesBC().saveNotesWhileAddingCharges("FILE", quotation.getFileNo(), user.getLoginName(), inlandCharges);
            }
        }
    }

    public QuotesForm getRemarksandTransitDaysFromFclSellrates(QuotesForm quotesForm) throws Exception {
        UnLocation originTerminal = null;
        UnLocation DestinationPort = null;
        UnLocation portOfloading = null;
        UnLocation portOfDischarge = null;
        String unlocationCode = "";
        UnLocation rampcity = null;
        List fclOrgDestMiscData = null;
        String Origin = "";
        if (quotesForm.getIsTerminal() != null) {
            Origin = quotesForm.getIsTerminal();
            int i = Origin.indexOf("/");
            if (i != -1) {
                String a[] = Origin.split("/");
                Origin = a[0];
                if (quotesForm.getIsTerminal().lastIndexOf("(") != -1) {
                    unlocationCode = quotesForm.getIsTerminal().substring(quotesForm.getIsTerminal().lastIndexOf("(") + 1,
                            quotesForm.getIsTerminal().lastIndexOf(")"));
                } else {
                    unlocationCode = a[1];
                }
            }

            List list = unLocationDAO.findForManagement(unlocationCode.trim(), Origin.trim());
            if (list != null && list.size() > 0) {
                originTerminal = (UnLocation) list.get(0);
            }
        }
        String rampCity = "";
        if (quotesForm.getRampCity() != null) {
            rampCity = quotesForm.getRampCity();
            int i = rampCity.indexOf("/");
            if (i != -1) {
                String a[] = rampCity.split("/");

                rampCity = a[0];
                if (quotesForm.getRampCity().lastIndexOf("(") != -1) {
                    unlocationCode = quotesForm.getRampCity().substring(quotesForm.getRampCity().lastIndexOf("(") + 1,
                            quotesForm.getRampCity().lastIndexOf(")"));
                } else {
                    unlocationCode = a[1];
                }
            }

            List list = unLocationDAO.findForManagement(unlocationCode.trim(), rampCity.trim());
            if (list != null && list.size() > 0) {
                rampcity = (UnLocation) list.get(0);
            }
        }

        unlocationCode = "";
        String pol = "";
        if (quotesForm.getPlaceofReceipt() != null) {
            pol = quotesForm.getPlaceofReceipt();
            int k = pol.indexOf("/");
            if (k != -1) {
                String a[] = pol.split("/");
                pol = a[0];
                unlocationCode = a[1];
            }

            List list2 = unLocationDAO.findForManagement(unlocationCode.trim(), pol.trim());
            if (list2 != null && list2.size() > 0) {
                portOfloading = (UnLocation) list2.get(0);
            }
        }
        unlocationCode = "";
        String pod = "";
        if (quotesForm.getFinalDestination() != null) {
            pod = quotesForm.getFinalDestination();
            int l = pod.indexOf("/");
            if (l != -1) {
                String a[] = pod.split("/");
                pod = a[0];
                unlocationCode = a[1];
            }
            List list3 = unLocationDAO.findForManagement(unlocationCode.trim(), pod.trim());
            if (list3 != null && list3.size() > 0) {
                portOfDischarge = (UnLocation) list3.get(0);
            }
        }
        unlocationCode = "";
        String Destination = "";
        if (quotesForm.getPortofDischarge() != null) {
            Destination = quotesForm.getPortofDischarge();
            int j = Destination.indexOf("/");
            if (j != -1) {
                String a[] = Destination.split("/");
                Destination = a[0];
                if (quotesForm.getPortofDischarge().lastIndexOf("(") != -1) {
                    unlocationCode = quotesForm.getPortofDischarge().substring(quotesForm.getPortofDischarge().lastIndexOf("(") + 1,
                            quotesForm.getPortofDischarge().lastIndexOf(")"));
                } else {
                    unlocationCode = a[1];
                }
            }
            List list1 = unLocationDAO.findForManagement(unlocationCode.trim(), Destination.trim());
            quotesForm.setFclTempRemarks(unLocationDAO.getDestinationTempRemarks(Destination));
            if (CommonUtils.isEqualIgnoreCase(quotesForm.getBulletRatesCheck(), "on")) {
                quotesForm.setFclGRIRemarks("");
            } else {
                quotesForm.setFclGRIRemarks(unLocationDAO.getDestinationGRIRemarks(Destination, quotesForm.getSslcode(), unlocationCode));
            }
            if (list1 != null && list1.size() > 0) {
                DestinationPort = (UnLocation) list1.get(0);
            }
        }
        List newTradingPartnerTempList = customerDAO.findAccountNo1(quotesForm.getSslcode());
        TradingPartnerTemp newTradingPartnerTemp = new TradingPartnerTemp();
        if (newTradingPartnerTempList.size() > 0) {
            newTradingPartnerTemp = (TradingPartnerTemp) newTradingPartnerTempList.get(0);
        }
        if (CommonFunctions.isNotNull(originTerminal) && CommonFunctions.isNotNull(DestinationPort) && null != newTradingPartnerTemp) {
            fclOrgDestMiscData = fclOrgDestMiscDataDAO.getorgdestmiscdate(originTerminal, DestinationPort, newTradingPartnerTemp);
        }

        if (null != fclOrgDestMiscData && fclOrgDestMiscData.size() > 0) {
            FclOrgDestMiscData tempFclOrgDestMiscData = (FclOrgDestMiscData) fclOrgDestMiscData.get(0);
            quotesForm.setRatesRemarks(tempFclOrgDestMiscData.getRemarks());
            if (tempFclOrgDestMiscData.getDaysInTransit() != null) {
                quotesForm.setNoOfDays(String.valueOf(tempFclOrgDestMiscData.getDaysInTransit()));
            }
        } else if (null != rampcity) {
            if (CommonFunctions.isNotNull(DestinationPort) && null != newTradingPartnerTemp) {
                fclOrgDestMiscData = fclOrgDestMiscDataDAO.getorgdestmiscdate(rampcity, DestinationPort, newTradingPartnerTemp);
            }
            if (null != fclOrgDestMiscData && fclOrgDestMiscData.size() > 0) {
                FclOrgDestMiscData tempFclOrgDestMiscData = (FclOrgDestMiscData) fclOrgDestMiscData.get(0);
                quotesForm.setRatesRemarks(tempFclOrgDestMiscData.getRemarks());
                if (tempFclOrgDestMiscData.getDaysInTransit() != null) {
                    quotesForm.setNoOfDays(String.valueOf(tempFclOrgDestMiscData.getDaysInTransit()));
                }
            } else {
                if (CommonFunctions.isNotNull(portOfloading) && CommonFunctions.isNotNull(DestinationPort) && null != newTradingPartnerTemp) {
                    fclOrgDestMiscData = fclOrgDestMiscDataDAO.getorgdestmiscdate(portOfloading, DestinationPort, newTradingPartnerTemp);
                }
                if (null != fclOrgDestMiscData && fclOrgDestMiscData.size() > 0) {
                    FclOrgDestMiscData tempFclOrgDestMiscData = (FclOrgDestMiscData) fclOrgDestMiscData.get(0);
                    quotesForm.setRatesRemarks(tempFclOrgDestMiscData.getRemarks());
                    if (tempFclOrgDestMiscData.getDaysInTransit() != null) {
                        quotesForm.setNoOfDays(String.valueOf(tempFclOrgDestMiscData.getDaysInTransit()));
                    }
                } else {
                    if (CommonFunctions.isNotNull(originTerminal) && CommonFunctions.isNotNull(portOfDischarge) && null != newTradingPartnerTemp) {
                        fclOrgDestMiscData = fclOrgDestMiscDataDAO.getorgdestmiscdate(originTerminal, portOfDischarge, newTradingPartnerTemp);
                    }
                    if (null != fclOrgDestMiscData && fclOrgDestMiscData.size() > 0) {
                        FclOrgDestMiscData tempFclOrgDestMiscData = (FclOrgDestMiscData) fclOrgDestMiscData.get(0);
                        quotesForm.setRatesRemarks(tempFclOrgDestMiscData.getRemarks());
                        if (tempFclOrgDestMiscData.getDaysInTransit() != null) {
                            quotesForm.setNoOfDays(String.valueOf(tempFclOrgDestMiscData.getDaysInTransit()));
                        }
                    } else {
                        if (CommonFunctions.isNotNull(rampcity) && CommonFunctions.isNotNull(portOfDischarge) && null != newTradingPartnerTemp) {
                            fclOrgDestMiscData = fclOrgDestMiscDataDAO.getorgdestmiscdate(rampcity, portOfDischarge, newTradingPartnerTemp);
                        }
                        if (null != fclOrgDestMiscData && fclOrgDestMiscData.size() > 0) {
                            FclOrgDestMiscData tempFclOrgDestMiscData = (FclOrgDestMiscData) fclOrgDestMiscData.get(0);
                            quotesForm.setRatesRemarks(tempFclOrgDestMiscData.getRemarks());
                            if (tempFclOrgDestMiscData.getDaysInTransit() != null) {
                                quotesForm.setNoOfDays(String.valueOf(tempFclOrgDestMiscData.getDaysInTransit()));
                            }
                        } else {
                            if (CommonFunctions.isNotNull(portOfloading) && CommonFunctions.isNotNull(portOfDischarge) && null != newTradingPartnerTemp) {
                                fclOrgDestMiscData = fclOrgDestMiscDataDAO.getorgdestmiscdate(portOfloading, portOfDischarge, newTradingPartnerTemp);
                            }
                            if (null != fclOrgDestMiscData && fclOrgDestMiscData.size() > 0) {
                                FclOrgDestMiscData tempFclOrgDestMiscData = (FclOrgDestMiscData) fclOrgDestMiscData.get(0);
                                quotesForm.setRatesRemarks(tempFclOrgDestMiscData.getRemarks());
                                if (tempFclOrgDestMiscData.getDaysInTransit() != null) {
                                    quotesForm.setNoOfDays(String.valueOf(tempFclOrgDestMiscData.getDaysInTransit()));
                                }
                            }
                        }
                    }
                }
            }
        }
        return quotesForm;
    }

    public Quotation getRemarksandTransitDaysFromFclSellratesForEdit(Quotation quotation) throws Exception {
        UnLocation originTerminal = null;
        UnLocation rampcity = null;
        UnLocation DestinationPort = null;
        UnLocation portOfloading = null;
        UnLocation portOfDischarge = null;
        String unlocationCode = "";
        String Origin = "";
        String rampCity = "";
        List fclOrgDestMiscData = null;
        if (quotation.getOrigin_terminal() != null) {
            Origin = quotation.getOrigin_terminal();
            int i = Origin.indexOf("/");
            if (i != -1) {
                String a[] = Origin.split("/");
                Origin = a[0];
                unlocationCode = quotation.getOrigin_terminal().substring(quotation.getOrigin_terminal().lastIndexOf("(") + 1, quotation.getOrigin_terminal().lastIndexOf(")"));
            }
            List list = unLocationDAO.findForManagement(unlocationCode.trim(), Origin.trim());
            if (list != null && list.size() > 0) {
                originTerminal = (UnLocation) list.get(0);
            }
        }
        if (quotation.getRampCity() != null) {
            rampCity = quotation.getRampCity();
            int i = rampCity.indexOf("/");
            if (i != -1) {
                String a[] = rampCity.split("/");
                rampCity = a[0];
                unlocationCode = a[1];
            }
            List list = unLocationDAO.findForManagement(unlocationCode.trim(), rampCity.trim());
            if (list != null && list.size() > 0) {
                rampcity = (UnLocation) list.get(0);
            }
        }
        unlocationCode = "";
        String pol = "";
        if (quotation.getPlor() != null) {
            pol = quotation.getPlor();
            int k = pol.indexOf("/");
            if (k != -1) {
                String a[] = pol.split("/");
                pol = a[0];
                unlocationCode = a[1];
            }

            List list2 = unLocationDAO.findForManagement(unlocationCode.trim(), pol.trim());
            if (list2 != null && list2.size() > 0) {
                portOfloading = (UnLocation) list2.get(0);
            }
        }
        unlocationCode = "";
        String pod = "";
        if (quotation.getFinaldestination() != null) {
            pod = quotation.getFinaldestination();
            int l = pod.indexOf("/");
            if (l != -1) {
                String a[] = pod.split("/");
                pod = a[0];
                unlocationCode = a[1];
            }
            List list3 = unLocationDAO.findForManagement(unlocationCode.trim(), pod.trim());
            if (list3 != null && list3.size() > 0) {
                portOfDischarge = (UnLocation) list3.get(0);
            }
        }
        unlocationCode = "";
        String Destination = "";
        if (quotation.getDestination_port() != null) {
            Destination = quotation.getDestination_port();
            int j = Destination.indexOf("/");
            if (j != -1) {
                String a[] = Destination.split("/");
                Destination = a[0];
                unlocationCode = quotation.getDestination_port().substring(quotation.getDestination_port().lastIndexOf("(") + 1, quotation.getDestination_port().lastIndexOf(")"));
            }
            List list1 = unLocationDAO.findForManagement(unlocationCode.trim(), Destination.trim());
            quotation.setFclTempRemarks(unLocationDAO.getDestinationTempRemarks(Destination));
            if (CommonUtils.isEqualIgnoreCase(quotation.getBulletRatesCheck(), "on")) {
                quotation.setFclGRIRemarks("");
            } else {
                quotation.setFclGRIRemarks(unLocationDAO.getDestinationGRIRemarks(Destination, quotation.getSsline(), unlocationCode));
            }
            if (list1 != null && list1.size() > 0) {
                DestinationPort = (UnLocation) list1.get(0);
            }
        }
        List newTradingPartnerTempList = customerDAO.findAccountNo1(quotation.getSsline());
        TradingPartnerTemp newTradingPartnerTemp = new TradingPartnerTemp();
        if (newTradingPartnerTempList.size() > 0) {
            newTradingPartnerTemp = (TradingPartnerTemp) newTradingPartnerTempList.get(0);
        }
        if (null != newTradingPartnerTemp && CommonFunctions.isNotNull(originTerminal) && CommonFunctions.isNotNull(DestinationPort)) {
            fclOrgDestMiscData = fclOrgDestMiscDataDAO.getorgdestmiscdate(originTerminal, DestinationPort, newTradingPartnerTemp);
        }

        if (null != fclOrgDestMiscData && fclOrgDestMiscData.size() > 0) {
            FclOrgDestMiscData tempFclOrgDestMiscData = (FclOrgDestMiscData) fclOrgDestMiscData.get(0);

            quotation.setRatesRemarks(tempFclOrgDestMiscData.getRemarks());
            if (tempFclOrgDestMiscData.getDaysInTransit() != null) {
                quotation.setNoOfDays(String.valueOf(tempFclOrgDestMiscData.getDaysInTransit()));
            }
        } else if (rampcity != null) {
            if (null != newTradingPartnerTemp && CommonFunctions.isNotNull(rampcity) && CommonFunctions.isNotNull(DestinationPort)) {
                fclOrgDestMiscData = fclOrgDestMiscDataDAO.getorgdestmiscdate(rampcity, DestinationPort, newTradingPartnerTemp);
            }
            if (null != fclOrgDestMiscData && fclOrgDestMiscData.size() > 0) {
                FclOrgDestMiscData tempFclOrgDestMiscData = (FclOrgDestMiscData) fclOrgDestMiscData.get(0);
                quotation.setRatesRemarks(tempFclOrgDestMiscData.getRemarks());
                if (tempFclOrgDestMiscData.getDaysInTransit() != null) {
                    quotation.setNoOfDays(String.valueOf(tempFclOrgDestMiscData.getDaysInTransit()));
                }
            } else {
                if (null != newTradingPartnerTemp && CommonFunctions.isNotNull(portOfloading) && CommonFunctions.isNotNull(DestinationPort)) {
                    fclOrgDestMiscData = fclOrgDestMiscDataDAO.getorgdestmiscdate(portOfloading, DestinationPort, newTradingPartnerTemp);
                }
                if (null != newTradingPartnerTemp && fclOrgDestMiscData.size() > 0) {
                    FclOrgDestMiscData tempFclOrgDestMiscData = (FclOrgDestMiscData) fclOrgDestMiscData.get(0);
                    quotation.setRatesRemarks(tempFclOrgDestMiscData.getRemarks());
                    if (tempFclOrgDestMiscData.getDaysInTransit() != null) {
                        quotation.setNoOfDays(String.valueOf(tempFclOrgDestMiscData.getDaysInTransit()));
                    }
                } else {
                    if (null != newTradingPartnerTemp && CommonFunctions.isNotNull(originTerminal) && CommonFunctions.isNotNull(portOfDischarge)) {
                        fclOrgDestMiscData = fclOrgDestMiscDataDAO.getorgdestmiscdate(originTerminal, portOfDischarge, newTradingPartnerTemp);
                    }
                    if (null != newTradingPartnerTemp && fclOrgDestMiscData.size() > 0) {
                        FclOrgDestMiscData tempFclOrgDestMiscData = (FclOrgDestMiscData) fclOrgDestMiscData.get(0);
                        quotation.setRatesRemarks(tempFclOrgDestMiscData.getRemarks());
                        if (tempFclOrgDestMiscData.getDaysInTransit() != null) {
                            quotation.setNoOfDays(String.valueOf(tempFclOrgDestMiscData.getDaysInTransit()));
                        }
                    } else {
                        if (null != newTradingPartnerTemp && CommonFunctions.isNotNull(originTerminal) && CommonFunctions.isNotNull(portOfDischarge)) {
                            fclOrgDestMiscData = fclOrgDestMiscDataDAO.getorgdestmiscdate(rampcity, portOfDischarge, newTradingPartnerTemp);
                        }
                        if (null != newTradingPartnerTemp && fclOrgDestMiscData.size() > 0) {
                            FclOrgDestMiscData tempFclOrgDestMiscData = (FclOrgDestMiscData) fclOrgDestMiscData.get(0);
                            quotation.setRatesRemarks(tempFclOrgDestMiscData.getRemarks());
                            if (tempFclOrgDestMiscData.getDaysInTransit() != null) {
                                quotation.setNoOfDays(String.valueOf(tempFclOrgDestMiscData.getDaysInTransit()));
                            }
                        } else {
                            if (null != newTradingPartnerTemp && CommonFunctions.isNotNull(portOfloading) && CommonFunctions.isNotNull(portOfDischarge)) {
                                fclOrgDestMiscData = fclOrgDestMiscDataDAO.getorgdestmiscdate(portOfloading, portOfDischarge, newTradingPartnerTemp);
                            }
                            if (null != newTradingPartnerTemp && fclOrgDestMiscData.size() > 0) {
                                FclOrgDestMiscData tempFclOrgDestMiscData = (FclOrgDestMiscData) fclOrgDestMiscData.get(0);
                                quotation.setRatesRemarks(tempFclOrgDestMiscData.getRemarks());
                                if (tempFclOrgDestMiscData.getDaysInTransit() != null) {
                                    quotation.setNoOfDays(String.valueOf(tempFclOrgDestMiscData.getDaysInTransit()));
                                }
                            }
                        }
                    }
                }
            }
        }
        return quotation;
    }

    public Charges setMarkUpValues(Charges cb1, String chargeMarkUp, Charges cb3, String numb1) throws Exception {
        cb1.setMarkUp(dbUtil.getDivideAmount(Double.parseDouble(dbUtil.removeComma(chargeMarkUp)), cb3.getNumber(), numb1));
        return cb1;
    }

    public Charges setAdjistMentValues(Charges cb1, String chargeMarkUp, Charges cb3, String numb1) throws Exception {
        cb1.setAdjestment(dbUtil.getDivideAmount(Double.parseDouble(dbUtil.removeComma(chargeMarkUp)), cb3.getNumber(), numb1));
        return cb1;
    }
    // in the charges grid when they change the number of containers

    public List<Charges> getNumbersChanged(List testRates, QuotesForm quotesForm, String userName) throws Exception {
        String rowindex = quotesForm.getNumbIdx();
        Charges charges = new Charges();
        for (Iterator iterator = testRates.iterator(); iterator.hasNext();) {
            charges = new Charges();
            charges = (Charges) iterator.next();
            if (charges.getUnitName().equalsIgnoreCase(rowindex)) {
                break;
            }
        }
        //charges=(Charges)testRates.get(Integer.parseInt(rowindex));
        String ut = charges.getUnitType();
        int j = 0;
        Charges charges1 = null;
        Charges charges3 = null;
        String numb1 = quotesForm.getNumbers1();
        String chargeMarkUp[] = quotesForm.getChargeMarkUp();
        String chargeAdjustment[] = quotesForm.getAdjestment();
        String numbers[] = quotesForm.getNumbers();
        List<Charges> changedfclRates = new ArrayList();
        while (j < testRates.size()) {
            charges1 = new Charges();
            charges3 = (Charges) testRates.get(j);
            charges1.setChargeCodeDesc(charges3.getChargeCodeDesc());
            charges1.setChgCode(charges3.getChgCode());
            charges1.setId(charges3.getId());
            charges1.setCostType(charges3.getCostType());
            charges1.setUnitType(charges3.getUnitType());
            charges1.setUnitName(charges3.getUnitName());
            charges1.setCurrecny(charges3.getCurrecny());
            charges1.setChargeFlag(charges3.getChargeFlag());
            charges1.setEfectiveDate(charges3.getEfectiveDate());

            String u1 = "";
            if (!charges3.getUnitType().equals("0.00")) {
                u1 = charges3.getUnitType();
            }
            if (u1.equals(ut) && charges3.getUnitType().trim().equals("0.00")) {
                if (charges3.getRetail() != null) {
                    charges1.setRetail(dbUtil.getDivideAmount(charges3.getRetail(), charges3.getNumber(), numb1));
                }
            }
            if (charges3.getUnitType().equals(ut)) {
                charges1.setNumber(numb1);
                if (quotesForm.getSpotRate().equals("Y")) {
                    if (charges3.getSpotRateAmt() != null) {
                        charges1.setSpotRateAmt(dbUtil.getDivideAmount(charges3.getSpotRateAmt(), charges3.getNumber(), numb1));
                    }
                } else {
                    if (charges3.getAmount() != null) {
                        charges1.setAmount(dbUtil.getDivideAmount(charges3.getAmount(), charges3.getNumber(), numb1));
                    }
                }
                if (quotesForm.getRateId() != null && quotesForm.getRateId().equals("Expand")) {
                    if (Integer.parseInt(charges3.getNumber()) > Integer.parseInt(numb1)) {
                        if (chargeMarkUp != null && chargeMarkUp[j] != null) {
                            charges1 = setMarkUpValues(charges1, chargeMarkUp[j], charges3, numb1);
                        }
                        if (chargeAdjustment != null && chargeAdjustment[j] != null) {
                            charges1 = setAdjistMentValues(charges1, chargeAdjustment[j], charges3, numb1);
                        }
                    } else {
                        if (chargeMarkUp != null && chargeMarkUp[j] != null) {
                            charges1 = setMarkUpValues(charges1, chargeMarkUp[j], charges3, numb1);
                        }
                        if (chargeAdjustment != null && chargeAdjustment[j] != null) {
                            charges1 = setAdjistMentValues(charges1, chargeAdjustment[j], charges3, numb1);
                        }
                    }
                } else {
                    charges1.setMarkUp(charges3.getMarkUp() * Double.parseDouble(charges1.getNumber()) / Double.parseDouble(charges3.getNumber()));
                    if (charges3.getAdjestment() != null) {
                        charges1.setAdjestment(charges3.getAdjestment() * Double.parseDouble(charges1.getNumber()) / Double.parseDouble(charges3.getNumber()));
                    }
                }
            } else {
                if (quotesForm.getSpotRate().equals("Y")) {
                    charges1.setSpotRateAmt(charges3.getSpotRateAmt());
                } else {
                    charges1.setAmount(charges3.getAmount());
                }
                charges1.setNumber(charges3.getNumber());
                if (charges3.getEfectiveDate() != null) {
                    charges1.setEfectiveDate(charges3.getEfectiveDate());
                }
                if (charges1.getRetail() == null || charges1.getRetail().equals(" ") || charges1.getRetail().equals("0.00")) {
                    charges1.setRetail(charges3.getRetail());
                }
                if (quotesForm.getRateId() != null && quotesForm.getRateId().equals("Expand")) {
                    if (chargeMarkUp != null && chargeMarkUp[j] != null) {
                        charges1.setMarkUp(Double.parseDouble(dbUtil.removeComma(chargeMarkUp[j])));
                    }
                } else {
                    charges1.setMarkUp(charges3.getMarkUp());
                }
                if (chargeAdjustment != null && chargeAdjustment[j] != null) {
                    charges1.setAdjestment(Double.parseDouble(dbUtil.removeComma(chargeAdjustment[j])));
                } else {
                    charges1.setAdjestment(charges3.getAdjestment());
                }
            }
            charges1.setUpdateBy(userName);
            charges1.setUpdateOn(new Date());
            changedfclRates.add(charges1);
            charges1 = null;
            j++;
        }
        return changedfclRates;
    }

    //to get the origin terminal number from from terminal table using terminal name
    public String findForManagement(String terminal, HttpServletRequest request) throws Exception {
        String origin = null;
        List originList = null;
        String unLocationCode = StringFormatter.orgDestStringFormatter(terminal);
        terminal = StringFormatter.getTerminalFromInputStringr(terminal);
        if (null != unLocationCode && null != terminal) {
            originList = unLocationDAO.findForManagement(unLocationCode.trim(), terminal.trim());
        }
        if (originList != null && originList.size() > 0) {
            unLocation = (UnLocation) originList.get(0);
            origin = unLocation.getId().toString();
            if (request != null) {
                request.setAttribute("destination", unLocation.getCountryId().getCodedesc()
                        + "/" + unLocation.getUnLocationName() + "/(" + unLocation.getUnLocationCode() + ")");
            }
        }
        return origin;
    }

    public String findForManagementofDest(String terminal, HttpServletRequest request) throws Exception {
        String unLocationCode = StringFormatter.orgDestStringFormatter(terminal);
        terminal = StringFormatter.getTerminalFromInputStringr(terminal);
        String DestList = portsDAO.getAllSynonymousCityforDest(unLocationCode.trim());
        return DestList;
    }

    //to get the commodity id from from genericcodedup table using commodity name
    public GenericCode findForGenericCode(String commdValue) throws Exception {
        List commodityList = genericCodeDAO.findForGenericCode(commdValue);
        if (commodityList != null && commodityList.size() > 0) {
            genericCode = (GenericCode) commodityList.get(0);
        }
        return genericCode;
    }

    //get all the unit types
    public List<GenericCode> getAllUnitCodeForFCLTestforList(Integer codeTypeId) throws Exception {
        List unitTypeList = genericCodeDAO.getAllUnitCodeForFCLTestforList(codeTypeId);
        return unitTypeList;
    }

    // to set the quotationform values into quotation object
    public Quotation save(QuotesForm quotesForm) throws Exception {
        Quotation quotation = null;
        if (quotesForm.getQuoteId() != null && !quotesForm.getQuoteId().equals("")) {

            quotation = quotationDAO.findById(Integer.parseInt(quotesForm.getQuoteId()));
            if (quotation == null) {
                quotation = new Quotation();
            }
        } else {
            quotation = new Quotation();
        }
        Date date = null;
        if (quotesForm.getQuotationDate() != null) {
            date = dateFormat2.parse(quotesForm.getQuotationDate());
        } else {
            date = new Date();
        }
        if (quotesForm.getCcEmail() != null && quotesForm.getCcEmail().equalsIgnoreCase("on")) {
            quotation.setCcEmail("on");
        } else {
            quotation.setCcEmail("off");
        }
        quotation.setRoutedbyAgentsCountry(quotesForm.getRoutedbyAgentsCountry());
        quotation.setRatesRemarks(quotesForm.getRatesRemarks());
        quotation.setSpotRate(quotesForm.getSpotRate());
        quotation.setFclTempRemarks(quotesForm.getFclTempRemarks());
        if (CommonUtils.isEqualIgnoreCase(quotesForm.getBulletRatesCheck(), "on")) {
            quotation.setFclGRIRemarks("");
        } else {
            quotation.setFclGRIRemarks(quotesForm.getFclGRIRemarks());
        }
        quotation.setRemarksFlag(quotesForm.getPrintRemarks());
        quotation.setRatesNonRates(quotesForm.getRatesNonRates());
        quotation.setDirectConsignmntCheck(quotesForm.getDirectConsignmntCheck());
        quotation.setBulletRatesCheck(quotesForm.getBulletRatesCheck());
        if (quotesForm.getBreakBulk() != null) {
            quotation.setBreakBulk(quotesForm.getBreakBulk());
        }
        quotation.setRemarks(quotesForm.getRemarks());
        quotation.setOriginCheck(quotesForm.getNewOrigin());
        quotation.setCcEmail(quotesForm.getCcEmail());
        quotation.setPolCheck(quotesForm.getNewRampCity());
        quotation.setDestinationCheck(quotesForm.getNewDestination());
        quotation.setRampCheck(quotesForm.getRampCheck());
        quotation.setDoorDestination(quotesForm.getDoorDestination());
        quotation.setDoorOrigin(quotesForm.getDoorOrigin());
        quotation.setQuoteDate(date);
        quotation.setQuoteBy(quotesForm.getQuoteBy());
        quotation.setAgent(quotesForm.getAgent());
        quotation.setVendorName(quotesForm.getVendorName());
        quotation.setVendorNo(quotesForm.getAccountNo());
        if (quotesForm.getNewClient() != null && quotesForm.getNewClient().equalsIgnoreCase("on")) {
            quotation.setClientname(quotesForm.getCustomerName1());
        } else {
            quotation.setClientname(quotesForm.getCustomerName());
        }
        quotation.setClientnumber(quotesForm.getClientNumber());
        quotation.setClienttype(quotesForm.getClienttype());
        quotation.setContactname(quotesForm.getContactName());
        quotation.setFax(quotesForm.getFax());
        quotation.setPhone(quotesForm.getPhone());
        if (quotesForm.getInsuranceCharge() != null && !quotesForm.getInsuranceCharge().equals("")) {
            quotation.setInsuranceCharge(Double.parseDouble(dbUtil.removeComma(quotesForm.getInsuranceCharge())));
        }
        quotation.setEmail1(quotesForm.getEmail());
        quotation.setOrigin_terminal(quotesForm.getIsTerminal());
        quotation.setRampCity(quotesForm.getRampCity());
        quotation.setPlor(quotesForm.getPlaceofReceipt());
        quotation.setFinaldestination(quotesForm.getFinalDestination());
        quotation.setDestination_port(quotesForm.getPortofDischarge());
        quotation.setPoe(quotesForm.getPoe());
        //quotation.setTypeofMove(quotesForm.getTypeofMove());
        quotation.setRoutedbymsg(quotesForm.getRoutedbymsg());
        quotation.setPrintDesc(quotesForm.getPrintDesc());
        quotation.setFinalized(quotesForm.getFinalized());
        quotation.setZip(quotesForm.getZip());
        quotation.setAgentNo(quotesForm.getAgentNo());
        if (quotesForm.getTransitTime() != null && !quotesForm.getTransitTime().equals("")) {
            quotation.setTransitTime(Integer.parseInt(quotesForm.getTransitTime()));
        }
        quotation.setAlternateagent(quotesForm.getAlternateagent());
        quotation.setIssuingTerminal(quotesForm.getIssuingTerminal());
        quotation.setCarrier(quotesForm.getSsline());
        quotation.setSslname(quotesForm.getSslDescription());
        quotation.setCarrierContact(quotesForm.getCarrierContact());
        quotation.setCarrierEmail(quotesForm.getCarrierEmail());
        quotation.setCarrierPhone(quotesForm.getCarrierPhone());
        quotation.setCarrierFax(quotesForm.getCarrierFax());
        quotation.setCarrierPrint(quotesForm.getCarrierPrint());
        if (quotesForm.getCommcode() != null && !quotesForm.getCommcode().equals("") && !quotesForm.getCommcode().equals("%")) {
            List comList = genericCodeDAO.findForGenericCode(quotesForm.getCommcode());
            if (comList != null && comList.size() > 0) {
                GenericCode genericObject = (GenericCode) comList.get(0);
                quotation.setCommcode(genericObject);
                quotation.setDescription(genericObject.getCodedesc());
            }
        }
//        if (quotesForm.getDescription() != null && !quotesForm.getDescription().equals("")) {
//            List codeList = genericCodeDAO.findForAirRates(null, quotesForm.getDescription());
//            if (codeList != null && codeList.size() > 0) {
//                GenericCode genObj = (GenericCode) codeList.get(0);
//                quotation.setCommcode(genObj);
//                quotation.setDescription(genObj.getCodedesc());
//            }
//        }

        quotation.setSsline(quotesForm.getSslcode());
        quotation.setCommodityPrint(quotesForm.getCommodityPrint());
        quotation.setSpclEqpmt(quotesForm.getSpecialEqpmt());
        quotation.setSpecialequipment(quotesForm.getSpecialequipment());
        quotation.setHazmat(quotesForm.getHazmat());
        quotation.setOutofgage(quotesForm.getOutofgate());
        quotation.setSoc(quotesForm.getSoc());
        quotation.setDefaultAgent(quotesForm.getDefaultAgent());
        quotation.setCustomertoprovideSed(quotesForm.getCustomertoprovideSED());
//        quotation.setLocaldryage(quotesForm.getLocaldryage());
        quotation.setIntermodel(quotesForm.getIntermodel());
        quotation.setDocCharge(quotesForm.getDocCharge());
        quotation.setPierPass(quotesForm.getPierPass());
        quotation.setChassisCharge(quotesForm.getChassisCharge());
        quotation.setInsurance(quotesForm.getInsurance());
        quotation.setDeductFfcomm(quotesForm.getDeductFFcomm());
        quotation.setNoOfDays(quotesForm.getNoOfDays());
        if (quotesForm.getAmount() == null || quotesForm.getAmount().equals("")) {
            quotesForm.setAmount("0.00");
        }
        if (quotesForm.getAmount1() == null || quotesForm.getAmount1().equals("")) {
            quotesForm.setAmount1("0.00");
        }
        if (quotesForm.getCostofgoods() == null || quotesForm.getCostofgoods().equals("")) {
            quotesForm.setCostofgoods("0.00");
        }
        quotation.setAmount(Double.parseDouble(dbUtil.removeComma(quotesForm.getAmount())));
        quotation.setAmount1(Double.parseDouble(dbUtil.removeComma(quotesForm.getAmount1())));
        quotation.setInsurancamt(Double.parseDouble(dbUtil.removeComma(quotesForm.getInsurancamt())));
        quotation.setCostofgoods(Double.parseDouble(dbUtil.removeComma(quotesForm.getCostofgoods())));
        quotation.setRoutedAgentCheck(quotesForm.getRoutedAgent());
        quotation.setLdprint(quotesForm.getLdprint());
        quotation.setInsureinclude(quotesForm.getInsureinclude());
        quotation.setInsureprint(quotesForm.getInsureprint());
        quotation.setIdinclude(quotesForm.getIdinclude());
        quotation.setIdprint(quotesForm.getIdprint());
        quotation.setGoodsdesc(quotesForm.getGoodsdesc());
        quotation.setComment1(CommonFunctions.isNotNull(quotesForm.getComment())
                ? quotesForm.getComment().toUpperCase() : quotesForm.getComment());

        quotation.setQuoteFlag("Open");
        quotation.setRoutedAgentCheck(quotesForm.getRoutedAgentCheck());

        if (quotesForm.getNewClient() != null) {
            quotation.setNewClient(quotesForm.getNewClient());
        } else {
            quotation.setNewClient("off");
        }
        if (quotesForm.getClientConsigneeCheck() != null) {
            quotation.setClientConsigneeCheck(quotesForm.getClientConsigneeCheck());
        } else {
            quotation.setClientConsigneeCheck("off");
        }
        quotation.setTypeofMove(quotesForm.getTypeofMove());
        quotation.setFileType(quotesForm.getFileType());
        quotation.setImportantDisclosures(quotesForm.getImportantDisclosures());
        quotation.setPrintPortRemarks(quotesForm.getPrintPortRemarks());
        quotation.setDocsInquiries(quotesForm.getDocsInquiries());
        quotation.setChangeIssuingTerminal(quotesForm.getChangeIssuingTerminal());
        quotation.setBrand(quotesForm.getBrand());
        quotation.setDocumentAmount(CommonUtils.isNotEmpty(quotesForm.getDocChargeAmount()) ? Double.parseDouble(quotesForm.getDocChargeAmount()) : 0d);
        return quotation;
    }

    public Quotation getQuotationObj(SearchQuotationForm editQuotesform) throws Exception {
        Quotation editquote = new Quotation();
        if (null != editQuotesform.getQuotationNo() && !editQuotesform.getQuotationNo().equals("")) {
            editquote = quotationDAO.findById(new Integer(editQuotesform.getQuotationNo()));
        }
        return editquote;
    }

    public Quotation getFormData(SearchQuotationForm editQuotesform) throws Exception {
        Quotation editquote = new Quotation();
        editquote.setAesFilling(editQuotesform.isAesFilling());
        if (editQuotesform.getButtonValue() != null && editQuotesform.getButtonValue().equalsIgnoreCase("validate")) {
            Quotation quote = quotationDAO.findById(new Integer(editQuotesform.getQuotationNo()));
            PropertyUtils.copyProperties(editquote, quote);
        } else {
            editquote = quotationDAO.findById(null != editQuotesform.getQuotationNo() ? new Integer(editQuotesform.getQuotationNo()) : 0);
        }
        if (null != editquote && null == editquote.getFinalized()) {
            editquote.setFinalized("off");
        }
        if (null != editquote && !editquote.getFinalized().equals("on")) {
            DBUtil dbUtil = new DBUtil();
            GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
            GenericCode genericObject = new GenericCode();
            editquote.setRatesRemarks(editQuotesform.getRatesRemarks());
            //editquote.setFclTempRemarks(editQuotesform.getFclTempRemarks());
            editquote.setRampCity(editQuotesform.getRampCity());
            editquote.setRoutedbyAgentsCountry(editQuotesform.getRoutedbyAgentsCountry());
            if (editquote.getQuoteDate() != null) {
                editquote.setQuoteDate(editquote.getQuoteDate());
            }
            editquote.setRemarks(editQuotesform.getRemarks());
            editquote.setSpotRate(editQuotesform.getSpotRate());
            editquote.setOriginCheck(editQuotesform.getOriginCheck());
            editquote.setPolCheck(editQuotesform.getPolCheck());
            editquote.setRampCheck(editQuotesform.getRampCheck());
            editquote.setDestinationCheck(editQuotesform.getDestinationCheck());
            editquote.setDirectConsignmntCheck(editQuotesform.getDirectConsignmntCheck());
            editquote.setBulletRatesCheck(editQuotesform.getBulletRatesCheck());
            editquote.setAgentNo(editQuotesform.getAgentNo());
            editquote.setQuoteNo(editQuotesform.getQuotationNo());
            editquote.setRemarksFlag(editQuotesform.getPrintRemarks());
            editquote.setDoorDestination(editQuotesform.getDoorDestination());
            editquote.setDoorOrigin(editQuotesform.getDoorOrigin());
            if (editQuotesform.getBreakBulk() != null) {
                editquote.setBreakBulk(editQuotesform.getBreakBulk());
            }
            if (editQuotesform.getNewClient() != null && editQuotesform.getNewClient().equalsIgnoreCase("on")) {
                editquote.setClientname(editQuotesform.getCustomerName1());
            } else {
                editquote.setClientname(editQuotesform.getCustomerName());
            }
            editquote.setClientnumber(editQuotesform.getClientNumber());
            editquote.setContactname(editQuotesform.getContactName());
            editquote.setPhone(editQuotesform.getPhone());
            editquote.setFax(editQuotesform.getFax());

            editquote.setRatesNonRates(editquote.getRatesNonRates());
            editquote.setZip(editQuotesform.getZip());
            editquote.setAgent(editQuotesform.getAgent());
            editquote.setVendorName(editQuotesform.getVendorName());
            editquote.setVendorNo(editQuotesform.getAccountNo());
            editquote.setDescription(editQuotesform.getDescription());
            editquote.setEmail1(editQuotesform.getEmail());
            editquote.setSoc(editQuotesform.getSoc());
            //editquote.setSpecialEqpmt(editQuotesform.getSpecialEqpmt());
            editquote.setComment1(editQuotesform.getComment());
            editquote.setAlternateagent(editQuotesform.getDefaultAgent());
            editquote.setIssuingTerminal(editQuotesform.getIssuingTerminal());
            editquote.setFinaldestination(editQuotesform.getFinalDestination());
            editquote.setPlor(editQuotesform.getPlaceofReceipt());
            editquote.setPlod(editQuotesform.getPlod());
            editquote.setStateoforigin(editQuotesform.getStateofOrigin());
            editquote.setRoutedAgentCheck(editQuotesform.getRoutedAgentCheck());
            editquote.setPrintDesc(editQuotesform.getPrintDesc());
            editquote.setDefaultAgent(editQuotesform.getDefaultAgent());
            editquote.setLdprint(editQuotesform.getLdprint());
            editquote.setIdinclude(editQuotesform.getIdinclude());
            editquote.setIdprint(editQuotesform.getIdprint());
            editquote.setInsureinclude(editQuotesform.getInsureinclude());
            editquote.setFrom(editQuotesform.getFrom());
            editquote.setInsureprint(editQuotesform.getInsureprint());
            editquote.setNoOfDays(editQuotesform.getNoOfDays());
            if (editQuotesform.getInsurancamt() != null && !editQuotesform.getInsurancamt().equals("")) {
                editquote.setInsurancamt(Double.parseDouble(dbUtil.removeComma(editQuotesform.getInsurancamt())));
            }
            if (editQuotesform.getCommcode() != null && !editQuotesform.getCommcode().equals("") && !editQuotesform.getCommcode().equals("%")) {
                List comList = genericCodeDAO.findForGenericCode(editQuotesform.getCommcode());
                if (comList != null && comList.size() > 0) {
                    genericObject = (GenericCode) comList.get(0);
                    editquote.setCommcode(genericObject);
                }
            }
            if (editQuotesform.getAmount() == null) {
                editQuotesform.setAmount("0.00");
            }
            double amt = Double.parseDouble(dbUtil.removeComma(editQuotesform.getAmount()));
            Double totalCharges = 0.00;
            editquote.setAmount(amt);
            if (editQuotesform.getAmount1() == null) {
                editQuotesform.setAmount1("0.00");
            }
            double amt1 = Double.parseDouble(dbUtil.removeComma(editQuotesform.getAmount1()));
            editquote.setAmount1(amt1);
            if (editQuotesform.getIdinclude() != null && editQuotesform.getIdinclude().equals("on")) {
                totalCharges = totalCharges + amt1;
            }
            if (editQuotesform.getInsuranceCharge() == null || editQuotesform.getInsuranceCharge().equals("")) {
                editQuotesform.setInsuranceCharge("0.00");
            }

            double insamt = Double.parseDouble(dbUtil.removeComma(editQuotesform.getInsuranceCharge()));
            editquote.setInsuranceCharge(insamt);

            totalCharges = totalCharges + insamt;

            if (editQuotesform.getTransitTime() != null && !editQuotesform.getTransitTime().equals("")) {
                editquote.setTransitTime(new Integer(editQuotesform.getTransitTime()));
            }
            //--MARK Up---
            if (editQuotesform.getDrayageMarkUp() == null) {
                editQuotesform.setDrayageMarkUp("0.00");
            }
            double drayMarkup = Double.parseDouble(dbUtil.removeComma(editQuotesform.getDrayageMarkUp()));
            editquote.setDrayageMarkUp(drayMarkup);

            if (editQuotesform.getIntermodalMarkUp() == null) {
                editQuotesform.setIntermodalMarkUp("0.00");
            }
            double interMarkup = Double.parseDouble(dbUtil.removeComma(editQuotesform.getIntermodalMarkUp()));
            editquote.setIntermodalMarkUp(interMarkup);

            if (editQuotesform.getInsuranceMarkUp() == null) {
                editQuotesform.setInsuranceMarkUp("0.00");
            }
            double insureMarkup = Double.parseDouble(dbUtil.removeComma(editQuotesform.getInsuranceMarkUp()));
            editquote.setInsuranceMarkUp(insureMarkup);

            editquote.setSpecialequipment(editQuotesform.getSpecialequipment());
            editquote.setHazmat(editQuotesform.getHazmat());
            editquote.setOutofgage(editQuotesform.getOutofgate());
            editquote.setCustomertoprovideSed(editQuotesform.getCustomertoprovideSED());
            editquote.setDeductFfcomm(editQuotesform.getDeductFFcomm());
            editquote.setSsline(editQuotesform.getSslcode());
            if (editQuotesform.getSslDescription() != null) {
                if (editQuotesform.getSslDescription().indexOf("//") != -1) {
                    String[] car = editQuotesform.getSslDescription().split("//");
                    if (null != car && car.length >= 0) {
                        editquote.setSslname(car[0]);
                    }
                } else {
                    if (null != editQuotesform.getSslDescription()
                            && !editQuotesform.getSslDescription().equals("//")) {
                        editquote.setSslname(editQuotesform.getSslDescription());
                    } else {
                        editquote.setSslname("");
                    }
                }
            }
            editquote.setCarrierPrint(editQuotesform.getCarrierPrint());
            editquote.setImportantDisclosures(editQuotesform.getImportantDisclosures());
            editquote.setDocsInquiries(editQuotesform.getDocsInquiries());
            editquote.setChangeIssuingTerminal(editQuotesform.getChangeIssuingTerminal());
            editquote.setPrintPortRemarks(editQuotesform.getPrintPortRemarks());
            editquote.setCommodityPrint(editQuotesform.getCommodityPrint());
            editquote.setCarrierContact(editQuotesform.getCarrierContact());
            editquote.setCarrierEmail(editQuotesform.getCarrierEmail());
            editquote.setCarrierFax(editQuotesform.getCarrierFax());
            editquote.setCarrierPhone(editQuotesform.getCarrierPhone());
            editquote.setCarrier(editQuotesform.getSslDescription());
            editquote.setLocaldryage(editQuotesform.getLocaldryage());
            editquote.setIntermodel(editQuotesform.getIntermodel());
            editquote.setInland(editQuotesform.getInland());
            editquote.setDocCharge(editQuotesform.getDocCharge());
            editquote.setPierPass(editQuotesform.getPierPass());
            editquote.setChassisCharge(editQuotesform.getChassisCharge());
            editquote.setInsurance(editQuotesform.getInsurance());
            editquote.setClienttype(editQuotesform.getClienttype());
            if (editQuotesform.getTypeofMove() != null) {
                editquote.setTypeofMove(editQuotesform.getTypeofMove());
            }
            editquote.setRoutedbymsg(editQuotesform.getRoutedbymsg());
            editquote.setGoodsdesc(editQuotesform.getGoodsdesc());
            editquote.setCostofgoods(Double.parseDouble(dbUtil.removeComma(editQuotesform.getCostofgoods())));
            editquote.setTotalCharges(totalCharges);
            editquote.setOrigin_terminal(editQuotesform.getIsTerminal());
            editquote.setRampCity(editQuotesform.getRampCity());
            editquote.setDestination_port(editQuotesform.getPortofDischarge());
            editquote.setPoe(editQuotesform.getPoe());
            editquote.setFinalized(editQuotesform.getFinalized());
            editquote.setCcEmail(editQuotesform.getCcEmail());
            editquote.setFileType(editQuotesform.getFileType());
            editquote.setBrand(editQuotesform.getBrand());
            if (editQuotesform.getNewClient() == null) {
                editquote.setNewClient("off");
            } else {
                editquote.setNewClient(editQuotesform.getNewClient());
            }
            if (editQuotesform.getClientConsigneeCheck() != null) {
                editquote.setClientConsigneeCheck(editQuotesform.getClientConsigneeCheck());
            } else {
                editquote.setClientConsigneeCheck("off");
            }
        }
        if (editQuotesform.getTypeofMove() != null) {
            editquote.setTypeofMove(editQuotesform.getTypeofMove());
        }
        editquote.setDocumentAmount(CommonUtils.isNotEmpty(editQuotesform.getDocChargeAmount()) ? Double.parseDouble(editQuotesform.getDocChargeAmount()) : 0d);
        if (null != editquote.getFileType() && "I".equalsIgnoreCase(editquote.getFileType())
                && null != editQuotesform.getUnitSizeSelected()) {
            String units = null;
            if ("converttobook".equalsIgnoreCase(editQuotesform.getButtonValue())) {
                units = genericCodeDAO.getAllUnits(editquote.getFileNo());
            } else {
                units = editQuotesform.getUnitSizeSelected();
            }
            editquote.setSelectedUnits(units);
        }
        return editquote;
    }

    public void save(List<CostBean> ratesList, SearchQuotationForm form, MessageResources messageResources) throws Exception {
        ChargesDAO chargesDAO = new ChargesDAO();
        int i = 0;
        List list1 = chargesDAO.getQuoteId(new Integer(form.getQuotationNo()));
        for (int j = 0; j < list1.size(); j++) {
            Charges charges = (Charges) list1.get(j);
            chargesDAO.delete(charges);
        }
        for (Iterator iterator = ratesList.iterator(); iterator.hasNext();) {
            Charges charges = (Charges) iterator.next();
            charges.setQouteId(new Integer(form.getQuotationNo()));
            chargesDAO.save(charges);
            i++;
        }
    }

    //rates in charges grid differentiate into fclrates, other charges and perkglist and save quotation and charges
    public Quotation getRatesandSave(QuotesForm quotesForm, MessageResources messageResources, List rates, Quotation quotationObject, String quote) throws Exception {
        List tempindexList = new ArrayList();
        List perkglbsList = new ArrayList<CostBean>();
        List<CostBean> ratesList1 = new ArrayList<CostBean>();
        List otherChargesList = new ArrayList();
        List<CostBean> ratesList = new ArrayList<CostBean>();
        List<CostBean> specialEquipmentList = new ArrayList<CostBean>();
        List chargesList = new ArrayList();
        Double totalCharges = 0.00;
        Double won = 0.00;
        Double baht = 0.00;
        Double nt = 0.00;
        Double yen = 0.00;
        Double inr = 0.00;
        Double hkd = 0.0;
        Double rmb = 0.00;
        Double prs = 0.00;
        Double lkr = 0.00;
        Double BDT = 0.00;
        Double eur = 0.00;
        Double cyp = 0.00;
        Double myr = 0.00;
        Double nht = 0.00;
        Double pkr = 0.00;
        Double rm = 0.00;
        Double spo = 0.00;
        Double vnd = 0.00;
        String currencies[] = messageResources.getMessage("currency").split(",");
        StringTokenizer st = new StringTokenizer(quotesForm.getSelectedCheck(), ",");
        while (st.hasMoreTokens()) {
            String t = st.nextToken();
            if (t.equals("E=45")) {
                t = "E=45'102";
            }
            tempindexList.add(t);
        }
        List unitsList = genericCodeDAO.getAllUnitCodeForFCLTestforListforFclRates(new Integer(38));
        String spclEquipments[] = messageResources.getMessage("spclequipment").split(",");

        if (quotesForm.getSpecialEqpmt() != null && quotesForm.getSpecialEqpmt().equals(spclEquipments[0])) {
            tempindexList.add("20-OT");
            tempindexList.add("40-OT");
        }
        if (quotesForm.getSpecialEqpmt() != null && quotesForm.getSpecialEqpmt().equals(spclEquipments[1])) {
            tempindexList.add("20-FR");
            tempindexList.add("40-FR");
        }
        if (quotesForm.getSpecialEqpmt() != null && quotesForm.getSpecialEqpmt().equals(spclEquipments[2])) {
            tempindexList.add("20-HT");
            tempindexList.add("40-HARD TOP");
        }
        if (quotesForm.getSpecialEqpmt() != null && quotesForm.getSpecialEqpmt().equals(spclEquipments[3])) {
            tempindexList.add("20- REEFER");
            tempindexList.add("40- REEFER");
        }
        if (quotesForm.getSpecialEqpmt() != null && quotesForm.getSpecialEqpmt().equals(spclEquipments[4])) {
            tempindexList.add("40- REEFER HIGH CUBE");
        }
        if (quotesForm.getSpecialEqpmt() != null && quotesForm.getSpecialEqpmt().equals(spclEquipments[5])) {
            //tempindexList.add("45"HC");
        }
        Integer unit = 0;
        int k = 0;
        while (k < tempindexList.size()) {
            //int unitIdx=(Integer)tempindexList.get(k);
            String unitIdx = "";
            unitIdx = (String) tempindexList.get(k);

            for (Iterator iter = unitsList.iterator(); iter.hasNext();) {
                GenericCode genericCode = (GenericCode) iter.next();
                if (genericCode.getCodedesc().equalsIgnoreCase(unitIdx)) {
                    unit = genericCode.getId();
                    break;
                }
            }

            // unitIdx=unitsList.get(unit);
            int i = 0;
            while (i < rates.size()) {
                CostBean cb = new CostBean();
                cb = (CostBean) rates.get(i);
                cb.setAccountName(quotesForm.getSslDescription());
                cb.setAccountNo(quotesForm.getSslcode());
                boolean perflag = false;
                if (cb.getChargecode() != null && (((cb.getChargecode().trim().equalsIgnoreCase(messageResources.getMessage("hazardousSurcharge")) || cb.getChargecode().trim().equalsIgnoreCase(messageResources.getMessage("hazardousSurchargeland")) || cb.getChargecode().trim().equalsIgnoreCase(messageResources.getMessage("hazardousSurchargesea")) || cb.getChargecode().trim().equalsIgnoreCase(messageResources.getMessage("hazfeecertun")) || cb.getChargecode().trim().equalsIgnoreCase(messageResources.getMessage("hazardcertificate")) || cb.getChargeCodedesc().trim().equalsIgnoreCase(messageResources.getMessage("hazardous")) || cb.getChargecode().trim().equalsIgnoreCase(messageResources.getMessage("hazardousfee")))
                        && quotesForm.getHazmat().equals("N")) || (cb.getChargecode().trim().equals(messageResources.getMessage("spclequipmentsurcharge")) && quotesForm.getSpecialequipment().equals("N"))
                        || (cb.getChargeCodedesc() != null && cb.getChargeCodedesc().trim().equals(messageResources.getMessage("soc")) && quotesForm.getSoc().equals("N")))) {
                } else {
                    if (cb.getCostType().trim().equalsIgnoreCase(messageResources.getMessage("per1000kg")) || cb.getCostType().trim().equalsIgnoreCase(messageResources.getMessage("per2000lbs"))) {
                        if (perkglbsList.size() > 0) {
                            for (int l = 0; l < perkglbsList.size(); l++) {
                                CostBean c1 = (CostBean) perkglbsList.get(l);
                                if (c1.getCostType().trim().equals(cb.getCostType().trim()) && c1.getChargecode().trim().equals(cb.getChargecode().trim())) {
                                    perflag = true;
                                    break;
                                }
                            }
                            if (!perflag) {
                                perkglbsList.add(cb);
                            }
                        } else {
                            perkglbsList.add(cb);
                        }

                    }
                }
                if (cb.getUnitType() != null && cb.getUnitType().equals("0.00") && cb.getCostType().trim().equals(messageResources.getMessage("perbl"))) {
                    if (cb.getChargecode() != null && (((cb.getChargecode().trim().equals(messageResources.getMessage("hazardousSurcharge"))
                            || cb.getChargecode().trim().equals(messageResources.getMessage("hazardousSurchargeland")) || cb.getChargecode().trim().equals(messageResources.getMessage("hazardousSurchargesea")) || cb.getChargecode().trim().equalsIgnoreCase(messageResources.getMessage("hazfeecertun")) || cb.getChargecode().trim().equalsIgnoreCase(messageResources.getMessage("hazardcertificate")) || cb.getChargeCodedesc().trim().equalsIgnoreCase(messageResources.getMessage("hazardous")) || cb.getChargecode().trim().equalsIgnoreCase(messageResources.getMessage("hazardousfee"))) && quotesForm.getHazmat().equals("N")) || (cb.getChargecode().trim().equals(messageResources.getMessage("spclequipmentsurcharge"))
                            && quotesForm.getSpecialequipment().equals("N")) || (cb.getChargeCodedesc().trim().equals(messageResources.getMessage("soc")) && quotesForm.getSoc().equals("N")))) {
                    } else {
                        cb.setOtherinclude("on");
                        cb.setOtherprint("on");
                        boolean flag = false;
                        if (otherChargesList.size() > 0) {
                            for (int a = 0; a < otherChargesList.size(); a++) {
                                CostBean c1 = (CostBean) otherChargesList.get(a);
                                if (c1.getChargecode().equals(cb.getChargecode())) {
                                    flag = true;
                                    break;
                                }
                            }
                            if (!flag) {
                                otherChargesList.add(cb);
                                if (cb.getCurrency().trim().equals(currencies[7])) {
                                    totalCharges = totalCharges + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                                } else if (cb.getCurrency().trim().equals(currencies[8])) {
                                    baht = baht + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                                } else if (cb.getCurrency().trim().equals(currencies[9])) {
                                    BDT = BDT + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                                } else if (cb.getCurrency().trim().equals(currencies[10])) {
                                    cyp = cyp + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                                } else if (cb.getCurrency().trim().equals(currencies[11])) {
                                    eur = eur + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                                } else if (cb.getCurrency().trim().equals(currencies[12])) {
                                    hkd = hkd + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                                } else if (cb.getCurrency().trim().equals(currencies[13])) {
                                    lkr = lkr + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                                } else if (cb.getCurrency().trim().equals(currencies[14])) {
                                    nt = nt + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                                } else if (cb.getCurrency().trim().equals(currencies[15])) {
                                    prs = prs + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                                } else if (cb.getCurrency().trim().equals(currencies[16])) {
                                    rmb = rmb + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                                } else if (cb.getCurrency().trim().equals(currencies[17])) {
                                    won = won + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                                } else if (cb.getCurrency().trim().equals(currencies[18])) {
                                    yen = yen + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                                } else if (cb.getCurrency().trim().equals(currencies[6])) {
                                    inr = inr + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                                } else if (cb.getCurrency().trim().equals(currencies[0])) {
                                    myr = myr + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                                } else if (cb.getCurrency().trim().equals(currencies[1])) {
                                    nht = nht + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                                } else if (cb.getCurrency().trim().equals(currencies[2])) {
                                    pkr = pkr + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                                } else if (cb.getCurrency().trim().equals(currencies[3])) {
                                    rm = rm + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                                } else if (cb.getCurrency().trim().equals(currencies[4])) {
                                    spo = spo + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                                } else if (cb.getCurrency().trim().equals(currencies[5])) {
                                    vnd = vnd + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                                }
                            }
                        } else {
                            otherChargesList.add(cb);
                            if (cb.getCurrency().trim().equals(currencies[7])) {
                                totalCharges = totalCharges + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                            } else if (cb.getCurrency().trim().equals(currencies[8])) {
                                baht = baht + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                            } else if (cb.getCurrency().trim().equals(currencies[9])) {
                                BDT = BDT + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                            } else if (cb.getCurrency().trim().equals(currencies[10])) {
                                cyp = cyp + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                            } else if (cb.getCurrency().trim().equals(currencies[11])) {
                                eur = eur + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                            } else if (cb.getCurrency().trim().equals(currencies[12])) {
                                hkd = hkd + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                            } else if (cb.getCurrency().trim().equals(currencies[13])) {
                                lkr = lkr + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                            } else if (cb.getCurrency().trim().equals(currencies[14])) {
                                nt = nt + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                            } else if (cb.getCurrency().trim().equals(currencies[15])) {
                                prs = prs + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                            } else if (cb.getCurrency().trim().equals(currencies[16])) {
                                rmb = rmb + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                            } else if (cb.getCurrency().trim().equals(currencies[17])) {
                                won = won + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                            } else if (cb.getCurrency().trim().equals(currencies[18])) {
                                yen = yen + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                            } else if (cb.getCurrency().trim().equals(currencies[6])) {
                                inr = inr + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                            } else if (cb.getCurrency().trim().equals(currencies[0])) {
                                myr = myr + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                            } else if (cb.getCurrency().trim().equals(currencies[1])) {
                                nht = nht + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                            } else if (cb.getCurrency().trim().equals(currencies[2])) {
                                pkr = pkr + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                            } else if (cb.getCurrency().trim().equals(currencies[3])) {
                                rm = rm + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                            } else if (cb.getCurrency().trim().equals(currencies[4])) {
                                spo = spo + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                            } else if (cb.getCurrency().trim().equals(currencies[5])) {
                                vnd = vnd + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                            }
                        }
                    }
                } else if (cb.getUnitType() != null && cb.getUnitType().equals("0.00") && cb.getCostType().trim().equals(messageResources.getMessage("Flatratepercontainer"))) {
                    if (cb.getChargecode() != null && (((cb.getChargecode().trim().equals(messageResources.getMessage("hazardousSurcharge")) || cb.getChargecode().trim().equals(messageResources.getMessage("hazardousSurchargeland")) || cb.getChargecode().trim().equals(messageResources.getMessage("hazardousSurchargesea")) || cb.getChargecode().trim().equalsIgnoreCase(messageResources.getMessage("hazfeecertun")) || cb.getChargecode().trim().equalsIgnoreCase(messageResources.getMessage("hazardcertificate")) || cb.getChargeCodedesc().trim().equalsIgnoreCase(messageResources.getMessage("hazardous")) || cb.getChargecode().trim().equalsIgnoreCase(messageResources.getMessage("hazardousfee"))) && quotesForm.getHazmat().equals("N")) || (cb.getChargecode().trim().equals(messageResources.getMessage("spclequipmentsurcharge")) && quotesForm.getSpecialequipment().equals("N"))
                            || (cb.getChargeCodedesc().trim().equals(messageResources.getMessage("soc")) && quotesForm.getSoc().equals("N")))) {
                    } else {
                        cb.setInclude("on");
                        cb.setPrint("on");
                        boolean flag = false;
                        if (ratesList1.size() > 0) {
                            for (int l = 0; l < ratesList1.size(); l++) {
                                CostBean c1 = (CostBean) ratesList1.get(l);
                                if (c1.getChargecode().equals(cb.getChargecode())) {
                                    flag = true;
                                    break;
                                }
                            }
                            if (!flag) {

                                ratesList1.add(cb);
                                if (cb.getCurrency().trim().equals(currencies[7])) {
                                    totalCharges = totalCharges + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                                } else if (cb.getCurrency().trim().equals(currencies[8])) {
                                    baht = baht + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                                } else if (cb.getCurrency().trim().equals(currencies[9])) {
                                    BDT = BDT + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                                } else if (cb.getCurrency().trim().equals(currencies[10])) {
                                    cyp = cyp + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                                } else if (cb.getCurrency().trim().equals(currencies[11])) {
                                    eur = eur + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                                } else if (cb.getCurrency().trim().equals(currencies[12])) {
                                    hkd = hkd + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                                } else if (cb.getCurrency().trim().equals(currencies[13])) {
                                    lkr = lkr + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                                } else if (cb.getCurrency().trim().equals(currencies[14])) {
                                    nt = nt + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                                } else if (cb.getCurrency().trim().equals(currencies[15])) {
                                    prs = prs + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                                } else if (cb.getCurrency().trim().equals(currencies[16])) {
                                    rmb = rmb + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                                } else if (cb.getCurrency().trim().equals(currencies[17])) {
                                    won = won + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                                } else if (cb.getCurrency().trim().equals(currencies[18])) {
                                    yen = yen + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                                } else if (cb.getCurrency().trim().equals(currencies[6])) {
                                    inr = inr + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                                } else if (cb.getCurrency().trim().equals(currencies[0])) {
                                    myr = myr + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                                } else if (cb.getCurrency().trim().equals(currencies[1])) {
                                    nht = nht + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                                } else if (cb.getCurrency().trim().equals(currencies[2])) {
                                    pkr = pkr + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                                } else if (cb.getCurrency().trim().equals(currencies[3])) {
                                    rm = rm + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                                } else if (cb.getCurrency().trim().equals(currencies[4])) {
                                    spo = spo + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                                } else if (cb.getCurrency().trim().equals(currencies[5])) {
                                    vnd = vnd + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                                }
                            }
                        } else {
                            ratesList1.add(cb);
                            if (cb.getCurrency().trim().equals(currencies[7])) {
                                totalCharges = totalCharges + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                            } else if (cb.getCurrency().trim().equals(currencies[8])) {
                                baht = baht + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                            } else if (cb.getCurrency().trim().equals(currencies[9])) {
                                BDT = BDT + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                            } else if (cb.getCurrency().trim().equals(currencies[10])) {
                                cyp = cyp + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                            } else if (cb.getCurrency().trim().equals(currencies[11])) {
                                eur = eur + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                            } else if (cb.getCurrency().trim().equals(currencies[12])) {
                                hkd = hkd + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                            } else if (cb.getCurrency().trim().equals(currencies[13])) {
                                lkr = lkr + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                            } else if (cb.getCurrency().trim().equals(currencies[14])) {
                                nt = nt + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                            } else if (cb.getCurrency().trim().equals(currencies[15])) {
                                prs = prs + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                            } else if (cb.getCurrency().trim().equals(currencies[16])) {
                                rmb = rmb + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                            } else if (cb.getCurrency().trim().equals(currencies[17])) {
                                won = won + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                            } else if (cb.getCurrency().trim().equals(currencies[18])) {
                                yen = yen + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                            } else if (cb.getCurrency().trim().equals(currencies[6])) {
                                inr = inr + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                            } else if (cb.getCurrency().trim().equals(currencies[0])) {
                                myr = myr + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                            } else if (cb.getCurrency().trim().equals(currencies[1])) {
                                nht = nht + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                            } else if (cb.getCurrency().trim().equals(currencies[2])) {
                                pkr = pkr + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                            } else if (cb.getCurrency().trim().equals(currencies[3])) {
                                rm = rm + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                            } else if (cb.getCurrency().trim().equals(currencies[4])) {
                                spo = spo + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                            } else if (cb.getCurrency().trim().equals(currencies[5])) {
                                vnd = vnd + Double.parseDouble(dbUtil.dayList1(cb.getRetail()));
                            }
                        }
                    }
                } else {
                    if (cb.getSetA() == null) {
                        cb.setSetA("0.00");
                    }
                    if (cb.getSetB() == null) {
                        cb.setSetB("0.00");
                    }
                    if (cb.getSetC() == null) {
                        cb.setSetC("0.00");
                    }
                    if (cb.getSetD() == null) {
                        cb.setSetD("0.00");
                    }
                    if (cb.getSetE() == null) {
                        cb.setSetE("0.00");
                    }
                    if (cb.getSetF() == null) {
                        cb.setSetF("0.00");
                    }
                    if (cb.getSetG() == null) {
                        cb.setSetG("0.00");
                    }
                    if (cb.getSetH() == null) {
                        cb.setSetH("0.00");
                    }
                    if (cb.getSetI() == null) {
                        cb.setSetI("0.00");
                    }
                    if (cb.getSetJ() == null) {
                        cb.setSetJ("0.00");
                    }
                    if (cb.getSetK() == null) {
                        cb.setSetK("0.00");
                    }
                    if (cb.getSetL() == null) {
                        cb.setSetL("0.00");
                    }
                    if (cb.getSetM() == null) {
                        cb.setSetM("0.00");
                    }
                    if (cb.getSetN() == null) {
                        cb.setSetN("0.00");
                    }
                    if (cb.getSetO() == null) {
                        cb.setSetO("0.00");
                    }
                    if (cb.getSetP() == null) {
                        cb.setSetP("0.00");
                    }
                    if (cb.getSetQ() == null) {
                        cb.setSetQ("0.00");
                    }
                    if (cb.getSetR() == null) {
                        cb.setSetR("0.00");
                    }
                    if (cb.getFutureRateA() == null) {
                        cb.setFutureRateA("0.00");
                    }
                    if (cb.getFutureRateB() == null) {
                        cb.setFutureRateB("0.00");
                    }
                    if (cb.getFutureRateC() == null) {
                        cb.setFutureRateC("0.00");
                    }
                    if (cb.getFutureRateD() == null) {
                        cb.setFutureRateD("0.00");
                    }
                    if (cb.getFutureRateE() == null) {
                        cb.setFutureRateE("0.00");
                    }
                    if (cb.getFutureRateF() == null) {
                        cb.setFutureRateF("0.00");
                    }
                    if (cb.getFutureRateG() == null) {
                        cb.setFutureRateG("0.00");
                    }
                    if (cb.getFutureRateH() == null) {
                        cb.setFutureRateH("0.00");
                    }
                    if (cb.getFutureRateI() == null) {
                        cb.setFutureRateI("0.00");
                    }
                    if (cb.getFutureRateJ() == null) {
                        cb.setFutureRateJ("0.00");
                    }
                    if (cb.getFutureRateK() == null) {
                        cb.setFutureRateK("0.00");
                    }
                    if (cb.getFutureRateL() == null) {
                        cb.setFutureRateL("0.00");
                    }
                    if (cb.getFutureRateM() == null) {
                        cb.setFutureRateM("0.00");
                    }
                    if (cb.getFutureRateN() == null) {
                        cb.setFutureRateN("0.00");
                    }
                    if (cb.getFutureRateO() == null) {
                        cb.setFutureRateO("0.00");
                    }
                    if (cb.getFutureRateP() == null) {
                        cb.setFutureRateP("0.00");
                    }
                    if (cb.getFutureRateQ() == null) {
                        cb.setFutureRateQ("0.00");
                    }

                    if (cb.getMarkUpA() == null) {
                        cb.setMarkUpA("0.00");
                    }
                    if (cb.getMarkUpB() == null) {
                        cb.setMarkUpB("0.00");
                    }
                    if (cb.getMarkUpC() == null) {
                        cb.setMarkUpC("0.00");
                    }
                    if (cb.getMarkUpD() == null) {
                        cb.setMarkUpD("0.00");
                    }
                    if (cb.getMarkUpE() == null) {
                        cb.setMarkUpE("0.00");
                    }
                    if (cb.getMarkUpF() == null) {
                        cb.setMarkUpF("0.00");
                    }
                    if (cb.getMarkUpG() == null) {
                        cb.setMarkUpG("0.00");
                    }
                    if (cb.getMarkUpH() == null) {
                        cb.setMarkUpH("0.00");
                    }
                    if (cb.getMarkUpI() == null) {
                        cb.setMarkUpI("0.00");
                    }
                    if (cb.getMarkUpJ() == null) {
                        cb.setMarkUpJ("0.00");
                    }
                    if (cb.getMarkUpK() == null) {
                        cb.setMarkUpK("0.00");
                    }
                    if (cb.getMarkUpL() == null) {
                        cb.setMarkUpL("0.00");
                    }
                    if (cb.getMarkUpM() == null) {
                        cb.setMarkUpM("0.00");
                    }
                    if (cb.getMarkUpN() == null) {
                        cb.setMarkUpN("0.00");
                    }
                    if (cb.getMarkUpO() == null) {
                        cb.setMarkUpO("0.00");
                    }
                    if (cb.getMarkUpQ() == null) {
                        cb.setMarkUpQ("0.00");
                    }
                    if (cb.getMarkUpP() == null) {
                        cb.setMarkUpP("0.00");
                    }
                    if (cb.getMarkUpR() == null) {
                        cb.setMarkUpR("0.00");
                    }
                    String ssName = cb.getSsLineNumber();
                    if (cb.getChargecode() != null && (((cb.getChargecode().trim().equals(messageResources.getMessage("hazardousSurcharge")) || cb.getChargecode().trim().equals(messageResources.getMessage("hazardousSurchargeland")) || cb.getChargecode().trim().equals(messageResources.getMessage("hazardousSurchargesea")) || cb.getChargecode().trim().equalsIgnoreCase(messageResources.getMessage("hazfeecertun")) || cb.getChargecode().trim().equalsIgnoreCase(messageResources.getMessage("hazardcertificate")) || cb.getChargeCodedesc().trim().equalsIgnoreCase(messageResources.getMessage("hazardous")) || cb.getChargecode().trim().equalsIgnoreCase(messageResources.getMessage("hazardousfee"))) && quotesForm.getHazmat().equals("N")) || (cb.getChargecode().trim().equals(messageResources.getMessage("spclequipmentsurcharge"))
                            && quotesForm.getSpecialequipment().equals("N")) || (cb.getChargeCodedesc() != null && cb.getChargeCodedesc().trim().equals(messageResources.getMessage("soc")) && quotesForm.getSoc().equals("N")))) {
                    } else {
                        if (quotesForm.getSsline().equals(ssName)) {
                            if (!ratesList.contains(cb)) {
                                if (cb.getUnitType() != null) {
                                    cb.setUnitType(String.valueOf(unit));
                                }

                                String unitTypes[] = messageResources.getMessage("unittype").split(",");
                                if (cb.getUnitType() != null && ((cb.getUnitType().equals(unitTypes[0]) && !cb.getFutureRateA().trim().equals("0.00") && cb.getSetA().trim().equals("0.00")) || (cb.getUnitType().equals(unitTypes[1]) && (!cb.getFutureRateD().trim().equals("0.00")) && cb.getSetD().trim().equals("0.00")) || (cb.getUnitType().equals(unitTypes[2]) && (!cb.getFutureRateB().trim().equals("0.00")) && cb.getSetB().trim().equals("0.00")) || (cb.getUnitType().equals(unitTypes[3]) && (!cb.getFutureRateE().trim().equals("0.00")) && cb.getSetE().trim().equals("0.00")) || (cb.getUnitType().equals(unitTypes[4]) && (!cb.getFutureRateC().trim().equals("0.00")) && cb.getSetC().trim().equals("0.00")) || (cb.getUnitType().equals(unitTypes[5]) && (!cb.getFutureRateF().trim().equals("0.00")) && cb.getSetF().trim().equals("0.00")) || (cb.getUnitType().equals(unitTypes[6]) && (!cb.getFutureRateG().trim().equals("0.00")) && cb.getSetG().trim().equals("0.00")) || (cb.getUnitType().equals(unitTypes[7]) && (!cb.getFutureRateH().trim().equals("0.00")) && cb.getSetH().equals("0.00")) || (cb.getUnitType().equals(unitTypes[8]) && (!cb.getFutureRateI().trim().equals("0.00")) && cb.getSetI().equals("0.00")) || (cb.getUnitType().equals(unitTypes[9]) && (!cb.getFutureRateJ().trim().equals("0.00")) && cb.getSetJ().equals("0.00")) || (cb.getUnitType().equals(unitTypes[10]) && (!cb.getFutureRateK().trim().equals("0.00")) && cb.getSetK().equals("0.00")) || (cb.getUnitType().equals(unitTypes[11]) && (!cb.getFutureRateL().trim().equals("0.00")) && cb.getSetL().equals("0.00")) || (cb.getUnitType().equals(unitTypes[12]) && (!cb.getFutureRateM().trim().equals("0.00")) && cb.getSetM().equals("0.00")) || (cb.getUnitType().equals(unitTypes[13]) && (!cb.getFutureRateN().trim().equals("0.00")) && cb.getSetN().equals("0.00")) || (cb.getUnitType().equals(unitTypes[14]) && (!cb.getFutureRateO().trim().equals("0.00")) && cb.getSetO().equals("0.00")) || (cb.getUnitType().equals(unitTypes[15]) && (!cb.getFutureRateP().trim().equals("0.00")) && cb.getSetP().equals("0.00")) || (cb.getUnitType().equals(unitTypes[16]) && (!cb.getFutureRateQ().trim().equals("0.00")) && cb.getSetQ().equals("0.00")))) {

                                    ratesList.add(cb);
                                }
                                if (cb.getUnitType() != null && ((cb.getUnitType().equals(unitTypes[0]) && cb.getSetA().trim().equals("0.00")) || (cb.getUnitType().equals(unitTypes[1]) && (cb.getSetD().trim().equals("0.00"))) || (cb.getUnitType().equals(unitTypes[2]) && (cb.getSetB().trim().equals("0.00"))) || (cb.getUnitType().equals(unitTypes[3]) && (cb.getSetE().trim().equals("0.00"))) || (cb.getUnitType().equals(unitTypes[4]) && (cb.getSetC().trim().equals("0.00"))) || (cb.getUnitType().equals(unitTypes[5]) && (cb.getSetF().trim().equals("0.00"))) || (cb.getUnitType().equals(unitTypes[6]) && (cb.getSetG().trim().equals("0.00"))) || (cb.getUnitType().equals(unitTypes[7]) && (cb.getSetH().trim().equals("0.00"))) || (cb.getUnitType().equals(unitTypes[8]) && (cb.getSetI().trim().equals("0.00"))) || (cb.getUnitType().equals(unitTypes[9]) && (cb.getSetJ().trim().equals("0.00"))) || (cb.getUnitType().equals(unitTypes[11]) && (cb.getSetK().trim().equals("0.00"))) || (cb.getUnitType().equals(unitTypes[12]) && (cb.getSetL().trim().equals("0.00"))) || (cb.getUnitType().equals(unitTypes[13]) && (cb.getSetM().trim().equals("0.00"))) || (cb.getUnitType().equals(unitTypes[14]) && (cb.getSetN().trim().equals("0.00"))) || (cb.getUnitType().equals(unitTypes[15]) && (cb.getSetO().trim().equals("0.00"))) || (cb.getUnitType().equals(unitTypes[16]) && (cb.getSetP().trim().equals("0.00"))) || (cb.getUnitType().equals("11499") && (cb.getSetQ().trim().equals("0.00"))))) {
                                } else {

                                    cb.setInclude("on");
                                    cb.setPrint("on");
                                    if (cb.getCurrency().trim().equals(currencies[7])) {
                                        totalCharges = dbUtil.getcurrency(cb, messageResources);
                                    } else if (cb.getCurrency().trim().equals(currencies[8])) {
                                        baht = dbUtil.getcurrency(cb, messageResources);
                                    } else if (cb.getCurrency().trim().equals(currencies[0])) {
                                        myr = dbUtil.getcurrency(cb, messageResources);
                                    } else if (cb.getCurrency().trim().equals(currencies[1])) {
                                        nht = dbUtil.getcurrency(cb, messageResources);
                                    } else if (cb.getCurrency().trim().equals(currencies[2])) {
                                        pkr = dbUtil.getcurrency(cb, messageResources);
                                    } else if (cb.getCurrency().trim().equals(currencies[3])) {
                                        rm = dbUtil.getcurrency(cb, messageResources);
                                    } else if (cb.getCurrency().trim().equals(currencies[4])) {
                                        spo = dbUtil.getcurrency(cb, messageResources);
                                    } else if (cb.getCurrency().trim().equals(currencies[5])) {
                                        vnd = dbUtil.getcurrency(cb, messageResources);
                                    } else if (cb.getCurrency().trim().equals(currencies[6])) {
                                        inr = dbUtil.getcurrency(cb, messageResources);
                                    } else if (cb.getCurrency().trim().equals(currencies[9])) {
                                        BDT = dbUtil.getcurrency(cb, messageResources);
                                    } else if (cb.getCurrency().trim().equals(currencies[10])) {
                                        cyp = dbUtil.getcurrency(cb, messageResources);
                                    } else if (cb.getCurrency().trim().equals(currencies[11])) {
                                        eur = dbUtil.getcurrency(cb, messageResources);
                                    } else if (cb.getCurrency().trim().equals(currencies[12])) {
                                        hkd = dbUtil.getcurrency(cb, messageResources);
                                    } else if (cb.getCurrency().trim().equals(currencies[13])) {
                                        lkr = dbUtil.getcurrency(cb, messageResources);
                                    } else if (cb.getCurrency().trim().equals(currencies[14])) {
                                        nt = dbUtil.getcurrency(cb, messageResources);
                                    } else if (cb.getCurrency().trim().equals(currencies[15])) {
                                        prs = dbUtil.getcurrency(cb, messageResources);
                                    } else if (cb.getCurrency().trim().equals(currencies[16])) {
                                        rmb = dbUtil.getcurrency(cb, messageResources);
                                    } else if (cb.getCurrency().trim().equals(currencies[17])) {
                                        won = dbUtil.getcurrency(cb, messageResources);
                                    } else if (cb.getCurrency().trim().equals(currencies[18])) {
                                        yen = dbUtil.getcurrency(cb, messageResources);
                                    }

                                    if (cb.getUnitType() != null && quotesForm.getSpecialEqpmt() != null && (((cb.getUnitType().equals(unitTypes[7]) || cb.getUnitType().equals(unitTypes[9])) && quotesForm.getSpecialEqpmt().trim().equals("OT"))
                                            || ((cb.getUnitType().equals(unitTypes[8]) || cb.getUnitType().equals(unitTypes[10])) && quotesForm.getSpecialEqpmt().trim().equals("FR"))
                                            || ((cb.getUnitType().equals(unitTypes[11]) || cb.getUnitType().equals(unitTypes[12])) && quotesForm.getSpecialEqpmt().trim().equals("HARD TOP")) || ((cb.getUnitType().equals(unitTypes[13]) || cb.getUnitType().equals(unitTypes[14])) && quotesForm.getSpecialEqpmt().trim().equals("REEFER")) || (cb.getUnitType().equals(unitTypes[15]) && quotesForm.getSpecialEqpmt().trim().equals("REEFER HIGH CUBE")) || (cb.getUnitType().equals(unitTypes[16]) && quotesForm.getSpecialEqpmt().trim().equals("HC")))) {

                                        CostBean c7 = new CostBean();
                                        c7.setUnitType(cb.getUnitType());
                                        c7.setNumber(cb.getNumber());
                                        c7.setChargecode(cb.getChargecode());
                                        c7.setChargeCodedesc(cb.getChargeCodedesc());
                                        c7.setCostType(cb.getCostType());
                                        c7.setCurrency(cb.getCurrency());
                                        c7.setSetA(cb.getSetA());
                                        c7.setSetB(cb.getSetB());
                                        c7.setSetC(cb.getSetC());
                                        c7.setSetD(cb.getSetD());
                                        c7.setSetE(cb.getSetE());
                                        c7.setSetF(cb.getSetF());
                                        c7.setSetG(cb.getSetG());
                                        c7.setSetH(cb.getSetH());
                                        c7.setSetI(cb.getSetI());
                                        c7.setSetJ(cb.getSetJ());
                                        c7.setSetK(cb.getSetK());
                                        c7.setSetL(cb.getSetL());
                                        c7.setSetM(cb.getSetM());
                                        c7.setSetN(cb.getSetN());
                                        c7.setSetO(cb.getSetO());
                                        c7.setSetP(cb.getSetP());
                                        c7.setSetQ(cb.getSetQ());
                                        c7.setMarkUpA(cb.getMarkUpA());
                                        c7.setMarkUpB(cb.getMarkUpB());
                                        c7.setMarkUpC(cb.getMarkUpC());
                                        c7.setMarkUpD(cb.getMarkUpD());
                                        c7.setMarkUpE(cb.getMarkUpE());
                                        c7.setMarkUpF(cb.getMarkUpF());
                                        c7.setMarkUpG(cb.getMarkUpG());
                                        c7.setMarkUpH(cb.getMarkUpH());
                                        c7.setMarkUpI(cb.getMarkUpI());
                                        c7.setMarkUpJ(cb.getMarkUpJ());
                                        c7.setMarkUpK(cb.getMarkUpK());
                                        c7.setMarkUpL(cb.getMarkUpL());
                                        c7.setMarkUpM(cb.getMarkUpM());
                                        c7.setMarkUpN(cb.getMarkUpN());
                                        c7.setMarkUpO(cb.getMarkUpO());
                                        c7.setMarkUpP(cb.getMarkUpP());
                                        c7.setMarkUpQ(cb.getMarkUpQ());
                                        c7.setInclude(cb.getInclude());
                                        c7.setPrint(cb.getPrint());
                                        specialEquipmentList.add(c7);
                                    } else {
                                        ratesList.add(cb);
                                    }
                                }
                            }
                        }
                    }
                }
                cb = null;
                i++;
            }
            k++;
        }
        String unitTypes[] = messageResources.getMessage("unittype").split(",");
        for (int i = 0; i < ratesList.size(); i++) {
            CostBean c4 = (CostBean) ratesList.get(i);
            if (c4.getChargecode().trim().equals(messageResources.getMessage("oceanfreight"))) {
                if (c4.getUnitType() != null && c4.getUnitType().equals(unitTypes[0])) {
                    for (int a = 0; a < specialEquipmentList.size(); a++) {
                        CostBean c5 = (CostBean) specialEquipmentList.get(a);
                        if (c5.getUnitType() != null && c5.getUnitType().equals(unitTypes[7])) {
                            String setA = "";
                            String setJ = "";
                            setJ = c5.getSetH();
                            setA = c4.getSetA();
                            Double aSetA = Double.parseDouble(dbUtil.removeComma(setA));
                            aSetA = aSetA + Double.parseDouble(dbUtil.removeComma(setJ));

                            c4.setSetA(numb.format(aSetA));
                            String markUpB = "";
                            String markUpJ = "";
                            markUpB = c4.getMarkUpA();
                            markUpJ = c5.getMarkUpH();
                            Double aMarkupA = Double.parseDouble(dbUtil.removeComma(markUpB));
                            aMarkupA = aMarkupA + Double.parseDouble(dbUtil.removeComma(markUpJ));
                            c4.setMarkUpA(numb.format(aMarkupA));
                        }
                        if (c5.getUnitType() != null && c5.getUnitType().equals(unitTypes[8])) {
                            String setA = "";
                            String setJ = "";
                            setJ = c5.getSetI();
                            setA = c4.getSetA();
                            Double aSetA = Double.parseDouble(dbUtil.removeComma(setA));
                            aSetA = aSetA + Double.parseDouble(dbUtil.removeComma(setJ));
                            c4.setSetA(numb.format(aSetA));
                            String markUpJ = "";
                            markUpJ = c5.getMarkUpI();
                            Double aMarkupA = Double.parseDouble(dbUtil.removeComma(c4.getMarkUpA()));
                            aMarkupA = aMarkupA + Double.parseDouble(dbUtil.removeComma(markUpJ));
                            c4.setMarkUpA(numb.format(aMarkupA));
                        }
                        if (c5.getUnitType() != null && c5.getUnitType().equals(unitTypes[11])) {
                            String setA = "";
                            String setJ = "";
                            setJ = c5.getSetL();
                            setA = c4.getSetA();
                            Double aSetA = Double.parseDouble(dbUtil.removeComma(setA));
                            aSetA = aSetA + Double.parseDouble(dbUtil.removeComma(setJ));
                            c4.setSetA(numb.format(aSetA));
                            String markUpB = "";
                            String markUpJ = "";
                            markUpB = c4.getMarkUpA();
                            markUpJ = c5.getMarkUpL();
                            Double aMarkupA = Double.parseDouble(dbUtil.removeComma(markUpB));
                            aMarkupA = aMarkupA + Double.parseDouble(dbUtil.removeComma(markUpJ));
                            c4.setMarkUpA(numb.format(aMarkupA));
                        }
                        if (c5.getUnitType() != null && c5.getUnitType().equals(unitTypes[13])) {
                            String setA = "";
                            String setJ = "";
                            setJ = c5.getSetO();
                            setA = c4.getSetA();
                            Double aSetA = Double.parseDouble(dbUtil.removeComma(setA));
                            aSetA = aSetA + Double.parseDouble(dbUtil.removeComma(setJ));
                            c4.setSetA(numb.format(aSetA));
                            String markUpB = "";
                            String markUpJ = "";
                            markUpB = c4.getMarkUpA();
                            markUpJ = c5.getMarkUpO();
                            Double aMarkupA = Double.parseDouble(dbUtil.removeComma(markUpB));
                            aMarkupA = aMarkupA + Double.parseDouble(dbUtil.removeComma(markUpJ));
                            c4.setMarkUpA(numb.format(aMarkupA));
                        }
                    }
                }
                if (c4.getUnitType() != null && c4.getUnitType().equals(unitTypes[2])) {
                    for (int a = 0; a < specialEquipmentList.size(); a++) {
                        CostBean c5 = (CostBean) specialEquipmentList.get(a);
                        if (c5.getUnitType() != null && c5.getUnitType().equals(unitTypes[9])) {
                            String setA = "";
                            String setJ = "";
                            setJ = c5.getSetJ();
                            setA = c4.getSetB();
                            Double aSetA = Double.parseDouble(dbUtil.removeComma(setA));
                            aSetA = aSetA + Double.parseDouble(dbUtil.removeComma(setJ));
                            c4.setSetB(numb.format(aSetA));
                            String markUpB = "";
                            String markUpJ = "";
                            markUpB = c4.getMarkUpB();
                            markUpJ = c5.getMarkUpJ();
                            Double aMarkupA = Double.parseDouble(dbUtil.removeComma(markUpB));
                            aMarkupA = aMarkupA + Double.parseDouble(dbUtil.removeComma(markUpJ));
                            c4.setMarkUpB(numb.format(aMarkupA));
                        }
                        if (c5.getUnitType() != null && c5.getUnitType().equals(unitTypes[10])) {
                            String setA = "";
                            String setJ = "";
                            setJ = c5.getSetK();
                            setA = c4.getSetB();
                            Double aSetA = Double.parseDouble(dbUtil.removeComma(setA));
                            aSetA = aSetA + Double.parseDouble(dbUtil.removeComma(setJ));
                            c4.setSetB(numb.format(aSetA));
                            String markUpB = "";
                            String markUpJ = "";
                            markUpB = c4.getMarkUpB();
                            markUpJ = c5.getMarkUpK();
                            Double aMarkupA = Double.parseDouble(dbUtil.removeComma(markUpB));
                            aMarkupA = aMarkupA + Double.parseDouble(dbUtil.removeComma(markUpJ));
                            c4.setMarkUpB(numb.format(aMarkupA));
                        }
                        if (c5.getUnitType() != null && c5.getUnitType().equals(unitTypes[11])) {
                            String setA = "";
                            String setJ = "";
                            setJ = c5.getSetM();
                            setA = c4.getSetB();
                            Double aSetA = Double.parseDouble(dbUtil.removeComma(setA));
                            aSetA = aSetA + Double.parseDouble(dbUtil.removeComma(setJ));
                            c4.setSetB(numb.format(aSetA));
                            String markUpB = "";
                            String markUpJ = "";
                            markUpB = c4.getMarkUpB();
                            markUpJ = c5.getMarkUpM();
                            Double aMarkupA = Double.parseDouble(dbUtil.removeComma(markUpB));
                            aMarkupA = aMarkupA + Double.parseDouble(dbUtil.removeComma(markUpJ));
                            c4.setMarkUpB(numb.format(aMarkupA));
                        }
                        if (c5.getUnitType() != null && c5.getUnitType().equals(unitTypes[14])) {
                            String setA = "";
                            String setJ = "";
                            setJ = c5.getSetN();
                            setA = c4.getSetB();
                            Double aSetA = Double.parseDouble(dbUtil.removeComma(setA));
                            aSetA = aSetA + Double.parseDouble(dbUtil.removeComma(setJ));
                            c4.setSetB(numb.format(aSetA));
                            String markUpB = "";
                            String markUpJ = "";
                            markUpB = c4.getMarkUpB();
                            markUpJ = c5.getMarkUpN();
                            Double aMarkupA = Double.parseDouble(dbUtil.removeComma(markUpB));
                            aMarkupA = aMarkupA + Double.parseDouble(dbUtil.removeComma(markUpJ));
                            c4.setMarkUpB(numb.format(aMarkupA));
                        }
                        if (c5.getUnitType() != null && c5.getUnitType().equals(unitTypes[15])) {
                            String setA = "";
                            String setJ = "";
                            setJ = c5.getSetP();
                            setA = c4.getSetB();
                            Double aSetA = Double.parseDouble(dbUtil.removeComma(setA));
                            aSetA = aSetA + Double.parseDouble(dbUtil.removeComma(setJ));
                            c4.setSetB(numb.format(aSetA));
                            String markUpB = "";
                            String markUpJ = "";
                            markUpB = c4.getMarkUpB();
                            markUpJ = c5.getMarkUpP();
                            Double aMarkupA = Double.parseDouble(dbUtil.removeComma(markUpB));
                            aMarkupA = aMarkupA + Double.parseDouble(dbUtil.removeComma(markUpJ));
                            c4.setMarkUpB(numb.format(aMarkupA));
                        }
                    }
                }
                if (c4.getUnitType() != null && c4.getUnitType().equals(unitTypes[1])) {
                    for (int a = 0; a < specialEquipmentList.size(); a++) {
                        CostBean c5 = (CostBean) specialEquipmentList.get(a);
                        if (c5.getUnitType() != null && c5.getUnitType().equals(unitTypes[16])) {
                            String setA = "";
                            String setJ = "";
                            setJ = c5.getSetQ();
                            setA = c4.getSetD();
                            Double aSetA = Double.parseDouble(dbUtil.removeComma(setA));
                            aSetA = aSetA + Double.parseDouble(dbUtil.removeComma(setJ));
                            c4.setSetD(numb.format(aSetA));
                            String markUpB = "";
                            String markUpJ = "";
                            markUpB = c4.getMarkUpD();
                            markUpJ = c5.getMarkUpQ();
                            Double aMarkupA = Double.parseDouble(dbUtil.removeComma(markUpB));
                            aMarkupA = aMarkupA + Double.parseDouble(dbUtil.removeComma(markUpJ));
                            c4.setMarkUpD(numb.format(aMarkupA));
                        }
                    }
                }
            }
        }

        for (int a = 0; a < ratesList.size(); a++) {
            CostBean c3 = (CostBean) ratesList.get(a);
            CostBean cbeanPrev = null;
            if (a != 0) {
                cbeanPrev = (CostBean) ratesList.get(a - 1);
                if (cbeanPrev.getUnitType() != null && c3.getUnitType() != null && !cbeanPrev.getUnitType().equals(c3.getUnitType())) {
                    for (int s = 0; s < ratesList1.size(); s++) {
                        CostBean c1 = new CostBean();
                        c1 = (CostBean) ratesList1.get(s);
                        CostBean c2 = new CostBean();
                        if (c1.getUnitType().equals("0.00")) {
                            PropertyUtils.copyProperties(c2, c1);
                            c2.setNumber("1");
                            c2.setInclude("on");
                            c2.setPrint("on");
                            String uniType[] = messageResources.getMessage("unittype").split(",");
                            c2.setUnitType(cbeanPrev.getUnitType());
                            if (c2.getUnitType().equals(uniType[0])) {
                                c2.setSetA(c2.getRetail());
                            } else if (c2.getUnitType().equals(uniType[1])) {
                                c2.setSetD(c2.getRetail());
                            } else if (c2.getUnitType().equals(uniType[2])) {
                                c2.setSetB(c2.getRetail());
                            } else if (c2.getUnitType().equals(uniType[3])) {
                                c2.setSetE(c2.getRetail());
                            } else if (c2.getUnitType().equals(uniType[4])) {
                                c2.setSetC(c2.getRetail());
                            } else if (c2.getUnitType().equals(uniType[5])) {
                                c2.setSetF(c2.getRetail());
                            } else if (c2.getUnitType().equals(uniType[6])) {
                                c2.setSetG(c2.getRetail());
                            } else if (c2.getUnitType().equals(uniType[7])) {
                                c2.setSetH(c2.getRetail());
                            } else if (c2.getUnitType().equals(uniType[8])) {
                                c2.setSetI(c2.getRetail());
                            } else if (c2.getUnitType().equals(uniType[9])) {
                                c2.setSetJ(c2.getRetail());
                            } else if (c2.getUnitType().equals(uniType[10])) {
                                c2.setSetK(c2.getRetail());
                            } else if (c2.getUnitType().equals(uniType[11])) {
                                c2.setSetL(c2.getRetail());
                            } else if (c2.getUnitType().equals(uniType[12])) {
                                c2.setSetM(c2.getRetail());
                            } else if (c2.getUnitType().equals(uniType[13])) {
                                c2.setSetN(c2.getRetail());
                            } else if (c2.getUnitType().equals(uniType[14])) {
                                c2.setSetO(c2.getRetail());
                            } else if (c2.getUnitType().equals(uniType[15])) {
                                c2.setSetP(c2.getRetail());
                            } else if (c2.getUnitType().equals(uniType[16])) {
                                c2.setSetQ(c2.getRetail());
                            }
                            chargesList.add(c2);
                        }
                    }
                }
            }
            chargesList.add(c3);
        }
        if (chargesList.size() > 0) {
            CostBean c4 = (CostBean) chargesList.get(chargesList.size() - 1);
            for (int i = 0; i < ratesList1.size(); i++) {
                CostBean c1 = (CostBean) ratesList1.get(i);
                CostBean c2 = new CostBean();
                if (c1.getUnitType().equals("0.00")) {
                    PropertyUtils.copyProperties(c2, c1);
                    c2.setNumber("1");
                    c2.setInclude("on");
                    c2.setPrint("on");
                    String uniType[] = messageResources.getMessage("unittype").split(",");
                    c2.setUnitType(c4.getUnitType());
                    if (c2.getUnitType().equals(uniType[0])) {
                        c2.setSetA(c2.getRetail());
                    } else if (c2.getUnitType().equals(uniType[1])) {
                        c2.setSetD(c2.getRetail());
                    } else if (c2.getUnitType().equals(uniType[2])) {
                        c2.setSetB(c2.getRetail());
                    } else if (c2.getUnitType().equals(uniType[3])) {
                        c2.setSetE(c2.getRetail());
                    } else if (c2.getUnitType().equals(uniType[4])) {
                        c2.setSetC(c2.getRetail());
                    } else if (c2.getUnitType().equals(uniType[5])) {
                        c2.setSetF(c2.getRetail());
                    } else if (c2.getUnitType().equals(uniType[6])) {
                        c2.setSetG(c2.getRetail());
                    } else if (c2.getUnitType().equals(uniType[7])) {
                        c2.setSetH(c2.getRetail());
                    } else if (c2.getUnitType().equals(uniType[8])) {
                        c2.setSetI(c2.getRetail());
                    } else if (c2.getUnitType().equals(uniType[9])) {
                        c2.setSetJ(c2.getRetail());
                    } else if (c2.getUnitType().equals(uniType[10])) {
                        c2.setSetK(c2.getRetail());
                    } else if (c2.getUnitType().equals(uniType[11])) {
                        c2.setSetL(c2.getRetail());
                    } else if (c2.getUnitType().equals(uniType[12])) {
                        c2.setSetM(c2.getRetail());
                    } else if (c2.getUnitType().equals(uniType[13])) {
                        c2.setSetN(c2.getRetail());
                    } else if (c2.getUnitType().equals(uniType[14])) {
                        c2.setSetO(c2.getRetail());
                    } else if (c2.getUnitType().equals(uniType[15])) {
                        c2.setSetP(c2.getRetail());
                    } else if (c2.getUnitType().equals(uniType[16])) {
                        c2.setSetQ(c2.getRetail());
                    }
                    chargesList.add(c2);
                }
            }
        }

        Quotation quotation = null;
        if (quote != null && quote.equals("editQuotes")) {
            quotation = quotationObject;
        } else {
            quotation = save(quotesForm);
        }
        quotation.setTotalCharges(totalCharges);
        quotation.setBaht(baht);
        quotation.setBdt(BDT);
        quotation.setCyp(cyp);
        quotation.setEur(eur);
        quotation.setHkd(hkd);
        quotation.setLkr(lkr);
        quotation.setNt(nt);
        quotation.setPrs(prs);
        quotation.setRmb(rmb);
        quotation.setWon(won);
        quotation.setYen(yen);
        quotation.setInr(inr);
        quotation.setMyr(myr);
        quotation.setNht(nht);
        quotation.setPkr(pkr);
        quotation.setRm(rm);
        quotation.setSpo(spo);
        quotation.setVnd(vnd);
        quotationDAO.save(quotation);

        //getCharges(chargesList, messageResources, quotation);
        getOtherChargesforKG(perkglbsList, quotation);
        getOtherCharges(otherChargesList, quotation);
        return quotation;
    }

    //saving the fcl container size charges into charges table
    public void getCharges(List chargesList, MessageResources messageResources, Quotation quotation, HttpServletRequest request) throws Exception {
        List list1 = chargesDAO.getChargesforSpecialEquipment(new Integer(quotation.getQuoteId()));
        HashMap deletedChargesMap = new HashMap();
        for (int j = 0; j < list1.size(); j++) {
            Charges c1 = (Charges) list1.get(j);
            deletedChargesMap.put(c1.getChgCode() + "-" + c1.getUnitType(), c1.getComment());
            chargesDAO.delete(c1);
        }
        String temp = "";
        int j = 0;
        int k = 0;
        LinkedList linkedList = new LinkedList();
        List newList = new ArrayList();
        for (Iterator iterator = chargesList.iterator(); iterator.hasNext();) {
            Charges c = (Charges) iterator.next();
            if (null != c.getUnitType()) {
                if (!temp.equals(c.getUnitType())) {
                    k = j;
                }
            }
            if (c.getChargeCodeDesc().equals("OCNFRT") || c.getChargeCodeDesc().equals("OFIMP")) {
                linkedList.add(k, c);
            } else {
                linkedList.add(c);
            }
            if (null != c.getUnitType()) {
                temp = c.getUnitType();
            }
            j++;
        }
        newList.addAll(linkedList);
        String outOfGage = "";
        for (int i = 0; i < newList.size(); i++) {
            Charges c1 = (Charges) newList.get(i);
            c1.setQouteId(quotation.getQuoteId());
            if ("R".equals(quotation.getRatesNonRates()) && "OCNFRT".equalsIgnoreCase(c1.getChargeCodeDesc()) && CommonUtils.isNotEmpty(c1.getUnitType())) {
                if (null == c1.getSpecialEquipmentUnit()) {
                    c1.setSpecialEquipmentUnit("");
                }
                if (null == c1.getStandardCharge()) {
                    c1.setStandardCharge("");
                }
                GenericCode genericCode = genericCodeDAO.findById(Integer.parseInt(c1.getUnitType()));
                if (null != genericCode) {
                    outOfGage = request.getParameter("outOfGageOCNFRT" + genericCode.getCodedesc() + "-" + c1.getSpecialEquipmentUnit() + "-" + c1.getStandardCharge());
                    if (CommonUtils.isEmpty(outOfGage)) {
                        outOfGage = request.getParameter("outOfGage1OCNFRT" + genericCode.getCodedesc() + "-" + c1.getSpecialEquipmentUnit() + "-" + c1.getStandardCharge());
                    }
                }
                if (CommonUtils.isNotEmpty(outOfGage)) {
                    c1.setOutOfGauge(outOfGage);
                } else {
                    c1.setOutOfGauge("N");
                }
            } else {
                if (null == c1.getSpecialEquipmentUnit()) {
                    c1.setSpecialEquipmentUnit("");
                }
                if (null == c1.getStandardCharge()) {
                    c1.setStandardCharge("");
                }
                if (null != c1.getUnitType()) {
                    GenericCode genericCode = genericCodeDAO.findById(Integer.parseInt(c1.getUnitType()));
                }
                if (null != genericCode) {
                    outOfGage = request.getParameter("outOfGage" + c1.getChargeCodeDesc() + genericCode.getCodedesc() + "-" + c1.getSpecialEquipmentUnit() + "-" + c1.getStandardCharge());
                    if (CommonUtils.isEmpty(outOfGage)) {
                        outOfGage = request.getParameter("outOfGage1" + c1.getChargeCodeDesc() + genericCode.getCodedesc() + "-" + c1.getSpecialEquipmentUnit() + "-" + c1.getStandardCharge());
                    }
                }
                if (null != c1.getUnitType()) {
                    if (CommonUtils.isNotEmpty(outOfGage)) {
                        List l = chargesDAO.getChargeByEquipmentUnit(quotation.getQuoteId(), c1.getUnitType(), c1.getStandardCharge());
                        if (!l.isEmpty()) {
                            for (Iterator it = l.iterator(); it.hasNext();) {
                                Charges c = (Charges) it.next();
                                c.setOutOfGauge(c1.getOutOfGauge());
                            }
                        }
                        c1.setOutOfGauge(outOfGage);
                    }
                } else {
                    c1.setOutOfGauge("N");
                }
            }

            //---METHOD FOR RESTORING THE COMMENTS FOR ALL THE CHARGES----
            // restoreTheCommentsOfCharges(deletedChargesMap,c1);
            chargesDAO.save(c1);
        }
    }

    /**
     * @param deletedChargesMap
     * @param newCharge
     * @see here we get the comments from deleted charges and reset to new
     * charge obj to restore them.
     */
    public void restoreTheCommentsOfCharges(HashMap deletedChargesMap, Charges newCharge) throws Exception {
        if (null != deletedChargesMap.get(newCharge.getChgCode() + "-" + newCharge.getUnitType())) {
            String deletedObj = (String) deletedChargesMap.get(newCharge.getChgCode() + "-" + newCharge.getUnitType());
            newCharge.setComment(null != deletedObj ? deletedObj : "");
        }
    }
    //get amount depends on the unit types

    public Double getAmount(CostBean c1, MessageResources messageResources) throws Exception {
        String unitTypes[] = messageResources.getMessage("unittype").split(",");
        Double amount = 0.00;
        if (c1.getUnitType() != null && c1.getSetA() != null && c1.getUnitType().equals(unitTypes[0])) {
            amount = Double.parseDouble(dbUtil.removeComma(c1.getSetA()));
        } else if (c1.getUnitType() != null && c1.getSetD() != null && c1.getUnitType().equals(unitTypes[1])) {
            amount = Double.parseDouble(dbUtil.removeComma(c1.getSetD()));
        } else if (c1.getUnitType() != null && c1.getUnitType().equals(unitTypes[2])) {
            amount = Double.parseDouble(dbUtil.removeComma(c1.getSetB()));
        } else if (c1.getUnitType() != null && c1.getUnitType().equals(unitTypes[3])) {
            amount = Double.parseDouble(dbUtil.removeComma(c1.getSetE()));
        } else if (c1.getUnitType() != null && c1.getUnitType().equals(unitTypes[4])) {
            amount = Double.parseDouble(dbUtil.removeComma(c1.getSetC()));
        } else if (c1.getUnitType() != null && c1.getUnitType().equals(unitTypes[5])) {
            amount = Double.parseDouble(dbUtil.removeComma(c1.getSetF()));
        } else if (c1.getUnitType() != null && c1.getUnitType().equals(unitTypes[6])) {
            amount = Double.parseDouble(dbUtil.removeComma(c1.getSetG()));
        } else if (c1.getUnitType() != null && c1.getUnitType().equals(unitTypes[7])) {
            amount = Double.parseDouble(dbUtil.removeComma(c1.getSetH()));
        } else if (c1.getUnitType() != null && c1.getUnitType().equals(unitTypes[8])) {
            amount = Double.parseDouble(dbUtil.removeComma(c1.getSetI()));
        } else if (c1.getUnitType() != null && c1.getUnitType().equals(unitTypes[9])) {
            amount = Double.parseDouble(dbUtil.removeComma(c1.getSetJ()));
        } else if (c1.getUnitType() != null && c1.getUnitType().equals(unitTypes[10])) {
            amount = Double.parseDouble(dbUtil.removeComma(c1.getSetK()));
        } else if (c1.getUnitType() != null && c1.getUnitType().equals(unitTypes[11])) {
            amount = Double.parseDouble(dbUtil.removeComma(c1.getSetL()));
        } else if (c1.getUnitType() != null && c1.getUnitType().equals(unitTypes[12])) {
            amount = Double.parseDouble(dbUtil.removeComma(c1.getSetM()));
        } else if (c1.getUnitType() != null && c1.getUnitType().equals(unitTypes[13])) {
            amount = Double.parseDouble(dbUtil.removeComma(c1.getSetN()));
        } else if (c1.getUnitType() != null && c1.getUnitType().equals(unitTypes[14])) {
            amount = Double.parseDouble(dbUtil.removeComma(c1.getSetO()));
        } else if (c1.getUnitType() != null && c1.getUnitType().equals(unitTypes[15])) {
            amount = Double.parseDouble(dbUtil.removeComma(c1.getSetP()));
        } else if (c1.getUnitType() != null && c1.getUnitType().equals(unitTypes[16])) {
            amount = Double.parseDouble(dbUtil.removeComma(c1.getSetQ()));
        }
        return amount;
    }

//	get markup amount depends on the unit types
    public Double getMarkUp(CostBean c1, MessageResources messageResources) throws Exception {
        String unitTypes[] = messageResources.getMessage("unittype").split(",");
        Double markUp = 0.00;
        if (c1.getUnitType() != null && c1.getUnitType().equals(unitTypes[0])) {
            markUp = Double.parseDouble(dbUtil.removeComma(c1.getMarkUpA()));
        } else if (c1.getUnitType() != null && c1.getUnitType().equals(unitTypes[1])) {
            markUp = Double.parseDouble(dbUtil.removeComma(c1.getMarkUpD()));
        } else if (c1.getUnitType() != null && c1.getUnitType().equals(unitTypes[2])) {
            markUp = Double.parseDouble(dbUtil.removeComma(c1.getMarkUpB()));
        } else if (c1.getUnitType() != null && c1.getUnitType().equals(unitTypes[3])) {
            markUp = Double.parseDouble(dbUtil.removeComma(c1.getMarkUpE()));
        } else if (c1.getUnitType() != null && c1.getUnitType().equals(unitTypes[4])) {
            markUp = Double.parseDouble(dbUtil.removeComma(c1.getMarkUpC()));
        } else if (c1.getUnitType() != null && c1.getUnitType().equals(unitTypes[5])) {
            markUp = Double.parseDouble(dbUtil.removeComma(c1.getMarkUpF()));
        } else if (c1.getUnitType() != null && c1.getUnitType().equals(unitTypes[6])) {
            markUp = Double.parseDouble(dbUtil.removeComma(c1.getMarkUpG()));
        } else if (c1.getUnitType() != null && c1.getUnitType().equals(unitTypes[7])) {
            markUp = Double.parseDouble(dbUtil.removeComma(c1.getMarkUpH()));
        } else if (c1.getUnitType() != null && c1.getUnitType().equals(unitTypes[8])) {
            markUp = Double.parseDouble(dbUtil.removeComma(c1.getMarkUpI()));
        } else if (c1.getUnitType() != null && c1.getUnitType().equals(unitTypes[9])) {
            markUp = Double.parseDouble(dbUtil.removeComma(c1.getMarkUpJ()));
        } else if (c1.getUnitType() != null && c1.getUnitType().equals(unitTypes[10])) {
            markUp = Double.parseDouble(dbUtil.removeComma(c1.getMarkUpK()));
        } else if (c1.getUnitType() != null && c1.getUnitType().equals(unitTypes[11])) {
            markUp = Double.parseDouble(dbUtil.removeComma(c1.getMarkUpL()));
        } else if (c1.getUnitType() != null && c1.getUnitType().equals(unitTypes[12])) {
            markUp = Double.parseDouble(dbUtil.removeComma(c1.getMarkUpM()));
        } else if (c1.getUnitType() != null && c1.getUnitType().equals(unitTypes[13])) {
            markUp = Double.parseDouble(dbUtil.removeComma(c1.getMarkUpN()));
        } else if (c1.getUnitType() != null && c1.getUnitType().equals(unitTypes[14])) {
            markUp = Double.parseDouble(dbUtil.removeComma(c1.getMarkUpO()));
        } else if (c1.getUnitType() != null && c1.getUnitType().equals(unitTypes[15])) {
            markUp = Double.parseDouble(dbUtil.removeComma(c1.getMarkUpP()));
        } else if (c1.getUnitType() != null && c1.getUnitType().equals(unitTypes[16])) {
            markUp = Double.parseDouble(dbUtil.removeComma(c1.getMarkUpQ()));
        }
        return markUp;
    }

//	get future rate amount depends on the unit types
    public Double getFutureRates(CostBean c1, MessageResources messageResources) throws Exception {
        String unitTypes[] = messageResources.getMessage("unittype").split(",");
        Double futureRate = 0.00;
        if (c1.getUnitType() != null && c1.getFutureRateA() != null && c1.getUnitType().equals(unitTypes[0])) {
            futureRate = Double.parseDouble(dbUtil.removeComma(c1.getFutureRateA()));
        } else if (c1.getUnitType() != null && c1.getFutureRateD() != null && c1.getUnitType().equals(unitTypes[1])) {
            futureRate = Double.parseDouble(dbUtil.removeComma(c1.getFutureRateD()));
        } else if (c1.getUnitType() != null && c1.getFutureRateB() != null && c1.getUnitType().equals(unitTypes[2])) {
            futureRate = Double.parseDouble(dbUtil.removeComma(c1.getFutureRateB()));
        } else if (c1.getUnitType() != null && c1.getFutureRateE() != null && c1.getUnitType().equals(unitTypes[3])) {
            futureRate = Double.parseDouble(dbUtil.removeComma(c1.getFutureRateE()));
        } else if (c1.getUnitType() != null && c1.getFutureRateC() != null && c1.getUnitType().equals(unitTypes[4])) {
            futureRate = Double.parseDouble(dbUtil.removeComma(c1.getFutureRateC()));
        } else if (c1.getUnitType() != null && c1.getFutureRateF() != null && c1.getUnitType().equals(unitTypes[5])) {
            futureRate = Double.parseDouble(dbUtil.removeComma(c1.getFutureRateF()));
        } else if (c1.getUnitType() != null && c1.getFutureRateG() != null && c1.getUnitType().equals(unitTypes[6])) {
            futureRate = Double.parseDouble(dbUtil.removeComma(c1.getFutureRateG()));
        } else if (c1.getUnitType() != null && c1.getFutureRateH() != null && c1.getUnitType().equals(unitTypes[7])) {
            futureRate = Double.parseDouble(dbUtil.removeComma(c1.getFutureRateH()));
        } else if (c1.getUnitType() != null && c1.getFutureRateI() != null && c1.getUnitType().equals(unitTypes[8])) {
            futureRate = Double.parseDouble(dbUtil.removeComma(c1.getFutureRateI()));
        } else if (c1.getUnitType() != null && c1.getFutureRateJ() != null && c1.getUnitType().equals(unitTypes[9])) {
            futureRate = Double.parseDouble(dbUtil.removeComma(c1.getFutureRateJ()));
        } else if (c1.getUnitType() != null && c1.getFutureRateK() != null && c1.getUnitType().equals(unitTypes[10])) {
            futureRate = Double.parseDouble(dbUtil.removeComma(c1.getFutureRateK()));
        } else if (c1.getUnitType() != null && c1.getFutureRateL() != null && c1.getUnitType().equals(unitTypes[11])) {
            futureRate = Double.parseDouble(dbUtil.removeComma(c1.getFutureRateL()));
        } else if (c1.getUnitType() != null && c1.getFutureRateM() != null && c1.getUnitType().equals(unitTypes[12])) {
            futureRate = Double.parseDouble(dbUtil.removeComma(c1.getFutureRateM()));
        } else if (c1.getUnitType() != null && c1.getFutureRateN() != null && c1.getUnitType().equals(unitTypes[13])) {
            futureRate = Double.parseDouble(dbUtil.removeComma(c1.getFutureRateN()));
        } else if (c1.getUnitType() != null && c1.getFutureRateO() != null && c1.getUnitType().equals(unitTypes[14])) {
            futureRate = Double.parseDouble(dbUtil.removeComma(c1.getFutureRateO()));
        } else if (c1.getUnitType() != null && c1.getFutureRateP() != null && c1.getUnitType().equals(unitTypes[15])) {
            futureRate = Double.parseDouble(dbUtil.removeComma(c1.getFutureRateP()));
        } else if (c1.getUnitType() != null && c1.getFutureRateQ() != null && c1.getUnitType().equals(unitTypes[16])) {
            futureRate = Double.parseDouble(dbUtil.removeComma(c1.getFutureRateQ()));
        }
        return futureRate;
    }

//	save perkglbs charges in the charges table
    public void getOtherChargesforKG(List chargesList, Quotation quotation) throws Exception {
        for (int i = 0; i < chargesList.size(); i++) {
            Charges c1 = (Charges) chargesList.get(i);
            chargesDAO.delete(c1);
            c1.setQouteId(quotation.getQuoteId());
            chargesDAO.save(c1);
        }
    }

//	save perbl charges in the charges table
    public void getOtherCharges(List chargesList, Quotation quotation) throws Exception {
        for (int i = 0; i < chargesList.size(); i++) {
            Charges c1 = (Charges) chargesList.get(i);
            chargesDAO.delete(c1);
            c1.setQouteId(quotation.getQuoteId());
            chargesDAO.save(c1);
        }
    }

//	set all the currencies into quotation form from the chargeslist
    public QuotesForm getCurrency(List chargesList, QuotesForm quotationForm, MessageResources messageResources) throws Exception {
        String currencies[] = messageResources.getMessage("currency").split(",");
        Double totalCharges = 0.00;
        Double won = 0.00;
        Double baht = 0.00;
        Double nt = 0.00;
        Double yen = 0.00;
        Double hkd = 0.0;
        Double rmb = 0.00;
        Double prs = 0.00;
        Double lkr = 0.00;
        Double BDT = 0.00;
        Double eur = 0.00;
        Double cyp = 0.00;
        Double myr = 0.00;
        Double nht = 0.00;
        Double pkr = 0.00;
        Double rm = 0.00;
        Double spo = 0.00;
        Double vnd = 0.00;
        Double inr = 0.00;
        for (int j = 0; j < chargesList.size(); j++) {
            Charges charges = (Charges) chargesList.get(j);
            if (charges.getCurrecny() != null && charges.getCurrecny().trim().equals(currencies[0])) {
                myr = myr + charges.getAmount();
                myr = myr + charges.getMarkUp();
            }
            if (charges.getCurrecny() != null && charges.getCurrecny().trim().equals(currencies[1])) {
                nht = nht + charges.getAmount();
                nht = nht + charges.getMarkUp();
            }
            if (charges.getCurrecny() != null && charges.getCurrecny().trim().equals(currencies[2])) {
                pkr = pkr + charges.getAmount();
                pkr = pkr + charges.getMarkUp();
            }
            if (charges.getCurrecny() != null && charges.getCurrecny().trim().equals(currencies[3])) {
                rm = rm + charges.getAmount();
                rm = rm + charges.getMarkUp();
            }
            if (charges.getCurrecny() != null && charges.getCurrecny().trim().equals(currencies[4])) {
                spo = spo + charges.getAmount();
                spo = spo + charges.getMarkUp();
            }
            if (charges.getCurrecny() != null && charges.getCurrecny().trim().equals(currencies[5])) {
                vnd = vnd + charges.getAmount();
                vnd = vnd + charges.getMarkUp();
            }
            if (charges.getCurrecny() != null && charges.getCurrecny().trim().equals(currencies[6])) {
                inr = inr + charges.getAmount();
                inr = inr + charges.getMarkUp();
            }
            if (charges.getCurrecny() != null && charges.getCurrecny().trim().equals(currencies[7])) {
                totalCharges = totalCharges + charges.getAmount();
                totalCharges = totalCharges + charges.getMarkUp();
            }
            if (charges.getCurrecny() != null && charges.getCurrecny().trim().equals(currencies[8])) {
                baht = baht + charges.getAmount();
                baht = baht + charges.getMarkUp();
            }
            if (charges.getCurrecny() != null && charges.getCurrecny().trim().equals(currencies[9])) {
                BDT = BDT + charges.getAmount();
                BDT = BDT + charges.getMarkUp();
            }
            if (charges.getCurrecny() != null && charges.getCurrecny().trim().equals(currencies[10])) {
                cyp = cyp + charges.getAmount();
                cyp = cyp + charges.getMarkUp();
            }
            if (charges.getCurrecny() != null && charges.getCurrecny().trim().equals(currencies[11])) {
                eur = eur + charges.getAmount();
                eur = eur + charges.getMarkUp();
            }
            if (charges.getCurrecny() != null && charges.getCurrecny().trim().equals(currencies[12])) {
                hkd = hkd + charges.getAmount();
                hkd = hkd + charges.getMarkUp();
            }
            if (charges.getCurrecny() != null && charges.getCurrecny().trim().equals(currencies[13])) {
                lkr = lkr + charges.getAmount();
                lkr = lkr + charges.getMarkUp();
            }
            if (charges.getCurrecny() != null && charges.getCurrecny().trim().equals(currencies[14])) {
                nt = nt + charges.getAmount();
                nt = nt + charges.getMarkUp();
            }
            if (charges.getCurrecny() != null && charges.getCurrecny().trim().equals(currencies[15])) {
                prs = prs + charges.getAmount();
                prs = prs + charges.getMarkUp();
            }
            if (charges.getCurrecny() != null && charges.getCurrecny().trim().equals(currencies[16])) {
                rmb = rmb + charges.getAmount();
                rmb = rmb + charges.getMarkUp();
            }
            if (charges.getCurrecny() != null && charges.getCurrecny().trim().equals(currencies[17])) {
                won = won + charges.getAmount();
                won = won + charges.getMarkUp();
            }
            if (charges.getCurrecny() != null && charges.getCurrecny().trim().equals(currencies[18])) {
                yen = yen + charges.getAmount();
                yen = yen + charges.getMarkUp();
            }
        }
        quotationForm.setTotalCharges(numb.format(totalCharges));
        quotationForm.setBaht(numb.format(baht));
        quotationForm.setBdt(numb.format(BDT));
        quotationForm.setCyp(numb.format(cyp));
        quotationForm.setEur(numb.format(eur));
        quotationForm.setHkd(numb.format(hkd));
        quotationForm.setLkr(numb.format(lkr));
        quotationForm.setNt(numb.format(nt));
        quotationForm.setPrs(numb.format(prs));
        quotationForm.setRmb(numb.format(rmb));
        quotationForm.setWon(numb.format(won));
        quotationForm.setYen(numb.format(yen));
        quotationForm.setMyr(numb.format(myr));
        quotationForm.setNht(numb.format(nht));
        quotationForm.setPkr(numb.format(pkr));
        quotationForm.setRm(numb.format(rm));
        quotationForm.setSpo(numb.format(spo));
        quotationForm.setVnd(numb.format(vnd));
        quotationForm.setInr(numb.format(inr));
        return quotationForm;
    }

//	set all the currencies into quotation object from the chargeslist
    public Quotation getCurrencyForQuotation(List chargesList, Quotation quotation, MessageResources messageResources) throws Exception {
        String currencies[] = messageResources.getMessage("currency").split(",");
        Double totalCharges = 0.00;
        Double won = 0.00;
        Double baht = 0.00;
        Double nt = 0.00;
        Double yen = 0.00;
        Double hkd = 0.0;
        Double rmb = 0.00;
        Double prs = 0.00;
        Double lkr = 0.00;
        Double BDT = 0.00;
        Double eur = 0.00;
        Double cyp = 0.00;
        Double myr = 0.00;
        Double nht = 0.00;
        Double pkr = 0.00;
        Double rm = 0.00;
        Double spo = 0.00;
        Double vnd = 0.00;
        Double inr = 0.00;
        for (int j = 0; j < chargesList.size(); j++) {
            Charges c1 = (Charges) chargesList.get(j);
            if (!c1.getChgCode().equals("INSURANCE")) {
                if (c1.getMarkUp() == null) {
                    c1.setMarkUp(0.00);
                }
                if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currencies[0])) {
                    myr = myr + c1.getAmount();
                    myr = myr + c1.getMarkUp();
                }
                if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currencies[1])) {
                    nht = nht + c1.getAmount();
                    nht = nht + c1.getMarkUp();
                }
                if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currencies[2])) {
                    pkr = pkr + c1.getAmount();
                    pkr = pkr + c1.getMarkUp();
                }
                if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currencies[3])) {
                    rm = rm + c1.getAmount();
                    rm = rm + c1.getMarkUp();
                }
                if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currencies[4])) {
                    spo = spo + c1.getAmount();
                    spo = spo + c1.getMarkUp();
                }
                if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currencies[5])) {
                    vnd = vnd + c1.getAmount();
                    vnd = vnd + c1.getMarkUp();
                }
                if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currencies[6])) {
                    inr = inr + c1.getAmount();
                    inr = inr + c1.getMarkUp();
                }
                if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currencies[7])) {
                    totalCharges = totalCharges + c1.getAmount();
                    totalCharges = totalCharges + c1.getMarkUp();
                }
                if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currencies[8])) {
                    baht = baht + c1.getAmount();
                    baht = baht + c1.getMarkUp();
                }
                if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currencies[9])) {
                    BDT = BDT + c1.getAmount();
                    BDT = BDT + c1.getMarkUp();
                }
                if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currencies[10])) {
                    cyp = cyp + c1.getAmount();
                    cyp = cyp + c1.getMarkUp();
                }
                if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currencies[11])) {
                    eur = eur + c1.getAmount();
                    eur = eur + c1.getMarkUp();
                }
                if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currencies[12])) {
                    hkd = hkd + c1.getAmount();
                    hkd = hkd + c1.getMarkUp();
                }
                if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currencies[13])) {
                    lkr = lkr + c1.getAmount();
                    lkr = lkr + c1.getMarkUp();
                }
                if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currencies[14])) {
                    nt = nt + c1.getAmount();
                    nt = nt + c1.getMarkUp();
                }
                if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currencies[15])) {
                    prs = prs + c1.getAmount();
                    prs = prs + c1.getMarkUp();
                }
                if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currencies[16])) {
                    rmb = rmb + c1.getAmount();
                    rmb = rmb + c1.getMarkUp();
                }
                if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currencies[17])) {
                    won = won + c1.getAmount();
                    won = won + c1.getMarkUp();
                }
                if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currencies[18])) {
                    yen = yen + c1.getAmount();
                    yen = yen + c1.getMarkUp();
                }
            }
            //  }
        }
        quotation.setTotalCharges(totalCharges);
        quotation.setBaht(baht);
        quotation.setBdt(BDT);
        quotation.setCyp(cyp);
        quotation.setEur(eur);
        quotation.setHkd(hkd);
        quotation.setLkr(lkr);
        quotation.setNt(nt);
        quotation.setPrs(prs);
        quotation.setRmb(rmb);
        quotation.setWon(won);
        quotation.setYen(yen);
        quotation.setMyr(myr);
        quotation.setNht(nht);
        quotation.setPkr(pkr);
        quotation.setRm(rm);
        quotation.setSpo(spo);
        quotation.setVnd(vnd);
        quotation.setInr(inr);
        return quotation;
    }

//	set all the currencies into quotation form from the perbl list
    public QuotesForm getOtherCurrency(List chargesList, QuotesForm quotationForm, MessageResources messageResources) {
        String currencies[] = messageResources.getMessage("currency").split(",");
        Double totalCharges = Double.parseDouble(dbUtil.removeComma(quotationForm.getTotalCharges()));
        Double won = Double.parseDouble(dbUtil.removeComma(quotationForm.getWon()));
        Double baht = Double.parseDouble(dbUtil.removeComma(quotationForm.getBaht()));
        Double nt = Double.parseDouble(dbUtil.removeComma(quotationForm.getNt()));
        Double yen = Double.parseDouble(dbUtil.removeComma(quotationForm.getYen()));
        Double hkd = Double.parseDouble(dbUtil.removeComma(quotationForm.getHkd()));
        Double rmb = Double.parseDouble(dbUtil.removeComma(quotationForm.getRmb()));
        Double prs = Double.parseDouble(dbUtil.removeComma(quotationForm.getPrs()));
        Double lkr = Double.parseDouble(dbUtil.removeComma(quotationForm.getLkr()));
        Double BDT = Double.parseDouble(dbUtil.removeComma(quotationForm.getBdt()));
        Double eur = Double.parseDouble(dbUtil.removeComma(quotationForm.getEur()));
        Double cyp = Double.parseDouble(dbUtil.removeComma(quotationForm.getCyp()));
        Double myr = Double.parseDouble(dbUtil.removeComma(quotationForm.getMyr()));
        Double nht = Double.parseDouble(dbUtil.removeComma(quotationForm.getNht()));
        Double pkr = Double.parseDouble(dbUtil.removeComma(quotationForm.getPkr()));
        Double rm = Double.parseDouble(dbUtil.removeComma(quotationForm.getRm()));
        Double spo = Double.parseDouble(dbUtil.removeComma(quotationForm.getSpo()));
        Double vnd = Double.parseDouble(dbUtil.removeComma(quotationForm.getVnd()));
        Double inr = Double.parseDouble(dbUtil.removeComma(quotationForm.getInr()));
        for (int j = 0; j < chargesList.size(); j++) {
            Charges c1 = (Charges) chargesList.get(j);
            if (c1.getCurrency1() != null && c1.getCurrency1().getCode().trim().equals(currencies[0])) {
                myr = myr + c1.getRetail();
            }
            if (c1.getCurrency1() != null && c1.getCurrency1().getCode().trim().equals(currencies[1])) {
                nht = nht + c1.getRetail();
            }
            if (c1.getCurrency1() != null && c1.getCurrency1().getCode().trim().equals(currencies[2])) {
                pkr = pkr + c1.getRetail();
            }
            if (c1.getCurrency1() != null && c1.getCurrency1().getCode().trim().equals(currencies[3])) {
                rm = rm + c1.getRetail();
            }
            if (c1.getCurrency1() != null && c1.getCurrency1().getCode().trim().equals(currencies[4])) {
                spo = spo + c1.getRetail();
            }
            if (c1.getCurrency1() != null && c1.getCurrency1().getCode().trim().equals(currencies[5])) {
                vnd = vnd + c1.getRetail();
            }
            if (c1.getCurrency1() != null && c1.getCurrency1().getCode().trim().equals(currencies[6])) {
                inr = inr + c1.getRetail();
            }
            if (c1.getCurrency1() != null && c1.getRetail() != null && c1.getCurrency1().getCode().trim().equals(currencies[7])) {
                totalCharges = totalCharges + c1.getRetail();
            }
            if (c1.getCurrency1() != null && c1.getCurrency1().getCode().trim().equals(currencies[8])) {
                baht = baht + c1.getRetail();
            }
            if (c1.getCurrency1() != null && c1.getCurrency1().getCode().trim().equals(currencies[9])) {
                BDT = BDT + c1.getRetail();
            }
            if (c1.getCurrency1() != null && c1.getCurrency1().getCode().trim().equals(currencies[10])) {
                cyp = cyp + c1.getRetail();
            }
            if (c1.getCurrency1() != null && c1.getCurrency1().getCode().trim().equals(currencies[11])) {
                eur = eur + c1.getRetail();
            }
            if (c1.getCurrency1() != null && c1.getCurrency1().getCode().trim().equals(currencies[12])) {
                hkd = hkd + c1.getRetail();
            }
            if (c1.getCurrency1() != null && c1.getCurrency1().getCode().trim().equals(currencies[13])) {
                lkr = lkr + c1.getRetail();
            }
            if (c1.getCurrency1() != null && c1.getCurrency1().getCode().trim().equals(currencies[14])) {
                nt = nt + c1.getRetail();
            }
            if (c1.getCurrency1() != null && c1.getCurrency1().getCode().trim().equals(currencies[15])) {
                prs = prs + c1.getRetail();
            }
            if (c1.getCurrency1() != null && c1.getCurrency1().getCode().trim().equals(currencies[16])) {
                rmb = rmb + c1.getRetail();
            }
            if (c1.getCurrency1() != null && c1.getCurrency1().getCode().trim().equals(currencies[17])) {
                won = won + c1.getRetail();
            }
            if (c1.getCurrency1() != null && c1.getCurrency1().getCode().trim().equals(currencies[18])) {
                yen = yen + c1.getRetail();
            }
        }
        quotationForm.setTotalCharges(numb.format(totalCharges));
        quotationForm.setBaht(numb.format(baht));
        quotationForm.setBdt(numb.format(BDT));
        quotationForm.setCyp(numb.format(cyp));
        quotationForm.setEur(numb.format(eur));
        quotationForm.setHkd(numb.format(hkd));
        quotationForm.setLkr(numb.format(lkr));
        quotationForm.setNt(numb.format(nt));
        quotationForm.setPrs(numb.format(prs));
        quotationForm.setRmb(numb.format(rmb));
        quotationForm.setWon(numb.format(won));
        quotationForm.setYen(numb.format(yen));
        quotationForm.setMyr(numb.format(myr));
        quotationForm.setNht(numb.format(nht));
        quotationForm.setPkr(numb.format(pkr));
        quotationForm.setRm(numb.format(rm));
        quotationForm.setSpo(numb.format(spo));
        quotationForm.setVnd(numb.format(vnd));
        quotationForm.setInr(numb.format(inr));
        return quotationForm;
    }

//	set all the currencies into quotation object from the perbl list
    public Quotation getOtherCurrencyForQuotation(List chargesList, Quotation quotation, MessageResources messageResources) {
        String currencies[] = messageResources.getMessage("currency").split(",");
        Double totalCharges = quotation.getTotalCharges();
        Double won = quotation.getWon();
        Double baht = quotation.getBaht();
        Double nt = quotation.getNt();
        Double yen = quotation.getYen();
        Double hkd = quotation.getHkd();
        Double rmb = quotation.getRmb();
        Double prs = quotation.getPrs();
        Double lkr = quotation.getLkr();
        Double BDT = quotation.getBdt();
        Double eur = quotation.getEur();
        Double cyp = quotation.getCyp();
        Double myr = quotation.getMyr();
        Double nht = quotation.getNht();
        Double pkr = quotation.getPkr();
        Double rm = quotation.getRm();
        Double spo = quotation.getSpo();
        Double vnd = quotation.getVnd();
        Double inr = quotation.getInr();
        for (int j = 0; j < chargesList.size(); j++) {
            Charges c1 = (Charges) chargesList.get(j);

            if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currencies[0])) {
                myr = myr + c1.getRetail();
            }
            if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currencies[1])) {
                nht = nht + c1.getRetail();
            }
            if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currencies[2])) {
                pkr = pkr + c1.getRetail();
            }
            if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currencies[3])) {
                rm = rm + c1.getRetail();
            }
            if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currencies[4])) {
                spo = spo + c1.getRetail();
            }
            if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currencies[5])) {
                vnd = vnd + c1.getRetail();
            }
            if (c1.getCurrecny() != null && c1.getRetail() != null && c1.getCurrecny().trim().equals(currencies[6])) {
                inr = inr + c1.getRetail();
            }
            if (c1.getCurrecny() != null && c1.getAmount() != null && c1.getCurrecny().trim().equals(currencies[7])) {
                totalCharges = totalCharges + c1.getAmount();

            }
            if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currencies[8])) {
                baht = baht + c1.getRetail();
            }
            if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currencies[9])) {
                BDT = BDT + c1.getRetail();
            }
            if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currencies[10])) {
                cyp = cyp + c1.getRetail();
            }
            if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currencies[11])) {
                eur = eur + c1.getRetail();
            }
            if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currencies[12])) {
                hkd = hkd + c1.getRetail();
            }
            if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currencies[13])) {
                lkr = lkr + c1.getRetail();
            }
            if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currencies[14])) {
                nt = nt + c1.getRetail();
            }
            if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currencies[15])) {
                prs = prs + c1.getRetail();
            }
            if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currencies[16])) {
                rmb = rmb + c1.getRetail();
            }
            if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currencies[17])) {
                won = won + c1.getRetail();
            }
            if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currencies[18])) {
                yen = yen + c1.getRetail();
            }
            // }
        }
        quotation.setTotalCharges(totalCharges);
        quotation.setBaht(baht);
        quotation.setBdt(BDT);
        quotation.setCyp(cyp);
        quotation.setEur(eur);
        quotation.setHkd(hkd);
        quotation.setLkr(lkr);
        quotation.setNt(nt);
        quotation.setPrs(prs);
        quotation.setRmb(rmb);
        quotation.setWon(won);
        quotation.setYen(yen);
        quotation.setMyr(myr);
        quotation.setNht(nht);
        quotation.setPkr(pkr);
        quotation.setRm(rm);
        quotation.setSpo(spo);
        quotation.setVnd(vnd);
        quotation.setInr(inr);
        return quotation;
    }

//	set all the charges into charges object form from the quotation form
    public Charges addCharges(QuotesForm quotationForm) throws Exception {
        Charges charges = new Charges();
        charges.setUnitType(quotationForm.getUnitSelect());
        charges.setNumber(quotationForm.getNumber());
        charges.setAmount(Double.parseDouble(dbUtil.removeComma(quotationForm.getChargeAmt())));
        charges.setMinimum(Double.parseDouble(dbUtil.removeComma(quotationForm.getMinimumAmt())));
        if (quotationForm.getCurrency() != null && !quotationForm.getCurrency().equals("0")) {
            GenericCode gen = genericCodeDAO.findById(Integer.parseInt(quotationForm.getCurrency()));
            charges.setCurrecny(gen.getCode());
            charges.setCurrency1(gen);
        }
        if (quotationForm.getChargeCodeDesc() != null && !quotationForm.getChargeCodeDesc().equals("0")) {
            GenericCode gen = genericCodeDAO.findById(Integer.parseInt(quotationForm.getChargeCodeDesc()));
            charges.setChgCode(gen.getCodedesc());
            charges.setChargeCodeDesc(gen.getCode());
            charges.setChargeCode(gen);
        }
        if (quotationForm.getCostSelect() != null && !quotationForm.getCostSelect().equals("0")) {
            GenericCode gen1 = genericCodeDAO.findById(Integer.parseInt(quotationForm.getCostSelect()));
            charges.setCostType(gen1.getCodedesc());
            charges.setCosttype(gen1);
        }
        return charges;
    }

    public Charges addChargesForEditQuotesForm(SearchQuotationForm editQuotesForm) throws Exception {
        Charges charges = new Charges();
        charges.setUnitType(editQuotesForm.getUnitSelect());
        charges.setNumber(editQuotesForm.getNumber());
        charges.setAmount(Double.parseDouble(dbUtil.removeComma(editQuotesForm.getChargeAmt())));
        charges.setMinimum(Double.parseDouble(dbUtil.removeComma(editQuotesForm.getMinimumAmt())));
        if (editQuotesForm.getCurrency() != null && !editQuotesForm.getCurrency().equals("0")) {
            GenericCode gen = genericCodeDAO.findById(Integer.parseInt(editQuotesForm.getCurrency()));
            charges.setCurrecny(gen.getCode());
            charges.setCurrency1(gen);
        }
        if (editQuotesForm.getChargeCodeDesc() != null && !editQuotesForm.getChargeCodeDesc().equals("0")) {
            GenericCode gen = genericCodeDAO.findById(Integer.parseInt(editQuotesForm.getChargeCodeDesc()));
            charges.setChgCode(gen.getCodedesc());
            charges.setChargeCodeDesc(gen.getCode());
            charges.setChargeCode(gen);
        }
        if (editQuotesForm.getCostSelect() != null && !editQuotesForm.getCostSelect().equals("0")) {
            GenericCode gen1 = genericCodeDAO.findById(Integer.parseInt(editQuotesForm.getCostSelect()));
            charges.setCostType(gen1.getCodedesc());
            charges.setCosttype(gen1);
        }
        return charges;
    }

    //set all the values charges object into costbean
    public Charges addCostBean(Charges charges, MessageResources messageResources, String unitSelect, String number) throws Exception {
        Charges newCharges = new Charges();
        PropertyUtils.copyProperties(newCharges, charges);
        newCharges.setChargeFlag("M");
        if (unitSelect != null) {
            GenericCode gen = genericCodeDAO.findById(Integer.parseInt(unitSelect));
            if (gen != null) {
                newCharges.setUnitName(gen.getCodedesc());
                newCharges.setUnitType(gen.getId().toString());
            }
        }
        newCharges.setInclude("on");
        newCharges.setPrint("off");
        if (number == null || number.equals("")) {
            number = "1";
        }
        newCharges.setNumber(number);
        newCharges.setEfectiveDate(new Date());
        newCharges.setNewFlag("new");
        return newCharges;
    }

    public Charges addCostBeanForPercenOFR(Charges charges, MessageResources messageResources, String unitSelect, String number, Charges c6) throws Exception {
        Charges c1 = new Charges();
        c1.setUnitType(unitSelect);
        if (number == null || number.equals("")) {
            number = "1";
        }
        c1.setNumber(number);
        c1.setChgCode(charges.getChgCode());
        c1.setChargeCodeDesc(charges.getChargeCodeDesc());
        c1.setCostType(charges.getCostType());
        c1.setCurrecny(charges.getCurrecny());
        c1.setChargeFlag("M");
        GenericCode gen = genericCodeDAO.findById(Integer.parseInt(unitSelect));
        c1.setUnitName(gen.getCodedesc());
        c1.setUnitType(gen.getId().toString());
        c1.setInclude("off");
        c1.setPrint("off");
        Double amount = charges.getAmount() * Double.parseDouble(number);
        c1.setAmount(amount * c6.getAmount() / 100);
        c1.setMarkUp(charges.getMarkUp() * Double.parseDouble(number));
        c1.setEfectiveDate(new Date());
        c1.setNewFlag("new");
        return c1;
    }

    public List<HazmatMaterial> getHazmatList(String name, String number) throws Exception {
        List hazmatList = null;
        if (number != null && !number.equals("")) {
            hazmatList = hazmatMaterialDAO.findbydoctypeid1(name, Integer.parseInt(number));
        } else {
            hazmatList = hazmatMaterialDAO.findListByDocTypeCode(name);
        }
        hazmatList = (CommonFunctions.isNotNullOrNotEmpty(hazmatList)) ? hazmatList : Collections.EMPTY_LIST;
        return hazmatList;
    }

    public void saveHazmat(FCLHazMatForm fCLHazMatForm, HazmatMaterial hazmatMaterial) throws Exception {
        if (hazmatMaterial == null) {
            hazmatMaterial = new HazmatMaterial();
        }
        hazmatMaterial.setFlashPointUMO(fCLHazMatForm.getFlashPointNo());
        hazmatMaterial.setNetWeightUMO(fCLHazMatForm.getNetWeightUMO());
        hazmatMaterial.setGrossWeightUMO(fCLHazMatForm.getGrossWeightUMO());
        hazmatMaterial.setUnNumber(fCLHazMatForm.getUnHazmatNumber());
        hazmatMaterial.setPropShipingNumber(fCLHazMatForm.getShippingname());
        hazmatMaterial.setTechnicalName(fCLHazMatForm.getTechnicalName());
        hazmatMaterial.setImoClssCode(fCLHazMatForm.getImoClssCode());
        hazmatMaterial.setImoSubsidiaryClassCode(fCLHazMatForm.getImoSubsidiaryClassCode());
        hazmatMaterial.setImoSecondarySubClass(fCLHazMatForm.getImoSecondarySubClass());
        hazmatMaterial.setPackingGroupCode(fCLHazMatForm.getPackingGroupCode());
        hazmatMaterial.setFlashPoint("C");
        hazmatMaterial.setPackingGroupCodeUom(fCLHazMatForm.getPackingGroupCodeUom());
        hazmatMaterial.setExceptedQuantity(fCLHazMatForm.getExceptedQuantity());
        hazmatMaterial.setMarinePollutant(fCLHazMatForm.getMarinePollutant());
        hazmatMaterial.setLimitedQuantity(fCLHazMatForm.getLimitedQuantity());
        hazmatMaterial.setInnerPackingPieces(fCLHazMatForm.getInnerPackingPieces());
        hazmatMaterial.setOuterPackingPieces(fCLHazMatForm.getOuterPackingPieces());
        hazmatMaterial.setInnerPackComposition(fCLHazMatForm.getInnerPackComposition());
        hazmatMaterial.setOuterPackComposition(fCLHazMatForm.getOuterPackComposition());
        hazmatMaterial.setInnerPackagingType(fCLHazMatForm.getInnerPackagingType());
        hazmatMaterial.setOuterPackagingType(fCLHazMatForm.getOuterPackagingType());
        hazmatMaterial.setNetWeight(Double.parseDouble(dbUtil.removeComma(fCLHazMatForm.getNetWeight())));
        hazmatMaterial.setGrossWeight(Double.parseDouble(dbUtil.removeComma(fCLHazMatForm.getGrossWeight())));
        hazmatMaterial.setTotalNetWeight(Double.parseDouble(dbUtil.removeComma(fCLHazMatForm.getTotalNetWeight())));
        hazmatMaterial.setVolume(Double.parseDouble(dbUtil.removeComma(fCLHazMatForm.getVolume())));
        hazmatMaterial.setReportableQuantity(fCLHazMatForm.getReportableQuantity());
        hazmatMaterial.setInhalationHazard(fCLHazMatForm.getInhalationHazard());
        hazmatMaterial.setResidue(fCLHazMatForm.getResidue());
        hazmatMaterial.setEmsCode(fCLHazMatForm.getEmsCode());
        hazmatMaterial.setFreeFormat(fCLHazMatForm.getFreeFormat());
        hazmatMaterial.setContactName(fCLHazMatForm.getContactName());
        hazmatMaterial.setEmerreprsNum(fCLHazMatForm.getEmergRespTelNo());
        if (CommonUtils.isNotEmpty(fCLHazMatForm.getQuoteName())) {
            hazmatMaterial.setDocTypeCode(fCLHazMatForm.getQuoteName());
            hazmatMaterial.setDocTypeId(fCLHazMatForm.getQuoteNumber());
        } else {
            hazmatMaterial.setDocTypeCode(fCLHazMatForm.getName());
            hazmatMaterial.setDocTypeId(fCLHazMatForm.getNumber());
        }
        hazmatMaterial.setDate(new Date());
        if (CommonUtils.isNotEmpty(fCLHazMatForm.getQuoteNumber())) {
            hazmatMaterial.setBolId(Integer.parseInt(fCLHazMatForm.getQuoteNumber()));
        } else if (null != fCLHazMatForm.getNumber() && !fCLHazMatForm.getNumber().equalsIgnoreCase("")) {
            hazmatMaterial.setBolId(Integer.parseInt(fCLHazMatForm.getNumber()));
        }
        if ("Y".equalsIgnoreCase(fCLHazMatForm.getFreeFormat())) {
            hazmatMaterial.setLine1(fCLHazMatForm.getLine1());
            hazmatMaterial.setLine2(fCLHazMatForm.getLine2());
            hazmatMaterial.setLine3(fCLHazMatForm.getLine3());
            hazmatMaterial.setLine4(fCLHazMatForm.getLine4());
            hazmatMaterial.setLine5(fCLHazMatForm.getLine5());
            hazmatMaterial.setLine6(fCLHazMatForm.getLine6());
            hazmatMaterial.setLine7(fCLHazMatForm.getLine7());
        } else {
            hazmatMaterial.setLine1("");
            hazmatMaterial.setLine2("");
            hazmatMaterial.setLine3("");
            hazmatMaterial.setLine4("");
            hazmatMaterial.setLine5("");
            hazmatMaterial.setLine6("");
            hazmatMaterial.setLine7("");
        }
        hazmatMaterialDAO.save(hazmatMaterial);
    }

    public QuotationDTO getQuotationDTO(QuotationDTO quotationDTO, QuotesForm quotesForm) throws Exception {
        quotationDTO.setOriginCheck(quotesForm.getOriginCheck());
        quotationDTO.setDestinationCheck(quotesForm.getDestinationCheck());
        quotationDTO.setBulletRatesCheck(quotesForm.getBulletRatesCheck());
        quotationDTO.setPolCheck(quotesForm.getPolCheck());
        quotationDTO.setPodCheck(quotesForm.getPodCheck());
        quotationDTO.setRampCheck(quotesForm.getRampCheck());
        quotationDTO.setCommcode(quotesForm.getCommcode());
        quotationDTO.setIsTerminal(quotesForm.getIsTerminal());
        quotationDTO.setPortofDischarge(quotesForm.getPortofDischarge());
        quotationDTO.setPlaceofReceipt(quotesForm.getPlaceofReceipt());
        quotationDTO.setFinalDestination(quotesForm.getFinalDestination());
        quotationDTO.setButtonValue(quotesForm.getButtonValue());
        quotationDTO.setRampCity(quotesForm.getRampCity());
        quotationDTO.setTypeOfMove(quotesForm.getTypeofMove());
        return quotationDTO;
    }

    public Charges getCostBean(String number, Charges charges) throws Exception {
        Charges c1 = new Charges();
        c1.setUnitType(null);
        c1.setNumber(number);
        c1.setChgCode(charges.getChgCode());
        c1.setChargeCodeDesc(charges.getChargeCodeDesc());
        c1.setCostType(charges.getCostType());
        c1.setCurrecny(charges.getCurrecny());
        c1.setAccountName(charges.getAccountName());
        c1.setAccountNo(charges.getAccountNo());
        c1.setAmount(charges.getAmount());
        c1.setMarkUp(charges.getMarkUp());
        c1.setDefaultCarrier(charges.getDefaultCarrier());
        c1.setComment(CommonFunctions.isNotNull(charges.getComment())
                ? charges.getComment().toUpperCase() : charges.getComment());
        c1.setChargeFlag("M");
        c1.setOtherprint("off");
        c1.setOtherinclude("off");
        c1.setNewFlag("new");
        return c1;
    }

    public String doConvertToBooking(Quotation quotation, String userName, MessageResources messageResources, String bookedDate, SearchQuotationForm editQuotesForm, String bkgAlert) throws Exception {
        String msg = "";
        Double totalCharges = 0.00;
        Set bookingFclSet = new LinkedHashSet<BookingfclUnits>();
        Set hazmatSet = new LinkedHashSet<HazmatMaterial>();
        quotation.setQuoteFlag("Book");
        quotation.setFinalized("on");
        quotation.setBookedBy(userName);
        Date date1 = null;
        if (bookedDate != null) {
            date1 = dateFormat.parse(bookedDate);
        } else {
            date1 = new Date();
        }
        quotation.setBookedDate(date1);
        bookingFCL.setImportFlag(quotation.getFileType());
        if(CommonUtils.isEmpty(quotation.getFileType())){
            bookingFCL.setFileType("S");
        }else{
        bookingFCL.setFileType(quotation.getFileType());
        }
        bookingFCL.setBookedBy(quotation.getBookedBy());
        bookingFCL.setQuoteNo(quotation.getQuoteId().toString());
        bookingFCL.setQuoteDate(quotation.getQuoteDate());
        bookingFCL.setNoOfDays(quotation.getNoOfDays());
        bookingFCL.setRatesRemarks(quotation.getRatesRemarks());
        bookingFCL.setSpotRate(quotation.getSpotRate());
        bookingFCL.setBlFlag("off");
        bookingFCL.setUsername(userName);
        Date newDate = new Date();
        String newDate1 = dateFormat.format(newDate);
        Date parsedDate = null;
        parsedDate = dateFormat.parse(newDate1);
        bookingFCL.setBookingDate(parsedDate);
        bookingFCL.setOriginTerminal(quotation.getOrigin_terminal());
//        bookingFCL.setSpecialEqpmtSelectBox(quotation.getSpecialEqpmt());
        bookingFCL.setPortofDischarge(quotation.getDestination_port());
        bookingFCL.setSpecialequipment(quotation.getSpecialequipment());
        bookingFCL.setSoc(quotation.getSoc());
        bookingFCL.setCustomertoprovideSED(quotation.getCustomertoprovideSed());
        bookingFCL.setDeductFFcomm(quotation.getDeductFfcomm());
        bookingFCL.setOutofgage(quotation.getOutofgage());
        bookingFCL.setLocaldryage(quotation.getLocaldryage());
        bookingFCL.setIntermodel(quotation.getIntermodel());
        bookingFCL.setInland(quotation.getInland());
        bookingFCL.setDocCharge(quotation.getDocCharge());
        bookingFCL.setPierPass(quotation.getPierPass());
        bookingFCL.setInsurance(quotation.getInsurance());
        bookingFCL.setDestRemarks(quotation.getRemarks());
        bookingFCL.setChassisCharge(quotation.getChassisCharge());
        bookingFCL.setCarrierPrint(null == quotation.getCarrierPrint() ? "off" : quotation.getCarrierPrint());

        if (quotation.getSsline() != null) {
            bookingFCL.setSSLine(quotation.getSsline());
        }
        if (quotation.getSslname() != null && !quotation.getSslname().equalsIgnoreCase("") && quotation.getSsline() != null && !quotation.getSsline().equals("")) {
            bookingFCL.setSslname(quotation.getSslname() + "//" + quotation.getSsline());
        }
        if (quotation.getTypeofMove() != null && !quotation.getTypeofMove().equals("")) {
            GenericCode genericCode = genericCodeDAO.findByCodeName(quotation.getTypeofMove(), 48);
            if (genericCode != null) {
                bookingFCL.setMoveType(genericCode.getCode());
            } else {
                bookingFCL.setMoveType(quotation.getTypeofMove());
            }

        }

        bookingFCL.setAgent(quotation.getAgent());
        bookingFCL.setRatesRemarks(quotation.getRatesRemarks());
        bookingFCL.setAgentNo(quotation.getAgentNo());
        bookingFCL.setRoutedByAgent(quotation.getRoutedbymsg());
        bookingFCL.setRemarks(quotation.getComment1());
        bookingFCL.setAlternateAgent(quotation.getAlternateagent());
        bookingFCL.setDirectConsignmntCheck(quotation.getDirectConsignmntCheck());
        bookingFCL.setPortofOrgin(quotation.getPlor());
        bookingFCL.setDestination(quotation.getFinaldestination());
        bookingFCL.setIssuingTerminal(quotation.getIssuingTerminal());
        bookingFCL.setExportDevliery(quotation.getPoe());
        bookingFCL.setRampCity(quotation.getRampCity());
        bookingFCL.setRoutedbyAgentsCountry(quotation.getRoutedbyAgentsCountry());
        bookingFCL.setBookingComplete("N");
        bookingFCL.setDrayMarkUp(quotation.getDrayageMarkUp());
        bookingFCL.setRampCheck(quotation.getRampCheck());
        bookingFCL.setInterMarkUp(quotation.getIntermodalMarkUp());
        bookingFCL.setInsureMarkUp(quotation.getInsuranceMarkUp());
        bookingFCL.setZip(quotation.getZip());
        bookingFCL.setDoorOrigin(quotation.getDoorOrigin());
        bookingFCL.setDoorDestination(quotation.getDoorDestination());
        bookingFCL.setRatesNonRates(quotation.getRatesNonRates());
        bookingFCL.setBreakBulk(quotation.getBreakBulk());
        bookingFCL.setBilltoCode("F");
        bookingFCL.setPrepaidCollect("P");
        bookingFCL.setRoutedAgentCheck(quotation.getRoutedAgentCheck());
        bookingFCL.setSelectedUnits(quotation.getSelectedUnits());

        CustGeneralDefault custGeneralDefault = new QuoteDwrBC().getDefaultDetails(quotation.getClientnumber());
        boolean isShipper = false;
        boolean isConsignee = false;
        boolean isForwarder = false;
        String address = "";
        if (null != custGeneralDefault && !"I".equalsIgnoreCase(quotation.getFileType())) {
            bookingFCL.setLineMove(custGeneralDefault.getLineMove());
            bookingFCL.setPrepaidCollect(custGeneralDefault.getPrepaidOrCollect());
            bookingFCL.setBilltoCode(custGeneralDefault.getBillTo());
            if (CommonUtils.isNotEmpty(custGeneralDefault.getShipperNo()) || CommonUtils.isNotEmpty(custGeneralDefault.getShipper())) {
                bookingFCL.setShipNo(custGeneralDefault.getShipperNo());
                bookingFCL.setShipper(custGeneralDefault.getShipper());
                bookingFCL.setShipperTpCheck(custGeneralDefault.getShipperCheck());
                CustAddress custAddress = new CustAddressDAO().findPrimeContact(custGeneralDefault.getShipperNo());
                if (null != custAddress) {
                    bookingFCL.setshipperEmail(custAddress.getEmail1());
                    bookingFCL.setshipperPhone(custAddress.getPhone());
                    if (CommonUtils.isNotEmpty(custAddress.getCoName())) {
                        address = custAddress.getCoName() + "\n";
                    }
                    bookingFCL.setAddressforShipper(address + custAddress.getAddress1());
                    if (custAddress.getCuntry() != null) {
                        bookingFCL.setShipperCountry(custAddress.getCuntry().getCodedesc());
                    }
                    bookingFCL.setShipperState(custAddress.getState());
                    bookingFCL.setShipperCity(custAddress.getCity1());
                    bookingFCL.setShipperZip(custAddress.getZip());
                    bookingFCL.setShipperFax(custAddress.getFax());
                    bookingFCL.setShippercheck("on");
                }
                isShipper = true;
            }
            if (CommonUtils.isNotEmpty(custGeneralDefault.getConsigneeNo()) || CommonUtils.isNotEmpty(custGeneralDefault.getConsignee())) {
                bookingFCL.setConsNo(custGeneralDefault.getConsigneeNo());
                bookingFCL.setConsignee(custGeneralDefault.getConsignee());
                bookingFCL.setConsigneeTpCheck(custGeneralDefault.getConsigneeCheck());
                CustAddress custAddress = new CustAddressDAO().findPrimeContact(custGeneralDefault.getConsigneeNo());
                if (null != custAddress) {
                    bookingFCL.setConsingeeEmail(custAddress.getEmail1());
                    bookingFCL.setConsingeePhone(custAddress.getPhone());
                    address = "";
                    if (CommonUtils.isNotEmpty(custAddress.getCoName())) {
                        address = custAddress.getCoName() + "\n";
                    }
                    bookingFCL.setAddressforConsingee(address + custAddress.getAddress1());
                    if (custAddress.getCuntry() != null) {
                        bookingFCL.setConsigneeCountry(custAddress.getCuntry().getCodedesc());
                    }
                    bookingFCL.setConsigneeState(custAddress.getState());
                    bookingFCL.setConsigneeCity(custAddress.getCity1());
                    bookingFCL.setConsigneeZip(custAddress.getZip());
                    bookingFCL.setConsigneeFax(custAddress.getFax());
                    bookingFCL.setConsigneecheck("on");
                }
                isConsignee = true;
            }
            if (CommonUtils.isNotEmpty(custGeneralDefault.getForwarderNo())) {
                bookingFCL.setForwNo(custGeneralDefault.getForwarderNo());
                bookingFCL.setForward(custGeneralDefault.getForwarder());
                CustAddress custAddress = new CustAddressDAO().findPrimeContact(custGeneralDefault.getForwarderNo());
                if (null != custAddress) {
                    bookingFCL.setForwarderEmail(custAddress.getEmail1());
                    bookingFCL.setForwarderPhone(custAddress.getPhone());
                    address = "";
                    if (CommonUtils.isNotEmpty(custAddress.getCoName())) {
                        address = custAddress.getCoName() + "\n";
                    }
                    bookingFCL.setAddressforForwarder(address + custAddress.getAddress1());
                    if (custAddress.getCuntry() != null) {
                        bookingFCL.setForwarderCountry(custAddress.getCuntry().getCodedesc());
                    }
                    bookingFCL.setForwarderState(custAddress.getState());
                    bookingFCL.setForwarderCity(custAddress.getCity1());
                    bookingFCL.setForwarderZip(custAddress.getZip());
                    bookingFCL.setForwarderFax(custAddress.getFax());
                    bookingFCL.setForwardercheck("on");
                }
                isForwarder = true;
            }
        }
        CustAddressDAO custAddressDAO = new CustAddressDAO();
        List custList = custAddressDAO.getCustomerNamesList1(quotation.getClientnumber());
        CustAddress cust = null;
        for (int i = 0; i < custList.size(); i++) {
            cust = new CustAddress();
            cust = (CustAddress) custList.get(i);
            if (null != cust && "on".equalsIgnoreCase(cust.getPrimeAddress())) {
                break;
            }
        }
        String client = null, coName = null;
        String[] clientType;
        if (quotation.getClienttype() != null) {
            client = quotation.getClienttype();
            clientType = client.split(",");
            if (null != cust) {
                if (CommonFunctions.isNotNull(cust.getCoName())) {
                    coName = cust.getCoName();
                }
            }
            if (client.contentEquals("Shipper,Vendor(Forwarder),")) {
                bookingFCL.setForwNo(quotation.getClientnumber());
                bookingFCL.setForward(quotation.getClientname());
                bookingFCL.setForwarderEmail(quotation.getEmail1());
                bookingFCL.setForwarderPhone(quotation.getPhone());
                if (cust != null) {
                    if (null != coName) {
                        bookingFCL.setAddressforForwarder(coName + "\n" + cust.getAddress1());
                    } else {
                        bookingFCL.setAddressforForwarder(cust.getAddress1());
                    }
                    if (cust.getCuntry() != null) {
                        bookingFCL.setForwarderCountry(cust.getCuntry().getCodedesc());
                    }
                    bookingFCL.setForwarderState(cust.getState());
                    bookingFCL.setForwarderCity(cust.getCity1());
                    bookingFCL.setForwarderZip(cust.getZip());
                }
                bookingFCL.setForwarderFax(quotation.getFax());
                bookingFCL.setForwardercheck("on");
            } else {
                for (int i = 0; i < clientType.length; i++) {
                    String str = clientType[i];
                    if (str.equalsIgnoreCase("Shipper") && !isShipper) {
                        bookingFCL.setShipNo(quotation.getClientnumber());
                        bookingFCL.setShipper(quotation.getClientname());
                        bookingFCL.setshipperEmail(quotation.getEmail1());
                        bookingFCL.setshipperPhone(quotation.getPhone());
                        if (cust != null) {
                            if (null != coName) {
                                bookingFCL.setAddressforShipper(coName + "\n" + cust.getAddress1());
                            } else {
                                bookingFCL.setAddressforShipper(cust.getAddress1());
                            }
                            if (cust.getCuntry() != null) {
                                bookingFCL.setShipperCountry(cust.getCuntry().getCodedesc());
                            }
                            bookingFCL.setShipperState(cust.getState());
                            bookingFCL.setShipperCity(cust.getCity1());
                            bookingFCL.setShipperZip(cust.getZip());
                        }
                        bookingFCL.setShipperFax(quotation.getFax());
                        bookingFCL.setShippercheck("on");
                    } else if (str.equalsIgnoreCase("Consignee") && clientType.length == 1 && !isConsignee) {
                        bookingFCL.setConsNo(quotation.getClientnumber());
                        bookingFCL.setConsignee(quotation.getClientname());
                        bookingFCL.setConsingeeEmail(quotation.getEmail1());
                        bookingFCL.setConsingeePhone(quotation.getPhone());
                        if (cust != null) {
                            if (null != coName) {
                                bookingFCL.setAddressforConsingee(coName + "\n" + cust.getAddress1());
                            } else {
                                bookingFCL.setAddressforConsingee(cust.getAddress1());
                            }
                            if (cust.getCuntry() != null) {
                                bookingFCL.setConsigneeCountry(cust.getCuntry().getCodedesc());
                            }
                            bookingFCL.setConsigneeState(cust.getState());
                            bookingFCL.setConsigneeCity(cust.getCity1());
                            bookingFCL.setConsigneeZip(cust.getZip());
                        }
                        bookingFCL.setConsigneeFax(quotation.getFax());
                        bookingFCL.setConsigneecheck("on");
                    } else if (str.contains("Forwarder") && !isForwarder) {
                        bookingFCL.setForwNo(quotation.getClientnumber());
                        bookingFCL.setForward(quotation.getClientname());
                        bookingFCL.setForwarderEmail(quotation.getEmail1());
                        bookingFCL.setForwarderPhone(quotation.getPhone());
                        if (cust != null) {
                            if (null != coName) {
                                bookingFCL.setAddressforForwarder(coName + "\n" + cust.getAddress1());
                            } else {
                                bookingFCL.setAddressforForwarder(cust.getAddress1());
                            }
                            if (cust.getCuntry() != null) {
                                bookingFCL.setForwarderCountry(cust.getCuntry().getCodedesc());
                            }
                            bookingFCL.setForwarderState(cust.getState());
                            bookingFCL.setForwarderCity(cust.getCity1());
                            bookingFCL.setForwarderZip(cust.getZip());
                        }
                        bookingFCL.setForwarderFax(quotation.getFax());
                        bookingFCL.setForwardercheck("on");
                    }
                }
            }
        }
        bookingFCL.setGoodsDescription(quotation.getGoodsdesc());
        // bookingFCL.setSpecialequipment(quotation.getSpclEqpmt());
        bookingFCL.setHazmat(quotation.getHazmat());
        bookingFCL.setOutofgage(quotation.getOutofgage());
        bookingFCL.setBaht(quotation.getBaht());
        bookingFCL.setBdt(quotation.getBdt());
        bookingFCL.setCyp(quotation.getCyp());
        bookingFCL.setEur(quotation.getEur());
        bookingFCL.setHkd(quotation.getHkd());
        bookingFCL.setLkr(quotation.getLkr());
        bookingFCL.setNt(quotation.getNt());
        bookingFCL.setPrs(quotation.getPrs());
        bookingFCL.setRmb(quotation.getRmb());
        bookingFCL.setWon(quotation.getWon());
        bookingFCL.setYen(quotation.getYen());
        bookingFCL.setFileNo(quotation.getFileNo());
        bookingFCL.setLocaldryage(quotation.getLocaldryage());
        /*if(quotation.getLdinclude()!=null && quotation.getLdinclude().equals("on")){
        totalCharges=totalCharges+quotation.getAmount();
        }*/
        bookingFCL.setAmount(quotation.getAmount());
        bookingFCL.setIntermodel(quotation.getIntermodel());
        bookingFCL.setInland(quotation.getInland());
        bookingFCL.setDocCharge(quotation.getDocCharge());
        bookingFCL.setPierPass(quotation.getPierPass());
        bookingFCL.setChassisCharge(quotation.getChassisCharge());
        bookingFCL.setAmount1(quotation.getAmount1());
        bookingFCL.setBrand(quotation.getBrand());
        if (quotation.getIdinclude() != null && quotation.getIdinclude().equals("on")) {
            totalCharges = totalCharges + quotation.getAmount1();
        }
        bookingFCL.setInsurance(quotation.getInsurance());
        bookingFCL.setInsurancamt(quotation.getInsurancamt());

        totalCharges = totalCharges + quotation.getInsuranceCharge();
        bookingFCL.setCostofgoods(quotation.getCostofgoods());
        bookingFCL.setTotalCharges(totalCharges);
        if (quotation.getCommcode() != null) {
            bookingFCL.setComcode(quotation.getCommcode().getCode());
            bookingFCL.setComdesc(quotation.getCommcode().getCodedesc());
        }
        List hazmatList = hazmatMaterialDAO.Hazmatlist1(quotation.getQuoteId());
        for (Iterator iter = hazmatList.iterator(); iter.hasNext();) {
            HazmatMaterial hazmatMaterial = (HazmatMaterial) iter.next();
            if (hazmatMaterial.getDocTypeCode() != null && hazmatMaterial.getDocTypeCode().equals("Quote")) {
                HazmatMaterial haz = new HazmatMaterial();
                PropertyUtils.copyProperties(haz, hazmatMaterial);
                haz.setDocTypeCode("Booking");
                haz.setId(null);
                haz.setBolId(null);
                hazmatSet.add(haz);
            }
        }
        bookingFCL.setHazmatSet(hazmatSet);
        if (bkgAlert != null && bkgAlert.equals("newgetRatesBKG")) {
            bookingFCL.setSoc("N");
        } else if (bkgAlert != null && bkgAlert.equals("oldgetRatesBKG")) {
            bookingFCL.setSoc("Y");
        }
        if (quotation.getRatesNonRates() != null && quotation.getRatesNonRates().equals("N")) {
            bookingFCL.setSoc(null);
        }
        //This is for setting trucker information in booking, In case if quote contains door origin and what ever the
        //vendor for local drayage is there, it has to be set as a trucker in booking.
        if (null != quotation.getDoorOrigin() && !quotation.getDoorOrigin().equalsIgnoreCase("")) {
            bookingFCL = setTruckerInfo(quotation, bookingFCL);
        }
        bookingFCL.setOnCarriage(quotation.getOnCarriage());
        bookingFCL.setOnCarriageRemarks(quotation.getOnCarriageRemarks());
        bookingFCL.setBookingContact(quotation.getEmail1());

        bookingFclDAO.save(bookingFCL);
        List bookingFclRatesList = new ArrayList();
        String inlandVendor = "";
        if ((quotation.getRatesNonRates() != null && quotation.getRatesNonRates().equals("R")) || (quotation.getRatesNonRates() != null && quotation.getRatesNonRates().equals("N")
                && quotation.getBreakBulk() != null && quotation.getBreakBulk().equals("N"))) {
            List fclRates = (List) chargesDAO.getChargesforQuotation1(quotation.getQuoteId());
            for (Iterator iter = fclRates.iterator(); iter.hasNext();) {
                Charges charges = (Charges) iter.next();
                bookingfclUnits = new BookingfclUnits();

                bookingfclUnits.setAmount(charges.getAmount());
                bookingfclUnits.setMarkUp(charges.getMarkUp());
                bookingfclUnits.setFutureRate(charges.getFutureRate());
                bookingfclUnits.setSpotRateAmt(charges.getSpotRateAmt());
                bookingfclUnits.setSpotRateMarkUp(charges.getSpotRateMarkUp());
                bookingfclUnits.setSpotRateChk(charges.getSpotRateChk());
                if (charges.getUnitType() != null && !charges.getUnitType().equals("0")) {
                    GenericCode gen = genericCodeDAO.findById(Integer.parseInt(charges.getUnitType()));
                    bookingfclUnits.setUnitType(gen);
                }
                bookingfclUnits.setNumbers(charges.getNumber());
                if (quotation.getCommcode() != null) {
                    bookingfclUnits.setCommcode(quotation.getCommcode().getId());
                }
                if (charges.getChargeFlag() != null) {
                    if (charges.getChargeFlag().equals("M")) {
                        bookingfclUnits.setManualCharges(charges.getChargeFlag());
                        bookingfclUnits.setNewFlag("new");
                    } else if (charges.getChargeFlag().equals("F")) {
                        bookingfclUnits.setManualCharges(charges.getChargeFlag());
                        bookingfclUnits.setNewFlag("FF");
                    } else if (charges.getChargeFlag().equals("I")) {
                        bookingfclUnits.setManualCharges(charges.getChargeFlag());
                        bookingfclUnits.setNewFlag("IN");
                    } else if (charges.getChargeFlag().equals("D")) {
                        bookingfclUnits.setManualCharges("M");
                        bookingfclUnits.setNewFlag("D");
                    } else if (charges.getChargeFlag().equals("P")) {
                        bookingfclUnits.setManualCharges("M");
                        bookingfclUnits.setNewFlag("PP");
                    } else if (charges.getChargeFlag().equals("CH")) {
                        bookingfclUnits.setManualCharges("M");
                        bookingfclUnits.setNewFlag("new");
                    } 
                }
                bookingfclUnits.setCostType(charges.getCostType());
                bookingfclUnits.setChgCode(charges.getChgCode());
                bookingfclUnits.setChargeCodeDesc(charges.getChargeCodeDesc());
                bookingfclUnits.setCurrency(charges.getCurrecny());
                if (charges.getMarkUp() == null) {
                    charges.setMarkUp(0.00);
                }
                if (bookingFCL.getSpotRate().equals("Y")) {
                    double spotRate = null != charges.getSpotRateAmt() ? charges.getSpotRateAmt() : 0d;
                    double spotMark = null != charges.getSpotRateMarkUp() ? charges.getSpotRateMarkUp() : 0d;
                    bookingfclUnits.setSellRate(spotRate + spotMark);
                } else {
                    bookingfclUnits.setSellRate(charges.getAmount() + charges.getMarkUp());
                }
                bookingfclUnits.setBuyRate(0.00);
                bookingfclUnits.setProfit(bookingfclUnits.getSellRate());
                bookingfclUnits.setEfectiveDate(charges.getEfectiveDate());
                bookingfclUnits.setBookingNumber(String.valueOf(bookingFCL.getBookingId()));
                bookingfclUnits.setAccountName(charges.getAccountName());
                bookingfclUnits.setAccountNo(charges.getAccountNo());
                bookingfclUnits.setAdjustment(charges.getAdjestment());
                if (charges.getAdjustmentChargeComments() != null) {
                    bookingfclUnits.setAdjustmentChargeComments(charges.getAdjustmentChargeComments());
                }
                bookingfclUnits.setComment(CommonFunctions.isNotNull(charges.getComment())
                        ? charges.getComment().toUpperCase() : charges.getComment());

                bookingfclUnits.setPrint(charges.getPrint());
                bookingfclUnits.setVendorCheckBox(charges.getDefaultCarrier());
                bookingfclUnits.setSpecialEquipment(charges.getSpecialEquipment());
                bookingfclUnits.setSpecialEquipmentUnit(charges.getSpecialEquipmentUnit());
                bookingfclUnits.setStandardCharge(charges.getStandardCharge());
                bookingfclUnits.setOutOfGauge(charges.getOutOfGauge());
                bookingfclUnits.setOutOfGaugeComment(charges.getOutOfGaugeComment());

                bookingFclRatesList.add(bookingfclUnits);
                //bookingFclUnitsDAO.save(bookingfclUnits);
                //bookingFclSet.add(bookingfclUnits) ;
                if ("INLAND".equalsIgnoreCase(charges.getChgCode())) {
                    inlandVendor = charges.getAccountNo();
                }

            }
            EditBookingsForm NewBookingsForm = new EditBookingsForm();
            NewBookingsForm.setSelectedCheck(editQuotesForm.getSelectedCheck());
            NewBookingsForm.setSslDescription(editQuotesForm.getSslDescription());
            List newOtherChargesList = new ArrayList();

            List otherChargesList = chargesDAO.getChargesforQuotation8(quotation.getQuoteId());
            for (Iterator iter = otherChargesList.iterator(); iter.hasNext();) {
                Charges charges = (Charges) iter.next();
                bookingfclUnits = new BookingfclUnits();
                if (charges.getCostType().equalsIgnoreCase(messageResources.getMessage("per1000kg"))
                        || charges.getCostType().trim().equalsIgnoreCase(messageResources.getMessage("per2000lbs"))) {
                    bookingfclUnits.setChgCode(charges.getChgCode());
                    bookingfclUnits.setChargeCodeDesc(charges.getChargeCodeDesc());
                    bookingfclUnits.setCostType(charges.getCostType());
                    bookingfclUnits.setAmount(charges.getRetail());
                    bookingfclUnits.setMinimum(charges.getMinimum());
                    bookingfclUnits.setAccountName(charges.getAccountName());
                    bookingfclUnits.setAccountNo(charges.getAccountNo());
                    bookingfclUnits.setComment(CommonFunctions.isNotNull(charges.getComment())
                            ? charges.getComment().toUpperCase() : charges.getComment());
                    bookingfclUnits.setVendorCheckBox(charges.getDefaultCarrier());
                    if (charges.getChargeFlag() != null) {
                        bookingfclUnits.setManualCharges(charges.getChargeFlag());
                        bookingfclUnits.setNewFlag("new");
                    }
                    if (charges.getCurrecny() != null) {
                        bookingfclUnits.setCurrency(charges.getCurrecny());
                    }
                    bookingfclUnits.setPrint(charges.getPrint());
                    bookingfclUnits.setBookingNumber(String.valueOf(bookingFCL.getBookingId()));
                    bookingFclUnitsDAO.save(bookingfclUnits);
                    //bookingFclSet.add(bookingfclUnits) ;
                } else {

                    bookingfclUnits.setAmount(charges.getAmount());
                    if (quotation.getCommcode() != null) {
                        bookingfclUnits.setCommcode(quotation.getCommcode().getId());
                    }
                    bookingfclUnits.setCostType(charges.getCostType());
                    if (charges.getChargeFlag() != null) {
                        bookingfclUnits.setManualCharges(charges.getChargeFlag());
                        bookingfclUnits.setNewFlag("new");
                    }
                    bookingfclUnits.setChgCode(charges.getChgCode());
                    bookingfclUnits.setChargeCodeDesc(charges.getChargeCodeDesc());
                    if (charges.getCurrecny() != null) {
                        bookingfclUnits.setCurrency(charges.getCurrecny());
                    }
                    bookingfclUnits.setMarkUp(charges.getMarkUp());
                    if (bookingFCL.getSpotRate().equals("Y")) {
                        double spotRate = null != charges.getSpotRateAmt() ? charges.getSpotRateAmt() : 0d;
                        double spotMark = null != charges.getSpotRateMarkUp() ? charges.getSpotRateMarkUp() : 0d;
                        bookingfclUnits.setSellRate(spotRate + spotMark);
                    } else {
                        bookingfclUnits.setSellRate(charges.getAmount() + charges.getMarkUp());
                    }
                    bookingfclUnits.setBuyRate(0.00);
                    bookingfclUnits.setProfit(0.00);
                    bookingfclUnits.setAccountName(charges.getAccountName());
                    bookingfclUnits.setAccountNo(charges.getAccountNo());
                    bookingfclUnits.setFutureRate(charges.getFutureRate());
                    bookingfclUnits.setEfectiveDate(charges.getEfectiveDate());
                    bookingfclUnits.setComment(CommonFunctions.isNotNull(charges.getComment())
                            ? charges.getComment().toUpperCase() : charges.getComment());

                    bookingfclUnits.setPrint(charges.getOtherprint());
                    bookingfclUnits.setStandardCharge(charges.getStandardCharge());
                    bookingfclUnits.setVendorCheckBox(charges.getDefaultCarrier());
                    newOtherChargesList.add(bookingfclUnits);
                }
                if ("INLAND".equalsIgnoreCase(charges.getChgCode())) {
                    inlandVendor = charges.getAccountNo();
                }
            }
            if (editQuotesForm.getButtonValue().equals("converttobooknew")) {
                getRates(NewBookingsForm, bookingFCL, messageResources, bookingFclRatesList, newOtherChargesList);
            } else {
                for (Iterator iterator = bookingFclRatesList.iterator(); iterator.hasNext();) {
                    BookingfclUnits bookingfclUnits = (BookingfclUnits) iterator.next();
                    bookingfclUnits.setBookingNumber(bookingFCL.getBookingId().toString());
                    bookingFclUnitsDAO.update(bookingfclUnits);
                }
                for (Iterator iterator = newOtherChargesList.iterator(); iterator.hasNext();) {
                    BookingfclUnits bookingfclUnits = (BookingfclUnits) iterator.next();
                    bookingfclUnits.setBookingNumber(bookingFCL.getBookingId().toString());
                    bookingFclUnitsDAO.update(bookingfclUnits);

                }
            }
            bookingFCL.setBookingFclUnit(bookingFclSet);
        } else {
            List chargesList = chargesDAO.getQuoteId(quotation.getQuoteId());
            for (Iterator iterator = chargesList.iterator(); iterator.hasNext();) {
                Charges charges = (Charges) iterator.next();
                bookingfclUnits = new BookingfclUnits();

                bookingfclUnits.setAmount(charges.getAmount());
                bookingfclUnits.setMarkUp(charges.getMarkUp());
                bookingfclUnits.setFutureRate(charges.getFutureRate());
                /*if(charges.getUnitType()!=null && !charges.getUnitType().equals("0")){
                GenericCode gen=genericCodeDAO.findById(Integer.parseInt(charges.getUnitType()));
                bookingfclUnits.setUnitType(gen);
                }*/
                bookingfclUnits.setNumbers(charges.getNumber());
                if (quotation.getCommcode() != null) {
                    bookingfclUnits.setCommcode(quotation.getCommcode().getId());
                }
                if (charges.getChargeFlag() != null) {
                    bookingfclUnits.setManualCharges(charges.getChargeFlag());
                    bookingfclUnits.setNewFlag("new");
                }
                bookingfclUnits.setCostType(charges.getCostType());
                bookingfclUnits.setChgCode(charges.getChgCode());
                bookingfclUnits.setChargeCodeDesc(charges.getChargeCodeDesc());
                bookingfclUnits.setCurrency(charges.getCurrecny());
                if (charges.getMarkUp() == null) {
                    charges.setMarkUp(0.00);
                }
                if (charges.getAmount() == null) {
                    charges.setAmount(0.00);
                }
                if (bookingFCL.getSpotRate().equals("Y")) {
                    double spotRate = null != charges.getSpotRateAmt() ? charges.getSpotRateAmt() : 0d;
                    double spotMark = null != charges.getSpotRateMarkUp() ? charges.getSpotRateMarkUp() : 0d;
                    bookingfclUnits.setSellRate(spotRate + spotMark);
                } else {
                    bookingfclUnits.setSellRate(charges.getAmount() + charges.getMarkUp());
                }
                bookingfclUnits.setBuyRate(0.00);
                bookingfclUnits.setProfit(bookingfclUnits.getSellRate());
                bookingfclUnits.setEfectiveDate(charges.getEfectiveDate());
                bookingfclUnits.setBookingNumber(String.valueOf(bookingFCL.getBookingId()));
                bookingfclUnits.setAccountName(charges.getAccountName());
                bookingfclUnits.setAccountNo(charges.getAccountNo());
                bookingfclUnits.setAdjustment(charges.getAdjestment());
                if (charges.getAdjustmentChargeComments() != null) {
                    bookingfclUnits.setAdjustmentChargeComments(charges.getAdjustmentChargeComments());
                }
                bookingfclUnits.setComment(CommonFunctions.isNotNull(charges.getComment())
                        ? charges.getComment().toUpperCase() : charges.getComment());
                bookingfclUnits.setVendorCheckBox(charges.getDefaultCarrier());
                bookingfclUnits.setBookingNumber(bookingFCL.getBookingId().toString());
                bookingfclUnits.setPrint(charges.getPrint());
                bookingfclUnits.setSpecialEquipment(charges.getSpecialEquipment());
                bookingfclUnits.setSpecialEquipmentUnit(charges.getSpecialEquipmentUnit());
                bookingfclUnits.setOutOfGauge(charges.getOutOfGauge());
                bookingfclUnits.setStandardCharge(charges.getStandardCharge());
                bookingfclUnits.setOutOfGaugeComment(charges.getOutOfGaugeComment());
                bookingFclUnitsDAO.save(bookingfclUnits);
                if ("INLAND".equalsIgnoreCase(charges.getChgCode())) {
                    inlandVendor = charges.getAccountNo();
                }
            }
        }
        if (CommonUtils.isNotEmpty(inlandVendor)) {
            CustAddress custAddress = new BookingDwrBC().getCustAddressForNo(inlandVendor);
            if (null != custAddress) {
                bookingFCL.setTruckerCity(custAddress.getCity1());
                bookingFCL.setName(custAddress.getAcctName());
                bookingFCL.setTruckerCode(custAddress.getAcctNo());
                bookingFCL.setTruckerPhone(custAddress.getPhone());
                bookingFCL.setTruckerEmail(custAddress.getEmail1());
                bookingFCL.setTruckerZip(custAddress.getZip());
                bookingFCL.setTruckerState(custAddress.getState());
                bookingFCL.setAddress(custAddress.getAddress1());
            }
        }
        quotation.setBookingNo(bookingFCL.getBookingId());
        quotationDAO.save(quotation);
        Notes notes = new Notes();
        notes.setModuleId("FILE");
        notes.setUpdateDate(new Date());
        notes.setNoteTpye("auto");
        notes.setNoteDesc("Quotes is converted to Booking");
        notes.setUpdatedBy(bookingFCL.getBookedBy());
        notes.setModuleRefId(bookingFCL.getFileNo());
        notesDAO.save(notes);
        msg = "This Quote is Converted into Booking";
        return msg;
    }

    /**
     * ---Method for Searching for Files through file search page-----
     *
     * @param searchQuotationform
     * @return
     */
    public List getSearchListByFileNumber(SearchQuotationForm searchQuotationform, boolean importFlag, String agentQuery) throws Exception {
        FileNumberForQuotaionBLBooking fileNumberForQuotaionBLBooking = new FileNumberForQuotaionBLBooking();
        Map getMap = fileNumberForQuotaionBLBooking.getSearchProperty(searchQuotationform);
        String filterBy = searchQuotationform.getFilerBy();
        String sortByDate = searchQuotationform.getSortByDate();
        return quotationDAO.getSearchListByFileNumber(getMap, filterBy, sortByDate, searchQuotationform.getLimit(), importFlag, agentQuery);
    }

    public Quotation calculateInsurance(Quotation quotation, String insuranceAmount) throws Exception {
        DecimalFormat formatter = new DecimalFormat("#0.00");
        Double a = quotation.getCostofgoods();
        if (quotation.getInsuranceCharge() == null || quotation.getInsuranceCharge().equals("")) {
            quotation.setInsuranceCharge(0.00);
        }
        if (insuranceAmount == null || insuranceAmount.equals("") || insuranceAmount.equals("0.0")) {
            insuranceAmount = "0.80";
        }
        Double insureAmt = Double.parseDouble(dbUtil.removeComma(insuranceAmount));
        if (quotation.getTotalCharges() == null || quotation.getTotalCharges().equals("")) {
            quotation.setTotalCharges(0.00);
        }
        //b->total of all charges for each container
        //a-> cost of goods
        //insuranceAmount->by default 0.80, or else user can enter
        Double b = quotation.getTotalCharges();
        Double c = ((a + b) * insureAmt) / 100;
        Double d = ((a + b + c) * 10) / 100;
        Double cif = a + b + c + d;
        Double insuranceCharge = Double.parseDouble(formatter.format((cif * insureAmt) / 100));
        //insuranceCharge is the final insurance amount
        quotation.setInsuranceCharge(insuranceCharge);
        quotation.setTotalCharges(b + insuranceCharge);
        return quotation;
    }

    public List setMarkUp(List fclRates, SearchQuotationForm searchQuotationform, MessageResources messageResources, String userName) throws Exception {
        Map hashMap = new HashMap();
        for (Iterator iterator = fclRates.iterator(); iterator.hasNext();) {
            Charges charges = (Charges) iterator.next();
            if (charges.getUnitName() == null) {
                charges.setUnitName("");
            }
            if (null == charges.getSpecialEquipmentUnit()) {
                charges.setSpecialEquipmentUnit("");
            }
            if (null == charges.getStandardCharge()) {
                charges.setStandardCharge("");
            }
            hashMap.put(charges.getChgCode() + "-" + charges.getUnitName() + "-" + charges.getSpecialEquipmentUnit() + "-" + charges.getStandardCharge(), charges);
        }
        if (searchQuotationform.getRateId() != null && searchQuotationform.getRateId().equalsIgnoreCase("expand")) {
            if (searchQuotationform.getChargeMarkUp() != null) {
                for (int i = 0; i < searchQuotationform.getChargeMarkUp().length; i++) {
                    String specialEquipment = "";
                    String standardCharge = "";
                    if (null != searchQuotationform.getSplEqpUnits()[i]) {
                        specialEquipment = searchQuotationform.getSplEqpUnits()[i];
                    }
                    if (null != searchQuotationform.getStandardCharge()[i]) {
                        standardCharge = searchQuotationform.getStandardCharge()[i];
                    }
                    if (hashMap.containsKey(searchQuotationform.getChargeCodes()[i] + "-" + searchQuotationform.getUnitType()[i] + "-" + specialEquipment + "-" + standardCharge)) {
                        Charges charges = (Charges) hashMap.get(searchQuotationform.getChargeCodes()[i] + "-" + searchQuotationform.getUnitType()[i] + "-" + specialEquipment + "-" + standardCharge);
                        Date date = null;
                        date = sdf.parse(searchQuotationform.getEffectiveDate()[i]);
                        charges.setEfectiveDate(date);
                        if (charges.getChargeFlag() != null && ("M".equals(charges.getChargeFlag()) || "I".equals(charges.getChargeFlag()))) {
                            charges.setMarkUp(Double.parseDouble(dbUtil.removeComma(searchQuotationform.getTotal()[i])));
                        } else {
                            if (charges.getChargeFlag() != null && ("F".equals(charges.getChargeFlag()))) {
                                charges.setAmount(Double.parseDouble(dbUtil.removeComma(searchQuotationform.getChargeMarkUp()[i])));
                                charges.setMarkUp(Double.parseDouble(dbUtil.removeComma(searchQuotationform.getChargeAmount()[i])));
                            } else {
                                charges.setMarkUp(Double.parseDouble(dbUtil.removeComma(searchQuotationform.getChargeMarkUp()[i])));
                            }
                        }
                        String buttonValue = searchQuotationform.getButtonValue();
                        if (searchQuotationform.getAdjestment().length > i) {
                            if (!("adjustmentChargeComments".equals(buttonValue)) && null != searchQuotationform.getAdjustmentChargeComments()
                                    && !searchQuotationform.getAdjustmentChargeComments().equals("")) {
                                charges.setAdjestment(Double.parseDouble(dbUtil.removeComma(searchQuotationform.getAdjestment()[i])));
                            }
                            charges.setUpdateBy(userName);
                            charges.setUpdateOn(new Date());
                        }
                    }
                }
            }
        } else if (searchQuotationform.getRateId() != null
                && searchQuotationform.getRateId().equalsIgnoreCase("collapse")) {

            if (searchQuotationform.getHiddenchargeMarkUp() != null) {
                for (int i = 0; i < searchQuotationform.getHiddenchargeMarkUp().length; i++) {
                    String specialEquipment = "";
                    String standardCharge = "";
                    if (null != searchQuotationform.getSplEqpUnitsCollapse()[i]) {
                        specialEquipment = searchQuotationform.getSplEqpUnitsCollapse()[i];
                    }
                    if (null != searchQuotationform.getStandardChargeCollapse()[i]) {
                        standardCharge = searchQuotationform.getStandardChargeCollapse()[i];
                    }
                    if (hashMap.containsKey(searchQuotationform.getHiddenchargeCodes()[i] + "-"
                            + searchQuotationform.getHiddenunitType()[i] + "-" + specialEquipment + "-" + standardCharge)) {
                        Charges charges = (Charges) hashMap.get(searchQuotationform.getHiddenchargeCodes()[i] + "-" + searchQuotationform.getHiddenunitType()[i] + "-" + specialEquipment + "-" + standardCharge);
                        Date date = null;
                        date = sdf.parse(searchQuotationform.getEffectiveDate()[i]);
                        charges.setEfectiveDate(date);
                        charges.setMarkUp(Double.parseDouble(dbUtil.removeComma(searchQuotationform.getHiddenchargeMarkUp()[i])));
                        String buttonValue = searchQuotationform.getButtonValue();
                        if (searchQuotationform.getAdjestmentforCollapse().length > i) {
                            if (!("adjustmentChargeComments".equals(buttonValue)) && null != searchQuotationform.getAdjustmentChargeComments()
                                    && !searchQuotationform.getAdjustmentChargeComments().equals("")) {
                                charges.setAdjestment(Double.parseDouble(dbUtil.removeComma(searchQuotationform.getAdjestmentforCollapse()[i])));
                            }
                            charges.setUpdateBy(userName);
                            charges.setUpdateOn(new Date());
                        }
                    }
                }
            }
        }
        Set hashSet = new HashSet();
        hashSet = hashMap.keySet();
        HashMap tempHashMap = new HashMap();
        List newList = new ArrayList();
        for (Iterator iterator = hashSet.iterator(); iterator.hasNext();) {
            String key = (String) iterator.next();
            Charges charges = (Charges) hashMap.get(key);
            if (null == charges.getSpecialEquipmentUnit()) {
                charges.setSpecialEquipmentUnit("");
            }
            if (null == charges.getStandardCharge()) {
                charges.setStandardCharge("");
            }
            newList.add(charges.getUnitType() + charges.getId().toString() + charges.getSpecialEquipmentUnit() + charges.getStandardCharge());
            tempHashMap.put(charges.getUnitType() + charges.getId().toString() + charges.getSpecialEquipmentUnit() + charges.getStandardCharge(), charges);
        }
        Collections.sort(newList);
        List tempList = new ArrayList();
        for (Iterator iterator = newList.iterator(); iterator.hasNext();) {
            String key = (String) iterator.next();
            tempList.add(tempHashMap.get(key));
        }
        return tempList;
    }

    public List serMarkUpValuesforQuotesForm(List fclRates, QuotesForm quotesForm, MessageResources messageResources) throws Exception {
        for (int i = 0; i < fclRates.size(); i++) {
            Charges costBean = (Charges) fclRates.get(i);
            if (quotesForm.getEffectiveDate() != null && quotesForm.getEffectiveDate().length > i && quotesForm.getEffectiveDate()[i] != null) {
                Date date = null;
                date = sdf.parse(quotesForm.getEffectiveDate()[i]);
                costBean.setEfectiveDate(date);
            }
            if (quotesForm.getChargeMarkUp() != null && quotesForm.getChargeMarkUp().length > i && quotesForm.getChargeMarkUp()[i] != null) {
                costBean.setMarkUp(Double.parseDouble(dbUtil.removeComma(quotesForm.getChargeMarkUp()[i])));
            }
        }
        return fclRates;
    }

    public void deleteCharge(String index) throws Exception {
        ChargesDAO chargeDAO = new ChargesDAO();
        StringBuilder message = new StringBuilder();
        if (index != null && !index.equals("")) {
            Charges charge = chargeDAO.findById(new Integer(index));
            if (null != charge) {
                message.append("DElETED ->Charge Code - ").append(charge.getChargeCodeDesc()).append(" Cost Type - ").append(charge.getCostType()).append(" Cost - ");
                message.append(charge.getAmount()).append(" Sell -").append(charge.getMarkUp()).append(" Currency - ").append(charge.getCurrecny()).append(" Vendor Name - ").append(charge.getAccountName()).append(" Vendor Number -").append(charge.getAccountNo()).append(" Comment -").append(charge.getComment());
                if (charge.getUnitType() != null && null != new GenericCodeDAO().findById(Integer.parseInt(charge.getUnitType()))) {
                    message.append("Unit Type -").append(new GenericCodeDAO().findById(Integer.parseInt(charge.getUnitType())).getCodedesc());
                }
                Notes note = new Notes();
                NotesBC notesBC = new NotesBC();
                note.setModuleId(NotesConstants.FILE);
                String fileNo = "";
                if (null != charge.getQouteId()) {
                    String quoteId = charge.getQouteId().toString();
                    fileNo = new QuotationDAO().findFileNo(quoteId);
                }
                note.setModuleRefId(fileNo);
                note.setUpdateDate(new Date());
                note.setNoteType(NotesConstants.AUTO);
                note.setNoteDesc(message.toString());
                notesBC.saveNotes(note);
                chargeDAO.delete(charge);
            }
        }
    }

    public List addLocalDrayageToBl(List ratesList, Quotation quotation, MessageResources messageResources) throws Exception {
        List chargesList = new ArrayList();
        Charges costBean = new Charges();
        costBean.setCostType("Flat Rate Per Container");
        costBean.setChgCode("Drayage");
        costBean.setChargeCodeDesc("DRAY");
        costBean.setRetail(quotation.getAmount());
        costBean.setMarkUp(quotation.getDrayageMarkUp());
        costBean.setEfectiveDate(new Date());
        costBean.setCurrecny("USD");
        costBean.setPrint("off");
        costBean.setUnitType("0.00");
        costBean.setAccountName(quotation.getSslname());
        costBean.setAccountNo(quotation.getSsline());
        costBean.setChargeFlag("M");
        boolean flag = false;
        List newList = new ArrayList(ratesList);
        for (int i = 0; i < ratesList.size(); i++) {
            Charges c1 = (Charges) ratesList.get(i);
            if (c1.getChgCode().equalsIgnoreCase("Drayage")) {
                flag = true;
                quotation.setAmount(c1.getAmount());
                quotation.setDrayageMarkUp(c1.getMarkUp());
                //costBean.setRetail(c1.getAmount()*Double.parseDouble(c1.getNumber()));
                //costBean.setMarkUp(c1.getMarkUp()*Double.parseDouble(c1.getNumber()));
                newList.remove(c1);
            } else if (c1.getChgCode().equalsIgnoreCase("Intermodal")) {
                flag = true;
                quotation.setAmount1(c1.getAmount());
                quotation.setIntermodalMarkUp(c1.getMarkUp());
                newList.remove(c1);
            } else if (c1.getChgCode().equalsIgnoreCase("FF COMMISSION")) {
                newList.remove(c1);
            } else if (c1.getChgCode().equalsIgnoreCase("INSURANCE")) {
                newList.remove(c1);
            }
        }
        if (flag) {
            for (int a = 0; a < newList.size(); a++) {
                Charges c3 = (Charges) newList.get(a);
                Charges cbeanPrev = null;
                if (a != 0) {
                    cbeanPrev = (Charges) newList.get(a - 1);
                    if (cbeanPrev.getUnitType() != null && c3.getUnitType() != null
                            && !cbeanPrev.getUnitType().equals("0") && !c3.getUnitType().equals("0")
                            && !cbeanPrev.getUnitType().equals(c3.getUnitType())) {
                        Charges c2 = new Charges();
                        if (costBean.getUnitType().equals("0.00")) {
                            PropertyUtils.copyProperties(c2, costBean);
                            c2.setNumber(cbeanPrev.getNumber());
                            c2.setInclude("on");
                            c2.setPrint("off");
                            c2.setAmount(quotation.getAmount() * Double.parseDouble(c2.getNumber()));
                            c2.setUnitType(cbeanPrev.getUnitType());
                            c2.setMarkUp(quotation.getDrayageMarkUp() * Double.parseDouble(c2.getNumber()));
                            chargesList.add(c2);
                        }
                    }
                }
                chargesList.add(c3);

            }
            if (chargesList.size() > 0) {
                Charges c4 = (Charges) chargesList.get(chargesList.size() - 1);
                Charges c2 = new Charges();
                if (costBean.getUnitType().equals("0.00")) {
                    PropertyUtils.copyProperties(c2, costBean);
                    c2.setNumber(c4.getNumber());
                    c2.setInclude("on");
                    c2.setPrint("off");
                    c2.setAmount(quotation.getAmount() * Double.parseDouble(c2.getNumber()));
                    c2.setUnitType(c4.getUnitType());
                    c2.setMarkUp(quotation.getDrayageMarkUp() * Double.parseDouble(c2.getNumber()));
                    chargesList.add(c2);
                }
            }
        }
        if (!flag) {
            return ratesList;
        } else {
            return chargesList;
        }
    }

    public List addaddLocalDrayageToBlManually(List ratesList, Quotation quotation, String unitType, MessageResources messageResources, List fclRatesList1) throws Exception {
        Charges costBean = new Charges();
        costBean.setCostType("Flat Rate Per Container");
        costBean.setChgCode("Percent Of Drayage");
        costBean.setChargeCodeDesc("DRAY");
        if (quotation.getAmount() == null || quotation.getAmount().equals("")) {
            quotation.setAmount(0.00);
        }
        costBean.setAmount(quotation.getAmount());
        costBean.setMarkUp(quotation.getMarkUp());
        costBean.setEfectiveDate(new Date());
        costBean.setCurrecny("USD");
        costBean.setPrint("off");
        costBean.setUnitType(unitType);
        costBean.setAccountName(quotation.getSslname());
        costBean.setAccountNo(quotation.getSsline());
        costBean.setNumber("1");
        boolean flag = false;
        for (Iterator iter = ratesList.iterator(); iter.hasNext();) {
            Charges tempCostBean = (Charges) iter.next();
            if (tempCostBean.getChgCode().equals(costBean.getChgCode()) && (tempCostBean.getUnitType().equals(costBean.getUnitType()))) {
                flag = true;
                break;
            }
        }
        if (!flag && quotation.getLocaldryage() != null && quotation.getLocaldryage().equals("Y")) {
            fclRatesList1.add(costBean);
        }
        return fclRatesList1;
    }

    public List addIntermodelToBl(List otherChargesList, Quotation quotation, MessageResources messageResources) throws Exception {
        List chargesList = new ArrayList();
        Charges costBean = new Charges();
        costBean.setCostType("Flat Rate Per Container");
        costBean.setChgCode("Intermodal");
        costBean.setChargeCodeDesc("INTMDL");
        if (quotation.getAmount1() == null || quotation.getAmount1().equals("")) {
            quotation.setAmount1(0.00);
        }
        costBean.setRetail(quotation.getAmount1());
        costBean.setMarkUp(quotation.getIntermodalMarkUp());
        costBean.setEfectiveDate(new Date());
        costBean.setCurrecny("USD");
        costBean.setPrint("off");
        costBean.setUnitType("0.00");
        costBean.setChargeFlag("M");
        costBean.setAccountName(quotation.getSslname());
        costBean.setAccountNo(quotation.getSsline());
        boolean flag = false;
        List newList = new ArrayList(otherChargesList);

        for (int a = 0; a < newList.size(); a++) {
            Charges c3 = (Charges) newList.get(a);
            Charges cbeanPrev = null;
            if (a != 0) {
                cbeanPrev = (Charges) newList.get(a - 1);
                if (cbeanPrev.getUnitType() != null && c3.getUnitType() != null
                        && !cbeanPrev.getUnitType().equals("0") && !c3.getUnitType().equals("0") && !cbeanPrev.getUnitType().equals(c3.getUnitType())) {
                    Charges c2 = new Charges();
                    if (costBean.getUnitType().equals("0.00")) {
                        PropertyUtils.copyProperties(c2, costBean);
                        c2.setNumber(cbeanPrev.getNumber());
                        c2.setInclude("off");
                        c2.setPrint("off");
                        c2.setUnitType(cbeanPrev.getUnitType());
                        c2.setAmount(c2.getRetail() * Double.parseDouble(c2.getNumber()));
                        c2.setMarkUp(c2.getMarkUp() * Double.parseDouble(c2.getNumber()));
                        flag = true;
                        chargesList.add(c2);
                    }
                }
            }
            chargesList.add(c3);

        }
        if (chargesList.size() > 0) {
            Charges c4 = (Charges) chargesList.get(chargesList.size() - 1);

            Charges c2 = new Charges();
            if (costBean.getUnitType().equals("0.00")) {
                PropertyUtils.copyProperties(c2, costBean);
                c2.setNumber(c4.getNumber());
                c2.setInclude("on");
                c2.setPrint("off");
                c2.setUnitType(c4.getUnitType());
                c2.setAmount(c2.getRetail() * Double.parseDouble(c2.getNumber()));
                c2.setMarkUp(c2.getMarkUp() * Double.parseDouble(c2.getNumber()));
                flag = true;
                chargesList.add(c2);
            }
        }

        if (!flag) {
            return otherChargesList;
        } else {
            return chargesList;
        }
    }

    public List addDocChargeToBl(List otherChargesList, Quotation quotation, String amount) throws Exception {
        Charges costBean = new Charges();
        costBean.setCostType("PER BL CHARGES");
        costBean.setChgCode("DOCUMENT CHARGE");
        costBean.setChargeCodeDesc("DOCUM");
        costBean.setMarkUp(Double.parseDouble(dbUtil.removeComma(amount)));
        costBean.setEfectiveDate(new Date());
        costBean.setCurrecny("USD");
        costBean.setPrint("off");
        costBean.setUnitType(null);
        costBean.setChargeFlag("D");
        costBean.setInclude("off");
        costBean.setPrint("off");
        costBean.setAccountName("");
        costBean.setAccountNo("");
        List newList = new ArrayList(otherChargesList);
        for (Object otherChargesList1 : otherChargesList) {
            Charges c1 = (Charges) otherChargesList1;
            if (c1.getChgCode().equalsIgnoreCase(costBean.getChgCode())) {
                newList.remove(c1);
            }
        }
        newList.add(costBean);
        return newList;
    }

    // Flat Rate Per Container
    public List addPierPassChargeToBl(List fclRates, Quotation quotation) throws Exception {

        List chargesList = new ArrayList();
        int no = 0;
        for (int a = 0; a < fclRates.size(); a++) {
            Charges chargeDomain = (Charges) fclRates.get(a);
            Charges cbeanPrev = null;
            if (a != 0) {
                cbeanPrev = (Charges) fclRates.get(a - 1);
                if (null != chargeDomain && null != cbeanPrev && !chargeDomain.getUnitType().equals(cbeanPrev.getUnitType())) {
                    if (cbeanPrev.getNumber() != null) {
                        no = no + Integer.parseInt(cbeanPrev.getNumber());
                    }
                }
            }
        }
        if (fclRates.size() > 0) {
            Charges c1 = (Charges) fclRates.get(fclRates.size() - 1);
            if (c1.getNumber() != null) {
                no = no + Integer.parseInt(c1.getNumber());
            }
        }

        Charges chargeObject = new Charges();
        chargeObject.setCostType("Flat Rate Per Container");
        chargeObject.setChgCode("PIER PASS");
        chargeObject.setChargeCodeDesc("PIERPA");
        chargeObject.setMarkUp(0.00);
        chargeObject.setEfectiveDate(new Date());
        chargeObject.setCurrecny("USD");
        chargeObject.setPrint("off");
        chargeObject.setUnitType("0.00");
        chargeObject.setChargeFlag("P");
        chargeObject.setInclude("off");
        chargeObject.setPrint("off");
        chargeObject.setAccountName(new PropertyDAO().getProperty("pier.pass.vendor"));
        chargeObject.setAccountNo(new PropertyDAO().getProperty("pier.pass.vendor"));
        chargeObject.setAmount(Double.parseDouble(new PropertyDAO().getProperty("pier.pass.amount")));

//        Charges chargeObject = new Charges();
//        chargeObject.setCostType("Flat Rate Per Container");
//        chargeObject.setChgCode("INSURANCE");
//        chargeObject.setChargeCodeDesc("INSURE");
//        chargeObject.setRetail(quotation.getInsuranceCharge());
//        chargeObject.setEfectiveDate(new Date());
//        chargeObject.setCurrecny("USD");
//        chargeObject.setPrint("off");
//        chargeObject.setUnitType("0.00");
//        chargeObject.setAccountName(quotation.getSslname());
//        chargeObject.setAccountNo(quotation.getSsline());
//        chargeObject.setChargeFlag("I");
        boolean flag = false;
        for (int i = 0; i < fclRates.size(); i++) {
            Charges c1 = (Charges) fclRates.get(i);
            if (c1.getChgCode().equalsIgnoreCase(chargeObject.getChgCode())) {
                c1.setRetail(chargeObject.getRetail());
                c1.setMarkUp(c1.getRetail());
            }
        }
        List tempList = new ArrayList(fclRates);
        Map<String, String> commentMap = new HashMap<String, String>();
        for (int i = 0; i < tempList.size(); i++) {
            Charges c1 = (Charges) tempList.get(i);
            if (c1.getChgCode().equalsIgnoreCase(chargeObject.getChgCode())) {
                if (CommonUtils.isNotEmpty(c1.getUnitType()) && CommonUtils.isNotEmpty(c1.getComment())) {
                    commentMap.put(c1.getUnitType(), c1.getComment());
                }
//                chargeObject.setComment(c1.getComment());
                fclRates.remove(c1);
                flag = true;
            }
        }
        fclRates = orderExpandList(fclRates);
        Double totalCharges = 0.00;
        String prevUnitValue = "";
        String unitValue = "";
        for (int a = 0; a < fclRates.size(); a++) {
            Charges chargeDom = (Charges) fclRates.get(a);
            if (null == chargeDom.getSpecialEquipmentUnit()) {
                chargeDom.setSpecialEquipmentUnit("");
            }
            if (CommonUtils.isNotEmpty(chargeDom.getUnitType())) {
                unitValue = chargeDom.getUnitType() + "-" + chargeDom.getSpecialEquipmentUnit() + "-" + chargeDom.getStandardCharge();
            }
            Charges cbeanPrev = null;
            if (a != 0) {
                cbeanPrev = (Charges) fclRates.get(a - 1);
                if (null == cbeanPrev.getSpecialEquipmentUnit()) {
                    cbeanPrev.setSpecialEquipmentUnit("");
                }
                if (CommonUtils.isNotEmpty(cbeanPrev.getUnitType())) {
                    prevUnitValue = cbeanPrev.getUnitType() + "-" + cbeanPrev.getSpecialEquipmentUnit() + "-" + cbeanPrev.getStandardCharge();
                }
                if (CommonUtils.isNotEmpty(prevUnitValue) && CommonUtils.isNotEmpty(unitValue) && !prevUnitValue.equals(unitValue)) {
                    Charges c2 = new Charges();
                    if (chargeObject.getUnitType().equals("0.00")) {
                        PropertyUtils.copyProperties(c2, chargeObject);
                        c2.setNumber(cbeanPrev.getNumber());
                        c2.setComment(commentMap.get(cbeanPrev.getUnitType()));
                        c2.setSpecialEquipmentUnit(cbeanPrev.getSpecialEquipmentUnit());
                        c2.setStandardCharge(cbeanPrev.getStandardCharge());
                        c2.setSpecialEquipment(cbeanPrev.getSpecialEquipment());
                        c2.setInclude("on");
                        c2.setPrint("off");
                        if ("M".equalsIgnoreCase(cbeanPrev.getChargeFlag())) {
                            totalCharges += cbeanPrev.getMarkUp() + cbeanPrev.getAdjestment();
                        } else {
                            if (quotation.getSpotRate().equals("Y")) {
                                totalCharges += cbeanPrev.getSpotRateAmt() + cbeanPrev.getMarkUp() + cbeanPrev.getAdjestment();
                            } else {
                                totalCharges += cbeanPrev.getAmount() + cbeanPrev.getMarkUp() + cbeanPrev.getAdjestment();
                            }
                        }
                        c2.setUnitType(cbeanPrev.getUnitType());
                        GenericCode genericCode = genericCodeDAO.findById(Integer.parseInt(c2.getUnitType()));
                        if (genericCode != null) {
                            c2.setUnitName(genericCode.getCodedesc());
                        }
                        quotation.setTotalCharges(totalCharges);
//                        quotation = calculateInsurance(quotation, quotation.getInsurancamt().toString());
                        c2.setAmount(chargeObject.getAmount());
                        c2.setMarkUp(chargeObject.getMarkUp());
                        if (!"A=20".equalsIgnoreCase(c2.getUnitName())) {
                            c2.setAmount(c2.getAmount() * 2.00);
                            c2.setMarkUp(c2.getMarkUp() * 2.00);
                        }
                        chargesList.add(c2);
                        totalCharges = 0.00;
                    }
                } else {
                    if ("M".equalsIgnoreCase(cbeanPrev.getChargeFlag())) {
                        totalCharges += cbeanPrev.getMarkUp() + cbeanPrev.getAdjestment();
                    } else {
                        if (quotation.getSpotRate().equals("Y")) {
                            totalCharges += cbeanPrev.getSpotRateAmt() + cbeanPrev.getMarkUp() + cbeanPrev.getAdjestment();
                        } else {
                            totalCharges += cbeanPrev.getAmount() + cbeanPrev.getMarkUp() + cbeanPrev.getAdjestment();
                        }
                    }
                }
            }
            chargesList.add(chargeDom);
        }
        if (chargesList.size() > 0) {
            Charges c4 = (Charges) chargesList.get(chargesList.size() - 1);
            Charges c2 = new Charges();
            if (chargeObject.getUnitType().equals("0.00")) {
                PropertyUtils.copyProperties(c2, chargeObject);
                if ("M".equalsIgnoreCase(c4.getChargeFlag())) {
                    totalCharges += c4.getMarkUp() + c4.getAdjestment();
                } else {
                    if (quotation.getSpotRate().equals("Y")) {
                        totalCharges += c4.getSpotRateAmt() + c4.getMarkUp() + c4.getAdjestment();
                    } else {
                        totalCharges += c4.getMarkUp() + c4.getAdjestment();
                    }
                }
                c2.setSpecialEquipmentUnit(c4.getSpecialEquipmentUnit());
                c2.setStandardCharge(c4.getStandardCharge());
                c2.setSpecialEquipment(c4.getSpecialEquipment());
                c2.setNumber(c4.getNumber());
                c2.setInclude("on");
                c2.setPrint("off");
                c2.setUnitType(c4.getUnitType());
                c2.setComment(commentMap.get(c4.getUnitType()));
                quotation.setTotalCharges(totalCharges);
                //c2.setRetail(c2.getRetail()*Double.parseDouble(c2.getNumber())/Double.parseDouble(String.valueOf(no)));
                quotation = calculateInsurance(quotation, quotation.getInsurancamt().toString());
                //c2.setRetail(c2.getRetail());
                c2.setAmount(chargeObject.getAmount());
                // c2.setMarkUp(chargeObject.getAmount());
                if (!"A=20".equalsIgnoreCase(c2.getUnitName())) {
                    double amount = c2.getAmount();
                    double markup = c2.getMarkUp();
                    if ((amount != 133.00) && (markup != 133.00)) {
                        c2.setAmount(c2.getAmount() * 2.00);
                        //  c2.setMarkUp(c2.getMarkUp() * 2.00);
                    }
                }
                GenericCode genericCode = genericCodeDAO.findById(Integer.parseInt(c4.getUnitType()));
                if (genericCode != null) {
                    c2.setUnitName(genericCode.getCodedesc());
                }
                chargesList.add(c2);
            }
        }
        return chargesList;

//        List newList = new ArrayList(otherChargesList);
//        for (Object otherChargesList1 : otherChargesList) {
//            Charges c1 = (Charges) otherChargesList1;
//            if (c1.getChgCode().equalsIgnoreCase(costBean.getChgCode())) {
//                newList.remove(c1);
//            }
//        }
//        newList.add(costBean);
//        return newList;
    }

    public List addIntermodelToBlManually(List ratesList, Quotation quotation, String unitType, MessageResources messageResources, List fclRatesList1) throws Exception {
        Charges costBean = new Charges();
        costBean.setCostType("Flat Rate Per Container");
        costBean.setChgCode("INTMDL");
        costBean.setChargeCodeDesc("INTMDL");
        if (quotation.getAmount1() == null || quotation.getAmount1().equals("")) {
            quotation.setAmount1(0.00);
        }
        costBean.setAmount(quotation.getAmount1());
        costBean.setMarkUp(quotation.getMarkUp());
        costBean.setEfectiveDate(new Date());
        costBean.setCurrecny("USD");
        costBean.setPrint("off");
        costBean.setUnitType(unitType);
        costBean.setAccountName(quotation.getSslname());
        costBean.setAccountNo(quotation.getSsline());
        costBean.setMarkUp(0.00);
        costBean.setNumber("1");
        boolean flag = false;
        for (Iterator iter = ratesList.iterator(); iter.hasNext();) {
            Charges tempCostBean = (Charges) iter.next();
            if (tempCostBean.getChgCode().equals(costBean.getChgCode()) & tempCostBean.getUnitType().equals(costBean.getUnitType())) {
                flag = true;
                break;
            }
        }
        if (!flag && quotation.getIntermodel() != null && quotation.getIntermodel().equals("Y")) {
            fclRatesList1.add(costBean);
        }
        return fclRatesList1;
    }

    public List addFFCommissionManually(List ratesList, Quotation quotation, String unitType, MessageResources messageResources, List fclRatesList1) throws Exception {
        String ffCommissionRates[] = LoadLogisoftProperties.getProperty("ffcommissionrates").split(",");
        Charges costBean = new Charges();
        costBean.setCostType("Flat Rate Per Container");
        costBean.setChgCode(FclBlConstants.FFCODEDESC);
        costBean.setChargeCodeDesc(FclBlConstants.FFCODE);
        costBean.setRetail(Double.parseDouble(ffCommissionRates[1]));
        costBean.setMarkUp(0.00);
        costBean.setEfectiveDate(new Date());
        costBean.setCurrecny("USD");
        costBean.setPrint("off");
        costBean.setUnitType(unitType);
        costBean.setNumber("1");
        costBean.setAccountName(quotation.getSslname());
        costBean.setAccountNo(quotation.getSsline());
        String uniType[] = messageResources.getMessage("unittype").split(",");
        if (costBean.getUnitType().equals(uniType[0])) {
            costBean.setAmount(Double.parseDouble(ffCommissionRates[0]));
            costBean.setMarkUp(0.00);
        } else {
            costBean.setAmount(costBean.getRetail());
            costBean.setMarkUp(0.00);
        }
        boolean flag = false;
        for (Iterator iter = ratesList.iterator(); iter.hasNext();) {
            Charges tempCostBean = (Charges) iter.next();
            if (tempCostBean.getChgCode().equals(costBean.getChgCode()) && tempCostBean.getUnitType().equals(costBean.getUnitType())) {
                flag = true;
                break;
            }
        }
        if (!flag && quotation.getDeductFfcomm() != null && quotation.getDeductFfcomm().equals("Y")) {
            fclRatesList1.add(costBean);
        }
        return fclRatesList1;
    }

    public List addFFCommission(List otherChargesList, MessageResources messageResources, Quotation quotation) throws Exception {
        List chargesList = new ArrayList();
        Charges costBean = new Charges();
        costBean.setCostType("Flat Rate Per Container");
        costBean.setChgCode(FclBlConstants.FFCODEDESC);
        costBean.setChargeCodeDesc(FclBlConstants.FFCODE);
        costBean.setMarkUp(0.00);
        costBean.setEfectiveDate(new Date());
        costBean.setCurrecny("USD");
        costBean.setPrint("on");
        costBean.setUnitType("0.00");
        costBean.setChargeFlag("F");
        costBean.setAccountName(quotation.getSslname());
        costBean.setAccountNo(quotation.getSsline());
        String uniType[] = messageResources.getMessage("unittype").split(",");
        String ffCommissionRates[] = LoadLogisoftProperties.getProperty("ffcommissionrates").split(",");
        boolean flag = false;
        List newList = new ArrayList(otherChargesList);
        for (Object otherChargesList1 : otherChargesList) {
            Charges c1 = (Charges) otherChargesList1;
            if (c1.getChgCode().equalsIgnoreCase(costBean.getChgCode())) {
                newList.remove(c1);
            }
        }
        List list = chargesDAO.getGroupByCharges(quotation.getQuoteId());
        for (Object object : list) {
            Charges charge = (Charges) object;
            Charges c2 = new Charges();
            PropertyUtils.copyProperties(c2, costBean);
            c2.setInclude("off");
            c2.setNumber(charge.getNumber());
            c2.setUnitType(charge.getUnitType());
            c2.setSpecialEquipment(charge.getSpecialEquipment());
            c2.setSpecialEquipmentUnit(charge.getSpecialEquipmentUnit());
            c2.setOutOfGauge(charge.getOutOfGauge());
            c2.setStandardCharge(charge.getStandardCharge());
            if (c2.getUnitType().equals(uniType[0])) {
                if (quotation.getSpotRate().equals("Y")) {
                    c2.setSpotRateAmt(Double.parseDouble(ffCommissionRates[0]) * Double.parseDouble(c2.getNumber()));
                } else {
                    c2.setAmount(Double.parseDouble(ffCommissionRates[0]) * Double.parseDouble(c2.getNumber()));
                }
            } else {
                if (quotation.getSpotRate().equals("Y")) {
                    c2.setSpotRateAmt(Double.parseDouble(ffCommissionRates[1]) * Double.parseDouble(c2.getNumber()));
                } else {
                    c2.setAmount(Double.parseDouble(ffCommissionRates[1]) * Double.parseDouble(c2.getNumber()));
                }
            }
            c2.setMarkUp(0.00);
            flag = true;
            newList.add(c2);
        }
        if (!flag) {
            return otherChargesList;
        } else {
            return newList;
        }
    }

    public List addInsuranceToBl(List otherChargesList, Quotation quotation) throws Exception {
        Charges costBean = new Charges();
        costBean.setCostType("PER BL CHARGES");
        costBean.setChargeCodeDesc("INSURE");
        costBean.setChgCode("INSURANCE");
        costBean.setRetail(quotation.getInsuranceCharge());
        costBean.setMarkUp(0.00);
        costBean.setEfectiveDate(new Date());
        costBean.setCurrecny("USD");
        costBean.setPrint("off");
        costBean.setChargeFlag("M");
        costBean.setAccountName(quotation.getSslname());
        costBean.setAccountNo(quotation.getSsline());
        boolean flag = false;
        for (Iterator iter = otherChargesList.iterator(); iter.hasNext();) {
            Charges tempCostBean = (Charges) iter.next();
            if (tempCostBean.getChargeCodeDesc().equals(costBean.getChargeCodeDesc())) {
                tempCostBean.setRetail(quotation.getInsuranceCharge());
                flag = true;
                break;
            }
        }
        if (!flag) {
            otherChargesList.add(costBean);
        }
        return otherChargesList;
    }

    public List addAdminToBl(List fclRates, Quotation quotation, String adminAdjustment, String oceanFreightAdjustment, String documentCharge, MessageResources messageResources, String userName) throws Exception {
        List chargesList = new ArrayList();
        Charges chargeObject = new Charges();
        chargeObject.setCostType("FLAT RATE PER CONTAINER");
        chargeObject.setChgCode("ADMINISTRATION");
        chargeObject.setChargeCodeDesc("ADMIN");
        if (null != quotation.getFileNo()) {
            quotation.setGreendollarSignClickCount(1);
            if (CommonUtils.isNotEmpty(adminAdjustment)) {
                quotation.setAdminAdjustment(Double.parseDouble(adminAdjustment.replace(",", "")));
            }
            if (CommonUtils.isNotEmpty(oceanFreightAdjustment)) {
                quotation.setOceanFreightAdjustment(-Double.parseDouble(oceanFreightAdjustment.replace(",", "")));
            }
        }
        if (quotation.getAmount1() == null || quotation.getAmount1().equals("")) {
            quotation.setAmount1(0.00);
        }
        chargeObject.setRetail(quotation.getAmount1());
        chargeObject.setMarkUp(Double.parseDouble(dbUtil.removeComma(adminAdjustment)));
        chargeObject.setEfectiveDate(new Date());
        chargeObject.setCurrecny("USD");
        chargeObject.setPrint("off");
        chargeObject.setUnitType("0.00");
        chargeObject.setChargeFlag("M");
        chargeObject.setAccountName("");
        chargeObject.setAccountNo("");
        boolean flag = false;
        for (int i = 0; i < fclRates.size(); i++) {
            Charges c1 = (Charges) fclRates.get(i);
            if (c1.getChgCode().equalsIgnoreCase(chargeObject.getChgCode())) {
                c1.setRetail(chargeObject.getRetail());
                c1.setAmount(c1.getRetail());
            }
        }
        List tempList = new ArrayList(fclRates);
        Map<String, String> commentMap = new HashMap<String, String>();
        for (int i = 0; i < tempList.size(); i++) {
            Charges c1 = (Charges) tempList.get(i);
            if (c1.getChgCode().equalsIgnoreCase(chargeObject.getChgCode())) {
                if (CommonUtils.isNotEmpty(c1.getUnitType()) && CommonUtils.isNotEmpty(c1.getComment())) {
                    commentMap.put(c1.getUnitType(), c1.getComment());
                }
                if (Double.parseDouble(dbUtil.removeComma(adminAdjustment)) != 0d) {
                    fclRates.remove(c1);
                }
                flag = true;
            }
        }
        fclRates = orderExpandList(fclRates);
        Double totalCharges = 0.00;
        String prevUnitValue = "";
        String unitValue = "";
        for (int a = 0; a < fclRates.size(); a++) {
            double adj = 0.00;
            Charges chargeDom = (Charges) fclRates.get(a);
            if (null == chargeDom.getSpecialEquipmentUnit()) {
                chargeDom.setSpecialEquipmentUnit("");
            }
            if (CommonUtils.isNotEmpty(chargeDom.getUnitType())) {
                unitValue = chargeDom.getUnitType() + "-" + chargeDom.getSpecialEquipmentUnit() + "-" + chargeDom.getStandardCharge();
            }
            Charges cbeanPrev = null;
            if (a != 0) {
                cbeanPrev = (Charges) fclRates.get(a - 1);
                if (null == cbeanPrev.getSpecialEquipmentUnit()) {
                    cbeanPrev.setSpecialEquipmentUnit("");
                }
                if (CommonUtils.isNotEmpty(cbeanPrev.getUnitType())) {
                    prevUnitValue = cbeanPrev.getUnitType() + "-" + cbeanPrev.getSpecialEquipmentUnit() + "-" + cbeanPrev.getStandardCharge();
                }
                if (CommonUtils.isNotEmpty(prevUnitValue) && CommonUtils.isNotEmpty(unitValue) && !prevUnitValue.equals(unitValue)) {
                    Charges c2 = new Charges();
                    if (chargeObject.getUnitType().equals("0.00")) {
                        PropertyUtils.copyProperties(c2, chargeObject);
                        c2.setNumber(cbeanPrev.getNumber());
                        c2.setComment(commentMap.get(cbeanPrev.getUnitType()));
                        c2.setSpecialEquipmentUnit(cbeanPrev.getSpecialEquipmentUnit());
                        c2.setStandardCharge(cbeanPrev.getStandardCharge());
                        c2.setSpecialEquipment(cbeanPrev.getSpecialEquipment());
                        c2.setInclude("on");
                        c2.setPrint("off");
                        if ("M".equalsIgnoreCase(cbeanPrev.getChargeFlag())) {
                            totalCharges += cbeanPrev.getMarkUp() + cbeanPrev.getAdjestment();
                        } else {
                            totalCharges += cbeanPrev.getAmount() + cbeanPrev.getMarkUp() + cbeanPrev.getAdjestment();
                        }
                        c2.setUnitType(cbeanPrev.getUnitType());
                        GenericCode genericCode = genericCodeDAO.findById(Integer.parseInt(c2.getUnitType()));
                        if (genericCode != null) {
                            c2.setUnitName(genericCode.getCodedesc());
                        }
                        quotation.setTotalCharges(totalCharges);
                        quotation = calculateInsurance(quotation, quotation.getInsurancamt().toString());
                        if (Double.parseDouble(dbUtil.removeComma(adminAdjustment)) != 0d) {
                            chargesList.add(c2);

                        }
                        totalCharges = 0.00;
                    }
                } else {
                    if ("M".equalsIgnoreCase(cbeanPrev.getChargeFlag())) {
                        totalCharges += cbeanPrev.getMarkUp() + cbeanPrev.getAdjestment();
                    } else {
                        if (cbeanPrev.getChgCode().equals("OCEAN FREIGHT") && (!oceanFreightAdjustment.equals("0.00") && !oceanFreightAdjustment.equals("0.0") && !oceanFreightAdjustment.equals("0") && !oceanFreightAdjustment.equals(""))) {
                            adj = 0.00 - Double.parseDouble(dbUtil.removeComma(oceanFreightAdjustment));
                            cbeanPrev.setAdjestment(adj);
                            cbeanPrev.setUpdateBy(userName);
                            totalCharges += (cbeanPrev.getAmount() + cbeanPrev.getMarkUp() + cbeanPrev.getAdjestment());
                        } else {
                            totalCharges += (cbeanPrev.getAmount() + cbeanPrev.getMarkUp() + cbeanPrev.getAdjestment());
                        }

                    }
                }
            }
            chargesList.add(chargeDom);
        }
        if (chargesList.size() > 0) {
            Charges c4 = (Charges) chargesList.get(chargesList.size() - 1);
            Charges c2 = new Charges();
            double adj = 0.00;
            if (chargeObject.getUnitType().equals("0.00")) {
                PropertyUtils.copyProperties(c2, chargeObject);
                if ("M".equalsIgnoreCase(c4.getChargeFlag())) {
                    totalCharges += c4.getMarkUp() + c4.getAdjestment();
                } else {
                    if (c4.getChgCode().equals("OCEAN FREIGHT") && (!oceanFreightAdjustment.equals("0.00") && !oceanFreightAdjustment.equals("0.0") && !oceanFreightAdjustment.equals("0") && !oceanFreightAdjustment.equals(""))) {
                        adj = 0.00 - Double.parseDouble(dbUtil.removeComma(oceanFreightAdjustment));
                        c4.setAdjestment(adj);
                        c4.setUpdateBy(userName);
                        totalCharges += (c4.getAmount() + c4.getMarkUp() + c4.getAdjestment());
                    } else {
                        totalCharges += (c4.getAmount() + c4.getMarkUp() + c4.getAdjestment());
                    }
                }
                c2.setSpecialEquipmentUnit(c4.getSpecialEquipmentUnit());
                c2.setStandardCharge(c4.getStandardCharge());
                c2.setSpecialEquipment(c4.getSpecialEquipment());
                c2.setNumber(c4.getNumber());
                c2.setInclude("on");
                c2.setPrint("off");
                c2.setUnitType(c4.getUnitType());
                c2.setComment(commentMap.get(c4.getUnitType()));
                quotation.setTotalCharges(totalCharges);
                quotation = calculateInsurance(quotation, quotation.getInsurancamt().toString());
                c2.setAmount(0.00);
                GenericCode genericCode = genericCodeDAO.findById(Integer.parseInt(c4.getUnitType()));
                if (genericCode != null) {
                    c2.setUnitName(genericCode.getCodedesc());
                }
                c2.setMarkUp(chargeObject.getMarkUp());
                if (Double.parseDouble(dbUtil.removeComma(adminAdjustment)) != 0d) {
                    chargesList.add(c2);
                }
            }
        }
        return chargesList;
    }

    public List getInsuranceCostofGoods(List fclRates, Quotation quotation, MessageResources messageResources) throws Exception {
        List chargesList = new ArrayList();
        int no = 0;
        for (int a = 0; a < fclRates.size(); a++) {
            Charges chargeDomain = (Charges) fclRates.get(a);
            Charges cbeanPrev = null;
            if (a != 0) {
                cbeanPrev = (Charges) fclRates.get(a - 1);
                if (null != chargeDomain && null != cbeanPrev && !chargeDomain.getUnitType().equals(cbeanPrev.getUnitType())) {
                    if (cbeanPrev.getNumber() != null) {
                        no = no + Integer.parseInt(cbeanPrev.getNumber());
                    }
                }
            }
        }
        if (fclRates.size() > 0) {
            Charges c1 = (Charges) fclRates.get(fclRates.size() - 1);
            if (c1.getNumber() != null) {
                no = no + Integer.parseInt(c1.getNumber());
            }
        }
        Charges chargeObject = new Charges();
        chargeObject.setCostType("Flat Rate Per Container");
        chargeObject.setChgCode("INSURANCE");
        chargeObject.setChargeCodeDesc("INSURE");
        chargeObject.setRetail(quotation.getInsuranceCharge());
        chargeObject.setAmount(0.00);
        chargeObject.setEfectiveDate(new Date());
        chargeObject.setCurrecny("USD");
        chargeObject.setPrint("off");
        chargeObject.setUnitType("0.00");
//        chargeObject.setAccountName(quotation.getSslname());
//        chargeObject.setAccountNo(quotation.getSsline());
        chargeObject.setChargeFlag("I");
        boolean flag = false;
        for (int i = 0; i < fclRates.size(); i++) {
            Charges c1 = (Charges) fclRates.get(i);
            if (c1.getChgCode().equalsIgnoreCase(chargeObject.getChgCode())) {
                c1.setRetail(chargeObject.getRetail());
                c1.setMarkUp(c1.getRetail());
            }
        }
        List tempList = new ArrayList(fclRates);
        Map<String, String> commentMap = new HashMap<String, String>();
        for (int i = 0; i < tempList.size(); i++) {
            Charges c1 = (Charges) tempList.get(i);
            if (c1.getChgCode().equalsIgnoreCase(chargeObject.getChgCode())) {
                if (CommonUtils.isNotEmpty(c1.getUnitType()) && CommonUtils.isNotEmpty(c1.getComment())) {
                    commentMap.put(c1.getUnitType(), c1.getComment());
                }
//                chargeObject.setComment(c1.getComment());
                fclRates.remove(c1);
                flag = true;
            }
        }
        fclRates = orderExpandList(fclRates);
        Double totalCharges = 0.00;
        String prevUnitValue = "";
        String unitValue = "";
        for (int a = 0; a < fclRates.size(); a++) {
            Charges chargeDom = (Charges) fclRates.get(a);
            if (null == chargeDom.getSpecialEquipmentUnit()) {
                chargeDom.setSpecialEquipmentUnit("");
            }
            if (CommonUtils.isNotEmpty(chargeDom.getUnitType())) {
                unitValue = chargeDom.getUnitType() + "-" + chargeDom.getSpecialEquipmentUnit() + "-" + chargeDom.getStandardCharge();
            }
            Charges cbeanPrev = null;
            if (a != 0) {
                cbeanPrev = (Charges) fclRates.get(a - 1);
                if (null == cbeanPrev.getSpecialEquipmentUnit()) {
                    cbeanPrev.setSpecialEquipmentUnit("");
                }
                if (CommonUtils.isNotEmpty(cbeanPrev.getUnitType())) {
                    prevUnitValue = cbeanPrev.getUnitType() + "-" + cbeanPrev.getSpecialEquipmentUnit() + "-" + cbeanPrev.getStandardCharge();
                }
                if (CommonUtils.isNotEmpty(prevUnitValue) && CommonUtils.isNotEmpty(unitValue) && !prevUnitValue.equals(unitValue)) {
                    Charges c2 = new Charges();
                    if (chargeObject.getUnitType().equals("0.00")) {
                        PropertyUtils.copyProperties(c2, chargeObject);
                        c2.setNumber(cbeanPrev.getNumber());
                        c2.setComment(commentMap.get(cbeanPrev.getUnitType()));
                        c2.setSpecialEquipmentUnit(cbeanPrev.getSpecialEquipmentUnit());
                        c2.setStandardCharge(cbeanPrev.getStandardCharge());
                        c2.setSpecialEquipment(cbeanPrev.getSpecialEquipment());
                        c2.setInclude("on");
                        c2.setPrint("off");
                        if ("M".equalsIgnoreCase(cbeanPrev.getChargeFlag())) {
                            totalCharges += cbeanPrev.getMarkUp() + cbeanPrev.getAdjestment();
                        } else {
                            if (quotation.getSpotRate().equals("Y")) {
                                totalCharges += cbeanPrev.getSpotRateAmt() + cbeanPrev.getMarkUp() + cbeanPrev.getAdjestment();
                            } else {
                                totalCharges += cbeanPrev.getAmount() + cbeanPrev.getMarkUp() + cbeanPrev.getAdjestment();
                            }
                        }
                        c2.setUnitType(cbeanPrev.getUnitType());
                        GenericCode genericCode = genericCodeDAO.findById(Integer.parseInt(c2.getUnitType()));
                        if (genericCode != null) {
                            c2.setUnitName(genericCode.getCodedesc());
                        }
                        quotation.setTotalCharges(totalCharges);
                        quotation = calculateInsurance(quotation, quotation.getInsurancamt().toString());
                        c2.setAmount(0.00);
                        c2.setMarkUp(quotation.getInsuranceCharge());
                        chargesList.add(c2);
                        totalCharges = 0.00;
                    }
                } else {
                    if ("M".equalsIgnoreCase(cbeanPrev.getChargeFlag())) {
                        totalCharges += cbeanPrev.getMarkUp() + cbeanPrev.getAdjestment();
                    } else {
                        if (quotation.getSpotRate().equals("Y")) {
                            totalCharges += cbeanPrev.getSpotRateAmt() + cbeanPrev.getMarkUp() + cbeanPrev.getAdjestment();
                        } else {
                            totalCharges += cbeanPrev.getAmount() + cbeanPrev.getMarkUp() + cbeanPrev.getAdjestment();
                        }
                    }
                }
            }
            chargesList.add(chargeDom);
        }
        if (chargesList.size() > 0) {
            Charges c4 = (Charges) chargesList.get(chargesList.size() - 1);
            Charges c2 = new Charges();
            if (chargeObject.getUnitType().equals("0.00")) {
                PropertyUtils.copyProperties(c2, chargeObject);
                if ("M".equalsIgnoreCase(c4.getChargeFlag())) {
                    totalCharges += c4.getMarkUp() + c4.getAdjestment();
                } else {
                    if (quotation.getSpotRate().equals("Y")) {
                        totalCharges += c4.getSpotRateAmt() + c4.getMarkUp() + c4.getAdjestment();
                    } else {
                        totalCharges += c4.getAmount() + c4.getMarkUp() + c4.getAdjestment();
                    }
                }
                c2.setSpecialEquipmentUnit(c4.getSpecialEquipmentUnit());
                c2.setStandardCharge(c4.getStandardCharge());
                c2.setSpecialEquipment(c4.getSpecialEquipment());
                c2.setNumber(c4.getNumber());
                c2.setInclude("on");
                c2.setPrint("off");
                c2.setUnitType(c4.getUnitType());
                c2.setComment(commentMap.get(c4.getUnitType()));
                quotation.setTotalCharges(totalCharges);
                //c2.setRetail(c2.getRetail()*Double.parseDouble(c2.getNumber())/Double.parseDouble(String.valueOf(no)));
                quotation = calculateInsurance(quotation, quotation.getInsurancamt().toString());
                //c2.setRetail(c2.getRetail());
                c2.setAmount(0.00);
                c2.setMarkUp(quotation.getInsuranceCharge());
                GenericCode genericCode = genericCodeDAO.findById(Integer.parseInt(c4.getUnitType()));
                if (genericCode != null) {
                    c2.setUnitName(genericCode.getCodedesc());
                }
                chargesList.add(c2);
            }
        }
        return chargesList;
    }

    public List getInsuranceCostofGoodsForNonRated(List fclRates, Quotation quotation, MessageResources messageResources) throws Exception {
        List chargesList = new ArrayList();
        Charges chargeObject = new Charges();
        chargeObject.setCostType("PER BL CHARGES");
        chargeObject.setChgCode("INSURANCE");
        chargeObject.setChargeCodeDesc("INSURE");
        chargeObject.setRetail(quotation.getInsuranceCharge());
        chargeObject.setAmount(0.00);
        chargeObject.setEfectiveDate(new Date());
        chargeObject.setCurrecny("USD");
        chargeObject.setPrint("off");
        chargeObject.setChargeFlag("I");
        chargeObject.setAccountName(quotation.getSslname());
        chargeObject.setAccountNo(quotation.getSsline());
        List tempList = new ArrayList(fclRates);
        Double totalCharges = 0.00;
        boolean insurance = false;
        for (int a = 0; a < fclRates.size(); a++) {
            Charges otherCharge = (Charges) fclRates.get(a);
            if (!"INSURE".equalsIgnoreCase(otherCharge.getChargeCodeDesc())) {
                totalCharges += otherCharge.getMarkUp();
            }
            chargesList.add(otherCharge);
        }
        for (int i = 0; i < chargesList.size(); i++) {
            Charges c1 = (Charges) chargesList.get(i);
            if (c1.getChgCode().equalsIgnoreCase(chargeObject.getChgCode())) {
                quotation.setTotalCharges(totalCharges);
                quotation = calculateInsurance(quotation, quotation.getInsurancamt().toString());
                c1.setMarkUp(quotation.getInsuranceCharge());
                c1.setAmount(0.00);
                insurance = true;
            }
        }
        if (!insurance && chargesList.size() > 0) {
            Charges charge = (Charges) chargesList.get(chargesList.size() - 1);
            chargeObject.setSpecialEquipmentUnit(charge.getSpecialEquipmentUnit());
            chargeObject.setStandardCharge(charge.getStandardCharge());
            chargeObject.setSpecialEquipment(charge.getSpecialEquipment());
            chargeObject.setInclude("on");
            chargeObject.setPrint("off");
            quotation.setTotalCharges(totalCharges);
            quotation = calculateInsurance(quotation, quotation.getInsurancamt().toString());
            chargeObject.setMarkUp(quotation.getInsuranceCharge());
            chargeObject.setAmount(0.00);
            chargesList.add(chargeObject);
        }
        return chargesList;
    }

    public List deleteLocalDrayage(List fclRates) throws Exception {
        List newfclRates = new ArrayList();
        newfclRates.addAll(fclRates);
        for (int i = 0; i < fclRates.size(); i++) {
            Charges costBean = (Charges) fclRates.get(i);
            if (costBean.getChargeCodeDesc().equalsIgnoreCase("DRAY")) {
                newfclRates.remove(costBean);
            }
        }
        return newfclRates;
    }

    public List deleteSpecialEquipmentCharges(List fclRates) throws Exception {
        List newfclRates = new ArrayList();
        newfclRates.addAll(fclRates);
        for (int i = 0; i < fclRates.size(); i++) {
            Charges costBean = (Charges) fclRates.get(i);
            if (CommonUtils.isNotEmpty(costBean.getSpecialEquipmentUnit())) {
                newfclRates.remove(costBean);
            }
        }
        return newfclRates;
    }

    public List deleteSpecialEquipmentUnitCharges(List fclRates, String unitCode, String index) throws Exception {
        List newfclRates = new ArrayList();
        newfclRates.addAll(fclRates);
        for (int i = 0; i < fclRates.size(); i++) {
            Charges costBean = (Charges) fclRates.get(i);
            if (CommonUtils.isNotEmpty(unitCode) && unitCode.equals(costBean.getSpecialEquipmentUnit())
                    && CommonUtils.isNotEmpty(index) && index.equals(costBean.getStandardCharge())) {
                newfclRates.remove(costBean);
            }
        }
        return newfclRates;
    }

    public List deleteIntermodelToBl(List fclRates) throws Exception {
        List newfclRates = new ArrayList();
        newfclRates.addAll(fclRates);
        for (int i = 0; i < fclRates.size(); i++) {
            Charges costBean = (Charges) fclRates.get(i);
            if (costBean.getChargeCodeDesc().equalsIgnoreCase("INTMDL")) {
                newfclRates.remove(costBean);
            }
        }
        return newfclRates;
    }

    public List deleteDocumentCharge(List otherChargesList) throws Exception {
        for (Iterator it = otherChargesList.iterator(); it.hasNext();) {
            Charges charges = (Charges) it.next();
            if (charges.getChargeCodeDesc().equalsIgnoreCase("DOCUM")) {
                it.remove();
            }
        }
        return otherChargesList;
    }

    public List deletePierPassCharge(List fclRates) throws Exception {
        List newfclRates = new ArrayList();
        newfclRates.addAll(fclRates);
        for (int i = 0; i < fclRates.size(); i++) {
            Charges costBean = (Charges) fclRates.get(i);
            if (costBean.getChargeCodeDesc().equalsIgnoreCase("PIERPA")) {
                newfclRates.remove(costBean);
            }
        }
        return newfclRates;

//        for (Iterator it = otherChargesList.iterator(); it.hasNext();) {
//            Charges charges = (Charges) it.next();
//            if (charges.getChargeCodeDesc().equalsIgnoreCase("PIERPASS")) {
//                it.remove();
//            }
//        }
//        return otherChargesList;
    }

    public List deleteChargeFromOtherCharges(List otherChargeList, String chargeCode) throws Exception {

        return otherChargeList;
    }

    public List deleteInsuranceToBl(List fclRates) throws Exception {
        List newfclRates = new ArrayList();
        newfclRates.addAll(fclRates);
        for (int i = 0; i < fclRates.size(); i++) {
            Charges costBean = (Charges) fclRates.get(i);
            if (costBean.getChargeCodeDesc().equalsIgnoreCase("INSURE")) {
                newfclRates.remove(costBean);
            }
        }
        return newfclRates;
    }

    public List deleteFFCommission(List fclRates) throws Exception {
        List newfclRates = new ArrayList();
        newfclRates.addAll(fclRates);
        for (Object fclRate : fclRates) {
            Charges costBean = (Charges) fclRate;
            if (costBean.getChargeCodeDesc().equalsIgnoreCase(FclBlConstants.FFCODE)) {
                newfclRates.remove(costBean);
            }
        }
        return newfclRates;
    }

    public List displayRecordsForConsolidator(List fclBuyCostList, String[] unitType, List fclconsolidatorList) throws Exception {
        List fclOrgDestMiscDataList = null;
        Map<String, FclConsolidator> fclConsolidatorMap = new HashMap<String, FclConsolidator>();
        if (fclconsolidatorList != null && !fclconsolidatorList.isEmpty()) {
            for (Iterator iterator = fclconsolidatorList.iterator(); iterator.hasNext();) {
                FclConsolidator fclConsolidator = (FclConsolidator) iterator.next();
                fclConsolidatorMap.put(fclConsolidator.getCharge(), fclConsolidator);
            }
        }
        Map<String, FclBuyCost> fclBuyCostMap = new HashMap<String, FclBuyCost>();

        List unitTypeList = new ArrayList();
        String addedUnitType = ",";

        for (Object fclBuyCostList1 : fclBuyCostList) {
            FclBuyCost fclBuyCost = new FclBuyCost();
            fclBuyCost = (FclBuyCost) fclBuyCostList1;
            unitTypeList = new ArrayList();
            if (fclBuyCost.getCostId() != null) {
                fclBuyCost.setCostCode(fclBuyCost.getCostId().getCode());
            }
            if (fclBuyCost.getContType() != null) {
                fclBuyCost.setCostType(fclBuyCost.getContType().getCode());
            }
            fclBuyCost.setCurrency("");
            fclBuyCost.setRetail(null);
            fclBuyCost.setUnitTypeList(unitTypeList);
            if (CommonFunctions.isNotNull(fclBuyCost.getOrgTerminalName()) && CommonFunctions.isNotNull(fclBuyCost.getDestinationPortName()) && CommonFunctions.isNotNull(fclBuyCost.getSsLineName())) {
                fclOrgDestMiscDataList = fclOrgDestMiscDataDAO.getorgdestmiscdate(fclBuyCost.getOrgTerminalName(), fclBuyCost.getDestinationPortName(), fclBuyCost.getSsLineName());
            }
            if (null != fclOrgDestMiscDataList && fclOrgDestMiscDataList.size() > 0) {
                FclOrgDestMiscData fclOrgDestMiscData = (FclOrgDestMiscData) fclOrgDestMiscDataList.get(0);
                if (fclOrgDestMiscData.getDaysInTransit() != null) {
                    fclBuyCost.setTransitTime(fclOrgDestMiscData.getDaysInTransit().toString());
                }
            }
            FclConsolidator fclConsolidator = fclConsolidatorMap.get(fclBuyCost.getCostId().getCode());
            if (fclBuyCost.getCostType().trim().equalsIgnoreCase("A")) {
                if (fclConsolidator != null) {
                    if (fclConsolidator.getDisplay().equalsIgnoreCase("Y")) {
                        String key = fclBuyCost.getOrgTerminalName().getId() + "_" + fclBuyCost.getDestinationPortName().getId() + "_" + fclBuyCost.getCommodityCode().getId() + "_" + fclBuyCost.getSsLineName().getAccountno() + "_" + fclConsolidator.getRollToCharge();
                        if (fclBuyCostMap.get(key) != null) {
                            FclBuyCost existingFclBuyCost = fclBuyCostMap.get(key);
                            List existingFclBuyUnitTypeRates = fclBuyCostTypeRatesDAO.getFclBuyCostTypeRates(existingFclBuyCost.getFclCostId());
                            List fclBuyUnitTypeRates = fclBuyCostTypeRatesDAO.getFclBuyCostTypeRates(fclBuyCost.getFclCostId());
                            List<FclBuyCostTypeRates> mergeUnitTypesList = null;
                            if (fclBuyUnitTypeRates.size() >= existingFclBuyUnitTypeRates.size()) {
                                mergeUnitTypesList = mergeUnitTypesAmount1(fclBuyCost.getFclBuyUnitTypesSet(), existingFclBuyCost.getFclBuyUnitTypesSet(), "N", unitType);
                            } else {
                                mergeUnitTypesList = mergeUnitTypesAmount1(fclBuyCost.getFclBuyUnitTypesSet(), existingFclBuyCost.getFclBuyUnitTypesSet(), "N", unitType);
                            }
                            existingFclBuyCost.setUnitTypeList(mergeUnitTypesList);

                        } else {
                            List fclBuyUnitTypeRates = fclBuyCostTypeRatesDAO.getFclBuyCostTypeRates(fclBuyCost.getFclCostId());
                            if (fclBuyUnitTypeRates != null) {
                                unitTypeList = new ArrayList();

                                for (Iterator iterator = fclBuyUnitTypeRates.iterator(); iterator.hasNext();) {
                                    if (fclConsolidator.getExcludeFromTotal().equalsIgnoreCase("N")) {
                                        FclBuyCostTypeRates fclBuyCostTypeRates = (FclBuyCostTypeRates) iterator.next();
                                        if (fclBuyCostTypeRates.getUnitType() != null) {
                                            if (fclBuyCostTypeRates.getActiveAmt() == null) {
                                                fclBuyCostTypeRates.setActiveAmt(0.00);
                                            }
                                            if (fclBuyCostTypeRates.getMarkup() == null) {
                                                fclBuyCostTypeRates.setMarkup(0.00);
                                            }
                                            if (fclBuyCostTypeRates.getUnitAmount() == null) {
                                                fclBuyCostTypeRates.setUnitAmount(fclBuyCostTypeRates.getActiveAmt() + fclBuyCostTypeRates.getMarkup());
                                            }
                                            fclBuyCostTypeRates.setUnitname(fclBuyCostTypeRates.getUnitType().getCodedesc());
                                            boolean flag = false;
                                            for (int utype = 0; utype < unitType.length; utype++) {
                                                String uType = unitType[utype];
                                                if (uType.equals("0") || uType.equals(fclBuyCostTypeRates.getUnitType().getId().toString())) {
                                                    flag = true;
                                                    if (fclBuyCostTypeRates.getUnitType() != null) {
                                                        if (addedUnitType.indexOf("," + fclBuyCostTypeRates.getUnitType().getCodedesc() + ",") == -1) {
                                                            addedUnitType += fclBuyCostTypeRates.getUnitType().getCodedesc() + ",";
                                                        }
                                                    }
                                                    break;
                                                }
                                            }
                                            if (flag) {

                                                unitTypeList.add(fclBuyCostTypeRates);

                                            }
                                        } else {
                                            fclBuyCost.setCurrency(fclBuyCostTypeRates.getCurrency().getCode());
                                            if (fclBuyCostTypeRates.getMinimumAmt() != null && !fclBuyCostTypeRates.getRatAmount().equals("0.00")) {
                                                fclBuyCost.setRetail(fclBuyCostTypeRates.getMinimumAmt());
                                            } else {
                                                fclBuyCost.setRetail(fclBuyCostTypeRates.getRatAmount());
                                            }
                                        }
                                    } else {

                                        FclBuyCostTypeRates fclBuyCostTypeRates = (FclBuyCostTypeRates) iterator.next();
                                        if (fclBuyCostTypeRates.getUnitType() != null) {
                                            fclBuyCostTypeRates.setUnitAmount(0.00);
                                            fclBuyCostTypeRates.setUnitname(fclBuyCostTypeRates.getUnitType().getCodedesc());

                                            boolean flag = false;
                                            for (int utype = 0; utype < unitType.length; utype++) {
                                                String uType = unitType[utype];
                                                if (uType.equals("0") || uType.equals(fclBuyCostTypeRates.getUnitType().getId().toString())) {
                                                    flag = true;
                                                    if (fclBuyCostTypeRates.getUnitType() != null) {
                                                        if (addedUnitType.indexOf("," + fclBuyCostTypeRates.getUnitType().getCodedesc() + ",") == -1) {
                                                            addedUnitType += fclBuyCostTypeRates.getUnitType().getCodedesc() + ",";
                                                        }
                                                    }
                                                    break;
                                                }
                                            }
                                            if (flag) {
                                                unitTypeList.add(fclBuyCostTypeRates);
                                            }
                                        } else {
                                            fclBuyCost.setCurrency(fclBuyCostTypeRates.getCurrency().getCode());
                                            if (fclBuyCostTypeRates.getMinimumAmt() != null && !fclBuyCostTypeRates.getRatAmount().equals("0.00")) {
                                                fclBuyCost.setRetail(fclBuyCostTypeRates.getMinimumAmt());
                                            } else {
                                                fclBuyCost.setRetail(fclBuyCostTypeRates.getRatAmount());
                                            }
                                        }
                                    }
                                }
                            }
                            Set fclBuyCostSet = new HashSet<FclBuyCostTypeRates>();
                            fclBuyCost.setUnitTypeList(unitTypeList);

                            fclBuyCostMap.put(key, fclBuyCost);
                        }
                    }
                } else {
                    String key = fclBuyCost.getOrgTerminalName().getId() + "_" + fclBuyCost.getDestinationPortName().getId() + "_" + fclBuyCost.getCommodityCode().getId() + "_" + fclBuyCost.getSsLineName().getAccountno() + "_" + "001";
                    if (fclBuyCostMap.get(key) != null) {

                        FclBuyCost existingFclBuyCost = fclBuyCostMap.get(key);
                        for (Iterator iter = existingFclBuyCost.getFclBuyUnitTypesSet().iterator(); iter.hasNext();) {
                            FclBuyCostTypeRates fclBuyCostTypeRates = (FclBuyCostTypeRates) iter.next();
                        }
                        List existingFclBuyUnitTypeRates = fclBuyCostTypeRatesDAO.getFclBuyCostTypeRates(existingFclBuyCost.getFclCostId());
                        List fclBuyUnitTypeRates = fclBuyCostTypeRatesDAO.getFclBuyCostTypeRates(fclBuyCost.getFclCostId());
                        List<FclBuyCostTypeRates> mergeUnitTypesList = null;

                        if (fclBuyUnitTypeRates.size() != 0 && existingFclBuyUnitTypeRates.size() != 0) {
                            if (fclBuyUnitTypeRates.size() >= existingFclBuyUnitTypeRates.size()) {
                                mergeUnitTypesList = mergeUnitTypesAmount1(existingFclBuyCost.getFclBuyUnitTypesSet(), fclBuyCost.getFclBuyUnitTypesSet(), "N", unitType);
                            } else {
                                mergeUnitTypesList = mergeUnitTypesAmount1(existingFclBuyCost.getFclBuyUnitTypesSet(), fclBuyCost.getFclBuyUnitTypesSet(), "N", unitType);
                            }
                            for (Iterator iter = existingFclBuyCost.getUnitTypeList().iterator(); iter.hasNext();) {
                                FclBuyCostTypeRates fFclBuyCostTypeRates = (FclBuyCostTypeRates) iter.next();
                            }
                        }
                        if (mergeUnitTypesList != null) {
                            existingFclBuyCost.setUnitTypeList(mergeUnitTypesList);
                        }

                    } else {

                        List fclBuyUnitTypeRates = fclBuyCostTypeRatesDAO.getFclBuyCostTypeRates(fclBuyCost.getFclCostId());
                        if (fclBuyUnitTypeRates != null) {
                            unitTypeList = new ArrayList();

                            for (Iterator iterator = fclBuyUnitTypeRates.iterator(); iterator.hasNext();) {

                                FclBuyCostTypeRates fclBuyCostTypeRates = (FclBuyCostTypeRates) iterator.next();
                                if (fclBuyCostTypeRates.getUnitType() != null) {
                                    if (fclBuyCostTypeRates.getActiveAmt() == null) {
                                        fclBuyCostTypeRates.setActiveAmt(0.00);
                                    }
                                    if (fclBuyCostTypeRates.getMarkup() == null) {
                                        fclBuyCostTypeRates.setMarkup(0.00);
                                    }
                                    fclBuyCostTypeRates.setUnitAmount(fclBuyCostTypeRates.getActiveAmt() + fclBuyCostTypeRates.getMarkup());
                                    fclBuyCostTypeRates.setUnitname(fclBuyCostTypeRates.getUnitType().getCodedesc());

                                    boolean flag = false;
                                    for (int utype = 0; utype < unitType.length; utype++) {
                                        String uType = unitType[utype];
                                        if (uType.equals("0") || uType.equals(fclBuyCostTypeRates.getUnitType().getId().toString())) {
                                            flag = true;
                                            if (fclBuyCostTypeRates.getUnitType() != null) {
                                                if (addedUnitType.indexOf("," + fclBuyCostTypeRates.getUnitType().getCodedesc() + ",") == -1) {
                                                    addedUnitType += fclBuyCostTypeRates.getUnitType().getCodedesc() + ",";
                                                }
                                            }
                                            break;
                                        }
                                    }
                                    if (flag) {
                                        unitTypeList.add(fclBuyCostTypeRates);
                                    }
                                } else {
                                    fclBuyCost.setCurrency(fclBuyCostTypeRates.getCurrency().getCode());
                                    if (fclBuyCostTypeRates.getMinimumAmt() != null && !fclBuyCostTypeRates.getRatAmount().equals("0.00")) {
                                        fclBuyCost.setRetail(fclBuyCostTypeRates.getMinimumAmt());
                                    } else {
                                        fclBuyCost.setRetail(fclBuyCostTypeRates.getRatAmount());
                                    }
                                }
                            }
                        }
                        fclBuyCost.setUnitTypeList(unitTypeList);
                        fclBuyCostMap.put(key, fclBuyCost);
                    }

                }
            } else {

                String key = fclBuyCost.getOrgTerminalName().getId() + "_" + fclBuyCost.getDestinationPortName().getId() + "_" + fclBuyCost.getCommodityCode().getId() + "_" + fclBuyCost.getSsLineName().getAccountno() + "_" + fclBuyCost.getCostCode();
                if (fclBuyCost.getFclBuyUnitTypesSet() != null) {
                    Iterator iter = fclBuyCost.getFclBuyUnitTypesSet().iterator();
                    while (iter.hasNext()) {
                        FclBuyCostTypeRates fclBuyCostTypeRates = (FclBuyCostTypeRates) iter.next();
                        fclBuyCost.setCurrency(fclBuyCostTypeRates.getCurrency().getCode());

                        if (fclBuyCostTypeRates.getMinimumAmt() != null && !fclBuyCostTypeRates.getMinimumAmt().equals(0.0)) {
                            fclBuyCost.setRetail(fclBuyCostTypeRates.getMinimumAmt());
                        } else {
                            fclBuyCost.setRetail(fclBuyCostTypeRates.getRatAmount());
                        }
                    }
                    fclBuyCostMap.put(key, fclBuyCost);
                }

            }
        }
        List tempfclBuyCostList = new ArrayList();
        List tempList = new ArrayList();
        List hashMap = new ArrayList();
        for (Iterator iter = fclBuyCostMap.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            FclBuyCost fclBuyCost = fclBuyCostMap.get(name);
            tempList.add(fclBuyCost.getFclStdId());
            hashMap.add(fclBuyCost);
        }
        Collections.sort(tempList);
        List<FclBuyCost> newFclBuyCostList = new ArrayList<FclBuyCost>();
        for (int i = 0; i < tempList.size(); i++) {
            for (int j = 0; j < hashMap.size(); j++) {
                FclBuyCost fclBuyCost = (FclBuyCost) hashMap.get(j);
                if (fclBuyCost.getFclStdId().equals(tempList.get(i))) {
                    boolean flag = false;
                    for (int k = 0; k < newFclBuyCostList.size(); k++) {
                        FclBuyCost fclBuyCost1 = (FclBuyCost) newFclBuyCostList.get(k);
                        if (fclBuyCost1.getCostCode().equals(fclBuyCost.getCostCode()) && fclBuyCost1.getSsLineName().getAccountno().equals(fclBuyCost.getSsLineName().getAccountno())) {
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        newFclBuyCostList.add(fclBuyCost);
                    }
                }
            }
        }
        for (int i = 0; i < newFclBuyCostList.size(); i++) {
            FclBuyCost fclBuyCost = (FclBuyCost) newFclBuyCostList.get(i);

            addEmptyUnitTypes(fclBuyCost, addedUnitType);

            tempfclBuyCostList.add(fclBuyCost);
        }
        return tempfclBuyCostList;
    }

    public Map displayRecordsForConsolidator1(List fclBuyCostList, String[] unitType, List fclconsolidatorList, HttpServletRequest request, MessageResources messageResources) throws Exception {
        List newFclBuyCostList = new ArrayList();
        String addedUnitType = ",";
        for (Iterator iter = fclconsolidatorList.iterator(); iter.hasNext();) {
            FclConsolidator fclConsolidator = (FclConsolidator) iter.next();
            FclBuyCost fclBuyCost = new FclBuyCost();
            fclBuyCost.setCostCode(fclConsolidator.getRollToCharge());
            newFclBuyCostList.add(fclBuyCost);
        }
        Map<Integer, List> ssLineNumberMap = new HashMap<Integer, List>();
        Map<String, Integer> sortedMap = new HashMap<String, Integer>();
        Map<String, Double> rateMap = null;
        Map<String, Double> hazardsMap = null;
        Map<String, Double> otherMap = null;
        List rateAndHazardList = null;
        //start of for
        for (Iterator iter = newFclBuyCostList.iterator(); iter.hasNext();) {
            FclBuyCost newFclBuyCost = (FclBuyCost) iter.next();
            List mergerUniTypeList = new ArrayList();

            for (Iterator iterator = fclBuyCostList.iterator(); iterator.hasNext();) {
                FclBuyCost fclBuyCost = (FclBuyCost) iterator.next();
                List fclBuyUnitTypeRates = new ArrayList();
                if (fclBuyCost.getFclBuyUnitTypesSet() != null) {
                    Iterator iter1 = fclBuyCost.getFclBuyUnitTypesSet().iterator();
                    while (iter1.hasNext()) {
                        FclBuyCostTypeRates fclBuyCostTypeRates = (FclBuyCostTypeRates) iter1.next();
                        fclBuyUnitTypeRates.add(fclBuyCostTypeRates);
                    }
                }
                for (Iterator iterator1 = fclBuyUnitTypeRates.iterator(); iterator1.hasNext();) {
                    FclBuyCostTypeRates fclBuyCostTypeRates = (FclBuyCostTypeRates) iterator1.next();
                    if (fclBuyCostTypeRates.getUnitType() != null) {
                        if (addedUnitType.indexOf("," + fclBuyCostTypeRates.getUnitType().getCodedesc() + ",") == -1) {
                            addedUnitType += fclBuyCostTypeRates.getUnitType().getCodedesc() + ",";
                        }
                    }
                }
                if (ssLineNumberMap.containsKey(fclBuyCost.getFclStdId())) {
                    rateAndHazardList = ssLineNumberMap.get(fclBuyCost.getFclStdId());
                    rateMap = (Map<String, Double>) rateAndHazardList.get(0);
                    hazardsMap = (Map<String, Double>) rateAndHazardList.get(1);
                    otherMap = (Map<String, Double>) rateAndHazardList.get(2);
                } else {
                    rateMap = new HashMap<String, Double>();
                    hazardsMap = new HashMap<String, Double>();
                    otherMap = new HashMap<String, Double>();
                }
                if (fclBuyUnitTypeRates.size() > 0) {
                    if (fclBuyCost.getCostId() != null && fclBuyCost.getContType() != null && fclBuyCost.getContType().getCodedesc().equalsIgnoreCase(messageResources.getMessage("FlatRatePerConatinerSize")) && !newFclBuyCost.getCostCode().equals(messageResources.getMessage("hazardouscharge")) && !fclBuyCost.getCostId().getCode().equals(messageResources.getMessage("hazardouscharge"))
                            && (fclBuyCost.getCostId().getCode().equals(messageResources.getMessage("oceanfreightcharge")) || !fclBuyCost.getCostId().getCode().equals(newFclBuyCost.getCostCode()))) {
                        for (Iterator iterator1 = fclBuyUnitTypeRates.iterator(); iterator1.hasNext();) {
                            FclBuyCostTypeRates fclBuyCostTypeRates = (FclBuyCostTypeRates) iterator1.next();

                            if (fclBuyCostTypeRates.getUnitType() != null && rateMap.containsKey(fclBuyCostTypeRates.getUnitType().getCodedesc())) {
                                Double amount = rateMap.get(fclBuyCostTypeRates.getUnitType().getCodedesc());

                                amount = amount + fclBuyCostTypeRates.getActiveAmt();
                                rateMap.put(fclBuyCostTypeRates.getUnitType().getCodedesc(), amount);
                            } else {
                                if (fclBuyCostTypeRates.getUnitType() != null) {
                                    rateMap.put(fclBuyCostTypeRates.getUnitType().getCodedesc(), fclBuyCostTypeRates.getActiveAmt());
                                }
                            }
                        }
                    } else if (fclBuyCost.getCostId() != null && fclBuyCost.getCostId().getCode().equals(messageResources.getMessage("hazardouscharge")) && fclBuyCost.getContType() != null && fclBuyCost.getContType().getCodedesc().equalsIgnoreCase(messageResources.getMessage("FlatRatePerConatinerSize"))) {
                        for (Iterator iterator1 = fclBuyUnitTypeRates.iterator(); iterator1.hasNext();) {
                            FclBuyCostTypeRates fclBuyCostTypeRates = (FclBuyCostTypeRates) iterator1.next();
                            if (fclBuyCostTypeRates.getUnitType() != null && hazardsMap.containsKey(fclBuyCostTypeRates.getUnitType().getCodedesc())) {
                                Double amount = hazardsMap.get(fclBuyCostTypeRates.getUnitType().getCodedesc());
                                hazardsMap.put(fclBuyCostTypeRates.getUnitType().getCodedesc(), amount);
                            } else {
                                if (fclBuyCostTypeRates.getUnitType() != null) {
                                    hazardsMap.put(fclBuyCostTypeRates.getUnitType().getCodedesc(), fclBuyCostTypeRates.getActiveAmt());
                                }
                            }
                        }
                    } else {
                        for (Iterator iterator1 = fclBuyUnitTypeRates.iterator(); iterator1.hasNext();) {
                            FclBuyCostTypeRates fclBuyCostTypeRates = (FclBuyCostTypeRates) iterator1.next();
                            if (fclBuyCostTypeRates.getRatAmount() != null && fclBuyCostTypeRates.getRatAmount() != 0.0) {
                                otherMap.put(fclBuyCost.getCostId().getCodedesc(), fclBuyCostTypeRates.getRatAmount());
                            }
                        }
                    }
                }
                if (ssLineNumberMap.containsKey(fclBuyCost.getFclStdId())) {
                    rateAndHazardList = ssLineNumberMap.get(fclBuyCost.getFclStdId());
                    rateAndHazardList.add(rateMap);
                    rateAndHazardList.add(hazardsMap);
                    rateAndHazardList.add(otherMap);
                    ssLineNumberMap.put(fclBuyCost.getFclStdId(), rateAndHazardList);
                    sortedMap.put(fclBuyCost.getSsLineName().getAccountName(), fclBuyCost.getFclStdId());
                } else {
                    rateAndHazardList = new ArrayList();
                    rateAndHazardList.add(rateMap);
                    rateAndHazardList.add(hazardsMap);
                    rateAndHazardList.add(otherMap);
                    ssLineNumberMap.put(fclBuyCost.getFclStdId(), rateAndHazardList);
                    sortedMap.put(fclBuyCost.getSsLineName().getAccountName(), fclBuyCost.getFclStdId());
                }
            }
            newFclBuyCost.setUnitTypeList(mergerUniTypeList);

        }//end of for

        //print rate map
        Set ssLineKeySet = ssLineNumberMap.keySet();
        Iterator ssLineIt = ssLineKeySet.iterator();
        String modifiedUnitTypes = new String(addedUnitType);
        if (modifiedUnitTypes.startsWith(",")) {
            modifiedUnitTypes = modifiedUnitTypes.substring(1);
        }
        if (modifiedUnitTypes.endsWith(",")) {
            modifiedUnitTypes = modifiedUnitTypes.substring(0, modifiedUnitTypes.length() - 1);
        }
        String[] unitTypeAry = modifiedUnitTypes.split(",");
        for (int i = 0; i < unitTypeAry.length; i++) {
            ssLineIt = ssLineKeySet.iterator();
            while (ssLineIt.hasNext()) {
                Object key = ssLineIt.next();
                rateAndHazardList = ssLineNumberMap.get(key);
                rateMap = (Map<String, Double>) rateAndHazardList.get(0);
                if (!rateMap.containsKey(unitTypeAry[i])) {
                    rateMap.put(unitTypeAry[i], 0.00);
                }
            }
        }
        request.setAttribute("sortedMap", sortedMap);
        return ssLineNumberMap;
    }

    public Map displayRecordsForConsolidator2(List ratesList, String[] unitType, List fclconsolidatorList, HttpServletRequest request, MessageResources messageResources, String hazmat) throws Exception {
        List newFclBuyCostList = new ArrayList();
        String addedUnitType = ",";
        for (Iterator iter = fclconsolidatorList.iterator(); iter.hasNext();) {
            FclConsolidator fclConsolidator = (FclConsolidator) iter.next();
            FclBuyCost fclBuyCost = new FclBuyCost();
            fclBuyCost.setCostCode(fclConsolidator.getRollToCharge());
            newFclBuyCostList.add(fclBuyCost);
        }
        String OFR = messageResources.getMessage("OceanFreight");
        String consolidatorRates[] = new String[10];
        if (OFR.indexOf(",") != -1) {
            consolidatorRates = OFR.split(",");
        }
        String oceanFreight = messageResources.getMessage("oceanfreightcharge");
        Map<Integer, List> ssLineNumberMap = new HashMap<Integer, List>();
        Map<String, Integer> sortedMap = new HashMap<String, Integer>();
        Map<String, Double> rateMap = null;
        Map<String, Double> hazardsMap = null;
        Map<String, String> keyMap = new HashMap<String, String>();
        Map<String, Double> otherMap = null;
        List rateAndHazardList = null;

        for (Iterator iterator = ratesList.iterator(); iterator.hasNext();) {
            Rates rates = (Rates) iterator.next();

            List fclBuyUnitTypeRates = new ArrayList();
            if (rates.getUnitType() != null) {
                String codeDesc = (String) GenericCodeCacheManager.getCodeDesc(rates.getUnitType());
                if (addedUnitType.indexOf("," + codeDesc + ",") == -1) {
                    addedUnitType += codeDesc + ",";
                }
            }
            if (ssLineNumberMap.containsKey(rates.getFclStdId())) {
                rateAndHazardList = ssLineNumberMap.get(rates.getFclStdId());
                rateMap = (Map<String, Double>) rateAndHazardList.get(0);
                hazardsMap = (Map<String, Double>) rateAndHazardList.get(1);
                otherMap = (Map<String, Double>) rateAndHazardList.get(2);
            } else {
                rateMap = new HashMap<String, Double>();
                hazardsMap = new HashMap<String, Double>();
                otherMap = new HashMap<String, Double>();
            }
            boolean flag = false;
            if (ratesList.size() > 0) {
                String code = (String) GenericCodeCacheManager.getCodeDesc(rates.getCostId());
                String costType = (String) GenericCodeCacheManager.getCodeDesc(rates.getCostType());
                String costCode = (String) GenericCodeCacheManager.getCode(rates.getCostId());
                if (costType.equalsIgnoreCase(messageResources.getMessage("FlatRatePerConatinerSize"))) {
                    for (int i = 0; i < consolidatorRates.length; i++) {
                        if (costCode.equals(consolidatorRates[i])) {
                            String codeDesc = (String) GenericCodeCacheManager.getCodeDesc(rates.getUnitType());
                            if (codeDesc != null && costCode != null && rateMap.containsKey(costCode + "-" + codeDesc)) {
                                Double amount = rateMap.get(costCode + "-" + codeDesc);
                                amount = amount + rates.getRatAmount() + rates.getMarkUp();
                                rateMap.put(costCode + "-" + codeDesc, amount);
                                keyMap.put(codeDesc, costCode + "-" + codeDesc);
                                flag = true;
                            } else {
                                if (codeDesc != null && costCode != null) {
                                    rateMap.put(costCode + "-" + codeDesc, rates.getRatAmount() + rates.getMarkUp());
                                    keyMap.put(codeDesc, costCode + "-" + codeDesc);
                                }
                                flag = true;
                            }
                        }
                    }
                    if (!flag) {
                        String codeDesc = (String) GenericCodeCacheManager.getCodeDesc(rates.getUnitType());
                        if (codeDesc != null && costCode != null && rateMap.containsKey(oceanFreight + "-" + codeDesc)) {
                            Double amount = rateMap.get(oceanFreight + "-" + codeDesc);
                            amount = amount + rates.getRatAmount() + rates.getMarkUp();
                            rateMap.put(oceanFreight + "-" + codeDesc, amount);
                            keyMap.put(codeDesc, oceanFreight + "-" + codeDesc);
                        } else {
                            if (codeDesc != null && costCode != null) {
                                rateMap.put(oceanFreight + "-" + codeDesc, rates.getRatAmount() + rates.getMarkUp());
                                keyMap.put(codeDesc, oceanFreight + "-" + codeDesc);
                            }
                        }
                    }
                } else {
                    if (rates.getActiveAmt() != null && rates.getActiveAmt() != 0.0) {
                        otherMap.put(rates.getCodeDesc(), rates.getActiveAmt() + rates.getMarkUp());
                    }
                }

            }
            if (ssLineNumberMap.containsKey(rates.getFclStdId())) {
                rateAndHazardList = ssLineNumberMap.get(rates.getFclStdId());
                rateAndHazardList.add(rateMap);
                rateAndHazardList.add(hazardsMap);
                rateAndHazardList.add(otherMap);
                ssLineNumberMap.put(rates.getFclStdId(), rateAndHazardList);
                sortedMap.put(rates.getAccountName(), rates.getFclStdId());
            } else {
                rateAndHazardList = new ArrayList();
                rateAndHazardList.add(rateMap);
                rateAndHazardList.add(hazardsMap);
                rateAndHazardList.add(otherMap);
                ssLineNumberMap.put(rates.getFclStdId(), rateAndHazardList);
                sortedMap.put(rates.getAccountName(), rates.getFclStdId());
            }
        }
        /*newFclBuyCost.setUnitTypeList(mergerUniTypeList);
        
        }*///end of for

        //print rate map
        Set ssLineKeySet = ssLineNumberMap.keySet();
        Iterator ssLineIt = ssLineKeySet.iterator();
        String modifiedUnitTypes = new String(addedUnitType);
        if (modifiedUnitTypes.startsWith(",")) {
            modifiedUnitTypes = modifiedUnitTypes.substring(1);
        }
        if (modifiedUnitTypes.endsWith(",")) {
            modifiedUnitTypes = modifiedUnitTypes.substring(0, modifiedUnitTypes.length() - 1);
        }
        String[] unitTypeAry = modifiedUnitTypes.split(",");
        request.setAttribute("keyMap", keyMap);
        request.setAttribute("sortedMap", sortedMap);
        return ssLineNumberMap;
    }

    private List<FclBuyCostTypeRates> mergeUnitTypesAmount(List fclBuyUnitTypesSet1, List fclBuyUnitTypesSet12) throws Exception {
        List<FclBuyCostTypeRates> mergeUnitTypeAmountList = new ArrayList<FclBuyCostTypeRates>();
        if (fclBuyUnitTypesSet1.size() > 0) {
            for (Iterator iter = fclBuyUnitTypesSet1.iterator(); iter.hasNext();) {
                FclBuyCostTypeRates fclBuyCostTypeRates = (FclBuyCostTypeRates) iter.next();
                for (Iterator iterator = fclBuyUnitTypesSet12.iterator(); iterator.hasNext();) {
                    FclBuyCostTypeRates newFclBuyCostTypeRates = (FclBuyCostTypeRates) iterator.next();
                    if (fclBuyCostTypeRates.getUnitType() != null && newFclBuyCostTypeRates.getUnitType() != null
                            && fclBuyCostTypeRates.getUnitType().getId().toString().equals(newFclBuyCostTypeRates.getUnitType().getId().toString())) {

                        if (fclBuyCostTypeRates.getUnitAmount() == null) {
                            fclBuyCostTypeRates.setUnitAmount(fclBuyCostTypeRates.getActiveAmt() + fclBuyCostTypeRates.getMarkup());
                        }
                        if (newFclBuyCostTypeRates.getUnitAmount() == null) {
                            fclBuyCostTypeRates.setUnitAmount(fclBuyCostTypeRates.getUnitAmount() + newFclBuyCostTypeRates.getActiveAmt() + newFclBuyCostTypeRates.getMarkup());
                        } else {
                            fclBuyCostTypeRates.setUnitAmount(fclBuyCostTypeRates.getUnitAmount() + newFclBuyCostTypeRates.getUnitAmount());
                        }
                        fclBuyCostTypeRates.setUnitname(fclBuyCostTypeRates.getUnitType().getCodedesc());
                        //mergeUnitTypeAmountList.add(fclBuyCostTypeRates);
                        break;
                    } else if (fclBuyCostTypeRates.getUnitType() != null) {
                        if (fclBuyCostTypeRates.getUnitAmount() == null) {
                            fclBuyCostTypeRates.setUnitAmount(fclBuyCostTypeRates.getActiveAmt() + fclBuyCostTypeRates.getMarkup());
                        }
                        fclBuyCostTypeRates.setUnitname(fclBuyCostTypeRates.getUnitType().getCodedesc());
                        mergeUnitTypeAmountList.add(fclBuyCostTypeRates);
                        break;
                    } else if (newFclBuyCostTypeRates.getUnitType() != null) {
                        if (newFclBuyCostTypeRates.getUnitAmount() == null) {
                            newFclBuyCostTypeRates.setUnitAmount(newFclBuyCostTypeRates.getActiveAmt() + newFclBuyCostTypeRates.getMarkup());
                        }
                        fclBuyCostTypeRates.setUnitname(newFclBuyCostTypeRates.getUnitType().getCodedesc());
                        mergeUnitTypeAmountList.add(newFclBuyCostTypeRates);
                        break;
                    }
                }
            }
        } else {
            for (Iterator iterator = fclBuyUnitTypesSet12.iterator(); iterator.hasNext();) {
                FclBuyCostTypeRates newFclBuyCostTypeRates = (FclBuyCostTypeRates) iterator.next();
                if (newFclBuyCostTypeRates.getUnitAmount() == null) {
                    newFclBuyCostTypeRates.setUnitAmount(newFclBuyCostTypeRates.getActiveAmt() + newFclBuyCostTypeRates.getMarkup());
                }
                newFclBuyCostTypeRates.setUnitname(newFclBuyCostTypeRates.getUnitType().getCodedesc());
                mergeUnitTypeAmountList.add(newFclBuyCostTypeRates);
            }
        }
        return mergeUnitTypeAmountList;
    }

    private List<FclBuyCostTypeRates> mergeUnitTypesAmount1(Set fclBuyUnitTypesSet1, Set fclBuyUnitTypesSet12, String excludeFromTotal, String[] unitType) throws Exception {
        List<FclBuyCostTypeRates> mergeUnitTypeAmountList = new ArrayList<FclBuyCostTypeRates>();
        Map<String, FclBuyCostTypeRates> fclConsolidatorMap = new HashMap<String, FclBuyCostTypeRates>();
        for (Iterator iterator = fclBuyUnitTypesSet1.iterator(); iterator.hasNext();) {
            FclBuyCostTypeRates fclBuyCostTypeRates = (FclBuyCostTypeRates) iterator.next();
            if (fclBuyCostTypeRates.getUnitType() != null) {
                fclConsolidatorMap.put(fclBuyCostTypeRates.getUnitType().getId().toString(), fclBuyCostTypeRates);
            }
        }
        for (Iterator iterator = fclBuyUnitTypesSet12.iterator(); iterator.hasNext();) {
            FclBuyCostTypeRates fclBuyCostTypeRates = (FclBuyCostTypeRates) iterator.next();
            String key = fclBuyCostTypeRates.getUnitType().getId().toString();
            if (fclConsolidatorMap.get(key) != null) {
                FclBuyCostTypeRates mergefclBuyCostTypeRates = fclConsolidatorMap.get(key);
                if (excludeFromTotal.equalsIgnoreCase("N")) {
                    if (mergefclBuyCostTypeRates.getActiveAmt() == null) {
                        mergefclBuyCostTypeRates.setActiveAmt(0.00);
                    }
                    if (mergefclBuyCostTypeRates.getMarkup() == null) {
                        mergefclBuyCostTypeRates.setMarkup(0.00);
                    }
                    if (fclBuyCostTypeRates.getActiveAmt() == null) {
                        fclBuyCostTypeRates.setActiveAmt(0.00);
                    }
                    if (fclBuyCostTypeRates.getMarkup() == null) {
                        fclBuyCostTypeRates.setMarkup(0.00);
                    }

                    if (mergefclBuyCostTypeRates.getUnitAmount() == null || mergefclBuyCostTypeRates.getUnitAmount().equals(0.0)) {
                        mergefclBuyCostTypeRates.setUnitAmount(mergefclBuyCostTypeRates.getActiveAmt() + mergefclBuyCostTypeRates.getMarkup());
                    }
                    if (fclBuyCostTypeRates.getUnitAmount() == null) {
                        mergefclBuyCostTypeRates.setUnitAmount(mergefclBuyCostTypeRates.getUnitAmount() + fclBuyCostTypeRates.getActiveAmt() + fclBuyCostTypeRates.getMarkup());
                    } else {
                        mergefclBuyCostTypeRates.setUnitAmount(mergefclBuyCostTypeRates.getUnitAmount() + fclBuyCostTypeRates.getUnitAmount());
                    }
                }
                mergefclBuyCostTypeRates.setUnitname(mergefclBuyCostTypeRates.getUnitType().getCodedesc());
                boolean flag = false;
                for (int utype = 0; utype < unitType.length; utype++) {
                    String uType = unitType[utype];
                    if (uType.equals("0") || uType.equals(fclBuyCostTypeRates.getUnitType().getId().toString())) {
                        flag = true;

                        break;
                    }
                }
                if (flag) {
                    mergeUnitTypeAmountList.add(mergefclBuyCostTypeRates);
                }
                fclConsolidatorMap.remove(mergefclBuyCostTypeRates);
            } else {
                if (excludeFromTotal.equalsIgnoreCase("N")) {
                    if (fclBuyCostTypeRates.getActiveAmt() == null) {
                        fclBuyCostTypeRates.setActiveAmt(0.00);
                    }
                    if (fclBuyCostTypeRates.getMarkup() == null) {
                        fclBuyCostTypeRates.setMarkup(0.00);
                    }
                    if (fclBuyCostTypeRates.getUnitAmount() == null) {
                        fclBuyCostTypeRates.setUnitAmount(fclBuyCostTypeRates.getActiveAmt() + fclBuyCostTypeRates.getMarkup());
                    }
                } else {
                    fclBuyCostTypeRates.setUnitAmount(0.00);
                }
                fclBuyCostTypeRates.setUnitname(fclBuyCostTypeRates.getUnitType().getCodedesc());
                boolean flag = false;
                for (int utype = 0; utype < unitType.length; utype++) {
                    String uType = unitType[utype];
                    if (uType.equals("0") || uType.equals(fclBuyCostTypeRates.getUnitType().getId().toString())) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    mergeUnitTypeAmountList.add(fclBuyCostTypeRates);

                }
            }
        }
        for (Iterator iterator = fclBuyUnitTypesSet1.iterator(); iterator.hasNext();) {
            FclBuyCostTypeRates fclBuyCostTypeRates = (FclBuyCostTypeRates) iterator.next();
        }
        return mergeUnitTypeAmountList;
    }

    private void addEmptyUnitTypes(FclBuyCost fclBuyCost, String unitTypes) throws Exception {
        String modifiedUnitTypes = new String(unitTypes);
        if (modifiedUnitTypes.startsWith(",")) {
            modifiedUnitTypes = modifiedUnitTypes.substring(1);
        }
        if (modifiedUnitTypes.endsWith(",")) {
            modifiedUnitTypes = modifiedUnitTypes.substring(0, modifiedUnitTypes.length() - 1);
        }
        String[] unitTypeAry = modifiedUnitTypes.split(",");
        for (int i = 0; i < unitTypeAry.length; i++) {
            FclBuyCostTypeRates blankFclBuyCostTypeRates = new FclBuyCostTypeRates();
            blankFclBuyCostTypeRates.setUnitAmount(0d);
            blankFclBuyCostTypeRates.setCurrency(new GenericCode());
            blankFclBuyCostTypeRates.setUnitname(unitTypeAry[i]);
            boolean flag = false;
            for (Iterator iterator = fclBuyCost.getUnitTypeList().iterator(); iterator.hasNext();) {
                FclBuyCostTypeRates fclBuyCostTypeRates = (FclBuyCostTypeRates) iterator.next();
                if (fclBuyCostTypeRates.getUnitname().equals(unitTypeAry[i])) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                fclBuyCost.getUnitTypeList().add(blankFclBuyCostTypeRates);
            }
        }
        HashMap hashMap = new HashMap();
        List tempList = new ArrayList();
        List tempUnitTypeList = new ArrayList();
        List unitTypeList = fclBuyCost.getUnitTypeList();
        fclBuyCost.setUnitTypeList(new ArrayList());
        for (Iterator iterator = unitTypeList.iterator(); iterator.hasNext();) {
            FclBuyCostTypeRates tempFclBuyCostTypeRates = (FclBuyCostTypeRates) iterator.next();
            hashMap.put(tempFclBuyCostTypeRates.getUnitname(), tempFclBuyCostTypeRates);
            tempList.add(tempFclBuyCostTypeRates.getUnitname());
        }
        Collections.sort(tempList);
        for (int i = 0; i < tempList.size(); i++) {
            FclBuyCostTypeRates tempFclBuyCostTypeRates = (FclBuyCostTypeRates) hashMap.get(tempList.get(i));
            tempUnitTypeList.add(tempFclBuyCostTypeRates);
        }
        fclBuyCost.setUnitTypeList(tempUnitTypeList);

    }

    public List getConsolidatorList(MessageResources messageResources) throws Exception {
        List<FclConsolidator> consolidatorList = new ArrayList<FclConsolidator>();
        String OFR = messageResources.getMessage("OceanFreight");

        String charges[] = OFR.split(",");
        for (String charge : charges) {
            FclConsolidator fclConsolidator = new FclConsolidator();
            fclConsolidator.setCharge(charge);
            fclConsolidator.setRollToCharge(charge);
            fclConsolidator.setDisplay("Y");
            fclConsolidator.setExcludeFromTotal("N");
            consolidatorList.add(fclConsolidator);
        }
        return consolidatorList;
    }

    public FclBl getfclby(String fileno) throws Exception {
        return fclBlDAO.getFileNoObject(fileno);
    }

    public HazmatMaterial findById(int id) throws Exception {
        return hazmatMaterialDAO.findById(id);
    }

    public List getHazmatByBolId(int bolId) throws Exception {
        return hazmatMaterialDAO.getHazmatByBolId(bolId);
    }

    public List getPackageType(Integer codeTypeId) throws Exception {
        List packageTypeList = genericCodeDAO.findByCodeTypeid(codeTypeId);
        List newPackageTypeList = new ArrayList();
        newPackageTypeList.add(new LabelValueBean("Select Package Type", ""));
        for (Iterator iter = packageTypeList.iterator(); iter.hasNext();) {
            GenericCode genericCode = (GenericCode) iter.next();
            newPackageTypeList.add(new LabelValueBean(genericCode.getCodedesc(), genericCode.getCodedesc()));
        }
        return newPackageTypeList;
    }

    public List getPackageCompositionType(Integer codeTypeId) throws Exception {
        List packageCompositionTypeList = genericCodeDAO.findByCodeTypeid(codeTypeId);
        List newpackageCompositionTypeList = new ArrayList();
        newpackageCompositionTypeList.add(new LabelValueBean("Select Package Type", ""));
        for (Iterator iter = packageCompositionTypeList.iterator(); iter.hasNext();) {
            GenericCode genericCode = (GenericCode) iter.next();
            newpackageCompositionTypeList.add(new LabelValueBean(genericCode.getCodedesc(), genericCode.getCodedesc()));
        }
        return newpackageCompositionTypeList;
    }

    public List getPackageTypeSingular(Integer codeTypeId) throws Exception {
        List packageTypeList = genericCodeDAO.findByCodeTypeid(codeTypeId);
        List newPackageTypeList = new ArrayList();
        newPackageTypeList.add(new LabelValueBean("Select Package Type", ""));
        for (Iterator iter = packageTypeList.iterator(); iter.hasNext();) {
            GenericCode genericCode = (GenericCode) iter.next();
            newPackageTypeList.add(new LabelValueBean(genericCode.getField1(), genericCode.getField1()));
        }
        return newPackageTypeList;
    }

    public List getPackingGroupCode() throws Exception {
        List packingGroupCodeList = new ArrayList();
        packingGroupCodeList.add(new LabelValueBean("Select Group Code", ""));
        List newList = genericCodeDAO.findByCodeTypeid(54);
        for (Iterator iterator = newList.iterator(); iterator.hasNext();) {
            GenericCode genericCode = (GenericCode) iterator.next();
            packingGroupCodeList.add(new LabelValueBean(genericCode.getCodedesc(), genericCode.getCodedesc()));
        }
        return packingGroupCodeList;
    }

    public List getRates(List fclBuyLIst, MessageResources messageResources, String hazmat) throws Exception {
        List tempFclBuyCostList = new ArrayList();
        List fclBuyCostList = new ArrayList();
        List tempOfrList = new ArrayList();
        for (int i = 0; i < fclBuyLIst.size(); i++) {
            FclBuy fclBuy = (FclBuy) fclBuyLIst.get(i);
            if (fclBuy.getFclBuyCostsSet() != null) {
                Iterator iter = fclBuy.getFclBuyCostsSet().iterator();
                tempOfrList = new ArrayList();
                tempFclBuyCostList = new ArrayList();
                while (iter.hasNext()) {
                    FclBuyCost fclBuyCost = (FclBuyCost) iter.next();
                    fclBuyCost.setOrgTerminalName(fclBuy.getOriginTerminal());
                    fclBuyCost.setDestinationPortName(fclBuy.getDestinationPort());
                    fclBuyCost.setCommodityCode(fclBuy.getComNum());
                    fclBuyCost.setSsLineName(fclBuy.getSslineNo());
                    if (hazmat != null && hazmat.equals("N") && (fclBuyCost.getCostId().getCodedesc().trim().equals(messageResources.getMessage("hazardousSurcharge"))
                            || fclBuyCost.getCostId().getCodedesc().trim().equals(messageResources.getMessage("hazardousSurchargeland"))
                            || fclBuyCost.getCostId().getCodedesc().trim().equals(messageResources.getMessage("hazardousSurchargesea"))
                            || fclBuyCost.getCostId().getCodedesc().trim().equalsIgnoreCase(messageResources.getMessage("hazfeecertun"))
                            || fclBuyCost.getCostId().getCodedesc().trim().equalsIgnoreCase(messageResources.getMessage("hazardcertificate"))
                            || fclBuyCost.getCostId().getCode().trim().equalsIgnoreCase(messageResources.getMessage("hazardous"))
                            || fclBuyCost.getCostId().getCodedesc().trim().equalsIgnoreCase(messageResources.getMessage("hazardousfee")))) {
                    } else {
                        if (fclBuyCost.getCostId() != null && fclBuyCost.getCostId().getCode().equals("001")) {
                            tempOfrList.add(fclBuyCost);
                        } else {
                            tempFclBuyCostList.add(fclBuyCost);
                        }
                    }

                }
                for (Iterator iterator = tempOfrList.iterator(); iterator.hasNext();) {
                    FclBuyCost tempFclBuyCost = (FclBuyCost) iterator.next();
                    fclBuyCostList.add(tempFclBuyCost);
                }
                for (Iterator iterator = tempFclBuyCostList.iterator(); iterator.hasNext();) {
                    FclBuyCost tempFclBuyCost = (FclBuyCost) iterator.next();
                    fclBuyCostList.add(tempFclBuyCost);
                }
            }
        }
        return fclBuyCostList;
    }

    public List consolidateRates(List fclRates, MessageResources messageResources, boolean importFlag) throws Exception {
        List consolidatorList = new ArrayList();
        Map hashMap = new HashMap();
        Map newHashMap = new HashMap();
        for (Iterator iterator = fclRates.iterator(); iterator.hasNext();) {
            Charges charges = (Charges) iterator.next();
            Charges newCharges = new Charges();
            PropertyUtils.copyProperties(newCharges, charges);
            if (null == newCharges.getSpecialEquipmentUnit()) {
                newCharges.setSpecialEquipmentUnit("");
            }
            if (null == newCharges.getStandardCharge()) {
                newCharges.setStandardCharge("");
            }
            if (null != newCharges.getSpotRateAmt()) {
                newCharges.setAmount(newCharges.getSpotRateAmt());
            }
            newHashMap.put(newCharges.getChargeCodeDesc() + "-" + newCharges.getUnitName() + "-" + newCharges.getSpecialEquipmentUnit() + "-" + newCharges.getStandardCharge(), newCharges);
        }
        String interModelRate = "";
        String interModelRate1 = "";
        String ofimpRate = "";
        String ofimpRate1 = "";
        boolean flag1 = false;
        String consolidator = "";
        for (Iterator iterator = fclRates.iterator(); iterator.hasNext();) {
            Charges charges = (Charges) iterator.next();
            Charges newCharges = new Charges();
            PropertyUtils.copyProperties(newCharges, charges);
            if (null == newCharges.getSpecialEquipmentUnit()) {
                newCharges.setSpecialEquipmentUnit("");
            }
            if (null == newCharges.getStandardCharge()) {
                newCharges.setStandardCharge("");
            }
            if (importFlag == false) {
                consolidator = messageResources.getMessage("OceanFreight");
            } else {
                consolidator = messageResources.getMessage("OceanFreightImports");
            }
            String colsolidatorRates[] = new String[5];
            if (consolidator.contains(",")) {
                colsolidatorRates = consolidator.split(",");
            }
            String interModel = messageResources.getMessage("Intermodel");
            String interModelRates[] = new String[10];
            if (interModel.contains(",")) {
                interModelRates = interModel.split(",");
            }

            boolean interModelFlag = false;
            boolean flag = false;
            boolean oceanImportFlag = false;
            for (int i = 0; i < colsolidatorRates.length; i++) {
                if (newCharges.getChargeCodeDesc().equalsIgnoreCase(colsolidatorRates[i])) {
                    if (newCharges.getChargeCodeDesc().equalsIgnoreCase(messageResources.getMessage("oceanfreightcharge")) && newHashMap.get(messageResources.getMessage("oceanfreightcharge") + "-"
                            + newCharges.getUnitName() + "-" + newCharges.getSpecialEquipmentUnit() + "-" + newCharges.getStandardCharge()) != null) {
                        Charges tempCharges = (Charges) newHashMap.get(messageResources.getMessage("oceanfreightcharge") + "-"
                                + newCharges.getUnitName() + "-" + newCharges.getSpecialEquipmentUnit() + "-" + newCharges.getStandardCharge());
                        if (null == tempCharges.getSpecialEquipmentUnit()) {
                            tempCharges.setSpecialEquipmentUnit("");
                        }
                        if (null == tempCharges.getStandardCharge()) {
                            tempCharges.setStandardCharge("");
                        }
                        hashMap.put(messageResources.getMessage("oceanfreightcharge") + "-" + tempCharges.getUnitName() + "-" + tempCharges.getSpecialEquipmentUnit() + "-" + tempCharges.getStandardCharge(), tempCharges);
                        flag = true;
                    } else if (newCharges.getChargeCodeDesc().equalsIgnoreCase(messageResources.getMessage("oceanfreightImpcharge")) && newHashMap.get(messageResources.getMessage("oceanfreightImpcharge") + "-"
                            + newCharges.getUnitName() + "-" + newCharges.getSpecialEquipmentUnit() + "-" + newCharges.getStandardCharge()) != null) {
                        Charges tempCharges = (Charges) newHashMap.get(messageResources.getMessage("oceanfreightImpcharge") + "-"
                                + newCharges.getUnitName() + "-" + newCharges.getSpecialEquipmentUnit() + "-" + newCharges.getStandardCharge());
                        if (null == tempCharges.getSpecialEquipmentUnit()) {
                            tempCharges.setSpecialEquipmentUnit("");
                        }
                        if (null == tempCharges.getStandardCharge()) {
                            tempCharges.setStandardCharge("");
                        }
                        hashMap.put(messageResources.getMessage("oceanfreightImpcharge") + "-" + tempCharges.getUnitName() + "-" + tempCharges.getSpecialEquipmentUnit() + "-" + tempCharges.getStandardCharge(), tempCharges);
                        flag = true;
                    } else {
                        interModelFlag = false;
                        for (int j = 0; j < interModelRates.length; j++) {
                            if (newCharges.getChargeFlag() == null) {
                                newCharges.setChargeFlag("");
                            }
                            if (newCharges.getChargeFlag() != null && !newCharges.getChargeFlag().equals("M")
                                    && newCharges.getChargeCodeDesc().equalsIgnoreCase(interModelRates[j])) {
                                interModelFlag = true;
                                break;
                            }
                        }

                        if (interModelFlag) {
                            interModelRate1 = interModelRate + "-" + newCharges.getUnitName() + "-" + newCharges.getSpecialEquipmentUnit() + "-" + newCharges.getStandardCharge();
                            if (hashMap.containsKey(interModelRate1)) {
                                Charges tempCharges = (Charges) newHashMap.get(interModelRate1);
                                if (null == tempCharges.getSpecialEquipmentUnit()) {
                                    tempCharges.setSpecialEquipmentUnit("");
                                }
                                if (null == tempCharges.getStandardCharge()) {
                                    tempCharges.setStandardCharge("");
                                }
                                tempCharges.setAmount(tempCharges.getAmount() + (null != newCharges.getSpotRateAmt() ? newCharges.getSpotRateAmt() : newCharges.getAmount()) + (null != newCharges.getSpotRateMarkUp() ? newCharges.getSpotRateMarkUp() : newCharges.getMarkUp()));
                                tempCharges.setAdjestment(tempCharges.getAdjestment() + newCharges.getAdjestment());
                                if (null != tempCharges.getSpotRateAmt() && null != newCharges.getSpotRateAmt()) {
                                    tempCharges.setSpotRateAmt(tempCharges.getSpotRateAmt() + newCharges.getSpotRateAmt());
                                } else if (null == tempCharges.getSpotRateAmt() && null != newCharges.getSpotRateAmt()) {
                                    tempCharges.setSpotRateAmt(newCharges.getSpotRateAmt());
                                }
                                hashMap.put(tempCharges.getChargeCodeDesc() + "-" + tempCharges.getUnitName() + "-" + tempCharges.getSpecialEquipmentUnit() + "-" + tempCharges.getStandardCharge(), tempCharges);
                            } else {
                                hashMap.put(newCharges.getChargeCodeDesc() + "-" + newCharges.getUnitName() + "-" + newCharges.getSpecialEquipmentUnit() + "-" + newCharges.getStandardCharge(), newCharges);
                                interModelRate = newCharges.getChargeCodeDesc();
                            }
                        }

                        if (!interModelFlag) {
                            if (null != newCharges.getSpotRateAmt()) {
                                newCharges.setAmount(newCharges.getSpotRateAmt());
                            }
                            if (null != newCharges.getSpotRateMarkUp()) {
                                newCharges.setSpotRateMarkUp(newCharges.getSpotRateMarkUp());
                            }
                            hashMap.put(newCharges.getChargeCodeDesc() + "-" + newCharges.getUnitName() + "-" + newCharges.getSpecialEquipmentUnit() + "-" + newCharges.getStandardCharge(), newCharges);
                            flag = true;
                        }
                    }
                    break;
                }
            }

            if (!flag && !interModelFlag) {
                if (importFlag == false) {
                    if (newCharges.getChargeCodeDesc().equals("DOCUM") || newCharges.getChargeCodeDesc().equals("INSURE") || newCharges.getChargeCodeDesc().equals(FclBlConstants.FFCODE) || (newCharges.getChargeFlag() != null && newCharges.getChargeFlag().equals("M")) || newCharges.getChargeCodeDesc().equals("PIERPA") || newCharges.getChargeCodeDesc().equals("CHASFEE")) {
                        hashMap.put(newCharges.getChargeCodeDesc() + "-" + newCharges.getUnitName() + "-" + newCharges.getSpecialEquipmentUnit() + "-" + newCharges.getStandardCharge(), newCharges);
                    } else {
                        if (newHashMap.get(messageResources.getMessage("oceanfreightcharge") + "-"
                                + newCharges.getUnitName() + "-" + newCharges.getSpecialEquipmentUnit() + "-" + newCharges.getStandardCharge()) != null) {
                            Charges tempCharges = (Charges) newHashMap.get(messageResources.getMessage("oceanfreightcharge") + "-"
                                    + newCharges.getUnitName() + "-" + newCharges.getSpecialEquipmentUnit() + "-" + newCharges.getStandardCharge());
                            if (null == tempCharges.getSpecialEquipmentUnit()) {
                                tempCharges.setSpecialEquipmentUnit("");
                            }
                            if (null == tempCharges.getStandardCharge()) {
                                tempCharges.setStandardCharge("");
                            }
                            tempCharges.setAmount(tempCharges.getAmount() + (null != newCharges.getSpotRateAmt() ? newCharges.getSpotRateAmt() : newCharges.getAmount()) + (null != newCharges.getSpotRateMarkUp() ? newCharges.getSpotRateMarkUp() : newCharges.getMarkUp()));
                            tempCharges.setAdjestment(tempCharges.getAdjestment() + newCharges.getAdjestment());
                            if (null != tempCharges.getSpotRateAmt() && null != newCharges.getSpotRateAmt()) {
                                tempCharges.setSpotRateAmt(tempCharges.getSpotRateAmt() + newCharges.getSpotRateAmt());
                            } else if (null == tempCharges.getSpotRateAmt() && null != newCharges.getSpotRateAmt()) {
                                tempCharges.setSpotRateAmt(newCharges.getSpotRateAmt());
                            }
                            hashMap.put(messageResources.getMessage("oceanfreightcharge") + "-" + tempCharges.getUnitName() + "-" + tempCharges.getSpecialEquipmentUnit() + "-" + tempCharges.getStandardCharge(), tempCharges);
                        } else if (newHashMap.get(messageResources.getMessage("oceanfreightImpcharge") + "-"
                                + newCharges.getUnitName() + "-" + newCharges.getSpecialEquipmentUnit() + "-" + newCharges.getStandardCharge()) != null) {
                            Charges tempCharges = (Charges) newHashMap.get(messageResources.getMessage("oceanfreightImpcharge") + "-"
                                    + newCharges.getUnitName() + "-" + newCharges.getSpecialEquipmentUnit() + "-" + newCharges.getStandardCharge());
                            if (null == tempCharges.getSpecialEquipmentUnit()) {
                                tempCharges.setSpecialEquipmentUnit("");
                            }
                            if (null == tempCharges.getStandardCharge()) {
                                tempCharges.setStandardCharge("");
                            }
                            tempCharges.setAmount(tempCharges.getAmount() + newCharges.getAmount() + newCharges.getMarkUp());
                            tempCharges.setAdjestment(tempCharges.getAdjestment() + newCharges.getAdjestment());
                            hashMap.put(messageResources.getMessage("oceanfreightImpcharge") + "-" + tempCharges.getUnitName() + "-" + tempCharges.getSpecialEquipmentUnit() + "-" + tempCharges.getStandardCharge(), tempCharges);
                        }
                    }
                } else if (importFlag == true) {
                    if ((newCharges.getChargeCodeDesc().equals("INSURE") || newCharges.getChargeCodeDesc().equals(FclBlConstants.FFCODE)) || (newCharges.getChargeFlag() != null && newCharges.getChargeFlag().equals("M"))) {
                        hashMap.put(newCharges.getChargeCodeDesc() + "-" + newCharges.getUnitName() + "-" + newCharges.getSpecialEquipmentUnit() + "-" + newCharges.getStandardCharge(), newCharges);
                    } else {
                        if (newHashMap.get(messageResources.getMessage("oceanfreightcharge") + "-"
                                + newCharges.getUnitName() + "-" + newCharges.getSpecialEquipmentUnit() + "-" + newCharges.getStandardCharge()) != null) {
                            Charges tempCharges = (Charges) newHashMap.get(messageResources.getMessage("oceanfreightcharge") + "-"
                                    + newCharges.getUnitName() + "-" + newCharges.getSpecialEquipmentUnit() + "-" + newCharges.getStandardCharge());
                            if (null == tempCharges.getSpecialEquipmentUnit()) {
                                tempCharges.setSpecialEquipmentUnit("");
                            }
                            if (null == tempCharges.getStandardCharge()) {
                                tempCharges.setStandardCharge("");
                            }
                            tempCharges.setAmount(tempCharges.getAmount() + (null != newCharges.getSpotRateAmt() ? newCharges.getSpotRateAmt() : newCharges.getAmount()) + (null != newCharges.getSpotRateMarkUp() ? newCharges.getSpotRateMarkUp() : newCharges.getMarkUp()));
                            tempCharges.setAdjestment(tempCharges.getAdjestment() + newCharges.getAdjestment());
                            if (null != tempCharges.getSpotRateAmt() && null != newCharges.getSpotRateAmt()) {
                                tempCharges.setSpotRateAmt(tempCharges.getSpotRateAmt() + newCharges.getSpotRateAmt());
                            } else if (null == tempCharges.getSpotRateAmt() && null != newCharges.getSpotRateAmt()) {
                                tempCharges.setSpotRateAmt(newCharges.getSpotRateAmt());
                            }
                            hashMap.put(messageResources.getMessage("oceanfreightcharge") + "-" + tempCharges.getUnitName() + "-" + tempCharges.getSpecialEquipmentUnit() + "-" + tempCharges.getStandardCharge(), tempCharges);
                        } else if (newHashMap.get(messageResources.getMessage("oceanfreightImpcharge") + "-"
                                + newCharges.getUnitName() + "-" + newCharges.getSpecialEquipmentUnit() + "-" + newCharges.getStandardCharge()) != null) {
                            Charges tempCharges = (Charges) newHashMap.get(messageResources.getMessage("oceanfreightImpcharge") + "-"
                                    + newCharges.getUnitName() + "-" + newCharges.getSpecialEquipmentUnit() + "-" + newCharges.getStandardCharge());
                            if (null == tempCharges.getSpecialEquipmentUnit()) {
                                tempCharges.setSpecialEquipmentUnit("");
                            }
                            if (null == tempCharges.getStandardCharge()) {
                                tempCharges.setStandardCharge("");
                            }
                            tempCharges.setAmount(tempCharges.getAmount() + newCharges.getAmount() + newCharges.getMarkUp());
                            tempCharges.setAdjestment(tempCharges.getAdjestment() + newCharges.getAdjestment());
                            hashMap.put(messageResources.getMessage("oceanfreightImpcharge") + "-" + tempCharges.getUnitName() + "-" + tempCharges.getSpecialEquipmentUnit() + "-" + tempCharges.getStandardCharge(), tempCharges);
                        }
                    }
                }
            }
        }
        Set hashSet = hashMap.keySet();
        List tempList = new ArrayList();
        HashMap tempMap = new HashMap();
        for (Iterator iterator = hashSet.iterator(); iterator.hasNext();) {
            String key = (String) iterator.next();
            Charges charges = (Charges) hashMap.get(key);
            consolidatorList.add(charges);
        }
        return orderExpandList(consolidatorList);
    }

    public void getRates(EditBookingsForm NewBookingsForm, BookingFcl bookingFcl, MessageResources messageResources, List bookingFclRatesList, List otherChargesList) throws Exception {
        String code = "";
        String desc = "";
        String sscode = "";
        String origin = bookingFcl.getOriginTerminal();
        String destination = bookingFcl.getPortofDischarge();
        String commodity = bookingFcl.getComcode();
        String ssline = NewBookingsForm.getSslDescription();
        String unitType = NewBookingsForm.getSelectedCheck();
        List tempRatesList = new ArrayList();
        tempRatesList.addAll(bookingFclRatesList);
        if (ssline != null) {
            int i = ssline.indexOf("//");
            if (i != -1) {
                String destinationPort[] = ssline.split("//");
                desc = destinationPort[0];
                code = destinationPort[1];
            }
            List list = customerDAO.findForAgenttNo1(code, desc);
            if (list != null && list.size() > 0) {
                TradingPartnerTemp customerTemp = (TradingPartnerTemp) list.get(0);
                ssline = customerTemp.getAccountName();
                sscode = customerTemp.getAccountno();

            }
        }
        HashMap hashMap = new HashMap();
        HashMap<String, BookingfclUnits> unitHashMap = new HashMap<String, BookingfclUnits>();
        for (Iterator iter = bookingFclRatesList.iterator(); iter.hasNext();) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) iter.next();
            if (bookingfclUnits.getUnitType() != null && unitHashMap.get(bookingfclUnits.getUnitType().getCodedesc()) == null) {
                unitHashMap.put(bookingfclUnits.getUnitType().getCodedesc(), bookingfclUnits);
            }
            if (bookingfclUnits.getUnitType() != null) {
                hashMap.put(bookingfclUnits.getChgCode() + "-" + bookingfclUnits.getUnitType().getCodedesc(), bookingfclUnits);
            } else {
                hashMap.put(bookingfclUnits.getChgCode(), bookingfclUnits);
            }
        }
        for (Iterator iterator = otherChargesList.iterator(); iterator.hasNext();) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) iterator.next();
            hashMap.put(bookingfclUnits.getChgCode(), bookingfclUnits);
        }
        //new code added for origin
        Integer comId = null;
        if (commodity != null && !commodity.equals("") && !commodity.equals("%")) {
            List comList = genericCodeDAO.findForGenericCode(commodity);
            if (comList != null && comList.size() > 0) {
                GenericCode genObj = (GenericCode) comList.get(0);
                commodity = genObj.getId().toString();
                comId = genObj.getId();
            }
        }
        String markUp = "markup1";
        if (CommonUtils.isNotEmpty(destination) && destination.lastIndexOf("(") != -1
                && destination.lastIndexOf(")") != -1) {
            String destinationCode = destination.substring(destination.lastIndexOf("(") + 1, destination.lastIndexOf(")"));
            Ports ports = new PortsDAO().getPorts(destinationCode);
            if (null != ports && null != ports.getRegioncode() && "Y".equalsIgnoreCase(ports.getRegioncode().getField3())) {
                markUp = "markup2";
            }
        }
        List nonRatesList = fclBuyDAO.findRatesForOtherCommodity(comId, markUp);
        if (CommonUtils.isNotEmpty(nonRatesList)) {
            for (Iterator iterator = nonRatesList.iterator(); iterator.hasNext();) {
                NonRates nonRates = (NonRates) iterator.next();
                commodity = nonRates.getBaseCommodity().toString();
                break;
            }
        }
        origin = stringFormatter.findForManagement(origin, null);
        destination = stringFormatter.findForManagement(destination, null);
        bookingFclDAO.update(bookingFcl);
        String unitTypes[] = unitType.split(",");
        List ratesList = fclBuyDAO.findRates2(origin, destination, commodity, sscode, sdf.format(bookingFcl.getQuoteDate()), sdf.format(bookingFcl.getBookingDate()));
        for (int i = 0; i < ratesList.size(); i++) {
            FclBuy fclBuy = (FclBuy) ratesList.get(i);
            if (fclBuy.getFclBuyCostsSet() != null) {
                Iterator iter = fclBuy.getFclBuyCostsSet().iterator();
                while (iter.hasNext()) {
                    FclBuyCost fclBuyCost = (FclBuyCost) iter.next();
                    if (bookingFcl.getHazmat() != null && bookingFcl.getHazmat().equals("N") && (fclBuyCost.getCostId().getCodedesc().trim().equals(messageResources.getMessage("hazardousSurcharge"))
                            || fclBuyCost.getCostId().getCodedesc().trim().equals(messageResources.getMessage("hazardousSurchargeland"))
                            || fclBuyCost.getCostId().getCodedesc().trim().equals(messageResources.getMessage("hazardousSurchargesea"))
                            || fclBuyCost.getCostId().getCodedesc().trim().equalsIgnoreCase(messageResources.getMessage("hazfeecertun"))
                            || fclBuyCost.getCostId().getCodedesc().trim().equalsIgnoreCase(messageResources.getMessage("hazardcertificate"))
                            || fclBuyCost.getCostId().getCode().trim().equalsIgnoreCase(messageResources.getMessage("hazardous"))
                            || fclBuyCost.getCostId().getCodedesc().trim().equalsIgnoreCase(messageResources.getMessage("hazardousfee")))) {
                    } else {
                        if (fclBuyCost.getFclBuyUnitTypesSet() != null) {
                            boolean flag = false;
                            for (Iterator iterator2 = fclBuyCost.getFclBuyUnitTypesSet().iterator(); iterator2.hasNext();) {
                                FclBuyCostTypeRates fclBuyCostTypeRates = (FclBuyCostTypeRates) iterator2.next();
                                if (fclBuyCostTypeRates.getEffectiveDate() != null && bookingFcl.getQuoteDate().before(fclBuyCostTypeRates.getEffectiveDate()) && fclBuyCostTypeRates.getEffectiveDate().before(new Date())) {
                                    if (fclBuyCostTypeRates.getUnitType() != null) {
                                        if (hashMap.containsKey(fclBuyCost.getCostId().getCodedesc() + "-" + fclBuyCostTypeRates.getUnitType().getCodedesc())) {
                                            flag = true;
                                            BookingfclUnits bookingfclUnits = (BookingfclUnits) hashMap.get(fclBuyCost.getCostId().getCodedesc() + "-" + fclBuyCostTypeRates.getUnitType().getCodedesc());
                                            if (fclBuyCostTypeRates.getActiveAmt() != bookingfclUnits.getAmount()) {
                                                bookingfclUnits.setAmount(fclBuyCostTypeRates.getActiveAmt());
                                            }
                                            if (fclBuyCostTypeRates.getMarkup() != bookingfclUnits.getMarkUp()) {
                                                bookingfclUnits.setMarkUp(fclBuyCostTypeRates.getMarkup());
                                            }
                                            if (CommonUtils.isNotEmpty(nonRatesList)) {
                                                for (Iterator iterator = nonRatesList.iterator(); iterator.hasNext();) {
                                                    NonRates nonRates = (NonRates) iterator.next();
                                                    if (nonRates.getChargeCode().equals(fclBuyCost.getCostId().getCode())) {
                                                        if (nonRates.getAddsub().equals("A")) {
                                                            bookingfclUnits.setMarkUp(bookingfclUnits.getMarkUp() + nonRates.getAmount());
                                                        } else {
                                                            bookingfclUnits.setMarkUp(bookingfclUnits.getMarkUp() - nonRates.getAmount());
                                                        }
                                                    }
                                                }
                                            }
                                            if (bookingFCL.getSpotRate().equals("Y")) {
                                                double spotRate = null != bookingfclUnits.getSpotRateAmt() ? bookingfclUnits.getSpotRateAmt() : 0d;
                                                double spotMark = null != bookingfclUnits.getSpotRateMarkUp() ? bookingfclUnits.getSpotRateMarkUp() : 0d;
                                                bookingfclUnits.setSellRate(spotRate + spotMark);
                                            } else {
                                                bookingfclUnits.setSellRate(bookingfclUnits.getAmount() + bookingfclUnits.getMarkUp());
                                            }
                                            bookingFclRatesList.add(bookingfclUnits);
                                        }
                                    } else {
                                        if (hashMap.containsKey(fclBuyCost.getCostId().getCodedesc())) {
                                            flag = true;
                                            BookingfclUnits bookingfclUnits = (BookingfclUnits) hashMap.get(fclBuyCost.getCostId().getCodedesc());
                                            if (fclBuyCostTypeRates.getRatAmount() != bookingfclUnits.getAmount()) {
                                                bookingfclUnits.setAmount(fclBuyCostTypeRates.getRatAmount());
                                            }
                                            if (fclBuyCostTypeRates.getMarkup() != bookingfclUnits.getMarkUp()) {
                                                bookingfclUnits.setMarkUp(fclBuyCostTypeRates.getMarkup());
                                            }
                                            if (CommonUtils.isNotEmpty(nonRatesList)) {
                                                for (Iterator iterator = nonRatesList.iterator(); iterator.hasNext();) {
                                                    NonRates nonRates = (NonRates) iterator.next();
                                                    if (nonRates.getChargeCode().equals(fclBuyCost.getCostId().getCode())) {
                                                        if (nonRates.getAddsub().equals("A")) {
                                                            bookingfclUnits.setMarkUp(bookingfclUnits.getMarkUp() + nonRates.getAmount());
                                                        } else {
                                                            bookingfclUnits.setMarkUp(bookingfclUnits.getMarkUp() - nonRates.getAmount());
                                                        }
                                                    }
                                                }
                                            }
                                            if (bookingFCL.getSpotRate().equals("Y")) {
                                                double spotRate = null != bookingfclUnits.getSpotRateAmt() ? bookingfclUnits.getSpotRateAmt() : 0d;
                                                double spotMark = null != bookingfclUnits.getSpotRateMarkUp() ? bookingfclUnits.getSpotRateMarkUp() : 0d;
                                                bookingfclUnits.setSellRate(spotRate + spotMark);
                                            } else {
                                                bookingfclUnits.setSellRate(bookingfclUnits.getAmount() + bookingfclUnits.getMarkUp());
                                            }
                                            bookingFclRatesList.add(bookingfclUnits);
                                        } else {
                                            String temp = "";
                                            if (fclBuyCost.getContType() != null && fclBuyCost.getContType().getCodedesc().equals("Flat Rate Per Container")) {
                                                for (Iterator iterator = tempRatesList.iterator(); iterator.hasNext();) {
                                                    BookingfclUnits bookingfclUnits = (BookingfclUnits) iterator.next();
                                                    if (bookingfclUnits.getUnitType() != null && !temp.equals(bookingfclUnits.getUnitType().getCode())) {
                                                        BookingfclUnits tempBookingfclUnits = new BookingfclUnits();
                                                        tempBookingfclUnits.setApproveBl(bookingfclUnits.getApproveBl());
                                                        tempBookingfclUnits.setUnitType(bookingfclUnits.getUnitType());
                                                        tempBookingfclUnits.setNumbers(bookingfclUnits.getNumbers());
                                                        tempBookingfclUnits.setChgCode(fclBuyCost.getCostId().getCodedesc());
                                                        tempBookingfclUnits.setChargeCodeDesc(fclBuyCost.getCostId().getCode());
                                                        tempBookingfclUnits.setCostType(fclBuyCost.getContType().getCodedesc());
                                                        tempBookingfclUnits.setAmount(fclBuyCostTypeRates.getRatAmount());
                                                        tempBookingfclUnits.setMarkUp(fclBuyCostTypeRates.getMarkup());
                                                        if (CommonUtils.isNotEmpty(nonRatesList)) {
                                                            for (Iterator iterator5 = nonRatesList.iterator(); iterator5.hasNext();) {
                                                                NonRates nonRates = (NonRates) iterator5.next();
                                                                if (nonRates.getChargeCode().equals(fclBuyCost.getCostId().getCode())) {
                                                                    if (nonRates.getAddsub().equals("A")) {
                                                                        bookingfclUnits.setMarkUp(bookingfclUnits.getMarkUp() + nonRates.getAmount());
                                                                    } else {
                                                                        bookingfclUnits.setMarkUp(bookingfclUnits.getMarkUp() - nonRates.getAmount());
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        if (bookingFCL.getSpotRate().equals("Y")) {
                                                            double spotRate = null != tempBookingfclUnits.getSpotRateAmt() ? tempBookingfclUnits.getSpotRateAmt() : 0d;
                                                            double spotMark = null != tempBookingfclUnits.getSpotRateMarkUp() ? tempBookingfclUnits.getSpotRateMarkUp() : 0d;
                                                            tempBookingfclUnits.setSellRate(spotRate + spotMark);
                                                        } else {
                                                            tempBookingfclUnits.setSellRate(tempBookingfclUnits.getAmount() + bookingfclUnits.getMarkUp());
                                                        }
                                                        tempBookingfclUnits.setAdjustment(0.00);
                                                        tempBookingfclUnits.setStandardCharge(bookingfclUnits.getStandardCharge());
                                                        tempBookingfclUnits.setCurrency(bookingfclUnits.getCurrency());
                                                        tempBookingfclUnits.setCommcode(bookingfclUnits.getCommcode());
                                                        tempBookingfclUnits.setSpecialEquipment(bookingfclUnits.getSpecialEquipment());
                                                        tempBookingfclUnits.setSpecialEquipmentUnit(bookingfclUnits.getSpecialEquipmentUnit());
                                                        tempBookingfclUnits.setEfectiveDate(fclBuyCost.getEffectiveDate());
                                                        tempBookingfclUnits.setAccountName(ssline);
                                                        tempBookingfclUnits.setAccountNo(sscode);
                                                        bookingFclRatesList.add(tempBookingfclUnits);
                                                        flag = true;
                                                        temp = bookingfclUnits.getUnitType().getCode();
                                                    }
                                                }
                                            } else if (fclBuyCost.getContType() != null && fclBuyCost.getContType().getCodedesc().equals("PER BL CHARGES")) {
                                                BookingfclUnits tempBookingfclUnits = new BookingfclUnits();
                                                tempBookingfclUnits.setChgCode(fclBuyCost.getCostId().getCodedesc());
                                                tempBookingfclUnits.setChargeCodeDesc(fclBuyCost.getCostId().getCode());
                                                tempBookingfclUnits.setCostType(fclBuyCost.getContType().getCodedesc());
                                                tempBookingfclUnits.setAmount(fclBuyCostTypeRates.getRatAmount());
                                                tempBookingfclUnits.setMarkUp(fclBuyCostTypeRates.getMarkup());
                                                if (CommonUtils.isNotEmpty(nonRatesList)) {
                                                    for (Iterator iterator = nonRatesList.iterator(); iterator.hasNext();) {
                                                        NonRates nonRates = (NonRates) iterator.next();
                                                        if (nonRates.getChargeCode().equals(fclBuyCost.getCostId().getCode())) {
                                                            if (nonRates.getAddsub().equals("A")) {
                                                                bookingfclUnits.setMarkUp(bookingfclUnits.getMarkUp() + nonRates.getAmount());
                                                            } else {
                                                                bookingfclUnits.setMarkUp(bookingfclUnits.getMarkUp() - nonRates.getAmount());
                                                            }
                                                        }
                                                    }
                                                }
                                                if (bookingFCL.getSpotRate().equals("Y")) {
                                                    double spotRate = null != tempBookingfclUnits.getSpotRateAmt() ? tempBookingfclUnits.getSpotRateAmt() : 0d;
                                                    double spotMark = null != tempBookingfclUnits.getSpotRateMarkUp() ? tempBookingfclUnits.getSpotRateMarkUp() : 0d;
                                                    tempBookingfclUnits.setSellRate(spotRate + spotMark);
                                                } else {
                                                    tempBookingfclUnits.setSellRate(tempBookingfclUnits.getAmount() + bookingfclUnits.getMarkUp());
                                                }
                                                tempBookingfclUnits.setAdjustment(0.00);
                                                tempBookingfclUnits.setStandardCharge(fclBuyCostTypeRates.getStandard());
                                                if (null != fclBuyCostTypeRates.getCurrency() && CommonUtils.isNotEmpty(fclBuyCostTypeRates.getCurrency().getCode())) {
                                                    tempBookingfclUnits.setCurrency(fclBuyCostTypeRates.getCurrency().getCode());
                                                } else {
                                                    tempBookingfclUnits.setCurrency("USD");
                                                }
                                                tempBookingfclUnits.setCommcode(fclBuy.getComNum().getId());
                                                tempBookingfclUnits.setEfectiveDate(fclBuyCost.getEffectiveDate());
                                                tempBookingfclUnits.setAccountName(ssline);
                                                tempBookingfclUnits.setAccountNo(sscode);
                                                otherChargesList.add(tempBookingfclUnits);
                                                flag = true;
                                            }
                                        }
                                    }
                                }
                            }
                            if (!flag) {
                                for (Iterator iterator2 = fclBuyCost.getFclBuyUnitTypesSet().iterator(); iterator2.hasNext();) {
                                    FclBuyCostTypeRates fclBuyCostTypeRates = (FclBuyCostTypeRates) iterator2.next();
                                    if (fclBuyCostTypeRates.getEffectiveDate() != null && bookingFcl.getQuoteDate().before(fclBuyCostTypeRates.getEffectiveDate()) && fclBuyCostTypeRates.getEffectiveDate().before(new Date())) {
                                        if (unitHashMap.containsKey(fclBuyCostTypeRates.getUnitType().getCodedesc())) {
                                            BookingfclUnits orgBookingfclUnits = unitHashMap.get(fclBuyCostTypeRates.getUnitType().getCodedesc());
                                            BookingfclUnits bookingfclUnits = new BookingfclUnits();
                                            bookingfclUnits.setApproveBl(orgBookingfclUnits.getApproveBl());
                                            bookingfclUnits.setUnitType(fclBuyCostTypeRates.getUnitType());
                                            bookingfclUnits.setNumbers(orgBookingfclUnits.getNumbers());
                                            bookingfclUnits.setChgCode(fclBuyCost.getCostId().getCodedesc());
                                            bookingfclUnits.setChargeCodeDesc(fclBuyCost.getCostId().getCode());
                                            bookingfclUnits.setCostType(fclBuyCost.getContType().getCodedesc());
                                            bookingfclUnits.setAmount(fclBuyCostTypeRates.getActiveAmt());
                                            bookingfclUnits.setMarkUp(fclBuyCostTypeRates.getMarkup());
                                            if (CommonUtils.isNotEmpty(nonRatesList)) {
                                                for (Iterator iterator = nonRatesList.iterator(); iterator.hasNext();) {
                                                    NonRates nonRates = (NonRates) iterator.next();
                                                    if (nonRates.getChargeCode().equals(fclBuyCost.getCostId().getCode())) {
                                                        if (nonRates.getAddsub().equals("A")) {
                                                            bookingfclUnits.setMarkUp(bookingfclUnits.getMarkUp() + nonRates.getAmount());
                                                        } else {
                                                            bookingfclUnits.setMarkUp(bookingfclUnits.getMarkUp() - nonRates.getAmount());
                                                        }
                                                    }
                                                }
                                            }
                                            if (bookingFCL.getSpotRate().equals("Y")) {
                                                double spotRate = null != bookingfclUnits.getSpotRateAmt() ? bookingfclUnits.getSpotRateAmt() : 0d;
                                                double spotMark = null != bookingfclUnits.getSpotRateMarkUp() ? bookingfclUnits.getSpotRateMarkUp() : 0d;
                                                bookingfclUnits.setSellRate(spotRate + spotMark);
                                            } else {
                                                bookingfclUnits.setSellRate(bookingfclUnits.getAmount() + bookingfclUnits.getMarkUp());
                                            }
                                            bookingfclUnits.setAdjustment(0.00);
                                            bookingfclUnits.setStandardCharge(orgBookingfclUnits.getStandardCharge());
                                            bookingfclUnits.setCurrency(orgBookingfclUnits.getCurrency());
                                            bookingfclUnits.setCommcode(orgBookingfclUnits.getCommcode());
                                            bookingfclUnits.setSpecialEquipment(orgBookingfclUnits.getSpecialEquipment());
                                            bookingfclUnits.setSpecialEquipmentUnit(orgBookingfclUnits.getSpecialEquipmentUnit());
                                            bookingfclUnits.setEfectiveDate(fclBuyCost.getEffectiveDate());
                                            bookingfclUnits.setAccountName(ssline);
                                            bookingfclUnits.setAccountNo(sscode);
                                            bookingFclRatesList.add(bookingfclUnits);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        for (Iterator iterator = bookingFclRatesList.iterator(); iterator.hasNext();) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) iterator.next();
            bookingfclUnits.setBookingNumber(bookingFcl.getBookingId().toString());
            bookingFclUnitsDAO.update(bookingfclUnits);
        }
        for (Iterator iterator = otherChargesList.iterator(); iterator.hasNext();) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) iterator.next();
            bookingfclUnits.setBookingNumber(bookingFcl.getBookingId().toString());
            bookingFclUnitsDAO.update(bookingfclUnits);

        }

    }

    public boolean findConvertedRecords(String fileno, boolean module) throws Exception {
        FclBl fclBl = new FclBlDAO().getFileNoObject(fileno.toString());
        BookingFcl bookingFcl = new BookingFclDAO().getFileNoObject(fileno);
        if (module) {
            if (fclBl != null) {
                return false;
            } else if (bookingFcl != null) {
                return false;
            } else {
                return true;
            }
        } else {
            if (fclBl != null) {
                return false;
            } else {
                return true;
            }
        }
    }

    public String deleteAllExistingChargesAndHazmatDetails(String quotationNo) throws Exception {
        //----DELETING THE HAZMAT DETAILS-----
        hazmatMaterialDAO.deleteHazmat(quotationNo);
        //----DELETING THE CHARGES DETAILS-----
        return chargesDAO.deleteAllCharges(Integer.parseInt(quotationNo));
    }

    public BookingFcl setTruckerInfo(Quotation quote, BookingFcl bookingFcl) throws Exception {
        String vendorNo = "";
        List<Charges> fclRates = (List<Charges>) chargesDAO.getChargesforQuotationForSettingTrucker(quote.getQuoteId());
        for (Charges charges : fclRates) {
            if (null != charges.getChgCode() && charges.getChargeCodeDesc().equalsIgnoreCase("INLAND")) {
                if (null != charges.getAccountNo() && !"".equals(charges.getAccountNo())) {
                    vendorNo = charges.getAccountNo();
                }
            }
        }
        if (!"".equalsIgnoreCase(vendorNo)) {
            CustAddress custAddress = null;
            CustAddressDAO custAddressDAO = new CustAddressDAO();
            List<CustAddress> custAddressList = custAddressDAO.getCustAddress(vendorNo);
            if (!custAddressList.isEmpty()) {
                custAddress = custAddressList.get(0);
                bookingFcl.setName(custAddress.getAcctName());
                bookingFcl.setTruckerCode(custAddress.getAcctNo());
                bookingFcl.setAddress(custAddress.getAddress1());
                bookingFcl.setTruckerCity(custAddress.getCity1());
                bookingFcl.setTruckerState(custAddress.getState());
                bookingFcl.setTruckerZip(custAddress.getZip());
                bookingFcl.setTruckerPhone(custAddress.getPhone());
                bookingFcl.setTruckerEmail(custAddress.getEmail1());
            }
        }
        return bookingFcl;
    }

    public List<Charges> cheackChargeCode(String columnName, String columnValue, String secondColumnName, Integer quoteId) throws Exception {
        return chargesDAO.findByPropertyUsingQuoteId(columnName, columnValue, secondColumnName, quoteId);
    }

    public List getQuatationInfo(int quoteId) {
        String sql = "SELECT  "
                + " q.file_no , "
                + " q.Quote_ID,"
                + " b.BookingId,"
                + " f.Bol, "
                + " p.id AS processId, "
                + " b.booked_by, "
                + " b.BookingDate, "
                + " f.bl_by, "
                + " f.Bol_date, "
                + " COUNT(d.Operation), "
                + " h.docType_code,  "
                + " u.login_name,  "
                + "p.process_info_date"
                + " FROM quotation q  "
                + " LEFT JOIN booking_fcl b ON q.file_no = b.file_no "
                + " LEFT JOIN process_info p ON p.record_id = q.file_no "
                + " LEFT JOIN fcl_bl f ON q.file_no = f.file_no  "
                + " LEFT JOIN hazmat_material h ON h.BolId = q.quote_id  "
                + " LEFT JOIN document_store_log d ON d.document_ID = q.file_no AND d.screen_Name = 'FCLFILE' AND d.operation IN ('Scan','Attach') "
                + " LEFT JOIN user_details u ON u.user_id = p.user_id "
                + " WHERE q.quote_id = '" + quoteId + "' group by q.file_no";
        SQLQuery query = quotationDAO.getCurrentSession().createSQLQuery(sql);
        return query.list();
    }

    public String getEmailToDisplayINReport(String code, String issuingTerminal, String unlocCode) throws Exception {
        String email = quotationDAO.getEmailToDisplayINReport(code, issuingTerminal, unlocCode);
        if (!CommonFunctions.isNotNull(email)) {
            email = quotationDAO.getEmailToDisplayForPortZero(code, issuingTerminal, QuotationConstants.PORT);
        }
        return email;
    }

    public List<CustomerContact> getCodeCContactList(String bol) throws Exception {
        List<CustomerContact> list = getListOfPartyDetails(bol);
        List<CustomerContact> contactList = new ArrayList<CustomerContact>();
        for (CustomerContact contact : list) {
            if (null != contact.getCodec() && CommonUtils.isNotEmpty(contact.getCodec().getCode())) {
                contactList.add(contact);
            }
        }
        return contactList;
    }

    public List<CustomerContact> getListOfPartyDetails(String quoteId) throws Exception {
        TradingPartnerBC tradingPartnerBC = new TradingPartnerBC();
        List<CustomerContact> custContactList = new ArrayList<CustomerContact>();
        Map<String, CustomerContact> custContactMap = new HashMap<String, CustomerContact>();
        if (CommonFunctions.isNotNull(quoteId)) {
            Quotation quotation = quotationDAO.findById(Integer.parseInt(quoteId));
            if (CommonFunctions.isNotNull(quotation.getClientnumber())) {
                //client
                tradingPartnerBC.getCustomerContactList(custContactMap, quotation.getClientnumber());
            }
            if (CommonFunctions.isNotNull(quotation.getAgentNo())) {
                //agent
                tradingPartnerBC.getCustomerContactList(custContactMap, quotation.getAgentNo());
            }
            Set<String> set = custContactMap.keySet();
            for (String keyValue : set) {
                custContactList.add(custContactMap.get(keyValue));
            }
            //Order contact by First name
            custContactMap = new HashMap<String, CustomerContact>();
            for (CustomerContact contact : custContactList) {
                custContactMap.put(contact.getAccountName() + "-" + contact.getFirstName() + "-" + contact.getEmail(), contact);
            }
            TreeSet<String> keys = new TreeSet<String>(custContactMap.keySet());
            custContactList = new ArrayList<CustomerContact>();
            for (String key : keys) {
                custContactList.add(custContactMap.get(key));
            }
        }
        return custContactList;
    }

    public String fetchRegionRemarks(String destination, String moduleName) throws Exception {
        String unlocCode = StringFormatter.orgDestStringFormatter(destination);
        String region = new PortsDAO().getRegionBasedOnDest(unlocCode);
        int codeTypeId = 19;// region remarks
        String regionRemarks = new GenericCodeDAO().getRegionRemarks(codeTypeId, region, moduleName);
        return null != regionRemarks ? regionRemarks : "";

    }

    public Map<String, Double> getOFRAmountConvertToMap(String inputString) throws Exception {
        // it wil get inputString value A=20--169.800)B=40--300.00 ETC
        // In this method we are tokenzing and adding into map String,Double String=B=40,Double=300.00;
        Map<String, Double> map = new HashMap();
        if (CommonFunctions.isNotNull(inputString)) {
            String[] firstToken = StringUtils.splitByWholeSeparator(inputString, ")");
            for (int i = 0; i < firstToken.length; i++) {
                String[] secondToken = StringUtils.splitByWholeSeparator(firstToken[i], "--");
                if (secondToken.length > 0) {
                    String value = secondToken[secondToken.length - 1];
                    map.put(secondToken[0], new Double(value.replace(",", "")));
                }
            }
        }
        return map;
    }
    
    public Map<String, Double> getOcnfrtMultiQuote(List<MultiChargesOrderBean> multiQuotecharges) throws Exception {
        Map<String, Double> map = new HashMap();
        for (MultiChargesOrderBean bean : multiQuotecharges) {
            map.put(bean.getUnitNo(), bean.getAmount().doubleValue());
        }
        return map;
    }
    
    public Map<String, Double> getBundleOcnfrtMultiQuote(List<MultiChargesOrderBean> multiQuotecharges) throws Exception {
        Map<String, Double> map = new HashMap();
        for (MultiChargesOrderBean bean : multiQuotecharges) {
            if (bean.getChargeCode().equalsIgnoreCase("OCNFRT")) {
                map.put(bean.getUnitNo(), bean.getAmount().doubleValue());
            }
        }
        return map;
    }

    public void getOFRUpdatedAmount(List<FclBuyCostTypeRates> OFRcollapsList, Map updatedOFRAmountMap) throws Exception {
        String unitName = "";
        //OFRcollapsList ;-- wil have fclbuyRatesObject and updatedOFRAmountMap wil have update ocean fright rates for all containers
        if (CommonFunctions.isNotNullOrNotEmpty(OFRcollapsList) && updatedOFRAmountMap != null) {
            for (FclBuyCostTypeRates fclBuyCostTypeRates : OFRcollapsList) {
                if (!unitName.equalsIgnoreCase(fclBuyCostTypeRates.getUnitname())) {
                    Set set = updatedOFRAmountMap.keySet();
                    for (Iterator iterator = set.iterator(); iterator.hasNext();) {
                        String key = (String) iterator.next();
                        if (key.equalsIgnoreCase(fclBuyCostTypeRates.getUnitname())) {
                            Double OFROldAmount = (Double) updatedOFRAmountMap.get(key);
                            Double differenceAmount = fclBuyCostTypeRates.getActiveAmt() - fclBuyCostTypeRates.getOldAmount();
                            Double markUpAmount = fclBuyCostTypeRates.getMarkup() - fclBuyCostTypeRates.getOldMarkUp();
                            fclBuyCostTypeRates.setOldAmount(OFROldAmount);
                            fclBuyCostTypeRates.setActiveAmt(OFROldAmount + differenceAmount + markUpAmount);
                        }
                    }
                }
                unitName = fclBuyCostTypeRates.getUnitname();
            }
        }
    }

    public List orderExpandList(List ratesList) throws Exception {
        TreeMap<String, Charges> map = new TreeMap<String, Charges>();
        List resultList = new ArrayList();
        for (Iterator it = ratesList.iterator(); it.hasNext();) {
            Charges charges = (Charges) it.next();
            String flag = "";
            String unitFlag = "";
            String standardFlag = "";
            GenericCode genericCode = new GenericCodeDAO().findById(Integer.parseInt(charges.getUnitType()));
            if ("Y".equalsIgnoreCase(charges.getStandardCharge())) {
                standardFlag = "0";
            } else {
                if (null != charges.getStandardCharge()) {
                    standardFlag = charges.getStandardCharge();
                } else {
                    standardFlag = "9";
                }
            }
            if (CommonUtils.isNotEmpty(charges.getSpecialEquipmentUnit())) {
                unitFlag = charges.getSpecialEquipmentUnit();
                if ("OCNFRT".equals(charges.getChargeCodeDesc()) || "OFIMP".equals(charges.getChargeCodeDesc())) {
                    flag = "BAAA";
                } else if (CommonUtils.isNotEmpty(charges.getChargeFlag())) {
                    flag = "B" + charges.getChargeFlag();
                } else if (charges.getChargeCodeDesc().startsWith("INT")) {
                    if (charges.getChargeCodeDesc().equals("INTRAMP")) {
                        flag = "BAZ";
                    } else if (charges.getChargeCodeDesc().startsWith("INT")) {
                        flag = "BAZZ";
                    }
                } else {
                    flag = "BA";
                }
            } else {
                if (null != genericCode) {
                    unitFlag = genericCode.getCode() + genericCode.getCode();
                }
                if ("OCNFRT".equals(charges.getChargeCodeDesc()) || "OFIMP".equals(charges.getChargeCodeDesc())) {
                    flag = "AAAA";
                } else if (CommonUtils.isNotEmpty(charges.getChargeFlag())) {
                    flag = "A" + charges.getChargeFlag();
                } else if (charges.getChargeCodeDesc().startsWith("INT")) {
                    if (charges.getChargeCodeDesc().equals("INTRAMP")) {
                        flag = "AAZ";
                    } else if (charges.getChargeCodeDesc().startsWith("INT")) {
                        flag = "AAZZ";
                    }
                } else if (charges.getChargeCodeDesc().startsWith("CA")) {
                    flag = "CC";
                } else {
                    flag = "AA";
                }
            }
            map.put(charges.getUnitType() + standardFlag + flag + unitFlag + charges.getChargeCodeDesc(), charges);
        }
        Set keySet = map.keySet();
        for (Iterator it = keySet.iterator(); it.hasNext();) {
            String key = (String) it.next();
            resultList.add(map.get(key));
        }
        return resultList;
    }

    public List orderNonRatedList(List ratesList) throws Exception {
        TreeMap<String, Charges> map = new TreeMap<String, Charges>();
        List resultList = new ArrayList();
        for (Iterator it = ratesList.iterator(); it.hasNext();) {
            Charges charges = (Charges) it.next();
            String flag = "";
            String unitFlag = "";
            String standardFlag = "";
            GenericCode genericCode = new GenericCodeDAO().findById(Integer.parseInt(charges.getUnitType()));
            if ("Y".equalsIgnoreCase(charges.getStandardCharge())) {
                standardFlag = "0";
            } else {
                if (null != charges.getStandardCharge()) {
                    standardFlag = charges.getStandardCharge();
                } else {
                    standardFlag = "9";
                }
            }
            if (CommonUtils.isNotEmpty(charges.getSpecialEquipmentUnit())) {
                unitFlag = charges.getSpecialEquipmentUnit();
                if ("OCNFRT".equals(charges.getChargeCodeDesc())) {
                    flag = "BAAA";
                } else if (CommonUtils.isNotEmpty(charges.getChargeFlag())) {
                    flag = "B" + charges.getChargeFlag();
                } else {
                    flag = "BA";
                }
            } else {
                if (null != genericCode) {
                    unitFlag = genericCode.getCode() + genericCode.getCode();
                }
                if ("OCNFRT".equals(charges.getChargeCodeDesc())) {
                    flag = "AAAA";
                } else if (CommonUtils.isNotEmpty(charges.getChargeFlag())) {
                    flag = "A" + charges.getChargeFlag();
                } else {
                    flag = "AA";
                }
            }
            map.put(charges.getUnitType() + standardFlag + flag + unitFlag + charges.getChargeCodeDesc() + "" + charges.getId(), charges);
        }
        Set keySet = map.keySet();
        for (Iterator it = keySet.iterator(); it.hasNext();) {
            String key = (String) it.next();
            resultList.add(map.get(key));
        }
        return resultList;
    }

    public String getIsuingTerminalEmail(String quoteId) throws Exception {
        if (CommonUtils.isNotEmpty(quoteId)) {
            Quotation quotation = new QuotationDAO().findById(Integer.parseInt(quoteId));
            if ("Y".equalsIgnoreCase(quotation.getChangeIssuingTerminal())) {
                User quoteBy = new UserDAO().findUserName(quotation.getQuoteBy());
                if (null != quoteBy.getTerminal() && null != quoteBy.getTerminal().getTerminalLocation() && !quotation.getIssuingTerminal().contains(quoteBy.getTerminal().getTerminalLocation())) {
                    String terminal = quotation.getIssuingTerminal().substring(quotation.getIssuingTerminal().indexOf("-") + 1);
                    if (null != terminal) {
                        RefTerminal refTerminal = new RefTerminalDAO().findById(terminal);
                        if (null != refTerminal) {
                            return new RetAddDAO().getRetAddEmail(terminal, refTerminal.getGovSchCode());
                        }
                    }
                }
            }
        }
        return null;
    }

    public FclBl convertToArrivalNotice(Quotation quotation, SearchQuotationForm editQuoteForm, User user) throws Exception {
        FclBl fclBl = new FclBl();
        BookingFclBC bookingFclBC = new BookingFclBC();
        CustAddressBC custAddressBC = new CustAddressBC();
        int index = 0;
        String address = "", clientType = "", acctNo = "", acctName = "";
        List addressList = null;
        CustAddress custAddress = new CustAddress();
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm a");
        Date date = new Date();
        fclBl.setBlBy(user.getLoginName());
        String bolDate = dateTimeFormat.format(date);
        fclBl.setBolDate(dateTimeFormat.parse(bolDate));
        fclBl.setRampCheck(quotation.getRampCheck());
        fclBl.setAutoDeductFFCom(quotation.getDeductFfcomm());
        fclBl.setRatesRemarks(quotation.getRatesRemarks());
        fclBl.setBillingTerminal(quotation.getIssuingTerminal());
        fclBl.setDomesticRouting(bookingFclBC.getIssuingTM(quotation.getIssuingTerminal()));
        fclBl.setAgent(quotation.getAgent());
        fclBl.setDoorOfOrigin(quotation.getDoorOrigin());
        fclBl.setDoorOfDestination(quotation.getDoorDestination());
        fclBl.setAgentNo(quotation.getAgentNo());
        fclBl.setZip(quotation.getZip());
        fclBl.setFileType(quotation.getImportFlag());
        fclBl.setLineMove(quotation.getTypeofMove());
        fclBl.setDefaultAgent(quotation.getDefaultAgent());
        fclBl.setHouseBl("C-Collect");
        fclBl.setFileType("I");
        fclBl.setBillToCode("C");
        fclBl.setImportFlag("I");
        fclBl.setStreamShipBl(null);
        fclBl.setSsBldestinationChargesPreCol(null);
        String ssl = quotation.getSslname();
        if (CommonUtils.isNotEmpty(ssl)) {
            index = ssl.indexOf("//");
        }
        if (index != -1 && index != 0) {
            fclBl.setSslineName(ssl.substring(0, index));
            fclBl.setSslineNo(ssl.substring(index + 2, ssl.length()));
        }
        fclBl.setFileNo(quotation.getFileNo().toString());
        fclBl.setFileNumber(quotation.getFileNo().toString());
        fclBl.setQuuoteNo(quotation.getQuoteNo());
        if (CommonUtils.isNotEmpty(quotation.getClienttype())) {
            clientType = quotation.getClienttype();
            acctNo = quotation.getClientnumber();
            acctName = quotation.getClientname();
            address = custAddressBC.getCompleteShipperAddress(acctNo);
            if (clientType.contains(QuotationConstants.SHIPPER)) {
                fclBl.setShipperNo(acctNo);
                fclBl.setShipperName(acctName);
                fclBl.setShipperAddress(address);
            }
            if (clientType.contains(QuotationConstants.CONSIGNEE)) {
                fclBl.setConsigneeNo(acctNo);
                fclBl.setConsigneeName(acctName);
                fclBl.setConsigneeAddress(address);
            }
            if (clientType.contains(QuotationConstants.VENDOR_FORWARDER)) {
                fclBl.setForwardAgentNo(acctNo);
                fclBl.setForwardingAgentName(acctName);
                fclBl.setForwardingAgent(address);
            }
        }
        fclBl.setMaster("No");
        if (CommonUtils.isNotEmpty(quotation.getClientnumber())) {
            TradingPartner tradingPartner = new TradingPartnerBC().findTradingPartnerById(quotation.getClientnumber());
            if (null != tradingPartner) {
                if (null != tradingPartner.getNotifyParty()) {
                    fclBl.setNotifyPartyName(tradingPartner.getNotifyParty());
                    fclBl.setNotifyCheck("on");
                    fclBl.setStreamshipNotifyParty(custAddressBC.concatNotifyPartyAddress(tradingPartner));
                }
            }
        }
        fclBl.setPortOfLoading(quotation.getPlor());
        fclBl.setMoveType(quotation.getTypeofMove());
        fclBl.setRampCity(quotation.getRampCity());
        fclBl.setNoOfDays(quotation.getNoOfDays());
        if (null != quotation.getRoutedAgentCheck() && "yes".equalsIgnoreCase(quotation.getRoutedAgentCheck())) {
            fclBl.setRoutedByAgent(quotation.getRoutedbymsg());
        }
        fclBl.setRoutedByAgentCountry(quotation.getRoutedbyAgentsCountry());
        fclBl.setDestRemarks(editQuoteForm.getRemarks());
        fclBl.setCommodityCode(editQuoteForm.getCommcode());
        fclBl.setZip(quotation.getZip());
        fclBl.setRoutedAgentCheck(quotation.getRoutedAgentCheck());
        fclBl.setBreakBulk(quotation.getBreakBulk());
        fclBl.setHazmat(quotation.getHazmat());
        fclBl.setRatesNonRates(quotation.getRatesNonRates());
        fclBl.setCostOfGoods(quotation.getCostofgoods());
        fclBl.setInsurance(quotation.getInsurance());
        fclBl.setInsuranceRate(quotation.getInsurancamt());
        fclBl.setLocalDrayage(quotation.getLocaldryage());
        //-----TO GET RELEASECLAUSE BASED ON DESTINATION------
        String destination = quotation.getDestination_port();
        if (null != destination && destination.lastIndexOf("(") != -1 && destination.lastIndexOf(")") != -1) {
            destination = destination.substring(destination.lastIndexOf("(") + 1, destination.lastIndexOf(")"));
        }
        GenericCode genericCode = new FCLPortConfigurationDAO().getReleaseClause(destination);
        if (null != genericCode) {
            fclBl.setFclBLClause(genericCode.getCode());
            fclBl.setClauseDescription(genericCode.getCodedesc());
        }//--ends--
        String agentNumber = quotation.getAgentNo();
        TradingPartner tradingPartner = null;
        List<TradingPartner> portsList = portsDAO.getPortsofDefaultValues(destination, agentNumber);
        if (!portsList.isEmpty()) {
            tradingPartner = portsList.get(0);
        }
        if (null != tradingPartner && CommonUtils.isNotEmpty(tradingPartner.getAccountno())) {
            CustomerAddress customerAddress = custAddressDAO.findByAgentName(tradingPartner.getAccountno());
            if (null != customerAddress) {
                if (null != customerAddress.getAcctname()) {
                    fclBl.setHouseConsigneeName(customerAddress.getAcctname());
                }
                if (null != customerAddress.getAccountNo()) {
                    fclBl.setHouseConsignee(customerAddress.getAccountNo());
                    tradingPartner = new TradingPartnerBC().findTradingPartnerById(customerAddress.getAccountNo());
                    if (null != tradingPartner) {
                        if (null != tradingPartner.getNotifyParty()) {
                            fclBl.setHouseNotifyPartyName(tradingPartner.getNotifyParty());
                            fclBl.setMasterNotifyCheck("on");
                            fclBl.setHouseNotifyParty(custAddressBC.concatNotifyPartyAddress(tradingPartner));
                        }
                    }
                }
                // house conginee address checking.....................................
                address = custAddressBC.getCompleteAddress(customerAddress.getAccountNo());
                fclBl.setHouseConsigneeAddress(address);
            }
        } else if (quotation.getAgentNo() != null && !quotation.getAgentNo().equals("")) {
            CustomerAddress customerAddress = custAddressDAO.findByAgentName(quotation.getAgentNo());
            if (customerAddress != null) {
                if (customerAddress.getAcctname() != null) {
                    fclBl.setHouseConsigneeName(customerAddress.getAcctname());
                }
                if (customerAddress.getAccountNo() != null) {
                    fclBl.setHouseConsignee(customerAddress.getAccountNo());
                    tradingPartner = new TradingPartnerBC().findTradingPartnerById(customerAddress.getAccountNo());
                    if (null != tradingPartner) {
                        if (null != tradingPartner.getNotifyParty()) {
                            fclBl.setHouseNotifyPartyName(tradingPartner.getNotifyParty());
                            fclBl.setMasterNotifyCheck("on");
                            fclBl.setHouseNotifyParty(custAddressBC.concatNotifyPartyAddress(tradingPartner));
                        }
                    }
                }
                // house conginee address checking.....................................
                address = custAddressBC.getCompleteAddress(customerAddress.getAccountNo());
                fclBl.setHouseConsigneeAddress(address);
            }
        }

        if (quotation.getOrigin_terminal() != null && !quotation.getOrigin_terminal().trim().equals("")) {
            fclBl.setTerminal(quotation.getOrigin_terminal());
        }
        if (CommonUtils.isNotEmpty(quotation.getDestination_port())) {
            fclBl.setFinalDestination(quotation.getDestination_port());
        }
        fclBl.setPortofDischarge(quotation.getFinaldestination());

        //--TO SET GT-NEXUS OR INTRA BASED ON SSLNAME----------
        if (quotation.getSsline() != null && !quotation.getSsline().trim().equals("")) {
            String accountNumber = quotation.getSsline();
            List carrier = custAddressDAO.findBy1(null, accountNumber, null, null);
            if (carrier != null && carrier.size() > 0) {
                CustAddress c1 = (CustAddress) carrier.get(0);
                fclBl.setSslineName(c1.getAcctName());
                fclBl.setSslineNo(c1.getAcctNo());
                GeneralInformation generalInformation = new GeneralInformationDAO().getGeneralInformationByAccountNumber(c1.getAcctNo());
                if (generalInformation != null && generalInformation.getShippingCode() != null && !generalInformation.getShippingCode().equalsIgnoreCase("N")) {
                    fclBl.setFclInttgra(generalInformation.getShippingCode());
                } else {
                    fclBl.setFclInttgra("");
                }
            }
        }//--ends---
        addressList = custAddressDAO.getPrimaryCustomerAddress(quotation.getClientname());
        for (Iterator iter = addressList.iterator(); iter.hasNext();) {
            custAddress = (CustAddress) iter.next();
            fclBl.setBillThirdPartyAddress(custAddress.getAddress1());
        }
        Notes notes = new Notes();
        notes.setModuleId("FILE");
        notes.setUpdateDate(new Date());
        notes.setNoteTpye("auto");
        notes.setNoteDesc("Import Quote has been converted to FCLBL");
        notes.setUpdatedBy(user.getLoginName());
        notes.setModuleRefId(fclBl.getFileNo());
        notesDAO.save(notes);
        return fclBl;
    }

    public List<FclBlContainer> getContainerList(Quotation quotation) throws Exception {
        FclBlContainer fclBlContainer = new FclBlContainer();
        List<FclBlContainer> fclBlContainerList = new ArrayList();
        List<Charges> groupChargesList = new ArrayList<Charges>();
        Map<String, String> selectedUnitIdsMap = getSelectedUnitTypeIds(quotation.getSelectedUnits());
        if (CommonUtils.isNotEmpty(selectedUnitIdsMap)) {
            for (Map.Entry<String, String> entry : selectedUnitIdsMap.entrySet()) {
                if (entry.getKey().contains("Y")) {
                    groupChargesList.addAll(chargesDAO.getGroupChargesByUnitType(quotation.getQuoteId(), entry.getValue(), "Y"));
                } else {
                    groupChargesList.addAll(chargesDAO.getGroupChargesByUnitType(quotation.getQuoteId(), entry.getValue(), entry.getKey()));
                }
            }
        }
        if ("R".equalsIgnoreCase(quotation.getRatesNonRates()) || "N".equalsIgnoreCase(quotation.getRatesNonRates())) {
            for (Charges groupCharges : groupChargesList) {
                if (null != groupCharges.getNumber()) {
                    for (int i = Integer.parseInt(groupCharges.getNumber()); i >= 1; i--) {
                        if (groupCharges.getUnitType() != null) {
                            fclBlContainer = new FclBlContainer();
                            fclBlContainer.setSizeLegend(genericCodeDAO.findById(Integer.parseInt(groupCharges.getUnitType())));
                            fclBlContainer.setSpecialEquipment(groupCharges.getSpecialEquipment());
                            fclBlContainerList.add(fclBlContainer);
                        }
                    }
                }
            }
        }
        return fclBlContainerList;
    }

    public List<Charges> getChargesList(int quoteId) throws Exception {
        List<Charges> chargesList = chargesDAO.getCharges(quoteId);
        String tempUnitType = "";
        int i = 0;
        int j = 0;
        LinkedList<Charges> linkedList = new LinkedList<Charges>();
        if (!chargesList.isEmpty()) {
            for (Charges charges : chargesList) {
                if (!tempUnitType.equals(charges.getUnitType())) {
                    j = i;
                }
                if (charges.getChargeCodeDesc().equals("OCNFRT") || charges.getChargeCodeDesc().equals("OFIMP")) {
                    linkedList.add(j, charges);
                } else {
                    linkedList.add(charges);
                }
                tempUnitType = charges.getUnitType();
                i++;
            }
        }
        return linkedList;
    }

    public List<FclBlCharges> convertToChargesList(Quotation quotation, List<Charges> chargesList) throws Exception {
        FclBlCharges fclBlCharges = new FclBlCharges();
        Map<String, FclBlCharges> fclBlChargesMap = new HashMap<String, FclBlCharges>();
        Map<String, String> selectedUnitIds = getSelectedUnitTypeIds(quotation.getSelectedUnits());
        chargesList = new ArrayList<Charges>();
        if (CommonUtils.isNotEmpty(selectedUnitIds)) {
            for (Map.Entry<String, String> entry : selectedUnitIds.entrySet()) {
                if (entry.getKey().contains("Y")) {
                    chargesList.addAll(chargesDAO.getChargesByUnitType(quotation.getQuoteId(), entry.getValue(), "Y"));
                } else {
                    chargesList.addAll(chargesDAO.getChargesByUnitType(quotation.getQuoteId(), entry.getValue(), entry.getKey()));
                }
            }
        }
        chargesList.addAll(chargesDAO.getOtherCharges(quotation.getQuoteId()));
        if (!chargesList.isEmpty()) {
            for (Charges charges : chargesList) {
                fclBlCharges.setReadOnlyFlag("on");
                if (charges != null && !("PER BL CHARGES".equalsIgnoreCase(charges.getCostType()))) {
                    if (fclBlChargesMap.containsKey(charges.getChargeCodeDesc())) {
                        fclBlCharges = (FclBlCharges) fclBlChargesMap.get(charges.getChargeCodeDesc());
                        double amount = fclBlCharges.getAmount() != null ? fclBlCharges.getAmount() : 0.00;
                        Integer number = charges.getNumber() != null ? new Integer(
                                charges.getNumber()) : 1;
                        if (!"new".equals(charges.getNewFlag()) && (!"M".equalsIgnoreCase(charges.getChargeFlag()))) {
                            amount += (charges.getAmount() != null ? charges.getAmount() : 0.00) * number;
                        }
                        amount += (charges.getMarkUp() != null ? charges.getMarkUp() : 0.00) * number;
                        amount += (charges.getAdjestment() != null ? charges.getAdjestment() : 0.00) * number;
                        fclBlCharges.setAmount(amount);
                        fclBlCharges.setOldAmount(amount);
                        if (fclBlCharges.getChargesRemarks() != null && !fclBlCharges.getChargesRemarks().equalsIgnoreCase("")) {
                            fclBlCharges.setChargesRemarks(fclBlCharges.getChargesRemarks());
                        } else {
                            fclBlCharges.setChargesRemarks(charges.getComment());
                        }
                        fclBlCharges.setBookingFlag("D".equals(charges.getNewFlag()) ? "new" : charges.getNewFlag());
                    } else {
                        fclBlCharges.setChargeCode(charges.getChargeCodeDesc());
                        fclBlCharges.setCharges(charges.getChgCode());
                        if (charges.getAmount() != null) {
                            Integer number = charges.getNumber() != null ? new Integer(
                                    charges.getNumber()) : 1;
                            double amount = 0.0;
                            if ("M".equalsIgnoreCase(charges.getChargeFlag())) {
                                fclBlCharges.setReadOnlyFlag(null);
                                amount += (charges.getMarkUp() != null ? charges.getMarkUp() : 0.00) * number;
                            } else {
                                amount += (charges.getAmount() != null ? charges.getAmount() : 0.00) * number;
                                amount += (charges.getMarkUp() != null ? charges.getMarkUp() : 0.00) * number;
                                amount += (charges.getAdjestment() != null ? charges.getAdjestment() : 0.00) * number;
                            }
                            fclBlCharges.setAmount(amount);
                            fclBlCharges.setOldAmount(amount);
                        }
                        if (charges.getCurrecny() != null) {
                            fclBlCharges.setCurrencyCode(charges.getCurrecny());
                        }
                        if (fclBlCharges.getChargesRemarks() != null && !fclBlCharges.getChargesRemarks().equalsIgnoreCase("")) {
                            fclBlCharges.setChargesRemarks(fclBlCharges.getChargesRemarks());
                        } else {
                            fclBlCharges.setChargesRemarks(charges.getComment());
                        }
                        fclBlCharges.setBillTo("Consignee");
                        fclBlCharges.setPrintOnBl("Yes");
                        fclBlCharges.setPcollect("prepaid");
                        fclBlCharges.setBookingFlag("D".equals(charges.getNewFlag()) ? "new" : charges.getNewFlag());
                        fclBlChargesMap.put(fclBlCharges.getChargeCode(), fclBlCharges);
                    }
                    fclBlCharges = new FclBlCharges();
                } else if (charges != null && "PER BL CHARGES".equalsIgnoreCase(charges.getCostType())) {
                    if (fclBlChargesMap.containsKey(charges.getChargeCodeDesc())) {
                        fclBlCharges = (FclBlCharges) fclBlChargesMap.get(charges.getChargeCodeDesc());
                        double amount = fclBlCharges.getAmount() != null ? fclBlCharges.getAmount() : 0.00;
                        fclBlCharges.setChargeCode(charges.getChargeCodeDesc());
                        fclBlCharges.setCharges(charges.getChgCode());
                        if (fclBlCharges.getChargesRemarks() != null && !fclBlCharges.getChargesRemarks().equalsIgnoreCase("")) {
                            fclBlCharges.setChargesRemarks(fclBlCharges.getChargesRemarks());
                        } else {
                            fclBlCharges.setChargesRemarks(charges.getComment());
                        }
                        if (charges.getCurrecny() != null) {
                            fclBlCharges.setCurrencyCode(charges.getCurrecny());
                        }
                        fclBlCharges.setBillTo("Consignee");
                        fclBlCharges.setPrintOnBl("Yes");
                        fclBlCharges.setPcollect("prepaid");
                        if (!"new".equals(charges.getNewFlag())) {
                            amount += (charges.getAmount() != null ? charges.getAmount() : 0.00);
                        }
                        amount += (charges.getMarkUp() != null ? charges.getMarkUp() : 0.00);
                        amount += (charges.getAdjestment() != null ? charges.getAdjestment() : 0.00);
                        fclBlCharges.setAmount(amount);
                        if (CommonUtils.isEqual(charges.getNewFlag(), "new")) {
                            fclBlCharges.setOldAmount(charges.getMarkUp());
                        }
                        fclBlCharges.setOldAmount(charges.getAmount() + charges.getMarkUp());
                        fclBlCharges.setBookingFlag("D".equals(charges.getNewFlag()) ? "new" : charges.getNewFlag());
                    } else {
                        fclBlCharges = new FclBlCharges();
                        fclBlCharges.setChargeCode(charges.getChargeCodeDesc());
                        fclBlCharges.setCharges(charges.getChgCode());
                        if (fclBlCharges.getChargesRemarks() != null && !fclBlCharges.getChargesRemarks().equalsIgnoreCase("")) {
                            fclBlCharges.setChargesRemarks(fclBlCharges.getChargesRemarks());
                        } else {
                            fclBlCharges.setChargesRemarks(charges.getComment());
                        }
                        if (charges.getCurrecny() != null) {
                            fclBlCharges.setCurrencyCode(charges.getCurrecny());
                        }
                        fclBlCharges.setBillTo("Consignee");
                        fclBlCharges.setPrintOnBl("Yes");
                        fclBlCharges.setPcollect("prepaid");
                        double amount = 0.0;
//                        if (!"new".equals(charges.getNewFlag())) {
//                            amount += (charges.getAmount() != null ? charges.getAmount() : 0.00);
//                        }
                        amount += (charges.getMarkUp() != null ? charges.getMarkUp() : 0.00);
                        amount += (charges.getAdjestment() != null ? charges.getAdjestment() : 0.00);
                        fclBlCharges.setAmount(amount);
                        if (CommonUtils.isEqual(charges.getNewFlag(), "new")) {
                            fclBlCharges.setOldAmount(charges.getMarkUp());
                        }
                        fclBlCharges.setOldAmount(charges.getAmount() + charges.getMarkUp());
                        fclBlCharges.setBookingFlag("D".equals(charges.getNewFlag()) ? "new" : charges.getNewFlag());
                        fclBlChargesMap.put(fclBlCharges.getChargeCode(), fclBlCharges);
                    }
                }
            }
        }
        return new ArrayList<FclBlCharges>(fclBlChargesMap.values());
    }

    public List<FclBlCostCodes> convertToCostCodesList(Quotation quotation, List<Charges> chargesList, MessageResources messageResources) throws Exception {
        FclBlCostCodes fclBlCostCodes = new FclBlCostCodes();
        Map<String, FclBlCostCodes> fclBlCostCodesMap = new HashMap<String, FclBlCostCodes>();
        Map<String, String> selectedUnitIds = getSelectedUnitTypeIds(quotation.getSelectedUnits());
        chargesList = new ArrayList<Charges>();
        if (CommonUtils.isNotEmpty(selectedUnitIds)) {
            for (Map.Entry<String, String> entry : selectedUnitIds.entrySet()) {
                if (entry.getKey().contains("Y")) {
                    chargesList.addAll(chargesDAO.getChargesByUnitType(quotation.getQuoteId(), entry.getValue(), "Y"));
                } else {
                    chargesList.addAll(chargesDAO.getChargesByUnitType(quotation.getQuoteId(), entry.getValue(), entry.getKey()));
                }
            }
        }
        chargesList.addAll(chargesDAO.getOtherCharges(quotation.getQuoteId()));
        if (!chargesList.isEmpty()) {
            for (Charges charges : chargesList) {
                if (CommonFunctions.isNotNull(charges.getAccountNo())) {
                    Integer number = 1;
                    double amount = 0.0;
                    number = charges.getNumber() != null ? new Integer(charges.getNumber()) : 1;
                    fclBlCostCodes.setReadOnlyFlag("on");
                    if (charges != null && !charges.getCostType().equalsIgnoreCase("PER BL CHARGES")) {
                        if (fclBlCostCodesMap.containsKey(charges.getChargeCodeDesc() + "-" + charges.getAccountName())) {
                            fclBlCostCodes = (FclBlCostCodes) fclBlCostCodesMap.get(charges.getChargeCodeDesc() + "-" + charges.getAccountName());
                            amount += (charges.getAmount() != null) ? charges.getAmount() * number : 0.0 * number;
                            if (fclBlCostCodes.getAmount() != null) {
                                fclBlCostCodes.setAmount(fclBlCostCodes.getAmount() + amount);
                            }
                            fclBlCostCodes.setCostComments(charges.getComment());
                            fclBlCostCodes.setBookingFlag("D".equals(charges.getNewFlag()) ? "new" : charges.getNewFlag());
                        } else {
                            if (charges.getChargeCodeDesc() != null) {
                                fclBlCostCodes.setCostCode(charges.getChargeCodeDesc());
                            }
                            if (charges.getChgCode() != null) {
                                fclBlCostCodes.setCostCodeDesc(charges.getChgCode());
                            }
                            if ("M".equalsIgnoreCase(charges.getChargeFlag())) {
                                fclBlCostCodes.setReadOnlyFlag(null);
                            }
                            amount += (charges.getAmount() != null) ? charges.getAmount() * number : 0.0 * number;
                            fclBlCostCodes.setAmount(amount);
                            if (charges.getAccountNo() != null) {
                                fclBlCostCodes.setAccNo(charges.getAccountNo());
                            }
                            if (charges.getAccountName() != null) {
                                fclBlCostCodes.setAccName(charges.getAccountName());
                            }
                            if (charges.getCurrecny() != null) {
                                fclBlCostCodes.setCurrencyCode(charges.getCurrecny());
                            }
                            if (charges.getComment() != null) {
                                fclBlCostCodes.setCostComments(charges.getComment());
                            }
                            fclBlCostCodes.setBookingFlag("D".equals(charges.getNewFlag()) ? "new" : charges.getNewFlag());
                            if (charges.getChgCode() != null) {
                                String chargeCode = charges.getChargeCodeDesc();
                                if (chargeCode.equals(messageResources.getMessage("chargeCodeAsPBA"))) {
                                    fclBlCostCodes.setAccNo(quotation.getAgentNo());
                                    fclBlCostCodes.setAccName(quotation.getAgent());
                                } else if (chargeCode.equals(messageResources.getMessage("chargeCodeAsADVSHP"))) {
                                    fclBlCostCodes.setAccNo(quotation.getClientnumber());
                                    fclBlCostCodes.setAccName(quotation.getClientname());
                                }
                            }
                            //--- FFCOMMISSION and PBA CHARGES NOT TO BE ADDED TO THE COSTCODE LIST------
                            if (fclBlCostCodes.getCostCodeDesc().equals(FclBlConstants.ADVANCESURCHARGEDESC) || (fclBlCostCodes.getCostCode().equals("INSURE"))
                                    || (fclBlCostCodes.getCostCode().equals(FclBlConstants.FFCODE) && fclBlCostCodes.getCostCodeDesc().equalsIgnoreCase("FF COMMISSION"))) {
                                //do nothing.
                            } else if (fclBlCostCodes.getAmount().equals(0.0)) {
                                // do nothing.i.e if amount is zero it should not be added to costcode list.
                            } else {
                                fclBlCostCodesMap.put(fclBlCostCodes.getCostCode() + "-" + fclBlCostCodes.getAccName(), fclBlCostCodes);
                            }
                        }
                        fclBlCostCodes = new FclBlCostCodes();
                    } else if (charges != null && charges.getCostType().equalsIgnoreCase("PER BL CHARGES")) {
                        fclBlCostCodes = new FclBlCostCodes();
                        if (charges.getChargeCodeDesc() != null) {
                            fclBlCostCodes.setCostCode(charges.getChargeCodeDesc());
                        }
                        if (charges.getChgCode() != null) {
                            fclBlCostCodes.setCostCodeDesc(charges.getChgCode());
                        }
                        if (charges.getCurrecny() != null) {
                            fclBlCostCodes.setCurrencyCode(charges.getCurrecny());
                        }
                        if (charges.getAccountNo() != null) {
                            fclBlCostCodes.setAccNo(charges.getAccountNo());
                        }
                        if (charges.getAccountName() != null) {
                            fclBlCostCodes.setAccName(charges.getAccountName());
                        }
                        if (charges.getComment() != null) {
                            fclBlCostCodes.setCostComments(charges.getComment());
                        }
                        if (charges.getChgCode() != null) {
                            String chargeCode = charges.getChgCode();
                            if (chargeCode.equals(messageResources.getMessage("chargeCodeAsPBA"))) {
                                fclBlCostCodes.setAccNo(quotation.getAgentNo());
                                fclBlCostCodes.setAccName(quotation.getAgent());
                            } else if (chargeCode.equals(messageResources.getMessage("chargeCodeAsADVSHP"))) {
                                fclBlCostCodes.setAccNo(quotation.getClientnumber());
                                fclBlCostCodes.setAccName(quotation.getClientname());
                            }
                        }
                        fclBlCostCodes.setBookingFlag("D".equals(charges.getNewFlag()) ? "new" : charges.getNewFlag());
                        amount += (charges.getAmount() != null) ? charges.getAmount() : 0.0 * number;
                        fclBlCostCodes.setAmount(amount);
                        if (fclBlCostCodes.getAmount().equals(0.0)) {
                            //--if amount is zero then it should not be added to costcode list--
                        } else if (fclBlCostCodes.getCostCodeDesc().equals(FclBlConstants.ADVANCESURCHARGEDESC)) {
                            //--do nothing i.e PBA charge should not be added to cost code list--
                        } else {
                            fclBlCostCodesMap.put(fclBlCostCodes.getCostCode() + "-" + fclBlCostCodes.getAccName(), fclBlCostCodes);
                        }
                    }
                }
            }
        }
        return new ArrayList<FclBlCostCodes>(fclBlCostCodesMap.values());
    }

    public String fclAutoCostCalculation(Quotation quotation, FclBl fclBl,
            List<FclBlCharges> fclBlChargesList, List<FclBlCostCodes> fclBlCostCodesList) throws Exception {
        String accountNo = "", portDest = "", nonRoutedCode = "", costIdCode = "", message = "Fcl Auto Cost Calculation=", costCode = "";
        Double amt012 = 0.00, amt029 = 0.00, amt001 = 0.00, amt022 = 0.00, amt009 = 0.00, amt005 = 0.00, amt007 = 0.00;
        Double totalAmt = 0.00, profit = 0.00, adminAmt = 0.00, adminTierAmt = 0.00, commissionAmt = 0.00, commissionTierAmt = 0.00;
        boolean flag = false, fclBlUnitsFlag = false;
        String originTerminal = (null != quotation.getOrigin_terminal() ? stringFormatter.findForManagement(quotation.getOrigin_terminal(), null) : "");
        String rampCity = (null != quotation.getRampCity() ? stringFormatter.findForManagement(quotation.getRampCity(), null) : "");
        String portOfLoading = (null != quotation.getPolCheck() ? stringFormatter.findForManagement(quotation.getPolCheck(), null) : "");
        String destination = (null != quotation.getPodCheck() ? stringFormatter.findForManagement(quotation.getPodCheck(), null) : "");
        String portOfDestination = (null != quotation.getDestination_port() ? stringFormatter.findForManagement(quotation.getDestination_port(), null) : "");
        String commodityCode = (null != quotation.getCommcode() ? (quotation.getCommcode().getId().toString()) : "");
        if (null != quotation.getSslname()) {
            String sslName = quotation.getSslname();
            if (sslName.contains("//")) {
                String destinationPort[] = sslName.split("//");
                List<TradingPartnerTemp> tradingPartnerTempList = customerDAO.findForAgenttNo1(destinationPort[0], destinationPort[1]);
                if (!tradingPartnerTempList.isEmpty()) {
                    TradingPartnerTemp tradingPartnerTemp = tradingPartnerTempList.get(0);
                    accountNo = tradingPartnerTemp.getAccountno();
                }
            }
        }
        List<FclBuy> fclBuyList = fclBuyDAO.findForSearchFclBuyRatesForCompressList2(originTerminal, destination, commodityCode, accountNo);
        if (fclBuyList.isEmpty()) {
            fclBuyList = fclBuyDAO.findForSearchFclBuyRatesForCompressList2(rampCity, destination, commodityCode, accountNo);
            if (fclBuyList.isEmpty()) {
                fclBuyList = fclBuyDAO.findForSearchFclBuyRatesForCompressList2(portOfLoading, destination, commodityCode, accountNo);
                if (fclBuyList.isEmpty()) {
                    fclBuyList = fclBuyDAO.findForSearchFclBuyRatesForCompressList2(originTerminal, portOfDestination, commodityCode, accountNo);
                    portDest = portOfDestination;
                    if (fclBuyList.isEmpty()) {
                        fclBuyList = fclBuyDAO.findForSearchFclBuyRatesForCompressList2(rampCity, portOfDestination, commodityCode, accountNo);
                        if (fclBuyList.isEmpty()) {
                            fclBuyList = fclBuyDAO.findForSearchFclBuyRatesForCompressList2(portOfLoading, portOfDestination, commodityCode, accountNo);
                        }
                    }
                }
            }
        }

        if (!fclBuyList.isEmpty()) {
            for (FclBuy fclBuy : fclBuyList) {
                if (null != fclBuy.getEndDate() && null != fclBl.getSailDate() && fclBl.getSailDate().before(fclBuy.getEndDate())) {
                    if (CommonUtils.isNotEmpty(fclBuy.getFclBuyCostsSet())) {
                        Set<FclBuyCost> fclBuyCostSet = fclBuy.getFclBuyCostsSet();
                        for (FclBuyCost fclBuyCost : fclBuyCostSet) {
                            if (null != fclBuyCost.getContType() && "A".equals(fclBuyCost.getContType().getCode())) {
                                if (CommonUtils.isNotEmpty(fclBuyCost.getFclBuyUnitTypesSet())) {
                                    Set<FclBuyCostTypeRates> fclBuyCostTypeRatesSet = fclBuyCost.getFclBuyUnitTypesSet();
                                    for (FclBuyCostTypeRates fclBuyCostTypeRates : fclBuyCostTypeRatesSet) {
                                        if (null != fclBuyCostTypeRates.getUnitType() && (CommonConstants.UNITTYPE_CODES.contains(fclBuyCostTypeRates.getUnitType().getCode()))) {
                                            if (null != fclBuyCost.getCostId()) {
                                                costIdCode = fclBuyCost.getCostId().getCode();
                                                if (costIdCode.equals("012")) {
                                                    amt012 += fclBuyCostTypeRates.getActiveAmt();
                                                }
                                                if (costIdCode.equals("029") && null != quotation.getHazmat() && quotation.getHazmat().equals("Y")) {
                                                    amt029 += fclBuyCostTypeRates.getActiveAmt();
                                                }
                                                if (costIdCode.equals("022") && !costIdCode.equals("029") && !costIdCode.equals("030")) {
                                                    amt001 += fclBuyCostTypeRates.getActiveAmt();
                                                    if (costIdCode.equals("022")) {
                                                        amt022 += fclBuyCostTypeRates.getActiveAmt();
                                                    }
                                                }

                                                if ((costIdCode.equals("005") || costIdCode.equals("007") || costIdCode.equals("009"))) {
                                                    if (null != fclBuyCostTypeRates.getUnitType()) {
                                                        if (null != quotation.getDeductFfcomm() && quotation.getDeductFfcomm().equals("N")
                                                                && null != fclBl.getForwardingAgentName() && !fclBl.getForwardingAgentName().equals("")) {
                                                            if (fclBuyCostTypeRates.getUnitType().getCode().equals("A")) {
                                                                amt005 += 75;
                                                            } else {
                                                                amt005 += 100;
                                                            }
                                                        }
                                                        if (null != fclBl.getBillingTerminal() && fclBl.getBillingTerminal().contains("-")) {
                                                            String issue = fclBl.getBillingTerminal().split("-")[1];
                                                            if ("18".equals(issue)) {
                                                                amt009 += 30;
                                                            } else if (!(CommonConstants.ISSUE_TERMINAL_CODES.contains(issue))) {
                                                                if ("A".equals(fclBuyCostTypeRates.getUnitType().getCode())) {
                                                                    amt009 += 50;
                                                                } else {
                                                                    amt009 += 100;
                                                                }
                                                            }
                                                        }
                                                    }
                                                    if (costIdCode.equals("007")) {
                                                        if (null != portDest) {
                                                            UnLocation unLocation = unLocationDAO.findById(Integer.parseInt(portDest));
                                                            List<Ports> portsList = portsDAO.findForUnlocCodeAndPortName(unLocation.getUnLocationCode(), unLocation.getUnLocationName());
                                                            if (!portsList.isEmpty()) {
                                                                Ports ports = portsList.get(0);
                                                                if (null != ports.getFclPortConfigSet()) {
                                                                    Set<FCLPortConfiguration> fclPortConfigurationSet = ports.getFclPortConfigSet();
                                                                    for (FCLPortConfiguration fclPortConfiguration : fclPortConfigurationSet) {
                                                                        if (null != fclPortConfiguration.getRcomRule() && fclPortConfiguration.getRcomRule().getCode().equals("3")) {
                                                                            if (CommonUtils.isNotEmpty(fclBl.getRoutedByAgent())) {
                                                                                if (null != fclPortConfiguration.getRadmAm()) {
                                                                                    adminAmt += fclPortConfiguration.getRadmAm();
                                                                                }
                                                                                if (null != fclPortConfiguration.getRcomAm()) {
                                                                                    commissionAmt += fclPortConfiguration.getRcomAm();
                                                                                }
                                                                                if (null != fclPortConfiguration.getRcomTierAmt()) {
                                                                                    commissionTierAmt += fclPortConfiguration.getRcomTierAmt();
                                                                                }
                                                                            } else {
                                                                                if (null != fclPortConfiguration.getNadmAm()) {
                                                                                    adminAmt += fclPortConfiguration.getNadmAm();
                                                                                }
                                                                                if (null != fclPortConfiguration.getNadmTierAmt()) {
                                                                                    adminTierAmt += fclPortConfiguration.getNadmTierAmt();
                                                                                }
                                                                                if (null != fclPortConfiguration.getNcomAm()) {
                                                                                    commissionAmt += fclPortConfiguration.getNcomAm();
                                                                                }
                                                                                if (null != fclPortConfiguration.getNcomTierAmt()) {
                                                                                    commissionTierAmt += fclPortConfiguration.getNcomTierAmt();
                                                                                }
                                                                            }
                                                                            if (null != fclPortConfiguration.getNcomRule()) {
                                                                                nonRoutedCode = fclPortConfiguration.getNcomRule().getCode();
                                                                                if (nonRoutedCode.equals("2")) {
                                                                                    amt007 += commissionAmt;
                                                                                }
                                                                                if (nonRoutedCode.equals("1")) {
                                                                                    commissionTierAmt += commissionAmt;
                                                                                }
                                                                                if (fclPortConfiguration.getNadmRule() != null && fclPortConfiguration.getNadmRule().getCode().equals("1")
                                                                                        && nonRoutedCode.equals("3")) {
                                                                                    adminTierAmt += adminAmt;
                                                                                }
                                                                                if (nonRoutedCode.equals("3")
                                                                                        && fclPortConfiguration.getNadmRule() != null && fclPortConfiguration.getNadmRule().getCode().equals("2")) {
                                                                                    totalAmt += adminAmt;
                                                                                }
                                                                                if ((nonRoutedCode.equals("1") || (nonRoutedCode.equals("4")))) {
                                                                                    amt007 += commissionAmt;
                                                                                    if (!flag) {
                                                                                        flag = true;
                                                                                        amt007 += commissionTierAmt;
                                                                                        totalAmt += adminAmt;
                                                                                    }
                                                                                    totalAmt += adminTierAmt;
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }

                                            }
                                            fclBlUnitsFlag = true;
                                            if (null != fclBuyCost.getCostId()) {
                                                costIdCode = fclBuyCost.getCostId().getCode();
                                                if (!costIdCode.equals("001")) {
                                                    profit -= amt001;
                                                }
                                                if (costIdCode.equals("005")) {
                                                    profit -= amt005;
                                                }
                                                if (costIdCode.equals("009")) {
                                                    profit -= amt009;
                                                }
                                                if (costIdCode.equals("012")) {
                                                    profit -= amt012;
                                                }
                                                if (costIdCode.equals("029")) {
                                                    profit -= amt029;
                                                }
                                            }
                                            profit -= totalAmt;
                                            amt007 = profit * commissionAmt;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (fclBlUnitsFlag) {
                for (FclBlCharges fclBlCharges : fclBlChargesList) {
                    if (null != fclBlCharges.getChargeCode()) {
                        if (CommonConstants.CHARGE_CODES.contains(fclBlCharges.getChargeCode())) {
                            profit += fclBlCharges.getAmount();
                        }
                    }
                }
                for (FclBlCostCodes fclBLCostCodes : fclBlCostCodesList) {
                    if (null != fclBLCostCodes.getCostCode()) {
                        if (CommonConstants.COST_CODES.contains(fclBLCostCodes.getCostCode())) {
                            profit -= fclBLCostCodes.getAmount();
                        }
                    }
                }
            }
        }
        for (FclBlCostCodes fclBlCostCodes : fclBlCostCodesList) {
            if (null != fclBlCostCodes.getCostCode()) {
                costCode = fclBlCostCodes.getCostCode();
                if ("001".equals(costCode) && !amt001.equals(0.0) && fclBlCostCodes.getAmount().compareTo(amt001) != 0) {
                    message += "Ocean Freight Amount Changed from " + fclBlCostCodes.getAmount() + " to " + amt001 + " = ";
                }
                if (FclBlConstants.FFCODE.equals(costCode) && !amt005.equals(0.0) && fclBlCostCodes.getAmount().compareTo(amt005) != 0) {
                    message += "FF Commission Amount Changed from " + fclBlCostCodes.getAmount() + " to " + amt005 + " = ";
                }
                if ("009".equals(costCode) && !amt009.equals(0.0) && fclBlCostCodes.getAmount().compareTo(amt009) != 0) {
                    message += "FCL COM Terminal Amount Changed from " + fclBlCostCodes.getAmount() + " to " + amt009 + " = ";
                }
                if ("012".equals(costCode) && !amt012.equals(0.0) && fclBlCostCodes.getAmount().compareTo(amt012) != 0) {
                    message += "DEFERRAL Amount Changed from " + fclBlCostCodes.getAmount() + " to " + amt012 + " = ";
                }
                if ("029".equals(costCode) && !amt029.equals(0.0) && fclBlCostCodes.getAmount().compareTo(amt029) != 0) {
                    message += "Hazardous Changed from " + fclBlCostCodes.getAmount() + " to " + amt029 + " = ";
                }
                if ("022".equals(costCode) && !amt022.equals(0.0) && fclBlCostCodes.getAmount().compareTo(amt022) != 0) {
                    message += "INTERMODAL RAMP Changed from " + fclBlCostCodes.getAmount() + " to " + amt022 + " = ";
                }
                if ("007".equals(costCode) && !amt007.equals(0.0) && fclBlCostCodes.getAmount().compareTo(amt007) != 0) {
                    message += "FCL FAE-COMMISSION Changed from " + fclBlCostCodes.getAmount() + " to " + amt007 + " = ";
                }
            }
        }
        return ("Fcl Auto Cost Calculation=".equalsIgnoreCase(message) ? "" : message);
    }

    public void setRequest(HttpSession session, HttpServletRequest request) throws Exception {
        if (null != session.getAttribute(ImportBc.sessionName)) {
            request.setAttribute("requestObjectVAlue", ItemConstants.FILE_SEARCH_IMPORT);
        } else {
            request.setAttribute("requestObjectVAlue", ItemConstants.FCL_SEARCH_EXPORT);
        }
    }

    public FclBlCostCodes getFFCommissionCostCodes(MessageResources messageResources, List<Charges> chargesList, Quotation quotation) throws Exception {
        String ffCommissionRates[] = LoadLogisoftProperties.getProperty("ffcommissionrates").split(",");
        String uniType[] = messageResources.getMessage("unittype").split(",");
        Integer amount = 0;
        String previousUnitType = "";
        if (chargesList != null) {
            for (Charges charges : chargesList) {
                if (charges.getUnitType() != null) {
                    if (!previousUnitType.equals(charges.getUnitType())) {
                        Integer value;
                        if (charges.getUnitType().equals(uniType[0])) {
                            value = new Integer(ffCommissionRates[0].replace("-", ""));
                        } else {
                            value = new Integer(ffCommissionRates[1].replace("-", ""));
                        }
                        Integer unitSize = null != charges.getNumber() ? new Integer(charges.getNumber()) : 1;
                        amount += (value * unitSize);
                    }
                    previousUnitType = null != quotation.getUnitType() ? quotation.getUnitType() : "";
                }
            }
        }
        FclBlCostCodes fclBlCostCodes = new FclBlCostCodes();
        fclBlCostCodes.setCostCode(FclBlConstants.FFCODE);
        fclBlCostCodes.setCostCodeDesc(FclBlConstants.FFCODEDESC);
        fclBlCostCodes.setAmount(amount.doubleValue());
        fclBlCostCodes.setCurrencyCode("USD");
        fclBlCostCodes.setBookingFlag("M");
        fclBlCostCodes.setReadOnlyFlag("new");
        fclBlCostCodes.setAccName((FclBlConstants.VENDOR_FORWARDER.equalsIgnoreCase(quotation.getClienttype()) ? quotation.getClientname() : ""));
        fclBlCostCodes.setAccNo((FclBlConstants.VENDOR_FORWARDER.equalsIgnoreCase(quotation.getClienttype()) ? quotation.getClientnumber() : ""));
        return fclBlCostCodes;
    }

    public Map<String, String> getSelectedUnitTypeIds(String selectedUnitsDesc) throws Exception {
        Map<String, String> unitsMap = new HashMap<String, String>();
        Map<String, String> unitIdsMap = new HashMap<String, String>();
        String seperatedString = null;
        if (CommonUtils.isNotEmpty(selectedUnitsDesc)) {
            for (String selectedUnit : selectedUnitsDesc.split(",")) {
                if (selectedUnit.contains("-")) {
                    seperatedString = StringUtils.substringBefore(selectedUnit, "-");
                    unitsMap.put(StringUtils.substringAfter(selectedUnit, "-"), seperatedString);
                } else {
                    unitsMap.put(selectedUnit.substring(0, 1) + "Y", selectedUnit);
                }
            }
        }
        for (Map.Entry<String, String> entry : unitsMap.entrySet()) {
            unitIdsMap.put(entry.getKey(), genericCodeDAO.getGenericodeIdList(entry.getValue()));
        }
        return unitIdsMap;
    }

    public void saveNotes(Quotation quotation, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("loginuser");
        List<Charges> hazChargeList = chargesDAO.findByPropertyUsingQuoteId("qouteId", quotation.getQuoteId(), "chargeCodeDesc", "HAZFEE");
        if (null != quotation.getHazmat() && "Y".equalsIgnoreCase(quotation.getHazmat()) && hazChargeList.isEmpty()) {
            Notes notes = new Notes();
            notes.setModuleId("FILE");
            notes.setUpdateDate(new Date());
            notes.setNoteTpye("auto");
            notes.setNoteDesc("File " + quotation.getFileNo() + " is hazardous but does not have hazardous surcharges ");
            notes.setUpdatedBy(user.getLoginName());
            notes.setModuleRefId(quotation.getFileNo());
            new NotesDAO().save(notes);
        }
    }

    public void saveAutoNotes(Quotation quotation, HttpServletRequest request, String Notes) throws Exception {
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("loginuser");
        Notes notes = new Notes();
        notes.setModuleId("FILE");
        notes.setUpdateDate(new Date());
        notes.setNoteTpye("auto");
        notes.setNoteDesc(Notes);
        notes.setUpdatedBy(user.getLoginName());
        notes.setModuleRefId(quotation.getFileNo());
        new NotesDAO().save(notes);
    }

    public void useTrueCost(Quotation quote) throws Exception {
        List<Charges> chargeList = chargesDAO.getAutoRatesList(quote.getQuoteNo());
        for (Charges charge : chargeList) {
            if (charge.getMarkUp() != 0.00) {
                if (CommonUtils.isNotEqual(charge.getChargeCodeDesc(), "SOLFEE")) {
                    charge.setAdjestment(-charge.getMarkUp());
                    chargesDAO.update(charge);
                }
            }
        }
        new NotesBC().deleteAesDetails(quote.getFileNo(), quote.getUpdateBy(), "Use True Cost Changed -> N to Y");
    }

    public void notesForSpotRates(String fileNo, String userName, String spotStatus) throws Exception {
        Quotation quotation = new QuotationDAO().findbyFileNo(fileNo);
        if (spotStatus.equals("Y")) {
            quotation.setSpotRate("Y");
            new NotesBC().deleteAesDetails(fileNo, userName, "Spot Rate Changed from 'N' to 'y'");
        } else {
            quotation.setSpotRate("N");
            new NotesBC().deleteAesDetails(fileNo, userName, "Spot Rate Changed from 'Y' to 'N'");
        }
        new QuotationDAO().update(quotation);
    }
    public List addChassisChargeToBl(List fclRates, Quotation quotation, String vendorName, String vendorAcct, String cost, String sell) throws Exception {
     List chargesList = new ArrayList();
        int no = 0;
        for (int a = 0; a < fclRates.size(); a++) {
            Charges chargeDomain = (Charges) fclRates.get(a);
            Charges cbeanPrev = null;
            if (a != 0) {
                cbeanPrev = (Charges) fclRates.get(a - 1);
                if (null != chargeDomain && null != cbeanPrev && !chargeDomain.getUnitType().equals(cbeanPrev.getUnitType())) {
                    if (cbeanPrev.getNumber() != null) {
                        no = no + Integer.parseInt(cbeanPrev.getNumber());
                    }
                }
            }
        }
        if (fclRates.size() > 0) {
            Charges c1 = (Charges) fclRates.get(fclRates.size() - 1);
            if (c1.getNumber() != null) {
                no = no + Integer.parseInt(c1.getNumber());
            }
        }

        Charges chargeObject = new Charges();
        chargeObject.setCostType("Flat Rate Per Container");
        chargeObject.setChgCode("CHASSIS FEE");
        chargeObject.setChargeCodeDesc("CHASFEE");
        chargeObject.setMarkUp(Double.parseDouble(dbUtil.removeComma(sell)));
        chargeObject.setAmount(Double.parseDouble(dbUtil.removeComma(cost)));
        chargeObject.setEfectiveDate(new Date());
        chargeObject.setCurrecny("USD");
        chargeObject.setPrint("off");
        chargeObject.setUnitType("0.00");
        chargeObject.setChargeFlag("CH");
        chargeObject.setInclude("off");
        chargeObject.setPrint("off");
        chargeObject.setAccountName(vendorName);
        chargeObject.setAccountNo(vendorAcct);


        boolean flag = false;
        for (int i = 0; i < fclRates.size(); i++) {
            Charges c1 = (Charges) fclRates.get(i);
            if (c1.getChgCode().equalsIgnoreCase(chargeObject.getChgCode())) {
                c1.setRetail(chargeObject.getRetail());
                c1.setMarkUp(c1.getRetail());
            }
        }
        List tempList = new ArrayList(fclRates);
        Map<String, String> commentMap = new HashMap<String, String>();
        for (int i = 0; i < tempList.size(); i++) {
            Charges c1 = (Charges) tempList.get(i);
            if (c1.getChgCode().equalsIgnoreCase(chargeObject.getChgCode())) {
                if (CommonUtils.isNotEmpty(c1.getUnitType()) && CommonUtils.isNotEmpty(c1.getComment())) {
                    commentMap.put(c1.getUnitType(), c1.getComment());
                }
//                chargeObject.setComment(c1.getComment());
                fclRates.remove(c1);
                flag = true;
            }
        }
        fclRates = orderExpandList(fclRates);
        Double totalCharges = 0.00;
        String prevUnitValue = "";
        String unitValue = "";
        for (int a = 0; a < fclRates.size(); a++) {
            Charges chargeDom = (Charges) fclRates.get(a);
            if (null == chargeDom.getSpecialEquipmentUnit()) {
                chargeDom.setSpecialEquipmentUnit("");
            }
            if (CommonUtils.isNotEmpty(chargeDom.getUnitType())) {
                unitValue = chargeDom.getUnitType() + "-" + chargeDom.getSpecialEquipmentUnit() + "-" + chargeDom.getStandardCharge();
            }
            Charges cbeanPrev = null;
            if (a != 0) {
                cbeanPrev = (Charges) fclRates.get(a - 1);
                if (null == cbeanPrev.getSpecialEquipmentUnit()) {
                    cbeanPrev.setSpecialEquipmentUnit("");
                }
                if (CommonUtils.isNotEmpty(cbeanPrev.getUnitType())) {
                    prevUnitValue = cbeanPrev.getUnitType() + "-" + cbeanPrev.getSpecialEquipmentUnit() + "-" + cbeanPrev.getStandardCharge();
                }
                if (CommonUtils.isNotEmpty(prevUnitValue) && CommonUtils.isNotEmpty(unitValue) && !prevUnitValue.equals(unitValue)) {
                    Charges c2 = new Charges();
                    if (chargeObject.getUnitType().equals("0.00")) {
                        PropertyUtils.copyProperties(c2, chargeObject);
                        c2.setNumber(cbeanPrev.getNumber());
                        c2.setComment(commentMap.get(cbeanPrev.getUnitType()));
                        c2.setSpecialEquipmentUnit(cbeanPrev.getSpecialEquipmentUnit());
                        c2.setStandardCharge(cbeanPrev.getStandardCharge());
                        c2.setSpecialEquipment(cbeanPrev.getSpecialEquipment());
                        c2.setInclude("on");
                        c2.setPrint("off");
                        if ("M".equalsIgnoreCase(cbeanPrev.getChargeFlag())) {
                            totalCharges += cbeanPrev.getMarkUp() + cbeanPrev.getAdjestment();
                        } else {
                            if (quotation.getSpotRate().equals("Y")) {
                                totalCharges += cbeanPrev.getSpotRateAmt() + cbeanPrev.getMarkUp() + cbeanPrev.getAdjestment();
                            } else {
                                totalCharges += cbeanPrev.getAmount() + cbeanPrev.getMarkUp() + cbeanPrev.getAdjestment();
                            }
                        }
                        c2.setUnitType(cbeanPrev.getUnitType());
                        GenericCode genericCode = genericCodeDAO.findById(Integer.parseInt(c2.getUnitType()));
                        if (genericCode != null) {
                            c2.setUnitName(genericCode.getCodedesc());
                        }
                        quotation.setTotalCharges(totalCharges);
//                        quotation = calculateInsurance(quotation, quotation.getInsurancamt().toString());
                        c2.setAmount(chargeObject.getAmount());
                        c2.setMarkUp(chargeObject.getMarkUp());
                        
                        chargesList.add(c2);
                        totalCharges = 0.00;
                    }
                } else {
                    if ("M".equalsIgnoreCase(cbeanPrev.getChargeFlag())) {
                        totalCharges += cbeanPrev.getMarkUp() + cbeanPrev.getAdjestment();
                    } else {
                        if (quotation.getSpotRate().equals("Y")) {
                            totalCharges += cbeanPrev.getSpotRateAmt() + cbeanPrev.getMarkUp() + cbeanPrev.getAdjestment();
                        } else {
                            totalCharges += cbeanPrev.getAmount() + cbeanPrev.getMarkUp() + cbeanPrev.getAdjestment();
                        }
                    }
                }
            }
            chargesList.add(chargeDom);
        }
        
           if (chargesList.size() > 0) {
            Charges c4 = (Charges) chargesList.get(chargesList.size() - 1);
            Charges c2 = new Charges();
            if (chargeObject.getUnitType().equals("0.00")) {
                PropertyUtils.copyProperties(c2, chargeObject);
                if ("M".equalsIgnoreCase(c4.getChargeFlag())) {
                    totalCharges += c4.getMarkUp() + c4.getAdjestment();
                } else {
                    if (quotation.getSpotRate().equals("Y")) {
                        totalCharges += c4.getSpotRateAmt() + c4.getMarkUp() + c4.getAdjestment();
                    } else {
                        totalCharges += c4.getMarkUp() + c4.getAdjestment();
                    }
                }
                c2.setSpecialEquipmentUnit(c4.getSpecialEquipmentUnit());
                c2.setStandardCharge(c4.getStandardCharge());
                c2.setSpecialEquipment(c4.getSpecialEquipment());
                c2.setNumber(c4.getNumber());
                c2.setInclude("on");
                c2.setPrint("off");
                c2.setUnitType(c4.getUnitType());
                c2.setComment(commentMap.get(c4.getUnitType()));
                quotation.setTotalCharges(totalCharges);
                //c2.setRetail(c2.getRetail()*Double.parseDouble(c2.getNumber())/Double.parseDouble(String.valueOf(no)));
                quotation = calculateInsurance(quotation, quotation.getInsurancamt().toString());
                //c2.setRetail(c2.getRetail());
                c2.setAmount(chargeObject.getAmount());
                // c2.setMarkUp(chargeObject.getAmount());
                if (!"A=20".equalsIgnoreCase(c2.getUnitName())) {
                    double amount = c2.getAmount();
                    double markup = c2.getMarkUp();
                     c2.setAmount(c2.getAmount());
                        //  c2.setMarkUp(c2.getMarkUp() * 2.00);
                    
                }
                GenericCode genericCode = genericCodeDAO.findById(Integer.parseInt(c4.getUnitType()));
                if (genericCode != null) {
                    c2.setUnitName(genericCode.getCodedesc());
                }
                chargesList.add(c2);
            }
        }
        
        return chargesList;
       
    }
       public List deleteChassisCharge(List fclRates) throws Exception {
        List newfclRates = new ArrayList();
        newfclRates.addAll(fclRates);
        for (int i = 0; i < fclRates.size(); i++) {
            Charges costBean = (Charges) fclRates.get(i);
            if (costBean.getChargeCodeDesc().equalsIgnoreCase("CHASFEE")) {
                newfclRates.remove(costBean);
            }
        }
        return newfclRates;

    }

}
