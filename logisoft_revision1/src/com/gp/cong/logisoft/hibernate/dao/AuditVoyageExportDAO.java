package com.gp.cong.logisoft.hibernate.dao;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import com.gp.cong.logisoft.domain.AuditVoyageExport;
import org.hibernate.LockOptions;

/**
 * Data access object (DAO) for domain model class AuditVoyageExport.
 * @see com.cong.hibernate.dao.AuditVoyageExport
 * @author MyEclipse - Hibernate Tools
 */
public class AuditVoyageExportDAO extends BaseHibernateDAO {

    private static final Log log = LogFactory.getLog(AuditVoyageExportDAO.class);
    //property constants
    public static final String ENTITY_ID = "entityId";
    public static final String ENTITY_NAME = "entityName";
    public static final String ENTITY_ATTRIBUTE = "entityAttribute";
    public static final String MESSAGE = "message";
    public static final String UPDATED_BY = "updatedBy";
    public static final String NEW_VALUE = "newValue";
    public static final String OLD_VALUE = "oldValue";
    public static final String ENTIRY_ID = "entiryId";
    public static final String NOTE_TYPE = "noteType";
    public static final String VOIDED = "voided";

    public void save(AuditVoyageExport transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public void delete(AuditVoyageExport persistentInstance)throws Exception {
            getSession().delete(persistentInstance);
            getSession().flush();
    }

    public AuditVoyageExport findById(java.lang.Integer id)throws Exception {
            AuditVoyageExport instance = (AuditVoyageExport) getSession().get("com.gp.cong.logisoft.domain.AuditVoyageExport", id);
            return instance;
    }

    public List findByExample(AuditVoyageExport instance)throws Exception {
            List results = getSession().createCriteria("com.cong.hibernate.dao.AuditVoyageExport").add(Example.create(instance)).list();
            return results;
    }

    public List findByProperty(String propertyName, Object value) throws Exception {
            String queryString = "from AuditVoyageExport as model where model." + propertyName + "= ?0";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setParameter("0", value);
            return queryObject.list();
    }

    public List findByEntityId(Object entityId)throws Exception {
        return findByProperty(ENTITY_ID, entityId);
    }

    public List findByEntityName(Object entityName)throws Exception {
        return findByProperty(ENTITY_NAME, entityName);
    }

    public List findByEntityAttribute(Object entityAttribute)throws Exception {
        return findByProperty(ENTITY_ATTRIBUTE, entityAttribute);
    }

    public List findByMessage(Object message)throws Exception {
        return findByProperty(MESSAGE, message);
    }

    public List findByUpdatedBy(Object updatedBy)throws Exception {
        return findByProperty(UPDATED_BY, updatedBy);
    }

    public List findByNewValue(Object newValue)throws Exception {
        return findByProperty(NEW_VALUE, newValue);
    }

    public List findByOldValue(Object oldValue)throws Exception {
        return findByProperty(OLD_VALUE, oldValue);
    }

    public List findByEntiryId(Object entiryId)throws Exception {
        return findByProperty(ENTIRY_ID, entiryId);
    }

    public List findByNoteType(Object noteType)throws Exception {
        return findByProperty(NOTE_TYPE, noteType);
    }

    public List findByVoided(Object voided)throws Exception {
        return findByProperty(VOIDED, voided);
    }

    public AuditVoyageExport merge(AuditVoyageExport detachedInstance)throws Exception {
            AuditVoyageExport result = (AuditVoyageExport) getSession().merge(detachedInstance);
            return result;
    }

    public void attachDirty(AuditVoyageExport instance)throws Exception {
            getSession().saveOrUpdate(instance);
            getSession().flush();
    }

    public void attachClean(AuditVoyageExport instance)throws Exception {
            getSession().buildLockRequest(LockOptions.NONE).lock(instance);
    }
}
