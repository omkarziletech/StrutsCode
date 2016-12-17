/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.common.utils;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.lcl.model.LclModel;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.CodetypeDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.lcl.comparator.LclManifestModelComparator;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.logiware.accounting.dao.LclManifestDAO;
import com.logiware.accounting.model.ManifestModel;
import java.util.Collections;
import java.util.List;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BooleanType;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

/**
 *
 * @author Wsware
 */
public class LclImportWarehouseRateUtil extends BaseHibernateDAO implements ConstantsInterface, LclCommonConstant {

    public List<ManifestModel> manifestedByUnitInDrQuery(Long unitSsId, List<Long> bookingAcIdList) throws Exception {
        Integer codeTypeId = new CodetypeDAO().getCodeTypeId("Vessel Codes");
        String ruleName = new SystemRulesDAO().getSystemRulesByCode(COMPANY_CODE);
        StringBuilder queryStr = new StringBuilder();
        queryStr.append("select fileId as fileId,shipmentType as shipmentType,bookingNumber as bookingNumber,dockReceipt as dockReceipt,");
        queryStr.append("voyageNumber as voyageNumber,sailDate as sailDate,eta AS eta,streamShipLine AS streamShipLine,containerNumbers as ");
        queryStr.append("containerNumbers,unitId AS unitId,origin as origin,destination as destination,");
        queryStr.append("shipperNo as shipperNo,shipperName as shipperName,consigneeNo as consigneeNo,consigneeName as consigneeName,");
        queryStr.append("blNumber AS blNumber,terminal as terminal,vesselNo as vesselNo,vesselName AS vesselName,chg.id as chargeId,");
        queryStr.append("chg.rate_per_unit_uom as ratePerUnitUOM,(chg.ar_amount+chg.adjustment_amount) as amount,glmap.Charge_code as chargeCode,");
        queryStr.append("glmap.bluescreen_chargecode as bluescreenChargeCode,IF(ad.Account IS NOT NULL,ad.account,'') AS glAccount,customerReferenceNo AS customerReferenceNo,");
        queryStr.append("pooCode AS pooCode,podCode AS podCode,fdCode AS fdCode,rateType AS rateType,");
        queryStr.append("masterBl AS masterBl,subhouseBl AS subhouseBl,chg.ar_bill_to_party AS billToParty, ");
        queryStr.append("customerName,customerNumber,TRUE AS manifest,thirdPartyNo as thirdPartyNo,thirdPartyName as thirdPartyName ");
        queryStr.append("from (SELECT file.id AS fileId,");
        queryStr.append("CONCAT('IMP-',file.file_number) as blNumber,'LCLI' AS shipmentType,unit.id AS unitId,unit_ss.sp_booking_no AS bookingNumber,file.file_number ");
        queryStr.append("AS dockReceipt,CONCAT(ss_head.billing_trmnum,'-',org.un_loc_code,'-',pod.un_loc_code,'-',ss_head.schedule_no) AS voyageNumber,ss_detail.sta AS sailDate,");
        queryStr.append("ss_detail.sta AS eta,steamshipline.acct_name ");
        queryStr.append("AS streamShipLine,unit.unit_no AS containerNumbers,CONCAT(org.un_loc_name,'/',IF(org_cntry.code = 'US',org_st.code,");
        queryStr.append("org_cntry.codedesc),'(',org.un_loc_code,')') AS origin,CONCAT(dest.un_loc_name,'/',IF(dest_cntry.code = 'US',dest_st.code,");
        queryStr.append("dest_cntry.codedesc),'(',dest.un_loc_code,')') AS destination,ship.acct_no AS shipperNo,ship.acct_name AS shipperName,");
        queryStr.append("cons.acct_no AS consigneeNo,cons.acct_name AS consigneeName, ");
        queryStr.append("tprty.acct_no AS thirdPartyNo,tprty.acct_name AS thirdPartyName,");
        queryStr.append("b.billing_terminal AS billing_terminal,vessel.code AS vesselNo,vessel.codedesc AS vesselName,IF(MAX(l3p.reference)<>'',MAX(l3p.reference),unit.unit_no) AS ");
        queryStr.append("customerReferenceNo,org.un_loc_code AS pooCode,pod.un_loc_code AS podCode,dest.un_loc_code AS fdCode,b.rate_type AS rateType,");
        queryStr.append("ussm.masterbl as masterBl,lbi.sub_house_bl as subhouseBl, ");
        queryStr.append("cfstp.acct_name AS customerName,cfstp.acct_no AS customerNumber ");
        queryStr.append("FROM lcl_unit_ss unit_ss JOIN lcl_booking_piece_unit unit_pieces ON unit_pieces.lcl_unit_ss_id = unit_ss.id ");
        queryStr.append("JOIN lcl_booking_piece book_piece ON unit_pieces.booking_piece_id = ");
        queryStr.append("book_piece.id JOIN lcl_unit unit ON unit_ss.unit_id = unit.id JOIN lcl_ss_header ss_head ON ");
        queryStr.append("unit_ss.ss_header_id = ss_head.id JOIN lcl_ss_detail ss_detail ON ss_head.id = ss_detail.ss_header_id AND ");
        queryStr.append("ss_detail.trans_mode = 'V'  LEFT JOIN lcl_unit_ss_manifest ussm ON (unit.id = ussm.unit_id AND ss_head.id = ussm.ss_header_id) ");
        queryStr.append(" JOIN lcl_unit_ss_imports lusi ON(lusi.ss_header_id = ss_head.id AND lusi.unit_id = unit.id) ");
        queryStr.append(" JOIN warehouse cfs ON cfs.id=lusi.cfs_warehouse_id JOIN trading_partner cfstp ON cfstp.acct_no=cfs.vendorno ");
        queryStr.append("JOIN lcl_file_number FILE ON book_piece.file_number_id = file.id JOIN lcl_booking b ON file.id = b.file_number_id AND b.booking_type='I'");
        queryStr.append("JOIN lcl_booking_import lbi ON file.id = lbi.file_number_id JOIN un_location org ON org.id = b.pol_id JOIN genericcode_dup ");
        queryStr.append("org_cntry ON org.countrycode = org_cntry.id LEFT JOIN genericcode_dup org_st ON org.statecode = org_st.id JOIN ");
        queryStr.append("un_location dest ON dest.id = b.fd_id JOIN un_location pod ON pod.id = b.pod_id JOIN genericcode_dup dest_cntry ON ");
        queryStr.append("dest.countrycode = dest_cntry.id LEFT JOIN ");
        queryStr.append("genericcode_dup dest_st ON dest.statecode = dest_st.id ");
        queryStr.append(" LEFT JOIN trading_partner ship ON b.ship_acct_no = ship.acct_no ");
        queryStr.append(" LEFT JOIN trading_partner cons ON b.cons_acct_no = cons.acct_no ");
        queryStr.append(" LEFT JOIN trading_partner tprty ON b.third_party_acct_no = tprty.acct_no ");
        queryStr.append(" LEFT JOIN trading_partner steamshipline ON steamshipline.acct_no = ss_detail.sp_acct_no ");
        queryStr.append(" LEFT JOIN genericcode_dup vessel ");
        queryStr.append("ON ss_detail.sp_reference_name = vessel.codedesc AND vessel.codetypeid = ");
        queryStr.append(codeTypeId);
        queryStr.append(" LEFT JOIN lcl_3p_ref_no l3p ON file.id = l3p.file_number_id AND l3p.type = 'cp'  WHERE ");
        queryStr.append("unit_ss.id = ").append(unitSsId);
        queryStr.append(" GROUP BY fileId) AS f JOIN lcl_booking_ac chg ON f.fileId = chg.file_number_id JOIN gl_mapping glmap ON glmap.id = ");
        queryStr.append("ar_gl_mapping_id LEFT JOIN terminal_gl_mapping term ON f.billing_terminal = term.terminal LEFT JOIN account_details ad ");
        queryStr.append("ON ad.Account=CONCAT_WS('-','");
        queryStr.append(ruleName);
        queryStr.append("',glmap.GL_Acct,LPAD(IF(glmap.derive_yn != 'N' AND glmap.derive_yn != 'F' AND ");
        queryStr.append("glmap.suffix_value IN ('B', 'L', 'D', 'F'),IF(glmap.suffix_value = 'B',term.lcl_import_billing,IF(glmap.suffix_value = ");
        queryStr.append("'L',term.lcl_import_loading,'')),glmap.suffix_value), 2, '0')) ");
        queryStr.append("where chg.ar_amount > 0.00 AND chg.post_ar=FALSE ");
        if (CommonUtils.isNotEmpty(bookingAcIdList)) {
            queryStr.append(" AND chg.id in(:bookingAcIdList) ");
        }
        queryStr.append(" AND chg.rels_to_inv = 0 AND chg.ar_bill_to_party='W' ORDER BY f.fileId");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        if (CommonUtils.isNotEmpty(bookingAcIdList)) {
            query.setParameterList("bookingAcIdList", bookingAcIdList);
        }
        query.setResultTransformer(Transformers.aliasToBean(ManifestModel.class));
        query.addScalar("fileId", LongType.INSTANCE);
        query.addScalar("unitId", LongType.INSTANCE);
        query.addScalar("blNumber", StringType.INSTANCE);
        query.addScalar("shipmentType", StringType.INSTANCE);
        query.addScalar("bookingNumber", StringType.INSTANCE);
        query.addScalar("dockReceipt", StringType.INSTANCE);
        query.addScalar("voyageNumber", StringType.INSTANCE);
        query.addScalar("sailDate", DateType.INSTANCE);
        query.addScalar("eta", DateType.INSTANCE);
        query.addScalar("streamShipLine", StringType.INSTANCE);
        query.addScalar("containerNumbers", StringType.INSTANCE);
        query.addScalar("origin", StringType.INSTANCE);
        query.addScalar("destination", StringType.INSTANCE);
        query.addScalar("customerReferenceNo", StringType.INSTANCE);
        query.addScalar("shipperNo", StringType.INSTANCE);
        query.addScalar("shipperName", StringType.INSTANCE);
        query.addScalar("consigneeNo", StringType.INSTANCE);
        query.addScalar("consigneeName", StringType.INSTANCE);
        query.addScalar("terminal", StringType.INSTANCE);
        query.addScalar("vesselNo", StringType.INSTANCE);
        query.addScalar("vesselName", StringType.INSTANCE);
        query.addScalar("chargeId", IntegerType.INSTANCE);
        query.addScalar("chargeCode", StringType.INSTANCE);
        query.addScalar("bluescreenChargeCode", StringType.INSTANCE);
        query.addScalar("glAccount", StringType.INSTANCE);
        query.addScalar("ratePerUnitUOM", StringType.INSTANCE);
        query.addScalar("pooCode", StringType.INSTANCE);
        query.addScalar("podCode", StringType.INSTANCE);
        query.addScalar("fdCode", StringType.INSTANCE);
        query.addScalar("rateType", StringType.INSTANCE);
        query.addScalar("amount", DoubleType.INSTANCE);
        query.addScalar("masterBl", StringType.INSTANCE);
        query.addScalar("subhouseBl", StringType.INSTANCE);
        query.addScalar("billToParty", StringType.INSTANCE);
        query.addScalar("customerName", StringType.INSTANCE);
        query.addScalar("customerNumber", StringType.INSTANCE);
        query.addScalar("manifest", BooleanType.INSTANCE);
        query.addScalar("thirdPartyNo", StringType.INSTANCE);
        query.addScalar("thirdPartyName", StringType.INSTANCE);
        return query.list();

    }

    public void manifestWarehouseCharges(Long unitSsId, List<Long> bookingAcIdList, User loginUser) throws Exception {
        User user = null;
        if (CommonUtils.isNotEmpty(bookingAcIdList)) {
            user = loginUser;
        } else {
            user = new UserDAO().findUserName("system");
        }
        LclManifestDAO lclManifestDAO = new LclManifestDAO();
        List<ManifestModel> warehouseChargeList = manifestedByUnitInDrQuery(unitSsId, bookingAcIdList);
        // warehouseChargeList = new LclUtils().getRolledUpChargesAccounting(warehouseChargeList, true, user, null, null, LCL_SHIPMENT_TYPE_IMPORT);
        if (CommonUtils.isNotEmpty(warehouseChargeList)) {
            Collections.sort(warehouseChargeList, new LclManifestModelComparator());
            lclManifestDAO.createLclArSubledgers(warehouseChargeList, user, true);
        }
    }

    public List<LclModel> getUnitSsId() throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append("SELECT lus.id AS unitSsId,lsh.schedule_no as scheduleNo,");
        queryStr.append(" w.vendorno as vendorNo ");//,cfs.acct_no as customerNumber ");
        queryStr.append(" FROM lcl_ss_header lsh ");
        queryStr.append(" JOIN lcl_unit_ss lus ON lus.ss_header_id=lsh.id ");
        queryStr.append(" JOIN lcl_unit lu ON lu.id=lus.unit_id ");
        queryStr.append(" JOIN lcl_unit_whse luw ON (luw.ss_header_id=lsh.id AND luw.unit_id=lus.unit_id) ");
        queryStr.append(" JOIN lcl_unit_ss_imports lusi ON(lusi.ss_header_id = lsh.id AND lusi.unit_id = lus.unit_id) ");
        queryStr.append(" JOIN warehouse w ON w.id=lusi.cfs_warehouse_id  ");
        //  queryStr.append(" LEFT JOIN trading_partner cfs ON cfs.acct_no=w.vendorno ");
        queryStr.append(" WHERE (lsh.closed_datetime IS NULL OR lsh.closed_datetime='')  ");
        queryStr.append(" AND lus.status='M' ");
        queryStr.append(" AND   DATE_ADD(luw.destuffed_datetime ,INTERVAL 5 DAY) = CURDATE() ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setResultTransformer(Transformers.aliasToBean(LclModel.class));
        query.addScalar("unitSsId", LongType.INSTANCE);
        query.addScalar("scheduleNo", StringType.INSTANCE);
        query.addScalar("vendorNo", StringType.INSTANCE);
        //  query.addScalar("vendorName", LongType.INSTANCE);
        return query.list();
    }

    public void manifestByWareHsCharge() throws Exception {
        List<LclModel> lclModelList = this.getUnitSsId();
        if (null != lclModelList && !lclModelList.isEmpty()) {
            for (LclModel lclModel : lclModelList) {
                if (CommonUtils.isNotEmpty(lclModel.getVendorNo())) {
                    this.manifestWarehouseCharges(lclModel.getUnitSsId(), null, null);
                }
            }
        }

    }
}
