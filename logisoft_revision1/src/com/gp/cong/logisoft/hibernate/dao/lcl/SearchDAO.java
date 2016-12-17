package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.lcl.model.ExportSearchBean;
import com.gp.cong.logisoft.beans.ImportBookingUnitsBean;
import com.gp.cvst.logisoft.struts.form.lcl.LclAddVoyageForm;
import com.gp.cvst.logisoft.struts.form.lcl.LclSearchForm;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.util.LabelValueBean;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BooleanType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

/**
 *
 * @author meiyazhakan.r
 */
public class SearchDAO extends BaseHibernateDAO implements ConstantsInterface, LclCommonConstant {

    public String checkLocking(String fileNumber, int userId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        String select1 = "File " + fileNumber + " is already opened in another window";
        String select2 = "This record is being used by ";
        queryBuilder.append("select if(u.user_id = '").append(userId).append("','").append(select1).append("',");
        queryBuilder.append("concat('").append(select2).append("',u.login_name,' on ',date_format(p.process_info_date,'%d-%b-%Y %h:%i %p'))) as result");
        queryBuilder.append(" from process_info p");
        queryBuilder.append(" join user_details u");
        queryBuilder.append(" on (p.user_id = u.user_id)");
        queryBuilder.append(" where p.record_id = '").append(fileNumber).append("'");
        queryBuilder.append(" AND module_id='LCL FILE' order by p.id desc limit 1");
        return (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public void checkLockingStatus(String fileNo, int userId, HttpServletResponse response) throws Exception {
        String result = checkLocking(fileNo, userId);
        PrintWriter out = response.getWriter();
        if (CommonUtils.isNotEmpty(result)) {
            out.print(result);
        } else {
            out.print("available");
        }
        out.flush();
        out.close();
    }

    public ImportBookingUnitsBean getMultiUnitsAndDrList(LclAddVoyageForm lclAddVoyageForm) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT lf.headerId,lf.scheduleNo,lf.originId,lf.ownerId,lf.originCode,lf.fdId,lf.unitssId,");
        sb.append("lf.unitId,lf.unitNo,lf.statuss,IF(lf.edi is NULL, '', lf.edi) AS edi ,lf.unitdescri,lf.remarks,lf.itno,lf.masterBL,lf.coloAcctNo,");
        sb.append("lf.coloAcctName,lf.coloAddress,lf.coloCity ,lf.uwareHsNo,lf.uwarehsName,lf.uwarehsAddress,CONCAT(lf.uwarehsCity,' ',lf.uwarehsState,' ',lf.uwarehsZip),");
        sb.append("lf.cwarehsNo,lf.cWarehsName,lf.cWarehsAddress,CONCAT(lf.cWarehsCity,' ',lf.cWarehsState,' ',lf.cWarehsZip),");
        sb.append("lf.dispoCode as dispoCode1,lf.dispoDesc as dispoDesc1,lf.arRedInvoiceId AS arRedInvoiceId,lf.ssremarks AS ssRemarks,");
        sb.append("lf.fileId,lf.fileNumber,lf.bookType,lf.fdUnCode,lf.fdName,lf.fdCode,lf.dispoCode as dispoCode2,lf.dispoDesc as dispoDesc2,");
        sb.append("SUM(IF(lf.booked_piece_count IS NOT NULL AND lf.booked_piece_count != 0,lf.booked_piece_count,'')) AS totalPiece,");
        sb.append("SUM(IF(lf.booked_weight_metric IS NOT NULL AND lf.booked_weight_metric != 0.000,lf.booked_weight_metric,'')) AS totalweightimperial,");
        sb.append("SUM(IF(lf.booked_volume_metric IS NOT NULL AND lf.booked_volume_metric != 0.000,lf.booked_volume_metric,'')) AS totalvolumeimperial,");
        sb.append("lf.collectCharge,lf.totalIPI,lf.shipAcct,lf.shipName,lf.notyAcct,lf.notyName,lf.consAcct,lf.consName,lf.bookedBy,lf.transhipment, ");
        sb.append("IF(lf.amsNo is NULL, '', lf.amsNo) AS amsNo ");
        sb.append("FROM ");
        sb.append("(SELECT lsh.id AS headerId,lsh.schedule_no AS scheduleNo,lsh.origin_id AS originId,lsh.owner_user_id AS ownerId,origin.un_loc_code AS originCode,lsh.destination_id AS fdId,lu.id AS unitssId,");
        sb.append("lud.id AS unitId,lud.unit_no AS unitNo,lu.status AS statuss,lu.edi AS edi,utd.description AS unitdescri,lu.trucking_remarks AS remarks,lusi.it_no AS itno,lusm.masterbl AS masterBL,");
        sb.append("colo.acct_no AS coloAcctNo,colo.acct_name AS coloAcctName,coloca.address1 AS coloAddress,coloca.city1 AS coloCity,unware.warehsno AS uwareHsNo,unware.warehsname AS uwarehsName,unware.address AS uwarehsAddress,unware.city AS uwarehsCity,unware.state AS uwarehsState,unware.zipcode AS uwarehsZip,cware.warehsno AS cwarehsNo,cware.warehsname AS cWarehsName,cware.address AS cWarehsAddress,cware.city AS cWarehsCity,cware.state AS cWarehsState,cware.zipcode AS cWarehsZip,");
        sb.append("(SELECT d.elite_code FROM lcl_unit_ss_dispo lussd LEFT JOIN disposition d ON lussd.disposition_id=d.id WHERE lussd.unit_id=lu.unit_id ORDER BY lussd.id DESC LIMIT 1)AS dispoCode,");
        sb.append("(SELECT d.description FROM lcl_unit_ss_dispo lussd LEFT JOIN disposition d ON lussd.disposition_id=d.id WHERE lussd.unit_id=lu.unit_id ORDER BY lussd.id DESC LIMIT 1) AS dispoDesc,");
        sb.append("f.id AS fileId ,f.file_number AS fileNumber,f.state AS state,b.booking_type AS bookType,dest.un_loc_code AS fdUnCode,dest.un_loc_name AS fdName,IF(deststate.code IS NULL ,destCou.codedesc,deststate.code) AS fdCode,");
        sb.append("bp.booked_piece_count, bp.booked_weight_metric,bp.booked_volume_metric,");
        sb.append("SUM(if(lba.ar_bill_to_party IN('N','C') and gm.bluescreen_chargecode!='1607',lba.ar_amount+lba.adjustment_amount,0)) as collectCharge,");
        sb.append("SUM(IF(gm.bluescreen_chargecode='1607',lba.ar_amount+lba.adjustment_amount,0)) as totalIPI,");
        sb.append("IF(b.ship_acct_no IS NULL,'',b.ship_acct_no) AS shipAcct,IF(b.ship_acct_no IS NULL ,lcls.company_name,ship.acct_name) AS shipName,");
        sb.append("IF(b.noty_acct_no IS NULL,'',b.noty_acct_no) AS notyAcct,IF(b.noty_acct_no IS NULL ,lcln.company_name,noty.acct_name) AS notyName,");
        sb.append("IF(b.cons_acct_no IS NULL,'',b.cons_acct_no) AS consAcct,IF(b.cons_acct_no IS NULL ,lclc.company_name,cons.acct_name) AS consName,ud.login_name AS bookedBy,lbm.transshipment AS transhipment,ari.id AS arRedInvoiceId, ");
        sb.append("ams.ams_no As amsNo,");
        sb.append("ssr.remarks AS ssRemarks ");
        sb.append("FROM lcl_ss_header lsh JOIN lcl_unit_ss lu ON lsh.id=lu.ss_header_id AND lu.ss_header_id=").append(lclAddVoyageForm.getHeaderId()).append(" ");
        sb.append("JOIN lcl_unit lud ON lu.unit_id = lud.id  LEFT JOIN lcl_unit_ss_imports lusi ON lusi.unit_id=lud.id LEFT JOIN lcl_unit_ss_manifest lusm ON lusm.unit_id=lud.id ");
        sb.append("LEFT JOIN lcl_unit_ss_dispo dispo ON dispo.unit_id=lud.id   LEFT JOIN lcl_booking_piece_unit bu ON lu.id=bu.lcl_unit_ss_id ");
        sb.append("LEFT JOIN lcl_booking_piece bp ON bu.booking_piece_id=bp.id LEFT JOIN lcl_file_number f ON bp.file_number_id=f.id ");
        sb.append("LEFT JOIN lcl_booking_import_ams ams ON ams.file_number_id = f.id ");
        sb.append("LEFT JOIN lcl_booking b ON b.booking_type IN('I','T') AND f.id=b.file_number_id LEFT JOIN lcl_booking_import lbm ON f.id=lbm.file_number_id LEFT JOIN lcl_booking_ac lba ON lba.file_number_id=f.id ");
        sb.append("LEFT JOIN gl_mapping gm on gm.id=lba.ar_gl_mapping_id LEFT JOIN un_location dest ON b.fd_id = dest.id  ");
        sb.append("LEFT JOIN genericcode_dup deststate ON dest.statecode=deststate.id LEFT JOIN genericcode_dup destCou ON dest.countrycode=destCou.id ");
        sb.append("LEFT JOIN trading_partner ship ON b.ship_acct_no=ship.acct_no LEFT JOIN trading_partner noty ON b.noty_acct_no=noty.acct_no ");
        sb.append("LEFT JOIN trading_partner cons ON b.cons_acct_no=cons.acct_no LEFT JOIN user_details ud ON b.entered_by_user_id=ud.user_id ");
        sb.append("LEFT JOIN unit_type utd ON lud.unit_type_id= utd.id  LEFT JOIN un_location origin ON lsh.origin_id=origin.id ");
        sb.append("LEFT JOIN warehouse unware ON unware.id=lusi.unit_warehouse_id LEFT JOIN warehouse cware ON cware.id=lusi.cfs_warehouse_id ");
        sb.append("LEFT JOIN ar_red_invoice ari ON ari.file_no = lu.id LEFT JOIN trading_partner colo ON colo.acct_no=lusi.coloader_acct_no LEFT JOIN cust_address coloca ON (colo.acct_no = coloca.acct_no AND coloca.prime = 'on') ");
        sb.append("LEFT JOIN lcl_contact lcls ON b.ship_contact_id=lcls.id LEFT JOIN lcl_contact lcln ON b.noty_contact_id=lcln.id LEFT JOIN lcl_contact lclc ON b.cons_contact_id=lclc.id ");
        sb.append("LEFT JOIN lcl_unit_ss_remarks ssr ON lud.id = ssr.unit_id AND ssr.type = 'Outsource'");
        sb.append("GROUP BY bp.id ) lf GROUP BY lf.unitId,lf.fileId ORDER BY lf.unitId,lf.fileId DESC ");
        Query queryObject = getSession().createSQLQuery(sb.toString());
        List l = queryObject.list();
        ImportBookingUnitsBean model = new ImportBookingUnitsBean(l);
        return model;
    }

    protected String getCondition(LclSearchForm lclSearchForm) throws Exception {
        StringBuilder condition = new StringBuilder();
        if (CommonUtils.isNotEmpty(lclSearchForm.getCustomerPo())) {
            condition.append(" AND ").append("customer_po like ").append("'").append(lclSearchForm.getCustomerPo()).append("%'");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getTrackingNo())) {
            condition.append(" AND ").append("tracking_no like ").append("'").append(lclSearchForm.getTrackingNo()).append("%'");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getWarehouseDocNo())) {
            condition.append(" AND ").append("warehs_no like ").append("'").append(lclSearchForm.getWarehouseDocNo()).append("%'");
        }
        return condition.toString();
    }

    public List<Object[]> getAllUnitDr(Long unitSsId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT f.file_number,lb.file_number_id,unpod.un_loc_code as pod,unfd.un_loc_code as fd,lb.bill_to_party,lbp.booked_volume_metric,");
        sb.append("(SELECT IF(COUNT(*)>0,'true','false') FROM lcl_booking_ac lba WHERE lba.ap_gl_mapping_id='259' ");
        sb.append("AND lba.file_number_id=lbp.file_number_id) AS chargecount,lb.booking_type as bookingType FROM lcl_booking_piece_unit lbpu ");
        sb.append("JOIN lcl_booking_piece lbp ON lbpu.booking_piece_id = lbp.id ");
        sb.append("JOIN lcl_booking lb ON lb.file_number_id = lbp.file_number_id JOIN lcl_file_number f ON lbp.file_number_id = f.id ");
        sb.append("JOIN un_location unfd ON unfd.id = lb.fd_id JOIN un_location unpod ON unpod.id = lb.pod_id WHERE lbpu.lcl_unit_ss_id = ").append(unitSsId);
        Query queryObject = getSession().createSQLQuery(sb.toString());
        List l = queryObject.list();
        return l;
    }

    // -- changing  the main search query
    private String QuoteByQuery() {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("  join `lcl_quote` qt");
        queryBuilder.append("    on (");
        queryBuilder.append("      qt.`entered_by_user_id` in (");
        queryBuilder.append("        select ");
        queryBuilder.append("          ud.`user_id`");
        queryBuilder.append("        from");
        queryBuilder.append("          `user_details` ud");
        queryBuilder.append("        where");
        queryBuilder.append("          ud.`login_name` = :bkQuoteBy");
        queryBuilder.append("      )");
        queryBuilder.append("      and fn.`id` = qt.`file_number_id` and fn.state = 'Q'");
        queryBuilder.append("    ) ");
        return queryBuilder.toString();
    }

    private String BookingByQuery() {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("  join `lcl_booking` bk");
        queryBuilder.append("    on (");
        queryBuilder.append("      bk.`entered_by_user_id` in (");
        queryBuilder.append("        select ");
        queryBuilder.append("          ud.`user_id`");
        queryBuilder.append("        from");
        queryBuilder.append("          `user_details` ud");
        queryBuilder.append("        where");
        queryBuilder.append("          ud.`login_name` = :bookedBy");
        queryBuilder.append("      )");
        queryBuilder.append("      and fn.`id` = bk.`file_number_id`");
        queryBuilder.append("    ) ");
        return queryBuilder.toString();
    }

    private String BLByQuery() {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("  join `lcl_bl` bl");
        queryBuilder.append("    on (");
        queryBuilder.append("      bl.`bl_owner_id` in (");
        queryBuilder.append("        select ");
        queryBuilder.append("          ud.`user_id`");
        queryBuilder.append("        from");
        queryBuilder.append("          `user_details` ud");
        queryBuilder.append("        where");
        queryBuilder.append("          ud.`login_name` = :blBy");
        queryBuilder.append("      )");
        queryBuilder.append("      and fn.`id` = bl.`file_number_id`  and fn.state = 'BL' and bl.posted_datetime IS NULL");
        queryBuilder.append("    ) ");
        return queryBuilder.toString();
    }

    private String showCfcl(LclSearchForm lclSearchForm) {
        StringBuilder queryBuilder = new StringBuilder();
        if (CommonUtils.isNotEmpty(lclSearchForm.getCfclAccount())) {
            queryBuilder.append("   AND  ");
            queryBuilder.append("   EXISTS (SELECT * FROM  lcl_booking_export lex  WHERE lex.`file_number_id` = fn.`id` and lex.cfcl = 1  and  lex.`cfcl_acct_no` LIKE :cfclAcct  )");
        } else {
            if ("0".equalsIgnoreCase(lclSearchForm.getCfcl())) {
                queryBuilder.append("   AND  ");
                queryBuilder.append("   NOT EXISTS (SELECT * FROM  lcl_booking_export lex  WHERE lex.`file_number_id` = fn.`id` and lex.cfcl = 1   ");
                queryBuilder.append(" )");
            } else if ("1".equalsIgnoreCase(lclSearchForm.getCfcl())) {
                queryBuilder.append("   AND  ");
                queryBuilder.append("   EXISTS (SELECT * FROM  lcl_booking_export lex  WHERE lex.`file_number_id` = fn.`id` AND  lex.cfcl <> 0   ");
                queryBuilder.append(" )");
            }
        }
        return queryBuilder.toString();
    }

    public String getConsolidateQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append(" (SELECT group_concat(lf.file_number) AS fileId   FROM lcl_consolidation lc  join lcl_file_number lf on ");
        sb.append(" lf.id = lc.lcl_file_number_id_a WHERE lc.`lcl_file_number_id_b` = ");
        sb.append(" (SELECT lcl_file_number_id_b FROM lcl_consolidation WHERE lcl_file_number_id_a = f.file_number_id limit 1)    ");
        sb.append(" and lcl_file_number_id_a  <> f.file_number_id ) ");
        return sb.toString();
    }

    public String filterAppendQuery(LclSearchForm lclSearchForm) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        StringBuilder searchCount = new StringBuilder();
        if (CommonUtils.isNotEmpty(lclSearchForm.getFileNumber())) {
            queryBuilder.append("select");
            queryBuilder.append("  fn.`id`,");
            queryBuilder.append("  fn.`created_datetime` as entered_datetime ");
            queryBuilder.append("from");
            queryBuilder.append("  `lcl_file_number` fn ");
            queryBuilder.append("where");
            queryBuilder.append("  fn.`file_number` = :fileNumber");
        } else if (CommonUtils.isNotEmpty(lclSearchForm.getContainerNo())) {
            queryBuilder.append("select");
            queryBuilder.append("  fn.`id`,");
            queryBuilder.append("  bk.`entered_datetime` ");
            queryBuilder.append("from");
            queryBuilder.append("  `lcl_unit` u");
            queryBuilder.append("  join `lcl_unit_ss` us");
            queryBuilder.append("    on (u.`id` = us.`unit_id`)");
            queryBuilder.append("  join `lcl_booking_piece_unit` bpu");
            queryBuilder.append("    on (us.`id` = bpu.`lcl_unit_ss_id`)");
            queryBuilder.append("  join `lcl_booking_piece` bp");
            queryBuilder.append("    on (bpu.`booking_piece_id` = bp.`id`)");
            queryBuilder.append("  join `lcl_booking` bk");
            queryBuilder.append("    on ( ");
            queryBuilder.append("      bp.`file_number_id` = bk.`file_number_id`");
            queryBuilder.append("      and bk.`booking_type` in ('E', 'T')");
            queryBuilder.append("    )");
            queryBuilder.append("  join `lcl_file_number` fn");
            queryBuilder.append("    on (bk.`file_number_id` = fn.`id`) ");
            queryBuilder.append("  LEFT JOIN lcl_booking_import lbm ON lbm.`file_number_id` = fn.id ");
            queryBuilder.append("where");
            queryBuilder.append("  u.`unit_no` like :unitNo");
            queryBuilder.append(showCfcl(lclSearchForm));
        } else if (CommonUtils.isNotEmpty(lclSearchForm.getSslineNo())) {
            queryBuilder.append("select ");
            queryBuilder.append("  fn.`id`,");
            queryBuilder.append("  bk.`entered_datetime` ");
            queryBuilder.append("from");
            queryBuilder.append("  `lcl_ss_detail` sd");
            queryBuilder.append("  join `lcl_ss_header` sh");
            queryBuilder.append("    on (sd.`ss_header_id` = sh.`id`)");
            queryBuilder.append("  join `lcl_unit_ss` us");
            queryBuilder.append("    on (");
            queryBuilder.append("      sh.`id` = us.`ss_header_id`");
            queryBuilder.append("    ) ");
            queryBuilder.append("  join `lcl_booking_piece_unit` bpu");
            queryBuilder.append("    on (us.`id` = bpu.`lcl_unit_ss_id`)");
            queryBuilder.append("  join `lcl_booking_piece` bp");
            queryBuilder.append("    on (bpu.`booking_piece_id` = bp.`id`)");
            queryBuilder.append("  join `lcl_booking` bk");
            queryBuilder.append("    on ( ");
            queryBuilder.append("      bp.`file_number_id` = bk.`file_number_id`");
            queryBuilder.append("      and bk.`booking_type` in ('E', 'T')");
            queryBuilder.append("    )");
            queryBuilder.append("  join `lcl_file_number` fn");
            queryBuilder.append("    on (");
            queryBuilder.append("      bk.`file_number_id` = fn.`id`");
            queryBuilder.append("      and fn.status not in ('X', 'RF')");
            queryBuilder.append("    ) ");
            queryBuilder.append("  LEFT JOIN lcl_booking_import lbm ON lbm.`file_number_id` = fn.id ");
            queryBuilder.append("where");
            queryBuilder.append("  sd.`sp_acct_no` like :sslineNo");
            queryBuilder.append(showCfcl(lclSearchForm));
        } else if (CommonUtils.isNotEmpty(lclSearchForm.getMasterBl())) {
            queryBuilder.append("select");
            queryBuilder.append("  fn.`id`,");
            queryBuilder.append("  bk.`entered_datetime` ");
            queryBuilder.append("from");
            queryBuilder.append("  `lcl_unit_ss_manifest` usm");
            queryBuilder.append("  join `lcl_unit_ss` us");
            queryBuilder.append("    on (usm.`ss_header_id` = us.`ss_header_id`)");
            queryBuilder.append("  join `lcl_booking_piece_unit` bpu ");
            queryBuilder.append("    on (us.`id` = bpu.`lcl_unit_ss_id`)");
            queryBuilder.append("  join `lcl_booking_piece` bp");
            queryBuilder.append("    on (bpu.`booking_piece_id` = bp.`id`)");
            queryBuilder.append("  join `lcl_booking` bk");
            queryBuilder.append("    on ( ");
            queryBuilder.append("      bp.`file_number_id` = bk.`file_number_id`");
            queryBuilder.append("      and bk.`booking_type` in ('E', 'T')");
            queryBuilder.append("    )");
            queryBuilder.append("  join `lcl_file_number` fn");
            queryBuilder.append("    on (bk.`file_number_id` = fn.`id`) ");
            queryBuilder.append("  LEFT JOIN lcl_booking_import lbm ON lbm.`file_number_id` = fn.id ");
            queryBuilder.append("where");
            queryBuilder.append("  usm.`masterbl` like :masterBl");
            queryBuilder.append(showCfcl(lclSearchForm));
        } else if (CommonUtils.isNotEmpty(lclSearchForm.getInbondNo())) {
            queryBuilder.append("select");
            queryBuilder.append("  fn.`id`,");
            queryBuilder.append("  bk.`entered_datetime` ");
            queryBuilder.append("from");
            queryBuilder.append("  `lcl_inbond` inb");
            queryBuilder.append("  join `lcl_booking` bk");
            queryBuilder.append("    on ( ");
            queryBuilder.append("      inb.`file_number_id` = bk.`file_number_id`");
            queryBuilder.append("      and bk.`booking_type` in ('E', 'T')");
            queryBuilder.append("    )");
            queryBuilder.append("  join `lcl_file_number` fn");
            queryBuilder.append("    on (bk.`file_number_id` = fn.`id`) ");
            queryBuilder.append("  LEFT JOIN lcl_booking_import lbm ON lbm.`file_number_id` = fn.id ");
            queryBuilder.append("where");
            queryBuilder.append("  inb.`inbond_no` like :bkInbondNo");
            queryBuilder.append(" union ");
            queryBuilder.append("select");
            queryBuilder.append("  fn.`id`,");
            queryBuilder.append("  qt.`entered_datetime` ");
            queryBuilder.append("from");
            queryBuilder.append("  `lcl_inbond` inb");
            queryBuilder.append("  join `lcl_quote` qt");
            queryBuilder.append("    on ( ");
            queryBuilder.append("      inb.`file_number_id` = qt.`file_number_id`");
            queryBuilder.append("      and qt.`quote_type` in ('E')");
            queryBuilder.append("    )");
            queryBuilder.append("  join `lcl_file_number` fn");
            queryBuilder.append("    on (");
            queryBuilder.append("      qt.`file_number_id` = fn.`id`");
            queryBuilder.append("      and fn.`state` = 'Q'");
            queryBuilder.append("    ) ");
            queryBuilder.append("  LEFT JOIN lcl_booking_import lbm ON lbm.`file_number_id` = fn.id ");
            queryBuilder.append("where");
            queryBuilder.append("  inb.`inbond_no` like :qtInbondNo");
            queryBuilder.append(showCfcl(lclSearchForm));
        } else if (CommonUtils.isNotEmpty(lclSearchForm.getWarehouseDocNo())) {
            queryBuilder.append("select");
            queryBuilder.append("  fn.`id`,");
            queryBuilder.append("  bk.`entered_datetime` ");
            queryBuilder.append("from");
            queryBuilder.append("  `lcl_3p_ref_no` whse");
            queryBuilder.append("  join `lcl_booking` bk");
            queryBuilder.append("    on ( ");
            queryBuilder.append("      whse.`file_number_id` = bk.`file_number_id`");
            queryBuilder.append("      and bk.`booking_type` in ('E', 'T')");
            queryBuilder.append("    )");
            queryBuilder.append("  join `lcl_file_number` fn");
            queryBuilder.append("    on (bk.`file_number_id` = fn.`id`) ");
            queryBuilder.append("  LEFT JOIN lcl_booking_import lbm ON lbm.`file_number_id` = fn.id ");
            queryBuilder.append("where");
            queryBuilder.append("  whse.`type` = 'WH' ");
            queryBuilder.append("  and whse.`reference` like :bkWarehouseNo");
            queryBuilder.append(" union ");
            queryBuilder.append("select");
            queryBuilder.append("  fn.`id`,");
            queryBuilder.append("  qt.`entered_datetime` ");
            queryBuilder.append("from");
            queryBuilder.append("  `lcl_3p_ref_no` whse");
            queryBuilder.append("  join `lcl_quote` qt");
            queryBuilder.append("    on ( ");
            queryBuilder.append("      whse.`file_number_id` = qt.`file_number_id`");
            queryBuilder.append("      and qt.`quote_type` in ('E')");
            queryBuilder.append("    )");
            queryBuilder.append("  join `lcl_file_number` fn");
            queryBuilder.append("    on (");
            queryBuilder.append("      qt.`file_number_id` = fn.`id`");
            queryBuilder.append("      and fn.`state` = 'Q'");
            queryBuilder.append("    ) ");
            queryBuilder.append("  LEFT JOIN lcl_booking_import lbm ON lbm.`file_number_id` = fn.id ");
            queryBuilder.append("where");
            queryBuilder.append("  whse.`type` = 'WH' ");
            queryBuilder.append("  and whse.`reference` like :qtWarehouseNo");
            queryBuilder.append(showCfcl(lclSearchForm));
        } else if (CommonUtils.isNotEmpty(lclSearchForm.getCustomerPo())) {
            queryBuilder.append("select");
            queryBuilder.append("  fn.`id`,");
            queryBuilder.append("  bk.`entered_datetime` ");
            queryBuilder.append("from");
            queryBuilder.append("  `lcl_3p_ref_no` cp");
            queryBuilder.append("  join `lcl_booking` bk");
            queryBuilder.append("    on ( ");
            queryBuilder.append("      cp.`file_number_id` = bk.`file_number_id`");
            queryBuilder.append("      and bk.`booking_type` in ('E', 'T')");
            queryBuilder.append("    )");
            queryBuilder.append("  join `lcl_file_number` fn");
            queryBuilder.append("    on (bk.`file_number_id` = fn.`id`) ");
            queryBuilder.append("  LEFT JOIN lcl_booking_import lbm ON lbm.`file_number_id` = fn.id ");
            queryBuilder.append("where");
            queryBuilder.append("  cp.`type` = 'CP' ");
            queryBuilder.append("  and cp.`reference` like :bkCustomerPo");
            queryBuilder.append(" union ");
            queryBuilder.append("select");
            queryBuilder.append("  fn.`id`,");
            queryBuilder.append("  qt.`entered_datetime` ");
            queryBuilder.append("from");
            queryBuilder.append("  `lcl_3p_ref_no` cp");
            queryBuilder.append("  join `lcl_quote` qt");
            queryBuilder.append("    on ( ");
            queryBuilder.append("      cp.`file_number_id` = qt.`file_number_id`");
            queryBuilder.append("      and qt.`quote_type` in ('E')");
            queryBuilder.append("    )");
            queryBuilder.append("  join `lcl_file_number` fn");
            queryBuilder.append("    on (");
            queryBuilder.append("      qt.`file_number_id` = fn.`id`");
            queryBuilder.append("      and fn.`state` = 'Q'");
            queryBuilder.append("    ) ");
            queryBuilder.append("  LEFT JOIN lcl_booking_import lbm ON lbm.`file_number_id` = fn.id ");
            queryBuilder.append("where");
            queryBuilder.append("  cp.`type` = 'CP' ");
            queryBuilder.append("  and cp.`reference` like :qtCustomerPo");
            queryBuilder.append(showCfcl(lclSearchForm));
        } else if (CommonUtils.isNotEmpty(lclSearchForm.getTrackingNo())) {
            queryBuilder.append("select");
            queryBuilder.append("  fn.`id`,");
            queryBuilder.append("  bk.`entered_datetime` ");
            queryBuilder.append("from");
            queryBuilder.append("  `lcl_3p_ref_no` tr");
            queryBuilder.append("  join `lcl_booking` bk");
            queryBuilder.append("    on ( ");
            queryBuilder.append("      tr.`file_number_id` = bk.`file_number_id`");
            queryBuilder.append("      and bk.`booking_type` in ('E', 'T')");
            queryBuilder.append("    )");
            queryBuilder.append("  join `lcl_file_number` fn");
            queryBuilder.append("    on (bk.`file_number_id` = fn.`id`) ");
            queryBuilder.append("  LEFT JOIN lcl_booking_import lbm ON lbm.`file_number_id` = fn.id ");
            queryBuilder.append("where");
            queryBuilder.append("  tr.`type` = 'TR' ");
            queryBuilder.append("  and tr.`reference` like :bkTrackingNo");
            queryBuilder.append(" union ");
            queryBuilder.append("select");
            queryBuilder.append("  fn.`id`,");
            queryBuilder.append("  qt.`entered_datetime` ");
            queryBuilder.append("from");
            queryBuilder.append("  `lcl_3p_ref_no` tr");
            queryBuilder.append("  join `lcl_quote` qt");
            queryBuilder.append("    on ( ");
            queryBuilder.append("      tr.`file_number_id` = qt.`file_number_id`");
            queryBuilder.append("      and qt.`quote_type` in ('E')");
            queryBuilder.append("    )");
            queryBuilder.append("  join `lcl_file_number` fn");
            queryBuilder.append("    on (");
            queryBuilder.append("      qt.`file_number_id` = fn.`id`");
            queryBuilder.append("      and fn.`state` = 'Q'");
            queryBuilder.append("    ) ");
            queryBuilder.append("  LEFT JOIN lcl_booking_import lbm ON lbm.`file_number_id` = fn.id ");
            queryBuilder.append("where");
            queryBuilder.append("  tr.`type` = 'TR' ");
            queryBuilder.append("  and tr.`reference` like :qtTrackingNo");
            queryBuilder.append(showCfcl(lclSearchForm));
        } else if (CommonUtils.isNotEmpty(lclSearchForm.getSslBookingNo())) {
            queryBuilder.append("select  ");
            queryBuilder.append("  fn.`id`,");
            queryBuilder.append("  bk.`entered_datetime` ");
            queryBuilder.append(" from");
            queryBuilder.append("  `lcl_booking` bk  join lcl_ss_header lsh on (lsh.id = bk.booked_ss_header_id   ");
            queryBuilder.append("  and bk.`booking_type` in ('E', 'T') ) ");
            queryBuilder.append("  join `lcl_file_number` fn");
            queryBuilder.append("    on (bk.`file_number_id` = fn.`id`) ");
            queryBuilder.append("  LEFT JOIN lcl_booking_import lbm ON lbm.`file_number_id` = fn.id ");
            queryBuilder.append(" where lsh.schedule_no=:scheduleNo ");
            queryBuilder.append(showCfcl(lclSearchForm));
        }  else if (CommonUtils.isNotEmpty(lclSearchForm.getCurrentLocation())
                && !"Q".equalsIgnoreCase(lclSearchForm.getFilterBy())
                && !"X".equalsIgnoreCase(lclSearchForm.getFilterBy())
                && !"BNR".equalsIgnoreCase(lclSearchForm.getFilterBy())
                && !"RF".equalsIgnoreCase(lclSearchForm.getFilterBy())) {
            queryBuilder.append("  SELECT  fn.`id`, bk.`entered_datetime`,");
            queryBuilder.append(" (SELECT  dp.`elite_code` FROM lcl_booking_dispo d ");
            queryBuilder.append("  JOIN disposition dp ON d.disposition_id = dp.id ");
            queryBuilder.append("  WHERE d.file_number_id = fn.`id` ORDER BY d.id DESC LIMIT 1) AS eliteCode  ");
            queryBuilder.append("  FROM `lcl_booking` bk  JOIN `lcl_file_number` fn    ");
            queryBuilder.append("  ON (bk.`file_number_id` = fn.`id`  ");
            if ("IWB".equalsIgnoreCase(lclSearchForm.getFilterBy())) {
                //queryBuilder.append("   AND fn.status NOT IN ('X', 'RF','L','M'))   "); 
                queryBuilder.append("   AND fn.status IN ('WV','WU','B','W'))   ");
            } else if ("IPO".equalsIgnoreCase(lclSearchForm.getFilterBy())) {
                if (CommonUtils.isNotEmpty(lclSearchForm.getFilterByInventory()) && "OFF".equalsIgnoreCase(lclSearchForm.getFilterByInventory())) {
                    queryBuilder.append(" AND  bk.poo_pickup = 1 AND fn.status NOT IN ('X','RF','B','M','L'))");
                } else {
                    queryBuilder.append(" AND  bk.poo_pickup = 1 AND fn.status NOT IN ('X','R','M','L'))");
                }
            } else {
                queryBuilder.append("   AND fn.status NOT IN ('X', 'RF','L'))   ");
            }
            queryBuilder.append("  LEFT JOIN lcl_booking_import lbm ON lbm.`file_number_id` = fn.id ");
            if ("BP".equalsIgnoreCase(lclSearchForm.getFilterBy()) && CommonUtils.isEmpty(lclSearchForm.getBlPoolOwner())) {
                queryBuilder.append(" join lcl_bl fbl on (fbl.file_number_id = fn.id and fbl.posted_by_user_id is null) ");
            }
            queryBuilder.append("  JOIN  lcl_booking_dispo bdis ON bdis.`file_number_id` = fn.`id` AND     ");
            queryBuilder.append("  bdis.id =(SELECT dispo.id FROM lcl_booking_dispo dispo ");
            queryBuilder.append("  WHERE dispo.`file_number_id` = fn.id ORDER BY dispo.id DESC LIMIT 1)  ");
            queryBuilder.append("  JOIN disposition d ON d.`id` = bdis.`disposition_id` ");
            if (CommonUtils.isNotEmpty(lclSearchForm.getCreatedBy())) {
                queryBuilder.append(QuoteByQuery());
            }
            if (CommonUtils.isNotEmpty(lclSearchForm.getBlPoolOwner())) {
                queryBuilder.append(BLByQuery());
            }
            boolean includeINTR = false;
            if ("ON".equalsIgnoreCase(lclSearchForm.getIncludeIntr())) {
                queryBuilder.append("  LEFT JOIN lcl_ss_detail ld ON ld.id =  bdis.`ss_detail_id` ");
                queryBuilder.append(" WHERE  (ld.arrival_id=:curLocCode  OR bdis.un_location_id=:curLocCode ");
                includeINTR = true;
                if ("OFF".equalsIgnoreCase(lclSearchForm.getIncludeBkg())) {
                    queryBuilder.append(" ) ");
                }
            } else {
                queryBuilder.append(" AND d.elite_code != 'INTR' ");
            }
            if ("ON".equalsIgnoreCase(lclSearchForm.getIncludeBkg())) {
                if (includeINTR) {
                    queryBuilder.append("  OR  ( bk.poo_id =:curLocCode and fn.status in ('B') ) ) ");
                } else {
                    queryBuilder.append("  WHERE (bdis.`un_location_id`=:curLocCode OR ( (bk.poo_id =:curLocCode or lbm.usa_port_of_exit_id =:curLocCode ) and fn.status in ('B')) )");
                }
            }
            if ("OFF".equalsIgnoreCase(lclSearchForm.getIncludeIntr()) && "OFF".equalsIgnoreCase(lclSearchForm.getIncludeBkg())) {
                queryBuilder.append(" WHERE bdis.`un_location_id`=:curLocCode ");
            }

            queryBuilder.append(buildCommonConditionsForBooking(lclSearchForm));
            queryBuilder.append(showCfcl(lclSearchForm));
        } else if (CommonUtils.isNotEmpty(lclSearchForm.getConoslidateFiles())) {
            queryBuilder.append("select  ");
            queryBuilder.append("  fn.`id`,");
            queryBuilder.append("  bk.`entered_datetime` ");
            queryBuilder.append("from");
            queryBuilder.append("  `lcl_booking` bk ");
            queryBuilder.append("  join `lcl_file_number` fn");
            queryBuilder.append("    on (bk.`file_number_id` = fn.`id`) and bk.`booking_type` in ('E', 'T') ");
            queryBuilder.append("  LEFT JOIN lcl_booking_import lbm ON lbm.`file_number_id` = fn.id ");
            queryBuilder.append("  where");
            queryBuilder.append("   fn.file_number in(:conslalidateFiles)");
        } else {
            if ("ALL".equalsIgnoreCase(lclSearchForm.getFilterBy())) {
                queryBuilder.append("select ");
                queryBuilder.append("  fn.`id`,");
                queryBuilder.append("  bk.`entered_datetime` ");
                queryBuilder.append("from");
                queryBuilder.append("  `lcl_booking` bk");
                queryBuilder.append("  join `lcl_file_number` fn");
                queryBuilder.append("    on (");
                queryBuilder.append("      bk.`file_number_id` = fn.`id`");
                queryBuilder.append("      and fn.status not in ('X', 'RF') ");
                queryBuilder.append("    ) ");
                queryBuilder.append("  LEFT JOIN lcl_booking_import lbm ON lbm.`file_number_id` = fn.id ");
                if (CommonUtils.isNotEmpty(lclSearchForm.getCreatedBy())) {
                    queryBuilder.append(QuoteByQuery());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getBlPoolOwner())) {
                    queryBuilder.append(BLByQuery());
                }
                queryBuilder.append("where");
                queryBuilder.append("  bk.`booking_type` in ('E', 'T')");
                queryBuilder.append(buildCommonConditionsForBooking(lclSearchForm));
                if (CommonUtils.isNotEmpty(lclSearchForm.getStartDate()) && CommonUtils.isNotEmpty(lclSearchForm.getEndDate())
                        && CommonUtils.isEmpty(lclSearchForm.getBlPoolOwner())) {
                    String startDate = DateUtils.formatDate(DateUtils.parseToDateForMonthMMM(lclSearchForm.getStartDate()), "yyyy-MM-dd");
                    String endDate = DateUtils.formatDate(DateUtils.parseToDateForMonthMMM(lclSearchForm.getEndDate()), "yyyy-MM-dd");
                    if (!startDate.equalsIgnoreCase(endDate)) {
                        queryBuilder.append(" and DATE_FORMAT(bk.`entered_datetime`,'%Y-%m-%d') ").append(" between '").append(startDate).append("' and '").append(endDate).append("'");
                    } else {
                        queryBuilder.append(" and DATE_FORMAT(bk.`entered_datetime`,'%Y-%m-%d') = '").append(startDate).append("'");
                    }
                }
                queryBuilder.append(showCfcl(lclSearchForm));
                if (CommonUtils.isEmpty(lclSearchForm.getBookedBy()) && CommonUtils.isEmpty(lclSearchForm.getBlPoolOwner())) {
                    queryBuilder.append(" union ");
                    queryBuilder.append("select ");
                    queryBuilder.append("  fn.`id`,");
                    queryBuilder.append("  qt.`entered_datetime` ");
                    queryBuilder.append("from");
                    queryBuilder.append("  `lcl_quote` qt ");
                    queryBuilder.append("  join `lcl_file_number` fn");
                    queryBuilder.append("    on (");
                    queryBuilder.append("      qt.`file_number_id` = fn.`id`");
                    queryBuilder.append("      and fn.`state` = 'Q'");
                    queryBuilder.append("      and fn.status not in ('X', 'RF')");
                    queryBuilder.append("    ) ");
                    queryBuilder.append("where");
                    queryBuilder.append("  qt.`quote_type` in ('E')");
                    queryBuilder.append(buildCommonConditionsForQuote(lclSearchForm));
                    if (CommonUtils.isNotEmpty(lclSearchForm.getStartDate()) && CommonUtils.isNotEmpty(lclSearchForm.getEndDate())) {
                        String startDate = DateUtils.formatDate(DateUtils.parseToDateForMonthMMM(lclSearchForm.getStartDate()), "yyyy-MM-dd");
                        String endDate = DateUtils.formatDate(DateUtils.parseToDateForMonthMMM(lclSearchForm.getEndDate()), "yyyy-MM-dd");
                        if (!startDate.equalsIgnoreCase(endDate)) {
                            queryBuilder.append(" and DATE_FORMAT(qt.`entered_datetime`,'%Y-%m-%d') ").append(" between '").append(startDate).append("' and '").append(endDate).append("'");
                        } else {
                            queryBuilder.append(" and DATE_FORMAT(qt.`entered_datetime`,'%Y-%m-%d') = '").append(startDate).append("'");
                        }
                    }
                    queryBuilder.append(showCfcl(lclSearchForm));
                }

            } else if ("IWB".equalsIgnoreCase(lclSearchForm.getFilterBy())) {
                queryBuilder.append("select ");
                queryBuilder.append("  fn.`id`,");
                queryBuilder.append("  bk.`entered_datetime` ");
                queryBuilder.append("from");
                queryBuilder.append("  `lcl_booking` bk");
                queryBuilder.append("  join `lcl_file_number` fn");
                queryBuilder.append("    on (");
                queryBuilder.append("      bk.`file_number_id` = fn.`id`");
                if ("OFF".equalsIgnoreCase(lclSearchForm.getFilterByInventory())) {
                    queryBuilder.append("      and fn.status in ('WV','WU','W')");
                } else {
                    queryBuilder.append("      and fn.status in ('WV','WU','B','W')");
                }
                queryBuilder.append("    ) ");
                queryBuilder.append("  LEFT JOIN lcl_booking_import lbm ON lbm.`file_number_id` = fn.id ");
                if (CommonUtils.isNotEmpty(lclSearchForm.getCreatedBy())) {
                    queryBuilder.append(QuoteByQuery());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getBlPoolOwner())) {
                    queryBuilder.append(BLByQuery());
                }
                queryBuilder.append("where");
                queryBuilder.append("  bk.`booking_type` in ('E', 'T')");
                queryBuilder.append(buildCommonConditionsForBooking(lclSearchForm));
                queryBuilder.append(showCfcl(lclSearchForm));
            } else if ("Q".equalsIgnoreCase(lclSearchForm.getFilterBy())) {
                queryBuilder.append("select ");
                queryBuilder.append("  fn.`id`,");
                queryBuilder.append("  qt.`entered_datetime` ");
                queryBuilder.append("from");
                queryBuilder.append("  `lcl_quote` qt ");
                queryBuilder.append("  join `lcl_file_number` fn");
                queryBuilder.append("    on (");
                queryBuilder.append("      qt.`file_number_id` = fn.`id`");
                queryBuilder.append("      and fn.`state` = 'Q'");
                queryBuilder.append("      and fn.status not in ('X', 'RF')");
                queryBuilder.append("    ) ");
                if (CommonUtils.isNotEmpty(lclSearchForm.getBookedBy())) {
                    queryBuilder.append(BookingByQuery());
                }
                queryBuilder.append("where");
                queryBuilder.append("  qt.`quote_type` in ('E')");
                queryBuilder.append(buildCommonConditionsForQuote(lclSearchForm));
                queryBuilder.append(showCfcl(lclSearchForm));
            } else if ("B".equalsIgnoreCase(lclSearchForm.getFilterBy())) {
                queryBuilder.append("select ");
                queryBuilder.append("  fn.`id`,");
                queryBuilder.append("  bk.`entered_datetime` ");
                queryBuilder.append("from");
                queryBuilder.append("  `lcl_booking` bk");
                queryBuilder.append("  join `lcl_file_number` fn");
                queryBuilder.append("    on (");
                queryBuilder.append("      bk.`file_number_id` = fn.`id`");
                queryBuilder.append("      and fn.status not in ('X', 'RF')");
                queryBuilder.append("    ) ");
                queryBuilder.append("  LEFT JOIN lcl_booking_import lbm ON lbm.`file_number_id` = fn.id ");
                if (CommonUtils.isNotEmpty(lclSearchForm.getCreatedBy())) {
                    queryBuilder.append(QuoteByQuery());
                }
                queryBuilder.append("where");
                queryBuilder.append("  bk.`booking_type` in ('E', 'T')");
                queryBuilder.append(buildCommonConditionsForBooking(lclSearchForm));
                queryBuilder.append(showCfcl(lclSearchForm));
            } else if ("X".equalsIgnoreCase(lclSearchForm.getFilterBy())) {
                queryBuilder.append("select ");
                queryBuilder.append("  fn.`id`,");
                queryBuilder.append("  bk.`entered_datetime` ");
                queryBuilder.append("from");
                queryBuilder.append("  `lcl_booking` bk");
                queryBuilder.append("  join `lcl_file_number` fn");
                queryBuilder.append("    on (");
                queryBuilder.append("      bk.`file_number_id` = fn.`id`");
                queryBuilder.append("      and fn.status = 'X'");
                queryBuilder.append("    ) ");
                queryBuilder.append("  LEFT JOIN lcl_booking_import lbm ON lbm.`file_number_id` = fn.id ");
                if (CommonUtils.isNotEmpty(lclSearchForm.getCreatedBy())) {
                    queryBuilder.append(QuoteByQuery());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getBlPoolOwner())) {
                    queryBuilder.append(BLByQuery());
                }
                queryBuilder.append("where");
                queryBuilder.append("  bk.`booking_type` in ('E', 'T')");
                queryBuilder.append(buildCommonConditionsForBooking(lclSearchForm));
                queryBuilder.append(showCfcl(lclSearchForm));
                if (CommonUtils.isEmpty(lclSearchForm.getBookedBy())) {
                    queryBuilder.append(" union ");
                    queryBuilder.append("select ");
                    queryBuilder.append("  fn.`id`,");
                    queryBuilder.append("  qt.`entered_datetime` ");
                    queryBuilder.append("from");
                    queryBuilder.append("  `lcl_quote` qt ");
                    queryBuilder.append("  join `lcl_file_number` fn");
                    queryBuilder.append("    on (");
                    queryBuilder.append("      qt.`file_number_id` = fn.`id`");
                    queryBuilder.append("      and fn.`state` = 'Q'");
                    queryBuilder.append("      and fn.status = 'X'");
                    queryBuilder.append("    ) ");
                    queryBuilder.append("where");
                    queryBuilder.append("  qt.`quote_type` in ('E')");
                    queryBuilder.append(buildCommonConditionsForQuote(lclSearchForm));
                    queryBuilder.append(showCfcl(lclSearchForm));
                }
            } else if ("RF".equalsIgnoreCase(lclSearchForm.getFilterBy())) {
                queryBuilder.append("select ");
                queryBuilder.append("  fn.`id`,");
                queryBuilder.append("  bk.`entered_datetime` ");
                queryBuilder.append("from");
                queryBuilder.append("  `lcl_booking` bk");
                queryBuilder.append("  join `lcl_file_number` fn");
                queryBuilder.append("    on (");
                queryBuilder.append("      bk.`file_number_id` = fn.`id`");
                queryBuilder.append("      and fn.status = 'RF'");
                queryBuilder.append("    ) ");
                queryBuilder.append("  LEFT JOIN lcl_booking_import lbm ON lbm.`file_number_id` = fn.id ");
                if (CommonUtils.isNotEmpty(lclSearchForm.getBlPoolOwner())) {
                    queryBuilder.append(BLByQuery());
                }
                queryBuilder.append("where");
                queryBuilder.append("  bk.`booking_type` in ('E', 'T')");
                queryBuilder.append(buildCommonConditionsForBooking(lclSearchForm));
                queryBuilder.append(showCfcl(lclSearchForm));
            } else if ("BL".equalsIgnoreCase(lclSearchForm.getFilterBy())) {
                queryBuilder.append("SELECT t.id,t.entered_datetime FROM ( ");
                queryBuilder.append("select ");
                queryBuilder.append("  fn.`id`,");
                queryBuilder.append("  bk.`entered_datetime`, ");
                queryBuilder.append("  (SELECT posted_by_user_id FROM lcl_bl WHERE file_number_id = getHouseBLForConsolidateDr (fn.id) LIMIT 1) AS  postedUser ");
                queryBuilder.append("from");
                queryBuilder.append("  `lcl_booking` bk");
                queryBuilder.append("  join `lcl_file_number` fn");
                queryBuilder.append("    on (");
                queryBuilder.append("      bk.`file_number_id` = fn.`id`");
                queryBuilder.append("      and ((fn.status = 'L' and fn.state='B') or (fn.status not in ('M','X','RF','V') and fn.state='BL'))");
                queryBuilder.append("    ) ");
                if (CommonUtils.isNotEmpty(lclSearchForm.getStartDate()) && CommonUtils.isNotEmpty(lclSearchForm.getEndDate())) {
                    queryBuilder.append(" JOIN lcl_booking_piece lbp on lbp.file_number_id = fn.`id`");
                    queryBuilder.append(" JOIN lcl_booking_piece_unit lbpu ON lbpu.booking_piece_id=lbp.id ");
                    queryBuilder.append(" JOIN lcl_unit_ss lus ON lus.id=lbpu.lcl_unit_ss_id");
                    queryBuilder.append(" JOIN lcl_ss_header lsh ON lsh.id=lus.ss_header_id");
                    queryBuilder.append(" JOIN lcl_ss_detail lsd on lsd.ss_header_id=lsh.id ");
                    queryBuilder.append(" and lsd.id=(select ls.id from lcl_ss_detail ls where ls.ss_header_id = lsh.id order by ls.id desc limit 1)");
                    queryBuilder.append(" AND lsh.service_type IN ('E','C') ");
                }
                queryBuilder.append("  LEFT JOIN lcl_booking_import lbm ON lbm.`file_number_id` = fn.id ");
                if (CommonUtils.isNotEmpty(lclSearchForm.getCreatedBy())) {
                    queryBuilder.append(QuoteByQuery());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getBlPoolOwner())) {
                    queryBuilder.append(BLByQuery());
                }
                queryBuilder.append("where");
                queryBuilder.append("  bk.`booking_type` in ('E', 'T') and fn.posted_by_user_id is null");
                queryBuilder.append(buildCommonConditionsForBooking(lclSearchForm));
                if (CommonUtils.isNotEmpty(lclSearchForm.getStartDate()) && CommonUtils.isNotEmpty(lclSearchForm.getEndDate())) {
                    String startDate = DateUtils.formatDate(DateUtils.parseToDateForMonthMMM(lclSearchForm.getStartDate()), "yyyy-MM-dd");
                    String endDate = DateUtils.formatDate(DateUtils.parseToDateForMonthMMM(lclSearchForm.getEndDate()), "yyyy-MM-dd");
                    if (!startDate.equalsIgnoreCase(endDate)) {
                        queryBuilder.append(" and DATE_FORMAT(lsd.std,'%Y-%m-%d') ").append(" between '").append(startDate).append("' and '").append(endDate).append("'");
                    } else {
                        queryBuilder.append(" and DATE_FORMAT(lsd.std,'%Y-%m-%d') = '").append(startDate).append("'");
                    }
                }
                queryBuilder.append(showCfcl(lclSearchForm));
                queryBuilder.append(") AS t WHERE t.postedUser IS NULL");
            } else if ("BP".equalsIgnoreCase(lclSearchForm.getFilterBy())) {
                queryBuilder.append("select ");
                queryBuilder.append("  fn.`id`,");
                queryBuilder.append("  bk.`entered_datetime` ");
                queryBuilder.append("from");
                queryBuilder.append("  `lcl_booking` bk");
                queryBuilder.append("  join `lcl_file_number` fn");
                queryBuilder.append("    on (");
                queryBuilder.append("      bk.`file_number_id` = fn.`id`");
                queryBuilder.append("      and fn.status not in ('M','X','RF','V') and fn.state='BL'");
                queryBuilder.append("    ) ");
                if (CommonUtils.isNotEmpty(lclSearchForm.getStartDate()) && CommonUtils.isNotEmpty(lclSearchForm.getEndDate())) {
                    queryBuilder.append(" JOIN lcl_booking_piece lbp on lbp.file_number_id = fn.`id`");
                    queryBuilder.append(" JOIN lcl_booking_piece_unit lbpu ON lbpu.booking_piece_id=lbp.id ");
                    queryBuilder.append(" JOIN lcl_unit_ss lus ON lus.id=lbpu.lcl_unit_ss_id");
                    queryBuilder.append(" JOIN lcl_ss_header lsh ON lsh.id=lus.ss_header_id");
                    queryBuilder.append(" JOIN lcl_ss_detail lsd on lsd.ss_header_id=lsh.id ");
                    queryBuilder.append(" and lsd.id=(select ls.id from lcl_ss_detail ls where ls.ss_header_id = lsh.id order by ls.id desc limit 1)");
                    queryBuilder.append(" AND lsh.service_type IN ('E','C') ");
                }
                queryBuilder.append("  LEFT JOIN lcl_booking_import lbm ON lbm.`file_number_id` = fn.id ");
                if (CommonUtils.isNotEmpty(lclSearchForm.getCreatedBy())) {
                    queryBuilder.append(QuoteByQuery());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getBlPoolOwner())) {
                    queryBuilder.append(BLByQuery());
                } else {
                    queryBuilder.append(" join lcl_bl bl on (bl.file_number_id = fn.id and bl.posted_by_user_id is null) ");
                }
                queryBuilder.append("where");
                queryBuilder.append("  bk.`booking_type` in ('E', 'T')");
                queryBuilder.append(buildCommonConditionsForBooking(lclSearchForm));
                if (CommonUtils.isNotEmpty(lclSearchForm.getStartDate()) && CommonUtils.isNotEmpty(lclSearchForm.getEndDate())) {
                    String startDate = DateUtils.formatDate(DateUtils.parseToDateForMonthMMM(lclSearchForm.getStartDate()), "yyyy-MM-dd");
                    String endDate = DateUtils.formatDate(DateUtils.parseToDateForMonthMMM(lclSearchForm.getEndDate()), "yyyy-MM-dd");
                    if (!startDate.equalsIgnoreCase(endDate)) {
                        queryBuilder.append(" and DATE_FORMAT(lsd.std,'%Y-%m-%d') ").append(" between '").append(startDate).append("' and '").append(endDate).append("'");
                    } else {
                        queryBuilder.append(" and DATE_FORMAT(lsd.std,'%Y-%m-%d') = '").append(startDate).append("'");
                    }
                }
                queryBuilder.append(showCfcl(lclSearchForm));
            } else if ("ONBK".equalsIgnoreCase(lclSearchForm.getFilterBy())) {
                queryBuilder.append("select ");
                queryBuilder.append("  fn.`id`,");
                queryBuilder.append("  bk.`entered_datetime` ");
                queryBuilder.append("from");
                queryBuilder.append("  `lcl_booking` bk");
                queryBuilder.append("  join `lcl_file_number` fn");
                queryBuilder.append("    on (");
                queryBuilder.append("      bk.`file_number_id` = fn.`id`");
                queryBuilder.append("      and fn.status != 'Q'  and fn.datasource ='W' ");
                queryBuilder.append("    ) ");
                queryBuilder.append("  LEFT JOIN lcl_booking_import lbm ON lbm.`file_number_id` = fn.id ");
                if (CommonUtils.isNotEmpty(lclSearchForm.getBlPoolOwner())) {
                    queryBuilder.append(BLByQuery());
                }
                queryBuilder.append("where");
                queryBuilder.append("  bk.`booking_type` in ('E', 'T')");
                queryBuilder.append(buildCommonConditionsForBooking(lclSearchForm));
                queryBuilder.append(showCfcl(lclSearchForm));
            } else if ("IPO".equalsIgnoreCase(lclSearchForm.getFilterBy())) {
                queryBuilder.append("select ");
                queryBuilder.append("  fn.`id`,");
                queryBuilder.append("  bk.`entered_datetime` ");
                queryBuilder.append("from");
                queryBuilder.append("  `lcl_booking` bk");
                queryBuilder.append("  join `lcl_file_number` fn");
                queryBuilder.append("    on (");
                queryBuilder.append("      bk.`file_number_id` = fn.`id` and  bk.poo_pickup = 1 ");
                if (CommonUtils.isNotEmpty(lclSearchForm.getFilterByInventory()) && "OFF".equalsIgnoreCase(lclSearchForm.getFilterByInventory())) {
                    queryBuilder.append("      and fn.status NOT IN ('X','RF','B','M','L')");
                } else {
                    queryBuilder.append("      and fn.status NOT IN ('X','R','M','L')");
                }
                queryBuilder.append("    ) ");
                queryBuilder.append("  LEFT JOIN lcl_booking_import lbm ON lbm.`file_number_id` = fn.id ");
                queryBuilder.append("  LEFT JOIN lcl_booking_pad lbkpad ON (lbkpad.`file_number_id` = bk.`file_number_id` ) ");
                queryBuilder.append("  LEFT JOIN lcl_contact lclcon ON (lbkpad.`pickup_contact_id` = lclcon.`id` AND  lclcon.`city` <> NULL) ");
                if (CommonUtils.isNotEmpty(lclSearchForm.getCreatedBy())) {
                    queryBuilder.append(QuoteByQuery());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getBlPoolOwner())) {
                    queryBuilder.append(BLByQuery());
                }
                queryBuilder.append("where");
                queryBuilder.append("  bk.`booking_type` in ('E', 'T') ");
                queryBuilder.append(buildCommonConditionsForBooking(lclSearchForm));
                queryBuilder.append(showCfcl(lclSearchForm));
            } else if ("UND".equalsIgnoreCase(lclSearchForm.getFilterBy())) {
                queryBuilder.append("select ");
                queryBuilder.append("  fn.`id`,");
                queryBuilder.append("  bk.`entered_datetime` ");
                queryBuilder.append("from");
                queryBuilder.append("  `lcl_booking` bk");
                queryBuilder.append("  join `lcl_file_number` fn");
                queryBuilder.append("    on (");
                queryBuilder.append("      bk.`file_number_id` = fn.`id`");
                queryBuilder.append("      and fn.status not in ('X', 'RF')");
                queryBuilder.append("      and bk.non_rated='1'");
                queryBuilder.append("    ) ");
                queryBuilder.append("  LEFT JOIN lcl_booking_import lbm ON lbm.`file_number_id` = fn.id ");
                if (CommonUtils.isNotEmpty(lclSearchForm.getCreatedBy())) {
                    queryBuilder.append(QuoteByQuery());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getBlPoolOwner())) {
                    queryBuilder.append(BLByQuery());
                }
                queryBuilder.append("where");
                queryBuilder.append("  bk.`booking_type` in ('E', 'T') ");
                queryBuilder.append(buildCommonConditionsForBooking(lclSearchForm));
                if (CommonUtils.isEmpty(lclSearchForm.getBookedBy()) && CommonUtils.isEmpty(lclSearchForm.getBlPoolOwner())) {
                    queryBuilder.append(" union ");
                    queryBuilder.append("select ");
                    queryBuilder.append("  fn.`id`,");
                    queryBuilder.append("  qt.`entered_datetime` ");
                    queryBuilder.append("from");
                    queryBuilder.append("  `lcl_quote` qt ");
                    queryBuilder.append("  join `lcl_file_number` fn");
                    queryBuilder.append("    on (");
                    queryBuilder.append("      qt.`file_number_id` = fn.`id`");
                    queryBuilder.append("      and fn.`state` = 'Q'");
                    queryBuilder.append("      and fn.status not in ('X', 'RF')");
                    queryBuilder.append("      and qt.non_rated='1'");
                    queryBuilder.append("    ) ");
                    queryBuilder.append("where");
                    queryBuilder.append("  qt.`quote_type` in ('E')");
                    queryBuilder.append(buildCommonConditionsForQuote(lclSearchForm));
                    if (CommonUtils.isNotEmpty(lclSearchForm.getStartDate()) && CommonUtils.isNotEmpty(lclSearchForm.getEndDate())) {
                        String startDate = DateUtils.formatDate(DateUtils.parseToDateForMonthMMM(lclSearchForm.getStartDate()), "yyyy-MM-dd");
                        String endDate = DateUtils.formatDate(DateUtils.parseToDateForMonthMMM(lclSearchForm.getEndDate()), "yyyy-MM-dd");
                        if (!startDate.equalsIgnoreCase(endDate)) {
                            queryBuilder.append(" and DATE_FORMAT(qt.`entered_datetime`,'%Y-%m-%d') ").append(" between '").append(startDate).append("' and '").append(endDate).append("'");
                        } else {
                            queryBuilder.append(" and DATE_FORMAT(qt.`entered_datetime`,'%Y-%m-%d') = '").append(startDate).append("'");
                        }
                    }
                    queryBuilder.append(showCfcl(lclSearchForm));
                }
                queryBuilder.append(showCfcl(lclSearchForm));
            } else if ("BNR".equalsIgnoreCase(lclSearchForm.getFilterBy())) {
                queryBuilder.append("select ");
                queryBuilder.append("  fn.`id`,");
                queryBuilder.append("  bk.`entered_datetime`, ");
                queryBuilder.append(" (SELECT  dp.`elite_code` FROM lcl_booking_dispo d ");
                queryBuilder.append("  JOIN disposition dp ON d.disposition_id = dp.id ");
                queryBuilder.append("  WHERE d.file_number_id = fn.`id` ORDER BY d.id DESC LIMIT 1) AS eliteCode  ");
                queryBuilder.append("from");
                queryBuilder.append("  `lcl_booking` bk");
                queryBuilder.append("  join `lcl_file_number` fn");
                queryBuilder.append("    on (");
                queryBuilder.append("      bk.`file_number_id` = fn.`id`");
                 queryBuilder.append("      and fn.`status` != 'X'");
                queryBuilder.append("    ) ");
                queryBuilder.append("  LEFT JOIN lcl_booking_import lbm ON lbm.`file_number_id` = fn.id ");
                if (CommonUtils.isNotEmpty(lclSearchForm.getCreatedBy())) {
                    queryBuilder.append(QuoteByQuery());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getBlPoolOwner())) {
                    queryBuilder.append(BLByQuery());
                }
                queryBuilder.append("where");
                queryBuilder.append("  bk.`booking_type` in ('E', 'T')");
                queryBuilder.append(buildCommonConditionsForBooking(lclSearchForm));
                queryBuilder.append(showCfcl(lclSearchForm));
                queryBuilder.append("   GROUP BY fn.id HAVING eliteCode = 'OBKG' ");
            }
            searchCount.append(queryBuilder);
            getcount(queryBuilder.toString(), lclSearchForm);
            queryBuilder.append(" order by id desc");
            if (!"IWB".equalsIgnoreCase(lclSearchForm.getFilterBy()) && !"BL".equalsIgnoreCase(lclSearchForm.getFilterBy()) 
                    && !"BP".equalsIgnoreCase(lclSearchForm.getFilterBy())) {
                queryBuilder.append(" limit ").append(lclSearchForm.getLimit());
            }
        }

        if (searchCount.toString().equals("")) {
            getcount(queryBuilder.toString(), lclSearchForm);
        }

        return queryBuilder.toString();
    }

    private String buildCommonConditionsForBooking(LclSearchForm lclSearchForm) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        if (CommonUtils.isNotEmpty(lclSearchForm.getClientNo())) {
            queryBuilder.append("  and bk.`client_acct_no` = :bkClientNo");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getShipperNo())) {
            queryBuilder.append("  and bk.`ship_acct_no` = :bkShipperNo");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getConsigneeNo())) {
            queryBuilder.append("  and bk.`cons_acct_no` = :bkConsigneeNo");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getForwarderNo())) {
            queryBuilder.append("  and bk.`fwd_acct_no` = :bkFwdNo");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getForeignAgentAccount())) {
            queryBuilder.append("  and bk.`agent_acct_no` = :bkForeignAgentNo");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getOrigin())) {
            queryBuilder.append("  and (bk.`poo_id` IN (SELECT u.id FROM un_location u WHERE u.`un_loc_code` IN(:bkOrigin))");
            queryBuilder.append("  OR bk.pod_id IN (SELECT u.id FROM un_location u WHERE u.`un_loc_code` IN(:bkOrigin)))");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getPol())) {
            queryBuilder.append("  and (bk.`pol_id` IN (SELECT u.id FROM un_location u WHERE u.`un_loc_code` IN(:bkPol))");
            queryBuilder.append("  OR lbm.usa_port_of_exit_id IN (SELECT u.id FROM un_location u WHERE u.`un_loc_code` IN(:bkPol)))");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getPodCountryCode())) {
            queryBuilder.append("  and (bk.`pod_id` = :bkPod OR lbm.foreign_port_of_discharge_id =:bkPod )");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getBookedForVoyage())) {
            queryBuilder.append("  AND bk.booked_ss_header_id=(SELECT id FROM lcl_ss_header WHERE schedule_no=:bkgVoyage)");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getDestCountryCode())) {
            queryBuilder.append("  and bk.`fd_id` = :bkDestination");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getOriginRegion())) {
            queryBuilder.append("  and bk.`poo_id` in (");
            queryBuilder.append("    select ");
            queryBuilder.append("      un.`id`");
            queryBuilder.append("     from");
            queryBuilder.append("       `ports` p");
            queryBuilder.append("       join `un_location` un");
            queryBuilder.append("         on (p.`unlocationcode` = un.`un_loc_code`)");
            queryBuilder.append("     where");
            queryBuilder.append("        p.`regioncode` = :bkOriginRegion");
            queryBuilder.append("    )");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getDestinationRegion())) {
            queryBuilder.append("  and bk.`fd_id` in (");
            queryBuilder.append("    select ");
            queryBuilder.append("      un.`id`");
            queryBuilder.append("     from");
            queryBuilder.append("       `ports` p");
            queryBuilder.append("       join `un_location` un");
            queryBuilder.append("         on (p.`unlocationcode` = un.`un_loc_code`)");
            queryBuilder.append("     where");
            queryBuilder.append("        p.`regioncode` =:bkDestinationRegion");
            queryBuilder.append("    )");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getIssuingTerminal())) {
            if ("ON".equalsIgnoreCase(lclSearchForm.getAssociatedTerminal())) {
                queryBuilder.append("  and bk.`billing_terminal` in (");
                queryBuilder.append("    select ");
                queryBuilder.append("      tm1.`trmnum`");
                queryBuilder.append("     FROM `terminal` tm");
                queryBuilder.append("       JOIN `terminal` tm1");
                queryBuilder.append("           ON tm.unLocationCode1 = tm1.unLocationCode1");
                queryBuilder.append("     where");
                queryBuilder.append("        tm.`terminal_location` = :bkIssuingTerminal");
                queryBuilder.append("        AND tm.unloccode != ''");
                queryBuilder.append("        AND tm1.actyon IN('Y','C','F')");
                queryBuilder.append("    )");
            }else{
                queryBuilder.append("  and bk.`billing_terminal` in (");
                queryBuilder.append("    select ");
                queryBuilder.append("      tm.`trmnum`");
                queryBuilder.append("     from");
                queryBuilder.append("       `terminal` tm");
                queryBuilder.append("     where");
                queryBuilder.append("        tm.`terminal_location` = :bkIssuingTerminal");
                queryBuilder.append("    )");
            }
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getBookedBy())) {
            queryBuilder.append("  and bk.`entered_by_user_id` in (");
            queryBuilder.append("    select ");
            queryBuilder.append("      ud.`user_id`");
            queryBuilder.append("     from");
            queryBuilder.append("       `user_details` ud");
            queryBuilder.append("     where");
            queryBuilder.append("        ud.`login_name` = :bookedBy");
            queryBuilder.append("    )");
        }

        return queryBuilder.toString();
    }

    private String buildCommonConditionsForQuote(LclSearchForm lclSearchForm) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        if (CommonUtils.isNotEmpty(lclSearchForm.getClientNo())) {
            queryBuilder.append("  and qt.`client_acct_no` = :qtClientNo");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getShipperNo())) {
            queryBuilder.append("  and qt.`ship_acct_no` = :qtShipperNo");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getConsigneeNo())) {
            queryBuilder.append("  and qt.`cons_acct_no` = :qtConsigneeNo");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getForwarderNo())) {
            queryBuilder.append("  and qt.`fwd_acct_no` = :qtFwdNo");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getForeignAgentAccount())) {
            queryBuilder.append("  and qt.`agent_acct_no` = :qtForeignAgentNo");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getOrigin())) {
            queryBuilder.append("  and qt.`poo_id`  IN (SELECT u.id FROM un_location u WHERE u.`un_loc_code` IN(:qtOrigin))");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getPol())) {
            queryBuilder.append("  and qt.`pol_id` IN (SELECT u.id FROM un_location u WHERE u.`un_loc_code` IN(:qtPol))");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getPodCountryCode())) {
            queryBuilder.append("  and qt.`pod_id` = :qtPod");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getDestCountryCode())) {
            queryBuilder.append("  and qt.`fd_id` = :qtDestination");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getBookedForVoyage())) {
            queryBuilder.append("  AND qt.booked_ss_header_id=(SELECT id FROM lcl_ss_header WHERE schedule_no=:bkgVoyage)");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getOriginRegion())) {
            queryBuilder.append("  and qt.`poo_id` in (");
            queryBuilder.append("    select ");
            queryBuilder.append("      un.`id`");
            queryBuilder.append("     from");
            queryBuilder.append("       `ports` p");
            queryBuilder.append("       join `un_location` un");
            queryBuilder.append("         on (p.`unlocationcode` = un.`un_loc_code`)");
            queryBuilder.append("     where");
            queryBuilder.append("        p.`regioncode` = :qtOriginRegion");
            queryBuilder.append("    )");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getDestinationRegion())) {
            queryBuilder.append("  and qt.`fd_id` in (");
            queryBuilder.append("    select ");
            queryBuilder.append("      un.`id`");
            queryBuilder.append("     from");
            queryBuilder.append("       `ports` p");
            queryBuilder.append("       join `un_location` un");
            queryBuilder.append("         on (p.`unlocationcode` = un.`un_loc_code`)");
            queryBuilder.append("     where");
            queryBuilder.append("        p.`regioncode` = :qtDestinationRegion");
            queryBuilder.append("    )");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getIssuingTerminal())) {
            if ("ON".equalsIgnoreCase(lclSearchForm.getAssociatedTerminal())) {
                queryBuilder.append("  and qt.`billing_terminal` in (");
                queryBuilder.append("    select ");
                queryBuilder.append("      tm1.`trmnum`");
                queryBuilder.append("     FROM `terminal` tm");
                queryBuilder.append("       JOIN `terminal` tm1");
                queryBuilder.append("           ON tm.unLocationCode1 = tm1.unLocationCode1");
                queryBuilder.append("     where");
                queryBuilder.append("        tm.`terminal_location` = :qtIssuingTerminal");
                queryBuilder.append("        AND tm.unloccode != ''");
                queryBuilder.append("        AND tm1.actyon IN('Y','C','F')");
                queryBuilder.append("    )");
            }else{
                queryBuilder.append("  and qt.`billing_terminal` in (");
                queryBuilder.append("    select ");
                queryBuilder.append("      tm.`trmnum`");
                queryBuilder.append("     from");
                queryBuilder.append("       `terminal` tm");
                queryBuilder.append("     where");
                queryBuilder.append("        tm.`terminal_location` = :qtIssuingTerminal");
                queryBuilder.append("    )");
            }
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getCreatedBy())) {
            queryBuilder.append("  and qt.`entered_by_user_id` in (");
            queryBuilder.append("    select ");
            queryBuilder.append("      ud.`user_id`");
            queryBuilder.append("     from");
            queryBuilder.append("       `user_details` ud");
            queryBuilder.append("     where");
            queryBuilder.append("        ud.`login_name` = :quoteBy");
            queryBuilder.append("    )");
        }

        return queryBuilder.toString();
    }

    private String mainAccessQuery(LclSearchForm lclSearchForm) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" select f.`file_number_id` AS fileNumberId, ");
        stringBuilder.append("    f.file_state  as fileState,");
        stringBuilder.append("    f.quoteComplete  as quoteComplete,");
        stringBuilder.append("    f.file_number  as fileNumber, ");
        stringBuilder.append("    f.file_status as fileStatus, ");
        stringBuilder.append("    f.original_file_status as originalFileStatus, ");
        stringBuilder.append("	  if(f.file_state = 'BL', ");
        stringBuilder.append("    (select lf.status from lcl_file_number lf join lcl_bl bl ");
        stringBuilder.append("     on bl.file_number_id = lf.id where bl.file_number_id = f.consolidateBlId),f.file_status ) AS currentFileStatus, ");
        stringBuilder.append("    f.client_pwk as clientPwk,");
        stringBuilder.append("    f.booking_type as bookingType,");
        stringBuilder.append("    f.schedule_no as scheduleNo,");
        stringBuilder.append("    f.sailDate as sailingDate,");
        stringBuilder.append("   DATE_FORMAT(f.sailDate,'%d-%b-%Y') AS sailDate, ");
        stringBuilder.append("    f.pol_lrdt as polLrdDate,");
        stringBuilder.append("    DATE_FORMAT(f.pol_lrdt,'%d-%b-%Y') AS polLrd , ");
        stringBuilder.append("    f.poo_lrdt as pooLrdDate,");
        stringBuilder.append("    DATE_FORMAT(f.poo_lrdt,'%d-%b-%Y') AS pooLrd , ");

        stringBuilder.append("    f.transPolCode as transPolCode,");
        stringBuilder.append("    f.transPolName as transPolName,");
        stringBuilder.append("    f.transPolState as transPolState,");
        stringBuilder.append("    f.transPodCode as transPodCode,");
        stringBuilder.append("    f.transPodName as transPodName,");
        stringBuilder.append("	  f.transPodCountry as transPodCountry,");
        stringBuilder.append("    (select GROUP_CONCAT(lc.id) from lcl_correction lc ");
        stringBuilder.append("   where lc.file_number_id = f.consolidateBlId AND lc.void = 0 )  AS correctionIds,");
        stringBuilder.append("    UnLocationGetCodeByID(f.poo_id) AS originUncode,	");
        stringBuilder.append("   `UnLocationGetNameStateCntryByID` (f.`poo_id`) AS origin,");
        stringBuilder.append("    UnLocationGetCodeByID(f.pol_id) AS polUncode,");
        stringBuilder.append("   `UnLocationGetNameStateCntryByID` (f.`pol_id`) AS pol,");
        stringBuilder.append("	  UnLocationGetCodeByID(f.pod_id) AS podUncode,");
        stringBuilder.append("	 `UnLocationGetNameStateCntryByID` (f.`pod_id`) AS pod,");
        stringBuilder.append("	  UnLocationGetCodeByID(f.fd_id) AS destinationUncode,");
        stringBuilder.append("	 `UnLocationGetNameStateCntryByID` (f.`fd_id`) AS destination,");
        stringBuilder.append("     Filenumber_frontUnloccode(f.file_number,f.`file_number_id`,f.poo_id,f.transshipment,f.shortShip) AS FileNoOriginUncode,");
        stringBuilder.append("	  f.`relay_override` AS relayOverride,");

        stringBuilder.append("	  IF(ship.`acct_name` <> '',ship.`acct_name`,shipc.`company_name`)  AS shipName,");
        stringBuilder.append("    ship.`acct_no` AS shipNo,");
        stringBuilder.append("    shipc.`address` AS shipAddress,");
        stringBuilder.append("    shipc.`city` AS shipCity,");
        stringBuilder.append("    shipc.`state` AS shipState,");
        stringBuilder.append("    shipc.`zip` AS shipZip,");

        stringBuilder.append("    IF(cons.`acct_name` <> '',cons.`acct_name`,consc.`company_name`) AS consName,");
        stringBuilder.append("    cons.`acct_no` AS consNo,");
        stringBuilder.append("    consc.`address` AS consAddress,");
        stringBuilder.append("    consc.`city` AS consCity,");
        stringBuilder.append("    consc.`state` AS consState,");
        stringBuilder.append("    consc.`zip` AS consZip,");
        stringBuilder.append("    IF(fwd.acct_name <> '',fwd.acct_name,fwdc.`company_name`)  AS fwdName,");
        stringBuilder.append("    fwd.`acct_no` AS fwdNo,");
        stringBuilder.append("	    fwdc.`address` AS fwdAddress,");
        stringBuilder.append("    fwdc.`city` AS fwdCity,");
        stringBuilder.append("    fwdc.`state` AS fwdState,");
        stringBuilder.append("    fwdc.`zip` AS fwdZip,");
        stringBuilder.append("    TerminalGetLocationByNo (f.`billing_terminal`) AS billingTerminal,");
        stringBuilder.append("    f.poo_pickup as pooPickup,");
        stringBuilder.append("    COALESCE((SELECT lbp.pickup_city FROM lcl_booking_pad lbp ");
        stringBuilder.append("    WHERE lbp.file_number_id = f.file_number_id LIMIT 1), ");
        stringBuilder.append("    (SELECT lqp.pickup_city FROM lcl_quote_pad lqp ");
        stringBuilder.append("    WHERE lqp.file_number_id = f.file_number_id LIMIT 1)) AS pickupCity, ");
        stringBuilder.append("    UserDetailsGetLoginNameByID(f.booked_by) AS bookedBy, ");
        stringBuilder.append("    UserDetailsGetLoginNameByID(f.quote_by) AS quoteBy, ");
        stringBuilder.append("    cas.credit_status AS creditS,");
        stringBuilder.append("    caf.credit_status AS creditF,");
        stringBuilder.append("    cat.`credit_status` AS creditT,");
        stringBuilder.append("	  f.bill_to_party as billToParty,");
        stringBuilder.append("	  COALESCE((SELECT  CONCAT(ul.un_loc_name,'/',gd.code,'(',ul.un_loc_code,')') FROM  lcl_booking_dispo lbdis  ");
        stringBuilder.append("	  LEFT JOIN un_location ul  ON lbdis.un_location_id = ul.id  ");
        stringBuilder.append("	  LEFT JOIN genericcode_dup gd  ON ul.statecode = gd.id ");
        stringBuilder.append("	  WHERE lbdis.file_number_id = f.file_number_id  ORDER BY lbdis.id DESC  LIMIT 1)) AS currentLocName,");

        stringBuilder.append("	  COALESCE((SELECT  CONCAT(ul.un_loc_code) FROM  lcl_booking_dispo lbdis  ");
        stringBuilder.append("	  LEFT JOIN un_location ul  ON lbdis.un_location_id = ul.id  ");
        stringBuilder.append("	  LEFT JOIN genericcode_dup gd  ON ul.statecode = gd.id ");
        stringBuilder.append("	  WHERE lbdis.file_number_id = f.file_number_id  ORDER BY lbdis.id DESC  LIMIT 1)) AS currentLocCode,");
        stringBuilder.append("	  f.created_datetime as createdDatetime,");
        stringBuilder.append("	  clnt.`acct_name` AS clientName,");
        stringBuilder.append("	 (SELECT lb.inbond_no FROM  lcl_inbond lb WHERE lb.`file_number_id` = f.file_number_id ");
        stringBuilder.append("	 ORDER BY lb.`inbond_id` DESC LIMIT 1) AS inbondNo,");

        // stringBuilder.append("   IF(lca.lcl_file_number_id_a IS NULL, lcb.lcl_file_number_id_b, lca.lcl_file_number_id_a )  as consolidatedFiles, ");
        stringBuilder.append(getConsolidateQuery()).append(" AS relatedConoslidted, ");
        stringBuilder.append("	 le.cfcl as cfcl,");
        stringBuilder.append("   le.cfcl_acct_no AS cfclAcct,");
        stringBuilder.append("   f.ssl_name as sslName,");
        stringBuilder.append("   f.agent_acct_no as agentAcct,");

        stringBuilder.append("   ROUND(f.total_piece) as totalPiece,");
        stringBuilder.append("   ROUND(f.total_weight_imperial) as totalWeightImperial,");
        stringBuilder.append("   ROUND(f.total_volume_imperial) as totalVolumeImperial,");
        stringBuilder.append("   ROUND(f.total_volume_metric) as totalVolumeMetric,");
        stringBuilder.append("   ROUND(f.total_weight_metric) as totalWeightMetric,");

        stringBuilder.append("   f.booked_weight as bookedWeight,");
        stringBuilder.append("   f.actual_weight as actualWeight,");
        stringBuilder.append("   f.booked_volume as bookedVolume,");
        stringBuilder.append("   f.actual_volume as actualVolume,");
        stringBuilder.append("   f.booked_weight_metric as bookedWeightMetric,");
        stringBuilder.append("   f.actual_weight_metric as actualWeightMetric,");
        stringBuilder.append("   f.actual_volume_metric as actualVolumeMetric,");
        stringBuilder.append("   f.booked_volume_metric as bookedVolumeMetric,");
        stringBuilder.append("   f.totalBookedPiece as totalBookedPiece,");
        stringBuilder.append("   f.totalActualPiece as totalActualPiece,");

        stringBuilder.append("   f.hazmat as hazmat,");
        stringBuilder.append("   f.hazmatInfo AS hazmatInfo,");
        stringBuilder.append("   DATE_FORMAT(f.`picked_up_datetime`,'%d-%b-%Y') AS pickedUpDateTime,");
        stringBuilder.append("   f.etaDate as etaDate,");
        stringBuilder.append("   f.dispoCode as dispoCode,");
        stringBuilder.append("   f.dispoDesc as dispoDesc,");
        stringBuilder.append("   f.disp as disp,");
        stringBuilder.append("   (select posted_by_user_id from lcl_bl where ");
        stringBuilder.append("   file_number_id = f.consolidateBlId ) as  postedUser,");
        stringBuilder.append("   f.transshipment as transshipment,");
        stringBuilder.append("   f.shortShip as shortShip,");
        stringBuilder.append("   f.shortShipSequence AS shortShipSequence, ");
        stringBuilder.append("   f.pieceUnit as pieceUnit,");
        stringBuilder.append("   f.voyageServiceType AS voyageServiceType,");
        stringBuilder.append("   f.dataSource as dataSource,");
        // hot code fetching method (Hot Code Table Changed so Depend Condition its will work)
        stringBuilder.append("  (SELECT if(f.file_state <> 'Q',(select Group_concat(code SEPARATOR '<br>') from lcl_booking_hot_code where file_number_id = f.`file_number_id`),");
        stringBuilder.append("  (select Group_concat(code SEPARATOR '<br>') from lcl_quote_hot_code where file_number_id = f.`file_number_id`))) AS hotCodes, ");
        stringBuilder.append(" (SELECT IF(COUNT(*)>0,TRUE,FALSE) AS priorityNotes FROM lcl_remarks WHERE file_number_id=f.`file_number_id` AND TYPE IN ('Priority View'))AS priorityNotes, ");
        stringBuilder.append(" (SELECT GROUP_CONCAT(remarks) FROM lcl_remarks WHERE file_number_id=f.`file_number_id` AND TYPE IN ('Priority View'))AS priorityNoteValues, ");
        stringBuilder.append("  (select if(f.file_state <> 'Q',(SELECT IF(INSTR(hot.code, '/'),GROUP_CONCAT( LEFT(  hot.code,  INSTR(hot.code, '/') - 1 )), '') FROM  lcl_booking_hot_code hot ");
        stringBuilder.append("   WHERE hot.file_number_id = f.`file_number_id`),  ");
        stringBuilder.append("  (SELECT IF(INSTR(hot.code, '/'),GROUP_CONCAT( LEFT(  hot.code,  INSTR(hot.code, '/') - 1 )), '') FROM  lcl_quote_hot_code hot ");
        stringBuilder.append("   WHERE hot.file_number_id = f.`file_number_id`))) AS hotCodeKey, ");
        stringBuilder.append("  (select if(f.file_state <> 'Q',(SELECT count(*) FROM  lcl_booking_hot_code hot  WHERE hot.file_number_id = f.`file_number_id`),");
        stringBuilder.append("  (SELECT count(*) FROM  lcl_quote_hot_code hot  WHERE hot.file_number_id = f.`file_number_id` ))) AS hotCodeCount,");

        stringBuilder.append("   (SELECT IF(lm.remarks IS NULL,'',CONCAT(' ");
        stringBuilder.append(REMARKS_LABEL_LOADING_REMARKS);
        stringBuilder.append("   ',lm.remarks)) FROM `lcl_remarks` lm WHERE `file_number_id` = f.`file_number_id`");
        stringBuilder.append("   AND lm.`type` = '");
        stringBuilder.append(REMARKS_TYPE_LOADING_REMARKS).append("'");
        stringBuilder.append("   ORDER BY lm.id DESC LIMIT 1) AS loadingRemarks, ");
        stringBuilder.append("  (SELECT  aes.status  FROM aes_history aes WHERE aes.file_no = f.file_number ORDER BY id DESC LIMIT 1) AS aesStatus, ");
        stringBuilder.append("  (SELECT  COUNT(*) FROM  sed_filings sed  WHERE sed.SHPDR = f.file_number AND sed.STATUS = 'S') AS sedCount, ");
        stringBuilder.append("  f.pieceDetailLocation, ");
        stringBuilder.append("  f.lineLocation,f.hold, ");
        stringBuilder.append("  IF(le.`released_datetime` IS NOT NULL,TRUE,FALSE) AS drReleaseFlag,");
        stringBuilder.append("  IF(f.file_state <> 'Q',(SELECT IF(COUNT(*) > 0 OR f.hold ='Y',TRUE,FALSE) FROM `lcl_booking_hot_code` WHERE file_number_id=f.`file_number_id` AND CODE='XXX/DO NOT RELEASE FREIGHT-HOLD'),(SELECT IF(COUNT(*) > 0,TRUE,FALSE) FROM `lcl_quote_hot_code` WHERE file_number_id=f.`file_number_id` AND CODE='XXX/DO NOT RELEASE FREIGHT-HOLD')) AS shipmentHoldFlag,");
        stringBuilder.append(" (SELECT DATE_FORMAT(bkd.`disposition_datetime`,'%Y-%m-%d')  FROM lcl_booking_dispo bkd JOIN  `disposition` d ON (bkd.`disposition_id`=d.`id` AND d.`elite_code`='RCVD') WHERE  bkd.`file_number_id` =f.`file_number_id` ORDER BY bkd.`id` DESC LIMIT 1) AS cargoRecDate ,");
        stringBuilder.append("  f.ERT as ert,f.OSD as OSD,f.osdRemarks,f.inlandETA as inlandETA ");
        //-------------------------------------------------------------------------------------
        stringBuilder.append("   FROM ( Select   ");
        stringBuilder.append("   f.`file_number_id`, ");
        stringBuilder.append("  f.file_state, ");
        stringBuilder.append("  f.quoteComplete, ");
        stringBuilder.append("  f.file_number, ");
        stringBuilder.append("  f.file_status, f.original_file_status, ");
        stringBuilder.append("  f.client_pwk, ");
        stringBuilder.append("  f.booking_type, ");
        stringBuilder.append("  f.schedule_no, ");
        stringBuilder.append("  f.sailDate, ");
        stringBuilder.append("  f.pol_lrdt, ");
        stringBuilder.append("  f.poo_lrdt, ");
        stringBuilder.append("  f.transPolCode, ");
        stringBuilder.append("	f.transPolName, ");
        stringBuilder.append("	f.transPolState, ");
        stringBuilder.append("  f.transPodCode, ");
        stringBuilder.append("	f.transPodName, ");
        stringBuilder.append("	f.transPodCountry, ");
        stringBuilder.append("	f.poo_id, ");
        stringBuilder.append("	f.pol_id, ");
        stringBuilder.append("	f.pod_id, ");
        stringBuilder.append("	f.fd_id, ");
        stringBuilder.append("	f.relay_override, ");
        stringBuilder.append("	f.ship_acct_no,");
        stringBuilder.append("	f.ship_contact_id, ");
        stringBuilder.append("	f.cons_acct_no, ");
        stringBuilder.append("  f.cons_contact_id, ");
        stringBuilder.append("  f.fwd_acct_no, ");
        stringBuilder.append("  f.fwd_contact_id, ");
        stringBuilder.append("  f.third_acct_no, ");
        stringBuilder.append("  f.`billing_terminal`, ");
        stringBuilder.append("  f.poo_pickup, ");
        stringBuilder.append("  f.booked_by, ");
        stringBuilder.append("  f.quote_by, ");
        stringBuilder.append("  f.bill_to_party, ");
        stringBuilder.append("  f.created_datetime, ");
        stringBuilder.append("  f.client_acct_no, ");
        stringBuilder.append("  f.ssl_name, ");
        stringBuilder.append("  f.agent_acct_no, ");
        stringBuilder.append("  f.total_piece,  ");
        stringBuilder.append("  f.total_weight_imperial,  ");
        stringBuilder.append("	f.total_volume_imperial,  ");
        stringBuilder.append("	f.total_volume_metric,");
        stringBuilder.append("	f.total_weight_metric,");
        stringBuilder.append("  f.booked_weight,");
        stringBuilder.append("  f.actual_weight,");
        stringBuilder.append("  f.booked_volume,");
        stringBuilder.append("  f.actual_volume,");
        stringBuilder.append("  f.booked_weight_metric,");
        stringBuilder.append("  f.actual_weight_metric,");
        stringBuilder.append("  f.actual_volume_metric,");
        stringBuilder.append("  f.booked_volume_metric,");
        stringBuilder.append("  f.totalBookedPiece,");
        stringBuilder.append("  f.totalActualPiece,");
        stringBuilder.append("  f.hazmat, ");
        stringBuilder.append("  f.hazmatInfo, ");
        stringBuilder.append("  f.picked_up_datetime, ");
        stringBuilder.append("  lsd.`sta` AS etaDate, ");
        stringBuilder.append("  (SELECT d.elite_code FROM lcl_booking_dispo  lb JOIN disposition  d ON  d.id = lb.disposition_id WHERE lb.id = MAX(lbd.id))   AS dispoCode, ");
        stringBuilder.append("  (SELECT d.description FROM lcl_booking_dispo lb JOIN disposition  d ON  d.id = lb.disposition_id WHERE lb.id = MAX(lbd.id)) AS dispoDesc, ");
        stringBuilder.append("  dis.description AS disp, ");
        stringBuilder.append("  f.transshipment, ");
        stringBuilder.append("  f.shortShip, ");
        stringBuilder.append("  f.shortShipSequence, ");
        stringBuilder.append("  bpu.`id` AS pieceUnit, ");
        stringBuilder.append("  (SELECT lshh.`service_type` FROM lcl_ss_header lshh  JOIN lcl_unit_ss luss ");
        stringBuilder.append("  ON luss.`ss_header_id`=lshh.id WHERE luss.id=MAX(bpu.lcl_unit_ss_id))  AS voyageServiceType, ");
        stringBuilder.append("  f.dataSource, ");
        stringBuilder.append("  (SELECT upper(GROUP_CONCAT(location)) FROM lcl_booking_piece_whse WHERE  ");
        stringBuilder.append("  booking_piece_id = f.booking_piece_id ORDER BY id DESC) AS lineLocation, ");
        stringBuilder.append("  (SELECT upper(GROUP_CONCAT(stowed_location)) FROM lcl_booking_piece_detail WHERE  ");
        stringBuilder.append("  booking_piece_id = f.booking_piece_id and stowed_location <> '' ORDER BY id DESC) AS pieceDetailLocation, ");
        stringBuilder.append("  f.consolidateBlId as consolidateBlId,f.hold,f.ERT, ");
        stringBuilder.append("  f.OSD,f.osdRemarks,f.inlandETA ");

        // ----------------------------------------------------------------------
        stringBuilder.append("FROM (SELECT");
        stringBuilder.append("   f.`file_number_id`,");
        stringBuilder.append("   bp.booking_piece_id AS booking_piece_id,");
        stringBuilder.append("   f.file_state,");
        stringBuilder.append("   f.quoteComplete,");
        stringBuilder.append("   f.file_number,");
        stringBuilder.append("   ls.`schedule_no` AS schedule_no,");
        stringBuilder.append("   (select lsd.`std` from lcl_ss_detail lsd where lsd.ss_header_id = ls.id ");
        stringBuilder.append("   and ls.trans_mode ='V' ORDER BY id DESC LIMIT 1) AS sailDate, ");
        stringBuilder.append("   f.file_status, f.original_file_status, ");
        stringBuilder.append("   f.client_pwk,");
        stringBuilder.append("   f.booking_type,");
        stringBuilder.append("	   '' AS pol_lrdt,");
        stringBuilder.append("	   (SELECT b.`poo_whse_lrdt` FROM lcl_booking b WHERE b.`file_number_id` = f.file_number_id) AS poo_lrdt,");
        stringBuilder.append("	   f.transPolCode,");
        stringBuilder.append("	   f.transPolName,");
        stringBuilder.append("	   f.transPolState,");
        stringBuilder.append("	   f.transPodCode,");
        stringBuilder.append("	   f.transPodName,");
        stringBuilder.append("	   f.transPodCountry,");
        stringBuilder.append("	   f.poo_id,");
        stringBuilder.append("	   f.pol_id,");
        stringBuilder.append("	   f.pod_id,");
        stringBuilder.append("	   f.fd_id,");
        stringBuilder.append("	   f.relay_override,");
        stringBuilder.append("	   f.ship_acct_no,");
        stringBuilder.append("	   f.ship_contact_id,	");
        stringBuilder.append("   f.cons_acct_no,");
        stringBuilder.append("   f.cons_contact_id,");
        stringBuilder.append("   f.fwd_acct_no,");
        stringBuilder.append("   f.fwd_contact_id,");
        stringBuilder.append("   f.`billing_terminal`,");
        stringBuilder.append("   f.poo_pickup,");

        stringBuilder.append("   f.booked_by,");
        stringBuilder.append("   f.quote_by,");
        stringBuilder.append("   f.third_acct_no,");
        stringBuilder.append("   f.bill_to_party,");
        stringBuilder.append("   f.created_datetime,");
        stringBuilder.append("   f.client_acct_no,");
        stringBuilder.append("   '' AS ssl_name,");
        stringBuilder.append("	   f.agent_acct_no,");

        stringBuilder.append("	   IF(lf.`state` <> 'Q',bp.total_piece,qp.total_piece) AS total_piece,  ");
        stringBuilder.append("	   IF(lf.`state` <> 'Q',bp.total_weight_imperial,qp.total_weight_imperial) AS total_weight_imperial,  ");
        stringBuilder.append("	   IF(lf.`state` <> 'Q',bp.total_volume_imperial,qp.total_volume_imperial) AS total_volume_imperial,  ");
        stringBuilder.append("	   IF(lf.`state` <> 'Q',bp.total_volume_metric,qp.total_volume_metric) AS total_volume_metric,  ");
        stringBuilder.append("	   IF(lf.`state` <> 'Q',bp.total_weight_metric,qp.total_weight_metric) AS total_weight_metric,  ");

        stringBuilder.append("	  IF(lf.`state` <> 'Q',bp.booked_weight,qp.booked_weight) AS booked_weight, ");
        stringBuilder.append("   IF(lf.`state` <> 'Q',bp.actual_weight,qp.actual_weight) AS actual_weight, ");
        stringBuilder.append("   IF(lf.`state` <> 'Q',bp.booked_volume,qp.booked_volume) AS booked_volume, ");
        stringBuilder.append("   IF(lf.`state` <> 'Q',bp.actual_volume,qp.actual_volume) AS actual_volume, ");
        stringBuilder.append("   IF(lf.`state` <> 'Q',bp.booked_weight_metric,qp.booked_weight_metric) AS booked_weight_metric, ");
        stringBuilder.append("   IF(lf.`state` <> 'Q',bp.actual_weight_metric,qp.actual_weight_metric) AS actual_weight_metric, ");
        stringBuilder.append("   IF(lf.`state` <> 'Q',bp.actual_volume_metric,qp.actual_volume_metric) AS actual_volume_metric, ");
        stringBuilder.append("   IF(lf.`state` <> 'Q',bp.booked_volume_metric,qp.booked_volume_metric) AS booked_volume_metric, ");
        stringBuilder.append("   IF(lf.`state` <> 'Q',bp.totalBookedPiece,qp.totalBookedPiece) AS totalBookedPiece, ");
        stringBuilder.append("   IF(lf.`state` <> 'Q',bp.totalActualPiece,qp.totalActualPiece) AS totalActualPiece, ");
        stringBuilder.append("  IF(lf.`state` <> 'Q',bp.hazmat,qp.hazmat) AS hazmat, ");
        stringBuilder.append("  IF(lf.`state` <> 'Q',bp.hazmatInfo,qp.hazmatInfo) AS hazmatInfo, ");
        stringBuilder.append("   f.picked_up_datetime,");
        stringBuilder.append("   f.transshipment,");
        stringBuilder.append("   f.shortShip,");
        stringBuilder.append("  f.shortShipSequence, ");
        stringBuilder.append("   f.dataSource , f.consolidateBlId as consolidateBlId,f.hold,f.ERT, ");
        stringBuilder.append("   f.OSD,f.osdRemarks,f.inlandETA ");

        //--------------------------------------------------------------------------------------------
        stringBuilder.append("    FROM (SELECT  ");
        stringBuilder.append("	   f.`id` AS file_number_id, ");
        stringBuilder.append("	   f.`state` AS file_state, ");
        stringBuilder.append("    qt.`quote_complete` AS quoteComplete,");
        stringBuilder.append("	   f.`file_number` AS file_number, ");
        stringBuilder.append("     CASE WHEN f.status = 'M' THEN f.previous_status  ");
        stringBuilder.append("     WHEN (f.status = 'L' or f.status='X') THEN f.status  WHEN lbe.released_datetime IS NOT NULL  THEN 'R' ");
        stringBuilder.append("     WHEN lbe.prerelease_datetime IS NOT NULL THEN 'PR'  ELSE f.`status` END AS  file_status, ");
        stringBuilder.append("     f.status as original_file_status, ");
        stringBuilder.append("	   COALESCE(bk.`booking_type`,  IF(f.`state` = 'Q', qt.`quote_type`, NULL )) AS booking_type, ");
        stringBuilder.append("	   COALESCE(bk.`client_pwk_recvd`,  IF(f.`state` = 'Q', qt.`client_pwk_recvd`, NULL )) AS client_pwk, ");
        stringBuilder.append("	   filter.entered_datetime, ");
        stringBuilder.append(" 	   COALESCE(bk.`booked_ss_header_id`,  IF(f.`state` = 'Q', null, NULL )) AS schedule_id,	 ");
        if (CommonUtils.isNotEmpty(lclSearchForm.getBlPoolOwner()) || ("BP".equalsIgnoreCase(lclSearchForm.getFilterBy()))) {
            stringBuilder.append("    IF(lbl.`ship_acct_no` <> '',lbl.`ship_acct_no`,NULL) AS ship_acct_no,  ");
            stringBuilder.append("    IF(lbl.`ship_contact_id` <> '',lbl.`ship_contact_id`,NULL) AS ship_contact_id,  ");
            stringBuilder.append("    IF(lbl.cons_acct_no <> '',lbl.`cons_acct_no`, NULL) AS cons_acct_no, ");
            stringBuilder.append("   IF(lbl.`cons_contact_id` <> '' ,lbl.`cons_contact_id`,NULL) AS cons_contact_id, ");
            stringBuilder.append("   IF(lbl.`fwd_acct_no` <> '' ,lbl.`fwd_acct_no`,NULL ) AS fwd_acct_no, ");
            stringBuilder.append("   IF(lbl.`fwd_contact_id` <> '',lbl.`fwd_contact_id`, NULL) AS fwd_contact_id, ");
        } else {
            stringBuilder.append("	   COALESCE(bk.`ship_acct_no`,  IF(f.`state` = 'Q', qt.`ship_acct_no`, NULL )) AS ship_acct_no, ");
            stringBuilder.append("	   COALESCE(bk.`ship_contact_id`,IF(f.`state` = 'Q', qt.`ship_contact_id`, NULL)) AS ship_contact_id, ");
            stringBuilder.append("	   COALESCE(bk.cons_acct_no,IF(f.`state` = 'Q', qt.`cons_acct_no`, NULL)) AS cons_acct_no, ");
            stringBuilder.append("	   COALESCE(bk.`cons_contact_id`,IF(f.`state` = 'Q', qt.`cons_contact_id`, NULL)) AS cons_contact_id, ");
            stringBuilder.append("	   COALESCE(bk.`fwd_acct_no`,IF(f.`state` = 'Q', qt.`fwd_acct_no`, NULL)) AS fwd_acct_no, ");
            stringBuilder.append("	   COALESCE(bk.`fwd_contact_id`,IF(f.`state` = 'Q', qt.`fwd_contact_id`, NULL)) AS fwd_contact_id, ");
        }
        stringBuilder.append("	   COALESCE(bk.`third_party_acct_no`,  IF(f.`state` = 'Q', qt.`third_party_acct_no`, NULL )) AS third_acct_no, ");
        stringBuilder.append("	   COALESCE(bk.`client_acct_no`,  IF(f.`state` = 'Q', qt.`client_acct_no`, NULL )) AS client_acct_no, ");
        stringBuilder.append("	   COALESCE(bk.`agent_acct_no`,  IF(f.`state` = 'Q', qt.`agent_acct_no`, NULL )) AS agent_acct_no, ");
        stringBuilder.append("	   transpol.un_loc_code AS transPolCode, ");
        stringBuilder.append("     transpol.un_loc_name AS transPolName, ");
        stringBuilder.append("     transpols.code AS transPolState, ");
        stringBuilder.append("     transpod.un_loc_code AS transPodCode, ");
        stringBuilder.append("     transpod.un_loc_name AS transPodName, ");
        stringBuilder.append("     transpodc.codedesc AS transPodCountry, ");
        stringBuilder.append("	   COALESCE( bk.`poo_id`, IF(f.`state` = 'Q', qt.`poo_id`, NULL)) AS poo_id, ");
        stringBuilder.append("     COALESCE( bk.`pol_id`,IF(f.`state` = 'Q', qt.`pol_id`, NULL)) AS pol_id, ");
        stringBuilder.append("     COALESCE( bk.`pod_id`,IF(f.`state` = 'Q', qt.`pod_id`, NULL)) AS pod_id, ");
        stringBuilder.append("     COALESCE( bk.`fd_id`,IF(f.`state` = 'Q', qt.`fd_id`, NULL)) AS fd_id, ");
        stringBuilder.append("     COALESCE( bk.`relay_override`,IF(f.`state` = 'Q', qt.`relay_override`,NULL)) AS relay_override, ");
        stringBuilder.append("     COALESCE( bk.`poo_pickup`,IF(f.`state` = 'Q',qt.`poo_door`,NULL)) AS poo_pickup, ");
        stringBuilder.append("     COALESCE( bk.`billing_terminal`,IF(f.`state` = 'Q', qt.`billing_terminal`,NULL)) AS billing_terminal, ");
        stringBuilder.append("     bk.`entered_by_user_id` AS booked_by, ");
        stringBuilder.append("	   qt.`entered_by_user_id` AS quote_by, ");
        stringBuilder.append("     COALESCE(bk.`bill_to_party`, IF(f.`state` = 'Q', qt.`bill_to_party`, NULL )) AS bill_to_party, ");
        stringBuilder.append("     COALESCE( bki.`picked_up_datetime`,IF(f.`state` = 'Q',qti.`picked_up_datetime`,NULL)) AS picked_up_datetime, ");
        stringBuilder.append("     f.created_datetime AS created_datetime, ");
        stringBuilder.append("     COALESCE(bki.`transshipment` AND bk.`booking_type` = 'T',0) AS transshipment, ");
        stringBuilder.append("     f.short_ship AS shortShip, ");
        stringBuilder.append("     f.short_ship_sequence AS shortShipSequence, ");
        stringBuilder.append("     f.`datasource` AS dataSource, getHouseBLForConsolidateDr (f.id) AS consolidateBlId,bk.hold as hold,   ");
        stringBuilder.append("     IF(f.`state` = 'Q', qt.rtd_transaction, bk.rtd_transaction) AS ERT,  ");
        stringBuilder.append("     IF(f.`state` = 'Q', qt.over_short_damaged, bk.over_short_damaged) AS OSD,  ");
        stringBuilder.append("     (select remarks from lcl_remarks where file_number_id = f.id and type ='OSD' limit 1) AS osdRemarks,  ");
        stringBuilder.append(getInlandDateQuery());
        // ALL MAIN CONDITIONS ARE SCROLLING HERE
        stringBuilder.append(" FROM ( ").append(filterAppendQuery(lclSearchForm)).append(") as filter ");

        // ---------------------------- join for origin and destination ----------------------------------
        stringBuilder.append("  JOIN `lcl_file_number` f ON (filter.`id` = f.`id`) ");
        stringBuilder.append("  LEFT JOIN `lcl_booking` bk ON (f.`id` = bk.`file_number_id` AND bk.`booking_type` IN ('E', 'T')) ");
        stringBuilder.append("  LEFT JOIN lcl_booking_export lbe  ON  lbe.`file_number_id` = f.id  ");
        stringBuilder.append("	LEFT JOIN `lcl_booking_import` bki ON (f.`id` = bki.`file_number_id`) ");
        stringBuilder.append("	LEFT JOIN  un_location transpol ON transpol.`id` = bki.`usa_port_of_exit_id`");
         if(CommonUtils.isNotEmpty(lclSearchForm.getBlPoolOwner()) || ("BP".equalsIgnoreCase(lclSearchForm.getFilterBy()))){
         stringBuilder.append("  LEFT JOIN `lcl_bl` lbl  ON lbl.file_number_id= f.id  ");
        }
        stringBuilder.append("	LEFT JOIN  genericcode_dup transpols ON transpols.id= transpol.`statecode`");
        stringBuilder.append("	LEFT JOIN un_location transpod  ON transpod.`id` = bki.foreign_port_of_discharge_id ");
        stringBuilder.append("	LEFT JOIN genericcode_dup transpods   ON transpod.statecode = transpods.id ");
        stringBuilder.append("  LEFT JOIN genericcode_dup transpodc   ON transpod.countrycode = transpodc.id 	");
        stringBuilder.append("  LEFT JOIN `lcl_quote` qt ON (f.`id` = qt.`file_number_id`  AND qt.`quote_type` IN ('E')) ");
        stringBuilder.append("	LEFT JOIN `lcl_quote_import` qti ON (f.`id` = qti.`file_number_id`) 	");
        stringBuilder.append("  WHERE (bk.`file_number_id` IS NOT NULL  OR qt.`file_number_id` IS NOT NULL " );
        if(CommonUtils.isNotEmpty(lclSearchForm.getBlPoolOwner()) || ("BP".equalsIgnoreCase(lclSearchForm.getFilterBy()))){
           stringBuilder.append(" OR lbl.`file_number_id` IS NOT NULL   ");
        }
        stringBuilder.append("  ) GROUP BY f.`id`)AS f");

        // ---------------------------- join for commondity pieces ----------------------------------
        stringBuilder.append("	JOIN lcl_file_number lf ON (lf.id = f.file_number_id)  ");
        stringBuilder.append("	LEFT JOIN lcl_ss_header ls ON (ls.`id` = f.schedule_id)  ");
        // stringBuilder.append("  LEFT JOIN `trading_partner` ssref ON ls.sp_acct_no = ssref.`acct_no`");       
        stringBuilder.append("  LEFT JOIN ").append(queryForBookingPieceDetais(lclSearchForm));
        // stringBuilder.append("  LEFT JOIN `lcl_booking_hazmat` bh ON(bp.`id`=bh.`booking_piece_id`)");
        stringBuilder.append("  LEFT JOIN ").append(queryForQuotePieceDetais(lclSearchForm));
        //  stringBuilder.append("  LEFT JOIN lcl_quote_hazmat qh ON (qp.`id`=qh.`quote_piece_id`)");
        stringBuilder.append("  ) AS f     ");

        // ---------------------------- join for voyage and units ----------------------------------
        stringBuilder.append("  LEFT JOIN lcl_booking_dispo lbd ON lbd.`file_number_id` = f.file_number_id   ");
        stringBuilder.append("  LEFT JOIN `lcl_booking_piece_unit` bpu ON (f.`booking_piece_id` = bpu.`booking_piece_id`) ");
        stringBuilder.append("  LEFT JOIN `lcl_unit_ss` luss ON (bpu.`lcl_unit_ss_id` = luss.`id`) ");
        stringBuilder.append("  LEFT JOIN lcl_unit lu ON (lu.`id` = luss.`unit_id`)");
        stringBuilder.append("  LEFT  JOIN lcl_unit_ss_manifest lusm  ON (lusm.`unit_id` = lu.`id`)");
        stringBuilder.append("  LEFT JOIN lcl_ss_detail lsd ON (luss.`ss_header_id` = lsd.`ss_header_id`) ");
        stringBuilder.append("  LEFT JOIN `lcl_unit_ss_dispo` lusd ON (lsd.`id` = lusd.`ss_detail_id` AND lu.`id` = lusd.`unit_id`)");
        stringBuilder.append("  LEFT JOIN lcl_ss_header lsh ON (lsh.`id` = luss.`ss_header_id`)");
        stringBuilder.append("  LEFT JOIN disposition dispo  ON (dispo.`id` = lusd.`disposition_id`)");
        stringBuilder.append("  LEFT JOIN disposition dis ON (lbd.`disposition_id` = dis.`id`)");
        stringBuilder.append("  GROUP BY f.`file_number_id`)AS f");

        // ---------------------------- join for customer as  ship,cons, fwd ----------------------------------
        stringBuilder.append("  LEFT JOIN `trading_partner` ship ON (f.`ship_acct_no` = ship.`acct_no`) ");
        stringBuilder.append("  LEFT JOIN `lcl_contact` shipc ON (f.`ship_contact_id` = shipc.`id`) ");
        stringBuilder.append("  LEFT JOIN `trading_partner` cons ON f.`cons_acct_no` = cons.`acct_no` ");
        stringBuilder.append("  LEFT JOIN `lcl_contact` consc ON ( f.`cons_contact_id` = consc.`id`) ");
        stringBuilder.append("  LEFT JOIN `trading_partner` fwd ON f.`fwd_acct_no` = fwd.`acct_no` ");
        stringBuilder.append("  LEFT JOIN `lcl_contact` fwdc ON ( f.`fwd_contact_id` = fwdc.`id`) ");
        stringBuilder.append("  LEFT JOIN `trading_partner` thirdParty ON thirdParty.`acct_no` = f.third_acct_no");
        stringBuilder.append("  LEFT JOIN `trading_partner` clnt ON clnt.`acct_no` = f.client_acct_no");
        stringBuilder.append("  LEFT JOIN cust_accounting cas ON (ship.`acct_no` = cas.`acct_no`) ");
        stringBuilder.append("  LEFT JOIN cust_accounting caf ON (fwd.`acct_no` = caf.`acct_no`) ");
        stringBuilder.append("  LEFT JOIN cust_accounting cat ON (thirdParty.`acct_no` = cat.`acct_no`)	");
        stringBuilder.append("  LEFT JOIN lcl_booking_export le ON (le.`file_number_id` = f.`file_number_id`)");

        // --------------------------join for conoslidates files -------------------------------------
        // stringBuilder.append("  LEFT JOIN lcl_consolidation lca  ON lca.`lcl_file_number_id_a` = f.file_number_id");
        //  stringBuilder.append("  LEFT JOIN lcl_consolidation lcb ON lcb.`lcl_file_number_id_b` = f.file_number_id");
        stringBuilder.append("  GROUP BY f.`file_number_id` ");
        if (CommonUtils.in(lclSearchForm.getOrderBy(), "BDesc", "BAsc", "B", "Des", "Cons", "Fwd", "Ship")) {
            stringBuilder.append(getOrdered(lclSearchForm));
        } else {
            stringBuilder.append(getSort(lclSearchForm));
        }
        return stringBuilder.toString();
    }

    private StringBuilder getSort(LclSearchForm lclSearchForm) {
        StringBuilder queryBuilder = new StringBuilder();
        if ("status".equalsIgnoreCase(lclSearchForm.getSortByValue())) {
            if ("up".equals(lclSearchForm.getSearchType())) {
                queryBuilder.append(" order by f.`file_state` asc, f.`file_status` asc");
            } else {
                queryBuilder.append(" order by f.`file_state` desc, f.`file_status` desc");
            }
        } else if (CommonUtils.isNotEmpty(lclSearchForm.getSortByValue())) {
            if ("up".equals(lclSearchForm.getSearchType())) {
                queryBuilder.append(" order by ").append(lclSearchForm.getSortByValue()).append(" asc");
            } else {
                queryBuilder.append(" order by ").append(lclSearchForm.getSortByValue()).append(" desc");
            }
        } else {
            queryBuilder.append(" order by f.`file_number_id` desc");
        }
        return queryBuilder;
    }

    public List<ExportSearchBean> search(LclSearchForm lclSearchForm) throws Exception {
        SQLQuery query = getSession().createSQLQuery(mainAccessQuery(lclSearchForm));
        query = setQueryValue(query, lclSearchForm);
        query.setResultTransformer(Transformers.aliasToBean(ExportSearchBean.class));
        query.addScalar("fileNumberId", IntegerType.INSTANCE);
        query.addScalar("fileState", StringType.INSTANCE);
        query.addScalar("quoteComplete", StringType.INSTANCE);
        query.addScalar("fileNumber", StringType.INSTANCE);
        query.addScalar("fileStatus", StringType.INSTANCE);
        query.addScalar("originalFileStatus", StringType.INSTANCE);
        query.addScalar("currentFileStatus", StringType.INSTANCE);
        query.addScalar("clientPwk", IntegerType.INSTANCE);
        query.addScalar("bookingType", StringType.INSTANCE);
        query.addScalar("scheduleNo", StringType.INSTANCE);
        query.addScalar("sailDate", StringType.INSTANCE);
        query.addScalar("polLrd", StringType.INSTANCE);
        query.addScalar("pooLrd", StringType.INSTANCE);
        query.addScalar("transPolCode", StringType.INSTANCE);
        query.addScalar("transPolName", StringType.INSTANCE);
        query.addScalar("transPolState", StringType.INSTANCE);
        query.addScalar("transPodCode", StringType.INSTANCE);
        query.addScalar("transPodName", StringType.INSTANCE);
        query.addScalar("transPodCountry", StringType.INSTANCE);
        query.addScalar("originUncode", StringType.INSTANCE);
        query.addScalar("origin", StringType.INSTANCE);
        query.addScalar("polUncode", StringType.INSTANCE);
        query.addScalar("pol", StringType.INSTANCE);
        query.addScalar("podUncode", StringType.INSTANCE);
        query.addScalar("pod", StringType.INSTANCE);
        query.addScalar("destinationUncode", StringType.INSTANCE);
        query.addScalar("destination", StringType.INSTANCE);
        query.addScalar("relayOverride", StringType.INSTANCE);

        query.addScalar("shipName", StringType.INSTANCE);
        query.addScalar("shipNo", StringType.INSTANCE);
        query.addScalar("shipAddress", StringType.INSTANCE);
        query.addScalar("shipCity", StringType.INSTANCE);
        query.addScalar("shipState", StringType.INSTANCE);
        query.addScalar("shipZip", StringType.INSTANCE);

        query.addScalar("consName", StringType.INSTANCE);
        query.addScalar("consNo", StringType.INSTANCE);
        query.addScalar("consAddress", StringType.INSTANCE);
        query.addScalar("consCity", StringType.INSTANCE);
        query.addScalar("consState", StringType.INSTANCE);
        query.addScalar("consZip", StringType.INSTANCE);

        query.addScalar("fwdName", StringType.INSTANCE);
        query.addScalar("fwdNo", StringType.INSTANCE);
        query.addScalar("fwdAddress", StringType.INSTANCE);
        query.addScalar("fwdCity", StringType.INSTANCE);
        query.addScalar("fwdState", StringType.INSTANCE);
        query.addScalar("fwdZip", StringType.INSTANCE);

        query.addScalar("billingTerminal", StringType.INSTANCE);
        query.addScalar("pooPickup", StringType.INSTANCE);
        query.addScalar("pickupCity", StringType.INSTANCE);
        query.addScalar("bookedBy", StringType.INSTANCE);
        query.addScalar("quoteBy", StringType.INSTANCE);
        query.addScalar("creditS", StringType.INSTANCE);
        query.addScalar("creditF", StringType.INSTANCE);
        query.addScalar("creditT", StringType.INSTANCE);
        query.addScalar("billToParty", StringType.INSTANCE);
        query.addScalar("currentLocCode", StringType.INSTANCE);
        query.addScalar("currentLocName", StringType.INSTANCE);

        query.addScalar("createdDatetime", StringType.INSTANCE);
        query.addScalar("clientName", StringType.INSTANCE);
        query.addScalar("inbondNo", StringType.INSTANCE);
        // query.addScalar("consolidatedFiles", IntegerType.INSTANCE);
        query.addScalar("relatedConoslidted", StringType.INSTANCE);
        query.addScalar("cfcl", StringType.INSTANCE);
        query.addScalar("hold", StringType.INSTANCE);
        if (CommonUtils.isEmpty(lclSearchForm.getFileNumber())) {
            query.addScalar("cfclAcct", StringType.INSTANCE);
        }
        query.addScalar("sslName", StringType.INSTANCE);
        query.addScalar("agentAcct", StringType.INSTANCE);
        query.addScalar("totalPiece", StringType.INSTANCE);
        query.addScalar("totalWeightImperial", StringType.INSTANCE);
        query.addScalar("totalVolumeImperial", StringType.INSTANCE);
        query.addScalar("totalVolumeMetric", StringType.INSTANCE);
        query.addScalar("totalWeightMetric", StringType.INSTANCE);

        query.addScalar("bookedWeight", StringType.INSTANCE);
        query.addScalar("actualWeight", StringType.INSTANCE);
        query.addScalar("bookedVolume", StringType.INSTANCE);
        query.addScalar("actualVolume", StringType.INSTANCE);
        query.addScalar("bookedWeightMetric", StringType.INSTANCE);
        query.addScalar("actualWeightMetric", StringType.INSTANCE);
        query.addScalar("actualVolumeMetric", StringType.INSTANCE);
        query.addScalar("bookedVolumeMetric", StringType.INSTANCE);
        query.addScalar("totalBookedPiece", StringType.INSTANCE);
        query.addScalar("totalActualPiece", StringType.INSTANCE);
        query.addScalar("hazmat", BooleanType.INSTANCE);
        query.addScalar("hazmatInfo", StringType.INSTANCE);
        query.addScalar("pickedUpDateTime", StringType.INSTANCE);
        query.addScalar("etaDate", StringType.INSTANCE);
        query.addScalar("dispoCode", StringType.INSTANCE);
        query.addScalar("dispoDesc", StringType.INSTANCE);
        query.addScalar("disp", StringType.INSTANCE);
        query.addScalar("postedUser", StringType.INSTANCE);
        query.addScalar("transshipment", IntegerType.INSTANCE);
        query.addScalar("shortShip", IntegerType.INSTANCE);
        query.addScalar("shortShipSequence", IntegerType.INSTANCE);
        query.addScalar("voyageServiceType", StringType.INSTANCE);
        query.addScalar("pieceUnit", StringType.INSTANCE);
        query.addScalar("dataSource", StringType.INSTANCE);
        query.addScalar("hotCodes", StringType.INSTANCE);
        query.addScalar("hotCodeKey", StringType.INSTANCE);
        query.addScalar("hotCodeCount", StringType.INSTANCE);
        query.addScalar("loadingRemarks", StringType.INSTANCE);
        query.addScalar("aesStatus", StringType.INSTANCE);
        query.addScalar("sedCount", IntegerType.INSTANCE);
        query.addScalar("correctionIds", StringType.INSTANCE);
        query.addScalar("pieceDetailLocation", StringType.INSTANCE);
        query.addScalar("lineLocation", StringType.INSTANCE);
        query.addScalar("drReleaseFlag", BooleanType.INSTANCE);
        query.addScalar("shipmentHoldFlag", BooleanType.INSTANCE);
        query.addScalar("cargoRecDate", StringType.INSTANCE);
        query.addScalar("priorityNotes", BooleanType.INSTANCE);
        query.addScalar("priorityNoteValues", StringType.INSTANCE);
        query.addScalar("ert", StringType.INSTANCE);
        query.addScalar("OSD", StringType.INSTANCE);
        query.addScalar("inlandETA", StringType.INSTANCE);
        query.addScalar("osdRemarks", StringType.INSTANCE);
        return query.list();
    }

    private StringBuilder getOrdered(LclSearchForm lclSearchForm) {
        StringBuilder queryBuilder = new StringBuilder();
        if (CommonUtils.isNotEmpty(lclSearchForm.getSortByValue())) {
            queryBuilder.append(" order by ").append(lclSearchForm.getSortByValue());
            if ("up".equals(lclSearchForm.getSearchType())) {
                queryBuilder.append(" ASC ");
            } else {
                queryBuilder.append(" DESC ");
            }
        } else if ("BDesc".equalsIgnoreCase(lclSearchForm.getOrderBy())) {
            queryBuilder.append(" order by ").append("f.`file_number` desc");
        } else if ("BAsc".equalsIgnoreCase(lclSearchForm.getOrderBy())) {
            queryBuilder.append(" order by ").append("f.`file_number` asc");
        } else if ("B".equalsIgnoreCase(lclSearchForm.getOrderBy())) {
            queryBuilder.append(" order by ").append("FileNoOriginUncode,f.`file_number`");
        } else if ("Des".equalsIgnoreCase(lclSearchForm.getOrderBy())) {
            queryBuilder.append(" order by ").append("destinationUncode,FileNoOriginUncode,f.`file_number`,f.`billing_terminal`");
        } else if ("Cons".equalsIgnoreCase(lclSearchForm.getOrderBy())) {
            queryBuilder.append(" order by ").append("consName,FileNoOriginUncode,f.`file_number`,f.`billing_terminal`");
        } else if ("Ship".equalsIgnoreCase(lclSearchForm.getOrderBy())) {
            queryBuilder.append(" order by ").append("shipName,FileNoOriginUncode,f.`file_number`,f.`billing_terminal`");
        } else if ("Fwd".equalsIgnoreCase(lclSearchForm.getOrderBy())) {
            queryBuilder.append(" order by ").append("fwdName,FileNoOriginUncode,f.`file_number`,f.`billing_terminal`");
        }
        return queryBuilder;
    }

    public SQLQuery setQueryValue(SQLQuery query, LclSearchForm lclSearchForm) {
        if (CommonUtils.isNotEmpty(lclSearchForm.getCfclAccount()) && CommonUtils.isEmpty(lclSearchForm.getFileNumber())) {
            query.setString("cfclAcct", lclSearchForm.getCfclAccount() + "%");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getFileNumber())) {
            query.setString("fileNumber", lclSearchForm.getFileNumber());
        } else if (CommonUtils.isNotEmpty(lclSearchForm.getContainerNo())) {
            query.setString("unitNo", lclSearchForm.getContainerNo() + "%");
        } else if (CommonUtils.isNotEmpty(lclSearchForm.getSslineNo())) {
            query.setString("sslineNo", lclSearchForm.getSslineNo() + "%");
        } else if (CommonUtils.isNotEmpty(lclSearchForm.getMasterBl())) {
            query.setString("masterBl", lclSearchForm.getMasterBl() + "%");
        } else if (CommonUtils.isNotEmpty(lclSearchForm.getInbondNo())) {
            query.setString("bkInbondNo", lclSearchForm.getInbondNo() + "%");
            query.setString("qtInbondNo", lclSearchForm.getInbondNo() + "%");
        } else if (CommonUtils.isNotEmpty(lclSearchForm.getWarehouseDocNo())) {
            query.setString("bkWarehouseNo", lclSearchForm.getWarehouseDocNo() + "%");
            query.setString("qtWarehouseNo", lclSearchForm.getWarehouseDocNo() + "%");
        } else if (CommonUtils.isNotEmpty(lclSearchForm.getCustomerPo())) {
            query.setString("bkCustomerPo", lclSearchForm.getCustomerPo() + "%");
            query.setString("qtCustomerPo", lclSearchForm.getCustomerPo() + "%");
        } else if (CommonUtils.isNotEmpty(lclSearchForm.getTrackingNo())) {
            query.setString("bkTrackingNo", lclSearchForm.getTrackingNo() + "%");
            query.setString("qtTrackingNo", lclSearchForm.getTrackingNo() + "%");
        } else if (CommonUtils.isNotEmpty(lclSearchForm.getSubHouse())) {
            query.setString("bkSubHouseBl", lclSearchForm.getSubHouse() + "%");
            query.setString("qtSubHouseBl", lclSearchForm.getSubHouse() + "%");
        } else if (CommonUtils.isNotEmpty(lclSearchForm.getSslBookingNo())) {
            query.setString("scheduleNo", lclSearchForm.getSslBookingNo());
        } else if (CommonUtils.isNotEmpty(lclSearchForm.getConoslidateFiles())) {
            String[] file_array = lclSearchForm.getConoslidateFiles().split(",");
            query.setParameterList("conslalidateFiles", Arrays.asList(file_array));
        }  else {

            if (CommonUtils.isNotEmpty(lclSearchForm.getCurrentLocCode())
                    && CommonUtils.isNotEmpty(lclSearchForm.getCurrentLocName())
                    && CommonUtils.isNotEmpty(lclSearchForm.getCurrentLocation())
                    && !"Q".equalsIgnoreCase(lclSearchForm.getFilterBy())
                    && !"X".equalsIgnoreCase(lclSearchForm.getFilterBy())
                    && !"RF".equalsIgnoreCase(lclSearchForm.getFilterBy())
                    && !"BNR".equalsIgnoreCase(lclSearchForm.getFilterBy())) {
                query.setString("curLocCode", lclSearchForm.getCurrentLocCode());
            }
            if (CommonUtils.in(lclSearchForm.getFilterBy(), "ALL", "X", "RF", "IWB", "B", "BP", "BL", "ONBK", "IPO", "UND", "BNR")) {
                if (CommonUtils.isNotEmpty(lclSearchForm.getClientNo())) {
                    query.setString("bkClientNo", lclSearchForm.getClientNo());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getShipperNo())) {
                    query.setString("bkShipperNo", lclSearchForm.getShipperNo());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getConsigneeNo())) {
                    query.setString("bkConsigneeNo", lclSearchForm.getConsigneeNo());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getForwarderNo())) {
                    query.setString("bkFwdNo", lclSearchForm.getForwarderNo());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getForeignAgentAccount())) {
                    query.setString("bkForeignAgentNo", lclSearchForm.getForeignAgentAccount());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getPortName())) {
                    List bkOrigin = Arrays.asList(lclSearchForm.getPortName().split(","));
                    query.setParameterList("bkOrigin", bkOrigin);
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getPolName())) {
                    List pol = Arrays.asList(lclSearchForm.getPolName().split(","));
                    query.setParameterList("bkPol", pol);
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getPodCountryCode())) {
                    query.setString("bkPod", lclSearchForm.getPodCountryCode());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getDestCountryCode())) {
                    query.setString("bkDestination", lclSearchForm.getDestCountryCode());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getOriginRegion())) {
                    query.setString("bkOriginRegion", lclSearchForm.getOriginRegion());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getDestinationRegion())) {
                    query.setString("bkDestinationRegion", lclSearchForm.getDestinationRegion());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getIssuingTerminal())) {
                    query.setString("bkIssuingTerminal", lclSearchForm.getIssuingTerminal());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getBookedBy())) {
                    query.setString("bookedBy", lclSearchForm.getBookedBy());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getCreatedBy())) {
                    query.setString("bkQuoteBy", lclSearchForm.getCreatedBy());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getBlPoolOwner())) {
                    query.setString("blBy", lclSearchForm.getBlPoolOwner());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getBookedForVoyage())) {
                    query.setString("bkgVoyage", lclSearchForm.getBookedForVoyage());
                }
            }
            if ("Q".equalsIgnoreCase(lclSearchForm.getFilterBy())
                    || (CommonUtils.in(lclSearchForm.getFilterBy(), "ALL", "X", "UND") && CommonUtils.isEmpty(lclSearchForm.getBlPoolOwner())
                    && CommonUtils.isEmpty(lclSearchForm.getBookedBy())) && (CommonUtils.isEmpty(lclSearchForm.getCurrentLocCode())
                    || "X".equalsIgnoreCase(lclSearchForm.getFilterBy()))) {
                if (CommonUtils.isNotEmpty(lclSearchForm.getClientNo())) {
                    query.setString("qtClientNo", lclSearchForm.getClientNo());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getShipperNo())) {
                    query.setString("qtShipperNo", lclSearchForm.getShipperNo());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getConsigneeNo())) {
                    query.setString("qtConsigneeNo", lclSearchForm.getConsigneeNo());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getForwarderNo())) {
                    query.setString("qtFwdNo", lclSearchForm.getForwarderNo());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getForeignAgentAccount())) {
                    query.setString("qtForeignAgentNo", lclSearchForm.getForeignAgentAccount());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getPortName())) {
                    List qtOrigin = Arrays.asList(lclSearchForm.getPortName().split(","));
                    query.setParameterList("qtOrigin", qtOrigin);
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getPolName())) {
                    List qtPol = Arrays.asList(lclSearchForm.getPolName().split(","));
                    query.setParameterList("qtPol", qtPol);
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getPodCountryCode())) {
                    query.setString("qtPod", lclSearchForm.getPodCountryCode());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getDestCountryCode())) {
                    query.setString("qtDestination", lclSearchForm.getDestCountryCode());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getOriginRegion())) {
                    query.setString("qtOriginRegion", lclSearchForm.getOriginRegion());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getDestinationRegion())) {
                    query.setString("qtDestinationRegion", lclSearchForm.getDestinationRegion());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getIssuingTerminal())) {
                    query.setString("qtIssuingTerminal", lclSearchForm.getIssuingTerminal());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getCreatedBy())) {
                    query.setString("quoteBy", lclSearchForm.getCreatedBy());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getBookedBy())) {
                    query.setString("bookedBy", lclSearchForm.getBookedBy());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getBookedForVoyage())) {
                    query.setString("bkgVoyage", lclSearchForm.getBookedForVoyage());
                }

            }
        }
        return query;
    }

    public void getcount(String query, LclSearchForm lclSearchForm) throws Exception {
        SQLQuery queryObject = getCurrentSession().createSQLQuery(query);
        queryObject = setQueryValue(queryObject, lclSearchForm);
        lclSearchForm.getIgnoreSearchStatus();
        int searchCount = queryObject.list().size();
        if (CommonUtils.isNotEmpty(lclSearchForm.getLimit()) && !lclSearchForm.getMethodName().equalsIgnoreCase("goBackScreen") && lclSearchForm.getSearchType() == null
                && (lclSearchForm.getIgnoreSearchStatus() == null || !lclSearchForm.getIgnoreSearchStatus().equalsIgnoreCase("ignoreAlert"))) {
            if (searchCount > Integer.parseInt(lclSearchForm.getLimit())) {
                lclSearchForm.setRecordCount(true);
            }
        }
    }

    public List getBookedVoyageToLoad(List pol, String pod) throws Exception {
        List<LabelValueBean> voyageLoadunitlist = new ArrayList<LabelValueBean>();
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT  CONCAT(lss.`schedule_no`,'/',pol.`un_loc_code`,'/',pod.`un_loc_code`,'/',DATE_FORMAT(lsd.std,'%Y-%m-%d')) AS voyage, ");
        sb.append(" lss.id AS headerId FROM lcl_ss_header lss LEFT  JOIN lcl_ss_detail lsd ON lsd.`ss_header_id` = lss.`id` ");
        sb.append(" JOIN un_location pol ON lss.`origin_id` = pol.id  ");
        sb.append(" JOIN un_location pod ON lss.`destination_id` = pod.id WHERE pol.`un_loc_code` IN(:pol) AND pod.`un_loc_code` =:pod ");
        sb.append(" and lss.service_type = 'E' AND  lss.`status` <> 'V' AND  (SELECT COUNT(*) FROM lcl_unit_ss luss  WHERE luss.`ss_header_id` = lss.id AND luss.`status` = 'E') > 0 ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameterList("pol", pol);
        query.setParameter("pod", pod);
        List<Object[]> resultList = query.list();
        if (CommonUtils.isNotEmpty(resultList)) {
            for (Object[] row : resultList) {
                String voyage = (String) row[0];
                String headerId = (String) row[1].toString();
                if (null != row[1] && null != row[0]) {
                    voyageLoadunitlist.add(new LabelValueBean(voyage, headerId));
                }
            }
        }
        return voyageLoadunitlist;
    }

    public String[] getVoyageDetails(String headerId) throws Exception {
        String voyagedetail[] = new String[2];
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT lus.id AS unitSsId ,lsd.id AS detailId FROM lcl_ss_header lsh JOIN lcl_unit_ss lus ON lus.id = ");
        sb.append(" (SELECT ls.id FROM lcl_unit_ss ls WHERE ls.`ss_header_id`  = lsh.id AND ls.`status` <> 'C' LIMIT 1) ");
        sb.append(" JOIN lcl_ss_detail lsd ON lsd.id = (SELECT id FROM lcl_ss_detail l WHERE l.`ss_header_id` = lsh.`id` LIMIT 1)   WHERE lsh.id=:headerId ");
        Query query = getSession().createSQLQuery(sb.toString());
        query.setParameter("headerId", headerId);
        Object obj = query.uniqueResult();
        if (obj != null) {
            Object[] voyage = (Object[]) obj;
            if (voyage[0] != null && !voyage[0].toString().trim().equals("")) {
                voyagedetail[0] = voyage[0].toString();
            }
            if (voyage[1] != null && !voyage[1].toString().trim().equals("")) {
                voyagedetail[1] = voyage[1].toString();
            }
        }
        return voyagedetail;
    }

    public String checkLockingFclFile(String fileNumber, int userId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        String select1 = "File " + fileNumber + " is already opened in another window";
        String select2 = "This record is being used by ";
        queryBuilder.append("select if(u.user_id = '").append(userId).append("','").append(select1).append("',");
        queryBuilder.append("concat('").append(select2).append("',u.login_name,' on ',date_format(p.process_info_date,'%m/%d/%Y %h:%i %p'))) as result");
        queryBuilder.append(" from process_info p");
        queryBuilder.append(" join user_details u");
        queryBuilder.append(" on (p.user_id = u.user_id)");
        queryBuilder.append(" where p.record_id = '").append(fileNumber).append("'");
        queryBuilder.append(" order by p.id desc limit 1");
        return (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public void checkLockingFile(String fileNo, int userId, String screenName, HttpServletResponse response) throws Exception {
        String result = "";
        if ("LCL".equalsIgnoreCase(screenName)) {
            result = checkLocking(fileNo, userId);
        } else if ("FCL".equalsIgnoreCase(screenName)) {
            result = new SearchDAO().checkLockingFclFile(fileNo, userId);
        }
        PrintWriter out = response.getWriter();
        if (CommonUtils.isNotEmpty(result)) {
            out.print(result);
        } else {
            out.print("available");
        }
        out.flush();
        out.close();
    }

    private String getInlandDateQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append(" (SELECT  DATE_FORMAT(lsd.`sta` , '%d-%m-%Y')  ");
        sb.append(" FROM lcl_booking b ");
        sb.append(" JOIN lcl_booking_piece bp ");
        sb.append(" ON b.`file_number_id` = bp.`file_number_id` ");
        sb.append(" JOIN lcl_booking_piece_unit bpu ");
        sb.append(" ON bpu.`booking_piece_id` = bp.`id` ");
        sb.append(" JOIN lcl_unit_ss lus ON lus.id = ");
        sb.append(" (SELECT id FROM lcl_unit_ss WHERE id = bpu.`lcl_unit_ss_id` ORDER BY id DESC LIMIT 1 ) ");
        sb.append(" JOIN lcl_ss_header lsh ON lsh.id = lus.`ss_header_id` AND lsh.service_type IN('N') ");
        sb.append(" JOIN lcl_ss_detail lsd ON lsd.`ss_header_id` = lsh.id  ");
        sb.append(" WHERE b.`file_number_id` = f.id limit 1) AS inlandETA ");
        return sb.toString();
    }

    private String queryForBookingPieceDetais(LclSearchForm lclSearchForm) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" (SELECT   ");
        sb.append(" bp.id AS booking_piece_id,bp.file_number_id AS bfid, ");
        sb.append(" COALESCE(SUM(bp.actual_piece_count),SUM(bp.booked_piece_count)) AS total_piece, ");
        sb.append(" COALESCE(SUM(bp.actual_weight_imperial),SUM(bp.booked_weight_imperial)) AS total_weight_imperial, ");
        sb.append(" COALESCE(SUM(bp.actual_volume_imperial),SUM(bp.booked_volume_imperial)) AS total_volume_imperial, ");
        sb.append(" COALESCE(SUM(bp.actual_volume_metric),SUM(bp.booked_volume_metric)) AS total_volume_metric, ");
        sb.append(" COALESCE(SUM(bp.actual_weight_metric),SUM(bp.booked_weight_metric)) AS total_weight_metric, ");
        sb.append(" SUM(bp.booked_weight_imperial) AS booked_weight, ");
        sb.append(" SUM(bp.actual_weight_imperial) AS actual_weight, ");
        sb.append(" SUM(bp.booked_volume_imperial) AS booked_volume,  ");
        sb.append(" SUM(bp.actual_volume_imperial) AS actual_volume, ");
        sb.append(" SUM(bp.booked_weight_metric) AS booked_weight_metric, ");
        sb.append(" SUM(bp.actual_weight_metric) AS actual_weight_metric, ");
        sb.append(" SUM(bp.actual_volume_metric) AS actual_volume_metric, ");
        sb.append(" SUM(bp.booked_volume_metric) AS booked_volume_metric, ");
        sb.append(" SUM(bp.booked_piece_count) AS totalBookedPiece, ");
        sb.append(" SUM(bp.actual_piece_count) AS totalActualPiece, ");
        sb.append(" SUM(bp.`hazmat`) AS hazmat, ");
        sb.append(" (SELECT IF(GROUP_CONCAT(bh.un_hazmat_no) IS NOT NULL,TRUE, NULL) ");
        sb.append(" FROM lcl_booking_hazmat bh ");
        sb.append(" WHERE bh.file_number_id = bp.`file_number_id`) AS hazmatInfo ");
        sb.append(" FROM lcl_booking_piece  bp ");
        sb.append(" JOIN (").append(filterAppendQuery(lclSearchForm)).append(") AS filter");
        sb.append(" ON (bp.file_number_id = filter.id) GROUP BY bp.file_number_id ");
        sb.append(" )AS bp ON (bp.bfid =  f.file_number_id) ");
        return sb.toString();
    }

    private String queryForQuotePieceDetais(LclSearchForm lclSearchForm) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" (SELECT  ");
        sb.append(" qp.file_number_id AS qfid, ");
        sb.append(" SUM(qp.booked_piece_count) AS total_piece, ");
        sb.append(" SUM(qp.booked_weight_imperial) AS total_weight_imperial, ");
        sb.append(" SUM(qp.booked_volume_imperial) AS total_volume_imperial, ");
        sb.append(" SUM(qp.booked_volume_metric) AS total_volume_metric, ");
        sb.append(" SUM(qp.booked_weight_metric)AS total_weight_metric, ");
        sb.append(" SUM(qp.booked_weight_imperial) AS booked_weight, ");
        sb.append(" SUM(qp.actual_weight_imperial) AS actual_weight, ");
        sb.append(" SUM(qp.booked_volume_imperial) AS booked_volume,  ");
        sb.append(" SUM(qp.actual_volume_imperial) AS actual_volume, ");
        sb.append(" SUM(qp.booked_weight_metric) AS booked_weight_metric, ");
        sb.append(" SUM(qp.actual_weight_metric) AS actual_weight_metric, ");
        sb.append(" SUM(qp.actual_volume_metric) AS actual_volume_metric, ");
        sb.append(" SUM(qp.booked_volume_metric) AS booked_volume_metric, ");
        sb.append(" SUM(qp.booked_piece_count) AS totalBookedPiece, ");
        sb.append(" SUM(qp.actual_piece_count) AS totalActualPiece, ");
        sb.append(" SUM(qp.`hazmat`) AS hazmat, ");
        sb.append(" (SELECT IF(GROUP_CONCAT(qh.un_hazmat_no) IS NOT NULL,TRUE,NULL) ");
        sb.append(" FROM lcl_quote_hazmat qh ");
        sb.append(" WHERE qh.file_number_id = qp.file_number_id) AS hazmatInfo ");
        sb.append(" FROM lcl_quote_piece  qp");
        sb.append(" JOIN (").append(filterAppendQuery(lclSearchForm)).append(") AS filter");
        sb.append(" ON (qp.file_number_id =  filter.id) GROUP BY qp.file_number_id ");
        sb.append(" )AS qp ON (qp.qfid =  f.file_number_id) ");
        return sb.toString();
    }
}
