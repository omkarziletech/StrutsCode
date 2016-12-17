package com.gp.cong.logisoft.hibernate.dao;

import java.util.List;
import org.hibernate.Query;

import com.gp.cong.logisoft.domain.RetailCommodityChargesHistory;

public class RetailCommodityChargesHistoryDAO extends BaseHibernateDAO {


    public void save(RetailCommodityChargesHistory transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public List findAllStandardHistory1(Integer id) throws Exception{
            String queryString = " from RetailCommodityChargesHistory where retailRatesId=?0";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter("0", id);
            return queryObject.list();
    }
}
