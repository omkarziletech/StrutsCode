/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.hibernate.dao;

import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.domain.FclDoorDelivery;
import org.hibernate.Query;

/**
 *
 * @author User
 */
public class FclDoorDeliveryDAO extends BaseHibernateDAO {

    public void save(FclDoorDelivery transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public FclDoorDelivery getFclDoorDeliveryByBol(Integer bolId) throws Exception {
        String queryString = "from FclDoorDelivery where bolId=:bolId";
        Query queryObj = getSession().createQuery(queryString);
        queryObj.setParameter("bolId", bolId);
        return (FclDoorDelivery) queryObj.setMaxResults(1).uniqueResult();
    }

    public void deleteFclDoorDeliveryByBol(String bolId) throws Exception {
        int bol = Integer.parseInt(bolId);
        String queryString = "delete from FclDoorDelivery where bolId=:bolId";
        Query queryObj = getSession().createQuery(queryString);
        queryObj.setParameter("bolId", bol);
        queryObj.executeUpdate();
    }
}
