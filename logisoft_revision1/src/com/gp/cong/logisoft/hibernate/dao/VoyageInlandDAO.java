package com.gp.cong.logisoft.hibernate.dao;

import java.util.List;
import java.util.Iterator;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import com.gp.cong.logisoft.domain.CarriersOrLineTemp;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.PortsTemp;
import com.gp.cong.logisoft.domain.RefTerminalTemp;
import com.gp.cong.logisoft.domain.VoyageInland;
import com.gp.cong.logisoft.domain.VoyageMaster;

public class VoyageInlandDAO extends BaseHibernateDAO {


    public List findAllftfRates() throws Exception {
            String queryString = "from VoyageMaster";
            Query queryObject = getCurrentSession().createQuery(queryString);
            return queryObject.list();
    }

    public List findAllDetails(RefTerminalTemp org, PortsTemp des, GenericCode com_code, CarriersOrLineTemp SSLine) throws Exception {
        List li = null;
            String queryString = " from VoyageMaster where originTerminal=?0 and destinationPort=?1 and comNum=?2 and sslineNo=?3";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter("0", org);
            queryObject.setParameter("1", des);
            queryObject.setParameter("2", com_code);
            queryObject.setParameter("3", SSLine);
            li = queryObject.list();
        return li;
    }

    public List getRecordsForCommRetail(RefTerminalTemp originTerminal, PortsTemp destAirPort, GenericCode comCode) throws Exception {
            Criteria criteria = getCurrentSession().createCriteria(VoyageMaster.class);
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

    public void save(VoyageInland transientInstance, String userName) throws Exception {
        getCurrentSession().save(transientInstance);
        getCurrentSession().flush();
    }

    public void update(VoyageInland persistanceInstance, String userName) throws Exception {
            getCurrentSession().update(persistanceInstance);
    }

    public VoyageInland findById(Integer id) throws Exception {
            VoyageInland instance = (VoyageInland) getCurrentSession().get("com.gp.cong.logisoft.domain.VoyageInland", id);
            return instance;
    }

    public List findForSearchVoyageRatesmatch(String originTerminal, String destAirPort, String VoyageNo, String match) throws Exception {
            Criteria criteria = getCurrentSession().createCriteria(VoyageInland.class);
            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            if (originTerminal != null && !originTerminal.equals("")) {
                criteria.createCriteria("originTerminal").add(Restrictions.like("trmnum", originTerminal + "%"));
            }
            if (destAirPort != null && !destAirPort.equals("")) {
                criteria.createCriteria("destTerminal").add(Restrictions.like("shedulenumber", destAirPort + "%"));
            }
            if (VoyageNo != null && !VoyageNo.equals("")) {
                criteria.add(Restrictions.like("inlandVoyageNo", Integer.parseInt(VoyageNo)));
            }
            return criteria.list();
    }

    public List findForSearchVoyageRatesstarts(String originTerminal, String destAirPort, String VoyageNo, String match) throws Exception {
            Criteria criteria = getCurrentSession().createCriteria(VoyageInland.class);
            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            if (originTerminal != null && !originTerminal.equals("")) {
                criteria.createCriteria("originTerminal").add(Restrictions.ge("trmnum", originTerminal + "%"));
            }
            if (destAirPort != null && !destAirPort.equals("")) {
                criteria.createCriteria("destTerminal").add(Restrictions.ge("shedulenumber", destAirPort + "%"));
            }
            if (VoyageNo != null && !VoyageNo.equals("")) {
                criteria.add(Restrictions.like("inlandVoyageNo", Integer.parseInt(VoyageNo)));
            }
            return criteria.list();
    }

    public List getRecordsForComm(RefTerminalTemp originTerminal, PortsTemp destAirPort) throws Exception {
            Criteria criteria = getCurrentSession().createCriteria(VoyageInland.class);
            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            if (originTerminal != null && !originTerminal.equals("")) {
                criteria.add(Restrictions.like("originTerminal", originTerminal));
            }
            if (destAirPort != null && !destAirPort.equals("")) {
                criteria.add(Restrictions.like("destTerminal", destAirPort));
            }
            return criteria.list();
    }

    public void delete(VoyageInland persistanceInstance, String userName) throws Exception {
            getSession().delete(persistanceInstance);
    }

    public List findAllDetails(RefTerminalTemp org, PortsTemp des, Integer VoyNo) throws Exception {
        List li = null;
            String queryString = " from VoyageInland where originTerminal=?0 and destTerminal=?1 and inlandVoyageNo=?2";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter("0", org);
            queryObject.setParameter("1", des);
            queryObject.setParameter("2", VoyNo);
            li = queryObject.list();
        return li;
    }

    public Iterator getAllftfStandardChargesForDisplay(RefTerminalTemp org, PortsTemp des) throws Exception {
        String results = null;
        Iterator it = null;
            results = "select a from FTFStandardCharges a,FTFMaster b where a.standard='Y'and a.FtfId=b.id and b.originTerminal=?0 and b.destinationPort=?1";
            Query queryObject = getCurrentSession().createQuery(results);
            queryObject.setParameter("0", org);
            queryObject.setParameter("1", des);
            it = queryObject.list().iterator();
        return it;

    }
}
