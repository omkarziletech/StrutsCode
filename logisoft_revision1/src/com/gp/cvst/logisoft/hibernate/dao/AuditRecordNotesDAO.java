package com.gp.cvst.logisoft.hibernate.dao;

import java.util.List;

import org.hibernate.Query;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import java.util.Date;

import org.apache.log4j.Logger;

public class AuditRecordNotesDAO extends BaseHibernateDAO {

    private static final Logger log = Logger.getLogger(AuditRecordNotesDAO.class);

    public List findById(String entityId) throws Exception {
	try {
	    String queryString = " from AuditLogRecordNotes where entityId=?0";
	    Query queryObject = getCurrentSession().createQuery(queryString);
	    queryObject.setParameter("0", entityId);
	    return queryObject.list();

	} catch (RuntimeException re) {
	    log.info("findById() failed in AuditRecordNotesDAO on " + new Date(),re);
	    throw re;
	} finally {
	    closeSession();
	}
    }
}
