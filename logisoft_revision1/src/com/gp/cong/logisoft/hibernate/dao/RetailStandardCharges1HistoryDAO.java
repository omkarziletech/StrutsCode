package com.gp.cong.logisoft.hibernate.dao;

import java.util.List;
import org.hibernate.Query;
import com.gp.cong.logisoft.domain.RetailStandardCharges1History;

public class RetailStandardCharges1HistoryDAO extends BaseHibernateDAO {


    public void save(RetailStandardCharges1History transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public List findAllStandardHistory(Integer id, Integer code) throws Exception{
            String queryString = " from RetailStandardCharges1History where standardId=?0 and chargeCode.id=?1";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter("0", id);
            queryObject.setParameter("1", code);
            return queryObject.list();
    }
}
