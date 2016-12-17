/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.lcl.common.constant;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.lcl.model.LclCorrectionModel;
import com.gp.cong.logisoft.bc.fcl.FclBlConstants;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.beans.ChargesInfoBean;
import com.gp.cong.logisoft.beans.LCLCorrectionChargeBean;
import com.gp.cong.logisoft.beans.LCLCorrectionViewBean;
import com.gp.cong.logisoft.domain.CreditDebitNote;
import com.gp.cong.logisoft.domain.Ports;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.CorrectionCommodity;
import com.gp.cong.logisoft.domain.lcl.LclContact;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.bl.LclBl;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlAc;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlPiece;
import com.gp.cong.logisoft.domain.lcl.bl.LclCorrection;
import com.gp.cong.logisoft.domain.lcl.bl.LclCorrectionCharge;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.logisoft.hibernate.dao.CodetypeDAO;
import com.gp.cong.logisoft.hibernate.dao.CreditDebitNoteDAO;
import com.gp.cong.logisoft.hibernate.dao.CustomerAccountingDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.CorrectionCommodityDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLContactDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBlAcDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsHeaderDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLBlDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLCorrectionChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLCorrectionDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LclBLPieceDAO;
import com.gp.cong.logisoft.lcl.model.RateModel;
import com.gp.cvst.logisoft.domain.CustAddress;
import com.gp.cvst.logisoft.domain.GlMapping;
import com.gp.cvst.logisoft.domain.Transaction;
import com.gp.cvst.logisoft.domain.TransactionLedger;
import com.gp.cvst.logisoft.hibernate.dao.CustAddressDAO;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import com.logiware.accounting.dao.SubledgerDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LCLCorrectionForm;
import com.logiware.accounting.dao.AccrualsDAO;
import com.logiware.accounting.model.ManifestModel;
import com.logiware.hibernate.dao.AccountingTransactionDAO;
import com.logiware.hibernate.dao.ArTransactionHistoryDAO;
import com.logiware.hibernate.domain.ArTransactionHistory;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.apache.struts.util.LabelValueBean;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.BooleanType;
import org.hibernate.type.DateType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

/**
 *
 * @author Mei
 */
public class BlCorrectionUtils extends BaseHibernateDAO implements ConstantsInterface, LclCommonConstant {

    private static final Logger log = Logger.getLogger(BlCorrectionUtils.class);

    public List<LclCorrectionModel> searchCnPoolList(LCLCorrectionForm correctionForm) {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT f.fileId AS fileId,f.fileNo AS fileNo,f.blNo AS blNo,f.correctionId AS correctionId, ");
        queryStr.append(" f.currentProfit AS currentProfit,f.profitAfterCN AS profitAfterCN,f.correctionNo AS correctionNo, ");
        queryStr.append(" f.createdByUser AS createdByUser,f.billingType AS billingType,f.createdByDate AS createdByDate, ");
        queryStr.append(" f.corrStatus AS corrStatus,f.correctionType AS correctionType, ");
        queryStr.append(" f.correcTypeDesc AS correcTypeDesc,f.correctionCode AS correctionCode,f.correcCodeDesc AS correcCodeDesc, ");
        queryStr.append(" f.approvedByUser AS approvedByUser,f.postedDate AS postedDate,f.voidStatus AS voidStatus, ");
        queryStr.append(" DATE_FORMAT(lsd.std, '%d-%b-%Y') AS sailDate FROM ");

        queryStr.append(" (SELECT lfn.file_number AS fileNo,lfn.id AS fileId,lfn.state AS fileState, ");
        queryStr.append(" CONCAT(UPPER(IF (t.unLocationCode1 <> '',RIGHT(t.unLocationCode1, 3),RIGHT(poo.un_loc_code, 3))), ");
        queryStr.append(" '-',IF(fd.bl_numbering = 'Y',RIGHT(fd.un_loc_code, 3),fd.un_loc_code),'-',lfn.file_number) AS blNo, ");
        queryStr.append(" corr.id AS correctionId,corr.current_profit AS currentProfit, ");
        queryStr.append(" corr.profit_aftercn AS profitAfterCN,corr.correction_no AS correctionNo, ");
        queryStr.append(" UserDetailsGetLoginNameByID (corr.entered_by_user_id) AS createdByUser, ");
        queryStr.append(" bl.billing_type AS billingType, ");
        queryStr.append(" DATE_FORMAT(corr.entered_datetime,'%d-%b-%Y %T %p') AS createdByDate, ");
        queryStr.append(" corr.status AS corrStatus,corrtype.code AS correctionType, ");
        queryStr.append(" corrtype.codedesc AS correcTypeDesc,corrcode.code AS correctionCode,corrcode.codedesc AS correcCodeDesc, ");
        queryStr.append(" UserDetailsGetLoginNameByID (corr.approved_by) AS approvedByUser, ");
        queryStr.append(" DATE_FORMAT(corr.posted_date,'%d-%b-%Y %T %p') AS postedDate, ");
        queryStr.append(" corr.void AS voidStatus ");

        queryStr.append(" FROM lcl_correction corr ");
        queryStr.append(" JOIN lcl_file_number lfn ON (lfn.id = corr.file_number_id) ");
        queryStr.append(" JOIN lcl_bl bl ON (bl.file_number_id = lfn.id) ");
        queryStr.append(" JOIN un_location poo ON (poo.id = bl.pol_id) ");
        queryStr.append(" JOIN un_location fd ON fd.id = IF(bl.fd_id IS NOT NULL,bl.fd_id,bl.pod_id) ");
        queryStr.append(" JOIN genericcode_dup corrtype ON (corr.TYPE = corrtype.id)  ");
        queryStr.append(" JOIN genericcode_dup corrcode ON (corr.CODE = corrcode.id) ");
        queryStr.append(" JOIN terminal t ON bl.billing_terminal = t.trmnum ");
        queryStr.append(" WHERE bl.booking_type in ('E','T') ");
        if (CommonUtils.isNotEmpty(correctionForm.getSearchFileNo())) {
            queryStr.append(" and lfn.file_number=:fileNo");
        }
        if (CommonUtils.isNotEmpty(correctionForm.getSearchCorrectionCode())) {
            queryStr.append(" and corr.code=:correctionCode");
        }
        if (CommonUtils.isNotEmpty(correctionForm.getSearchPooId())) {
            queryStr.append(" and bl.poo_id=:pooId");
        }
        if (CommonUtils.isNotEmpty(correctionForm.getSearchPolId())) {
            queryStr.append(" and bl.pol_id=:polId");
        }
        if (CommonUtils.isNotEmpty(correctionForm.getSearchPodId())) {
            queryStr.append(" and fd.id=:podId");
        }
        if (CommonUtils.isNotEmpty(correctionForm.getSearchFdId())) {
            queryStr.append(" and fd.id=:fdId");
        }
        queryStr.append(" GROUP BY corr.id DESC  ) f ");
        queryStr.append(" JOIN lcl_ss_header lsh ON lsh.id = (SELECT lsh.`id` as headerId FROM lcl_ss_header lsh ");
        queryStr.append(" JOIN lcl_unit_ss lus ON lus.`ss_header_id` = lsh.`id` ");
        queryStr.append(" JOIN lcl_booking_piece_unit bpu ON bpu.lcl_unit_ss_id = lus.id ");
        queryStr.append(" JOIN lcl_booking_piece bp ON bp.id = bpu.booking_piece_id ");
        queryStr.append(" WHERE bp.`file_number_id` = f.fileId AND service_type NOT IN('I','N') limit 1) ");
        queryStr.append(" JOIN lcl_ss_detail lsd ON (lsh.id = lsd.ss_header_id)   ");
        queryStr.append(" and lsd.id=(select ls.id from lcl_ss_detail ls where ls.ss_header_id = lsh.id  order by ls.id desc limit 1)  ");
        queryStr.append(" AND lsh.service_type IN ('E', 'C')   ");
        if (CommonUtils.isNotEmpty(correctionForm.getSearchBlNo())) {
            queryStr.append(" and f.blNo=:blNo");
        }
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setResultTransformer(Transformers.aliasToBean(LclCorrectionModel.class));
        if (CommonUtils.isNotEmpty(correctionForm.getSearchFileNo())) {
            query.setParameter("fileNo", correctionForm.getSearchFileNo());
        }
        if (CommonUtils.isNotEmpty(correctionForm.getSearchBlNo())) {
            query.setParameter("blNo", correctionForm.getSearchBlNo());
        }
        if (CommonUtils.isNotEmpty(correctionForm.getSearchCorrectionCode())) {
            query.setParameter("correctionCode", correctionForm.getSearchCorrectionCode());
        }
        if (CommonUtils.isNotEmpty(correctionForm.getSearchPooId())) {
            query.setParameter("pooId", correctionForm.getSearchPooId());
        }
        if (CommonUtils.isNotEmpty(correctionForm.getSearchPolId())) {
            query.setParameter("polId", correctionForm.getSearchPolId());
        }
        if (CommonUtils.isNotEmpty(correctionForm.getSearchPodId())) {
            query.setParameter("podId", correctionForm.getSearchPodId());
        }
        if (CommonUtils.isNotEmpty(correctionForm.getSearchFdId())) {
            query.setParameter("fdId", correctionForm.getSearchFdId());
        }
        query.addScalar("fileId", StringType.INSTANCE);
        query.addScalar("fileNo", StringType.INSTANCE);
        query.addScalar("blNo", StringType.INSTANCE);
        query.addScalar("correctionId", StringType.INSTANCE);
        query.addScalar("currentProfit", StringType.INSTANCE);
        query.addScalar("profitAfterCN", StringType.INSTANCE);
        query.addScalar("correctionNo", StringType.INSTANCE);
        query.addScalar("createdByUser", StringType.INSTANCE);
        query.addScalar("billingType", StringType.INSTANCE);
        query.addScalar("createdByDate", StringType.INSTANCE);
        query.addScalar("corrStatus", StringType.INSTANCE);
        query.addScalar("correctionType", StringType.INSTANCE);
        query.addScalar("correcTypeDesc", StringType.INSTANCE);
        query.addScalar("correctionCode", StringType.INSTANCE);
        query.addScalar("correcCodeDesc", StringType.INSTANCE);
        query.addScalar("approvedByUser", StringType.INSTANCE);
        query.addScalar("postedDate", StringType.INSTANCE);
        query.addScalar("voidStatus", BooleanType.INSTANCE);
        query.addScalar("sailDate", StringType.INSTANCE);
        return query.list();
    }

    public List<LCLCorrectionViewBean> getCorrectionList(Long fileId, Boolean voidFlag) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT CONCAT(UPPER(RIGHT(org.un_loc_code, 3)),'-',dest.un_loc_code,'-',f.file_number)AS blNo, ");
        queryStr.append(" corr.id AS correctionId,corr.correction_no AS noticeNo, ");
        queryStr.append(" corr.current_profit AS currentProfit,corr.profit_aftercn AS profitAfterCN, ");
        queryStr.append("  UserDetailsGetLoginNameByID(corr.entered_by_user_id) AS userName,");
        queryStr.append("  bl.billing_type AS prepaidCollect,");
        queryStr.append("  DATE_FORMAT(corr.entered_datetime,'%d-%b-%Y %T %p') AS noticeDate,");
        queryStr.append("  gen_dup_type.code AS correctionType,gen_dup_code.code AS correctionCode,");
        queryStr.append("  UserDetailsGetLoginNameByID(corr.approved_by) AS approval,");
        queryStr.append("  UserDetailsGetLoginNameByID(corr.posted_by) AS whoPosted,");
        queryStr.append("  DATE_FORMAT(corr.posted_date,'%d-%b-%Y %T %p') AS postedDate,");
        queryStr.append("  IF(corr.posted_date IS NULL OR corr.posted_date = '','No','Yes') AS posted,");
        queryStr.append("  corr.status AS STATUS,corr.party_acct_no AS partyNo,ca.send_debit_credit_notes AS creditDebit  ");
        queryStr.append("  FROM lcl_bl bl ");
        queryStr.append(" JOIN lcl_file_number f ON bl.file_number_id = f.id  ");
        queryStr.append(" JOIN lcl_correction corr ON bl.file_number_id = corr.file_number_id  ");
        queryStr.append(" LEFT JOIN cust_accounting ca ON ca.acct_no = corr.party_acct_no  ");
        queryStr.append(" JOIN genericcode_dup gen_dup_type ON corr.TYPE = gen_dup_type.id  ");
        queryStr.append(" JOIN genericcode_dup gen_dup_code ON corr.CODE = gen_dup_code.id ");
        queryStr.append(" JOIN un_location org ON org.id = bl.pol_id  ");
        queryStr.append(" JOIN un_location dest ON dest.id = IF(bl.fd_id IS NOT NULL,bl.fd_id,bl.pod_id)  ");
        queryStr.append(" WHERE corr.void = 0 AND bl.file_number_id =:fileId GROUP BY corr.id DESC  ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setLong("fileId", fileId);
        query.setResultTransformer(Transformers.aliasToBean(LCLCorrectionViewBean.class));
        query.addScalar("blNo", StringType.INSTANCE);
        query.addScalar("correctionId", LongType.INSTANCE);
        query.addScalar("noticeNo", LongType.INSTANCE);
        query.addScalar("userName", StringType.INSTANCE);
        query.addScalar("prepaidCollect", StringType.INSTANCE);
        query.addScalar("noticeDate", StringType.INSTANCE);
        query.addScalar("correctionType", StringType.INSTANCE);
        query.addScalar("correctionCode", StringType.INSTANCE);
        query.addScalar("approval", StringType.INSTANCE);
        query.addScalar("whoPosted", StringType.INSTANCE);
        query.addScalar("postedDate", StringType.INSTANCE);
        query.addScalar("posted", StringType.INSTANCE);
        query.addScalar("status", StringType.INSTANCE);
        query.addScalar("partyNo", StringType.INSTANCE);
        query.addScalar("creditDebit", StringType.INSTANCE);
        query.addScalar("currentProfit", StringType.INSTANCE);
        query.addScalar("profitAfterCN", StringType.INSTANCE);
        return query.list();
    }

    public void setCorrection(LCLCorrectionForm lclCorrectionForm, LclBl lclBl, HttpServletRequest request) throws Exception {
        LclBlAcDAO lclBlAcDAO = new LclBlAcDAO();
        LclBLPieceDAO lclBlPieceDAO = new LclBLPieceDAO();
        List<LclBlAc> lclBlAcList = lclBlAcDAO.getLclChargesByFileNumberAsc(lclCorrectionForm.getFileId());
        BigInteger commodityCount = lclBlPieceDAO.getPieceCountByFileId(lclCorrectionForm.getFileId());
        if (commodityCount.doubleValue() > 1) {
            lclBlAcList = this.getRolledUpChargesForBlCorrections(lclBlAcList);
        }
        LCLCorrectionChargeDAO correctionChargeDAO = new LCLCorrectionChargeDAO();
        Long previousCorrectionId = correctionChargeDAO.getPreviousCorrectionId(lclBl.getLclFileNumber().getId());
        if (previousCorrectionId == 0) {
            this.setBLChargeList(lclBlAcList, lclCorrectionForm, request);
        } else if (null != previousCorrectionId && lclCorrectionForm.getCorrectionId() == 0) {
            this.setPreviousChargeList(previousCorrectionId, lclCorrectionForm, request);
        } else {
            LclCorrection lclCorrection = new LCLCorrectionDAO().findById(lclCorrectionForm.getCorrectionId());
            request.setAttribute("lclCorrection", lclCorrection);
            this.setChargeList(lclCorrectionForm.getCorrectionId(), lclCorrectionForm, request);
        }
    }

    public String setBillToPartyForCorrectionCharges(String billToParty) {
        String label = null;
        if (billToParty.equalsIgnoreCase("F")) {
            label = "Forwarder";
        } else if (billToParty.equalsIgnoreCase("S")) {
            label = "Shipper";
        } else if (billToParty.equalsIgnoreCase("T")) {
            label = "ThirdParty";
        } else if (billToParty.equalsIgnoreCase("A")) {
            label = "Agent";
        } else if (billToParty.equalsIgnoreCase("C")) {
            label = "Consignee";
        } else if (billToParty.equalsIgnoreCase("N")) {
            label = "Notify";
        } else if (billToParty.equalsIgnoreCase("W")) {
            label = "Warehouse";
        }
        return label;
    }

    public List getRolledUpChargesForBlCorrections(List<LclBlAc> lclBlAcList) {
        Map chargesInfoMap = new LinkedHashMap();
        Double minchg = 0.0;
        Double calculatedWeight = 0.0;
        Double calculatedMeasure = 0.0;
        for (int i = 0; i < lclBlAcList.size(); i++) {
            LclBlAc lclBlAc = lclBlAcList.get(i);
            if (!chargesInfoMap.containsKey(lclBlAc.getArglMapping().getChargeCode())) {
                lclBlAc.setRolledupCharges(lclBlAc.getArAmount());
                if (!lclBlAc.isManualEntry() && lclBlAc.getRatePerUnitUom() != null && lclBlAc.getRateUom() != null && lclBlAc.getArglMapping() != null
                        && lclBlAc.getArglMapping().getBlueScreenChargeCode() != null && !lclBlAc.getArglMapping().getBlueScreenChargeCode().equals("0032")
                        && !lclBlAc.getArglMapping().getBlueScreenChargeCode().equals("0232")) {
                    if (lclBlAc.getRatePerUnitUom().equalsIgnoreCase("V") || lclBlAc.getRatePerUnitUom().equalsIgnoreCase("W")
                            || lclBlAc.getRatePerUnitUom().equalsIgnoreCase("M")) {
                        if (lclBlAc.getRateUom().equalsIgnoreCase("I")) {
                            if (lclBlAc.getLclBlPiece() != null) {
                                if (lclBlAc.getLclBlPiece().getActualWeightImperial() != null && lclBlAc.getLclBlPiece().getActualWeightImperial().doubleValue() != 0.00) {
                                    lclBlAc.setTotalWeight(lclBlAc.getLclBlPiece().getActualWeightImperial());
                                } else if (lclBlAc.getLclBlPiece().getBookedWeightImperial() != null && lclBlAc.getLclBlPiece().getBookedWeightImperial().doubleValue() != 0.00) {
                                    lclBlAc.setTotalWeight(lclBlAc.getLclBlPiece().getBookedWeightImperial());
                                }
                                if (lclBlAc.getLclBlPiece().getActualVolumeImperial() != null && lclBlAc.getLclBlPiece().getActualVolumeImperial().doubleValue() != 0.00) {
                                    lclBlAc.setTotalMeasure(lclBlAc.getLclBlPiece().getActualVolumeImperial());
                                } else if (lclBlAc.getLclBlPiece().getBookedVolumeImperial() != null && lclBlAc.getLclBlPiece().getBookedVolumeImperial().doubleValue() != 0.00) {
                                    lclBlAc.setTotalMeasure(lclBlAc.getLclBlPiece().getBookedVolumeImperial());
                                }
                            } else if (lclBlAc.getRateUom().equalsIgnoreCase("M")) {
                                if (lclBlAc.getLclBlPiece().getActualWeightMetric() != null && lclBlAc.getLclBlPiece().getActualWeightMetric().doubleValue() != 0.00) {
                                    lclBlAc.setTotalWeight(lclBlAc.getLclBlPiece().getActualWeightMetric());
                                } else if (lclBlAc.getLclBlPiece().getBookedWeightMetric() != null && lclBlAc.getLclBlPiece().getBookedWeightMetric().doubleValue() != 0.00) {
                                    lclBlAc.setTotalWeight(lclBlAc.getLclBlPiece().getBookedWeightMetric());
                                }
                                if (lclBlAc.getLclBlPiece().getActualVolumeMetric() != null && lclBlAc.getLclBlPiece().getActualVolumeMetric().doubleValue() != 0.00) {
                                    lclBlAc.setTotalMeasure(lclBlAc.getLclBlPiece().getActualVolumeMetric());
                                } else if (lclBlAc.getLclBlPiece().getBookedVolumeMetric() != null && lclBlAc.getLclBlPiece().getBookedVolumeMetric().doubleValue() != 0.00) {
                                    lclBlAc.setTotalMeasure(lclBlAc.getLclBlPiece().getBookedVolumeMetric());
                                }
                            }
                        }

                    }
                }
                chargesInfoMap.put(lclBlAc.getArglMapping().getChargeCode(), lclBlAc);
            } else {
                LclBlAc lclBookingAcFromMap = (LclBlAc) chargesInfoMap.get(lclBlAc.getArglMapping().getChargeCode());
                if (!lclBookingAcFromMap.getRatePerUnitUom().equalsIgnoreCase("FL") || lclBlAc.getArglMapping().getChargeCode().equalsIgnoreCase("OFBARR")
                        || lclBlAc.getArglMapping().getChargeCode().equalsIgnoreCase("TTBARR")) {
                    BigDecimal total = new BigDecimal(lclBlAc.getArAmount().doubleValue() + lclBookingAcFromMap.getRolledupCharges().doubleValue());
                    total = total.setScale(2, BigDecimal.ROUND_HALF_UP);
                    lclBookingAcFromMap.setRolledupCharges(total);
                }
                if (lclBookingAcFromMap.getRatePerUnitUom() != null && lclBookingAcFromMap.getRateUom() != null && lclBookingAcFromMap.getArglMapping() != null
                        && lclBookingAcFromMap.getArglMapping().getBlueScreenChargeCode() != null && !lclBookingAcFromMap.getArglMapping().getBlueScreenChargeCode().equals("0032")
                        && !lclBookingAcFromMap.getArglMapping().getBlueScreenChargeCode().equals("0232")) {
                    if (lclBookingAcFromMap.getRatePerUnitUom().equalsIgnoreCase("V") || lclBookingAcFromMap.getRatePerUnitUom().equalsIgnoreCase("W")
                            || lclBookingAcFromMap.getRatePerUnitUom().equalsIgnoreCase("M")) {
                        if (lclBookingAcFromMap.getRateUom().equalsIgnoreCase("I")) {
                            if (lclBlAc.getLclBlPiece() != null) {
                                if (lclBlAc.getLclBlPiece().getActualWeightImperial() != null && lclBlAc.getLclBlPiece().getActualWeightImperial().doubleValue() != 0.00) {
                                    if (!CommonUtils.isEmpty(lclBookingAcFromMap.getTotalWeight())) {
                                        lclBookingAcFromMap.setTotalWeight(new BigDecimal(lclBookingAcFromMap.getTotalWeight().doubleValue()
                                                + lclBlAc.getLclBlPiece().getActualWeightImperial().doubleValue()));
                                    }
                                } else if (lclBlAc.getLclBlPiece().getBookedWeightImperial() != null && lclBlAc.getLclBlPiece().getBookedWeightImperial().doubleValue() != 0.00) {
                                    if (!CommonUtils.isEmpty(lclBookingAcFromMap.getTotalWeight())) {
                                        lclBookingAcFromMap.setTotalWeight(new BigDecimal(lclBookingAcFromMap.getTotalWeight().doubleValue()
                                                + lclBlAc.getLclBlPiece().getBookedWeightImperial().doubleValue()));
                                    }
                                }
                                if (lclBlAc.getLclBlPiece().getActualVolumeImperial() != null && lclBlAc.getLclBlPiece().getActualVolumeImperial().doubleValue() != 0.00) {
                                    if (!CommonUtils.isEmpty(lclBookingAcFromMap.getTotalMeasure())) {
                                        lclBookingAcFromMap.setTotalMeasure(new BigDecimal(lclBookingAcFromMap.getTotalMeasure().doubleValue()
                                                + lclBlAc.getLclBlPiece().getActualVolumeImperial().doubleValue()));
                                    }
                                } else if (lclBlAc.getLclBlPiece().getBookedVolumeImperial() != null && lclBlAc.getLclBlPiece().getBookedVolumeImperial().doubleValue() != 0.00) {
                                    if (!CommonUtils.isEmpty(lclBookingAcFromMap.getTotalMeasure())) {
                                        lclBookingAcFromMap.setTotalMeasure(new BigDecimal(lclBookingAcFromMap.getTotalMeasure().doubleValue()
                                                + lclBlAc.getLclBlPiece().getBookedVolumeImperial().doubleValue()));
                                    }
                                }
                                if (!CommonUtils.isEmpty(lclBookingAcFromMap.getTotalWeight())) {
                                    calculatedWeight = (lclBookingAcFromMap.getTotalWeight().doubleValue() / 100) * calculatedWeight;
                                }
                            }
                        } else if (lclBookingAcFromMap.getRateUom().equalsIgnoreCase("M")) {
                            if (lclBlAc.getLclBlPiece() != null) {
                                if (lclBlAc.getLclBlPiece().getActualWeightMetric() != null && lclBlAc.getLclBlPiece().getActualWeightMetric().doubleValue() != 0.00) {
                                    if (!CommonUtils.isEmpty(lclBookingAcFromMap.getTotalWeight())) {
                                        lclBookingAcFromMap.setTotalWeight(new BigDecimal(lclBookingAcFromMap.getTotalWeight().doubleValue()
                                                + lclBlAc.getLclBlPiece().getActualWeightMetric().doubleValue()));
                                    }
                                } else if (lclBlAc.getLclBlPiece().getBookedWeightMetric() != null && lclBlAc.getLclBlPiece().getBookedWeightMetric().doubleValue() != 0.00) {
                                    if (!CommonUtils.isEmpty(lclBookingAcFromMap.getTotalWeight())) {
                                        lclBookingAcFromMap.setTotalWeight(new BigDecimal(lclBookingAcFromMap.getTotalWeight().doubleValue()
                                                + lclBlAc.getLclBlPiece().getBookedWeightMetric().doubleValue()));
                                    }
                                }
                                if (lclBlAc.getLclBlPiece().getActualVolumeMetric() != null && lclBlAc.getLclBlPiece().getActualVolumeMetric().doubleValue() != 0.00) {
                                    if (!CommonUtils.isEmpty(lclBookingAcFromMap.getTotalMeasure())) {
                                        lclBookingAcFromMap.setTotalMeasure(new BigDecimal(lclBookingAcFromMap.getTotalMeasure().doubleValue()
                                                + lclBlAc.getLclBlPiece().getActualVolumeMetric().doubleValue()));
                                    }
                                } else if (lclBlAc.getLclBlPiece().getBookedVolumeMetric() != null && lclBlAc.getLclBlPiece().getBookedVolumeMetric().doubleValue() != 0.00) {
                                    if (!CommonUtils.isEmpty(lclBookingAcFromMap.getTotalMeasure())) {
                                        lclBookingAcFromMap.setTotalMeasure(new BigDecimal(lclBookingAcFromMap.getTotalMeasure().doubleValue()
                                                + lclBlAc.getLclBlPiece().getBookedVolumeMetric().doubleValue()));
                                    }
                                }
                                if (!CommonUtils.isEmpty(lclBookingAcFromMap.getTotalWeight()) && !CommonUtils.isEmpty(lclBlAc.getRatePerWeightUnit())) {
                                    calculatedWeight = (lclBookingAcFromMap.getTotalWeight().doubleValue() / 1000) * lclBlAc.getRatePerWeightUnit().doubleValue();
                                }
                            }
                        }
                        if (!CommonUtils.isEmpty(lclBookingAcFromMap.getTotalMeasure()) && !CommonUtils.isEmpty(lclBlAc.getRatePerVolumeUnit())) {
                            calculatedMeasure = lclBookingAcFromMap.getTotalMeasure().doubleValue() * lclBlAc.getRatePerVolumeUnit().doubleValue();
                        }
                        minchg = lclBlAc.getRateFlatMinimum().doubleValue();
                        if (calculatedWeight >= calculatedMeasure && calculatedWeight >= minchg) {
                            lclBookingAcFromMap.setRolledupCharges(new BigDecimal(calculatedWeight).setScale(2, BigDecimal.ROUND_HALF_UP));
                        } else if (calculatedMeasure >= calculatedWeight && calculatedMeasure >= minchg) {
                            lclBookingAcFromMap.setRolledupCharges(new BigDecimal(calculatedMeasure).setScale(2, BigDecimal.ROUND_HALF_UP));
                        } else {
                            lclBookingAcFromMap.setRolledupCharges(new BigDecimal(minchg).setScale(2, BigDecimal.ROUND_HALF_UP));
                        }
                    }
                }
                chargesInfoMap.put(lclBlAc.getArglMapping().getChargeCode(), lclBookingAcFromMap);
            }
        }
        List rolledChargesList = new ArrayList(chargesInfoMap.values());
        return rolledChargesList;
    }

    public List setBillingTypeList() {
        List vendortype = new ArrayList();
        vendortype.add(new LabelValueBean("Forwarder", "F"));
        vendortype.add(new LabelValueBean("Shipper", "S"));
        vendortype.add(new LabelValueBean("Agent", "A"));
        vendortype.add(new LabelValueBean("ThirdParty", "T"));
        return vendortype;
    }

    public void createLclCorrections(User user, boolean approved, LclCorrection lclCorrection,
            boolean isManifest, LclBl bl) throws Exception {
        try {
            AccrualsDAO accrualsDAO = new AccrualsDAO();
            ManifestModel model = null;
            model = getLCLDetails(lclCorrection.getId(), lclCorrection.getLclFileNumber().getId());
            model.setCorrection(true);
            model.setManifest(isManifest);
            model.setReportingDate(model.getSailDate());
            model.setPostedDate(model.getReportingDate() != null ? accrualsDAO.getPostedDate(model.getReportingDate()) : new Date());
            new LCLBlDAO().getCurrentSession().evict(bl);
            if (bl.getShipAcct() != null && !bl.getShipAcct().equals(lclCorrection.getNewShipper())) {
                bl.setShipAcct(lclCorrection.getNewShipper());
                bl.getShipContact().setCompanyName(lclCorrection.getNewShipper().getAccountName());
                bl.setShipContact(this.getContactForBl(lclCorrection.getNewShipper().getAccountno(),
                        bl.getShipContact(), bl.getLclFileNumber(), "blShipper", user));
            } else if (bl.getShipAcct() == null && null != lclCorrection.getNewShipper()) {
                bl.setShipAcct(lclCorrection.getNewShipper());
                bl.getShipContact().setCompanyName(lclCorrection.getNewShipper().getAccountName());
                bl.setShipContact(this.getContactForBl(lclCorrection.getNewShipper().getAccountno(),
                        bl.getShipContact(), bl.getLclFileNumber(), "blShipper", user));
            }
            if (bl.getAgentAcct() != null && !bl.getAgentAcct().equals(lclCorrection.getNewAgent())) {
                bl.setAgentAcct(lclCorrection.getNewAgent());
                bl.getAgentContact().setCompanyName(lclCorrection.getNewAgent().getAccountName());
                bl.setAgentContact(this.getContactForBl(lclCorrection.getNewAgent().getAccountno(),
                        bl.getAgentContact(), bl.getLclFileNumber(), "blAgent", user));
            } else if (bl.getAgentAcct() == null && null != lclCorrection.getNewAgent()) {
                bl.setAgentAcct(lclCorrection.getNewAgent());
                bl.getAgentContact().setCompanyName(lclCorrection.getNewAgent().getAccountName());
                bl.setAgentContact(this.getContactForBl(lclCorrection.getNewAgent().getAccountno(),
                        bl.getAgentContact(), bl.getLclFileNumber(), "blAgent", user));
            }
            if (bl.getFwdAcct() != null && !bl.getFwdAcct().equals(lclCorrection.getNewForwarder())) {
                bl.setFwdAcct(lclCorrection.getNewForwarder());
                bl.getFwdContact().setCompanyName(lclCorrection.getNewForwarder().getAccountName());
                bl.setFwdContact(this.getContactForBl(lclCorrection.getNewForwarder().getAccountno(),
                        bl.getFwdContact(), bl.getLclFileNumber(), "blForwarder", user));
            } else if (bl.getFwdAcct() == null && null != lclCorrection.getNewForwarder()) {
                bl.setFwdAcct(lclCorrection.getNewForwarder());
                bl.getFwdContact().setCompanyName(lclCorrection.getNewForwarder().getAccountName());
                bl.setFwdContact(this.getContactForBl(lclCorrection.getNewForwarder().getAccountno(),
                        bl.getFwdContact(), bl.getLclFileNumber(), "blForwarder", user));
            }
            if (bl.getThirdPartyAcct() != null && !bl.getThirdPartyAcct().equals(lclCorrection.getNewThirdParty())) {
                bl.setThirdPartyAcct(lclCorrection.getNewThirdParty());
                bl.getThirdPartyContact().setCompanyName(lclCorrection.getNewThirdParty().getAccountName());
                bl.setThirdPartyContact(this.getContactForBl(lclCorrection.getNewThirdParty().getAccountno(),
                        bl.getThirdPartyContact(), bl.getLclFileNumber(), "blThirdParty", user));
            } else if (bl.getThirdPartyAcct() == null && null != lclCorrection.getNewThirdParty()) {
                bl.setThirdPartyAcct(lclCorrection.getNewThirdParty());
                bl.getThirdPartyContact().setCompanyName(lclCorrection.getNewThirdParty().getAccountName());
                bl.setThirdPartyContact(this.getContactForBl(lclCorrection.getNewThirdParty().getAccountno(),
                        bl.getThirdPartyContact(), bl.getLclFileNumber(), "blThirdParty", user));
            }

            if (lclCorrection.getCode().getCode().equalsIgnoreCase("019")) {
                List<CorrectionCommodity> commodityList = new CorrectionCommodityDAO()
                        .getCommodityCodeList(lclCorrection.getId());
                List<LclBlPiece> blPieceList = new LclBLPieceDAO().findByProperty("lclFileNumber.id", bl.getFileNumberId());
                for (int i = 0; i < commodityList.size(); i++) {
                    CorrectionCommodity commodity = commodityList.get(i);
                    LclBlPiece blPiece = blPieceList.get(i);
                    blPiece.setCommodityType(commodity.getCommodityType());
                    blPiece.setActualVolumeImperial(commodity.getTotalCft());
                    blPiece.setActualVolumeMetric(commodity.getTotalCbm());
                    blPiece.setActualWeightImperial(commodity.getTotalLbs());
                    blPiece.setActualWeightMetric(commodity.getTotalKgs());
                    new LclBLPieceDAO().saveOrUpdate(blPiece);
                }
            }

            new LCLBlDAO().saveOrUpdate(bl);
            if (CommonUtils.in(lclCorrection.getType().getCode(), "A")) {
                correction_A(model, user, lclCorrection.getType().getCode(), lclCorrection);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Error in correction . " + new Date() + " for ", e);
        }
    }

    public void correction_A(ManifestModel model, User user, String correctionType, LclCorrection lclCorrection) throws Exception {
        try {
            List<LclCorrectionCharge> chargeList = (List<LclCorrectionCharge>) lclCorrection.getLclCorrectionChargeCollection();
            Map<String, Double> arCustomer = new HashMap<String, Double>();
            Map<String, String> arCustomersBillToParty = new HashMap<String, String>();
            Map<String, String> arBlNumber = new HashMap<String, String>();
            for (LclCorrectionCharge charge : chargeList) {
                String old_acct_No = "", old_acct_Name = "", new_acct_No = "", new_acct_Name = "";

                String previous_billToParty = charge.getOldBillToParty() != null ? charge.getOldBillToParty() : "";
                if (CommonUtils.isNotEmpty(previous_billToParty)) {
                    if (previous_billToParty.equalsIgnoreCase("A") && null != lclCorrection.getOldAgent()) {
                        old_acct_No = lclCorrection.getOldAgent().getAccountno();
                        old_acct_Name = lclCorrection.getOldAgent().getAccountName();
                    } else if (previous_billToParty.equalsIgnoreCase("S") && null != lclCorrection.getOldShipper()) {
                        old_acct_No = lclCorrection.getOldShipper().getAccountno();
                        old_acct_Name = lclCorrection.getOldShipper().getAccountName();
                    } else if (previous_billToParty.equalsIgnoreCase("F") && null != lclCorrection.getOldForwarder()) {
                        old_acct_No = lclCorrection.getOldForwarder().getAccountno();
                        old_acct_Name = lclCorrection.getOldForwarder().getAccountName();
                    } else if (previous_billToParty.equalsIgnoreCase("T") && null != lclCorrection.getThirdPartyAcct()) {
                        old_acct_No = lclCorrection.getThirdPartyAcct().getAccountno();
                        old_acct_Name = lclCorrection.getThirdPartyAcct().getAccountName();
                    }
                }

                if (CommonUtils.isNotEmpty(charge.getBillToParty())) {
                    if (charge.getBillToParty().equalsIgnoreCase("A") && null != lclCorrection.getNewAgent()) {
                        new_acct_No = lclCorrection.getNewAgent().getAccountno();
                        new_acct_Name = lclCorrection.getNewAgent().getAccountName();
                    } else if (charge.getBillToParty().equalsIgnoreCase("S") && null != lclCorrection.getNewShipper()) {
                        new_acct_No = lclCorrection.getNewShipper().getAccountno();
                        new_acct_Name = lclCorrection.getNewShipper().getAccountName();
                    } else if (charge.getBillToParty().equalsIgnoreCase("F") && null != lclCorrection.getNewForwarder()) {
                        new_acct_No = lclCorrection.getNewForwarder().getAccountno();
                        new_acct_Name = lclCorrection.getNewForwarder().getAccountName();
                    } else if (charge.getBillToParty().equalsIgnoreCase("T") && null != lclCorrection.getNewThirdParty()) {
                        new_acct_No = lclCorrection.getNewThirdParty().getAccountno();
                        new_acct_Name = lclCorrection.getNewThirdParty().getAccountName();
                    }
                }
                if (!charge.getBillToParty().equalsIgnoreCase(previous_billToParty)) {
                    // -----------------New Customer +ve Entry --------------------//

                    model.setBillToParty(charge.getBillToParty());
                    model.setCustomerNumber(new_acct_No);
                    model.setCustomerName(new_acct_Name);
                    setModelCommonData(model, charge, lclCorrection);

                    double amount = charge.getNewAmount().doubleValue();
                    model.setAmount(amount);
                    if (arCustomer.containsKey(model.getCustomerNumber())) {
                        arCustomer.put(model.getCustomerNumber(), arCustomer.get(model.getCustomerNumber()) + (amount));
                    } else {
                        arCustomer.put(model.getCustomerNumber(), (amount));
                        arCustomersBillToParty.put(model.getCustomerNumber(), model.getBillToParty());
                        arBlNumber.put(model.getCustomerNumber(), model.getBlNumber());
                    }
                    createLclArSubledgers(model, lclCorrection.getCorrectionNo(), amount, user);
                    // -----------------Old Customer -ve Entry --------------------//

                    amount = charge.getOldAmount().doubleValue();
                    if (amount != 0.0) {
                        model.setBillToParty(previous_billToParty);
                        model.setCustomerNumber(old_acct_No);
                        model.setCustomerName(old_acct_Name);
                        setModelCommonData(model, charge, lclCorrection);
                        model.setAmount(amount);
                        if (arCustomer.containsKey(model.getCustomerNumber())) {
                            arCustomer.put(model.getCustomerNumber(), arCustomer.get(model.getCustomerNumber()) + (-amount));
                        } else {
                            arCustomer.put(model.getCustomerNumber(), (-amount));
                            arCustomersBillToParty.put(model.getCustomerNumber(), model.getBillToParty());
                            arBlNumber.put(model.getCustomerNumber(), model.getBlNumber());
                        }
                        createLclArSubledgers(model, lclCorrection.getCorrectionNo(), -amount, user);
                    }
                } else if (charge.getBillToParty().equalsIgnoreCase(previous_billToParty)
                        && !new_acct_No.equalsIgnoreCase(old_acct_No)) {
                    //   -----------------New Customer +ve Entry --------------------//

                    model.setBillToParty(charge.getBillToParty());
                    model.setCustomerNumber(new_acct_No);
                    model.setCustomerName(new_acct_Name);
                    setModelCommonData(model, charge, lclCorrection);

                    double amount = charge.getNewAmount().doubleValue();
                    model.setAmount(amount);
                    if (arCustomer.containsKey(model.getCustomerNumber())) {
                        arCustomer.put(model.getCustomerNumber(), arCustomer.get(model.getCustomerNumber()) + (amount));
                    } else {
                        arCustomer.put(model.getCustomerNumber(), (amount));
                        arCustomersBillToParty.put(model.getCustomerNumber(), model.getBillToParty());
                        arBlNumber.put(model.getCustomerNumber(), model.getBlNumber());
                    }
                    createLclArSubledgers(model, lclCorrection.getCorrectionNo(), amount, user);
                    // -----------------Old Customer -ve Entry --------------------//

                    amount = charge.getOldAmount().doubleValue();
                    if (amount != 0.0) {
                        model.setBillToParty(previous_billToParty);
                        model.setCustomerNumber(old_acct_No);
                        model.setCustomerName(old_acct_Name);
                        setModelCommonData(model, charge, lclCorrection);
                        model.setAmount(amount);
                        if (arCustomer.containsKey(model.getCustomerNumber())) {
                            arCustomer.put(model.getCustomerNumber(), arCustomer.get(model.getCustomerNumber()) + (-amount));
                        } else {
                            arCustomer.put(model.getCustomerNumber(), (-amount));
                            arCustomersBillToParty.put(model.getCustomerNumber(), model.getBillToParty());
                            arBlNumber.put(model.getCustomerNumber(), model.getBlNumber());
                        }
                        createLclArSubledgers(model, lclCorrection.getCorrectionNo(), -amount, user);
                    }
                } else if (charge.getBillToParty().equalsIgnoreCase(previous_billToParty)
                        && new_acct_No.equalsIgnoreCase(old_acct_No)
                        && !charge.getOldAmount().equals(charge.getNewAmount())) {

                    model.setBillToParty(previous_billToParty);
                    model.setCustomerNumber(old_acct_No);
                    model.setCustomerName(old_acct_Name);
                    setModelCommonData(model, charge, lclCorrection);

                    double amount = charge.getNewAmount().subtract(charge.getOldAmount()).doubleValue();
                    model.setAmount(amount);
                    if (arCustomer.containsKey(model.getCustomerNumber())) {
                        arCustomer.put(model.getCustomerNumber(), arCustomer.get(model.getCustomerNumber()) + (amount));
                    } else {
                        arCustomer.put(model.getCustomerNumber(), (amount));
                        arCustomersBillToParty.put(model.getCustomerNumber(), model.getBillToParty());
                        arBlNumber.put(model.getCustomerNumber(), model.getBlNumber());
                    }
                    createLclArSubledgers(model, lclCorrection.getCorrectionNo(), amount, user);

                }
            }
            model.setArCustomers(arCustomer);
            model.setArCustomersBillToParty(arCustomersBillToParty);
            model.setCorrectionBlNumber(arBlNumber);
            createLclArInvoice(model, user, lclCorrection);
        } catch (Exception e) {
            log.info("Error in Approve correction . " + new Date() + " for ", e);
        }
    }

    public ManifestModel setModelCommonData(ManifestModel model,
            LclCorrectionCharge charge, LclCorrection lclCorrection) throws Exception {
        model.setBluescreenChargeCode(charge.getGlMapping().getBlueScreenChargeCode());
        model.setChargeCode(charge.getGlMapping().getChargeCode());
        String originId = lclCorrection.getLclFileNumber().getLclBl().getPortOfOrigin().getId().toString();
        String bookingType = lclCorrection.getLclFileNumber().getLclBooking().getBookingType();
        if(bookingType.equalsIgnoreCase("T")){
            originId = lclCorrection.getLclFileNumber().getLclBl().getPortOfDestination().getId().toString();
        }
        String voyOriginID = new LclSsHeaderDAO().getvoyageOriginIdWithFileId(lclCorrection.getLclFileNumber().getId().toString());
        model.setGlAccount(new GlMappingDAO().getLclExportDerivedGlAccount(
                charge.getGlMapping().getId().toString(), model.getTerminal(),originId,voyOriginID));
        boolean isCreditDebitNote = new CustomerAccountingDAO().isCreditDebitNote(model.getCustomerNumber());
        if (isCreditDebitNote && !model.getBlNumber().contains("CN")) {
            model.setBlNumber(model.getBlNumber() + "CN" + lclCorrection.getCorrectionNo());
        }
        return model;
    }

    public ManifestModel getLCLDetails(Long correctionId, Long fileId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        CodetypeDAO codeTypeDAO = new CodetypeDAO();
        Integer codeTypeId = codeTypeDAO.getCodeTypeId("Vessel Codes");
        queryBuilder.append("SELECT SUBSTRING(BlNumberSystemForLclExports(file.id),6)  AS blNumber,");
        queryBuilder.append("file.file_number AS dockReceipt,unit_ss.sp_booking_no AS bookingNumber,ss_head.schedule_no AS ");
        queryBuilder.append("voyageNumber,ss_detail.std AS sailDate,ss_detail.sta AS eta, ");
        queryBuilder.append("steamshipline.acct_name AS streamShipLine,unit.unit_no AS containerNumbers,");
        queryBuilder.append(" UnLocationGetNameStateCntryByID(org.id)AS origin,");
        queryBuilder.append(" UnLocationGetNameStateCntryByID(dest.id) AS destination,");
        queryBuilder.append(" ship.acct_no AS shipperNo,ship.acct_name AS shipperName,cons.acct_no AS ");
        queryBuilder.append("consigneeNo, notify.acct_no AS notifyNo,notify.acct_name AS notifyName,");
        queryBuilder.append("cons.acct_name AS consigneeName,fwd.acct_no AS forwarderNo,fwd.acct_name AS forwarderName,tprty.acct_no AS ");
        queryBuilder.append("thirdPartyNo,tprty.acct_name AS thirdPartyName,agent.acct_no AS agentNo,agent.acct_name AS agentName,bl.billing_terminal AS");
        queryBuilder.append(" terminal,vessel.code AS vesselNo,vessel.codedesc AS vesselName,org.un_loc_code AS pooCode  ");
        queryBuilder.append(" FROM lcl_file_number FILE JOIN lcl_bl bl ON file.id = bl.file_number_id JOIN un_location org ON org.id = bl.poo_id ");
        queryBuilder.append(" JOIN un_location dest ON dest.id = IF(bl.fd_id  IS NOT NULL,bl.fd_id ,bl.pod_id) ");
        queryBuilder.append(" LEFT JOIN trading_partner ship ON bl.ship_acct_no =ship.acct_no  ");
        queryBuilder.append(" LEFT JOIN trading_partner cons ON bl.cons_acct_no = cons.acct_no ");
        queryBuilder.append(" LEFT JOIN trading_partner fwd ON bl.fwd_acct_no = fwd.acct_no ");
        queryBuilder.append(" LEFT JOIN trading_partner tprty ON bl.third_party_acct_no = tprty.acct_no ");
        queryBuilder.append(" LEFT JOIN trading_partner agent ON bl.agent_acct_no = agent.acct_no ");
        queryBuilder.append(" LEFT JOIN trading_partner notify ON bl.noty_acct_no = notify.acct_no ");
        queryBuilder.append(" JOIN lcl_booking_piece book_piece ON bl.file_number_id = book_piece.file_number_id ");
        queryBuilder.append(" JOIN lcl_booking_piece_unit unit_pieces ON book_piece.id = unit_pieces.booking_piece_id ");
        queryBuilder.append(" JOIN lcl_unit_ss unit_ss ON unit_pieces.lcl_unit_ss_id =unit_ss.id JOIN lcl_unit unit ON unit_ss.unit_id = ");
        queryBuilder.append(" unit.id JOIN lcl_ss_header ss_head ON unit_ss.ss_header_id = ss_head.id AND ss_head.service_type IN('E','C') ");
        queryBuilder.append(" join lcl_ss_detail ss_detail on ss_detail.ss_header_id = ss_head.id and  ss_detail.id = ");
        queryBuilder.append(" (select ls.id from lcl_ss_detail ls where ls.ss_header_id = ss_head.id order by ls.id desc limit 1) ");
        queryBuilder.append(" LEFT JOIN trading_partner steamshipline ON steamshipline.acct_no = ss_detail.sp_acct_no ");
        queryBuilder.append(" LEFT JOIN genericcode_dup vessel ON ss_detail.sp_reference_name = vessel.codedesc AND vessel.codetypeid = ");
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

    public void createLclArSubledgers(ManifestModel model, Long correctionNo, double amount, User user) throws Exception {
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
        arSubledger.setBillLaddingNo(model.getBlNumber());
        arSubledger.setBookingNo(model.getBookingNumber());
        arSubledger.setDocReceipt(model.getDockReceipt());
        arSubledger.setVoyageNo(model.getVoyageNumber());
        arSubledger.setContainerNo(model.getContainerNumbers());
        arSubledger.setMasterBl(model.getMasterBl());
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
        arSubledger.setCorrectionNotice(FclBlConstants.CNA00 + correctionNo);
        arSubledger.setNoticeNumber(FclBlConstants.CNA00 + correctionNo);
        arSubledger.setManifestFlag(model.isManifest() ? YES : NO);
        arSubledger.setCreatedOn(new Date());
        arSubledger.setCreatedBy(user.getUserId());
        arSubledger.setInvoiceNumber(model.getDockReceipt());
        subledgerDAO.save(arSubledger);
    }

    public void createLclArInvoice(ManifestModel model, User user, LclCorrection lclCorrection) throws Exception {
        synchronized (this) {
            TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
            CustomerAccountingDAO accountingDAO = new CustomerAccountingDAO();
            AccountingTransactionDAO transactionDAO = new AccountingTransactionDAO();
            String dockReceipt = model.getDockReceipt();
            for (String custNo : model.getArCustomers().keySet()) {
                String arBlNumber = model.getCorrectionBlNumber() == null ? model.getBlNumber() : model.getCorrectionBlNumber().get(custNo);
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
        history.setTransactionType(model.isCorrection() ? "LCL CN" : model.isManifest() ? "LCL BL" : "LCL VOID");
        history.setTransactionDate(new Date());
        history.setCreatedBy(user.getLoginName());
        history.setCreatedDate(new Date());
        if (model.isCorrection() && lclCorrection != null) {
            history.setCorrectionNotice(FclBlConstants.CNA00 + lclCorrection.getCorrectionNo());
        } else {
            history.setCorrectionNotice(FclBlConstants.CNA0);
        }
        historyDAO.save(history);
    }

    public void updateLclArSubledgerInvoiceNumber(String blNo, String customerNo, String invoiceNo) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update transaction_ledger ");
        queryBuilder.append("set Invoice_number  = '").append(invoiceNo);
        queryBuilder.append("' where cust_no = '").append(customerNo).append("' and Bill_Ladding_No ='").append(blNo).append("'");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        getCurrentSession().flush();
    }

    public void setChargeList(Long correctionId, LCLCorrectionForm lclCorrectionForm, HttpServletRequest request) throws Exception {
        BigDecimal current_Profit = BigDecimal.ZERO;
        BigDecimal profit_After_CN = BigDecimal.ZERO;
        List<LCLCorrectionChargeBean> correctionChargeList = new ArrayList<LCLCorrectionChargeBean>();
        List<LclCorrectionCharge> correctedList = new LCLCorrectionChargeDAO().getLclCorrectionChargesList(correctionId);         
        for (int x=0; x < correctedList.size() ; x++) {
            LclCorrectionCharge ch = correctedList.get(x);
            String previous_billToParty = ch.getOldBillToParty() != null ? ch.getOldBillToParty() : "";
            LCLCorrectionChargeBean ch_Bean = new LCLCorrectionChargeBean();
            ch_Bean.setCorrectionChargeId(ch.getId());
            ch_Bean.setOldAmount(ch.getOldAmount());
            ch_Bean.setBillToPartyLabel(new BlCorrectionUtils().setBillToPartyForCorrectionCharges(ch.getBillToParty()));
            if (ch.getNewAmount() != null && !ch.getNewAmount().equals(ch.getOldAmount())) {
                ch_Bean.setNewAmount(ch.getNewAmount());
                ch_Bean.setDifferenceAmount(ch.getNewAmount().subtract(ch_Bean.getOldAmount()));
            }
            ch_Bean.setDelete(!previous_billToParty.equalsIgnoreCase(ch.getBillToParty())
                    || ch_Bean.getDifferenceAmount() != null);
            ch_Bean.setChargeId(ch.getGlMapping().getId());
            ch_Bean.setChargeCode(ch.getGlMapping().getChargeCode());
            ch_Bean.setChargeDescriptions(ch.getGlMapping().getChargeDescriptions());
            ch_Bean.setManualcharge(ch.isManualCharge());
            correctionChargeList.add(ch_Bean);
            current_Profit = current_Profit.add(ch_Bean.getOldAmount());
            profit_After_CN = profit_After_CN.add(ch_Bean.getDifferenceAmount() != null ? ch.getNewAmount() : ch.getOldAmount());
            request.setAttribute("lclCorrectionChargesList", correctionChargeList);
            lclCorrectionForm.setCurrentProfit(current_Profit.toString());
            lclCorrectionForm.setProfitAfterCN(profit_After_CN.toString());
        }
    }

    public List<LCLCorrectionChargeBean> setPreviousChargeList(Long correctionId,
            LCLCorrectionForm lclCorrectionForm, HttpServletRequest request) throws Exception {
        BigDecimal current_Profit = BigDecimal.ZERO;
        BigDecimal profit_After_CN = BigDecimal.ZERO;
        List<LCLCorrectionChargeBean> correctionChargeList = new ArrayList<LCLCorrectionChargeBean>();
        List<LclCorrectionCharge> correctedList = new LCLCorrectionChargeDAO().findByProperty("lclCorrection.id", correctionId);
        for (int x = correctedList.size() - 1; x >= 0; x--) {
            LclCorrectionCharge ch = correctedList.get(x);
            LCLCorrectionChargeBean ch_Bean = new LCLCorrectionChargeBean();
            ch_Bean.setCorrectionChargeId(ch.getId());
            ch_Bean.setOldAmount(ch.getNewAmount() != null ? ch.getNewAmount() : ch.getOldAmount());
            ch_Bean.setBillToPartyLabel(new BlCorrectionUtils().setBillToPartyForCorrectionCharges(ch.getBillToParty()));
            ch_Bean.setChargeId(ch.getGlMapping().getId());
            ch_Bean.setChargeCode(ch.getGlMapping().getChargeCode());
            ch_Bean.setChargeDescriptions(ch.getGlMapping().getChargeDescriptions());
            ch_Bean.setManualcharge(ch.isManualCharge());
            correctionChargeList.add(ch_Bean);
            current_Profit = current_Profit.add(ch_Bean.getOldAmount());
            profit_After_CN = profit_After_CN.add(ch_Bean.getNewAmount() == null ? ch_Bean.getOldAmount() : ch_Bean.getNewAmount());
            request.setAttribute("lclCorrectionChargesList", correctionChargeList);
            lclCorrectionForm.setCurrentProfit(current_Profit.toString());
            lclCorrectionForm.setProfitAfterCN(profit_After_CN.toString());
        }
        return correctionChargeList;
    }

    public List<LCLCorrectionChargeBean> setBLChargeList(List<LclBlAc> lclBlAcList,
            LCLCorrectionForm lclCorrectionForm, HttpServletRequest request) throws Exception {
        BigDecimal current_Profit = BigDecimal.ZERO;
        BigDecimal profit_After_CN = BigDecimal.ZERO;
        List<LCLCorrectionChargeBean> correctionChargeList = new ArrayList<LCLCorrectionChargeBean>();
        for (LclBlAc bl : lclBlAcList) {
            LCLCorrectionChargeBean ch_Bean = new LCLCorrectionChargeBean();
            ch_Bean.setCorrectionChargeId(bl.getId());
            if (null != bl.getRolledupCharges()) {
                ch_Bean.setOldAmount(bl.getRolledupCharges().add(bl.getAdjustmentAmount()));
            } else {
                ch_Bean.setOldAmount(bl.getArAmount().add(bl.getAdjustmentAmount()));
            }
            ch_Bean.setBillToPartyLabel(setBillToPartyForCorrectionCharges(bl.getArBillToParty()));
            ch_Bean.setDelete(false);
            ch_Bean.setChargeId(bl.getArglMapping().getId());
            ch_Bean.setChargeCode(bl.getArglMapping().getChargeCode());
            ch_Bean.setChargeDescriptions(bl.getArglMapping().getChargeDescriptions());
            ch_Bean.setManualcharge(bl.isManualEntry());
            correctionChargeList.add(ch_Bean);
            current_Profit = current_Profit.add(ch_Bean.getOldAmount());
            profit_After_CN = profit_After_CN.add(ch_Bean.getOldAmount());
            request.setAttribute("lclCorrectionChargesList", correctionChargeList);
            lclCorrectionForm.setCurrentProfit(current_Profit.toString());
            lclCorrectionForm.setProfitAfterCN(profit_After_CN.toString());
        }
        return correctionChargeList;
    }

    public List<LCLCorrectionChargeBean> getFormattedCorrectionExportcharges(Integer creditDebitId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT gl.`Charge_code` as chargeCode,lc.`amount` as differenceAmount FROM lcl_credit_debit_charge lc ");
        sb.append(" JOIN gl_mapping gl ON gl.`id` = lc.`charge_id` where lc.credit_debit_id=:creditDebitId ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameter("creditDebitId", creditDebitId);
        query.setResultTransformer(Transformers.aliasToBean(LCLCorrectionChargeBean.class));
        query.addScalar("chargeCode", StringType.INSTANCE);
        query.addScalar("differenceAmount", BigDecimalType.INSTANCE);
        return (List<LCLCorrectionChargeBean>) query.list();
    }

    public CreditDebitNote insertCreditDebitNote(String fileNumber, String correctionNumber, String customerName, String customerNumber,
            String debitCreditNote, String billToParty) throws Exception {
        CreditDebitNoteDAO creditDebitNoteDAO = new CreditDebitNoteDAO();
        CreditDebitNote creditDebitNote = new CreditDebitNote();
        creditDebitNote.setBolid(fileNumber);
        creditDebitNote.setCorrectionNumber(correctionNumber);
        creditDebitNote.setCustomerName(customerName);
        creditDebitNote.setDebitCreditNote(debitCreditNote);
        creditDebitNote.setCustomerNumber(customerNumber);
        creditDebitNote.setBillToParty(billToParty);
        creditDebitNoteDAO.save(creditDebitNote);
        return creditDebitNote;
    }

    public LclContact getContactForBl(String acctNo, LclContact contact,
            LclFileNumber fileNumber, String contactType, User user) throws Exception {
        CustAddress cust = new CustAddressDAO().findByAccountNo(acctNo);
        LclContact newcontact = null;
        if (cust != null) {
            LclContact oldcontact = new LCLContactDAO().getContact(fileNumber.getId(), contactType);
            newcontact = oldcontact == null ? new LclContact() : oldcontact;
            String con_address = CommonUtils.isNotEmpty(cust.getAddress1()) ? cust.getAddress1() + "\n" : "";
            con_address += CommonUtils.isNotEmpty(cust.getCity()) ? cust.getCity() : "";
            con_address += CommonUtils.isNotEmpty(cust.getState()) && CommonUtils.isNotEmpty(cust.getCity()) ? ","
                    + cust.getState() : CommonUtils.isNotEmpty(cust.getState()) ? "" + cust.getState() : "";
            con_address += CommonUtils.isNotEmpty(cust.getCountry()) ? "," + cust.getCountry() + "\n" : "\n";
            con_address += CommonUtils.isNotEmpty(cust.getZip()) ? cust.getZip() : "";
            con_address += CommonUtils.isNotEmpty(cust.getPhone()) ? " " + "PHONE" + cust.getPhone() : "";

            newcontact.setCompanyName(contact.getCompanyName());
            newcontact.setAddress(con_address);
            newcontact.setLclFileNumber(fileNumber);
            newcontact.setRemarks(contactType);
            newcontact.setEnteredBy(user);
            newcontact.setModifiedBy(user);
            newcontact.setEnteredDatetime(new Date());
            newcontact.setModifiedDatetime(new Date());
        }
        return newcontact;
    }

    public void insertCreditDebitCharge(Integer id, Integer chargeId, BigDecimal amount) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("insert into lcl_credit_debit_charge(credit_debit_id,charge_id,amount)values(:id,:charge,:amount);");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameter("id", id);
        query.setParameter("charge", chargeId);
        query.setParameter("amount", amount);
        query.executeUpdate();
    }

    public LclCorrection saveCorrection(LCLCorrectionForm correctionForm, LclBl bl,
            User loginUser, LCLCorrectionDAO correctionDAO) throws Exception {
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
        Date now = new Date();
        LclCorrection correction = correctionDAO.findById(correctionForm.getCorrectionId());
        if (correction == null) {
            BigInteger lastCorrectionNo = correctionDAO.getIntegerDescByFileIdWithoutVoid(correctionForm.getFileId(), "correction_no");
            if (lastCorrectionNo == null) {
                lastCorrectionNo = new BigInteger("0");
            }
            String concatenatedBlNo = "(" + correctionForm.getBlNo() + "-C-" + String.valueOf(lastCorrectionNo.intValue() + 1) + ")";
            correction = new LclCorrection();
            correction.setEnteredBy(loginUser);
            correction.setEnteredDatetime(now);
            correction.setCorrectionNo(lastCorrectionNo.intValue() + 1);
            new LclUtils().insertLCLCorrectionRemarks(REMARKS_TYPE_LCL_CORRECTIONS, correctionForm.getFileId(), concatenatedBlNo, "saved", loginUser);
            correction.setLclFileNumber(new LclFileNumber(correctionForm.getFileId()));
            correction.setOldShipper(bl.getShipAcct());
            correction.setOldAgent(bl.getAgentAcct());
            correction.setOldForwarder(bl.getFwdAcct());
            correction.setThirdPartyAcct(bl.getThirdPartyAcct());
        }
        correction.setModifiedBy(loginUser);
        correction.setModifiedDatetime(now);
        correction.setComments(CommonUtils.isNotEmpty(correctionForm.getComments())
                ? correctionForm.getComments() : null);
        correction.setCode(genericCodeDAO.findById(correctionForm.getCorrectionCode()));
        correction.setType(genericCodeDAO.findById(correctionForm.getCorrectionType()));
        TradingPartner newShipper = null, newAgent = null, newForwarder = null, newThirdParty = null;
        newShipper = CommonUtils.isNotEmpty(correctionForm.getExpShipperNo())
                ? tradingPartnerDAO.findById(correctionForm.getExpShipperNo()) : bl.getShipAcct();
        newAgent = CommonUtils.isNotEmpty(correctionForm.getExpAgentNo())
                ? tradingPartnerDAO.findById(correctionForm.getExpAgentNo()) : bl.getAgentAcct();
        newForwarder = CommonUtils.isNotEmpty(correctionForm.getExpForwarderNo())
                ? tradingPartnerDAO.findById(correctionForm.getExpForwarderNo()) : bl.getFwdAcct();
        newThirdParty = CommonUtils.isNotEmpty(correctionForm.getExpThirdPartyNo())
                ? tradingPartnerDAO.findById(correctionForm.getExpThirdPartyNo()) : bl.getThirdPartyAcct();
        correction.setNewShipper(newShipper);
        correction.setNewAgent(newAgent);
        correction.setNewForwarder(newForwarder);
        correction.setNewThirdParty(newThirdParty);

        correctionForm.setExpShipperNo(newShipper != null ? newShipper.getAccountno() : "");
        correctionForm.setExpShipperName(newShipper != null ? newShipper.getAccountName() : "");

        correctionForm.setExpAgentNo(newAgent != null ? newAgent.getAccountno() : "");
        correctionForm.setExpAgentName(newAgent != null ? newAgent.getAccountName() : "");

        correctionForm.setExpForwarderNo(newForwarder != null ? newForwarder.getAccountno() : "");
        correctionForm.setExpForwarderName(newForwarder != null ? newForwarder.getAccountName() : "");

        correctionForm.setExpThirdPartyNo(newThirdParty != null ? newThirdParty.getAccountno() : "");
        correctionForm.setExpThirdPartyName(newThirdParty != null ? newThirdParty.getAccountName() : "");

        correction.setStatus("O");
        correction.setVoid1(Boolean.FALSE);
        correctionDAO.saveOrUpdate(correction);
        return correction;
    }

    public void saveReCalculateCorrection(LCLCorrectionForm correctionForm, LclBl bl,
            List<ChargesInfoBean> chargeList, User loginUser, HttpServletRequest request) throws Exception {
        LCLCorrectionDAO correctionDAO = new LCLCorrectionDAO();
        List<LCLCorrectionChargeBean> correctionChargeList = new ArrayList<LCLCorrectionChargeBean>();
        LCLCorrectionChargeDAO correctionChargeDAO = new LCLCorrectionChargeDAO();
        Long previousCorrectionId = correctionChargeDAO.getPreviousCorrectionId(bl.getLclFileNumber().getId());
        if (previousCorrectionId == 0) {
            List<LclBlAc> lclBlAcList = new LclBlAcDAO().getLclChargesByFileNumberAsc(correctionForm.getFileId());
            BigInteger commodityCount = new LclBLPieceDAO().getPieceCountByFileId(correctionForm.getFileId());
            if (commodityCount.doubleValue() > 1) {
                lclBlAcList = this.getRolledUpChargesForBlCorrections(lclBlAcList);
            }
            correctionChargeList = this.setBLChargeList(lclBlAcList, correctionForm, request);
        } else if (null != previousCorrectionId && correctionForm.getCorrectionId() == 0) {
            correctionChargeList = this.setPreviousChargeList(previousCorrectionId, correctionForm, request);
        }
        LclCorrection correction = this.saveCorrection(correctionForm, bl, loginUser, correctionDAO);
        compareCorrectionChargeList(correction, bl, chargeList, correctionChargeList, loginUser);
        if (CommonUtils.isNotEmpty(correctionForm.getCommodityList())) {
            for (RateModel commodity : correctionForm.getCommodityList()) {
                new CorrectionCommodityDAO().createInstance(commodity, correction);
            }
        }
        this.setChargeList(correction.getId(), correctionForm, request);
        correctionForm.setCorrectionId(correction.getId());
        request.setAttribute("lclCorrection", correction);

        correctionDAO.updateCorrectionByField(correction.getId(), "current_profit", correctionForm.getCurrentProfit());
        correctionDAO.updateCorrectionByField(correction.getId(), "profit_aftercn", correctionForm.getProfitAfterCN());
        correctionDAO.updateCorrectionByField(correction.getId(), "debit_email", correctionForm.getDebitMemoEmail());
        correctionDAO.updateCorrectionByField(correction.getId(), "credit_email", correctionForm.getCreditMemoEmail());
    }

    public void compareCorrectionChargeList(LclCorrection correction, LclBl bl, List<ChargesInfoBean> chargeList,
            List<LCLCorrectionChargeBean> correctionChargeList, User loginUser) throws Exception {
        String eciPortCode = null != bl.getFinalDestination()
                ? bl.getFinalDestination().getUnLocationCode() : bl.getPortOfDestination().getUnLocationCode();
        Ports port = new PortsDAO().getPorts(eciPortCode);
        String engMet = null != port ? port.getEngmet() : "E";
        LinkedHashMap<String, LCLCorrectionChargeBean> existChargeMap = new LinkedHashMap<String, LCLCorrectionChargeBean>();
        LinkedHashMap<String, ChargesInfoBean> currentChargeMap = new LinkedHashMap<String, ChargesInfoBean>();
        LinkedHashMap<String, LCLCorrectionChargeBean> manualChargeMap = new LinkedHashMap<String, LCLCorrectionChargeBean>();

        for (LCLCorrectionChargeBean correctionCharge : correctionChargeList) {
            if (!correctionCharge.isManualcharge()) {
                existChargeMap.put(correctionCharge.getChargeCode(), correctionCharge);
            } else {
                manualChargeMap.put(correctionCharge.getChargeCode(), correctionCharge);
            }
        }
        if (!chargeList.isEmpty()) {
            for (ChargesInfoBean charge : chargeList) {
                if (currentChargeMap.containsKey(charge.getGlMapping().getChargeCode())
                        && CommonUtils.notIn(charge.getRatePerUnitUom(), "FL", "V", "M", "W")) {
                    charge.setRate(charge.getRate().add(currentChargeMap.get(charge.getGlMapping().getChargeCode()).getRate()));
                    currentChargeMap.put(charge.getGlMapping().getChargeCode(), charge);
                } else {
                    currentChargeMap.put(charge.getGlMapping().getChargeCode(), charge);
                }
            }
        }

        LCLCorrectionChargeDAO correctionChargeDAO = new LCLCorrectionChargeDAO();
        for (Map.Entry key : existChargeMap.entrySet()) {
            LCLCorrectionChargeBean existChargeValue = (LCLCorrectionChargeBean) key.getValue();
            GlMapping glMapping = new GlMappingDAO().findById(existChargeValue.getChargeId());
            if (currentChargeMap.containsKey(glMapping.getChargeCode())) {
                ChargesInfoBean charge = currentChargeMap.get(glMapping.getChargeCode());
                correctionChargeDAO.saveCorrectionCharge(correction, charge.getGlMapping(),
                        existChargeValue.getOldAmount(), charge.getRate(), bl.getBillToParty(), bl.getBillToParty(), loginUser,
                        charge.getRateUom(), charge.getRatePerUnitUom(), charge.getRatePerVolumeUnit(),
                        charge.getRatePerWeightUnit(), charge.getMinCharge());
                currentChargeMap.remove(charge.getGlMapping().getChargeCode());
            } else {
                correctionChargeDAO.saveCorrectionCharge(correction, glMapping,
                        existChargeValue.getOldAmount(), BigDecimal.ZERO, bl.getBillToParty(), bl.getBillToParty(), loginUser,
                        engMet, "FL", BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
            }
        }

        for (Map.Entry key : currentChargeMap.entrySet()) {
            ChargesInfoBean charge = (ChargesInfoBean) key.getValue();
            correctionChargeDAO.saveCorrectionCharge(correction, charge.getGlMapping(),
                    BigDecimal.ZERO, charge.getRate(), bl.getBillToParty(), bl.getBillToParty(), loginUser,
                    engMet, charge.getRatePerUnitUom(), charge.getRatePerVolumeUnit(),
                    charge.getRatePerWeightUnit(), charge.getMinCharge());
        }
        for (Map.Entry key : manualChargeMap.entrySet()) {
            LCLCorrectionChargeBean charge = (LCLCorrectionChargeBean) key.getValue();
            correctionChargeDAO.insertExportCorrectionCharge(correction, null, charge.getChargeId(),
                    charge.getOldAmount(), charge.getOldAmount(), bl.getBillToParty(), bl.getBillToParty(), loginUser, true,
                    engMet, "FL", BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        }
    }
}
