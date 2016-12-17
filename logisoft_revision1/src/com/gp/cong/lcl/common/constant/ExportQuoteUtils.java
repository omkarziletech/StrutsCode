package com.gp.cong.lcl.common.constant;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.lcl.dwr.LclDwr;
import com.gp.cong.lcl.dwr.LclQuotationChargesCalculation;
import com.gp.cong.lcl.dwr.LclSession;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.Warehouse;
import com.gp.cong.logisoft.domain.lcl.LclBookingExport;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.LclQuote;
import com.gp.cong.logisoft.domain.lcl.LclQuoteAc;
import com.gp.cong.logisoft.domain.lcl.LclQuoteHotCode;
import com.gp.cong.logisoft.domain.lcl.LclQuoteImport;
import com.gp.cong.logisoft.domain.lcl.LclQuotePiece;
import com.gp.cong.logisoft.domain.lcl.LclQuotePlan;
import com.gp.cong.logisoft.domain.lcl.LclRemarks;
import com.gp.cong.logisoft.hibernate.dao.GeneralInformationDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.NotesDAO;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.hibernate.dao.WarehouseDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLQuoteDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingExportDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingHotCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPlanDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuoteAcDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuoteHotCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuoteImportDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuotePieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuotePlanDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.commodityTypeDAO;
import com.gp.cong.logisoft.lcl.model.LclBookingPlanBean;
import com.gp.cong.logisoft.lcl.model.LclBookingVoyageBean;
import com.gp.cvst.logisoft.struts.form.lcl.LCLQuoteForm;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Mei
 */
public class ExportQuoteUtils implements LclCommonConstant {

    private LclUtils lclUtils = new LclUtils();

    public void setExpChargesDetails(LCLQuoteForm lclQuoteForm, LclQuote lclQuote, List<LclQuotePiece> lclCommodityList, List<LclQuoteAc> chargeList,
            User user, boolean cafFlag, HttpServletRequest request) throws Exception {
        setCharges(lclQuoteForm.getPooTrmNum(), lclQuoteForm.getPolTrmNum(), lclQuoteForm.getPodEciPortCode(), lclQuoteForm.getFdEciPortCode(),
                lclQuoteForm.getFdEngmet(), lclQuote, lclCommodityList, chargeList, user, cafFlag, request);
    }

    public void setCharges(String pooTrmNum, String polTrmNum, String podEciCode, String fdEciCode, String fdEngmet, LclQuote lclQuote, List<LclQuotePiece> lclCommodityList, List<LclQuoteAc> chargeList,
            User user, boolean cafFlag, HttpServletRequest request) throws Exception {
        LclQuoteAcDAO lclQuoteAcDAO = new LclQuoteAcDAO();
        if (cafFlag) {
            LclQuotationChargesCalculation lclQuotationChargesCalculation = new LclQuotationChargesCalculation();
            String cafChargeFlag = lclQuoteAcDAO.isChargeCodeValidate(String.valueOf(lclQuote.getFileNumberId()), CAF_CHARGE_CODE, "false");
            if ("true".equalsIgnoreCase(cafChargeFlag)) {
                lclQuotationChargesCalculation.calculateCAFChargeForRadio(pooTrmNum, polTrmNum, fdEciCode,
                        podEciCode, lclCommodityList, lclQuote.getBillingType(),
                        user, lclQuote.getFileNumberId(), lclQuote.getFinalDestination().getUnLocationCode(), fdEngmet);
            }
        }
        setWeighMeasureForQuote(request, lclCommodityList, fdEngmet);
        setRolledUpChargesForQuote(chargeList, request, lclQuote.getFileNumberId(), lclQuoteAcDAO, lclCommodityList, lclQuote.getBillingType(), fdEngmet, "No");
        request.setAttribute("lclQuote", lclQuote);
    }

    public void setExpChargesDetails(LclQuote lclQuote, List<LclQuotePiece> lclCommodityList, List<LclQuoteAc> chargeList,
            User user, boolean cafFlag, HttpServletRequest request) throws Exception {
        String pooTrmnum = "", polTrmnum = "", podEciPortCode = "", fdEciPortCode = "", fdEngmet = "";
        String pooUnloCode = lclQuote.getPortOfOrigin() != null ? lclQuote.getPortOfOrigin().getUnLocationCode() : "";
        String polUnloCode = lclQuote.getPortOfLoading() != null ? lclQuote.getPortOfLoading().getUnLocationCode() : "";
        String podUnloCode = lclQuote.getPortOfDestination() != null ? lclQuote.getPortOfDestination().getUnLocationCode() : "";
        String fdUnloCode = lclQuote.getFinalDestination() != null ? lclQuote.getFinalDestination().getUnLocationCode() : "";
        String rateType = "R".equalsIgnoreCase(lclQuote.getRateType()) ? "Y" : lclQuote.getRateType();
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
                fdEngmet = (String) col[1];
            }
        }
        setCharges(pooTrmnum, polTrmnum, podEciPortCode, fdEciPortCode, fdEngmet, lclQuote,
                lclCommodityList, chargeList, user, cafFlag, request);
    }

    public void setWeighMeasureForQuote(HttpServletRequest request, List<LclQuotePiece> lclQuotePiecesList, String fdEngmet) {
        if (CommonUtils.isNotEmpty(lclQuotePiecesList)) {
            BigDecimal weightValues = new BigDecimal(0.00);
            BigDecimal volumeValues = new BigDecimal(0.00);
            if (fdEngmet != null && !"".equalsIgnoreCase(fdEngmet)) {
                for (LclQuotePiece lclQuotePiece : lclQuotePiecesList) {
                    if (fdEngmet.equals("E")) {
                        if (lclQuotePiece.getActualWeightImperial() != null && lclQuotePiece.getActualWeightImperial().doubleValue() != 0.00) {
                            weightValues = lclQuotePiece.getActualWeightImperial().add(weightValues);
                            request.setAttribute("weight", weightValues + " LBS");
                        } else if (lclQuotePiece.getBookedWeightImperial() != null && lclQuotePiece.getBookedWeightImperial().doubleValue() != 0.00) {
                            weightValues = lclQuotePiece.getBookedWeightImperial().add(weightValues);
                            request.setAttribute("weight", weightValues + " LBS");
                        }
                        if (lclQuotePiece.getActualVolumeImperial() != null && lclQuotePiece.getActualVolumeImperial().doubleValue() != 0.00) {
                            volumeValues = lclQuotePiece.getActualVolumeImperial().add(volumeValues);
                            request.setAttribute("measure", volumeValues + " CFT");
                        } else if (lclQuotePiece.getBookedVolumeImperial() != null && lclQuotePiece.getBookedVolumeImperial().doubleValue() != 0.00) {
                            volumeValues = lclQuotePiece.getBookedVolumeImperial().add(volumeValues);
                            request.setAttribute("measure", volumeValues + " CFT");
                        }
                    } else if (fdEngmet.equals("M")) {
                        if (lclQuotePiece.getActualWeightMetric() != null && lclQuotePiece.getActualWeightMetric().doubleValue() != 0.00) {
                            weightValues = lclQuotePiece.getActualWeightMetric().add(weightValues);
                            request.setAttribute("weight", weightValues + " KGS");
                        } else if (lclQuotePiece.getBookedWeightMetric() != null && lclQuotePiece.getBookedWeightMetric().doubleValue() != 0.00) {
                            weightValues = lclQuotePiece.getBookedWeightMetric().add(weightValues);
                            request.setAttribute("weight", weightValues + " KGS");
                        }
                        if (lclQuotePiece.getActualVolumeMetric() != null && lclQuotePiece.getActualVolumeMetric().doubleValue() != 0.00) {
                            volumeValues = lclQuotePiece.getActualVolumeMetric().add(volumeValues);
                            request.setAttribute("measure", volumeValues + " CBM");
                        } else if (lclQuotePiece.getBookedVolumeMetric() != null && lclQuotePiece.getBookedVolumeMetric().doubleValue() != 0.00) {
                            volumeValues = lclQuotePiece.getBookedVolumeMetric().add(volumeValues);
                            request.setAttribute("measure", volumeValues + " CBM");
                        }
                    }
                }
            }
        }
    }

    public void setRolledUpChargesForQuote(List<LclQuoteAc> chargeList, HttpServletRequest request,
            Long longFileId, LclQuoteAcDAO lclQuoteAcDAO, List<LclQuotePiece> lclQuotePiecesList, String billingType, String engmet, String pdfFormatLabelQuote) throws Exception {
        List<LclQuoteAc> lclQuoteAcList = null;
        if (chargeList == null || chargeList.size() == 0) {
            request.setAttribute("rateErrorMessage", "No Rates Found.");
        } else if (CommonUtils.isNotEmpty(lclQuotePiecesList)) {
            if (lclQuotePiecesList.size() == 1) {
                lclQuoteAcList = getFormattedLabelChargesForQuote(lclQuotePiecesList, chargeList, engmet, pdfFormatLabelQuote);
                request.setAttribute("chargeList", lclQuoteAcList);
                if (longFileId != null && longFileId > 0) {
                    request.setAttribute("totalCharges", lclQuoteAcDAO.getTotalLclCostByFileNumber(longFileId));
                }
            } else {
                lclQuoteAcList = getRolledUpChargesForQuote(lclQuotePiecesList, chargeList, engmet, pdfFormatLabelQuote);
                request.setAttribute("chargeList", lclQuoteAcList);
                request.setAttribute("totalCharges", calculateTotalByQuoteAcList(lclQuoteAcList));
            }
        }
        if (billingType != null) {
            if (billingType.equalsIgnoreCase("P")) {
                request.setAttribute("billingMethod", "PREPAID");
            } else if (billingType.equalsIgnoreCase("C")) {
                request.setAttribute("billingMethod", "COLLECT");
            } else {
                request.setAttribute("billingMethod", "BOTH");
            }
        }
    }

    public List getFormattedLabelChargesForQuote(List<LclQuotePiece> lclQuotePiecesList, List<LclQuoteAc> lclQuoteAcList, String engmet, String pdfFormatLabelQuote) {
        for (int i = 0; i < lclQuoteAcList.size(); i++) {
            LclQuoteAc lclQuoteAc = lclQuoteAcList.get(i);
            lclQuoteAc.setRolledupCharges(lclQuoteAc.getArAmount());
            if (pdfFormatLabelQuote.equalsIgnoreCase("Yes")) {
                formatLabelChargeForQuotePdf(lclQuotePiecesList, lclQuoteAc, engmet);
            } else {
                formatLabelChargeForQuote(lclQuotePiecesList, lclQuoteAc, engmet);
            }
        }
        return lclQuoteAcList;
    }

    public List getRolledUpChargesForQuote(List<LclQuotePiece> lclQuotePiecesList, List<LclQuoteAc> lclQuoteAcList, String engmet, String pdfFormatLabelQuote) {
        Map chargesInfoMap = new LinkedHashMap();
        Double minchg = 0.0;
        Double calculatedWeight = 0.0;
        Double calculatedMeasure = 0.0;
        for (int i = 0; i < lclQuoteAcList.size(); i++) {
            LclQuoteAc lclQuoteAc = lclQuoteAcList.get(i);
            if (!chargesInfoMap.containsKey(lclQuoteAc.getArglMapping().getChargeCode())) {
                formatLabelChargeForQuote(lclQuotePiecesList, lclQuoteAc, engmet);
                lclQuoteAc.setRolledupCharges(lclQuoteAc.getArAmount());
                if (!lclQuoteAc.isManualEntry() && lclQuoteAc.getRatePerUnitUom() != null && lclQuoteAc.getRateUom() != null && lclQuoteAc.getArglMapping() != null
                        && lclQuoteAc.getArglMapping().getBlueScreenChargeCode() != null && !lclQuoteAc.getArglMapping().getBlueScreenChargeCode().equals("0032")
                        && !lclQuoteAc.getArglMapping().getBlueScreenChargeCode().equals("0232")) {
                    if (lclQuoteAc.getRatePerUnitUom().equalsIgnoreCase("V") || lclQuoteAc.getRatePerUnitUom().equalsIgnoreCase("W")
                            || lclQuoteAc.getRatePerUnitUom().equalsIgnoreCase("M")) {
                        if (lclQuoteAc.getRateUom().equalsIgnoreCase("I")) {
                            if (lclQuoteAc.getLclQuotePiece() != null) {
                                if (lclQuoteAc.getLclQuotePiece().getActualWeightImperial() != null && lclQuoteAc.getLclQuotePiece().getActualWeightImperial().doubleValue() != 0.00) {
                                    lclQuoteAc.setTotalWeight(lclQuoteAc.getLclQuotePiece().getActualWeightImperial());
                                } else if (lclQuoteAc.getLclQuotePiece().getBookedWeightImperial() != null && lclQuoteAc.getLclQuotePiece().getBookedWeightImperial().doubleValue() != 0.00) {
                                    lclQuoteAc.setTotalWeight(lclQuoteAc.getLclQuotePiece().getBookedWeightImperial());
                                }
                                if (lclQuoteAc.getLclQuotePiece().getActualVolumeImperial() != null && lclQuoteAc.getLclQuotePiece().getActualVolumeImperial().doubleValue() != 0.00) {
                                    lclQuoteAc.setTotalMeasure(lclQuoteAc.getLclQuotePiece().getActualVolumeImperial());
                                } else if (lclQuoteAc.getLclQuotePiece().getBookedVolumeImperial() != null && lclQuoteAc.getLclQuotePiece().getBookedVolumeImperial().doubleValue() != 0.00) {
                                    lclQuoteAc.setTotalMeasure(lclQuoteAc.getLclQuotePiece().getBookedVolumeImperial());
                                }
                            }
                        } else if (lclQuoteAc.getRateUom().equalsIgnoreCase("M")) {
                            if (lclQuoteAc.getLclQuotePiece() != null) {
                                if (lclQuoteAc.getLclQuotePiece().getActualWeightMetric() != null && lclQuoteAc.getLclQuotePiece().getActualWeightMetric().doubleValue() != 0.00) {
                                    lclQuoteAc.setTotalWeight(lclQuoteAc.getLclQuotePiece().getActualWeightMetric());
                                } else if (lclQuoteAc.getLclQuotePiece().getBookedWeightMetric() != null && lclQuoteAc.getLclQuotePiece().getBookedWeightMetric().doubleValue() != 0.00) {
                                    lclQuoteAc.setTotalWeight(lclQuoteAc.getLclQuotePiece().getBookedWeightMetric());
                                }
                                if (lclQuoteAc.getLclQuotePiece().getActualVolumeMetric() != null && lclQuoteAc.getLclQuotePiece().getActualVolumeMetric().doubleValue() != 0.00) {
                                    lclQuoteAc.setTotalMeasure(lclQuoteAc.getLclQuotePiece().getActualVolumeMetric());
                                } else if (lclQuoteAc.getLclQuotePiece().getBookedVolumeMetric() != null && lclQuoteAc.getLclQuotePiece().getBookedVolumeMetric().doubleValue() != 0.00) {
                                    lclQuoteAc.setTotalMeasure(lclQuoteAc.getLclQuotePiece().getBookedVolumeMetric());
                                }
                            }
                        }

                    }
                }
                chargesInfoMap.put(lclQuoteAc.getArglMapping().getChargeCode(), lclQuoteAc);
            } else {
                LclQuoteAc lclQuoteAcFromMap = (LclQuoteAc) chargesInfoMap.get(lclQuoteAc.getArglMapping().getChargeCode());
                if (lclQuoteAcFromMap != null) {
                    if (CommonUtils.isNotEmpty(lclQuoteAcFromMap.getRatePerUnitUom()) && !lclQuoteAcFromMap.getRatePerUnitUom().equalsIgnoreCase("FL")) {
                        BigDecimal total = new BigDecimal(lclQuoteAc.getArAmount().doubleValue() + lclQuoteAcFromMap.getRolledupCharges().doubleValue());
                        total = total.setScale(2, BigDecimal.ROUND_HALF_UP);
                        lclQuoteAcFromMap.setRolledupCharges(total);
                    }
                    if (lclQuoteAcFromMap.getRatePerUnitUom() != null && lclQuoteAcFromMap.getRateUom() != null && lclQuoteAcFromMap.getArglMapping() != null
                            && lclQuoteAcFromMap.getArglMapping().getBlueScreenChargeCode() != null && !lclQuoteAcFromMap.getArglMapping().getBlueScreenChargeCode().equals("0032")
                            && !lclQuoteAcFromMap.getArglMapping().getBlueScreenChargeCode().equals("0232")) {
                        if (lclQuoteAcFromMap.getRatePerUnitUom().equalsIgnoreCase("V") || lclQuoteAcFromMap.getRatePerUnitUom().equalsIgnoreCase("W")
                                || lclQuoteAcFromMap.getRatePerUnitUom().equalsIgnoreCase("M")) {
                            if (lclQuoteAcFromMap.getRateUom().equalsIgnoreCase("I")) {
                                if (lclQuoteAc.getLclQuotePiece() != null) {
                                    if (lclQuoteAc.getLclQuotePiece().getActualWeightImperial() != null && lclQuoteAc.getLclQuotePiece().getActualWeightImperial().doubleValue() != 0.00) {
                                        if (!CommonUtils.isEmpty(lclQuoteAcFromMap.getTotalWeight())) {
                                            lclQuoteAcFromMap.setTotalWeight(new BigDecimal(lclQuoteAcFromMap.getTotalWeight().doubleValue()
                                                    + lclQuoteAc.getLclQuotePiece().getActualWeightImperial().doubleValue()));
                                        }
                                    } else if (lclQuoteAc.getLclQuotePiece().getBookedWeightImperial() != null && lclQuoteAc.getLclQuotePiece().getBookedWeightImperial().doubleValue() != 0.00) {
                                        if (!CommonUtils.isEmpty(lclQuoteAcFromMap.getTotalWeight())) {
                                            lclQuoteAcFromMap.setTotalWeight(new BigDecimal(lclQuoteAcFromMap.getTotalWeight().doubleValue()
                                                    + lclQuoteAc.getLclQuotePiece().getBookedWeightImperial().doubleValue()));
                                        }
                                    }
                                    if (lclQuoteAc.getLclQuotePiece().getActualVolumeImperial() != null && lclQuoteAc.getLclQuotePiece().getActualVolumeImperial().doubleValue() != 0.00) {
                                        if (!CommonUtils.isEmpty(lclQuoteAcFromMap.getTotalMeasure())) {
                                            lclQuoteAcFromMap.setTotalMeasure(new BigDecimal(lclQuoteAcFromMap.getTotalMeasure().doubleValue()
                                                    + lclQuoteAc.getLclQuotePiece().getActualVolumeImperial().doubleValue()));
                                        }
                                    } else if (lclQuoteAc.getLclQuotePiece().getBookedVolumeImperial() != null && lclQuoteAc.getLclQuotePiece().getBookedVolumeImperial().doubleValue() != 0.00) {
                                        if (!CommonUtils.isEmpty(lclQuoteAcFromMap.getTotalMeasure())) {
                                            lclQuoteAcFromMap.setTotalMeasure(new BigDecimal(lclQuoteAcFromMap.getTotalMeasure().doubleValue()
                                                    + lclQuoteAc.getLclQuotePiece().getBookedVolumeImperial().doubleValue()));
                                        }
                                    }
                                    if (!CommonUtils.isEmpty(lclQuoteAcFromMap.getTotalWeight())) {
                                        calculatedWeight = (lclQuoteAcFromMap.getTotalWeight().doubleValue() / 100) * calculatedWeight;
                                    }
                                }
                            } else if (lclQuoteAcFromMap.getRateUom().equalsIgnoreCase("M")) {
                                if (lclQuoteAc.getLclQuotePiece() != null) {
                                    if (lclQuoteAc.getLclQuotePiece().getActualWeightMetric() != null && lclQuoteAc.getLclQuotePiece().getActualWeightMetric().doubleValue() != 0.00) {
                                        if (!CommonUtils.isEmpty(lclQuoteAcFromMap.getTotalWeight())) {
                                            lclQuoteAcFromMap.setTotalWeight(new BigDecimal(lclQuoteAcFromMap.getTotalWeight().doubleValue()
                                                    + lclQuoteAc.getLclQuotePiece().getActualWeightMetric().doubleValue()));
                                        }
                                    } else if (lclQuoteAc.getLclQuotePiece().getBookedWeightMetric() != null && lclQuoteAc.getLclQuotePiece().getBookedWeightMetric().doubleValue() != 0.00) {
                                        if (!CommonUtils.isEmpty(lclQuoteAcFromMap.getTotalWeight())) {
                                            lclQuoteAcFromMap.setTotalWeight(new BigDecimal(lclQuoteAcFromMap.getTotalWeight().doubleValue()
                                                    + lclQuoteAc.getLclQuotePiece().getBookedWeightMetric().doubleValue()));
                                        }
                                    }
                                    if (lclQuoteAc.getLclQuotePiece().getActualVolumeMetric() != null && lclQuoteAc.getLclQuotePiece().getActualVolumeMetric().doubleValue() != 0.00) {
                                        if (!CommonUtils.isEmpty(lclQuoteAcFromMap.getTotalMeasure())) {
                                            lclQuoteAcFromMap.setTotalMeasure(new BigDecimal(lclQuoteAcFromMap.getTotalMeasure().doubleValue()
                                                    + lclQuoteAc.getLclQuotePiece().getActualVolumeMetric().doubleValue()));
                                        }
                                    } else if (lclQuoteAc.getLclQuotePiece().getBookedVolumeMetric() != null && lclQuoteAc.getLclQuotePiece().getBookedVolumeMetric().doubleValue() != 0.00) {
                                        if (!CommonUtils.isEmpty(lclQuoteAcFromMap.getTotalMeasure())) {
                                            lclQuoteAcFromMap.setTotalMeasure(new BigDecimal(lclQuoteAcFromMap.getTotalMeasure().doubleValue()
                                                    + lclQuoteAc.getLclQuotePiece().getBookedVolumeMetric().doubleValue()));
                                        }
                                    }
                                    if (!CommonUtils.isEmpty(lclQuoteAcFromMap.getTotalWeight()) && !CommonUtils.isEmpty(lclQuoteAc.getRatePerWeightUnit())) {
                                        calculatedWeight = (lclQuoteAcFromMap.getTotalWeight().doubleValue() / 1000) * lclQuoteAc.getRatePerWeightUnit().doubleValue();
                                    }
                                }
                            }
                            if (lclQuoteAcFromMap.getTotalMeasure() != null && lclQuoteAc.getRatePerVolumeUnit() != null) {
                                calculatedMeasure = lclQuoteAcFromMap.getTotalMeasure().doubleValue() * lclQuoteAc.getRatePerVolumeUnit().doubleValue();
                            }
                            if (lclQuoteAc.getRateFlatMinimum() != null) {
                                minchg = lclQuoteAc.getRateFlatMinimum().doubleValue();
                            }
                            lclQuoteAcFromMap.setRatePerVolumeUnit(lclQuoteAc.getRatePerVolumeUnit()); 
                            lclQuoteAcFromMap.setRatePerWeightUnit(lclQuoteAc.getRatePerWeightUnit()); 
                            if (calculatedWeight >= calculatedMeasure && calculatedWeight >= minchg) {
                                lclQuoteAcFromMap.setRolledupCharges(new BigDecimal(calculatedWeight).setScale(2, BigDecimal.ROUND_HALF_UP));
                            } else if (calculatedMeasure >= calculatedWeight && calculatedMeasure >= minchg) {
                                lclQuoteAcFromMap.setRolledupCharges(new BigDecimal(calculatedMeasure).setScale(2, BigDecimal.ROUND_HALF_UP));
                            } else {
                                lclQuoteAcFromMap.setRolledupCharges(new BigDecimal(minchg).setScale(2, BigDecimal.ROUND_HALF_UP));
                            }
                        }
                    }
                }
                if (pdfFormatLabelQuote.equalsIgnoreCase("Yes")) {
                    formatLabelChargeForQuotePdf(lclQuotePiecesList, lclQuoteAcFromMap, engmet);
                } else {
                    formatLabelChargeForQuote(lclQuotePiecesList, lclQuoteAcFromMap, engmet);
                }
                chargesInfoMap.put(lclQuoteAc.getArglMapping().getChargeCode(), lclQuoteAcFromMap);
            }
        }
        List rolledChargesList = new ArrayList(chargesInfoMap.values());
        return rolledChargesList;
    }

    public void formatLabelChargeForQuotePdf(List<LclQuotePiece> lclQuotePiecesList, LclQuoteAc lclQuoteAc, String engmet) {
        if (lclQuoteAc.getRatePerUnitUom() != null && !lclQuoteAc.getRatePerUnitUom().trim().equals("")) {
            if (lclQuoteAc.getRatePerUnitUom().equalsIgnoreCase("FL")) {
                if (lclQuoteAc.getArglMapping() != null && lclQuoteAc.getArglMapping().getBlueScreenChargeCode() != null) {
                    if (lclQuoteAc.getArglMapping().getBlueScreenChargeCode().equals("0006")) {                       
                        String ratePerUnit = lclQuoteAc.getRatePerWeightUnit() != null ? lclQuoteAc.getRatePerWeightUnit().toString() : "";
                        String ratePeUnitDiv = lclQuoteAc.getRatePerWeightUnitDiv() != null ? lclQuoteAc.getRatePerWeightUnitDiv().toString() : "";                        
                        lclQuoteAc.setLabel2("(" + ratePerUnit + " PER " + ratePeUnitDiv + " CIF)");
                    } else {
                        lclQuoteAc.setLabel2("($" + lclQuoteAc.getArAmount().toString() + " FLAT RATE)");
                    }
                }
                if (lclQuoteAc.getArglMapping().getChargeCode() != null && (lclQuoteAc.getArglMapping().getChargeCode().equals("OFBARR")
                        || lclQuoteAc.getArglMapping().getChargeCode().equals("TTBARR"))) {
                    lclQuoteAc.setLabel2("$" + lclQuoteAc.getRatePerWeightUnit() + " PER BARREL.");
                }
            }
            if (lclQuoteAc.getRatePerUnitUom().equalsIgnoreCase("W")) {
                lclQuoteAc.setLabel1("*** TO WEIGHT ***");
            }
            if (lclQuoteAc.getRatePerUnitUom().equalsIgnoreCase("V")) {
                lclQuoteAc.setLabel1("*** VOLUME ***");
            }
            if (lclQuoteAc.getRatePerUnitUom().equalsIgnoreCase("M")) {
                lclQuoteAc.setLabel1("*** MINIMUM ***");
            }
            if (lclQuoteAc.getRatePerUnitUom().equalsIgnoreCase("PCT")) {
                if (lclQuoteAc.getRatePerUnit() != null) {
                    int ratePercentage = (int) (lclQuoteAc.getRatePerUnit().doubleValue() * 100);
                    lclQuoteAc.setLabel2(String.valueOf(ratePercentage) + " PERCENT OF THE B/L.");
                }
            }
            if (lclQuoteAc.getRatePerUnitUom().equalsIgnoreCase("FRW")) {
                lclQuoteAc.setLabel1("* O/F - TO WEIGHT *");
            }
            if (lclQuoteAc.getRatePerUnitUom().equalsIgnoreCase("FRV")) {
                lclQuoteAc.setLabel1("* O/F - VOLUME *");
            }
            if (lclQuoteAc.getRatePerUnitUom().equalsIgnoreCase("FRM")) {
                lclQuoteAc.setLabel1("* O/F - MINIMUM *");
            }
            if (lclQuoteAc.getRatePerUnitUom().equalsIgnoreCase("W") || lclQuoteAc.getRatePerUnitUom().equalsIgnoreCase("V")
                    || lclQuoteAc.getRatePerUnitUom().equalsIgnoreCase("M") || lclQuoteAc.getRatePerUnitUom().equalsIgnoreCase("FRW")
                    || lclQuoteAc.getRatePerUnitUom().equalsIgnoreCase("FRV") || lclQuoteAc.getRatePerUnitUom().equalsIgnoreCase("FRM")) {
                if (engmet != null) {
                    if (engmet.equalsIgnoreCase("E")) {
                        if (lclQuotePiecesList.size() > 1 && lclQuoteAc.getArglMapping().getBlueScreenChargeCode().equalsIgnoreCase("0001")) {
                            lclQuoteAc.setLabel2("(MULTIPLE)");
                        } else {
                            if (lclQuoteAc.isManualEntry() && lclQuoteAc.getRateUom().equalsIgnoreCase("M")) {
                                lclQuoteAc.setLabel2("($" + lclUtils.convertToCft(lclQuoteAc.getRatePerVolumeUnit().doubleValue()) + " CFT or "
                                        + lclUtils.convertToLbs(lclQuoteAc.getRatePerWeightUnit().doubleValue()) + "/"
                                        + lclQuoteAc.getRatePerWeightUnitDiv() + " LBS,$" + lclQuoteAc.getRateFlatMinimum() + " MINIMUM)");
                            } else {
                                lclQuoteAc.setLabel2("($" + lclQuoteAc.getRatePerVolumeUnit() + " CFT or " + lclQuoteAc.getRatePerWeightUnit()
                                        + "/" + lclQuoteAc.getRatePerWeightUnitDiv() + " LBS,$" + lclQuoteAc.getRateFlatMinimum() + " MINIMUM)");
                            }
                        }
                    } else if (engmet.equalsIgnoreCase("M")) {
                        if (lclQuotePiecesList.size() > 1 && lclQuoteAc.getArglMapping().getBlueScreenChargeCode().equalsIgnoreCase("0001")) {
                            lclQuoteAc.setLabel2("(MULTIPLE)");
                        } else {
                            if (lclQuoteAc.isManualEntry() && lclQuoteAc.getRateUom().equalsIgnoreCase("I")) {
                                lclQuoteAc.setLabel2("($" + lclUtils.convertToCbm(lclQuoteAc.getRatePerVolumeUnit().doubleValue()) + "/CBM or "
                                        + lclUtils.convertToKgs(lclQuoteAc.getRatePerWeightUnit().doubleValue()) + "/"
                                        + lclQuoteAc.getRatePerWeightUnitDiv() + " KGS,$" + lclQuoteAc.getRateFlatMinimum() + " MINIMUM)");
                            } else {
                                lclQuoteAc.setLabel2("($" + lclQuoteAc.getRatePerVolumeUnit() + "/CBM or " + lclQuoteAc.getRatePerWeightUnit()
                                        + "/" + lclQuoteAc.getRatePerWeightUnitDiv() + " KGS,$" + lclQuoteAc.getRateFlatMinimum() + " MINIMUM)");
                            }
                        }
                    }
                }
            }
        }
    }

    public void formatLabelChargeForQuote(List<LclQuotePiece> lclQuotePiecesList, LclQuoteAc lclQuoteAc, String engmet) {
        if (lclQuoteAc.getRatePerUnitUom() != null && !lclQuoteAc.getRatePerUnitUom().trim().equals("")) {
            if (lclQuoteAc.getRatePerUnitUom().equalsIgnoreCase("FL")) {
                if (lclQuoteAc.getArglMapping() != null && lclQuoteAc.getArglMapping().getBlueScreenChargeCode() != null) {
                    if (lclQuoteAc.getArglMapping().getBlueScreenChargeCode().equals("0006")) {
                        String ratePerUnit = lclQuoteAc.getRatePerWeightUnit() != null ? lclQuoteAc.getRatePerWeightUnit().toString() : "";
                        String ratePeUnitDiv = lclQuoteAc.getRatePerWeightUnitDiv() != null ? lclQuoteAc.getRatePerWeightUnitDiv().toString() : "";
                        lclQuoteAc.setLabel2("(" + ratePerUnit + " PER " + ratePeUnitDiv + " CIF)");
                    } else {
                        lclQuoteAc.setLabel2("$" + lclQuoteAc.getArAmount().toString() + " FLAT RATE.");
                    }
                }
                if (lclQuoteAc.getArglMapping().getChargeCode() != null && (lclQuoteAc.getArglMapping().getChargeCode().equals("OFBARR")
                        || lclQuoteAc.getArglMapping().getChargeCode().equals("TTBARR"))) {
                    String ratePerWeightUnit = lclQuoteAc.getRatePerWeightUnit() != null ? lclQuoteAc.getRatePerWeightUnit().toString() : "";
                    lclQuoteAc.setLabel2("$" + ratePerWeightUnit + " PER BARREL.");
                }

            }
            if (lclQuoteAc.getRatePerUnitUom().equalsIgnoreCase("W")) {
                lclQuoteAc.setLabel1("*** TO WEIGHT ***");
            }
            if (lclQuoteAc.getRatePerUnitUom().equalsIgnoreCase("V")) {
                lclQuoteAc.setLabel1("*** VOLUME ***");
            }
            if (lclQuoteAc.getRatePerUnitUom().equalsIgnoreCase("M")) {
                lclQuoteAc.setLabel1("*** MINIMUM ***");
            }
            if (lclQuoteAc.getRatePerUnitUom().equalsIgnoreCase("PCT")) {
                if (lclQuoteAc.getRatePerUnit() != null) {
                    int ratePercentage = (int) (lclQuoteAc.getRatePerUnit().doubleValue() * 100);
                    lclQuoteAc.setLabel2(String.valueOf(ratePercentage) + " PERCENT OF THE B/L.");
                }
            }
            if (lclQuoteAc.getRatePerUnitUom().equalsIgnoreCase("FRW")) {
                lclQuoteAc.setLabel1("* O/F - TO WEIGHT *");
            }
            if (lclQuoteAc.getRatePerUnitUom().equalsIgnoreCase("FRV")) {
                lclQuoteAc.setLabel1("* O/F - VOLUME *");
            }
            if (lclQuoteAc.getRatePerUnitUom().equalsIgnoreCase("FRM")) {
                lclQuoteAc.setLabel1("* O/F - MINIMUM *");
            }
            if (lclQuoteAc.getRatePerUnitUom().equalsIgnoreCase("W") || lclQuoteAc.getRatePerUnitUom().equalsIgnoreCase("V")
                    || lclQuoteAc.getRatePerUnitUom().equalsIgnoreCase("M") || lclQuoteAc.getRatePerUnitUom().equalsIgnoreCase("FRW")
                    || lclQuoteAc.getRatePerUnitUom().equalsIgnoreCase("FRV") || lclQuoteAc.getRatePerUnitUom().equalsIgnoreCase("FRM")) {
                if (engmet != null) {
                    if (engmet.equalsIgnoreCase("E")) {
                        if (lclQuotePiecesList.size() > 1 && lclQuoteAc.getArglMapping().getBlueScreenChargeCode().equalsIgnoreCase("0001")) {
                            lclQuoteAc.setLabel2("MULTIPLE");
                        } else {
                            if (lclQuoteAc.isManualEntry() && lclQuoteAc.getRateUom().equalsIgnoreCase("M")) {
                                lclQuoteAc.setLabel2("$" + lclUtils.convertToCft(lclQuoteAc.getRatePerVolumeUnit().doubleValue()) + " CFT, "
                                        + lclUtils.convertToLbs(lclQuoteAc.getRatePerWeightUnit().doubleValue()) + "/"
                                        + lclQuoteAc.getRatePerWeightUnitDiv() + " LBS, ($" + lclQuoteAc.getRateFlatMinimum()
                                        + " MINIMUM)");
                            } else {
                                lclQuoteAc.setLabel2("$" + lclQuoteAc.getRatePerVolumeUnit() + " CFT, " + lclQuoteAc.getRatePerWeightUnit()
                                        + "/" + lclQuoteAc.getRatePerWeightUnitDiv() + " LBS, ($" + lclQuoteAc.getRateFlatMinimum() + " MINIMUM)");
                            }
                        }
                    } else if (engmet.equalsIgnoreCase("M")) {
                        if (lclQuotePiecesList.size() > 1 && lclQuoteAc.getArglMapping().getBlueScreenChargeCode().equalsIgnoreCase("0001")) {
                            lclQuoteAc.setLabel2("MULTIPLE");
                        } else {
                            if (lclQuoteAc.isManualEntry() && lclQuoteAc.getRateUom().equalsIgnoreCase("I")) {
                                lclQuoteAc.setLabel2("$" + lclUtils.convertToCbm(lclQuoteAc.getRatePerVolumeUnit().doubleValue()) + " CBM, "
                                        + lclUtils.convertToKgs(lclQuoteAc.getRatePerWeightUnit().doubleValue()) + "/"
                                        + lclQuoteAc.getRatePerWeightUnitDiv() + " KGS, ($" + lclQuoteAc.getRateFlatMinimum() + " MINIMUM)");
                            } else {
                                lclQuoteAc.setLabel2("$" + lclQuoteAc.getRatePerVolumeUnit() + " CBM, " + lclQuoteAc.getRatePerWeightUnit()
                                        + "/" + lclQuoteAc.getRatePerWeightUnitDiv() + " KGS, ($" + lclQuoteAc.getRateFlatMinimum() + " MINIMUM)");
                            }
                        }
                    }
                }
                if (lclQuoteAc.isManualEntry() && null != lclQuoteAc.getRatePerWeightUnit() && null != lclQuoteAc.getRatePerWeightUnit()
                        && lclQuoteAc.getRatePerUnit().doubleValue() > 0.00 && lclQuoteAc.getRatePerWeightUnit().doubleValue() > 0.00
                        && lclQuoteAc.getRatePerVolumeUnit().doubleValue() > 0.00) {
                    if (lclQuoteAc.getArglMapping().getBlueScreenChargeCode().equals("0351")) {
                    lclQuoteAc.setLabel2(lclQuoteAc.getLabel2());
                    }else{
                    lclQuoteAc.setLabel2("$" + lclQuoteAc.getRatePerUnit().toString() + " FLAT RATE + <br/>" + lclQuoteAc.getLabel2());
                    }
                }
            }

        }
    }

    public String calculateTotalByQuoteAcList(List<LclQuoteAc> lclQuoteAclist) {
        Double total = 0.0;
        Double adjustmentTotal = 0.0;
        if (lclQuoteAclist != null && lclQuoteAclist.size() > 0) {
            for (int i = 0; i < lclQuoteAclist.size(); i++) {
                LclQuoteAc lclQuoteAc = lclQuoteAclist.get(i);
                total = total + lclQuoteAc.getRolledupCharges().doubleValue();
                if (lclQuoteAc.getAdjustmentAmount() != null) {
                    adjustmentTotal = adjustmentTotal + lclQuoteAc.getAdjustmentAmount().doubleValue();
                }
            }
        }
        return NumberUtils.convertToTwoDecimal(total + adjustmentTotal);
    }

    public void setUpcomingSailings(LclQuote lclQuote, String relayFlag, HttpServletRequest request,String prevSailing) throws Exception {
        LclBookingPlanDAO bookingPlanDAO = new LclBookingPlanDAO();
        if (lclQuote.getPortOfOrigin() != null && lclQuote.getFinalDestination() != null) {
             String cfcl = lclQuote.getLclFileNumber().getLclBookingExport().isCfcl() ? "C" :"E" ;
            if (!lclQuote.getRelayOverride()) {
                LclBookingPlanBean bookingPlanBean = bookingPlanDAO.getRelay(lclQuote.getPortOfOrigin().getId(),
                        lclQuote.getFinalDestination().getId(), "N");
                if (bookingPlanBean != null) {
                    List<LclBookingVoyageBean> upcomingSailings = null;
                    if (prevSailing.equalsIgnoreCase("true")){
                        upcomingSailings = bookingPlanDAO.getUpComingSailingsScheduleOlder(lclQuote.getPortOfOrigin().getId(), bookingPlanBean.getPol_id(), bookingPlanBean.getPod_id(),
                            lclQuote.getFinalDestination().getId(), "V", bookingPlanBean,prevSailing,cfcl);
                    }else{
                         upcomingSailings = bookingPlanDAO.getUpComingSailingsSchedule(lclQuote.getPortOfOrigin().getId(), bookingPlanBean.getPol_id(), bookingPlanBean.getPod_id(),
                            lclQuote.getFinalDestination().getId(), "V", bookingPlanBean,cfcl);
                    }
                           
                    request.setAttribute("voyageList", upcomingSailings);
                }
//            setUpcomingSailing(lclQuote.getPortOfOrigin().getId(), lclQuote.getPortOfLoading().getId(),
//                    lclQuote.getPortOfDestination().getId(), lclQuote.getPortOfDestination().getId(),
//                    relayFlag, request);
            } else if (lclQuote.getPortOfLoading() != null && lclQuote.getPortOfDestination() != null
                    && lclQuote.getRelayOverride()) {
                LclBookingPlanBean bookingPlanBean = bookingPlanDAO.getRelayOverride(lclQuote.getPortOfOrigin().getId(),
                        lclQuote.getPortOfLoading().getId(), lclQuote.getPortOfDestination().getId(),
                        lclQuote.getFinalDestination().getId(), 0);
                if (bookingPlanBean != null) {
                    List<LclBookingVoyageBean> upcomingSailings = null;
                    if (prevSailing.equalsIgnoreCase("true")){
                        upcomingSailings = bookingPlanDAO.getUpComingSailingsScheduleOlder(lclQuote.getPortOfLoading().getId(),
                            lclQuote.getPortOfLoading().getId(), lclQuote.getPortOfDestination().getId(),
                            lclQuote.getPortOfDestination().getId(), "V", bookingPlanBean,prevSailing,cfcl);
                    }else{
                         upcomingSailings = bookingPlanDAO.getUpComingSailingsSchedule(lclQuote.getPortOfLoading().getId(),
                            lclQuote.getPortOfLoading().getId(), lclQuote.getPortOfDestination().getId(),
                            lclQuote.getPortOfDestination().getId(), "V", bookingPlanBean,cfcl);
                    }
                    request.setAttribute("voyageList", upcomingSailings);
                }
            }
            request.setAttribute("voyageAction", true);
        }
    }

    public void setUpcomingSailing(Integer pooId, Integer polId, Integer podId, Integer fdId, String relayFlag, HttpServletRequest request) throws Exception {
        LclBookingPlanDAO lclBookingPlanDAO = new LclBookingPlanDAO();
//        List<LclBookingVoyageBean> voyageList = lclBookingPlanDAO.getVoyageList(pooId,
//                polId, podId, fdId, "V");
//        if (null != relayFlag && !"".equalsIgnoreCase(relayFlag) && "Y".equalsIgnoreCase(relayFlag)) {
//            voyageList = lclBookingPlanDAO.getRelayVoyageList(polId, polId, podId, podId, "V");
//        } else {
//            if (CommonUtils.isEmpty(voyageList)) {
//                voyageList = lclBookingPlanDAO.getVoyageList(polId, polId, podId, podId, "V");
//            }
//        }
        // Collections.sort(voyageList, new LclVoyageComparator());
        // request.setAttribute("voyageList", voyageList);
        request.setAttribute("voyageAction", true);
    }

    public void setRelayDetails(Integer pooId, Integer fdId, HttpServletRequest request, LCLQuoteForm quoteForm) throws Exception {
        UnLocationDAO unLocationDAO = new UnLocationDAO();
        String polState = "";
        String podState = "";
        //  LclBookingPlanBean bookingPlanBean = new LclBookingPlanDAO().lclRelayFind(pooId, fdId);
//        if (bookingPlanBean != null && CommonUtils.isNotEmpty(bookingPlanBean.getPolName()) && CommonUtils.isNotEmpty(bookingPlanBean.getPolName())) {
//            UnLocation unlocation = unLocationDAO.findById(Integer.parseInt(bookingPlanBean.getPolId()));
//            if (unlocation != null && unlocation.getCountryId() != null) {
//                if ("UNITED STATES".equalsIgnoreCase(unlocation.getCountryId().getCodedesc()) && null != unlocation.getStateId()) {
//                    polState = unlocation.getStateId().getCode();
//                } else if (null != unlocation.getCountryId().getCodedesc()) {
//                    polState = unlocation.getCountryId().getCodedesc();
//                }
//            }
//            unlocation = unLocationDAO.findById(Integer.parseInt(bookingPlanBean.getPodId()));
//            if (null != unlocation && null != unlocation.getCountryId()) {
//                if ("UNITED STATES".equalsIgnoreCase(unlocation.getCountryId().getCodedesc()) && null != unlocation.getStateId()) {
//                    podState = unlocation.getStateId().getCode();
//                } else if (null != unlocation.getCountryId().getCodedesc()) {
//                    podState = unlocation.getCountryId().getCodedesc();
//                }
//            }
//            request.setAttribute("pol", bookingPlanBean.getPolName() + "/" + polState + "(" + bookingPlanBean.getPolCode() + ")");
//            request.setAttribute("pod", bookingPlanBean.getPodName() + "/" + podState + "(" + bookingPlanBean.getPodCode() + ")");
//            request.setAttribute("podUnlocationcode", bookingPlanBean.getPodCode());
//            request.setAttribute("polUnlocationcode", bookingPlanBean.getPolCode());
//            request.setAttribute("portOfLoadingId", bookingPlanBean.getPolId());
//            request.setAttribute("portOfDestinationId", bookingPlanBean.getPodId());
        request.setAttribute("relaySearch", "RelayAvailable");//remove
//        } else {
//            if (!"".equalsIgnoreCase(quoteForm.getPortOfOrigin())) {
//                request.setAttribute("pol", quoteForm.getPortOfOrigin());
//                request.setAttribute("pod", quoteForm.getFinalDestination());
//                request.setAttribute("polUnlocationcode", quoteForm.getOriginUnlocationCode());
//                request.setAttribute("podUnlocationcode", quoteForm.getUnlocationCode());
//                request.setAttribute("portOfLoadingId", quoteForm.getPortOfOriginId());
//                request.setAttribute("portOfDestinationId", quoteForm.getFinalDestinationId());
//                request.setAttribute("relayOverRide", true);
//            }
//            request.setAttribute("relaySearch", "");
        // }
    }
    //set POA and CreditStatus for all vendors.This method is used to savebooking and editBooking

    public void setPoaandCreditStatusValues(LCLQuoteForm lclQuoteForm, LclQuote lclQuote, String moduleName, HttpServletRequest request) throws Exception {
        LclDwr lclDwr = new LclDwr();
        NotesDAO notesDAO = new NotesDAO();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        if (lclQuote.getClientAcct() != null && lclQuote.getClientAcct().getAccountno() != null
                && !lclQuote.getClientAcct().getAccountno().equals("")) {
            if (LCL_IMPORT.equalsIgnoreCase(moduleName)) {
                lclQuoteForm.setClientIcon(notesDAO.isShipCustomerNotesForImports(lclQuote.getClientAcct().getAccountno()));
            } else {
                lclQuoteForm.setClientIcon(notesDAO.isCustomerNotes(lclQuote.getClientAcct().getAccountno()));
                request.setAttribute("CreditForClient", genericCodeDAO.getCreditStatus(lclQuote.getClientAcct().getAccountno()));
            }
        }
        if (LCL_EXPORT.equalsIgnoreCase(moduleName) && lclQuote.getFwdAcct() != null && lclQuote.getFwdAcct().getAccountno() != null && !lclQuote.getFwdAcct().getAccountno().equals("")) {
            lclQuoteForm.setForwaderIcon(notesDAO.isCustomerNotes(lclQuote.getFwdAcct().getAccountno()));
            request.setAttribute("CreditForForwarder", genericCodeDAO.getCreditStatus(lclQuote.getFwdAcct().getAccountno()));
        }
        if (lclQuote.getShipAcct() != null && lclQuote.getShipAcct().getAccountno() != null && !lclQuote.getShipAcct().getAccountno().equals("")) {
            if (LCL_IMPORT.equalsIgnoreCase(moduleName)) {
                lclQuoteForm.setShipperToolTip(lclDwr.getContactDetails("Shipper", lclQuote.getShipAcct().getAccountno(), lclQuote.getShipAcct().getAccountName(), ""));
                request.setAttribute("CreditForShipper", genericCodeDAO.getCreditStatusForImports(lclQuote.getShipAcct().getAccountno()));
                lclQuoteForm.setShipperIcon(notesDAO.isShipCustomerNotesForImports(lclQuote.getShipAcct().getAccountno()));
            } else {
                lclQuoteForm.setShipperIcon(notesDAO.isCustomerNotes(lclQuote.getShipAcct().getAccountno()));
                request.setAttribute("CreditForShipper", genericCodeDAO.getCreditStatus(lclQuote.getShipAcct().getAccountno()));
            }
        } else {
            lclQuoteForm.setShipperToolTip(lclDwr.getContactDetails("Shipper", "", "", ""));
        }
        if (lclQuote.getConsAcct() != null && lclQuote.getConsAcct().getAccountno() != null && !lclQuote.getConsAcct().getAccountno().equals("")) {
            if (LCL_IMPORT.equalsIgnoreCase(moduleName)) {
                lclQuoteForm.setConsigneeToolTip(lclDwr.getContactDetails("Consignee", lclQuote.getConsAcct().getAccountno(), lclQuote.getConsContact().getCompanyName(), ""));
                lclQuoteForm.setConsigneeIcon(notesDAO.isCustomerNotesForImports(lclQuote.getConsAcct().getAccountno()));
                request.setAttribute("CreditForConsignee", genericCodeDAO.getCreditStatusForImports(lclQuote.getConsAcct().getAccountno()));
            } else {
                request.setAttribute("CreditForConsignee", genericCodeDAO.getCreditStatus(lclQuote.getConsAcct().getAccountno()));
                lclQuoteForm.setConsigneeIcon(notesDAO.isCustomerNotes(lclQuote.getConsAcct().getAccountno()));
            }
        } else {
            lclQuoteForm.setConsigneeToolTip(lclDwr.getContactDetails("Consignee", "", "", ""));
        }
        if (lclQuote.getNotyAcct() != null && lclQuote.getNotyAcct().getAccountno() != null && !lclQuote.getNotyAcct().getAccountno().equals("")) {
            if (LCL_IMPORT.equalsIgnoreCase(moduleName)) {
                lclQuoteForm.setNotifyToolTip(lclDwr.getContactDetails("Notify", lclQuote.getNotyAcct().getAccountno(), lclQuote.getNotyContact().getCompanyName(), ""));
                lclQuoteForm.setNotifyIcon(notesDAO.isCustomerNotesForImports(lclQuote.getNotyAcct().getAccountno()));
                request.setAttribute("CreditForNotify", genericCodeDAO.getCreditStatusForImports(lclQuote.getNotyAcct().getAccountno()));
            } else {
                request.setAttribute("CreditForNotify", genericCodeDAO.getCreditStatus(lclQuote.getNotyAcct().getAccountno()));
                lclQuoteForm.setNotifyIcon(notesDAO.isCustomerNotes(lclQuote.getNotyAcct().getAccountno()));
            }
        } else {
            lclQuoteForm.setNotifyToolTip(lclDwr.getContactDetails("Notify", "", "", ""));
        }
        if (LCL_IMPORT.equalsIgnoreCase(moduleName) && lclQuote.getNotify2Contact() != null && lclQuote.getNotify2Contact().getTradingPartner() != null
                && lclQuote.getNotify2Contact().getTradingPartner().getAccountno() != null) {
            lclQuoteForm.setNotify2ToolTip(lclDwr.getContactDetails("Notify2", lclQuote.getNotify2Contact().getTradingPartner().getAccountno(), lclQuote.getNotify2Contact().getTradingPartner().getAccountName(), ""));
            lclQuoteForm.setNotify2Icon(notesDAO.isCustomerNotesForImports(lclQuote.getNotify2Contact().getTradingPartner().getAccountno()));
            request.setAttribute("creditForNotify2", genericCodeDAO.getCreditStatusForImports(lclQuote.getNotify2Contact().getTradingPartner().getAccountno()));
        } else {
            lclQuoteForm.setNotify2ToolTip(lclDwr.getContactDetails("Notify2", "", "", ""));
        }
    }

    public void saveQtePlan(LCLQuoteForm lclQuoteForm, LclFileNumber lclFileNumber, User loginUser, Date todayDate) throws Exception {
        LclQuotePlanDAO lclQuotePlanDAO = new LclQuotePlanDAO();//insert the record into the lcl_quote_plan table
        if (lclQuoteForm.getPortOfOriginId() != null && lclQuoteForm.getFinalDestinationId() != null) {
            LclQuotePlan lclQuotePlan = lclQuotePlanDAO.getByProperty("lclFileNumber.id", lclFileNumber.getId());
            if (lclQuotePlan == null) {
                lclQuotePlan = new LclQuotePlan();
                lclQuotePlan.setEnteredBy(loginUser);
                lclQuotePlan.setEnteredDatetime(todayDate);
            }
            lclQuotePlan.setFromId(new UnLocation(lclQuoteForm.getPortOfOriginId()));
            lclQuotePlan.setToId(new UnLocation(lclQuoteForm.getFinalDestinationId()));
            lclQuotePlan.setLclFileNumber(lclFileNumber);
            lclQuotePlan.setSegNo(4);
            lclQuotePlan.setModifiedBy(loginUser);
            lclQuotePlan.setModifiedDatetime(todayDate);
            lclQuotePlanDAO.saveOrUpdate(lclQuotePlan);
        }
    }

    public void addBkgUpdateorDeleteContactNotes(LCLQuoteForm lclQuoteForm, LclFileNumber lclFileNumber, User loginUser, LclRemarksDAO lclRemarksDAO) throws Exception {
        String contactName = "";
        if (lclQuoteForm.getLclQuote() != null
                && (lclQuoteForm.getLclQuote().getClientContact() != null || lclQuoteForm.getLclQuote().getShipContact() != null
                || lclQuoteForm.getLclQuote().getConsContact() != null || lclQuoteForm.getLclQuote().getFwdContact() != null)) {
            if (lclQuoteForm.getLclQuote() != null && lclQuoteForm.getLclQuote().getClientContact() != null
                    && lclQuoteForm.getLclQuote().getClientContact().getContactName() != null
                    && !lclQuoteForm.getLclQuote().getClientContact().getContactName().equals("")) {
                contactName = lclQuoteForm.getLclQuote().getClientContact().getContactName();
            } else if (lclQuoteForm.getLclQuote() != null && lclQuoteForm.getLclQuote().getShipContact() != null && lclQuoteForm.getLclQuote().getShipContact().getContactName() != null
                    && !lclQuoteForm.getLclQuote().getShipContact().getContactName().equals("")) {
                contactName = lclQuoteForm.getLclQuote().getShipContact().getContactName();
            } else if (lclQuoteForm.getLclQuote() != null && lclQuoteForm.getLclQuote().getConsContact() != null && lclQuoteForm.getLclQuote().getConsContact().getContactName() != null
                    && !lclQuoteForm.getLclQuote().getConsContact().getContactName().equals("")) {
                contactName = lclQuoteForm.getLclQuote().getConsContact().getContactName();
            } else if (lclQuoteForm.getLclQuote() != null && lclQuoteForm.getLclQuote().getFwdContact() != null && lclQuoteForm.getLclQuote().getFwdContact().getContactName() != null
                    && !lclQuoteForm.getLclQuote().getFwdContact().getContactName().equals("")) {
                contactName = lclQuoteForm.getLclQuote().getFwdContact().getContactName();
            }
        }
        String email = "";
        if (lclQuoteForm.getLclQuote() != null
                && (lclQuoteForm.getLclQuote().getClientContact() != null || lclQuoteForm.getLclQuote().getShipContact() != null
                || lclQuoteForm.getLclQuote().getConsContact() != null || lclQuoteForm.getLclQuote().getFwdContact() != null)) {
            if (lclQuoteForm.getLclQuote() != null && lclQuoteForm.getLclQuote().getClientContact() != null
                    && lclQuoteForm.getLclQuote().getClientContact().getEmail1() != null
                    && !lclQuoteForm.getLclQuote().getClientContact().getEmail1().equals("")) {
                email = lclQuoteForm.getLclQuote().getClientContact().getEmail1();
            } else if (lclQuoteForm.getLclQuote() != null && lclQuoteForm.getLclQuote().getShipContact() != null && lclQuoteForm.getLclQuote().getShipContact().getEmail1() != null
                    && !lclQuoteForm.getLclQuote().getShipContact().getEmail1().equals("")) {
                email = lclQuoteForm.getLclQuote().getShipContact().getEmail1();
            } else if (lclQuoteForm.getLclQuote() != null && lclQuoteForm.getLclQuote().getConsContact() != null && lclQuoteForm.getLclQuote().getConsContact().getEmail1() != null
                    && !lclQuoteForm.getLclQuote().getConsContact().getEmail1().equals("")) {
                email = lclQuoteForm.getLclQuote().getConsContact().getEmail1();
            } else if (lclQuoteForm.getLclQuote() != null && lclQuoteForm.getLclQuote().getFwdContact() != null && lclQuoteForm.getLclQuote().getFwdContact().getEmail1() != null
                    && !lclQuoteForm.getLclQuote().getFwdContact().getEmail1().equals("")) {
                email = lclQuoteForm.getLclQuote().getFwdContact().getEmail1();
            }
        }
        if (((lclQuoteForm.getBookingContact() != null && !lclQuoteForm.getBookingContact().equals(""))
                || (lclQuoteForm.getBookingContactEmail() != null && !lclQuoteForm.getBookingContactEmail().equals("")))
                && (!lclQuoteForm.getBookingContact().equals(contactName) || !lclQuoteForm.getBookingContactEmail().equals(email))) {
            String remarks = "UPDATED -> Booking Contact Name -> " + lclQuoteForm.getBookingContact() + " to " + contactName
                    + ", Booking Contact Email -> " + lclQuoteForm.getBookingContactEmail() + " to " + email;
            lclRemarksDAO.insertLclRemarks(lclFileNumber.getId(), REMARKS_QT_AUTO_NOTES, remarks, loginUser.getUserId());
        }
        if (CommonUtils.isEmpty(contactName) && CommonUtils.isEmpty(email)
                && (CommonUtils.isNotEmpty(lclQuoteForm.getBookingContact())
                || CommonUtils.isNotEmpty(lclQuoteForm.getBookingContactEmail()))) {
            String remarks = "DELETED -> Booking Contact Name -> " + lclQuoteForm.getBookingContact()
                    + ", Booking Contact Email -> " + lclQuoteForm.getBookingContactEmail();
            lclRemarksDAO.insertLclRemarks(lclFileNumber.getId(), REMARKS_QT_AUTO_NOTES, remarks, loginUser.getUserId());
        }
    }

    public void addInsertBkgContactNotes(LCLQuoteForm lclQuoteForm, LclFileNumber lclFileNumber, User loginUser, LclRemarksDAO lclRemarksDAO) throws Exception {
        String contactName = "";
        String email = "";
        if (CommonUtils.isEmpty(lclQuoteForm.getBookingContact()) && CommonUtils.isEmpty(lclQuoteForm.getBookingContactEmail())) {
            if (lclQuoteForm.getLclQuote() != null
                    && (lclQuoteForm.getLclQuote().getClientContact() != null || lclQuoteForm.getLclQuote().getShipContact() != null
                    || lclQuoteForm.getLclQuote().getConsContact() != null || lclQuoteForm.getLclQuote().getFwdContact() != null)) {
                if (lclQuoteForm.getLclQuote() != null && lclQuoteForm.getLclQuote().getClientContact() != null
                        && lclQuoteForm.getLclQuote().getClientContact().getContactName() != null
                        && !lclQuoteForm.getLclQuote().getClientContact().getContactName().equals("") && !lclQuoteForm.getLclQuote().getClientContact().getContactName().equals(lclQuoteForm.getBookingContact())) {
                    contactName = lclQuoteForm.getLclQuote().getClientContact().getContactName();
                } else if (lclQuoteForm.getLclQuote() != null && lclQuoteForm.getLclQuote().getShipContact() != null && lclQuoteForm.getLclQuote().getShipContact().getContactName() != null
                        && !lclQuoteForm.getLclQuote().getShipContact().getContactName().equals("") && !lclQuoteForm.getLclQuote().getShipContact().getContactName().equals(lclQuoteForm.getBookingContact())) {
                    contactName = lclQuoteForm.getLclQuote().getShipContact().getContactName();
                } else if (lclQuoteForm.getLclQuote() != null && lclQuoteForm.getLclQuote().getConsContact() != null && lclQuoteForm.getLclQuote().getConsContact().getContactName() != null
                        && !lclQuoteForm.getLclQuote().getConsContact().getContactName().equals("") && !lclQuoteForm.getLclQuote().getConsContact().getContactName().equals(lclQuoteForm.getBookingContact())) {
                    contactName = lclQuoteForm.getLclQuote().getConsContact().getContactName();
                } else if (lclQuoteForm.getLclQuote() != null && lclQuoteForm.getLclQuote().getFwdContact() != null && lclQuoteForm.getLclQuote().getFwdContact().getContactName() != null
                        && !lclQuoteForm.getLclQuote().getFwdContact().getContactName().equals("") && !lclQuoteForm.getLclQuote().getFwdContact().getContactName().equals(lclQuoteForm.getBookingContact())) {
                    contactName = lclQuoteForm.getLclQuote().getFwdContact().getContactName();
                }
            }

            if (lclQuoteForm.getLclQuote() != null
                    && (lclQuoteForm.getLclQuote().getClientContact() != null || lclQuoteForm.getLclQuote().getShipContact() != null
                    || lclQuoteForm.getLclQuote().getConsContact() != null || lclQuoteForm.getLclQuote().getFwdContact() != null)) {
                if (lclQuoteForm.getLclQuote() != null && lclQuoteForm.getLclQuote().getClientContact() != null
                        && lclQuoteForm.getLclQuote().getClientContact().getEmail1() != null
                        && !lclQuoteForm.getLclQuote().getClientContact().getEmail1().equals("") && !lclQuoteForm.getLclQuote().getClientContact().getEmail1().equals(lclQuoteForm.getBookingContactEmail())) {
                    email = lclQuoteForm.getLclQuote().getClientContact().getEmail1();
                } else if (lclQuoteForm.getLclQuote() != null && lclQuoteForm.getLclQuote().getShipContact() != null && lclQuoteForm.getLclQuote().getShipContact().getEmail1() != null
                        && !lclQuoteForm.getLclQuote().getShipContact().getEmail1().equals("") && !lclQuoteForm.getLclQuote().getShipContact().getEmail1().equals(lclQuoteForm.getBookingContactEmail())) {
                    email = lclQuoteForm.getLclQuote().getShipContact().getEmail1();
                } else if (lclQuoteForm.getLclQuote() != null && lclQuoteForm.getLclQuote().getConsContact() != null && lclQuoteForm.getLclQuote().getConsContact().getEmail1() != null
                        && !lclQuoteForm.getLclQuote().getConsContact().getEmail1().equals("") && !lclQuoteForm.getLclQuote().getConsContact().getEmail1().equals(lclQuoteForm.getBookingContactEmail())) {
                    email = lclQuoteForm.getLclQuote().getConsContact().getEmail1();
                } else if (lclQuoteForm.getLclQuote() != null && lclQuoteForm.getLclQuote().getFwdContact() != null && lclQuoteForm.getLclQuote().getFwdContact().getEmail1() != null
                        && !lclQuoteForm.getLclQuote().getFwdContact().getEmail1().equals("") && !lclQuoteForm.getLclQuote().getFwdContact().getEmail1().equals(lclQuoteForm.getBookingContactEmail())) {
                    email = lclQuoteForm.getLclQuote().getFwdContact().getEmail1();
                }
            }
            if ((contactName != null && !contactName.equals("")) || (email != null && !email.equals(""))) {
                String remarks = "INSERTED -> Booking Contact Name -> " + contactName
                        + ", Booking Contact Email -> " + email;
                lclRemarksDAO.insertLclRemarks(lclFileNumber.getId(), REMARKS_QT_AUTO_NOTES, remarks, loginUser.getUserId());
            }
        }
    }

    public void setTrmandEciPortCode(LCLQuoteForm lclQuoteForm, LclQuote lclQuote, LclUtils lclUtils) throws Exception {
        String pooUnloCode = lclQuote.getPortOfOrigin() != null ? lclQuote.getPortOfOrigin().getUnLocationCode() : "";
        String polUnloCode = lclQuote.getPortOfLoading() != null ? lclQuote.getPortOfLoading().getUnLocationCode() : "";
        String podUnloCode = lclQuote.getPortOfDestination() != null ? lclQuote.getPortOfDestination().getUnLocationCode() : "";
        String fdUnloCode = lclQuote.getFinalDestination() != null ? lclQuote.getFinalDestination().getUnLocationCode() : "";
        String rateType = "R".equalsIgnoreCase(lclQuote.getRateType()) ? "Y" : lclQuote.getRateType();
        List l = lclUtils.getTrmNumandEciPortCode(pooUnloCode, polUnloCode, podUnloCode, fdUnloCode, rateType);
        for (Object row : l) {
            Object[] col = (Object[]) row;
            if (col[2].toString().equalsIgnoreCase("POO")) {
                lclQuoteForm.setPooTrmNum((String) col[0]);
            }
            if (col[2].toString().equalsIgnoreCase("POL")) {
                lclQuoteForm.setPolTrmNum((String) col[0]);
            }
            if (col[2].toString().equalsIgnoreCase("POD")) {
                lclQuoteForm.setPodEciPortCode((String) col[0]);
            }
            if (col[2].toString().equalsIgnoreCase("FD")) {
                lclQuoteForm.setFdEciPortCode((String) col[0]);
                lclQuoteForm.setFdEngmet((String) col[1]);
            }
        }
    }

    public void setBookingExportFromQuote(LclQuote quote, LclFileNumber lclFileNumber, HttpServletRequest request, User loginUser) throws Exception {
        LclBookingExport lclBookingExport = new LclBookingExport();
        Warehouse wareHouse = null;
        if (null != lclFileNumber) {
            lclBookingExport = new LclBookingExportDAO().getByProperty("lclFileNumber.id", lclFileNumber.getId());
        }
        if (null == lclBookingExport) {
            lclBookingExport = new LclBookingExport();
            lclBookingExport.setEnteredBy(loginUser);
            lclBookingExport.setEnteredDatetime(new Date());
        }
        LclQuoteImport quoteImports = new LclQuoteImportDAO().findById(quote.getFileNumberId());
        String unLocation = "";
        if ("T".equalsIgnoreCase(quote.getQuoteType())) {
            unLocation = null != quoteImports.getUsaPortOfExit() ? quoteImports.getUsaPortOfExit().getUnLocationCode() : "";
        } else {
            unLocation = null != quote.getPortOfOrigin() ? quote.getPortOfOrigin().getUnLocationCode() : "";
        }
        if (!"".equalsIgnoreCase(unLocation)) {
            RefTerminal terminal = new RefTerminalDAO().getTerminalByUnLocation(unLocation, "Y");
            wareHouse = new WarehouseDAO().getWareHouseBywarehsNo(terminal != null ? "W" + terminal.getTrmnum() : "");
            lclBookingExport.setFileNumberId(lclFileNumber.getId());
            lclBookingExport.setOrginWarehouse(null != wareHouse ? wareHouse : null);
            lclBookingExport.setRtAgentAcct(null);
            lclBookingExport.setDeliverPickup("P");
            lclBookingExport.setDeliverPickupDatetime(new Date());
            lclBookingExport.setModifiedBy(loginUser);
            lclBookingExport.setModifiedDatetime(new Date());
            //lclBookingExport.setAes(false);
            //lclBookingExport.setUps(false);
            new LclBookingExportDAO().saveOrUpdate(lclBookingExport);
        }
    }

    public void addDefaultHotCodesOfCustomer(LclQuote quote, Integer userId) throws Exception {
        List accountList = new ArrayList();
//        accountList.add(quote.getShipAcct() != null ? quote.getShipAcct().getAccountno() : "");
//        accountList.add(quote.getClientAcct() != null ? quote.getClientAcct().getAccountno() : "");
//        accountList.add(quote.getConsAcct() != null ? quote.getConsAcct().getAccountno() : "");
//        accountList.add(quote.getFwdAcct() != null ? quote.getFwdAcct().getAccountno() : "");
//        List hotCodeList = new GeneralInformationDAO().getHotCodesForMultieAccount(accountList);
        List<LclQuoteHotCode> quoteHotcodelist = new LclQuoteHotCodeDAO().getHotCodeList(quote.getLclFileNumber().getId());
//        if (CommonUtils.isNotEmpty(quoteHotcodelist)) {
//            for (LclQuoteHotCode code : quoteHotcodelist) {
//                if (!hotCodeList.contains(code.getCode())) {
//                    hotCodeList.add(code.getCode());
//                }
//            }
//        }
        if (CommonUtils.isNotEmpty(quoteHotcodelist)) {
            LclBookingHotCodeDAO hotCodeDAO = new LclBookingHotCodeDAO();
            for (LclQuoteHotCode hotCode : quoteHotcodelist) {
                if (hotCodeDAO.isHotCodeNotExist(hotCode.getCode(), quote.getLclFileNumber().getId().toString())) {
                    hotCodeDAO.insertQuery(quote.getLclFileNumber().getId().toString(), hotCode.getCode(), userId);
                }
            }
        }
    }

    public void setLclBookingExport(LCLQuoteForm lclQuoteForm, LclFileNumber lclFileNumber,
            HttpServletRequest request, User loginUser) throws Exception {
        Warehouse wareHouse = null;
        HttpSession session = request.getSession();
        LclSession lclSession = null != (LclSession) session.getAttribute("lclSession") ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        LclBookingExport lclBookingExport = new LclBookingExportDAO().getByProperty("lclFileNumber.id", lclFileNumber.getId());
        if (null == lclBookingExport) {
            lclBookingExport = new LclBookingExport();
            lclBookingExport.setEnteredBy(loginUser);
            lclBookingExport.setEnteredDatetime(new Date());
            lclBookingExport.setFileNumberId(lclFileNumber.getId());
            lclBookingExport.setDeliverPickup("P");
            lclBookingExport.setDeliverPickupDatetime(new Date());
        }
        String unLocation = "";
        if ("T".equalsIgnoreCase(lclQuoteForm.getLclQuote().getQuoteType())) {
            unLocation = CommonUtils.isNotEmpty(lclQuoteForm.getPortExit()) ? lclQuoteForm.getPortExit() : "";
        } else {
            unLocation = CommonUtils.isNotEmpty(lclQuoteForm.getPortOfOrigin()) ? lclQuoteForm.getPortOfOrigin() : "";
        }
        if (!"".equalsIgnoreCase(unLocation)) {
            unLocation = unLocation.substring(unLocation.indexOf("(") + 1, unLocation.indexOf(")"));
            RefTerminal terminal = new RefTerminalDAO().getTerminalByUnLocation(unLocation, "Y");
            wareHouse = new WarehouseDAO().getWareHouseBywarehsNo(terminal != null ? "W" + terminal.getTrmnum() : "");
        }
        lclBookingExport.setStorageDatetime(CommonUtils.isNotEmpty(lclQuoteForm.getStorageDate())
                ? DateUtils.parseDate(lclQuoteForm.getStorageDate(), "dd-MMM-yyyy hh:mm:ss a") : null);
        lclBookingExport.setOrginWarehouse(null != wareHouse ? wareHouse : null);
        lclBookingExport.setRtAgentAcct(null);
        lclBookingExport.setIncludeDestfees(lclSession.isIncludeDestfees());
        lclBookingExport.setModifiedBy(loginUser);
        lclBookingExport.setModifiedDatetime(new Date());
        lclBookingExport.setCfcl(null != lclQuoteForm.getCfcl() ? lclQuoteForm.getCfcl() : false);
        lclBookingExport.setAes(null != lclQuoteForm.getAesBy() ? lclQuoteForm.getAesBy() : false);
        lclBookingExport.setUps(lclQuoteForm.isUps());
        lclBookingExport.setCfclAcctNo(CommonUtils.isNotEmpty(lclQuoteForm.getCfclAcctNo())
                ? new TradingPartnerDAO().findById(lclQuoteForm.getCfclAcctNo()) : null);
        new LclBookingExportDAO().saveOrUpdate(lclBookingExport);
        request.setAttribute("lclBookingExport", lclBookingExport);
    }

    public void refreshRates(LCLQuoteForm quoteForm, HttpServletRequest request,
            Long fileId, LclQuote lclQuote, User loginUser, List<LclQuotePiece> quotePieceList) throws Exception {
        LclQuoteAcDAO lclquoteacdao = new LclQuoteAcDAO();
        LclQuotationChargesCalculation chargeCal = new LclQuotationChargesCalculation();
        ExportQuoteUtils exportQuoteUtils = new ExportQuoteUtils();

        String rateType = "R".equalsIgnoreCase(quoteForm.getRateType()) ? "Y" : quoteForm.getRateType();
        String fromZip = "";
        if (quoteForm.getDoorOriginCityZip() != null && !quoteForm.getDoorOriginCityZip().trim().equals("")) {
            String[] zip = quoteForm.getDoorOriginCityZip().split("-");
            fromZip = zip[0];
        }
        String pickupReadyDate = new LCLQuoteDAO().getPickupReadyDate(fileId);
        chargeCal.calculateRates(quoteForm.getOriginUnlocationCode(), quoteForm.getDestinationCode(), quoteForm.getPolUnlocationcode(),
                quoteForm.getPodUnlocationcode(), fileId, quotePieceList, loginUser, quoteForm.getPooDoor(),
                quoteForm.getInsurance(), quoteForm.getLclQuote().getValueOfGoods(), rateType, "C", null, pickupReadyDate,
                fromZip, null, quoteForm.getCalcHeavy(), quoteForm.getDeliveryMetro(), quoteForm.getPcBoth(), null, "", request);
        request.setAttribute("highVolumeMessage", chargeCal.getHighVolumeMessage());
        if (chargeCal.getHighVolumeMessage() != null && !"".equalsIgnoreCase(chargeCal.getHighVolumeMessage())) {
            LclRemarksDAO remarksDAO = new LclRemarksDAO();
            LclRemarks lclRemarks = remarksDAO.getRemarks(fileId, "AutoRates", null);
            if (lclRemarks == null) {
                lclRemarks = new LclRemarks();
                lclRemarks.setEnteredBy(loginUser);
                lclRemarks.setEnteredDatetime(new Date());
            }
            lclRemarks.setLclFileNumber(lclQuote.getLclFileNumber());
            lclRemarks.setType("AutoRates");
            lclRemarks.setRemarks(chargeCal.getHighVolumeMessage());
            lclRemarks.setModifiedBy(loginUser);
            lclRemarks.setModifiedDatetime(new Date());
            remarksDAO.saveOrUpdate(lclRemarks);
        }
        request.setAttribute("lclQuote", lclQuote);
        HttpSession session = request.getSession();
        LclSession lclSession = (LclSession) session.getAttribute("lclSession") != null ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        request.setAttribute("lclSession", lclSession);
        List<LclQuoteAc> chargeList = lclquoteacdao.getLclCostByFileNumberAsc(fileId, LCL_EXPORT);
        exportQuoteUtils.setWeighMeasureForQuote(request, quotePieceList, quoteForm.getFdEngmet());
        exportQuoteUtils.setRolledUpChargesForQuote(chargeList, request, fileId, lclquoteacdao,
                quotePieceList, quoteForm.getBillingType(), quoteForm.getFdEngmet(), "No");
    }

    public void calculateSpotRate(Long file_number_id, LclQuote lclQuote, String billing_type,
            String spot_Rate_cbm, String Spot_rate_cft, Boolean isOnlyOcnfrt, Boolean spotCheckBottom,
            String spotComment, String spotRateCommodity, HttpServletRequest request,
            List<LclQuotePiece> lclQuotePiecesList) throws Exception {
        LclQuoteAcDAO lclQuoteAcDAO = new LclQuoteAcDAO();
        String fdEngmet = "", rate_uom = "";
        Spot_rate_cft = "".equalsIgnoreCase(Spot_rate_cft) ? "0.00" : Spot_rate_cft;
        spot_Rate_cbm = "".equalsIgnoreCase(spot_Rate_cbm) ? "0.00" : spot_Rate_cbm;
        String fdUnloCode = lclQuote.getFinalDestination() != null ? lclQuote.getFinalDestination().getUnLocationCode() : "";
        List l = new LclUtils().getTrmNumandEciPortCode("", "", "", fdUnloCode, "");
        for (Object row : l) {
            Object[] col = (Object[]) row;
            if (col[2].toString().equalsIgnoreCase("FD")) {
                fdEngmet = (String) col[1];
            }
        }
        Double CFT = 0.00, CBM = 0.00, OCN_CFT = 0.00, OCN_CBM = 0.00;
        LclQuoteAc ocean_Freight_Rate = lclQuoteAcDAO.manaualChargeValidate(file_number_id, "OCNFRT", false);
        if (null != ocean_Freight_Rate) {

            List<Object[]> rates_list = lclQuoteAcDAO.getSpotRateCharge(file_number_id);
            for (Object[] obj : rates_list) {
                CFT = Double.parseDouble(obj[0].toString());
                CBM = Double.parseDouble(obj[1].toString());
                OCN_CFT = Double.parseDouble(obj[2].toString());
                OCN_CBM = Double.parseDouble(obj[3].toString());

            }
            LclQuotePiece commodity = lclQuotePiecesList.isEmpty() ? new LclQuotePiece() : lclQuotePiecesList.get(0);
            LclQuoteAc TT_rev_charge = lclQuoteAcDAO.getTTCharges(file_number_id, false);
            BigDecimal total_weight = BigDecimal.ZERO;
            BigDecimal total_measure = BigDecimal.ZERO;
            BigDecimal comm_measure = BigDecimal.ZERO;
            BigDecimal comm_weight = BigDecimal.ZERO;

            ocean_Freight_Rate.setRatePerVolumeUnit(BigDecimal.ZERO);
            if ("E".equalsIgnoreCase(fdEngmet)) {
                comm_measure = commodity.getActualVolumeImperial() != null && commodity.getActualVolumeImperial().compareTo(BigDecimal.ZERO) > 0
                        ? commodity.getActualVolumeImperial() : commodity.getBookedVolumeImperial();
                comm_weight = commodity.getActualWeightImperial() != null && commodity.getActualWeightImperial().compareTo(BigDecimal.ZERO) > 0
                        ? commodity.getActualWeightImperial() : commodity.getBookedWeightImperial();
            } else {
                comm_measure = commodity.getActualVolumeMetric() != null && commodity.getActualVolumeMetric().compareTo(BigDecimal.ZERO) > 0
                        ? commodity.getActualVolumeMetric() : commodity.getBookedVolumeMetric();
                comm_weight = commodity.getActualWeightMetric() != null && commodity.getActualWeightMetric().compareTo(BigDecimal.ZERO) > 0
                        ? commodity.getActualWeightMetric() : commodity.getBookedWeightMetric();
            }
            if (CommonUtils.isNotEmpty(Spot_rate_cft)) {
                lclQuote.setSpotWmRate(new BigDecimal(Spot_rate_cft));
                rate_uom = "M";
                Double calculated_cft = 0.00;
                if (isOnlyOcnfrt) {
                    calculated_cft = null != TT_rev_charge ? Double.parseDouble(Spot_rate_cft)
                            - TT_rev_charge.getRatePerVolumeUnit().doubleValue() : Double.parseDouble(Spot_rate_cft);
                } else {
                    calculated_cft = Double.parseDouble(Spot_rate_cft) - CFT;
                }
                calculated_cft = calculated_cft < 0 ? 0.00 : calculated_cft;
                ocean_Freight_Rate.setRatePerVolumeUnit(new BigDecimal(calculated_cft));
                total_measure = new BigDecimal(calculated_cft).multiply(comm_measure);
            }
            if (CommonUtils.isNotEmpty(spot_Rate_cbm)) {
                lclQuote.setSpotRateMeasure(new BigDecimal(spot_Rate_cbm));
                rate_uom = "W";
                Double calculated_cbm = 0.00;
                if (isOnlyOcnfrt) {
                    calculated_cbm = null != TT_rev_charge ? Double.parseDouble(spot_Rate_cbm)
                            - TT_rev_charge.getRatePerWeightUnit().doubleValue() : Double.parseDouble(spot_Rate_cbm);
                } else {
                    calculated_cbm = Double.parseDouble(spot_Rate_cbm) - CBM;
                }
                calculated_cbm = calculated_cbm < 0 ? 0.00 : calculated_cbm;
                total_weight = new BigDecimal(calculated_cbm).multiply(comm_weight);
                total_weight = new BigDecimal(calculated_cbm).divide(new BigDecimal(fdEngmet.equalsIgnoreCase("E") ? 100 : 1000));
                ocean_Freight_Rate.setRatePerWeightUnit(new BigDecimal(calculated_cbm));
            }
            if (total_measure.doubleValue() > total_weight.doubleValue()
                    && total_measure.doubleValue() > ocean_Freight_Rate.getRateFlatMinimum().doubleValue()) {
                ocean_Freight_Rate.setArAmount(total_measure.setScale(2, RoundingMode.HALF_DOWN));
                ocean_Freight_Rate.setRatePerUnitUom("FRV");
            } else if (total_weight.doubleValue() > total_measure.doubleValue()
                    && total_weight.doubleValue() > ocean_Freight_Rate.getRateFlatMinimum().doubleValue()) {
                ocean_Freight_Rate.setArAmount(total_weight.setScale(2, RoundingMode.HALF_DOWN));
                ocean_Freight_Rate.setRatePerUnitUom("FRW");
            } else {
                ocean_Freight_Rate.setArAmount(ocean_Freight_Rate.getRateFlatMinimum().setScale(2, RoundingMode.HALF_DOWN));
                ocean_Freight_Rate.setRatePerUnitUom("FRM");
            }
            ocean_Freight_Rate.setBundleIntoOf(false);
            ocean_Freight_Rate.setPrintOnBl(true);
            lclQuoteAcDAO.update(ocean_Freight_Rate);
            lclQuoteAcDAO.getSession().clear();
            lclQuote.setSpotRate(true);
            lclQuote.setSpotRateUom("".equalsIgnoreCase(rate_uom) ? null : rate_uom);
            lclQuote.setSpotComment(spotComment);
            lclQuote.setSpotRateBottom(spotCheckBottom);
            lclQuote.setSpotOfRate(isOnlyOcnfrt);
            lclQuote.setSpotWmRate(CommonUtils.isNotEmpty(Spot_rate_cft) ? new BigDecimal(Spot_rate_cft) : new BigDecimal(0.00));
            lclQuote.setSpotRateMeasure(CommonUtils.isNotEmpty(spot_Rate_cbm) ? new BigDecimal(spot_Rate_cbm) : new BigDecimal(0.00));
            new LCLQuoteDAO().saveOrUpdate(lclQuote);
            if ("Y".equalsIgnoreCase(request.getParameter("updateTariff"))) {
                commodity.setCommodityType(new commodityTypeDAO().getByProperty("code", spotRateCommodity));
                new LclQuotePieceDAO().update(commodity);
            }
        }
    }
}
