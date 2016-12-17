package com.gp.cong.logisoft.hibernate.dao;

import com.gp.cong.logisoft.domain.BankReconcilliationSummary;

public class BankReconcilliationSummaryDAO extends BaseHibernateDAO {
    
    public void save(BankReconcilliationSummary transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public void update(BankReconcilliationSummary persistanceInstance)throws Exception {
            getSession().update(persistanceInstance);
            getSession().flush();
    }

    public void delete(BankReconcilliationSummary persistentInstance)throws Exception {
            getSession().delete(persistentInstance);
            getSession().flush();
    }

    public BankReconcilliationSummary findById(java.lang.Integer id)throws Exception {
            BankReconcilliationSummary instance = (BankReconcilliationSummary) getSession().get("com.gp.cong.logisoft.domain.BankReconcilliationSummary", id);
            return instance;
    }
}
