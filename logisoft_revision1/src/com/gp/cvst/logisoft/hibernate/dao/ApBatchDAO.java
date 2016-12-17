package com.gp.cvst.logisoft.hibernate.dao;

import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;

import com.gp.cvst.logisoft.domain.ApBatch;

/**
 * Data access object (DAO) for domain model class Batch.
 *
 * @see com.gp.cvst.logisoft.hibernate.dao.Batch
 * @author MyEclipse - Hibernate Tools
 */
public class ApBatchDAO extends BaseHibernateDAO {

    //property constants
    public void save(ApBatch transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public void delete(ApBatch persistentInstance) throws Exception {
        getSession().delete(persistentInstance);
        getSession().flush();
    }

    public ApBatch findById(Integer batchId) throws Exception {
        ApBatch instance = (ApBatch) getSession().get("com.gp.cvst.logisoft.domain.ApBatch", batchId);
        return instance;
    }

    public ApBatch findById(String batchId) throws Exception {
        return findById(Integer.parseInt(batchId));
    }
}
