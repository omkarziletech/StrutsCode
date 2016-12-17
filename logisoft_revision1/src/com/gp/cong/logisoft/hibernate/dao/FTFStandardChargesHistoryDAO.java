package com.gp.cong.logisoft.hibernate.dao;

import java.util.List;
import org.hibernate.Query;
import com.gp.cong.logisoft.domain.FTFStandardChargesHistory;

public class FTFStandardChargesHistoryDAO extends BaseHibernateDAO {

    public void save(FTFStandardChargesHistory transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public List findAllStandardHistory(Integer ftfMaster, Integer code)throws Exception {
            String queryString = " from FTFStandardChargesHistory where FtfId=?0 and chargeCode.id=?1";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter("0", ftfMaster);
            queryObject.setParameter("1", code);
            return queryObject.list();
    }
}
