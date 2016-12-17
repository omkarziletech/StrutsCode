package com.gp.cong.logisoft.hibernate.dao;

import java.util.List;
import org.hibernate.Query;
import com.gp.cong.logisoft.domain.LCLColoadCommodityChargesHistory;

public class LCLColoadCommodityChargesHistoryDAO extends BaseHibernateDAO {

    public void save(LCLColoadCommodityChargesHistory transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public List findAllStandardHistory(Integer lCLColoadMaster)throws Exception{
            String queryString = " from LCLColoadCommodityChargesHistory where lclCoLoadId=?0";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter("0", lCLColoadMaster);
            return queryObject.list();
    }
}
