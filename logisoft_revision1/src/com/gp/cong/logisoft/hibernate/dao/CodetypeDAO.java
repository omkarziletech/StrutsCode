package com.gp.cong.logisoft.hibernate.dao;

import com.gp.cong.logisoft.domain.Codetype;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;

/**
 * Data access object (DAO) for domain model class Codetype.
 *
 * @see com.gp.cong.logisoft.hibernate.dao.Codetype
 * @author MyEclipse - Hibernate Tools
 */
public class CodetypeDAO extends BaseHibernateDAO {

    public static final String DESCRIPTION = "description";

    public void save(Codetype transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public void delete(Codetype persistentInstance) throws Exception {
        getSession().delete(persistentInstance);
        getSession().flush();
    }

    public Codetype findById(java.lang.Integer id) throws Exception {
        Codetype instance = (Codetype) getSession().get("com.gp.cong.logisoft.domain.Codetype", id);
        return instance;
    }

    public List findByExample(Codetype instance) throws Exception {
        List results = getSession().createCriteria("com.gp.cong.logisoft.domain.Codetype").add(Example.create(instance)).list();
        return results;
    }

    public List findByProperty(String propertyName, Object value) throws Exception {
        String queryString = "from Codetype as model where model." + propertyName + "= ?0";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", value);
        return queryObject.list();
    }

    public List findByDescription(Object description) throws Exception {
        return findByProperty(DESCRIPTION, description);
    }

    public Codetype merge(Codetype detachedInstance) throws Exception {
        Codetype result = (Codetype) getSession().merge(detachedInstance);
        return result;
    }

    public void attachDirty(Codetype instance) throws Exception {
        getSession().saveOrUpdate(instance);
        getSession().flush();
    }

    public void attachClean(Codetype instance) throws Exception {
        getSession().buildLockRequest(LockOptions.NONE).lock(instance);
    }

    public Iterator getAllCodesForDisplay() throws Exception {
        Iterator results = null;
        results = getCurrentSession().createQuery(
                "select codetype.codetypeid,codetype.description from Codetype codetype where editable = '1' order by codetype.description").list().iterator();
        return results;
    }

    public List getCodeTypeDescription(String codeTypeId) throws Exception {
        String query = "select description,loadFromBluescreen from Codetype where codetypeid='" + codeTypeId + "'";
        List descriptionList = getCurrentSession().createQuery(query).list();
        return descriptionList;
    }

    public Integer getCodeTypeId(String description) throws Exception {
        String query = "select codetypeid from codetype where description = '" + description + "'";
        Integer id = (Integer) getCurrentSession().createSQLQuery(query).uniqueResult();
        return null != id ? id : 0;
    }

    public List<Codetype> getCodeTypes() {
        Criteria criteria = getCurrentSession().createCriteria(Codetype.class);
        criteria.addOrder(Order.asc("description"));
        return criteria.list();
    }
}
