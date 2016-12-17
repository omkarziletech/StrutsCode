package com.gp.cong.logisoft.hibernate.dao;

import java.util.List;

import org.hibernate.Query;

import com.gp.cong.logisoft.domain.UniverseAirFreightHistory;
//import com.gerber.domain.Consignee;

/**
 * @author Gho
 *
 */
public class UniverseAirFreightHistoryDAO extends BaseHibernateDAO {

    public void save(UniverseAirFreightHistory transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public List findAllUniAirHistory(Integer universalid) throws Exception {
            String queryString = " from UniverseAirFreightHistory  where universeId=?0";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter("0", universalid);
            return queryObject.list();
    }
}
