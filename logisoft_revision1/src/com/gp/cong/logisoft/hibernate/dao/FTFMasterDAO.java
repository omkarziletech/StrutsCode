package com.gp.cong.logisoft.hibernate.dao;

import java.util.List;
import java.util.Iterator;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import com.gp.cong.logisoft.domain.FTFMaster;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.PortsTemp;
import com.gp.cong.logisoft.domain.RefTerminalTemp;

public class FTFMasterDAO extends BaseHibernateDAO {

    public List findAllftfRates()throws Exception {
            String queryString = "from FTFMaster";
            Query queryObject = getCurrentSession().createQuery(queryString);
            return queryObject.list();
    }

    /**
     * @param orgTerminal
     * @param destAirPort
     * @param comCode
     * @return
     */
    public List getRecordsForCommRetail(RefTerminalTemp orgTerminal,
            PortsTemp destAirPort, GenericCode comCode) throws Exception {
            Criteria criteria = getCurrentSession().createCriteria(
                    FTFMaster.class);
            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            if (orgTerminal != null && !orgTerminal.equals("")) {
                criteria.add(Restrictions.like("orgTerminal", orgTerminal));
            }
            if (destAirPort != null && !destAirPort.equals("")) {
                criteria.add(Restrictions.like("destPort", destAirPort));
            }
            if (comCode != null && !comCode.equals("")) {
                criteria.add(Restrictions.like("comCode", comCode));
            }
            return criteria.list();
    }

    /**
     * @param transientInstance
     * @param userName
     */
    public void save(FTFMaster transientInstance, String userName) throws Exception {
        getCurrentSession().save(transientInstance);
        getCurrentSession().flush();
    }

    /**
     * @param persistanceInstance
     * @param userName
     */
    public void update(FTFMaster persistanceInstance, String userName) throws Exception {
            getCurrentSession().update(persistanceInstance);
            getCurrentSession().flush();
    }

    /**
     * @param id
     * @return
     */
    public FTFMaster findById(Integer id) throws Exception {
            FTFMaster instance = (FTFMaster) getCurrentSession().get(
                    "com.gp.cong.logisoft.domain.FTFMaster", id);
            return instance;
    }

    /**
     * @param orgTerminal
     * @param destAirPort
     * @param comCode
     * @param match
     * @return
     */
    public List findForSearchftfRatesmatch(String orgTerminal,
            String destAirPort, String comCode, String match)throws Exception {
            Criteria criteria = getCurrentSession().createCriteria(
                    FTFMaster.class);
            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            if (orgTerminal != null && !orgTerminal.equals("")) {
                criteria.add(Restrictions.like("orgTerminal",
                        orgTerminal + "%"));
            }
            if (destAirPort != null && !destAirPort.equals("")) {
                criteria.add(Restrictions.like("destPort", destAirPort + "%"));
            }
            if (comCode != null && !comCode.equals("")) {
                criteria.add(Restrictions.like("comCode", comCode + "%"));
            }
            return criteria.list();
    }

    /**
     * @param orgTerminal
     * @param destAirPort
     * @param comCode
     * @param match
     * @return
     */
    public List findForSearchftfRatesstarts(String orgTerminal,
            String destAirPort, String comCode, String match)throws Exception {
            Criteria criteria = getCurrentSession().createCriteria(
                    FTFMaster.class);
            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            if (orgTerminal != null && !orgTerminal.equals("")) {
                criteria.add(Restrictions.eq("orgTerminal", orgTerminal));
            }
            if (destAirPort != null && !destAirPort.equals("")) {
                criteria.add(Restrictions.eq("destPort", destAirPort));
            }
            if (comCode != null && !comCode.equals("")) {
                criteria.add(Restrictions.ge("comCode", comCode));
            }
            return criteria.list();
    }

    /**
     * @param orgTerminal
     * @param destAirPort
     * @param comCode
     * @return
     */
    public List getRecordsForComm(RefTerminalTemp orgTerminal,
            PortsTemp destAirPort, GenericCode comCode)throws Exception {
            Criteria criteria = getCurrentSession().createCriteria(
                    FTFMaster.class);
            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            if (orgTerminal != null && !orgTerminal.equals("")) {
                criteria.add(Restrictions.like("orgTerminal", orgTerminal));
            }
            if (destAirPort != null && !destAirPort.equals("")) {
                criteria.add(Restrictions.like("destPort", destAirPort));
            }
            if (comCode != null && !comCode.equals("")) {
                criteria.add(Restrictions.like("comCode", comCode));
            }
            return criteria.list();
    }

    /**
     * @param persistanceInstance
     * @param userName
     */
    public void delete(FTFMaster persistanceInstance, String userName)throws Exception {
            getSession().delete(persistanceInstance);
            getSession().flush();
    }

    /**
     * @param org
     * @param des
     * @param com_code
     * @return
     */
    public List findAllDetails(String org, String des, String com_code)throws Exception {
        List li = null;
            String queryString = " from FTFMaster where orgTerminal=?0 and destPort=?1 and comCode=?2";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter("0", org);
            queryObject.setParameter("1", des);
            queryObject.setParameter("2", com_code);
            li = queryObject.list();
        return li;
    }

    /**
     * @param org
     * @param des
     * @return
     */
    public Iterator getAllftfStandardChargesForDisplay(RefTerminalTemp org,
            PortsTemp des) throws Exception {
        String results = null;
        Iterator it = null;
            results = "select a from FTFStandardCharges a,FTFMaster b where a.standard='Y'and a.FtfId=b.id and b.orgTerminal=?0 and b.destPort=?1";
            Query queryObject = getCurrentSession().createQuery(results);
            queryObject.setParameter("0", org);
            queryObject.setParameter("1", des);
            it = queryObject.list().iterator();
        return it;

    }
}
