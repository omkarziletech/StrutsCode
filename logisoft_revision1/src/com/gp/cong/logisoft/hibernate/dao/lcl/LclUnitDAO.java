/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.lcl.model.LclModel;
import com.gp.cong.logisoft.beans.BookingUnitsBean;
import com.gp.cong.logisoft.beans.ImportsManifestBean;
import com.gp.cong.logisoft.beans.ManifestBean;
import com.gp.cong.logisoft.domain.lcl.LclUnit;
import com.gp.cvst.logisoft.struts.form.lcl.LclAddVoyageForm;
import java.util.List;
import org.hibernate.Query;
import java.math.BigInteger;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.BooleanType;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

public class LclUnitDAO extends BaseHibernateDAO<LclUnit> {

    private BookingUnitsBean drBookingUnitsBean;

    public BookingUnitsBean getDrBookingUnitsBean() {
        return drBookingUnitsBean;
    }

    public void setDrBookingUnitsBean(BookingUnitsBean drBookingUnitsBean) {
        this.drBookingUnitsBean = drBookingUnitsBean;
    }

    public LclUnitDAO() {
        super(LclUnit.class);
    }

    public List<LclUnit> getAllUnitList() throws Exception {
        String queryString = "from LclUnit order by modifiedDatetime";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setMaxResults(1000);
        return queryObject.list();
    }

    public Boolean isValidateByUnitSs(String unitSsId, String unitNo) throws Exception {
        String queryStr = "SELECT IF(COUNT(*)>0,TRUE,FALSE) AS result FROM lcl_unit_ss ls join lcl_unit lu on lu.id = ls.unit_id WHERE ls.ss_header_id=:unitSsId AND lu.unit_no=:unitNo";
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr);
        query.setString("unitNo", unitNo);
        query.setString("unitSsId", unitSsId);
        query.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) query.uniqueResult();
    }

    public Integer getUnitNumberCountByHeader(String headerId, String unitNumber) throws Exception {
        StringBuilder sb = new StringBuilder();
        BigInteger count = new BigInteger("0");
        sb.append("select count(*) from lcl_unit lu JOIN lcl_unit_ss luss ON luss.unit_id = lu.id WHERE ");
        if (unitNumber != null) {
            sb.append("lu.unit_no ='").append(unitNumber).append("' AND ");
        }
        sb.append("luss.ss_header_id =").append(headerId);
        Query queryObject = getSession().createSQLQuery(sb.toString());
        Object o = queryObject.uniqueResult();
        if (o != null) {
            count = (BigInteger) o;
        }
        return count.intValue();
    }

    public String checkUnitNoExist(String unitNo) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT GROUP_CONCAT(lsh.schedule_no) as scheduleNo FROM lcl_ss_header lsh ");
        queryBuilder.append("JOIN lcl_unit_ss lus ON lus.ss_header_id = lsh.id ");
        queryBuilder.append("JOIN lcl_unit lu ON (lu.id = lus.unit_id AND lus.ss_header_id = lsh.id)");
        queryBuilder.append("WHERE lu.unit_no = :unitNo AND lsh.status <> 'V' ");
        queryBuilder.append("AND lsh.`audited_by_user_id` IS NULL ");
        queryBuilder.append("AND lsh.`closed_by_user_id` IS NULL");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setString("unitNo", unitNo);
        query.addScalar("scheduleNo", StringType.INSTANCE);
        return (String) query.uniqueResult();
    }

    public Integer getInUnitCount(String unitSSId) throws Exception {
        BigInteger count = new BigInteger("0");
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT COUNT(*) FROM (SELECT f.file_number FROM lcl_booking_piece_unit u JOIN lcl_booking_piece p ON u.booking_piece_id = ");
        queryBuilder.append("p.id JOIN lcl_file_number f ON p.file_number_id = f.id WHERE u.lcl_unit_ss_id = ");
        queryBuilder.append(unitSSId);
        queryBuilder.append(" GROUP BY f.id) lcl_file");
        Query queryObject = getSession().createSQLQuery(queryBuilder.toString());
        Object o = queryObject.uniqueResult();
        if (o != null) {
            count = (BigInteger) o;
        }
        return count.intValue();
    }

    public Integer getRelsToInvoiceCount(String unitSSId, Integer relsToInv) throws Exception {
        BigInteger count = new BigInteger("0");
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT COUNT(*) FROM lcl_unit_ss unit_ss JOIN lcl_booking_piece_unit unit_pieces ON unit_pieces.lcl_unit_ss_id = unit_ss.id ");
        queryBuilder.append("JOIN lcl_booking_piece book_piece ON unit_pieces.booking_piece_id = book_piece.id JOIN lcl_unit unit ON unit_ss.unit_id = ");
        queryBuilder.append("unit.id JOIN lcl_file_number FILE ON book_piece.file_number_id = file.id JOIN lcl_booking_ac chg ON file.id = chg.");
        queryBuilder.append("file_number_id AND chg.ar_amount > 0.00 AND chg.ar_bill_to_party = 'A' AND (chg.sp_reference_no IS NULL OR ");
        queryBuilder.append("chg.sp_reference_no='') AND chg.rels_to_inv = 1 WHERE unit_ss.id = ");
        queryBuilder.append(unitSSId);
        Query queryObject = getSession().createSQLQuery(queryBuilder.toString());
        Object o = queryObject.uniqueResult();
        if (o != null) {
            count = (BigInteger) o;
        }
        return count.intValue();
    }

    public String getRelsToInvoiceAgentCount(String unitSSId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        String agentNo = new String();
        queryBuilder.append("SELECT GROUP_CONCAT(DISTINCT (lb.sup_acct_no)) FROM lcl_unit_ss unit_ss JOIN lcl_booking_piece_unit unit_pieces ON unit_pieces.lcl_unit_ss_id = unit_ss.id ");
        queryBuilder.append("JOIN lcl_booking_piece book_piece ON unit_pieces.booking_piece_id = book_piece.id JOIN lcl_unit unit ON unit_ss.unit_id = ");
        queryBuilder.append("unit.id JOIN lcl_file_number FILE ON book_piece.file_number_id = file.id LEFT JOIN lcl_booking lb ON file.id=lb.file_number_id");
        queryBuilder.append(" JOIN lcl_booking_ac chg ON file.id = chg.file_number_id AND chg.ar_amount > 0.00 AND chg.ar_bill_to_party = 'A' ");
        queryBuilder.append("AND (chg.sp_reference_no IS NULL OR chg.sp_reference_no='') AND chg.rels_to_inv = 1 WHERE unit_ss.id = ");
        queryBuilder.append(unitSSId);
        Query queryObject = getSession().createSQLQuery(queryBuilder.toString());
        Object o = queryObject.uniqueResult();
        if (o != null) {
            agentNo = o.toString();
        }
        return agentNo;
    }

    public String getUnitNotConvertedFileNumbers(String unitSsId) throws Exception {
        String fileNumbers = new String();
        String queryString = "SELECT CONVERT(GROUP_CONCAT(lcl_file.file_number) USING utf8) AS file_number FROM (SELECT file_number FROM "
                + "lcl_booking_piece_unit u JOIN lcl_booking_piece p ON u.booking_piece_id = p.id "
                + "JOIN lcl_file_number f ON p.file_number_id = f.id "
                + " WHERE u.lcl_unit_ss_id = '" + unitSsId + "' GROUP BY f.id) lcl_file";
        Query queryObject = getSession().createSQLQuery(queryString);
        Object o = queryObject.uniqueResult();
        if (o != null) {
            fileNumbers = o.toString();
        }
        return fileNumbers;
    }

    public List<Object[]> getScheduleNoByUnitNo(String unitNumber) throws Exception {
        List<Object[]> unitList = null;
        String queryString = "select lclssh.schedule_no,lu.id from lcl_unit lu  LEFT JOIN lcl_unit_ss luss ON luss.unit_id = lu.id "
                + "LEFT JOIN lcl_ss_header lclssh ON  lclssh.id = luss.ss_header_id WHERE  lu.unit_no ='" + unitNumber + "'";
        Query queryObject = getSession().createSQLQuery(queryString);
        unitList = (List<Object[]>) queryObject.list();
        return unitList;
    }

    public String isUnitNoValidateByUnitSs(String unitNo) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT IF(lus.id IS NULL ,lu.id,'true') AS result FROM lcl_unit lu  ");
        queryStr.append("  LEFT JOIN lcl_unit_ss lus ON lus.unit_id=lu.id ");
        queryStr.append(" WHERE lu.unit_no=:unitNo GROUP BY lu.id  ");
        SQLQuery queryObject = getSession().createSQLQuery(queryStr.toString());
        queryObject.setParameter("unitNo", unitNo);
        queryObject.addScalar("result", StringType.INSTANCE);
        return (String) queryObject.setMaxResults(1).uniqueResult();
    }

    public List<LclModel> isUnitNumberExistsByVoy(String unitNo, String etaDate) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT lclssh.schedule_no as scheduleNo,  ");
        queryStr.append(" DATEDIFF(lsd.sta,:etaDate) as dateDiff,lu.id as unitId,lu.unit_type_id AS unitTypeId from    lcl_unit lu  ");
        queryStr.append(" LEFT JOIN lcl_unit_ss luss ON luss.unit_id = lu.id   ");
        queryStr.append(" LEFT JOIN lcl_ss_header lclssh ON lclssh.id = luss.ss_header_id   ");
        queryStr.append(" JOIN lcl_ss_detail lsd ON lsd.ss_header_id=luss.ss_header_id ");
        queryStr.append(" WHERE lu.unit_no = :unitNo   ");
        SQLQuery queryObject = getSession().createSQLQuery(queryStr.toString());
        queryObject.setParameter("etaDate", etaDate);
        queryObject.setParameter("unitNo", unitNo);
        queryObject.setResultTransformer(Transformers.aliasToBean(LclModel.class));
        queryObject.addScalar("scheduleNo", StringType.INSTANCE);
        queryObject.addScalar("dateDiff", IntegerType.INSTANCE);
        queryObject.addScalar("unitId", StringType.INSTANCE);
        queryObject.addScalar("unitTypeId", StringType.INSTANCE);
        return queryObject.list();
//        List<Object[]> unitList = null;
//        String queryString = "select lclssh.schedule_no,lu.id from lcl_unit lu  LEFT JOIN lcl_unit_ss luss ON luss.unit_id = lu.id "
//                + "LEFT JOIN lcl_ss_header lclssh ON  lclssh.id = luss.ss_header_id WHERE  lu.unit_no ='" + unitNumber + "'";
//        Query queryObject = getSession().createSQLQuery(queryString);
//        unitList = (List<Object[]>) queryObject.list();
    }

    private String getLengthWeightFromBkg() {
        StringBuilder sb = new StringBuilder();
        sb.append(" (SELECT Round(MAX(bdp.actual_length)) FROM lcl_booking_piece_detail bdp join lcl_booking_piece lb on lb.id = bdp.booking_piece_id where lb.file_number_id = fn.fileId) AS dimsLength, ");
        sb.append(" (SELECT Round(MAX(bdp.actual_width)) FROM lcl_booking_piece_detail bdp join lcl_booking_piece lb on lb.id = bdp.booking_piece_id where lb.file_number_id = fn.fileId) AS dimsWidth, ");
        sb.append(" (SELECT Round(MAX(bdp.actual_height)) FROM lcl_booking_piece_detail bdp join lcl_booking_piece lb on lb.id = bdp.booking_piece_id where lb.file_number_id = fn.fileId) AS dimsHeight, ");
        sb.append(" (SELECT Round(MAX(bdp.actual_weight)) FROM lcl_booking_piece_detail bdp join lcl_booking_piece lb on lb.id = bdp.booking_piece_id where lb.file_number_id = fn.fileId) AS dimsWeight, ");
        return sb.toString();
    }

    public String getConsolidateFileNoQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append(" (SELECT group_concat(lf.file_number) AS fileId   FROM lcl_consolidation lc  join lcl_file_number lf on ");
        sb.append(" lf.id = lc.lcl_file_number_id_a WHERE lc.`lcl_file_number_id_b` = ");
        sb.append(" (SELECT lcl_file_number_id_b FROM lcl_consolidation WHERE lcl_file_number_id_a = fn.fileId limit 1)    ");
        sb.append(" and lcl_file_number_id_a  <> fn.fileId ) AS consolidatesFiles, ");
        return sb.toString();
    }

    public String getConsolidateFileIdQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append(" (SELECT group_concat(lf.id) AS fileId   FROM lcl_consolidation lc  join lcl_file_number lf on ");
        sb.append(" lf.id = lc.lcl_file_number_id_a WHERE lc.`lcl_file_number_id_b` = ");
        sb.append(" (SELECT lcl_file_number_id_b FROM lcl_consolidation WHERE lcl_file_number_id_a = fn.fileId limit 1)    ");
        sb.append(" and lcl_file_number_id_a  <> fn.fileId ) AS consolidatesFileId ");
        return sb.toString();
    }

    private String exportVoyageFilterQuery(LclAddVoyageForm lclAddVoyageForm) throws Exception {
        String checkCurrentLoc = "";
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT f.`file_number` AS fileNumber, f.id AS fileId, b.id AS bookingPieceId, f.`status` AS STATUS, bk.`booking_type` AS bookingType, ");
        sb.append(" bk.`booked_ss_header_id` AS masterId, bk.poo_id AS pooId, bk.pol_id AS polId, bk.pod_id AS podId,bk.fd_id AS fdId, ");
        sb.append(" CONVERT(GROUP_CONCAT(b.hazmat) USING utf8) AS hazardous, ");
        sb.append(" (SELECT disposition_id  FROM  lcl_booking_dispo WHERE file_number_id = f.id ORDER BY id DESC LIMIT 1) AS dispoId, ");
        sb.append(" (SELECT elite_code FROM disposition WHERE id = dispoId) AS dispo, ");
        sb.append(" (SELECT un_location_id FROM lcl_booking_dispo WHERE file_number_id = f.id ORDER BY id DESC LIMIT 1) AS unLocId, ");
        sb.append(" (SELECT count(*) FROM lcl_booking_piece bp  join lcl_booking_piece_unit bpu ON bp.`id` = bpu.`booking_piece_id` ");
        sb.append(" JOIN lcl_unit_ss lus ON lus.`id` = bpu.`lcl_unit_ss_id` JOIN lcl_ss_header lsh ON lsh.`id` = lus.`ss_header_id` ");
        sb.append("  AND lsh.service_type IN('E','C') WHERE bp.`file_number_id` = f.id  LIMIT 1 ) AS pickedCount, ");
        sb.append(" (SELECT GROUP_CONCAT(lsh.`schedule_no`) FROM lcl_ss_header lsh JOIN lcl_unit_ss lus ON lus.`ss_header_id` = lsh.id JOIN lcl_booking_piece_unit ");
        sb.append(" lbu ON lbu.`lcl_unit_ss_id` =  lus.`id` JOIN lcl_booking_piece lb ON lb.`id` = lbu.`booking_piece_id` WHERE lb.file_number_id = f.id) AS voyageNo, ");
        sb.append(" (SELECT GROUP_CONCAT(lbpw.location SEPARATOR ',') FROM lcl_booking_piece_whse lbpw ");
        sb.append(" JOIN lcl_booking_piece lb ON lb.`id` = lbpw.booking_piece_id WHERE lb.file_number_id = f.id and lbpw.location <> '') AS whse, ");
        sb.append(" (SELECT GROUP_CONCAT(detail.stowed_location) FROM lcl_booking_piece_detail detail ");
        sb.append(" JOIN lcl_booking_piece lb ON lb.`id` = detail.booking_piece_id WHERE lb.file_number_id =  f.id ");
        sb.append(" and detail.stowed_location <> '' ) AS detailLine, ");
        sb.append(" (SELECT GROUP_CONCAT(lu.unit_no) FROM lcl_unit lu JOIN lcl_unit_ss lus ON lus.unit_id = lu.id JOIN lcl_booking_piece_unit lbu ");
        sb.append(" ON lbu.`lcl_unit_ss_id` =  lus.`id` JOIN lcl_booking_piece lb ON lb.`id` = lbu.`booking_piece_id` WHERE lb.file_number_id = f.id) AS unitNo, ");
        sb.append(" f.`short_ship_sequence` AS shortShipSequence ");
        sb.append(" FROM lcl_booking bk JOIN lcl_file_number f   ON f.id = bk.file_number_id ");
        sb.append(" JOIN lcl_booking_piece b  ON b.`file_number_id` = f.id  AND  f.`state` <> 'Q' and f.status <>'V' ");
        sb.append(" JOIN lcl_booking_export bux ON  bux.file_number_id = f.id  AND  bux.released_datetime IS NOT NULL ");
        sb.append(" LEFT JOIN lcl_booking_import lbm ON lbm.file_number_id = f.id LEFT JOIN un_location imppol ON lbm.usa_port_of_exit_id = imppol.id ");
        sb.append(" LEFT JOIN un_location imppod ON lbm.foreign_port_of_discharge_id = imppod.id ");
        sb.append(" WHERE ((pol_id =:pol AND pod_id =:pod ) OR (lbm.usa_port_of_exit_id =:pol AND ");
        sb.append(" lbm.foreign_port_of_discharge_id =:pod)) ");
        if ("Y".equalsIgnoreCase(lclAddVoyageForm.getBookScheduleNo())
                && "N".equalsIgnoreCase(lclAddVoyageForm.getIsReleasedDr())) {
            sb.append("  AND bk.booked_ss_header_id=").append(lclAddVoyageForm.getHeaderId());
            checkCurrentLoc = "HAVING unLocId =:pol AND  dispo <> 'INTR' ";
        } else if (lclAddVoyageForm.isCheckAllRealeaseWithCurrLoc()) {
            checkCurrentLoc = "HAVING unLocId =:pol  AND  dispo <> 'INTR' ";
        }

        if ("lclExport".equalsIgnoreCase(lclAddVoyageForm.getFilterByChanges())) {
            sb.append("  AND bux.cfcl=0 ");
            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getForceAgentAcctNo())) {
                sb.append("  AND bk.agent_acct_no= '").append(lclAddVoyageForm.getForceAgentAcctNo()).append("' ");
            }
        } else if ("lclCfcl".equalsIgnoreCase(lclAddVoyageForm.getFilterByChanges())) {
            sb.append("  AND bux.cfcl=1 ");
            if (CommonUtils.isNotEmpty(lclAddVoyageForm.getCfclAcctNo())) {
                sb.append("  AND bux.cfcl_acct_no='").append(lclAddVoyageForm.getCfclAcctNo()).append("' ");
            }
        }
        sb.append("  AND bk.hold!='Y' ");
        sb.append(" GROUP BY f.`id` ").append(checkCurrentLoc);
        return sb.toString();
    }

    public List<BookingUnitsBean> getAllReleasedBookings(LclAddVoyageForm lclAddVoyageForm) throws Exception {
        StringBuilder sb = new StringBuilder();
        boolean flag = false;
        sb.append("  SELECT  fn.fileId AS fileId, fn.fileNumber AS fileNo,  fn.hazardous  AS haz,  ");
        sb.append("  fn.bookingType AS bookingType, impol.`un_loc_code` AS impol, impod.`un_loc_code` AS impod, ");
        sb.append("  pod.`un_loc_name` AS pod , IF(fn.pickedCount > 0,'PICKED', '') AS status, piece.total_piece AS totalPieceCount,  ");
        sb.append("  ROUND(piece.total_weight_imperial, 0) AS totalWeightImperial, ROUND(piece.total_volume_imperial, 0) AS totalVolumeImperial, ");
        sb.append(" (SELECT un_loc_code FROM un_location WHERE id = fn.unLocId) AS curLoc, (SELECT un_loc_name FROM un_location WHERE id = fn.unLocId) AS curLocName, ");
        sb.append("  fn.dispo as dispo, ");
        sb.append(" (select lrm.`remarks` from lcl_remarks lrm where lrm.`file_number_id` =  fn.fileId  AND lrm.`type` ='Loading Remarks') AS remarks,");
        sb.append(" (SELECT GROUP_CONCAT(htc.code SEPARATOR '<br>') FROM lcl_booking_hot_code htc WHERE htc.file_number_id = fn.fileId ) AS hotCodes, ");
        sb.append(" (SELECT IF(INSTR(htc.code, '/'),GROUP_CONCAT( LEFT(  htc.code,  INSTR(htc.code, '/') - 1 )), '') FROM  lcl_booking_hot_code htc WHERE htc.file_number_id = fn.fileId) AS hotCodeKey, ");
        sb.append(" lsh.`schedule_no` AS pickVoyageNo, lu.`unit_no` AS unitno, upper(poo.`un_loc_code`) AS poo, ");
        sb.append(" (IF (impol.un_loc_name IS NOT NULL,impol.un_loc_name,pol.un_loc_name)) AS pol, ");
        sb.append(" pod.un_loc_name AS pod, fd.un_loc_name AS fd, lsc.schedule_no AS bookVoyageNo, ");
        sb.append(" (IF (impol.un_loc_code IS NOT NULL,impol.un_loc_code, pol.un_loc_code)) AS polUnCode, ");
        sb.append(getLengthWeightFromBkg());
        sb.append(" '' AS transMode, luss.status AS unitStatus,fn.voyageNo as voyageNo,fn.unitNo as pickedunitNo,");
        sb.append(" (SELECT CONCAT('<tr><td>PCS</td><td>LEN</td><td>WID</td><td>HEI</td></tr>',");
        sb.append(" IF(lbd.piece_count IS NOT NULL,GROUP_CONCAT(CONCAT('<tr><td>',lbd.piece_count,'</td>'), ");
        sb.append(" CONCAT('<td>',lbd.actual_length,'</td>'),CONCAT('<td>',lbd.actual_width,'</td>'), ");
        sb.append(" CONCAT('<td>',lbd.actual_height,'</td></tr>') SEPARATOR '' ),'<tr><td colspan=4> ");
        sb.append(" <font size=2 color=red><b>No Dimensions</b></font></td></tr>')) ");
        sb.append(" FROM lcl_booking_piece_detail lbd JOIN lcl_booking_piece lb ON lbd.booking_piece_id = lb.id ");
        sb.append(" WHERE lb.file_number_id = fn.fileId) AS dimensionData, upper(fn.whse) As warehouseLine ,upper(fn.detailLine) as detailLine,");
        sb.append(" fn.shortShipSequence, ");
        sb.append(getHazardousDetailsInfo());
        sb.append(getConsolidateFileNoQuery());
        sb.append(getConsolidateFileIdQuery());
        sb.append(" from (").append(exportVoyageFilterQuery(lclAddVoyageForm)).append(") fn ");
        sb.append(" JOIN un_location poo ON poo.`id` = fn.pooId JOIN un_location pol ON pol.`id` = fn.polId  ");
        sb.append(" JOIN un_location pod ON pod.`id` = fn.podId JOIN un_location fd  ON fd.`id` = fn.fdId ");
        sb.append(" LEFT  JOIN lcl_booking_import lbm  ON lbm.`file_number_id` = fn.fileId ");
        sb.append(" LEFT JOIN un_location impol ON impol.id = lbm.`usa_port_of_exit_id` ");
        sb.append(" LEFT JOIN un_location impod ON impod.id = lbm.`foreign_port_of_discharge_id` ");
        sb.append(" LEFT JOIN lcl_ss_header lsc ON lsc.id= fn.masterId ");
        sb.append(" LEFT JOIN lcl_booking_piece_unit lbu ON lbu.`booking_piece_id` = fn.bookingPieceId ");
        sb.append(" LEFT JOIN lcl_unit_ss luss ON luss.id = lbu.`lcl_unit_ss_id` AND luss.status != 'C' ");
        sb.append(" LEFT JOIN lcl_unit lu ON lu.`id` = luss.`unit_id` LEFT JOIN lcl_ss_header lsh ON lsh.`id` = luss.`ss_header_id` ");
        sb.append(" LEFT JOIN  (SELECT lb.file_number_id, ");
        sb.append(" SUM(IF(lb.actual_piece_count IS NOT NULL AND lb.actual_piece_count != 0, lb.actual_piece_count,lb.booked_piece_count)) AS total_piece, ");
        sb.append(" SUM(IF(lb.actual_weight_imperial IS NOT NULL AND lb.actual_weight_imperial != 0.000,lb.actual_weight_imperial,lb.booked_weight_imperial )) AS total_weight_imperial, ");
        sb.append(" SUM(IF(lb.actual_volume_imperial IS NOT NULL AND lb.actual_volume_imperial != 0.000,lb.actual_volume_imperial,lb.booked_volume_imperial)) AS total_volume_imperial ");
        sb.append(" FROM lcl_booking_piece lb GROUP BY lb.file_number_id ) piece ON piece.file_number_id = fn.fileId ");
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getUnitssId())) {
            sb.append(" where fn.fileId NOT IN (SELECT f.id FROM lcl_booking_piece b JOIN lcl_booking_piece_unit bu  ON bu.`booking_piece_id` = b.`id` ");
            sb.append(" JOIN lcl_unit_ss lus ON lus.id = bu.`lcl_unit_ss_id` JOIN lcl_file_number f ON f.id = b.`file_number_id` WHERE lus.id =").append(lclAddVoyageForm.getUnitssId()).append(")");
            flag = true;
        }
        if (lclAddVoyageForm.getDisplayLoadComplete() == null || lclAddVoyageForm.getDisplayLoadComplete().equalsIgnoreCase("Y")) {
            sb.append(flag ? " AND " : " WHERE ").append(" ( lbu.id IS NULL OR lbu.lcl_unit_ss_id!=");
            sb.append(lclAddVoyageForm.getUnitssId()).append(" )");
            flag = true;
        }
        sb.append(flag ? " AND " : " WHERE ").append("  fn.pickedCount=0 ");
        sb.append(" GROUP BY fn.fileId ");
        sb.append(" order by CONCAT(IF(fn.shortshipSequence = 0 ,SUBSTR(poo.`un_loc_code`,3,5), ");
        sb.append(" CONCAT('ZZ',fn.shortshipSequence)) ,'-',fn.fileNumber) ASC  ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameter("pol", lclAddVoyageForm.getOriginId());
        query.setParameter("pod", lclAddVoyageForm.getFinalDestinationId());
        query.setResultTransformer(Transformers.aliasToBean(BookingUnitsBean.class));
        query.addScalar("fileId", LongType.INSTANCE);
        query.addScalar("fileNo", StringType.INSTANCE);
        query.addScalar("haz", BooleanType.INSTANCE);
        query.addScalar("bookingType", StringType.INSTANCE);
        query.addScalar("impol", StringType.INSTANCE);
        query.addScalar("impod", StringType.INSTANCE);
        query.addScalar("pod", StringType.INSTANCE);
        query.addScalar("status", StringType.INSTANCE);
        query.addScalar("totalPieceCount", IntegerType.INSTANCE);
        query.addScalar("totalWeightImperial", BigDecimalType.INSTANCE);
        query.addScalar("totalVolumeImperial", BigDecimalType.INSTANCE);
        query.addScalar("curLoc", StringType.INSTANCE);
        query.addScalar("curLocName", StringType.INSTANCE);
        query.addScalar("dispo", StringType.INSTANCE);
        query.addScalar("remarks", StringType.INSTANCE);
        query.addScalar("hotCodes", StringType.INSTANCE);
        query.addScalar("hotCodeKey", StringType.INSTANCE);
        query.addScalar("pickVoyageNo", StringType.INSTANCE);
        query.addScalar("unitno", StringType.INSTANCE);
        query.addScalar("poo", StringType.INSTANCE);
        query.addScalar("pol", StringType.INSTANCE);
        query.addScalar("pod", StringType.INSTANCE);
        query.addScalar("pod", StringType.INSTANCE);
        query.addScalar("fd", StringType.INSTANCE);
        query.addScalar("bookVoyageNo", StringType.INSTANCE);
        query.addScalar("polUnCode", StringType.INSTANCE);
        query.addScalar("transMode", StringType.INSTANCE);
        query.addScalar("unitStatus", StringType.INSTANCE);
        query.addScalar("voyageNo", StringType.INSTANCE);
        query.addScalar("pickedunitNo", StringType.INSTANCE);
        query.addScalar("dimsLength", IntegerType.INSTANCE);
        query.addScalar("dimsWidth", IntegerType.INSTANCE);
        query.addScalar("dimsHeight", IntegerType.INSTANCE);
        query.addScalar("dimsWeight", IntegerType.INSTANCE);
        query.addScalar("dimensionData", StringType.INSTANCE);
        query.addScalar("detailLine", StringType.INSTANCE);
        query.addScalar("bookingHazmatDetails", StringType.INSTANCE);
        query.addScalar("warehouseLine", StringType.INSTANCE);
        query.addScalar("consolidatesFiles", StringType.INSTANCE);
        query.addScalar("consolidatesFileId", StringType.INSTANCE);
        query.addScalar("shortShipSequence", StringType.INSTANCE);
        return query.list();
    }

    public List<BookingUnitsBean> getStuffedListByUnit(Long unitId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT fn.fileId AS fileId, fn.fileNumber AS fileNo, fn.bookingPieceId, 'PICKED' AS STATUS, ");
        sb.append(" fn.remarks as remarks, fn.hazardous AS haz, piece.total_piece AS totalPieceCount, ");
        sb.append(" ROUND(piece.total_weight_imperial, 0) AS totalWeightImperial,ROUND(piece.total_volume_imperial, 0) AS totalVolumeImperial, ");
        sb.append(" (SELECT GROUP_CONCAT(htc.code SEPARATOR '<br>') FROM lcl_booking_hot_code htc WHERE htc.file_number_id = fn.fileId ) AS hotCodes, ");
        sb.append(" (SELECT IF(INSTR(htc.code, '/'),GROUP_CONCAT( LEFT(  htc.code,  INSTR(htc.code, '/') - 1 )), '') FROM  ");
        sb.append(" lcl_booking_hot_code htc WHERE htc.file_number_id = fn.fileId) AS hotCodeKey, ");
        sb.append(" (SELECT GROUP_CONCAT(upper(lbpw.location) SEPARATOR ',') FROM lcl_booking_piece_whse lbpw ");
        sb.append(" JOIN lcl_booking_piece lb ON lb.`id` = lbpw.booking_piece_id WHERE lb.file_number_id =  fn.fileId and lbpw.location <> '') AS warehouseLine, ");
        sb.append(" (SELECT GROUP_CONCAT(upper(detail.stowed_location)) FROM lcl_booking_piece_detail detail ");
        sb.append(" JOIN lcl_booking_piece lb ON lb.`id` = detail.booking_piece_id ");
        sb.append(" WHERE lb.file_number_id =  fn.fileId and detail.stowed_location <> '') AS detailLine, ");
        sb.append(" fn.shortShipSequence, ");
        sb.append(getHazardousDetailsInfo());
        sb.append(" (SELECT CONCAT('<tr><td>PCS</td><td>LEN</td><td>WID</td><td>HEI</td></tr>',");
        sb.append(" IF(lbd.piece_count IS NOT NULL,GROUP_CONCAT(CONCAT('<tr><td>',lbd.piece_count,'</td>'), ");
        sb.append(" CONCAT('<td>',lbd.actual_length,'</td>'),CONCAT('<td>',lbd.actual_width,'</td>'), ");
        sb.append(" CONCAT('<td>',lbd.actual_height,'</td></tr>') SEPARATOR '' ),'<tr><td colspan=4><font size=2 color=red><b>No Dimensions</b></font></td></tr>')) ");
        sb.append(" FROM lcl_booking_piece_detail lbd JOIN lcl_booking_piece lb ON lbd.booking_piece_id = lb.id WHERE lb.file_number_id = fn.fileId) AS dimensionData, ");
        sb.append(this.getConsolidateFileNoQuery());
        sb.append(this.getConsolidateFileIdQuery());
        sb.append(" FROM ( SELECT f.`id` AS fileId,f.`file_number` AS fileNumber, ");
        sb.append(" lbu.`booking_piece_id` AS bookingPieceId, lm.`remarks`  AS remarks, ");
        sb.append(" CONVERT(GROUP_CONCAT(lb.hazmat) USING utf8) AS hazardous, ");
        sb.append(" f.`short_ship_sequence` AS shortShipSequence ");
        sb.append(" FROM lcl_booking_piece_unit lbu JOIN lcl_booking_piece lb ON lb.`id` = lbu.`booking_piece_id` ");
        sb.append(" JOIN lcl_file_number f ON f.`id` = lb.`file_number_id` AND f.`state` <> 'Q' ");
        sb.append(" JOIN lcl_booking bk ON bk.`file_number_id` = f.`id` LEFT JOIN lcl_remarks lm  ON f.id = lm.file_number_id AND lm.type = 'Loading Remarks'  ");
        sb.append(" WHERE lbu.lcl_unit_ss_id =").append(unitId).append(" GROUP BY f.id ) fn ");
        sb.append(" LEFT JOIN  (SELECT lb.file_number_id, ");
        sb.append(" SUM(IF(lb.actual_piece_count IS NOT NULL AND lb.actual_piece_count != 0, lb.actual_piece_count,lb.booked_piece_count)) AS total_piece, ");
        sb.append(" SUM(IF(lb.actual_weight_imperial IS NOT NULL AND lb.actual_weight_imperial != 0.000,lb.actual_weight_imperial,lb.booked_weight_imperial )) AS total_weight_imperial, ");
        sb.append(" SUM(IF(lb.actual_volume_imperial IS NOT NULL AND lb.actual_volume_imperial != 0.000,lb.actual_volume_imperial,lb.booked_volume_imperial)) AS total_volume_imperial ");
        sb.append(" FROM lcl_booking_piece lb GROUP BY lb.file_number_id ) piece ON piece.file_number_id = fn.fileId ");
        sb.append(" GROUP BY fn.fileId ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setResultTransformer(Transformers.aliasToBean(BookingUnitsBean.class));
        query.addScalar("fileId", LongType.INSTANCE);
        query.addScalar("fileNo", StringType.INSTANCE);
        query.addScalar("haz", BooleanType.INSTANCE);
        query.addScalar("status", StringType.INSTANCE);
        query.addScalar("totalPieceCount", IntegerType.INSTANCE);
        query.addScalar("totalWeightImperial", BigDecimalType.INSTANCE);
        query.addScalar("totalVolumeImperial", BigDecimalType.INSTANCE);
        query.addScalar("remarks", StringType.INSTANCE);
        query.addScalar("hotCodes", StringType.INSTANCE);
        query.addScalar("hotCodeKey", StringType.INSTANCE);
        query.addScalar("detailLine", StringType.INSTANCE);
        query.addScalar("warehouseLine", StringType.INSTANCE);
        query.addScalar("dimensionData", StringType.INSTANCE);
        query.addScalar("bookingHazmatDetails", StringType.INSTANCE);
        query.addScalar("shortShipSequence", StringType.INSTANCE);
        query.addScalar("consolidatesFiles", StringType.INSTANCE);
        query.addScalar("consolidatesFileId", StringType.INSTANCE);
        return query.list();
    }

    public List<Object[]> getAllReleasedBookingsTotalWeightMeasure(Integer poo, Integer fd) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT lcl_file.total_piece_count,lcl_file.total_weight_imperial,lcl_file.total_volume_imperial FROM (SELECT ");
        sb.append("SUM(IF(p.actual_piece_count IS NOT NULL AND p.actual_piece_count != 0,p.actual_piece_count,p.booked_piece_count)) AS ");
        sb.append("total_piece_count,SUM(IF(p.actual_weight_imperial IS ");
        sb.append("NOT NULL AND p.actual_weight_imperial != 0.000,p.actual_weight_imperial,p.booked_weight_imperial)) AS total_weight_imperial,");
        sb.append("SUM(IF(p.actual_volume_imperial IS NOT NULL AND p.actual_volume_imperial != 0.000,p.actual_volume_imperial,");
        sb.append("p.booked_volume_imperial)) AS total_volume_imperial FROM  lcl_booking_piece p JOIN lcl_file_number f ON p.file_number_id = ");
        sb.append("f.id AND f.state != 'Q'JOIN lcl_booking b ON f.id = b.file_number_id  JOIN lcl_booking_export lbe ON lbe.`file_number_id` = f.`id`");
        sb.append(" AND ( lbe.`released_datetime` <> '') LEFT JOIN lcl_booking_piece_unit u ");
        sb.append("ON p.id = u.booking_piece_id LEFT JOIN lcl_unit_ss luss ON u.lcl_unit_ss_id = luss.id WHERE u.id IS NULL AND b.pol_id = ");
        sb.append(poo).append(" AND b.pod_id = ").append(fd).append(" GROUP BY f.id) lcl_file");
        Query queryObject = getSession().createSQLQuery(sb.toString());
        return queryObject.list();
    }

    public List<BookingUnitsBean> getAllUnitsByVoyage(Long headerId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT v.id AS unitId,v.unit_no AS label1,COUNT(v.file_number_id) AS count,IF(v.total_piece_count IS NOT NULL,");
        queryBuilder.append("SUM(total_piece_count),0.00) AS totalPieceCount, IF(v.total_volume_imperial IS NOT NULL ,");
        queryBuilder.append("SUM(total_volume_imperial),0.00) AS totalVolumeImperial, IF(v.total_weight_imperial IS NOT NULL ,");
        queryBuilder.append("SUM(v.total_weight_imperial),0.00) AS totalWeightImperial,v.unit_ss_id AS unitssId,UPPER(v.containerType) AS unitType ,v.volumeImperial AS unitCapacity,v.shortDesc AS unitDesc,v.loadByName AS loadBy  FROM (SELECT lu.id,lu.unit_no,");
        queryBuilder.append("SUM(IF(p.actual_piece_count IS NOT NULL AND p.actual_piece_count != 0,p.actual_piece_count,p.booked_piece_count)) AS ");
        queryBuilder.append("total_piece_count,SUM(IF(p.actual_weight_imperial IS NOT NULL AND p.actual_weight_imperial != 0.000,p.actual_weight_imperial,");
        queryBuilder.append("p.booked_weight_imperial)) AS total_weight_imperial,SUM(IF(p.actual_volume_imperial IS NOT NULL AND p.actual_volume_imperial ");
        queryBuilder.append("!= 0.000,p.actual_volume_imperial,p.booked_volume_imperial)) AS total_volume_imperial,b.file_number_id,luss.id AS unit_ss_id ,ut.description AS containerType ,");
        queryBuilder.append("ut.volume_imperial AS volumeImperial,ut.short_desc AS shortDesc,");
        queryBuilder.append(" (SELECT CONCAT(ud.first_name, ' ', ud.last_name) FROM lcl_unit_whse wh ");
        queryBuilder.append("JOIN user_details ud ON ud.user_id = wh.stuffed_user_id WHERE wh.unit_id = lu.id and wh.ss_header_id = luss.ss_header_id ORDER BY wh.id DESC LIMIT 1 ) AS loadByName ");
        queryBuilder.append("FROM lcl_unit_ss luss JOIN lcl_unit lu ON luss.unit_id = lu.id LEFT JOIN lcl_booking_piece_unit u ON u.lcl_unit_ss_id = luss.id");
        queryBuilder.append(" LEFT JOIN lcl_booking_piece p ON u.booking_piece_id = p.id LEFT JOIN lcl_booking b ON p.file_number_id = b.file_number_id LEFT JOIN unit_type ut ON ut.id = lu.unit_type_id ");
        queryBuilder.append(" WHERE luss.ss_header_id = ");
        queryBuilder.append(headerId).append(" GROUP BY luss.unit_id,b.file_number_id)v GROUP BY v.unit_no");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(BookingUnitsBean.class));
        query.addScalar("unitId", LongType.INSTANCE);
        query.addScalar("unitssId", LongType.INSTANCE);
        query.addScalar("label1", StringType.INSTANCE);
        query.addScalar("count", IntegerType.INSTANCE);
        query.addScalar("totalPieceCount", IntegerType.INSTANCE);
        query.addScalar("totalWeightImperial", BigDecimalType.INSTANCE);
        query.addScalar("totalVolumeImperial", BigDecimalType.INSTANCE);
        query.addScalar("unitType", StringType.INSTANCE);
        query.addScalar("unitDesc", StringType.INSTANCE);
        query.addScalar("unitCapacity", BigDecimalType.INSTANCE);
        query.addScalar("loadBy", StringType.INSTANCE);
        List<BookingUnitsBean> drList = query.list();
        return drList;
    }

    public Character getUnitStatus(Long unitId, Long scheduleNo) throws Exception {
        String query = "SELECT STATUS FROM lcl_unit_ss WHERE unit_id='" + unitId + "' AND ss_header_id='" + scheduleNo + "'";
        Character ch = (Character) getSession().createSQLQuery(query).list().get(0);
        return ch;
    }

    public List<Object[]> getUnitNoCreatedDate(Long fileid) throws Exception {
        List<Object[]> unitList = null;
        String queryString = "SELECT u.unit_no , u.entered_datetime FROM lcl_booking_piece lbp JOIN lcl_booking_piece_unit lb ON lbp.id = lb.booking_piece_id"
                + " JOIN lcl_unit_ss ss ON ss.id = lb.lcl_unit_ss_id JOIN lcl_unit u ON u.id = ss.unit_id WHERE lbp.file_number_id = " + fileid + " GROUP BY u.unit_no,u.entered_datetime";
        Query queryObject = getSession().createSQLQuery(queryString);
        unitList = (List<Object[]>) queryObject.list();
        return unitList;
    }

    public List<ManifestBean> getAllDRSForManifestByUnitSSId(Long unitSSId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT f.file_number AS fileNo,BlNumberSystemForLclExports(bl.file_number_id) AS blNo, ");
        sb.append(" f.status AS STATUS, f.state AS state, bl.posted_by_user_id AS postedByUserId ,  lbe.`no_bl_required` AS isNoBLRequired   ");
        sb.append(" FROM (SELECT bp.`file_number_id`  AS fileId FROM lcl_booking_piece bp ");
        sb.append(" JOIN lcl_booking_piece_unit bpu ON bpu.`booking_piece_id` = bp.`id` WHERE bpu.`lcl_unit_ss_id` =:unitSSId GROUP BY  bp.file_number_id) f  ");
        sb.append(" JOIN lcl_file_number f ON f.fileId = f.id  AND f.state != 'Q'  JOIN  lcl_booking_export   lbe  ON f.fileId = lbe.file_number_id  ");
        sb.append(" LEFT JOIN lcl_booking b ON f.id = b.file_number_id    ");
        sb.append(" LEFT JOIN lcl_bl bl ON bl.file_number_id = getHouseBLForConsolidateDr(f.fileId) ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameter("unitSSId", unitSSId);
        query.setResultTransformer(Transformers.aliasToBean(ManifestBean.class));
        query.addScalar("fileNo", StringType.INSTANCE);
        query.addScalar("blNo", StringType.INSTANCE);
        query.addScalar("status", StringType.INSTANCE);
        query.addScalar("state", StringType.INSTANCE);
        query.addScalar("postedByUserId", IntegerType.INSTANCE);
        query.addScalar("isNoBLRequired", BooleanType.INSTANCE);
        List<ManifestBean> drList = query.list();
        return drList;
    }

    public List<ManifestBean> getAllDRSForViewManifestByUnitSSId(Long unitSSId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT lcl_file.file_number AS fileNo,lcl_file.blNo AS blNo,lcl_file.total_piece_count AS totalPieceCount,");
        queryBuilder.append("lcl_file.total_weight_imperial AS totalWeightImperial,lcl_file.total_volume_imperial AS ");
        queryBuilder.append("totalVolumeImperial,shipperName AS shipperName,consigneeName AS consigneeName,forwarderName AS forwarderName,");
        queryBuilder.append("billingType AS billingType,FORMAT(IF(SUM(t.balance) IS NOT NULL,SUM(t.balance),0.00),2) AS totalBilledAmount FROM ");
        queryBuilder.append("(SELECT f.id,f.file_number,CONCAT('ECCI','-',UPPER(IF (tr.unLocationCode1 IS NOT NULL,RIGHT(tr.unLocationCode1,3),RIGHT(org.un_loc_code,3))),'-',IF(dest.bl_numbering='Y',RIGHT(dest.un_loc_code,3),dest.un_loc_code),");
        queryBuilder.append("'-',f.file_number) AS blNo,SUM(IF(p.actual_piece_count IS NOT NULL AND p.actual_piece_count != 0,p.actual_piece_count,");
        queryBuilder.append("p.booked_piece_count)) AS total_piece_count,SUM(IF(p.actual_weight_imperial IS NOT NULL AND p.actual_weight_imperial ");
        queryBuilder.append("!= 0.000,p.actual_weight_imperial,p.booked_weight_imperial)) AS total_weight_imperial,SUM(IF(p.actual_volume_imperial IS ");
        queryBuilder.append("NOT NULL AND p.actual_volume_imperial != 0.000,p.actual_volume_imperial,p.booked_volume_imperial)) AS total_volume_imperial,");
        queryBuilder.append("ship.acct_name AS shipperName,cons.acct_name AS consigneeName,fwd.acct_name AS forwarderName,bl.billing_type AS billingType ");
        queryBuilder.append("FROM lcl_booking_piece_unit u JOIN lcl_booking_piece p ON u.booking_piece_id = p.id JOIN lcl_file_number f ON ");
        queryBuilder.append("p.file_number_id = f.id AND f.state != 'Q' LEFT JOIN lcl_bl bl  ON f.id = bl.file_number_id  JOIN un_location org ON ");
        queryBuilder.append("org.id = bl.poo_id LEFT JOIN un_location dest ON dest.id = IF (bl.fd_id <> NULL AND bl.fd_id  <> '',bl.fd_id,bl.`pod_id`)  LEFT JOIN trading_partner ship ON bl.ship_acct_no = ");
        queryBuilder.append("ship.acct_no LEFT JOIN trading_partner cons ON bl.cons_acct_no = cons.acct_no LEFT JOIN trading_partner fwd ON ");
        queryBuilder.append("bl.fwd_acct_no = fwd.acct_no LEFT JOIN terminal tr ON bl.billing_terminal = tr.trmnum ");
        queryBuilder.append("WHERE u.lcl_unit_ss_id = ");
        queryBuilder.append(unitSSId);
        queryBuilder.append(" GROUP BY f.id) lcl_file LEFT JOIN  TRANSACTION t ON t.transaction_type = 'AR' AND t.bill_ladding_no =  lcl_file.blNo ");
        queryBuilder.append("group by lcl_file.id");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(ManifestBean.class));
        query.addScalar("fileNo", StringType.INSTANCE);
        query.addScalar("blNo", StringType.INSTANCE);
        query.addScalar("totalPieceCount", IntegerType.INSTANCE);
        query.addScalar("totalWeightImperial", BigDecimalType.INSTANCE);
        query.addScalar("totalVolumeImperial", BigDecimalType.INSTANCE);
        query.addScalar("totalBilledAmount", StringType.INSTANCE);
        query.addScalar("shipperName", StringType.INSTANCE);
        query.addScalar("consigneeName", StringType.INSTANCE);
        query.addScalar("forwarderName", StringType.INSTANCE);
        query.addScalar("billingType", StringType.INSTANCE);
        return query.list();
    }

    public List<ManifestBean> getAllDRSForViewUnitExceptionByUnitSSId(Long unitSSId, String unitExceptionFlag) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT lcl_file.id as fileNumberId, lcl_file.file_number AS fileNo,lcl_file.blNo AS blNo,lcl_file.total_piece_count AS totalPieceCount,");
        queryBuilder.append("lcl_file.total_weight_imperial AS totalWeightImperial,lcl_file.total_volume_imperial AS totalVolumeImperial,");
        queryBuilder.append("blInvoiceNo AS blInvoiceNo,lcl_file.unitException AS unitException,lcl_file.enteredDatetime AS enteredDatetime,lcl_file.modifiedBy AS modifiedBy,lcl_file.consigneeName AS consigneeName FROM ");
        queryBuilder.append("(SELECT f.id ,f.file_number,IF(f.state='BL',BlNumberSystemForLclExports(f.id),'') AS blNo,");
        queryBuilder.append("SUM(IF(p.actual_piece_count IS NOT NULL AND p.actual_piece_count != 0,p.actual_piece_count,p.booked_piece_count)) AS total_piece_count,");
        queryBuilder.append("SUM(IF(p.actual_weight_imperial IS NOT NULL AND p.actual_weight_imperial != 0.000,p.actual_weight_imperial,p.booked_weight_imperial)) AS total_weight_imperial,");
        queryBuilder.append("SUM(IF(p.actual_volume_imperial IS NOT NULL AND p.actual_volume_imperial != 0.000,p.actual_volume_imperial,p.booked_volume_imperial)) AS total_volume_imperial,");
        queryBuilder.append("tr.invoice_number AS blInvoiceNo,lr.remarks AS unitException,lr.entered_datetime AS enteredDatetime,usd.first_name AS modifiedBy,lc.`company_name` AS consigneeName FROM lcl_booking_piece_unit u JOIN lcl_booking_piece p ");
        queryBuilder.append("ON u.booking_piece_id = p.id JOIN lcl_file_number f ON p.file_number_id = f.id AND f.state != 'Q' JOIN lcl_booking b ON f.id = b.file_number_id ");
        queryBuilder.append("LEFT JOIN lcl_bl bl ON bl.`file_number_id`= getHouseBLForConsolidateDr (f.id) JOIN un_location org ON org.id = b.poo_id JOIN un_location dest ON dest.id = b.fd_id LEFT JOIN TRANSACTION tr ");
        queryBuilder.append("ON tr.drcpt = f.file_number LEFT JOIN lcl_remarks lr ON f.id = lr.file_number_id and lr.type = 'Unit Exception' LEFT JOIN user_details usd ON lr.modified_by_user_id = usd.user_id ");
        queryBuilder.append("LEFT JOIN lcl_contact lc ON bl.`cons_contact_id` = lc.`id`   WHERE u.lcl_unit_ss_id =");
        // LEFT JOIN lcl_schedule ls ON b.master_schedule_id = ls.id
        queryBuilder.append(unitSSId);
        queryBuilder.append(" GROUP BY f.id) lcl_file ");
        if (!unitExceptionFlag.equals("true")) {
            queryBuilder.append(" WHERE lcl_file.unitException NOT LIKE ''");
        }
        queryBuilder.append(" group by lcl_file.id ORDER BY lcl_file.unitException ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(ManifestBean.class));
        query.addScalar("fileNumberId", IntegerType.INSTANCE);
        query.addScalar("fileNo", StringType.INSTANCE);
        query.addScalar("blNo", StringType.INSTANCE);
        query.addScalar("totalPieceCount", IntegerType.INSTANCE);
        query.addScalar("totalWeightImperial", BigDecimalType.INSTANCE);
        query.addScalar("totalVolumeImperial", BigDecimalType.INSTANCE);
        query.addScalar("blInvoiceNo", StringType.INSTANCE);
        query.addScalar("unitException", StringType.INSTANCE);
        query.addScalar("enteredDatetime", DateType.INSTANCE);
        query.addScalar("modifiedBy", StringType.INSTANCE);
        query.addScalar("consigneeName", StringType.INSTANCE);
        return query.list();
    }

    public List<ImportsManifestBean> getDRSForImportsDisp(Long unitSSId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT lcl_file.fileId AS fileId,lcl_file.fileNo AS fileNo,lcl_file.totalCharges AS totalCharges,lcl_file.transShipment ");
        queryBuilder.append("AS transShipment,lcl_file.consigneeName AS consigneeName,lcl_file.consigneeNo as consAcct,");
        queryBuilder.append("lcl_file.notifyName AS notifyName,lcl_file.notifyNo as notifyAcct,billToParty AS billToParty,");
        queryBuilder.append("GROUP_CONCAT(DISTINCT(IF(code_f.code IN ('E1', 'E2', 'E3'),LOWER(ct.email),NULL))) AS consigneeEmail,");
        queryBuilder.append("GROUP_CONCAT(DISTINCT(IF(code_fn.code IN ('E1', 'E2', 'E3'),LOWER(ctn.email),NULL)))AS notifyEmail,  ");
        queryBuilder.append("GROUP_CONCAT(DISTINCT(IF(code_f.code IN ('F1', 'F2', 'F3'),LOWER(ct.fax),NULL))) AS consigneeFax,");
        queryBuilder.append("GROUP_CONCAT(DISTINCT(IF(code_fn.code IN ('F1', 'F2', 'F3'),LOWER(ctn.fax),NULL)))AS notifyFax,");
//        queryBuilder.append("GROUP_CONCAT(DISTINCT(IF(code_f.code IN ('E1','E2','E3'), LOWER(ct.email),IF(code_fn.code IN ('E1','E2','E3'), LOWER(ctn.email), NULL)))) AS arrivalNoticeEmail,");
//        queryBuilder.append("GROUP_CONCAT(DISTINCT(IF(code_f.code IN ('f1','f2','f3'), LOWER(ct.fax),IF(code_fn.code IN ('f1','f2','f3'), LOWER(ctn.fax), NULL)))) AS arrivalNoticeFax,");
        queryBuilder.append("lcl_file.destination AS destination,lcl_file.destinationName AS ");
        queryBuilder.append("destinationName,lcl_file.");
        queryBuilder.append("destinationCountry AS destinationCountry,lcl_file.totalIPI AS totalIPI FROM (SELECT file.id AS fileId,file.file_number AS ");
        queryBuilder.append("fileNo,SUM(IF(chg.ar_bill_to_party IN('N','C') AND gm.bluescreen_chargecode!='1607',");
        queryBuilder.append("chg.ar_amount+chg.adjustment_amount,0)) AS totalCharges,SUM(if(gm.bluescreen_chargecode='1607',chg.ar_amount+chg.");
        queryBuilder.append("adjustment_amount,0)) as totalIPI,IF(lbi.transshipment = 0, 'No', 'Yes') AS transShipment,cons.acct_no AS consigneeNo,");
        queryBuilder.append("cons.acct_name AS consigneeName,noty.acct_name AS notifyName,noty.acct_no as notifyNo,IF(lb.bill_to_party = 'A','Agent',IF(lb.bill_to_party = 'C',");
        queryBuilder.append("'Consignee',IF(lb.bill_to_party = 'N','Notify Party','Third Party'))) AS billToParty,dest.un_loc_code AS destination,");
        queryBuilder.append("dest.un_loc_name AS destinationName,IF(depstate.code IS NULL,dests.codedesc,depstate.code) AS destinationCountry ");
        queryBuilder.append("FROM lcl_unit_ss unit_ss JOIN lcl_booking_piece_unit unit_pieces ON unit_pieces.lcl_unit_ss_id = ");
        queryBuilder.append("unit_ss.id JOIN lcl_booking_piece book_piece ON unit_pieces.booking_piece_id = book_piece.id JOIN lcl_unit unit ON unit_ss.");
        queryBuilder.append("unit_id = unit.id JOIN lcl_file_number FILE ON book_piece.file_number_id = file.id JOIN lcl_booking_import lbi ON ");
        queryBuilder.append("lbi.file_number_id = file.id JOIN lcl_booking lb ON lb.file_number_id = file.id LEFT JOIN trading_partner cons ON ");
        queryBuilder.append("lb.cons_acct_no = cons.acct_no  LEFT JOIN trading_partner noty ON lb.noty_acct_no = noty.acct_no LEFT JOIN ");
        queryBuilder.append("lcl_booking_ac chg ON chg.file_number_id = file.id LEFT JOIN gl_mapping gm ON ");
        queryBuilder.append("gm.id=chg.ar_gl_mapping_id  JOIN un_location dest ON dest.id = lb.fd_id LEFT JOIN genericcode_dup dests ON dest.countrycode ");
        queryBuilder.append("= dests.id LEFT JOIN genericcode_dup depstate ON dest.statecode=depstate.id  WHERE FILE.status!='M' AND unit_ss.id = ");
        queryBuilder.append(unitSSId);
        queryBuilder.append(" GROUP BY file.id) lcl_file  LEFT JOIN cust_contact ct ON ct.acct_no = consigneeNo LEFT JOIN genericcode_dup code_f ON ct.code_f = code_f.id ");
        queryBuilder.append("LEFT JOIN cust_contact ctn ON ctn.acct_no = notifyNo LEFT JOIN genericcode_dup code_fn ON ctn.code_f = code_fn.id ");
        queryBuilder.append(" GROUP BY lcl_file.fileId DESC");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(ImportsManifestBean.class));
        query.addScalar("fileId", LongType.INSTANCE);
        query.addScalar("consigneeName", StringType.INSTANCE);
        query.addScalar("fileNo", StringType.INSTANCE);
        query.addScalar("totalCharges", DoubleType.INSTANCE);
        query.addScalar("transShipment", StringType.INSTANCE);
        query.addScalar("consigneeName", StringType.INSTANCE);
        query.addScalar("consAcct", StringType.INSTANCE);
        query.addScalar("notifyName", StringType.INSTANCE);
        query.addScalar("notifyAcct", StringType.INSTANCE);
        query.addScalar("billToParty", StringType.INSTANCE);
        query.addScalar("consigneeEmail", StringType.INSTANCE);
        query.addScalar("consigneeFax", StringType.INSTANCE);
        query.addScalar("notifyEmail", StringType.INSTANCE);
        query.addScalar("notifyFax", StringType.INSTANCE);
        query.addScalar("destination", StringType.INSTANCE);
        query.addScalar("destinationName", StringType.INSTANCE);
        query.addScalar("destinationCountry", StringType.INSTANCE);
        query.addScalar("totalIPI", BigDecimalType.INSTANCE);
        return query.list();
    }

    public Boolean getHazmat(Long unitSsId) throws Exception {//In Unit Screen
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT if(count(*)>0, true,false) as hazStatus FROM ");
        queryStr.append("  lcl_booking_piece bp  JOIN lcl_file_number f ON bp.file_number_id=f.id ");
        queryStr.append(" JOIN lcl_booking_hazmat hz ON hz.file_number_id = bp.file_number_id ");
        queryStr.append(" JOIN lcl_booking_piece_unit pu ON pu.booking_piece_id = bp.id ");
        queryStr.append(" WHERE pu.lcl_unit_ss_id =:unitSsId ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setLong("unitSsId", unitSsId);
        query.addScalar("hazStatus", BooleanType.INSTANCE);
        return (Boolean) query.uniqueResult();
    }

    public Boolean getInbond(Long unitSsId) throws Exception {//In Unit Screen
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT if(count(*)>0, true,false) as inbondStatus FROM ");
        queryStr.append("  lcl_inbond bo JOIN lcl_file_number f ON bo.file_number_id=f.id ");
        queryStr.append("  JOIN lcl_booking_piece bp ON bo.file_number_id = bp.file_number_id ");
        queryStr.append(" JOIN lcl_booking_piece_unit pu ON pu.booking_piece_id = bp.id ");
        queryStr.append(" WHERE pu.lcl_unit_ss_id =:unitSsId ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setLong("unitSsId", unitSsId);
        query.addScalar("inbondStatus", BooleanType.INSTANCE);
        return (Boolean) query.uniqueResult();
    }

    public String[] getBkgPieceUnitId(String fileId) throws Exception {
        StringBuilder sb = new StringBuilder();
        String commDetails[] = new String[3];
        sb.append("SELECT CONVERT(GROUP_CONCAT(lbpu.booking_piece_id) USING utf8),lu.unit_no,lsh.schedule_no FROM lcl_booking_piece_unit lbpu ");
        sb.append(" JOIN lcl_file_number lfn ON lfn.id IN(").append(fileId).append(")");
        sb.append(" JOIN lcl_booking_piece lbc ON lbc.file_number_id=lfn.id ");
        sb.append(" JOIN lcl_unit_ss lus ON lus.id=lbpu.lcl_unit_ss_id JOIN lcl_unit lu ON lu.id=lus.unit_id ");
        sb.append("JOIN lcl_ss_header lsh ON lsh.id=lus.ss_header_id ");
        sb.append(" WHERE lbpu.booking_piece_id=lbc.id");
        Query queryObj = getCurrentSession().createSQLQuery(sb.toString());
        List commdetailsList = queryObj.list();
        if (commdetailsList != null && !commdetailsList.isEmpty()) {
            Object[] commDetailsObj = (Object[]) commdetailsList.get(0);
            if (commDetailsObj != null) {
                if (commDetailsObj[0] != null) {
                    commDetails[0] = commDetailsObj[0].toString();
                }
                if (commDetailsObj[1] != null) {
                    commDetails[1] = commDetailsObj[1].toString();
                }
                if (commDetailsObj[2] != null) {
                    commDetails[2] = commDetailsObj[2].toString();
                }
            }
        }
        return commDetails;
    }

    public Integer unlinkDrfromBkgUnit(String bkgPieceId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("delete from lcl_booking_piece_unit where booking_piece_id IN (").append(bkgPieceId).append(")");
        Query queryObject = getSession().createSQLQuery(sb.toString());
        int count = queryObject.executeUpdate();
        return count;
    }

    public List getCommodityValues(Long unitSsId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ROUND(lcl.cft,2),ROUND(lcl.cbm,2),ROUND(lcl.lbs,2),ROUND(lcl.kgs,2),lcl.pcs FROM (SELECT ");
        sb.append("SUM(IF(bp.actual_volume_imperial IS NOT NULL AND bp.actual_volume_imperial != 0.000,bp.actual_volume_imperial,bp.booked_volume_imperial)) AS cft,");
        sb.append("SUM(IF(bp.actual_volume_metric IS NOT NULL AND bp.actual_volume_metric != 0.000,bp.actual_volume_metric,bp.booked_volume_metric)) AS cbm,");
        sb.append("SUM(IF(bp.actual_weight_imperial IS NOT NULL AND bp.actual_weight_imperial != 0.000,bp.actual_weight_imperial,bp.booked_weight_imperial)) AS lbs,");
        sb.append("SUM(IF(bp.actual_weight_metric IS NOT NULL AND bp.actual_weight_metric != 0.000,bp.actual_weight_metric,bp.booked_weight_metric)) AS kgs,  ");
        sb.append("SUM(IF(bp.actual_piece_count IS NOT NULL AND bp.actual_piece_count != 0,bp.actual_piece_count,bp.booked_piece_count)) as pcs ");
        sb.append("FROM lcl_booking_piece bp LEFT JOIN lcl_booking_piece_unit bpu ON bpu.booking_piece_id = bp.id WHERE bpu.lcl_unit_ss_id =");
        sb.append(unitSsId);
        sb.append(")lcl");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(sb.toString());
        List li = queryObject.list();
        return li;
    }

    public BigInteger getNumDRCount(Long unitSsId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT COUNT(DISTINCT fn.file_number) FROM lcl_booking_piece_unit bpu LEFT JOIN lcl_booking_piece bp ON bpu.booking_piece_id=bp.id ");
        sb.append("LEFT JOIN lcl_file_number fn ON bp.file_number_id=fn.id WHERE bpu.lcl_unit_ss_id=").append(unitSsId);
        BigInteger num = (BigInteger) getSession().createSQLQuery(sb.toString()).uniqueResult();
        return num;
    }

    public LclUnit getUnit(String unitNo) throws Exception {
        return (LclUnit) getCurrentSession().getNamedQuery("LclUnit.findByUnitNo").setString("unitNo", unitNo).uniqueResult();
    }

    public String getUnitNo(long unitId) throws Exception {
        String queryString = "SELECT u.unit_no FROM  lcl_unit AS u  LEFT JOIN lcl_unit_ss AS ss ON ss.unit_id =u.id WHERE ss.id='" + unitId + ",";
        Query queryobject = getCurrentSession().createSQLQuery(queryString);
        return queryobject.toString();
    }

    public String getUnitNoByFileId(Long fileNumberId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT lu.unit_no FROM lcl_file_number lfn ");
        sb.append("JOIN lcl_booking_piece lbp ON lbp.file_number_id = lfn.id ");
        sb.append("JOIN lcl_booking_piece_unit lbpu ON lbpu.booking_piece_id = lbp.id ");
        sb.append("JOIN lcl_unit_ss lus ON lus.id = lbpu.lcl_unit_ss_id ");
        sb.append("JOIN lcl_unit lu ON lu.id = lus.unit_id ");
        sb.append("WHERE lfn.id = :fileNumberId");
        Query queryobject = getCurrentSession().createSQLQuery(sb.toString());
        queryobject.setMaxResults(1);
        queryobject.setLong("fileNumberId", fileNumberId);
        return queryobject.uniqueResult() != null ? queryobject.uniqueResult().toString() : "";
    }

    public String getCommodityPieceValues() {
        StringBuilder piece = new StringBuilder();
        piece.append(" LEFT JOIN (SELECT lb.file_number_id,");
        piece.append(" SUM(IF(lb.actual_piece_count IS NOT NULL AND lb.actual_piece_count != 0,lb.actual_piece_count,lb.booked_piece_count)) AS total_piece, ");
        piece.append(" SUM(IF(lb.actual_weight_imperial IS NOT NULL AND lb.actual_weight_imperial != 0.000,lb.actual_weight_imperial,lb.booked_weight_imperial)) AS total_weight_imperial,");
        piece.append(" SUM(IF(lb.actual_volume_imperial IS NOT NULL AND lb.actual_volume_imperial != 0.000,lb.actual_volume_imperial,lb.booked_volume_imperial)) AS total_volume_imperial");
        piece.append(" FROM lcl_booking_piece lb GROUP BY lb.file_number_id) piece ON piece.file_number_id = f.id ");
        return piece.toString();
    }

    public List<BookingUnitsBean> getReleasedBookingsForDomestic(LclAddVoyageForm lclAddVoyageForm) throws Exception {
        StringBuilder sb = new StringBuilder();
        boolean flag = false;
        sb.append("  SELECT  fn.fileId AS fileId, fn.fileNumber AS fileNo,  fn.hazardous  AS haz,  ");
        sb.append("  fn.bookingType AS bookingType, impol.`un_loc_code` AS impol, impod.`un_loc_code` AS impod, ");
        sb.append("  pod.`un_loc_name` AS pod , IF((fn.transMode = 'T'),'PICKED', '') AS status, piece.total_piece AS totalPieceCount,  ");
        sb.append("  ROUND(piece.total_weight_imperial, 0) AS totalWeightImperial, ROUND(piece.total_volume_imperial, 0) AS totalVolumeImperial, ");
        sb.append(" (SELECT un_loc_code FROM un_location WHERE id = fn.unLocId) AS curLoc, (SELECT un_loc_name FROM un_location WHERE id = fn.unLocId) AS curLocName, ");
        sb.append("  fn.dispo as dispo, lrm.`remarks` AS remarks,");
        sb.append(" (SELECT GROUP_CONCAT(htc.code SEPARATOR '<br>') FROM lcl_booking_hot_code htc WHERE htc.file_number_id = fn.fileId ) AS hotCodes, ");
        sb.append(" (SELECT IF(INSTR(htc.code, '/'),GROUP_CONCAT( LEFT(  htc.code,  INSTR(htc.code, '/') - 1 )), '') FROM  lcl_booking_hot_code htc WHERE htc.file_number_id = fn.fileId) AS hotCodeKey, ");
        sb.append(" lsh.`schedule_no` AS pickVoyageNo, lu.`unit_no` AS unitno, upper(poo.`un_loc_code`) AS poo, ");
        sb.append(" (IF (impol.un_loc_name IS NOT NULL,impol.un_loc_name,pol.un_loc_name)) AS pol, ");
        sb.append(" pod.un_loc_name AS pod, fd.un_loc_name AS fd, lsc.schedule_no AS bookVoyageNo, ");
        sb.append(" (IF (impol.un_loc_code IS NOT NULL,impol.un_loc_code, pol.un_loc_code)) AS polUnCode, ");
        sb.append(" (SELECT GROUP_CONCAT(upper(lbpw.location) SEPARATOR ',') FROM lcl_booking_piece_whse lbpw ");
        sb.append(" JOIN lcl_booking_piece lb ON lb.`id` = lbpw.booking_piece_id WHERE lb.file_number_id =  fn.fileId and lbpw.location <> '') AS warehouseLine, ");
        sb.append(" (SELECT GROUP_CONCAT(upper(detail.stowed_location)) FROM lcl_booking_piece_detail detail ");
        sb.append(" JOIN lcl_booking_piece lb ON lb.`id` = detail.booking_piece_id ");
        sb.append(" WHERE lb.file_number_id =  fn.fileId and detail.stowed_location <> '') AS detailLine, ");
        sb.append(" fn.shortShipSequence, ");
        sb.append(getHazardousDetailsInfo());
        sb.append(getLengthWeightFromBkg());
        sb.append(" fn.transMode AS transMode, luss.status AS unitStatus, fn.voyageNo as voyageNo,fn.unitNo as pickedunitNo, ");
        sb.append(" (SELECT CONCAT('<tr><td>PCS</td><td>LEN</td><td>WID</td><td>HEI</td></tr>',");
        sb.append(" IF(lbd.piece_count IS NOT NULL,GROUP_CONCAT(CONCAT('<tr><td>',lbd.piece_count,'</td>'), ");
        sb.append(" CONCAT('<td>',lbd.actual_length,'</td>'),CONCAT('<td>',lbd.actual_width,'</td>'), ");
        sb.append(" CONCAT('<td>',lbd.actual_height,'</td></tr>') SEPARATOR '' ),'<tr><td colspan=4> ");
        sb.append(" <font size=2 color=red><b>No Dimensions</b></font></td></tr>')) ");
        sb.append(" FROM lcl_booking_piece_detail lbd JOIN lcl_booking_piece lb ON lbd.booking_piece_id = lb.id ");
        sb.append(" WHERE lb.file_number_id = fn.fileId) AS dimensionData, ");
        sb.append(getConsolidateFileNoQuery());
        sb.append(getConsolidateFileIdQuery());
        sb.append(" from (").append(mainSubQuery(lclAddVoyageForm)).append(") fn ");
        sb.append(" JOIN un_location poo ON poo.`id` = fn.pooId JOIN un_location pol ON pol.`id` = fn.polId  ");
        sb.append(" JOIN un_location pod ON pod.`id` = fn.podId JOIN un_location fd  ON fd.`id` = fn.fdId ");
        sb.append(" LEFT  JOIN lcl_booking_import lbm  ON lbm.`file_number_id` = fn.fileId ");
        sb.append(" LEFT JOIN un_location impol ON impol.id = lbm.`usa_port_of_exit_id` ");
        sb.append(" LEFT JOIN un_location impod ON impod.id = lbm.`foreign_port_of_discharge_id` ");
        sb.append(" LEFT JOIN lcl_ss_header lsc ON lsc.id= fn.masterId ");
        sb.append(" LEFT JOIN lcl_remarks lrm ON lrm.`file_number_id` =  fn.fileId  AND lrm.`type` ='Loading Remarks' ");
        sb.append(" LEFT JOIN lcl_booking_piece_unit lbu ON lbu.`booking_piece_id` = fn.bookingPieceId ");
        sb.append(" LEFT JOIN lcl_unit_ss luss ON luss.id = lbu.`lcl_unit_ss_id` ");
        sb.append(" AND luss.`ss_header_id` = (SELECT id FROM lcl_ss_header WHERE id = luss.`ss_header_id` ");
        sb.append(" AND service_type NOT IN('I'))  ");
        sb.append(" LEFT JOIN lcl_unit lu ON lu.`id` = luss.`unit_id` LEFT JOIN lcl_ss_header lsh ON lsh.`id` = luss.`ss_header_id` ");
        sb.append(" LEFT JOIN  (SELECT lb.file_number_id, ");
        sb.append(" SUM(IF(lb.actual_piece_count IS NOT NULL AND lb.actual_piece_count != 0, lb.actual_piece_count,lb.booked_piece_count)) AS total_piece, ");
        sb.append(" SUM(IF(lb.actual_weight_imperial IS NOT NULL AND lb.actual_weight_imperial != 0.000,lb.actual_weight_imperial,lb.booked_weight_imperial )) AS total_weight_imperial, ");
        sb.append(" SUM(IF(lb.actual_volume_imperial IS NOT NULL AND lb.actual_volume_imperial != 0.000,lb.actual_volume_imperial,lb.booked_volume_imperial)) AS total_volume_imperial ");
        sb.append(" FROM lcl_booking_piece lb GROUP BY lb.file_number_id ) piece ON piece.file_number_id = fn.fileId ");
        
        if (lclAddVoyageForm.isShowPreReleased()) {
            sb.append(" JOIN lcl_booking_export lbe ON lbe.file_number_id = fn.fileId AND prerelease_datetime is not null ");
        }
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getUnitssId())) {
            sb.append(" where fn.fileId NOT IN (SELECT f.id FROM lcl_booking_piece b JOIN lcl_booking_piece_unit bu  ON bu.`booking_piece_id` = b.`id` ");
            sb.append(" JOIN lcl_unit_ss lus ON lus.id = bu.`lcl_unit_ss_id` JOIN lcl_file_number f ON f.id = b.`file_number_id` WHERE lus.id =").append(lclAddVoyageForm.getUnitssId()).append(")");
            flag = true;
        }
        sb.append(flag ? "AND" : "WHERE").append(" fn.transMode IS NULL  ");
        sb.append(" order by CONCAT(IF(fn.shortshipSequence = 0 ,SUBSTR(poo.`un_loc_code`,3,5), ");
        sb.append(" CONCAT('ZZ',fn.shortshipSequence)) ,'-',fn.fileNumber) ASC  ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());

        query.setResultTransformer(Transformers.aliasToBean(BookingUnitsBean.class));
        query.addScalar("fileId", LongType.INSTANCE);
        query.addScalar("fileNo", StringType.INSTANCE);
        query.addScalar("haz", BooleanType.INSTANCE);
        query.addScalar("bookingType", StringType.INSTANCE);
        query.addScalar("impol", StringType.INSTANCE);
        query.addScalar("impod", StringType.INSTANCE);
        query.addScalar("pod", StringType.INSTANCE);
        query.addScalar("status", StringType.INSTANCE);
        query.addScalar("totalPieceCount", IntegerType.INSTANCE);
        query.addScalar("totalWeightImperial", BigDecimalType.INSTANCE);
        query.addScalar("totalVolumeImperial", BigDecimalType.INSTANCE);
        query.addScalar("curLoc", StringType.INSTANCE);
        query.addScalar("curLocName", StringType.INSTANCE);
        query.addScalar("dispo", StringType.INSTANCE);
        query.addScalar("remarks", StringType.INSTANCE);
        query.addScalar("hotCodes", StringType.INSTANCE);
        query.addScalar("hotCodeKey", StringType.INSTANCE);
        query.addScalar("pickVoyageNo", StringType.INSTANCE);
        query.addScalar("unitno", StringType.INSTANCE);
        query.addScalar("poo", StringType.INSTANCE);
        query.addScalar("pod", StringType.INSTANCE);
        query.addScalar("pod", StringType.INSTANCE);
        query.addScalar("fd", StringType.INSTANCE);
        query.addScalar("bookVoyageNo", StringType.INSTANCE);
        query.addScalar("polUnCode", StringType.INSTANCE);
        query.addScalar("transMode", StringType.INSTANCE);
        query.addScalar("unitStatus", StringType.INSTANCE);
        query.addScalar("voyageNo", StringType.INSTANCE);
        query.addScalar("pickedunitNo", StringType.INSTANCE);
        query.addScalar("dimsLength", IntegerType.INSTANCE);
        query.addScalar("dimsWidth", IntegerType.INSTANCE);
        query.addScalar("dimsHeight", IntegerType.INSTANCE);
        query.addScalar("dimsWeight", IntegerType.INSTANCE);
        query.addScalar("dimensionData", StringType.INSTANCE);
        query.addScalar("detailLine", StringType.INSTANCE);
        query.addScalar("warehouseLine", StringType.INSTANCE);
        query.addScalar("consolidatesFiles", StringType.INSTANCE);
        query.addScalar("consolidatesFileId", StringType.INSTANCE);
        query.addScalar("bookingHazmatDetails", StringType.INSTANCE);
        query.addScalar("shortShipSequence", StringType.INSTANCE);
        return query.list();
    }

    private String appendCommonQuery(LclAddVoyageForm lclAddVoyageForm) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT f.`file_number` AS fileNumber,  f.id AS fileId, b.id AS bookingPieceId,f.`status` AS STATUS,  ");
        sb.append("  bk.`booking_type` AS bookingType, bk.`booked_ss_header_id` AS masterId,  bk.poo_id AS pooId, ");
        sb.append("  bk.pol_id AS polId,bk.pod_id AS podId,bk.fd_id AS fdId , CONVERT(GROUP_CONCAT(b.hazmat) USING utf8) AS hazardous, ");
        sb.append("  (SELECT disposition_id  FROM  lcl_booking_dispo WHERE file_number_id = f.id ORDER BY id DESC LIMIT 1) AS dispoId, ");
        sb.append(" (SELECT elite_code FROM disposition WHERE id = dispoId) AS dispo, ");
        sb.append(" (SELECT un_location_id FROM lcl_booking_dispo WHERE file_number_id = f.id ORDER BY id DESC LIMIT 1) AS unLocId, ");
        sb.append(" (SELECT lus.id FROM lcl_booking_piece_unit bpu JOIN lcl_unit_ss lus ON lus.id = bpu.lcl_unit_ss_id ");
        sb.append("  JOIN lcl_ss_header lsh ON lsh.id =  lus.ss_header_id  WHERE lsh.service_type NOT IN('I') AND bpu.booking_piece_id = b.id ");
        sb.append("  ORDER BY bpu.id DESC LIMIT 1)as lclUnitSsId,  ");
        sb.append(" (SELECT trans_mode FROM lcl_ss_header lsh LEFT JOIN lcl_unit_ss lus ON lsh.id = lus.ss_header_id  ");
        sb.append("  AND lsh.service_type NOT IN('I')  WHERE lus.id = lclUnitSsId) AS transMode, ");
        sb.append(" (SELECT GROUP_CONCAT(lsh.`schedule_no`) FROM lcl_ss_header lsh JOIN lcl_unit_ss lus ON lus.`ss_header_id` = lsh.id AND lsh.service_type NOT IN('I') JOIN lcl_booking_piece_unit ");
        sb.append(" lbu ON lbu.`lcl_unit_ss_id` =  lus.`id` JOIN lcl_booking_piece lb ON lb.`id` = lbu.`booking_piece_id` WHERE lb.id= bookingPieceId) AS voyageNo, ");
        sb.append(" (SELECT GROUP_CONCAT(lu.unit_no) FROM lcl_unit lu JOIN lcl_unit_ss lus ON lus.unit_id = lu.id ");
        sb.append(" JOIN lcl_ss_header lsh ON lus.`ss_header_id` = lsh.id AND lsh.service_type NOT IN('I')  JOIN lcl_booking_piece_unit lbu ");
        sb.append(" ON lbu.`lcl_unit_ss_id` =  lus.`id` JOIN lcl_booking_piece lb ON lb.`id` = lbu.`booking_piece_id` WHERE lb.id= bookingPieceId) AS unitNo, ");
        sb.append(" f.`short_ship_sequence` AS shortShipSequence ");
        return sb.toString();
    }

    private String mainSubQuery(LclAddVoyageForm lclAddVoyageForm) throws Exception {
        StringBuilder sb = new StringBuilder();
        int poo = lclAddVoyageForm.getOriginId();
        int pol = lclAddVoyageForm.getFinalDestinationId();
        if (lclAddVoyageForm.isIntransitDr()) {
            if (lclAddVoyageForm.isShowAllDr() && !lclAddVoyageForm.isShowBooking()) {
                sb.append(appendCommonQuery(lclAddVoyageForm));
                sb.append(" FROM lcl_booking bk  JOIN lcl_file_number f  ON f.id = bk.file_number_id  ");
                sb.append(" AND bk.`booking_type` IN('E','T') ");
                sb.append(" LEFT JOIN lcl_booking_import bm ON bm.`file_number_id` = f.`id` ");
                sb.append(" JOIN lcl_booking_piece b  ON b.`file_number_id` = f.id    ");
                sb.append(" WHERE f.`state` <> 'Q' AND (f.status = 'WV' OR f.status = 'W' OR f.status = 'B') ");
                sb.append(" and ( IF(bk.`booking_type` <> 'T',bk.poo_id <> bk.pol_id,bk.pod_id <> bm.`usa_port_of_exit_id`)  and ");
                sb.append("  IF(bk.`booking_type` <> 'T',bk.pol_id <>").append(poo).append(", bm.`usa_port_of_exit_id` <>").append(poo).append("))");
                sb.append(" GROUP BY f.id Having (dispo <> 'RUNV' and dispo<>'OBKG' and (unLocId=").append(poo).append(" or unLocId is null)) ");
            } else if (!lclAddVoyageForm.isShowAllDr() && lclAddVoyageForm.isShowBooking()) {
                sb.append(appendCommonQuery(lclAddVoyageForm));
                sb.append(" FROM lcl_booking bk  JOIN lcl_file_number f  ON f.id = bk.file_number_id  ");
                sb.append(" AND bk.`booking_type` IN('E','T') ");
                sb.append(" LEFT JOIN lcl_booking_import bm ON bm.`file_number_id` = f.`id` ");
                sb.append(" JOIN lcl_booking_piece b  ON b.`file_number_id` = f.id    ");
                sb.append(" WHERE  f.`state` <> 'Q' AND (f.status = 'WV' OR f.status = 'W' OR f.status = 'B') ");
                sb.append(" AND IF(bk.`booking_type` <> 'T',bk.poo_id <> bk.pol_id,bk.pod_id <> bm.`usa_port_of_exit_id`) ");
                sb.append(" AND IF(bk.`booking_type` <> 'T',bk.pol_id =").append(pol).append(" , bm.`usa_port_of_exit_id` =").append(poo).append(" ) ");
                sb.append(" GROUP BY f.id Having (dispo <> 'RUNV' and (unLocId=").append(poo).append(" or unLocId is null )) ");
            } else if (lclAddVoyageForm.isShowAllDr() && lclAddVoyageForm.isShowBooking()) {
                sb.append(appendCommonQuery(lclAddVoyageForm));
                sb.append(" FROM lcl_booking bk  JOIN lcl_file_number f  ON f.id = bk.file_number_id  ");
                sb.append(" AND bk.`booking_type` IN('E','T') ");
                sb.append(" LEFT JOIN lcl_booking_import bm ON  bm.`file_number_id` = f.`id` ");
                sb.append(" JOIN lcl_booking_piece b  ON b.`file_number_id` = f.id    ");
                sb.append(" WHERE  f.`state` <> 'Q' AND (f.status = 'WV' OR f.status = 'W' OR f.status = 'B') ");
                sb.append(" AND IF(bk.`booking_type` <> 'T',bk.poo_id <> bk.pol_id,bk.pod_id <> bm.`usa_port_of_exit_id`) ");
                sb.append(" AND IF(bk.`booking_type` <> 'T',bk.pol_id =").append(pol).append(" , bm.`usa_port_of_exit_id` =").append(pol).append(") ");
                sb.append(" GROUP BY f.id Having (dispo <> 'RUNV' and (unLocId=").append(poo).append(" or unLocId is null )) ");
                sb.append(" UNION ");
                sb.append(appendCommonQuery(lclAddVoyageForm));
                sb.append(" FROM lcl_booking bk  JOIN lcl_file_number f  ON f.id = bk.file_number_id  ");
                sb.append(" AND bk.`booking_type` IN('E','T') ");
                sb.append(" LEFT JOIN lcl_booking_import bm ON bm.`file_number_id` = f.`id` ");
                sb.append(" JOIN lcl_booking_piece b  ON b.`file_number_id` = f.id    ");
                sb.append(" WHERE  f.`state` <> 'Q' AND (f.status = 'WV' OR f.status = 'W' OR f.status = 'B') ");
                sb.append(" and ( IF(bk.`booking_type` <> 'T',bk.poo_id <> bk.pol_id,bk.pod_id <> bm.`usa_port_of_exit_id`)  and ");
                sb.append("  IF(bk.`booking_type` <> 'T',bk.pol_id <>").append(poo).append(" , bm.`usa_port_of_exit_id` <>").append(poo).append(") )");
                sb.append(" GROUP BY f.id Having (dispo <> 'RUNV' and dispo<>'OBKG' and (unLocId=").append(poo).append(" or unLocId is null)) ");
            } else {
                    sb.append(appendCommonQuery(lclAddVoyageForm));
                    sb.append(" FROM lcl_booking bk  JOIN lcl_file_number f  ON f.id = bk.file_number_id  ");
                    sb.append(" AND bk.`booking_type` IN('E','T') ");
                    sb.append(" LEFT JOIN lcl_booking_import bm ON bm.`file_number_id` = f.`id` ");
                    sb.append(" JOIN lcl_booking_piece b  ON b.`file_number_id` = f.id    ");
                    sb.append(" WHERE f.`state` <> 'Q' AND (f.status = 'WV' OR f.status = 'W' OR f.status = 'B') ");
                    sb.append(" AND IF(bk.`booking_type` <> 'T',bk.poo_id <> bk.pol_id,bk.pod_id <> bm.`usa_port_of_exit_id`) ");
                    sb.append(" AND IF(bk.`booking_type` <> 'T',bk.pol_id =").append(pol).append(" , bm.`usa_port_of_exit_id` =").append(pol).append(") ");
                    sb.append(" GROUP BY f.id Having (dispo <> 'RUNV' and dispo<>'OBKG' and (unLocId=").append(poo).append(" or unLocId is null )) ");
            }
        } else {
            if (lclAddVoyageForm.isShowAllDr() && !lclAddVoyageForm.isShowBooking()) {
                sb.append(appendCommonQuery(lclAddVoyageForm));
                sb.append(" FROM lcl_booking bk  JOIN lcl_file_number f  ON f.id = bk.file_number_id  ");
                sb.append(" AND bk.`booking_type` IN('E','T') ");
                sb.append(" LEFT JOIN lcl_booking_import bm ON bm.`file_number_id` = f.`id` ");
                sb.append(" JOIN lcl_booking_piece b  ON b.`file_number_id` = f.id    ");
                sb.append(" WHERE f.`state` <> 'Q' AND (f.status = 'WV' OR f.status = 'W' OR f.status = 'B') ");
                sb.append(" and ( IF(bk.`booking_type` <> 'T',bk.poo_id <> bk.pol_id,bk.pod_id <> bm.`usa_port_of_exit_id`)  and ");
                sb.append("  IF(bk.`booking_type` <> 'T',bk.pol_id <>").append(poo).append(" , bm.`usa_port_of_exit_id` <>").append(poo).append(") )");
                sb.append(" GROUP BY f.id Having (dispo <> 'RUNV' and  dispo<>'INTR' and dispo<>'OBKG' and unLocId=").append(poo).append(" ) ");
            } else if (!lclAddVoyageForm.isShowAllDr() && lclAddVoyageForm.isShowBooking()) {
                sb.append(appendCommonQuery(lclAddVoyageForm));
                sb.append(" FROM lcl_booking bk  JOIN lcl_file_number f  ON f.id = bk.file_number_id  ");
                sb.append(" AND bk.`booking_type` IN('E','T') ");
                sb.append(" LEFT JOIN lcl_booking_import bm ON bm.`file_number_id` = f.`id` ");
                sb.append(" JOIN lcl_booking_piece b  ON b.`file_number_id` = f.id    ");
                sb.append(" WHERE f.`state` <> 'Q' AND (f.status = 'WV' OR f.status = 'W' OR f.status = 'B') ");
                sb.append(" AND IF(bk.`booking_type` <> 'T',bk.poo_id <> bk.pol_id,bk.pod_id <> bm.`usa_port_of_exit_id`) ");
                sb.append(" AND IF(bk.`booking_type` <> 'T',bk.pol_id =").append(pol).append(" , bm.`usa_port_of_exit_id` =").append(pol).append(") ");
                sb.append(" GROUP BY f.id Having (dispo <> 'RUNV' and  dispo<>'INTR' and (unLocId=").append(poo).append(" or unLocId is null ))");
            } else if (lclAddVoyageForm.isShowAllDr() && lclAddVoyageForm.isShowBooking()) {
                sb.append(appendCommonQuery(lclAddVoyageForm));
                sb.append(" FROM lcl_booking bk  JOIN lcl_file_number f  ON f.id = bk.file_number_id  ");
                sb.append(" AND bk.`booking_type` IN('E','T') ");
                sb.append(" LEFT JOIN lcl_booking_import bm ON bm.`file_number_id` = f.`id` ");
                sb.append(" JOIN lcl_booking_piece b  ON b.`file_number_id` = f.id    ");
                sb.append(" WHERE f.`state` <> 'Q' AND (f.status = 'WV' OR f.status = 'W' OR f.status = 'B') ");
                sb.append(" AND IF(bk.`booking_type` <> 'T',bk.poo_id <> bk.pol_id,bk.pod_id <> bm.`usa_port_of_exit_id`) ");
                sb.append(" AND IF(bk.`booking_type` <> 'T',bk.pol_id =").append(pol).append(" , bm.`usa_port_of_exit_id` =").append(pol).append(") ");
                sb.append(" GROUP BY f.id Having (dispo <> 'RUNV' and  dispo<>'INTR' and (unLocId=").append(poo).append("  or unLocId is null )) ");
                sb.append(" UNION ");
                sb.append(appendCommonQuery(lclAddVoyageForm));
                sb.append(" FROM lcl_booking bk  JOIN lcl_file_number f  ON f.id = bk.file_number_id  ");
                sb.append(" AND bk.`booking_type` IN('E','T') ");
                sb.append(" LEFT JOIN lcl_booking_import bm ON bm.`file_number_id` = f.`id` ");
                sb.append(" JOIN lcl_booking_piece b  ON b.`file_number_id` = f.id    ");
                sb.append(" WHERE f.`state` <> 'Q' AND (f.status = 'WV' OR f.status = 'W' OR f.status = 'B') ");
                sb.append(" and ( IF(bk.`booking_type` <> 'T',bk.poo_id <> bk.pol_id,bk.pod_id <> bm.`usa_port_of_exit_id`)  and ");
                sb.append("  IF(bk.`booking_type` <> 'T',bk.pol_id <>").append(poo).append(" , bm.`usa_port_of_exit_id` <>").append(poo).append(") )");
                sb.append(" GROUP BY f.id Having (dispo <> 'RUNV' and  dispo<>'INTR' and dispo<>'OBKG' and unLocId=").append(poo).append(" ) ");
            } else {
                    sb.append(appendCommonQuery(lclAddVoyageForm));
                    sb.append(" FROM lcl_booking bk  JOIN lcl_file_number f  ON f.id = bk.file_number_id  ");
                    sb.append(" AND bk.`booking_type` IN('E','T') ");
                    sb.append(" LEFT JOIN lcl_booking_import bm ON bm.`file_number_id` = f.`id` ");
                    sb.append(" JOIN lcl_booking_piece b  ON b.`file_number_id` = f.id    ");
                    sb.append(" WHERE f.`state` <> 'Q' AND (f.status = 'WV' OR f.status = 'W' OR f.status = 'B') ");
                    sb.append(" AND IF(bk.`booking_type` <> 'T',bk.poo_id <> bk.pol_id,bk.pod_id <> bm.`usa_port_of_exit_id`) ");
                    sb.append(" AND IF(bk.`booking_type` <> 'T',bk.pol_id =").append(pol).append(" , bm.`usa_port_of_exit_id` =").append(pol).append(") ");
                    sb.append(" GROUP BY f.id Having (dispo <> 'RUNV' and  dispo<>'INTR' and dispo<>'OBKG' and unLocId=").append(poo).append(" ) ");
            }
        }
        return sb.toString();
    }

    public List getPickedDrForMasterBL(String bookingNo, boolean isMainfest, Long headerId, Long unitSsId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ROUND(SUM(COALESCE(bp.actual_volume_imperial,bp.booked_volume_imperial)),2) AS cft, ");
        sb.append(" ROUND(SUM(COALESCE(bp.actual_volume_metric,bp.booked_volume_metric)),2) AS cbm, ");
        sb.append(" ROUND(SUM(COALESCE(bp.actual_weight_imperial,bp.booked_weight_imperial)),2) AS lbs, ");
        sb.append(" ROUND(SUM(COALESCE(bp.actual_weight_metric,bp.booked_weight_metric)),2) AS kgs, ");
        sb.append(" SUM(COALESCE(bp.actual_piece_count,bp.booked_piece_count)) AS pcs from ");
        sb.append(" (SELECT DISTINCT lf.id AS fileNumberId FROM  (SELECT DISTINCT bp.`file_number_id` AS fileId FROM ");
        sb.append(" lcl_ss_header lsh  JOIN lcl_unit_ss lus   ON lus.ss_header_id = lsh.id  ");
        sb.append(" JOIN lcl_booking_piece_unit bpu  ON bpu.`lcl_unit_ss_id` = lus.`id`  ");
        sb.append(" JOIN lcl_booking_piece bp   ON bp.id = bpu.`booking_piece_id`  ");
        sb.append(" WHERE lus.sp_booking_no =:bookingNo AND lsh.id =:headerId and lus.id=:unitSsId) AS fn   ");
        if (isMainfest) {
            sb.append("  JOIN lcl_file_number lf ON lf.id= getHouseBLForConsolidateDr(fn.fileId) ");
            sb.append(" and lf.status in('M')) bl JOIN lcl_bl_piece bp ON bp.`file_number_id` = bl.fileNumberId ");
        } else {
            sb.append(" JOIN lcl_file_number lf ON lf.id = fn.fileId) AS bk ");
            sb.append(" JOIN lcl_booking_piece bp ON bp.`file_number_id` = bk.fileNumberId ");
        }
        SQLQuery queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setParameter("bookingNo", bookingNo);
        queryObject.setParameter("headerId", headerId);
        queryObject.setParameter("unitSsId", unitSsId);
        return queryObject.list();
    }

    public String getPickedDrWeightValue(String unitssId, String headerId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT SUM(IF(bp.`actual_weight_metric` <> '' AND bp.`actual_weight_metric` <> 0.00, ");
        sb.append(" bp.`actual_weight_metric`,bp.`booked_weight_metric` )) AS totalWeight ");
        sb.append(" FROM lcl_booking_piece bp JOIN lcl_booking_piece_unit bpu ON bpu.`booking_piece_id` = bp.`id` ");
        sb.append(" JOIN lcl_unit_ss lus ON lus.id = bpu.`lcl_unit_ss_id` ");
        sb.append(" JOIN lcl_ss_header lsh ON lsh.id = lus.`ss_header_id` ");
        sb.append(" WHERE lus.id =:unitssId AND lsh.id=:headerId ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameter("unitssId", unitssId);
        query.setParameter("headerId", headerId);
        Object obj = query.uniqueResult();
        return obj != null ? obj.toString() : "";
    }

    public String getWareHouseNumber(String unitId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT  w.warehsno FROM `lcl_unit_whse` wh LEFT JOIN warehouse w ON  wh.warehouse_id=w.id WHERE wh.unit_id= ")
                .append(unitId).append(" ORDER BY wh.id DESC  LIMIT 1 ");
        Query queryObject = getSession().createSQLQuery(sb.toString());
        return (String) queryObject.uniqueResult();
    }

    public String[] getunAssignedValue(String unitNo) throws Exception {
        String[] result = new String[2];
        String str = "SELECT ut.id ,lu.comments AS comments  FROM unit_type ut JOIN lcl_unit lu ON ut.id=lu.unit_type_id WHERE unit_no=:unitNo";
        SQLQuery query = getCurrentSession().createSQLQuery(str);
        query.setParameter("unitNo", unitNo);
        List list = query.list();
        for (Object obj : list) {
            Object[] size = (Object[]) obj;
            result[0] = size[0].toString();
            result[1] = size[1] != null ? size[1].toString() : "";
        }
        return result;
    }

    public String isCobValidation(String unitssId) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append(" SELECT GROUP_CONCAT(DISTINCT (lcf.`file_number`)) AS filNo FROM lcl_unit_ss lus  ");
        builder.append(" JOIN lcl_booking_piece_unit lbpu  ON lus.id = lbpu.lcl_unit_ss_id  ");
        builder.append(" JOIN lcl_booking_piece lbpi ON lbpi.`id` = lbpu.`booking_piece_id`  ");
        builder.append(" JOIN lcl_bl_piece lbp  ON lbpi.`file_number_id` = lbp.`file_number_id`  ");
        builder.append(" JOIN lcl_file_number lcf  ON lcf.`id` = lbp.`file_number_id`");
        builder.append(" WHERE lus.id =:unitssId AND ((lbp.`actual_piece_count` = 0.00 OR lbp.actual_piece_count IS NULL)");
        builder.append(" OR( lbp.`actual_weight_imperial` = 0.00 OR lbp.actual_weight_imperial IS NULL)");
        builder.append(" OR( lbp.`actual_volume_imperial` = 0.00 OR lbp.actual_volume_imperial IS NULL)");
        builder.append(" OR (lbp.`actual_volume_metric` = 0.00 OR lbp.actual_volume_metric IS NULL)");
        builder.append(" OR (lbp.`actual_weight_metric` = 0.00 OR lbp.actual_weight_metric IS NULL ))");
        SQLQuery query = getCurrentSession().createSQLQuery(builder.toString());
        query.setString("unitssId", unitssId);
        return (String) query.uniqueResult();
    }

    public List<BookingUnitsBean> getConsolidateReleasedDetails(LclAddVoyageForm lclAddVoyageForm) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT  fn.fileId, fn.file_number as fileNo,  IF(fn.pickedCount > 0, 'PICKED', '') AS STATUS, ");
        sb.append(" fn.hazardous as haz ,fn.dispo, (SELECT  un_loc_code   FROM ");
        sb.append(" un_location WHERE id = polId) AS pol, ");
        sb.append(" piece.total_piece AS totalPieceCount, ");
        sb.append(" ROUND(piece.total_weight_imperial, 0) AS totalWeightImperial, ");
        sb.append(" ROUND(piece.total_volume_imperial, 0) AS totalVolumeImperial, ");
        sb.append(" (SELECT   un_loc_code   FROM    un_location  ");
        sb.append(" WHERE id = fn.unLocId) AS curLoc, ");
        sb.append(" (SELECT lrm.`remarks` FROM lcl_remarks lrm ");
        sb.append(" WHERE lrm.`file_number_id` = fn.fileId ");
        sb.append(" AND lrm.`type` = 'Loading Remarks') AS remarks, ");
        sb.append(" (SELECT     GROUP_CONCAT(htc.code SEPARATOR '<br>') ");
        sb.append(" FROM    lcl_booking_hot_code htc   WHERE htc.file_number_id = fn.fileId) AS hotCodes,");
        sb.append(" (SELECT  IF(      INSTR(htc.code, '/'),      GROUP_CONCAT( ");
        sb.append(" LEFT(htc.code, INSTR(htc.code, '/') - 1) ");
        sb.append(" ),'' ) FROM    lcl_booking_hot_code htc ");
        sb.append(" WHERE htc.file_number_id = fn.fileId) AS hotCodeKey, ");
        sb.append(" UPPER(fn.whse) AS warehouseLine ");
        sb.append(" FROM  (SELECT    f.id AS fileId, ");
        sb.append(" f.file_number, bk.poo_id AS pooId, bk.pol_id AS polId, ");
        sb.append(" bk.pod_id AS podId, bk.fd_id AS fdId, ");
        sb.append(" CONVERT(GROUP_CONCAT(b.hazmat) USING utf8) AS hazardous, ");
        sb.append(" (SELECT    COUNT(*)   FROM      lcl_booking_piece bp  ");
        sb.append("JOIN lcl_booking_piece_unit bpu  ");
        sb.append(" ON bp.`id` = bpu.`booking_piece_id` ");
        sb.append(" JOIN lcl_unit_ss lus ON lus.`id` = bpu.`lcl_unit_ss_id` ");
        sb.append(" JOIN lcl_ss_header lsh  ON lsh.`id` = lus.`ss_header_id` ");
        sb.append(" AND lsh.service_type IN ('E', 'C') ");
        sb.append(" WHERE bp.`file_number_id` = f.id  ");
        sb.append(" LIMIT 1) AS pickedCount, (SELECT ");
        sb.append(" disposition_id  FROM   lcl_booking_dispo  WHERE file_number_id = f.id ");
        sb.append(" ORDER BY id DESC   LIMIT 1) AS dispoId, (SELECT elite_code ");
        sb.append(" FROM  disposition  WHERE id = dispoId) AS dispo, ");
        sb.append(" (SELECT un_location_id     FROM      lcl_booking_dispo ");
        sb.append(" WHERE file_number_id = f.id  ORDER BY id DESC  LIMIT 1) AS unLocId, ");
        sb.append(" (SELECT  GROUP_CONCAT(lbpw.location SEPARATOR ',')  FROM ");
        sb.append(" lcl_booking_piece_whse lbpw  JOIN lcl_booking_piece lb  ");
        sb.append(" ON lb.`id` = lbpw.booking_piece_id WHERE lb.file_number_id = f.id  ");
        sb.append(" AND lbpw.location <> '') AS whse  FROM lcl_booking bk  JOIN lcl_file_number f  ");
        sb.append(" ON f.id = bk.file_number_id   AND f.id IN ( ").append(lclAddVoyageForm.getConsolidateFiles()).append(" )");
        sb.append(" JOIN lcl_booking_piece b   ON b.`file_number_id` = f.id  ");
        sb.append(" JOIN lcl_booking_export bux ON bux.file_number_id = f.id  ");
        if (!lclAddVoyageForm.getFilterByChanges().equalsIgnoreCase("lclDomestic")) {
            sb.append(" where f.`state` <> 'Q' AND f.status not in ('V','X','RF') ");
            sb.append(" and bux.released_datetime IS NOT NULL  GROUP BY f.`id`) fn   ");
        } else {
            sb.append(" where f.`state` <> 'Q' AND f.status not in ('V','X','RF') ");
            sb.append(" GROUP BY f.`id`) fn ");
        }
        sb.append(" LEFT JOIN (SELECT lb.file_number_id, SUM(IF( lb.actual_piece_count IS NOT NULL ");
        sb.append(" AND lb.actual_piece_count != 0,lb.actual_piece_count, ");
        sb.append(" lb.booked_piece_count )) AS total_piece, ");
        sb.append(" SUM(IF(lb.actual_weight_imperial IS NOT NULL  AND lb.actual_weight_imperial != 0.000, ");
        sb.append(" lb.actual_weight_imperial,lb.booked_weight_imperial ");
        sb.append(" )) AS total_weight_imperial, ");
        sb.append(" SUM(IF(lb.actual_volume_imperial IS NOT NULL AND lb.actual_volume_imperial != 0.000, ");
        sb.append(" lb.actual_volume_imperial,lb.booked_volume_imperial ");
        sb.append(")) AS total_volume_imperial FROM lcl_booking_piece lb  GROUP BY lb.file_number_id) piece  ");
        sb.append(" ON piece.file_number_id = fn.fileId  ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setResultTransformer(Transformers.aliasToBean(BookingUnitsBean.class));
        query.addScalar("fileId", LongType.INSTANCE);
        query.addScalar("fileNo", StringType.INSTANCE);
        query.addScalar("haz", BooleanType.INSTANCE);
        query.addScalar("status", StringType.INSTANCE);
        query.addScalar("totalPieceCount", IntegerType.INSTANCE);
        query.addScalar("totalWeightImperial", BigDecimalType.INSTANCE);
        query.addScalar("totalVolumeImperial", BigDecimalType.INSTANCE);
        query.addScalar("curLoc", StringType.INSTANCE);
        query.addScalar("dispo", StringType.INSTANCE);
        query.addScalar("remarks", StringType.INSTANCE);
        query.addScalar("hotCodes", StringType.INSTANCE);
        query.addScalar("hotCodeKey", StringType.INSTANCE);
        query.addScalar("pol", StringType.INSTANCE);
        query.addScalar("warehouseLine", StringType.INSTANCE);
        return query.list();
    }

    public String getNonReleasedDrList(String consolidateFileIds) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT GROUP_CONCAT(lf.file_number) AS nonReleaseFile ");
        sb.append(" FROM  lcl_booking_export bk  ");
        sb.append(" JOIN lcl_file_number lf ON bk.`file_number_id` = lf.id  AND lf.`state` <> 'Q' AND lf.status <> 'V' ");
        sb.append(" WHERE bk.`file_number_id` IN (").append(consolidateFileIds);
        sb.append(") AND bk.`released_datetime` IS NULL ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        return (String) query.addScalar("nonReleaseFile", StringType.INSTANCE).uniqueResult();
    }

    public String getPickedDrInDiffferentUnit(String consolidateFileIds,
            String unitSsId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT GROUP_CONCAT(DISTINCT lf.`file_number`) as pickedUnitsAndFile FROM ");
        sb.append(" lcl_booking_piece bp ");
        sb.append(" JOIN lcl_file_number lf ON lf.id = bp.`file_number_id` AND  lf.`state` <> 'Q' AND lf.status <> 'V' ");
        sb.append(" JOIN lcl_booking_piece_unit bpu ON bp.id = bpu.`booking_piece_id` ");
        sb.append(" JOIN lcl_unit_ss lus ");
        sb.append("  ON lus.`id` = bpu.`lcl_unit_ss_id`  ");
        sb.append("  JOIN lcl_unit lu ON lu.id = lus.`unit_id` ");
        sb.append("  JOIN lcl_ss_header lsh ON lsh.id = lus.`ss_header_id` ");
        sb.append(" WHERE bp.`file_number_id` IN (").append(consolidateFileIds).append(")");
        sb.append(" AND lsh.`service_type` Not IN ('I') AND lus.id NOT IN(:unitSsId); ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameter("unitSsId", unitSsId);
        return (String) query.addScalar("pickedUnitsAndFile", StringType.INSTANCE).uniqueResult();
    }

    public String getBookedOnAnotherVoyageDRList(String consolidateFileIds,
            String headerId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT GROUP_CONCAT(DISTINCT lf.file_number) AS bookedOnAnotherVoyage ");
        sb.append(" FROM lcl_booking b JOIN lcl_file_number lf ON (b.`file_number_id` = lf.id ");
        sb.append(" AND b.`file_number_id` IN (").append(consolidateFileIds).append(")) WHERE b.booked_ss_header_id <> ''  ");
        sb.append(" AND b.booked_ss_header_id <> (").append(headerId).append("); ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        Object result = query.addScalar("bookedOnAnotherVoyage", StringType.INSTANCE).uniqueResult();
        return null != result ? result.toString() : "";
    }

    private String getHazardousDetailsInfo() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" ( SELECT");
        stringBuilder.append("  GROUP_CONCAT('<font color=red>UN#-></font>' ,bh.un_hazmat_no ,");
        stringBuilder.append("  ' ,<font color=red>Class-></font>',IF(");
        stringBuilder.append("    bh.imo_pri_class_code IS NOT NULL,");
        stringBuilder.append("    bh.imo_pri_class_code,");
        stringBuilder.append("    ''");
        stringBuilder.append("  ),' ,<font color=red>Packing Group-></font>',");
        stringBuilder.append("  IF(");
        stringBuilder.append("    bh.packing_group_code IS NOT NULL,");
        stringBuilder.append("    bh.packing_group_code,");
        stringBuilder.append("    ''");
        stringBuilder.append("  ),' ,<font color=red>LTD QTY-></font>',");
        stringBuilder.append("  IF(");
        stringBuilder.append("    bh.limited_quantity='' || bh.limited_quantity='0',");
        stringBuilder.append("    'N',");
        stringBuilder.append("    'Y'");
        stringBuilder.append("  )SEPARATOR '<br>') FROM lcl_booking_hazmat bh WHERE bh.file_number_id = fn.fileId) AS bookingHazmatDetails, ");
        return stringBuilder.toString();
    }
}
