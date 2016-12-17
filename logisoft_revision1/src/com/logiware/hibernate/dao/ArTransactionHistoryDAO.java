package com.logiware.hibernate.dao;

import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.beans.TransactionBean;
import com.logiware.accounting.model.ResultModel;
import com.logiware.bean.ArTransactionBean;
import java.io.Serializable;
import java.util.List;
import com.logiware.hibernate.domain.ArTransactionHistory;
import java.util.ArrayList;
import java.util.Date;
import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DoubleType;
import org.hibernate.type.StringType;

/**
 * Data access object (DAO) for domain model class ArTransactionHistory.
 * @see com.logiware.hibernate.domain.ArTransactionHistory
 * @author Lakshminarayanan
 */
public class ArTransactionHistoryDAO extends BaseHibernateDAO implements Serializable {

    private static final long serialVersionUID = -6416993010394778974L;

    public void save(ArTransactionHistory transientInstance) {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public void delete(ArTransactionHistory persistentInstance) throws Exception {
        getSession().delete(persistentInstance);
        getSession().flush();
    }

    public ArTransactionHistory findById(java.lang.Integer id) throws Exception {
        ArTransactionHistory instance = (ArTransactionHistory) getSession().get(
                "com.gp.cvst.logisoft.domain.ArTransactionHistory", id);
        return instance;
    }

    public TransactionBean findByIdTransactionBean(java.lang.Integer id) throws Exception {
        TransactionBean instance = (TransactionBean) getSession().get(
                "com.gp.cvst.logisoft.domain.TransactionBean", id);
        return instance;
    }

    public List<ArTransactionHistory> findByExample(ArTransactionHistory instance) throws Exception {
        List results = getSession().createCriteria(
                "com.gp.cvst.logisoft.domain.ArTransactionHistory").add(
                Example.create(instance)).list();
        return results;
    }

    public List<ArTransactionHistory> findByProperty(String propertyName, Object value) throws Exception {
        String queryString = "from ArTransactionHistory as model where model." + propertyName + " = ?0";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", value);
        return queryObject.list();
    }

    public List<ArTransactionHistory> findByAr(String custNo, String transactionType) throws Exception {
        String queryString = "from ArTransactionHistory as model where custNo = ?0 and transactionType = ?1";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", custNo);
        queryObject.setParameter("1", transactionType);
        return queryObject.list();
    }

    public void attachDirty(ArTransactionHistory instance) throws Exception {
        getSession().saveOrUpdate(instance);
    }

    public void attachClean(ArTransactionHistory instance) throws Exception {
        getSession().buildLockRequest(LockOptions.NONE).lock(instance);
    }

    @SuppressWarnings("unchecked")
    public List<ArTransactionBean> getArTransactionHistory(String customerNumber, String blNumber, String invoiceNumber) throws Exception {
        StringBuilder queryBuilder = new StringBuilder("select ath.transaction_date,ath.transaction_amount,");
        queryBuilder.append("ath.adjustment_amount,ath.transaction_type,ath.gl_account_number,ath.check_number,");
        queryBuilder.append("if(ath.ar_batch_id!='',ath.ar_batch_id,if(ath.ap_batch_id!='',ath.ap_batch_id,'')) as batchId,ath.created_by");
        queryBuilder.append(" from ar_transaction_history ath");
        queryBuilder.append(" where ath.customer_number='").append(customerNumber).append("'");
        if (null != blNumber && !blNumber.isEmpty()) {
            queryBuilder.append(" and (ath.bl_number='").append(blNumber).append("'");
            queryBuilder.append(" or ath.invoice_number='").append(blNumber).append("')");
        } else {
            queryBuilder.append(" and (ath.bl_number='").append(invoiceNumber).append("'");
            queryBuilder.append(" or ath.invoice_number='").append(invoiceNumber).append("')");
        }
        queryBuilder.append(" order by ath.transaction_date,ath.id");
        List<Object> result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        List<ArTransactionBean> arTransactionHistoryList = new ArrayList<ArTransactionBean>();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            ArTransactionBean arTransactionBean = new ArTransactionBean();
            Date date = null != col[0] ? (Date) col[0] : null;
            arTransactionBean.setTransactionDate(date);
            arTransactionBean.setTransactionAmount(null != col[1] ? Double.parseDouble(col[1].toString()) : 0d);
            Double adjustmentAmount = null != col[2] ? Double.parseDouble(col[2].toString()) : null;
            if (adjustmentAmount != null && adjustmentAmount != 0d) {
                arTransactionBean.setAdjustmentAmount(adjustmentAmount);
                arTransactionBean.setAdjustmentDate(date);
            }
            arTransactionBean.setTransactionType((String) col[3]);
            arTransactionBean.setGlAccount((String) col[4]);
            arTransactionBean.setCheckNo((String) col[5]);
            arTransactionBean.setBatchId((String) col[6]);
            arTransactionBean.setUserName(null != col[7] ? (String) col[7] : "System");
            arTransactionHistoryList.add(arTransactionBean);
        }
        return arTransactionHistoryList;
    }

    public ArTransactionHistory findTransactionHistoryByBlNoAndCustomer(String blNumber, String customerNumber) {
        Criteria criteria = getCurrentSession().createCriteria(ArTransactionHistory.class);
        criteria.add(Restrictions.eq("customerNumber", customerNumber));
        criteria.add(Restrictions.eq("blNumber", customerNumber));
        criteria.add(Restrictions.ne("transactionType", "VOID"));
        criteria.addOrder(Order.asc("id"));
        criteria.setMaxResults(1);
        return (ArTransactionHistory) criteria.uniqueResult();
    }

    public List<ArTransactionHistory> getArTransactionHistories(String arBatchId) {
        Criteria criteria = getCurrentSession().createCriteria(ArTransactionHistory.class);
        criteria.add(Restrictions.eq("arBatchId", Integer.parseInt(arBatchId)));
        return criteria.list();
    }

    public List<ArTransactionHistory> getArPaidInApInvoice(String checkNumber) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append(" customer_number as customerNumber,");
        queryBuilder.append(" bl_number as blNumber,");
        queryBuilder.append(" invoice_number as invoiceNumber,");
        queryBuilder.append(" sum(transaction_amount) as transactionAmount");
        queryBuilder.append(" from ar_transaction_history");
        queryBuilder.append(" where check_number = '").append(checkNumber).append("'");
        queryBuilder.append(" group by customer_number,invoice_or_bl");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("customerNumber", StringType.INSTANCE);
        query.addScalar("blNumber", StringType.INSTANCE);
        query.addScalar("invoiceNumber", StringType.INSTANCE);
        query.addScalar("transactionAmount", DoubleType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(ArTransactionHistory.class));
        return query.list();
    }

    public void updateVoyageNoByLcl(String fileNo, String newScheNo, Date sailDate) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append("UPDATE ar_transaction_history set ");
        queryStr.append("voyage_number=:newScheNo,invoice_date=:sailDate ");
        queryStr.append(" where invoice_number=:fileNo ");
        SQLQuery queryObject = getSession().createSQLQuery(queryStr.toString());
        queryObject.setString("fileNo", fileNo);
        queryObject.setParameter("newScheNo", newScheNo);
        queryObject.setParameter("sailDate", sailDate);
        queryObject.executeUpdate();
    }

    public void updateLclEBlNumber(String blNumber, String fileNo) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" update ar_transaction_history set bl_number=:blNumber where ");
        queryBuilder.append(" invoice_number=:fileNo ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setParameter("blNumber", blNumber);
        query.setParameter("fileNo", fileNo);
        query.executeUpdate();
        getCurrentSession().getTransaction().commit();
        getCurrentSession().getTransaction().begin();
    }
     public List<ResultModel> getPostedTransactions(String blNumber, String invoiceNumber, String transactionsType) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append("th.customer_number AS customerNumber,tp.acct_name AS customerName,");
        String paymentTypes = "'AP PY','AR PY','PP INV','AP INV','ADJ'";
        queryBuilder.append("  date_format(if(th.transaction_type in (").append(paymentTypes).append("), th.posted_date, th.transaction_date), '%d-%b-%Y') as transactionDate,");
        queryBuilder.append("  date_format(th.posted_date, '%d-%b-%Y') as postedDate,");
        queryBuilder.append("  th.transaction_type as type,");
        queryBuilder.append("  format(th.transaction_amount, 2) as transactionAmount,");
        queryBuilder.append("  if(");
        queryBuilder.append("    th.ar_batch_id <> 0,");
        queryBuilder.append("    th.ar_batch_id,");
        queryBuilder.append("    if(");
        queryBuilder.append("      th.ap_batch_id <> 0,");
        queryBuilder.append("      th.ap_batch_id,");
        queryBuilder.append("      null");
        queryBuilder.append("    )");
        queryBuilder.append("  ) as batchNumber,");
        queryBuilder.append("  upper(th.check_number) as checkNumber,");
        queryBuilder.append("  th.gl_account_number as glAccount,");
        queryBuilder.append("  if(");
        queryBuilder.append("    th.adjustment_amount <> 0,");
        queryBuilder.append("    date_format(th.transaction_date, '%d-%b-%Y'),");
        queryBuilder.append("    null");
        queryBuilder.append("  ) as adjustmentDate,");
        queryBuilder.append("  format(th.adjustment_amount, 2) as adjustmentAmount,");
        queryBuilder.append("  upper(");
        queryBuilder.append("    if(");
        queryBuilder.append("      th.created_by <> '',");
        queryBuilder.append("      th.created_by,");
        queryBuilder.append("      'System'");
        queryBuilder.append("    )");
        queryBuilder.append("  ) as user ");
        queryBuilder.append("from");
        queryBuilder.append("  ar_transaction_history th ");
        queryBuilder.append("  JOIN trading_partner tp ON tp.acct_no=th.customer_number ");
        queryBuilder.append("where");
        if(transactionsType.equalsIgnoreCase("FCL")){
         queryBuilder.append(" (th.bl_number = '04").append(invoiceNumber).append("'"); 
         queryBuilder.append(" OR th.bl_number IN(SELECT REPLACE(bolid, '==', 'CN') FROM fcl_bl WHERE file_no='").append(invoiceNumber).append("')");
         queryBuilder.append(" OR th.`invoice_number` IN(SELECT invoice_number FROM ar_red_invoice WHERE file_no='").append(invoiceNumber).append("'))");
        }else{
        queryBuilder.append(" (th.invoice_number = '").append(invoiceNumber).append("'").append(" OR th.bl_number='").append(invoiceNumber).append("')");
        }
        queryBuilder.append(" order by th.transaction_date, th.id");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("customerName", StringType.INSTANCE);
        query.addScalar("customerNumber", StringType.INSTANCE);
        query.addScalar("transactionDate", StringType.INSTANCE);
        query.addScalar("postedDate", StringType.INSTANCE);
        query.addScalar("type", StringType.INSTANCE);
        query.addScalar("transactionAmount", StringType.INSTANCE);
        query.addScalar("batchNumber", StringType.INSTANCE);
        query.addScalar("checkNumber", StringType.INSTANCE);
        query.addScalar("glAccount", StringType.INSTANCE);
        query.addScalar("adjustmentDate", StringType.INSTANCE);
        query.addScalar("adjustmentAmount", StringType.INSTANCE);
        query.addScalar("user", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(ResultModel.class));
        return query.list();
    }
}
