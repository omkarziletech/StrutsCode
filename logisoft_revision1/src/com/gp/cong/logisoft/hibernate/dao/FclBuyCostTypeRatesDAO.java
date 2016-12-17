package com.gp.cong.logisoft.hibernate.dao;

import java.util.List;
import org.hibernate.Query;
import com.gp.cong.logisoft.domain.FclBuyCostTypeRates;

public class FclBuyCostTypeRatesDAO extends BaseHibernateDAO {

    public void save(FclBuyCostTypeRates transientInstance) throws Exception {
        getCurrentSession().save(transientInstance);
    }

    public List getFclBuyCostTypeRates(Integer fclCostId)throws Exception {
        Query queryObject = null;
            String queryString = "from FclBuyCostTypeRates where fclCostId=? order by unitType.id";
            queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setInteger(0, fclCostId);
            List li = (List) queryObject.list();
            return li;
    }
}
