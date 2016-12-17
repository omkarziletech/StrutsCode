/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.lcl.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.beans.ManifestBean;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.lcl.model.NotificationModel;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BooleanType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

/**
 *
 * @author Mei
 */
public class ExportNotificationDAO extends BaseHibernateDAO {

    public List<NotificationModel> getJobNotification(String status, String notificationStatus, Boolean flag) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT l.file_number_id AS fileNumberId,l.email AS toAddress,  ");
        queryStr.append(" l.id as id,l.file_status as fileStatus, ");
//        queryStr.append(" l.to_name as toName,l.company_name as companyName, ");
        queryStr.append(" f.file_number as fileNumber,f.business_unit as bussinessUnit ");
        queryStr.append(" FROM lcl_export_notification l join lcl_file_number f ");
        queryStr.append(" on f.id=l.file_number_id WHERE l.STATUS=:status ");
        if (flag) {
            queryStr.append("  AND file_status=:notificationStatus  AND l.email <>''   ");
        } else {
            queryStr.append("  AND file_status <> :notificationStatus   ");
        }
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setParameter("status", status);
        query.setParameter("notificationStatus", notificationStatus);
        query.setResultTransformer(Transformers.aliasToBean(NotificationModel.class));
        query.addScalar("id", LongType.INSTANCE);
        query.addScalar("fileNumberId", LongType.INSTANCE);
        query.addScalar("bussinessUnit", StringType.INSTANCE);
        query.addScalar("toAddress", StringType.INSTANCE);
//        query.addScalar("toName", StringType.INSTANCE);
//        query.addScalar("companyName", StringType.INSTANCE);
        query.addScalar("fileStatus", StringType.INSTANCE);
        query.addScalar("fileNumber", StringType.INSTANCE);
        return query.list();
    }
    /*
     * Get Booking Disposition contact codeJ 
     */

    public String getBookingContactCodeJ(String fileId, String dispoColumnName, List<String> bkgAcctNoList) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT GROUP_CONCAT(filterContact.contact SEPARATOR '##') FROM ( ");
        queryStr.append(" SELECT concat_ws('-->',contact.CONTACT_NAME,"); // special char included for separate
        queryStr.append(" contact.ACCT_NAME, contact.Email) as contact  ");
        queryStr.append(" FROM ( SELECT CONCAT(cc.first_name, ' ', cc.last_name) AS contact_name,  ");
        queryStr.append(" (SELECT acct_name FROM  trading_partner WHERE acct_no = cc.acct_no) AS acct_name, ");
        queryStr.append(" CASE g.code WHEN 'E' THEN TRIM(LOWER(cc.email)) WHEN 'F' THEN TRIM(cc.fax) ");
        queryStr.append(" ELSE NULL END AS email FROM cust_contact cc JOIN genericcode_dup g ");
        queryStr.append(" ON (g.id = cc.code_j AND g.code IN ('E', 'F')) ");
        queryStr.append(" WHERE cc.acct_no IN (:bkgAcctNoList) ");
        queryStr.append(" AND cc.applicable_to_all_shipments = TRUE ");
        queryStr.append(" AND ( cc.").append(dispoColumnName).append(" = TRUE OR cc.any = TRUE ) ");
        queryStr.append(" UNION (");
        queryStr.append(appendBkgDispoContactCodeJ("cons_acct_no", "cons_contact_id", dispoColumnName));
        queryStr.append(" ) UNION ( ");
        queryStr.append(appendBkgDispoContactCodeJ("ship_acct_no", "ship_contact_id", dispoColumnName));
        queryStr.append(" ) UNION (");
        queryStr.append(appendBkgDispoContactCodeJ("client_acct_no", "client_contact_id", dispoColumnName));
        queryStr.append(" ) UNION (  ");
        queryStr.append(appendBkgDispoContactCodeJ("fwd_acct_no", "fwd_contact_id", dispoColumnName));
        queryStr.append(" ) UNION (  ");
        queryStr.append(getManualBookingContact());
        queryStr.append(" ) ) AS contact ");
        queryStr.append(" GROUP BY contact.email ) AS filterContact ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setParameter("fileId", fileId);
        query.setParameterList("bkgAcctNoList", bkgAcctNoList);
        Object obj = query.uniqueResult();
        return obj != null ? (String) obj.toString() : "";
    }

    public String getManualBookingContact() {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT COALESCE(cc.contact_name, sc.contact_name,   fc.contact_name,   cnc.contact_name ) AS contact_name, ");
        queryStr.append(" '' AS acct_name, ");
        queryStr.append(" LOWER(COALESCE(cc.email1,sc.`email1`,fc.`email1`,cnc.`email1`)) AS email ");
        queryStr.append(" FROM lcl_booking bkg ");
        queryStr.append("  LEFT JOIN lcl_contact cc ");
        queryStr.append("  ON bkg.`client_contact_id` = cc.id ");
        queryStr.append(" LEFT JOIN lcl_contact sc ");
        queryStr.append("  ON bkg.`ship_contact_id` = sc.id ");
        queryStr.append(" LEFT JOIN lcl_contact fc ");
        queryStr.append("  ON bkg.`fwd_contact_id` = fc.`id` ");
        queryStr.append(" LEFT JOIN lcl_contact cnc ");
        queryStr.append(" ON bkg.`cons_contact_id` = cnc.id ");
        queryStr.append(" WHERE bkg.`file_number_id` =:fileId");
        return queryStr.toString();
    }

    public String appendBkgDispoContactCodeJ(String columnName, String columnName1, String dispoColumn) {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append("  SELECT CONCAT(cc.first_name, ' ', cc.last_name) AS contact_name, ");
        queryStr.append("  (SELECT acct_name FROM  trading_partner WHERE acct_no = cc.acct_no) AS acct_name, ");
        queryStr.append("   CASE gd.code WHEN 'E' THEN TRIM(LOWER(cc.email)) WHEN 'F' ");
        queryStr.append("  THEN TRIM(cc.`fax`) ELSE NULL END AS email ");
        queryStr.append(" FROM lcl_booking lb  ");
        queryStr.append(" JOIN lcl_contact lc ON lb.").append(columnName1).append(" = lc.id ");
        queryStr.append(" JOIN cust_contact cc ON ((lb.").append(columnName).append("=cc.acct_no AND");
        queryStr.append("  CONCAT(cc.first_name,' ',cc.last_name)=lc.contact_name)");
        queryStr.append("  AND cc.only_when_booking_contact=true ");
        queryStr.append(" AND (cc.").append(dispoColumn).append("=TRUE OR cc.any=TRUE))");
        queryStr.append(" JOIN genericcode_dup gd ON (gd.id=cc.code_j AND gd.`code` IN ('E', 'F')) ");
        queryStr.append(" WHERE lb.file_number_id=:fileId ");
        return queryStr.toString();
    }

    public void insertNotification(Long fileId, String fileStatus,
            String status, String email, Integer userId) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" INSERT INTO lcl_export_notification (file_number_id, file_status,");
        queryStr.append("  STATUS,email, entered_datetime, entered_by_user_id)  VALUES  ");
        queryStr.append(" (:fileId,:fileStatus,:status,:email,SYSDATE(),:userId)  ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setParameter("fileId", fileId);
        query.setParameter("fileStatus", fileStatus);
        query.setParameter("status", status);
        query.setParameter("email", email);
        query.setParameter("userId", userId);
        query.executeUpdate();
    }

    public void updateNotification(Long id, String status) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update lcl_export_notification vn set vn.status = :status,");
        queryBuilder.append("vn.modified_datetime=SYSDATE() ");
        queryBuilder.append("where vn.id = :id");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setLong("id", id);
        query.setString("status", status);
        query.executeUpdate();
    }

    public void updateNotification(Long id, String email, String status) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update lcl_export_notification vn set vn.status = :status,");
        queryBuilder.append("vn.modified_datetime=SYSDATE(),email=:sentEmails ");
        queryBuilder.append("where vn.id = :id");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setLong("id", id);
        query.setString("status", status);
        query.setString("sentEmails", email);
        query.executeUpdate();
    }

    public String getBkgAcctNo(Long fileId, boolean consigneeFlag) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("  SELECT CONCAT_WS(',',IF(lb.`agent_acct_no`<> '',lb.`agent_acct_no`,NULL),  ");
        queryBuilder.append("  IF(lb.`client_acct_no`<> '',lb.`client_acct_no`,NULL),  ");
        queryBuilder.append("  IF(lb.`ship_acct_no`<> '',lb.`ship_acct_no`,NULL),  ");
        if (consigneeFlag) {
            queryBuilder.append("  IF(lb.`cons_acct_no`<> '',lb.`cons_acct_no`,NULL),  ");
        }
        queryBuilder.append("  IF(lb.`third_party_acct_no`<> '',lb.`third_party_acct_no`,NULL),  ");
        queryBuilder.append("  IF(lb.`fwd_acct_no`<> '',lb.`fwd_acct_no`,NULL)) AS acctNo  ");
        queryBuilder.append("  FROM lcl_booking lb WHERE lb.file_number_id=:fileId  ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setLong("fileId", fileId);
        query.addScalar("acctNo", StringType.INSTANCE);
        return (String) query.setMaxResults(1).uniqueResult();
    }

    /*
     * Get CodeJ Contact List By Posting
     * @Param isShipmentFlag is true mean will check applicable_to_all_shipments is true
     * @Param acctNoList will pass Booking customer acctNo list
     */
    public List<NotificationModel> getCodeJPostingContactList(Boolean isShipmentFlag,
            List<String> acctNoList, String toEmail, boolean consigneeFlag) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT CONCAT(cc.first_name,' ',cc.last_name) AS toName,");
        queryStr.append("(SELECT acct_name FROM trading_partner WHERE acct_no=cc.acct_no) AS companyName, ");
        queryStr.append("  CASE g.code WHEN 'E' THEN TRIM(LOWER(cc.email)) ");
        queryStr.append(" WHEN 'F' THEN TRIM(cc.fax) ELSE NULL END AS toEmail, ");
        queryStr.append(!consigneeFlag ? " cc.non_neg_rated_posting as nonRated,cc.non_neg_unrated_posting as nonUnRated "
                : "cc.non_neg_rated_posting as nonRated,cc.non_neg_unrated_posting as nonUnRated ");
       // queryStr.append(" cc.non_neg_rated_posting as nonRated,cc.non_neg_unrated_posting as nonUnRated ");
        queryStr.append(" FROM cust_contact cc ");
        queryStr.append(" JOIN genericcode_dup g ON(g.id = cc.code_j AND g.code IN ('E', 'F')) ");
        queryStr.append(" WHERE cc.acct_no IN(:acctNoList) ");
        queryStr.append(isShipmentFlag ? "  AND cc.applicable_to_all_shipments=TRUE " : "");
        queryStr.append(" AND (cc.non_neg_rated_posting = TRUE OR cc.non_neg_unrated_posting = TRUE) ");
        if (!"".equalsIgnoreCase(toEmail)) {
            queryStr.append(" HAVING toEmail <> '").append(toEmail).append("'");
        }
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setParameterList("acctNoList", acctNoList);
        query.setResultTransformer(Transformers.aliasToBean(NotificationModel.class));
        query.addScalar("toName", StringType.INSTANCE);
        query.addScalar("companyName", StringType.INSTANCE);
        query.addScalar("toEmail", StringType.INSTANCE);
        query.addScalar("nonRated", BooleanType.INSTANCE);
        query.addScalar("nonUnRated", BooleanType.INSTANCE);
        return query.list();
    }

    public List<NotificationModel> getCodeJManifestContactList(Boolean isShipmentFlag,
            List<String> acctNoList, String toEmail, boolean consigneeFlag) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT CONCAT(cc.first_name,' ',cc.last_name) AS toName,");
        queryStr.append("(SELECT acct_name FROM trading_partner WHERE acct_no=cc.acct_no) AS companyName, ");
        queryStr.append(" CASE g.code WHEN 'E' THEN TRIM(LOWER(cc.email))  ");
        queryStr.append(" WHEN 'F' THEN TRIM(cc.`fax`) ELSE NULL END AS toEmail, ");
        queryStr.append(!consigneeFlag ? " cc.non_neg_rated_manifest as nonRated,cc.non_neg_unrated_manifest as nonUnRated  "
                : "cc.non_neg_rated_manifest as nonRated,cc.non_neg_unrated_manifest as nonUnRated ");
      //  queryStr.append("cc.non_neg_rated_manifest as nonRated,cc.non_neg_unrated_manifest as nonUnRated");
        queryStr.append(" FROM cust_contact cc ");
        queryStr.append(" JOIN genericcode_dup g ON(g.id = cc.code_j AND g.`code` IN ('E', 'F') ) ");
        queryStr.append(" WHERE cc.acct_no IN(:acctNoList) ");
        queryStr.append(isShipmentFlag ? "  AND cc.applicable_to_all_shipments=TRUE " : "");
        queryStr.append(" AND (cc.non_neg_rated_manifest=TRUE OR cc.non_neg_unrated_manifest=true )   ");
        if (!"".equalsIgnoreCase(toEmail)) {
            queryStr.append(" HAVING toEmail <> '").append(toEmail).append("'");
        }
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setParameterList("acctNoList", acctNoList);
        query.setResultTransformer(Transformers.aliasToBean(NotificationModel.class));
        query.addScalar("toName", StringType.INSTANCE);
        query.addScalar("companyName", StringType.INSTANCE);
        query.addScalar("toEmail", StringType.INSTANCE);
        query.addScalar("nonRated", BooleanType.INSTANCE);
        query.addScalar("nonUnRated", BooleanType.INSTANCE);
        return query.list();
    }

    public List<NotificationModel> getCodeJCobContactList(Boolean isShipmentFlag,
            List<String> acctNoList, String toEmail, boolean consigneeFlag) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT CONCAT(cc.first_name,' ',cc.last_name) AS toName,");
        queryStr.append("(SELECT acct_name FROM trading_partner WHERE acct_no=cc.acct_no) AS companyName, ");
        queryStr.append("  CASE g.code WHEN 'E' THEN TRIM(LOWER(cc.email)) ");
        queryStr.append(" WHEN 'F' THEN TRIM(cc.fax) ELSE NULL END AS toEmail, ");
        queryStr.append(!consigneeFlag ? " cc.non_neg_rated_cob as nonRated,cc.non_neg_unrated_cob as nonUnRated , "
                : "cc.non_neg_rated_cob as nonRated,cc.non_neg_unrated_cob as nonUnRated ,");
        //queryStr.append("cc.non_neg_rated_cob as nonRated,cc.non_neg_unrated_cob as nonUnRated");
        queryStr.append(" cc.confirm_on_board_cob as cob   ");// cc.freight_invoice_cob as freightInvoice
        queryStr.append(" FROM cust_contact cc ");
        queryStr.append(" JOIN genericcode_dup g ON(g.id = cc.code_j AND g.`code` IN ('E', 'F') ) ");
        queryStr.append(" WHERE cc.acct_no IN(:acctNoList) ");
        queryStr.append(isShipmentFlag ? "  AND cc.applicable_to_all_shipments=TRUE " : "");
        queryStr.append(" AND (cc.non_neg_rated_cob=TRUE OR cc.non_neg_unrated_cob=TRUE  ");
        queryStr.append("   OR cc.confirm_on_board_cob=TRUE)  ");
        if (!"".equalsIgnoreCase(toEmail)) {
            queryStr.append(" HAVING toEmail <> '").append(toEmail).append("'");
        }
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setParameterList("acctNoList", acctNoList);
        query.setResultTransformer(Transformers.aliasToBean(NotificationModel.class));
        query.addScalar("toName", StringType.INSTANCE);
        query.addScalar("companyName", StringType.INSTANCE);
        query.addScalar("toEmail", StringType.INSTANCE);
        query.addScalar("nonRated", BooleanType.INSTANCE);
        query.addScalar("nonUnRated", BooleanType.INSTANCE);
        query.addScalar("cob", BooleanType.INSTANCE);
        return query.list();
    }

    public List<NotificationModel> getCodeJChangesContactList(Boolean isShipmentFlag,
            List<String> acctNoList, String toEmail, boolean consigneeFlag) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT CONCAT(cc.first_name,' ',cc.last_name) AS toName,");
        queryStr.append("(SELECT acct_name FROM trading_partner WHERE acct_no=cc.acct_no) AS companyName, ");
        queryStr.append("  CASE g.code WHEN 'E' THEN TRIM(LOWER(cc.email)) ");
        queryStr.append(" WHEN 'F' THEN TRIM(cc.fax) ELSE NULL END AS toEmail, ");
        queryStr.append(!consigneeFlag ? " cc.non_neg_rated_cob as nonRated,cc.non_neg_unrated_cob as nonUnRated  "
                : "cc.non_neg_rated_cob as nonRated,cc.non_neg_unrated_cob as nonUnRated ");
      //  queryStr.append(" cc.non_neg_rated_cob as nonRated,cc.non_neg_unrated_cob as nonUnRated");
        queryStr.append(" FROM cust_contact cc ");
        queryStr.append(" JOIN genericcode_dup g ON(g.id = cc.code_j AND g.`code` IN ('E', 'F') ) ");
        queryStr.append(" WHERE cc.acct_no IN(:acctNoList) ");
        queryStr.append(isShipmentFlag ? "  AND cc.applicable_to_all_shipments=TRUE " : "");
        queryStr.append(" AND (cc.non_neg_rated_changes = TRUE OR cc.non_neg_unrated_changes = TRUE)  ");
        if (!"".equalsIgnoreCase(toEmail)) {
            queryStr.append(" HAVING toEmail <> '").append(toEmail).append("'");
        }
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setParameterList("acctNoList", acctNoList);
        query.setResultTransformer(Transformers.aliasToBean(NotificationModel.class));
        query.addScalar("toName", StringType.INSTANCE);
        query.addScalar("companyName", StringType.INSTANCE);
        query.addScalar("toEmail", StringType.INSTANCE);
        query.addScalar("nonRated", BooleanType.INSTANCE);
        query.addScalar("nonUnRated", BooleanType.INSTANCE);
        return query.list();
    }

    /*
     * Get CodeJ Contact List by Booking Contacts
     * @Param-1 fileId
     * @Param-2 fileStatus will be posting,manifest,cob,changes
     */
    public NotificationModel getCodeJBkgContacts(Long fileId, String fileStatus) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT t.* FROM(  ");
        queryStr.append(appendBkgQuery("ship_acct_no", "ship_contact_id", fileStatus, false));
        queryStr.append(" UNION  ");
        queryStr.append(appendBkgQuery("client_acct_no", "client_contact_id", fileStatus, false));
        queryStr.append(" UNION  ");
        queryStr.append(appendBkgQuery("fwd_acct_no", "fwd_contact_id", fileStatus, false));
        queryStr.append(" UNION  ");
        queryStr.append(appendBkgQuery("cons_acct_no", "cons_contact_id", fileStatus, true));
        queryStr.append(" ) AS t  ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setLong("fileId", fileId);
        query.setResultTransformer(Transformers.aliasToBean(NotificationModel.class));
        query.addScalar("toName", StringType.INSTANCE);
        query.addScalar("companyName", StringType.INSTANCE);
        query.addScalar("toEmail", StringType.INSTANCE);
        query.addScalar("nonRated", BooleanType.INSTANCE);
        query.addScalar("nonUnRated", BooleanType.INSTANCE);
        if ("COB".equalsIgnoreCase(fileStatus)) {
            query.addScalar("cob", BooleanType.INSTANCE);
        }
        return (NotificationModel) query.setMaxResults(1).uniqueResult();
    }

    private String appendBkgQuery(String columnName1, String columnName2, String fileStatus, boolean consigneeFlag) {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" (SELECT lc.contact_name AS toName,lc.company_name AS companyName, ");
        queryStr.append("  CASE g.code WHEN 'E' THEN TRIM(LOWER(cc.email)) WHEN 'F' THEN TRIM(cc.`fax`) ELSE NULL END AS toEmail  ");
        if (!consigneeFlag) {
            if ("Posting".equalsIgnoreCase(fileStatus)) {
                queryStr.append(" ,cc.non_neg_rated_posting as nonRated,cc.non_neg_unrated_posting as nonUnRated  ");
            } else if ("Manifest".equalsIgnoreCase(fileStatus)) {
                queryStr.append(" ,cc.non_neg_rated_manifest as nonRated,cc.non_neg_unrated_manifest as nonUnRated  ");
            } else if ("COB".equalsIgnoreCase(fileStatus)) {
                queryStr.append(" ,cc.non_neg_rated_cob as nonRated,cc.non_neg_unrated_cob as nonUnRated  ");
                queryStr.append(" ,confirm_on_board_cob as cob ");//,cc.freight_invoice_cob as freightInvoice
            } else if ("Changes".equalsIgnoreCase(fileStatus)) {
                queryStr.append(" ,cc.non_neg_rated_changes as nonRated,cc.non_neg_unrated_changes as nonUnRated  ");
            }
        } else {
            if ("Posting".equalsIgnoreCase(fileStatus)) {
                queryStr.append(" ,cc.non_neg_rated_posting as nonRated,cc.non_neg_unrated_posting as nonUnRated  ");
            } else if ("Manifest".equalsIgnoreCase(fileStatus)) {
                queryStr.append(" ,cc.non_neg_rated_manifest as nonRated,cc.non_neg_unrated_manifest as nonUnRated  ");
            } else if ("COB".equalsIgnoreCase(fileStatus)) {
                queryStr.append(" ,cc.non_neg_rated_cob as nonRated,cc.non_neg_unrated_cob as nonUnRated  ");
                queryStr.append(" ,confirm_on_board_cob as cob ");//,cc.freight_invoice_cob as freightInvoice
            } else if ("Changes".equalsIgnoreCase(fileStatus)) {
                queryStr.append(" ,cc.non_neg_rated_changes as nonRated,cc.non_neg_unrated_changes as nonUnRated  ");
            }
        }
        queryStr.append(" FROM lcl_booking lb ");
        queryStr.append(" JOIN lcl_contact lc on (lc.id = lb.").append(columnName2);
        queryStr.append(" ) JOIN cust_contact cc ON ( cc.acct_no = lb.").append(columnName1);
        queryStr.append("  AND (CONCAT_WS(' ',cc.`first_name`,cc.`last_name`) = lc.`contact_name`))  ");
        queryStr.append(" JOIN genericcode_dup g on (g.id = cc.code_j and g.`code` IN ('E', 'F'))  ");
        queryStr.append(" WHERE lb.file_number_id = :fileId ");
        if ("Posting".equalsIgnoreCase(fileStatus)) {
            queryStr.append(" AND ( cc.non_neg_rated_posting=TRUE OR cc.non_neg_unrated_posting=TRUE )  ");
        } else if ("Manifest".equalsIgnoreCase(fileStatus)) {
            queryStr.append(" AND ( cc.non_neg_rated_manifest=TRUE OR cc.non_neg_unrated_manifest=true");
            queryStr.append("  ) ");//OR cc.freight_invoice_manifest=TRUE
        } else if ("COB".equalsIgnoreCase(fileStatus)) {
            queryStr.append(" AND ( cc.non_neg_rated_cob=TRUE OR cc.non_neg_unrated_cob=TRUE ");
            queryStr.append("  OR cc.confirm_on_board_cob=TRUE)  ");//OR cc.freight_invoice_manifest=TRUE
        } else if ("Changes".equalsIgnoreCase(fileStatus)) {
            queryStr.append(" AND ( cc.non_neg_rated_changes=TRUE OR cc.non_neg_unrated_changes=TRUE ) ");
        }
        queryStr.append(" )    ");
        return queryStr.toString();
    }

    public NotificationModel getBkgNewContact(Long fileId, String fileStatus) throws Exception {
        StringBuilder queryStr = new StringBuilder();

        queryStr.append(" SELECT t.* ");
        queryStr.append("post".equalsIgnoreCase(fileStatus) ? ",false as nonRated,true as nonUnRated" : "");
        queryStr.append("cob".equalsIgnoreCase(fileStatus) ? ",false as nonRated,false as nonUnRated,false as freightInvoice,true as cob" : "");
        queryStr.append("  FROM(  ");
        queryStr.append(appendBkgNewQuery("ship_contact_id", "ship_acct_no"));
        queryStr.append(" UNION  ");
        queryStr.append(appendBkgNewQuery("client_contact_id", "client_acct_no"));
        queryStr.append(" UNION  ");
        queryStr.append(appendBkgNewQuery("fwd_contact_id", "fwd_acct_no"));
        queryStr.append(" UNION  ");
        queryStr.append(appendBkgNewQuery("cons_contact_id", "cons_acct_no"));
        queryStr.append(" ) AS t  WHERE t.toEmail <> '' ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setParameter("fileId", fileId);
        query.setResultTransformer(Transformers.aliasToBean(NotificationModel.class));
        query.addScalar("toName", StringType.INSTANCE);
        query.addScalar("companyName", StringType.INSTANCE);
        query.addScalar("toEmail", StringType.INSTANCE);
        if (fileStatus.equalsIgnoreCase("post")) {
            query.addScalar("nonRated", BooleanType.INSTANCE);
            query.addScalar("nonUnRated", BooleanType.INSTANCE);
        } else if (fileStatus.equalsIgnoreCase("cob")) {
            query.addScalar("nonRated", BooleanType.INSTANCE);
            query.addScalar("nonUnRated", BooleanType.INSTANCE);
            query.addScalar("freightInvoice", BooleanType.INSTANCE);
            query.addScalar("cob", BooleanType.INSTANCE);
        }
        return (NotificationModel) query.setMaxResults(1).uniqueResult();
    }

    private String appendBkgNewQuery(String columnName, String columnName1) {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" (SELECT ");
        queryStr.append(" IF(CONCAT_WS(' ',cc.`first_name`,cc.`last_name`) =lc.contact_name,'',lc.contact_name) AS toName, ");
        queryStr.append(" IF(CONCAT_WS(' ',cc.`first_name`,cc.`last_name`) =lc.contact_name,'',lc.company_name) AS companyName, ");
        queryStr.append(" IF(CONCAT_WS(' ',cc.`first_name`,cc.`last_name`) =lc.`contact_name`,'',lc.`email1`) AS toEmail ");
        queryStr.append(" FROM lcl_booking lb ");
        queryStr.append("  JOIN lcl_contact lc ON (lc.id = lb.").append(columnName).append(" AND lc.contact_name <> '' )");
        queryStr.append("  LEFT JOIN cust_contact cc ON cc.acct_no=lb.").append(columnName1);
        queryStr.append(" AND (CONCAT_WS(' ',cc.`first_name`,cc.`last_name`) = lc.`contact_name`) ");
        queryStr.append(" WHERE lb.file_number_id =:fileId");
        queryStr.append(" ) ");
        return queryStr.toString();
    }

    public List<NotificationModel> getCodeJFreightContactList(Long fileId, String columnName, String columnName1,
            Boolean isShipmentFlag) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append("  SELECT  t.* FROM(  ");
        queryStr.append(" SELECT CASE g.code WHEN 'E' THEN TRIM(LOWER(cc.email)) WHEN 'F' THEN TRIM(LOWER(cc.fax))  ");
        queryStr.append("  ELSE NULL END AS toEmail, ");
        queryStr.append(" concat(cc.first_name,' ',cc.last_name) AS toName,");
        queryStr.append(" (select acct_name from trading_partner where acct_no=cc.acct_no) AS companyName ");
        queryStr.append(" FROM cust_contact cc ");
        queryStr.append(" join lcl_bl bl on (bl.").append(columnName).append("=cc.acct_no) ");
        queryStr.append(" JOIN genericcode_dup g ON(g.id = cc.code_k AND g.`code` IN ('E','F'))");
        queryStr.append(" WHERE bl.file_number_id=:fileId  AND cc.lcl_exports = TRUE  ");
        queryStr.append(" AND cc.applicable_to_all_shipments_codeK = TRUE) AS t WHERE t.toEmail <> ''  ");
        queryStr.append(" UNION (");
        queryStr.append(appendBLContactCodeK(columnName, columnName1, fileId));
        queryStr.append(" )");
        //queryStr.append(" AND ").append(isShipmentFlag ? "cc.freight_invoice_cob" : "cc.freight_invoice_manifest");
        //   queryStr.append("= TRUE and cc.applicable_to_all_shipments = true) AS t  ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setParameter("fileId", fileId);
        query.setResultTransformer(Transformers.aliasToBean(NotificationModel.class));
        query.addScalar("toName", StringType.INSTANCE);
        query.addScalar("companyName", StringType.INSTANCE);
        query.addScalar("toEmail", StringType.INSTANCE);
        return query.list();
    }

    public String appendBLContactCodeK(String columnName, String columnName1, Long fileId) {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append("  SELECT  t.* FROM ( ");
        queryStr.append("  SELECT CASE gd.code WHEN 'E' THEN TRIM(LOWER(cc.email))  ");
        queryStr.append("  WHEN 'F' THEN TRIM(cc.`fax`) ELSE NULL END AS toEmail, ");
        queryStr.append("  CONCAT(cc.first_name, ' ', cc.last_name) AS toName, ");
        queryStr.append("  (SELECT acct_name FROM  trading_partner  ");
        queryStr.append("  WHERE acct_no = cc.acct_no) AS companyName ");
        queryStr.append("  FROM lcl_booking lb JOIN lcl_contact lc ON lb.").append(columnName1).append("=lc.id  ");
        queryStr.append("  JOIN cust_contact cc ON (( lb.").append(columnName).append("=cc.acct_no ");
        queryStr.append("  AND CONCAT(cc.first_name, ' ', cc.last_name) = lc.contact_name)  ");
        queryStr.append("  AND cc.only_when_booking_contact_codeK = TRUE) ");
        queryStr.append("  JOIN genericcode_dup gd  ON (gd.id = cc.code_k  AND gd.`code` IN ('E', 'F')) ");
        queryStr.append("  WHERE lb.file_number_id =:fileId  AND cc.lcl_exports = TRUE AND ");
        queryStr.append("  lb.").append(columnName).append("= (SELECT lbl.").append(columnName).append(" FROM lcl_bl lbl WHERE lbl.file_number_id =:fileId) ");
        queryStr.append("  ) AS t WHERE t.toEmail <> '' ");
        return queryStr.toString();
    }

    public NotificationModel getBkgNewContactByFreight(Long fileId) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT t.* FROM(  ");
        queryStr.append(appendBkgNewQueryFreight("ship_contact_id", "ship_acct_no"));
        queryStr.append(" UNION  ");
        queryStr.append(appendBkgNewQueryFreight("client_contact_id", "client_acct_no"));
        queryStr.append(" UNION  ");
        queryStr.append(appendBkgNewQueryFreight("fwd_contact_id", "fwd_acct_no"));
//        queryStr.append(" UNION  ");
//        queryStr.append(appendBkgNewQueryFreight("cons_contact_id", "cons_acct_no"));
        queryStr.append(" ) AS t  WHERE t.toEmail <> '' ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setParameter("fileId", fileId);
        query.setResultTransformer(Transformers.aliasToBean(NotificationModel.class));
        query.addScalar("toName", StringType.INSTANCE);
        query.addScalar("companyName", StringType.INSTANCE);
        query.addScalar("toEmail", StringType.INSTANCE);
        return (NotificationModel) query.uniqueResult();
    }

    private String appendBkgNewQueryFreight(String columnName, String columnName1) {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" (SELECT lc.contact_name AS toName,lc.company_name AS companyName,");
        queryStr.append(" lc.email1 AS toEmail ");
        queryStr.append(" FROM lcl_booking lb ");
        queryStr.append("  JOIN lcl_contact lc ON (lc.id = lb.").append(columnName).append(" AND lc.contact_name <> '' )");
        queryStr.append(" WHERE lb.file_number_id =:fileId and lb.").append(columnName1).append("<> '' ");
        queryStr.append(" ) ");
        return queryStr.toString();
    }

    public String getFromAddress(Long fileId) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT lower(r.email) AS fromEmail ");
        queryStr.append(" FROM (SELECT u.un_loc_code AS podUnlocCode,b.billing_terminal AS billTm ");
        queryStr.append(" FROM lcl_booking b LEFT JOIN lcl_booking_import bm ");
        queryStr.append(" ON bm.`file_number_id` = b.`file_number_id` JOIN un_location u ");
        queryStr.append(" ON if(b.booking_type <> 'T' , u.id = b.pod_id ,u.id = bm.`foreign_port_of_discharge_id`) ");
        queryStr.append(" WHERE b.`file_number_id` =:fileId) f  ");
        queryStr.append(" JOIN ports p ON p.unlocationcode = f.podUnlocCode JOIN ");
        queryStr.append(" retadd r ON p.`govschnum` = r.destin AND r.code = 'L' AND r.blterm = f.billTm ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setParameter("fileId", fileId);
        query.addScalar("fromEmail", StringType.INSTANCE);
        String fromEmailAddress = (String) query.setMaxResults(1).uniqueResult();
        if (CommonUtils.isEmpty(fromEmailAddress)) {
            queryStr = new StringBuilder();
            queryStr.append(" SELECT lower(r.email) AS fromEmail ");
            queryStr.append(" FROM (SELECT u.un_loc_code AS fdUnlocCode,b.billing_terminal AS billTm ");
            queryStr.append(" FROM lcl_booking b JOIN un_location u  ");
            queryStr.append(" ON u.id = b.fd_id WHERE b.`file_number_id` =:fileId) f  ");
            queryStr.append(" JOIN ports p ON p.unlocationcode = f.fdUnlocCode JOIN ");
            queryStr.append(" retadd r ON p.`govschnum` = r.destin AND r.code = 'L' AND r.blterm = f.billTm ");
            query = getCurrentSession().createSQLQuery(queryStr.toString());
            query.setParameter("fileId", fileId);
            query.addScalar("fromEmail", StringType.INSTANCE);
            fromEmailAddress = (String) query.setMaxResults(1).uniqueResult();
        }
        if (CommonUtils.isEmpty(fromEmailAddress)) {
            String queryS = "SELECT lower(email) as fromEmail FROM retadd rd WHERE rd.code='L' "
                    + "AND rd.blterm=(SELECT b.billing_terminal FROM lcl_booking b WHERE b.file_number_id =:fileId) AND rd.destin='00000'";
            SQLQuery querys = getCurrentSession().createSQLQuery(queryS);
            querys.setParameter("fileId", fileId);
            querys.addScalar("fromEmail", StringType.INSTANCE);
            fromEmailAddress = (String) querys.setMaxResults(1).uniqueResult();
        }
        return fromEmailAddress;
    }

    public void setEmailDetails(String bussinessUnit, NotificationModel notificationModel) throws Exception {
        String companyLogo = LoadLogisoftProperties.getProperty("ECU".equalsIgnoreCase(bussinessUnit) ? "application.image.logo"
                : "application.image.econo.logo");
        notificationModel.setCompanyLogoPath(companyLogo);
        String companyWebsite = LoadLogisoftProperties.getProperty(bussinessUnit.equalsIgnoreCase("ECU") ? "application.ECU.website"
                : bussinessUnit.equalsIgnoreCase("OTI") ? "application.OTI.website" : "application.Econo.website");
        notificationModel.setCompanyWebsiteName(companyWebsite);
    }

    public String emailBodyAppend(String toName, String toCompanyName, String contextPath,
            String companyLogo, String companyWebsite) throws Exception {
        StringBuilder emailBody = new StringBuilder();
        emailBody.append("<HTML><BODY>");
        emailBody.append("<div style='font-family: sans-serif;'>");
        emailBody.append("<b>Please DO NOT reply to this message, see note 3 below.<b><br>");
        emailBody.append("<a href='http://").append(companyWebsite).append("' target='_blank'><img src='").append(contextPath).append(companyLogo).append("'></a>");
        emailBody.append("<br>");
        emailBody.append("<b>To Name:</b>").append(null != toName ? toName : "").append(" <br>");
        emailBody.append("<b>To Company:</b>").append(null != toCompanyName ? toCompanyName : "");
        emailBody.append("<br>");
        emailBody.append("<br>");
        emailBody.append("<b>From Name:</b> <br>");
        emailBody.append("<b>From Fax #:</b> <br>");
        emailBody.append("<b>From Phone #:</b> <p></p>");
        emailBody.append("<p></p>");
        emailBody.append("<br>");
        emailBody.append("<br>");
        emailBody.append("<br>");
        emailBody.append("<b>Did you know?</b><br>");
        emailBody.append("NEED LCL TRANS-ATLANTIC/PACIFIC SERVICES?  WE CAN ASSIST WITH YOUR IMPORT AND<br>");
        emailBody.append("EXPORT NEEDS TO AND FROM ASIA, EUROPE, THE MED, MIDDLE EAST AND AFRICA.<br>");
        emailBody.append("<br>");
        emailBody.append("CALL  1-866-326-6648 OR BOOK ON LINE AT <a  href='http://");
        emailBody.append(companyWebsite).append("' target='_blank'>").append(companyWebsite).append("</a><br>");
        emailBody.append("<p></p>");
        emailBody.append("<b>Helpful Information:</b><br>");
        emailBody.append("1. Open the attached PDF image with Adobe Acrobat Reader. This software can<br>");
        emailBody.append("be downloaded for free, just visit <a href='http://");
        emailBody.append(companyWebsite).append("' target='_blank'>").append(companyWebsite).append("</a>.<br>");
        emailBody.append("2. The attached image may contain multiple pages.<br>");
        emailBody.append("3. Please do not reply to this email, it is sent from an automated<br>");
        emailBody.append("system, there will be no response from this address. For assistance contact<br>");
        emailBody.append("your sales representative or your local office at (866) 326-6648.<br>");
        emailBody.append("</b></b>");
        emailBody.append("</div>");
        emailBody.append("</BODY>");
        emailBody.append("</HTML>");
        return emailBody.toString();

    }

    public String getBkgContact(Long fileId) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT t.emailFaxList FROM(  ");
        queryStr.append(appendBkgContactQuery("ship_contact_id", "ship_acct_no"));
        queryStr.append(" UNION  ");
        queryStr.append(appendBkgContactQuery("client_contact_id", "client_acct_no"));
        queryStr.append(" UNION  ");
        queryStr.append(appendBkgContactQuery("fwd_contact_id", "fwd_acct_no"));
        queryStr.append(" UNION  ");
        queryStr.append(appendBkgContactQuery("cons_contact_id", "cons_acct_no"));
        queryStr.append(" ) AS t  WHERE t.emailFaxList <> '' ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setParameter("fileId", fileId);
        query.addScalar("emailFaxList", StringType.INSTANCE);
        return (String) query.uniqueResult();
    }

    private String appendBkgContactQuery(String columnName, String columnName1) {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" (SELECT ");
        queryStr.append(" lc.`email1` AS emailFaxList ");
        queryStr.append(" FROM lcl_booking lb ");
        queryStr.append("  JOIN lcl_contact lc ON (lc.id = lb.").append(columnName).append(" AND lc.contact_name <> '' )");
        queryStr.append(" WHERE lb.file_number_id =:fileId and ").append(columnName1).append("<> '' ) ");
        return queryStr.toString();
    }

    public String getBlAcctNo(Long fileId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("  SELECT CONCAT_WS(',',IF(lb.`agent_acct_no`<> '',lb.`agent_acct_no`,NULL),  ");
        queryBuilder.append("  IF(lb.`ship_acct_no`<> '',lb.`ship_acct_no`,NULL),  ");
        queryBuilder.append("  IF(lb.`cons_acct_no`<> '',lb.`cons_acct_no`,NULL),  ");
        queryBuilder.append("  IF(lb.`noty_acct_no`<> '',lb.`noty_acct_no`,NULL),  ");
        queryBuilder.append("  IF(lb.`third_party_acct_no`<> '',lb.`third_party_acct_no`,NULL),  ");
        queryBuilder.append("  IF(lb.`fwd_acct_no`<> '',lb.`fwd_acct_no`,NULL)) AS acctNo  ");
        queryBuilder.append("  FROM lcl_bl lb WHERE lb.file_number_id=:fileId  ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setLong("fileId", fileId);
        query.addScalar("acctNo", StringType.INSTANCE);
        return (String) query.setMaxResults(1).uniqueResult();
    }

    public String getCodeIList(List<String> acctNoList) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT GROUP_CONCAT(DISTINCT e.toEmail) FROM(  ");
        queryStr.append(" SELECT CASE g.code WHEN 'E1' THEN TRIM(LOWER(cc.email))  ");
        queryStr.append(" WHEN 'F1' THEN TRIM(cc.`fax`) ELSE NULL END AS toEmail ");
        queryStr.append(" FROM cust_contact cc ");
        queryStr.append(" JOIN genericcode_dup g ON(g.id = cc.code_i AND g.`code` IN ('E1', 'F1') ) ");
        queryStr.append(" WHERE cc.acct_no IN(:acctNoList)) as e ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setParameterList("acctNoList", acctNoList);
        return (String) query.uniqueResult();
    }

    public String getTerminalEmail(Long fileId) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" select exports_billing_terminal_email as email FROM terminal t  ");
        queryStr.append(" join lcl_booking b on b.billing_terminal=t.trmnum where b.file_number_id=:fileId ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setLong("fileId", fileId);
        query.addScalar("email", StringType.INSTANCE);
        return (String) query.setMaxResults(1).uniqueResult();
    }

    public String getEmailContentBody(HttpServletRequest req, String bussinessUnit, String comment) throws Exception {
        String imagePath = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath();
        String companyLogo = imagePath + LoadLogisoftProperties.getProperty("ECU".equalsIgnoreCase(bussinessUnit) ? "application.image.logo"
                : "application.image.econo.logo");
        String companyWebsite = LoadLogisoftProperties.getProperty(bussinessUnit.equalsIgnoreCase("ECU") ? "application.ECU.website"
                : bussinessUnit.equalsIgnoreCase("OTI") ? "application.OTI.website" : "application.Econo.website");
        StringBuilder emailBody = new StringBuilder();
        emailBody.append("<HTML><BODY>");
        emailBody.append("<table style='font-family: sans-serif;font-weight:bold;font-size: 12px;'><tr><td>");
        emailBody.append("Please DO NOT reply to this message, see note 3 below.</td></tr>");
        emailBody.append("<tr><td>");
        emailBody.append("<a href='http://").append(companyWebsite).append("' target='_blank'><img src='");
        emailBody.append(companyLogo).append("'></a></td></tr>");
        emailBody.append("<tr><td><font color='red'>Important Comments:<br/>");
        emailBody.append(comment.toUpperCase()).append("</font>");
        emailBody.append("</td></tr>");
        emailBody.append("<tr><td>");
        emailBody.append("Did you know?<br>");
        emailBody.append("NEED LCL TRANS-ATLANTIC/PACIFIC SERVICES?  WE CAN ASSIST WITH YOUR IMPORT AND<br>");
        emailBody.append("EXPORT NEEDS TO AND FROM ASIA, EUROPE, THE MED, MIDDLE EAST AND AFRICA.<br>");
        emailBody.append("<br/>");
        emailBody.append("CALL  1-866-326-6648 OR BOOK ON LINE AT <a  href='http://");
        emailBody.append(companyWebsite).append("' target='_blank'>").append(companyWebsite).append("</a><br/>");
        emailBody.append("Helpful Information:<br>");
        emailBody.append("1. Open the attached PDF image with Adobe Acrobat Reader. This software can<br>");
        emailBody.append("be downloaded for free, just visit <a href='http://");
        emailBody.append(companyWebsite).append("' target='_blank'>").append(companyWebsite).append("</a>.<br>");
        emailBody.append("2. The attached image may contain multiple pages.<br>");
        emailBody.append("3. Please do not reply to this email, it is sent from an automated<br>");
        emailBody.append("system, there will be no response from this address. For assistance contact<br>");
        emailBody.append("your sales representative or your local office at (866) 326-6648.<br>");
        emailBody.append("</td></tr>");
        emailBody.append("</table></BODY></HTML>");
        return emailBody.toString();
    }

    public List<ManifestBean> getPickedDrList(Long unitSSId) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT DISTINCT fn.fileId as fileId,fn.fileNo as fileNo,fn.state as fileState,fn.buss as businessUnit  FROM  ");
        queryStr.append(" (SELECT DISTINCT bkp.file_number_id AS fileId,lfn.file_number AS fileNo, ");
        queryStr.append(" lfn.state AS state,lfn.business_unit as buss FROM lcl_booking_piece_unit bpu JOIN lcl_booking_piece bkp ON  bpu.booking_piece_id = bkp.id ");
        queryStr.append(" JOIN lcl_file_number lfn ON lfn.id=bkp.file_number_id  JOIN lcl_booking lb ON lb.file_number_id=lfn.id  ");
        queryStr.append(" WHERE bpu.lcl_unit_ss_id =:unitSSId) fn ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setLong("unitSSId", unitSSId);
        query.setResultTransformer(Transformers.aliasToBean(ManifestBean.class));
        query.addScalar("fileId", LongType.INSTANCE);
        query.addScalar("fileNo", StringType.INSTANCE);
        query.addScalar("fileState", StringType.INSTANCE);
        query.addScalar("businessUnit", StringType.INSTANCE);
        return query.list();
    }

    public Boolean isNotificationStatus(Long fileId, String fileStatus, String status) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT IF(COUNT(*)>0,TRUE,FALSE) as result FROM lcl_export_notification  ");
        queryStr.append(" WHERE file_number_id=:fileId AND file_status=:fileStatus AND status=:status ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setLong("fileId", fileId);
        query.setString("fileStatus", fileStatus);
        query.setString("status", status);
        query.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) query.uniqueResult();
    }

    public boolean getSystemContact(String acctNo, String contactName) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT if(count(*) > 0,true,false) as result FROM cust_contact cc WHERE cc.acct_no =:acctNo ");
        queryStr.append(" AND CONCAT_WS(' ',cc.`first_name`,cc.`last_name`) =:accountName and cc.code_j <> '' ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setParameter("acctNo", acctNo);
        query.setParameter("accountName", contactName);
        return (boolean) query.addScalar("result",BooleanType.INSTANCE).uniqueResult();
    }

    public NotificationModel getBookingContact(Long fileId) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT ac.acctNo as accountNo,ac.c_contact_name as toName, LOWER(ac.c_email) as toEmail FROM ( ");
        queryStr.append(" SELECT bk.`client_acct_no` AS acctNo , lc.email1 AS c_email, ");
        queryStr.append(" lc.`contact_name` AS c_contact_name ");
        queryStr.append(" FROM lcl_booking bk JOIN lcl_file_number lf ON lf.`id` = bk.`file_number_id` ");
        queryStr.append(" JOIN lcl_contact lc ON (lc.id = bk.`client_contact_id` AND lc.`contact_name` <> '')  ");
        queryStr.append(" WHERE lf.id =:fileId ");
        queryStr.append(" UNION ALL ");
        queryStr.append(" SELECT bk.`ship_acct_no` AS acctNo , lc.email1 AS c_email ,");
        queryStr.append(" lc.`contact_name` AS c_contact_name ");
        queryStr.append(" FROM lcl_booking bk JOIN lcl_file_number lf ON lf.`id` = bk.`file_number_id`");
        queryStr.append(" JOIN lcl_contact lc ON (lc.id = bk.`ship_contact_id` AND lc.`contact_name` <> '')  ");
        queryStr.append(" WHERE lf.id =:fileId ");
        queryStr.append(" UNION ALL");
        queryStr.append(" SELECT bk.`fwd_acct_no` AS acctNo , lc.email1 AS c_email ,");
        queryStr.append(" lc.`contact_name` AS c_contact_name");
        queryStr.append(" FROM lcl_booking bk JOIN lcl_file_number lf ON lf.`id` = bk.`file_number_id`");
        queryStr.append(" JOIN lcl_contact lc ON (lc.id = bk.`fwd_contact_id` AND lc.`contact_name` <> '')  ");
        queryStr.append(" WHERE lf.id =:fileId ");
        queryStr.append(" UNION ALL");
        queryStr.append(" SELECT bk.`cons_acct_no` AS acctNo , lc.email1 AS c_email,");
        queryStr.append(" lc.`contact_name` AS c_contact_name");
        queryStr.append(" FROM lcl_booking bk JOIN lcl_file_number lf ON lf.`id` = bk.`file_number_id`");
        queryStr.append(" JOIN lcl_contact lc ON (lc.id = bk.`cons_contact_id` AND lc.`contact_name` <> '') ");
        queryStr.append(" WHERE lf.id =:fileId   ");
        queryStr.append(" ) AS ac ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setParameter("fileId", fileId);
        query.setResultTransformer(Transformers.aliasToBean(NotificationModel.class));
        query.addScalar("accountNo", StringType.INSTANCE);
        query.addScalar("toName", StringType.INSTANCE);
        query.addScalar("toEmail", StringType.INSTANCE);
        return (NotificationModel) query.setMaxResults(1).uniqueResult();
    }
}
