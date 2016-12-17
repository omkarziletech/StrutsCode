/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.lcl.LclRelay;
import java.util.List;

import org.hibernate.Query;

public class LclRelayDAO extends BaseHibernateDAO<LclRelay> {

    public LclRelayDAO() throws Exception {
        super(LclRelay.class);
    }

    public Object[] getRelayTTCOW(Integer pol, Integer pod) throws Exception {
        Object[] instance = null;
        String queryString = "select transit_time,co_dow from lcl_relay where pol_id = ?0 and pod_id = ?1";
        Query queryObject = getSession().createSQLQuery(queryString);
        queryObject.setParameter("0", pol);
        queryObject.setParameter("1", pod);
        List l = queryObject.list();
        if (!l.isEmpty()) {
            instance = (Object[]) l.get(0);
        }
        return instance;
    }

    public Object[] getRelayTTDBD(Integer pol, Integer pod) throws Exception {
        Object[] instance = null;
        String queryString = "select transit_time,co_dbd,co_tod from lcl_relay where pol_id = ?0 and pod_id = ?1";
        Query queryObject = getSession().createSQLQuery(queryString);
        queryObject.setParameter("0", pol);
        queryObject.setParameter("1", pod);
        List l = queryObject.list();
        if (!l.isEmpty()) {
            instance = (Object[]) l.get(0);
        }
        return instance;
    }
}
