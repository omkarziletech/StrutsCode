package com.gp.cong.logisoft.hibernate.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.Example;

import com.gp.cong.logisoft.domain.Unit;
import org.hibernate.LockOptions;

/**
 * Data access object (DAO) for domain model class Unit.
 * 
 * @see com.gp.cvst.logisoft.hibernate.dao.Unit
 * @author MyEclipse Persistence Tools
 */
public class UnitDAO extends BaseHibernateDAO {

    public void save(Unit transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public void delete(Unit persistentInstance) throws Exception {
            getSession().delete(persistentInstance);
            getSession().flush();
    }

    public Unit findById(java.lang.Integer id) throws Exception {
            Unit instance = (Unit) getSession().get(
                    "com.gp.cvst.logisoft.domain.Unit", id);
            return instance;
    }

    public List findByExample(Unit instance) throws Exception {
            List results = getSession().createCriteria(
                    "com.gp.cvst.logisoft.hibernate.dao.Unit").add(
                    Example.create(instance)).list();
            return  results;
    }

    public List findByProperty(String propertyName, Object value) throws Exception {
            String queryString = "from Unit as model where model." + propertyName + "= ?0";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setParameter("0", value);
            return queryObject.list();
    }

    public List findAll() throws Exception {
            String queryString = "from Unit";
            Query queryObject = getSession().createQuery(queryString);
            return queryObject.list();
    }

    public Unit merge(Unit detachedInstance) throws Exception {
            Unit result = (Unit) getSession().merge(detachedInstance);
            return result;
    }

    public void attachDirty(Unit instance) throws Exception {
            getSession().saveOrUpdate(instance);
            getSession().flush();
    }

    public void attachClean(Unit instance) throws Exception {
            getSession().buildLockRequest(LockOptions.NONE).lock(instance);
    }

    public List findUnitNo(String unitNo) throws Exception {
        List list = new ArrayList();
            String queryString = "select id from Unit where unitNo like'" + unitNo + "%'";
            Query queryObject = getCurrentSession().createQuery(queryString);
            list = queryObject.list();
        return list;
    }
}
