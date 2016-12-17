package com.gp.cong.hibernate;

import java.util.Date;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.apache.log4j.Logger;

import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 * Configures and provides access to Hibernate sessions, tied to the
 * current thread of execution. Follows the Thread Local Session
 * pattern, see {@link http://hibernate.org/42.html }.
 */
public class HibernateSessionFactory {

    private static final Logger log = Logger.getLogger(HibernateSessionFactory.class);
    private static final ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();
    private static Configuration configuration;
    private static org.hibernate.SessionFactory sessionFactory;
    private static ServiceRegistry serviceRegistry;
    private static String configFile = "/hibernate.cfg.xml";

    private HibernateSessionFactory() {
    }

    public static Session getSession() throws HibernateException {
	Session session = (Session) threadLocal.get();
	if (session == null || !session.isOpen()) {
	    if (sessionFactory == null) {
		rebuildSessionFactory();
	    }
	    session = (sessionFactory != null) ? sessionFactory.openSession() : null;
	    threadLocal.set(session);
	}

	return session;
    }

    /**
     * Rebuild hibernate session factory
     */
    public static void rebuildSessionFactory() {
	try {
	    configuration = new Configuration().configure(configFile);
	    serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
	    sessionFactory = configuration.buildSessionFactory(serviceRegistry);
	} catch (Exception e) {
	    log.info("rebuildSessionFactory() in HibernateSessionFactory failed on " + new Date(),e);
	}
    }

    /**
     * Close the single hibernate session instance.
     * @throws HibernateException
     */
    public static void closeSession() throws HibernateException {
	Session session = (Session) threadLocal.get();
	threadLocal.set(null);

	if (session != null) {
	    session.close();
	}
    }

    /**
     * return session factory
     */
    public static org.hibernate.SessionFactory getSessionFactory() {
	if (null == sessionFactory) {
	    rebuildSessionFactory();
	}
	return sessionFactory;
    }

    /**
     * return session factory
     * session factory will be rebuilded in the next call
     */
    public static void setConfigFile(String configFile) {
	HibernateSessionFactory.configFile = configFile;
	sessionFactory = null;
    }

    /**
     * return hibernate configuration
     */
    public static Configuration getConfiguration() {
	return configuration;
    }
}
