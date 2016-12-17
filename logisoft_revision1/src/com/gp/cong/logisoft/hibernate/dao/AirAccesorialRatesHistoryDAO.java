package com.gp.cong.logisoft.hibernate.dao;

import java.util.List;
import org.hibernate.Query;
import com.gp.cong.logisoft.domain.AirAccesorialRatesHistory;

public class AirAccesorialRatesHistoryDAO extends BaseHibernateDAO {

    public void save(AirAccesorialRatesHistory transientInstance) throws Exception {
        getSession().save(transientInstance);
    }

    public List findAllStandardHistory(Integer standardid, Integer code) throws Exception {
        String queryString = " from AirAccesorialRatesHistory where standardId=?0 and chargeCode.id=?1";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", standardid);
        queryObject.setParameter("1", code);
        return queryObject.list();
    }
}
