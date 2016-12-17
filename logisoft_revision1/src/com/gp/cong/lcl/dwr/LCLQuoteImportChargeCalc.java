package com.gp.cong.lcl.dwr;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.logisoft.beans.LclImportsRatesBean;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import java.math.BigDecimal;
import java.util.ArrayList;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBookingPiece;
import com.gp.cong.logisoft.domain.lcl.LclQuoteAc;
import com.gp.cong.logisoft.domain.lcl.LclQuotePiece;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLImportRatesDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuoteAcDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO;
import com.gp.cvst.logisoft.domain.GlMapping;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LCLQuoteImportChargeCalc implements LclCommonConstant, ConstantsInterface {

    private LclQuoteAcDAO lclQuoteAcDAO = new LclQuoteAcDAO();

    public List<LclQuoteAc> ImportRateCalculation(String originCode, String polUnCode, String podUnCode, String fdUnCode, String transhipment, String billingType,
            String billToParty, Long fileId, List<LclQuotePiece> lclQuotePiecesList, User user, HttpServletRequest request) throws Exception {
        LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
        PortsDAO portsDAO = new PortsDAO();
        LCLImportRatesDAO lclImportRatesDAO = new LCLImportRatesDAO();
        LCLQuoteImportChargeCalc lclQuoteImportChargeCalc = new LCLQuoteImportChargeCalc();
        List<String> chargeCodeList = new ArrayList<String>();//no need to retrive charges for these charge codes
        List<String> commodityList = new ArrayList<String>();
        List<LclQuoteAc> calculateImportChargeList = null;
        boolean hazmatFound = false;
        String ipiIgnoreStatus = "";
        String billingTerms[] = {"C", "P"};
        List<String> billingTermsList = Arrays.asList(billingTerms);
        List<LclImportsRatesBean> exceptionRatesList = new ArrayList();
        List<LclImportsRatesBean> importChargesList = new ArrayList();
        List<LclImportsRatesBean> ratesList =new ArrayList();
        for (LclQuotePiece lqp : lclQuotePiecesList) {
            if (lqp.getCommodityType() != null && lqp.getCommodityType().getCode() != null) {
                commodityList.add(lqp.getCommodityType().getCode());
                ipiIgnoreStatus = lclQuotePiecesList.get(lclQuotePiecesList.size() - 1).getCommodityType().getCode();
            } else {
                commodityList.add(lqp.getCommNo());
                ipiIgnoreStatus = lqp.getCommNo();
            }
            if (lqp.isHazmat()) {
                hazmatFound = true;
            }
        }
        if (CommonUtils.isNotEmpty(ipiIgnoreStatus)) {
            ipiIgnoreStatus = lclImportRatesDAO.checkNewIpiCost(ipiIgnoreStatus);
        }
        //adding the charge code for the hazardus
           if (!hazmatFound) {
            if (!chargeCodeList.contains("1625")) {
                chargeCodeList.add("1625");
            }
            if (!chargeCodeList.contains("1682")) {
                chargeCodeList.add("1682");
            }
        }

        String polSchnum = portsDAO.getShedulenumber(polUnCode);//pol Schedule Number
        String podSchnum = portsDAO.getShedulenumber(podUnCode);
        String fdSchnum = portsDAO.getShedulenumber(fdUnCode);
        String pooSchnum = portsDAO.getShedulenumber(originCode);
        
        exceptionRatesList=lclImportRatesDAO.getExceptionList(!pooSchnum.equalsIgnoreCase("") ? pooSchnum : "00000", polSchnum, podSchnum, fdSchnum, commodityList);
        //Delete Old Charges
        if (fileId != null && fileId > 0) {
            lclRemarksDAO.insertLclRemarks(fileId, REMARKS_QT_AUTO_NOTES, "AutoRates Recalculated", user.getUserId());
            lclQuoteAcDAO.deleteLclCostByFileNumber(fileId, "I");
        }
        if ("Y".equalsIgnoreCase(transhipment)) {
            podSchnum = fdSchnum;
        }
        if ("Y".equalsIgnoreCase(ipiIgnoreStatus) && "N".equalsIgnoreCase(transhipment) && CommonUtils.isNotEqualIgnoreCase(podUnCode, fdUnCode)) {
            importChargesList = lclImportRatesDAO.ignoreIpiRatesTrigger(polSchnum, fdSchnum, commodityList, chargeCodeList, billingTermsList);
            ratesList.addAll(importChargesList);
//            calculateImportChargeList = lclQuoteImportChargeCalc.calculateQuoteImportRate(importCharges,
//                    lclQuotePiecesList, billToParty, fileId, transhipment, user, exceptionRatesList, request);
        } else {
            //calculate rates from Origin to POD
            if (CommonUtils.isNotEmpty(originCode)) {
                if (!originCode.equalsIgnoreCase(polUnCode)) {
                    importChargesList = lclImportRatesDAO.getLCLImportCharges(pooSchnum, podSchnum, commodityList, chargeCodeList, billingTermsList);
                    ratesList.addAll(importChargesList);
                    if (importChargesList.isEmpty()) {
                        request.setAttribute("validateImpRates", "No rates existing using the absolute origin city.");
                    }
                } else {
                    importChargesList = lclImportRatesDAO.getLCLImportCharges(polSchnum, podSchnum, commodityList, chargeCodeList, billingTermsList);
                    ratesList.addAll(importChargesList);
                }
            } // otherwise , calculate rates from POL to POD
            else {
                importChargesList = lclImportRatesDAO.getLCLImportCharges(polSchnum, podSchnum, commodityList, chargeCodeList, billingTermsList);
                ratesList.addAll(importChargesList);
            }
//            calculateImportChargeList = lclQuoteImportChargeCalc.calculateQuoteImportRate(importCharges,
//                    lclQuotePiecesList, billToParty, fileId, transhipment, user, exceptionRatesList, request);
            //3rd leg for computing rates, using POD-POD setup
            if ("N".equalsIgnoreCase(transhipment) && CommonUtils.isNotEmpty(podUnCode) && podUnCode.equalsIgnoreCase(fdUnCode)) {
                importChargesList = lclImportRatesDAO.getLCLImportCharges(podSchnum, podSchnum, commodityList,
                        chargeCodeList, billingTermsList);
                ratesList.addAll(importChargesList);
//                List<LclQuoteAc> calculate3LegCharges = lclQuoteImportChargeCalc.calculateQuoteImportRate(import3LegCharges,
//                        lclQuotePiecesList, billToParty, fileId, transhipment, user, request);
//                calculateImportChargeList.addAll(calculate3LegCharges);

            }
        }
        //IPI CHARGES
        if (CommonUtils.isNotEqualIgnoreCase(podUnCode, fdUnCode) && "N".equalsIgnoreCase(transhipment)) {
            importChargesList = lclImportRatesDAO.getLCLImportIPICharges(podSchnum, fdSchnum, commodityList, ipiIgnoreStatus, chargeCodeList);
            ratesList.addAll(importChargesList);
//            List<LclQuoteAc> calculatedIPICharge = lclQuoteImportChargeCalc.calculateQuoteImportRate(ipiCharges, lclQuotePiecesList, billToParty, fileId, transhipment, user, exceptionRatesList,request);
//            calculateImportChargeList.addAll(calculatedIPICharge);
        }
        if (ratesList.size() > 0) {
            calculateImportChargeList = lclQuoteImportChargeCalc.calculateQuoteImportRate(ratesList, lclQuotePiecesList, billToParty, fileId, transhipment, user, exceptionRatesList, request);
        }
        return calculateImportChargeList;
    }

    public List<LclQuoteAc> calculateQuoteImportRate(List<LclImportsRatesBean> ratesList, List<LclQuotePiece> commodityList, String billToParty,
            Long fileId, String transhipment, User user, List<LclImportsRatesBean> exceptionRatesList, HttpServletRequest request) throws Exception {
        GlMappingDAO glMappingDAO = new GlMappingDAO();
        LclQuotePiece lclQuotePiece = null;
        List<LclQuoteAc> importChargeList = new ArrayList();
        Double totalMeasureImp = 0.00;
        Double totalWeightImp = 0.00;
        Double totalMeasureMet = 0.00;
        Double totalWeightMet = 0.00;
        Double totalAmount = 0.00;
        LclQuoteAc cafQuoteAc = null;
        BigDecimal measureDiv = new BigDecimal(1000);
        BigDecimal weightDiv = new BigDecimal(100);
        Date now = new Date();
        String blueGLchargeCode = "";
        String billingType = "";
        List<LclImportsRatesBean> importCharge;
        importCharge = new LCLImportRatesDAO().filterOriginalRatesList(ratesList, exceptionRatesList,transhipment);
        for (LclQuotePiece lqp : commodityList) {
            if (lqp.getBookedVolumeImperial() != null && lqp.getBookedVolumeImperial().doubleValue() != 0.00) {
                totalMeasureImp += lqp.getBookedVolumeImperial().doubleValue();
            }
            if (lqp.getBookedWeightImperial() != null && lqp.getBookedWeightImperial().doubleValue() != 0.00) {
                totalWeightImp += lqp.getBookedWeightImperial().doubleValue();
            }
            if (lqp.getBookedVolumeMetric() != null && lqp.getBookedVolumeMetric().doubleValue() != 0.00) {
                totalMeasureMet += lqp.getBookedVolumeMetric().doubleValue();
            }
            if (lqp.getBookedWeightMetric() != null && lqp.getBookedWeightMetric().doubleValue() != 0.00) {
                totalWeightMet += lqp.getBookedWeightMetric().doubleValue();
            }
            lclQuotePiece = lqp;
        }
        String shipmentType = LCL_SHIPMENT_TYPE_IMPORT;
        if ("Y".equalsIgnoreCase(transhipment)) {
            shipmentType = LCL_SHIPMENT_TYPE_EXPORT;
        }
        for(LclImportsRatesBean chargeBean : importCharge) {
            LclQuoteAc lclQuoteAc = new LclQuoteAc();
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
            chargeCode = chargeBean.getChargeCode();
            if (null != chargeBean.getMinCharge()) {
                minCharge = chargeBean.getMinCharge().doubleValue();
            }
            if (CommonUtils.isNotEmpty(chargeBean.getChargeType())) {
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
            if (CommonUtils.isNotEmpty(chargeBean.getBillingType())) {
                billingType = chargeBean.getBillingType();
            }
            GlMapping arGLMapping = glMappingDAO.findByBlueScreenChargeCode(chargeCode, shipmentType, TRANSACTION_TYPE_ACCOUNT_RECEIVABLE);
            if (null != arGLMapping) {
                lclQuoteAc.setArglMapping(arGLMapping);
                if ("1".equalsIgnoreCase(chargeType)) {
                    lclQuoteAc.setRolledupCharges(new BigDecimal(flatrt).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                    lclQuoteAc.setRatePerUnitUom(RATE_UNIT_PER_UOM_FL);
                    importChargeList.add(lclQuoteAc);
                }
                if ("2".equalsIgnoreCase(chargeType)) {
                    BigDecimal blpctValue = new BigDecimal(blpct).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP);
                    lclQuoteAc.setRolledupCharges(blpctValue);//Blpcpercentage
                    lclQuoteAc.setTotalWeight(new BigDecimal(minCharge));//Min
                    lclQuoteAc.setRatePerUnitUom(RATE_UNIT_PER_UOM_PCT);
                    lclQuoteAc.setRatePerUnit(blpctValue);
                    importChargeList.add(lclQuoteAc);
                }
                if ("3".equalsIgnoreCase(chargeType)) {
                    Double cbmCalValue = 0.0;
                    Double kgsCalValue = 0.0;
                    lclQuoteAc.setRateFlatMinimum(new BigDecimal(minCharge));
                    lclQuoteAc.setRateFlatMaximum(new BigDecimal(maximumCharge).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                    if (cw$ != 0.0 || cf$ != 0.0) {
                        cbmCalValue = cf$ * totalMeasureImp;
                        kgsCalValue = (cw$ * totalWeightImp) / 100;
                        if (maximumCharge != 0.00 && (cbmCalValue >= maximumCharge || kgsCalValue >= maximumCharge)) {
                            lclQuoteAc.setRolledupCharges(new BigDecimal(maximumCharge).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                            lclQuoteAc.setRatePerUnitUom(RATE_UNIT_PER_UOM_MAX);
                        } else if (cbmCalValue <= minCharge && kgsCalValue <= minCharge) {
                            lclQuoteAc.setRolledupCharges(new BigDecimal(minCharge).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                            lclQuoteAc.setRatePerUnitUom(RATE_UNIT_PER_UOM_MIN);
                        } else if (cbmCalValue <= kgsCalValue) {
                            lclQuoteAc.setRolledupCharges(new BigDecimal(kgsCalValue).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                            lclQuoteAc.setRatePerUnitUom(RATE_UNIT_PER_UOM_WEIGHT);
                        } else if (cbmCalValue >= kgsCalValue) {
                            lclQuoteAc.setRolledupCharges(new BigDecimal(cbmCalValue).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                            lclQuoteAc.setRatePerUnitUom(RATE_UNIT_PER_UOM_VOLUME);
                        }
                        lclQuoteAc.setRateUom(RATE_UOM_I);
                        lclQuoteAc.setRatePerWeightUnit(new BigDecimal(cw$));
                        lclQuoteAc.setRatePerWeightUnitDiv(weightDiv);
                        lclQuoteAc.setRatePerVolumeUnit(new BigDecimal(cf$));
                        lclQuoteAc.setRatePerVolumeUnitDiv(measureDiv);
                    } else if (cbmrt != 0.0 || kgsrt != 0.0) {
                        cbmCalValue = cbmrt * totalMeasureMet;
                        kgsCalValue = (kgsrt * totalWeightMet) / 1000;
                        if (maximumCharge != 0.00 && (cbmCalValue >= maximumCharge || kgsCalValue >= maximumCharge)) {
                            lclQuoteAc.setRolledupCharges(new BigDecimal(maximumCharge).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                            lclQuoteAc.setRatePerUnitUom(RATE_UNIT_PER_UOM_MAX);
                        } else if (cbmCalValue <= minCharge && kgsCalValue <= minCharge) {
                            lclQuoteAc.setRolledupCharges(new BigDecimal(minCharge).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                            lclQuoteAc.setRatePerUnitUom(RATE_UNIT_PER_UOM_MIN);
                        } else if (cbmCalValue <= kgsCalValue) {
                            lclQuoteAc.setRolledupCharges(new BigDecimal(kgsCalValue).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                            lclQuoteAc.setRatePerUnitUom(RATE_UNIT_PER_UOM_WEIGHT);
                        } else if (cbmCalValue >= kgsCalValue) {
                            lclQuoteAc.setRolledupCharges(new BigDecimal(cbmCalValue).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                            lclQuoteAc.setLabel1("*** VOLUME ***");
                            lclQuoteAc.setRatePerUnitUom(RATE_UNIT_PER_UOM_VOLUME);
                        }
                        lclQuoteAc.setRatePerWeightUnit(new BigDecimal(kgsrt));
                        lclQuoteAc.setRatePerWeightUnitDiv(weightDiv);
                        lclQuoteAc.setRatePerVolumeUnit(new BigDecimal(cbmrt));
                        lclQuoteAc.setRatePerVolumeUnitDiv(measureDiv);
                        lclQuoteAc.setRateUom(RATE_UOM_M);
                    }
                    importChargeList.add(lclQuoteAc);
                }
                if ("4".equalsIgnoreCase(chargeType)) {
                    Double calValue = 0.0;
                    Double fobValue = 1.00;//need to remove
                    calValue = (fobValue * inrate) / insamt;
                    if (calValue < minCharge) {
                        lclQuoteAc.setRolledupCharges(new BigDecimal(minCharge));
                        lclQuoteAc.setRatePerUnitUom(RATE_UNIT_PER_UOM_MIN);
                    } else {
                        lclQuoteAc.setRolledupCharges(new BigDecimal(calValue).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                        lclQuoteAc.setRatePerUnitUom(RATE_UNIT_PER_UOM_WEIGHT);
                    }
                    importChargeList.add(lclQuoteAc);
                }
                billToPartyAuto = (CommonUtils.isNotEmpty(billingType) && "P".equalsIgnoreCase(billingType)) ? "A" : billToParty;
                lclQuoteAc.setArAmount(lclQuoteAc.getRolledupCharges());
                lclQuoteAc.setRolledupCharges(lclQuoteAc.getArAmount());
                lclQuoteAc.setAdjustmentAmount(BigDecimal.ZERO);
                lclQuoteAc.setArBillToParty(billToPartyAuto);
                lclQuoteAc.setPrintOnBl(true);
                lclQuoteAc.setBundleIntoOf(false);
                lclQuoteAc.setSupAcct(null);
                lclQuoteAc.setEnteredBy(user);
                lclQuoteAc.setModifiedBy(user);
                lclQuoteAc.setEnteredDatetime(now);
                lclQuoteAc.setModifiedDatetime(now);
                lclQuoteAc.setTransDatetime(now);
                if (fileId != null && fileId > 0) {
                    lclQuoteAc.setLclFileNumber(new LclFileNumber(fileId));
                    lclQuoteAc.setLclQuotePiece(lclQuotePiece);
                    if (!CHARGE_CODE_CAF.equalsIgnoreCase(lclQuoteAc.getArglMapping().getChargeCode())) {
                        totalAmount += lclQuoteAc.getArAmount().doubleValue();
                        lclQuoteAcDAO.saveOrUpdate(lclQuoteAc);
                    } else {
                        cafQuoteAc = lclQuoteAc;
                    }
                }
            } else {
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

        if (cafQuoteAc != null) {
            Double cafAmt = calculateCAFAmount(cafQuoteAc, totalAmount);
            cafQuoteAc.setArAmount(new BigDecimal(cafAmt).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
            lclQuoteAcDAO.saveOrUpdate(cafQuoteAc);
        }
        return importChargeList;
    }

    public Double calculateCAFAmount(LclQuoteAc cafQuoteAc, Double totalAmount) throws Exception {
        Double finalValue = 0.0;
        if (cafQuoteAc.getRolledupCharges() != null) {
            finalValue = totalAmount * cafQuoteAc.getRolledupCharges().doubleValue();
        }
        if (cafQuoteAc.getTotalWeight() != null && cafQuoteAc.getTotalWeight().doubleValue() > finalValue) {
            finalValue = cafQuoteAc.getTotalWeight().doubleValue();
        }
        return finalValue;
    }
public void calculateHazMatRates(String pooCode, String polUnCode, String podUnCode, String fdUnCode, String transhipment, String billingType,
            String billToParty, Long fileId, List<LclQuotePiece> lclQuotePiecesList, HttpServletRequest request, User user) throws Exception {
        PortsDAO portsDAO = new PortsDAO();
        LCLImportRatesDAO lclImportRatesDAO = new LCLImportRatesDAO();
        List<String> chargeCodeList = new ArrayList<String>();//no need to retrive charges for these charge codes
        List<String> commodityList = new ArrayList<String>();
        String billingTerms[] = {"C", "P"};
        List<String> billingTermsList = Arrays.asList(billingTerms);
        List<LclImportsRatesBean> exceptionRatesList = new ArrayList();
        List<LclImportsRatesBean> importChargesList = new ArrayList();
        List<LclImportsRatesBean> ratesList =new ArrayList();
        List<LclImportsRatesBean> ipiChargeList = new ArrayList();
    
            for (LclQuotePiece lqp : lclQuotePiecesList) {
            if (lqp.getCommodityType() != null && lqp.getCommodityType().getCode() != null) {
                commodityList.add(lqp.getCommodityType().getCode());
            } else {
                commodityList.add(lqp.getCommNo());
            }
        }

        if (!chargeCodeList.contains("1625")) {
            chargeCodeList.add("1625");
        }
        if (!chargeCodeList.contains("1682")) {
            chargeCodeList.add("1682");
        }
        //auto Charges only deleted from DataBase
        String polSchnum = portsDAO.getShedulenumber(polUnCode);
        String podSchnum = portsDAO.getShedulenumber(podUnCode);
        String fdSchnum = portsDAO.getShedulenumber(fdUnCode);
        String pooSchnum = portsDAO.getShedulenumber(pooCode);
        exceptionRatesList = lclImportRatesDAO.getExceptionList(!pooSchnum.equalsIgnoreCase("") ? pooSchnum : "00000", polSchnum, podSchnum, fdSchnum, commodityList);
        if ("Y".equalsIgnoreCase(transhipment)) {
            podSchnum = fdSchnum;
        }
        importChargesList = lclImportRatesDAO.getLCLImportHazCharges(polSchnum, podSchnum, commodityList, chargeCodeList, billingTermsList);

        if (CommonUtils.isNotEqualIgnoreCase(podUnCode, fdUnCode) && "N".equalsIgnoreCase(transhipment)) {
            ipiChargeList = lclImportRatesDAO.getLCLImportIPICharges(podSchnum, fdSchnum, commodityList, "", null);// ipi ipiCharges
            for (LclImportsRatesBean ipiChargeLists : ipiChargeList) {
                if (ipiChargeLists.getChargeCode().equalsIgnoreCase("1682")) {
                    importChargesList.add(ipiChargeLists);
                }
            }
        }
        if (importChargesList.size() > 0) {
            ratesList.addAll(importChargesList);
        }
        this.calculateQuoteImportRate(ratesList, lclQuotePiecesList, billToParty, fileId, transhipment, user, exceptionRatesList, request);
    }
            }
