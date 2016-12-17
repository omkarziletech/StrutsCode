package com.gp.cong.logisoft.hibernate.dao;

import java.util.List;
import java.util.Iterator;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.LCLColoadMaster;
import com.gp.cong.logisoft.domain.PortsTemp;
import com.gp.cong.logisoft.domain.RefTerminalTemp;

public class LCLColoadMasterDAO extends BaseHibernateDAO {

    public List findAllLCLColoadRates() throws Exception{
            String queryString = "from LCLColoadMaster";
            Query queryObject = getCurrentSession().createQuery(queryString);
            return queryObject.list();
    }

    public List getRecordsForCommRetail(RefTerminalTemp originTerminal, PortsTemp destAirPort, GenericCode comCode)throws Exception{
            Criteria criteria = getCurrentSession().createCriteria(LCLColoadMaster.class);
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

    public List getRecordsForComm(RefTerminalTemp originTerminal, PortsTemp destAirPort, GenericCode comCode)throws Exception{
            Criteria criteria = getCurrentSession().createCriteria(LCLColoadMaster.class);
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

    public void save(LCLColoadMaster transientInstance, String userName) throws Exception {
        getCurrentSession().save(transientInstance);
        getCurrentSession().flush();
    }

    public void update(LCLColoadMaster persistanceInstance, String userName) throws Exception{
            getCurrentSession().update(persistanceInstance);
    }

    public LCLColoadMaster findById(Integer id) throws Exception{
            LCLColoadMaster instance = (LCLColoadMaster) getCurrentSession().get("com.gp.cong.logisoft.domain.LCLColoadMaster", id);
            return instance;
    }

    public List findForSearchlclcoRatesmatch(String originTerminal, String destAirPort, String comCode, String match)throws Exception{
            Criteria criteria = getCurrentSession().createCriteria(LCLColoadMaster.class);
            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            if (originTerminal != null && !originTerminal.equals("")) {
                criteria.add(Restrictions.like("originTerminal", originTerminal + "%"));
            }
            if (destAirPort != null && !destAirPort.equals("")) {
                criteria.add(Restrictions.like("destinationPort", destAirPort + "%"));
            }
            if (comCode != null && !comCode.equals("")) {
                criteria.add(Restrictions.like("commodityCode", comCode + "%"));
            }
            return criteria.list();
    }

    public List findForSearchlclcoRatesstarts(String originTerminal, String destAirPort, String comCode, String match)throws Exception{
            Criteria criteria = getCurrentSession().createCriteria(LCLColoadMaster.class);
            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            if (originTerminal != null && !originTerminal.equals("")) {
                criteria.add(Restrictions.eq("originTerminal", originTerminal));
            }
            if (destAirPort != null && !destAirPort.equals("")) {
                criteria.add(Restrictions.eq("destinationPort", destAirPort));
            }
            if (comCode != null && !comCode.equals("")) {
                criteria.add(Restrictions.ge("commodityCode", comCode));
            }
            return criteria.list();
    }

    public void delete(LCLColoadMaster persistanceInstance, String userName)throws Exception{

            getSession().delete(persistanceInstance);
    }

    public List findAllDetails(String org, String des, String com_code)throws Exception{
        List li = null;
            String queryString = " from LCLColoadMaster where originTerminal=?0 and destinationPort=?1 and commodityCode=?2";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter("0", org);
            queryObject.setParameter("1", des);
            queryObject.setParameter("2", com_code);
            li = queryObject.list();
        return li;
    }

    public Iterator getAllcoStandardChargesForDisplay(RefTerminalTemp org, PortsTemp des)throws Exception{
        String results = null;
        Iterator it = null;
            results = "select a from AirStandardCharges a,StandardCharges b where a.standard='Y'and a.standardId=b.id and b.orgTerminal=?0 and b.destPort=?1";
            Query queryObject = getCurrentSession().createQuery(results);
            queryObject.setParameter("0", org);
            queryObject.setParameter("1", des);
            it = queryObject.list().iterator();
        return it;
    }
}
