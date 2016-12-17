 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.lcl.LclBookingDispo;
import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.type.BooleanType;

public class LclBookingDispoDAO extends BaseHibernateDAO<LclBookingDispo> {

    private static final Log log = LogFactory.getLog(LclBookingDispo.class);

    public LclBookingDispoDAO() {
        super(LclBookingDispo.class);
    }

    public void delteLclBookingDispo(Long fileno, Long unitId, Integer dispoId) throws Exception {
        String sqlquery = "delete from LclBookingDispo where lclUnit.id='" + unitId + "' and lclFileNumber.id='" + fileno + "' and disposition.id='" + dispoId + "'";
        getCurrentSession().createQuery(sqlquery).executeUpdate();
        getCurrentSession().flush();
    }

    public void deleteDisposition(Long fileId) throws Exception {
        Query query = getCurrentSession().createQuery(" delete from LclBookingDispo where lclFileNumber.id=:fileId");
        query.setParameter("fileId", fileId);
        query.executeUpdate();
    }

    public String currenLocationUnLocName(Long fileId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT CONCAT(UPPER(un.un_loc_name),'/',IFNULL(UPPER(p.statecode),''),'(',unlocationcode,')') FROM lcl_booking_dispo lbd LEFT JOIN un_location un ON ");
        sb.append("lbd.un_location_id=un.id LEFT JOIN ports p ON un.un_loc_code=p.unlocationcode WHERE lbd.file_number_id=").append(fileId).append(" ORDER BY lbd.id DESC limit 1");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        Object polName = query.uniqueResult();
        return null != polName ? polName.toString() : "";
    }

    public Integer currentLocationId(Long fileId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("select unLocation.id from LclBookingDispo where lclFileNumber.id=:fileId order by id desc");
        Query query = getCurrentSession().createQuery(sb.toString()).setLong("fileId", fileId).setMaxResults(1);
        Object currLocId = query.uniqueResult();
        return null != currLocId ? (Integer) currLocId : null;
    }

    public List bookDispoStatus(Long fileId, String bookingType) throws Exception {
        StringBuilder sb = new StringBuilder();
        if ("T".equalsIgnoreCase(bookingType)) {
            sb.append(" SELECT d.`elite_code`, DATE_FORMAT(lusd.`disposition_datetime`,'%d-%b-%Y %r') AS dispdate,NULL AS currentlocation, NULL AS warehouse  FROM lcl_booking_piece lbp ");
            sb.append(" JOIN lcl_booking_piece_unit lbpu ON  lbpu.booking_piece_id=lbp.id JOIN lcl_unit_ss lus ON lus.id=lbpu.lcl_unit_ss_id ");
            sb.append(" JOIN lcl_unit lu ON lu.id = lus.unit_id JOIN lcl_ss_detail lsd ON (lus.`ss_header_id` = lsd.`ss_header_id`) ");
            sb.append(" JOIN lcl_unit_ss_dispo lusd ON (lsd.`id` = lusd.`ss_detail_id` AND lusd.unit_id=lu.id ) JOIN disposition d ON d.`id` = lusd.`disposition_id` ");
            sb.append(" WHERE lbp.file_number_id = ").append(fileId).append(" AND d.`elite_code` <> 'AVAL'  UNION  ");
        }
        sb.append("SELECT ds.elite_code,DATE_FORMAT(lbd.disposition_datetime,'%d-%b-%Y %r') AS dispdate, bd.location  as currentlocation, bd.wareHouseNo  as warehouse   FROM lcl_booking_dispo lbd INNER JOIN ");
        sb.append("(SELECT lb.disposition_id,lb.id AS lbId,");
        sb.append(" lb.un_location_id,`UnLocationGetNameStateCntryByID`(un.`id`) AS location , w.`warehsno` AS wareHouseNo");
        sb.append(" FROM lcl_booking_dispo lb LEFT JOIN un_location un ON lb.un_location_id=un.id LEFT JOIN ");
        sb.append("ports p ON un.un_loc_code=p.unlocationcode  left join warehouse w  on w.id = lb.warehouse_id  WHERE lb.file_number_id =").append(fileId);
        sb.append(" ) bd ON bd.lbId = lbd.id LEFT JOIN disposition ds ON lbd.disposition_id = ds.id");
        if ("T".equalsIgnoreCase(bookingType)) {
            sb.append(" ORDER BY STR_TO_DATE(dispdate,'%d-%b-%Y %r') ASC");
        }
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        return query.list();
    }

    public String getDispositionCode(Long fileId) throws Exception {
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append(" SELECT d.elite_code AS dispoCode FROM lcl_booking_dispo lbp JOIN disposition d ON d.id=lbp.disposition_id  ");
        sqlQuery.append("WHERE lbp.file_number_id=").append(fileId);
        sqlQuery.append(" ORDER BY lbp.id DESC LIMIT 1");
        Object dispoCode = getCurrentSession().createSQLQuery(sqlQuery.toString()).uniqueResult();
        return null != dispoCode ? (String) dispoCode : "";
    }
    public String getDescription(String desc) throws Exception {
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append(" SELECT description FROM disposition WHERE elite_code ='").append(desc).append("'");
        Object code = getCurrentSession().createSQLQuery(sqlQuery.toString()).uniqueResult();
        return null != code ? (String) code : "";
    }

    public void insertBookingDispo(Long fileId, Integer dispoId, Integer loginId) throws Exception {
        log.info("START:Insert Booking Dispostion");
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("INSERT INTO lcl_booking_dispo(file_number_id,disposition_id,disposition_datetime,");
        queryBuilder.append("entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)VALUES(");
        queryBuilder.append(fileId).append(",").append(dispoId).append(",SYSDATE(),").append("SYSDATE(),").append(loginId).append(",SYSDATE(),").append(loginId).append(")");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        getCurrentSession().flush();
        log.info("END:Insert Booking Dispostion");
    }

    public void insertBookingDispo(Long fileId, Integer dispoId, Integer unitId, Integer ssdetailId, Integer loginId, Integer currentLocId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("INSERT INTO lcl_booking_dispo(file_number_id,disposition_id,disposition_datetime,");
        queryBuilder.append("entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id,un_location_id)VALUES(");
        queryBuilder.append(fileId).append(",").append(dispoId).append(",SYSDATE(),").append("SYSDATE(),").append(loginId).append(",SYSDATE(),").append(loginId).append(",");
        queryBuilder.append(currentLocId).append(")");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        getCurrentSession().flush();
    }

    public void insertBookingDispoForRCVD(Long fileId, Integer dispoId, Integer unitId, Integer ssdetailId, Integer loginId, Integer currentLocId, Integer wareHouseId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("INSERT INTO lcl_booking_dispo(file_number_id,disposition_id,unit_id,ss_detail_id,warehouse_id,disposition_datetime,");
        queryBuilder.append("entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id,un_location_id)VALUES(");
        queryBuilder.append(":fileId,:dispoId,:unitId,:detailId,:wareHouseId,:sysDate,:sysDate,:loginId,:sysDate,:loginId,:currLocId)");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setParameter("fileId", fileId);
        query.setParameter("dispoId", dispoId);
        query.setParameter("unitId", unitId);
        query.setParameter("detailId", ssdetailId);
        query.setParameter("sysDate", new Date());
        query.setParameter("loginId", loginId);
        query.setParameter("wareHouseId", wareHouseId);
        query.setParameter("currLocId", currentLocId);
        query.executeUpdate();
        getCurrentSession().flush();
    }

    public Boolean isCheckedDispoCode(Long fileId, List<String> dispoCode) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT IF(COUNT(*)>0,TRUE,FALSE) AS result FROM lcl_booking_dispo lbp ");
        queryBuilder.append(" JOIN disposition d ON d.id=lbp.disposition_id WHERE  ");
        queryBuilder.append("  lbp.file_number_id=:fileId AND d.elite_code IN(:dispoCode) ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setLong("fileId", fileId);
        query.setParameterList("dispoCode", dispoCode);
        query.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) query.uniqueResult();
    }
    public Integer currentDispoId(Long fileId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT l.disposition_id FROM lcl_booking_dispo l ");
        queryBuilder.append(" WHERE l.file_number_id=").append(fileId).append(" ORDER BY id DESC limit 1 ");
        Query queryObject = getSession().createSQLQuery(queryBuilder.toString());
        return (Integer) queryObject.uniqueResult();
    }

    public void insertDisposition(Long fileId, Integer unlocId, Integer warhseId, Integer dispoId, Integer userId) throws Exception {
        Date date = new Date();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" INSERT INTO lcl_booking_dispo(file_number_id,disposition_id,warehouse_id,un_location_id,");
        queryBuilder.append(" entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id,disposition_datetime)VALUES(");
        queryBuilder.append(":fileId,:dispoId,:warehouseId,:unlocId,:entered_datetime,:entered_by_user_id,:modified_datetime,");
        queryBuilder.append(":modified_by_user_id,:dispoTime)");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(queryBuilder.toString());
        queryObject.setLong("fileId", fileId);
        queryObject.setParameter("dispoId", dispoId);
        queryObject.setParameter("warehouseId", warhseId);
        queryObject.setParameter("unlocId", unlocId);
        queryObject.setParameter("entered_datetime", date);
        queryObject.setInteger("entered_by_user_id", userId);
        queryObject.setParameter("modified_datetime", date);
        queryObject.setInteger("modified_by_user_id", userId);
        queryObject.setParameter("dispoTime", date);
        queryObject.executeUpdate();
    }
}
