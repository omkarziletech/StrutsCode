/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.accounting.dao;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.logisoft.bc.fcl.FclBlConstants;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBookingAc;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.bl.LclCorrection;
import com.gp.cong.logisoft.hibernate.dao.CodetypeDAO;
import com.gp.cong.logisoft.hibernate.dao.CustomerAccountingDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingAcTransDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLRatesDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBlAcDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLBlDAO;
import com.gp.cong.logisoft.lcl.comparator.LclManifestModelComparator;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.GlMapping;
import com.gp.cvst.logisoft.domain.Transaction;
import com.gp.cvst.logisoft.domain.TransactionLedger;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import com.gp.cvst.logisoft.hibernate.dao.TransactionDAO;
import com.gp.cvst.logisoft.hibernate.dao.TransactionLedgerDAO;
import com.logiware.accounting.model.ManifestModel;
import com.logiware.hibernate.dao.AccountingTransactionDAO;
import com.logiware.hibernate.dao.ArTransactionHistoryDAO;
import com.logiware.hibernate.domain.ArTransactionHistory;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

/**
 *
 * @author Mei
 */
public class LclExportManifestDAO extends BaseHibernateDAO implements ConstantsInterface, LclCommonConstant {

    private StringBuilder fileNumbers = new StringBuilder();
    private Set<Long> fileNumberIds = new HashSet<Long>();

    public StringBuilder getFileNumbers() {
        return fileNumbers;
    }

    public void setFileNumbers(StringBuilder fileNumbers) {
        this.fileNumbers = fileNumbers;
    }

    public Set<Long> getFileNumberIds() {
        return fileNumberIds;
    }

    public void setFileNumberIds(Set<Long> fileNumberIds) {
        this.fileNumberIds = fileNumberIds;
    }

    public ManifestModel manifest(Long unitSSId, User user, boolean isManifest, Long fileId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        ManifestModel manifestModel = null;
        CodetypeDAO codeTypeDAO = new CodetypeDAO();
        LCLBlDAO blDAO = new LCLBlDAO();
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
        queryBuilder.append("chg.rate_per_unit_uom as ratePerUnitUOM,chg.adjustment_amount as adjustmentAmount,(chg.ar_amount+chg.adjustment_amount) ");
        queryBuilder.append("as amount,glmap.Charge_code as chargeCode,glmap.bluescreen_chargecode as bluescreenChargeCode,");
        queryBuilder.append("DeriveLCLExportGlAccount(glmap.id,'','0',voyageOrigin) AS glAccount,");
        queryBuilder.append(" customerReferenceNo AS customerReferenceNo, ");
        queryBuilder.append("pooCode AS pooCode,podCode AS podCode,fdCode AS fdCode,rateType AS rateType,fdid,chg.ar_bill_to_party AS billToParty");
        queryBuilder.append(" from (SELECT file.id AS fileId,");
        queryBuilder.append("'LCLE' AS shipmentType,CONCAT(");
        queryBuilder.append("(SELECT IF(t.unlocationcode1 <> '',RIGHT(t.unlocationcode1,3),t.trmnum) AS terminal FROM terminal t where t.trmnum=bl.billing_terminal),'-',");
        queryBuilder.append(" IF(dest.bl_numbering = 'Y',RIGHT(dest.un_loc_code, 3),dest.un_loc_code), ");
        queryBuilder.append("'-',file.file_number) AS blNumber,unit.id AS unitId,");
        queryBuilder.append("unit_ss.sp_booking_no AS bookingNumber,file.file_number AS dockReceipt,ss_head.schedule_no AS voyageNumber,");
        queryBuilder.append("ss_detail.std AS sailDate,ss_detail.sta AS eta,steamshipline.acct_name AS streamShipLine,unit.unit_no AS containerNumbers,");
        queryBuilder.append(" UnLocationGetNameStateCntryByID(org.id)AS origin,");
        queryBuilder.append(" UnLocationGetNameStateCntryByID(dest.id) AS destination,");
        queryBuilder.append("ship.acct_no AS shipperNo,ship.acct_name AS ");
        queryBuilder.append("shipperName,cons.acct_no AS consigneeNo,cons.acct_name AS consigneeName,fwd.acct_no AS forwarderNo,fwd.acct_name AS ");
        queryBuilder.append("forwarderName,tprty.acct_no AS thirdPartyNo,tprty.acct_name AS thirdPartyName,agent.acct_no AS agentNo,agent.acct_name AS ");
        queryBuilder.append("agentName,bl.billing_terminal AS billing_terminal,vessel.code AS vesselNo,vessel.codedesc AS vesselName, ");
        queryBuilder.append("(SELECT lrm.remarks FROM lcl_remarks lrm WHERE file.id = lrm.file_number_id ");
        queryBuilder.append(" AND lrm.type = 'Export Reference' )AS customerReferenceNo,");
        queryBuilder.append("org.un_loc_code AS pooCode,pod.un_loc_code AS podCode,dest.un_loc_code AS fdCode,bl.rate_type AS rateType,");
        queryBuilder.append("if(bl.fd_id <> '',bl.fd_id,bl.pod_id) as fdid, bl.posted_by_user_id AS posted, ss_head.`origin_id` AS voyageOrigin  ");
        queryBuilder.append("FROM lcl_unit_ss unit_ss JOIN lcl_booking_piece_unit  ");
        queryBuilder.append("unit_pieces ON unit_pieces.lcl_unit_ss_id = unit_ss.id JOIN lcl_booking_piece book_piece ON unit_pieces.booking_piece_id = ");
        queryBuilder.append("book_piece.id JOIN lcl_unit unit ON unit_ss.unit_id = unit.id  JOIN lcl_ss_header ss_head ON ");
        queryBuilder.append("unit_ss.ss_header_id = ss_head.id AND ss_head.service_type not in ('I') ");
        /* query append for to fetch the sailing date in voyage which in EXPORT , DOMESTIC , CFCL */
        queryBuilder.append(" join lcl_ss_detail ss_detail on ss_detail.ss_header_id = ss_head.id and  ss_detail.id = ");
        queryBuilder.append(" (select ls.id from lcl_ss_detail ls where ls.ss_header_id = ss_head.id order by ls.id desc limit 1) ");
        queryBuilder.append(" JOIN lcl_file_number FILE ON book_piece.file_number_id = file.id AND file.state = 'BL'  and file.status='M' JOIN ");
        queryBuilder.append(" lcl_bl bl ON file.id = bl.file_number_id JOIN lcl_booking bk ON file.id = bk.`file_number_id` ");
        queryBuilder.append(" LEFT JOIN lcl_booking_import bi ON file.id = bi.file_number_id LEFT JOIN un_location org ON org.id = bl.poo_id ");
        queryBuilder.append(" LEFT JOIN un_location dest ON dest.id = IF(bl.fd_id IS NOT NULL,bl.fd_id ,bl.pod_id) ");
        queryBuilder.append(" LEFT JOIN un_location pod ON pod.id = bl.pod_id ");
        queryBuilder.append(" LEFT JOIN trading_partner ship ON bl.ship_acct_no = ship.acct_no ");
        queryBuilder.append(" LEFT JOIN trading_partner cons ON bl.cons_acct_no = cons.acct_no LEFT JOIN trading_partner fwd ON bl.fwd_acct_no = ");
        queryBuilder.append("fwd.acct_no LEFT JOIN trading_partner tprty ON bl.third_party_acct_no = tprty.acct_no");
        /*This is check freight agent or pickup Agent*/
        queryBuilder.append(" LEFT JOIN trading_partner agent ON IF(bk.booking_type = 'T' AND bi.export_agent_acct_no IS NOT NULL, bi.export_agent_acct_no =agent.acct_no, ");
        queryBuilder.append(" IF( bk.agent_acct_no IS NOT NULL,bk.agent_acct_no = agent.acct_no,  bl.agent_acct_no = agent.acct_no))");
        queryBuilder.append(" LEFT JOIN trading_partner steamshipline ON steamshipline.acct_no = ss_detail.sp_acct_no ");
        queryBuilder.append(" LEFT JOIN genericcode_dup vessel ON ss_detail.sp_reference_name = vessel.codedesc AND vessel.codetypeid = ");
        queryBuilder.append(codeTypeId);
        queryBuilder.append(" WHERE ");
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
        queryBuilder.append("(chg.ar_amount > 0.00 OR chg.adjustment_amount > 0.00) and f.posted is not null ORDER BY f.fileId");
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
        query.addScalar("adjustmentAmount", DoubleType.INSTANCE);
        query.addScalar("billToParty", StringType.INSTANCE);
        List<ManifestModel> l = query.list();
        l = this.getRolledUpChargesAccounting(l, isManifest, user, "", "", LCL_SHIPMENT_TYPE_EXPORT);
        Collections.sort(l, new LclManifestModelComparator());
        if (CommonUtils.isNotEmpty(l)) {
            createLclArSubledgers(l, user);
            manifestModel = l.get(0);
            blDAO.updateModifiedDate(this.getFileNumbers().toString(),user.getUserId());
            if (!isManifest) {
                Set<Long> fileIds = this.getFileNumberIds();
                for (Long fileNumberId : fileIds) {
                    revertFFComm(fileNumberId, user);
                }
            }
            // lclUtils.updateLclFileNumbers(lclUtils.getFileNumbers().toString(), isManifest, manifestModel.getShipmentType());
        }
        return manifestModel;
    }

    private void revertFFComm(Long fileNumberId, User loginUser) throws Exception {
        LclCostChargeDAO costChargeDAO = new LclCostChargeDAO();
        LclManifestDAO lclManifestDAO = new LclManifestDAO();
        LCLBookingAcTransDAO lclBookingAcTransDAO = new LCLBookingAcTransDAO();
//        LclRemarksDAO remarksDAO = new LclRemarksDAO();
        LclBookingAc lclBookingAc = costChargeDAO.getCost(fileNumberId, "FFCOMM", true);
        if (lclBookingAc != null && NumberUtils.isNotZero(lclBookingAc.getApAmount())) {
            String costStatus = lclBookingAcTransDAO.getTransType(lclBookingAc.getId());
            if ("AC".equalsIgnoreCase(costStatus)) {
                String lclBookingAcTransIds = lclBookingAcTransDAO.getConcatenatedBookingAcTransIds(lclBookingAc.getId().toString());
                lclBookingAc.setSupAcct(null);
                lclBookingAc.setApBillToParty(null);
                lclBookingAc.setApAmount(BigDecimal.ZERO);
                lclBookingAc.setCostFlatrateAmount(BigDecimal.ZERO);
                lclBookingAc.setDeleted(Boolean.TRUE);
                costChargeDAO.saveOrUpdate(lclBookingAc);
                lclManifestDAO.deleteLclAccruals(lclBookingAc.getId().intValue(), lclBookingAc.getLclFileNumber().getFileNumber());
                costChargeDAO.deleteLclBookingAcTa(lclBookingAc.getId().intValue());
                lclBookingAcTransDAO.deleteLclBookingAcTransByBkgAcId(lclBookingAcTransIds);
//                remarksDAO.insertLclRemarks(fileNumberId, "auto", notes1, loginUser.getUserId());
            }
        }
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
            arSubledger.setInvoiceNumber(model.getDockReceipt());
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
                        if ("R".equalsIgnoreCase(model.getRateType()) && CommonUtils.isNotEmpty(model.getForwarderNo())) {
                            Boolean fwdFlag = new LCLBlDAO().getFreightForwardAcctStatus(model.getForwarderNo());
                            if (!fwdFlag) {
                                checkFFCommission(model, user);
                            }
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
                    if ("R".equalsIgnoreCase(model.getRateType()) && CommonUtils.isNotEmpty(model.getForwarderNo())) {
                        Boolean fwdFlag = new LCLBlDAO().getFreightForwardAcctStatus(model.getForwarderNo());
                        if (!fwdFlag) {
                            checkFFCommission(model, user);
                        }
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
            TransactionDAO atransactionDAO = new TransactionDAO();
            TransactionLedgerDAO ledgerDAO = new TransactionLedgerDAO();
            ArTransactionHistoryDAO arTransactionHistoryDAO = new ArTransactionHistoryDAO();
            String dockReceipt = model.getDockReceipt();
            for (String custNo : model.getArCustomers().keySet()) {
                String arBlNumber = model.getCorrectionBlNumber() == null ? model.getBlNumber() : model.getCorrectionBlNumber().get(custNo);

                ledgerDAO.updateLclEBlNumber(arBlNumber, model.getDockReceipt());
                atransactionDAO.updateLclEBlNumber(arBlNumber, model.getDockReceipt());
                arTransactionHistoryDAO.updateLclEBlNumber(arBlNumber, model.getDockReceipt());

                Transaction ar = transactionDAO.getArInvoice(custNo, arBlNumber, dockReceipt);
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
                    ar.setInvoiceNumber(model.getDockReceipt());
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
                createLclArHistory(model, user, lclCorrection);
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

    public void calculatePBASurcharge(ManifestModel model, User user) throws Exception {
        LclBlAcDAO lclBlAcDAO = new LclBlAcDAO();
        Double totalAmount = lclBlAcDAO.getChargeForPBA(model.getFileId(), 1);
        if (totalAmount > 0.00) {
            GlMappingDAO glMappingDAO = new GlMappingDAO();
            String pbaperAmt = LoadLogisoftProperties.getProperty("pbaSurchargePercentage");
            Integer pbaper;
            if (CommonUtils.isEmpty(pbaperAmt)) {
                pbaper = 1;
            } else {
                pbaper = Integer.parseInt(pbaperAmt);
            }
            totalAmount = (totalAmount * pbaper) / 100;
            GlMapping glmapping = glMappingDAO.findByChargeCode(CommonConstants.PBA_CHARGECODE, model.getShipmentType(), "AC");
            if (null != glmapping) {
                String glAccount = glMappingDAO.dervieGlAccount(CommonConstants.PBA_CHARGECODE, model.getShipmentType(), model.getTerminal(),
                        glmapping.getRevExp());
                insertLclBookingAc(model, totalAmount, glmapping, glAccount, user);
            }
        }
    }

    public void checkFFCommission(ManifestModel model, User user) throws Exception {
        RefTerminalDAO refterminaldao = new RefTerminalDAO();
        PortsDAO portsdao = new PortsDAO();
        String trmnum = null, eciPortCodeFd = null, eciPortCodePod = null;
        String rateType = "R".equalsIgnoreCase(model.getRateType()) ? "Y" : model.getRateType();
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
            insertLclBookingAc(model, totalAmount, glmapping, glAccount, user);
        }
    }

    public void insertLclBookingAc(ManifestModel model, double amount, GlMapping glmapping, String glAcct, User user) throws Exception {
        LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
        TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
        LclRemarksDAO remarksDAO = new LclRemarksDAO();
        Date d = new Date();
        LclBookingAc lclBookingAc = lclCostChargeDAO.manaualChargeValidate(model.getFileId(), glmapping.getChargeCode(), true);
        if (lclBookingAc == null) {
            lclBookingAc = new LclBookingAc();
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
        lclBookingAc.setCostFlatrateAmount(new BigDecimal(amount));
        lclBookingAc.setModifiedBy(user);
        lclBookingAc.setModifiedDatetime(d);
        if (glmapping.getChargeCode().equalsIgnoreCase(CommonConstants.FFCOMM_CHARGECODE)) {
            lclBookingAc.setSupAcct(tradingPartnerDAO.findById(model.getForwarderNo()));
        } else {
            lclBookingAc.setSupAcct(tradingPartnerDAO.findById(model.getAgentNo()));
        }
        lclCostChargeDAO.saveOrUpdate(lclBookingAc);
    }

    public List<ManifestModel> getRolledUpChargesAccounting(List<ManifestModel> lclManifestList, boolean isManifest,
            User user, String buttonValue, String realPath, String shipmentType) throws Exception {
        Map<String, ManifestModel> manifestMap = new LinkedHashMap();
        //Map<String, String> mailMap = new LinkedHashMap();
        //LclPrintUtil lclPrintUtil = new LclPrintUtil();
        String key = "";
        //String keyMail = "";
        //String fileLocation = null;
        for (int i = 0; i < lclManifestList.size(); i++) {
            ManifestModel manifestModal = lclManifestList.get(i);
            manifestModal.setManifest(isManifest);
            key = manifestModal.getFileId().toString() + "_" + manifestModal.getChargeCode() + "_" + manifestModal.getBluescreenChargeCode();
//            if (manifestModal.getShipmentType().equalsIgnoreCase("LCLI") && buttonValue != null && buttonValue.equalsIgnoreCase("CS")) {
//                keyMail = manifestModal.getFileId().toString();
//                if (!mailMap.containsKey(keyMail)) {
//                    fileLocation = lclPrintUtil.createImportBkgReport(keyMail, manifestModal.getDockReceipt(), DOCUMENTLCLIMPORTSARRIVALNOTICE, realPath,null);
//                    if (CommonUtils.isNotEmpty(manifestModal.getArrivalNoticeEmail())) {
//                        sendMailWithoutPrintConfig(fileLocation, SCREENNAMELCLIMPORTBOOKINGREPORT,
//                                DOCUMENTLCLIMPORTSARRIVALNOTICE + "For File#" + manifestModal.getDockReceipt(), "Email", "Pending",
//                                manifestModal.getArrivalNoticeEmail(), manifestModal.getDockReceipt(), SCREENNAMELCLIMPORTBOOKINGREPORT,
//                                "", "", user);
//                    }
//                    if (CommonUtils.isNotEmpty(manifestModal.getArrivalNoticeFax())) {
//                        String fax[] = manifestModal.getArrivalNoticeFax().split(",");
//                        for (int j = 0; j < fax.length; j++) {
//                            sendMailWithoutPrintConfig(fileLocation, SCREENNAMELCLIMPORTBOOKINGREPORT,
//                                    DOCUMENTLCLIMPORTSARRIVALNOTICE + "For File#" + manifestModal.getDockReceipt(), "Fax", "Pending",
//                                    fax[j], manifestModal.getDockReceipt(), SCREENNAMELCLIMPORTBOOKINGREPORT,
//                                    "", "", user);
//                        }
//                    }
//                    mailMap.put(keyMail, keyMail);
//                }
//            }// Don't Remove this comment line..this may required in future
            if (!manifestMap.containsKey(key)) {
                if (!fileNumbers.toString().contains(manifestModal.getDockReceipt())) {
                    fileNumbers.append("'").append(manifestModal.getDockReceipt()).append("',");
                }
                fileNumberIds.add(manifestModal.getFileId());
                manifestMap.put(key, manifestModal);
            } else if (!manifestModal.getRatePerUnitUOM().equalsIgnoreCase("FL") && !manifestModal.getRatePerUnitUOM().equalsIgnoreCase("PCT")) {
                ManifestModel manifestModalFromMap = (ManifestModel) manifestMap.get(key);
                if (manifestModalFromMap.getRatePerUnitUOM().equalsIgnoreCase("FRW")
                        || manifestModalFromMap.getRatePerUnitUOM().equalsIgnoreCase("FRV")
                        || manifestModalFromMap.getRatePerUnitUOM().equalsIgnoreCase("FRM")) {
                    manifestModalFromMap.setAmount((manifestModalFromMap.getAmount() + manifestModal.getAmount())
                            - manifestModalFromMap.getAdjustmentAmount());
                } else if (manifestModalFromMap.getRatePerUnitUOM().equalsIgnoreCase("W")
                        || manifestModalFromMap.getRatePerUnitUOM().equalsIgnoreCase("V")
                        || manifestModalFromMap.getRatePerUnitUOM().equalsIgnoreCase("M")) {
                    //manifestModalFromMap.setAmount(manifestModal.getAmount());
                    manifestModalFromMap.setAmount(manifestModalFromMap.getAmount() - manifestModalFromMap.getAdjustmentAmount());
                }
                manifestMap.put(key, manifestModalFromMap);
            }
        }
        List<ManifestModel> rolledChargesList = new ArrayList(manifestMap.values());
        if (CommonUtils.isNotEmpty(fileNumbers) && fileNumbers.toString().contains(",")) {
            fileNumbers.deleteCharAt(fileNumbers.length() - 1);
        }
        return rolledChargesList;
    }
}
