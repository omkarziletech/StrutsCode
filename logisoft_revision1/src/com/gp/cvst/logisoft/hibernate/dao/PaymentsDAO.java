package com.gp.cvst.logisoft.hibernate.dao;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.Example;

import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.AccountingConstants;
import com.gp.cvst.logisoft.beans.AdjustmentBean;
import com.gp.cvst.logisoft.beans.TransactionBean;
import com.gp.cvst.logisoft.domain.PaymentChecks;
import com.gp.cvst.logisoft.domain.Payments;
import com.logiware.bean.ArTransactionBean;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * Data access object (DAO) for domain model class Payments.
 *
 * @see com.gp.cvst.logisoft.domain.Payments
 * @author MyEclipse - Hibernate Tools
 */
public class PaymentsDAO extends BaseHibernateDAO implements ConstantsInterface {

    //property constants
    public static final String BATCH_ID = "batchId";
    public static final String CUST_NO = "custNo";
    public static final String PAYMENT_AMT = "paymentAmt";
    public static final String PAYMENT_TYPE = "paymentType";
    public static final String CHARGE_CODE = "chargeCode";
    public static final String INVOICE_NO = "invoiceNo";
    public static final String BILL_LADDING_NO = "billLaddingNo";
    public static final String ADJUSTMENT_AMT = "adjustmentAmt";

    public void save(Payments payments) throws Exception {
        getSession().flush();
        getSession().saveOrUpdate(payments);
        getSession().flush();
    }

    public void update(Payments payments) throws Exception {
        getSession().update(payments);
        getSession().flush();
    }

    public void delete(Payments persistentInstance) throws Exception {
        getSession().delete(persistentInstance);
        getSession().flush();
    }

    public Payments findById(java.lang.Integer id) throws Exception {
        Payments instance = (Payments) getSession().get("com.gp.cvst.logisoft.domain.Payments", id);
        return instance;
    }

    public List findByExample(Payments instance) throws Exception {
        List results = getSession().createCriteria("com.gp.cvst.logisoft.domain.Payments").add(Example.create(instance)).list();
        return results;
    }

    public List findByProperty(String propertyName, Object value) throws Exception {
        String queryString = "from Payments as model where model." + propertyName + "= ?0";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", value);
        return queryObject.list();
    }

    public Payments merge(Payments detachedInstance) throws Exception {
        Payments result = (Payments) getSession().merge(detachedInstance);
        return result;
    }

    public void attachDirty(Payments instance) throws Exception {
        getSession().saveOrUpdate(instance);
        getSession().flush();
    }

    public void attachClean(Payments instance) throws Exception {
        getSession().buildLockRequest(LockOptions.NONE).lock(instance);
    }
    static SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

    public List paymentsBySummerisingOnCheckNo(String transId) {

        return null;
    }

    public double sumof(String invoice) throws Exception {
        String queryString = "select sum(px.paymentAmt) from Payments px where px.invoiceNo='" + invoice + "'";
        Object result = getCurrentSession().createQuery(queryString).setMaxResults(1).uniqueResult();
        return null != result ? Double.parseDouble(result.toString()): 0.0;
    }

    public List showforchargecode(String transid) throws Exception {
        List<AdjustmentBean> adjustmentList = new ArrayList<AdjustmentBean>();
        AdjustmentBean adjustbean = null;
        String queryString = "";
        queryString = "select px.chargeCode,px.adjustmentAmt,px.check_no,px.batchId,px.adjustmentDate,"
                + "px.userName from Payments px where px.transactionId='" + transid + "'";

        List queryObject = getCurrentSession().createQuery(queryString).list();
        Iterator itr = queryObject.iterator();
        while (itr.hasNext()) {
            adjustbean = new AdjustmentBean();
            Object[] row = (Object[]) itr.next();
            String chargeCode = (String) (row[0]);
            Double amount = (Double) (row[1]);
            String chequeNumber = (String) (row[2]);
            Integer batchId = (Integer) (row[3]);
            Date adjustmentDate = (Date) row[4];
            String userName = (String) row[5];
            adjustbean.setAdjamount(amount);
            if (adjustmentDate != null) {
                adjustbean.setAdjustmentDate(sdf.format(adjustmentDate));
            }
            adjustbean.setUserName(userName);
            adjustbean.setChargeCode(chargeCode);
            adjustbean.setChecknumber(chequeNumber);
            adjustbean.setBatchId(batchId);

            adjustmentList.add(adjustbean);
            adjustbean = null;

        }

        return adjustmentList;
    }// end for the list

    public List findByPaymentByTranIDAndBatchId(Integer batchId, String check_no, Integer checkId) throws Exception {
        String queryString = "from Payments  where batchId=?0 and check_no=?1 and paymentCheckId =?2";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", batchId);
        queryObject.setParameter("1", check_no);
        queryObject.setParameter("2", checkId);
        return queryObject.list();
    }

    public List transactions(String batchId) throws Exception {
        String query = "select Distinct(transactionId) from Payments p where p.batchId=" + batchId;
        List transactionsList = getCurrentSession().createQuery(query).list();
        return transactionsList;
    }

    public Double sumOfPaymentsInBatchForTransaction(String batchId, String transId) throws Exception {
        String paymentQueryString = "select sum(p.paymentAmt)+sum(adjustmentAmt) from Payments p where p.batchId=" + batchId + " and p.transactionId='" + transId + "'";
        Object result = getCurrentSession().createQuery(paymentQueryString).setMaxResults(1).uniqueResult();
        return null != result ? Double.parseDouble(result.toString())  : 0d;
    }

    public List getPaymentWithBatchNoCheckNoandTransId(Integer batchNo, String checkNo, String transId, Integer paymentCheckId) throws Exception {
        List<Payments> paymentList = new ArrayList<Payments>();
        String query = "from Payments p where p.batchId=" + batchNo;
        if (transId != null && !transId.equals("")) {
            if (transId.contains("ACAC")) {
                transId = transId.replace("ACAC", "");
                transId += CommonConstants.TRANSACTION_TYPE_ACCRUALS;
            }
            query += " and p.transactionId='" + transId + "'";
        } else {
            query += " and p.transactionId is null";
        }
        query += " and p.paymentCheckId=" + paymentCheckId;
        paymentList = getCurrentSession().createQuery(query).list();
        return paymentList;
    }

    public List getPaymentsForCheck(Integer batchId, Integer paymentCheckId, String paymentType, String invoiceNo) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(Payments.class);
        criteria.add(Restrictions.eq("batchId", batchId));
        criteria.add(Restrictions.eq("paymentCheckId", paymentCheckId));
        if (null != paymentType) {
            criteria.add(Restrictions.eq("paymentType", paymentType));
        }
        if (null != invoiceNo) {
            criteria.add(Restrictions.eq("invoiceNo", invoiceNo));
        }
        criteria.add(Restrictions.ne("paymentAmt", 0d));
        return criteria.list();
    }

    public void removePayments(Integer batchId, String paymentCheckId, Integer userId) {
        StringBuilder queryBuilder = new StringBuilder("update transaction_ledger tl,payments p");
        queryBuilder.append(" set tl.Status='").append(STATUS_OPEN).append("',tl.Balance_In_Process=tl.Balance,");
        queryBuilder.append(" tl.Updated_By=").append(userId).append(",tl.Updated_On=sysdate()");
        queryBuilder.append(" where p.batch_id=").append(batchId).append(" and p.Payment_Check_Id=").append(paymentCheckId);
        queryBuilder.append(" and p.transaction_type = '").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append(" and tl.Transaction_Id = p.transaction_id");
        queryBuilder.append(" and tl.Status='").append(STATUS_OPEN).append("'");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        queryBuilder = new StringBuilder("update transaction tr,payments p");
        queryBuilder.append(" set tr.Status='").append(STATUS_OPEN).append("',tr.Balance_In_Process=tr.Balance,");
        queryBuilder.append(" tr.Updated_By=").append(userId).append(",tr.Updated_On=sysdate()");
        queryBuilder.append(" where p.batch_id=").append(batchId).append(" and p.Payment_Check_Id=").append(paymentCheckId);
        queryBuilder.append(" and p.transaction_type = '").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("'");
        queryBuilder.append(" and tr.Transaction_Id = p.transaction_id");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        queryBuilder = new StringBuilder("update transaction tr,payments p");
        queryBuilder.append(" set tr.Status='").append(STATUS_OPEN).append("',tr.Balance_In_Process=tr.Balance_In_Process+p.Payment_Amt+p.Adjustment_Amt,");
        queryBuilder.append(" tr.Updated_By=").append(userId).append(",tr.Updated_On=sysdate()");
        queryBuilder.append(" where p.batch_id=").append(batchId).append(" and p.Payment_Check_Id=").append(paymentCheckId);
        queryBuilder.append(" and p.transaction_type = '").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
        queryBuilder.append(" and tr.Transaction_Id = p.transaction_id");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        queryBuilder = new StringBuilder("delete from payments where batch_id=").append(batchId).append(" and Payment_Check_Id=").append(paymentCheckId);
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    /**
     * @param batchNo
     * @param checkNo
     * @return only On Account payment for a Check
     */
    public List<Payments> getPaymentWithBatchNoCheckNoForOnAccount(Integer batchNo, String checkNo, Integer paymentCheckId) {
        List<Payments> paymentList = new ArrayList<Payments>();
        String query = "from Payments p where p.check_no='" + checkNo + "' and"
                + " p.invoiceNo='" + AccountingConstants.ON_ACCOUNT + "' and p.batchId=" + batchNo;
        if (null != paymentCheckId) {
            query = query + " and p.paymentCheckId=" + paymentCheckId;
        }
        paymentList = getCurrentSession().createQuery(query).list();
        return paymentList;
    }

    /**
     * @param batchNo
     * @param checkNo
     * @param transId
     * @return (Used only in case of Inserting other Entry(like OnAcct,PrePay or
     * Invoice)
     */
    public List getPaymentWithBatchNoCheckNoAsPaymentTypeandTransId(Integer batchNo, String checkNo, String transId) {
        List<Payments> paymentList = new ArrayList<Payments>();
        String query = "from Payments p where p.paymentType='" + checkNo + "' and p.batchId=" + batchNo;
        if (transId != null && !transId.equals("")) {
            query = query + " and p.transactionId='" + transId + "'";
        } else {
            query = query + " and p.transactionId is null";
        }
        paymentList = getCurrentSession().createQuery(query).list();
        return paymentList;
    }

    public List distinctCustomersFromPaymentsForBatch(String batchId) {
        String query = "select Distinct(custNo) from Payments p where p.batchId=" + batchId;
        List transactionsList = getCurrentSession().createQuery(query).list();
        return transactionsList;
    }

    public List<Payments> getPaymentsWithBatchNoCheckNoandAdjamt(String batchNo, String checkNo) {
        List<Payments> paymentList = new ArrayList<Payments>();
        String query = " from Payments p where  p.batchId='" + batchNo + "' and p.check_no='" + checkNo + "' and p.adjustmentAmt<>0";
        paymentList = getCurrentSession().createQuery(query).list();
        return paymentList;
    }

    public List<TransactionBean> getPaymentDetailsForApInquiry(String transactionId) {
        NumberFormat number = new DecimalFormat("###,###,##0.00");
        List<TransactionBean> returnList = new ArrayList<TransactionBean>();
        String queryString = "from Payments where transactionId = " + transactionId;
        Query query = getCurrentSession().createQuery(queryString);
        List<Payments> paymentList = query.list();
        for (Payments payments : paymentList) {
            TransactionBean transactionBean = new TransactionBean();
            transactionBean.setChequenumber(null != payments.getBatchId() ? payments.getBatchId().toString() : "");
            transactionBean.setAmount(null != payments.getPaymentAmt() ? number.format(payments.getPaymentAmt()) : "");
            transactionBean.setCheckDate(null != payments.getBatchDate() ? DateUtils.parseDateToString(payments.getBatchDate()) : "");
            returnList.add(transactionBean);
        }
        return returnList;
    }

    public String getPaymentChecksByBatchAndTransactionId(Integer batchId, String transactionId) throws Exception {
        String queryString = "select paymentCheckId from Payments where batchId=" + batchId + " and transactionId = " + transactionId;
        Integer paymentChecksId = (Integer) getSession().createQuery(queryString).uniqueResult();
        if (null != paymentChecksId) {
            PaymentChecks paymentChecks = new PaymentChecksDAO().findById(paymentChecksId);
            return null != paymentChecks ? paymentChecks.getCheckNo() : "";
        } else {
            return null;
        }
    }

    public List<String> getAccruals(Integer batchId, String paymentCheckId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder("select cast(group_concat(tl.Transaction_Id) as char)");
        queryBuilder.append(" from transaction_ledger tl join payments p on p.batch_id=").append(batchId);
        queryBuilder.append(" and p.transaction_type = '").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append(" and tl.Transaction_Id = p.transaction_id");
        queryBuilder.append(" and p.Payment_Amt<>0 and p.Payment_Check_Id=").append(paymentCheckId);
        queryBuilder.append(" group by tl.Invoice_number order by tl.Transaction_Id");
        return getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
    }

    public double getAccrualPaidAmount(Integer batchId, String paymentCheckId, Integer transactionId) {
        StringBuilder queryBuilder = new StringBuilder("select Payment_Amt");
        queryBuilder.append(" from payments p where p.batch_id=").append(batchId);
        queryBuilder.append(" and p.transaction_type = '").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append(" and p.transaction_id = ").append(transactionId);
        queryBuilder.append(" and p.Payment_Check_Id=").append(paymentCheckId);
        BigDecimal result = (BigDecimal) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return result.doubleValue();
    }

    public List<String> getApTransactions(Integer batchId) {
        StringBuilder queryBuilder = new StringBuilder("select cast(tr.Transaction_Id as char)");
        queryBuilder.append(" from transaction tr join payments p on p.batch_id=").append(batchId);
        queryBuilder.append(" and p.transaction_type = '").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("'");
        queryBuilder.append(" and tr.Transaction_Id = p.transaction_id");
        queryBuilder.append(" and p.Payment_Amt<>0");
        queryBuilder.append(" and tr.Transaction_type='").append(CommonConstants.TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("'");
        queryBuilder.append(" order by tr.Transaction_Id ");
        return getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
    }

    public List<Payments> getArTransactions(Integer batchId) {
        StringBuilder queryBuilder = new StringBuilder("select p from Payments p,Transaction tr where p.batchId=").append(batchId);
        queryBuilder.append(" and p.transactionType = '").append(CommonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
        queryBuilder.append(" and tr.transactionId = p.transactionId and p.paymentAmt<>0 and p.paymentType='P'");
        queryBuilder.append(" and tr.transactionType='").append(CommonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
        queryBuilder.append(" order by tr.transactionId");
        return getCurrentSession().createQuery(queryBuilder.toString()).list();
    }

    public List<Payments> getOnAccounts(Integer batchId) {
        Criteria criteria = getCurrentSession().createCriteria(Payments.class);
        criteria.add(Restrictions.eq("batchId", batchId));
        criteria.add(Restrictions.isNull("transactionId"));
        criteria.add(Restrictions.eq("invoiceNo", AccountingConstants.ON_ACCOUNT));
        criteria.add(Restrictions.ne("paymentAmt", 0d));
        criteria.add(Restrictions.eq("paymentType", "Check"));
        return criteria.list();
    }

    public List<Payments> getPrepayments(Integer batchId) {
        Criteria criteria = getCurrentSession().createCriteria(Payments.class);
        criteria.add(Restrictions.eq("batchId", batchId));
        criteria.add(Restrictions.isNull("transactionId"));
        criteria.add(Restrictions.isNotNull("billLaddingNo"));
        criteria.add(Restrictions.eq("invoiceNo", AccountingConstants.PRE_PAYMENT));
        criteria.add(Restrictions.ne("paymentAmt", 0d));
        criteria.add(Restrictions.eq("paymentType", "Check"));
        return criteria.list();
    }

    public List<Payments> getChargeCodes(Integer batchId) {
        Criteria criteria = getCurrentSession().createCriteria(Payments.class);
        criteria.add(Restrictions.eq("batchId", batchId));
        criteria.add(Restrictions.isNull("transactionId"));
        criteria.add(Restrictions.isNotNull("chargeCode"));
        criteria.add(Restrictions.eq("invoiceNo", AccountingConstants.CHARGE_CODE));
        criteria.add(Restrictions.ne("paymentAmt", 0d));
        criteria.add(Restrictions.eq("paymentType", "Check"));
        return criteria.list();
    }

    public List<Payments> getAdjustments(Integer batchId) {
        Criteria criteria = getCurrentSession().createCriteria(Payments.class);
        criteria.add(Restrictions.eq("batchId", batchId));
        criteria.add(Restrictions.ne("adjustmentAmt", 0d));
        criteria.add(Restrictions.isNotNull("chargeCode"));
        criteria.add(Restrictions.isNotNull("transactionId"));
        criteria.add(Restrictions.eq("paymentType", "P"));
        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    public List<ArTransactionBean> getArPayments(Integer transactionId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder("select ab.batchId,ab.depositDate,p.paymentAmt,p.adjustmentAmt");
        queryBuilder.append(" ,p.adjustmentDate,p.chargeCode,p.check_no");
        queryBuilder.append(" ,p.userName from Payments p,ArBatch ab");
        queryBuilder.append(" where ab.batchId = p.batchId and p.transactionId = '").append(transactionId).append("'");
        queryBuilder.append(" and (ab.status = '").append(STATUS_CLOSED).append("'");
        queryBuilder.append(" or ab.status = '").append(STATUS_RECONCILE_IN_PROGRESS).append("'");
        queryBuilder.append(" or ab.status = '").append(CLEAR).append("')");
        queryBuilder.append(" order by ab.depositDate");
        List<ArTransactionBean> arPayments = new ArrayList<ArTransactionBean>();
        List<Object> result = getSession().createQuery(queryBuilder.toString()).list();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            ArTransactionBean arTransactionBean = new ArTransactionBean();
            arTransactionBean.setBatchId(col[0].toString());
            arTransactionBean.setTransactionDate((Date) col[1]);
            arTransactionBean.setTransactionAmount((Double) col[2]);
            arTransactionBean.setAdjustmentAmount((Double) col[3]);
            arTransactionBean.setAdjustmentDate((Date) col[4]);
            arTransactionBean.setGlAccount((String) col[5]);
            arTransactionBean.setCheckNo((String) col[6]);
            arTransactionBean.setUserName((String) col[7]);
            if (null != arTransactionBean.getAdjustmentAmount() && arTransactionBean.getAdjustmentAmount() != 0d) {
                arTransactionBean.setTransactionType("ADJ");
            } else {
                arTransactionBean.setTransactionType("PY");
            }
            arPayments.add(arTransactionBean);
        }
        return arPayments;
    }

    @SuppressWarnings("unchecked")
    public List<ArTransactionBean> getArAdjustments(Integer transactionId) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(Payments.class);
        criteria.add(Restrictions.eq("transactionId", transactionId.toString()));
        criteria.add(Restrictions.eq("check_no", AccountingConstants.ADJ_CHECK_NO));
        criteria.add(Restrictions.isNull("batchId"));
        criteria.add(Restrictions.isNull("paymentCheckId"));
        criteria.addOrder(Order.asc("adjustmentDate"));
        List<ArTransactionBean> arAdjustments = new ArrayList<ArTransactionBean>();
        List<Payments> result = criteria.list();
        for (Payments payments : result) {
            ArTransactionBean arTransactionBean = new ArTransactionBean();
            arTransactionBean.setBatchId(null);
            arTransactionBean.setTransactionDate(payments.getAdjustmentDate());
            arTransactionBean.setTransactionAmount(0d);
            arTransactionBean.setAdjustmentAmount(payments.getAdjustmentAmt());
            arTransactionBean.setAdjustmentDate(payments.getAdjustmentDate());
            arTransactionBean.setGlAccount(payments.getChargeCode());
            arTransactionBean.setCheckNo(payments.getCheck_no());
            arTransactionBean.setUserName(payments.getUserName());
            arTransactionBean.setTransactionType("ADJ");
            arAdjustments.add(arTransactionBean);
        }
        return arAdjustments;
    }

    public Double getPaymentAmountFromPayments(Integer batchid) throws Exception {
        String query = "select sum(payment_amt) from payments where batch_id=" + batchid;
        getCurrentSession().flush();
        Object result = getCurrentSession().createSQLQuery(query).setMaxResults(1).uniqueResult();
        return null == result ? 0d : Double.parseDouble(result.toString());
    }

    public Double getOnAccountAmountFromPayments(Integer batchid) {
        String query = "select sum(payment_amt) from payments where batch_id=" + batchid;
        query += " and invoice_no='" + AccountingConstants.ON_ACCOUNT + "' and payment_type='Check'";
        getCurrentSession().flush();
        Object result = getCurrentSession().createSQLQuery(query).uniqueResult();
        return null == result ? 0d : Double.parseDouble(result.toString());
    }

    public Double getPrePaymentAmountFromPayments(Integer batchid) {
        String query = "select sum(payment_amt) from payments where batch_id=" + batchid;
        query += " and invoice_no='" + AccountingConstants.PRE_PAYMENT + "' and payment_type='Check'";
        getCurrentSession().flush();
        Object result = getCurrentSession().createSQLQuery(query).uniqueResult();
        return null == result ? 0d : Double.parseDouble(result.toString());
    }

    public Double getAdjustAmountFromPayments(Integer batchid) {
        String query = "select sum(adjustment_amt) from payments where batch_id=" + batchid + " and payment_type='P' and adjustment_amt!=0";
        getCurrentSession().flush();
        Object result = getCurrentSession().createSQLQuery(query).uniqueResult();
        return null == result ? 0d : Double.parseDouble(result.toString());
    }

    public List<ArTransactionBean> getUnPostedPayments(Integer transactionId) {
        List<ArTransactionBean> unpostedPayments = new ArrayList<ArTransactionBean>();
        StringBuilder queryBuilder = new StringBuilder("select ab.Batch_id,p.Check_no,ab.Deposit_date,p.Payment_Amt,");
        queryBuilder.append("'AR PY' as type,p.Charge_Code,p.Adjustment_Amt,p.userName");
        queryBuilder.append(" from payments p join ar_batch ab where ab.Batch_id=p.Batch_Id");
        queryBuilder.append(" and ab.Status='").append(STATUS_OPEN).append("' and p.Payment_Amt<>0");
        queryBuilder.append(" and p.transaction_type = '").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
        queryBuilder.append(" and p.transaction_id = '").append(transactionId).append("'");
        List result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            ArTransactionBean arTransactionBean = new ArTransactionBean();
            arTransactionBean.setBatchId(col[0].toString());
            arTransactionBean.setCheckNo((String) col[1]);
            Date date = null != col[2] ? (Date) col[2] : null;
            arTransactionBean.setTransactionDate(date);
            arTransactionBean.setTransactionAmount(null != col[3] ? -Double.parseDouble(col[3].toString()) : 0d);
            arTransactionBean.setTransactionType((String) col[4]);
            arTransactionBean.setGlAccount((String) col[5]);
            Double adjustmentAmount = null != col[6] ? -Double.parseDouble(col[6].toString()) : null;
            if (adjustmentAmount != null && adjustmentAmount != 0d) {
                arTransactionBean.setAdjustmentAmount(adjustmentAmount);
                arTransactionBean.setAdjustmentDate(date);
            }
            arTransactionBean.setUserName(null != col[7] ? (String) col[7] : "System");
            unpostedPayments.add(arTransactionBean);
        }
        return unpostedPayments;
    }
}
