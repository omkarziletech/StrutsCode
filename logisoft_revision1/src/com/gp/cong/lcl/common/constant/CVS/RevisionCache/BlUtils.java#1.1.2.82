/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.lcl.common.constant;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import static com.gp.cong.lcl.common.constant.LclCommonConstant.REMARKS_BL_AUTO_NOTES;
import com.gp.cong.lcl.dwr.LclDwr;
import com.gp.cong.lcl.dwr.LclSession;
import com.gp.cong.logisoft.beans.LclImportsRatesBean;
import com.gp.cong.logisoft.domain.Ports;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclBookingPad;
import com.gp.cong.logisoft.domain.lcl.LclContact;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.LclOptions;
import com.gp.cong.logisoft.domain.lcl.LclRemarks;
import com.gp.cong.logisoft.domain.lcl.bl.LclBl;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlAc;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlPiece;
import com.gp.cong.logisoft.domain.lcl.bl.LclCorrectionCharge;
import com.gp.cong.logisoft.hibernate.dao.CustomerAccountingDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLContactDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLImportRatesDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.Lcl3pRefNoDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBlAcDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBlHazmatDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPadDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclConsolidateDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclOptionsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.RoleDutyDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLBlDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLCorrectionChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLCorrectionDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LclBLPieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.commodityTypeDAO;
import com.gp.cong.logisoft.lcl.model.LCLExportChargeBean;
import com.gp.cvst.logisoft.domain.GlMapping;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.gp.cvst.logisoft.hibernate.dao.TransactionDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LCLBlForm;
import com.logiware.hibernate.dao.ArRedInvoiceDAO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.hibernate.SQLQuery;

/**
 *
 * @author Mei
 */
public class BlUtils implements ConstantsInterface, LclCommonConstant {

    private LclUtils lclUtils = new LclUtils();

    public void setWeightMeasureDetails(LclBl lclBl, LCLBlForm lclBlForm, HttpServletRequest request) throws Exception {
        String engmet = "";
        String rateType = "R".equalsIgnoreCase(lclBl.getRateType()) ? "Y" : lclBl.getRateType();
        RefTerminalDAO refterminaldao = new RefTerminalDAO();
        RefTerminal refterminal = refterminaldao.getTerminalByUnLocation(lclBl.getPortOfOrigin().getUnLocationCode(), rateType);
        String ofratebasis = new String();
        if (refterminal != null && refterminal.getTrmnum() != null && !refterminal.getTrmnum().trim().equals("")) {
            ofratebasis = refterminal.getTrmnum() + "-";
        }
        if (CommonUtils.isNotEmpty(lclBl.getRatesFromTerminalNo())) {
            RefTerminal ratesFromTerminal = refterminaldao.getTerminal(lclBl.getRatesFromTerminalNo());
            lclBlForm.setRatesFromTerminal(ratesFromTerminal.getTerminalLocation() + "/" + ratesFromTerminal.getTrmnum());
            lclBlForm.setRatesFromTerminalNo(lclBl.getRatesFromTerminalNo());
        }
        PortsDAO portsDAO = new PortsDAO();
        Ports ports = null;
        if (lclBl.getFinalDestination() != null) {
            ports = portsDAO.getByProperty("unLocationCode", lclBl.getFinalDestination().getUnLocationCode());
        } else {
            ports = portsDAO.getByProperty("unLocationCode", lclBl.getPortOfDestination().getUnLocationCode());
        }
        if (ports != null && ports.getEciportcode() != null && !ports.getEciportcode().trim().equals("")) {
            engmet = ports.getEngmet();
            ofratebasis += ports.getEciportcode() + "-";
        }
        List<LclBlPiece> lclCommodityList = new LclBLPieceDAO().findByProperty("lclFileNumber.id", lclBl.getFileNumberId());
        request.setAttribute("lclCommodityList", lclCommodityList);
        if (lclCommodityList != null && lclCommodityList.size() > 0) {
            LclBlPiece lclBlPiece = lclCommodityList.get(0);
            ofratebasis += lclBlPiece.getCommodityType().getCode();
            if (lclBlPiece.getStdchgRateBasis() != null && !lclBlPiece.getStdchgRateBasis().trim().equals("")) {
                request.setAttribute("stdchgratebasis", lclBlPiece.getStdchgRateBasis());
            }
            request.setAttribute("ofratebasis", ofratebasis);
        }

        LclBlAcDAO lclBlAcDAO = new LclBlAcDAO();
        List<LclBlAc> chargeList = lclBlAcDAO.getLclCostByFileNumberAsc(lclBl.getFileNumberId());
        if (CommonUtils.isNotEmpty(lclCommodityList)) {
            setWeighMeasureForBl(request, lclCommodityList, engmet);
            setRolledUpChargesForBl(chargeList, request, lclBl.getFileNumberId(), lclBlAcDAO, lclCommodityList, lclBl.getBillingType(), engmet, lclBl);
        }
        setCorrectedChargeList(lclBl, request);
    }

    public void setWeighMeasureForBl(HttpServletRequest request, List<LclBlPiece> lclBlPiecesList, String engmet) {
        BigDecimal weightValues = new BigDecimal(0.00);
        BigDecimal volumeValues = new BigDecimal(0.00);
        if (CommonUtils.isNotEmpty(lclBlPiecesList)) {
            for (LclBlPiece lclBlPiece : lclBlPiecesList) {
                if (engmet != null && !"".equalsIgnoreCase(engmet)) {
                    if ("E".equalsIgnoreCase(engmet)) {
                        if (lclBlPiece.getActualWeightImperial() != null
                                && lclBlPiece.getActualWeightImperial().doubleValue() != 0.00) {
                            weightValues = lclBlPiece.getActualWeightImperial().add(weightValues);
                            request.setAttribute("actualWeight", weightValues + " LBS");
                        }
                        if (lclBlPiece.getActualVolumeImperial() != null && lclBlPiece.getActualVolumeImperial().doubleValue() != 0.00) {
                            volumeValues = lclBlPiece.getActualVolumeImperial().add(volumeValues);
                            request.setAttribute("actualVolume", volumeValues + " CFT");
                        }
                    } else if ("M".equalsIgnoreCase(engmet)) {
                        if (lclBlPiece.getActualWeightMetric() != null && lclBlPiece.getActualWeightMetric().doubleValue() != 0.00) {
                            weightValues = lclBlPiece.getActualWeightMetric().add(weightValues);
                            request.setAttribute("actualWeight", weightValues + " KGS");
                        }
                        if (lclBlPiece.getActualVolumeMetric() != null && lclBlPiece.getActualVolumeMetric().doubleValue() != 0.00) {
                            volumeValues = lclBlPiece.getActualVolumeMetric().add(volumeValues);
                            request.setAttribute("actualVolume", volumeValues + " CBM");
                        }
                    }
                }
                if (lclBlPiece.getCommodityType() != null && lclBlPiece.getCommodityType().getCode() != null) {
                    request.setAttribute("commodityNumber", lclBlPiece.getCommodityType().getCode());
                }
            }
        }
    }

    public void setRolledUpChargesForBl(List<LclBlAc> chargeList, HttpServletRequest request,
            Long fileId, LclBlAcDAO lclblAcDAO, List<LclBlPiece> lclBlPiecesList,
            String billingType, String engmet, LclBl lclBl) throws Exception {
        List consolidatedfilenumber = new LclConsolidateDAO().getConsolidatesFiles(fileId);
        consolidatedfilenumber.add(fileId);
        if (chargeList == null || chargeList.isEmpty()) {
            request.setAttribute("rateErrorMessage", "No Rates Found.");
        } else if (CommonUtils.isNotEmpty(lclBlPiecesList)) {
            if (lclBlPiecesList.size() == 1) {
                List<LclBlAc> formattedChargesList = getFormattedLabelChargesForBl(lclBlPiecesList, chargeList, engmet, consolidatedfilenumber, false);
                request.setAttribute("chargeList", formattedChargesList);
                request.setAttribute("totalCharges", lclblAcDAO.getTotalChargesAmtByBl(fileId));
            } else {
                List rolledUpChargesList = getRolledUpChargesForBl(lclBlPiecesList, chargeList, engmet, consolidatedfilenumber, false);
                request.setAttribute("chargeList", rolledUpChargesList);
                request.setAttribute("totalCharges", calculateTotalByBlAcList(rolledUpChargesList));
            }
        }
        this.setArTotalAmount(lclBl, request);
    }

    public void setArTotalAmount(LclBl lclBl, HttpServletRequest request) throws Exception {
        TransactionDAO transactionDAO = new TransactionDAO();
        StringBuilder blNumber = new StringBuilder();
        StringBuilder blNumberII = new StringBuilder();
        if (lclBl.getPortOfOrigin() != null) {
            blNumber.append(lclBl.getPortOfOrigin().getUnLocationCode().substring(2, 5)).append("-");
            blNumberII.append(lclBl.getPortOfOrigin().getUnLocationCode().substring(2, 5)).append("-");
            if (lclBl.getFinalDestination() != null) {
                blNumber.append(lclBl.getFinalDestination().getUnLocationCode()).append("-");
                blNumberII.append(lclBl.getFinalDestination().getUnLocationCode().substring(2)).append("-");
            } else {
                blNumber.append(lclBl.getPortOfDestination().getUnLocationCode()).append("-");
                blNumberII.append(lclBl.getPortOfDestination().getUnLocationCode().substring(2)).append("-");
            }
            blNumber.append(lclBl.getLclFileNumber().getFileNumber());
            blNumberII.append(lclBl.getLclFileNumber().getFileNumber());
        }
        String totalArBalanceAmount = transactionDAO
                .getLclARBalanceExports(lclBl.getLclFileNumber().getFileNumber(), blNumber.toString(), blNumberII.toString());
        request.setAttribute("totalArBalanceAmount", totalArBalanceAmount);
        LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
        List consolidateFileNoList = new LclConsolidateDAO().getConsolidatesFiles(lclBl.getFileNumberId());
        if (consolidateFileNoList.isEmpty()) {
            Double totalCost = Double.parseDouble(lclCostChargeDAO.getTotalLclCostAmountByFileNumber(lclBl.getFileNumberId()));
            request.setAttribute("totalCost", NumberUtils.convertToTwoDecimal(totalCost));
        } else {
            consolidateFileNoList.add(lclBl.getFileNumberId());
            request.setAttribute("totalCost", lclCostChargeDAO.getTotalLclCostAmountByFileList(consolidateFileNoList));
        }
    }

    public void setCorrectedChargeList(LclBl lclBl, HttpServletRequest request) throws Exception {
        String corrected = "N";
        LCLCorrectionDAO lclCorrectionDAO = new LCLCorrectionDAO();
        Object correctionId = lclCorrectionDAO.getLastApprovedFieldsByFileId(lclBl.getLclFileNumber().getId(), "id");
        if (correctionId != null) {
            Long correctedId = Long.parseLong(correctionId.toString());
            List<LclCorrectionCharge> correctedList = new LCLCorrectionChargeDAO().getLclCorrectionChargesList(correctedId);
            List<LCLExportChargeBean> correctedChargeList = new ArrayList<LCLExportChargeBean>();
            BigDecimal totalCharge = BigDecimal.ZERO;
        //    Collections.reverse(correctedList);
            for (LclCorrectionCharge charge : correctedList) {
                LCLExportChargeBean chargeBean = new LCLExportChargeBean();
                chargeBean.setLclBl(lclBl);
                chargeBean.setFileNumber(lclBl.getLclFileNumber().getFileNumber());
                chargeBean.setFileNumberId(lclBl.getLclFileNumber().getId());
                chargeBean.setId(charge.getId());
                chargeBean.setManualEntry(true);
                chargeBean.setModifiedBy(charge.getModifiedBy());
                chargeBean.setModifiedDatetime(charge.getModifiedDatetime());
                totalCharge = totalCharge.add(charge.getNewAmount());
                setRateColumnValueForCorrectedCharge(lclBl, chargeBean, charge);
                chargeBean.setPrintOnBl(charge.isPrintOnBl());
                chargeBean.setArglMapping(charge.getGlMapping());
                chargeBean.setArBillToParty(charge.getBillToParty());
                chargeBean.setRolledupCharges(charge.getNewAmount());
                correctedChargeList.add(chargeBean);
                request.setAttribute("chargeList", correctedChargeList);
                request.setAttribute("totalCharges", totalCharge);
            }
            this.setArTotalAmount(lclBl, request);
            String totalArBalanceAmount = (String) request.getAttribute("totalArBalanceAmount");
            totalArBalanceAmount = totalArBalanceAmount.replace(",", "");
            Double totalArAmount = Double.parseDouble(totalArBalanceAmount);
            request.setAttribute("totalArAmount", totalArAmount);
            corrected = "Y";
        }
        request.setAttribute("corrected", corrected);
    }

    public void setRateColumnValueForCorrectedCharge(LclBl lclBl, LCLExportChargeBean chargeBean, LclCorrectionCharge charge) throws Exception {
        String eciPortCode = null != lclBl.getFinalDestination()
                ? lclBl.getFinalDestination().getUnLocationCode() : lclBl.getPortOfDestination().getUnLocationCode();
        Ports port = new PortsDAO().getPorts(eciPortCode);
        String engMet = null != port ? port.getEngmet() : "E";

        if (charge.getRatePerUnitUom().equalsIgnoreCase("W")) {
            chargeBean.setLabel1("*** TO WEIGHT ***");
        }
        if (charge.getRatePerUnitUom().equalsIgnoreCase("V")) {
            chargeBean.setLabel1("*** VOLUME ***");
        }
        if (charge.getRatePerUnitUom().equalsIgnoreCase("M")) {
            chargeBean.setLabel1("*** MINIMUM ***");
        }
        if (charge.getRatePerUnitUom().equalsIgnoreCase("PCT")) {
            if (charge.getNewAmount() != null) {
                int ratePercentage = (int) (charge.getNewAmount().doubleValue() * 100);
                chargeBean.setLabel2(String.valueOf(ratePercentage) + " PERCENT OF THE B/L.");
            }
        }
        if (charge.getRatePerUnitUom().equalsIgnoreCase("FRW")) {
            chargeBean.setLabel1("* O/F - TO WEIGHT *");
        }
        if (charge.getRatePerUnitUom().equalsIgnoreCase("FRV")) {
            chargeBean.setLabel1("* O/F - VOLUME *");
        }
        if (charge.getRatePerUnitUom().equalsIgnoreCase("FRM")) {
            chargeBean.setLabel1("* O/F - MINIMUM *");
        }
        boolean isOcnfrt = false;
        if (CommonUtils.in(charge.getRatePerUnitUom(), "W", "V", "M", "FRW", "FRV", "FRM")) {
            List<LclBlPiece> lclBlPieceList = new LclBLPieceDAO().findByProperty("lclFileNumber.id", lclBl.getFileNumberId());
            if (lclBlPieceList.size() > 1
                    && charge.getGlMapping().getBlueScreenChargeCode().equalsIgnoreCase("0001")) {
                chargeBean.setLabel2("MULTIPLE");
                isOcnfrt = true;
            }
            if (engMet != null) {
                if (engMet.equalsIgnoreCase("E") && !isOcnfrt) {
                    chargeBean.setLabel2("$" + charge.getRatePerVolumeUnit() + " CFT, " + charge.getRatePerWeightUnit()
                            + "/" + charge.getRatePerWeightUnitDiv() + " LBS, ($" + charge.getMinimumRate() + " MINIMUM)");
                } else if (engMet.equalsIgnoreCase("M") && !isOcnfrt) {
                    chargeBean.setLabel2("$" + charge.getRatePerVolumeUnit() + " CBM, " + charge.getRatePerWeightUnit()
                            + "/" + charge.getRatePerWeightUnitDiv() + " KGS, ($" + charge.getMinimumRate() + " MINIMUM)");
                }
            }
        } else if (charge.getRatePerUnitUom().equalsIgnoreCase("FL")) {
            chargeBean.setLabel2("$" + charge.getNewAmount().toString() + " FLAT RATE.");
            if (charge.getGlMapping().getChargeCode() != null && (charge.getGlMapping().getChargeCode().equals("OFBARR")
                    || charge.getGlMapping().getChargeCode().equals("TTBARR"))) {
                String ratePerWeightUnit = charge.getRatePerWeightUnit() != null ? charge.getRatePerWeightUnit().toString() : "";
                chargeBean.setLabel2("$" + ratePerWeightUnit + " PER BARREL.");
            }
        }
    }

    public List getFormattedLabelChargesForBl(List<LclBlPiece> lclBlPiecesList, List<LclBlAc> lclBlAcList,
            String engmet, List consolidatedList, boolean isAddAdjustment) throws Exception {
        for (int i = 0; i < lclBlAcList.size(); i++) {
            LclBlAc lclBlAc = lclBlAcList.get(i);
            if (isAddAdjustment) {
                double rolledUpAmt = lclBlAc.getArAmount().doubleValue() + lclBlAc.getAdjustmentAmount().doubleValue();
                lclBlAc.setRolledupCharges(new BigDecimal(NumberUtils.convertToTwoDecimal(rolledUpAmt)));
            } else {
                lclBlAc.setRolledupCharges(lclBlAc.getArAmount());
            }
            formatLabelChargeForBl(lclBlPiecesList, lclBlAc, engmet);
            if (CommonUtils.isNotEmpty(consolidatedList)) {
                consolidatedList.add(lclBlAc.getLclFileNumber().getId());
                consolidateChargesWithDrs(lclBlAc, consolidatedList);
            }
        }
        return lclBlAcList;
    }

    public void formatLabelChargeForBl(List<LclBlPiece> lclBlPiecesList, LclBlAc lclBlAc, String engmet) {
        if (lclBlAc.getRatePerUnitUom() != null && !lclBlAc.getRatePerUnitUom().trim().equals("")) {
            if (lclBlAc.getRatePerUnitUom().equalsIgnoreCase("FL")) {
                if (lclBlAc.getArglMapping() != null && lclBlAc.getArglMapping().getBlueScreenChargeCode() != null) {
                    if (lclBlAc.getArglMapping().getBlueScreenChargeCode().equals("0006")) {
                        String ratePerUnit = lclBlAc.getRatePerWeightUnit() != null ? lclBlAc.getRatePerWeightUnit().toString() : "";
                        String ratePeUnitDiv = lclBlAc.getRatePerWeightUnitDiv() != null ? lclBlAc.getRatePerWeightUnitDiv().toString() : "";
                        lclBlAc.setLabel2("(" + ratePerUnit + " PER " + ratePeUnitDiv + " CIF)");
                    } else {
                        lclBlAc.setLabel2("$" + lclBlAc.getArAmount().toString() + " FLAT RATE.");
                    }
                }
                if (lclBlAc.getArglMapping().getChargeCode() != null && (lclBlAc.getArglMapping().getChargeCode().equals("OFBARR")
                        || lclBlAc.getArglMapping().getChargeCode().equals("TTBARR"))) {
                    String ratePerWeightUnit = lclBlAc.getRatePerWeightUnit() != null ? lclBlAc.getRatePerWeightUnit().toString() : "";
                    lclBlAc.setLabel2("$" + ratePerWeightUnit + " PER BARREL.");
                }

            }
            if (lclBlAc.getRatePerUnitUom().equalsIgnoreCase("W")) {
                lclBlAc.setLabel1("*** TO WEIGHT ***");
            }
            if (lclBlAc.getRatePerUnitUom().equalsIgnoreCase("V")) {
                lclBlAc.setLabel1("*** VOLUME ***");
            }
            if (lclBlAc.getRatePerUnitUom().equalsIgnoreCase("M")) {
                lclBlAc.setLabel1("*** MINIMUM ***");
            }
            if (lclBlAc.getRatePerUnitUom().equalsIgnoreCase("PCT")) {
                if (lclBlAc.getRatePerUnit() != null) {
                    int ratePercentage = (int) (lclBlAc.getRatePerUnit().doubleValue() * 100);
                    lclBlAc.setLabel2(String.valueOf(ratePercentage) + " PERCENT OF THE B/L.");
                }
            }
            if (lclBlAc.getRatePerUnitUom().equalsIgnoreCase("FRW")) {
                lclBlAc.setLabel1("* O/F - TO WEIGHT *");
            }
            if (lclBlAc.getRatePerUnitUom().equalsIgnoreCase("FRV")) {
                lclBlAc.setLabel1("* O/F - VOLUME *");
            }
            if (lclBlAc.getRatePerUnitUom().equalsIgnoreCase("FRM")) {
                lclBlAc.setLabel1("* O/F - MINIMUM *");
            }
            if (lclBlAc.getRatePerUnitUom().equalsIgnoreCase("W") || lclBlAc.getRatePerUnitUom().equalsIgnoreCase("V")
                    || lclBlAc.getRatePerUnitUom().equalsIgnoreCase("M") || lclBlAc.getRatePerUnitUom().equalsIgnoreCase("FRW")
                    || lclBlAc.getRatePerUnitUom().equalsIgnoreCase("FRV") || lclBlAc.getRatePerUnitUom().equalsIgnoreCase("FRM")) {
                if (engmet != null) {
                    if (engmet.equalsIgnoreCase("E")) {
                        if (lclBlPiecesList.size() > 1 && lclBlAc.getArglMapping().getBlueScreenChargeCode().equalsIgnoreCase("0001")) {
                            lclBlAc.setLabel2("MULTIPLE");
                        } else {
                            if (lclBlAc.isManualEntry() && lclBlAc.getRateUom().equalsIgnoreCase("M")) {
                                lclBlAc.setLabel2("$" + lclUtils.convertToCft(lclBlAc.getRatePerVolumeUnit().doubleValue()) + " CFT, "
                                        + lclUtils.convertToLbs(lclBlAc.getRatePerWeightUnit().doubleValue()) + "/"
                                        + lclBlAc.getRatePerWeightUnitDiv() + " LBS, ($" + lclBlAc.getRateFlatMinimum()
                                        + " MINIMUM)");
                            } else {
                                lclBlAc.setLabel2("$" + lclBlAc.getRatePerVolumeUnit() + " CFT, " + lclBlAc.getRatePerWeightUnit()
                                        + "/" + lclBlAc.getRatePerWeightUnitDiv() + " LBS, ($" + lclBlAc.getRateFlatMinimum() + " MINIMUM)");
                            }
                        }
                    } else if (engmet.equalsIgnoreCase("M")) {
                        if (lclBlPiecesList.size() > 1 && lclBlAc.getArglMapping().getBlueScreenChargeCode().equalsIgnoreCase("0001")) {
                            lclBlAc.setLabel2("MULTIPLE");
                        } else {
                            if (lclBlAc.isManualEntry() && lclBlAc.getRateUom().equalsIgnoreCase("I")) {
                                lclBlAc.setLabel2("$" + lclUtils.convertToCbm(lclBlAc.getRatePerVolumeUnit().doubleValue()) + " CBM, "
                                        + lclUtils.convertToKgs(lclBlAc.getRatePerWeightUnit().doubleValue()) + "/"
                                        + lclBlAc.getRatePerWeightUnitDiv() + " KGS, ($" + lclBlAc.getRateFlatMinimum() + " MINIMUM)");
                            } else {
                                lclBlAc.setLabel2("$" + lclBlAc.getRatePerVolumeUnit() + " CBM, " + lclBlAc.getRatePerWeightUnit()
                                        + "/" + lclBlAc.getRatePerWeightUnitDiv() + " KGS, ($" + lclBlAc.getRateFlatMinimum() + " MINIMUM)");
                            }
                        }
                    }
                }

            }

        }
    }

    public List getRolledUpChargesForBl(List<LclBlPiece> lclBlPiecesList, List<LclBlAc> lclBlAcList, String engmet, List consolidatedList,
            boolean isAddAdjustment) throws Exception {
        Map chargesInfoMap = new LinkedHashMap();
        Double minchg = 0.0;
        Double calculatedWeight = 0.0;
        Double calculatedMeasure = 0.0;
        for (int i = 0; i < lclBlAcList.size(); i++) {
            LclBlAc lclBlAc = lclBlAcList.get(i);
            if (!chargesInfoMap.containsKey(lclBlAc.getArglMapping().getChargeCode())) {
                formatLabelChargeForBl(lclBlPiecesList, lclBlAc, engmet);
                if (isAddAdjustment) {
                    double rolledUpAmt = lclBlAc.getArAmount().doubleValue() + lclBlAc.getAdjustmentAmount().doubleValue();
                    lclBlAc.setRolledupCharges(new BigDecimal(NumberUtils.convertToTwoDecimal(rolledUpAmt)));
                } else {
                    lclBlAc.setRolledupCharges(lclBlAc.getArAmount());
                }
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
                            }
                        } else if (lclBlAc.getRateUom().equalsIgnoreCase("M")) {
                            if (lclBlAc.getLclBlPiece() != null) {
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
                        if (isAddAdjustment) {
                            double rolledUpAmt = lclBookingAcFromMap.getRolledupCharges().doubleValue() + lclBookingAcFromMap.getAdjustmentAmount().doubleValue();
                            lclBookingAcFromMap.setRolledupCharges(new BigDecimal(NumberUtils.convertToTwoDecimal(rolledUpAmt)));
                        }
                    }
                }
                formatLabelChargeForBl(lclBlPiecesList, lclBookingAcFromMap, engmet);
                chargesInfoMap.put(lclBlAc.getArglMapping().getChargeCode(), lclBookingAcFromMap);
            }
            if (CommonUtils.isNotEmpty(consolidatedList)) {
                consolidateChargesWithDrs(lclBlAc, consolidatedList);
            }
        }
        List rolledChargesList = new ArrayList(chargesInfoMap.values());
        return rolledChargesList;
    }

    public String calculateTotalByBlAcList(List<LclBlAc> lclBlAclist) {
        Double total = 0.0;
        if (lclBlAclist != null && lclBlAclist.size() > 0) {
            for (int i = 0; i < lclBlAclist.size(); i++) {
                LclBlAc lclBlAc = lclBlAclist.get(i);
                total = total + lclBlAc.getRolledupCharges().doubleValue() + lclBlAc.getAdjustmentAmount().doubleValue();
            }
        }
        return NumberUtils.convertToTwoDecimal(total);
    }

    public Double calculateTotalWithoutInsuranceByBlAcList(List<LclBlAc> lclBlAclist) {
        Double total = 0.0;
        if (lclBlAclist != null && lclBlAclist.size() > 0) {
            for (int i = 0; i < lclBlAclist.size(); i++) {
                LclBlAc lclBlAc = lclBlAclist.get(i);
                if (lclBlAc.getArglMapping() != null && lclBlAc.getArglMapping().getBlueScreenChargeCode() != null
                        && !lclBlAc.getArglMapping().getBlueScreenChargeCode().equals("0006")) {
                    total = total + lclBlAc.getRolledupCharges().doubleValue();
                }
            }
        }
        return total;
    }

    public String getBLConcatenatedFinalDestination(LclBl lclBl) {
        if (lclBl.getFinalDestination() != null) {
            StringBuilder builder = new StringBuilder();
            if (CommonUtils.isNotEmpty(lclBl.getFinalDestination().getUnLocationName())
                    && CommonUtils.isNotEmpty(lclBl.getFinalDestination().getCountryId().getCodedesc()) && CommonUtils.isEmpty(lclBl.getFinalDestination().getUnLocationCode())) {
                builder.append(lclBl.getFinalDestination().getUnLocationName() + ", " + lclBl.getFinalDestination().getCountryId().getCodedesc());
            }
            if (CommonUtils.isNotEmpty(lclBl.getFinalDestination().getUnLocationName()) && CommonUtils.isNotEmpty(lclBl.getFinalDestination().getUnLocationCode()) && CommonUtils.isNotEmpty(lclBl.getFinalDestination().getCountryId().getCodedesc())) {
                builder.append(lclBl.getFinalDestination().getUnLocationName() + ", " + lclBl.getFinalDestination().getCountryId().getCodedesc());
            } else if (CommonUtils.isNotEmpty(lclBl.getFinalDestination().getUnLocationCode()) && CommonUtils.isEmpty(lclBl.getFinalDestination().getCountryId().getCodedesc()) && CommonUtils.isNotEmpty(lclBl.getFinalDestination().getUnLocationCode())) {
                builder.append(lclBl.getFinalDestination().getUnLocationName());
            } else if (CommonUtils.isNotEmpty(lclBl.getFinalDestination().getUnLocationCode()) && CommonUtils.isEmpty(lclBl.getFinalDestination().getCountryId().getCodedesc()) && CommonUtils.isEmpty(lclBl.getFinalDestination().getUnLocationCode())) {
                builder.append(lclBl.getFinalDestination().getUnLocationName());
            }
            return builder.toString();
        } else {
            return "";
        }
    }

    public void setCreditStauAndLimit(LclBl lclBl, HttpServletRequest request, User loginUser) throws Exception {
        /* AES */
        List consolidateList = new LclConsolidateDAO().getConsolidatesFiles(lclBl.getLclFileNumber().getId());
        consolidateList.add(lclBl.getLclFileNumber().getId());

        LclBookingPad lclBookingPad = new LclBookingPadDAO().getLclBookingPadByFileNumber(lclBl.getFileNumberId());
        if (null != lclBookingPad && CommonUtils.isNotEmpty(lclBookingPad.getPickUpCity())) {
            //  lclBlForm.setDoorOriginCityZip(lclBookingPad.getPickUpCity());
            String[] pickup = lclBookingPad.getPickUpCity().split("-");
            request.setAttribute("pickUpCity", pickup.length > 1 ? pickup[1] : "");
        } else {
            request.setAttribute("pickUpCity", lclUtils.getConcatenatedOriginByUnlocation(lclBl.getPortOfOrigin()));
        }
        request.setAttribute("aesList", new Lcl3pRefNoDAO().get3pRefAesList(consolidateList));
        /* Hazmat */
        request.setAttribute("hazmatFlag", new LclBlHazmatDAO().isCheckedHazmat(consolidateList));

        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        CustomerAccountingDAO custAcctDAO = new CustomerAccountingDAO();
        if (lclBl.getShipAcct() != null && lclBl.getShipAcct().getAccountno() != null && !"".equalsIgnoreCase(lclBl.getShipAcct().getAccountno())) {
            request.setAttribute("shipCreditStatus", genericCodeDAO.getCreditStatus(lclBl.getShipAcct().getAccountno()));
            request.setAttribute("shipCreditLimt", custAcctDAO.getCreditLimitForCustomer(lclBl.getShipAcct().getAccountno()));
        }
        if (lclBl.getConsAcct() != null && lclBl.getConsAcct().getAccountno() != null && !"".equalsIgnoreCase(lclBl.getConsAcct().getAccountno())) {
            request.setAttribute("consCreditStatus", genericCodeDAO.getCreditStatus(lclBl.getConsAcct().getAccountno()));
            request.setAttribute("consCreditLimt", custAcctDAO.getCreditLimitForCustomer(lclBl.getConsAcct().getAccountno()));
        }
        if (lclBl.getFwdAcct() != null && lclBl.getFwdAcct().getAccountno() != null && !"".equalsIgnoreCase(lclBl.getFwdAcct().getAccountno())) {
            request.setAttribute("fwdCreditStatus", genericCodeDAO.getCreditStatus(lclBl.getFwdAcct().getAccountno()));
            request.setAttribute("fwdCreditLimit", custAcctDAO.getCreditLimitForCustomer(lclBl.getFwdAcct().getAccountno()));
        }
        if (lclBl.getThirdPartyAcct() != null && lclBl.getThirdPartyAcct().getAccountno() != null && !"".equalsIgnoreCase(lclBl.getThirdPartyAcct().getAccountno())) {
            request.setAttribute("thirdCreditStatus", genericCodeDAO.getCreditStatus(lclBl.getThirdPartyAcct().getAccountno()));
            request.setAttribute("thirdCreditLimit", custAcctDAO.getCreditLimitForCustomer(lclBl.getThirdPartyAcct().getAccountno()));
        }
        if (lclBl.getNotyAcct() != null && lclBl.getNotyAcct().getAccountno() != null && !"".equalsIgnoreCase(lclBl.getNotyAcct().getAccountno())) {
            request.setAttribute("notyCreditStatus", genericCodeDAO.getCreditStatus(lclBl.getNotyAcct().getAccountno()));
            request.setAttribute("notyCreditLimit", custAcctDAO.getCreditLimitForCustomer(lclBl.getNotyAcct().getAccountno()));
        }
        String arInvoiceFlag = "Empty";
        String arCount[] = new ArRedInvoiceDAO().isLclARInvoice(lclBl.getLclFileNumber().getId().toString(), "LCLE DR");
        lclUtils.inbondDetails(lclBl.getLclFileNumber().getId(), request);//set Inbond Details
        if (!arCount[0].equalsIgnoreCase("0") || !arCount[1].equalsIgnoreCase("0")) {
            if (Integer.parseInt(arCount[0]) > Integer.parseInt(arCount[1])) {
                arInvoiceFlag = "Posted";
            } else {
                arInvoiceFlag = "Open";
            }
        }
        if (null != lclBl.getEnteredBy().getRole()) {
            request.setAttribute("roleDutyForUnPost", new LclDwr().checkUnPost(String.valueOf(lclBl.getEnteredBy().getRole().getRoleId())));
        }
        if (null != loginUser.getRole()) {
            request.setAttribute("editBlOwner", new RoleDutyDAO().getRoleDetails(EDIT_LCL_BL_OWNER, loginUser.getRole().getRoleId()));
            request.setAttribute("role", new RoleDutyDAO().getRoleDetails(loginUser.getRole().getRoleId()));
        }
        request.setAttribute("companyMnemoniceName", new SystemRulesDAO().getSystemRulesByCode("CompanyNameMnemonic").toUpperCase());
        request.setAttribute("arInvoiceFlag", arInvoiceFlag);
    }

    public void setPolAndPod(LclBl lclBl, HttpServletRequest request) {
        if (lclBl.getPortOfLoading() != null) {
            StringBuilder sb = new StringBuilder();
            if (CommonUtils.isNotEmpty(lclBl.getPortOfLoading().getUnLocationName()) && null != lclBl.getPortOfLoading().getStateId()
                    && CommonUtils.isNotEmpty(lclBl.getPortOfLoading().getStateId().getCode()) && CommonUtils.isNotEmpty(lclBl.getPortOfLoading().getUnLocationCode())) {
                sb.append(lclBl.getPortOfLoading().getUnLocationName()).append("/").append(lclBl.getPortOfLoading().getStateId().getCode()).append("(").append(lclBl.getPortOfLoading().getUnLocationCode()).append(")");
            } else if (CommonUtils.isNotEmpty(lclBl.getPortOfLoading().getUnLocationName()) && CommonUtils.isNotEmpty(lclBl.getPortOfLoading().getUnLocationCode())
                    && null != lclBl.getPortOfLoading().getCountryId() && CommonUtils.isNotEmpty(lclBl.getPortOfLoading().getCountryId().getCodedesc()) && CommonUtils.isNotEmpty(lclBl.getPortOfLoading().getUnLocationCode())) {
                sb.append(lclBl.getPortOfLoading().getUnLocationName()).append("/").append(lclBl.getPortOfLoading().getCountryId().getCodedesc()).append("(").append(lclBl.getPortOfLoading().getUnLocationCode()).append(")");
            } else if (CommonUtils.isNotEmpty(lclBl.getPortOfLoading().getUnLocationName()) && CommonUtils.isNotEmpty(lclBl.getPortOfLoading().getUnLocationCode())) {
                sb.append(lclBl.getPortOfLoading().getUnLocationName()).append("(").append(lclBl.getPortOfLoading().getUnLocationCode()).append(")");
            }
            request.setAttribute("pol", sb.toString());
            request.setAttribute("polUnlocationcode", lclBl.getPortOfLoading().getUnLocationCode());
        }
        if (lclBl.getPortOfDestination() != null) {
            StringBuilder sb = new StringBuilder();
            if (CommonUtils.isNotEmpty(lclBl.getPortOfDestination().getUnLocationName()) && null != lclBl.getPortOfDestination().getStateId()
                    && CommonUtils.isNotEmpty(lclBl.getPortOfDestination().getStateId().getCode()) && CommonUtils.isNotEmpty(lclBl.getPortOfDestination().getUnLocationCode())) {
                sb.append(lclBl.getPortOfDestination().getUnLocationName()).append("/").append(lclBl.getPortOfDestination().getStateId().getCode()).append("(").append(lclBl.getPortOfDestination().getUnLocationCode()).append(")");
            } else if (CommonUtils.isNotEmpty(lclBl.getPortOfDestination().getUnLocationName()) && CommonUtils.isNotEmpty(lclBl.getPortOfDestination().getUnLocationCode())
                    && null != lclBl.getPortOfDestination().getCountryId() && CommonUtils.isNotEmpty(lclBl.getPortOfDestination().getCountryId().getCodedesc()) && CommonUtils.isNotEmpty(lclBl.getPortOfDestination().getUnLocationCode())) {
                sb.append(lclBl.getPortOfDestination().getUnLocationName()).append("/").append(lclBl.getPortOfDestination().getCountryId().getCodedesc()).append("(").append(lclBl.getPortOfDestination().getUnLocationCode()).append(")");
            } else if (CommonUtils.isNotEmpty(lclBl.getPortOfDestination().getUnLocationName()) && CommonUtils.isNotEmpty(lclBl.getPortOfDestination().getUnLocationCode())) {
                sb.append(lclBl.getPortOfDestination().getUnLocationName()).append("(").append(lclBl.getPortOfDestination().getUnLocationCode()).append(")");
            }
            request.setAttribute("pod", sb.toString());
            request.setAttribute("podUnlocationcode", lclBl.getPortOfDestination().getUnLocationCode());
        }
    }

    public void setPrintOptionsList(Long fileId, HttpServletRequest request) throws Exception {
        LclOptionsDAO optionsDAO = new LclOptionsDAO();
        List<LclOptions> lclPrintOptionsList = optionsDAO.findByProperty("lclFileNumber.id", fileId);
        request.setAttribute("lclPrintOptionsList", lclPrintOptionsList);
        String altFreightAgent = optionsDAO.getOptionValue(fileId, BL_PRINT_OPTION_FPFEILD);
        if (CommonUtils.isNotEmpty(altFreightAgent)) {
            TradingPartner frieghtPickupValue = new TradingPartnerDAO().findById(altFreightAgent);
            if (frieghtPickupValue != null) {
                request.setAttribute("frieghtPickupValue", frieghtPickupValue.getAccountName());
                request.setAttribute("frieghtPickupNo", frieghtPickupValue.getAccountno());
            }
        }
        String altPortLower = optionsDAO.getOptionValue(fileId, BL_PRINT_OPTION_PORTFIELD);
        if (CommonUtils.isNotEmpty(altPortLower)) {
            request.setAttribute("altPortValue", altPortLower);
        }
    }

    public LCLBlForm getPickedVoyageDetailsForBL(LCLBlForm lclBlForm, String fileId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT  lu.`unit_no` AS unitNumber, lsh.`schedule_no` AS voyageNumber, ");
        sb.append(" DATE_FORMAT(lusm.`manifested_datetime`,'%Y-%m-%d %T') AS manifestedOn, ");
        sb.append(" u.`login_name` AS manifestedBy,  lus.`su_heading_note` AS sealNumber, lsd.`sp_reference_no` AS ssVoyage1,");
        sb.append(" DATE_FORMAT(lsd.std,'%d-%b-%Y') AS sailDate1,  lus.`cob` AS blUnitCob, lus.id as unitSsId,lsd.`sp_reference_name` as vessal, ");
        sb.append(" (SELECT t.acct_name FROM trading_partner t WHERE t.acct_no = lsd.`sp_acct_no`) AS spAcctName, ");
        sb.append("  UnLocationGetNameStateCntryByID(lsd.departure_id) AS pierName, ");
        sb.append("  lsh.closed_by_user_id as voyageClosedUser ");
        sb.append(" FROM ");
        sb.append(" (SELECT lf.`id` AS fileId, lbu.`lcl_unit_ss_id` AS unitSsId FROM lcl_booking_piece bp  ");
        sb.append(" JOIN lcl_booking_piece_unit lbu ON lbu.`booking_piece_id` = bp.`id`  ");
        sb.append(" JOIN lcl_file_number lf ON lf.`id` = bp.`file_number_id` WHERE lf.id =:fileId) fn  ");
        sb.append(" JOIN lcl_unit_ss lus ON lus.id = fn.unitSsId ");
        sb.append(" JOIN lcl_ss_header lsh ON lsh.`id` = lus.`ss_header_id` AND lsh.`service_type` IN ('E' ,'C')  ");
        sb.append(" JOIN lcl_ss_detail lsd ON lsd.`ss_header_id` = lsh.`id` AND lsd.`id` =  ");
        sb.append(" (select ls.id from lcl_ss_detail ls where ls.ss_header_id = lsh.id order by ls.id desc limit 1) ");
        sb.append(" JOIN  lcl_unit lu ON lu.`id` =  lus.`unit_id` ");
        sb.append(" LEFT JOIN `lcl_unit_ss_manifest` lusm  ON lusm.`unit_id` = lu.`id` AND  lusm.`ss_header_id` = lsh.`id` ");
        sb.append(" LEFT JOIN user_details u ON lusm.`manifested_by_user_id` = u.`user_id` ");
        sb.append(" ORDER BY lsh.id DESC LIMIT 1 ");
        SQLQuery query = new BaseHibernateDAO().getCurrentSession().createSQLQuery(sb.toString());
        query.setParameter("fileId", fileId);
        Object[] row = (Object[]) query.uniqueResult();
        if (row != null) {
            lclBlForm.setUnitNumber(null != row[0] ? row[0].toString() : "");
            lclBlForm.setVoyageNumber(null != row[1] ? row[1].toString() : "");
            lclBlForm.setManifestedOn(null != row[2] ? row[2].toString() : "");
            lclBlForm.setManifestedBy(null != row[3] ? row[3].toString() : "");
            lclBlForm.setSealNumber(null != row[4] ? row[4].toString() : "");
            lclBlForm.setSsVoyage1(null != row[5] ? row[5].toString() : "");
            lclBlForm.setSailDate1(null != row[6] ? row[6].toString() : "");
            lclBlForm.setBlUnitCob(null != row[7] ? Boolean.parseBoolean(row[7].toString()) : false);
            lclBlForm.setUnitSsId(null != row[8] ? Long.parseLong(row[8].toString()) : null);
            lclBlForm.setVesselName(null != row[9] ? row[9].toString() : "");
            lclBlForm.setSsLine(null != row[10] ? row[10].toString() : "");
            lclBlForm.setPier(null != row[11] ? row[11].toString() : "");
            lclBlForm.setVoyageClosedUser(null != row[12] ? row[12].toString() : "");
        }
        return lclBlForm;
    }

    public String consolidateChargesWithDrs(LclBlAc lclBlAc, List consolidatesList) throws Exception {
        String consolidateCharges = new LclBlAcDAO().getConsolidateChargesWithDrs(consolidatesList, lclBlAc.getArglMapping().getChargeCode());
        lclBlAc.setConsolidateCharges(consolidateCharges.replace(",", "<br/>"));
        return "";
    }

    public LclContact getBLContact(LclFileNumber fileNumber, LclContact contact, User user,
            String company_name, String contactType) throws Exception {
        LclContact oldcontact = new LCLContactDAO().getContact(fileNumber.getId(), contactType);
        LclContact newcontact = oldcontact == null ? new LclContact() : oldcontact;
        if (CommonUtils.isNotEmpty(company_name) && !contact.getCompanyName().equalsIgnoreCase(company_name)) {
            String remarks = "UPDATED ->(" + contactType.substring(2) + "--> " + contact.getCompanyName() + " to " + company_name + ")";
            new LclRemarksDAO().insertLclRemarks(fileNumber.getId(), REMARKS_BL_AUTO_NOTES, remarks, user.getUserId());
        }
        if (CommonUtils.isNotEmpty(company_name)) {
            newcontact.setCompanyName(company_name);
        } else if (CommonUtils.isNotEmpty(contact.getCompanyName())) {
            newcontact.setCompanyName(contact.getCompanyName());
        }
        newcontact.setAddress(contact.getAddress());
        if (newcontact.getValidLclContact().length() > 1) {
            newcontact.setLclFileNumber(fileNumber);
            newcontact.setRemarks(contactType);
            newcontact.setEnteredBy(user);
            newcontact.setModifiedBy(user);
            newcontact.setEnteredDatetime(new Date());
            newcontact.setModifiedDatetime(new Date());
            new LCLContactDAO().saveOrUpdate(newcontact);
        } else {
            return null;
        }
        return newcontact;
    }

    public void setSportRateValues(LclBl lclBl, String engmet, HttpServletRequest request) {
        request.setAttribute("ofspotrate", lclBl.getSpotRate());//set ofrate flag in commodity Tab display Ofrate
        if (null != engmet && !"".equalsIgnoreCase(engmet) && lclBl.getSpotRate()) {
            if (engmet.equalsIgnoreCase("E")) {
                request.setAttribute("spotratelabel", (lclBl.getSpotWmRate() == null ? 0 : lclBl.getSpotWmRate())
                        + "/CFT" + "," + (lclBl.getSpotRateMeasure() == null ? 0 : lclBl.getSpotRateMeasure()) + "/100 LBS");
            } else {
                request.setAttribute("spotratelabel", (lclBl.getSpotWmRate() == null ? 0 : lclBl.getSpotWmRate())
                        + "/CBM" + "," + (lclBl.getSpotRateMeasure() == null ? 0 : lclBl.getSpotRateMeasure()) + "/1000 KGS");
            }
        }
    }

    public void calculateSpotRate(Long file_number_id, LclBl lclBL, String billing_type, String spot_Rate_cbm,
            String Spot_rate_cft, String rateType, Boolean isOnlyOcnfrt, Boolean spotCheckBottom,
            String spotComment, String spotRateCommodity, HttpServletRequest request) throws Exception {
        String ofratebasis = "", engmet = "", rate_uom = "";
        Spot_rate_cft = "".equalsIgnoreCase(Spot_rate_cft) ? "0.00" : Spot_rate_cft;
        spot_Rate_cbm = "".equalsIgnoreCase(spot_Rate_cbm) ? "0.00" : spot_Rate_cbm;
        PortsDAO portsDAO = new PortsDAO();
        LclBlAcDAO lclBlACDAO = new LclBlAcDAO();
        RefTerminalDAO refterminaldao = new RefTerminalDAO();
        RefTerminal refterminal = refterminaldao.getTerminalByUnLocation(lclBL.getPortOfOrigin().getUnLocationCode(), rateType);
        if (refterminal != null && refterminal.getTrmnum() != null && !refterminal.getTrmnum().trim().equals("")) {
            ofratebasis = refterminal.getTrmnum() + "-";
        }
        String destinationCode = null != lclBL.getFinalDestination()
                ? lclBL.getFinalDestination().getUnLocationCode() : lclBL.getPortOfDestination().getUnLocationCode();
        Ports ports = portsDAO.getByProperty("unLocationCode", destinationCode);
        if (ports != null && ports.getEciportcode() != null && !ports.getEciportcode().trim().equals("")) {
            engmet = ports.getEngmet();
            ofratebasis += ports.getEciportcode() + "-";
        }
        Double CFT = 0.00, CBM = 0.00, OCN_CFT = 0.00, OCN_CBM = 0.00;
        LclBlAc ocean_Freight_Rate = lclBlACDAO.manualChargeValidate(file_number_id, "OCNFRT", false);
        List<Object[]> rates_list = lclBlACDAO.getBLSpotRateCharge(file_number_id);
        for (Object[] obj : rates_list) {
            CFT = Double.parseDouble(obj[0].toString());
            CBM = Double.parseDouble(obj[1].toString());
            OCN_CFT = Double.parseDouble(obj[2].toString());
            OCN_CBM = Double.parseDouble(obj[3].toString());
        }
        List<LclBlPiece> lclBLPiecesList = new LclBLPieceDAO().findByProperty("lclFileNumber.id", file_number_id);
        LclBlPiece commodity = lclBLPiecesList.isEmpty() ? new LclBlPiece() : lclBLPiecesList.get(0);
        LclBlAc TT_rev_charge = lclBlACDAO.getTTCharges(file_number_id, false);
        BigDecimal total_weight = BigDecimal.ZERO;
        BigDecimal total_measure = BigDecimal.ZERO;
        BigDecimal comm_measure = BigDecimal.ZERO;
        BigDecimal comm_weight = BigDecimal.ZERO;
        if (null != ocean_Freight_Rate) {
            if ("E".equalsIgnoreCase(engmet)) {
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
                lclBL.setSpotWmRate(new BigDecimal(Spot_rate_cft));
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
                lclBL.setSpotRateMeasure(new BigDecimal(spot_Rate_cbm));
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
                total_weight = new BigDecimal(calculated_cbm).divide(new BigDecimal(engmet.equalsIgnoreCase("E") ? 100 : 1000));
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
            lclBlACDAO.update(ocean_Freight_Rate);
            lclBlACDAO.getSession().clear();
        }
        lclBL.setSpotRateBottom(spotCheckBottom);
        lclBL.setSpotOfRate(isOnlyOcnfrt);
        lclBL.setSpotRateUom("".equalsIgnoreCase(rate_uom) ? null : rate_uom);
        lclBL.setSpotComment(spotComment);
        lclBL.setSpotRate(true);
        new LCLBlDAO().saveOrUpdate(lclBL);
        request.setAttribute("lclBl", lclBL);
        if ("Y".equalsIgnoreCase(request.getParameter("updateTariff"))) {
            commodity.setCommodityType(new commodityTypeDAO().getByProperty("code", spotRateCommodity));
            new LclBookingPieceDAO().update(commodity);
        }
        if (CommonUtils.isNotEmpty(lclBLPiecesList)) {
            List<LclBlAc> chargeList = lclBlACDAO.getLclCostByFileNumberAsc(file_number_id);
            setWeighMeasureForBl(request, lclBLPiecesList, engmet);
            setRolledUpChargesForBl(chargeList, request, file_number_id, lclBlACDAO, lclBLPiecesList,
                    billing_type, engmet, commodity.getLclFileNumber().getLclBl());
            ofratebasis += commodity.getCommodityType().getCode();
            request.setAttribute("ofratebasis", ofratebasis);
            request.setAttribute("isConsolidate", new LclConsolidateDAO().isConsolidatedByFileAB(file_number_id));
        }
    }

    public List<LclBlAc> getTransshipmentRaterForBl(LclBl lclBl, User user, HttpServletRequest request) throws Exception {
        List<LclBlAc> lclBlAcList = new ArrayList<LclBlAc>();
        String billingTerms[] = {lclBl.getBillingType()};
        List<String> billingTermsList = Arrays.asList(billingTerms);
        PortsDAO portsDAO = new PortsDAO();
        LclBooking booking = lclBl.getLclFileNumber().getLclBooking();
        List<LclBlPiece> commodity = lclBl.getLclFileNumber().getLclBlPieceList();
        List commodityList = new ArrayList<>();
        for (LclBlPiece blpiece : commodity) {
            commodityList.add(blpiece.getCommodityType().getCode());
        }
        String pooSchnum = portsDAO.getShedulenumber(booking.getPortOfOrigin().getUnLocationCode());
        String polSchnum = portsDAO.getShedulenumber(booking.getPortOfLoading().getUnLocationCode());
        String fdSchnum = portsDAO.getShedulenumber(booking.getFinalDestination().getUnLocationCode());
        new LclRemarksDAO().insertLclRemarks(lclBl.getFileNumberId(), REMARKS_DR_AUTO_NOTES, "AutoRates Recalculated", user.getUserId());
        List<LclImportsRatesBean> importChargesList = new ArrayList();
        if (CommonUtils.isNotEmpty(commodityList)) {
            importChargesList = new LCLImportRatesDAO().getLCLImportCharges(pooSchnum, fdSchnum, commodityList, null, billingTermsList);
            if (importChargesList.isEmpty()) {
                importChargesList = new LCLImportRatesDAO().getLCLImportCharges(polSchnum, fdSchnum, commodityList, null, billingTermsList);
            }
        }
        if (importChargesList.size() > 0) {
            lclBlAcList = this.calculateImportRate(importChargesList, commodity,
                    lclBl.getBillToParty(), lclBl.getFileNumberId(), user, request);
        }
        return lclBlAcList;
    }

    public List<LclBlAc> calculateImportRate(List<LclImportsRatesBean> ratesList, List<LclBlPiece> commodityList,
            String billToParty, Long fileId, User user, HttpServletRequest request) throws Exception {

        Double totalMeasureImp = 0.00;
        Double totalWeightImp = 0.00;
        Double totalMeasureMet = 0.00;
        Double totalWeightMet = 0.00;
        Double totalAmount = 0.00;
        String billingType = "";
        BigDecimal measureDiv = new BigDecimal(1000);
        BigDecimal weightDiv = new BigDecimal(100);
        LclBlPiece lclblPiece = null;
        LclBlAc cafBookingAc = null;
        GlMappingDAO glMappingDAO = new GlMappingDAO();
        Date now = new Date();
        String shipmentType = LCL_SHIPMENT_TYPE_IMPORT;
        String blueGLchargeCode = "";
        Map<String, BigDecimal> adjustmentAmount = new LinkedHashMap<String, BigDecimal>();
        Map<String, String> adjustmentComment = new LinkedHashMap<String, String>();
        for (LclBlPiece lbp : commodityList) {
            lclblPiece = lbp;
            if (lbp.getActualVolumeImperial() != null && lbp.getActualVolumeImperial().doubleValue() != 0.00) {
                totalMeasureImp += lbp.getActualVolumeImperial().doubleValue();//cft
            }
            if (lbp.getActualWeightImperial() != null && lbp.getActualWeightImperial().doubleValue() != 0.00) {
                totalWeightImp += lbp.getActualWeightImperial().doubleValue();//LBS
            }
            if (lbp.getActualVolumeMetric() != null && lbp.getActualVolumeMetric().doubleValue() != 0.00) {
                totalMeasureMet += lbp.getActualVolumeMetric().doubleValue();//CBM
            }
            if (lbp.getActualWeightMetric() != null && lbp.getActualWeightMetric().doubleValue() != 0.00) {
                totalWeightMet += lbp.getActualWeightMetric().doubleValue();//KGS
            }
        }
        List<LclBlAc> lclBlAcList = new LclBlAcDAO().getLclCostByFileNumberME(fileId, false);
        new LclBlAcDAO().deleteLclCostByFileNumber(fileId);
        LclFileNumber lclFileNumber = new LclFileNumberDAO().findById(fileId);
        for (LclBlAc lclBlAc : lclBlAcList) {
            if (lclBlAc != null) {
                String charge = lclBlAc.getArglMapping().getChargeCode();
                BigDecimal amount = lclBlAc.getArAmount();
                LclRemarks lclRemarks = new LclRemarks();
                lclRemarks.setLclFileNumber(lclFileNumber);
                lclRemarks.setEnteredBy(user);
                lclRemarks.setEnteredDatetime(new Date());
                lclRemarks.setModifiedBy(user);
                lclRemarks.setModifiedDatetime(new Date());
                lclRemarks.setRemarks("DELETED -> Charge Code -> " + charge + " Charge Amount -> " + amount);
                lclRemarks.setType(LclCommonConstant.REMARKS_BL_AUTO_NOTES);
                adjustmentAmount.put(lclBlAc.getArglMapping().getBlueScreenChargeCode(), lclBlAc.getAdjustmentAmount());
                adjustmentComment.put(lclBlAc.getArglMapping().getBlueScreenChargeCode(), lclBlAc.getAdjustmentComment());
                new LclRemarksDAO().saveOrUpdate(lclRemarks);
            }
        }
        for(LclImportsRatesBean chargeBean : ratesList) {
            String chargeCode = "";
            Double minCharge = 0.0;
            String chargeType = "";
            Double blpct = 0.0;
            Double cw$ = 0.0;
            Double inrate = 0.0;
            Double insamt = 0.0;
            Double cbmrt = 0.0;
            Double kgsrt = 0.0;
            Double cf$ = 0.0;
            Double flatrt = 0.0;
            Double maximumCharge = 0.0;
            String billToPartyAuto = "";
            if (null != chargeBean.getChargeCode()) {
                chargeCode = chargeBean.getChargeCode();
            }
            if (null != chargeBean.getMinCharge()) {
                minCharge = chargeBean.getMinCharge().doubleValue();
            }
            if (null != chargeBean.getChargeType()) {
                chargeType = chargeBean.getChargeType();
            }
            if (null != chargeBean.getBlpct()) {
                blpct = chargeBean.getBlpct().doubleValue();
            }
            if (null != chargeBean.getLbs()) {
                cw$ = chargeBean.getLbs().doubleValue();
            }
            if (null != chargeBean.getInrate()) {
                inrate = chargeBean.getInrate().doubleValue();
            }
            if (null != chargeBean.getInsamt()) {
                insamt = chargeBean.getInsamt().doubleValue();
            }
            if (null != chargeBean.getCbmrt()) {
                cbmrt = chargeBean.getCbmrt().doubleValue();
            }
            if (null != chargeBean.getKgsrt()) {
                kgsrt = chargeBean.getKgsrt().doubleValue();
            }
            if (null != chargeBean.getCft()) {
                cf$ = chargeBean.getCft().doubleValue();
            }
            if (null != chargeBean.getFlatrt()) {
                flatrt = chargeBean.getFlatrt().doubleValue();
            }
            if (null != chargeBean.getMaximumCharge()) {
                maximumCharge = chargeBean.getMaximumCharge().doubleValue();
            }
            if (null != chargeBean.getBillingType()) {
                billingType = chargeBean.getBillingType();
            }
            GlMapping arglMapping = glMappingDAO.findByBlueScreenChargeCode(chargeCode, shipmentType, TRANSACTION_TYPE_ACCOUNT_RECEIVABLE);
            if (arglMapping != null) {
                LclBlAc lclBlAc = new LclBlAc();
                lclBlAc.setArglMapping(arglMapping);
                if ("1".equalsIgnoreCase(chargeType)) {
                    if (CommonUtils.isEmpty(lclBlAc.getSpReferenceNo())) {
                        lclBlAc.setRolledupCharges(new BigDecimal(flatrt).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                    } else {
                        lclBlAc.setRolledupCharges(lclBlAc.getArAmount());
                    }
                    lclBlAc.setRatePerUnitUom(RATE_UNIT_PER_UOM_FL);
                    //importChargeList.add(lclBlAc);
                }
                if ("2".equalsIgnoreCase(chargeType)) {
                    if (CommonUtils.isEmpty(lclBlAc.getSpReferenceNo())) {
                        BigDecimal blpctValue = new BigDecimal(blpct).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP);
                        lclBlAc.setRolledupCharges(blpctValue);//Blpcpercentage
                        lclBlAc.setRatePerUnit(blpctValue);
                    } else {
                        lclBlAc.setRolledupCharges(lclBlAc.getArAmount());
                    }
                    lclBlAc.setTotalWeight(new BigDecimal(minCharge));//Min
                    lclBlAc.setRatePerUnitUom(RATE_UNIT_PER_UOM_PCT);
                    // importChargeList.add(lclBlAc);
                }
                if ("3".equalsIgnoreCase(chargeType)) {
                    Double cbmCalValue = 0.0;
                    Double kgsCalValue = 0.0;
                    lclBlAc.setRateFlatMinimum(new BigDecimal(minCharge));
                    if (CommonUtils.isEmpty(lclBlAc.getSpReferenceNo())) {
                        if (cw$ != 0.0 || cf$ != 0.0) {
                            cbmCalValue = cf$ * totalMeasureImp;
                            kgsCalValue = (cw$ * totalWeightImp) / 100;
                            if (maximumCharge != 0.00 && (cbmCalValue >= maximumCharge || kgsCalValue >= maximumCharge)) {
                                lclBlAc.setRolledupCharges(new BigDecimal(maximumCharge).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                                lclBlAc.setRatePerUnitUom(RATE_UNIT_PER_UOM_MAX);
                            } else if (cbmCalValue <= minCharge && kgsCalValue <= minCharge) {
                                lclBlAc.setRolledupCharges(new BigDecimal(minCharge).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                                lclBlAc.setRatePerUnitUom(RATE_UNIT_PER_UOM_MIN);
                            } else if (cbmCalValue <= kgsCalValue) {
                                lclBlAc.setRolledupCharges(new BigDecimal(kgsCalValue).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                                lclBlAc.setRatePerUnitUom(RATE_UNIT_PER_UOM_WEIGHT);
                            } else if (cbmCalValue >= kgsCalValue) {
                                lclBlAc.setRolledupCharges(new BigDecimal(cbmCalValue).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                                lclBlAc.setRatePerUnitUom(RATE_UNIT_PER_UOM_VOLUME);
                            }
                            lclBlAc.setRateUom(RATE_UOM_I);
                            lclBlAc.setRatePerWeightUnit(new BigDecimal(cw$));
                            lclBlAc.setRatePerWeightUnitDiv(weightDiv);
                            lclBlAc.setRatePerVolumeUnit(new BigDecimal(cf$));
                            lclBlAc.setRatePerVolumeUnitDiv(measureDiv);
                        } else if (cbmrt != 0.0 || kgsrt != 0.0) {
                            cbmCalValue = cbmrt * totalMeasureMet;
                            kgsCalValue = (kgsrt * totalWeightMet) / 1000;
                            if (maximumCharge != 0.00 && (cbmCalValue >= maximumCharge || kgsCalValue >= maximumCharge)) {
                                lclBlAc.setRolledupCharges(new BigDecimal(maximumCharge).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                                lclBlAc.setRatePerUnitUom(RATE_UNIT_PER_UOM_MAX);
                            } else if (cbmCalValue <= minCharge && kgsCalValue <= minCharge) {
                                lclBlAc.setRolledupCharges(new BigDecimal(minCharge).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                                lclBlAc.setRatePerUnitUom(RATE_UNIT_PER_UOM_MIN);
                            } else if (cbmCalValue <= kgsCalValue) {
                                lclBlAc.setRolledupCharges(new BigDecimal(kgsCalValue).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                                lclBlAc.setRatePerUnitUom(RATE_UNIT_PER_UOM_WEIGHT);
                            } else if (cbmCalValue >= kgsCalValue) {
                                lclBlAc.setRolledupCharges(new BigDecimal(cbmCalValue).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                                lclBlAc.setRatePerUnitUom(RATE_UNIT_PER_UOM_VOLUME);
                            }
                            lclBlAc.setRatePerWeightUnit(new BigDecimal(kgsrt));
                            lclBlAc.setRatePerWeightUnitDiv(weightDiv);

                            lclBlAc.setRatePerVolumeUnit(new BigDecimal(cbmrt));
                            lclBlAc.setRatePerVolumeUnitDiv(measureDiv);
                            lclBlAc.setRateUom(RATE_UOM_M);
                        }
                    } else {
                        lclBlAc.setRolledupCharges(lclBlAc.getArAmount());
                    }
                    lclBlAc.setApAmount(lclBlAc.getApAmount());
                    //importChargeList.add(lclBookingAc);
                }
                if ("4".equalsIgnoreCase(chargeType)) {
                    Double calValue = 0.0;
                    Double fobValue = 1.00;//need to remove
                    calValue = (fobValue * inrate) / insamt;
                    if (CommonUtils.isEmpty(lclBlAc.getSpReferenceNo())) {
                        if (calValue < minCharge) {
                            lclBlAc.setRolledupCharges(new BigDecimal(minCharge));
                            lclBlAc.setRatePerUnitUom(RATE_UNIT_PER_UOM_MIN);
                        } else {
                            lclBlAc.setRolledupCharges(new BigDecimal(calValue).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                            lclBlAc.setRatePerUnitUom(RATE_UNIT_PER_UOM_WEIGHT);
                        }
                    } else {
                        lclBlAc.setRolledupCharges(lclBlAc.getArAmount());
                    }
                    //  importChargeList.add(lclBlAc);
                }
                lclBlAc.setArAmount(null != lclBlAc.getRolledupCharges() ? lclBlAc.getRolledupCharges() : new BigDecimal(0.00));
                lclBlAc.setRolledupCharges(lclBlAc.getArAmount());
                BigDecimal adjAmount = adjustmentAmount.get(lclBlAc.getArglMapping().getBlueScreenChargeCode());
                String adjComment = adjustmentComment.get(lclBlAc.getArglMapping().getBlueScreenChargeCode());
                lclBlAc.setAdjustmentAmount(null != adjAmount ? adjAmount : BigDecimal.ZERO);
                lclBlAc.setAdjustmentComment(null != adjComment ? adjComment : "");
                billToPartyAuto = (CommonUtils.isNotEmpty(billingType) && "W".equalsIgnoreCase(billingType)) ? billingType : "P".equalsIgnoreCase(billingType) ? "A" : billToParty;

                if (CommonUtils.isEmpty(lclBlAc.getArBillToParty()) || CommonUtils.isEmpty(lclBlAc.getSpReferenceNo())) {
                    lclBlAc.setArBillToParty(billToPartyAuto);
                }
                lclBlAc.setPrintOnBl(true);
                lclBlAc.setBundleIntoOf(false);
                if (lclblPiece != null) {
                    lclBlAc.setLclBlPiece(lclblPiece);
                }
                lclBlAc.setEnteredBy(user);
                lclBlAc.setModifiedBy(user);
                lclBlAc.setEnteredDatetime(now);
                lclBlAc.setModifiedDatetime(now);
                lclBlAc.setTransDatetime(now);
                if (fileId != null && fileId > 0) {
                    lclBlAc.setLclFileNumber(lclFileNumber);
                    if (lclBlAc.getApAmount() == null) {
                        lclBlAc.setApAmount(BigDecimal.ZERO);
                    }
                    if (!CHARGE_CODE_CAF.equalsIgnoreCase(lclBlAc.getArglMapping().getChargeCode())) {
                        if (lclBlAc.getArAmount() != null) {
                            totalAmount += lclBlAc.getArAmount().doubleValue();
                        }
                        new LclBlAcDAO().saveOrUpdate(lclBlAc);
                    } else {
                        cafBookingAc = lclBlAc;
                    }
                }
            } else if (arglMapping == null) {
                blueGLchargeCode += chargeCode + ",";
            }
        }

        HttpSession session = request.getSession();
        LclSession lclSession = (LclSession) session.getAttribute("lclSession") != null ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        if (CommonUtils.isNotEmpty(blueGLchargeCode)) {
            lclSession.setIsArGlmappingFlag(true);
            lclSession.setGlMappingBlueCode(blueGLchargeCode);
        } else {
            lclSession.setIsArGlmappingFlag(false);
            lclSession.setGlMappingBlueCode(null);
        }
        session.setAttribute("lclSession", lclSession);

        if (cafBookingAc != null) {
            Double cafAmt = this.calculateExportCAFAmount(cafBookingAc, totalAmount);
            cafBookingAc.setArAmount(new BigDecimal(cafAmt).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
            if (cafBookingAc.getApAmount() == null) {
                cafBookingAc.setApAmount(BigDecimal.ZERO);
            }
            new LclBlAcDAO().saveOrUpdate(cafBookingAc);
        }
        List<LclBlAc> blAcList = new LclBlAcDAO().findByProperty("lclFileNumber.id", fileId);
        return blAcList;
    }

    public Double calculateExportCAFAmount(LclBlAc cafBlAc, Double totalAmount) throws Exception {
        Double finalValue = 0.0;
        if (cafBlAc.getRolledupCharges() != null) {
            finalValue = totalAmount * cafBlAc.getRolledupCharges().doubleValue();
        }
        if (cafBlAc.getTotalWeight() != null && cafBlAc.getTotalWeight().doubleValue() > finalValue) {
            finalValue = cafBlAc.getTotalWeight().doubleValue();
        }
        return finalValue;
    }
}
