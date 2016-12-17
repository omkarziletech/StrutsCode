/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.lcl.report;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclBookingImport;
import com.gp.cong.logisoft.domain.lcl.LclBookingImportAms;
import com.gp.cong.logisoft.domain.lcl.LclBookingPiece;
import com.gp.cong.logisoft.domain.lcl.LclBookingPieceUnit;
import com.gp.cong.logisoft.domain.lcl.LclBookingPieceWhse;
import com.gp.cong.logisoft.domain.lcl.LclContact;
import com.gp.cong.logisoft.domain.lcl.LclInbond;
import com.gp.cong.logisoft.domain.lcl.LclSsDetail;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.domain.lcl.LclUnitSsImports;
import com.gp.cong.logisoft.domain.lcl.LclUnitWhse;
import com.gp.cong.logisoft.domain.lcl.LclUnitSsManifest;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsManifestDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLContactDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingImportAmsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingImportDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclInbondsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.gp.cvst.logisoft.hibernate.dao.CustAddressDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.CustAddress;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Shanmugam
 */
public class LCLImpDR extends LclReportFormatMethods {

    private Font normalFont = FontFactory.getFont("COURIER", 11f, Font.NORMAL);
    String companyName = "";

    public void createImportBLPdf(String realPath, String fileId, String fileNumber, String outputFileName, String documentName) throws Exception {
        document = new Document();
        document.setPageSize(PageSize.A4);
        document.setMargins(8, 8, 15, 15);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(outputFileName));
        document.open();
        String[] fileValues = new String[2];
        if (!fileId.equals("")) {
            fileValues = new LclUnitSsDAO().getTotalBkgInUnits(fileId);
        }
        if (fileValues == null || fileValues[0] == null || fileValues[0] == "") {
            fileValues[0] = fileId;
        }
        String[] sFileId = fileValues[0].split(",");
        for (int i = 0; i < sFileId.length; i++) {
            LclUnitSs lclUnitSs = null;
            StringBuilder whseDetails = new StringBuilder();
            StringBuilder deliveryDetails = new StringBuilder();
            StringBuilder origin = new StringBuilder();
            StringBuilder destination = new StringBuilder();
            StringBuilder podValues = new StringBuilder();
            StringBuilder conFileNumber = new StringBuilder();
            StringBuilder itDetails = new StringBuilder();
            StringBuilder consigneeDetails = new StringBuilder();
            StringBuilder agentDetails = new StringBuilder();
            StringBuilder vesselName = new StringBuilder();
            Integer pieceCount = 0;
            Double cube = 0.00;
            Double weight = 0.00;
            Double cbm = 0.00;
            Double kilos = 0.00;
            String whsePhone = "";
            String whseFax = "";
            String arrivalDate = "";
            String strippingDate = "";
            String deliveryPhone = "";
            String deliveryFax = "";
            String unitsNumber = "";
            String bookingNumber = "";
            String subHouseBL = "";
            String terminalNo = "";
            String externalComments = "";
            String whseLoc = "";
            String ambHouseBL = "";
            String commodity = "";
            String commodityDesc = "";
            String marks = "";
            String packType = "";
            String warehouseDetails = "";
            LclBooking lclbooking = new LCLBookingDAO().getByProperty("lclFileNumber.id", Long.parseLong(sFileId[i]));
            LclContact lclContact = new LclContact();
            LCLContactDAO lclContactDAO = new LCLContactDAO();
            externalComments = new LclRemarksDAO().getLclRemarksByTypeSQL(lclbooking.getLclFileNumber().getId().toString(), "E");
            List<LclBookingImportAms> lclBookingImportAmsList = new LclBookingImportAmsDAO().findByProperty("lclFileNumber.id", lclbooking.getLclFileNumber().getId());
            Collections.sort(lclBookingImportAmsList, new Comparator<LclBookingImportAms>() {
                public int compare(LclBookingImportAms o1, LclBookingImportAms o2) {
                    return o2.getId().compareTo(o1.getId());
                }
            });
            if (lclbooking != null && lclbooking.getLclFileNumber() != null && lclbooking.getLclFileNumber().getBusinessUnit() != null) {
                if ("ECI".equalsIgnoreCase(lclbooking.getLclFileNumber().getBusinessUnit())) {
                    companyName = LoadLogisoftProperties.getProperty("application.Econo.companyname");
                } else if ("OTI".equalsIgnoreCase(lclbooking.getLclFileNumber().getBusinessUnit())) {
                    companyName = LoadLogisoftProperties.getProperty("application.OTI.companyname");
                } else {
                    companyName = LoadLogisoftProperties.getProperty("application.ECU.companyname");

                }
            }
            if (lclBookingImportAmsList != null && lclBookingImportAmsList.size() > 0) {
                for (int j = 0; j < lclBookingImportAmsList.size(); j++) {
                    LclBookingImportAms lclBookingImportAms = (LclBookingImportAms) lclBookingImportAmsList.get(j);
                    if (CommonFunctions.isNotNull(lclBookingImportAms.getAmsNo())) {
                        ambHouseBL = lclBookingImportAms.getAmsNo().toUpperCase();
                    }
                    break;
                }
            }
            if (CommonFunctions.isNotNull(lclbooking.getFinalDestination()) && CommonFunctions.isNotNull(lclbooking.getFinalDestination().getUnLocationCode())) {
                RefTerminal terminal = lclbooking.getTerminal();
                if (CommonFunctions.isNotNull(companyName)) {
                    deliveryDetails.append("     ").append(companyName).append("\n");
                }
                if (terminal != null && CommonFunctions.isNotNull(terminal.getAddres1())) {
                    deliveryDetails.append("     ").append(terminal.getAddres1()).append("\n");
                }
                if (terminal != null && CommonFunctions.isNotNull(terminal.getCity1())) {
                    deliveryDetails.append("     ").append(terminal.getCity1()).append(",");
                }
                if (terminal != null && CommonFunctions.isNotNull(terminal.getState())) {
                    deliveryDetails.append(terminal.getState()).append(".");
                }
                if (terminal != null && CommonFunctions.isNotNull(terminal.getZipcde())) {
                    deliveryDetails.append(terminal.getZipcde()).append("\n");
                }
                if (terminal != null && CommonFunctions.isNotNull(terminal.getPhnnum1())) {
                    String phoneNumber = terminal.getPhnnum1();
                    if (terminal.getPhnnum1().length() >= 10) {
                        String pNoSpaceRemove = StringUtils.remove(terminal.getPhnnum1(), " ");
                        String ph1 = pNoSpaceRemove.substring(0, 3);
                        String ph2 = pNoSpaceRemove.substring(3, 6);
                        String ph3 = pNoSpaceRemove.substring(6);
                        phoneNumber = "(" + ph1 + ") " + ph2 + "-" + ph3;
                    }
                    deliveryDetails.append("     ").append("Tel  " + phoneNumber + "   ");
                }
                if (terminal != null && CommonFunctions.isNotNull(terminal.getFaxnum1())) {
                    String faxNumber = terminal.getFaxnum1();
                    if (terminal.getFaxnum1().length() >= 10) {
                        String fNoSpaceRemove = StringUtils.remove(terminal.getFaxnum1(), "");
                        String fax1 = fNoSpaceRemove.substring(0, 3);
                        String fax2 = fNoSpaceRemove.substring(3, 6);
                        String fax3 = fNoSpaceRemove.substring(6);
                        faxNumber = "(" + fax1 + ")" + fax2 + "-" + fax3;
                    }
                    deliveryDetails.append("Fax  " + faxNumber);
                }
                if (terminal != null && CommonFunctions.isNotNull(terminal.getTrmnum())) {
                    terminalNo = terminal.getTrmnum();
                }
            }
            if (CommonFunctions.isNotNull(lclbooking.getTerminal()) && CommonFunctions.isNotNull(lclbooking.getTerminal().getTrmnum())) {
                conFileNumber.append(lclbooking.getTerminal().getTrmnum()).append("-");
            }
            LclBookingImport lclBookingImport = new LclBookingImportDAO().getByProperty("lclFileNumber.id", lclbooking.getLclFileNumber().getId());
            if (CommonFunctions.isNotNull(lclBookingImport) && CommonFunctions.isNotNull(lclBookingImport.getSubHouseBl())) {
                subHouseBL = lclBookingImport.getSubHouseBl();
            }
            if (CommonFunctions.isNotNull(lclbooking.getPortOfLoading()) && CommonFunctions.isNotNull(lclbooking.getPortOfLoading().getUnLocationCode())) {
                conFileNumber.append(lclbooking.getPortOfLoading().getUnLocationCode()).append("-");
            }
            if (CommonFunctions.isNotNull(lclbooking.getFinalDestination()) && CommonFunctions.isNotNull(lclbooking.getFinalDestination().getUnLocationCode())) {
                conFileNumber.append(lclbooking.getFinalDestination().getUnLocationCode()).append("-");
            }
            if (CommonFunctions.isNotNull(lclbooking)) {
                if (CommonFunctions.isNotNull(lclbooking.getPortOfLoading()) && CommonFunctions.isNotNull(lclbooking.getPortOfLoading().getUnLocationName())) {
                    origin.append("(" + lclbooking.getPortOfLoading().getUnLocationCode() + ")" + "  ").append(lclbooking.getPortOfLoading().getUnLocationName());
                }
                if (CommonFunctions.isNotNull(lclbooking.getPortOfLoading()) && CommonFunctions.isNotNull(lclbooking.getPortOfLoading().getCountryId()) && CommonFunctions.isNotNull(lclbooking.getPortOfLoading().getCountryId().getCodedesc()) && CommonFunctions.isNotNull(lclbooking.getPortOfLoading().getUnLocationCode())) {
                    origin.append(",").append(lclbooking.getPortOfLoading().getCountryId().getCodedesc());
                }
                if (CommonFunctions.isNotNull(lclbooking.getPortOfDestination()) && CommonFunctions.isNotNull(lclbooking.getPortOfDestination().getUnLocationName()) && CommonFunctions.isNotNull(lclbooking.getPortOfDestination().getUnLocationCode())) {
                    podValues.append("(" + lclbooking.getPortOfDestination().getUnLocationCode() + ")" + "  ").append(lclbooking.getPortOfDestination().getUnLocationName());
                }
                if (CommonFunctions.isNotNull(lclbooking.getPortOfDestination()) && CommonFunctions.isNotNull(lclbooking.getPortOfDestination().getStateId()) && CommonFunctions.isNotNull(lclbooking.getPortOfDestination().getStateId().getCode())) {
                    podValues.append(",").append(lclbooking.getPortOfDestination().getStateId().getCode());
                }
                if (CommonFunctions.isNotNull(lclbooking.getFinalDestination()) && CommonFunctions.isNotNull(lclbooking.getFinalDestination().getUnLocationName())) {
                    destination.append(lclbooking.getFinalDestination().getUnLocationName());
                }
                if (CommonFunctions.isNotNull(lclbooking.getFinalDestination()) && CommonFunctions.isNotNull(lclbooking.getFinalDestination().getStateId()) && CommonFunctions.isNotNull(lclbooking.getFinalDestination().getStateId().getCode())) {
                    destination.append(",").append(lclbooking.getFinalDestination().getStateId().getCode());
                }
                if (CommonFunctions.isNotNull(lclbooking.getAgentAcct()) && CommonFunctions.isNotNull(lclbooking.getAgentAcct().getAccountno())) {
                    CustAddress custDetails = new CustAddressDAO().getCustAddressForCheck(lclbooking.getAgentAcct().getAccountno());
                    if (custDetails != null) {
                        if (custDetails.getContactName() != null) {
                            agentDetails.append(custDetails.getAcctName()).append("\n");
                        }
                        if (custDetails.getAddress1() != null) {
                            agentDetails.append(custDetails.getAddress1());
                        }
                        if (custDetails.getCity1() != null && !custDetails.getCity1().equals("")) {
                            agentDetails.append(custDetails.getCity1()).append(",");
                        }
                        if (custDetails.getState() != null && !custDetails.getState().equals("")) {
                            agentDetails.append(custDetails.getState()).append(",");
                        }
                        if (custDetails.getZip() != null && custDetails.getZip() != "") {
                            agentDetails.append(custDetails.getZip()).append(".");
                        }
                    }
                }
            }

            List<LclBookingPiece> lclBookingPiecesList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", lclbooking.getLclFileNumber().getId());

            if (!lclBookingPiecesList.isEmpty()) {
                for (LclBookingPiece lclbookingPiece : lclBookingPiecesList) {
                    if (CommonUtils.isNotEmpty(lclbookingPiece.getBookedPieceCount())) {
                        pieceCount += lclbookingPiece.getBookedPieceCount();
                    } else if (CommonUtils.isNotEmpty(lclbookingPiece.getActualPieceCount())) {
                        pieceCount += lclbookingPiece.getActualPieceCount();
                    }
                    packType = lclbookingPiece.getPackageType().getAbbr01();
                    if (lclbookingPiece.getBookedVolumeImperial() != null && CommonUtils.isNotEmpty(lclbookingPiece.getBookedVolumeImperial().doubleValue())) {
                        cube += lclbookingPiece.getBookedVolumeImperial().doubleValue();
                    } else if (null != lclbookingPiece.getActualVolumeImperial() && CommonUtils.isNotEmpty(lclbookingPiece.getActualVolumeImperial().doubleValue())) {
                        cube += lclbookingPiece.getActualVolumeImperial().doubleValue();
                    }
                    if (lclbookingPiece.getBookedVolumeMetric() != null && CommonUtils.isNotEmpty(lclbookingPiece.getBookedVolumeMetric().doubleValue())) {
                        cbm += lclbookingPiece.getBookedVolumeMetric().doubleValue();
                    } else if (null != lclbookingPiece.getActualVolumeMetric() && CommonUtils.isNotEmpty(lclbookingPiece.getActualVolumeMetric().doubleValue())) {
                        cbm += lclbookingPiece.getActualVolumeMetric().doubleValue();
                    }
                    if (lclbookingPiece.getBookedWeightImperial() != null && CommonUtils.isNotEmpty(lclbookingPiece.getBookedWeightImperial().doubleValue())) {
                        weight += lclbookingPiece.getBookedWeightImperial().doubleValue();
                    } else if (null != lclbookingPiece.getActualWeightImperial() && CommonUtils.isNotEmpty(lclbookingPiece.getActualWeightImperial().doubleValue())) {
                        weight += lclbookingPiece.getActualWeightImperial().doubleValue();
                    }
                    if (lclbookingPiece.getBookedWeightMetric() != null && CommonUtils.isNotEmpty(lclbookingPiece.getBookedWeightMetric().doubleValue())) {
                        kilos += lclbookingPiece.getBookedWeightMetric().doubleValue();
                    } else if (null != lclbookingPiece.getActualWeightMetric() && CommonUtils.isNotEmpty(lclbookingPiece.getActualWeightMetric().doubleValue())) {
                        kilos += lclbookingPiece.getActualWeightMetric().doubleValue();
                    }
                    if(lclbookingPiece !=null && lclbookingPiece.getCommodityType() !=null && lclbookingPiece.getCommodityType().getDescEn() !=null){
                    commodity = lclbookingPiece.getCommodityType().getDescEn();
                    }
                    commodityDesc = lclbookingPiece.getPieceDesc() != null ? lclbookingPiece.getPieceDesc().toUpperCase() : lclbookingPiece.getPieceDesc();
                    marks = lclbookingPiece.getMarkNoDesc() != null ? lclbookingPiece.getMarkNoDesc().toUpperCase() : lclbookingPiece.getMarkNoDesc();
                }
                LclBookingPiece lclbookingPiece = lclBookingPiecesList.get(0);
                List<LclBookingPieceWhse> lclBookingPieceWhseList = lclbookingPiece.getLclBookingPieceWhseList();
                if (!lclBookingPieceWhseList.isEmpty()) {
                    Collections.reverse(lclBookingPieceWhseList);
                    LclBookingPieceWhse lclBookingPieceWhse = lclBookingPieceWhseList.get(0);
                    warehouseDetails = lclBookingPieceWhse.getWarehouse().getWarehouseNo();
                    if (lclBookingPieceWhse.getLocation() != null) {
                        warehouseDetails = warehouseDetails + " " + lclBookingPieceWhse.getLocation();
                    }
                }

                //   packType= lclbookingPiece.getPackageType().getAbbr01();
                // cube = lclbookingPiece.getBookedVolumeImperial();
                if (!lclbookingPiece.getLclBookingPieceUnitList().isEmpty()) {
                    LclBookingPieceUnit lclBookingPieceUnits = lclbookingPiece.getLclBookingPieceUnitList().get(0);
                    if (lclBookingPieceUnits != null) {
                        lclUnitSs = lclBookingPieceUnits.getLclUnitSs();
                        List<LclSsDetail> lclSsDetailList = lclUnitSs.getLclSsHeader().getLclSsDetailList();
                        if (lclSsDetailList != null && lclSsDetailList.size() > 0) {
                            for (Object lclSSDetail : lclSsDetailList) {
                                LclSsDetail lclssDetail = (LclSsDetail) lclSSDetail;
                                //pieces values
                                if (CommonFunctions.isNotNull(lclssDetail.getSpReferenceName())) {
                                    vesselName.append(lclssDetail.getSpReferenceName()).append("              ").append("V-").append(lclssDetail.getSpReferenceNo());
                                }
                            }
                        }
                        if (CommonFunctions.isNotNull(lclUnitSs.getLclSsHeader().getScheduleNo())) {
                            conFileNumber.append(lclUnitSs.getLclSsHeader().getScheduleNo());
                        }
                        if (CommonFunctions.isNotNull(lclUnitSs.getLclUnit().getUnitNo())) {
                            unitsNumber = lclUnitSs.getLclUnit().getUnitNo();
                        }
                        LclUnitSsManifest lclUnitSsManifest = new LclUnitSsManifestDAO().getLclUnitSSManifestByHeader(lclUnitSs.getLclUnit().getId(), lclUnitSs.getLclSsHeader().getId());
                        if (CommonFunctions.isNotNull(lclUnitSsManifest.getMasterbl())) {
                            bookingNumber = lclUnitSsManifest.getMasterbl();
                        }
                        List<LclInbond> lclInbondList = new LclInbondsDAO().executeQuery("from LclInbond where lclFileNumber.id= " + fileId + " order by id desc");
                        LclInbond lclInbond = null;
                        if (!lclInbondList.isEmpty()) {
                            lclInbond = lclInbondList.get(0);
                            if (lclInbond != null && CommonUtils.isEmpty(itDetails)) {
                                if (CommonFunctions.isNotNull(lclInbond.getInbondNo())) {
                                    itDetails.append(" ").append(lclInbond.getInbondNo()).append("  ");
                                }
                            }
                        } else {
                            if (!lclUnitSs.getLclUnit().getLclUnitSsImportsList().isEmpty() && CommonUtils.isEmpty(itDetails)) {
                                LclUnitSsImports lclUnitSsImport = lclUnitSs.getLclUnit().getLclUnitSsImportsList().get(0);
                                if (CommonFunctions.isNotNull(lclUnitSsImport.getItNo())) {
                                    itDetails.append(" ").append(lclUnitSsImport.getItNo()).append("  ");
                                }
                            }
                        }
                        if (CommonFunctions.isNotNull(lclbooking.getConsContact()) && CommonFunctions.isNotNull(lclbooking.getConsContact().getId())) {
                            lclContact = lclContactDAO.findById(lclbooking.getConsContact().getId());

                            if (CommonFunctions.isNotNull(lclContact)) {
                                consigneeDetails.append(lclbooking.getConsContact().getId() + "\n");
                                consigneeDetails.append(lclbooking.getConsContact().getAddress() + "\n");
                                if (CommonUtils.isNotEmpty(lclbooking.getConsContact().getCity())) {
                                    consigneeDetails.append(lclbooking.getConsContact().getCity()).append(" ,");
                                }
                                consigneeDetails.append(lclbooking.getConsContact().getState()).append(",");
                                consigneeDetails.append(lclbooking.getConsContact().getZip());
                            }
                        }
                        if (lclBookingPieceUnits.getLclUnitSs().getLclUnit().getLclUnitWhseList() != null && lclBookingPieceUnits.getLclUnitSs().getLclUnit().getLclUnitWhseList().size() > 0) {
                            for (int j = 0; j < lclBookingPieceUnits.getLclUnitSs().getLclUnit().getLclUnitWhseList().size(); j++) {
                                LclUnitWhse lclUnitWhse = (LclUnitWhse) lclBookingPieceUnits.getLclUnitSs().getLclUnit().getLclUnitWhseList().get(0);
                                if (CommonFunctions.isNotNull(lclUnitWhse.getWarehouse()) && CommonFunctions.isNotNull(lclUnitWhse.getWarehouse().getWarehouseNo())) {
                                    whseDetails.append(lclUnitWhse.getWarehouse().getWarehouseNo()).append("-").append(" ");
                                }
                                if (CommonFunctions.isNotNull(lclUnitWhse.getWarehouse()) && CommonFunctions.isNotNull(lclUnitWhse.getWarehouse().getWarehouseName())) {
                                    whseLoc = lclUnitWhse.getWarehouse().getWarehouseNo();
                                }
                                if (CommonFunctions.isNotNull(lclUnitWhse.getWarehouse()) && CommonFunctions.isNotNull(lclUnitWhse.getWarehouse().getWarehouseName())) {
                                    whseDetails.append(lclUnitWhse.getWarehouse().getWarehouseName()).append("\n");
                                }
                                if (CommonFunctions.isNotNull(lclUnitWhse.getWarehouse()) && CommonFunctions.isNotNull(lclUnitWhse.getWarehouse().getAddress())) {
                                    whseDetails.append(lclUnitWhse.getWarehouse().getAddress()).append("\n");
                                }
                                if (CommonFunctions.isNotNull(lclUnitWhse.getWarehouse()) && CommonFunctions.isNotNull(lclUnitWhse.getWarehouse().getCity())) {
                                    whseDetails.append(lclUnitWhse.getWarehouse().getCity()).append(" ");
                                }
                                if (CommonFunctions.isNotNull(lclUnitWhse.getWarehouse()) && CommonFunctions.isNotNull(lclUnitWhse.getWarehouse().getState())) {
                                    whseDetails.append(lclUnitWhse.getWarehouse().getState()).append(" ");
                                }
                                if (CommonFunctions.isNotNull(lclUnitWhse.getWarehouse()) && CommonFunctions.isNotNull(lclUnitWhse.getWarehouse().getZipCode())) {
                                    whseDetails.append(lclUnitWhse.getWarehouse().getZipCode());
                                }
                                if (CommonFunctions.isNotNull(lclUnitWhse.getWarehouse()) && CommonFunctions.isNotNull(lclUnitWhse.getWarehouse().getPhone())) {
                                    whsePhone = lclUnitWhse.getWarehouse().getPhone();
                                }
                                if (CommonFunctions.isNotNull(lclUnitWhse.getWarehouse()) && CommonFunctions.isNotNull(lclUnitWhse.getWarehouse().getFax())) {
                                    whseFax = lclUnitWhse.getWarehouse().getFax();
                                }
                                if (CommonFunctions.isNotNull(lclUnitWhse.getArrivedDatetime())) {
                                    arrivalDate = DateUtils.formatDate(lclUnitWhse.getArrivedDatetime(), "dd-MMM-yyyy");
                                }
                                if (CommonFunctions.isNotNull(lclUnitWhse.getDestuffedDatetime())) {
                                    strippingDate = DateUtils.formatDate(lclUnitWhse.getDestuffedDatetime(), "dd-MMM-yyyy");
                                }
                                break;
                            }
                        }
                    }
                }
                //}
            }
            String headingName = "";
            document.add(headerTable(realPath, documentName, headingName, whseDetails.toString(), deliveryDetails.toString(), terminalNo.toString(), lclbooking, consigneeDetails.toString(), agentDetails.toString()));
            document.add(generalTable(documentName));
            document.add(contentTable(documentName, lclbooking, whseDetails.toString(),
                    deliveryDetails.toString(), whsePhone, whseFax, deliveryPhone, deliveryFax, conFileNumber.toString(), itDetails.toString(), vesselName.toString(), unitsNumber, ambHouseBL.toString(), cube, cbm, weight, kilos, pieceCount, packType.toString()));
            document.add(orgDestTable(bookingNumber.toString(), commodity, commodityDesc, marks, origin.toString(), destination.toString(), podValues.toString(), externalComments.toString(), lclbooking));
            document.add(generalTable(documentName));
            document.add(remarksTable(documentName));
            document.add(generalTable(documentName));
            document.add(commodityTable(documentName, lclbooking));
            document.add(generalTable(documentName));
            document.add(exceptionTable(documentName, lclbooking, warehouseDetails));
            document.add(generalTable(documentName));
            document.add(signTable(documentName, lclbooking));
            document.add(generalTable(documentName));
            document.add(totalSignTable(documentName, lclbooking));
        }
        document.close();

    }

    public PdfPTable headerTable(String realPath, String documentName, String headingName, String whseDetails, String deliveryDetails, String terminalNo, LclBooking lclbooking, String consigneeDetails, String agentDetails) throws Exception {
        Font blackNormalFont11 = FontFactory.getFont("Arial", 10f, Font.NORMAL);
        Paragraph p = null;
        Phrase pEmpty = null;
        Font boldHeadingFont = FontFactory.getFont("Arial", 7f, Font.BOLD);
        String imagePath = LoadLogisoftProperties.getProperty("application.image.logo");
        PdfPTable headerTable = new PdfPTable(3);
        headerTable.setWidthPercentage(100f);
        headerTable.setWidths(new float[]{1f, 5f, 3f});
        cell = new PdfPCell();
        cell.setBorder(0);
        pEmpty = new Phrase("ACCOUNT:", normalFont);
        pEmpty.setLeading(10f);
        cell.addElement(pEmpty);
        headerTable.addCell(cell);

        /* cell = new PdfPCell();
         cell.setBorder(0);
         cell.setColspan(4);
         if (CommonUtils.isNotEmpty(whseDetails)) {
         pEmpty = new Phrase(15f, "" + whseDetails.toString().toUpperCase(), blackNormalCourierFont9f);
         } else {
         pEmpty = new Phrase(15f, "", blackNormalCourierFont9f);
         }
         cell.addElement(pEmpty);
         headerTable.addCell(cell); */
        Font blackContentBoldFont8 = FontFactory.getFont("Arial", 8f, Font.BOLD, BaseColor.BLUE);
        cell = new PdfPCell();
        cell.setBorder(0);
        if (agentDetails != null && agentDetails != "") {
            pEmpty = new Phrase(10f, agentDetails.toUpperCase(), normalFont);
        } else {
            pEmpty = new Phrase(10f, agentDetails.toUpperCase(), normalFont);
        }
        cell.addElement(pEmpty);
        p = new Paragraph(6f, " ", normalFont);
        cell.addElement(p);
        p = new Paragraph(8f, deliveryDetails.toUpperCase(), normalFont);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        headerTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        pEmpty = new Phrase(7f, "Dock Receipt No.:" + "IMP-" + lclbooking.getLclFileNumber().getFileNumber(), normalFont);
        cell.setPaddingRight(-5f);
        cell.addElement(pEmpty);
        headerTable.addCell(cell);

        return headerTable;
    }

    public PdfPTable contentTable(String documentName, LclBooking lclbooking, String whseDetails, String deliveryDetails, String whsePhone, String whseFax, String deliveryPhone, String deliveryFax, String conFileNumber, String itDetails, String vesselName, String unitsNumber, String ambHouseBL, Double cube, Double cbm, Double weight, Double kilos, Integer pieceCount, String packType) throws Exception {
        LclUtils lclUtils = new LclUtils();
        Paragraph p = null;
        Phrase pEmpty = null;
        contentTable = new PdfPTable(3);
        contentTable.setWidthPercentage(100f);
        contentTable.setWidths(new float[]{18f, 18f, 10f});

        cell = new PdfPCell();
        cell.setBorder(0);
        PdfPTable Tables = new PdfPTable(3);
        Tables.setWidthPercentage(100.5f);
        Tables.setWidths(new float[]{5.4f, 0.5f, 14f});
        PdfPCell newcel = new PdfPCell();
        newcel.setBorder(0);
        newcel.setPadding(0f);
        p = new Paragraph(7f, "BL #", normalFont);
        p.setAlignment(Element.ALIGN_RIGHT);
        newcel.addElement(p);
        Tables.addCell(newcel);
        newcel = new PdfPCell();
        newcel.setBorder(0);
        newcel.setPadding(0f);
        p = new Paragraph(7f, ": ", normalFont);
        // p.setAlignment(Element.ALIGN_RIGHT);
        newcel.addElement(p);
        Tables.addCell(newcel);
        newcel = new PdfPCell();
        newcel.setBorder(0);
        newcel.setPadding(0f);
        p = new Paragraph(7f, ambHouseBL, normalFont);
        // p.setAlignment(Element.ALIGN_RIGHT);
        newcel.addElement(p);
        Tables.addCell(newcel);
        cell.addElement(Tables);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        pEmpty = new Phrase(4f, " ECI VOY.:" + conFileNumber.toUpperCase(), normalFont);
        cell.setPaddingRight(-5f);
        cell.addElement(pEmpty);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        pEmpty = new Phrase(4f, "I.T.NO.:" + " " + itDetails, normalFont);
        cell.setPaddingRight(-5f);
        cell.addElement(pEmpty);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        PdfPTable Table1 = new PdfPTable(3);
        Table1.setWidthPercentage(100.5f);
        Table1.setWidths(new float[]{5.4f, 0.5f, 14f});
        PdfPCell newcel1 = new PdfPCell();
        newcel1.setBorder(0);
        newcel1.setPadding(0f);
        p = new Paragraph(7f, "Shipper", normalFont);
        p.setAlignment(Element.ALIGN_RIGHT);
        newcel1.addElement(p);
        Table1.addCell(newcel1);
        newcel1 = new PdfPCell();
        newcel1.setBorder(0);
        newcel1.setPadding(0f);
        p = new Paragraph(7f, ": ", normalFont);
        // p.setAlignment(Element.ALIGN_RIGHT);
        newcel1.addElement(p);
        Table1.addCell(newcel1);
        newcel1 = new PdfPCell();
        newcel1.setBorder(0);
        newcel1.setPadding(0f);
        if (lclbooking.getShipContact() != null && lclbooking.getShipContact().getCompanyName() != null) {
            pEmpty = new Phrase(8f, lclbooking.getShipContact().getCompanyName(), normalFont);
        }
        newcel1.addElement(pEmpty);
        Table1.addCell(newcel1);
        cell.addElement(Table1);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(2);
        if (lclbooking.getConsContact() != null && lclbooking.getConsContact().getTradingPartner() != null && lclbooking.getConsContact().getTradingPartner().getECIFWNO() != null && lclbooking.getConsContact().getCompanyName() != null) {
            pEmpty = new Phrase(7f, "Consignee:" + "(" + lclbooking.getConsContact().getTradingPartner().getECIFWNO() + ")" + lclbooking.getConsContact().getCompanyName(), normalFont);
        } else if (lclbooking.getConsContact().getCompanyName() != null) {
            pEmpty = new Phrase(7f, "Consignee:" + lclbooking.getConsContact().getCompanyName(), normalFont);
        } else {
            pEmpty = new Phrase(7f, "Consignee:", normalFont);
        }
        cell.setPaddingRight(-10f);
        cell.addElement(pEmpty);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(3);
        PdfPTable Table = new PdfPTable(3);
        Table.setWidthPercentage(100.5f);
        Table.setWidths(new float[]{1.7f, 0.2f, 14f});
        PdfPCell newcells = new PdfPCell();
        newcells.setBorder(0);
        newcells.setPadding(0f);
        newcells.setPaddingLeft(2f);
        p = new Paragraph(7f, "Vessel", normalFont);
        p.setAlignment(Element.ALIGN_RIGHT);
        newcells.addElement(p);
        Table.addCell(newcells);

        newcells = new PdfPCell();
        newcells.setBorder(0);
        newcells.setPadding(0f);
        // cell.setColspan(3);
        p = new Paragraph(7f, ":", normalFont);
        //cell.setPaddingRight(-5f);
        newcells.addElement(p);
        Table.addCell(newcells);

        newcells = new PdfPCell();
        newcells.setBorder(0);
        newcells.setPadding(0f);
        // cell.setColspan(3);
        p = new Paragraph(7f, vesselName, normalFont);
        // cell.setPaddingRight(-5f);
        newcells.addElement(p);
        Table.addCell(newcells);

        newcells = new PdfPCell();
        newcells.setBorder(0);
        newcells.setPadding(0f);
        newcells.setPaddingLeft(2f);
        // cell.setColspan(3);
        p = new Paragraph(9f, "Container", normalFont);
        p.setAlignment(Element.ALIGN_RIGHT);
        newcells.addElement(p);
        Table.addCell(newcells);
        newcells = new PdfPCell();
        newcells.setBorder(0);
        newcells.setPadding(0f);
        //cell.setColspan(3);
        p = new Paragraph(9f, ":", normalFont);
        // cell.setPaddingRight(-5f);
        newcells.addElement(p);
        Table.addCell(newcells);
        newcells = new PdfPCell();
        newcells.setBorder(0);
        newcells.setPadding(0f);
        // cell.setColspan(3);
        p = new Paragraph(9f, unitsNumber, normalFont);
        // cell.setPaddingRight(-5f);
        newcells.addElement(p);
        Table.addCell(newcells);
        cell.addElement(Table);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(3);
        PdfPTable newTable = new PdfPTable(8);
        newTable.setWidthPercentage(100.5f);
        newTable.setWidths(new float[]{6.3f, 0.6f, 8f, 6f, 10f, 6f, 10f, 14f});

        PdfPCell newcell = new PdfPCell();
        newcell.setBorder(0);
        p = new Paragraph(4f, "Pieces", normalFont);
        p.setAlignment(Element.ALIGN_RIGHT);
        newcell.addElement(p);
        newTable.addCell(newcell);
        newcell = new PdfPCell();
        newcell.setBorder(0);
        pEmpty = new Phrase(4f, ":", normalFont);
        newcell.addElement(pEmpty);
        newTable.addCell(newcell);
        newcell = new PdfPCell();
        newcell.setBorder(0);
        pEmpty = new Phrase(4f, pieceCount + "    " + packType, normalFont);
        newcell.addElement(pEmpty);
        newTable.addCell(newcell);

        newcell = new PdfPCell();
        newcell.setBorder(0);
        p = new Paragraph(4f, "CFT", normalFont);
        p.setAlignment(Element.ALIGN_RIGHT);
        newcell.addElement(p);
        p = new Paragraph(8f, "CBM", normalFont);
        p.setAlignment(Element.ALIGN_RIGHT);
        newcell.addElement(p);
        newTable.addCell(newcell);

        newcell = new PdfPCell();
        newcell.setBorder(0);
        p = new Paragraph(4f, NumberUtils.convertToThreeDecimal(cube), normalFont);
        p.setAlignment(Element.ALIGN_RIGHT);
        newcell.addElement(p);
        p = new Paragraph(8f, NumberUtils.convertToThreeDecimal(cbm), normalFont);
        p.setAlignment(Element.ALIGN_RIGHT);
        newcell.addElement(p);
        newTable.addCell(newcell);

        newcell = new PdfPCell();
        newcell.setBorder(0);
        p = new Paragraph(4f, "LBS", normalFont);
        p.setAlignment(Element.ALIGN_RIGHT);
        newcell.addElement(p);
        p = new Paragraph(8f, "KGS", normalFont);
        p.setAlignment(Element.ALIGN_RIGHT);
        newcell.addElement(p);
        newTable.addCell(newcell);

        newcell = new PdfPCell();
        newcell.setBorder(0);
        p = new Paragraph(4f, NumberUtils.convertToThreeDecimal(weight), normalFont);
        p.setAlignment(Element.ALIGN_RIGHT);
        newcell.addElement(p);
        p = new Paragraph(8f, NumberUtils.convertToThreeDecimal(kilos), normalFont);
        p.setAlignment(Element.ALIGN_RIGHT);
        newcell.addElement(p);
        newTable.addCell(newcell);

        newcell = new PdfPCell();
        newcell.setBorder(0);
        p = new Paragraph(4f, "", normalFont);
        p.setAlignment(Element.ALIGN_RIGHT);
        newcell.addElement(p);
        newTable.addCell(newcell);

        newcell = new PdfPCell();
        newcell.setBorder(0);
        p = new Paragraph(6f, "", normalFont);
        p.setAlignment(Element.ALIGN_CENTER);
        newcell.addElement(p);
        newTable.addCell(newcell);
        cell.addElement(newTable);
        contentTable.addCell(cell);

        return contentTable;

    }

    public PdfPTable orgDestTable(String bookingNumber, String commodity, String commodityDesc, String marks, String origin, String destination, String podValues, String externalComments, LclBooking lclBooking) throws Exception {
        LclUtils lclUtils = new LclUtils();
        Paragraph p = null;
        Phrase pEmpty = null;
        PdfPTable orgDestTable = new PdfPTable(3);
        orgDestTable.setWidthPercentage(100f);
        orgDestTable.setWidths(new float[]{3.8f, 0.3f, 14f});

        cell = new PdfPCell();
        cell.setBorder(0);
        // cell.setColspan(3);
        if (bookingNumber != null) {
            p = new Paragraph(4f, "Master B/L", normalFont);
            p.setAlignment(Element.ALIGN_RIGHT);
            cell.addElement(p);
        }
        orgDestTable.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        // cell.setColspan(3);
        if (bookingNumber != null) {
            pEmpty = new Phrase(4f, ": ", normalFont);
            //cell.setPaddingRight(-5f);
            cell.addElement(pEmpty);
        }
        orgDestTable.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        // cell.setColspan(3);
        if (bookingNumber != null) {
            pEmpty = new Phrase(4f, bookingNumber.toUpperCase(), normalFont);
            // cell.setPaddingRight(-5f);
            cell.addElement(pEmpty);
        }
        orgDestTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        // cell.setColspan(3);
        p = new Paragraph(8f, "Commodity", normalFont);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        orgDestTable.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        // cell.setColspan(3);
        pEmpty = new Phrase(8f, ": ", normalFont);
        // cell.setPaddingRight(-5f);
        cell.addElement(pEmpty);
        orgDestTable.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        // cell.setColspan(3);
        pEmpty = new Phrase(8f, commodityDesc, normalFont);
        // cell.setPaddingRight(-5f);
        cell.addElement(pEmpty);
        orgDestTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        // cell.setColspan(3);
        p = new Paragraph(8f, "Marks", normalFont);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        orgDestTable.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        // cell.setColspan(3);
        pEmpty = new Phrase(8f, ": ", normalFont);
        // cell.setPaddingRight(-5f);
        cell.addElement(pEmpty);
        orgDestTable.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        //cell.setColspan(3);
        pEmpty = new Phrase(8f, marks, normalFont);
        // cell.setPaddingRight(-5f);
        cell.addElement(pEmpty);
        orgDestTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(7f, "Origin", normalFont);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        orgDestTable.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        if (CommonFunctions.isNotNull(origin)) {
            pEmpty = new Phrase(7f, ":", normalFont);
        } else {
            pEmpty = new Phrase(5f, ":", normalFont);
        }
        // cell.setPaddingRight(-5f);
        cell.addElement(pEmpty);
        orgDestTable.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        if (CommonFunctions.isNotNull(origin)) {
            pEmpty = new Phrase(7f, origin.toUpperCase(), normalFont);
        } else {
            pEmpty = new Phrase(5f, " ", normalFont);
        }
        //cell.setPaddingRight(-5f);
        cell.addElement(pEmpty);
        orgDestTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(7f, "Port of Discharge", normalFont);
        p.setAlignment(Element.ALIGN_RIGHT);
        //cell.setPaddingRight(-5f);
        cell.addElement(p);
        orgDestTable.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        pEmpty = new Phrase(7f, ":", normalFont);
        //cell.setPaddingRight(-5f);
        cell.addElement(pEmpty);
        orgDestTable.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(7f, podValues.toUpperCase(), normalFont);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        orgDestTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        if (CommonFunctions.isNotNull(destination)) {
            p = new Paragraph(7f, "Destination", normalFont);
        }
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        orgDestTable.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        if (CommonFunctions.isNotNull(destination)) {
            pEmpty = new Phrase(7f, ": ", normalFont);
        }
        //cell.setPaddingRight(-5f);
        cell.addElement(pEmpty);
        orgDestTable.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        if (CommonFunctions.isNotNull(destination)) {
            pEmpty = new Phrase(7f, destination.toUpperCase(), normalFont);
        }
        //cell.setPaddingRight(-5f);
        cell.addElement(pEmpty);
        orgDestTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(8f, "Rmrks", normalFont);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        orgDestTable.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        pEmpty = new Phrase(8f, ": ", normalFont);
        //cell.setPaddingRight(-5f);
        cell.addElement(pEmpty);
        orgDestTable.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        pEmpty = new Phrase(8f, externalComments.toUpperCase(), normalFont);
        // cell.setPaddingRight(-5f);
        cell.addElement(pEmpty);
//        List<LclRemarks> manualNotesList = new LclRemarksDAO().getAllRemarksByType(lclBooking.getLclFileNumber().getId().toString(), "Manual Note");
//        for (int j = 0; j < manualNotesList.size(); j++) {
//            LclRemarks lclRemarks = (LclRemarks) manualNotesList.get(j);
//            if (CommonFunctions.isNotNull(lclRemarks.getRemarks())) {
//                pEmpty = new Phrase(8f, lclRemarks.getRemarks().toUpperCase(), normalFont);
//                cell.addElement(pEmpty);
//            }
//        }
        orgDestTable.addCell(cell);
        return orgDestTable;
    }

    public PdfPTable generalTable(String documentName) throws DocumentException, Exception {
        Paragraph p = null;
        Phrase pEmpty = null;
        PdfPTable generalTable = new PdfPTable(1);
        generalTable.setWidthPercentage(100f);
        //generalTable.setWidths(new float[]{4f, 4f, 4f});
        cell = new PdfPCell();
        cell.setBorder(0);
        pEmpty = new Phrase(6f, "=======================================================================================", normalFont);
        cell.addElement(pEmpty);
        generalTable.addCell(cell);
        return generalTable;
    }

    public PdfPTable remarksTable(String documentName) throws DocumentException, Exception {
        Paragraph p = null;
        Phrase pEmpty = null;
        PdfPTable remarksTable = new PdfPTable(1);
        remarksTable.setWidthPercentage(100f);
        // remarksTable.setWidths(new float[]{4f, 4f, 4f});
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingLeft(20f);
        p = new Paragraph(9f, "Received for shipment or for storage prior to customer pick up,the property \ndescribed,marked as indicated,in apparent good order,except as noted. "
                + "\n(contents,and condition of contents,of packages unknown;thus ECI is not \nresponsible for any concealed damage).If for shipment,this property will be \nheld awaiting loading,"
                + "subject to delay,and or substitution of other vessels \nand is covered under the terms and conditions of the carrier's bill of \nlading and tariff in effect on the date of departure."
                + "If for storage,ECI \nlimit of liability for all loss,damage,or \"mis-shipment\"  shall not exceed \n$.50 per 100 lbs,limited to a maximum of $500 for any one dock receipt.", normalFont);
        cell.addElement(p);

        remarksTable.addCell(cell);
        return remarksTable;
    }

    public PdfPTable commodityTable(String documentName, LclBooking lclbooking) throws DocumentException, Exception {
        Paragraph p = null;
        Phrase pValues = null;
        Font boldHeadingFont = FontFactory.getFont("Arial", 7f, Font.BOLD);
        table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{2f, 2f, 2f, 2f, 2f});
        //marks
        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(7f, "Pieces", normalFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);
        //no of pkgs
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(7f, "Description", normalFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);
        //desc
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(7f, "Measurements", normalFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);
        //grossweight
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(7f, "Cubic Feet", normalFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(7f, "Weight", normalFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(7f, "________________", normalFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(7f, "________________", normalFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(7f, "________________", normalFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(7f, "________________", normalFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(7f, "________________", normalFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(4f, "", normalFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(4f, "", normalFont);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(4f, "", normalFont);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(4f, "", normalFont);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(4f, "", normalFont);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(4f, "", normalFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(4f, "", normalFont);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(4f, "", normalFont);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(4f, "", normalFont);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(4f, "", normalFont);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(7f, "________________", normalFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(7f, "________________", normalFont);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(7f, "________________", normalFont);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(7f, "________________", normalFont);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(7f, "________________", normalFont);
        cell.addElement(p);
        table.addCell(cell);

        return table;

    }

    public PdfPTable exceptionTable(String documentName, LclBooking lclbooking, String warehouseDetails) throws DocumentException, Exception {
        Paragraph p = null;
        Phrase pValues = null;
        PdfPTable exceptionTable = new PdfPTable(2);
        exceptionTable.setWidthPercentage(100f);
        exceptionTable.setWidths(new float[]{6f, 3f});
        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(7f, "Exceptions and/or observations", normalFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        p = new Paragraph(7f, " ", normalFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        exceptionTable.addCell(cell);
        //measure
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(7f, "Location", normalFont);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(9f, "" + warehouseDetails.toUpperCase(), normalFont);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(7f, " ", normalFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        exceptionTable.addCell(cell);

        return exceptionTable;

    }

    public PdfPTable signTable(String documentName, LclBooking lclbooking) throws DocumentException, Exception {
        Paragraph p = null;
        Phrase pValues = null;
        PdfPTable signTable = new PdfPTable(2);
        signTable.setWidthPercentage(100f);
        signTable.setWidths(new float[]{6f, 3f});
        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(7f, "Signed__________________________________", normalFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        signTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(7f, "Date:       " + "/" + "     " + "/", normalFont);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        signTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(-0f);
        cell.setPaddingLeft(-3f);
        p = new Paragraph(7f, "Received By "+companyName, normalFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        signTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(4f, "", normalFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        signTable.addCell(cell);

        return signTable;
    }

    public PdfPTable totalSignTable(String documentName, LclBooking lclbooking) throws DocumentException, Exception {
        Paragraph p = null;
        Phrase pValues = null;
        PdfPTable totalSignTable = new PdfPTable(3);
        totalSignTable.setWidthPercentage(100f);
        totalSignTable.setWidths(new float[]{1f, 4f, 4f});
        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(7f, "P/U:", normalFont);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        totalSignTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(7f, "__________________________________", normalFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        totalSignTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(7f, "__________________________________", normalFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        totalSignTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(-2f);
        cell.setPaddingLeft(-3f);
        p = new Paragraph(7f, "", normalFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        totalSignTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(-0f);
        cell.setPaddingLeft(-3f);
        p = new Paragraph(7f, "Trucking Company", normalFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        totalSignTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(-0f);
        cell.setPaddingLeft(-3f);
        p = new Paragraph(7f, "Authorized By", normalFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        totalSignTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(-2f);
        cell.setPaddingLeft(-3f);
        p = new Paragraph(7f, "", normalFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        totalSignTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(7f, "__________________________________", normalFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        totalSignTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(7f, "", normalFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        totalSignTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(-2f);
        cell.setPaddingLeft(-3f);
        p = new Paragraph(7f, "", normalFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        totalSignTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(-0f);
        cell.setPaddingLeft(-3f);
        p = new Paragraph(7f, "Date           No.of.pcs. ", normalFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        totalSignTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(7f, "", normalFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        totalSignTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(7f, "", normalFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        totalSignTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(7f, "__________________________________", normalFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        totalSignTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(7f, "__________________________________", normalFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        totalSignTable.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(7f, "", normalFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        totalSignTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(-0f);
        cell.setPaddingLeft(-3f);
        p = new Paragraph(7f, "Drivers Signature", normalFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        totalSignTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(-0f);
        cell.setPaddingLeft(-3f);
        p = new Paragraph(7f, "Customs Release I.D. No.", normalFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        totalSignTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(5);
        p = new Paragraph(7f, "", normalFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        totalSignTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(4);
        p = new Paragraph(8f, companyName + " WILL NOT BE RESPONSIBLE FOR ANY TRANSPORTATION \nCHARGES.ALL PICK-UP CHARGES ARE TO BE BILLED TO THE CONSIGNEE \n(FREIGHT COLLECT)BY THE DELIVERING CARRIER.", normalFont);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        totalSignTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(5);
        p = new Paragraph(7f, "", normalFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        totalSignTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(3);
        p = new Paragraph(7f, "****ALL CARGO TENDERED FOR TRANSPORTATION IS SUBJECT TO INSPECTION****", normalFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        totalSignTable.addCell(cell);
        return totalSignTable;
    }
}
