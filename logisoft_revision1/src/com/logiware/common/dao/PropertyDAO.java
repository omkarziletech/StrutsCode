/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.common.dao;

import com.logiware.common.domain.Property;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import java.io.Serializable;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Balaji.E
 */
public class PropertyDAO extends BaseHibernateDAO {

    public Property findById(Serializable id) {
        return (Property) getCurrentSession().get(Property.class, id);
    }

    public List<Property> getCommonProperties() {
        getCurrentSession().flush();
        Criteria criteria = getCurrentSession().createCriteria(Property.class);
        criteria.add(Restrictions.eq("type", "COMMON").ignoreCase());
        return criteria.list();
    }

    public List<Property> getDomesticProperties() {
        getCurrentSession().flush();
        Criteria criteria = getCurrentSession().createCriteria(Property.class);
        criteria.add(Restrictions.eq("type", "DOMESTIC").ignoreCase());
        return criteria.list();
    }

    public List<Property> getAccountingProperties() {
        getCurrentSession().flush();
        Criteria criteria = getCurrentSession().createCriteria(Property.class);
        criteria.add(Restrictions.eq("type", "ACCOUNTING").ignoreCase());
        return criteria.list();
    }

    public List<Property> getFclProperties() {
        getCurrentSession().flush();
        Criteria criteria = getCurrentSession().createCriteria(Property.class);
        criteria.add(Restrictions.eq("type", "FCL").ignoreCase());
        return criteria.list();
    }

    public List<Property> getLclProperties() {
        getCurrentSession().flush();
        Criteria criteria = getCurrentSession().createCriteria(Property.class);
        criteria.add(Restrictions.eq("type", "LCL").ignoreCase());
        return criteria.list();
    }

    public String getProperty(String propertyName) {
        StringBuilder querybBuilder = new StringBuilder();
        querybBuilder.append("select value from property where name='").append(propertyName).append("' limit 1");
        Query query = getCurrentSession().createSQLQuery(querybBuilder.toString());
        String propertyValue = (String) query.uniqueResult();
        return null != propertyValue ? propertyValue : "";
    }
}
