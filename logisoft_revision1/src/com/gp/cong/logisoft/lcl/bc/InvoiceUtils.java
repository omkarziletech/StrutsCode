package com.gp.cong.logisoft.lcl.bc;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import static com.gp.cong.lcl.common.constant.LclCommonConstant.REMARKS_TYPE_LCL_CORRECTIONS;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclBookingAc;
import com.gp.cong.logisoft.domain.lcl.LclBookingAcTa;
import com.gp.cong.logisoft.domain.lcl.LclBookingAcTrans;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.bl.LclCorrection;
import com.gp.cong.logisoft.domain.lcl.bl.LclCorrectionCharge;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingAcTransDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingAcTaDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLCorrectionChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLCorrectionDAO;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import com.gp.cvst.logisoft.struts.form.lcl.EculineInvoiceForm;
import com.logiware.accounting.dao.EdiInvoiceDAO;
import com.logiware.accounting.dao.LclManifestDAO;
import com.logiware.accounting.model.EdiInvoiceCharges;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Rajesh
 */
public class InvoiceUtils implements LclCommonConstant, ConstantsInterface {

    private static final String DEFAULT_BILL_TO_PARTY = "C";
    private static final String DEFAULT_RATE_PER_UNIT = "FL";

    // set booking accruals (aka costs) and each accruals transaction_ledger entry
    public void setBookingAc(User user, List<EdiInvoiceCharges> charges) throws Exception {
        StringBuilder chargeIds = new StringBuilder();
        LclBookingAc bookingAc = new LclBookingAc();
        GlMappingDAO glDao = new GlMappingDAO();
        LclFileNumberDAO fileNoDao = new LclFileNumberDAO();
        EdiInvoiceDAO invoiceDao = new EdiInvoiceDAO();
        LclManifestDAO lclManifestDAO = new LclManifestDAO();
        TradingPartnerDAO tpDao = new TradingPartnerDAO();
        LclUtils lclUtils = new LclUtils();
        Date now = new Date();
        LclCostChargeDAO bookingAcDao = new LclCostChargeDAO();
        for (EdiInvoiceCharges charge : charges) {
            LclBooking lclBooking = new LCLBookingDAO().getByProperty("lclFileNumber.id", charge.getFileId());
            String status = (String) new LclFileNumberDAO().getStatusByField("id", charge.getFileId().toString());
//            if (status.equalsIgnoreCase("B")) {
                List<LclBookingAc> lbaTempList = (List<LclBookingAc>) new LclCostChargeDAO().getByCostAndChargeCode(charge.getFileId(), charge.getChargeId(), charge.getCostId());
                if (!lbaTempList.isEmpty()) {
                    LclBookingAc lba = lbaTempList.get(0);
                    bookingAc = lba;
                    lba.setRateFlatMinimum(new BigDecimal(NumberUtils.convertToDouble(lba.getArAmount()) + NumberUtils.convertToDouble(charge.getArAmount())));
                    lba.setArAmount(new BigDecimal(NumberUtils.convertToDouble(lba.getArAmount()) + NumberUtils.convertToDouble(charge.getArAmount())));
                    BigDecimal apAmount = new BigDecimal(NumberUtils.convertToDouble(lba.getApAmount()) + NumberUtils.convertToDouble(charge.getApAmount()));
                    lba.setApAmount(apAmount);
                    if (apAmount.doubleValue() == 0.00) {
                        lba.setInvoiceNumber(null);
                        lba.setSupAcct(null);
                        lba.setApglMapping(null);
                    } else {
                        LclBookingAcTa lbat = new LclBookingAcTaDAO().getByProperty("lclBookingAc", lba);
                        if (lbat != null) {
                            LclBookingAcTrans lbatr = lbat.getLclBookingAcTrans();
                            lbatr.setAmount(apAmount);
                            new LCLBookingAcTransDAO().update(lbatr);
                        }
                    }
                    lba.setArBillToParty(charge.getArBillToParty());
                    new LclCostChargeDAO().update(lba);
                } else {
                    bookingAc.setLclFileNumber(fileNoDao.findById(charge.getFileId()));
                    bookingAc.setManualEntry(true);
                    bookingAc.setConverted(true);
                    bookingAc.setTransDatetime(now);
                    bookingAc.setArglMapping(glDao.findById(charge.getChargeId()));
                    bookingAc.setApBillToParty(DEFAULT_BILL_TO_PARTY);
                    bookingAc.setArBillToParty(charge.getArBillToParty());
                    bookingAc.setRatePerUnitUom(DEFAULT_RATE_PER_UNIT);
                    bookingAc.setRateFlatMinimum(charge.getArAmount());
                    bookingAc.setAdjustmentAmount(BigDecimal.ZERO);
                    bookingAc.setApAmount(charge.getApAmount());
                    bookingAc.setArAmount(charge.getArAmount());
                    bookingAc.setPostedDateTime(now);
                    if (charge.getApAmount().doubleValue() != 0.00) {
                        bookingAc.setApglMapping(glDao.findById(charge.getCostId()));
                        bookingAc.setInvoiceNumber(charge.getInvoiceNo());
                        if (CommonUtils.isNotEmpty(charge.getVendorNo())) {
                            bookingAc.setSupAcct(tpDao.findById(charge.getVendorNo()));
                        }
                    } else {
                        bookingAc.setSupAcct(null);
                        bookingAc.setInvoiceNumber(null);
                        bookingAc.setApglMapping(null);
                    }
                    bookingAc.setPrintOnBl(true);
                    bookingAc.setBundleIntoOf(false);
                    bookingAc.setRelsToInv(false);
                    bookingAc.setEnteredDatetime(now);
                    bookingAc.setModifiedDatetime(now);
                    bookingAc.setEnteredBy(user);
                    bookingAc.setModifiedBy(user);
                    bookingAcDao.save(bookingAc);
                    bookingAcDao.getCurrentSession().flush();
                    bookingAcDao.getCurrentSession().clear();
//                if (charge.getApAmount().doubleValue() != 0.00) {
//                    lclManifestDAO.createLclAccruals(bookingAc, LCL_SHIPMENT_TYPE_IMPORT, STATUS_PAY.equalsIgnoreCase(charge.getChargeStatus()) ? STATUS_OPEN : STATUS_DISPUTE);
//                    lclUtils.insertLCLBookingAcTrans(bookingAc, TRANSACTION_TYPE_ACCRUALS, "A", "", bookingAc.getApAmount(), user);
//                }
                }
//            }
            //chargeIds.append(charge.getChargeId()).append(",");
            invoiceDao.updateInvoiceStatus(charge.getInvoiceId().toString(), user.getUserId());
            // issuing Quick CN
            if (status.equalsIgnoreCase("M")) {
                String strCorrectionTypeIdA = new GenericCodeDAO().getFieldByCodeAndCodetypeId(CODE_TYPE_DESCRIPTION_BL_CORRECTION, GENERIC_CODE_A_BL_CORRECTION, "id");
                GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
                TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
                LCLCorrectionDAO lclCorrectionDAO = new LCLCorrectionDAO();
//                    BigInteger correctionId = lclCorrectionDAO.getCorrectionIdByFileId(charge.getFileId());
//                    if (correctionId == null) {
                BigInteger lastCorrectionNo = lclCorrectionDAO.getIntegerDescByFileIdWithoutVoid(charge.getFileId(), "correction_no");
                if (lastCorrectionNo == null) {
                    lastCorrectionNo = new BigInteger("0");
                }
//                    String concatenatedBlNo = "(" + lclCorrectionForm.getConcatenatedBlNo() + "-C-" + String.valueOf(lastCorrectionNo.intValue() + 1) + ")";
                LclCorrection lclCorrection = new LclCorrection();
                lclCorrection.setEnteredBy(user);
                lclCorrection.setEnteredDatetime(now);
                lclCorrection.setCorrectionNo(lastCorrectionNo.intValue() + 1);
                lclUtils.insertLCLCorrectionRemarks(REMARKS_TYPE_LCL_CORRECTIONS, charge.getFileId(), charge.getFileNo(), "saved", user);
                lclCorrection.setModifiedBy(user);
                lclCorrection.setModifiedDatetime(now);
                lclCorrection.setComments("QUICK CN");
                lclCorrection.setType(genericCodeDAO.findById(Integer.valueOf(strCorrectionTypeIdA)));
                String id = genericCodeDAO.getFieldByCodeAndCodetypeId(CODE_TYPE_DESCRIPTION_BL_CORRECTION_CODE, GENERIC_CODE_A_CORRECTION_IMPORTS, "id");
                lclCorrection.setCode(genericCodeDAO.findById(Integer.parseInt(id)));
                if (charge.getArBillToParty().equals("C")) {
                    lclCorrection.setConsAcct(tradingPartnerDAO.findById(lclBooking.getConsAcct().getAccountno()));
                } else if (charge.getArBillToParty().equals("N")) {
                    lclCorrection.setNotyAcct(tradingPartnerDAO.findById(lclBooking.getNotyAcct().getAccountno()));
                } else if (charge.getArBillToParty().equals("T")) {
                    lclCorrection.setThirdPartyAcct(tradingPartnerDAO.findById(lclBooking.getThirdPartyAcct().getAccountno()));
                }
                lclCorrection.setLclFileNumber(new LclFileNumber(charge.getFileId()));
                lclCorrection.setStatus("O");
                lclCorrection.setVoid1(Boolean.FALSE);
                // inserting correction charge
                LclCorrectionCharge lclCorrectionCharge = new LclCorrectionCharge();
                lclCorrectionCharge.setLclCorrection(lclCorrection);
                lclCorrectionCharge.setOldAmount(BigDecimal.ZERO);
                lclCorrectionCharge.setNewAmount(charge.getArAmount());
                lclCorrectionCharge.setGlMapping(new GlMappingDAO().findById(charge.getChargeId()));
                lclCorrectionCharge.setBillToParty(charge.getArBillToParty());
                lclCorrectionCharge.setEnteredBy(user);
                lclCorrectionCharge.setEnteredDatetime(now);
                lclCorrectionCharge.setModifiedDatetime(now);
                lclCorrectionCharge.setModifiedBy(user);
                List<LclCorrectionCharge> lclCorrectionCharges = new ArrayList<LclCorrectionCharge>();
                lclCorrectionCharges.add(lclCorrectionCharge);
                lclCorrection.setLclCorrectionChargeCollection(lclCorrectionCharges);
                lclCorrectionDAO.saveOrUpdate(lclCorrection);
                // posting the correction
                lclManifestDAO.postCorrection(lclBooking, lclCorrection, user);
                lclCorrectionDAO.approveCorrections(lclCorrection.getId(), user.getUserId(), "Q");
            }
        }
    }

    public void invoiceDetails(String bol, HttpServletRequest request, String cntrId, String invoiceNo, String fileNumberId) throws Exception {
        EdiInvoiceDAO invoiceDao = new EdiInvoiceDAO();
        List<EdiInvoiceCharges> allCharges = invoiceDao.getCharges(bol, invoiceNo, null, cntrId, fileNumberId);
        Map<String, List<EdiInvoiceCharges>> invoices = new HashMap<String, List<EdiInvoiceCharges>>();
        for (EdiInvoiceCharges charge : allCharges) {
            if (invoices.containsKey(charge.getInvoiceNo())) {
                invoices.get(charge.getInvoiceNo()).add(charge);
            } else {
                List<EdiInvoiceCharges> charges = new ArrayList<EdiInvoiceCharges>();
                charges.add(charge);
                invoices.put(charge.getInvoiceNo(), charges);
            }
        }
        if (allCharges != null && !allCharges.isEmpty() && CommonUtils.isNotEmpty(allCharges.get(0).getFileNo())) {
            request.setAttribute("fileNo", allCharges.get(0).getFileNo());
        } else {
            request.setAttribute("fileNo", "");
        }
        request.setAttribute("invoices", invoices);
        request.setAttribute("bol", bol);
    }

    public void setValues(EculineInvoiceForm invoiceForm, HttpServletRequest request) throws Exception {
        request.setAttribute("id", invoiceForm.getContainerId());
        request.setAttribute("fileNumberId", invoiceForm.getFileNumberId());
        request.setAttribute("invoiceForm", invoiceForm);
    }

    public void mappingChargeDesc(EculineInvoiceForm invoiceForm) throws Exception {
        EdiInvoiceDAO ediInvoiceDAO = new EdiInvoiceDAO();
        if (CommonUtils.isNotEmpty(invoiceForm.getEculineChargedesc()) && CommonUtils.isNotEmpty(invoiceForm.getChargeCode())
                && CommonUtils.isNotEmpty(invoiceForm.getCostCode()) && CommonUtils.isNotEmpty(invoiceForm.getBlueChargeCode())
                && CommonUtils.isNotEmpty(invoiceForm.getBlueCostCode())) {
            //String ecuMappId = ediInvoiceDAO.getEcuMappingId(invoiceForm.getEculineChargedesc(), invoiceForm.getBlueChargeCode(), invoiceForm.getBlueCostCode());
            //  if (CommonUtils.isEmpty(ecuMappId)) {
            ediInvoiceDAO.insertEcuicdMapping(invoiceForm.getBlueChargeCode(), invoiceForm.getBlueCostCode(), invoiceForm.getEculineChargedesc());
            // }
        }
    }

    public void mappingAllChargesDescForEculine(EculineInvoiceForm invoiceForm) throws Exception {
        EdiInvoiceDAO ediInvoiceDAO = new EdiInvoiceDAO();
        if (invoiceForm.getEcuChargedesc() != null) {
            for (int i = 0; i <= (invoiceForm.getEcuChargedesc().length - 1); i++) {
                if (CommonUtils.isNotEmpty(invoiceForm.getEcuChargedesc()[i]) && CommonUtils.isNotEmpty(invoiceForm.getEcuChargeCode()[i])
                        && CommonUtils.isNotEmpty(invoiceForm.getEcuCostCode()[i]) && CommonUtils.isNotEmpty(invoiceForm.getEcuBlueChargeCode()[i])
                        && CommonUtils.isNotEmpty(invoiceForm.getEcuBlueCostCode()[i])) {
                    ediInvoiceDAO.insertEcuicdMapping(invoiceForm.getEcuBlueChargeCode()[i], invoiceForm.getEcuBlueCostCode()[i], invoiceForm.getEcuChargedesc()[i]);
                }
            }
        }
    }
}
