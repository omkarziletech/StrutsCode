/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonUtils;
import static com.gp.cong.common.ConstantsInterface.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE;
import com.gp.cong.lcl.common.constant.BlUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.lcl.dwr.LclBlChargesCalculation;
import com.gp.cong.lcl.dwr.LclChargesCalculation;
import com.gp.cong.logisoft.domain.Ports;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.bl.LclBl;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlAc;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlPiece;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBlAcDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclConsolidateDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLBlDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LclBLPieceDAO;
import com.gp.cvst.logisoft.domain.GlMapping;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LclBlCostAndChargeForm;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;

/**
 *
 * @author lakshh
 */
public class LclBlCostAndChargeAction extends LogiwareDispatchAction implements LclCommonConstant {

    private static final Logger log = Logger.getLogger(LclBlCostAndChargeAction.class);
    private static String CHARGE_DESC = "chargeDesc";
    private BlUtils blUtils = new BlUtils();

    public ActionForward display(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclBlCostAndChargeForm lclBlCostAndChargeForm = (LclBlCostAndChargeForm) form;
        request.setAttribute("fileNumber", request.getParameter("fileNumber"));
        request.setAttribute("destination", lclBlCostAndChargeForm.getDestination());
        if (CommonUtils.isNotEmpty(lclBlCostAndChargeForm.getDestination())) {
            PortsDAO portsDAO = new PortsDAO();
            Ports ports = portsDAO.getByProperty("unLocationCode", lclBlCostAndChargeForm.getDestination());
            if (ports != null && ports.getEciportcode() != null && !ports.getEciportcode().trim().equals("")) {
                request.setAttribute("engmet", ports.getEngmet());
            }
            request.setAttribute("destination", lclBlCostAndChargeForm.getDestination());
        }
        if (CommonUtils.isNotEmpty(lclBlCostAndChargeForm.getFwdAcctNo())) {
            Boolean fwdFlag = new LCLBlDAO().getFreightForwardAcctStatus(lclBlCostAndChargeForm.getFwdAcctNo());
            if (fwdFlag) {
                request.setAttribute("frtFwdAcct", "fwd");
            }
        }
        request.setAttribute("billToPartyList", new LclUtils().getBillingTypeByLclE(lclBlCostAndChargeForm.getBillToParty(), lclBlCostAndChargeForm.getBlBillingType()));
        return mapping.findForward(SUCCESS);
    }

    public ActionForward addCharges(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            LclBlCostAndChargeForm lclBlCostAndChargeForm = (LclBlCostAndChargeForm) form;
            LclBlAcDAO lclBlAcDAO = new LclBlAcDAO();
            PortsDAO portsdao = new PortsDAO();
            LCLBlDAO lclBlDAO = new LCLBlDAO();
            User loginUser = getCurrentUser(request);
            RefTerminalDAO refterminaldao = new RefTerminalDAO();
            LclBlAc lclBlAc = lclBlCostAndChargeForm.getLclBlAc();
            List<LclBlPiece> lclBlPiecesList = new LclBLPieceDAO().findByProperty("lclFileNumber.id", lclBlCostAndChargeForm.getFileNumberId());
            Double totalWeight = 0.00;
            Double totalMeasure = 0.00;
            Double calculatedWeight = 0.00;
            Double calculatedMeasure = 0.00;
            String engmet = "";
            String ofratebasis = new String();
            LclBl lclBl = lclBlDAO.getByProperty("lclFileNumber.id", lclBlCostAndChargeForm.getFileNumberId());
            String rateType = lclBl.getRateType();
            if (rateType.equalsIgnoreCase("R")) {
                rateType = "Y";
            }
            RefTerminal refterminal = refterminaldao.getTerminalByUnLocation(lclBl.getPortOfOrigin().getUnLocationCode(), rateType);
            if (refterminal != null && refterminal.getTrmnum() != null && !refterminal.getTrmnum().trim().equals("")) {
                ofratebasis = refterminal.getTrmnum() + "-";
            }
            Ports ports = null;
            if (lclBl.getFinalDestination() != null) {
                ports = portsdao.getByProperty("unLocationCode", lclBl.getFinalDestination().getUnLocationCode());
            } else {
                ports = portsdao.getByProperty("unLocationCode", lclBl.getPortOfDestination().getUnLocationCode());
            }
            if (ports != null && ports.getEngmet() != null && !ports.getEngmet().trim().equals("")) {
                ofratebasis += ports.getEciportcode() + "-";
                engmet = ports.getEngmet();
            }

            if (CommonUtils.isNotEmpty(lclBlCostAndChargeForm.getFileNumberId())) {
                lclBlAc.setLclFileNumber(new LclFileNumber(lclBlCostAndChargeForm.getFileNumberId()));
            }
            if (CommonUtils.isNotEmpty(lclBlCostAndChargeForm.getChargesCode())) {
                GlMapping glmapping = new GlMappingDAO().findById(lclBlCostAndChargeForm.getChargesCodeId());
                lclBlAc.setArglMapping(glmapping);
                lclBlAc.setApglMapping(glmapping);
            }
            //*****added for bill to party*****************
            lclBlAc.setArBillToParty(lclBlCostAndChargeForm.getBillingType());
            lclBlAc.setTransDatetime(new Date());
            lclBlAc.setEnteredBy(loginUser);
            lclBlAc.setModifiedBy(loginUser);
            lclBlAc.setEnteredDatetime(new Date());
            lclBlAc.setModifiedDatetime(new Date());
            if (!lclBlCostAndChargeForm.getManualEntry().equalsIgnoreCase("false")) {
                lclBlAc.setManualEntry(true);
                lclBlAc.setAdjustmentAmount(BigDecimal.ZERO);
            }

            if (lclBlAc.getRatePerWeightUnit() != null && lclBlAc.getRatePerWeightUnit().doubleValue() > 0.00
                    && lclBlAc.getRatePerVolumeUnit() != null && lclBlAc.getRatePerVolumeUnit().doubleValue() > 0.00
                    && lclBlAc.getRateFlatMinimum() != null && lclBlAc.getRateFlatMinimum().doubleValue() > 0.00 && lclBlAc.isManualEntry()) {
                for (int j = 0; j < lclBlPiecesList.size(); j++) {
                    LclBlPiece lclBlPiece = (LclBlPiece) lclBlPiecesList.get(j);
                    Double weightDouble = 0.00;
                    Double weightMeasure = 0.00;

                    if (engmet != null) {
                        if (engmet.equals("E")) {
                            if (lclBlPiece.getActualWeightImperial() != null && lclBlPiece.getActualWeightImperial().doubleValue() != 0.00) {
                                weightDouble = lclBlPiece.getActualWeightImperial().doubleValue();
                            } else if (lclBlPiece.getBookedWeightImperial() != null && lclBlPiece.getBookedWeightImperial().doubleValue() != 0.00) {
                                weightDouble = lclBlPiece.getBookedWeightImperial().doubleValue();
                            }

                            if (lclBlPiece.getActualVolumeImperial() != null && lclBlPiece.getActualVolumeImperial().doubleValue() != 0.00) {
                                weightMeasure = lclBlPiece.getActualVolumeImperial().doubleValue();
                            } else if (lclBlPiece.getBookedVolumeImperial() != null && lclBlPiece.getBookedVolumeImperial().doubleValue() != 0.00) {
                                weightMeasure = lclBlPiece.getBookedVolumeImperial().doubleValue();
                            }
                        } else if (engmet.equals("M")) {
                            if (lclBlPiece.getActualWeightMetric() != null && lclBlPiece.getActualWeightMetric().doubleValue() != 0.00) {
                                weightDouble = lclBlPiece.getActualWeightMetric().doubleValue();
                            } else if (lclBlPiece.getBookedWeightMetric() != null && lclBlPiece.getBookedWeightMetric().doubleValue() != 0.00) {
                                weightDouble = lclBlPiece.getBookedWeightMetric().doubleValue();
                            }
                            if (lclBlPiece.getActualVolumeMetric() != null && lclBlPiece.getActualVolumeMetric().doubleValue() != 0.00) {
                                weightMeasure = lclBlPiece.getActualVolumeMetric().doubleValue();
                            } else if (lclBlPiece.getBookedVolumeMetric() != null && lclBlPiece.getBookedVolumeMetric().doubleValue() != 0.00) {
                                weightMeasure = lclBlPiece.getBookedVolumeMetric().doubleValue();
                            }
                        }//end of else if engmet
                    }//end of else null

                    //calculate the Total Weight Of Commodities
                    totalWeight = totalWeight + weightDouble;
                    //calculate the Total Measure Of Commodities
                    totalMeasure = totalMeasure + weightMeasure;
                }//end of for loop
                if (engmet != null) {
                    if (engmet.equals("E")) {
                        calculatedWeight = (totalWeight / 100) * lclBlAc.getRatePerWeightUnit().doubleValue();
                        lclBlAc.setRatePerWeightUnitDiv(new BigDecimal(100));
                    } else if (engmet.equals("M")) {
                        calculatedWeight = (totalWeight / 1000) * lclBlAc.getRatePerWeightUnit().doubleValue();
                        lclBlAc.setRatePerWeightUnitDiv(new BigDecimal(1000));
                    }
                }//end of else if engmet
                calculatedMeasure = totalMeasure * lclBlAc.getRatePerVolumeUnit().doubleValue();
                lclBlAc.setRatePerUnitDiv(lclBlAc.getRatePerWeightUnitDiv());
                lclBlAc.setRatePerVolumeUnitDiv(new BigDecimal(1000));
                if (calculatedWeight >= calculatedMeasure && calculatedWeight >= lclBlAc.getRateFlatMinimum().doubleValue()) {
                    lclBlAc.setArAmount(new BigDecimal(calculatedWeight));
                    lclBlAc.setRatePerUnitUom("W");
                    lclBlAc.setRatePerUnitDiv(lclBlAc.getRatePerVolumeUnitDiv());
                } else if (calculatedMeasure >= calculatedWeight && calculatedMeasure >= lclBlAc.getRateFlatMinimum().doubleValue()) {
                    lclBlAc.setArAmount(new BigDecimal(calculatedMeasure));
                    lclBlAc.setRatePerUnitUom("V");
                    lclBlAc.setRatePerUnitDiv(lclBlAc.getRatePerVolumeUnitDiv());

                } else {
                    lclBlAc.setArAmount(lclBlAc.getRateFlatMinimum());
                    lclBlAc.setRatePerUnitUom("M");
                }
            }
            if (lclBlAc.getRatePerUnit().doubleValue() > 0.00 && lclBlAc.getRatePerWeightUnit().doubleValue() == 0.00
                    && lclBlAc.getRatePerVolumeUnit().doubleValue() == 0.00) {
                lclBlAc.setRatePerUnitUom("FL");
            }
            lclBlAc.setAdjustmentAmount(CommonUtils.isNotEmpty(lclBlCostAndChargeForm.getAdjustmentAmount())
                    ? new BigDecimal(lclBlCostAndChargeForm.getAdjustmentAmount()) : BigDecimal.ZERO);
            lclBlAc.setAdjustmentComment(CommonUtils.isNotEmpty(lclBlCostAndChargeForm.getAdjustmentComment())
                    ? lclBlCostAndChargeForm.getAdjustmentComment() : "");
            lclBlAc.setBundleIntoOf(false);
            lclBlAc.setPrintOnBl(true);
            lclBlAcDAO.saveOrUpdate(lclBlAc);
            if (CommonUtils.isNotEmpty(lclBlCostAndChargeForm.getExistBillToParty())
                    && !lclBlCostAndChargeForm.getExistBillToParty().equalsIgnoreCase(lclBlAc.getArBillToParty())) {
                String notes = "UPDATED -> (Code -> " + lclBlAc.getArglMapping().getChargeCode()
                        + ")(Bill To Party -> " + lclBlCostAndChargeForm.getExistBillToParty() + " to " + lclBlAc.getArBillToParty() + ")";
                new LclRemarksDAO().insertLclRemarks(lclBlCostAndChargeForm.getFileNumberId(), REMARKS_BL_AUTO_NOTES,
                        notes, loginUser.getUserId());
            }
            String[] cafData = new LclBlChargesCalculation().getCAFCalculationContent(lclBl);
            new LclBlChargesCalculation().calculateCAFCharge(cafData[0], cafData[1], cafData[2],
                    cafData[3], lclBlPiecesList, lclBl.getBillingType(), getCurrentUser(request),
                    lclBl.getFileNumberId(), request, ports, lclBl.getBillToParty());
            List<LclBlAc> chargeList = lclBlAcDAO.getLclCostByFileNumberAsc(lclBlCostAndChargeForm.getFileNumberId());
            if (CommonUtils.isNotEmpty(lclBlPiecesList)) {
                blUtils.setWeighMeasureForBl(request, lclBlPiecesList, engmet);
                LclBlPiece lclBlPiece = lclBlPiecesList.get(0);
                if (lclBlPiece.getStdchgRateBasis() != null && !lclBlPiece.getStdchgRateBasis().trim().equals("")) {
                    request.setAttribute("stdchgratebasis", lclBlPiece.getStdchgRateBasis());
                }
                request.setAttribute("lclCommodityList", lclBlPiecesList);
                ofratebasis += lclBlPiece.getCommodityType().getCode();
                request.setAttribute("ofratebasis", ofratebasis);

            }
            blUtils.setRolledUpChargesForBl(chargeList, request, lclBlCostAndChargeForm.getFileNumberId(), lclBlAcDAO, lclBlPiecesList, lclBl.getBillingType(), engmet, lclBl);
            if (!lclBl.getBillingType().equals("P")) {
                request.setAttribute("lclBooking", lclBl.getLclFileNumber().getLclBooking()); //for Freight Agent Acct
            }
            request.setAttribute("lclBl", lclBl);
            request.setAttribute("isConsolidate", new LclConsolidateDAO().isConsolidatedByFileAB(lclBl.getFileNumberId()));
        } catch (Exception e) {
            log.info("Error in addCharges method. " + new Date(), e);
            return mapping.findForward(CHARGE_DESC);
        }
        return mapping.findForward(CHARGE_DESC);
    }

    public ActionForward deleteCharge(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            LclBlCostAndChargeForm lclBlCostAndChargeForm = (LclBlCostAndChargeForm) form;
            Ports ports = null;
            String engmet = new String();
            if (lclBlCostAndChargeForm.getDestination() != null && !lclBlCostAndChargeForm.getDestination().trim().equals("")) {
                PortsDAO portsdao = new PortsDAO();
                ports = portsdao.getByProperty("unLocationCode", lclBlCostAndChargeForm.getDestination());
                if (ports != null && ports.getEngmet() != null && !ports.getEngmet().trim().equals("")) {
                    engmet = ports.getEngmet();
                }
            }
            LclBlAcDAO lclBlAcDAO = new LclBlAcDAO();
            String id = request.getParameter("id");
            LclBlAc lclBlAc = null;
            if (CommonUtils.isNotEmpty(id)) {
                lclBlAc = lclBlAcDAO.findById(Long.parseLong(id));
                lclBlCostAndChargeForm.setFileNumberId(lclBlAc.getLclFileNumber().getId());
                lclBlAcDAO.delete(lclBlAc);
                String notes = "DELETED -> Code -> " + lclBlAc.getArglMapping().getChargeCode() + " Charge Amount ->" + lclBlAc.getArAmount();
                new LclRemarksDAO().insertLclRemarks(lclBlCostAndChargeForm.getFileNumberId(), REMARKS_BL_AUTO_NOTES,
                        notes, getCurrentUser(request).getUserId());
            }
            List<LclBlPiece> lclBlPiecesList = new LclBLPieceDAO().findByProperty("lclFileNumber.id", lclBlCostAndChargeForm.getFileNumberId());
            if (CommonUtils.isNotEmpty(lclBlPiecesList)) {
                List<LclBlAc> chargeList = lclBlAcDAO.getLclCostByFileNumberAsc(lclBlCostAndChargeForm.getFileNumberId());
                blUtils.setWeighMeasureForBl(request, lclBlPiecesList, engmet);
                blUtils.setRolledUpChargesForBl(chargeList, request, lclBlCostAndChargeForm.getFileNumberId(), lclBlAcDAO, lclBlPiecesList, lclBlCostAndChargeForm.getBillingType(), engmet, lclBlAc != null ? lclBlAc.getLclFileNumber().getLclBl() : new LCLBlDAO().findById(lclBlCostAndChargeForm.getFileNumberId()));
                request.setAttribute("isConsolidate", new LclConsolidateDAO().isConsolidatedByFileAB(lclBlCostAndChargeForm.getFileNumberId()));
            }
        } catch (Exception e) {
            log.info("Error in deleteCharge method. " + new Date(), e);
            return mapping.findForward(CHARGE_DESC);
        }
        return mapping.findForward(CHARGE_DESC);
    }

    public ActionForward editCharge(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            LclBlCostAndChargeForm lclBlCostAndChargeForm = (LclBlCostAndChargeForm) form;
            LclBlAc lclBlAc = new LclBlAcDAO().findById(lclBlCostAndChargeForm.getLclBlAc().getId());
            lclBlCostAndChargeForm.setLclBlAc(lclBlAc);
            lclBlCostAndChargeForm.setFileNumberId(lclBlAc.getLclFileNumber().getId());
            lclBlCostAndChargeForm.setChargesCode(lclBlAc.getArglMapping().getChargeCode());
            lclBlCostAndChargeForm.setChargesCodeId(lclBlAc.getArglMapping().getId());
            if (lclBlCostAndChargeForm.getManualEntry().equalsIgnoreCase("false")) {
                lclBlCostAndChargeForm.setAdjustmentAmount(lclBlAc.getAdjustmentAmount().toString());
                lclBlCostAndChargeForm.setAdjustmentComment(CommonUtils.isNotEmpty(lclBlAc.getAdjustmentComment()) ? lclBlAc.getAdjustmentComment() : "");
            }
            // lclBlCostAndChargeForm.setAdjustmentComment(lclBlAc.get);
            //*****added for bill to party*****************
            lclBlCostAndChargeForm.setBillingType(lclBlAc.getArBillToParty());
            request.setAttribute("lclBlAc", lclBlAc);
            if (CommonUtils.isNotEmpty(lclBlCostAndChargeForm.getFwdAcctNo())) {
                Boolean fwdFlag = new LCLBlDAO().getFreightForwardAcctStatus(lclBlCostAndChargeForm.getFwdAcctNo());
                if (fwdFlag) {
                    request.setAttribute("frtFwdAcct", "fwd");
                }
            }
            request.setAttribute("lclBlCostAndChargeForm", lclBlCostAndChargeForm);
            request.setAttribute("fileNumber", request.getParameter("fileNumber"));
            request.setAttribute("chargeCode", lclBlAc.getArglMapping().getChargeCode());
            request.setAttribute("blAcId", lclBlCostAndChargeForm.getLclBlAc().getId());
            request.setAttribute("billToPartyList", new LclUtils().getBillingTypeByLclE(lclBlCostAndChargeForm.getBillToParty(), lclBlCostAndChargeForm.getBlBillingType()));
        } catch (Exception e) {
            log.info("Error in editCharge method. " + new Date(), e);
            return mapping.findForward(SUCCESS);
        }
        return mapping.findForward(SUCCESS);
    }

    public ActionForward addSpotRate(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            LclBlCostAndChargeForm chargeForm = (LclBlCostAndChargeForm) form;
            String CFT = chargeForm.getRate();
            String CBM = chargeForm.getRateN();
            Boolean spotCheckBottom = chargeForm.getSpotCheckBottom();
            Boolean isOnlyOcnfrt = chargeForm.getSpotCheckOF();
            String spotComment = chargeForm.getComment();
            String billingType = request.getParameter("billingType");
            LclBl lclBL = new LCLBlDAO().findById(chargeForm.getFileNumberId());
            new LCLBlDAO().getCurrentSession().evict(lclBL);
            String rateType = lclBL.getRateType();
            if (rateType.equalsIgnoreCase("R")) {
                rateType = "Y";
            }
            MessageResources messageResources = getResources(request);
            String spotRateCommodity = messageResources.getMessage("application.spotRate.commodityCode");
            blUtils.calculateSpotRate(chargeForm.getFileNumberId(), lclBL, billingType, CBM, CFT, rateType, isOnlyOcnfrt,
                    spotCheckBottom, spotComment, spotRateCommodity, request);
            if (lclBL.getBillingType().equals("P")) {
                LclBooking lclBooking = new LCLBookingDAO().findById(chargeForm.getFileNumberId());
                request.setAttribute("lclBooking", lclBooking);//for Freight Agent Acct
            }
        } catch (Exception e) {
            return mapping.findForward(CHARGE_DESC);
        }
        return mapping.findForward(CHARGE_DESC);
    }

    public ActionForward refreshCommodity(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fileNumberId = request.getParameter("fileNumberId");
        request.setAttribute("lclCommodityList", new LclBLPieceDAO().findByProperty("lclFileNumber.id", Long.parseLong(fileNumberId)));
        return mapping.findForward("commodityBlDesc");
    }

    public ActionForward displayDocum(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclBlCostAndChargeForm lclCostAndChargeForm = (LclBlCostAndChargeForm) form;
        request.setAttribute("LclBlCostAndChargeForm", lclCostAndChargeForm);
        return mapping.findForward("display");
    }

    public ActionForward addDocumCharge(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            LclBlCostAndChargeForm costAndChargeForm = (LclBlCostAndChargeForm) form;
            if (CommonUtils.isNotEmpty(costAndChargeForm.getFileNumberId())) {
                LclCostChargeDAO lclCostChargesDAO = new LclCostChargeDAO();
                LCLBlDAO blDao = new LCLBlDAO();
                LclBlAcDAO blAcDAO = new LclBlAcDAO();
                Date d = new Date();
                User loginUser = getCurrentUser(request);
                String pooorigin = "", polorigin = "", destinationfd = "", destinationpod = "", chargeCode = "";
                String engmet = new String();
                LclBl lclBl = blDao.findById(costAndChargeForm.getFileNumberId());
                lclBl.setDocumentation(true);
                lclBl.setModifiedBy(loginUser);
                lclBl.setModifiedDatetime(d);
                blDao.saveOrUpdate(lclBl);
                chargeCode = BLUESCREEN_CHARGECODE_DOCUM;
                LclBlAc lclBlAc = new LclCostChargeDAO().findByBlChargeCode(costAndChargeForm.getFileNumberId(),
                        true, "LCLE", chargeCode);
                if (lclBlAc == null) {
                    lclBlAc = new LclBlAc();
                    lclBlAc = setInitialValues(lclBlAc, loginUser);
                    lclBlAc.setLclFileNumber(lclBl.getLclFileNumber());
                    lclBlAc.setAdjustmentAmount(BigDecimal.ZERO);
                    lclBlAc.setBundleIntoOf(false);
                    lclBlAc.setPrintOnBl(true);
                }
                List<LclBlPiece> lclBlPiecesList = new LclBLPieceDAO().findByProperty("lclFileNumber.id", costAndChargeForm.getFileNumberId());
                GlMapping glmapping = new GlMappingDAO().findByChargeCode(chargeCode, LCL_SHIPMENT_TYPE_EXPORT, TRANSACTION_TYPE_ACCOUNT_RECEIVABLE);
                lclBlAc.setArglMapping(glmapping);
                lclBlAc.setApglMapping(glmapping);
                lclBlAc.setManualEntry(true);
                lclBlAc.setModifiedBy(loginUser);
                lclBlAc.setModifiedDatetime(d);
                lclBlAc.setArBillToParty(costAndChargeForm.getBillingType());
                if (costAndChargeForm.getDocChargeAmount() != null
                        && costAndChargeForm.getDocChargeAmount().doubleValue() > 0.00) {
                    lclBlAc.setRatePerUnit(costAndChargeForm.getDocChargeAmount());
                    lclBlAc.setArAmount(costAndChargeForm.getDocChargeAmount());
                    lclBlAc.setRatePerUnitUom("FL");
                }
                //cost amount/costMinimum/costMeasure/costMeasure

                lclCostChargesDAO.saveOrUpdate(lclBlAc);
                List<LclBlAc> chargeList = blAcDAO.getLclCostByFileNumberAsc(costAndChargeForm.getFileNumberId());
                String rateType = "R".equalsIgnoreCase(lclBl.getRateType()) ? "Y" : lclBl.getRateType();
                RefTerminalDAO refterminaldao = new RefTerminalDAO();
                if (lclBl.getPortOfOrigin() != null) {
                    RefTerminal refterminal = refterminaldao.getTerminalByUnLocation(lclBl.getPortOfOrigin().getUnLocationCode(), rateType);
                    if (refterminal != null && refterminal.getTrmnum() != null && !refterminal.getTrmnum().trim().equals("")) {
                        pooorigin = refterminal.getTrmnum();
                    }
                }
                if (lclBl.getPortOfLoading() != null) {
                    RefTerminal refterminalpol = refterminaldao.getTerminalByUnLocation(lclBl.getPortOfLoading().getUnLocationCode(), rateType);
                    if (refterminalpol != null && refterminalpol.getTrmnum() != null && !refterminalpol.getTrmnum().trim().equals("")) {
                        polorigin = refterminalpol.getTrmnum();
                    }
                }
                PortsDAO portsDAO = new PortsDAO();
                if (lclBl.getPortOfDestination() != null) {
                    Ports portspod = portsDAO.getByProperty("unLocationCode", lclBl.getPortOfDestination().getUnLocationCode());
                    if (portspod != null && portspod.getEciportcode() != null && !portspod.getEciportcode().trim().equals("")) {
                        destinationpod = portspod.getEciportcode();
                    }
                }
                Ports ports = portsDAO.getByProperty("unLocationCode", lclBl.getFinalDestination().getUnLocationCode());
                if (ports != null && ports.getEciportcode() != null && !ports.getEciportcode().trim().equals("")) {
                    engmet = ports.getEngmet();
                    destinationfd = ports.getEciportcode();
                }

                LclBlAc cafBlAc = lclCostChargesDAO.manaualBlChargeValidate(costAndChargeForm.getFileNumberId(), "CAF", false);
                LclChargesCalculation lclChargesCalculation = new LclChargesCalculation();
                if (cafBlAc != null) {
                    lclChargesCalculation.calculateCAFChargeForBl(pooorigin, polorigin, destinationfd, destinationpod,
                            lclBlPiecesList, lclBl.getBillingType(), loginUser, costAndChargeForm.getFileNumberId(), request, ports, lclBl.getBillToParty());
                }
                if (!lclBl.getBillingType().equals("P")) {
                    request.setAttribute("lclBooking", lclBl.getLclFileNumber().getLclBooking()); //for Freight Agent Acct
                }
                request.setAttribute("lclBl", lclBl);
                blUtils.setWeighMeasureForBl(request, lclBlPiecesList, engmet);
                blUtils.setRolledUpChargesForBl(chargeList, request, costAndChargeForm.getFileNumberId(), blAcDAO, lclBlPiecesList,
                        lclBl.getBillingType(),
                        engmet, lclBl);
            }
        } catch (Exception e) {
            log.info("Error in addDocumCharge method. ", e);
            return mapping.findForward(CHARGE_DESC);
        }
        return mapping.findForward(CHARGE_DESC);
    }

    private LclBlAc setInitialValues(LclBlAc lclBlAc, User user) {
        lclBlAc.setTransDatetime(new Date());
        lclBlAc.setAdjustmentAmount(BigDecimal.ZERO);
        lclBlAc.setRatePerUnitUom("FL");
        lclBlAc.setRatePerWeightUnit(BigDecimal.ZERO);
        lclBlAc.setRatePerVolumeUnit(BigDecimal.ZERO);
        lclBlAc.setRateFlatMinimum(BigDecimal.ZERO);
        lclBlAc.setEnteredBy(user);
        lclBlAc.setEnteredDatetime(new Date());
        return lclBlAc;
    }
}
