package com.gp.cong.logisoft.hibernate.dao;

import java.util.List;

import org.hibernate.Query;
import com.gp.cong.logisoft.domain.UniverseInsuranceChrgHistory;

public class UniverseInsuranceChrgHistoryDAO extends BaseHibernateDAO {

    public void save(UniverseInsuranceChrgHistory transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public List findAllUniInsuHistory(Integer lCLColoadMaster) throws Exception {
            String queryString = " from UniverseInsuranceChrgHistory where universeId=?0";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter("0", lCLColoadMaster);
            return queryObject.list();
    }
}
