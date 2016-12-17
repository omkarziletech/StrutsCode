/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.LclRemarks;
import com.gp.cong.struts.LoadLogisoftProperties;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BooleanType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;

/**
 *
 * @author Administrator
 */
public class LclRemarksDAO extends BaseHibernateDAO<LclRemarks> implements LclCommonConstant {

    public LclRemarksDAO() {
        super(LclRemarks.class);
    }

    public LclRemarks validateQuoteRemarks(Long fileId) throws Exception {
        LclRemarks bookingRemarks = this.executeUniqueQuery("from LclRemarks where lclFileNumber.id= " + fileId
                + " AND type in('auto','QT-AutoNotes') AND (remarks='Quote Created'  or remarks like 'Quote Created - Copy from QT#%')");
        return bookingRemarks;
    }

    public LclRemarks validateBookingRemarks(Long fileId) throws Exception {
        LclRemarks bookingRemarks = this.executeUniqueQuery("from LclRemarks where lclFileNumber.id= " + fileId
                + " AND type in('auto','DR-AutoNotes')  AND (remarks='Booking Created' or remarks='Quick DR Created'  "
                + " or remarks='EDI: D/R created by Eculine EDI' or remarks='Quote is converted to Booking' or remarks like 'Booking Created - Copy from DR#%')");
        return bookingRemarks;
    }

    public List<LclRemarks> getAllRemarksByType(String fileId, String[] type) throws Exception {
        Criteria criteria = getSession().createCriteria(LclRemarks.class, "lclRemarks");
        criteria.createAlias("lclRemarks.lclFileNumber", "lclFileNumber");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", new Long(fileId)));
        }
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.in("lclRemarks.type", type));
        }
        criteria.addOrder(Order.desc("enteredDatetime"));
        return criteria.list();
    }

    public List<LclRemarks> getAllRemarksOrderById(Long fileId, String type) throws Exception {
        Criteria criteria = getSession().createCriteria(LclRemarks.class, "lclRemarks");
        criteria.createAlias("lclRemarks.lclFileNumber", "lclFileNumber");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclRemarks.type", type));
        }
        criteria.addOrder(Order.desc("id"));
        return criteria.list();
    }

    public LclRemarks getLclRemarksByType(String fileId, String type1) throws Exception {
        Criteria criteria = getSession().createCriteria(LclRemarks.class, "lclRemarks");
        criteria.createAlias("lclRemarks.lclFileNumber", "lclFileNumber");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", Long.parseLong(fileId)));
        }
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclRemarks.type", type1));
        }
        criteria.setMaxResults(1);
        return (LclRemarks) criteria.uniqueResult();
    }

    public String getLclRemarksByTypeSQL(String fileId, String type) throws Exception {
        String remarks = new String();
        getCurrentSession().clear();
        String SQL_QUERY = "SELECT remarks FROM lcl_remarks WHERE file_number_id = ?0 AND TYPE=?1";
        Query query = getCurrentSession().createSQLQuery(SQL_QUERY);
        query.setParameter("0", fileId);
        query.setParameter("1", type);
        Object remarksObj = query.setMaxResults(1).uniqueResult();
        if (remarksObj != null && !remarksObj.toString().trim().equals("")) {
            remarks = remarksObj.toString();
        }
        return remarks;
    }

    public LclRemarks getExternalComments(Long fileId) throws Exception {
        Criteria criteria = getSession().createCriteria(LclRemarks.class, "lclRemarks");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        criteria.add(Restrictions.like("lclRemarks.remarks", "%External Comments%"));
        criteria.addOrder(Order.desc("id"));
        criteria.setMaxResults(1);
        return (LclRemarks) criteria.uniqueResult();
    }

    public String getCargoReceivedDate(String fileId) throws Exception {
        String SQL_QUERY = "SELECT entered_datetime FROM lcl_remarks  WHERE file_number_id =?0 AND TYPE IN('T','auto','DR-AutoNotes') AND "
                + "(remarks LIKE 'Cargo Released for Export' OR  remarks LIKE 'Cargo Received (Veri%'"
                + " OR remarks LIKE 'Cargo Un-Released for Export' OR remarks LIKE 'Cargo Received (Not%'  "
                + "  OR remarks LIKE 'Inventory Status->WAREHOUSE%') ORDER BY entered_datetime DESC";
        Query query = getCurrentSession().createSQLQuery(SQL_QUERY);
        Object entereddate = (Date) query.setParameter("0", fileId).setMaxResults(1).uniqueResult();
        return entereddate != null ? entereddate.toString() : null;
    }

    public boolean checkCustomerNotes(String acctNo) throws Exception {
        String eciNoFf = null, eciNoCn = null;
        LclRemarksDAO lcl = new LclRemarksDAO();
        List eciAcct = lcl.getEciAccnNo(acctNo);
        if (CommonUtils.isNotEmpty(eciAcct)) {
            Object[] eciNo = (Object[]) eciAcct.get(0);
            if (eciNo[0] != null && !eciNo[0].toString().trim().equals("")) {
                eciNoFf = eciNo[0].toString();
            }
            if (eciNo[1] != null && !eciNo[1].toString().trim().equals("")) {
                eciNoCn = eciNo[1].toString();
            }
        }
        if (null != eciNoFf || null != eciNoCn) {
            List<String> notesList = lcl.getNotesCount(eciNoFf, eciNoCn);
            if (null != notesList && !notesList.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public List getEciAccnNo(String accNo) throws Exception {
        String sqlquery = "select ECI_ACCT_NO,ECIFWNO from trading_partner where acct_no='" + accNo + "'";
        Query query = getCurrentSession().createSQLQuery(sqlquery);
        List list = query.list();
        return list;
    }
    private String databaseSchema, specialNotes;

    public List<String> getNotesCount(String accNoEciFf, String accNoEciCn) throws Exception {
        databaseSchema = LoadLogisoftProperties.getProperty("elite.database.name");
        String sqlquery = "SELECT COUNT(*) FROM " + databaseSchema + "";
        if (null != accNoEciFf) {
            sqlquery += ".ffnote WHERE actnum='" + accNoEciFf + "'";
        } else if (null != accNoEciCn) {
            sqlquery += ".cnnote WHERE actnum='" + accNoEciCn + "'";
        }
        Query query = getCurrentSession().createSQLQuery(sqlquery);
        return query.list();
    }

    public List<String> getNoteSymbolVoid(String accNoEciFf, String accNoEciCn) throws Exception {
        databaseSchema = LoadLogisoftProperties.getProperty("elite.database.name");
        StringBuilder sb = new StringBuilder();
        sb.append("(SELECT DISTINCT ntvoid from ").append(databaseSchema).append(".ffnote WHERE actnum='");
        sb.append(accNoEciFf).append("' AND ntvoid='V' )").append(" UNION ");
        sb.append("(SELECT DISTINCT ntvoid from ").append(databaseSchema).append(".ffnote WHERE actnum='");
        sb.append(accNoEciCn).append("' AND ntvoid='V' )");
        sb.append(" UNION ");
        sb.append(" (SELECT ");
        sb.append("   tp.note_type");
        sb.append(" FROM");
        sb.append("  `tp_note` tp ");
        sb.append("WHERE (tp.`actnum` = '").append(accNoEciFf).append("'");
        sb.append("  OR tp.`connum` = '").append(accNoEciCn).append("'");
        sb.append(") AND  tp.note_type='V')");
        Query query = getCurrentSession().createSQLQuery(sb.toString());
        return query.list();
    }

    public List<String> getNoteSymbol(String accNoEciFf, String accNoEciCn) throws Exception {
        databaseSchema = LoadLogisoftProperties.getProperty("elite.database.name");
        StringBuilder sb = new StringBuilder();
        sb.append("(SELECT DISTINCT autnot from ").append(databaseSchema).append(".ffnote WHERE actnum='").append(accNoEciFf);
        sb.append("' )").append(" UNION ");
        sb.append("(SELECT DISTINCT autnot from ").append(databaseSchema).append(".cnnote WHERE actnum='").append(accNoEciCn);
        sb.append("' )");
        sb.append(" UNION ");
        sb.append("(SELECT ");
        sb.append("  tp.note_type ");
        sb.append("FROM");
        sb.append("  tp_note tp ");
        sb.append("WHERE (");
        sb.append("    tp.actnum = '").append(accNoEciFf).append("'");
        sb.append("    OR tp.connum = '").append(accNoEciCn).append("'");
        sb.append("  ))");
        Query query = getCurrentSession().createSQLQuery(sb.toString());
        return query.list();
    }

    public List getSpecialNotesSymbl(String accNoEciFf, String accNoEciCn, String symbl) throws Exception {
        databaseSchema = LoadLogisoftProperties.getProperty("elite.database.name");
        StringBuilder sb = new StringBuilder();
        sb.append("(SELECT notlne AS remarks,TIMESTAMP(datent, timent) AS enteredDatetime,USER AS userName,autnot AS noteSymbol,");
        sb.append("ntvoid AS voidSymbol, '' AS id FROM ").append(databaseSchema).append(".ffnote WHERE actnum ='").append(accNoEciFf).append("'");
        sb.append(appendWhereCondition(symbl));
        sb.append(" UNION ");
        sb.append("(SELECT notlne AS remarks,TIMESTAMP(datent, timent) AS enteredDatetime,USER AS userName,autnot AS noteSymbol,");
        sb.append("ntvoid AS voidSymbol, '' AS id FROM ").append(databaseSchema).append(".cnnote WHERE actnum ='").append(accNoEciCn).append("'");
        sb.append(appendWhereCondition(symbl));
        sb.append(" UNION ");
        sb.append(" (SELECT ");
        sb.append("  tn.`note_desc` AS remarks,");
        sb.append("  TIMESTAMP(tn.`entered_datetime`) AS enteredDatetime,");
        sb.append("  ud.login_name AS userName,");
        sb.append("  tn.`note_type` AS noteSymbol, '',");
        sb.append("  tn.id AS id");
        sb.append(" FROM");
        sb.append("  `tp_note` tn ");
        sb.append("  JOIN `user_details` ud ");
        sb.append("    ON tn.`entered_by_user_id` = ud.user_id ");
        sb.append("WHERE (tn.`actnum` = '").append(accNoEciFf).append("'");
        sb.append("  OR tn.`connum` = '").append(accNoEciCn).append("')");
        sb.append(tpAppendWhereCondition(symbl));
        sb.append("ORDER BY enteredDatetime DESC");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setResultTransformer(Transformers.aliasToBean(LclRemarks.class));
        query.addScalar("remarks", StringType.INSTANCE);
        query.addScalar("enteredDatetime", TimestampType.INSTANCE);
        query.addScalar("userName", StringType.INSTANCE);
        query.addScalar("noteSymbol", StringType.INSTANCE);
        query.addScalar("voidSymbol", StringType.INSTANCE);
        query.addScalar("id", LongType.INSTANCE);
        List<LclRemarks> li = query.list();
        return li;
    }

    public String tpAppendWhereCondition(String symbl) {
        StringBuilder sb = new StringBuilder();
        if (!"V".equals(symbl) && null != symbl) {
            sb.append(" AND tn.`note_type`='").append(symbl).append("' AND  tn.`note_type`<>'V'");
            sb.append(" )");
        } else if (null != symbl) {
            sb.append(" AND tn.`note_type`='").append(symbl).append("'").append(sb);
            sb.append(" )");
        } else {
            sb.append(" AND  tn.note_type <> 'V')");
        }
        return sb.toString();
    }

    public String appendWhereCondition(String symbl) {
        StringBuilder sb = new StringBuilder();
        if (!"V".equals(symbl) && null != symbl) {
            sb.append("AND autnot='").append(symbl).append("' AND ntvoid<>'V'");
            sb.append(" )");
        } else if (null != symbl) {
            sb.append("AND ntvoid='").append(symbl).append("'").append(sb);
            sb.append(" )");
        } else {
            sb.append(" )");
        }
        return sb.toString();
    }

    public List getSpecialNotesProp(String accNoEciFf, String accNoEciCn) throws Exception {
        databaseSchema = LoadLogisoftProperties.getProperty("elite.database.name");
        specialNotes = LoadLogisoftProperties.getProperty("lcl.specialNotes.code");
        String sqlquery = "SELECT notlne AS remarks,TIMESTAMP(datent,timent) AS enteredDatetime,user AS userName from " + databaseSchema + "";
        if (null != accNoEciFf) {
            sqlquery += ".ffnote where actnum='" + accNoEciFf + "'";
        } else if (null != accNoEciCn) {
            sqlquery += ".cnnote where actnum='" + accNoEciCn + "'";
        }
        sqlquery += " AND autnot IN('" + specialNotes + "') ORDER BY enteredDatetime DESC";
        SQLQuery query = getCurrentSession().createSQLQuery(sqlquery);
        query.setResultTransformer(Transformers.aliasToBean(LclRemarks.class));
        query.addScalar("remarks", StringType.INSTANCE);
        query.addScalar("enteredDatetime", TimestampType.INSTANCE);
        query.addScalar("userName", StringType.INSTANCE);
        List<LclRemarks> li = query.list();
        return li;
    }

    public List<LclRemarks> getFollowUpNotes(String startDate, String endDate) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("(SELECT lr.remarks as remarks,lr.followup_datetime as followupDate,'Lcl Booking' AS userName,");
        sb.append("IF(lb.booking_type = 'I' OR lb.booking_type='T',CONCAT('IMP-',lfn.file_number),CONCAT(SUBSTRING(poo.un_loc_code, 3,5),'-',lfn.file_number)) AS noteSymbol,");
        sb.append("lr.followup_email AS followupEmail,ud.first_name AS voidSymbol,ud.email AS emailId  ");
        sb.append("FROM lcl_remarks lr JOIN lcl_file_number lfn ON lr.file_number_id=lfn.id ");
        sb.append("JOIN lcl_booking lb ON lb.file_number_id=lfn.id LEFT JOIN un_location poo ON poo.id=lb.poo_id JOIN user_details ud ON ud.user_id=lr.entered_by_user_id ");
        sb.append("WHERE lr.followup_email!=\"\" AND lr.followup_datetime ").append("BETWEEN  '").append(startDate).append("' AND  '").append(endDate).append("') ");
        sb.append("UNION ");
        sb.append("(SELECT lr.remarks as remarks,lr.followup_datetime as followupDate,'Lcl Unit' AS userName, lu.unit_no AS noteSymbol,lr.followup_email AS followupEmail,ud.first_name AS voidSymbol,ud.email AS emailId ");
        sb.append("FROM lcl_unit_ss_remarks lr JOIN lcl_unit lu ON lu.id=lr.unit_id JOIN user_details ud ON ud.user_id=lr.entered_by_user_id ");
        sb.append("WHERE lr.followup_email!=\"\" AND lr.followup_datetime BETWEEN  '").append(startDate).append("' AND  '").append(endDate).append("') ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setResultTransformer(Transformers.aliasToBean(LclRemarks.class));
        query.addScalar("remarks", StringType.INSTANCE);//lclremarks
        query.addScalar("followupDate", TimestampType.INSTANCE);//followUpdata
        query.addScalar("userName", StringType.INSTANCE);//moduleName Transient Method
        query.addScalar("noteSymbol", StringType.INSTANCE);//fileNumber Imports means --IMP,exports--->MIA
        query.addScalar("followupEmail", StringType.INSTANCE);//followUpEmail
        query.addScalar("voidSymbol", StringType.INSTANCE);//firstName Transient Method
        query.addScalar("emailId", StringType.INSTANCE);//UserEmailId Transient Method
        return query.list();
    }

    public LclRemarks getRemarks(Long fileId, String type, String remarks) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("FROM LclRemarks WHERE type='").append(type).append("' AND lclFileNumber.id=").append(fileId);
        if (remarks != null && !"".equals(remarks)) {
            queryBuilder.append(" AND remarks='").append(remarks).append("'");
        }
        queryBuilder.append(" ORDER BY id DESC");
        return (LclRemarks) getSession().createQuery(queryBuilder.toString()).setMaxResults(1).uniqueResult();
    }

    public void insertLclRemarks(LclFileNumber lclFileNumber, String type, String remarks, User loginUser) throws Exception {
        Date d = new Date();
        LclRemarks lclRemarks = new LclRemarks();
        lclRemarks.setLclFileNumber(lclFileNumber);
        lclRemarks.setEnteredBy(loginUser);
        lclRemarks.setModifiedBy(loginUser);
        lclRemarks.setModifiedDatetime(d);
        lclRemarks.setEnteredDatetime(d);
        lclRemarks.setRemarks(remarks);
        lclRemarks.setType(type);
        new LclRemarksDAO().save(lclRemarks);
    }

    public void insertLclRemarks(LclRemarks lclRemarks, LclFileNumber lclFileNumber, String type, String remarks, User loginUser) throws Exception {
        Date d = new Date();
        lclRemarks.setLclFileNumber(lclFileNumber);
        lclRemarks.setEnteredBy(loginUser);
        lclRemarks.setModifiedBy(loginUser);
        lclRemarks.setModifiedDatetime(d);
        if (null == lclRemarks.getRemarks()) {
            lclRemarks.setEnteredDatetime(d);
        } else if (lclRemarks.getRemarks().equalsIgnoreCase(remarks)) {
            lclRemarks.setEnteredDatetime(lclRemarks.getEnteredDatetime());
        }
        lclRemarks.setRemarks(remarks);
        lclRemarks.setType(type);
        new LclRemarksDAO().saveOrUpdate(lclRemarks);
    }

    public void deleteRemarks(Long fileId, String type) throws Exception {
        String queryString = "delete from lcl_remarks where file_number_id=?0 and type=?1";
        Query queryObject = getSession().createSQLQuery(queryString);
        queryObject.setParameter("0", fileId);
        queryObject.setParameter("1", type);
        queryObject.executeUpdate();
    }

    public void insertLclRemarks(Long fileId, String type, String remarks, Integer userId) throws Exception {
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO lcl_remarks(file_number_id,type,remarks,entered_by_user_id,modified_by_user_id,entered_datetime,modified_datetime)");
        query.append(" VALUES (:fileId,:type,:remarks,:enteredBy,:modifiedBy,SYSDATE(),SYSDATE())");
        SQLQuery queryObject = getSession().createSQLQuery(query.toString());
        queryObject.setLong("fileId", fileId);
        queryObject.setString("type", type);
        queryObject.setString("remarks", remarks);
        queryObject.setInteger("enteredBy", userId);
        queryObject.setInteger("modifiedBy", userId);
        queryObject.executeUpdate();
    }

    public String getFileId(String fileNumber) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append("  id ");
        sb.append("FROM");
        sb.append("  `lcl_file_number` ");
        sb.append("WHERE file_number ='").append(fileNumber).append("' limit 1");
        String queryObject = (String) getCurrentSession().createSQLQuery(sb.toString()).uniqueResult().toString();
        return queryObject;
    }

    public void insertLclRemarks(int costId, String shipmentType, String type, String remarks, User user) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO lcl_remarks (file_number_id,type,remarks,entered_datetime,entered_by_user_id,");
        sb.append("modified_datetime,modified_by_user_id) VALUES (");
        sb.append("(SELECT lbc.file_number_id FROM transaction_ledger tl JOIN lcl_booking_Ac lbc ON lbc.id=tl.cost_id WHERE cost_id=").append(costId);
        sb.append(" AND shipment_type='").append(shipmentType).append("' and tl.transaction_type = 'AC' limit 1)");
        sb.append(",'").append(type).append("','").append(remarks).append("',SYSDATE(),");
        sb.append(user.getUserId()).append(",SYSDATE(),").append(user.getUserId()).append(")");
        getCurrentSession().createSQLQuery(sb.toString()).executeUpdate();
    }

    public Boolean isRemarks(Long fileId, String[] type) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT IF(COUNT(*)>0,true,false) as result FROM lcl_remarks WHERE file_number_id=:fileId");
        sb.append(" AND type IN (:types) ORDER BY id LIMIT 1 ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setLong("fileId", fileId);
        query.setParameterList("types", type);
        query.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) query.uniqueResult();
    }

    public List getRemarksByTypes(Long fileId, String[] remarksTypes) throws Exception {
        String queryStr = "SELECT remarks,TYPE FROM lcl_remarks WHERE file_number_id=:fileId AND TYPE IN(:remarksType) ORDER BY modified_datetime DESC";
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr);
        query.setLong("fileId", fileId);
        query.setParameterList("remarksType", remarksTypes);
        return query.list();
    }

    public void saveRemarks(String remarks, String remarksType, LclFileNumber lclFileNumber, User loginUser, Date now) throws Exception {
        if (null != remarks && !"".equalsIgnoreCase(remarks)) {
            LclRemarks lclRemarks = this.getLclRemarksByType(String.valueOf(lclFileNumber.getId()), remarksType);
            if (lclRemarks == null) {
                lclRemarks = new LclRemarks();
                lclRemarks.setEnteredBy(loginUser);
                lclRemarks.setEnteredDatetime(now);
            }
            lclRemarks.setRemarks(remarks.toUpperCase());
            lclRemarks.setType(remarksType);
            lclRemarks.setLclFileNumber(lclFileNumber);
            lclRemarks.setModifiedBy(loginUser);
            lclRemarks.setModifiedDatetime(now);
            this.saveOrUpdate(lclRemarks);
        }
    }

    public void saveOrUpdateRemarks(String remarks, String remarksType, LclFileNumber lclFileNumber, User loginUser, Date now) throws Exception {
        if (null != remarks && !"".equalsIgnoreCase(remarks)) {
            LclRemarksDAO remarksDAO = new LclRemarksDAO();
            LclRemarks lclRemarks = remarksDAO.getLclRemarksByType(String.valueOf(lclFileNumber.getId()), remarksType);
            if (lclRemarks == null) {
                lclRemarks = new LclRemarks();
                lclRemarks.setEnteredBy(loginUser);
                lclRemarks.setEnteredDatetime(now);
            }
            lclRemarks.setRemarks(remarks.toUpperCase());
            lclRemarks.setType(remarksType);
            lclRemarks.setLclFileNumber(lclFileNumber);
            lclRemarks.setModifiedBy(loginUser);
            lclRemarks.setModifiedDatetime(now);
            remarksDAO.saveOrUpdate(lclRemarks);
        }
    }

    public List<LclRemarks> getRemarksList(Long fileId, String remarksAction, String screenName) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" from LclRemarks where lclFileNumber.id=:fileId ");
        if (("showVoid".equalsIgnoreCase(remarksAction)) || ("voidedNotes".equalsIgnoreCase(remarksAction))) {
            queryStr.append(" AND status = 'Closed' ");
        } else if ("manualNotes".equalsIgnoreCase(remarksAction)) {
            queryStr.append(" AND type IN('Manual Note',");
            queryStr.append("'").append("Quote".equalsIgnoreCase(screenName) ? REMARKS_QT_MANUAL_NOTES
                    : "Booking".equalsIgnoreCase(screenName) ? REMARKS_DR_MANUAL_NOTES : REMARKS_BL_MANUAL_NOTES).append("')");

        } else if (remarksAction.contains("followUpDate")) {
            if ("followUpDatePast".equalsIgnoreCase(remarksAction)) {
                queryStr.append("  AND followupDate >= DATE_FORMAT(SYSDATE(),'%Y-%m-%d') ");
            } else {
                queryStr.append("  AND followupDate <= DATE_FORMAT(SYSDATE(),'%Y-%m-%d') ");
            }
        } else if ("autoNotes".equalsIgnoreCase(remarksAction)) {
            queryStr.append(" AND type IN ('auto','Unit_Tracking', ");
            queryStr.append("'").append("Quote".equalsIgnoreCase(screenName) ? REMARKS_QT_AUTO_NOTES
                    : "Booking".equalsIgnoreCase(screenName) ? REMARKS_DR_AUTO_NOTES : REMARKS_BL_AUTO_NOTES).append("')");
        } else if ("events".equalsIgnoreCase(remarksAction)) {
        } else if ("disputedNotes".equalsIgnoreCase(remarksAction)) {
        } else if ("trackingNotes".equalsIgnoreCase(remarksAction)) {
            queryStr.append(" AND type IN ('T','Unit_Tracking')  ");
        } else if ("specialNotes".equalsIgnoreCase(remarksAction)) {
        } else if ("cts".equalsIgnoreCase(remarksAction)) {
            queryStr.append(" AND type='CTS_EDI' ");
        } else if ("OnHoldNotes".equalsIgnoreCase(remarksAction)) {
            queryStr.append(" AND type='OnHoldNotes' ");
        } else if ("edistg".equalsIgnoreCase(remarksAction)) {
            queryStr.append(" AND type like 'EDISTG%' ");
        } else if ("showAll".equalsIgnoreCase(remarksAction)) {
            queryStr.append(" AND TYPE NOT IN ('E','Loading Remarks','OSD',");
            if ("Quote".equalsIgnoreCase(screenName)) {
                queryStr.append("'LclCorrections','").append(REMARKS_DR_AUTO_NOTES).append("','").append(REMARKS_DR_MANUAL_NOTES).append("'");
                queryStr.append(",'").append(REMARKS_BL_MANUAL_NOTES).append("','").append(REMARKS_BL_AUTO_NOTES).append("'");
            } else if ("Booking".equalsIgnoreCase(screenName)) {
                queryStr.append("'").append(REMARKS_QT_AUTO_NOTES).append("','").append(REMARKS_QT_MANUAL_NOTES).append("'");
                queryStr.append(",'").append(REMARKS_BL_MANUAL_NOTES).append("','").append(REMARKS_BL_AUTO_NOTES).append("'");
            } else {
                queryStr.append("'").append(REMARKS_DR_AUTO_NOTES).append("','").append(REMARKS_DR_MANUAL_NOTES).append("'");
                queryStr.append(",'").append(REMARKS_QT_AUTO_NOTES).append("','").append(REMARKS_QT_MANUAL_NOTES).append("'");
            }
            queryStr.append(" )");

        } else if ("correction".equalsIgnoreCase(remarksAction)) {
            queryStr.append(" AND type='LclCorrections' ");
        }
        if ((!"showVoid".equalsIgnoreCase(remarksAction))
                && (!"voidedNotes".equalsIgnoreCase(remarksAction)) && !"edistg".equalsIgnoreCase(remarksAction)) {
            queryStr.append(" AND status IS NULL AND TYPE <> 'edistg'");
        }
        queryStr.append(" ORDER BY id DESC");
        Query query = getCurrentSession().createQuery(queryStr.toString());
        query.setLong("fileId", fileId);
        List<LclRemarks> remarksList = query.list();
        return remarksList;
    }

    public void updateRemarksByField(LclFileNumber fileNumber, String remark_type,
            String append_remarks_type, String remarks_field, User loginUser, String screen_remark_type) throws Exception {
        LclRemarks remark = this.getRemarks(fileNumber.getId(), remark_type, "");
        String remarks = "";
        if (remark == null && CommonUtils.isNotEmpty(remarks_field)) {
            remarks = "INSERTED-> " + append_remarks_type + " -> " + remarks_field.toUpperCase();
            this.insertOrUpdateRemarks(fileNumber, remark, remark_type, remarks_field, loginUser);

        } else if (remark != null && !remark.getRemarks().equalsIgnoreCase(remarks_field) && CommonUtils.isNotEmpty(remarks_field)) {
            remarks = "UPDATED-> " + append_remarks_type + " -> " + remark.getRemarks().toUpperCase() + " to " + remarks_field.toUpperCase();
            this.insertOrUpdateRemarks(fileNumber, remark, remark_type, remarks_field, loginUser);

        } else if (remark != null && CommonUtils.isEmpty(remarks_field)) {
            remarks = "DELETED-> " + append_remarks_type + " -> " + remark.getRemarks().toUpperCase();
            this.delete(remark);
        }
        if (!remarks.equalsIgnoreCase("")) {
            this.insertLclRemarks(fileNumber.getId(), screen_remark_type, remarks, loginUser.getUserId());
        }
    }

    public void insertOrUpdateRemarks(LclFileNumber file_Number, LclRemarks remark,
            String remark_type, String remarks, User loginUser) throws Exception {
        Date now = new Date();
        if (remark == null) {
            remark = new LclRemarks();
            remark.setEnteredBy(loginUser);
            remark.setEnteredDatetime(now);
        }
        remark.setRemarks(remarks.toUpperCase());
        remark.setType(remark_type);
        remark.setLclFileNumber(file_Number);
        remark.setModifiedBy(loginUser);
        remark.setModifiedDatetime(now);
        this.saveOrUpdate(remark);
    }

    public String isRemarks(Long fileId, String remarks) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT remarks FROM lcl_remarks  WHERE file_number_id=:fileId ");
        queryStr.append(" AND TYPE in('auto','DR-AutoNotes') AND remarks LIKE '").append(remarks).append("%' ");
        queryStr.append(" ORDER BY id DESC LIMIT 1 ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setLong("fileId", fileId);
        String remark = (String) query.uniqueResult();
        return null != remark ? remark : "";
    }

    public String isRemark(Long fileId, String remarks) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT remarks FROM lcl_remarks  WHERE file_number_id=:fileId ");
        queryStr.append(" AND TYPE in('auto','DR-AutoNotes') AND remarks LIKE '%").append(remarks).append("%' ");
        queryStr.append(" ORDER BY id DESC LIMIT 1 ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setLong("fileId", fileId);
        String remark = (String) query.uniqueResult();
        return null != remark ? remark : "";
    }

    public String getCityName(String currentLocId) throws Exception {
        String Query = ("SELECT GROUP_CONCAT(un_loc_name,'(',un_loc_code,')') AS cityname  FROM un_location WHERE id=:currentLocId");
        SQLQuery query = getCurrentSession().createSQLQuery(Query);
        query.setParameter("currentLocId", currentLocId);
        return (String) query.uniqueResult();

    }
     public String deleteOsdRemarks(Long fileId, String type) throws Exception {
        String Query = ("SELECT remarks FROM lcl_remarks WHERE file_number_id=:fileId AND TYPE=:type");
        SQLQuery query = getCurrentSession().createSQLQuery(Query);
        query.setLong("fileId", fileId);
        query.setParameter("type", type);
        return (String) query.uniqueResult();
    }
     
      public void insertLclRemark(String fileId, String type, String remarks, String userId) throws Exception {
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO lcl_remarks(file_number_id,type,remarks,entered_by_user_id,modified_by_user_id,entered_datetime,modified_datetime)");
        query.append(" VALUES (:fileId,:type,:remarks,:enteredBy,:modifiedBy,SYSDATE(),SYSDATE())");
        SQLQuery queryObject = getSession().createSQLQuery(query.toString());
        queryObject.setString("fileId", fileId);
        queryObject.setString("type", type);
        queryObject.setString("remarks", remarks);
        queryObject.setString("enteredBy", userId);
        queryObject.setString("modifiedBy", userId);
        queryObject.executeUpdate();
    }
}
