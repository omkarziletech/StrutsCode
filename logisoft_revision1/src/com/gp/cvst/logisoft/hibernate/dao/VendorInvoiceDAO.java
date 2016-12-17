package com.gp.cvst.logisoft.hibernate.dao;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.domain.VendorInvoice;
import org.hibernate.LockOptions;

/**
 * Data access object (DAO) for domain model class VendorInvoice.
 * @see com.gp.cvst.logisoft.hibernate.dao.VendorInvoice
 * @author MyEclipse - Hibernate Tools
 */
public class VendorInvoiceDAO extends BaseHibernateDAO {


    public void save(VendorInvoice transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public void delete(VendorInvoice persistentInstance) throws Exception {
            getSession().delete(persistentInstance);
            getSession().flush();
    }

    public VendorInvoice findById(java.lang.Integer id) throws Exception {
            VendorInvoice instance = (VendorInvoice) getSession().get("com.gp.cvst.logisoft.hibernate.dao.VendorInvoice", id);
            return instance;
    }

    public List findByExample(VendorInvoice instance) throws Exception {
            List results = getSession().createCriteria("com.gp.cvst.logisoft.hibernate.dao.VendorInvoice").add(Example.create(instance)).list();
            return results;
    }

    public List findByProperty(String propertyName, Object value)throws Exception {
            String queryString = "from VendorInvoice as model where model." + propertyName + "= ?0";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setParameter("0", value);
            return queryObject.list();
    }

    public VendorInvoice merge(VendorInvoice detachedInstance) throws Exception {
            VendorInvoice result = (VendorInvoice) getSession().merge(detachedInstance);
            return result;
    }

    public void attachDirty(VendorInvoice instance) throws Exception {
            getSession().saveOrUpdate(instance);
            getSession().flush();
    }

    public void attachClean(VendorInvoice instance) throws Exception {
            getSession().buildLockRequest(LockOptions.NONE).lock(instance);
    }
}
