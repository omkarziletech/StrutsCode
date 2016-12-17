package com.gp.cong.logisoft.hibernate.dao;

import java.util.ArrayList;
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
import com.gp.cong.logisoft.domain.UnitOsdInfo;
import com.gp.cong.logisoft.domain.VoyageExport;

public class UnitOsdInfoDAO extends BaseHibernateDAO {


    public List findAllftfRates() throws Exception {
            String queryString = "from VoyageExport";
            Query queryObject = getCurrentSession().createQuery(queryString);
            return queryObject.list();
    }

    public List findUnitNo(String unitNo) throws Exception {
        List list = new ArrayList();
            String queryString = "from Unit where id like'" + unitNo + "%'";
            Query queryObject = getCurrentSession().createQuery(queryString);

            list = queryObject.list();
        return list;
    }

    public List findAllDetails(RefTerminalTemp org, PortsTemp des, GenericCode com_code, CarriersOrLineTemp SSLine) throws Exception {
        List li = null;
            String queryString = " from VoyageExport where originTerminal=?0 and destinationPort=?1 and comNum=?2 and lineNo=?3";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter("0", org);
            queryObject.setParameter("1", des);
            queryObject.setParameter("2", com_code);
            queryObject.setParameter("3", SSLine);
            li = queryObject.list();
        return li;
    }

    public List getRecordsForCommRetail(RefTerminalTemp originTerminal, PortsTemp destAirPort, GenericCode comCode) throws Exception {
            Criteria criteria = getCurrentSession().createCriteria(VoyageExport.class);
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

    public void save(UnitOsdInfo persistanceInstance) throws Exception {
        getCurrentSession().save(persistanceInstance);
        getCurrentSession().flush();
    }


    public void update(VoyageExport persistanceInstance, String userName) throws Exception {
            getCurrentSession().update(persistanceInstance);
            getCurrentSession().flush();
    }

    public VoyageExport findById(Integer id) throws Exception {
            VoyageExport instance = (VoyageExport) getCurrentSession().get("com.gp.cong.logisoft.domain.VoyageExport", id);
            return instance;
    }

    public List findForSearchVoyageRatesmatch(String originTerminal, String destAirPort, String carries, String match) throws Exception {
            Criteria criteria = getCurrentSession().createCriteria(VoyageExport.class);
            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            if (originTerminal != null && !originTerminal.equals("")) {
                //criteria.add(Restrictions.like("originTerminal",originTerminal+"%"));
                //criteria.addOrder( Order.asc("originTerminal") );
                criteria.createCriteria("originTerminal").add(Restrictions.like("trmnum", originTerminal + "%"));
            }
            if (destAirPort != null && !destAirPort.equals("")) {
                //criteria.add(Restrictions.like("destinationPort",destAirPort+"%"));
                //criteria.addOrder( Order.asc("destinationPort") );
                criteria.createCriteria("destinationPort").add(Restrictions.like("shedulenumber", destAirPort + "%"));
            }
            /*if( comCode != null && !comCode.equals(""))
            {
            //criteria.add(Restrictions.like("commodityCode",comCode+"%"));
            //criteria.addOrder( Order.asc("commodityCode") );
            criteria.createCriteria("commodityCode").add( Restrictions.
            like("code", comCode+"%"));
            }*/

            if (carries != null && !carries.equals("")) {
                criteria.createCriteria("lineNo").add(Restrictions.like("carriercode", carries + "%"));

            }

            return criteria.list();
    }

    public List findForSearchVoyageRatesstarts(String originTerminal, String destAirPort, String carries, String match) throws Exception {
            Criteria criteria = getCurrentSession().createCriteria(VoyageExport.class);
            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

            if (originTerminal != null && !originTerminal.equals("")) {
                //criteria.add(Restrictions.ge("originTerminal",originTerminal+"%"));
                //criteria.addOrder( Order.asc("originTerminal") );
                criteria.createCriteria("originTerminal").add(Restrictions.ge("trmnum", originTerminal + "%"));
            }

            if (destAirPort != null && !destAirPort.equals("")) {
                //criteria.add(Restrictions.ge("destinationPort",destAirPort+"%"));
                //criteria.addOrder( Order.asc("destinationPort") );
                criteria.createCriteria("destinationPort").add(Restrictions.ge("shedulenumber", destAirPort + "%"));
            }

            /*if( comCode != null && !comCode.equals(""))
            {
            //criteria.add(Restrictions.like("commodityCode",comCode+"%"));
            //criteria.addOrder( Order.asc("commodityCode") );
            criteria.createCriteria("commodityCode").add( Restrictions.
            ge("code", comCode+"%"));
            }*/
            if (carries != null && !carries.equals("")) {
                criteria.createCriteria("lineNo").add(Restrictions.ge("carriercode", carries + "%"));

            }

            return criteria.list();

    }

    public List getRecordsForComm(RefTerminalTemp originTerminal, PortsTemp destAirPort) throws Exception {
            Criteria criteria = getCurrentSession().createCriteria(VoyageExport.class);
            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            if (originTerminal != null && !originTerminal.equals("")) {
                criteria.add(Restrictions.like("originTerminal", originTerminal));
            }
            if (destAirPort != null && !destAirPort.equals("")) {
                criteria.add(Restrictions.like("destinationPort", destAirPort));
            }
            /*if( lineNo != null && !lineNo.equals(""))
            {
            criteria.add(Restrictions.like("lineNo",lineNo));
            }*/
            return criteria.list();
    }

    public void delete(VoyageExport persistanceInstance, String userName) throws Exception {
            getSession().delete(persistanceInstance);
            getSession().flush();
    }

    public List findAllDetails(RefTerminalTemp org, PortsTemp des, GenericCode com_code) throws Exception {
        List li = null;
            String queryString = " from FTFMaster where originTerminal=?0 and destinationPort=?1 and commodityCode=?2";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter("0", org);
            queryObject.setParameter("1", des);
            queryObject.setParameter("2", com_code);

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
