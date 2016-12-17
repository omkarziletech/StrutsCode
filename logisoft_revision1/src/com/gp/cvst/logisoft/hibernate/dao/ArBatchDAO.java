package com.gp.cvst.logisoft.hibernate.dao;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.Example;

import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.AccountingConstants;
import com.gp.cvst.logisoft.beans.ArBatchbean;
import com.gp.cvst.logisoft.beans.TransactionBean;
import com.gp.cvst.logisoft.domain.ArBatch;
import com.gp.cvst.logisoft.struts.form.ReconcileForm;
import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * Data access object (DAO) for domain model class ArBatch.
 * @see com.gp.cvst.logisoft.hibernate.dao.ArBatch
 * @author MyEclipse - Hibernate Tools
 */
public class ArBatchDAO extends BaseHibernateDAO {

    public void save(ArBatch transientInstance) {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public Integer saveAndReturnId(ArBatch transientInstance) throws Exception {
        getSession().save(transientInstance);
        return transientInstance.getBatchId();
    }

    public void update(ArBatch persistanceInstance) throws Exception {
        getSession().update(persistanceInstance);
    }

    public void delete(ArBatch persistentInstance) throws Exception {
        getSession().delete(persistentInstance);
    }

    public ArBatch findById(java.lang.Integer id) throws Exception {
        ArBatch instance = (ArBatch) getSession().get("com.gp.cvst.logisoft.domain.ArBatch", id);
        return instance;
    }

    public List findByExample(ArBatch instance) throws Exception {
        List results = getSession().createCriteria("com.gp.cvst.logisoft.domain.ArBatch").add(Example.create(instance)).list();
        return results;
    }

    public List findByProperty(String propertyName, Object value) throws Exception {
        String queryString = "from ArBatch as model where model." + propertyName + "= ?0";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", value);
        return queryObject.list();
    }

    public ArBatch merge(ArBatch detachedInstance) throws Exception {
        ArBatch result = (ArBatch) getSession().merge(detachedInstance);
        return result;
    }

    public void attachDirty(ArBatch instance) throws Exception {
        getSession().saveOrUpdate(instance);
    }

    public void attachClean(ArBatch instance) throws Exception {
        getSession().buildLockRequest(LockOptions.NONE).lock(instance);
    }

    public String getMaxBatchNumber() throws Exception {
        Iterator results = null;
        results = getCurrentSession().createQuery(
                "select max(batch.batchId) from ArBatch batch").list().iterator();
        while (results.hasNext()) {
            int i = Integer.parseInt(results.next().toString()) + 1;
            String batchno = new Integer(i).toString();
            return batchno;
        }
        return null;
    }

    public List findByBatchNo(Integer batchNo) throws Exception {
        String queryString = "from ArBatch  where batchId like'" + batchNo + "%'";
        Query queryObject = getSession().createQuery(queryString);

        return queryObject.list();
    }
    public ArBatch findByBatchNoUnique(Integer batchNo) throws Exception {
        String queryString = "from ArBatch  where batchId like'" + batchNo + "%'";
        Query queryObject = getSession().createQuery(queryString);
        return (ArBatch)queryObject.setMaxResults(1).uniqueResult();
    }

    public String getBankAcctnoFromBankdetails(int batchid) throws Exception {
        String result = "";
        String QueryString = "";
        QueryString = "select bp.bankAccount from ArBatch bp where bp.batchId='" + batchid + "'";
        Iterator queryObject = getCurrentSession().createQuery(QueryString).list().iterator();
        result = (String) queryObject.next();

        return result;
    }

    public Iterator getArbatchList() throws Exception {
        Iterator results = null;
        results = getCurrentSession().createQuery(
                "select arb.batchId from ArBatch arb").list().iterator();
        return results;
    }

    public List getForSearch(Integer batchId, String startDate, String endDate, String status, String batchType, String loginName, String amount) throws Exception {
        List<ArBatchbean> arBatchList = new ArrayList<ArBatchbean>();
        Criteria criteria = getCurrentSession().createCriteria(ArBatch.class);
        if (CommonUtils.isNotEmpty(batchId)) {
            criteria.add(Restrictions.eq("batchId", batchId));
        }
        if (CommonUtils.isNotEmpty(loginName)) {
            criteria.add(Restrictions.like("userId", loginName + "%"));
        }
        if (CommonUtils.isEqualIgnoreCase(status, CommonConstants.BOTH)) {
            Disjunction statusDisjunction = Restrictions.disjunction();
            statusDisjunction.add(Restrictions.eq("status", CommonConstants.STATUS_OPEN));
            statusDisjunction.add(Restrictions.eq("status", CommonConstants.STATUS_CLOSED));
            statusDisjunction.add(Restrictions.eq("status", CommonConstants.STATUS_RECONCILE_IN_PROGRESS));
            statusDisjunction.add(Restrictions.eq("status", CommonConstants.CLEAR));
            statusDisjunction.add(Restrictions.eq("status", CommonConstants.STATUS_VOID));
            criteria.add(statusDisjunction);
        } else if (CommonUtils.isEqualIgnoreCase(status, CommonConstants.STATUS_CLOSED)) {
            Disjunction disjunction = Restrictions.disjunction();
            disjunction.add(Restrictions.eq("status", CommonConstants.STATUS_CLOSED));
            disjunction.add(Restrictions.eq("status", CommonConstants.STATUS_RECONCILE_IN_PROGRESS));
            disjunction.add(Restrictions.eq("status", CommonConstants.CLEAR));
            criteria.add(disjunction);
        } else {
            criteria.add(Restrictions.eq("status", CommonConstants.STATUS_OPEN));
        }
        if (CommonUtils.isEqualIgnoreCase(batchType, "cash")) {
            Disjunction typeDisjunction = Restrictions.disjunction();
            typeDisjunction.add(Restrictions.ne("batchType", "S"));
            typeDisjunction.add(Restrictions.isNull("batchType"));
            criteria.add(typeDisjunction);
        } else if (CommonUtils.isEqualIgnoreCase(batchType, "net")) {
            criteria.add(Restrictions.eq("batchType", "S"));
        } else {
            Disjunction typeDisjunction = Restrictions.disjunction();
            typeDisjunction.add(Restrictions.eq("batchType", "N"));
            typeDisjunction.add(Restrictions.eq("batchType", "S"));
            criteria.add(typeDisjunction);
        }
        if (CommonUtils.isNotEmpty(startDate) && CommonUtils.isNotEmpty(endDate)) {
            criteria.add(Restrictions.between("date", DateUtils.parseDate(startDate, "MM/dd/yyyy"), DateUtils.parseDate(endDate, "MM/dd/yyyy")));
        }
        if (CommonUtils.isNotEmpty(amount)) {
            criteria.add(Restrictions.eq("totalAmount", Double.parseDouble(amount.replaceAll(",", ""))));
        }
        criteria.addOrder(Order.asc("batchId"));
        List<ArBatch> result = criteria.list();
        if (CommonUtils.isNotEmpty(result)) {
            for (ArBatch arBatch : result) {
                ArBatchbean arbatchbean = new ArBatchbean(arBatch);
                if (CommonUtils.isNotEqual(arBatch.getBatchType(), "S")) {
                    Double totalAmount = null != arBatch.getTotalAmount() ? arBatch.getTotalAmount() : 0d;
                    Double appliedAmount = null != arBatch.getAppliedAmount() ? arBatch.getAppliedAmount() : 0d;
                    arbatchbean.setBalanceAmount(totalAmount - appliedAmount);
                } else {
                    arbatchbean.setBalanceAmount(null);
                }
                if (CommonUtils.isEqual(arBatch.getStatus(), CommonConstants.STATUS_RECONCILE_IN_PROGRESS)
                        || CommonUtils.isEqual(arBatch.getStatus(), CommonConstants.CLEAR)) {
                    arbatchbean.setStatus(CommonConstants.STATUS_CLOSED);
                }
                arBatchList.add(arbatchbean);
            }
        }
        return arBatchList;
    }

    public Date getBatchDate(String batchid) throws Exception {
        Date bdate = null;
        String queryString = "select arb.date from ArBatch arb where arb.batchId='" + batchid + "'";
        Iterator it = getCurrentSession().
                createQuery(queryString).list().iterator();
        bdate = (Date) it.next();
        return bdate;
    }
    /* public void upDateBatchBalanceAmt(String amt,String batchid,String onamt,String preamt,String amtapplied)
    {


    try {
    String queryString="update ArBatch arb set arb.balanceAmount='"+amt+"',arb.onAcctAmount=arb.onAcctAmount+"+onamt+",arb.prepayAmount=arb.prepayAmount+'"+preamt+"',arb.appliedAmount=arb.appliedAmount+"+amtapplied+" where arb.batchId='"+batchid+"'";

    int updatedrecords=getCurrentSession().createQuery(queryString).executeUpdate();


    if(amt.equals("0.00")||amt.equals("0"))
    {
    
    String queryString2="update ArBatch arb set arb.reconciled='Y' where arb.batchId='"+batchid+"'";
    int updatedReconcile=getCurrentSession().createQuery(queryString2).executeUpdate();

    getCurrentSession().flush();
    }
    }
    catch (HibernateException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
    }

    }*/

    public String getBalAmountForBatch(String batchid) throws Exception {
        String result = "";
        String queryString = "select arb.balanceAmount from ArBatch arb where arb.batchId='" + batchid + "'";
        Iterator it = getCurrentSession().
                createQuery(queryString).list().iterator();
        double r = (Double) it.next();

        NumberFormat number = new DecimalFormat("0.00");
        result = number.format(r);
        return result;
    }

    public String updateBatchStatus(String batch) throws Exception {
        String status = AccountingConstants.BATCH_POSTED_STATUS;
        int updatedrecords = 0;
        String queryString = "update ArBatch arb set arb.status='" + status + "' where arb.batchId='" + batch + "'";
        updatedrecords = getCurrentSession().createQuery(queryString).executeUpdate();
        return status;
    }
    // To get Batch No List For Dojo..By Pradeep

    public List getBatchNumberList(String batchNo) throws Exception {
        List arBatchList = null;
        if (batchNo != null && !batchNo.equals(0)) {
            String query = "from ArBatch arbatch where arbatch.batchId like '" + batchNo + "%'";
            arBatchList = getCurrentSession().createQuery(query).setMaxResults(50).list();
        }
        return arBatchList;
    }

    public List<TransactionBean> getOpenDepositsForReconcile(ReconcileForm reconcileForm) throws Exception {
        String date = DateUtils.formatDate(DateUtils.parseDate(reconcileForm.getBankReconcileDate(), "MM/dd/yyyy"), "yyyy-MM-dd");
        List<TransactionBean> reconcileList = new ArrayList<TransactionBean>();
        StringBuilder queryBuilder = new StringBuilder("select ab.Batch_id,tl.Transaction_Id,");
        queryBuilder.append("format(if(tl.Transaction_amt>0,tl.Transaction_amt,0),2) as debit,");
        queryBuilder.append("format(if(tl.Transaction_amt<0,-tl.Transaction_amt,0),2) as credit,");
        queryBuilder.append("date_format(ab.Deposit_date,'%m/%d/%Y') as date,");
        queryBuilder.append("if(tl.reconciled='").append(CommonConstants.STATUS_IN_PROGRESS).append("','");
        queryBuilder.append(CommonConstants.STATUS_RECONCILE_IN_PROGRESS).append("','").append(CommonConstants.STATUS_OPEN).append("') as status");
        queryBuilder.append(" from ar_batch ab join transaction_ledger tl on tl.Ar_Batch_Id=ab.Batch_id");
        queryBuilder.append(" and tl.Subledger_Source_code='").append(AccountingConstants.AR_BATCH_CLOSING_SUBLEDGER_CODE).append("'");
        queryBuilder.append(" and tl.GL_account_number='").append(reconcileForm.getGlAccountNumber()).append("'");
        queryBuilder.append(" and (tl.reconciled is null or tl.reconciled!='").append(CommonConstants.YES).append("')");
        queryBuilder.append(" and (tl.invoice_number!='").append(AccountingConstants.PRE_PAYMENT).append("' or tl.invoice_number is null)");
        queryBuilder.append(" and tl.Transaction_amt<>0");
        queryBuilder.append(" where date_format(ab.Deposit_date,'%Y-%m-%d')<='").append(date).append("'");
        queryBuilder.append(" and ab.Status='").append(AccountingConstants.BATCH_POSTED_STATUS).append("'");
        queryBuilder.append(" and ab.batch_type!='").append(AccountingConstants.AR_BATCH_TYPE_NETS).append("'");
        queryBuilder.append(" order by ab.Batch_id");
        getCurrentSession().flush();
        List result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            TransactionBean transactionBean = new TransactionBean();
            transactionBean.setBatchId(col[0].toString());
            transactionBean.setTransactionId(col[1].toString() + "@" + CommonConstants.MODULE_TRANSACTION_LEDGER);
            transactionBean.setPaymentMethod(AccountingConstants.DEPOSIT);
            transactionBean.setDebit(col[2].toString());
            transactionBean.setCredit(col[3].toString());
            transactionBean.setTransDate(col[4].toString());
            transactionBean.setStatus(col[5].toString());
            reconcileList.add(transactionBean);
        }
        return reconcileList;
    }

    public String getTotalOpenArBatchesForGivenPeriod(String startDate, String endDate) throws Exception {
        Integer count = 0;
        String queryString = "select count(*) from ar_batch where Deposit_date between '" + DateUtils.parseStringDateToMYSQLFormat(startDate) + "'"
                + " and '" + DateUtils.parseStringDateToMYSQLFormat(endDate) + "' and Status='" + CommonConstants.STATUS_OPEN + "'";
        Query query = getCurrentSession().createSQLQuery(queryString);

        Object result = query.uniqueResult();
        if (null != result) {
            count = Integer.parseInt(result.toString());
        }
        return count.toString();
    }

    public boolean unLockBatchforUser(Integer userId) throws Exception {
        String queryString = "update ar_batch set using_by=null where using_by=" + userId;
        getCurrentSession().createSQLQuery(queryString).executeUpdate();
        return true;
    }
}
