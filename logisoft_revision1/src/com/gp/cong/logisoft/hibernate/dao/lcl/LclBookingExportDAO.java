/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBookingExport;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import java.util.Date;
import org.hibernate.SQLQuery;
import org.hibernate.type.BooleanType;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import com.gp.cong.common.CommonUtils;

/**
 *
 * @author aravindhan.v
 */
public class LclBookingExportDAO extends BaseHibernateDAO<LclBookingExport> {

    public LclBookingExportDAO() {
        super(LclBookingExport.class);
    }

    public Boolean getReleasedDateTime(String fileId) throws Exception {
        String queryString = "SELECT IF(released_datetime IS NOT NULL,TRUE,FALSE) as result FROM lcl_booking_export WHERE file_number_id=:fileId";
        SQLQuery queryObject = getSession().createSQLQuery(queryString);
        queryObject.setString("fileId", fileId);
        queryObject.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) queryObject.setMaxResults(1).uniqueResult();
    }

    public void delete(String fileNumberId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("delete from lcl_booking_export where file_number_id=").append(fileNumberId);
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        getCurrentSession().flush();
    }

    public LclBookingExport save(LclBookingExport lclBookingExport, LclFileNumber lclFileNumber, User loginUser) throws Exception {
        lclBookingExport = new LclBookingExport();
        lclBookingExport.setEnteredBy(loginUser);
        lclBookingExport.setEnteredDatetime(new Date());
        lclBookingExport.setFileNumberId(lclFileNumber.getId());
        lclBookingExport.setDeliverPickup("P");
        lclBookingExport.setDeliverPickupDatetime(new Date());
        lclBookingExport.setModifiedBy(loginUser);
        lclBookingExport.setModifiedDatetime(new Date());
        this.saveOrUpdate(lclBookingExport);
        return lclBookingExport;
    }
    
     public LclBookingExport getLclBookingExportByFileNumber(Long fileId) throws Exception {
        Criteria criteria = getSession().createCriteria(LclBookingExport.class, "lclBookingExport");
        criteria.createAlias("lclBookingExport.lclFileNumber", "lclFileNumber");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        return (LclBookingExport) criteria.setMaxResults(1).uniqueResult();
    }
    
    public void updateFields(String fileId, String fieldName, String fieldValue, String userId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update lcl_booking_export set ").append(fieldName).append(" = '").append(fieldValue).append("',modified_by_user_id = ").append(userId).append(",modified_datetime = SYSDATE() where file_number_id = ").append(fileId);
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }
     
}
