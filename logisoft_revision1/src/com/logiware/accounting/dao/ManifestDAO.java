package com.logiware.accounting.dao;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.logisoft.bc.fcl.FclBlConstants;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclBookingAc;
import com.gp.cong.logisoft.domain.lcl.LclBookingAcTa;
import com.gp.cong.logisoft.domain.lcl.LclBookingAcTrans;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.LclSsAc;
import com.gp.cong.logisoft.domain.lcl.bl.LclCorrection;
import com.gp.cong.logisoft.domain.lcl.bl.LclCorrectionCharge;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.logisoft.hibernate.dao.CodetypeDAO;
import com.gp.cong.logisoft.hibernate.dao.CustomerAccountingDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingAcTransDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLRatesDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBlAcDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsAcDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLCorrectionChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLCorrectionDAO;
import com.gp.cong.logisoft.lcl.comparator.LclManifestModelComparator;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.BookingFcl;
import com.gp.cvst.logisoft.domain.FclBlCostCodes;
import com.gp.cvst.logisoft.domain.GlMapping;
import com.gp.cvst.logisoft.domain.Transaction;
import com.gp.cvst.logisoft.domain.TransactionLedger;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.gp.cvst.logisoft.hibernate.dao.TransactionLedgerDAO;
import com.logiware.accounting.model.ManifestModel;
import com.logiware.hibernate.dao.AccountingTransactionDAO;
import com.logiware.hibernate.dao.ArTransactionHistoryDAO;
import com.logiware.hibernate.domain.ArTransactionHistory;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

/**
 *
 * @author Lakshmi Narayanan
 */
public class ManifestDAO extends BaseHibernateDAO implements ConstantsInterface, LclCommonConstant {

    public void addAndUpdateFclBookingAccruals(ManifestModel model) throws Exception {
        AccrualsDAO accrualsDAO = new AccrualsDAO();
        GlMappingDAO glMappingDAO = new GlMappingDAO();
        TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
        BookingFcl booking = model.getBookingFcl();
        FclBlCostCodes fclBlCostCodes = model.getFclBlCostCodes();
        TransactionLedger accrual = new TransactionLedger();
        boolean update = false;
        if (null != accrualsDAO.getAccrualsByCostIdAndDR(fclBlCostCodes.getCodeId(), "04" + booking.getFileNo())) {
            accrual = accrualsDAO.getAccrualsByCostIdAndDR(fclBlCostCodes.getCodeId(), "04" + booking.getFileNo());
            update = true;
        }
        if ("I".equalsIgnoreCase(booking.getImportFlag())) {
            accrual.setTransactionDate(booking.getEta());
            accrual.setSailingDate(booking.getEta());
            accrual.setShipmentType("FCLI");
        } else {
            accrual.setSailingDate(booking.getEtd());
            accrual.setTransactionDate(booking.getEtd());
            accrual.setShipmentType("FCLE");
        }
        accrual.setTransactionType("AC");
        accrual.setTransactionAmt(fclBlCostCodes.getAmount());
        accrual.setBalance(fclBlCostCodes.getAmount());
        accrual.setBalanceInProcess(fclBlCostCodes.getAmount());
        accrual.setSubledgerSourceCode(SUB_LEDGER_CODE_ACCRUALS);
        accrual.setStatus(STATUS_OPEN);
        accrual.setCustNo(fclBlCostCodes.getAccNo());
        accrual.setCustName(tradingPartnerDAO.getAccountName(fclBlCostCodes.getAccNo()));
        accrual.setInvoiceNumber(fclBlCostCodes.getInvoiceNumber());
        accrual.setBookingNo(booking.getBookingNumber());
        accrual.setDocReceipt("04" + booking.getFileNo());
        accrual.setVoyageNo(booking.getVoyageCarrier());
        accrual.setContainerNo(model.getContainerNumbers());
        if (CommonUtils.isEqualIgnoreCase(fclBlCostCodes.getCostCode(), FclBlConstants.INTRAMP)) {
            accrual.setChargeCode(FclBlConstants.INTMDL);
        } else {
            accrual.setChargeCode(fclBlCostCodes.getCostCode());
        }
        String bluescreenChargeCode = glMappingDAO.getBlueScreenChargeCode(accrual.getShipmentType(), accrual.getChargeCode(), "AC", "E");
        accrual.setBlueScreenChargeCode(bluescreenChargeCode);
        String glAccountNumber = glMappingDAO.dervieGlAccount(accrual.getChargeCode(), accrual.getShipmentType(), model.getTerminal(), "E");
        accrual.setGlAccountNumber(glAccountNumber);
        accrual.setBillTo(YES);
        accrual.setReadyToPost(OFF);
        accrual.setCurrencyCode(fclBlCostCodes.getCurrencyCode());
        accrual.setDescription(fclBlCostCodes.getCostComments());
        accrual.setOrgTerminal(booking.getOriginTerminal());
        accrual.setDestination(booking.getPortofDischarge());
        accrual.setShipName(booking.getShipper());
        accrual.setShipNo(booking.getShipNo());
        accrual.setConsName(booking.getConsignee());
        accrual.setConsNo(booking.getConsNo());
        accrual.setFwdName(booking.getForward());
        accrual.setFwdNo(booking.getForwNo());
        accrual.setThirdptyName(booking.getAccountName());
        accrual.setThirdptyNo(booking.getAccountNumber());
        accrual.setAgentName(booking.getAgent());
        accrual.setAgentNo(booking.getAgentNo());
        if (CommonUtils.isNotEmpty(booking.getVessel())) {
            GenericCode vessel = new GenericCodeDAO().findByCodeDescName(booking.getVessel(), 14);
            if (null != vessel) {
                accrual.setVesselNo(vessel.getCode());
            }
        } else {
            accrual.setVesselNo(null);
        }
        accrual.setCostId(fclBlCostCodes.getCodeId());
        accrual.setManifestFlag(NO);
        if (update) {
            accrual.setUpdatedOn(new Date());
            accrual.setUpdatedBy(model.getUser().getUserId());
        } else {
            accrual.setCreatedOn(new Date());
            accrual.setCreatedBy(model.getUser().getUserId());
        }
        accrualsDAO.save(accrual);
    }

    public void deleteFclBookingAccruals(String codeId, String fileNo) {
        AccrualsDAO accrualsDAO = new AccrualsDAO();
        TransactionLedger accrual = accrualsDAO.getAccrualsByCostIdAndDR(Integer.parseInt(codeId), "04" + fileNo);
        if (null != accrual) {
            accrualsDAO.delete(accrual);
        }
    }

    public ManifestModel getAllManifestBookingsByUnitSS(Long unitSSId, User user, boolean isManifest, Long fileId, String transMode) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        ManifestModel manifestModel = null;
        CodetypeDAO codeTypeDAO = new CodetypeDAO();
        Integer codeTypeId = codeTypeDAO.getCodeTypeId("Vessel Codes");
        queryBuilder.append("select fileId as fileId,shipmentType as shipmentType,blNumber as blNumber,bookingNumber as bookingNumber,dockReceipt as dockReceipt,");
        queryBuilder.append("voyageNumber as voyageNumber,sailDate as sailDate,eta AS eta,streamShipLine AS streamShipLine,containerNumbers as ");
        queryBuilder.append("containerNumbers,unitId AS unitId,origin as origin,destination as destination,IF(chg.ar_bill_to_party = 'A',agentNo,");
        queryBuilder.append("IF(chg.ar_bill_to_party = 'C',consigneeNo,IF(chg.ar_bill_to_party = 'F',forwarderNo,");
        queryBuilder.append("if(chg.ar_bill_to_party = 'S',shipperNo,thirdPartyNo)))) AS customerNumber,");
        queryBuilder.append("IF(chg.ar_bill_to_party = 'A',agentName,");
        queryBuilder.append("IF(chg.ar_bill_to_party = 'C',consigneeName,IF(chg.ar_bill_to_party = 'F',forwarderName,");
        queryBuilder.append("if(chg.ar_bill_to_party = 'S',shipperName,thirdPartyName)))) AS customerName,");
        queryBuilder.append("shipperNo as shipperNo,shipperName as shipperName,consigneeNo as consigneeNo,consigneeName as consigneeName,forwarderNo as ");
        queryBuilder.append("forwarderNo,forwarderName as forwarderName,thirdPartyNo as thirdPartyNo,thirdPartyName as thirdPartyName,agentNo as agentNo,");
        queryBuilder.append("agentName as agentName,terminal as terminal,vesselNo as vesselNo,vesselName AS vesselName,chg.id as chargeId,");
        queryBuilder.append("chg.rate_per_unit_uom as ratePerUnitUOM,(chg.ar_amount+chg.adjustment_amount) as amount,glmap.Charge_code as chargeCode,");
        queryBuilder.append("glmap.bluescreen_chargecode as bluescreenChargeCode,concat_ws('-',(select rule_name from system_rules where rule_code = 'CompanyCode' limit 1),glmap.GL_Acct,IF(glmap.derive_yn != 'N' ");
        queryBuilder.append("AND glmap.derive_yn != 'F' AND glmap.suffix_value IN ('B', 'L', 'D', 'F'),IF(glmap.suffix_value = 'B',");
        queryBuilder.append("IF(f.shipmentType = 'LCLE',term.lcl_export_billing,term.lcl_import_billing),IF(glmap.suffix_value = 'L',");
        queryBuilder.append("IF(f.shipmentType = 'LCLE',term.lcl_export_loading,term.lcl_import_loading),IF(f.shipmentType = 'LCLE',");
        queryBuilder.append("term.lcl_export_dockreceipt,''))),glmap.suffix_value)) AS glAccount,customerReferenceNo AS customerReferenceNo,");
        queryBuilder.append("pooCode AS pooCode,podCode AS podCode,fdCode AS fdCode,rateType AS rateType,fdid from (SELECT file.id AS fileId,");
        queryBuilder.append("'LCLE' AS shipmentType,CONCAT(UPPER(RIGHT(org.un_loc_code, 3)),'-',dest.un_loc_code,");
        queryBuilder.append("'-',file.file_number) AS blNumber,unit.id AS unitId,");
        queryBuilder.append("unit_ss.sp_booking_no AS bookingNumber,file.file_number AS dockReceipt,ss_head.schedule_no AS voyageNumber,");
        queryBuilder.append("ss_detail.std AS sailDate,ss_detail.sta AS eta,steamshipline.acct_name AS streamShipLine,unit.unit_no AS containerNumbers,");
        queryBuilder.append("CONCAT(org.un_loc_name,'/',IF(org_cntry.code = 'US',org_st.code,");
        queryBuilder.append("org_cntry.codedesc),'(',org.un_loc_code,')') AS origin,CONCAT(dest.un_loc_name,'/',IF(dest_cntry.code = 'US',dest_st.code,");
        queryBuilder.append("dest_cntry.codedesc),'(',dest.un_loc_code,')') AS destination,ship.acct_no AS shipperNo,ship.acct_name AS ");
        queryBuilder.append("shipperName,cons.acct_no AS consigneeNo,cons.acct_name AS consigneeName,fwd.acct_no AS forwarderNo,fwd.acct_name AS ");
        queryBuilder.append("forwarderName,tprty.acct_no AS thirdPartyNo,tprty.acct_name AS thirdPartyName,agent.acct_no AS agentNo,agent.acct_name AS ");
        queryBuilder.append("agentName,bl.billing_terminal AS billing_terminal,vessel.code AS vesselNo,vessel.codedesc AS vesselName,lrm.remarks AS ");
        queryBuilder.append("customerReferenceNo,org.un_loc_code AS pooCode,pod.un_loc_code AS podCode,dest.un_loc_code AS fdCode,bl.rate_type AS ");
        queryBuilder.append("rateType ,if(bl.fd_id != '',bl.fd_id,bl.pod_id) as fdid FROM lcl_unit_ss unit_ss LEFT JOIN lcl_booking_piece_unit ");
        queryBuilder.append("unit_pieces ON unit_pieces.lcl_unit_ss_id = unit_ss.id LEFT JOIN lcl_booking_piece book_piece ON unit_pieces.booking_piece_id = ");
        queryBuilder.append("book_piece.id LEFT JOIN lcl_unit unit ON unit_ss.unit_id = unit.id LEFT JOIN lcl_ss_header ss_head ON ");
        queryBuilder.append("unit_ss.ss_header_id = ss_head.id LEFT JOIN lcl_ss_detail ss_detail ON ss_head.id = ss_detail.ss_header_id AND ss_detail.trans_mode = '");
        queryBuilder.append(transMode);
        queryBuilder.append("' LEFT JOIN lcl_file_number FILE ON book_piece.file_number_id = file.id AND file.state = 'BL' LEFT JOIN ");
        queryBuilder.append("lcl_bl bl ON file.id = bl.file_number_id LEFT JOIN un_location org ON org.id = bl.poo_id LEFT JOIN genericcode_dup ");
        queryBuilder.append("org_cntry ON org.countrycode = org_cntry.id LEFT JOIN genericcode_dup org_st ON org.statecode = org_st.id LEFT JOIN ");
        queryBuilder.append("un_location dest ON dest.id = IF(bl.fd_id IS NOT NULL,bl.fd_id ,bl.pod_id) LEFT JOIN un_location pod ON pod.id = bl.pod_id LEFT JOIN genericcode_dup dest_cntry ON ");
        queryBuilder.append("dest.countrycode = dest_cntry.id LEFT JOIN ");
        queryBuilder.append("genericcode_dup dest_st ON dest.statecode = dest_st.id LEFT JOIN trading_partner ship ON bl.ship_acct_no = ship.acct_no ");
        queryBuilder.append("LEFT JOIN trading_partner cons ON bl.cons_acct_no = cons.acct_no LEFT JOIN trading_partner fwd ON bl.fwd_acct_no = ");
        queryBuilder.append("fwd.acct_no LEFT JOIN trading_partner tprty ON bl.third_party_acct_no = tprty.acct_no LEFT JOIN trading_partner agent ON ");
        queryBuilder.append("bl.agent_acct_no = agent.acct_no LEFT JOIN trading_partner steamshipline ON steamshipline.acct_no = ss_detail.sp_acct_no ");
        queryBuilder.append(" LEFT JOIN genericcode_dup vessel ON ss_detail.sp_reference_name = vessel.codedesc AND vessel.codetypeid = ");
        queryBuilder.append(codeTypeId);
        queryBuilder.append(" LEFT JOIN lcl_remarks lrm ON file.id = lrm.file_number_id AND lrm.type = 'Export Reference' WHERE ");
        if (CommonUtils.isNotEmpty(unitSSId)) {
            queryBuilder.append("unit_ss.id = ").append(unitSSId);
        }
        if (CommonUtils.isNotEmpty(fileId)) {
            if (CommonUtils.isNotEmpty(unitSSId)) {
                queryBuilder.append(" AND ");
            }
            queryBuilder.append("file.id = ").append(fileId);
        }
        queryBuilder.append(" GROUP BY fileId) AS f LEFT JOIN lcl_bl_ac chg ON f.fileId = chg.file_number_id LEFT JOIN gl_mapping glmap ON glmap.id = ");
        queryBuilder.append("ar_gl_mapping_id LEFT JOIN terminal_gl_mapping term ON f.billing_terminal = term.terminal where ");
        queryBuilder.append("(chg.ar_amount > 0.00 OR chg.adjustment_amount > 0.00) ORDER BY f.fileId");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
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
        query.addScalar("customerNumber", StringType.INSTANCE);
        query.addScalar("customerName", StringType.INSTANCE);
        query.addScalar("customerReferenceNo", StringType.INSTANCE);
        query.addScalar("shipperNo", StringType.INSTANCE);
        query.addScalar("shipperName", StringType.INSTANCE);
        query.addScalar("consigneeNo", StringType.INSTANCE);
        query.addScalar("consigneeName", StringType.INSTANCE);
        query.addScalar("forwarderNo", StringType.INSTANCE);
        query.addScalar("forwarderName", StringType.INSTANCE);
        query.addScalar("thirdPartyNo", StringType.INSTANCE);
        query.addScalar("thirdPartyName", StringType.INSTANCE);
        query.addScalar("agentNo", StringType.INSTANCE);
        query.addScalar("agentName", StringType.INSTANCE);
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
        List<ManifestModel> l = query.list();
        LclUtils lclUtils = new LclUtils();
        l = lclUtils.getRolledUpChargesAccounting(l, isManifest, user, "", "", LCL_SHIPMENT_TYPE_EXPORT);
        Collections.sort(l, new LclManifestModelComparator());
        if (CommonUtils.isNotEmpty(l)) {
            createLclArSubledgers(l, user);
            manifestModel = l.get(0);
            updateLclAccruals(lclUtils.getFileNumbers().toString(), manifestModel);
            lclUtils.updateLclFileNumbers(lclUtils.getFileNumbers().toString(), isManifest, manifestModel.getShipmentType());
        }
        return manifestModel;
    }

    public ManifestModel getAllManifestImportsBookingsByUnitSS(Long unitSSId, Long bookingAcId, String buttonValue, User user,
            boolean isManifest, String realPath, boolean isUpdateFileStatus, String fileId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        TransactionLedgerDAO transactionLedgerDAO = new TransactionLedgerDAO();
        LclSsAcDAO lclSsAcDAO = new LclSsAcDAO();
        LCLCorrectionDAO lclCorrectionDAO = new LCLCorrectionDAO();
        SystemRulesDAO systemRulesDAO = new SystemRulesDAO();
        CodetypeDAO codeTypeDAO = new CodetypeDAO();
        LclUtils lclUtils = new LclUtils();
        LclUnitSsDAO lclUnitSsDAO = new LclUnitSsDAO();
        LclFileNumberDAO lclFileNumberDAO = new LclFileNumberDAO();
        String ruleName = systemRulesDAO.getSystemRulesByCode(COMPANY_CODE);
        Integer codeTypeId = codeTypeDAO.getCodeTypeId("Vessel Codes");
        ManifestModel manifestModel = null;
        String manifestStatus = "M";
        if (!isManifest) {
            String correctionIds = lclCorrectionDAO.getAllCorrectionIds(unitSSId);
            if (CommonUtils.isNotEmpty(correctionIds)) {
                lclUtils.reverseAllImportsCorrections(correctionIds, user);
            }
            manifestStatus = "B";
        }
        queryBuilder.append("select fileId as fileId,shipmentType as shipmentType,bookingNumber as bookingNumber,dockReceipt as dockReceipt,");
        queryBuilder.append("voyageNumber as voyageNumber,sailDate as sailDate,eta AS eta,streamShipLine AS streamShipLine,containerNumbers as ");
        queryBuilder.append("containerNumbers,unitId AS unitId,origin as origin,destination as destination,");
        queryBuilder.append("shipperNo as shipperNo,shipperName as shipperName,consigneeNo as consigneeNo,consigneeName as consigneeName,");
        queryBuilder.append("forwarderNo as forwarderNo,forwarderName as forwarderName,thirdPartyNo as thirdPartyNo,thirdPartyName as thirdPartyName,");
        queryBuilder.append("blNumber AS blNumber,terminal as terminal,vesselNo as vesselNo,vesselName AS vesselName,chg.id as chargeId,");
        queryBuilder.append("chg.rate_per_unit_uom as ratePerUnitUOM,(chg.ar_amount+chg.adjustment_amount) as amount,glmap.Charge_code as chargeCode,");
        queryBuilder.append("glmap.bluescreen_chargecode as bluescreenChargeCode,IF(ad.Account IS NOT NULL,ad.account,'') AS glAccount,customerReferenceNo AS customerReferenceNo,");
        queryBuilder.append("pooCode AS pooCode,podCode AS podCode,fdCode AS fdCode,rateType AS rateType,");
        queryBuilder.append("IF(chg.ar_bill_to_party = 'C',consigneeNo,IF(chg.ar_bill_to_party = 'N',notifyNo,thirdPartyNo)) AS customerNumber,");
        queryBuilder.append("IF(chg.ar_bill_to_party = 'C',consigneeName,IF(chg.ar_bill_to_party = 'N',notifyName,thirdPartyName)) AS customerName,");
        queryBuilder.append("masterBl AS masterBl,subhouseBl AS subhouseBl,chg.ar_bill_to_party AS billToParty ");
        queryBuilder.append("from (SELECT file.id AS fileId,");
        queryBuilder.append("CONCAT('IMP-',file.file_number) as blNumber,'LCLI' AS shipmentType,unit.id AS unitId,unit_ss.sp_booking_no AS bookingNumber,file.file_number ");
        queryBuilder.append("AS dockReceipt,CONCAT(ss_head.billing_trmnum,'-',org.un_loc_code,'-',pod.un_loc_code,'-',ss_head.schedule_no) AS voyageNumber,ss_detail.sta AS sailDate,");
        queryBuilder.append("ss_detail.sta AS eta,steamshipline.acct_name ");
        queryBuilder.append("AS streamShipLine,unit.unit_no AS containerNumbers,CONCAT(org.un_loc_name,'/',IF(org_cntry.code = 'US',org_st.code,");
        queryBuilder.append("org_cntry.codedesc),'(',org.un_loc_code,')') AS origin,CONCAT(dest.un_loc_name,'/',IF(dest_cntry.code = 'US',dest_st.code,");
        queryBuilder.append("dest_cntry.codedesc),'(',dest.un_loc_code,')') AS destination,ship.acct_no AS shipperNo,ship.acct_name AS shipperName,");
        queryBuilder.append("noty.acct_no AS notifyNo,noty.acct_name AS ");
        queryBuilder.append("notifyName,cons.acct_no AS consigneeNo,cons.acct_name AS consigneeName,fwd.acct_no AS forwarderNo,fwd.acct_name AS ");
        queryBuilder.append("forwarderName,tprty.acct_no AS thirdPartyNo,tprty.acct_name AS thirdPartyName,");
        queryBuilder.append("b.billing_terminal AS billing_terminal,vessel.code AS vesselNo,vessel.codedesc AS vesselName,lrm.remarks AS ");
        queryBuilder.append("customerReferenceNo,org.un_loc_code AS pooCode,pod.un_loc_code AS podCode,dest.un_loc_code AS fdCode,b.rate_type AS rateType,");
        queryBuilder.append("ussm.masterbl as masterBl,lbi.sub_house_bl as subhouseBl ");
        queryBuilder.append("FROM lcl_unit_ss unit_ss JOIN lcl_booking_piece_unit unit_pieces ON unit_pieces.lcl_unit_ss_id = unit_ss.id ");
        queryBuilder.append("JOIN lcl_booking_piece book_piece ON unit_pieces.booking_piece_id = ");
        queryBuilder.append("book_piece.id JOIN lcl_unit unit ON unit_ss.unit_id = unit.id JOIN lcl_ss_header ss_head ON ");
        queryBuilder.append("unit_ss.ss_header_id = ss_head.id JOIN lcl_ss_detail ss_detail ON ss_head.id = ss_detail.ss_header_id AND ");
        queryBuilder.append("ss_detail.trans_mode = 'V'  LEFT JOIN lcl_unit_ss_manifest ussm ON (unit.id = ussm.unit_id AND ss_head.id = ussm.ss_header_id) ");
        queryBuilder.append("JOIN lcl_file_number FILE ON book_piece.file_number_id = file.id JOIN lcl_booking b ON file.id = b.file_number_id AND b.booking_type='I'");
        queryBuilder.append("JOIN lcl_booking_import lbi ON file.id = lbi.file_number_id JOIN un_location org ON org.id = b.pol_id JOIN genericcode_dup ");
        queryBuilder.append("org_cntry ON org.countrycode = org_cntry.id LEFT JOIN genericcode_dup org_st ON org.statecode = org_st.id JOIN ");
        queryBuilder.append("un_location dest ON dest.id = b.fd_id JOIN un_location pod ON pod.id = b.pod_id JOIN genericcode_dup dest_cntry ON ");
        queryBuilder.append("dest.countrycode = dest_cntry.id LEFT JOIN ");
        queryBuilder.append("genericcode_dup dest_st ON dest.statecode = dest_st.id LEFT JOIN trading_partner ship ON b.ship_acct_no = ship.acct_no ");
        queryBuilder.append("LEFT JOIN trading_partner cons ON b.cons_acct_no = cons.acct_no LEFT JOIN trading_partner fwd ON b.fwd_acct_no = ");
        queryBuilder.append("fwd.acct_no LEFT JOIN trading_partner tprty ON b.third_party_acct_no = tprty.acct_no ");
        queryBuilder.append("LEFT JOIN trading_partner steamshipline ON steamshipline.acct_no = ss_detail.sp_acct_no ");
        queryBuilder.append("LEFT JOIN trading_partner noty ON b.noty_acct_no = noty.acct_no  LEFT JOIN genericcode_dup vessel ");
        queryBuilder.append("ON ss_detail.sp_reference_name = vessel.codedesc AND vessel.codetypeid = ");
        queryBuilder.append(codeTypeId);
        queryBuilder.append(" LEFT JOIN lcl_remarks lrm ON file.id = lrm.file_number_id AND lrm.type = 'Export Reference' WHERE ");
        if (CommonUtils.isNotEmpty(fileId)) {
            queryBuilder.append("file.id = ").append(fileId);
        } else {
            if (isManifest) {
                queryBuilder.append("file.status <> 'M' AND ");
            } else {
                queryBuilder.append("file.status = 'M' AND ");
            }
            queryBuilder.append("unit_ss.id = ").append(unitSSId);
        }
        queryBuilder.append(" GROUP BY fileId) AS f JOIN lcl_booking_ac chg ON f.fileId = chg.file_number_id JOIN gl_mapping glmap ON glmap.id = ");
        queryBuilder.append("ar_gl_mapping_id LEFT JOIN terminal_gl_mapping term ON f.billing_terminal = term.terminal LEFT JOIN account_details ad ");
        queryBuilder.append("ON ad.Account=CONCAT_WS('-','");
        queryBuilder.append(ruleName);
        queryBuilder.append("',glmap.GL_Acct,LPAD(IF(glmap.derive_yn != 'N' AND glmap.derive_yn != 'F' AND ");
        queryBuilder.append("glmap.suffix_value IN ('B', 'L', 'D', 'F'),IF(glmap.suffix_value = 'B',term.lcl_import_billing,IF(glmap.suffix_value = ");
        queryBuilder.append("'L',term.lcl_import_loading,'')),glmap.suffix_value), 2, '0')) ");
        queryBuilder.append("where chg.ar_amount > 0.00");
        if (CommonUtils.isNotEmpty(bookingAcId)) {
            queryBuilder.append(" AND chg.id = ").append(bookingAcId);
        }
        queryBuilder.append(" AND chg.rels_to_inv = 0 AND chg.ar_bill_to_party!='A' ORDER BY f.fileId");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
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
        query.addScalar("customerNumber", StringType.INSTANCE);
        query.addScalar("customerName", StringType.INSTANCE);
        query.addScalar("customerReferenceNo", StringType.INSTANCE);
        query.addScalar("shipperNo", StringType.INSTANCE);
        query.addScalar("shipperName", StringType.INSTANCE);
        query.addScalar("consigneeNo", StringType.INSTANCE);
        query.addScalar("consigneeName", StringType.INSTANCE);
        query.addScalar("forwarderNo", StringType.INSTANCE);
        query.addScalar("forwarderName", StringType.INSTANCE);
        query.addScalar("thirdPartyNo", StringType.INSTANCE);
        query.addScalar("thirdPartyName", StringType.INSTANCE);
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
        List<ManifestModel> l = query.list();
        l = lclUtils.getRolledUpChargesAccounting(l, isManifest, user, buttonValue, realPath, LCL_SHIPMENT_TYPE_IMPORT);
        Collections.sort(l, new LclManifestModelComparator());
        if (CommonUtils.isNotEmpty(l)) {
            createLclArSubledgers(l, user);
            manifestModel = l.get(0);
            updateLclAccruals(lclUtils.getFileNumbers().toString(), manifestModel);
            if (isManifest) {
                String costIds = lclSsAcDAO.getConcatenatedIds(unitSSId);
                if (CommonUtils.isNotEmpty(costIds)) {
                    transactionLedgerDAO.updateTransactionLedgerDates(manifestModel, costIds);
                }
            }
        }
        if (isUpdateFileStatus) {
            String fileIds = null;
            if (CommonUtils.isNotEmpty(fileId)) {
                fileIds = fileId;
            } else {
                fileIds = lclUnitSsDAO.getConcatenatedFileIds(String.valueOf(unitSSId));
            }
            lclFileNumberDAO.updateLclFileNumbersStatus(fileIds, manifestStatus);
        }
        return manifestModel;
    }

    public ManifestModel getLCLDetails(Long correctionId, Long fileId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        CodetypeDAO codeTypeDAO = new CodetypeDAO();
        Integer codeTypeId = codeTypeDAO.getCodeTypeId("Vessel Codes");
        queryBuilder.append("SELECT CONCAT(UPPER(RIGHT(org.un_loc_code, 3)),'-',dest.un_loc_code,'-',file.file_number) AS blNumber,file.file_number ");
        queryBuilder.append("AS dockReceipt,unit_ss.sp_booking_no AS bookingNumber,ss_head.schedule_no AS ");
        queryBuilder.append("voyageNumber,ss_detail.std AS sailDate,ss_detail.sta AS eta,steamshipline.acct_name AS ");
        queryBuilder.append("streamShipLine,unit.unit_no AS containerNumbers,CONCAT(org.un_loc_name,'/',IF(org_cntry.code = 'US',org_st.code,org_cntry.codedesc),'(',org.un_loc_code,");
        queryBuilder.append("')') AS origin,CONCAT(dest.un_loc_name,'/',IF(dest_cntry.code = 'US',dest_st.code,dest_cntry.codedesc");
        queryBuilder.append("),'(',dest.un_loc_code,')') AS destination,ship.acct_no AS shipperNo,ship.acct_name AS shipperName,cons.acct_no AS ");
        queryBuilder.append("consigneeNo, notify.acct_no AS notifyNo,notify.acct_name AS notifyName,");
        queryBuilder.append("cons.acct_name AS consigneeName,fwd.acct_no AS forwarderNo,fwd.acct_name AS forwarderName,tprty.acct_no AS ");
        queryBuilder.append("thirdPartyNo,tprty.acct_name AS thirdPartyName,agent.acct_no AS agentNo,agent.acct_name AS agentName,bl.billing_terminal AS");
        queryBuilder.append(" terminal,vessel.code AS vesselNo,vessel.codedesc AS vesselName,org.un_loc_code AS pooCode FROM lcl_file_number FILE JOIN ");
        queryBuilder.append("lcl_bl bl ON file.id = bl.file_number_id JOIN un_location org ON org.id = bl.poo_id JOIN genericcode_dup org_cntry ON org.countrycode = ");
        queryBuilder.append("org_cntry.id LEFT JOIN genericcode_dup org_st ON org.statecode = org_st.id JOIN un_location dest ON dest.id = IF(bl.fd_id  IS NOT NULL,bl.fd_id ,bl.pod_id) ");
        queryBuilder.append("JOIN genericcode_dup dest_cntry ON dest.countrycode = dest_cntry.id ");
        queryBuilder.append("LEFT JOIN genericcode_dup dest_st ON dest.statecode = dest_st.id LEFT JOIN trading_partner ship ON bl.ship_acct_no = ");
        queryBuilder.append("ship.acct_no LEFT JOIN trading_partner cons ON bl.cons_acct_no = cons.acct_no LEFT JOIN trading_partner fwd ON bl.");
        queryBuilder.append("fwd_acct_no = fwd.acct_no LEFT JOIN trading_partner tprty ON bl.third_party_acct_no = tprty.acct_no LEFT JOIN ");
        queryBuilder.append("trading_partner agent ON bl.agent_acct_no = agent.acct_no  LEFT JOIN trading_partner notify ON bl.noty_acct_no = ");
        queryBuilder.append("notify.acct_no LEFT JOIN lcl_booking_piece book_piece ON bl.file_number_id = book_piece.file_number_id ");
        queryBuilder.append("LEFT JOIN lcl_booking_piece_unit unit_pieces ON book_piece.id = unit_pieces.booking_piece_id ");
        queryBuilder.append("LEFT JOIN lcl_unit_ss unit_ss ON unit_pieces.lcl_unit_ss_id =unit_ss.id LEFT JOIN lcl_unit unit ON unit_ss.unit_id = ");
        queryBuilder.append("unit.id LEFT JOIN lcl_ss_header ss_head ON unit_ss.ss_header_id = ss_head.id LEFT JOIN lcl_ss_detail ss_detail ");
        queryBuilder.append("ON ss_head.id = ss_detail.ss_header_id AND ss_detail.trans_mode = 'V' LEFT JOIN trading_partner steamshipline ON ");
        queryBuilder.append("steamshipline.acct_no = ss_detail.sp_acct_no LEFT JOIN genericcode_dup vessel ON ss_detail.sp_reference_name = ");
        queryBuilder.append("vessel.codedesc AND vessel.codetypeid = ");
        queryBuilder.append(codeTypeId);
        queryBuilder.append(" LEFT JOIN lcl_correction lcl_corr ON file.id = lcl_corr.file_number_id AND lcl_corr.id = ");
        queryBuilder.append(correctionId);
        queryBuilder.append(" WHERE file.id = ");
        queryBuilder.append(fileId);
        queryBuilder.append(" GROUP BY file.id ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(ManifestModel.class));
        query.addScalar("blNumber", StringType.INSTANCE);
        query.addScalar("bookingNumber", StringType.INSTANCE);
        query.addScalar("dockReceipt", StringType.INSTANCE);
        query.addScalar("voyageNumber", StringType.INSTANCE);
        query.addScalar("sailDate", DateType.INSTANCE);
        query.addScalar("eta", DateType.INSTANCE);
        query.addScalar("streamShipLine", StringType.INSTANCE);
        query.addScalar("containerNumbers", StringType.INSTANCE);
        query.addScalar("origin", StringType.INSTANCE);
        query.addScalar("destination", StringType.INSTANCE);
        query.addScalar("shipperNo", StringType.INSTANCE);
        query.addScalar("shipperName", StringType.INSTANCE);
        query.addScalar("consigneeNo", StringType.INSTANCE);
        query.addScalar("consigneeName", StringType.INSTANCE);
        query.addScalar("forwarderNo", StringType.INSTANCE);
        query.addScalar("forwarderName", StringType.INSTANCE);
        query.addScalar("thirdPartyNo", StringType.INSTANCE);
        query.addScalar("thirdPartyName", StringType.INSTANCE);
        query.addScalar("agentNo", StringType.INSTANCE);
        query.addScalar("agentName", StringType.INSTANCE);
        query.addScalar("notifyNo", StringType.INSTANCE);
        query.addScalar("notifyName", StringType.INSTANCE);
        query.addScalar("terminal", StringType.INSTANCE);
        query.addScalar("vesselNo", StringType.INSTANCE);
        query.addScalar("vesselName", StringType.INSTANCE);
        query.addScalar("pooCode", StringType.INSTANCE);
        ManifestModel manifestModel = (ManifestModel) query.uniqueResult();
        manifestModel.setShipmentType(LCL_SHIPMENT_TYPE_EXPORT);
        return manifestModel;
    }

    public ManifestModel getLCLDetailsImports(Long correctionId, Long fileId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        CodetypeDAO codeTypeDAO = new CodetypeDAO();
        Integer codeTypeId = codeTypeDAO.getCodeTypeId("Vessel Codes");
        queryBuilder.append("SELECT CONCAT('IMP-', file.file_number) AS blNumber,file.file_number AS dockReceipt,unit_ss.sp_booking_no AS bookingNumber,");
        queryBuilder.append("CONCAT(ss_head.billing_trmnum,'-',org.un_loc_code,'-',pod.un_loc_code,'-',ss_head.schedule_no) as voyageNumber,ss_detail.sta AS sailDate,ss_detail.sta AS eta,");
        queryBuilder.append("steamshipline.acct_name AS streamShipLine,unit.unit_no AS containerNumbers,CONCAT(org.un_loc_name,'/',IF(org_cntry.code = 'US',org_st.code,org_cntry.codedesc),");
        queryBuilder.append("'(',org.un_loc_code,')') AS origin,CONCAT(dest.un_loc_name,'/',IF(dest_cntry.code = 'US',dest_st.code,dest_cntry.codedesc");
        queryBuilder.append("),'(',dest.un_loc_code,')') AS destination,ship.acct_no AS shipperNo,ship.acct_name AS shipperName,cons.acct_no AS ");
        queryBuilder.append("consigneeNo, notify.acct_no AS notifyNo,notify.acct_name AS notifyName,");
        queryBuilder.append("cons.acct_name AS consigneeName,fwd.acct_no AS forwarderNo,fwd.acct_name AS forwarderName,tprty.acct_no AS ");
        queryBuilder.append("thirdPartyNo,tprty.acct_name AS thirdPartyName,agent.acct_no AS agentNo,agent.acct_name AS agentName,book.billing_terminal AS ");
        queryBuilder.append("terminal,vessel.code AS vesselNo,vessel.codedesc AS vesselName,org.un_loc_code AS pooCode,ussm.masterbl AS masterBl,lbi.sub_house_bl AS subhouseBl,book.bill_to_party AS ");
        queryBuilder.append("billToParty FROM lcl_file_number FILE JOIN lcl_booking book ON file.id = book.file_number_id JOIN un_location org ON org.id = book.pol_id  JOIN  lcl_booking_import lbi ");
        queryBuilder.append("ON file.id = lbi.file_number_id JOIN genericcode_dup org_cntry ON org.countrycode = org_cntry.id LEFT JOIN genericcode_dup org_st ON org.statecode = ");
        queryBuilder.append("org_st.id JOIN un_location dest ON dest.id = book.fd_id JOIN genericcode_dup dest_cntry ON dest.countrycode = dest_cntry.id ");
        queryBuilder.append("LEFT JOIN genericcode_dup dest_st ON dest.statecode = dest_st.id JOIN un_location pod ON pod.id = book.pod_id LEFT JOIN trading_partner ship ON book.ship_acct_no = ");
        queryBuilder.append("ship.acct_no LEFT JOIN trading_partner cons ON book.cons_acct_no = cons.acct_no LEFT JOIN trading_partner fwd ON book.");
        queryBuilder.append("fwd_acct_no = fwd.acct_no LEFT JOIN trading_partner tprty ON book.third_party_acct_no = tprty.acct_no LEFT JOIN ");
        queryBuilder.append("trading_partner agent ON book.agent_acct_no = agent.acct_no  LEFT JOIN trading_partner notify ON book.noty_acct_no = ");
        queryBuilder.append("notify.acct_no LEFT JOIN lcl_booking_piece book_piece ON book.file_number_id = book_piece.file_number_id ");
        queryBuilder.append("LEFT JOIN lcl_booking_piece_unit unit_pieces ON book_piece.id = unit_pieces.booking_piece_id ");
        queryBuilder.append("LEFT JOIN lcl_unit_ss unit_ss ON unit_pieces.lcl_unit_ss_id =unit_ss.id LEFT JOIN lcl_unit unit ON unit_ss.unit_id = ");
        queryBuilder.append("unit.id LEFT JOIN lcl_ss_header ss_head ON unit_ss.ss_header_id = ss_head.id LEFT JOIN lcl_ss_detail ss_detail ");
        queryBuilder.append("ON ss_head.id = ss_detail.ss_header_id AND ss_detail.trans_mode = 'V' LEFT JOIN trading_partner steamshipline ON ");
        queryBuilder.append("steamshipline.acct_no = ss_detail.sp_acct_no LEFT JOIN genericcode_dup vessel ON ss_detail.sp_reference_name = ");
        queryBuilder.append("vessel.codedesc AND vessel.codetypeid = ");
        queryBuilder.append(codeTypeId);
        queryBuilder.append(" LEFT JOIN lcl_unit_ss_manifest ussm ON (unit.id = ussm.unit_id AND ss_head.id = ussm.ss_header_id) ");
        queryBuilder.append("LEFT JOIN lcl_correction lcl_corr ON file.id = lcl_corr.file_number_id AND lcl_corr.id = ");
        queryBuilder.append(correctionId);
        queryBuilder.append(" WHERE file.id = ");
        queryBuilder.append(fileId);
        queryBuilder.append(" GROUP BY file.id ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(ManifestModel.class));
        query.addScalar("blNumber", StringType.INSTANCE);
        query.addScalar("bookingNumber", StringType.INSTANCE);
        query.addScalar("dockReceipt", StringType.INSTANCE);
        query.addScalar("voyageNumber", StringType.INSTANCE);
        query.addScalar("sailDate", DateType.INSTANCE);
        query.addScalar("eta", DateType.INSTANCE);
        query.addScalar("streamShipLine", StringType.INSTANCE);
        query.addScalar("containerNumbers", StringType.INSTANCE);
        query.addScalar("origin", StringType.INSTANCE);
        query.addScalar("destination", StringType.INSTANCE);
        query.addScalar("shipperNo", StringType.INSTANCE);
        query.addScalar("shipperName", StringType.INSTANCE);
        query.addScalar("consigneeNo", StringType.INSTANCE);
        query.addScalar("consigneeName", StringType.INSTANCE);
        query.addScalar("forwarderNo", StringType.INSTANCE);
        query.addScalar("forwarderName", StringType.INSTANCE);
        query.addScalar("thirdPartyNo", StringType.INSTANCE);
        query.addScalar("thirdPartyName", StringType.INSTANCE);
        query.addScalar("agentNo", StringType.INSTANCE);
        query.addScalar("agentName", StringType.INSTANCE);
        query.addScalar("notifyNo", StringType.INSTANCE);
        query.addScalar("notifyName", StringType.INSTANCE);
        query.addScalar("terminal", StringType.INSTANCE);
        query.addScalar("vesselNo", StringType.INSTANCE);
        query.addScalar("vesselName", StringType.INSTANCE);
        query.addScalar("pooCode", StringType.INSTANCE);
        query.addScalar("masterBl", StringType.INSTANCE);
        query.addScalar("subhouseBl", StringType.INSTANCE);
        query.addScalar("billToParty", StringType.INSTANCE);
        ManifestModel manifestModel = (ManifestModel) query.uniqueResult();
        manifestModel.setShipmentType(LCL_SHIPMENT_TYPE_IMPORT);
        return manifestModel;
    }

    public void createLclCorrections(String moduleName, User user, boolean approved, LclCorrection lclCorrection, boolean isManifest) throws Exception {
        AccrualsDAO accrualsDAO = new AccrualsDAO();
        GlMappingDAO glMappingDAO = new GlMappingDAO();
        LCLCorrectionChargeDAO lclCorrectionChargeDAO = new LCLCorrectionChargeDAO();
        CustomerAccountingDAO accountingDAO = new CustomerAccountingDAO();
        TransactionLedgerDAO transactionLedgerDAO = new TransactionLedgerDAO();
        LclUtils lclUtils = new LclUtils();
        ManifestModel model = null;
        if (moduleName.equalsIgnoreCase("Imports")) {
            model = getLCLDetailsImports(lclCorrection.getId(), lclCorrection.getLclFileNumber().getId());
        } else {
            model = getLCLDetails(lclCorrection.getId(), lclCorrection.getLclFileNumber().getId());
        }
        String blNo = model.getBlNumber();
        model.setCorrection(true);
        if (isManifest) {
            model.setManifest(true);
        }
        model.setReportingDate(model.getSailDate());
        model.setPostedDate(accrualsDAO.getPostedDate(model.getReportingDate()));
        if (CommonUtils.in(lclCorrection.getType().getCode(), "A", "Y")) {
            Map<String, Double> arCustomers = new HashMap<String, Double>();
            Map<String, String> arCustomersBillToParty = new HashMap<String, String>();
            Double newAmount = 0.00, oldAmount = 0.00;
            boolean isCreditDebitNote;
            for (LclCorrectionCharge lclCorrectionCharge : lclCorrection.getLclCorrectionChargeCollection()) {
                String bkgAcBillToParty = lclCorrectionChargeDAO.getArBillToParty(lclCorrection.getLclFileNumber().getId(),
                        lclCorrectionCharge.getGlMapping().getId(), lclCorrectionCharge.getOldAmount().toString(),null);
                model.setBluescreenChargeCode(lclCorrectionCharge.getGlMapping().getBlueScreenChargeCode());
                model.setChargeCode(lclCorrectionCharge.getGlMapping().getChargeCode());
                model.setGlAccount(glMappingDAO.dervieGlAccount(model.getChargeCode(), model.getShipmentType(), model.getTerminal(), "R"));
                if ("001".equalsIgnoreCase(lclCorrectionCharge.getLclCorrection().getCode().getCode()) && bkgAcBillToParty != null && !bkgAcBillToParty.equalsIgnoreCase(lclCorrectionCharge.getBillToParty())) {
                    model.setBillToParty(bkgAcBillToParty);
                    LclBooking lclBooking = lclCorrection.getLclFileNumber().getLclBooking();
                    if (CommonUtils.isEqualIgnoreCase(bkgAcBillToParty, "A")) {
                        model.setCustomerNumber(lclBooking.getSupAcct().getAccountno());
                        model.setCustomerName(lclBooking.getSupAcct().getAccountName());
                    } else if (CommonUtils.isEqualIgnoreCase(bkgAcBillToParty, "C")) {
                        model.setCustomerNumber(lclBooking.getConsAcct().getAccountno());
                        model.setCustomerName(lclBooking.getConsAcct().getAccountName());
                    } else if (CommonUtils.isEqualIgnoreCase(bkgAcBillToParty, "F")) {
                        model.setCustomerNumber(lclBooking.getFwdAcct().getAccountno());
                        model.setCustomerName(lclBooking.getFwdAcct().getAccountName());
                    } else if (CommonUtils.isEqualIgnoreCase(bkgAcBillToParty, "S")) {
                        model.setCustomerNumber(lclBooking.getShipAcct().getAccountno());
                        model.setCustomerName(lclBooking.getShipAcct().getAccountName());
                    } else if (CommonUtils.isEqualIgnoreCase(bkgAcBillToParty, "T")) {
                        model.setCustomerNumber(lclBooking.getThirdPartyAcct().getAccountno());
                        model.setCustomerName(lclBooking.getThirdPartyAcct().getAccountName());
                    } else if (CommonUtils.isEqualIgnoreCase(bkgAcBillToParty, "N")) {
                        model.setCustomerNumber(lclBooking.getNotyAcct().getAccountno());
                        model.setCustomerName(lclBooking.getNotyAcct().getAccountName());
                    }
                    model.setAmount(lclCorrectionCharge.getOldAmount().doubleValue());
                    isCreditDebitNote = accountingDAO.isCreditDebitNote(model.getCustomerNumber());
                    if (isCreditDebitNote && !model.getBlNumber().contains("CN")) {
                        if (isManifest) {
                            model.setBlNumber(model.getBlNumber() + "CN" + lclCorrection.getCorrectionNo());
                        } else {
                            model.setBlNumber(transactionLedgerDAO.getBlNumberByNoticeNumber(model.getBlNumber(), FclBlConstants.CNA00
                                    + lclCorrection.getCorrectionNo(), model.getCustomerNumber()));
                        }
                    }
                    double oldAmt = model.getAmount();
                    if (arCustomers.containsKey(model.getCustomerNumber())) {
                        arCustomers.put(model.getCustomerNumber(), arCustomers.get(model.getCustomerNumber()) + (-oldAmt));
                    } else {
                        arCustomers.put(model.getCustomerNumber(), (-oldAmt));
                        arCustomersBillToParty.put(model.getCustomerNumber(), model.getBillToParty());
                    }
                    createLclArSubledgers(model, moduleName, lclCorrectionCharge, -oldAmt, user);
                }
                if ("001".equalsIgnoreCase(lclCorrectionCharge.getLclCorrection().getCode().getCode())) {
                    model.setAmount(lclCorrectionCharge.getNewAmount().doubleValue());
                } else {
                    model.setAmount(lclCorrectionCharge.getNewAmount().subtract(lclCorrectionCharge.getOldAmount()).doubleValue());
                }
                newAmount += lclCorrectionCharge.getNewAmount().doubleValue();
                oldAmount += lclCorrectionCharge.getOldAmount().doubleValue();
                model.setBillToParty(lclCorrectionCharge.getBillToParty());
                if (CommonUtils.isEqualIgnoreCase(lclCorrectionCharge.getBillToParty(), "A")) {
                    model.setCustomerNumber(model.getAgentNo());
                    model.setCustomerName(model.getAgentName());
                } else if (CommonUtils.isEqualIgnoreCase(lclCorrectionCharge.getBillToParty(), "C")) {
                    model.setCustomerNumber(lclCorrection.getConsAcct().getAccountno());
                    model.setCustomerName(lclCorrection.getConsAcct().getAccountName());
                } else if (CommonUtils.isEqualIgnoreCase(lclCorrectionCharge.getBillToParty(), "F")) {
                    model.setCustomerNumber(model.getForwarderNo());
                    model.setCustomerName(model.getForwarderName());
                } else if (CommonUtils.isEqualIgnoreCase(lclCorrectionCharge.getBillToParty(), "S")) {
                    model.setCustomerNumber(model.getShipperNo());
                    model.setCustomerName(model.getShipperNo());
                } else if (CommonUtils.isEqualIgnoreCase(lclCorrectionCharge.getBillToParty(), "T")) {
                    model.setCustomerNumber(lclCorrection.getThirdPartyAcct().getAccountno());
                    model.setCustomerName(lclCorrection.getThirdPartyAcct().getAccountName());
                } else if (CommonUtils.isEqualIgnoreCase(lclCorrectionCharge.getBillToParty(), "N")) {
                    model.setCustomerNumber(lclCorrection.getNotyAcct().getAccountno());
                    model.setCustomerName(lclCorrection.getNotyAcct().getAccountName());
                }
                isCreditDebitNote = accountingDAO.isCreditDebitNote(model.getCustomerNumber());
                if (isCreditDebitNote && !model.getBlNumber().contains("CN")) {
                    if (isManifest) {
                        model.setBlNumber(model.getBlNumber() + "CN" + lclCorrection.getCorrectionNo());
                    } else {
                        model.setBlNumber(transactionLedgerDAO.getBlNumberByNoticeNumber(model.getBlNumber(), FclBlConstants.CNA00
                                + lclCorrection.getCorrectionNo(), model.getCustomerNumber()));
                    }
                }
                double amount = model.isManifest() ? model.getAmount() : -model.getAmount();
                if (arCustomers.containsKey(model.getCustomerNumber())) {
                    arCustomers.put(model.getCustomerNumber(), arCustomers.get(model.getCustomerNumber()) + (amount));
                } else {
                    arCustomers.put(model.getCustomerNumber(), (amount));
                    arCustomersBillToParty.put(model.getCustomerNumber(), lclCorrectionCharge.getBillToParty());
                }
                createLclArSubledgers(model, moduleName, lclCorrectionCharge, amount, user);
            }
            if (approved) {
                String noteType = oldAmount > newAmount ? FclBlConstants.CREDTINOTE : FclBlConstants.DEBITNOTE;
                if (moduleName.equalsIgnoreCase("Imports")) {
                    blNo = model.getDockReceipt();
                }
                lclUtils.insertCreditDebitNote(blNo, String.valueOf(lclCorrection.getCorrectionNo()), model.getCustomerName(),
                        model.getCustomerNumber(), noteType);
            }
            model.setArCustomers(arCustomers);
            model.setArCustomersBillToParty(arCustomersBillToParty);
            createLclArInvoice(model, user, lclCorrection);
        } else {
            //LclBlAcDAO lclBlAcDAO = new LclBlAcDAO();
            //LclBLPieceDAO lclBlPieceDAO = new LclBLPieceDAO();
            //List<LclBlAc> lclBlAcList = lclBlAcDAO.getLclChargesByFileNumberAsc(lclCorrectionForm.getFileId());
            //BigInteger pieceCount = lclBlPieceDAO.getPieceCountByFileId(lclCorrectionForm.getFileId());
            //List<LCLCorrectionChargeBean> lclCorrectionChargesList = lclUtils.getFormattedCorrectionCharges(lclBlAcList,correctionId,fileId,pieceCount.intValue());
        }
    }

    public void createLclArSubledgers(ManifestModel model, String moduleName, LclCorrectionCharge lclCorrectionCharge,
            double amount, User user) throws Exception {
        SubledgerDAO subledgerDAO = new SubledgerDAO();
        TransactionLedger arSubledger = new TransactionLedger();
        arSubledger.setTransactionDate(model.getReportingDate());
        arSubledger.setSailingDate(model.getReportingDate());
        arSubledger.setPostedDate(model.getPostedDate());
        arSubledger.setTransactionAmt(amount);
        arSubledger.setBalance(amount);
        arSubledger.setBalanceInProcess(amount);
        arSubledger.setTransactionType(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE);
        arSubledger.setSubledgerSourceCode(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE + "-CN");
        arSubledger.setStatus(STATUS_OPEN);
        arSubledger.setCustNo(model.getCustomerNumber());
        arSubledger.setCustName(model.getCustomerName());
        if (moduleName.equalsIgnoreCase("Imports")) {
            arSubledger.setInvoiceNumber(model.getDockReceipt());
        }
        arSubledger.setBillLaddingNo(model.getBlNumber());
        arSubledger.setBookingNo(model.getBookingNumber());
        arSubledger.setDocReceipt(model.getDockReceipt());
        arSubledger.setVoyageNo(model.getVoyageNumber());
        arSubledger.setContainerNo(model.getContainerNumbers());
        arSubledger.setMasterBl(model.getMasterBl());//Name changed as house in bl instead of master
        arSubledger.setSubHouseBl(model.getSubhouseBl());
        arSubledger.setShipmentType(model.getShipmentType());
        if (CommonUtils.isEqualIgnoreCase(model.getChargeCode(), FclBlConstants.INTRAMP)) {
            arSubledger.setChargeCode(FclBlConstants.INTMDL);
        } else {
            arSubledger.setChargeCode(model.getChargeCode());
        }
        arSubledger.setBlueScreenChargeCode(model.getBluescreenChargeCode());
        arSubledger.setGlAccountNumber(model.getGlAccount());
        arSubledger.setBillTo(model.getBillToParty());
        arSubledger.setReadyToPost(ON);
        arSubledger.setCurrencyCode(CURRENCY_CODE);
        arSubledger.setOrgTerminal(model.getOrigin());
        arSubledger.setDestination(model.getDestination());
        arSubledger.setShipName(model.getShipperName());
        arSubledger.setShipNo(model.getShipperNo());
        arSubledger.setConsName(model.getConsigneeName());
        arSubledger.setConsNo(model.getConsigneeNo());
        arSubledger.setFwdName(model.getForwarderName());
        arSubledger.setFwdNo(model.getForwarderNo());
        arSubledger.setThirdptyName(model.getThirdPartyName());
        arSubledger.setThirdptyNo(model.getThirdPartyNo());
        arSubledger.setAgentName(model.getAgentName());
        arSubledger.setAgentNo(model.getAgentNo());
        arSubledger.setBillingTerminal(model.getTerminal());
        arSubledger.setCustomerReferenceNo(model.getCustomerReferenceNo());
        arSubledger.setVesselNo(model.getVesselNo());
        arSubledger.setCorrectionFlag(YES);
        arSubledger.setChargeId(model.getChargeId());
        arSubledger.setCorrectionNotice(FclBlConstants.CNA00 + lclCorrectionCharge.getLclCorrection().getCorrectionNo());
        arSubledger.setNoticeNumber(FclBlConstants.CNA00 + lclCorrectionCharge.getLclCorrection().getCorrectionNo());
        arSubledger.setManifestFlag(model.isManifest() ? YES : NO);
        arSubledger.setCreatedOn(new Date());
        arSubledger.setCreatedBy(user.getUserId());
        subledgerDAO.save(arSubledger);
    }

    public ManifestModel getAccrualsByFileId(LclBookingAc lclBookingAc, String shipmentType) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        CodetypeDAO codeTypeDAO = new CodetypeDAO();
        Integer codeTypeId = codeTypeDAO.getCodeTypeId("Vessel Codes");
        queryBuilder.append("SELECT IF(ss_head.service_type = 'E','LCLE','LCLI') AS shipmentType,IF(ss_head.service_type = 'E',CONCAT(UPPER(RIGHT(org.");
        queryBuilder.append("un_loc_code, 3)),'-',dest.un_loc_code,'-',file.file_number),CONCAT('IMP-', file.file_number)) AS blNumber,");
        queryBuilder.append("unit_ss.sp_booking_no AS bookingNumber,CONCAT(ss_head.billing_trmnum,'-',headorg.un_loc_code,'-',headdest.un_loc_code,'-',");
        queryBuilder.append("ss_head.schedule_no) AS voyageNumber,IF(ss_head.service_type = 'E',");
        queryBuilder.append("ss_detail.std,ss_detail.sta) AS sailDate,ss_detail.sta AS eta,steamshipline.acct_name AS ");
        queryBuilder.append("streamShipLine,unit.unit_no AS containerNumbers,CONCAT(org.un_loc_name,'/',IF(org_cntry.code = 'US',org_st.code,");
        queryBuilder.append("org_cntry.codedesc),'(',org.un_loc_code,')') AS origin,CONCAT(dest.un_loc_name,'/',IF(dest_cntry.code = 'US',");
        queryBuilder.append("dest_st.code,dest_cntry.codedesc),'(',dest.un_loc_code,')') AS destination,ship.acct_no AS shipperNo,ship.acct_name AS ");
        queryBuilder.append("shipperName,cons.acct_no AS consigneeNo,cons.acct_name AS consigneeName,fwd.acct_no AS forwarderNo,fwd.acct_name AS ");
        queryBuilder.append("forwarderName,tprty.acct_no AS thirdPartyNo,tprty.acct_name AS thirdPartyName,agent.acct_no AS agentNo,");
        queryBuilder.append("agent.acct_name AS agentName,book.billing_terminal AS terminal,vessel.code AS vesselNo,vessel.codedesc AS vesselName, ");
        queryBuilder.append("concat_ws('-',(select rule_name from system_rules where rule_code = 'CompanyCode' limit 1),glmap.GL_Acct,LPAD(IF(glmap.derive_yn != 'N' AND glmap.derive_yn != 'F' AND glmap.suffix_value IN ");
        queryBuilder.append("('B', 'L', 'D', 'F'),IF(glmap.suffix_value = 'B',IF(ss_head.service_type = 'E',term.lcl_export_billing,term.");
        queryBuilder.append("lcl_import_billing),IF(glmap.suffix_value = 'L',IF(ss_head.service_type = 'E',term.lcl_export_loading, term.");
        queryBuilder.append("lcl_import_loading),IF(ss_head.service_type = 'E',term.lcl_export_dockreceipt,''))),glmap.suffix_value), 2, '0')) AS glAccount ");
        queryBuilder.append("FROM lcl_file_number FILE JOIN lcl_booking book ON file.id = book.file_number_id JOIN un_location org ON org.id = book.");
        if ("LCLE".equalsIgnoreCase(shipmentType)) {
            queryBuilder.append("poo_id ");
        } else {
            queryBuilder.append("pol_id ");
        }
        queryBuilder.append("JOIN genericcode_dup org_cntry ON org.countrycode = org_cntry.id LEFT JOIN genericcode_dup org_st ON ");
        queryBuilder.append("org.statecode = org_st.id  JOIN un_location dest ON dest.id = book.fd_id JOIN genericcode_dup dest_cntry ON ");
        queryBuilder.append("dest.countrycode = dest_cntry.id LEFT JOIN genericcode_dup dest_st ON dest.statecode = dest_st.id  LEFT JOIN ");
        queryBuilder.append("trading_partner ship  ON book.ship_acct_no = ship.acct_no LEFT JOIN trading_partner cons ON book.cons_acct_no = ");
        queryBuilder.append("cons.acct_no LEFT JOIN trading_partner fwd ON book.fwd_acct_no = fwd.acct_no LEFT JOIN trading_partner tprty ON ");
        queryBuilder.append("book.third_party_acct_no = tprty.acct_no LEFT JOIN trading_partner agent ON book.agent_acct_no = agent.acct_no LEFT ");
        queryBuilder.append("JOIN lcl_booking_piece book_piece ON book.file_number_id = book_piece.file_number_id LEFT JOIN lcl_booking_piece_unit ");
        queryBuilder.append("unit_pieces ON book_piece.id = unit_pieces.booking_piece_id LEFT JOIN lcl_unit_ss unit_ss ON unit_pieces.lcl_unit_ss_id = ");
        queryBuilder.append("unit_ss.id LEFT JOIN lcl_unit unit ON unit_ss.unit_id = unit.id  LEFT JOIN lcl_ss_header ss_head ON ");
        queryBuilder.append("unit_ss.ss_header_id = ss_head.id LEFT JOIN lcl_ss_detail ss_detail ON ss_head.id = ss_detail.ss_header_id AND ");
        queryBuilder.append("ss_detail.trans_mode = 'V' LEFT JOIN trading_partner steamshipline ON steamshipline.acct_no = ss_detail.sp_acct_no ");
        queryBuilder.append("LEFT JOIN genericcode_dup vessel ON ss_detail.sp_reference_name = vessel.codedesc AND vessel.codetypeid = ");
        queryBuilder.append(codeTypeId);
        queryBuilder.append(" JOIN lcl_booking_ac lbac ON file.id = lbac.file_number_id AND lbac.id = ");
        queryBuilder.append(lclBookingAc.getId());
        queryBuilder.append(" JOIN gl_mapping glmap ON glmap.id = lbac.ap_gl_mapping_id LEFT JOIN terminal_gl_mapping term ON book.billing_terminal ");
        queryBuilder.append("= term.terminal LEFT JOIN un_location headorg ON headorg.id = ss_head.origin_id LEFT JOIN un_location headdest ON headdest.id = ");
        queryBuilder.append("ss_head.destination_id WHERE file.id =  ");
        queryBuilder.append(lclBookingAc.getLclFileNumber().getId());
        queryBuilder.append(" GROUP BY file.id");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(ManifestModel.class));
        query.addScalar("blNumber", StringType.INSTANCE);
        query.addScalar("shipmentType", StringType.INSTANCE);
        query.addScalar("bookingNumber", StringType.INSTANCE);
        query.addScalar("voyageNumber", StringType.INSTANCE);
        query.addScalar("sailDate", DateType.INSTANCE);
        query.addScalar("eta", DateType.INSTANCE);
        query.addScalar("streamShipLine", StringType.INSTANCE);
        query.addScalar("containerNumbers", StringType.INSTANCE);
        query.addScalar("origin", StringType.INSTANCE);
        query.addScalar("destination", StringType.INSTANCE);
        query.addScalar("shipperNo", StringType.INSTANCE);
        query.addScalar("shipperName", StringType.INSTANCE);
        query.addScalar("consigneeNo", StringType.INSTANCE);
        query.addScalar("consigneeName", StringType.INSTANCE);
        query.addScalar("forwarderNo", StringType.INSTANCE);
        query.addScalar("forwarderName", StringType.INSTANCE);
        query.addScalar("thirdPartyNo", StringType.INSTANCE);
        query.addScalar("thirdPartyName", StringType.INSTANCE);
        query.addScalar("agentNo", StringType.INSTANCE);
        query.addScalar("agentName", StringType.INSTANCE);
        query.addScalar("terminal", StringType.INSTANCE);
        query.addScalar("vesselNo", StringType.INSTANCE);
        query.addScalar("vesselName", StringType.INSTANCE);
        query.addScalar("glAccount", StringType.INSTANCE);
        ManifestModel manifestModel = (ManifestModel) query.uniqueResult();
        return manifestModel;
    }

    public void createLclArSubledgers(List<ManifestModel> manifestModalList, User user) throws Exception {
        SubledgerDAO subledgerDAO = new SubledgerDAO();
        Map<String, Double> arCustomers = new HashMap<String, Double>();
        Map<String, String> arCustomersBillToParty = new HashMap<String, String>();
        AccrualsDAO accrualsDAO = new AccrualsDAO();
        int rowCount = 0;
        for (ManifestModel model : manifestModalList) {
            model.setReportingDate(model.getSailDate());
            model.setPostedDate(accrualsDAO.getPostedDate(model.getReportingDate()));
            TransactionLedger arSubledger = new TransactionLedger();
            arSubledger.setTransactionDate(model.getReportingDate());
            arSubledger.setSailingDate(model.getReportingDate());
            arSubledger.setPostedDate(model.getPostedDate());
            double amount = model.isManifest() ? model.getAmount() : -model.getAmount();
            arSubledger.setTransactionAmt(amount);
            arSubledger.setBalance(amount);
            arSubledger.setBalanceInProcess(amount);
            arSubledger.setTransactionType(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE);
            arSubledger.setSubledgerSourceCode(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE + "-" + model.getShipmentType());
            arSubledger.setStatus(STATUS_OPEN);
            arSubledger.setCustNo(model.getCustomerNumber());
            arSubledger.setCustName(model.getCustomerName());
            if (arCustomers.containsKey(model.getCustomerNumber())) {
                arCustomers.put(model.getCustomerNumber(), arCustomers.get(model.getCustomerNumber()) + (amount));
            } else {
                arCustomers.put(model.getCustomerNumber(), (amount));
                arCustomersBillToParty.put(model.getCustomerNumber(), model.getBillToParty());
            }
            if (model.getShipmentType().equalsIgnoreCase("LCLI")) {
                arSubledger.setInvoiceNumber(model.getDockReceipt());
            }
            arSubledger.setBillLaddingNo(model.getBlNumber());
            arSubledger.setBookingNo(model.getBookingNumber());
            arSubledger.setDocReceipt(model.getDockReceipt());
            arSubledger.setVoyageNo(model.getVoyageNumber());
            arSubledger.setContainerNo(model.getContainerNumbers());
            arSubledger.setMasterBl(model.getMasterBl());//Name changed as house in bl instead of master
            arSubledger.setSubHouseBl(model.getSubhouseBl());
            arSubledger.setShipmentType(model.getShipmentType());
            if (CommonUtils.isEqualIgnoreCase(model.getChargeCode(), FclBlConstants.INTRAMP)) {
                arSubledger.setChargeCode(FclBlConstants.INTMDL);

            } else {
                arSubledger.setChargeCode(model.getChargeCode());
            }
            arSubledger.setBlueScreenChargeCode(model.getBluescreenChargeCode());
            arSubledger.setGlAccountNumber(model.getGlAccount());
            arSubledger.setBillTo(model.getBillToParty());
            arSubledger.setReadyToPost(ON);
            arSubledger.setCurrencyCode(CURRENCY_CODE);
            arSubledger.setOrgTerminal(model.getOrigin());
            arSubledger.setDestination(model.getDestination());
            arSubledger.setShipName(model.getShipperName());
            arSubledger.setShipNo(model.getShipperNo());
            arSubledger.setConsName(model.getConsigneeName());
            arSubledger.setConsNo(model.getConsigneeNo());
            arSubledger.setFwdName(model.getForwarderName());
            arSubledger.setFwdNo(model.getForwarderNo());
            arSubledger.setThirdptyName(model.getThirdPartyName());
            arSubledger.setThirdptyNo(model.getThirdPartyNo());
            arSubledger.setAgentName(model.getAgentName());
            arSubledger.setAgentNo(model.getAgentNo());
            arSubledger.setBillingTerminal(model.getTerminal());
            arSubledger.setCustomerReferenceNo(model.getCustomerReferenceNo());
            arSubledger.setVesselNo(model.getVesselNo());
            arSubledger.setCorrectionFlag(NO);
            arSubledger.setChargeId(model.getChargeId());
            arSubledger.setCorrectionNotice(FclBlConstants.CNA0);
            arSubledger.setNoticeNumber(FclBlConstants.CNA0);
            arSubledger.setManifestFlag(model.isManifest() ? YES : NO);
            arSubledger.setCreatedOn(new Date());
            arSubledger.setCreatedBy(user.getUserId());
            subledgerDAO.save(arSubledger);
            if (rowCount + 1 < manifestModalList.size()) {
                ManifestModel nextModel = manifestModalList.get(rowCount + 1);
                if (CommonUtils.isNotEqualIgnoreCase(model.getBlNumber(), nextModel.getBlNumber())) {
                    model.setArCustomers(arCustomers);
                    model.setArCustomersBillToParty(arCustomersBillToParty);
                    createLclArInvoice(model, user, null);
                    arCustomers.clear();
                    arCustomersBillToParty.clear();
                    if (model.isManifest()) {
                        if (CommonUtils.isNotEmpty(model.getForwarderNo())) {
                            checkFFCommission(model, user);
                        }
                        if (CommonUtils.isNotEmpty(model.getAgentNo()) && (CommonUtils.isNotEmpty(model.getForwarderNo())
                                || CommonUtils.isNotEmpty(model.getShipperNo()))) {
                            calculatePBASurcharge(model, user);
                        }
                    }
                }
                rowCount++;
            } else {
                model.setArCustomers(arCustomers);
                model.setArCustomersBillToParty(arCustomersBillToParty);
                createLclArInvoice(model, user, null);
                arCustomers.clear();
                arCustomersBillToParty.clear();
                if (model.isManifest()) {
                    if (CommonUtils.isNotEmpty(model.getForwarderNo())) {
                        checkFFCommission(model, user);
                    }
                    if (CommonUtils.isNotEmpty(model.getAgentNo()) && (CommonUtils.isNotEmpty(model.getForwarderNo())
                            || CommonUtils.isNotEmpty(model.getShipperNo()))) {
                        calculatePBASurcharge(model, user);
                    }
                }
            }
        }
    }

    public void createLclArInvoice(ManifestModel model, User user, LclCorrection lclCorrection) throws Exception {
        synchronized (this) {
            TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
            CustomerAccountingDAO accountingDAO = new CustomerAccountingDAO();
            AccountingTransactionDAO transactionDAO = new AccountingTransactionDAO();
            GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
            String dockReceipt = model.getDockReceipt();
            String blNumber = model.getBlNumber();
            int invoiceSequence = 0;
            String invoiceNumber = null;
            for (String custNo : model.getArCustomers().keySet()) {
                Transaction ar = transactionDAO.getArInvoice(custNo, blNumber, dockReceipt);
                double amount = model.getArCustomers().get(custNo);
                String billToParty = model.getArCustomersBillToParty().get(custNo);
                boolean isCreditDebitNote = accountingDAO.isCreditDebitNote(custNo);
                if (null != ar && ((!model.isCorrection()) || (model.isCorrection() && (!isCreditDebitNote || !model.isManifest())))) {
                    if (CommonUtils.isEqualIgnoreCase(ar.getInvoiceNumber(), "PRE PAYMENT")) {
                        ar.setTransactionDate(model.getReportingDate());
                        ar.setPostedDate(model.getPostedDate());
                        StringBuilder queryBuilder = new StringBuilder("update ar_transaction_history");
                        queryBuilder.append(" set bl_number = '").append(model.getBlNumber()).append("',");
                        queryBuilder.append("invoice_number = '").append(ar.getDocReceipt()).append("',");
                        queryBuilder.append("invoice_or_bl = '").append(model.getBlNumber()).append("'");
                        queryBuilder.append(" where customer_number='").append(ar.getCustNo()).append("'");
                        queryBuilder.append(" and invoice_or_bl = '").append(ar.getDocReceipt()).append("'");
                        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();

                        queryBuilder = new StringBuilder("update document_store_log");
                        queryBuilder.append(" set document_id = '").append(ar.getCustNo()).append("-").append(model.getBlNumber()).append("'");
                        queryBuilder.append(" where document_id = '").append(ar.getCustNo()).append("-").append(ar.getBillLaddingNo()).append("'");
                        queryBuilder.append(" and screen_name = 'INVOICE'");
                        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();

                        queryBuilder = new StringBuilder("update notes");
                        queryBuilder.append(" set module_ref_id = '").append(ar.getCustNo()).append("-").append(model.getBlNumber()).append("'");
                        queryBuilder.append(" where module_ref_id = '").append(ar.getCustNo()).append("-").append(ar.getBillLaddingNo()).append("'");
                        queryBuilder.append(" and module_id = '").append(NotesConstants.AR_INVOICE).append("'");
                        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
                        ar.setBillLaddingNo(model.getBlNumber());
                        ar.setInvoiceNumber(model.getDockReceipt());
                    }
                    ar.setTransactionAmt(ar.getTransactionAmt() + amount);
                    ar.setBalance(ar.getBalance() + amount);
                    ar.setBalanceInProcess(ar.getBalanceInProcess() + amount);
                    ar.setUpdatedOn(new Date());
                    ar.setUpdatedBy(user.getUserId());
                } else {
                    ar = new Transaction();
                    if (model.isManifest()) {
                        if (model.getShipmentType().equalsIgnoreCase("LCLI")) {
                            ar.setInvoiceNumber(model.getDockReceipt());
                        } else {
                            invoiceSequence = Integer.parseInt(genericCodeDAO.getFieldByCodeAndCodetypeId(CODE_TYPE_DESCRIPTION_INVOICENO, GENERIC_CODE_INVOICENO, "Field1"));
                            invoiceSequence++;
                            invoiceNumber = StringUtils.leftPad("" + invoiceSequence, 9, "0");
                            if (blNumber != null) {
                                invoiceNumber = blNumber.substring(0, 3) + "-" + invoiceNumber;
                            }
                            ar.setInvoiceNumber(invoiceNumber);
                        }
                    }
                    ar.setStatus(STATUS_OPEN);
                    ar.setTransactionType(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE);
                    ar.setCustNo(custNo);
                    ar.setCustName(tradingPartnerDAO.getAccountName(custNo));
                    ar.setTransactionAmt(amount);
                    ar.setBalance(amount);
                    ar.setBalanceInProcess(amount);
                    ar.setTransactionDate(model.getReportingDate());
                    ar.setPostedDate(model.getPostedDate());
                    ar.setBillLaddingNo(model.getBlNumber());
                    String creditStatus = accountingDAO.getCreditStatus(ar.getCustNo());
                    ar.setCreditHold(CommonUtils.isEqualIgnoreCase(creditStatus, "In Good Standing") ? NO : YES);
                    ar.setCreatedOn(new Date());
                    ar.setCreatedBy(user.getUserId());
                }
                ar.setSailingDate(model.getReportingDate());
                ar.setEta(model.getEta());
                ar.setSteamShipLine(model.getStreamShipLine());
                ar.setBookingNo(model.getBookingNumber());
                ar.setDocReceipt(model.getDockReceipt());
                ar.setVoyageNo(model.getVoyageNumber());
                ar.setSealNo(model.getSealNumbers());
                ar.setContainerNo(model.getContainerNumbers());
                ar.setMasterBl(model.getMasterBl());//Name changed as house in bl instead of master
                ar.setSubHouseBl(model.getSubhouseBl());
                ar.setBillTo(billToParty);
                //ar.setBlTerms(bl.getStreamShipBl());
                ar.setOrgTerminal(model.getOrigin());
                ar.setDestination(model.getDestination());
                ar.setShipperName(model.getShipperName());
                ar.setShipperNo(model.getShipperNo());
                ar.setConsName(model.getConsigneeName());
                ar.setConsNo(model.getConsigneeNo());
                ar.setFwdName(model.getForwarderName());
                ar.setFwdNo(model.getForwarderNo());
                ar.setThirdptyName(model.getThirdPartyName());
                ar.setThirdptyNo(model.getThirdPartyNo());
                ar.setAgentName(model.getAgentName());
                ar.setAgentNo(model.getAgentNo());
                ar.setCustomerReferenceNo(model.getCustomerReferenceNo());
                ar.setVesselNo(model.getVesselNo());
                ar.setVesselName(model.getVesselName());
                if (model.isCorrection() && lclCorrection != null) {
                    ar.setCorrectionNotice(FclBlConstants.CNA00 + lclCorrection.getCorrectionNo());
                    ar.setCorrectionFlag(YES);
                } else {
                    ar.setCorrectionNotice(FclBlConstants.CNA0);
                    ar.setCorrectionFlag(NO);
                }
                ar.setManifestFlag(model.isManifest() || model.isCorrection() ? YES : NO);
                transactionDAO.saveOrUpdate(ar);
                model.setAr(ar);
                model.setAmount(amount);
                if (model.getShipmentType().equalsIgnoreCase("LCLE")) {
                    updateLclArSubledgerInvoiceNumber(ar.getBillLaddingNo(), custNo, ar.getInvoiceNumber());
                }
                createLclArHistory(model, user, lclCorrection);
            }
            if (model.isManifest()) {
                //Update invoice number in generic code
                genericCodeDAO.updateFieldByCodeAndType("Field1", String.valueOf(invoiceSequence), CODE_TYPE_DESCRIPTION_INVOICENO, GENERIC_CODE_INVOICENO);
            }
        }
    }

    public void createLclArHistory(ManifestModel model, User user, LclCorrection lclCorrection) throws Exception {
        ArTransactionHistoryDAO historyDAO = new ArTransactionHistoryDAO();
        Transaction ar = model.getAr();
        ArTransactionHistory history = new ArTransactionHistory();
        history.setCustomerNumber(ar.getCustNo());
        history.setBlNumber(ar.getBillLaddingNo());
        history.setInvoiceNumber(ar.getInvoiceNumber());
        history.setInvoiceDate(ar.getTransactionDate());
        history.setPostedDate(model.getPostedDate());
        history.setTransactionAmount(model.getAmount());
        history.setCustomerReferenceNumber(ar.getCustomerReferenceNo());
        history.setVoyageNumber(ar.getVoyageNo());
        if (model.getShipmentType().equalsIgnoreCase("LCLI")) {
            history.setTransactionType(model.isCorrection() ? "LCL CN" : model.isManifest() ? "LCLI" : "LCL VOID");
            history.setTransactionDate(model.getEta());
        } else {
            history.setTransactionType(model.isCorrection() ? "LCL CN" : model.isManifest() ? "LCL BL" : "LCL VOID");
            history.setTransactionDate(new Date());
        }
        history.setCreatedBy(user.getLoginName());
        history.setCreatedDate(new Date());
        if (model.isCorrection() && lclCorrection != null) {
            history.setCorrectionNotice(FclBlConstants.CNA00 + lclCorrection.getCorrectionNo());
        } else {
            history.setCorrectionNotice(FclBlConstants.CNA0);
        }
        historyDAO.save(history);
    }

    public void createLclAccruals(LclBookingAc lclBookingAc, String shipmentType, String statusType) throws Exception {
        AccrualsDAO accrualsDAO = new AccrualsDAO();
        TransactionLedger accruals = new TransactionLedger();
        Date d = new Date();
        accruals.setTransactionAmt(lclBookingAc.getApAmount().doubleValue());
        accruals.setBalance(accruals.getTransactionAmt());
        accruals.setBalanceInProcess(accruals.getTransactionAmt());
        accruals.setTransactionType(TRANSACTION_TYPE_ACCRUALS);
        accruals.setSubledgerSourceCode(SUB_LEDGER_CODE_ACCRUALS);
        accruals.setStatus(statusType);
        if (lclBookingAc.getSupAcct() != null) {
            accruals.setCustNo(lclBookingAc.getSupAcct().getAccountno());
            accruals.setCustName(lclBookingAc.getSupAcct().getAccountName());
        }
        if (CommonUtils.isNotEmpty(lclBookingAc.getInvoiceNumber())) {
            accruals.setInvoiceNumber(lclBookingAc.getInvoiceNumber().toString());
        }
        if (lclBookingAc.getLclFileNumber() != null) {
            accruals.setDocReceipt(lclBookingAc.getLclFileNumber().getFileNumber());
            ManifestModel model = getAccrualsByFileId(lclBookingAc, shipmentType);
            accruals.setBillLaddingNo(model.getBlNumber());
            accruals.setBookingNo(model.getBookingNumber());
            accruals.setVoyageNo(model.getVoyageNumber());
            accruals.setContainerNo(model.getContainerNumbers());
            //arSubledger.setMasterBl(model.getHouseBl());//Name changed as house in bl instead of master
            //arSubledger.setSubHouseBl(model.getStreamShipBl());
            accruals.setShipmentType(model.getShipmentType());
            accruals.setOrgTerminal(model.getOrigin());
            accruals.setDestination(model.getDestination());
            accruals.setShipName(model.getShipperName());
            accruals.setShipNo(model.getShipperNo());
            accruals.setConsName(model.getConsigneeName());
            accruals.setConsNo(model.getConsigneeNo());
            accruals.setFwdName(model.getForwarderName());
            accruals.setFwdNo(model.getForwarderNo());
            accruals.setThirdptyName(model.getThirdPartyName());
            accruals.setThirdptyNo(model.getThirdPartyNo());
            accruals.setAgentName(model.getAgentName());
            accruals.setAgentNo(model.getAgentNo());
            accruals.setBillingTerminal(model.getTerminal());
            accruals.setCustomerReferenceNo(model.getCustomerReferenceNo());
            accruals.setVesselNo(model.getVesselNo());
            if (lclBookingAc.getLclFileNumber().getStatus().equalsIgnoreCase("M")) {
                model.setReportingDate(model.getSailDate());
                accruals.setSailingDate(model.getReportingDate());
                accruals.setTransactionDate(model.getReportingDate());
            } else {
                accruals.setTransactionDate(d);
            }
            accruals.setGlAccountNumber(model.getGlAccount());
            if (CommonUtils.isNotEmpty(model.getGlAccount()) && model.getGlAccount().contains("-")) {
                String[] splittedValues = model.getGlAccount().split("-");
                accruals.setTerminal(splittedValues[2]);
            }
        }
        //accrual.setMasterBl(bl.getHouseBl());//Name changed in bl instead of master, it is house
        //accrual.setSubHouseBl(bl.getStreamShipBl());
        if (CommonUtils.isEqualIgnoreCase(lclBookingAc.getApglMapping().getChargeCode(), FclBlConstants.INTRAMP)) {
            accruals.setChargeCode(FclBlConstants.INTMDL);
        } else {
            accruals.setChargeCode(lclBookingAc.getApglMapping().getChargeCode());
        }
        accruals.setBlueScreenChargeCode(lclBookingAc.getApglMapping().getBlueScreenChargeCode());
        accruals.setBillTo(YES);
        accruals.setReadyToPost(ON);
        accruals.setCurrencyCode(CURRENCY_CODE);
        //accrual.setDescription(cost.getCostComments())
        //accrual.setBlTerms(bl.getStreamShipBl());
        accruals.setCostId(lclBookingAc.getId().intValue());
        accruals.setManifestFlag(NO);
        accruals.setCreatedOn(d);
        accruals.setCreatedBy(lclBookingAc.getEnteredBy().getUserId());
        accrualsDAO.save(accruals);
    }

    public void updateLclAccruals(LclBookingAc lclBookingAc, User user, String shipmentType) throws Exception {
        LclUtils lclUtils = new LclUtils();
        AccrualsDAO accrualsDAO = new AccrualsDAO();
        TransactionLedger accruals = accrualsDAO.getAccrualsByCostIdAndDR(lclBookingAc.getId().intValue(), lclBookingAc.getLclFileNumber().getFileNumber());
        if (accruals == null) {
            createLclAccruals(lclBookingAc, shipmentType, STATUS_OPEN);
            lclUtils.insertLCLBookingAcTrans(lclBookingAc, TRANSACTION_TYPE_ACCRUALS, "A", "", lclBookingAc.getApAmount(), user);
        } else {
            accruals.setTransactionAmt(lclBookingAc.getApAmount().doubleValue());
            if (lclBookingAc.getSupAcct() != null) {
                accruals.setCustNo(lclBookingAc.getSupAcct().getAccountno());
                accruals.setCustName(lclBookingAc.getSupAcct().getAccountName());
            }
            if (CommonUtils.isNotEmpty(lclBookingAc.getInvoiceNumber())) {
                accruals.setInvoiceNumber(lclBookingAc.getInvoiceNumber().toString());
            }
            if (CommonUtils.isEqualIgnoreCase(lclBookingAc.getApglMapping().getChargeCode(), FclBlConstants.INTRAMP)) {
                accruals.setChargeCode(FclBlConstants.INTMDL);
            } else {
                accruals.setChargeCode(lclBookingAc.getApglMapping().getChargeCode());
            }
            accruals.setBlueScreenChargeCode(lclBookingAc.getApglMapping().getBlueScreenChargeCode());
            accruals.setUpdatedOn(new Date());
            accruals.setUpdatedBy(lclBookingAc.getModifiedBy().getUserId());
            accrualsDAO.update(accruals);
            if (CommonUtils.isNotEmpty(lclBookingAc.getLclBookingAcTaList())) {
                LCLBookingAcTransDAO lclBookingAcTransDAO = new LCLBookingAcTransDAO();
                LclBookingAcTa lclBookingAcTa = lclBookingAc.getLclBookingAcTaList().get(0);
                LclBookingAcTrans lclBookingAcTrans = lclBookingAcTa.getLclBookingAcTrans();
                lclBookingAcTrans.setAmount(lclBookingAc.getApAmount());
                lclBookingAcTransDAO.update(lclBookingAcTrans);
            }
        }
    }

    public void updateLclUnitAccruals(LclSsAc lclSsAc) throws Exception {
        AccrualsDAO accrualsDAO = new AccrualsDAO();
        String concatenatedVoyageNumber = lclSsAc.getLclSsHeader().getBillingTerminal().getTrmnum() + "-"
                + lclSsAc.getLclSsHeader().getOrigin().getUnLocationCode() + "-"
                + lclSsAc.getLclSsHeader().getDestination().getUnLocationCode() + "-"
                + lclSsAc.getLclSsHeader().getScheduleNo();
        TransactionLedger accruals = accrualsDAO.getAccrualsByCostIdVnCn(lclSsAc.getId().intValue(), concatenatedVoyageNumber,
                lclSsAc.getLclUnitSs().getLclUnit().getUnitNo());
        if (accruals != null) {
            accruals.setTransactionAmt(lclSsAc.getApAmount().doubleValue());
            if (lclSsAc.getApAcctNo() != null) {
                accruals.setCustNo(lclSsAc.getApAcctNo().getAccountno());
                accruals.setCustName(lclSsAc.getApAcctNo().getAccountName());
            }
            if (CommonUtils.isNotEmpty(lclSsAc.getApReferenceNo())) {
                accruals.setInvoiceNumber(lclSsAc.getApReferenceNo());
            } else {
                accruals.setInvoiceNumber("");
            }
            if (CommonUtils.isEqualIgnoreCase(lclSsAc.getApGlMappingId().getChargeCode(), FclBlConstants.INTRAMP)) {
                accruals.setChargeCode(FclBlConstants.INTMDL);
            } else {
                accruals.setChargeCode(lclSsAc.getApGlMappingId().getChargeCode());
            }
            accruals.setBlueScreenChargeCode(lclSsAc.getApGlMappingId().getBlueScreenChargeCode());
            accruals.setUpdatedOn(new Date());
            accruals.setUpdatedBy(lclSsAc.getModifiedByUserId().getUserId());
            accrualsDAO.update(accruals);
        }

    }

    public void deleteLclAccruals(Integer costId, String dockReceipt) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("delete from transaction_ledger");
        queryBuilder.append(" where cost_id = ").append(costId);
        queryBuilder.append(" and drcpt = '").append(dockReceipt).append("'");
        queryBuilder.append(" and transaction_type = '").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append(" and status = '").append(STATUS_OPEN).append("'");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        getCurrentSession().flush();
    }

    public void deleteLclAccrualsByIds(String costIds, String dockReceipt) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("delete from transaction_ledger");
        queryBuilder.append(" where cost_id IN (").append(costIds).append(")");
        queryBuilder.append(" and drcpt = '").append(dockReceipt).append("'");
        queryBuilder.append(" and transaction_type = '").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append(" and status = '").append(STATUS_OPEN).append("'");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        getCurrentSession().flush();
    }

    public void deleteLclUnitAccruals(Integer costId, String voyageNo, String containerNo) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("delete from transaction_ledger");
        queryBuilder.append(" where cost_id = ").append(costId);
        queryBuilder.append(" and Voyage_No = '").append(voyageNo).append("'");
        queryBuilder.append(" and Container_No = '").append(containerNo).append("'");
        queryBuilder.append(" and transaction_type = '").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append(" and status = '").append(STATUS_OPEN).append("'");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        getCurrentSession().flush();
    }

    public void deleteLclAccruals(String dockReceipt) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("delete from transaction_ledger");
        queryBuilder.append(" where drcpt = '").append(dockReceipt).append("'");
        queryBuilder.append(" and transaction_type = '").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append(" and status = '").append(STATUS_OPEN).append("'");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        getCurrentSession().flush();
    }

    public void updateLclAccruals(String fileNumbers, ManifestModel model) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update transaction_ledger ");
        queryBuilder.append("set Voyage_No  = '").append(model.getVoyageNumber()).append("',booking_no='").append(model.getBookingNumber());
        queryBuilder.append("',Container_No  = '").append(model.getContainerNumbers()).append("',sailing_date = '");
        queryBuilder.append(model.getSailDate()).append("' where transaction_type = 'AC' and drcpt IN(").append(fileNumbers).append(")");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        getCurrentSession().flush();
    }

    public void updateLclArSubledgerInvoiceNumber(String blNo, String customerNo, String invoiceNo) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update transaction_ledger ");
        queryBuilder.append("set Invoice_number  = '").append(invoiceNo);
        queryBuilder.append("' where cust_no = '").append(customerNo).append("' and Bill_Ladding_No ='").append(blNo).append("'");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        getCurrentSession().flush();
    }

    public void checkFFCommission(ManifestModel model, User user) throws Exception {
        RefTerminalDAO refterminaldao = new RefTerminalDAO();
        PortsDAO portsdao = new PortsDAO();
        String trmnum = null, eciPortCodeFd = null, eciPortCodePod = null;
        String rateType = model.getRateType();
        if (rateType.equalsIgnoreCase("R")) {
            rateType = "Y";
        }
        trmnum = refterminaldao.getTrmnum(model.getPooCode(), rateType);
        if (CommonUtils.isEmpty(model.getFdCode())) {
            model.setFdCode(model.getPodCode());
        }
        eciPortCodeFd = portsdao.getFieldsByUnlocCode("eciportcode", model.getFdCode());
        eciPortCodePod = eciPortCodeFd;
        if (!model.getFdCode().equals(model.getPodCode())) {
            eciPortCodePod = portsdao.getFieldsByUnlocCode("eciportcode", model.getPodCode());
        }
        String databaseSchema = LoadLogisoftProperties.getProperty("elite.database.name");
        LCLRatesDAO lclratesdao = new LCLRatesDAO(databaseSchema);
        double ffocom = lclratesdao.getFFCommision(trmnum, eciPortCodePod, eciPortCodeFd);
        if (ffocom > 0.0) {
            LclBlAcDAO lclBlAcDAO = new LclBlAcDAO();
            GlMappingDAO glMappingDAO = new GlMappingDAO();
            Double totalAmount = lclBlAcDAO.getAllOfrAndTTCharges(model.getFileId(), 0);
            totalAmount = (totalAmount * ffocom) / 100;
            GlMapping glmapping = glMappingDAO.findByChargeCode(CommonConstants.FFCOMM_CHARGECODE, model.getShipmentType(), "AC");
            String glAccount = glMappingDAO.dervieGlAccount(CommonConstants.FFCOMM_CHARGECODE, model.getShipmentType(), model.getTerminal(),
                    glmapping.getRevExp());
            insertLclFFCommissionAndPBA(model, totalAmount, glmapping, glAccount, user);
        }
    }

    public void calculatePBASurcharge(ManifestModel model, User user) throws Exception {
        LclBlAcDAO lclBlAcDAO = new LclBlAcDAO();
        Double totalAmount = lclBlAcDAO.getChargeForPBA(model.getFileId(), 1);
        if (totalAmount > 0.00) {
            GlMappingDAO glMappingDAO = new GlMappingDAO();
            int pbaper = Integer.parseInt(LoadLogisoftProperties.getProperty("pbaSurchargePercentage"));
            totalAmount = (totalAmount * pbaper) / 100;
            GlMapping glmapping = glMappingDAO.findByChargeCode(CommonConstants.PBA_CHARGECODE, model.getShipmentType(), "AC");
            String glAccount = glMappingDAO.dervieGlAccount(CommonConstants.PBA_CHARGECODE, model.getShipmentType(), model.getTerminal(),
                    glmapping.getRevExp());
            insertLclFFCommissionAndPBA(model, totalAmount, glmapping, glAccount, user);
        }
    }
    //Import Units Auto costing

    public void insertLclFFCommissionAndPBA(ManifestModel model, double amount, GlMapping glmapping, String glAcct, User user) throws Exception {
        AccrualsDAO accrualsDAO = new AccrualsDAO();
        LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
        TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
        TransactionLedger accruals = new TransactionLedger();
        LclUtils lclUtils = new LclUtils();
        Date d = new Date();
        boolean insertAcTrans = false;
        LclBookingAc lclBookingAc = lclCostChargeDAO.manaualChargeValidate(model.getFileId(), glmapping.getChargeCode(), true);
        if (lclBookingAc == null) {
            lclBookingAc = new LclBookingAc();
            insertAcTrans = true;
            lclBookingAc.setLclFileNumber(new LclFileNumber(model.getFileId()));
            lclBookingAc.setApglMapping(glmapping);
            lclBookingAc.setArglMapping(glmapping);
            lclBookingAc.setEnteredBy(user);
            lclBookingAc.setEnteredDatetime(d);
            lclBookingAc.setTransDatetime(d);
            lclBookingAc.setManualEntry(true);
            lclBookingAc.setAdjustmentAmount(BigDecimal.ZERO);
            lclBookingAc.setArAmount(BigDecimal.ZERO);
            lclBookingAc.setRatePerUnitUom("FL");
            lclBookingAc.setBundleIntoOf(false);
            lclBookingAc.setPrintOnBl(true);
        }
        lclBookingAc.setApAmount(new BigDecimal(amount));
        lclBookingAc.setModifiedBy(user);
        lclBookingAc.setModifiedDatetime(d);
        accruals.setTransactionAmt(amount);
        accruals.setBalance(accruals.getTransactionAmt());
        accruals.setBalanceInProcess(accruals.getTransactionAmt());
        accruals.setTransactionType(TRANSACTION_TYPE_ACCRUALS);
        accruals.setSubledgerSourceCode(SUB_LEDGER_CODE_ACCRUALS);
        accruals.setStatus(STATUS_OPEN);
        if (glmapping.getChargeCode().equalsIgnoreCase(CommonConstants.FFCOMM_CHARGECODE)) {
            accruals.setCustNo(model.getForwarderNo());
            accruals.setCustName(model.getForwarderName());
            lclBookingAc.setSupAcct(tradingPartnerDAO.findById(model.getForwarderNo()));
        } else {
            accruals.setCustNo(model.getAgentNo());
            accruals.setCustName(model.getAgentName());
            lclBookingAc.setSupAcct(tradingPartnerDAO.findById(model.getAgentNo()));
        }
        accruals.setDocReceipt(model.getDockReceipt());
        accruals.setBillLaddingNo(model.getBlNumber());
        accruals.setBookingNo(model.getBookingNumber());
        accruals.setVoyageNo(model.getVoyageNumber());
        accruals.setContainerNo(model.getContainerNumbers());
        //arSubledger.setMasterBl(model.getHouseBl());//Name changed as house in bl instead of master
        //arSubledger.setSubHouseBl(model.getStreamShipBl());
        accruals.setShipmentType(model.getShipmentType());
        accruals.setOrgTerminal(model.getOrigin());
        accruals.setDestination(model.getDestination());
        accruals.setShipName(model.getShipperName());
        accruals.setShipNo(model.getShipperNo());
        accruals.setConsName(model.getConsigneeName());
        accruals.setConsNo(model.getConsigneeNo());
        accruals.setFwdName(model.getForwarderName());
        accruals.setFwdNo(model.getForwarderNo());
        accruals.setThirdptyName(model.getThirdPartyName());
        accruals.setThirdptyNo(model.getThirdPartyNo());
        accruals.setAgentName(model.getAgentName());
        accruals.setAgentNo(model.getAgentNo());
        accruals.setBillingTerminal(model.getTerminal());
        accruals.setCustomerReferenceNo(model.getCustomerReferenceNo());
        accruals.setVesselNo(model.getVesselNo());
        if (model.getSailDate() != null) {
            model.setReportingDate(model.getSailDate());
        } else {
            model.setReportingDate(d);
        }
        model.setPostedDate(accrualsDAO.getPostedDate(model.getReportingDate()));
        accruals.setTransactionDate(model.getReportingDate());
        accruals.setSailingDate(model.getReportingDate());
        accruals.setPostedDate(model.getPostedDate());
        accruals.setGlAccountNumber(glAcct);
        //accrual.setMasterBl(bl.getHouseBl());//Name changed in bl instead of master, it is house
        //accrual.setSubHouseBl(bl.getStreamShipBl());
        accruals.setChargeCode(glmapping.getChargeCode());
        accruals.setBlueScreenChargeCode(glmapping.getBlueScreenChargeCode());
        accruals.setBillTo(YES);
        accruals.setReadyToPost(ON);
        accruals.setCurrencyCode(CURRENCY_CODE);
        //accrual.setDescription(cost.getCostComments())
        //accrual.setBlTerms(bl.getStreamShipBl());
        lclCostChargeDAO.saveOrUpdate(lclBookingAc);
        accruals.setCostId(lclBookingAc.getId().intValue());
        accruals.setManifestFlag(NO);
        accruals.setCreatedOn(d);
        accruals.setCreatedBy(user.getUserId());
        accrualsDAO.save(accruals);
        if (insertAcTrans) {
            lclUtils.insertLCLBookingAcTrans(lclBookingAc, "AC", "A", "", lclBookingAc.getApAmount(), user);
        }
    }

    public void createLclAccrualsforAutoCosting(List<LclSsAc> lclSsAcList) throws Exception {
        AccrualsDAO accrualsDAO = new AccrualsDAO();
        Date d = new Date();
        if (lclSsAcList != null && lclSsAcList.size() > 0) {
            for (LclSsAc lclSsAc : lclSsAcList) {
                TransactionLedger accruals = new TransactionLedger();
                accruals.setTransactionAmt(lclSsAc.getApAmount().doubleValue());
                accruals.setBalance(accruals.getTransactionAmt());
                accruals.setBalanceInProcess(accruals.getTransactionAmt());
                accruals.setTransactionType(TRANSACTION_TYPE_ACCRUALS);
                accruals.setSubledgerSourceCode(SUB_LEDGER_CODE_ACCRUALS);
                accruals.setStatus(STATUS_OPEN);
                accruals.setInvoiceNumber(lclSsAc.getApReferenceNo());
                if (lclSsAc.getApAcctNo() != null) {
                    accruals.setCustNo(lclSsAc.getApAcctNo().getAccountno());
                    accruals.setCustName(lclSsAc.getApAcctNo().getAccountName());
                }
                if (lclSsAc.getLclUnitSs().getLclUnit() != null) {
                    //   accruals.setDocReceipt(lclBookingAc.getLclFileNumber().getFileNumber());
                    ManifestModel model = getUnitAutoCostAccruals(lclSsAc.getLclUnitSs().getId(), lclSsAc.getApGlMappingId().getId());
                    accruals.setBillLaddingNo(model.getBlNumber());
                    accruals.setBookingNo(model.getBookingNumber());
                    accruals.setVoyageNo(lclSsAc.getLclSsHeader().getBillingTerminal().getTrmnum() + "-"
                            + lclSsAc.getLclSsHeader().getOrigin().getUnLocationCode() + "-"
                            + lclSsAc.getLclSsHeader().getDestination().getUnLocationCode() + "-"
                            + lclSsAc.getLclSsHeader().getScheduleNo());
                    accruals.setContainerNo(lclSsAc.getLclUnitSs().getLclUnit().getUnitNo());
                    //arSubledger.setMasterBl(model.getHouseBl());//Name changed as house in bl instead of master
                    //arSubledger.setSubHouseBl(model.getStreamShipBl());
                    accruals.setShipmentType(model.getShipmentType());
                    accruals.setOrgTerminal(model.getOrigin());
                    accruals.setDestination(model.getDestination());
                    accruals.setShipName(model.getShipperName());
                    accruals.setShipNo(model.getShipperNo());
                    accruals.setConsName(model.getConsigneeName());
                    accruals.setConsNo(model.getConsigneeNo());
//            accruals.setFwdName(model.getForwarderName());
//            accruals.setFwdNo(model.getForwarderNo());
//            accruals.setThirdptyName(model.getThirdPartyName());
//            accruals.setThirdptyNo(model.getThirdPartyNo());
                    accruals.setAgentName(model.getAgentName());
                    accruals.setAgentNo(model.getAgentNo());
                    accruals.setBillingTerminal(model.getTerminal());
//            accruals.setCustomerReferenceNo(model.getCustomerReferenceNo());
                    accruals.setVesselNo(model.getVesselNo());
                    if (lclSsAc.getLclUnitSs().getStatus().equalsIgnoreCase("M")) {
                        model.setReportingDate(model.getSailDate());
                        accruals.setSailingDate(model.getReportingDate());
                        accruals.setTransactionDate(model.getReportingDate());
                    } else {
                        accruals.setTransactionDate(d);
                    }
                    accruals.setGlAccountNumber(model.getGlAccount());
                }
                //accrual.setMasterBl(bl.getHouseBl());//Name changed in bl instead of master, it is house
                //accrual.setSubHouseBl(bl.getStreamShipBl());
                if (CommonUtils.isEqualIgnoreCase(lclSsAc.getApGlMappingId().getChargeCode(), FclBlConstants.INTRAMP)) {
                    accruals.setChargeCode(FclBlConstants.INTMDL);
                } else {
                    accruals.setChargeCode(lclSsAc.getApGlMappingId().getChargeCode());
                }
                accruals.setBlueScreenChargeCode(lclSsAc.getApGlMappingId().getBlueScreenChargeCode());
                accruals.setBillTo(YES);
                accruals.setReadyToPost(ON);
                accruals.setCurrencyCode(CURRENCY_CODE);
                //accrual.setDescription(cost.getCostComments())
                //accrual.setBlTerms(bl.getStreamShipBl());
                accruals.setCostId(lclSsAc.getId().intValue());
                accruals.setManifestFlag(NO);
                accruals.setCreatedOn(d);
                accruals.setCreatedBy(lclSsAc.getEnteredByUserId().getUserId());
                accrualsDAO.save(accruals);
            }
        }
    }

    public ManifestModel getUnitAutoCostAccruals(Long unitSsId, int apGlMapId) throws Exception {
        StringBuilder sb = new StringBuilder();
        CodetypeDAO codeTypeDAO = new CodetypeDAO();
        Integer codeTypeId = codeTypeDAO.getCodeTypeId("Vessel Codes");
        sb.append("SELECT IF(ss_head.service_type = 'E','LCLE','LCLI') AS shipmentType,");
        sb.append("CONCAT(org.un_loc_name,'/',IF(org_cntry.code = 'US',org_st.code,org_cntry.codedesc),'(',org.un_loc_code,')') AS origin,");
        sb.append("CONCAT(dest.un_loc_name,'/',IF(dest_cntry.code = 'US',dest_st.code,dest_cntry.codedesc),'(',dest.un_loc_code,')') AS destination,");
        sb.append("ss_detail.sta AS sailDate,ss_detail.sta AS eta,steamshipline.acct_name AS streamShipLine,");
        sb.append("vessel.code AS vesselNo,vessel.codedesc AS vesselName,ship.acct_name AS shipperName,ship.acct_no AS shipperNo,cons.acct_name AS consigneeName,cons.acct_no AS consigneeNo,agent.acct_no AS agentNo,agent.acct_name AS agentName,");
        sb.append("concat_ws('-',(select rule_name from system_rules where rule_code = 'CompanyCode' limit 1),glmap.GL_Acct,LPAD(IF(glmap.derive_yn != 'N' AND glmap.derive_yn != 'F' AND glmap.suffix_value IN ('B', 'L', 'D', 'F'),IF(glmap.suffix_value = 'B',IF(ss_head.service_type = 'E',term.lcl_export_billing,");
        sb.append("term.lcl_import_billing),IF(glmap.suffix_value = 'L',IF(ss_head.service_type = 'E',term.lcl_export_loading, term.lcl_import_loading),IF(ss_head.service_type = 'E',term.lcl_export_dockreceipt,''))),");
        sb.append("glmap.suffix_value), 2, '0')) AS glAccount,ss_head.billing_trmnum AS terminal ");
        sb.append("FROM lcl_unit_ss lus LEFT JOIN lcl_ss_header ss_head ON ss_head.id=lus.ss_header_id ");
        sb.append("LEFT JOIN lcl_unit lu ON lus.unit_id=lu.id LEFT JOIN lcl_unit_ss luss ON luss.unit_id=lu.id  LEFT JOIN lcl_unit_ss_imports lusm ON lusm.unit_id=lu.id ");
        sb.append("LEFT JOIN lcl_ss_masterbl lsm ON lsm.ss_header_id=lus.ss_header_id AND lsm.sp_booking_no=luss.sp_booking_no ");
        sb.append("LEFT JOIN lcl_ss_contact lsc ON lsc.id=lsm.ship_ss_contact_id LEFT JOIN trading_partner ship ON ship.acct_no=lsc.tp_acct_no ");
        sb.append("LEFT JOIN lcl_ss_contact lscc ON lscc.id=lsm.cons_ss_contact_id LEFT JOIN trading_partner cons ON cons.acct_no=lscc.tp_acct_no ");
        sb.append("LEFT JOIN lcl_ss_detail ss_detail ON ss_head.id = ss_detail.ss_header_id AND ss_detail.trans_mode = 'V' ");
        sb.append("LEFT JOIN trading_partner steamshipline ON steamshipline.acct_no = ss_detail.sp_acct_no LEFT JOIN trading_partner agent ON agent.acct_no=lusm.origin_acct_no ");
        sb.append("LEFT JOIN genericcode_dup vessel ON ss_detail.sp_reference_name = vessel.codedesc AND vessel.codetypeid = ");
        sb.append(codeTypeId);
        sb.append(" JOIN un_location org ON org.id =ss_head.origin_id JOIN genericcode_dup org_cntry ON org.countrycode = org_cntry.id LEFT JOIN genericcode_dup org_st ON org.statecode = org_st.id ");
        sb.append("JOIN un_location dest ON dest.id = ss_head.destination_id JOIN genericcode_dup dest_cntry ON dest.countrycode = dest_cntry.id LEFT JOIN genericcode_dup dest_st ON dest.statecode = dest_st.id ");
        sb.append("JOIN gl_mapping glmap ON glmap.id = ").append(apGlMapId).append(" LEFT JOIN terminal_gl_mapping term ON ss_head.billing_trmnum= term.terminal ");
        sb.append("WHERE lus.id = ").append(unitSsId);
        sb.append("  GROUP BY lus.id");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setResultTransformer(Transformers.aliasToBean(ManifestModel.class));
        query.addScalar("shipmentType", StringType.INSTANCE);
        query.addScalar("origin", StringType.INSTANCE);
        query.addScalar("destination", StringType.INSTANCE);
        query.addScalar("sailDate", DateType.INSTANCE);
        query.addScalar("eta", DateType.INSTANCE);
        query.addScalar("streamShipLine", StringType.INSTANCE);
        query.addScalar("vesselNo", StringType.INSTANCE);
        query.addScalar("vesselName", StringType.INSTANCE);
        query.addScalar("shipperName", StringType.INSTANCE);
        query.addScalar("shipperNo", StringType.INSTANCE);
        query.addScalar("consigneeName", StringType.INSTANCE);
        query.addScalar("consigneeNo", StringType.INSTANCE);
        query.addScalar("agentNo", StringType.INSTANCE);
        query.addScalar("agentName", StringType.INSTANCE);
        query.addScalar("glAccount", StringType.INSTANCE);
        query.addScalar("terminal", StringType.INSTANCE);
        ManifestModel manifestModel = (ManifestModel) query.uniqueResult();
        return manifestModel;
    }

    public void deleteLclAccrualsByUnit(String voyageNo, String unitNo, Long headerId, Long unitSsId, boolean manualEntry) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("DELETE FROM transaction_ledger ");
        queryBuilder.append("WHERE cost_id IN (SELECT id FROM lcl_ss_ac WHERE ss_header_id=").append(headerId);
        queryBuilder.append(" AND unit_ss_id=").append(unitSsId).append(" AND manual_entry=").append(manualEntry).append(")");
        queryBuilder.append(" AND container_no='").append(unitNo).append("'");
        queryBuilder.append(" and transaction_type = '").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append(" and status = '").append(STATUS_OPEN).append("'");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        getCurrentSession().flush();
    }

    public String getCostCountByUnit(String voyageNo, String unitNo, Long headerId, Long unitSsId, boolean manualEntry) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT COUNT(*)FROM transaction_ledger ");
        sb.append("WHERE cost_id IN (SELECT id FROM lcl_ss_ac WHERE ss_header_id=").append(headerId);
        sb.append(" AND unit_ss_id=").append(unitSsId).append(" AND manual_entry=").append(manualEntry).append(" GROUP BY unit_ss_id) ");
        sb.append(" AND voyage_no=").append(voyageNo).append(" AND container_no='").append(unitNo).append("'");
        Object count = (Object) getCurrentSession().createSQLQuery(sb.toString()).uniqueResult();
        if (count != null) {
            return count.toString();
        }
        return "";
    }
}
