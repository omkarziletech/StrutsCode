package com.gp.cvst.logisoft.hibernate.dao;

import com.gp.cong.common.ConstantsInterface;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.AccountingConstants;
import com.gp.cvst.logisoft.domain.PaymentChecks;
import org.apache.struts.util.LabelValueBean;
import org.hibernate.LockOptions;
import org.hibernate.type.StringType;

public class PaymentChecksDAO extends BaseHibernateDAO implements ConstantsInterface {

    public static final String BATCH_ID = "batchId";

    public void save(PaymentChecks paymentChecks) throws Exception {
        getSession().save(paymentChecks);
        getSession().flush();
    }

    public void update(PaymentChecks paymentChecks) throws Exception {
	getSession().update(paymentChecks);
	getSession().flush();
    }

    public void delete(PaymentChecks persistentInstance) throws Exception {
	getSession().delete(persistentInstance);
	getSession().flush();
    }

    public PaymentChecks findById(java.lang.Integer id) throws Exception {
	PaymentChecks instance = (PaymentChecks) getSession().get("com.gp.cvst.logisoft.domain.PaymentChecks", id);
	return instance;
    }

    public List findByExample(PaymentChecks instance) throws Exception {
	List results = getSession().createCriteria("com.gp.cvst.logisoft.hibernate.dao.PaymentChecks").add(Example.create(instance)).list();
	return results;
    }

    public List findByProperty(String propertyName, Object value) throws Exception {
	String queryString = "from PaymentChecks as model where model." + propertyName + "= ?0";
	Query queryObject = getSession().createQuery(queryString);
	queryObject.setParameter("0", value);
	return queryObject.list();
    }

    public PaymentChecks merge(PaymentChecks detachedInstance) throws Exception {
	PaymentChecks result = (PaymentChecks) getSession().merge(detachedInstance);
	return result;
    }

    public void attachDirty(PaymentChecks instance) throws Exception {
	getSession().saveOrUpdate(instance);
	getSession().flush();
    }

    public void attachClean(PaymentChecks instance) throws Exception {
	getSession().buildLockRequest(LockOptions.NONE).lock(instance);
    }

    public Double sumOfAppliedAmountsOFChecksInBatch(Integer batchid) throws Exception {
	Double result = 0d;
	String query = "select sum(pc.appliedAmount) from PaymentChecks pc where pc.batchId='" + batchid + "'";
	result = (Double) getCurrentSession().createQuery(query).uniqueResult();
	if (result == null) {
	    result = 0d;
	}
	return result;
    }

    public Double sumOfAdjustAmountsOFChecksInBatch(Integer batchid) throws Exception {
	Double result = 0d;
	String query = "select sum(pc.adjustAmt) from PaymentChecks pc where pc.batchId='" + batchid + "'";
	result = (Double) getCurrentSession().createQuery(query).uniqueResult();
	if (result == null) {
	    result = 0d;
	}
	return result;
    }

    public Double sumOfOnAccountOFChecksInBatch(Integer batchid, String invoiceNo) throws Exception {
	Double result = 0d;
	String query = "select sum(pc.paymentAmt) from Payments pc where pc.batchId='" + batchid + "' and pc.invoiceNo='" + invoiceNo + "'";
	if (null != invoiceNo && (invoiceNo.trim().equals(AccountingConstants.PRE_PAYMENT) || invoiceNo.trim().equals(AccountingConstants.ON_ACCOUNT) || invoiceNo.trim().equals(AccountingConstants.CHARGE_CODE))) {
	    query += " and transactionId is null and paymentCheckId is not null";
	}
	result = (Double) getCurrentSession().createQuery(query).uniqueResult();
	if (result == null) {
	    result = 0d;
	}
	return result;
    }

    public List getPaymentCheckBasedOnBatchidAndCheckNo(String batchid, String checkNo) throws Exception {
	List<PaymentChecks> paymentCheckList = new ArrayList<PaymentChecks>();
	String query = " from PaymentChecks pc where pc.batchId='" + batchid + "' and pc.checkNo='" + checkNo + "'";
	paymentCheckList = getCurrentSession().createQuery(query).list();
	return paymentCheckList;
    }

    public String getTransactionType(Integer id, String action) throws Exception {
	String type = "";
	String query = "";

	if (action.equals(AccountingConstants.ACTION_TRANSACTION)) {
	    query = "select t.transactionType from Transaction t where t.transactionId=" + id;
	} else {
	    query = "select t.transactionType from TransactionLedger t where t.transactionId=" + id;
	}
	Query q = getCurrentSession().createQuery(query);
	type = (String) q.uniqueResult();
	if (type == null) {
	    type = "";
	}
	return type;
    }

    public String getCheckNoByBatchId(Integer batchId) throws Exception {
	String query = "select group_concat(pc.Check_no) as checkNo from payment_checks pc where pc.Batch_id='" + batchId + "'";
	Query q = getCurrentSession().createSQLQuery(query).addScalar("checkNo", StringType.INSTANCE);
	return (String) q.uniqueResult();
    }

    public Double getCheckAmtForCustomer(String batchId, String checkNo, String custId) throws Exception {
	String queryString = " select sum(pc.receivedAmt) from PaymentChecks pc where pc.batchId = " + batchId
		+ " and checkNo = '" + checkNo + "' and custId = '" + custId + "'";
	Double totalCheckAmt = (Double) getSession().createQuery(queryString).uniqueResult();
	if (null != totalCheckAmt) {
	    return totalCheckAmt;
	}
	return 0d;
    }

    public Double getTotalCheckAmtForBatch(Integer batchId) throws Exception {
	String queryString = " select sum(pc.receivedAmt) from PaymentChecks pc where pc.batchId = " + batchId;
	Double totalCheckAmt = (Double) getSession().createQuery(queryString).uniqueResult();
	if (null != totalCheckAmt) {
	    return totalCheckAmt;
	}
	return 0d;
    }

    public void voidCheck(Integer batchId, Integer checkId, Integer userId) throws Exception {
	StringBuilder queryBuilder = new StringBuilder("update transaction_ledger tl,payments p");
	queryBuilder.append(" set tl.Status='").append(STATUS_OPEN).append("',tl.Balance_In_Process=tl.Balance,");
	queryBuilder.append(" tl.Updated_By=").append(userId).append(",tl.Updated_On=sysdate()");
	queryBuilder.append(" where p.batch_id=").append(batchId).append(" and p.Payment_Check_Id=").append(checkId);
	queryBuilder.append(" and p.transaction_type = '").append(TRANSACTION_TYPE_ACCRUALS).append("'");
	queryBuilder.append(" and tl.Transaction_Id = p.transaction_id");
	queryBuilder.append(" and tl.Status='").append(STATUS_OPEN).append("'");
	getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
	queryBuilder = new StringBuilder("update transaction tr,payments p");
	queryBuilder.append(" set tr.Status='").append(STATUS_OPEN).append("',tr.Balance_In_Process=tr.Balance,");
	queryBuilder.append(" tr.Updated_By=").append(userId).append(",tr.Updated_On=sysdate()");
	queryBuilder.append(" where p.batch_id=").append(batchId).append(" and p.Payment_Check_Id=").append(checkId);
	queryBuilder.append(" and p.transaction_type = '").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("'");
	queryBuilder.append(" and tr.Transaction_Id = p.transaction_id");
	getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
	queryBuilder = new StringBuilder("update transaction tr,payments p");
	queryBuilder.append(" set tr.Status='").append(STATUS_OPEN).append("',tr.Balance_In_Process=tr.Balance_In_Process+p.Payment_Amt+p.Adjustment_Amt,");
	queryBuilder.append(" tr.Updated_By=").append(userId).append(",tr.Updated_On=sysdate()");
	queryBuilder.append(" where p.batch_id=").append(batchId).append(" and p.Payment_Check_Id=").append(checkId);
        queryBuilder.append(" and p.transaction_type = '").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
	queryBuilder.append(" and tr.Transaction_Id = p.transaction_id");
	getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
	queryBuilder = new StringBuilder("delete from payments where batch_id=").append(batchId).append(" and Payment_Check_Id=").append(checkId);
	getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
	queryBuilder = new StringBuilder("delete from payment_prepay where batch_id=").append(batchId).append(" and Payment_Check_Id=").append(checkId);
	getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
	queryBuilder = new StringBuilder("delete from payment_chargecode where batch_id=").append(batchId).append(" and Payment_Check_Id=").append(checkId);
	getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
	getCurrentSession().flush();
    }

    public Double getCheckAmountFromChecks(Integer batchId) throws Exception {
	String query = "select sum(Received_Amt) from payment_checks where batch_id=" + batchId;
	getCurrentSession().flush();
	Object result = getCurrentSession().createSQLQuery(query).uniqueResult();
	return null == result ? 0d : Double.parseDouble(result.toString());
    }

    public Double getAppliedAmountFromChecks(Integer batchId) throws Exception {
	String query = "select sum(applied_amount) from payment_checks where batch_id=" + batchId;
	getCurrentSession().flush();
	Object result = getCurrentSession().createSQLQuery(query).uniqueResult();
	return null == result ? 0d : Double.parseDouble(result.toString());
    }

    public List<LabelValueBean> getCustomersFromChecks(Integer batchId) {
	List<LabelValueBean> customers = new ArrayList<LabelValueBean>();
	StringBuilder queryBuilder = new StringBuilder("select acct_name,acct_no from");
	queryBuilder.append("((select concat(tp.acct_name,' <---> ',tp.acct_no) as acct_name,tp.acct_no as acct_no,'1' as control");
	queryBuilder.append(" from payment_checks pc join trading_partner tp on tp.acct_no=pc.Cust_id");
	queryBuilder.append(" where pc.batch_id=").append(batchId).append(" order by pc.Id)");
	queryBuilder.append(" union ");
	queryBuilder.append("(select concat(tp.acct_name,' <---> ',tp.acct_no),tp.acct_no,'2' as control");
	queryBuilder.append(" from payments p join trading_partner tp on tp.acct_no=p.Cust_No");
	queryBuilder.append(" where p.batch_id=").append(batchId).append(" and p.payment_type='P'");
	queryBuilder.append(" order by tp.acct_name,tp.acct_no)) as t group by acct_no order by control");
	getCurrentSession().flush();
	List result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
	for (Object row : result) {
	    Object[] col = (Object[]) row;
	    customers.add(new LabelValueBean((String) col[0], (String) col[1]));
	}
	return customers;
    }

    public List<String> getPaymentChecksIds(String batchId) {
	StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append("select cast(id as char character set latin1) as id");
	queryBuilder.append(" from payment_checks");
	queryBuilder.append(" where batch_id = ").append(batchId);
	getCurrentSession().flush();
	return getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
    }
}
