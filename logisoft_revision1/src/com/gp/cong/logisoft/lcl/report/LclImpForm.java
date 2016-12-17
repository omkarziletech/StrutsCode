/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.lcl.report;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.Ports;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclBookingImport;
import com.gp.cong.logisoft.domain.lcl.LclBookingImportAms;
import com.gp.cong.logisoft.domain.lcl.LclBookingPiece;
import com.gp.cong.logisoft.domain.lcl.LclBookingPieceUnit;
import com.gp.cong.logisoft.domain.lcl.LclContact;
import com.gp.cong.logisoft.domain.lcl.LclInbond;
import com.gp.cong.logisoft.domain.lcl.LclSsDetail;
import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.domain.lcl.LclUnitSsImports;
import com.gp.cong.logisoft.domain.lcl.LclUnitSsManifest;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingImportAmsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingImportDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclInbondsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsManifestDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfTemplate;
//import java.awt.Image;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Shanmugam
 */
public class LclImpForm extends LclReportFormatMethods {

    private Font normalFont = FontFactory.getFont("Arial", 9f, Font.NORMAL);
    private Font blackNormalFont11 = FontFactory.getFont("Arial", 9.25f, Font.NORMAL);
    private Font valueFont = FontFactory.getFont("Arial", 6.6f, Font.NORMAL);
    private Font boldHeadingFont = FontFactory.getFont("Arial", 8f, Font.NORMAL);
    private Font small = FontFactory.getFont("Arial", 6f, Font.NORMAL);
    private LclBooking lclbooking;
    HttpServletRequest request;
    String entryNo = "";
    String classEntry = "";
    Integer podCode = 0;
    String sailDate = "";
    String inbond = "";
    String inbondVia = "";
    String vessell = "";
    String warehouse = "";
    String marks = "";
    String orgCtry = "";
    String orgCity = "";
    String ETD = "";
    String ETA = "";
    String inbondDate = "";
    String ssVoy = "";
    String bookingNumber = "";
    Double weight = 0.00;
    Double kilos = 0.00;
    String itNo = "";
    String inbondPort = "";
    String amsHouseBL = "";
    String itClass = "";
    String po = "";
    String gen = "";
    String printEntry7512 ="";
    GenericCode genericCode = null;
    Ports ports = null;
    StringBuilder portCode = new StringBuilder();
    StringBuilder portDirector = new StringBuilder();
    StringBuilder companyDetails = new StringBuilder();
    StringBuilder companyAddress = new StringBuilder();
    StringBuilder vessel = new StringBuilder();
    StringBuilder pod = new StringBuilder();
    StringBuilder consDetails = new StringBuilder();
    StringBuilder userName = new StringBuilder();
    StringBuilder companyInfo = new StringBuilder();
    StringBuilder companyPhone = new StringBuilder();
    StringBuilder description = new StringBuilder();
    StringBuilder port = new StringBuilder();
    StringBuilder finalForeignDestination  = new StringBuilder();

    public LclImpForm(LclBooking lclbooking, HttpServletRequest request) throws Exception {
        this.lclbooking = lclbooking;
        this.request = request;
        LclUnitSs lclUnitSs = null;
        LclBookingImport lclBookingImport = new LclBookingImportDAO().getByProperty("lclFileNumber.id", lclbooking.getLclFileNumber().getId());
        //   LclInbond lclInbond = new LclInbondsDAO().findById(lclbooking.getLclFileNumber().getId());
        List<LclBookingPiece> lclBookingPiecesList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", lclbooking.getLclFileNumber().getId());
        if (!lclBookingPiecesList.isEmpty()) {
            LclBookingPiece lclbookingPiece = lclBookingPiecesList.get(0);
            if (!lclbookingPiece.getLclBookingPieceUnitList().isEmpty()) {
                LclBookingPieceUnit lclBookingPieceUnits = lclbookingPiece.getLclBookingPieceUnitList().get(0);
                LclUnitSsImports lclUnitSsImports = (LclUnitSsImports) lclBookingPieceUnits.getLclUnitSs().getLclUnit().getLclUnitSsImportsList().get(0);
                if (CommonFunctions.isNotNull(lclUnitSsImports) && CommonFunctions.isNotNull(lclUnitSsImports.getCfsWarehouseId()) && CommonFunctions.isNotNull(lclUnitSsImports.getCfsWarehouseId().getWarehouseName())) {
                    warehouse = lclUnitSsImports.getCfsWarehouseId().getWarehouseName();
                }
                // lclUnitSs = lclBookingPieceUnits.getLclUnitSs();
                // }
            }
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
                                vessel.append(lclssDetail.getSpReferenceName()).append("              ").append("V-").append(lclssDetail.getSpReferenceNo());
                                GenericCode genericCode = new GenericCodeDAO().getGenericCode(lclssDetail.getSpReferenceName(), 14);
                                if (null != genericCode && CommonFunctions.isNotNull(genericCode.getField1())) {
                                    gen = genericCode.getField1();
                                }
                            }
                            ETD = DateUtils.formatDate(lclssDetail.getStd(), "dd-MMM-yyyy");
                            ETA = DateUtils.formatDate(lclssDetail.getSta(), "dd-MMM-yyyy");
                            LclUnitSsManifest lclUnitSsManifest = new LclUnitSsManifestDAO().getLclUnitSSManifestByHeader(lclUnitSs.getLclUnit().getId(), lclUnitSs.getLclSsHeader().getId());
                            if (null != lclUnitSsManifest && CommonFunctions.isNotNull(lclUnitSsManifest.getMasterbl())) {
                                bookingNumber = lclUnitSsManifest.getMasterbl();
                            }
                        }
                    }
                }
            }
            if (CommonFunctions.isNotNull(lclbookingPiece.getMarkNoDesc())) {
                marks = lclbookingPiece.getMarkNoDesc().toUpperCase();
            }
            if (CommonUtils.isNotEmpty(lclbookingPiece.getBookedPieceCount())) {
                description.append(lclbookingPiece.getBookedPieceCount()).append(" ");
            } else if (CommonUtils.isNotEmpty(lclbookingPiece.getActualPieceCount())) {
                description.append(lclbookingPiece.getActualPieceCount()).append(" ");
            }
            if (CommonFunctions.isNotNull(lclbookingPiece.getPackageType())) {
                description.append(lclbookingPiece.getPackageType().getAbbr01()).append("\n");
            }
            if (CommonFunctions.isNotNull(lclbookingPiece.getPieceDesc())) {
                description.append(lclbookingPiece.getPieceDesc().toUpperCase());
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
        }
        if (CommonFunctions.isNotNull(lclBookingImport)) {
            if (CommonFunctions.isNotNull(lclBookingImport.getEntryNo())) {
                entryNo = lclBookingImport.getEntryNo().toUpperCase();
            }
            if (CommonFunctions.isNotNull(lclBookingImport.getItNo())) {
                itNo = lclBookingImport.getItNo();
            }
            if (CommonFunctions.isNotNull(lclBookingImport.getInbondVia())) {
                inbondVia = lclBookingImport.getInbondVia().toUpperCase();
            }
            if (CommonFunctions.isNotNull(lclBookingImport.getItNo())) {
                //  itPort=lclBookingImport.geti
            }
            if (CommonFunctions.isNotNull(lclBookingImport.getEntryClass())) {
                classEntry = lclBookingImport.getEntryClass().toUpperCase();
            }
            if (CommonFunctions.isNotNull(lclBookingImport.getItClass())) {
                if (lclBookingImport.getItClass().length() > 10) {
                    itClass = lclBookingImport.getItClass().substring(0, 10).toUpperCase();
                } else {
                    itClass = lclBookingImport.getItClass().toUpperCase();
                }
            }
        if (CommonFunctions.isNotNull(lclBookingImport.getPrintEntry7512())) {
                printEntry7512 = lclBookingImport.getPrintEntry7512();
            } 
        if (CommonFunctions.isNotNull(lclBookingImport.getForeignPortOfDischarge())) {
                finalForeignDestination.append(lclBookingImport.getForeignPortOfDischarge().getUnLocationName().toUpperCase());
            }    
        if (CommonFunctions.isNotNull(lclBookingImport.getForeignPortOfDischarge())) {
                finalForeignDestination.append(",").append(lclBookingImport.getForeignPortOfDischarge().getCountryId().getCodedesc().toUpperCase());
            }
        if (CommonFunctions.isNotNull(lclBookingImport.getForeignPortOfDischarge())){
              Ports ports = new PortsDAO().getPorts(lclBookingImport.getForeignPortOfDischarge().getUnLocationCode());
                    if (null != ports && CommonFunctions.isNotNull(ports.getShedulenumber())) {
                        finalForeignDestination.append(",").append(ports.getShedulenumber());
                    }
            }  
       }
        if (CommonFunctions.isNotNull(lclbooking)) {
            if (CommonFunctions.isNotNull(lclbooking.getPortOfOrigin())) {
                if (CommonFunctions.isNotNull(lclbooking.getPortOfOrigin().getCountryId()) && CommonFunctions.isNotNull(lclbooking.getPortOfOrigin().getCountryId().getCodedesc())) {
                    orgCtry = lclbooking.getPortOfOrigin().getCountryId().getCodedesc();
                }
                if (CommonFunctions.isNotNull(lclbooking.getPortOfOrigin().getUnLocationName())) {
                    orgCity = lclbooking.getPortOfOrigin().getUnLocationName().toUpperCase();
                }
                if (CommonFunctions.isNotNull(lclbooking.getPortOfOrigin().getCountryId()) && CommonFunctions.isNotNull(lclbooking.getPortOfOrigin().getCountryId().getCodedesc())) {
                    orgCtry = lclbooking.getPortOfOrigin().getCountryId().getCodedesc();
                }
            } else {
                if (CommonFunctions.isNotNull(lclbooking.getPortOfLoading().getCountryId()) && CommonFunctions.isNotNull(lclbooking.getPortOfLoading().getCountryId().getCodedesc())) {
                    orgCtry = lclbooking.getPortOfLoading().getCountryId().getCodedesc();
                }
                if (CommonFunctions.isNotNull(lclbooking.getPortOfLoading().getUnLocationName())) {
                    orgCity = lclbooking.getPortOfLoading().getUnLocationName().toUpperCase();
                }
                if (CommonFunctions.isNotNull(lclbooking.getPortOfLoading().getCountryId()) && CommonFunctions.isNotNull(lclbooking.getPortOfLoading().getCountryId().getCodedesc())) {
                    orgCtry = lclbooking.getPortOfLoading().getCountryId().getCodedesc();
                }
            }
            if (CommonFunctions.isNotNull(lclbooking.getPortOfDestination())) {
                if (CommonFunctions.isNotNull(lclbooking.getPortOfDestination().getSchnum())) {
                    podCode = lclbooking.getPortOfDestination().getSchnum();
                }
                if (CommonFunctions.isNotNull(lclbooking.getPortOfDestination().getUnLocationName())) {
                    port.append(lclbooking.getPortOfDestination().getUnLocationName().toUpperCase());
                }
                if (CommonFunctions.isNotNull(lclbooking.getPortOfDestination().getStateId()) && CommonFunctions.isNotNull(lclbooking.getPortOfDestination().getStateId().getCode())) {
                    port.append(",").append(lclbooking.getPortOfDestination().getStateId().getCode());
                }
                if (CommonFunctions.isNotNull(lclbooking.getPortOfDestination().getUnLocationCode())) {
                    port.append("  ").append(lclbooking.getPortOfDestination().getUnLocationCode());
                    Ports ports = new PortsDAO().getPorts(lclbooking.getPortOfDestination().getUnLocationCode());
                    if (null != ports && CommonFunctions.isNotNull(ports.getShedulenumber())) {
                        po = ports.getShedulenumber();
                    }
                }
                if (CommonFunctions.isNotNull(lclbooking.getPortOfDestination().getSchnum())) {
                    portCode.append(lclbooking.getPortOfDestination().getSchnum());
                }
                if (CommonFunctions.isNotNull(lclbooking.getPortOfDestination().getUnLocationName())) {
                    pod.append(lclbooking.getPortOfDestination().getUnLocationName().toUpperCase());
                }
                if (CommonFunctions.isNotNull(lclbooking.getPortOfDestination().getStateId()) && CommonFunctions.isNotNull(lclbooking.getPortOfDestination().getStateId().getCode())) {
                    pod.append(",").append(lclbooking.getPortOfDestination().getStateId().getCode());
                }
                // if (CommonFunctions.isNotNull(lclbooking.getPortOfDestination().getSchnum())) {
                //   pod.append(" " + lclbooking.getPortOfDestination().getSchnum());
                // }
                if (CommonFunctions.isNotNull(lclbooking.getPortOfDestination().getUnLocationName())) {
                    portDirector.append(lclbooking.getPortOfDestination().getUnLocationName().toUpperCase());
                }
                if (CommonFunctions.isNotNull(lclbooking.getPortOfDestination().getStateId()) && CommonFunctions.isNotNull(lclbooking.getPortOfDestination().getStateId().getCodedesc())) {
                    portDirector.append(",").append(lclbooking.getPortOfDestination().getStateId().getCode().toUpperCase());
                }
            }
            LclSsHeader bookedMasterSch = lclbooking.getBookedSsHeaderId();
            if (null != bookedMasterSch && null != bookedMasterSch.getVesselSsDetail()) {
                LclSsDetail bookedSsDetails = bookedMasterSch.getVesselSsDetail();
                if (CommonFunctions.isNotNull(bookedSsDetails.getStd())) {
                    sailDate = DateUtils.formatDate(bookedSsDetails.getStd(), "dd-MMM-yyyy");
                }
                if (CommonFunctions.isNotNull(bookedSsDetails.getSpReferenceName())) {
                    vessell = bookedSsDetails.getSpReferenceName();
                }
            }
        }
        List<LclInbond> lclInbondList = new LclInbondsDAO().executeQuery("from LclInbond where lclFileNumber.id= " + lclbooking.getLclFileNumber().getId() + " order by id desc");
        LclInbond lclInbond = null;
        if (lclInbondList != null && !lclInbondList.isEmpty()) {
            lclInbond = lclInbondList.get(0);
            if (CommonFunctions.isNotNull(lclInbond)) {
                if (CommonFunctions.isNotNull(lclInbond.getInbondNo())) {
                    if (lclInbond.getInbondNo().length() > 10) {
                        inbond = lclInbond.getInbondNo().substring(0, 10).toUpperCase();
                    } else {
                        inbond = lclInbond.getInbondNo().toUpperCase();
                    }
                } else {
                }
                if (CommonFunctions.isNotNull(lclInbond.getInbondDatetime())) {
                    inbondDate = DateUtils.formatDate(lclInbond.getInbondDatetime(), "dd-MMM-yyyy");
                }
                if (CommonFunctions.isNotNull(lclInbond.getInbondPort()) && CommonFunctions.isNotNull(lclInbond.getInbondPort().getUnLocationName())) {
                    inbondPort = lclInbond.getInbondPort().getUnLocationName().toString().toUpperCase();
                }
            }
        }
        if (CommonFunctions.isNotNull(lclbooking.getConsContact())) {
            LclContact lclContact = lclbooking.getConsContact();
            if (CommonUtils.isNotEmpty(lclContact.getCompanyName())) {
                consDetails.append(lclContact.getCompanyName() + ",");
            }
            if (CommonFunctions.isNotNull(lclContact.getAddress())) {
                consDetails.append(lclContact.getAddress() + "  ");
            }
            if (CommonUtils.isNotEmpty(lclContact.getCity())) {
                consDetails.append(lclContact.getCity());
            }
            if (CommonUtils.isNotEmpty(lclContact.getState())) {
                consDetails.append(" ,").append(lclContact.getState());
            }
            if (CommonUtils.isNotEmpty(lclContact.getZip())) {
                consDetails.append("-").append(lclContact.getZip());
            }
            if (CommonFunctions.isNotNull(lclContact) && CommonFunctions.isNotNull(lclContact.getPhone1())) {
                consDetails.append("   TEL: " + lclContact.getPhone1());
            }
        }
        User user = (User) request.getSession().getAttribute("loginuser");
        if (CommonFunctions.isNotNull(user)) {
            if (user.getFirstName() != null) {
                userName.append(" " + user.getFirstName().toUpperCase());
            }
            if (user.getLastName() != null) {
                userName.append(" " + user.getLastName().toUpperCase());
            }
        }
        if (lclUnitSs != null && lclUnitSs.getLclSsHeader() != null) {
            RefTerminal terminal = null;
            if (lclUnitSs != null && lclUnitSs.getLclSsHeader() != null && lclUnitSs.getLclSsHeader().getBillingTerminal() != null && lclUnitSs.getLclSsHeader().getBillingTerminal().getTrmnum() != null) {
                terminal = new RefTerminalDAO().getByProperty("trmnum", lclUnitSs.getLclSsHeader().getBillingTerminal().getTrmnum());
            }
            if (terminal != null && terminal.getAddres1() != null) {
                companyAddress.append(terminal.getAddres1());
            }
            if (terminal != null && terminal.getCity1() != null) {
                companyInfo.append(" ").append(terminal.getCity1().toUpperCase());
            }
            if (terminal != null && terminal.getState() != null) {
                companyInfo.append(" ,").append(terminal.getState());
            }
            if (terminal != null && terminal.getZipcde() != null) {
                companyInfo.append(" ").append(terminal.getZipcde());
            }
            if (terminal != null && CommonUtils.isNotEmpty(terminal.getPhnnum1())) {
                String phone = terminal.getPhnnum1();
                companyPhone.append(" PH:").append(phone.substring(0, 3)).append("-").append(phone.substring(3, 6)).append("-").append(phone.substring(6));
            }
        }
        List<LclBookingImportAms> lclBookingImportAmsList = new LclBookingImportAmsDAO().findByProperty("lclFileNumber.id", lclbooking.getLclFileNumber().getId());
        Collections.sort(lclBookingImportAmsList, new Comparator<LclBookingImportAms>() {

            public int compare(LclBookingImportAms o1, LclBookingImportAms o2) {
                return o2.getId().compareTo(o1.getId());
            }
        });

        if (lclBookingImportAmsList != null && lclBookingImportAmsList.size() > 0) {
            for (int j = 0; j < lclBookingImportAmsList.size(); j++) {
                LclBookingImportAms lclBookingImportAms = (LclBookingImportAms) lclBookingImportAmsList.get(j);
                if (CommonFunctions.isNotNull(lclBookingImportAms) && CommonFunctions.isNotNull(lclBookingImportAms.getAmsNo())) {
                    amsHouseBL = lclBookingImportAms.getAmsNo().toUpperCase();
                }
                break;
            }
        }
    }

    public void createImportBLPdf(String realPath, String fileId, String fileNumber, String outputFileName, String documentName) throws Exception {
        document = new Document();
        document.setPageSize(PageSize.A4);
        document.setMargins(8, 8, 15, 15);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(outputFileName));
        document.open();
        String headingName = "";
        document.add(headerTable(realPath, documentName, headingName));
        // document.add(generalTable(documentName));
        document.add(contentTable(documentName));
        document.add(commodityTable(realPath, documentName));
        document.add(certificateTable(documentName));
        document.add(otherInfoTable(documentName));
        document.add(cbpTable(documentName));
        document.add(instructionsTable());
        document.add(conveyanceTable());
        document.close();
    }

    public PdfPTable headerTable(String realPath, String documentName, String headingName) throws Exception {
        Paragraph p = null;
        Phrase pEmpty = null;
        Font blackNormalFont10 = FontFactory.getFont("Arial", 10f, Font.BOLD);
        Font boldHeadingFont = FontFactory.getFont("Arial", 7f, Font.BOLD);
        Font smallTop = FontFactory.getFont("Arial", 9f, Font.NORMAL);
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100f);
        headerTable.setWidths(new float[]{7f, 15f});

        PdfPCell cell = new PdfPCell();
        cell.setBorder(0);
        //cell.setColspan(2);
        cell.setPaddingBottom(12f);
        p = new Paragraph(11f, "", smallTop);
        p.setAlignment(Element.ALIGN_BASELINE);
        cell.addElement(p);
        headerTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        //cell.setColspan(2);
        cell.setPaddingBottom(0.2f);
        p = new Paragraph(11f, "O.M.B No. 1651-0003", smallTop);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        headerTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setRowspan(3);

        PdfPTable portsTable = new PdfPTable(1);
        portsTable.setWidthPercentage(100f);
        portsTable.setWidths(new float[]{9f});

        PdfPCell portscell = new PdfPCell();
        portscell.setBorder(0);
        // portscell.setBorderWidthBottom(0.4f);
        p = new Paragraph(15f, "19 CFR 10.60,10.61,123.41,123.42", normalFont);
        portscell.addElement(p);
        portsTable.addCell(portscell);

        portscell = new PdfPCell();
        // portscell.setBorder(0);
        portscell.setFixedHeight(70f);
        PdfPTable firstEntryTable = new PdfPTable(1);
        firstEntryTable.setWidthPercentage(100f);
        firstEntryTable.setWidths(new float[]{5f});



        PdfPCell firstEntryCell = new PdfPCell();
        firstEntryCell.setBorder(0);
        PdfPTable lineTable = new PdfPTable(3);
        lineTable.setWidthPercentage(100f);
        lineTable.setWidths(new float[]{2.2f, 1.5f, 2.5f});

        PdfPCell lineCell = new PdfPCell();
        lineCell.setBorder(0);
        lineCell.setBorderWidthBottom(0.4f);
        PdfPTable firstTable = new PdfPTable(1);
        firstTable.setWidthPercentage(100f);
        firstTable.setWidths(new float[]{2.2f});

        PdfPCell firstCell = new PdfPCell();
        firstCell.setBorder(0);
        firstCell.setPaddingBottom(2f);
        // p = new Paragraph(14f, " ", valueFont);
        //p.setAlignment(Element.ALIGN_BOTTOM);
        // firstEntryCell.addElement(p);
        p = new Paragraph(10f, "" + itClass, valueFont);
        firstCell.addElement(p);
        firstTable.addCell(firstCell);
        lineCell.addElement(firstTable);
        lineTable.addCell(lineCell);

        lineCell = new PdfPCell();
        lineCell.setBorder(0);
        lineCell.setPadding(0f);
        p = new Paragraph(16f, "Entry No.", normalFont);
        lineCell.addElement(p);
        lineTable.addCell(lineCell);
        lineCell = new PdfPCell();
        lineCell.setBorder(0);
        lineCell.setBorderWidthBottom(0.4f);
        if(printEntry7512.equalsIgnoreCase("L") || printEntry7512.equalsIgnoreCase("B")){
        p = new Paragraph(13f, "" + inbond, valueFont);
        }else{
         p = new Paragraph(13f, "" , valueFont);   
        }
        lineCell.addElement(p);
        lineTable.addCell(lineCell);
        firstEntryCell.addElement(lineTable);
        firstEntryTable.addCell(firstEntryCell);
        // headerTable.addCell(cell);

        firstEntryCell = new PdfPCell();
        firstEntryCell.setBorder(0);
        PdfPTable ftTable = new PdfPTable(2);
        ftTable.setWidthPercentage(100f);
        ftTable.setWidths(new float[]{1.2f, 5f});

        PdfPCell ftCell = new PdfPCell();
        ftCell.setBorder(0);
        p = new Paragraph(10f, "Port", normalFont);
        ftCell.addElement(p);
        ftTable.addCell(ftCell);
        ftCell = new PdfPCell();
        ftCell.setBorder(0);
        ftCell.setBorderWidthBottom(0.4f);
        ftCell.setColspan(2);
        p = new Paragraph(10f, "" + inbondPort, valueFont);
        ftCell.addElement(p);
        ftTable.addCell(ftCell);
        ftCell = new PdfPCell();
        ftCell.setBorder(0);
        p = new Paragraph(10f, "Date", normalFont);
        ftCell.addElement(p);
        ftTable.addCell(ftCell);
        ftCell = new PdfPCell();
        ftCell.setBorder(0);
        ftCell.setBorderWidthBottom(0.4f);
        ftCell.setColspan(2);
        p = new Paragraph(10f, "" + inbondDate, valueFont);
        ftCell.addElement(p);
        ftTable.addCell(ftCell);
        ftCell = new PdfPCell();
        // firstEntryCell.setColspan(2);
        // firstEntryCell.setBorder(0);
        //firstEntryCell.setBorderWidthBottom(0.4f);
        //firstEntryCell.setFixedHeight(15f);
        p = new Paragraph(10f, "  ", valueFont);
        ftCell.addElement(p);
        ftTable.addCell(ftCell);
        firstEntryCell.addElement(ftTable);
        firstEntryTable.addCell(firstEntryCell);
        portscell.addElement(firstEntryTable);
        portsTable.addCell(portscell);
        cell.addElement(portsTable);
        headerTable.addCell(cell);

        // headerTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        PdfPTable mTable = new PdfPTable(2);
        mTable.setWidthPercentage(100f);
        mTable.setWidths(new float[]{8.5f, 6f});

        PdfPCell mcell = new PdfPCell();
        mcell.setBorder(0);
        mcell.setPaddingTop(-6f);
        // mcell.setRowspan(2);
        p = new Paragraph(11f, "TRANSPORTATION ENTRY AND MANIFEST", blackNormalFont10);
        p.setAlignment(Element.ALIGN_CENTER);
        mcell.addElement(p);
        p = new Paragraph(11f, "OF GOODS SUBJECT TO CUSTOMS \n         INSPECTION AND PERMIT", blackNormalFont10);
        p.setAlignment(Element.ALIGN_CENTER);
        mcell.addElement(p);
        pEmpty = new Phrase(15f, "Bureau of Customs and Border Protection", FontFactory.getFont("Calibri", 12f, Font.NORMAL));
        mcell.addElement(pEmpty);
        mTable.addCell(mcell);

        // mcell = new PdfPCell();
        //mcell.setBorder(0);
        //cell.setColspan(2);
        //mcell.setPaddingBottom(0.5f);
        // p = new Paragraph(11f, "O.M.B No. 1651-0003", smallTop);
        //p.setAlignment(Element.ALIGN_RIGHT);
        //mcell.addElement(p);
        //mTable.addCell(mcell);

        mcell = new PdfPCell();
        // firstcell.setBorder(0);
        // headcells.setBorder(0);
        PdfPTable entryTable = new PdfPTable(2);
        entryTable.setWidthPercentage(100f);
        entryTable.setWidths(new float[]{5f, 7.5f});


        PdfPCell entryCell = new PdfPCell();
        entryCell.setBorder(0);
        pEmpty = new Phrase(11f, "Entry No.", normalFont);
        entryCell.addElement(pEmpty);
        entryTable.addCell(entryCell);

        entryCell = new PdfPCell();
        entryCell.setBorder(0);
        entryCell.setBorderWidthBottom(0.4f);

        if(printEntry7512.equalsIgnoreCase("R") || printEntry7512.equalsIgnoreCase("B")){
        pEmpty = new Phrase(10f, "" + inbond, valueFont);
        }else{
            pEmpty = new Phrase(10f, "", valueFont);
        }
        entryCell.addElement(pEmpty);
        entryTable.addCell(entryCell);

        entryCell = new PdfPCell();
        entryCell.setBorder(0);
        pEmpty = new Phrase(11f, "Class of Entry", normalFont);
        entryCell.addElement(pEmpty);
        entryTable.addCell(entryCell);

        entryCell = new PdfPCell();
        // newcel.setBorder(0);
        entryCell.setBorder(0);
        entryCell.setBorderWidthBottom(0.4f);
        pEmpty = new Phrase(10f, classEntry, valueFont);
        entryCell.addElement(pEmpty);
        entryTable.addCell(entryCell);

        entryCell = new PdfPCell();
        entryCell.setBorder(0);
        entryCell.setColspan(2);
        pEmpty = new Phrase(7f, "     (I.T.)(T.E.)(WD.I.E)(Drawback,etc,)", smallTop);
        entryCell.addElement(pEmpty);
        entryTable.addCell(entryCell);
        mcell.addElement(entryTable);
        mTable.addCell(mcell);
        // cell.addElement(mTable);
        // headerTable.addCell(cell);




//         firstcell = new PdfPCell();
////        firstcell.setBorder(0);
//        firstcell.setPaddingTop(-18f);
//
//        firstTable.addCell(firstcell);

//        mcell = new PdfPCell();
//        mcell.setBorder(0);
//        mcell.setColspan(2);
//        pEmpty = new Phrase(15f, "", FontFactory.getFont("Calibri", 12f, Font.NORMAL));
//        mcell.addElement(pEmpty);
//        mTable.addCell(mcell);

        mcell = new PdfPCell();
        mcell.setBorder(0);
        mcell.setColspan(2);
        PdfPTable portTables = new PdfPTable(4);
        portTables.setWidthPercentage(100f);
        portTables.setWidths(new float[]{2f, 4.5f, 2.5f, 5f});
        PdfPCell portcel = new PdfPCell();
        portcel.setBorder(0);
        //portcel.setColspan(2);
        pEmpty = new Phrase(11f, "PORT \nCODE NO", FontFactory.getFont("Arial", 8f, Font.NORMAL));
        portcel.addElement(pEmpty);
        portTables.addCell(portcel);
        portcel = new PdfPCell();
        // newcel.setBorder(0);
        portcel.setBorder(0);
        portcel.setBorderWidthBottom(0.4f);
        //  portcel.setPaddingBottom(20f);
        // portcel.setPaddingBottom(5f);
        pEmpty = new Phrase(21f, "" + po, valueFont);
        portcel.addElement(pEmpty);
        portTables.addCell(portcel);
        portcel = new PdfPCell();
        portcel.setBorder(0);
        pEmpty = new Phrase(11f, "FIRST U.S.PORT \nOF UNLADING", FontFactory.getFont("Arial", 8f, Font.NORMAL));
        portcel.addElement(pEmpty);
        portTables.addCell(portcel);

        portcel = new PdfPCell();
        // newcel.setBorder(0);
        portcel.setBorder(0);
        portcel.setBorderWidthBottom(0.4f);
        // portcel.setPaddingBottom(18f);
        // portcel.setPaddingBottom(8f);
        pEmpty = new Phrase(21f, "" + port, valueFont);
        portcel.addElement(pEmpty);
        portTables.addCell(portcel);

        portcel = new PdfPCell();
        portcel.setBorder(0);
        pEmpty = new Phrase(11f, "PORT OF", FontFactory.getFont("Arial", 8f, Font.NORMAL));
        portcel.addElement(pEmpty);
        portTables.addCell(portcel);

        portcel = new PdfPCell();
        // newcel.setBorder(0);
        portcel.setBorder(0);
        portcel.setBorderWidthBottom(0.4f);
        pEmpty = new Phrase(10f, "" + port, valueFont);
        portcel.addElement(pEmpty);
        portTables.addCell(portcel);
        portcel = new PdfPCell();
        portcel.setBorder(0);
        pEmpty = new Phrase(11f, "DATE", FontFactory.getFont("Arial", 8f, Font.NORMAL));
        portcel.addElement(pEmpty);
        portTables.addCell(portcel);
        portcel = new PdfPCell();
        // newcel.setBorder(0);
        portcel.setBorder(0);
        portcel.setBorderWidthBottom(0.4f);
        pEmpty = new Phrase(10f, "" + DateUtils.formatDate(new Date(), "dd-MMM-yyyy"), valueFont);
        portcel.addElement(pEmpty);
        portTables.addCell(portcel);
        mcell.addElement(portTables);
        mTable.addCell(mcell);

        cell.addElement(mTable);
        headerTable.addCell(cell);

        return headerTable;
    }

    public PdfPTable contentTable(String documentName) throws Exception {
        Paragraph p = null;
        Phrase pEmpty = null;
        Font boldHeadingFont = FontFactory.getFont("Arial", 7f, Font.BOLD);
        PdfPTable contentTable = new PdfPTable(1);
        contentTable.setWidthPercentage(100f);
        contentTable.setWidths(new float[]{100f});

        cell = new PdfPCell();
        cell.setBorder(0);
        PdfPTable Tables = new PdfPTable(5);
        Tables.setWidthPercentage(100f);
        Tables.setWidths(new float[]{4.3f, 10f, 2.7f, 3.4f, 2.6f});

        PdfPCell newcel = new PdfPCell();
        newcel.setBorder(0);
        pEmpty = new Phrase(10f, "Entered or imported By", normalFont);
        newcel.addElement(pEmpty);
        Tables.addCell(newcel);

        newcel = new PdfPCell();
        newcel.setBorder(0);
        newcel.setBorderWidthBottom(0.4f);
        pEmpty = new Phrase(10f, "ECONOCARIBE CONSOLIDATORS,INC.", valueFont);
        newcel.addElement(pEmpty);
        Tables.addCell(newcel);

        newcel = new PdfPCell();
        newcel.setBorder(0);
        // newcel.setBorderWidthBottom(1);
        pEmpty = new Phrase(10f, "Importer/IRS#", normalFont);
        newcel.addElement(pEmpty);
        Tables.addCell(newcel);

        newcel = new PdfPCell();
        newcel.setBorder(0);
        newcel.setBorderWidthBottom(0.4f);
        pEmpty = new Phrase(10f, "59-1428228", valueFont);
        newcel.addElement(pEmpty);
        Tables.addCell(newcel);

        newcel = new PdfPCell();
        newcel.setBorder(0);
        // newcel.setBorderWidthBottom(1);
        pEmpty = new Phrase(10f, "to be shipped", normalFont);
        newcel.addElement(pEmpty);
        Tables.addCell(newcel);
        cell.addElement(Tables);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        PdfPTable Tab = new PdfPTable(6);
        Tab.setWidthPercentage(100f);
        Tab.setWidths(new float[]{1f, 4f, 2.5f, 2f, 2f, 1.22f});

        PdfPCell newcells = new PdfPCell();
        newcells.setBorder(0);
        pEmpty = new Phrase(10f, "inbond via", normalFont);
        newcells.addElement(pEmpty);
        Tab.addCell(newcells);

        newcells = new PdfPCell();
        // newcel.setBorder(0);
        newcells.setColspan(4);
        newcells.setBorder(0);
        PdfPTable Tabl = new PdfPTable(2);
        Tabl.setWidthPercentage(100f);
        Tabl.setWidths(new float[]{0.65f, 4f});

        PdfPCell newc = new PdfPCell();
        newc.setBorder(0);
        newc.setBorderWidthBottom(0.4f);
        pEmpty = new Phrase(10f, " ", normalFont);
        newc.addElement(pEmpty);
        Tabl.addCell(newc);

        newc = new PdfPCell();
        newc.setBorder(0);
        newc.setBorderWidthBottom(0.4f);
        pEmpty = new Phrase(10f, inbondVia, valueFont);
        newc.addElement(pEmpty);
        Tabl.addCell(newc);
        newcells.addElement(Tabl);
        Tab.addCell(newcells);

        newcells = new PdfPCell();
        newcells.setBorder(0);
        pEmpty = new Phrase(10f, "consigned to", normalFont);
        newcells.addElement(pEmpty);
        Tab.addCell(newcells);

        newcells = new PdfPCell();
        newcells.setBorder(0);
        pEmpty = new Phrase(9f, "", normalFont);
        newcells.addElement(pEmpty);
        Tab.addCell(newcells);

        newcells = new PdfPCell();
        // newcel.setBorder(0);
        newcells.setBorder(0);
        // newcells.setBorderWidthBottom(1);
        p = new Paragraph(3f, "(C.H.L number)", small);
        p.setAlignment(Element.ALIGN_CENTER);
        newcells.addElement(p);
        Tab.addCell(newcells);

        newcells = new PdfPCell();
        // newcel.setBorder(0);
        newcells.setBorder(0);
        // newcells.setBorderWidthBottom(1);
        p = new Paragraph(3f, "(vessel or carrier)", small);
        p.setAlignment(Element.ALIGN_CENTER);
        newcells.addElement(p);
        Tab.addCell(newcells);

        newcells = new PdfPCell();
        // newcel.setBorder(0);
        newcells.setBorder(0);
        // newcells.setBorderWidthBottom(1);
        p = new Paragraph(3f, "(car number and initial)", small);
        p.setAlignment(Element.ALIGN_CENTER);
        newcells.addElement(p);
        Tab.addCell(newcells);
        newcells = new PdfPCell();
        // newcel.setBorder(0);
        newcells.setBorder(0);
        // newcells.setBorderWidthBottom(1);
        p = new Paragraph(3f, "(pier or station)", small);
        p.setAlignment(Element.ALIGN_CENTER);
        newcells.addElement(p);
        Tab.addCell(newcells);

        newcells = new PdfPCell();
        newcells.setBorder(0);
        pEmpty = new Phrase(9f, "", normalFont);
        newcells.addElement(pEmpty);
        Tab.addCell(newcells);

        cell.addElement(Tab);
        contentTable.addCell(cell);
        //" + portDirector + "

        cell = new PdfPCell();
        cell.setBorder(0);
        PdfPTable Table1 = new PdfPTable(4);
        Table1.setWidthPercentage(100f);
        Table1.setWidths(new float[]{4.6f, 10f, 6f, 10f});

        PdfPCell portCell = new PdfPCell();
        portCell.setBorder(0);
        pEmpty = new Phrase(10f, "CBP Port Director", normalFont);
        portCell.addElement(pEmpty);
        Table1.addCell(portCell);

        portCell = new PdfPCell();
        // newcel.setBorder(0);
        portCell.setBorder(0);
        portCell.setBorderWidthBottom(0.4f);
        pEmpty = new Phrase(10f, "" +inbondPort, valueFont);
        portCell.addElement(pEmpty);
        Table1.addCell(portCell);

        portCell = new PdfPCell();
        portCell.setBorder(0);
        pEmpty = new Phrase(10f, "Final foreign destination", normalFont);
        portCell.addElement(pEmpty);
        Table1.addCell(portCell);

        portCell = new PdfPCell();
        // newcel.setBorder(0);
        portCell.setBorder(0);
        portCell.setBorderWidthBottom(0.4f);
        pEmpty = new Phrase(10f, finalForeignDestination.toString(), valueFont);
        portCell.addElement(pEmpty);
        Table1.addCell(portCell);

        portCell = new PdfPCell();
        portCell.setBorder(0);
        pEmpty = new Phrase(2f, "", normalFont);
        portCell.addElement(pEmpty);
        Table1.addCell(portCell);

        portCell = new PdfPCell();
        // newcel.setBorder(0);
        portCell.setBorder(0);
        // portCell.setBorderWidthBottom(1);
        pEmpty = new Phrase(2f, "", normalFont);
        portCell.addElement(pEmpty);
        Table1.addCell(portCell);

        portCell = new PdfPCell();
        portCell.setBorder(0);
        pEmpty = new Phrase(2f, "", normalFont);
        portCell.addElement(pEmpty);
        Table1.addCell(portCell);

        portCell = new PdfPCell();
        // newcel.setBorder(0);
        portCell.setBorder(0);
        // portCell.setBorderWidthBottom(1);
        p = new Paragraph(3f, "(For exportations only)", small);
        p.setAlignment(Element.ALIGN_CENTER);
        portCell.addElement(p);
        Table1.addCell(portCell);

        cell.addElement(Table1);
        contentTable.addCell(cell);


        cell = new PdfPCell();
        cell.setBorder(0);
        PdfPTable Table = new PdfPTable(2);
        Table.setWidthPercentage(100f);
        Table.setWidths(new float[]{2f, 20f});

        PdfPCell newcell = new PdfPCell();
        newcell.setBorder(0);
        pEmpty = new Phrase(10f, "Consignee", normalFont);
        newcell.addElement(pEmpty);
        Table.addCell(newcell);

        newcell = new PdfPCell();
        //newcel.setBorder(0);
        newcell.setBorder(0);
        newcell.setBorderWidthBottom(0.4f);
        pEmpty = new Phrase(10f, consDetails.toString(), valueFont);
        newcell.addElement(pEmpty);
        Table.addCell(newcell);

        newcell = new PdfPCell();
        newcell.setBorder(0);
        pEmpty = new Phrase(2f, "", normalFont);
        newcell.addElement(pEmpty);
        Table.addCell(newcell);

        newcell = new PdfPCell();
        //newcel.setBorder(0);
        newcell.setBorder(0);
        // newcell.setBorderWidthBottom(1);
        p = new Paragraph(3f, "(AI CBP port of exit or destination)", small);
        p.setAlignment(Element.ALIGN_CENTER);
        newcell.addElement(p);
        Table.addCell(newcell);


        cell.addElement(Table);
        contentTable.addCell(cell);

        //" + consDetails + "
        cell = new PdfPCell();
        cell.setBorder(0);
        PdfPTable foreignTable = new PdfPTable(6);
        foreignTable.setWidthPercentage(100f);
        foreignTable.setWidths(new float[]{6f, 8f, 2f, 8f, 4f, 5f});

        PdfPCell foreignCell = new PdfPCell();
        foreignCell.setBorder(0);
        pEmpty = new Phrase(10f, "Foreign Port of Lading", normalFont);
        foreignCell.addElement(pEmpty);
        foreignTable.addCell(foreignCell);

        foreignCell = new PdfPCell();
        // newcel.setBorder(0);
        foreignCell.setBorder(0);
        foreignCell.setBorderWidthBottom(0.4f);
        pEmpty = new Phrase(10f, orgCity, valueFont);
        foreignCell.addElement(pEmpty);
        foreignTable.addCell(foreignCell);

        foreignCell = new PdfPCell();
        foreignCell.setBorder(0);
        pEmpty = new Phrase(10f, "B/L No", normalFont);
        foreignCell.addElement(pEmpty);
        foreignTable.addCell(foreignCell);

        foreignCell = new PdfPCell();
        // newcel.setBorder(0);
        foreignCell.setBorder(0);
        foreignCell.setBorderWidthBottom(0.4f);
        pEmpty = new Phrase(10f, bookingNumber, valueFont);
        foreignCell.addElement(pEmpty);
        foreignTable.addCell(foreignCell);

        foreignCell = new PdfPCell();
        foreignCell.setBorder(0);
        pEmpty = new Phrase(10f, "Date of sailing", normalFont);
        foreignCell.addElement(pEmpty);
        foreignTable.addCell(foreignCell);

        foreignCell = new PdfPCell();
        // newcel.setBorder(0);
        foreignCell.setBorder(0);
        foreignCell.setBorderWidthBottom(0.4f);
        pEmpty = new Phrase(10f, ETD, valueFont);
        foreignCell.addElement(pEmpty);
        foreignTable.addCell(foreignCell);
        cell.addElement(foreignTable);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(2f, "(Above information to be furnished only when merchandise is imported by vessel)", small);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        PdfPTable importTable = new PdfPTable(8);
        importTable.setWidthPercentage(100f);
        importTable.setWidths(new float[]{2.8f, 7f, 1f, 2f, 0.7f, 3f, 0.7f, 3f});

        PdfPCell importCell = new PdfPCell();
        importCell.setBorder(0);
        pEmpty = new Phrase(10f, "Imported on the", normalFont);
        importCell.addElement(pEmpty);
        importTable.addCell(importCell);
        importCell = new PdfPCell();
        // newcel.setBorder(0);
        importCell.setBorder(0);
        importCell.setBorderWidthBottom(0.4f);
        pEmpty = new Phrase(10f, vessel.toString(), valueFont);
        importCell.addElement(pEmpty);
        importTable.addCell(importCell);
        importCell = new PdfPCell();
        importCell.setBorder(0);
        pEmpty = new Phrase(10f, "Flag", normalFont);
        importCell.addElement(pEmpty);
        importTable.addCell(importCell);
        importCell = new PdfPCell();
        // newcel.setBorder(0);
        importCell.setBorder(0);
        importCell.setBorderWidthBottom(0.4f);
        pEmpty = new Phrase(10f, "" + gen, valueFont);
        importCell.addElement(pEmpty);
        importTable.addCell(importCell);
        importCell = new PdfPCell();
        importCell.setBorder(0);
        pEmpty = new Phrase(10f, "on", normalFont);
        importCell.addElement(pEmpty);
        importTable.addCell(importCell);
        importCell = new PdfPCell();
        // newcel.setBorder(0);
        importCell.setBorder(0);
        importCell.setBorderWidthBottom(0.4f);
        pEmpty = new Phrase(10f, "" + ETA, valueFont);
        importCell.addElement(pEmpty);
        importTable.addCell(importCell);
        importCell = new PdfPCell();
        importCell.setBorder(0);
        pEmpty = new Phrase(10f, "via", normalFont);
        importCell.addElement(pEmpty);
        importTable.addCell(importCell);
        importCell = new PdfPCell();
        // newcel.setBorder(0);
        importCell.setBorder(0);
        importCell.setBorderWidthBottom(0.4f);
        pEmpty = new Phrase(10f, "", normalFont);
        importCell.addElement(pEmpty);
        importTable.addCell(importCell);

        importCell = new PdfPCell();
        importCell.setBorder(0);
        pEmpty = new Phrase(2f, "", normalFont);
        importCell.addElement(pEmpty);
        importTable.addCell(importCell);
        importCell = new PdfPCell();
        // newcel.setBorder(0);
        importCell.setBorder(0);
        //importCell.setBorderWidthBottom(1);
        pEmpty = new Phrase(3f, "(Name of vessel or carrier and motive power)", small);
        importCell.addElement(pEmpty);
        importTable.addCell(importCell);
        importCell = new PdfPCell();
        importCell.setBorder(0);
        pEmpty = new Phrase(2f, "", normalFont);
        importCell.addElement(pEmpty);
        importTable.addCell(importCell);
        importCell = new PdfPCell();
        // newcel.setBorder(0);
        importCell.setBorder(0);
        // importCell.setBorderWidthBottom(1);
        pEmpty = new Phrase(2f, "", normalFont);
        importCell.addElement(pEmpty);
        importTable.addCell(importCell);
        importCell = new PdfPCell();
        importCell.setBorder(0);
        pEmpty = new Phrase(2f, "", normalFont);
        importCell.addElement(pEmpty);
        importTable.addCell(importCell);
        importCell = new PdfPCell();
        // newcel.setBorder(0);
        importCell.setBorder(0);
        //importCell.setBorderWidthBottom(1);
        pEmpty = new Phrase(3f, "(Date imported)", small);
        importCell.addElement(pEmpty);
        importTable.addCell(importCell);
        importCell = new PdfPCell();
        importCell.setBorder(0);
        pEmpty = new Phrase(2f, "", normalFont);
        importCell.addElement(pEmpty);
        importTable.addCell(importCell);
        importCell = new PdfPCell();
        // newcel.setBorder(0);
        importCell.setBorder(0);
        // importCell.setBorderWidthBottom(1);
        p = new Paragraph(3f, "(Last foreign port)", small);
        p.setAlignment(Element.ALIGN_CENTER);
        importCell.addElement(p);
        importTable.addCell(importCell);

        cell.addElement(importTable);
        contentTable.addCell(cell);
        //" + warehouse + "

        cell = new PdfPCell();
        cell.setBorder(0);
        PdfPTable exportTable = new PdfPTable(6);
        exportTable.setWidthPercentage(100f);
        exportTable.setWidths(new float[]{2.7f, 5f, 0.8f, 3f, 2.8f, 7f});

        PdfPCell exportCell = new PdfPCell();
        exportCell.setBorder(0);
        pEmpty = new Phrase(10f, "Exported from", normalFont);
        exportCell.addElement(pEmpty);
        exportTable.addCell(exportCell);
        exportCell = new PdfPCell();
        // newcel.setBorder(0);
        exportCell.setBorder(0);
        exportCell.setBorderWidthBottom(0.4f);
        pEmpty = new Phrase(10f, orgCtry, valueFont);
        exportCell.addElement(pEmpty);

        exportTable.addCell(exportCell);
        exportCell = new PdfPCell();
        exportCell.setBorder(0);
        pEmpty = new Phrase(10f, "on", normalFont);
        exportCell.addElement(pEmpty);
        exportTable.addCell(exportCell);
        exportCell = new PdfPCell();
        // newcel.setBorder(0);
        exportCell.setBorder(0);
        exportCell.setBorderWidthBottom(0.4f);
        pEmpty = new Phrase(10f, ETD, valueFont);
        exportCell.addElement(pEmpty);

        exportTable.addCell(exportCell);
        exportCell = new PdfPCell();
        exportCell.setBorder(0);
        pEmpty = new Phrase(10f, "Goods now at", normalFont);
        exportCell.addElement(pEmpty);
        exportTable.addCell(exportCell);
        exportCell = new PdfPCell();
        // newcel.setBorder(0);
        exportCell.setBorder(0);
        exportCell.setBorderWidthBottom(0.4f);
        pEmpty = new Phrase(10f, warehouse, valueFont);
        exportCell.addElement(pEmpty);
        exportTable.addCell(exportCell);

        exportCell = new PdfPCell();
        exportCell.setBorder(0);
        p = new Paragraph(3f, "", boldHeadingFont);
        p.setAlignment(Element.ALIGN_CENTER);
        exportCell.addElement(p);
        exportTable.addCell(exportCell);
        exportCell = new PdfPCell();
        exportCell.setBorder(0);
        p = new Paragraph(3f, "(country)", small);
        p.setAlignment(Element.ALIGN_CENTER);
        exportCell.addElement(p);
        exportTable.addCell(exportCell);
        exportCell = new PdfPCell();
        exportCell.setBorder(0);
        p = new Paragraph(3f, "", boldHeadingFont);
        p.setAlignment(Element.ALIGN_CENTER);
        exportCell.addElement(p);
        exportTable.addCell(exportCell);
        exportCell = new PdfPCell();
        exportCell.setBorder(0);
        p = new Paragraph(3f, "(Date)", small);
        p.setAlignment(Element.ALIGN_CENTER);
        exportCell.addElement(p);
        exportTable.addCell(exportCell);
        exportCell = new PdfPCell();
        exportCell.setBorder(0);
        p = new Paragraph(3f, "", boldHeadingFont);
        p.setAlignment(Element.ALIGN_CENTER);
        exportCell.addElement(p);
        exportTable.addCell(exportCell);
        exportCell = new PdfPCell();
        exportCell.setBorder(0);
        p = new Paragraph(3f, "(Name of warehouse,station,pier,etc.)", small);
        p.setAlignment(Element.ALIGN_CENTER);
        exportCell.addElement(p);
        exportTable.addCell(exportCell);
        cell.addElement(exportTable);
        contentTable.addCell(cell);

        return contentTable;
    }

    public PdfPTable commodityTable(String realPath, String documentName) throws Exception {
        Paragraph p = null;
        Phrase pEmpty = null;

        // String imagePath = /img/icons/BarCode.gif;
        PdfPTable commodityTable = new PdfPTable(6);
        commodityTable.setWidthPercentage(100f);
        commodityTable.setWidths(new float[]{10f, 25f, 11f, 6f, 5f, 5f});

        cell = new PdfPCell();
        //cell.setBorder(0);
        cell.setBorderWidthLeft(0);
        cell.setBorderWidthRight(0);
        p = new Paragraph(10f, "Marks and number \nof Packages", boldHeadingFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        commodityTable.addCell(cell);


        cell = new PdfPCell();
        // cell.setBorder(0);
        //cell.setBorder(3);
        cell.setBorderWidthBottom(0);
        p = new Paragraph(10f, "Description and Quantity of Merchandise \n Number and kind of packages \n(Describe fully as per shipping papers)", boldHeadingFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        commodityTable.addCell(cell);

        cell = new PdfPCell();
        // cell.setBorder(0);
        // cell.setBorder(2);
        cell.setBorderWidthLeft(0);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthBottom(0);
        p = new Paragraph(10f, "Gross Weight \nin Pounds", boldHeadingFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        commodityTable.addCell(cell);

        cell = new PdfPCell();
        // cell.setBorder(0);
        // cell.setBorder(1);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthBottom(0);
        p = new Paragraph(10f, "Value \n(Dollars Only)", boldHeadingFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        commodityTable.addCell(cell);

        cell = new PdfPCell();
        // cell.setBorder(0);
        p = new Paragraph(10f, "Rate", boldHeadingFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        commodityTable.addCell(cell);

        cell = new PdfPCell();
        // cell.setBorder(0);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthLeft(0);
        p = new Paragraph(10f, "Duty", boldHeadingFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        commodityTable.addCell(cell);

        cell = new PdfPCell();
        // cell.setBorder(0);
        //  cell.setBorderWidthBottom(0);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthLeft(0);
        cell.setFixedHeight(150f);
        p = new Paragraph(12f, "" + marks, boldHeadingFont);
        // p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        commodityTable.addCell(cell);

        cell = new PdfPCell();
        cell.setFixedHeight(150f);
        // cell.setBorderWidthBottom(0);
        cell.setBorderWidthRight(0);
        p = new Paragraph(10f, "" + description, boldHeadingFont);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        if (lclbooking != null && lclbooking.getLclFileNumber() != null && lclbooking.getLclFileNumber().getFileNumber() != null) {
            p = new Paragraph(8f, "ECI REF: IMP-" + lclbooking.getLclFileNumber().getFileNumber(), boldHeadingFont);
        } else {
            p = new Paragraph(8f, "ECI REF:");
        }
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        p = new Paragraph(8f, "HBL:" + amsHouseBL, boldHeadingFont);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        //Image img = Image.getInstance(realPath + imagePath);
        // img.scalePercent(60);
        // img.setAlignment(Element.ALIGN_CENTER);
        // cell.addElement(img);


        commodityTable.addCell(cell);

        cell = new PdfPCell();
        cell.setFixedHeight(150f);
        //cell.setBorderWidthBottom(0);
        cell.setBorderWidthRight(0);
        PdfPTable comTable = new PdfPTable(2);
        comTable.setWidthPercentage(100f);
        comTable.setWidths(new float[]{12f, 3.7f});

        PdfPCell comCell = new PdfPCell();
        comCell.setBorder(0);
        p = new Paragraph(10f, "" + NumberUtils.convertToTwoDecimal(weight), boldHeadingFont);
        p.setAlignment(Element.ALIGN_RIGHT);
        comCell.addElement(p);
        p = new Paragraph(10f, "" + NumberUtils.convertToTwoDecimal(kilos), boldHeadingFont);
        p.setAlignment(Element.ALIGN_RIGHT);
        comCell.addElement(p);
        comTable.addCell(comCell);

        comCell = new PdfPCell();
        comCell.setBorder(0);
        p = new Paragraph(10f, "LBS", boldHeadingFont);
        p.setAlignment(Element.ALIGN_RIGHT);
        comCell.addElement(p);
        p = new Paragraph(10f, "KGS", boldHeadingFont);
        p.setAlignment(Element.ALIGN_RIGHT);
        comCell.addElement(p);
        comTable.addCell(comCell);
        cell.addElement(comTable);
        commodityTable.addCell(cell);

        cell = new PdfPCell();
        cell.setFixedHeight(150f);
        // cell.setBorderWidthBottom(0);
        cell.setBorderWidthRight(0);
        p = new Paragraph(25f, "", boldHeadingFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        commodityTable.addCell(cell);

        cell = new PdfPCell();
        cell.setFixedHeight(150f);
        // cell.setBorderWidthBottom(0);
        p = new Paragraph(25f, "", boldHeadingFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        commodityTable.addCell(cell);

        cell = new PdfPCell();
        cell.setFixedHeight(150f);
        //cell.setBorderWidthBottom(0);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthLeft(0);
        cell.setBorderWidthRight(0);
        p = new Paragraph(25f, "", boldHeadingFont);
        cell.addElement(p);
        commodityTable.addCell(cell);


        //emptycell
        // cell = new PdfPCell();
        //cell.setBorder(0);
        // commodityTable.addCell(createEmptyCell(0, 25f, 5));
        //commodityTable.addCell(createEmptyCell(0, 25f, 5));

        return commodityTable;
    }

    public PdfPTable certificateTable(String documentName) throws Exception {
        Paragraph p = null;
        Phrase pEmpty = null;
        PdfTemplate template = pdfWriter.getDirectContent().createTemplate(30, 30);
        template.setLineWidth(0.7f);
        template.rectangle(0, 0, 10f, 10f);
        template.stroke();

        Image img = Image.getInstance(template);
        Chunk chunks = new Chunk(img, 1f, 0f);


        PdfPTable certificateTable = new PdfPTable(3);
        certificateTable.setWidthPercentage(100f);
        certificateTable.setWidths(new float[]{4f, 0.4f, 9f});

        cell = new PdfPCell();
        cell.setBorder(0);
        Chunk g1 = new Chunk(" ______________________________", valueFont);
        pEmpty = new Phrase(18f, "G.O.No.", normalFont);
        pEmpty.add(g1);
        cell.addElement(pEmpty);
        cell.setFixedHeight(15f);
        cell.setBorderWidthTop(0);
        certificateTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(18f, "", normalFont);
        p.add(chunks);
        cell.addElement(p);
        cell.setBorderWidthTop(0);
        certificateTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        pEmpty = new Phrase(18f, "Check if withdrawn for Vessel supplies(19 U.S.C. 1309)", normalFont);
        cell.addElement(pEmpty);
        cell.setBorderWidthTop(0);
        certificateTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setFixedHeight(15f);
        pEmpty = new Phrase(18f, "", normalFont);
        cell.addElement(pEmpty);
        cell.setBorderWidthLeft(0);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthBottom(0);
        certificateTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setFixedHeight(15f);
        pEmpty = new Phrase(18f, "", normalFont);
        cell.addElement(pEmpty);
        cell.setBorderWidthLeft(0);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthBottom(0);
        certificateTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setFixedHeight(15f);
        pEmpty = new Phrase(18f, "", normalFont);
        cell.addElement(pEmpty);
        cell.setBorderWidthLeft(0);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthBottom(0);
        certificateTable.addCell(cell);

        return certificateTable;
    }

    public PdfPTable otherInfoTable(String documentName) throws Exception {
        Paragraph p = null;
        Phrase pEmpty = null;
        Font blackNormalFont = FontFactory.getFont("Arial", 8f, Font.NORMAL);
        Font boldHeadingFont = FontFactory.getFont("Arial", 7f, Font.BOLD);
        Font boldHeading = FontFactory.getFont("Arial", 9f, Font.BOLD);
        PdfPTable otherInfoTable = new PdfPTable(2);
        otherInfoTable.setWidthPercentage(100f);
        otherInfoTable.setWidths(new float[]{5f, 5.5f});

        cell = new PdfPCell();
        cell.setBorderWidthLeft(0);
        cell.setBorderWidthRight(0);

        pEmpty = new Phrase(10f, "  CERTIFICATE OF LADING FOR TRANSPORTATION IN BOND \n                 AND/OR LADING FOR EXPORTATION FOR", boldHeading);
        cell.addElement(pEmpty);

        PdfPTable podTable = new PdfPTable(2);
        podTable.setWidthPercentage(100f);
        podTable.setWidths(new float[]{2f, 5f});

        PdfPCell podcel = new PdfPCell();
        podcel.setBorder(0);
        pEmpty = new Phrase("", normalFont);
        //  Chunk pod1 = new Chunk(POD, underline);
        podcel.addElement(pEmpty);
        podTable.addCell(podcel);

        podcel = new PdfPCell();
        podcel.setBorder(0);
        podcel.setBorderWidthBottom(0.4f);
        pEmpty = new Phrase(10f, "" +inbondPort, valueFont);
        //  Chunk pod1 = new Chunk(POD, underline);
        podcel.addElement(pEmpty);
        podTable.addCell(podcel);
        cell.addElement(podTable);
        // otherInfoTable.addCell(cell);

        p = new Paragraph(8f, "        (Port)", small);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        pEmpty = new Phrase(10f, "WITH THE EXCEPTIONS NOTED ABOVE,THE \nWITHIN-DESCRIBED GOODS WERE:", blackNormalFont);
        cell.addElement(pEmpty);

        PdfPTable Tables = new PdfPTable(2);
        Tables.setWidthPercentage(100f);
        Tables.setWidths(new float[]{7f, 9f});

        PdfPCell newcel = new PdfPCell();
        // newcel.setBorder(0);
        newcel.setBorderWidthTop(0);
        newcel.setBorderWidthLeft(0);
        newcel.setBorderWidthBottom(0);
        Chunk c1 = new Chunk("______________", valueFont);
        pEmpty = new Phrase(15f, "Delivered    to   the   Carrier \nnamed above,for delivery to \nthe  CBP  Port   Director  at \ndestination sealed with CBP \nseals Nos.", normalFont);
        pEmpty.add(c1);
        newcel.addElement(pEmpty);
        p = new Paragraph(7f, "\n or  the   packages   (were) ", normalFont);
        newcel.addElement(p);
        p = new Paragraph(7f, "\n(were not) labeled,or corded ", normalFont);
        newcel.addElement(p);
        p = new Paragraph(7f, "\n and sealed.", normalFont);
        newcel.addElement(p);
        pEmpty = new Phrase(24f, "_________________________", valueFont);
        newcel.addElement(pEmpty);
        p = new Paragraph(10f, "(Inspector)", small);
        p.setAlignment(Element.ALIGN_CENTER);
        newcel.addElement(p);
        pEmpty = new Phrase(18f, "_________________________", valueFont);
        newcel.addElement(pEmpty);
        p = new Paragraph(10f, "(Date)", small);
        p.setAlignment(Element.ALIGN_CENTER);
        newcel.addElement(p);
        Tables.addCell(newcel);

        newcel = new PdfPCell();
        // newcel.setBorder(0);
        newcel.setBorderWidthTop(0);
        newcel.setBorderWidthBottom(0);
        newcel.setBorderWidthLeft(0);
        newcel.setBorderWidthRight(0);
        pEmpty = new Phrase(16f, "Laden on the-", normalFont);
        newcel.addElement(pEmpty);
        pEmpty = new Phrase(18f, "_________________________________ ", valueFont);
        newcel.addElement(pEmpty);
        p = new Paragraph(10f, "(Vessel,vehicle,or aircraft) ", small);
        p.setAlignment(Element.ALIGN_CENTER);
        newcel.addElement(p);
        pEmpty = new Phrase(18f, "which cleared for-", normalFont);
        newcel.addElement(pEmpty);
        pEmpty = new Phrase(18f, "_________________________________ ", valueFont);
        newcel.addElement(pEmpty);
        pEmpty = new Phrase(25f, "on ______________________________", valueFont);
        newcel.addElement(pEmpty);
        p = new Paragraph(10f, "       (Date)", small);
        p.setAlignment(Element.ALIGN_CENTER);
        newcel.addElement(p);
        pEmpty = new Phrase(12f, "as verified by export records.", normalFont);
        newcel.addElement(pEmpty);
        pEmpty = new Phrase(15f, "________________________________", valueFont);
        newcel.addElement(pEmpty);
        p = new Paragraph(10f, "(Inspector)", small);
        p.setAlignment(Element.ALIGN_CENTER);
        newcel.addElement(p);
        pEmpty = new Phrase(18f, "________________________________", valueFont);
        newcel.addElement(pEmpty);
        p = new Paragraph(10f, "(Date)", small);
        p.setAlignment(Element.ALIGN_CENTER);
        newcel.addElement(p);
        Tables.addCell(newcel);
        cell.addElement(Tables);
        otherInfoTable.addCell(cell);

        cell = new PdfPCell();
        // cell.setBorder(0);
        cell.setBorderWidthRight(0);
        pEmpty = new Phrase(10f, "   I truly declare that the statements contained herein are true and \n correct to the best my knowledge and belief.", normalFont);
        cell.addElement(pEmpty);
        // otherInfoTable.addCell(cell);

        // cell = new PdfPCell();
        // cell.setBorder(0);
        PdfPTable userTable = new PdfPTable(2);
        userTable.setWidthPercentage(100f);
        userTable.setWidths(new float[]{3.2f, 5f});

        PdfPCell userCell = new PdfPCell();
        userCell.setBorder(0);
        pEmpty = new Phrase(16f, "Entered or Withdrawn by", normalFont);
        userCell.addElement(pEmpty);
        userTable.addCell(userCell);
        userCell = new PdfPCell();
        userCell.setBorder(0);
        userCell.setBorderWidthBottom(0.4f);
        pEmpty = new Phrase(17f, "ECONOCARIBE CONSOLIDATORS,INC", valueFont);
        userCell.addElement(pEmpty);
        userTable.addCell(userCell);

        userCell = new PdfPCell();
        userCell.setColspan(2);
        userCell.setBorder(0);
        userCell.setFixedHeight(14f);
        userCell.setBorderWidthBottom(0.4f);
        pEmpty = new Phrase(9f, userName+" " +"/ " +"ATT.IN"  , valueFont);
        userCell.addElement(pEmpty);
        userTable.addCell(userCell);

        userCell = new PdfPCell();
        userCell.setColspan(2);
        userCell.setBorder(0);
        userCell.setFixedHeight(14f);
        userCell.setBorderWidthBottom(0.4f);
        if (companyAddress != null && !companyAddress.equals("")) {
            pEmpty = new Phrase(9f, "" + companyAddress, valueFont);
        } else {
            // userCell.setFixedHeight(15f);
            pEmpty = new Phrase(9f, " ", valueFont);
        }
        userCell.addElement(pEmpty);
        userTable.addCell(userCell);

        userCell = new PdfPCell();
        userCell.setColspan(2);
        userCell.setBorder(0);
        userCell.setFixedHeight(14f);
        userCell.setBorderWidthBottom(0.4f);
        if (companyInfo != null && !companyInfo.equals("")) {
            pEmpty = new Phrase(9f, "" + companyInfo, valueFont);
        } else {
            pEmpty = new Phrase(9f, " ", valueFont);
        }
        userCell.addElement(pEmpty);
        userTable.addCell(userCell);

        userCell = new PdfPCell();
        userCell.setColspan(2);
        userCell.setBorder(0);
        userCell.setFixedHeight(14f);
        userCell.setBorderWidthBottom(0.4f);
        pEmpty = new Phrase(9f, "" + companyPhone, valueFont);
        userCell.addElement(pEmpty);
        userTable.addCell(userCell);

        userCell = new PdfPCell();
        userCell.setColspan(2);
        userCell.setBorder(0);
        userCell.setFixedHeight(14f);
        userCell.setBorderWidthBottom(0.4f);
        pEmpty = new Phrase(13f, "", valueFont);
        userCell.addElement(pEmpty);
        userTable.addCell(userCell);

        userCell = new PdfPCell();
        userCell.setColspan(2);
        userCell.setBorder(0);
        userCell.setFixedHeight(6f);
        // userCell.setBorderWidthTop(1);
        // userCell.setBorderWidthBottom(1);
        pEmpty = new Phrase(7f, "", valueFont);
        userCell.addElement(pEmpty);
        userTable.addCell(userCell);

        userCell = new PdfPCell();
        userCell.setColspan(2);
        userCell.setBorder(0);
        userCell.setFixedHeight(2.2f);
        userCell.setBorderWidthTop(0.06f);
        userCell.setBorderWidthBottom(0.06f);
        pEmpty = new Phrase(7f, "", valueFont);
        userCell.addElement(pEmpty);
        userTable.addCell(userCell);

        userCell = new PdfPCell();
        userCell.setColspan(2);
        userCell.setBorder(0);
        //userCell.setBorderWidthTop(1);
        //  userCell.setBorderWidthBottom(1);
        pEmpty = new Phrase(10f, " To the inspector.The above-described goods shall be disposed of", normalFont);
        userCell.addElement(pEmpty);
        p = new Paragraph(22f, "________________________________________________", valueFont);
        p.setAlignment(Element.ALIGN_RIGHT);
        userCell.addElement(p);
        p = new Paragraph(10f, "                                         For the Port Director", small);
        p.setAlignment(Element.ALIGN_CENTER);
        userCell.addElement(p);
        userTable.addCell(userCell);

        userCell = new PdfPCell();
        userCell.setColspan(2);
        userCell.setBorder(0);
        userCell.setFixedHeight(2.2f);
        userCell.setBorderWidthTop(0.06f);
        userCell.setBorderWidthBottom(0.06f);
        pEmpty = new Phrase(7f, "", boldHeadingFont);
        userCell.addElement(pEmpty);
        userTable.addCell(userCell);
       
        userCell = new PdfPCell();
        userCell.setBorder(0);
        userCell.setColspan(2);
        userCell.setBorderWidthRight(0);
        pEmpty = new Phrase(11f, "   Received from the Port Director of the above CBP location the \nmerchandise described in this manifest for transportation and \ndelivery into the custody of the CBP officers at the port named \nabove,all packages in apparent good order except as noted here on.", normalFont);
        userCell.addElement(pEmpty);
        userTable.addCell(userCell);
        
        userCell = new PdfPCell();
        userCell.setColspan(2);
        userCell.setBorder(0);
        userCell.setBorderWidthBottom(0.4f);
        pEmpty = new Phrase(13f,"ECONOCARIBE / IRS#59-1428228", valueFont);
        userCell.addElement(pEmpty);
        userTable.addCell(userCell);
        
        userCell = new PdfPCell();
        userCell.setColspan(2);
        userCell.setBorder(0);
        p = new Paragraph(15f, " ___________________________________________", valueFont);
        p.setAlignment(Element.ALIGN_RIGHT);
        userCell.addElement(p);
        p = new Paragraph(10f, "                  Attorney or Agent of carrier", small);
        p.setAlignment(Element.ALIGN_CENTER);
        userCell.addElement(p);
        userTable.addCell(userCell);
        
        cell.addElement(userTable);
        otherInfoTable.addCell(cell);
        
        
        return otherInfoTable;
    }

    public PdfPTable cbpTable(String documentName) throws Exception {
        Paragraph p = null;
        Phrase pEmpty = null;
        Font blackNormalFont = FontFactory.getFont("Arial", 8f, Font.NORMAL);
        Font boldHeadingFont = FontFactory.getFont("Arial", 7f, Font.BOLD);
        PdfPTable cbpTable = new PdfPTable(1);
        cbpTable.setWidthPercentage(100f);
        cbpTable.setWidths(new float[]{5f});

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(10f, "CBP Form 7512", normalFont);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        cbpTable.addCell(cell);
        return cbpTable;
    }

    public PdfPTable instructionsTable() throws Exception {
        document.newPage();
        Paragraph p = null;
        Font smallBold = FontFactory.getFont("Arial", 6.5f, Font.BOLD);
        Font blackNormalFont10 = FontFactory.getFont("Arial", 10f, Font.BOLD);
        PdfPTable commodityTable = new PdfPTable(6);
        commodityTable.setWidthPercentage(100f);
        commodityTable.setWidths(new float[]{5f, 2.5f, 3f, 5f, 5f, 7f});

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(6);
        p = new Paragraph(30f, "INSTRUCTIONS", blackNormalFont10);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        p = new Paragraph(10f, "     Consult CBP officer or Part 18, Customs Regulations, for the appropriate number of copies required for entry, withdrawal, or manifest\n"
                + "     purposes.\n     For the purpose of transfer under the cartage or lighterage provisions of a proper bond to the place of shipment from the port of entry,\n"
                + "     extra copies bearing a stamp, or notation as to their intended use may be required for local administration.\n"
                + "     As the form is the same whether used as an entry or withdrawal or manifest, all copies may be prepared at the same time by carbon\n"
                + "     process, unless more than one vessel or vehicle is used, in which case a separate manifest must be prepared for each such vessel or\n"
                + "     vehicle.\n     Whenever this form is used as an entry or withdrawal, care should be taken that the kind of entry is plainly shown in the block in the\n"
                + "     upper right-hand corner of the face of the entry.\n     This form may be printed by private parties provided that the supply printed conforms to the official form in size, wording, arrangement,\n"
                + "     and quality and color of paper.", blackNormalFont11);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        p = new Paragraph(25f, "RECORD OF CARTAGE OR LIGHTERAGE", blackNormalFont10);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        p = new Paragraph(10f, "Delivered to Cartman or Lighterman in apparent good condition except as noted on this form", blackNormalFont10);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        commodityTable.addCell(cell);
//1
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthRight(0.06f);
        p = new Paragraph(10f, "Conveyance", blackContentBoldFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        commodityTable.addCell(cell);


        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthRight(0.06f);
        p = new Paragraph(10f, "Quantity", blackContentBoldFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        commodityTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthRight(0.06f);
        p = new Paragraph(10f, "Date", blackContentBoldFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        commodityTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthRight(0.06f);
        p = new Paragraph(10f, "Delivered", blackContentBoldFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        commodityTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthRight(0.06f);
        p = new Paragraph(10f, "Received", blackContentBoldFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        commodityTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        p = new Paragraph(10f, "Received", blackContentBoldFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        commodityTable.addCell(cell);
        //2
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthBottom(0.06f);
        p = new Paragraph(25f, ".............................................", boldHeadingFont);
        cell.addElement(p);
        p = new Paragraph(5f, " ", boldHeadingFont);
        cell.addElement(p);
        p = new Paragraph(25f, ".............................................", boldHeadingFont);
        cell.addElement(p);
        p = new Paragraph(5f, " ", boldHeadingFont);
        cell.addElement(p);
        p = new Paragraph(25f, ".............................................", boldHeadingFont);
        cell.addElement(p);
        p = new Paragraph(5f, " ", boldHeadingFont);
        cell.addElement(p);
        commodityTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthBottom(0.06f);
        p = new Paragraph(25f, "....................", boldHeadingFont);
        cell.addElement(p);
        p = new Paragraph(5f, " ", boldHeadingFont);
        cell.addElement(p);
        p = new Paragraph(25f, "....................", boldHeadingFont);
        cell.addElement(p);
        p = new Paragraph(5f, " ", boldHeadingFont);
        cell.addElement(p);
        p = new Paragraph(25f, "....................", boldHeadingFont);
        cell.addElement(p);
        p = new Paragraph(5f, " ", boldHeadingFont);
        cell.addElement(p);
        commodityTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthBottom(0.06f);
        p = new Paragraph(25f, "..........................", boldHeadingFont);
        cell.addElement(p);
        p = new Paragraph(5f, " ", boldHeadingFont);
        cell.addElement(p);
        p = new Paragraph(25f, "..........................", boldHeadingFont);
        cell.addElement(p);
        p = new Paragraph(5f, " ", boldHeadingFont);
        cell.addElement(p);
        p = new Paragraph(25f, "..........................", boldHeadingFont);
        cell.addElement(p);
        p = new Paragraph(5f, " ", boldHeadingFont);
        cell.addElement(p);
        commodityTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthBottom(0.06f);
        p = new Paragraph(25f, ".............................................", boldHeadingFont);
        cell.addElement(p);
        p = new Paragraph(5f, "(Inspector)", small);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        p = new Paragraph(25f, ".............................................", boldHeadingFont);
        cell.addElement(p);
        p = new Paragraph(5f, "(Inspector)", small);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        p = new Paragraph(25f, ".............................................", boldHeadingFont);
        cell.addElement(p);
        p = new Paragraph(5f, "(Inspector)", small);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        commodityTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthBottom(0.06f);
        p = new Paragraph(25f, ".............................................", boldHeadingFont);
        cell.addElement(p);
        p = new Paragraph(5f, "(Cartman or Lighterman)", small);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        p = new Paragraph(25f, ".............................................", boldHeadingFont);
        cell.addElement(p);
        p = new Paragraph(5f, "(Cartman or Lighterman)", small);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        p = new Paragraph(25f, ".............................................", boldHeadingFont);
        cell.addElement(p);
        p = new Paragraph(5f, "(Cartman or Lighterman)", small);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        commodityTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.06f);
        p = new Paragraph(25f, "................................................................", boldHeadingFont);
        cell.addElement(p);
        p = new Paragraph(5f, "(Date)                          (Inspector)", small);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        p = new Paragraph(25f, "................................................................", boldHeadingFont);
        cell.addElement(p);
        p = new Paragraph(5f, "(Date)                          (Inspector)", small);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        p = new Paragraph(25f, "................................................................", boldHeadingFont);
        cell.addElement(p);
        p = new Paragraph(5f, "(Date)                          (Inspector)", small);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        commodityTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(15f, "Total", blackContentBoldFont);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        commodityTable.addCell(cell);

        cell = new PdfPCell();
        cell.setFixedHeight(20f);
        p = new Paragraph(25f, " ", small);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        commodityTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(4);
        p = new Paragraph(15f, "                                           ...................................................................", small);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        p = new Paragraph(5f, "                                                 (Warehouse Proprietor)", small);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        commodityTable.addCell(cell);

        //3
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(6);
        PdfPTable certificateTable = new PdfPTable(3);
        certificateTable.setWidthPercentage(100f);
        certificateTable.setWidths(new float[]{4.95f, 4.95f, 3.20f});
        PdfPCell certificateCell = new PdfPCell();
        certificateCell.setBorder(0);
        certificateCell.setBorderWidthTop(0.06f);
        certificateCell.setColspan(2);
        Chunk c = new Chunk();
        p = new Paragraph(10f, "CERTIFICATES OF TRANSFER.", blackNormalFont10);
        c = new Chunk("(If Required)", boldHeadingFont);
        p.add(c);
        p.setAlignment(Element.ALIGN_CENTER);
        certificateCell.addElement(p);
        certificateTable.addCell(certificateCell);

        certificateCell = new PdfPCell();
        certificateCell.setBorder(0);
        certificateCell.setBorderWidthLeft(0.06f);
        certificateCell.setBorderWidthTop(0.06f);
        p = new Paragraph(10f, "INSPECTED", blackNormalFont10);
        p.setAlignment(Element.ALIGN_CENTER);
        certificateCell.addElement(p);
        certificateTable.addCell(certificateCell);
        cell.addElement(certificateTable);
        commodityTable.addCell(cell);

        //4
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(6);
        certificateTable = new PdfPTable(3);
        certificateTable.setWidthPercentage(100f);
        certificateTable.setWidths(new float[]{4.95f, 4.95f, 3.20f});
        certificateCell = new PdfPCell();
        certificateCell.setBorder(0);
        p = new Paragraph(15f, "   I certify that within-described goods were trans-\nferred by reason of ...................................................\nto ..............................................................................\non ............................, at ...........................................\nand sealed with.............................................or seals\nNos............................................................., and that\ngoods were in same apparent condition as noted on\noriginal lading except.................................................\n..................................................................................\n                          ........................................................", blackNormalFont11);
        p.setAlignment(Element.ALIGN_LEFT);
        certificateCell.addElement(p);
        p = new Paragraph(5f, "Inspector, Conductor or Master", small);
        p.setAlignment(Element.ALIGN_CENTER);
        certificateCell.addElement(p);
        certificateTable.addCell(certificateCell);

        certificateCell = new PdfPCell();
        certificateCell.setBorder(0);
        certificateCell.setBorderWidthLeft(0.06f);
        p = new Paragraph(15f, "   I certify that within-described goods were trans-\nferred by reason of ...................................................\nto ..............................................................................\non ............................, at ...........................................\nand sealed with.............................................or seals\nNos............................................................., and that\ngoods were in same apparent condition as noted on\noriginal lading except.................................................\n..................................................................................\n                          ........................................................", blackNormalFont11);
        p.setAlignment(Element.ALIGN_LEFT);
        certificateCell.addElement(p);
        p = new Paragraph(5f, "Inspector, Conductor or Master", small);
        p.setAlignment(Element.ALIGN_CENTER);
        certificateCell.addElement(p);
        certificateTable.addCell(certificateCell);

        certificateCell = new PdfPCell();
        certificateCell.setBorder(0);
        certificateCell.setBorderWidthLeft(0.06f);
        p = new Paragraph(15f, "at .................................................\non ................................................\n", blackNormalFont11);
        p.setAlignment(Element.ALIGN_LEFT);
        certificateCell.addElement(p);
        p = new Paragraph(5f, "(Date)", small);
        p.setAlignment(Element.ALIGN_CENTER);
        certificateCell.addElement(p);
        p = new Paragraph(10f, "and seals found ..........................", blackNormalFont11);
        p.setAlignment(Element.ALIGN_LEFT);
        certificateCell.addElement(p);
        p = new Paragraph(15f, "....................................................\n.....................................................\n.....................................................\n.....................................................\n.....................................................", blackNormalFont11);
        p.setAlignment(Element.ALIGN_LEFT);
        certificateCell.addElement(p);
        p = new Paragraph(5f, "Inspector", small);
        p.setAlignment(Element.ALIGN_CENTER);
        certificateCell.addElement(p);
        certificateTable.addCell(certificateCell);
        cell.addElement(certificateTable);
        commodityTable.addCell(cell);
        Font blackNormalItalicFont = FontFactory.getFont("Arial", 9.25f, Font.ITALIC);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(6);
        cell.setBorderWidthBottom(1f);
        p = new Paragraph(7f, "If transfer occurs within city limits of a CBP port or station CBP officers must be notified to supervise transfer.", blackNormalItalicFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        commodityTable.addCell(cell);
        Font boldHeadingItalicFont = FontFactory.getFont("Arial", 8f, Font.ITALIC);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(6);
        cell.setBorderWidthBottom(1f);
        p = new Paragraph(7f, "INSPECTOR'S REPORT OF DISCHARGE AT DESTINATION", blackNormalFont10);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        p = new Paragraph(15f, "Port ........................................................................... Station ........................................................,....................................................", boldHeadingItalicFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        p = new Paragraph(5f, "                                                                                                                                            (Date)", small);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        p = new Paragraph(15f, "TO THE PORT DIRECTOR: Delivering line ................................................... Car No ......................................................,Initial.......................................................", boldHeadingFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        p = new Paragraph(15f, "Arrived ............................................ Condition of car ...................................................,of seals ..........................................,of packages .........................................", boldHeadingFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        p = new Paragraph(5f, "                                     (Date)", small);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        commodityTable.addCell(cell);
        return commodityTable;
    }

    public PdfPTable conveyanceTable() throws Exception {
        Paragraph p = null;
        Font smallBold = FontFactory.getFont("Arial", 6.5f, Font.BOLD);
        Font blackNormalFont10 = FontFactory.getFont("Arial", 10f, Font.BOLD);
        PdfPTable commodityTable = new PdfPTable(5);
        commodityTable.setWidthPercentage(100f);
        commodityTable.setWidths(new float[]{3.5f, 5f, 3f, 2.5f, 6.5f});

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthRight(0.06f);
        p = new Paragraph(10f, "Date of Delivery to\nImporter, or Gen.Order", smallBold);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        commodityTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthRight(0.06f);
        p = new Paragraph(10f, "Packages", smallBold);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        commodityTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthRight(0.06f);
        p = new Paragraph(10f, "No. and Kind of Entry\nor General Order", smallBold);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        commodityTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthRight(0.06f);
        p = new Paragraph(10f, "Bonded Truck or\nLighter No.", smallBold);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        commodityTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        p = new Paragraph(10f, "Conditions, Etc", smallBold);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        commodityTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthRight(0.06f);
        p = new Paragraph(15f, "........................................................", small);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        p = new Paragraph(20f, "........................................................", small);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        p = new Paragraph(20f, " ", small);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        commodityTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthRight(0.06f);
        p = new Paragraph(15f, ".................................................................................", small);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        p = new Paragraph(20f, ".................................................................................", small);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        p = new Paragraph(20f, " ", small);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        commodityTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthRight(0.06f);
        p = new Paragraph(15f, "................................................", small);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        p = new Paragraph(20f, "................................................", small);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        p = new Paragraph(20f, " ", small);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        commodityTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthRight(0.06f);
        p = new Paragraph(15f, ".......................................", small);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        p = new Paragraph(20f, ".......................................", small);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        p = new Paragraph(20f, " ", small);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        commodityTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        p = new Paragraph(15f, "..........................................................................................................", small);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        p = new Paragraph(20f, "..........................................................................................................", small);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        p = new Paragraph(20f, " ", small);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        commodityTable.addCell(cell);

        Font boldHeadingItalicFont = FontFactory.getFont("Arial", 8f, Font.ITALIC);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(5);
        p = new Paragraph(10f, "                 I certify above report is correct.", boldHeadingFont);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        p = new Paragraph(5f, "............................................Inspector", boldHeadingItalicFont);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        commodityTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(5);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        p = new Paragraph(10f, "PAPERWORK REDUCTION ACT NOTICE: The Paperwork Reduction Act says we must tell you why we are collecting this information, how we will use it, and whether you have to\n"
                + "give it to us. We ask for the information in order to carry out the laws and regulations administered by the Bureau of Customs and Border Protection. These regulations and forms\n"
                + "apply to carriers and brokers who are transporting merchandise in-bond from a port of importation to another CBP port prior to final release of the merchandise from CBP custody.\n"
                + "It is mandatory. The estimated average burden associated with this collection of information is 10 minutes per respondent depending on individual circumstances. Comments\n"
                + "concerning the accuracy of this burden estimate and suggestions for reducing this burden should be directed to Bureau of Customs and Border Protection, Information Services\n"
                + "Branch, Washington,DC 20229, and to the Office of Management and Budget, Paperwork Reduction Project(1651-0003), Washington, DC 20503", smallBold);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        commodityTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(5);
        cell.setBorderWidthTop(0.06f);
        p = new Paragraph(10f, "CBP Form 7512", blackNormalFont10);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        commodityTable.addCell(cell);
        return commodityTable;
    }
}
