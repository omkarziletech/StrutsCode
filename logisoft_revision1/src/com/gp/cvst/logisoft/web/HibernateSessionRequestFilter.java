package com.gp.cvst.logisoft.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.hibernate.StaleObjectStateException;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;

import org.apache.log4j.Logger;

public class HibernateSessionRequestFilter extends BaseHibernateDAO implements Filter {

    private static final Logger log = Logger.getLogger(HibernateSessionRequestFilter.class);

    public void doFilter(ServletRequest request,
	    ServletResponse response,
	    FilterChain chain)
	    throws IOException, ServletException {

	try {
	    getCurrentSession().beginTransaction();
	    // Call the next filter (continue request processing)
	    chain.doFilter(request, response);

	    // Commit and cleanup
	    getCurrentSession().getTransaction().commit();
	    //      getCurrentSession().close();
	} catch (StaleObjectStateException staleEx) {
	    log.info("INSDIE CATCH ERROR STATE OBJECT STATE EXCEPTION  :" ,staleEx);

	    // Rollback, close everything, possibly compensate for any permanent changes
	    // during the conversation, and finally restart business conversation. Maybe
	    // give the user of the application a chance to merge some of his work with
	    // fresh data... what you do here depends on your applications design.
	    throw staleEx;
	} catch (Throwable ex) {
	    // Rollback only
	    log.info("Trying to rollback database transaction after exception " ,ex);
	    try {
		if (getCurrentSession().getTransaction().isActive()) {

		    log.info("Trying to rollback database transaction after exception");
		    getCurrentSession().getTransaction().rollback();
		}
	    } catch (Throwable rbEx) {
		System.err.println("Could not rollback transaction after exception! "+ rbEx);
	    }
	    throw new ServletException(ex);
	}
    }

    public void init(FilterConfig filterConfig) throws ServletException {
	try {
	    this.initializeSessionFactory();
	} catch (Exception ex) {
	    log.info("Trying to rollback database transaction after exception !", ex);
	}
    }

    public void destroy() {
    }
}