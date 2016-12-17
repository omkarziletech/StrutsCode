package com.gp.cong.logisoft.hibernate.dao;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import com.gp.cong.logisoft.domain.AirRates;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.Ports;
import com.gp.cong.logisoft.domain.RefTerminal;

public class AirRatesDAO extends BaseHibernateDAO {

    public void save(AirRates transientInstance) throws Exception {
        getCurrentSession().save(transientInstance);
        getCurrentSession().flush();
    }

    public List findAllAirRates() throws Exception {
        String queryString = "from AirRates Order by airRatesId";
        Query queryObject = getCurrentSession().createQuery(queryString);
        return queryObject.list();
    }

    public List findForSearchAirRatesAction(RefTerminal originTerminal, Ports destAirPort, GenericCode comCode, String match) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(AirRates.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (originTerminal != null && !originTerminal.equals("")) {
            criteria.add(Restrictions.like("originTerminal", originTerminal));
        }
        if (destAirPort != null && !destAirPort.equals("")) {
            criteria.add(Restrictions.like("destinationPort", destAirPort));
        }
        if (comCode != null && !comCode.equals("")) {
            criteria.add(Restrictions.like("commodityCode", comCode));
        }

        return criteria.list();
    }

    public List findForSearchAirRatesn(RefTerminal originTerminal, Ports destAirPort, GenericCode comCode, String match) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(AirRates.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (originTerminal != null && !originTerminal.equals("")) {
            criteria.add(Restrictions.ge("originTerminal", originTerminal));
        }
        if (destAirPort != null && !destAirPort.equals("")) {
            criteria.add(Restrictions.ge("destinationPort", destAirPort));
        }
        if (comCode != null && !comCode.equals("")) {
            criteria.add(Restrictions.ge("commodityCode", comCode));
        }
        return criteria.list();
    }
}
