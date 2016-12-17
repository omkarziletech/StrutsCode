/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.lcl.report;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.beans.BookingChargesBean;
import com.gp.cong.logisoft.domain.lcl.LclBookingPad;
import com.gp.cong.logisoft.domain.lcl.LclBookingPiece;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPadDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.logiware.excel.BaseExcelCreator;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author NambuRajasekar
 */
public class CfsImpWarehouseWorksheet extends BaseExcelCreator implements LclReportConstants {

    public String filePath;
    public String unitSSId;
    public String unitNo;
    public LclUnitSs lclUnitSs;
    private LclUnitSsDAO lclUnitSsDAO = new LclUnitSsDAO();
//    DecimalFormat df = new DecimalFormat("########0.00");
    private String concatVoyageNo;
    private Integer totalPieceCount = 0;
    private Double totalKgs = 0d;
    private Double totalCbm = 0d;
    private String fileId = "";
    private String drNumber = "";
    List<String> aList = null;
    List<String> bList = null;
    List<BookingChargesBean> lclBookingAcList = null;
    private String code = "";
    private String codeDesc = "";
    GenericCodeDAO genericCodeDAO = new GenericCodeDAO();

    public CfsImpWarehouseWorksheet() {

    }

    public CfsImpWarehouseWorksheet(String fileLocPath, String unitSsId, String unitNum) {
        this.filePath = fileLocPath;
        this.unitSSId = unitSsId;
        this.unitNo = unitNum;
    }

    public void createReport() {
        try {
            init(filePath, DOCUMENT_IMPORT_WAREHOUSE);
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
//     

    public void headingDetails() throws Exception {
        createRow();
        createHeaderCell("CFS Warehouse Sheet", lclCellStyleLeftBoldArial15);
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
        String StripDate = DateUtils.formatDate(lclUnitSs.getLclUnit().getLclUnitWhseList().get(0).getDestuffedDatetime(), "dd-MMM-yyyy");
        if (StripDate == null) {
            StripDate = "";
        }
        createHeaderCell("Strip Date: " + StripDate, lclCellStyleLeftBoldArial10);
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
//    

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

    public void commonHeadingNoVendorDetails() throws Exception {
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
        createCell();
        createCell();
//        createHeaderCell("Vendor", lclCellStyleLeftBoldArial10);
//        createHeaderCell("Cost", lclCellStyleLeftBoldArial10);
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
        createHeaderCell(concatVoyageNo, lclCellStyleLeftBoldArial12);
//        commonHeadingDetails();
//        List<LclImpProfitLossBean> unitCostDetails = lclUnitSsDAO.getUnitCostDetails(lclUnitSs.getId(), "unitFlag");
//        if (CommonUtils.isNotEmpty(unitCostDetails)) {
//            for (LclImpProfitLossBean chargecost : unitCostDetails) {
//                if (chargecost.getArAmount() != null && chargecost.getArAmount() != 0.00d) {
//                    createHeaderCell(chargecost.getBlueChargeCode(), cellStyleRightNormalArial10);
//                    createHeaderCell(chargecost.getChargeCode(), lclCellStyleNormalArial10);
//                    String chargeVendor = "";
//                    if (CommonUtils.isNotEmpty(chargecost.getChargeVendorName())) {
//                        if (chargecost.getChargeVendorName().length() >= 19) {
//                            chargeVendor = chargecost.getChargeVendorName().substring(0, 19);
//                        } else {
//                            chargeVendor = chargecost.getChargeVendorName();
//                        }
//                    }
//                    createHeaderCell(chargecost.getChargeVendorNo() + "(" + chargeVendor + ")", lclCellStyleNormalArial10);
//                    if (chargecost.getArAmount() < 0.0) {
//                        createHeaderCell("$" + df.format(chargecost.getArAmount()), redLclCellStyleRightNormalArial10);
//                    } else {
//                        createHeaderCell("$" + df.format(chargecost.getArAmount()), cellStyleRightNormalArial10);
//                    }
//                } else {
//                    createEmptyCell();
//                }
//                setCellBorder(lclCellStyleBoldArialLine, (short) 0, (short) 1, (short) 0, (short) 0);
//                createHeaderCell("", lclCellStyleBoldArialLine);
//                if (chargecost.getApAmount() != null && chargecost.getApAmount() != 0.00d) {
//                    createHeaderCell(chargecost.getBlueCostCode(), cellStyleRightNormalArial10);
//                    createHeaderCell(chargecost.getCostCode(), lclCellStyleNormalArial10);
//                    String costVendor = "";
//                    if (CommonUtils.isNotEmpty(chargecost.getCostVendorName())) {
//                        if (chargecost.getCostVendorName().length() >= 14) {
//                            costVendor = chargecost.getCostVendorName().substring(0, 14);
//                        } else {
//                            costVendor = chargecost.getCostVendorName();
//                        }
//                    }
//                    createHeaderCell(chargecost.getCostVendorNo() + "(" + costVendor + ")", lclCellStyleNormalArial10);
//                    if (chargecost.getApAmount() < 0.0) {
//                        createHeaderCell("$" + df.format(chargecost.getApAmount()), redLclCellStyleRightNormalArial10);
//                    } else {
//                        createHeaderCell("$" + df.format(chargecost.getApAmount()), cellStyleRightNormalArial10);
//                    }
//                } else {
//                    createEmptyCell();
//                }
//                resetColumnIndex();
//                createRow();
//            }
//        }
    }

    public void bookingDetails() throws Exception {
        String[] drNumbers = new LclUnitSsDAO().getWareHouseFileId(lclUnitSs.getId().toString());
        fileId = drNumbers[0];
        drNumber = drNumbers[1];

        if (drNumber != null) {
            bList = new ArrayList(Arrays.asList(drNumber.split(",")));// drNumber List
        }

        if (bList != null) {
            for (Object b : bList) {
                String subStr = b.toString().substring(4);
                Long fId = new LclFileNumberDAO().getFileIdByFileNumber(subStr);
                List<LclBookingPiece> listBookPiece = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", fId);
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

                        createHeaderCell(b.toString(), lclCellStyleLeftBoldArial10);
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
                            createHeaderCell("Final dest." + bookPiece.getLclFileNumber().getLclBooking().getFinalDestination().getUnLocationName(), lclCellStyleLeftBoldArial10);
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
                                stateName = bookPiece.getLclFileNumber().getLclBooking().getFinalDestination().getStateId().getCodedesc();
                            } else {
                                stateName = "";
                            }
                            createHeaderCell(bookPiece.getLclFileNumber().getLclBooking().getFinalDestination().getUnLocationName() + " ," + stateName, lclCellStyleLeftBoldArial10);
                        }
                        commonHeadingNoVendorDetails();
//                        List<LclImpProfitLossBean> chargeCostList = lclUnitSsDAO.getDRChargeDetails(bookPiece.getLclFileNumber().getId());
                        lclBookingAcList = new LclCostChargeDAO().findBybookingAcWarehouse(String.valueOf(bookPiece.getLclFileNumber().getId()));
                        if (CommonUtils.isNotEmpty(lclBookingAcList)) {
                            Double chargeTotal = 0.00;

                            resetColumnIndex();
                            createRow();
                            //Cahrge Code***********************************
                            for (int i = 0; i < lclBookingAcList.size(); i++) {
                                BookingChargesBean lclBookingA = (BookingChargesBean) lclBookingAcList.get(i);
                                code = CommonUtils.isNotEmpty(lclBookingA.getChargeCode()) ? lclBookingA.getChargeCode() : "";
                                codeDesc = genericCodeDAO.getGenericCodeDesc(code);
                                createHeaderCell(lclBookingA.getBlueScreencode(), cellStyleRightNormalArial10);
                                if (CommonUtils.isNotEmpty(codeDesc)) {
                                    createHeaderCell(codeDesc, cellStyleRightNormalArial10);
                                } else {
                                    createHeaderCell(code, cellStyleRightNormalArial10);

                                }
                                createCell();

                                createDollarCell(lclBookingA.getTotalAmt().doubleValue(), cellStyleRightNormalArial10);
                                chargeTotal = chargeTotal + lclBookingA.getTotalAmt().doubleValue();
                                resetColumnIndex();
                                createRow();
                            }

                            resetColumnIndex();
                            createRow();

                            createRow();
                            createHeaderCell("Total", lclCellStyleLeftBoldArial10);
                            createCell();
                            createCell();
                            if (chargeTotal < 0.0) {
                                createDollarCell(chargeTotal, redLclCellStyleRightNormalArial10);
                            } else {
                                createDollarCell(chargeTotal, cellStyleRightBoldArial10);
                            }
                            createCell();
                            createCell();
                            createCell();
                            createCell();
                            createCell();
                        }
                        resetColumnIndex();
                        createRow();
                        setCellBorder(lclCellStyleBoldArialBottomLine, (short) 0, (short) 0, (short) 2, (short) 0);
                        createHeaderCell("", lclCellStyleBoldArialBottomLine);
                        createHeaderCell("", lclCellStyleBoldArialBottomLine);
                        createHeaderCell("", lclCellStyleBoldArialBottomLine);
                        createHeaderCell("", lclCellStyleBoldArialBottomLine);
//                        setCellBorder(lclCellStyleBoldArialBottomRightLine, (short) 0, (short) 1, (short) 2, (short) 0);
                        createHeaderCell("", lclCellStyleBoldArialBottomLine);
                        createHeaderCell("", lclCellStyleBoldArialBottomLine);
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
//        createHeaderCell("Cost", lclCellStyleLeftBoldArial12);
        createCell();
        createCell();
//        createHeaderCell("Net", lclCellStyleLeftBoldArial12);
        createCell();
        resetColumnIndex();
        createRow();
        createRow();

        Double Total = 0.0d;
        if (fileId != null) {
            lclBookingAcList = new LclCostChargeDAO().findBybookingAcWarehouse(fileId);
            for (int i = 0; i < lclBookingAcList.size(); i++) {
                BookingChargesBean lclBookingA = (BookingChargesBean) lclBookingAcList.get(i);
                code = CommonUtils.isNotEmpty(lclBookingA.getChargeCode()) ? lclBookingA.getChargeCode() : "";
                codeDesc = genericCodeDAO.getGenericCodeDesc(code);
                createHeaderCell(lclBookingA.getBlueScreencode(), cellStyleRightNormalArial10);
                if (CommonUtils.isNotEmpty(codeDesc)) {
                    createHeaderCell(codeDesc, cellStyleRightNormalArial10);
                } else {
                    createHeaderCell(code, cellStyleRightNormalArial10);

                }
                createCell();

                createDollarCell(lclBookingA.getTotalAmt().doubleValue(), cellStyleRightNormalArial10);
                Total = Total + lclBookingA.getTotalAmt().doubleValue();
                resetColumnIndex();
                createRow();
            }
            if (Total < 0.0) {
                createHeaderCell("Total", lclCellStyleLeftBoldArial10);
                createCell();
                createCell();
                createDollarCell(Total, redLclCellStyleRightNormalArial10);//negaive Amount
            } else {
                createHeaderCell("Total", lclCellStyleLeftBoldArial10);
                createCell();
                createCell();
                createDollarCell(Total, cellStyleRightBoldArial10);
            }
        }
    }
}
