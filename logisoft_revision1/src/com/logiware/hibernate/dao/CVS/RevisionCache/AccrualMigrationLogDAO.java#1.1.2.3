package com.logiware.hibernate.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.logiware.form.AccrualMigrationLogForm;
import com.logiware.hibernate.domain.AccrualMigrationLog;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Lakshmi Narayanan
 */
public class AccrualMigrationLogDAO extends BaseHibernateDAO implements java.io.Serializable {

    public void save(AccrualMigrationLog transientInstance) throws Exception {
        getCurrentSession().flush();
        getCurrentSession().saveOrUpdate(transientInstance);
    }

    public void update(AccrualMigrationLog persistentInstance) throws Exception {
        getCurrentSession().flush();
        getCurrentSession().update(persistentInstance);
    }

    public void delete(AccrualMigrationLog persistentInstance) throws Exception {
        getCurrentSession().flush();
        getCurrentSession().delete(persistentInstance);
    }

    public AccrualMigrationLog findById(Integer id) throws Exception {
        getCurrentSession().flush();
        AccrualMigrationLog instance = (AccrualMigrationLog) getCurrentSession().get("com.logiware.hibernate.domain.AccrualMigrationLog", id);
        return instance;
    }

    public List<AccrualMigrationLog> findByProperty(String propertyName, Object value) throws Exception {
        getCurrentSession().flush();
        Query query = getCurrentSession().createQuery("from AccrualMigrationLog as model where model." + propertyName + "= ?0");
        query.setParameter("0", value);
        return query.list();
    }

    public Criteria createCriteria(AccrualMigrationLogForm form) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(AccrualMigrationLog.class);
        if (CommonUtils.isNotEmpty(form.getLogType())) {
            criteria.add(Restrictions.eq("logType", form.getLogType()));
        }
        if (CommonUtils.isEqual(form.getSearchBy(), "amount") && CommonUtils.isNotEmpty(form.getSearchValue())) {
            criteria.add(Restrictions.eq(form.getSearchBy(), Double.parseDouble(form.getSearchValue().trim().replace(",", ""))));
        } else if (CommonUtils.isNotEmpty(form.getSearchBy()) && CommonUtils.isNotEmpty(form.getSearchValue())) {
            criteria.add(Restrictions.like(form.getSearchBy(), "%" + form.getSearchValue().trim() + "%"));
        }
        Date fromDate = null;
        if (CommonUtils.isNotEmpty(form.getFromDate())) {
            fromDate = DateUtils.parseDate(form.getFromDate() + " 00:00:00", "MM/dd/yyyy HH:mm:ss");
        }
        Date toDate = null;
        if (CommonUtils.isNotEmpty(form.getToDate())) {
            toDate = DateUtils.parseDate(form.getToDate() + " 23:59:59", "MM/dd/yyyy HH:mm:ss");
        }
        if (null != fromDate && null != toDate) {
            criteria.add(Restrictions.between("reportedDate", fromDate, toDate));
        } else if (null != fromDate) {
            criteria.add(Restrictions.ge("reportedDate", fromDate));
        } else if (null != toDate) {
            criteria.add(Restrictions.lt("reportedDate", toDate));
        }
        return criteria;
    }

    public Integer getTotalNoOfRecords(Criteria criteria) {
        Object result = criteria.setProjection(Projections.rowCount()).uniqueResult();
        return Integer.valueOf(null != result ? Integer.parseInt(result.toString()) : 0);
    }

    public List<AccrualMigrationLog> getLogs(Criteria criteria, String sortBy, String orderBy, int start, int end) throws Exception {
        if ("asc".equals(orderBy)) {
            criteria.addOrder(Order.asc(sortBy));
        } else {
            criteria.addOrder(Order.desc(sortBy));
        }
        criteria.setFirstResult(start);
        criteria.setMaxResults(end);
        criteria.setProjection(null);
        return criteria.list();
    }
}
