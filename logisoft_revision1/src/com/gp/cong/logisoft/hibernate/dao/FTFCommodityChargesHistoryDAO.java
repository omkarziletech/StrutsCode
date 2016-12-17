package com.gp.cong.logisoft.hibernate.dao;

import java.util.List;
import org.hibernate.Query;
import com.gp.cong.logisoft.domain.FTFCommodityChargesHistory;

public class FTFCommodityChargesHistoryDAO extends BaseHibernateDAO {

    public void save(FTFCommodityChargesHistory transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public List findAllStandardHistory(Integer ftfMaster)throws Exception {
            String queryString = " from FTFCommodityChargesHistory where FtfId=?0";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter("0", ftfMaster);
            return queryObject.list();
    }
}
