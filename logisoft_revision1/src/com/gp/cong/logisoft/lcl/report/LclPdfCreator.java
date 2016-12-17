package com.gp.cong.logisoft.lcl.report;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.lcl.common.constant.ExportUnitQueryUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.lcl.model.ExportVoyageSearchModel;
import com.gp.cong.logisoft.domain.Ports;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclBookingAc;
import com.gp.cong.logisoft.domain.lcl.LclBookingPad;
import com.gp.cong.logisoft.domain.lcl.LclBookingPiece;
import com.gp.cong.logisoft.domain.lcl.LclBookingPieceWhse;
import com.gp.cong.logisoft.domain.lcl.LclBookingPieceDetail;
import com.gp.cong.logisoft.domain.lcl.LclSsDetail;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingDispoDAO;
import com.gp.cong.logisoft.domain.lcl.LclBookingHotCode;
import com.gp.cong.logisoft.domain.lcl.LclBookingImport;
import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.hibernate.dao.lcl.Lcl3pRefNoDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingHotCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPadDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPlanDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsHeaderDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.gp.cong.logisoft.lcl.bc.ExportBookingUtils;
import com.gp.cong.logisoft.lcl.model.LclBookingPlanBean;
import com.gp.cvst.logisoft.domain.CustAddress;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.hibernate.dao.CustAddressDAO;
import com.gp.cvst.logisoft.struts.form.lcl.ExportVoyageHblBatchForm;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.logiware.lcl.dao.ExportNotificationDAO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

public class LclPdfCreator extends LclReportFormatMethods implements LclCommonConstant, ConstantsInterface {

    protected BaseFont helv;
    private LclBooking lclBooking;
    private LclSsDetail bookedSsDetails;
    private String terminalNo = "";
    private String warehouseNo = "";
    private String companyName = "";
    private String phoneNumber = "";
    private String faxNumber = "";
    private String emailId = "";
    private String billingMethod = "";
    private String DR001 = null;
    private String DR002 = null;
    private String DR003 = null;
    private String DR004 = null;
    private String DR005 = null;
    private String LA001 = null;
    private String LA002 = null;
    private String additionalRefNo = "";
    private LclCostChargeDAO lclCostChargesDAO = new LclCostChargeDAO();
    StringBuilder deliverCargoValues = new StringBuilder();
    StringBuilder terminalDetails = new StringBuilder();
    private String bkStatus = "";
    private String dispo = "";
    private Boolean fileStatusFlag = false;
    String osdRemarks = "", loadingRemarks = "", externalRemarks = "", regionalGriRemarks = "", specialRemarks = "";
    private String ruleName = "";
    int pieceCount = 0;
    int labelCount = 0;
    String wareHouseNumber = "";
    String customerPo = "";
    private boolean documentOpenFlag = false;
    private boolean isNew = false;
    LclSsDetail ssDetail = null;
    LclUnitSs unitSs = null;
    UnLocation pol = null;
    UnLocation pod = null;
    UnLocation poo = null;
    Integer polId = null;

    public LclPdfCreator(LclBooking lclBooking) throws Exception {
        if (null != lclBooking) {
            this.lclBooking = lclBooking;
            new LCLBookingDAO().getCurrentSession().evict(lclBooking);
            ruleName = lclBooking.getLclFileNumber().getBusinessUnit();
            if (CommonUtils.isEmpty(ruleName)) {
                ruleName = "ECU";
            }
            String[] remarkTypes = new String[]{"OSD", "Loading Remarks", "E", "G", "Special Remarks"};
            List remarks = new LclRemarksDAO().getRemarksByTypes(lclBooking.getFileNumberId(), remarkTypes);
            for (Object row : remarks) {
                Object[] col = (Object[]) row;
                if ("OSD".equalsIgnoreCase(col[1].toString()) && col[0] != null) {
                    osdRemarks = col[0].toString();
                }
                if ("Loading Remarks".equalsIgnoreCase(col[1].toString()) && col[0] != null) {
                    loadingRemarks = col[0].toString();
                }
                if ("G".equalsIgnoreCase(col[1].toString()) && col[0] != null) {
                    regionalGriRemarks = col[0].toString();
                }
                if ("E".equalsIgnoreCase(col[1].toString()) && col[0] != null) {
                    externalRemarks = col[0].toString();
                }
                if ("Special Remarks".equalsIgnoreCase(col[1].toString()) && col[0] != null) {
                    specialRemarks = col[0].toString();
                }
            }
            wareHouseNumber = new Lcl3pRefNoDAO().get3pWHList(lclBooking.getFileNumberId());

            LclSsHeader bookedMasterSch = lclBooking.getBookedSsHeaderId();
            if (null != bookedMasterSch && null != bookedMasterSch.getVesselSsDetail()) {
                bookedSsDetails = bookedMasterSch.getVesselSsDetail();
            }
            terminalNo = lclBooking.getTerminal().getTrmnum();
            RefTerminal refTerminal = lclBooking.getTerminal();
            if (CommonFunctions.isNotNull(refTerminal)) {
                if (CommonFunctions.isNotNull(refTerminal.getTrmnam())) {
                    companyName = refTerminal.getTrmnam();
                }
                if (CommonUtils.isNotEmpty(terminalDetails)) {
                    terminalDetails.setLength(0);
                }
                if (CommonFunctions.isNotNull(refTerminal.getAddres1())) {
                    terminalDetails.append(refTerminal.getAddres1()).append(" * ");
                }
                if (CommonFunctions.isNotNull(refTerminal.getCity1())) {
                    terminalDetails.append(refTerminal.getCity1()).append(",");
                }
                if (CommonFunctions.isNotNull(refTerminal.getState())) {
                    terminalDetails.append(refTerminal.getState()).append(" ");
                }
                if (CommonFunctions.isNotNull(refTerminal.getZipcde())) {
                    terminalDetails.append(refTerminal.getZipcde()).append(" ");
                }
            }
            if (CommonFunctions.isNotNull(refTerminal.getPhnnum1())) {
                String pNoSpaceRemove = StringUtils.remove(refTerminal.getPhnnum1(), " ");
                String ph1 = pNoSpaceRemove.substring(0, 3);
                String ph2 = pNoSpaceRemove.substring(3, 6);
                String ph3 = pNoSpaceRemove.substring(6);
                phoneNumber = "Phone " + ph1 + "-" + ph2 + "-" + ph3;
            }

            if (CommonFunctions.isNotNull(refTerminal.getFaxnum1())) {
                String faxNoSpaceRemove = StringUtils.remove(refTerminal.getFaxnum1(), " ");
                String fax1 = faxNoSpaceRemove.substring(0, 3);
                String fax2 = faxNoSpaceRemove.substring(3, 6);
                String fax3 = faxNoSpaceRemove.substring(6);
                faxNumber = " * Fax " + fax1 + "-" + fax2 + "-" + fax3;
            }
            emailId = new ExportNotificationDAO().getFromAddress(lclBooking.getFileNumberId());
            if (lclBooking.getPooWhseContact() != null) {
                if (CommonUtils.isNotEmpty(deliverCargoValues)) {
                    deliverCargoValues.setLength(0);
                }
                if (null != lclBooking.getPooWhseContact().getWarehouse() && CommonUtils.isNotEmpty(lclBooking.getPooWhseContact().getWarehouse().getWarehouseNo())) {
                    deliverCargoValues.append(lclBooking.getPooWhseContact().getWarehouse().getWarehouseNo()).append("\n");
                }
                if (CommonUtils.isNotEmpty(lclBooking.getPooWhseContact().getCompanyName())) {
                    deliverCargoValues.append(lclBooking.getPooWhseContact().getCompanyName()).append("\n");
                }
                if (CommonUtils.isNotEmpty(lclBooking.getPooWhseContact().getAddress())) {
                    deliverCargoValues.append(lclBooking.getPooWhseContact().getAddress()).append("\n");
                }
                if (CommonUtils.isNotEmpty(lclBooking.getPooWhseContact().getCity())) {
                    deliverCargoValues.append(lclBooking.getPooWhseContact().getCity()).append(",");
                }
                if (CommonUtils.isNotEmpty(lclBooking.getPooWhseContact().getState())) {
                    deliverCargoValues.append(lclBooking.getPooWhseContact().getState()).append("  ");
                }
                if (CommonUtils.isNotEmpty(lclBooking.getPooWhseContact().getZip())) {
                    deliverCargoValues.append(lclBooking.getPooWhseContact().getZip()).append("\n");
                }
                if (CommonUtils.isNotEmpty(lclBooking.getPooWhseContact().getPhone1())) {
                    deliverCargoValues.append("Phone:").append(lclBooking.getPooWhseContact().getPhone1());
                }
            }
//            bookingStatus = lclBooking.getLclFileNumber().getStatus();
            LclBookingPad lclBookingPad = new LclBookingPadDAO().getLclBookingPadByFileNumber(lclBooking.getFileNumberId());
            if (lclBookingPad != null && lclBookingPad.getPickupReferenceNo() != null) {
                additionalRefNo = lclBookingPad.getPickupReferenceNo();
            }
            billingMethod = "P".equalsIgnoreCase(lclBooking.getBillingType()) ? "FREIGHT PREPAID"
                    : "C".equalsIgnoreCase(lclBooking.getBillingType()) ? "FREIGHT COLLECT" : "BOTH";

            if (lclBooking.getLclFileNumber().getLclBookingDispoList() != null) {
                //bkStatus = new LclBookingDispoDAO().getDispositionCode(lclBooking.getFileNumberId());
                dispo = new LclBookingDispoDAO().getDispositionCode(lclBooking.getFileNumberId());
                bkStatus = !"OBKG".equalsIgnoreCase(dispo) ? "Warehouse" : "Booking";
            }
            fileStatusFlag = !"Booking".equalsIgnoreCase(bkStatus) ? true : false;
            LclUnitSsDAO unitSsDAO = new LclUnitSsDAO();
            ExportVoyageSearchModel pickedDetails = unitSsDAO.getPickedVoyageByVessel(lclBooking.getFileNumberId(), "E");
            if (pickedDetails != null && CommonUtils.isNotEmpty(pickedDetails.getUnitSsId())) {
                unitSs = unitSsDAO.findById(Long.parseLong(pickedDetails.getUnitSsId()));
                if (unitSs != null) {
                    ssDetail = unitSs.getLclSsHeader().getVesselSsDetail();
                }
            } else if (null != lclBooking.getBookedSsHeaderId()) {
                ssDetail = lclBooking.getBookedSsHeaderId().getVesselSsDetail();
            }

            if ("T".equalsIgnoreCase(lclBooking.getBookingType())) {
                LclBookingImport bookingImport = lclBooking.getLclFileNumber().getLclBookingImport();
                poo = lclBooking.getPortOfDestination();
                pol = bookingImport.getUsaPortOfExit();
                pod = bookingImport.getForeignPortOfDischarge();
                polId = bookingImport.getUsaPortOfExit().getId();
            } else {
                poo = lclBooking.getPortOfOrigin();
                pol = lclBooking.getPortOfLoading();
                pod = lclBooking.getPortOfDestination();
            }

            printBookingComments();
        }
    }

    public void printBookingComments() throws Exception {
        Iterator bookingCommentsIterator = new GenericCodeDAO().getLclPrintComments(39, "DR");
        while (bookingCommentsIterator.hasNext()) {
            Object[] row = (Object[]) bookingCommentsIterator.next();
            String code = (String) row[0];
            String codeDesc = (String) row[1];
            if (code != null) {
                if ("DR001".equalsIgnoreCase(code)) {
                    DR001 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                } else if ("DR002".equalsIgnoreCase(code)) {
                    DR002 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                } else if ("DR003".equalsIgnoreCase(code)) {
                    DR003 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                } else if ("DR004".equalsIgnoreCase(code)) {
                    DR004 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                } else if ("DR005".equalsIgnoreCase(code)) {
                    DR005 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                }
            }
        }
    }

    public void createPdf(String realPath, String outputFileName, String documentName,
            HttpServletRequest request) throws Exception {
        try {
            document = new Document();
            document.setPageSize(PageSize.A4);
            document.setMargins(8, 2, 8, 8);
            pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(outputFileName));
            document.open();
            document.add(onStartPage(realPath));
            document.add(contentTable());
            document.add(lineTable());
            document.add(bodyTable());
            document.add(bodyConsiTable());
            document.add(bodyOrginTable());
            document.add(lineTable1());
            document.add(contentDisplayTable());
            document.add(lineTable());
            document.add(commodityContentTable());
            document.add(commodityValuesTable());
            document.add(additionalTable());
            document.add(deliverTable(documentName));
            document.add(overseasTable());
            document.add(voyageTable(documentName));
            document.add(emptyLargeTable());
            document.add(contentFinalTable());
            document.add(contentFinalTable1(request));
            if (bkStatus.equalsIgnoreCase("Booking")) {
                document.newPage();
                document.add(addingPrintingLabelWithQrCode(realPath));
                document.add(addingPrintingLabelWithQrCode(realPath));
            }
            document.close();
        } catch (Exception e) {
            throw e;
        }
    }

    public PdfPTable onStartPage(String realPath) throws Exception {
        try {

            String path = LoadLogisoftProperties.getProperty(ruleName.equalsIgnoreCase("ECU") ? "application.image.logo"
                    : "application.image.econo.logo");
            Paragraph p = null;
            table = new PdfPTable(5);
            table.setWidthPercentage(100f);
            table.setWidths(new float[]{0.02f, 5.9f, 5f, 4f, 4.7f});
            cell = new PdfPCell();
            cell.setBorder(0);
            cell.setRowspan(4);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(0);
            cell.setRowspan(2);
            p = new Paragraph("" + bkStatus, blackBoldFont);
            p.setLeading(16f);
            cell.addElement(p);
            table.addCell(cell);

            //logo
            cell = new PdfPCell();
            cell.setBorder(0);
            cell.setPadding(0f);
            cell.setColspan(2);
            cell.setRowspan(4);
            Image img = Image.getInstance(realPath + path);
            img.scalePercent(60);
            img.setAlignment(Element.ALIGN_CENTER);
            cell.addElement(img);
            p = new Paragraph("" + terminalDetails.toString().toUpperCase(), ratessubHeadingQuote);
            p.setAlignment(Element.ALIGN_CENTER);
            p.setLeading(9f);
            cell.setPaddingBottom(3f);
            cell.addElement(p);
            p = new Paragraph("" + phoneNumber + faxNumber, ratessubHeadingQuote);
            p.setLeading(9f);
            p.setAlignment(Element.ALIGN_CENTER);
            cell.addElement(p);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(0);
            cell.setRowspan(4);
            if (fileStatusFlag) {
                String enteredDate = new LclRemarksDAO().getCargoReceivedDate(lclBooking.getFileNumberId().toString());
                Date date = null;
                if (CommonUtils.isNotEmpty(enteredDate)) {
                    date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(enteredDate);
                    p = new Paragraph("Received Date: " + new SimpleDateFormat("dd-MMM-yyyy").format(date), blackBoldFont);
                } else {
                    p = new Paragraph("Received Date: ", blackBoldFont);
                }
            } else {
                p = new Paragraph("Booking Date: " + DateUtils.formatStringDateToAppFormatMMM(lclBooking.getLclFileNumber().getCreatedDatetime()), blackBoldFont);
            }
            p.setAlignment(Element.ALIGN_RIGHT);
            cell.addElement(p);
            if (lclBooking.getLclFileNumber().getLclBookingExport() != null && lclBooking.getLclFileNumber().getLclBookingExport().isCfcl()) {
                p = new Paragraph("\n" + "CFCL", boxRedFontBig);
                p.setAlignment(Element.ALIGN_CENTER);
                p.setLeading(9f);
                cell.setPaddingBottom(3f);
                cell.addElement(p);
            }
            table.addCell(cell);
            Font emailfont = FontFactory.getFont("Helvetica-Narrow-Bold", 8.5f, Font.BOLD);
            cell = new PdfPCell();
            cell.setBorder(0);
            cell.setBorderColor(new BaseColor(128, 00, 00));
            cell.setPaddingBottom(5f);
            cell.setBorderWidthBottom(1f);
            cell.setBorderWidthLeft(1f);
            cell.setBorderWidthRight(1f);
            cell.setBorderWidthTop(1f);
            p = new Paragraph(9f, " Please send documents to:", emailfont);
            cell.addElement(p);
            p = new Paragraph(10f, CommonUtils.isNotEmpty(emailId) ? emailId : " ", emailfont);
            cell.setPaddingBottom(5f);
            cell.addElement(p);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(0);
            table.addCell(cell);
        } catch (Exception e) {
            throw e;
        }
        return table;

    }

    public PdfPTable contentTable() throws DocumentException {
        String cityCode;
        if (!lclBooking.getBookingType().equalsIgnoreCase("T")) {
            cityCode = lclBooking.getPortOfOrigin().getUnLocationCode().substring(2);
        } else {
            cityCode = lclBooking.getPortOfDestination().getUnLocationCode().substring(2);
        }
        Font headingBlueFont = FontFactory.getFont("Arial", 16, Font.BOLD, new BaseColor(00, 51, 153));
        Paragraph p = null;
        table = new PdfPTable(3);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{9f, 4.7f, 3f});
        cell = new PdfPCell();
        cell.setPadding(4f);
        cell.setBorder(0);
        cell.setColspan(3);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setPadding(0f);
        cell.setPaddingBottom(3f);
        cell.setBorder(0);
        p = new Paragraph(16f, fileStatusFlag ? "LCL Ocean Dock Receipt" : "LCL Ocean Booking", headingBlueBoldFont);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        cell.setPaddingBottom(3f);
        p = new Paragraph(16f, fileStatusFlag ? "Terminal/Dock Receipt:" : "Booking Number:", headingBlueBoldFont);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        cell.setPaddingBottom(3f);
        p = new Paragraph(16f, "" + cityCode + "-" + lclBooking.getLclFileNumber().getFileNumber(), headingBlueFont);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        table.addCell(cell);
        return table;
    }

    public static PdfPTable lineTable() {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100f);
        // table.setSpacingAfter(1f);
        PdfPCell cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthTop(0.09f);
        cell.setPadding(1f);
        table.addCell(cell);
        return table;
    }

    public PdfPTable bodyTable() throws Exception {
        Phrase p = null;
        table = new PdfPTable(6);
        table.setWidthPercentage(100f);
        StringBuilder shipperValues = new StringBuilder();
        if (lclBooking != null) {
            if (lclBooking.getShipAcct() != null && CommonUtils.isNotEmpty(lclBooking.getShipAcct().getAccountno())) {
                shipperValues.append(lclBooking.getShipAcct().getAccountno()).append(" - ");
            }
            if (lclBooking.getShipContact() != null && CommonUtils.isNotEmpty(lclBooking.getShipContact().getCompanyName())) {
                shipperValues.append(lclBooking.getShipContact().getCompanyName());
            }
        }
        StringBuilder forwarderValues = new StringBuilder();
        if (lclBooking != null) {
            if (lclBooking.getFwdAcct() != null && CommonUtils.isNotEmpty(lclBooking.getFwdAcct().getAccountno())) {
                forwarderValues.append(lclBooking.getFwdAcct().getAccountno()).append(" - ");
            }
            if (lclBooking.getFwdContact() != null && CommonUtils.isNotEmpty(lclBooking.getFwdContact().getCompanyName())) {
                forwarderValues.append(lclBooking.getFwdContact().getCompanyName());
            }
        }
        //first row
        cell = new PdfPCell();
        cell.setColspan(2);
        cell.setBorder(0);
        cell.setBorderWidthTop(0.08f);
        p = new Phrase("Shipper:", headingBoldFont);
        p.setLeading(6f);
        cell.setPaddingTop(3f);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthTop(0.08f);
        cell.setBorderWidthRight(0.08f);
        p = new Phrase("Ref#:", headingBoldFont);
        p.setLeading(6f);
        cell.setPaddingTop(3f);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(2);
        cell.setBorder(0);
        cell.setBorderWidthTop(0.08f);

        p = new Phrase("Forwarder:", headingBoldFont);
        p.setLeading(6f);
        cell.setPaddingTop(3f);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthTop(0.08f);
        p = new Phrase("Ref#:", headingBoldFont);
        p.setLeading(6f);
        cell.setPaddingTop(3f);
        cell.addElement(p);
        table.addCell(cell);
        //ShipperName
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(0f);
        cell.setPaddingBottom(3f);
        cell.setColspan(2);
        if (!"".equals(shipperValues.toString())) {
            p = new Phrase("" + shipperValues.toString(), fontCompNormalSub);
        } else {
            p = new Phrase("", fontCompNormalSub);
        }
        p.setLeading(8f);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(0f);
        cell.setBorderWidthRight(0.08f);
        if (lclBooking.getShipReference() != null) {
            p = new Phrase("" + lclBooking.getShipReference().toUpperCase(), fontCompNormalSub);
        } else {
            cell.setPadding(8f);
            p = new Phrase("", fontCompNormalSub);
        }
        p.setLeading(8f);
        cell.addElement(p);
        table.addCell(cell);
        //ForwarderNAme

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(0f);
        cell.setColspan(2);
        cell.setPaddingBottom(3f);
        p = new Phrase("" + forwarderValues.toString(), fontCompNormalSub);
        p.setLeading(8f);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        if (lclBooking.getFwdReference() != null) {
            p = new Phrase("" + lclBooking.getFwdReference().toUpperCase(), fontCompNormalSub);
        } else {
            p = new Phrase("", fontCompNormalSub);
        }
        p.setLeading(8f);
        cell.addElement(p);
        table.addCell(cell);
        return table;
    }

    public PdfPTable bodyConsiTable() throws Exception {
        Phrase p = null;
        table = new PdfPTable(6);
        table.setWidthPercentage(100f);
        StringBuilder notfyValues = new StringBuilder();
        if (lclBooking.getNotyAcct() != null && CommonUtils.isNotEmpty(lclBooking.getNotyAcct().getAccountno())) {
            notfyValues.append(lclBooking.getNotyAcct().getAccountno()).append(" - ");
        }
        if (lclBooking.getNotyContact() != null && CommonUtils.isNotEmpty(lclBooking.getNotyContact().getCompanyName())) {
            notfyValues.append(lclBooking.getNotyContact().getCompanyName());
        }
        StringBuilder consgValues = new StringBuilder();
        if (lclBooking.getConsAcct() != null && CommonUtils.isNotEmpty(lclBooking.getConsAcct().getAccountno())) {
            consgValues.append(lclBooking.getConsAcct().getAccountno()).append(" - ");
        }
        if (lclBooking.getConsContact() != null && CommonUtils.isNotEmpty(lclBooking.getConsContact().getCompanyName())) {
            consgValues.append(lclBooking.getConsContact().getCompanyName());
        }
        //first row
        cell = new PdfPCell();
        cell.setColspan(2);
        cell.setBorder(0);
        cell.setBorderWidthTop(0.06f);
        p = new Phrase("Notify Party:", headingBoldFont);
        p.setLeading(6f);
        cell.setPaddingTop(3f);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthRight(0.06f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(2);
        cell.setBorder(0);
        cell.setBorderWidthTop(0.06f);
        p = new Phrase("Consignee:", headingBoldFont);
        p.setLeading(6f);
        cell.setPaddingTop(3f);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthTop(0.06f);
        p = new Phrase("Ref#:", headingBoldFont);
        p.setLeading(6f);
        cell.setPaddingTop(3f);
        cell.addElement(p);
        table.addCell(cell);
        //notify name
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.06f);
        cell.setPaddingTop(0f);
        cell.setPaddingBottom(3f);
        if (!"".equals(notfyValues.toString())) {
            p = new Phrase("" + notfyValues.toString(), fontCompNormalSub);
            p.setLeading(8f);
        } else {
            p = new Phrase("", fontCompNormalSub);
        }
        cell.addElement(p);
        cell.setColspan(2);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(0f);
        cell.setPaddingBottom(3f);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthRight(0.08f);
        cell.setPadding(8f);
        p.setLeading(8f);
        cell.addElement(p);
        table.addCell(cell);
        //consignee Name
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.06f);
        cell.setColspan(2);
        cell.setPaddingTop(0f);
        cell.setPaddingBottom(3f);
        p = new Phrase("" + consgValues.toString(), fontCompNormalSub);
        p.setLeading(8f);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.08f);
        if (lclBooking.getConsReference() != null) {
            p = new Phrase("" + lclBooking.getConsReference().toUpperCase(), fontCompNormalSub);
        } else {
            p = new Phrase("", fontCompNormalSub);
        }
        p.setLeading(8f);
        cell.addElement(p);
        table.addCell(cell);
        return table;
    }

    public PdfPTable bodyOrginTable() throws DocumentException, Exception {
        Phrase p = null;
        Chunk c = null;

        UnLocation polUnlocation = null;
        UnLocation podUnlocation = null;

        // Destination Port/CFS
        StringBuilder destCfs = new StringBuilder();
        if (null != pod) {
            if (null != pod.getStateId() && CommonUtils.isNotEmpty(pod.getStateId().getCodedesc())
                    && pod.getUnLocationCode().startsWith("US")) {
                destCfs.append(pod.getUnLocationName()).append(", ").append(pod.getStateId().getCode()); // state
            } else if (null != pod.getCountryId() && CommonUtils.isNotEmpty(pod.getCountryId().getCodedesc())) {
                destCfs.append(pod.getUnLocationName()).append(", ").append(pod.getCountryId().getCodedesc()); // country
            }
        }

        if (null != ssDetail) {
            polUnlocation = ssDetail.getDeparture();
            podUnlocation = ssDetail.getArrival();
        } else {
            polUnlocation = pol;
            podUnlocation = pod;
        }

//        int pod = null != lclBooking.getPortOfDestination() ? lclBooking.getPortOfDestination().getId() : 0;
//        if (podValues != 0 && pod != 0) {
//            if (pod != podValues) {
//                flag = true;
//                unLocationValues = new UnLocationDAO().findById(podValues);
//            } else {
//                flag = false;
//            }
//        } else {
//            flag = false;
//        }
        //Origin Terminal
        StringBuilder originValues = new StringBuilder();
        if (CommonFunctions.isNotNull(poo)) {
            if (CommonUtils.isNotEmpty(poo.getUnLocationName()) && null != poo.getStateId()
                    && poo.getUnLocationCode().startsWith("US") && CommonUtils.isNotEmpty(poo.getStateId().getCode())) {
                originValues.append(poo.getUnLocationName()).append(",").append(poo.getStateId().getCode());
            } else if (CommonUtils.isNotEmpty(poo.getUnLocationName()) && null != poo.getCountryId()
                    && CommonUtils.isNotEmpty(poo.getCountryId().getCodedesc())) {
                originValues.append(poo.getUnLocationName()).append(",").append(poo.getCountryId().getCodedesc());
            }
        }
        // POL
        StringBuilder polValues = new StringBuilder();
        if (CommonFunctions.isNotNull(polUnlocation)) {
            if (CommonUtils.isNotEmpty(polUnlocation.getUnLocationName()) && null != polUnlocation.getStateId()
                    && polUnlocation.getUnLocationCode().startsWith("US") && CommonUtils.isNotEmpty(polUnlocation.getStateId().getCode())) {
                polValues.append(polUnlocation.getUnLocationName()).append(",").append(polUnlocation.getStateId().getCode());
            } else if (CommonUtils.isNotEmpty(polUnlocation.getUnLocationName())
                    && null != polUnlocation.getCountryId() && CommonUtils.isNotEmpty(polUnlocation.getCountryId().getCodedesc())) {
                polValues.append(polUnlocation.getUnLocationName()).append(",").append(polUnlocation.getCountryId().getCodedesc());
            }
        }

        // POD
        StringBuilder destValues = new StringBuilder();
        if (null != podUnlocation) {
            if (CommonUtils.isNotEmpty(podUnlocation.getUnLocationName()) && null != podUnlocation.getStateId()
                    && podUnlocation.getUnLocationCode().startsWith("US") && CommonUtils.isNotEmpty(podUnlocation.getStateId().getCode())) {
                destValues.append(podUnlocation.getUnLocationName()).append(",").append(podUnlocation.getStateId().getCode());
            } else if (CommonUtils.isNotEmpty(podUnlocation.getUnLocationName())
                    && null != podUnlocation.getCountryId() && CommonUtils.isNotEmpty(podUnlocation.getCountryId().getCodedesc())) {
                destValues.append(podUnlocation.getUnLocationName()).append(",").append(podUnlocation.getCountryId().getCodedesc());
            }
        }

        // Final Destination
        StringBuilder fdValues = new StringBuilder();
        if (CommonFunctions.isNotNull(lclBooking.getFinalDestination())) {
            if (CommonUtils.isNotEmpty(lclBooking.getFinalDestination().getUnLocationName()) && null != lclBooking.getFinalDestination().getStateId()
                    && lclBooking.getFinalDestination().getUnLocationCode().startsWith("US") && CommonUtils.isNotEmpty(lclBooking.getFinalDestination().getStateId().getCode())) {
                fdValues.append(lclBooking.getFinalDestination().getUnLocationName()).append(",").append(lclBooking.getFinalDestination().getStateId().getCode());
            } else if (CommonUtils.isNotEmpty(lclBooking.getFinalDestination().getUnLocationName()) && null != lclBooking.getFinalDestination().getCountryId()
                    && CommonUtils.isNotEmpty(lclBooking.getFinalDestination().getCountryId().getCodedesc())) {
                fdValues.append(lclBooking.getFinalDestination().getUnLocationName()).append(",").append(lclBooking.getFinalDestination().getCountryId().getCodedesc());
            }
        }

        table = new PdfPTable(5);
        table.setWidths(new float[]{2f, 1.9f, 2f, 2f, 2f});
        table.setWidthPercentage(100f);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthRight(0.06f);
        c = new Chunk("Origin Terminal:", headingBoldFont);
        c.setTextRise(7f);
        c.setBackground(new BaseColor(00, 255, 00));
        cell.addElement(c);
        cell.setPaddingBottom(0.5f);
        cell.setPaddingLeft(1f);
        cell.setPaddingTop(1f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthRight(0.06f);
        c = new Chunk("Port of Loading:", headingBoldFont);
        c.setTextRise(7f);
        c.setBackground(new BaseColor(00, 255, 0));
        cell.addElement(c);
        cell.setPaddingBottom(0.5f);
        cell.setPaddingLeft(1f);
        cell.setPaddingTop(1f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthRight(0.06f);
        c = new Chunk("Port of Discharge:", headingBoldFont);
        c.setTextRise(7f);
        c.setBackground(new BaseColor(0, 255, 0));
        cell.addElement(c);
        cell.setPaddingBottom(0.5f);
        cell.setPaddingLeft(1f);
        cell.setPaddingTop(1f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthRight(0.06f);
        c = new Chunk("Destination Port/CFS:", headingBoldFont);
        c.setTextRise(7f);
        c.setBackground(new BaseColor(0, 255, 0));
        cell.addElement(c);
        cell.setPaddingBottom(0.5f);
        cell.setPaddingLeft(1f);
        cell.setPaddingTop(1f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        c = new Chunk("Final Destination:", headingBoldFont);
        c.setTextRise(7f);
        c.setBackground(new BaseColor(0, 255, 0));
        cell.addElement(c);
        cell.setPaddingBottom(0.5f);
        cell.setPaddingLeft(1f);
        cell.setPaddingTop(1f);
        table.addCell(cell);

        //Origin Terminal:
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(-3f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthBottom(0.06f);
        cell.setPaddingBottom(3f);
        p = new Phrase(7f, "" + originValues.toString().toUpperCase(), bodyNormalFont);
        cell.addElement(p);
        table.addCell(cell);

        //pol
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(-3f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthBottom(0.06f);
        p = new Phrase(7f, "" + polValues.toString().toUpperCase(), bodyNormalFont);
        cell.addElement(p);
        table.addCell(cell);

        //pod
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthBottom(0.06f);
        cell.setPaddingTop(-3f);
        p = new Phrase(7f, "" + destValues.toString().toUpperCase(), bodyNormalFont);
        cell.addElement(p);
        table.addCell(cell);

        //Destination Port/CFS
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthBottom(0.06f);
        cell.setPaddingTop(-3f);
        p = new Phrase(7f, "" + destCfs.toString().toUpperCase(), bodyNormalFont);
        cell.addElement(p);
        table.addCell(cell);

        //Final Destination:
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.06f);
        cell.setPaddingTop(-3f);
        String destination_service_city = new LclBookingPadDAO().getCountryForBkgDestinationServices(lclBooking.getFileNumberId());
        p = new Phrase(7f, CommonUtils.isNotEmpty(destination_service_city) ? "" + destination_service_city
                : "" + fdValues.toString().toUpperCase(), bodyNormalFont);
        cell.addElement(p);
        table.addCell(cell);
        return table;
    }

    public static PdfPTable lineTable1() {
        PdfPCell cell = null;
        PdfPTable table = new PdfPTable(1);
        table.setSpacingBefore(2f);
        table.setWidthPercentage(100f);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthTop(0.06f);
        cell.setPadding(1f);
        table.addCell(cell);
        return table;
    }

    public PdfPTable contentDisplayTable() throws DocumentException, Exception {
        Paragraph p = null;
        String comment1 = DR001 != null ? DR001 : "";
        table = new PdfPTable(1);
        table.setWidthPercentage(100f);
        if (fileStatusFlag) {
            cell = new PdfPCell();
            cell.setBorder(0);
            cell.setPadding(0f);
            cell.setPaddingBottom(1f);
            p = new Paragraph(7f, "Received for shipment or for storage prior to customer pick up, the property described, marked as indicated, in apparent good order, except as noted.", contentNormalFont);
            if (ruleName.equalsIgnoreCase("OTI")) {
                p.add(new Chunk("Contents, and conditions of contents, of packages unknown; thus OTI is not responsible for any concealed damage.", headingBoldFontSmall));
                p.add(new Chunk("If for shipment, this property will be held a waiting loading or departure, subject to delay, and/or substitution of other vessels or aircraft and is covered under the terms and conditions of the carrier''s bill of lading and tariff in effect on the date of departure. If for storage, OTI's limit of liability for all loss, damage, or mis-shipment shall not exceed $.50 per 100 lbs, limited to a maximum of $500 for any one dock receipt.", contentNormalFont));
            } else {
                p.add(new Chunk("Contents, and conditions of contents, of packages unknown; thus Econocaribe/Eculine is not responsible for any concealed damage.", headingBoldFontSmall));
                p.add(new Chunk("If for shipment, this property will be held a waiting loading or departure, subject to delay, and/or substitution of other vessels or aircraft and is covered under the terms and conditions of the carrier''s bill of lading and tariff in effect on the date of departure. If for storage, ECI's limit of liability for all loss, damage, or mis-shipment shall not exceed $.50 per 100 lbs, limited to a maximum of $500 for any one dock receipt.", contentNormalFont));
            }
            p.setLeading(7f);
            cell.addElement(p);
            table.addCell(cell);
        } else {
            cell = new PdfPCell();
            cell.setBorder(0);
            cell.setPadding(0f);
            cell.setPaddingBottom(1f);
            p = new Paragraph(5f, "" + comment1, blackContentNormalFont);
            p.setAlignment(Element.ALIGN_LEFT);
            cell.addElement(p);
            table.addCell(cell);
            cell = new PdfPCell();
            cell.setBorder(0);
            p = new Paragraph(6f, "Please forward your paper work to " + companyName + " " + phoneNumber + " " + faxNumber + " * EMAIL: " + emailId, blackContentBoldFont);
            p.setAlignment(Element.ALIGN_LEFT);
            cell.addElement(p);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setFixedHeight(10f);
            cell.setBorder(0);
            table.addCell(cell);
        }

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph("Please Contact Line Manager", headingBoldFont);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);
        return table;
    }

    public PdfPTable commodityContentTable() throws DocumentException {
        table = new PdfPTable(6);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{1.3f, 4.5f, 2f, 4f, 1.8f, 2f});
        Paragraph p = null;

        //pieces values
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        p = new Paragraph(8f, "Number of Pieces", commoditydescBoldFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        cell.setPadding(0f);
        cell.setPaddingTop(2f);
        table.addCell(cell);
        //description values
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        p = new Paragraph(7f, "Description", commoditydescBoldFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        cell.setPadding(0f);
        cell.setPaddingTop(5f);
        table.addCell(cell);
        //marksvalues
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        p = new Paragraph(7f, "Marks & No", commoditydescBoldFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        //cell.setPadding(0f);
        cell.setPaddingTop(5f);
        table.addCell(cell);
        //measure values
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthBottom(0.06f);
        PdfPTable nTable = new PdfPTable(4);
        nTable.setWidthPercentage(100f);
        PdfPCell nCell = null;
        //measurement
        nCell = new PdfPCell();
        nCell.setBorder(0);
        nCell.setBorderColor(new BaseColor(153, 153, 153));
        nCell.setBorderWidthBottom(0.06f);
        nCell.setColspan(4);
        nCell.setPaddingTop(3f);
        nCell.setPaddingBottom(2f);
        p = new Paragraph(5f, "Measurements", commoditydescBoldFont);
        p.setAlignment(Element.ALIGN_CENTER);
        nCell.addElement(p);
        nTable.addCell(nCell);
        //Lvalues
        nCell = new PdfPCell();
        nCell.setBorder(0);
        nCell.setPadding(0f);
        nCell.setBorderColor(new BaseColor(153, 153, 153));
        nCell.setBorderWidthRight(0.06f);
        nCell.setPaddingTop(1f);
        nCell.setPaddingBottom(2f);
        p = new Paragraph(7f, "L", commoditydescBoldFont);
        p.setAlignment(Element.ALIGN_CENTER);
        nCell.addElement(p);
        nTable.addCell(nCell);
        //
        nCell = new PdfPCell();
        nCell.setBorder(0);
        nCell.setPadding(0f);
        nCell.setBorderColor(new BaseColor(153, 153, 153));
        nCell.setBorderWidthRight(0.06f);
        nCell.setPaddingTop(1f);
        nCell.setPaddingBottom(2f);
        p = new Paragraph(7f, "W", commoditydescBoldFont);
        p.setAlignment(Element.ALIGN_CENTER);
        nCell.addElement(p);
        nTable.addCell(nCell);
        //
        nCell = new PdfPCell();
        nCell.setBorder(0);
        nCell.setPadding(0f);
        nCell.setBorderColor(new BaseColor(153, 153, 153));
        nCell.setBorderWidthRight(0.06f);
        nCell.setPaddingTop(1f);
        nCell.setPaddingBottom(2f);
        p = new Paragraph(7f, "H", commoditydescBoldFont);
        p.setAlignment(Element.ALIGN_CENTER);
        nCell.addElement(p);
        nTable.addCell(nCell);
        //
        nCell = new PdfPCell();
        nCell.setBorder(0);
        nCell.setPadding(0f);
        nCell.setPaddingTop(1f);
        nCell.setPaddingBottom(2f);
        p = new Paragraph(7f, "PC", commoditydescBoldFont);
        p.setAlignment(Element.ALIGN_CENTER);
        nCell.addElement(p);
        nTable.addCell(nCell);

        cell.addElement(nTable);
        table.addCell(cell);
        //cubic feet
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        cell.setPaddingTop(5f);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthRight(0.06f);
        p = new Paragraph(7f, "Cubic Feet", commoditydescBoldFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);
        //weight
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        cell.setPaddingTop(5f);
        cell.setPaddingRight(5f);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        p = new Paragraph(7f, "Weight", commoditydescBoldFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);

        table.addCell(cell);

        return table;
    }

    ///values Commodity
    public PdfPTable commodityValuesTable() throws DocumentException, Exception {
        Font marksnumberFont = FontFactory.getFont("Arial", 6f, Font.NORMAL);
        table = new PdfPTable(6);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{1.3f, 4.5f, 2f, 4f, 1.8f, 2f});
        Phrase p1 = null;
        Paragraph p = null;
        customerPo = new Lcl3pRefNoDAO().getCustomerPo(lclBooking.getFileNumberId().toString());
        List<LclBookingPiece> lclBookingPieceList = lclBooking.getLclFileNumber().getLclBookingPieceList();
        if (lclBookingPieceList != null && lclBookingPieceList.size() > 0) {
            for (Object lclBookingList : lclBookingPieceList) {
                LclBookingPiece lclBookingPiece = (LclBookingPiece) lclBookingList;
                //pieces values
                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setBorderWidthRight(0.06f);
                cell.setBorderWidthBottom(0.06f);

                if (lclBookingPiece != null && lclBookingPiece.getActualPieceCount() != null
                        && 0 != lclBookingPiece.getActualPieceCount() && lclBookingPiece.getActualPackageType() != null) {
                    p = new Paragraph(8f, "" + lclBookingPiece.getActualPieceCount() + " " + lclBookingPiece.getActualPackageType().getAbbr01(), commodityNormalFont);
                    pieceCount += lclBookingPiece.getActualPieceCount();
                    p.setAlignment(Element.ALIGN_CENTER);
                } else if (lclBookingPiece != null && lclBookingPiece.getBookedPieceCount() != null && lclBookingPiece.getPackageType().getAbbr01() != null && lclBookingPiece.getPackageType() != null) {
                    p = new Paragraph(8f, "" + lclBookingPiece.getBookedPieceCount() + " " + lclBookingPiece.getPackageType().getAbbr01(), commodityNormalFont);
                    p.setAlignment(Element.ALIGN_CENTER);
                    pieceCount += lclBookingPiece.getBookedPieceCount();
                }
                cell.addElement(p);
                cell.setPaddingBottom(2f);
                cell.setPaddingTop(7f);
                table.addCell(cell);
                //description values
                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setBorderWidthRight(0.06f);
                cell.setBorderWidthBottom(0.06f);
                if (lclBookingPiece != null && lclBookingPiece.getPieceDesc() != null) {
                    p = new Paragraph(7f, "" + lclBookingPiece.getPieceDesc().toUpperCase(), marksnumberFont);
                } else {
                    p = new Paragraph(8f, "", commodityNormalFont);
                }
                p.setAlignment(Element.ALIGN_LEFT);
                cell.addElement(p);
                cell.setPadding(0f);
                cell.setPaddingLeft(1f);
                cell.setPaddingBottom(2f);
                table.addCell(cell);
                //marksvalues
                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setBorderWidthRight(0.06f);
                cell.setBorderWidthBottom(0.06f);
                if (lclBookingPiece != null && CommonUtils.isNotEmpty(lclBookingPiece.getMarkNoDesc())) {
                    p = new Paragraph(7f, "" + lclBookingPiece.getMarkNoDesc().toUpperCase(), marksnumberFont);
                    if (CommonUtils.isNotEmpty(customerPo)) {
                        p.add(" PO#");
                    }
                    p.add(customerPo);
                    customerPo = "";

                } else if (CommonUtils.isNotEmpty(customerPo)) {
                    p = new Paragraph(7f, "PO#" + customerPo, marksnumberFont);
                    customerPo = "";
                }
                p.setAlignment(Element.ALIGN_LEFT);
                cell.addElement(p);
                cell.setPadding(0f);
                cell.setPaddingBottom(2f);
                cell.setPaddingTop(1f);
                table.addCell(cell);
                //measure values
                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setPadding(0f);
                cell.setBorderWidthRight(0.06f);
                cell.setBorderWidthBottom(0.06f);
                PdfPTable nTable = new PdfPTable(4);
                nTable.setWidthPercentage(100f);
                PdfPCell nCell = null;
                //measurement
                //Lvalues
                List<LclBookingPieceDetail> lclBookingPieceDetailList = lclBookingPiece.getLclBookingPieceDetailList();
                if (lclBookingPieceDetailList != null && lclBookingPieceDetailList.size() > 0) {
                    for (int i = 0; i < lclBookingPieceDetailList.size(); i++) {
                        LclBookingPieceDetail lclBookingPieceDetail = (LclBookingPieceDetail) lclBookingPieceDetailList.get(i);
                        nCell = new PdfPCell();
                        nCell.setBorder(0);
                        nCell.setPadding(0f);
                        nCell.setBorderColor(new BaseColor(153, 153, 153));
                        nCell.setPaddingTop(5f);
                        nCell.setPaddingBottom(2f);
                        nCell.setBorderWidthRight(0.06f);
                        nCell.setBorderWidthBottom(0.06f);
                        if (lclBookingPieceDetail != null && lclBookingPieceDetail.getActualLength() != null) {
                            p = new Paragraph(5f, "" + NumberUtils.truncateTwoDecimal(lclBookingPieceDetail.getActualLength().doubleValue()), commodityNormalFont);
                        }
                        p.setAlignment(Element.ALIGN_CENTER);
                        nCell.addElement(p);
                        nTable.addCell(nCell);
                        //
                        nCell = new PdfPCell();
                        nCell.setBorder(0);
                        nCell.setPadding(0f);
                        nCell.setBorderColor(new BaseColor(153, 153, 153));
                        nCell.setBorderWidthRight(0.06f);
                        nCell.setBorderWidthBottom(0.06f);
                        nCell.setPaddingBottom(2f);
                        nCell.setPaddingTop(5f);
                        if (lclBookingPieceDetail != null && lclBookingPieceDetail.getActualWidth() != null) {
                            p = new Paragraph(5f, "" + NumberUtils.truncateTwoDecimal(lclBookingPieceDetail.getActualWidth().doubleValue()), commodityNormalFont);
                            p.setAlignment(Element.ALIGN_CENTER);
                            nCell.addElement(p);
                        }
                        nTable.addCell(nCell);
                        //
                        nCell = new PdfPCell();
                        nCell.setBorder(0);
                        nCell.setPadding(0f);
                        nCell.setBorderColor(new BaseColor(153, 153, 153));
                        nCell.setBorderWidthRight(0.06f);
                        nCell.setBorderWidthBottom(0.06f);
                        nCell.setPaddingBottom(2f);
                        nCell.setPaddingTop(5f);
                        if (lclBookingPieceDetail != null && lclBookingPieceDetail.getActualHeight() != null) {
                            p = new Paragraph(5f, "" + NumberUtils.truncateTwoDecimal(lclBookingPieceDetail.getActualHeight().doubleValue()), commodityNormalFont);
                            p.setAlignment(Element.ALIGN_CENTER);
                            nCell.addElement(p);
                        }
                        nTable.addCell(nCell);
                        //
                        nCell = new PdfPCell();
                        nCell.setBorder(0);
                        nCell.setBorderColor(new BaseColor(153, 153, 153));
                        nCell.setBorderWidthBottom(0.06f);
                        nCell.setPadding(0f);
                        nCell.setPaddingBottom(2f);
                        nCell.setPaddingTop(5f);
                        if (lclBookingPieceDetail != null && lclBookingPieceDetail.getPieceCount() != null) {
                            p = new Paragraph(5f, "" + NumberUtils.truncateTwoDecimal(lclBookingPieceDetail.getPieceCount().doubleValue()), commodityNormalFont);
                            p.setAlignment(Element.ALIGN_CENTER);
                            nCell.addElement(p);
                        }
                        nTable.addCell(nCell);

                    }
                    if (lclBookingPieceDetailList.size() % 2 != 0) {
                        //Lvalues
                        nCell = new PdfPCell();
                        nCell.setBorder(0);
                        nCell.setPadding(0f);
                        nCell.setBorderColor(new BaseColor(153, 153, 153));
                        nCell.setBorderWidthRight(0.06f);
                        nCell.setPadding(5f);
                        p = new Paragraph("", commodityNormalFont);
                        p.setAlignment(Element.ALIGN_CENTER);
                        nCell.addElement(p);
                        nTable.addCell(nCell);
                        //
                        nCell = new PdfPCell();
                        nCell.setBorder(0);
                        nCell.setPadding(0f);
                        nCell.setBorderColor(new BaseColor(153, 153, 153));
                        nCell.setBorderWidthRight(0.06f);
                        p = new Paragraph("", commodityNormalFont);
                        p.setAlignment(Element.ALIGN_CENTER);
                        nCell.addElement(p);
                        nTable.addCell(nCell);
                        //
                        nCell = new PdfPCell();
                        nCell.setBorder(0);
                        nCell.setPadding(0f);
                        nCell.setBorderColor(new BaseColor(153, 153, 153));
                        nCell.setBorderWidthRight(0.06f);
                        p = new Paragraph("", commodityNormalFont);
                        p.setAlignment(Element.ALIGN_CENTER);
                        nCell.addElement(p);
                        nTable.addCell(nCell);
                        //
                        nCell = new PdfPCell();
                        nCell.setBorder(0);
                        p = new Paragraph(11f, "", commodityNormalFont);
                        p.setAlignment(Element.ALIGN_CENTER);
                        nCell.addElement(p);
                        nTable.addCell(nCell);
                    }
                } else {
                    //Lvalues
                    nCell = new PdfPCell();
                    nCell.setBorder(0);
                    nCell.setPadding(0f);
                    nCell.setBorderColor(new BaseColor(153, 153, 153));
                    nCell.setBorderWidthRight(0.06f);
                    nCell.setBorderWidthBottom(0.06f);
                    nCell.setPaddingBottom(0f);
                    nCell.setPadding(5f);
                    p = new Paragraph("", commodityNormalFont);
                    p.setAlignment(Element.ALIGN_CENTER);
                    nCell.addElement(p);
                    nTable.addCell(nCell);
                    //
                    nCell = new PdfPCell();
                    nCell.setBorder(0);
                    nCell.setPadding(5f);
                    nCell.setBorderColor(new BaseColor(153, 153, 153));
                    nCell.setBorderWidthRight(0.06f);
                    nCell.setBorderWidthBottom(0.06f);
                    p = new Paragraph(3f, "", commodityNormalFont);
                    p.setAlignment(Element.ALIGN_CENTER);
                    nCell.addElement(p);
                    nTable.addCell(nCell);
                    //
                    nCell = new PdfPCell();
                    nCell.setBorder(0);
                    nCell.setPadding(5f);
                    nCell.setBorderColor(new BaseColor(153, 153, 153));
                    nCell.setBorderWidthRight(0.06f);
                    nCell.setBorderWidthBottom(0.06f);
                    p = new Paragraph(3f, "", commodityNormalFont);
                    p.setAlignment(Element.ALIGN_CENTER);
                    nCell.addElement(p);
                    nTable.addCell(nCell);
                    //
                    nCell = new PdfPCell();
                    nCell.setBorder(0);
                    nCell.setPadding(5f);
                    nCell.setBorderColor(new BaseColor(153, 153, 153));
                    nCell.setBorderWidthBottom(0.06f);
                    p = new Paragraph(3f, "", commodityNormalFont);
                    p.setAlignment(Element.ALIGN_CENTER);
                    nCell.addElement(p);
                    nTable.addCell(nCell);

                    //Lvalues
                    nCell = new PdfPCell();
                    nCell.setBorder(0);
                    nCell.setBorderColor(new BaseColor(153, 153, 153));
                    nCell.setBorderWidthRight(0.06f);
                    nCell.setBorderWidthBottom(0.06f);
                    nCell.setPadding(5f);
                    p = new Paragraph(3f, "", commodityNormalFont);
                    p.setAlignment(Element.ALIGN_CENTER);
                    nCell.addElement(p);
                    nTable.addCell(nCell);
                    //
                    nCell = new PdfPCell();
                    nCell.setBorder(0);
                    nCell.setPadding(5f);
                    nCell.setBorderColor(new BaseColor(153, 153, 153));
                    nCell.setBorderWidthRight(0.06f);
                    nCell.setBorderWidthBottom(0.06f);
                    p = new Paragraph(3f, "", commodityNormalFont);
                    p.setAlignment(Element.ALIGN_CENTER);
                    nCell.addElement(p);
                    nTable.addCell(nCell);
                    //
                    nCell = new PdfPCell();
                    nCell.setBorder(0);
                    nCell.setPadding(5f);
                    nCell.setBorderColor(new BaseColor(153, 153, 153));
                    nCell.setBorderWidthRight(0.06f);
                    nCell.setBorderWidthBottom(0.06f);
                    p = new Paragraph(3f, "", commodityNormalFont);
                    p.setAlignment(Element.ALIGN_CENTER);
                    nCell.addElement(p);
                    nTable.addCell(nCell);
                    //
                    nCell = new PdfPCell();
                    nCell.setBorder(0);
                    nCell.setPadding(5f);
                    nCell.setBorderColor(new BaseColor(153, 153, 153));
                    nCell.setBorderWidthBottom(0.06f);
                    p = new Paragraph(3f, "", commodityNormalFont);
                    p.setAlignment(Element.ALIGN_CENTER);
                    nCell.addElement(p);
                    nTable.addCell(nCell);
                }
                cell.addElement(nTable);
                table.addCell(cell);
                //cubic feet
                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setPaddingTop(8f);
                cell.setPaddingBottom(2f);
                cell.setPaddingRight(15f);
                cell.setBorderWidthBottom(0.06f);
                cell.setBorderWidthRight(0.06f);
                if (lclBookingPiece != null && lclBookingPiece.getActualVolumeImperial() != null) {
                    p = new Paragraph(6f, "" + NumberUtils.convertToThreeDecimalhash(lclBookingPiece.getActualVolumeImperial().doubleValue()), commodityNormalFont);
                } else if (lclBookingPiece != null && lclBookingPiece.getBookedVolumeImperial() != null) {
                    p = new Paragraph(6f, "" + NumberUtils.convertToThreeDecimalhash(lclBookingPiece.getBookedVolumeImperial().doubleValue()), commodityNormalFont);
                }
                if (null != lclBookingPiece && null != lclBookingPiece.getLclBookingPieceWhseList()
                        && !lclBookingPiece.getLclBookingPieceWhseList().isEmpty()) {
                    int size = lclBookingPiece.getLclBookingPieceWhseList().size();
                    LclBookingPieceWhse pieceWhse = lclBookingPiece.getLclBookingPieceWhseList().get(size - 1);
                    if (CommonFunctions.isNotNull(pieceWhse)) {
                        warehouseNo = pieceWhse.getWarehouse().getWarehouseNo();
                        if (CommonUtils.isNotEmpty(pieceWhse.getLocation())) {
                            warehouseNo += "," + pieceWhse.getLocation();
                        }
                    }
                }
                p.setAlignment(Element.ALIGN_RIGHT);
                cell.addElement(p);
                table.addCell(cell);
                //weight
                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setPaddingBottom(2f);
                cell.setPaddingTop(8f);
                cell.setPaddingRight(15f);
                cell.setBorderWidthBottom(0.06f);
                if (lclBookingPiece != null && lclBookingPiece.getActualWeightImperial() != null) {
                    p = new Paragraph(6f, "" + NumberUtils.convertToThreeDecimalhash(lclBookingPiece.getActualWeightImperial().doubleValue()), commodityNormalFont);
                } else if (lclBookingPiece != null && lclBookingPiece.getBookedWeightImperial() != null) {
                    p = new Paragraph(6f, "" + NumberUtils.convertToThreeDecimalhash(lclBookingPiece.getBookedWeightImperial().doubleValue()), commodityNormalFont);
                }
                cell.addElement(p);
                p.setAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell);
            }
        }
        cell = new PdfPCell();
        p1 = new Phrase("Location:" + warehouseNo.toUpperCase(), resultComFont);
        cell.setColspan(4);
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.addElement(p1);
        table.addCell(cell);

        List<LclBookingPiece> lclBookingPieceListCBM = lclBooking.getLclFileNumber().getLclBookingPieceList();
        if (lclBookingPieceListCBM != null && lclBookingPieceListCBM.size() > 0) {
            for (Object lclBookingListd : lclBookingPieceListCBM) {
                LclBookingPiece lclBookingPiece = (LclBookingPiece) lclBookingListd;
                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setBorderWidthBottom(0.06f);
                cell.setBorderWidthRight(0.06f);
                if (lclBookingPiece.getActualVolumeMetric() != null) {
                    p1 = new Phrase("CBM:" + NumberUtils.convertToThreeDecimalhash(lclBookingPiece.getActualVolumeMetric().doubleValue()), resultComFont);
                } else if (lclBookingPiece.getBookedVolumeMetric() != null) {
                    p1 = new Phrase("CBM:" + NumberUtils.convertToThreeDecimalhash(lclBookingPiece.getBookedVolumeMetric().doubleValue()), resultComFont);
                } else {
                    p1 = new Phrase("");
                }
                p1.setLeading(8f);
                cell.addElement(p1);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setBorderWidthBottom(0.06f);
                if (lclBookingPiece.getActualWeightMetric() != null) {
                    p1 = new Phrase("KILOS:" + NumberUtils.convertToThreeDecimalhash(lclBookingPiece.getActualWeightMetric().doubleValue()), resultComFont);
                } else if (lclBookingPiece.getBookedWeightMetric() != null) {
                    p1 = new Phrase("KILOS:" + NumberUtils.convertToThreeDecimalhash(lclBookingPiece.getBookedWeightMetric().doubleValue()), resultComFont);
                }
                p1.setLeading(8f);
                cell.addElement(p1);
                table.addCell(cell);
            }
        }
        return table;
    }

    public PdfPTable additionalTable() throws DocumentException, Exception {
        Phrase p = null;
        Paragraph addPValues = null;
        table = new PdfPTable(3);
        table.setSpacingBefore(2f);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{2.5f, 0.03f, 5.5f});

        //Additional column
        cell = new PdfPCell();
        cell.setRowspan(2);
        cell.setBorder(0);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthRight(0.06f);
        addPValues = new Paragraph(7f, "Additional Information", headingBoldFont);
        addPValues.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(addPValues);
        addPValues = new Paragraph(9f, additionalRefNo.toUpperCase(), fontCompNormalSub);
        addPValues.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(addPValues);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(2);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setPaddingTop(0.06f);
        cell.setBackgroundColor(BaseColor.WHITE);
        table.addCell(cell);

        //
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.06f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBackgroundColor(BaseColor.YELLOW);
        addPValues = new Paragraph(5f, "Special Remarks For This Port", headingBoldFont);
        addPValues.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(addPValues);
        table.addCell(cell);
        //
        cell = new PdfPCell();
        cell.setBorder(0);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.06f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingLeft(3f);
        cell.setPaddingTop(2f);
        cell.setPaddingBottom(3f);
        if (!"".equals(specialRemarks)) {
            addPValues = new Paragraph(8f, "" + specialRemarks, fontCompNormalSub);
        } else {
            addPValues = new Paragraph(8f, "", fontCompNormalSub);
            cell.setPadding(10f);
        }
        addPValues.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(addPValues);
        table.addCell(cell);
        //
        cell = new PdfPCell();
        cell.setBorder(0);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.06f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBackgroundColor(BaseColor.YELLOW);
        String comment2 = DR002 != null ? DR002 : "";
        addPValues = new Paragraph(6f, "" + comment2, headingBoldFont);
        addPValues.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(addPValues);
        table.addCell(cell);
        //
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        cell.setBorderWidthBottom(0.06f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthBottom(0.06f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        cell.setBackgroundColor(BaseColor.WHITE);
        table.addCell(cell);
        return table;
    }

    public PdfPTable deliverTable(String documentName) throws DocumentException, Exception {
        Font fontTimeBlue = FontFactory.getFont("Courier-Bold", 8.5f, Font.NORMAL, new BaseColor(00, 00, 153));
        List formattedChargesList = null;
        Font addressFont = FontFactory.getFont("Arial", 10f, Font.BOLD, new BaseColor(00, 102, 25));
        Font redFont = new Font(FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.RED);
        StringBuilder oceanRates = new StringBuilder();
        Paragraph pdeliver = null;
        PdfPCell cell2 = null;
        table = new PdfPTable(3);
        table.setSpacingBefore(5f);
        table.setSpacingAfter(3f);
        table.setWidths(new float[]{2.2f, 1.3f, 4f});
        table.setWidthPercentage(100f);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setRowspan(2);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        cell.setPaddingTop(1f);
        pdeliver = new Paragraph(10f, "Deliver Cargo To:" + deliverCargoValues.toString(), addressFont);
        pdeliver.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(pdeliver);
        table.addCell(cell);
        List<LclBookingPiece> lclBookingPieceList = lclBooking.getLclFileNumber().getLclBookingPieceList();
        if (lclBookingPieceList != null && lclBookingPieceList.size() > 0) {
            for (LclBookingPiece lclBookingPiece : lclBookingPieceList) {
                if (lclBookingPiece.getCommodityType() != null && lclBookingPiece.getCommodityType().getCode() != null && lclBookingPiece.getCommodityType().getDescEn() != null) {
                    oceanRates.append(lclBookingPiece.getCommodityType().getCode()).append(" ").append(lclBookingPiece.getCommodityType().getDescEn()).append(",");
                }
            }
        }
        String commodityCode = StringUtils.substringBeforeLast(oceanRates.toString(), ",");
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(2);
        cell.setPaddingTop(1f);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthBottom(0.06f);
        pdeliver = new Paragraph(8f, "Ocean Rates:  " + commodityCode, headingBoldFont);
        pdeliver.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(pdeliver);
        table.addCell(cell);

        List<LclBookingPiece> lclCommodityList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", lclBooking.getFileNumberId());
        List<LclBookingAc> chargeList = lclCostChargesDAO.getLclCostByFileNumberAsc(lclBooking.getFileNumberId(), LclCommonConstant.LCL_EXPORT);
        Double minchg = 0.0;
        Double bundleMinchg = 0.0;
        Double barrelBundlechg = 0.0;
        Double flatRateMinimum = 0.0;
        Double ratePervolumeUnit = 0.0;
        Double ratePerWeightUnit = 0.0;
        Double ttcbmcftrate = 0.0;
        Double bundleTtcbmcftrate = 0.0;
        String label = "";
        Double ttlbskgsrate = 0.0;
        Double bundleTtlbskgsrate = 0.0;
        Double arAmount = 0.0;
        Double arValue = 0.0;
        Double arRate = 0.0;
        Double barrelRatePerVolumeUnit = 0.0;
        Double barrelRatePerWeightUnit = 0.0;
        Double ttbarrelcbmcftrate = 0.0;
        Double ttbarrellbskgsrate = 0.0;
        Double barrelAmount = 0.0;
        BigDecimal adjusmentAmt = new BigDecimal(0.00);
        boolean is_OFBARR_Bundle = false;
        boolean OFBARR_Bundle = false;
        for (int i = 0; i < chargeList.size(); i++) {
            LclBookingAc lclBookingAc = (LclBookingAc) chargeList.get(i);
            adjusmentAmt = lclBookingAc.getAdjustmentAmount() != null ? lclBookingAc.getAdjustmentAmount() : new BigDecimal(0.00);
            if (lclBookingAc.getBundleIntoOf() && lclBookingAc.getPrintOnBl()) {
                if ("OFBARR".equalsIgnoreCase(lclBookingAc.getArglMapping().getChargeCode())) {
                    is_OFBARR_Bundle = true;
                    OFBARR_Bundle = true;
                }
                if (OFBARR_Bundle) {
                    if (!CommonUtils.isEmpty(lclBookingAc.getRateFlatMinimum())) {
                        minchg += lclBookingAc.getRateFlatMinimum().doubleValue();
                    }
                    if (!CommonUtils.isEmpty(lclBookingAc.getRatePerVolumeUnit())) {
                        ttcbmcftrate += lclBookingAc.getRatePerVolumeUnit().doubleValue();
                    }
                    if (!CommonUtils.isEmpty(lclBookingAc.getRatePerWeightUnit())) {
                        ttlbskgsrate += lclBookingAc.getRatePerWeightUnit().doubleValue();
                    }
                    if ("TTBARR".equalsIgnoreCase(lclBookingAc.getArglMapping().getChargeCode())) {
                        OFBARR_Bundle = false;
                    }
                } else if (CommonUtils.notIn(lclBookingAc.getArglMapping().getChargeCode(), "OFBARR", "TTBARR")) {
                    if (!CommonUtils.isEmpty(lclBookingAc.getRateFlatMinimum())) {
                        minchg += lclBookingAc.getRateFlatMinimum().doubleValue();
                    }
                    if (!CommonUtils.isEmpty(lclBookingAc.getRatePerVolumeUnit())) {
                        ttcbmcftrate += lclBookingAc.getRatePerVolumeUnit().doubleValue();
                    }
                    if (!CommonUtils.isEmpty(lclBookingAc.getRatePerWeightUnit())) {
                        ttlbskgsrate += lclBookingAc.getRatePerWeightUnit().doubleValue();
                    }
                }

                if ("TTBARR".equalsIgnoreCase(lclBookingAc.getArglMapping().getChargeCode()) && !is_OFBARR_Bundle) {
                    if (null != lclBookingAc.getRolledupCharges()) {
                        barrelBundlechg += lclBookingAc.getRolledupCharges().doubleValue() + adjusmentAmt.doubleValue();
                    } else if (!CommonUtils.isEmpty(lclBookingAc.getArAmount())) {
                        barrelBundlechg += lclBookingAc.getArAmount().doubleValue() + adjusmentAmt.doubleValue();
                    }
                    if (!CommonUtils.isEmpty(lclBookingAc.getRatePerVolumeUnit())) {
                        ttbarrelcbmcftrate += lclBookingAc.getRatePerVolumeUnit().doubleValue();
                    }
                    if (!CommonUtils.isEmpty(lclBookingAc.getRatePerWeightUnit())) {
                        ttbarrellbskgsrate += lclBookingAc.getRatePerWeightUnit().doubleValue();
                    }
                } else {
                    if (!CommonUtils.isEmpty(lclBookingAc.getRolledupCharges())) {
                        bundleMinchg += lclBookingAc.getRolledupCharges().doubleValue() + adjusmentAmt.doubleValue();
                    } else if (!CommonUtils.isEmpty(lclBookingAc.getArAmount())) {
                        bundleMinchg += lclBookingAc.getArAmount().doubleValue() + adjusmentAmt.doubleValue();
                    }
                }
            }

        }
        for (int k = 0; k < chargeList.size(); k++) {
            LclBookingAc lclBookingAc = (LclBookingAc) chargeList.get(k);
            adjusmentAmt = lclBookingAc.getAdjustmentAmount() != null ? lclBookingAc.getAdjustmentAmount() : new BigDecimal(0.00);
            if (lclBookingAc != null && !lclBookingAc.getBundleIntoOf()) {
                if (lclBookingAc.getArglMapping().getChargeCode().equals(CommonConstants.OFR_CHARGECODE)) {
                    flatRateMinimum += lclBookingAc.getRateFlatMinimum().doubleValue() + minchg;
                    ratePervolumeUnit += lclBookingAc.getRatePerVolumeUnit().doubleValue() + ttcbmcftrate;
                    ratePerWeightUnit += lclBookingAc.getRatePerWeightUnit().doubleValue() + ttlbskgsrate;
                    if (lclBookingAc != null && lclBookingAc.getRolledupCharges() != null) {
                        arRate += lclBookingAc.getRolledupCharges().doubleValue() + bundleMinchg;
                    } else if (lclBookingAc != null && lclBookingAc.getArAmount() != null) {
                        arRate += lclBookingAc.getArAmount().doubleValue() + bundleMinchg;
                    }
                } else if (!is_OFBARR_Bundle && lclBookingAc.getArglMapping().getChargeCode().equalsIgnoreCase("OFBARR")) {
                    if (null != lclBookingAc.getRolledupCharges()) {
                        barrelAmount = lclBookingAc.getRolledupCharges().doubleValue() + barrelBundlechg;
                    } else if (lclBookingAc.getArAmount() != null) {
                        barrelAmount = lclBookingAc.getArAmount().doubleValue() + barrelBundlechg;
                    }
                    if (!CommonUtils.isEmpty(lclBookingAc.getRatePerVolumeUnit())) {
                        barrelRatePerVolumeUnit = lclBookingAc.getRatePerVolumeUnit().doubleValue() + ttbarrelcbmcftrate;
                    }
                    if (!CommonUtils.isEmpty(lclBookingAc.getRatePerWeightUnit())) {
                        barrelRatePerWeightUnit = lclBookingAc.getRatePerWeightUnit().doubleValue() + ttbarrellbskgsrate;
                    }
                }
            }
        }
        if (chargeList != null && chargeList.size() > 0 && lclBookingPieceList != null) {
            PortsDAO portsDAO = new PortsDAO();
            Ports ports = portsDAO.getByProperty("unLocationCode", lclBooking.getFinalDestination().getUnLocationCode());
            if (ports.getEngmet() != null) {
                if (ports.getEngmet().equalsIgnoreCase("E")) {
                    label += "$" + NumberUtils.convertToTwoDecimal(ratePervolumeUnit) + " CFT " + NumberUtils.convertToTwoDecimal(ratePerWeightUnit) + "/100.00 LBS ($" + NumberUtils.convertToTwoDecimal(flatRateMinimum) + " Min)";
                } else if (ports.getEngmet().equalsIgnoreCase("M")) {
                    label += "$" + NumberUtils.convertToTwoDecimal(ratePervolumeUnit) + " CBM " + NumberUtils.convertToTwoDecimal(ratePerWeightUnit) + "/1000.00 KGS ($" + NumberUtils.convertToTwoDecimal(flatRateMinimum) + " Min)";
                }
            }
            formattedChargesList = new ExportBookingUtils().setLabelCharges(chargeList, lclCommodityList, ports.getEngmet());
        }
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthBottom(0.06f);
        cell.setPaddingTop(1f);
        if (formattedChargesList != null && formattedChargesList.size() > 0) {
            String chargeDesc = null;
            LclBookingAc lclBookingAc = null;
            if (documentName.equalsIgnoreCase("Booking Confirmation With Rate")) {
                for (Object lclBookingAcList : formattedChargesList) {
                    lclBookingAc = (LclBookingAc) lclBookingAcList;
                    if (lclBookingAc != null && lclBookingAc.getArglMapping() != null && CommonUtils.isNotEmpty(lclBookingAc.getArglMapping().getChargeDescriptions())) {
                        chargeDesc = lclBookingAc.getArglMapping().getChargeDescriptions();
                    } else if (lclBookingAc != null && lclBookingAc.getArglMapping() != null && CommonUtils.isNotEmpty(lclBookingAc.getArglMapping().getChargeCode())) {
                        chargeDesc = lclBookingAc.getArglMapping().getChargeCode();
                    }
                    if (!lclBookingAc.getBundleIntoOf() && lclBookingAc.getPrintOnBl()) {
                        pdeliver = new Paragraph(8f, "" + chargeDesc, fontTimeBlue);
                        cell.addElement(pdeliver);
                    }
                }
                if (CommonUtils.isNotEmpty(dispo) && !"OBKG".equalsIgnoreCase(dispo) && !"RUNV".equalsIgnoreCase(dispo)) {
                    pdeliver = new Paragraph(12f, " Total($-USD)", redFont);
                    pdeliver.setAlignment(Element.ALIGN_CENTER);
                    cell.addElement(pdeliver);
                }
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setPaddingTop(-1f);
                PdfPTable ntable1 = new PdfPTable(2);
                float width = 0.1f, widthy = 6f;
                if (CommonUtils.isNotEmpty(dispo) && !"OBKG".equalsIgnoreCase(dispo) && !"RUNV".equalsIgnoreCase(dispo)) {
                    width = 1f;// after cargo received
                    widthy = 4.8f;
                }
                ntable1.setWidths(new float[]{width, widthy});
                ntable1.setWidthPercentage(99f);
                cell2 = new PdfPCell();
                cell2.setBorder(0);
                // cell2.setPaddingTop(1f);
                // cell.setBorderWidthLeft(0.06f);
                // cell.setBorderWidthBottom(0.06f);
                // cell.setPaddingTop(1f);
                if (CommonUtils.isNotEmpty(dispo) && !"OBKG".equalsIgnoreCase(dispo) && !"RUNV".equalsIgnoreCase(dispo)) {
                    for (Object lclBookingAcList : formattedChargesList) {
                        lclBookingAc = (LclBookingAc) lclBookingAcList;
                        adjusmentAmt = lclBookingAc.getAdjustmentAmount() != null ? lclBookingAc.getAdjustmentAmount() : new BigDecimal(0.00);
                        if (lclBookingAc.getPrintOnBl() && !lclBookingAc.getBundleIntoOf()) {
                            if (lclBookingAc.getArglMapping().getChargeCode().equals(CommonConstants.OFR_CHARGECODE)) {
                                Double arAmt = arRate + adjusmentAmt.doubleValue();
                                pdeliver = new Paragraph(8f, "" + NumberUtils.convertToTwoDecimal(arAmt), fontTimeBlue);

                            } else if (lclBookingAc.getArglMapping().getChargeCode().equals("OFBARR")) {
                                Double barrelAmt = barrelAmount + adjusmentAmt.doubleValue();
                                pdeliver = new Paragraph(8f, "" + NumberUtils.convertToTwoDecimal(barrelAmt), fontTimeBlue);
                            } else {
                                Double arAmt = 0.0;
                                if (lclBookingAc.getRolledupCharges() != null) {
                                    arAmt = lclBookingAc.getRolledupCharges().doubleValue() + adjusmentAmt.doubleValue();
                                } else if (lclBookingAc.getArAmount() != null) {
                                    arAmt = lclBookingAc.getArAmount().doubleValue() + adjusmentAmt.doubleValue();
                                }
                                pdeliver = new Paragraph(8f, NumberUtils.convertToTwoDecimal(arAmt), fontTimeBlue);
                            }
                            pdeliver.setAlignment(Element.ALIGN_LEFT);
                            cell2.addElement(pdeliver);
                        }
                        if (lclBookingAc.getPrintOnBl()) {
                            if (!CommonUtils.isEmpty(lclBookingAc.getRolledupCharges())) {
                                arAmount += lclBookingAc.getRolledupCharges().doubleValue() + adjusmentAmt.doubleValue();
                            } else if (!CommonUtils.isEmpty(lclBookingAc.getArAmount())) {
                                arAmount += lclBookingAc.getArAmount().doubleValue() + adjusmentAmt.doubleValue();
                            }
                        }
                    }
                } else {
                    pdeliver = new Paragraph(8f, "", fontTimeBlue);
                    pdeliver.setAlignment(Element.ALIGN_LEFT);
                    cell2.addElement(pdeliver);
                }
                if (CommonUtils.isNotEmpty(dispo) && !"OBKG".equalsIgnoreCase(dispo) && !"RUNV".equalsIgnoreCase(dispo)) {
                    pdeliver = new Paragraph(12f, "" + NumberUtils.convertToTwoDecimal(arAmount), redFont);
                    pdeliver.setAlignment(Element.ALIGN_LEFT);
                    cell2.addElement(pdeliver);
                }

                ntable1.addCell(cell2);

                cell2 = new PdfPCell();
                cell2.setBorder(0);
                // cell2.setBorderWidthBottom(0.06f);
                cell2.setPaddingTop(1f);
                for (Object lclBookingAcList : formattedChargesList) {
                    lclBookingAc = (LclBookingAc) lclBookingAcList;
                    if (lclBookingAc != null && lclBookingAc.getArglMapping() != null && CommonUtils.isNotEmpty(lclBookingAc.getArglMapping().getChargeDescriptions())) {
                        chargeDesc = lclBookingAc.getArglMapping().getChargeDescriptions();
                    } else if (lclBookingAc != null && lclBookingAc.getArglMapping() != null && CommonUtils.isNotEmpty(lclBookingAc.getArglMapping().getChargeCode())) {
                        chargeDesc = lclBookingAc.getArglMapping().getChargeCode();
                    }

                    if (lclBookingAc.getPrintOnBl() && !lclBookingAc.getBundleIntoOf()) {
                        if (lclBookingAc.getArglMapping().getChargeCode().equals(CommonConstants.OFR_CHARGECODE)) {
                            pdeliver = new Paragraph(8f, "" + label, fontTimeBlue);
                        } else if (lclBookingAc.getArglMapping().getChargeCode().equals("OFBARR")) {
                            if (barrelRatePerVolumeUnit > 0.00) {
                                pdeliver = new Paragraph(8f, "$" + NumberUtils.convertToTwoDecimal(barrelRatePerVolumeUnit) + " PER BARREL.", fontTimeBlue);
                            } else if (barrelRatePerWeightUnit > 0.00) {
                                pdeliver = new Paragraph(8f, "$" + NumberUtils.convertToTwoDecimal(barrelRatePerWeightUnit) + " PER BARREL.", fontTimeBlue);
                            }
                        } else {
                            pdeliver = new Paragraph(8f, "" + lclBookingAc.getLabel2(), fontTimeBlue);
                        }
                        pdeliver.setAlignment(Element.ALIGN_LEFT);
                        cell2.addElement(pdeliver);
                    }
                }

                ntable1.addCell(cell2);
                cell.addElement(ntable1);
                table.addCell(cell);
            }
        } else {
            cell.setColspan(2);
            cell.setFixedHeight(5f);
            pdeliver = new Paragraph(8f, "", voyageBlueFont);
            cell.addElement(pdeliver);
            table.addCell(cell);
        }

        String comment3 = DR003 != null ? DR003 : "";

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(3);
        cell.setBorderWidthTop(0.06f);
        cell.setBackgroundColor(BaseColor.WHITE);
        cell.setPaddingTop(0.06f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(3);
        cell.setBackgroundColor(new BaseColor(0, 255, 0));
        cell.setPadding(0f);
        cell.setPaddingBottom(2f);
        pdeliver = new Paragraph(8f, "" + comment3, commoditydescBoldFont);
        pdeliver.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(pdeliver);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(3);
        cell.setBorderWidthBottom(0.06f);
        cell.setBackgroundColor(BaseColor.WHITE);
        cell.setPaddingTop(0.06f);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(3);
        cell.setBorderWidthBottom(0.06f);
        cell.setBackgroundColor(BaseColor.WHITE);
        cell.setPaddingTop(0.06f);
        table.addCell(cell);
        return table;
    }

    public PdfPTable overseasTable() throws DocumentException, Exception {
        StringBuilder agentDetails = new StringBuilder();
        if (CommonFunctions.isNotNull(lclBooking) && CommonFunctions.isNotNull(lclBooking.getAgentAcct()) && CommonFunctions.isNotNull(lclBooking.getAgentAcct().getAccountno())) {
            CustAddress custAddress = new CustAddressDAO().findPrimeContact(lclBooking.getAgentAcct().getAccountno());
            if (CommonFunctions.isNotNull(custAddress)) {
                if (CommonFunctions.isNotNull(custAddress.getAcctName())) {
                    agentDetails.append(custAddress.getAcctName()).append("  ");
                }
                if (CommonFunctions.isNotNull(custAddress.getPhone())) {
                    agentDetails.append("Phone: ").append(custAddress.getPhone()).append("    ");
                }
                if (CommonFunctions.isNotNull(custAddress.getFax())) {
                    agentDetails.append("Fax: ").append(custAddress.getFax()).append("\n");
                }
                if (CommonFunctions.isNotNull(custAddress.getAddress1())) {
                    agentDetails.append(custAddress.getAddress1()).append("  ");
                }
                if (CommonFunctions.isNotNull(custAddress.getCity1())) {
                    agentDetails.append(custAddress.getCity1()).append(",  ");
                }
                if (CommonFunctions.isNotNull(custAddress.getZip())) {
                    agentDetails.append(custAddress.getZip()).append("  ");
                }
            }
        }
        table = new PdfPTable(2);
        table.setWidths(new float[]{0.6f, 4f});
        table.setWidthPercentage(100f);
        Paragraph pValues = null;
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6f);
        pValues = new Paragraph(8f, "Overseas Office:", commoditydescBoldFont);
        pValues.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(pValues);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6f);
        pValues = new Paragraph(8f, "" + agentDetails.toString(), fontCompNormalSub);
        pValues.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(pValues);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(2);
        cell.setPadding(10f);
        table.addCell(cell);
        return table;
    }

    public PdfPTable voyageTable(String documentName) throws DocumentException, Exception {
        Font fontTimeBlue = FontFactory.getFont("Courier-Bold", 8.5f, Font.NORMAL, new BaseColor(00, 00, 153));
        Font voyageSubFont = FontFactory.getFont("Arial", 8.5f, Font.BOLD);
        Font voyageboxFont = FontFactory.getFont("Arial", 9f, Font.NORMAL);
        Date originLrd = null;
        Date loadLrd;
        Date podAtEta, fdEta;
        long ttPooFd = 0;
        long ttPolPod = 0;
        Integer pooId = null;
        if (null != ssDetail) {
            LclBookingPlanBean bookingPlanBean = null;
            LclBookingPlanDAO bookingPlanDAO = new LclBookingPlanDAO();
            if ("T".equalsIgnoreCase(lclBooking.getBookingType())) {
                pooId = pol != null ? pol.getId() : null;
                if (pol != null && pod != null) {
                    bookingPlanBean = bookingPlanDAO.getRelayOverride(pol.getId(), pol.getId(), pod.getId(),
                            pod.getId(), 0);
                }
            } else {
                pooId = lclBooking.getPortOfOrigin() != null ? lclBooking.getPortOfOrigin().getId() : null;
                polId = lclBooking.getPortOfLoading().getId();
                if (!lclBooking.getRelayOverride()) {
                    bookingPlanBean = bookingPlanDAO.getRelay(lclBooking.getPortOfOrigin().getId(),
                            lclBooking.getFinalDestination().getId(), "N");

                } else if (lclBooking.getPortOfLoading() != null && lclBooking.getPortOfDestination() != null
                        && lclBooking.getRelayOverride()) {
                    bookingPlanBean = bookingPlanDAO.getRelayOverride(lclBooking.getPortOfOrigin().getId(),
                            lclBooking.getPortOfLoading().getId(), lclBooking.getPortOfDestination().getId(),
                            lclBooking.getFinalDestination().getId(), 0);

                }
            }

            if (null != bookingPlanBean) {
                Calendar calLoadingLRD = Calendar.getInstance();
                calLoadingLRD.setTime(ssDetail.getStd());

                /**
                 * POL Cut-off Days Before Departure Use LRD override if present
                 * otherwise use the relay CO_DBD.
                 */
                calLoadingLRD.add(Calendar.DATE, -(null != ssDetail.getRelayLrdOverride() ? ssDetail.getRelayLrdOverride() : bookingPlanBean.getPol_co_dbd()));

                /* Don't permit Weekend days (Sat, Sun) */
                loadLrd = calLoadingLRD.getTime();
                if (loadLrd.getDay() == 0) {
                    calLoadingLRD.add(Calendar.DATE, -2);
                } else if (loadLrd.getDay() == 6) {
                    calLoadingLRD.add(Calendar.DATE, -1);
                }

                /**
                 * POL Cut-Off Day of Week (CO_DOW) Logic assumes Sat/Sun are
                 * never specified in CO_DOW.
                 */
                loadLrd = calLoadingLRD.getTime();
                if (CommonUtils.isNotEmpty(bookingPlanBean.getPol_co_dow())) {
                    int mDays = 0;

                    if (loadLrd.getDay() > bookingPlanBean.getPol_co_dow()) {
                        /* Backup to the Cut-Off Day of Week in the CURRENT week */
                        mDays = loadLrd.getDay() - bookingPlanBean.getPol_co_dow();
                    } else if (loadLrd.getDay() < bookingPlanBean.getPol_co_dow()) {
                        /* Backup to the Cut-Off Day of Week in the PREVIOUS week */
                        mDays = 7 - (bookingPlanBean.getPol_co_dow() - loadLrd.getDay());
                    }
                    calLoadingLRD.add(Calendar.DATE, -mDays);
                }

                /* Check for Alternate LRD */
                Date altDate = new LclBookingPlanDAO().getAltDateByOriginalDate(pooId, polId, "LRD", calLoadingLRD.getTime());
                calLoadingLRD.setTime(altDate);

                /* Set Cut-Off Time Of Day (CO_TOD) */
                if (null != bookingPlanBean.getPol_co_tod()) {
                    calLoadingLRD.add(Calendar.HOUR, bookingPlanBean.getPol_co_tod().getHours());
                    calLoadingLRD.add(Calendar.MINUTE, bookingPlanBean.getPol_co_tod().getMinutes());
                    calLoadingLRD.add(Calendar.SECOND, bookingPlanBean.getPol_co_tod().getSeconds());
                }

                loadLrd = calLoadingLRD.getTime();

                /**
                 * ***************************************************
                 * Origin LRD (Last Receive Date) Computed when POO is not the
                 * same as POL.
                 *
                 * @ticket 0009282, 0011597
                 * ***************************************************
                 */
                originLrd = loadLrd;
                if (pooId.intValue() != polId.intValue()) {
                    originLrd = DateUtils.formatDateAndParseToDate(loadLrd);
                    /* POO is not the same as POL, compute it. */
                    Calendar calOriginLRD = Calendar.getInstance();
                    calOriginLRD.setTime(originLrd);

                    /* Subtract POO -> POL Transit Time (in days) */
                    calOriginLRD.add(Calendar.DATE, -bookingPlanBean.getPoo_transit_time());

                    /**
                     * POO Cut-Off Day of Week (CO_DOW) Logic assumes Sat/Sun
                     * are never specified in CO_DOW.
                     */
                    originLrd = calOriginLRD.getTime();
                    if (CommonUtils.isNotEmpty(bookingPlanBean.getPoo_co_dow())) {
                        int mDays = 0;

                        if (originLrd.getDay() > bookingPlanBean.getPoo_co_dow()) {
                            /* Backup to the Cut-Off Day of Week in the CURRENT week */
                            mDays = originLrd.getDay() - bookingPlanBean.getPoo_co_dow();
                        } else if (originLrd.getDay() < bookingPlanBean.getPoo_co_dow()) {
                            /* Backup to the Cut-Off Day of Week in the PREVIOUS week */
                            mDays = 7 - (bookingPlanBean.getPoo_co_dow() - originLrd.getDay());
                        }

                        calOriginLRD.add(Calendar.DATE, -mDays);
                    }

                    /* Check for Alternate LRD */
                    altDate = new LclBookingPlanDAO().getAltDateByOriginalDate(pooId, polId, "LRD", calOriginLRD.getTime());
                    calOriginLRD.setTime(altDate);

                    /* Set Cut-Off Time Of Day (CO_TOD) */
                    if (null != bookingPlanBean.getPoo_co_tod()) {
                        calOriginLRD.add(Calendar.HOUR, bookingPlanBean.getPoo_co_tod().getHours());
                        calOriginLRD.add(Calendar.MINUTE, bookingPlanBean.getPoo_co_tod().getMinutes());
                        calOriginLRD.add(Calendar.SECOND, bookingPlanBean.getPoo_co_tod().getSeconds());
                    }

                    originLrd = calOriginLRD.getTime();
                } // Origin LRD

                /**
                 * ***************************************************
                 * POD ETA ***************************************************
                 */
                Calendar calPODETA = Calendar.getInstance();
                calPODETA.setTime(ssDetail.getStd());
                calPODETA.add(Calendar.DATE, +(null != ssDetail.getRelayTtOverride() ? ssDetail.getRelayTtOverride() : bookingPlanBean.getPol_transit_time()));
                podAtEta = calPODETA.getTime();

                /**
                 * ***************************************************
                 * FD ETA ***************************************************
                 */
                fdEta = podAtEta;
                if (!Objects.equals(bookingPlanBean.getPod_id(), bookingPlanBean.getFd_id())) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(podAtEta);
                    cal.add(Calendar.DATE, bookingPlanBean.getFd_transit_time());
                    fdEta = cal.getTime();
                    /**
                     * NOTE: lcl_booking.pod_fd_tt must be added to this
                     * datetime value elsewhere.
                     */
                }

                if (podAtEta != null && ssDetail.getStd() != null) {
                    ttPolPod = DateUtils.daysBetween(ssDetail.getStd(), podAtEta);
                }

                if (originLrd != null && fdEta != null) {
                    //getGeneralLrdt(), getStd()
                    ttPooFd = DateUtils.daysBetween(originLrd, fdEta);
                }
            }
        }

        PdfPTable table = null;
        PdfPCell cell = null;
        Phrase p = null;
        table = new PdfPTable(2);
        table.setWidths(new float[]{4f, 4f});
        table.setWidthPercentage(100f);
        Paragraph pValues = null;

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthTop(1f);
        cell.setBorderWidthRight(1f);
        cell.setBorderWidthBottom(1f);
        PdfPTable vesseltable = new PdfPTable(5);
        vesseltable.setWidthPercentage(101.3f);
        vesseltable.setWidths(new float[]{1.8f, 0.1f, 3f, 1f, 1f});
        PdfPCell vesselcell = null;
        vesselcell = new PdfPCell();
        vesselcell.setColspan(5);
        vesselcell.setBorder(0);
        vesselcell.setBorderWidthBottom(0.06f);
        pValues = new Paragraph(6f, "Updated Voyage Information", headingBoldFont);
        pValues.setAlignment(Element.ALIGN_CENTER);
        vesselcell.addElement(pValues);
        vesseltable.addCell(vesselcell);

        vesselcell = new PdfPCell();
        vesselcell.setPaddingLeft(-1f);
        vesselcell.setBorder(0);
        vesselcell.setPadding(0f);
        vesselcell.setPaddingTop(2f);
        pValues = new Paragraph(8f, "Voyage. . . . . . . . . . .", voyageBlueFont);
        pValues.setAlignment(Element.ALIGN_LEFT);
        vesselcell.addElement(pValues);
        vesseltable.addCell(vesselcell);

        vesselcell = new PdfPCell();
        vesselcell.setBorder(0);
        vesselcell.setPadding(0f);
        vesselcell.setPaddingTop(2f);
        pValues = new Paragraph(8f, ":", voyageBlueFont);
        vesselcell.addElement(pValues);
        vesseltable.addCell(vesselcell);

        vesselcell = new PdfPCell();
        vesselcell.setColspan(3);
        vesselcell.setPaddingLeft(-1f);
        vesselcell.setPadding(0f);
        vesselcell.setPaddingTop(2f);
        vesselcell.setBorder(0);
        if (ssDetail != null) {
            pValues = new Paragraph(8f, "" + ssDetail.getLclSsHeader().getScheduleNo(), voyageGreenFont);
        } else {
            pValues = new Paragraph(8f, "", voyageGreenFont);
        }
        pValues.setAlignment(Element.ALIGN_LEFT);
        vesselcell.addElement(pValues);
        vesseltable.addCell(vesselcell);
        //2
        vesselcell = new PdfPCell();
        vesselcell.setPaddingLeft(-1f);
        vesselcell.setBorder(0);
        pValues = new Paragraph(8f, "SS Line . . . . . . . . . .", voyageBlueFont);
        pValues.setAlignment(Element.ALIGN_LEFT);
        vesselcell.addElement(pValues);
        vesseltable.addCell(vesselcell);

        vesselcell = new PdfPCell();
        vesselcell.setBorder(0);
        vesselcell.setPadding(0f);
        vesselcell.setPaddingTop(2f);
        pValues = new Paragraph(8f, ":", voyageBlueFont);
        vesselcell.addElement(pValues);
        vesseltable.addCell(vesselcell);
        StringBuilder ssLineScacCode = new StringBuilder();
        if (ssDetail != null && ssDetail.getSpAcctNo() != null && ssDetail.getSpAcctNo().getAccountName() != null) {
            ssLineScacCode.append(ssDetail.getSpAcctNo().getAccountName()).append(" ");
        } else {
            ssLineScacCode.append("        ");
        }
        if (ssDetail != null && ssDetail.getSpAcctNo() != null && CommonUtils.isNotEmpty(ssDetail.getSpAcctNo().getSslineNumber())) {
            String scacValues = new LclSsHeaderDAO().masterSchedulescacCode(ssDetail.getSpAcctNo().getSslineNumber());
            ssLineScacCode.append("(scac:").append(scacValues).append(")");
        }

        vesselcell = new PdfPCell();
        vesselcell.setColspan(3);
        vesselcell.setPaddingLeft(-1f);
        vesselcell.setPaddingTop(2f);
        vesselcell.setBorder(0);
        pValues = new Paragraph(8f, "" + ssLineScacCode.toString(), voyageGreenFont);

        pValues.setAlignment(Element.ALIGN_LEFT);
        vesselcell.addElement(pValues);
        vesseltable.addCell(vesselcell);
        //3
        vesselcell = new PdfPCell();
        vesselcell.setPaddingLeft(-1f);
        vesselcell.setBorder(0);
        pValues = new Paragraph(8f, "Vessel . . . . . . . . . . .", voyageBlueFont);
        pValues.setAlignment(Element.ALIGN_LEFT);
        vesselcell.addElement(pValues);
        vesseltable.addCell(vesselcell);

        vesselcell = new PdfPCell();
        vesselcell.setBorder(0);
        vesselcell.setPadding(0f);
        vesselcell.setPaddingTop(2f);
        pValues = new Paragraph(8f, ":", voyageBlueFont);
        vesselcell.addElement(pValues);
        vesseltable.addCell(vesselcell);

        vesselcell = new PdfPCell();
        vesselcell.setPaddingLeft(-1f);
        vesselcell.setPaddingTop(2f);
        vesselcell.setBorder(0);
        if (null != ssDetail && ssDetail.getSpReferenceName() != null) {
            pValues = new Paragraph(8f, "" + ssDetail.getSpReferenceName(), voyageGreenFont);
        } else {
            pValues = new Paragraph(8f, "", voyageGreenFont);
        }
        pValues.setAlignment(Element.ALIGN_LEFT);
        vesselcell.addElement(pValues);
        vesseltable.addCell(vesselcell);

        vesselcell = new PdfPCell();
        vesselcell.setPaddingLeft(-1f);
        vesselcell.setPaddingTop(2f);
        vesselcell.setBorder(0);
        pValues = new Paragraph(8f, "SS Voy:", voyageBlueFont);
        pValues.setAlignment(Element.ALIGN_LEFT);
        vesselcell.addElement(pValues);
        vesseltable.addCell(vesselcell);

        vesselcell = new PdfPCell();
        vesselcell.setPaddingLeft(-13f);
        vesselcell.setPaddingTop(2f);
        vesselcell.setBorder(0);
        if (null != ssDetail && ssDetail.getSpReferenceNo() != null) {
            pValues = new Paragraph(8f, "" + ssDetail.getSpReferenceNo(), voyageGreenFont);
        } else {
            pValues = new Paragraph(8f, " ", voyageGreenFont);
        }
        pValues.setAlignment(Element.ALIGN_LEFT);
        vesselcell.addElement(pValues);
        vesseltable.addCell(vesselcell);
        //4
        vesselcell = new PdfPCell();
        vesselcell.setPaddingLeft(-1f);
        vesselcell.setBorder(0);
        pValues = new Paragraph(8f, "Sails From . . . . . . .", voyageBlueFont);
        pValues.setAlignment(Element.ALIGN_LEFT);
        vesselcell.addElement(pValues);
        vesseltable.addCell(vesselcell);

        vesselcell = new PdfPCell();
        vesselcell.setBorder(0);
        vesselcell.setPadding(0f);
        vesselcell.setPaddingTop(2f);
        pValues = new Paragraph(8f, ":", voyageBlueFont);
        vesselcell.addElement(pValues);
        vesseltable.addCell(vesselcell);

        vesselcell = new PdfPCell();
        vesselcell.setColspan(3);
        vesselcell.setPaddingLeft(-1f);
        vesselcell.setPaddingTop(2f);
        vesselcell.setBorder(0);
        if (null != ssDetail && ssDetail.getDeparture() != null) {
            pValues = new Paragraph(8f, "" + ssDetail.getDeparture().getUnLocationName(), voyageGreenFont);
        } else {
            pValues = new Paragraph(8f, "", voyageGreenFont);
        }
        pValues.setAlignment(Element.ALIGN_LEFT);
        vesselcell.addElement(pValues);
        vesseltable.addCell(vesselcell);
        //Sailing Date
        vesselcell = new PdfPCell();
        vesselcell.setPaddingLeft(-1.3f);
        vesselcell.setBorder(0);
        pValues = new Paragraph(8f, "Sailing Date. . . . . . .", voyageBlueFont);
        pValues.setAlignment(Element.ALIGN_LEFT);
        vesselcell.addElement(pValues);
        vesseltable.addCell(vesselcell);

        vesselcell = new PdfPCell();
        vesselcell.setBorder(0);
        vesselcell.setPadding(0f);
        vesselcell.setPaddingTop(2f);
        pValues = new Paragraph(8f, ":", voyageBlueFont);
        vesselcell.addElement(pValues);
        vesseltable.addCell(vesselcell);

        vesselcell = new PdfPCell();
        vesselcell.setColspan(3);
        vesselcell.setPaddingLeft(-1f);
        vesselcell.setPaddingTop(2f);
        vesselcell.setBorder(0);
        if (null != ssDetail && ssDetail.getStd() != null) {
            pValues = new Paragraph(8f, "" + DateUtils.formatStringDateToAppFormatMMM(ssDetail.getStd()), voyageGreenFont);
        } else {
            pValues = new Paragraph(8f, "", voyageGreenFont);
        }
        pValues.setAlignment(Element.ALIGN_LEFT);
        vesselcell.addElement(pValues);
        vesseltable.addCell(vesselcell);
        //Eta Dest
        vesselcell = new PdfPCell();
        vesselcell.setPaddingLeft(-1f);
        vesselcell.setBorder(0);
        pValues = new Paragraph(8f, "ETA Dest. . . . . . . . .", voyageBlueFont);
        pValues.setAlignment(Element.ALIGN_LEFT);
        vesselcell.addElement(pValues);
        vesseltable.addCell(vesselcell);

        vesselcell = new PdfPCell();
        vesselcell.setBorder(0);
        vesselcell.setPadding(0f);
        vesselcell.setPaddingTop(2f);
        pValues = new Paragraph(8f, ":", voyageBlueFont);
        vesselcell.addElement(pValues);
        vesseltable.addCell(vesselcell);

        vesselcell = new PdfPCell();
        vesselcell.setPaddingLeft(-1f);
        vesselcell.setBorder(0);
        if (null != ssDetail && ssDetail.getSta() != null) {
            if (lclBooking.getRelayOverride() && lclBooking.getPodfdtt() != null) { // relay over is yes 
                Date addDays = DateUtils.addDays(ssDetail.getSta(), lclBooking.getPodfdtt());
                pValues = new Paragraph(8f, "" + DateUtils.formatStringDateToAppFormatMMM(addDays), voyageGreenFont);
            } else {
                pValues = new Paragraph(8f, "" + DateUtils.formatStringDateToAppFormatMMM(ssDetail.getSta()), voyageGreenFont);
            }
        } else {
            pValues = new Paragraph(8f, "", voyageGreenFont);
        }
        pValues.setAlignment(Element.ALIGN_LEFT);
        vesselcell.addElement(pValues);
        vesseltable.addCell(vesselcell);

        vesselcell = new PdfPCell();
        vesselcell.setPaddingLeft(-1f);
        vesselcell.setPaddingRight(-60f);
        vesselcell.setBorder(0);
        pValues = new Paragraph(8f, "Total Trans . . : ", voyageBlueFont);
        pValues.setAlignment(Element.ALIGN_LEFT);
        vesselcell.addElement(pValues);
        vesseltable.addCell(vesselcell);

        vesselcell = new PdfPCell();
        vesselcell.setBorder(0);
        pValues = new Paragraph(8f, "       " + (ttPooFd != 0 ? ttPooFd : ""), voyageGreenFont);
        vesselcell.addElement(pValues);
        vesseltable.addCell(vesselcell);

        //docs
        vesselcell = new PdfPCell();
        vesselcell.setColspan(3);
        vesselcell.setPaddingLeft(-1.2f);
        vesselcell.setBorder(0);
        pValues = new Paragraph(6f, "Docs & Cargo Cutoff/LRD. .. :", voyageBlueFont);
        pValues.setAlignment(Element.ALIGN_LEFT);
        vesselcell.addElement(pValues);
        vesseltable.addCell(vesselcell);

        vesselcell = new PdfPCell();
        vesselcell.setColspan(2);
        vesselcell.setPaddingLeft(-95.5f);
        vesselcell.setBorder(0);
        if (null != ssDetail && originLrd != null) {
            pValues = new Paragraph(8f, "" + DateUtils.formatDateToDateTimeString(originLrd), voyageGreenFont);

        } else if (lclBooking.getPooWhseLrdt() != null) {
            pValues = new Paragraph(6f, "" + DateUtils.formatDateToDateTimeString(lclBooking.getPooWhseLrdt()), voyageGreenFont);
        } else {
            pValues = new Paragraph(6f, "", voyageGreenFont);
        }
        pValues.setAlignment(Element.ALIGN_LEFT);
        vesselcell.addElement(pValues);
        vesseltable.addCell(vesselcell);
        //docs
        vesselcell = new PdfPCell();
        vesselcell.setBorder(0);
        vesselcell.setColspan(3);
        vesselcell.setPaddingLeft(-1f);
        vesselcell.setPaddingBottom(3f);
        vesselcell.setBorderWidthBottom(1f);
        pValues = new Paragraph(6f, "Haz(docs&cargo)Cutoff/LRD:", voyageBlueFont);
        pValues.setAlignment(Element.ALIGN_LEFT);
        vesselcell.addElement(pValues);
        vesseltable.addCell(vesselcell);

        vesselcell = new PdfPCell();
        vesselcell.setBorder(0);
        vesselcell.setColspan(2);
        vesselcell.setPaddingLeft(-95f);
        vesselcell.setBorderWidthBottom(1f);

        GregorianCalendar gc = new GregorianCalendar();
        if (null != ssDetail && ssDetail.getGeneralLrdt() != null && null != originLrd) { // picked voyage 
            gc.setTime(originLrd);
        } else if (lclBooking.getPooWhseLrdt() != null) {           //booked voyage 
            Date originLrdDate = lclBooking.getPooWhseLrdt();
            gc.setTime(originLrdDate);
        }
        if (null != ssDetail && ssDetail.getGeneralLrdt() != null || lclBooking.getPooWhseLrdt() != null) {
            gc.add(GregorianCalendar.DATE, -1);
            int day = gc.get(GregorianCalendar.DAY_OF_WEEK);
            if (day == 1) { // here day 1 is sunday 
                gc.add(GregorianCalendar.DATE, -2);
            } else if (day == 7) { //day 7 is saturday 
                gc.add(GregorianCalendar.DATE, -1);
            }
        }
        Date isResult = gc.getTime();

        if (null != ssDetail && ssDetail.getGeneralLrdt() != null) {
            pValues = new Paragraph(8f, "" + DateUtils.formatDateToDateTimeString(isResult), voyageGreenFont);
        } else if (lclBooking.getPooWhseLrdt() != null) {
            pValues = new Paragraph(6f, "" + DateUtils.formatDateToDateTimeString(isResult), voyageGreenFont);
        } else {
            pValues = new Paragraph(6f, "", voyageGreenFont);
        }
        pValues.setAlignment(Element.ALIGN_LEFT);
        vesselcell.addElement(pValues);
        vesseltable.addCell(vesselcell);

        vesselcell = new PdfPCell();
        vesselcell.setPaddingLeft(-1f);
        vesselcell.setBorder(0);
        vesselcell.setBorderWidthBottom(0.06f);
        pValues = new Paragraph(6f, "Warehouse D/R:", commoditydescBoldFont);
        pValues.setAlignment(Element.ALIGN_LEFT);
        vesselcell.addElement(pValues);
        vesseltable.addCell(vesselcell);

        vesselcell = new PdfPCell();
        vesselcell.setColspan(5);
        vesselcell.setBorder(0);
        vesselcell.setPadding(0f);
        vesselcell.setPaddingLeft(-1f);
        vesselcell.setPaddingBottom(3f);
        vesselcell.setBorderWidthBottom(0.06f);
        pValues = new Paragraph(8f, "" + wareHouseNumber.toUpperCase(), fontCompNormalSub);
        pValues.setAlignment(Element.ALIGN_LEFT);
        vesselcell.addElement(pValues);
        vesseltable.addCell(vesselcell);

        //OSD Remarks
        vesselcell = new PdfPCell();
        vesselcell.setPaddingLeft(-1f);
        vesselcell.setBorder(0);
        vesselcell.setBorderWidthBottom(0.06f);
        pValues = new Paragraph(6f, "OSD:", commoditydescBoldFont);
        pValues.setAlignment(Element.ALIGN_LEFT);
        vesselcell.addElement(pValues);
        vesseltable.addCell(vesselcell);
        vesselcell = new PdfPCell();
        vesselcell.setColspan(4);
        vesselcell.setBorder(0);
        vesselcell.setPadding(0f);
        vesselcell.setPaddingLeft(-55f);
        vesselcell.setPaddingBottom(3f);
        vesselcell.setBorderWidthBottom(0.06f);
        pValues = new Paragraph(8f, "" + osdRemarks.toUpperCase(), fontCompNormalSub);
        pValues.setAlignment(Element.ALIGN_LEFT);
        vesselcell.addElement(pValues);
        vesseltable.addCell(vesselcell);
        //Remarks
        vesselcell = new PdfPCell();
        vesselcell.setPaddingLeft(-1f);
        vesselcell.setBorder(0);
        vesselcell.setBorderWidthBottom(0.06f);
        pValues = new Paragraph(7f, "Remarks:", commoditydescBoldFont);
        pValues.setAlignment(Element.ALIGN_LEFT);
        vesselcell.addElement(pValues);
        vesseltable.addCell(vesselcell);
        vesselcell = new PdfPCell();
        vesselcell.setColspan(4);
        vesselcell.setPaddingLeft(-35f);
        vesselcell.setBorder(0);
        vesselcell.setBorderWidthBottom(0.06f);
        pValues = new Paragraph(7f, "" + loadingRemarks.toUpperCase(), fontCompNormalSub);
        pValues.setAlignment(Element.ALIGN_LEFT);
        vesselcell.addElement(pValues);
        vesseltable.addCell(vesselcell);
        //hotcodes
        vesselcell = new PdfPCell();
        vesselcell.setPaddingLeft(-1f);
        vesselcell.setBorder(0);
        vesselcell.setColspan(5);
        pValues = new Paragraph(8f, "Hot Codes:", commoditydescBoldFont);
        pValues.setAlignment(Element.ALIGN_LEFT);
        vesselcell.addElement(pValues);
        vesseltable.addCell(vesselcell);
        StringBuilder hotCodes = new StringBuilder();
        List<LclBookingHotCode> lclBookingHotCodeList = new LclBookingHotCodeDAO().findByProperty("lclFileNumber.id", lclBooking.getFileNumberId());
        for (LclBookingHotCode lclBookingHotCode : lclBookingHotCodeList) {
            if (lclBookingHotCode != null && lclBookingHotCode.getCode() != null) {
                hotCodes.append(lclBookingHotCode.getCode()).append("\n");
            }
        }
        //hot values
        vesselcell = new PdfPCell();
        vesselcell.setColspan(5);
        vesselcell.setPadding(0f);
        vesselcell.setPaddingLeft(-1f);
        vesselcell.setBorder(0);
        pValues = new Paragraph(8f, "" + StringUtils.substringBeforeLast(hotCodes.toString(), ","), fontCompNormalSub);
        pValues.setAlignment(Element.ALIGN_LEFT);
        vesselcell.addElement(pValues);
        vesseltable.addCell(vesselcell);
        //empty
        vesselcell = new PdfPCell();
        vesselcell.setColspan(5);
        vesselcell.setPaddingLeft(-1f);
        cell.setPadding(3f);
        vesselcell.setBorder(0);
        vesseltable.addCell(vesselcell);

        //
        if (!"".equalsIgnoreCase(regionalGriRemarks)) {
            vesselcell = new PdfPCell();
            vesselcell.setBorder(0);
            vesselcell.setColspan(5);
            PdfPTable ntable = new PdfPTable(1);
            ntable.setWidthPercentage(100f);
            PdfPCell cell1 = new PdfPCell();
            cell1.setBorder(0);
            cell1.setBorderColor(new BaseColor(128, 00, 00));
            cell1.setBorderWidthBottom(1f);
            cell1.setBorderWidthLeft(1f);
            cell1.setBorderWidthRight(1f);
            cell1.setBorderWidthTop(1f);
            PdfPTable ntable1 = new PdfPTable(1);
            ntable1.setWidthPercentage(99.8f);
            PdfPCell cell2 = new PdfPCell();
            cell2.setBorder(0);
            cell2.setBorderColor(new BaseColor(128, 00, 00));
            cell2.setBorderWidthBottom(1f);
            cell2.setBorderWidthLeft(1f);
            cell2.setBorderWidthRight(1f);
            cell2.setBorderWidthTop(1f);
            cell2.setPaddingBottom(2f);
            cell2.setBackgroundColor(new BaseColor(204, 204, 204));
            pValues = new Paragraph(6f, "" + regionalGriRemarks, blackBoldFontSize7);
            pValues.setAlignment(Element.ALIGN_LEFT);
            cell2.addElement(pValues);
            ntable1.addCell(cell2);
            cell1.addElement(ntable1);
            ntable.addCell(cell1);
            vesselcell.addElement(ntable);

            vesseltable.addCell(vesselcell);
        }
        cell.addElement(vesseltable);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthTop(1f);
        cell.setBorderWidthBottom(1f);
        PdfPTable voyageTable = new PdfPTable(4);
        voyageTable.setWidthPercentage(101.3f);
        voyageTable.setWidths(new float[]{2.5f, 0.1f, 3.6f, 1f});
        PdfPCell voyagecell = null;

        if ("Booking Confirmation With Rate".equalsIgnoreCase(documentName)) {
            List<LclBookingAc> manualchargeList = lclCostChargesDAO.getLclCostByFileNumberME(lclBooking.getFileNumberId(), true);
            voyagecell = new PdfPCell();
            voyagecell.setBorder(0);
            voyagecell.setColspan(4);
            voyagecell.setBorderWidthBottom(0.06f);
            pValues = new Paragraph(6f, "Additional Secured Charges", headingBoldFont);
            pValues.setAlignment(Element.ALIGN_CENTER);
            voyagecell.addElement(pValues);
            voyageTable.addCell(voyagecell);

            voyagecell = new PdfPCell();
            voyagecell.setBorder(0);
            voyagecell.setPadding(0f);
            voyagecell.setColspan(4);
            voyageTable.addCell(voyagecell);
            if (manualchargeList != null && manualchargeList.size() > 0) {
                for (Object lclBookingManualList : manualchargeList) {
                    LclBookingAc lclBookingAc = (LclBookingAc) lclBookingManualList;

                    voyagecell = new PdfPCell();
                    voyagecell.setBorder(0);
                    voyagecell.setColspan(2);
                    pValues = new Paragraph(8f, "" + (lclBookingAc.getArglMapping().getBlueScreenChargeCode().replaceFirst("^0+(?!$)", "") + "") + "  " + lclBookingAc.getArglMapping().getChargeCode(), fontTimeBlue);
                    pValues.setAlignment(Element.ALIGN_RIGHT);
                    voyagecell.addElement(pValues);
                    voyageTable.addCell(voyagecell);
                    voyagecell = new PdfPCell();
                    voyagecell.setBorder(0);
                    pValues = new Paragraph(8f, "" + NumberUtils.convertToThreeDecimalhash(lclBookingAc.getArAmount().doubleValue()) + "  USD", fontTimeBlue);
                    pValues.setAlignment(Element.ALIGN_RIGHT);
                    voyagecell.addElement(pValues);
                    voyageTable.addCell(voyagecell);

                    voyagecell = new PdfPCell();
                    voyagecell.setBorder(0);
                    voyageTable.addCell(voyagecell);
                }
            } else {
                voyagecell = new PdfPCell();
                voyagecell.setBorder(0);
                voyagecell.setColspan(4);
                voyagecell.setPadding(10f);
                voyageTable.addCell(voyagecell);
            }

            voyagecell = new PdfPCell();
            voyagecell.setBorder(0);
            voyagecell.setColspan(4);
            voyagecell.setBorderWidthBottom(1f);
            voyageTable.addCell(voyagecell);
        }

        voyagecell = new PdfPCell();
        voyagecell.setBorder(0);
        voyagecell.setBorderWidthBottom(0.06f);
        voyagecell.setPaddingTop(3f);
        voyagecell.setPaddingBottom(2f);
        pValues = new Paragraph(6f, "Term To Do B/L . . . . . . .", voyageSubFont);
        voyagecell.addElement(pValues);
        voyageTable.addCell(voyagecell);

        voyagecell = new PdfPCell();
        voyagecell.setBorder(0);
        voyagecell.setBorderWidthBottom(0.06f);
        voyagecell.setPadding(0);
        voyagecell.setPaddingBottom(3f);
        voyagecell.setPaddingTop(3f);
        pValues = new Paragraph(6f, ":", voyageSubFont);
        voyagecell.addElement(pValues);
        voyageTable.addCell(voyagecell);

        voyagecell = new PdfPCell();
        voyagecell.setBorder(0);
        voyagecell.setColspan(2);
        voyagecell.setBorderWidthBottom(0.06f);
        voyagecell.setPaddingBottom(3f);
        voyagecell.setPaddingTop(3f);
        // voyagecell.setPaddingLeft(-5f);
        pValues = new Paragraph(6f, "" + terminalNo + " " + companyName, voyageboxFont);
        pValues.setAlignment(Element.ALIGN_LEFT);
        voyagecell.addElement(pValues);
        voyageTable.addCell(voyagecell);
        //2
        voyagecell = new PdfPCell();
        voyagecell.setBorder(0);
        voyagecell.setBorderWidthBottom(0.06f);
        voyagecell.setPaddingBottom(3f);
        voyagecell.setPaddingTop(3f);
        pValues = new Paragraph(6f, "Approx. Due Date. . . . . .", voyageSubFont);
        pValues.setAlignment(Element.ALIGN_LEFT);
        voyagecell.addElement(pValues);
        voyageTable.addCell(voyagecell);

        voyagecell = new PdfPCell();
        voyagecell.setBorder(0);
        voyagecell.setBorderWidthBottom(0.06f);
        voyagecell.setPadding(0);
        voyagecell.setPaddingBottom(3f);
        voyagecell.setPaddingTop(3f);
        pValues = new Paragraph(6f, ":", voyageSubFont);
        voyagecell.addElement(pValues);
        voyageTable.addCell(voyagecell);

        voyagecell = new PdfPCell();
        voyagecell.setBorder(0);
        voyagecell.setColspan(2);
        voyagecell.setBorderWidthBottom(0.06f);
        voyagecell.setPaddingTop(3f);
        voyagecell.setPaddingBottom(4f);
        if (null != bookedSsDetails && bookedSsDetails.getStd() != null) {
            pValues = new Paragraph(6f, "" + DateUtils.formatStringDateToAppFormatMMM(bookedSsDetails.getStd()), voyageboxFont);
        } else {
            pValues = new Paragraph(6f, "", voyageGreenFont);
        }
        pValues.setAlignment(Element.ALIGN_LEFT);
        voyagecell.addElement(pValues);
        voyageTable.addCell(voyagecell);
        //33
        voyagecell = new PdfPCell();
        voyagecell.setBorder(0);
        voyagecell.setPadding(0f);
        voyagecell.setPaddingBottom(1f);
        voyagecell.setColspan(4);
        voyageTable.addCell(voyagecell);

        voyagecell = new PdfPCell();
        voyagecell.setBorder(0);
        voyagecell.setPaddingBottom(3f);
        voyagecell.setPaddingTop(3f);
        voyagecell.setBackgroundColor(BaseColor.GREEN);
        pValues = new Paragraph(6f, "BOOKING CONTACT. . . ", voyageSubFont);
        pValues.setAlignment(Element.ALIGN_LEFT);
        voyagecell.addElement(pValues);
        voyageTable.addCell(voyagecell);

        voyagecell = new PdfPCell();
        voyagecell.setBorder(0);
        voyagecell.setPadding(0);
        voyagecell.setPaddingBottom(3f);
        voyagecell.setPaddingTop(3f);
        voyagecell.setBackgroundColor(BaseColor.GREEN);
        pValues = new Paragraph(6f, ":", voyageSubFont);
        voyagecell.addElement(pValues);
        voyageTable.addCell(voyagecell);
        StringBuilder bookingContactDetails = new StringBuilder();
        if (CommonFunctions.isNotNull(lclBooking) && CommonFunctions.isNotNull(lclBooking.getClientContact().getId()) && CommonFunctions.isNotNull(lclBooking.getClientContact().getContactName())) {
            bookingContactDetails.append(lclBooking.getClientContact().getContactName());
        } else if (CommonFunctions.isNotNull(lclBooking) && CommonFunctions.isNotNull(lclBooking.getShipContact().getId()) && CommonFunctions.isNotNull(lclBooking.getShipContact().getContactName())) {
            bookingContactDetails.append(lclBooking.getShipContact().getContactName());
        } else if (CommonFunctions.isNotNull(lclBooking) && CommonFunctions.isNotNull(lclBooking.getFwdContact().getId()) && CommonFunctions.isNotNull(lclBooking.getFwdContact().getContactName())) {
            bookingContactDetails.append(lclBooking.getFwdContact().getContactName());
        } else if (CommonFunctions.isNotNull(lclBooking) && CommonFunctions.isNotNull(lclBooking.getNotyContact().getId()) && CommonFunctions.isNotNull(lclBooking.getNotyContact().getContactName())) {
            bookingContactDetails.append(lclBooking.getNotyContact().getContactName());
        }
        StringBuilder bookingEmailDetails = new StringBuilder();
        if (CommonFunctions.isNotNull(lclBooking) && CommonFunctions.isNotNull(lclBooking.getClientContact().getId()) && CommonFunctions.isNotNull(lclBooking.getClientContact().getEmail1())) {
            bookingEmailDetails.append(lclBooking.getClientContact().getEmail1());
        } else if (CommonFunctions.isNotNull(lclBooking) && CommonFunctions.isNotNull(lclBooking.getShipContact().getId()) && CommonFunctions.isNotNull(lclBooking.getShipContact().getEmail1())) {
            bookingEmailDetails.append(lclBooking.getShipContact().getEmail1());
        } else if (CommonFunctions.isNotNull(lclBooking) && CommonFunctions.isNotNull(lclBooking.getFwdContact().getId()) && CommonFunctions.isNotNull(lclBooking.getFwdContact().getEmail1())) {
            bookingEmailDetails.append(lclBooking.getFwdContact().getEmail1());
        } else if (CommonFunctions.isNotNull(lclBooking) && CommonFunctions.isNotNull(lclBooking.getNotyContact().getId()) && CommonFunctions.isNotNull(lclBooking.getNotyContact().getEmail1())) {
            bookingEmailDetails.append(lclBooking.getNotyContact().getEmail1());
        }
        voyagecell = new PdfPCell();
        voyagecell.setBorder(0);
        voyagecell.setColspan(2);
        voyagecell.setPaddingBottom(3f);
        voyagecell.setPaddingTop(3f);
        voyagecell.setBackgroundColor(BaseColor.GREEN);
        if (!"".equals(bookingContactDetails.toString())) {
            pValues = new Paragraph(6f, "" + bookingContactDetails.toString().toUpperCase(), voyageboxFont);
        } else {
            pValues = new Paragraph(6f, "", voyageboxFont);
        }
        pValues.setAlignment(Element.ALIGN_LEFT);
        voyagecell.addElement(pValues);
        voyageTable.addCell(voyagecell);

        voyagecell = new PdfPCell();
        voyagecell.setBorder(0);
        voyagecell.setPadding(0f);
        voyagecell.setPaddingBottom(1f);
        voyagecell.setBorderWidthBottom(0.06f);
        voyagecell.setColspan(4);
        voyageTable.addCell(voyagecell);
        //4
        voyagecell = new PdfPCell();
        voyagecell.setBorder(0);
        voyagecell.setPadding(0f);
        voyagecell.setBackgroundColor(BaseColor.WHITE);
        voyagecell.setPaddingBottom(1f);
        voyagecell.setColspan(4);
        voyageTable.addCell(voyagecell);

        voyagecell = new PdfPCell();
        voyagecell.setBorder(0);
        voyagecell.setPaddingBottom(3f);
        voyagecell.setPaddingTop(3f);
        voyagecell.setBackgroundColor(BaseColor.GREEN);
        pValues = new Paragraph(6f, "BOOKING EMAIL. . . . . .", voyageSubFont);
        pValues.setAlignment(Element.ALIGN_LEFT);
        voyagecell.addElement(pValues);
        voyageTable.addCell(voyagecell);

        voyagecell = new PdfPCell();
        voyagecell.setBorder(0);
        voyagecell.setPadding(0);
        voyagecell.setPaddingBottom(3f);
        voyagecell.setPaddingTop(3f);
        voyagecell.setBackgroundColor(BaseColor.GREEN);
        pValues = new Paragraph(6f, ":", voyageSubFont);
        voyagecell.addElement(pValues);
        voyageTable.addCell(voyagecell);

        voyagecell = new PdfPCell();
        voyagecell.setBorder(0);
        voyagecell.setColspan(2);
        voyagecell.setPaddingBottom(3f);
        voyagecell.setPaddingTop(3f);
        voyagecell.setBackgroundColor(BaseColor.GREEN);
        if (!"".equals(bookingEmailDetails.toString())) {
            pValues = new Paragraph(6f, "" + bookingEmailDetails.toString(), voyageboxFont);
        } else {
            pValues = new Paragraph(8f, "", voyageGreenFont);
        }
        pValues.setAlignment(Element.ALIGN_LEFT);
        voyagecell.addElement(pValues);
        voyageTable.addCell(voyagecell);

        voyagecell = new PdfPCell();
        voyagecell.setBorder(0);
        voyagecell.setPadding(0f);
        voyagecell.setBackgroundColor(BaseColor.WHITE);
        voyagecell.setPaddingBottom(1f);
        voyagecell.setBorderWidthBottom(0.06f);
        voyagecell.setColspan(4);
        voyageTable.addCell(voyagecell);
        //5
        voyagecell = new PdfPCell();
        voyagecell.setBorder(0);
        //voyagecell.setColspan(2);
        voyagecell.setBorderWidthBottom(0.06f);
        voyagecell.setPaddingBottom(3f);
        voyagecell.setPaddingTop(3f);
        pValues = new Paragraph(6f, "Due Forwarder. . . . . . . .", voyageSubFont);
        pValues.setAlignment(Element.ALIGN_LEFT);
        voyagecell.addElement(pValues);
        voyageTable.addCell(voyagecell);

        voyagecell = new PdfPCell();
        voyagecell.setBorder(0);
        voyagecell.setBorderWidthBottom(0.06f);
        voyagecell.setPadding(0);
        voyagecell.setPaddingBottom(3f);
        voyagecell.setPaddingTop(3f);
        pValues = new Paragraph(6f, ":", voyageSubFont);
        voyagecell.addElement(pValues);
        voyageTable.addCell(voyagecell);

        voyagecell = new PdfPCell();
        voyagecell.setBorder(0);
        voyagecell.setColspan(2);
        voyagecell.setBorderWidthBottom(0.06f);
        voyageTable.addCell(voyagecell);

        //6
        voyagecell = new PdfPCell();
        voyagecell.setBorder(0);
        // voyagecell.setColspan(2);
        voyagecell.setBorderWidthBottom(0.06f);
        voyagecell.setPaddingBottom(3f);
        voyagecell.setPaddingTop(3f);
        pValues = new Paragraph(6f, "Insurance . . . . . . . . . . . .", voyageSubFont);
        pValues.setAlignment(Element.ALIGN_LEFT);
        voyagecell.addElement(pValues);
        voyageTable.addCell(voyagecell);

        voyagecell = new PdfPCell();
        voyagecell.setBorder(0);
        voyagecell.setBorderWidthBottom(0.06f);
        voyagecell.setPadding(0);
        voyagecell.setPaddingBottom(3f);
        voyagecell.setPaddingTop(3f);
        pValues = new Paragraph(6f, ":", voyageSubFont);
        voyagecell.addElement(pValues);
        voyageTable.addCell(voyagecell);

        voyagecell = new PdfPCell();
        voyagecell.setBorder(0);
        voyagecell.setColspan(2);
        voyagecell.setBorderWidthBottom(0.06f);
        voyageTable.addCell(voyagecell);

        //7
        voyagecell = new PdfPCell();
        voyagecell.setBorder(0);
        // voyagecell.setColspan(2);
        voyagecell.setBorderWidthBottom(0.06f);
        voyagecell.setPaddingBottom(3f);
        voyagecell.setPaddingTop(3f);
        pValues = new Paragraph(6f, "Total Charges Prepaid. .", voyageSubFont);
        pValues.setAlignment(Element.ALIGN_LEFT);
        voyagecell.addElement(pValues);
        voyageTable.addCell(voyagecell);

        voyagecell = new PdfPCell();
        voyagecell.setBorder(0);
        voyagecell.setBorderWidthBottom(0.06f);
        voyagecell.setPadding(0);
        voyagecell.setPaddingBottom(3f);
        voyagecell.setPaddingTop(3f);
        pValues = new Paragraph(6f, ":", voyageSubFont);
        voyagecell.addElement(pValues);
        voyageTable.addCell(voyagecell);

        voyagecell = new PdfPCell();
        voyagecell.setBorder(0);
        voyagecell.setColspan(2);
        voyagecell.setBorderWidthBottom(0.06f);
        voyageTable.addCell(voyagecell);

        //8
        voyagecell = new PdfPCell();
        voyagecell.setBorder(0);
        // voyagecell.setColspan(2);
        voyagecell.setBorderWidthBottom(0.06f);
        voyagecell.setPaddingBottom(3f);
        voyagecell.setPaddingTop(3f);
        pValues = new Paragraph(6f, "Total Charges Collect . .", voyageSubFont);
        pValues.setAlignment(Element.ALIGN_LEFT);
        voyagecell.addElement(pValues);
        voyageTable.addCell(voyagecell);

        voyagecell = new PdfPCell();
        voyagecell.setBorder(0);
        voyagecell.setBorderWidthBottom(0.06f);
        voyagecell.setPadding(0);
        voyagecell.setPaddingBottom(3f);
        voyagecell.setPaddingTop(3f);
        pValues = new Paragraph(6f, ":", voyageSubFont);
        voyagecell.addElement(pValues);
        voyageTable.addCell(voyagecell);

        voyagecell = new PdfPCell();
        voyagecell.setBorder(0);
        voyagecell.setColspan(2);
        voyagecell.setBorderWidthBottom(0.06f);
        voyageTable.addCell(voyagecell);

        //9
        voyagecell = new PdfPCell();
        voyagecell.setBorder(0);
        voyagecell.setBorderWidthBottom(0.06f);
        voyagecell.setPaddingTop(1f);
        voyagecell.setPaddingBottom(15f);
        pValues = new Paragraph(7f, "Comments . . . . . . . . . . .", voyageSubFont);
        pValues.setAlignment(Element.ALIGN_LEFT);
        voyagecell.addElement(pValues);
        voyageTable.addCell(voyagecell);

        voyagecell = new PdfPCell();
        voyagecell.setBorder(0);
        voyagecell.setBorderWidthBottom(0.06f);
        voyagecell.setPadding(0);
        voyagecell.setPaddingBottom(3f);
        voyagecell.setPaddingTop(3f);
        pValues = new Paragraph(6f, ":", voyageSubFont);
        voyagecell.addElement(pValues);

        voyageTable.addCell(voyagecell);

        voyagecell = new PdfPCell();
        voyagecell.setBorder(0);
        voyagecell.setColspan(2);
        voyagecell.setBorderWidthBottom(0.06f);
        pValues = new Paragraph(7f, "" + billingMethod, voyageSubFont);
        pValues.setAlignment(Element.ALIGN_CENTER);
        voyagecell.addElement(pValues);
        pValues = new Paragraph(7f, "" + externalRemarks.toUpperCase(), voyageboxFont);
        pValues.setAlignment(Element.ALIGN_LEFT);
        voyagecell.addElement(pValues);
        voyageTable.addCell(voyagecell);

        voyagecell = new PdfPCell();
        voyagecell.setBorder(0);
        voyagecell.setColspan(4);

        PdfPTable ntable2 = new PdfPTable(1);
        ntable2.setWidthPercentage(100f);
        PdfPCell cell13 = new PdfPCell();
        cell13.setBorder(0);
        cell13.setBorderColor(new BaseColor(128, 00, 00));
        cell13.setBorderWidthBottom(1f);
        cell13.setBorderWidthLeft(1f);
        cell13.setBorderWidthRight(1f);
        cell13.setBorderWidthTop(1f);
        PdfPTable ntable3 = new PdfPTable(1);
        ntable3.setWidthPercentage(99.8f);
        PdfPCell cell4 = new PdfPCell();
        cell4.setBorder(0);
        cell4.setBorderColor(new BaseColor(128, 00, 00));
        cell4.setBorderWidthBottom(1f);
        cell4.setBorderWidthLeft(1f);
        cell4.setBorderWidthRight(1f);
        cell4.setBorderWidthTop(1f);
        cell4.setBackgroundColor(new BaseColor(204, 204, 204));
        String comment5 = DR004 != null ? DR004 : "";
        pValues = new Paragraph(8f, "" + comment5, voyageSubFont);
        pValues.setAlignment(Element.ALIGN_CENTER);
        cell4.addElement(pValues);
        ntable3.addCell(cell4);
        cell13.addElement(ntable3);
        ntable2.addCell(cell13);
        voyagecell.addElement(ntable2);
        voyageTable.addCell(voyagecell);

        cell.addElement(voyageTable);
        table.addCell(cell);

        return table;
    }

    public PdfPTable emptyLargeTable() {
        PdfPTable table = null;
        PdfPCell cell = null;
        Phrase p = null;
        Font redFont = new Font(FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.RED);
        table = new PdfPTable(1);
        table.setWidthPercentage(100);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setFixedHeight(15f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        if (lclBooking.getRateType().equalsIgnoreCase("C")) {
            p = new Phrase("*** These rates are non-commissionable. ***", redFont);
            p.setLeading(7f);
            cell.addElement(p);
        }
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setFixedHeight(30f);
        table.addCell(cell);
        return table;
    }

    public PdfPTable contentFinalTable() throws DocumentException {
        String comment5 = DR005 != null ? DR005 : "";
        Font contentBoldFont = FontFactory.getFont("Arial", 10f, Font.BOLD);
        Phrase p = null;
        table = new PdfPTable(1);
        table.setWidthPercentage(100);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBackgroundColor(new BaseColor(255, 204, 0));
        p = new Phrase("                                 ***** All Cargo Tendered for Transportation is Subject to Inspection *****", contentBoldFont);
        p.setLeading(7f);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBackgroundColor(new BaseColor(255, 204, 0));
        p = new Phrase("" + comment5, contentBoldFont);
        p.setLeading(7f);
        cell.addElement(p);
        table.addCell(cell);

        return table;
    }

    public PdfPTable contentFinalTable1(HttpServletRequest request) throws DocumentException, Exception {
        String loginName = "";
        if (null != request) {
            HttpSession session = request.getSession();
            if (session.getAttribute("loginuser") != null) {
                User user = (User) session.getAttribute("loginuser");
                loginName = user.getLoginName();
            }
        } else {
            loginName = "System";
        }
        Iterator quoteCommentsIterator = new GenericCodeDAO().getLclPrintComments(39, "LA");
        while (quoteCommentsIterator.hasNext()) {
            Object[] row = (Object[]) quoteCommentsIterator.next();
            String code = (String) row[0];
            String codeDesc = (String) row[1];
            if (code != null) {
                if ("LA001".equalsIgnoreCase(code)) {
                    LA001 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                } else if ("LA002".equalsIgnoreCase(code)) {
                    LA002 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                }
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm a zzz ");
        Phrase p = null;
        table = new PdfPTable(2);
        table.setSpacingBefore(3f);
        table.setWidths(new float[]{0.9f, 3.6f});
        table.setWidthPercentage(100);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setRowspan(2);
        cell.setBackgroundColor(BaseColor.YELLOW);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        p = new Phrase("**ADVISORY**", contenthBoldFont);
        p.setLeading(22f);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBackgroundColor(BaseColor.YELLOW);
        cell.setBorderWidthTop(0.06f);
        String comment6 = LA001 != null ? LA001 : "";
        p = new Phrase("" + comment6, contentNormalFont);
        String companyWebsite = LoadLogisoftProperties.getProperty(ruleName.equalsIgnoreCase("ECU") ? "application.ECU.website"
                : ruleName.equalsIgnoreCase("OTI") ? "application.OTI.website" : "application.Econo.website");
        p.add(new Chunk("" + companyWebsite, blueNormalFont8));
        p.setLeading(7f);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBackgroundColor(BaseColor.YELLOW);
        cell.setBorderWidthBottom(0.06f);
        String comment7 = LA002 != null ? LA002 : "";
        p = new Phrase("" + comment7, contentNormalFont);

        p.setLeading(7f);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(2);
        cell.setPaddingTop(0f);
        cell.setBorder(0);
        p = new Phrase(7f, "Printed By:  " + loginName.toUpperCase() + " " + sdf.format(new Date()), contentBLNormalFont);
        cell.addElement(p);
        table.addCell(cell);
        return table;
    }

    public PdfPTable addingPrintingLabelWithQrCode(String realPath) throws DocumentException, Exception {
        Paragraph p = null;
        String pooUnLocCode = "";
        if ("T".equalsIgnoreCase(lclBooking.getBookingType())) {
            pooUnLocCode = null != lclBooking.getPortOfLoading() ? lclBooking.getPortOfLoading().getUnLocationCode() : "";
        } else {
            pooUnLocCode = null != lclBooking.getPortOfOrigin() ? lclBooking.getPortOfOrigin().getUnLocationCode() : "";
        }
        BaseFont arialBold = BaseFont.createFont(realPath + "/ttf/arial.ttf", BaseFont.IDENTITY_H, false);
        Font medium = new Font(arialBold, 35f, Font.BOLD);
        Font large = new Font(arialBold, 60f, Font.BOLD);
        Font whiteFont = new Font(arialBold, 40f, Font.BOLD, BaseColor.WHITE);
        labelCount++;
        table = new PdfPTable(4);
        table.setWidths(new float[]{0.4f, 8f, 3.6f, 1f});
        table.setWidthPercentage(100);
        String company = LoadLogisoftProperties.getProperty(ruleName.equalsIgnoreCase("ECU") ? "application.ECU.companyname"
                : ruleName.equalsIgnoreCase("OTI") ? "application.OTI.companyname" : "application.Econo.companyname");
        String companyWebsite = LoadLogisoftProperties.getProperty(ruleName.equalsIgnoreCase("ECU") ? "application.ECU.website"
                : ruleName.equalsIgnoreCase("OTI") ? "application.OTI.website" : "application.Econo.website");
        cell = new PdfPCell();
        cell.setBorder(0);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(40f);
        cell.setBorderWidthBottom(2f);
        cell.setPaddingBottom(4f);
        p = new Paragraph(13f, "" + company, arialBoldFont14Size);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(40f);
        cell.setBorderWidthBottom(2f);
        cell.setPaddingBottom(4f);
        p = new Paragraph(13f, "http://" + companyWebsite, arialFontSize10Normal);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(4);
        cell.setFixedHeight(10f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);

        String topLineCity = "", belowLineCity = "";

        if (null != lclBooking.getPortOfDestination() && lclBooking.getPortOfDestination().equals(lclBooking.getFinalDestination())) {
            topLineCity = lclBooking.getPortOfDestination().getUnLocationName().toUpperCase();
            if (null != lclBooking.getPortOfDestination().getCountryId()) {
                belowLineCity = lclBooking.getPortOfDestination().getCountryId().getCodedesc().toUpperCase();
            } else {
                belowLineCity = "";
            }
        } else {
            topLineCity = lclBooking.getFinalDestination().getUnLocationName().toUpperCase();
            if (null != lclBooking.getPortOfDestination()) {
                belowLineCity = "Via:" + lclBooking.getPortOfDestination().getUnLocationName().toUpperCase();
            }
        }

        PdfPTable table1 = new PdfPTable(1);
        table1.setWidthPercentage(100);
        PdfPCell cell1 = new PdfPCell();
        cell1.setBorder(0);
        cell1.setPaddingTop(30f);
        cell1.setPaddingBottom(10f);
        p = new Paragraph(30f, "" + topLineCity, medium);
        cell1.addElement(p);
        table1.addCell(cell1);

        cell1 = new PdfPCell();
        cell1.setBorder(0);
        cell1.setPaddingTop(10f);
        table1.addCell(cell1);

        cell1 = new PdfPCell();
        cell1.setBorder(0);
        p = new Paragraph(30f, "" + belowLineCity, medium);
        p.setSpacingAfter(100f);
        cell1.addElement(p);
        table1.addCell(cell1);

        cell.addElement(table1);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);

        table1 = new PdfPTable(1);
        table1.setWidthPercentage(100);
        cell1 = new PdfPCell();
        cell1.setBorder(0);
        cell1.setPaddingTop(20f);
        cell1.setBackgroundColor(BaseColor.BLACK);
        p = new Paragraph(10f, "OCEAN", whiteFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell1.addElement(p);
        table1.addCell(cell1);

        cell1 = new PdfPCell();
        cell1.setBorder(0);
        String qrcodeData = lclBooking.getLclFileNumber().getFileNumber();
        BarcodeQRCode qrcode = new BarcodeQRCode(qrcodeData.toUpperCase().trim(), 10, 2, null);
        Image qrcodeImage = qrcode.getImage();
        qrcodeImage.scalePercent(500);
        cell1.addElement(qrcodeImage);
        table1.addCell(cell1);

        cell1 = new PdfPCell();
        cell1.setBorder(0);
        cell1.setPaddingTop(-15f);
        cell1.setPaddingLeft(50f);
        p = new Paragraph(10f, "" + pooUnLocCode, arialBoldFont14Size);
        p.setAlignment(Element.ALIGN_LEFT);
        cell1.addElement(p);
        table1.addCell(cell1);

        cell.addElement(table1);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        table.addCell(cell);
        Font Ex_large = new Font(arialBold, 65f, Font.BOLD);
        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(30f, "" + lclBooking.getLclFileNumber().getFileNumber(), Ex_large);
        cell.setPaddingRight(-30f);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        String pieces = pieceCount > 1 ? "Pieces" : "Piece";
        p = new Paragraph(30f, "" + pieceCount, large);
        p.add(new Paragraph(30f, "" + pieces, arialBoldFont14Size));
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(4);
        p = new Paragraph(20f, "Please place label on freight", arialBoldFont14Size);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(4);
        String mnemonicName = ruleName.equalsIgnoreCase("ECU") ? "ECU Worldwide" : ruleName.equalsIgnoreCase("OTI") ? "OTI" : "Econocaribe";
        p = new Paragraph(10f, "Thank you for shipping with " + company + "-Gracias Por Embarcar Con " + mnemonicName, arialFontSize10Normal);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);

        if (labelCount <= 1) {
            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setBorder(0);
            p = new Paragraph(10f, "", arialFontSize10Normal);
            p.add(new Chunk("Cut Here", arialFontSize10Normal));
            p.setAlignment(Element.ALIGN_RIGHT);
            cell.setBorderWidthBottom(0.6f);
            cell.addElement(p);
            table.addCell(cell);
        }
        return table;
    }

    public void setemailReport(LclBooking lclBooking) throws Exception {
        this.lclBooking = lclBooking;
        new LCLBookingDAO().getCurrentSession().evict(lclBooking);
        ruleName = lclBooking.getLclFileNumber().getBusinessUnit();
        if (CommonUtils.isEmpty(ruleName)) {
            ruleName = "ECU";
        }
        String[] remarkTypes = new String[]{"OSD", "Loading Remarks", "E", "G", "Special Remarks"};
        List remarks = new LclRemarksDAO().getRemarksByTypes(lclBooking.getFileNumberId(), remarkTypes);
        for (Object row : remarks) {
            Object[] col = (Object[]) row;
            if ("OSD".equalsIgnoreCase(col[1].toString()) && col[0] != null) {
                osdRemarks = col[0].toString();
            }
            if ("Loading Remarks".equalsIgnoreCase(col[1].toString()) && col[0] != null) {
                loadingRemarks = col[0].toString();
            }
            if ("G".equalsIgnoreCase(col[1].toString()) && col[0] != null) {
                regionalGriRemarks = col[0].toString();
            }
            if ("E".equalsIgnoreCase(col[1].toString()) && col[0] != null) {
                externalRemarks = col[0].toString();
            }
            if ("Special Remarks".equalsIgnoreCase(col[1].toString()) && col[0] != null) {
                specialRemarks = col[0].toString();
            }
        }
        wareHouseNumber = new Lcl3pRefNoDAO().get3pWHList(lclBooking.getFileNumberId());

        LclSsHeader bookedMasterSch = lclBooking.getBookedSsHeaderId();
        if (null != bookedMasterSch && null != bookedMasterSch.getVesselSsDetail()) {
            bookedSsDetails = bookedMasterSch.getVesselSsDetail();
        }
        terminalNo = lclBooking.getTerminal().getTrmnum();
        RefTerminal refTerminal = lclBooking.getTerminal();
        if (CommonFunctions.isNotNull(refTerminal)) {
            if (CommonFunctions.isNotNull(refTerminal.getTrmnam())) {
                companyName = refTerminal.getTrmnam();
            }
            if (CommonUtils.isNotEmpty(terminalDetails)) {
                terminalDetails.setLength(0);
            }
            if (CommonFunctions.isNotNull(refTerminal.getAddres1())) {
                terminalDetails.append(refTerminal.getAddres1()).append(" * ");
            }
            if (CommonFunctions.isNotNull(refTerminal.getCity1())) {
                terminalDetails.append(refTerminal.getCity1()).append(",");
            }
            if (CommonFunctions.isNotNull(refTerminal.getState())) {
                terminalDetails.append(refTerminal.getState()).append(" ");
            }
            if (CommonFunctions.isNotNull(refTerminal.getZipcde())) {
                terminalDetails.append(refTerminal.getZipcde()).append(" ");
            }
        }
        if (CommonFunctions.isNotNull(refTerminal.getPhnnum1())) {
            String pNoSpaceRemove = StringUtils.remove(refTerminal.getPhnnum1(), " ");
            String ph1 = pNoSpaceRemove.substring(0, 3);
            String ph2 = pNoSpaceRemove.substring(3, 6);
            String ph3 = pNoSpaceRemove.substring(6);
            phoneNumber = "Phone " + ph1 + "-" + ph2 + "-" + ph3;
        }

        if (CommonFunctions.isNotNull(refTerminal.getFaxnum1())) {
            String faxNoSpaceRemove = StringUtils.remove(refTerminal.getFaxnum1(), " ");
            String fax1 = faxNoSpaceRemove.substring(0, 3);
            String fax2 = faxNoSpaceRemove.substring(3, 6);
            String fax3 = faxNoSpaceRemove.substring(6);
            faxNumber = " * Fax " + fax1 + "-" + fax2 + "-" + fax3;
        }
        emailId = new ExportNotificationDAO().getFromAddress(lclBooking.getFileNumberId());
        if (lclBooking.getPooWhseContact() != null) {
            if (CommonUtils.isNotEmpty(deliverCargoValues)) {
                deliverCargoValues.setLength(0);
            }
            if (null != lclBooking.getPooWhseContact().getWarehouse() && CommonUtils.isNotEmpty(lclBooking.getPooWhseContact().getWarehouse().getWarehouseNo())) {
                deliverCargoValues.append(lclBooking.getPooWhseContact().getWarehouse().getWarehouseNo()).append("\n");
            }
            if (CommonUtils.isNotEmpty(lclBooking.getPooWhseContact().getCompanyName())) {
                deliverCargoValues.append(lclBooking.getPooWhseContact().getCompanyName()).append("\n");
            }
            if (CommonUtils.isNotEmpty(lclBooking.getPooWhseContact().getAddress())) {
                deliverCargoValues.append(lclBooking.getPooWhseContact().getAddress()).append("\n");
            }
            if (CommonUtils.isNotEmpty(lclBooking.getPooWhseContact().getCity())) {
                deliverCargoValues.append(lclBooking.getPooWhseContact().getCity()).append(",");
            }
            if (CommonUtils.isNotEmpty(lclBooking.getPooWhseContact().getState())) {
                deliverCargoValues.append(lclBooking.getPooWhseContact().getState()).append("  ");
            }
            if (CommonUtils.isNotEmpty(lclBooking.getPooWhseContact().getZip())) {
                deliverCargoValues.append(lclBooking.getPooWhseContact().getZip()).append("\n");
            }
            if (CommonUtils.isNotEmpty(lclBooking.getPooWhseContact().getPhone1())) {
                deliverCargoValues.append("Phone:").append(lclBooking.getPooWhseContact().getPhone1());
            }
        }
//        bookingStatus = lclBooking.getLclFileNumber().getStatus();
        LclBookingPad lclBookingPad = new LclBookingPadDAO().getLclBookingPadByFileNumber(lclBooking.getFileNumberId());
        if (lclBookingPad != null && lclBookingPad.getPickupReferenceNo() != null) {
            additionalRefNo = lclBookingPad.getPickupReferenceNo();
        }
        billingMethod = "P".equalsIgnoreCase(lclBooking.getBillingType()) ? "FREIGHT PREPAID"
                : "C".equalsIgnoreCase(lclBooking.getBillingType()) ? "FREIGHT COLLECT" : "BOTH";

        if (lclBooking.getLclFileNumber().getLclBookingDispoList() != null) {
            //bkStatus = new LclBookingDispoDAO().getDispositionCode(lclBooking.getFileNumberId());
            dispo = new LclBookingDispoDAO().getDispositionCode(lclBooking.getFileNumberId());
            bkStatus = !"OBKG".equalsIgnoreCase(dispo) ? "Warehouse" : "Booking";
        }
        fileStatusFlag = !"Booking".equalsIgnoreCase(bkStatus) ? true : false;
        printBookingComments();
    }

    public String createEmailReport(String realPath, String outputFileName, ExportVoyageHblBatchForm batchForm, HttpServletRequest request) throws Exception {

        List<Long> actualDrList = new ArrayList<Long>();
        if (CommonUtils.isNotEmpty(batchForm.getFileNumberId())) {
            actualDrList.add(Long.parseLong(batchForm.getFileNumberId()));
        } else {
            actualDrList = new ExportUnitQueryUtils().getAllPickedCargoBkgUnrated(Long.parseLong(batchForm.getHeaderId()),
                    Long.parseLong(batchForm.getUnitSSId()));
        }
        if (CommonUtils.isNotEmpty(actualDrList)) {
            for (Long fileId : actualDrList) {
                LclBooking lclBooking = new LCLBookingDAO().getByProperty("lclFileNumber.id", fileId);
                new LCLBookingDAO().getCurrentSession().evict(lclBooking);
                this.lclBooking = lclBooking;
                setemailReport(lclBooking);
                if (!isNew) {
                    document = new Document();
                    document.setPageSize(PageSize.A4);
                    document.setMargins(8, 2, 8, 8);
                    pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(outputFileName));
                    document.open();
                }
                if (!documentOpenFlag) {
                    documentOpenFlag = true;
                    isNew = true;
                    commonEmailPdf(realPath, "Booking Confirmation Without Rate", request);
                } else {
                    document.newPage();
                    commonEmailPdf(realPath, "Booking Confirmation Without Rate", request);
                }
            }
            if (documentOpenFlag) {
                document.close();
            } else {
                outputFileName = null;
            }

        }
        return outputFileName;
    }

    public void commonEmailPdf(String realPath, String documentName, HttpServletRequest request) throws DocumentException, Exception {
        document.add(onStartPage(realPath));
        document.add(contentTable());
        document.add(lineTable());
        document.add(bodyTable());
        document.add(bodyConsiTable());
        document.add(bodyOrginTable());
        document.add(lineTable1());
        document.add(contentDisplayTable());
        document.add(lineTable());
        document.add(commodityContentTable());
        document.add(commodityValuesTable());
        document.add(additionalTable());
        document.add(deliverTable(documentName));
        document.add(overseasTable());
        document.add(voyageTable(documentName));
        document.add(emptyLargeTable());
        document.add(contentFinalTable());
        document.add(contentFinalTable1(request));
        if (bkStatus.equalsIgnoreCase("Booking")) {
            document.newPage();
            document.add(addingPrintingLabelWithQrCode(realPath));
            document.add(addingPrintingLabelWithQrCode(realPath));
        }
    }
}
