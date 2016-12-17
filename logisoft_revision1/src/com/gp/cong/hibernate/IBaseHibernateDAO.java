package com.gp.cong.hibernate;

import org.hibernate.Session;

/**
 *
 * @author shanmugam
 */
public class IBaseHibernateDAO {

    public Session getSession() throws Exception{
        return this.getCurrentSession();
    }

    public Session getCurrentSession() throws Exception{
        return HibernateSessionFactory.getSessionFactory().getCurrentSession();
    }
}
