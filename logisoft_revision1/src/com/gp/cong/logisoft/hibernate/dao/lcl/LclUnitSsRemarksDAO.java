/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
import com.gp.cong.logisoft.domain.lcl.LclUnit;
import com.gp.cong.logisoft.domain.lcl.LclUnitSsRemarks;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.type.StringType;

/**
 *
 * @author Administrator
 */
public class LclUnitSsRemarksDAO extends BaseHibernateDAO<LclUnitSsRemarks> {

    public LclUnitSsRemarksDAO() {
        super(LclUnitSsRemarks.class);
    }

    public List<LclUnitSsRemarks> getRemarksList(Long headerId, Long unitId,
            String remarksType, String remarksAction) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" from LclUnitSsRemarks ");
        queryStr.append(" where lclSsHeader.id = :headerId  ");
        queryStr.append(" and lclUnit.id = :unitId ");
        if ("voidedNotes".equalsIgnoreCase(remarksType)) {
            queryStr.append(" and status = 'Closed' ORDER BY id DESC");
        } else if (null != remarksType && !"".equalsIgnoreCase(remarksType)) {
            queryStr.append(" and type = :type ");
        }
        if (!"voidedNotes".equalsIgnoreCase(remarksType)) {
            queryStr.append(" and status IS NULL ORDER BY id DESC");
        }
        Query query = getCurrentSession().createQuery(queryStr.toString());
        query.setLong("headerId", headerId);
        query.setLong("unitId", unitId);
        if (null != remarksType && !"".equalsIgnoreCase(remarksType) && !"voidedNotes".equalsIgnoreCase(remarksType)) {
            query.setString("type", remarksType);
        }
        List<LclUnitSsRemarks> remarksList = query.list();
        return remarksList;
    }

    public LclUnitSsRemarks getRemarks(Long headerId, Long unitId, String type) throws Exception {
        String remarksQuery = "from LclUnitSsRemarks where lclSsHeader.id = :headerId and lclUnit.id = :unitId and type = :type";
        Query query = getCurrentSession().createQuery(remarksQuery);
        query.setLong("headerId", headerId);
        query.setLong("unitId", unitId);
        query.setString("type", type);
        return (LclUnitSsRemarks) query.uniqueResult();
    }

    public void insertUnitSsRemarks(LclSsHeader lclSsHeader, LclUnit lclUnit, String type, Date followUpDate, User followUpUser, String remarks, User user) throws Exception {
        LclUnitSsRemarksDAO lclUnitSsRemarksDAO = new LclUnitSsRemarksDAO();
        Date d = new Date();
        LclUnitSsRemarks lclUnitSsRemarks = new LclUnitSsRemarks();
        lclUnitSsRemarks.setLclUnit(lclUnit);
        lclUnitSsRemarks.setLclSsHeader(lclSsHeader);
        lclUnitSsRemarks.setType(type);
        lclUnitSsRemarks.setRemarks(remarks);
        lclUnitSsRemarks.setFollowupDateTime(followUpDate);
        lclUnitSsRemarks.setFollowUpUserId(followUpUser);
        lclUnitSsRemarks.setEnteredBy(user);
        lclUnitSsRemarks.setEnteredDatetime(d);
        lclUnitSsRemarks.setModifiedDatetime(d);
        lclUnitSsRemarks.setModifiedby(user);
        lclUnitSsRemarksDAO.save(lclUnitSsRemarks);
    }

    public boolean isRemarks(Long headerId, Long unitId, String type) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT IF(COUNT(*)>0,'true','false') as result FROM lcl_unit_ss_remarks WHERE ss_header_id=").append(headerId);
        sb.append(" AND unit_id=").append(unitId).append(" AND type='").append(type).append("' ORDER BY id LIMIT 1 ");
        String remarksCount = (String) getCurrentSession().createSQLQuery(sb.toString()).addScalar("result", StringType.INSTANCE).uniqueResult();
        return Boolean.valueOf(remarksCount);
    }

    public List getUnitandHeaderId(String voyageNo) throws Exception {
        StringBuilder sb = new StringBuilder();
        List list = null;
        sb.append("SELECT ");
        sb.append(" lus.`ss_header_id`,");
        sb.append("  lus.`unit_id` ");
        sb.append("FROM");
        sb.append("  lcl_ss_header lsh ");
        sb.append("  JOIN `lcl_unit_ss` lus ");
        sb.append("    ON lsh.`id` = lus.`ss_header_id` ");
        sb.append("WHERE lsh.`schedule_no` = '").append(voyageNo).append("'");
        Query queryObject = getCurrentSession().createSQLQuery(sb.toString());
        list = queryObject.list();
        return list;
    }

    public void insertLclunitRemarks(Long headerId, Long unitId, String type, String remarks, Integer userId) throws Exception {
        StringBuilder sb = new StringBuilder();
        Date date = new Date();
        sb.append("INSERT INTO lcl_unit_ss_remarks (");
        sb.append("  unit_id,");
        sb.append("  ss_header_id,");
        sb.append("  TYPE,");
        sb.append("  remarks,");
        sb.append("  entered_datetime,");
        sb.append("  entered_by_user_id,");
        sb.append("  modified_datetime,");
        sb.append("  modified_by_user_id");
        sb.append(") ");
        sb.append("VALUES");
        sb.append("  (");
        sb.append(":unit_id,:ss_header_id,:TYPE,");
        sb.append(":remarks,:entered_datetime,");
        sb.append(":entered_by_user_id,:modified_datetime,");
        sb.append(":modified_by_user_id)");
        Query queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setLong("unit_id", unitId);
        queryObject.setLong("ss_header_id", headerId);
        queryObject.setString("TYPE", type);
        queryObject.setString("remarks", remarks);
        queryObject.setParameter("entered_datetime", date);
        queryObject.setLong("entered_by_user_id", userId);
        queryObject.setParameter("modified_datetime", date);
        queryObject.setLong("modified_by_user_id", userId);
        queryObject.executeUpdate();
    }

    public void updateSsHeaderId(Long oldSsHeaderId, Long newSsHeaderId, Long unitId) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append("UPDATE lcl_unit_ss_remarks set ss_header_id =:newSsHeaderId ");
        queryStr.append(" WHERE ss_header_id = :oldSsHeaderId and unit_id=:unitId");
        SQLQuery queryObject = getSession().createSQLQuery(queryStr.toString());
        queryObject.setParameter("newSsHeaderId", newSsHeaderId);
        queryObject.setParameter("oldSsHeaderId", oldSsHeaderId);
        queryObject.setParameter("unitId", unitId);
        queryObject.executeUpdate();
    }

    public void updateUnitId(Long ssHeaderId, Long oldUnitId, Long newUnitId) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" UPDATE lcl_unit_ss_remarks set unit_id =:newUnitId ");
        queryStr.append(" WHERE ss_header_id =:ssHeaderId  and unit_id=:oldUnitId");
        SQLQuery queryObject = getSession().createSQLQuery(queryStr.toString());
        queryObject.setParameter("ssHeaderId", ssHeaderId);
        queryObject.setParameter("oldUnitId", oldUnitId);
        queryObject.setParameter("newUnitId", newUnitId);
        queryObject.executeUpdate();
        getCurrentSession().getTransaction().commit();
        getCurrentSession().getTransaction().begin();
    }
}
