package com.gp.cong.logisoft.hibernate.dao;

import java.util.List;

import org.hibernate.Query;
import com.gp.cong.logisoft.domain.AirRatesHistory;

public class AirRatesHistoryDAO extends BaseHibernateDAO {

    public void save(AirRatesHistory transientInstance) throws Exception {
        getCurrentSession().save(transientInstance);
    }

    public List findAllStandardHistory(Integer standardid) throws Exception {
        String queryString = " from AirRatesHistory where standardId=?0";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", standardid);
        return queryObject.list();
    }
}
