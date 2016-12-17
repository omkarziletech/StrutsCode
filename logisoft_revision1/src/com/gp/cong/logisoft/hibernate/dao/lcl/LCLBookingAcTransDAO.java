/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBookingAcTrans;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.SQLQuery;
import org.hibernate.type.BooleanType;

/**
 *
 * @author Owner
 */
public class LCLBookingAcTransDAO extends BaseHibernateDAO<LclBookingAcTrans> {

    public LCLBookingAcTransDAO() {
        super(LclBookingAcTrans.class);
    }

    public List<LclBookingAcTrans> findByFileNumber(Long fileId) throws Exception {
        Criteria criteria = getSession().createCriteria(LclBookingAcTrans.class);
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        criteria.add(Restrictions.ne("referenceNo", ""));
        criteria.addOrder(Order.asc("id"));
        return criteria.list();
    }

    public LclBookingAcTrans getLclBookingAcTrans(Long fileId) throws Exception {
        Criteria criteria = getSession().createCriteria(LclBookingAcTrans.class);
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        return (LclBookingAcTrans) criteria.uniqueResult();
    }

    public String getTransType(Long lclBookingAcId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT lbctrans.trans_type FROM lcl_booking_ac_ta lbacta ");
        sb.append("JOIN lcl_booking_ac_trans lbctrans ON lbacta.lcl_booking_ac_trans_id = lbctrans.id ");
        sb.append("WHERE lbacta.lcl_booking_ac_id = '").append(lclBookingAcId).append("'");
        sb.append(" ORDER BY lbacta.id DESC LIMIT 1");
        String transType = (String) getCurrentSession().createSQLQuery(sb.toString()).uniqueResult();
        return null != transType ? transType : "";
    }

    public String getTransTypeCount(String lclBookingAcId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT if(COUNT(t.id) > 0, 'true','false') FROM lcl_booking_ac ac ");
        sb.append("JOIN lcl_booking_ac_ta ta ON ta.`lcl_booking_ac_id`=ac.`id` ");
        sb.append("JOIN lcl_booking_ac_trans t ON t.id=ta.lcl_booking_ac_trans_id ");
        sb.append("WHERE ac.id IN (").append(lclBookingAcId).append(") AND t.trans_type <> 'AC' AND t.trans_type <> '' AND t.trans_type IS NOT NULL");
        return (String) getCurrentSession().createSQLQuery(sb.toString()).uniqueResult();
    }

    public String getConcatenatedBookingAcTransIds(String bookingAcIds) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT CONVERT(GROUP_CONCAT(t.id) USING utf8) FROM lcl_booking_ac ac");
        sb.append(" JOIN lcl_booking_ac_ta ta ON ta.`lcl_booking_ac_id`=ac.`id`");
        sb.append(" JOIN lcl_booking_ac_trans t ON t.id=ta.lcl_booking_ac_trans_id");
        sb.append(" WHERE ac.id in (").append(bookingAcIds).append(")");
        return (String) getSession().createSQLQuery(sb.toString()).uniqueResult();
    }

    public void deleteLclBookingAcTransByBkgAcId(String lclBookingTransIds) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("delete from lcl_booking_ac_trans ");
        queryBuilder.append("where id in (").append(lclBookingTransIds).append(")");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        getCurrentSession().flush();
    }

    public Boolean getPaymentType(Long fileNumberId) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" select if(count(*)>0,true,false) as result FROM ");
        queryStr.append(" lcl_booking_ac_trans where file_number_id=:fileId and payment_type='Check Copy' ");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(queryStr.toString());
        queryObject.setParameter("fileId", fileNumberId);
        queryObject.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) queryObject.uniqueResult();
    }

    public void updatePaymentType(String paymentType, String id, String fileNumberId,HttpServletRequest request) throws Exception {
        User loginUser = (User) request.getSession().getAttribute("loginuser");
        StringBuilder query = new StringBuilder();
        query.append("update lcl_booking_ac_trans set payment_type =:paymentType,");
        query.append(" modified_by_user_id =:loginUser,");
        query.append(" modified_datetime =SYSDATE()");
        query.append(" where file_number_id =:fileNumberId and id =:id");
        SQLQuery queryObject = getSession().createSQLQuery(query.toString());
        queryObject.setParameter("paymentType", paymentType);
        queryObject.setParameter("loginUser", loginUser);
        queryObject.setParameter("fileNumberId", fileNumberId);
        queryObject.setParameter("id", id);
        queryObject.executeUpdate();
    }

    public void updateTransType(Long fileId, Integer userId) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" UPDATE lcl_booking_ac_trans bat SET bat.payment_type='Check/Cash' ");
        queryStr.append(" WHERE bat.file_number_id =:fileNumberId AND bat.payment_type='Check Copy' ");
        SQLQuery queryObject = getSession().createSQLQuery(queryStr.toString());
        queryObject.setParameter("fileNumberId", fileId);
        int updateCount = queryObject.executeUpdate();
        if (updateCount > 0) {
            String remarks = "UPDATED ->Payment Type Changed to Check/Cash - Accounting has Processed Payment in Full";
            new LclRemarksDAO().insertLclRemarks(fileId, LclCommonConstant.REMARKS_DR_AUTO_NOTES, remarks, userId);
        }
    }
    public Boolean ValidateCostStatus(Long fileNumberId) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT IF(COUNT(*) > 0, TRUE, FALSE) as result ");
        queryStr.append(" FROM lcl_booking_ac_trans lbat  ");
        queryStr.append(" WHERE lbat.`trans_type` <> 'AC' AND lbat.`trans_type` IS NOT NULL ");
        queryStr.append(" AND lbat.`trans_type` <> '' ");
        queryStr.append(" AND file_number_id =:fileId");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(queryStr.toString());
        queryObject.setParameter("fileId", fileNumberId);
        queryObject.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) queryObject.uniqueResult();
    }
}
