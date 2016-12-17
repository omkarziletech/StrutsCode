package com.gp.cong.logisoft.hibernate.dao;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.PortsTemp;
import com.gp.cong.logisoft.domain.RefTerminalTemp;
import com.gp.cong.logisoft.domain.RetailStandardCharges;
import com.gp.cong.logisoft.domain.StandardCharges;

public class StandardChargesDAO extends BaseHibernateDAO {

    /**
     * @param transientInstance
     * @param userName
     */
    public void save(StandardCharges transientInstance, String userName) throws Exception {
        getCurrentSession().save(transientInstance);
    }

    public void save1(RetailStandardCharges transientInstance, String userName) throws Exception {
        getCurrentSession().save(transientInstance);
    }

    public RetailStandardCharges findById1(Integer id)  throws Exception{
            RetailStandardCharges instance = (RetailStandardCharges) getCurrentSession().get("com.gp.cong.logisoft.domain.RetailStandardCharges",
                    id);
            return instance;
    }

    // ---------------------------------------
    public List findAllDetails(GenericCode org, GenericCode des,
            GenericCode com_code)  throws Exception{
        List li = null;
            String queryString = " from StandardCharges where orgTerminal=?0 and destPort=?1 and comCode=?2";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter("0", org);
            queryObject.setParameter("1", des);
            queryObject.setParameter("2", com_code);
            li = queryObject.list();
        return li;
    }

    public List findAllDetails1(String orgine, String destination,
            String commodity_code)  throws Exception{
        List li = null;
            String queryString = " from RetailStandardCharges where orgTerminal=?0 and destPort=?1 and comCode=?2";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter("0", orgine);
            queryObject.setParameter("1", destination);
            queryObject.setParameter("2", commodity_code);
            li = queryObject.list();
        return li;
    }

    public void save(RetailStandardCharges transientInstance) throws Exception {
        getCurrentSession().save(transientInstance);
    }

    public void update(StandardCharges persistanceInstance, String userName)  throws Exception{
            getCurrentSession().update(persistanceInstance);
            getCurrentSession().flush();
            /*
             * AuditLogInterceptor audit=new AuditLogInterceptor();
             *
             * audit.setSessionFactory(HibernateSessionFactory.getSessionFactory());
             * audit.setUserName(userName); AuditLogRecord aud=new
             * AuditLogRecordAirRates(); audit.setAuditLogReord(aud);
             * audit.onFlushDirty(persistanceInstance,
             * persistanceInstance.getId(), null, null, null, null); Iterator
             * iter=null; audit.postFlush(iter); log.debug("update successful");
             */
    }

    public void update1(RetailStandardCharges persistanceInstance,
            String userName)  throws Exception{
            getCurrentSession().update(persistanceInstance);
            getCurrentSession().flush();
    }

    public List findAllAirRates()  throws Exception{
            String queryString = "from StandardCharges";
            Query queryObject = getCurrentSession().createQuery(queryString);
            return queryObject.list();
    }

    public List findAllRetailRates()  throws Exception{
            String queryString = "from RetailStandardCharges";
            Query queryObject = getCurrentSession().createQuery(queryString);
            return queryObject.list();
    }

    public List findForSearchAirRatesAction(String originTerminal,
            String destAirPort, String comCode, String match)  throws Exception{
            Criteria criteria = getCurrentSession().createCriteria(
                    StandardCharges.class);
            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            if (originTerminal != null && !originTerminal.equals("")) {
                // criteria.add(Restrictions.like("orgTerminal",originTerminal));
                criteria.createCriteria("orgTerminal").add(
                        Restrictions.like("code", originTerminal + "%"));
            }
            if (destAirPort != null && !destAirPort.equals("")) {

                criteria.createCriteria("destPort").add(
                        Restrictions.like("code", destAirPort + "%"));
            }
            if (comCode != null && !comCode.equals("")) {
                criteria.createCriteria("comCode").add(
                        Restrictions.like("code", comCode + "%"));
            }

            return criteria.list();
    }

    public List getRecordsForComm(RefTerminalTemp originTerminal,
            PortsTemp destAirPort, GenericCode comCode)  throws Exception{
            Criteria criteria = getCurrentSession().createCriteria(
                    StandardCharges.class);
            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            if (originTerminal != null && !originTerminal.equals("")) {
                criteria.add(Restrictions.like("orgTerminal", originTerminal));
            }
            if (destAirPort != null && !destAirPort.equals("")) {
                criteria.add(Restrictions.like("destPort", destAirPort));
            }
            if (comCode != null && !comCode.equals("")) {
                criteria.add(Restrictions.like("comCode", comCode));
            }
            return criteria.list();
    }

    public List getRecordsForCommRetail(String originTerminal,
            String destAirPort, String comCode)  throws Exception{
            Criteria criteria = getCurrentSession().createCriteria(
                    RetailStandardCharges.class);
            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            if (originTerminal != null && !originTerminal.equals("")) {
                criteria.add(Restrictions.like("orgTerminal", originTerminal));
            }
            if (destAirPort != null && !destAirPort.equals("")) {
                criteria.add(Restrictions.like("destPort", destAirPort));
            }
            if (comCode != null && !comCode.equals("")) {
                criteria.add(Restrictions.like("comCode", comCode));
            }
            return criteria.list();
    }

    public List findForSearchAirRatesn(String originTerminal,
            String destAirPort, String comCode, String match)  throws Exception{
            Criteria criteria = getCurrentSession().createCriteria(
                    StandardCharges.class);
            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            if (originTerminal != null && !originTerminal.equals("")) {
                // criteria.add(Restrictions.like("orgTerminal",originTerminal));
                criteria.createCriteria("orgTerminal").add(
                        Restrictions.eq("code", originTerminal));
            }
            if (destAirPort != null && !destAirPort.equals("")) {

                criteria.createCriteria("destPort").add(
                        Restrictions.eq("code", destAirPort));
            }
            if (comCode != null && !comCode.equals("")) {

                criteria.createCriteria("comCode").add(
                        Restrictions.ge("code", comCode));
            }


            return criteria.list();
    }

    //
    public List findForSearchRetailRatesAction(String originTerminal,
            String destAirPort, String comCode, String match)  throws Exception{
            Criteria criteria = getCurrentSession().createCriteria(
                    RetailStandardCharges.class);
            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            if (originTerminal != null && !originTerminal.equals("")) {
                criteria.add(Restrictions.like("orgTerminal", originTerminal + "%"));
            }
            if (destAirPort != null && !destAirPort.equals("")) {
                criteria.add(Restrictions.like("destPort", destAirPort + "%"));
            }
            if (comCode != null && !comCode.equals("")) {

                criteria.add(Restrictions.like("comCode", comCode + "%"));
            }

            return criteria.list();
    }

    public List findForSearchRetailRatesn(String originTerminal,
            String destAirPort, String comCode, String match)  throws Exception{
            Criteria criteria = getCurrentSession().createCriteria(RetailStandardCharges.class);
            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            if (originTerminal != null && !originTerminal.equals("")) {
                criteria.add(Restrictions.eq("orgTerminal", originTerminal));
            }
            if (destAirPort != null && !destAirPort.equals("")) {
                criteria.add(Restrictions.eq("destPort", destAirPort));
            }
            if (comCode != null && !comCode.equals("")) {
                criteria.add(Restrictions.ge("comCode", comCode));
            }
            return criteria.list();
    }

    public List findForSearchRetailRatesMatchOnly(String originTerminal,
            String destAirPort, String comCode, String match)  throws Exception{
            Criteria criteria = getCurrentSession().createCriteria(
                    RetailStandardCharges.class);
            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            if (originTerminal != null && !originTerminal.equals("")) {
                criteria.add(Restrictions.eq("orgTerminal", originTerminal));
            }
            if (destAirPort != null && !destAirPort.equals("")) {

                criteria.add(Restrictions.eq("destPort", destAirPort));
            }

            if (comCode != null && !comCode.equals("")) {

                criteria.add(Restrictions.eq("comCode", comCode));
            }

            return criteria.list();
    }

    public StandardCharges findById(Integer id)  throws Exception{
            StandardCharges instance = (StandardCharges) getCurrentSession().get("com.gp.cong.logisoft.domain.StandardCharges", id);

            return instance;
    }

    public void delete(StandardCharges persistanceInstance, String userName)  throws Exception{
            getSession().delete(persistanceInstance);
    }

    public void delete1(RetailStandardCharges persistanceInstance,
            String userName)  throws Exception{
            getSession().delete(persistanceInstance);
    }
}
