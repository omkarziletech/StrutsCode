/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.lcl.common.constant;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.lcl.dwr.LclDwr;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.LclQuote;
import com.gp.cong.logisoft.domain.lcl.LclQuoteAc;
import com.gp.cong.logisoft.domain.lcl.LclQuoteImport;
import com.gp.cong.logisoft.domain.lcl.LclQuotePiece;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.WarehouseDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPlanDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuoteAcDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuoteImportAmsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuoteImportDAO;
import com.gp.cong.logisoft.lcl.model.LclBookingVoyageBean;
import com.gp.cvst.logisoft.struts.form.lcl.LCLQuoteForm;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Mei
 */
public class ImportQuoteUtils implements LclCommonConstant {

    public LclQuoteImport saveQteImp(LCLQuoteForm lclQuoteForm, LclFileNumber lclFileNumber, User loginUser, Date todayDate) throws Exception {
        LclQuoteImportDAO lclQuoteImportDAO = new LclQuoteImportDAO();
        LclQuoteImport lclQuoteImport = lclQuoteImportDAO.findById(lclFileNumber.getId());
        if (null == lclQuoteImport) {
            lclQuoteImport = lclQuoteForm.getLclQuoteImport();
            lclQuoteImport.setFileNumberId(lclFileNumber.getId());
            lclQuoteImport.setLclFileNumber(lclFileNumber);
        }
        lclQuoteImport.setDestWhse(new WarehouseDAO().findById(17));
        lclQuoteImport.setSubHouseBl(lclQuoteForm.getSubHouseBl());
        lclQuoteImport.setItNo(lclQuoteForm.getItNo());
        lclQuoteImport.setEntryClass(lclQuoteForm.getEntryClass());
        Boolean value = lclQuoteImport.getDeclaredValueEstimated();
        Boolean weight = lclQuoteImport.getDeclaredWeightEstimated();
        lclQuoteImport.setDeclaredValueEstimated(null != value ? value : false);
        lclQuoteImport.setDeclaredWeightEstimated(null != weight ? weight : false);
        if (CommonUtils.isNotEmpty(lclQuoteForm.getScac())) {
            lclQuoteImport.setScac(lclQuoteForm.getScac());
        }
        if (CommonUtils.isNotEmpty(lclQuoteForm.getEntryNo())) {
            lclQuoteImport.setEntryNo(lclQuoteForm.getEntryNo());
        }
        if (CommonUtils.isNotEmpty(lclQuoteForm.getPortExit())) {
            lclQuoteImport.setUsaPortOfExit(new UnLocation(lclQuoteForm.getPortExitId()));
        }
        if (CommonUtils.isNotEmpty(lclQuoteForm.getForeignDischarge())) {
            lclQuoteImport.setForeignPortOfDischarge(new UnLocation(lclQuoteForm.getForeignDischargeId()));
        }
        if (CommonUtils.isEqual(lclQuoteForm.getExpressReleaseClasuse(), "Y")) {
            lclQuoteImport.setExpressReleaseClause(true);
        } else {
            lclQuoteImport.setExpressReleaseClause(false);
        }
        if (CommonUtils.isNotEmpty(lclQuoteForm.getGoDate())) {
            lclQuoteImport.setGoDatetime(DateUtils.parseDate(lclQuoteForm.getGoDate(), "dd-MMM-yyyy"));
        }
        if (CommonUtils.isNotEmpty(lclQuoteForm.getPickupDate())) {
            lclQuoteImport.setPickupDateTime(DateUtils.parseDate(lclQuoteForm.getPickupDate(), "dd-MMM-yyyy"));
        }
        if (CommonUtils.isNotEmpty(lclQuoteForm.getLastFd())) {
            lclQuoteImport.setLastFreeDateTime(DateUtils.parseDate(lclQuoteForm.getLastFd(), "dd-MMM-yyyy"));
        }
        if (CommonUtils.isNotEmpty(lclQuoteForm.getStGeorgeAccount())) {
            lclQuoteImport.setIpiCfsAcctNo(new TradingPartner(lclQuoteForm.getStGeorgeAccountNo()));
            lclQuoteImport.getIpiCfsAcctNo().setAccountName(lclQuoteForm.getStGeorgeAccount());
            lclQuoteImport.getIpiCfsAcctNo().setAddress(lclQuoteForm.getStGeorgeAddress());
        } else {
            lclQuoteImport.setIpiCfsAcctNo(null);
        }
        if ("Y".equalsIgnoreCase(lclQuoteForm.getTransShipMent())) {
            lclQuoteImport.setTransShipment(true);
            lclQuoteForm.getLclQuote().setRelayOverride(Boolean.TRUE);
            String[] agentInfo = new PortsDAO().getDefaultAgentForLcl(lclQuoteForm.getUnlocationCode(), "L");
            if (agentInfo != null && agentInfo.length > 0 && agentInfo[0] != null) {
                lclQuoteImport.setExportAgentAcctNo(new TradingPartner(agentInfo[0].toString()));
            }
        } else {
            lclQuoteImport.setTransShipment(false);
            lclQuoteForm.getLclQuote().setAgentAcct(null);
            lclQuoteImport.setExportAgentAcctNo(null);
        }
        lclQuoteImport.setEnteredBy(loginUser);
        lclQuoteImport.setModifiedBy(loginUser);
        lclQuoteImport.setEnteredDatetime(todayDate);
        lclQuoteImport.setModifiedDatetime(todayDate);
        lclQuoteImportDAO.saveOrUpdate(lclQuoteImport);
        return lclQuoteImport;
    }

    public void setWeighMeasureForImpQuote(HttpServletRequest request, List<LclQuotePiece> lclCommodityList) {
        if (CommonUtils.isNotEmpty(lclCommodityList)) {
            LclQuotePiece lclQuotePiece = lclCommodityList.get(0);
            if (lclQuotePiece.getActualWeightMetric() != null && lclQuotePiece.getActualWeightMetric().doubleValue() != 0.00) {
                request.setAttribute("weight", lclQuotePiece.getActualWeightMetric() + " KGS");
            } else if (lclQuotePiece.getBookedWeightMetric() != null && lclQuotePiece.getBookedWeightMetric().doubleValue() != 0.00) {
                request.setAttribute("weight", lclQuotePiece.getBookedWeightMetric() + " KGS");
            }
            if (lclQuotePiece.getActualVolumeMetric() != null && lclQuotePiece.getActualVolumeMetric().doubleValue() != 0.00) {
                request.setAttribute("measure", lclQuotePiece.getActualVolumeMetric() + " CBM");
            } else if (lclQuotePiece.getBookedVolumeMetric() != null && lclQuotePiece.getBookedVolumeMetric().doubleValue() != 0.00) {
                request.setAttribute("measure", lclQuotePiece.getBookedVolumeMetric() + " CBM");
            }
            if (lclQuotePiece.getCommodityType() != null && lclQuotePiece.getCommodityType().getCode() != null) {
                request.setAttribute("commodityNumber", lclQuotePiece.getCommodityType().getCode());
            }
        }
    }
    //Imports Rates for Quote

    public void setImpRolledUpChargesForQuote(List<LclQuoteAc> chargeList, HttpServletRequest request, Long longFileId,
            List<LclQuotePiece> lclCommodityList, String billingType) throws Exception {
        LclQuoteAcDAO lclQuoteAcDAO = new LclQuoteAcDAO();
        if (chargeList.isEmpty()) {
            request.setAttribute("rateErrorMessage", "No Rates Found.");
        } else {
            chargeList = impFormatLabelChargeQuote(chargeList);
        }
        request.setAttribute("chargeList", chargeList);
        if (longFileId != null && longFileId > 0) {
            request.setAttribute("totalCollectChargesAmt", lclQuoteAcDAO.getTotalAmountByCollect(longFileId, "A"));
            request.setAttribute("totalPrepaidChargesAmt", lclQuoteAcDAO.getTotalAmountByPrepaid(longFileId, "A"));
            request.setAttribute("totalCharges", lclQuoteAcDAO.getTotalLclCostByFileNumber(longFileId));
            request.setAttribute("totalCostAmt", lclQuoteAcDAO.getTotalApAmt(longFileId));
        }
    }

    public List impFormatLabelChargeQuote(List<LclQuoteAc> lclQuoteAcList) {
        int count = 0;
        for (int i = 0; i < lclQuoteAcList.size(); i++) {
            LclQuoteAc lclQuoteAc = lclQuoteAcList.get(i);
            if (null != lclQuoteAcList.get(i).getArglMapping() && lclQuoteAcList.get(i).getArglMapping().getChargeCode().equals("OFIMP")
                    && !lclQuoteAcList.get(i).isManualEntry()) {
                Collections.swap(lclQuoteAcList, 0, count);
            } else {
                count++;
            }
            lclQuoteAc.setRolledupCharges(lclQuoteAc.getArAmount());
            if (lclQuoteAc.getRatePerUnitUom() != null && !lclQuoteAc.getRatePerUnitUom().trim().equals("")) {
                if (RATE_UNIT_PER_UOM_WEIGHT.equalsIgnoreCase(lclQuoteAc.getRatePerUnitUom())) {
                    lclQuoteAc.setLabel1("*** TO WEIGHT ***");
                } else if (RATE_UNIT_PER_UOM_VOLUME.equalsIgnoreCase(lclQuoteAc.getRatePerUnitUom())) {
                    lclQuoteAc.setLabel1("*** VOLUME ***");
                } else if (RATE_UNIT_PER_UOM_MIN.equalsIgnoreCase(lclQuoteAc.getRatePerUnitUom())) {
                    lclQuoteAc.setLabel1("*** MINIMUM ***");
                } else if (RATE_UNIT_PER_UOM_MAX.equalsIgnoreCase(lclQuoteAc.getRatePerUnitUom())) {
                    lclQuoteAc.setLabel1("*** MAXIMUM ***");
                }
                if (lclQuoteAc.getArglMapping() != null && lclQuoteAc.getArglMapping().getBlueScreenChargeCode() != null) {
                    if (RATE_UNIT_PER_UOM_FL.equalsIgnoreCase(lclQuoteAc.getRatePerUnitUom())) {
                        lclQuoteAc.setLabel2("$" + lclQuoteAc.getArAmount().doubleValue() + " FLAT RATE.");
                    } else if (RATE_UNIT_PER_UOM_PCT.equalsIgnoreCase(lclQuoteAc.getRatePerUnitUom()) && lclQuoteAc.getRatePerUnit() != null) {
                        int ratePercentage = (int) (lclQuoteAc.getRatePerUnit().doubleValue() * 100);
                        lclQuoteAc.setLabel2(String.valueOf(ratePercentage) + " PERCENT OF THE B/L.");
                    } else if (!RATE_UNIT_PER_UOM_FL.equalsIgnoreCase(lclQuoteAc.getRatePerUnitUom()) && !RATE_UNIT_PER_UOM_PCT.equalsIgnoreCase(lclQuoteAc.getRatePerUnitUom())
                            && RATE_UOM_M.equalsIgnoreCase(lclQuoteAc.getRateUom())) {
                        lclQuoteAc.setLabel2("$" + NumberUtils.convertToTwoDecimal(lclQuoteAc.getRatePerVolumeUnit().doubleValue()) + " CBM, "
                                + NumberUtils.convertToTwoDecimal(lclQuoteAc.getRatePerWeightUnit().doubleValue()) + "/1000.00 KGS,"
                                + " ($" + NumberUtils.convertToTwoDecimal(lclQuoteAc.getRateFlatMinimum().doubleValue()) + " MINIMUM/"
                                + NumberUtils.convertToTwoDecimal(lclQuoteAc.getRateFlatMaximum() != null ? lclQuoteAc.getRateFlatMaximum().doubleValue() : 0.00) + " MAXIMUM)");
                    } else {
                        lclQuoteAc.setLabel2("$" + NumberUtils.convertToTwoDecimal(lclQuoteAc.getRatePerVolumeUnit().doubleValue()) + " CFT, "
                                + NumberUtils.convertToTwoDecimal(lclQuoteAc.getRatePerWeightUnit().doubleValue()) + "/100.00 LBS, "
                                + " ($" + NumberUtils.convertToTwoDecimal(lclQuoteAc.getRateFlatMinimum().doubleValue()) + " MINIMUM/"
                                + NumberUtils.convertToTwoDecimal(lclQuoteAc.getRateFlatMaximum() != null ? lclQuoteAc.getRateFlatMaximum().doubleValue() : 0.00) + " MAXIMUM)");
                    }
                }
            }
        }
        return lclQuoteAcList;
    }

    public List impLabelFormatChargeforPdfQuote(List<LclQuoteAc> lclQuoteAcList) {
        for (int i = 0; i < lclQuoteAcList.size(); i++) {
            LclQuoteAc lclQuoteAc = lclQuoteAcList.get(i);
            lclQuoteAc.setRolledupCharges(lclQuoteAc.getArAmount());
            if (lclQuoteAc.getRatePerUnitUom() != null && !lclQuoteAc.getRatePerUnitUom().trim().equals("")) {
                if (lclQuoteAc.getRatePerUnitUom().equalsIgnoreCase("FL")) {
                    if (lclQuoteAc.getArglMapping() != null && lclQuoteAc.getArglMapping().getBlueScreenChargeCode() != null) {
//                        if (lclQuoteAc.getArglMapping().getBlueScreenChargeCode().equals("0006")) {
//                            lclQuoteAc.setLabel2("(" + lclQuoteAc.getRatePerWeightUnit().toString() + " PER "
//                                    + lclQuoteAc.getRatePerWeightUnitDiv().toString() + " CIF)");
//                        } else {
                        lclQuoteAc.setLabel2("(" + lclQuoteAc.getArAmount().doubleValue() + " FLAT RATE)");
                        // }
                    }
                    if (lclQuoteAc.getArglMapping().getChargeCode() != null && (lclQuoteAc.getArglMapping().getChargeCode().equals("OFTBARELL")
                            || lclQuoteAc.getArglMapping().getChargeCode().equals("OFTTTA"))) {
                        lclQuoteAc.setLabel2(lclQuoteAc.getRatePerWeightUnit() + " PER BARELL.");
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
                    if (lclQuoteAc.getRateUom().equalsIgnoreCase("M")) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("(");
                        if (lclQuoteAc.getRatePerVolumeUnit() != null && !lclQuoteAc.getRatePerVolumeUnit().toString().equals("0.00")) {
                            sb.append(NumberUtils.convertToTwoDecimal(lclQuoteAc.getRatePerVolumeUnit().doubleValue())).append("/CBM,");
                        }
                        if (lclQuoteAc.getRatePerWeightUnit() != null && !lclQuoteAc.getRatePerWeightUnit().toString().equals("0.00")) {
                            sb.append(NumberUtils.convertToTwoDecimal(lclQuoteAc.getRatePerWeightUnit().doubleValue())).append("/1000.00 KGS,");
                        }
                        if (lclQuoteAc.getRateFlatMinimum() != null && !lclQuoteAc.getRateFlatMinimum().toString().equals("0.00")) {
                            sb.append(NumberUtils.convertToTwoDecimal(lclQuoteAc.getRateFlatMinimum().doubleValue())).append(" MINIMUM)");
                        }
                        lclQuoteAc.setLabel2(sb.toString());
                    } else {
                        StringBuilder sb = new StringBuilder();
                        sb.append("(");
                        if (lclQuoteAc.getRatePerVolumeUnit() != null && !lclQuoteAc.getRatePerVolumeUnit().toString().equals("0.00")) {
                            sb.append(NumberUtils.convertToTwoDecimal(lclQuoteAc.getRatePerVolumeUnit().doubleValue())).append("/CFT,");
                        }
                        if (lclQuoteAc.getRatePerWeightUnit() != null && !lclQuoteAc.getRatePerWeightUnit().toString().equals("0.00")) {
                            sb.append(NumberUtils.convertToTwoDecimal(lclQuoteAc.getRatePerWeightUnit().doubleValue())).append("/100.00 LBS,");
                        }
                        if (lclQuoteAc.getRateFlatMinimum() != null && !lclQuoteAc.getRateFlatMinimum().toString().equals("0.00")) {
                            sb.append(NumberUtils.convertToTwoDecimal(lclQuoteAc.getRateFlatMinimum().doubleValue())).append(" MINIMUM)");
                        }
                        lclQuoteAc.setLabel2(sb.toString());
                    }
                }
            }
        }
        return lclQuoteAcList;
    }

    public void setUpcomingSailings(LclQuote lclQuote, LclQuoteImport lclQuoteImport, HttpServletRequest request) throws Exception {
        LclBookingPlanDAO lclBookingPlanDAO = new LclBookingPlanDAO();
//        if (lclQuoteImport.getUsaPortOfExit() != null && lclQuoteImport.getForeignPortOfDischarge() != null
//                && lclQuote.getPortOfDestination() != null && lclQuote.getFinalDestination() != null) {
//            List<LclBookingVoyageBean> voyageList = lclBookingPlanDAO.getVoyageList(lclQuote.getPortOfDestination().getId(),
//                    lclQuoteImport.getUsaPortOfExit().getId(), lclQuoteImport.getForeignPortOfDischarge().getId(), lclQuote.getFinalDestination().getId(), "V");
//            request.setAttribute("voyageAction", true);
//            request.setAttribute("voyageList", voyageList);
//        }
        String[] getExternalGriRemarks = new LclDwr().defaultDestinationImportRemarks(lclQuote.getFinalDestination().getUnLocationCode());
        request.setAttribute("fdGriRemarks", getExternalGriRemarks[2]);
    }

    public void agentCount(String unlocationCode, HttpServletRequest request) throws Exception {
        Integer agentCount = new PortsDAO().getAgentCount(unlocationCode, LCL_IMPORT_TYPE);
        if (agentCount > 1) {
            request.setAttribute("agentFlag", true);
        }
    }

    public void setRequestValues(LCLQuoteForm lclQuoteForm, LclQuote lclQuote,
            List<LclQuotePiece> lclQuotePieceList, HttpServletRequest request) throws Exception {
        if (lclQuote != null && lclQuote.getPortOfLoading() != null && lclQuote.getPortOfLoading().getUnLocationCode() != null) {
            agentCount(lclQuote.getPortOfLoading().getUnLocationCode(), request);//set origint agent style
        }
        setTransitTime(lclQuote, lclQuotePieceList, request);//set origint agent style
        request.setAttribute("amsHBLList", new LclQuoteImportAmsDAO().findAll(lclQuote.getFileNumberId()));
    }

    public void setTransitTime(LclQuote lclQuote, List<LclQuotePiece> lclQuotePieceList,
            HttpServletRequest request) throws Exception {
        if (null != lclQuotePieceList && !lclQuotePieceList.isEmpty()) {
            int transitTime = 0;
            LclQuoteAcDAO lclquoteacdao = new LclQuoteAcDAO();
            String ofimpFlag = lclquoteacdao.isChargeCodeValidate(String.valueOf(lclQuote.getFileNumberId()), "OFIMP", "");
            if ("true".equalsIgnoreCase(ofimpFlag) && lclQuote.getPortOfLoading() != null && lclQuote.getPortOfDestination() != null
                    && null != lclQuotePieceList && !lclQuotePieceList.isEmpty()) {
                transitTime = lclquoteacdao.getTransitTime(lclQuote.getPortOfLoading().getUnLocationCode(),
                        lclQuote.getPortOfDestination().getUnLocationCode(),
                        lclQuotePieceList.get(0).getCommodityType().getCode());
            }
            request.setAttribute("transitTime", transitTime);
        }
    }
}
