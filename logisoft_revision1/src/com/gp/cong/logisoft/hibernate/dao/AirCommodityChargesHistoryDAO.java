package com.gp.cong.logisoft.hibernate.dao;

import java.util.List;
import org.hibernate.Query;

import com.gp.cong.logisoft.domain.AirCommodityChargesHistory;

public class AirCommodityChargesHistoryDAO extends BaseHibernateDAO {

    public void save(AirCommodityChargesHistory transientInstance) throws Exception {
            getSession().save(transientInstance);
    }

    public List findAllStandardHistory(Integer standardId) throws Exception {
        String queryString = " from AirCommodityChargesHistory where standardId=?0";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", standardId);
        return queryObject.list();
    }
}
