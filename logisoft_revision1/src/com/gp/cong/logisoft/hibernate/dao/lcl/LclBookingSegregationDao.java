/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.lcl.LclBookingSegregation;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Expression;
import org.hibernate.type.BooleanType;

/**
 *
 * @author Mohanapriya
 */
public class LclBookingSegregationDao extends BaseHibernateDAO<LclBookingSegregation> {

    public static final Logger log = Logger.getLogger(LclBookingSegregationDao.class);

    public LclBookingSegregationDao() {
        super(LclBookingSegregation.class);
    }

    public String getChildFileNumbers(String fileNumberId) throws Exception {
        String queryString = "select group_concat(child_file_number_id) from lcl_booking_segregation where parent_file_number_id = :fileNumberId";
        Query query = getCurrentSession().createSQLQuery(queryString).setString("fileNumberId", fileNumberId);
        return query.uniqueResult() != null ? (String) query.uniqueResult() : "";
    }

    public void deleteSegregatedDrs(String fileNumberId) throws Exception {
        String queryString = "delete from lcl_booking_segregation where parent_file_number_id = :fileNumberId";
        Query query = getCurrentSession().createSQLQuery(queryString).setString("fileNumberId", fileNumberId);
        query.executeUpdate();
    }

    public Boolean isCheckedSegregationDr(Long fileId) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append("SELECT IF(COUNT(*)>0,true,false) as result FROM lcl_booking_segregation");
        queryStr.append(" WHERE child_file_number_id=:fileId");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setParameter("fileId", fileId);
        query.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) query.setMaxResults(1).uniqueResult();
    }
}
