package com.gp.cong.logisoft.hibernate.dao;

import java.util.List;
import org.hibernate.Query;
import com.gp.cong.logisoft.domain.UniverseFlatRateHistory;

/**
 * @author Gho
 *
 */
public class UniverseFlatRateHistoryDAO extends BaseHibernateDAO {

    public void save(UniverseFlatRateHistory transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public List findAllUniversalHistory(Integer universalid) throws Exception {
            String queryString = " from UniverseFlatRateHistory where universeId=?0";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter("0", universalid);
            return queryObject.list();
    }
}
