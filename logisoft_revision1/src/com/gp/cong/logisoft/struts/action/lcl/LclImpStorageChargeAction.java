/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.lcl.dwr.LCLImportChargeCalc;
import com.gp.cong.logisoft.beans.LclImportsRatesBean;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclBookingAc;
import com.gp.cong.logisoft.domain.lcl.LclBookingPiece;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.bl.LclCorrection;
import com.gp.cong.logisoft.hibernate.dao.CodetypeDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLImportRatesDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLCorrectionChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLCorrectionDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cvst.logisoft.struts.form.lcl.LclImportPaymentForm;
import com.logiware.accounting.dao.LclManifestDAO;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Logiware
 */
public class LclImpStorageChargeAction extends LogiwareDispatchAction implements LclCommonConstant, ConstantsInterface {

    private LCLBookingDAO lclBookingDAO = new LCLBookingDAO();
    private LCLImportChargeCalc lclImportChargeCalc = new LCLImportChargeCalc();
    private PortsDAO portDAO = new PortsDAO();

    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclImportPaymentForm lclImportPaymentForm = (LclImportPaymentForm) form;
        LCLImportRatesDAO lclImportRatesDAO = new LCLImportRatesDAO();
        LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
        LclBooking lclBooking = lclBookingDAO.getByProperty("lclFileNumber.id", Long.parseLong(lclImportPaymentForm.getFileNumberId()));
        List<LclBookingPiece> lclCommodityList = lclBooking.getLclFileNumber().getLclBookingPieceList();
        List<String> commodityList = new ArrayList<String>();
        for (LclBookingPiece lbp : lclCommodityList) {
            if (lbp.getCommodityType() != null && lbp.getCommodityType().getCode() != null) {
                commodityList.add(lbp.getCommodityType().getCode());
            } else {
                commodityList.add(lbp.getCommNo());
            }
        }
        String polSchnum = portDAO.getShedulenumber(lclBooking.getPortOfLoading().getUnLocationCode());
        String podSchnum = portDAO.getShedulenumber(lclBooking.getPortOfDestination().getUnLocationCode());
        String fdSchnum = portDAO.getShedulenumber(lclBooking.getFinalDestination().getUnLocationCode());
        String pooSchnum = portDAO.getShedulenumber(null != lclBooking.getPortOfOrigin() ? lclBooking.getPortOfOrigin().getUnLocationCode() : "");
        List<LclImportsRatesBean> exceptionRatesList = lclImportRatesDAO.getExceptionList(!pooSchnum.equalsIgnoreCase("") ? pooSchnum : "00000", polSchnum, podSchnum, fdSchnum, commodityList);
        int daysCount = 0;
        if (lclImportPaymentForm.getLastFdDate() != null && !lclImportPaymentForm.getLastFdDate().equalsIgnoreCase("")) {
            Long totalDays = DateUtils.getDateDiffByTotalDays(DateUtils.parseDate(lclImportPaymentForm.getLastFdDate(), "dd-MMM-yyyy"), DateUtils.formatDateAndParseTo(new Date(), "dd-MMM-yyyy"));
            Long d = (totalDays / 7);
            daysCount = d.intValue() + 1;
        }
        request.setAttribute("noOfWeeks", daysCount);
        LclBookingAc oldlclBookingAc = lclCostChargeDAO.findByBlueScreenChargeCode(Long.parseLong(lclImportPaymentForm.getFileNumberId()), "1603", false);
        if (oldlclBookingAc != null && oldlclBookingAc.getArAmount() != null) {
            request.setAttribute("oldAmt", oldlclBookingAc.getArAmount());
        }
        User user = getCurrentUser(request);
        List<LclImportsRatesBean> storageChargesList = lclImportRatesDAO.getLclImpStorageCharges(polSchnum, podSchnum, "", commodityList);
        List<LclBookingAc> calculatedStorageChargeList = lclImportChargeCalc.calculateImportRate(storageChargesList, lclCommodityList, "C", null, user, null, "", exceptionRatesList, request);
        if (calculatedStorageChargeList != null && !calculatedStorageChargeList.isEmpty() && daysCount > 0) {
            for (LclBookingAc lclBookingAc : calculatedStorageChargeList) {
                if (lclBookingAc.getArAmount() != null) {
                    lclBookingAc.setArAmount(new BigDecimal(lclBookingAc.getArAmount().doubleValue() * daysCount).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                }
            }
        }
        lclImportPaymentForm.setModuleName("Imports");
        request.setAttribute("fileStatus", lclBooking.getLclFileNumber().getStatus());
        request.setAttribute("storageChargelist", calculatedStorageChargeList);
        request.setAttribute("lclImportPaymentForm", lclImportPaymentForm);
        return mapping.findForward("displayStorageCharge");
    }

    public ActionForward saveCharges(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclImportPaymentForm lclImportPaymentForm = (LclImportPaymentForm) form;
        LCLImportRatesDAO lclImportRatesDAO = new LCLImportRatesDAO();
        List<LclBookingAc> calculatedStorageChargeList = null;
        LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
        String agentNo = "";
        LclBooking lclBooking = lclBookingDAO.getByProperty("lclFileNumber.id", Long.parseLong(lclImportPaymentForm.getFileNumberId()));
        String pooUnCode = null != lclBooking.getPortOfOrigin() ? lclBooking.getPortOfOrigin().getUnLocationCode() : "";
        if (CommonFunctions.isNotNull(lclBooking.getAgentAcct()) && CommonFunctions.isNotNull(lclBooking.getAgentAcct().getAccountno())) {
            agentNo = lclBooking.getAgentAcct().getAccountno();
        }
        List<LclBookingPiece> lclCommodityList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", Long.parseLong(lclImportPaymentForm.getFileNumberId()));
        LclBookingAc lclBookingAc = lclCostChargeDAO.findByBlueScreenChargeCode(Long.parseLong(lclImportPaymentForm.getFileNumberId()), "1603", false);
        User user = getCurrentUser(request);
        //boolean manifestFlag = false;
        String oldAmt = "0.00";
        if (lclBookingAc == null) {
            //  manifestFlag = true;
            List<String> commodityList = new ArrayList<String>();
            for (LclBookingPiece lbp : lclCommodityList) {
                if (lbp.getCommodityType() != null && lbp.getCommodityType().getCode() != null) {
                    commodityList.add(lbp.getCommodityType().getCode());
                } else {
                    commodityList.add(lbp.getCommNo());
                }
            }
            String polSchnum = portDAO.getShedulenumber(lclBooking.getPortOfLoading().getUnLocationCode());
            String podSchnum = portDAO.getShedulenumber(lclBooking.getPortOfDestination().getUnLocationCode());
            String fdSchnum = portDAO.getShedulenumber(lclBooking.getFinalDestination().getUnLocationCode());
            String pooSchnum = portDAO.getShedulenumber(pooUnCode);
            List<LclImportsRatesBean> exceptionRatesList = lclImportRatesDAO.getExceptionList(!pooSchnum.equalsIgnoreCase("") ? pooSchnum : "00000", polSchnum, podSchnum, fdSchnum, commodityList);
            List<LclImportsRatesBean> storageChargesList = lclImportRatesDAO.getLclImpStorageCharges(polSchnum, podSchnum, "", commodityList);
            calculatedStorageChargeList = lclImportChargeCalc.calculateImportRate(storageChargesList, lclCommodityList, "C", null, user, null, "N", exceptionRatesList, request);
            lclBookingAc = calculatedStorageChargeList.get(0);
        } else {
            oldAmt = String.valueOf(lclBookingAc.getArAmount());
        }
        if (lclImportPaymentForm.getAmount().trim() != null) {
            lclBookingAc.setArAmount(new BigDecimal(lclImportPaymentForm.getAmount().trim()));
            lclBookingAc.setLclFileNumber(new LclFileNumber(Long.parseLong(lclImportPaymentForm.getFileNumberId())));
        }
        lclCostChargeDAO.saveOrUpdate(lclBookingAc);
        if ("M".equalsIgnoreCase(lclBooking.getLclFileNumber().getStatus())) {
            String vendorNo = "";
            if ("C".equalsIgnoreCase(lclBooking.getBillToParty())) {
                vendorNo = lclBooking.getConsAcct().getAccountno();
            } else if ("N".equalsIgnoreCase(lclBooking.getBillToParty())) {
                vendorNo = lclBooking.getNotyAcct().getAccountno();
            } else {
                vendorNo = lclBooking.getThirdPartyAcct().getAccountno();
            }
            LCLCorrectionDAO lclCorrectionDAO = new LCLCorrectionDAO();
            LCLCorrectionChargeDAO lclCorrectionChargeDAO = new LCLCorrectionChargeDAO();
            LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
            GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
            LclManifestDAO lclManifestDAO = new LclManifestDAO();
            CodetypeDAO codeTypeDAO = new CodetypeDAO();
            Integer codeTypeId = codeTypeDAO.getCodeTypeId(CODE_TYPE_DESCRIPTION_BL_CORRECTION);
            Integer codeId = codeTypeDAO.getCodeTypeId(CODE_TYPE_DESCRIPTION_BL_CORRECTION_CODE);
            String cnType = genericCodeDAO.getByCodeAndCodetypeId(String.valueOf(codeTypeId), GENERIC_CODE_A_BL_CORRECTION, "id");
            String cnCode = genericCodeDAO.getByCodeAndCodetypeId(String.valueOf(codeId), GENERIC_CODE_A_CORRECTION_IMPORTS, "id");
            BigInteger lastCorrectionNo = lclCorrectionDAO.getIntegerDescByFileIdWithoutVoid(lclBooking.getLclFileNumber().getId(), "correction_no");
            if (lastCorrectionNo == null) {
                lastCorrectionNo = new BigInteger("0");
            }
            String cnFileNo = "(" + lclBooking.getLclFileNumber().getFileNumber() + "-C-" + String.valueOf(lastCorrectionNo.intValue() + 1) + ")";
            lclCorrectionDAO.insertCorrections(lclBooking.getLclFileNumber().getId(), Integer.parseInt(cnType), Integer.parseInt(cnCode),
                    vendorNo, lastCorrectionNo.intValue() + 1, "QUICK CN", "A", null, null, user.getUserId(), 0, lclBooking.getBillToParty());
            String correction = CommonConstants.getEventMap().get(GENERIC_EVENT_CODE_CORRECTION_NOTES);
            StringBuilder savedCnNotes = new StringBuilder();
            savedCnNotes.append(correction).append(" ").append(cnFileNo).append(" ").append("Saved");
            lclRemarksDAO.insertLclRemarks(lclBooking.getLclFileNumber().getId(), REMARKS_TYPE_LCL_CORRECTIONS, savedCnNotes.toString(), user.getUserId());
            BigInteger cnId = lclCorrectionDAO.getIntegerDescByFileIdWithoutVoid(lclBooking.getLclFileNumber().getId(), "id");
            lclCorrectionChargeDAO.insertCorrectionCharge(cnId.longValue(), oldAmt,
                    lclImportPaymentForm.getAmount().trim(), lclBookingAc.getArglMapping().getId(), lclBooking.getBillToParty(), user.getUserId());
            lclCorrectionDAO.approveCorrections(cnId.longValue(), user.getUserId(), "A");
            LclCorrection lclCorrection = lclCorrectionDAO.findById(cnId.longValue());
            lclManifestDAO.createLclCorrections(LCL_IMPORT, user, true, lclCorrection, true, lclBooking);
            StringBuilder postCnNotes = new StringBuilder();
            postCnNotes.append(correction).append(" ").append(cnFileNo).append(" ").append("is got Approved and Post");
            lclRemarksDAO.insertLclRemarks(lclBooking.getLclFileNumber().getId(), REMARKS_TYPE_LCL_CORRECTIONS, postCnNotes.toString(), user.getUserId());
//            if (manifestFlag) {
//                ManifestDAO manifestDAO = new ManifestDAO();
//                manifestDAO.getAllManifestImportsBookingsByUnitSS(null, lclBookingAc.getId(), null, user, true, null, true, String.valueOf(lclBooking.getLclFileNumber().getId()));
//            } else {
//                TransactionLedgerDAO transactionLedgerDAO = new TransactionLedgerDAO();
//                transactionLedgerDAO.updateTransactionAmt(lclBooking.getLclFileNumber().getFileNumber(), lclBookingAc.getArAmount(), lclBookingAc.getId());
//            }
        }
        LclUtils lclUtils = new LclUtils();
        List<LclBookingAc> chargeList = lclCostChargeDAO.getLclCostByFileNumberAsc(Long.parseLong(lclImportPaymentForm.getFileNumberId()), LclCommonConstant.LCL_IMPORT);
        request.setAttribute("chargeList", chargeList);
        request.setAttribute("lclImportPaymentForm", lclImportPaymentForm);
        request.setAttribute("lclBooking", lclBooking);
        request.setAttribute("totalStorageAmt", lclImportChargeCalc.calculateImpAutoRates(lclBooking.getPortOfLoading().getUnLocationCode(), lclBooking.getPortOfDestination().getUnLocationCode(), "", pooUnCode, lclCommodityList, "N"));
        lclUtils.setWeighMeasureForImportBooking(request, lclCommodityList, null);
        lclUtils.setImportRolledUpChargesForBooking(chargeList, request, Long.parseLong(lclImportPaymentForm.getFileNumberId()), lclCostChargeDAO, lclCommodityList, lclBooking.getBillingType(), "", agentNo);
        return mapping.findForward("chargeDesc");
    }
}
