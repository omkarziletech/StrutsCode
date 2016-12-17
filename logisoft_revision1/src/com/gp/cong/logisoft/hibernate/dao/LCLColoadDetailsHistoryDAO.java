package com.gp.cong.logisoft.hibernate.dao;

import java.util.List;
import org.hibernate.Query;
import com.gp.cong.logisoft.domain.LCLColoadDetailsHistory;

public class LCLColoadDetailsHistoryDAO extends BaseHibernateDAO {

    public void save(LCLColoadDetailsHistory transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public List findAllCoLoadHistory(Integer lCLColoadMaster)throws Exception{
            String queryString = " from LCLColoadDetailsHistory where lclCoLoadId=?0";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter("0", lCLColoadMaster);
            return queryObject.list();
    }
}
