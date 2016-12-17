package com.gp.cong.logisoft.hibernate.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.PortsTemp;
import com.gp.cong.logisoft.domain.RefTerminalTemp;
import com.gp.cong.logisoft.domain.RetailWeightRangesRates;
import com.gp.cong.logisoft.domain.RetailWeightRangesRatesHistory;

public class RetailWeightRangesRatesHistoryDAO extends BaseHibernateDAO {

    public void save(RetailWeightRangesRatesHistory transientInstance) throws Exception {
        getCurrentSession().save(transientInstance);
        getCurrentSession().flush();
    }

    public List findAllRetailRates() throws Exception {
        String queryString = "from RetailWeightRangesRates Order by retailRatesId";
        Query queryObject = getCurrentSession().createQuery(queryString);
        return queryObject.list();
    }

    public List findAllStandardHistory(Integer id) throws Exception {
        String queryString = " from RetailWeightRangesRatesHistory where retailRatesId=?0";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", id);
        return queryObject.list();
    }

    public List findForSearchRetailRatesAction(RefTerminalTemp originTerminal, PortsTemp destAirPort, GenericCode comCode, String match) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(RetailWeightRangesRatesHistory.class);
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

    public List findForSearchRetailRatesn(RefTerminalTemp originTerminal, PortsTemp destAirPort, GenericCode comCode, String match) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(RetailWeightRangesRates.class);
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
