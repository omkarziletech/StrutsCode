package com.logiware.hibernate.dao;

import java.util.List;
import org.hibernate.Query;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.logiware.hibernate.domain.PaymentRelease;

/**
 * Data access object (DAO) for domain model class ApInvoice.
 * @see com.gp.cvst.logisoft.hibernate.dao.ApInvoice
 * @author MyEclipse - Hibernate Tools
 */
public class PaymentReleaseDAO extends BaseHibernateDAO {

    // property constants
    /**
     * @param transientInstance
     */
    public void save(PaymentRelease paymentRelease) throws Exception {
        getSession().save(paymentRelease);
        getSession().flush();
    }

    /**
     * @param persistentInstance
     */
    public void delete(PaymentRelease paymentRelease) throws Exception {
        getSession().delete(paymentRelease);
        getSession().flush();
    }

    /**
     *
     * @param updateInstance
     */
    public void saveOrUpdate(PaymentRelease paymentRelease) throws Exception {
        getSession().saveOrUpdate(paymentRelease);
        getSession().flush();
    }

    /**
     * @param id
     * @return
     */
    public PaymentRelease findById(Integer id) throws Exception {
        PaymentRelease instance = (PaymentRelease) getSession().get("com.logiware.hibernate.domain.PaymentRelease", id);
        return instance;
    }

    public List findAll(Integer bolId) throws Exception {
        String queryString = "from PaymentRelease where bolId = ?0";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", bolId);
        return queryObject.list();
    }

    public Object sumOfPaidCharges(Integer bolId) throws Exception {
        String queryString = "select sum(amount) FROM PaymentRelease WHERE bolId=" + bolId;
        Object object = getSession().createQuery(queryString).uniqueResult();
        return object;
    }
}
