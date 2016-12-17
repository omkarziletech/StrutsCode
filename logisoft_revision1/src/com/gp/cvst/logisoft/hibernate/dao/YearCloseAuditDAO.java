package com.gp.cvst.logisoft.hibernate.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.domain.YearCloseAudit;

/**
 * Data access object (DAO) for domain model class AccountBalance.
 * @see com.gp.cvst.logisoft.hibernate.dao.AccountBalance
 * @author MyEclipse - Hibernate Tools
 */
public class YearCloseAuditDAO extends BaseHibernateDAO {

    private static final Log log = LogFactory.getLog(YearCloseAuditDAO.class);

    public void save(YearCloseAudit transientInstance) {
        log.debug("saving AccountBalance instance");
        try {
            getSession().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        } finally {
            closeSession();
        }
    }

    public void update(YearCloseAudit persistentInstance) {
        log.debug("saving AccountBalance instance");
        try {
            getSession().update(persistentInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        } finally {
            closeSession();
        }
    }

    public void delete(YearCloseAudit persistentInstance) {
        log.debug("deleting AccountBalance instance");
        try {
            getSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        } finally {
            closeSession();
        }
    }
}
