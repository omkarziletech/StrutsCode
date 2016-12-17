/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.lcl.dwr;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.logisoft.beans.LclImportsRatesBean;
import com.gp.cong.logisoft.domain.lcl.LclBookingAc;
import com.gp.cong.logisoft.domain.lcl.LclBookingPiece;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.GeneralInformationDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingAcTransDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLImportRatesDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.gp.cvst.logisoft.domain.GlMapping;
import com.gp.cvst.logisoft.struts.form.lcl.LCLBookingForm;
import com.logiware.accounting.dao.LclManifestDAO;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author lakshh
 */
public class LCLImportChargeCalc implements LclCommonConstant, ConstantsInterface {

    private List<LclBookingAc> importChargeList = null;
    private LclCostChargeDAO lclcostchargedao = new LclCostChargeDAO();
    private List<LclBookingAc> bookingAcList = new ArrayList();
    private PortsDAO portsDAO = new PortsDAO();

    public List<LclBookingAc> getBookingAcList() {
        return bookingAcList;
    }

    public void setBookingAcList(List<LclBookingAc> bookingAcList) {
        this.bookingAcList = bookingAcList;
    }

    public List<LclBookingAc> ImportRateCalculation(String originCode, String polUnCode,
            String podUnCode, String fdUnCode, String transhipment, String billingType,
            String billToParty, String vendorNo, String impCfsId, Long fileId, List<LclBookingPiece> lclBookingPiecesList,
            HttpServletRequest request, User user, String unitSsId) throws Exception {
        LCLImportRatesDAO lclImportRatesDAO = new LCLImportRatesDAO();
        LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
        LclManifestDAO lclManifestDAO = new LclManifestDAO();
        LclFileNumberDAO lclFileNumberDAO = new LclFileNumberDAO();
        LCLImportChargeCalc lclImportChargeCalc = new LCLImportChargeCalc();
        List<String> chargeCodeList = new ArrayList<String>();//no need to retrive charges for these charge codes
        List<String> commodityList = new ArrayList<String>();
        List<LclBookingAc> calculateImportChargeList = new ArrayList<LclBookingAc>();
        boolean hazmatFound = false;
        String ipiIgnoreStatus = "";
        Iterator ipiCost = null;
        List<LclBookingAc> calculatedIPICharge = null;
        String billingTerms[] = {"C", "P"};
        List<String> billingTermsList = Arrays.asList(billingTerms);
        List<LclImportsRatesBean> exceptionRatesList = new ArrayList();
        List<LclImportsRatesBean> importChargesList = new ArrayList();
        List<LclImportsRatesBean> ratesList = new ArrayList();
        String validateImpRates = "";
        for (LclBookingPiece lbp : lclBookingPiecesList) {
            if (lbp.getCommodityType() != null && lbp.getCommodityType().getCode() != null) {
                commodityList.add(lbp.getCommodityType().getCode());
                ipiIgnoreStatus = lclBookingPiecesList.get(lclBookingPiecesList.size() - 1).getCommodityType().getCode();
            } else {
                commodityList.add(lbp.getCommNo());
                ipiIgnoreStatus = lbp.getCommNo();
            }
            if (lbp.isHazmat()) {
                hazmatFound = true;
            }
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

        // IPI Ignore Logic
        if (CommonUtils.isNotEmpty(ipiIgnoreStatus)) {
            ipiIgnoreStatus = lclImportRatesDAO.checkNewIpiCost(ipiIgnoreStatus);
        }
        String polSchnum = portsDAO.getShedulenumber(polUnCode);//pol Schedule Number
        String podSchnum = portsDAO.getShedulenumber(podUnCode);
        String fdSchnum = portsDAO.getShedulenumber(fdUnCode);
        String pooSchnum = portsDAO.getShedulenumber(originCode);
        exceptionRatesList = lclImportRatesDAO.getExceptionList(!pooSchnum.equalsIgnoreCase("") ? pooSchnum : "00000", polSchnum, podSchnum, fdSchnum, commodityList);
        //auto Charges only deleted from DataBase
        LCLBookingAcTransDAO lclBookingAcTransDAO = new LCLBookingAcTransDAO();
        if (fileId != null && fileId > 0) {
            BigDecimal zeroValues = new BigDecimal(0.00);
            String bookingAcIds = lclcostchargedao.getBkgAcIdsWithoutApStatus(fileId);
            String lclBookingAcTransIds = new LCLBookingAcTransDAO().getConcatenatedBookingAcTransIds(bookingAcIds);
            lclcostchargedao.updateApAmount(fileId, zeroValues, zeroValues, false, true, false, LCL_IMPORT);
            lclcostchargedao.deleteArAmount(fileId, LCL_IMPORT);
            lclRemarksDAO.insertLclRemarks(fileId, REMARKS_DR_AUTO_NOTES, "AutoRates Recalculated", user.getUserId());
            if (CommonUtils.isNotEmpty(bookingAcIds)) {
                lclcostchargedao.deleteChargesById(bookingAcIds);
                lclManifestDAO.deleteLclAccrualsByIds(bookingAcIds, lclFileNumberDAO.getFileNumberByFileId(fileId.toString()));
                lclBookingAcTransDAO.deleteLclBookingAcTransByBkgAcId(lclBookingAcTransIds);
            }
        }

        //PREPAID OR COLLECT CHARGES
        if ("Y".equalsIgnoreCase(transhipment)) {
            podSchnum = fdSchnum;
        }
        if ("Y".equalsIgnoreCase(ipiIgnoreStatus) && "N".equalsIgnoreCase(transhipment) && CommonUtils.isNotEqualIgnoreCase(podUnCode, fdUnCode)) {
//            Iterator ignoreIpiImportCharges;
            ratesList.addAll(lclImportRatesDAO.ignoreIpiRatesTrigger(polSchnum, fdSchnum, commodityList, chargeCodeList, billingTermsList));// To added ignoreIpiCharges
//            List<LclBookingAc> chargeList = lclImportChargeCalc.calculateImportRate(ignoreIpiImportCharges, lclBookingPiecesList, billToParty, fileId, user, null, transhipment, exceptionRatesList, request);
//            calculateImportChargeList.addAll(chargeList);
        } else {
            //calculate CFS Devanning Warehouse Rates
            boolean warhouseFlag = false;
            boolean includeConsigneeFlag = false;
            if (CommonUtils.isNotEmpty(unitSsId) && "N".equalsIgnoreCase(transhipment)
                    && CommonUtils.isNotEmpty(podUnCode) && podUnCode.equalsIgnoreCase(fdUnCode)) {
                Object[] billingTypes = new LclUnitSsDAO().getBillingTypeByUnit(unitSsId);
                String commodity = commodityList.get(0);
                String[] agentCommodity = new GeneralInformationDAO().getCommodity(vendorNo, "Agent", "");
                String retailCommodity = lclImportRatesDAO.getRetailCommodity(vendorNo);
                if ((null != agentCommodity && CommonUtils.isNotEmpty(agentCommodity[0]))
                        && agentCommodity[0].equalsIgnoreCase(commodity)
                        || (CommonUtils.isNotEmpty(retailCommodity) && retailCommodity.equalsIgnoreCase(commodity))) {
                    warhouseFlag = true;
                } else {
                    includeConsigneeFlag = true;
                    //  warhouseFlag = true;
                }

                if (null != billingTypes && billingTypes.length > 0 && "C".equalsIgnoreCase(billingTypes[0].toString())
                        && billingTypes[1] != null && !billingTypes[1].toString().trim().equals("") && warhouseFlag) {
                    List<String> ipiCommList = new ArrayList<String>();
                    ipiCommList.add(billingTypes[1].toString());
                    List<String> billingWarehsTermsList = new ArrayList<String>();
                    billingWarehsTermsList.add("W");
                    List warehouseChargesList = lclImportRatesDAO.getLCLImportCharges(polSchnum, podSchnum, ipiCommList, chargeCodeList, billingWarehsTermsList); // To added warehouseCharges
                    if (warehouseChargesList.size() > 0) {
                        ratesList.addAll(warehouseChargesList);
                        warhouseFlag = false;
                        if (includeConsigneeFlag) {
                            warhouseFlag = true;

                        }
                    }
//                    List<LclBookingAc> warehouseList = lclImportChargeCalc.calculateImportRate(warehouseCharges,
//                            lclBookingPiecesList, "W", fileId, user, null, transhipment, exceptionRatesList, request);
                } else {
                    warhouseFlag = true;
                }
            } else {
                warhouseFlag = true;
            }
            if (warhouseFlag) {
//                Iterator importCharges = null;
                //calculate rates from Origin to POD
                if (CommonUtils.isNotEmpty(originCode)) {
                    if (!originCode.equalsIgnoreCase(polUnCode)) {
                        if (CommonUtils.isNotEmpty(unitSsId)) {
                            importChargesList = lclImportRatesDAO.getLCLImportOceanFreightRate(pooSchnum, podSchnum, commodityList, billingTermsList, unitSsId);
                            if (importChargesList.size() > 0) {
                                ratesList.addAll(importChargesList);
                                chargeCodeList.add("0080");
                            }
                        }
                        importChargesList = lclImportRatesDAO.getLCLImportCharges(pooSchnum, podSchnum, commodityList, chargeCodeList, billingTermsList);
                        ratesList.addAll(importChargesList);
                        if (importChargesList.isEmpty()) {
                            validateImpRates = "No rates existing using the absolute origin city. <br>";
                        }
                    } else {
                        if (CommonUtils.isNotEmpty(unitSsId)) {
                            importChargesList = lclImportRatesDAO.getLCLImportOceanFreightRate(polSchnum, podSchnum, commodityList, billingTermsList, unitSsId);
                            if (importChargesList.size() > 0) {
                                ratesList.addAll(importChargesList);
                                chargeCodeList.add("0080");
                            }
                        }
                        ratesList.addAll(lclImportRatesDAO.getLCLImportCharges(polSchnum, podSchnum, commodityList, chargeCodeList, billingTermsList));
                    }
                } // otherwise , calculate rates from POL to POD
                else {
                    if (CommonUtils.isNotEmpty(unitSsId)) {
                        importChargesList = lclImportRatesDAO.getLCLImportOceanFreightRate(polSchnum, podSchnum, commodityList, billingTermsList, unitSsId);
                        if (importChargesList.size() > 0) {
                            ratesList.addAll(importChargesList);
                            chargeCodeList.add("0080");
                        } 
                    }
                    ratesList.addAll(lclImportRatesDAO.getLCLImportCharges(polSchnum, podSchnum, commodityList, chargeCodeList, billingTermsList));
                }
//                List<LclBookingAc> chargeList = lclImportChargeCalc.calculateImportRate(importCharges, lclBookingPiecesList, billToParty, fileId, user, null, transhipment, exceptionRatesList, request);
//                calculateImportChargeList.addAll(chargeList);
            }

            //3Leg charges
            if ("N".equalsIgnoreCase(transhipment) && CommonUtils.isNotEmpty(podUnCode) && podUnCode.equalsIgnoreCase(fdUnCode)) {
                ratesList.addAll(lclImportRatesDAO.getLCLImportCharges(podSchnum, podSchnum, commodityList, chargeCodeList, billingTermsList));
//                List<LclBookingAc> _3legChargeList = lclImportChargeCalc.calculateImportRate(_3legCharges, lclBookingPiecesList, billToParty, fileId, user, null, transhipment, exceptionRatesList, request);
//                calculateImportChargeList.addAll(_3legChargeList);
            }
        }

        //IPI CHARGES
        LclBookingAc ipiCharge = null;
        LclBookingAc inbCharge = null;
        if (fileId != null && fileId > 0) {
            ipiCharge = lclcostchargedao.getFirstLclCostByBluescreenChargeCode(fileId, "1607");
            inbCharge = lclcostchargedao.getFirstLclCostByBluescreenChargeCode(fileId, "1617");
        }
        if (ipiCharge == null || inbCharge == null) {
            if (CommonUtils.isNotEqualIgnoreCase(podUnCode, fdUnCode) && "N".equalsIgnoreCase(transhipment)) {
                importChargesList = lclImportRatesDAO.getLCLImportIPICharges(podSchnum, fdSchnum, commodityList, ipiIgnoreStatus, chargeCodeList);// ipi ipiCharges
                if (null != impCfsId && !impCfsId.equals("")) {
                    List<String> costeCodes = new ArrayList<String>();
                    costeCodes.add("1607");
                    if (hazmatFound) {
                        costeCodes.add("1682");
                    }
                    ipiCost = lclImportRatesDAO.getLclImpIPICost(podSchnum, fdSchnum, impCfsId, costeCodes);
                }
                if (importChargesList.size() > 0) {
                    ratesList.addAll(importChargesList);
//                    calculatedIPICharge = lclImportChargeCalc.calculateImportRate(importChargesList, lclBookingPiecesList, billToParty, fileId, user, ipiCost, transhipment, exceptionRatesList, request);
//                    calculateImportChargeList.addAll(calculatedIPICharge);
                } else if ("Y".equalsIgnoreCase(ipiIgnoreStatus)) {
                    if (ipiCost != null && ipiCost.hasNext()) {
                        calculatedIPICharge = lclImportChargeCalc.getNewipiCost(lclBookingPiecesList, "A", fileId, user, ipiCost, transhipment);
                        calculateImportChargeList.addAll(calculatedIPICharge);
                        ipiCost = null;
                    }
                }
            }
        }
        if (ratesList.size() > 0) {
            calculateImportChargeList.addAll(lclImportChargeCalc.calculateImportRate(ratesList, lclBookingPiecesList, billToParty, fileId, user, ipiCost, transhipment, exceptionRatesList, request));
        }
        bookingAcList.addAll(calculateImportChargeList);
        if ("Y".equalsIgnoreCase(ipiIgnoreStatus) && bookingAcList.isEmpty()) {
            validateImpRates = "No rates are setup for this final destination. Please contact importpricing@econocaribe.com.";
        }
        request.setAttribute("validateImpRates", validateImpRates);
        request.setAttribute("oldFdUnlocationCode", fdUnCode);
        return calculateImportChargeList;
    }

    public void calculate3LegSetUp(String billingType, String billToParty, Long fileId, List<LclBookingPiece> lclBookingPiecesList, User user,
            String pooSchnum, String polSchnum, String podSchnum, String fdSchnum, HttpServletRequest request) throws Exception {
        List<String> commodityList = new ArrayList<String>();
        for (LclBookingPiece lbp : lclBookingPiecesList) {
            if (lbp.getCommodityType() != null && lbp.getCommodityType().getCode() != null) {
                commodityList.add(lbp.getCommodityType().getCode());
            } else {
                commodityList.add(lbp.getCommNo());
            }
        }
        LCLImportRatesDAO lclImportRatesDAO = new LCLImportRatesDAO();
        LCLImportChargeCalc lclImportChargeCalc = new LCLImportChargeCalc();
        String billingTerms[] = {billingType};
        List<String> billingTermsList = Arrays.asList(billingTerms);
        List<LclImportsRatesBean> exceptionRatesList = new LCLImportRatesDAO().getExceptionList(!pooSchnum.equalsIgnoreCase("") ? pooSchnum : "00000", polSchnum, podSchnum, fdSchnum, commodityList);
        List<LclImportsRatesBean> importChargesList = lclImportRatesDAO.getLCLImportCharges(podSchnum, podSchnum, commodityList, null, billingTermsList);
        lclImportChargeCalc.calculateImportRate(importChargesList, lclBookingPiecesList, billToParty, fileId, user, null, "N", exceptionRatesList, request);
    }

    public List<LclBookingAc> calculatediffPodFdRates(String polUnCode, String podUnCode, String fdUnCode, String transhipment, String billingType,
            String billToParty, String vendorNo, String impCfsId, Long fileId, List<LclBookingPiece> lclBookingPiecesList, HttpServletRequest request, User user, String originUnCode) throws Exception {
        LCLImportRatesDAO lclImportRatesDAO = new LCLImportRatesDAO();
        LCLImportChargeCalc lclImportChargeCalc = new LCLImportChargeCalc();
        List<String> commodityList = new ArrayList<String>();
        List<LclBookingAc> calculateImportChargeList = null;
        List<String> chargeCodeList = new ArrayList<String>();
        boolean hazmatFound = false;
        String ipiIgnoreStatus = "";
        String fileNumberStatus = "";
        for (LclBookingPiece lbp : lclBookingPiecesList) {
            if (lbp.getCommodityType() != null && lbp.getCommodityType().getCode() != null) {
                commodityList.add(lbp.getCommodityType().getCode());
                ipiIgnoreStatus = lclBookingPiecesList.get(lclBookingPiecesList.size() - 1).getCommodityType().getCode();
            } else {
                commodityList.add(lbp.getCommNo());
                ipiIgnoreStatus = lbp.getCommNo();
            }
            if (lbp.isHazmat()) {
                hazmatFound = true;
            }
        }
        if (!hazmatFound) {
            if (!chargeCodeList.contains("1625")) {
                chargeCodeList.add("1625");
            }
            if (!chargeCodeList.contains("1682")) {
                chargeCodeList.add("1682");
            }
        }
        //auto Charges only deleted from DataBase
        String poo5Digit = portsDAO.getShedulenumber(originUnCode);
        String pol5Digit = portsDAO.getShedulenumber(polUnCode);
        String pod5Digit = portsDAO.getShedulenumber(podUnCode);
        String fd5Digit = portsDAO.getShedulenumber(fdUnCode);
        List<LclImportsRatesBean> exceptionRatesList = new LCLImportRatesDAO().getExceptionList(!poo5Digit.equalsIgnoreCase("") ? poo5Digit : "00000", pol5Digit, pod5Digit, fd5Digit, commodityList);
        if (fileId != null && fileId > 0) {
            LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
            fileNumberStatus = (String) new LclFileNumberDAO().getStatusByField("id", String.valueOf(fileId));
            if (!"M".equalsIgnoreCase(fileNumberStatus)) {
                lclCostChargeDAO.deleteWarehouseCharges(fileId, "W", false);
            }
            //3Leg delete Charges
            if (null != commodityList && !commodityList.isEmpty()) {
                String _3LegBlueChargeCode = lclImportRatesDAO.getRatesChargeCode(pod5Digit, pod5Digit, commodityList);
                if (CommonUtils.isNotEmpty(_3LegBlueChargeCode)) {
                    List<String> _3LegChargeCodeList = new ArrayList<String>();
                    for (String _3LegCharges : _3LegBlueChargeCode.split(",")) {
                        _3LegChargeCodeList.addAll(Arrays.asList(_3LegCharges));
                    }
                    List<LclBookingAc> _3LegChargeList = lclCostChargeDAO.getRatesByBlueScreenCode(fileId, Boolean.FALSE, _3LegChargeCodeList);
                    if (null != _3LegChargeList && !_3LegChargeList.isEmpty()) {
                        LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
                        for (LclBookingAc _3leg : _3LegChargeList) {
                            String deleteCharges = "Deleted--> Code->" + _3leg.getArglMapping().getChargeCode() + " Charge Amount->" + _3leg.getArAmount();
                            lclRemarksDAO.insertLclRemarks(fileId, REMARKS_DR_AUTO_NOTES, deleteCharges, user.getUserId());
                            lclCostChargeDAO.delete(_3leg);
                        }
                    }
                }
            }
        }

        if (CommonUtils.isNotEmpty(ipiIgnoreStatus)) {
            ipiIgnoreStatus = lclImportRatesDAO.checkNewIpiCost(ipiIgnoreStatus);
        }
        //IPI CHARGES
        if ("N".equalsIgnoreCase(transhipment)) {
            List<LclImportsRatesBean> ipiCharges = lclImportRatesDAO.getLCLImportIPICharges(pod5Digit, fd5Digit, commodityList, ipiIgnoreStatus, chargeCodeList);
            List<LclImportsRatesBean> ipiIgnoreCollect = lclImportRatesDAO.getLCLImportIPICharges(pod5Digit, fd5Digit, commodityList, ipiIgnoreStatus, chargeCodeList);
            Iterator ipiCost = null;
            if (null != impCfsId && !impCfsId.equals("")) {
                List<String> costeCodes = new ArrayList<String>();
                costeCodes.add("1607");
                if (hazmatFound) {
                    costeCodes.add("1682");
                }
                ipiCost = lclImportRatesDAO.getLclImpIPICost(pod5Digit, fd5Digit, impCfsId, costeCodes);
            }
            if (ipiCharges.size() > 0) {
                LclImportsRatesBean chargeBean = (LclImportsRatesBean) ipiIgnoreCollect.get(0);
                if ((null != chargeBean.getBillingType() && "C".equalsIgnoreCase(chargeBean.getBillingType())) && ("M".equalsIgnoreCase(fileNumberStatus))) {
                    request.setAttribute("newIpiChargeStatus", "IPI charge must be added via Correction notice since DR is Posted.");
                } else {
                    List<LclBookingAc> calculatedIPICharge = lclImportChargeCalc.calculateImportRate(ipiCharges, lclBookingPiecesList, billToParty, fileId, user, ipiCost, transhipment, exceptionRatesList, request);
                    bookingAcList.addAll(calculatedIPICharge);
                }
            } else if (("N".equalsIgnoreCase(transhipment) && "Y".equalsIgnoreCase(ipiIgnoreStatus))) {
                if (null != ipiCost && ipiCost.hasNext()) {
                    List<LclBookingAc> calculatedIPICharge = lclImportChargeCalc.getNewipiCost(lclBookingPiecesList, "A", fileId, user, ipiCost, transhipment);
                    bookingAcList.addAll(calculatedIPICharge);
                }
            }
        }
        if ("Y".equalsIgnoreCase(ipiIgnoreStatus) && bookingAcList.isEmpty()) {
            request.setAttribute("validateImpRates", "No rates are setup for this final destination. Please contact importpricing@econocaribe.com.");
        }
        return calculateImportChargeList;
    }

    public List<LclBookingAc> calculateImportRate(List<LclImportsRatesBean> ratesList, List<LclBookingPiece> commodityList,
            String billToParty, Long fileId, User user, Iterator ipiCost, String transhipment, List<LclImportsRatesBean> exceptionRatesList, HttpServletRequest request) throws Exception {
        List copy = new ArrayList();
        while (ipiCost != null && ipiCost.hasNext()) {
            copy.add(ipiCost.next());
        }
        LclFileNumber lclFileNumber = null;
        importChargeList = new ArrayList();
        Double totalMeasureImp = 0.00;
        Double totalWeightImp = 0.00;
        Double totalMeasureMet = 0.00;
        Double totalWeightMet = 0.00;
        Double totalAmount = 0.00;
        String billingType = "";
        BigDecimal measureDiv = new BigDecimal(1000);
        BigDecimal weightDiv = new BigDecimal(100);
        LclBookingPiece lclBookingPiece = null;
        LclBookingAc cafBookingAc = null;
        LclFileNumberDAO lclFileNumberDAO = new LclFileNumberDAO();
        GlMappingDAO glMappingDAO = new GlMappingDAO();
        Date now = new Date();
        String shipmentType = LCL_SHIPMENT_TYPE_IMPORT;
//        if ("Y".equalsIgnoreCase(transhipment)) {
//            shipmentType = LCL_SHIPMENT_TYPE_EXPORT;
//        }
        String blueGLchargeCode = "";
        List<LclImportsRatesBean> importCharge = new LCLImportRatesDAO().filterOriginalRatesList(ratesList, exceptionRatesList, transhipment);
        for (LclBookingPiece lbp : commodityList) {
            lclBookingPiece = lbp;
            LCLBookingForm bkForm = (LCLBookingForm) request.getAttribute("lclBookingForm");
            String moduleName = null != bkForm ? bkForm.getModuleName()
                    : "Y".equalsIgnoreCase(transhipment) ? "Exports" : "Imports";
            if ("Y".equalsIgnoreCase(transhipment) && moduleName.equalsIgnoreCase("Exports")) {
                totalMeasureImp += (!CommonUtils.isEmpty(lbp.getActualVolumeImperial())
                        ? lbp.getActualVolumeImperial() : !CommonUtils.isEmpty(lbp.getBookedVolumeImperial())
                                ? lbp.getBookedVolumeImperial() : BigDecimal.ZERO).doubleValue();
                totalWeightImp += (!CommonUtils.isEmpty(lbp.getActualWeightImperial())
                        ? lbp.getActualWeightImperial() : !CommonUtils.isEmpty(lbp.getBookedWeightImperial())
                                ? lbp.getBookedWeightImperial() : BigDecimal.ZERO).doubleValue();
                totalMeasureMet += (!CommonUtils.isEmpty(lbp.getActualVolumeMetric())
                        ? lbp.getActualVolumeMetric() : !CommonUtils.isEmpty(lbp.getBookedVolumeMetric())
                                ? lbp.getBookedVolumeMetric() : BigDecimal.ZERO).doubleValue();
                totalWeightMet += (!CommonUtils.isEmpty(lbp.getActualWeightMetric())
                        ? lbp.getActualWeightMetric() : !CommonUtils.isEmpty(lbp.getBookedWeightMetric())
                                ? lbp.getBookedWeightMetric() : BigDecimal.ZERO).doubleValue();
            } else {
                if (lbp.getBookedVolumeImperial() != null && lbp.getBookedVolumeImperial().doubleValue() != 0.00) {
                    totalMeasureImp += lbp.getBookedVolumeImperial().doubleValue();//cft
                }
                if (lbp.getBookedWeightImperial() != null && lbp.getBookedWeightImperial().doubleValue() != 0.00) {
                    totalWeightImp += lbp.getBookedWeightImperial().doubleValue();//LBS
                }
                if (lbp.getBookedVolumeMetric() != null && lbp.getBookedVolumeMetric().doubleValue() != 0.00) {
                    totalMeasureMet += lbp.getBookedVolumeMetric().doubleValue();//CBM
                }
                if (lbp.getBookedWeightMetric() != null && lbp.getBookedWeightMetric().doubleValue() != 0.00) {
                    totalWeightMet += lbp.getBookedWeightMetric().doubleValue();//KGS
                }
            }
        }
        for (LclImportsRatesBean chargeBean : importCharge) {
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
                LclBookingAc apCost = fileId != null && fileId > 0 ? new LclCostChargeDAO().findByBlueScreenChargeCode(fileId, chargeCode, false) : null;
                LclBookingAc lclBookingAc = apCost == null ? new LclBookingAc() : apCost;
                String costStatus = new LCLBookingAcTransDAO().getTransType(lclBookingAc.getId());
                lclBookingAc.setArglMapping(arglMapping);
                if ("1".equalsIgnoreCase(chargeType)) {
                    if (CommonUtils.isEmpty(lclBookingAc.getSpReferenceNo())) {
                        lclBookingAc.setRolledupCharges(new BigDecimal(flatrt).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                    } else {
                        lclBookingAc.setRolledupCharges(lclBookingAc.getArAmount());
                    }
                    lclBookingAc.setRatePerUnitUom(RATE_UNIT_PER_UOM_FL);
                    if (!copy.isEmpty()) {
                        for (Object costs : copy) {
                            Object[] cost = (Object[]) costs;
                            if ("1682".equalsIgnoreCase(cost[0].toString())
                                    && "1682".equalsIgnoreCase(lclBookingAc.getArglMapping().getBlueScreenChargeCode())) {
                                Double flatrate = 0.0;
                                String costVendor = "";

                                if (cost[6] != null) {
                                    costVendor = String.valueOf(cost[6]);
                                }
                                if (cost[11] != null) {
                                    flatrate = Double.parseDouble(String.valueOf(cost[11]));
                                }
                                if (CommonUtils.notIn(costStatus, "AP", "IP")) {
                                    lclBookingAc.setApAmount(new BigDecimal(flatrate).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                                    lclBookingAc.setSupAcct(new TradingPartnerDAO().findById(costVendor));
                                    lclBookingAc.setDeleted(false);
                                }
                                lclBookingAc.setApglMapping(glMappingDAO.findByChargeCode(CHARGE_CODE_IPI_HAZ, LCL_SHIPMENT_TYPE_IMPORT, TRANSACTION_TYPE_ACCRUALS));
                            }
                        }
                    }
                    importChargeList.add(lclBookingAc);
                }
                if ("2".equalsIgnoreCase(chargeType)) {
                    if (CommonUtils.isEmpty(lclBookingAc.getSpReferenceNo())) {
                        BigDecimal blpctValue = new BigDecimal(blpct).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP);
                        lclBookingAc.setRolledupCharges(blpctValue);//Blpcpercentage
                        lclBookingAc.setRatePerUnit(blpctValue);
                    } else {
                        lclBookingAc.setRolledupCharges(lclBookingAc.getArAmount());
                    }
                    lclBookingAc.setTotalWeight(new BigDecimal(minCharge));//Min
                    lclBookingAc.setRatePerUnitUom(RATE_UNIT_PER_UOM_PCT);
                    importChargeList.add(lclBookingAc);
                }
                if ("3".equalsIgnoreCase(chargeType)) {
                    Double cbmCalValue = 0.0;
                    Double kgsCalValue = 0.0;
                    lclBookingAc.setRateFlatMinimum(new BigDecimal(minCharge));
                    lclBookingAc.setRateFlatMaximum(new BigDecimal(maximumCharge).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                    if (CommonUtils.isEmpty(lclBookingAc.getSpReferenceNo())) {
                        if (cw$ != 0.0 || cf$ != 0.0) {
                            cbmCalValue = cf$ * totalMeasureImp;
                            kgsCalValue = (cw$ * totalWeightImp) / 100;
                            if (maximumCharge != 0.00 && (cbmCalValue >= maximumCharge || kgsCalValue >= maximumCharge)) {
                                lclBookingAc.setRolledupCharges(new BigDecimal(maximumCharge).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                                lclBookingAc.setRatePerUnitUom(RATE_UNIT_PER_UOM_MAX);
                            } else if (cbmCalValue <= minCharge && kgsCalValue <= minCharge) {
                                lclBookingAc.setRolledupCharges(new BigDecimal(minCharge).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                                lclBookingAc.setRatePerUnitUom(RATE_UNIT_PER_UOM_MIN);
                            } else if (cbmCalValue <= kgsCalValue) {
                                lclBookingAc.setRolledupCharges(new BigDecimal(kgsCalValue).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                                lclBookingAc.setRatePerUnitUom(RATE_UNIT_PER_UOM_WEIGHT);
                            } else if (cbmCalValue >= kgsCalValue) {
                                lclBookingAc.setRolledupCharges(new BigDecimal(cbmCalValue).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                                lclBookingAc.setRatePerUnitUom(RATE_UNIT_PER_UOM_VOLUME);
                            }
                            lclBookingAc.setRateUom(RATE_UOM_I);
                            lclBookingAc.setRatePerWeightUnit(new BigDecimal(cw$));
                            lclBookingAc.setRatePerWeightUnitDiv(weightDiv);
                            lclBookingAc.setRatePerVolumeUnit(new BigDecimal(cf$));
                            lclBookingAc.setRatePerVolumeUnitDiv(measureDiv);
                        } else if (cbmrt != 0.0 || kgsrt != 0.0) {
                            cbmCalValue = cbmrt * totalMeasureMet;
                            kgsCalValue = (kgsrt * totalWeightMet) / 1000;
                            if (maximumCharge != 0.00 && (cbmCalValue >= maximumCharge || kgsCalValue >= maximumCharge)) {
                                lclBookingAc.setRolledupCharges(new BigDecimal(maximumCharge).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                                lclBookingAc.setRatePerUnitUom(RATE_UNIT_PER_UOM_MAX);
                            } else if (cbmCalValue <= minCharge && kgsCalValue <= minCharge) {
                                lclBookingAc.setRolledupCharges(new BigDecimal(minCharge).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                                lclBookingAc.setRatePerUnitUom(RATE_UNIT_PER_UOM_MIN);
                            } else if (cbmCalValue <= kgsCalValue) {
                                lclBookingAc.setRolledupCharges(new BigDecimal(kgsCalValue).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                                lclBookingAc.setRatePerUnitUom(RATE_UNIT_PER_UOM_WEIGHT);
                            } else if (cbmCalValue >= kgsCalValue) {
                                lclBookingAc.setRolledupCharges(new BigDecimal(cbmCalValue).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                                lclBookingAc.setRatePerUnitUom(RATE_UNIT_PER_UOM_VOLUME);
                            }
                            lclBookingAc.setRatePerWeightUnit(new BigDecimal(kgsrt));
                            lclBookingAc.setRatePerWeightUnitDiv(weightDiv);
                            //cost

                            if (!copy.isEmpty()) {
                                for (Object costs : copy) {
                                    Object[] cost = (Object[]) costs;
                                    if (("1607".equalsIgnoreCase(cost[0].toString())
                                            && "1607".equalsIgnoreCase(lclBookingAc.getArglMapping().getBlueScreenChargeCode()))
                                            || ("1682".equalsIgnoreCase(cost[0].toString())
                                            && "1682".equalsIgnoreCase(lclBookingAc.getArglMapping().getBlueScreenChargeCode()))) {

                                        Double minCost = 0.0;
                                        Double lbsCost = 0.0;
                                        Double cbmCost = 0.0;
                                        Double kgsCost = 0.0;
                                        Double cftCost = 0.0;
                                        Double cbmCostVal = 0.0;
                                        Double kgsCostVal = 0.0;
                                        String costVendor = "";
                                        if (cost[1] != null) {
                                            minCost = Double.parseDouble(String.valueOf(cost[1]));
                                        }
                                        if (cost[2] != null) {
                                            lbsCost = Double.parseDouble(String.valueOf(cost[2]));
                                        }
                                        if (cost[3] != null) {
                                            cftCost = Double.parseDouble(String.valueOf(cost[3]));
                                        }
                                        if (cost[4] != null) {
                                            cbmCost = Double.parseDouble(String.valueOf(cost[4]));
                                        }
                                        if (cost[5] != null) {
                                            kgsCost = Double.parseDouble(String.valueOf(cost[5]));
                                        }
                                        if (cost[6] != null) {
                                            costVendor = String.valueOf(cost[6]);
                                        }
                                        if (lbsCost != 0.0 || cftCost != 0.0) {
                                            if (CommonUtils.notIn(costStatus, "AP", "IP")) {
                                                cbmCostVal = cftCost * totalMeasureImp;
                                                kgsCostVal = (lbsCost * totalWeightImp) / 100;
                                                if (cbmCostVal <= minCost && kgsCostVal <= minCost) {
                                                    lclBookingAc.setApAmount(new BigDecimal(minCost).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                                                } else if (cbmCostVal <= kgsCostVal) {
                                                    lclBookingAc.setApAmount(new BigDecimal(kgsCostVal).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                                                } else if (cbmCostVal >= kgsCostVal) {
                                                    lclBookingAc.setApAmount(new BigDecimal(cbmCostVal).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                                                }
                                                lclBookingAc.setSupAcct(new TradingPartnerDAO().findById(costVendor));
                                                lclBookingAc.setDeleted(false);
                                            }
                                        } else if (cbmCost != 0.0 || kgsCost != 0.0) {
                                            if (CommonUtils.notIn(costStatus, "AP", "IP")) {
                                                cbmCostVal = cbmCost * totalMeasureMet;
                                                kgsCostVal = (kgsCost * totalWeightMet) / 1000;
                                                if (cbmCostVal <= minCharge && kgsCostVal <= minCost) {
                                                    lclBookingAc.setApAmount(new BigDecimal(minCost).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                                                } else if (cbmCostVal <= kgsCostVal) {
                                                    lclBookingAc.setApAmount(new BigDecimal(kgsCostVal).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                                                } else if (cbmCostVal >= kgsCostVal) {
                                                    lclBookingAc.setApAmount(new BigDecimal(cbmCostVal).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                                                }
                                                lclBookingAc.setSupAcct(new TradingPartnerDAO().findById(costVendor));
                                                lclBookingAc.setDeleted(false);
                                            }
                                        }
                                        if (lclBookingAc.getArglMapping().getChargeCode().equalsIgnoreCase(CHARGE_CODE_IPI)) {
                                            lclBookingAc.setApglMapping(glMappingDAO.findByChargeCode(CHARGE_CODE_IPI, LCL_SHIPMENT_TYPE_IMPORT, TRANSACTION_TYPE_ACCRUALS));
                                        } else if (lclBookingAc.getArglMapping().getChargeCode().equalsIgnoreCase(CHARGE_CODE_IPI_HAZ)) {
                                            lclBookingAc.setApglMapping(glMappingDAO.findByChargeCode(CHARGE_CODE_IPI_HAZ, LCL_SHIPMENT_TYPE_IMPORT, TRANSACTION_TYPE_ACCRUALS));
                                        }
                                    }
                                }
                            }

                            lclBookingAc.setRatePerVolumeUnit(new BigDecimal(cbmrt));
                            lclBookingAc.setRatePerVolumeUnitDiv(measureDiv);
                            lclBookingAc.setRateUom(RATE_UOM_M);
                        }
                    } else {
                        lclBookingAc.setRolledupCharges(lclBookingAc.getArAmount());
                    }
                    lclBookingAc.setCostFlatrateAmount(lclBookingAc.getApAmount());
                    importChargeList.add(lclBookingAc);
                }
                if ("4".equalsIgnoreCase(chargeType)) {
                    Double calValue = 0.0;
                    Double fobValue = 1.00;//need to remove
                    calValue = (fobValue * inrate) / insamt;
                    if (CommonUtils.isEmpty(lclBookingAc.getSpReferenceNo())) {
                        if (calValue < minCharge) {
                            lclBookingAc.setRolledupCharges(new BigDecimal(minCharge));
                            lclBookingAc.setRatePerUnitUom(RATE_UNIT_PER_UOM_MIN);
                        } else {
                            lclBookingAc.setRolledupCharges(new BigDecimal(calValue).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                            lclBookingAc.setRatePerUnitUom(RATE_UNIT_PER_UOM_WEIGHT);
                        }
                    } else {
                        lclBookingAc.setRolledupCharges(lclBookingAc.getArAmount());
                    }
                    importChargeList.add(lclBookingAc);
                }
                lclBookingAc.setArAmount(null != lclBookingAc.getRolledupCharges() ? lclBookingAc.getRolledupCharges() : new BigDecimal(0.00));
                lclBookingAc.setRolledupCharges(lclBookingAc.getArAmount());
                lclBookingAc.setAdjustmentAmount(BigDecimal.ZERO);
                billToPartyAuto = (CommonUtils.isNotEmpty(billingType) && "W".equalsIgnoreCase(billingType)) ? billingType : "P".equalsIgnoreCase(billingType) ? "A" : billToParty;
                if ("W".equalsIgnoreCase(billToPartyAuto)) {
                    lclBookingAc.setPostAr(false);
                }
                if (CommonUtils.isEmpty(lclBookingAc.getArBillToParty()) || CommonUtils.isEmpty(lclBookingAc.getSpReferenceNo())) {
                    lclBookingAc.setArBillToParty(billToPartyAuto);
                }
                if (lclBookingAc.getArglMapping().getChargeCode().equalsIgnoreCase("IPI") && "A".equalsIgnoreCase(billToPartyAuto)) {
                    lclBookingAc.setRelsToInv(true);
                } else {
                    lclBookingAc.setRelsToInv(lclBookingAc.getArBillToParty().equalsIgnoreCase("A") && CommonUtils.isNotEmpty(lclBookingAc.getSpReferenceNo()) ? true : false);
                }
                lclBookingAc.setPrintOnBl(true);
                lclBookingAc.setBundleIntoOf(false);
                if (lclBookingPiece != null) {
                    lclBookingAc.setLclBookingPiece(lclBookingPiece);
                }
                lclBookingAc.setEnteredBy(user);
                lclBookingAc.setModifiedBy(user);
                lclBookingAc.setEnteredDatetime(now);
                lclBookingAc.setModifiedDatetime(now);
                lclBookingAc.setTransDatetime(now);
                if (fileId != null && fileId > 0) {
                    if (null == lclFileNumber) {
                        lclFileNumber = lclFileNumberDAO.findById(fileId);
                    }
                    lclBookingAc.setLclFileNumber(lclFileNumber);
                    lclBookingAc.setControlNo(lclBookingAc.getControlNo());
                    if (lclBookingAc.getApAmount() == null) {
                        lclBookingAc.setApAmount(BigDecimal.ZERO);
                    }
                    if (!CHARGE_CODE_CAF.equalsIgnoreCase(lclBookingAc.getArglMapping().getChargeCode())) {
                        if (lclBookingAc.getArAmount() != null) {
                            totalAmount += lclBookingAc.getArAmount().doubleValue();
                        }
                        lclcostchargedao.saveOrUpdate(lclBookingAc);
                    } else {
                        cafBookingAc = lclBookingAc;
                    }
//                if (lclBookingAc.getApAmount() != null && lclBookingAc.getApAmount().doubleValue() > 0.00 && CommonUtils.notIn(costStatus, "AP", "DS", "IP")) {
//                    lclManifestDAO.createLclAccruals(lclBookingAc, LCL_SHIPMENT_TYPE_IMPORT, STATUS_OPEN);
//                    lclUtils.insertLCLBookingAcTrans(lclBookingAc, TRANSACTION_TYPE_ACCRUALS, "A", "", lclBookingAc.getApAmount(), user);
//                }
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
            Double cafAmt = calculateCAFAmount(cafBookingAc, totalAmount);
            cafBookingAc.setArAmount(new BigDecimal(cafAmt).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
            if (cafBookingAc.getApAmount() == null) {
                cafBookingAc.setApAmount(BigDecimal.ZERO);
            }
            cafBookingAc.setControlNo(cafBookingAc.getControlNo());
            lclcostchargedao.saveOrUpdate(cafBookingAc);
        }
        return importChargeList;
    }

    public Double calculateCAFAmount(LclBookingAc cafBookingAc, Double totalAmount) throws Exception {
        Double finalValue = 0.0;
        if (cafBookingAc.getRolledupCharges() != null) {
            finalValue = totalAmount * cafBookingAc.getRolledupCharges().doubleValue();
        }
        if (cafBookingAc.getTotalWeight() != null && cafBookingAc.getTotalWeight().doubleValue() > finalValue) {
            finalValue = cafBookingAc.getTotalWeight().doubleValue();
        }
        return finalValue;
    }

    /**
     * A-->valueOfGoods B-->totalCharge
     *
     */
    public Double calInsurance(Double insurt, Double insamt, Double A) {
        Double CIFValue = 0.0;
        Double B = sumOfCharges();
        Double C = 0.0;
        Double D = 0.0;
        Double insurance = 0.0;
        if (insamt != 0.0 && insurt != 0.0) {
            C = ((A + B) / insamt) * insurt;
        }
        D = (10.0 / 100.0) * (A + B + C);
        CIFValue = A + B + C + D;
        if (insamt != null) {
            insurance = (CIFValue / 100) * insurt;
        }
        return insurance;
    }

    public Double sumOfCharges() {
        Double total = 0.0;
        if (importChargeList != null && importChargeList.size() > 0) {
            for (int i = 0; i < importChargeList.size(); i++) {
                LclBookingAc lba = (LclBookingAc) importChargeList.get(i);
                if (lba.getRolledupCharges() != null && CommonUtils.isNotEmpty(lba.getRolledupCharges().doubleValue())) {
                    total = total + lba.getRolledupCharges().doubleValue();
                }
            }
        }
        return total;
    }

    public BigDecimal calculateImpAutoRates(String polUnCode, String podUnCode, String fdUnCode, String pooUnCode, List<LclBookingPiece> lclBookingPiecesList, String transhipment) throws Exception {
        BigDecimal totalStorageAmt = new BigDecimal(0.00);
        List<String> commodityList = new ArrayList<String>();
        LCLImportRatesDAO importRatesDAO = new LCLImportRatesDAO();
        List exceptionRatesList = new ArrayList();
        for (LclBookingPiece lbp : lclBookingPiecesList) {
            if (lbp.getCommodityType() != null && lbp.getCommodityType().getCode() != null) {
                commodityList.add(lbp.getCommodityType().getCode());
            } else {
                commodityList.add(lbp.getCommNo());
            }
        }
        String polSchnum = portsDAO.getShedulenumber(polUnCode);
        String podSchnum = portsDAO.getShedulenumber(podUnCode);
        String fdSchnum = portsDAO.getShedulenumber(fdUnCode);
        String pooSchnum = portsDAO.getShedulenumber(pooUnCode);
        if (!commodityList.isEmpty() && !"".equalsIgnoreCase(polSchnum) && !"".equalsIgnoreCase(podSchnum)) {
            List storageChargesList = new LCLImportRatesDAO().getLclImpStorageCharges(polSchnum, podSchnum, "", commodityList);
            if (storageChargesList.size() > 0) {
                exceptionRatesList = importRatesDAO.getExceptionList(!pooSchnum.equalsIgnoreCase("") ? pooSchnum : "00000", polSchnum, podSchnum, fdSchnum, commodityList);
                totalStorageAmt = new LCLImportChargeCalc().calculateStorageImpRate(storageChargesList, lclBookingPiecesList, exceptionRatesList, transhipment);
            }
        }
        return totalStorageAmt;
    }

    public BigDecimal calculateStorageImpRate(List ratesList, List<LclBookingPiece> commodityList, List exceptionRatesList, String transhipment) throws Exception {
        BigDecimal totalStorage = new BigDecimal(0.00);
        Double totalMeasureImp = 0.00;
        Double totalWeightImp = 0.00;
        Double totalMeasureMet = 0.00;
        Double totalWeightMet = 0.00;
        List<LclImportsRatesBean> importCharge = new LCLImportRatesDAO().filterOriginalRatesList(ratesList, exceptionRatesList, transhipment);
        for (LclBookingPiece lbp : commodityList) {
            if (lbp.getBookedVolumeImperial() != null && lbp.getBookedVolumeImperial().doubleValue() != 0.00) {
                totalMeasureImp += lbp.getBookedVolumeImperial().doubleValue();
            }
            if (lbp.getBookedWeightImperial() != null && lbp.getBookedWeightImperial().doubleValue() != 0.00) {
                totalWeightImp += lbp.getBookedWeightImperial().doubleValue();
            }
            if (lbp.getBookedVolumeMetric() != null && lbp.getBookedVolumeMetric().doubleValue() != 0.00) {
                totalMeasureMet += lbp.getBookedVolumeMetric().doubleValue();
            }
            if (lbp.getBookedWeightMetric() != null && lbp.getBookedWeightMetric().doubleValue() != 0.00) {
                totalWeightMet += lbp.getBookedWeightMetric().doubleValue();
            }
        }
        for (LclImportsRatesBean chargeBean : importCharge) {
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
            if (chargeType.equals("1")) {
                totalStorage = (new BigDecimal(flatrt).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
            }
            if (chargeType.equals("2")) {
                Double fobValue = 1.00;//need to remove
                Double calValue = 0.0;
                calValue = (blpct * fobValue) / 100;
                if (calValue < minCharge) {
                    totalStorage = (new BigDecimal(minCharge).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                } else {
                    totalStorage = (new BigDecimal(calValue).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                }
            }
            if (chargeType.equals("3")) {
                Double cbmCalValue = 0.0;
                Double kgsCalValue = 0.0;
                if (cw$ != 0.0 || cf$ != 0.0) {
                    cbmCalValue = cf$ * totalMeasureImp;
                    kgsCalValue = (cw$ * totalWeightImp) / 100;
                    if (cbmCalValue <= minCharge && kgsCalValue <= minCharge) {
                        totalStorage = (new BigDecimal(minCharge).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                    } else if (cbmCalValue <= kgsCalValue) {
                        totalStorage = (new BigDecimal(kgsCalValue).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                    } else if (cbmCalValue >= kgsCalValue) {
                        totalStorage = (new BigDecimal(cbmCalValue).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                    }
                } else if (cbmrt != 0.0 || kgsrt != 0.0) {
                    cbmCalValue = cbmrt * totalMeasureMet;
                    kgsCalValue = (kgsrt * totalWeightMet) / 1000;
                    if (cbmCalValue <= minCharge && kgsCalValue <= minCharge) {
                        totalStorage = (new BigDecimal(minCharge).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                    } else if (cbmCalValue <= kgsCalValue) {
                        totalStorage = (new BigDecimal(kgsCalValue).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                    } else if (cbmCalValue >= kgsCalValue) {
                        totalStorage = (new BigDecimal(cbmCalValue).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                    }
                }
            }
            if (chargeType.equals("4")) {
                Double calValue = 0.0;
                Double fobValue = 1.00;//need to remove
                calValue = (fobValue * inrate) / insamt;
                if (calValue < minCharge) {
                    totalStorage = (new BigDecimal(minCharge));
                } else {
                    totalStorage = (new BigDecimal(calValue).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                }
            }
        }
        return totalStorage;
    }

    public List<LclBookingAc> getNewipiCost(List<LclBookingPiece> commodityList,
            String billToParty, Long fileId,
            User user, Iterator ipiCost, String transhipment) throws Exception {
        LclFileNumberDAO lclFileNumberDAO = new LclFileNumberDAO();
        GlMappingDAO glMappingDAO = new GlMappingDAO();
        LclBookingPiece lclBookingPiece = null;
        Date now = new Date();
        importChargeList = new ArrayList();
        LclBookingAc apCost = fileId != null && fileId > 0 ? new LclCostChargeDAO().findByBlueScreenChargeCode(fileId, "1607", false) : null;
        LclBookingAc lclBookingAc = apCost == null ? new LclBookingAc() : apCost;
        String costStatus = new LCLBookingAcTransDAO().getTransType(lclBookingAc.getId());
        String shipmentType = LCL_SHIPMENT_TYPE_IMPORT;
        if ("Y".equalsIgnoreCase(transhipment)) {
            shipmentType = LCL_SHIPMENT_TYPE_EXPORT;
        }
        lclBookingAc.setArglMapping(glMappingDAO.findByBlueScreenChargeCode("1607", shipmentType, TRANSACTION_TYPE_ACCOUNT_RECEIVABLE));
        if (ipiCost != null && "1607".equalsIgnoreCase(lclBookingAc.getArglMapping().getBlueScreenChargeCode()) && ipiCost.hasNext()) {
            Object cost[] = (Object[]) ipiCost.next();
            Double totalMeasureImp = 0.00;
            Double totalWeightImp = 0.00;
            Double totalMeasureMet = 0.00;
            Double totalWeightMet = 0.00;
            Double minCost = 0.0;
            Double lbsCost = 0.0;
            Double cbmCost = 0.0;
            Double kgsCost = 0.0;
            Double cftCost = 0.0;
            Double cbmCostVal = 0.0;
            Double kgsCostVal = 0.0;
            String costVendor = "";
            for (LclBookingPiece lbp : commodityList) {
                lclBookingPiece = lbp;
                if (lbp.getBookedVolumeImperial() != null && lbp.getBookedVolumeImperial().doubleValue() != 0.00) {
                    totalMeasureImp += lbp.getBookedVolumeImperial().doubleValue();//cft
                }
                if (lbp.getBookedWeightImperial() != null && lbp.getBookedWeightImperial().doubleValue() != 0.00) {
                    totalWeightImp += lbp.getBookedWeightImperial().doubleValue();//LBS
                }
                if (lbp.getBookedVolumeMetric() != null && lbp.getBookedVolumeMetric().doubleValue() != 0.00) {
                    totalMeasureMet += lbp.getBookedVolumeMetric().doubleValue();//CBM
                }
                if (lbp.getBookedWeightMetric() != null && lbp.getBookedWeightMetric().doubleValue() != 0.00) {
                    totalWeightMet += lbp.getBookedWeightMetric().doubleValue();//KGS
                }
            }
            if (cost[1] != null) {
                minCost = Double.parseDouble(String.valueOf(cost[1]));
            }
            if (cost[2] != null) {
                lbsCost = Double.parseDouble(String.valueOf(cost[2]));
            }
            if (cost[3] != null) {
                cftCost = Double.parseDouble(String.valueOf(cost[3]));
            }
            if (cost[4] != null) {
                cbmCost = Double.parseDouble(String.valueOf(cost[4]));
            }
            if (cost[5] != null) {
                kgsCost = Double.parseDouble(String.valueOf(cost[5]));
            }
            if (cost[6] != null) {
                costVendor = String.valueOf(cost[6]);
            }
            if (lbsCost != 0.0 || cftCost != 0.0) {
                if (CommonUtils.notIn(costStatus, "AP", "IP")) {
                    cbmCostVal = cftCost * totalMeasureImp;
                    kgsCostVal = (lbsCost * totalWeightImp) / 100;
                    if (cbmCostVal <= minCost && kgsCostVal <= minCost) {
                        lclBookingAc.setApAmount(new BigDecimal(minCost).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                    } else if (cbmCostVal <= kgsCostVal) {
                        lclBookingAc.setApAmount(new BigDecimal(kgsCostVal).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                    } else if (cbmCostVal >= kgsCostVal) {
                        lclBookingAc.setApAmount(new BigDecimal(cbmCostVal).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                    }
                    lclBookingAc.setSupAcct(new TradingPartnerDAO().findById(costVendor));
                    lclBookingAc.setDeleted(false);
                }
            } else if (cbmCost != 0.0 || kgsCost != 0.0) {
                if (CommonUtils.notIn(costStatus, "AP", "IP")) {
                    cbmCostVal = cbmCost * totalMeasureMet;
                    kgsCostVal = (kgsCost * totalWeightMet) / 1000;
                    if (kgsCostVal <= minCost) {
                        lclBookingAc.setApAmount(new BigDecimal(minCost).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                    } else if (cbmCostVal <= kgsCostVal) {
                        lclBookingAc.setApAmount(new BigDecimal(kgsCostVal).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                    } else if (cbmCostVal >= kgsCostVal) {
                        lclBookingAc.setApAmount(new BigDecimal(cbmCostVal).divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                    }
                    lclBookingAc.setSupAcct(new TradingPartnerDAO().findById(costVendor));
                    lclBookingAc.setDeleted(false);
                }
            }
        }

        if (lclBookingAc.getArglMapping().getChargeCode().equalsIgnoreCase(CHARGE_CODE_IPI)) {
            lclBookingAc.setApglMapping(glMappingDAO.findByChargeCode(CHARGE_CODE_IPI, LCL_SHIPMENT_TYPE_IMPORT, TRANSACTION_TYPE_ACCRUALS));
        }
        lclBookingAc.setRatePerUnitUom(RATE_UNIT_PER_UOM_FL);
        lclBookingAc.setAdjustmentAmount(BigDecimal.ZERO);
//        lclBookingAc.setArBillToParty(lclBookingAc.getArBillToParty() == null ? billToParty : lclBookingAc.getArBillToParty());
//        lclBookingAc.setRelsToInv(lclBookingAc.getArBillToParty().equalsIgnoreCase("A") ? true : false);
        lclBookingAc.setPrintOnBl(true);
        lclBookingAc.setBundleIntoOf(false);
        if (lclBookingPiece != null) {
            lclBookingAc.setLclBookingPiece(lclBookingPiece);
        }
        lclBookingAc.setCostFlatrateAmount(lclBookingAc.getApAmount());
        lclBookingAc.setEnteredBy(user);
        lclBookingAc.setModifiedBy(user);
        lclBookingAc.setEnteredDatetime(now);
        lclBookingAc.setModifiedDatetime(now);
        lclBookingAc.setTransDatetime(now);
        lclBookingAc.setArAmount(BigDecimal.ZERO);
        lclBookingAc.setControlNo(lclBookingAc.getControlNo());
        if (fileId != null && fileId > 0) {
            LclFileNumber lclFileNumber = lclFileNumberDAO.findById(fileId);
            lclBookingAc.setLclFileNumber(lclFileNumber);
            importChargeList.add(lclBookingAc);
            lclcostchargedao.saveOrUpdate(lclBookingAc);
        } else {
            importChargeList.add(lclBookingAc);
        }
        return importChargeList;
    }

    public void calculateHazMatRates(String pooCode, String polUnCode, String podUnCode, String fdUnCode, String transhipment, String billingType,
            String billToParty, String impCfsId, Long fileId, List<LclBookingPiece> lclBookingPiecesList, HttpServletRequest request, User user) throws Exception {
        LCLImportRatesDAO lclImportRatesDAO = new LCLImportRatesDAO();
        List<LclImportsRatesBean> importChargesList = new ArrayList();
        List<String> commodityList = new ArrayList<String>();
        List<String> chargeCodeList = new ArrayList<String>();
        List<LclImportsRatesBean> exceptionRatesList = new ArrayList<LclImportsRatesBean>();
        List<LclImportsRatesBean> ratesList = new ArrayList();
        List<LclImportsRatesBean> ipiChargeList = new ArrayList();
        Iterator ipiCost = null;
//        boolean hazmatFound = false;
        for (LclBookingPiece lbp : lclBookingPiecesList) {
            if (lbp.getCommodityType() != null && lbp.getCommodityType().getCode() != null) {
                commodityList.add(lbp.getCommodityType().getCode());
            } else {
                commodityList.add(lbp.getCommNo());
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
        String billingTerms[] = {"C", "P"};
        List<String> billingTermsList = Arrays.asList(billingTerms);
        importChargesList = lclImportRatesDAO.getLCLImportHazCharges(polSchnum, podSchnum, commodityList, chargeCodeList, billingTermsList);

        if (CommonUtils.isNotEqualIgnoreCase(podUnCode, fdUnCode) && "N".equalsIgnoreCase(transhipment)) {
            ipiChargeList = lclImportRatesDAO.getLCLImportIPICharges(podSchnum, fdSchnum, commodityList, "", null);// ipi ipiCharges
            for (LclImportsRatesBean ipiChargeLists : ipiChargeList) {
                if (ipiChargeLists.getChargeCode().equalsIgnoreCase("1682")) {
                    importChargesList.add(ipiChargeLists);
                }
            }
            if (null != impCfsId && !impCfsId.equals("")) {
                List<String> costeCodes = new ArrayList<String>();
                costeCodes.add("1682");
                ipiCost = lclImportRatesDAO.getLclImpIPICost(podSchnum, fdSchnum, impCfsId, costeCodes);
            }
        }
        if (importChargesList.size() > 0) {
            ratesList.addAll(importChargesList);
        }
        this.calculateImportRate(ratesList, lclBookingPiecesList, billToParty, fileId, user, ipiCost, transhipment, exceptionRatesList, request);
    }
}
