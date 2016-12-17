/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.lcl.LclSSMasterBl;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cvst.logisoft.struts.form.lcl.SsMasterBlForm;
import java.util.ArrayList;
import java.util.List;
import org.apache.struts.util.LabelValueBean;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.StringType;

public class LclSSMasterBlDAO extends BaseHibernateDAO<LclSSMasterBl> {

    public LclSSMasterBlDAO() {
        super(LclSSMasterBl.class);
    }

    public LclSSMasterBl findBkgNo(Long headerId, String bookingNo) throws Exception {
        String queryStr = "from LclSSMasterBl where lclSsHeader.id=:headerId and spBookingNo=:bookingNo";
        Query query = getSession().createQuery(queryStr);
        query.setLong("headerId", headerId);
        query.setString("bookingNo", bookingNo);
        List list = query.list();
        return CommonUtils.isNotEmpty(list) ? (LclSSMasterBl) list.get(0) : null;
    }
    public String findVoyageNoByBkgNo(String bookingNo) throws Exception {
        String queryStr = "SELECT ss.schedule_no FROM lcl_ss_masterbl bl JOIN lcl_ss_header ss ON ss.id = bl.ss_header_id WHERE bl.sp_booking_no = :bookingNo AND ss.service_type = 'E' LIMIT 1";
        Query query = getSession().createSQLQuery(queryStr);
        query.setString("bookingNo", bookingNo);
        Object obj = query.uniqueResult();
        return obj != null ? obj.toString() : "";
    }

    public List getAllBillingTypes() throws Exception {
        List billingTypeList = new ArrayList();
        billingTypeList.add(new LabelValueBean("Prepaid", "P"));
        billingTypeList.add(new LabelValueBean("Collect", "C"));
        return billingTypeList;
    }

    public List getDestChargeList() throws Exception {
        List billingTypeList = new ArrayList();
        billingTypeList.add(new LabelValueBean("Prepaid", "P"));
        billingTypeList.add(new LabelValueBean("Collect", "C"));
        return billingTypeList;
    }

    public List getAllBookingNumbers(Long headerId) throws Exception {
        List bookingNumbersList = new ArrayList();
        bookingNumbersList.add(new LabelValueBean("Please Select", ""));
        List l = getAllBookingNumbersByHeaderId(headerId);
        if (!l.isEmpty()) {
            for (Object o : l) {
                if (o != null) {
                    bookingNumbersList.add(new LabelValueBean(o.toString(), o.toString()));
                }

            }
        }
        return bookingNumbersList;
    }

    public List<Object> getAllBookingNumbersByHeaderId(Long headerId) throws Exception {
        String queryString = "SELECT sp_booking_no FROM  lcl_ss_masterbl WHERE ss_header_id = '" + headerId + "'";
        Query queryObject = getSession().createSQLQuery(queryString);
        return queryObject.list();
    }

    public List<SsMasterBlForm> getSsMasterDisputeBlList(String status, String screenName, SsMasterBlForm masterForm) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT fn.headerId AS headerId, lsh.`schedule_no` AS voyage, fn.bookingNo AS bookingNo, lsh.service_type as serviceType, ");
        sb.append(" UnLocationGetCodeByID(lsh.`origin_id`) AS voyageOrigin, UnLocationGetCodeByID(lsh.`destination_id`) AS voyageDest, ");
        sb.append(" (SELECT date_format(lsd.std,'%Y-%b-%d') FROM lcl_ss_detail lsd WHERE lsd.ss_header_id = fn.headerId ORDER BY id ASC LIMIT 1) AS std, ");
        sb.append(" (SELECT date_format(lsd.sta,'%Y-%b-%d') FROM lcl_ss_detail lsd WHERE lsd.ss_header_id = fn.headerId ORDER BY id ASC LIMIT 1) AS sta,");
        sb.append(" tp.acct_name AS carrierName, ");
        sb.append(" fn.ack AS acknowledge, fn.STATUS AS status, date_format(fn.enteredDate,'%Y-%b-%d') AS disputedDate, ");
        sb.append("  fn.documentId as documentId , fn.masterId as documentMasterId,fn.prepaid_collect as sslBlPrepaid FROM  ");
        sb.append(" (SELECT d.id as documentId, d.Document_ID AS masterId, b.`ss_header_id` AS headerId , b.`sp_booking_no` AS bookingNo, d.`ack_comments` AS ack, ");
        sb.append(" d.`status` AS STATUS, d.`Date_Opr_done` AS enteredDate,b.prepaid_collect AS prepaid_collect FROM document_store_log d JOIN lcl_ss_masterbl b  ");
        sb.append(" ON b.`id` = SUBSTRING(d.`Document_ID`,INSTR(d.`Document_ID`,'-')+1,LENGTH(d.`Document_ID`)) ");
        sb.append(" AND d.id = (SELECT d.id FROM document_store_log d WHERE d.`Document_ID` = CONCAT(b.`sp_booking_no`,'-',b.`id`) ORDER BY d.id DESC LIMIT 1) ");
        sb.append(" where d.status=:status and  d.screen_name=:screenName order by d.id desc ) AS fn ");
        sb.append(" JOIN lcl_ss_header lsh ON  lsh.id = fn.headerId join lcl_ss_detail lsd on lsh.id =lsd.ss_header_id ");
        sb.append(" and  lsd.id = (SELECT lsd.id FROM lcl_ss_detail lsd WHERE lsd.ss_header_id = lsh.id order by lsd.id desc limit 1) ");
        sb.append(" join trading_partner tp on tp.acct_no = lsd.sp_acct_no ");
        sb.append(getConditionQuery(masterForm));
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameter("status", status);
        query.setParameter("screenName", screenName);
        query.setResultTransformer(Transformers.aliasToBean(SsMasterBlForm.class));
        query.addScalar("headerId", StringType.INSTANCE);
        query.addScalar("voyage", StringType.INSTANCE);
        query.addScalar("bookingNo", StringType.INSTANCE);
        query.addScalar("serviceType", StringType.INSTANCE);
        query.addScalar("voyageOrigin", StringType.INSTANCE);
        query.addScalar("voyageDest", StringType.INSTANCE);
        query.addScalar("carrierName", StringType.INSTANCE);
        query.addScalar("disputedDate", StringType.INSTANCE);
        query.addScalar("std", StringType.INSTANCE);
        query.addScalar("sta", StringType.INSTANCE);
        query.addScalar("status", StringType.INSTANCE);
        query.addScalar("acknowledge", StringType.INSTANCE);
        query.addScalar("documentMasterId", StringType.INSTANCE);
        query.addScalar("documentId", StringType.INSTANCE);
        query.addScalar("sslBlPrepaid", StringType.INSTANCE);
        return query.list();
    }

    public String getConditionQuery(SsMasterBlForm masterForm) throws Exception {
        StringBuilder query = new StringBuilder();
        boolean flag = false;
        if (CommonUtils.isNotEmpty(masterForm.getVoyageNumber())) {
            query.append(" where lsh.schedule_no = ").append(masterForm.getVoyageNumber());
            flag = true;
        }
        if (CommonUtils.isNotEmpty(masterForm.getOrigin()) || CommonUtils.isNotEmpty(masterForm.getPol())) {
            query.append(flag ? " AND " : " Where ");
            query.append(" lsh.origin_id = ").append(masterForm.getOriginId());
            flag = true;
        }
        if (CommonUtils.isNotEmpty(masterForm.getDestination()) || CommonUtils.isNotEmpty(masterForm.getPod())) {
            query.append(flag ? " AND " : " Where ");
            query.append(" lsh.destination_id = ").append(masterForm.getDestinationId());
            flag = true;
        }
        if (CommonUtils.isNotEmpty(masterForm.getSslAccountNo())) {
            query.append(flag ? " AND " : " Where ");
            query.append(" tp.acct_no = '").append(masterForm.getSslAccountNo()).append("'");
            flag = true;
        }
        if (CommonUtils.isNotEmpty(masterForm.getEtd())) {
            String std = DateUtils.formatDate(DateUtils.parseDate(masterForm.getEtd(), "dd-MMM-yyyy"), "yyyy-MM-dd");
            query.append(flag ? " AND " : " Where ");
            query.append(" lsd.std = '").append(std).append("'");
            flag = true;
        }
        if (CommonUtils.isNotEmpty(masterForm.getEta())) {
            String sta = DateUtils.formatDate(DateUtils.parseDate(masterForm.getEta(), "dd-MMM-yyyy"), "yyyy-MM-dd");
            query.append(flag ? " AND " : " Where ");
            query.append("  lsd.sta = '").append(sta).append("'");
        }
        if (CommonUtils.isNotEmpty(masterForm.getSslBlPrepaid())) {
            query.append(flag ? " AND " : " Where ");
            query.append(" fn.prepaid_collect = '").append(masterForm.getSslBlPrepaid()).append("'");
            flag = true;
        }
        return query.toString();
    }

    public boolean isMasterBookingAvailable(String headerId, String currentBookingNo) throws Exception {
        SQLQuery query = getCurrentSession().createSQLQuery("select if(count(*)>0,true,false) from lcl_ss_masterBl where ss_header_id =:headerId "
                + " and sp_booking_no=:currentBookingNo ");
        query.setParameter("headerId", headerId);
        query.setParameter("currentBookingNo", currentBookingNo);
        Object obj = query.uniqueResult();
        return "1".equals(obj.toString());
    }

    public String getMasterBlDocumentStatus(String spBookingNo, String headerId) throws Exception {
        String queryString = "SELECT d.status FROM `document_store_log` d WHERE d.document_id = ("
                + "SELECT CONCAT(m.`sp_booking_no`,'-',m.`id`) FROM lcl_ss_masterbl m WHERE m.ss_header_id =:headerId AND m.sp_booking_no =:spBookingNo"
                + ") ORDER BY id DESC LIMIT 1";
        SQLQuery query = getCurrentSession().createSQLQuery(queryString);
        query.setParameter("headerId", headerId);
        query.setParameter("spBookingNo", spBookingNo);
        Object obj = query.uniqueResult();
        return obj != null ? obj.toString() : "";
    }

    public String getMasterDetails(Long unitSsId, String bookingNo) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append("SELECT lsm.prepaid_collect as result FROM lcl_unit_ss lus ");
        queryStr.append(" JOIN lcl_ss_masterbl lsm ON ");
        queryStr.append("  (lsm.sp_booking_no=lus.sp_booking_no AND lsm.ss_header_id=lus.ss_header_id) ");
        queryStr.append(" WHERE lus.id=:unitSsId AND lus.sp_booking_no=:bookingNo ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setParameter("unitSsId", unitSsId);
        query.setParameter("bookingNo", bookingNo);
        query.addScalar("result", StringType.INSTANCE);
        return (String) query.setMaxResults(1).uniqueResult();
    }

    public String getCostByChangeBill(String headerId, String bookingNo) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT GROUP_CONCAT(DISTINCT lsa.unit_ss_id) AS unitSsId FROM lcl_ss_Ac lsa ");
        queryStr.append(" JOIN lcl_unit_ss lus ON (lus.id=lsa.unit_ss_id) ");
        queryStr.append(" WHERE lus.sp_booking_no=:spBookingNo  AND lsa.ss_header_id=:ssHeaderId AND  ");
        queryStr.append(" lsa.manual_entry=FALSE GROUP BY lsa.ss_header_id  ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setParameter("ssHeaderId", headerId);
        query.setParameter("spBookingNo", bookingNo);
        query.addScalar("unitSsId", StringType.INSTANCE);
        return (String) query.setMaxResults(1).uniqueResult();
    }

    public List<LclUnitSs> getUnitSsListLinkWithMaster(Long headerId, String spBookingNo) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" from LclUnitSs lus join fetch lus.lclSsHeader lsh where lsh.id =:headerId and lus.spBookingNo=:spBookingNo");
        Query query = getCurrentSession().createQuery(sb.toString());
        query.setParameter("headerId", headerId);
        query.setParameter("spBookingNo", spBookingNo);
        return query.list();
    }

    public String getMasterBlInvoiceValueTotal(String headerId, String spBookingNo) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT SUM(bl.invoice_value) AS totalInvoiceValue  FROM ");
        sb.append(" ( SELECT bp.`file_number_id` AS fileId  FROM ");
        sb.append(" lcl_booking_piece bp ");
        sb.append(" JOIN lcl_booking_piece_unit bpu ");
        sb.append("  ON bp.`id` = bpu.`booking_piece_id` ");
        sb.append(" JOIN lcl_unit_ss lus ");
        sb.append("   ON lus.id = bpu.`lcl_unit_ss_id` ");
        sb.append(" JOIN lcl_ss_header lsh ");
        sb.append("  ON lus.`ss_header_id` = lsh.`id` ");
        sb.append(" WHERE lus.`ss_header_id` =:headerId ");
        sb.append(" AND lus.`sp_booking_no` =:spBookingNo GROUP BY bp.`file_number_id` ) as f ");
        sb.append(" JOIN lcl_file_number lf ON lf.id = f.fileId  ");
        sb.append(" JOIN lcl_bl bl ON bl.`file_number_id` = getHouseBLForConsolidateDr (lf.id) ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameter("headerId", headerId);
        query.setParameter("spBookingNo", spBookingNo);
        Object result = query.addScalar("totalInvoiceValue", BigDecimalType.INSTANCE).uniqueResult();
        return result != null ? result.toString() : "";
    }
}
