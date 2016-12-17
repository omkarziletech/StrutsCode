/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.lcl.report;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclBookingImportAms;
import com.gp.cong.logisoft.domain.lcl.LclBookingPieceUnit;
import com.gp.cong.logisoft.domain.lcl.LclSsDetail;
import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.domain.lcl.LclUnitSsImports;
import com.gp.cong.logisoft.domain.lcl.LclUnitSsManifest;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingImportAmsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsDetailDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsHeaderDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsImportsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsManifestDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitWhseDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.excel.BaseExcelCreator;
import java.io.File;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author i3
 */
public class LclImportCFSExcelCreator extends BaseExcelCreator {

    private String CFS01 = "";

    public LclImportCFSExcelCreator() {
    }

    private void writeContent(String unitSsId) throws Exception {
//        String name = "";
        String to = "";
        String freightLocation = "";
        String city = "";
        String state = "";
        String address = "";
        String phone = "";
        String fax = "";
        String size = "";
        String masterBl = "";
        StringBuilder importFileNumber = new StringBuilder();
        String steamShip = "";
        String vessel = "";
        String pol = "";
        String itNo = "";
        String unitNo = "";
        Double packagesTotal = 0.00;
        Double commodityTotal = 0.00;
        Double amsTotal = 0.00;
        Double totalWeight = 0.00;
        String prepaidCollect = "";
        String remarks = "";
        String sealNo = "";
        String companyName = "";
        LclUnitSs lclUnitSs = new LclUnitSsDAO().findById(Long.parseLong(unitSsId));
        List<LclBookingPieceUnit> lclBookingPieceUnitsList = lclUnitSs.getLclBookingPieceUnitList();
        LclSsHeader lclSsHeader = lclUnitSs.getLclSsHeader();
        sealNo = new LclUnitWhseDAO().getLCLUnitWhseSeal(lclUnitSs.getLclUnit().getId(), lclUnitSs.getLclSsHeader().getId());
        RefTerminal terminal = new RefTerminalDAO().getByProperty("trmnum", lclUnitSs.getLclSsHeader().getBillingTerminal().getTrmnum());
        if ((null != lclUnitSs && null != lclUnitSs.getLclSsHeader() && !lclUnitSs.getLclSsHeader().getLclUnitSsImportsList().isEmpty())
                && null != lclUnitSs.getLclSsHeader().getLclUnitSsImportsList().get(0).getOriginAcctNo().getAccountno()) {
            String brand = new TradingPartnerDAO().getBusinessUnit(lclUnitSs.getLclSsHeader().getLclUnitSsImportsList().get(0).getOriginAcctNo().getAccountno());
            if (CommonUtils.isNotEmpty(brand)) {
                companyName = LoadLogisoftProperties.getProperty("ECI".equalsIgnoreCase(brand) ? "application.Econo.companyname"
                        : "OTI".equalsIgnoreCase(brand) ? "application.OTI.companyname" : "application.ECU.companyname");
            }
        }
//        if (terminal != null && terminal.getTrmnam() != null) {
//            name = terminal.getTrmnam();
//        }
        if (terminal != null && terminal.getAddres1() != null) {
            address = terminal.getAddres1();
        }
        if (terminal != null && terminal.getPhnnum1() != null) {
            phone = terminal.getPhnnum1();
        }
        if (terminal != null && terminal.getFaxnum1() != null) {
            fax = terminal.getFaxnum1();
        }
        if (terminal != null && terminal.getCity1() != null) {
            city = terminal.getCity1();
        }
        if (terminal != null && terminal.getState() != null) {
            state = terminal.getState();
        }
        if (lclUnitSs != null && lclUnitSs.getLclUnit() != null && lclUnitSs.getLclUnit().getUnitNo() != null) {
            unitNo = lclUnitSs.getLclUnit().getUnitNo();
        }
        if (lclUnitSs != null && lclUnitSs.getLclUnit() != null && lclUnitSs.getLclUnit().getUnitType() != null && lclUnitSs.getLclUnit().getUnitType().getDescription() != null) {
            size = lclUnitSs.getLclUnit().getUnitType().getDescription().toUpperCase();
        }
        LclUnitSsManifest lclUnitSsManifest = new LclUnitSsManifestDAO().getLclUnitSSManifestByHeader(lclUnitSs.getLclUnit().getId(), lclUnitSs.getLclSsHeader().getId());
        if (lclUnitSsManifest != null && lclUnitSsManifest.getMasterbl() != null) {
            masterBl = lclUnitSsManifest.getMasterbl().toUpperCase();
        }
        LclSsDetail lclSsDetail = lclSsHeader.getVesselSsDetail();
        if (lclSsDetail != null && lclSsDetail.getSpReferenceName() != null) {
            vessel = lclSsDetail.getSpReferenceName();
        }
        if (lclSsDetail != null && lclSsDetail.getSpAcctNo() != null && lclSsDetail.getSpAcctNo().getAccountName() != null) {
            steamShip = lclSsDetail.getSpAcctNo().getAccountName();
        }
        if (lclSsHeader != null && lclSsHeader.getOrigin().getUnLocationName() != null) {
            pol = lclSsHeader.getOrigin().getUnLocationName();
        }
        if (terminal != null && terminal.getTrmnum() != null) {
            importFileNumber.append(terminal.getTrmnum());
        }
        if (lclSsHeader != null && lclSsHeader.getOrigin() != null && lclSsHeader.getOrigin().getUnLocationCode() != null) {
            importFileNumber.append("-").append(lclSsHeader.getOrigin().getUnLocationCode());
        }
        if (lclSsHeader != null && lclSsHeader.getDestination() != null && lclSsHeader.getDestination().getUnLocationCode() != null) {
            importFileNumber.append("-").append(lclSsHeader.getDestination().getUnLocationCode());
        }
        if (lclSsHeader != null && lclSsHeader.getScheduleNo() != null) {
            importFileNumber.append("-").append(lclSsHeader.getScheduleNo());
        }
        if (lclUnitSs != null) {
            //prepaidCollect = new LclUnitSsDAO().getPrepaidCollect(lclUnitSs.getSpBookingNo());
            if (lclUnitSs.getPrepaidCollect() != null && !lclUnitSs.getPrepaidCollect().equalsIgnoreCase("")
                    && lclUnitSs.getPrepaidCollect().equalsIgnoreCase("P")) {
                prepaidCollect = "PREPAID";
            } else if (lclUnitSs.getPrepaidCollect() != null && !lclUnitSs.getPrepaidCollect().equalsIgnoreCase("")
                    && lclUnitSs.getPrepaidCollect().equalsIgnoreCase("C")) {
                prepaidCollect = "COLLECT";
            }
        } else {
            prepaidCollect = "PREPAID";
        }
        LclUnitSsImports lclUnitSsImport = new LclUnitSsImportsDAO().getLclUnitSSImportsByHeader(lclUnitSs.getLclUnit().getId(), lclUnitSs.getLclSsHeader().getId());
        if (lclUnitSsImport != null && lclUnitSsImport.getCfsWarehouseId() != null) {
            to = lclUnitSsImport.getCfsWarehouseId().getWarehouseName();
        }
        if (lclUnitSsImport != null && lclUnitSsImport.getUnitWareHouseId() != null) {
            freightLocation = lclUnitSsImport.getUnitWareHouseId().getWarehouseName();
        }
        createRow();
        row.setHeightInPoints(15f);
        resetColumnIndex();
        createHeaderCell(companyName.toUpperCase(), lclCellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 0, 2);
        createRow();
        row.setHeightInPoints(15f);
        resetColumnIndex();
        createHeaderCell(address.toUpperCase(), lclCellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 0, 2);
        createRow();
        createRow();
        row.setHeightInPoints(15f);
        resetColumnIndex();
        createHeaderCell(city.toUpperCase() + "," + state.toUpperCase(), lclCellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 0, 1);

        createRow();
        row.setHeightInPoints(15f);
        resetColumnIndex();
        createHeaderCell("Telephone: " + phone, lclCellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 0, 3);
        createRow();
        row.setHeightInPoints(15f);
        resetColumnIndex();
        createHeaderCell("Fax: " + fax, lclCellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 0, 2);
        createRow();
        row.setHeightInPoints(15f);
        resetColumnIndex();
        createHeaderCell("Contact:", lclCellStyleLeftNormal);

        createRow();
        createRow();
        createRow();
        row.setHeightInPoints(15f);
        resetColumnIndex();
        createHeaderCell("*** " + prepaidCollect + " ***", lclCellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 0, 1);

        createRow();
        createRow();
        createRow();
        resetColumnIndex();
        row.setHeightInPoints(15f);
        createHeaderCell("Date", lclCellStyleLeftNormal);
        createTextCell(DateUtils.formatStringDateToAppFormatMMM(new Date()), lclCellStyleLeftNormal);

        createRow();
        resetColumnIndex();
        row.setHeightInPoints(15f);
        createHeaderCell("To", lclCellStyleLeftNormal);
        createTextCell(to, lclCellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 1, 2);

        createRow();
        row.setHeightInPoints(15f);
        resetColumnIndex();
        createHeaderCell("Att", lclCellStyleLeftNormal);
        createTextCell("Import", lclCellStyleLeftNormal);

        createRow();
        row.setHeightInPoints(15f);
        resetColumnIndex();
        createHeaderCell("Vessel", lclCellStyleLeftNormal);
        createTextCell(vessel, lclCellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 1, 2);

        createRow();
        row.setHeightInPoints(15f);
        resetColumnIndex();
        createHeaderCell("Port of Loading", lclCellStyleLeftNormal);
        createTextCell(pol, lclCellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 1, 2);

        createRow();
        row.setHeightInPoints(15f);
        resetColumnIndex();
        createHeaderCell("Stemship Line", lclCellStyleLeftNormal);
        createTextCell(steamShip, lclCellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 1, 2);

        createRow();
        row.setHeightInPoints(15f);
        resetColumnIndex();
        createHeaderCell("Freight Location", lclCellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 0, 1);
        createTextCell(freightLocation, lclCellStyleLeftNormal);

        createRow();
        row.setHeightInPoints(15f);
        resetColumnIndex();
        createHeaderCell("Import File #", lclCellStyleLeftNormal);
        createTextCell(importFileNumber.toString().toUpperCase(), lclCellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 1, 2);

        createRow();
        row.setHeightInPoints(15f);
        resetColumnIndex();
        createHeaderCell("ETA", lclCellStyleLeftNormal);
        if (lclSsDetail != null && lclSsDetail.getSta() != null) {
            createTextCell(DateUtils.formatStringDateToAppFormatMMM(lclSsDetail.getSta()), lclCellStyleLeftNormal);
        } else {
            createTextCell("", lclCellStyleLeftNormal);
        }

        createRow();
        row.setHeightInPoints(15f);
        resetColumnIndex();
        createHeaderCell("Master B/L", lclCellStyleLeftNormal);
        createTextCell(masterBl, lclCellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 1, 2);

        createRow();
        row.setHeightInPoints(15f);
        resetColumnIndex();
        createHeaderCell("Container", lclCellStyleLeftNormal);
        createTextCell(unitNo, lclCellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 1, 2);

        createRow();
        row.setHeightInPoints(15f);
        resetColumnIndex();
        createHeaderCell("Seal #", lclCellStyleLeftNormal);
        createTextCell(sealNo, lclCellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 1, 2);

        createRow();
        row.setHeightInPoints(15f);
        resetColumnIndex();
        createHeaderCell("Size", lclCellStyleLeftNormal);
        createTextCell(size, lclCellStyleLeftNormal);

        createRow();
        row.setHeightInPoints(15f);
        resetColumnIndex();
        createHeaderCell("Hazardous", lclCellStyleLeftNormal);
        if (lclUnitSs != null && lclUnitSs.getLclUnit() != null && lclUnitSs.getLclUnit().getHazmatPermitted()) {
            createTextCell("YES", lclCellStyleLeftNormal);
        } else {
            createTextCell("NO", lclCellStyleLeftNormal);
        }
        createRow();
        createRow();
        createRow();
        row.setHeightInPoints(15f);
        resetColumnIndex();
        createHeaderCell("ECI Ref #", lclCellStyleLeftNormal);
        createHeaderCell("Bill Of Lading", lclCellStyleLeftNormal);
        createHeaderCell("AMS #", lclCellStyleLeftNormal);
        createHeaderCell("Packages", lclCellStyleLeftNormal);
        createHeaderCell("Weight/KGS", lclCellStyleLeftNormal);
        createHeaderCell("Measurement", lclCellStyleLeftNormal);
        createHeaderCell("Destination", lclCellStyleLeftNormal);
        createHeaderCell("IT #", lclCellStyleLeftNormal);
        createHeaderCell("Remarks", lclCellStyleLeftNormal);
        if (lclBookingPieceUnitsList != null && lclBookingPieceUnitsList.size() > 0) {
            for (int i = 0; i < lclBookingPieceUnitsList.size(); i++) {
                boolean amsFlag = false;
                String amsNo = "";
                int packages = 0;
                Double measure = 0.00;
                Double weight = 0.00;
                LclBookingPieceUnit lclBookingPieceUnit = (LclBookingPieceUnit) lclBookingPieceUnitsList.get(i);
                if (lclBookingPieceUnit.getLclBookingPiece() != null && lclBookingPieceUnit.getLclBookingPiece().getActualVolumeMetric() != null && lclBookingPieceUnit.getLclBookingPiece().getActualVolumeMetric().doubleValue() != 0.00) {
                    measure = lclBookingPieceUnit.getLclBookingPiece().getActualVolumeMetric().doubleValue();
                } else if (lclBookingPieceUnit.getLclBookingPiece() != null && lclBookingPieceUnit.getLclBookingPiece().getBookedVolumeMetric() != null && lclBookingPieceUnit.getLclBookingPiece().getBookedVolumeMetric().doubleValue() != 0.00) {
                    measure = lclBookingPieceUnit.getLclBookingPiece().getBookedVolumeMetric().doubleValue();
                }
                if (lclBookingPieceUnit.getLclBookingPiece() != null && lclBookingPieceUnit.getLclBookingPiece().getActualWeightMetric() != null && lclBookingPieceUnit.getLclBookingPiece().getActualWeightMetric().doubleValue() != 0.00) {
                    weight = lclBookingPieceUnit.getLclBookingPiece().getActualWeightMetric().doubleValue();
                } else if (lclBookingPieceUnit.getLclBookingPiece() != null && lclBookingPieceUnit.getLclBookingPiece().getBookedWeightMetric() != null && lclBookingPieceUnit.getLclBookingPiece().getBookedWeightMetric().doubleValue() != 0.00) {
                    weight = lclBookingPieceUnit.getLclBookingPiece().getBookedWeightMetric().doubleValue();
                }
                LclBooking lclBooking = new LCLBookingDAO().findById(lclBookingPieceUnit.getLclBookingPiece().getLclFileNumber().getId());
                remarks = new LclRemarksDAO().getLclRemarksByTypeSQL((String) (lclBooking != null && lclBooking.getLclFileNumber() != null && lclBooking.getLclFileNumber().getId() != null ? lclBooking.getLclFileNumber().getId().toString() : ""), "Loading Remarks");
                List<LclBookingImportAms> lclBookingImportAmsList = new LclBookingImportAmsDAO().findByProperty("lclFileNumber.id", lclBooking != null && lclBooking.getLclFileNumber() != null && lclBooking.getLclFileNumber().getId() != null ? lclBooking.getLclFileNumber().getId() : 0);
                Collections.reverse(lclBookingImportAmsList);
                if (lclBookingImportAmsList != null && lclBookingImportAmsList.size() > 1) {
                    int count = 0;
                    for (int j = 0; j < lclBookingImportAmsList.size(); j++) {
                        measure = 0.00;
                        weight = 0.00;
                        if (lclBookingPieceUnit.getLclBookingPiece() != null && lclBookingPieceUnit.getLclBookingPiece().getActualVolumeMetric() != null && lclBookingPieceUnit.getLclBookingPiece().getActualVolumeMetric().doubleValue() != 0.00) {
                            measure = lclBookingPieceUnit.getLclBookingPiece().getActualVolumeMetric().doubleValue();
                        } else if (lclBookingPieceUnit.getLclBookingPiece() != null && lclBookingPieceUnit.getLclBookingPiece().getBookedVolumeMetric() != null && lclBookingPieceUnit.getLclBookingPiece().getBookedVolumeMetric().doubleValue() != 0.00) {
                            measure = lclBookingPieceUnit.getLclBookingPiece().getBookedVolumeMetric().doubleValue();
                        }
                        if (lclBookingPieceUnit.getLclBookingPiece() != null && lclBookingPieceUnit.getLclBookingPiece().getActualWeightMetric() != null && lclBookingPieceUnit.getLclBookingPiece().getActualWeightMetric().doubleValue() != 0.00) {
                            weight = lclBookingPieceUnit.getLclBookingPiece().getActualWeightMetric().doubleValue();
                        } else if (lclBookingPieceUnit.getLclBookingPiece() != null && lclBookingPieceUnit.getLclBookingPiece().getBookedWeightMetric() != null && lclBookingPieceUnit.getLclBookingPiece().getBookedWeightMetric().doubleValue() != 0.00) {
                            weight = lclBookingPieceUnit.getLclBookingPiece().getBookedWeightMetric().doubleValue();
                        }
                        LclBookingImportAms lclBookingImportAms = (LclBookingImportAms) lclBookingImportAmsList.get(j);
                        if (lclBookingImportAmsList.size() == 1) {
                            if (CommonUtils.isNotEmpty(lclBookingPieceUnit.getLclBookingPiece().getActualPieceCount())) {
                                packages = lclBookingPieceUnit.getLclBookingPiece().getActualPieceCount();
                            } else if (lclBookingPieceUnit.getLclBookingPiece() != null && lclBookingPieceUnit.getLclBookingPiece().getBookedPieceCount() != null) {
                                packages = lclBookingPieceUnit.getLclBookingPiece().getBookedPieceCount();
                            }
                        } else {
                            packages = lclBookingImportAms.getPieces();
                        }
                        if (CommonFunctions.isNotNull(lclBookingImportAms.getAmsNo())) {
                            if (count == 0) {
                                if (lclBookingImportAms.getScac() != null) {
                                    amsNo = lclBookingImportAms.getScac() + lclBookingImportAms.getAmsNo();
                                } else {
                                    amsNo = lclBookingImportAms.getAmsNo();
                                }
                            } else {
                                if (lclBookingImportAms.getScac() != null) {
                                    amsNo = lclBookingImportAms.getScac() + lclBookingImportAms.getAmsNo();
                                } else {
                                    amsNo = lclBookingImportAms.getAmsNo();
                                }
                            }
                            if (CommonUtils.isNotEmpty(lclBookingImportAms.getPieces())) {
                                amsTotal = amsTotal + lclBookingImportAms.getPieces();
                            }
                            amsFlag = true;
                            int rowCount = 0;
                            rowCount++;
                            createRow();
                            row.setHeightInPoints(15f);
                            resetColumnIndex();
                            createTextCell("IMP-" + lclBookingPieceUnit.getLclBookingPiece().getLclFileNumber().getFileNumber(), lclCellStyleLeftNormal);
                            if (lclBooking.getLclFileNumber().getLclBookingImport() != null && lclBooking.getLclFileNumber().getLclBookingImport().getSubHouseBl() != null) {
                                createTextCell(lclBooking.getLclFileNumber().getLclBookingImport().getSubHouseBl(), lclCellStyleLeftNormal);
                            } else {
                                createTextCell("", lclCellStyleLeftNormal);
                            }
                            createTextCell(amsNo.toUpperCase(), lclCellStyleLeftNormal);
                            createIntegerCell(packages, lclCellStyleLeftNormal);
                            createDoubleCell(weight, lclCellStyleLeftNormal);
                            createDoubleCell(measure, lclCellStyleLeftNormal);
                            if (lclBooking != null && lclBooking.getFinalDestination() != null && lclBooking.getFinalDestination().getUnLocationName() != null && lclBooking.getFinalDestination().getStateId() != null && lclBooking.getFinalDestination().getStateId().getCode() != null) {
                                createTextCell(lclBooking.getFinalDestination().getUnLocationName() + "," + lclBooking.getFinalDestination().getStateId().getCode(), lclCellStyleLeftNormal);
                            } else {
                                createTextCell("", lclCellStyleLeftNormal);
                            }
                            createTextCell(itNo, lclCellStyleLeftNormal);
                            createTextCell(remarks, lclCellStyleLeftNormal);

                        }
                        count++;
                    }
                }
//                if (lclBookingPieceUnit.getLclBookingPiece().getActualWeightMetric() != null && lclBookingPieceUnit.getLclBookingPiece().getActualWeightMetric().doubleValue() != 0.00) {
//                    weight = lclBookingPieceUnit.getLclBookingPiece().getActualWeightMetric().doubleValue();
//                } else if (lclBookingPieceUnit.getLclBookingPiece().getBookedWeightMetric() != null && lclBookingPieceUnit.getLclBookingPiece().getBookedWeightMetric().doubleValue() != 0.00) {
//                    weight = lclBookingPieceUnit.getLclBookingPiece().getBookedWeightMetric().doubleValue();
//                }
                if (lclBookingPieceUnit.getLclBookingPiece() != null && lclBookingPieceUnit.getLclBookingPiece().getActualWeightMetric() != null && lclBookingPieceUnit.getLclBookingPiece().getActualWeightMetric().doubleValue() != 0.00) {
                    totalWeight = totalWeight + lclBookingPieceUnit.getLclBookingPiece().getActualWeightMetric().doubleValue();
                } else if (lclBookingPieceUnit.getLclBookingPiece() != null && lclBookingPieceUnit.getLclBookingPiece().getBookedWeightMetric() != null && lclBookingPieceUnit.getLclBookingPiece().getBookedWeightMetric().doubleValue() != 0.00) {
                    totalWeight = totalWeight + lclBookingPieceUnit.getLclBookingPiece().getBookedWeightMetric().doubleValue();
                }
//                if (lclBookingPieceUnit.getLclBookingPiece().getActualVolumeMetric() != null && lclBookingPieceUnit.getLclBookingPiece().getActualVolumeMetric().doubleValue() != 0.00) {
//                    measure = lclBookingPieceUnit.getLclBookingPiece().getActualVolumeMetric().doubleValue();
//                } else if (lclBookingPieceUnit.getLclBookingPiece().getBookedVolumeMetric() != null && lclBookingPieceUnit.getLclBookingPiece().getBookedVolumeMetric().doubleValue() != 0.00) {
//                    measure = lclBookingPieceUnit.getLclBookingPiece().getBookedVolumeMetric().doubleValue();
//                }
                if (lclUnitSsImport != null && CommonUtils.isNotEmpty(lclUnitSsImport.getItNo())) {
                    itNo = lclUnitSsImport.getItNo();
                }
                if (!amsFlag) {
                    int rowCount = 0;
                    rowCount++;
                    createRow();
                    row.setHeightInPoints(15f);
                    resetColumnIndex();
                    if (lclBookingImportAmsList != null && lclBookingImportAmsList.size() == 1) {
                        LclBookingImportAms lclBookingImportAms = new LclBookingImportAmsDAO().getByProperty("lclFileNumber.id", lclBooking.getLclFileNumber().getId());
                        if (lclBookingImportAms != null) {
                            if (lclBookingImportAms.getScac() != null) {
                                amsNo = lclBookingImportAms.getScac() + lclBookingImportAms.getAmsNo();
                            } else {
                                amsNo = lclBookingImportAms.getAmsNo();
                            }
                        }
                    }
                    createTextCell("IMP-" + lclBookingPieceUnit.getLclBookingPiece().getLclFileNumber().getFileNumber(), lclCellStyleLeftNormal);
                    if (lclBooking != null && lclBooking.getLclFileNumber() != null && lclBooking.getLclFileNumber().getLclBookingImport() != null && lclBooking.getLclFileNumber().getLclBookingImport().getSubHouseBl() != null) {
                        createTextCell(lclBooking.getLclFileNumber().getLclBookingImport().getSubHouseBl().toUpperCase(), lclCellStyleLeftNormal);
                    } else {
                        createTextCell("", lclCellStyleLeftNormal);
                    }
                    createTextCell(amsNo.toUpperCase(), lclCellStyleLeftNormal);
                    if (CommonUtils.isNotEmpty(lclBookingPieceUnit.getLclBookingPiece().getActualPieceCount())) {
                        packages = lclBookingPieceUnit.getLclBookingPiece().getActualPieceCount();
                    } else if (lclBookingPieceUnit.getLclBookingPiece().getBookedPieceCount() != null) {
                        packages = lclBookingPieceUnit.getLclBookingPiece().getBookedPieceCount();
                    }
                    if (CommonUtils.isNotEmpty(lclBookingPieceUnit.getLclBookingPiece().getActualPieceCount())) {
                        commodityTotal = commodityTotal + lclBookingPieceUnit.getLclBookingPiece().getActualPieceCount().doubleValue();
                    } else if (lclBookingPieceUnit.getLclBookingPiece().getBookedPieceCount() != null) {
                        commodityTotal = commodityTotal + lclBookingPieceUnit.getLclBookingPiece().getBookedPieceCount();
                    }
                    createIntegerCell(packages, lclCellStyleLeftNormal);
                    createDoubleCell(weight, lclCellStyleLeftNormal);
                    createDoubleCell(measure, lclCellStyleLeftNormal);
                    if (lclBooking != null && lclBooking.getFinalDestination() != null && lclBooking.getFinalDestination().getUnLocationName() != null && lclBooking.getFinalDestination().getStateId() != null && lclBooking.getFinalDestination().getStateId().getCode() != null) {
                        createTextCell(lclBooking.getFinalDestination().getUnLocationName() + "," + lclBooking.getFinalDestination().getStateId().getCode(), lclCellStyleLeftNormal);
                    } else {
                        createTextCell("", lclCellStyleLeftNormal);
                    }
                    createTextCell(itNo, lclCellStyleLeftNormal);
                    createTextCell(remarks, lclCellStyleLeftNormal);
                }
            }
            packagesTotal = amsTotal + commodityTotal;
            createRow();
            createRow();
            createRow();
            row.setHeightInPoints(15f);
            resetColumnIndex();
            createHeaderCell("***DO not Segregate***", lclCellStyleLeftNormal);
            mergeCells(rowIndex, rowIndex, 0, 2);
            createRow();
            row.setHeightInPoints(15f);
            resetColumnIndex();
            createTextCell("Total Packages", lclCellStyleLeftNormal);
            createDoubleCell(packagesTotal, lclCellStyleLeftNormal);
            createRow();
            row.setHeightInPoints(15f);
            resetColumnIndex();
            createTextCell("Total Weight", lclCellStyleLeftNormal);
            createDoubleCell(totalWeight, lclCellStyleLeftNormal);
        }
        createRow();
        createRow();
        createRow();
        row.setHeightInPoints(15f);
        resetColumnIndex();
        createHeaderCell("Special Instructions", lclCellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 0, 1);
        Iterator bookingCommentsIterator = new GenericCodeDAO().getLclPrintComments(39, "CFS");
        while (bookingCommentsIterator.hasNext()) {
            Object[] row = (Object[]) bookingCommentsIterator.next();
            String code = (String) row[0];
            String codeDesc = (String) row[1];
            if (code != null) {
                if ("CFS01".equalsIgnoreCase(code)) {
                    CFS01 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                    String[] cfs = CFS01.split("\n");
                    for (int i = 0; i < cfs.length; i++) {
                        createRow();
                        resetColumnIndex();
                        createHeaderCell(cfs[i], lclCellStyleLeftNormal);
                        mergeCells(rowIndex, rowIndex, 0, 7);
                    }
                }
            }
        }
        createRow();
        row.setHeightInPoints(15f);
        createRow();
        row.setHeightInPoints(15f);
        createRow();
        row.setHeightInPoints(15f);
        resetColumnIndex();
        createHeaderCell("If you have any questions,please contact", lclCellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 0, 3);
        createRow();
        row.setHeightInPoints(15f);
        resetColumnIndex();
        createHeaderCell("Thank you for your assistance", lclCellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 0, 2);
    }

    public void create(String reportLocation, String unitSsId) throws Exception {
        try {
            init(reportLocation, "CFSInstructions");
            writeContent(unitSsId);
            writeIntoFile();
        } catch (Exception e) {
            throw e;
        } finally {
            exit();
        }
    }
}
