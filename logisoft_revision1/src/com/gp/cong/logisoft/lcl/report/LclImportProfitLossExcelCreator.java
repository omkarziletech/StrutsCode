/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.lcl.report;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.beans.BlueScreenChargesBean;
import com.gp.cong.logisoft.beans.LclImpProfitLossBean;
import com.gp.cong.logisoft.domain.lcl.LclBookingPad;
import com.gp.cong.logisoft.domain.lcl.LclBookingPiece;
import com.gp.cong.logisoft.domain.lcl.LclBookingPieceUnit;
import com.gp.cong.logisoft.domain.lcl.LclSsAc;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.hibernate.dao.WarehouseDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPadDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceUnitDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.logiware.excel.BaseExcelCreator;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author VinothS
 */
public class LclImportProfitLossExcelCreator extends BaseExcelCreator implements LclReportConstants {

    public String filePath;
    public String unitSSId;
    public String unitNo;
    public List<LclSsAc> lclSsAcList = null;
    public LclUnitSs lclUnitSs;
//    DecimalFormat df = new DecimalFormat("########0.00");
    private Integer totalPieceCount = 0;
    private Double totalKgs = 0d;
    private Double totalCbm = 0d;
    private LclUnitSsDAO lclUnitSsDAO = new LclUnitSsDAO();
    private String concatVoyageNo;
    private StringBuilder fileId = new StringBuilder();
    private String whseAcctNo = "";
    private String whseAcctName = "";

    public LclImportProfitLossExcelCreator() {
    }

    public LclImportProfitLossExcelCreator(String fileLocPath, String unitSsId, String unitNum) {
        this.filePath = fileLocPath;
        this.unitSSId = unitSsId;
        this.unitNo = unitNum;
    }

    public void createReport() {
        try {
            init(filePath, LCL_IMP_UNITS_PL_DOC_NAME);
            writeContent(unitSSId);
            writeIntoFile();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            exit();
        }
    }

    public void writeContent(String unitSSId) throws Exception {
        headingDetails();
        setCellBorder(lclCellStyleBoldArialBorder, (short) 2, (short) 2, (short) 2, (short) 2);
        createHeaderCell("", lclCellStyleBoldArialBorder);
        mergeCells(rowIndex, rowIndex, 0, 10);
        row.setHeightInPoints(6);
        createRow();
        resetColumnIndex();
        createRow();
        resetColumnIndex();
        unitDetails();
        resetColumnIndex();
        createRow();
        createHeaderCell("", lclCellStyleBoldArialBorder);
        mergeCells(rowIndex, rowIndex, 0, 10);
        row.setHeightInPoints(6);
        createRow();
        resetColumnIndex();
        createRow();
        bookingDetails();
        summaryDetails();
        createRow();
    }

    public void createEmptyCell() throws IOException {
        createCell();
        createCell();
        createCell();
        createCell();
    }

    public void headingDetails() throws Exception {
        createRow();
        createHeaderCell("Profit and Loss Sheet", lclCellStyleLeftBoldArial15);
        mergeCells(rowIndex, rowIndex, 0, 3);
        createRow();
        resetColumnIndex();
        lclUnitSs = lclUnitSsDAO.getByProperty("id", Long.parseLong(unitSSId));
        //**Total no Of Voyag No
        createHeaderCell("Voyage No :" + lclUnitSs.getLclSsHeader().getScheduleNo(), lclCellStyleLeftBoldArial10);
        mergeCells(rowIndex, rowIndex, 0, 3);
        createRow();
        resetColumnIndex();
        //**Total no Of City
        createHeaderCell("City :" + lclUnitSs.getLclSsHeader().getOrigin().getUnLocationName(), lclCellStyleLeftBoldArial10);
        mergeCells(rowIndex, rowIndex, 0, 3);
        createRow();
        resetColumnIndex();
        //**Total no Of Destination
        createHeaderCell("Destination :" + lclUnitSs.getLclSsHeader().getDestination().getUnLocationName(), lclCellStyleLeftBoldArial10);
        mergeCells(rowIndex, rowIndex, 0, 3);
        createRow();
        resetColumnIndex();
        createHeaderCell("Container Size :" + lclUnitSs.getLclUnit().getUnitType().getDescription(), lclCellStyleLeftBoldArial10);
        mergeCells(rowIndex, rowIndex, 0, 3);
        createRow();
        resetColumnIndex();
        //Agent Value***************
//        Integer agentCount = new PortsDAO().getAgentCount(lclUnitSs.getLclSsHeader().getOrigin().getUnLocationCode(), "I");
//        String agent[] = null;
//        if (agentCount <= 1) {
//            agent = new PortsDAO().getDefaultAgentForLcl(lclUnitSs.getLclSsHeader().getOrigin().getUnLocationCode(), "I");
//        }
        createHeaderCell("Agent Name :" + lclUnitSs.getLclSsHeader().getLclUnitSsImportsList().get(0).getOriginAcctNo().getAccountName(), lclCellStyleLeftBoldArial10);
        mergeCells(rowIndex, rowIndex, 0, 3);
        createRow();
        resetColumnIndex();
        //**Total no Of Drs
        Object count = new LclBookingPieceDAO().noOfDrBylclUnitSSId(lclUnitSs.getId().toString());
        createHeaderCell("No Of Drs :" + count, lclCellStyleLeftBoldArial10);
        mergeCells(rowIndex, rowIndex, 0, 3);
        createRow();
        resetColumnIndex();
        //**Total no Of Cbm 
        createHeaderCell("CBM :" + lclUnitSs.getLclSsHeader().getLclUnitSsManifestList().get(0).getCalculatedVolumeMetric(), lclCellStyleLeftBoldArial10);
        mergeCells(rowIndex, rowIndex, 0, 3);
        createRow();
        resetColumnIndex();
        //**Total no Of KGS
        createHeaderCell("KGS :" + lclUnitSs.getLclSsHeader().getLclUnitSsManifestList().get(0).getCalculatedWeightMetric(), lclCellStyleLeftBoldArial10);
        mergeCells(rowIndex, rowIndex, 0, 3);
        createRow();
        resetColumnIndex();
        createRow();
        createHeaderCell("General Imports", lclCellStyleLeftBoldArial12);
        mergeCells(rowIndex, rowIndex, 0, 3);
        createRow();
        resetColumnIndex();
        createRow();
    }

    public void commonHeadingDetails() throws Exception {
        resetColumnIndex();
        createRow();
        createRow();
        createCell();
        createCell();
        createHeaderCell("Bill To", lclCellStyleLeftBoldArial10);
        createHeaderCell("Revenue", lclCellStyleLeftBoldArial10);
        createCell();
        createCell();
        createCell();
        createHeaderCell("Vendor", lclCellStyleLeftBoldArial10);
        createHeaderCell("Cost", lclCellStyleLeftBoldArial10);
        createCell();
        resetColumnIndex();
        createRow();
    }

    public void unitDetails() throws Exception {
        createHeaderCell("cont." + unitNo, lclCellStyleLeftBoldArial12);
        createCell();
        createCell();
        createCell();
        lclUnitSs = lclUnitSsDAO.getByProperty("id", Long.parseLong(unitSSId));
        concatVoyageNo = lclUnitSs.getLclSsHeader().getBillingTerminal().getTrmnum() + "-" + lclUnitSs.getLclSsHeader().getOrigin().getUnLocationCode()
                + "-" + lclUnitSs.getLclSsHeader().getDestination().getUnLocationCode() + "-" + lclUnitSs.getLclSsHeader().getScheduleNo();
        String[] whseDetails = new WarehouseDAO().getWhseDetails(lclUnitSs.getLclSsHeader().getId(), lclUnitSs.getLclUnit().getId());
        whseAcctNo = whseDetails[0];
        whseAcctName = whseDetails[1];
        createHeaderCell(concatVoyageNo, lclCellStyleLeftBoldArial12);
        commonHeadingDetails();
        List<LclImpProfitLossBean> unitCostDetails = lclUnitSsDAO.getUnitCostDetails(lclUnitSs.getId(), "unitFlag");
        if (CommonUtils.isNotEmpty(unitCostDetails)) {
            for (LclImpProfitLossBean chargecost : unitCostDetails) {
                if (chargecost.getArAmount() != null && chargecost.getArAmount() != 0.00d) {
                    createHeaderCell(chargecost.getBlueChargeCode(), cellStyleRightNormalArial10);
                    createHeaderCell(chargecost.getChargeCode(), lclCellStyleNormalArial10);
                    String chargeVendor = "";
                    if (CommonUtils.isNotEmpty(chargecost.getChargeVendorName())) {
                        if (chargecost.getChargeVendorName().length() >= 19) {
                            chargeVendor = chargecost.getChargeVendorName().substring(0, 19);
                        } else {
                            chargeVendor = chargecost.getChargeVendorName();
                        }
                    }
                    createHeaderCell(chargecost.getChargeVendorNo() + "(" + chargeVendor + ")", lclCellStyleNormalArial10);
                    if (chargecost.getArAmount() < 0.0) {
                        createDollarCell(chargecost.getArAmount(), redLclCellStyleRightNormalArial10);
                    } else {
                        createDollarCell(chargecost.getArAmount(), cellStyleRightNormalArial10);
                    }
                } else {
                    createEmptyCell();
                }
                setCellBorder(lclCellStyleBoldArialLine, (short) 0, (short) 1, (short) 0, (short) 0);
                createHeaderCell("", lclCellStyleBoldArialLine);
                if (chargecost.getApAmount() != null && chargecost.getApAmount() != 0.00d) {
                    createHeaderCell(chargecost.getBlueCostCode(), cellStyleRightNormalArial10);
                    createHeaderCell(chargecost.getCostCode(), lclCellStyleNormalArial10);
                    String costVendor = "";
                    if (CommonUtils.isNotEmpty(chargecost.getCostVendorName())) {
                        if (chargecost.getCostVendorName().length() >= 14) {
                            costVendor = chargecost.getCostVendorName().substring(0, 14);
                        } else {
                            costVendor = chargecost.getCostVendorName();
                        }
                    }
                    createHeaderCell(chargecost.getCostVendorNo() + "(" + costVendor + ")", lclCellStyleNormalArial10);
                    if (chargecost.getApAmount() < 0.0) {
                        createDollarCell(chargecost.getApAmount(), redLclCellStyleRightNormalArial10);
                    } else {
                        createDollarCell(chargecost.getApAmount(), cellStyleRightNormalArial10);
                    }
                } else {
                    createEmptyCell();
                }
                resetColumnIndex();
                createRow();
            }
        }
    }

    public void bookingDetails() throws Exception {
        List<LclBookingPieceUnit> bookPieceUnitList = new LclBookingPieceUnitDAO().findByProperty("lclUnitSs.id", Long.parseLong(unitSSId));
        if (CommonUtils.isNotEmpty(bookPieceUnitList)) {
            for (LclBookingPieceUnit bookPieceUnit : bookPieceUnitList) {
                List<LclBookingPiece> listBookPiece = new LclBookingPieceDAO().findByProperty("id", bookPieceUnit.getLclBookingPiece().getId());
                if (CommonUtils.isNotEmpty(listBookPiece)) {
                    for (LclBookingPiece bookPiece : listBookPiece) {
                        totalPieceCount += bookPiece.getBookedPieceCount();
                        if (bookPiece.getBookedWeightMetric() == null) {
                            bookPiece.setBookedWeightMetric(BigDecimal.ZERO);
                        }
                        if (bookPiece.getBookedVolumeMetric() == null) {
                            bookPiece.setBookedVolumeMetric(BigDecimal.ZERO);
                        }
                        totalKgs += bookPiece.getBookedWeightMetric().doubleValue();
                        totalCbm += bookPiece.getBookedVolumeMetric().doubleValue();
                        fileId.append(bookPiece.getLclFileNumber().getId()).append(",");
                        createHeaderCell("IMP-" + bookPiece.getLclFileNumber().getFileNumber(), lclCellStyleLeftBoldArial10);
                        createCell();
                        if (CommonUtils.isNotEmpty(bookPiece.getLclFileNumber().getLclBookingImportAmsList())) {
                            createHeaderCell("HBL " + bookPiece.getLclFileNumber().getLclBookingImportAmsList().get(0).getScac().toUpperCase() + " " + bookPiece.getLclFileNumber().getLclBookingImportAmsList().get(0).getAmsNo().toString(), lclCellStyleLeftBoldArial10);
                        } else {
                            createCell();
                        }
                        createCell();
                        createCell();
                        createHeaderCell(bookPiece.getBookedPieceCount() + " " + bookPiece.getPackageType().getDescription(), lclCellStyleLeftBoldArial10);
                        createHeaderCell(bookPiece.getBookedWeightMetric() + " kgs " + " - " + bookPiece.getBookedVolumeMetric() + " cbm", lclCellStyleLeftBoldArial10);
                        createCell();
                        createCell();
                        if (null != bookPiece.getLclFileNumber().getLclBooking().getPortOfDestination()
                                && !bookPiece.getLclFileNumber().getLclBooking().getFinalDestination().
                                getUnLocationCode().equalsIgnoreCase(bookPiece.getLclFileNumber().getLclBooking().getPortOfDestination().getUnLocationCode())) {
                            createHeaderCell("Final dest." + bookPiece.getLclFileNumber().getLclBooking().getFinalDestination().getUnLocationName().toString(), lclCellStyleLeftBoldArial10);
                        } else if (bookPiece.getLclFileNumber().getLclBooking().getPooPickup() != null && bookPiece.getLclFileNumber().getLclBooking().getPooPickup()) {
                            createHeaderCell("Door Del .", lclCellStyleLeftBoldArial10);
                            resetColumnIndex();
                            createRow();
                            createEmptyCell();
                            createEmptyCell();
                            createCell();
                            String city = "";
                            LclBookingPad lclBookingPad = new LclBookingPadDAO().getByProperty("lclFileNumber.id", bookPiece.getLclFileNumber().getId());
                            if (lclBookingPad != null && lclBookingPad.getPickupContact() != null && lclBookingPad.getPickupContact().getCity() != null) {
                                city = lclBookingPad.getPickupContact().getCity();
                            }
                            createHeaderCell(city, lclCellStyleLeftBoldArial10);
                        } else {
                            createHeaderCell("Final dest .", lclCellStyleLeftBoldArial10);
                            resetColumnIndex();
                            createRow();
                            createEmptyCell();
                            createEmptyCell();
                            createCell();
                            String stateName = "";
                            if (bookPiece.getLclFileNumber().getLclBooking().getFinalDestination().getStateId() != null) {
                                stateName = bookPiece.getLclFileNumber().getLclBooking().getFinalDestination().getStateId().getCodedesc().toString();
                            } else {
                                stateName = "";
                            }
                            createHeaderCell(bookPiece.getLclFileNumber().getLclBooking().getFinalDestination().getUnLocationName().toString() + " ," + stateName, lclCellStyleLeftBoldArial10);
                        }
                        commonHeadingDetails();
                        List<LclImpProfitLossBean> chargeCostList = lclUnitSsDAO.getDRChargeDetails(bookPiece.getLclFileNumber().getId());
                        //Adding Charges from bluscreen DB *********************
                        if (bookPiece.getLclFileNumber().getLclBooking().getBookingType().equalsIgnoreCase("T")) {
                            chargeCostList = addingChargesFromBluscreenDB(chargeCostList, bookPiece.getLclFileNumber().getId().toString());
                        }
                        //******************************************************
                        if (CommonUtils.isNotEmpty(chargeCostList)) {
                            Double chargeTotal = 0.00;
                            Double costTotal = 0.00;
                            for (LclImpProfitLossBean chargeList : chargeCostList) {
                                resetColumnIndex();
                                createRow();
                                //Cahrge Code***********************************
                                if (chargeList.getArAmount() != null && chargeList.getArAmount() != 0.00d && (bookPiece.getLclFileNumber().getStatus().equals("M") || "true".equals(chargeList.getAgentFlag()))
                                        && (CommonUtils.isEmpty(chargeList.getAgentBilledFlag()) || "true".equals(chargeList.getAgentFlag())) || (bookPiece.getLclFileNumber().getLclBooking().getBookingType().equalsIgnoreCase("T") && chargeList.isBluScreenFlag())) {
                                    createHeaderCell(chargeList.getBlueChargeCode(), lclCellStyleLeftNormalArial10);
                                    if (chargeList.isBluScreenFlag()) {
                                        createHeaderCell(chargeList.getChargeCode() + "(BS)", lclCellStyleLeftNormalArial10);
                                    } else if ("true".equals(chargeList.getAgentFlag())) {
                                        createHeaderCell(chargeList.getChargeCode() + "(INV)", lclCellStyleLeftNormalArial10);
                                    } else {
                                        createHeaderCell(chargeList.getChargeCode(), lclCellStyleLeftNormalArial10);
                                    }
                                    //Bill To***************************************
                                    String vendorName = "";
                                    if ("W".equalsIgnoreCase(chargeList.getBilltoParty())) {
                                        vendorName = whseAcctName;
                                    } else if (CommonUtils.isNotEmpty(chargeList.getChargeVendorName())) {
                                        if (chargeList.getChargeVendorName().length() >= 19) {
                                            vendorName = chargeList.getChargeVendorName().substring(0, 19);
                                        } else {
                                            vendorName = chargeList.getChargeVendorName();
                                        }
                                    }
                                    if (CommonUtils.isNotEmpty(chargeList.getChargeVendorNo())) {
                                        createHeaderCell(chargeList.getChargeVendorNo() + "(" + vendorName + ")", lclCellStyleLeftNormalArial10);
                                    } else if ("W".equalsIgnoreCase(chargeList.getBilltoParty())) {
                                        createHeaderCell(whseAcctNo + "(" + vendorName + ")", lclCellStyleLeftNormalArial10);
                                    } else {
                                        createHeaderCell("", lclCellStyleLeftNormalArial10);
                                    }
                                    //Revanue Amount****************************
                                    if (chargeList.getArAmount() < 0.0) {
                                        createDollarCell(chargeList.getArAmount(), redLclCellStyleRightNormalArial10);
                                    } else {
                                        createDollarCell(chargeList.getArAmount(), cellStyleRightNormalArial10);
                                    }
                                    chargeTotal += chargeList.getArAmount().doubleValue();
                                } else {
                                    createCell();
                                    createCell();
                                    createCell();
                                    createCell();
                                }
                                setCellBorder(lclCellStyleBoldArialLine, (short) 0, (short) 1, (short) 0, (short) 0);
                                //Cost Code***************************************
                                createHeaderCell("", lclCellStyleBoldArialLine);
                                if (chargeList.getApAmount() != null && chargeList.getApAmount() != 0.00d) {
                                    createHeaderCell(chargeList.getBlueCostCode(), lclCellStyleLeftNormalArial10);
                                    createHeaderCell(chargeList.getCostCode(), lclCellStyleLeftNormalArial10);
                                    //Vendor****************************************
                                    String vendorName = "";
                                    if (CommonUtils.isNotEmpty(chargeList.getCostVendorName())) {
                                        if (chargeList.getCostVendorName().length() >= 14) {
                                            vendorName = chargeList.getCostVendorName().substring(0, 14);
                                        } else {
                                            vendorName = chargeList.getCostVendorName();
                                        }
                                    }
                                    if (CommonUtils.isNotEmpty(chargeList.getCostVendorNo())) {
                                        createHeaderCell(chargeList.getCostVendorNo() + "(" + vendorName + ")", lclCellStyleLeftNormalArial10);
                                    } else {
                                        createHeaderCell("", lclCellStyleLeftNormalArial10);
                                    }
                                    //Cost Amount*******************************************
                                    if (chargeList.getApAmount() < 0.0) {
                                        createDollarCell(chargeList.getApAmount(), redLclCellStyleRightNormalArial10);
                                    } else {
                                        createDollarCell(chargeList.getApAmount(), cellStyleRightNormalArial10);
                                    }
                                    costTotal += chargeList.getApAmount().doubleValue();
                                } else {
                                    createCell();
                                    createCell();
                                    createCell();
                                    createCell();
                                }
                            }
                            //END LOOP******************************************
                            resetColumnIndex();
                            createRow();
                            createEmptyCell();
                            createHeaderCell("", lclCellStyleBoldArialLine);
                            createEmptyCell();
                            resetColumnIndex();
                            createRow();
                            createHeaderCell("Total", lclCellStyleLeftBoldArial10);
                            createCell();
                            createCell();
                            if (chargeTotal < 0.0) {
                                createDollarCell(chargeTotal, redLclCellStyleRightNormalArial10);
                            } else {
                                createDollarCell(chargeTotal, cellStyleRightBoldArial10);
                            }
                            createHeaderCell("", lclCellStyleBoldArialLine);
                            createCell();
                            createCell();
                            createCell();
                            if (costTotal < 0.0) {
                                createDollarCell(costTotal, redLclCellStyleRightNormalArial10);
                            } else {
                                createDollarCell(costTotal, cellStyleRightBoldArial10);
                            }
                        }
                        //END IF CONDITION**************************************
                        resetColumnIndex();
                        createRow();
                        setCellBorder(lclCellStyleBoldArialBottomLine, (short) 0, (short) 0, (short) 2, (short) 0);
                        createHeaderCell("", lclCellStyleBoldArialBottomLine);
                        createHeaderCell("", lclCellStyleBoldArialBottomLine);
                        createHeaderCell("", lclCellStyleBoldArialBottomLine);
                        createHeaderCell("", lclCellStyleBoldArialBottomLine);
                        setCellBorder(lclCellStyleBoldArialBottomRightLine, (short) 0, (short) 1, (short) 2, (short) 0);
                        createHeaderCell("", lclCellStyleBoldArialBottomRightLine);
                        createHeaderCell("", lclCellStyleBoldArialBottomLine);
                        createHeaderCell("", lclCellStyleBoldArialBottomLine);
                        createHeaderCell("", lclCellStyleBoldArialBottomLine);
                        createHeaderCell("", lclCellStyleBoldArialBottomLine);
                        createHeaderCell("", lclCellStyleBoldArialBottomLine);
                        createHeaderCell("", lclCellStyleBoldArialBottomLine);
                        resetColumnIndex();
                        createRow();
                        resetColumnIndex();
                        createRow();
                    }
                    //END PARENT LOOP*******************************************
                }
            }
        }
    }

    public void summaryDetails() throws Exception {
        createHeaderCell("Summary", lclCellStyleLeftBoldArial12);
        resetColumnIndex();
        createRow();
        createRow();
        createHeaderCell("cont." + unitNo, lclCellStyleLeftBoldArial12);
        createCell();
        createCell();
        createHeaderCell(concatVoyageNo, lclCellStyleLeftBoldArial12);
        createCell();
        createCell();
        createCell();
        createHeaderCell(totalPieceCount + " pcs " + totalKgs + " kgs - " + NumberUtils.convertToTwoDecimal(totalCbm) + " cbm", lclCellStyleLeftBoldArial12);
        createCell();
        resetColumnIndex();
        createRow();
        createRow();
        createCell();
        createCell();
        createCell();
        createHeaderCell("Revenue", lclCellStyleLeftBoldArial12);
        createEmptyCell();
        createHeaderCell("Cost", lclCellStyleLeftBoldArial12);
        createCell();
        createHeaderCell("Net", lclCellStyleLeftBoldArial12);
        resetColumnIndex();
        createRow();
        createRow();
        LclImpProfitLossBean plBean = null;
        List<LclImpProfitLossBean> chargeCostList = lclUnitSsDAO.getSummaryOfPL(Long.parseLong(unitSSId), StringUtils.removeEnd(fileId.toString(), ","), "Summary");
        //Adding Charges from bluscreen DB *************************************
        //Check existance of T file in voyage
        int noOfTranshpmntFiles = lclUnitSsDAO.findNoOfTranshipmentFilesInVoyage(Long.parseLong(unitSSId));
        if (noOfTranshpmntFiles > 0) {
            chargeCostList = addingChargesFromBluscreenDB(chargeCostList, null);
        }
        //**********************************************************************
        HashMap<String, LclImpProfitLossBean> consolidatedCharges = new LinkedHashMap<String, LclImpProfitLossBean>();
        if (!chargeCostList.isEmpty()) {
            for (LclImpProfitLossBean charges : chargeCostList) {
                //Charge Codes Details******************************************
                if (CommonUtils.isNotEmpty(charges.getFileStatus()) && CommonUtils.isNotEmpty(charges.getBookingType())) {
                    if (charges.getArAmount() != null && charges.getArAmount() != 0.00d) {
                        if (consolidatedCharges.containsKey(charges.getKeyChargeCode())) {
                            plBean = consolidatedCharges.get(charges.getKeyChargeCode());
                            plBean.setChargeCode(charges.getChargeCode());
                            plBean.setBlueChargeCode(charges.getBlueChargeCode());
                            plBean.setArAmount(plBean.getArAmount() + charges.getArAmount());
                        } else {
                            plBean = (LclImpProfitLossBean) BeanUtils.cloneBean(charges);
                            plBean.setApAmount(0.00);
                        }
                        consolidatedCharges.put(charges.getKeyChargeCode(), plBean);
                    }
                } else {
                    if (charges.getArAmount() != null && charges.getArAmount() != 0.00d) {
                        if (consolidatedCharges.containsKey(charges.getKeyChargeCode())) {
                            plBean = consolidatedCharges.get(charges.getKeyChargeCode());
                            plBean.setChargeCode(charges.getChargeCode());
                            plBean.setBlueChargeCode(charges.getBlueChargeCode());
                            plBean.setArAmount(plBean.getArAmount() + charges.getArAmount());
                        } else {
                            plBean = (LclImpProfitLossBean) BeanUtils.cloneBean(charges);
                            plBean.setApAmount(0.00);
                        }
                        consolidatedCharges.put(charges.getKeyChargeCode(), plBean);
                    }
                }
                //Cost Codes Details*********************************************
                if (charges.getApAmount() != null && charges.getApAmount() != 0.00d) {
                    if (consolidatedCharges.containsKey(charges.getKeyCostCode())) {
                        plBean = consolidatedCharges.get(charges.getKeyCostCode());
                        plBean.setCostCode(charges.getCostCode());
                        plBean.setBlueCostCode(charges.getBlueCostCode());
                        plBean.setApAmount(plBean.getApAmount() + charges.getApAmount());
                    } else {
                        plBean = (LclImpProfitLossBean) BeanUtils.cloneBean(charges);
                        plBean.setArAmount(0.0);
                    }
                    consolidatedCharges.put(charges.getKeyCostCode(), plBean);
                }
            }
        }
        Double totalCost = 0.00d;
        Double totalCharge = 0.00d;
        Double netTotal = 0.00d;
        for (Map.Entry<String, LclImpProfitLossBean> mapValues : consolidatedCharges.entrySet()) {
            if (mapValues.getValue().getArAmount() != null && mapValues.getValue().getArAmount() != 0.00d) {
                totalCharge = totalCharge + mapValues.getValue().getArAmount();
                createHeaderCell(mapValues.getValue().getBlueChargeCode(), cellStyleRightNormalArial10);
                createHeaderCell(mapValues.getValue().getChargeCode(), cellStyleRightNormalArial10);
                createCell();
                if (mapValues.getValue().getArAmount() < 0.0) {
                    createDollarCell(mapValues.getValue().getArAmount(), redLclCellStyleRightNormalArial10);
                } else {
                    createDollarCell(mapValues.getValue().getArAmount(), cellStyleRightNormalArial10);
                }
            } else {
                createCell();
                createCell();
                createCell();
                createCell();
            }
            createCell();
            if (mapValues.getValue().getApAmount() != null && mapValues.getValue().getApAmount() != 0.00d) {
                totalCost = totalCost + mapValues.getValue().getApAmount();
                createHeaderCell(mapValues.getValue().getBlueCostCode(), cellStyleRightNormalArial10);
                createHeaderCell(mapValues.getValue().getCostCode(), cellStyleRightNormalArial10);
                createCell();
                if (mapValues.getValue().getApAmount() < 0.0) {
                    createDollarCell(mapValues.getValue().getApAmount(), redLclCellStyleRightNormalArial10);
                } else {
                    createDollarCell(mapValues.getValue().getApAmount(), cellStyleRightNormalArial10);
                }
            } else {
                createCell();
                createCell();
                createCell();
                createCell();
            }
            createCell();
            if ((mapValues.getValue().getArAmount() - mapValues.getValue().getApAmount()) < 0.0) {
                createDollarCell(mapValues.getValue().getArAmount() - mapValues.getValue().getApAmount(), redLclCellStyleRightNormalArial10);
            } else {
                createDollarCell(mapValues.getValue().getArAmount() - mapValues.getValue().getApAmount(), cellStyleRightNormalArial10);
            }
            resetColumnIndex();
            createRow();
            netTotal += mapValues.getValue().getArAmount() - mapValues.getValue().getApAmount();
        }
        createRow();
        createHeaderCell("Total", lclCellStyleLeftBoldArial12);
        createCell();
        createCell();
        if (totalCharge < 0.0) {
            createDollarCell(totalCharge, redLclCellStyleRightNormalArial10);
        } else {
            createDollarCell(totalCharge, cellStyleRightBoldArial10);
        }
        createCell();
        createCell();
        createCell();
        createCell();
        if (totalCost < 0.0) {
            createDollarCell(totalCost, redLclCellStyleRightNormalArial10);
        } else {
            createDollarCell(totalCost, cellStyleRightBoldArial10);
        }
        createCell();
        if (netTotal < 0.0) {
            createDollarCell(netTotal, redLclCellStyleRightNormalArial10);
        } else {
            createDollarCell(netTotal, cellStyleRightBoldArial10);
        }

    }

    private List<LclImpProfitLossBean> addingChargesFromBluscreenDB(List<LclImpProfitLossBean> chargeCostListTemp, String fileNoTemp) throws Exception {
        List<BlueScreenChargesBean> bscbList = null;
        if (fileNoTemp != null) {
            bscbList = new LclUnitSsDAO().getDRChargeFRomBlueScreenDB(fileNoTemp, null);
        } else {
            bscbList = new LclUnitSsDAO().getDRChargeFRomBlueScreenDB(null, StringUtils.removeEnd(fileId.toString(), ","));
        }
        if (!CommonUtils.isEmpty(bscbList)) {
            for (BlueScreenChargesBean bscb : bscbList) {
                if (bscb != null) {
                    if (CommonUtils.isNotEmpty(bscb.getChg01())) {
                        LclImpProfitLossBean liplb = new LclImpProfitLossBean();
                        liplb.setChargeCode(bscb.getChg01());
                        liplb.setBlueChargeCode(bscb.getBchg01());
                        liplb.setArAmount(Double.parseDouble(bscb.getAmt01()));
                        liplb.setKeyChargeCode(bscb.getChg01());
                        liplb.setBluScreenFlag(true);
                        chargeCostListTemp.add(liplb);
                    }
                    if (CommonUtils.isNotEmpty(bscb.getChg02())) {
                        LclImpProfitLossBean liplb = new LclImpProfitLossBean();
                        liplb.setChargeCode(bscb.getChg02());
                        liplb.setBlueChargeCode(bscb.getBchg02());
                        liplb.setArAmount(Double.parseDouble(bscb.getAmt02()));
                        liplb.setKeyChargeCode(bscb.getChg02());
                        liplb.setBluScreenFlag(true);
                        chargeCostListTemp.add(liplb);
                    }
                    if (CommonUtils.isNotEmpty(bscb.getChg03())) {
                        LclImpProfitLossBean liplb = new LclImpProfitLossBean();
                        liplb.setChargeCode(bscb.getChg03());
                        liplb.setBlueChargeCode(bscb.getBchg03());
                        liplb.setArAmount(Double.parseDouble(bscb.getAmt03()));
                        liplb.setKeyChargeCode(bscb.getChg03());
                        liplb.setBluScreenFlag(true);
                        chargeCostListTemp.add(liplb);
                    }
                    if (CommonUtils.isNotEmpty(bscb.getChg04())) {
                        LclImpProfitLossBean liplb = new LclImpProfitLossBean();
                        liplb.setChargeCode(bscb.getChg04());
                        liplb.setBlueChargeCode(bscb.getBchg04());
                        liplb.setArAmount(Double.parseDouble(bscb.getAmt04()));
                        liplb.setKeyChargeCode(bscb.getChg04());
                        liplb.setBluScreenFlag(true);
                        chargeCostListTemp.add(liplb);
                    }
                    if (CommonUtils.isNotEmpty(bscb.getChg05())) {
                        LclImpProfitLossBean liplb = new LclImpProfitLossBean();
                        liplb.setChargeCode(bscb.getChg05());
                        liplb.setBlueChargeCode(bscb.getBchg05());
                        liplb.setArAmount(Double.parseDouble(bscb.getAmt05()));
                        liplb.setKeyChargeCode(bscb.getChg05());
                        liplb.setBluScreenFlag(true);
                        chargeCostListTemp.add(liplb);
                    }
                    if (CommonUtils.isNotEmpty(bscb.getChg06())) {
                        LclImpProfitLossBean liplb = new LclImpProfitLossBean();
                        liplb.setChargeCode(bscb.getChg06());
                        liplb.setBlueChargeCode(bscb.getBchg06());
                        liplb.setArAmount(Double.parseDouble(bscb.getAmt06()));
                        liplb.setKeyChargeCode(bscb.getChg06());
                        liplb.setBluScreenFlag(true);
                        chargeCostListTemp.add(liplb);
                    }
                    if (CommonUtils.isNotEmpty(bscb.getChg07())) {
                        LclImpProfitLossBean liplb = new LclImpProfitLossBean();
                        liplb.setChargeCode(bscb.getChg07());
                        liplb.setBlueChargeCode(bscb.getBchg07());
                        liplb.setArAmount(Double.parseDouble(bscb.getAmt07()));
                        liplb.setKeyChargeCode(bscb.getChg07());
                        liplb.setBluScreenFlag(true);
                        chargeCostListTemp.add(liplb);
                    }
                    if (CommonUtils.isNotEmpty(bscb.getChg08())) {
                        LclImpProfitLossBean liplb = new LclImpProfitLossBean();
                        liplb.setChargeCode(bscb.getChg08());
                        liplb.setBlueChargeCode(bscb.getBchg08());
                        liplb.setArAmount(Double.parseDouble(bscb.getAmt08()));
                        liplb.setKeyChargeCode(bscb.getChg08());
                        liplb.setBluScreenFlag(true);
                        chargeCostListTemp.add(liplb);
                    }
                    if (CommonUtils.isNotEmpty(bscb.getChg09())) {
                        LclImpProfitLossBean liplb = new LclImpProfitLossBean();
                        liplb.setChargeCode(bscb.getChg09());
                        liplb.setBlueChargeCode(bscb.getBchg09());
                        liplb.setArAmount(Double.parseDouble(bscb.getAmt09()));
                        liplb.setKeyChargeCode(bscb.getChg09());
                        liplb.setBluScreenFlag(true);
                        chargeCostListTemp.add(liplb);
                    }
                    if (CommonUtils.isNotEmpty(bscb.getChg10())) {
                        LclImpProfitLossBean liplb = new LclImpProfitLossBean();
                        liplb.setChargeCode(bscb.getChg10());
                        liplb.setBlueChargeCode(bscb.getBchg10());
                        liplb.setArAmount(Double.parseDouble(bscb.getAmt10()));
                        liplb.setKeyChargeCode(bscb.getChg10());
                        liplb.setBluScreenFlag(true);
                        chargeCostListTemp.add(liplb);
                    }
                    if (CommonUtils.isNotEmpty(bscb.getChg11())) {
                        LclImpProfitLossBean liplb = new LclImpProfitLossBean();
                        liplb.setChargeCode(bscb.getChg11());
                        liplb.setBlueChargeCode(bscb.getBchg11());
                        liplb.setArAmount(Double.parseDouble(bscb.getAmt11()));
                        liplb.setKeyChargeCode(bscb.getChg11());
                        liplb.setBluScreenFlag(true);
                        chargeCostListTemp.add(liplb);
                    }
                    if (CommonUtils.isNotEmpty(bscb.getChg12())) {
                        LclImpProfitLossBean liplb = new LclImpProfitLossBean();
                        liplb.setChargeCode(bscb.getChg12());
                        liplb.setBlueChargeCode(bscb.getBchg12());
                        liplb.setArAmount(Double.parseDouble(bscb.getAmt12()));
                        liplb.setKeyChargeCode(bscb.getChg12());
                        liplb.setBluScreenFlag(true);
                        chargeCostListTemp.add(liplb);
                    }
                }
            }
        }
        return chargeCostListTemp;
    }
}
