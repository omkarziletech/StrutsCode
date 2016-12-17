package com.gp.cong.logisoft.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpSession;

import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.bc.accounting.GLMappingConstant;
import com.gp.cong.logisoft.bc.fcl.QuotationConstants;
import com.gp.cong.logisoft.bc.scheduler.ProcessInfoBC;
import com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerConstants;
import com.gp.cong.logisoft.beans.CostBean;
import com.gp.cong.logisoft.beans.Rates;
import com.gp.cong.logisoft.domain.AirCommodityCharges;
import com.gp.cong.logisoft.domain.AirStandardCharges;
import com.gp.cong.logisoft.domain.AirWeightRangesRates;
import com.gp.cong.logisoft.domain.AuditLogRecord;
import com.gp.cong.logisoft.domain.AuditLogRecordCustomer;
import com.gp.cong.logisoft.domain.CarrierOceanEqptRates;
import com.gp.cong.logisoft.domain.CarriersOrLineTemp;
import com.gp.cong.logisoft.domain.Country;
import com.gp.cong.logisoft.domain.FTFCommodityCharges;
import com.gp.cong.logisoft.domain.FTFStandardCharges;
import com.gp.cong.logisoft.domain.FclBuyAirFreightCharges;
import com.gp.cong.logisoft.domain.FclBuyCostTypeFutureRates;
import com.gp.cong.logisoft.domain.FclBuyCostTypeRates;
import com.gp.cong.logisoft.domain.FclOrgDestMiscData;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.LCLColoadStandardCharges;
import com.gp.cong.logisoft.domain.LCLPortConfiguration;
import com.gp.cong.logisoft.domain.Ports;
import com.gp.cong.logisoft.domain.PortsTemp;
import com.gp.cong.logisoft.domain.Printer;
import com.gp.cong.logisoft.domain.ProcessInfo;
import com.gp.cong.logisoft.domain.ProcessInfoHistory;
import com.gp.cong.logisoft.domain.RefTerminalTemp;
import com.gp.cong.logisoft.domain.RelayDestination;
import com.gp.cong.logisoft.domain.RelayOrigin;
import com.gp.cong.logisoft.domain.RetailStandardCharges1;
import com.gp.cong.logisoft.domain.SedFilings;
import com.gp.cong.logisoft.domain.TerminalContacts;
import com.gp.cong.logisoft.domain.TradingPartnerTemp;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.UniverseAirFreight;
import com.gp.cong.logisoft.domain.UniverseFlatRate;
import com.gp.cong.logisoft.domain.Usecases;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclSearchTemplate;
import com.gp.cong.logisoft.hibernate.dao.AirAccesorialRatesHistoryDAO;
import com.gp.cong.logisoft.hibernate.dao.AirCommodityChargesHistoryDAO;
import com.gp.cong.logisoft.hibernate.dao.AirRatesHistoryDAO;
import com.gp.cong.logisoft.hibernate.dao.AirStandardChargesDAO;
import com.gp.cong.logisoft.hibernate.dao.AuditLogRecordDAO;
import com.gp.cong.logisoft.hibernate.dao.CarriersOrLineDAO;
import com.gp.cong.logisoft.hibernate.dao.ChangeVoyageDAO;
import com.gp.cong.logisoft.hibernate.dao.ClaimDAO;
import com.gp.cong.logisoft.hibernate.dao.CodetypeDAO;
import com.gp.cong.logisoft.hibernate.dao.CustomerDAO;
import com.gp.cong.logisoft.hibernate.dao.DataExchangeDAO;
import com.gp.cong.logisoft.hibernate.dao.DataExchangeTransactionDAO;
import com.gp.cong.logisoft.hibernate.dao.FTFCommodityChargesHistoryDAO;
import com.gp.cong.logisoft.hibernate.dao.FTFDetailsHistoryDAO;
import com.gp.cong.logisoft.hibernate.dao.FTFMasterDAO;
import com.gp.cong.logisoft.hibernate.dao.FTFStandardChargesHistoryDAO;
import com.gp.cong.logisoft.hibernate.dao.FclBuyDAO;
import com.gp.cong.logisoft.hibernate.dao.FclOrgDestMiscDataDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericcodelabelsDAO;
import com.gp.cong.logisoft.hibernate.dao.ItemDAO;
import com.gp.cong.logisoft.hibernate.dao.LCLColoadCommodityChargesHistoryDAO;
import com.gp.cong.logisoft.hibernate.dao.LCLColoadDetailsHistoryDAO;
import com.gp.cong.logisoft.hibernate.dao.LCLColoadMasterDAO;
import com.gp.cong.logisoft.hibernate.dao.LCLColoadStandardChargesHistoryDAO;
import com.gp.cong.logisoft.hibernate.dao.LClStandardChargesDAO;
import com.gp.cong.logisoft.hibernate.dao.PortExceptionDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.PrinterDAO;
import com.gp.cong.logisoft.hibernate.dao.ProcessInfoDAO;
import com.gp.cong.logisoft.hibernate.dao.ProcessInfoHistoryDAO;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.RelayDestinationDAO;
import com.gp.cong.logisoft.hibernate.dao.RelayInquiryDAO;
import com.gp.cong.logisoft.hibernate.dao.RelayOriginDAO;
import com.gp.cong.logisoft.hibernate.dao.RetailCommodityChargesHistoryDAO;
import com.gp.cong.logisoft.hibernate.dao.RetailOceanDetailsRatesHistoryDAO;
import com.gp.cong.logisoft.hibernate.dao.RetailStandardCharges1HistoryDAO;
import com.gp.cong.logisoft.hibernate.dao.RoleDAO;
import com.gp.cong.logisoft.hibernate.dao.SedFilingsDAO;
import com.gp.cong.logisoft.hibernate.dao.StandardChargesDAO;
import com.gp.cong.logisoft.hibernate.dao.StateDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.hibernate.dao.UniversalMasterDAO;
import com.gp.cong.logisoft.hibernate.dao.UniverseAirFreightHistoryDAO;
import com.gp.cong.logisoft.hibernate.dao.UniverseCommodityChrgHistoryDAO;
import com.gp.cong.logisoft.hibernate.dao.UniverseFlatRateHistoryDAO;
import com.gp.cong.logisoft.hibernate.dao.UniverseInsuranceChrgHistoryDAO;
import com.gp.cong.logisoft.hibernate.dao.UsecasesDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.hibernate.dao.VoyageExportDAO;
import com.gp.cong.logisoft.hibernate.dao.VoyageInlandDAO;
import com.gp.cong.logisoft.hibernate.dao.WarehouseDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSearchTemplateDAO;
import com.gp.cvst.logisoft.beans.SearchQuotationBean;
import com.gp.cvst.logisoft.domain.BookingFcl;
import com.gp.cvst.logisoft.domain.Charges;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.Quotation;
import com.gp.cvst.logisoft.hibernate.dao.ApInvoiceDAO;
import com.gp.cvst.logisoft.hibernate.dao.ArBatchDAO;
import com.gp.cvst.logisoft.hibernate.dao.BookingFclDAO;
import com.gp.cvst.logisoft.hibernate.dao.ChargesDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlDAO;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import com.gp.cvst.logisoft.hibernate.dao.HazmatMaterialDAO;
import com.gp.cvst.logisoft.hibernate.dao.QuotationDAO;
import com.gp.cvst.logisoft.hibernate.dao.SubledgerDAO;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.gp.cvst.logisoft.reports.data.EconoHelper;
import com.gp.cvst.logisoft.reports.dto.QuotationDTO;
import com.gp.cvst.logisoft.reports.dto.ReportDTO;
import org.hibernate.Transaction;

public class DBUtil {

    public static final String Quotation = "Quotation";
    public static final String Booking = "Booking";
    public static final String FclBl = "FclBl";
    public static final String type = "Email";
    public static final String status = "Pending";
    NumberFormat nf = new DecimalFormat("###,###,##0.00");

    public List limitList() {
        List limitList = new ArrayList();
        //limitList.add(new LabelValueBean("All","All"));
        //limitList.add(new LabelValueBean("100","100"));
        limitList.add(new LabelValueBean("250", "250"));
        limitList.add(new LabelValueBean("500", "500"));
        limitList.add(new LabelValueBean("1000", "1000"));
        return limitList;
    }

    public List getGenericCodeList(Integer codeType, String desc, String label) throws Exception {
        List genericCodeList = new ArrayList();
        Iterator iter = new GenericCodeDAO().getAllGenericCodesForDisplay(codeType, desc);
        if (CommonUtils.isNotEmpty(label)) {
            genericCodeList.add(new LabelValueBean(label, "0"));
        }
        if (iter != null) {
            while (iter.hasNext()) {
                Object[] row = (Object[]) iter.next();
                Integer cid = (Integer) row[0];
                String cname = (String) row[1];
                genericCodeList.add(new LabelValueBean(cname, cid.toString()));
            }

        }
        return genericCodeList;
    }

    public List getGenericCodeCodesList(Integer codeTypeId) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();

        Iterator iter = genericCodeDAO.getAllNotesForDisplay2(codeTypeId);

        genericCodeList.add(new LabelValueBean("Select Shipping Code", "0"));
        if (iter != null) {
            while (iter.hasNext()) {
                Object[] row = (Object[]) iter.next();
                String cid = (String) row[0];
                String cname = (String) row[1];
                genericCodeList.add(new LabelValueBean(cid + "-" + cname, cid));
            }

        }

        return genericCodeList;
    }

    public List getGenericCodeListWithDesc(Integer codeType, String code, String desc, String label) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();

        Iterator iter = genericCodeDAO.getAllGenericCostCodesForCorrections(codeType, code, desc);

        genericCodeList.add(new LabelValueBean(label, "0"));
        if (iter != null) {
            while (iter.hasNext()) {
                Object[] row = (Object[]) iter.next();
                Integer cid = (Integer) row[0];
                String code1 = (String) row[1];
                String cname = (String) row[2];
                genericCodeList.add(new LabelValueBean(code1 + "-" + cname, cid.toString()));
            }

        }
        return genericCodeList;
    }

    public List getGenericCodeListWithDescForCorrectionOnly() throws Exception {
        Integer codeType = 52;
        String code = "no", desc = "yes", label = "Select Correction Code";

        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();

        Iterator iter = genericCodeDAO.getAllGenericCostCodesForCorrections(codeType, code, desc);
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer cid = (Integer) row[0];
            String code1 = (String) row[1];
        }
        genericCodeList.add(new LabelValueBean(label, "0"));
        if (iter != null) {
            while (iter.hasNext()) {
                Object[] row = (Object[]) iter.next();
                Integer cid = (Integer) row[0];
                String code1 = (String) row[1];
                String cname = (String) row[2];
                genericCodeList.add(new LabelValueBean(code1 + "-" + cname, cid.toString()));
            }

        }
        return genericCodeList;
    }

    public List getVendortypeIncudingThirdParty() throws Exception {
        List vendortype = new ArrayList();
        vendortype.add(new LabelValueBean("-Select-", "00"));
        vendortype.add(new LabelValueBean("Forwarder", "Forwarder"));
        vendortype.add(new LabelValueBean("Shipper", "Shipper"));
        vendortype.add(new LabelValueBean("ThirdParty", "ThirdParty"));
        return vendortype;
    }

    public List getSortByList() {
        List sortByList = new ArrayList();
        sortByList.add(new LabelValueBean("Account Name", "1"));
        sortByList.add(new LabelValueBean("Account No", "2"));
        sortByList.add(new LabelValueBean("Address", "6"));
        return sortByList;
    }

    public List getMasterSortByList() {
        List sortByList = new ArrayList();
        sortByList.add(new LabelValueBean("Account Name", "1"));
        sortByList.add(new LabelValueBean("Account No", "2"));
        sortByList.add(new LabelValueBean("Address", "5"));
        return sortByList;
    }

    public List getAccountTypeList(String accType, String acc, String label) {
        List accountType = new ArrayList();

        return accountType;
    }

    public List getFCLDetails(UnLocation org, UnLocation des,
            GenericCode com_code, TradingPartnerTemp SSLine, String orgRegion,
            String destRegion) throws Exception {
        List list = new ArrayList();

        FclBuyDAO fclBuyDAO = new FclBuyDAO();
        list = fclBuyDAO.findAllDetails(org, des, com_code, SSLine, orgRegion,
                destRegion);

        return list;

    }

    public List getExportDetailsInland(RefTerminalTemp org, PortsTemp des,
            Integer VoyNo) throws Exception {
        List list = new ArrayList();

        VoyageInlandDAO voyageInlandDAO = new VoyageInlandDAO();
        list = voyageInlandDAO.findAllDetails(org, des, VoyNo);

        return list;

    }

    public List getExportDetails(RefTerminalTemp org, PortsTemp des,
            CarriersOrLineTemp SSLine) throws Exception {
        List list = new ArrayList();

        VoyageExportDAO voyageExportDAO = new VoyageExportDAO();
        list = voyageExportDAO.findAllDetails(org, des, SSLine);

        return list;

    }

    /*
     * public List getUniversalRecords(RefTerminalTemp org,PortsTemp
     * des,GenericCode com_code) { List list=new ArrayList();
     *
     * UniversalMasterDAO universalMasterDAO = new UniversalMasterDAO();
     * list=universalMasterDAO.findAllDetails(org, des, com_code);
     *
     * return list;
     *  }
     */
    // THIS METHODS IS USED FOR RETRIVING RECORDS FOR FCL COST CODE
    public List getGenericCodeCostList(Integer codeType, String desc,
            String label) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();

        Iterator iter = genericCodeDAO.getAllGenericCostCodesForDisplay(
                codeType, desc);

        genericCodeList.add(new LabelValueBean(label, "0"));
        if (iter != null) {
            while (iter.hasNext()) {
                Object[] row = (Object[]) iter.next();
                Integer cid = (Integer) row[0];
                String cname = (String) row[1];
                genericCodeList.add(new LabelValueBean(cname, cid.toString()));
            }
        }

        return genericCodeList;
    }

    public List getTerminalCodeList() throws Exception {
        List terminalCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        Iterator iter = new RefTerminalDAO().getAllTerminalsForDisplay();
        terminalCodeList.add(new LabelValueBean("Select", ""));
        if (iter != null) {
            while (iter.hasNext()) {
                Object[] row = (Object[]) iter.next();
                String cid = (String) row[0];
                String cname = (String) row[1];
                terminalCodeList.add(new LabelValueBean(cid + "-" + cname, cid));
            }
        }

        return terminalCodeList;
    }

    public List getScheduleList() {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();

        // Iterator iter = genericCodeDAO.getAllUserDetails();
        genericCodeList.add(new LabelValueBean("Both", "Both"));
        genericCodeList.add(new LabelValueBean("1st of month", "1st of month"));
        genericCodeList.add(new LabelValueBean("16th of month", "16th of month"));

        return genericCodeList;
    }

    public String removeComma(String amount) {
        if (amount == null || amount.equals("")) {
            amount = "0.00";
        }
        if (amount != null && amount.contains(",")) {
            amount = amount.replace(",", "");
        }
        return amount;
    }

    public String getAmount(String amount) {

        if (amount == null || amount.equals(" ")) {
            amount = "0.00";
        }
        amount = removeComma(amount);
        if (!amount.equals("")) {
            double a1 = Double.parseDouble(amount);
            amount = nf.format(a1);
        }
        return amount;
    }

    public String getMultiPlyAmount(String amt, String numb) {
        if (!amt.trim().equals("")) {
            amt = removeComma(amt);
            double a1 = Double.parseDouble(amt) * Double.parseDouble(numb);
            amt = nf.format(a1);
        }
        return amt;
    }

    public Double getDivideAmount(Double amt, String numb, String number) {
        if (!amt.equals("")) {
            double a1 = amt / Double.parseDouble(numb);
            a1 = a1 * Double.parseDouble(number);
            amt = a1;
        }
        return amt;
    }

    public String getaddAmountMarkUp(String amt, String markUp) {
        if (!amt.trim().equals("") && !markUp.trim().equals("")) {
            amt = removeComma(amt);
            markUp = removeComma(markUp);
            double total = Double.parseDouble(amt) + Double.parseDouble(markUp);
            amt = nf.format(total);
        }
        return amt;
    }

    public List getPaymentList() {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        genericCodeList.add(new LabelValueBean("CHECK", "CHECK"));
        genericCodeList.add(new LabelValueBean("ACH", "ACH"));
        genericCodeList.add(new LabelValueBean("WIRE", "WIRE"));
        genericCodeList.add(new LabelValueBean("ACH DEBIT", "ACH DEBIT"));
        genericCodeList.add(new LabelValueBean("CREDIT CARD", "CREDIT CARD"));

        return genericCodeList;
    }

    public List getPaymentList(String vendorName) throws Exception {
        List genericCodeList = new ArrayList();
        TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
        if (null != vendorName && !vendorName.trim().equals("")) {
            List result = tradingPartnerDAO.getPaymentListByVendorName(vendorName);
            int i = 0;
            for (Object elements : result) {
                genericCodeList.add(new LabelValueBean(elements.toString(), elements.toString()));
            }
        }
        return genericCodeList;
    }

    // METHOD FOR LOADING DATA INTO DATABASE
    public GenericCode getCostID(String codeType, int code) throws Exception {

        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();

        GenericCode genericCode = genericCodeDAO.findByCodeName(codeType,
                new Integer(code));
        return genericCode;
    }

    public GenericCode getContentTypeID(String codeType, int codepe) throws Exception {
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        GenericCode genericCode = genericCodeDAO.findByCodeName(codeType,
                new Integer(codepe));
        return genericCode;
    }

    public List getGenericUnitTypeList(Integer codeType, String desc,
            String label) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();

        Iterator iter = genericCodeDAO.getAllGenericUnitTypeForDisplay(
                codeType, desc);

        genericCodeList.add(new LabelValueBean(label, "0"));
        if (iter != null) {
            while (iter.hasNext()) {
                Object[] row = (Object[]) iter.next();
                Integer cid = (Integer) row[0];
                String cname = (String) row[1];
                genericCodeList.add(new LabelValueBean(cname, cid.toString()));
            }
        }

        return genericCodeList;
    }

    public List getGenericCodetypeList(Integer codeType, String desc,
            String label) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();

        Iterator iter = genericCodeDAO.getAllGenericCosttypeForDisplay(
                codeType, desc);

        genericCodeList.add(new LabelValueBean(label, "0"));
        if (iter != null) {
            while (iter.hasNext()) {
                Object[] row = (Object[]) iter.next();
                Integer cid = (Integer) row[0];
                String cname = (String) row[1];
                genericCodeList.add(new LabelValueBean(cname, cid.toString()));
            }
        }

        return genericCodeList;
    }

    // calling master list from customerDAO.java
    public List getMasterCodeList() throws Exception {
        List masterCodeList = new ArrayList();
        CustomerDAO customerDAO = new CustomerDAO();

        Iterator iter = customerDAO.master1();
        masterCodeList.add(new LabelValueBean("select", null));
        if (iter != null) {
            while (iter.hasNext()) {
                Object[] row = (Object[]) iter.next();
                String cid = (String) row[0];
                String cname = (String) row[1];
                masterCodeList.add(new LabelValueBean(cname, cid));
            }
        }
        return masterCodeList;
    }

    // end
    public List getWeightRangeList(Integer codeType, String desc, String label,
            List weightrangeList) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        StringBuffer cmbselect = new StringBuffer();
        if (weightrangeList != null && weightrangeList.size() > 0) {
            for (int i = 0; i < weightrangeList.size(); i++) {
                AirWeightRangesRates airweight = (AirWeightRangesRates) weightrangeList.get(i);
                if (airweight.getWeightRange() != null) {
                    if (i == (weightrangeList.size() - 1)) {
                        cmbselect.append("'"
                                + airweight.getWeightRange().getId().toString()
                                + "'");

                    } else {
                        cmbselect.append("'"
                                + airweight.getWeightRange().getId().toString()
                                + "',");

                    }
                }
            }

        } else {
            cmbselect.append("'0'");
        }

        Iterator iter = genericCodeDAO.getAllGenericCodesForWeightRange(
                codeType, desc, cmbselect);

        genericCodeList.add(new LabelValueBean(label, "0"));
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer cid = (Integer) row[0];
            String cname = (String) row[1];
            genericCodeList.add(new LabelValueBean(cname, cid.toString()));
        }

        return genericCodeList;

    }

    // FOR FCL WIGTH RANDE
    public List getWeightRangeFCLList(Integer codeType, String desc,
            String label) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        Iterator iter = genericCodeDAO.getAllGenericCodesForFCLWeightRange(
                codeType, desc);
        genericCodeList.add(new LabelValueBean(label, "0"));
        if (iter != null) {
            while (iter.hasNext()) {
                Object[] row = (Object[]) iter.next();
                Integer cid = (Integer) row[0];
                String cname = (String) row[1];
                genericCodeList.add(new LabelValueBean(cname, cid.toString()));
            }
        }

        return genericCodeList;

    }

    // ----------------------
    public List getWeightRangeListforedit(Integer codeType, String desc,
            String label, List weightrangeList, String weight) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        StringBuffer cmbselect = new StringBuffer();
        if (weightrangeList != null && weightrangeList.size() > 0) {

            for (int i = 0; i < weightrangeList.size(); i++) {

                AirWeightRangesRates airweight = (AirWeightRangesRates) weightrangeList.get(i);

                if (!airweight.getWeightRange().getId().toString().equals(
                        weight)) {

                    if (i == (weightrangeList.size() - 1)) {
                        cmbselect.append("'"
                                + airweight.getWeightRange().getId().toString()
                                + "'");

                    } else {
                        cmbselect.append("'"
                                + airweight.getWeightRange().getId().toString()
                                + "',");

                    }
                }

            }

        } else {

            cmbselect.append("'0'");
        }
        if (cmbselect.length() > 0) {
            if (cmbselect.charAt(cmbselect.length() - 1) == ',') {
                cmbselect.deleteCharAt(cmbselect.length() - 1);
            }
        }
        if (!cmbselect.equals("")) {
            cmbselect.append("'0'");
        }

        Iterator iter = genericCodeDAO.getAllGenericCodesForWeightRange(
                codeType, desc, cmbselect);

        genericCodeList.add(new LabelValueBean(label, "0"));
        while (iter.hasNext()) {

            Object[] row = (Object[]) iter.next();
            Integer cid = (Integer) row[0];
            String cname = (String) row[1];
            genericCodeList.add(new LabelValueBean(cname, cid.toString()));
        }

        return genericCodeList;

    }

    public List getWeightFCLList(Integer codeType, String desc, String label,
            List typeList) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        StringBuffer cmbselect = new StringBuffer();
        String code = "11289";
        cmbselect.append("'" + code.toString() + "',");
        if (typeList != null && typeList.size() > 0) {

            for (int i = 0; i < typeList.size(); i++) {

                FclBuyAirFreightCharges airStandardCharges = (FclBuyAirFreightCharges) typeList.get(i);

                if (i == (typeList.size() - 1)) {
                    if (airStandardCharges.getWieghtRange() != null) {
                        cmbselect.append("'"
                                + airStandardCharges.getWieghtRange().getId().toString() + "',");
                    }

                } else {
                    if (airStandardCharges.getWieghtRange() != null) {
                        cmbselect.append("'"
                                + airStandardCharges.getWieghtRange().getId().toString() + "',");
                    }
                }

            }

        }
        cmbselect = cmbselect.append(cmbselect.length() - 1);

        Iterator iter = genericCodeDAO.getAllGenericCodesForUni(codeType, desc,
                cmbselect);

        genericCodeList.add(new LabelValueBean(label, "0"));
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer cid = (Integer) row[0];
            String cname = (String) row[1];
            genericCodeList.add(new LabelValueBean(cname, cid.toString()));
        }

        return genericCodeList;

    }

    public List getTypeList(Integer codeType, String desc, String label,
            List typeList) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        StringBuffer cmbselect = new StringBuffer();
        String code = "11289";
        cmbselect.append("'" + code.toString() + "',");
        if (typeList != null && typeList.size() > 0) {

            for (int i = 0; i < typeList.size(); i++) {

                AirStandardCharges airStandardCharges = (AirStandardCharges) typeList.get(i);

                if (i == (typeList.size() - 1)) {
                    cmbselect.append("'"
                            + airStandardCharges.getChargeType().getId().toString() + "',");

                } else {
                    cmbselect.append("'"
                            + airStandardCharges.getChargeType().getId().toString() + "',");
                }

            }

        }
        cmbselect = cmbselect.append(cmbselect.length() - 1);

        Iterator iter = genericCodeDAO.getAllGenericCodesForAGSS(codeType,
                desc, cmbselect);

        genericCodeList.add(new LabelValueBean(label, "0"));
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer cid = (Integer) row[0];
            String cname = (String) row[1];
            genericCodeList.add(new LabelValueBean(cname, cid.toString()));
        }

        return genericCodeList;

    }

    public List getTypeSSList(Integer codeType, String desc, String label,
            List typeList) throws Exception {

        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        StringBuffer cmbselect = new StringBuffer();
        String code = "11291";
        cmbselect.append("'" + code.toString() + "',");
        if (typeList != null && typeList.size() > 0) {

            for (int i = 0; i < typeList.size(); i++) {

                LCLColoadStandardCharges lCLColoadStandardCharges = (LCLColoadStandardCharges) typeList.get(i);

                if (i == (typeList.size() - 1)) {
                    if (lCLColoadStandardCharges.getChargeType() != null) {
                        cmbselect.append("'"
                                + lCLColoadStandardCharges.getChargeType().getId().toString() + "',");
                    }

                } else {
                    if (lCLColoadStandardCharges.getChargeType() != null) {
                        cmbselect.append("'"
                                + lCLColoadStandardCharges.getChargeType().getId().toString() + "',");
                    }
                }

            }

        }

        cmbselect = cmbselect.append(cmbselect.length() - 1);
        Iterator iter = genericCodeDAO.getAllGenericCodesForAGSS(codeType,
                desc, cmbselect);

        genericCodeList.add(new LabelValueBean(label, "0"));
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer cid = (Integer) row[0];
            String cname = (String) row[1];
            genericCodeList.add(new LabelValueBean(cname, cid.toString()));
        }

        return genericCodeList;

    }

    // for FTF Standard Charges
    public List getTypeFTFList(Integer codeType, String desc, String label,
            List typeList) throws Exception {

        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        StringBuffer cmbselect = new StringBuffer();
        String code = "11291";
        cmbselect.append("'" + code.toString() + "',");

        if (typeList != null && typeList.size() > 0) {

            for (int i = 0; i < typeList.size(); i++) {

                FTFStandardCharges ftfStandardCharges = (FTFStandardCharges) typeList.get(i);

                if (i == (typeList.size() - 1)) {
                    if (ftfStandardCharges.getChargeType() != null) {
                        cmbselect.append("'"
                                + ftfStandardCharges.getChargeType().getId().toString() + "',");
                    }

                } else {
                    if (ftfStandardCharges.getChargeType() != null) {
                        cmbselect.append("'"
                                + ftfStandardCharges.getChargeType().getId().toString() + "',");
                    }
                }

            }

        }

        cmbselect = cmbselect.append(cmbselect.length() - 1);

        Iterator iter = genericCodeDAO.getAllGenericCodesForAGSS(codeType,
                desc, cmbselect);

        genericCodeList.add(new LabelValueBean(label, "0"));
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer cid = (Integer) row[0];
            String cname = (String) row[1];
            genericCodeList.add(new LabelValueBean(cname, cid.toString()));
        }

        return genericCodeList;

    }

    public List getTypeListforagss(Integer codeType, String desc, String label,
            List typeList, String chargeType) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        StringBuffer cmbselect = new StringBuffer();

        if (typeList != null && typeList.size() > 0) {

            for (int i = 0; i < typeList.size(); i++) {

                AirStandardCharges airStandardCharges = (AirStandardCharges) typeList.get(i);

                if (!airStandardCharges.getChargeType().getId().toString().equals(chargeType)) {
                    if (i == (typeList.size() - 1)) {
                        cmbselect.append("'"
                                + airStandardCharges.getChargeType().getId().toString() + "'");

                    } else {
                        cmbselect.append("'"
                                + airStandardCharges.getChargeType().getId().toString() + "',");
                    }
                }

            }

        } else {
            cmbselect.append("'0'");
        }
        if (cmbselect.length() > 0) {
            if (cmbselect.charAt(cmbselect.length() - 1) == ',') {
                cmbselect.deleteCharAt(cmbselect.length() - 1);
            }
        }
        if (!cmbselect.equals("")) {
            cmbselect.append("'0'");
        }
        Iterator iter = genericCodeDAO.getAllGenericCodesForAGSS(codeType,
                desc, cmbselect);

        genericCodeList.add(new LabelValueBean(label, "0"));
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer cid = (Integer) row[0];
            String cname = (String) row[1];
            genericCodeList.add(new LabelValueBean(cname, cid.toString()));
        }

        return genericCodeList;

    }

    public List getTypeListforcoagss(Integer codeType, String desc,
            String label, List typeList, String chargeType) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        StringBuffer cmbselect = new StringBuffer();
        String code = "11291";
        if (typeList != null && typeList.size() > 0) {

            for (int i = 0; i < typeList.size(); i++) {

                LCLColoadStandardCharges lCLColoadStandardCharges = (LCLColoadStandardCharges) typeList.get(i);

                if (!lCLColoadStandardCharges.getChargeType().getId().toString().equals(chargeType)) {
                    if (i == (typeList.size() - 1)) {
                        cmbselect.append("'"
                                + lCLColoadStandardCharges.getChargeType().getId().toString() + "'");

                    } else {
                        cmbselect.append("'"
                                + lCLColoadStandardCharges.getChargeType().getId().toString() + "',");
                    }
                }

            }

        }

        cmbselect.append("'" + code.toString() + "'");
        if (cmbselect.length() > 0) {
            if (cmbselect.charAt(cmbselect.length() - 1) == ',') {
                cmbselect.deleteCharAt(cmbselect.length() - 1);
            }
        }
        if (!cmbselect.equals("")) {
            cmbselect.append("'0'");
        }
        Iterator iter = genericCodeDAO.getAllGenericCodesForAGSS(codeType,
                desc, cmbselect);

        genericCodeList.add(new LabelValueBean(label, "0"));
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer cid = (Integer) row[0];
            String cname = (String) row[1];
            genericCodeList.add(new LabelValueBean(cname, cid.toString()));
        }

        return genericCodeList;

    }

    // for ftf edit standard charges
    public List getTypeListforftfagss(Integer codeType, String desc,
            String label, List typeList, String chargeType) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        StringBuffer cmbselect = new StringBuffer();

        if (typeList != null && typeList.size() > 0) {

            for (int i = 0; i < typeList.size(); i++) {

                FTFStandardCharges ftfStandardCharges = (FTFStandardCharges) typeList.get(i);

                if (!ftfStandardCharges.getChargeType().getId().toString().equals(chargeType)) {
                    if (i == (typeList.size() - 1)) {
                        cmbselect.append("'"
                                + ftfStandardCharges.getChargeType().getId().toString() + "'");

                    } else {
                        cmbselect.append("'"
                                + ftfStandardCharges.getChargeType().getId().toString() + "',");
                    }
                }

            }

        } else {
            cmbselect.append("'0'");
        }
        if (cmbselect.length() > 0) {
            if (cmbselect.charAt(cmbselect.length() - 1) == ',') {
                cmbselect.deleteCharAt(cmbselect.length() - 1);
            }
        }
        if (!cmbselect.equals("")) {
            cmbselect.append("'0'");
        }
        Iterator iter = genericCodeDAO.getAllGenericCodesForAGSS(codeType,
                desc, cmbselect);

        genericCodeList.add(new LabelValueBean(label, "0"));
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer cid = (Integer) row[0];
            String cname = (String) row[1];
            genericCodeList.add(new LabelValueBean(cname, cid.toString()));
        }

        return genericCodeList;

    }

    public List getNotesList(Integer codeType, String desc, String label) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();

        Iterator iter = genericCodeDAO.getAllNotesForDisplay(codeType, desc);

        genericCodeList.add(new LabelValueBean(label, "0"));
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            String cid = (String) row[0];
            String cname = (String) row[1];
            genericCodeList.add(new LabelValueBean(cname, cid.toString()));
        }

        return genericCodeList;

    }

    public List getspecialcodelist(Integer codeType, String desc, String label,
            String field1) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();

        Iterator iter = genericCodeDAO.getAllSpecialCodesForDisplay(codeType,
                desc, field1);
        genericCodeList.add(new LabelValueBean(label, "0"));
        if (iter != null) {
            while (iter.hasNext()) {
                Object[] row = (Object[]) iter.next();
                Integer cid = (Integer) row[0];
                String code = (String) row[1];
                String cname = (String) row[2];
                genericCodeList.add(new LabelValueBean(code + "-" + cname, cid.toString()));
            }
        }

        return genericCodeList;

    }

    public List getGenericCodeList1(Integer codeType, String desc,
            String label, List eqptListforDisplay) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        StringBuffer cmbselect = new StringBuffer();
        if (eqptListforDisplay != null && eqptListforDisplay.size() > 0) {
            for (int i = 0; i < eqptListforDisplay.size(); i++) {
                CarrierOceanEqptRates carrierOceanEqptRates = (CarrierOceanEqptRates) eqptListforDisplay.get(i);
                if (i == (eqptListforDisplay.size() - 1)) {
                    cmbselect.append("'"
                            + carrierOceanEqptRates.getEqpttype().getId().intValue() + "'");
                } else {
                    cmbselect.append("'"
                            + carrierOceanEqptRates.getEqpttype().getId().intValue() + "',");
                }

            }

        } else {
            cmbselect.append("'0'");
        }

        Iterator iter = genericCodeDAO.getAllGenericCodesForDisp1(codeType,
                desc, cmbselect);
        genericCodeList.add(new LabelValueBean(label, "0"));

        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer cid = (Integer) row[0];
            String cname = (String) row[1];
            genericCodeList.add(new LabelValueBean(cname, cid.toString()));
        }

        return genericCodeList;

    }

    public List getStateList() throws Exception {
        List stateList = new ArrayList();
        StateDAO stateDAO = new StateDAO();
        Iterator iter = stateDAO.getAllStatesForDisplay();
        stateList.add(new LabelValueBean("Select", "0"));
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer cid = (Integer) row[0];
            String cname = (String) row[1];
            stateList.add(new LabelValueBean(cname, cid.toString()));
        }
        return stateList;
    }

    public List getRoleList() throws Exception {
        List roleList = new ArrayList();
        RoleDAO roleDAO = new RoleDAO();
        //roleList.add(new LabelValueBean("Select Role", "0"));

        Iterator iter = roleDAO.getAllRolesForDisplay();

        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer cid = (Integer) row[0];
            String cname = (String) row[1];

            if (cname.equals("USER_LEV_1")) {
                roleList.add(0, new LabelValueBean(cname, cid.toString()));
            } else {
                roleList.add(new LabelValueBean(cname, cid.toString()));
            }
        }
        return roleList;
    }

    public List getPortList() throws Exception {
        List portcodeList = new ArrayList();
        PortsDAO portsDAO = new PortsDAO();
        portcodeList.add(new LabelValueBean("Select Port Code", "0"));
        Iterator iter = portsDAO.getAllPortForDisplay();
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer cid = (Integer) row[0];
            String cname = (String) row[1];
            portcodeList.add(new LabelValueBean(cname, cid.toString()));
        }
        return portcodeList;
    }

    public List getPortList1(List originList) {
        List portcodeList = new ArrayList();
        PortsDAO portsDAO = new PortsDAO();
        portcodeList.add(new LabelValueBean("Select Port Code", "0"));
        String scheduleNumber = "";
        String controlNo = "";
        HashMap hashMap = new HashMap();
        int i;
        List orgList = new ArrayList();
        if (originList != null) {
            for (i = 0; i < originList.size(); i++) {
                RelayOrigin ports = (RelayOrigin) originList.get(i);
                hashMap.put(ports.getOriginId().getPortname(), ports.getOriginId());
                orgList.add(ports.getOriginId().getPortname());
            }
        }
        Collections.sort(orgList);
        for (int j = 0; j < orgList.size(); j++) {
            PortsTemp p1 = (PortsTemp) hashMap.get(orgList.get(j));
            portcodeList.add(new LabelValueBean(p1.getPortname() + "-"
                    + p1.getShedulenumber(), p1.getId().toString()));
        }

        return portcodeList;
    }

    public List getPortList2(List destList) throws Exception {
        List portcodeList = new ArrayList();
        portcodeList.add(new LabelValueBean("Select Port Code", "0"));
        int j;
        HashMap hash = new HashMap();
        List destinationList = new ArrayList();
        if (destList != null) {
            for (j = 0; j < destList.size(); j++) {
                RelayDestination ports = (RelayDestination) destList.get(j);

                if (ports.getDestinationId() != null) {
                    hash.put(ports.getDestinationId().getPortname(), ports.getDestinationId());
                    destinationList.add(ports.getDestinationId().getPortname());
                }
            }
            Collections.sort(destinationList);
            for (int i = 0; i < destinationList.size(); i++) {
                PortsTemp p1 = (PortsTemp) hash.get(destinationList.get(i));
                portcodeList.add(new LabelValueBean(p1.getPortname() + "-"
                        + p1.getShedulenumber(), p1.getId().toString()));
            }

        }
        Collections.sort(portcodeList);
        return portcodeList;
    }

    public List getlclCodeList() throws Exception {
        List portcodeList = new ArrayList();
        PortsDAO portsDAO = new PortsDAO();
        portcodeList.add(new LabelValueBean("Select Port Code", "0"));
        Iterator iter = portsDAO.getAllPortCodeForDisplay();

        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer cid = (Integer) row[0];
            String cname = (String) row[1];
            portcodeList.add(new LabelValueBean(cname, cid.toString()));
        }
        return portcodeList;
    }

    public List getPortCodeList(List portList) throws Exception {

        List portcodeList = new ArrayList();
        PortsDAO portsDAO = new PortsDAO();

        StringBuffer cmbselect = new StringBuffer();
        if (portList != null && portList.size() > 0) {
            for (int i = 0; i < portList.size(); i++) {

                Ports ports = (Ports) portList.get(i);

                if (i == (portList.size() - 1)) {
                    cmbselect.append("'" + ports.getShedulenumber() + "'");

                } else {
                    cmbselect.append("'" + ports.getShedulenumber() + "',");

                }

            }

        } else {
            cmbselect.append("'0'");
        }

        portcodeList.add(new LabelValueBean("Select Port Code", "0"));
        Iterator iter = portsDAO.getAllPortCodesForDisplay(cmbselect);

        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer cid = (Integer) row[0];
            String cname = (String) row[1];
            portcodeList.add(new LabelValueBean(cname, cid.toString()));
        }
        return portcodeList;
    }

    public List getPortCodeList1(List portList) throws Exception {

        List portcodeList = new ArrayList();
        PortsDAO portsDAO = new PortsDAO();
        StringBuffer cmbselect = new StringBuffer();
        if (portList != null && portList.size() > 0) {
            for (int i = 0; i < portList.size(); i++) {
                RelayOrigin ports = (RelayOrigin) portList.get(i);
                if (i == (portList.size() - 1)) {
                    cmbselect.append("'"
                            + ports.getOriginId().getId().toString() + "'");

                } else {
                    cmbselect.append("'"
                            + ports.getOriginId().getId().toString() + "',");

                }

            }

        } else {
            cmbselect.append("'0'");
        }

        portcodeList.add(new LabelValueBean("Select Port Code", "0"));
        Iterator iter = portsDAO.getAllPortCodesForDisplay(cmbselect);
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer cid = (Integer) row[0];
            String cname = (String) row[1];
            portcodeList.add(new LabelValueBean(cname, cid.toString()));
        }
        return portcodeList;
    }

    public List getPortCodeList2(List portList) throws Exception {

        List portcodeList = new ArrayList();
        PortsDAO portsDAO = new PortsDAO();
        StringBuffer cmbselect = new StringBuffer();
        if (portList != null && portList.size() > 0) {
            for (int i = 0; i < portList.size(); i++) {
                RelayDestination ports = (RelayDestination) portList.get(i);
                if (i == (portList.size() - 1)) {
                    cmbselect.append("'"
                            + ports.getDestinationId().getId().toString() + "'");

                } else {
                    cmbselect.append("'"
                            + ports.getDestinationId().getId().toString()
                            + "',");

                }

            }

        } else {
            cmbselect.append("'0'");
        }

        portcodeList.add(new LabelValueBean("Select Port Code", "0"));
        Iterator iter = portsDAO.getAllPortCodesForDisplay(cmbselect);
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer cid = (Integer) row[0];
            String cname = (String) row[1];
            portcodeList.add(new LabelValueBean(cname, cid.toString()));
        }
        return portcodeList;
    }

    public List getPortCodeListforcheck(List portList, List onCarriage) throws Exception {

        List portcodeList = new ArrayList();
        PortsDAO portsDAO = new PortsDAO();
        StringBuffer cmbselect = new StringBuffer();
        StringBuffer cmbselect1 = new StringBuffer();
        boolean flag = false;
        if (onCarriage != null && onCarriage.size() > 0) {
            for (int j = 0; j < onCarriage.size(); j++) {
                LCLPortConfiguration ports = (LCLPortConfiguration) onCarriage.get(j);

                if (portList != null && portList.size() > 0) {
                    for (int i = 0; i < portList.size(); i++) {
                        RelayDestination ports1 = (RelayDestination) portList.get(i);

                        if (ports.getShedulenumber() != null
                                && ports1.getDestinationId() != null
                                && ports1.getDestinationId().getId() != null) {
                            if (ports.getShedulenumber().equals(
                                    ports1.getDestinationId().getId())) {
                                flag = true;
                                break;
                            }
                        }
                    }

                }
                if (!flag) {
                    portcodeList.add(ports);

                }
            }
        }

        return portcodeList;
    }

    public List getManagerList() throws Exception {
        List managerList = new ArrayList();
        UserDAO userDAO = new UserDAO();
        managerList.add(new LabelValueBean("Select Manager", "0"));

        Iterator iter = userDAO.getManagerDisplay();
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer cid = (Integer) row[0];
            String cname = (String) row[1];
            String lname = (String) row[2];
            managerList.add(new LabelValueBean(cname + " " + lname, cid.toString()));
        }

        return managerList;
    }

    public List getCityList(GenericCode countryId) throws Exception {

        List cityList = new ArrayList();
        UnLocationDAO unLocationDAO = new UnLocationDAO();
        cityList.add(new LabelValueBean("Select City", "0"));

        Iterator iter = unLocationDAO.getAllCityForDisplay(countryId);
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer cid = (Integer) row[0];
            String cname = (String) row[1];

            cityList.add(new LabelValueBean(cname, cid.toString()));

        }
        return cityList;
    }

    public Usecases getName(String usecaseId) throws Exception {
        UsecasesDAO usecasesDAO = new UsecasesDAO();
        Usecases usecase = null;
        if (usecaseId != null) {
            usecase = usecasesDAO.findById(Integer.parseInt(usecaseId));
        }
        return usecase;

    }

    public List getFlowFromList(String usecaseId) throws Exception {
        List usecasesList = new ArrayList();

        UsecasesDAO usecasesDAO = new UsecasesDAO();

        if (usecaseId != null) {
            Usecases usecase = usecasesDAO.findById(Integer.parseInt(usecaseId));
            DataExchangeDAO exchange = new DataExchangeDAO();

            Iterator iter = exchange.findAllFlowFrom(usecase);

            if (iter.hasNext()) {
                Object obj = iter.next();

                String cname = (String) obj;

                if (cname.equals("Logisoft")) {
                    usecasesList.add(new LabelValueBean("BlueScreen",
                            "BlueScreen"));
                } else if (cname.equals("BlueScreen")) {
                    usecasesList.add(new LabelValueBean("Logisoft", "Logisoft"));
                }

            } else {
                usecasesList.add(new LabelValueBean("BlueScreen", "BlueScreen"));
                usecasesList.add(new LabelValueBean("Logisoft", "Logisoft"));
            }

        }

        return usecasesList;
    }

    public List getUsecasesList() throws Exception {
        List usecasesList = new ArrayList();
        UsecasesDAO usecasesDAO = new UsecasesDAO();
        usecasesList.add(new LabelValueBean("Select Usecases", "0"));

        Iterator iter = usecasesDAO.getAllUsecasesForDisplay();

        while (iter.hasNext()) {

            Object[] row = (Object[]) iter.next();

            Integer cid = (Integer) row[0];
            String cname = (String) row[1];
            usecasesList.add(new LabelValueBean(cname, cid.toString()));
        }
        return usecasesList;
    }

    public List getStatusListForRelay() {
        List statusList = new ArrayList();
        statusList.add(new LabelValueBean("ACTIVE", "A"));
        statusList.add(new LabelValueBean("INACTIVE", "N"));
        return statusList;
    }

    public List getStatusList() {
        List statusList = new ArrayList();
        statusList.add(new LabelValueBean("ACTIVE", "ACTIVE"));
        statusList.add(new LabelValueBean("INACTIVE", "INACTIVE"));
        return statusList;
    }

    public List getacfList() {
        List acfList = new ArrayList();
        acfList.add(new LabelValueBean("Y", "Y"));
        acfList.add(new LabelValueBean("N", "N"));
        acfList.add(new LabelValueBean("C", "C"));
        acfList.add(new LabelValueBean("F", "F"));
        return acfList;
    }

    public List getStatesByCountry(Country country) throws Exception {

        List stateList = new ArrayList();
        StateDAO stateDAO = new StateDAO();
        stateList.add(new LabelValueBean("Select State", "0"));

        Iterator iter = stateDAO.getStatesByCountry(country);
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer cid = (Integer) row[0];
            String cname = (String) row[1];
            stateList.add(new LabelValueBean(cname, cid.toString()));

        }
        return stateList;
    }

    public List getAllUser() throws Exception {
        List userList = new ArrayList();
        UserDAO userDAO = new UserDAO();
        userList = userDAO.findAllUsers();
        return userList;

    }

    public List getAllUserToDisplay() throws Exception {
        List listOfUsers = new ArrayList();
        UserDAO userDAO = new UserDAO();
        List userList = userDAO.findAllUsers();
        Iterator iter = userList.iterator();
        listOfUsers.add(new LabelValueBean("select User", ""));
        listOfUsers.add(new LabelValueBean("ALL", "ALL"));
        while (iter.hasNext()) {
            User user = (User) iter.next();
            listOfUsers.add(new LabelValueBean(user.getLoginName(), user.getLoginName()));
        }
        return listOfUsers;

    }

    public List<LabelValueBean> getAllUsers() throws Exception {
        List<LabelValueBean> allUsers = new ArrayList<LabelValueBean>();
        allUsers.add(new LabelValueBean("select User", "0"));
        List<User> users = new UserDAO().findAllUsers();
        for (User user : users) {
            allUsers.add(new LabelValueBean(user.getLoginName(), user.getUserId().toString()));
        }
        return allUsers;

    }

    public List<LabelValueBean> getCollectors() throws Exception {
        List<LabelValueBean> collectors = new ArrayList<LabelValueBean>();
        collectors.add(new LabelValueBean("Select User", ""));
        collectors.add(new LabelValueBean("ALL", "ALL"));
        List<User> users = new UserDAO().findAllUsers();
        for (User user : users) {
            collectors.add(new LabelValueBean(user.getLoginName(), user.getUserId().toString()));
        }
        return collectors;

    }

    public List getAllStandardHistory(Integer standardid, Integer code) throws Exception {

        List standardHistoryList = new ArrayList();
        AirAccesorialRatesHistoryDAO airAccesorialRatesHistoryDAO = new AirAccesorialRatesHistoryDAO();
        standardHistoryList = airAccesorialRatesHistoryDAO.findAllStandardHistory(standardid, code);
        return standardHistoryList;
    }

    public List getAllRetailStandardHistory(Integer id, Integer code) throws Exception {

        List standardHistoryList = new ArrayList();
        RetailStandardCharges1HistoryDAO retailStandardCharges1HistoryDAO = new RetailStandardCharges1HistoryDAO();

        standardHistoryList = retailStandardCharges1HistoryDAO.findAllStandardHistory(id, code);

        return standardHistoryList;

    }

    public List getAllAirCommodityHistory(Integer standardId) throws Exception {

        List airCommodityHistoryList = new ArrayList();
        AirCommodityChargesHistoryDAO airCommodityChargesHistoryDAO = new AirCommodityChargesHistoryDAO();
        airCommodityHistoryList = airCommodityChargesHistoryDAO.findAllStandardHistory(standardId);

        return airCommodityHistoryList;

    }

    public List getAirRatesHistory(Integer standardid) throws Exception {

        List airRatesHistoryList = new ArrayList();
        AirRatesHistoryDAO airRatesHistoryDAO = new AirRatesHistoryDAO();
        airRatesHistoryList = airRatesHistoryDAO.findAllStandardHistory(standardid);

        return airRatesHistoryList;

    }

    public List getRetailRatesHistory(Integer id) throws Exception {

        List retailRatesHistoryList = new ArrayList();
        RetailOceanDetailsRatesHistoryDAO retailRatesHistoryDAO = new RetailOceanDetailsRatesHistoryDAO();
        retailRatesHistoryList = retailRatesHistoryDAO.findAllStandardHistory(id);

        return retailRatesHistoryList;

    }

    public List getAllRetailCommodityHistory(Integer id) throws Exception {

        List retailCommodityHistoryList = new ArrayList();
        RetailCommodityChargesHistoryDAO retailCommodityChargesHistoryDAO = new RetailCommodityChargesHistoryDAO();
        retailCommodityHistoryList = retailCommodityChargesHistoryDAO.findAllStandardHistory1(id);

        return retailCommodityHistoryList;

    }

    // ---------------------------------------------------
    public List getAirDetails(GenericCode org, GenericCode des,
            GenericCode com_code) throws Exception {
        List list = new ArrayList();

        StandardChargesDAO standardChargesDAO = new StandardChargesDAO();
        list = standardChargesDAO.findAllDetails(org, des, com_code);

        return list;

    }

    public List getRetailDetails(String orgine, String destination,
            String com_code) throws Exception {
        List list = new ArrayList();
        StandardChargesDAO standardChargesDAO = new StandardChargesDAO();
        list = standardChargesDAO.findAllDetails1(orgine, destination, com_code);
        return list;
    }

    public List getAllCustomer(String type) throws Exception {

        List customerList = new ArrayList();
        CustomerDAO customerDAO = new CustomerDAO();
        customerList = customerDAO.findAllCustomers(type);

        return customerList;

    }

    public List getAllWarehouses() throws Exception {

        List warehouseList = new ArrayList();
        WarehouseDAO warehouseDAO = new WarehouseDAO();

        warehouseList = warehouseDAO.findAllWarehouses();

        return warehouseList;

    }

    public List getAllRelay() throws Exception {

        List relayList = new ArrayList();
        RelayInquiryDAO relayInquiryDAO = new RelayInquiryDAO();

        relayList = relayInquiryDAO.findAllRelays();

        return relayList;

    }

    public List getAllAirRates() throws Exception {
        List airRatesList = new ArrayList();
        StandardChargesDAO standardChargesDAO = new StandardChargesDAO();
        airRatesList = standardChargesDAO.findAllAirRates();
        return airRatesList;
    }

    public List getAllRetailRates() throws Exception {
        List retailRatesList = new ArrayList();
        StandardChargesDAO standardChargesDAO = new StandardChargesDAO();
        retailRatesList = standardChargesDAO.findAllRetailRates();
        return retailRatesList;
    }

    public List getAllUseCases() throws Exception {

        List useCaseList = new ArrayList();
        DataExchangeTransactionDAO dataExchangeTransactionDAO = new DataExchangeTransactionDAO();
        useCaseList = dataExchangeTransactionDAO.findAllUseCases();

        return useCaseList;
    }

    public List getAllUseCaseId() throws Exception {
        List useCaseIdList = new ArrayList();
        UsecasesDAO usecasesDAO = new UsecasesDAO();
        useCaseIdList.add(new LabelValueBean("select useCaseId", "0"));
        Iterator iter = usecasesDAO.findAllUseCasesId();
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer cid = (Integer) row[0];
            String cname = (String) row[1];
            useCaseIdList.add(new LabelValueBean(cname, cid.toString()));

        }
        return useCaseIdList;
    }

    public List getAllPrinter() throws Exception {

        List printerList = new ArrayList();
        PrinterDAO printerDAO = new PrinterDAO();
        printerList = printerDAO.findAllPrinters();

        return printerList;

    }

    public List getAllRole() throws Exception {

        List roleList = new ArrayList();
        RoleDAO roleDAO = new RoleDAO();
        roleList = roleDAO.findAllRoles();

        return roleList;

    }

    public List getDataExchangeList() throws Exception {

        List dataexchangeList = new ArrayList();
        DataExchangeDAO dataexchangeDAO = new DataExchangeDAO();
        dataexchangeList = dataexchangeDAO.findAllDataExchange();

        return dataexchangeList;

    }

    public List getUserByLoginName(String loginName) throws Exception {
        List userList = new ArrayList();
        UserDAO userDAO = new UserDAO();
        userList = userDAO.findLoginName(loginName);
        return userList;
    }

    public List getTerminalList() throws Exception {
        List terminalList = new ArrayList();
        RefTerminalDAO terminalDAO = new RefTerminalDAO();
        Iterator iter = terminalDAO.getAllTerminalsForDisplay();
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            String cid = (String) row[0];
            String cname = (String) row[1];
            terminalList.add(new LabelValueBean(cname + "-" + cid, cid));
        }
        return terminalList;
    }

    public List getTemplateList() throws Exception {
        List templateList = new ArrayList();
        List<LclSearchTemplate> exporttemplateList = new LclSearchTemplateDAO().getAllTemplate();
        if (CommonUtils.isNotEmpty(exporttemplateList)) {
            Iterator iter = exporttemplateList.iterator();
            while (iter.hasNext()) {
                LclSearchTemplate template = (LclSearchTemplate) iter.next();
                String cid = template.getId().toString();
                String cname = template.getTemplateName();
                templateList.add(new LabelValueBean(cname, cid));
            }
        }
        return templateList;
    }

    public List getFlowList() {
        List flowList = new ArrayList();
        flowList.add(new LabelValueBean("Select FlowFrom", "0"));
        flowList.add(new LabelValueBean("Logisoft", "Logisoft"));
        flowList.add(new LabelValueBean("Blue Screen", "Blue Screen"));
        return flowList;
    }

    public List getStatasList() {
        List statasList = new ArrayList();
        statasList.add(new LabelValueBean("select status", "0"));
        statasList.add(new LabelValueBean("Logisoft Outgoing Database",
                "Logisoft Outgoing Database"));
        statasList.add(new LabelValueBean("Converted to CSV",
                "Converted to CSV"));
        statasList.add(new LabelValueBean("Loaded into Blue Screen",
                "Loaded into Blue Screen"));
        statasList.add(new LabelValueBean("Processed", "Processed"));
        return statasList;
    }

    public List getPrinterList() throws Exception {
        List printerList = new ArrayList();
        PrinterDAO printerDAO = new PrinterDAO();
        Iterator iter = printerDAO.getAllPrintersForDisplay();

        printerList.add(new LabelValueBean("Select Printer", "0"));

        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer cid = (Integer) row[0];

            String cname = (String) row[1];
            printerList.add(new LabelValueBean(cname, cid.toString()));
        }

        return printerList;

    }

    public List getPrintList(String printType, List addedPrinterList) throws Exception {
        List printerList = new ArrayList();
        PrinterDAO printerDAO = new PrinterDAO();
        StringBuffer cmbselect = new StringBuffer();
        if (addedPrinterList != null && addedPrinterList.size() > 0) {
            for (int i = 0; i < addedPrinterList.size(); i++) {

                Printer printer = (Printer) addedPrinterList.get(i);

                if (i == (addedPrinterList.size() - 1)) {
                    cmbselect.append("'" + printer.getPrinterId() + "'");
                } else {
                    cmbselect.append("'" + printer.getPrinterId().intValue()
                            + "',");
                }

            }

        } else {
            cmbselect.append("'0'");
        }

        Iterator iter = printerDAO.getAllPrintersName(printType, cmbselect);

        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();

            Integer cid = (Integer) row[0];

            String cname = (String) row[1];
            printerList.add(new LabelValueBean(cname, cid.toString()));
        }

        return printerList;

    }

    public List getAllItems() throws Exception {
        List itemList = new ArrayList();
        ItemDAO itemDAO = new ItemDAO();
        itemList = itemDAO.findAllItems();

        return itemList;

    }

    public List getAllCarriers() throws Exception {
        List carrierList = new ArrayList();
        CarriersOrLineDAO carrierDAO = new CarriersOrLineDAO();

        carrierList = carrierDAO.findAllCarriers();

        return carrierList;

    }

    public List getAllPorts() throws Exception {
        List portsList = new ArrayList();
        PortsDAO portsDAO = new PortsDAO();
        portsList = portsDAO.findAllPorts();
        return portsList;

    }

    public List getPredecessorList(String edit) throws Exception {
        List predecessorList = new ArrayList();
        ItemDAO itemDAO = new ItemDAO();
        Iterator iter = itemDAO.getPredecessorForDisplay(edit);

        predecessorList.add(new LabelValueBean("Select a Predecessor", "0"));

        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer iid = (Integer) row[0];
            String iname = (String) row[1];
            predecessorList.add(new LabelValueBean(iname, iid.toString()));
        }

        return predecessorList;
    }

    public void getProcessInfo(String programid, String recordid,
            String editstatusaftermoving, String deletestatusaftermoving) throws Exception {
        ProcessInfoDAO processinfoDAO = new ProcessInfoDAO();
        ProcessInfoHistoryDAO processinfohistoryDAO = new ProcessInfoHistoryDAO();
        ProcessInfoHistory processinfohistory = new ProcessInfoHistory();
        ProcessInfo processinfoobj = new ProcessInfo();
        programid = programid != null ? programid : "0";

        String editstatus = "startedited";
        String deletestatus = "startdeleted";
        if (programid != null && !programid.equals("")) {
            processinfoobj = processinfoDAO.findById(Integer.parseInt(programid), recordid, deletestatus, editstatus);
        }

        if (processinfoobj != null) {
            processinfohistory.setUserid(processinfoobj.getUserid());

            processinfohistory.setProgramid(processinfoobj.getProgramid());
            processinfohistory.setRecordid(processinfoobj.getRecordid());
            processinfohistory.setProcessinfodate(processinfoobj.getProcessinfodate());
            processinfohistory.setId(processinfoobj.getId());
            processinfohistory.setAction(processinfoobj.getAction());
            processinfohistory.setEditstatus(processinfoobj.getEditstatus());
            processinfohistory.setDeletestatus(processinfoobj.getDeletestatus());
            if (processinfoobj.getUserid() != null) {
                processinfohistoryDAO.save(processinfohistory);
            }
            java.util.Date currdate = new java.util.Date();
            processinfoobj.setEditstatus(editstatusaftermoving);

            processinfoobj.setProcessinfodate(currdate);
        }
        if (processinfoobj != null && processinfoobj.getId() != null) {
            processinfoDAO.update(processinfoobj);
        }

        // Update Process_info
        // Move to Process_info_history
        ProcessInfo pi = processinfoDAO.findById(Integer.parseInt(programid),
                recordid, deletestatusaftermoving, editstatusaftermoving);
        ProcessInfoHistory pih = new ProcessInfoHistory();
        if (pi != null) {
            pih.setUserid(pi.getUserid());
            pih.setProgramid(pi.getProgramid());
            pih.setRecordid(pi.getRecordid());
            pih.setProcessinfodate(pi.getProcessinfodate());
            pih.setId(pi.getId());
            pih.setAction(processinfoobj.getAction());
            pih.setEditstatus(pi.getEditstatus());

            processinfohistoryDAO.save(pih);

        }
        // delete in Process_info
        if (pi != null && pi.getId() != null) {
            processinfoDAO.delete(pi);
        }

    }

    public boolean tabProcessInfo(int userid) throws Exception {
        ProcessInfoDAO processinfoDAO = new ProcessInfoDAO();
        ProcessInfo pi = new ProcessInfo();
        pi = processinfoDAO.getProcessInfo(userid);
        boolean lockRemoved = false;
        if (pi != null) {
            ProcessInfoHistoryDAO processinfohistoryDAO = new ProcessInfoHistoryDAO();
            ProcessInfoHistory processinfohistory = new ProcessInfoHistory();
            processinfohistory.setUserid(pi.getUserid());
            processinfohistory.setProgramid(pi.getProgramid());
            processinfohistory.setRecordid(pi.getRecordid());
            processinfohistory.setProcessinfodate(pi.getProcessinfodate());
            processinfohistory.setId(pi.getId());
            processinfohistory.setAction(pi.getAction());
            processinfohistory.setEditstatus(pi.getEditstatus());
            processinfohistory.setDeletestatus(pi.getDeletestatus());
            processinfohistoryDAO.save(processinfohistory);
            java.util.Date currdate = new java.util.Date();
            String editstat = "editcancelled";
            pi.setEditstatus(editstat);
            pi.setProcessinfodate(currdate);
            // processinfoDAO.update(pi);

            ProcessInfoHistory pih = new ProcessInfoHistory();
            // ProcessInfo pi1 = new ProcessInfo();
            // String editstatus="editcancelled";
            // pi1=processinfoDAO.getProcess(editstatus);
            // if(pi1!=null)
            // {
            pih.setUserid(pi.getUserid());
            pih.setProgramid(pi.getProgramid());
            pih.setRecordid(pi.getRecordid());
            pih.setProcessinfodate(pi.getProcessinfodate());
            pih.setId(pi.getId());
            pih.setAction(pi.getAction());
            pih.setEditstatus(pi.getEditstatus());
            pih.setDeletestatus(pi.getDeletestatus());
            processinfohistoryDAO.save(pih);
            processinfoDAO.delete(pi);
            lockRemoved = true;
            // }
        }
        return lockRemoved;

    }

    public boolean deletelogout(int userid) throws Exception {

        ProcessInfoDAO processinfoDAO = new ProcessInfoDAO();
        List<ProcessInfo> processInfoList = processinfoDAO.getProcessInf(userid);
        processInfoList = null == processInfoList ? new ArrayList<ProcessInfo>() : processInfoList;
        boolean lockRemoved = false;
        ProcessInfoHistoryDAO processinfohistoryDAO = new ProcessInfoHistoryDAO();
        ProcessInfoHistory processinfohistory = null;
        ProcessInfoHistory pih = new ProcessInfoHistory();
        for (ProcessInfo processInfo : processInfoList) {
            processinfohistory = new ProcessInfoHistory();
            processinfohistory.setUserid(processInfo.getUserid());
            processinfohistory.setProgramid(processInfo.getProgramid());
            processinfohistory.setRecordid(processInfo.getRecordid());
            processinfohistory.setProcessinfodate(processInfo.getProcessinfodate());
            processinfohistory.setId(processInfo.getId());
            processinfohistory.setAction(processInfo.getAction());
            processinfohistory.setEditstatus(processInfo.getEditstatus());
            processinfohistory.setDeletestatus(processInfo.getDeletestatus());
            processinfohistoryDAO.save(processinfohistory);
            pih = new ProcessInfoHistory();
            pih.setUserid(processInfo.getUserid());
            pih.setProgramid(processInfo.getProgramid());
            pih.setRecordid(processInfo.getRecordid());
            pih.setProcessinfodate(new Date());
            pih.setId(processInfo.getId());
            pih.setAction(processInfo.getAction());
            pih.setEditstatus("editcancelled");
            pih.setDeletestatus(processInfo.getDeletestatus());
            processinfohistoryDAO.save(pih);
            processinfoDAO.delete(processInfo);
        }
        lockRemoved = true;
        return lockRemoved;

    }

    public List getHours() {
        List hoursList = new ArrayList();
        String s = "0";
        for (int i = 0; i <= 23; i++) {
            if (i < 10) {
                hoursList.add(new LabelValueBean(String.valueOf(i), s.concat(String.valueOf(i))));
            } else {
                hoursList.add(new LabelValueBean(String.valueOf(i), String.valueOf(i)));
            }
        }
        return hoursList;
    }

    public List getMinutes() {
        String s = "0";
        List minutesList = new ArrayList();
        for (int i = 0; i <= 59; i++) {
            if (i < 10) {
                minutesList.add(new LabelValueBean(String.valueOf(i), s.concat(String.valueOf(i))));
            } else {
                minutesList.add(new LabelValueBean(String.valueOf(i), String.valueOf(i)));
            }
        }
        return minutesList;
    }

    public List getNoteInformation(String id, AuditLogRecord auditLogRecord) throws Exception {

        String query = "  from " + auditLogRecord.getClass().getName()
                + "  where entityId=?0 and voided is null order by id desc";
        AuditLogRecordDAO audit = new AuditLogRecordDAO();
        List l = null;
        l = audit.findAllUsers(query, id);

        return l;
    }

    public List getNoteInformation1(String id, AuditLogRecord auditLogRecord) throws Exception {

        String query = "  from " + auditLogRecord.getClass().getName()
                + "  where entityId=?0 and voided is null order by id desc";
        AuditLogRecordDAO audit = new AuditLogRecordDAO();
        List l = null;
        l = audit.findAllUsers(query, id);

        return l;
    }

    public List getAllTerminal() throws Exception {

        List terminalList = new ArrayList();
        RefTerminalDAO refTerminalDAO = new RefTerminalDAO();

        terminalList = refTerminalDAO.findAllTerminals();

        return terminalList;

    }

    public List getAllClaims() throws Exception {

        List claimList = new ArrayList();
        ClaimDAO claimDAO = new ClaimDAO();
        claimList = claimDAO.findAllClaims();

        return claimList;

    }

    public List getTerminalTypeList() throws Exception {
        List terminalTypeList = new ArrayList();
        RefTerminalDAO refTerminalDAO = new RefTerminalDAO();
        Iterator iter = refTerminalDAO.getAllTerminalsForDisplay();

        terminalTypeList.add(new LabelValueBean("Select TerminalType", "0"));

        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            String cid = (String) row[1];
            String cname = (String) row[1];
            terminalTypeList.add(new LabelValueBean(cname, cid));
        }

        return terminalTypeList;

    }

    public List getCityList() throws Exception {
        List cityList = new ArrayList();
        UnLocationDAO unLocationDAO = new UnLocationDAO();
        cityList.add(new LabelValueBean("select city", "0"));
        Iterator iter = unLocationDAO.getAllCitiesForDisplay();
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer cid = (Integer) row[0];
            String cname = (String) row[1];
            cityList.add(new LabelValueBean(cname, cid.toString()));
        }
        return cityList;
    }

    public List getContactList(List contactAddList) throws Exception {
        List contactList = new ArrayList();
        UserDAO userDAO = new UserDAO();
        StringBuffer cmbselect = new StringBuffer();

        if (contactAddList != null && contactAddList.size() > 0) {
            for (int i = 0; i < contactAddList.size(); i++) {
                TerminalContacts terminalContact = (TerminalContacts) contactAddList.get(i);
                if (i == (contactAddList.size() - 1)) {
                    cmbselect.append("'"
                            + terminalContact.getUser().getUserId().intValue()
                            + "'");
                } else {
                    cmbselect.append("'"
                            + terminalContact.getUser().getUserId().intValue()
                            + "',");
                }

            }

        } else {
            cmbselect.append("'0'");
        }

        contactList.add(new LabelValueBean("select ContactName", "0"));
        Iterator iter = userDAO.getAllContactsForDisplay(cmbselect);
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer cid = (Integer) row[0];
            String cname = (String) row[1];
            String cnam = (String) row[2];
            contactList.add(new LabelValueBean(cname + cnam, cid.toString()));
        }
        return contactList;
    }

    public List getCodesList() throws Exception {
        List codesList = new ArrayList();
        CodetypeDAO codetypeDAO = new CodetypeDAO();
        Iterator iter = codetypeDAO.getAllCodesForDisplay();
        codesList.add((new LabelValueBean("Select Codes", "0")));
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer cid = (Integer) row[0];
            String cname = (String) row[1];
            codesList.add(new LabelValueBean(cname, cid.toString()));
        }

        return codesList;
    }

    public List getUnCityCodeList() throws Exception {
        List unCityCodeList = new ArrayList();
        UnLocationDAO unLocationDAO = new UnLocationDAO();
        Iterator iter = unLocationDAO.getAllUnCityCodesForDisplay();
        unCityCodeList.add((new LabelValueBean("Select UN Loc Code", "0")));

        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer cid = (Integer) row[0];
            String cname = (String) row[1];
            unCityCodeList.add(new LabelValueBean(cname, cid.toString()));
        }

        return unCityCodeList;
    }

    public List getUnCityCodeListForAirport() throws Exception {
        List unCityCodeList = new ArrayList();
        UnLocationDAO unLocationDAO = new UnLocationDAO();
        Iterator iter = unLocationDAO.getAllUnCityCodesForDisplay();
        unCityCodeList.add((new LabelValueBean("Select Airport City Name", "0")));

        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer cid = (Integer) row[0];
            String cname = (String) row[1];
            unCityCodeList.add(new LabelValueBean(cname, cid.toString()));
        }

        return unCityCodeList;
    }

    public List getUnCityCodeForAirport() throws Exception {
        List unCityCodeList = new ArrayList();
        UnLocationDAO unLocationDAO = new UnLocationDAO();
        Iterator iter = unLocationDAO.getAllUnCityCodeForDisplay();
        unCityCodeList.add((new LabelValueBean("Select Airport City Name", "0")));

        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer cid = (Integer) row[0];
            String cname = (String) row[1];
            unCityCodeList.add(new LabelValueBean(cname, cid.toString()));
        }

        return unCityCodeList;
    }

    public List getTerminalListForLCLPortsConfig() throws Exception {
        List terminalList = new ArrayList();
        RefTerminalDAO refTreminalDAO = new RefTerminalDAO();
        Iterator iter = refTreminalDAO.getTerminalNumbersForDisplay();
        terminalList.add((new LabelValueBean("Select Terminal Number", "0")));
        while (iter.hasNext()) {
            String cid = (String) iter.next();
            terminalList.add(new LabelValueBean(cid, cid));

        }

        return terminalList;
    }

    public List getAgentAcctList() throws Exception {
        List agentAcctList = new ArrayList();
        CustomerDAO customerDAO = new CustomerDAO();
        Iterator iter = customerDAO.getAgentAcctNoForDisplay();
        agentAcctList.add((new LabelValueBean("Select Agent Acct Number", "0")));
        while (iter.hasNext()) {
            String cid = (String) iter.next();
            agentAcctList.add(new LabelValueBean(cid, cid));

        }

        return agentAcctList;
    }

    public List getAllUserForLinemanagers() throws Exception {
        List userList = new ArrayList();
        UserDAO userDAO = new UserDAO();
        Iterator iter = userDAO.getUsersForLineManagers();
        userList.add((new LabelValueBean("Select Line Manager", "0")));

        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer cid = (Integer) row[0];
            String cname = (String) row[1];
            userList.add(new LabelValueBean(cname, cid.toString()));
        }

        return userList;
    }

    public List getAllpiercode() throws Exception {
        List userList = new ArrayList();
        PortsDAO portDAO = new PortsDAO();
        Iterator iter = portDAO.getPortsForDisplay();
        userList.add((new LabelValueBean("Select Pier Code", "0")));

        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer cid = (Integer) row[0];
            String cname = (String) row[1];
            userList.add(new LabelValueBean(cname, cid.toString()));
        }

        return userList;
    }

    public List getCodesDetails(int i) throws Exception {
        List codesDetails = new ArrayList();
        GenericcodelabelsDAO codedetailsDAO = new GenericcodelabelsDAO();
        Iterator iter = codedetailsDAO.getAllCodesDetails(i);
        if (!(iter == null)) {
            while (iter.hasNext()) {
                Object[] row = (Object[]) iter.next();
                String cid = (String) row[0];
                String cname = (String) row[1];
                codesDetails.add(new LabelValueBean(cname, cid));
            }

            return codesDetails;
        } else {
            return null;
        }
    }

    public String[] getSessionParams() throws Exception {
        String s[] = {"userList", "roleList", "itemList",
            "processinfoformenu", "modifyforuser", "rolecode",
            "processinfoforrole", "modifyforrole", "user",
            "processinfoforwarehouse", "modifyforwarehouse",
            "processinfoforterminal", "modifyforterminal",
            "processinfoforuser", "processinfoforrelay", "modifyforuser",
            "portList", "relayList", "lclPortConfigurationObj",
            "fclPortObjConfiguration", "airPortObjConfigConfiguration",
            "impPortObjConfiguration", "importPortObj",
            "agencyInfoListForLCL", "agencyInfoListForFCL",
            "agencyInfoListForAir", "agencyInfoListForImp", "portList1",
            "view", "modifyforports", "processinfoforports",
            "modifyforCodeDetails",
            "portobject", "terminal", "processinfoforcarriers",
            "modifyformanagingcarriers", "carrierList", "search",
            "buttonValue", "searchrelaycode", "customernotes",
            "processinfoforcustomer", "modifyforcustomer", "airocean",
            "carriers", "mangingcarrierCaption", "portLst",
            "carrierairline", "eqptList", "aircode",
            "processinfoformastercustomer", "modifyformastercustomer",
            "mastercustomerList", "batch", "journalEntry", "line",
            "batchList", "journalEntryList", "cancelline", "search1",
            "search", "notemsg", "standardCharges", "flightShedulesAdd",
            "airFrieght", "docChargesAdd", "agssAdd", "documentCharges",
            "addrates", "standardCharges", "setTabEnable",
            "postedBatchList", "modifyforairRates",
            "processinfoforairRates", "buttonValue", "search1",
            "batchesList", "addlclColoadMaster", "processinfoforcoLoad",
            "modifyforlclcoloadRates", "processinfoforretailRates",
            "modifyforretailRates", "modifyforftfRates", "batchNo",
            "rerecords",
            "retailstandardCharges", "fclcommonList", "addfclrecords",
            "creditholdobj", "Ardestination", "processinfoforquotation",
            "modifyforquotation"};
        return s;
    }

    /**
     * @param session removed session every time when user changed tab.....
     */
    public void removeSessions(HttpSession session) throws Exception {
        String s[] = getSessionParams();
        for (int i = 0; i < s.length; i++) {
            if (session.getAttribute(s[i]) != null) {
                session.removeAttribute(s[i]);
            }
        }
        if (session.getAttribute("loginuser") != null) {
            ProcessInfoBC processInfoBC = new ProcessInfoBC();
            User user = (User) session.getAttribute("loginuser");
            DBUtil dbUtil = new DBUtil();
            //processInfoBC.deleteAllRecordsWhileChangeTab(user.getUserId());
        }
    }

    /**
     * @return remove session when user logout off.....
     */
    public String[] getSessionParamsToRemoveWhenLogout() {
        String s[] = {QuotationConstants.FILESEARCHLIST};
        return s;
    }

    public void removeSessionsWhenLogout(HttpSession session) {
        String s[] = getSessionParamsToRemoveWhenLogout();
        for (int i = 0; i < s.length; i++) {
            if (session.getAttribute(s[i]) != null) {
                session.removeAttribute(s[i]);
            }
        }
    }

    public List getCountryList() throws Exception {

        List countryList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        countryList.add(new LabelValueBean("Select Country", "0"));

        Iterator iter = genericCodeDAO.getAllcountriesForDisplay();
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            String cid = (String) row[0];
            String cname = (String) row[1];
            countryList.add(new LabelValueBean(cname, cid.toString()));
        }
        return countryList;
    }

    public List getShipmentList() throws Exception {

        List countryList = new ArrayList();
        SubledgerDAO genericCodeDAO = new SubledgerDAO();
        countryList.add(new LabelValueBean("Select Shipment Type", "0"));

        Iterator iter = genericCodeDAO.getUniqueSubLedgerList1();
        if (iter != null) {
            while (iter.hasNext()) {
                Object[] row = (Object[]) iter.next();
                String cid = (String) row[0];
                String cname = (String) row[1];
                countryList.add(new LabelValueBean(cname, cid.toString()));
            }
        }
        return countryList;
    }

    // done by pravin --17/01/08
    public List getRegionList() throws Exception {

        List regionList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        regionList.add(new LabelValueBean("Select Region", "0"));

        Iterator iter = genericCodeDAO.getAllRegionsForDisplay();
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            String cid = (String) row[0];
            String cname = (String) row[1];
            regionList.add(new LabelValueBean(cname, cid.toString()));
        }
        return regionList;
    }

    public List getUomCodes() throws Exception {

        List regionList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        regionList.add(new LabelValueBean("Select UOM", ""));

        Iterator iter = genericCodeDAO.getAllUomCodes();
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            String cid = (String) row[0];
            String cname = (String) row[1];
            regionList.add(new LabelValueBean(cname, cid.toString()));
        }
        return regionList;
    }

    public String stringtokenizer(String strg) {
        StringTokenizer st;
        String st1 = "";
        st = new StringTokenizer(strg, "()-");
        while (st.hasMoreTokens()) {
            st1 = st1 + st.nextToken();
        }
        return st1;
    }

    public String accountname(String strg) {
        StringTokenizer st;
        String st1 = "";
        st = new StringTokenizer(strg, " ");
        while (st.hasMoreTokens()) {
            st1 = st.nextToken();
        }
        return st1;
    }

    public String appendstring(String s) {

        String st2 = "";
        if (s != null && s.length() >= 10) {
            st2 = "(".concat(s.substring(0, 3).concat(")").concat(
                    s.substring(3, 6).concat("-").concat(s.substring(6, 10))));
        }
        return st2;
    }

    // hydoffice
    public String getStateByCity(String s) throws Exception {
        UnLocationDAO unlocationDAO = new UnLocationDAO();
        UnLocation unlocation = unlocationDAO.findById(Integer.parseInt(s));
        GenericCode gc = unlocation.getStateId();
        return gc.getCode();
    }

    public Integer getNumberOfOrigins(Integer relayId) throws Exception {
        RelayOriginDAO originDao = new RelayOriginDAO();
        Integer no = originDao.getNumberOFOrigins(relayId);
        return no;
    }

    public Integer getNumberOfDestinations(Integer relayId) throws Exception {
        RelayDestinationDAO relayDestinationDao = new RelayDestinationDAO();
        Integer no = relayDestinationDao.getNumberOFDestinations(relayId);
        return no;
    }

    public Integer getNumberOfPortExceptions(Integer relayId) throws Exception {
        PortExceptionDAO portExceptionDAO = new PortExceptionDAO();
        Integer no = portExceptionDAO.getNumberOFPortexceptions(relayId);
        return no;
    }

    public String getNotesType(String accountNo) throws Exception {
        AuditLogRecordDAO auditLogRecordDAO = new AuditLogRecordDAO();
        AuditLogRecordCustomer notesType = new AuditLogRecordCustomer();
        String flag = "false";
        List accountNo1 = auditLogRecordDAO.findSpecialNotes(accountNo);
        if (accountNo1 != null && accountNo1.size() > 0) {
            for (int i = 0; i < accountNo1.size(); i++) {
                notesType = (AuditLogRecordCustomer) accountNo1.get(i);
                if (notesType.getNoteType().equals("Special Notes")) {
                    flag = "true";
                    break;
                }
            }
        }
        return flag;
    }

    public List getWeekList(Integer codeType, String desc, String label) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();

        Iterator iter = genericCodeDAO.getAllWeekForDisplay(codeType, desc);

        genericCodeList.add(new LabelValueBean(label, "0"));

        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer cid = (Integer) row[0];
            String cname = (String) row[1];
            genericCodeList.add(new LabelValueBean(cname, cid.toString()));
        }

        return genericCodeList;

    }

    public List getWeekListForFlightShedule(Integer codeType, String desc,
            String label) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();

        Iterator iter = genericCodeDAO.getAllWeekForDisplayForFlightShedule(
                codeType, desc);

        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer cid = (Integer) row[0];
            String code = (String) row[1];
            String cname = (String) row[2];
            genericCodeList.add(new LabelValueBean(cname, code));
        }

        return genericCodeList;
    }

    // g
    public List getTypeListForCsss(Integer codeType, String desc, String label) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        StringBuffer cmbselect = new StringBuffer();
        String code = "11289";
        cmbselect.append("'" + code.toString() + "',");
        cmbselect = cmbselect.append(cmbselect.length() - 1);
        Iterator iter = genericCodeDAO.getAllGenericCodesForAGSS(codeType,
                desc, cmbselect);

        genericCodeList.add(new LabelValueBean(label, "0"));
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer cid = (Integer) row[0];
            String cname = (String) row[1];
            genericCodeList.add(new LabelValueBean(cname, cid.toString()));
        }

        return genericCodeList;
    }

    public List getTypeListForcoloadCsss(Integer codeType, String desc,
            String label) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        StringBuffer cmbselect = new StringBuffer();
        String code = "11291";
        cmbselect.append("'" + code.toString() + "',");
        cmbselect = cmbselect.append(cmbselect.length() - 1);
        Iterator iter = genericCodeDAO.getAllGenericCodesForAGSS(codeType,
                desc, cmbselect);
        genericCodeList.add(new LabelValueBean(label, "0"));
        if (iter != null) {
            while (iter.hasNext()) {
                Object[] row = (Object[]) iter.next();
                Integer cid = (Integer) row[0];
                String cname = (String) row[1];
                genericCodeList.add(new LabelValueBean(cname, cid.toString()));
            }
        }
        return genericCodeList;
    }

    public List getTypeListForUniverse(Integer codeType, String desc,
            String label) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        StringBuffer cmbselect = new StringBuffer();
        String code = "11291";
        cmbselect.append("'" + code.toString() + "',");
        cmbselect = cmbselect.append(cmbselect.length() - 1);
        Iterator iter = genericCodeDAO.getAllGenericCodesForAGSS(codeType,
                desc, cmbselect);
        genericCodeList.add(new LabelValueBean(label, "0"));
        if (iter != null) {
            while (iter.hasNext()) {
                Object[] row = (Object[]) iter.next();
                Integer cid = (Integer) row[0];
                String cname = (String) row[1];
                genericCodeList.add(new LabelValueBean(cname, cid.toString()));
            }
        }
        return genericCodeList;
    }

    // unit type List
    public List getUnitListForFCL(Integer codeType, String desc, String label,
            List typeList) throws Exception {

        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        StringBuffer cmbselect = new StringBuffer();

        if (typeList != null && typeList.size() > 0) {

            for (int i = 0; i < typeList.size(); i++) {

                FclBuyCostTypeRates fclBuyCostTypeRates = (FclBuyCostTypeRates) typeList.get(i);

                if (i == (typeList.size() - 1)) {
                    if (fclBuyCostTypeRates.getUnitType() != null) {
                        cmbselect.append("'"
                                + fclBuyCostTypeRates.getUnitType().getId().toString() + "'");
                    }

                } else {
                    if (fclBuyCostTypeRates.getUnitType() != null) {
                        cmbselect.append("'"
                                + fclBuyCostTypeRates.getUnitType().getId().toString() + "',");
                    }
                }

            }

        } else {
            cmbselect.append("'0'");
        }
        if (!cmbselect.equals("")) {
            cmbselect.append("'0'");
        }
        Iterator iter = genericCodeDAO.getAllUnitCodeForFCL(codeType, desc,
                cmbselect);
        genericCodeList.add(new LabelValueBean(label, "0"));
        if (iter != null) {
            while (iter.hasNext()) {
                Object[] row = (Object[]) iter.next();
                Integer cid = (Integer) row[0];
                String cname = (String) row[1];
                genericCodeList.add(new LabelValueBean(cname, cid.toString()));
            }
        }
        return genericCodeList;
    }

    public List getTypeListForftfCsss(Integer codeType, String desc,
            String label, List typeList) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        StringBuffer cmbselect = new StringBuffer();
        String code = "11291";
        cmbselect.append("'" + code.toString() + "',");

        if (typeList != null && typeList.size() > 0) {

            for (int i = 0; i < typeList.size(); i++) {

                FTFCommodityCharges ftfCommodityCharges = (FTFCommodityCharges) typeList.get(i);

                if (i == (typeList.size() - 1)) {
                    if (ftfCommodityCharges.getChargeType() != null) {
                        cmbselect.append("'"
                                + ftfCommodityCharges.getChargeType().getId().toString() + "',");
                    }

                } else {
                    if (ftfCommodityCharges.getChargeType() != null) {
                        cmbselect.append("'"
                                + ftfCommodityCharges.getChargeType().getId().toString() + "',");
                    }
                }

            }

        }

        cmbselect = cmbselect.append(cmbselect.length() - 1);
        Iterator iter = genericCodeDAO.getAllGenericCodesForAGSS(codeType,
                desc, cmbselect);

        genericCodeList.add(new LabelValueBean(label, "0"));
        if (iter != null) {
            while (iter.hasNext()) {
                Object[] row = (Object[]) iter.next();
                Integer cid = (Integer) row[0];
                String cname = (String) row[1];
                genericCodeList.add(new LabelValueBean(cname, cid.toString()));
            }
        }
        return genericCodeList;
    }

    public List getTypeListForCssc1(Integer codeType, String desc, String label) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        StringBuffer cmbselect = new StringBuffer();
        if (cmbselect.length() > 0) {
            if (cmbselect.charAt(cmbselect.length() - 1) == ',') {
                cmbselect.deleteCharAt(cmbselect.length() - 1);
            }
        }

        if (!cmbselect.equals("")) {
            cmbselect.append("'0'");
        }

        Iterator iter = genericCodeDAO.getAllGenericCodesForAGSS(codeType,
                desc, cmbselect);

        genericCodeList.add(new LabelValueBean(label, "0"));
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer cid = (Integer) row[0];
            String cname = (String) row[1];
            genericCodeList.add(new LabelValueBean(cname, cid.toString()));
        }

        return genericCodeList;
    }

    public List getTypeList2(Integer codeType, String desc, String label,
            List typeList, String chargeType) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        StringBuffer cmbselect = new StringBuffer();

        if (typeList != null && typeList.size() > 0) {

            for (int i = 0; i < typeList.size(); i++) {

                boolean flag = false;
                RetailStandardCharges1 retailStandardCharges = (RetailStandardCharges1) typeList.get(i);
                if (!retailStandardCharges.getChargeType().getId().toString().equals(chargeType)) {
                    flag = true;
                    if (i == (typeList.size() - 1)) {
                        cmbselect.append("'"
                                + retailStandardCharges.getChargeType().getId().toString() + "'");

                    } else {
                        cmbselect.append("'"
                                + retailStandardCharges.getChargeType().getId().toString() + "',");
                    }
                }

            }

        } else {
            cmbselect.append("'0'");
        }
        if (cmbselect.length() > 0) {
            if (cmbselect.charAt(cmbselect.length() - 1) == ',') {
                cmbselect.deleteCharAt(cmbselect.length() - 1);
            }
        }

        if (!cmbselect.equals("")) {
            cmbselect.append("'0'");
        }
        Iterator iter = genericCodeDAO.getAllGenericCodesForAGSS(codeType,
                desc, cmbselect);
        genericCodeList.add(new LabelValueBean(label, "0"));
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer cid = (Integer) row[0];
            String cname = (String) row[1];
            genericCodeList.add(new LabelValueBean(cname, cid.toString()));
        }
        return genericCodeList;

    }

    // LIST FOR COMMODITY SPECIFIC ACESSORIAL CHARGES
    public List getTypeListForCssc(Integer codeType, String desc, String label) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        StringBuffer cmbselect = new StringBuffer();
        String code = "11291";
        cmbselect.append("'" + code.toString() + "',");

        cmbselect = cmbselect.append(cmbselect.length() - 1);

        Iterator iter = genericCodeDAO.getAllGenericCodesForAGSS(codeType,
                desc, cmbselect);

        genericCodeList.add(new LabelValueBean(label, "0"));
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer cid = (Integer) row[0];
            String cname = (String) row[1];
            genericCodeList.add(new LabelValueBean(cname, cid.toString()));
        }

        return genericCodeList;
    }

    // LIST FOR ACESSORIAL GENERAL STANDARD CHARGES
    public List getTypeList1(Integer codeType, String desc, String label) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        StringBuffer cmbselect = new StringBuffer();
        String code = "11289";
        cmbselect.append("'" + code.toString() + "',");
        cmbselect = cmbselect.append(cmbselect.length() - 1);
        Iterator iter = genericCodeDAO.getAllGenericCodesForAGSS1(codeType,
                desc, cmbselect);
        genericCodeList.add(new LabelValueBean(label, "0"));
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer cid = (Integer) row[0];
            String cname = (String) row[1];
            genericCodeList.add(new LabelValueBean(cname, cid.toString()));
        }
        return genericCodeList;

    }

    public List getTypeListForRetail(Integer codeType, String desc, String label) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        StringBuffer cmbselect = new StringBuffer();
        String code = "11291";
        cmbselect.append("'" + code.toString() + "',");
        cmbselect = cmbselect.append(cmbselect.length() - 1);
        Iterator iter = genericCodeDAO.getAllGenericCodesForAGSS1(codeType,
                desc, cmbselect);
        genericCodeList.add(new LabelValueBean(label, "0"));
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer cid = (Integer) row[0];
            String cname = (String) row[1];
            genericCodeList.add(new LabelValueBean(cname, cid.toString()));
        }
        return genericCodeList;

    }

    public List getTypeListForCsssedit(Integer codeType, String desc,
            String label, List typeList, String chargeType) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        StringBuffer cmbselect = new StringBuffer();

        if (typeList != null && typeList.size() > 0) {

            for (int i = 0; i < typeList.size(); i++) {

                AirCommodityCharges airCommodityCharges = (AirCommodityCharges) typeList.get(i);
                if (airCommodityCharges.getChargeType() != null) {
                    if (!airCommodityCharges.getChargeType().getId().toString().equals(chargeType)) {
                        if (i == (typeList.size() - 1)) {
                            if (airCommodityCharges.getChargeType() != null) {
                                cmbselect.append("'"
                                        + airCommodityCharges.getChargeType().getId().toString() + "'");
                            }

                        } else {
                            if (airCommodityCharges.getChargeType() != null) {
                                cmbselect.append("'"
                                        + airCommodityCharges.getChargeType().getId().toString() + "',");
                            }
                        }
                    }
                }

            }

        } else {
            cmbselect.append("'0'");
        }
        if (cmbselect.length() > 0) {
            if (cmbselect.charAt(cmbselect.length() - 1) == ',') {
                cmbselect.deleteCharAt(cmbselect.length() - 1);
            }
        }
        if (!cmbselect.equals("")) {
            cmbselect.append("'0'");
        }
        Iterator iter = genericCodeDAO.getAllGenericCodesForAGSS(codeType,
                desc, cmbselect);

        genericCodeList.add(new LabelValueBean(label, "0"));
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer cid = (Integer) row[0];
            String cname = (String) row[1];
            genericCodeList.add(new LabelValueBean(cname, cid.toString()));
        }

        return genericCodeList;
    }

    public List getAirStandardChargesList(RefTerminalTemp org, PortsTemp des) throws Exception {
        List aitStandardChargesList = new ArrayList();
        AirStandardChargesDAO airStandardChargesDAO = new AirStandardChargesDAO();

        Iterator iter = airStandardChargesDAO.getAllAirStandardChargesForDisplay(org, des);
        // Iterator it=iter.iterator();

        while (iter.hasNext()) {
            aitStandardChargesList.add(iter.next());
        }
        return aitStandardChargesList;

    }// ---------------------------------------------------------------

    public List getCoStandardChargesList(RefTerminalTemp org, PortsTemp des) throws Exception {
        List coStandardChargesList = new ArrayList();
        LClStandardChargesDAO lClStandardChargesDAO = new LClStandardChargesDAO();

        Iterator iter = lClStandardChargesDAO.getAllcoStandardChargesForDisplay(org, des);
        // Iterator it=iter.iterator();

        while (iter.hasNext()) {
            coStandardChargesList.add(iter.next());
        }
        return coStandardChargesList;

    }

    public List getRetailStandardChargesList(RefTerminalTemp org, PortsTemp des) throws Exception {
        List aitStandardChargesList = new ArrayList();
        AirStandardChargesDAO retailStandardChargesDAO = new AirStandardChargesDAO();
        Iterator iter = retailStandardChargesDAO.getAllRetailStandardChargesForDisplay(org, des);
        // Iterator it=iter.iterator();
        while (iter.hasNext()) {
            aitStandardChargesList.add(iter.next());
        }
        return aitStandardChargesList;
    }

    public List getAllLCLColoadRates() throws Exception {
        List lclColoadRatesList = new ArrayList();
        LCLColoadMasterDAO lCLColoadMasterDAO = new LCLColoadMasterDAO();
        lclColoadRatesList = lCLColoadMasterDAO.findAllLCLColoadRates();
        return lclColoadRatesList;
    }

    public List getAllftfRates() throws Exception {
        List ftfRatesList = new ArrayList();
        FTFMasterDAO ftfMasterDAO = new FTFMasterDAO();
        ftfRatesList = ftfMasterDAO.findAllftfRates();
        return ftfRatesList;
    }

    public List getRetailStandardCharges1List() throws Exception {
        List aitStandardChargesList = new ArrayList();
        AirStandardChargesDAO airStandardChargesDAO = new AirStandardChargesDAO();
        Iterator iter = airStandardChargesDAO.getAllRetailStandardCharges1ForDisplay();
        while (iter.hasNext()) {
            aitStandardChargesList.add(iter.next());
        }
        return aitStandardChargesList;

    }

    public String[] dayList(String strg) {
        StringTokenizer st;
        String st1 = "";
        String[] day = new String[strg.length()];

        int i = 0;
        st = new StringTokenizer(strg, ",");
        while (st.hasMoreTokens()) {
            st1 = st.nextToken();

            day[i] = st1;
            i++;
        }

        return day;
    }

    public String hourslist(String strg) {
        String day = "";
        String st1 = "";
        String hrs = "";
        String mins = "";
        boolean flag = false;
        for (int i = 0; i < strg.length(); i++) {
            if (strg.charAt(i) != ':') {
                if (!flag) {
                    hrs = hrs + strg.charAt(i);
                } else if (flag) {
                    mins = mins + strg.charAt(i);
                }
            } else if (strg.charAt(i) == ':') {
                flag = true;
            }
        }
        return day;
    }

    // -----------------------CO CLoad detail function-------------------------
    public List getCoLoadHistory(Integer lCLColoadMaster) throws Exception {

        List coLoadHistoryList = new ArrayList();
        LCLColoadDetailsHistoryDAO lCLColoadDetailsHistoryDAO = new LCLColoadDetailsHistoryDAO();
        coLoadHistoryList = lCLColoadDetailsHistoryDAO.findAllCoLoadHistory(lCLColoadMaster);

        return coLoadHistoryList;

    }// -------------------------------Coomodity------------------------------

    public List getftfHistory(Integer ftfMaster) throws Exception {
        List ftfHistoryList = new ArrayList();
        FTFDetailsHistoryDAO ftfDetailsHistoryDAO = new FTFDetailsHistoryDAO();
        ftfHistoryList = ftfDetailsHistoryDAO.findAllftfHistory(ftfMaster);

        return ftfHistoryList;

    }// ------------------------

    public List getAllCoLoadCommodityHistory(Integer lCLColoadMaster) throws Exception {

        List coCommodityHistoryList = new ArrayList();
        LCLColoadCommodityChargesHistoryDAO lCLColoadCommodityChargesHistoryDAO = new LCLColoadCommodityChargesHistoryDAO();
        coCommodityHistoryList = lCLColoadCommodityChargesHistoryDAO.findAllStandardHistory(lCLColoadMaster);

        return coCommodityHistoryList;

    }

    // -------------------LCL
    // CO------satnrad------------------------------------------------
    // for commodity history
    public List getAllftfCommodityHistory(Integer ftfMaster) throws Exception {

        List ftfCommodityHistoryList = new ArrayList();
        FTFCommodityChargesHistoryDAO ftfCommodityChargesHistoryDAO = new FTFCommodityChargesHistoryDAO();
        ftfCommodityHistoryList = ftfCommodityChargesHistoryDAO.findAllStandardHistory(ftfMaster);

        return ftfCommodityHistoryList;

    }

    public List getAllCoStandardHistory(Integer lCLColoadMaster, Integer code) throws Exception {

        List costandardHistoryList = new ArrayList();
        LCLColoadStandardChargesHistoryDAO lCLColoadStandardChargesHistoryDAO = new LCLColoadStandardChargesHistoryDAO();
        costandardHistoryList = lCLColoadStandardChargesHistoryDAO.findAllStandardHistory(lCLColoadMaster, code);
        return costandardHistoryList;

    }

    // for ftf standard history
    public List getAllftfStandardHistory(Integer ftfMaster, Integer code) throws Exception {

        List ftfstandardHistoryList = new ArrayList();
        FTFStandardChargesHistoryDAO ftfStandardChargesHistoryDAO = new FTFStandardChargesHistoryDAO();
        ftfstandardHistoryList = ftfStandardChargesHistoryDAO.findAllStandardHistory(ftfMaster, code);
        return ftfstandardHistoryList;

    }

    public List getCoLoadDetails(String org, String des,
            String com_code) throws Exception {
        List list = new ArrayList();

        LCLColoadMasterDAO lCLColoadMasterDAO = new LCLColoadMasterDAO();
        list = lCLColoadMasterDAO.findAllDetails(org, des, com_code);

        return list;

    }

    public List getftfDetails(String org, String des,
            String com_code) throws Exception {
        List list = new ArrayList();

        FTFMasterDAO ftfMasterDAO = new FTFMasterDAO();
        list = ftfMasterDAO.findAllDetails(org, des, com_code);

        return list;

    }

    public List getUnitListForFCLTest(Integer codeType, String desc,
            String label) throws Exception {

        List genericCodeList = new ArrayList();

        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();

        StringBuffer cmbselect = new StringBuffer();

        Iterator iter = genericCodeDAO.getAllUnitCodeForFCLTest(codeType, desc);

        genericCodeList.add(new LabelValueBean(label, "0"));
        if (iter != null) {
            while (iter.hasNext()) {

                Object[] row = (Object[]) iter.next();

                Integer cid = (Integer) row[0];

                String cname = (String) row[1];

                genericCodeList.add(new LabelValueBean(cname, cid.toString()));
            }

        }
        return genericCodeList;
    }

    public List getUnitListForFCLTest1(Integer codeType, String desc,
            String label) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        StringBuffer cmbselect = new StringBuffer();
        Iterator iter = genericCodeDAO.getAllUnitCodeForFCLTest1(codeType, desc);
        genericCodeList.add(new LabelValueBean(label, ""));
        if (iter != null) {
            while (iter.hasNext()) {

                Object[] row = (Object[]) iter.next();

                String cid = (String) row[0];

                String cname = (String) row[1];
                genericCodeList.add(new LabelValueBean(cname, cid.toString()));
            }
        }
        return genericCodeList;
    }

    // -----------------------------------------------------------------------------------------
    public List getUnitListUnitypeTest(Integer codeType, String desc,
            String label, List typeList) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        StringBuffer cmbselect = new StringBuffer();
        String code = "11289";
        cmbselect.append("'" + code.toString() + "',");
        if (typeList != null && typeList.size() > 0) {

            for (int i = 0; i < typeList.size(); i++) {

                UniverseFlatRate airStandardCharges = (UniverseFlatRate) typeList.get(i);

                if (i == (typeList.size() - 1)) {
                    if (airStandardCharges.getUnitType() != null) {
                        cmbselect.append("'"
                                + airStandardCharges.getUnitType().getId().toString() + "',");
                    }

                } else {
                    if (airStandardCharges.getUnitType() != null) {
                        cmbselect.append("'"
                                + airStandardCharges.getUnitType().getId().toString() + "',");
                    }
                }

            }

        }
        cmbselect = cmbselect.append(cmbselect.length() - 1);

        Iterator iter = genericCodeDAO.getAllGenericCodesForUni(codeType, desc,
                cmbselect);

        genericCodeList.add(new LabelValueBean(label, "0"));
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer cid = (Integer) row[0];
            String cname = (String) row[1];
            genericCodeList.add(new LabelValueBean(cname, cid.toString()));
        }

        return genericCodeList;

    }

    public List getUnitFCLUnitypeTest(Integer codeType, String desc, String label, List typeList, String bol) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        StringBuffer cmbselect = new StringBuffer();
        String code = "11289";
        Iterator iter = null;
        FclBl fcl = new FclBl();
        BookingFcl bookingFcl = new BookingFcl();
        String fileNo = "";
        if (null != bol && !bol.equals("")) {
            fcl = new FclBlDAO().findById(Integer.parseInt(bol));
            fileNo = fcl.getFileNo();
            bookingFcl = new BookingFclDAO().findbyFileNo(fileNo);
        }
        if (null != bol && !fileNo.contains("-") && new QuotationDAO().isRatedBl(fileNo) && null != bookingFcl) {
            iter = new GenericCodeDAO().getBookingGenericCodesForUnits(bol).iterator();
        } else {
            cmbselect.append("'").append(code.toString()).append("',");
            if (typeList != null && typeList.size() > 0) {
                for (int i = 0; i < typeList.size(); i++) {
                    FclBuyCostTypeRates airStandardCharges = (FclBuyCostTypeRates) typeList.get(i);
                    if (i == (typeList.size() - 1)) {
                        if (airStandardCharges.getUnitType() != null) {
                            cmbselect.append("'").append(airStandardCharges.getUnitType().getId().toString()).append("',");
                        }
                    } else {
                        if (airStandardCharges.getUnitType() != null) {
                            cmbselect.append("'").append(airStandardCharges.getUnitType().getId().toString()).append("',");
                        }
                    }
                }
            }
            cmbselect = cmbselect.append(cmbselect.length() - 1);
            iter = genericCodeDAO.getAllGenericCodesForUni(codeType, desc, cmbselect);
        }
        genericCodeList.add(new LabelValueBean(label, "0"));
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer cid = (Integer) row[0];
            String cname = (String) row[1];
            genericCodeList.add(new LabelValueBean(cname, cid.toString()));
        }
        return genericCodeList;
    }

    public List getUniversalRecords(RefTerminalTemp org, PortsTemp des,
            GenericCode com_code) throws Exception {
        List list = new ArrayList();
        UniversalMasterDAO universalMasterDAO = new UniversalMasterDAO();
        list = universalMasterDAO.findAllDetails(org, des, com_code);
        return list;
    }

    public List getUnitListForFCLTesting(Integer codeType, String desc) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        Iterator iter = genericCodeDAO.getAllUnitCodeForFCLTest(codeType, desc);
        if (iter != null) {
            while (iter.hasNext()) {
                Object[] row = (Object[]) iter.next();
                Integer cid = (Integer) row[0];
                String cname = (String) row[1];
                genericCodeList.add(cname);
            }
        }
        return genericCodeList;
    }

    public List getUnitListForFclaRates(Integer codeType, String desc, MessageResources messageResources) throws Exception {
        List genericCodeList = new ArrayList();
        String UnitTypes = messageResources.getMessage("unittypes");
        String unitTypesArray[] = UnitTypes.split(",");
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        Iterator iter = genericCodeDAO.getAllUnitCodeForFCLTest(codeType, desc);
        if (iter != null) {
            while (iter.hasNext()) {
                Object[] row = (Object[]) iter.next();
                Integer cid = (Integer) row[0];
                String cname = (String) row[1];
                boolean flag = false;
                for (String unitTypesArray1 : unitTypesArray) {
                    if (cid.toString().equals(unitTypesArray1)) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    genericCodeList.add(cname);
                }
            }
        }
        return genericCodeList;
    }

    public List getGenericFCL(Integer codeType, String desc) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        Iterator iter = genericCodeDAO.getAllGenericCostCodesForDisplay(
                codeType, desc);
        if (iter != null) {
            while (iter.hasNext()) {
                Object[] row = (Object[]) iter.next();
                Integer cid = (Integer) row[0];
                String cname = (String) row[1];
                genericCodeList.add(new LabelValueBean(cname, cid.toString()));
            }
        }
        return genericCodeList;
    }

    public List getGenericFCLCode(Integer codeType, String desc) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        Iterator iter = genericCodeDAO.getAllGenericCostCodesForDisplay(codeType, desc);
        genericCodeList.add(new LabelValueBean("Select", ""));
        if (iter != null) {
            while (iter.hasNext()) {
                Object[] row = (Object[]) iter.next();
                Integer cid = (Integer) row[0];
                String cname = (String) row[1];
                genericCodeList.add(new LabelValueBean(cname, cname));
            }
        }
        return genericCodeList;
    }

    public List getGenericFCLforTypeOfMove(Integer codeType, String code, String desc) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        Iterator iter = genericCodeDAO.getAllGenericCostCodesForTypeOfMove(codeType, code, desc);
        genericCodeList.add(new LabelValueBean("Select", "00"));
        if (iter != null) {
            while (iter.hasNext()) {
                Object[] row = (Object[]) iter.next();
                Integer cid = (Integer) row[0];
                String code1 = (String) row[1];
                String cname = (String) row[2];
                genericCodeList.add(new LabelValueBean(cname, cname));
            }
        }
        return genericCodeList;
    }

    public List getGenericFCLforTypeOfMovebooking(Integer codeType, String code, String desc) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        Iterator iter = genericCodeDAO.getAllGenericCostCodesForTypeOfMovebooking(codeType, code, desc);
        genericCodeList.add(new LabelValueBean("Select", "00"));
        if (iter != null) {
            while (iter.hasNext()) {
                Object[] row = (Object[]) iter.next();
                Integer cid = (Integer) row[0];
                String code1 = (String) row[1];
                String cname = (String) row[2];
                genericCodeList.add(new LabelValueBean(cname, cname));
            }
        }
        return genericCodeList;
    }

    public String dayList1(String strg) {
        StringTokenizer st;
        String st1 = "";
        String day = "";
        int i = 0;
        st = new StringTokenizer(strg, ",");
        while (st.hasMoreTokens()) {
            st1 = st.nextToken();
            day = day + st1;
            i++;
        }
        return day.toString();
    }

    public Double getcurrency(CostBean cb, MessageResources messageResources) throws Exception {
        Double totalCharges = 0.00;
        String unitTypes[] = messageResources.getMessage("unittype").split(",");
        if (cb.getSetA() != null && cb.getUnitType().equals(unitTypes[0])) {
            totalCharges = totalCharges
                    + Double.parseDouble(this.dayList1(cb.getSetA()));
        }
        if (cb.getSetB() != null && cb.getUnitType().equals(unitTypes[2])) {
            totalCharges = totalCharges
                    + Double.parseDouble(this.dayList1(cb.getSetB()));
        }
        if (cb.getSetC() != null && cb.getUnitType().equals(unitTypes[4])) {
            totalCharges = totalCharges
                    + Double.parseDouble(this.dayList1(cb.getSetC()));
        }
        if (cb.getSetD() != null && cb.getUnitType().equals(unitTypes[1])) {
            totalCharges = totalCharges
                    + Double.parseDouble(this.dayList1(cb.getSetD()));
        }
        if (cb.getSetE() != null && cb.getUnitType().equals(unitTypes[3])) {
            totalCharges = totalCharges
                    + Double.parseDouble(this.dayList1(cb.getSetE()));
        }
        if (cb.getSetF() != null && cb.getUnitType().equals(unitTypes[5])) {
            totalCharges = totalCharges
                    + Double.parseDouble(this.dayList1(cb.getSetF()));
        }
        if (cb.getSetG() != null && cb.getUnitType().equals(unitTypes[6])) {
            totalCharges = totalCharges
                    + Double.parseDouble(this.dayList1(cb.getSetG()));
        }
        if (cb.getSetI() != null && cb.getUnitType().equals(unitTypes[8])) {
            totalCharges = totalCharges
                    + Double.parseDouble(this.dayList1(cb.getSetI()));
        }
        if (cb.getSetH() != null && cb.getUnitType().equals(unitTypes[7])) {
            totalCharges = totalCharges
                    + Double.parseDouble(this.dayList1(cb.getSetH()));
        }
        if (cb.getSetJ() != null && cb.getUnitType().equals(unitTypes[9])) {
            totalCharges = totalCharges
                    + Double.parseDouble(this.dayList1(cb.getSetJ()));
        }
        if (cb.getSetK() != null && cb.getUnitType().equals(unitTypes[10])) {
            totalCharges = totalCharges
                    + Double.parseDouble(this.dayList1(cb.getSetK()));
        }
        if (cb.getSetL() != null && cb.getUnitType().equals(unitTypes[11])) {
            totalCharges = totalCharges
                    + Double.parseDouble(this.dayList1(cb.getSetL()));
        }
        if (cb.getSetM() != null && cb.getUnitType().equals(unitTypes[12])) {
            totalCharges = totalCharges
                    + Double.parseDouble(this.dayList1(cb.getSetM()));
        }
        if (cb.getSetN() != null && cb.getUnitType().equals(unitTypes[13])) {
            totalCharges = totalCharges
                    + Double.parseDouble(this.dayList1(cb.getSetN()));
        }
        if (cb.getSetO() != null && cb.getUnitType().equals(unitTypes[14])) {
            totalCharges = totalCharges
                    + Double.parseDouble(this.dayList1(cb.getSetO()));
        }
        if (cb.getSetP() != null && cb.getUnitType().equals(unitTypes[15])) {
            totalCharges = totalCharges
                    + Double.parseDouble(this.dayList1(cb.getSetP()));
        }
        if (cb.getSetQ() != null && cb.getUnitType().equals(unitTypes[16])) {
            totalCharges = totalCharges
                    + Double.parseDouble(this.dayList1(cb.getSetQ()));
        }
        if (cb.getMarkUpA() != null && cb.getUnitType().equals(unitTypes[0])) {
            totalCharges = totalCharges
                    + Double.parseDouble(this.dayList1(cb.getMarkUpA()));
        }
        if (cb.getMarkUpB() != null && cb.getUnitType().equals(unitTypes[2])) {
            totalCharges = totalCharges
                    + Double.parseDouble(this.dayList1(cb.getMarkUpB()));
        }
        if (cb.getMarkUpC() != null && cb.getUnitType().equals(unitTypes[4])) {
            totalCharges = totalCharges
                    + Double.parseDouble(this.dayList1(cb.getMarkUpC()));
        }
        if (cb.getMarkUpD() != null && cb.getUnitType().equals(unitTypes[1])) {
            totalCharges = totalCharges
                    + Double.parseDouble(this.dayList1(cb.getMarkUpD()));
        }
        if (cb.getMarkUpE() != null && cb.getUnitType().equals(unitTypes[3])) {
            totalCharges = totalCharges
                    + Double.parseDouble(this.dayList1(cb.getMarkUpE()));
        }
        if (cb.getMarkUpF() != null && cb.getUnitType().equals(unitTypes[5])) {
            totalCharges = totalCharges
                    + Double.parseDouble(this.dayList1(cb.getMarkUpF()));
        }
        if (cb.getMarkUpG() != null && cb.getUnitType().equals(unitTypes[6])) {
            totalCharges = totalCharges
                    + Double.parseDouble(this.dayList1(cb.getMarkUpG()));
        }
        if (cb.getMarkUpH() != null && cb.getUnitType().equals(unitTypes[8])) {
            totalCharges = totalCharges
                    + Double.parseDouble(this.dayList1(cb.getMarkUpH()));
        }
        if (cb.getMarkUpI() != null && cb.getUnitType().equals(unitTypes[7])) {
            totalCharges = totalCharges
                    + Double.parseDouble(this.dayList1(cb.getMarkUpI()));
        }
        if (cb.getMarkUpJ() != null && cb.getUnitType().equals(unitTypes[9])) {
            totalCharges = totalCharges
                    + Double.parseDouble(this.dayList1(cb.getMarkUpJ()));
        }
        if (cb.getMarkUpK() != null && cb.getUnitType().equals(unitTypes[10])) {
            totalCharges = totalCharges
                    + Double.parseDouble(this.dayList1(cb.getMarkUpK()));
        }
        if (cb.getMarkUpL() != null && cb.getUnitType().equals(unitTypes[11])) {
            totalCharges = totalCharges
                    + Double.parseDouble(this.dayList1(cb.getMarkUpL()));
        }
        if (cb.getMarkUpM() != null && cb.getUnitType().equals(unitTypes[12])) {
            totalCharges = totalCharges
                    + Double.parseDouble(this.dayList1(cb.getMarkUpM()));
        }
        if (cb.getMarkUpN() != null && cb.getUnitType().equals(unitTypes[13])) {
            totalCharges = totalCharges
                    + Double.parseDouble(this.dayList1(cb.getMarkUpN()));
        }
        if (cb.getMarkUpO() != null && cb.getUnitType().equals(unitTypes[14])) {
            totalCharges = totalCharges
                    + Double.parseDouble(this.dayList1(cb.getMarkUpO()));
        }
        if (cb.getMarkUpP() != null && cb.getUnitType().equals(unitTypes[15])) {
            totalCharges = totalCharges
                    + Double.parseDouble(this.dayList1(cb.getMarkUpP()));
        }
        if (cb.getMarkUpQ() != null && cb.getUnitType().equals(unitTypes[16])) {
            totalCharges = totalCharges
                    + Double.parseDouble(this.dayList1(cb.getMarkUpQ()));
        }
        return totalCharges;
    }

    public List dayList2(String strg) {
        StringTokenizer st;
        String st1 = "";
        String day = "";
        List alist = new ArrayList();
        int i = 0;
        st = new StringTokenizer(strg, ",");
        while (st.hasMoreTokens()) {
            st1 = st.nextToken();
            alist.add(st1);

            i++;
        }
        return alist;
    }

    public String amountrepre(String amount) {
        String dec = "";
        boolean flag = false;
        String num = "";
        String dec1 = "";
        if (amount.length() > 0) {
            for (int i = amount.length() - 1; i >= 0; i--) {
                if (amount.charAt(i) == '.') {
                    flag = true;
                }
                if (!flag) {
                    dec1 = dec1.concat(String.valueOf(amount.charAt(i)));
                } else if (flag) {

                    dec = dec.concat(String.valueOf(amount.charAt(i)));

                    if (dec.length() % 4 == 0) {
                        dec = dec.concat(",");

                    }

                }
            }
            if (dec.charAt(dec.length() - 1) == ',') {
                dec = dec.substring(0, dec.length() - 1);
            }

            dec1 = dec1.concat(dec);
            for (int i = dec1.length() - 1; i >= 0; i--) {
                num = num.concat(String.valueOf(dec1.charAt(i)));
            }
        }

        return num;
    }

    public GenericCode getCurrency(String codeType, int code) throws Exception {

        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();

        GenericCode genericCode = genericCodeDAO.findByCodeDesc(codeType,
                new Integer(code));
        return genericCode;
    }

    public List getAllUniversalAirHistory(Integer standardid) throws Exception {
        List UniversalHistoryList = new ArrayList();
        UniverseAirFreightHistoryDAO universeCommodityChrgHistoryDAO = new UniverseAirFreightHistoryDAO();
        UniversalHistoryList = universeCommodityChrgHistoryDAO.findAllUniAirHistory(standardid);
        return UniversalHistoryList;
    }

    public List getAllUniversalCommHistory(Integer standardid) throws Exception {

        List UniversalHistoryList = new ArrayList();
        UniverseCommodityChrgHistoryDAO universeCommodityChrgHistoryDAO = new UniverseCommodityChrgHistoryDAO();
        UniversalHistoryList = universeCommodityChrgHistoryDAO.findAllUniCommHistory(standardid);
        return UniversalHistoryList;
    }

    public List getAllUniversalFlatHistory(Integer standardid) throws Exception {

        List UniversalHistoryList = new ArrayList();
        UniverseFlatRateHistoryDAO universeFlatRateHistoryDAO = new UniverseFlatRateHistoryDAO();
        UniversalHistoryList = universeFlatRateHistoryDAO.findAllUniversalHistory(standardid);
        return UniversalHistoryList;
    }

    public List getAllUniversalInsuranceHistory(Integer standardid) throws Exception {

        List UniversalHistoryList = new ArrayList();
        UniverseInsuranceChrgHistoryDAO universeInsuranceChrgHistoryDAO = new UniverseInsuranceChrgHistoryDAO();
        UniversalHistoryList = universeInsuranceChrgHistoryDAO.findAllUniInsuHistory(standardid);
        return UniversalHistoryList;
    }

    public List getUserList(Integer codeType, String desc, String label) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        Iterator iter = genericCodeDAO.getAllUserDetails(codeType, desc);
        genericCodeList.add(new LabelValueBean(label, "0"));
        if (iter != null) {
            while (iter.hasNext()) {
                Object[] row = (Object[]) iter.next();
                Integer cid = (Integer) row[0];

                String cname = (String) row[1];
                genericCodeList.add(new LabelValueBean(cname, cid.toString()));

            }
        }

        return genericCodeList;
    }

    public List getUserListBasedOnRole(String role) throws Exception {
        List userCodeList = new ArrayList();
        UserDAO userDAO = new UserDAO();
        List<User> userList = userDAO.findUserByNameAndRole("", role);
        userCodeList.add(new LabelValueBean("-Select-", "0"));
        if (null != userList && !userList.isEmpty()) {
            for (User user : userList) {
                userCodeList.add(new LabelValueBean(user.getLoginName(), user.getUserId().toString()));
            }
        }
        return userCodeList;
    }

    public List getOneCustomer(String type) throws Exception {

        List customerList = new ArrayList();
        CustomerDAO customerDAO = new CustomerDAO();

        customerList = customerDAO.findAccountNo(type);

        return customerList;

    }

    public List getUnitFreightListUniTest(Integer codeType, String desc,
            String label, List typeList) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        StringBuffer cmbselect = new StringBuffer();
        String code = "11289";
        cmbselect.append("'").append(code.toString()).append("',");
        if (typeList != null && typeList.size() > 0) {

            for (int i = 0; i < typeList.size(); i++) {

                UniverseFlatRate airStandardCharges = (UniverseFlatRate) typeList.get(i);

                if (i == (typeList.size() - 1)) {
                    if (airStandardCharges.getUnitType() != null) {
                        cmbselect.append("'").append(airStandardCharges.getUnitType().getId().toString()).append("',");
                    }

                } else {
                    if (airStandardCharges.getUnitType() != null) {
                        cmbselect.append("'").append(airStandardCharges.getUnitType().getId().toString()).append("',");
                    }
                }

            }

        }
        cmbselect = cmbselect.append(cmbselect.length() - 1);
        Iterator iter = genericCodeDAO.getAllGenericCodesForUni(codeType, desc,
                cmbselect);
        genericCodeList.add(new LabelValueBean(label, "0"));
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer cid = (Integer) row[0];
            String cname = (String) row[1];
            genericCodeList.add(new LabelValueBean(cname, cid.toString()));
        }
        return genericCodeList;
    }

    public List getUnitListUniTest(Integer codeType, String desc, String label,
            List typeList) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        StringBuffer cmbselect = new StringBuffer();
        String code = "11289";
        cmbselect.append("'").append(code.toString()).append("',");
        if (typeList != null && typeList.size() > 0) {
            for (int i = 0; i < typeList.size(); i++) {
                UniverseAirFreight airStandardCharges = (UniverseAirFreight) typeList.get(i);
                if (i == (typeList.size() - 1)) {
                    if (airStandardCharges.getWeightRange() != null) {
                        cmbselect.append("'").append(airStandardCharges.getWeightRange().getId().toString()).append("',");
                    }
                } else {
                    if (airStandardCharges.getWeightRange() != null) {
                        cmbselect.append("'").append(airStandardCharges.getWeightRange().getId().toString()).append("',");
                    }
                }

            }
        }
        cmbselect = cmbselect.append(cmbselect.length() - 1);
        Iterator iter = genericCodeDAO.getAllGenericCodesForUni(codeType, desc, cmbselect);
        genericCodeList.add(new LabelValueBean(label, "0"));
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer cid = (Integer) row[0];
            String cname = (String) row[1];
            genericCodeList.add(new LabelValueBean(cname, cid.toString()));
        }
        return genericCodeList;
    }

    public List getUnitFCLFutureType(Integer codeType, String desc, String label, List typeList) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        StringBuffer cmbselect = new StringBuffer();
        String code = "11289";
        cmbselect.append("'").append(code.toString()).append("',");
        if (typeList != null && typeList.size() > 0) {
            for (int i = 0; i < typeList.size(); i++) {
                FclBuyCostTypeFutureRates airStandardCharges = (FclBuyCostTypeFutureRates) typeList.get(i);
                if (i == (typeList.size() - 1)) {
                    if (airStandardCharges.getUnitType() != null) {
                        cmbselect.append("'").append(airStandardCharges.getUnitType().getId().toString()).append("',");
                    }
                } else {
                    if (airStandardCharges.getUnitType() != null) {
                        cmbselect.append("'").append(airStandardCharges.getUnitType().getId().toString()).append("',");
                    }
                }
            }
        }
        cmbselect = cmbselect.append(cmbselect.length() - 1);
        Iterator iter = genericCodeDAO.getAllGenericCodesForUni(codeType, desc, cmbselect);
        genericCodeList.add(new LabelValueBean(label, "0"));
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer cid = (Integer) row[0];
            String cname = (String) row[1];
            genericCodeList.add(new LabelValueBean(cname, cid.toString()));
        }
        return genericCodeList;
    }

    public List getChangeVoyagesByID(Integer voyageId) throws Exception {
        ChangeVoyageDAO cdao = new ChangeVoyageDAO();
        List acctNameList = cdao.findByVoyageId(voyageId);
        return acctNameList;
    }

    public ReportDTO getPDFReport(Integer quotationNo, String hazmat,
            String reqReportName, String ReportName) throws Exception {
        QuotationDAO qtDAO = new QuotationDAO();
        NumberFormat number = new DecimalFormat("###,###,##0.00");
        Double otheramount1 = 0.00;
        Double otheramount2 = 0.00;
        Double otheramount3 = 0.00;
        Quotation quotation = (Quotation) qtDAO.findById(quotationNo);
        SystemRulesDAO srdao = new SystemRulesDAO();
        List Custaddress = (List) srdao.getCompanyAddress();
        String addr1 = "";
        String addr2 = "";
        String city = "";
        String state = "";
        String phoneno11 = "";
        String email11 = "";
        HashMap hm = new HashMap();
        ReportDTO rdto = new ReportDTO();
        if (Custaddress != null && Custaddress.size() == 6) {
            if (Custaddress.get(0) != null && !Custaddress.get(0).equals("")) {
                addr1 = (String) Custaddress.get(0) + ",";
            }
            if (Custaddress.get(1) != null && !Custaddress.get(1).equals("")) {
                addr2 = (String) Custaddress.get(1) + ",";
            }
            city = (String) Custaddress.get(2) + ",";
            state = (String) Custaddress.get(3) + ",";
            phoneno11 = (String) Custaddress.get(4) + ",";
            email11 = (String) Custaddress.get(5);
        }
        List listofEffectiveDates = null;
        listofEffectiveDates = qtDAO.getListofEffDatesforQuotaion(quotation.getQuoteNo());
        StringBuffer addtoComment = new StringBuffer();
        if (!listofEffectiveDates.isEmpty()) {
            int edsize = listofEffectiveDates.size();
            int ed = 0;
            addtoComment.append("\n");
            String underlined = "\u001F Rate Change Alert";
            addtoComment.append(underlined);
            addtoComment.append("\n");
            while (edsize > ed) {
                addtoComment = addtoComment.append((String) listofEffectiveDates.get(ed));
                addtoComment.append("\n");
                ed++;
            }
        }
        if (quotation != null) {
            GenericCodeDAO gcDAO = new GenericCodeDAO();
            List commentcodes = gcDAO.getAllCommentCodesForReports();

            Double grandtotal = (Double.parseDouble("0.0"));
            Double otherCharges = 0.0;
            Double includedcharges = 0.0;
            if (!addr1.equals(" ") && !addr2.equals("")) {
                hm.put("addressLine1", addr1 + addr2 + city + state
                        + phoneno11 + email11);
            }
            hm.put("transittime", quotation.getTransitTime().toString());
            hm.put("orgprt", "Origin Port");
            hm.put("frm", "From");
            hm.put("to1", "To");
            hm.put("email1", "Email");
            hm.put("company1", "Company");
            hm.put("phone1", "Phone");
            hm.put("fax1", "Fax");
            hm.put("pol1", "POL");
            hm.put("pod1", "POD");
            hm.put("dest", "Destination");
            hm.put("transtime", "Transit Time");
            hm.put("to", quotation.getQuoteBy());
            hm.put("from", quotation.getFrom());
            hm.put("phoneno", quotation.getPhone());
            hm.put("fax", quotation.getFax());
            hm.put("company", quotation.getClientname());
            hm.put("email", quotation.getEmail1());
            hm.put("comments", quotation.getComment1() + addtoComment);
            hm.put("originport", quotation.getPlor());
            hm.put("destination", quotation.getFinaldestination());
            hm.put("pol", quotation.getOrigin_terminal());
            hm.put("pod", quotation.getDestination_port());
            if (quotation.getCommodityPrint().equalsIgnoreCase("on")) {
                hm.put("comod1", "Commodity    :");
                hm.put("commodity", quotation.getCommcode().getCodedesc());
            }
            if (quotation.getCarrierPrint().equalsIgnoreCase("on")) {
                hm.put("carrier1", "Carrier            :");
                hm.put("carrier", quotation.getSslname());
            }
            if (quotation.getPrintDesc() == null) {
                quotation.setPrintDesc("off");
            }
            if (quotation.getPrintDesc().equals("on")) {
                hm.put("goodslabel", "Goods Desc");
                hm.put("goodsdesc", quotation.getGoodsdesc());
            }
            if (!quotation.getBaht().toString().equals("0.00")) {
                hm.put("totalcharge1", "TotalCharges(BAHT)");
                hm.put("tc1amt", quotation.getBaht().toString());
            }
            if (!quotation.getBdt().toString().equals("0.00")) {
                hm.put("totalcharge2", "Total Charges(BDT)");
                hm.put("tc2amt", quotation.getBdt().toString());
            }
            if (!quotation.getCyp().toString().equals("0.00")) {
                hm.put("totalcharge3", "Total Charges(CYP)");
                hm.put("tc3amt", quotation.getCyp().toString());
            }
            if (!quotation.getEur().toString().equals("0.00")) {
                hm.put("totalcharge4", "Total Charges(EUR)");
                hm.put("tc4amt", quotation.getEur().toString());
            }
            if (!quotation.getHkd().toString().equals("0.00")) {
                hm.put("totalcharge5", "Total Charges(HKD)");
                hm.put("tc5amt", quotation.getHkd().toString());
            }
            if (!quotation.getLkr().toString().equals("0.00")) {
                hm.put("totalcharge6", "Total Charges(LKR)");
                hm.put("tc6amt", quotation.getLkr().toString());
            }
            if (!quotation.getNt().toString().equals("0.00")) {
                hm.put("totalcharge7", "Total Charges(NT)");
                hm.put("tc7amt", quotation.getNt().toString());
            }
            if (!quotation.getPrs().toString().equals("0.00")) {
                hm.put("totalcharge8", "Total Charges(PRS)");
                hm.put("tc8amt", quotation.getPrs().toString());
            }
            if (!quotation.getRmb().toString().equals("0.00")) {
                hm.put("totalcharge9", "Total Charges(RMB)");
                hm.put("tc9amt", quotation.getRmb().toString());
            }
            if (!quotation.getWon().toString().equals("0.00")) {
                hm.put("totalcharge10", "Total Charges(WON)  ");
                hm.put("tc10amt", quotation.getWon().toString());
            }
            if (!quotation.getYen().toString().equals("0.00")) {
                hm.put("totalcharge11", "Total Charges(YEN)");
                hm.put("tc11amt", quotation.getYen().toString());
            }
            /*if (quotation.getLdinclude().equals("on")
             && quotation.getLdprint().equals("on")) {
             hm.put("localdryage", "Local Dryage");
             hm.put("localdryageamt", number.format(quotation
             .getAmount()));
             grandtotal = grandtotal + quotation.getAmount();
             }
             if (quotation.getLdinclude().equals("on")
             && quotation.getLdprint().equals("off")) {
             otheramount1 = quotation.getAmount();
             }*/
            if (quotation.getIdinclude().equals("on")
                    && quotation.getIdprint().equals("on")) {
                hm.put("intermodal", "Intermodal");
                hm.put("intmodelamt", number.format(quotation.getAmount1()));
                grandtotal = grandtotal + quotation.getAmount1();
            }
            if (quotation.getIdinclude().equals("on")
                    && quotation.getIdprint().equals("off")) {
                otheramount2 = quotation.getAmount1();
            }
            if (quotation.getInsureinclude().equals("on")
                    && quotation.getInsureprint().equals("on")) {
                hm.put("insurance", "Insurance");
                hm.put("insurenceamt", number.format(quotation.getInsurancamt()));
                grandtotal = grandtotal + quotation.getInsurancamt();
            }
            if (quotation.getInsureinclude().equals("on")
                    && quotation.getInsureprint().equals("off")) {
                otheramount3 = quotation.getInsurancamt();
            }
            /*if ((quotation.getLdinclude().equals("on") && quotation
             .getLdprint().equals("off"))
             || (quotation.getIdinclude().equals("on") && quotation
             .getIdprint().equals("off"))
             || (quotation.getInsureinclude().equals("on") && quotation
             .getInsureprint().equals("off"))) {
             Double othertotal = (otheramount1 + otheramount2 + otheramount3);
             hm.put("otherTotal", "Other Charges");
             hm.put("otherchargesamt", number.format(othertotal));
             grandtotal = grandtotal + othertotal;
             }*/
            hm.put("quotationno", quotation.getQuoteNo());
            int commentSize = commentcodes.size();
            for (int j = 0; j < commentSize; j++) {
                if (j == 0) {
                    hm.put("comment1", commentcodes.get(j));
                }
                if (j == 1) {
                    hm.put("comment2", commentcodes.get(j));
                }
                if (j == 2) {
                    hm.put("comment3", commentcodes.get(j));
                }
                if (j == 3) {
                    hm.put("comment4", commentcodes.get(j));
                }
                if (j == 4) {
                    hm.put("comment5", commentcodes.get(j));
                }
                if (j == 5) {
                    hm.put("comment6", commentcodes.get(j));
                }
                if (j == 6) {
                    hm.put("comment7", commentcodes.get(j));
                }
                if (j == 7) {
                    hm.put("comment8", commentcodes.get(j));
                }
                if (j == 8) {
                    hm.put("comment9", commentcodes.get(j));
                }
            }
            hm.put("othercharges", "Other Charges");
            ChargesDAO chDao = new ChargesDAO();
            List otherChargesLIst = (List) chDao.getQuoteId(quotation.getQuoteId());
            Charges charges = null;
            int temp = 0;
            int temp1 = 0;
            int temp2 = 0;
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            for (int i = 0; i < otherChargesLIst.size(); i++) {
                charges = (Charges) otherChargesLIst.get(i);
                String retailamt = "0.00";
                if (charges.getRetail() != null) {
                    retailamt = number.format(charges.getRetail());
                }
                String futureRates = "0.00";
                if (charges.getFutureRate() != null) {
                    futureRates = number.format(charges.getFutureRate());
                }
                if (charges.getOtherinclude() == null) {
                    charges.setOtherinclude("off");
                }
                if (charges.getPrint() == null) {
                    charges.setOtherprint("off");
                }
                if (charges.getCurrecny().startsWith("USD")) {
                    if (temp1 == 0
                            && charges.getOtherinclude().equals("on")
                            && charges.getPrint().equals("on")) {
                        hm.put("oc1", charges.getChgCode());
                        hm.put("oc1amt", retailamt);
                        if (charges.getEfectiveDate() != null) {
                            hm.put("oc1ed", "Effective "
                                    + sdf.format(charges.getEfectiveDate())
                                    + " the new rate will be");
                            if (charges.getFutureRate() != null) {
                                hm.put("oc1newrate", futureRates);
                            } else {
                                hm.put("oc1newrate", "0.00");
                            }
                        }
                        if (retailamt.contains(",")) {
                            retailamt = retailamt.replace(",", "");
                            Double ocamt1 = Double.parseDouble(retailamt);
                            otherCharges = otherCharges + ocamt1;
                        } else {
                            otherCharges = otherCharges
                                    + Double.parseDouble(retailamt);
                        }
                    } else if (temp1 == 0
                            && charges.getOtherinclude().equals("on")) {
                        if (retailamt.contains(",")) {
                            retailamt = retailamt.replace(",", "");
                            Double ocamt1 = Double.parseDouble(retailamt);
                            includedcharges = includedcharges + ocamt1;
                        } else {
                            includedcharges = includedcharges
                                    + Double.parseDouble(retailamt);
                        }
                    } else if (temp1 == 1
                            && charges.getOtherinclude().equals("on")
                            && charges.getOtherprint().equals("on")) {
                        hm.put("oc2", charges.getChgCode());
                        hm.put("oc2amt", retailamt);
                        if (charges.getEfectiveDate() != null) {
                            hm.put("oc2ed", "Effective "
                                    + sdf.format(charges.getEfectiveDate())
                                    + " the new rate will be");
                            if (futureRates != null) {
                                hm.put("oc2newrate", futureRates);
                            } else {
                                hm.put("oc2newrate", "0.00");
                            }

                        }
                        if (retailamt.contains(",")) {
                            retailamt = retailamt.replace(",", "");
                            Double ocamt1 = Double.parseDouble(retailamt);
                            otherCharges = otherCharges + ocamt1;
                        } else {
                            otherCharges = otherCharges
                                    + Double.parseDouble(retailamt);
                        }

                    } else if (temp1 == 1
                            && charges.getOtherinclude().equals("on")) {
                        if (retailamt.contains(",")) {
                            retailamt = retailamt.replace(",", "");
                            Double ocamt1 = Double.parseDouble(retailamt);
                            includedcharges = includedcharges + ocamt1;
                        } else {
                            includedcharges = includedcharges
                                    + Double.parseDouble(retailamt);
                        }
                    } else if (temp1 == 2
                            && charges.getOtherinclude().equals("on")
                            && charges.getOtherprint().equals("on")) {
                        hm.put("oc3", charges.getChgCode());
                        hm.put("oc3amt", retailamt);
                        if (charges.getEfectiveDate() != null) {
                            hm.put("oc3ed", "Effective "
                                    + sdf.format(charges.getEfectiveDate())
                                    + "the new rate will be");
                            if (futureRates != null) {
                                hm.put("oc3newrate", futureRates);
                            } else {
                                hm.put("oc3newrate", "0.00");
                            }

                        }
                        if (retailamt.contains(",")) {
                            retailamt = retailamt.replace(",", "");
                            Double ocamt1 = Double.parseDouble(retailamt);
                            otherCharges = otherCharges + ocamt1;
                        } else {
                            otherCharges = otherCharges
                                    + Double.parseDouble(retailamt);
                        }

                    } else if (temp1 == 2
                            && charges.getOtherinclude().equals("on")) {
                        if (retailamt.contains(",")) {
                            retailamt = retailamt.replace(",", "");
                            Double ocamt1 = Double.parseDouble(retailamt);
                            includedcharges = includedcharges + ocamt1;
                        } else {
                            includedcharges = includedcharges
                                    + Double.parseDouble(retailamt);
                        }
                    } else if (temp1 == 3
                            && charges.getOtherinclude().equals("on")
                            && charges.getOtherprint().equals("on")) {
                        hm.put("oc4", sdf.format(charges.getEfectiveDate()));
                        hm.put("oc4amt", retailamt);
                        if (charges.getEfectiveDate() != null) {
                            hm.put("oc4ed", "Effective Date "
                                    + sdf.format(charges.getEfectiveDate()));
                            if (futureRates != null) {
                                hm.put("oc4newrate", futureRates);
                            } else {
                                hm.put("oc4newrate", "0.00");
                            }

                        }
                        if (retailamt.contains(",")) {
                            retailamt = retailamt.replace(",", "");
                            Double ocamt1 = Double.parseDouble(retailamt);
                            otherCharges = otherCharges + ocamt1;
                        } else {
                            otherCharges = otherCharges
                                    + Double.parseDouble(retailamt);
                        }

                    } else if (temp1 == 3
                            && charges.getOtherinclude().equals("on")) {
                        if (retailamt.contains(",")) {
                            retailamt = retailamt.replace(",", "");
                            Double ocamt1 = Double.parseDouble(retailamt);
                            includedcharges = includedcharges + ocamt1;
                        } else {
                            includedcharges = includedcharges
                                    + Double.parseDouble(retailamt);
                        }
                    } else if (temp1 == 4
                            && charges.getOtherinclude().equals("on")
                            && charges.getOtherprint().equals("on")) {
                        hm.put("oc5", charges.getChgCode());
                        hm.put("oc5amt", retailamt);
                        if (charges.getEfectiveDate() != null) {
                            hm.put("oc5ed", "Effective Date "
                                    + sdf.format(charges.getEfectiveDate()));
                            if (futureRates != null) {
                                hm.put("oc5newrate", futureRates);
                            } else {
                                hm.put("oc5newrate", "0.00");
                            }
                        }
                        if (retailamt.contains(",")) {
                            retailamt = retailamt.replace(",", "");
                            Double ocamt1 = Double.parseDouble(retailamt);
                            otherCharges = otherCharges + ocamt1;
                        } else {
                            otherCharges = otherCharges
                                    + Double.parseDouble(retailamt);
                        }

                    } else if (temp1 == 4
                            && charges.getOtherinclude().equals("on")) {
                        if (retailamt.contains(",")) {
                            retailamt = retailamt.replace(",", "");
                            Double ocamt1 = Double.parseDouble(retailamt);
                            includedcharges = includedcharges + ocamt1;
                        } else {
                            includedcharges = includedcharges
                                    + Double.parseDouble(retailamt);
                        }
                    }

                    temp1++;// To increment Temp1

                } // If the Currency is NON USD
                else {
                    if (temp2 == 0
                            && charges.getOtherinclude().equals("on")
                            && charges.getOtherprint().equals("on")) {
                        hm.put("noc1", charges.getChgCode());
                        hm.put("noc1amt", retailamt);
                        hm.put("cur1", charges.getCurrecny());
                        if (charges.getEfectiveDate() != null) {
                            hm.put("noc1ed", "Effective Date "
                                    + sdf.format(charges.getEfectiveDate()));
                            if (charges.getFutureRate() != null) {
                                hm.put("noc1newrate", futureRates);
                            } else {
                                hm.put("noc1newrate", "0.00");
                            }

                        }
                        if (retailamt.contains(",")) {
                            retailamt = retailamt.replace(",", "");
                            Double ocamt1 = Double.parseDouble(retailamt);
                        }
                    } else if (temp2 == 0
                            && charges.getOtherinclude().equals("on")) {
                        if (retailamt.contains(",")) {
                            retailamt = retailamt.replace(",", "");
                            Double ocamt1 = Double.parseDouble(retailamt);
                        }
                    } else if (temp2 == 1
                            && charges.getOtherinclude().equals("on")
                            && charges.getOtherprint().equals("on")) {
                        hm.put("noc2", charges.getChgCode());
                        hm.put("noc2amt", retailamt);
                        hm.put("cur2", charges.getCurrecny());
                        if (charges.getEfectiveDate() != null) {
                            hm.put("noc2ed", "Effective Date "
                                    + sdf.format(charges.getEfectiveDate()));
                            if (futureRates != null) {
                                hm.put("noc2newrate", futureRates);
                            } else {
                                hm.put("noc2newrate", "0.00");
                            }

                        }
                        if (retailamt.contains(",")) {
                            retailamt = retailamt.replace(",", "");
                            Double ocamt1 = Double.parseDouble(retailamt);
                        }
                    } else if (temp2 == 1
                            && charges.getOtherinclude().equals("on")) {
                        if (retailamt.contains(",")) {
                            retailamt = retailamt.replace(",", "");
                            Double ocamt1 = Double.parseDouble(retailamt);
                        }
                    } else if (temp2 == 2
                            && charges.getOtherinclude().equals("on")
                            && charges.getOtherprint().equals("on")) {
                        hm.put("noc3", charges.getChgCode());
                        hm.put("noc3amt", retailamt);
                        hm.put("cur3", charges.getCurrecny());
                        if (charges.getEfectiveDate() != null) {
                            hm.put("noc3ed", "Effective Date "
                                    + sdf.format(charges.getEfectiveDate()));
                            if (futureRates != null) {
                                hm.put("noc3newrate", futureRates);
                            } else {
                                hm.put("noc3newrate", "0.00");
                            }

                        }
                        if (retailamt.contains(",")) {
                            retailamt = retailamt.replace(",", "");
                            Double ocamt1 = Double.parseDouble(retailamt);
                        }
                    } else if (temp2 == 2
                            && charges.getOtherinclude().equals("on")) {
                        if (retailamt.contains(",")) {
                            retailamt = retailamt.replace(",", "");
                            Double ocamt1 = Double.parseDouble(retailamt);
                        }
                    } else if (temp2 == 3
                            && charges.getOtherinclude().equals("on")
                            && charges.getOtherprint().equals("on")) {
                        hm.put("noc4", charges.getChgCode());
                        hm.put("noc4amt", retailamt);
                        hm.put("cur4", charges.getCurrecny());
                        if (charges.getEfectiveDate() != null) {
                            hm.put("noc4ed", "Effective Date "
                                    + sdf.format(charges.getEfectiveDate()));
                            if (futureRates != null) {
                                hm.put("noc4newrate", futureRates);
                            } else {
                                hm.put("noc4newrate", "0.00");
                            }

                        }
                        if (retailamt.contains(",")) {
                            retailamt = retailamt.replace(",", "");
                            Double ocamt1 = Double.parseDouble(retailamt);
                        }
                    } else if (temp2 == 3
                            && charges.getOtherinclude().equals("on")) {
                        if (retailamt.contains(",")) {
                            retailamt = retailamt.replace(",", "");
                            Double ocamt1 = Double.parseDouble(retailamt);
                        }
                    } else if (temp2 == 4
                            && charges.getOtherinclude().equals("on")
                            && charges.getOtherprint().equals("on")) {
                        hm.put("noc5", charges.getChgCode());
                        hm.put("noc5amt", retailamt);
                        hm.put("cur5", charges.getCurrecny());
                        if (charges.getEfectiveDate() != null) {
                            hm.put("noc5ed", "Effective Date "
                                    + sdf.format(charges.getEfectiveDate()));
                            if (futureRates != null) {
                                hm.put("noc5newrate", futureRates);
                            } else {
                                hm.put("noc5newrate", "0.00");
                            }
                        }
                        if (retailamt.contains(",")) {
                            retailamt = retailamt.replace(",", "");
                            Double ocamt1 = Double.parseDouble(retailamt);
                        }
                    } else if (temp2 == 4
                            && charges.getOtherinclude().equals("on")) {
                        if (retailamt.contains(",")) {
                            retailamt = retailamt.replace(",", "");
                            Double ocamt1 = Double.parseDouble(retailamt);
                        }
                    }
                    temp2++;
                }
                temp++;
            }
            if (hazmat.equals("Y")) {
                hm.put("unNo", "UnNo");
                hm.put("emRespTellNo", "EmRespTellNo");
                hm.put("propshippingName", "ProperShippingName");
                hm.put("hazardClass", "HazardClass");

                List hazmatList = null;
                SearchQuotationBean sqbean = null;
                QuotationDAO quotationDAO = new QuotationDAO();
                hm.put("hazmatDetails", "HAZMAT Details");
                HazmatMaterialDAO hazmatDAo = new HazmatMaterialDAO();
                hazmatList = (List) hazmatDAo.Hazmatlist(quotation.getQuoteId());
                if (!hazmatList.isEmpty()) {
                    sqbean = (SearchQuotationBean) hazmatList.get(0);
                    String un1 = sqbean.getUnNo1();
                    String ertellno1 = sqbean.getEmRespTellNo1();
                    String pname1 = sqbean.getPropsName1();
                    String hazc1 = sqbean.getHazardClass1();
                    hm.put("unNo1", un1);
                    hm.put("emRespTellNo1", ertellno1);
                    hm.put("propsName1", pname1);
                    hm.put("hazardClass1", hazc1);
                }
            }
            List QtFieldsList = null;
            QtFieldsList = (List) qtDAO.getQtFieldsList(quotation.getQuoteNo());

            int lsize = 0;
            QuotationDTO qdto = new QuotationDTO();
            int QtFieldssize = QtFieldsList.size();
            while (QtFieldssize > 0) {
                qdto = (QuotationDTO) QtFieldsList.get(lsize);
                String t = qdto.getTotal();
                if (t.contains(",")) {
                    t = t.replace(",", "");
                }
                grandtotal = grandtotal + Double.parseDouble(t);
                qdto = null;
                QtFieldssize--;
                lsize++;
            }
            grandtotal = grandtotal + otherCharges + includedcharges;
            hm.put("tot", number.format(grandtotal));
            if (QtFieldsList.size() > 1) {
                reqReportName = "FCLquotes";
            }
            if (includedcharges != null && includedcharges > 0) {
                hm.put("otherTotal", "Others Total");
                hm.put("otherchargesamt", number.format(includedcharges));
            }
            rdto = EconoHelper.sendToReport(hm, true,
                    reqReportName + ".jasper", true, ReportName, QtFieldsList);
        }
        return rdto;
    }

    // Get charge code by ChargeCodeType,ChargeCode
    public List getChargeCode(String chargeCodeType, String chargeCode) throws Exception {
        GenericCodeDAO genericDAO = new GenericCodeDAO();
        List codeList = genericDAO.findGenericCode("", chargeCode);
        return codeList;
    }

    public List getTypesOfMove() throws Exception {
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        List list = genericCodeDAO.findForGenericAction(new Integer("48"), null, null);
        return list;
    }

    public List<String> getAllInvoiceNumberByStatusAndAccNo(String status, String accountNumber, String invoiceNumber) {
        ApInvoiceDAO invoiceDAO = new ApInvoiceDAO();
        return invoiceDAO.getAllInvoiceNumberByStatusAndAccNo(status, accountNumber, invoiceNumber);
    }

    public List uniTypeList() throws Exception {
        List unitTypeList = new ArrayList();
        GenericCodeDAO gen = new GenericCodeDAO();
        Iterator iter = gen.getAllUnitCodeForFCLTest(38, "yes");
        unitTypeList.add((new LabelValueBean("Select Unit Types ", "0")));
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer code = (Integer) row[0];
            String desc = (String) row[1];
            unitTypeList.add(new LabelValueBean(desc, code.toString()));
        }
        return unitTypeList;
    }

    public List getAmendmentTypeList() throws Exception {
        List amendmentTypeList = new ArrayList();
        amendmentTypeList.add(new LabelValueBean("Select Amendment Type", "0"));
        amendmentTypeList.add(new LabelValueBean("Add Amount", "A"));
        amendmentTypeList.add(new LabelValueBean("Subtract Amount", "S"));
        amendmentTypeList.add(new LabelValueBean("Increase by Percent", "I"));
        amendmentTypeList.add(new LabelValueBean("Decrease by Percent", "D"));
        return amendmentTypeList;
    }

    public String getTransitDays(Rates fclBuy) throws Exception {
        FclOrgDestMiscDataDAO fclOrgDestMiscDataDAO = new FclOrgDestMiscDataDAO();
        FclOrgDestMiscData fclOrgDestMiscData = new FclOrgDestMiscData();
        List fclOrgDestMiscDataList = fclOrgDestMiscDataDAO.getorgdestmiscdate1(fclBuy.getOriginTerminal(), fclBuy.getDestinationPort(), fclBuy.getAccountNumber());
        if (fclOrgDestMiscDataList.size() > 0) {
            fclOrgDestMiscData = (FclOrgDestMiscData) fclOrgDestMiscDataList.get(0);

        }
        String transitDays = "";
        if (fclOrgDestMiscData.getDaysInTransit() != null) {
            transitDays = fclOrgDestMiscData.getDaysInTransit().toString();
        }
        return transitDays;
    }

    public long getDaysBetweenTwoDays(Date d1, Date d2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(d2);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(d1);
        long milliseconds1 = calendar1.getTimeInMillis();
        long milliseconds2 = calendar2.getTimeInMillis();
        long diff = milliseconds2 - milliseconds1;
        long diffSeconds = diff / 1000;
        long diffMinutes = diff / (60 * 1000);
        long diffHours = diff / (60 * 60 * 1000);
        long diffDays = diff / (24 * 60 * 60 * 1000);
        //long i=(d1.getTime() - d2.getTime() + 60 * 60 * 1000)/( 60 * 60 * 1000*24);
        return diffDays;
    }

    public static String getData(String one, int size) {
        String dataValue = "";
        getString(one, size);
        dataValue = staticData;
        staticData = null;
        staticData = "";
        return dataValue;
    }
    static String staticData = "";

    public static void getString(String one, int size) {
        if (one.length() > size) {
            staticData += one.substring(0, size) + "<br>";
            getString(one.substring(size), size);
        } else {
            staticData += one.substring(0, one.length());
        }
    }

    public List<LabelValueBean> getSubTypeList() {
        List<LabelValueBean> subTypeList = new ArrayList<LabelValueBean>();
        subTypeList.add(new LabelValueBean("Select One", ""));
        subTypeList.add(new LabelValueBean(TradingPartnerConstants.AIR_LINE, TradingPartnerConstants.AIR_LINE));
        subTypeList.add(new LabelValueBean(TradingPartnerConstants.SUB_TYPE_SHIPPER, TradingPartnerConstants.SUB_TYPE_SHIPPER));
        subTypeList.add(new LabelValueBean(TradingPartnerConstants.FORWARDER, TradingPartnerConstants.FORWARDER));
        subTypeList.add(new LabelValueBean(TradingPartnerConstants.SUB_TYPE_CONSIGNEE, TradingPartnerConstants.SUB_TYPE_CONSIGNEE));
        subTypeList.add(new LabelValueBean(TradingPartnerConstants.OVERHEAD, TradingPartnerConstants.OVERHEAD));
        subTypeList.add(new LabelValueBean(TradingPartnerConstants.STEAMSHIP_LINE, TradingPartnerConstants.STEAMSHIP_LINE));
        subTypeList.add(new LabelValueBean(TradingPartnerConstants.TRUCKER, TradingPartnerConstants.TRUCKER));
        subTypeList.add(new LabelValueBean(TradingPartnerConstants.WAREHOUSE, TradingPartnerConstants.WAREHOUSE));
        subTypeList.add(new LabelValueBean(TradingPartnerConstants.SUB_TYPE_IMPORT_AGENT, TradingPartnerConstants.SUB_TYPE_IMPORT_AGENT));
        subTypeList.add(new LabelValueBean(TradingPartnerConstants.SUB_TYPE_EXPORT_AGENT, TradingPartnerConstants.SUB_TYPE_EXPORT_AGENT));
        subTypeList.add(new LabelValueBean(TradingPartnerConstants.SUB_TYPE_IMPORT_CFS, TradingPartnerConstants.SUB_TYPE_IMPORT_CFS));
        return subTypeList;
    }

    public List<LabelValueBean> getShipmentType() {
        List<LabelValueBean> subTypeList = new ArrayList<LabelValueBean>();
        subTypeList.add(new LabelValueBean("Select One", "0"));
        subTypeList.add(new LabelValueBean("FCLI", "FCLI"));
        subTypeList.add(new LabelValueBean("FCLE", "FCLE"));
        subTypeList.add(new LabelValueBean("LCLI", "LCLI"));
        subTypeList.add(new LabelValueBean("LCLE", "LCLE"));
        subTypeList.add(new LabelValueBean("AIRI", "AIRI"));
        subTypeList.add(new LabelValueBean("AIRE", "AIRE"));
        return subTypeList;
    }

    public List<LabelValueBean> getSearchType(String page) {
        List<LabelValueBean> searchByList = new ArrayList<LabelValueBean>();
        searchByList.add(new LabelValueBean("Select One", "0"));
        if (page.trim().equals(CommonConstants.PAGE_AR_INQUIRY)) {
            searchByList.add(new LabelValueBean("Invoice/BL ", CommonConstants.SEARCH_BY_INVOICE_BL));
            searchByList.add(new LabelValueBean("Dock Receipt", CommonConstants.SEARCH_BY_DOCK_RECEIPT));
            searchByList.add(new LabelValueBean("Voyage", CommonConstants.SEARCH_BY_VOYAGE));
            searchByList.add(new LabelValueBean("Check Number", CommonConstants.SEARCH_BY_CHECK_NUMBER));
        } else if (page.trim().equals(CommonConstants.PAGE_AR_APPLYPAYMENT)) {
            searchByList.add(new LabelValueBean("Invoice Number", CommonConstants.SEARCH_BY_INVOICE_NUMBER));
            searchByList.add(new LabelValueBean("Invoice Amount", CommonConstants.SEARCH_BY_INVOICE_AMOUNT));
            searchByList.add(new LabelValueBean("Bill Of Lading", CommonConstants.SEARCH_BY_BILL_LADDING_NUMBER));
            searchByList.add(new LabelValueBean("Dock Receipt", CommonConstants.SEARCH_BY_DOCK_RECEIPT));
            searchByList.add(new LabelValueBean("Voyage", CommonConstants.SEARCH_BY_VOYAGE));
            searchByList.add(new LabelValueBean("Check Number", CommonConstants.SEARCH_BY_CHECK_NUMBER));
            searchByList.add(new LabelValueBean("Check Amount", CommonConstants.SEARCH_BY_CHECK_AMOUNT));
            searchByList.add(new LabelValueBean("Cost Code", CommonConstants.SEARCH_BY_COST_CODE));
            searchByList.add(new LabelValueBean("Container Number", CommonConstants.SEARCH_BY_CONTAINER));
        } else if (page.trim().equals(CommonConstants.PAGE_ACCRUALS)) {
            searchByList.add(new LabelValueBean("Invoice Number", CommonConstants.SEARCH_BY_INVOICE_NUMBER));
            searchByList.add(new LabelValueBean("Container Number", CommonConstants.SEARCH_BY_CONTAINER));
            searchByList.add(new LabelValueBean("Dock Receipt", CommonConstants.SEARCH_BY_DOCK_RECEIPT));
            searchByList.add(new LabelValueBean("House Bill", CommonConstants.SEARCH_BY_HOUSE_BILL));
            searchByList.add(new LabelValueBean("Sub-House Bill", CommonConstants.SEARCH_BY_SUB_HOUSE_BILL));
            searchByList.add(new LabelValueBean("Voyage", CommonConstants.SEARCH_BY_VOYAGE));
            searchByList.add(new LabelValueBean("Master Bill", CommonConstants.SEARCH_BY_MASTER_BILL));
            searchByList.add(new LabelValueBean("Booking Number", CommonConstants.SEARCH_BY_BOOKING_NUMBER));
        }
        return searchByList;
    }

    public List<LabelValueBean> getSortByList(String page, String action) {
        List<LabelValueBean> sortByList = new ArrayList<LabelValueBean>();
        if (null != page && page.trim().equals(CommonConstants.PAGE_SUB_LEDGER)) {
            if (null != action && action.trim().equals(CommonConstants.SORT_FOR_ALL_SUBLEDGERS)) {
                sortByList.add(new LabelValueBean("Transaction Date", CommonConstants.SORT_BY_TRANSACTION_DATE));
                sortByList.add(new LabelValueBean("User", CommonConstants.SORT_BY_USER));
                sortByList.add(new LabelValueBean("Vendor", CommonConstants.SORT_BY_VENDOR));
                sortByList.add(new LabelValueBean("Charge Code", CommonConstants.SORT_BY_CHARGECODE));
                sortByList.add(new LabelValueBean("Bill of Lading", CommonConstants.SORT_BY_BILL_OF_LADDING));
            } else {
                sortByList.add(new LabelValueBean("Charge Code", CommonConstants.SORT_BY_CHARGECODE));
                sortByList.add(new LabelValueBean("GLAccount", CommonConstants.SORT_BY_GL_ACCOUNT));
                sortByList.add(new LabelValueBean("Vendor", CommonConstants.SORT_BY_VENDOR));
            }
        }
        return sortByList;
    }

    public List<LabelValueBean> getChargeCodeList(String revOrExp) throws Exception {
        List<LabelValueBean> chargeCodeList = new ArrayList<LabelValueBean>();
        chargeCodeList.add(new LabelValueBean(CommonConstants.ALL, CommonConstants.ALL));
        GlMappingDAO glMappingDAO = new GlMappingDAO();
        if (null == revOrExp || revOrExp.trim().equals("") || revOrExp.trim().equals(GLMappingConstant.BOTH)) {
            revOrExp = "%";
        }
        chargeCodeList.addAll(glMappingDAO.getChargeCodeByRevOrExp(revOrExp));
        return chargeCodeList;
    }

    public List<LabelValueBean> getRevOrExpList() {
        List<LabelValueBean> revOrExpList = new ArrayList<LabelValueBean>();
        revOrExpList.add(new LabelValueBean("Revenue", GLMappingConstant.REVENUE));
        revOrExpList.add(new LabelValueBean("Expense", GLMappingConstant.EXPENSE));
        revOrExpList.add(new LabelValueBean("Both", GLMappingConstant.BOTH));
        return revOrExpList;
    }

    public void unLockBatchForUser(Integer userId) throws Exception {
        ArBatchDAO arBatchDAO = new ArBatchDAO();
        Transaction tx = arBatchDAO.getCurrentSession().beginTransaction();
        arBatchDAO.unLockBatchforUser(userId);
        tx.commit();
    }

    public String checkNewBl(String fileNo) throws Exception {
        String result = "new";
        if (CommonUtils.isNotEmpty(fileNo)) {
            if (null != new BookingFclDAO().getFileNoObject(fileNo)) {
                result = "edit";
            } else if (null != new QuotationDAO().getFileNoObject(fileNo)) {
                result = "fromQuote";
            }
        }
        return result;
    }

    public String getAESStatus(String fileNo) throws Exception {
        String result = "";
        if (CommonUtils.isNotEmpty(fileNo)) {
            SedFilings sedFilings = new SedFilingsDAO().findByFileNumber(fileNo);
            if (null != sedFilings) {
                result = sedFilings.getStatus();
            }
        }
        return result;
    }

    public String checkNewBooking(String fileNo) throws Exception {
        String result = "new";
        if (CommonUtils.isNotEmpty(fileNo) && null != new QuotationDAO().getFileNoObject(fileNo)) {
            result = "edit";
        }
        return result;
    }

    public boolean killTabProcessInfo(int userid) throws Exception {
        ProcessInfoDAO processinfoDAO = new ProcessInfoDAO();
        Transaction tx = processinfoDAO.getCurrentSession().beginTransaction();
        ProcessInfo pi = new ProcessInfo();
        pi = processinfoDAO.getProcessInfo(userid);
        boolean lockRemoved = false;
        if (pi != null) {
            ProcessInfoHistoryDAO processinfohistoryDAO = new ProcessInfoHistoryDAO();
            ProcessInfoHistory processinfohistory = new ProcessInfoHistory();
            processinfohistory.setUserid(pi.getUserid());
            processinfohistory.setProgramid(pi.getProgramid());
            processinfohistory.setRecordid(pi.getRecordid());
            processinfohistory.setProcessinfodate(pi.getProcessinfodate());
            processinfohistory.setId(pi.getId());
            processinfohistory.setAction(pi.getAction());
            processinfohistory.setEditstatus(pi.getEditstatus());
            processinfohistory.setDeletestatus(pi.getDeletestatus());
            processinfohistoryDAO.save(processinfohistory);
            java.util.Date currdate = new java.util.Date();
            String editstat = "editcancelled";
            pi.setEditstatus(editstat);
            pi.setProcessinfodate(currdate);
            // processinfoDAO.update(pi);

            ProcessInfoHistory pih = new ProcessInfoHistory();
            // ProcessInfo pi1 = new ProcessInfo();
            // String editstatus="editcancelled";
            // pi1=processinfoDAO.getProcess(editstatus);
            // if(pi1!=null)
            // {
            pih.setUserid(pi.getUserid());
            pih.setProgramid(pi.getProgramid());
            pih.setRecordid(pi.getRecordid());
            pih.setProcessinfodate(pi.getProcessinfodate());
            pih.setId(pi.getId());
            pih.setAction(pi.getAction());
            pih.setEditstatus(pi.getEditstatus());
            pih.setDeletestatus(pi.getDeletestatus());
            processinfohistoryDAO.save(pih);
            processinfoDAO.delete(pi);
            lockRemoved = true;
            // }
        }
        tx.commit();
        return lockRemoved;

    }

    public void releaseLockByRecordIdAndModuleId(String recordId, String moduleId, Integer userId) throws Exception {
        new ProcessInfoDAO().releaseLockByRecordIdAndModuleId(recordId, moduleId, userId);
    }

    public List getSelectBoxList(Integer codeTypeId, String fieldName) throws Exception {
        List selectBoxList = new ArrayList();
        selectBoxList.add(new LabelValueBean(fieldName, ""));
        List newList = new GenericCodeDAO().findByCodeTypeid(codeTypeId);
        for (Iterator iterator = newList.iterator(); iterator.hasNext();) {
            GenericCode genericCode = (GenericCode) iterator.next();
            selectBoxList.add(new LabelValueBean(genericCode.getCode(), genericCode.getCode()));
        }
        return selectBoxList;
    }

    public List getSpecialEquipmentUnitList(String unitType) throws Exception {
        List specialEquipmentUnitList = new ArrayList();
        if (CommonUtils.isNotEmpty(unitType)) {
            String unitTypes[] = unitType.split(",");
            for (String string : unitTypes) {
                if (CommonUtils.isNotEmpty(string)) {
                    specialEquipmentUnitList.add(new LabelValueBean(string, string));
                }
            }
        }
        return specialEquipmentUnitList;
    }
}
