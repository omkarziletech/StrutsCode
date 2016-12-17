package com.gp.cong.logisoft.hibernate.dao;

import java.util.List;
import org.hibernate.Query;
import com.gp.cong.logisoft.domain.FclBuyCostTypeFutureRates;

public class FclBuyCostFutureRatesDAO extends BaseHibernateDAO {

    public void save(FclBuyCostTypeFutureRates transientInstance) throws Exception {
        getCurrentSession().save(transientInstance);
        getCurrentSession().flush();
    }

    public void update(FclBuyCostTypeFutureRates persistanceInstance)throws Exception {
            getCurrentSession().update(persistanceInstance);
            getCurrentSession().flush();
    }

    public void delete(FclBuyCostTypeFutureRates persistanceInstance)throws Exception {
            getCurrentSession().delete(persistanceInstance);
            getCurrentSession().flush();
    }

    public FclBuyCostTypeFutureRates findById(Integer id)throws Exception {
            FclBuyCostTypeFutureRates instance = (FclBuyCostTypeFutureRates) getCurrentSession().get("com.gp.cong.logisoft.domain.FclBuyCostTypeFutureRates", id);
            return instance;
    }

    public FclBuyCostTypeFutureRates findFclBuyFutureByCostId(Integer id) throws Exception {
            String queryString1 = "from FclBuyCostTypeFutureRates where fclCostId=?0";
            Query queryObject = getCurrentSession().createQuery(queryString1);
            queryObject.setParameter("0", id);
            FclBuyCostTypeFutureRates ff = null;
            if (queryObject.list().size() > 0) {
                ff = (FclBuyCostTypeFutureRates) queryObject.list().get(0);
            }
            return ff;
    }

    public List findFclBuyFutureByCostAll(Integer id)throws Exception {
            String queryString1 = "from FclBuyCostTypeFutureRates where fclCostId=?0";
            Query queryObject = getCurrentSession().createQuery(queryString1);
            queryObject.setParameter("0", id);
            //queryObject.setParameter(1, stid);
            return queryObject.list();
    }
}
