/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.lcl.common.constant.ExportQuoteUtils;
import com.gp.cong.lcl.common.constant.ImportQuoteUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.lcl.common.constant.LclQuoteUtils;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.lcl.dwr.LclDwr;
import com.gp.cong.lcl.dwr.LclQuotationChargesCalculation;
import com.gp.cong.logisoft.domain.Ports;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclQuote;
import com.gp.cong.logisoft.domain.lcl.LclQuoteAc;
import com.gp.cong.logisoft.domain.lcl.LclQuoteDestinationServices;
import com.gp.cong.logisoft.domain.lcl.LclQuotePiece;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLQuoteDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuoteAcDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuotePieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO;
import com.gp.cvst.logisoft.domain.GlMapping;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LclQuoteCostAndChargeForm;
import com.logiware.common.dao.PropertyDAO;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

/**
 *
 * @author lakshh
 */
public class LclQuoteCostAndChargeAction extends LogiwareDispatchAction implements LclCommonConstant {

    private static final Logger log = Logger.getLogger(LclQuoteCostAndChargeAction.class);
    private static String CHARGE_DESC = "chargeDesc";

    public ActionForward display(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclQuoteCostAndChargeForm lclQuoteCostAndChargeForm = (LclQuoteCostAndChargeForm) form;
        request.setAttribute("fileNumber", request.getParameter("fileNumber"));
        if (CommonUtils.isNotEmpty(lclQuoteCostAndChargeForm.getDestination())) {
            PortsDAO portsDAO = new PortsDAO();
            Ports ports = portsDAO.getByProperty("unLocationCode", lclQuoteCostAndChargeForm.getDestination());
            if (ports != null && ports.getEciportcode() != null && !ports.getEciportcode().trim().equals("")) {
                request.setAttribute("engmet", ports.getEngmet());
            }
        }
        return mapping.findForward(SUCCESS);
    }

    public ActionForward addCharges(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            LclQuoteCostAndChargeForm lclQuoteCostAndChargeForm = (LclQuoteCostAndChargeForm) form;
            LclQuoteAcDAO lclQuoteAcDAO = new LclQuoteAcDAO();
            LclFileNumberDAO lclFileNumberDAO = new LclFileNumberDAO();
            Double totalWeight = 0.00;
            Double totalMeasure = 0.00;
            Double calculatedWeight = 0.00;
            Double calculatedMeasure = 0.00;
            String oldChargeCode = "";
            String shipmentType = "", pooTrmnum = "", polTrmnum = "", podEciPortCode = "", fdEciPortCode = "", fdEngmet = "";
            LclQuoteAc lclQuoteAc = lclQuoteCostAndChargeForm.getLclQuoteAc();
            LclUtils lclUtils = new LclUtils();
            if (CommonUtils.isNotEmpty(lclQuoteCostAndChargeForm.getFileNumberId())) {
                LclQuote lclQuote = new LCLQuoteDAO().findById(lclQuoteCostAndChargeForm.getFileNumberId());
                lclQuoteAc.setLclFileNumber(lclQuote.getLclFileNumber());
                List<LclQuotePiece> lclQuotePiecesList = new LclQuotePieceDAO().findByProperty("lclFileNumber.id", lclQuote.getFileNumberId());
                if ("E".equalsIgnoreCase(lclQuote.getQuoteType())) {
                    shipmentType = LclCommonConstant.LCL_SHIPMENT_TYPE_EXPORT;
                    String pooUnloCode = lclQuote.getPortOfOrigin() != null ? lclQuote.getPortOfOrigin().getUnLocationCode() : "";
                    String polUnloCode = lclQuote.getPortOfLoading() != null ? lclQuote.getPortOfLoading().getUnLocationCode() : "";
                    String podUnloCode = lclQuote.getPortOfDestination() != null ? lclQuote.getPortOfDestination().getUnLocationCode() : "";
                    String fdUnloCode = lclQuote.getFinalDestination() != null ? lclQuote.getFinalDestination().getUnLocationCode() : "";
                    String rateType = "R".equalsIgnoreCase(lclQuote.getRateType()) ? "Y" : lclQuote.getRateType();
                    List l = lclUtils.getTrmNumandEciPortCode(pooUnloCode, polUnloCode, podUnloCode, fdUnloCode, rateType);
                    for (Object row : l) {
                        Object[] col = (Object[]) row;
                        if (col[2].toString().equalsIgnoreCase("POO")) {
                            pooTrmnum = (String) col[0];
                        }
                        if (col[2].toString().equalsIgnoreCase("POL")) {
                            polTrmnum = (String) col[0];
                        }
                        if (col[2].toString().equalsIgnoreCase("POD")) {
                            podEciPortCode = (String) col[0];
                        }
                        if (col[2].toString().equalsIgnoreCase("FD")) {
                            fdEciPortCode = (String) col[0];
                            fdEngmet = (String) col[1];
                        }
                    }
                    if (CommonUtils.isNotEmpty(lclQuotePiecesList)) {
                        for (int j = 0; j < lclQuotePiecesList.size(); j++) {
                            LclQuotePiece lclQuotePiece = lclQuotePiecesList.get(j);
                            Double weightDouble = 0.00;
                            Double weightMeasure = 0.00;
                            if ("I".equalsIgnoreCase(lclQuote.getQuoteType())) {
                                fdEngmet = lclQuoteAc.getRateUom();
                                if (fdEngmet.equalsIgnoreCase("I")) {
                                    fdEngmet = "E";
                                }
                            }
                            if (fdEngmet != null && !"".equalsIgnoreCase(fdEngmet)) {
                                if (fdEngmet.equals("E")) {
                                    if (lclQuotePiece.getActualWeightImperial() != null && lclQuotePiece.getActualWeightImperial().doubleValue() != 0.00) {
                                        weightDouble = lclQuotePiece.getActualWeightImperial().doubleValue();
                                    } else if (lclQuotePiece.getBookedWeightImperial() != null && lclQuotePiece.getBookedWeightImperial().doubleValue() != 0.00) {
                                        weightDouble = lclQuotePiece.getBookedWeightImperial().doubleValue();
                                    }

                                    if (lclQuotePiece.getActualVolumeImperial() != null && lclQuotePiece.getActualVolumeImperial().doubleValue() != 0.00) {
                                        weightMeasure = lclQuotePiece.getActualVolumeImperial().doubleValue();
                                    } else if (lclQuotePiece.getBookedVolumeImperial() != null && lclQuotePiece.getBookedVolumeImperial().doubleValue() != 0.00) {
                                        weightMeasure = lclQuotePiece.getBookedVolumeImperial().doubleValue();
                                    }
                                } else if (fdEngmet.equals("M")) {
                                    if (lclQuotePiece.getActualWeightMetric() != null && lclQuotePiece.getActualWeightMetric().doubleValue() != 0.00) {
                                        weightDouble = lclQuotePiece.getActualWeightMetric().doubleValue();
                                    } else if (lclQuotePiece.getBookedWeightMetric() != null && lclQuotePiece.getBookedWeightMetric().doubleValue() != 0.00) {
                                        weightDouble = lclQuotePiece.getBookedWeightMetric().doubleValue();
                                    }
                                    if (lclQuotePiece.getActualVolumeMetric() != null && lclQuotePiece.getActualVolumeMetric().doubleValue() != 0.00) {
                                        weightMeasure = lclQuotePiece.getActualVolumeMetric().doubleValue();
                                    } else if (lclQuotePiece.getBookedVolumeMetric() != null && lclQuotePiece.getBookedVolumeMetric().doubleValue() != 0.00) {
                                        weightMeasure = lclQuotePiece.getBookedVolumeMetric().doubleValue();
                                    }
                                }//end of else if engmet
                            }//end of else null

                            //calculate the Total Weight Of Commodities
                            totalWeight = totalWeight + weightDouble;
                            //calculate the Total Measure Of Commodities
                            totalMeasure = totalMeasure + weightMeasure;
                        }//end of for loop
                    }
                } else {
                    shipmentType = LclCommonConstant.LCL_SHIPMENT_TYPE_IMPORT;
                }
                GlMappingDAO glMappingDAO = new GlMappingDAO();
                if (CommonUtils.isNotEmpty(lclQuoteCostAndChargeForm.getChargesCodeId())) {
                    oldChargeCode = (lclQuoteAc.getArglMapping() != null && lclQuoteAc.getArglMapping().getChargeCode() != null ? lclQuoteAc.getArglMapping().getChargeCode() : "");
                    if (!oldChargeCode.equalsIgnoreCase(lclQuoteCostAndChargeForm.getChargesCode())) {
                        GlMapping arGlmapping = glMappingDAO.findByChargeCode(lclQuoteCostAndChargeForm.getChargesCode(), shipmentType, ConstantsInterface.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE);
                        GlMapping apGlmapping = glMappingDAO.findByChargeCode(lclQuoteCostAndChargeForm.getChargesCode(), shipmentType, ConstantsInterface.TRANSACTION_TYPE_ACCRUALS);
                        if (arGlmapping != null) {
                            lclQuoteAc.setArglMapping(arGlmapping);
                        } else {
                            lclQuoteAc.setArglMapping(apGlmapping);
                        }
                    }
                    if (!oldChargeCode.equalsIgnoreCase(lclQuoteCostAndChargeForm.getChargesCode()) || (!lclQuoteCostAndChargeForm.getManualEntry() && CommonUtils.isNotEmpty(lclQuoteCostAndChargeForm.getApAmount()) && !lclQuoteCostAndChargeForm.getApAmount().equals("0.00") && lclQuoteAc.getApglMapping() == null)) {
                        GlMapping apGlmapping = glMappingDAO.findByChargeCode(lclQuoteCostAndChargeForm.getChargesCode(), shipmentType, ConstantsInterface.TRANSACTION_TYPE_ACCRUALS);
                        lclQuoteAc.setApglMapping(apGlmapping);
                    }
                }
                lclQuoteAc.setTransDatetime(new Date());
                lclQuoteAc.setEnteredBy(getCurrentUser(request));
                lclQuoteAc.setModifiedBy(getCurrentUser(request));
                lclQuoteAc.setEnteredDatetime(new Date());
                lclQuoteAc.setModifiedDatetime(new Date());
                if (lclQuoteCostAndChargeForm.getManualEntry()) {
                    lclQuoteAc.setManualEntry(true);
                    lclQuoteAc.setAdjustmentAmount(BigDecimal.ZERO);
                }
                lclQuoteAc.setArBillToParty(lclQuoteCostAndChargeForm.getBillToParty());

                if (lclQuoteCostAndChargeForm.getFlatRateAmount() != null && !lclQuoteCostAndChargeForm.getFlatRateAmount().trim().equals("")) {
                    lclQuoteAc.setRatePerUnit(new BigDecimal(lclQuoteCostAndChargeForm.getFlatRateAmount()));
                    lclQuoteAc.setArAmount(new BigDecimal(lclQuoteCostAndChargeForm.getFlatRateAmount()));
                    lclQuoteAc.setRatePerUnitUom("FL");
                } else {
                    if ("Imports".equalsIgnoreCase(lclQuoteCostAndChargeForm.getModuleName())) {
                        lclQuoteAc.setArAmount(lclQuoteCostAndChargeForm.getArAmount());
                    }
                    lclQuoteAc.setRatePerUnit(new BigDecimal(0.00));
                }
                if (!"Imports".equalsIgnoreCase(lclQuoteCostAndChargeForm.getModuleName()) && lclQuoteCostAndChargeForm.getWeight() != null && !lclQuoteCostAndChargeForm.getWeight().trim().equals("") && !lclQuoteCostAndChargeForm.getWeight().trim().equals("0.00")
                        && lclQuoteCostAndChargeForm.getMeasure() != null && !lclQuoteCostAndChargeForm.getMeasure().trim().equals("") && !lclQuoteCostAndChargeForm.getMeasure().trim().equals("0.00")
                        && lclQuoteCostAndChargeForm.getMinimum() != null && !lclQuoteCostAndChargeForm.getMinimum().trim().equals("") && !lclQuoteCostAndChargeForm.getMinimum().trim().equals("0.00")) {
                    lclQuoteAc.setRatePerWeightUnit(new BigDecimal(lclQuoteCostAndChargeForm.getWeight()));
                    lclQuoteAc.setRatePerVolumeUnit(new BigDecimal(lclQuoteCostAndChargeForm.getMeasure()));
                    lclQuoteAc.setRateFlatMinimum(new BigDecimal(lclQuoteCostAndChargeForm.getMinimum()));
                    if (fdEngmet != null && !"".equalsIgnoreCase(fdEngmet)) {
                        if (fdEngmet.equals("E")) {
                            if (lclQuoteAc.getRateUom().equalsIgnoreCase("M")) {
                                calculatedWeight = (totalWeight / 100) * lclUtils.convertToLbs(lclQuoteAc.getRatePerWeightUnit().doubleValue()).doubleValue();
                                calculatedMeasure = totalMeasure * lclUtils.convertToCft(lclQuoteAc.getRatePerVolumeUnit().doubleValue()).doubleValue();
                            } else {
                                calculatedWeight = (totalWeight / 100) * lclQuoteAc.getRatePerWeightUnit().doubleValue();
                                calculatedMeasure = totalMeasure * lclQuoteAc.getRatePerVolumeUnit().doubleValue();
                            }
                            lclQuoteAc.setRatePerWeightUnitDiv(new BigDecimal(100));
                        } else if (fdEngmet.equals("M")) {
                            if (lclQuoteAc.getRateUom().equalsIgnoreCase("I")) {
                                calculatedWeight = (totalWeight / 1000) * lclUtils.convertToKgs(lclQuoteAc.getRatePerWeightUnit().doubleValue()).doubleValue();
                                calculatedMeasure = totalMeasure * lclUtils.convertToCbm(lclQuoteAc.getRatePerVolumeUnit().doubleValue()).doubleValue();
                            } else {
                                calculatedWeight = (totalWeight / 1000) * lclQuoteAc.getRatePerWeightUnit().doubleValue();
                                calculatedMeasure = totalMeasure * lclQuoteAc.getRatePerVolumeUnit().doubleValue();
                            }
                            lclQuoteAc.setRatePerWeightUnitDiv(new BigDecimal(1000));
                        }
                    }//end of else if engmet
                    lclQuoteAc.setRatePerUnitDiv(lclQuoteAc.getRatePerWeightUnitDiv());
                    lclQuoteAc.setRatePerVolumeUnitDiv(new BigDecimal(1000));
                    if (calculatedWeight >= calculatedMeasure && calculatedWeight >= lclQuoteAc.getRateFlatMinimum().doubleValue()) {
                        lclQuoteAc.setArAmount(new BigDecimal(calculatedWeight));
                        lclQuoteAc.setRatePerUnitUom("W");
                        lclQuoteAc.setRatePerUnitDiv(lclQuoteAc.getRatePerVolumeUnitDiv());
                    } else if (calculatedMeasure >= calculatedWeight && calculatedMeasure >= lclQuoteAc.getRateFlatMinimum().doubleValue()) {
                        lclQuoteAc.setArAmount(new BigDecimal(calculatedMeasure));
                        lclQuoteAc.setRatePerUnitUom("V");
                        lclQuoteAc.setRatePerUnitDiv(lclQuoteAc.getRatePerVolumeUnitDiv());

                    } else {
                        lclQuoteAc.setArAmount(lclQuoteAc.getRateFlatMinimum());
                        lclQuoteAc.setRatePerUnitUom("M");
                    }
                } else {
                    lclQuoteAc.setRatePerWeightUnit(new BigDecimal(0.00));
                    lclQuoteAc.setRatePerVolumeUnit(new BigDecimal(0.00));
                    lclQuoteAc.setRateFlatMinimum(new BigDecimal(0.00));
                    if (null == lclQuoteAc.getArAmount()) {
                        lclQuoteAc.setArAmount(new BigDecimal(0.00));
                        lclQuoteAc.setRatePerUnitUom("FL");
                    }
                    if ("Imports".equalsIgnoreCase(lclQuoteCostAndChargeForm.getModuleName()) && !lclQuoteCostAndChargeForm.getManualEntry()) {
                        if (CommonUtils.isNotEmpty(lclQuoteCostAndChargeForm.getWeight()) && !"0.00".equalsIgnoreCase(lclQuoteCostAndChargeForm.getWeight())) {
                            lclQuoteAc.setRatePerWeightUnit(new BigDecimal(lclQuoteCostAndChargeForm.getWeight()));
                        }
                        if (CommonUtils.isNotEmpty(lclQuoteCostAndChargeForm.getMeasure()) && !"0.00".equalsIgnoreCase(lclQuoteCostAndChargeForm.getMeasure())) {
                            lclQuoteAc.setRatePerVolumeUnit(new BigDecimal(lclQuoteCostAndChargeForm.getMeasure()));
                        }
                        if (CommonUtils.isNotEmpty(lclQuoteCostAndChargeForm.getMinimum()) && !"0.00".equalsIgnoreCase(lclQuoteCostAndChargeForm.getMinimum())) {
                            lclQuoteAc.setRateFlatMinimum(new BigDecimal(lclQuoteCostAndChargeForm.getMinimum()));
                        }

                    }
                }
                lclQuoteAc.setApAmount(CommonUtils.isNotEmpty(lclQuoteCostAndChargeForm.getApAmount())
                        ? new BigDecimal(lclQuoteCostAndChargeForm.getApAmount()) : BigDecimal.ZERO);

                if (lclQuoteCostAndChargeForm.getApAmount() != null && !"".equalsIgnoreCase(lclQuoteCostAndChargeForm.getApAmount())
                        && !"0.00".equalsIgnoreCase(lclQuoteCostAndChargeForm.getApAmount())) {
                    lclQuoteAc.setApAmount(new BigDecimal(lclQuoteCostAndChargeForm.getApAmount()));
                    if (!"Imports".equalsIgnoreCase(lclQuoteCostAndChargeForm.getModuleName())) {
                        lclQuoteAc.setRatePerWeightUnit(BigDecimal.ZERO);
                        lclQuoteAc.setRatePerVolumeUnit(BigDecimal.ZERO);
                        lclQuoteAc.setRateFlatMinimum(BigDecimal.ZERO);
                    }
                } else if (!"Imports".equalsIgnoreCase(lclQuoteCostAndChargeForm.getModuleName()) && lclQuoteCostAndChargeForm.getWeightForCost() != null
                        && !lclQuoteCostAndChargeForm.getWeightForCost().trim().equals("") && !lclQuoteCostAndChargeForm.getWeightForCost().trim().equals("0.00")
                        && lclQuoteCostAndChargeForm.getMeasureForCost() != null && !lclQuoteCostAndChargeForm.getMeasureForCost().trim().equals("")
                        && !lclQuoteCostAndChargeForm.getMeasureForCost().trim().equals("0.00") && lclQuoteCostAndChargeForm.getMinimumForCost() != null
                        && !lclQuoteCostAndChargeForm.getMinimumForCost().trim().equals("") && !lclQuoteCostAndChargeForm.getMinimumForCost().trim().equals("0.00")) {
                    lclQuoteAc.setCostWeight(new BigDecimal(lclQuoteCostAndChargeForm.getWeightForCost()));
                    lclQuoteAc.setCostMeasure(new BigDecimal(lclQuoteCostAndChargeForm.getMeasureForCost()));
                    lclQuoteAc.setCostMinimum(new BigDecimal(lclQuoteCostAndChargeForm.getMinimumForCost()));
                    if (fdEngmet != null && !"".equalsIgnoreCase(fdEngmet)) {
                        if (fdEngmet.equals("E")) {
                            if (lclQuoteAc.getRateUom().equalsIgnoreCase("M")) {
                                calculatedWeight = (totalWeight / 100) * lclUtils.convertToLbs(lclQuoteAc.getCostWeight().doubleValue()).doubleValue();
                                calculatedMeasure = totalMeasure * lclUtils.convertToCft(lclQuoteAc.getCostMeasure().doubleValue()).doubleValue();
                            } else {
                                calculatedWeight = (totalWeight / 100) * lclQuoteAc.getCostWeight().doubleValue();
                                calculatedMeasure = totalMeasure * lclQuoteAc.getCostMeasure().doubleValue();
                            }
                        } else if (fdEngmet.equals("M")) {
                            if (lclQuoteAc.getRateUom().equalsIgnoreCase("I")) {
                                calculatedWeight = (totalWeight / 1000) * lclUtils.convertToKgs(lclQuoteAc.getCostWeight().doubleValue()).doubleValue();
                                calculatedMeasure = totalMeasure * lclUtils.convertToCbm(lclQuoteAc.getCostMeasure().doubleValue()).doubleValue();
                            } else {
                                calculatedWeight = (totalWeight / 1000) * lclQuoteAc.getCostWeight().doubleValue();
                                calculatedMeasure = totalMeasure * lclQuoteAc.getCostMeasure().doubleValue();
                            }
                        }
                    }//end of else if engmet
                    if (calculatedWeight >= calculatedMeasure && calculatedWeight >= lclQuoteAc.getCostMinimum().doubleValue()) {
                        lclQuoteAc.setApAmount(new BigDecimal(calculatedWeight));
                    } else if (calculatedMeasure >= calculatedWeight && calculatedMeasure >= lclQuoteAc.getCostMinimum().doubleValue()) {
                        lclQuoteAc.setApAmount(new BigDecimal(calculatedMeasure));

                    } else {
                        lclQuoteAc.setApAmount(lclQuoteAc.getCostMinimum());
                    }
                }
                lclQuoteAc.setBundleIntoOf(false);
                lclQuoteAc.setPrintOnBl(true);
                lclQuoteAc.setCostFlatrateAmount(CommonUtils.isNotEmpty(lclQuoteCostAndChargeForm.getApAmount())
                        ? new BigDecimal(lclQuoteCostAndChargeForm.getApAmount()) : BigDecimal.ZERO);

                lclQuoteAcDAO.saveOrUpdate(lclQuoteAc);
                List<LclQuoteAc> chargeList = lclQuoteAcDAO.getLclCostByFileNumberAsc(lclQuoteCostAndChargeForm.getFileNumberId(), lclQuoteCostAndChargeForm.getModuleName());
                if ("Imports".equalsIgnoreCase(lclQuoteCostAndChargeForm.getModuleName())) {
                    ImportQuoteUtils importQuoteUtils = new ImportQuoteUtils();
                    importQuoteUtils.setWeighMeasureForImpQuote(request, lclQuotePiecesList);
                    importQuoteUtils.setImpRolledUpChargesForQuote(chargeList, request, lclQuoteCostAndChargeForm.getFileNumberId(), lclQuotePiecesList, lclQuote.getBillingType());
                } else {
                    ExportQuoteUtils exportQuoteUtils = new ExportQuoteUtils();
                    LclQuotationChargesCalculation lclQuotationChargesCalculation = new LclQuotationChargesCalculation();
                    lclQuoteAc = lclQuoteAcDAO.manaualChargeValidate(lclQuoteCostAndChargeForm.getFileNumberId(), "CAF", false);
                    if (lclQuoteAc != null) {
                        lclQuotationChargesCalculation.calculateCAFChargeForRadio(pooTrmnum, polTrmnum, fdEciPortCode, podEciPortCode, lclQuotePiecesList, lclQuote.getBillingType(),
                                getCurrentUser(request), lclQuoteCostAndChargeForm.getFileNumberId(), lclQuote.getFinalDestination().getUnLocationCode(), fdEngmet);
                    }
                    exportQuoteUtils.setWeighMeasureForQuote(request, lclQuotePiecesList, fdEngmet);
                    exportQuoteUtils.setRolledUpChargesForQuote(chargeList, request, lclQuoteCostAndChargeForm.getFileNumberId(), lclQuoteAcDAO, lclQuotePiecesList, request.getParameter("billingType"), fdEngmet, "No");
                }
                request.setAttribute("lclCommodityList", lclQuotePiecesList);
                request.setAttribute("lclQuote", lclQuote);
            }
        }//end of try block
        catch (Exception e) {
            log.info("Error in addCharges method. ", e);
            return mapping.findForward(CHARGE_DESC);
        }//emd of catch block
        return mapping.findForward(CHARGE_DESC);
    }

    public ActionForward displayDocum(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclQuoteCostAndChargeForm lclQuoteCostAndChargeForm = (LclQuoteCostAndChargeForm) form;
        request.setAttribute("fileNumber", request.getParameter("fileNumber"));
        request.setAttribute("buttonValue", request.getParameter("buttonValue"));
        request.setAttribute("destination", lclQuoteCostAndChargeForm.getDestination());
        return mapping.findForward("display");
    }

    public ActionForward addQuoteDocumCharge(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            LclQuoteCostAndChargeForm lclQuoteCostAndChargeForm = (LclQuoteCostAndChargeForm) form;
            ExportQuoteUtils exportQuoteUtils = new ExportQuoteUtils();
            LclQuoteAcDAO lclQuoteAcDAO = new LclQuoteAcDAO();
            LCLQuoteDAO lclQuoteDAO = new LCLQuoteDAO();
            User loginUser = getCurrentUser(request);
            Date now = new Date();
            String fileId = request.getParameter("fileNumberId");
            Long fileNumberId = Long.parseLong(fileId);
            LclQuoteAc lclQuoteAc = lclQuoteAcDAO.getLclQuoteAcByChargeCode(fileNumberId, "DOCUM");
            if (lclQuoteAc == null) {
                lclQuoteAc = new LclQuoteAc();
                lclQuoteAc.setEnteredBy(loginUser);
                lclQuoteAc.setEnteredDatetime(now);
            }
            LclQuote lclQuote = lclQuoteDAO.findById(fileNumberId);
            lclQuoteAc.setLclFileNumber(lclQuote.getLclFileNumber());
            lclQuote.setDocumentation(true);
            lclQuoteDAO.saveOrUpdate(lclQuote);
            GlMapping glmapping = new GlMappingDAO().findByChargeCode("DOCUM", "LCLE", "AR");
            lclQuoteAc.setArglMapping(glmapping);
            lclQuoteAc.setApglMapping(glmapping);
            lclQuoteAc.setTransDatetime(now);
            lclQuoteAc.setModifiedBy(loginUser);
            lclQuoteAc.setModifiedDatetime(now);
            lclQuoteAc.setManualEntry(true);
            lclQuoteAc.setAdjustmentAmount(BigDecimal.ZERO);
            if (lclQuoteCostAndChargeForm.getDollarAmount() != null && lclQuoteCostAndChargeForm.getDollarAmount().doubleValue() > 0.00) {
                lclQuoteAc.setRatePerUnit(lclQuoteCostAndChargeForm.getDollarAmount());
                lclQuoteAc.setArAmount(lclQuoteAc.getRatePerUnit());
                lclQuoteAc.setRatePerUnitUom("FL");
            }
            lclQuoteAc.setBundleIntoOf(false);
            lclQuoteAc.setPrintOnBl(true);
            lclQuoteAcDAO.saveOrUpdate(lclQuoteAc);
            List<LclQuotePiece> lclQuotePiecesList = new LclQuotePieceDAO().findByProperty("lclFileNumber.id", fileNumberId);
            List<LclQuoteAc> chargeList = lclQuoteAcDAO.getLclCostByFileNumberAsc(fileNumberId, LCL_EXPORT);
            exportQuoteUtils.setExpChargesDetails(lclQuote, lclQuotePiecesList, chargeList, loginUser, true, request);//set Export Rate Details
        }//end of try block
        catch (Exception e) {
            log.info("Error in addCharges method. ", e);
            return mapping.findForward(CHARGE_DESC);
        }//emd of catch block
        return mapping.findForward(CHARGE_DESC);
    }

    public ActionForward deleteQuoteCharge(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {//remove this methods
            LclQuoteCostAndChargeForm lclQuoteCostAndChargeForm = (LclQuoteCostAndChargeForm) form;
            ExportQuoteUtils exportQuoteUtils = new ExportQuoteUtils();
            LclQuoteAcDAO lclQuoteAcDAO = new LclQuoteAcDAO();
            String fdUnloCode = lclQuoteCostAndChargeForm.getDestination() != null ? lclQuoteCostAndChargeForm.getDestination() : "";
            List l = new LclUtils().getTrmNumandEciPortCode("", "", "", fdUnloCode, "");
            String fdEngmet = "";
            for (Object row : l) {
                Object[] col = (Object[]) row;
                if (col[2].toString().equalsIgnoreCase("FD")) {
                    fdEngmet = (String) col[1];
                }
            }
            String id = request.getParameter("id");
            if (CommonUtils.isNotEmpty(id)) {
                LclQuoteAc lclQuoteAc = lclQuoteAcDAO.findById(Long.parseLong(id));
                lclQuoteCostAndChargeForm.setFileNumberId(lclQuoteAc.getLclFileNumber().getId());
                lclQuoteAcDAO.delete(lclQuoteAc);
            }
            List<LclQuoteAc> chargeList = lclQuoteAcDAO.getLclCostByFileNumberAsc(lclQuoteCostAndChargeForm.getFileNumberId(), LCL_EXPORT);
            List<LclQuotePiece> lclQuotePiecesList = new LclQuotePieceDAO().findByProperty("lclFileNumber.id", lclQuoteCostAndChargeForm.getFileNumberId());
            exportQuoteUtils.setRolledUpChargesForQuote(chargeList, request, lclQuoteCostAndChargeForm.getFileNumberId(), lclQuoteAcDAO, lclQuotePiecesList, lclQuoteCostAndChargeForm.getBillingType(), fdEngmet, "No");
        }//end of try block
        catch (Exception e) {
            log.info("Error in deleteQuoteCharge method. " + new Date(), e);
            return mapping.findForward(CHARGE_DESC);
        }//emd of catch block
        return mapping.findForward(CHARGE_DESC);
    }

    public ActionForward editQuoteCharge(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            LclQuoteCostAndChargeForm lclQuoteCostAndChargeForm = (LclQuoteCostAndChargeForm) form;
            LclDwr lclDwr = new LclDwr();
            LclQuoteAc lclQuoteAc = new LclQuoteAcDAO().findById(lclQuoteCostAndChargeForm.getLclQuoteAc().getId());
            lclQuoteCostAndChargeForm.setLclQuoteAc(lclQuoteAc);
            lclQuoteCostAndChargeForm.setFileNumberId(lclQuoteAc.getLclFileNumber().getId());
            lclQuoteCostAndChargeForm.setChargesCode(lclQuoteAc.getArglMapping().getChargeCode());
            lclQuoteCostAndChargeForm.setChargesCodeId(lclQuoteAc.getArglMapping().getId());
            if (lclQuoteAc.getRatePerUnit() != null) {
                lclQuoteCostAndChargeForm.setFlatRateAmount(NumberUtils.convertToTwoDecimal(lclQuoteAc.getRatePerUnit().doubleValue()));
            }
            lclQuoteCostAndChargeForm.setApAmount(null != lclQuoteAc.getCostFlatrateAmount() ? NumberUtils.convertToTwoDecimal(lclQuoteAc.getCostFlatrateAmount().doubleValue()) : "");
            if (lclQuoteAc.getApAmount() != null) {
                lclQuoteCostAndChargeForm.setCostAmount(NumberUtils.convertToTwoDecimal(lclQuoteAc.getApAmount().doubleValue()));
            }
            if (lclQuoteAc.getArAmount() != null) {
                lclQuoteCostAndChargeForm.setArAmount(lclQuoteAc.getArAmount());
                //lclQuoteCostAndChargeForm.setFlatRateAmount(NumberUtils.convertToTwoDecimal(lclQuoteAc.getArAmount().doubleValue()));
            }
            if (lclQuoteAc.getRatePerWeightUnit() != null) {
                lclQuoteCostAndChargeForm.setWeight(NumberUtils.convertToTwoDecimal(lclQuoteAc.getRatePerWeightUnit().doubleValue()));
            }
            if (lclQuoteAc.getRatePerVolumeUnit() != null) {
                lclQuoteCostAndChargeForm.setMeasure(NumberUtils.convertToTwoDecimal(lclQuoteAc.getRatePerVolumeUnit().doubleValue()));
            }
            if (lclQuoteAc.getRateFlatMinimum() != null) {
                lclQuoteCostAndChargeForm.setMinimum(NumberUtils.convertToTwoDecimal(lclQuoteAc.getRateFlatMinimum().doubleValue()));
            }
            if (lclQuoteAc.getCostWeight() != null && !lclQuoteAc.getCostWeight().toString().trim().equals("")) {
                lclQuoteCostAndChargeForm.setWeightForCost(NumberUtils.convertToTwoDecimal(lclQuoteAc.getCostWeight().doubleValue()));
            }
            if (lclQuoteAc.getCostMeasure() != null) {
                lclQuoteCostAndChargeForm.setMeasureForCost(NumberUtils.convertToTwoDecimal(lclQuoteAc.getCostMeasure().doubleValue()));
            }
            if (lclQuoteAc.getCostMinimum() != null) {
                lclQuoteCostAndChargeForm.setMinimumForCost(NumberUtils.convertToTwoDecimal(lclQuoteAc.getCostMinimum().doubleValue()));
            }
            String shipmentType = lclQuoteCostAndChargeForm.getModuleName().equalsIgnoreCase("Exports") ? "LCLE" : "LCLI";
            String[] validateCode = lclDwr.chargeCostValidate(lclQuoteAc.getArglMapping().getChargeCode(), shipmentType);
            if (validateCode[1].equalsIgnoreCase("N")) {
                request.setAttribute("disableCost", "disableCostButton");
            }
            lclQuoteCostAndChargeForm.setManualEntry(lclQuoteAc.isManualEntry());
            lclQuoteCostAndChargeForm.setBillToParty(lclQuoteAc.getArBillToParty());
            request.setAttribute("lclQuoteAc", lclQuoteAc);
            request.setAttribute("lclQuoteCostAndChargeForm", lclQuoteCostAndChargeForm);
            request.setAttribute("chargeCode", lclQuoteAc.getArglMapping().getChargeCode());
            request.setAttribute("fileNumber", request.getParameter("fileNumber"));
            request.setAttribute("quoteAcId", lclQuoteCostAndChargeForm.getLclQuoteAc().getId());
            if (lclQuoteAc.getCostMinimum() != null && !lclQuoteAc.getCostMinimum().toString().equals("0.00")) {
                lclQuoteCostAndChargeForm.setCostAmount("0.00");
                lclQuoteCostAndChargeForm.setMinimumForCost(NumberUtils.convertToTwoDecimal(lclQuoteAc.getCostMinimum().doubleValue()));
            }
            if (lclQuoteAc.getArAmount() != null && !lclQuoteAc.getArAmount().toString().equals("0.00")) {
                request.setAttribute("buttonValue", "addCharge");
            } else {
                request.setAttribute("buttonValue", "addCost");
            }
        }//end of try block
        catch (Exception e) {
            log.info("Error in editQuoteCharge method. " + new Date(), e);
            return mapping.findForward(SUCCESS);
        }//emd of catch block
        return mapping.findForward(SUCCESS);
    }

    public ActionForward addSpotRate(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            LclQuoteCostAndChargeForm chargeForm = (LclQuoteCostAndChargeForm) form;
            LclQuoteAcDAO lclQuoteAcDAO = new LclQuoteAcDAO();
            String billingType = request.getParameter("billingType");
            String CFT = chargeForm.getRate();
            String CBM = chargeForm.getRateN();
            Boolean spotCheckBottom = chargeForm.getSpotCheckBottom();
            Boolean isOnlyOcnfrt = chargeForm.getSpotCheckOF();
            String spotComment = chargeForm.getComment().toUpperCase();
            LCLQuoteDAO quoteDAO = new LCLQuoteDAO();
            LclQuote lclQuote = quoteDAO.findById(chargeForm.getFileNumberId());
            quoteDAO.getCurrentSession().evict(lclQuote);
            String fdEngmet = "";
            String fdUnloCode = lclQuote.getFinalDestination() != null ? lclQuote.getFinalDestination().getUnLocationCode() : "";
            List l = new LclUtils().getTrmNumandEciPortCode("", "", "", fdUnloCode, "");
            for (Object row : l) {
                Object[] col = (Object[]) row;
                if (col[2].toString().equalsIgnoreCase("FD")) {
                    fdEngmet = (String) col[1];
                }
            }
            List<LclQuotePiece> lclQuotePiecesList = new LclQuotePieceDAO()
                    .findByProperty("lclFileNumber.id", chargeForm.getFileNumberId());
            MessageResources messageResources = getResources(request);
            String spotRateCommodity = messageResources.getMessage("application.spotRate.commodityCode");
            new ExportQuoteUtils().calculateSpotRate(chargeForm.getFileNumberId(), lclQuote, billingType,
                    CBM, CFT, isOnlyOcnfrt, spotCheckBottom, spotComment, spotRateCommodity, request, lclQuotePiecesList);
            request.setAttribute("lclQuote", lclQuote);
            refreshRates(chargeForm.getFileNumberId(), billingType, fdEngmet, lclQuotePiecesList, lclQuoteAcDAO, request);
        } catch (Exception e) {
            return mapping.findForward(CHARGE_DESC);
        }

        return mapping.findForward(CHARGE_DESC);
    }

    private void refreshRates(Long fileNumberId, String billingType, String fdEngmet,
            List<LclQuotePiece> lclQuotePiecesList, LclQuoteAcDAO lclQuoteAcDAO,
            HttpServletRequest request) throws Exception {
        List<LclQuoteAc> chargeList = lclQuoteAcDAO.getLclCostByFileNumberAsc(fileNumberId, LCL_EXPORT);
        ExportQuoteUtils exportQuoteUtils = new ExportQuoteUtils();
        exportQuoteUtils.setWeighMeasureForQuote(request, lclQuotePiecesList, fdEngmet);
        exportQuoteUtils.setRolledUpChargesForQuote(chargeList, request, fileNumberId, lclQuoteAcDAO, lclQuotePiecesList, billingType, fdEngmet, "No");
    }

    public ActionForward refreshCommodity(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fileNumberId = request.getParameter("fileNumberId");
        request.setAttribute("lclCommodityList", new LclQuotePieceDAO().findByProperty("lclFileNumber.id", Long.parseLong(fileNumberId)));
        LCLQuoteDAO lclQuoteDAO = new LCLQuoteDAO();
        LclQuote lclQuote = lclQuoteDAO.getByProperty("lclFileNumber.id", Long.parseLong(fileNumberId));
        request.setAttribute("ofspotrate", lclQuote.getSpotRate());
        return mapping.findForward(COMMODITY_DESC);
    }
    // Adding for Destination Services  for Lcl Exports

    public ActionForward displayQuoteDestinationServices(ActionMapping mapping,
            ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclQuoteCostAndChargeForm chargeForm = (LclQuoteCostAndChargeForm) form;
        PropertyDAO property = new PropertyDAO();
        String buttonValue = null != request.getParameter("buttonValue") ? request.getParameter("buttonValue") : "";
        request.setAttribute("buttonValue", buttonValue);
        if ("editService".equalsIgnoreCase(buttonValue)) {
            LclQuoteAc lclQuoteAc = new LclQuoteAcDAO().findById(chargeForm.getId());
            if (null != lclQuoteAc) {
                chargeForm.setLclQuoteAc(lclQuoteAc);
                chargeForm.setFileNumberId(lclQuoteAc.getLclFileNumber().getId());
                chargeForm.setChargesCode(lclQuoteAc.getArglMapping().getChargeCode());
                chargeForm.setChargesCodeId(lclQuoteAc.getArglMapping().getId());
                chargeForm.setBillToParty(lclQuoteAc.getArBillToParty());
                chargeForm.setModuleName(LCL_EXPORT);
                chargeForm.setId(lclQuoteAc.getId());

                if (lclQuoteAc.getRatePerUnit() != null) {
                    chargeForm.setFlatRateAmount(NumberUtils.convertToTwoDecimal(lclQuoteAc.getRatePerUnit().doubleValue()));
                }
                if (lclQuoteAc.getCostFlatrateAmount() != null) {
                    chargeForm.setCostAmount(NumberUtils.convertToTwoDecimal(lclQuoteAc.getCostFlatrateAmount().doubleValue()));
                }
                if (lclQuoteAc.getRatePerWeightUnit() != null) {
                    chargeForm.setWeight(NumberUtils.convertToTwoDecimal(lclQuoteAc.getRatePerWeightUnit().doubleValue()));
                }
                if (lclQuoteAc.getRatePerVolumeUnit() != null) {
                    chargeForm.setMeasure(NumberUtils.convertToTwoDecimal(lclQuoteAc.getRatePerVolumeUnit().doubleValue()));
                }
                if (lclQuoteAc.getArAmount() != null) {
                    chargeForm.setArAmount(lclQuoteAc.getArAmount());
                }
                if (lclQuoteAc.getRateFlatMinimum() != null) {
                    chargeForm.setMinimum(NumberUtils.convertToTwoDecimal(lclQuoteAc.getRateFlatMinimum().doubleValue()));
                }
                if (lclQuoteAc.getCostWeight() != null && !lclQuoteAc.getCostWeight().toString().trim().equals("")) {
                    chargeForm.setWeightForCost(NumberUtils.convertToTwoDecimal(lclQuoteAc.getCostWeight().doubleValue()));
                }
                if (lclQuoteAc.getCostMeasure() != null) {
                    chargeForm.setMeasureForCost(NumberUtils.convertToTwoDecimal(lclQuoteAc.getCostMeasure().doubleValue()));
                }
                if (lclQuoteAc.getCostMinimum() != null) {
                    chargeForm.setMinimumForCost(NumberUtils.convertToTwoDecimal(lclQuoteAc.getCostMinimum().doubleValue()));
                }
                if (null != lclQuoteAc.getLclFileNumber().getLclQuote()
                        && lclQuoteAc.getArglMapping().getChargeCode().equalsIgnoreCase("ONCARR")) {
                    chargeForm.setTotaldestCarriageTT(lclQuoteAc.getLclFileNumber().getLclQuote().getPodfdtt());
                }
                LclQuoteDestinationServices destService = new LclQuoteAcDAO()
                        .getDestinationCharge(lclQuoteAc.getId(), lclQuoteAc.getLclFileNumber().getId());
                if (null != destService) {
                    chargeForm.setCityName(destService.getCity());
                    if (destService.getOncarriageAcctNo() != null) {
                        chargeForm.setAlternateAgentAccntNo(destService.getOncarriageAcctNo().getAccountno());
                        chargeForm.setAlternateAgent(destService.getOncarriageAcctNo().getAccountName());
                    }
                    chargeForm.setTotalDestTT(destService.getOncarriageTt());
                    chargeForm.setDestFrequency(destService.getOncarriageTtFreq());
                    chargeForm.setDestinatnSersRemark(destService.getRemarks());
                }
                request.setAttribute("lclQuoteAc", lclQuoteAc);
                request.setAttribute("chargeCode", lclQuoteAc.getArglMapping().getChargeCode());
            }
        } else if ("deleteService".equalsIgnoreCase(buttonValue)) {
            deleteDestinationCharge(chargeForm, request);
        }
        String destination = null != request.getParameter("destination") ? request.getParameter("destination") : chargeForm.getDestination();
        Ports ports = new PortsDAO().getByProperty("unLocationCode", destination);
        List<LclQuotePiece> piecesList = new LclQuotePieceDAO()
                .findByProperty("lclFileNumber.id", chargeForm.getFileNumberId());
        List<LclQuoteAc> chargeList = new LclQuoteAcDAO()
                .getQTAllDestinationChargeList(chargeForm.getFileNumberId());
        new ExportQuoteUtils().setRolledUpChargesForQuote(chargeList, request, chargeForm.getFileNumberId(), new LclQuoteAcDAO(),
                piecesList, null, ports.getEngmet(), "No");

        request.setAttribute("profit", property.getProperty("Destination Services DAP/DDP/Delivery Profit (%)"));
        request.setAttribute("sellMin", property.getProperty("Destination Services DAP/DDP/Delivery min profit"));
        request.setAttribute("sellMax", property.getProperty("Destination Services DAP/DDP/Delivery max profit"));
        request.setAttribute("oc_min_profit", property.getProperty("Destination Services O/C Min Profit"));
        request.setAttribute("oc_max_profit", property.getProperty("Destination Services O/C Max Profit"));
        request.setAttribute("onCarrigeMarkUp", property.getProperty("Destination Services OnCarriage W/M Markup"));
        request.setAttribute("DTHC_currency_adj", property.getProperty("Destination Services DTHC Currency adjustment (%)"));
        request.setAttribute("lclQuoteCostAndChargeForm", chargeForm);
        request.setAttribute("chargeCodeList", new GlMappingDAO().getchargeCodeListForDestinationServices());
        request.setAttribute("fileNumber", request.getParameter("fileNumber"));
        return mapping.findForward("displayDestinationServices");
    }

    public void setQuoteDestinationServices(LclQuoteCostAndChargeForm chargeForm,
            LclQuoteAc lclQuoteAc, HttpServletRequest request) throws Exception {
        BaseHibernateDAO<LclQuoteDestinationServices> destinationDAO = new BaseHibernateDAO<LclQuoteDestinationServices>();
        LclQuoteDestinationServices destinationServices = null;
        User user = getCurrentUser(request);
        if (chargeForm.getLclQuoteAc() != null) {
            destinationServices = new LclQuoteAcDAO()
                    .getDestinationCharge(lclQuoteAc.getId(), lclQuoteAc.getLclFileNumber().getId());
            if (destinationServices == null) {
                destinationServices = new LclQuoteDestinationServices();
                destinationServices.setEnteredBy(user);
                destinationServices.setEnteredDatetime(new Date());
            }
            destinationServices.setLclFileNumber(lclQuoteAc.getLclFileNumber());
            destinationServices.setLclquoteAc(lclQuoteAc);
            destinationServices.setCity("".equalsIgnoreCase(chargeForm.getCityName()) ? chargeForm.getManualCityName() : chargeForm.getCityName());
            destinationServices.setCountry("".equalsIgnoreCase(chargeForm.getCityName()) ? chargeForm.getManualCityName() : chargeForm.getCityName());
            if (CommonUtils.isNotEmpty(chargeForm.getAlternateAgentAccntNo())) {
                destinationServices.setOncarriageAcctNo(new TradingPartnerDAO().findById(chargeForm.getAlternateAgentAccntNo()));
            }
            destinationServices.setOncarriageTtFreq(CommonUtils.isNotEmpty(chargeForm.getDestFrequency()) ? chargeForm.getDestFrequency() : 0);
            destinationServices.setOncarriageTt(chargeForm.getTotalDestTT());
            destinationServices.setRemarks(CommonUtils.isNotEmpty(chargeForm.getDestinatnSersRemark()) ? chargeForm.getDestinatnSersRemark() : "");
            destinationServices.setModifiedBy(user);
            destinationServices.setModifiedDatetime(new Date());
            destinationDAO.saveOrUpdate(destinationServices);
        }
    }

    public ActionForward addManualChargesForExport(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            LclQuoteCostAndChargeForm chargeForm = (LclQuoteCostAndChargeForm) form;
            LclQuoteAcDAO chargeDAO = new LclQuoteAcDAO();
            LclQuoteAc lclQuoteAc = chargeForm.getLclQuoteAc();
            GlMappingDAO glMappingDAO = new GlMappingDAO();
            LclUtils lclUtils = new LclUtils();
            Double totalWeight = 0.00, totalMeasure = 0.00, calculatedWeight = 0.00, calculatedMeasure = 0.00;
            String pooTrmnum = "", polTrmnum = "", podEciPortCode = "", fdEciPortCode = "", fdEngmet = "";
            List<LclQuotePiece> quotePiecesList = new LclQuotePieceDAO().findByProperty("lclFileNumber.id", chargeForm.getFileNumberId());
            LclQuote quote = new LCLQuoteDAO().findById(chargeForm.getFileNumberId());
            String pooUnloCode = quote.getPortOfOrigin() != null ? quote.getPortOfOrigin().getUnLocationCode() : "";
            String polUnloCode = quote.getPortOfLoading() != null ? quote.getPortOfLoading().getUnLocationCode() : "";
            String podUnloCode = quote.getPortOfDestination() != null ? quote.getPortOfDestination().getUnLocationCode() : "";
            String fdUnloCode = quote.getFinalDestination() != null ? quote.getFinalDestination().getUnLocationCode() : "";
            String rateType = "R".equalsIgnoreCase(quote.getRateType()) ? "Y" : quote.getRateType();
            List l = new LclUtils().getTrmNumandEciPortCode(pooUnloCode, polUnloCode, podUnloCode, fdUnloCode, rateType);
            for (Object row : l) {
                Object[] col = (Object[]) row;
                if (col[2].toString().equalsIgnoreCase("POO")) {
                    pooTrmnum = (String) col[0];
                }
                if (col[2].toString().equalsIgnoreCase("POL")) {
                    polTrmnum = (String) col[0];
                }
                if (col[2].toString().equalsIgnoreCase("POD")) {
                    podEciPortCode = (String) col[0];
                }
                if (col[2].toString().equalsIgnoreCase("FD")) {
                    fdEciPortCode = (String) col[0];
                    fdEngmet = col[1] != null ? (String) col[1] : "";
                }
            }

            if (CommonUtils.isNotEmpty(chargeForm.getFileNumberId())) {
                lclQuoteAc.setLclFileNumber(quote.getLclFileNumber());
            }
            GlMapping arGlmapping = glMappingDAO.findByChargeCode(chargeForm.getChargesCode(),
                    LCL_SHIPMENT_TYPE_EXPORT, ConstantsInterface.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE);
            GlMapping apGlmapping = glMappingDAO.findByChargeCode(chargeForm.getChargesCode(),
                    LCL_SHIPMENT_TYPE_EXPORT, ConstantsInterface.TRANSACTION_TYPE_ACCRUALS);
            if (CommonUtils.in(chargeForm.getChargesCode(), "DTHC PREPAID", "ONCARR", "DELIV", "DDP", "DAP")) {
                arGlmapping = glMappingDAO.findByDestinationChargeCode(chargeForm.getChargesCode(),
                        LCL_SHIPMENT_TYPE_EXPORT, ConstantsInterface.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE);
                apGlmapping = glMappingDAO.findByDestinationChargeCode(chargeForm.getChargesCode(),
                        LCL_SHIPMENT_TYPE_EXPORT, ConstantsInterface.TRANSACTION_TYPE_ACCRUALS);
            }

            lclQuoteAc.setApglMapping(apGlmapping);
            if (arGlmapping != null) {
                lclQuoteAc.setArglMapping(arGlmapping);
            } else {
                lclQuoteAc.setArglMapping(apGlmapping);
            }

            lclQuoteAc.setArBillToParty(null);
            lclQuoteAc.setApBillToParty(null);
            lclQuoteAc.setTransDatetime(new Date());
            lclQuoteAc.setEnteredBy(getCurrentUser(request));
            lclQuoteAc.setModifiedBy(getCurrentUser(request));
            lclQuoteAc.setEnteredDatetime(new Date());
            lclQuoteAc.setModifiedDatetime(new Date());
            lclQuoteAc.setManualEntry(true);
            lclQuoteAc.setAdjustmentAmount(BigDecimal.ZERO);

            // calculation starts here for cost and charge .
            lclQuoteAc.setRatePerWeightUnit(CommonUtils.isNotEmpty(chargeForm.getWeight())
                    ? new BigDecimal(chargeForm.getWeight()) : BigDecimal.ZERO);
            lclQuoteAc.setRatePerVolumeUnit(CommonUtils.isNotEmpty(chargeForm.getMeasure())
                    ? new BigDecimal(chargeForm.getMeasure()) : BigDecimal.ZERO);

            if (lclQuoteAc.getRatePerWeightUnit().doubleValue() > 0.00 || lclQuoteAc.getRatePerVolumeUnit().doubleValue() > 0.00) {
                if (CommonUtils.isNotEmpty(quotePiecesList)) {
                    for (LclQuotePiece piece : quotePiecesList) {
                        Double weightDouble = 0.00;
                        Double weightMeasure = 0.00;
                        if (null != lclQuoteAc.getRateUom()) {
                            if (("I").equalsIgnoreCase(lclQuoteAc.getRateUom())) {
                                if (piece.getBookedWeightImperial() != null && piece.getBookedWeightImperial().doubleValue() != 0.00) {
                                    weightDouble = piece.getBookedWeightImperial().doubleValue();
                                }
                                if (piece.getBookedVolumeImperial() != null && piece.getBookedVolumeImperial().doubleValue() != 0.00) {
                                    weightMeasure = piece.getBookedVolumeImperial().doubleValue();
                                }
                            } else if (("M").equalsIgnoreCase(lclQuoteAc.getRateUom())) {
                                if (piece.getBookedWeightMetric() != null && piece.getBookedWeightMetric().doubleValue() != 0.00) {
                                    weightDouble = piece.getBookedWeightMetric().doubleValue();//kgs
                                }
                                if (piece.getBookedVolumeMetric() != null && piece.getBookedVolumeMetric().doubleValue() != 0.00) {
                                    weightMeasure = piece.getBookedVolumeMetric().doubleValue();//cbm
                                }
                            }
                            totalWeight = totalWeight + weightDouble;
                            totalMeasure = totalMeasure + weightMeasure;
                        }
                    }
                }

                if (fdEngmet.equals("E")) {
                    if (lclQuoteAc.getRateUom().equalsIgnoreCase("M")) {
                        calculatedWeight = (totalWeight / 100) * lclUtils.convertToLbs(lclQuoteAc.getRatePerWeightUnit().doubleValue()).doubleValue();
                        calculatedMeasure = totalMeasure * lclUtils.convertToCft(lclQuoteAc.getRatePerVolumeUnit().doubleValue()).doubleValue();
                    } else {
                        calculatedWeight = (totalWeight / 100) * lclQuoteAc.getRatePerWeightUnit().doubleValue();
                        calculatedMeasure = totalMeasure * lclQuoteAc.getRatePerVolumeUnit().doubleValue();
                    }
                    lclQuoteAc.setRatePerWeightUnitDiv(new BigDecimal(100));
                } else if (fdEngmet.equals("M")) {
                    if (lclQuoteAc.getRateUom().equalsIgnoreCase("I")) {
                        calculatedWeight = (totalWeight / 1000) * lclUtils.convertToKgs(lclQuoteAc.getRatePerWeightUnit().doubleValue()).doubleValue();
                        calculatedMeasure = totalMeasure * lclUtils.convertToCbm(lclQuoteAc.getRatePerVolumeUnit().doubleValue()).doubleValue();
                    } else {
                        calculatedWeight = (totalWeight / 1000) * lclQuoteAc.getRatePerWeightUnit().doubleValue();
                        calculatedMeasure = totalMeasure * lclQuoteAc.getRatePerVolumeUnit().doubleValue();
                    }
                    lclQuoteAc.setRatePerWeightUnitDiv(new BigDecimal(1000));
                }
                //end of else if engmet
                lclQuoteAc.setRatePerUnitDiv(lclQuoteAc.getRatePerWeightUnitDiv());
                lclQuoteAc.setRatePerVolumeUnitDiv(new BigDecimal(1000));
            }

            if (chargeForm.getFlatRateAmount() != null && !chargeForm.getFlatRateAmount().trim().equals("")) {
                lclQuoteAc.setRatePerUnit(new BigDecimal(chargeForm.getFlatRateAmount()));
                lclQuoteAc.setArAmount(new BigDecimal(chargeForm.getFlatRateAmount()));
            } else {
                lclQuoteAc.setArAmount(BigDecimal.ZERO);
                lclQuoteAc.setRatePerUnit(BigDecimal.ZERO);
            }
            lclQuoteAc.setRateFlatMinimum(CommonUtils.isNotEmpty(chargeForm.getMinimum())
                    ? new BigDecimal(chargeForm.getMinimum()) : BigDecimal.ZERO);

            double calculateChargeWeiMea = 0.00;
            if (calculatedWeight >= calculatedMeasure && calculatedWeight >= lclQuoteAc.getRateFlatMinimum().doubleValue()) {
                lclQuoteAc.setRatePerUnitUom("W");
                lclQuoteAc.setRatePerUnitDiv(lclQuoteAc.getRatePerVolumeUnitDiv());
                calculateChargeWeiMea = calculatedWeight;
            } else if (calculatedMeasure >= calculatedWeight && calculatedMeasure >= lclQuoteAc.getRateFlatMinimum().doubleValue()) {
                lclQuoteAc.setRatePerUnitUom("V");
                lclQuoteAc.setRatePerUnitDiv(lclQuoteAc.getRatePerVolumeUnitDiv());
                calculateChargeWeiMea = calculatedMeasure;
            } else {
                lclQuoteAc.setRatePerUnitUom("M");
                calculateChargeWeiMea = lclQuoteAc.getRateFlatMinimum().doubleValue();
            }

            if (lclQuoteAc.getRatePerUnit().doubleValue() > 0.00 && lclQuoteAc.getRatePerWeightUnit().doubleValue() == 0.00
                    && lclQuoteAc.getRatePerVolumeUnit().doubleValue() == 0.00) {
                lclQuoteAc.setRatePerUnitUom("FL");
            }

            if (null != lclQuoteAc.getArAmount()) {
                calculateChargeWeiMea = calculateChargeWeiMea + lclQuoteAc.getArAmount().doubleValue();
                lclQuoteAc.setArAmount(new BigDecimal(calculateChargeWeiMea));
            }
            lclQuoteAc.setApAmount(CommonUtils.isNotEmpty(chargeForm.getCostAmount())
                    ? new BigDecimal(chargeForm.getCostAmount()) : BigDecimal.ZERO);
            lclQuoteAc.setCostFlatrateAmount(lclQuoteAc.getApAmount());
            lclQuoteAc.setCostMinimum(CommonUtils.isNotEmpty(chargeForm.getMinimumForCost())
                    ? new BigDecimal(chargeForm.getMinimumForCost()) : BigDecimal.ZERO);

            lclQuoteAc.setCostMeasure(CommonUtils.isNotEmpty(chargeForm.getMeasureForCost())
                    ? new BigDecimal(chargeForm.getMeasureForCost()) : BigDecimal.ZERO);
            lclQuoteAc.setCostWeight(CommonUtils.isNotEmpty(chargeForm.getWeightForCost())
                    ? new BigDecimal(chargeForm.getWeightForCost()) : BigDecimal.ZERO);

            double calculatedCostWeight = 0.00, calculatedCostMeasure = 0.00, calculateCostWeiMea = 0.00;
            if (lclQuoteAc.getCostWeight().doubleValue() > 0.00 || lclQuoteAc.getCostMeasure().doubleValue() > 0.00) {
                if (fdEngmet.equals("E")) {
                    if (lclQuoteAc.getRateUom().equalsIgnoreCase("M")) {
                        calculatedCostWeight = (totalWeight / 100) * lclUtils.convertToLbs(lclQuoteAc.getCostWeight().doubleValue()).doubleValue();
                        calculatedCostMeasure = totalMeasure * lclUtils.convertToCft(lclQuoteAc.getCostMeasure().doubleValue()).doubleValue();
                    } else {
                        calculatedCostWeight = (totalWeight / 100) * lclQuoteAc.getCostWeight().doubleValue();
                        calculatedCostMeasure = totalMeasure * lclQuoteAc.getCostMeasure().doubleValue();
                    }
                    lclQuoteAc.setRatePerWeightUnitDiv(new BigDecimal(100));
                } else if (fdEngmet.equals("M")) {
                    if (lclQuoteAc.getRateUom().equalsIgnoreCase("I")) {
                        calculatedCostWeight = (totalWeight / 1000) * lclUtils.convertToKgs(lclQuoteAc.getCostWeight().doubleValue()).doubleValue();
                        calculatedCostMeasure = totalMeasure * lclUtils.convertToCbm(lclQuoteAc.getCostMeasure().doubleValue()).doubleValue();
                    } else {
                        calculatedCostWeight = (totalWeight / 1000) * lclQuoteAc.getCostWeight().doubleValue();
                        calculatedCostMeasure = totalMeasure * lclQuoteAc.getCostMeasure().doubleValue();
                    }
                    lclQuoteAc.setRatePerWeightUnitDiv(new BigDecimal(1000));
                }
                if (calculatedCostWeight >= calculatedCostMeasure && calculatedCostWeight >= lclQuoteAc.getCostMinimum().doubleValue()) {
                    calculateCostWeiMea = calculatedCostWeight;
                } else if (calculatedCostMeasure >= calculatedCostWeight && calculatedCostMeasure >= lclQuoteAc.getCostMinimum().doubleValue()) {
                    calculateCostWeiMea = calculatedCostMeasure;
                } else {
                    calculateCostWeiMea = lclQuoteAc.getCostMinimum().doubleValue();
                }
            }
            if (null != lclQuoteAc.getApAmount()) {
                calculateCostWeiMea = calculateCostWeiMea + lclQuoteAc.getApAmount().doubleValue();
                lclQuoteAc.setApAmount(new BigDecimal(calculateCostWeiMea));
            }
            if (lclQuoteAc.getArglMapping().isDestinationServices()) {
                String calculateMinMax = request.getParameter("calculateMinMax");
                if (calculateMinMax != null && calculateMinMax.equalsIgnoreCase("false")
                        && !lclQuoteAc.getArglMapping().getChargeCode().equalsIgnoreCase("DTHC PREPAID")) {
                    BigDecimal costAmount = lclQuoteAc.getApAmount();
                    BigDecimal destinationDiff = lclQuoteAc.getArAmount().subtract(lclQuoteAc.getApAmount());
                    int profitMax = chargeForm.getProfitMax();
                    int profitMin = chargeForm.getProfitMin();
                    if (destinationDiff.intValue() <= profitMin) {
                        lclQuoteAc.setArAmount(new BigDecimal(costAmount.doubleValue() + profitMin));
                    } else if (destinationDiff.intValue() >= profitMax) {
                        lclQuoteAc.setArAmount(new BigDecimal(costAmount.doubleValue() + profitMax));
                    } else {
                        lclQuoteAc.setArAmount(destinationDiff);
                    }
                }
            }
            // calculation end for cost and  charge;
            lclQuoteAc.setBundleIntoOf(false);
            lclQuoteAc.setPrintOnBl(true);
            chargeDAO.saveOrUpdate(lclQuoteAc);

            String isDest_service_page = request.getParameter("isDest_Service_Page");
            if (null != isDest_service_page && "true".equalsIgnoreCase(isDest_service_page)) {
                setQuoteDestinationServices(chargeForm, lclQuoteAc, request);
                if (lclQuoteAc.getArglMapping().getChargeCode().equalsIgnoreCase("ONCARR")) {
                    quote.setPodfdtt(CommonUtils.isNotEmpty(chargeForm.getTotaldestCarriageTT()) ? chargeForm.getTotaldestCarriageTT() : null);
                    new LCLQuoteDAO().update(quote);
                }
            }

            List<LclQuoteAc> chargeList = chargeDAO.getLclCostByFileNumberAsc(chargeForm.getFileNumberId(), chargeForm.getModuleName());
            ExportQuoteUtils exportQuoteUtils = new ExportQuoteUtils();
            LclQuotationChargesCalculation lclQuotationChargesCalculation = new LclQuotationChargesCalculation();
            lclQuoteAc = chargeDAO.manaualChargeValidate(chargeForm.getFileNumberId(), "CAF", false);
            if (lclQuoteAc != null) {
                lclQuotationChargesCalculation.calculateCAFChargeForRadio(pooTrmnum, polTrmnum, fdEciPortCode, podEciPortCode, quotePiecesList, quote.getBillingType(),
                        getCurrentUser(request), chargeForm.getFileNumberId(), quote.getFinalDestination().getUnLocationCode(), fdEngmet);
            }
            exportQuoteUtils.setWeighMeasureForQuote(request, quotePiecesList, fdEngmet);
            exportQuoteUtils.setRolledUpChargesForQuote(chargeList, request, chargeForm.getFileNumberId(), chargeDAO,
                    quotePiecesList, request.getParameter("billingType"), fdEngmet, "No");
            request.setAttribute("lclCommodityList", quotePiecesList);
            request.setAttribute("lclQuote", quote);

        } catch (Exception e) {
            log.info("Error in LclCostAndChargeAction Class addCharges method. " + new Date(), e);
            return mapping.findForward(CHARGE_DESC);
        }

        return mapping.findForward(CHARGE_DESC);
    }

    public void deleteDestinationCharge(LclQuoteCostAndChargeForm chargeForm,
            HttpServletRequest request) throws Exception {
        LclQuoteAcDAO lclQuoteAcDAO = new LclQuoteAcDAO();
        User loginUser = getCurrentUser(request);
        if (CommonUtils.isNotEmpty(chargeForm.getId())) {
            LclQuoteAc lclQuoteAc = lclQuoteAcDAO.findById(chargeForm.getId());
            if (lclQuoteAc != null) {
                String remarks = new LclQuoteUtils().setDeleteChargeTriggerValues(lclQuoteAc);
                new LclRemarksDAO().insertLclRemarks(chargeForm.getFileNumberId(), REMARKS_QT_AUTO_NOTES, remarks, loginUser.getUserId());
                lclQuoteAcDAO.delete(lclQuoteAc);
            }
        }

    }
}
