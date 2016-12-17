/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.lcl.LclSsRemarks;
import java.util.Date;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;
import java.util.List;
import org.hibernate.Query;

public class LclSsRemarksDAO extends BaseHibernateDAO<LclSsRemarks> {

    public LclSsRemarksDAO() {
        super(LclSsRemarks.class);
    }

    public List<LclSsRemarks> getHeaderRemarksByHeaderIdAndType(Long headerId, String type, String followupDate) throws Exception {
        Criteria criteria = getSession().createCriteria(LclSsRemarks.class, "lclSSRemarks");
        Date d = new Date();
        if (CommonUtils.isNotEmpty(headerId)) {
            criteria.add(Restrictions.eq("lclSsHeader.id", headerId));
        }
        if (CommonUtils.isNotEmpty(type)) {
            criteria.add(Restrictions.eq("type", type));
        }
        if (CommonUtils.isNotEmpty(followupDate)) {
            if (followupDate.equalsIgnoreCase("followexists")) {
                criteria.add(Restrictions.ge("followupDatetime", d));
            } else {
                criteria.add(Restrictions.le("followupDatetime", d));
            }
        }
        criteria.addOrder(Order.desc("id"));
        return criteria.list();
    }

    public void insertLclSSRemarks(Long headerId, String type, String status, String remarks, String followupDateTime, Integer userId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("INSERT INTO lcl_ss_remarks (ss_header_id,type,status,remarks,");
        if (CommonUtils.isNotEmpty(followupDateTime)) {
            queryBuilder.append("followup_datetime,followup_user_id,");
        }
        queryBuilder.append("entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id) VALUES (");
        queryBuilder.append(headerId).append(",'").append(type).append("','").append(status).append("','").append(remarks).append("'");
        if (CommonUtils.isNotEmpty(followupDateTime)) {
            queryBuilder.append(",STR_TO_DATE('").append(followupDateTime).append("','%d-%M-%Y'),").append(userId);
        }
        queryBuilder.append(",SYSDATE(),").append(userId).append(",SYSDATE(),");
        queryBuilder.append(userId).append(")");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public void insertSsHeaderRemarks(Long headerId, String type, String remarks,
            Integer userId, String followUpDate) throws Exception {
        StringBuilder sb = new StringBuilder();
        Date date = new Date();
        sb.append("INSERT INTO lcl_ss_remarks (");
        sb.append("  ss_header_id,");
        sb.append("  TYPE,");
        sb.append("  remarks,");
        sb.append("  entered_datetime,");
        sb.append("  entered_by_user_id,");
        sb.append("  modified_datetime,");
        sb.append("  modified_by_user_id");
        if (CommonUtils.isNotEmpty(followUpDate)) {
            sb.append(",followup_datetime,followup_user_id");
        }
        sb.append(") ");
        sb.append("VALUES");
        sb.append("  (");
        sb.append(":ss_header_id,:TYPE,");
        sb.append(":remarks,:entered_datetime,");
        sb.append(":entered_by_user_id,:modified_datetime,");
        sb.append(":modified_by_user_id");
        if (CommonUtils.isNotEmpty(followUpDate)) {
            sb.append(",:followUpDate,:followUpUser");
        }
        sb.append(")");
        Query queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setLong("ss_header_id", headerId);
        queryObject.setString("TYPE", type);
        queryObject.setString("remarks", remarks);
        queryObject.setParameter("entered_datetime", date);
        queryObject.setInteger("entered_by_user_id", userId);
        queryObject.setParameter("modified_datetime", date);
        queryObject.setInteger("modified_by_user_id", userId);
        if (CommonUtils.isNotEmpty(followUpDate)) {
            queryObject.setParameter("followUpDate", DateUtils.parseToDateForMonthMMM(followUpDate));
            queryObject.setInteger("followUpUser", userId);
        }
        queryObject.executeUpdate();
    }
}
