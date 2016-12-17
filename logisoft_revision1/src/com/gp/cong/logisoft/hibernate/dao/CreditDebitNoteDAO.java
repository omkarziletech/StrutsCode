package com.gp.cong.logisoft.hibernate.dao;

// default package
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;

import com.gp.cong.logisoft.domain.CreditDebitNote;
import com.gp.cong.logisoft.util.CommonFunctions;
import org.hibernate.LockOptions;
import org.hibernate.SQLQuery;

/**
 * A data access object (DAO) providing persistence and search support for CreditDebitNote entities.
 * Transaction control of the save(), update() and delete() operations
can directly support Spring container-managed transactions or they can be augmented	to handle user-managed Spring transactions. 
Each of these methods provides additional information for how to configure it for the desired type of transaction control. 	
 * @see .CreditDebitNote
 * @author MyEclipse Persistence Tools
 */
public class CreditDebitNoteDAO extends BaseHibernateDAO {

    //property constants
    public static final String BOLID = "bolid";
    public static final String CORRECTION_NUMBER = "correctionNumber";
    public static final String CUSTOMER_NAME = "customerName";
    public static final String CUSTOMER_NUMBER = "customerNumber";
    public static final String DEBIT_CREDIT_NOTE = "debitCreditNote";

    public void save(CreditDebitNote transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public void delete(CreditDebitNote persistentInstance) throws Exception {
        getSession().delete(persistentInstance);
        getSession().flush();
    }

    public CreditDebitNote findById(java.lang.Integer id) throws Exception {
        CreditDebitNote instance = (CreditDebitNote) getSession().get("com.gp.cong.logisoft.domain.CreditDebitNote", id);
        return instance;
    }

    public List findByExample(CreditDebitNote instance) throws Exception {
        List results = getSession().createCriteria("com.gp.cong.logisoft.domain.CreditDebitNote").add(Example.create(instance)).list();
        return results;
    }

    public List findByProperty(String propertyName, Object value) throws Exception {
        String queryString = "from CreditDebitNote as model where model."
                + propertyName + "= ?0";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", value);
        return queryObject.list();
    }

    public List findByBolid(Object bolid) throws Exception {
        return findByProperty(BOLID, bolid);
    }

    public List findByCorrectionNumber(Object correctionNumber) throws Exception {
        return findByProperty(CORRECTION_NUMBER, correctionNumber);
    }

    public List findByCustomerName(Object customerName) throws Exception {
        return findByProperty(CUSTOMER_NAME, customerName);
    }

    public List findByCustomerNumber(Object customerNumber) throws Exception {
        return findByProperty(CUSTOMER_NUMBER, customerNumber);
    }

    public List findByDebitCreditNote(Object debitCreditNote) throws Exception {
        return findByProperty(DEBIT_CREDIT_NOTE, debitCreditNote);
    }

    public List findAll() {
        try {
            String queryString = "from CreditDebitNote";
            Query queryObject = getSession().createQuery(queryString);
            return queryObject.list();
        } catch (RuntimeException re) {
            throw re;
        }
    }

    public CreditDebitNote merge(CreditDebitNote detachedInstance) throws Exception {
        CreditDebitNote result = (CreditDebitNote) getSession().merge(detachedInstance);
        return result;
    }

    public void attachDirty(CreditDebitNote instance) throws Exception {
        getSession().saveOrUpdate(instance);
        getSession().flush();
    }

    public void attachClean(CreditDebitNote instance) throws Exception {
        getSession().buildLockRequest(LockOptions.NONE).lock(instance);
    }

    public List<CreditDebitNote> searchThroughCriteria(String noticeNumber, String blNo) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(CreditDebitNote.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (CommonFunctions.isNotNull(noticeNumber)) {
            criteria.add(Restrictions.eq(CORRECTION_NUMBER, noticeNumber));
        }
        if (CommonFunctions.isNotNull(blNo)) {
            if (blNo.contains("-")) {
                criteria.add(Restrictions.like(BOLID, "%" + blNo.substring(blNo.lastIndexOf("-")+1, blNo.length())));
            } else {
                criteria.add(Restrictions.eq(BOLID, blNo));
            }
        }
        return criteria.list();
    }
    
    public String getDebitorCredit(String bolId,String correctionNo) throws Exception {
        String queryString = "SELECT debit_credit_note FROM credit_debit_note WHERE bolid=:bolId AND correction_number=:correctionNo Limit 1";
        SQLQuery query = getCurrentSession().createSQLQuery(queryString);
        query.setParameter("bolId", bolId);
        query.setParameter("correctionNo", correctionNo);
        return (String) query.uniqueResult();
    }
}
