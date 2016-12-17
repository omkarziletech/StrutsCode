package com.gp.cvst.logisoft.hibernate.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.Example;

import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.domain.PaymentPrepay;
import org.hibernate.LockOptions;

/**
 * Data access object (DAO) for domain model class PaymentPrepay.
 * @see com.gp.cvst.logisoft.hibernate.dao.PaymentPrepay
 * @author MyEclipse - Hibernate Tools
 */
public class PaymentPrepayDAO extends BaseHibernateDAO {

    //property constants
    public static final String BATCH_ID = "batchId";
    public static final String CUST_ID = "custId";
    public static final String CHECK_NO = "checkNo";
    public static final String DOCK_RECEIPT_NO = "dockReceiptNo";
    public static final String PREPAY_AMT = "prepayAmt";

    public void save(PaymentPrepay transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public void delete(PaymentPrepay persistentInstance) throws Exception{
            getSession().delete(persistentInstance);
            getSession().flush();
    }

    public PaymentPrepay findById(java.lang.Integer id)  throws Exception{
            PaymentPrepay instance = (PaymentPrepay) getSession().get("com.gp.cvst.logisoft.hibernate.dao.PaymentPrepay", id);
            return instance;
    }

    public List findByExample(PaymentPrepay instance) throws Exception{
            List results = getSession().createCriteria("com.gp.cvst.logisoft.hibernate.dao.PaymentPrepay").add(Example.create(instance)).list();
            return results;
    }

    public List findByProperty(String propertyName, Object value) throws Exception{
            String queryString = "from PaymentPrepay as model where model." + propertyName + "= ?0";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setParameter("0", value);
            return queryObject.list();
    }

    public List findByBatchId(Object batchId) throws Exception{
        return findByProperty(BATCH_ID, batchId);
    }

    public List findByCustId(Object custId) throws Exception{
        return findByProperty(CUST_ID, custId);
    }

    public List findByCheckNo(Object checkNo) throws Exception{
        return findByProperty(CHECK_NO, checkNo);
    }

    public List findByDockReceiptNo(Object dockReceiptNo) throws Exception{
        return findByProperty(DOCK_RECEIPT_NO, dockReceiptNo);
    }

    public List findByPrepayAmt(Object prepayAmt) throws Exception{
        return findByProperty(PREPAY_AMT, prepayAmt);
    }

    public PaymentPrepay merge(PaymentPrepay detachedInstance) throws Exception{
            PaymentPrepay result = (PaymentPrepay) getSession().merge(detachedInstance);
            return result;
    }

    public void attachDirty(PaymentPrepay instance) throws Exception{
            getSession().saveOrUpdate(instance);
            getSession().flush();
    }

    public void attachClean(PaymentPrepay instance) throws Exception{
            getSession().buildLockRequest(LockOptions.NONE).lock(instance);
    }

    public String getNotesFromBatchCheckNoAndDocReceipt(Integer batchId, String checkNo, String docReceiptNo)throws Exception {
        StringBuffer notes = new StringBuffer("");
        String query = " From PaymentPrepay p where p.batchId=" + batchId + " and p.checkNo='" + checkNo + "'" +
                " and p.dockReceiptNo='" + docReceiptNo + "'";
        List<PaymentPrepay> prepaymentList = getCurrentSession().createQuery(query).list();
        if (null != prepaymentList && !prepaymentList.isEmpty()) {
            for (PaymentPrepay prepay : prepaymentList) {
                if (null != prepay.getNotes() && !prepay.getNotes().trim().equals("")) {
                    notes.append(prepay.getNotes());
                }
            }
        }

        return notes.toString();
    }

    public List<PaymentPrepay> findByCheckNoandPaymentCheckId(String checkNo, Integer paymentCheckId)  throws Exception{
        List<PaymentPrepay> paymentPrePayList = new ArrayList<PaymentPrepay>();
        Query queryObject;
            String queryString = "from PaymentPrepay  where checkNo=?0 and paymentCheckId =?1";
            queryObject = getSession().createQuery(queryString);
            queryObject.setParameter("0", checkNo);
            queryObject.setParameter("1", paymentCheckId);
            paymentPrePayList = queryObject.list();
        return paymentPrePayList;
    }
}
