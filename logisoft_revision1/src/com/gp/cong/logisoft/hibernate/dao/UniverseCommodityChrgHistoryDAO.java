package com.gp.cong.logisoft.hibernate.dao;

import java.util.List;

import org.hibernate.Query;
import com.gp.cong.logisoft.domain.UniverseCommodityChrgHistory;

public class UniverseCommodityChrgHistoryDAO extends BaseHibernateDAO {


    public void save(UniverseCommodityChrgHistory transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public List findAllUniCommHistory(Integer lCLColoadMaster) throws Exception {
            String queryString = " from UniverseCommodityChrgHistory where universeId=?0";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter("0", lCLColoadMaster);
            return queryObject.list();
    }
}
