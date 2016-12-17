package com.gp.cvst.logisoft.hibernate.dao;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.bc.fcl.FclBlConstants;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cvst.logisoft.AccountingConstants;
import com.gp.cvst.logisoft.beans.TransactionBean;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.Transaction;
import com.gp.cvst.logisoft.domain.TransactionLedger;
import com.gp.cvst.logisoft.struts.form.AccrualsForm;
import com.logiware.accounting.model.ManifestModel;
import java.math.BigDecimal;
import org.hibernate.LockOptions;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.type.StringType;

@SuppressWarnings("unchecked")
public class TransactionLedgerDAO extends BaseHibernateDAO implements ConstantsInterface {

    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    NumberFormat number = new DecimalFormat("###,###,##0.00");
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    public void save(TransactionLedger transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public Integer saveAndReturnId(TransactionLedger transientInstance) throws Exception {
        getSession().save(transientInstance);
        return transientInstance.getTransactionId();
    }

    public void update(TransactionLedger transientInstance) throws Exception {
        getSession().update(transientInstance);
        getSession().flush();
        getSession().clear();
    }

    public void delete(TransactionLedger persistentInstance) throws Exception {
        getSession().delete(persistentInstance);
        getSession().flush();
    }

    public TransactionLedger findById(java.lang.Integer id) throws Exception {
        getSession().flush();
        TransactionLedger instance = (TransactionLedger) getSession().get("com.gp.cvst.logisoft.domain.TransactionLedger", id);
        return instance;
    }

    public List findByExample(TransactionLedger instance) throws Exception {
        List results = getSession().createCriteria("com.gp.cvst.logisoft.hibernate.dao.TransactionLedger").add(Example.create(instance)).list();
        return results;
    }

    public List findByProperty(String propertyName, Object value) throws Exception {
        String queryString = "from TransactionLedger as model where model." + propertyName + "= ?0";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", value);
        return queryObject.list();
    }

    public TransactionLedger merge(TransactionLedger detachedInstance) throws Exception {
        TransactionLedger result = (TransactionLedger) getSession().merge(detachedInstance);
        return result;
    }

    public void attachDirty(TransactionLedger instance) throws Exception {
        getSession().saveOrUpdate(instance);
        getSession().flush();
    }

    public List getTransactionLadgerforBolIDToDelete(String bolId) throws Exception {
        String queryString = "from TransactionLedger where billLaddingNo='" + bolId + "'";
        List resultList = getCurrentSession().createQuery(queryString).list();
        return resultList;
    }

    public List findByBolId(String bolId) throws Exception {
        String queryString = "from TransactionLedger where billLaddingNo='" + bolId + "' and status='Open'";
        List resultList = getCurrentSession().createQuery(queryString).list();
        return resultList;
    }

    public void attachClean(TransactionLedger instance) throws Exception {
        getSession().buildLockRequest(LockOptions.NONE).lock(instance);
    }

    public List getTransactionList(String billNumber, String transactionType, String chargeCode) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(TransactionLedger.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (billNumber != null) {
            criteria.add(Restrictions.eq("billLaddingNo", billNumber));
        }
        if (transactionType != null) {
            criteria.add(Restrictions.eq("transactionType", transactionType));
        }
        if (chargeCode != null) {
            criteria.add(Restrictions.eq("chargeCode", chargeCode));
        }
        return criteria.list();
    }

    public List getCostCodeAccruals(TransactionLedger transactionLedger) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(TransactionLedger.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (transactionLedger.getBillLaddingNo() != null) {
            criteria.add(Restrictions.eq("billLaddingNo", transactionLedger.getBillLaddingNo()));
        }
        if (transactionLedger.getTransactionType() != null) {
            criteria.add(Restrictions.eq("transactionType", transactionLedger.getTransactionType()));
        }
        if (transactionLedger.getChargeCode() != null) {
            criteria.add(Restrictions.eq("chargeCode", transactionLedger.getChargeCode()));
        }
        if (transactionLedger.getCustNo() != null) {
            criteria.add(Restrictions.eq("custNo", transactionLedger.getCustNo()));
        }
        if (transactionLedger.getNoticeNumber() != null) {
            criteria.add(Restrictions.eq("noticeNumber", transactionLedger.getNoticeNumber()));
        }
        return criteria.list();
    }

    public List getFFCommCostCodeAccruals(TransactionLedger transactionLedger) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(TransactionLedger.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (transactionLedger.getBillLaddingNo() != null) {
            criteria.add(Restrictions.eq("billLaddingNo", transactionLedger.getBillLaddingNo()));
        }
        if (transactionLedger.getTransactionType() != null) {
            criteria.add(Restrictions.eq("transactionType", transactionLedger.getTransactionType()));
        }
        if (transactionLedger.getChargeCode() != null) {
            criteria.add(Restrictions.eq("chargeCode", transactionLedger.getChargeCode()));
        }
        if (transactionLedger.getCustNo() != null) {
            criteria.add(Restrictions.eq("custNo", transactionLedger.getCustNo()));
        }
        if (transactionLedger.getNoticeNumber() != null) {
            criteria.add(Restrictions.eq("noticeNumber", transactionLedger.getNoticeNumber()));
        }
        criteria.add(Restrictions.or(Restrictions.isNull("accrualsCorrectionFlag"), Restrictions.eq("accrualsCorrectionFlag", "")));
        return criteria.list();
    }

    public List getTransactionListForPBASHIPER(String billNumber, String transactionType, String chargeCode[], String manifest) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(TransactionLedger.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (billNumber != null) {
            criteria.add(Restrictions.eq("billLaddingNo", billNumber));
        }
        if (transactionType != null) {
            criteria.add(Restrictions.eq("transactionType", transactionType));
        }
        if (chargeCode != null) {
            criteria.add(Restrictions.in("chargeCode", chargeCode));
        }
        if (CommonFunctions.isNotNull(manifest)) {
            criteria.add(Restrictions.eq("manifestFlag", manifest));
        }
        return criteria.list();
    }

    public List<TransactionBean> findByTransactionIds(String transactionIds) throws Exception {
        List<TransactionBean> list = new ArrayList<TransactionBean>();
        String queryString = "from TransactionLedger where transactionId in (" + transactionIds + ")";
        List<TransactionLedger> resultList = getCurrentSession().createQuery(queryString).list();
        for (TransactionLedger transactionLedger : resultList) {
            TransactionBean transactionBean = new TransactionBean(transactionLedger);
            list.add(transactionBean);
        }
        return list;
    }

    public List<TransactionLedger> getDistinctTransactionLadger(String billLaddingNo, String transactionType) throws Exception {
        List<TransactionLedger> outList = null;
        String queryString = "FROM TransactionLedger where billLaddingNo=" + "'" + billLaddingNo + "'"
                + " and transactionType='" + CommonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE + "'GROUP BY cust_no";
        outList = getCurrentSession().createQuery(queryString).list();
        return outList;
    }

    public List<TransactionLedger> getAllTransactionLadgerByPassingBillNumber(String billLaddingNo, String transactionType) throws Exception {
        List<TransactionLedger> outList = null;
        String queryString = "FROM TransactionLedger where billLaddingNo=" + "'" + billLaddingNo + "'"
                + " and transactionType='" + CommonConstants.TRANSACTION_TYPE_ACCRUALS + "' and status='" + CommonConstants.STATUS_OPEN + "'";
        if (transactionType != null && transactionType.trim().equalsIgnoreCase(CommonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE)) {
            queryString = "FROM TransactionLedger where billLaddingNo=" + "'" + billLaddingNo + "'"
                    + " and transactionType='" + CommonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE + "'"
                    + "and manifestFlag='" + CommonConstants.YES + "'";
        }
        outList = getCurrentSession().createQuery(queryString).list();
        return outList;
    }

    public List<TransactionLedger> getAllAccrualsByPassingBillNumber(String billLaddingNo) throws Exception {
        List<TransactionLedger> outList = null;
        String queryString = "FROM TransactionLedger where billLaddingNo=" + "'" + billLaddingNo + "'"
                + " and transactionType='" + CommonConstants.TRANSACTION_TYPE_ACCRUALS + "'";
        outList = getCurrentSession().createQuery(queryString).list();
        return outList;
    }
    // need to cheack this methiod I think not in use 

    public List<TransactionLedger> getAllTransactionLadgerByPassingBillNumberAndTP(String billLaddingNo) throws Exception {
        List<TransactionLedger> outList = null;
        String queryString = " from TransactionLedger where billLaddingNo='" + billLaddingNo + "'"
                + " and transactionType='" + CommonConstants.TRANSACTION_TYPE_ACCRUALS + "'"
                + " and status='" + CommonConstants.STATUS_OPEN + "'";
        outList = getCurrentSession().createQuery(queryString).list();
        return (null != outList) ? outList : Collections.EMPTY_LIST;
    }

    public List getTransactionValues1(String billLaddingNo) throws Exception {
        List result = null;
        List result1 = new ArrayList();
        String queryString = "select distinct(custNo) from TransactionLedger where billLaddingNo="
                + "'" + billLaddingNo + "'";
        result = getCurrentSession().createQuery(queryString).list();
        for (int i = 0; i < result.size(); i++) {
            String a = (String) result.get(i);
            result1.add(a);
        }
        return result1;
    }
    // get TransactionLedger Records.....

    public List getTransactionRecords(String billLaddingNo, String invoiceNumber) throws Exception {
        List result1 = new ArrayList();
        String queryString = null;
        queryString = " from TransactionLedger where (billLaddingNo='" + billLaddingNo + "'or "
                + "invoiceNumber='" + invoiceNumber + "') and transactionType='" + CommonConstants.TRANSACTION_TYPE_ACCRUALS + "' "
                + "and status='" + CommonConstants.STATUS_ASSIGN + "'";
        result1 = getCurrentSession().createQuery(queryString).list();
        return result1;
    }

    public List getAPtypeOfRecords(String billLaddingNo, String invoiceNumber) throws Exception {
        List resultlist = new ArrayList();
        String queryString = null;
        queryString = " from TransactionLedger where (billLaddingNo='" + billLaddingNo + "'or "
                + "invoiceNumber='" + invoiceNumber + "') and transactionType='" + CommonConstants.TRANSACTION_TYPE_ACCOUNT_PAYABLE + "'"
                + "and status='" + CommonConstants.STATUS_OPEN + "'";
        resultlist = getCurrentSession().createQuery(queryString).list();
        return resultlist;
    }

    public List getAPTransactionRecords(String invoiceNumber, String vendorNumber) throws Exception {
        List resultlist = new ArrayList();
        String queryString = null;
        queryString = " from TransactionLedger where invoiceNumber='" + invoiceNumber + "' and transactionType='" + CommonConstants.TRANSACTION_TYPE_ACCOUNT_PAYABLE + "'"
                + "and status='" + CommonConstants.STATUS_OPEN + "' and custNo='" + vendorNumber + "' order by custNo";
        resultlist = getCurrentSession().createQuery(queryString).list();
        return resultlist;
    }

    public List getTransactionValues3(String billLaddingNo) throws Exception {
        List result = null;
        List result1 = new ArrayList();
        String queryString = "select distinct(custNo) from TransactionLedger where invoiceNumber='" + billLaddingNo + "'";
        result = getCurrentSession().createQuery(queryString).list();
        for (int i = 0; i < result.size(); i++) {
            String a = (String) result.get(i);
            result1.add(a);
        }
        return result1;
    }

    public List getTransactionValues2(String billLaddingNo, String custNo) throws Exception {
        List result = null;
        List result1 = new ArrayList();
        String queryString = "select distinct(transactionType) from TransactionLedger "
                + "where billLaddingNo='" + billLaddingNo + "' and custNo='" + custNo + "' "
                + "and transactionType='AR'";
        result = getCurrentSession().createQuery(queryString).list();
        for (int i = 0; i < result.size(); i++) {
            String a = (String) result.get(i);
            result1.add(a);
        }
        return result1;
    }

    public List getTransactionValues4(String billLaddingNo, String custNo) throws Exception {
        List result = null;
        List result1 = new ArrayList();
        String queryString = "select distinct(transactionType) from TransactionLedger where invoiceNumber='" + billLaddingNo + "' and custNo='" + custNo + "' and transactionType='AR'";
        result = getCurrentSession().createQuery(queryString).list();
        for (int i = 0; i < result.size(); i++) {
            String a = (String) result.get(i);

            result1.add(a);
        }
        return result1;
    }

    public List getTransactionRecords(String billladdingNo, String custNo,
            Double transactionAmount, String transType) throws Exception {
        List result = null;
        String querystring = "update Transaction set transactionAmt=" + transactionAmount + " where billLaddingNo='" + billladdingNo + "' and custNo='" + custNo + "' and transactionType='" + transType + "'";
        int id = getCurrentSession().createQuery(querystring).executeUpdate();
        return result;
    }

    public List getTransactionValues(String billLaddingNo, String custNo, String transType) throws Exception {
        List result = null;
        String queryString = "from TransactionLedger where transactionId=(Select max(transactionId) "
                + "from TransactionLedger where billLaddingNo='" + billLaddingNo + "' "
                + "and custNo='" + custNo + "' "
                + "and transactionType='" + transType + "')";
        result = getCurrentSession().createQuery(queryString).list();
        return result;
    }

    public List getTransactionValues1(String billLaddingNo, String custNo, String transType) throws Exception {
        List result = null;
        String queryString = "from TransactionLedger where transactionId=(Select max(transactionId) from TransactionLedger where invoiceNumber='" + billLaddingNo + "' and custNo='" + custNo + "' and transactionType='" + transType + "')";
        result = getCurrentSession().createQuery(queryString).list();
        return result;
    }

    public Double gettransactionamount(String blladingno, String custNo, String transType) {
        Double result = null;

        String queryString = "Select sum(transactionAmt) from TransactionLedger"
                + " where billLaddingNo ='" + blladingno + "' and custNo='" + custNo + "' "
                + "and transactionType='" + transType + "'";
        List queryObject = getCurrentSession().createQuery(queryString).list();
        Iterator itr = queryObject.iterator();

        while (itr.hasNext()) {

            Double row = (Double) itr.next();
            if (row != null) {
                result = (Double) row;
            }
        }

        return result;
    }

    public Double getCorrectedAmount(String blladingno, String custNo, String transType) {
        Double result = null;

        String queryString = "Select sum(transactionAmt) from TransactionLedger"
                + " where billLaddingNo ='" + blladingno + "' and custNo='" + custNo + "' "
                + "and transactionType='" + transType + "'";
        List queryObject = getCurrentSession().createQuery(queryString).list();
        Iterator itr = queryObject.iterator();

        while (itr.hasNext()) {

            Double row = (Double) itr.next();
            if (row != null) {
                result = (Double) row;
            }
        }

        return result;
    }

    public Double gettransactionamount1(String blladingno, String custNo, String transType) {
        Double result = null;

        String queryString = "Select sum(transactionAmt) from TransactionLedger where invoiceNumber ='" + blladingno + "' and custNo='" + custNo + "' and transactionType='" + transType + "'";
        List queryObject = getCurrentSession().createQuery(queryString).list();
        Iterator itr = queryObject.iterator();

        while (itr.hasNext()) {

            Double row = (Double) itr.next();
            if (row != null) {
                result = (Double) row;
            }
        }

        return result;
    }

    public List getTransactionsForVendor(String vendorno) {
        List<TransactionBean> transList = new ArrayList<TransactionBean>();
        //status A-assign,R-Reject
        String queryString = "select tl.custName,tl.custNo,tl.billLaddingNo,tl.subledgerSourceCode,tl.transactionAmt,tl.invoiceNumber,tl.transactionId from TransactionLedger tl where tl.transactionType='AC' and tl.custNo='" + vendorno + "' and tl.status !='XX' and tl.status !='AS' and tl.status !='R' and tl.status !='RP'";
        List queryObject = getCurrentSession().createQuery(queryString).setMaxResults(500).list();
        Iterator itr = queryObject.iterator();
        TransactionBean tbean = null;
        NumberFormat number = new DecimalFormat("###,###,##0.00");
        while (itr.hasNext()) {
            Object[] row = (Object[]) itr.next();
            tbean = new TransactionBean();
            String custName = (String) row[0];
            String custNo = (String) row[1];
            String billofladdingNo = (String) row[2];
            String costCode = (String) row[3];
            Double amt = (Double) row[4];
            String invoiceNo = (String) row[5];
            int tid = (Integer) row[6];
            String accAmt = number.format(amt);
            tbean.setCustomerNo(custNo);
            tbean.setCustomer(custName);
            tbean.setInvoiceOrBl(invoiceNo);
            tbean.setSourcecode(costCode);
            tbean.setAmount(accAmt);
            tbean.setBillofLadding(billofladdingNo);
            tbean.setTransactionId(Integer.toString(tid));
            transList.add(tbean);
            tbean = null;

        }
        return transList;
    }

    public List detialsListForChargeCode(String sdate, String edate, String subledgerList, String posted, String sortBy) throws Exception {
        List<TransactionBean> chargeCodeList = new ArrayList<TransactionBean>();
        TransactionBean tbean = null;
        DateUtils dateUtils = new DateUtils();
        Date startdate = null;
        Date enddate = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (sdate != null && !sdate.equals("")) {
            startdate = dateUtils.parseToDate(sdate);
            sdate = simpleDateFormat.format(startdate);
        }
        if (edate != null && !edate.equals("")) {
            enddate = dateUtils.parseToDate(edate);
            edate = simpleDateFormat.format(enddate);

        }

        String queryString = "";
        if (posted.equals("no")) {
            queryString = " from TransactionLedger tl where tl.date(transactionDate) Between '" + sdate + "' and '" + edate + "' and tl.status='" + AccountingConstants.STATUS_CHARGECODE + "'";
        } else {
            queryString = " from TransactionLedger tl where tl.date(transactionDate) Between '" + sdate + "' and '" + edate + "' and (tl.status='" + AccountingConstants.STATUS_CHARGECODEPOSTED + "')";
        }
        if (subledgerList != null && !subledgerList.equals("")) {
            queryString = queryString + " and tl.subledgerSourceCode='" + subledgerList + "'";
            if (AccountingConstants.SUBLEDGER_CODE_ACC.equals(subledgerList)) {
                queryString = queryString + " and tl.transactionType='" + AccountingConstants.TRANSACTION_TYPE_AC + "' ";
            }
        }
        if (null != sortBy && !sortBy.trim().equals("")) {
            if (sortBy.trim().equals(CommonConstants.SORT_BY_GL_ACCOUNT)) {
                queryString += "  order by tl.glAccountNumber";
            } else if (sortBy.trim().equals(CommonConstants.SORT_BY_VENDOR)) {
                queryString += "  order by tl.custNo";
            } else if (sortBy.trim().equals(CommonConstants.SORT_BY_CHARGECODE)) {
                queryString += "  order by tl.chargeCode";
            } else if (sortBy.trim().equals(CommonConstants.SORT_BY_SUB_LEDGER)) {
                queryString += "  order by tl.subledgerSourceCode";
            }
        }

        List<TransactionLedger> transLedgerList = getCurrentSession().createQuery(queryString).list();
        if (transLedgerList != null && !transLedgerList.isEmpty()) {
            for (TransactionLedger transactionLedger : transLedgerList) {
                tbean = new TransactionBean(transactionLedger);
                chargeCodeList.add(tbean);
            }
        }
        return chargeCodeList;
    }

    public void updateTransLedgerStatus(String tid, String status) throws Exception {

        int updatedrecords = 0;
        String queryString = "update TransactionLedger tl set tl.status='" + status + "' where tl.transactionId='" + tid + "'";
        updatedrecords = getCurrentSession().createQuery(queryString).executeUpdate();
        getCurrentSession().flush();
        getCurrentSession().clear();
    }

    public void updateTransLedgerStatus(String tid, String invoiceNumber, String status) throws Exception {
        int updatedrecords = 0;
        String queryString = "update TransactionLedger tl set tl.status='" + status + "',tl.invoiceNumber = '" + invoiceNumber + "' where tl.transactionId='" + tid + "'";
        updatedrecords = getCurrentSession().createQuery(queryString).executeUpdate();
        getCurrentSession().flush();
        getCurrentSession().clear();
    }

    public List getPaidTransactionsForVendor(String vendorno, String status) throws Exception {
        List<TransactionBean> transList = new ArrayList<TransactionBean>();
        String queryString = "select tl.custName,tl.custNo,tl.billLaddingNo,tl.subledgerSourceCode,tl.transactionAmt,tl.invoiceNumber,tl.transactionId from TransactionLedger tl where tl.transactionType='AC' and tl.custNo='" + vendorno + "' and tl.status='" + status + "'";
        List queryObject = getCurrentSession().createQuery(queryString).list();
        Iterator itr = queryObject.iterator();
        TransactionBean tbean = null;
        NumberFormat number = new DecimalFormat("0.00");
        while (itr.hasNext()) {
            Object[] row = (Object[]) itr.next();
            tbean = new TransactionBean();
            String custName = (String) row[0];
            String custNo = (String) row[1];
            String billofladdingNo = (String) row[2];
            String costCode = (String) row[3];
            Double amt = (Double) row[4];
            String invoice = (String) row[5];
            int tid = (Integer) row[6];
            String accAmt = number.format(amt);
            tbean.setCustomerNo(custNo);
            tbean.setCustomer(custName);
            tbean.setInvoiceOrBl(invoice);
            tbean.setSourcecode(costCode);
            tbean.setAmount(accAmt);
            tbean.setBillofLadding(billofladdingNo);
            //tbean.setChequenumber(chequenumber);
            tbean.setTransactionId(Integer.toString(tid));
            transList.add(tbean);
            tbean = null;

        }
        return transList;
    }

    public void saveToTransaction(String custNo) throws Exception {
        String queryString1 = "select distinct tl.invoiceNumber from TransactionLedger tl where tl.custNo='" + custNo + "' and tl.status='XX'";
        List QueryObject1 = getCurrentSession().createQuery(queryString1).list();

        int j = 0;
        int i = QueryObject1.size();
        List transAmt1 = new ArrayList();
        while (i > 0) {
            String invc = (String) QueryObject1.get(j);
            String queryforGLAmt = "select sum(tl.transactionAmt) from TransactionLedger tl where tl.invoiceNumber='" + invc + "' and tl.custNo='" + custNo + "' and tl.status='XX'";
            Iterator QueryObj = getCurrentSession().createQuery(queryforGLAmt).list().iterator();
            Double tamt = (Double) QueryObj.next();
            transAmt1.add(tamt);
            j++;
            i--;
        }
        //Insertion of records in to Transaction_Ledger table as Transaction type='AP'

        String queryStringforAPEntry = "select tl.custName,tl.custNo,tl.billLaddingNo,tl.subledgerSourceCode,"
                + "tl.transactionAmt,tl.invoiceNumber,tl.glAccountNumber,tl.chargeCode,tl.chequeNumber,"
                + "tl.journalEntryNumber,tl.lineItemNumber,tl.balance,tl.blTerms,tl.subHouseBl,tl.voyageNo,tl.containerNo,tl.masterBl,tl.vesselNo,tl.dueDate,"
                + "tl.currencyCode from TransactionLedger tl where tl.custNo='" + custNo + "' and tl.status='XX'";
        List queryObj = getCurrentSession().createQuery(queryStringforAPEntry).list();
        Iterator itr1 = queryObj.iterator();
        TransactionLedger tl = null;
        while (itr1.hasNext()) {
            tl = new TransactionLedger();
            Object[] r = (Object[]) itr1.next();
            String custName = (String) r[0];
            String custNumber = (String) r[1];
            String blno = (String) r[2];
            String subledsourccode = (String) r[3];
            Double transctionAmt = (Double) r[4];
            String invnumber = (String) r[5];
            String glno = (String) r[6];
            String chargecode = (String) r[7];
            String checknumber = (String) r[8];
            String jenumber = (String) r[9];
            String linumber = (String) r[10];
            Double bal = (Double) r[11];
            String blterms = (String) r[12];
            String subhousebl = (String) r[13];
            String voyageno = (String) r[14];
            String containerno = (String) r[15];
            String masterbl = (String) r[16];
            String vesselno = (String) r[17];
            Date duedate = (Date) r[18];
            String currCode = (String) r[19];
            tl.setBalance(bal);
            tl.setBillLaddingNo(blno);
            tl.setBlTerms(blterms);
            tl.setChargeCode(chargecode);
            tl.setChequeNumber(checknumber);
            tl.setContainerNo(containerno);
            tl.setCurrencyCode(currCode);
            tl.setCustName(custName);
            tl.setCustNo(custNumber);
            tl.setDueDate(duedate);
            tl.setGlAccountNumber(glno);
            tl.setInvoiceNumber(invnumber);
            tl.setJournalEntryNumber(jenumber);
            tl.setLineItemNumber(linumber);
            tl.setMasterBl(masterbl);
            tl.setStatus("Open");
            tl.setSubHouseBl(subhousebl);
            tl.setSubledgerSourceCode(subledsourccode);
            tl.setTransactionAmt(transctionAmt);
            Date d = new Date();
            tl.setTransactionDate(d);
            tl.setTransactionType("AP");
            tl.setVesselNo(vesselno);
            tl.setVoyageNo(voyageno);
            save(tl);
            tl = null;
        }


        int x = 0;
        int y = QueryObject1.size();

        // Saving to Transaction Table summarising based on Invoice Number
        while (y > 0) {
            String inv = (String) QueryObject1.get(x);
            String queryString = "select tl.custName,tl.custNo,tl.billLaddingNo,tl.subledgerSourceCode,tl.transactionAmt,"
                    + "tl.invoiceNumber,tl.glAccountNumber,tl.chargeCode from TransactionLedger tl where tl.custNo='" + custNo + "' and tl.status='XX' and tl.invoiceNumber='" + inv + "'";

            //,tl.transactionId

            List queryObject = getCurrentSession().createQuery(queryString).list();
            Iterator itr = queryObject.iterator();
            Transaction trans = null;

            if (itr.hasNext()) {
                trans = new Transaction();
                Object[] row = (Object[]) itr.next();
                String custname = (String) row[0];
                String custnumber = (String) row[1];
                String blNo = (String) row[2];
                String sscode = (String) row[3];
                Double transAmt = (Double) row[4];
                String invNumber = (String) row[5];
                String glacctNo = (String) row[6];
                String chargecode = (String) row[7];

                //String transLedgerId=(String)row[8];
                //updateTransLedgerStatus(transLedgerId,"RP");

                trans.setBillLaddingNo(blNo);
                trans.setCustName(custname);
                trans.setCustNo(custnumber);
                trans.setSubledgerSourceCode(sscode);
                Date d = new Date();
                trans.setTransactionDate(d);
                trans.setTransactionType("AP");
                transAmt = (Double) transAmt1.get(x);
                trans.setTransactionAmt(transAmt);
                trans.setChargeCode(chargecode);
                //status Open
                trans.setStatus("Open");
                trans.setInvoiceNumber(invNumber);
                trans.setGlAccountNumber(glacctNo);
                TransactionDAO transDAO = new TransactionDAO();
                transDAO.save(trans);
                trans = null;


            }
            y--;
            x++;
        }
        String querytoupdatetrans = "select tl.transactionId from TransactionLedger tl where tl.custNo='" + custNo + "' and tl.status='XX'";
        List qo = getCurrentSession().createQuery(querytoupdatetrans).list();
        int z = qo.size();
        int t = 0;
        while (z > 0) {
            int tid = (Integer) qo.get(t);
            String transid = Integer.toString(tid);
            updateTransLedgerStatus(transid, "AS");
            z--;
            t++;
        }
    }
    // This method is to update status in Transactio_ledger when ever the related trasnaction in Transaction table
    //is updated with status ready to pay(RP).

    public void updateTLbasedonInvNo(String custno, String inv, String status) throws Exception {
        int updatedrecords = 0;
        String queryString = "update TransactionLedger tl set tl.status='" + status + "',tl.readyToPost='Y' where tl.custNo='" + custno + "' and tl.invoiceNumber='" + inv + "'";
        updatedrecords = getCurrentSession().createQuery(queryString).executeUpdate();
    }

    public void updateTLJELINE(String transID, String jeId, String lineId) throws Exception {
        int updatedrecords = 0;
        String queryString = "update TransactionLedger tl set tl.journalEntryNumber='" + jeId + "',tl.lineItemNumber='" + lineId + "' where tl.transactionId='" + transID + "'";
        updatedrecords = getCurrentSession().createQuery(queryString).executeUpdate();
    }

    public List getTransactionLedgerDetailsForPopUp(String vendorNumber, String invoiceNumber, String BlNumber, String transactionType) throws Exception {
        StringBuilder queryString = new StringBuilder("select tl.Bill_Ladding_No, tl.drcpt, tl.Voyage_No,tl.Container_No,tl.Transaction_date, tl.Transaction_amt,"
                + " tl.Charge_Code, gc.codedesc from transaction_ledger tl left join genericcode_dup gc on gc.code = tl.Charge_Code"
                + " and gc.Codetypeid = " + CommonConstants.COST_CODE_TYPE + " where tl.Transaction_type = '" + transactionType + "'");
        if (transactionType.trim().equals(CommonConstants.TRANSACTION_TYPE_ACCOUNT_PAYABLE)) {
            queryString.append(" and tl.status='" + CommonConstants.STATUS_OPEN + "'");
        }
        if (invoiceNumber != null && !invoiceNumber.trim().equals("")) {
            queryString.append(" and tl.invoice_number ='").append(invoiceNumber).append("'");
        }
        if (BlNumber != null && !BlNumber.trim().equals("")) {
            queryString.append(" and tl.Bill_Ladding_No ='").append(BlNumber).append("'");
        }
        if (vendorNumber != null && !vendorNumber.trim().equals("")) {
            queryString.append(" and tl.cust_no ='").append(vendorNumber).append("'");
        }
        Query queryObject = getSession().createSQLQuery(queryString.toString());
        return queryObject.list();
    }

    public Double getSumForSubledger(String subledgerCode, String sdate, String edate) throws Exception {
        Date startdate = null;
        Date enddate = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (sdate != null && !sdate.equals("")) {
            startdate = DateUtils.parseToDate(sdate);
            sdate = simpleDateFormat.format(startdate);
        }
        if (edate != null && !edate.equals("")) {
            enddate = DateUtils.parseToDate(edate);
            edate = simpleDateFormat.format(enddate);

        }
        String queryString = "select sum(t.transactionAmt) from TransactionLedger t where t.date(transactionDate) Between '" + sdate + "' and '" + edate + "' and (t.status='Open' or t.status='" + AccountingConstants.STATUS_CHARGECODE + "'";
        if (CommonConstants.SUB_LEDGER_CODE_CASH_DEPOSIT.equals(subledgerCode)) {
            queryString = queryString + " or t.status='" + CommonConstants.STATUS_PAID + "' ) and t.subledgerSourceCode='" + subledgerCode + "'";
        } else {
            queryString = queryString + ") and t.subledgerSourceCode='" + subledgerCode + "'";
        }
        queryString = queryString + " and (t.glAccountNumber is not null and trim(t.glAccountNumber)!='') limit 1";
        Object result = getCurrentSession().createQuery(queryString).uniqueResult();
        return null != result ? (Double) result : 0.0;
    }

    public List<TransactionBean> searchAccuralsById(String ids) throws Exception {
        List<TransactionBean> accruals = new ArrayList<TransactionBean>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select tl.Cust_name,tl.cust_no,tl.Bill_Ladding_No,tl.Charge_Code,tl.Transaction_amt,tl.Invoice_number,");
        queryBuilder.append("tl.Transaction_Id, tl.Container_No, tl.Voyage_No, ve.company, tl.status, tl.sailing_date, tl.Cheque_number,");
        queryBuilder.append("tl.drcpt,tl.correction_notice from transaction_ledger tl left join vendor_info ve on ve.cust_accno=tl.cust_no");
        queryBuilder.append(" where tl.cust_no is not null  and tl.transaction_type='").append(CommonConstants.TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append(" and tl.Transaction_Id in(").append(StringUtils.removeEnd(ids, ",")).append(")");
        queryBuilder.append(" group by tl.Transaction_Id");
        getSession().clear();
        getSession().flush();
        List result = getSession().createSQLQuery(queryBuilder.toString()).setMaxResults(2000).list();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            TransactionBean tbean = new TransactionBean();
            tbean.setCustomer(null != col[0] ? col[0].toString() : "");
            tbean.setCustomerNo(null != col[1] ? col[1].toString() : "");
            tbean.setBillofLadding(null != col[2] ? col[2].toString() : "");
            tbean.setChargeCode(null != col[3] ? col[3].toString() : "");
            tbean.setAmount(null != col[4] ? NumberUtils.formatNumber(Double.parseDouble(col[4].toString().trim()), "###,###,##0.00") : "0");
            tbean.setInvoiceOrBl(null != col[5] ? col[5].toString() : "");
            tbean.setTransactionId(null != col[6] ? col[6].toString() : "");
            tbean.setContainerNo(null != col[7] ? col[7].toString() : "");
            tbean.setVoyage(null != col[8] ? col[8].toString() : "");
            tbean.setContact(null != col[9] ? col[9].toString() : "");
            tbean.setStatus(null != col[10] ? col[10].toString() : "");
            tbean.setSailingDate((Date) col[11]);
            if (col[12] != null && !col[12].toString().trim().equals("")
                    && Integer.parseInt(col[12].toString()) > 0) {
                tbean.setChequenumber(col[12].toString());
            }
            tbean.setDocReceipt(null != col[13] ? col[13].toString() : "");
            tbean.setCorrectionNotice(null != col[14] && CommonUtils.isNotEqual(col[14].toString(), FclBlConstants.CNA0) ? col[14].toString() : "");
            accruals.add(tbean);
        }
        return accruals;
    }

    public List searchAccuralsWithInvoiceNumber(AccrualsForm accrualsForm) throws Exception {
        List<TransactionBean> accruals = new ArrayList<TransactionBean>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select tl.Cust_name,tl.cust_no,tl.Bill_Ladding_No,tl.Charge_Code,tl.Transaction_amt,tl.Invoice_number,");
        queryBuilder.append("tl.Transaction_Id, tl.Container_No, tl.Voyage_No, ve.company, tl.status, tl.sailing_date, tl.Cheque_number,");
        queryBuilder.append("tl.drcpt,tl.correction_notice from transaction_ledger tl left join vendor_info ve on ve.cust_accno=tl.cust_no");
        queryBuilder.append(" where tl.cust_no is not null and tl.accruals_correction_flag is null and tl.transaction_type='").append(CommonConstants.TRANSACTION_TYPE_ACCRUALS).append("'");
        if (null != accrualsForm.getVendor() && !accrualsForm.getVendor().trim().equals("")) {
            queryBuilder.append(" and tl.Cust_name = \"").append(accrualsForm.getVendor().trim()).append("\"");
        }
        if (null != accrualsForm.getVendornumber() && !accrualsForm.getVendornumber().trim().equals("")) {
            queryBuilder.append(" and tl.cust_no = '").append(accrualsForm.getVendornumber().trim()).append("'");
        }
        if (null != accrualsForm.getInvoicenumber() && !accrualsForm.getInvoicenumber().trim().equals("")) {
            queryBuilder.append(" and tl.invoice_number='").append(accrualsForm.getInvoicenumber()).append("'");
        }
        if (null != accrualsForm.getStatus() && !accrualsForm.getStatus().trim().equals("")) {
            queryBuilder.append(" and tl.status='").append(accrualsForm.getStatus()).append("'");
        } else {
            queryBuilder.append(" and (tl.status!='").append(CommonConstants.STATUS_ASSIGN).append("' and tl.status!='").append(CommonConstants.STATUS_VOID).append("' and tl.status!='").append(CommonConstants.STATUS_PAID).append("')");
        }
        if (accrualsForm.isHideAccruals()) {
            queryBuilder.append(" and datediff(sysdate(),tl.sailing_date)<").append(accrualsForm.getAccrualsLimit().trim());
        }
        String accrualIds = accrualsForm.getAccrualIds();
        if (null != accrualIds && !accrualIds.trim().equals("")) {
            accrualIds = StringUtils.removeEnd(StringUtils.removeStart(accrualIds, ","), ",");
            queryBuilder.append(" and tl.Transaction_Id not in(").append(accrualIds).append(")");
        }
        queryBuilder.append(" group by tl.Transaction_Id order by tl.cust_no");
        List result = getSession().createSQLQuery(queryBuilder.toString()).setMaxResults(2000).list();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            TransactionBean tbean = new TransactionBean();
            tbean.setCustomer(null != col[0] ? col[0].toString() : "");
            tbean.setCustomerNo(null != col[1] ? col[1].toString() : "");
            tbean.setBillofLadding(null != col[2] ? col[2].toString() : "");
            tbean.setChargeCode(null != col[3] ? col[3].toString() : "");
            tbean.setAmount(null != col[4] ? NumberUtils.formatNumber(Double.parseDouble(col[4].toString().trim()), "###,###,##0.00") : "0");
            tbean.setInvoiceOrBl(null != col[5] ? col[5].toString() : "");
            tbean.setTransactionId(null != col[6] ? col[6].toString() : "");
            tbean.setContainerNo(null != col[7] ? col[7].toString() : "");
            tbean.setVoyage(null != col[8] ? col[8].toString() : "");
            tbean.setContact(null != col[9] ? col[9].toString() : "");
            tbean.setStatus(null != col[10] ? col[10].toString() : "");
            tbean.setSailingDate((Date) col[11]);
            if (col[12] != null && !col[12].toString().trim().equals("")
                    && Integer.parseInt(col[12].toString()) > 0) {
                tbean.setChequenumber(col[12].toString());
            }
            tbean.setDocReceipt(null != col[13] ? col[13].toString() : "");
            tbean.setCorrectionNotice(null != col[14] && CommonUtils.isNotEqual(col[14].toString(), FclBlConstants.CNA0) ? col[14].toString() : "");
            accruals.add(tbean);
        }
        return accruals;
    }

    public List searchAccuralsByInvoiceNumber(String vendorName, String vendorNumber, String invoiceNumber, String status) throws Exception {
        List<TransactionBean> accruals = new ArrayList<TransactionBean>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select tl.Cust_name,tl.cust_no,tl.Bill_Ladding_No,tl.Charge_Code,tl.Transaction_amt,tl.Invoice_number,");
        queryBuilder.append("tl.Transaction_Id, tl.Container_No, tl.Voyage_No, ve.company, tl.status, tl.sailing_date, tl.Cheque_number,");
        queryBuilder.append("tl.drcpt,tl.correction_notice,tl.GL_account_number,tl.description");
        queryBuilder.append(" from transaction_ledger tl left join vendor_info ve on ve.cust_accno=tl.cust_no");
        queryBuilder.append(" where tl.cust_no is not null  and tl.accruals_correction_flag is null");
        queryBuilder.append(" and tl.transaction_type='").append(CommonConstants.TRANSACTION_TYPE_ACCRUALS).append("'");
        if (null != vendorName && !vendorName.trim().equals("")) {
            queryBuilder.append(" and tl.Cust_name = \"").append(vendorName.trim()).append("\"");
        }
        if (null != vendorNumber && !vendorNumber.trim().equals("")) {
            queryBuilder.append(" and tl.cust_no = '").append(vendorNumber.trim()).append("'");
        }
        if (null != invoiceNumber && !invoiceNumber.trim().equals("")) {
            queryBuilder.append(" and tl.invoice_number='").append(invoiceNumber).append("'");
        }
        queryBuilder.append(" and tl.direct_gl_account=1");
        queryBuilder.append(" group by tl.Transaction_Id order by tl.cust_no");
        getSession().flush();
        List result = getSession().createSQLQuery(queryBuilder.toString()).setMaxResults(2000).list();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            TransactionBean tbean = new TransactionBean();
            tbean.setCustomer(null != col[0] ? col[0].toString() : "");
            tbean.setCustomerNo(null != col[1] ? col[1].toString() : "");
            tbean.setBillofLadding(null != col[2] ? col[2].toString() : "");
            tbean.setChargeCode(null != col[3] ? col[3].toString() : "");
            tbean.setAmount(null != col[4] ? NumberUtils.formatNumber(Double.parseDouble(col[4].toString().trim()), "###,###,##0.00") : "0");
            tbean.setInvoiceOrBl(null != col[5] ? col[5].toString() : "");
            tbean.setTransactionId(null != col[6] ? col[6].toString() : "");
            tbean.setContainerNo(null != col[7] ? col[7].toString() : "");
            tbean.setVoyage(null != col[8] ? col[8].toString() : "");
            tbean.setContact(null != col[9] ? col[9].toString() : "");
            tbean.setStatus(null != col[10] ? col[10].toString() : "");
            tbean.setSailingDate((Date) col[11]);
            if (col[12] != null && !col[12].toString().trim().equals("")
                    && Integer.parseInt(col[12].toString()) > 0) {
                tbean.setChequenumber(col[12].toString());
            }
            tbean.setDocReceipt(null != col[13] ? col[13].toString() : "");
            tbean.setCorrectionNotice(null != col[14] && CommonUtils.isNotEqual(col[14].toString(), FclBlConstants.CNA0) ? col[14].toString() : "");
            tbean.setGlAcctNo((String) col[15]);
            tbean.setDescription((String) col[16]);
            accruals.add(tbean);
        }
        return accruals;
    }

    public void releaseAccuralsFromInvoiceNumber(String vendorNumber, String invoiceNumber) throws Exception {
        StringBuilder query = new StringBuilder();
        query.append("update TransactionLedger set invoiceNumber=null");
        query.append(" where transactionType='").append(CommonConstants.TRANSACTION_TYPE_ACCRUALS).append("'");
        query.append(" and custNo = '").append(vendorNumber.trim()).append("'");
        query.append(" and invoiceNumber = '").append(invoiceNumber.trim()).append("'");
        query.append(" and status != '").append(CommonConstants.STATUS_ASSIGN).append("'");
        getCurrentSession().createQuery(query.toString()).executeUpdate();
        getCurrentSession().flush();
    }

    public String getGLAccount(String chargeCode) throws Exception {
        String queryString = "SELECT  gl_Acct FROM gl_mapping WHERE charge_code  = '" + chargeCode + "' and rev_exp = 'E' limit 1";
        Query queryObject = getSession().createSQLQuery(queryString).addScalar("gl_Acct", StringType.INSTANCE);
        Object glAccount = queryObject.uniqueResult();
        return null != glAccount ? glAccount.toString() : "";
    }

    public String getGLAccount(String chargeCode, FclBl fclBl) throws Exception {
        GlMappingDAO glMappingDAO = new GlMappingDAO();
        if ((null != chargeCode || !chargeCode.trim().equals("")) && null != fclBl) {
            String shipmentType = null != fclBl.getShipmentType() ? fclBl.getShipmentType() : "";
            String originalTerminal = null != fclBl.getOriginalTerminal() ? fclBl.getOriginalTerminal() : "";
            String billingTerminal = null != fclBl.getBillingTerminal() ? fclBl.getBillingTerminal() : "";
            return glMappingDAO.getGLAccountNo(shipmentType, chargeCode, originalTerminal, billingTerminal, "E");
        } else {
            return null;
        }
    }

    public String getGLAccount(String chargeCode, String shipmentType, String originalTerminal, String billingTerminal) throws Exception {
        GlMappingDAO glMappingDAO = new GlMappingDAO();
        if ((null != chargeCode && !chargeCode.trim().equals("")) && null != shipmentType && !shipmentType.trim().equals("")) {
            return glMappingDAO.getGLAccountNo(shipmentType, chargeCode, originalTerminal, billingTerminal, "E");
        } else {
            return null;
        }
    }

    public List<TransactionLedger> getTransactionLedgerByInvoiceNumber(String invoiceNumber) throws Exception {
        List<TransactionLedger> transactionList = new ArrayList();
        String queryString = "from TransactionLedger where invoiceNumber='" + invoiceNumber + "'";
        transactionList = getCurrentSession().createQuery(queryString).list();
        return transactionList;
    }

    public void updateOtherVendorsAccruals(String vendorName, String vendorNumbe, String tid, String status) throws Exception {
        int updatedrecords = 0;
        String queryString = "update TransactionLedger tl custName='" + vendorName + "',custNo='" + vendorNumbe + "',set tl.status='" + status + "' where tl.transactionId='" + tid + "'";
        updatedrecords = getCurrentSession().createQuery(queryString).executeUpdate();
        getCurrentSession().flush();
    }

    public void insertTransactionLedgerRecordsForMakePayment(Transaction transaction) throws Exception {
        TransactionLedgerDAO transactionLedgerDAO = new TransactionLedgerDAO();
        String queryString = "from TransactionLedger where custNo='" + transaction.getCustNo() + "' and invoiceNumber='" + transaction.getInvoiceNumber() + "' and transactionType='AP'";
        Query query = getCurrentSession().createQuery(queryString);
        List<TransactionLedger> list = query.list();
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            TransactionLedger transactionLedger = (TransactionLedger) iterator.next();
            TransactionLedger transactionLedger2 = new TransactionLedger(transactionLedger);
            transactionLedger2.setTransactionType("PY");
            transactionLedgerDAO.save(transactionLedger2);
        }
    }

    public List<TransactionBean> getDrillDownForJE(String glAccountNumber, String lineItemNumber) throws Exception {
        List<TransactionBean> transactionBeanList = new ArrayList<TransactionBean>();
        String queryString = "select charge_code,amount from transaction_ledger_history "
                + "where  gl_account='" + glAccountNumber + "' and line_item_id='" + lineItemNumber + "'";
        List resultList = getCurrentSession().createSQLQuery(queryString).list();
        if (null != resultList && !resultList.isEmpty()) {
            for (Object result : resultList) {
                Object[] row = (Object[]) result;
                TransactionBean transactionBean = new TransactionBean();
                transactionBean.setChargeCode(null != row[0] ? row[0].toString() : "");
                transactionBean.setAmount(null != row[1] ? number.format(Double.parseDouble(row[1].toString())) : "");
                transactionBeanList.add(transactionBean);
            }
        }
        return transactionBeanList;
    }

    public TransactionLedger findByCostId(Integer costId, String BlNumber) {
        String queryString = "from TransactionLedger where costId ='" + costId + "' "
                + "and billLaddingNo='" + BlNumber + "' and transactionType='" + CommonConstants.TRANSACTION_TYPE_ACCRUALS + "'";
        return (TransactionLedger) getCurrentSession().createQuery(queryString).setMaxResults(1).uniqueResult();
    }

    public List<TransactionBean> getChargeCodeForInvoice(String invoiceNumber, String custNo) throws Exception {
        List<TransactionBean> chargeCodeList = new ArrayList<TransactionBean>();
        StringBuilder queryBuilder = new StringBuilder(" select invoiceNumber,transactionDate,chargeCode,sum(transactionAmt)");
        queryBuilder.append(" from TransactionLedger where transactionType='").append(CommonConstants.TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("'");
        queryBuilder.append(" and subledgerSourceCode='").append(CommonConstants.SUB_LEDGER_CODE_PURCHASE_JOURNAL).append("'");
        queryBuilder.append(" and invoiceNumber in (").append(invoiceNumber).append(")");
        queryBuilder.append(" and custNo='").append(custNo).append("'");
        queryBuilder.append(" group by invoiceNumber,chargeCode order by invoiceNumber,chargeCode");
        List<Object> list = getCurrentSession().createQuery(queryBuilder.toString()).list();
        if (null != list && !list.isEmpty()) {
            for (Object row : list) {
                TransactionBean transactionBean = new TransactionBean();
                Object[] col = (Object[]) row;
                transactionBean.setInvoiceOrBl((String) col[0]);
                transactionBean.setInvoiceDate(DateUtils.formatDate((Date) col[1], "MM/dd/yyyy"));
                transactionBean.setChargeCode((String) col[2]);
                transactionBean.setAmount(number.format((Double) col[3]));
                chargeCodeList.add(transactionBean);
            }
        }
        return chargeCodeList;
    }

    public List<TransactionLedger> currentCustomerNameAndNumber(String billLaddingNo) throws Exception {
        List<TransactionLedger> outList = null;
        String queryString = "FROM TransactionLedger where billLaddingNo=" + "'" + billLaddingNo + "'"
                + " and transactionType='" + CommonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE + "'"
                + "and manifestFlag='" + CommonConstants.YES + "' GROUP BY custName,custNo";
        outList = getCurrentSession().createQuery(queryString).list();
        return outList;
    }

    public List<TransactionLedger> getTransactionPassingCustNameAndNumber(String billLaddingNo, String customerName,
            String customerNo) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(TransactionLedger.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (billLaddingNo != null) {
            criteria.add(Restrictions.eq("billLaddingNo", billLaddingNo));
        }
        if (customerName != null) {
            criteria.add(Restrictions.eq("custName", customerName));
        }
        if (customerNo != null) {
            criteria.add(Restrictions.eq("custNo", customerNo));
        }
        return criteria.list();
    }

    public List<TransactionLedger> getListOfTaransactionToDelete(String billLaddingNo, String noticeNumber) throws Exception {
        List<TransactionLedger> outList = null;
        String queryString = null;
        if (noticeNumber != null && noticeNumber.indexOf("-") < 0) {
            queryString = "FROM TransactionLedger where billLaddingNo=" + "'" + billLaddingNo + "'"
                    + "and correctionNotice='" + noticeNumber + "'";
        } else {
            queryString = "FROM TransactionLedger where billLaddingNo=" + "'" + billLaddingNo + "'"
                    + "and correctionNotice like '" + noticeNumber + "%'";
        }
        outList = getCurrentSession().createQuery(queryString).list();
        return outList;
    }

    public List<TransactionLedger> getListOfAccrualsToDelete(String billLaddingNo, String noticeNumber) throws Exception {
        List<TransactionLedger> outList = null;
        String queryString = "FROM TransactionLedger where billLaddingNo=" + "'" + billLaddingNo + "'"
                + "and transactionType='" + CommonConstants.TRANSACTION_TYPE_ACCRUALS + "' and correctionNotice like '" + noticeNumber + "%'";
        outList = getCurrentSession().createQuery(queryString).list();
        return outList;
    }

    public List<TransactionLedger> getListOfAccrualsToDisplay(String billLaddingNo, String accrualsCorrectionNumber) throws Exception {
        List<TransactionLedger> outList = null;
        String queryString = "FROM TransactionLedger where billLaddingNo=" + "'" + billLaddingNo + "'"
                + "and transactionType='" + CommonConstants.TRANSACTION_TYPE_ACCRUALS + "' "
                + "and (chargeCode = 'ADVSHP' or chargeCode = 'ADVFF' or chargeCode = 'FFCOMM') and accrualsCorrectionNumber = '" + accrualsCorrectionNumber + "'";
        outList = getCurrentSession().createQuery(queryString).list();
        return outList;
    }

    public Object getSumOfAccruals(String billLaddingNo, String charge) throws Exception {
        Object outList = null;
        String queryString = "select sum(transactionAmt) FROM TransactionLedger where billLaddingNo=" + "'" + billLaddingNo + "'"
                + "and transactionType='" + CommonConstants.TRANSACTION_TYPE_ACCRUALS + "' and chargeCode='" + charge + "'";
        outList = getCurrentSession().createQuery(queryString).uniqueResult();
        return outList;
    }

    public Object getSumOfAccrualsForFF(String billLaddingNo, String charge) throws Exception {
        Object outList = null;
        String queryString = "select sum(transactionAmt) FROM TransactionLedger where billLaddingNo=" + "'" + billLaddingNo + "'"
                + "and transactionType='" + CommonConstants.TRANSACTION_TYPE_ACCRUALS + "' and (chargeCode='" + charge + "' or chargeCode='FFCOMM')";
        outList = getCurrentSession().createQuery(queryString).uniqueResult();
        return outList;
    }

    public String getMaxofNoticenumber(String billLaddingNo) throws Exception {
        String correctionNotice = null;
        String queryString = "SELECT MAX(correctionNotice) FROM TransactionLedger where billLaddingNo=" + "'" + billLaddingNo + "'"
                + "and transactionType='" + CommonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE + "' and manifestFlag='" + CommonConstants.YES + "'";
        correctionNotice = (String) getCurrentSession().createQuery(queryString).uniqueResult();
        return correctionNotice;
    }

    public String getBlNumberByNoticeNumber(String billLaddingNo, String noticeNumber, String customerNumber) throws Exception {
        String queryString = "SELECT Bill_Ladding_No FROM transaction_ledger where Bill_Ladding_No like '" + billLaddingNo + "%' "
                + "and Transaction_type='" + CommonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE + "' and correction_notice= '" + noticeNumber + "' "
                + "and cust_no='" + customerNumber + "' and manifest_flag='" + CommonConstants.YES + "' limit 1";
        return (String) getCurrentSession().createSQLQuery(queryString).uniqueResult();
    }

    public List<TransactionLedger> getTaransactionToPrint(String billLaddingNo, String noticeNumber) throws Exception {
        List<TransactionLedger> outList = null;
        String queryString = "FROM TransactionLedger where billLaddingNo=" + "'" + billLaddingNo + "'"
                + "and transactionType='" + CommonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE + "' and correctionNotice like '%" + noticeNumber + "%'";
        outList = getCurrentSession().createQuery(queryString).list();
        return outList;
    }

    public TransactionLedger getArTaransactionByChargeId(Integer chargeId) throws Exception {
        String queryString = "FROM TransactionLedger where transactionType='" + CommonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE + "' "
                + "and chargeId = " + chargeId + " and manifestFlag = 'Y' and status = 'open' limit 1";
        return (TransactionLedger) getCurrentSession().createQuery(queryString).uniqueResult();
    }

    public List<String> getListOfDistinctTaransactionCorrectionsNotice(String billLaddingNo) throws Exception {
        List<String> outList = null;
        String queryString = "select distinct(correctionNotice) FROM TransactionLedger where"
                + " billLaddingNo=" + "'" + billLaddingNo + "' and correctionNotice is not null order by correctionNotice desc";
        outList = getCurrentSession().createQuery(queryString).list();
        return outList;
    }

    public List<TransactionLedger> getListOfTaransactionToDisplayOnCDReport(String billLaddingNo, String noticeNumber,
            String customerNumber) throws Exception {// cd credti/debot note
        String queryString = "FROM TransactionLedger where billLaddingNo like " + "'" + billLaddingNo + "%'"
                + "and transactionType='" + CommonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE + "' and noticeNumber like '%" + noticeNumber + "%' "
                + "and custNo='" + customerNumber + "'";
        List<TransactionLedger> outList = getCurrentSession().createQuery(queryString).list();
        return outList;
    }

    public Integer transactionLedgerUpdate(Integer costId, String vendorName, String VendorNumber) throws Exception {
        int updatedrecords = 0;
        String queryString = "UPDATE transaction_ledger SET Cust_name='" + vendorName + "',cust_no='" + VendorNumber + "' WHERE STATUS='Open' and transaction_type='AC' AND cost_id='" + costId + "'";
        updatedrecords = getCurrentSession().createSQLQuery(queryString).executeUpdate();
        getCurrentSession().flush();
        return updatedrecords;
    }

    public void updateAccrual(Integer costId, String vendorName, String vendorNumber) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update transaction_ledger");
        queryBuilder.append(" set cust_name = ?0, cust_no = ?1");
        queryBuilder.append(" where cost_id = ").append(costId);
        queryBuilder.append(" and transaction_type = '").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append(" and status = '").append(STATUS_OPEN).append("'");
        queryBuilder.append("and (shipment_type ='FCLI' OR shipment_type='FCLE') ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setParameter("0", vendorName);
        query.setParameter("1", vendorNumber);
        query.executeUpdate();
    }

    public void updateTransactionLedgerDates(ManifestModel model, String costIds) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update transaction_ledger ");
        queryBuilder.append("set sailing_date = '");
        queryBuilder.append(model.getSailDate()).append("' where transaction_type = 'AC' and cost_id IN(").append(costIds).append(")");
        queryBuilder.append("and (shipment_type ='LCLI' OR shipment_type ='LCLE') ");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        getCurrentSession().flush();
    }

    public TransactionLedger findByChargeId(int chargeId) {
        Criteria criteria = getCurrentSession().createCriteria(TransactionLedger.class);
        criteria.add(Restrictions.eq("transactionType", TRANSACTION_TYPE_ACCOUNT_RECEIVABLE));
        criteria.add(Restrictions.eq("chargeId", chargeId));
        criteria.addOrder(Order.desc("transactionId"));
        criteria.setMaxResults(1);
        return (TransactionLedger) criteria.uniqueResult();
    }

    public void updateTransactionAmt(String fileNumber, BigDecimal amount, Long chargeId) throws Exception {
        StringBuilder query = new StringBuilder();
        query.append("update transaction_ledger set transaction_amt=").append(amount);
        query.append(" where drcpt='").append(fileNumber).append("' AND charge_id=").append(chargeId);
        getCurrentSession().createSQLQuery(query.toString()).executeUpdate();
        getCurrentSession().flush();
    }

    public void updateVoyageNoByLcl(String fileNo, Date sailDate, String newScheNo,
            String vesselNo, Integer userId, String masterBkgNo) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append("UPDATE transaction_ledger set sailing_date =:sailDate,transaction_date=:sailDate,");
        queryStr.append("voyage_no=:newScheNo,vessel_no=:vesselNo,");
        queryStr.append("updated_on=:updateDate,updated_by=:userId ");
        queryStr.append(" where drcpt=:fileNo and shipment_type='LCLE' ");
        SQLQuery queryObject = getSession().createSQLQuery(queryStr.toString());
        queryObject.setString("fileNo", fileNo);
        queryObject.setParameter("newScheNo", newScheNo);
        queryObject.setParameter("sailDate", sailDate);
        queryObject.setParameter("vesselNo", vesselNo);
        queryObject.setParameter("updateDate", new Date());
        queryObject.setParameter("userId", userId);
        queryObject.executeUpdate();
    }

    public void updateLclEAccruals(String fileNo, String blNo, String voyageNo, String bookingNo,
            String origin, String fd, String containerNo, Date sailDate, String vesselNo) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update transaction_ledger ");
        queryBuilder.append("set bill_ladding_no=:blNo,voyage_No=:voyageNo,booking_no=:bookingNo,");
        queryBuilder.append("Originating_Terminal=:original,destination=:fd,");
        queryBuilder.append("container_No  =:containerNo,sailing_date =:sailDate,vessel_no=:vesselNo ");
        queryBuilder.append(" where subledger_source_code IN('ACC','PJ') and shipment_type='LCLE' and drcpt=:fileNo ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setParameter("fileNo", fileNo);
        query.setParameter("blNo", blNo);
        query.setParameter("voyageNo", voyageNo);
        query.setParameter("original", origin);
        query.setParameter("fd", fd);
        query.setParameter("vesselNo", vesselNo);
        query.setParameter("bookingNo", bookingNo);
        query.setParameter("containerNo", containerNo);
        query.setParameter("sailDate", sailDate);
        query.executeUpdate();
        getCurrentSession().flush();
        getCurrentSession().clear();
    }

    public void updateLclEContainers(String fileNo, String containerNo) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update transaction_ledger ");
        queryBuilder.append("set container_no  =:containerNo ");
        queryBuilder.append(" where shipment_type='LCLE' and drcpt=:fileNo ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setParameter("containerNo", containerNo);
        query.setParameter("fileNo", fileNo);
        query.executeUpdate();
    }

    public void updateLclEBlNumber(String blNumber, String fileNo) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" update transaction_ledger set Bill_Ladding_No=:blNumber where ");
        queryBuilder.append(" drcpt=:fileNo and Transaction_type='AR' and shipment_type='LCLE'");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setParameter("blNumber", blNumber);
        query.setParameter("fileNo", fileNo);
        query.executeUpdate();
        getCurrentSession().getTransaction().commit();
        getCurrentSession().getTransaction().begin();
    }
}
