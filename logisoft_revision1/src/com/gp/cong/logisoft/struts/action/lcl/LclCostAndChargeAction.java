/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.lcl.dwr.LclChargesCalculation;
import com.gp.cong.lcl.dwr.LclDwr;
import com.gp.cong.lcl.model.ExportVoyageSearchModel;
import com.gp.cong.logisoft.domain.Ports;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclBookingAc;
import com.gp.cong.logisoft.domain.lcl.LclBookingDestinationServices;
import com.gp.cong.logisoft.domain.lcl.LclBookingPiece;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.domain.lcl.bl.LclCorrection;
import com.gp.cong.logisoft.hibernate.dao.CodetypeDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.WarehouseDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingAcTransDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclConsolidateDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLCorrectionChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLCorrectionDAO;
import com.gp.cong.logisoft.lcl.bc.ExportBookingUtils;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cvst.logisoft.domain.GlMapping;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LclCostAndChargeForm;
import com.logiware.accounting.dao.LclManifestDAO;
import com.logiware.common.dao.PropertyDAO;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.log4j.Logger;

/**
 *
 * @author lakshh
 */
public class LclCostAndChargeAction extends LogiwareDispatchAction implements ConstantsInterface, LclCommonConstant {

    private static final Logger log = Logger.getLogger(LclCostAndChargeAction.class);
    private static String CHARGE_DESC = "chargeDesc";
    private static String CHARGE_INVOICE = "chargeInvoiceTab";
    private LclUtils lclUtils = new LclUtils();
    private LCLBookingDAO lclBookingDAO = new LCLBookingDAO();
    WarehouseDAO warehouseDAO = new WarehouseDAO();

    public ActionForward display(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclCostAndChargeForm costForm = (LclCostAndChargeForm) form;
        setValues(costForm, request);
        if (costForm.getModuleName().equalsIgnoreCase("Exports")) {
            costForm.setBillToParty("B".equalsIgnoreCase(costForm.getBkgBillToParty()) ? "" : costForm.getBkgBillingType());
            request.setAttribute("billToPartyList", new LclUtils().getBillingTypeByLclE(costForm.getBkgBillToParty(), costForm.getBkgBillingType()));
        }
        return mapping.findForward("success");
    }

    public ActionForward displayInvoiceCharges(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclCostAndChargeForm lclCostAndChargeForm = (LclCostAndChargeForm) form;
        setValues(lclCostAndChargeForm, request);
        return mapping.findForward(CHARGE_INVOICE);
    }

    private void setValues(LclCostAndChargeForm lclCostAndChargeForm,
            HttpServletRequest request) throws Exception {
        request.setAttribute("fileNumber", request.getParameter("fileNumber"));
        request.setAttribute("buttonValue", request.getParameter("buttonValue"));
        if (CommonUtils.isNotEmpty(lclCostAndChargeForm.getDestination())) {
            PortsDAO portsDAO = new PortsDAO();
            Ports ports = portsDAO.getByProperty("unLocationCode", lclCostAndChargeForm.getDestination());
            if (ports != null && ports.getEciportcode() != null && !ports.getEciportcode().trim().equals("")) {
                request.setAttribute("engmet", ports.getEngmet());
                request.setAttribute("ports", ports);
            }
            request.setAttribute("destination", lclCostAndChargeForm.getDestination());
        }
        request.setAttribute("lclCostAndChargeForm", lclCostAndChargeForm);
    }

    public ActionForward displayDocum(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclCostAndChargeForm lclCostAndChargeForm = (LclCostAndChargeForm) form;
        request.setAttribute("lclCostAndChargeForm", lclCostAndChargeForm);
        return mapping.findForward("display");
    }

    public ActionForward addCharges(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String origin = "";
            User loginUser = getCurrentUser(request);
            LclCostAndChargeForm lclCostAndChargeForm = (LclCostAndChargeForm) form;
            LclCostChargeDAO lclCostChargesDAO = new LclCostChargeDAO();
            LclBookingAc lclBookingAc = lclCostAndChargeForm.getLclBookingAc();
            LclFileNumberDAO lclFileNumberDAO = new LclFileNumberDAO();
            GlMappingDAO glMappingDAO = new GlMappingDAO();
            String billingType = request.getParameter("billingType");
            List<LclBookingPiece> lclBookingPiecesList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", lclCostAndChargeForm.getFileNumberId());
            Double totalWeight = 0.00;
            Double totalMeasure = 0.00;
            Double calculatedWeight = 0.00;
            Double calculatedCostWeight = 0.00;
            Double calculatedMeasure = 0.00;
            String engmet = new String();
            Double calculatedCostMeasure = 0.00;
            boolean updateAccruals = false;
            String pooorigin = "", polorigin = "", destinationfd = "", destinationpod = "";
            Ports ports = null;
            String ofratebasis = new String();
            String shipmentType = null;
            LclBooking lclBooking = lclBookingDAO.findById(lclCostAndChargeForm.getFileNumberId());
            if (CommonFunctions.isNotNull(lclBooking.getPortOfOrigin())) {
                origin = lclBooking.getPortOfOrigin().getUnLocationName();
            } else if (CommonFunctions.isNotNull(lclBooking.getPortOfLoading()) && CommonFunctions.isNotNull(lclBooking.getPortOfLoading().getUnLocationName())) {
                origin = lclBooking.getPortOfLoading().getUnLocationName();
            }
            String destination = lclBooking.getFinalDestination().getUnLocationName();
            //Export
            if ("Exports".equalsIgnoreCase(lclCostAndChargeForm.getModuleName()) && ("T".equalsIgnoreCase(lclBooking.getBookingType()) || "E".equalsIgnoreCase(lclBooking.getBookingType()))) {
                String rateType = lclBooking.getRateType();
                if (rateType.equalsIgnoreCase("R")) {
                    rateType = "Y";
                }
                RefTerminalDAO refterminaldao = new RefTerminalDAO();
                if (lclBooking.getPortOfOrigin() != null) {
                    RefTerminal refterminal = refterminaldao.getTerminalByUnLocation(lclBooking.getPortOfOrigin().getUnLocationCode(), rateType);
                    if (refterminal != null && refterminal.getTrmnum() != null && !refterminal.getTrmnum().trim().equals("")) {
                        pooorigin = refterminal.getTrmnum();
                        ofratebasis = refterminal.getTrmnum() + "-";
                    }
                }
                if (lclBooking.getPortOfLoading() != null) {
                    RefTerminal refterminalpol = refterminaldao.getTerminalByUnLocation(lclBooking.getPortOfLoading().getUnLocationCode(), rateType);
                    if (refterminalpol != null && refterminalpol.getTrmnum() != null && !refterminalpol.getTrmnum().trim().equals("")) {
                        polorigin = refterminalpol.getTrmnum();
                    }
                }
                PortsDAO portsDAO = new PortsDAO();
                if (lclBooking.getPortOfDestination() != null) {
                    Ports portspod = portsDAO.getByProperty("unLocationCode", lclBooking.getPortOfDestination().getUnLocationCode());
                    if (portspod != null && portspod.getEciportcode() != null && !portspod.getEciportcode().trim().equals("")) {
                        destinationpod = portspod.getEciportcode();
                    }
                }
                ports = portsDAO.getByProperty("unLocationCode", lclBooking.getFinalDestination().getUnLocationCode());
                if (ports != null && ports.getEciportcode() != null && !ports.getEciportcode().trim().equals("")) {
                    engmet = ports.getEngmet();
                    destinationfd = ports.getEciportcode();
                    ofratebasis += ports.getEciportcode() + "-";
                }
                request.setAttribute("billToPartyList", new LclUtils().getBillingTypeByLclE(lclCostAndChargeForm.getBkgBillToParty(), lclCostAndChargeForm.getBkgBillingType()));
            }
            if (LCL_EXPORT.equalsIgnoreCase(lclCostAndChargeForm.getModuleName()) && ("T".equalsIgnoreCase(lclBooking.getBookingType()) || "E".equalsIgnoreCase(lclBooking.getBookingType()))) {
                shipmentType = LCL_SHIPMENT_TYPE_EXPORT;
            } else {
                shipmentType = LCL_SHIPMENT_TYPE_IMPORT;
            }
            //Charges are added
            if (CommonUtils.isNotEmpty(lclCostAndChargeForm.getFileNumberId())) {
                lclBookingAc.setLclFileNumber(lclBooking.getLclFileNumber());
            }
            if (CommonUtils.isNotEmpty(lclCostAndChargeForm.getChargesCode())) {
                GlMapping arGlmapping = null;
                if (null != lclCostAndChargeForm.getChargesCodeId()) {
                    arGlmapping = glMappingDAO.findById(lclCostAndChargeForm.getChargesCodeId());
                } else {
                    arGlmapping = glMappingDAO.findByChargeCode(lclCostAndChargeForm.getChargesCode(), shipmentType, TRANSACTION_TYPE_ACCOUNT_RECEIVABLE);
                }
                GlMapping apGlmapping = glMappingDAO.findByChargeCode(lclCostAndChargeForm.getChargesCode(), shipmentType, TRANSACTION_TYPE_ACCRUALS);
                lclBookingAc.setApglMapping(apGlmapping);
                if (arGlmapping != null) {
                    lclBookingAc.setArglMapping(arGlmapping);
                } else {
                    lclBookingAc.setArglMapping(apGlmapping);
                }
            }
            if (CommonUtils.isNotEmpty(lclCostAndChargeForm.getBillToParty())
                    && "W".equalsIgnoreCase(lclCostAndChargeForm.getBillToParty())) {
                lclBookingAc.setPostAr(false);
            }
            if ("Imports".equalsIgnoreCase(lclCostAndChargeForm.getModuleName())) {
                lclBookingAc.setArBillToParty(lclCostAndChargeForm.getBillToParty());
            } else {
                if (CommonUtils.isNotEmpty(lclCostAndChargeForm.getBillToParty())) {
                    lclBookingAc.setArBillToParty(lclCostAndChargeForm.getBillToParty());
                } else {
                    lclBookingAc.setArBillToParty(lclBooking.getBillToParty());
                }
            }

            lclBookingAc.setRelsToInv(CommonUtils.isNotEmpty(lclCostAndChargeForm.getBillToParty())
                    && lclCostAndChargeForm.getBillToParty().equalsIgnoreCase("A") ? true : false);
            lclBookingAc.setTransDatetime(new Date());
            lclBookingAc.setEnteredBy(getCurrentUser(request));
            lclBookingAc.setModifiedBy(getCurrentUser(request));
            lclBookingAc.setEnteredDatetime(new Date());
            lclBookingAc.setModifiedDatetime(new Date());
            if (!lclCostAndChargeForm.getManualEntry().equalsIgnoreCase("false")) {
                lclBookingAc.setManualEntry(true);
                lclBookingAc.setAdjustmentAmount(BigDecimal.ZERO);
            }
            if (!lclCostAndChargeForm.getManualEntry().equalsIgnoreCase("false")) {
                if (lclCostAndChargeForm.getFlatRateAmount() != null && !lclCostAndChargeForm.getFlatRateAmount().trim().equals("")) {
                    lclBookingAc.setRatePerUnit(new BigDecimal(lclCostAndChargeForm.getFlatRateAmount()));
                    lclBookingAc.setArAmount(new BigDecimal(lclCostAndChargeForm.getFlatRateAmount()));
                    lclBookingAc.setRatePerUnitUom("FL");
                } else {
                    lclBookingAc.setRatePerUnit(BigDecimal.ZERO);
                }
            }
            if (lclCostAndChargeForm.getCostAmount() != null && !"".equalsIgnoreCase(lclCostAndChargeForm.getCostAmount())) {//FLAT RATE
                lclBookingAc.setApAmount(new BigDecimal(lclCostAndChargeForm.getCostAmount()));
            } else {
                lclBookingAc.setApAmount(BigDecimal.ZERO);
            }
            lclBookingAc.setCostFlatrateAmount(lclBookingAc.getApAmount());
            if (lclCostAndChargeForm.getMeasureForCost() != null && !lclCostAndChargeForm.getMeasureForCost().trim().equals("")) {//CBM OR CFT
                lclBookingAc.setCostMeasure(new BigDecimal(lclCostAndChargeForm.getMeasureForCost()));
            } else {
                lclBookingAc.setCostMeasure(BigDecimal.ZERO);
            }
            if (lclCostAndChargeForm.getWeightForCost() != null && !lclCostAndChargeForm.getWeightForCost().trim().equals("")) {//KGS OR LBS
                lclBookingAc.setCostWeight(new BigDecimal(lclCostAndChargeForm.getWeightForCost()));
            } else {
                lclBookingAc.setCostWeight(BigDecimal.ZERO);

            }
            if (lclCostAndChargeForm.getMinimumForCost() != null && !lclCostAndChargeForm.getMinimumForCost().trim().equals("")) {//MINIMUM
                lclBookingAc.setCostMinimum(new BigDecimal(lclCostAndChargeForm.getMinimumForCost()));
            } else {
                lclBookingAc.setCostMinimum(BigDecimal.ZERO);
            }

            if (!lclCostAndChargeForm.getManualEntry().equalsIgnoreCase("false")) {
                if ((lclCostAndChargeForm.getWeight() != null && !lclCostAndChargeForm.getWeight().trim().equals("") && !lclCostAndChargeForm.getWeight().trim().equals("0.00")
                        && lclCostAndChargeForm.getMeasure() != null && !lclCostAndChargeForm.getMeasure().trim().equals("") && !lclCostAndChargeForm.getMeasure().trim().equals("0.00")
                        && lclCostAndChargeForm.getMinimum() != null && !lclCostAndChargeForm.getMinimum().trim().equals("") && !lclCostAndChargeForm.getMinimum().trim().equals("0.00"))
                        || (lclCostAndChargeForm.getWeightForCost() != null && !lclCostAndChargeForm.getWeightForCost().trim().equals("") && !lclCostAndChargeForm.getWeightForCost().trim().equals("0.00")
                        && lclCostAndChargeForm.getMeasureForCost() != null && !lclCostAndChargeForm.getMeasureForCost().trim().equals("") && !lclCostAndChargeForm.getMeasureForCost().trim().equals("0.00")
                        && lclCostAndChargeForm.getMinimumForCost() != null && !lclCostAndChargeForm.getMinimumForCost().trim().equals("") && !lclCostAndChargeForm.getMinimumForCost().trim().equals("0.00"))) {
                    if (CommonUtils.isNotEmpty(lclCostAndChargeForm.getWeight())) {
                        lclBookingAc.setRatePerWeightUnit(new BigDecimal(lclCostAndChargeForm.getWeight()));
                    } else {
                        lclBookingAc.setRatePerWeightUnit(BigDecimal.ZERO);
                    }
                    if (CommonUtils.isNotEmpty(lclCostAndChargeForm.getMeasure())) {
                        lclBookingAc.setRatePerVolumeUnit(new BigDecimal(lclCostAndChargeForm.getMeasure()));
                    } else {
                        lclBookingAc.setRatePerVolumeUnit(BigDecimal.ZERO);
                    }
                    if (CommonUtils.isNotEmpty(lclCostAndChargeForm.getMinimum())) {
                        lclBookingAc.setRateFlatMinimum(new BigDecimal(lclCostAndChargeForm.getMinimum()));
                    } else {
                        lclBookingAc.setRateFlatMinimum(BigDecimal.ZERO);
                    }
                    for (int j = 0; j < lclBookingPiecesList.size(); j++) {
                        LclBookingPiece lclBookingPiece = (LclBookingPiece) lclBookingPiecesList.get(j);
                        Double weightDouble = 0.00;
                        Double weightMeasure = 0.00;
                        if ("Imports".equalsIgnoreCase(lclCostAndChargeForm.getModuleName()) && ("T".equalsIgnoreCase(lclBooking.getBookingType()) || "I".equalsIgnoreCase(lclBooking.getBookingType()))) {
                            engmet = lclBookingAc.getRateUom();
                            if (engmet.equalsIgnoreCase("I")) {
                                engmet = "E";
                            }
                        }
//                        if (engmet != null) {
//                            if (engmet.equals("E")) {
                        if (lclBookingAc.getRateUom() != null) {
                            if (("I").equalsIgnoreCase(lclBookingAc.getRateUom())) {
                                if (lclBookingPiece.getActualWeightImperial() != null && lclBookingPiece.getActualWeightImperial().doubleValue() != 0.00) {
                                    weightDouble = lclBookingPiece.getActualWeightImperial().doubleValue();
                                } else if (lclBookingPiece.getBookedWeightImperial() != null && lclBookingPiece.getBookedWeightImperial().doubleValue() != 0.00) {
                                    weightDouble = lclBookingPiece.getBookedWeightImperial().doubleValue();
                                }

                                if (lclBookingPiece.getActualVolumeImperial() != null && lclBookingPiece.getActualVolumeImperial().doubleValue() != 0.00) {
                                    weightMeasure = lclBookingPiece.getActualVolumeImperial().doubleValue();
                                } else if (lclBookingPiece.getBookedVolumeImperial() != null && lclBookingPiece.getBookedVolumeImperial().doubleValue() != 0.00) {
                                    weightMeasure = lclBookingPiece.getBookedVolumeImperial().doubleValue();
                                }
                            } else if (("M").equalsIgnoreCase(lclBookingAc.getRateUom())) {
                                if (lclBookingPiece.getActualWeightMetric() != null && lclBookingPiece.getActualWeightMetric().doubleValue() != 0.00) {
                                    weightDouble = lclBookingPiece.getActualWeightMetric().doubleValue();
                                } else if (lclBookingPiece.getBookedWeightMetric() != null && lclBookingPiece.getBookedWeightMetric().doubleValue() != 0.00) {
                                    weightDouble = lclBookingPiece.getBookedWeightMetric().doubleValue();//kgs
                                }
                                if (lclBookingPiece.getActualVolumeMetric() != null && lclBookingPiece.getActualVolumeMetric().doubleValue() != 0.00) {
                                    weightMeasure = lclBookingPiece.getActualVolumeMetric().doubleValue();
                                } else if (lclBookingPiece.getBookedVolumeMetric() != null && lclBookingPiece.getBookedVolumeMetric().doubleValue() != 0.00) {
                                    weightMeasure = lclBookingPiece.getBookedVolumeMetric().doubleValue();//cbm
                                }
                            }//end of else if engmet
//                        }//end of else null

                            //calculate the Total Weight Of Commodities
                            totalWeight = totalWeight + weightDouble;
                            //calculate the Total Measure Of Commodities
                            totalMeasure = totalMeasure + weightMeasure;
                        }
                    }
                } else {
                    lclBookingAc.setRatePerWeightUnit(BigDecimal.ZERO);
                    lclBookingAc.setRatePerVolumeUnit(BigDecimal.ZERO);
                    lclBookingAc.setRateFlatMinimum(BigDecimal.ZERO);
                    lclBookingAc.setCostWeight(BigDecimal.ZERO);
                    lclBookingAc.setCostMeasure(BigDecimal.ZERO);
                    lclBookingAc.setCostMinimum(BigDecimal.ZERO);
                }
            }
            //end of for loop
            if (!lclCostAndChargeForm.getManualEntry().equalsIgnoreCase("false") && lclBookingAc.getRatePerWeightUnit() != null
                    && lclBookingAc.getRatePerWeightUnit().doubleValue() > 0.00 && lclBookingAc.getRatePerVolumeUnit() != null
                    && lclBookingAc.getRatePerVolumeUnit().doubleValue() > 0.00 && lclBookingAc.getRateFlatMinimum() != null
                    && lclBookingAc.getRateFlatMinimum().doubleValue() > 0.00) {
                if (engmet != null && lclBooking.getBookingType().equalsIgnoreCase("E")) {
                    if (engmet.equals("E")) {
                        if (lclBookingAc.getRateUom().equalsIgnoreCase("M")) {
                            calculatedWeight = (totalWeight / 100) * lclUtils.convertToLbs(lclBookingAc.getRatePerWeightUnit().doubleValue()).doubleValue();
                            calculatedMeasure = totalMeasure * lclUtils.convertToCft(lclBookingAc.getRatePerVolumeUnit().doubleValue()).doubleValue();
                        } else {
                            calculatedWeight = (totalWeight / 100) * lclBookingAc.getRatePerWeightUnit().doubleValue();
                            calculatedMeasure = totalMeasure * lclBookingAc.getRatePerVolumeUnit().doubleValue();
                        }
                        lclBookingAc.setRatePerWeightUnitDiv(new BigDecimal(100));
                    } else if (engmet.equals("M")) {
                        if (lclBookingAc.getRateUom().equalsIgnoreCase("I")) {
                            calculatedWeight = (totalWeight / 1000) * lclUtils.convertToKgs(lclBookingAc.getRatePerWeightUnit().doubleValue()).doubleValue();
                            calculatedMeasure = totalMeasure * lclUtils.convertToCbm(lclBookingAc.getRatePerVolumeUnit().doubleValue()).doubleValue();
                        } else {
                            calculatedWeight = (totalWeight / 1000) * lclBookingAc.getRatePerWeightUnit().doubleValue();
                            calculatedMeasure = totalMeasure * lclBookingAc.getRatePerVolumeUnit().doubleValue();
                        }
                        lclBookingAc.setRatePerWeightUnitDiv(new BigDecimal(1000));
                    }
                } else {
                    calculatedMeasure = totalMeasure * lclBookingAc.getRatePerVolumeUnit().doubleValue();
                    if (lclBookingAc.getRateUom().equalsIgnoreCase("M")) {
                        calculatedWeight = (totalWeight / 1000) * lclBookingAc.getRatePerWeightUnit().doubleValue();
                    } else {
                        calculatedWeight = (totalWeight / 100) * lclBookingAc.getRatePerWeightUnit().doubleValue();
                    }
                }
                //end of else if engmet
                lclBookingAc.setRatePerUnitDiv(lclBookingAc.getRatePerWeightUnitDiv());
                lclBookingAc.setRatePerVolumeUnitDiv(new BigDecimal(1000));
                if (calculatedWeight >= calculatedMeasure && calculatedWeight >= lclBookingAc.getRateFlatMinimum().doubleValue()) {
                    lclBookingAc.setArAmount(new BigDecimal(calculatedWeight));
                    lclBookingAc.setRatePerUnitUom("W");
                    lclBookingAc.setRatePerUnitDiv(lclBookingAc.getRatePerVolumeUnitDiv());
                } else if (calculatedMeasure >= calculatedWeight && calculatedMeasure >= lclBookingAc.getRateFlatMinimum().doubleValue()) {
                    lclBookingAc.setArAmount(new BigDecimal(calculatedMeasure));
                    lclBookingAc.setRatePerUnitUom("V");
                    lclBookingAc.setRatePerUnitDiv(lclBookingAc.getRatePerVolumeUnitDiv());

                } else {
                    lclBookingAc.setArAmount(lclBookingAc.getRateFlatMinimum());
                    lclBookingAc.setRatePerUnitUom("M");
                }
                //cost amount/costMinimum/costMeasure/costMeasure
            } else {
                if ("false".equalsIgnoreCase(lclCostAndChargeForm.getManualEntry()) && lclCostAndChargeForm.getArAmount() != null && !lclCostAndChargeForm.getArAmount().equals("")) {
                    lclBookingAc.setArAmount(new BigDecimal(lclCostAndChargeForm.getArAmount()));
                }
            }
            if (lclBookingAc.getCostWeight() != null && lclBookingAc.getCostWeight().doubleValue() > 0.00
                    && lclBookingAc.getCostMeasure() != null && lclBookingAc.getCostMeasure().doubleValue() > 0.00
                    && lclBookingAc.getCostMinimum() != null && lclBookingAc.getCostMinimum().doubleValue() > 0.00) {
                if (lclBookingAc.getRateUom() != null) {
                    if (("I").equalsIgnoreCase(lclBookingAc.getRateUom())) {
                        calculatedCostWeight = (totalWeight / 100) * lclBookingAc.getCostWeight().doubleValue();
                    } else {
                        calculatedCostWeight = (totalWeight / 1000) * lclBookingAc.getCostWeight().doubleValue();
                    }
                }
                calculatedCostMeasure = totalMeasure * lclBookingAc.getCostMeasure().doubleValue();
                if (calculatedCostWeight >= calculatedCostMeasure && calculatedCostWeight >= lclBookingAc.getCostMinimum().doubleValue()) {
                    lclBookingAc.setApAmount(new BigDecimal(calculatedCostWeight));
                } else if (calculatedCostMeasure >= calculatedCostWeight && calculatedCostMeasure >= lclBookingAc.getCostMinimum().doubleValue()) {
                    lclBookingAc.setApAmount(new BigDecimal(calculatedCostMeasure));

                } else {
                    lclBookingAc.setApAmount(lclBookingAc.getCostMinimum());
                }
            }
            if (CommonUtils.isEmpty(lclBookingAc.getArAmount()) && CommonUtils.isEmpty(lclBookingAc.getRateFlatMinimum())) {
                lclBookingAc.setArAmount(BigDecimal.ZERO);
                lclBookingAc.setRatePerUnitUom("FL");
            }
            if (CommonUtils.isNotEmpty(lclBookingAc.getId())) {
                updateAccruals = true;
            }
            lclBookingAc.setBundleIntoOf(false);
            lclBookingAc.setPrintOnBl(true);
            lclBookingAc.setControlNo(lclBookingAc.getControlNo());
            LclManifestDAO lclManifestDAO = new LclManifestDAO();
            if (lclBookingAc.getArAmount() != null && lclBookingAc.getArAmount().doubleValue() != 0.00
                    && lclCostAndChargeForm.getCostAmount() != null && "0.00".equalsIgnoreCase(lclCostAndChargeForm.getCostAmount())
                    && CommonUtils.isNotEmpty(lclCostAndChargeForm.getCostStatus()) && lclCostAndChargeForm.getCostStatus().equals("AC")) {
                lclBookingAc.setApAmount(BigDecimal.ZERO);
                lclBookingAc.setSupAcct(null);
                lclBookingAc.setDeleted(Boolean.TRUE);
                lclCostChargesDAO.saveOrUpdate(lclBookingAc);
                lclManifestDAO.deleteLclAccruals(lclBookingAc.getId().intValue(), lclBookingAc.getLclFileNumber().getFileNumber());
                lclCostChargesDAO.deleteLclBookingAcTa(lclBookingAc.getId().intValue());
            } else {
                lclCostChargesDAO.saveOrUpdate(lclBookingAc);
            }
            if (lclBooking.getBookingType().equalsIgnoreCase("E")) {
                if (CommonUtils.isNotEmpty(lclCostAndChargeForm.getExistBillToParty())
                        && !lclCostAndChargeForm.getExistBillToParty().equalsIgnoreCase(lclBookingAc.getArBillToParty())) {
                    String notes = "UPDATED -> (Code -> " + lclBookingAc.getArglMapping().getChargeCode()
                            + ")(Bill To Party -> " + lclCostAndChargeForm.getExistBillToParty() + " to " + lclBookingAc.getArBillToParty() + ")";
                    new LclRemarksDAO().insertLclRemarks(lclCostAndChargeForm.getFileNumberId(), REMARKS_DR_AUTO_NOTES,
                            notes, loginUser.getUserId());
                }
                new LclChargesCalculation().calculateCAFCharge(pooorigin, polorigin, destinationfd, destinationpod, lclBookingPiecesList, lclBooking.getBillingType(),
                        getCurrentUser(request), lclCostAndChargeForm.getFileNumberId(), request, ports, lclBooking.getBillToParty());
            }
            if (CommonUtils.isNotEmpty(lclBookingPiecesList) && LclCommonConstant.LCL_EXPORT.equalsIgnoreCase(lclCostAndChargeForm.getModuleName())) {
                LclBookingPiece lclBookingPiece = lclBookingPiecesList.get(0);
                if (lclBookingPiecesList != null && lclBookingPiecesList.size() > 0) {
                    lclBookingPiece = lclBookingPiecesList.get(0);
                    if (lclBookingPiece.getStdchgRateBasis() != null && !lclBookingPiece.getStdchgRateBasis().trim().equals("")) {
                        request.setAttribute("stdchgratebasis", lclBookingPiece.getStdchgRateBasis());
                    }
                }
                request.setAttribute("lclCommodityList", lclBookingPiecesList);
                ofratebasis += lclBookingPiece.getCommodityType().getCode();
                request.setAttribute("ofratebasis", ofratebasis);
            }

            List<LclBookingAc> chargeList = lclCostChargesDAO.getLclCostByFileNumberAsc(lclCostAndChargeForm.getFileNumberId(), lclCostAndChargeForm.getModuleName());
            request.setAttribute("origin", origin);
            request.setAttribute("destination", destination);
            if ("Exports".equalsIgnoreCase(lclCostAndChargeForm.getModuleName()) && ("T".equalsIgnoreCase(lclBooking.getBookingType()) || "E".equalsIgnoreCase(lclBooking.getBookingType()))) {
                lclUtils.setWeighMeasureForBooking(request, lclBookingPiecesList, ports);
                lclUtils.setRolledUpChargesForBooking(chargeList, request, lclCostAndChargeForm.getFileNumberId(), lclCostChargesDAO,
                        lclBookingPiecesList, billingType, engmet, "No");
            } else {
                String agentNo = "";
                if (!lclCostAndChargeForm.getAgentNo().equals("") && lclCostAndChargeForm.getAgentNo() != null) {
                    agentNo = lclCostAndChargeForm.getAgentNo();
                }
                lclUtils.setWeighMeasureForImportBooking(request, lclBookingPiecesList, null);
                lclUtils.setImportRolledUpChargesForBooking(chargeList, request, lclCostAndChargeForm.getFileNumberId(), lclCostChargesDAO,
                        lclBookingPiecesList, lclBooking.getBillingType(), "", agentNo);
            }
            //request.setAttribute("chargeList", chargeList);
            if ("Exports".equalsIgnoreCase(lclCostAndChargeForm.getModuleName()) && "T".equalsIgnoreCase(lclBooking.getBookingType())) {
                lclUtils.setTemBillToPartyList(request, "");
            }
            request.setAttribute("lclBooking", lclBooking);
        } catch (Exception e) {
            log.info("Error in LclCostAndChargeAction Class addCharges method. " + new Date(), e);
            return mapping.findForward(CHARGE_DESC);
        }
        return mapping.findForward(CHARGE_DESC);
    }

    public ActionForward addChargesInvoice(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String origin = "";
            LclCostAndChargeForm lclCostAndChargeForm = (LclCostAndChargeForm) form;
            LclCostChargeDAO lclCostChargesDAO = new LclCostChargeDAO();
            LclBookingAc lclBookingAc = lclCostAndChargeForm.getLclBookingAc();
            LclFileNumberDAO lclFileNumberDAO = new LclFileNumberDAO();
            GlMappingDAO glMappingDAO = new GlMappingDAO();
            String billingType = request.getParameter("billingType");
            List<LclBookingPiece> lclBookingPiecesList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", lclCostAndChargeForm.getFileNumberId());
            Double totalWeight = 0.00;
            Double totalMeasure = 0.00;
            Double calculatedWeight = 0.00;
            Double calculatedMeasure = 0.00;
            String engmet = new String();
            LclBooking lclBooking = lclBookingDAO.findById(lclCostAndChargeForm.getFileNumberId());
            request.setAttribute("fileNumberStatus", lclBooking.getLclFileNumber().getStatus());
            if (CommonFunctions.isNotNull(lclBooking.getPortOfOrigin())) {
                origin = lclBooking.getPortOfOrigin().getUnLocationName();
            } else if (CommonFunctions.isNotNull(lclBooking.getPortOfLoading()) && CommonFunctions.isNotNull(lclBooking.getPortOfLoading().getUnLocationName())) {
                origin = lclBooking.getPortOfLoading().getUnLocationName();
            }
            String destination = lclBooking.getFinalDestination().getUnLocationName();
            String rateType = lclBooking.getRateType();
            if (rateType.equalsIgnoreCase("R")) {
                rateType = "Y";
            }
            String ofratebasis = new String();
            RefTerminalDAO refterminaldao = new RefTerminalDAO();
            if (lclBooking.getPortOfOrigin() != null) {
                RefTerminal refterminal = refterminaldao.getTerminalByUnLocation(lclBooking.getPortOfOrigin().getUnLocationCode(), rateType);
                if (refterminal != null && refterminal.getTrmnum() != null && !refterminal.getTrmnum().trim().equals("")) {
                    ofratebasis = refterminal.getTrmnum() + "-";
                }
            }
            PortsDAO portsDAO = new PortsDAO();
            Ports ports = portsDAO.getByProperty("unLocationCode", lclBooking.getFinalDestination().getUnLocationCode());
            if (ports != null && ports.getEciportcode() != null && !ports.getEciportcode().trim().equals("")) {
                engmet = ports.getEngmet();
                ofratebasis += ports.getEciportcode() + "-";
            }
            if (CommonUtils.isNotEmpty(lclCostAndChargeForm.getFileNumberId())) {
                lclBookingAc.setLclFileNumber(lclFileNumberDAO.findById(lclCostAndChargeForm.getFileNumberId()));
            }
            if (CommonUtils.isNotEmpty(lclCostAndChargeForm.getChargesCode())) {
                GlMapping arGlmapping = glMappingDAO.findById(lclCostAndChargeForm.getChargesCodeId());
                lclBookingAc.setArglMapping(arGlmapping);
            }
            lclBookingAc.setArBillToParty("A");
            lclBookingAc.setTransDatetime(new Date());
            lclBookingAc.setEnteredBy(getCurrentUser(request));
            lclBookingAc.setModifiedBy(getCurrentUser(request));
            lclBookingAc.setEnteredDatetime(new Date());
            lclBookingAc.setModifiedDatetime(new Date());
            lclBookingAc.setManualEntry(true);
            lclBookingAc.setAdjustmentAmount(BigDecimal.ZERO);
            if (lclCostAndChargeForm.getFlatRateAmount() != null && !lclCostAndChargeForm.getFlatRateAmount().trim().equals("")) {
                lclBookingAc.setRatePerUnit(new BigDecimal(lclCostAndChargeForm.getFlatRateAmount()));
                lclBookingAc.setArAmount(new BigDecimal(lclCostAndChargeForm.getFlatRateAmount()));
                lclBookingAc.setRatePerUnitUom("FL");
            } else {
                lclBookingAc.setRatePerUnit(BigDecimal.ZERO);
            }

            if (lclCostAndChargeForm.getWeight() != null && !lclCostAndChargeForm.getWeight().trim().equals("") && !lclCostAndChargeForm.getWeight().trim().equals("0.00")
                    && lclCostAndChargeForm.getMeasure() != null && !lclCostAndChargeForm.getMeasure().trim().equals("") && !lclCostAndChargeForm.getMeasure().trim().equals("0.00")
                    && lclCostAndChargeForm.getMinimum() != null && !lclCostAndChargeForm.getMinimum().trim().equals("") && !lclCostAndChargeForm.getMinimum().trim().equals("0.00")) {
                if (CommonUtils.isNotEmpty(lclCostAndChargeForm.getWeight())) {
                    lclBookingAc.setRatePerWeightUnit(new BigDecimal(lclCostAndChargeForm.getWeight()));
                }
                if (CommonUtils.isNotEmpty(lclCostAndChargeForm.getMeasure())) {
                    lclBookingAc.setRatePerVolumeUnit(new BigDecimal(lclCostAndChargeForm.getMeasure()));
                }
                if (CommonUtils.isNotEmpty(lclCostAndChargeForm.getMinimum())) {
                    lclBookingAc.setRateFlatMinimum(new BigDecimal(lclCostAndChargeForm.getMinimum()));
                }
                for (int j = 0; j < lclBookingPiecesList.size(); j++) {
                    LclBookingPiece lclBookingPiece = (LclBookingPiece) lclBookingPiecesList.get(j);
                    Double weightDouble = 0.00;
                    Double weightMeasure = 0.00;
                    engmet = lclBookingAc.getRateUom();
                    if (engmet.equalsIgnoreCase("I")) {
                        engmet = "E";
                    }
                    if (engmet != null) {
                        if (engmet.equals("E")) {
                            if (lclBookingPiece.getActualWeightImperial() != null && lclBookingPiece.getActualWeightImperial().doubleValue() != 0.00) {
                                weightDouble = lclBookingPiece.getActualWeightImperial().doubleValue();
                            } else if (lclBookingPiece.getBookedWeightImperial() != null && lclBookingPiece.getBookedWeightImperial().doubleValue() != 0.00) {
                                weightDouble = lclBookingPiece.getBookedWeightImperial().doubleValue();
                            }

                            if (lclBookingPiece.getActualVolumeImperial() != null && lclBookingPiece.getActualVolumeImperial().doubleValue() != 0.00) {
                                weightMeasure = lclBookingPiece.getActualVolumeImperial().doubleValue();
                            } else if (lclBookingPiece.getBookedVolumeImperial() != null && lclBookingPiece.getBookedVolumeImperial().doubleValue() != 0.00) {
                                weightMeasure = lclBookingPiece.getBookedVolumeImperial().doubleValue();
                            }
                        } else if (engmet.equals("M")) {
                            if (lclBookingPiece.getActualWeightMetric() != null && lclBookingPiece.getActualWeightMetric().doubleValue() != 0.00) {
                                weightDouble = lclBookingPiece.getActualWeightMetric().doubleValue();
                            } else if (lclBookingPiece.getBookedWeightMetric() != null && lclBookingPiece.getBookedWeightMetric().doubleValue() != 0.00) {
                                weightDouble = lclBookingPiece.getBookedWeightMetric().doubleValue();
                            }
                            if (lclBookingPiece.getActualVolumeMetric() != null && lclBookingPiece.getActualVolumeMetric().doubleValue() != 0.00) {
                                weightMeasure = lclBookingPiece.getActualVolumeMetric().doubleValue();
                            } else if (lclBookingPiece.getBookedVolumeMetric() != null && lclBookingPiece.getBookedVolumeMetric().doubleValue() != 0.00) {
                                weightMeasure = lclBookingPiece.getBookedVolumeMetric().doubleValue();
                            }
                        }//end of else if engmet
                    }//end of else null

                    //calculate the Total Weight Of Commodities
                    totalWeight = totalWeight + weightDouble;
                    //calculate the Total Measure Of Commodities
                    totalMeasure = totalMeasure + weightMeasure;
                }
            } else {
                lclBookingAc.setRatePerWeightUnit(BigDecimal.ZERO);
                lclBookingAc.setRatePerVolumeUnit(BigDecimal.ZERO);
                lclBookingAc.setRateFlatMinimum(BigDecimal.ZERO);
            }
            //end of for loop
            if (lclBookingAc.getRatePerWeightUnit() != null && lclBookingAc.getRatePerWeightUnit().doubleValue() > 0.00
                    && lclBookingAc.getRatePerVolumeUnit() != null && lclBookingAc.getRatePerVolumeUnit().doubleValue() > 0.00
                    && lclBookingAc.getRateFlatMinimum() != null && lclBookingAc.getRateFlatMinimum().doubleValue() > 0.00) {
                calculatedMeasure = totalMeasure * lclBookingAc.getRatePerVolumeUnit().doubleValue();
                if (lclBookingAc.getRateUom().equalsIgnoreCase("M")) {
                    calculatedWeight = (totalWeight / 1000) * lclBookingAc.getRatePerWeightUnit().doubleValue();
                } else {
                    calculatedWeight = (totalWeight / 100) * lclBookingAc.getRatePerWeightUnit().doubleValue();
                }
                //end of else if engmet
                lclBookingAc.setRatePerUnitDiv(lclBookingAc.getRatePerWeightUnitDiv());
                lclBookingAc.setRatePerVolumeUnitDiv(new BigDecimal(1000));
                if (calculatedWeight >= calculatedMeasure && calculatedWeight >= lclBookingAc.getRateFlatMinimum().doubleValue()) {
                    lclBookingAc.setArAmount(new BigDecimal(calculatedWeight));
                    lclBookingAc.setRatePerUnitUom("W");
                    lclBookingAc.setRatePerUnitDiv(lclBookingAc.getRatePerVolumeUnitDiv());
                } else if (calculatedMeasure >= calculatedWeight && calculatedMeasure >= lclBookingAc.getRateFlatMinimum().doubleValue()) {
                    lclBookingAc.setArAmount(new BigDecimal(calculatedMeasure));
                    lclBookingAc.setRatePerUnitUom("V");
                    lclBookingAc.setRatePerUnitDiv(lclBookingAc.getRatePerVolumeUnitDiv());

                } else {
                    lclBookingAc.setArAmount(lclBookingAc.getRateFlatMinimum());
                    lclBookingAc.setRatePerUnitUom("M");
                }
                //cost amount/costMinimum/costMeasure/costMeasure
            }
            if (CommonUtils.isEmpty(lclBookingAc.getArAmount()) && CommonUtils.isEmpty(lclBookingAc.getRateFlatMinimum())) {
                lclBookingAc.setArAmount(BigDecimal.ZERO);
                lclBookingAc.setRatePerUnitUom("FL");
            }
            lclBookingAc.setBundleIntoOf(false);
            lclBookingAc.setPrintOnBl(true);
            lclBookingAc.setRelsToInv(true);
            lclCostChargesDAO.saveOrUpdate(lclBookingAc);
            List<LclBookingAc> chargeList = lclCostChargesDAO.getLclCostByFileNumberAsc(lclCostAndChargeForm.getFileNumberId(), lclCostAndChargeForm.getModuleName());
            if (CommonUtils.isNotEmpty(lclBookingPiecesList)) {
                LclBookingPiece lclBookingPiece = lclBookingPiecesList.get(0);
                if (lclBookingPiecesList != null && lclBookingPiecesList.size() > 0) {
                    lclBookingPiece = lclBookingPiecesList.get(0);
                    if (lclBookingPiece.getStdchgRateBasis() != null && !lclBookingPiece.getStdchgRateBasis().trim().equals("")) {
                        request.setAttribute("stdchgratebasis", lclBookingPiece.getStdchgRateBasis());
                    }
                }
                request.setAttribute("lclCommodityList", lclBookingPiecesList);
                ofratebasis += lclBookingPiece.getCommodityType().getCode();
                request.setAttribute("ofratebasis", ofratebasis);
                request.setAttribute("origin", origin);
                request.setAttribute("destination", destination);
                String agentNo = "";
                if (!lclCostAndChargeForm.getAgentNo().equals("") && lclCostAndChargeForm.getAgentNo() != null) {
                    agentNo = lclCostAndChargeForm.getAgentNo();
                }
                lclUtils.setWeighMeasureForImportBooking(request, lclBookingPiecesList, null);
                lclUtils.setImportRolledUpChargesForBooking(chargeList, request, lclCostAndChargeForm.getFileNumberId(), lclCostChargesDAO,
                        lclBookingPiecesList, lclBooking.getBillingType(), "", agentNo);
            }
            request.setAttribute("lclBooking", lclBooking);
        } catch (Exception e) {
            return mapping.findForward(CHARGE_DESC);
        }
        return mapping.findForward(CHARGE_DESC);
    }

    public ActionForward addDocumCharge(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            LclCostAndChargeForm costAndChargeForm = (LclCostAndChargeForm) form;
            if (CommonUtils.isNotEmpty(costAndChargeForm.getFileNumberId())) {
                LclCostChargeDAO lclCostChargesDAO = new LclCostChargeDAO();
                Date d = new Date();
                User loginUser = getCurrentUser(request);
                String pooorigin = "", polorigin = "", destinationfd = "", destinationpod = "", chargeCode = "";
                String engmet = new String();
                LclBooking lclBooking = lclBookingDAO.findById(costAndChargeForm.getFileNumberId());
                lclBooking.setDocumentation(true);
                lclBooking.setModifiedBy(loginUser);
                lclBooking.setModifiedDatetime(d);
                lclBookingDAO.saveOrUpdate(lclBooking);
                chargeCode = BLUESCREEN_CHARGECODE_DOCUM;
                LclBookingAc lclBookingAc = new LclCostChargeDAO().findByChargeCode(costAndChargeForm.getFileNumberId(),
                        true, "LCLE", chargeCode);
                if (lclBookingAc == null) {
                    lclBookingAc = new LclBookingAc();
                    lclBookingAc = setInitialValues(lclBookingAc, loginUser);
                    lclBookingAc.setLclFileNumber(lclBooking.getLclFileNumber());
                    lclBookingAc.setAdjustmentAmount(BigDecimal.ZERO);
                    lclBookingAc.setBundleIntoOf(false);
                    lclBookingAc.setPrintOnBl(true);
                }
                List<LclBookingPiece> lclBookingPiecesList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", costAndChargeForm.getFileNumberId());
                GlMapping glmapping = new GlMappingDAO().findByChargeCode(chargeCode, LCL_SHIPMENT_TYPE_EXPORT,
                        TRANSACTION_TYPE_ACCOUNT_RECEIVABLE);
                lclBookingAc.setArglMapping(glmapping);
                lclBookingAc.setApglMapping(glmapping);
                lclBookingAc.setManualEntry(true);
                lclBookingAc.setModifiedBy(loginUser);
                lclBookingAc.setModifiedDatetime(d);
                lclBookingAc.setArBillToParty(costAndChargeForm.getBillingType());
                if (costAndChargeForm.getDollarAmount() != null
                        && costAndChargeForm.getDollarAmount().doubleValue() > 0.00) {
                    lclBookingAc.setRatePerUnit(costAndChargeForm.getDollarAmount());
                    lclBookingAc.setCostFlatrateAmount(costAndChargeForm.getDollarAmount());
                    lclBookingAc.setArAmount(costAndChargeForm.getDollarAmount());
                    lclBookingAc.setRatePerUnitUom("FL");
                }
                //cost amount/costMinimum/costMeasure/costMeasure

                lclCostChargesDAO.saveOrUpdate(lclBookingAc);
                List<LclBookingAc> chargeList = lclCostChargesDAO.getLclCostByFileNumberAsc(costAndChargeForm.getFileNumberId(),
                        "Exports");
                String rateType = "R".equalsIgnoreCase(lclBooking.getRateType()) ? "Y" : lclBooking.getRateType();
                RefTerminalDAO refterminaldao = new RefTerminalDAO();
                if (lclBooking.getPortOfOrigin() != null) {
                    RefTerminal refterminal = refterminaldao.getTerminalByUnLocation(lclBooking.getPortOfOrigin().getUnLocationCode(), rateType);
                    if (refterminal != null && refterminal.getTrmnum() != null && !refterminal.getTrmnum().trim().equals("")) {
                        pooorigin = refterminal.getTrmnum();
                    }
                }
                if (lclBooking.getPortOfLoading() != null) {
                    RefTerminal refterminalpol = refterminaldao.getTerminalByUnLocation(lclBooking.getPortOfLoading().getUnLocationCode(), rateType);
                    if (refterminalpol != null && refterminalpol.getTrmnum() != null && !refterminalpol.getTrmnum().trim().equals("")) {
                        polorigin = refterminalpol.getTrmnum();
                    }
                }
                PortsDAO portsDAO = new PortsDAO();
                if (lclBooking.getPortOfDestination() != null) {
                    Ports portspod = portsDAO.getByProperty("unLocationCode", lclBooking.getPortOfDestination().getUnLocationCode());
                    if (portspod != null && portspod.getEciportcode() != null && !portspod.getEciportcode().trim().equals("")) {
                        destinationpod = portspod.getEciportcode();
                    }
                }
                Ports ports = portsDAO.getByProperty("unLocationCode", lclBooking.getFinalDestination().getUnLocationCode());
                if (ports != null && ports.getEciportcode() != null && !ports.getEciportcode().trim().equals("")) {
                    engmet = ports.getEngmet();
                    destinationfd = ports.getEciportcode();
                }
                lclBookingAc = lclCostChargesDAO.manaualChargeValidate(costAndChargeForm.getFileNumberId(), "CAF", false);
                LclChargesCalculation lclChargesCalculation = new LclChargesCalculation();
                if (lclBookingAc != null) {
                    lclChargesCalculation.calculateCAFCharge(pooorigin, polorigin, destinationfd, destinationpod,
                            lclBookingPiecesList, lclBooking.getBillingType(), loginUser, costAndChargeForm.getFileNumberId(),
                            request, ports, lclBooking.getBillToParty());
                }
                lclUtils.setWeighMeasureForBooking(request, lclBookingPiecesList, ports);
                lclUtils.setRolledUpChargesForBooking(chargeList, request, costAndChargeForm.getFileNumberId(),
                        lclCostChargesDAO, lclBookingPiecesList, costAndChargeForm.getBillToParty(), engmet, "No");
                request.setAttribute("lclBooking", lclBooking);
            }
        } catch (Exception e) {
            log.info("Error in addDocumCharge method. ", e);
            return mapping.findForward(CHARGE_DESC);
        }
        return mapping.findForward(CHARGE_DESC);
    }

    public ActionForward editCharge(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            LclDwr lclDwr = new LclDwr();
            LclCostAndChargeForm lclCostAndChargeForm = (LclCostAndChargeForm) form;
            LclBookingAc lclBookingAc = new LclCostChargeDAO().findById(lclCostAndChargeForm.getLclBookingAc().getId());
            lclCostAndChargeForm.setLclBookingAc(lclBookingAc);
            lclCostAndChargeForm.setFileNumberId(lclBookingAc.getLclFileNumber().getId());
            lclCostAndChargeForm.setChargesCode(lclBookingAc.getArglMapping().getChargeCode());
            lclCostAndChargeForm.setChargesCodeId(lclBookingAc.getArglMapping().getId());
            lclCostAndChargeForm.setBillToParty(lclBookingAc.getArBillToParty());
            if (lclBookingAc.getRatePerUnit() != null) {
                lclCostAndChargeForm.setFlatRateAmount(NumberUtils.convertToTwoDecimal(lclBookingAc.getRatePerUnit().doubleValue()));
            }
            if (lclBookingAc.getCostFlatrateAmount() != null) {
                lclCostAndChargeForm.setCostAmount(NumberUtils.convertToTwoDecimal(lclBookingAc.getCostFlatrateAmount().doubleValue()));
            }
            if (lclBookingAc.getRatePerWeightUnit() != null) {
                lclCostAndChargeForm.setWeight(NumberUtils.convertToTwoDecimal(lclBookingAc.getRatePerWeightUnit().doubleValue()));
            }
            if (lclBookingAc.getRatePerVolumeUnit() != null) {
                lclCostAndChargeForm.setMeasure(NumberUtils.convertToTwoDecimal(lclBookingAc.getRatePerVolumeUnit().doubleValue()));
            }
            if (lclBookingAc.getArAmount() != null) {
                lclCostAndChargeForm.setArAmount(NumberUtils.convertToTwoDecimal(lclBookingAc.getArAmount().doubleValue()));
                lclCostAndChargeForm.setFlatRateAmount(NumberUtils.convertToTwoDecimal(lclBookingAc.getArAmount().doubleValue()));
            }
            if (lclBookingAc.getRateFlatMinimum() != null) {
                lclCostAndChargeForm.setMinimum(NumberUtils.convertToTwoDecimal(lclBookingAc.getRateFlatMinimum().doubleValue()));
            }
            if (lclBookingAc.getCostWeight() != null && !lclBookingAc.getCostWeight().toString().trim().equals("")) {
                lclCostAndChargeForm.setWeightForCost(NumberUtils.convertToTwoDecimal(lclBookingAc.getCostWeight().doubleValue()));
            }
            if (lclBookingAc.getCostMeasure() != null) {
                lclCostAndChargeForm.setMeasureForCost(NumberUtils.convertToTwoDecimal(lclBookingAc.getCostMeasure().doubleValue()));
            }
            String cfsWarhouseNo = request.getParameter("cfsWarehouseNo");
            String[] AccValues = warehouseDAO.getWarehouseAccountNo(cfsWarhouseNo);
            if (AccValues[1] != null && !AccValues[1].toString().trim().equals("")) {
                String cfAcctNo = AccValues[1].toString();
                request.setAttribute("cfAcctNo", cfAcctNo);
            }
            request.setAttribute("lclBookingAc", lclBookingAc);
            request.setAttribute("lclCostAndChargeForm", lclCostAndChargeForm);
            request.setAttribute("fileNumber", request.getParameter("fileNumber"));
            request.setAttribute("chargeCode", lclBookingAc.getArglMapping().getChargeCode());
            request.setAttribute("bookingAcId", lclCostAndChargeForm.getLclBookingAc().getId());
            if (lclBookingAc.getCostMinimum() != null && !lclBookingAc.getCostMinimum().toString().equals("0.00")) {
                //lclCostAndChargeForm.setCostAmount("0.00");
                lclCostAndChargeForm.setMinimumForCost(NumberUtils.convertToTwoDecimal(lclBookingAc.getCostMinimum().doubleValue()));
            }
            if (lclBookingAc.getArAmount() != null && !lclBookingAc.getArAmount().toString().equals("0.00")) {
                request.setAttribute("buttonValue", "addCharge");
            } else {
                request.setAttribute("buttonValue", "addCost");
            }
            String shipmentType = lclCostAndChargeForm.getModuleName().equalsIgnoreCase("Exports") ? "LCLE" : "LCLI";
            String[] validateCode = lclDwr.chargeCostValidate(lclBookingAc.getArglMapping().getChargeCode(), shipmentType);
            if (validateCode[1].equalsIgnoreCase("N")) {
                request.setAttribute("disableCost", "disableCostButton");
            }
            if (lclCostAndChargeForm.getModuleName().equalsIgnoreCase("Exports")) {
                request.setAttribute("billToPartyList", new LclUtils().getBillingTypeByLclE(lclCostAndChargeForm.getBkgBillToParty(), lclCostAndChargeForm.getBkgBillingType()));
            }
        } catch (Exception e) {
            return mapping.findForward("success");
        }
        return mapping.findForward("success");
    }

    public ActionForward editInvoiceCharge(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {//remove this method
            LclCostAndChargeForm lclCostAndChargeForm = (LclCostAndChargeForm) form;
            LclBookingAc lclBookingAc = new LclCostChargeDAO().findById(lclCostAndChargeForm.getLclBookingAc().getId());
            lclCostAndChargeForm.setLclBookingAc(lclBookingAc);
            lclCostAndChargeForm.setFileNumberId(lclBookingAc.getLclFileNumber().getId());
            lclCostAndChargeForm.setChargesCode(lclBookingAc.getArglMapping().getChargeCode());
            lclCostAndChargeForm.setChargesCodeId(lclBookingAc.getArglMapping().getId());
            if (lclBookingAc.getRatePerUnit() != null) {
                lclCostAndChargeForm.setFlatRateAmount(NumberUtils.convertToTwoDecimal(lclBookingAc.getRatePerUnit().doubleValue()));
            }
            if (lclBookingAc.getRatePerWeightUnit() != null) {
                lclCostAndChargeForm.setWeight(NumberUtils.convertToTwoDecimal(lclBookingAc.getRatePerWeightUnit().doubleValue()));
            }
            if (lclBookingAc.getRatePerVolumeUnit() != null) {
                lclCostAndChargeForm.setMeasure(NumberUtils.convertToTwoDecimal(lclBookingAc.getRatePerVolumeUnit().doubleValue()));
            }
            if (lclBookingAc.getArAmount() != null) {
                lclCostAndChargeForm.setArAmount(NumberUtils.convertToTwoDecimal(lclBookingAc.getArAmount().doubleValue()));
            }
            if (lclBookingAc.getRateFlatMinimum() != null) {
                lclCostAndChargeForm.setMinimum(NumberUtils.convertToTwoDecimal(lclBookingAc.getRateFlatMinimum().doubleValue()));
            }
            request.setAttribute("lclBookingAc", lclBookingAc);
            request.setAttribute("lclCostAndChargeForm", lclCostAndChargeForm);
            request.setAttribute("fileNumber", request.getParameter("fileNumber"));
            request.setAttribute("chargeCode", lclBookingAc.getArglMapping().getChargeCode());
            request.setAttribute("bookingAcId", lclCostAndChargeForm.getLclBookingAc().getId());
        } catch (Exception e) {
            return mapping.findForward(CHARGE_INVOICE);
        }
        return mapping.findForward(CHARGE_INVOICE);
    }

    public ActionForward addSpotRate(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            LclCostAndChargeForm chargeForm = (LclCostAndChargeForm) form;
            String CFT = chargeForm.getRate();
            String CBM = chargeForm.getRateN();
            Boolean spotCheckBottom = chargeForm.getSpotCheckBottom();
            Boolean isOnlyOcnfrt = chargeForm.getSpotCheckOF();
            String spotComment = chargeForm.getComment();
            LclCostChargeDAO lclCostChargesDAO = new LclCostChargeDAO();
            String billingType = request.getParameter("billingType");
            LclBooking lclBooking = lclBookingDAO.findById(chargeForm.getFileNumberId());
            lclBookingDAO.getCurrentSession().evict(lclBooking);
            String rateType = lclBooking.getRateType();
            if (rateType.equalsIgnoreCase("R")) {
                rateType = "Y";
            }
            String engmet = "";
            Ports ports = new PortsDAO().getByProperty("unLocationCode", lclBooking.getFinalDestination().getUnLocationCode());
            if (ports != null && ports.getEciportcode() != null && !ports.getEciportcode().trim().equals("")) {
                engmet = ports.getEngmet();
            }
            List<LclBookingPiece> lclBookingPiecesList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", chargeForm.getFileNumberId());
            MessageResources messageResources = getResources(request);
            String spotRateCommodity = messageResources.getMessage("application.spotRate.commodityCode");
            new ExportBookingUtils().calculateSpotRate(chargeForm.getFileNumberId(), lclBooking, billingType,
                    CBM, CFT, rateType, isOnlyOcnfrt, spotCheckBottom, spotComment, spotRateCommodity,
                    request, lclBookingPiecesList, engmet);
            refreshRates(chargeForm.getFileNumberId(), billingType, engmet, ports, lclBookingPiecesList,
                    lclCostChargesDAO, request, chargeForm.getModuleName());
        } catch (Exception e) {
            log.info("Error in LclCostAndChargeAction Class method in addSpotRate" + new Date(), e);
            return mapping.findForward(CHARGE_DESC);
        }
        return mapping.findForward(CHARGE_DESC);
    }

    private void refreshRates(Long fileNumberId, String billingType,
            String engmet, Ports ports, List<LclBookingPiece> lclBookingPiecesList, LclCostChargeDAO lclCostChargesDAO,
            HttpServletRequest request, String moduleName) throws Exception {
        List<LclBookingAc> chargeList = new LclCostChargeDAO().getLclCostByFileNumberAsc(fileNumberId, moduleName);
        if (CommonUtils.isNotEmpty(lclBookingPiecesList)) {
            lclUtils.setWeighMeasureForBooking(request, lclBookingPiecesList, ports);
        }
        lclUtils.setRolledUpChargesForBooking(chargeList, request, fileNumberId, lclCostChargesDAO, lclBookingPiecesList, billingType, engmet, "No");
    }

    public ActionForward refreshCommodity(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fileNumberId = request.getParameter("fileNumberId");
        request.setAttribute("lclCommodityList", new LclBookingPieceDAO().findByProperty("lclFileNumber.id", Long.parseLong(fileNumberId)));
        LclBooking lclBooking = new LCLBookingDAO().getByProperty("lclFileNumber.id", Long.parseLong(fileNumberId));
        request.setAttribute("ofspotrate", lclBooking.getSpotRate());
        return mapping.findForward(COMMODITY_DESC);
    }

    public ActionForward updateAutoChargesInImp(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclCostAndChargeForm lclCostAndChargeForm = (LclCostAndChargeForm) form;
        LclCostChargeDAO lclCostChargesDAO = new LclCostChargeDAO();
        LclBookingAc lclBookingAc = new LclCostChargeDAO().findById(Long.parseLong(lclCostAndChargeForm.getId()));
        if (lclBookingAc != null) {
            lclBookingAc.setArBillToParty(lclCostAndChargeForm.getBillToParty());
            lclCostChargesDAO.update(lclBookingAc);
        }
        String agentNo = "";
        if (!lclCostAndChargeForm.getAgentNo().equals("") && lclCostAndChargeForm.getAgentNo() != null) {
            agentNo = lclCostAndChargeForm.getAgentNo();
        }
        List<LclBookingPiece> lclBookingPiecesList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", lclCostAndChargeForm.getFileNumberId());
        List<LclBookingAc> chargeList = lclCostChargesDAO.getLclCostByFileNumberAsc(lclCostAndChargeForm.getFileNumberId(), lclCostAndChargeForm.getModuleName());
        if (CommonUtils.isNotEmpty(lclBookingPiecesList)) {
            LclBooking lclBooking = lclBookingDAO.findById(lclCostAndChargeForm.getFileNumberId());
            lclUtils.setWeighMeasureForImportBooking(request, lclBookingPiecesList, null);
            lclUtils.setImportRolledUpChargesForBooking(chargeList, request, lclCostAndChargeForm.getFileNumberId(), lclCostChargesDAO,
                    lclBookingPiecesList, lclBooking.getBillingType(), "", agentNo);
        }
        return mapping.findForward(CHARGE_DESC);
    }

    public ActionForward createImpCorrection(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclCostAndChargeForm lclCostAndChargeForm = (LclCostAndChargeForm) form;
        LclCostChargeDAO lclCostChargesDAO = new LclCostChargeDAO();
        LCLCorrectionDAO lclCorrectionDAO = new LCLCorrectionDAO();
        LCLCorrectionChargeDAO lclCorrectionChargeDAO = new LCLCorrectionChargeDAO();
        LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        LclManifestDAO lclManifestDAO = new LclManifestDAO();
        CodetypeDAO codeTypeDAO = new CodetypeDAO();
        LclBookingAc lclBookingAc = lclCostChargesDAO.findById(Long.parseLong(lclCostAndChargeForm.getId()));
        lclBookingAc.setArBillToParty(lclCostAndChargeForm.getBillToParty());
        lclBookingAc.setModifiedBy(getCurrentUser(request));
        lclBookingAc.setModifiedDatetime(new Date());
        lclCorrectionDAO.saveOrUpdate(lclBookingAc);
        LclBooking lclBooking = lclBookingDAO.getByProperty("lclFileNumber.id", lclCostAndChargeForm.getFileNumberId());
        String vendorNo = "";
        if ("C".equalsIgnoreCase(lclBooking.getBillToParty())) {
            vendorNo = null != lclBooking.getConsAcct() ? lclBooking.getConsAcct().getAccountno() : "";
        } else if ("N".equalsIgnoreCase(lclBooking.getBillToParty())) {
            vendorNo = null != lclBooking.getNotyAcct() ? lclBooking.getNotyAcct().getAccountno() : "";
        } else {
            vendorNo = null != lclBooking.getThirdPartyAcct() ? lclBooking.getThirdPartyAcct().getAccountno() : "";
        }
        Integer codeTypeId = codeTypeDAO.getCodeTypeId(CODE_TYPE_DESCRIPTION_BL_CORRECTION);
        Integer codeId = codeTypeDAO.getCodeTypeId(CODE_TYPE_DESCRIPTION_BL_CORRECTION_CODE);
        String cnType = genericCodeDAO.getByCodeAndCodetypeId(String.valueOf(codeTypeId), GENERIC_CODE_Y_BL_CORRECTION, "id");
        String cnCode = genericCodeDAO.getByCodeAndCodetypeId(String.valueOf(codeId), GENERIC_CODE_A_CORRECTION_IMPORTS, "id");
        BigInteger lastCorrectionNo = lclCorrectionDAO.getIntegerDescByFileIdWithoutVoid(lclBooking.getLclFileNumber().getId(), "correction_no");
        String oldAmt = "";
        if (lastCorrectionNo == null) {
            lastCorrectionNo = new BigInteger("0");
        }
        oldAmt = "0.00";
        String cnFileNo = "(" + lclBooking.getLclFileNumber().getFileNumber() + "-C-" + String.valueOf(lastCorrectionNo.intValue() + 1) + ")";
        lclCorrectionDAO.insertCorrections(lclBooking.getLclFileNumber().getId(), Integer.parseInt(cnType), Integer.parseInt(cnCode),
                vendorNo, lastCorrectionNo.intValue() + 1, "QUICK CN", "A", null, null, getCurrentUser(request).getUserId(), 0, lclBooking.getBillToParty());
        String correction = CommonConstants.getEventMap().get(GENERIC_EVENT_CODE_CORRECTION_NOTES);
        StringBuilder savedCnNotes = new StringBuilder();
        savedCnNotes.append(correction).append(" ").append(cnFileNo).append(" ").append("Saved");
        lclRemarksDAO.insertLclRemarks(lclBooking.getLclFileNumber().getId(), REMARKS_TYPE_LCL_CORRECTIONS, savedCnNotes.toString(), getCurrentUser(request).getUserId());
        BigInteger cnId = lclCorrectionDAO.getIntegerDescByFileIdWithoutVoid(lclBooking.getLclFileNumber().getId(), "id");
        lclCorrectionChargeDAO.insertCorrectionCharge(cnId.longValue(), oldAmt,
                String.valueOf(lclBookingAc.getArAmount()), lclBookingAc.getArglMapping().getId(), lclBookingAc.getArBillToParty(), getCurrentUser(request).getUserId());
        lclCorrectionDAO.approveCorrections(cnId.longValue(), getCurrentUser(request).getUserId(), "A");
        LclCorrection lclCorrection = lclCorrectionDAO.findById(cnId.longValue());
        lclManifestDAO.createLclCorrections(LCL_IMPORT, getCurrentUser(request), true, lclCorrection, true, lclBooking);
        StringBuilder postCnNotes = new StringBuilder();
        postCnNotes.append(correction).append(" ").append(cnFileNo).append(" ").append("is got Approved and Post");
        lclRemarksDAO.insertLclRemarks(lclBooking.getLclFileNumber().getId(), REMARKS_TYPE_LCL_CORRECTIONS, postCnNotes.toString(), getCurrentUser(request).getUserId());
        List<LclBookingPiece> lclBookingPiecesList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", lclCostAndChargeForm.getFileNumberId());
        List<LclBookingAc> chargeList = lclCostChargesDAO.getLclCostByFileNumberAsc(lclCostAndChargeForm.getFileNumberId(), lclCostAndChargeForm.getModuleName());
        if (CommonUtils.isNotEmpty(lclBookingPiecesList)) {
            lclUtils.setWeighMeasureForImportBooking(request, lclBookingPiecesList, null);
            lclUtils.setImportRolledUpChargesForBooking(chargeList, request, lclCostAndChargeForm.getFileNumberId(), lclCostChargesDAO,
                    lclBookingPiecesList, lclBooking.getBillingType(), "", "");
        }
        request.setAttribute("lclBooking", lclBooking);
        request.setAttribute("chargeList", chargeList);
        return mapping.findForward(CHARGE_DESC);
    }

    //Adding DestinationServices for Lcl Exports
    public ActionForward displayDestinationServices(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclCostAndChargeForm lclCostAndChargeForm = (LclCostAndChargeForm) form;
        PropertyDAO property = new PropertyDAO();
        String buttonValue = null != request.getParameter("buttonValue") ? request.getParameter("buttonValue") : "";
        GlMappingDAO glMappingDAO = new GlMappingDAO();
        if ("editService".equalsIgnoreCase(buttonValue)) {
            request.setAttribute("buttonValue", buttonValue);
            LclBookingAc lclBookingAc = new LclCostChargeDAO().findById(Long.parseLong(lclCostAndChargeForm.getId()));
            if (null != lclBookingAc) {
                lclCostAndChargeForm.setLclBookingAc(lclBookingAc);
                lclCostAndChargeForm.setFileNumberId(lclBookingAc.getLclFileNumber().getId());
                lclCostAndChargeForm.setChargesCode(lclBookingAc.getArglMapping().getChargeCode());
                lclCostAndChargeForm.setChargesCodeId(lclBookingAc.getArglMapping().getId());
                lclCostAndChargeForm.setBillToParty(lclBookingAc.getArBillToParty());
                lclCostAndChargeForm.setModuleName(LCL_EXPORT);
                lclCostAndChargeForm.setId(lclBookingAc.getId().toString());

                if (lclBookingAc.getRatePerUnit() != null) {
                    lclCostAndChargeForm.setFlatRateAmount(NumberUtils.convertToTwoDecimal(lclBookingAc.getRatePerUnit().doubleValue()));
                }
                if (lclBookingAc.getCostFlatrateAmount() != null) {
                    lclCostAndChargeForm.setCostAmount(NumberUtils.convertToTwoDecimal(lclBookingAc.getCostFlatrateAmount().doubleValue()));
                }
                if (lclBookingAc.getRatePerWeightUnit() != null) {
                    lclCostAndChargeForm.setWeight(NumberUtils.convertToTwoDecimal(lclBookingAc.getRatePerWeightUnit().doubleValue()));
                }
                if (lclBookingAc.getRatePerVolumeUnit() != null) {
                    lclCostAndChargeForm.setMeasure(NumberUtils.convertToTwoDecimal(lclBookingAc.getRatePerVolumeUnit().doubleValue()));
                }
                if (lclBookingAc.getArAmount() != null) {
                    lclCostAndChargeForm.setArAmount(NumberUtils.convertToTwoDecimal(lclBookingAc.getArAmount().doubleValue()));
                }
                if (lclBookingAc.getRateFlatMinimum() != null) {
                    lclCostAndChargeForm.setMinimum(NumberUtils.convertToTwoDecimal(lclBookingAc.getRateFlatMinimum().doubleValue()));
                }
                if (lclBookingAc.getCostWeight() != null && !lclBookingAc.getCostWeight().toString().trim().equals("")) {
                    lclCostAndChargeForm.setWeightForCost(NumberUtils.convertToTwoDecimal(lclBookingAc.getCostWeight().doubleValue()));
                }
                if (lclBookingAc.getCostMeasure() != null) {
                    lclCostAndChargeForm.setMeasureForCost(NumberUtils.convertToTwoDecimal(lclBookingAc.getCostMeasure().doubleValue()));
                }
                if (lclBookingAc.getCostMinimum() != null) {
                    lclCostAndChargeForm.setMinimumForCost(NumberUtils.convertToTwoDecimal(lclBookingAc.getCostMinimum().doubleValue()));
                }

                if (null != lclBookingAc.getLclFileNumber().getLclBooking()
                        && lclBookingAc.getArglMapping().getChargeCode().equalsIgnoreCase("ONCARR")) {
                    lclCostAndChargeForm.setTotaldestCarriageTT(lclBookingAc.getLclFileNumber().getLclBooking().getPodfdtt());
                }
                LclBookingDestinationServices destService = new LclCostChargeDAO().getDestinationCharge(lclBookingAc.getId(), lclBookingAc.getLclFileNumber().getId());
                if (null != destService) {
                    lclCostAndChargeForm.setCityName(destService.getCity());
                    if (destService.getOncarriageAcctNo() != null) {
                        lclCostAndChargeForm.setAlternateAgentAccntNo(destService.getOncarriageAcctNo().getAccountno());
                        lclCostAndChargeForm.setAlternateAgent(destService.getOncarriageAcctNo().getAccountName());
                    }
                    lclCostAndChargeForm.setTotalDestTT(destService.getOncarriageTt());
                    lclCostAndChargeForm.setDestFrequency(destService.getOncarriageTtFreq());
                    lclCostAndChargeForm.setDestinatnSersRemark(destService.getRemarks());
                }
                request.setAttribute("lclBookingAc", lclBookingAc);
                request.setAttribute("chargeCode", lclBookingAc.getArglMapping().getChargeCode());
            }
        } else if ("deleteService".equalsIgnoreCase(buttonValue)) {
            deleteDestinationCharge(lclCostAndChargeForm, request);
        }
        String destination = null != request.getParameter("destination") ? request.getParameter("destination") : lclCostAndChargeForm.getDestination();
        Ports ports = new PortsDAO().getByProperty("unLocationCode", destination);
        List<LclBookingPiece> lclBookingPiecesList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", lclCostAndChargeForm.getFileNumberId());
        List<LclBookingAc> destinationchargeList = new LclCostChargeDAO().getAllDestinationChargeList(lclCostAndChargeForm.getFileNumberId());
        new LclUtils().setRolledUpChargesForBooking(destinationchargeList, request, lclCostAndChargeForm.getFileNumberId(), new LclCostChargeDAO(),
                lclBookingPiecesList, null, ports.getEngmet(), "No");

        request.setAttribute("profit", property.getProperty("Destination Services DAP/DDP/Delivery Profit (%)"));
        request.setAttribute("sellMin", property.getProperty("Destination Services DAP/DDP/Delivery min profit"));
        request.setAttribute("sellMax", property.getProperty("Destination Services DAP/DDP/Delivery max profit"));
        request.setAttribute("oc_min_profit", property.getProperty("Destination Services O/C Min Profit"));
        request.setAttribute("oc_max_profit", property.getProperty("Destination Services O/C Max Profit"));
        request.setAttribute("onCarrigeMarkUp", property.getProperty("Destination Services OnCarriage W/M Markup"));
        request.setAttribute("DTHC_currency_adj", property.getProperty("Destination Services DTHC Currency adjustment (%)"));
        request.setAttribute("lclCostAndChargeForm", lclCostAndChargeForm);
        request.setAttribute("fileNumber", request.getParameter("fileNumber"));
        request.setAttribute("chargeCodeList", glMappingDAO.getchargeCodeListForDestinationServices());
        return mapping.findForward("displayDestinationServices");
    }

    public void setDestinationServices(LclCostAndChargeForm chargeForm,
            LclBookingAc lclBookingAc, HttpServletRequest request) throws Exception {
        BaseHibernateDAO<LclBookingDestinationServices> destinationDAO = new BaseHibernateDAO<LclBookingDestinationServices>();
        LclBookingDestinationServices destinationServices = null;
        User user = getCurrentUser(request);
        if (chargeForm.getLclBookingAc() != null) {
            destinationServices = new LclCostChargeDAO().getDestinationCharge(lclBookingAc.getId(), lclBookingAc.getLclFileNumber().getId());
            if (destinationServices == null) {
                destinationServices = new LclBookingDestinationServices();
                destinationServices.setEnteredBy(user);
                destinationServices.setEnteredDatetime(new Date());
            }
            destinationServices.setLclFileNumber(lclBookingAc.getLclFileNumber());
            destinationServices.setLclbookingAc(lclBookingAc);
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
    //Check if Charge code exist for same bill to party

    public boolean checkChargeForBillToParty(String chargeCode, String billtoParty,
            String fileNumberId, String moduleName) {
        try {
            Long fileNumber = Long.parseLong(fileNumberId);
            List<LclBookingAc> chargeList = new LclCostChargeDAO().getLclCostByFileNumberAsc(fileNumber, moduleName);
            for (LclBookingAc bookingAc : chargeList) {
                if (chargeCode.equalsIgnoreCase(bookingAc.getArglMapping().getChargeCode()) && billtoParty.equalsIgnoreCase(bookingAc.getArBillToParty())) {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public ActionForward addManualChargesForExport(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            LclCostAndChargeForm chargeForm = (LclCostAndChargeForm) form;
            LclCostChargeDAO chargeDAO = new LclCostChargeDAO();
            LclBookingAc lclBookingAc = chargeForm.getLclBookingAc();
            GlMappingDAO glMappingDAO = new GlMappingDAO();
            String billingType = request.getParameter("billingType");
            String billToParty = request.getParameter("billToParty");
            User loginUser = getCurrentUser(request);
            Double totalWeight = 0.00, totalMeasure = 0.00, calculatedWeight = 0.00, calculatedMeasure = 0.00;
            String pooTrmnum = "", polTrmnum = "", podEciPortCode = "", fdEciPortCode = "", fdEngmet = "";
            List<LclBookingPiece> lclBookingPiecesList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", chargeForm.getFileNumberId());
            LclBooking lclBooking = lclBookingDAO.findById(chargeForm.getFileNumberId());
            String pooUnloCode = lclBooking.getPortOfOrigin() != null ? lclBooking.getPortOfOrigin().getUnLocationCode() : "";
            String polUnloCode = lclBooking.getPortOfLoading() != null ? lclBooking.getPortOfLoading().getUnLocationCode() : "";
            String podUnloCode = lclBooking.getPortOfDestination() != null ? lclBooking.getPortOfDestination().getUnLocationCode() : "";
            String fdUnloCode = lclBooking.getFinalDestination() != null ? lclBooking.getFinalDestination().getUnLocationCode() : "";
            String rateType = "R".equalsIgnoreCase(lclBooking.getRateType()) ? "Y" : lclBooking.getRateType();
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
                    fdEngmet = col[1] != null ? (String) col[1] : "";
                }
            }

            if (CommonUtils.isNotEmpty(chargeForm.getFileNumberId())) {
                lclBookingAc.setLclFileNumber(lclBooking.getLclFileNumber());
            }
            GlMapping arGlmapping = glMappingDAO.findByChargeCode(chargeForm.getChargesCode(),
                    LCL_SHIPMENT_TYPE_EXPORT, ConstantsInterface.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE);
            GlMapping apGlmapping = glMappingDAO.findByChargeCode(chargeForm.getChargesCode(),
                    LCL_SHIPMENT_TYPE_EXPORT, ConstantsInterface.TRANSACTION_TYPE_ACCRUALS);
            if (CommonUtils.in(chargeForm.getChargesCode(), "DTHC PREPAID", "ONCARR", "DELIV", "DDP", "DAP")) {
                arGlmapping = glMappingDAO.findByDestinationChargeCode(chargeForm.getChargesCode(),
                        LCL_SHIPMENT_TYPE_EXPORT, ConstantsInterface.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE);
//                apGlmapping = glMappingDAO.findByDestinationChargeCode(chargeForm.getChargesCode(),
//                        LCL_SHIPMENT_TYPE_EXPORT, ConstantsInterface.TRANSACTION_TYPE_ACCRUALS);
            }
            lclBookingAc.setApglMapping(apGlmapping);
            if (arGlmapping != null) {
                lclBookingAc.setArglMapping(arGlmapping);
            } else {
                lclBookingAc.setArglMapping(apGlmapping);
            }

            lclBookingAc.setArBillToParty(CommonUtils.isNotEmpty(chargeForm.getBillToParty())
                    ? chargeForm.getBillToParty() : CommonUtils.isNotEmpty(billToParty) ? billToParty : null);
            lclBookingAc.setApBillToParty(CommonUtils.isNotEmpty(chargeForm.getBillToParty())
                    ? chargeForm.getBillToParty() : CommonUtils.isNotEmpty(billToParty) ? billToParty : null);

            lclBookingAc.setRelsToInv(CommonUtils.isNotEmpty(chargeForm.getBillToParty())
                    && chargeForm.getBillToParty().equalsIgnoreCase("A") ? true : false);
            lclBookingAc.setTransDatetime(new Date());
            lclBookingAc.setEnteredBy(getCurrentUser(request));
            lclBookingAc.setModifiedBy(getCurrentUser(request));
            lclBookingAc.setEnteredDatetime(new Date());
            lclBookingAc.setModifiedDatetime(new Date());
            lclBookingAc.setManualEntry(true);
            lclBookingAc.setAdjustmentAmount(BigDecimal.ZERO);

            // calculation starts here for cost and charge .
            lclBookingAc.setRatePerWeightUnit(CommonUtils.isNotEmpty(chargeForm.getWeight())
                    ? new BigDecimal(chargeForm.getWeight()) : BigDecimal.ZERO);
            lclBookingAc.setRatePerVolumeUnit(CommonUtils.isNotEmpty(chargeForm.getMeasure())
                    ? new BigDecimal(chargeForm.getMeasure()) : BigDecimal.ZERO);

            if (lclBookingAc.getRatePerWeightUnit().doubleValue() > 0.00 || lclBookingAc.getRatePerVolumeUnit().doubleValue() > 0.00) {
                if (CommonUtils.isNotEmpty(lclBookingPiecesList)) {
                    for (LclBookingPiece piece : lclBookingPiecesList) {
                        Double weightDouble = 0.00;
                        Double weightMeasure = 0.00;
                        if (null != lclBookingAc.getRateUom()) {
                            if (("I").equalsIgnoreCase(lclBookingAc.getRateUom())) {
                                if (piece.getActualWeightImperial() != null && piece.getActualWeightImperial().doubleValue() != 0.00) {
                                    weightDouble = piece.getActualWeightImperial().doubleValue();
                                } else if (piece.getBookedWeightImperial() != null && piece.getBookedWeightImperial().doubleValue() != 0.00) {
                                    weightDouble = piece.getBookedWeightImperial().doubleValue();
                                }
                                if (piece.getActualVolumeImperial() != null && piece.getActualVolumeImperial().doubleValue() != 0.00) {
                                    weightMeasure = piece.getActualVolumeImperial().doubleValue();
                                } else if (piece.getBookedVolumeImperial() != null && piece.getBookedVolumeImperial().doubleValue() != 0.00) {
                                    weightMeasure = piece.getBookedVolumeImperial().doubleValue();
                                }
                            } else if (("M").equalsIgnoreCase(lclBookingAc.getRateUom())) {
                                if (piece.getActualWeightMetric() != null && piece.getActualWeightMetric().doubleValue() != 0.00) {
                                    weightDouble = piece.getActualWeightMetric().doubleValue();
                                } else if (piece.getBookedWeightMetric() != null && piece.getBookedWeightMetric().doubleValue() != 0.00) {
                                    weightDouble = piece.getBookedWeightMetric().doubleValue();//kgs
                                }
                                if (piece.getActualVolumeMetric() != null && piece.getActualVolumeMetric().doubleValue() != 0.00) {
                                    weightMeasure = piece.getActualVolumeMetric().doubleValue();
                                } else if (piece.getBookedVolumeMetric() != null && piece.getBookedVolumeMetric().doubleValue() != 0.00) {
                                    weightMeasure = piece.getBookedVolumeMetric().doubleValue();//cbm
                                }
                            }

                            totalWeight = totalWeight + weightDouble;
                            totalMeasure = totalMeasure + weightMeasure;
                        }
                    }
                }
                if (fdEngmet.equals("E")) {
                    if (lclBookingAc.getRateUom().equalsIgnoreCase("M")) {
                        calculatedWeight = (totalWeight / 100) * lclUtils.convertToLbs(lclBookingAc.getRatePerWeightUnit().doubleValue()).doubleValue();
                        calculatedMeasure = totalMeasure * lclUtils.convertToCft(lclBookingAc.getRatePerVolumeUnit().doubleValue()).doubleValue();
                    } else {
                        calculatedWeight = (totalWeight / 100) * lclBookingAc.getRatePerWeightUnit().doubleValue();
                        calculatedMeasure = totalMeasure * lclBookingAc.getRatePerVolumeUnit().doubleValue();
                    }
                    lclBookingAc.setRatePerWeightUnitDiv(new BigDecimal(100));
                } else if (fdEngmet.equals("M")) {
                    if (lclBookingAc.getRateUom().equalsIgnoreCase("I")) {
                        calculatedWeight = (totalWeight / 1000) * lclUtils.convertToKgs(lclBookingAc.getRatePerWeightUnit().doubleValue()).doubleValue();
                        calculatedMeasure = totalMeasure * lclUtils.convertToCbm(lclBookingAc.getRatePerVolumeUnit().doubleValue()).doubleValue();
                    } else {
                        calculatedWeight = (totalWeight / 1000) * lclBookingAc.getRatePerWeightUnit().doubleValue();
                        calculatedMeasure = totalMeasure * lclBookingAc.getRatePerVolumeUnit().doubleValue();
                    }
                    lclBookingAc.setRatePerWeightUnitDiv(new BigDecimal(1000));
                }
                //end of else if engmet

                lclBookingAc.setRatePerUnitDiv(lclBookingAc.getRatePerWeightUnitDiv());
                lclBookingAc.setRatePerVolumeUnitDiv(new BigDecimal(1000));
            }

            if (chargeForm.getFlatRateAmount() != null && !chargeForm.getFlatRateAmount().trim().equals("")) {
                lclBookingAc.setRatePerUnit(new BigDecimal(chargeForm.getFlatRateAmount()));
                lclBookingAc.setArAmount(new BigDecimal(chargeForm.getFlatRateAmount()));
            } else {
                lclBookingAc.setArAmount(BigDecimal.ZERO);
                lclBookingAc.setRatePerUnit(BigDecimal.ZERO);
            }

            lclBookingAc.setRateFlatMinimum(CommonUtils.isNotEmpty(chargeForm.getMinimum())
                    ? new BigDecimal(chargeForm.getMinimum()) : BigDecimal.ZERO);

            double calculateChargeWeiMea = 0.00;

            if (calculatedWeight >= calculatedMeasure && calculatedWeight >= lclBookingAc.getRateFlatMinimum().doubleValue()) {
                lclBookingAc.setRatePerUnitUom("W");
                lclBookingAc.setRatePerUnitDiv(lclBookingAc.getRatePerVolumeUnitDiv());
                calculateChargeWeiMea = calculatedWeight;
            } else if (calculatedMeasure >= calculatedWeight && calculatedMeasure >= lclBookingAc.getRateFlatMinimum().doubleValue()) {
                lclBookingAc.setRatePerUnitUom("V");
                lclBookingAc.setRatePerUnitDiv(lclBookingAc.getRatePerVolumeUnitDiv());
                calculateChargeWeiMea = calculatedMeasure;
            } else {
                lclBookingAc.setRatePerUnitUom("M");
                calculateChargeWeiMea = lclBookingAc.getRateFlatMinimum().doubleValue();
            }

            if (lclBookingAc.getRatePerUnit().doubleValue() > 0.00 && lclBookingAc.getRatePerWeightUnit().doubleValue() == 0.00
                    && lclBookingAc.getRatePerVolumeUnit().doubleValue() == 0.00) {
                lclBookingAc.setRatePerUnitUom("FL");
            }

            if (null != lclBookingAc.getArAmount()) {
                calculateChargeWeiMea = calculateChargeWeiMea + lclBookingAc.getArAmount().doubleValue();
                lclBookingAc.setArAmount(new BigDecimal(calculateChargeWeiMea));
            }

            lclBookingAc.setCostMeasure(CommonUtils.isNotEmpty(chargeForm.getMeasureForCost())
                    ? new BigDecimal(chargeForm.getMeasureForCost()) : BigDecimal.ZERO);
            lclBookingAc.setCostWeight(CommonUtils.isNotEmpty(chargeForm.getWeightForCost())
                    ? new BigDecimal(chargeForm.getWeightForCost()) : BigDecimal.ZERO);
            lclBookingAc.setApAmount(CommonUtils.isNotEmpty(chargeForm.getCostAmount())
                    ? new BigDecimal(chargeForm.getCostAmount()) : BigDecimal.ZERO);
            lclBookingAc.setCostFlatrateAmount(lclBookingAc.getApAmount());
            lclBookingAc.setCostMinimum(CommonUtils.isNotEmpty(chargeForm.getMinimumForCost())
                    ? new BigDecimal(chargeForm.getMinimumForCost()) : BigDecimal.ZERO);

            double calculatedCostWeight = 0.00, calculatedCostMeasure = 0.00, calculateCostWeiMea = 0.00;

            if (lclBookingAc.getApAmount().doubleValue() > 0.00 || lclBookingAc.getCostWeight().doubleValue() > 0.00
                    || lclBookingAc.getCostMeasure().doubleValue() > 0.00) {
                if (fdEngmet.equals("E")) {
                    if (lclBookingAc.getRateUom().equalsIgnoreCase("M")) {
                        calculatedCostWeight = (totalWeight / 100) * lclUtils.convertToLbs(lclBookingAc.getCostWeight().doubleValue()).doubleValue();
                        calculatedCostMeasure = totalMeasure * lclUtils.convertToCft(lclBookingAc.getCostMeasure().doubleValue()).doubleValue();
                    } else {
                        calculatedCostWeight = (totalWeight / 100) * lclBookingAc.getCostWeight().doubleValue();
                        calculatedCostMeasure = totalMeasure * lclBookingAc.getCostMeasure().doubleValue();
                    }
                    lclBookingAc.setRatePerWeightUnitDiv(new BigDecimal(100));
                } else if (fdEngmet.equals("M")) {
                    if (lclBookingAc.getRateUom().equalsIgnoreCase("I")) {
                        calculatedCostWeight = (totalWeight / 1000) * lclUtils.convertToKgs(lclBookingAc.getCostWeight().doubleValue()).doubleValue();
                        calculatedCostMeasure = totalMeasure * lclUtils.convertToCbm(lclBookingAc.getCostMeasure().doubleValue()).doubleValue();
                    } else {
                        calculatedCostWeight = (totalWeight / 1000) * lclBookingAc.getCostWeight().doubleValue();
                        calculatedCostMeasure = totalMeasure * lclBookingAc.getCostMeasure().doubleValue();
                    }
                    lclBookingAc.setRatePerWeightUnitDiv(new BigDecimal(1000));
                }
            }
            if (calculatedCostWeight >= calculatedCostMeasure && calculatedCostWeight >= lclBookingAc.getCostMinimum().doubleValue()) {
                calculateCostWeiMea = calculatedCostWeight;
            } else if (calculatedCostMeasure >= calculatedCostWeight && calculatedCostMeasure >= lclBookingAc.getCostMinimum().doubleValue()) {
                calculateCostWeiMea = calculatedCostMeasure;
            } else {
                calculateCostWeiMea = lclBookingAc.getCostMinimum().doubleValue();
            }

            if (null != lclBookingAc.getApAmount()) {
                calculateCostWeiMea = calculateCostWeiMea + lclBookingAc.getApAmount().doubleValue();
                lclBookingAc.setApAmount(new BigDecimal(calculateCostWeiMea));
                lclBookingAc.setDeleted(false);
            }
            if (lclBookingAc.getArglMapping().isDestinationServices()) {
                String calculateMinMax = request.getParameter("calculateMinMax");
                if (calculateMinMax != null && calculateMinMax.equalsIgnoreCase("false")
                        && !lclBookingAc.getArglMapping().getChargeCode().equalsIgnoreCase("DTHC PREPAID")) {
                    BigDecimal costAmount = lclBookingAc.getApAmount();
                    BigDecimal destinationDiff = lclBookingAc.getArAmount().subtract(lclBookingAc.getApAmount());
                    int profitMax = chargeForm.getProfitMax();
                    int profitMin = chargeForm.getProfitMin();
                    if (destinationDiff.intValue() <= profitMin) {
                        lclBookingAc.setArAmount(new BigDecimal(costAmount.doubleValue() + profitMin));
                    } else if (destinationDiff.intValue() >= profitMax) {
                        lclBookingAc.setArAmount(new BigDecimal(costAmount.doubleValue() + profitMax));
                    } else {
                        lclBookingAc.setArAmount(destinationDiff);
                    }
                }
            }
            // calculation end for cost and  charge;
            lclBookingAc.setBundleIntoOf(false);
            lclBookingAc.setPrintOnBl(true);
            lclBookingAc.setControlNo(lclBookingAc.getControlNo());
            LclManifestDAO lclManifestDAO = new LclManifestDAO();
            if (null !=lclBookingAc.getId() && lclBookingAc.getApAmount().doubleValue() == 0.00) {
                lclBookingAc.setApAmount(BigDecimal.ZERO);
                lclBookingAc.setSupAcct(null);
                lclBookingAc.setDeleted(Boolean.TRUE);
                chargeDAO.saveOrUpdate(lclBookingAc);
                lclManifestDAO.deleteLclAccruals(lclBookingAc.getId().intValue(), lclBookingAc.getLclFileNumber().getFileNumber());
                chargeDAO.deleteLclBookingAcTa(lclBookingAc.getId().intValue());
            } else {
                chargeDAO.saveOrUpdate(lclBookingAc);
            }
            String isDest_service_page = request.getParameter("isDest_Service_Page");
            if (null != isDest_service_page && "true".equalsIgnoreCase(isDest_service_page)) {
                chargeForm.setLclBookingAc(lclBookingAc);
                setDestinationServices(chargeForm, lclBookingAc, request);
                if (lclBookingAc.getArglMapping().getChargeCode().equalsIgnoreCase("ONCARR")) {
                    lclBooking.setPodfdtt(CommonUtils.isNotEmpty(chargeForm.getTotaldestCarriageTT()) ? chargeForm.getTotaldestCarriageTT() : null);
                    lclBookingDAO.update(lclBooking);
                }
            }
            if (CommonUtils.isNotEmpty(chargeForm.getExistBillToParty())
                    && !chargeForm.getExistBillToParty().equalsIgnoreCase(lclBookingAc.getArBillToParty())) {
                String notes = "UPDATED -> (Code -> " + lclBookingAc.getArglMapping().getChargeCode() + ")(Bill To Party -> "
                        + chargeForm.getExistBillToParty() + " to " + lclBookingAc.getArBillToParty() + ")";
                new LclRemarksDAO().insertLclRemarks(chargeForm.getFileNumberId(), REMARKS_DR_AUTO_NOTES,
                        notes, loginUser.getUserId());
            }

            Ports ports = new PortsDAO().getByProperty("unLocationCode", fdUnloCode);
            new LclChargesCalculation().calculateCAFCharge(pooTrmnum, polTrmnum, fdEciPortCode, podEciPortCode, lclBookingPiecesList, lclBooking.getBillingType(),
                    getCurrentUser(request), chargeForm.getFileNumberId(), request, ports, lclBooking.getBillToParty());
            request.setAttribute("lclCommodityList", lclBookingPiecesList);
            List<LclBookingAc> chargeList = chargeDAO.getLclCostByFileNumberAsc(chargeForm.getFileNumberId(), chargeForm.getModuleName());
            lclUtils.setWeighMeasureForBooking(request, lclBookingPiecesList, ports);
            lclUtils.setRolledUpChargesForBooking(chargeList, request, chargeForm.getFileNumberId(), chargeDAO,
                    lclBookingPiecesList, billingType, fdEngmet, "No");
            if ("T".equalsIgnoreCase(lclBooking.getBookingType())) {
                lclUtils.setTemBillToPartyList(request, "");
            }
            LclConsolidateDAO consolidateDao = new LclConsolidateDAO();
            ExportVoyageSearchModel pickedDetails = null;
            if (consolidateDao.isConsoildateFile(chargeForm.getFileNumberId().toString())) {
                String[] file = consolidateDao.getParentConsolidateFile(chargeForm.getFileNumberId().toString());
                pickedDetails = new ExportBookingUtils().getPickedExpVoyageDetails(Long.parseLong(file[0]), "E");
                if (null == pickedDetails) {
                    pickedDetails = new ExportBookingUtils().getPickedExpVoyageDetails(chargeForm.getFileNumberId(), "E");
                }
            } else {
                pickedDetails = new ExportBookingUtils().getPickedExpVoyageDetails(chargeForm.getFileNumberId(), "E");
            }
            if (pickedDetails != null && CommonUtils.isNotEmpty(pickedDetails.getUnitSsId())) {
                LclUnitSs lclUnitSs = new LclUnitSsDAO().findById(Long.parseLong(pickedDetails.getUnitSsId()));
                request.setAttribute("lclssheader", lclUnitSs.getLclSsHeader());
            }
            request.setAttribute("lclBooking", lclBooking);
        } catch (Exception e) {
            log.info("Error in LclCostAndChargeAction Class addCharges method. " + new Date(), e);
            return mapping.findForward(CHARGE_DESC);
        }

        return mapping.findForward(CHARGE_DESC);
    }

    public ActionForward addImpCharge(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String agentNo = "";
        String oldChargeCode = "";
        GlMapping arGlmapping = null;
        GlMapping apGlmapping = null;
        String shipmentType = null;
        LclCostAndChargeForm lclCostAndChargeForm = (LclCostAndChargeForm) form;
        LclCostChargeDAO lclCostChargesDAO = new LclCostChargeDAO();
        LclBookingAc lclBookingAc = lclCostAndChargeForm.getLclBookingAc();
        GlMappingDAO glMappingDAO = new GlMappingDAO();
        try {
            List<LclBookingPiece> lclBookingPiecesList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", lclCostAndChargeForm.getFileNumberId());
            LclBooking lclBooking = lclBookingDAO.findById(lclCostAndChargeForm.getFileNumberId());
            shipmentType = LCL_SHIPMENT_TYPE_IMPORT;
            //Charges are added
            lclBookingAc.setLclFileNumber(lclBooking.getLclFileNumber());
            if (CommonUtils.isNotEmpty(lclCostAndChargeForm.getChargesCodeId())) {
                oldChargeCode = (lclBookingAc.getArglMapping() != null && lclBookingAc.getArglMapping().getChargeCode() != null ? lclBookingAc.getArglMapping().getChargeCode() : "");
                if (!oldChargeCode.equalsIgnoreCase(lclCostAndChargeForm.getChargesCode())) {
                    arGlmapping = glMappingDAO.findByChargeCode(lclCostAndChargeForm.getChargesCode(), shipmentType, TRANSACTION_TYPE_ACCOUNT_RECEIVABLE);
                    apGlmapping = glMappingDAO.findByChargeCode(lclCostAndChargeForm.getChargesCode(), shipmentType, TRANSACTION_TYPE_ACCRUALS);
                    if (arGlmapping != null) {
                        lclBookingAc.setArglMapping(arGlmapping);
                    } else {
                        lclBookingAc.setArglMapping(apGlmapping);
                    }
                }
                if (!oldChargeCode.equalsIgnoreCase(lclCostAndChargeForm.getChargesCode()) || (CommonUtils.isNotEmpty(lclCostAndChargeForm.getCostAmount())
                        && !lclCostAndChargeForm.getCostAmount().equals("0.00") && lclBookingAc.getApglMapping() == null)) {
                    apGlmapping = glMappingDAO.findByChargeCode(lclCostAndChargeForm.getChargesCode(), shipmentType, TRANSACTION_TYPE_ACCRUALS);
                    lclBookingAc.setApglMapping(apGlmapping);
                }
            }
            if ("W".equalsIgnoreCase(lclCostAndChargeForm.getBillToParty())) {
                lclBookingAc.setPostAr(false);
            }
            lclBookingAc.setArBillToParty(lclCostAndChargeForm.getBillToParty());
            lclBookingAc.setRelsToInv("A".equalsIgnoreCase(lclCostAndChargeForm.getBillToParty()) ? true : false);
            lclBookingAc.setModifiedBy(getCurrentUser(request));
            lclBookingAc.setModifiedDatetime(new Date());
            if (!lclCostAndChargeForm.getManualEntry().equalsIgnoreCase("false")) {
                lclBookingAc.setManualEntry(true);
            }
            if (CommonUtils.isNotEmpty(lclCostAndChargeForm.getFlatRateAmount())) {//FLAT RATE For Manual Chareg
                lclBookingAc.setRatePerUnit(new BigDecimal(lclCostAndChargeForm.getFlatRateAmount()));
                lclBookingAc.setArAmount(new BigDecimal(lclCostAndChargeForm.getFlatRateAmount()));
            } else if (CommonUtils.isNotEmpty(lclCostAndChargeForm.getArAmount())) {//FLAT RATE For Auto Chareg
                lclBookingAc.setRatePerUnit(new BigDecimal(lclCostAndChargeForm.getArAmount()));
                lclBookingAc.setArAmount(new BigDecimal(lclCostAndChargeForm.getArAmount()));
            } else {
                lclBookingAc.setRatePerUnit(BigDecimal.ZERO);
                lclBookingAc.setArAmount(BigDecimal.ZERO);
            }
            if (CommonUtils.isNotEmpty(lclCostAndChargeForm.getCostAmount())) {//FLAT RATE For Both Manual And Auto Cost
                lclBookingAc.setApAmount(new BigDecimal(lclCostAndChargeForm.getCostAmount()));
                lclBookingAc.setDeleted(Boolean.FALSE);
            } else {
                lclBookingAc.setApAmount(BigDecimal.ZERO);
            }
            lclBookingAc.setCostFlatrateAmount(lclBookingAc.getApAmount());
            lclBookingAc.setBundleIntoOf(false);
            lclBookingAc.setPrintOnBl(true);
            if (lclBookingAc.getId() == null) {
                lclBookingAc = setInitialValues(lclBookingAc, getCurrentUser(request));
            }
            if (lclBookingAc.getApAmount() != null && lclBookingAc.getApAmount().doubleValue() == 0.00
                    && "AC".equalsIgnoreCase(lclCostAndChargeForm.getCostStatus())) {
                lclBookingAc.setApAmount(BigDecimal.ZERO);
                lclBookingAc.setSupAcct(null);
                lclBookingAc.setDeleted(Boolean.TRUE);
                lclCostChargesDAO.saveOrUpdate(lclBookingAc);
                new LclManifestDAO().deleteLclAccruals(lclBookingAc.getId().intValue(), lclBookingAc.getLclFileNumber().getFileNumber());
                lclCostChargesDAO.deleteLclBookingAcTa(lclBookingAc.getId().intValue());
            } else {
                lclCostChargesDAO.saveOrUpdate(lclBookingAc);
            }
            List<LclBookingAc> chargeList = lclCostChargesDAO.getLclCostByFileNumberAsc(lclCostAndChargeForm.getFileNumberId(), lclCostAndChargeForm.getModuleName());
            if (!lclCostAndChargeForm.getAgentNo().equals("") && lclCostAndChargeForm.getAgentNo() != null) {
                agentNo = lclCostAndChargeForm.getAgentNo();
            }
            lclUtils.setWeighMeasureForImportBooking(request, lclBookingPiecesList, null);
            lclUtils.setImportRolledUpChargesForBooking(chargeList, request, lclCostAndChargeForm.getFileNumberId(), lclCostChargesDAO,
                    lclBookingPiecesList, lclBooking.getBillingType(), "", agentNo);
            request.setAttribute("lclBooking", lclBooking);
            request.setAttribute("unitSsCollectType", request.getParameter("unitSsCollectType"));
        } catch (Exception e) {
            log.info("Error in LclCostAndChargeAction Class addCharges method. " + new Date(), e);
            return mapping.findForward(CHARGE_DESC);
        }
        return mapping.findForward(CHARGE_DESC);
    }

    private LclBookingAc setInitialValues(LclBookingAc lclBookingAc, User user) {
        lclBookingAc.setTransDatetime(new Date());
        lclBookingAc.setAdjustmentAmount(BigDecimal.ZERO);
        lclBookingAc.setRatePerUnitUom("FL");
        lclBookingAc.setRatePerWeightUnit(BigDecimal.ZERO);
        lclBookingAc.setRatePerVolumeUnit(BigDecimal.ZERO);
        lclBookingAc.setRateFlatMinimum(BigDecimal.ZERO);
        lclBookingAc.setCostWeight(BigDecimal.ZERO);
        lclBookingAc.setCostMeasure(BigDecimal.ZERO);
        lclBookingAc.setCostMinimum(BigDecimal.ZERO);
        lclBookingAc.setEnteredBy(user);
        lclBookingAc.setEnteredDatetime(new Date());
        return lclBookingAc;
    }

    public void deleteDestinationCharge(LclCostAndChargeForm chargeForm,
            HttpServletRequest request) throws Exception {
        LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
        LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
        LclManifestDAO lclManifestDAO = new LclManifestDAO();
        LCLBookingAcTransDAO lclBookingAcTransDAO = new LCLBookingAcTransDAO();
        String fileNumberStatus = chargeForm.getFileNumberStatus();
        User loginUser = getCurrentUser(request);
        Date now = new Date();
        if (CommonUtils.isNotEmpty(chargeForm.getId())) {
            String costStatus = lclBookingAcTransDAO.getTransType(Long.parseLong(chargeForm.getId()));
            LclBookingAc lclBookingAc = lclCostChargeDAO.findById(Long.parseLong(chargeForm.getId()));
            StringBuilder remarks = new StringBuilder();
            remarks.append("DELETED -> Code -> ");
            String code;
            if (null != lclBookingAc.getArglMapping() && CommonUtils.isNotEmpty(lclBookingAc.getArglMapping().getChargeCode())) {
                code = lclBookingAc.getArglMapping().getChargeCode();
            } else {
                code = lclBookingAc.getApglMapping().getChargeCode();
            }
            remarks.append(code);
            if (CommonUtils.notIn(costStatus, "AP", "IP", "DS")
                    && NumberUtils.isNotZero(lclBookingAc.getApAmount())
                    && null != lclBookingAc.getApglMapping() && CommonUtils.isNotEmpty(lclBookingAc.getApglMapping().getChargeCode())) {
                String lclBookingAcTransIds = lclBookingAcTransDAO.getConcatenatedBookingAcTransIds(lclBookingAc.getId().toString());
                remarks.append(" Cost Amount -> ").append(lclBookingAc.getApAmount());
                lclBookingAc.setSupAcct(null);
                lclBookingAc.setApBillToParty(null);
                lclBookingAc.setApAmount(BigDecimal.ZERO);
                lclBookingAc.setCostFlatrateAmount(BigDecimal.ZERO);
                lclBookingAc.setDeleted(Boolean.TRUE);
                lclManifestDAO.deleteLclAccruals(lclBookingAc.getId().intValue(), lclBookingAc.getLclFileNumber().getFileNumber());
                lclCostChargeDAO.deleteLclBookingAcTa(lclBookingAc.getId().intValue());
                lclBookingAcTransDAO.deleteLclBookingAcTransByBkgAcId(lclBookingAcTransIds);
            }
            if (lclBookingAc.getArglMapping().isDestinationServices()
                    && CommonUtils.notIn(costStatus, "AP", "IP", "DS") && NumberUtils.isNotZero(lclBookingAc.getApAmount())) {
                remarks.append(" Cost Amount -> ").append(lclBookingAc.getApAmount());
                lclBookingAc.setSupAcct(null);
                lclBookingAc.setApBillToParty(null);
                lclBookingAc.setApAmount(BigDecimal.ZERO);
                lclBookingAc.setCostFlatrateAmount(BigDecimal.ZERO);
            }
            if ((CommonUtils.isNotEqual(fileNumberStatus, "M") && CommonUtils.isNotEqual(lclBookingAc.getArBillToParty(), "A"))
                    || (CommonUtils.isEqual(lclBookingAc.getArBillToParty(), "A") && CommonUtils.isEmpty(lclBookingAc.getSpReferenceNo()))) {
                remarks.append(" Charge Amount -> ").append(lclBookingAc.getArAmount());
                lclBookingAc.setSpReferenceNo(null);
                lclBookingAc.setArBillToParty(null);
                lclBookingAc.setArAmount(BigDecimal.ZERO);
            }
            if (CommonUtils.isEqual(fileNumberStatus, "M")
                    && CommonUtils.isEqual(lclBookingAc.getArBillToParty(), "W")) {
                lclBookingAc.setArAmount(BigDecimal.ZERO);
            }
            lclBookingAc.setModifiedDatetime(now);
            lclBookingAc.setModifiedBy(loginUser);
            lclCostChargeDAO.saveOrUpdate(lclBookingAc);
            lclRemarksDAO.insertLclRemarks(chargeForm.getFileNumberId(), REMARKS_DR_AUTO_NOTES, remarks.toString(), loginUser.getUserId());
        }
    }
}
