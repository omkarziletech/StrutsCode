package com.gp.cong.logisoft.hibernate.dao;

import java.util.List;

import org.hibernate.Query;

public class ClaimDAO extends BaseHibernateDAO {

    public List findAllClaims()throws Exception {
            String queryString = " from Claim";
            Query queryObject = getCurrentSession().createQuery(queryString);
            return queryObject.list();
    }
}
