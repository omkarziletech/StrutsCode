package com.gp.cvst.logisoft.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.struts.util.LabelValueBean;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.ItemDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cvst.logisoft.AccountingConstants;
import com.gp.cvst.logisoft.domain.AccountGroup;
import com.gp.cvst.logisoft.domain.BankDetails;
import com.gp.cvst.logisoft.domain.Batch;
import com.gp.cvst.logisoft.domain.FiscalPeriod;
import com.gp.cvst.logisoft.domain.JournalEntry;
import com.gp.cvst.logisoft.hibernate.dao.AccountDetailsDAO;
import com.gp.cvst.logisoft.hibernate.dao.AccountGroupDAO;
import com.gp.cvst.logisoft.hibernate.dao.AcctStructureDAO;
import com.gp.cvst.logisoft.hibernate.dao.ArBatchDAO;
import com.gp.cvst.logisoft.hibernate.dao.BatchDAO;
import com.gp.cvst.logisoft.hibernate.dao.BookingFclDAO;
import com.gp.cvst.logisoft.hibernate.dao.CustAddressDAO;
import com.gp.cvst.logisoft.hibernate.dao.FiscalPeriodDAO;
import com.gp.cvst.logisoft.hibernate.dao.FiscalYearDAO;
import com.gp.cvst.logisoft.hibernate.dao.JournalEntryDAO;
import com.gp.cvst.logisoft.hibernate.dao.PaymentsDAO;
import com.gp.cvst.logisoft.hibernate.dao.QuotationDAO;
import com.gp.cvst.logisoft.hibernate.dao.SegmentValuesDAO;
import com.gp.cvst.logisoft.hibernate.dao.SegmentsDAO;
import com.gp.cvst.logisoft.hibernate.dao.SubledgerAcctsDAO;
import com.gp.cvst.logisoft.hibernate.dao.SubledgerDAO;
import com.gp.cvst.logisoft.hibernate.dao.UserBankAccountPermissionDAO;
import com.logiware.ims.client.EmptyLocation;

public class DBUtil {

    public List getSegment1() throws Exception {
        List segment1 = new ArrayList();
        SegmentsDAO seg1 = new SegmentsDAO();
        Iterator iter = seg1.getAllSegment1ForDisplay();

        segment1.add((new LabelValueBean(" ", "")));

        while (iter.hasNext()) {

            String svalue = (String) iter.next();

            //String sname = (String) row[1];
            segment1.add(new LabelValueBean(svalue, svalue));

        }

        return segment1;
    }

    public List limitList() {
        List limitList = new ArrayList();
        //limitList.add(new LabelValueBean("All","All"));
        //limitList.add(new LabelValueBean("100","100"));
        //limitList.add(new LabelValueBean("250", "250"));
        limitList.add(new LabelValueBean("500", "500"));
        limitList.add(new LabelValueBean("1000", "1000"));
        return limitList;
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

    public List uniTypeList1(Map hashMap) throws Exception {
        List unitTypeList = new ArrayList();
        GenericCodeDAO gen = new GenericCodeDAO();
        Iterator iter = gen.getAllUnitCodeForFCLTest(38, "yes");
        unitTypeList.add((new LabelValueBean("Select Unit Types ", "0")));

        if (hashMap.size() > 0) {
            while (iter.hasNext()) {
                Object[] row = (Object[]) iter.next();
                Integer code = (Integer) row[0];
                String desc = (String) row[1];
                if (hashMap.containsKey(code.toString())) {
                    unitTypeList.add(new LabelValueBean(desc, code.toString()));
                }
            }
        } else {
            while (iter.hasNext()) {
                Object[] row = (Object[]) iter.next();
                Integer code = (Integer) row[0];
                String desc = (String) row[1];
                unitTypeList.add(new LabelValueBean(desc, code.toString()));
            }
        }

        return unitTypeList;
    }

    public List getGenericCodeCostList(Integer codeType, String desc, String label) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();

        List<Object[]> iter = genericCodeDAO.getChargeCodeId(null, null, "FCLE", "AR");

        genericCodeList.add(new LabelValueBean(label, "0"));
        if (iter != null) {
            for (Object[] row : iter) {
                Integer cid = (Integer) row[0];
                String cname = (String) row[1];
                genericCodeList.add(new LabelValueBean(cname, cid.toString()));
            }
        }

        return genericCodeList;
    }

    public List getGenericChargeCostList(Integer codeType, String desc, String label, String fileType) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        List<Object[]> iter = null;
        if (CommonUtils.isNotEmpty(fileType) && "importNavigation".equalsIgnoreCase(fileType)) {
            iter = genericCodeDAO.getChargeCodeId(null, null, "FCLI", "AR");
        } else {
            iter = genericCodeDAO.getChargeCodeId(null, null, "FCLE", "AR");
        }

        genericCodeList.add(new LabelValueBean(label, "0"));
        if (iter != null) {
            for (Object[] row : iter) {
                Integer cid = (Integer) row[0];
                String cname = (String) row[1];
                genericCodeList.add(new LabelValueBean(cname, cid.toString()));
            }
        }

        return genericCodeList;
    }

    public List getGenericCodeCostListForLocalDrayage(Integer codeType, String desc, String label) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();

        Iterator iter = genericCodeDAO.getAllGenericCostCodesForDisplay(codeType, desc);

        //genericCodeList.add(new LabelValueBean(label,"0"));
        if (iter != null) {
            while (iter.hasNext()) {
                Object[] row = (Object[]) iter.next();
                Integer cid = (Integer) row[0];
                String cname = (String) row[1];
                if (cname.equalsIgnoreCase("Drayage")) {
                    genericCodeList.add(new LabelValueBean(cname, cid.toString()));
                }
            }
        }

        return genericCodeList;
    }

    public List getGenericCodeCostListInterModal(Integer codeType, String desc, String label) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();

        Iterator iter = genericCodeDAO.getAllGenericCostCodesForDisplay(codeType, desc);

        //genericCodeList.add(new LabelValueBean(label,"0"));
        if (iter != null) {
            while (iter.hasNext()) {
                Object[] row = (Object[]) iter.next();
                Integer cid = (Integer) row[0];
                String cname = (String) row[1];
                if (cname.equalsIgnoreCase("Intermodal")) {
                    genericCodeList.add(new LabelValueBean(cname, cid.toString()));
                }
            }
        }

        return genericCodeList;
    }

    public List getGenericCodeCostListInland(Integer codeType, String desc, String label) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        Iterator iter = genericCodeDAO.getAllGenericCostCodesForDisplay(codeType, desc);
        //genericCodeList.add(new LabelValueBean(label,"0"));
        if (iter != null) {
            while (iter.hasNext()) {
                Object[] row = (Object[]) iter.next();
                Integer cid = (Integer) row[0];
                String cname = (String) row[1];
                if (null != cname && cname.equalsIgnoreCase("deliv".equals(desc)?"deliv":"Inland")) {
                    genericCodeList.add(new LabelValueBean("deliv".equals(desc) && "DELIV".equals(cname)?"DELIVERY":cname, cid.toString()));
                }
            }
        }
        return genericCodeList;
    }

    public List getGenericCodeCostListIntrampMod(Integer codeType, String desc, String label) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();

        Iterator iter = genericCodeDAO.getAllGenericCostCodesForDisplay(codeType, desc);
        if (iter != null) {
            while (iter.hasNext()) {
                Object[] row = (Object[]) iter.next();
                Integer cid = (Integer) row[0];
                String cname = (String) row[1];
                if (cname.equalsIgnoreCase("Intermodal Ramp")) {
                    genericCodeList.add(new LabelValueBean(cname, cid.toString()));
                }
            }
        }

        return genericCodeList;
    }

    public List getChargeCodes(Integer codeType, String desc, String label, String chargeCode) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();

        Iterator iter = genericCodeDAO.getAllGenericCostCodesForDisplay(codeType, desc);

        //genericCodeList.add(new LabelValueBean(label,"0"));
        if (iter != null) {
            while (iter.hasNext()) {
                Object[] row = (Object[]) iter.next();
                Integer cid = (Integer) row[0];
                String cname = (String) row[1];
                if (cname.equalsIgnoreCase(chargeCode)) {
                    genericCodeList.add(new LabelValueBean(cname, cid.toString()));
                }
            }
        }

        return genericCodeList;
    }
//    public List getGenericCodeCostListDeliveryCharge(Integer codeType, String desc, String label)throws Exception {
//        List genericCodeList = new ArrayList();
//        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
//
//        Iterator iter = genericCodeDAO.getAllGenericCostCodesForDisplay(codeType, desc);
//
//        //genericCodeList.add(new LabelValueBean(label,"0"));
//        if (iter != null) {
//            while (iter.hasNext()) {
//                Object[] row = (Object[]) iter.next();
//                Integer cid = (Integer) row[0];
//                String cname = (String) row[1];
//                if (cname.equalsIgnoreCase("Delivery")) {
//                    genericCodeList.add(new LabelValueBean(cname, cid.toString()));
//                }
//            }
//        }
//
//        return genericCodeList;
//    }

    public String getCostCodeForDrayage(String cid) throws Exception {
        GenericCodeDAO genDAO = new GenericCodeDAO();
        GenericCode gen = genDAO.findById(Integer.parseInt(cid));
        return gen.getCode();
    }

    public List getGenericCodeCostListForQuoteCharge(Integer codeType, String desc, String label) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();

        Iterator iter = genericCodeDAO.getAllGenericCostCodesForDisplay(codeType, desc);

        genericCodeList.add(new LabelValueBean(label, "0"));
        if (iter != null) {
            while (iter.hasNext()) {
                Object[] row = (Object[]) iter.next();
                Integer cid = (Integer) row[0];
                String cname = (String) row[1];
                if (cname.equalsIgnoreCase("Flat Rate Per Container") || cname.equalsIgnoreCase("Per BL Charges") || cname.equalsIgnoreCase("per container size") || cname.equalsIgnoreCase("PER TEU")) {
                    genericCodeList.add(new LabelValueBean(cname, cid.toString()));
                }
            }
        }

        return genericCodeList;
    }

    public List getCodeCostListForNonRated(List costList) throws Exception {
        List codeCostList = new ArrayList();
        for (Iterator iterator = costList.iterator(); iterator.hasNext();) {
            LabelValueBean labelValueBean = (LabelValueBean) iterator.next();
            if (null != labelValueBean.getLabel() && !labelValueBean.getLabel().equalsIgnoreCase("Flat Rate Per Container")
                    && !labelValueBean.getLabel().equalsIgnoreCase("Per BL Charges") && !labelValueBean.getLabel().equalsIgnoreCase("PER TEU")) {
                codeCostList.add(labelValueBean);
            }
        }
        return codeCostList;
    }

    public List getCodeCostListForDefaultCustomer(Integer codeType, String desc, String label) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();

        Iterator iter = genericCodeDAO.getAllGenericCostCodesForDisplay(codeType, desc);

        genericCodeList.add(new LabelValueBean(label, "0"));
        if (iter != null) {
            while (iter.hasNext()) {
                Object[] row = (Object[]) iter.next();
                Integer cid = (Integer) row[0];
                String cname = (String) row[1];
                if (cname.equalsIgnoreCase("Per BL Charges") || cname.equalsIgnoreCase("per container size")) {
                    genericCodeList.add(new LabelValueBean(cname, cid.toString()));
                }
            }
        }

        return genericCodeList;
    }

    public List getGenericCodeCostListForQuoteChargeForLocalDrayage(Integer codeType, String desc, String label) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();

        Iterator iter = genericCodeDAO.getAllGenericCostCodesForDisplay(codeType, desc);

        genericCodeList.add(new LabelValueBean(label, "0"));
        if (iter != null) {
            while (iter.hasNext()) {
                Object[] row = (Object[]) iter.next();
                Integer cid = (Integer) row[0];
                String cname = (String) row[1];
                if (cname.equalsIgnoreCase("Flat Rate Per Container") || cname.equalsIgnoreCase("per container size")) {
                    genericCodeList.add(new LabelValueBean(cname, cid.toString()));
                }
            }
        }

        return genericCodeList;
    }

    public List getGenericCodeCostListForQuoteChargeForBreakBulk(Integer codeType, String desc, String label) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();

        Iterator iter = genericCodeDAO.getAllGenericCostCodesForDisplay(codeType, desc);

        genericCodeList.add(new LabelValueBean(label, "0"));
        if (iter != null) {
            while (iter.hasNext()) {
                Object[] row = (Object[]) iter.next();
                Integer cid = (Integer) row[0];
                String cname = (String) row[1];
                if (cname.equalsIgnoreCase("Per BL Charges")) {
                    genericCodeList.add(new LabelValueBean(cname, cid.toString()));
                }
            }
        }

        return genericCodeList;
    }

    public List getGenericFCL(Integer codeType, String desc) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();

        Iterator iter = genericCodeDAO.getAllGenericCostCodesForDisplay(codeType, desc);

        //genericCodeList.add(new LabelValueBean(label,"0"));
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

    public List getGenericFCL1(Integer codeType) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();

        Iterator iter = genericCodeDAO.getAllNotesForDisplay1(codeType);

        //genericCodeList.add(new LabelValueBean(label,"0"));
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

    public String getDecimal(String strg) {
        String day = "";
        String st1 = "";
        String numbers = "";
        String decimals = "";
        boolean flag = false;

        for (int i = 0; i < strg.length(); i++) {
            if (strg.charAt(i) != '.') {
                if (!flag) {
                    numbers = numbers + strg.charAt(i);
                } else if (flag) {
                    decimals = decimals + strg.charAt(i);
                }
            } else if (strg.charAt(i) == '.') {
                flag = true;
            }
        }

        if (strg.length() > 0 && decimals.length() > 2) {
            decimals = decimals.substring(0, 2);
        }
        if (strg.length() > 0 && decimals.length() == 1) {
            decimals = decimals.concat("0");
        }
        if (strg.length() > 0 && decimals.length() == 0) {
            decimals = decimals.concat("00");
        }
        if (strg.length() > 0) {
            numbers = numbers.concat(".").concat(decimals);
        }
        return numbers;
    }

    public List getSegment2() throws Exception {
        List segment2 = new ArrayList();
        SegmentsDAO seg2 = new SegmentsDAO();
        Iterator iter = seg2.getAllSegment2ForDisplay();
        segment2.add((new LabelValueBean(" ", "")));

        while (iter.hasNext()) {
            String svalue = (String) iter.next();
            //			String sname = (String) row[1];
            segment2.add(new LabelValueBean(svalue, svalue));
        }

        return segment2;
    }

    public List getSegment3() throws Exception {
        List segment3 = new ArrayList();
        SegmentsDAO seg3 = new SegmentsDAO();
        Iterator iter = seg3.getAllSegment3ForDisplay();
        segment3.add((new LabelValueBean(" ", "")));

        while (iter.hasNext()) {
            String svalue = (String) iter.next();
//				String sname = (String) row[1];
            segment3.add(new LabelValueBean(svalue, svalue));
        }

        return segment3;
    }

    public List getSegment(Integer id) throws Exception {
        List segment = new ArrayList();
        SegmentValuesDAO seg = new SegmentValuesDAO();
        Iterator iter = seg.getAllValuesForSegment(id);
        segment.add((new LabelValueBean(" ", "")));
        while (iter.hasNext()) {
            String svalue = (String) iter.next();
//				String sname = (String) row[1];
            segment.add(new LabelValueBean(svalue, svalue));
        }
        return segment;
    }

    public List scanScreenName(Integer codetypeid) throws Exception {
        List sourceLedgerList = new ArrayList();
        GenericCodeDAO gcdao = new GenericCodeDAO();
        Iterator it = gcdao.getScanDetails(codetypeid);
        sourceLedgerList.add(new LabelValueBean("--select--", ""));
        while (it.hasNext()) {
            Object row[] = (Object[]) it.next();
            String name = (String) row[0];
            String lable = name;
            if (null != row[1]) {
                lable = (String) name + "<-->" + row[1];
            }
            sourceLedgerList.add(new LabelValueBean(lable, name));
        }
        return sourceLedgerList;
    }

    public List filterScreenName(Integer codetypeid) throws Exception {
        List sourceLedgerList = new ArrayList();
        GenericCodeDAO gcdao = new GenericCodeDAO();
        Iterator it = gcdao.getScanDetails(codetypeid);
        sourceLedgerList.add(new LabelValueBean("ALL", "ALL"));
        while (it.hasNext()) {
            Object row[] = (Object[]) it.next();
            String name = (String) row[0];
            String lable = name;
            if (null != row[1]) {
                lable = (String) name + "<-->" + row[1];
            }
            sourceLedgerList.add(new LabelValueBean(lable, name));
        }
        return sourceLedgerList;
    }

    public List scanDocumentType(Integer codetypeid) throws Exception {
        List sourceLedgerList = new ArrayList();
        GenericCodeDAO gcdao = new GenericCodeDAO();
        Iterator it = gcdao.getDocumentType(codetypeid);
        sourceLedgerList.add(new LabelValueBean("--select--", ""));
        while (it.hasNext()) {
            Object row[] = (Object[]) it.next();
            String name = (String) row[1];
            String lable = (String) row[0];
            sourceLedgerList.add(new LabelValueBean(lable, name));
        }
        return sourceLedgerList;
    }

    public List getAcctDetails() throws Exception {
        List AcctDetails = new ArrayList();
        AccountDetailsDAO adt = new AccountDetailsDAO();
        Iterator iter = adt.getAllAcctDetailsForDisplay();
        AcctDetails.add((new LabelValueBean("Select Account ", "")));

        while (iter.hasNext()) {
            String acctvalue = (String) iter.next();

//				String sname = (String) row[1];
            AcctDetails.add(new LabelValueBean(acctvalue, acctvalue));

        }

        return AcctDetails;
    }

    public List getAcctgroup() throws Exception {
        List Acctgroup = new ArrayList();
        AccountDetailsDAO adt = new AccountDetailsDAO();
        Iterator iter = adt.getAllAcctGroupsForDisplay();
        Acctgroup.add((new LabelValueBean("Select AccountGroup ", "")));

        while (iter.hasNext()) {

            String acctgroupvalue = (String) iter.next();

//				String sname = (String) row[1];
            Acctgroup.add(new LabelValueBean(acctgroupvalue, acctgroupvalue));

        }

        return Acctgroup;
    }

    public List getAcctgroupByType(String acct) throws Exception {
        List Acctgroup = new ArrayList();
        AccountGroupDAO accgroupDao = new AccountGroupDAO();
        Iterator iter = accgroupDao.getAcctGroups(acct);
        Acctgroup.add((new LabelValueBean("Select Acct Group ", "")));

        while (iter.hasNext()) {

            String acctgroupvalue = (String) iter.next();

//				String sname = (String) row[1];
            Acctgroup.add(new LabelValueBean(acctgroupvalue, acctgroupvalue));

        }
        return Acctgroup;
    }

    public List getAccttype() {
        List Accttype = new ArrayList();
        Accttype.add((new LabelValueBean("Select Acct type ", "")));
        Accttype.add((new LabelValueBean("Balance Sheet", "Balance Sheet")));
        Accttype.add((new LabelValueBean("Income Statement", "Income Statement")));
        Accttype.add((new LabelValueBean("Retained Earnings", "Retained Earnings")));
        return Accttype;
    }

    public List getnormalbalance() {
        List normalbalance = new ArrayList();
        normalbalance.add(new LabelValueBean("Debit", "Debit"));
        normalbalance.add(new LabelValueBean("Credit", "Credit"));
        return normalbalance;
    }

    public List getdefaultcurrency() {
        List defaultcurrency = new ArrayList();
        defaultcurrency.add(new LabelValueBean("USD", "USD"));
        defaultcurrency.add(new LabelValueBean("INR", "INR"));
        return defaultcurrency;
    }
    //done by sudheer

    public List getcurrencytype() {
        List Currencytype = new ArrayList();
        Currencytype.add(new LabelValueBean("Select Currency Type", ""));
        Currencytype.add(new LabelValueBean("Source", "Source"));
        Currencytype.add(new LabelValueBean("Functional", "Functional"));
        Currencytype.add(new LabelValueBean("Equivalent", "Equivalent"));
        return Currencytype;
    }
    /*  public List getCurrencyList()
     {
     List CurrencyList=new ArrayList();
     GenericCodeDAO gcdao=new GenericCodeDAO();
     Iterator it=gcdao.getCurrency();
     CurrencyList.add(new LabelValueBean("--select--",""));
    
     while(it.hasNext())
     {
    
     String name=(String)it.next();
     CurrencyList.add(new LabelValueBean(name,name));
     }
    
     return CurrencyList;
     }*/

    public List getbudgetset() {
        List budgetset = new ArrayList();

        //BudgetDAO bdao=new BudgetDAO();
        //Iterator it=bdao.getallbudgetset();
        budgetset.add(new LabelValueBean("1", "1"));
        budgetset.add(new LabelValueBean("2", "2"));
        budgetset.add(new LabelValueBean("3", "3"));
        budgetset.add(new LabelValueBean("4", "4"));

        return budgetset;
    }

    public List getbudgetsetForFiscalPeriod() {
        List budgetset = new ArrayList();
        budgetset.add(new LabelValueBean("1", "1"));
        budgetset.add(new LabelValueBean("2", "2"));
        budgetset.add(new LabelValueBean("3", "3"));
        budgetset.add(new LabelValueBean("4", "4"));

        return budgetset;
    }

    public List getbudgetMethods() {
        List budgetset = new ArrayList();

        budgetset.add(new LabelValueBean("Fixed Amount", "Fixed Amount"));
        budgetset.add(new LabelValueBean("Spread Amount", "Spread Amount"));
        budgetset.add(new LabelValueBean("Base,Amount Increase", "Base,Amount Increase"));
        budgetset.add(new LabelValueBean("Base,Percent Increase", "Base,Percent Increase"));

        return budgetset;
    }

    public List getyearList() throws Exception {
        List yearlist = new ArrayList();

        FiscalPeriodDAO fcdao = new FiscalPeriodDAO();
        Iterator it = fcdao.getallyeardetails();
        //yearlist.add(new LabelValueBean("--select--",""));
        while (it.hasNext()) {
            String year = it.next().toString();
            yearlist.add(new LabelValueBean(year, year));
        }
        return yearlist;

    }

    //for batches
    public List getSourceLedgerList() throws Exception {
        List sourceLedgerList = new ArrayList();
        GenericCodeDAO gcdao = new GenericCodeDAO();
        Iterator it = gcdao.getSourceLedgers();
        sourceLedgerList.add(new LabelValueBean("--select--", ""));

        while (it.hasNext()) {

            String name = (String) it.next();
            sourceLedgerList.add(new LabelValueBean(name, name));
        }

        return sourceLedgerList;
    }

    public List getBatchNumbersList() throws Exception {
        List batchNumbersList = new ArrayList();
        BatchDAO batchDao = new BatchDAO();
        Iterator it = batchDao.getAllBatchNumbers();
        batchNumbersList.add(new LabelValueBean("--select--", ""));
        while (it.hasNext()) {
            String batchName = it.next().toString();
            batchNumbersList.add(new LabelValueBean(batchName, batchName));
        }
        return batchNumbersList;
    }

    public String getBatchNumber() throws Exception {
        BatchDAO batchDao = new BatchDAO();
        String batchno = batchDao.getMaxBatchNumber();
        return batchno;
    }

    public String getNumberOfBatches() throws Exception {
        BatchDAO batchDao = new BatchDAO();
        String no = batchDao.getNumberOFBatches();
        return no;
    }

    public String getNumberOfJournalEntry(String batchId) throws Exception {
        JournalEntryDAO journalEntryDao = new JournalEntryDAO();
        String no = journalEntryDao.getNumberOFJournalEntry(batchId);
        return no;
    }

    public List batchList() throws Exception {
        List batchList = new ArrayList();
        BatchDAO batchDAO = new BatchDAO();
        batchList = batchDAO.findBatchList();
        return batchList;
    }

    public Batch batchList1(String batchId) throws Exception {
        Batch batchList1 = new Batch();
        BatchDAO batchDAO = new BatchDAO();
        batchList1 = batchDAO.findById(batchId);
        return batchList1;
    }

    public JournalEntry journalEntryList1(String batchId) throws Exception {
        JournalEntry journalEntry = new JournalEntry();
        JournalEntryDAO journalEntryDAO = new JournalEntryDAO();
        journalEntry = journalEntryDAO.findById(batchId);
        return journalEntry;
    }
    /*public String getJournalEntryNumber()
     {
     JournalEntryDAO jeDao=new JournalEntryDAO();
     String jeno=jeDao.getMaxJournalEntryNumber();
     return jeno;
     }*/

    public String getAcctgroupDescription(String acctgroup) throws Exception {
        //String acctgroup;
        AccountGroup editdesc = new AccountGroup();
        AccountGroupDAO accgroupDao = new AccountGroupDAO();
        editdesc.setAcctGroup(acctgroup);
        List acgrp = accgroupDao.findByExample(editdesc);
        return ((AccountGroup) acgrp.get(0)).getGroupDesc();
    }
    //by pradeep

    public List getAcctStructureList() throws Exception {
        List codesList = new ArrayList();
        AcctStructureDAO acctstructureDAO = new AcctStructureDAO();
        Iterator iter = acctstructureDAO.getAcctStructureList();
        codesList.add((new LabelValueBean("Select", "0")));

        while (iter.hasNext()) {

            Object[] row = (Object[]) iter.next();
            Integer sid = (Integer) row[0];
            String scode = (String) row[1];
            String sids = String.valueOf(sid);
            codesList.add(new LabelValueBean(scode, sids));

        }

        return codesList;
    }

    public List getClosetoAcctList() throws Exception {
        List closetoAcct = new ArrayList();
        AccountDetailsDAO accdetailsDAO = new AccountDetailsDAO();
        Iterator iter = accdetailsDAO.getClosetoAcctList();
        closetoAcct.add((new LabelValueBean("-Select-", "")));

        while (iter.hasNext()) {
            String svalue = (String) iter.next();
            closetoAcct.add(new LabelValueBean(svalue, svalue));
        }

        return closetoAcct;
    }

    public List getSubLedgeraddList(String account) throws Exception {
        List codesList = new ArrayList();
        SubledgerAcctsDAO subledgeracctsDAO = new SubledgerAcctsDAO();

        codesList = subledgeracctsDAO.getSubledgeraddList(account);
        //codesList.add((new LabelValueBean("Select","0")));
        return codesList;
    }
    //Getting Subledger list from DAo

    public List getSubledgerList() throws Exception {
        List codesList = new ArrayList();
        SubledgerDAO subledgerDAO = new SubledgerDAO();
        Iterator iter = subledgerDAO.getSubledgerList();
        codesList.add((new LabelValueBean("Select SubLedger", "")));
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer sid = (Integer) row[0];
            String scode = (String) row[1];
            codesList.add(new LabelValueBean(scode, sid.toString()));
        }

        return codesList;
    }

    public List getUniqueSubLedgerList() throws Exception {
        List codesList = new ArrayList();
        SubledgerDAO subledgerDAO = new SubledgerDAO();
        Iterator iter = subledgerDAO.getUniqueSubLedgerList().iterator();
        codesList.add((new LabelValueBean("Select SubLedger", "")));
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer sid = (Integer) row[0];
            String scode = (String) row[1];
            codesList.add(new LabelValueBean(scode, sid.toString()));
        }
        return codesList;
    }

    public List getSubledgerListforRL() throws Exception {
        List codesList = new ArrayList();
        SubledgerDAO subledgerDAO = new SubledgerDAO();
        Iterator iter = subledgerDAO.getSubledgerList();
        codesList.add((new LabelValueBean("ALL ", CommonConstants.ALL)));
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer sid = (Integer) row[0];
            String scode = (String) row[1];
            codesList.add(new LabelValueBean(scode, scode));
        }
        codesList.add(new LabelValueBean(CommonConstants.NONE, CommonConstants.NONE));
        return codesList;
    }

    public String getAcctgroupDesc(String acctgroup) {

        AccountGroupDAO accgroupDao = new AccountGroupDAO();
        accgroupDao.getAcctgroupDesc(acctgroup);
        return acctgroup;
    }

    /*public Boolean searchForBatch(Integer batch)
     {
     //List codesList=new ArrayList();
     JournalEntryDAO jeDAO=new JournalEntryDAO();
     Integer batchid=Integer.parseInt(jeDAO.checkForBatch(batch));
     if(batch.equals(batchid))
     {
     return true;
     }
    
     return false;
     }*/

    /*public List getJErelatedtoBatch(Integer batch)
     {
     List codesList=new ArrayList();
     JournalEntryDAO jeDAO=new JournalEntryDAO();
     Iterator iter =jeDAO.getJEdetails(batch);
     codesList.add((new LabelValueBean("Select SubLedger","")));
    
    
     while(iter.hasNext())
     {
     Object[] row = (Object[]) iter.next();
     Integer sid = (Integer) row[0];
     String scode = (String) row[1];
     codesList.add(new LabelValueBean(scode,sid.toString()));
     }
    
    
     return codesList;
     }*/
    /* public List findJesForBatch(String batch)
     {
     List jelist=new ArrayList();
     JournalEntryDAO jeDAO=new JournalEntryDAO();
     Iterator iter =jeDAO.findJesForBatch(batch);
    
     while(iter.hasNext())
     {
     Integer svalue=(Integer)iter.next() ;
    
     jelist.add(svalue);
     }
    
    
     return jelist;
     }*/
    public List getSourcecodeList(Integer codeType, String desc, String label) throws Exception {
        List codelist = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();

        Iterator iter = genericCodeDAO.getAllGenericCodesForDisplay(codeType, desc);
        codelist.add(new LabelValueBean(label, "0"));
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer cid = (Integer) row[0];
            String cname = (String) row[1];
            codelist.add(new LabelValueBean(cname, cid.toString()));
        }
        return codelist;
    }

    public List getperiodList() throws Exception {
        List codelist = new ArrayList();
        FiscalPeriodDAO fpdao = new FiscalPeriodDAO();
        Iterator it = fpdao.getPeriodList();
        codelist.add(new LabelValueBean("--select--", "0"));
        while (it.hasNext()) {
            Object[] row = (Object[]) it.next();

            Integer cid = (Integer) row[0];
            String cname = (String) row[1];
            codelist.add(new LabelValueBean(cname, cid.toString()));
        }
        return codelist;
    }

    public List getperiodList2(String jeperiod) throws Exception {

        List codelist = new ArrayList();
        FiscalPeriodDAO fpdao = new FiscalPeriodDAO();
        Iterator it = fpdao.getPeriodList2(jeperiod);
        codelist.add(new LabelValueBean("--select--", "0"));
        while (it.hasNext()) {
            Object[] row = (Object[]) it.next();
            String cid = (String) row[0];

            String cname = (String) row[0];
            codelist.add(new LabelValueBean(cname, cid));
        }

        return codelist;
    }
    /*public List getAccountList(){
     List  codelist=new ArrayList();
     AccountDetailsDAO accountDetailsDAO=new AccountDetailsDAO();
     Iterator it=accountDetailsDAO.getAccountList();
     codelist.add(new LabelValueBean("Select Account","0"));
     while(it.hasNext())
     {
     Object[] row = (Object[]) it.next();
     String cname = (String) row[0];
     codelist.add(new LabelValueBean(cname,cname));
     }
     return codelist;
     }*/

    public List gethashmapList() {
        List hashList = new ArrayList();
        HashMap hashMap = new HashMap();
        hashMap.put("10005-002", "0");
        hashMap.put("10005-001", "1");
        hashMap.put("10005-003", "2");
        hashList.add("10005-002");
        hashList.add("10005-001");
        hashList.add("10005-003");
        // hashMap.
        Collections.sort(hashList);
        return hashList;
    }

    public String batchid(String strg) {
        int a1 = 0;
        int a2 = 0;
        String ar2 = "";
        for (int i = 0; i < strg.length(); i++) {
            if (strg.charAt(i) == '-') {
                break;
            } else {
                ar2 = ar2.concat(String.valueOf(strg.charAt(i)));
            }
        }
        return ar2;
    }

    public String lineitemidid(String strg) {
        int a1 = 0;
        int a2 = 0;
        String ar2 = "";
        for (int i = 0; i < strg.length(); i++) {
            if (strg.charAt(i) == '-') {
                a2++;
            } else {
                a1++;

            }
            if (a2 == 2) {
                break;
            }
            if (a2 != 2) {
                ar2 = ar2.concat(String.valueOf(strg.charAt(i)));
            }

        }
        return ar2;
    }

    public String lineitemidid1(String strg) {
        int a1 = 0;
        int a2 = 0;
        String ar2 = "";
        for (int i = 0; i < strg.length(); i++) {
            if (strg.charAt(i) == '-') {
                a2++;
            } else {
                a1++;
                if (a2 == 2) {
                    ar2 = ar2.concat(String.valueOf(strg.charAt(i)));
                }
            }
        }

        return ar2;
    }

    public String journalid(String journalid) {
        int a1 = 0;
        int a2 = 0;
        String ar2 = "";
        for (int i = 0; i < journalid.length(); i++) {
            if (journalid.charAt(i) == '-') {
                a2++;
            } else {
                a1++;
            }
            if (a2 == 2) {
                ar2 = ar2.concat(String.valueOf(journalid.charAt(i)));
            }
            if (a1 > 5) {
                ar2 = ar2.concat(String.valueOf(journalid.charAt(i)));
            }
        }
        return ar2;
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

    public String removecomma(String strg) {
        StringTokenizer st;
        String st1 = "";
        String day = "";

        int i = 0;
        st = new StringTokenizer(strg, ",");
        while (st.hasMoreTokens()) {
            st1 = st.nextToken();

            day = day.concat(st1);
            i++;
        }

        return day;
    }

    public List getyearsList() throws Exception {
        FiscalYearDAO fiscalYearDAO = new FiscalYearDAO();
        List yearlist = new ArrayList();
        Iterator fiscalYearList = fiscalYearDAO.yearlist();

        while (fiscalYearList.hasNext()) {
            String year = fiscalYearList.next().toString();
            yearlist.add(new LabelValueBean(year, year));
        }
        return yearlist;
    }

    public List getFiscalYearForStatus(String status) throws Exception {
        FiscalYearDAO fiscalYearDAO = new FiscalYearDAO();
        List yearlist = new ArrayList();
        Iterator fiscalYearList = fiscalYearDAO.getFiscalYearForStatus(status);

        while (fiscalYearList.hasNext()) {
            String year = fiscalYearList.next().toString();
            yearlist.add(new LabelValueBean(year, year));
        }
        return yearlist;
    }

    public List getcurrency() {
        List Currency = new ArrayList();
        Currency.add(new LabelValueBean("USD", "USD"));
        return Currency;
    }

    public List getCurrencyList() throws Exception {
        List CurrencyList = new ArrayList();
        GenericCodeDAO gcdao = new GenericCodeDAO();
        Iterator it = gcdao.getCurrency();
        CurrencyList.add(new LabelValueBean("--select--", ""));

        while (it.hasNext()) {

            String name = (String) it.next();
            CurrencyList.add(new LabelValueBean(name, name));
        }

        return CurrencyList;
    }

    public List getSourcecodeList() throws Exception {
        List codelist = new ArrayList();
        GenericCodeDAO gcdao = new GenericCodeDAO();
        Iterator it = gcdao.getSourcecode();
        codelist.add(new LabelValueBean("--select--", ""));
        while (it.hasNext()) {
            Object[] row = (Object[]) it.next();
            Integer cid = (Integer) row[0];
            String sourcecode = (String) row[1];
            ;
            codelist.add(new LabelValueBean(sourcecode, cid.toString()));
        }
        return codelist;
    }

    public List getfiscalset() {
        List fiscalset = new ArrayList();
        fiscalset.add(new LabelValueBean("Actuals", "Actuals"));
        fiscalset.add(new LabelValueBean("Budget", "Budget"));
        return fiscalset;
    }

    public List getGenericCodeList(Integer codeType, String desc, String label) throws Exception {
        List genericCodeList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        //getAllGenericCodesForDisplay
        Iterator iter = genericCodeDAO.getAllGenericCostCodesForDisplay(codeType, desc);

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

    public String getArBatchNumber() throws Exception {
        ArBatchDAO arbatchDao = new ArBatchDAO();
        String batchno = arbatchDao.getMaxBatchNumber();
        if (batchno == null) {
            batchno = "10000";
        }
        return batchno;
    }

    public List getArBatchList() throws Exception {
        List codesList = new ArrayList();
        ArBatchDAO arbatchDao = new ArBatchDAO();
        Iterator iter = arbatchDao.getArbatchList();
        codesList.add((new LabelValueBean("Select", "")));

        while (iter.hasNext()) {

            String batchid = iter.next().toString();

            codesList.add(new LabelValueBean(batchid, batchid));
        }

        return codesList;
    }

    public List getBankAccountList(Integer userid, String from) throws Exception {
        List bankCodesList = new ArrayList();
        UserBankAccountPermissionDAO ubapDAO = new UserBankAccountPermissionDAO();
        if (from.equals("newArBatch")) {
            Iterator iter = ubapDAO.getBankAccount(userid);
            bankCodesList.add((new LabelValueBean("Select", "0")));
            if (null != iter) {
                while (iter.hasNext()) {
                    BankDetails bankAcct = (BankDetails) iter.next();
                    bankCodesList.add(new LabelValueBean(bankAcct.getBankAcctNo() + ',' + bankAcct.getBankName(), bankAcct.getBankAcctNo()));
                }
            }
            return bankCodesList;
        } else if (from.equals("bankAccount")) {
            List<BankDetails> bankList = new ArrayList<BankDetails>();
            Iterator iter = ubapDAO.getBankAccount(userid);
            if (null != iter) {
                while (iter.hasNext()) {
                    BankDetails bankDetails = (BankDetails) iter.next();
                    bankList.add(bankDetails);
                }
            }
            return bankList;
        } else if (from.equals("apPaymentBankName")) {
            Iterator iter = ubapDAO.getBankAccount(userid);
            //bankCodesList.add((new LabelValueBean("Select","0")));
            if (null != iter) {
                while (iter.hasNext()) {
                    BankDetails bankAcct = (BankDetails) iter.next();
                    bankCodesList.add(bankAcct.getBankName());
                }
            }
            return bankCodesList;
        } else {
            return bankCodesList;
        }
    }

    public List<LabelValueBean> getBankAccountNoList(Integer userid, String bankName) throws Exception {
        List<LabelValueBean> bankCodesList = new ArrayList<LabelValueBean>();
        UserBankAccountPermissionDAO ubapDAO = new UserBankAccountPermissionDAO();
        if (null != bankName && !bankName.trim().equals("")) {
            Iterator iter = ubapDAO.getBankAccount(userid);
            //bankCodesList.add((new LabelValueBean("Select","0")));
            while (iter.hasNext()) {
                BankDetails bankAcct = (BankDetails) iter.next();
                if (null != bankAcct && bankAcct.getBankName().trim().equals(bankName.trim())) {
                    bankCodesList.add(new LabelValueBean(bankAcct.getBankAcctNo(), bankAcct.getStartingSerialNo().toString()));
                }
            }
            return bankCodesList;
        } else {
            return bankCodesList;
        }
    }

    public String getAcctStructDesc1(String acctgrop) throws Exception {
        AcctStructureDAO acctstructDAO = new AcctStructureDAO();
        String st = acctstructDAO.getAcctStructDesc1(acctgrop);
        return st;
        //return null;
    }

    //for fiscal Period
    public List getperiodList1() {
        List periodlist1 = new ArrayList();
        periodlist1.add(new LabelValueBean("-select-", "00"));
        periodlist1.add(new LabelValueBean("12", "12"));
        periodlist1.add(new LabelValueBean("13", "13"));

        return periodlist1;
    }

    public List getmonthList1() {
        List monthList1 = new ArrayList();
        monthList1.add(new LabelValueBean("Jan", "01"));
        monthList1.add(new LabelValueBean("Feb", "02"));
        monthList1.add(new LabelValueBean("Mar", "03"));
        monthList1.add(new LabelValueBean("Apr", "04"));
        monthList1.add(new LabelValueBean("May", "05"));
        monthList1.add(new LabelValueBean("Jun", "06"));
        monthList1.add(new LabelValueBean("Jul", "07"));
        monthList1.add(new LabelValueBean("Aug", "08"));
        monthList1.add(new LabelValueBean("Sep", "09"));
        monthList1.add(new LabelValueBean("Oct", "10"));
        monthList1.add(new LabelValueBean("Nov", "11"));
        monthList1.add(new LabelValueBean("Dec", "12"));
        return monthList1;
    }

    public List getVendortype() {
        List vendortype = new ArrayList();
        vendortype.add(new LabelValueBean("-Select-", AccountingConstants.AR_CREDIT_HOLD_SELECT));
        vendortype.add(new LabelValueBean("F-Forwarder", "F"));
        vendortype.add(new LabelValueBean("S-Shipper", "S"));
        vendortype.add(new LabelValueBean("C-Consignee", "C"));

        return vendortype;
    }

    public List getdept() {
        List dept = new ArrayList();
        dept.add(new LabelValueBean("-Select-", "00"));
        dept.add(new LabelValueBean("Atlanta", "Atlanta"));
        dept.add(new LabelValueBean("US", "US"));
        dept.add(new LabelValueBean("UK", "UK"));

        return dept;
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

    public List getBookingNumbers() throws Exception {
        List genericCodeList = new ArrayList();
        BookingFclDAO bookingfcl = new BookingFclDAO();

        Iterator iter = bookingfcl.getAllbookingNo();
        genericCodeList.add(new LabelValueBean("Select Number", "0"));
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer cid = (Integer) row[0];
            String cname = (String) row[1];
            genericCodeList.add(new LabelValueBean(cname, cid.toString()));
        }

        return genericCodeList;
    }

    /*public List getQuotesNumbers() {
     List genericCodeList=new ArrayList();
     QuotationDAO  quotDAO = new QuotationDAO();
    
     Iterator iter =quotDAO.getAllQuotesNo();
     genericCodeList.add(new LabelValueBean("Select Number","0"));
     while(iter.hasNext())
     {
     Object[] row = (Object[]) iter.next();
     Integer cid = (Integer)row[0];
     String cname = (String) row[1];
     genericCodeList.add(new LabelValueBean(cname,cid.toString()));
     }
    
     return genericCodeList;
     }*/
    public List getyearsList1() throws Exception {
        List yearlist1 = new ArrayList();

        Date d1 = new Date();
        int Currentyear = d1.getYear();
        String CurYear = String.valueOf(Currentyear + 1900);
        String preYear = String.valueOf(Currentyear + 1900 - 1);
        String postYear = String.valueOf(Currentyear + 1900 + 1);

        FiscalYearDAO fisyearDAO = new FiscalYearDAO();
        List ylist = fisyearDAO.distinctYear();

        yearlist1.add(new LabelValueBean("--Select--", "00"));
        Integer Cy = Integer.parseInt(CurYear);
        Integer prey = Integer.parseInt(preYear);
        Integer posty = Integer.parseInt(postYear);
        if (ylist != null) {
            if (!ylist.contains(prey)) {

                yearlist1.add(new LabelValueBean(preYear, preYear));
            }
            if (!ylist.contains(Cy)) {
                yearlist1.add(new LabelValueBean(CurYear, CurYear));
            }
            if (!ylist.contains(posty)) {
                yearlist1.add(new LabelValueBean(postYear, postYear));
            }
        }

        return yearlist1;
    }

    public List findyerlist() throws Exception {

        List yearsList = new ArrayList();
        FiscalYearDAO fisyearDAO = new FiscalYearDAO();
        Iterator iter = (Iterator) fisyearDAO.yearlist();

        yearsList.add((new LabelValueBean("Select", "00")));
        while (iter.hasNext()) {
            Integer year = (Integer) iter.next();
            String years = String.valueOf(year);
            yearsList.add(new LabelValueBean(years, years));
        }

        return yearsList;
    }

    public List getAccountTypeList() {
        List accountTypeList = new ArrayList();
        accountTypeList.add(new LabelValueBean("SELECT", ""));
        accountTypeList.add(new LabelValueBean("SHIPPER", "S"));
        accountTypeList.add(new LabelValueBean("VENDOR", "V"));
        accountTypeList.add(new LabelValueBean("CONSIGNEE", "C"));
        accountTypeList.add(new LabelValueBean("FORWARDER", "F"));
        accountTypeList.add(new LabelValueBean("AGENT", "A"));
        accountTypeList.add(new LabelValueBean("THIRDPARTY", "T"));

        return accountTypeList;
    }

    public List checkType() {
        List checkType = new ArrayList();
        checkType.add(new LabelValueBean("<>", "<>"));
        checkType.add(new LabelValueBean(">", ">"));
        checkType.add(new LabelValueBean("=", "="));
        checkType.add(new LabelValueBean("<", "<"));
        checkType.add(new LabelValueBean(">=", ">="));
        checkType.add(new LabelValueBean("<=", "<="));
        return checkType;

    }

    public List getBlTerms() {
        List blTerms = new ArrayList();
        blTerms.add(new LabelValueBean("All", "All"));
        blTerms.add(new LabelValueBean("Both", "B-Both"));
        blTerms.add(new LabelValueBean("Prepaid", "P-Prepaid"));
        blTerms.add(new LabelValueBean("Collect", "C-Collect"));
        blTerms.add(new LabelValueBean("ThirdParty", "T-3rd Party"));
        // blTerms.add(new LabelValueBean("Both","B-Both"));
        return blTerms;

    }

    public List getUserLoginNames() throws Exception {
        List userList = new ArrayList();
        UserDAO userDAO = new UserDAO();
        userList.add(new LabelValueBean("Select", "0"));
        Iterator itr = userDAO.getUsersForLineManagers();
        while (itr.hasNext()) {
            Object row[] = (Object[]) itr.next();
            Integer cid = (Integer) row[0];
            String cname = (String) row[1];
            userList.add(new LabelValueBean(cname, cid.toString()));
        }
        return userList;
    }

    public FiscalPeriod getPeriodForCurrentDate() throws Exception {
        FiscalPeriodDAO fpdao = new FiscalPeriodDAO();
        FiscalPeriod period = fpdao.getPeriodForCurrentDate();
        while (period != null && !period.getStatus().equalsIgnoreCase("Open")) {
            int id = period.getId();
            id += 1;
            period = fpdao.findById(id);
        }
        return period;
    }

    public List getAccountNumber(String accountNo) throws Exception {
        AccountDetailsDAO accountDetailsDAO = new AccountDetailsDAO();
        List accountList = accountDetailsDAO.findAccountNo(accountNo, accountNo);
        return accountList;
    }

    public List getItemCode(String itemCode) throws Exception {
        ItemDAO itemDAO = new ItemDAO();

        List accountList = itemDAO.getItemCode(itemCode);
        return accountList;
    }

    public List getUnLocationCode(String code, String codeDesc) throws Exception {
        UnLocationDAO unLocationDAO = new UnLocationDAO();
        List accountList = unLocationDAO.findForManagement(code, codeDesc);
        return accountList;
    }

    public List getPrintOnBill() {
        List printBill = new ArrayList();

        printBill.add(new LabelValueBean("Yes", "Yes"));
        printBill.add(new LabelValueBean("No", "No"));

        return printBill;
    }

    public List getCustomers(String customerName) throws Exception {
        CustAddressDAO custAddressDAO = new CustAddressDAO();
        List acctNameList = custAddressDAO.findAcctName(customerName);
        return acctNameList;
    }

    public List getBilltype(Boolean importFlag) {
        List vendortype = new ArrayList();
        vendortype.add(new LabelValueBean("Forwarder", "Forwarder"));
        vendortype.add(new LabelValueBean("Shipper", "Shipper"));
        vendortype.add(new LabelValueBean("ThirdParty", "ThirdParty"));
        if (importFlag) {
            vendortype.add(new LabelValueBean("Consignee", "Consignee"));
            vendortype.add(new LabelValueBean("NotifyParty", "NotifyParty"));
        } else {
            vendortype.add(new LabelValueBean("Agent", "Agent"));
        }
        return vendortype;
    }

    public List getBilltypeLcl(Boolean importFlag) {
        List vendortype = new ArrayList();
        if (importFlag) {
            vendortype.add(new LabelValueBean("Consignee", "Consignee"));
            vendortype.add(new LabelValueBean("Notify", "Notify"));
            vendortype.add(new LabelValueBean("Warehouse", "Warehouse"));
        } else {
            vendortype.add(new LabelValueBean("Forwarder", "Forwarder"));
            vendortype.add(new LabelValueBean("Shipper", "Shipper"));
            vendortype.add(new LabelValueBean("Agent", "Agent"));
        }
        vendortype.add(new LabelValueBean("ThirdParty", "ThirdParty"));
        return vendortype;
    }

    public List getQuotesNumbers() throws Exception {
        List genericCodeList = new ArrayList();
        QuotationDAO quotDAO = new QuotationDAO();

        Iterator iter = quotDAO.getAllQuotesNo();
        genericCodeList.add(new LabelValueBean("Select Number", "0"));
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            Integer cid = (Integer) row[0];
            String cname = (String) row[1];
            genericCodeList.add(new LabelValueBean(cname, cid.toString()));
        }

        return genericCodeList;
    }

    public List getSizeLegend() {
        List sizeLegend = new ArrayList();

        sizeLegend.add(new LabelValueBean("A-20'", "A-20'"));
        sizeLegend.add(new LabelValueBean("A-40 STD", "A-40 STD"));
        sizeLegend.add(new LabelValueBean("C-40 HC", "C-40 HC"));
        return sizeLegend;
    }

    public List getTotalTrls() {
        List totalTrls = new ArrayList();

        totalTrls.add(new LabelValueBean("D-45'", "D-45'"));
        totalTrls.add(new LabelValueBean("E-48", "E-48"));
        totalTrls.add(new LabelValueBean("F-40 REFR", "F-40 REFR"));
        return totalTrls;
    }

    public List getNumberList() throws Exception {
        NumberFormat numberFormatForTwoDigits = new DecimalFormat("00");
        List monthList1 = new ArrayList();
        monthList1.add(new LabelValueBean("-select-", "00"));
        for (int i = 1; i < 100; i++) {
            String numbers = numberFormatForTwoDigits.format(i);
            monthList1.add(new LabelValueBean("" + numbers, "i"));
        }
        return monthList1;
    }

    public List getCustomerListsInNetsBatch(String batchId) throws Exception {
        PaymentsDAO paymentsDAO = new PaymentsDAO();
        List customerNoList = paymentsDAO.distinctCustomersFromPaymentsForBatch(batchId);
        List customerList = new ArrayList();
        // customerList.add(new LabelValueBean("-Select-","00"));
        CustAddressDAO custAddressDAO = new CustAddressDAO();
        for (Iterator iterator = customerNoList.iterator(); iterator.hasNext();) {
            String customerNo = (String) iterator.next();
            customerList.add(new LabelValueBean(custAddressDAO.getCustomerName(customerNo), customerNo));
        }

        return customerList;
    }
    //Transaction Type List In Close Batch

    public List getTransactionType() {
        List transactionTypeList = new ArrayList();
        //transactionTypeList.add(new LabelValueBean("-Select-","00"));
        transactionTypeList.add(new LabelValueBean("AR", "AR"));
        // transactionTypeList.add(new LabelValueBean("AP","AP"));

        return transactionTypeList;
    }

    public String getCustomerAccountNumber(String customerName) throws Exception {
        String customerNumber = "";
        if (customerName.contains(AccountingConstants.DELIMITER_FOR_CUSTOMERNAME_NUMBER)) {
            int i = customerName.lastIndexOf(AccountingConstants.DELIMITER_FOR_CUSTOMERNAME_NUMBER);
            customerNumber = customerName.substring(i);

            customerNumber = customerNumber.replaceAll(AccountingConstants.DELIMITER_FOR_CUSTOMERNAME_NUMBER, "");

        } else {
            CustAddressDAO custAddressDAO = new CustAddressDAO();
            if (null != customerName && !"".equals(customerName.trim())) {
                customerNumber = custAddressDAO.getCustomerNumber(customerName);
            }

        }
        return customerNumber;
    }

    public String getVendorOptional(String code) throws Exception {
        return new GenericCodeDAO().getFieldByCodeAndCodetypeId("36", code, "field8");
    }

    public List getEmptyLocationList(List<EmptyLocation> emptyLocation) {
        List locationList = new ArrayList();
        locationList.add(new LabelValueBean("Select", ""));
        for (EmptyLocation location : emptyLocation) {
            locationList.add(new LabelValueBean(location.getLocationName() + "<-->" + location.getMiles(), location.getLocationName()));
        }
        return locationList;
    }
}
