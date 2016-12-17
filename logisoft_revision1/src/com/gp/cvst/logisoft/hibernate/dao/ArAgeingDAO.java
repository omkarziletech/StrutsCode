package com.gp.cvst.logisoft.hibernate.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.domain.ArAgeing;
import org.hibernate.LockOptions;

/**
 * Data access object (DAO) for domain model class ArAgeing.
 * @see com.gp.cvst.logisoft.hibernate.dao.ArAgeing
 * @author MyEclipse - Hibernate Tools
 */
public class ArAgeingDAO extends BaseHibernateDAO {

    public void save(ArAgeing transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public void delete(ArAgeing persistentInstance) throws Exception {
        getSession().delete(persistentInstance);
        getSession().flush();
    }

    public ArAgeing findById(java.lang.Integer id) throws Exception {
        ArAgeing instance = (ArAgeing) getSession().get("com.gp.cvst.logisoft.hibernate.dao.ArAgeing", id);
        return instance;
    }

    public List findByExample(ArAgeing instance) throws Exception {
        List results = getSession().createCriteria("com.gp.cvst.logisoft.hibernate.dao.ArAgeing").add(Example.create(instance)).list();
        return results;
    }

    public List findByProperty(String propertyName, Object value) throws Exception {
        String queryString = "from ArAgeing as model where model." + propertyName + "= ?0";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", value);
        return queryObject.list();
    }

    public ArAgeing merge(ArAgeing detachedInstance) throws Exception {
        ArAgeing result = (ArAgeing) getSession().merge(detachedInstance);
        return result;
    }

    public void attachDirty(ArAgeing instance) throws Exception {
        getSession().saveOrUpdate(instance);
    }

    public void attachClean(ArAgeing instance) throws Exception {
        getSession().buildLockRequest(LockOptions.NONE).lock(instance);
    }

    public List fimdAge(String acctno) throws Exception {
        List result = new ArrayList();
        String queryString = "select a.ageCurrent,a.age3060,a.age6090,a.ageGt90,a.total from ArAgeing a where a.custNo='" + acctno + "'";
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
