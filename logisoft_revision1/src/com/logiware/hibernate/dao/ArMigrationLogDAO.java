package com.logiware.hibernate.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.logiware.form.ArMigrationLogForm;
import com.logiware.hibernate.domain.ArMigrationLog;
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
public class ArMigrationLogDAO extends BaseHibernateDAO implements java.io.Serializable {

    public void save(ArMigrationLog transientInstance) throws Exception {
        getCurrentSession().flush();
        getCurrentSession().saveOrUpdate(transientInstance);
    }

    public void update(ArMigrationLog persistentInstance) throws Exception {
        getCurrentSession().flush();
        getCurrentSession().update(persistentInstance);
    }

    public void delete(ArMigrationLog persistentInstance) throws Exception {
        getCurrentSession().flush();
        getCurrentSession().delete(persistentInstance);
    }

    public ArMigrationLog findById(Integer id) throws Exception {
        getCurrentSession().flush();
        ArMigrationLog instance = (ArMigrationLog) getCurrentSession().get("com.logiware.hibernate.domain.ArMigrationLog", id);
        return instance;
    }

    public List<ArMigrationLog> findByProperty(String propertyName, Object value) throws Exception {
        getCurrentSession().flush();
        Query query = getCurrentSession().createQuery("from ArMigrationLog as model where model." + propertyName + "= ?0");
        query.setParameter("0", value);
        return query.list();
    }

    public Criteria createCriteria(ArMigrationLogForm arMigrationLogForm) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(ArMigrationLog.class);
        if (arMigrationLogForm.isMissingCharges()) {
            criteria.add(Restrictions.like("error", "%charges are missing in bluescreen%"));
        } else {
            criteria.add(Restrictions.not(Restrictions.like("error", "%charges are missing in bluescreen%")));
        }
        if (CommonUtils.isNotEmpty(arMigrationLogForm.getLogType())) {
            criteria.add(Restrictions.eq("logType", arMigrationLogForm.getLogType()));
        }
        if (CommonUtils.isNotEmpty(arMigrationLogForm.getBillOfLading())) {
            criteria.add(Restrictions.eq("blNumber", arMigrationLogForm.getBillOfLading()));
        }
        Date fromDate = null;
        if (CommonUtils.isNotEmpty(arMigrationLogForm.getFromDate())) {
            fromDate = DateUtils.parseDate(arMigrationLogForm.getFromDate() + " 00:00:00", "MM/dd/yyyy HH:mm:ss");
        }
        Date toDate = null;
        if (CommonUtils.isNotEmpty(arMigrationLogForm.getToDate())) {
            toDate = DateUtils.parseDate(arMigrationLogForm.getToDate() + " 23:59:59", "MM/dd/yyyy HH:mm:ss");
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

    public List<ArMigrationLog> getLogs(Criteria criteria, String sortBy, String orderBy, int start, int end) throws Exception {
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
