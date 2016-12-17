/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl.bl;

import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.bl.LclBl;
import com.gp.cong.logisoft.hibernate.dao.LCLPortConfigurationDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBlAcDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import com.gp.cvst.logisoft.hibernate.dao.TransactionDAO;
import com.gp.cvst.logisoft.hibernate.dao.TransactionLedgerDAO;
import com.logiware.hibernate.dao.ArTransactionHistoryDAO;
import com.logiware.lcl.dao.ExportNotificationDAO;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.type.BooleanType;
import org.hibernate.type.StringType;

/**
 *
 * @author Owner
 */
public class LCLBlDAO extends BaseHibernateDAO<LclBl> implements LclCommonConstant {

    private static final Logger log = Logger.getLogger(LCLBlDAO.class);

    public LCLBlDAO() {
        super(LclBl.class);
    }

    public void updateBillToParty(Long fileId, String fieldName, String acctNo, Integer userId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update lcl_bl set ").append(fieldName).append(" = '").append(acctNo).append("',modified_by_user_id = ").append(userId).append(",modified_datetime = SYSDATE() where file_number_id = ").append(fileId);
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public void updateModifiedDate(String fileNo, Integer userId) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" UPDATE lcl_bl bl join lcl_file_number lfn on lfn.id=bl.file_number_id ");
        queryStr.append(" set bl.modified_datetime = SYSDATE(),bl.modified_by_user_id=").append(userId).append(" where lfn.file_number IN(").append(fileNo).append(")");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.executeUpdate();
        getCurrentSession().flush();
        getCurrentSession().clear();
    }

    public void updateModifiedDate(List fileId, Integer userId) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append("UPDATE lcl_bl set modified_datetime = SYSDATE(),modified_by_user_id=").append(userId).append(" where file_number_id IN(:fileIds)");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setParameterList("fileIds", fileId);
        query.executeUpdate();
        getCurrentSession().flush();
        getCurrentSession().clear();
    }

    public void updateModifiedDate(Long fileId, Integer userId) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append("UPDATE lcl_bl set modified_datetime = SYSDATE(),modified_by_user_id=").append(userId).append(" where file_number_id=").append(fileId);
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.executeUpdate();
        getCurrentSession().flush();
        getCurrentSession().clear();
    }

    public void updateBillToParty(Long fileId, String billingType, String billToParty, String fieldName, String acctNo, Integer userId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update lcl_bl set billing_type = '");
        queryBuilder.append(billingType);
        queryBuilder.append("',bill_to_party = '").append(billToParty).append("'");
        if (!fieldName.equals("")) {
            queryBuilder.append(",").append(fieldName).append(" = '").append(acctNo).append("'");

        }
        queryBuilder.append(",modified_by_user_id = ").append(userId);
        queryBuilder.append(",modified_datetime = SYSDATE() where file_number_id = ");
        queryBuilder.append(fileId);
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        getCurrentSession().flush();
        getCurrentSession().clear();
    }

    public String fmcNo(String acctNo) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("select fw_fmc_no fmc from `cust_general_info` where acct_no='").append(acctNo).append("'");
        return (String) getSession().createSQLQuery(sb.toString()).uniqueResult();
    }

    public Long findConsolidateBl(Long fileId) throws Exception {
        StringBuilder query = new StringBuilder();
        query.append(" select bl.file_number_id  from lcl_consolidation lc  join lcl_bl bl on lc.lcl_file_number_id_b =  bl.file_number_id  ");
        query.append(" where lc.lcl_file_number_id_a=").append(fileId);
        BigInteger consolidateFileId = (BigInteger) getSession().createSQLQuery(query.toString()).setMaxResults(1).uniqueResult();
        return (null != consolidateFileId ? consolidateFileId.longValue() : fileId);
    }

    public String getCustomerByFileIdImports(String fileId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT IF(bl.bill_to_party = 'A',agent.acct_no,IF(bl.bill_to_party = 'S',shipper.acct_no,IF(bl.bill_to_party = 'F',fwd.acct_no,");
        sb.append("tprty.acct_no ))) AS customerNumber FROM lcl_file_number FILE JOIN lcl_bl bl  ON file.id = bl.file_number_id LEFT JOIN ");
        sb.append("trading_partner tprty ON bl.third_party_acct_no = tprty.acct_no LEFT JOIN trading_partner agent ON bl.agent_acct_no = agent.acct_no ");
        sb.append("LEFT JOIN trading_partner  shipper ON bl.ship_acct_no = shipper.acct_no LEFT JOIN trading_partner fwd ON bl.fwd_acct_no = fwd.acct_no ");
        sb.append("WHERE file.id = ");
        sb.append(fileId);
        sb.append(" GROUP BY file.id");
        return (String) getSession().createSQLQuery(sb.toString()).uniqueResult();
    }

    public void updatePostStatus(String fileId, String userId, String postFlag, String docCmd) throws Exception {
        StringBuilder query = new StringBuilder();
        LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
        LclBlAcDAO lclBlAcDAO = new LclBlAcDAO();
        query.append("update lcl_bl set posted_datetime=:postDate,posted_by_user_id=:postUserId,modified_datetime = SYSDATE() where file_number_id=:fileId");
        SQLQuery queryObject = getSession().createSQLQuery(query.toString());
        String bType = new LCLBookingDAO().getBookingType(fileId);
        String unLocationCode = new UnLocationDAO().getUnlocationCode(fileId);
        Double ftfFee = new LCLPortConfigurationDAO().getFTFFEECharge(unLocationCode);
        Integer arGLMappingId = new GlMappingDAO().getGLMappingId("FTFFEE", "LCLE", "AR");
        String documentCmd = "";
        if ("true".equals(docCmd)) {
            documentCmd = " Without Document Charge";
        }
        if ("P".equalsIgnoreCase(postFlag)) {
            queryObject.setParameter("postDate", new Date());
            queryObject.setParameter("postUserId", userId);
            lclRemarksDAO.insertLclRemarks(Long.parseLong(fileId), REMARKS_BL_AUTO_NOTES, " BL Posted" + documentCmd, Integer.parseInt(userId));
            ExportNotificationDAO notificationDAO = new ExportNotificationDAO();
            Boolean postedNotiFlag = notificationDAO.isNotificationStatus(Long.parseLong(fileId), "Posting", "Pending");
            if (!postedNotiFlag) {
                notificationDAO.insertNotification(Long.parseLong(fileId), "Posting", "Pending", null, Integer.parseInt(userId));
            }
            if ("T".equalsIgnoreCase(bType) && ftfFee != 0.0) {
                lclBlAcDAO.insertChargesForTransipment(fileId, arGLMappingId, "1", "FL", "0", userId, ftfFee, "A");
            }
        } else {
            queryObject.setParameter("postDate", null);
            queryObject.setParameter("postUserId", null);
            lclRemarksDAO.insertLclRemarks(Long.parseLong(fileId), REMARKS_BL_AUTO_NOTES, " BL UnPosted", Integer.parseInt(userId));
            if ("T".equalsIgnoreCase(bType)) {
                lclBlAcDAO.deleteChargesByFileIdAndArGlMappingId(Long.parseLong(fileId), arGLMappingId.toString(), "1");
            }
        }
        queryObject.setParameter("fileId", fileId);
        queryObject.executeUpdate();
    }

    public void updateVoidStatus(String fileId, String status) throws Exception {
        status = "V";
        StringBuilder queryString = new StringBuilder();
        queryString.append("update lcl_file_number set status='").append(status).append("' where id IN (").append(fileId).append(")");
        SQLQuery query = getCurrentSession().createSQLQuery(queryString.toString());
        query.executeUpdate();
        getCurrentSession().flush();
        getCurrentSession().clear();
    }

    public void updateEciSailings(String fileId, String masterScheduleId) throws Exception {
        StringBuilder query = new StringBuilder();
        query.append("update lcl_bl set booked_ss_header_id=:masterSchedule,modified_datetime=sysdate() where file_number_id=:fileId");
        SQLQuery queryObject = getSession().createSQLQuery(query.toString());
        queryObject.setParameter("masterSchedule", masterScheduleId);
        queryObject.setParameter("fileId", fileId);
        queryObject.executeUpdate();
    }

    public String getBlPoolFiles(String userId) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT GROUP_CONCAT(bl.file_number_id) FROM lcl_bl bl JOIN lcl_booking bk ON bk.`file_number_id`  = bl.`file_number_id`");
        queryStr.append(" JOIN lcl_file_number f ON f.id = bk.`file_number_id` AND  f.`status` NOT IN ('X','RF') AND f.`state` = 'BL'   ");
        queryStr.append(" WHERE bl.posted_by_user_id IS NULL  AND bl.bl_owner_id=:userId ");
        SQLQuery queryObject = getSession().createSQLQuery(queryStr.toString());
        queryObject.setParameter("userId", userId);
        return (String) queryObject.uniqueResult();
    }

    public void updateTerminal(String trmnum, String fileId, HttpServletRequest request) {
        try {
            StringBuilder query = new StringBuilder();
            User user = (User) request.getSession().getAttribute("loginuser");
            query.append("update lcl_bl set billing_terminal=:trmnum,modified_by_user_id=:user,modified_datetime = SYSDATE() where file_number_id=:fileId");
            SQLQuery queryObject = getSession().createSQLQuery(query.toString());
            queryObject.setParameter("trmnum", trmnum);
            queryObject.setParameter("fileId", fileId);
            queryObject.setParameter("user", user.getUserId());
            queryObject.executeUpdate();
            getCurrentSession().flush();
            query = new StringBuilder();
            query.append("update lcl_booking set billing_terminal=:trmnum,modified_by_user_id=:user,modified_datetime = SYSDATE() where file_number_id=:fileId");
            queryObject = getSession().createSQLQuery(query.toString());
            queryObject.setParameter("trmnum", trmnum);
            queryObject.setParameter("fileId", fileId);
            queryObject.setParameter("user", user.getUserId());
            queryObject.executeUpdate();
        } catch (Exception exec) {
            log.info("Error in Update Terminal  method. " + new Date() + " for ", exec);
        } finally {
            try {
                String fileNo = new LclFileNumberDAO().getFileNumberByFileId(fileId);
                String blNumber = this.getExportBlNumbering(fileId);
                blNumber = blNumber.substring(5);
                new TransactionDAO().updateLclEBlNumber(blNumber, fileNo);
                new TransactionLedgerDAO().updateLclEBlNumber(blNumber, fileNo);
                new ArTransactionHistoryDAO().updateLclEBlNumber(blNumber, fileNo);
            } catch (Exception exec) {
                log.info("Error in Update BlNumber in Transaction . " + new Date() + " for ", exec);
            }
        }
    }

    public void inserNotesForBlVoid(String fileId, String userId) throws Exception {
        String remarks = "BL is Voided";
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO lcl_remarks(file_number_id,type,remarks,entered_by_user_id,modified_by_user_id,entered_datetime,modified_datetime)");
        query.append(" VALUES (:fileId,:type,:remarks,:enteredBy,:modifiedBy,SYSDATE(),SYSDATE())");
        SQLQuery queryObject = getSession().createSQLQuery(query.toString());
        queryObject.setParameter("fileId", fileId);
        queryObject.setString("type", REMARKS_BL_AUTO_NOTES);
        queryObject.setString("remarks", remarks);
        queryObject.setParameter("enteredBy", userId);
        queryObject.setParameter("modifiedBy", userId);
        queryObject.executeUpdate();
    }

    public boolean getFreightForwardAcctStatus(String acctNo) throws Exception {
        SQLQuery query = getCurrentSession().createSQLQuery(" SELECT IF(INSTR(p.`value`,:acctNo)>0,TRUE,FALSE) AS result "
                + " FROM property p WHERE p.`name` = 'FreightForwarderAccountNo' ");
        query.setParameter("acctNo", acctNo);
        query.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) query.uniqueResult();
    }

    public boolean getFreightForwardAcctBl(String acctNo) throws Exception {
        SQLQuery query = getCurrentSession().createSQLQuery(" SELECT IF(INSTR(p.`value`,:acctNo)>0,TRUE,FALSE) AS result "
                + " FROM property p WHERE p.`name` = 'FreightForwarderAccountBl' ");
        query.setParameter("acctNo", acctNo);
        query.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) query.uniqueResult();
    }

    public List<String> getConcatBlByUnitSsId(Long unitSsId) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append("SELECT CONCAT(UPPER(RIGHT(org.un_loc_code, 3)),");
        queryStr.append("'-',fd.un_loc_code,'-',lfn.file_number) AS filenumber FROM lcl_unit_ss lus  ");
        queryStr.append(" JOIN lcl_booking_piece_unit lbpu ON lbpu.lcl_unit_ss_id=lus.id ");
        queryStr.append(" JOIN lcl_booking_piece lbp ON lbp.id=lbpu.booking_piece_id ");
        queryStr.append(" JOIN lcl_bl lb ON lb.file_number_id=lbp.file_number_id ");
        queryStr.append(" JOIN lcl_file_number lfn ON lfn.id=lb.file_number_id ");
        queryStr.append(" JOIN un_location org ON org.id=lb.poo_id ");
        queryStr.append(" JOIN un_location fd ON fd.id=IF(lb.fd_id IS NOT NULL,lb.fd_id ,lb.pod_id)");
        queryStr.append(" WHERE lus.id=:unitSsId GROUP BY lbp.file_number_id ");
        SQLQuery queryObject = getSession().createSQLQuery(queryStr.toString());
        queryObject.setParameter("unitSsId", unitSsId);
        return queryObject.list();
    }

    public List<String> getBillToCode(Long fileId) throws Exception {
        String sqlQuery = "SELECT ar_bill_to_party FROM `lcl_bl_ac` WHERE file_number_id =" + fileId;
        return getCurrentSession().createSQLQuery(sqlQuery).list();
    }

    public void updateBillToCode(String fileId, String billCode) throws Exception {
        String query = "update lcl_bl set billing_type=:billCode,modified_datetime=sysdate()  where  file_number_id=:fileId";
        SQLQuery queryObject = getSession().createSQLQuery(query);
        queryObject.setParameter("fileId", fileId);
        queryObject.setParameter("billCode", billCode);
        queryObject.executeUpdate();
    }

    public String getExportBlNumbering(String fileId) throws Exception {
        String strQuery = "Select BlNumberSystemForLclExports(:fileId) as result";
        SQLQuery query = getCurrentSession().createSQLQuery(strQuery);
        query.setParameter("fileId", fileId);
        return (String) query.addScalar("result", StringType.INSTANCE).setMaxResults(1).uniqueResult();
    }

    public void setTerms(String fileId, String terms) throws Exception {
        String query = "update lcl_bl set terms_type1=:termsType where file_number_id=:fileId";
        SQLQuery queryObject = getSession().createSQLQuery(query);
        queryObject.setParameter("fileId", fileId);
        queryObject.setParameter("termsType", terms);
        queryObject.executeUpdate();
    }

    public Boolean isCollectBl(Long fileId) throws Exception {
        String Stquery = "SELECT COUNT(*) FROM lcl_bl_ac WHERE ar_bill_to_party='A' AND file_number_id=:fileId";
        SQLQuery query = getCurrentSession().createSQLQuery(Stquery);
        query.setParameter("fileId", fileId);
        return ((BigInteger) query.uniqueResult()).intValue() > 0;
    }

    public void updateEdiDataToBl(String commodityDes, String marksAndNo, String fileNumberId) throws Exception {
        String query = "UPDATE `lcl_bl_piece` lbp SET lbp.`piece_desc`=:commodityDesc,lbp.`mark_no_desc`=:marksAndNo WHERE lbp.`file_number_id`=:fileId ORDER BY id DESC LIMIT 1";
        SQLQuery queryObject = getCurrentSession().createSQLQuery(query);
        queryObject.setParameter("commodityDesc", commodityDes);
        queryObject.setParameter("marksAndNo", marksAndNo);
        queryObject.setParameter("fileId", fileNumberId);
        queryObject.executeUpdate();
    }

    public String getCollectAmount(Long fileId) throws Exception {
        String query = "SELECT SUM(ar_amount) AS collectAmount FROM (SELECT ar_amount FROM lcl_bl_ac WHERE file_number_id=:fileId AND ar_bill_to_party = 'A' GROUP BY ar_gl_mapping_id) AS charges";
        SQLQuery queryObject = getCurrentSession().createSQLQuery(query);
        queryObject.setParameter("fileId", fileId);
        queryObject.addScalar("collectAmount", StringType.INSTANCE);
        return (String) queryObject.uniqueResult();
    }

    public void updateCIF(Long fileId, String CIF) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("update  lcl_bl set insurance_cif = '").append(CIF).append("' where file_number_id= '").append(fileId).append("'");
        getCurrentSession().createSQLQuery(sb.toString()).executeUpdate();
        getCurrentSession().flush();
        getCurrentSession().clear();
    }

    public void updatePod(String fileId, String pod, String bookingType,
            String relayOverride, HttpServletRequest request) throws Exception {
        User user = (User) request.getSession().getAttribute("loginuser");
        String query = "";
        if ("T".equalsIgnoreCase(bookingType)) {
            query = "UPDATE lcl_booking_import SET foreign_port_of_discharge_id=:pod,modified_datetime=SYSDATE(),modified_by_user_id=:user WHERE file_number_id=:fileId";
        } else {
            query = "update lcl_booking set pod_id=:pod,relay_override=" + relayOverride + ",modified_by_user_id=:user,modified_datetime = SYSDATE() where file_number_id=:fileId";
        }
        SQLQuery queryObject = getSession().createSQLQuery(query);
        queryObject.setParameter("pod", pod);
        queryObject.setParameter("fileId", fileId);
        queryObject.setParameter("user", user.getUserId());
        queryObject.executeUpdate();
        getCurrentSession().flush();

        String sb = "update lcl_bl set pod_id=:pod,relay_override=:relayOverride,modified_by_user_id=:user,modified_datetime = SYSDATE() where file_number_id=:fileId";
        queryObject = getSession().createSQLQuery(sb);
        queryObject.setParameter("pod", pod);
        queryObject.setParameter("relayOverride", relayOverride);
        queryObject.setParameter("fileId", fileId);
        queryObject.setParameter("user", user.getUserId());
        queryObject.executeUpdate();
//            getCurrentSession().flush();
//            getCurrentSession().clear();
    }

    public String getPostAlert(String fileId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT GROUP_CONCAT(IF(actual_weight_imperial IS NULL OR actual_weight_imperial =0.00,TRUE,'') ,");
        sb.append("IF (actual_volume_imperial IS NULL OR actual_volume_imperial =0.00,TRUE,''),");
        sb.append("IF(actual_weight_metric IS NULL OR actual_weight_metric=0.00,TRUE,''),");
        sb.append("IF(actual_volume_metric IS NULL OR actual_volume_metric=0.00,TRUE,''),");
        sb.append("IF(actual_piece_count IS NULL OR actual_piece_count = 0, TRUE, '')) AS WeightVolume  ");
        sb.append("FROM lcl_bl_piece  WHERE file_number_id=:fileId ");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.addScalar("WeightVolume", StringType.INSTANCE);
        queryObject.setParameter("fileId", fileId);
        return (String) queryObject.uniqueResult();
    }

    public void updateRateType(String rateType, String fileId) throws Exception {
        String query = "UPDATE lcl_bl SET rate_type=:rateType WHERE file_number_id=:fileId limit 1";
        SQLQuery queryObject = getCurrentSession().createSQLQuery(query);
        queryObject.setParameter("rateType", rateType);
        queryObject.setParameter("fileId", fileId);
        queryObject.executeUpdate();
    }

    public boolean getRateTypeFlag(String fileId, String reteType) throws Exception {
        String query = "SELECT IF(COUNT(*) > 0,TRUE,FALSE) AS flag FROM lcl_bl WHERE file_number_id =:fileId AND rate_type <> :reteType";
        SQLQuery queryObject = getCurrentSession().createSQLQuery(query);
        queryObject.setParameter("fileId", fileId);
        queryObject.setParameter("reteType", reteType);
        queryObject.addScalar("flag", BooleanType.INSTANCE);
        return (Boolean) queryObject.uniqueResult();
    }

    public boolean getPoolStatus(Long fileId) throws Exception {
        String query = "SELECT IF(COUNT(*) > 0,TRUE,FALSE) AS flag FROM lcl_bl WHERE file_number_id =:fileId AND posted_by_user_id is not null";
        SQLQuery queryObject = getCurrentSession().createSQLQuery(query);
        queryObject.setParameter("fileId", fileId);
        queryObject.addScalar("flag", BooleanType.INSTANCE);
        return (Boolean) queryObject.uniqueResult();
    }
}
