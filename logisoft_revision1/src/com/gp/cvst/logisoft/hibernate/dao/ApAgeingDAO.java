package com.gp.cvst.logisoft.hibernate.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.Example;

import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.domain.ApAgeing;
import org.hibernate.LockOptions;

/**
 * Data access object (DAO) for domain model class ApAgeing.
 * @see com.gp.cvst.logisoft.hibernate.dao.ApAgeing
 * @author MyEclipse - Hibernate Tools
 */
public class ApAgeingDAO extends BaseHibernateDAO {

    public void save(ApAgeing transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public void delete(ApAgeing persistentInstance) throws Exception {
        getSession().delete(persistentInstance);
        getSession().flush();
    }

    public ApAgeing findById(java.lang.Integer id) throws Exception {
        ApAgeing instance = (ApAgeing) getSession().get("com.gp.cvst.logisoft.hibernate.dao.ApAgeing", id);
        return instance;
    }

    public List findByExample(ApAgeing instance) throws Exception {
        List results = getSession().createCriteria("com.gp.cvst.logisoft.hibernate.dao.ApAgeing").add(Example.create(instance)).list();
        return results;
    }

    public List findByProperty(String propertyName, Object value) throws Exception {
        String queryString = "from ApAgeing as model where model." + propertyName + "= ?0";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", value);
        return queryObject.list();
    }

    public ApAgeing merge(ApAgeing detachedInstance) throws Exception {
        ApAgeing result = (ApAgeing) getSession().merge(detachedInstance);
        return result;
    }

    public void attachDirty(ApAgeing instance) throws Exception {
        getSession().saveOrUpdate(instance);
        getSession().flush();
    }

    public void attachClean(ApAgeing instance) throws Exception {
        getSession().buildLockRequest(LockOptions.NONE).lock(instance);
    }

    public List findAge(String acctno) throws Exception {
        List result = new ArrayList();
        String queryString = "select a.ageCurrent,a.age3060,a.age6090,a.ageGt90,a.total from ApAgeing a where a.custNo='" + acctno + "'";
        List QueryObject = getCurrentSession().createQuery(queryString).list();
        Iterator itr = QueryObject.iterator();
        if (itr.hasNext()) {
            Object[] row = (Object[]) itr.next();
            result.add((Integer) row[0]);
            result.add((Integer) row[1]);
            result.add((Integer) row[2]);
            result.add((Integer) row[3]);
            result.add((Integer) row[4]);
        }
        return result;
    }
}
