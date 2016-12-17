package com.gp.cvst.logisoft.hibernate.dao;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.domain.InvoiceCharges;
import org.hibernate.LockOptions;

/**
 * Data access object (DAO) for domain model class InvoiceCharges.
 * @see com.gp.cvst.logisoft.hibernate.dao.InvoiceCharges
 * @author MyEclipse - Hibernate Tools
 */
public class InvoiceChargesDAO extends BaseHibernateDAO {

    public void save(InvoiceCharges transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public void delete(InvoiceCharges persistentInstance)  throws Exception{
            getSession().delete(persistentInstance);
            getSession().flush();
    }

    public InvoiceCharges findById(java.lang.Integer id) throws Exception{
            InvoiceCharges instance = (InvoiceCharges) getSession().get("com.gp.cvst.logisoft.hibernate.dao.InvoiceCharges", id);
            return instance;
    }

    public List findByExample(InvoiceCharges instance) throws Exception{
            List results = getSession().createCriteria("com.gp.cvst.logisoft.hibernate.dao.InvoiceCharges").add(Example.create(instance)).list();
            return results;
    }

    public List findByProperty(String propertyName, Object value) throws Exception{
            String queryString = "from InvoiceCharges as model where model." + propertyName + "= ?0";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setParameter("0", value);
            return queryObject.list();
    }

    public InvoiceCharges merge(InvoiceCharges detachedInstance) throws Exception{
            InvoiceCharges result = (InvoiceCharges) getSession().merge(detachedInstance);
            return  result;
    }

    public void attachDirty(InvoiceCharges instance)  throws Exception{
            getSession().saveOrUpdate(instance);
    }

    public void attachClean(InvoiceCharges instance) throws Exception{
            getSession().buildLockRequest(LockOptions.NONE).lock(instance);
    }
}
