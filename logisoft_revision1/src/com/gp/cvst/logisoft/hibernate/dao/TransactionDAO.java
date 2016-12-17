package com.gp.cvst.logisoft.hibernate.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.bc.accounting.ReportConstants;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.domain.RoleDuty;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.logisoft.hibernate.dao.CustomerAccountingDAO;
import com.gp.cong.logisoft.hibernate.dao.RoleDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.AccountingConstants;
import com.gp.cvst.logisoft.beans.TransactionBean;
import com.gp.cvst.logisoft.domain.CustAddress;
import com.gp.cvst.logisoft.domain.FclBlCostCodes;
import com.gp.cvst.logisoft.domain.Transaction;
import com.gp.cvst.logisoft.domain.TransactionLedger;
import com.gp.cvst.logisoft.reports.dto.AgingReportPeriodDTO;
import com.gp.cvst.logisoft.reports.dto.SearchCustomerSampleDTO;
import com.gp.cvst.logisoft.reports.dto.VendorDetailsDTO;
import com.gp.cvst.logisoft.struts.action.customerStatementVO;
import com.gp.cvst.logisoft.struts.form.AgingReportForm;
import com.gp.cvst.logisoft.struts.form.SearchCustomerSampleForm;
import com.logiware.accounting.dao.AccrualsDAO;
import com.logiware.accounting.dao.EdiInvoiceDAO;
import com.logiware.accounting.domain.ApInvoice;
import com.logiware.accounting.domain.EdiInvoice;
import com.logiware.hibernate.dao.ApTransactionHistoryDAO;
import com.logiware.hibernate.dao.ArTransactionHistoryDAO;
import com.logiware.hibernate.dao.PaymentMethodDAO;
import com.logiware.hibernate.domain.ApTransactionHistory;
import com.logiware.hibernate.domain.ArTransactionHistory;
import com.logiware.utils.AuditNotesUtils;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.util.LabelValueBean;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.BooleanType;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

/**
 * Data access object (DAO) for domain model class Transaction.
 *
 * @see com.gp.cvst.logisoft.hibernate.dao.Transaction
 * @author MyEclipse - Hibernate Tools
 */
/**
 * @author user
 *
 */
public class TransactionDAO extends BaseHibernateDAO implements java.io.Serializable, ConstantsInterface {

    private static final long serialVersionUID = 5523369665556460703L;
    NumberFormat number = new DecimalFormat("###,###,##0.00");
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    CustomerAccountingDAO customerAccountingDAO = new CustomerAccountingDAO();
    // property constants

    public void save(Transaction transientInstance) throws Exception {
        getSession().save(transientInstance);
    }

    public void delete(Transaction persistentInstance) throws Exception {
        getSession().delete(persistentInstance);
    }

    public Transaction findById(java.lang.Integer id) throws Exception {
        Transaction instance = (Transaction) getSession().get(
                "com.gp.cvst.logisoft.domain.Transaction", id);
        return instance;
    }

    public TransactionBean findByIdTransactionBean(java.lang.Integer id) throws Exception {
        TransactionBean instance = (TransactionBean) getSession().get(
                "com.gp.cvst.logisoft.domain.TransactionBean", id);
        return instance;
    }

    public List findByExample(Transaction instance) throws Exception {
        List results = getSession().createCriteria(
                "com.gp.cvst.logisoft.domain.Transaction").add(
                Example.create(instance)).list();
        return results;
    }

    public List findByProperty(String propertyName, Object value) throws Exception {
        String queryString = "from Transaction as model where model." + propertyName + "= ?0";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", value);
        return queryObject.list();
    }

    public List findByAr(String custNo, String transactionType) throws Exception {
        String queryString = "from Transaction as model where custNo=?0 and transactionType=?1";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", custNo);
        queryObject.setParameter("1", transactionType);
        return queryObject.list();
    }

    public Transaction merge(Transaction detachedInstance) throws Exception {
        Transaction result = (Transaction) getSession().merge(
                detachedInstance);
        return result;
    }

    public void attachDirty(Transaction instance) throws Exception {
        getSession().saveOrUpdate(instance);
        getSession().flush();
    }

    public void attachClean(Transaction instance) throws Exception {
        getSession().buildLockRequest(LockOptions.NONE).lock(instance);
    }

    public List<TransactionBean> findByTransactionIds(String transactionIds) throws Exception {
        List<TransactionBean> list = new ArrayList<TransactionBean>();
        String queryString = "from Transaction where transactionId in (" + transactionIds + ")";
        List<Transaction> resultList = getCurrentSession().createQuery(queryString).list();
        for (Transaction transaction : resultList) {
            TransactionBean transactionBean = new TransactionBean(transaction);
            list.add(transactionBean);
        }
        return list;
    }

    public Transaction findByTransactionByBillNoAndCustomer(String billladdingNo, String custNo) throws Exception {
        String queryString = "from Transaction where billLaddingNo='" + billladdingNo + "' and custNo='" + custNo + "' and transactionType='AR'";
        return (Transaction) getCurrentSession().createQuery(queryString).setMaxResults(1).uniqueResult();
    }

    public Transaction findByTransactionByInvoiceNoAndCustomer(String invoiceNumber, String custNo) throws Exception {
        String queryString = "from Transaction where invoiceNumber='" + invoiceNumber + "' and custNo='" + custNo + "' and transactionType = 'AR'";
        return (Transaction) getCurrentSession().createQuery(queryString).setMaxResults(1).uniqueResult();
    }

    public List getTransactionValues(String billladdingNo, String custNo,
            Double transactionAmount, String transType) throws Exception {
        List result = null;
        String querystring = "update Transaction set transactionAmt=" + transactionAmount + " "
                + "where billLaddingNo='" + billladdingNo + "' and custNo='" + custNo + "' "
                + "and transactionType='" + transType + "' and transactionAmt>0";

        int id = getCurrentSession().createQuery(querystring).executeUpdate();
        return result;
    }

    public List updateBalanceInProcess(String billladdingNo, String custNo,
            Double transactionAmount, String transType) throws Exception {
        List result = null;
        String querystring = "update Transaction set "
                + "balanceInProcess=" + transactionAmount + " where billLaddingNo='" + billladdingNo + "' and custNo='" + custNo + "' and transactionType='" + transType + "' and transactionAmt>0";

        int id = getCurrentSession().createQuery(querystring).executeUpdate();
        return result;
    }

    public List getTransactionValues2(String billladdingNo, String custNo,
            Double transactionAmount, String transType) throws Exception {
        List result = null;
        String querystring = "update Transaction set transactionAmt=" + transactionAmount + " where invoiceNumber='" + billladdingNo + "' and custNo='" + custNo + "' and transactionType='" + transType + "'";

        int id = getCurrentSession().createQuery(querystring).executeUpdate();
        return result;
    }

    public List getTransactionValues1(String billladdingNo, String custNo,
            Double transactionAmount, String transType) throws Exception {
        List result = null;
        String querystring = "update Transaction set balance=" + transactionAmount + " "
                + "where billLaddingNo='" + billladdingNo + "' and custNo='" + custNo + "'"
                + " and transactionType='" + transType + "' and transactionAmt>0";

        int id = getCurrentSession().createQuery(querystring).executeUpdate();
        return result;
    }

    public List updateBalanceInProcessForAPinvoice(String billladdingNo,
            String custNo, Double transactionAmount, String transType) throws Exception {
        List result = null;
        String querystring = "update Transaction set balanceInProcess=" + transactionAmount + " where billLaddingNo='" + billladdingNo + "' and custNo='" + custNo + "' and transactionType='" + transType + "' and transactionAmt>0";

        int id = getCurrentSession().createQuery(querystring).executeUpdate();
        return result;
    }

    public List getTransactionValues3(String billladdingNo, String custNo,
            Double transactionAmount, String transType) throws Exception {
        List result = null;
        String querystring = "update Transaction set balance=" + transactionAmount + " where invoiceNumber='" + billladdingNo + "' and custNo='" + custNo + "' and transactionType='" + transType + "'";

        int id = getCurrentSession().createQuery(querystring).executeUpdate();
        return result;
    }

    public List updateBalanceInProcessInARInvoice(String billladdingNo,
            String custNo, Double transactionAmount, String transType) throws Exception {
        List result = null;
        String querystring = "update Transaction set balanceInProcess=" + transactionAmount + " where invoiceNumber='" + billladdingNo + "' and custNo='" + custNo + "' and transactionType='" + transType + "'";

        int id = getCurrentSession().createQuery(querystring).executeUpdate();
        return result;
    }

    public List<TransactionBean> getTranactionsForCustomer(String customerno) throws Exception {
        List<TransactionBean> transList = new ArrayList<TransactionBean>();
        String querystring = "";
        querystring = "from Transaction tx where tx.custNo='" + customerno + "' and tx.transactionType='AR' and tx.billTo='y' and tx.balanceInProcess!=0.0 Order By tx.transactionDate,tx.invoiceNumber DESC";
        getCurrentSession().clear();
        List resultList = getCurrentSession().createQuery(querystring).list();
        int tempVar = 0;
        while (resultList != null && resultList.size() > tempVar) {
            Transaction transaction = null;
            TransactionBean transactionBean = null;
            transaction = (Transaction) resultList.get(tempVar);
            transactionBean = new TransactionBean(transaction);
            transList.add(transactionBean);
            tempVar++;
        }

        return transList;
    }

    public List<TransactionBean> getApplyPaymentTransListForPagination(String custNo, int page, int size, String selectedIds) throws Exception {
        List<TransactionBean> transList = new ArrayList<TransactionBean>();
        int selectedIdsSize = 0;
        String transactionIds = "";
        String transactionLedgerIds = "";
        int transactionIdsSize = 0;
        if (null != selectedIds && !selectedIds.trim().equals("")) {
            if (null != selectedIds && !selectedIds.trim().equals("")) {
                if (selectedIds.endsWith(",")) {
                    selectedIds = selectedIds.substring(0, selectedIds.length() - 1);
                }
                String[] selectedId = StringUtils.splitByWholeSeparator(selectedIds, ",");
                for (String id : selectedId) {
                    if (null != id && id.contains("AC")) {
                        transactionLedgerIds = id.replaceAll("AC", "") + "," + transactionLedgerIds;
                    } else {
                        transactionIds = id + "," + transactionIds;
                        transactionIdsSize++;
                    }
                }
            }
            if (selectedIds.endsWith(",")) {
                selectedIds = selectedIds.substring(0, selectedIds.length() - 1);
            }
            String[] selectedId = StringUtils.splitByWholeSeparator(selectedIds, ",");
            selectedIdsSize = selectedIdsSize + selectedId.length;
        }
        if (selectedIdsSize > 0) {
            if (!transactionIds.trim().equals("")) {
                if (transactionIds.endsWith(",")) {
                    transactionIds = transactionIds.substring(0, transactionIds.length() - 1);
                }
                List<TransactionBean> trList = findByTransactionIds(transactionIds);
                if (null != trList && !trList.isEmpty()) {
                    transList.addAll(trList);
                }
                if (!transactionLedgerIds.trim().equals("")) {
                    if (transactionLedgerIds.endsWith(",")) {
                        transactionLedgerIds = transactionLedgerIds.substring(0, transactionLedgerIds.length() - 1);
                    }
                    TransactionLedgerDAO transactionLedgerDAO = new TransactionLedgerDAO();
                    List<TransactionBean> tlList = transactionLedgerDAO.findByTransactionIds(transactionLedgerIds);
                    if (null != tlList && !tlList.isEmpty()) {
                        transList.addAll(tlList);
                    }
                }
            }
        }
        StringBuilder queryString = new StringBuilder();
        queryString.append("from Transaction tx where tx.custNo='").append(custNo).append("' and tx.transactionType='AR' and tx.balanceInProcess!=0.0");
        if (transactionIdsSize > 0) {
            queryString.append("and transactionId not in(").append(transactionIds).append(")");
        }
        queryString.append("  order By tx.transactionDate,tx.invoiceNumber DESC");
        getCurrentSession().clear();
        Query query = getCurrentSession().createQuery(queryString.toString());
        if (size > transactionIdsSize) {
            query.setFirstResult((page * size) + transactionIdsSize).setMaxResults(size - transactionIdsSize);
            List<Transaction> resultList = query.list();
            if (null != resultList && !resultList.isEmpty()) {
                for (Transaction transaction : resultList) {
                    TransactionBean transactionBean = new TransactionBean(transaction);
                    transList.add(transactionBean);
                }
            }
        }
        return transList;
    }

    /**
     * Used in the case of Editing from BAtch details pop In AR Batch
     *
     * @param customerno
     * @return
     */
    public List getTranactionsForCustomerWithPaymentAmount(String customerno) throws Exception {
        //
        List<TransactionBean> transList = new ArrayList<TransactionBean>();
        String querystring = "";
        querystring = "from Transaction tx where tx.custNo='" + customerno + "' and tx.transactionType='AR' and tx.billTo='y' and tx.balanceInProcess!=0.0";
        getCurrentSession().clear();
        List resultList = getCurrentSession().createQuery(querystring).list();
        int tempVar = 0;
        while (resultList != null && resultList.size() > tempVar) {
            Transaction transaction = null;
            TransactionBean transactionBean = null;
            transaction = (Transaction) resultList.get(tempVar);
            transactionBean = new TransactionBean(transaction);
            transList.add(transactionBean);
            tempVar++;
        }

        return transList;
    }

    public Transaction getTransactionByPassingBLNumberAndCostomerNo(String blNumber, String custNo, String dockReceipt) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(Transaction.class);
        criteria.add(Restrictions.or(Restrictions.eq("billLaddingNo", blNumber), Restrictions.eq("docReceipt", dockReceipt)));
        criteria.add(Restrictions.eq("custNo", custNo));
        criteria.add(Restrictions.eq("transactionType", "AR"));
        criteria.setMaxResults(1);
        return (Transaction) criteria.uniqueResult();
    }

    public List getTransactionforBolIDToDelete(String bolId) throws Exception {
        String queryString = "from Transaction where billLaddingNo='" + bolId + "' and transactionType='AR'";
        List resultList = getCurrentSession().createQuery(queryString).list();
        return resultList;
    }

    public List getTransactionforBolID(String bolId) throws Exception {
        String queryString = "from Transaction where billLaddingNo='" + bolId + "' and transactionType='AA'";
        List resultList = getCurrentSession().createQuery(queryString).list();
        return resultList;
    }

    public List getTransactionforInvoiceNo(String bolId) throws Exception {
        String queryString = "from Transaction where invoiceNumber='" + bolId + "' and transactionType='AA'";
        List resultList = getCurrentSession().createQuery(queryString).list();
        return resultList;
    }

    public List getTranactionsForCustomerandParent(String custid,
            String parentname) throws Exception {
        List<TransactionBean> transList = new ArrayList<TransactionBean>();
        if (custid != null && !custid.equals("")) {
            transList = getTranactionsForCustomer(custid);
        }

        String querystringForparent = "from Transaction tx where tx.custName='" + parentname + "' and tx.transactionType='AR' and tx.billTo='y' and tx.balance!=0.0";

        List resultList = getCurrentSession().createQuery(querystringForparent).list();
        int tempVar = 0;
        while (resultList != null && resultList.size() > tempVar) {
            Transaction transaction = null;
            TransactionBean transactionBean = null;
            transaction = (Transaction) resultList.get(tempVar);
            transactionBean = new TransactionBean(transaction);
            transList.add(transactionBean);
            tempVar++;
        }
        return transList;

    }

    public List getTranactionsForCustomerandParent(String custid,
            String parentname, int page, int size) throws Exception {
        TransactionBean transbean = null;
        List<TransactionBean> transList = new ArrayList<TransactionBean>();
        if (custid != null && !custid.equals("")) {
            transList = getTranactionsForCustomer(custid);
        }

        String querystringForparent = "from Transaction tx where tx.custName='" + parentname + "' and tx.transactionType='AR' and tx.billTo='y' and tx.balance!=0.0";

        List resultList = getCurrentSession().createQuery(querystringForparent).list();
        int tempVar = 0;
        while (resultList != null && resultList.size() > tempVar) {
            Transaction transaction = null;
            TransactionBean transactionBean = null;
            transaction = (Transaction) resultList.get(tempVar);
            transactionBean = new TransactionBean(transaction);
            transList.add(transactionBean);
            tempVar++;
        }
        return transList;

    }

    public Integer getApplyPaymentTransListSizeForPagination(String custNumber) throws Exception {
        Integer totalSize = 0;
        if (null != custNumber && !custNumber.trim().equals("")) {
            String queryString = "select count(*) from Transaction tx where tx.custNo='" + custNumber + "' and tx.transactionType='AR' and tx.balanceInProcess!=0.0 ";
            getCurrentSession().clear();
            Object count = getCurrentSession().createQuery(queryString).uniqueResult();
            if (null != count) {
                Integer size = Integer.parseInt(count.toString());
                if (null != size) {
                    totalSize = totalSize + size;
                }
            }
        }
        return totalSize.intValue();
    }

    public void updateRemainingBalance(String transid, String invoiceno, String amt) throws Exception {
        String queryString = "update Transaction tx set tx.balanceInProcess=tx.balanceInProcess-'" + amt + "' where tx.transactionId='" + transid + "'";
        if (invoiceno != null && !invoiceno.trim().equals("")) {
            queryString = queryString + " and tx.invoiceNumber='" + invoiceno + "'";
        }
        getCurrentSession().createQuery(queryString).executeUpdate();
        // getCurrentSession().flush();
    }

    public List findforshowAll(String customerNumber, String fromdate,
            String todate, String checkNumber, double checkAmount,
            String invoiceNo, double invAmt, String accountType) throws Exception {
        NumberFormat number = new DecimalFormat("0.00");
        List<TransactionBean> genericBean = new ArrayList<TransactionBean>();
        String m1 = "";
        String d1 = "";
        String y1 = "";
        String m2 = "";
        String d2 = "";
        String y2 = "";
        String sdate = fromdate;
        String edate = todate;
        StringTokenizer str = new StringTokenizer(sdate, "/");
        if (str.hasMoreTokens()) {
            m1 = str.nextToken();
            d1 = str.nextToken();
            y1 = str.nextToken();
        }
        sdate = y1 + "-" + m1 + "-" + d1;

        StringTokenizer str1 = new StringTokenizer(edate, "/");
        if (str1.hasMoreTokens()) {
            m2 = str1.nextToken();
            d2 = str1.nextToken();
            y2 = str1.nextToken();
        }
        edate = y2 + "-" + m2 + "-" + d2;
        TransactionBean transactionBean = null;
        String QueryString = "";

        if ((fromdate.equals("")) && (todate.equals("")) && invoiceNo.equals("") && (checkAmount == 0.0) && (invAmt == 0.0)) {

            QueryString = "select  tx.custName,tx.custNo,tx.transactionDate,tx.billLaddingNo,tx.transactionAmt,tx.balance,tx.transactionType,tx.blTerms,tx.subHouseBl,tx.masterBl,tx.voyageNo,tx.vesselNo,tx.containerNo,tx.transactionId,ca.hold from Transaction tx ,CustAddress ca where  tx.custNo='" + customerNumber + "'  and tx.custNo=ca.acctNo and   tx.transactionType='AR'";
        } else if (((fromdate != "") && (todate != "")) && ((invoiceNo.equals("")) && ((checkAmount == 0.0)) && (invAmt == 0.0))) {

            QueryString = "select  tx.custName,tx.custNo,tx.transactionDate,tx.billLaddingNo,tx.transactionAmt,tx.balance,tx.transactionType,tx.blTerms,tx.subHouseBl,tx.masterBl,tx.voyageNo,tx.vesselNo,tx.containerNo,tx.transactionId,ca.hold from Transaction tx ,CustAddress ca where tx.custNo='" + customerNumber + "' and tx.transactionType='AR' and tx.transactionDate Between'" + sdate + "'and '" + edate + "'";

        } else if (((invoiceNo != "")) && (((fromdate.equals(""))) && ((todate.equals(""))) && ((checkAmount == 0.0)) && (invAmt == 0.0))) {

            QueryString = "select  tx.custName,tx.custNo,tx.transactionDate,tx.billLaddingNo,tx.transactionAmt,tx.balance,tx.transactionType,tx.blTerms,tx.subHouseBl,tx.masterBl,tx.voyageNo,tx.vesselNo,tx.containerNo,tx.transactionId,ca.hold from Transaction tx ,CustAddress ca where tx.invoiceNumber='" + invoiceNo + "'and  tx.custNo='" + customerNumber + "' and tx.custNo=ca.acctNo and tx.transactionType='AR' ";
        } else if ((checkAmount != 0.0) && ((fromdate.equals(""))) && ((todate.equals(""))) && ((invoiceNo.equals(""))) && (invAmt == 0)) {

            QueryString = "select tx.custName,tx.custNo,tx.transactionDate,tx.billLaddingNo,tx.transactionAmt,tx.balance,tx.transactionType,tx.blTerms,tx.subHouseBl,tx.masterBl,tx.voyageNo,tx.vesselNo,tx.containerNo,tx.transactionId,ca.hold from Transaction tx ,CustAddress ca where tx.transactionAmt='" + checkAmount + "' and tx.custNo='" + customerNumber + "' and tx.custNo=ca.acctNo and tx.transactionType='AR'";
        } else if ((invAmt != 0) && (fromdate.equals("")) && (todate.equals("")) && invoiceNo.equals("") && (checkAmount == 0.0) && (customerNumber.equals(""))) {

            QueryString = "select tx.custName,tx.custNo,tx.transactionDate,tx.billLaddingNo,tx.transactionAmt,tx.balance,tx.transactionType,tx.blTerms,tx.subHouseBl,tx.masterBl,tx.voyageNo,tx.vesselNo,tx.containerNo,tx.transactionId,ca.hold from Transaction tx ,CustAddress ca where tx.transactionAmt='" + invAmt + "'  and tx.transactionType='AR' and tx.custNo=ca.acctNo ";
        }

        List queryObject = getCurrentSession().createQuery(QueryString).list();

        Iterator itr = queryObject.iterator();
        CustAddressDAO custAddressDAO = null;
        while (itr.hasNext()) {

            transactionBean = new TransactionBean();
            custAddressDAO = new CustAddressDAO();
            Object[] row = (Object[]) itr.next();
            String custName = (String) (row[0]);
            String custNo = (String) (row[1]);

            Date invoiceDate = (Date) (row[2]);
            String invoceorBL = (String) (row[3]);
            Double amt = (Double) (row[4]);
            // (String)(row[5]);
            Double balance = (Double) row[5];
            String recordtype = (String) (row[6]);
            String blterms = (String) (row[7]);
            String subhouse = (String) (row[8]);
            String master = (String) (row[9]);
            String voyage = (String) (row[10]);

            String vessel = (String) (row[11]);
            String container = (String) (row[12]);
            String transactionId = row[13].toString();
            String hold = row[14].toString();
            Date today = new Date();
            List creditHold = custAddressDAO.getHold(custNo);
            String creditHold1 = "";
            String acctType = "";
            for (int i = 0; i < creditHold.size(); i++) {
                creditHold1 = (String) row[0];
                acctType = (String) row[1];
            }
            if (hold != null && !hold.equals("B")) {
                transactionBean.setHold(hold);
            } else {
                transactionBean.setHold("");
            }
            if (acctType != null) {
                transactionBean.setAcctType(acctType);
            } else {
                transactionBean.setAcctType("");

            }

            // this is to conver from date to string format
            String invDate = "";
            if (invoiceDate != null) {
                invDate = sdf.format(invoiceDate);
            }

            // to set caledartime

            Calendar cal1 = Calendar.getInstance();
            if (invoiceDate != null) {
                cal1.setTime(invoiceDate);
            }

            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(today);
            if (invoiceDate != null) {
                long age = printOutput("Calendar ", invoiceDate, today,
                        daysBetween(cal1, cal2));
                transactionBean.setAge(age);
            }

            transactionBean.setCustomer(custName);
            transactionBean.setCustomerNo(custNo);

            transactionBean.setInvoiceDate(invDate);
            // this is to conver to String value to double

            if (amt != null) {
                transactionBean.setAmount(number.format(amt));
            }

            transactionBean.setRecordType(recordtype);
            transactionBean.setInvoiceOrBl(invoceorBL);
            transactionBean.setBlterms(blterms);

            if (balance == null) {
                balance = 0.0;
            }
            transactionBean.setBalance(number.format(balance));
            transactionBean.setSubhouseBl(subhouse);
            transactionBean.setMasterbl(master);

            transactionBean.setVesselnumber(vessel);
            transactionBean.setVoyagenumber(voyage);

            transactionBean.setCotainernumber(container);
            transactionBean.setTransactionId(transactionId);

            genericBean.add(transactionBean);

            transactionBean = null;

        }

        return genericBean;
    }

    public List masterSubsidary(String custNo) throws Exception {
        String QueryString = ("select  tx.custName,tx.custNo,tx.transactionDate,tx.billLaddingNo,tx.transactionAmt,tx.balance,tx.transactionType,tx.blTerms,tx.subHouseBl,tx.masterBl,tx.voyageNo,tx.vesselNo,tx.containerNo,tx.transactionId,ca.hold,tx.billTo,ca.type,tx.consName ,tx.consNo,tx.fwdName,tx.fwdNo,tx.thirdptyName,tx.thirdptyNo,tx.agentName,tx.agentNo from Transaction tx ,CustAddress ca where tx.custNo=ca.acctNo  and ca.acctNo='" + custNo + "'");
        List queryObject = getCurrentSession().createQuery(QueryString).list();
        Iterator itr = queryObject.iterator();
        TransactionBean transactionBean = new TransactionBean();
        NumberFormat number = new DecimalFormat("0.00");
        List<TransactionBean> genericBean1 = new ArrayList<TransactionBean>();

        int i = 0;
        while (itr.hasNext()) {

            i++;
            transactionBean = new TransactionBean();

            Object[] row = (Object[]) itr.next();
            String custName = (String) (row[0]);
            String custNo1 = (String) (row[1]);

            Date invoiceDate = (Date) (row[2]);
            String invoceorBL = (String) (row[3]);
            Double amt = (Double) (row[4]);
            // (String)(row[5]);
            Double balance = (Double) row[5];
            String recordtype = (String) (row[6]);
            String blterms = (String) (row[7]);
            String subhouse = (String) (row[8]);
            String master = (String) (row[9]);
            String voyage = (String) (row[10]);
            String vessel = (String) (row[11]);
            String container = (String) (row[12]);

            String transactionId = row[13].toString();
            String hold = row[14].toString();

            String billTo1 = "";

            if (row[15] != null && !row[15].equals("")) {
                billTo1 = row[15].toString();
            }

            String type = "";
            List accountNames = new ArrayList();
            if (row[16] != null) {
                type = row[16].toString();
            }

            String fwdName = "";
            String fwdNo = "";
            String consName = "";
            String consNo = "";
            String thirdptyName = "";
            String thirdptyNo = "";
            String agentName = "";
            String agentNo = "";

            if (row[19] != null && !row[19].equals("")) {

                transactionBean.setFwdName(row[17].toString());

            }
            if (row[20] != null && !row[20].equals("")) {
                transactionBean.setFwdNo(row[18].toString());
            }
            if (row[23] != null && !row[23].equals("")) {
                transactionBean.setAgentName(row[23].toString());
            }
            if (row[24] != null && !row[24].equals("")) {
                transactionBean.setAgentNo(row[24].toString());
            }
            if (row[21] != null && !row[21].equals("")) {
                transactionBean.setThirdptyName(row[21].toString());
            }
            if (row[22] != null && !row[22].equals("")) {
                transactionBean.setThirdptyNo(row[22].toString());
            }
            if (row[17] != null && !row[17].equals("")) {
                transactionBean.setConsName(row[17].toString());
            }
            if (row[18] != null && !row[18].equals("")) {
                transactionBean.setConsNo(row[18].toString());
            }

            Date today = new Date();

            if (hold != null && !hold.equals("B")) {
                transactionBean.setHold(hold);
            } else {
                transactionBean.setHold("");
            }

            // this is to conver from date to string format
            String invDate = "";
            if (invoiceDate != null) {
                invDate = sdf.format(invoiceDate);
            }

            // to set caledartime

            Calendar cal1 = Calendar.getInstance();
            if (invoiceDate != null) {
                cal1.setTime(invoiceDate);
            }

            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(today);
            if (invoiceDate != null) {
                long age = printOutput("Calendar ", invoiceDate, today,
                        daysBetween(cal1, cal2));
                transactionBean.setAge(age);
            }

            transactionBean.setCustomer(custName);

            transactionBean.setCustomerNo(custNo);

            transactionBean.setInvoiceDate(invDate);
            // this is to conver to String value to double
            if (amt == null) {
                amt = 0.00;
            }
            transactionBean.setAmount(number.format(amt));
            transactionBean.setRecordType(recordtype);
            transactionBean.setInvoiceOrBl(invoceorBL);
            transactionBean.setBlterms(blterms);
            if (hold != null && !hold.equals("B")) {
                transactionBean.setHold(hold);
            } else {
                transactionBean.setHold("");
            }
            if (balance == null) {
                balance = 0.0;
            }
            transactionBean.setBalance(number.format(balance));
            transactionBean.setSubhouseBl(subhouse);
            transactionBean.setMasterbl(master);
            transactionBean.setVesselnumber(vessel);
            transactionBean.setVoyagenumber(voyage);
            transactionBean.setCotainernumber(container);
            transactionBean.setTransactionId(transactionId);
            transactionBean.setBillTo(billTo1);
            genericBean1.add(transactionBean);
            transactionBean = null;

        }
        return genericBean1;

    }

    public List findforshowAllTL(String customerNumber, String fromdate,
            String todate, String checkNumber, double checkAmount,
            String invoiceNo, double invAmt, String accountType) throws Exception {

        NumberFormat number = new DecimalFormat("0.00");
        List<TransactionBean> genericBean = new ArrayList<TransactionBean>();
        String m1 = "";
        String d1 = "";
        String y1 = "";
        String m2 = "";
        String d2 = "";
        String y2 = "";
        String sdate = fromdate;
        String edate = todate;

        StringTokenizer str = new StringTokenizer(sdate, "/");
        if (str.hasMoreTokens()) {
            m1 = str.nextToken();
            d1 = str.nextToken();
            y1 = str.nextToken();
        }
        sdate = y1 + "-" + m1 + "-" + d1;

        StringTokenizer str1 = new StringTokenizer(edate, "/");
        if (str1.hasMoreTokens()) {
            m2 = str1.nextToken();
            d2 = str1.nextToken();
            y2 = str1.nextToken();
        }
        edate = y2 + "-" + m2 + "-" + d2;
        TransactionBean transactionBean = null;
        String QueryString = "";

        if ((fromdate.equals("")) && (todate.equals("")) && invoiceNo.equals("") && (checkAmount == 0.0) && (invAmt == 0.0)) {

            QueryString = "select  tx.custName,tx.custNo,tx.transactionDate,tx.billLaddingNo,tx.transactionAmt,tx.balance,tx.transactionType,tx.blTerms,tx.subHouseBl,tx.masterBl,tx.voyageNo,tx.vesselNo,tx.containerNo,tx.transactionId from Transaction tx ,CustAddress ca where  tx.custNo='" + customerNumber + "'  and tx.custNo=ca.acctNo and ca.acctType='" + accountType + "' " + "union all" + " select tx1.custName,tx1.custNo,tx1.transactionDate,tx1.billLaddingNo,tx1.transactionAmt,tx1.balance,tx1.transactionType,tx1.blTerms,tx1.subHouseBl,tx1.masterBl,tx1.voyageNo,tx1.vesselNo,tx1.containerNo,tx1.transactionId from TransactionLedger tx1 where  tx1.custNo='" + customerNumber + "'and tx.transactionType='AR'";
        } else if (((!fromdate.equals("")) && (todate != "")) && ((invoiceNo.equals("")) && ((checkAmount == 0.0)) && (invAmt == 0.0))) {
            QueryString = "select  tx.custName,tx.custNo,tx.transactionDate,tx.billLaddingNo,tx.transactionAmt,tx.balance,tx.transactionType,tx.blTerms,tx.subHouseBl,tx.masterBl,tx.voyageNo,tx.vesselNo,tx.containerNo,tx.transactionId from Transaction tx ,CustAddress ca where tx.custNo='" + customerNumber + "' and tx.transactionType='AR' and ca.acctType='" + accountType + "' and tx.transactionDate Between'" + sdate + "'and '" + edate + "'" + "union all" + " select tx1.custName,tx1.custNo,tx1.transactionDate,tx1.billLaddingNo,tx1.transactionAmt,tx1.balance,tx1.transactionType,tx1.blTerms,tx1.subHouseBl,tx1.masterBl,tx1.voyageNo,tx1.vesselNo,tx1.containerNo,tx1.transactionId from TransactionLedger tx1 where tx1.custNo='" + customerNumber + "' and tx1.transactionType='AR'";
        } else if (((!invoiceNo.equals(""))) && (((fromdate.equals(""))) && ((todate.equals(""))) && ((checkAmount == 0.0)) && (invAmt == 0.0))) {
            QueryString = "select  tx.custName,tx.custNo,tx.transactionDate,tx.billLaddingNo,tx.transactionAmt,tx.balance,tx.transactionType,tx.blTerms,tx.subHouseBl,tx.masterBl,tx.voyageNo,tx.vesselNo,tx.containerNo,tx.transactionId from Transaction tx ,CustAddress ca where tx.invoiceNumber='" + invoiceNo + "'and  tx.custNo='" + customerNumber + "' and tx.custNo=ca.acctNo and tx.transactionType='AR' and ca.acctType='" + accountType + "' " + "union all" + " select tx1.custName,tx1.custNo,tx1.transactionDate,tx1.billLaddingNo,tx1.transactionAmt,tx1.balance,tx1.transactionType,tx1.blTerms,tx1.subHouseBl,tx1.masterBl,tx1.voyageNo,tx1.vesselNo,tx1.containerNo,tx1.transactionId from TransactionLedger tx1 where tx1.invoiceNumber='" + invoiceNo + "'and  tx1.custNo='" + customerNumber + "' and tx1.transactionType='AR'";
        } else if ((checkAmount != 0.0) && ((fromdate.equals(""))) && ((todate.equals(""))) && ((invoiceNo.equals(""))) && (invAmt == 0)) {
            QueryString = "select tx.custName,tx.custNo,tx.transactionDate,tx.billLaddingNo,tx.transactionAmt,tx.balance,tx.transactionType,tx.blTerms,tx.subHouseBl,tx.masterBl,tx.voyageNo,tx.vesselNo,tx.containerNo,tx.transactionId from Transaction tx ,CustAddress ca where tx.transactionAmt='" + checkAmount + "' and tx.custNo='" + customerNumber + "' and tx.custNo=ca.acctNo and tx.transactionType='AR' and ca.acctType='" + accountType + "'" + "union all" + " select tx1.custName,tx1.custNo,tx1.transactionDate,tx1.billLaddingNo,tx1.transactionAmt,tx1.balance,tx1.transactionType,tx1.blTerms,tx1.subHouseBl,tx1.masterBl,tx1.voyageNo,tx1.vesselNo,tx1.containerNo,tx1.transactionId from TransactionLedger tx1 where tx1.transactionAmt='" + checkAmount + "' and tx1.custNo='" + customerNumber + "' and tx1.transactionType='AR'";
        } else if ((invAmt != 0) && (fromdate.equals("")) && (todate.equals("")) && invoiceNo.equals("") && (checkAmount == 0.0) && (customerNumber.equals(""))) {
            QueryString = "select tx.custName,tx.custNo,tx.transactionDate,tx.billLaddingNo,tx.transactionAmt,tx.balance,tx.transactionType,tx.blTerms,tx.subHouseBl,tx.masterBl,tx.voyageNo,tx.vesselNo,tx.containerNo,tx.transactionId from Transaction tx ,CustAddress ca where tx.transactionAmt='" + invAmt + "'  and tx.transactionType='AR' and tx.custNo=ca.acctNo  and ca.acctType='" + accountType + "'" + "union all" + " select tx1.custName,tx1.custNo,tx1.transactionDate,tx1.billLaddingNo,tx1.transactionAmt,tx1.balance,tx1.transactionType,tx1.blTerms,tx1.subHouseBl,tx1.masterBl,tx1.voyageNo,tx1.vesselNo,tx1.containerNo,tx1.transactionId from TransactionLedger tx1 where tx1.transactionAmt='" + invAmt + "'  and tx1.transactionType='AR'";
        }

        List queryObject = getCurrentSession().createQuery(QueryString).list();

        Iterator itr = queryObject.iterator();
        CustAddressDAO custAddressDAO = null;
        while (itr.hasNext()) {

            transactionBean = new TransactionBean();
            custAddressDAO = new CustAddressDAO();
            Object[] row = (Object[]) itr.next();

            String custName = (String) (row[0]);
            String custNo = (String) (row[1]);

            Date invoiceDate = (Date) (row[2]);
            String invoceorBL = (String) (row[3]);
            Double amt = (Double) (row[4]);
            // (String)(row[5]);
            Double balance = (Double) row[5];
            String recordtype = (String) (row[6]);
            String blterms = (String) (row[7]);
            String subhouse = (String) (row[8]);
            String master = (String) (row[9]);
            String voyage = (String) (row[10]);
            String vessel = (String) (row[11]);
            String container = (String) (row[12]);
            String transactionId = row[13].toString();
            Date today = new Date();
            List creditHold = custAddressDAO.getHold(custNo);
            String creditHold1 = "";
            String acctType = "";
            for (int i = 0; i < creditHold.size(); i++) {
                creditHold1 = (String) row[0];
                acctType = (String) row[1];
            }
            if (creditHold1 != null) {
                transactionBean.setHold(creditHold1);
            } else {
                transactionBean.setHold("");
            }
            if (acctType != null) {
                transactionBean.setAcctType(acctType);
            } else {
                transactionBean.setAcctType("");

            }
            if (creditHold != null) {
                transactionBean.setHold(creditHold1);
            } else {
                transactionBean.setHold("");
            }

            // this is to conver from date to string format
            String invDate = "";
            if (invoiceDate != null) {
                invDate = sdf.format(invoiceDate);
            }

            // to set caledartime

            Calendar cal1 = Calendar.getInstance();
            if (invoiceDate != null) {
                cal1.setTime(invoiceDate);
            }

            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(today);
            if (invoiceDate != null) {
                long age = printOutput("Calendar ", invoiceDate, today,
                        daysBetween(cal1, cal2));
                transactionBean.setAge(age);
            }

            transactionBean.setCustomer(custName);
            transactionBean.setCustomerNo(custNo);

            transactionBean.setInvoiceDate(invDate);
            // this is to conver to String value to double

            transactionBean.setAmount(number.format(amt));

            transactionBean.setRecordType(recordtype);
            transactionBean.setInvoiceOrBl(invoceorBL);
            transactionBean.setBlterms(blterms);
            transactionBean.setHold(creditHold1);
            if (balance == null) {
                balance = 0.0;
            }
            transactionBean.setBalance(number.format(balance));
            transactionBean.setSubhouseBl(subhouse);
            transactionBean.setMasterbl(master);
            transactionBean.setVesselnumber(vessel);
            transactionBean.setVoyagenumber(voyage);
            transactionBean.setCotainernumber(container);
            transactionBean.setTransactionId(transactionId);
            genericBean.add(transactionBean);
            transactionBean = null;

        }
        return genericBean;

    }

    public static long printOutput(String type, Date invoiceDate, Date today,
            long result) throws Exception {
        return result;
    }

    public static long daysBetween(Calendar invoiceDate, Calendar today) throws Exception {
        Calendar date = (Calendar) invoiceDate.clone();
        long daysBetween = 0;
        while (date.before(today)) {
            date.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }
        return daysBetween;
    }

    public List findforsearch(String transid) throws Exception {
        List<TransactionBean> adjustmentList = new ArrayList<TransactionBean>();
        TransactionBean adjustbean = null;
        String queryString = "";
        queryString = "select tx.subHouseBl,tx.masterBl,tx.voyageNo,tx.vesselNo,tx.containerNo,tx.customerReferenceNo from Transaction tx where tx.transactionId='" + transid + "'";
        List queryObject = getCurrentSession().createQuery(queryString).list();
        Iterator itr = queryObject.iterator();
        while (itr.hasNext()) {
            adjustbean = new TransactionBean();
            Object[] row = (Object[]) itr.next();
            String subhousebl = (String) (row[0]);
            String masterbl = (String) (row[1]);
            String vayageno = (String) (row[2]);
            String vesselno = (String) (row[3]);
            String containerno = (String) (row[4]);
            String customerReferenceNo = (String) (row[5]);

            adjustbean.setSubhouseBl(subhousebl);
            adjustbean.setMasterbl(masterbl);
            adjustbean.setVoyagenumber(vayageno);
            adjustbean.setVesselnumber(vesselno);
            adjustbean.setCotainernumber(containerno);
            adjustbean.setCustomerReference(customerReferenceNo);
            adjustmentList.add(adjustbean);
            adjustbean = null;
        }
        return adjustmentList;
    }// end for the list

    public List findforshowAll(String customerName, String customerNumber,
            String fromdate, String todate, String checkNumber,
            double checkAmount, String invoiceNo, double invAmt, String parentno) throws Exception {
        NumberFormat number = new DecimalFormat("0.00");
        List<TransactionBean> genericBean = new ArrayList<TransactionBean>();
        String m1 = "";
        String d1 = "";
        String y1 = "";
        String m2 = "";
        String d2 = "";
        String y2 = "";
        String sdate = fromdate;
        String edate = todate;
        StringTokenizer str = new StringTokenizer(sdate, "/");
        if (str.hasMoreTokens()) {
            m1 = str.nextToken();
            d1 = str.nextToken();
            y1 = str.nextToken();
        }
        sdate = y1 + "-" + m1 + "-" + d1;

        StringTokenizer str1 = new StringTokenizer(edate, "/");
        if (str1.hasMoreTokens()) {
            m2 = str1.nextToken();
            d2 = str1.nextToken();
            y2 = str1.nextToken();
        }
        edate = y2 + "-" + m2 + "-" + d2;
        TransactionBean transactionBean = null;
        String QueryString = "";

        if ((fromdate == null || fromdate.equals("")) && (invoiceNo == null || invoiceNo.equals("")) && (checkAmount == 0)) {

            QueryString = "select  tx.custName,tx.custNo,tx.transactionDate,tx.billLaddingNo,tx.transactionAmt,ca.hold,tx.balance,tx.transactionType,tx.blTerms,tx.subHouseBl,tx.masterBl,tx.voyageNo,tx.vesselNo,tx.containerNo,tx.transactionId from Transaction tx, CustAddress ca where tx.custName like '" + customerName + "%' and tx.custNo like'" + customerNumber + "%' and ca.acctNo like '" + customerNumber + "%' and tx.transactionType='AR'";
        } else if (invoiceNo == null || invoiceNo.equals("")) {

            QueryString = "select  tx.custName,tx.custNo,tx.transactionDate,tx.billLaddingNo,tx.transactionAmt,ca.hold,tx.balance,tx.transactionType,tx.blTerms,tx.subHouseBl,tx.masterBl,tx.voyageNo,tx.vesselNo,tx.containerNo,tx.transactionId from Transaction tx,CustAddress ca where tx.transactionDate Between'" + sdate + "'and '" + edate + "'and tx.custNo='" + customerNumber + "' and tx.custName='" + customerName + "'and ca.acctNo ='" + customerNumber + "'  and tx.transactionType='AR'";
        } else if (checkAmount == 0) {

            QueryString = "select  tx.custName,tx.custNo,tx.transactionDate,tx.billLaddingNo,tx.transactionAmt,ca.hold,tx.balance,tx.transactionType,tx.blTerms,tx.subHouseBl,tx.masterBl,tx.voyageNo,tx.vesselNo,tx.containerNo,tx.transactionId from Transaction tx,CustAddress ca where tx.billLaddingNo='" + invoiceNo + "'and tx.transactionDate Between'" + sdate + "'and '" + edate + "' and tx.custNo='" + customerNumber + "' and tx.custName='" + customerName + "'and ca.acctNo ='" + customerNumber + "' and tx.transactionType='AR' ";
        } else {

            QueryString = "select tx.custName,tx.custNo,tx.transactionDate,tx.billLaddingNo,tx.transactionAmt,ca.hold,tx.balance,tx.transactionType,tx.blTerms,tx.subHouseBl,tx.masterBl,tx.voyageNo,tx.vesselNo,tx.containerNo,tx.transactionId from Transaction tx,CustAddress ca where tx.transactionAmt='" + checkAmount + "'and tx.transactionDate Between'" + sdate + "'and '" + edate + "' and tx.custNo='" + customerNumber + "' and tx.custName='" + customerName + "' and ca.acctNo ='" + customerNumber + "' and tx.transactionType='AR' ";
        }

        // QueryString ="select
        // tx.custName,tx.custNo,tx.transactionDate,tx.billLaddingNo,tx.transactionAmt,ca.hold,tx.transactionType,tx.blTerms,tx.subHouseBl,tx.masterBl,tx.voyageNo,tx.vesselNo,tx.containerNo,tx.transactionId
        // from Transaction tx, CustAddress ca where tx.custName like
        // '"+customerName+"%' and tx.custNo like'"+customerNumber+"%' and
        // ca.acctNo like '"+customerNumber+"%'";
        List queryObject = getCurrentSession().createQuery(QueryString).list();

        Iterator itr = queryObject.iterator();
        while (itr.hasNext()) {

            transactionBean = new TransactionBean();
            Object[] row = (Object[]) itr.next();
            String custName = (String) (row[0]);
            String custNo = (String) (row[1]);
            Date invoiceDate = (Date) (row[2]);
            String invoceorBL = (String) (row[3]);
            Double amt = (Double) (row[4]);
            String creditHold = (String) (row[5]);
            Double balance = (Double) row[6];
            String recordtype = (String) (row[7]);
            String blterms = (String) (row[8]);
            String subhouse = (String) (row[9]);
            String master = (String) (row[10]);
            String voyage = (String) (row[11]);
            String vessel = (String) (row[12]);
            String container = (String) (row[13]);
            String transactionId = row[14].toString();
            Date today = new Date();

            // this is to conver from date to string format
            String invDate = sdf.format(invoiceDate);
            // to set caledartime

            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(invoiceDate);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(today);

            long age = printOutput("Calendar ", invoiceDate, today,
                    daysBetween(cal1, cal2));

            transactionBean.setCustomer(custName);
            transactionBean.setCustomerNo(custNo);

            transactionBean.setInvoiceDate(invDate);
            // this is to conver to String value to double

            transactionBean.setAmount(number.format(amt));
            transactionBean.setAge(age);
            transactionBean.setRecordType(recordtype);
            transactionBean.setInvoiceOrBl(invoceorBL);
            transactionBean.setBlterms(blterms);
            transactionBean.setHold(creditHold);
            transactionBean.setBalance(number.format(balance));
            transactionBean.setSubhouseBl(subhouse);
            transactionBean.setMasterbl(master);
            transactionBean.setVesselnumber(vessel);
            transactionBean.setVoyagenumber(voyage);
            transactionBean.setCotainernumber(container);
            transactionBean.setTransactionId(transactionId);
            genericBean.add(transactionBean);
            transactionBean = null;

        }

        // Added by pradeep

        if (!parentno.equals("") || parentno == null) {
            String QueryString1 = "";
            QueryString1 = "select  tx.custName,tx.custNo,tx.transactionDate,tx.billLaddingNo,tx.transactionAmt,ca.hold,tx.balance,tx.transactionType,tx.blTerms,tx.subHouseBl,tx.masterBl,tx.voyageNo,tx.vesselNo,tx.containerNo,tx.transactionId from Transaction tx, CustAddress ca where tx.custNo='" + parentno + "' and ca.acctNo='" + parentno + "'";

            // QueryString ="select
            // tx.custName,tx.custNo,tx.transactionDate,tx.billLaddingNo,tx.transactionAmt,ca.hold,tx.transactionType,tx.blTerms,tx.subHouseBl,tx.masterBl,tx.voyageNo,tx.vesselNo,tx.containerNo,tx.transactionId
            // from Transaction tx, CustAddress ca where tx.custName like
            // '"+customerName+"%' and tx.custNo like'"+customerNumber+"%'
            // and ca.acctNo like '"+customerNumber+"%'";
            List queryObject1 = getCurrentSession().createQuery(
                    QueryString1).list();

            Iterator itr1 = queryObject1.iterator();
            while (itr1.hasNext()) {
                transactionBean = new TransactionBean();
                Object[] row = (Object[]) itr1.next();
                String custName = (String) (row[0]);
                String custNo = (String) (row[1]);
                Date invoiceDate = (Date) (row[2]);
                String invoceorBL = (String) (row[3]);
                Double amt = (Double) (row[4]);
                String creditHold = (String) (row[5]);
                Double balance = (Double) row[6];

                String recordtype = (String) (row[7]);
                String blterms = (String) (row[8]);
                String subhouse = (String) (row[9]);
                String master = (String) (row[10]);
                String voyage = (String) (row[11]);
                String vessel = (String) (row[12]);
                String container = (String) (row[13]);
                String transactionId = row[14].toString();
                Date today = new Date();

                // this is to conver from date to string format
                String invDate = sdf.format(invoiceDate);
                // to set caledartime

                Calendar cal1 = Calendar.getInstance();
                cal1.setTime(invoiceDate);
                Calendar cal2 = Calendar.getInstance();
                cal2.setTime(today);

                long age = printOutput("Calendar ", invoiceDate, today,
                        daysBetween(cal1, cal2));

                transactionBean.setCustomer(custName);
                transactionBean.setCustomerNo(custNo);

                transactionBean.setInvoiceDate(invDate);
                // this is to conver to String value to double

                transactionBean.setAmount(number.format(amt));
                transactionBean.setAge(age);
                transactionBean.setRecordType(recordtype);
                transactionBean.setInvoiceOrBl(invoceorBL);
                transactionBean.setBlterms(blterms);
                transactionBean.setHold(creditHold);
                transactionBean.setBalance(number.format(balance));
                transactionBean.setSubhouseBl(subhouse);
                transactionBean.setMasterbl(master);
                transactionBean.setVesselnumber(vessel);
                transactionBean.setVoyagenumber(voyage);
                transactionBean.setCotainernumber(container);
                transactionBean.setTransactionId(transactionId);
                genericBean.add(transactionBean);
                transactionBean = null;

            }

        }

        return genericBean;
    }

    // using master to get child
    public List getChildacct(String acctNumber, String fromdate, String todate) throws Exception {
        List<TransactionBean> genericList = new ArrayList<TransactionBean>();
        TransactionBean transactionBean = null;
        NumberFormat number = new DecimalFormat("0.00");

        Iterator result = null;

        List resultList = new ArrayList();

        String m1 = "";
        String d1 = "";
        String y1 = "";
        String m2 = "";
        String d2 = "";
        String y2 = "";
        String sdate = fromdate;
        String edate = todate;

        StringTokenizer str = new StringTokenizer(sdate, "/");
        if (str.hasMoreTokens()) {
            m1 = str.nextToken();
            d1 = str.nextToken();
            y1 = str.nextToken();
        }
        sdate = y1 + "-" + m1 + "-" + d1;

        StringTokenizer str1 = new StringTokenizer(edate, "/");
        if (str1.hasMoreTokens()) {
            m2 = str1.nextToken();
            d2 = str1.nextToken();
            y2 = str1.nextToken();
        }
        edate = y2 + "-" + m2 + "-" + d2;

        result = getCurrentSession().createQuery(
                "select ca.acctNo from CustAddress ca where ca.master='" + acctNumber + "' ").list().iterator();

        while (result.hasNext()) {
            String name = (String) result.next();
            resultList.add(name);
        }
        int j = 0;
        int i = resultList.size();
        while (i > 0) {
            String childname = (String) resultList.get(j);
            String QueryString = "select  tx.custName,tx.custNo,tx.transactionDate,tx.billLaddingNo,tx.transactionAmt,ca.hold,tx.balance,tx.transactionType,tx.blTerms,tx.subHouseBl,tx.masterBl,tx.voyageNo,tx.vesselNo,tx.containerNo,tx.transactionId from Transaction tx, CustAddress ca where ca.acctNo='" + childname + "' and tx.custNo='" + childname + "'";

            List queryObejct = getCurrentSession().createQuery(QueryString).list();
            Iterator itr = queryObejct.iterator();
            while (itr.hasNext()) {
                transactionBean = new TransactionBean();
                Object[] row = (Object[]) itr.next();

                String custName = (String) (row[0]);

                String custNo = (String) (row[1]);
                Date invoiceDate = (Date) (row[2]);

                String invoceorBL = (String) (row[3]);

                Double amt = (Double) (row[4]);

                String creditHold = (String) (row[5]);

                Double balance = (Double) row[6];

                String recordtype = (String) (row[7]);

                String blterms = (String) (row[8]);

                String subhouse = (String) (row[9]);

                String master = (String) (row[10]);

                String voyage = (String) (row[11]);

                String vessel = (String) (row[12]);

                String container = (String) (row[13]);

                String transactionId = row[14].toString();

                Date today = new Date();

                // this is to conver from date to string format
                String invDate = sdf.format(invoiceDate);
                // to set caledartime

                Calendar cal1 = Calendar.getInstance();
                cal1.setTime(invoiceDate);
                Calendar cal2 = Calendar.getInstance();
                cal2.setTime(today);

                long age = printOutput("Calendar ", invoiceDate, today,
                        daysBetween(cal1, cal2));

                transactionBean.setCustomer(custName);

                transactionBean.setCustomerNo(custNo);
                transactionBean.setInvoiceDate(invDate);
                transactionBean.setAmount(number.format(amt));
                transactionBean.setAge(age);
                transactionBean.setRecordType(recordtype);
                transactionBean.setInvoiceOrBl(invoceorBL);
                transactionBean.setBlterms(blterms);
                transactionBean.setHold(creditHold);
                transactionBean.setBalance(number.format(balance));
                transactionBean.setSubhouseBl(subhouse);
                transactionBean.setMasterbl(master);
                transactionBean.setVesselnumber(vessel);
                transactionBean.setVoyagenumber(voyage);
                transactionBean.setCotainernumber(container);
                transactionBean.setTransactionId(transactionId);
                genericList.add(transactionBean);
                transactionBean = null;
            }

            j++;
            i--;
        }

        return genericList;
    }

    public List findVendorList(VendorDetailsDTO vendorDetailsDTO, User loginuser) throws Exception {
        List<TransactionBean> transactionBeanList = new ArrayList<TransactionBean>();
        List<Transaction> resultList = new ArrayList<Transaction>();
        String startDate = null;
        boolean flag = false;
        String endDate = null;
        StringBuffer queryString = null;
        if (vendorDetailsDTO.getFromDate() != null && !vendorDetailsDTO.getFromDate().equals("")) {
            startDate = vendorDetailsDTO.getFromDate();
        }
        if (vendorDetailsDTO.getToDate() != null && !vendorDetailsDTO.getToDate().equals("")) {
            endDate = vendorDetailsDTO.getToDate();
        }
        queryString = new StringBuffer("from Transaction transaction where transaction.transactionType='" + TRANSACTION_TYPE_ACCOUNT_PAYABLE + "' and transaction.balance!=0 and transaction.custNo like '%" + vendorDetailsDTO.getVendorNumber() + "%'");
        // for showing Holded Transaction
        if (null != vendorDetailsDTO.getShowHold() && vendorDetailsDTO.getShowHold().trim().equals("Yes")) {
            queryString.append(" and (transaction.status = '" + STATUS_HOLD + "' or transaction.status = '" + STATUS_OPEN + "')");
        } else {
            queryString.append(" and transaction.status = '" + STATUS_OPEN + "'");
        }

        // for show only AR's
        if (vendorDetailsDTO.getShowOnlyAR() != null && vendorDetailsDTO.getShowOnlyAR().equals("Yes")) {
            queryString = new StringBuffer("from Transaction transaction where ");
            queryString.append("((transaction.transactionType='" + TRANSACTION_TYPE_ACCOUNT_PAYABLE + "'  and transaction.balance!=0");
            if (null != vendorDetailsDTO.getShowHold() && vendorDetailsDTO.getShowHold().trim().equals("Yes")) {
                queryString.append(" and (transaction.status = '" + STATUS_HOLD + "' or transaction.status = '" + STATUS_OPEN + "'))");
            } else {
                queryString.append(" and transaction.status = '" + STATUS_OPEN + "')");
            }
            queryString.append(" or (transaction.transactionType='" + TRANSACTION_TYPE_ACCOUNT_RECEIVABLE + "' and transaction.balance<>0 and (transaction.status!='" + STATUS_READY_TO_PAY + "'"
                    + " or transaction.status is null)))");
            if (vendorDetailsDTO.getVendorNumber() != null && !vendorDetailsDTO.getVendorNumber().equals("")) {
                queryString.append(" and transaction.custNo='").append(vendorDetailsDTO.getVendorNumber()).append("'");
            }
        }

        // for show only My AP Accounts
        if (vendorDetailsDTO.getShowMyAP() != null && vendorDetailsDTO.getShowMyAP().equalsIgnoreCase("Yes")) {
            queryString = new StringBuffer("from Transaction transaction,Vendor vendor where transaction.custNo=vendor.accountno and vendor.apSpecialist=" + loginuser.getUserId() + " and transaction.transactionType='" + TRANSACTION_TYPE_ACCOUNT_PAYABLE + "'");
            queryString.append(" and transaction.status = '" + STATUS_OPEN + "'");
            if (vendorDetailsDTO.getVendorNumber() != null && !vendorDetailsDTO.getVendorNumber().equals("")) {
                queryString.append(" and transaction.custNo='").append(vendorDetailsDTO.getVendorNumber()).append("'");
            }
            flag = true;
        }

        // for show only My AP Entries
        if (vendorDetailsDTO.getShowOnlyMyAPEntries() != null && vendorDetailsDTO.getShowOnlyMyAPEntries().equalsIgnoreCase("Yes")) {
            queryString = new StringBuffer("from Transaction transaction where " + "transaction.createdBy=" + loginuser.getUserId() + " and transaction.transactionType='" + TRANSACTION_TYPE_ACCOUNT_PAYABLE + "'");
            queryString.append(" and transaction.status = '" + STATUS_OPEN + "')");
        }

        // for invoice Number
        if (vendorDetailsDTO.getInvoiceNumber() != null && !vendorDetailsDTO.getInvoiceNumber().equals("")) {
            queryString.append(" and transaction.invoiceNumber='" + vendorDetailsDTO.getInvoiceNumber() + "'");
        }

        // for invoice Amount
        if (vendorDetailsDTO.getInvoiceAmount() != null && !vendorDetailsDTO.getInvoiceAmount().equals("")) {
            queryString.append(" and transaction.transactionAmt" + vendorDetailsDTO.getCheckType() + vendorDetailsDTO.getInvoiceAmount());
        }
        // for voyage
        if (vendorDetailsDTO.getVoyage() != null && !vendorDetailsDTO.getVoyage().equals("")) {
            queryString.append(" and transaction.voyageNo='" + vendorDetailsDTO.getVoyage() + "'");
        }
        // for BilOfLadding
        if (vendorDetailsDTO.getBillOfLadding() != null && !vendorDetailsDTO.getBillOfLadding().equals("")) {
            queryString.append(" and transaction.billLaddingNo='" + vendorDetailsDTO.getBillOfLadding() + "'");
        }


        // for transaction Date
        if (startDate != null && endDate != null) {
            queryString.append(" and transaction.transactionDate between ?0 and ?1 ");
        }
        queryString.append(" order by transaction.custNo");
        Query query = getCurrentSession().createQuery(queryString.toString());
        query.setMaxResults(1000);
        if (startDate != null && !startDate.equals("")) {
            query.setParameter("0", sdf.parse(startDate), DateType.INSTANCE);
        }
        if (endDate != null && !endDate.equals("")) {
            query.setParameter("1", sdf.parse(endDate), DateType.INSTANCE);
        }
        resultList = query.list();
        if (flag) {
            for (Iterator iterator = resultList.iterator(); iterator.hasNext();) {
                Object[] transObj = (Object[]) iterator.next();
                Transaction transaction = null;
                transaction = (Transaction) transObj[0];
                TransactionBean transactionBean = null;
                transactionBean = new TransactionBean(transaction);
                if (null != transaction.getCreditTerms() && transaction.getCreditTerms().toString().trim().equals("")) {
                    String creditTerms = getCreditTerm(transaction.getCustNo(), transaction.getCreditTerms());
                    transactionBean.setCreditTerms(creditTerms);
                }
                if (null != transaction.getTransactionDate() && !transaction.getTransactionDate().toString().trim().equals("")) {
                    String invoiceDate = sdf.format(transaction.getTransactionDate());
                    String dueDate = calculateDueDate(transaction.getCustNo(), invoiceDate);
                    transactionBean.setDuedate(dueDate);
                    long age = calculateAge(transaction.getTransactionDate());
                    transactionBean.setAge(age);
                }
                if (null != transactionBean.getRecordType() && !transactionBean.getRecordType().trim().equals(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE)
                        && null != transactionBean.getStatus() && transaction.getStatus().trim().equals(STATUS_HOLD)) {
                    transactionBean.setHold(ON);
                }
                transactionBeanList.add(transactionBean);
            }
        } else {
            for (Iterator iterator = resultList.iterator(); iterator.hasNext();) {
                Transaction transaction = (Transaction) iterator.next();
                TransactionBean transactionBean = null;
                transactionBean = new TransactionBean(transaction);
                if (null != transaction.getCreditTerms() && !transaction.getCreditTerms().toString().trim().equals("")) {
                    String creditTerms = getCreditTerm(transaction.getCustNo(), transaction.getCreditTerms());
                    transactionBean.setCreditTerms(creditTerms);
                }
                if (null != transaction.getTransactionDate() && !transaction.getTransactionDate().toString().trim().equals("")) {
                    String invoiceDate = sdf.format(transaction.getTransactionDate());
                    String dueDate = calculateDueDate(transaction.getCustNo(), invoiceDate);
                    transactionBean.setDuedate(dueDate);
                    long age = calculateAge(transaction.getTransactionDate());
                    transactionBean.setAge(age);
                }
                if (null != transactionBean.getRecordType() && !transactionBean.getRecordType().trim().equals(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE)
                        && null != transactionBean.getStatus() && transaction.getStatus().trim().equals(STATUS_HOLD)) {
                    transactionBean.setHold(ON);
                }
                transactionBeanList.add(transactionBean);
            }
        }
        return transactionBeanList;
    }

    // added by chandu
    public List findvendorlist(String customerNumber, String datefrom,
            String dateto, String invoicenumber, String invoiceamount) throws Exception {
        String record = "AP";
        NumberFormat number = new DecimalFormat("0.00");
        String m1 = "";
        String d1 = "";
        String y1 = "";
        String m2 = "";
        String d2 = "";
        String y2 = "";
        String sdate = datefrom;
        String edate = dateto;

        StringTokenizer str = new StringTokenizer(sdate, "/");
        if (str.hasMoreTokens()) {
            m1 = str.nextToken();
            d1 = str.nextToken();
            y1 = str.nextToken();
        }
        sdate = y1 + "-" + m1 + "-" + d1;

        StringTokenizer str1 = new StringTokenizer(edate, "/");
        if (str1.hasMoreTokens()) {
            m2 = str1.nextToken();
            d2 = str1.nextToken();
            y2 = str1.nextToken();
        }
        edate = y2 + "-" + m2 + "-" + d2;
        List<TransactionBean> genericBean = new ArrayList<TransactionBean>();
        CustAddressDAO custAddressDAO = null;
        String QueryString = "";
        TransactionBean transactionBean = null;
        if ((datefrom.equals("")) && (dateto.equals("")) && invoicenumber.equals("") && invoiceamount.equals("")) {
            QueryString = "select  tx.custName,tx.custNo,tx.transactionDate,tx.billLaddingNo,tx.transactionType,tx.transactionAmt,tx.dueDate,tx.transactionId,tx.invoiceNumber from Transaction tx where tx.transactionType = '" + record + "' and tx.custNo like'" + customerNumber + "%'and tx.status='Open'"; // tx.custNo
            // like'"+customerNumber+"%'
            // and ca.acctNo like
            // '"+customerNumber+"%' and
            // tx.transactionType='"+record+"'";
        } else if (((datefrom != "") && (dateto != "")) && ((invoicenumber.equals("")) && ((invoiceamount.equals(""))))) {
            QueryString = "select  tx.custName,tx.custNo,tx.transactionDate,tx.billLaddingNo,tx.transactionType,tx.transactionAmt,tx.dueDate,tx.transactionId,tx.invoiceNumber from Transaction tx where tx.transactionType = '" + record + "' and tx.custNo like'" + customerNumber + "%' and tx.status='Open' and tx.transactionDate between '" + sdate + "%' and '" + edate + "%' ";// tx.transactionDate
            // Between'"+sdate+"'and
            // '"+edate+"'and
            // tx.custNo='"+customerNumber+"'
            // and ca.acctNo
            // ='"+customerNumber+"'
            // and
            // tx.transactionType='"+record+"'
            // ";
        } else if (((invoicenumber != "")) && (((datefrom.equals(""))) && ((dateto.equals(""))) && ((invoiceamount.equals(""))))) {
            QueryString = "select  tx.custName,tx.custNo,tx.transactionDate,tx.billLaddingNo,tx.transactionType,tx.transactionAmt,tx.dueDate,tx.transactionId,tx.invoiceNumber from Transaction tx where tx.transactionType = '" + record + "' and tx.custNo like'" + customerNumber + "%' and tx.invoiceNumber='" + invoicenumber + "' and tx.status='Open'"; // and
            // tx.custNo='"+customerNumber+"'
            // and ca.acctNo
            // ='"+customerNumber+"' and
            // tx.transactionType='"+record+"'
            // ";
        } else if ((invoiceamount != "") && ((datefrom.equals(""))) && ((dateto.equals(""))) && ((invoicenumber.equals("")))) {
            QueryString = "select  tx.custName,tx.custNo,tx.transactionDate,tx.billLaddingNo,tx.transactionType,tx.transactionAmt,tx.dueDate,tx.transactionId,tx.invoiceNumber from Transaction tx where tx.transactionType = '" + record + "' and tx.custNo like'" + customerNumber + "%' and tx.transactionAmt='" + invoiceamount + "' and tx.status='Open'"; // and
            // tx.custNo='"+customerNumber+"'
            // and ca.acctNo
            // ='"+customerNumber+"' and
            // tx.transactionType='"+record+"'
            // ";
        }

        List queryObject = getCurrentSession().createQuery(QueryString).list();
        Iterator itr = queryObject.iterator();
        while (itr.hasNext()) {

            transactionBean = new TransactionBean();
            custAddressDAO = new CustAddressDAO();
            Object[] row = (Object[]) itr.next();
            String custName = (String) (row[0]);
            String custNo = (String) (row[1]);
            Date invoiceDate = (Date) (row[2]);
            String billofLad = (String) (row[3]);
            String recordtype = (String) (row[4]);
            Double amt = (Double) (row[5]);
            // String creditHold = (String)(row[6]);
            Date duedate = (Date) row[6];
            String Duedate = "";
            if (duedate == null) {
                Duedate = "0";

            } else {
                Duedate = sdf.format(duedate);
            }

            String transactionId = row[7].toString();
            Date today = new Date();
            String invoiceNumber = (String) (row[8]);
            // this is to conver from date to string format
            String invDate = sdf.format(invoiceDate);
            // to set caledartime

            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(invoiceDate);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(today);

            long age = printOutput("Calendar ", invoiceDate, today,
                    daysBetween(cal1, cal2));

            transactionBean.setCustomer(custName);
            transactionBean.setCustomerNo(custNo);
            transactionBean.setInvoiceDate(invDate);

            transactionBean.setInvoiceOrBl(invoiceNumber);
            // transactionBean.setInvoiceOrBl(billofLad);
            List creditHold = custAddressDAO.getHold(custNo);
            String creditHold1 = "";
            String acctType = "";
            for (int i = 0; i < creditHold.size(); i++) {
                creditHold1 = (String) row[0];
                acctType = (String) row[1];
            }
            if (creditHold1 != null) {
                transactionBean.setHold(creditHold1);
            } else {
                transactionBean.setHold("");
            }
            if (acctType != null) {
                transactionBean.setAcctType(acctType);
            } else {
                transactionBean.setAcctType("");

            }
            transactionBean.setAge(age);
            transactionBean.setAmount(number.format(amt));
            transactionBean.setRecordType(recordtype);
            // transactionBean.setHold(creditHold);
            transactionBean.setDuedate(Duedate);
            transactionBean.setTransactionId(transactionId);

            genericBean.add(transactionBean);
            transactionBean = null;

        }
        return genericBean;
    }

    // added by chandu
    public List findAP(String customerNumber, String value) throws Exception {
        NumberFormat number = new DecimalFormat("0.00");
        List<TransactionBean> genericBean = new ArrayList<TransactionBean>();
        String QueryString = "";
        TransactionBean transactionBean = null;
        QueryString = "select  tx.custName,tx.custNo,tx.transactionDate,tx.billLaddingNo,tx.transactionType,tx.transactionAmt,ca.hold,tx.dueDate,tx.transactionId from Transaction tx,CustAddress ca where tx.custNo like'" + customerNumber + "%' and tx.transactionType='" + value + "' and  ca.acctNo like'" + customerNumber + "%'";

        // QueryString ="select
        // tx.custName,tx.custNo,tx.transactionDate,tx.billLaddingNo,tx.transactionAmt,ca.hold,tx.transactionType,tx.blTerms,tx.subHouseBl,tx.masterBl,tx.voyageNo,tx.vesselNo,tx.containerNo,tx.transactionId
        // from Transaction tx, CustAddress ca where tx.custName like
        // '"+customerName+"%' and tx.custNo like'"+customerNumber+"%' and
        // ca.acctNo like '"+customerNumber+"%'";
        List queryObject = getCurrentSession().createQuery(QueryString).list();

        Iterator itr = queryObject.iterator();
        while (itr.hasNext()) {

            transactionBean = new TransactionBean();
            Object[] row = (Object[]) itr.next();
            String custName = (String) (row[0]);
            String custNo = (String) (row[1]);
            Date invoiceDate = (Date) (row[2]);
            String billofLad = (String) (row[3]);
            String recordtype = (String) (row[4]);
            Double amt = (Double) (row[5]);
            String creditHold = (String) (row[6]);
            Date duedate = (Date) row[7];
            String Duedate = "";
            if (duedate == null) {
                Duedate = "0";
            } else {
                Duedate = sdf.format(duedate);
            }
            transactionBean.setDuedate(Duedate);
            String transactionId = row[8].toString();
            Date today = new Date();

            // this is to conver from date to string format
            String invDate = sdf.format(invoiceDate);
            // to set caledartime

            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(invoiceDate);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(today);

            long age = printOutput("Calendar ", invoiceDate, today,
                    daysBetween(cal1, cal2));

            transactionBean.setCustomer(custName);
            transactionBean.setCustomerNo(custNo);
            transactionBean.setInvoiceDate(invDate);
            transactionBean.setInvoiceOrBl(billofLad);
            transactionBean.setAge(age);
            transactionBean.setAmount(number.format(amt));
            transactionBean.setRecordType(recordtype);
            transactionBean.setHold(creditHold);

            transactionBean.setTransactionId(transactionId);
            genericBean.add(transactionBean);
            transactionBean = null;

        }
        return genericBean;
    }

    // to find AR
    public List findAR(String customerNumber, String value) throws Exception {
        NumberFormat number = new DecimalFormat("0.00");
        List<TransactionBean> genericBean = new ArrayList<TransactionBean>();
        String QueryString = "";
        TransactionBean transactionBean = null;
        QueryString = "select  tx.custName,tx.custNo,tx.transactionDate,tx.billLaddingNo,tx.transactionType,tx.transactionAmt,ca.hold,tx.dueDate,tx.transactionId from Transaction tx,CustAddress ca where tx.custNo like'" + customerNumber + "%' and tx.transactionType='" + value + "' and  ca.acctNo like'" + customerNumber + "%'";

        // QueryString ="select
        // tx.custName,tx.custNo,tx.transactionDate,tx.billLaddingNo,tx.transactionAmt,ca.hold,tx.transactionType,tx.blTerms,tx.subHouseBl,tx.masterBl,tx.voyageNo,tx.vesselNo,tx.containerNo,tx.transactionId
        // from Transaction tx, CustAddress ca where tx.custName like
        // '"+customerName+"%' and tx.custNo like'"+customerNumber+"%' and
        // ca.acctNo like '"+customerNumber+"%'";
        List queryObject = getCurrentSession().createQuery(QueryString).list();

        Iterator itr = queryObject.iterator();
        while (itr.hasNext()) {

            transactionBean = new TransactionBean();
            Object[] row = (Object[]) itr.next();
            String custName = (String) (row[0]);
            String custNo = (String) (row[1]);
            Date invoiceDate = (Date) (row[2]);
            String billofLad = (String) (row[3]);

            String recordtype = (String) (row[4]);
            Double amt = (Double) (row[5]);
            String creditHold = (String) (row[6]);

            Date duedate = (Date) row[7];

            String Duedate = "";
            if (duedate == null) {
                Duedate = "0";

            } else {
                Duedate = sdf.format(duedate);
            }
            transactionBean.setDuedate(Duedate);
            String transactionId = row[8].toString();
            Date today = new Date();

            // this is to conver from date to string format
            String invDate = sdf.format(invoiceDate);
            // to set caledartime

            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(invoiceDate);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(today);

            long age = printOutput("Calendar ", invoiceDate, today,
                    daysBetween(cal1, cal2));

            transactionBean.setCustomer(custName);
            transactionBean.setCustomerNo(custNo);
            transactionBean.setInvoiceDate(invDate);
            transactionBean.setInvoiceOrBl(billofLad);
            transactionBean.setAge(age);
            transactionBean.setAmount(number.format(amt));
            transactionBean.setRecordType(recordtype);
            transactionBean.setHold(creditHold);

            transactionBean.setTransactionId(transactionId);
            genericBean.add(transactionBean);
            transactionBean = null;

        }
        return genericBean;
    }

    // for string return;
    public List findforparentchild(String customerNumber, String datefrom,
            String dateto, String invoicenumber, String invoiceamount,
            String parentno) throws Exception {
        NumberFormat number = new DecimalFormat("0.00");
        List<TransactionBean> genericBean = new ArrayList<TransactionBean>();
        String record = "AP";
        TransactionBean transactionBean = null;
        Iterator result = null;
        List resultList = new ArrayList();
        String master = "";
        String QueryString = "";
        result = getCurrentSession().createQuery(
                "select ca.master from CustAddress ca where ca.acctNo='" + customerNumber + "' ").list().iterator();

        while (result.hasNext()) {
            master = (String) result.next();
            resultList.add(master);
        }
        // Added by chandu

        if (!master.equals("") || master == null) {
            String QueryString1 = "";

            QueryString1 = "select  tx.custName,tx.custNo,tx.transactionDate,tx.billLaddingNo,tx.transactionType,tx.transactionAmt,ca.hold,tx.dueDate,tx.transactionId from Transaction tx, CustAddress ca where   tx.custNo like'" + master + "%' and ca.acctNo like '" + master + "%'  and tx.transactionType='" + record + "'";

            List queryObject1 = getCurrentSession().createQuery(
                    QueryString1).list();

            Iterator itr1 = queryObject1.iterator();

            while (itr1.hasNext()) {

                transactionBean = new TransactionBean();
                Object[] row = (Object[]) itr1.next();
                String custName = (String) (row[0]);

                String custNo = (String) (row[1]);

                Date invoiceDate = (Date) (row[2]);

                String billofLad = (String) (row[3]);

                String recordtype = (String) (row[4]);
                Double amt = (Double) (row[5]);

                String creditHold = (String) (row[6]);

                Date duedate = (Date) row[7];
                String Duedate = "";
                if (duedate == null) {
                    Duedate = "0";

                } else {
                    Duedate = sdf.format(duedate);
                }
                transactionBean.setDuedate(Duedate);
                String transactionId = row[8].toString();
                Date today = new Date();

                // this is to conver from date to string format
                String invDate = sdf.format(invoiceDate);
                // to set caledartime

                Calendar cal1 = Calendar.getInstance();
                cal1.setTime(invoiceDate);
                Calendar cal2 = Calendar.getInstance();
                cal2.setTime(today);

                long age = printOutput("Calendar", invoiceDate, today,
                        daysBetween(cal1, cal2));

                transactionBean.setCustomer(custName);
                transactionBean.setCustomerNo(custNo);
                transactionBean.setInvoiceDate(invDate);
                transactionBean.setInvoiceOrBl(billofLad);
                transactionBean.setAge(age);
                transactionBean.setAmount(number.format(amt));
                transactionBean.setRecordType(recordtype);
                transactionBean.setHold(creditHold);

                transactionBean.setTransactionId(transactionId);
                genericBean.add(transactionBean);
                transactionBean = null;

            }
        }
        String childname = "";

        result = getCurrentSession().createQuery(
                "select ca.acctNo from CustAddress ca where ca.master='" + master + "' ").list().iterator();

        while (result.hasNext()) {
            childname = (String) result.next();
            resultList.add(childname);
        }
        if (!childname.equals("") || childname == null) {
            String QueryString2 = "";

            QueryString2 = "select  tx.custName,tx.custNo,tx.transactionDate,tx.billLaddingNo,tx.transactionType,tx.transactionAmt,ca.hold,tx.dueDate,tx.transactionId from Transaction tx, CustAddress ca where   tx.custNo like'" + childname + "%' and ca.acctNo like '" + childname + "%'  and tx.transactionType='" + record + "'";

            List queryObject2 = getCurrentSession().createQuery(
                    QueryString2).list();

            Iterator itr2 = queryObject2.iterator();

            while (itr2.hasNext()) {

                transactionBean = new TransactionBean();
                Object[] row = (Object[]) itr2.next();
                String custName = (String) (row[0]);

                String custNo = (String) (row[1]);

                Date invoiceDate = (Date) (row[2]);

                String billofLad = (String) (row[3]);

                String recordtype = (String) (row[4]);
                Double amt = (Double) (row[5]);

                String creditHold = (String) (row[6]);

                Date duedate = (Date) row[7];
                String transactionId = row[8].toString();
                Date today = new Date();

                // this is to conver from date to string format
                String invDate = sdf.format(invoiceDate);
                String Duedate = "";
                if (duedate == null) {
                    Duedate = "0";

                } else {
                    Duedate = sdf.format(duedate);
                }
                // to set caledartime

                Calendar cal1 = Calendar.getInstance();
                cal1.setTime(invoiceDate);
                Calendar cal2 = Calendar.getInstance();
                cal2.setTime(today);

                long age = printOutput("Calendar", invoiceDate, today,
                        daysBetween(cal1, cal2));

                transactionBean.setCustomer(custName);
                transactionBean.setCustomerNo(custNo);
                transactionBean.setInvoiceDate(invDate);
                transactionBean.setInvoiceOrBl(billofLad);
                transactionBean.setAge(age);
                transactionBean.setAmount(number.format(amt));
                transactionBean.setRecordType(recordtype);
                transactionBean.setHold(creditHold);
                transactionBean.setDuedate(Duedate);
                transactionBean.setTransactionId(transactionId);
                genericBean.add(transactionBean);
                transactionBean = null;

            }
        }

        return genericBean;
    }

    public void updatestatus(String transid, String status) throws Exception {
        String queryString = "UPDATE Transaction tx set tx.status='" + status + "' where tx.transactionId='" + transid + "'";
        int query = getCurrentSession().createQuery(queryString).executeUpdate();
    }

    public List getTranactionsForCustomerwithBalance(String customerno) throws Exception {
        TransactionBean transbean = null;
        List<TransactionBean> transList = new ArrayList<TransactionBean>();
        String querystring = "";
        querystring = "select tx.custName,tx.custNo,tx.invoiceNumber,tx.transactionAmt,tx.transactionDate,tx.transactionId,tx.balance from Transaction tx where tx.custNo='" + customerno + "' and tx.transactionType='AR' and tx.balance>0";

        List queryObject = getCurrentSession().createQuery(querystring).list();
        Iterator iter = queryObject.iterator();

        NumberFormat number = new DecimalFormat("0.00");

        while (iter.hasNext()) {
            transbean = new TransactionBean();
            Object[] row = (Object[]) iter.next();
            String customer = (String) row[0];
            String customerNo = (String) row[1];
            String invoice = row[2].toString();
            double Transamount = (Double) row[3];
            Date tdate = (Date) row[4];
            String transDate = sdf.format(tdate);
            String transactionId = row[5].toString();
            Double balance = (Double) row[6];
            transbean.setCustomer(customer);
            transbean.setCustomerNo(customerNo);
            transbean.setInvoiceOrBl(invoice);
            transbean.setAmount(number.format(Transamount));
            transbean.setInvoiceDate(transDate);
            transbean.setTransactionId(transactionId);
            transbean.setBalance(number.format(balance));
            transList.add(transbean);
            transbean = null;

        }

        return transList;
    }

    public List findByInvoiceNumberOrCheckNumberOrRefernceNumber(String invoiceNumber, String checkNumber, String referenceNumber) throws Exception {
        List result = new ArrayList();
        invoiceNumber = null != invoiceNumber ? invoiceNumber : "";
        checkNumber = null != checkNumber ? checkNumber : "";
        referenceNumber = null != referenceNumber ? referenceNumber : "";
        StringBuffer queryString = new StringBuffer();
        queryString.append("");
        queryString.append("select distinct tx.custNo from Transaction tx,CustomerAddress ca where (");
        queryString.append("tx.invoiceNumber like '%" + invoiceNumber + "%'");
        queryString.append(" and tx.chequeNumber like '%" + checkNumber + "%'");
        queryString.append(" and tx.customerReferenceNo like '%" + referenceNumber + "%'");

        /*		if(null!=invoiceNumber && !invoiceNumber.trim().equals("")){
        queryString.append("tx.invoiceNumber like '%");
        queryString.append(invoiceNumber+"%'");
        if(null!=checkNumber && !checkNumber.trim().equals("")){
        queryString.append(" and ");
        queryString.append(" tx.chequeNumber like '%");
        queryString.append(checkNumber+"%'");
        }
        if(null!=referenceNumber && !referenceNumber.trim().equals("")){
        queryString.append(" and ");
        queryString.append(" tx.customerReferenceNo like '%");
        queryString.append(referenceNumber+"%'");

        }
        }else if(null!=checkNumber && !checkNumber.trim().equals("")){
        queryString.append(" tx.chequeNumber like '%");
        queryString.append(checkNumber+"%'");
        if(null!=referenceNumber && !referenceNumber.trim().equals("")){
        queryString.append(" and ");
        queryString.append(" tx.customerReferenceNo like '%");
        queryString.append(referenceNumber+"%'");

        }
        }else if(null!=referenceNumber && !referenceNumber.trim().equals("")){
        queryString.append(" tx.customerReferenceNo like '%");
        queryString.append(referenceNumber+"%'");
        }
         */ queryString.append(")");
        queryString.append(" and tx.transactionType='");
        queryString.append(TRANSACTION_TYPE_ACCOUNT_PAYABLE);
        queryString.append("' and tx.custNo=ca.accountNo order by tx.custNo");
        result = getCurrentSession().createQuery(queryString.toString()).list();
        return result;
    }

    public List getTranactionsForCustomerwithBalanceForAP(String customerno) throws Exception {
        TransactionBean transbean = null;
        List<TransactionBean> transList = new ArrayList<TransactionBean>();
        String querystring = "";
        querystring = "select tx.custName,tx.custNo,tx.invoiceNumber,tx.transactionAmt,tx.transactionDate,tx.transactionId,tx.balance,tx.dueDate,tx.chequeNumber from Transaction tx where tx.custNo='" + customerno + "' and tx.transactionType='AP' and tx.balance>0";

        List queryObject = getCurrentSession().createQuery(querystring).list();
        Iterator iter = queryObject.iterator();

        NumberFormat number = new DecimalFormat("0.00");

        while (iter.hasNext()) {
            transbean = new TransactionBean();
            Object[] row = (Object[]) iter.next();
            String customer = (String) row[0];
            String customerNo = (String) row[1];
            String invoice = row[2].toString();
            double Transamount = 0.00;
            if (row[3] != null) {
                Transamount = (Double) row[3];
            }
            Date tdate = (Date) row[4];
            String transDate = sdf.format(tdate);
            String transactionId = row[5].toString();
            Double balance = (Double) row[6];
            Date dueDate = (Date) row[7];
            String chequeNumber = (String) row[8];
            String duedate = "";
            if (dueDate != null) {
                duedate = sdf.format(dueDate);
            }
            transbean.setCustomer(customer);
            transbean.setCustomerNo(customerNo);
            transbean.setInvoiceOrBl(invoice);
            transbean.setAmount(number.format(Transamount));
            transbean.setInvoiceDate(transDate);
            transbean.setTransactionId(transactionId);
            transbean.setBalance(number.format(balance));
            transbean.setDuedate(duedate);
            transbean.setChequenumber(chequeNumber);
            transList.add(transbean);
            transbean = null;

        }

        return transList;
    }

    public List showAccruals(String vendorNO) throws Exception {
        TransactionBean transbean = null;
        List<TransactionBean> transList = new ArrayList<TransactionBean>();
        String querystring = "";
        querystring = "select tx.custName,tx.custNo,tx.invoiceNumber,tx.transactionAmt,tx.transactionDate,tx.chequeNumber,tx.Duedate from Transaction tx where tx.transactionType='AC' and tx.custNo='" + vendorNO + "'";
        List queryObject = getCurrentSession().createQuery(querystring).list();
        Iterator iter = queryObject.iterator();

        NumberFormat number = new DecimalFormat("0.00");
        while (iter.hasNext()) {
            transbean = new TransactionBean();
            // custAddressDAO = new CustAddressDAO();
            Object[] row = (Object[]) iter.next();
            String custName = (String) (row[0]);
            String custNo = (String) (row[1]);
            String invoiceNumber = (String) (row[2]);
            Double transactionAmt = (Double) (row[3]);
            Date transactionDate = (Date) (row[4]);
            String chequeNo = (String) (row[5]);
            Date dueDate = (Date) (row[6]);

            transbean.setCustomer(custName);
            transbean.setCustomerNo(custNo);
            transbean.setAmount(number.format(transactionAmt));
            String tDate = sdf.format(transactionDate);
            transbean.setInvoiceDate(tDate);
            // int i = invoiceNumber.intValue();
            transbean.setInvoiceOrBl(invoiceNumber);
            // String hold = custAddressDAO.getHold(custNo);
			/*
             * if(hold != null) { transbean.setHold(hold); } else {
             * transactionBean.setHold(""); }
             */

            if (chequeNo == null) {
                transbean.setChequenumber("");
            } else {
                // int c = chequeNo.intValue();
                transbean.setChequenumber(chequeNo);
            }
            if (dueDate == null) {
                transbean.setDuedate("");
            } else {
                String dDate = sdf.format(dueDate);
                transbean.setDuedate(dDate);
            }
            transList.add(transbean);
            transbean = null;

        }

        return transList;

    }

    // AllCollector list for customer Statement report
    public List getAllCollectorStatementList(customerStatementVO statementVO,
            String allcollectors) throws Exception {
        String ageingzeero = statementVO.getAgingzeero();
        String ageingthirty = statementVO.getAgingthirty();
        String greaterthanthirty = statementVO.getGreaterthanthirty();
        String agingsixty = statementVO.getAgingsixty();
        String greaterthansixty = statementVO.getGreaterthansixty();
        String agingninty = statementVO.getAgingninty();
        String greaterthanninty = statementVO.getGreaterthanninty();
        String overdue = statementVO.getOverdue();
        String minamt = statementVO.getMinamt();
        List<AgingReportPeriodDTO> CollectorStatementList = new ArrayList<AgingReportPeriodDTO>();
        NumberFormat number = new DecimalFormat("###,###,##0.00");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String arContactCode = statementVO.getCollector();
        // String allcollectors=statementVO.getAllcollectors();
        // String onlyAgent=statementVO.getOnlyAgents();
        // String agentsInclude=statementVO.getAgentsInclude();
        // String agentsNotInclude=statementVO.getAgentsNotInclude();
        AgingReportPeriodDTO agingReportPeriodDTO = new AgingReportPeriodDTO();
        Date today = new Date();
        String queryString = "";
        if (allcollectors != null && allcollectors.equals("yes")) {
            queryString = "select distinct ca.accountNo from CustomerAccounting ca order by ca.accountNo asc";
        } else {
            queryString = "select distinct ca.accountNo from CustomerAccounting ca where ca.arcode.id='" + arContactCode + "' order by ca.accountNo asc";
        }
        List queryObject = getCurrentSession().createQuery(queryString).list();
        int count = 0;
        int size = queryObject.size();
        while (size > 0) {
            Double total = 0.00;
            Double totalone = 0.00;
            Double totaltwo = 0.00;
            Double totalthree = 0.00;
            Double totalfour = 0.00;
            String custNo = (String) queryObject.get(count);
            if (custNo != null && !custNo.equals("")) {
                String queryString2 = "select cust.address1 from CustAddress cust where cust.acctNo='" + custNo + "' limit 1";
                String result = (String) getCurrentSession().createQuery(
                        queryString2).uniqueResult().toString();
                String custAddress = null != result ? result : "";
                String queryString1 = "select trans.transactionDate,trans.invoiceNumber,trans.transactionAmt,trans.balance,trans.consName,trans.billLaddingNo,trans.transactionId,trans.customerReferenceNo,trans.custName from Transaction trans where trans.custNo='" + custNo + "' and transactionType='" + ReportConstants.TRANSACTIONTYPE + "'";
                List queryObject1 = getCurrentSession().createQuery(
                        queryString1).list();
                Iterator iter1 = queryObject1.iterator();
                while (iter1.hasNext()) {
                    agingReportPeriodDTO = new AgingReportPeriodDTO();
                    Object[] row1 = (Object[]) iter1.next();
                    Date transactionDate = (Date) row1[0];
                    String invoiceNumber = (String) row1[1];
                    Double transactionAmt = (Double) row1[2];
                    Double balance = (Double) row1[3];
                    String consName = (String) row1[4];
                    String transactionNo = (String) row1[5];
                    Integer transactionId = (Integer) row1[6];
                    String custrefno = (String) row1[7];
                    String custName = (String) row1[8];
                    if (custName == null) {
                        custName = "";
                    }
                    if (custNo == null) {
                        custNo = "";
                    }
                    if (transactionAmt == null) {
                        transactionAmt = 0.0;
                    }
                    String transAmt = (String.valueOf(transactionAmt));
                    if (transAmt.contains(",")) {
                        transAmt = transAmt.replace(",", "");
                    }
                    transactionAmt = (Double.parseDouble(transAmt));
                    if (balance == null) {
                        balance = 0.0;
                    }
                    String bal = (String.valueOf(balance));
                    if (bal.contains(",")) {
                        bal = bal.replace(",", "");
                    }
                    balance = (Double.parseDouble(bal));
                    if (transactionId != null) {
                        String queryString3 = "select sum(pay.paymentAmt) from Payments pay where pay.arTranactionId='" + transactionId + "'";
                        List queryObject3 = getCurrentSession().createQuery(
                                queryString3).list();
                        Double payamt = (Double) queryObject3.get(0);
                        if (payamt == null) {
                            payamt = 0.0;
                        }
                        String payment = (String.valueOf(payamt));
                        if (payment.contains(",")) {
                            payment = payment.replace(",", "");
                        }
                        payamt = (Double.parseDouble(payment));
                        agingReportPeriodDTO.setPaymentoradjustment(number.format(payamt));
                    }
                    if (custrefno == null) {
                        custrefno = "";
                    }
                    String aging = "";
                    long range = 0;
                    if (null != transactionDate) {
                        range = this.calculateAge(transactionDate);
                        aging = String.valueOf(range);
                    }

                    int OverDue = (Integer.parseInt(overdue));
                    Double MinAmt = (Double.parseDouble(minamt));
                    if (balance != null && balance > 0.0) {
                        if (OverDue <= range && MinAmt <= balance) {
                            if (Integer.parseInt(ageingzeero) <= range && Integer.parseInt(ageingthirty) >= range) {
                                totalone = totalone + balance;
                                // agReportPeriodDTO.setAgerangeone(number.format(balance));
                            } else if (Integer.parseInt(greaterthanthirty) <= range && Integer.parseInt(agingsixty) >= range) {
                                totaltwo = totaltwo + balance;
                                // agReportPeriodDTO.setAgerangetwo(number.format(balance));
                            } else if (Integer.parseInt(greaterthansixty) <= range && Integer.parseInt(agingninty) >= range) {
                                totalthree = totalthree + balance;
                                // agReportPeriodDTO.setAgerangethree(number.format(balance));
                            } else {
                                totalfour = totalfour + balance;
                                // agReportPeriodDTO.setAgerangefour(number.format(balance));
                            }
                            Double paymentAdjustment = transactionAmt - balance;
                            String patAdj = (String.valueOf(paymentAdjustment));
                            if (patAdj.equals("0.0")) {
                                patAdj = "";
                            } else {
                                patAdj = number.format(paymentAdjustment);
                            }
                            total = totalone + totaltwo + totalthree + totalfour;
                            agingReportPeriodDTO.setDate(simpleDateFormat.format(transactionDate));
                            agingReportPeriodDTO.setCustName(custName);
                            agingReportPeriodDTO.setCustNo(custNo);
                            agingReportPeriodDTO.setCustAddress(custAddress);
                            agingReportPeriodDTO.setInvoiceNo(invoiceNumber);
                            agingReportPeriodDTO.setInvoiceamt(number.format(transactionAmt));
                            agingReportPeriodDTO.setPaymentoradjustment(patAdj);
                            agingReportPeriodDTO.setInvoicebalance(number.format(balance));
                            agingReportPeriodDTO.setAgent(aging);
                            agingReportPeriodDTO.setConsigneename(consName);
                            agingReportPeriodDTO.setVoyageno(transactionNo);
                            agingReportPeriodDTO.setCustRefer(custrefno);
                            CollectorStatementList.add(agingReportPeriodDTO);
                            agingReportPeriodDTO = null;
                        }
                    }
                }
                // this is for setting line to the report
                Double totalbal = totalone + totaltwo + totalthree + totalfour;
                String totOne = (String.valueOf(totalone));
                if (totOne.equals("0.0")) {
                    totOne = "";
                } else {
                    totOne = number.format(totalone);
                }
                String totTwo = (String.valueOf(totaltwo));
                if (totTwo.equals("0.0")) {
                    totTwo = "";
                } else {
                    totTwo = number.format(totaltwo);
                }
                String totThree = (String.valueOf(totalthree));
                if (totThree.equals("0.0")) {
                    totThree = "";
                } else {
                    totThree = number.format(totalthree);
                }
                String totFour = (String.valueOf(totalfour));
                if (totFour.equals("0.0")) {
                    totFour = "";
                } else {
                    totFour = number.format(totalfour);
                }
                AgingReportPeriodDTO totalagReportPeriodDTO = null;
                totalagReportPeriodDTO = new AgingReportPeriodDTO();
                totalagReportPeriodDTO.setCustNo(custNo);
                totalagReportPeriodDTO.setInvoiceNo("");
                totalagReportPeriodDTO.setDate("");
                totalagReportPeriodDTO.setInvoiceamt("");
                totalagReportPeriodDTO.setTotal(number.format(total));
                totalagReportPeriodDTO.setPaymentoradjustment("");
                totalagReportPeriodDTO.setInvoicebalance("");
                totalagReportPeriodDTO.setAgent("");
                totalagReportPeriodDTO.setAgerangeone(totOne);
                totalagReportPeriodDTO.setAgerangetwo(totTwo);
                totalagReportPeriodDTO.setAgerangethree(totThree);
                totalagReportPeriodDTO.setAgerangefour(totFour);
                CollectorStatementList.add(totalagReportPeriodDTO);
                totalagReportPeriodDTO = null;
            }
            count++;
            size--;

        }
        return CollectorStatementList;
    }

    public List ForAgingPeriodReport(String custName, String ageingzeero,
            String ageingthirty, String greaterthanthirty, String agingsixty,
            String greaterthansixty, String agingninty, String greaterthanninty) throws Exception {
        AgingReportPeriodDTO agReportPeriodDTO = null;
        List<AgingReportPeriodDTO> agingReportList = new ArrayList<AgingReportPeriodDTO>();
        Date today = new Date();
        Double totalone = 0.00;
        Double totaltwo = 0.00;
        Double totalthree = 0.00;
        Double totalfour = 0.00;
        String queryString = "select trans.invoiceNumber,trans.sailingDate,trans.balance from Transaction trans where trans.custName='" + custName + "'";
        // String queryString = "select
        // seg.segmentCode,seg.segmentDesc,seg.Segment_leng from Segment seg
        // where seg.Account_structure_Id='"+acct+"'";
        List queryObject = getCurrentSession().createQuery(queryString).list();
        Iterator iter = queryObject.iterator();

        while (iter.hasNext()) {
            agReportPeriodDTO = new AgingReportPeriodDTO();
            Object[] row = (Object[]) iter.next();
            String invoiceNumber = (String) (row[0]);
            Date sailindDate = (Date) row[1];
            Double balance = (Double) row[2];
            if (balance == null) {
                balance = 0.0;
            }
            String sailingdate = sdf.format(sailindDate);
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(sailindDate);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(today);
            long inDate = printOutput("Calendar", sailindDate, today,
                    daysBetween(cal1, cal2));
            String aging = (String.valueOf(inDate));

            Integer range = (Integer.parseInt(aging));

            if (Integer.parseInt(ageingzeero) <= range && Integer.parseInt(ageingthirty) > range) {

                totalone = totalone + balance;
                agReportPeriodDTO.setAgerangeone(String.valueOf(number.format(balance)));
            } else if (Integer.parseInt(greaterthanthirty) <= range && Integer.parseInt(agingsixty) > range) {
                totaltwo = totaltwo + balance;
                agReportPeriodDTO.setAgerangetwo(String.valueOf(number.format(balance)));
            } else if (Integer.parseInt(greaterthansixty) <= range && Integer.parseInt(agingninty) > range) {
                totalthree = totalthree + balance;
                agReportPeriodDTO.setAgerangethree(String.valueOf(number.format(balance)));
            } else {
                totalfour = totalfour + balance;
                agReportPeriodDTO.setAgerangefour(String.valueOf(number.format(balance)));
            }

            agReportPeriodDTO.setInvoiceNo(invoiceNumber);
            agReportPeriodDTO.setInvoiceDate(sailingdate);
            agReportPeriodDTO.setBalance(String.valueOf(number.format(balance)));
            agingReportList.add(agReportPeriodDTO);
            agReportPeriodDTO = null;
        }
        // This is for Line Breaking
        AgingReportPeriodDTO emptyagReportPeriodDTO = null;
        emptyagReportPeriodDTO = new AgingReportPeriodDTO();
        emptyagReportPeriodDTO.setInvoiceNo("");
        emptyagReportPeriodDTO.setInvoiceDate("");
        emptyagReportPeriodDTO.setAgerangeone("___________");
        emptyagReportPeriodDTO.setAgerangetwo("___________");
        emptyagReportPeriodDTO.setAgerangethree("___________");
        emptyagReportPeriodDTO.setAgerangefour("___________");
        agingReportList.add(emptyagReportPeriodDTO);
        emptyagReportPeriodDTO = null;
        // This is for setting values in to the Report
        AgingReportPeriodDTO totalagReportPeriodDTO = null;
        totalagReportPeriodDTO = new AgingReportPeriodDTO();
        totalagReportPeriodDTO.setInvoiceNo("");
        totalagReportPeriodDTO.setInvoiceDate("");
        totalagReportPeriodDTO.setBalance("TOTAL");
        totalagReportPeriodDTO.setAgerangeone(String.valueOf(number.format(totalone)));
        totalagReportPeriodDTO.setAgerangetwo(String.valueOf(number.format(totaltwo)));
        totalagReportPeriodDTO.setAgerangethree(String.valueOf(number.format(totalthree)));
        totalagReportPeriodDTO.setAgerangefour(String.valueOf(number.format(totalfour)));
        agingReportList.add(totalagReportPeriodDTO);
        totalagReportPeriodDTO = null;
        return agingReportList;
    }

    public List ForAgingSearchList(String custName) throws Exception {
        Date today = new Date();
        List<TransactionBean> transList = new ArrayList<TransactionBean>();
        TransactionBean transbean = null;
        String queryString = "select trans.invoiceNumber,trans.balance,trans.transactionDate,trans.transactionAmt,trans.custName,trans.custNo,trans.transactionId from Transaction trans where trans.custName='" + custName + "'";
        List queryObject = getCurrentSession().createQuery(queryString).list();
        Iterator iter = queryObject.iterator();
        while (iter.hasNext()) {
            transbean = new TransactionBean();
            Object[] row = (Object[]) iter.next();
            String invoiceNumber = (String) (row[0]);
            Double balance = (Double) row[1];
            Date date = (Date) row[2];
            Double transAmt = (Double) row[3];
            String customerName = (String) (row[4]);
            String custNumber = (String) (row[5]);
            Integer transactionId = (Integer) row[6];
            if (balance == null) {
                balance = 0.0;
            }
            String bal = (String.valueOf(balance));
            if (bal.contains(",")) {
                bal = bal.replace(",", "");
            }
            balance = (Double.parseDouble(bal));
            if (transAmt == null) {
                transAmt = 0.0;
            }
            String transactionAmt = (String.valueOf(transAmt));
            if (transactionAmt.contains(",")) {
                transactionAmt = transactionAmt.replace(",", "");
            }
            transAmt = (Double.parseDouble(transactionAmt));
            String invoiceDate = "";
            String aging = "";
            if (date != null && !date.equals("")) {
                invoiceDate = sdf.format(date);
                Calendar cal1 = Calendar.getInstance();
                cal1.setTime(date);
                Calendar cal2 = Calendar.getInstance();
                cal2.setTime(today);
                long inDate = printOutput("Calendar", date, today,
                        daysBetween(cal1, cal2));
                aging = (String.valueOf(inDate));
            }

            transbean.setTransactionId(String.valueOf(transactionId));
            transbean.setCustomer(customerName);
            transbean.setCustomerNo(custNumber);
            transbean.setInvoiceOrBl(invoiceNumber);
            transbean.setBalance(String.valueOf(number.format(balance)));
            transbean.setAging(aging);
            transbean.setInvoiceDate(invoiceDate);
            transbean.setAmount(String.valueOf(number.format(transAmt)));
            transList.add(transbean);
            transbean = null;
        }
        return transList;
    }

    public List ForAgingPeriodReport1(String custName, String ageingzeero,
            String ageingthirty, String greaterthanthirty, String agingsixty,
            String greaterthansixty, String agingninty, String greaterthanninty) throws Exception {
        AgingReportPeriodDTO agReportPeriodDTO = null;
        List<AgingReportPeriodDTO> agingReportList = new ArrayList<AgingReportPeriodDTO>();
        Date today = new Date();
        Double totalone = 0.00;
        Double totaltwo = 0.00;
        Double totalthree = 0.00;
        Double totalfour = 0.00;
        String queryString = "select trans.invoiceNumber,trans.sailingDate,trans.balance,trans.billLaddingNo,trans.customerReferenceNo,trans.custNo,ca.address1 from Transaction trans, CustAddress ca where trans.custName='" + custName + "' and ca.acctName='" + custName + "'";
        List queryObject = getCurrentSession().createQuery(queryString).list();
        Iterator iter = queryObject.iterator();
        while (iter.hasNext()) {
            agReportPeriodDTO = new AgingReportPeriodDTO();
            Object[] row = (Object[]) iter.next();
            String invoiceNumber = (String) row[0];
            Date sailingDate = (Date) row[1];
            Double balance = (Double) row[2];
            String blNumber = (String) row[3];
            String custRefNo = (String) row[4];
            String custNumber = (String) row[5];
            String cusAddress = (String) row[6];
            String sailingdate = sdf.format(sailingDate);
            if (custRefNo == null) {
                custRefNo = "";
            }
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(sailingDate);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(today);
            long inDate = printOutput("Calendar", sailingDate, today,
                    daysBetween(cal1, cal2));
            String aging = (String.valueOf(inDate));
            Integer range = (Integer.parseInt(aging));
            if (Integer.parseInt(ageingzeero) <= range && Integer.parseInt(ageingthirty) > range) {

                totalone = totalone + balance;
                agReportPeriodDTO.setAgerangeone(String.valueOf(number.format(balance)));
            } else if (Integer.parseInt(greaterthanthirty) <= range && Integer.parseInt(agingsixty) > range) {
                totaltwo = totaltwo + balance;
                agReportPeriodDTO.setAgerangetwo(String.valueOf(number.format(balance)));
            } else if (Integer.parseInt(greaterthansixty) <= range && Integer.parseInt(agingninty) > range) {
                totalthree = totalthree + balance;
                agReportPeriodDTO.setAgerangethree(String.valueOf(number.format(balance)));
            } else {
                totalfour = totalfour + balance;
                agReportPeriodDTO.setAgerangefour(String.valueOf(number.format(balance)));
            }
            agReportPeriodDTO.setInvoiceNo(invoiceNumber);
            agReportPeriodDTO.setInvoiceDate(sailingdate);
            agReportPeriodDTO.setBalance(String.valueOf(number.format(balance)));
            agReportPeriodDTO.setCustAddress(cusAddress);
            agReportPeriodDTO.setCustNo(custNumber);
            agReportPeriodDTO.setInvoiceNo(blNumber);
            agReportPeriodDTO.setCustRefer(custRefNo);
            agingReportList.add(agReportPeriodDTO);
            agReportPeriodDTO = null;
        }
        // This is for Line Breaking
        AgingReportPeriodDTO emptyagReportPeriodDTO = null;
        emptyagReportPeriodDTO = new AgingReportPeriodDTO();
        emptyagReportPeriodDTO.setInvoiceNo("");
        emptyagReportPeriodDTO.setInvoiceDate("");
        emptyagReportPeriodDTO.setAgerangeone("___________");
        emptyagReportPeriodDTO.setAgerangetwo("___________");
        emptyagReportPeriodDTO.setAgerangethree("___________");
        emptyagReportPeriodDTO.setAgerangefour("___________");
        /*
         * agingReportList.add(emptyagReportPeriodDTO);
         * emptyagReportPeriodDTO=null; //This is for setting values in to
         * the Report AgingReportPeriodDTO totalagReportPeriodDTO=null;
         * totalagReportPeriodDTO=new AgingReportPeriodDTO();
         */
        /*
         * totalagReportPeriodDTO.setInvoiceNo("");
         * totalagReportPeriodDTO.setInvoiceDate("");
         * totalagReportPeriodDTO.setBalance("TOTAL");
         */
        emptyagReportPeriodDTO.setAgerangeone(String.valueOf(number.format(totalone)));
        emptyagReportPeriodDTO.setAgerangetwo(String.valueOf(number.format(totaltwo)));
        emptyagReportPeriodDTO.setAgerangethree(String.valueOf(number.format(totalthree)));
        emptyagReportPeriodDTO.setAgerangefour(String.valueOf(number.format(totalfour)));
        agingReportList.add(emptyagReportPeriodDTO);
        emptyagReportPeriodDTO = null;
        return agingReportList;
    }

    public List ForOnlyAgentReport(String custName, String ageingzeero,
            String ageingthirty, String greaterthanthirty, String agingsixty,
            String greaterthansixty, String agingninty, String greaterthanninty) throws Exception {
        AgingReportPeriodDTO agReportPeriodDTO = null;
        List<AgingReportPeriodDTO> agingReportList = new ArrayList<AgingReportPeriodDTO>();
        Date today = new Date();
        String queryString1 = "select trans.transactionDate,trans.invoiceNumber,trans.transactionAmt,trans.balance,trans.consName,trans.voyageNo,trans.transactionId from Transaction trans where trans.custName='" + custName + "'";
        List queryObject1 = getCurrentSession().createQuery(queryString1).list();
        Iterator iter1 = queryObject1.iterator();
        int i = 0;
        int k = 0;
        int j = 0;
        Double totalone = 0.00;
        Double totaltwo = 0.00;
        Double totalthree = 0.00;
        Double totalfour = 0.00;
        Double tot = 0.00;
        while (iter1.hasNext()) {
            agReportPeriodDTO = new AgingReportPeriodDTO();
            Object[] row1 = (Object[]) iter1.next();
            Date salingDate = (Date) row1[0];
            String invoiceNumber = (String) row1[1];
            Double transactionAmt = (Double) row1[2];
            Double balance = (Double) row1[3];
            String consName = (String) row1[4];
            String voyageNo = (String) row1[5];
            Integer transactionId = (Integer) row1[6];
            if (transactionId != null) {
                String queryString3 = "select sum(pay.paymentAmt) from Payments pay where pay.arTranactionId='" + transactionId + "'";
                List queryObject3 = getCurrentSession().createQuery(
                        queryString3).list();
                Double payamt = (Double) queryObject3.get(0);
                String payamount = String.valueOf(payamt);
                if (payamount.equals("null")) {
                    payamount = "0.0";
                }
                agReportPeriodDTO.setPaymentoradjustment(number.format(Double.parseDouble(payamount)));
            }
            tot = tot + balance;
            String sailingdate = sdf.format(salingDate);
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(salingDate);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(today);
            long inDate = printOutput("Calendar", salingDate, today,
                    daysBetween(cal1, cal2));
            String aging = (String.valueOf(inDate));
            Integer range = (Integer.parseInt(aging));
            if (Integer.parseInt(ageingzeero) <= range && Integer.parseInt(ageingthirty) > range) {
                totalone = totalone + balance;
            } else if (Integer.parseInt(greaterthanthirty) <= range && Integer.parseInt(agingsixty) > range) {
                totaltwo = totaltwo + balance;
            } else if (Integer.parseInt(greaterthansixty) <= range && Integer.parseInt(agingninty) > range) {
                totalthree = totalthree + balance;
            } else {
                totalfour = totalfour + balance;
            }
            agReportPeriodDTO.setCurrent(number.format(totalone));
            agReportPeriodDTO.setThirtyone(number.format(totaltwo));
            agReportPeriodDTO.setSixtyone(number.format(totalthree));
            agReportPeriodDTO.setNintyone(number.format(totalfour));
            Double Total = totalone + totaltwo + totalthree + totalfour;
            agReportPeriodDTO.setTotal(number.format(Total));
            agReportPeriodDTO.setDate(sdf.format(salingDate));
            agReportPeriodDTO.setInvoiceNo(invoiceNumber);
            agReportPeriodDTO.setInvoiceamt(number.format(transactionAmt));
            agReportPeriodDTO.setInvoicebalance(number.format(balance));
            agReportPeriodDTO.setConsigneename(consName);
            agReportPeriodDTO.setVoyageno(voyageNo);
            agingReportList.add(agReportPeriodDTO);
            agReportPeriodDTO = null;
        }
        return agingReportList;
    }

    // Wire Transfer List for report
    public List getWireTransfer(customerStatementVO statementVO,
            String excludeFromStatement) throws Exception {
        String custNo = statementVO.getCustomerNumber();
        List<AgingReportPeriodDTO> wireTransferList = new ArrayList<AgingReportPeriodDTO>();
        AgingReportPeriodDTO agingReportPeriodDTO = null;
        String queryString = "select ca.acctName,bd.bankAddress,ca.city1,ca.state,ca.zip,bd.bankName,bd.bankAcctNo,bd.aba,bd.swiftCode from CustAddress ca,BankDetails bd where ca.acctNo=bd.acctNo and bd.wireTransferFlag='y'";
        List queryObject = getCurrentSession().createQuery(queryString).list();
        Iterator iter = queryObject.iterator();
        while (iter.hasNext()) {
            agingReportPeriodDTO = new AgingReportPeriodDTO();
            Object[] row = (Object[]) iter.next();
            String acctName = (String) row[0];
            String bankAddress = (String) row[1];
            String city = (String) row[2];
            String state = (String) row[3];
            String zip = (String) row[4];
            String bankName = (String) row[5];
            String bankAcctNo = (String) row[6];
            String aba = (String) row[7];
            String swiftCode = (String) row[8];
            agingReportPeriodDTO.setCustName(null != acctName ? acctName
                    : "");
            agingReportPeriodDTO.setBankAddress(null != bankAddress ? bankAddress : "");
            agingReportPeriodDTO.setCity(null != city ? city : "");
            agingReportPeriodDTO.setState(null != state ? state : "");
            agingReportPeriodDTO.setZip(null != zip ? zip : "");
            agingReportPeriodDTO.setBankName(null != bankName ? bankName
                    : "");
            agingReportPeriodDTO.setBankAcctNo(null != bankAcctNo ? bankAcctNo : "");
            agingReportPeriodDTO.setAba(null != aba ? aba : "");
            agingReportPeriodDTO.setSwiftCode(null != swiftCode ? swiftCode
                    : "");
            wireTransferList.add(agingReportPeriodDTO);
            agingReportPeriodDTO = null;
        }
        return wireTransferList;
    }

    public void update(Transaction instance) throws Exception {
        getSession().saveOrUpdate(instance);
        getSession().flush();
    }

    // THIS IS FOR CUSTOMER STATEMENT PRINT IN AGING
    public List ForAgingPrint(customerStatementVO statementVO) throws Exception {
        String custName = statementVO.getCustomerName();
        String subject = statementVO.getSubject();
        String textBody = statementVO.getTextArea();
        String footerName = statementVO.getFooterName();
        AgingReportPeriodDTO agingReportPeriodDTO = null;
        List<AgingReportPeriodDTO> agingReportList = new ArrayList<AgingReportPeriodDTO>();
        Date today = new Date();
        String queryString = "select cust.acctNo,cust.acctName,cust.address1,cust.city1,cust.state,cust.phone,cust.fax,cust.contactName from CustAddress cust where cust.acctName='" + custName + "'";
        List queryObject = getCurrentSession().createQuery(queryString).list();
        Iterator iter = queryObject.iterator();

        if (iter.hasNext() && queryObject != null) {
            agingReportPeriodDTO = new AgingReportPeriodDTO();
            Object[] row = (Object[]) iter.next();
            String custnum = (String) row[0];
            String custname = (String) row[1];
            String custaddress = (String) row[2];
            String city = (String) row[3];
            String state = (String) row[4];
            String phone = (String) row[5];
            String fax = (String) row[6];
            String contactName = (String) row[7];
            agingReportPeriodDTO.setCustNo(custnum);
            agingReportPeriodDTO.setCustName(custname);
            agingReportPeriodDTO.setCustAddress(custaddress);
            agingReportPeriodDTO.setSubject(subject);
            agingReportPeriodDTO.setTextArea(textBody);
            agingReportPeriodDTO.setFooterName(footerName);
            agingReportPeriodDTO.setPhone(phone);
            agingReportPeriodDTO.setFax(fax);
            agingReportPeriodDTO.setCity(city);
            agingReportPeriodDTO.setContactName(contactName);
            agingReportList.add(agingReportPeriodDTO);
            agingReportPeriodDTO = null;
        }// if
        return agingReportList;
    }

    public List findByCustomerName(String customerName) throws Exception {
        String queryString = "from Transaction as model where custName=?0";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", customerName);
        return queryObject.list();
    }

    public List<TransactionBean> getTransactionsForCustomerNumber(
            String customerNumber) throws Exception {
        List<TransactionBean> transList = new ArrayList<TransactionBean>();
        List customerList = getCurrentSession().createQuery(
                "from Transaction transaction where transaction.custNo='" + customerNumber + "' and transaction.transactionType='AR' and transaction.billTo='Y' and transaction.balanceInProcess!=0.0 Order By field(transaction.invoiceNumber,'PRE PAYMENT','ON ACCOUNT'),transaction.transactionDate asc").list();
        int tempVar = 0;
        while (customerList != null && customerList.size() > tempVar) {
            Transaction transaction = null;
            TransactionBean transactionBean = null;
            transaction = (Transaction) customerList.get(tempVar);
            transactionBean = new TransactionBean(transaction);
            transList.add(transactionBean);
            tempVar++;
        }
        return transList;
    }

    public List findForARTransactions(String custNumber) throws Exception {
        List<TransactionBean> transList = new ArrayList<TransactionBean>();
        List customerList = getCurrentSession().createQuery(
                "from Transaction transaction where transaction.custNo='" + custNumber + "' and transaction.transactionType='AR'").list();
        int tempVar = 0;
        while (customerList != null && customerList.size() > tempVar) {
            Transaction transaction = null;
            TransactionBean transactionBean = null;
            transaction = (Transaction) customerList.get(tempVar);
            transactionBean = new TransactionBean(transaction);
            transList.add(transactionBean);
            tempVar++;
        }
        return transList;
    }

    public List findForAPTransactions(String custNumber) throws Exception {
        List<TransactionBean> transList = new ArrayList<TransactionBean>();
        List customerList = getCurrentSession().createQuery(
                "from Transaction transaction where transaction.custNo='" + custNumber + "' and transaction.transactionType='AP'").list();
        int tempVar = 0;
        while (customerList != null && customerList.size() > tempVar) {
            Transaction transaction = null;
            TransactionBean transactionBean = null;
            transaction = (Transaction) customerList.get(tempVar);
            transactionBean = new TransactionBean(transaction);
            transList.add(transactionBean);
            tempVar++;
        }
        return transList;
    }

    public List findForAPTransactionsWithStatusPay(String custNumber) throws Exception {
        List<TransactionBean> transList = new ArrayList<TransactionBean>();
        List customerList = getCurrentSession().createQuery(
                "from Transaction transaction where transaction.custNo='" + custNumber + "' and transaction.status='Pay'").list();
        int tempVar = 0;
        while (customerList != null && customerList.size() > tempVar) {
            Transaction transaction = null;
            TransactionBean transactionBean = null;
            transaction = (Transaction) customerList.get(tempVar);
            transactionBean = new TransactionBean(transaction);
            if (transaction.getStatus() != null) {
                transactionBean.setStatus(transaction.getStatus());
            }
            if (transaction.getTransactionAmt() != null) {
                transactionBean.setInvoiceAmount(transaction.getTransactionAmt().toString());
            }
            transList.add(transactionBean);
            tempVar++;
        }

        return transList;
    }

    public List findForACTransactions(String custNumber) throws Exception {
        List<TransactionBean> transList = new ArrayList<TransactionBean>();
        List customerList = getCurrentSession().createQuery(
                "from Transaction transaction where transaction.custNo='" + custNumber + "' and transaction.transactionType='AC'").list();
        int tempVar = 0;
        while (customerList != null && customerList.size() > tempVar) {
            Transaction transaction = null;
            TransactionBean transactionBean = null;
            transaction = (Transaction) customerList.get(tempVar);
            transactionBean = new TransactionBean(transaction);
            transList.add(transactionBean);
            tempVar++;
        }
        return transList;
    }

    public List findForACTransactionsForshowAccruals(String custNumber) throws Exception {
        List<TransactionBean> transList = new ArrayList<TransactionBean>();
        String queryString = "from TransactionLedger tl where tl.custNo='" + custNumber + "' and tl.transactionType='AC'"
                + " and (tl.status='" + STATUS_OPEN + "' or (tl.status = '" + STATUS_POSTED_TO_GL + "'"
                + " and concat(tl.custNo,tl.invoiceNumber,tl.transactionAmt) not in (select concat(pj.custNo,pj.invoiceNumber,pj.transactionAmt)"
                + " from TransactionLedger pj where pj.subledgerSourceCode='PJ' and pj.custNo=tl.custNo))) and tl.subledgerSourceCode='ACC'";
        List<TransactionLedger> accrualsList = getCurrentSession().createQuery(queryString).list();
        for (TransactionLedger transactionLedger : accrualsList) {
            TransactionBean transactionBean = new TransactionBean(transactionLedger);
            transList.add(transactionBean);
        }
        return transList;
    }

    /**
     * @param customerName
     * @return
     */
    public List getCustomers(String customerName, String customerNumber) throws Exception {
        List<TransactionBean> transList = new ArrayList<TransactionBean>();
        Query query = getCurrentSession().createQuery(
                "select tr.custNo from Transaction tr,Vendor vendor where tr.custNo=vendor.accountno and tr.custNo like?0 "
                + "and tr.custName like?1 and (tr.status=?2 or tr.status=?3) and ((tr.transactionType=?4) or (tr.transactionType=?5 and tr.balance!=0))" + " group by tr.custNo order by tr.custNo");
        query.setParameter("0", customerNumber.trim() + "%", StringType.INSTANCE);
        query.setParameter("1", customerName.trim() + "%", StringType.INSTANCE);
        query.setParameter("2", STATUS_READY_TO_PAY, StringType.INSTANCE);
        query.setParameter("3", STATUS_WAITING_FOR_APPROVAL, StringType.INSTANCE);
        query.setParameter("4", TRANSACTION_TYPE_ACCOUNT_PAYABLE, StringType.INSTANCE);
        query.setParameter("5", TRANSACTION_TYPE_ACCOUNT_RECEIVABLE, StringType.INSTANCE);
        List<String> resultList = query.list();
        for (String custNo : resultList) {
            TransactionBean transactionBean = null;
            if (custNo != null && !custNo.trim().equals("")) {
                Criteria criteria = getCurrentSession().createCriteria(Transaction.class);
                criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
                criteria.add(Restrictions.like("custNo", custNo));
                criteria.add(Restrictions.or(Restrictions.like("status", STATUS_READY_TO_PAY), Restrictions.like("status", STATUS_WAITING_FOR_APPROVAL)));
                criteria.add(Restrictions.or(Restrictions.like("transactionType", TRANSACTION_TYPE_ACCOUNT_PAYABLE),
                        Restrictions.and(Restrictions.like("transactionType", TRANSACTION_TYPE_ACCOUNT_RECEIVABLE),
                        Restrictions.ne("balance", new Double(0)))));
                List<Transaction> paymentList = criteria.list();
                String multiTransactionId = "";
                Double amount = 0d;
                String waitingForApprovalIdWithACH = "";
                Double waitingForApprovalAmountWithACH = 0d;
                String waitingForApprovalIdWithWire = "";
                Double waitingForApprovalAmountWithWire = 0d;
                Transaction transaction = null;
                for (Transaction tempTransaction : paymentList) {
                    if (tempTransaction.getTransactionType().equalsIgnoreCase(TRANSACTION_TYPE_ACCOUNT_PAYABLE)) {
                        if (tempTransaction.getStatus().equals(STATUS_READY_TO_PAY)) {
                            amount += tempTransaction.getBalance();
                        }
                        if (tempTransaction.getStatus().equals(STATUS_WAITING_FOR_APPROVAL)) {
                            if (null != tempTransaction.getPaymentMethod()) {
                                if (tempTransaction.getPaymentMethod().trim().equalsIgnoreCase(PAYMENT_METHOD_ACH)) {
                                    waitingForApprovalAmountWithACH += tempTransaction.getBalance();
                                } else {
                                    waitingForApprovalAmountWithWire += tempTransaction.getBalance();
                                }
                            }
                        }
                    } else if (tempTransaction.getTransactionType().equalsIgnoreCase(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE)) {
                        if (tempTransaction.getStatus().equals(STATUS_READY_TO_PAY)) {
                            amount -= tempTransaction.getBalance();
                        }
                        if (tempTransaction.getStatus().equals(STATUS_WAITING_FOR_APPROVAL)) {
                            if (null != tempTransaction.getPaymentMethod()) {
                                if (tempTransaction.getPaymentMethod().trim().equalsIgnoreCase(PAYMENT_METHOD_ACH)) {
                                    waitingForApprovalAmountWithACH -= tempTransaction.getBalance();
                                } else {
                                    waitingForApprovalAmountWithWire -= tempTransaction.getBalance();
                                }
                            }
                        }
                    }
                    if (tempTransaction.getStatus().equals(STATUS_READY_TO_PAY)) {
                        multiTransactionId += "" + tempTransaction.getTransactionId() + ",";
                    }
                    if (tempTransaction.getStatus().equals(STATUS_WAITING_FOR_APPROVAL)) {
                        if (null != tempTransaction.getPaymentMethod()) {
                            if (tempTransaction.getPaymentMethod().trim().equalsIgnoreCase(PAYMENT_METHOD_ACH)) {
                                waitingForApprovalIdWithACH += "" + tempTransaction.getTransactionId() + ",";
                            } else {
                                waitingForApprovalIdWithWire += "" + tempTransaction.getTransactionId() + ",";
                            }
                        }
                    }
                    transaction = tempTransaction;
                }
                if (multiTransactionId.endsWith(",")) {
                    multiTransactionId = multiTransactionId.substring(0, multiTransactionId.length() - 1);
                }
                if (waitingForApprovalIdWithACH.endsWith(",")) {
                    waitingForApprovalIdWithACH = waitingForApprovalIdWithACH.substring(0, waitingForApprovalIdWithACH.length() - 1);
                }
                if (waitingForApprovalIdWithWire.endsWith(",")) {
                    waitingForApprovalIdWithWire = waitingForApprovalIdWithWire.substring(0, waitingForApprovalIdWithWire.length() - 1);
                }
                if (transaction != null && null != multiTransactionId
                        && !multiTransactionId.trim().equals("") && amount != 0d) {
                    transactionBean = new TransactionBean(transaction);
                    transactionBean.setBalance(number.format(amount));
                    transactionBean.setTransactionId(multiTransactionId);
                    transactionBean.setStatus(STATUS_READY_TO_PAY);
                    Date date = new Date();
                    String transDate = sdf.format(date);
                    transactionBean.setTransDate(transDate);
                    transList.add(transactionBean);
                }
                if (null != transaction && null != waitingForApprovalIdWithACH
                        && !waitingForApprovalIdWithACH.trim().equals("") && waitingForApprovalAmountWithACH != 0d) {
                    // for ACH transactions waiting for Approval
                    transactionBean = new TransactionBean(transaction);
                    transactionBean.setBalance(number.format(waitingForApprovalAmountWithACH));
                    transactionBean.setTransactionId(waitingForApprovalIdWithACH);
                    transactionBean.setStatus(STATUS_WAITING_FOR_APPROVAL);
                    transactionBean.setPaymentMethod(PAYMENT_METHOD_ACH.toUpperCase());
                    Date date = new Date();
                    String transDate = sdf.format(date);
                    transactionBean.setTransDate(transDate);
                    transList.add(transactionBean);
                }
                if (null != transaction && null != waitingForApprovalIdWithWire
                        && !waitingForApprovalIdWithWire.trim().equals("") && waitingForApprovalAmountWithWire != 0d) {
                    // for WIRE transactions waiting for Approval
                    transactionBean = new TransactionBean(transaction);
                    transactionBean.setBalance(number.format(waitingForApprovalAmountWithWire));
                    transactionBean.setTransactionId(waitingForApprovalIdWithWire);
                    transactionBean.setStatus(STATUS_WAITING_FOR_APPROVAL);
                    transactionBean.setPaymentMethod(PAYMENT_METHOD_WIRE.toUpperCase());
                    Date date = new Date();
                    String transDate = sdf.format(date);
                    transactionBean.setTransDate(transDate);
                    transList.add(transactionBean);
                }
            }
        }
        return transList;
    }

    public List getCustomersForPayment(String customerName, String customerNumber) throws Exception {
        List<TransactionBean> transList = new ArrayList<TransactionBean>();
        Query query = getCurrentSession().createQuery(
                "select tr.custNo from Transaction tr,Vendor vendor where tr.custNo=vendor.accountno and tr.custNo like?0 "
                + "and tr.custName like?1 and tr.status=?2 and ((tr.transactionType=?3) or (tr.transactionType=?4 and tr.balance!=0))" + " group by tr.custNo");
        query.setParameter("0", customerNumber.trim() + "%", StringType.INSTANCE);
        query.setParameter("1", customerName.trim() + "%", StringType.INSTANCE);
        query.setParameter("2", STATUS_READY_TO_PAY, StringType.INSTANCE);
        query.setParameter("3", TRANSACTION_TYPE_ACCOUNT_PAYABLE, StringType.INSTANCE);
        query.setParameter("4", TRANSACTION_TYPE_ACCOUNT_RECEIVABLE, StringType.INSTANCE);
        List queryObject = query.list();
        NumberFormat number = new DecimalFormat("###,###,##0.00");
        for (Iterator iterator = queryObject.iterator(); iterator.hasNext();) {
            TransactionBean transactionBean = null;
            String custNo = (String) iterator.next();
            if (custNo != null) {
                Criteria criteria = getCurrentSession().createCriteria(
                        Transaction.class);
                criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
                criteria.add(Restrictions.like("custNo", custNo));
                criteria.add(Restrictions.like("status", STATUS_READY_TO_PAY));
                criteria.add(Restrictions.or(Restrictions.like("transactionType", TRANSACTION_TYPE_ACCOUNT_PAYABLE),
                        Restrictions.and(Restrictions.like("transactionType", TRANSACTION_TYPE_ACCOUNT_RECEIVABLE),
                        Restrictions.ne("balance", new Double(0)))));
                List invoiceList = criteria.list();
                String multiTransactionId = "";
                Double amount = 0d;
                Transaction transaction = null;
                for (Iterator iterator2 = invoiceList.iterator(); iterator2.hasNext();) {
                    transaction = (Transaction) iterator2.next();
                    if (transaction != null) {
                        transactionBean = new TransactionBean(transaction);
                        Date date = new Date();
                        String transDate = sdf.format(date);
                        transactionBean.setTransDate(transDate);
                        if (null != transaction.getTransactionType() && transaction.getTransactionType().trim().equals(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE)) {
                            transactionBean.setAmount("" + ((-1) * transaction.getBalance()));
                        }
                    }
                    transList.add(transactionBean);

                }

            }
        }
        return transList;
    }

    public List<TransactionBean> getTransactionListByCheckNumberAndVendorNumber(String chequeNumber, String vendorNumber, String batchNumber) throws Exception {
        List<TransactionBean> transactionBeanList = new ArrayList<TransactionBean>();
        TransactionBean transactionBean = null;
        StringBuffer queryString = new StringBuffer();
        queryString.append("from Transaction tr where tr.transactionType = '" + TRANSACTION_TYPE_PAYAMENT + "' and status='" + STATUS_PAID + "'");

        if (null != chequeNumber && !chequeNumber.trim().equals("")) {
            queryString.append(" and tr.chequeNumber= '" + chequeNumber + "'");
        }

        if (null != batchNumber && batchNumber.trim().equals("")) {
            queryString.append(" and tr.apBatchId = '" + batchNumber + "'");
        }
        if (null != vendorNumber && !vendorNumber.trim().equals("")) {
            queryString.append(" and tr.custNo = '" + vendorNumber + "'");
        }
        queryString.append(" and balance=0 and balanceInProcess=0 and transactionAmt!=0");
        queryString.append(" order by tr.apBatchId desc");
        Query queryObject = getCurrentSession().createQuery(
                queryString.toString());
        List<Transaction> queryResult = queryObject.list();
        for (Transaction transaction : queryResult) {
            transactionBean = new TransactionBean(transaction);
            transactionBeanList.add(transactionBean);
            transactionBean = null;
        }
        return transactionBeanList;
    }

    public List<TransactionBean> getTransactionListByTransacionIds(String TransactionIds) throws Exception {
        List<TransactionBean> transactionBeanList = new ArrayList<TransactionBean>();
        TransactionBean transactionBean = null;
        StringBuffer queryString = new StringBuffer();
        queryString.append("from Transaction tr where tr.transactionId in (" + TransactionIds + ")");
        queryString.append(" order by tr.apBatchId desc");
        Query queryObject = getCurrentSession().createQuery(
                queryString.toString());
        List<Transaction> queryResult = queryObject.list();
        for (Transaction transaction : queryResult) {
            transactionBean = new TransactionBean(transaction);
            transactionBeanList.add(transactionBean);
            transactionBean = null;
        }
        return transactionBeanList;
    }

    public List getCustomersByUserId(String customerName, String customerNumber, User loginUser) throws Exception {
        List<TransactionBean> transList = new ArrayList<TransactionBean>();
        Query query = getCurrentSession().createQuery("select tr.custNo from Transaction tr,Vendor vendor where tr.custNo=vendor.accountno and vendor.apSpecialist=?0 and tr.custNo like?1 " + "and tr.custName like?2 and (tr.status=?3 or tr.status=?4) and ((tr.transactionType=?5) or (tr.transactionType=?6 and tr.balance!=0))" + " group by tr.custNo order by tr.custNo");
        query.setParameter("0", loginUser.getUserId(), IntegerType.INSTANCE);
        query.setParameter("1", customerNumber.trim() + "%", StringType.INSTANCE);
        query.setParameter("2", customerName.trim() + "%", StringType.INSTANCE);
        query.setParameter("3", STATUS_READY_TO_PAY, StringType.INSTANCE);
        query.setParameter("4", STATUS_WAITING_FOR_APPROVAL, StringType.INSTANCE);
        query.setParameter("5", TRANSACTION_TYPE_ACCOUNT_PAYABLE, StringType.INSTANCE);
        query.setParameter("6", TRANSACTION_TYPE_ACCOUNT_RECEIVABLE, StringType.INSTANCE);
        List<String> resultList = query.list();
        for (String custNo : resultList) {
            TransactionBean transactionBean = null;
            if (custNo != null && !custNo.trim().equals("")) {
                Criteria criteria = getCurrentSession().createCriteria(Transaction.class);
                criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
                criteria.add(Restrictions.like("custNo", custNo));
                criteria.add(Restrictions.or(Restrictions.like("status", STATUS_READY_TO_PAY), Restrictions.like("status", STATUS_WAITING_FOR_APPROVAL)));
                criteria.add(Restrictions.or(Restrictions.like("transactionType", TRANSACTION_TYPE_ACCOUNT_PAYABLE),
                        Restrictions.and(Restrictions.like("transactionType", TRANSACTION_TYPE_ACCOUNT_RECEIVABLE),
                        Restrictions.ne("balance", new Double(0)))));
                criteria.setMaxResults(50);
                List<Transaction> paymentList = criteria.list();
                String multiTransactionId = "";
                Double amount = 0d;
                String waitingForApprovalIdWithACH = "";
                Double waitingForApprovalAmountWithACH = 0d;
                String waitingForApprovalIdWithWire = "";
                Double waitingForApprovalAmountWithWire = 0d;
                Transaction transaction = null;
                for (Transaction tempTransaction : paymentList) {
                    if (tempTransaction.getTransactionType().equalsIgnoreCase(TRANSACTION_TYPE_ACCOUNT_PAYABLE)) {
                        if (tempTransaction.getStatus().equals(STATUS_READY_TO_PAY)) {
                            amount += tempTransaction.getBalance();
                        }
                        if (tempTransaction.getStatus().equals(STATUS_WAITING_FOR_APPROVAL)) {
                            if (null != tempTransaction.getPaymentMethod()) {
                                if (tempTransaction.getPaymentMethod().trim().equalsIgnoreCase(PAYMENT_METHOD_ACH)) {
                                    waitingForApprovalAmountWithACH += tempTransaction.getBalance();
                                } else {
                                    waitingForApprovalAmountWithWire += tempTransaction.getBalance();
                                }
                            }
                        }
                    } else if (transaction.getTransactionType().equalsIgnoreCase(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE)) {
                        if (tempTransaction.getStatus().equals(STATUS_READY_TO_PAY)) {
                            amount -= tempTransaction.getBalance();
                        }
                        if (tempTransaction.getStatus().equals(STATUS_WAITING_FOR_APPROVAL)) {
                            if (null != tempTransaction.getPaymentMethod()) {
                                if (tempTransaction.getPaymentMethod().trim().equalsIgnoreCase(PAYMENT_METHOD_ACH)) {
                                    waitingForApprovalAmountWithACH -= tempTransaction.getBalance();
                                } else {
                                    waitingForApprovalAmountWithWire -= tempTransaction.getBalance();
                                }
                            }
                        }
                    }
                    if (tempTransaction.getStatus().equals(STATUS_READY_TO_PAY)) {
                        multiTransactionId += "" + tempTransaction.getTransactionId() + ",";
                    }
                    if (tempTransaction.getStatus().equals(STATUS_WAITING_FOR_APPROVAL)) {
                        if (null != tempTransaction.getPaymentMethod()) {
                            if (tempTransaction.getPaymentMethod().trim().equalsIgnoreCase(PAYMENT_METHOD_ACH)) {
                                waitingForApprovalIdWithACH += "" + tempTransaction.getTransactionId() + ",";
                            } else {
                                waitingForApprovalIdWithWire += "" + tempTransaction.getTransactionId() + ",";
                            }
                        }
                    }
                    transaction = tempTransaction;
                }
                if (multiTransactionId.endsWith(",")) {
                    multiTransactionId = multiTransactionId.substring(0, multiTransactionId.length() - 1);
                }
                if (waitingForApprovalIdWithACH.endsWith(",")) {
                    waitingForApprovalIdWithACH = waitingForApprovalIdWithACH.substring(0, waitingForApprovalIdWithACH.length() - 1);
                }
                if (waitingForApprovalIdWithWire.endsWith(",")) {
                    waitingForApprovalIdWithWire = waitingForApprovalIdWithWire.substring(0, waitingForApprovalIdWithWire.length() - 1);
                }
                if (transaction != null && null != multiTransactionId
                        && !multiTransactionId.trim().equals("") && amount != 0d) {
                    transactionBean = new TransactionBean(transaction);
                    transactionBean.setBalance(number.format(amount));
                    transactionBean.setTransactionId(multiTransactionId);
                    transactionBean.setStatus(STATUS_READY_TO_PAY);
                    Date date = new Date();
                    String transDate = sdf.format(date);
                    transactionBean.setTransDate(transDate);
                    transList.add(transactionBean);
                }
                if (null != transaction && null != waitingForApprovalIdWithACH
                        && !waitingForApprovalIdWithACH.trim().equals("") && waitingForApprovalAmountWithACH != 0d) {
                    // for ACH transactions waiting for Approval
                    transactionBean = new TransactionBean(transaction);
                    transactionBean.setBalance(number.format(waitingForApprovalAmountWithACH));
                    transactionBean.setTransactionId(waitingForApprovalIdWithACH);
                    transactionBean.setStatus(STATUS_WAITING_FOR_APPROVAL);
                    transactionBean.setPaymentMethod(PAYMENT_METHOD_ACH.toUpperCase());
                    Date date = new Date();
                    String transDate = sdf.format(date);
                    transactionBean.setTransDate(transDate);
                    transList.add(transactionBean);
                }
                if (null != transaction && null != waitingForApprovalIdWithWire
                        && !waitingForApprovalIdWithWire.trim().equals("") && waitingForApprovalAmountWithWire != 0d) {
                    // for WIRE transactions waiting for Approval
                    transactionBean = new TransactionBean(transaction);
                    transactionBean.setBalance(number.format(waitingForApprovalAmountWithWire));
                    transactionBean.setTransactionId(waitingForApprovalIdWithWire);
                    transactionBean.setStatus(STATUS_WAITING_FOR_APPROVAL);
                    transactionBean.setPaymentMethod(PAYMENT_METHOD_WIRE.toUpperCase());
                    Date date = new Date();
                    String transDate = sdf.format(date);
                    transactionBean.setTransDate(transDate);
                    transList.add(transactionBean);
                }
            }
        }
        return transList;
    }

    /**
     * To get Ready To Pay Customers by Using UserId
     *
     * @param custNo
     * @param custName
     * @param user
     * @return readyToPayList
     * @throws HibernateException
     * @author Lakshmi Narayanan
     */
    public List<TransactionBean> getReadyToPayCustomersByUserId(String custNo, String custName, User user) throws Exception {
        List<TransactionBean> readyToPayList = new ArrayList<TransactionBean>();
        RoleDuty roleDuty = new RoleDAO().getRoleDuty(user.getRole().getRoleId());
        PaymentMethodDAO paymentMethodDAO = new PaymentMethodDAO();
        StringBuilder queryBuilder = new StringBuilder("select tr from Transaction tr,Vendor ve");
        queryBuilder.append(" where tr.custNo=ve.accountno");
        queryBuilder.append(" and tr.custNo like?0 and tr.custName like?1");
        queryBuilder.append(" and (tr.status=?2 or tr.status=?3) and tr.balance!=0");
        queryBuilder.append(" and (tr.transactionType=?4 or tr.transactionType=?5)");
        if (!roleDuty.isApPayment()) {
            queryBuilder.append(" and (ve.apSpecialist=?6 or tr.owner=?7 or tr.createdBy=?8)");
        }
        queryBuilder.append(" order by tr.custNo");
        Query query = getCurrentSession().createQuery(queryBuilder.toString());
        query.setParameter("0", "%" + custNo.trim() + "%", StringType.INSTANCE);
        query.setParameter("1", "%" + custName.trim() + "%", StringType.INSTANCE);
        query.setParameter("2", STATUS_READY_TO_PAY, StringType.INSTANCE);
        query.setParameter("3", STATUS_WAITING_FOR_APPROVAL, StringType.INSTANCE);
        query.setParameter("4", TRANSACTION_TYPE_ACCOUNT_PAYABLE, StringType.INSTANCE);
        query.setParameter("5", TRANSACTION_TYPE_ACCOUNT_RECEIVABLE, StringType.INSTANCE);
        if (!roleDuty.isApPayment()) {
            query.setParameter("6", user.getUserId(), IntegerType.INSTANCE);
            query.setParameter("7", user.getUserId(), IntegerType.INSTANCE);
            query.setParameter("8", user.getUserId(), IntegerType.INSTANCE);
        }
        List<Transaction> resultList = query.list();
        int i = 0;
        String readyToPayIds = "";
        String waitingForApprovalIdsForACH = "";
        String waitingForApprovalIdsForWIRE = "";
        Double readyToPayamount = 0d;
        Double waitingForApprovalAmountForACH = 0d;
        Double waitingForApprovalAmountForWIRE = 0d;
        boolean canAdd = false;
        for (Transaction transaction : resultList) {
            if (CommonUtils.isEqual(transaction.getStatus(), STATUS_READY_TO_PAY)) {
                readyToPayIds += "," + transaction.getTransactionId();
                if (CommonUtils.isEqual(transaction.getTransactionType(), TRANSACTION_TYPE_ACCOUNT_PAYABLE)) {
                    readyToPayamount += transaction.getBalance();
                } else if (CommonUtils.isEqual(transaction.getTransactionType(), TRANSACTION_TYPE_ACCOUNT_RECEIVABLE)) {
                    readyToPayamount -= transaction.getBalance();
                }
            } else if (CommonUtils.isEqual(transaction.getStatus(), STATUS_WAITING_FOR_APPROVAL)) {
                Double wfaAmount = 0d;
                if (CommonUtils.isEqual(transaction.getTransactionType(), TRANSACTION_TYPE_ACCOUNT_PAYABLE)) {
                    wfaAmount += transaction.getBalance();
                } else if (CommonUtils.isEqual(transaction.getTransactionType(), TRANSACTION_TYPE_ACCOUNT_RECEIVABLE)) {
                    wfaAmount -= transaction.getBalance();
                }
                if (CommonUtils.isEqual(transaction.getPaymentMethod().toUpperCase(), PAYMENT_METHOD_ACH.toUpperCase())) {
                    waitingForApprovalIdsForACH += "," + transaction.getTransactionId();
                    waitingForApprovalAmountForACH += wfaAmount;
                } else if (CommonUtils.isEqual(transaction.getPaymentMethod().toUpperCase(), PAYMENT_METHOD_WIRE.toUpperCase())) {
                    waitingForApprovalIdsForWIRE += "," + transaction.getTransactionId();
                    waitingForApprovalAmountForWIRE += wfaAmount;
                }
            }
            if ((i + 1) < resultList.size()) {
                Transaction nextTransaction = resultList.get(i + 1);
                if (!CommonUtils.isEqual(transaction.getCustNo(), nextTransaction.getCustNo())) {
                    canAdd = true;
                } else {
                    canAdd = false;
                }
            } else {
                canAdd = true;
            }
            if (canAdd) {
                if (CommonUtils.isNotEmpty(readyToPayIds) && readyToPayamount != 0d) {
                    TransactionBean transactionBean = new TransactionBean(transaction);
                    transactionBean.setTransactionId(readyToPayIds);
                    transactionBean.setBalance(NumberUtils.formatNumber(readyToPayamount, "###,###,##0.00"));
                    transactionBean.setStatus(STATUS_READY_TO_PAY);
                    transactionBean.setTransDate(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
                    transactionBean.setPaymentMethods(paymentMethodDAO.getPaymentMethods(transaction.getCustNo()));
                    readyToPayList.add(transactionBean);
                }
                if (CommonUtils.isNotEmpty(waitingForApprovalIdsForACH) && waitingForApprovalAmountForACH != 0d) {
                    TransactionBean transactionBean = new TransactionBean(transaction);
                    transactionBean.setTransactionId(waitingForApprovalIdsForACH);
                    transactionBean.setBalance(NumberUtils.formatNumber(waitingForApprovalAmountForACH, "###,###,##0.00"));
                    transactionBean.setStatus(STATUS_WAITING_FOR_APPROVAL);
                    transactionBean.setTransDate(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
                    List<LabelValueBean> paymentMethods = new ArrayList<LabelValueBean>();
                    paymentMethods.add(new LabelValueBean(PAYMENT_METHOD_ACH.toUpperCase(), PAYMENT_METHOD_ACH.toUpperCase()));
                    transactionBean.setPaymentMethods(paymentMethods);
                    readyToPayList.add(transactionBean);
                }
                if (CommonUtils.isNotEmpty(waitingForApprovalIdsForWIRE) && waitingForApprovalAmountForWIRE != 0d) {
                    TransactionBean transactionBean = new TransactionBean(transaction);
                    transactionBean.setTransactionId(waitingForApprovalIdsForWIRE);
                    transactionBean.setBalance(NumberUtils.formatNumber(waitingForApprovalAmountForWIRE, "###,###,##0.00"));
                    transactionBean.setStatus(STATUS_WAITING_FOR_APPROVAL);
                    transactionBean.setTransDate(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
                    List<LabelValueBean> paymentMethods = new ArrayList<LabelValueBean>();
                    paymentMethods.add(new LabelValueBean(PAYMENT_METHOD_WIRE.toUpperCase(), PAYMENT_METHOD_WIRE.toUpperCase()));
                    transactionBean.setPaymentMethods(paymentMethods);
                    readyToPayList.add(transactionBean);
                }
                canAdd = false;
                readyToPayIds = "";
                waitingForApprovalIdsForACH = "";
                waitingForApprovalIdsForWIRE = "";
                readyToPayamount = 0d;
                waitingForApprovalAmountForACH = 0d;
                waitingForApprovalAmountForWIRE = 0d;
            }
            i++;
        }
        return readyToPayList;
    }

    public List getPaymentsByInvoiceNumberAndVendorNumber(String invoiceNumber,
            String vendorNumber) throws Exception {
        List<TransactionBean> transList = new ArrayList<TransactionBean>();
        TransactionBean transactionBean = null;
        List invoiceList = getCurrentSession().createQuery(
                "from Transaction tx where tx.status='P' and tx.transactionType='AP' and tx.custNo='" + vendorNumber + "' and tx.invoiceNumber='" + invoiceNumber + "'").list();
        Transaction transaction = null;
        for (Iterator iterator = invoiceList.iterator(); iterator.hasNext();) {
            transaction = (Transaction) iterator.next();
            if (transaction != null) {
                transactionBean = new TransactionBean(transaction);
                transList.add(transactionBean);
            }
        }
        return transList;
    }

    public List getPaymentsByVendorNumber(String vendorNumber) throws Exception {
        List<TransactionBean> transList = new ArrayList<TransactionBean>();
        TransactionBean transactionBean = null;
        List invoiceList = getCurrentSession().createQuery(
                "from Transaction tx where tx.status='RP' and ((tx.transactionType='AP') or (tx.transactionType='AR' and tx.balance!=0)) and tx.custNo='" + vendorNumber + "'").list();
        Transaction transaction = null;
        for (Iterator iterator = invoiceList.iterator(); iterator.hasNext();) {
            transaction = (Transaction) iterator.next();
            if (transaction != null) {
                transactionBean = new TransactionBean(transaction);
                transList.add(transactionBean);
            }
        }
        return transList;
    }

    public List getPaymentsByTransactionId(String transactionIds) throws Exception {
        List<TransactionBean> transList = new ArrayList<TransactionBean>();
        TransactionBean transactionBean = null;
        if (transactionIds != null) {
            Query query = getCurrentSession().createQuery("from Transaction tx where  tx.transactionId in(" + transactionIds + ")");
            List<Transaction> paymentList = query.list();
            for (Transaction transaction : paymentList) {
                if (transaction != null) {
                    transactionBean = new TransactionBean(transaction);
                    if (transaction.getTransactionType().equals(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE)) {
                        transactionBean.setAmount(number.format(transaction.getBalance()));
                    }
                    List creditDetails = customerAccountingDAO.getCreditDetails(transactionBean.getCustomerNo().trim());
                    if (null != creditDetails && !creditDetails.isEmpty() && creditDetails.size() >= 2) {
                        if (null != creditDetails.get(1)) {
                            transactionBean.setCreditTerms(creditDetails.get(1).toString());
                        } else {
                            transactionBean.setCreditTerms("");
                        }
                    } else {
                        transactionBean.setCreditTerms("");
                    }
                    String dueDate = calculateDueDate(transactionBean.getCustomerNo().trim(), transactionBean.getInvoiceDate());
                    transactionBean.setDuedate(dueDate);
                    transList.add(transactionBean);
                }
            }
        }
        return transList;
    }

    /**
     * @param customerNo
     * @return
     */
    public List getCustomerDetails(String customerNo, String invoiceNumber) throws Exception {
        List<TransactionBean> transList = new ArrayList<TransactionBean>();
        List queryObject = getCurrentSession().createQuery(
                "from TransactionLedger tx where tx.transactionType='AP' and invoiceNumber='" + invoiceNumber + "'" + "and tx.custNo like'" + customerNo + "%'").list();
        Iterator iter = queryObject.iterator();
        while (iter.hasNext()) {
            TransactionLedger transactionLedger = (TransactionLedger) iter.next();
            transList.add(new TransactionBean(transactionLedger));
        }
        return transList;
    }

    /**
     * @param customerName
     * @return
     */
    public List getCustomersWithStatusPay(String customerName) throws Exception {
        List<CustAddress> transList = new ArrayList<CustAddress>();
        Query query = getCurrentSession().createQuery(
                "select tr.custName,tr.custNo,ca.city1,ca.state,ca.acctType from Transaction tr,CustAddress ca where tr.custNo=ca.acctNo and (tr.custName like ?0 or tr.custNo like ?1)and tr.status='RP' and ((tr.transactionType='AP') or (tr.transactionType='AR' and tr.balance!=0))" + " group by tr.custNo");
        query.setParameter("0", customerName + "%", StringType.INSTANCE);
        query.setParameter("1", customerName + "%", StringType.INSTANCE);
        List queryObject = query.list();
        Iterator iter = queryObject.iterator();

        while (iter.hasNext()) {
            CustAddress custAddress = new CustAddress();
            Object[] row = (Object[]) iter.next();
            custAddress.setAcctName(null != row[0] ? row[0].toString() : "");
            custAddress.setAcctNo(null != row[1] ? row[1].toString() : "");
            custAddress.setCity1(null != row[2] ? row[2].toString() : "");
            custAddress.setState(null != row[3] ? row[3].toString() : "");
            custAddress.setAcctType(null != row[4] ? row[4].toString() : "");
            transList.add(custAddress);
        }
        return transList;
    }

    public List getCustomersWithStatusPayByUserId(String customerName, User loginUser) throws Exception {
        List<CustAddress> transList = new ArrayList<CustAddress>();
        Query queryObject = getCurrentSession().createQuery(
                "select tr.custName,tr.custNo,ca.city1,ca.state,ca.acctType from Transaction tr,CustAddress ca,Vendor vendor where tr.custNo=ca.acctNo and tr.custNo=vendor.accountno and vendor.apSpecialist=?0 and (tr.custName like ?1 or tr.custNo like ?2) and tr.status='RP' and ((tr.transactionType='AP') or (tr.transactionType='AR' and tr.balance!=0))" + " group by tr.custNo");
        queryObject.setParameter("0", loginUser.getId(), IntegerType.INSTANCE);
        queryObject.setParameter("1", customerName + "%", StringType.INSTANCE);
        queryObject.setParameter("2", customerName + "%", StringType.INSTANCE);
        List result = queryObject.list();
        if (null != result && !result.isEmpty()) {
            Iterator iter = result.iterator();
            while (iter.hasNext()) {
                CustAddress custAddress = new CustAddress();
                Object[] row = (Object[]) iter.next();
                custAddress.setAcctName(null != row[0] ? row[0].toString() : "");
                custAddress.setAcctNo(null != row[1] ? row[1].toString() : "");
                custAddress.setCity1(null != row[2] ? row[2].toString() : "");
                custAddress.setState(null != row[3] ? row[3].toString() : "");
                custAddress.setAcctType(null != row[4] ? row[4].toString() : "");
                transList.add(custAddress);
            }
        }
        return transList;
    }

    public int payAllVendors(User loginUser) throws Exception {
        String queryString = "update transaction set Status = ?0 where Cust_no in (select cust_accno from " + "vendor_info where acc_manager = ?1) and Due_Date <= now() and transaction_type = ?2";
        Query queryObject = getSession().createSQLQuery(queryString);
        queryObject.setParameter("0", STATUS_PAY);
        queryObject.setParameter("1", loginUser.getId());
        queryObject.setParameter("2",
                TRANSACTION_TYPE_ACCOUNT_PAYABLE);
        int updatedRow = queryObject.executeUpdate();
        return updatedRow;
    }

    public int updateTransactionStatus(String transactionIds, String status, User loginUser) {
        String queryString = "update transaction set Status = '" + status + "' where Transaction_ID in (" + transactionIds + ")";
        Query queryObject = getSession().createSQLQuery(queryString);
        int updatedRow = queryObject.executeUpdate();
        return updatedRow;
    }

    public Integer paidTransactions(String paidTransactions, User loginUser) throws Exception {
        int totalPaid = 0;
        //update status in transaction table and create records in transaction_ledger
        // and set sub_ledger_code according to transaction_type
        if (null != paidTransactions && !paidTransactions.trim().equals("")) {
            String[] paidIds = StringUtils.split(paidTransactions, ",");
            for (String id : paidIds) {
                Transaction updatedTransaction = findById(Integer.parseInt(id));
                if (null != updatedTransaction) {
                    String glAccountNumber = "";
                    if (null != updatedTransaction.getTransactionType() && updatedTransaction.getTransactionType().trim().equals(TRANSACTION_TYPE_ACCOUNT_PAYABLE)) {
                        glAccountNumber = LoadLogisoftProperties.getProperty(AP_CONTROL_ACCOUNT);
                    } else if (null != updatedTransaction.getTransactionType() && updatedTransaction.getTransactionType().trim().equals(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE)) {
                        glAccountNumber = LoadLogisoftProperties.getProperty(AR_CONTROL_ACCOUNT);
                    }
                    updatedTransaction.setStatus(STATUS_READY_TO_PAY);
                    updatedTransaction.setGlAccountNumber(glAccountNumber);
                    updatedTransaction.setUpdatedOn(new Date());
                    updatedTransaction.setUpdatedBy(loginUser.getUserId());
                    updatedTransaction.setOwner(loginUser.getUserId());
                    StringBuilder desc = new StringBuilder("Invoice '").append(updatedTransaction.getInvoiceNumber()).append("'");
                    desc.append(" of '").append(updatedTransaction.getCustName()).append("'");
                    desc.append(" for amount '").append(NumberUtils.formatNumber(updatedTransaction.getTransactionAmt(), "###,###,##0.00")).append("'");
                    desc.append(" is set ready to pay by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
                    AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.AP_INVOICE, updatedTransaction.getCustNo() + "-" + updatedTransaction.getInvoiceNumber(), NotesConstants.AP_INVOICE, loginUser);
                    totalPaid++;
                }
            }
        }
        return totalPaid;
    }

    public Transaction getArTransaction(String customerNumber, String blNumber, String invoiceNumber) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(Transaction.class);
        criteria.add(Restrictions.eq("transactionType", TRANSACTION_TYPE_ACCOUNT_RECEIVABLE));
        criteria.add(Restrictions.eq("custNo", customerNumber));
        Disjunction disjunction = Restrictions.disjunction();
        if (CommonUtils.isNotEmpty(blNumber)) {
            disjunction.add(Restrictions.eq("billLaddingNo", blNumber));
            disjunction.add(Restrictions.eq("invoiceNumber", blNumber));
        } else {
            disjunction.add(Restrictions.eq("billLaddingNo", invoiceNumber));
            disjunction.add(Restrictions.eq("invoiceNumber", invoiceNumber));
        }
        criteria.add(disjunction);
        criteria.addOrder(Order.asc("transactionDate"));
        criteria.setMaxResults(1);
        return (Transaction) criteria.uniqueResult();
    }

    public Integer releaseTransactions(String ids, User user) throws Exception {
        int totalRelease = 0;
        ApTransactionHistoryDAO historyDAO = new ApTransactionHistoryDAO();
        AccountDetailsDAO accountDetailsDAO = new AccountDetailsDAO();
        FclBlCostCodesDAO fclBlCostCodesDAO = new FclBlCostCodesDAO();
        AccrualsDAO accrualsDAO = new AccrualsDAO();
        ApInvoiceDAO apInvoiceDAO = new ApInvoiceDAO();
        EdiInvoiceDAO ediInvoiceDAO = new EdiInvoiceDAO();
        ArTransactionHistoryDAO arTransactionHistoryDAO = new ArTransactionHistoryDAO();
        for (String id : StringUtils.split(ids, ",")) {
            Transaction oldTransaction = findById(Integer.parseInt(id));
            String vendorNumber = oldTransaction.getCustNo();
            String invoiceNumber = oldTransaction.getInvoiceNumber();
            Date postedDate = accrualsDAO.getPostedDate(oldTransaction.getPostedDate());
            oldTransaction.setStatus(STATUS_REJECT);
            oldTransaction.setUpdatedOn(new Date());
            oldTransaction.setUpdatedBy(user.getUserId());
            List<TransactionLedger> accruals = accrualsDAO.getAssignedAccruals(vendorNumber, invoiceNumber);
            for (TransactionLedger accrual : accruals) {
                // Revert back Assigned Accruals into Open Accruals when released
                if (accountDetailsDAO.validateAccount(accrual.getGlAccountNumber())) {
                    if (!accrual.isDirectGlAccount()) {
                        accrual.setStatus(STATUS_OPEN);
                        accrual.setUpdatedOn(new Date());
                        accrual.setUpdatedBy(user.getUserId());
                        accrual.setPostedDate(null);
                        accrual.setBalance(accrual.getTransactionAmt());
                        accrual.setBalanceInProcess(accrual.getTransactionAmt());
                        StringBuilder desc = new StringBuilder("Accrual of ");
                        boolean addAnd = false;
                        if (CommonUtils.isNotEmpty(accrual.getBillLaddingNo())) {
                            desc.append("B/L - '").append(accrual.getBillLaddingNo()).append("'");
                            addAnd = true;
                        }
                        if (CommonUtils.isNotEmpty(accrual.getDocReceipt())) {
                            if (addAnd) {
                                desc.append(" and ");
                            }
                            desc.append("Doc Receipt - '").append(accrual.getDocReceipt()).append("'");
                            addAnd = true;
                        }
                        if (CommonUtils.isNotEmpty(accrual.getVoyageNo())) {
                            if (addAnd) {
                                desc.append(" and ");
                            }
                            desc.append("Voyage - '").append(accrual.getVoyageNo()).append("'");
                        }
                        desc.append(" for amount '").append(NumberUtils.formatNumber(accrual.getTransactionAmt(), "###,###,##0.00")).append("'");
                        if (STATUS_EDI_ASSIGNED.equals(accrual.getStatus())) {
                            desc.append(" is marked back as inprogress with Invoice '").append(accrual.getInvoiceNumber()).append("'");
                        } else {
                            desc.append(" is unassigned from Invoice '").append(accrual.getInvoiceNumber()).append("'");
                        }
                        desc.append(" by ").append(user.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
                        AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.ACCRUALS, accrual.getTransactionId().toString(), NotesConstants.ACCRUALS, user);
                        accrual.setInvoiceNumber(STATUS_EDI_ASSIGNED.equals(accrual.getStatus()) ? accrual.getInvoiceNumber() : null);
                    }
                    //Create PJ Subledger Record with negative amount
                    TransactionLedger pjSubledger = new TransactionLedger();
                    PropertyUtils.copyProperties(pjSubledger, accrual);
                    pjSubledger.setInvoiceNumber(invoiceNumber);
                    pjSubledger.setTransactionAmt((-1) * pjSubledger.getTransactionAmt());
                    pjSubledger.setTransactionDate(new Date());
                    pjSubledger.setPostedDate(postedDate);
                    pjSubledger.setSubledgerSourceCode(SUB_LEDGER_CODE_PURCHASE_JOURNAL);
                    pjSubledger.setStatus(STATUS_OPEN);
                    pjSubledger.setTransactionType(TRANSACTION_TYPE_ACCOUNT_PAYABLE);
                    pjSubledger.setUpdatedOn(null);
                    pjSubledger.setUpdatedBy(null);
                    pjSubledger.setCreatedOn(new Date());
                    pjSubledger.setCreatedBy(user.getUserId());
                    accrualsDAO.save(pjSubledger);
                    if (CommonUtils.isNotEmpty(accrual.getCostId())) {
                        if (CommonUtils.in(accrual.getShipmentType(), "LCLE", "LCLI")) {
                            Map<Serializable, Serializable> columns = new HashMap<Serializable, Serializable>();
                            if (CommonUtils.isNotEmpty(accrual.getDocReceipt())) {
                                columns.put("b.invoice_number", accrual.getInvoiceNumber());
                                columns.put("t.trans_type", TRANSACTION_TYPE_ACCRUALS);
                                accrualsDAO.updateLclCost(accrual.getCostId(), columns);
                            } else {
                                columns.put("lssac.ap_reference_no", accrual.getInvoiceNumber());
                                columns.put("lssac.ap_trans_type", TRANSACTION_TYPE_ACCRUALS);
                                accrualsDAO.updateLclUnitCost(accrual.getCostId(), columns);
                            }
                        } else {
                            FclBlCostCodes fclBlCostCodes = fclBlCostCodesDAO.findById(accrual.getCostId());
                            if (null != fclBlCostCodes) {
                                fclBlCostCodes.setTransactionType(TRANSACTION_TYPE_ACCRUALS);
                                fclBlCostCodes.setInvoiceNumber(null);
                                fclBlCostCodes.setDatePaid(null);
                            }
                        }
                    } else if (accrual.isDirectGlAccount()) {
                        accrualsDAO.delete(accrual);
                    }
                } else {
                    throw new Exception("No GL account Found for original accrual");
                }
            }

            List<ArTransactionHistory> arPayments = arTransactionHistoryDAO.getArPaidInApInvoice(vendorNumber + "-" + invoiceNumber);
            if (CommonUtils.isNotEmpty(arPayments)) {
                for (ArTransactionHistory arPayment : arPayments) {
                    Transaction ar = getArTransaction(arPayment.getCustomerNumber(), arPayment.getBlNumber(), arPayment.getInvoiceNumber());
                    ArTransactionHistory arTransactionHistory = new ArTransactionHistory();
                    arTransactionHistory.setCustomerNumber(ar.getCustNo());
                    arTransactionHistory.setBlNumber(ar.getBillLaddingNo());
                    arTransactionHistory.setInvoiceNumber(ar.getInvoiceNumber());
                    arTransactionHistory.setInvoiceDate(ar.getTransactionDate());
                    arTransactionHistory.setTransactionDate(new Date());
                    arTransactionHistory.setPostedDate(postedDate);
                    arTransactionHistory.setTransactionAmount(-arPayment.getTransactionAmount());
                    arTransactionHistory.setAdjustmentAmount(0d);
                    arTransactionHistory.setCustomerReferenceNumber(ar.getCustomerReferenceNo());
                    arTransactionHistory.setVoyageNumber(ar.getVoyageNo());
                    arTransactionHistory.setCheckNumber(vendorNumber + "-" + invoiceNumber);
                    arTransactionHistory.setArBatchId(null);
                    arTransactionHistory.setApBatchId(null);
                    arTransactionHistory.setTransactionType("AP INV");
                    arTransactionHistory.setCreatedBy(user.getLoginName());
                    arTransactionHistory.setCreatedDate(new Date());
                    arTransactionHistoryDAO.save(arTransactionHistory);
                    ar.setBalance(ar.getBalance() - arPayment.getTransactionAmount());
                    ar.setBalanceInProcess(ar.getBalanceInProcess() - arPayment.getTransactionAmount());
                    String apInvoice = vendorNumber + "-" + invoiceNumber;
                    String invoiceOrBl = (CommonUtils.isNotEmpty(ar.getBillLaddingNo()) ? ar.getBillLaddingNo() : ar.getInvoiceNumber());
                    String moduleRefId = ar.getCustNo() + "-" + invoiceOrBl;
                    StringBuilder desc = new StringBuilder();
                    desc.append("For Amount : ").append(NumberUtils.formatNumber(-arPayment.getTransactionAmount())).append(",");
                    desc.append(" removed from ");
                    desc.append(CommonUtils.isEqualIgnoreCase(ar.getApInvoiceStatus(), STATUS_EDI_ASSIGNED) ? "EDI" : "AP");
                    desc.append(" Invoice - '").append(apInvoice).append("'");
                    desc.append(" by ").append(user.getLoginName());
                    desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
                    AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.AR_INVOICE, moduleRefId, NotesConstants.AR_INVOICE, user);
                }
            }

            Transaction newTransaction = new Transaction();
            PropertyUtils.copyProperties(newTransaction, oldTransaction);
            newTransaction.setTransactionDate(postedDate);
            newTransaction.setPostedDate(postedDate);
            newTransaction.setTransactionAmt((-1) * newTransaction.getTransactionAmt());
            newTransaction.setBalance((-1) * newTransaction.getBalance());
            newTransaction.setBalanceInProcess((-1) * newTransaction.getTransactionAmt());
            newTransaction.setBillTo(YES);
            newTransaction.setStatus(STATUS_REJECT);
            newTransaction.setCreatedOn(new Date());
            newTransaction.setCreatedBy(user.getUserId());
            newTransaction.setUpdatedOn(null);
            newTransaction.setUpdatedBy(null);
            newTransaction.setApBatchId(null);
            newTransaction.setArBatchId(null);
            newTransaction.setAchBatchSequence(null);
            save(newTransaction);
            ApTransactionHistory apTransactionHistory = new ApTransactionHistory(newTransaction);
            apTransactionHistory.setInvoiceDate(oldTransaction.getTransactionDate());
            apTransactionHistory.setCreatedBy(user.getLoginName());
            historyDAO.save(apTransactionHistory);
            //delete notes
            String glPeriod = DateUtils.formatDate(postedDate, "yyyyMM");
            StringBuilder desc = new StringBuilder("Invoice '").append(invoiceNumber).append("'");
            desc.append(" of '").append(oldTransaction.getCustName()).append("'").append(" deleted on GL Period '");
            desc.append(glPeriod).append(" for amount '").append(NumberUtils.formatNumber(oldTransaction.getTransactionAmt(), "###,###,##0.00")).append("'");
            desc.append("' by ").append(user.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
            AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.AP_INVOICE, vendorNumber + "-" + invoiceNumber, NotesConstants.AP_INVOICE, user);
            // delete invoice records from ap_invoice when released
            ApInvoice apInvoice = apInvoiceDAO.findInvoiceByInvoiceNumber(invoiceNumber, vendorNumber);
            if (null != apInvoice) {
                apInvoiceDAO.delete(apInvoice);
            } else {
                EdiInvoice ediInvoice = ediInvoiceDAO.getInvoice(vendorNumber, invoiceNumber, STATUS_EDI_POSTED_TO_AP);
                if (null != ediInvoice) {
                    ediInvoice.setStatus(ConstantsInterface.STATUS_EDI_ARCHIVE);
                    ediInvoiceDAO.update(ediInvoice);
                }
            }
            totalRelease++;
        }
        return totalRelease;
    }

    // Search CustomerSample Reports
    // THIS IS FOR SEARCH CUSTOMER SAMPLE AGING REPORT-02-02-09
    public List getSingleCustomerList(
            SearchCustomerSampleForm searchCustomerSample) throws Exception {
        SearchCustomerSampleDTO searchCustomerSampleDTO = null;
        String custName = searchCustomerSample.getCustomerName();
        List<SearchCustomerSampleDTO> singleCustomerFieldList = new ArrayList<SearchCustomerSampleDTO>();
        Date today = new Date();
        String queryString = "select trans.invoiceNumber,trans.balance,trans.transactionDate,trans.transactionAmt from Transaction trans where trans.custName='" + custName + "'";
        List queryObject = getCurrentSession().createQuery(queryString).list();
        Iterator iter = queryObject.iterator();
        int i = 0;
        while (iter.hasNext()) {
            searchCustomerSampleDTO = new SearchCustomerSampleDTO();
            Object[] row = (Object[]) iter.next();
            String invoiceNumber = (String) (row[0]);
            Double balance = (Double) row[1];
            Date date = (Date) row[2];
            Double transAmt = (Double) row[3];
            if (balance == null) {
                balance = 0.0;
            }
            String aging = "";
            String invoiceDate = "";
            if (date != null && !date.equals("")) {
                invoiceDate = sdf.format(date);
                Calendar cal1 = Calendar.getInstance();
                cal1.setTime(date);
                Calendar cal2 = Calendar.getInstance();
                cal2.setTime(today);
                long inDate = printOutput("Calendar", date, today,
                        daysBetween(cal1, cal2));
                aging = (String.valueOf(inDate));
            }
            if (invoiceNumber == null) {
                invoiceNumber = "";
            }
            if (balance != null && !balance.equals("") && balance > 0.0) {
                searchCustomerSampleDTO.setBalance(number.format(balance));
                searchCustomerSampleDTO.setInvoiceNo(invoiceNumber);
                searchCustomerSampleDTO.setAging(aging);
                if (transAmt != null && !transAmt.equals("") && transAmt != 0.0) {
                    searchCustomerSampleDTO.setAmount(number.format(transAmt));
                }
                searchCustomerSampleDTO.setInvoiceDate(invoiceDate);
                singleCustomerFieldList.add(searchCustomerSampleDTO);
                searchCustomerSampleDTO = null;
            }
        }
        return singleCustomerFieldList;
    }

    // THIS IS FOR SEARCH CUSTOMER SAMPLE REPORT
    public List ForSearchCustomerSampleAddress(
            SearchCustomerSampleForm searchCustomerSample) throws Exception {
        String custName = searchCustomerSample.getCustomerName();
        SearchCustomerSampleDTO searchCustomerSampleDTO = null;
        List<SearchCustomerSampleDTO> customerAddressList = new ArrayList<SearchCustomerSampleDTO>();
        Date today = new Date();
        String queryString = "select cust.acctNo,cust.acctName,cust.address1,cust.city1,cust.state,cust.phone,cust.fax from CustAddress cust where cust.acctName='" + custName + "'";
        List queryObject = getCurrentSession().createQuery(queryString).list();
        Iterator iter = queryObject.iterator();
        if (queryObject != null) {
            searchCustomerSampleDTO = new SearchCustomerSampleDTO();
            Object[] row = (Object[]) iter.next();
            String custnum = (String) row[0];
            String custname = (String) row[1];
            String custaddress = (String) row[2];
            String city = (String) row[3];
            String state = (String) row[4];
            String phone = (String) row[5];
            String fax = (String) row[6];

            searchCustomerSampleDTO.setAcctNo(custnum);
            searchCustomerSampleDTO.setAcctName(custname);
            searchCustomerSampleDTO.setCustAddress(custaddress);
            searchCustomerSampleDTO.setPhone(phone);
            searchCustomerSampleDTO.setFax(fax);
            searchCustomerSampleDTO.setCity(city);
            customerAddressList.add(searchCustomerSampleDTO);
            searchCustomerSampleDTO = null;
        }// if
        return customerAddressList;
    }

    // THIS IS AR AGING RWEPORT SAMPLE REPORT
    public List ForSearchCustomerSampleAddress(AgingReportForm agingReportForm) throws Exception {
        String custName = agingReportForm.getCustomerName();
        SearchCustomerSampleDTO searchCustomerSampleDTO = null;
        List<SearchCustomerSampleDTO> customerAddressList = new ArrayList<SearchCustomerSampleDTO>();
        Date today = new Date();
        String queryString = "select cust.acctNo,cust.acctName,cust.address1,cust.city1,cust.state,cust.phone,cust.fax from CustAddress cust where cust.acctName='" + custName + "'";
        List queryObject = getCurrentSession().createQuery(queryString).list();
        Iterator iter = queryObject.iterator();
        if (queryObject != null) {
            searchCustomerSampleDTO = new SearchCustomerSampleDTO();
            Object[] row = (Object[]) iter.next();
            String custnum = (String) row[0];
            String custname = (String) row[1];
            String custaddress = (String) row[2];
            String city = (String) row[3];
            String state = (String) row[4];
            String phone = (String) row[5];
            String fax = (String) row[6];

            searchCustomerSampleDTO.setAcctNo(custnum);
            searchCustomerSampleDTO.setAcctName(custname);
            searchCustomerSampleDTO.setCustAddress(custaddress);
            searchCustomerSampleDTO.setPhone(phone);
            searchCustomerSampleDTO.setFax(fax);
            searchCustomerSampleDTO.setCity(city);
            customerAddressList.add(searchCustomerSampleDTO);
            searchCustomerSampleDTO = null;
        }// if
        return customerAddressList;
    }

    // THIS IS FOR AR AGING REPORT SAMPLE AGING REPORT-02-02-09
    public List getSingleCustomerList(AgingReportForm agingReportForm) throws Exception {
        SearchCustomerSampleDTO searchCustomerSampleDTO = null;
        String custName = agingReportForm.getCustomerName();
        List<SearchCustomerSampleDTO> singleCustomerFieldList = new ArrayList<SearchCustomerSampleDTO>();
        Date today = new Date();
        String queryString = "select trans.invoiceNumber,trans.balance,trans.transactionDate,trans.transactionAmt,trans.transactionId from Transaction trans where trans.custName='" + custName + "'";
        List queryObject = getCurrentSession().createQuery(queryString).list();
        Iterator iter = queryObject.iterator();
        int i = 0;
        while (iter.hasNext()) {
            searchCustomerSampleDTO = new SearchCustomerSampleDTO();
            Object[] row = (Object[]) iter.next();
            String invoiceNumber = (String) (row[0]);
            Double balance = (Double) row[1];
            Date date = (Date) row[2];
            Double transAmt = (Double) row[3];
            Integer transId = (Integer) row[4];

            // To Check Whether the Payment for the Transaction is Open or
            // closed batch
            String paymentQueryString = "select sum(p.paymentAmt) from Payments p,ArBatch b where p.arTranactionId=" + transId + " and p.batchId=b.batchId and b.status='Open'";
            Double balanceToBeAdded = 0d;
            List batchIdList = getCurrentSession().createQuery(
                    paymentQueryString).list();
            if (batchIdList != null && !batchIdList.isEmpty()) {
                balanceToBeAdded = (Double) batchIdList.get(0);
            }
            if (balance == null) {
                balance = 0.0;
            }
            if (balanceToBeAdded != null) {
                balance = balance + balanceToBeAdded;
            }
            String aging = "";
            String invoiceDate = "";
            if (date != null && !date.equals("")) {
                invoiceDate = sdf.format(date);
                Calendar cal1 = Calendar.getInstance();
                cal1.setTime(date);
                Calendar cal2 = Calendar.getInstance();
                cal2.setTime(today);
                long inDate = printOutput("Calendar", date, today,
                        daysBetween(cal1, cal2));
                aging = (String.valueOf(inDate));
            }
            if (invoiceNumber == null) {
                invoiceNumber = "";
            }
            if (balance != null && !balance.equals("") && balance > 0.0) {
                searchCustomerSampleDTO.setBalance(number.format(balance));
                searchCustomerSampleDTO.setInvoiceNo(invoiceNumber);
                searchCustomerSampleDTO.setAging(aging);
                if (transAmt != null && !transAmt.equals("") && transAmt != 0.0) {
                    searchCustomerSampleDTO.setAmount(number.format(transAmt));
                }
                searchCustomerSampleDTO.setInvoiceDate(invoiceDate);
                searchCustomerSampleDTO.setTransId(transId);
                singleCustomerFieldList.add(searchCustomerSampleDTO);
                searchCustomerSampleDTO = null;
            }
        }
        return singleCustomerFieldList;
    }

    // Gayatri junk Code
    public List getVendorsList() throws Exception {
        List<TransactionBean> vendorList = new ArrayList<TransactionBean>();
        List customerRecords = new ArrayList();
        StringBuffer queryString = new StringBuffer();
        queryString.append("from Transaction transaction where transaction.transactionType='");
        queryString.append(TRANSACTION_TYPE_ACCOUNT_PAYABLE);
        queryString.append("'");
        queryString.append(" and(transaction.status='");
        queryString.append(STATUS_OPEN);
        queryString.append("' or transaction.status='");
        queryString.append(STATUS_ASSIGN);
        queryString.append("') order by transaction.custNo");
        customerRecords = getCurrentSession().createQuery(queryString.toString()).setMaxResults(100).list();
        for (int i = 0; i < customerRecords.size(); i++) {
            Transaction transaction = (Transaction) customerRecords.get(i);
            TransactionBean transactionBean = null;
            transactionBean = new TransactionBean(transaction);
            if (null != transaction.getCreditTerms() && !transaction.getCreditTerms().toString().trim().equals("")) {
                String creditTerms = getCreditTerm(transaction.getCustNo(), transaction.getCreditTerms());
                transactionBean.setCreditTerms(creditTerms);
            }
            if (null != transaction.getTransactionDate() && !transaction.getTransactionDate().toString().trim().equals("")) {
                String invoiceDate = sdf.format(transaction.getTransactionDate());
                String dueDate = calculateDueDate(transaction.getCustNo(), invoiceDate);
                transactionBean.setDuedate(dueDate);
                long age = calculateAge(transaction.getTransactionDate());
                transactionBean.setAge(age);
            }
            if (null != transactionBean.getRecordType() && !transactionBean.getRecordType().trim().equals(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE)
                    && null != transactionBean.getStatus() && transaction.getStatus().trim().equals(STATUS_HOLD)) {
                transactionBean.setHold(ON);
            }
            vendorList.add(transactionBean);
        }
        return vendorList;
    }

    public List getVendorsListByShowHold(String showHold) throws Exception {
        List<TransactionBean> vendorList = new ArrayList<TransactionBean>();
        List customerRecords = new ArrayList();
        StringBuffer queryString = new StringBuffer();
        queryString.append("from Transaction transaction where transaction.transactionType='");
        queryString.append(TRANSACTION_TYPE_ACCOUNT_PAYABLE);
        queryString.append("'");
        queryString.append(" and(transaction.status='");
        queryString.append(STATUS_HOLD);
        queryString.append("') order by transaction.custNo");
        customerRecords = getCurrentSession().createQuery(queryString.toString()).setMaxResults(100).list();
        for (int i = 0; i < customerRecords.size(); i++) {
            Transaction transaction = (Transaction) customerRecords.get(i);
            TransactionBean transactionBean = null;
            transactionBean = new TransactionBean(transaction);
            if (null != transaction.getCreditTerms() && !transaction.getCreditTerms().toString().trim().equals("")) {
                String creditTerms = getCreditTerm(transaction.getCustNo(), transaction.getCreditTerms());
                transactionBean.setCreditTerms(creditTerms);
            }
            if (null != transaction.getTransactionDate() && !transaction.getTransactionDate().toString().trim().equals("")) {
                String invoiceDate = sdf.format(transaction.getTransactionDate());
                String dueDate = calculateDueDate(transaction.getCustNo(), invoiceDate);
                transactionBean.setDuedate(dueDate);
                long age = calculateAge(transaction.getTransactionDate());
                transactionBean.setAge(age);
            }
            vendorList.add(transactionBean);
        }
        return vendorList;
    }

    public List getTransactionRecordsByInvoiceOrBLNumber(String billLaddingNo,
            String invoiceNumber) throws Exception {
        List result1 = new ArrayList();
        String queryString = null;
        if (billLaddingNo != null && !billLaddingNo.trim().equals("")) {
            queryString = " from Transaction where billLaddingNo='" + billLaddingNo + "'and transactionType='" + TRANSACTION_TYPE_ACCOUNT_PAYABLE + "'";
        } else if (invoiceNumber != null && !invoiceNumber.trim().equals("")) {
            queryString = " from Transaction where invoiceNumber='" + invoiceNumber + "' and transactionType='" + TRANSACTION_TYPE_ACCOUNT_PAYABLE + "'";

        }
        result1 = getCurrentSession().createQuery(queryString).list();
        return result1;
    }

    public List<String> getTransactionIds(List<TransactionBean> transList) throws Exception {
        List<String> transIdList = null;
        List<TransactionBean> list = transList;
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            TransactionBean transactionBean = (TransactionBean) iterator.next();
            transIdList.add(transactionBean.getTransactionId());
        }
        return transIdList;
    }

    public List<TransactionBean> getCustomersForReprint(String chequeNumber, String vendorNumber, String batchNumber) throws Exception {
        List<TransactionBean> transactionBeanList = new ArrayList<TransactionBean>();
        TransactionBean transactionBean = null;
        Criteria criteria = getCurrentSession().createCriteria(Transaction.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        criteria.add(Restrictions.like("chequeNumber", chequeNumber));
        criteria.add(Restrictions.like("custNo", vendorNumber));
        criteria.add(Restrictions.like("apBatchId", Integer.parseInt(batchNumber)));
        criteria.add(Restrictions.like("transactionType", TRANSACTION_TYPE_PAYAMENT));
        //criteria.add(Restrictions.like("balance", new Double(0)));
        //criteria.add(Restrictions.like("balanceInProcess", new Double(0)));
        List invoiceList = criteria.list();
        Iterator itr = invoiceList.iterator();
        while (itr.hasNext()) {
            Transaction transaction = (Transaction) itr.next();
            if (transaction != null) {
                transactionBean = new TransactionBean(transaction);
                Date date = new Date();
                String transDate = sdf.format(date);
                transactionBean.setTransDate(transDate);
            }
            transactionBeanList.add(transactionBean);
            transactionBean = null;
        }
        return transactionBeanList;
    }

    public List getOnAccountorPrepaymentTransactionForCustomer(
            String custNumber, String invoiceNo) throws Exception {
        List<Transaction> transList = new ArrayList<Transaction>();
        String query = " from Transaction t where t.custNo='" + custNumber + "' and t.invoiceNumber='" + invoiceNo + "'";
        transList = getCurrentSession().createQuery(query).list();
        return transList;
    }
    // THIS IS FOR ALL AGING REPORT STATEMENT Report

    public List ForallAgingPeriodReport(customerStatementVO statementVO,
            String excludeFromStatement) throws Exception {
        String ageingzeero = statementVO.getAgingzeero();
        String ageingthirty = statementVO.getAgingthirty();
        String greaterthanthirty = statementVO.getGreaterthanthirty();
        String agingsixty = statementVO.getAgingsixty();
        String greaterthansixty = statementVO.getGreaterthansixty();
        String agingninty = statementVO.getAgingninty();
        String greaterthanninty = statementVO.getGreaterthanninty();
        String overdue = statementVO.getOverdue();
        String minamt = statementVO.getMinamt();
        String stmtWithCredit = statementVO.getStmtWithCredit();
        String includeAP = statementVO.getIncludeAP();
        String includeAccruals = statementVO.getIncludeAccruals();
        String includeInvoiceCredit = statementVO.getIncludeInvoiceCredit();
        String includeNetSettlement = statementVO.getIncludeNetSettlement();
        AgingReportPeriodDTO agReportPeriodDTO = null;
        List<AgingReportPeriodDTO> agingReportList = new ArrayList<AgingReportPeriodDTO>();
        NumberFormat number = new DecimalFormat("###,###,##0.00");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date today = new Date();
        String queryString = "select distinct trans.custNo from Transaction trans where trans.custName IS NOT NULL order by trans.custNo asc";
        List queryObject = getCurrentSession().createQuery(queryString).list();
        int size = queryObject.size();
        int count = 0;
        String CustAddress = "";
        while (size > 0) {
            Double total = 0.00;
            Double totalone = 0.00;
            Double totaltwo = 0.00;
            Double totalthree = 0.00;
            Double totalfour = 0.00;
            Double totBalance = 0.00;
            String custNum = (String) queryObject.get(count);
            String queryString2 = "select cust.address1 from CustAddress cust where cust.acctNo='" + custNum + "'";
            List queryObject2 = getCurrentSession().createQuery(
                    queryString2).list();

            if (!queryObject2.isEmpty()) {
                CustAddress = (String) queryObject2.get(0);
            }
            if (custNum != null && !custNum.equals("")) {
                String queryString1 = "";
                if (null != includeNetSettlement && includeNetSettlement.trim().equals("only")) {
                    queryString1 = "select trans.transactionDate,trans.invoiceNumber,trans.transactionAmt,trans.balance,trans.consName,trans.billLaddingNo,trans.transactionId,trans.customerReferenceNo,trans.custNo from Transaction trans where trans.custNo='" + custNum + "' and transactionType='" + ReportConstants.TRANSACTIONTYPE + "'" + "	and trans.subledgerSourceCode='" + AccountingConstants.SUBLEDGER_CODE_NETSETT + "'";
                } else if (null != includeAP && includeAP.equals("yes") && null != includeAccruals && includeAccruals.equals("yes")) {
                    queryString1 = "select trans.transactionDate,trans.invoiceNumber,trans.transactionAmt,trans.balance,trans.consName,trans.billLaddingNo,trans.transactionId,trans.customerReferenceNo,trans.custNo from Transaction trans where trans.custNo='" + custNum + "' and transactionType='" + ReportConstants.TRANSACTIONTYPE + "' or transactionType='" + ReportConstants.TRANSACTIONTYPEAP + "' or transactionType='" + ReportConstants.TRANSACTIONTYPEAC + "'";
                } else if (null != includeAP && includeAP.equals("yes")) {
                    queryString1 = "select trans.transactionDate,trans.invoiceNumber,trans.transactionAmt,trans.balance,trans.consName,trans.billLaddingNo,trans.transactionId,trans.customerReferenceNo,trans.custNo from Transaction trans where trans.custNo='" + custNum + "' and transactionType='" + ReportConstants.TRANSACTIONTYPE + "' or transactionType='" + ReportConstants.TRANSACTIONTYPEAP + "' ";
                } else if (null != includeAccruals && includeAccruals.equals("yes")) {
                    queryString1 = "select trans.transactionDate,trans.invoiceNumber,trans.transactionAmt,trans.balance,trans.consName,trans.billLaddingNo,trans.transactionId,trans.customerReferenceNo,trans.custNo from Transaction trans where trans.custNo='" + custNum + "' and transactionType='" + ReportConstants.TRANSACTIONTYPE + "' or transactionType='" + ReportConstants.TRANSACTIONTYPEAC + "'";
                } else {
                    queryString1 = "select trans.transactionDate,trans.invoiceNumber,trans.transactionAmt,trans.balance,trans.consName,trans.billLaddingNo,trans.transactionId,trans.customerReferenceNo,trans.custNo from Transaction trans where trans.custNo='" + custNum + "' and transactionType='" + ReportConstants.TRANSACTIONTYPE + "'";
                }
                List queryObject1 = getCurrentSession().createQuery(
                        queryString1).list();


                Iterator iter1 = queryObject1.iterator();
                while (iter1.hasNext()) {
                    agReportPeriodDTO = new AgingReportPeriodDTO();
                    Object[] row1 = (Object[]) iter1.next();
                    Date transactionDate = (Date) row1[0];
                    String invoiceNumber = (String) row1[1];
                    Double transactionAmt = (Double) row1[2];
                    Double balance = (Double) row1[3];
                    String consName = (String) row1[4];
                    String transactionNo = (String) row1[5];
                    Integer transactionId = (Integer) row1[6];
                    String custrefno = (String) row1[7];
                    String custNo = (String) row1[8];
                    if (!excludeFromStatement.contains(transactionId.toString())) {
                        if (custNo == null) {
                            custNo = "";
                        }
                        if (transactionAmt == null) {
                            transactionAmt = 0.0;
                        }
                        String transAmt = (String.valueOf(transactionAmt));
                        if (transAmt.contains(",")) {
                            transAmt = transAmt.replace(",", "");
                        }
                        transactionAmt = (Double.parseDouble(transAmt));
                        if (balance == null) {
                            balance = 0.0;
                        }
                        String bal = (String.valueOf(balance));
                        if (bal.contains(",")) {
                            bal = bal.replace(",", "");
                        }
                        balance = (Double.parseDouble(bal));
                        if (transactionId != null) {
                            String queryString3 = "select sum(pay.paymentAmt) from Payments pay where pay.arTranactionId='" + transactionId + "'";
                            List queryObject3 = getCurrentSession().createQuery(queryString3).list();
                            Double payamt = (Double) queryObject3.get(0);
                            if (payamt == null) {
                                payamt = 0.0;
                            }
                            String payment = (String.valueOf(payamt));
                            if (payment.contains(",")) {
                                payment = payment.replace(",", "");
                            }
                            payamt = (Double.parseDouble(payment));
                            agReportPeriodDTO.setPaymentoradjustment(number.format(payamt));
                        }
                        if (custrefno == null) {
                            custrefno = "";
                        }
                        String aging = "";
                        long range = 0;
                        if (null != transactionDate) {
                            range = this.calculateAge(transactionDate);
                        }
                        int OverDue = (Integer.parseInt(overdue));
                        Double MinAmt = (Double.parseDouble(minamt));
                        if (balance != null && balance < 0) {
                            totBalance = totBalance + balance;
                            if (includeInvoiceCredit != null && includeInvoiceCredit.equals("yes")) {
                                totBalance = totBalance + transactionAmt;
                            }
                        }
                        if (balance != null && balance > 0.0) {
                            if (OverDue <= range && MinAmt <= balance) {
                                if (Integer.parseInt(ageingzeero) <= range && Integer.parseInt(ageingthirty) >= range) {
                                    totalone = totalone + balance;
                                    // agReportPeriodDTO.setAgerangeone(number.format(balance));
                                } else if (Integer.parseInt(greaterthanthirty) <= range && Integer.parseInt(agingsixty) >= range) {
                                    totaltwo = totaltwo + balance;
                                    // agReportPeriodDTO.setAgerangetwo(number.format(balance));
                                } else if (Integer.parseInt(greaterthansixty) <= range && Integer.parseInt(agingninty) >= range) {
                                    totalthree = totalthree + balance;
                                    // agReportPeriodDTO.setAgerangethree(number.format(balance));
                                } else {
                                    totalfour = totalfour + balance;
                                    // agReportPeriodDTO.setAgerangefour(number.format(balance));
                                }
                                Double paymentAdjustment = transactionAmt - balance;
                                String patAdj = (String.valueOf(paymentAdjustment));
                                if (patAdj.equals("0.0")) {
                                    patAdj = "";
                                } else {
                                    patAdj = number.format(paymentAdjustment);
                                }
                                total = totalone + totaltwo + totalthree + totalfour;
                                if (null != transactionDate && !transactionDate.equals("")) {
                                    agReportPeriodDTO.setDate(simpleDateFormat.format(transactionDate));
                                } else {
                                    agReportPeriodDTO.setDate("");
                                }
                                agReportPeriodDTO.setCustName(statementVO.getCustomerName());//custName
                                agReportPeriodDTO.setCustNo(custNo);
                                agReportPeriodDTO.setCustAddress(CustAddress);
                                agReportPeriodDTO.setInvoiceNo(invoiceNumber);
                                agReportPeriodDTO.setInvoiceamt(number.format(transactionAmt));
                                agReportPeriodDTO.setPaymentoradjustment(patAdj);
                                agReportPeriodDTO.setInvoicebalance(number.format(balance));
                                agReportPeriodDTO.setAgent(aging);
                                agReportPeriodDTO.setConsigneename(consName);
                                agReportPeriodDTO.setVoyageno(transactionNo);
                                agReportPeriodDTO.setCustRefer(custrefno);
                                agingReportList.add(agReportPeriodDTO);
                            }
                            agReportPeriodDTO = null;

                        }
                    }
                }
                // this is for setting line to the report
                Double totalbal = totalone + totaltwo + totalthree + totalfour;
                String totOne = (String.valueOf(totalone));
                if (totOne.equals("0.0")) {
                    totOne = "";
                } else {
                    totOne = number.format(totalone);
                }
                String totTwo = (String.valueOf(totaltwo));
                if (totTwo.equals("0.0")) {
                    totTwo = "";
                } else {
                    totTwo = number.format(totaltwo);
                }
                String totThree = (String.valueOf(totalthree));
                if (totThree.equals("0.0")) {
                    totThree = "";
                } else {
                    totThree = number.format(totalthree);
                }
                String totFour = (String.valueOf(totalfour));
                if (totFour.equals("0.0")) {
                    totFour = "";
                } else {
                    totFour = number.format(totalfour);
                }
                AgingReportPeriodDTO totalagReportPeriodDTO = null;
                totalagReportPeriodDTO = new AgingReportPeriodDTO();
                totalagReportPeriodDTO.setCustName(statementVO.getCustomerName());//custName
                totalagReportPeriodDTO.setInvoiceNo("");
                totalagReportPeriodDTO.setDate("");
                totalagReportPeriodDTO.setInvoiceamt("");
                totalagReportPeriodDTO.setTotal(number.format(total));
                totalagReportPeriodDTO.setPaymentoradjustment("");
                totalagReportPeriodDTO.setInvoicebalance("");
                totalagReportPeriodDTO.setAgent("");
                totalagReportPeriodDTO.setAgerangeone(totOne);
                totalagReportPeriodDTO.setAgerangetwo(totTwo);
                totalagReportPeriodDTO.setAgerangethree(totThree);
                totalagReportPeriodDTO.setAgerangefour(totFour);
                if (totBalance != null && totBalance < 0) {
                    totBalance = totBalance * -1;
                }
                if (stmtWithCredit != null && !stmtWithCredit.equals("0.0") && stmtWithCredit.equals("yes")) {
                    totalagReportPeriodDTO.setStmtWithCredit("-" + number.format(totBalance));
                }
                agingReportList.add(totalagReportPeriodDTO);
                totalagReportPeriodDTO = null;
            }
            size--;
            count++;
        }
        return agingReportList;
    }
    // THIS IS FOR CUSTOMER STATEMENT REPORT FOR A SINGLE CUSTOMER

    public List forthirdpartystatement(customerStatementVO statementVO, String excludeFromStatement) throws Exception {
        String custName = statementVO.getCustomerName();
        String custNo = "'" + statementVO.getCustomerNumber() + "',";
        String ageingzeero = statementVO.getAgingzeero();
        String ageingthirty = statementVO.getAgingthirty();
        String greaterthanthirty = statementVO.getGreaterthanthirty();
        String agingsixty = statementVO.getAgingsixty();
        String greaterthansixty = statementVO.getGreaterthansixty();
        String agingninty = statementVO.getAgingninty();
        String greaterthanninty = statementVO.getGreaterthanninty();
        String overdue = statementVO.getOverdue();
        String minamt = statementVO.getMinamt();
        String stmtWithCredit = statementVO.getStmtWithCredit();
        String includeInvoiceCredit = statementVO.getIncludeInvoiceCredit();
        String includeAP = statementVO.getIncludeAP();
        String includeAccruals = statementVO.getIncludeAccruals();
        String includeNetSettlement = statementVO.getIncludeNetSettlement();
        TradingPartner tradingPartner = new TradingPartnerDAO().findById(statementVO.getCustomerNumber());
        if (null != tradingPartner && CommonUtils.isEqualIgnoreCase(tradingPartner.getType(), "master")) {
            custNo = "'" + tradingPartner.getAccountno() + "',";
            String childsOfMaster = new TradingPartnerDAO().getChilds(tradingPartner.getAccountno());
            if (CommonUtils.isNotEmpty(childsOfMaster)) {
                StringTokenizer tokenizer = new StringTokenizer(childsOfMaster, ",");
                while (tokenizer.hasMoreTokens()) {
                    custNo += "'" + tokenizer.nextToken() + "',";
                }
            }
        }
        AgingReportPeriodDTO agReportPeriodDTO = null;
        Double total = 0.00;
        Double totBalance = 0.00;
        List<AgingReportPeriodDTO> agingReportList = new ArrayList<AgingReportPeriodDTO>();
        String queryString1 = "";
        if (null != includeNetSettlement && includeNetSettlement.equals("yes")) {
            if (null != includeAP && includeAP.equals("yes") && null != includeAccruals && includeAccruals.equals("yes")) {
                queryString1 = "select trans.transactionDate,trans.invoiceNumber,trans.transactionAmt,"
                        + "trans.balance,trans.consName,trans.billLaddingNo,"
                        + "trans.transactionId,trans.customerReferenceNo,trans.bookingNo,trans.quotationNo,"
                        + "trans.transactionType,"
                        + "trans.voyageNo from Transaction trans where trans.custNo in (" + StringUtils.removeEnd(custNo, ",") + ") and (trans.transactionType='" + ReportConstants.TRANSACTIONTYPEAP + "' "
                        + "or trans.transactionType='" + ReportConstants.TRANSACTIONTYPE + "' or "
                        + "trans.transactionType='" + ReportConstants.TRANSACTIONTYPEAC + "')";


            } else if (null != includeAP && includeAP.equals("yes")) {
                queryString1 = "select trans.transactionDate,trans.invoiceNumber,trans.transactionAmt,trans.balance,trans.consName,trans.billLaddingNo,trans.transactionId,trans.customerReferenceNo,trans.bookingNo,trans.quotationNo,trans.transactionType,trans.voyageNo from Transaction trans where trans.custNo in (" + StringUtils.removeEnd(custNo, ",") + ") and (trans.transactionType='" + ReportConstants.TRANSACTIONTYPEAP + "' or trans.transactionType='" + ReportConstants.TRANSACTIONTYPE + "') ";
            } else if (null != includeAccruals && includeAccruals.equals("yes")) {
                queryString1 = "select trans.transactionDate,trans.invoiceNumber,trans.transactionAmt,trans.balance,trans.consName,trans.billLaddingNo,trans.transactionId,trans.customerReferenceNo,trans.bookingNo,trans.quotationNo,trans.transactionType,trans.voyageNo from Transaction trans where trans.custNo in (" + StringUtils.removeEnd(custNo, ",") + ") and (trans.transactionType='" + ReportConstants.TRANSACTIONTYPEAC + "' or trans.transactionType='" + ReportConstants.TRANSACTIONTYPE + "') ";
            } else {
                queryString1 = "select trans.transactionDate,trans.invoiceNumber,trans.transactionAmt,trans.balance,trans.consName,trans.billLaddingNo,trans.transactionId,trans.customerReferenceNo,trans.bookingNo,trans.quotationNo,trans.transactionType,trans.voyageNo from Transaction trans where trans.custNo in (" + StringUtils.removeEnd(custNo, ",") + ") and trans.transactionType='" + ReportConstants.TRANSACTIONTYPE + "' ";
            }
        } else if (null != includeNetSettlement && includeNetSettlement.equals("only")) {
            queryString1 = "select trans.transactionDate,trans.invoiceNumber,trans.transactionAmt,trans.balance,trans.consName,"
                    + "trans.billLaddingNo,trans.transactionId,trans.customerReferenceNo,trans.bookingNo,trans.quotationNo,"
                    + "trans.transactionType,trans.voyageNo from Transaction trans where trans.custNo in (" + StringUtils.removeEnd(custNo, ",") + ") and trans.transactionType='" + ReportConstants.TRANSACTIONTYPE + "' "
                    + "	and trans.invoiceNumber like '" + AccountingConstants.SUBLEDGER_CODE_NETSETT + "%'";
        } else {
            if (null != includeAP && includeAP.equals("yes") && null != includeAccruals && includeAccruals.equals("yes")) {
                queryString1 = "select trans.transactionDate,trans.invoiceNumber,trans.transactionAmt,trans.balance,trans.consName,trans.billLaddingNo,trans.transactionId,trans.customerReferenceNo,trans.bookingNo,trans.quotationNo,trans.transactionType,trans.voyageNo from Transaction trans where trans.custNo in (" + StringUtils.removeEnd(custNo, ",") + ") and (trans.transactionType='" + ReportConstants.TRANSACTIONTYPEAP + "' or trans.transactionType='" + ReportConstants.TRANSACTIONTYPE + "' or trans.transactionType='" + ReportConstants.TRANSACTIONTYPEAC + "') ";
            } else if (null != includeAP && includeAP.equals("yes")) {
                queryString1 = "select trans.transactionDate,trans.invoiceNumber,trans.transactionAmt,trans.balance,trans.consName,trans.billLaddingNo,trans.transactionId,trans.customerReferenceNo,trans.bookingNo,trans.quotationNo,trans.transactionType,trans.voyageNo from Transaction trans where trans.custNo in (" + StringUtils.removeEnd(custNo, ",") + ") and (trans.transactionType='" + ReportConstants.TRANSACTIONTYPEAP + "' or trans.transactionType='" + ReportConstants.TRANSACTIONTYPE + "') ";
            } else if (null != includeAccruals && includeAccruals.equals("yes")) {
                queryString1 = "select trans.transactionDate,trans.invoiceNumber,trans.transactionAmt,trans.balance,trans.consName,trans.billLaddingNo,trans.transactionId,trans.customerReferenceNo,trans.bookingNo,trans.quotationNo,trans.transactionType,trans.voyageNo from Transaction trans where trans.custNo in (" + StringUtils.removeEnd(custNo, ",") + ") and (trans.transactionType='" + ReportConstants.TRANSACTIONTYPEAC + "' or trans.transactionType='" + ReportConstants.TRANSACTIONTYPE + "') ";
            } else {
                queryString1 = "select trans.transactionDate,trans.invoiceNumber,trans.transactionAmt,trans.balance,trans.consName,trans.billLaddingNo,trans.transactionId,trans.customerReferenceNo,trans.bookingNo,trans.quotationNo,trans.transactionType,trans.voyageNo from Transaction trans where trans.custNo in (" + StringUtils.removeEnd(custNo, ",") + ") and trans.transactionType='" + ReportConstants.TRANSACTIONTYPE + "'";
            }
            queryString1 = queryString1 + "	and trans.invoiceNumber not like '" + AccountingConstants.SUBLEDGER_CODE_NETSETT + "%'";
        }
        // Oder By Transaction Date
        queryString1 += " ORDER BY  trans.transactionDate ";
        List queryObject1 = getCurrentSession().createQuery(queryString1).list();
        Iterator iter1 = queryObject1.iterator();
        Double totalone = 0.00;
        Double totaltwo = 0.00;
        Double totalthree = 0.00;
        Double totalfour = 0.00;
        Double tot = 0.00;
        while (iter1.hasNext()) {
            agReportPeriodDTO = new AgingReportPeriodDTO();
            Object[] row1 = (Object[]) iter1.next();
            Date transactionDate = (Date) row1[0];
            String invoiceNumber = (String) row1[1];
            Double transactionAmt = (Double) row1[2];
            Double balance = (Double) row1[3];
            String consName = (String) row1[4];
            String billOdLadingNo = (String) row1[5];
            Integer transactionId = (Integer) row1[6];
            String custrefno = (String) row1[7];
            String bookingNo = (String) row1[8];
            String quotationNo = (String) row1[9];
            String transactionType = (String) row1[10];
            String voyageNo = (String) row1[11];
            String quoteOrBookingNo = "";
            if (null != bookingNo && !bookingNo.equals("")) {
                quoteOrBookingNo = bookingNo;
            } else {
                quoteOrBookingNo = quotationNo;
            }
            if (invoiceNumber == null) {
                invoiceNumber = billOdLadingNo;
            }
            String invoiceOrBlNo = "";
            if (null != billOdLadingNo && !billOdLadingNo.equals("")) {
                invoiceOrBlNo = billOdLadingNo;
            } else {
                invoiceOrBlNo = invoiceNumber;
            }
            if (!excludeFromStatement.contains(transactionId.toString())) {
                if (transactionAmt == null) {
                    transactionAmt = 0.0;
                }
                String transAmt = (String.valueOf(transactionAmt));
                if (transAmt.contains(",")) {
                    transAmt = transAmt.replace(",", "");
                }
                transactionAmt = (Double.parseDouble(transAmt));
                if (balance == null) {
                    balance = 0.0;
                }
                String bal = (String.valueOf(balance));
                if (bal.contains(",")) {
                    bal = bal.replace(",", "");
                }
                balance = (Double.parseDouble(bal));
                if (null != transactionId && !transactionId.equals("")) {
                    String queryString3 = "select sum(pay.paymentAmt) from Payments pay where pay.arTranactionId='" + transactionId + "'";
                    List queryObject3 = getCurrentSession().createQuery(
                            queryString3).list();
                    Double payamt = (Double) queryObject3.get(0);
                    if (payamt == null) {
                        payamt = 0.0;
                    }
                    String payment = (String.valueOf(payamt));
                    if (payment.contains(",")) {
                        payment = payment.replace(",", "");
                    }
                    payamt = (Double.parseDouble(payment));
                    agReportPeriodDTO.setPaymentoradjustment(number.format(payamt));
                }
                if (custrefno == null) {
                    custrefno = "";
                }
                tot = tot + balance;
                String aging = "";
                long range = 0;
                if (null != transactionDate) {
                    range = this.calculateAge(transactionDate);
                    aging = String.valueOf(range);
                }

                int OverDue = 0;
                if (overdue != null) {
                    OverDue = Integer.parseInt(overdue);
                }
                Double MinAmt = 0.0;
                if (minamt != null) {
                    MinAmt = (Double.parseDouble(minamt));
                }
                /*if (balance != null && balance < 0) {
                if(transactionType.equals("AP") || transactionType.equals("AC")){
                balance= balance * -1;
                } else{
                balance=balance;
                }
                totBalance = totBalance + transactionAmt - balance;
                }*/
                if (null != includeInvoiceCredit && !includeInvoiceCredit.equals("no")) {
                    if (balance != null && balance != 0d) {
                        if (OverDue <= range) {
                            if (transactionType.equals("AP") || transactionType.equals("AC")) {
                                balance = balance * -1;
                                transactionAmt = transactionAmt * -1;
                                //totBalance = totBalance + transactionAmt - balance;
                            }
                            if (Integer.parseInt(ageingzeero) <= range && Integer.parseInt(ageingthirty) >= range) {
                                totalone = totalone + balance;
                            } else if (Integer.parseInt(greaterthanthirty) <= range && Integer.parseInt(agingsixty) >= range) {
                                totaltwo = totaltwo + balance;
                            } else if (Integer.parseInt(greaterthansixty) <= range && Integer.parseInt(agingninty) >= range) {
                                totalthree = totalthree + balance;
                            } else {
                                totalfour = totalfour + balance;
                            }
                            Double paymentAdjustment = transactionAmt - balance;
                            totBalance = totBalance + paymentAdjustment;
                            String patAdj = (String.valueOf(paymentAdjustment));
                            if (patAdj == null && patAdj.equals("0.0")) {
                                patAdj = "";
                            } else {
                                patAdj = number.format(paymentAdjustment);
                            }
                            total = totalone + totaltwo + totalthree + totalfour;
                            if (null != transactionDate && !transactionDate.equals("")) {
                                agReportPeriodDTO.setDate(sdf.format(transactionDate));
                            } else {
                                agReportPeriodDTO.setDate("");
                            }
                            agReportPeriodDTO.setInvoiceNo(invoiceNumber);
                            agReportPeriodDTO.setInvoiceamt(number.format(transactionAmt));
                            agReportPeriodDTO.setPaymentoradjustment(patAdj);
                            agReportPeriodDTO.setInvoicebalance(number.format(balance));
                            agReportPeriodDTO.setAgent(aging);
                            agReportPeriodDTO.setConsigneename(consName);
                            agReportPeriodDTO.setBlNo(invoiceOrBlNo);
                            agReportPeriodDTO.setCustRefer(custrefno);
                            agReportPeriodDTO.setTransactionId(transactionId.toString());
                            agReportPeriodDTO.setQuoteOrBookingNo(quoteOrBookingNo);
                            agReportPeriodDTO.setTransactionType(transactionType);
                            agReportPeriodDTO.setVoyageno(null != voyageNo ? voyageNo : "");
                            agingReportList.add(agReportPeriodDTO);
                        }
                        agReportPeriodDTO = null;
                    }
                } else {
                    if (balance != null && balance != 0) {
                        if (OverDue <= range && MinAmt <= balance) {
                            if (transactionType.equals("AP") || transactionType.equals("AC")) {
                                balance = balance * -1;
                                transactionAmt = transactionAmt * -1;
                                //totBalance = totBalance + transactionAmt - balance;
                            }
                            if (Integer.parseInt(ageingzeero) <= range && Integer.parseInt(ageingthirty) >= range) {
                                totalone = totalone + balance;
                            } else if (Integer.parseInt(greaterthanthirty) <= range && Integer.parseInt(agingsixty) >= range) {
                                totaltwo = totaltwo + balance;
                            } else if (Integer.parseInt(greaterthansixty) <= range && Integer.parseInt(agingninty) >= range) {
                                totalthree = totalthree + balance;
                            } else {
                                totalfour = totalfour + balance;
                            }
                            Double paymentAdjustment = transactionAmt - balance;
                            totBalance = totBalance + paymentAdjustment;
                            String patAdj = (String.valueOf(paymentAdjustment));
                            if (patAdj == null && patAdj.equals("0.0")) {
                                patAdj = "";
                            } else {
                                patAdj = number.format(paymentAdjustment);
                            }
                            total = totalone + totaltwo + totalthree + totalfour;
                            if (null != transactionDate && !transactionDate.equals("")) {
                                agReportPeriodDTO.setDate(sdf.format(transactionDate));
                            } else {
                                agReportPeriodDTO.setDate("");
                            }
                            agReportPeriodDTO.setInvoiceNo(invoiceNumber);
                            agReportPeriodDTO.setCustName(custName);
                            agReportPeriodDTO.setInvoiceamt(number.format(transactionAmt));
                            agReportPeriodDTO.setPaymentoradjustment(patAdj);
                            agReportPeriodDTO.setInvoicebalance(number.format(balance));
                            agReportPeriodDTO.setAgent(aging);
                            agReportPeriodDTO.setConsigneename(consName);
                            agReportPeriodDTO.setBlNo(invoiceOrBlNo);
                            agReportPeriodDTO.setCustRefer(custrefno);
                            agReportPeriodDTO.setTransactionId(transactionId.toString());
                            agReportPeriodDTO.setQuoteOrBookingNo(quoteOrBookingNo);
                            agReportPeriodDTO.setTransactionType(transactionType);
                            agReportPeriodDTO.setVoyageno(null != voyageNo ? voyageNo : "");
                            agingReportList.add(agReportPeriodDTO);
                        }
                        agReportPeriodDTO = null;
                    }
                }
            }
        }
        // this is for setting line to the report
        Double totalbal = totalone + totaltwo + totalthree + totalfour;
        String totOne = "";
        if (totalone != null && totalone != 0.0) {
            totOne = number.format(totalone);
        }
        String totTwo = "";
        if (totaltwo != null && totaltwo != 0.0) {
            totTwo = number.format(totaltwo);
        }
        String totThree = "";
        if (totalthree != null && totalthree != 0.0) {
            totThree = number.format(totalthree);
        }
        String totFour = "";
        if (totalfour != null && totalfour != 0.0) {
            totFour = number.format(totalfour);
        }

        AgingReportPeriodDTO totalagReportPeriodDTO = null;
        totalagReportPeriodDTO = new AgingReportPeriodDTO();
        totalagReportPeriodDTO.setInvoiceNo("");
        totalagReportPeriodDTO.setDate("");
        totalagReportPeriodDTO.setInvoiceamt("");
        totalagReportPeriodDTO.setTotal(number.format(total));
        totalagReportPeriodDTO.setPaymentoradjustment("");
        totalagReportPeriodDTO.setInvoicebalance("");
        totalagReportPeriodDTO.setAgent("");
        totalagReportPeriodDTO.setAgerangeone(totOne);
        totalagReportPeriodDTO.setAgerangetwo(totTwo);
        totalagReportPeriodDTO.setAgerangethree(totThree);
        totalagReportPeriodDTO.setAgerangefour(totFour);
        if (stmtWithCredit != null && !stmtWithCredit.equals("0.0") && stmtWithCredit.equals("yes")) {
            totalagReportPeriodDTO.setStmtWithCredit(number.format(totBalance));
        }
        agingReportList.add(totalagReportPeriodDTO);
        totalagReportPeriodDTO = null;
        return agingReportList;
    }

    public String getInvOrBLNumberForTransaction(String transactionId) throws Exception {
        String result = "";
        Transaction transaction = findById(Integer.parseInt(transactionId));
        if (transaction != null) {
            if (transaction.getInvoiceNumber() != null) {
                result = transaction.getInvoiceNumber();
            } else if (transaction.getBillLaddingNo() != null) {
                result = transaction.getBillLaddingNo();
            } else {
                result = "";
            }
        }
        return result;
    }

    public String getLastBatchIdForPayment() throws Exception {
        String queryString = "select batch_id from ap_batch order by batch_id desc limit 1";
        Query queryObject = getCurrentSession().createSQLQuery(queryString);
        Object BatchId = queryObject.uniqueResult();
        if (null != BatchId) {
            return BatchId.toString();
        } else {
            return "";
        }
    }

    public String getCreditTerm(String accountNumber, Integer creditTerm) throws Exception {
        List creditDetails = customerAccountingDAO.getCreditDetailsByCreditTerms(accountNumber, creditTerm);
        String creditLimit = "";
        String creditTerms = "";
        if (creditDetails != null && !creditDetails.isEmpty()) {
            if (creditDetails.get(0).toString() != null) {
                creditLimit = creditDetails.get(0).toString();
            }
            if (creditDetails.get(1).toString() != null) {
                creditTerms = creditDetails.get(1).toString();
            }
        }
        return creditTerms;
    }

    public String calculateDueDate(String accountNumber, String invoiceDate) throws Exception {
        List creditDetails = customerAccountingDAO.getCreditDetails(accountNumber);
        String creditLimit = "";
        String creditTerms = "";
        String dueDate = "";
        if (creditDetails != null && !creditDetails.isEmpty()) {
            if (creditDetails.get(0).toString() != null) {
                creditLimit = creditDetails.get(0).toString();
            }
            if (creditDetails.get(1).toString() != null) {
                creditTerms = creditDetails.get(1).toString();
            }
        }
        if (null != invoiceDate && !invoiceDate.trim().equals("")) {
            Date convertedDate = sdf.parse(invoiceDate);
            int daysToAdd = -1;
            if (creditTerms != null && creditTerms.trim().equalsIgnoreCase("NET 15 DAYS")) {
                daysToAdd = 15;
            } else if (creditTerms != null && creditTerms.trim().equalsIgnoreCase("NET 30 DAYS")) {
                daysToAdd = 30;
            } else if (creditTerms != null && creditTerms.trim().equalsIgnoreCase("NET 45 DAYS")) {
                daysToAdd = 45;
            } else if (creditTerms != null && creditTerms.trim().equalsIgnoreCase("NET 60 DAYS")) {
                daysToAdd = 60;
            } else if (creditTerms != null && creditTerms.trim().equalsIgnoreCase("NET 7 DAYS")) {
                daysToAdd = 7;
            } else if (creditTerms != null && creditTerms.trim().equalsIgnoreCase("")) {
                daysToAdd = 0;
            }
            if (daysToAdd >= 0) {
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(convertedDate);
                calendar.add(Calendar.DAY_OF_MONTH, daysToAdd);
                dueDate = sdf.format(calendar.getTime());
            }
        }
        return dueDate;
    }

    public long calculateAge(Date date) throws Exception {
        Calendar yesterday = Calendar.getInstance();
        yesterday.setTime(date);
        Calendar today = Calendar.getInstance();
        if (today.compareTo(yesterday) >= 0) {
            return (today.getTimeInMillis() - yesterday.getTimeInMillis()) / (24 * 60 * 60 * 1000);
        } else {
            return -1;
        }
    }

    public String getTransactionIdsForVoid(Transaction transaction) {
        String transactionIds = "";
        String queryString = "select group_concat(transaction_id) as transactionIds from transaction where Cust_no='" + transaction.getCustNo() + "'"
                + " and ((transaction_type='" + TRANSACTION_TYPE_ACCOUNT_PAYABLE + "' and transaction_amt='" + transaction.getTransactionAmt() + "')"
                + " or (transaction_type='" + TRANSACTION_TYPE_ACCOUNT_RECEIVABLE + "'))"
                + " and status='" + STATUS_PAID + "' and Ap_Batch_Id='" + transaction.getApBatchId() + "' and bank_name='" + transaction.getBankName() + "'"
                + " and bank_account_no='" + transaction.getBankAccountNumber() + "'"
                + " and balance=0 and balance_in_process=0"
                + " and pay_method='" + transaction.getPaymentMethod() + "'";
        if (CommonUtils.isNotEmpty(transaction.getInvoiceNumber())) {
            queryString += " and invoice_number='" + transaction.getInvoiceNumber() + "'";
        }
        if (CommonUtils.isNotEmpty(transaction.getBillLaddingNo())) {
            queryString += " and bill_ladding_no='" + transaction.getBillLaddingNo() + "'";
        }
        Query query = getSession().createSQLQuery(queryString).addScalar("transactionIds", StringType.INSTANCE);
        Object object = query.uniqueResult();
        if (null != object) {
            transactionIds = object.toString();
        }
        return transactionIds;
    }

    public List<TransactionBean> getPaymentsListByBatchID(Integer apBatchId, String transactionType, String status) {
        List<TransactionBean> transactionBeanList = new ArrayList<TransactionBean>();
        String queryString = " from Transaction where apBatchId=" + apBatchId
                + " and status='" + STATUS_PAID + "' and transactionType='" + transactionType + "' and balance=0 and balanceInProcess=0";
        Query query = getCurrentSession().createQuery(queryString);
        List<Transaction> transactionList = query.list();
        TransactionBean transactionBean = null;
        for (Transaction transaction : transactionList) {
            transactionBean = new TransactionBean(transaction);
            transactionBeanList.add(transactionBean);
        }
        return transactionBeanList;
    }

    public List getMoreInfoList(TransactionBean transBean) throws Exception {
        String queryString = "select tl.posted_date,fp.Period_Dis,tl.transaction_amt from transaction_ledger tl,fiscal_period fp where (fp.Start_Date<=tl.posted_date and fp.End_Date>=tl.posted_date)" + " and tl.Subledger_Source_code='" + SUB_LEDGER_CODE_PURCHASE_JOURNAL + "'"
                + " and tl.Cust_no='" + transBean.getCustomerNo() + "' and invoice_number='" + transBean.getInvoiceOrBl() + "'";
        Query query = getCurrentSession().createSQLQuery(queryString);
        List<TransactionBean> moreInfoList = new ArrayList<TransactionBean>();
        Iterator iter = query.list().iterator();
        TransactionBean transactionBean = null;
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            transactionBean = new TransactionBean();
            if (null != row[0]) {
                Date postedDate = df.parse(row[0].toString());
                transactionBean.setPostedDate(sdf.parse(sdf.format(postedDate)));
            }
            transactionBean.setGlPeriod(null != row[1] ? row[1].toString() : "");
            transactionBean.setAmount(null != row[2] ? number.format(Double.parseDouble(row[2].toString())) : "");
            moreInfoList.add(transactionBean);
            transactionBean = null;
        }
        return moreInfoList;
    }

    public String getApContactForVendor(String vendorNumber) throws Exception {
        StringBuilder queryBuilder = new StringBuilder("select email from vendor_info");
        queryBuilder.append(" where cust_accno='").append(vendorNumber).append("' limit 1");
        String result = (String) getSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return result;
    }

    public String getLclARBalance(String fileNo, String concatFileNo) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT FORMAT(SUM(IFNULL(transaction_amount+adjustment_amount,0)),2) AS amt FROM ar_transaction_history ");
        sb.append("WHERE ");
        sb.append("(invoice_number='").append(fileNo).append("' OR bl_number='").append(concatFileNo).append("')");
        return (String) getSession().createSQLQuery(sb.toString()).uniqueResult();
    }

    public String getLclARBalanceExports(String fileNo, String concatFileNo, String concatFileNoII) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT FORMAT(SUM(IFNULL(transaction_amount+adjustment_amount,0)),2) AS amt FROM ar_transaction_history ");
        sb.append("WHERE (bl_number like '").append(concatFileNo).append("%'");
        sb.append(" OR bl_number like '").append(concatFileNoII).append("%'");
        sb.append(" OR invoice_number='").append(fileNo).append("')");
        return (String) getSession().createSQLQuery(sb.toString()).uniqueResult();
    }

    public double getTransactionAmt(String customerNumber, String blNumber, String transactionType) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  if(sum(transaction_amt) <> 0, sum(transaction_amt), 0) as amount ");
        queryBuilder.append("from");
        queryBuilder.append("  transaction ");
        queryBuilder.append("where");
        queryBuilder.append("  cust_no = '").append(customerNumber).append("'");
        queryBuilder.append("  and bill_ladding_no = '").append(blNumber).append("'");
        queryBuilder.append("  and transaction_type = '").append(transactionType).append("'");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("amount", DoubleType.INSTANCE);
        return (Double) query.uniqueResult();
    }

    public void updateVoyageNoByLcl(String fileNo, Date sailDate, String newScheNo,
            String vesselNo, Date etaDate, String vesselName, String carrierName, Integer userId, String masterBkgNo) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" UPDATE transaction set saling_date =:sailDate,voyage_no=:newScheNo,transaction_date=:sailDate,");
        queryStr.append("vessel_no=:vesselNo,eta=:etaDate,vessel_name=:vesselName,steam_ship_line=:carrierName,");
        queryStr.append("updated_on=:updateDate,updated_by=:userId ");
        queryStr.append(" where drcpt=:fileNo ");
        SQLQuery queryObject = getSession().createSQLQuery(queryStr.toString());
        queryObject.setString("fileNo", fileNo);
        queryObject.setParameter("newScheNo", newScheNo);
        queryObject.setParameter("sailDate", sailDate);
        queryObject.setParameter("etaDate", etaDate);
        queryObject.setParameter("vesselNo", vesselNo);
        queryObject.setParameter("vesselName", vesselName);
        queryObject.setParameter("carrierName", carrierName);
        queryObject.setParameter("updateDate", new Date());
        queryObject.setParameter("userId", userId);
        queryObject.executeUpdate();
    }

    public void updateLclEContainers(String fileNo, String voyageNo, String containerNo) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update transaction ");
        queryBuilder.append("set container_no  =:containerNo ");
        queryBuilder.append(" where drcpt=:fileNo ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setParameter("containerNo", containerNo);
        query.setParameter("fileNo", fileNo);
        query.executeUpdate();
    }

    public void updateLclEBlNumber(String blNumber, String fileNo) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" update transaction set Bill_Ladding_No=:blNumber where ");
        queryBuilder.append(" Invoice_number=:fileNo and Transaction_type='AR'");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setParameter("blNumber", blNumber);
        query.setParameter("fileNo", fileNo);
        query.executeUpdate();
        getCurrentSession().getTransaction().commit();
        getCurrentSession().getTransaction().begin();
    }
     public Boolean ValidateLclChargeStatus(String fileNumber) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT IF(COUNT(*) >0,TRUE, FALSE) as result ");
        queryStr.append(" FROM TRANSACTION t  ");
        queryStr.append(" JOIN `lcl_file_number` lf ON(lf.`file_number` = t.`drcpt`) ");
        queryStr.append(" WHERE t.`Transaction_amt` <> t.`Balance` ");
        queryStr.append(" AND lf.`status`='M' AND t.`drcpt`=:fileNumber");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(queryStr.toString());
        queryObject.setParameter("fileNumber", fileNumber);
        queryObject.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) queryObject.uniqueResult();
    }    
}
