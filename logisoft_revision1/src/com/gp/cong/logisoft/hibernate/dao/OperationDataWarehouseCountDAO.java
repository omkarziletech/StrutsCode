package com.gp.cong.logisoft.hibernate.dao;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import com.gp.cong.logisoft.domain.Country;
import com.gp.cong.logisoft.domain.OperationDataWarehouseCount;
import java.util.Date;

public class OperationDataWarehouseCountDAO extends BaseHibernateDAO {

    private static final Log log = LogFactory.getLog(InlandSummaryDAO.class);

    public void save(OperationDataWarehouseCount operationDataWarehouseCount) {
        log.debug("saving OperationDataWarehouseCount instance");
        try {
            getCurrentSession().save(operationDataWarehouseCount);
        } catch (RuntimeException re) {
            log.info("save failed in OperationDataWarehouseCountDAO on " + new Date(),re);
            throw re;
        }
    }

    public void delete(OperationDataWarehouseCount operationDataWarehouseCount)throws Exception {
        try {
            getCurrentSession().delete(operationDataWarehouseCount);
        } catch (RuntimeException re) {
                log.info("delete failed in OperationDataWarehouseCountDAO on " + new Date(),re);
            throw re;
        }
    }

    public Country findById(java.lang.Integer id)throws Exception {
        try {
            Country instance = (Country) getCurrentSession().get("com.gp.cong.logisoft.domain.OperationDataWarehouseCount", id);
            return instance;
        } catch (RuntimeException re) {
            log.info("findById failed in OperationDataWarehouseCountDAO on " + new Date(),re);
            throw re;
        }
    }

    public List findByExample(Country instance)throws Exception {
        try {
            List results = getCurrentSession().createCriteria("com.gp.cong.logisoft.domain.OperationDataWarehouseCount").add(Example.create(instance)).list();
            return results;
        } catch (RuntimeException re) {
            log.info("findByExample failed in OperationDataWarehouseCountDAO on " + new Date(),re);
            throw re;
        }
    }

    public List getAllOperationDataWarehouseCount()throws Exception {
        try {
            String queryString = "from OperationDataWarehouseCount";
            Query queryObject = getCurrentSession().createQuery(queryString);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.info("getAllOperationDataWarehouseCount failed in OperationDataWarehouseCountDAO on " + new Date(),re);
            throw re;
        }
    }

    public List getAllOperationDataWarehouseCountForLatestMonth(Integer limit)throws Exception {
        try {
            String queryString = "from OperationDataWarehouseCount dc order by str_to_date(concat(dc.month,' 1,',dc.year), '%M %d,%Y')";
            Query queryObject = getCurrentSession().createQuery(queryString).setMaxResults(limit);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.info("getAllOperationDataWarehouseCountForLatestMonth failed in OperationDataWarehouseCountDAO on " + new Date(),re);
            throw re;
        }
    }
}
