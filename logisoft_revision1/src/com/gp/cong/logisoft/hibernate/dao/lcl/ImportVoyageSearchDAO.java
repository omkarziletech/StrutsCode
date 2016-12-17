/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.beans.ImpVoyageSearchBean;
import com.gp.cong.logisoft.beans.LclImportUnitBkgModel;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LclAddVoyageForm;
import com.gp.cvst.logisoft.struts.form.lcl.LclUnitsScheduleForm;
import java.util.List;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BooleanType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.StringType;

/**
 *
 * @author Mei
 */
public class ImportVoyageSearchDAO extends BaseHibernateDAO {

    public String buildSelectQuery() {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("   ");
        queryBuilder.append("  SELECT lfn.id AS fileId,  ");
        queryBuilder.append("  lfn.file_number AS fileNo,lb.booking_type AS bookingType,  ");
        queryBuilder.append("  UnLocationGetCodeByID (lb.fd_id) AS fdUnloc,UnLocationGetNameStateCntryByID (lb.fd_id) AS fdName,  ");
        queryBuilder.append("  lb.sup_acct_no AS originAgentNo,  ");
        queryBuilder.append("  (SELECT acct_name FROM trading_partner WHERE acct_no = lb.sup_acct_no LIMIT 1) AS originAgentName,  ");
        queryBuilder.append("  lb.ship_acct_no AS shipAcctNo,  ");
        queryBuilder.append("  (SELECT company_name FROM lcl_contact WHERE id = lb.ship_contact_id) AS shipperName,  ");
        queryBuilder.append("  lb.cons_acct_no AS consAcctNo,  ");
        queryBuilder.append("  (SELECT company_name FROM lcl_contact WHERE id = lb.cons_contact_id) AS consigneeName,  ");
        queryBuilder.append("  lb.noty_acct_no AS notyAcctNo,  ");
        queryBuilder.append("  (SELECT company_name FROM lcl_contact WHERE id = lb.noty_contact_id) AS notifyName,  ");
        queryBuilder.append("  SUM(IF(lbp.booked_piece_count IS NOT NULL AND lbp.booked_piece_count != 0,lbp.booked_piece_count,'')) AS totalPiece,  ");
        queryBuilder.append("  IF(lbp.booked_weight_metric IS NOT NULL AND lbp.booked_weight_metric != 0.000,lbp.booked_weight_metric,'') AS totalweightimperial,  ");
        queryBuilder.append("  IF(lbp.booked_volume_metric IS NOT NULL AND lbp.booked_volume_metric != 0.000,lbp.booked_volume_metric,'') AS totalvolumeimperial,  ");
        queryBuilder.append("  UserDetailsGetLoginNameByID (lb.entered_by_user_id) AS bookedBy,  ");
        queryBuilder.append("  lbm.sub_house_bl AS subhouseBl,  ");
        queryBuilder.append("  (SELECT SUM(ar_amount) FROM lcl_booking_ac  WHERE file_number_id = lfn.id AND ar_bill_to_party = 'C' GROUP BY file_number_id) AS collectAmt,  ");
        queryBuilder.append("  (SELECT SUM(ar_amount) FROM lcl_booking_ac WHERE file_number_id = lfn.id AND ar_bill_to_party = 'A' AND rels_to_inv = 1 GROUP BY file_number_id) AS agentReleAmt,  ");
        queryBuilder.append("  (SELECT SUM(ar_amount) FROM lcl_booking_ac WHERE file_number_id = lfn.id AND ar_bill_to_party = 'A' AND rels_to_inv = 0 GROUP BY file_number_id) AS agentNotReleAmt,  ");
        queryBuilder.append("  lbm.door_delivery_status AS doorStatus,  ");
        queryBuilder.append("  (SELECT IF(COUNT(*) > 1,GROUP_CONCAT(scac,',',ams_no ,',',pieces  SEPARATOR '<br>'),'') FROM lcl_booking_import_ams WHERE file_number_id = lfn.id) AS amsDetails,  ");
        queryBuilder.append("  (SELECT IF(COUNT(*) > 0, TRUE, FALSE) FROM lcl_booking_segregation WHERE child_file_number_id = lfn.id) AS segFileFlag,  ");
        queryBuilder.append("  lbm.original_bl_received AS originalRecv,lbm.freight_released_datetime AS freightRel,  ");
        queryBuilder.append("  lbm.payment_release_received AS payRel,lbm.cargo_on_hold AS cargohold,  ");
        queryBuilder.append("  lbm.cargo_general_order AS cargoorder,lbm.customs_clearance_received AS cusrcRecv,  ");
        queryBuilder.append("  lbm.delivery_order_received AS delivrecv,lbm.release_order_received AS relerecv,  ");
        queryBuilder.append("  lbm.express_release AS expressRelease,DATE_FORMAT(lbm.fd_eta, '%d-%b-%Y') AS etafd,lbm.picked_up_datetime AS pickedUp,  ");
        queryBuilder.append("  (SELECT ams_no FROM lcl_booking_import_ams WHERE file_number_id = lfn.id ORDER BY id ASC LIMIT 1) AS defaultAms,   ");
        queryBuilder.append("  (SELECT ar.status AS arStatus FROM lcl_booking_ac ac JOIN ar_red_invoice ar ON ac.sp_reference_no = ar.invoice_number   ");
        queryBuilder.append("  WHERE  ac.file_number_id=lfn.id GROUP BY ac.file_number_id) as arInvoiceStatus,  ");
        queryBuilder.append(" (SELECT ams_no FROM lcl_booking_import_ams WHERE seg_file_number_id=lfn.id) AS segAmsNo, ");
        queryBuilder.append(" MAX(lbp.hazmat) AS hazmat, ");
        queryBuilder.append(" (SELECT SUM(IFNULL(ar_amount,0)) FROM lcl_booking_ac WHERE file_number_id=lfn.id AND ar_bill_to_party NOT IN('W','A')) AS amtTotal, ");
        queryBuilder.append(" (SELECT SUM(lbata.amount) AS amt FROM lcl_booking_ac_trans lbat JOIN lcl_booking_ac_ta lbata ON lbata.lcl_booking_ac_trans_id = lbat.id WHERE lbat.file_number_id = lfn.id) AS paidAmt, ");
        queryBuilder.append(" (SELECT IF(COUNT(*) >0,TRUE,FALSE)  FROM lcl_booking_ac_trans WHERE file_number_id = lfn.id AND payment_type='check copy') AS isPaymentType, ");
        queryBuilder.append(" (SELECT SUM(IFNULL(transaction_amount+adjustment_amount, 0)) AS amt FROM ar_transaction_history WHERE (invoice_number = lfn.`file_number`)) AS totalArBalanceAmount ");
        return queryBuilder.toString();
    }

    public String buildInnerQuery() {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("  (SELECT  ");
        queryBuilder.append("  booking_piece_id  ");
        queryBuilder.append("  FROM ");
        queryBuilder.append("  lcl_booking_piece_unit ");
        queryBuilder.append("  WHERE lcl_unit_ss_id = :unitSsId) filter ");
        return queryBuilder.toString();
    }

    public String buildJoinQuery() {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("   lcl_booking_piece_unit lbpu   ");
        queryBuilder.append("    JOIN lcl_booking_piece lbp ON lbp.id = lbpu.booking_piece_id   ");
        queryBuilder.append("  JOIN lcl_booking lb ON lb.file_number_id = lbp.file_number_id   ");
        queryBuilder.append("  JOIN lcl_file_number lfn ON lfn.id = lb.file_number_id   ");
        queryBuilder.append("  JOIN lcl_booking_import lbm ON lbm.file_number_id = lfn.id   ");
        queryBuilder.append("  WHERE lbpu.lcl_unit_ss_id =:unitSsId   ");
        queryBuilder.append("  GROUP BY lfn.id ORDER BY lfn.id DESC   ");
        return queryBuilder.toString();
    }

    public List<LclImportUnitBkgModel> getPickedDrList(LclAddVoyageForm lclAddVoyageForm) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(this.buildSelectQuery());
        queryBuilder.append("  FROM  ");
        // queryBuilder.append(this.buildInnerQuery());
        queryBuilder.append(this.buildJoinQuery());
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setLong("unitSsId", lclAddVoyageForm.getUnitssId());
        query.setResultTransformer(Transformers.aliasToBean(LclImportUnitBkgModel.class));
        query.addScalar("fileId", StringType.INSTANCE);
        query.addScalar("fileNo", StringType.INSTANCE);
        query.addScalar("bookingType", StringType.INSTANCE);
        query.addScalar("fdUnloc", StringType.INSTANCE);
        query.addScalar("fdName", StringType.INSTANCE);
        query.addScalar("originAgentNo", StringType.INSTANCE);
        query.addScalar("originAgentName", StringType.INSTANCE);
        query.addScalar("shipAcctNo", StringType.INSTANCE);
        query.addScalar("shipperName", StringType.INSTANCE);
        query.addScalar("consAcctNo", StringType.INSTANCE);
        query.addScalar("consigneeName", StringType.INSTANCE);
        query.addScalar("notyAcctNo", StringType.INSTANCE);
        query.addScalar("notifyName", StringType.INSTANCE);
        query.addScalar("totalPiece", StringType.INSTANCE);
        query.addScalar("totalweightimperial", StringType.INSTANCE);
        query.addScalar("totalvolumeimperial", StringType.INSTANCE);
        query.addScalar("bookedBy", StringType.INSTANCE);
        query.addScalar("subhouseBl", StringType.INSTANCE);
        query.addScalar("collectAmt", StringType.INSTANCE);
        query.addScalar("agentReleAmt", StringType.INSTANCE);
        query.addScalar("agentNotReleAmt", StringType.INSTANCE);
        query.addScalar("doorStatus", StringType.INSTANCE);
        query.addScalar("amsDetails", StringType.INSTANCE);
        query.addScalar("segFileFlag", BooleanType.INSTANCE);
        query.addScalar("originalRecv", StringType.INSTANCE);
        query.addScalar("freightRel", StringType.INSTANCE);
        query.addScalar("payRel", StringType.INSTANCE);
        query.addScalar("cargohold", StringType.INSTANCE);
        query.addScalar("cargoorder", StringType.INSTANCE);
        query.addScalar("cusrcRecv", StringType.INSTANCE);
        query.addScalar("delivrecv", StringType.INSTANCE);
        query.addScalar("relerecv", StringType.INSTANCE);
        query.addScalar("expressRelease", BooleanType.INSTANCE);
        query.addScalar("etafd", StringType.INSTANCE);
        query.addScalar("pickedUp", StringType.INSTANCE);
        query.addScalar("defaultAms", StringType.INSTANCE);
        query.addScalar("arInvoiceStatus", StringType.INSTANCE);
        query.addScalar("segAmsNo", StringType.INSTANCE);
        query.addScalar("hazmat", BooleanType.INSTANCE);
        query.addScalar("amtTotal", DoubleType.INSTANCE);
        query.addScalar("paidAmt", DoubleType.INSTANCE);
        query.addScalar("isPaymentType", BooleanType.INSTANCE);
        query.addScalar("totalArBalanceAmount", StringType.INSTANCE);
        return query.list();
    }

    public List<ImpVoyageSearchBean> getImportVoyageSearch(LclUnitsScheduleForm lclUnitsScheduleForm) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(buildSelectQuery(lclUnitsScheduleForm));
        queryBuilder.append(" FROM ");
        queryBuilder.append(innerImpVoyageQuery(lclUnitsScheduleForm));
        queryBuilder.append(joinImpVoyageQuery(lclUnitsScheduleForm));
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(ImpVoyageSearchBean.class));
        query.addScalar("ssHeaderId", StringType.INSTANCE);
        query.addScalar("ssDetailId", StringType.INSTANCE);
        query.addScalar("scheduleNo", StringType.INSTANCE);
        query.addScalar("unitId", StringType.INSTANCE);
        query.addScalar("unitSsId", StringType.INSTANCE);
        query.addScalar("unitNo", StringType.INSTANCE);
        query.addScalar("dataSource", StringType.INSTANCE);
        query.addScalar("originUnCode", StringType.INSTANCE);
        query.addScalar("originName", StringType.INSTANCE);
        query.addScalar("fdUnCode", StringType.INSTANCE);
        query.addScalar("fdName", StringType.INSTANCE);
        query.addScalar("createdBy", StringType.INSTANCE);
        query.addScalar("voyOwner", StringType.INSTANCE);
        query.addScalar("terminal", StringType.INSTANCE);
        query.addScalar("carrierName", StringType.INSTANCE);
        query.addScalar("carrierAcctNo", StringType.INSTANCE);
        query.addScalar("vesselName", StringType.INSTANCE);
        query.addScalar("ssVoyage", StringType.INSTANCE);
        query.addScalar("departPierUnloc", StringType.INSTANCE);
        query.addScalar("departPier", StringType.INSTANCE);
        query.addScalar("arrivalPierUnloc", StringType.INSTANCE);
        query.addScalar("arrivalPier", StringType.INSTANCE);
        query.addScalar("etaSailDate", StringType.INSTANCE);
        query.addScalar("etaPodDate", StringType.INSTANCE);
        query.addScalar("totaltransPod", StringType.INSTANCE);
        query.addScalar("agentName", StringType.INSTANCE);
        query.addScalar("agentAcct", StringType.INSTANCE);
        query.addScalar("closedStatus", StringType.INSTANCE);
        query.addScalar("auditedStatus", StringType.INSTANCE);
        query.addScalar("closedBy", StringType.INSTANCE);
        query.addScalar("auditedBy", StringType.INSTANCE);
        query.addScalar("closedOn", StringType.INSTANCE);
        query.addScalar("auditedOn", StringType.INSTANCE);
        query.addScalar("closedRemarks", StringType.INSTANCE);
        query.addScalar("auditedRemarks", StringType.INSTANCE);
        query.addScalar("dispoCode", StringType.INSTANCE);
        query.addScalar("dispoDesc", StringType.INSTANCE);
        query.addScalar("eculineVoyage", StringType.INSTANCE);
        query.addScalar("strippeddate", StringType.INSTANCE);
        query.addScalar("hazmatPermitted", StringType.INSTANCE);
        return query.list();
    }

    public String buildSelectQuery(LclUnitsScheduleForm lclUnitsScheduleForm) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("  SELECT  ");
        queryBuilder.append("  filter.id AS ssHeaderId,   ");
        queryBuilder.append("  lsd.id AS ssDetailId,   ");
        queryBuilder.append("  lsh.schedule_no AS scheduleNo,   ");
        queryBuilder.append("  lu.id AS unitId,   ");
        queryBuilder.append("  lus.id AS unitSsId,   ");
        queryBuilder.append("  lu.unit_no AS unitNo,   ");
        queryBuilder.append("  lsh.datasource AS dataSource,   ");
        queryBuilder.append("  UnLocationGetCodeByID (lsh.origin_id) AS originUnCode,   ");
        queryBuilder.append("  UnLocationGetNameStateCntryByID (lsh.origin_id) AS originName,   ");
        queryBuilder.append("  UnLocationGetCodeByID (lsh.destination_id) AS fdUnCode,   ");
        queryBuilder.append("  UnLocationGetNameStateCntryByID (lsh.destination_id) AS fdName,   ");
        queryBuilder.append("  UserDetailsGetLoginNameByID (lsh.entered_by_user_id) AS createdBy,   ");
        queryBuilder.append("  UserDetailsGetLoginNameByID (lsh.owner_user_id) AS voyOwner,   ");
        queryBuilder.append("  TerminalGetLocationByNo (lsh.billing_trmnum) AS terminal,   ");
        queryBuilder.append("  (SELECT acct_name FROM trading_partner WHERE acct_no = lsd.sp_acct_no LIMIT 1) AS carrierName,   ");
        queryBuilder.append("  lsd.sp_acct_no AS carrierAcctNo,   ");
        queryBuilder.append("  lsd.sp_reference_name AS vesselName,   ");
        queryBuilder.append("  lsd.sp_reference_no AS ssVoyage,   ");
        queryBuilder.append("  UnLocationGetCodeByID (lsd.departure_id) AS departPierUnloc,   ");
        queryBuilder.append("  UnLocationGetNameStateCntryByID (lsd.departure_id) AS departPier,   ");
        queryBuilder.append("  UnLocationGetCodeByID (lsd.arrival_id) AS arrivalPierUnloc,   ");
        queryBuilder.append("  UnLocationGetNameStateCntryByID (lsd.arrival_id) AS arrivalPier,   ");
        queryBuilder.append("  DATE_FORMAT(lsd.std, '%d-%b-%Y') AS etaSailDate,   ");
        queryBuilder.append("  DATE_FORMAT(lsd.sta, '%d-%b-%Y') AS etaPodDate,   ");
        queryBuilder.append("  DATEDIFF(lsd.sta, lsd.std) AS totaltransPod,   ");
        queryBuilder.append("  (SELECT acct_name FROM trading_partner WHERE acct_no = lusi.origin_acct_no LIMIT 1) AS agentName,   ");
        queryBuilder.append("  lusi.origin_acct_no AS agentAcct,   ");
        queryBuilder.append("  IF(lsh.closed_by_user_id IS NOT NULL,'CL','') AS closedStatus,   ");
        queryBuilder.append("  IF(lsh.audited_by_user_id IS NOT NULL,'AU','') AS auditedStatus,   ");
        queryBuilder.append("  IF(lsh.closed_by_user_id IS NOT NULL,UserDetailsGetLoginNameByID (lsh.closed_by_user_id),'') AS closedBy,   ");
        queryBuilder.append("  IF(lsh.audited_by_user_id IS NOT NULL,UserDetailsGetLoginNameByID (lsh.audited_by_user_id),'') AS auditedBy,   ");
        queryBuilder.append("  IF(lsh.closed_datetime IS NOT NULL,DATE_FORMAT(closed_datetime, '%d-%b-%Y %T'),'') AS closedOn,   ");
        queryBuilder.append("  IF(lsh.audited_datetime IS NOT NULL,DATE_FORMAT(audited_datetime, '%d-%b-%Y %T'),'') AS auditedOn,   ");
        queryBuilder.append("  IF(lsh.closed_remarks IS NOT NULL,lsh.closed_remarks,'') AS closedRemarks,   ");
        queryBuilder.append("  IF(lsh.audited_remarks IS NOT NULL,lsh.audited_remarks,'') AS auditedRemarks,   ");
        if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getDispositionId())) {
            queryBuilder.append(" dispo.elite_code AS dispoCode, ");
            queryBuilder.append(" dispo.description AS dispoDesc, ");
        } else {
            queryBuilder.append("  (SELECT d.elite_code FROM lcl_unit_ss_dispo lussd   ");
            queryBuilder.append("  LEFT JOIN disposition d ON lussd.disposition_id = d.id   ");
            queryBuilder.append("  WHERE lussd.unit_id = lu.id AND lsd.id = lussd.ss_detail_id ORDER BY lussd.id DESC LIMIT 1) AS dispoCode,  ");
            queryBuilder.append("  (SELECT d.description FROM lcl_unit_ss_dispo lussd  ");
            queryBuilder.append("  LEFT JOIN disposition d ON lussd.disposition_id = d.id  ");
            queryBuilder.append("  WHERE lussd.unit_id = lu.id AND lsd.id = lussd.ss_detail_id  ");
            queryBuilder.append("  ORDER BY lussd.id DESC LIMIT 1) AS dispoDesc, ");
        }
        queryBuilder.append("  (SELECT unitss_id FROM eculine_edi WHERE unitss_id = lus.id) AS eculineVoyage, ");
        queryBuilder.append("  DATE_FORMAT(luw.`destuffed_datetime`, '%d-%b-%Y') AS strippeddate, ");
        queryBuilder.append("  lu.`hazmat_permitted` AS hazmatPermitted ");
        return queryBuilder.toString();
    }

    public String innerImpVoyageQuery(LclUnitsScheduleForm lclUnitsScheduleForm) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" (SELECT id FROM lcl_ss_header ");
        queryBuilder.append(" WHERE trans_mode = 'V' AND service_type = 'I' ");
        queryBuilder.append("  AND STATUS <> 'V' ");
        queryBuilder.append(filterVoyageQuery(lclUnitsScheduleForm));
        // AND origin_id = 26113
        queryBuilder.append("  ) filter");
        return queryBuilder.toString();
    }

    public String filterVoyageQuery(LclUnitsScheduleForm lclUnitsScheduleForm) {
        StringBuilder queryBuilder = new StringBuilder();
        if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getPortOfOriginId())) {
            queryBuilder.append(" AND origin_id =").append(lclUnitsScheduleForm.getPortOfOriginId());
        } else if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getOrigin())) {
            queryBuilder.append(" AND origin_id in(");
            queryBuilder.append(" select id from un_location where un_loc_name='");
            queryBuilder.append(lclUnitsScheduleForm.getOrigin()).append("')");
        }
        if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getLoginId())) {
            queryBuilder.append(" AND owner_user_id =").append(lclUnitsScheduleForm.getLoginId());
        }
        if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getFinalDestinationId())) {
            queryBuilder.append(" AND destination_id =").append(lclUnitsScheduleForm.getFinalDestinationId());
        } else if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getDestination())) {
            queryBuilder.append(" AND destination_id in(");
            queryBuilder.append(" select id from un_location where un_loc_name='");
            queryBuilder.append(lclUnitsScheduleForm.getDestination()).append("')");
        }
        if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getBillsTerminalNo())) {
            queryBuilder.append(" AND billing_trmnum =").append(lclUnitsScheduleForm.getBillsTerminalNo());
        } else if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getBillsTerminal())) {
            queryBuilder.append(" AND ").append("  billing_trmnum= ").append(lclUnitsScheduleForm.getBillsTerminal());
        }
        if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getVoyageNo())) {
            queryBuilder.append(" AND schedule_no <= '").append(lclUnitsScheduleForm.getVoyageNo()).append("'");
        }
        if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getMasterBl())) {
            queryBuilder.append(" AND id IN(SELECT ss_header_id FROM ");
            queryBuilder.append(" lcl_unit_ss_manifest WHERE masterbl LIKE '");
            queryBuilder.append(lclUnitsScheduleForm.getMasterBl()).append("%')");
        }
        if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getAgentNo())) {
            queryBuilder.append(" AND id IN(SELECT ss_header_id FROM ");
            queryBuilder.append(" lcl_unit_ss_imports WHERE origin_acct_no='");
            queryBuilder.append(lclUnitsScheduleForm.getAgentNo()).append("')");
        }
        if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getUnitNo())) {
            queryBuilder.append(" AND id IN(SELECT lus.ss_header_id FROM lcl_unit_ss lus ");
            queryBuilder.append(" JOIN lcl_unit lu ON lus.unit_id=lu.id WHERE lu.unit_no='");
            queryBuilder.append(lclUnitsScheduleForm.getUnitNo()).append("')");
        }
        queryBuilder.append("   order by id desc  ");
        if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getLimit())
                && CommonUtils.isEmpty(lclUnitsScheduleForm.getDispositionId())) {
            queryBuilder.append("  LIMIT ").append(lclUnitsScheduleForm.getLimit());
        }
        return queryBuilder.toString();
    }

    public String joinImpVoyageQuery(LclUnitsScheduleForm lclUnitsScheduleForm) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("  JOIN lcl_ss_header lsh ON (lsh.id = filter.id)  ");
        queryBuilder.append(" JOIN lcl_ss_detail lsd ON (  ");
        queryBuilder.append("     lsd.ss_header_id = lsh.id AND lsd.trans_mode = 'V')  ");
        queryBuilder.append(" LEFT JOIN lcl_unit_ss lus ON lus.ss_header_id = lsh.id  ");
        queryBuilder.append(" LEFT JOIN lcl_unit_whse luw ON lus.ss_header_id = luw.ss_header_id  ");
        queryBuilder.append(" LEFT JOIN lcl_unit lu ON lu.id = lus.unit_id  ");
        queryBuilder.append(" LEFT JOIN lcl_unit_ss_imports lusi ON lusi.ss_header_id = lsh.id ");
        if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getDispositionId())) {
            queryBuilder.append(" LEFT JOIN lcl_unit_ss_dispo t1 ON lu.id = t1.unit_id ");
            queryBuilder.append(" LEFT JOIN lcl_unit_ss_dispo t2 ");
            queryBuilder.append(" ON (t1.unit_id = t2.unit_id AND t1.id < t2.id) LEFT JOIN disposition dispo ");
            queryBuilder.append(" ON t1.disposition_id = dispo.id ");

        }
        if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getDispositionId())) {
            queryBuilder.append(" WHERE ");
        }
        if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getDispositionId())) {
            queryBuilder.append(" t2.unit_id IS NULL AND t1.disposition_id =").append(lclUnitsScheduleForm.getDispositionId());
        }
        if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getVoyageNo()) || "voyageNo".equalsIgnoreCase(lclUnitsScheduleForm.getColumnName())) {
            queryBuilder.append("   GROUP BY lsh.id ORDER BY lsh.schedule_no DESC   ");
        } else {
            queryBuilder.append("   GROUP BY lsh.id ORDER BY lsd.sta DESC   ");
        }
        if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getDispositionId())) {
            queryBuilder.append(" LIMIT ").append(lclUnitsScheduleForm.getLimit());
        }
        return queryBuilder.toString();
    }
}
