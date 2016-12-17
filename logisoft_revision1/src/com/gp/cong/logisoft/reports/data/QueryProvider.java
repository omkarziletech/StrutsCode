package com.gp.cong.logisoft.reports.data; 

import java.util.List;

import org.hibernate.HibernateException;

/**
     * A QueryProvidor provides a generic way of fetching a set of objects.
     */
    public interface QueryProvider  {

        /**
         * Return a set of objects based on a given criteria set.
         * @param firstResult the first result to be returned
         * @param maxResults the maximum number of results to be returned
         * @return a list of objects
         */
        List getObjects(CriteriaSet criteria,
                        int firstResult,
                        int maxResults) throws HibernateException;
        
        
    }