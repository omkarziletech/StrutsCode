/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.lcl.LclUnitSsDispo;
import java.util.List;
import java.math.BigInteger;
import java.util.Date;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class LclUnitSsDispoDAO extends BaseHibernateDAO<LclUnitSsDispo> {

    public LclUnitSsDispoDAO() {
        super(LclUnitSsDispo.class);
    }

    public void delteLclUnitSsDispo(Long unitId, Long ssDetailId, Integer dispoId) throws Exception {
        String sqlquery = "delete from LclUnitSsDispo where lclUnit.id='" + unitId + "' and lclSsDetail.id='" + ssDetailId + "' and disposition.id='" + dispoId + "'";
        getCurrentSession().createQuery(sqlquery).executeUpdate();
        getCurrentSession().flush();
    }

    public Integer getDispoId(Long unitId) throws Exception {
        String sqlQuery = "SELECT disposition_id FROM lcl_unit_ss_dispo WHERE unit_id='" + unitId + "' ORDER BY id DESC LIMIT 1";
        Object dispId = getSession().createSQLQuery(sqlQuery).uniqueResult();
        return null != dispId ? (Integer) dispId : null;
    }

    public String getDisposition(String unitId) throws Exception {
        String query = "SELECT elite_code FROM disposition WHERE id=(SELECT disposition_id FROM lcl_unit_ss_dispo WHERE unit_id= '" + unitId + "' ORDER BY id DESC LIMIT 1 ) limit 1";
        Object description = getSession().createSQLQuery(query).uniqueResult();
        return null != description ? description.toString() : "";
    }

    public List getUnitDispoDetails(String unitId, Long detailId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT disp.description,DATE_FORMAT(lusd.disposition_datetime,'%d-%b-%Y %r') ");
        sb.append(" FROM lcl_unit_ss_dispo lusd JOIN disposition disp ON lusd.disposition_id=disp.id ");
        sb.append("WHERE unit_id =:unitId");
        sb.append(" AND ss_detail_id =:detailId").append(" ORDER BY lusd.id DESC");
        SQLQuery query = getSession().createSQLQuery(sb.toString());
        query.setParameter("unitId", unitId);
        query.setParameter("detailId", detailId);
        return query.list();
    }
  
    public List<LclUnitSsDispo> getUnitDispoDetailsWithoutData(Long unitId, Long detailId) throws Exception {
        Criteria criteria = getSession().createCriteria(LclUnitSsDispo.class, "lclUnitSsDispo");
        criteria.createAlias("lclUnitSsDispo.disposition", "disposition");
        if (!CommonUtils.isEmpty(unitId)) {
            criteria.add(Restrictions.eq("lclUnitSsDispo.lclUnit.id", unitId));
            criteria.add(Restrictions.eq("lclUnitSsDispo.lclSsDetail.id", detailId));
        }
        criteria.add(Restrictions.ne("disposition.eliteCode", "DATA"));
        criteria.add(Restrictions.ne("disposition.eliteCode", "CLSD"));
        criteria.add(Restrictions.ne("disposition.eliteCode", "AUDT"));
        criteria.addOrder(Order.desc("id"));
        return criteria.list();
    }

    public Integer getUnitDispoCountByDispDesc(String unitId, String eliteCode, Long detailId) throws Exception {
        BigInteger count = new BigInteger("0");
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT count(*) FROM lcl_unit_ss_dispo lusd JOIN disposition disp ON lusd.disposition_id=disp.id WHERE unit_id=");
        sb.append(unitId).append(" and ss_detail_id =:detailId and elite_code = '").append(eliteCode).append("'");
        SQLQuery query = getSession().createSQLQuery(sb.toString());
        query.setLong("detailId", detailId);
        Object o = query.uniqueResult();
        if (o != null) {
            count = (BigInteger) o;
        }
        return count.intValue();
    }

    public void insertVoyageNotification(String unitSsId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO lcl_voyage_notification(unit_ss_id,MINUTE,Daily,entered_datetime)VALUES(").append(unitSsId).append(",'Pending','Pending',now())");
        getCurrentSession().createSQLQuery(sb.toString()).executeUpdate();
    }

    public String getPdfDocumentName(String unitId, Long detailId) throws Exception {//remove
        String headingName = "";
        if (null != unitId && !"".equals(unitId)) {
            Integer dispCount = this.getUnitDispoCountByDispDesc(unitId, "PORT", detailId);
            if (dispCount == 0) {
                headingName = "Pre Advice";
            } else if (dispCount > 0) {
                String disposition = this.getDisposition(unitId);
                if (disposition.equalsIgnoreCase("PORT")) {
                    headingName = "Arrival Notice";
                } else {
                    headingName = "Status Update";
                }
            }
        } else {
            headingName = "Pre Advice";
        }
        return headingName;
    }

    public String getDispositionByDetailId(Long unitId, Long detailId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT elite_code FROM disposition WHERE id=");
        sb.append("(SELECT disposition_id FROM lcl_unit_ss_dispo WHERE unit_id= :unitId and ss_detail_id =:detailId ORDER BY id DESC LIMIT 1) limit 1");
        SQLQuery query = getSession().createSQLQuery(sb.toString());
        query.setLong("unitId", unitId);
        query.setLong("detailId", detailId);
        String description = (String) query.uniqueResult();
        return null != description ? description : "";
    }

    public List<LclUnitSsDispo> getDispositionList(Long unitId, Long detailId) throws Exception {
        Criteria criteria = getSession().createCriteria(LclUnitSsDispo.class, "lclUnitSsDispo");
        criteria.createAlias("lclUnitSsDispo.disposition", "disposition");
        if (CommonUtils.isNotEmpty(unitId)) {
            criteria.add(Restrictions.eq("lclUnitSsDispo.lclUnit.id", unitId));
        }
        if (CommonUtils.isNotEmpty(detailId)) {
            criteria.add(Restrictions.eq("lclUnitSsDispo.lclSsDetail.id", detailId));
        }
        criteria.addOrder(Order.desc("id"));
        return criteria.list();
    }

    public String getDispoCode(String fileId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append("  d.elite_code AS dispoCode");
        sb.append(" FROM");
        sb.append("  lcl_booking_dispo lb ");
        sb.append("  JOIN disposition d ");
        sb.append("    ON d.id = lb.disposition_id ");
        sb.append(" WHERE lb.`file_number_id` = :fileId AND");
        sb.append(" (d.elite_code = 'INTX' || d.elite_code = 'INTR')");
        sb.append(" LIMIT 1 ");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setParameter("fileId", fileId);
        return queryObject.uniqueResult().toString();
    }

    public void insertLclUnitSsDisposition(Long unitId, Long detailId, Integer dispoId, Integer userId) throws Exception {
        Date d = new Date();
        StringBuilder sb = new StringBuilder();
        sb.append("insert into lcl_unit_ss_dispo(unit_id,ss_detail_id,disposition_id,disposition_datetime,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)");
        sb.append(" values (:unitId,:detailId,:dispoId,:now,:now,:user,:now,:user)");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setParameter("unitId", unitId);
        queryObject.setParameter("detailId", detailId);
        queryObject.setParameter("dispoId", dispoId);
        queryObject.setParameter("now", d);
        queryObject.setParameter("user", userId);
        queryObject.executeUpdate();
    }

    public void updateSsHeaderId(Long oldSsDetailId, Long newSsDetailId, Long unitId, Integer userId) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append("UPDATE lcl_unit_ss_dispo set ss_detail_id =:newSsHeaderId, ");
        queryStr.append(" modified_datetime=:dateTime,modified_by_user_id=:userId ");
        queryStr.append(" WHERE ss_detail_id = :oldSsHeaderId and unit_id=:unitId");
        SQLQuery queryObject = getSession().createSQLQuery(queryStr.toString());
        queryObject.setParameter("newSsHeaderId", newSsDetailId);
        queryObject.setParameter("userId", userId);
        queryObject.setParameter("dateTime", new Date());
        queryObject.setParameter("oldSsHeaderId", oldSsDetailId);
        queryObject.setParameter("unitId", unitId);
        queryObject.executeUpdate();
    }

    public void updateUnitId(Long ssDetailId, Long oldUnitId, Long newUnitId, Integer userId) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append("UPDATE lcl_unit_ss_dispo set unit_id =:newUnitId, ");
        queryStr.append(" modified_datetime=:dateTime,modified_by_user_id=:userId ");
        queryStr.append(" WHERE ss_detail_id = :ssDetailId and unit_id=:oldUnitId");
        SQLQuery queryObject = getSession().createSQLQuery(queryStr.toString());
        queryObject.setParameter("ssDetailId", ssDetailId);
        queryObject.setParameter("oldUnitId", oldUnitId);
        queryObject.setParameter("userId", userId);
        queryObject.setParameter("dateTime", new Date());
        queryObject.setParameter("newUnitId", newUnitId);
        queryObject.executeUpdate();
        getCurrentSession().getTransaction().commit();
        getCurrentSession().getTransaction().begin();
    }
    public String getImpUnitDispostionCode(Long fileId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT d.`elite_code` FROM lcl_booking_piece lbp ");
        sb.append(" JOIN lcl_booking_piece_unit lbpu ON  lbpu.booking_piece_id=lbp.id JOIN lcl_unit_ss lus ON lus.id=lbpu.lcl_unit_ss_id ");
        sb.append(" JOIN lcl_unit lu ON lu.id = lus.unit_id JOIN lcl_ss_detail lsd ON (lus.`ss_header_id` = lsd.`ss_header_id`) ");
        sb.append(" JOIN lcl_unit_ss_dispo lusd ON (lsd.`id` = lusd.`ss_detail_id` AND lusd.unit_id=lu.id ) JOIN disposition d ON d.`id` = lusd.`disposition_id` ");
        sb.append(" WHERE lbp.file_number_id = ").append(fileId).append("  ORDER BY lusd.`id` DESC LIMIT 1");
        Object queryReturn = getCurrentSession().createSQLQuery(sb.toString()).uniqueResult();
        return null != queryReturn ? queryReturn.toString() : "";
    }
}
