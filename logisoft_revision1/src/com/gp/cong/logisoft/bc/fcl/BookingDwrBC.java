package com.gp.cong.logisoft.bc.fcl;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.bc.notes.NotesBC;
import com.gp.cong.logisoft.bc.referenceDataManagement.WareHouseTempBC;
import com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerBC;
import com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerConstants;
import com.gp.cong.logisoft.domain.CustomerTemp;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.Ports;
import com.gp.cong.logisoft.domain.PortsTemp;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.Warehouse;
import com.gp.cong.logisoft.hibernate.dao.FclBuyCostDAO;
import com.gp.cong.logisoft.hibernate.dao.FclBuyDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.WarehouseDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsDetailDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.logisoft.util.DBUtil;
import com.gp.cong.logisoft.util.StringFormatter;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.BookingFcl;
import com.gp.cvst.logisoft.domain.BookingfclUnits;
import com.gp.cvst.logisoft.domain.CustAddress;
import com.gp.cvst.logisoft.domain.FclBlCostCodes;
import com.gp.cvst.logisoft.domain.Quotation;
import com.gp.cvst.logisoft.hibernate.dao.BookingFclDAO;
import com.gp.cvst.logisoft.hibernate.dao.BookingfclUnitsDAO;
import com.gp.cvst.logisoft.hibernate.dao.HazmatMaterialDAO;
import com.logiware.hibernate.dao.ArRedInvoiceDAO;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.util.MessageResources;
import org.directwebremoting.WebContextFactory;

public class BookingDwrBC {

    public Warehouse getWarehouseAddress(String warehouseName) throws Exception {
        WareHouseTempBC wareHouseTempBC = new WareHouseTempBC();
        Warehouse wareHouse = wareHouseTempBC.getWareHouseAddress(warehouseName);
        return wareHouse;
    }

    public Warehouse getWarehouseAddressById(String id) throws Exception {
        WarehouseDAO warehouseDAO = new WarehouseDAO();
        Warehouse wareHouse = warehouseDAO.findById(Integer.parseInt(id));
        warehouseDAO.getSession().evict(wareHouse);
        wareHouse.setAcCity(null);
        wareHouse.setCityCode(null);
        return wareHouse;
    }

    public String getComcodeDesc(String commodity) throws Exception {
        return new GenericCodeDAO().getCodeDescription("4", commodity);
    }

    public String getComCode(String comDesc) throws Exception {
        return new GenericCodeDAO().getCodeDescription("4", comDesc);
    }

    public CustAddress getCustAddress(String acctName) throws Exception {
        String clientName = acctName.replace("amp;", "").trim();
        CustAddressBC custAddressBC = new CustAddressBC();
        CustAddress custAddress = custAddressBC.getCustInfo(clientName);
        return custAddress;
    }

    public CustAddress getCustAddressForNo(String acctNo) throws Exception {
        CustAddressBC custAddressBC = new CustAddressBC();
        CustAddress custAddress = custAddressBC.getCustInfoForCustNo(acctNo);
        TradingPartnerBC tradingPartnerBC = new TradingPartnerBC();
        tradingPartnerBC.setSubType(custAddress);
        return custAddress;
    }

    public String getAgent(String finalDest) throws Exception {
        PortsDAO portsDAO = new PortsDAO();
        List portsList = portsDAO.getPortsForAgentInfo(finalDest);
        if (portsList.isEmpty()) {
            return "true";
        } else {
            return "false";
        }
    }

    public CustomerTemp getDestAgent(String destination, boolean importFlag) throws Exception {
        String type = importFlag ? "I" : "F";
        PortsDAO portsDAO = new PortsDAO();
        CustomerTemp customerTemp = new CustomerTemp();
        List portsList = portsDAO.getPortsForAgentInfoWithDefaultForBooking(destination, type);
        if (!portsList.isEmpty()) {
            customerTemp = (CustomerTemp) portsList.get(0);
        }
        return customerTemp;
    }

    public String checkForNonDomesticPort(String code, String desc) throws Exception {
        String type = "";
        PortsDAO portsDAO = new PortsDAO();
        PortsTemp portsTemp = new PortsTemp();
        List portsList = portsDAO.findForUnlocCodeAndPortNameforPortsTemp(code, desc);
        if (portsList.size() > 0) {
            portsTemp = (PortsTemp) portsList.get(0);
            type = portsTemp.getType();
        }
        return type;
    }

    public String etdDateValidation(String selectedDate, String currentDate) throws Exception {
        String returnString = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        Date choosenDate = null;
        Date newDate = null;
        Calendar ca1 = Calendar.getInstance();
        ca1.add(Calendar.DATE, -60);
        if (CommonFunctions.isNotNull(selectedDate)) {
            choosenDate = sdf.parse(selectedDate);
        }
        String compareDate = sdf.format(ca1.getTime());
        newDate = sdf.parse(compareDate);
        int result = choosenDate.compareTo(newDate);
        if (result > 0) {
            returnString = "greater";
        } else if (result < 0) {
            returnString = "lesser";
        } else {
            returnString = "equal";
        }
        return returnString;
    }

    public String dateValidationforImport(String selectedDate, String currentDate) throws Exception {
        String returnString = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date todaysDate = null;
        Date choosenDate = null;
        Date newDate = null;
        if (CommonFunctions.isNotNull(currentDate)) {
            todaysDate = dateFormat.parse(currentDate);
        }
        if (CommonFunctions.isNotNull(currentDate)) {
            newDate = sdf.parse(currentDate); //--format here is (dd-MMM-yyyy).......
        }
        if (null != newDate) {
            todaysDate = newDate;
        }
        if (CommonFunctions.isNotNull(selectedDate)) {
            choosenDate = sdf.parse(selectedDate);
        }
        if (CommonFunctions.isNotNull(todaysDate) && CommonFunctions.isNotNull(choosenDate)) {
            long diff1 = (todaysDate.getTime() - choosenDate.getTime()) / (1000 * 60 * 60 * 24);
            if (diff1 > 60) {
                returnString = "lesser";
            }
        }
        return returnString;
    }

    public String dateValidationforSixMonth(String selectedDate, String currentDate) throws Exception {
        String returnString = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date todaysDate = null;
        Date choosenDate = null;
        Date newDate = null;
        if (CommonFunctions.isNotNull(currentDate)) {
            todaysDate = dateFormat.parse(currentDate);
        }
        if (CommonFunctions.isNotNull(currentDate)) {
            newDate = sdf.parse(currentDate);
        }
        if (null != newDate) {
            todaysDate = newDate;
        }
        if (CommonFunctions.isNotNull(selectedDate)) {
            choosenDate = sdf.parse(selectedDate);
        }
        if (null != todaysDate && !todaysDate.equals("")
                && null != choosenDate && !choosenDate.equals("")) {
            long diff1 = (todaysDate.getTime() - choosenDate.getTime()) / (1000 * 60 * 60 * 24);
            if (diff1 > 180) {
                returnString = "greater";
            } else if (diff1 < -180) {
                returnString = "lesser";
            }
        }
        return returnString;
    }

    public String dateValidation(String selectedDate, String currentDate) throws Exception {
        String returnString = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date todaysDate = null;
        Date choosenDate = null;
        Date newDate = null;
        if (CommonFunctions.isNotNull(currentDate)) {
            todaysDate = dateFormat.parse(currentDate);
        }
        if (CommonFunctions.isNotNull(currentDate)) {
            newDate = sdf.parse(currentDate);//--format here is (dd-MMM-yyyy).......
        }
        if (CommonFunctions.isNotNull(selectedDate)) {
            choosenDate = sdf.parse(selectedDate);
        }
        if (null != newDate) {
            todaysDate = newDate;
        }
        if (null != todaysDate && !todaysDate.equals("")
                && null != choosenDate && !choosenDate.equals("")) {
            int result = choosenDate.compareTo(todaysDate);
            if (result > 0) {
                returnString = "greater";
            } else if (result < 0) {
                returnString = "lesser";
            } else {
                returnString = "equal";
            }
        }
        return returnString;
    }

    public TradingPartner getNvo() throws Exception {
        TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
        return tradingPartnerDAO.getNvoCompanyDetails();
    }

    public String checkForTheRegion(String destination) throws Exception {
        String flag = "";
        String finalDestination = destination;
        String unlocCode = StringFormatter.orgDestStringFormatter(finalDestination);
        if (null != unlocCode && !unlocCode.equals("")) {
            PortsDAO portsDAO = new PortsDAO();
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

    public String checkHazmat(String id) throws Exception {
        String result = "";
        if (id != null && !id.equalsIgnoreCase("")) {
            HazmatMaterialDAO materialDAO = new HazmatMaterialDAO();
            int bolID = Integer.parseInt(id);
            List hazMatList = materialDAO.findbydoctypeid1("Booking", bolID);
            if (hazMatList != null && hazMatList.size() > 0) {
                result = "true";
            } else {
                result = "false";
            }
        }
        return result;
    }

    public String checkForDisable(String accountNo) throws Exception {
        return new TradingPartnerDAO().chekForDisable(accountNo);
    }

    public void clearTradingPartnerSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute("tradingPartnerId") != null) {
            session.removeAttribute("tradingPartnerId");
        }
        if (session.getAttribute(TradingPartnerConstants.TRADINGPARTNER) != null) {
            session.removeAttribute(TradingPartnerConstants.TRADINGPARTNER);
        }
        if (session.getAttribute(TradingPartnerConstants.TRADINGPARTNER_FORM) != null) {
            session.removeAttribute(TradingPartnerConstants.TRADINGPARTNER_FORM);
        }
        if (session.getAttribute(TradingPartnerConstants.VIEW) != null) {
            session.removeAttribute(TradingPartnerConstants.VIEW);
        }
        if (session.getAttribute("tradingPartnerSearchList") != null) {
            session.removeAttribute("tradingPartnerSearchList");
        }
    }

    public boolean rateChangeAlert(String origin, String pol, String pod, String destination,
            String commodityId, String carrier, String quoteDate, String bookingDate,
            String fileNo, String hazmat, HttpServletRequest request) throws Exception {
        boolean result = false;
        MessageResources messageResources = CommonConstants.loadMessageResources();
        GenericCode genericCode = null;
        QuotationBC quotationBC = new QuotationBC();
        FclBuyDAO fclBuyDAO = new FclBuyDAO();
        if (null != commodityId && !commodityId.equals("")) {
            genericCode = quotationBC.findForGenericCode(commodityId);
        }
        String carrier1 = "";
        origin = quotationBC.findForManagement(origin, null);
        destination = quotationBC.findForManagement(destination, null);
        if (carrier != null && carrier.contains("//")) {
            String tempCarrier[] = carrier.split("//");
            carrier1 = tempCarrier[1];
        }
        if (origin != null && destination != null && genericCode != null && carrier1 != null) {
            List otherCommodityList = new FclBuyCostDAO().getOtherCommodity(genericCode.getCode(), null);
            String baseCommodity = genericCode.getId().toString();
            if (CommonUtils.isNotEmpty(otherCommodityList)) {
                for (Object row : otherCommodityList) {
                    Object[] cols = (Object[]) row;
                    baseCommodity = null != cols[3] ? cols[3].toString() : "";
                    break;
                }
            }
            List fclBuyLIst = fclBuyDAO.findRates2(origin.trim(), destination.trim(), baseCommodity.trim(), carrier1, quoteDate, bookingDate);
            if (fclBuyLIst.isEmpty()) {
                result = true;
            }
            if (fclBuyLIst.size() > 0) {
                BookingFclBC bookingFclBC = new BookingFclBC();
                Ports destPort = new PortsDAO().findById(Integer.parseInt(destination));
                String region = null != destPort && null != destPort.getRegioncode() ? destPort.getRegioncode().getField3() : "";
                fclBuyLIst = bookingFclBC.getListOfCharges(fclBuyLIst, messageResources, hazmat, request, fileNo, otherCommodityList, region);
            }
            if (null != request.getAttribute("msg") && request.getAttribute("msg").equals("BkgRates")) {
                result = true;
            }
        } else {
            result = true;
        }

        return result;
    }

    public String checkChargeCode(String bookingId, String chargcodeId, String unitTypeId) throws Exception {
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        BookingfclUnitsDAO bookingfclUnitsDAO = new BookingfclUnitsDAO();
        String resultString = "";
        if (chargcodeId != null && !chargcodeId.equalsIgnoreCase("0") && bookingId != null) {
            GenericCode genericCode = genericCodeDAO.findById(Integer.parseInt(chargcodeId));
            if (genericCode.getCode() != null) {
                if (unitTypeId != null && !unitTypeId.equalsIgnoreCase("0")) {
                    resultString = (CommonFunctions.isNotNullOrNotEmpty(bookingfclUnitsDAO.checkChargeCode(bookingId, genericCode.getCode(), new Integer(unitTypeId), QuotationConstants.COSTTYPE))) ? "true" : "";
                } else {
                    resultString = (CommonFunctions.isNotNullOrNotEmpty(bookingfclUnitsDAO.checkChargeCode(bookingId, genericCode.getCode(), null, null))) ? "true" : "";
                }
            }
        }
        return resultString;
    }

    public String checkExistADVFFandADVSHP(String bookingId) throws Exception {
        BookingfclUnitsDAO bookingfclUnitsDAO = new BookingfclUnitsDAO();
        String returnString = "";
        if (bookingId != null) {
            returnString = CommonFunctions.isNotNullOrNotEmpty(bookingfclUnitsDAO.getADVFFandADVSHPAmount(new Integer(bookingId))) ? "true" : "";
        }
        return returnString;
    }

    public String checkAmountMarkup(String chargeCode) throws Exception {
        String returnString = "", codeTypeId = "36";
        GenericCode genericCode = new GenericCodeDAO().getGenericCodeId(codeTypeId, chargeCode);
        if (CommonFunctions.isNotNull(genericCode.getField7())) {
            returnString = genericCode.getField7();
        }
        return returnString;
    }

    public String getQuoteContactName(String fileNo) throws Exception {
        Quotation quotation = new BookingFclBC().getQuoteByFileNo(fileNo);
        return quotation.getContactname();
    }

    public String getQuoteEmail(String fileNo) throws Exception {
        Quotation quotation = new BookingFclBC().getQuoteByFileNo(fileNo);
        return quotation.getEmail1();
    }

    public String getETADate(String fileNo, String etd) throws Exception {
        Quotation quotation = new BookingFclBC().getQuoteByFileNo(fileNo);
        Date etdDate = DateUtils.parseDate(etd, "dd-MMM-yyyy");
        int daysToAdd = 0;
        if (CommonUtils.isNotEmpty(quotation.getNoOfDays())) {
            daysToAdd = Integer.parseInt(quotation.getNoOfDays());
        }
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(etdDate);
        calendar.add(Calendar.DAY_OF_MONTH, daysToAdd);
        return DateUtils.formatDate(calendar.getTime(), "dd-MMM-yyyy");
    }

    public String getTransitDays(String etd, String eta) throws Exception {
        Date etdDate = DateUtils.parseDate(etd, "MM/dd/yyyy");
        Date etaDate = DateUtils.parseDate(eta, "MM/dd/yyyy");
        long diff = etaDate.getTime() - etdDate.getTime();
        return String.valueOf(diff / (1000 * 60 * 60 * 24));
    }

    public String getInbondForThisBooking(String fclBlId) throws Exception {
        String returnValue = "";
        Integer bookingId = (CommonFunctions.isNotNull(fclBlId)) ? new Integer(fclBlId) : 0;
        BookingFcl BookingFcl = new BookingFclDAO().findById(bookingId);
        if (null != BookingFcl && CommonFunctions.isNotNullOrNotEmpty(BookingFcl.getBookingInbondDetails())) {
            returnValue += "INBOND";
        }
        return returnValue;
    }

    public List getTypeOfMove() throws Exception {
        return new DBUtil().getGenericFCLforTypeOfMove(new Integer(48), "yes", "yes");
    }

    public List getTypeOfMoveForrampCheck() throws Exception {
        return new DBUtil().getGenericFCLforTypeOfMovebooking(new Integer(48), "yes", "yes");
    }

    public List getUnitListForFCLTest1() throws Exception {
        return new DBUtil().getUnitListForFCLTest1(new Integer(41), "yes", "Select Special Equipments");
    }

    public String checkVendorOptional(String bookingNumber) throws Exception {
        String vendorOptional = "";
        String vendorOptionalChargeCode = "";
        List vendorOptionalCheckList = new BookingfclUnitsDAO().getVendorOptionalList(bookingNumber);
        for (Iterator it = vendorOptionalCheckList.iterator(); it.hasNext();) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) it.next();
            if (CommonFunctions.isNotNull(bookingfclUnits.getChargeCodeDesc())) {
                vendorOptional = new GenericCodeDAO().getFieldByCodeAndCodetypeId("Cost Code", bookingfclUnits.getChargeCodeDesc(), "field8");
                if ("N".equalsIgnoreCase(vendorOptional) && !CommonFunctions.isNotNull(bookingfclUnits.getAccountNo())) {
                    if (!vendorOptionalChargeCode.contains(bookingfclUnits.getChargeCodeDesc())) {
                        vendorOptionalChargeCode += bookingfclUnits.getChargeCodeDesc() + ",";
                    }
                }
            }
        }
        return vendorOptionalChargeCode;
    }

    public boolean validateWareHouseCode(String code) throws Exception {
        return new WarehouseDAO().findForWarehouseNoAvailability(code);
    }

    public String getDestCode(String dest) throws Exception {
        String code = "";
        BookingFclDAO bookingFclDAO = new BookingFclDAO();
        code = bookingFclDAO.getDestCode(dest);
        return code;
    }

    public String getDestCodeforHBL(String dest) throws Exception {
        String code = "";
        BookingFclDAO bookingFclDAO = new BookingFclDAO();
        code = bookingFclDAO.getDestCodeforHBL(dest);
        return code;
    }

    public String setVessName(String vessel, String fileNO, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loginuser");
        String userName = user.getLoginName();
        String vesselCode = "";
        BookingFclDAO bookingFclDAO = new BookingFclDAO();
        vesselCode = bookingFclDAO.updateVesselName(vessel, fileNO, userName);
        return vesselCode;

    }

    public void setVoyValue(String ssvoyage, String fileNO) throws Exception {
        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loginuser");
        String userName = user.getLoginName();
        new BookingFclDAO().updateSSVoyValue(ssvoyage, fileNO, userName);
    }

    public String getBlManifestFlag(String fileNO) throws Exception {
        String flag = "";
        BookingFclDAO bookingFclDAO = new BookingFclDAO();
        flag = bookingFclDAO.getmanifestFlag(fileNO);
        return flag;
    }

    public String checkVendorOptionalwithflag(String bookingNumber) throws Exception {
        String vendorOptional = "";
        String vendorOptionalChargeCode = "";
        double sell;
        double adjustment;
        BookingFclDAO bookingFclDAO = new BookingFclDAO();
        List vendorOptionalCheckList = new BookingfclUnitsDAO().getVendorOptionalList(bookingNumber);
        for (Iterator it = vendorOptionalCheckList.iterator(); it.hasNext();) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) it.next();
            sell = bookingfclUnits.getSellRate();
            adjustment = bookingfclUnits.getAdjustment();
            if (CommonFunctions.isNotNull(bookingfclUnits.getChargeCodeDesc())) {
                if ("OCNFRT".equalsIgnoreCase(bookingfclUnits.getChargeCodeDesc()) && sell == 0.00) {
                    String ofrBundle = new BookingfclUnitsDAO().getOfrBundleCharge(bookingNumber);
                    if (!"0.00".equalsIgnoreCase(ofrBundle)) {
                        sell = Double.parseDouble(ofrBundle);
                    }
                }
                vendorOptional = bookingFclDAO.getFieldByCodeAndCodetypeId("Cost Code", bookingfclUnits.getChargeCodeDesc(), "field7", "field8");
                if ("true".equals(vendorOptional)
                        && (sell == 0.0 || sell == 0.00 || sell == 0) && (adjustment == 0.0 || adjustment == 0.00 || adjustment == 0)) {
                    if (!vendorOptionalChargeCode.contains(bookingfclUnits.getChargeCodeDesc())) {
                        vendorOptionalChargeCode += bookingfclUnits.getChargeCodeDesc() + ",";
                    }
                }
            }
        }
        return vendorOptionalChargeCode;
    }

    public String checkVendorOptionalwithflagforCost(String bookingNumber) throws Exception {
        String vendorOptional = "";
        String vendorOptionalChargeCode = "";
        double sell;
        double cost;
        BookingFclDAO bookingFclDAO = new BookingFclDAO();
        List vendorOptionalCheckList = new BookingfclUnitsDAO().getVendorOptionalList(bookingNumber);
        for (Iterator it = vendorOptionalCheckList.iterator(); it.hasNext();) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) it.next();
            if (CommonFunctions.isNotNull(bookingfclUnits) && CommonFunctions.isNotNull(bookingfclUnits.getSellRate()) && CommonFunctions.isNotNull(bookingfclUnits.getAmount())) {
                sell = bookingfclUnits.getSellRate();
                cost = bookingfclUnits.getAmount();
                if (CommonFunctions.isNotNull(bookingfclUnits.getChargeCodeDesc())) {
                    vendorOptional = bookingFclDAO.getFieldByCodeAndCodetypeIdforCost("Cost Code", bookingfclUnits.getChargeCodeDesc(), "field7", "field8");
                    boolean hasCostCode = checkCostCodeInGeneralLedger(bookingfclUnits.getChargeCodeDesc(), "");
                    if (!hasCostCode && "true".equals(vendorOptional) && (cost == 0.0 || cost == 0.00 || cost == 0)) {
                        if (!vendorOptionalChargeCode.contains(bookingfclUnits.getChargeCodeDesc())) {
                            vendorOptionalChargeCode += bookingfclUnits.getChargeCodeDesc() + ",";
                        }
                    }
                }
            }
        }
        return vendorOptionalChargeCode;
    }

    public String setPoa(String code) throws Exception {
        String poa = new BookingFclDAO().getPoa(code);
        return CommonUtils.isNotEmpty(poa) ? poa : "";
    }

    public String getInlandVendor(String bookingId) throws Exception {
        return new BookingfclUnitsDAO().getInlandVendor(bookingId);
    }

    public boolean checkCostCodeInGeneralLedger(String costCode, String shipmentType) throws Exception {
        return new GenericCodeDAO().checkCostCodeInGeneralLedger(costCode, shipmentType);
    }

    public boolean validateSpecialEquipment(String bookingId, String unitType, String desc, String action) throws Exception {
        GenericCode genericCode = new GenericCodeDAO().findByCodeDesc(unitType);
        List chargesList = new ArrayList();
        if (CommonUtils.isNotEmpty(bookingId) && null != genericCode) {
            if ("add".equalsIgnoreCase(action)) {
                chargesList = new BookingfclUnitsDAO().findByPropertyUsingBookingId("specialEquipmentUnit", genericCode.getCode(), "bookingNumber", bookingId);
            } else {
                chargesList = new BookingfclUnitsDAO().getSpecialEquipmentCharges(bookingId, genericCode.getCode(), desc);

            }
        }
        if (!chargesList.isEmpty()) {
            return true;
        }
        return false;
    }

    public String validateCostHasBlueScreenCostCode(String bookingNumber, String shipmentType) throws Exception {
        String costCode = "";
        List list = new BookingfclUnitsDAO().getVendorOptionalList(bookingNumber);
        for (Iterator it = list.iterator(); it.hasNext();) {
            BookingfclUnits bookingfclUnits = (BookingfclUnits) it.next();
            if (CommonFunctions.isNotNull(bookingfclUnits.getAccountName()) && !bookingfclUnits.getAmount().equals(0d)
                    && !"INSURE".equalsIgnoreCase(bookingfclUnits.getChargeCodeDesc()) && !"FFCOMM".equalsIgnoreCase(bookingfclUnits.getChargeCodeDesc())
                    && !"PBASUR ".equalsIgnoreCase(bookingfclUnits.getChargeCodeDesc())) {
                boolean hasCostCode = new GenericCodeDAO().checkCostCodeHasBlueScreenCodeInGeneralLedger(bookingfclUnits.getChargeCodeDesc(), shipmentType);
                if (!hasCostCode) {
                    if (!costCode.contains(bookingfclUnits.getChargeCodeDesc())) {
                        costCode += bookingfclUnits.getChargeCodeDesc() + ",";
                    }
                }
            }
        }
        return costCode;
    }

    public Date dateTimeValidation(String date, String format) {
        try {
            return DateUtils.parseDate(date, format);
        } catch (Exception e) {
            return null;
        }
    }

    public Integer resendToBlueScreen(String fileNo, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginuser");
        if (null != fileNo) {
            new NotesBC().saveNotes(fileNo, loginUser.getLoginName(), "Booking Resent to Bluescreen");
            return new BookingFclDAO().resendToBlueScreen(fileNo);
        }
        return 0;
    }

    public String checkForCommodity(String commcode) throws Exception {
        String flagValue = "false";
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        String field7 = genericCodeDAO.getByCodeAndCodetypeId("4", commcode, "field9");
        if ("Y".equals(field7)) {
            flagValue = "true";
        }
        return flagValue;
    }

    public String inlandCheck(Integer bookingId) throws Exception {
        String inland = "";
        String bookingIds = bookingId.toString();
        BookingFclBC bookingFclBC = new BookingFclBC();
        if (CommonFunctions.isNotNullOrNotEmpty(bookingFclBC.cheackChargeCodeforBooking("chgCode",
                "INLAND", "bookingNumber", bookingIds))) {
            inland = "Y";
        } else {
            inland = "N";
        }
        return inland;
    }

    public String intrmodRampCheck(Integer bookingId) throws Exception {
        String inland = "";
        String bookingIds = bookingId.toString();
        BookingFclBC bookingFclBC = new BookingFclBC();
        if (CommonFunctions.isNotNullOrNotEmpty(bookingFclBC.cheackChargeCodeforBooking("chgCode",
                "INTERMODAL RAMP", "bookingNumber", bookingIds))) {
            inland = "Y";
        } else {
            inland = "N";
        }
        return inland;
    }

    public String saveCommentsAboutAdjustment(String Id, String comments, String adjustAmtValue, String bookingId, String isApplyToallContainerChecked, HttpServletRequest request) throws Exception {
        if (null != Id && !Id.trim().equals("")
                && null != comments
                && !comments.trim().equals("")) {
            User user = (User) request.getSession().getAttribute("loginuser");
            String userName = user.getLoginName();
            DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm a");
            formatter.setTimeZone(TimeZone.getDefault());
            formatter.setLenient(false);
            comments += "(" + userName + "-" + formatter.format(new Date()) + ").";
            double adjustmentAmount = Double.parseDouble(adjustAmtValue);
            if ("true".equalsIgnoreCase(isApplyToallContainerChecked)) {
                BookingfclUnitsDAO bookingfclUnitsDAO = new BookingfclUnitsDAO();
                BookingfclUnits bookingfclUnits = bookingfclUnitsDAO.findById(Integer.parseInt(Id));
                String chargeCodeDesc = bookingfclUnits.getChargeCodeDesc();
                bookingfclUnitsDAO.updateAdjustmentChargeComments(bookingId, chargeCodeDesc, comments, adjustmentAmount);
            } else {
                BookingfclUnitsDAO bookingfclUnitsDAO = new BookingfclUnitsDAO();
                BookingfclUnits bookingfclUnits = bookingfclUnitsDAO.findById(Integer.parseInt(Id));
                bookingfclUnits.setAdjustmentChargeComments(comments);
                bookingfclUnits.setAdjustment(adjustmentAmount);
                bookingfclUnitsDAO.save(bookingfclUnits);
            }
        } else {
            comments = null;
        }
        return comments;
    }

    public void showFclBookingCharges(String bookingId, HttpServletRequest request) throws Exception {
        List<FclBlCostCodes> fclBlCostCodesList = new BookingFclDAO().getBookingAccruals(Integer.parseInt(bookingId));
        request.setAttribute("fclBlCostCodesList", fclBlCostCodesList);
    }

    public String getFclBookingAccruals(String bookingId) throws Exception {
        List<FclBlCostCodes> fclBlCostCodesList = new BookingFclDAO().getBookingAccruals(Integer.parseInt(bookingId));
        String accrualsList = (CommonFunctions.isNotNullOrNotEmpty(fclBlCostCodesList)) ? "data" : "";
        return accrualsList;
    }

    public String getPrepaidCollect(String fileNo) throws Exception {
        BookingFcl bookingFcl = new BookingFclDAO().findbyFileNo(fileNo);
        return bookingFcl.getPrepaidCollect().equals("P") ? "true" : "false";
    }

    public String validateEtdDate(String etd, String eta, String containerCutoff, String docCutoff) throws Exception {
        Date d1 = DateUtils.parseDate(etd, "MM/dd/yyyy");
        Date d2 = CommonUtils.isNotEmpty(eta) ? DateUtils.parseDate(eta, "MM/dd/yyyy") : null;
        Date d3 = CommonUtils.isNotEmpty(containerCutoff) ? DateUtils.parseDate(containerCutoff, "MM/dd/yyyy") : null;
        Date d4 = CommonUtils.isNotEmpty(docCutoff) ? DateUtils.parseDate(docCutoff, "MM/dd/yyyy") : null;
        if (null != d2) {
            if (d1.compareTo(d2) >= 0) {
                return "ETD should be less than ETA";
            }
        }
        if (null != d3) {
            if (d1.compareTo(d3) < 0) {
                return "ETD should be greater than or equal to Container Cut Off";
            }
        }
        if (null != d4) {
            if (d1.compareTo(d4) < 0) {
                return "ETD should be greater than or equal to Doc Cut Off";
            }
        }
        return "";
    }

    public String validateEtaDate(String eta, String etd) throws Exception {
        Date d1 = DateUtils.parseDate(eta, "MM/dd/yyyy");
        Date d2 = new Date();
        Date d3 = DateUtils.parseDate(etd, "MM/dd/yyyy");
        if (d1.compareTo(d2) < 0) {
            return "ETA should not be less than Current Date";
        }
        if (null != d3) {
            if (d1.compareTo(d3) < 0) {
                return "ETA should be greater than ETD";
            }
        }
        return "";
    }

    public String updateEdiStatus(String fileNo, String userName, String statusMsg) throws Exception {
        if (CommonFunctions.isNotNull(fileNo)) {
            Date date = new Date();
            BookingFcl booking = new BookingFclDAO().findbyFileNo(fileNo);
            if (statusMsg.equals("EDI Booking Request sent Successfully") || statusMsg.equals("Amendment EDI Booking Request Sent successfully")) {
                booking.setEdiCreatedBy(userName);
                booking.setEdiCreatedOn(date);
            } else if (statusMsg.equals("Cancel EDI Booking Request Sent successfully")) {
                booking.setEdiCanceledBy(userName);
                booking.setEdiCanceledOn(date);
            }
            new NotesBC().deleteAesDetails(fileNo, userName, statusMsg);
            return DateUtils.formatDate(date, "dd-MMM-yyyy HH:mm");
        }
        return null;
    }

    public String updateAmendmentComments(String fileNo, String comments, String userName) throws Exception {
        if (CommonFunctions.isNotNull(fileNo)) {
            BookingFcl booking = new BookingFclDAO().findbyFileNo(fileNo);
            booking.setReasonForAmending(comments);
            new NotesBC().deleteAesDetails(fileNo, userName, "Reason For Amendment : ".concat(comments));
            return "success";
        }
        return null;
    }

    public void notesForSpotRates(String fileNo, String userName, String spotStatus) throws Exception {
        BookingFcl bookingFcl = new BookingFclDAO().findbyFileNo(fileNo);
        BookingFclDAO bookingFclDAO = new BookingFclDAO();
        if (spotStatus.equals("Y")) {
            BookingfclUnitsDAO bookingfclUnitsDAO = new BookingfclUnitsDAO();
            bookingFcl.setSpotRate("Y");

            List<BookingfclUnits> deferChargeList = bookingfclUnitsDAO.findByChargeCode(bookingFcl.getBookingNumber(), "DEFER");
            for (BookingfclUnits deferCharge : deferChargeList) {
                deferCharge.setStandardChk("on");
                deferCharge.setSpotRateAmt(deferCharge.getAmount());
                deferCharge.setSpotRateMarkUp(deferCharge.getMarkUp());
                deferCharge.setSellRate(deferCharge.getAmount() + (null != deferCharge.getMarkUp() ? deferCharge.getMarkUp() : 0d));
                bookingfclUnitsDAO.update(deferCharge);
            }

            new NotesBC().deleteAesDetails(fileNo, userName.toUpperCase(), "Spot Rate Changed from 'N' to 'Y'");
        } else {
            bookingFcl.setSpotRate("N");
            bookingFclDAO.clearSpotCost(fileNo);
            new NotesBC().deleteAesDetails(fileNo, userName.toUpperCase(), "Spot Rate Changed from 'Y' to 'N'");
        }
        bookingFclDAO.update(bookingFcl);
    }

    public String isContainDeferral(String bookingNo) throws Exception {
        return new BookingfclUnitsDAO().isContainDeferral(bookingNo) ? "DEFERRAL" : "";
    }
      public String checkBrandForDestination(String destination) {
        String brandField = new PortsDAO().brandField(destination);
        return brandField;
    }
    
    public String checkBrandForClient(String acctno){
        String brandField = new TradingPartnerDAO().brandFieldForClient(acctno);
        return brandField;
    }
    public String checkBrandForBooking(String bookingId) {
        String queryString = new BookingfclUnitsDAO().brandValue(bookingId);
        return queryString;
    }
    public String checkLclArInvoiceStatus(String voyageNo) throws Exception {
        BookingFclDAO bookingFclDAO = new BookingFclDAO();
        ArRedInvoiceDAO arRedInvoiceDAO = new ArRedInvoiceDAO();
        StringBuilder sb = new StringBuilder();
        BigInteger ssHeaderId = null;
        List lclFileNumberList = null;
        String fileNumber[] = null;
        String result = "";
        if (CommonUtils.isNotEmpty(voyageNo)) {
            ssHeaderId = bookingFclDAO.getSsHeaderId(voyageNo);
            lclFileNumberList = new BookingFclDAO().getFileNumber(ssHeaderId);
        }
        if (lclFileNumberList != null) {
            Object[] file = (Object[]) lclFileNumberList.get(0);
            if (file[1] != null && !file[1].equals("")) {
                fileNumber = file[1].toString().split(",");
                for (String drNo : fileNumber) {
                    sb.append("'").append(drNo).append("',");
                }
            }
            String groupFileNo = StringUtils.removeEnd(sb.toString(), ",");
            result = arRedInvoiceDAO.getfileNumber(groupFileNo);
        }

        return result;
    }
    public String getCFCLHazmatStatus(String voyageNo) {
        BigInteger ssHeaderId = null;
        BookingFclDAO bookingFclDAO = new BookingFclDAO();
        List<Long> fileIdList = new ArrayList<Long>();
        ssHeaderId = bookingFclDAO.getSsHeaderId(voyageNo);
        if (null != ssHeaderId) {
            fileIdList = bookingFclDAO.getFileId(ssHeaderId);
            if (fileIdList.size() > 0) {
                if (bookingFclDAO.getHazFlag(fileIdList)) {
                    return "Y";
                }
            }
        }
        return "N";
    }
      public String[] checkvendorForChassis(String fileNo) throws Exception{
        BookingfclUnitsDAO bookingfclUnitsDAO = new BookingfclUnitsDAO();
         String[] chargesValues = bookingfclUnitsDAO.checkvendorForChassisCharge(fileNo);
        return chargesValues;
     }
     public String[] getBookingDetails(String bookingId, String voyageNo) throws Exception {
        BigInteger ssHeaderId = null;
        String[] bookingDetils = new String[4];
        BookingFclDAO bookingFclDAO = new BookingFclDAO();
        LclSsDetailDAO lclSsDetailDAO = new LclSsDetailDAO();
        ssHeaderId = bookingFclDAO.getSsHeaderId(voyageNo);
        if (null != ssHeaderId) {
            bookingDetils = lclSsDetailDAO.getVoyageDetails(ssHeaderId.longValue());
        }
        return bookingDetils;
    }
}
