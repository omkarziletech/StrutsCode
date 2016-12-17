package com.gp.cong.logisoft.hibernate.dao;

import java.util.List;
import org.hibernate.Query;
import com.gp.cong.logisoft.domain.BankReconcilliationDetail;

public class BankReconcilliationDetailDAO extends BaseHibernateDAO {

    public void save(BankReconcilliationDetail transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public void update(BankReconcilliationDetail persistanceInstance)throws Exception {
            getSession().update(persistanceInstance);
            getSession().flush();
    }

    public void delete(BankReconcilliationDetail persistentInstance)throws Exception {
            getSession().delete(persistentInstance);
            getSession().flush();
    }

    public BankReconcilliationDetail findById(java.lang.Integer id)throws Exception {
            BankReconcilliationDetail instance = (BankReconcilliationDetail) getSession().get("com.gp.cong.logisoft.domain.BankReconcilliationDetail", id);
            return instance;
    }

    public List findByProperty(String propertyName, Object value)throws Exception {
            String queryString = "from BankReconcilliationDetail as model where model." + propertyName + " like ?0";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setParameter("0", value);
            return queryObject.list();
    }
}
