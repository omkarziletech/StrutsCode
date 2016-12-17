/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.lcl.common.constant.BlCorrectionUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.logisoft.beans.ChargesInfoBean;
import com.gp.cong.logisoft.beans.ImportsManifestBean;
import com.gp.cong.logisoft.beans.LCLCorrectionChargeBean;
import com.gp.cong.logisoft.beans.LCLCorrectionNoticeBean;
import com.gp.cong.logisoft.domain.CreditDebitNote;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.Ports;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.CorrectionCommodity;
import com.gp.cong.logisoft.domain.lcl.bl.LclBl;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlAc;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlPiece;
import com.gp.cong.logisoft.domain.lcl.bl.LclCorrection;
import com.gp.cong.logisoft.domain.lcl.bl.LclCorrectionCharge;
import com.gp.cong.logisoft.hibernate.dao.CustomerAccountingDAO;
import com.gp.cong.logisoft.hibernate.dao.CustomerContactDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.CorrectionCommodityDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLRatesDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBlAcDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsHeaderDAO;
import com.gp.cong.logisoft.util.DBUtil;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLBlDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLCorrectionChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLCorrectionDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LclBLPieceDAO;
import com.gp.cong.logisoft.lcl.model.RateModel;
import com.gp.cong.logisoft.lcl.model.RatesCalculationModel;
import com.gp.cong.logisoft.lcl.report.LclBLPdfCreator;
import com.gp.cong.logisoft.lcl.report.LclCorrectionDebitCreditPdfCreator;
import com.gp.cong.logisoft.lcl.report.LclReportConstants;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.CustAddress;
import com.gp.cvst.logisoft.domain.GlMapping;
import com.gp.cvst.logisoft.hibernate.dao.CustAddressDAO;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LCLCorrectionForm;
import com.logiware.accounting.dao.LclExportManifestDAO;
import com.logiware.accounting.model.ManifestModel;
import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

/**
 *
 * @author Mei
 */
public class BlCorrectionAction extends LogiwareDispatchAction implements LclCommonConstant, LclReportConstants {

    private static final Logger log = Logger.getLogger(BlCorrectionAction.class);

    public ActionForward searchResult(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLCorrectionForm correctionForm = (LCLCorrectionForm) form;
        LCLCorrectionDAO correctionDAO = new LCLCorrectionDAO();

        request.setAttribute("correctionList", new BlCorrectionUtils().getCorrectionList(correctionForm.getFileId(), false));
        //request.setAttribute("correctionList", correctionDAO.findByFileId(correctionForm.getFileId(), false));
        request.setAttribute("isVoidCorrection", correctionDAO.isVoidCorrection(correctionForm.getFileId()));
        request.setAttribute("pickedVoyage", new LclUnitSsDAO().getPickedVoyageByVessel(correctionForm.getFileId(), "E"));

        request.setAttribute("issuingTerminal", correctionForm.getIssuingTerminal());
        return mapping.findForward("searchResult");
    }

    public ActionForward saveCorrection(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLCorrectionForm correctionForm = (LCLCorrectionForm) form;
        saveCorrection(correctionForm, request);
        if ("searchPool".equalsIgnoreCase(correctionForm.getScreenName())) {
            searchCorrectionByPool(mapping, form, request, response);
            return mapping.findForward("blCorrectionResultPool");
        } else {
            LclUtils utils = new LclUtils();
            LclCorrection lclCorrection = new LCLCorrectionDAO().findById(correctionForm.getCorrectionId());
            request.setAttribute("lclCorrection", lclCorrection);
            setCorrectionReq(correctionForm, request);
            LclBl bl = new LCLBlDAO().getByProperty("lclFileNumber.id", correctionForm.getFileId());
            correctionForm.setOrigin(null != bl.getPortOfOrigin()
                    ? utils.getConcatenatedOriginByUnlocation(bl.getPortOfOrigin()) : "");
            correctionForm.setDestination(null != bl.getFinalDestination()
                    ? utils.getConcatenatedOriginByUnlocation(bl.getFinalDestination())
                    : utils.getConcatenatedOriginByUnlocation(bl.getPortOfDestination()));
            correctionForm.setBillingType(new BlCorrectionUtils().setBillToPartyForCorrectionCharges(bl.getBillToParty()));
            correctionForm.setFileNo(bl.getLclFileNumber().getFileNumber());
            request.setAttribute("bl", bl);
            new BlCorrectionUtils().setCorrection(correctionForm, bl, request);
            setCommodityList(correctionForm, null);
            return mapping.findForward("addOrEdit");
        }
    }

    public ActionForward saveCorrectionCharges(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLCorrectionForm correctionForm = (LCLCorrectionForm) form;
        saveCorrection(correctionForm, request);
        LclUtils utils = new LclUtils();
        LclCorrection lclCorrection = new LCLCorrectionDAO().findById(correctionForm.getCorrectionId());
        request.setAttribute("lclCorrection", lclCorrection);
        setCorrectionReq(correctionForm, request);
        LclBl bl = new LCLBlDAO().getByProperty("lclFileNumber.id", correctionForm.getFileId());
        correctionForm.setOrigin(null != bl.getPortOfOrigin()
                ? utils.getConcatenatedOriginByUnlocation(bl.getPortOfOrigin()) : "");
        correctionForm.setDestination(null != bl.getFinalDestination()
                ? utils.getConcatenatedOriginByUnlocation(bl.getFinalDestination())
                : utils.getConcatenatedOriginByUnlocation(bl.getPortOfDestination()));
        correctionForm.setBillingType(new BlCorrectionUtils().setBillToPartyForCorrectionCharges(bl.getBillToParty()));
        correctionForm.setFileNo(bl.getLclFileNumber().getFileNumber());

        request.setAttribute("bl", bl);
        new BlCorrectionUtils().setChargeList(lclCorrection.getId(), correctionForm, request);
        if (CommonUtils.isNotEmpty(correctionForm.getCurrentProfit())) {
            lclCorrection.setCurrentProfit(new BigDecimal(correctionForm.getCurrentProfit()));
            lclCorrection.setProfitAfterCN(new BigDecimal(correctionForm.getProfitAfterCN()));
        }
        setCommodityList(correctionForm, bl);
        return mapping.findForward("addOrEdit");
    }

    public void saveCorrection(LCLCorrectionForm correctionForm, HttpServletRequest request) throws Exception {
        User loginUser = getCurrentUser(request);
        BlCorrectionUtils correctionUtils = new BlCorrectionUtils();
        LCLCorrectionDAO correctionDAO = new LCLCorrectionDAO();
        LclBl bl = new LCLBlDAO().getByProperty("lclFileNumber.id", correctionForm.getFileId());
        LclCorrection correction = correctionUtils.saveCorrection(correctionForm, bl, loginUser, correctionDAO);
        this.saveCharge(correctionForm, correction, request);
        if (CommonUtils.isNotEmpty(correction.getId())) {
            this.setCreditDebitEmail(correction, correctionForm, request);
        }
        correctionUtils.setChargeList(correction.getId(), correctionForm, request);
        correctionDAO.updateCorrectionByField(correction.getId(), "current_profit", correctionForm.getCurrentProfit());
        correctionDAO.updateCorrectionByField(correction.getId(), "profit_aftercn", correctionForm.getProfitAfterCN());
        correctionDAO.updateCorrectionByField(correction.getId(), "debit_email", correctionForm.getDebitMemoEmail());
        correctionDAO.updateCorrectionByField(correction.getId(), "credit_email", correctionForm.getCreditMemoEmail());

        correctionForm.setCorrectionId(correction.getId());
        correctionForm.setChargeCode("");
        correctionForm.setCorrectionChargeId(0L);
        correctionForm.setChargeId(0);
        correctionForm.setCurrentProfit("");
    }

    public void setCreditDebitEmail(LclCorrection correction, LCLCorrectionForm cForm,
            HttpServletRequest request) throws Exception {
        Set<String> creditEmailList = new HashSet<String>();
        Set<String> debitEmailList = new HashSet<String>();
        Set<String> creditFaxlList = new HashSet<String>();
        Set<String> debitFaxList = new HashSet<String>();
        List<LclCorrectionCharge> correctedList = new LCLCorrectionChargeDAO().findByProperty("lclCorrection.id", correction.getId());
        for (int x = correctedList.size() - 1; x >= 0; x--) {
            LclCorrectionCharge ch = correctedList.get(x);
            String previous_billToParty = ch.getOldBillToParty() != null ? ch.getOldBillToParty() : "";
            LclBl bl = ch.getLclCorrection().getLclFileNumber().getLclBl();
            if (ch.getNewAmount().doubleValue() != 0.00 && (!ch.getNewAmount().equals(ch.getOldAmount())
                    || !previous_billToParty.equalsIgnoreCase(ch.getBillToParty()))) {
                String acctNo = "", previousBillAcctNo = "";
                acctNo = ch.getBillToParty().equalsIgnoreCase("A") ? correction.getNewAgent().getAccountno()
                        : ch.getBillToParty().equalsIgnoreCase("F") ? correction.getNewForwarder().getAccountno()
                                : ch.getBillToParty().equalsIgnoreCase("S") ? correction.getNewShipper().getAccountno()
                                        : ch.getBillToParty().equalsIgnoreCase("T") ? correction.getNewThirdParty().getAccountno() : "";
                boolean isCreditDebit = new CustomerAccountingDAO().isCreditDebitNote(acctNo);
                if (previous_billToParty.equalsIgnoreCase(ch.getBillToParty())) {
                    BigDecimal amount = ch.getNewAmount().subtract(ch.getOldAmount());
                    if (isCreditDebit) {
                        List<ImportsManifestBean> custContact = new CustomerContactDAO().getCorrectionCreditDebitEmailForExport(acctNo);
                        if (amount.doubleValue() >= 0) {
                            for (ImportsManifestBean bean : custContact) {
                                debitEmailList.add(bean.getEmail());
                                debitFaxList.add(bean.getFax());
                            }
                        } else if (amount.doubleValue() <= 0) {
                            for (ImportsManifestBean bean : custContact) {
                                creditEmailList.add(bean.getEmail());
                                creditFaxlList.add(bean.getFax());
                            }
                        }
                    }
                }
                if (!previous_billToParty.equalsIgnoreCase(ch.getBillToParty())) {
                    previousBillAcctNo = previous_billToParty.equalsIgnoreCase("A") ? correction.getOldAgent().getAccountno()
                            : previous_billToParty.equalsIgnoreCase("F") ? correction.getOldForwarder().getAccountno()
                                    : previous_billToParty.equalsIgnoreCase("S") ? correction.getOldShipper().getAccountno()
                                            : previous_billToParty.equalsIgnoreCase("T") ? correction.getThirdPartyAcct().getAccountno() : "";
                    boolean previousBillCreditDebit = new CustomerAccountingDAO().isCreditDebitNote(previousBillAcctNo);
                    if (previousBillCreditDebit) {
                        List<ImportsManifestBean> custContact = new CustomerContactDAO().getCorrectionCreditDebitEmailForExport(previousBillAcctNo);
                        for (ImportsManifestBean bean : custContact) {
                            creditEmailList.add(bean.getEmail());
                            creditFaxlList.add(bean.getFax());
                        }
                    }
                    if (isCreditDebit) {
                        List<ImportsManifestBean> custContact = new CustomerContactDAO().getCorrectionCreditDebitEmailForExport(acctNo);
                        for (ImportsManifestBean bean : custContact) {
                            debitEmailList.add(bean.getEmail());
                            debitFaxList.add(bean.getFax());
                        }
                    }
                }
            }
        }
        String debitEmail = convertSetToString(debitEmailList);
        String creditEmail = convertSetToString(creditEmailList);
        cForm.setDebitMemoEmail(debitEmail);
        cForm.setCreditMemoEmail(creditEmail);
        request.setAttribute("debitEmailList", debitEmailList);
        request.setAttribute("creditEmailList", creditEmailList);
        request.setAttribute("debitFaxList", debitFaxList);
        request.setAttribute("creditFaxlList", creditFaxlList);
    }

    public String convertSetToString(Set<String> emailFaxSet) {
        StringBuilder sb = new StringBuilder();
        if (CommonUtils.isNotEmpty(emailFaxSet)) {
            sb.append(emailFaxSet.toString().replace("[", "").replace("]", ""));
        }
        return sb.toString();
    }

    public void saveCharge(LCLCorrectionForm correctionForm, LclCorrection correction,
            HttpServletRequest request) throws Exception {
        User loginUser = getCurrentUser(request);
        LCLCorrectionChargeDAO correctionChargeDAO = new LCLCorrectionChargeDAO();
        LclBlAcDAO lclBlAcDAO = new LclBlAcDAO();
        LclBLPieceDAO lclBlPieceDAO = new LclBLPieceDAO();
        List<LclBlAc> lclBlAcList = lclBlAcDAO.getLclChargesByFileNo(correction.getLclFileNumber().getId());
        BigInteger commodityCount = lclBlPieceDAO.getPieceCountByFileId(correction.getLclFileNumber().getId());

        Long previous_CID = correctionChargeDAO.getPreviousCorrectionId(correction.getLclFileNumber().getId());
        List<LclCorrectionCharge> previousChargeList = correctionChargeDAO.findByProperty("lclCorrection.id", previous_CID);
        List<LclCorrectionCharge> correctionList = correctionChargeDAO.findByProperty("lclCorrection.id", correction.getId());
        LclBl lclbl = new LCLBlDAO().getByProperty("lclFileNumber.id", correction.getLclFileNumber().getId());
        new LCLBlDAO().getCurrentSession().evict(lclbl);

        String eciPortCode = null != lclbl.getFinalDestination()
                ? lclbl.getFinalDestination().getUnLocationCode() : lclbl.getPortOfDestination().getUnLocationCode();

        Ports port = new PortsDAO().getPorts(eciPortCode);
        String engMet = null != port ? port.getEngmet() : "E";
        if (CommonUtils.isEmpty(correctionList) && CommonUtils.isEmpty(previousChargeList)) {
            LclBlAc charge = new LclBlAcDAO().findById(correctionForm.getCorrectionChargeId());
            if (commodityCount.doubleValue() > 1) {
                lclBlAcList = new BlCorrectionUtils().getRolledUpChargesForBlCorrections(lclBlAcList);
            }
            for (LclBlAc blAc : lclBlAcList) {
                BigDecimal oldAmount = blAc.getRolledupCharges() != null ? blAc.getRolledupCharges().add(blAc.getAdjustmentAmount())
                        : blAc.getArAmount().add(blAc.getAdjustmentAmount());
                BigDecimal newAmount = charge != null && charge.equals(blAc)
                        ? new BigDecimal(correctionForm.getNewAmount()) : oldAmount;
                String billToParty = charge != null && charge.equals(blAc) ? correctionForm.getBillToParty() : blAc.getArBillToParty();
                correctionChargeDAO.insertExportCorrectionCharge(correction, blAc, blAc.getArglMapping().getId(),
                        oldAmount, newAmount, billToParty, blAc.getArBillToParty(), loginUser, blAc.isManualEntry(),
                        engMet, blAc.getRatePerUnitUom(), blAc.getRatePerVolumeUnit(),
                        blAc.getRatePerWeightUnit(), blAc.getRateFlatMinimum());
            }
            if (charge == null && "saveCorrectionCharges".equalsIgnoreCase(correctionForm.getMethodName())) {
                correctionChargeDAO.insertExportCorrectionCharge(correction, null, correctionForm.getChargeId(),
                        new BigDecimal("0.00"), new BigDecimal(correctionForm.getNewAmount()),
                        correctionForm.getBillToParty(), correctionForm.getBillToParty(), loginUser, true,
                        engMet, "FL", BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
            }
        } else if (CommonUtils.isNotEmpty(correctionList) && "saveCorrectionCharges".equalsIgnoreCase(correctionForm.getMethodName())) {
            LclCorrectionCharge charge = correctionChargeDAO.findById(correctionForm.getCorrectionChargeId());
            if (charge == null) {
                correctionChargeDAO.insertExportCorrectionCharge(correction, null, correctionForm.getChargeId(),
                        new BigDecimal("0.00"), new BigDecimal(correctionForm.getNewAmount()),
                        correctionForm.getBillToParty(), correctionForm.getBillToParty(), loginUser, true,
                        engMet, "FL", BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
            } else {
                charge.setNewAmount(new BigDecimal(correctionForm.getNewAmount()));
                charge.setBillToParty(correctionForm.getBillToParty());
                correctionChargeDAO.update(charge);
            }
        } else if (CommonUtils.isEmpty(correctionList) && CommonUtils.isNotEmpty(previousChargeList)) {
            LclCorrectionCharge charge = correctionChargeDAO.findById(correctionForm.getCorrectionChargeId());
            for (int x = previousChargeList.size() - 1; x >= 0; x--) {
                LclCorrectionCharge ch = previousChargeList.get(x);
                BigDecimal newAmount = charge != null && charge.equals(ch) ? new BigDecimal(correctionForm.getNewAmount()) : ch.getNewAmount();
                String billToParty = charge != null && charge.equals(ch) ? correctionForm.getBillToParty() : ch.getBillToParty();
                correctionChargeDAO.insertExportCorrectionCharge(correction, ch.getLclBlAc(), ch.getGlMapping().getId(), ch.getNewAmount(),
                        newAmount, billToParty, ch.getBillToParty(), loginUser, ch.isManualCharge(),
                        engMet, ch.getRatePerUnitUom(), ch.getRatePerVolumeUnit(),
                        ch.getRatePerWeightUnit(), ch.getMinimumRate());
            }
            if (charge == null && "saveCorrectionCharges".equalsIgnoreCase(correctionForm.getMethodName())) {
                correctionChargeDAO.insertExportCorrectionCharge(correction, null, correctionForm.getChargeId(),
                        new BigDecimal("0.00"), new BigDecimal(correctionForm.getNewAmount()), correctionForm.getBillToParty(),
                        correctionForm.getBillToParty(), loginUser, true,
                        engMet, "FL", BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
            }
        }
    }

    public ActionForward editCorrection(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLCorrectionForm correctionForm = (LCLCorrectionForm) form;
        LclCorrection lclCorrection = new LCLCorrectionDAO().findById(correctionForm.getCorrectionId());
        request.setAttribute("lclCorrection", lclCorrection);
        LclUtils utils = new LclUtils();

        LclBl bl = new LCLBlDAO().getByProperty("lclFileNumber.id", correctionForm.getFileId());
        correctionForm.setOrigin(null != bl.getPortOfOrigin()
                ? utils.getConcatenatedOriginByUnlocation(bl.getPortOfOrigin()) : "");
        correctionForm.setDestination(null != bl.getFinalDestination()
                ? utils.getConcatenatedOriginByUnlocation(bl.getFinalDestination())
                : utils.getConcatenatedOriginByUnlocation(bl.getPortOfDestination()));
        correctionForm.setBillingType(new BlCorrectionUtils().setBillToPartyForCorrectionCharges(bl.getBillToParty()));
        correctionForm.setFileNo(bl.getLclFileNumber().getFileNumber());

        correctionForm.setExpShipperNo(lclCorrection.getNewShipper() != null ? lclCorrection.getNewShipper().getAccountno() : "");
        correctionForm.setExpShipperName(lclCorrection.getNewShipper() != null ? lclCorrection.getNewShipper().getAccountName() : "");

        correctionForm.setExpAgentNo(lclCorrection.getNewAgent() != null ? lclCorrection.getNewAgent().getAccountno() : "");
        correctionForm.setExpAgentName(lclCorrection.getNewAgent() != null ? lclCorrection.getNewAgent().getAccountName() : "");

        correctionForm.setExpForwarderNo(lclCorrection.getNewForwarder() != null ? lclCorrection.getNewForwarder().getAccountno() : "");
        correctionForm.setExpForwarderName(lclCorrection.getNewForwarder() != null ? lclCorrection.getNewForwarder().getAccountName() : "");

        correctionForm.setExpThirdPartyNo(lclCorrection.getNewThirdParty() != null ? lclCorrection.getNewThirdParty().getAccountno() : "");
        correctionForm.setExpThirdPartyName(lclCorrection.getNewThirdParty() != null ? lclCorrection.getNewThirdParty().getAccountName() : "");

        request.setAttribute("bl", bl);

        if ("F".equalsIgnoreCase(bl.getBillToParty())) {
            correctionForm.setCustomerAcctNo(bl.getFwdAcct().getAccountno());
            correctionForm.setCustomerAcctName(bl.getFwdAcct().getAccountName());
        } else if ("S".equalsIgnoreCase(bl.getBillToParty())) {
            correctionForm.setCustomerAcctNo(bl.getShipAcct().getAccountno());
            correctionForm.setCustomerAcctName(bl.getShipAcct().getAccountName());
        } else if ("T".equalsIgnoreCase(bl.getBillToParty())) {
            correctionForm.setCustomerAcctNo(bl.getThirdPartyAcct().getAccountno());
            correctionForm.setCustomerAcctName(bl.getThirdPartyAcct().getAccountName());
        } else if ("A".equalsIgnoreCase(bl.getBillToParty())) {
            correctionForm.setCustomerAcctNo(bl.getAgentAcct().getAccountno());
            correctionForm.setCustomerAcctName(bl.getAgentAcct().getAccountName());
        }
        this.setCorrectionReq(correctionForm, request);
        this.setCreditDebitEmail(lclCorrection, correctionForm, request);
        new BlCorrectionUtils().setChargeList(lclCorrection.getId(), correctionForm, request);
        this.setCommodityList(correctionForm, bl);
        return mapping.findForward("addOrEdit");
    }

    public ActionForward addCorrection(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLCorrectionForm correctionForm = (LCLCorrectionForm) form;
        correctionForm.setViewMode("");
        correctionForm.setCorrectionId(0l);
        LclUtils utils = new LclUtils();
        setCorrectionReq(correctionForm, request);
        LclBl bl = new LCLBlDAO().getByProperty("lclFileNumber.id", correctionForm.getFileId());
        LclCorrection correction = new LclCorrection();
        if (null != bl.getThirdPartyAcct()) {
            correction.setThirdPartyAcct(bl.getThirdPartyAcct());
            request.setAttribute("lclCorrection", correction);
        }
        correctionForm.setOrigin(null != bl.getPortOfOrigin() ? utils.getConcatenatedOriginByUnlocation(bl.getPortOfOrigin()) : "");
        correctionForm.setDestination(null != bl.getFinalDestination() ? utils.getConcatenatedOriginByUnlocation(bl.getFinalDestination())
                : utils.getConcatenatedOriginByUnlocation(bl.getPortOfDestination()));

        correctionForm.setBillingType(new BlCorrectionUtils().setBillToPartyForCorrectionCharges(bl.getBillToParty()));
        correctionForm.setFileNo(bl.getLclFileNumber().getFileNumber());
        request.setAttribute("bl", bl);
        correctionForm.setExpShipperNo(bl.getShipAcct() != null ? bl.getShipAcct().getAccountno() : "");
        correctionForm.setExpShipperName(bl.getShipAcct() != null ? bl.getShipAcct().getAccountName() : "");

        correctionForm.setExpAgentNo(bl.getAgentAcct() != null ? bl.getAgentAcct().getAccountno() : "");
        correctionForm.setExpAgentName(bl.getAgentAcct() != null ? bl.getAgentAcct().getAccountName() : "");

        correctionForm.setExpForwarderNo(bl.getFwdAcct() != null ? bl.getFwdAcct().getAccountno() : "");
        correctionForm.setExpForwarderName(bl.getFwdAcct() != null ? bl.getFwdAcct().getAccountName() : "");

        correctionForm.setExpThirdPartyNo(bl.getThirdPartyAcct() != null ? bl.getThirdPartyAcct().getAccountno() : "");
        correctionForm.setExpThirdPartyName(bl.getThirdPartyAcct() != null ? bl.getThirdPartyAcct().getAccountName() : "");
        String _acctNo = "F".equalsIgnoreCase(bl.getBillToParty()) ? bl.getFwdAcct().getAccountno()
                : "S".equalsIgnoreCase(bl.getBillToParty()) ? bl.getShipAcct().getAccountno()
                        : "T".equalsIgnoreCase(bl.getBillToParty()) ? bl.getThirdPartyAcct().getAccountno()
                                : "A".equalsIgnoreCase(bl.getBillToParty()) ? bl.getAgentAcct().getAccountno() : "";

        String _acctName = "F".equalsIgnoreCase(bl.getBillToParty()) ? bl.getFwdAcct().getAccountName()
                : "S".equalsIgnoreCase(bl.getBillToParty()) ? bl.getShipAcct().getAccountName()
                        : "T".equalsIgnoreCase(bl.getBillToParty()) ? bl.getThirdPartyAcct().getAccountName()
                                : "A".equalsIgnoreCase(bl.getBillToParty()) ? bl.getAgentAcct().getAccountName() : "";

        correctionForm.setCustomerAcctNo(_acctNo);
        correctionForm.setCustomerAcctName(_acctName);

        correctionForm.setCostAmount(new LclCostChargeDAO().getTotalLclCostAmountByFileNumber(correctionForm.getFileId()));

        new BlCorrectionUtils().setCorrection(correctionForm, bl, request);

        this.setCommodityList(correctionForm, bl);
        request.setAttribute("lclCorrectionForm", correctionForm);
        request.setAttribute("lclCorrection", correction);
        return mapping.findForward("addOrEdit");
    }

    public void setCommodityList(LCLCorrectionForm correctionForm, LclBl lclBl) throws Exception {
        List<CorrectionCommodity> commodityList = null;
        commodityList = new CorrectionCommodityDAO().getCommodityCodeList(correctionForm.getCorrectionId());
        List<RateModel> pieceList = new ArrayList<RateModel>();

        if (CommonUtils.isEmpty(commodityList) && lclBl != null) {
            Collections.reverse(lclBl.getLclFileNumber().getLclBlPieceList());
            for (LclBlPiece piece : lclBl.getLclFileNumber().getLclBlPieceList()) {
                RateModel model = new RateModel();
                model.setPieceCount(piece.getActualPieceCount());
                model.setBarrel(piece.isIsBarrel());
                model.setHazmat(piece.getHazmat());
                model.setCommodityType(piece.getCommodityType());
                model.setCft(piece.getActualVolumeImperial());
                model.setKgs(piece.getActualWeightMetric());
                model.setCbm(piece.getActualVolumeMetric());
                model.setLbs(piece.getActualWeightImperial());
                pieceList.add(model);
            }
        } else {
            for (CorrectionCommodity commodity : commodityList) {
                RateModel model = new RateModel();
                model.setCommodityType(commodity.getCommodityType());
                model.setPieceCount(0);
                model.setBarrel(false);
                model.setHazmat(false);
                model.setCft(commodity.getTotalCft());
                model.setKgs(commodity.getTotalKgs());
                model.setCbm(commodity.getTotalCbm());
                model.setLbs(commodity.getTotalLbs());
                pieceList.add(model);
            }
        }
        correctionForm.setCommodityList(pieceList);
    }

    public ActionForward viewCorrections(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLCorrectionForm correctionForm = (LCLCorrectionForm) form;
        LclUtils utils = new LclUtils();
        correctionForm.setViewMode("view");
        LclCorrection lclCorrection = new LCLCorrectionDAO().findById(correctionForm.getCorrectionId());
        request.setAttribute("lclCorrection", lclCorrection);
        setCorrectionReq(correctionForm, request);
        LclBl bl = new LCLBlDAO().getByProperty("lclFileNumber.id", correctionForm.getFileId());
        correctionForm.setOrigin(null != bl.getPortOfOrigin()
                ? utils.getConcatenatedOriginByUnlocation(bl.getPortOfOrigin()) : "");
        correctionForm.setDestination(null != bl.getFinalDestination()
                ? utils.getConcatenatedOriginByUnlocation(bl.getFinalDestination())
                : utils.getConcatenatedOriginByUnlocation(bl.getPortOfDestination()));
        correctionForm.setBillingType(new BlCorrectionUtils().setBillToPartyForCorrectionCharges(bl.getBillToParty()));
        correctionForm.setFileNo(bl.getLclFileNumber().getFileNumber());
        request.setAttribute("bl", bl);

        correctionForm.setExpShipperNo(lclCorrection.getNewShipper() != null ? lclCorrection.getNewShipper().getAccountno() : "");
        correctionForm.setExpShipperName(lclCorrection.getNewShipper() != null ? lclCorrection.getNewShipper().getAccountName() : "");

        correctionForm.setExpAgentNo(lclCorrection.getNewAgent() != null ? lclCorrection.getNewAgent().getAccountno() : "");
        correctionForm.setExpAgentName(lclCorrection.getNewAgent() != null ? lclCorrection.getNewAgent().getAccountName() : "");

        correctionForm.setExpForwarderNo(lclCorrection.getNewForwarder() != null ? lclCorrection.getNewForwarder().getAccountno() : "");
        correctionForm.setExpForwarderName(lclCorrection.getNewForwarder() != null ? lclCorrection.getNewForwarder().getAccountName() : "");

        correctionForm.setExpThirdPartyNo(lclCorrection.getNewThirdParty() != null ? lclCorrection.getNewThirdParty().getAccountno() : "");
        correctionForm.setExpThirdPartyName(lclCorrection.getNewThirdParty() != null ? lclCorrection.getNewThirdParty().getAccountName() : "");

        String _acctNo = "F".equalsIgnoreCase(bl.getBillToParty()) ? bl.getFwdAcct().getAccountno()
                : "S".equalsIgnoreCase(bl.getBillToParty()) ? bl.getShipAcct().getAccountno()
                        : "T".equalsIgnoreCase(bl.getBillToParty()) ? bl.getThirdPartyAcct().getAccountno()
                                : "A".equalsIgnoreCase(bl.getBillToParty()) ? bl.getAgentAcct().getAccountno() : "";

        String _acctName = "F".equalsIgnoreCase(bl.getBillToParty()) ? bl.getFwdAcct().getAccountName()
                : "S".equalsIgnoreCase(bl.getBillToParty()) ? bl.getShipAcct().getAccountName()
                        : "T".equalsIgnoreCase(bl.getBillToParty()) ? bl.getThirdPartyAcct().getAccountName()
                                : "A".equalsIgnoreCase(bl.getBillToParty()) ? bl.getAgentAcct().getAccountName() : "";

        correctionForm.setCustomerAcctNo(_acctNo);
        correctionForm.setCustomerAcctName(_acctName);
        new BlCorrectionUtils().setChargeList(lclCorrection.getId(), correctionForm, request);
        Set<String> creditEmailList = new HashSet<String>();
        Set<String> debitEmailList = new HashSet<String>();
        if (CommonUtils.isNotEmpty(lclCorrection.getDebitEmail())) {
            for (String email : lclCorrection.getDebitEmail().split(",")) {
                if (CommonUtils.isNotEmpty(email)) {
                    debitEmailList.add(email);
                }
            }
        }
        if (CommonUtils.isNotEmpty(lclCorrection.getCreditEmail())) {
            for (String cEmail : lclCorrection.getCreditEmail().split(",")) {
                if (CommonUtils.isNotEmpty(cEmail)) {
                    creditEmailList.add(cEmail);
                }
            }
        }
        this.setCommodityList(correctionForm, bl);
        request.setAttribute("debitEmailList", debitEmailList);
        request.setAttribute("creditEmailList", creditEmailList);
        return mapping.findForward("addOrEdit");
    }

    public ActionForward goBackCorrection(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLCorrectionForm correctionForm = (LCLCorrectionForm) form;
        if ("searchPool".equalsIgnoreCase(correctionForm.getScreenName())) {
            searchCorrectionByPool(mapping, form, request, response);
            return mapping.findForward("blCorrectionResultPool");
        } else {
            LCLCorrectionDAO correctionDAO = new LCLCorrectionDAO();
            request.setAttribute("correctionList", new BlCorrectionUtils().getCorrectionList(correctionForm.getFileId(), false));
            // request.setAttribute("correctionList", correctionDAO.findByFileId(correctionForm.getFileId(), false));
            request.setAttribute("isVoidCorrection", correctionDAO.isVoidCorrection(correctionForm.getFileId()));
            request.setAttribute("pickedVoyage", new LclUnitSsDAO().getPickedVoyageByVessel(correctionForm.getFileId(), "E"));
            return mapping.findForward("searchResult");
        }
    }

    public ActionForward viewVoidCorrection(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLCorrectionForm correctionForm = (LCLCorrectionForm) form;
        LCLCorrectionDAO correctionDAO = new LCLCorrectionDAO();
        request.setAttribute("correctionList", correctionDAO.findByFileId(correctionForm.getFileId(), true));
        request.setAttribute("pickedVoyage", new LclUnitSsDAO().getPickedVoyageByVessel(correctionForm.getFileId(), "E"));
        return mapping.findForward("viewVoidCorrection");
    }

    public void setCorrectionReq(LCLCorrectionForm correctionForm, HttpServletRequest request) throws Exception {
        DBUtil dbUtil = new DBUtil();
        request.setAttribute("correctionCodeList", dbUtil.getGenericCodeListWithDesc(52, "no", "yes", "Select Correction Code"));
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        List<LabelValueBean> selectList = new ArrayList<LabelValueBean>();
        List<GenericCode> correctionTypeList = genericCodeDAO.findByCodeNameByInOpreator(51, "'A'", "");
        //correctionTypeList.addAll(genericCodeDAO.findByCodeNameByInOpreator(51, "'Y'", ""));
        if (CommonUtils.isNotEmpty(correctionTypeList)) {
            for (GenericCode genericCode : correctionTypeList) {
                selectList.add(new LabelValueBean(genericCode.getCode() + "-" + genericCode.getCodedesc(), genericCode.getId().toString()));
            }
        }
        request.setAttribute("correctionTypeList", selectList);
        request.setAttribute("pickedVoyage", new LclUnitSsDAO().getPickedVoyageByVessel(correctionForm.getFileId(), "E"));
    }

    public ActionForward addCorrectionCharge(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLCorrectionForm lclCorrectionForm = (LCLCorrectionForm) form;
        request.setAttribute("billingList", new BlCorrectionUtils().setBillingTypeList());
        lclCorrectionForm.setChargeStatus("add");
        lclCorrectionForm.setBillToParty(lclCorrectionForm.getBillToParty());
        if (CommonUtils.isNotEmpty(lclCorrectionForm.getCorrectionType())) {
            GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
            GenericCode correctionType = genericCodeDAO.findById(lclCorrectionForm.getCorrectionType());
            request.setAttribute("correctionType", correctionType.getCode());
        }
        request.setAttribute("lclCorrectionForm", lclCorrectionForm);
        return mapping.findForward("addOrEditCorrCharge");
    }

    public ActionForward editCorrectionCharge(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLCorrectionForm correctionForm = (LCLCorrectionForm) form;
        correctionForm.setChargeStatus("edit");
        request.setAttribute("billingList", new BlCorrectionUtils().setBillingTypeList());
        if (CommonUtils.isNotEmpty(correctionForm.getBillToParty())) {
            correctionForm.setBillToParty(correctionForm.getBillToParty().substring(0, 1));
        }
        request.setAttribute("lclCorrectionForm", correctionForm);
        if (CommonUtils.isNotEmpty(correctionForm.getCorrectionType())) {
            GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
            GenericCode correctionType = genericCodeDAO.findById(correctionForm.getCorrectionType());
            request.setAttribute("correctionType", correctionType.getCode());
        }
        return mapping.findForward("addOrEditCorrCharge");
    }

    public ActionForward deleteCorrections(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLCorrectionForm correctionForm = (LCLCorrectionForm) form;
        LclUtils utils = new LclUtils();
        LCLCorrectionDAO correctionDAO = new LCLCorrectionDAO();
        correctionDAO.deleteCorrections(request.getParameter("correctionId"), getCurrentUser(request).getUserId());
        // new LCLCorrectionChargeDAO().deleteAllCorrectionCharge(correctionForm.getCorrectionId());
        String concatenatedBlNo = "(" + correctionForm.getBlNo() + "-C-" + correctionForm.getNoticeNo() + ")";
        utils.insertLCLCorrectionRemarks(REMARKS_TYPE_LCL_CORRECTIONS, correctionForm.getFileId(),
                concatenatedBlNo, "deleted", getCurrentUser(request));
        request.setAttribute("correctionList", new BlCorrectionUtils().getCorrectionList(correctionForm.getFileId(), false));
        request.setAttribute("isVoidCorrection", correctionDAO.isVoidCorrection(correctionForm.getFileId()));
        return mapping.findForward("searchResult");
    }

    public ActionForward deleteCorrectionCharges(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLCorrectionForm correctionForm = (LCLCorrectionForm) form;
        if (CommonUtils.isNotEmpty(correctionForm.getCorrectionChargeId())) {
            LCLCorrectionDAO correctionDAO = new LCLCorrectionDAO();
            LclCorrection correction = correctionDAO.findById(correctionForm.getCorrectionId());
            LCLCorrectionChargeDAO lclCorrectionChargeDAO = new LCLCorrectionChargeDAO();
            LclCorrectionCharge correctionCharge = lclCorrectionChargeDAO.findById(correctionForm.getCorrectionChargeId());
            if (correctionCharge.getOldAmount().doubleValue() != 0.0) {
                correctionCharge.setNewAmount(correctionCharge.getOldAmount());
                correctionCharge.setBillToParty(correctionCharge.getOldBillToParty());
                lclCorrectionChargeDAO.update(correctionCharge);
            } else {
                lclCorrectionChargeDAO.deleteCorrectionCharge(correctionForm.getCorrectionChargeId());
            }
            LclUtils utils = new LclUtils();
            setCorrectionReq(correctionForm, request);
            LclBl bl = new LCLBlDAO().getByProperty("lclFileNumber.id", correctionForm.getFileId());
            correctionForm.setOrigin(null != bl.getPortOfOrigin() ? utils.getConcatenatedOriginByUnlocation(bl.getPortOfOrigin()) : "");
            correctionForm.setDestination(null != bl.getFinalDestination() ? utils.getConcatenatedOriginByUnlocation(bl.getFinalDestination())
                    : utils.getConcatenatedOriginByUnlocation(bl.getPortOfDestination()));
            correctionForm.setBillingType(new BlCorrectionUtils().setBillToPartyForCorrectionCharges(bl.getBillToParty()));
            correctionForm.setFileNo(bl.getLclFileNumber().getFileNumber());
            request.setAttribute("bl", bl);
            String _acctNo = "F".equalsIgnoreCase(bl.getBillToParty()) ? bl.getFwdAcct().getAccountno()
                    : "S".equalsIgnoreCase(bl.getBillToParty()) ? bl.getShipAcct().getAccountno()
                            : "T".equalsIgnoreCase(bl.getBillToParty()) ? bl.getThirdPartyAcct().getAccountno()
                                    : "A".equalsIgnoreCase(bl.getBillToParty()) ? bl.getAgentAcct().getAccountno() : "";

            String _acctName = "F".equalsIgnoreCase(bl.getBillToParty()) ? bl.getFwdAcct().getAccountName()
                    : "S".equalsIgnoreCase(bl.getBillToParty()) ? bl.getShipAcct().getAccountName()
                            : "T".equalsIgnoreCase(bl.getBillToParty()) ? bl.getThirdPartyAcct().getAccountName()
                                    : "A".equalsIgnoreCase(bl.getBillToParty()) ? bl.getAgentAcct().getAccountName() : "";
            correctionForm.setCustomerAcctNo(_acctNo);
            correctionForm.setCustomerAcctName(_acctName);
            this.setCreditDebitEmail(correctionCharge.getLclCorrection(), correctionForm, request);
            if (CommonUtils.isNotEmpty(correction.getId())) {
                this.setCreditDebitEmail(correction, correctionForm, request);
            }
            new BlCorrectionUtils().setCorrection(correctionForm, bl, request);
            setCommodityList(correctionForm, bl);

            correctionDAO.updateCorrectionByField(correction.getId(), "current_profit", correctionForm.getCurrentProfit());
            correctionDAO.updateCorrectionByField(correction.getId(), "profit_aftercn", correctionForm.getProfitAfterCN());
            correctionDAO.updateCorrectionByField(correction.getId(), "debit_email", correctionForm.getDebitMemoEmail());
            correctionDAO.updateCorrectionByField(correction.getId(), "credit_email", correctionForm.getCreditMemoEmail());
        }
        return mapping.findForward("addOrEdit");
    }

    public ActionForward displayByPool(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("loginName", getCurrentUser(request).getLoginName());
        request.setAttribute("userId", getCurrentUser(request).getUserId());
        DBUtil dbUtil = new DBUtil();
        request.setAttribute("correctionCodeList", dbUtil.getGenericCodeList(52, "no", "Select Correction Code"));
        return mapping.findForward("blCorrectionSearchPool");
    }

    public ActionForward searchCorrectionByPool(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLCorrectionForm correctionForm = (LCLCorrectionForm) form;
        request.setAttribute("correctionList", new BlCorrectionUtils().searchCnPoolList(correctionForm));
        //LCLCorrectionDAO correctionDAO = new LCLCorrectionDAO();
        //request.setAttribute("lclBlCorrectionSearchList", correctionDAO.getSearchCorrections(correctionForm));
        correctionForm.setUserId(getCurrentUser(request).getUserId());
        request.setAttribute("lclCorrectionForm", correctionForm);
        request.setAttribute("disabledFilter", correctionForm.getFilterBy().equalsIgnoreCase("1") ? "D" : "");
        request.setAttribute("showAllFilter", correctionForm.getFilterBy());
        return mapping.findForward("blCorrectionResultPool");
    }

    public ActionForward viewAuthenticationScreen(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLCorrectionForm lclCorrectionForm = (LCLCorrectionForm) form;
        lclCorrectionForm.setUserPassword(getCurrentUser(request).getPassword());
        request.setAttribute("lclCorrectionForm", lclCorrectionForm);
        return mapping.findForward("authenticScreen");
    }

    public ActionForward approveCorrections(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        synchronized (this) {
            LCLCorrectionForm correctionForm = (LCLCorrectionForm) form;
            LCLCorrectionDAO lclCorrectionDAO = new LCLCorrectionDAO();
            User user = getCurrentUser(request);
            LclCorrection lclCorrection = lclCorrectionDAO.findById(correctionForm.getCorrectionId());
            if (CommonUtils.notIn(lclCorrection.getStatus(), "A", "Q")) {
                //-----------Correction AR side Charge Updated  method----------------//
                new BlCorrectionUtils().createLclCorrections(user, true, lclCorrection,
                        true, lclCorrection.getLclFileNumber().getLclBl());

                request.setAttribute("message", "Correction Notice "
                        + correctionForm.getConcatenatedBlNo() + " is Approved and Posted");

                if (lclCorrection.getType().getCode().equalsIgnoreCase("A")) {
                    sentPdfForCorrection_A(correctionForm, lclCorrection, user, request);
                }

                lclCorrectionDAO.approveCorrections(correctionForm.getCorrectionId(), user.getUserId(), "A");
                //-----------Updating FFComm method----------------//
                this.manipulateFFCommissionCost(lclCorrection, user);
            }
            request.setAttribute("correctionList", new BlCorrectionUtils().searchCnPoolList(correctionForm));
            request.setAttribute("lclCorrectionForm", correctionForm);
        }
        return mapping.findForward("blCorrectionResultPool");
    }

    public void sentPdfForCorrection_A(LCLCorrectionForm correctionForm, LclCorrection lclCorrection,
            User user, HttpServletRequest request) throws Exception {

        LCLCorrectionNoticeBean lclCorrectionNoticeBean = new LCLCorrectionDAO().getExportCorrectionFileds(lclCorrection.getId(), lclCorrection.getLclFileNumber().getId());
        List<LclCorrectionCharge> correctedList = new LCLCorrectionChargeDAO().findByProperty("lclCorrection.id", lclCorrection.getId());
        LclBl BL = lclCorrection.getLclFileNumber().getLclBl();
        new LCLBlDAO().getCurrentSession().evict(BL);

        Map<String, List<LCLCorrectionChargeBean>> creditPartyMap = new HashMap<String, List<LCLCorrectionChargeBean>>();
        Map<String, List<LCLCorrectionChargeBean>> debitPartyMap = new HashMap<String, List<LCLCorrectionChargeBean>>();
        Map<String, List<LCLCorrectionChargeBean>> bothDebitPartyMap = new HashMap<String, List<LCLCorrectionChargeBean>>();
        List<LCLCorrectionChargeBean> debitList = new ArrayList<LCLCorrectionChargeBean>();
        List<LCLCorrectionChargeBean> creditList = new ArrayList<LCLCorrectionChargeBean>();

        if (correctedList != null) {
            Collections.reverse(correctedList);
            for (LclCorrectionCharge charge : correctedList) {
                String previous_billToParty = charge.getOldBillToParty() != null ? charge.getOldBillToParty() : "";
                LCLCorrectionChargeBean bean = null;

                TradingPartner old_acct = previous_billToParty.equalsIgnoreCase("A") ? lclCorrection.getOldAgent()
                        : previous_billToParty.equalsIgnoreCase("S") ? lclCorrection.getOldShipper()
                                : previous_billToParty.equalsIgnoreCase("F") ? lclCorrection.getOldForwarder()
                                        : lclCorrection.getThirdPartyAcct();
                TradingPartner new_acct = charge.getBillToParty().equalsIgnoreCase("A") ? lclCorrection.getNewAgent()
                        : charge.getBillToParty().equalsIgnoreCase("S") ? lclCorrection.getNewShipper()
                                : charge.getBillToParty().equalsIgnoreCase("F") ? lclCorrection.getNewForwarder()
                                        : lclCorrection.getNewThirdParty();

                if (!previous_billToParty.equalsIgnoreCase(charge.getBillToParty())) {
                    if (old_acct != null) {
                        bean = new LCLCorrectionChargeBean();
                        bean.setChargeId(charge.getGlMapping().getId());
                        bean.setChargeCode(charge.getGlMapping().getChargeCode());
                        bean.setNewAmount(charge.getNewAmount());
                        bean.setOldAmount(charge.getOldAmount());
                        bean.setDifferenceAmount(charge.getOldAmount().multiply(new BigDecimal(-1))); // credit note should be in negative);
                        if (creditPartyMap.containsKey(old_acct.getAccountno() + "#" + previous_billToParty)) {
                            creditList = (List<LCLCorrectionChargeBean>) creditPartyMap.get(old_acct.getAccountno() + "#" + previous_billToParty);
                            creditList.add(bean);
                            creditPartyMap.put(old_acct.getAccountno() + "#" + previous_billToParty, creditList);
                        } else {
                            creditList = new ArrayList<LCLCorrectionChargeBean>();
                            creditList.add(bean);
                            creditPartyMap.put(old_acct.getAccountno() + "#" + previous_billToParty, creditList);
                        }
                    }
                    bean = new LCLCorrectionChargeBean();
                    bean.setChargeId(charge.getGlMapping().getId());
                    bean.setChargeCode(charge.getGlMapping().getChargeCode());
                    bean.setNewAmount(charge.getNewAmount());
                    bean.setOldAmount(charge.getOldAmount());
                    bean.setDifferenceAmount(charge.getNewAmount()); // credit note should be in negative);
                    if (debitPartyMap.containsKey(new_acct.getAccountno() + "#" + charge.getBillToParty())) {
                        debitList = (List<LCLCorrectionChargeBean>) debitPartyMap.get(new_acct.getAccountno() + "#" + charge.getBillToParty());
                        debitList.add(bean);
                        debitPartyMap.put(new_acct.getAccountno() + "#" + charge.getBillToParty(), debitList);
                    } else {
                        debitList = new ArrayList<LCLCorrectionChargeBean>();
                        debitList.add(bean);
                        debitPartyMap.put(new_acct.getAccountno() + "#" + charge.getBillToParty(), debitList);
                    }
                } else if (charge.getBillToParty().equalsIgnoreCase(previous_billToParty)
                        && !new_acct.equals(old_acct)) {
                    if (old_acct != null) {
                        bean = new LCLCorrectionChargeBean();
                        bean.setChargeId(charge.getGlMapping().getId());
                        bean.setChargeCode(charge.getGlMapping().getChargeCode());
                        bean.setNewAmount(charge.getNewAmount());
                        bean.setOldAmount(charge.getOldAmount());
                        bean.setDifferenceAmount(charge.getOldAmount().multiply(new BigDecimal(-1))); // credit note should be in negative);
                        if (creditPartyMap.containsKey(old_acct.getAccountno() + "#" + previous_billToParty)) {
                            creditList = (List<LCLCorrectionChargeBean>) creditPartyMap.get(old_acct.getAccountno() + "#" + previous_billToParty);
                            creditList.add(bean);
                            creditPartyMap.put(old_acct.getAccountno() + "#" + previous_billToParty, creditList);
                        } else {
                            creditList = new ArrayList<LCLCorrectionChargeBean>();
                            creditList.add(bean);
                            creditPartyMap.put(old_acct.getAccountno() + "#" + previous_billToParty, creditList);
                        }
                    }
                    bean = new LCLCorrectionChargeBean();
                    bean.setChargeId(charge.getGlMapping().getId());
                    bean.setChargeCode(charge.getGlMapping().getChargeCode());
                    bean.setNewAmount(charge.getNewAmount());
                    bean.setOldAmount(charge.getOldAmount());
                    bean.setDifferenceAmount(charge.getNewAmount()); // credit note should be in negative);
                    if (debitPartyMap.containsKey(new_acct.getAccountno() + "#" + charge.getBillToParty())) {
                        debitList = (List<LCLCorrectionChargeBean>) debitPartyMap.get(new_acct.getAccountno() + "#" + charge.getBillToParty());
                        debitList.add(bean);
                        debitPartyMap.put(new_acct.getAccountno() + "#" + charge.getBillToParty(), debitList);
                    } else {
                        debitList = new ArrayList<LCLCorrectionChargeBean>();
                        debitList.add(bean);
                        debitPartyMap.put(new_acct.getAccountno() + "#" + charge.getBillToParty(), debitList);
                    }
                } else if (charge.getBillToParty().equalsIgnoreCase(previous_billToParty)
                        && new_acct.equals(old_acct) && !charge.getOldAmount().equals(charge.getNewAmount())) {
                    bean = new LCLCorrectionChargeBean();
                    bean.setChargeId(charge.getGlMapping().getId());
                    bean.setChargeCode(charge.getGlMapping().getChargeCode());
                    bean.setNewAmount(charge.getNewAmount());
                    bean.setOldAmount(charge.getOldAmount());
                    double amount = charge.getNewAmount().subtract(charge.getOldAmount()).doubleValue();
                    bean.setDifferenceAmount(new BigDecimal(amount));
                    if (amount >= 0) {
                        if (debitPartyMap.containsKey(new_acct.getAccountno() + "#" + charge.getBillToParty())) {
                            debitList = (List<LCLCorrectionChargeBean>) debitPartyMap.get(new_acct.getAccountno() + "#" + charge.getBillToParty());
                            debitList.add(bean);
                            debitPartyMap.put(new_acct.getAccountno() + "#" + charge.getBillToParty(), debitList);
                        } else {
                            debitList = new ArrayList<LCLCorrectionChargeBean>();
                            debitList.add(bean);
                            debitPartyMap.put(new_acct.getAccountno() + "#" + charge.getBillToParty(), debitList);
                        }
                    } else if (amount < 0) {
                        if (creditPartyMap.containsKey(old_acct.getAccountno() + "#" + charge.getBillToParty())) {
                            creditList = (List<LCLCorrectionChargeBean>) creditPartyMap.get(old_acct.getAccountno() + "#" + charge.getBillToParty());
                            creditList.add(bean);
                            creditPartyMap.put(old_acct.getAccountno() + "#" + charge.getBillToParty(), creditList);
                        } else {
                            creditList = new ArrayList<LCLCorrectionChargeBean>();
                            creditList.add(bean);
                            creditPartyMap.put(old_acct.getAccountno() + "#" + charge.getBillToParty(), creditList);
                        }
                    }
                }
            }
        }

        if (CommonUtils.isNotEmpty(debitPartyMap)) {
            this.sendCreditDebitNotes(debitPartyMap, "DEBIT NOTE", lclCorrectionNoticeBean, lclCorrection, BL,
                    correctionForm, user, request);
        }
        if (CommonUtils.isNotEmpty(creditPartyMap)) {
            this.sendCreditDebitNotes(creditPartyMap, "CREDIT NOTE", lclCorrectionNoticeBean, lclCorrection, BL,
                    correctionForm, user, request);
        }
    }

    public void sendCreditDebitNotes(Map<String, List<LCLCorrectionChargeBean>> map, String debitOrCreditNote,
            LCLCorrectionNoticeBean lclCorrectionNoticeBean, LclCorrection lclCorrection, LclBl BL,
            LCLCorrectionForm correctionForm, User user, HttpServletRequest request) throws Exception {
        if (CommonUtils.isNotEmpty(map)) {
            if (lclCorrectionNoticeBean != null) {
                for (Map.Entry party : map.entrySet()) {
                    List<LCLCorrectionChargeBean> beanList = (List<LCLCorrectionChargeBean>) party.getValue();
                    String key = (String) party.getKey();
                    String[] customerdata = key.split("#");
                    TradingPartner customer = new TradingPartnerDAO().findById(customerdata[0]);

                    lclCorrectionNoticeBean.setCustomerAcctNo(customer.getAccountno());
                    lclCorrectionNoticeBean.setCustomer(customer.getAccountName());
                    lclCorrectionNoticeBean.setDebitOrCreditNote(debitOrCreditNote);
                    lclCorrectionNoticeBean.setCorrectionNo(String.valueOf(lclCorrection.getCorrectionNo()));
                    boolean isCreditDebitNote = new CustomerAccountingDAO().isCreditDebitNote(customer.getAccountno());
                    if (isCreditDebitNote) {
                        CreditDebitNote creditDebitNote = new BlCorrectionUtils().insertCreditDebitNote(lclCorrectionNoticeBean.getBlNo(),
                                String.valueOf(lclCorrection.getCorrectionNo()), lclCorrectionNoticeBean.getCustomer(),
                                lclCorrectionNoticeBean.getCustomerAcctNo(), debitOrCreditNote, customerdata[1]);
                        for (LCLCorrectionChargeBean charge : beanList) {
                            new BlCorrectionUtils().insertCreditDebitCharge(creditDebitNote.getId(), charge.getChargeId(), charge.getDifferenceAmount());
                        }
                        List<ImportsManifestBean> custContact = new CustomerContactDAO().getCorrectionCreditDebitEmailForExport(customer.getAccountno());
                        StringBuilder creditDebitEmail = new StringBuilder();
                        for (ImportsManifestBean contact : custContact) {
                            creditDebitEmail.append(contact.getEmail()).append(",");
                        }
                        if (!creditDebitEmail.toString().equalsIgnoreCase("")) {
                            CustAddress cust = new CustAddressDAO().findByAccountNo(customer.getAccountno());
                            if (cust != null) {
                                String con_address = CommonUtils.isNotEmpty(cust.getAddress1()) ? cust.getAddress1() + "\n" : "";
                                con_address += CommonUtils.isNotEmpty(cust.getCity()) ? cust.getCity() : "";
                                con_address += CommonUtils.isNotEmpty(cust.getState()) && CommonUtils.isNotEmpty(cust.getCity()) ? ","
                                        + cust.getState() : CommonUtils.isNotEmpty(cust.getState()) ? "" + cust.getState() : "";
                                con_address += CommonUtils.isNotEmpty(cust.getCountry()) ? "," + cust.getCountry() + "\n" : "\n";
                                con_address += CommonUtils.isNotEmpty(cust.getZip()) ? cust.getZip() : "";
                                con_address += CommonUtils.isNotEmpty(cust.getPhone()) ? " " + "PHONE" + cust.getPhone() : "";
                                lclCorrectionNoticeBean.setBillToPartyAddress(con_address);
                            }
                            request.setAttribute("lclCorrectionChargesList", beanList);
                            this.sentEmail(lclCorrectionNoticeBean, correctionForm, lclCorrection,
                                    creditDebitEmail.toString(), debitOrCreditNote, user, request, "");
                        }
                    } else {
                        CreditDebitNote creditDebitNote = new BlCorrectionUtils().insertCreditDebitNote(lclCorrectionNoticeBean.getBlNo(),
                                String.valueOf(lclCorrection.getCorrectionNo()), lclCorrectionNoticeBean.getCustomer(),
                                lclCorrectionNoticeBean.getCustomerAcctNo(), "Freight Invoice", customerdata[1]);
                        for (LCLCorrectionChargeBean charge : beanList) {
                            new BlCorrectionUtils().insertCreditDebitCharge(creditDebitNote.getId(),
                                    charge.getChargeId(), charge.getDifferenceAmount());
                        }
                        List<ImportsManifestBean> custContact = new CustomerContactDAO().getCorrectionCreditDebitEmailForExport(customer.getAccountno());
                        StringBuilder creditDebitEmail = new StringBuilder();
                        for (ImportsManifestBean contact : custContact) {
                            creditDebitEmail.append(contact.getEmail()).append(",");
                        }
                        if (!creditDebitEmail.toString().equalsIgnoreCase("")) {
                            this.sentEmail(lclCorrectionNoticeBean, correctionForm, lclCorrection,
                                    creditDebitEmail.toString(), "Freight Invoice", user, request, customerdata[1]);
                        }
                    }
                }
            }
        }
    }

    public void sentEmail(LCLCorrectionNoticeBean bean, LCLCorrectionForm correctionForm,
            LclCorrection lclCorrection, String email, String debitCreditNote,
            User user, HttpServletRequest request, String billToParty) throws Exception {
        String fileLocation = null;
        String realPath = request.getSession().getServletContext().getRealPath("/");
        String outputFileName = LoadLogisoftProperties.getProperty("reportLocation");
        File file = null;
        if (!debitCreditNote.contains("Freight")) {
            file = new File(outputFileName + "/" + FOLDER_NAME + "/" + MODULENAME + "/" + "BlCorrection" + "/" + DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/");
            if (!file.exists()) {
                file.mkdirs();
            }
            outputFileName = outputFileName + "/" + FOLDER_NAME + "/" + MODULENAME + "/" + "BlCorrection" + "/"
                    + DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/" + "LCL-CN-"
                    + bean.getBlNo() + "-" + lclCorrection.getCorrectionNo() + "-" + debitCreditNote + "(" + bean.getCustomerAcctNo() + ").pdf";
            LclCorrectionDebitCreditPdfCreator pdf = new LclCorrectionDebitCreditPdfCreator();
            fileLocation = pdf.createReport(bean, outputFileName, realPath,
                    request, correctionForm.getFileId(), correctionForm.getSelectedMenu());
            String subject = "LCL" + "-" + bean.getBlNo() + "-" + bean.getDebitOrCreditNote() + " For "
                    + bean.getCustomer() + "(" + bean.getCustomerAcctNo() + ")";
            new LclUtils().sendMailWithoutPrintConfig(fileLocation, "BlCorrection", subject, "Email", "Pending", email,
                    lclCorrection.getLclFileNumber().getFileNumber(), "BlCorrection", "", "", user);
        } else {
            LclBLPdfCreator lclBLPdfCreator = new LclBLPdfCreator();
            file = new File(LoadLogisoftProperties.getProperty("reportLocation") + "/" + FOLDER_NAME
                    + "/" + MODULENAME + "/" + "Bill Of Lading" + "/" + DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/");
            if (!file.exists()) {
                file.mkdirs();
            }
            lclBLPdfCreator.setPrintdocumentName("Freight Invoice");
            lclBLPdfCreator.setExportBilltoParty(billToParty);
            outputFileName = outputFileName + "/" + FOLDER_NAME + "/" + MODULENAME + "/"
                    + "Bill Of Lading" + "/" + DateUtils.formatDate(new Date(), "yyyy/MM/dd")
                    + "/" + "Freight_Invoice_" + lclCorrection.getLclFileNumber().getFileNumber() + ".pdf";
            lclBLPdfCreator.createReport(realPath, outputFileName, "Freight Invoice",
                    lclCorrection.getLclFileNumber().getId().toString(), "", String.valueOf(lclCorrection.getCorrectionNo()), true);
            String subject = "LCL" + "-" + bean.getBlNo() + "-" + debitCreditNote + " For "
                    + bean.getCustomer() + "(" + bean.getCustomerAcctNo() + ")";
            new LclUtils().sendMailWithoutPrintConfig(outputFileName, "BlCorrection", subject, "Email", "Pending", email,
                    lclCorrection.getLclFileNumber().getFileNumber(), "BlCorrection", "", "", user);
        }
    }

    public ActionForward reCalculateCharges(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LCLCorrectionForm correctionForm = (LCLCorrectionForm) form;
        User loginUser = getCurrentUser(request);
        List<RateModel> commodityList = correctionForm.getCommodityList();
        LclBl lclbl = new LCLBlDAO().findById(correctionForm.getFileId());
        RatesCalculationModel ratesModel = new RatesCalculationModel();
        ratesModel.setOrigin(lclbl.getPortOfOrigin().getUnLocationCode());
        ratesModel.setPol(lclbl.getPortOfLoading().getUnLocationCode());
        ratesModel.setPod(lclbl.getPortOfDestination().getUnLocationCode());
        ratesModel.setDestination(lclbl.getFinalDestination().getUnLocationCode());
        ratesModel.setRateType(lclbl.getRateType().equalsIgnoreCase("R") ? "Y" : lclbl.getRateType());
        ratesModel.setDeliveryMetro("");
        ratesModel.setValueOfGoods(lclbl.getValueOfGoods());
        ratesModel.setInsurance(lclbl.getInsurance() ? "Y" : "N");
        ratesModel.setCalcHeavyOrExtraLength(false);
        Map<String, List<RateModel>> autoChargeList = ratesModel.calculateExportBlRates(commodityList);
        List<ChargesInfoBean> chargeList = new RateModel().getLclExportsChargeList(ratesModel, autoChargeList, commodityList);
        new BlCorrectionUtils().saveReCalculateCorrection(correctionForm, lclbl, chargeList, loginUser, request);
        setCorrectionReq(correctionForm, request);
        setCommodityList(correctionForm, lclbl);
        return mapping.findForward("addOrEdit");
    }

    public void manipulateFFCommissionCost(LclCorrection lclCorrection, User user) throws Exception {
        BigDecimal totalChargeAmount = BigDecimal.ZERO;
        LclBl lclbl = lclCorrection.getLclFileNumber().getLclBl();

        if ("R".equalsIgnoreCase(lclbl.getRateType()) && null != lclCorrection.getNewForwarder()) {
            Boolean fwdFlag = new LCLBlDAO().getFreightForwardAcctStatus(lclCorrection.getNewForwarder().getAccountno());
            if (!fwdFlag) {
                UnLocation orgin = lclbl.getPortOfOrigin();
                UnLocation pod = lclbl.getPortOfDestination();
                UnLocation destnation = lclbl.getFinalDestination();
                String terminal = null, fdPortCode = null, podPortCode = null;
                String rateType = "R".equalsIgnoreCase(lclbl.getRateType()) ? "Y" : lclbl.getRateType();
                terminal = new RefTerminalDAO().getTrmnum(orgin.getUnLocationCode(), rateType);
                fdPortCode = new PortsDAO().getFieldsByUnlocCode("eciportcode", destnation.getUnLocationCode());
                podPortCode = new PortsDAO().getFieldsByUnlocCode("eciportcode", pod.getUnLocationCode());
                String databaseSchema = LoadLogisoftProperties.getProperty("elite.database.name");
                LCLRatesDAO lclratesdao = new LCLRatesDAO(databaseSchema);
                double ffCommissionCostPercent = lclratesdao.getFFCommision(terminal, podPortCode, fdPortCode);
                for (LclCorrectionCharge charge : lclCorrection.getLclCorrectionChargeCollection()) {
                    totalChargeAmount = totalChargeAmount.add(charge.getNewAmount());
                }
                if (ffCommissionCostPercent > 0.0) {
                    GlMappingDAO glMappingDAO = new GlMappingDAO();
                    Double totalAmount = (totalChargeAmount.doubleValue() * ffCommissionCostPercent) / 100;
                    GlMapping glmapping = glMappingDAO.findByChargeCode(CommonConstants.FFCOMM_CHARGECODE, "LCLE", "AC");
                    String voyOriginID = new LclSsHeaderDAO().getvoyageOriginIdWithFileId(lclbl.getFileNumberId().toString());
                    String glAccount = glMappingDAO.getLclExportDerivedGlAccount(glmapping
                            .getId().toString(), terminal, orgin.getId().toString(), voyOriginID);
                    ManifestModel model = new ManifestModel();
                    model.setFileId(lclbl.getFileNumberId());
                    model.setForwarderNo(lclCorrection.getNewForwarder() != null ? lclCorrection.getNewForwarder().getAccountno() : null);
                    model.setAgentNo(lclCorrection.getNewAgent() != null ? lclCorrection.getNewAgent().getAccountno() : null);
                    new LclExportManifestDAO().insertLclBookingAc(model, totalAmount, glmapping, glAccount, user);
                }
            }
        }
    }
}
