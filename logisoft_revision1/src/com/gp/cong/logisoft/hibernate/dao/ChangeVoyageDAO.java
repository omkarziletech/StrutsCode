package com.gp.cong.logisoft.hibernate.dao;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

import com.gp.cong.logisoft.domain.ChangeVoyage;
import org.hibernate.LockOptions;

/**
 * Data access object (DAO) for domain model class ChangeVoyage.
 * @see com.cong.hibernate.dao.ChangeVoyage
 * @author MyEclipse - Hibernate Tools
 */
public class ChangeVoyageDAO extends BaseHibernateDAO {

    //property constants
    public static final String VOYAGE_ID = "voyageId";
    public static final String OLD_VESSEL_NO = "oldVesselNo";
    public static final String NEW_VESSEL_NO = "newVesselNo";
    public static final String OLD_PIER = "oldPier";
    public static final String NEW_PIER = "newPier";
    public static final String OLD_FLIGHT_SS_VOYAGE = "oldFlightSsVoyage";
    public static final String NEW_FLIGHT_SS_VOYAGE = "newFlightSsVoyage";
    public static final String OLD_LINE_NO = "oldLineNo";
    public static final String NEW_LINE_NO = "newLineNo";
    public static final String CODETYPEID = "codetypeid";
    public static final String CODE_DESCRIPTION = "codeDescription";
    public static final String NOTIFY_CUSTOMER = "notifyCustomer";
    public static final String NOTIFY_ECI = "notifyEci";

    public void save(ChangeVoyage transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public void delete(ChangeVoyage persistentInstance)throws Exception {
            getSession().delete(persistentInstance);
            getSession().flush();
    }

    public ChangeVoyage findById(java.lang.Integer id)throws Exception {
            ChangeVoyage instance = (ChangeVoyage) getSession().get("com.cong.domain.ChangeVoyage", id);
            return instance;
    }

    public List findByExample(ChangeVoyage instance)throws Exception {
            List results = getSession().createCriteria("com.gp.cong.logisoft.domain.ChangeVoyage").add(Example.create(instance)).list();
            return results;
    }

    public List findByProperty(String propertyName, Object value)throws Exception {
            String queryString = "from ChangeVoyage as model where model." + propertyName + "= ?0";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setParameter("0", value);
            return queryObject.list();
    }

    public List findByVoyageId(Object voyageId)throws Exception {
        return findByProperty(VOYAGE_ID, voyageId);
    }

    public List findByOldVesselNo(Object oldVesselNo)throws Exception {
        return findByProperty(OLD_VESSEL_NO, oldVesselNo);
    }

    public List findByNewVesselNo(Object newVesselNo)throws Exception {
        return findByProperty(NEW_VESSEL_NO, newVesselNo);
    }

    public List findByOldPier(Object oldPier) throws Exception {
        return findByProperty(OLD_PIER, oldPier);
    }

    public List findByNewPier(Object newPier)throws Exception {
        return findByProperty(NEW_PIER, newPier);
    }

    public List findByOldFlightSsVoyage(Object oldFlightSsVoyage)throws Exception {
        return findByProperty(OLD_FLIGHT_SS_VOYAGE, oldFlightSsVoyage);
    }

    public List findByNewFlightSsVoyage(Object newFlightSsVoyage)throws Exception {
        return findByProperty(NEW_FLIGHT_SS_VOYAGE, newFlightSsVoyage);
    }

    public List findByOldLineNo(Object oldLineNo)throws Exception {
        return findByProperty(OLD_LINE_NO, oldLineNo);
    }

    public List findByNewLineNo(Object newLineNo)throws Exception {
        return findByProperty(NEW_LINE_NO, newLineNo);
    }

    public List findByCodetypeid(Object codetypeid)throws Exception {
        return findByProperty(CODETYPEID, codetypeid);
    }

    public List findByCodeDescription(Object codeDescription)throws Exception {
        return findByProperty(CODE_DESCRIPTION, codeDescription);
    }

    public List findByNotifyCustomer(Object notifyCustomer)throws Exception {
        return findByProperty(NOTIFY_CUSTOMER, notifyCustomer);
    }

    public List findByNotifyEci(Object notifyEci)throws Exception {
        return findByProperty(NOTIFY_ECI, notifyEci);
    }

    public ChangeVoyage merge(ChangeVoyage detachedInstance)throws Exception {
            ChangeVoyage result = (ChangeVoyage) getSession().merge(detachedInstance);
            return result;
    }

    public void attachDirty(ChangeVoyage instance)throws Exception {
            getSession().saveOrUpdate(instance);
            getSession().flush();
    }

    public void attachClean(ChangeVoyage instance)throws Exception {
            getSession().buildLockRequest(LockOptions.NONE).lock(instance);
    }
}
