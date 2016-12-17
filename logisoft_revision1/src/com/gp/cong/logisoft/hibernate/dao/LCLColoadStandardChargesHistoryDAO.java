package com.gp.cong.logisoft.hibernate.dao;

import java.util.List;
import org.hibernate.Query;
import com.gp.cong.logisoft.domain.LCLColoadStandardChargesHistory;

public class LCLColoadStandardChargesHistoryDAO extends BaseHibernateDAO {

    public void save(LCLColoadStandardChargesHistory transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public List findAllStandardHistory(Integer lCLColoadMaster, Integer code)throws Exception{
            String queryString = " from LCLColoadStandardChargesHistory where lclCoLoadId=?0 and chargeCode.id=?1";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter("0", lCLColoadMaster);
            queryObject.setParameter("1", code);
            return queryObject.list();
    }
}
