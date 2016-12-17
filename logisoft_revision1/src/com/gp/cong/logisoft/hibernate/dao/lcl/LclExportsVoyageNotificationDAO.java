/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.struts.form.lcl.LclAddVoyageForm;
import com.gp.cvst.logisoft.struts.form.lcl.LclExportNotiFicationForm;
import com.logiware.lcl.model.LclNotificationModel;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.struts.util.LabelValueBean;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BooleanType;
import org.hibernate.type.DateType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

/**
 *
 * @author aravindhan.v
 */
public class LclExportsVoyageNotificationDAO extends BaseHibernateDAO {

    public Long saveLclExportVoyageNotification(Long headerId, LclAddVoyageForm lclAddVoyageForm, boolean Shipper, boolean Consignee, boolean Forwarder, boolean Notify, boolean BookingContact,
            boolean internalEmployees, boolean portAgent, String remarks, Integer reasonCode, String voyageChangeReason, String status, User userId) throws Exception {
        String queryString = "INSERT INTO  lcl_export_voyage_notification(voyage_id,shipper,consignee,forwarder,notify,booking_con,port_agent,internal_employees,"
                + "remarks,reason_code,voyage_change_reason,vessel,pier,port_lrd,sail_date,eta_date,ss_line,ss_voyage,notice_status,entered_by_user_id,entered_datetime)"
                + "values(:headeId,:Shipper,:Consignee,:Forwarder,:Notify,:BookingContact,:portAgent,:internalEmployees,"
                + ":remarks,:reasonCode,:voyageChangeReason,:vessel,:pier,:port_lrd,:sail_date,:eta_date,:ss_line,:ss_voyage,:status,:userId,:ndate)";
        String vessel = null, pier = null, port_lrd = null, sail_date = null, eta_date = null, ss_line = null, ss_voyage = null;

        if (lclAddVoyageForm != null) {
            vessel = lclAddVoyageForm.getSpReferenceName();
            pier = lclAddVoyageForm.getDeparturePier();
            port_lrd = lclAddVoyageForm.getLrdOverrideDays().toString();
            sail_date = lclAddVoyageForm.getStd();
            eta_date = lclAddVoyageForm.getEtaPod();
            ss_line = lclAddVoyageForm.getAccountName();
            ss_voyage = lclAddVoyageForm.getSpReferenceNo();
        }
        SQLQuery query = getCurrentSession().createSQLQuery(queryString);
        query.setParameter("headeId", headerId);
        query.setParameter("Shipper", Shipper);
        query.setParameter("Consignee", Consignee);
        query.setParameter("Forwarder", Forwarder);
        query.setParameter("Notify", Notify);
        query.setParameter("BookingContact", BookingContact);
        query.setParameter("portAgent", portAgent);
        query.setParameter("internalEmployees", internalEmployees);
        query.setParameter("remarks", remarks);
        query.setParameter("voyageChangeReason", voyageChangeReason);
        query.setParameter("reasonCode", reasonCode);
        query.setParameter("vessel", vessel);
        query.setParameter("pier", pier);
        query.setParameter("port_lrd", port_lrd);
        query.setParameter("sail_date", DateUtils.parseDate(sail_date, "dd-MMM-yyyy"));
        query.setParameter("eta_date", DateUtils.parseDate(eta_date, "dd-MMM-yyyy"));
        query.setParameter("ss_line", ss_line);
        query.setParameter("ss_voyage", ss_voyage);
        query.setParameter("status", status);
        query.setParameter("userId", userId.getId());
        query.setParameter("ndate", new Date());
        query.executeUpdate();
        BigInteger id = (BigInteger) getCurrentSession().createSQLQuery("Select LAST_INSERT_ID()").uniqueResult();
        getCurrentSession().getTransaction().commit();
        getCurrentSession().getTransaction().begin();
        return id.longValue();
    }

    public int getNotificationUser(Long notificationId) throws Exception {
        SQLQuery query = getCurrentSession().createSQLQuery("SELECT lvn.`entered_by_user_id`  FROM lcl_export_voyage_notification lvn WHERE lvn.`id`=:id");
        query.setParameter("id", notificationId);
        return (Integer) query.uniqueResult();
    }

    public List<LclNotificationModel> getNotificationIds(String status) throws Exception {
        SQLQuery query = getCurrentSession().createSQLQuery("Select id as id,voyage_id as ssDetailId  from lcl_export_voyage_notification where notice_status=:status");
        query.setParameter("status", status);
        query.addScalar("ssDetailId", LongType.INSTANCE);
        query.addScalar("id", LongType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(LclNotificationModel.class));
        return query.list();
    }

    public String getFileNumbersByHeaderId(Long headerId) throws Exception {
        String queryString = " select GROUP_CONCAT(DISTINCT fn.fileNumberIds) from (SELECT  GROUP_CONCAT(DISTINCT lb.`file_number_id`) "
                + " AS fileNumberIds FROM lcl_booking_piece lb JOIN lcl_booking_piece_unit lbu  ON lbu.`booking_piece_id` = lb.`id` "
                + " JOIN lcl_unit_ss lss ON lss.id = lbu.`lcl_unit_ss_id` WHERE lss.`ss_header_id` =:headerId "
                + " union SELECT  GROUP_CONCAT(DISTINCT b.`file_number_id`) AS fileNumberIds FROM lcl_booking b"
                + " JOIN lcl_ss_header lsh  ON b.`booked_ss_header_id` = lsh.`id` "
                + " JOIN lcl_booking_piece bp ON bp.`file_number_id` = b.`file_number_id` "
                + " LEFT JOIN lcl_booking_piece_unit bpu ON bpu.`booking_piece_id` = bp.`id` "
                + " WHERE lsh.`id` =:headerId AND bpu.`booking_piece_id` IS NULL ) as fn";
        SQLQuery query = getCurrentSession().createSQLQuery(queryString);
        query.setParameter("headerId", headerId);
        return (String) query.uniqueResult();
    }

    public String getContactEmailAndFaxListByCodeI(Long fileID, String emailCode, String faxcode, Long notificationId) throws Exception {
        String queryString = "select LclContactGetEmailFaxByFileIdCodeI(:fileID,:emailCode,:faxcode,:notificationId)";
        SQLQuery query = getCurrentSession().createSQLQuery(queryString);
        query.setParameter("fileID", fileID);
        query.setParameter("emailCode", emailCode);
        query.setParameter("faxcode", faxcode);
        query.setParameter("notificationId", notificationId);
        return (String) query.uniqueResult();
    }

    public void updateExportsVoyageNotification(Long notificationId, String status) throws Exception {
        SQLQuery query = getCurrentSession().createSQLQuery("update lcl_export_voyage_notification  evn set evn.notice_status=:status"
                + " where evn.id=:id");
        query.setParameter("id", notificationId);
        query.setParameter("status", status);
        query.executeUpdate();
    }

    public String getVoyageNumberByNotificationId(Long notificationId) throws Exception {
        SQLQuery query = getCurrentSession().createSQLQuery("SELECT  lsh.`schedule_no` FROM lcl_ss_header lsh  JOIN lcl_export_voyage_notification evn "
                + " ON evn.voyage_id = lsh.id  WHERE evn.`id`= :id");
        query.setParameter("id", notificationId);
        return (String) query.uniqueResult();
    }

    public Long getHeaderIdByVoyageNumber(String voyageNumber) throws Exception {
        SQLQuery query = getCurrentSession().createSQLQuery("SELECT lsh.id FROM lcl_ss_header lsh WHERE lsh.`schedule_no`=:voyageNumber");
        query.setParameter("voyageNumber", voyageNumber);
        return (Long) query.uniqueResult();
    }

    public String getFileNumberByHeaderIdUnitId(Long headerId, Long unitId) throws Exception {
        String queryString = " select GROUP_CONCAT(DISTINCT fn.fileNumberIds) from ( SELECT  GROUP_CONCAT(DISTINCT lb.`file_number_id`) AS fileNumberIds "
                + " FROM lcl_booking_piece lb JOIN lcl_booking_piece_unit lbu  ON lbu.`booking_piece_id` = lb.`id` "
                + " JOIN lcl_booking_piece_unit lbu  ON lbu.`booking_piece_id` = lb.`id` "
                + " JOIN lcl_unit_ss lss ON lss.id = lbu.`lcl_unit_ss_id` WHERE lss.`ss_header_id` =:headerId AND lss.`unit_id`=:unitId "
                + " union SELECT  GROUP_CONCAT(DISTINCT b.`file_number_id`) AS fileNumberIds FROM lcl_booking b"
                + " JOIN lcl_ss_header lsh  ON b.`booked_ss_header_id` = lsh.`id` "
                + " JOIN lcl_booking_piece bp ON bp.`file_number_id` = b.`file_number_id` "
                + " LEFT JOIN lcl_booking_piece_unit bpu ON bpu.`booking_piece_id` = bp.`id` "
                + " WHERE lsh.`ss_header_id` =:headerId AND bpu.`booking_piece_id` IS NULL ) as fn";
        SQLQuery query = getCurrentSession().createSQLQuery(queryString);
        query.setParameter("headerId", headerId);
        query.setParameter("unitId", unitId);
        return (String) query.uniqueResult();
    }

    public void saveExportGeneralNotification(Long headerId, Long unitId, String remarks, String title, byte[] file, int userId, int modifiedId) throws Exception {
        String queryString = "insert into lcl_export_general_notification(voyage_id,unit_id,remarks,title,distribution_file,entered_by_user_id,entered_datetime,modified_by_user_id,modified_datetime)"
                + "  values(:headerId,:unitId,:remarks,:title,:fileName,:userId,:ndate,:modifiedId,:mdate)";
        SQLQuery query = getCurrentSession().createSQLQuery(queryString);
        query.setParameter("headerId", headerId);
        query.setParameter("unitId", unitId);
        query.setParameter("remarks", remarks);
        query.setParameter("title", title);
        query.setParameter("fileName", file);
        query.setParameter("userId", userId);
        query.setParameter("ndate", new Date());
        query.setParameter("modifiedId", userId);
        query.setParameter("mdate", new Date());
        query.executeUpdate();
    }

    public List<LclExportNotiFicationForm> getExportGeneralNotificationList(Long headerId, Long unitId) throws Exception {
        StringBuilder queryString = new StringBuilder();
        SQLQuery query = null;
        queryString.append("select lvn.`id` AS notificationId,lvn.voyage_id as headerId,lvn.unit_id as unitId,(SELECT lsh.schedule_no FROM lcl_ss_header lsh WHERE lsh.id=headerId) AS voyageNo,"
                + "(SELECT lu.unit_no FROM lcl_unit lu WHERE lu.id=unitId) AS unitNo,lvn.remarks AS remarks,lvn.distribution_file AS fileName,"
                + "(SELECT usr.login_name FROM user_details usr WHERE usr.user_id=lvn.`entered_by_user_id`) AS userName from "
                + "lcl_export_general_notification lvn where voyage_id=:headerId");
        if (CommonUtils.isNotEmpty(unitId)) {
            queryString.append(" and unit_id=:unitId");
        }
        query = getCurrentSession().createSQLQuery(queryString.toString());
        if (CommonUtils.isNotEmpty(unitId)) {
            query.setParameter("unitId", unitId);
        }
        query.setParameter("headerId", headerId);
        query.setResultTransformer(Transformers.aliasToBean(LclExportNotiFicationForm.class));
        query.addScalar("notificationId", LongType.INSTANCE);
        query.addScalar("headerId", LongType.INSTANCE);
        query.addScalar("unitId", LongType.INSTANCE);
        query.addScalar("voyageNo", StringType.INSTANCE);
        query.addScalar("unitNo", StringType.INSTANCE);
        query.addScalar("remarks", StringType.INSTANCE);
        query.addScalar("userName", StringType.INSTANCE);
        query.addScalar("fileName", StringType.INSTANCE);
        return query.list();
    }

    public void deleteNotificationId(Long notificationId) throws Exception {
        String queryString = "DELETE FROM lcl_export_general_notification WHERE id=:id";
        SQLQuery query = getCurrentSession().createSQLQuery(queryString);
        query.setParameter("id", notificationId);
        query.executeUpdate();
    }

    public byte[] viewNotificationReport(Long notificationId) throws Exception {
        SQLQuery query = getCurrentSession().createSQLQuery("select distribution_file as content FROM lcl_export_general_notification WHERE id=:id");
        query.setParameter("id", notificationId);
        return (byte[]) query.uniqueResult();
    }

    public String subjectforVoyageNotification(Long notificationId) throws Exception {
        SQLQuery query = getCurrentSession().createSQLQuery("select remarks as content FROM lcl_export_voyage_notification WHERE id=:id");
        query.setParameter("id", notificationId);
        return (String) query.uniqueResult();
    }

    public LclExportNotiFicationForm getNotificationDetail(Long notificationId) throws Exception {
        SQLQuery query = getCurrentSession().createSQLQuery("SELECT lsh.voyage_id as headerId,lsh.`remarks` as remarks,lsh.`entered_by_user_id` as userId,lsh.`entered_datetime` as enterDateTime,"
                + " lsh.vessel as vessel, lsh.pier as pier, lsh.port_lrd  as portLrd, lsh.sail_date as sailDate, lsh.eta_date as etaDate, lsh.ss_line as ssLine,"
                + " lsh.ss_voyage  as ssVoyage, notice_status as noticeStatus,lsh.voyage_change_reason as voyageComment,CONCAT(gd.code,'-',gd.`codedesc`) AS voyageReason "
                + "FROM lcl_export_voyage_notification lsh  LEFT JOIN genericcode_dup gd ON gd.id=lsh.`reason_code` WHERE lsh.id=:notificationId ");
        query.setParameter("notificationId", notificationId);
        query.setResultTransformer(Transformers.aliasToBean(LclExportNotiFicationForm.class));
        query.addScalar("headerId", LongType.INSTANCE);
        query.addScalar("remarks", StringType.INSTANCE);
        query.addScalar("userId", LongType.INSTANCE);
        query.addScalar("enterDateTime", DateType.INSTANCE);
        query.addScalar("vessel", StringType.INSTANCE);
        query.addScalar("pier", StringType.INSTANCE);
        query.addScalar("portLrd", StringType.INSTANCE);
        query.addScalar("sailDate", DateType.INSTANCE);
        query.addScalar("etaDate", DateType.INSTANCE);
        query.addScalar("ssLine", StringType.INSTANCE);
        query.addScalar("ssVoyage", StringType.INSTANCE);
        query.addScalar("noticeStatus", StringType.INSTANCE);
        query.addScalar("voyageComment", StringType.INSTANCE);
        query.addScalar("voyageReason", StringType.INSTANCE);
        return (LclExportNotiFicationForm) query.uniqueResult();
    }

    public List<LabelValueBean> reasonCodesorVoyageNotification(String description) throws Exception {
        List<LabelValueBean> reasonList = new ArrayList<LabelValueBean>();
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT id AS codeId , CONCAT(CODE ,'-', codedesc) as codedesc FROM genericcode_dup  WHERE codetypeId= ");
        sb.append(" (SELECT codetypeid FROM codetype WHERE description =:description) ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setString("description", description);
        List<Object[]> resultList = query.list();
        if (CommonUtils.isNotEmpty(resultList)) {
            for (Object[] row : resultList) {
                String codeId = (String) row[0].toString();
                String codeReason = (String) row[1].toString();
                if (null != row[1] && null != row[0]) {
                    reasonList.add(new LabelValueBean(codeReason, codeId));
                }
            }
        }
        return reasonList;
    }

    public String getAllContainerNoFormVoyage(String headerId) throws Exception {
        SQLQuery query = getCurrentSession().createSQLQuery(" SELECT GROUP_CONCAT(lu.`unit_no`) AS UnitNo FROM lcl_ss_header "
                + "lsh JOIN lcl_unit_ss  lus ON lus.`ss_header_id` = lsh.`id` JOIN lcl_unit lu ON lu.`id` = lus.`unit_id` "
                + "WHERE lsh.id=:headerId");
        query.setParameter("headerId", headerId);
        return (String) query.uniqueResult();
    }

    public void insertVoyageRemarks(User loginUser, String notificationId) throws Exception {
        SQLQuery query = getCurrentSession().createSQLQuery(" SELECT GROUP_CONCAT(IF(l.`shipper`,'SHIPPER\\\\','') , IF(l.`consignee`,'CONSIGNEE\\\\',''),IF(l.`forwarder`,'FORWARDER\\\\',''), "
                + "IF(l.`internal_employees`,'ECI EMPLOYEES\\\\',''), IF(l.`notify`,'NOTIFY\\\\',''), IF(l.`port_agent`,'AGENT\\\\',''),"
                + "IF(l.`booking_con`,'BOOKING CONTACT','')) AS notes FROM  lcl_export_voyage_notification l WHERE l.id =:notificationId");
        query.setParameter("notificationId", notificationId);
        Object result = query.uniqueResult();
        if (null != result) {
            SQLQuery query2 = getCurrentSession().createSQLQuery("insert into lcl_ss_remarks(ss_header_id,type,remarks,"
                    + "entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id)"
                    + "values((select voyage_id from lcl_export_voyage_notification where id=:notificationId),:type,:remarks,:eTime,:eUser,:mTime,:mUser)");
            query2.setParameter("notificationId", notificationId);
            query2.setParameter("type", "auto");
            query2.setParameter("remarks", "Voyage Change Notification sent to  " + result.toString());
            query2.setParameter("eTime", new Date());
            query2.setParameter("eUser", loginUser.getUserId());
            query2.setParameter("mTime", new Date());
            query2.setParameter("mUser", loginUser.getUserId());
            query2.executeUpdate();
        }
        SQLQuery query2 = getCurrentSession().createSQLQuery("insert into lcl_ss_remarks(ss_header_id,type,remarks,"
                + " entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id) values("
                + " (select voyage_id from lcl_export_voyage_notification where id=:notificationId),:type,"
                + " (SELECT CONCAT('Voyage Change Notification Reason Code ' ,(select CONCAT(gd.code,' \\\"',gd.codedesc,'\\\"') from "
                + " lcl_export_voyage_notification l join genericcode_dup gd on gd.id = l.reason_code where l.id =:notificationId)))"
                + " ,:eTime,:eUser,:mTime,:mUser)");
        query2.setParameter("notificationId", notificationId);
        query2.setParameter("type", "auto");
        query2.setParameter("eTime", new Date());
        query2.setParameter("eUser", loginUser.getUserId());
        query2.setParameter("mTime", new Date());
        query2.setParameter("mUser", loginUser.getUserId());
        query2.executeUpdate();

    }

    public String[] fromEmailAtRetadd(String origin, String port, String code) throws Exception {
        String[] result = new String[2];
        StringBuilder sb = new StringBuilder();
        sb.append("Select email,usrnam from retadd where blterm=(select trmnum from terminal where unlocationcode1=:origin and actyon = 'Y') ");
        sb.append(" and destin=(select govschnum from ports where unlocationcode=:port) and code=:code ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameter("origin", origin);
        query.setParameter("port", port);
        query.setParameter("code", code);
        List<Object[]> data = query.list();
        for (Object[] obj : data) {
            result[0] = obj[0] != null ? (String) obj[0].toString() : "";
            result[1] = obj[1] != null ? (String) obj[1].toString() : "";
        }
        return result;
    }
    
    public boolean getInternalEmpStatus(Long notificationId) throws Exception {
        String query = "SELECT `internal_employees` as flag FROM `lcl_export_voyage_notification` WHERE id=:notificationId LIMIT 1;";
        SQLQuery queryObject = getCurrentSession().createSQLQuery(query);
        queryObject.setParameter("notificationId", notificationId);
        queryObject.addScalar("flag", BooleanType.INSTANCE);
        return (boolean) queryObject.uniqueResult();
    }
}
