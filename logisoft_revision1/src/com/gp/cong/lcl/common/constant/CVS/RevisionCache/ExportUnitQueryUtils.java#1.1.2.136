/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.lcl.common.constant;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.lcl.model.ExportVoyageSearchModel;
import com.gp.cong.lcl.model.LclModel;
import com.gp.cong.logisoft.beans.ManifestBean;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.lcl.RoleDutyDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LclUnitsScheduleForm;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.util.LabelValueBean;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.BooleanType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

public class ExportUnitQueryUtils extends BaseHibernateDAO {

    public List<ManifestBean> getUnitViewDrList(Long unitSSId, HttpServletRequest request) throws Exception { // to show DR or Bl list in View Dr page
        User user = (User) request.getSession().getAttribute("loginuser");
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT fn.fileId AS fileId, fn.fileNo AS fileNo, fn.STATUS AS STATUS,fn.state as state,");
        queryBuilder.append("(SELECT CONCAT_WS('~~~',ar.invoice_number,ar.status) FROM ar_red_invoice ar WHERE ar.bl_number = fn.fileNo ORDER BY ar.id DESC LIMIT 1) AS arInvoiceNumber,");
        queryBuilder.append(" getDisposion_UnLoc(fn.fileId) AS disposition, IF(fn.state ='BL' , BlNumberSystemForLclExports(bl.file_number_id),'') AS blNo,");
        queryBuilder.append(" piece.total_piece as totalPieceCount, piece.total_weight_imperial as totalWeightImperial ,piece.total_volume_imperial as totalVolumeImperial, ");
        queryBuilder.append(" blPiece.blCft as blCft ,blPiece.blCbm as blCbm,blPiece.blLbs as blLbs ,blPiece.blKgs as blKgs, ");
        queryBuilder.append(" IF(fn.state = 'BL',bl.rate_type,'') AS rateType,  ");
        queryBuilder.append(" (SELECT SUM(chg.ap_amount) FROM lcl_booking_ac chg WHERE chg.file_number_id = fn.fileId AND deleted = '0' AND chg.ap_gl_mapping_id IN (SELECT id FROM gl_mapping WHERE charge_code = 'FFCOMM' AND shipment_type='LCLE')) AS ffComm,  ");
        queryBuilder.append(" IF(fn.state = 'BL',TradingPartnerAcctName(bl.ship_acct_no),TradingPartnerAcctName(b.ship_acct_no)) AS shipperName,  ");
        queryBuilder.append(" IF(fn.state = 'BL',TradingPartnerAcctName(bl.cons_acct_no),TradingPartnerAcctName(b.cons_acct_no)) AS consigneeName,  ");
        queryBuilder.append(" IF(fn.state = 'BL',TradingPartnerAcctName(bl.fwd_acct_no),TradingPartnerAcctName(b.fwd_acct_no)) AS forwarderName,  ");
        queryBuilder.append(" UnLocationGetCodeByID(b.poo_id) AS origin,");
        queryBuilder.append(" UnLocationGetCodeByID (b.fd_id) AS destination,");
        queryBuilder.append(" UnLocationGetCodeByID (b.pol_id) AS pol,");
        queryBuilder.append(" UnLocationGetCodeByID (b.pod_id) AS pod,");
        queryBuilder.append(" IF(fn.state = 'BL',TerminalGetLocationByNo(bl.billing_terminal),TerminalGetLocationByNo(b.billing_terminal))AS terminalLocation,");
        queryBuilder.append(" IF(b.poo_pickup,(SELECT pickup_city FROM lcl_booking_pad WHERE file_number_id =fn.fileId),'')  AS pickupCity, ");
        queryBuilder.append(" UnLocationGetNameByID(b.poo_id) AS originName,getStateCode(b.poo_id) AS originState,");
        queryBuilder.append(" UnLocationGetNameByID(b.pol_id) AS polName,getStateCode(b.pol_id) AS polState,UnLocationGetNameByID(b.pod_id) AS podName,");
        queryBuilder.append(" getStateCode(b.pod_id) AS podCountry,UnLocationGetNameByID(b.fd_id) AS destinationName,getStateCode(b.fd_id) AS destinationCountry,");
        queryBuilder.append(" bl.billing_type AS billingType,IF(b.client_pwk_recvd = 1, 'Y', '') AS doc,(SELECT invoice_number FROM TRANSACTION WHERE drcpt = fn.fileNo LIMIT 1) AS blInvoiceNo,");
        queryBuilder.append("(SELECT schedule_no FROM lcl_ss_header WHERE id = b.booked_ss_header_id) AS bookedVoyageNo,bl.posted_by_user_id AS postedByUserId,fn.haz AS hazmat,");
        queryBuilder.append("(SELECT CONCAT_WS('~~~',GROUP_CONCAT(htc.code SEPARATOR '<br>'),COUNT(htc.code),GROUP_CONCAT(LEFT(htc.code, INSTR(htc.code, '/') - 1)))");
        queryBuilder.append(" FROM lcl_booking_hot_code htc WHERE htc.file_number_id = fn.fileId) AS hotCodes, ");
        queryBuilder.append(" IF(bl.file_number_id != fn.fileId , null ,IF(fn.state = 'BL',(SELECT SUM(chg.ar_amount)+ SUM(chg.adjustment_amount) FROM lcl_bl_ac chg WHERE chg.file_number_id = bl.file_number_id AND chg.ar_bill_to_party = 'A'),(SELECT SUM(chg.ar_amount)+ SUM(chg.adjustment_amount) FROM lcl_booking_ac chg WHERE chg.file_number_id = fn.fileId AND chg.ar_bill_to_party = 'A'))) AS colCharge, ");
        queryBuilder.append(" IF(bl.file_number_id != fn.fileId , null ,IF(fn.state = 'BL',(SELECT SUM(chg.ar_amount)+ SUM(chg.adjustment_amount) FROM lcl_bl_ac chg WHERE chg.file_number_id = bl.file_number_id AND chg.ar_bill_to_party != 'A'),(SELECT SUM(chg.ar_amount)+ SUM(chg.adjustment_amount) FROM lcl_booking_ac chg WHERE chg.file_number_id = fn.fileId AND chg.ar_bill_to_party != 'A'))) AS ppdCharge, ");
        queryBuilder.append(" IF(bl.file_number_id != fn.fileId , '' ,IF(fn.state = 'BL',(SELECT GROUP_CONCAT(DISTINCT chg.ar_bill_to_party SEPARATOR '/') FROM lcl_bl_ac chg WHERE chg.file_number_id = bl.file_number_id AND chg.ar_bill_to_party != 'A'),(SELECT GROUP_CONCAT(DISTINCT chg.ar_bill_to_party SEPARATOR '/') FROM lcl_booking_ac chg WHERE chg.file_number_id = fn.fileId AND chg.ar_bill_to_party != 'A'))) AS ppdParties, ");
        // checking condition for  status and classname label in list
        queryBuilder.append(" CASE   ");
        Boolean isManifest_Posted_Bl = new RoleDutyDAO().getRoleDetails("lcl_manifest_postedbl", user.getRole().getRoleId());
        if (isManifest_Posted_Bl) {
            queryBuilder.append(" WHEN fn.state <> 'BL' AND lbe.no_bl_required ='1'  THEN 'purpleBold'  ");
        }
        queryBuilder.append(" WHEN fn.state <> 'BL' THEN 'fileNo' ");
        queryBuilder.append(" WHEN bl.file_number_id != fn.fileId THEN 'greenBold14px' ");
        queryBuilder.append(" WHEN (bl.posted_by_user_id <> '' OR  bl.posted_by_user_id IS NOT NULL ) AND  fn.status <> 'M' THEN  'purpleBold' ");
        queryBuilder.append(" WHEN fn.status ='M' THEN 'greenBold14px' ");
        queryBuilder.append(" WHEN fn.state = 'BL' AND (bl.posted_by_user_id='' OR  bl.posted_by_user_id IS NULL) THEN 'fileNo' END  AS className, ");
        // ---------------------------------------------------------------STATUS LABEL-----------------------------------------------------------------------------
        queryBuilder.append(" CASE  ");
        if (isManifest_Posted_Bl) {
            queryBuilder.append("  WHEN fn.state <> 'BL' AND lbe.no_bl_required ='1'  THEN 'NO B/L Required'   ");
        }
        queryBuilder.append(" WHEN fn.state <> 'BL' THEN 'NoBL'");
        queryBuilder.append(" WHEN bl.file_number_id != fn.fileId THEN 'CONS'");
        queryBuilder.append(" WHEN (bl.posted_by_user_id <> '' OR  bl.posted_by_user_id IS NOT NULL ) AND  fn.status <> 'M' THEN  'POSTED' ");
        queryBuilder.append(" WHEN fn.status ='M' THEN 'MANIFESTED' ");
        queryBuilder.append(" WHEN fn.state = 'BL' AND (bl.posted_by_user_id = '' OR  bl.posted_by_user_id IS NULL) THEN 'POOL' END  AS statusLabel,");
        queryBuilder.append(" IF(lc.id IS NOT NULL,TRUE,FALSE) AS isCorrection, ");
        queryBuilder.append(" IF(lc.id IS NOT NULL,(SELECT SUM(ch.new_amount) FROM lcl_correction_Charge ch  ");
        queryBuilder.append(" JOIN gl_mapping gl ON gl.id = ch.gl_mapping_id WHERE  ");
        queryBuilder.append(" ch.correction_id=lc.id AND gl.charge_code='FTFFEE'),  ");
        queryBuilder.append(" (SELECT SUM(blac.ar_amount) FROM lcl_bl_Ac blac JOIN gl_mapping gl ON gl.id = blac.ar_gl_mapping_id  ");
        queryBuilder.append(" WHERE file_number_id = bl.file_number_id AND gl.charge_code='FTFFEE')) AS ftfFee  ");

        // --------------------------------------------------------MAIN SUB QUERY------------------------------------------------------------------------------------
        queryBuilder.append(" from ( ");
        queryBuilder.append(" SELECT f.id AS fileId, f.file_number AS fileNo, f.state AS state, f.status AS STATUS, lbp.hazmat AS haz ");
        queryBuilder.append(" FROM lcl_file_number f JOIN lcl_booking_piece lbp ON lbp.file_number_id = f.id JOIN lcl_booking_piece_unit u ON u.booking_piece_id = lbp.id ");
        queryBuilder.append(" WHERE u.lcl_unit_ss_id =:unitSdId ) fn");
        // ---------------------------------------------------------JOIN STATS HERE-----------------------------------------------------------------------------------
        queryBuilder.append(" LEFT JOIN lcl_booking b  ON fn.fileId = b.file_number_id ");
        // ------------------------------------- Conslidation logic is applied please verify ----------------------------------------------------------------
        queryBuilder.append("  LEFT JOIN lcl_bl bl  ON (bl.file_number_id = getHouseBLForConsolidateDr(fn.fileId))");
        queryBuilder.append("  LEFT JOIN lcl_correction lc ON (lc.file_number_id = bl.file_number_id AND lc.status = 'A') ");

        if (isManifest_Posted_Bl) {
            queryBuilder.append(" JOIN lcl_booking_export lbe ON fn.fileId = lbe.file_number_id  ");
        }
        queryBuilder.append(getSumOfCommodityValues());
        queryBuilder.append(getSumOfBlCommodityValues());
        queryBuilder.append(" GROUP BY fn.fileId ");
        queryBuilder.append(" ORDER BY fn.fileNo ");

        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setParameter("unitSdId", unitSSId);
        query.setResultTransformer(Transformers.aliasToBean(ManifestBean.class));
        query.addScalar("fileId", LongType.INSTANCE);
        query.addScalar("fileNo", StringType.INSTANCE);
        query.addScalar("status", StringType.INSTANCE);
        query.addScalar("state", StringType.INSTANCE);
        query.addScalar("arInvoiceNumber", StringType.INSTANCE);
        query.addScalar("disposition", StringType.INSTANCE);
        query.addScalar("blNo", StringType.INSTANCE);
        query.addScalar("totalPieceCount", IntegerType.INSTANCE);
        query.addScalar("totalWeightImperial", BigDecimalType.INSTANCE);
        query.addScalar("totalVolumeImperial", BigDecimalType.INSTANCE);
        query.addScalar("blCft", BigDecimalType.INSTANCE);
        query.addScalar("blCbm", BigDecimalType.INSTANCE);
        query.addScalar("blKgs", BigDecimalType.INSTANCE);
        query.addScalar("blLbs", BigDecimalType.INSTANCE);
        query.addScalar("rateType", StringType.INSTANCE);
        query.addScalar("ffComm", BigDecimalType.INSTANCE);
        query.addScalar("shipperName", StringType.INSTANCE);
        query.addScalar("consigneeName", StringType.INSTANCE);
        query.addScalar("forwarderName", StringType.INSTANCE);
        query.addScalar("origin", StringType.INSTANCE);
        query.addScalar("destination", StringType.INSTANCE);
        query.addScalar("pol", StringType.INSTANCE);
        query.addScalar("pod", StringType.INSTANCE);
        query.addScalar("terminalLocation", StringType.INSTANCE);
        query.addScalar("pickupCity", StringType.INSTANCE);
        query.addScalar("originName", StringType.INSTANCE);
        query.addScalar("originState", StringType.INSTANCE);
        query.addScalar("polName", StringType.INSTANCE);
        query.addScalar("polState", StringType.INSTANCE);
        query.addScalar("podName", StringType.INSTANCE);
        query.addScalar("podCountry", StringType.INSTANCE);
        query.addScalar("destinationName", StringType.INSTANCE);
        query.addScalar("destinationCountry", StringType.INSTANCE);
        query.addScalar("billingType", StringType.INSTANCE);
        query.addScalar("doc", StringType.INSTANCE);
        query.addScalar("blInvoiceNo", StringType.INSTANCE);
        query.addScalar("bookedVoyageNo", StringType.INSTANCE);
        query.addScalar("hotCodes", StringType.INSTANCE);
        query.addScalar("postedByUserId", IntegerType.INSTANCE);
        query.addScalar("hazmat", BooleanType.INSTANCE);
        query.addScalar("colCharge", BigDecimalType.INSTANCE);
        query.addScalar("ppdCharge", BigDecimalType.INSTANCE);
        query.addScalar("ppdParties", StringType.INSTANCE);
        query.addScalar("className", StringType.INSTANCE);
        query.addScalar("statusLabel", StringType.INSTANCE);
        query.addScalar("isCorrection", BooleanType.INSTANCE);
        query.addScalar("ftfFee", BigDecimalType.INSTANCE);
        List<ManifestBean> drList = query.list();
        return drList;
    }

    public String getSumOfCommodityValues() {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" LEFT JOIN  (SELECT lb.file_number_id, ");
        queryBuilder.append(" SUM(IF(lb.actual_piece_count IS NOT NULL AND lb.actual_piece_count != 0, lb.actual_piece_count,lb.booked_piece_count)) AS total_piece, ");
        queryBuilder.append(" SUM(IF(lb.actual_weight_imperial IS NOT NULL AND lb.actual_weight_imperial != 0.000,lb.actual_weight_imperial,lb.booked_weight_imperial )) AS total_weight_imperial, ");
        queryBuilder.append(" SUM(IF(lb.actual_volume_imperial IS NOT NULL AND lb.actual_volume_imperial != 0.000,lb.actual_volume_imperial,lb.booked_volume_imperial)) AS total_volume_imperial ");
        queryBuilder.append(" FROM lcl_booking_piece lb GROUP BY lb.file_number_id ) piece ON piece.file_number_id = fn.fileId ");
        return queryBuilder.toString();
    }

    public String getSumOfBlCommodityValues() {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" LEFT JOIN  (SELECT lb.file_number_id, ");
        queryBuilder.append(" SUM(IF(lb.actual_weight_metric IS NOT NULL AND lb.actual_weight_metric != 0.000,lb.actual_weight_metric,lb.booked_weight_metric )) AS blKgs, ");
        queryBuilder.append(" SUM(IF(lb.actual_volume_metric IS NOT NULL AND lb.actual_volume_metric != 0.000,lb.actual_volume_metric,lb.booked_volume_metric)) AS blCbm, ");
        queryBuilder.append(" SUM(IF(lb.actual_weight_imperial IS NOT NULL AND lb.actual_weight_imperial != 0.000,lb.actual_weight_imperial,lb.booked_weight_imperial )) AS blLbs, ");
        queryBuilder.append(" SUM(IF(lb.actual_volume_imperial IS NOT NULL AND lb.actual_volume_imperial != 0.000,lb.actual_volume_imperial,lb.booked_volume_imperial)) AS blCft ");
        queryBuilder.append(" FROM lcl_bl_piece lb GROUP BY lb.file_number_id ) blPiece ON blPiece.file_number_id = bl.file_number_id ");
        return queryBuilder.toString();
    }

    public String selectvoyageSearchQuery() {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("   SELECT  ");
        queryBuilder.append(" lsh.id AS ssHeaderId, ");
        queryBuilder.append(" if(lsh.service_type='C',lsh.service_type,'') AS voyageStatus, ");
        queryBuilder.append(" lsh.schedule_no AS scheduleNo, ");
        queryBuilder.append(" lsh.service_type AS serviceType, ");
        queryBuilder.append(" UnLocationGetCodeByID (lsh.destination_id) AS fdUnLocCode,");
        queryBuilder.append(" UnLocationGetNameStateCntryByID (lsh.destination_id) AS fdName,  ");
        queryBuilder.append(" UnLocationGetCodeByID (lsd.departure_id) AS departPierUnloc, ");
        queryBuilder.append(" UnLocationGetNameStateCntryByID (lsd.departure_id) AS departPier, ");
        queryBuilder.append(" UnLocationGetCodeByID (lsd.arrival_id) AS arrivalPierUnloc, ");
        queryBuilder.append(" UnLocationGetNameStateCntryByID (lsd.arrival_id) AS arrivalPier, ");
        //queryBuilder.append(" lsd.trans_mode AS ssTransMode, ");
        queryBuilder.append(" DATE_FORMAT(lsd.general_loading_deadline, '%d-%b-%Y') AS loadingDeadLineDate, ");
        queryBuilder.append(" DATE_FORMAT(lsd.std, '%d-%b-%Y') AS etaSailDate, ");
        queryBuilder.append(" DATE_FORMAT(lsd.sta, '%d-%b-%Y') AS etaPodDate, ");
        //queryBuilder.append(" DATE_FORMAT(lsd.general_lrdt, '%d-%b-%Y') AS polLrdDate, ");
        // queryBuilder.append(" DATE_FORMAT(lsd.sta, '%d-%b-%Y') AS etaFd, ");
        queryBuilder.append(" DATEDIFF(lsd.sta,lsd.std) AS totaltransPod, ");
        queryBuilder.append(" DATEDIFF(lsd.sta,lsd.general_lrdt) AS totaltransFd, ");
        queryBuilder.append(" lsd.sp_reference_name AS vesselName, ");
        queryBuilder.append(" lsd.sp_reference_no AS ssVoyage, ");
        queryBuilder.append(" (select tp.acct_name from trading_partner tp where tp.acct_no = lsd.sp_acct_no) AS carrierName, ");
        queryBuilder.append(" lsd.sp_acct_no AS carrierAcctNo,lus.su_heading_note as sealNo, ");
        // queryBuilder.append(" UserDetailsGetLoginNameByID (lsh.entered_by_user_id) AS createdBy, ");
        //queryBuilder.append(" UserDetailsGetLoginNameByID (lsh.owner_user_id) AS voyOwner, ");
//        queryBuilder.append(" (SELECT COUNT(*) FROM lcl_unit lu ");
//        queryBuilder.append(" JOIN lcl_unit_ss luss ON (luss.unit_id = lu.id) ");
//        queryBuilder.append(" WHERE luss.ss_header_id = lsh.id) AS unitcount,");
        queryBuilder.append(" lu.unit_no AS unitNo,ut.description  AS unitSize,  ");
        queryBuilder.append(" ut.short_desc  AS unitSizeShortDesc,  ");
        queryBuilder.append("  (SELECT COUNT(DISTINCT fn.file_number) FROM   ");
        queryBuilder.append(" lcl_booking_piece_unit bpu JOIN lcl_booking_piece bp ON bpu.booking_piece_id=bp.id    ");
        queryBuilder.append("  JOIN lcl_file_number fn ON bp.file_number_id=fn.id WHERE bpu.lcl_unit_ss_id=lus.id) AS numberDrs ,  ");
        queryBuilder.append(" (SELECT UserDetailsGetLoginNameByID(luw.stuffed_user_id) FROM lcl_unit_whse luw    ");
        queryBuilder.append(" WHERE luw.unit_id=lus.unit_id AND luw.ss_header_id   ");
        queryBuilder.append("  = lsd.ss_header_id ORDER BY luw.id ASC LIMIT 1) AS loadedBy,   ");
        queryBuilder.append("  (SELECT luw.location FROM lcl_unit_whse luw WHERE luw.unit_id=lus.unit_id   ");
        queryBuilder.append("  AND luw.ss_header_id = lsd.ss_header_id ORDER BY luw.id DESC LIMIT 1) AS doorLocation,  ");
        queryBuilder.append(" (SELECT d.elite_code FROM lcl_unit_ss_dispo lusd JOIN disposition d  ");
        queryBuilder.append(" ON d.id=lusd.disposition_id WHERE lus.unit_id=lusd.unit_id ORDER BY lusd. id DESC LIMIT 1) AS dispoCode, ");
        queryBuilder.append(" (SELECT d.description FROM lcl_unit_ss_dispo lusd JOIN disposition d  ");
        queryBuilder.append(" ON d.id=lusd.disposition_id WHERE lus.unit_id=lusd.unit_id ORDER BY lusd. id DESC LIMIT 1) AS dispoDesc, ");
        queryBuilder.append(" (SELECT lnn.inbond_no FROM lcl_booking_piece_unit lbpu JOIN lcl_booking_piece lbp ON lbp.id=lbpu.booking_piece_id  ");
        queryBuilder.append(" JOIN lcl_inbond lnn ON lnn.file_number_id=lbp.file_number_id WHERE lbpu.lcl_unit_ss_id=lus.id LIMIT 1) AS isInbond,  ");
        queryBuilder.append(" (SELECT lbh.un_hazmat_no FROM lcl_booking_piece_unit lbpu JOIN lcl_booking_piece lbp ON lbp.id=lbpu.booking_piece_id ");
        queryBuilder.append(" JOIN lcl_booking_hazmat lbh ON lbh.file_number_id=lbp.file_number_id WHERE lbpu.lcl_unit_ss_id=lus.id LIMIT 1) AS isHazmat, ");
        //queryBuilder.append(" FROM lcl_unit lu JOIN lcl_unit_ss luss ON (luss.unit_id = lu.id) ");
        //queryBuilder.append(" WHERE luss.ss_header_id = lsh.id) AS unitNo ");
        queryBuilder.append("  (SELECT  SUM(IF(bp.actual_volume_imperial IS NOT NULL AND bp.actual_volume_imperial != 0.000, bp.actual_volume_imperial, bp.booked_volume_imperial)) FROM lcl_booking_piece_unit bpu JOIN lcl_booking_piece bp  ON bpu.booking_piece_id = bp.id ");
        queryBuilder.append(" JOIN lcl_file_number fn ON bp.file_number_id = fn.id WHERE bpu.lcl_unit_ss_id = lus.id) AS totalVolumeMetric , ");
        queryBuilder.append(" (SELECT SUM(IF(bp.actual_weight_imperial IS NOT NULL AND bp.actual_weight_imperial != 0.000,  bp.actual_weight_imperial,  bp.booked_weight_imperial))  FROM lcl_booking_piece_unit bpu JOIN lcl_booking_piece bp  ON bpu.booking_piece_id = bp.id ");
        queryBuilder.append("  JOIN lcl_file_number fn ON bp.file_number_id = fn.id WHERE bpu.lcl_unit_ss_id = lus.id) AS totalWeightMetric ");

        return queryBuilder.toString();
    }

    public String fromVoyageSearchQuery(LclUnitsScheduleForm lclUnitsScheduleForm) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("   FROM ");
        queryBuilder.append(" lcl_ss_header lsh ");
        queryBuilder.append(" JOIN un_location pod ON lsh.destination_id=pod.id ");
        queryBuilder.append(" JOIN lcl_ss_detail lsd ");
        queryBuilder.append(" ON (lsh.id = lsd.ss_header_id ");
        queryBuilder.append("  AND lsh.service_type IN('E','C') AND lsh.status <> 'V' ");
        queryBuilder.append(" AND lsd.`id` = (SELECT ls.id FROM ");
        queryBuilder.append(" lcl_ss_detail ls ");
        queryBuilder.append("  WHERE ls.`ss_header_id` = lsh.id ");
        queryBuilder.append(" ORDER BY id DESC LIMIT 1)) ");
        queryBuilder.append("  JOIN lcl_unit_ss lus ");
        queryBuilder.append(" ON (lus.ss_header_id = lsh.id ) ");
        queryBuilder.append("  JOIN lcl_unit lu ON (lu.id=lus.unit_id) JOIN unit_type ut ON (ut.id = lu.unit_type_id ");
        queryBuilder.append(!lclUnitsScheduleForm.isIsLclContainerSize() ? " AND ut.description <> 'LCL' )" : ")");
        queryBuilder.append(" WHERE lsh.`origin_id` = :originId AND lus.status = 'E'  ");
        if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getFinalDestinationId())) {
            queryBuilder.append(" AND lsh.`destination_id` = :destinationId ");
        }
        queryBuilder.append(" ORDER BY ");
        if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getColumnName())) {
            if ("up".equals(lclUnitsScheduleForm.getSortBy())) {
                queryBuilder.append(lclUnitsScheduleForm.getColumnName()).append(" asc");
            } else {
                queryBuilder.append(lclUnitsScheduleForm.getColumnName()).append(" desc");
            }
        } else {
            queryBuilder.append(" pod.un_loc_code ,lsh.schedule_no ASC ");
        }
        queryBuilder.append(" LIMIT ").append(CommonUtils.isNotEmpty(lclUnitsScheduleForm.getLimit()) ? lclUnitsScheduleForm.getLimit() : 50);
        return queryBuilder.toString();
    }

    public List<ExportVoyageSearchModel> getVoyageSearch(LclUnitsScheduleForm lclUnitsScheduleForm) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(this.selectvoyageSearchQuery());
        queryBuilder.append(this.fromVoyageSearchQuery(lclUnitsScheduleForm));
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setParameter("originId", lclUnitsScheduleForm.getPortOfOriginId());
        if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getFinalDestinationId())) {
            query.setParameter("destinationId", lclUnitsScheduleForm.getFinalDestinationId());
        }
        query.setResultTransformer(Transformers.aliasToBean(ExportVoyageSearchModel.class));
        query.addScalar("ssHeaderId", StringType.INSTANCE);
        query.addScalar("voyageStatus", StringType.INSTANCE);
        query.addScalar("scheduleNo", StringType.INSTANCE);
        query.addScalar("serviceType", StringType.INSTANCE);
        query.addScalar("serviceType", StringType.INSTANCE);
        query.addScalar("fdUnLocCode", StringType.INSTANCE);
        query.addScalar("fdName", StringType.INSTANCE);
        query.addScalar("departPierUnloc", StringType.INSTANCE);
        query.addScalar("departPier", StringType.INSTANCE);
        query.addScalar("arrivalPierUnloc", StringType.INSTANCE);
        query.addScalar("arrivalPier", StringType.INSTANCE);
        query.addScalar("loadingDeadLineDate", StringType.INSTANCE);
        query.addScalar("etaSailDate", StringType.INSTANCE);
        query.addScalar("etaPodDate", StringType.INSTANCE);
        query.addScalar("totaltransPod", StringType.INSTANCE);
        query.addScalar("totaltransFd", StringType.INSTANCE);
        query.addScalar("vesselName", StringType.INSTANCE);
        query.addScalar("ssVoyage", StringType.INSTANCE);
        query.addScalar("carrierName", StringType.INSTANCE);
        query.addScalar("carrierAcctNo", StringType.INSTANCE);
        query.addScalar("sealNo", StringType.INSTANCE);
        query.addScalar("unitNo", StringType.INSTANCE);
        query.addScalar("unitSize", StringType.INSTANCE);
        query.addScalar("numberDrs", StringType.INSTANCE);
        query.addScalar("loadedBy", StringType.INSTANCE);
        query.addScalar("doorLocation", StringType.INSTANCE);
        query.addScalar("dispoCode", StringType.INSTANCE);
        query.addScalar("dispoDesc", StringType.INSTANCE);
        query.addScalar("isInbond", StringType.INSTANCE);
        query.addScalar("dispoDesc", StringType.INSTANCE);
        query.addScalar("isHazmat", StringType.INSTANCE);
        query.addScalar("totalWeightMetric", BigDecimalType.INSTANCE);
        query.addScalar("totalVolumeMetric", BigDecimalType.INSTANCE);
        query.addScalar("unitSizeShortDesc", StringType.INSTANCE);
        return query.list();
    }

    public void updateUnitSsAssociatedwithMasterBl(Long headerId, String currrentspBookingNo, String previousspBookingNo) throws Exception {
        SQLQuery query = getCurrentSession().createSQLQuery("UPDATE  lcl_unit_ss lus SET lus.sp_booking_no =:currrentspBookingNo WHERE lus.ss_header_id =:headerId  AND  "
                + "lus.`sp_booking_no` =:previousspBookingNo");
        query.setLong("headerId", headerId);
        query.setParameter("currrentspBookingNo", currrentspBookingNo.toUpperCase());
        query.setParameter("previousspBookingNo", previousspBookingNo);
        query.executeUpdate();
    }

    public String getINTRCargo(int pol, int pod, Long unitSsId, List fileList) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT GROUP_CONCAT(distinct fn.fileId) FROM (SELECT distinct bkp.file_number_id AS fileId FROM lcl_booking_piece bkp ");
        if (null != unitSsId) {
            sb.append(" Left JOIN  lcl_booking_piece_unit bpu ON  bpu.`booking_piece_id` = bkp.`id`   ");
            sb.append(" JOIN lcl_unit_ss luss ON luss.id = bpu.`lcl_unit_ss_id` WHERE luss.id =:unitSsId ) fn");
        } else {
            sb.append(" where bkp.file_number_id in(:fileList) )fn");
        }
        sb.append(" JOIN  lcl_booking b ON b.`file_number_id` = fn.fileId ");
        sb.append(" JOIN lcl_file_number f  ON f.`id` = b.`file_number_id` ");
        sb.append(" JOIN lcl_booking_piece bp     ON bp.`file_number_id` = f.`id`   JOIN lcl_booking_dispo dispo ");
        sb.append(" ON b.`file_number_id` = dispo.`file_number_id`   JOIN disposition d     ON dispo.`disposition_id` = d.id ");
        sb.append(" AND d.`elite_code` =  (SELECT   d.`elite_code` FROM   lcl_booking_dispo dispo   JOIN disposition d  ");
        sb.append(" ON d.`id` = dispo.`disposition_id` WHERE dispo.`file_number_id` = b.`file_number_id` ORDER BY dispo.id DESC LIMIT 1)  ");
        sb.append(" WHERE  b.`pol_id` =:pol  AND  b.`pod_id` =:pod  AND d.`elite_code` = 'INTR' ");
        Query queryObject = getCurrentSession().createSQLQuery(sb.toString());
        if (null != unitSsId) {
            queryObject.setLong("unitSsId", unitSsId);
        } else {
            queryObject.setParameterList("fileList", fileList);
        }
        queryObject.setInteger("pol", pol);
        queryObject.setInteger("pod", pod);
        return (String) queryObject.uniqueResult();
    }

    public String toArriveBookingList(Long unitssId, int poo, int pol, List fileList) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT GROUP_CONCAT(distinct fn.fileId) FROM (SELECT distinct bkp.file_number_id AS fileId FROM lcl_booking_piece bkp ");
        sb.append(" join lcl_booking b on b.file_number_id = bkp.file_number_id  ");
        if (null != unitssId) {
            sb.append("  Left JOIN  lcl_booking_piece_unit bpu ON  bpu.`booking_piece_id` = bkp.`id`  JOIN lcl_unit_ss luss ON luss.id = bpu.`lcl_unit_ss_id` ");
        }
        sb.append(getBookingDispo());
        sb.append(" WHERE b.poo_id =:poo AND b.`pol_id` =:pol   ");
        if (null != unitssId) {
            sb.append(" and luss.id =:unitSsId ");
        } else {
            sb.append(" and bkp.file_number_id in(:fileList) ");
        }
        sb.append(" AND d.`elite_code` = 'OBKG' ) fn  ");
        Query queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setInteger("poo", poo);
        queryObject.setInteger("pol", pol);
        if (null != unitssId) {
            queryObject.setLong("unitSsId", unitssId);
        } else {
            queryObject.setParameterList("fileList", fileList);
        }
        return (String) queryObject.uniqueResult();
    }

    public String getGeneralCargoBkgList(Long unitssId, String serviceType, List fileList) throws Exception {
        StringBuilder sb = new StringBuilder();

        String secure_cargo = getSecureCargoBkgList(unitssId, serviceType, fileList);
        String ups_cargo = getUPSCargoBkgList(unitssId, serviceType, fileList);
        String hazmat_cargo = getHazmatCargoBkgList(unitssId, serviceType, fileList);

        secure_cargo = null != secure_cargo ? secure_cargo + "," : "";
        ups_cargo = null != ups_cargo ? ups_cargo + "," : "";
        hazmat_cargo = null != hazmat_cargo ? hazmat_cargo : "";

        String booking_ids = secure_cargo + "" + ups_cargo + "" + hazmat_cargo;

        List secure_ups_bkg = !"".equalsIgnoreCase(booking_ids) ? Arrays.asList(booking_ids.split(",")) : new ArrayList();

        sb.append(" SELECT GROUP_CONCAT(distinct fn.fileId) FROM (SELECT distinct bkp.file_number_id AS fileId FROM lcl_booking_piece bkp ");
        if (null != unitssId) {
            sb.append(" Left JOIN  lcl_booking_piece_unit bpu ON  bpu.`booking_piece_id` = bkp.`id`   ");
            sb.append(" JOIN lcl_unit_ss luss ON luss.id = bpu.`lcl_unit_ss_id` ");
        }
        sb.append(getBookingDispo());
        if (null != unitssId) {
            sb.append(" WHERE luss.id =:unitSsId ");
        } else {
            sb.append(" WHERE bkp.file_number_id in(:fileList) ");
        }
        if ("E".equalsIgnoreCase(serviceType) || "C".equalsIgnoreCase(serviceType)) {
            sb.append(" AND d.`elite_code` != 'INTR' ");
        } else if ("N".equalsIgnoreCase(serviceType)) {
            sb.append(" and d.`elite_code` != 'OBKG'");
        }
        sb.append(!secure_ups_bkg.isEmpty() ? " AND bkp.file_number_id NOT IN (:BkgId)" : "").append(") fn");
        Query queryObject = getCurrentSession().createSQLQuery(sb.toString());
        if (null != unitssId) {
            queryObject.setLong("unitSsId", unitssId);
        } else {
            queryObject.setParameterList("fileList", fileList);
        }
        if (!secure_ups_bkg.isEmpty()) {
            queryObject.setParameterList("BkgId", secure_ups_bkg);
        }
        return (String) queryObject.uniqueResult();
    }

    public String getPickedCargoBkgList(Long unitssId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT GROUP_CONCAT(distinct fn.fileId) FROM (SELECT distinct bkp.file_number_id AS fileId FROM lcl_booking_piece bkp ");
        sb.append(" Left JOIN  lcl_booking_piece_unit bpu ON  bpu.`booking_piece_id` = bkp.`id`   ");
        sb.append(" JOIN lcl_unit_ss luss ON luss.id = bpu.`lcl_unit_ss_id` WHERE luss.id =:unitSsId) fn");
        Query queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setLong("unitSsId", unitssId);
        return (String) queryObject.uniqueResult();
    }

    public String getHazmatCargoBkgList(Long unitssId, String serviceType, List fileList) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT GROUP_CONCAT(distinct fn.fileId) FROM (SELECT distinct bkp.file_number_id AS fileId FROM lcl_booking_piece bkp ");
        if (unitssId != null) {
            sb.append(" Left JOIN  lcl_booking_piece_unit bpu ON  bpu.`booking_piece_id` = bkp.`id`   ");
            sb.append(" JOIN lcl_unit_ss luss ON luss.id = bpu.`lcl_unit_ss_id` ");
        }
        sb.append(getBookingDispo());
        sb.append(" WHERE  bkp.hazmat = 1  ");
        if (null != unitssId) {
            sb.append(" and luss.id =:unitSsId ");
        } else {
            sb.append(" and bkp.file_number_id IN(:fileList) ");
        }
        if ("E".equalsIgnoreCase(serviceType)) {
            sb.append(" AND d.`elite_code` != 'INTR' ");
        } else if ("N".equalsIgnoreCase(serviceType)) {
            sb.append(" and d.`elite_code` != 'OBKG' ");
        }
        sb.append(" ) fn");
        Query queryObject = getCurrentSession().createSQLQuery(sb.toString());
        if (null != unitssId) {
            queryObject.setLong("unitSsId", unitssId);
        } else {
            queryObject.setParameterList("fileList", fileList);
        }
        return (String) queryObject.uniqueResult();
    }

    public String getSecureCargoBkgList(Long unitssId, String serviceType, List<String> fileList) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT GROUP_CONCAT(distinct fn.fileId) FROM (SELECT distinct bkp.file_number_id AS fileId FROM lcl_booking_piece bkp ");
        sb.append(getBookingDispo());
        if (null != unitssId) {
            sb.append(" Left JOIN  lcl_booking_piece_unit bpu ON  bpu.`booking_piece_id` = bkp.`id`   ");
            sb.append(" JOIN lcl_unit_ss luss ON luss.id = bpu.`lcl_unit_ss_id` ");
        }
        sb.append(" JOIN  lcl_booking_piece_whse  bkw ON bkw.id = (SELECT bw.id FROM lcl_booking_piece_whse bw ");
        sb.append(" WHERE bw.`booking_piece_id` = bkp.`id`AND bw.location <> ''  order by bw.id desc LIMIT 1) ");
        sb.append(" AND ( bkw.`location`  LIKE 's%' AND  bkw.`location` NOT LIKE 'r%')  WHERE  bkp.hazmat = 0  ");
        if (null != unitssId) {
            sb.append(" and luss.id =:unitSsId ");
        } else {
            sb.append(" and bkp.file_number_id IN(:fileList) ");
        }
        if ("E".equalsIgnoreCase(serviceType)) {
            sb.append(" AND d.`elite_code` != 'INTR' ");
        } else if ("N".equalsIgnoreCase(serviceType)) {
            sb.append(" and d.`elite_code` != 'OBKG' ");
        }
        sb.append(" ) fn");
        Query queryObject = getCurrentSession().createSQLQuery(sb.toString());
        if (null != unitssId) {
            queryObject.setLong("unitSsId", unitssId);
        } else {
            queryObject.setParameterList("fileList", fileList);
        }
        return (String) queryObject.uniqueResult();
    }

    public String getBookingDispo() {
        StringBuilder sb = new StringBuilder();
        sb.append(" JOIN lcl_booking_dispo dispo ON bkp.`file_number_id` = dispo.`file_number_id`   JOIN disposition d ON dispo.`disposition_id` = d.id ");
        sb.append(" AND d.`elite_code` = (SELECT   d.`elite_code` FROM   lcl_booking_dispo dispo   JOIN disposition d  ");
        sb.append(" ON d.`id` = dispo.`disposition_id` WHERE dispo.`file_number_id` = bkp.`file_number_id` ORDER BY dispo.id DESC LIMIT 1)  ");
        return sb.toString();
    }

    public String getUPSCargoBkgList(Long unitssId, String serviceType, List<String> fileList) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT GROUP_CONCAT(distinct fn.fileId) FROM (SELECT distinct bkp.file_number_id AS fileId FROM lcl_booking_piece bkp ");
        if (null != unitssId) {
            sb.append(" Left JOIN  lcl_booking_piece_unit bpu ON  bpu.`booking_piece_id` = bkp.`id`   ");
            sb.append(" JOIN lcl_unit_ss luss ON luss.id = bpu.`lcl_unit_ss_id` ");
        }
        sb.append(" JOIN lcl_booking_dispo dispo ON bkp.`file_number_id` = dispo.`file_number_id`   JOIN disposition d ON dispo.`disposition_id` = d.id ");
        sb.append(" AND d.`elite_code` = (SELECT   d.`elite_code` FROM   lcl_booking_dispo dispo   JOIN disposition d  ");
        sb.append(" ON d.`id` = dispo.`disposition_id` WHERE dispo.`file_number_id` = bkp.`file_number_id` ORDER BY dispo.id DESC LIMIT 1)  ");
        sb.append(" JOIN  lcl_booking_piece_whse  bkw ON bkw.id = (SELECT bw.id FROM lcl_booking_piece_whse bw  ");
        sb.append(" WHERE bw.`booking_piece_id` = bkp.`id`AND bw.location <> '' order by bw.id desc LIMIT 1) ");
        sb.append(" AND ( bkw.`location` NOT LIKE 's%' AND  bkw.`location`  LIKE 'r%')  WHERE  bkp.hazmat = 0  ");
        if (null != unitssId) {
            sb.append(" and luss.id =:unitSsId ");
        } else {
            sb.append(" and bkp.file_number_id IN(:fileList) ");
        }
        if ("E".equalsIgnoreCase(serviceType)) {
            sb.append(" AND d.`elite_code` != 'INTR' ");
        } else if ("N".equalsIgnoreCase(serviceType)) {
            sb.append(" and d.`elite_code` != 'OBKG' ");
        }
        sb.append(" ) fn");
        Query queryObject = getCurrentSession().createSQLQuery(sb.toString());
        if (null != unitssId) {
            queryObject.setLong("unitSsId", unitssId);
        } else {
            queryObject.setParameterList("fileList", fileList);
        }
        return (String) queryObject.uniqueResult();
    }

    public List<ExportVoyageSearchModel> getViewAllList(Integer pooId, Integer fdId,
            String serviceType, String transMode) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT unit.id as ssHeaderId,unit.scheduleNo as scheduleNo,unit.dataSource as dataSource, ");
        queryBuilder.append(" unit.unitNo as unitNo,unit.origin as pooName,unit.destn as fdName, ");
        queryBuilder.append(" unit.dispoDesc as dispoDesc,unit.dispoCode as dispoCode,unit.inBond as isInbond , ");
        queryBuilder.append(" unit.hazNo as isHazmat,unit.polLrd as polLrdDate,unit.etdSail as etaSailDate,unit.unitSize,  ");
        queryBuilder.append(" unit.unitTrackingNotes ");
        queryBuilder.append(" FROM (SELECT  ");
        queryBuilder.append(" ss.id AS id,ss.schedule_no AS scheduleNo, ");
        queryBuilder.append(" ss.datasource AS dataSource,u.unit_no AS unitNo, ");
        queryBuilder.append(" UnLocationGetNameStateCntryByID (ss.origin_id) AS origin, ");
        queryBuilder.append(" UnLocationGetNameStateCntryByID (ss.destination_id) AS destn, ");
        queryBuilder.append(" dispo.description AS dispoDesc,dispo.elite_code AS dispoCode, ");
        queryBuilder.append(" inb.inbond_no AS inBond,bhz.un_hazmat_no AS hazNo, ");
        queryBuilder.append(" DATE_FORMAT(lssd.general_lrdt, '%d-%b-%Y %h:%i %p') AS polLrd, ");
        queryBuilder.append(" DATE_FORMAT(lssd.std, '%d-%b-%Y') AS etdSail,  ");
        queryBuilder.append(" (SELECT description FROM unit_type WHERE id=u.unit_type_id) AS unitSize,u.remarks AS unitTrackingNotes ");
        queryBuilder.append(" FROM lcl_ss_header ss  ");
        queryBuilder.append(" JOIN lcl_ss_detail lssd ON (ss.id = lssd.ss_header_id) ");
        queryBuilder.append(" LEFT JOIN lcl_unit_ss us ON (ss.id = us.ss_header_id) ");
        queryBuilder.append(" LEFT JOIN lcl_unit u ON (us.unit_id = u.id) ");
        queryBuilder.append(" LEFT JOIN lcl_unit_ss_dispo ssd ON (u.id = ssd.unit_id) ");
        queryBuilder.append(" LEFT JOIN disposition dispo ON (ssd.disposition_id = dispo.id) ");
        queryBuilder.append(" LEFT JOIN lcl_booking_piece_unit bpu ON (us.id = bpu.lcl_unit_ss_id) ");
        queryBuilder.append(" LEFT JOIN lcl_booking_piece bp ON (bpu.booking_piece_id = bp.id) ");
        queryBuilder.append(" LEFT JOIN lcl_inbond inb ON (bp.file_number_id = inb.file_number_id)  ");
        queryBuilder.append(" LEFT JOIN lcl_booking_hazmat bhz ON (bp.file_number_id = bhz.file_number_id) ");
        // queryBuilder.append("  us.status <> 'C' AND   ");
        queryBuilder.append(" WHERE ss.datasource = 'L' and ss.status <> 'V' AND ss.service_type = :serviceType ");
        queryBuilder.append(" AND ss.trans_mode = :transMode ");
        if (CommonUtils.isNotEmpty(pooId)) {
            queryBuilder.append(" AND ss.origin_id=:pooId  ");
        }
        if (CommonUtils.isNotEmpty(fdId)) {
            queryBuilder.append(" AND ss.destination_id=:fdId ");
        }
        // queryBuilder.append(" GROUP BY unitNo  ");
        queryBuilder.append(" ORDER BY lssd.std DESC) unit  ");
        SQLQuery queryObj = getCurrentSession().createSQLQuery(queryBuilder.toString());
        if (CommonUtils.isNotEmpty(pooId)) {
            queryObj.setParameter("pooId", pooId);
        }
        if (CommonUtils.isNotEmpty(fdId)) {
            queryObj.setParameter("fdId", fdId);
        }
        queryObj.setParameter("serviceType", serviceType);
        queryObj.setParameter("transMode", transMode);
        queryObj.setResultTransformer(Transformers.aliasToBean(ExportVoyageSearchModel.class));
        queryObj.addScalar("ssHeaderId", StringType.INSTANCE);
        queryObj.addScalar("scheduleNo", StringType.INSTANCE);
        queryObj.addScalar("dataSource", StringType.INSTANCE);
        queryObj.addScalar("unitNo", StringType.INSTANCE);
        queryObj.addScalar("pooName", StringType.INSTANCE);
        queryObj.addScalar("fdName", StringType.INSTANCE);
        queryObj.addScalar("dispoDesc", StringType.INSTANCE);
        queryObj.addScalar("dispoCode", StringType.INSTANCE);
        queryObj.addScalar("isInbond", StringType.INSTANCE);
        queryObj.addScalar("isHazmat", StringType.INSTANCE);
        queryObj.addScalar("polLrdDate", StringType.INSTANCE);
        queryObj.addScalar("etaSailDate", StringType.INSTANCE);
        queryObj.addScalar("unitSize", StringType.INSTANCE);
        queryObj.addScalar("unitTrackingNotes", StringType.INSTANCE);
        return queryObj.list();
    }

    public List<ExportVoyageSearchModel> searchByVoyageList(LclUnitsScheduleForm lclUnitsScheduleForm) throws Exception {
        List<ExportVoyageSearchModel> voyageList = new ArrayList<ExportVoyageSearchModel>();
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT lclssh.id AS ssHeaderId,lclssh.service_type as serviceType,lclssd.id AS ssDeatailId, ");
        queryStr.append("  lclssh.schedule_no AS scheduleNo,lclssd.sp_acct_no AS carrierAcctNo, ");
        queryStr.append("  (SELECT acct_name FROM trading_partner WHERE acct_no = lclssd.sp_acct_no LIMIT 1) AS carrierName, ");
        queryStr.append("  (SELECT COUNT(*) FROM lcl_unit lu LEFT JOIN lcl_unit_ss luss ON luss.unit_id = lu.id WHERE lclssh.id = luss.ss_header_id) AS units, ");
        queryStr.append("  (SELECT COUNT(*) FROM lcl_unit lu LEFT JOIN lcl_unit_ss luss ON luss.unit_id = lu.id ");
        queryStr.append("   WHERE lclssh.id = luss.ss_header_id and luss.status ='M') as manifestUnitCount, ");
        queryStr.append("  (SELECT COUNT(*) FROM lcl_unit lu LEFT JOIN lcl_unit_ss luss ON luss.unit_id = lu.id ");
        queryStr.append("   WHERE lclssh.id = luss.ss_header_id and luss.cob = 1 ) as cobUnitCount, ");
        queryStr.append(getAppendQueryForUnitNo());
        queryStr.append("  lclssd.sp_reference_name AS vesselName,lclssd.sp_reference_no AS ssVoyage, ");
        queryStr.append("  UnLocationGetCodeByID (lclssd.departure_id) AS departPierUnloc,");
        queryStr.append("  UnLocationGetNameStateCntryByID (lclssd.departure_id) AS departPier,");
        queryStr.append("  UnLocationGetCodeByID (lclssd.arrival_id) AS arrivalPierUnloc,");
        queryStr.append("  UnLocationGetNameStateCntryByID (lclssd.arrival_id) AS arrivalPier,");
        queryStr.append("  lclssd.relay_lrd_override AS lrdOverride,lclssd.relay_lrd_override AS lrdOverrideDays, ");
        queryStr.append("  DATE_FORMAT(lclssd.general_lrdt, '%d-%b-%Y') AS generalLrdt,lclssd.general_lrdt as polLrdDates,");
        queryStr.append("  DATE_FORMAT(lclssd.std, '%d-%b-%Y') AS etaSailDate,");
        queryStr.append("  DATE_FORMAT(lclssd.sta, '%d-%b-%Y') AS etaPodDate,lclssd.sta as etaPodDates,");
        queryStr.append("  IF(lclssd.relay_tt_override IS NOT NULL AND lclssd.relay_tt_override > 0 ,lclssd.relay_tt_override,lr.transit_time) AS LRT,lclssh.datasource,");
        queryStr.append("  UserDetailsGetLoginNameByID (lclssh.entered_by_user_id) AS createdBy,");
        queryStr.append("  UserDetailsGetLoginNameByID (lclssh.owner_user_id) AS voyOwner,");
        queryStr.append("  IF(lclssh.closed_by_user_id IS NOT NULL,'CL','') AS closedStatus,   ");
        queryStr.append("  IF(lclssh.audited_by_user_id IS NOT NULL,'AU','') AS auditedStatus,   ");
        queryStr.append("  IF(lclssh.closed_by_user_id IS NOT NULL,UserDetailsGetLoginNameByID (lclssh.closed_by_user_id),'') AS closedBy,   ");
        queryStr.append("  IF(lclssh.audited_by_user_id IS NOT NULL,UserDetailsGetLoginNameByID (lclssh.audited_by_user_id),'') AS auditedBy,   ");
        queryStr.append("  IF(lclssh.closed_datetime IS NOT NULL,DATE_FORMAT(lclssh.closed_datetime, '%d-%b-%Y %T'),'') AS closedOn,   ");
        queryStr.append("  IF(lclssh.audited_datetime IS NOT NULL,DATE_FORMAT(lclssh.audited_datetime, '%d-%b-%Y %T'),'') AS auditedOn,   ");
        queryStr.append("  IF(lclssh.closed_remarks IS NOT NULL,lclssh.closed_remarks,'') AS closedRemarks,   ");
        queryStr.append("  IF(lclssh.audited_remarks IS NOT NULL,lclssh.audited_remarks,'') AS auditedRemarks,lclssd.std as etaSailDates,   ");
        queryStr.append("  lrp.transit_time AS LRPT,lrp.co_dow,lrf.transit_time AS LFT, ");
        queryStr.append("  (SELECT MAX(CASE WHEN lclssd.sta = luss.cob_datetime THEN 1 ");
        queryStr.append("  WHEN (SELECT COUNT(*) FROM lcl_unit_ss lus  WHERE lus.`ss_header_id` = lclssh.id)  =  ");
        queryStr.append("  (SELECT COUNT(*) FROM lcl_unit_ss lus WHERE lus.`ss_header_id` = lclssh.id AND lus.cob_datetime IS NULL) THEN 2");
        queryStr.append("  WHEN (SELECT COUNT(*) FROM lcl_unit_ss lus ");
        queryStr.append("  WHERE lus.`ss_header_id` = lclssh.id)>1 AND luss.cob_datetime IS NULL THEN 3 ");
        queryStr.append("  WHEN lclssd.sta <> luss.cob_datetime THEN 4 ELSE 0 END ");
        queryStr.append("  ) AS vETA FROM lcl_unit_ss luss WHERE luss.ss_header_id = lclssh.id) AS verifiedEta ");
        queryStr.append("  FROM  lcl_ss_header lclssh ");
        queryStr.append("  JOIN lcl_ss_detail lclssd ON (lclssh.id = lclssd.ss_header_id) ");
        if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getUnitNo())) {
            queryStr.append("  LEFT JOIN lcl_unit_ss luss ON (luss.ss_header_id = lclssh.id) ");
            queryStr.append("  LEFT JOIN lcl_unit lu ON (lu.id = luss.unit_id) ");
        }
        if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getCfclAcctNo())) {
            queryStr.append(" join lcl_ss_exports lsexp  on  ");
            queryStr.append("(lsexp.ss_header_id = lclssh.id and  lsexp.export_agent_acct_no =:cfclAcct)");
        }
        queryStr.append(" LEFT JOIN lcl_relay lr ON (lclssh.origin_id = lr.pol_id AND lclssh.destination_id = lr.pod_id) ");
        queryStr.append(" LEFT JOIN lcl_relay_poo lrp ON (lrp.poo_id = lclssh.origin_id AND lrp.relay_id = lr.id) ");
        queryStr.append(" LEFT JOIN lcl_relay_fd lrf ON (lrf.fd_id = lclssh.destination_id AND lrf.relay_id = lr.id) ");
        queryStr.append(" WHERE ");
        queryStr.append(" lclssh.trans_mode = 'V'  ");
        queryStr.append(" AND lclssh.status <> 'V'  ");
        if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getServiceType())) {
            queryStr.append(" AND lclssh.service_type =:serviceType ");
        }
        if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getPortOfOriginId())) {
            queryStr.append(" AND lclssh.origin_id=:originId ");
        }
        if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getFinalDestinationId())) {
            queryStr.append(" AND lclssh.destination_id=:destinationId ");
        }
        if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getVoyageNo())) {
            queryStr.append(" AND lclssh.schedule_no=:scheduleNo");
        }
        if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getUnitNo())) {
            queryStr.append(" AND lu.unit_no=:unitNo ");
        }
        queryStr.append(" GROUP BY lclssh.id order by  ");

        if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getColumnName())) {
            if ("up".equals(lclUnitsScheduleForm.getSortBy())) {
                queryStr.append(lclUnitsScheduleForm.getColumnName()).append(" asc");
            } else {
                queryStr.append(lclUnitsScheduleForm.getColumnName()).append(" desc");
            }
        } else {
            queryStr.append(" lclssd.std DESC  ");
        }
        queryStr.append("  LIMIT ");
        if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getLimit())) {
            queryStr.append(lclUnitsScheduleForm.getLimit());
        } else {
            queryStr.append("50");
        }
        SQLQuery queryObj = getSession().createSQLQuery(queryStr.toString());
        if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getServiceType())) {
            queryObj.setParameter("serviceType", lclUnitsScheduleForm.getServiceType());
        }
        if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getPortOfOriginId())) {
            queryObj.setParameter("originId", lclUnitsScheduleForm.getPortOfOriginId());
        }
        if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getFinalDestinationId())) {
            queryObj.setParameter("destinationId", lclUnitsScheduleForm.getFinalDestinationId());
        }
        if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getVoyageNo())) {
            queryObj.setParameter("scheduleNo", lclUnitsScheduleForm.getVoyageNo());
        }
        if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getUnitNo())) {
            queryObj.setParameter("unitNo", lclUnitsScheduleForm.getUnitNo());
        }
        if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getCfclAcctNo())) {
            queryObj.setParameter("cfclAcct", lclUnitsScheduleForm.getCfclAcctNo());
        }
        List queryList = queryObj.list();
        for (Object obj : queryList) {
            Object[] row = (Object[]) obj;
            ExportVoyageSearchModel model = new ExportVoyageSearchModel(row, lclUnitsScheduleForm);
            voyageList.add(model);
        }
        return voyageList;
    }

    public List<ExportVoyageSearchModel> getInandVoyageList(LclUnitsScheduleForm lclUnitsScheduleForm) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT lclssh.id AS ssHeaderId,lclssd.id AS ssDetailId,lclssh.service_type as serviceType,");
        queryStr.append(" lclssh.schedule_no AS scheduleNo,lclssd.sp_acct_no AS carrierAcctNo,");
        queryStr.append(" (SELECT acct_name FROM trading_partner WHERE acct_no = lclssd.sp_acct_no LIMIT 1) AS carrierName,");
        queryStr.append(" (SELECT COUNT(*) FROM lcl_unit lu LEFT JOIN lcl_unit_ss luss ON luss.unit_id = lu.id ");
        queryStr.append(" WHERE lclssh.id = luss.ss_header_id) AS unitcount,");
        queryStr.append(getAppendQueryForUnitNo());
        queryStr.append(" lclssd.sp_reference_name AS vesselName,lclssd.sp_reference_no AS ssVoyage,");
        queryStr.append(" UnLocationGetCodeByID (lclssd.departure_id) AS departPierUnloc,");
        queryStr.append(" UnLocationGetNameStateCntryByID (lclssd.departure_id) AS departPier,");
        queryStr.append(" UnLocationGetCodeByID (lclssd.arrival_id) AS arrivalPierUnloc,");
        queryStr.append(" UnLocationGetNameStateCntryByID (lclssd.arrival_id) AS arrivalPier,");
        queryStr.append(" DATE_FORMAT(lclssd.general_lrdt, '%d-%b-%Y') AS polLrdDate,lclssd.general_lrdt as polLrdDates,");
        queryStr.append(" lclssd.sta as etaPodDates,lclssd.std as etaSailDates,");
        queryStr.append(" DATE_FORMAT(lclssd.std, '%d-%b-%Y') AS etaSailDate,DATE_FORMAT(lclssd.sta, '%d-%b-%Y') AS etaPodDate,");
        queryStr.append(" lclssh.datasource AS dataSource,");
        queryStr.append(" UserDetailsGetLoginNameByID (lclssh.entered_by_user_id) AS createdBy,");
        queryStr.append(" UserDetailsGetLoginNameByID (lclssh.owner_user_id) AS voyOwner,");
        queryStr.append("  (SELECT COUNT(*) FROM lcl_unit lu LEFT JOIN lcl_unit_ss luss ON luss.unit_id = lu.id ");
        queryStr.append("   WHERE lclssh.id = luss.ss_header_id and luss.status ='M') as manifestUnitCount, ");
        queryStr.append("  (SELECT COUNT(*) FROM lcl_unit lu LEFT JOIN lcl_unit_ss luss ON luss.unit_id = lu.id ");
        queryStr.append("   WHERE lclssh.id = luss.ss_header_id and luss.cob = 1 ) as cobUnitCount, ");
        queryStr.append("  (SELECT MAX(CASE WHEN lclssd.sta = luss.cob_datetime THEN 1 ");
        queryStr.append("  WHEN (SELECT COUNT(*) FROM lcl_unit_ss lus  WHERE lus.`ss_header_id` = lclssh.id)  =  ");
        queryStr.append("  (SELECT COUNT(*) FROM lcl_unit_ss lus WHERE lus.`ss_header_id` = lclssh.id AND lus.cob_datetime IS NULL) THEN 2");
        queryStr.append("  WHEN (SELECT COUNT(*) FROM lcl_unit_ss lus ");
        queryStr.append("  WHERE lus.`ss_header_id` = lclssh.id)>1 AND luss.cob_datetime IS NULL THEN 3 ");
        queryStr.append("  WHEN lclssd.sta <> luss.cob_datetime THEN 4 ELSE 0 END ");
        queryStr.append("  ) AS vETA FROM lcl_unit_ss luss WHERE luss.ss_header_id = lclssh.id) AS verifiedEta ");
        queryStr.append(" FROM lcl_ss_header lclssh ");
        queryStr.append(" JOIN lcl_ss_detail lclssd ON (lclssh.id = lclssd.ss_header_id)");
        queryStr.append(" WHERE lclssh.trans_mode = 'T' AND lclssh.service_type = 'N' ");
        queryStr.append(" AND lclssh.status <> 'V'  ");
        if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getPortOfOriginId())) {
            queryStr.append(" AND lclssh.origin_id=:originId ");
        }
        if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getFinalDestinationId())) {
            queryStr.append(" AND lclssh.destination_id=:destinationId ");
        }
        if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getVoyageNo())) {
            queryStr.append(" AND lclssh.schedule_no=:scheduleNo");
        }
        queryStr.append(" GROUP BY lclssh.id ORDER BY ");
        if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getColumnName())) {
            if ("up".equals(lclUnitsScheduleForm.getSortBy())) {
                queryStr.append(lclUnitsScheduleForm.getColumnName()).append(" asc");
            } else {
                queryStr.append(lclUnitsScheduleForm.getColumnName()).append(" desc");
            }
        } else {
            queryStr.append(" lclssd.std DESC  ");
        }
        queryStr.append("  LIMIT ");
        if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getLimit())) {
            queryStr.append(lclUnitsScheduleForm.getLimit());
        } else {
            queryStr.append("50");
        }
        SQLQuery query = getSession().createSQLQuery(queryStr.toString());
        if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getPortOfOriginId())) {
            query.setParameter("originId", lclUnitsScheduleForm.getPortOfOriginId());
        }
        if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getFinalDestinationId())) {
            query.setParameter("destinationId", lclUnitsScheduleForm.getFinalDestinationId());
        }
        if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getVoyageNo())) {
            query.setParameter("scheduleNo", lclUnitsScheduleForm.getVoyageNo());
        }
        query.setResultTransformer(Transformers.aliasToBean(ExportVoyageSearchModel.class));
        query.addScalar("ssHeaderId", StringType.INSTANCE);
        query.addScalar("ssDetailId", StringType.INSTANCE);
        query.addScalar("serviceType", StringType.INSTANCE);
        query.addScalar("scheduleNo", StringType.INSTANCE);
        query.addScalar("carrierName", StringType.INSTANCE);
        query.addScalar("carrierAcctNo", StringType.INSTANCE);
        query.addScalar("unitcount", StringType.INSTANCE);
        query.addScalar("unitNo", StringType.INSTANCE);
        query.addScalar("vesselName", StringType.INSTANCE);
        query.addScalar("ssVoyage", StringType.INSTANCE);
        query.addScalar("departPierUnloc", StringType.INSTANCE);
        query.addScalar("departPier", StringType.INSTANCE);
        query.addScalar("arrivalPierUnloc", StringType.INSTANCE);
        query.addScalar("arrivalPier", StringType.INSTANCE);
        query.addScalar("polLrdDate", StringType.INSTANCE);
        query.addScalar("etaSailDate", StringType.INSTANCE);
        query.addScalar("etaPodDate", StringType.INSTANCE);
        query.addScalar("createdBy", StringType.INSTANCE);
        query.addScalar("voyOwner", StringType.INSTANCE);
        query.addScalar("dataSource", StringType.INSTANCE);
        query.addScalar("manifestUnitCount", StringType.INSTANCE);
        query.addScalar("cobUnitCount", StringType.INSTANCE);
        query.addScalar("verifiedEta", IntegerType.INSTANCE);
        return query.list();
    }

    public List<ExportVoyageSearchModel> getMultiUnitSearchList(LclUnitsScheduleForm lclUnitsScheduleForm) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append("SELECT lsh.id AS ssHeaderId,lsh.schedule_no as scheduleNo,");
        queryStr.append("  UnLocationGetCodeByID (lsh.destination_id) AS departPierUnloc,");
        queryStr.append("UnLocationGetCodeByID(lsh.origin_id) AS arrivalPierUnloc,");
        queryStr.append("UnLocationGetNameStateCntryByID (lsh.origin_id) AS arrivalPier,");
        queryStr.append("UnLocationGetNameStateCntryByID (lsh.destination_id) AS departPier,");
        queryStr.append("lsh.origin_id as pooId,lsh.destination_id as fdId ");
        queryStr.append(" FROM lcl_unit lu  ");
        queryStr.append(" JOIN lcl_unit_ss luss ON luss.unit_id = lu.id  ");
        queryStr.append("JOIN lcl_ss_header lsh ON lsh.id=luss.ss_header_id");
        queryStr.append(" WHERE luss.unit_id=:unitId ");
        //queryStr.append("AND lsh.service_type =:serviceType");
        SQLQuery query = getSession().createSQLQuery(queryStr.toString());
        query.setParameter("unitId", lclUnitsScheduleForm.getUnitId());
        //query.setParameter("serviceType", lclUnitsScheduleForm.getServiceType());
        query.setResultTransformer(Transformers.aliasToBean(ExportVoyageSearchModel.class));
        query.addScalar("ssHeaderId", StringType.INSTANCE);
        query.addScalar("scheduleNo", StringType.INSTANCE);
        query.addScalar("departPierUnloc", StringType.INSTANCE);
        query.addScalar("departPier", StringType.INSTANCE);
        query.addScalar("arrivalPierUnloc", StringType.INSTANCE);
        query.addScalar("arrivalPier", StringType.INSTANCE);
        query.addScalar("pooId", StringType.INSTANCE);
        query.addScalar("fdId", StringType.INSTANCE);
        return query.list();
    }

    public List<ExportVoyageSearchModel> searchByUnAssignUnit(String wareHouseId) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append("select lu.id AS unitId, lu.`unit_no` as unitNo,  ");
        queryStr.append(" un.description as unitSize,wa.warehsname as warehouseName, ");
        queryStr.append(" lu.hazmat_permitted AS isHazmat, lu.remarks AS unitTrackingNotes, ");
        queryStr.append("  lu.`volume_imperial`  AS totalVolumeImperial , lu.`volume_metric` AS totalWeightImperial, ");
        queryStr.append(" UserDetailsGetLoginNameByID(lu.entered_by_user_id) as createdBy, ");
        queryStr.append(" DATE_FORMAT(lu.entered_datetime, '%d-%b-%Y') AS createdDate,lu.comments AS comments ");
        queryStr.append(" from lcl_unit lu join lcl_unit_whse luw on luw.unit_id=lu.id ");
        queryStr.append(" JOIN unit_type un  ON un.id=lu.`unit_type_id` ");
        queryStr.append(" join  warehouse wa on luw.warehouse_id=wa.id  where luw.warehouse_id = :warehouseId ");
        queryStr.append(" and luw.id = (SELECT id FROM lcl_unit_whse WHERE unit_id = luw.unit_id ORDER BY id DESC LIMIT 1) ");
        queryStr.append(" AND luw.ss_header_id IS NULL  GROUP BY lu.id Desc ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setParameter("warehouseId", wareHouseId);
        query.setResultTransformer(Transformers.aliasToBean(ExportVoyageSearchModel.class));
        query.addScalar("unitId", StringType.INSTANCE);
        query.addScalar("unitNo", StringType.INSTANCE);
        query.addScalar("unitSize", StringType.INSTANCE);
        query.addScalar("warehouseName", StringType.INSTANCE);
        query.addScalar("isHazmat", StringType.INSTANCE);
        query.addScalar("unitTrackingNotes", StringType.INSTANCE);
        query.addScalar("totalVolumeImperial", BigDecimalType.INSTANCE);
        query.addScalar("totalWeightImperial", BigDecimalType.INSTANCE);
        query.addScalar("createdBy", StringType.INSTANCE);
        query.addScalar("createdDate", StringType.INSTANCE);
        query.addScalar("comments", StringType.INSTANCE);
        return query.list();
    }

    public List<ExportVoyageSearchModel> searchByUnCompleteUnit(Integer pooId, Integer fdId, String serviceType) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(this.selectvoyageSearchQuery());
        queryStr.append("   FROM ");
        queryStr.append(" lcl_ss_header lsh ");
        queryStr.append(" JOIN un_location pod ON lsh.destination_id=pod.id ");
        queryStr.append(" JOIN lcl_ss_detail lsd ");
        queryStr.append(" ON (lsh.id = lsd.ss_header_id ");
        queryStr.append(" AND lsh.status <> 'V' ");
        queryStr.append(" AND lsd.`id` = (SELECT ls.id FROM ");
        queryStr.append(" lcl_ss_detail ls ");
        queryStr.append("  WHERE ls.`ss_header_id` = lsh.id ");
        queryStr.append(" ORDER BY id DESC LIMIT 1)) ");
        queryStr.append("  JOIN lcl_unit_ss lus ");
        queryStr.append(" ON (lus.ss_header_id = lsh.id ) ");
        queryStr.append("  JOIN lcl_unit lu ON (lu.id=lus.unit_id) JOIN unit_type ut ON (ut.id = lu.unit_type_id) ");
        queryStr.append(" WHERE lsh.service_type=:serviceType and  lus.status = 'E'  ");
        if (CommonUtils.isNotEmpty(pooId)) {
            queryStr.append(" AND lsh.origin_id =:pooId ");
        }
        if (CommonUtils.isNotEmpty(fdId)) {
            queryStr.append(" AND lsh.destination_id =:fdId ");
        }
        queryStr.append(" ORDER BY pod.un_loc_code ,lsh.schedule_no ASC ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        if (CommonUtils.isNotEmpty(pooId)) {
            query.setInteger("pooId", pooId);
        }
        if (CommonUtils.isNotEmpty(fdId)) {
            query.setInteger("fdId", fdId);
        }
        query.setString("serviceType", serviceType);
        query.setResultTransformer(Transformers.aliasToBean(ExportVoyageSearchModel.class));
        query.addScalar("ssHeaderId", StringType.INSTANCE);
        query.addScalar("scheduleNo", StringType.INSTANCE);
        query.addScalar("serviceType", StringType.INSTANCE);
        query.addScalar("serviceType", StringType.INSTANCE);
        query.addScalar("fdUnLocCode", StringType.INSTANCE);
        query.addScalar("fdName", StringType.INSTANCE);
        query.addScalar("departPierUnloc", StringType.INSTANCE);
        query.addScalar("departPier", StringType.INSTANCE);
        query.addScalar("arrivalPierUnloc", StringType.INSTANCE);
        query.addScalar("arrivalPier", StringType.INSTANCE);
        query.addScalar("loadingDeadLineDate", StringType.INSTANCE);
        query.addScalar("etaSailDate", StringType.INSTANCE);
        query.addScalar("etaPodDate", StringType.INSTANCE);
        query.addScalar("totaltransPod", StringType.INSTANCE);
        query.addScalar("totaltransFd", StringType.INSTANCE);
        query.addScalar("vesselName", StringType.INSTANCE);
        query.addScalar("ssVoyage", StringType.INSTANCE);
        query.addScalar("carrierName", StringType.INSTANCE);
        query.addScalar("carrierAcctNo", StringType.INSTANCE);
        query.addScalar("sealNo", StringType.INSTANCE);
        query.addScalar("unitNo", StringType.INSTANCE);
        query.addScalar("unitSize", StringType.INSTANCE);
        query.addScalar("numberDrs", StringType.INSTANCE);
        query.addScalar("loadedBy", StringType.INSTANCE);
        query.addScalar("doorLocation", StringType.INSTANCE);
        query.addScalar("dispoCode", StringType.INSTANCE);
        query.addScalar("dispoDesc", StringType.INSTANCE);
        query.addScalar("isInbond", StringType.INSTANCE);
        query.addScalar("dispoDesc", StringType.INSTANCE);
        query.addScalar("isHazmat", StringType.INSTANCE);
        return query.list();
    }

    public List<ManifestBean> getPickedDrList(Long unitSSId) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT DISTINCT fn.fileId as fileId,fn.fileNo as fileNo,fn.blNumber AS blNo,fn.bkgPieceId AS bkgPieceId  FROM  ");
        queryStr.append(" (SELECT DISTINCT bkp.file_number_id AS fileId,lfn.file_number AS fileNo,  ");
        queryStr.append(" CONCAT_WS('-',(SELECT IF(t.unlocationcode1 <> '',RIGHT(t.unlocationcode1,3),t.trmnum)  ");
        queryStr.append(" AS terminal FROM terminal t WHERE t.trmnum=lb.billing_terminal),  ");
        queryStr.append(" IF(dest.bl_numbering = 'Y',RIGHT(dest.un_loc_code, 3),dest.un_loc_code),lfn.file_number) AS blNumber,bkp.id as bkgPieceId   ");
        queryStr.append("  FROM lcl_booking_piece bkp JOIN  lcl_booking_piece_unit bpu ON  bpu.booking_piece_id = bkp.id ");
        queryStr.append(" JOIN lcl_file_number lfn ON lfn.id=bkp.file_number_id  JOIN lcl_booking lb ON lb.file_number_id=lfn.id  ");
        queryStr.append(" JOIN un_location dest ON dest.id=lb.fd_id ");
        queryStr.append(" JOIN lcl_unit_ss luss ON luss.id = bpu.lcl_unit_ss_id WHERE luss.id =:unitSSId GROUP BY bkp.file_number_id) fn ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setLong("unitSSId", unitSSId);
        query.setResultTransformer(Transformers.aliasToBean(ManifestBean.class));
        query.addScalar("fileId", LongType.INSTANCE);
        query.addScalar("fileNo", StringType.INSTANCE);
        query.addScalar("blNo", StringType.INSTANCE);
        query.addScalar("bkgPieceId", LongType.INSTANCE);
        return query.list();
    }

    public List<Long> getAllPickedCargoBkg(Long headerId, Long UnitSsId) throws Exception {
        List queryObject = new ArrayList();
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT DISTINCT bl.`file_number_id` AS fileId ");
        sb.append(" FROM lcl_bl bl JOIN lcl_booking_piece bp ON (bl.file_number_id = getHouseBLForConsolidateDr(bp.`file_number_id`)) ");
        sb.append(" JOIN lcl_booking_piece_unit bpu ON bpu.`booking_piece_id` = bp.`id`  ");
        sb.append(" JOIN lcl_unit_ss lus ON lus.id=bpu.`lcl_unit_ss_id` where lus.ss_header_id=").append(headerId);
        if (UnitSsId != 0) {
            sb.append(" and lus.id=").append(UnitSsId);
        }
        sb.append(" order by bl.bl_seq_num asc");
        queryObject = getCurrentSession().createSQLQuery(sb.toString()).list();
        List<Long> resultList = new ArrayList<Long>();
        for (Object obj : queryObject) {
            resultList.add(Long.parseLong(obj.toString()));
        }
        return resultList;
    }

    public List getUnitList(Long headerId) throws Exception {
        List<LabelValueBean> chargeCodeList = new ArrayList<LabelValueBean>();
        StringBuilder queryString = new StringBuilder();
        queryString.append(" SELECT lus.id, lu.`unit_no` FROM lcl_unit_ss lus JOIN lcl_ss_header lsh ");
        queryString.append(" ON lus.`ss_header_id` = lsh.id JOIN lcl_unit lu ON lu.`id` = lus.`unit_id` ");
        queryString.append(" WHERE lsh.id =").append(headerId);
        List<Object[]> resultList = getCurrentSession().createSQLQuery(queryString.toString()).list();
        if (CommonUtils.isNotEmpty(resultList)) {
            for (Object[] row : resultList) {
                BigInteger unitssId = (BigInteger) row[0];
                String unitname = (String) row[1];
                if (null != row[1] && null != row[0]) {
                    chargeCodeList.add(new LabelValueBean(unitname, unitssId.toString()));
                }
            }
        }
        return chargeCodeList;
    }

    public boolean isDrAvailableInVoyage(String headerId, String unitSsId) throws Exception {
        String sb = " select if(count(*)>0,true,false) as result  from lcl_unit_ss lus join lcl_booking_piece_unit bu "
                + " on bu.lcl_unit_ss_id = lus.id join lcl_booking_piece b  on b.id= bu.booking_piece_id "
                + " join lcl_file_number f on f.id = b.file_number_id "
                + " join lcl_ss_header lsh on lsh.id = lus.ss_header_id where f.state not in ('B') and lsh.id =:headerId";
        if (!"0".equalsIgnoreCase(unitSsId)) {
            sb += " and lus.id=" + unitSsId;
        }
        SQLQuery query = getCurrentSession().createSQLQuery(sb);
        query.setParameter("headerId", headerId);
        query.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) query.uniqueResult();
    }

    public List<Long> getPickedFileId(Long unitSsId) throws Exception {
        List<Long> resultList = new ArrayList<Long>();
        String sb = "select bp.file_number_id from lcl_booking_piece_unit bpu join lcl_booking_piece bp on bp.id=bpu.booking_piece_id where bpu.lcl_unit_ss_id=:unitSsId ";
        SQLQuery query = getCurrentSession().createSQLQuery(sb);
        query.setParameter("unitSsId", unitSsId);
        for (Object obj : query.list()) {
            resultList.add(Long.parseLong(obj.toString()));
        }
        return resultList;
    }

    public LclModel getPickedVoyageByFileId(Long fileId, String serviceType) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT lsh.schedule_no as scheduleNo,lu.unit_no as unitNo ");
        queryStr.append(" FROM lcl_booking_piece lbp ");
        queryStr.append(" JOIN lcl_booking_piece_unit lbpu ON lbpu.booking_piece_id=lbp.id ");
        queryStr.append(" JOIN lcl_unit_ss lus ON lus.id=lbpu.lcl_unit_ss_id");
        queryStr.append(" JOIN lcl_unit lu ON lu.id=lus.unit_id");
        queryStr.append(" JOIN lcl_ss_header lsh ON lsh.id=lus.ss_header_id");
        queryStr.append(" WHERE lbp.file_number_id=:fileId");
        queryStr.append(" AND lsh.service_type=:serviceType");
        SQLQuery query = getSession().createSQLQuery(queryStr.toString());
        query.setParameter("fileId", fileId);
        query.setParameter("serviceType", serviceType);
        query.setResultTransformer(Transformers.aliasToBean(LclModel.class));
        query.addScalar("scheduleNo", StringType.INSTANCE);
        query.addScalar("unitNo", StringType.INSTANCE);
        List<LclModel> pickedList = query.list();
        if (pickedList != null && !pickedList.isEmpty()) {
            return pickedList.get(0);
        }
        return null;
    }

    public List<LclModel> getBlChargesValidate(Long fileId) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT IF(chg.ar_bill_to_party = 'A',b.agent_acct_no,IF(chg.ar_bill_to_party = 'F',b.fwd_acct_no, ");
        queryStr.append("IF(chg.ar_bill_to_party = 'S',b.ship_acct_no, b.third_party_acct_no))) AS vendorName, ");
        queryStr.append("gm.Charge_code AS chargeCode,chg.ar_bill_to_party AS billToParty   ");
        queryStr.append("FROM lcl_bl b JOIN lcl_bl_ac chg ON b.file_number_id = chg.file_number_id  ");
        queryStr.append("JOIN gl_mapping gm ON gm.id = ar_gl_mapping_id ");
        queryStr.append("WHERE chg.ar_amount > 0.00 AND b.file_number_id  =:fileId   GROUP BY  chg.ar_bill_to_party HAVING vendorName IS NULL ");
        SQLQuery query = getSession().createSQLQuery(queryStr.toString());
        query.setParameter("fileId", fileId);
        query.setResultTransformer(Transformers.aliasToBean(LclModel.class));
        query.addScalar("vendorName", StringType.INSTANCE);
        query.addScalar("chargeCode", StringType.INSTANCE);
        query.addScalar("billToParty", StringType.INSTANCE);
        return query.list();
    }

    public boolean isPickedFile(Long fileId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT if( count(*) > 0, true, false) as result  FROM lcl_booking_piece bp ");
        sb.append(" JOIN lcl_booking_piece_unit bpu ON bpu.`booking_piece_id` = bp.`id` ");
        sb.append(" JOIN lcl_unit_ss lus ON lus.id = bpu.`lcl_unit_ss_id` ");
        sb.append(" JOIN lcl_ss_detail lsd ON lsd.`ss_header_id` = lus.`ss_header_id` ");
        sb.append(" WHERE bp.`file_number_id` =:fileId ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameter("fileId", fileId);
        return (boolean) query.addScalar("result", BooleanType.INSTANCE).uniqueResult();

    }

    public boolean isDrContainCollectCharge(Long fileId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" select if( count(*)>0 , true ,false ) as result from lcl_bl_ac ac  ");
        sb.append(" join lcl_bl bl on bl.file_number_id = ac.file_number_id ");
        sb.append(" where ac.file_number_id=:fileId  and ac.ar_bill_to_party = 'A' ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameter("fileId", fileId);
        return (boolean) query.addScalar("result", BooleanType.INSTANCE).uniqueResult();
    }

    public boolean validateCollectCharges(String headerId, String unitssId, String fileId) throws Exception {
        SQLQuery query = null;
        if (!fileId.isEmpty()) {
            query = getCurrentSession().createSQLQuery("select if( count(*)>0 ,true ,false) as result from "
                    + " lcl_bl_ac ac where ac.file_number_id =:fileId and ac.ar_bill_to_party='A'");
            query.setLong("fileId", Long.parseLong(fileId));
        } else {
            List<Long> actualDrList = new ExportUnitQueryUtils().getAllPickedCargoBkg(Long.parseLong(headerId), Long.parseLong(unitssId));
            query = getCurrentSession().createSQLQuery("select if( count(*)>0 ,true ,false) as result from "
                    + " lcl_bl_ac ac where ac.file_number_id in(:fileList) and ac.ar_bill_to_party='A'");
            query.setParameterList("fileList", actualDrList);
        }
        return (boolean) query.addScalar("result", BooleanType.INSTANCE).uniqueResult();
    }

    public String getPickedDrBillToParty(Long fileId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT GROUP_CONCAT(DISTINCT ac.`ar_bill_to_party`) AS billtToParty FROM lcl_bl_ac ac ");
        sb.append(" WHERE ac.`file_number_id` =:fileId ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameter("fileId", fileId);
        return (String) query.addScalar("billtToParty", StringType.INSTANCE).uniqueResult();
    }

    public List<ExportVoyageSearchModel> searchVoyageDetails(String voyageNumber) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT lclssh.id AS ssHeaderId,lclssh.service_type as serviceType,lclssd.id AS ssDetailId,");
        queryStr.append(" lclssh.schedule_no AS scheduleNo,lclssd.sp_acct_no AS carrierAcctNo,");
        queryStr.append(" (SELECT acct_name FROM trading_partner WHERE acct_no = lclssd.sp_acct_no LIMIT 1) AS carrierName,");
        queryStr.append(" (SELECT COUNT(*) FROM lcl_unit lu LEFT JOIN lcl_unit_ss luss ON luss.unit_id = lu.id ");
        queryStr.append(" WHERE lclssh.id = luss.ss_header_id) AS unitcount,");
        queryStr.append(getAppendQueryForUnitNo());
        queryStr.append(" lclssd.sp_reference_name AS vesselName,lclssd.sp_reference_no AS ssVoyage,");
        queryStr.append(" UnLocationGetCodeByID (lclssd.departure_id) AS departPierUnloc,");
        queryStr.append(" UnLocationGetNameStateCntryByID (lclssd.departure_id) AS departPier,");
        queryStr.append(" UnLocationGetCodeByID (lclssd.arrival_id) AS arrivalPierUnloc,");
        queryStr.append(" UnLocationGetNameStateCntryByID (lclssd.arrival_id) AS arrivalPier,");
        queryStr.append(" lclssd.relay_lrd_override AS lrdOverride,lclssd.relay_lrd_override AS lrdOverrideDays, ");
        queryStr.append(" DATE_FORMAT(lclssd.general_lrdt, '%d-%b-%Y') AS polLrdDate,lclssd.general_lrdt as polLrdDates,");
        queryStr.append(" lclssd.sta as etaPodDates,lclssd.std as etaSailDates,");
        queryStr.append(" DATE_FORMAT(lclssd.std, '%d-%b-%Y') AS etaSailDate,DATE_FORMAT(lclssd.sta, '%d-%b-%Y') AS etaPodDate,");
        queryStr.append(" lclssh.datasource AS dataSource,");
        queryStr.append(" UserDetailsGetLoginNameByID (lclssh.entered_by_user_id) AS createdBy,");
        queryStr.append(" UserDetailsGetLoginNameByID (lclssh.owner_user_id) AS voyOwner,");
        queryStr.append("  (SELECT COUNT(*) FROM lcl_unit lu LEFT JOIN lcl_unit_ss luss ON luss.unit_id = lu.id ");
        queryStr.append("   WHERE lclssh.id = luss.ss_header_id and luss.status ='M') as manifestUnitCount, ");
        queryStr.append("  (SELECT COUNT(*) FROM lcl_unit lu LEFT JOIN lcl_unit_ss luss ON luss.unit_id = lu.id ");
        queryStr.append("   WHERE lclssh.id = luss.ss_header_id and luss.cob = 1 ) as cobUnitCount, ");
        queryStr.append("  (SELECT MAX(CASE WHEN lclssd.sta = luss.cob_datetime THEN 1 ");
        queryStr.append("  WHEN (SELECT COUNT(*) FROM lcl_unit_ss lus  WHERE lus.`ss_header_id` = lclssh.id)  =  ");
        queryStr.append("  (SELECT COUNT(*) FROM lcl_unit_ss lus WHERE lus.`ss_header_id` = lclssh.id AND lus.cob_datetime IS NULL) THEN 2");
        queryStr.append("  WHEN (SELECT COUNT(*) FROM lcl_unit_ss lus ");
        queryStr.append("  WHERE lus.`ss_header_id` = lclssh.id)>1 AND luss.cob_datetime IS NULL THEN 3 ");
        queryStr.append("  WHEN lclssd.sta <> luss.cob_datetime THEN 4 ELSE 0 END ");
        queryStr.append("  ) AS vETA FROM lcl_unit_ss luss WHERE luss.ss_header_id = lclssh.id) AS verifiedEta ");
        queryStr.append(" FROM lcl_ss_header lclssh ");
        queryStr.append(" LEFT JOIN lcl_ss_detail lclssd ON (lclssh.id = lclssd.ss_header_id)");
        queryStr.append(" WHERE ");

        if (CommonUtils.isNotEmpty(voyageNumber)) {
            queryStr.append(" lclssh.schedule_no=:scheduleNo");
        }
        queryStr.append(" GROUP BY lclssh.schedule_no ");
        SQLQuery query = getSession().createSQLQuery(queryStr.toString());
        if (CommonUtils.isNotEmpty(voyageNumber)) {
            query.setParameter("scheduleNo", voyageNumber);
        }
        query.setResultTransformer(Transformers.aliasToBean(ExportVoyageSearchModel.class));
        query.addScalar("ssHeaderId", StringType.INSTANCE);
        query.addScalar("ssDetailId", StringType.INSTANCE);
        query.addScalar("serviceType", StringType.INSTANCE);
        query.addScalar("scheduleNo", StringType.INSTANCE);
        query.addScalar("carrierName", StringType.INSTANCE);
        query.addScalar("carrierAcctNo", StringType.INSTANCE);
        query.addScalar("unitcount", StringType.INSTANCE);
        query.addScalar("unitNo", StringType.INSTANCE);
        query.addScalar("vesselName", StringType.INSTANCE);
        query.addScalar("ssVoyage", StringType.INSTANCE);
        query.addScalar("departPierUnloc", StringType.INSTANCE);
        query.addScalar("departPier", StringType.INSTANCE);
        query.addScalar("arrivalPierUnloc", StringType.INSTANCE);
        query.addScalar("arrivalPier", StringType.INSTANCE);
        query.addScalar("lrdOverrideDays", StringType.INSTANCE);
        query.addScalar("polLrdDate", StringType.INSTANCE);
        query.addScalar("etaSailDate", StringType.INSTANCE);
        query.addScalar("etaPodDate", StringType.INSTANCE);
        query.addScalar("createdBy", StringType.INSTANCE);
        query.addScalar("voyOwner", StringType.INSTANCE);
        query.addScalar("dataSource", StringType.INSTANCE);
        query.addScalar("manifestUnitCount", StringType.INSTANCE);
        query.addScalar("cobUnitCount", StringType.INSTANCE);
        query.addScalar("verifiedEta", IntegerType.INSTANCE);
        return query.list();
    }

    public List<ExportVoyageSearchModel> getMultiBookingSearchList(LclUnitsScheduleForm lclUnitsScheduleForm) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append("SELECT lmb.sp_booking_no AS bookingNo,");
        queryStr.append(" lsh.id AS ssHeaderId,lsh.schedule_no as scheduleNo,");
        queryStr.append("  UnLocationGetCodeByID (lsh.destination_id) AS departPierUnloc,");
        queryStr.append(" UnLocationGetCodeByID(lsh.origin_id) AS arrivalPierUnloc,");
        queryStr.append(" UnLocationGetNameStateCntryByID (lsh.origin_id) AS arrivalPier,");
        queryStr.append(" UnLocationGetNameStateCntryByID (lsh.destination_id) AS departPier,");
        queryStr.append(" lsh.origin_id as pooId,lsh.destination_id as fdId ");
        queryStr.append("  FROM  lcl_ss_masterbl lmb ");
        queryStr.append("  JOIN lcl_ss_header lsh ON lsh.id=lmb.ss_header_id ");
        queryStr.append("  WHERE lmb.`sp_booking_no`=");
        queryStr.append("'").append(lclUnitsScheduleForm.getBookingNo()).append("' ");
        SQLQuery query = getSession().createSQLQuery(queryStr.toString());
        query.setResultTransformer(Transformers.aliasToBean(ExportVoyageSearchModel.class));
        query.addScalar("ssHeaderId", StringType.INSTANCE);
        query.addScalar("scheduleNo", StringType.INSTANCE);
        query.addScalar("departPierUnloc", StringType.INSTANCE);
        query.addScalar("departPier", StringType.INSTANCE);
        query.addScalar("arrivalPierUnloc", StringType.INSTANCE);
        query.addScalar("arrivalPier", StringType.INSTANCE);
        query.addScalar("pooId", StringType.INSTANCE);
        query.addScalar("fdId", StringType.INSTANCE);
        query.addScalar("bookingNo", StringType.INSTANCE);
        return query.list();
    }

    public String validateGlAccount(String unitSsId, String originId) throws Exception {
        StringBuilder sb = new StringBuilder();
        StringBuilder result = new StringBuilder();
        sb.append(" SELECT CASE WHEN glAcct.glAccount IS NULL THEN ");
        sb.append(" CONCAT('<tr><td>',glAcct.fileNo,'</td><td>', ");
        sb.append(" glAcct.chargeCode,'</td><td class=\''red\''> No GL account is mappped.</td></tr>') ");
        sb.append(" WHEN IsValidGlAccount(glAcct.glAccount) = 0 THEN ");
        sb.append(" CONCAT('<tr><td>',glAcct.fileNo,'</td><td>', ");
        sb.append(" glAcct.chargeCode,'</td><td>GL Account <span class=\''red\''>',glAcct.glAccount,'</span> is not valid one.</td></tr>') ");
        sb.append(" ELSE '' END  AS glAccountMsg ");
        sb.append(" FROM (SELECT lf.id AS fileId, lf.`file_number` AS fileNo, ");
        sb.append(" gl.`Charge_code` AS chargeCode, ");
        sb.append(" DeriveLCLExportGlAccount(gl.id,'','0',").append(originId).append(") AS glAccount ");
        sb.append(" FROM  lcl_bl_ac blac  ");
        sb.append(" JOIN gl_mapping gl ON (gl.id = blac.`ar_gl_mapping_id` and gl.shipment_type='LCLE' and gl.transaction_type = 'AR') ");
        sb.append(" JOIN lcl_file_number lf ON lf.id = blac.`file_number_id` ");
        sb.append(" JOIN lcl_booking_piece lbp ON lbp.`file_number_id` = lf.id ");
        sb.append(" JOIN lcl_booking_piece_unit bpu ON bpu.`booking_piece_id` = lbp.`id` ");
        sb.append(" JOIN lcl_unit_ss lus ON lus.`id` = bpu.`lcl_unit_ss_id` ");
        sb.append(" WHERE lus.id =:unitSsId ");
        sb.append(" GROUP BY gl.`bluescreen_chargecode` , lf.`id` )  AS  glAcct ");
        sb.append(" HAVING glAccountMsg <> '' ORDER BY glAcct.fileId DESC ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameter("unitSsId", unitSsId);
        List errorList = query.list();
        if (!errorList.isEmpty()) {
            for (Object obj : errorList) {
                result.append(obj.toString());
            }
        }
        return result.toString();
    }

    public String getAppendQueryForUnitNo() {
        StringBuilder sb = new StringBuilder();
        sb.append(" (SELECT GROUP_CONCAT(CONCAT(lu.unit_no,'</td><td>', ");
        sb.append(" ut.`description`,'</td><td>', ");
        sb.append(" IF(luss.`cob` = 1,'MANIFESTED/COB',IF(luss.`status` = 'M','MANIFESTED','')) ");
        sb.append(" ,IF(lclssd.sta IS NOT NULL,CONCAT('</td><td>',DATE_FORMAT(lclssd.sta, '%d-%b-%Y'),'</td><td>'),''),");
        sb.append(" IF(luss.cob_datetime IS NOT NULL,CONCAT(DATE_FORMAT(luss.cob_datetime, '%d-%b-%Y')),''))");
        sb.append(" ORDER BY luss.unit_id DESC SEPARATOR '</td></tr><tr><td>') AS unitNo ");
        sb.append(" FROM lcl_unit lu JOIN unit_type ut ON ut.id = lu.`unit_type_id` ");
        sb.append(" LEFT JOIN lcl_unit_ss luss ON luss.unit_id = lu.id WHERE luss.ss_header_id = lclssh.id ");
        sb.append(" ORDER BY lu.unit_no ASC) as unitNo, ");
        return sb.toString();
    }

    public List<Long> getUnitSSIdList(Long headerId, Long UnitSsId) throws Exception {
        List<Long> unitSsIdList = new ArrayList<Long>();
        List queryObject = new ArrayList();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT lus.id FROM lcl_unit_ss lus JOIN lcl_ss_header lsh ");
        sb.append(" ON lus.`ss_header_id` = lsh.id JOIN lcl_unit lu ON lu.`id` = lus.`unit_id` ");
        sb.append(" WHERE lsh.id=").append(headerId);
        if (UnitSsId != 0) {
            sb.append(" and lus.id=").append(UnitSsId);
        }
        queryObject = getCurrentSession().createSQLQuery(sb.toString()).list();
        if (CommonUtils.isNotEmpty(queryObject)) {
            for (Object obj : queryObject) {

                unitSsIdList.add(Long.parseLong(obj.toString()));
            }
        }
        return unitSsIdList;
    }

    public List<Long> getAllPickedCargoBkgUnrated(Long headerId, Long UnitSsId) throws Exception {
        List queryObject = new ArrayList();
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT DISTINCT fn.fileId as fileId  FROM  ");
        sb.append(" (SELECT DISTINCT bkp.file_number_id AS fileId ");
        sb.append("  FROM lcl_booking_piece_unit bpu JOIN lcl_booking_piece bkp ON  bpu.booking_piece_id = bkp.id ");
        sb.append(" JOIN lcl_file_number lfn ON lfn.id=bkp.file_number_id  JOIN lcl_booking lb ON lb.file_number_id=lfn.id  ");
        sb.append(" JOIN lcl_unit_ss lus ON lus.id=bpu.`lcl_unit_ss_id` where lus.ss_header_id=").append(headerId);
        if (UnitSsId != 0) {
            sb.append(" and lus.id=").append(UnitSsId);
        }
        sb.append(" ) fn");
        queryObject = getCurrentSession().createSQLQuery(sb.toString()).list();
        List<Long> resultList = new ArrayList<Long>();
        for (Object obj : queryObject) {
            resultList.add(Long.parseLong(obj.toString()));
        }
        return resultList;
    }

}
