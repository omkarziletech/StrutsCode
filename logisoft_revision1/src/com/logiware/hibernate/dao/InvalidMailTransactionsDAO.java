package com.logiware.hibernate.dao;

import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.logiware.hibernate.domain.InvalidMailTransactions;

/**
 * @author Administrator
 *
 */
public class InvalidMailTransactionsDAO extends BaseHibernateDAO {

    public void save(InvalidMailTransactions transientInstance) throws Exception {
        getCurrentSession().save(transientInstance);
        getCurrentSession().flush();
    }

    public void saveEmail(InvalidMailTransactions transientInstance) throws Exception {
        getCurrentSession().save(transientInstance);
        getCurrentSession().flush();
    }

    public void saveOrUpdate(InvalidMailTransactions transientInstance) throws Exception{
            getCurrentSession().saveOrUpdate(transientInstance);
            getCurrentSession().flush();
    }

    public void update(InvalidMailTransactions persistanceInstance)  throws Exception{
            getCurrentSession().update(persistanceInstance);
            getCurrentSession().flush();
    }

    public InvalidMailTransactions findById(java.lang.Integer id) throws Exception{
            InvalidMailTransactions instance = (InvalidMailTransactions) getSession().get("com.gp.cong.logisoft.domain.InvalidMailTransactions", id);
            return instance;
    }
}
