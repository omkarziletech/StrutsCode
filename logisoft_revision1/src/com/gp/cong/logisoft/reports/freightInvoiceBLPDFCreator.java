package com.gp.cong.logisoft.reports;

import java.awt.Color;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.struts.util.MessageResources;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.bc.accounting.ReportConstants;
import com.gp.cong.logisoft.bc.fcl.FclBlBC;
import com.gp.cong.logisoft.bc.fcl.FclBlConstants;
import com.gp.cong.logisoft.bc.fcl.HazmatBC;
import com.gp.cong.logisoft.bc.print.PrintConfigBC;
import com.gp.cong.logisoft.bc.print.PrintReportsConstants;
import com.gp.cong.logisoft.domain.CustomerAccounting;
import com.gp.cong.logisoft.domain.FclInbondDetails;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.FclInbondDetailsDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.CustAddress;
import com.gp.cvst.logisoft.domain.FclAESDetails;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.FclBlCharges;
import com.gp.cvst.logisoft.domain.FclBlContainer;
import com.gp.cvst.logisoft.domain.FclBlMarks;
import com.gp.cvst.logisoft.hibernate.dao.CustAddressDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlContainerDAO;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfGState;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTemplate;
import com.gp.cong.logisoft.edi.inttra.HelperClass;
import com.gp.cong.logisoft.hibernate.dao.CustomerAccountingDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlDAO;
import com.logiware.common.reports.PdfCreator;
import com.lowagie.text.pdf.PdfWriter;
import java.util.Arrays;
import java.util.Date;
import org.apache.log4j.Logger;

public class freightInvoiceBLPDFCreator extends ReportFormatMethods {

    private static final Logger log = Logger.getLogger(PdfCreator.class);
    FclBlBC fclBlBC = new FclBlBC();
    Document document = null;
    PdfWriter pdfWriter = null;
    protected PdfTemplate total;
    protected BaseFont helv;
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    SimpleDateFormat currentdateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
    private MessageResources messageResources = null;
    HelperClass helperClass = new HelperClass();
    private String simpleRequest = "";
    private FclBl fclBl = null;
    private String context = "";
    private List commentsList = null;
    private String manifestRev = "";
    private String logoS = "";
    private int ADDRESS_SIZE = 0;
    private int DESC_SIZE = 0;
    private static String BL100 = "";
    private static String BL101 = "";
    private static String BL102 = "";
    private static String FI100 = "";
    private static String FI101 = "";
    private static String FI102 = "";

    public freightInvoiceBLPDFCreator() {
    }

    public freightInvoiceBLPDFCreator(FclBl bl, List commentList,
            String request, MessageResources messageResource, String contextPath, String logoStatus) throws Exception {
        fclBl = bl;
        commentsList = commentList;
        messageResources = messageResource;
        context = contextPath;
        simpleRequest = request;
        logoS = logoStatus;
        FclBl fclbl = new FclBlDAO().getOriginalBl(bl.getFileNo());
        if (bl.getBolId() != null && bl.getBolId().indexOf("=") != -1) {
            if (CommonFunctions.isNotNull(fclbl.getCorrectionCount()) && "Yes".equalsIgnoreCase(fclbl.getPrintRev())) {
                if (CommonFunctions.isNotNull(fclbl.getManifestRev())) {
                    int count = fclbl.getCorrectionCount() + Integer.parseInt(fclbl.getManifestRev());
                    manifestRev = "" + count;
                } else {
                    manifestRev = "" + fclbl.getCorrectionCount();
                }
            }
        } else {
            if (CommonFunctions.isNotNull(fclBl.getManifestRev()) && "Yes".equalsIgnoreCase(fclBl.getPrintRev())) {
                manifestRev = fclBl.getManifestRev();
            }
        }
    }

    public void initialize(String fileName, String contextPath, FclBl fclBl, List commentsList,
            String simpleRequest, MessageResources messageResource, String logoStatus) throws Exception {

        document = new Document(PageSize.A4);
        document.setMargins(0, 0, 0, 280);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(
                fileName));
        pdfWriter.setPageEvent(new freightInvoiceBLPDFCreator(fclBl, commentsList, simpleRequest, messageResource, contextPath, logoStatus));
        document.setHeader(getMyHeader(fclBl, contextPath, messageResource, simpleRequest));
        document.open();
    }

    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        total = writer.getDirectContent().createTemplate(100, 100);
        total.setBoundingBox(new Rectangle(-20, -20, 250, 250));
        try {
            helv = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI,
                    BaseFont.NOT_EMBEDDED);
        } catch (Exception e) {
            log.info("onOpenDocument failed on " + new Date(), e);
            throw new ExceptionConverter(e);
        }
    }

    public HeaderFooter getMyHeader(FclBl bl, String contextPath, MessageResources messageResources, String simpleRequest) throws Exception {
        FclBl originalBl = new FclBlDAO().getOriginalBl(bl.getFileNo());
        Phrase phrase = new Phrase(-16);
        PdfPCell cell = null;
        PdfPCell mainCell = null;
        String companyName = "";
        Phrase phraseLogoS = new Phrase();
        PdfPCell footerCellLogoS = null;
        BaseFont palationRomanBase = BaseFont.createFont(contextPath + "/ttf/Palatino-Roman.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font palatinoRomanSmallFont = new Font(palationRomanBase, 8, Font.NORMAL, Color.BLACK);
        Font palatinoRomanMediumFont = new Font(palationRomanBase, 10, Font.NORMAL, Color.BLACK);
        Font palatinoRomanBigFont = new Font(palationRomanBase, 12, Font.NORMAL, Color.BLACK);
        // Heading table
        PdfPTable headingTable = makeTable(3);
        headingTable.setWidthPercentage(100);
//        if ("Y".equalsIgnoreCase(logoS)) {
//            companyName = LoadLogisoftProperties.getProperty("application.fclBl.print.companyName");
//            if (companyName.contains("(")) {
//                phraseLogoS.add(new Phrase(new Chunk(companyName.substring(0, companyName.indexOf("(")), bottom3TextFontHead)));
//                phraseLogoS.add(new Phrase(new Chunk(" " + companyName.substring(companyName.indexOf("(")), bottomTextFontSub)));
//            } else {
//                phraseLogoS.add(new Phrase(companyName.toUpperCase(), bottom3TextFontHead));
//            }
//            footerCellLogoS = makeCell(phraseLogoS, Element.ALIGN_LEFT);
//        } else {
//            companyName = LoadLogisoftProperties.getProperty("application.fclBl.print.companyName.econo");
//            phraseLogoS.add(new Phrase(companyName.toUpperCase(), bottom3TextFontHead));
//            footerCellLogoS = makeCell(phraseLogoS, Element.ALIGN_LEFT);
//        }
        String companyCode = new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
        if (originalBl.getBrand().equalsIgnoreCase("Econo") && ("03").equals(companyCode)) {
            companyName = LoadLogisoftProperties.getProperty("application.Econo.companyname");
            phraseLogoS.add(new Phrase(companyName.toUpperCase(), bottom3TextFontHead));
            footerCellLogoS = makeCell(phraseLogoS, Element.ALIGN_LEFT);
        } else if (originalBl.getBrand().equalsIgnoreCase("OTI") && ("02").equals(companyCode)) {
            companyName = LoadLogisoftProperties.getProperty("application.OTI.companyname");
            phraseLogoS.add(new Phrase(companyName.toUpperCase(), bottom3TextFontHead));
            footerCellLogoS = makeCell(phraseLogoS, Element.ALIGN_LEFT);
        } else if (originalBl.getBrand().equalsIgnoreCase("Ecu Worldwide")) {
            companyName = LoadLogisoftProperties.getProperty("application.ECU.companyname");
            phraseLogoS.add(new Phrase(companyName.toUpperCase(), bottom3TextFontHead));
            footerCellLogoS = makeCell(phraseLogoS, Element.ALIGN_LEFT);
        }

        headingTable.addCell(footerCellLogoS);
        if (originalBl.getBolId() != null && originalBl.getBolId().indexOf("=") != -1) {
            String type = "";
            if (CommonUtils.isNotEmpty(originalBl.getManifestRev()) && Integer.parseInt(originalBl.getManifestRev()) > 0) {
                type = "REVISED \n";
            }
            cell = makeCellCenterNoBorderPalatinoFclBl(type + "CORRECTED", palatinoRomanBigFont);
            cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
            headingTable.addCell(cell);
        } else if (CommonUtils.isNotEmpty(originalBl.getManifestRev()) && Integer.parseInt(originalBl.getManifestRev()) > 0) {
            cell = makeCellCenterNoBorderPalatinoFclBl("REVISED", palatinoRomanBigFont);
            cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
            headingTable.addCell(cell);

        } else {
            cell = makeCellCenterNoBorderPalatinoFclBl("", palatinoRomanBigFont);
            headingTable.addCell(cell);
        }
        cell = makeCellleftNoBorderForHeadingFclBLWithBlack(ReportConstants.FRIEGHTINVOICE);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        headingTable.addCell(cell);

        // details table for bl
        PdfPTable detailsTable = makeTable(2);
        detailsTable.setWidthPercentage(100);
        // shipper exporter table
        String houseShipperAddr = "";
        mainCell = makeCellleftNoBorderWithBoldFont("");
        mainCell.setBorderWidthLeft(0.0f);
        mainCell.setBorderWidthRight(0.6f);
        mainCell.setBorderWidthBottom(0.6f);
        mainCell.setBorderWidthTop(0.6f);
        PdfPTable shipperExporter = makeTable(1);
        shipperExporter.setWidthPercentage(100);
        cell = makeCellLeftNoBorderFclBL("SHIPPER/EXPORTER");
        shipperExporter.addCell(cell);
        houseShipperAddr = bl.getShipperAddress() != null ? bl.getShipperAddress() : originalBl.getShipperAddress();
        houseShipperAddr = trimShipperConsigNotifyForwardAddress(houseShipperAddr);
        StringBuilder shipperDetails = new StringBuilder();
        shipperDetails.append(bl.getShipperName() != null ? bl.getShipperName() : originalBl.getShipperName());
        shipperDetails.append("\n");
        shipperDetails.append(houseShipperAddr);
        List l = helperClass.wrapAddress(houseShipperAddr);
        if (l.isEmpty() || l.size() < 5) {
            ADDRESS_SIZE += 4;
        } else {
            ADDRESS_SIZE += l.size();
        }
        cell = makeCellLeftNoBorderPalatinoFclBl(shipperDetails.toString(), palatinoRomanMediumFont);
        shipperExporter.addCell(cell);

        mainCell.addElement(shipperExporter);

        detailsTable.addCell(mainCell);
        // document number and export reference table
        mainCell = makeCellleftNoBorderWithBoldFont("");
        mainCell.setBorderWidthLeft(0);
        mainCell.setBorderWidthRight(0);
        mainCell.setBorderWidthBottom(0.6f);
        mainCell.setBorderWidthTop(0.6f);
        PdfPTable documentNoTable = makeTable(1);
        documentNoTable.setWidthPercentage(101);
        cell = makeCellLeftNoBorderFclBL("INVOICE NO.");
        documentNoTable.addCell(cell);
        String invoiceNo = (String) messageResources.getMessage("fileNumberPrefix") + originalBl.getFileNo().toString();
        if (null != originalBl.getHouseBl() && originalBl.getHouseBl().equalsIgnoreCase("B-Both")) {
            if (simpleRequest.equalsIgnoreCase(PrintReportsConstants.FREIGHT_INVOICE_AGENT)) {
                invoiceNo = (String) messageResources.getMessage("fileNumberPrefix") + originalBl.getFileNo().toString() + "-" + "CA";
            } else if (simpleRequest.equalsIgnoreCase(PrintReportsConstants.FREIGHT_INVOICE_FORWARDER)) {
                invoiceNo = (String) messageResources.getMessage("fileNumberPrefix") + originalBl.getFileNo().toString() + "-" + "PF";
            } else if (simpleRequest.equalsIgnoreCase(PrintReportsConstants.FREIGHT_INVOICE_SHIPPER)) {
                invoiceNo = (String) messageResources.getMessage("fileNumberPrefix") + originalBl.getFileNo().toString() + "-" + "PS";
            } else if (simpleRequest.equalsIgnoreCase(PrintReportsConstants.FREIGHT_INVOICE_THIRDPARTY)) {
                invoiceNo = (String) messageResources.getMessage("fileNumberPrefix") + originalBl.getFileNo().toString() + "-" + "PT";
            } else {
                invoiceNo = (String) messageResources.getMessage("fileNumberPrefix") + originalBl.getFileNo().toString();
            }
        } else {
            invoiceNo = (String) messageResources.getMessage("fileNumberPrefix") + originalBl.getFileNo().toString();
        }
        String exportReferenceMorelength = "";
        cell = makeCellLeftNoBorderPalatinoFclBl(invoiceNo, palatinoRomanMediumFont);
        cell.setBorderWidthBottom(0.6f);
        documentNoTable.addCell(cell);
        mainCell.addElement(documentNoTable);
        PdfPTable exportReferenceTable = makeTable(1);
        exportReferenceTable.setWidthPercentage(100);
        cell = makeCellLeftNoBorderFclBL("EXPORT REFERENCES");
        exportReferenceTable.addCell(cell);
        exportReferenceMorelength = originalBl.getExportReference() != null ? originalBl.getExportReference() : "";
        cell = makeCellLeftNoBorderPalatinoFclBl(exportReferenceMorelength.length() > 250 ? exportReferenceMorelength.substring(0,250) : exportReferenceMorelength, palatinoRomanMediumFont);
        cell.setMinimumHeight(40);
        exportReferenceTable.addCell(cell);
        mainCell.addElement(exportReferenceTable);

        detailsTable.addCell(mainCell);
        // Consignee table
        mainCell = makeCellleftNoBorderWithBoldFont("");
        String houseConsigneeAddr = "";
        mainCell.setBorderWidthLeft(0.0f);
        mainCell.setBorderWidthRight(0.6f);
        mainCell.setBorderWidthBottom(0.6f);
        mainCell.setBorderWidthTop(0.0f);
        PdfPTable consigneeTable = makeTable(1);
        consigneeTable.setWidthPercentage(100);
        cell = makeCellLeftNoBorderFclBL("CONSIGNEE");
        consigneeTable.addCell(cell);
        houseConsigneeAddr = null != originalBl.getConsigneeAddress() ? originalBl.getConsigneeAddress() : "";
        houseConsigneeAddr = trimShipperConsigNotifyForwardAddress(houseConsigneeAddr);
        StringBuilder consigneeDetails = new StringBuilder();
        consigneeDetails.append(originalBl.getConsigneeName() != null ? originalBl.getConsigneeName() : "");
        consigneeDetails.append("\n");
        consigneeDetails.append(houseConsigneeAddr);
        cell = makeCellLeftNoBorderPalatinoFclBl(consigneeDetails.toString(), palatinoRomanMediumFont);
        consigneeTable.addCell(cell);

        mainCell.addElement(consigneeTable);

        detailsTable.addCell(mainCell);
        // FORWARDING AND POINT OF COUNTRY TABLE
        mainCell = makeCellleftNoBorderWithBoldFont("");
        mainCell.setBorderWidthLeft(0);
        mainCell.setBorderWidthRight(0);
        mainCell.setBorderWidthBottom(0.6f);
        mainCell.setBorderWidthTop(0.0f);
        PdfPTable forwardingTable = makeTable(1);
        forwardingTable.setWidthPercentage(101);
        cell = makeCellLeftNoBorderFclBL("FORWARDING AGENT - REFERENCES");
        forwardingTable.addCell(cell);
        String forwardingAgent = "";
        StringBuilder forwardingAgentDetails = new StringBuilder();
        if (null != bl.getForwardingAgentName() && !bl.getForwardingAgentName().trim().equalsIgnoreCase("NO FF ASSIGNED".trim())
                && !bl.getForwardingAgentName().trim().equalsIgnoreCase("NO FF ASSIGNED / B/L PROVIDED".trim())) {
            forwardingAgent = null != bl.getForwardingAgent() ? bl.getForwardingAgent().trim() : "";
            forwardingAgent = trimShipperConsigNotifyForwardAddress(forwardingAgent);
            if (bl.getBolId().indexOf("==") != -1 && CommonUtils.isEmpty(bl.getForwardingAgent())) {
                forwardingAgent = trimShipperConsigNotifyForwardAddress(getAddress(bl.getForwardAgentNo()));
            }
            forwardingAgentDetails.append(bl.getForwardingAgentName() != null ? bl.getForwardingAgentName() : "");
            forwardingAgentDetails.append("\n");
            forwardingAgentDetails.append(forwardingAgent);
            l = helperClass.wrapAddress(forwardingAgent);
            if (l.isEmpty() || l.size() < 5) {
                ADDRESS_SIZE += 7;
            } else {
                ADDRESS_SIZE += l.size() + 3;
            }
        } else {
            forwardingAgentDetails.append("");
            ADDRESS_SIZE += 7;
        }
        cell = makeCellLeftNoBorderPalatinoFclBl(forwardingAgentDetails.toString(), palatinoRomanMediumFont);
        cell.setBorderWidthBottom(0.6f);
        cell.setMinimumHeight(40);
        forwardingTable.addCell(cell);
        mainCell.addElement(forwardingTable);
        PdfPTable pointAndCountryTable = makeTable(1);
        pointAndCountryTable.setWidthPercentage(100);
        cell = makeCellLeftNoBorderFclBL("POINT AND COUNTRY OF ORIGIN");
        pointAndCountryTable.addCell(cell);
        if (null != originalBl.getCountryOfOrigin() && originalBl.getCountryOfOrigin().equalsIgnoreCase("Yes")) {
            cell = makeCellLeftNoBorderPalatinoFclBl(originalBl.getRoutedByAgentCountry().toUpperCase() != null ? originalBl.getRoutedByAgentCountry().toUpperCase()
                    : "", palatinoRomanMediumFont);
        } else {
            cell = makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanMediumFont);
        }
        cell.setMinimumHeight(20);
        pointAndCountryTable.addCell(cell);
        mainCell.addElement(pointAndCountryTable);

        detailsTable.addCell(mainCell);
        // NOTIFY PARTY TABLE
        mainCell = makeCellleftNoBorderWithBoldFont("");
        mainCell.setBorderWidthLeft(0.0f);
        mainCell.setBorderWidthRight(0.6f);
        mainCell.setBorderWidthBottom(0.6f);
        mainCell.setBorderWidthTop(0.0f);
        PdfPTable notifyPartyTable = makeTable(1);
        notifyPartyTable.setWidthPercentage(100);
        cell = makeCellLeftNoBorderFclBL("NOTIFY PARTY");
        notifyPartyTable.addCell(cell);
        String houseNotifyParty = "";
        houseNotifyParty = originalBl.getStreamshipNotifyParty() != null ? originalBl.getStreamshipNotifyParty() : "";
        houseNotifyParty = trimShipperConsigNotifyForwardAddress(houseNotifyParty);
        StringBuilder notifyPartyDetails = new StringBuilder();
        notifyPartyDetails.append(originalBl.getNotifyPartyName() != null ? originalBl.getNotifyPartyName() : "");
        notifyPartyDetails.append("\n");
        notifyPartyDetails.append(houseNotifyParty);
        cell = makeCellLeftNoBorderPalatinoFclBl(notifyPartyDetails.toString(), palatinoRomanMediumFont);
        cell.setMinimumHeight(50);
        notifyPartyTable.addCell(cell);
        l = helperClass.wrapAddress(houseNotifyParty);
        if (l.isEmpty() || l.size() < 5) {
            ADDRESS_SIZE += 4;
        } else {
            ADDRESS_SIZE += l.size();
        }

        mainCell.addElement(notifyPartyTable);
        if (ADDRESS_SIZE <= 19) {
            DESC_SIZE = 15;
        } else if (ADDRESS_SIZE > 27) {
            DESC_SIZE = 3;
        } else if (ADDRESS_SIZE > 26) {
            DESC_SIZE = 5;
        } else if (ADDRESS_SIZE > 25) {
            DESC_SIZE = 6;
        } else if (ADDRESS_SIZE > 24) {
            DESC_SIZE = 8;
        } else if (ADDRESS_SIZE > 23) {
            DESC_SIZE = 9;
        } else if (ADDRESS_SIZE > 22) {
            DESC_SIZE = 10;
        } else if (ADDRESS_SIZE > 21) {
            DESC_SIZE = 11;
        } else if (ADDRESS_SIZE > 20) {
            DESC_SIZE = 13;
        } else if (ADDRESS_SIZE > 19) {
            DESC_SIZE = 14;
        }
        detailsTable.addCell(mainCell);
        // DOMESTIC ROUTING /EXPORT INSTRUCTIONS
        mainCell = makeCellleftNoBorderWithBoldFont("");
        mainCell.setBorderWidthLeft(0.0f);
        mainCell.setBorderWidthRight(0.0f);
        mainCell.setBorderWidthBottom(0.6f);
        mainCell.setBorderWidthTop(0.0f);
        PdfPTable domesticTable = makeTable(1);
        domesticTable.setWidthPercentage(100);
        cell = makeCellLeftNoBorderFclBL("DOMESTIC ROUTING/EXPORT INSTRUCTIONS");
        domesticTable.addCell(cell);
//        cell = makeCellLeftNoBorderPalatinoFclBl("Issued By  "+getIssuingTM(fclBl),palatinoRomanMediumFont);
        cell = makeCellLeftNoBorderPalatinoFclBl(originalBl.getDomesticRouting(), palatinoRomanMediumFont);
        domesticTable.addCell(cell);
        mainCell.addElement(domesticTable);

        detailsTable.addCell(mainCell);
        // PLACE OF RECIEPT,EXPORTING CARRIER ,PORT OF LOADING,SEA PORT OF
        // DISCHARGE AND FINAL DELIVERY TO
        mainCell = makeCellleftNoBorderWithBoldFont("");
        mainCell.setBorderWidthLeft(0.0f);
        mainCell.setBorderWidthRight(0.6f);
        mainCell.setBorderWidthBottom(0.6f);
        mainCell.setBorderWidthTop(0.0f);

        PdfPTable placeofRecieptTable = makeTable(1);
        placeofRecieptTable.setWidthPercentage(100);
        cell = makeCellLeftNoBorderFclBL("PLACE OF RECEIPT");
        placeofRecieptTable.addCell(cell);
        if (originalBl.getHblPlaceReceiptOverride().equalsIgnoreCase("yes")) {
            cell = makeCellLeftNoBorderPalatinoFclBl(originalBl.getHblPlaceReceipt().toUpperCase(), palatinoRomanMediumFont);
        } else {
            cell = makeCellLeftNoBorderPalatinoFclBl(removeUnlocCode(fclBlBC.getDoorOriginForHouseBlPrint(originalBl)), palatinoRomanMediumFont);
        }
        cell.setBorderWidthBottom(0.6f);
        placeofRecieptTable.addCell(cell);
        mainCell.addElement(placeofRecieptTable);

        PdfPTable exportingAndPol = makeTable(2);
        exportingAndPol.setWidthPercentage(100);
        cell = makeCellLeftNoBorderFclBL("EXPORTING CARRIER(Vessel)(Flag)");
        cell.setBorderWidthRight(0.6f);
        exportingAndPol.addCell(cell);
        cell = makeCellLeftNoBorderFclBL("PORT OF LOADING");
        exportingAndPol.addCell(cell);
        StringBuilder vesselVoyage = new StringBuilder();
        vesselVoyage.append(originalBl.getVessel() != null ? (originalBl.getVessel().getCodedesc() != null ? originalBl.getVessel().getCodedesc() : "") : "");
        vesselVoyage.append(" ");
        vesselVoyage.append("V.");
        vesselVoyage.append(originalBl.getVoyages() != null ? originalBl.getVoyages() : "");
        cell = makeCellLeftNoBorderPalatinoFclBl(vesselVoyage.toString(), palatinoRomanSmallFont);
        cell.setBorderWidthRight(0.6f);
        cell.setBorderWidthBottom(0.6f);
        exportingAndPol.addCell(cell);
        cell = makeCellLeftNoBorderPalatinoFclBl(originalBl.getPortOfLoading() != null ? removeUnlocCode(originalBl.getPortOfLoading()) : "", palatinoRomanSmallFont);
        cell.setBorderWidthBottom(0.6f);
        exportingAndPol.addCell(cell);

        mainCell.addElement(exportingAndPol);

        PdfPTable seaPortFinalyDelivery = makeTable(2);
        seaPortFinalyDelivery.setWidthPercentage(100);
        cell = makeCellLeftNoBorderFclBL("SEA PORT OF DISCHARGE");
        cell.setBorderWidthRight(0.6f);
        seaPortFinalyDelivery.addCell(cell);
        cell = makeCellLeftNoBorderFclBL("FINAL DELIVERY TO");
        seaPortFinalyDelivery.addCell(cell);
        cell = makeCellLeftNoBorderPalatinoFclBl(originalBl.getPortofDischarge() != null
                ? removeUnlocCodeAppendCountryName(originalBl.getPortofDischarge()) : "", palatinoRomanSmallFont);
        cell.setBorderWidthRight(0.6f);
        seaPortFinalyDelivery.addCell(cell);
        // Get Final delivery based on these prefernces 1. House BL Override value 2. Door Dest 3. Destination
        String finalDelivery = "";
        String doorDest = "I".equalsIgnoreCase(originalBl.getImportFlag()) ? originalBl.getDoorOfOrigin() : originalBl.getDoorOfDestination();
        if (CommonUtils.isNotEmpty(doorDest)) {
            finalDelivery = doorDest;
        } else if (CommonUtils.isNotEmpty(originalBl.getFinalDestination())) {
            finalDelivery = originalBl.getFinalDestination();
        }
        // ends
        String portName = "";
        if ("yes".equalsIgnoreCase(originalBl.getHblFDOverride())) {
            portName = originalBl.getHblFD().toUpperCase();
        } else {
            portName = fclBlBC.getPortName(finalDelivery);
        }
        cell = makeCellLeftNoBorderPalatinoFclBl((null != portName ? portName : ""), palatinoRomanSmallFont);
        seaPortFinalyDelivery.addCell(cell);
        mainCell.addElement(seaPortFinalyDelivery);
        mainCell.setPadding(0);
        detailsTable.addCell(mainCell);
        // ONWARD INLAND ROUTING
        mainCell = makeCellleftNoBorderWithBoldFont("");
        mainCell.setBorderWidthLeft(0.0f);
        mainCell.setBorderWidthRight(0.0f);
        mainCell.setBorderWidthBottom(0.6f);
        mainCell.setBorderWidthTop(0.0f);

        PdfPTable onwardInlandRouting = makeTable(1);
        onwardInlandRouting.setWidthPercentage(100);
        cell = makeCellLeftNoBorderFclBL("ONWARD INLAND ROUTING");
        onwardInlandRouting.addCell(cell);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        PdfPTable upperTable = makeTable(1);
        upperTable.setWidthPercentage(100);
        PdfPCell inlandCell = makeCellLeftNoBorderPalatinoFclBl(originalBl.getOnwardInlandRouting() != null ? originalBl.getOnwardInlandRouting()
                : "", palatinoRomanMediumFont);
        upperTable.addCell(inlandCell);
        cell.addElement(upperTable);
        if (originalBl.getShipperLoadsAndCounts() != null && originalBl.getShipperLoadsAndCounts().equalsIgnoreCase("yes")) {
            PdfPTable innerTable = makeTable(1);
            innerTable.setWidthPercentage(100);
            PdfPCell innerCell = makeCellLeftNoBorderFclBL("");
            Chunk chunkStow = new Chunk(PrintReportsConstants.SHIPPER_LOAD_STOW_COUNT);
            chunkStow.setFont(new Font(Font.HELVETICA, 13, Font.BOLD, Color.BLACK));
            chunkStow.setTextRenderMode(PdfContentByte.TEXT_RENDER_MODE_STROKE,
                    0.3f, Color.black);
            innerCell.addElement(chunkStow);
            innerCell.setHorizontalAlignment(Element.ALIGN_BOTTOM);
            innerCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
            innerCell.setNoWrap(true);
            innerCell.setPaddingTop(25);
            innerTable.addCell(innerCell);
            cell.addElement(innerTable);
            // cell.setPaddingTop(50);
        }

        onwardInlandRouting.addCell(cell);
        mainCell.addElement(onwardInlandRouting);
        detailsTable.addCell(mainCell);
        cell = makeCellLeftNoBorderFclBL("");
        cell.setBorderWidthBottom(0.6f);
        cell.setBorderWidthTop(0.0f);
        cell.setBorderWidthLeft(0.0f);
        cell.setColspan(2);
        detailsTable.addCell(cell);
        phrase.add(headingTable);
        detailsTable.setKeepTogether(true);
        phrase.add(detailsTable);
        //phrase.add(particularsTable);
        HeaderFooter header = new HeaderFooter(phrase, false);
        header.setBorder(Rectangle.NO_BORDER);
        return header;
    }

    public void createBody(String contextPath, FclBl fclBl, List commentsList,
            String simpleRequest, MessageResources messageResources) throws Exception {
        // PARTICULARS FURNISHED BY SHIPPER
        // PdfPTable particularsTable = fillMarksAndContinerInformation(bl, messageResources,commentsList);
        BaseFont palationRomanBase = BaseFont.createFont(contextPath + "/ttf/Palatino-Roman.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font palatinoRomanSmallFont = new Font(palationRomanBase, 8, Font.NORMAL, Color.BLACK);
        Font palatinoRomanLargeFont = new Font(palationRomanBase, 10, Font.NORMAL, Color.BLACK);
        Font palatinoRomanBigFont = new Font(palationRomanBase, 12, Font.NORMAL, Color.BLACK);

        //bottom table
        List TempList = new ArrayList();
        List chargeListTemp = new ArrayList();
        Set<FclBlCharges> fclChargesSet = fclBl.getFclcharge();
        for (Iterator iter = fclChargesSet.iterator(); iter.hasNext();) {
            FclBlCharges fclBlCharges = (FclBlCharges) iter.next();
            TempList.add(fclBlCharges);
        }
        boolean importFlag = "I".equalsIgnoreCase(fclBl.getImportFlag());
        if ("I".equalsIgnoreCase(fclBl.getImportFlag())) {
            chargeListTemp = fclBlBC.consolidateRates(fclBlBC.getSortedList(TempList), messageResources, importFlag);
        } else {
            String client = "";
            if (fclBl.getBillToCode() != null && fclBl.getBillToCode().equalsIgnoreCase("S")) {
                client = "shipper";
            } else if (fclBl.getBillToCode() != null && fclBl.getBillToCode().equalsIgnoreCase("F")) {
                client = "forwarder";
            } else if (fclBl.getBillToCode() != null && fclBl.getBillToCode().equalsIgnoreCase("T")) {
                client = "thirdParty";
            } else if (fclBl.getBillToCode() != null && fclBl.getBillToCode().equalsIgnoreCase("A")) {
                client = "agent";
            } else if (fclBl.getBillToCode() != null && fclBl.getBillToCode().equalsIgnoreCase("A")) {
                client = "consignee";
            } else if (fclBl.getBillToCode() != null && fclBl.getBillToCode().equalsIgnoreCase("N")) {
                client = "notifyParty";
            } else {
                if (simpleRequest.equalsIgnoreCase(PrintReportsConstants.FREIGHT_INVOICE_AGENT)) {
                    client = "agent";
                } else if (simpleRequest.equalsIgnoreCase(PrintReportsConstants.FREIGHT_INVOICE_FORWARDER)) {
                    client = "forwarder";
                } else if (simpleRequest.equalsIgnoreCase(PrintReportsConstants.FREIGHT_INVOICE_SHIPPER)) {
                    client = "shipper";
                } else if (simpleRequest.equalsIgnoreCase(PrintReportsConstants.FREIGHT_INVOICE_THIRDPARTY)) {
                    client = "thirdParty";
                } else if (simpleRequest.equalsIgnoreCase(PrintReportsConstants.FREIGHT_INVOICE_CONSIGNEE)) {
                    client = "consignee";
                } else if (simpleRequest.equalsIgnoreCase(PrintReportsConstants.FREIGHT_INVOICE_NOTIFYPARTY)) {
                    client = "notifyParty";
                }
            }
            chargeListTemp = new PrintConfigBC().getExportRatesForFclPrintForBounleOfr(fclBlBC.getSortedList(TempList), messageResources, client);
        }
        List chargeList = fclBlBC.getCorrectedList(chargeListTemp);
        List<FclBlCharges> chargesList = new ArrayList<FclBlCharges>();
        String frieghtBill = "";
        if (chargeList.size() > 0) {
            int check = 0;
            String whichList = "";
            List<FclBlCharges> shipperList = new ArrayList<FclBlCharges>();
            List<FclBlCharges> forwarderList = new ArrayList<FclBlCharges>();
            List<FclBlCharges> agentList = new ArrayList<FclBlCharges>();
            List<FclBlCharges> thirdPartyList = new ArrayList<FclBlCharges>();
            List<FclBlCharges> consigneeList = new ArrayList<FclBlCharges>();
            List<FclBlCharges> notifyPartyList = new ArrayList<FclBlCharges>();

            for (Iterator iter = chargeList.iterator(); iter.hasNext();) {
                FclBlCharges fclBlCharges = (FclBlCharges) iter.next();
                if (fclBlCharges != null && fclBlCharges.getBillTo() != null) {
                    String billTo = fclBlCharges.getBillTo();
                    if (billTo.equalsIgnoreCase("Shipper")) {
                        check = check + 1;
                        whichList = "Shipper";
                        shipperList.add(fclBlCharges);
                    } else if (billTo.equalsIgnoreCase("Forwarder")) {
                        check = check + 1;
                        whichList = "Forwarder";
                        forwarderList.add(fclBlCharges);
                    } else if (billTo.equalsIgnoreCase("Agent")) {
                        check = check + 1;
                        whichList = "Agent";
                        agentList.add(fclBlCharges);
                    } else if (billTo.equalsIgnoreCase("ThirdParty")) {
                        check = check + 1;
                        whichList = "ThirdParty";
                        thirdPartyList.add(fclBlCharges);
                    } else if (billTo.equalsIgnoreCase("Consignee")) {
                        check = check + 1;
                        whichList = "Consignee";
                        consigneeList.add(fclBlCharges);
                    } else if (billTo.equalsIgnoreCase("NotifyParty")) {
                        check = check + 1;
                        whichList = "NotifyParty";
                        notifyPartyList.add(fclBlCharges);
                    }
                }
            }
            String whichListToPass = "";
            if ("B-Both".equalsIgnoreCase(fclBl.getHouseBl()) || "B".equalsIgnoreCase(fclBl.getHouseBl())) {
                whichListToPass = "both";
            } else if (fclBl.getBillToCode() != null && fclBl.getBillToCode().equalsIgnoreCase("S")) {
                whichListToPass = "shipper";
            } else if (fclBl.getBillToCode() != null && fclBl.getBillToCode().equalsIgnoreCase("F")) {
                whichListToPass = "forwarder";
            } else if (fclBl.getBillToCode() != null && fclBl.getBillToCode().equalsIgnoreCase("T")) {
                whichListToPass = "thirdParty";
            } else if (fclBl.getBillToCode() != null && fclBl.getBillToCode().equalsIgnoreCase("A")) {
                whichListToPass = "agent";
            } else if (fclBl.getBillToCode() != null && fclBl.getBillToCode().equalsIgnoreCase("C")) {
                whichListToPass = "consignee";
            } else if (fclBl.getBillToCode() != null && fclBl.getBillToCode().equalsIgnoreCase("N")) {
                whichListToPass = "notifyParty";
            } else {
                whichListToPass = "both";
            }
            if (whichListToPass.equalsIgnoreCase("agent")) {
                chargesList = agentList;
                frieghtBill = "agent";
            } else if (whichListToPass.equalsIgnoreCase("forwarder")) {
                chargesList = forwarderList;
            } else if (whichListToPass.equalsIgnoreCase("shipper")) {
                chargesList = shipperList;
            } else if (whichListToPass.equalsIgnoreCase("thirdParty")) {
                chargesList = thirdPartyList;
            } else if (whichListToPass.equalsIgnoreCase("consignee")) {
                chargesList = consigneeList;
            } else if (whichListToPass.equalsIgnoreCase("notifyParty")) {
                chargesList = notifyPartyList;
            } else if (whichListToPass.equalsIgnoreCase("both")) {
                if (simpleRequest.equalsIgnoreCase(PrintReportsConstants.FREIGHT_INVOICE_AGENT)) {
                    frieghtBill = "agent";
                    chargesList = agentList;
                } else if (simpleRequest.equalsIgnoreCase(PrintReportsConstants.FREIGHT_INVOICE_FORWARDER)) {
                    chargesList = forwarderList;
                } else if (simpleRequest.equalsIgnoreCase(PrintReportsConstants.FREIGHT_INVOICE_SHIPPER)) {
                    chargesList = shipperList;
                } else if (simpleRequest.equalsIgnoreCase(PrintReportsConstants.FREIGHT_INVOICE_THIRDPARTY)) {
                    chargesList = thirdPartyList;
                } else if (simpleRequest.equalsIgnoreCase(PrintReportsConstants.FREIGHT_INVOICE_CONSIGNEE)) {
                    chargesList = consigneeList;
                } else if (simpleRequest.equalsIgnoreCase(PrintReportsConstants.FREIGHT_INVOICE_NOTIFYPARTY)) {
                    chargesList = notifyPartyList;
                }

            }
        }
        FclBl fclbl = new FclBlDAO().getOriginalBl(fclBl.getFileNo());
        fillMarksAndContinerInformation(fclbl, messageResources, palatinoRomanSmallFont);
        PdfPTable particularsAesTable = fillAesDetails(fclbl, messageResources, palatinoRomanSmallFont, simpleRequest);
//        particularsAesTable.setExtendLastRow(true);
        document.add(particularsAesTable);
        PdfPTable chargesTable = getChargesTable(chargesList, fclBl, messageResources, palatinoRomanSmallFont, palatinoRomanLargeFont, frieghtBill);
        chargesTable.setTotalWidth(document.getPageSize().getWidth() / 2 - document.leftMargin() - document.rightMargin() + 42);
        chargesTable.writeSelectedRows(0, -1, document.leftMargin(), document.bottomMargin() - 72, pdfWriter.getDirectContent());
    }

    public PdfPTable getBottomTable(FclBl bl, List<FclBlCharges> chargesList, String party, Font palatinoRomanSmallFont, Font palatinoRomanLargeFont, List commentList) throws Exception {
        SystemRulesDAO systemRulesDAO = new SystemRulesDAO();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        PdfPCell cell;
        String companyName = "";
        PdfPTable bottomTable = makeTable(2);
        bottomTable.setWidthPercentage(100);
        bottomTable.setWidths(new float[]{56.5f, 43.5f});
        PdfPTable ratesTable = makeTable(1);
        bottomTable.setWidthPercentage(100);
        Font frtTextFont = new Font(Font.HELVETICA, 10, Font.NORMAL, Color.BLACK);
        Font helvSmallFont = new Font(helv, 7, Font.NORMAL, Color.BLACK);
        //adding rates information
        cell = makeCellLeftNoBorderFclBL("");
        cell.setBorderWidthRight(0.0f);
        cell.setBorderWidthTop(0.6f);
        //To leave Space for Printing Rates in Last Page
        for (int i = 0; i < 25; i++) {
            ratesTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
        }
        cell.addElement(ratesTable);
        bottomTable.addCell(cell);

        //adding Discolusre
        cell = makeCellLeftNoBorderFclBL("");
        cell.setBorderWidthRight(0.0f);
        cell.setBorderWidthTop(0.6f);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthBottom(0.0f);
        PdfPTable Discolusre = makeTable(1);
        StringBuilder disclosure = new StringBuilder();
        StringBuilder disclosure1 = new StringBuilder();
        disclosure.append("          ");
        disclosure.append(FI100 != null ? FI100 : "");
        disclosure.append("\n");
        disclosure.append("");
        disclosure.append("\n");
        disclosure.append("          ");
        disclosure.append(FI101 != null ? FI101 : "");
        disclosure.append("\n");
        disclosure.append("");
        disclosure.append("\n");
//        String companyName = null != LoadLogisoftProperties.getProperty("application.fclBl.print.companyFullName")
//                ? LoadLogisoftProperties.getProperty("application.fclBl.print.companyFullName").toUpperCase() : "";
        String companyCode = new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
        if (bl.getBrand().equals("Econo") && ("03").equals(companyCode)) {
            companyName = LoadLogisoftProperties.getProperty("application.Econo.companyname");

        } else if (bl.getBrand().equals("OTI") && ("02").equals(companyCode)) {
            companyName = LoadLogisoftProperties.getProperty("application.OTI.companyname");

        } else if (bl.getBrand().equalsIgnoreCase("Ecu Worldwide")) {
            companyName = LoadLogisoftProperties.getProperty("application.ECU.companyname");

        }
        disclosure.append("          " + companyName);
        disclosure.append("\n");
        disclosure.append("          " + systemRulesDAO.getSystemRulesByCode("CompanyAddress"));
        disclosure.append("\n");
        disclosure.append("          " + "Phone: " + systemRulesDAO.getSystemRulesByCode("CompanyPhone"));
        disclosure.append("\n");
        disclosure.append("          " + "Fax: " + systemRulesDAO.getSystemRulesByCode("CompanyFax"));
        disclosure.append("\n");
        disclosure.append("");
        disclosure.append("\n");
        disclosure1.append(checkPayment(bl));
//        disclosure1.append(FI102 != null ? FI102 : "");
        disclosure1.append("\n");
        disclosure1.append("");
        disclosure1.append("\n");
        String sailDate = bl.getSailDate() != null ? sdf.format(bl.getSailDate()).toString() : "";
//        disclosure.append("Shipment Sailed On:");
//        disclosure.append(" "+sailDate);
        PdfPCell disclosureCell = makeCellLeftNoBorderPalatinoFclBl(disclosure.toString(), frtTextFont);
        disclosureCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        PdfPCell disclosureCell1 = makeCellLeftNoBorderPalatinoFclBl(disclosure1.toString(), frtTextFont);
        disclosureCell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        Discolusre.setWidthPercentage(100);
        Discolusre.addCell(disclosureCell);
        Discolusre.addCell(disclosureCell1);
        cell.addElement(Discolusre);
        bottomTable.addCell(cell);

        PdfPTable billToPartyTable1 = makeTable(2);
        billToPartyTable1.setWidthPercentage(100);
        billToPartyTable1.setWidths(new float[]{35, 65});
        String billTo = "";
        String billToAcctNo = "";
        if ("forwarder".equals(party)) {
            billTo = "Forwarder:";
            billToAcctNo = bl.getForwardAgentNo() + (CommonUtils.isNotEmpty(bl.getForwardingAgentName()) ? " (" + bl.getForwardingAgentName() + ")" : "");
        } else if ("shipper".equals(party)) {
            billTo = "Shipper:";
            billToAcctNo = bl.getShipperNo() + (CommonUtils.isNotEmpty(bl.getShipperName()) ? " (" + bl.getShipperName() + ")" : "");
        } else if ("thirdParty".equals(party)) {
            billTo = "ThirdParty:";
            billToAcctNo = bl.getBillTrdPrty() + (CommonUtils.isNotEmpty(bl.getThirdPartyName()) ? " (" + bl.getThirdPartyName() + ")" : "");
        } else if ("consignee".equals(party)) {
            billTo = "Consignee:";
            billToAcctNo = bl.getConsigneeNo() + (CommonUtils.isNotEmpty(bl.getConsigneeName()) ? " (" + bl.getConsigneeName() + ")" : "");
        } else if ("notifyParty".equals(party)) {
            billTo = "NotifyParty:";
            billToAcctNo = bl.getNotifyParty() + (CommonUtils.isNotEmpty(bl.getNotifyPartyName()) ? " (" + bl.getNotifyPartyName() + ")" : "");
        } else {
            billTo = "Agent:";
            billToAcctNo = bl.getAgentNo() + (CommonUtils.isNotEmpty(bl.getAgent()) ? " (" + bl.getAgent() + ")" : "");
        }
        cell = makeCellLeftNoBorderPalatinoFclBl(billTo, palatinoRomanSmallFont);
        billToPartyTable1.addCell(cell);
        if (null != bl.getOmit2LetterCountryCode() && "yes".equalsIgnoreCase(bl.getOmit2LetterCountryCode())) {
            String BolNo = getBolNo(bl.getBolId());
            cell = makeCellLeftNoBorderPalatinoFclBl("Bill of Lading No." + BolNo.substring(0, BolNo.indexOf("-") + 1) + BolNo.substring(BolNo.indexOf("-") + 3), palatinoRomanSmallFont);
        } else {
            cell = makeCellLeftNoBorderPalatinoFclBl("Bill of Lading No." + getBolNo(bl.getBolId()), palatinoRomanSmallFont);
        }
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        billToPartyTable1.addCell(cell);
        cell.addElement(billToPartyTable1);
        bottomTable.addCell(cell);
        cell = makeCellLeftNoBorderPalatinoFclBl("          Shipment Sailed On: " + sailDate, frtTextFont);
        cell.setBorderWidthLeft(0.6f);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        bottomTable.addCell(cell);

        //adding bill to party information
        cell = makeCellLeftNoBorderPalatinoFclBl((billToAcctNo.length() > 29 ? billToAcctNo.substring(0, 29) + ")" : billToAcctNo), palatinoRomanSmallFont);
        cell.setNoWrap(true);
        billToPartyTable1.addCell(cell);
        billToPartyTable1.addCell(makeCellRightNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
        cell = makeCellLeftNoBorderFclBL("");
        cell.setBorderWidthRight(0.0f);
        cell.setBorderWidthTop(0.6f);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthBottom(0.6f);
        PdfPTable billToPartyTable = makeTable(2);
        billToPartyTable.setWidthPercentage(100);
        billToPartyTable.setWidths(new float[]{35, 65});
        billToPartyTable.addCell(makeCellLeftNoBorderFclBL("BILL TO PARTY:"));
//        if(bl.getBillToCode()!=null && bl.getBillToCode().equalsIgnoreCase("S")){
//        	party = "shipper";
//        }else  if(bl.getBillToCode()!=null && bl.getBillToCode().equalsIgnoreCase("F")){
//        	party = "forwarder";
//        }else  if(bl.getBillToCode()!=null && bl.getBillToCode().equalsIgnoreCase("T")){
//        	party = "thirdParty";
//        }else  if(bl.getBillToCode()!=null && bl.getBillToCode().equalsIgnoreCase("A")){
//        	party = "agent";
//        }else  if(bl.getBillToCode()!=null && bl.getBillToCode().equalsIgnoreCase("C")){
//        	party = "consignee";
//        }else  if(bl.getBillToCode()!=null && bl.getBillToCode().equalsIgnoreCase("N")){
//        	party = "notifyParty";
//        }
        if (party != null && party.equalsIgnoreCase("shipper")) {
            PdfPCell subCell;
            StringBuilder shipperDetails = new StringBuilder();
            shipperDetails.append(bl.getShipperName() != null ? bl.getShipperName() : "");
            shipperDetails.append("\n");
            shipperDetails.append(bl.getShipperAddress());
            subCell = makeCellLeftNoBorderFclBL(shipperDetails.toString());
            subCell.setMinimumHeight(50);
            billToPartyTable.addCell(subCell);
        } else if (party != null && party.equalsIgnoreCase("forwarder")) {
            PdfPCell subCell;
            StringBuilder forwardingAgentDetails = new StringBuilder();
            if (null != bl.getForwardingAgentName() && !bl.getForwardingAgentName().trim().equalsIgnoreCase("NO FF ASSIGNED".trim())
                    && !bl.getForwardingAgentName().trim().equalsIgnoreCase("NO FF ASSIGNED / B/L PROVIDED".trim())) {
                String forwardingAgent = null != bl.getForwardingAgent() ? bl.getForwardingAgent().trim() : "";
                forwardingAgent = trimShipperConsigNotifyForwardAddress(forwardingAgent);
                if (bl.getBolId().indexOf("==") != -1 && CommonUtils.isEmpty(bl.getForwardingAgent())) {
                    forwardingAgent = trimShipperConsigNotifyForwardAddress(getAddress(bl.getForwardAgentNo()));
                }
                forwardingAgentDetails.append(bl.getForwardingAgentName() != null ? bl.getForwardingAgentName() : "");
                forwardingAgentDetails.append("\n");
                forwardingAgentDetails.append(forwardingAgent);
            } else {
                forwardingAgentDetails.append("");
            }
            subCell = makeCellLeftNoBorderFclBL(forwardingAgentDetails.toString());
            subCell.setMinimumHeight(50);
            billToPartyTable.addCell(subCell);
        } else if (party != null && party.equalsIgnoreCase("agent")) {
            PdfPCell subCell;
            StringBuilder agentDetails = new StringBuilder();
            agentDetails.append(bl.getAgent() != null ? bl.getAgent() : "");
            agentDetails.append("\n");
            agentDetails.append(getAddress(bl.getAgentNo()));
            subCell = makeCellLeftNoBorderFclBL(agentDetails.toString());
            subCell.setMinimumHeight(50);
            billToPartyTable.addCell(subCell);
        } else if (party != null && party.equalsIgnoreCase("thirdParty")) {
            PdfPCell subCell;
            StringBuilder thirdPartydetails = new StringBuilder();
            thirdPartydetails.append(bl.getThirdPartyName() != null ? bl.getThirdPartyName() : "");
            thirdPartydetails.append("\n");
            thirdPartydetails.append(getAddress(bl.getBillTrdPrty()));
            subCell = makeCellLeftNoBorderFclBL(thirdPartydetails.toString());
            subCell.setMinimumHeight(50);
            billToPartyTable.addCell(subCell);
        } else if (party != null && party.equalsIgnoreCase("consignee")) {
            PdfPCell subCell;
            StringBuilder consigneedetails = new StringBuilder();
            consigneedetails.append(bl.getConsigneeName() != null ? bl.getConsigneeName() : "");
            consigneedetails.append("\n");
            consigneedetails.append(getAddress(bl.getConsigneeNo()));
            subCell = makeCellLeftNoBorderFclBL(consigneedetails.toString());
            subCell.setMinimumHeight(50);
            billToPartyTable.addCell(subCell);
        } else if (party != null && party.equalsIgnoreCase("notifyParty")) {
            PdfPCell subCell;
            StringBuilder notifyPartydetails = new StringBuilder();
            notifyPartydetails.append(bl.getNotifyPartyName() != null ? bl.getNotifyPartyName() : "");
            notifyPartydetails.append("\n");
            notifyPartydetails.append(getAddress(bl.getNotifyParty()));
            subCell = makeCellLeftNoBorderFclBL(notifyPartydetails.toString());
            subCell.setMinimumHeight(50);
            billToPartyTable.addCell(subCell);
        }
        cell.addElement(billToPartyTable);
        bottomTable.addCell(cell);

        cell = makeCellLeftNoBorderFclBL("");
        cell.setBorderWidthRight(0.0f);
        cell.setBorderWidthLeft(0.6f);
        PdfPTable tempTable = makeTable(1);
        StringBuilder builder = new StringBuilder();
        if (null != bl.getAgentsForCarrier() && bl.getAgentsForCarrier().equalsIgnoreCase("Yes")) {
            builder.append("As Agents for the Carrier");
            builder.append("\n");
            builder.append(bl.getSslineName());
            builder.append("\n");
        } else if (null != bl.getAgentsForCarrier() && bl.getAgentsForCarrier().equalsIgnoreCase("A")) {
            builder.append("As Carrier");
            builder.append("\n");
            builder.append(bl.getSslineName());
            builder.append("\n");
        }
        PdfPCell cell1 = makeCellLeftNoBorderPalatinoFclBl(builder.toString(), palatinoRomanLargeFont);
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        tempTable.addCell(cell1);
        cell.addElement(tempTable);
        bottomTable.addCell(cell);
        //empty table

        cell1 = makeCellLeftNoBorderPalatinoFclBl(DateUtils.formatDate(new Date(), "MM/dd/yyyy hh:mm"), palatinoRomanSmallFont);
        cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
        bottomTable.addCell(cell1);
        return bottomTable;
    }

    public PdfPTable getChargesTable(List<FclBlCharges> chargesList, FclBl bl, MessageResources messageResources, Font palatinoRomanSmallFont, Font palatinoRomanLargeFont, String billTo) throws DocumentException {
        PdfPCell cell;
        NumberFormat numberFormat = new DecimalFormat("###,###,##0.00");
        PdfPTable ratesTable = makeTable(2);
        ratesTable.setWidthPercentage(100);
        ratesTable.setWidths(new float[]{49, 51});
        double total = 0.00;

        PdfPTable ratesSubTable = makeTable(3);
        ratesSubTable.setWidths(new float[]{60, 13, 27});
        ratesSubTable.setWidthPercentage(100);
        PdfPTable ratesSubTable1 = makeTable(3);
        ratesSubTable1.setWidths(new float[]{60, 13, 27});
        ratesSubTable1.setWidthPercentage(100);
        int subTableSize = 0;
        for (Iterator iter = chargesList.iterator(); iter.hasNext();) {
            FclBlCharges fclBlCharges = (FclBlCharges) iter.next();
//            if(fclBlCharges.getPrintOnBl()!=null && !fclBlCharges.getPrintOnBl().equalsIgnoreCase("no")){
            subTableSize++;
            if (subTableSize < 7) {
                cell = makeCellLeftNoBorderPalatinoFclBl(fclBlCharges.getCharges() != null ? fclBlCharges.getCharges() : "", palatinoRomanSmallFont);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setNoWrap(true);
                ratesSubTable.addCell(cell);
                cell = makeCellLeftNoBorderPalatinoFclBl("US$", palatinoRomanSmallFont);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setNoWrap(true);
                ratesSubTable.addCell(cell);
                cell = makeCellLeftNoBorderPalatinoFclBl(fclBlCharges.getAmount() != null ? numberFormat.format(fclBlCharges.getAmount()).toString()
                        : "0.00", palatinoRomanSmallFont);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setNoWrap(true);
                ratesSubTable.addCell(cell);
            } else {
                cell = makeCellLeftNoBorderPalatinoFclBl(fclBlCharges.getCharges() != null ? fclBlCharges.getCharges() : "", palatinoRomanSmallFont);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setNoWrap(true);
                ratesSubTable1.addCell(cell);
                cell = makeCellLeftNoBorderPalatinoFclBl("US$", palatinoRomanSmallFont);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setNoWrap(true);
                ratesSubTable1.addCell(cell);
                cell = makeCellLeftNoBorderPalatinoFclBl(fclBlCharges.getAmount() != null ? numberFormat.format(fclBlCharges.getAmount()).toString()
                        : "0.00", palatinoRomanSmallFont);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setNoWrap(true);
                ratesSubTable1.addCell(cell);
            }
            total = total + (fclBlCharges.getAmount() != null ? fclBlCharges.getAmount() : 0.00);
//            }
        }
        ratesTable.addCell(ratesSubTable);
        ratesTable.addCell(ratesSubTable1);
//      ---------------------charges-----^

        StringBuilder collectString = new StringBuilder();
        if (bl.getHouseBl() != null && (bl.getHouseBl().equalsIgnoreCase("P-Prepaid") || bl.getHouseBl().equalsIgnoreCase("P"))) {
            if ("Yes".equalsIgnoreCase(bl.getCollectThirdParty())) {
                collectString.append("COLLECT TOTAL US $ ");
            } else {
                collectString.append("PREPAID TOTAL US $ ");
            }
        } else if (bl.getHouseBl() != null && (bl.getHouseBl().equalsIgnoreCase("C-Collect") || bl.getHouseBl().equalsIgnoreCase("C"))) {
            collectString.append("COLLECT TOTAL US $ ");
        } else if (bl.getHouseBl() != null && (bl.getHouseBl().equalsIgnoreCase("B-Both") || bl.getHouseBl().equalsIgnoreCase("B"))) {
            if ("agent".equalsIgnoreCase(billTo)) {
                collectString.append("COLLECT TOTAL US $ ");
            } else {
                collectString.append("PREPAID TOTAL US $ ");
            }
        } else {
            collectString.append("TOTAL US $ ");
        }
        collectString.append("$").append(numberFormat.format(total).toString());
        if (subTableSize < 7) {
            cell = makeCellLeftNoBorderFclBL(collectString.toString());
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setColspan(2);
            float paddingTop = 60;
            for (int i = 1; i < 6; i++) {
                if (i == subTableSize) {
                    cell.setPaddingTop(paddingTop);
                } else {
                    paddingTop = paddingTop - 10;
                }
            }
            ratesTable.addCell(cell);
        } else {
            cell = makeCellLeftNoBorderFclBL(collectString.toString());
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setColspan(2);
            ratesTable.addCell(cell);
        }

        //---------------------total-----^
        String totalString = numberFormat.format(total).replaceAll(",", "");
        int index = totalString.indexOf(".");
        String beforeDecimal = totalString.substring(0, index);
        String afterDecimal = totalString.substring(index + 1, totalString.length());
        StringBuilder convertedTotal = new StringBuilder();
        convertedTotal.append(ConvertNumberToWords.convert(Integer.parseInt(beforeDecimal)));
        convertedTotal.append(" DOLLARS AND ");
        convertedTotal.append(ConvertNumberToWords.convert(Integer.parseInt(afterDecimal)));
        convertedTotal.append(" CENTS ");
        cell = makeCellLeftNoBorderPalatinoFclBl(convertedTotal.toString(), palatinoRomanSmallFont);
        cell.setColspan(2);
        ratesTable.addCell(cell);
        //------------------------total displayinig words--------------------------------------------

        return ratesTable;

    }

    public PdfPTable fillMarksAndContinerInformation(FclBl bl, MessageResources messageResources, Font palatinoRomanSmallFont)
            throws DocumentException, Exception {
        PdfPCell cell = null;
        PdfPTable particularsTable = makeTable(5);
        NumberFormat numberFormat = new DecimalFormat("###,###,##0.000");
        Set<FclBlContainer> containerSet = bl.getFclcontainer();
        HashMap hashMap = new HashMap();
        List TempList = new ArrayList();
        List containerList = new ArrayList();
        for (Iterator iter = containerSet.iterator(); iter.hasNext();) {
            FclBlContainer fclBlCont = (FclBlContainer) iter.next();
            if (!"D".equalsIgnoreCase(fclBlCont.getDisabledFlag())) {
                hashMap.put(fclBlCont.getTrailerNoId(), fclBlCont);
                TempList.add(fclBlCont.getTrailerNoId());
            }
        }
        Collections.sort(TempList);
        for (int i = 0; i < TempList.size(); i++) {
            FclBlContainer fclBlCont = (FclBlContainer) hashMap.get(TempList.get(i));
            containerList.add(fclBlCont);
        }
        int count = 0;
        for (Iterator iter = containerList.iterator(); iter.hasNext();) {
            count++;
            boolean set = false;
            particularsTable = makeTable(5);
            particularsTable.setWidths(new float[]{18, 10, 44, 14, 14});
            particularsTable.setWidthPercentage(100);
            FclBlContainer fclBlContainer = (FclBlContainer) iter.next();
            StringBuilder tempSizeLegened = new StringBuilder();
            String sizeLegend = fclBlContainer.getSizeLegend() != null ? (fclBlContainer.getSizeLegend().
                    getCodedesc() != null ? fclBlContainer.getSizeLegend().getCodedesc() : "") : "";
            int index = sizeLegend.indexOf("=");
            if (index != -1) {
                tempSizeLegened.append("1X");
                String tempSize = sizeLegend.substring(index + 1, sizeLegend.length());
                if (tempSize.equalsIgnoreCase(messageResources.getMessage("container40HC"))) {
                    tempSize = "40" + "'" + "HC";
                } else if (tempSize.equalsIgnoreCase(messageResources.getMessage("container40NOR"))) {
                    tempSize = "40" + "'" + "NOR";
                } else {
                    tempSize = tempSize + "'";
                }
                tempSizeLegened.append(tempSize);
            } else {
                tempSizeLegened.append("");
            }
            List<String> hazmatList = new ArrayList<String>();
            StringBuilder marksNumber = new StringBuilder();
            if (CommonUtils.isNotEmpty(fclBlContainer.getTrailerNo())) {
                List hazmatMaterialList = fclBlBC.getHazmatForBlPrint(fclBlContainer.getTrailerNoId(), FclBlConstants.FCLBL);
                hazmatList = new HazmatBC().getHazmatDetails(hazmatMaterialList);
            }
            if (null != bl.getPrintContainersOnBL() && bl.getPrintContainersOnBL().equalsIgnoreCase("Yes")) {
                marksNumber.append(fclBlContainer.getTrailerNo() != null ? fclBlContainer.getTrailerNo() : "");
                marksNumber.append("\n");
                marksNumber.append("SEAL: ");
                marksNumber.append(" " + fclBlContainer.getSealNo() != null ? fclBlContainer.getSealNo() : "");
            }
            List marksList = helperClass.splitDescrption(helperClass.wrapAddress(fclBlContainer.getMarks()), DESC_SIZE + 1);
            if (!marksList.isEmpty()) {
                marksNumber.append("\n");
                marksNumber.append(marksList.get(0).toString());
                marksList.remove(0);
            }
            particularsTable.addCell(makeCellLeftRightBorderPalatinoFclBl(marksNumber.toString(), palatinoRomanSmallFont));
            if (null != bl.getNoOfPackages() && bl.getNoOfPackages().equalsIgnoreCase("Yes")) {
                if (CommonUtils.isNotEmpty(fclBlContainer.getSpecialEquipment())) {
                    tempSizeLegened.append("\n");
                    tempSizeLegened.append(fclBlContainer.getSpecialEquipment());
                }
                particularsTable.addCell(makeCellLeftRightBorderPalatinoFclBl(tempSizeLegened.toString(), palatinoRomanSmallFont));
            } else if (null != bl.getNoOfPackages() && bl.getNoOfPackages().equalsIgnoreCase("Alt")) {
                particularsTable.addCell(makeCellLeftRightBorderPalatinoFclBl(bl.getAlternateNoOfPackages().toUpperCase(), palatinoRomanSmallFont));
            } else {
                particularsTable.addCell(makeCellLeftRightBorderPalatinoFclBl("", palatinoRomanSmallFont));
            }

            List<FclBlMarks> fclMarksList = new ArrayList();
            FclBlContainerDAO fclBlContainerDAO = new FclBlContainerDAO();
            fclMarksList = fclBlContainerDAO.getPakagesDetails(fclBlContainer.getTrailerNoId());
            if (fclMarksList != null && !fclMarksList.isEmpty()) {
                fclBlContainer.setFclMarksList(fclMarksList);
                List arrayList = fclBlContainer.getFclMarksList();
                if (arrayList.isEmpty()) {
                    particularsTable.addCell(makeCellLeftWithRightBorderBold(""));
                    particularsTable.addCell(makeCellLeftWithRightBorderBold(""));
                    particularsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
                    document.add(particularsTable);
                    particularsTable = makeTable(5);
                    particularsTable.setWidths(new float[]{18, 10, 44, 14, 14});
                    particularsTable.setWidthPercentage(100);
                } else {
                    for (Iterator iterator1 = arrayList.iterator(); iterator1.hasNext();) {
                        FclBlMarks fclBlmarks = (FclBlMarks) iterator1.next();
                        List l = helperClass.splitDescrption(helperClass.wrapAddress(fclBlmarks.getDescPckgs()), DESC_SIZE);
                        if (set) {
                            if (!marksList.isEmpty()) {
                                document.newPage();
                                particularsTable = makeTable(5);
                                particularsTable.setWidths(new float[]{18, 10, 44, 14, 14});
                                particularsTable.setWidthPercentage(100);
                                particularsTable.addCell(makeCellLeftWithRightBorderBold(marksList.get(0).toString()));
                                particularsTable.addCell(makeCellLeftWithRightBorderBold(""));
                                marksList.remove(0);
                            } else {
                                particularsTable.addCell(makeCellLeftWithRightBorderBold(""));
                                particularsTable.addCell(makeCellLeftWithRightBorderBold(""));
                            }
                        }
                        for (int i = 0; i < l.size(); i++) {
                            if (i == 0) {
                                StringBuilder stcAndPackages = new StringBuilder();
                                if (null != bl.getTotalContainers() && bl.getTotalContainers().equalsIgnoreCase("Yes")) {
                                    stcAndPackages.append("STC: ");
                                    stcAndPackages.append(null != fclBlmarks.getNoOfPkgs() && fclBlmarks.getNoOfPkgs() != 0 ? fclBlmarks.getNoOfPkgs() : "");
                                    stcAndPackages.append(" ");
                                    stcAndPackages.append(null != fclBlmarks.getUom() ? fclBlmarks.getUom() : "");
                                    stcAndPackages.append("\n");
                                }
                                stcAndPackages.append(l.get(i) != null ? l.get(i).toString() : "");
                                particularsTable.addCell(makeCellLeftRightBorderPalatinoFclBl(stcAndPackages.toString(), palatinoRomanSmallFont));

                                PdfPTable lbsTable = makeTable(2);
                                lbsTable.setWidths(new float[]{50, 50});
                                lbsTable.setWidthPercentage(100);

                                double netWeightLBS = fclBlmarks.getNetweightLbs() != null ? fclBlmarks.getNetweightLbs()
                                        : 0.00;
                                StringBuilder lbs = new StringBuilder();
                                double measureKgs = fclBlmarks.getNetweightKgs() != null ? fclBlmarks.getNetweightKgs() : 0.00;
                                if (measureKgs != 0.00) {
                                    if ("yes".equalsIgnoreCase(bl.getTrimTrailingZerosForQty())) {
                                        cell = makeCellLeftNoBorderPalatinoFclBl(CommonUtils.trimTrailingZeros("" + numberFormat.format(measureKgs)), palatinoRomanSmallFont);
                                    } else {
                                        cell = makeCellLeftNoBorderPalatinoFclBl(numberFormat.format(measureKgs).toString(), palatinoRomanSmallFont);
                                    }
                                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                    cell.setNoWrap(true);
                                    lbsTable.addCell(cell);
                                    cell = makeCellLeftNoBorderPalatinoFclBl("   KGS", palatinoRomanSmallFont);
                                    lbsTable.addCell(cell);
                                } else {
                                    lbsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
                                    lbsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
                                }
                                if (netWeightLBS != 0.00) {
                                    if ("yes".equalsIgnoreCase(bl.getTrimTrailingZerosForQty())) {
                                        cell = makeCellLeftNoBorderPalatinoFclBl(CommonUtils.trimTrailingZeros("" + numberFormat.format(netWeightLBS)), palatinoRomanSmallFont);
                                    } else {
                                        cell = makeCellLeftNoBorderPalatinoFclBl(numberFormat.format(netWeightLBS).toString(), palatinoRomanSmallFont);
                                    }
                                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                    cell.setNoWrap(true);
                                    lbsTable.addCell(cell);
                                    cell = makeCellLeftNoBorderPalatinoFclBl("   LBS", palatinoRomanSmallFont);
                                    lbsTable.addCell(cell);
                                } else {
                                    lbsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
                                    lbsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
                                }
                                cell = makeCellLeftWithRightBorder("");
                                cell.addElement(lbsTable);
                                particularsTable.addCell(cell);

                                PdfPTable cftTable = makeTable(2);
                                cftTable.setWidths(new float[]{50, 50});
                                cftTable.setWidthPercentage(100);

                                double measureCFT = fclBlmarks.getMeasureCft() != null ? fclBlmarks.getMeasureCft()
                                        : 0.00;
                                StringBuilder cft = new StringBuilder();
                                double measureCbm = fclBlmarks.getMeasureCbm() != null ? fclBlmarks.getMeasureCbm()
                                        : 0.00;
                                if (measureCbm != 0.00) {
                                    if ("yes".equalsIgnoreCase(bl.getTrimTrailingZerosForQty())) {
                                        cell = makeCellLeftNoBorderPalatinoFclBl(CommonUtils.trimTrailingZeros("" + numberFormat.format(measureCbm)), palatinoRomanSmallFont);
                                    } else {
                                        cell = makeCellLeftNoBorderPalatinoFclBl(numberFormat.format(measureCbm).toString(), palatinoRomanSmallFont);
                                    }
                                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                    cell.setNoWrap(true);
                                    cftTable.addCell(cell);
                                    cftTable.addCell(makeCellLeftNoBorderPalatinoFclBl("   CBM", palatinoRomanSmallFont));

                                } else {
                                    cftTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
                                    cftTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
                                }
                                if (measureCFT != 0.00) {
                                    if ("yes".equalsIgnoreCase(bl.getTrimTrailingZerosForQty())) {
                                        cell = makeCellLeftNoBorderPalatinoFclBl(CommonUtils.trimTrailingZeros("" + numberFormat.format(measureCFT)), palatinoRomanSmallFont);
                                    } else {
                                        cell = makeCellLeftNoBorderPalatinoFclBl(numberFormat.format(measureCFT).toString(), palatinoRomanSmallFont);
                                    }
                                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                    cell.setNoWrap(true);
                                    cftTable.addCell(cell);
                                    cftTable.addCell(makeCellLeftNoBorderPalatinoFclBl("   CFT", palatinoRomanSmallFont));
                                } else {
                                    cftTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
                                    cftTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
                                }
                                particularsTable.addCell(cftTable);
                                document.add(particularsTable);
                                particularsTable = makeTable(5);
                                particularsTable.setWidths(new float[]{18, 10, 44, 14, 14});
                                particularsTable.setWidthPercentage(100);
                            } else {
                                document.newPage();
                                if (!marksList.isEmpty()) {
                                    particularsTable.addCell(makeCellLeftWithRightBorderBold(marksList.get(0).toString()));
                                    marksList.remove(0);
                                } else {
                                    particularsTable.addCell(makeCellLeftWithRightBorderBold(""));
                                }
                                particularsTable.addCell(makeCellLeftWithRightBorderBold(""));
                                particularsTable.addCell(makeCellLeftRightBorderPalatinoFclBl(l.get(i) != null ? l.get(i).toString() : "", palatinoRomanSmallFont));
                                particularsTable.addCell(makeCellLeftWithRightBorderBold(""));
                                particularsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
                                document.add(particularsTable);
                                particularsTable = makeTable(5);
                                particularsTable.setWidths(new float[]{18, 10, 44, 14, 14});
                                particularsTable.setWidthPercentage(100);
                            }
                        }
                        set = true;
                    }
                    if (!marksList.isEmpty()) {
                        document.newPage();
                        particularsTable = makeTable(5);
                        particularsTable.setWidths(new float[]{18, 10, 44, 14, 14});
                        particularsTable.setWidthPercentage(100);
                        particularsTable.addCell(makeCellLeftWithRightBorderBold(marksList.get(0).toString()));
                        particularsTable.addCell(makeCellLeftWithRightBorderBold(""));
                        particularsTable.addCell(makeCellLeftWithRightBorderBold(""));
                        particularsTable.addCell(makeCellLeftWithRightBorderBold(""));
                        particularsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
                        document.add(particularsTable);
                        marksList.remove(0);
                    }
                    if (!marksList.isEmpty()) {
                        document.newPage();
                        particularsTable = makeTable(5);
                        particularsTable.setWidths(new float[]{18, 10, 44, 14, 14});
                        particularsTable.setWidthPercentage(100);
                        particularsTable.addCell(makeCellLeftWithRightBorderBold(marksList.get(0).toString()));
                        particularsTable.addCell(makeCellLeftWithRightBorderBold(""));
                        particularsTable.addCell(makeCellLeftWithRightBorderBold(""));
                        particularsTable.addCell(makeCellLeftWithRightBorderBold(""));
                        particularsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
                        document.add(particularsTable);
                        marksList.remove(0);
                    }
                    if (!marksList.isEmpty()) {
                        document.newPage();
                        particularsTable = makeTable(5);
                        particularsTable.setWidths(new float[]{18, 10, 44, 14, 14});
                        particularsTable.setWidthPercentage(100);
                        particularsTable.addCell(makeCellLeftWithRightBorderBold(marksList.get(0).toString()));
                        particularsTable.addCell(makeCellLeftWithRightBorderBold(""));
                        particularsTable.addCell(makeCellLeftWithRightBorderBold(""));
                        particularsTable.addCell(makeCellLeftWithRightBorderBold(""));
                        particularsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
                        document.add(particularsTable);
                        marksList.remove(0);
                    }
                    if (!marksList.isEmpty()) {
                        document.newPage();
                        particularsTable = makeTable(5);
                        particularsTable.setWidths(new float[]{18, 10, 44, 14, 14});
                        particularsTable.setWidthPercentage(100);
                        particularsTable.addCell(makeCellLeftWithRightBorderBold(marksList.get(0).toString()));
                        particularsTable.addCell(makeCellLeftWithRightBorderBold(""));
                        particularsTable.addCell(makeCellLeftWithRightBorderBold(""));
                        particularsTable.addCell(makeCellLeftWithRightBorderBold(""));
                        particularsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
                        document.add(particularsTable);
                        marksList.remove(0);
                    }
                    if (CommonUtils.isNotEmpty(hazmatList)) {
                        for (int i = 0; i < hazmatList.size(); i++) {
                            StringBuilder hazmatBuild = new StringBuilder();
                            particularsTable = makeTable(5);
                            particularsTable.setWidths(new float[]{18, 10, 44, 14, 14});
                            particularsTable.setWidthPercentage(100);
                            hazmatBuild.append("\n");
                            hazmatBuild.append(CommonUtils.splitString((String) hazmatList.get(i), 50));
                            particularsTable.addCell(makeCellLeftRightBorderPalatinoFclBl("", palatinoRomanSmallFont));
                            particularsTable.addCell(makeCellLeftRightBorderPalatinoFclBl("", palatinoRomanSmallFont));
                            particularsTable.addCell(makeCellLeftRightBorderPalatinoFclBl(hazmatBuild.toString(), palatinoRomanSmallFont));
                            particularsTable.addCell(makeCellLeftRightBorderPalatinoFclBl("", palatinoRomanSmallFont));
                            particularsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
                            document.add(particularsTable);
                        }
                    }
                }
            }

        }
        return particularsTable;
    }

    public PdfPTable fillAesDetails(FclBl bl, MessageResources messageResources, Font palatinoRomanSmallFont, String simpleRequest)
            throws DocumentException, Exception {
        PdfPTable particularsTable = makeTable(5);
        particularsTable.setWidths(new float[]{18, 10, 44, 14, 14});
        particularsTable.setWidthPercentage(100);
        List inbondList = new FclInbondDetailsDAO().findByProperty("fileNumber", bl.getFileNo());
        List aesList = new FclInbondDetailsDAO().findAesdetails("fileNo", bl.getFileNo());
        if (!aesList.isEmpty()) {
            int count = 0;
            boolean printAes = false;
            StringBuilder aes = new StringBuilder("");
            for (Iterator it = aesList.iterator(); it.hasNext();) {
                count++;
                FclAESDetails aesDet = (FclAESDetails) it.next();
                if (CommonUtils.isNotEmpty(aesDet.getAesDetails())) {
                    if (count == 1) {
                        aes.append("AES ITN: " + aesDet.getAesDetails());
                        printAes = false;
                    } else {
                        aes.append(",AES ITN: " + aesDet.getAesDetails());
                        count = 0;
                        printAes = true;
                    }
                } else if (CommonUtils.isNotEmpty(aesDet.getException())) {
                    aes.append("AES: " + aesDet.getException());
                    printAes = true;
                    count = 0;
                }
                if (printAes) {
                    particularsTable.addCell(makeCellLeftRightBorderPalatinoFclBl("", palatinoRomanSmallFont));
                    particularsTable.addCell(makeCellLeftRightBorderPalatinoFclBl("", palatinoRomanSmallFont));
                    PdfPCell aesDeta = makeCellLeftRightBorderPalatinoFclBl(aes.toString(), palatinoRomanSmallFont);
                    particularsTable.addCell(aesDeta);
                    particularsTable.addCell(makeCellLeftRightBorderPalatinoFclBl("", palatinoRomanSmallFont));
                    particularsTable.addCell(makeCellLeftRightBorderPalatinoFclBl("", palatinoRomanSmallFont));
                    aes = new StringBuilder("");
                }
            }
            if (count == 1) {
                particularsTable.addCell(makeCellLeftRightBorderPalatinoFclBl("", palatinoRomanSmallFont));
                particularsTable.addCell(makeCellLeftRightBorderPalatinoFclBl("", palatinoRomanSmallFont));
                PdfPCell aesDeta = makeCellLeftRightBorderPalatinoFclBl(aes.toString(), palatinoRomanSmallFont);
                particularsTable.addCell(aesDeta);
                particularsTable.addCell(makeCellLeftRightBorderPalatinoFclBl("", palatinoRomanSmallFont));
                particularsTable.addCell(makeCellLeftRightBorderPalatinoFclBl("", palatinoRomanSmallFont));
            }
        }
        if (null != bl.getFclInbondDetails()) {
            Set inbondDetails = bl.getFclInbondDetails();
            String heading = "INBOND: ";
            for (Iterator iterator = inbondList.iterator(); iterator.hasNext();) {
                FclInbondDetails inbondDet = (FclInbondDetails) iterator.next();
                StringBuilder inbondDetailsBuilder = new StringBuilder();
                inbondDetailsBuilder.append((CommonFunctions.isNotNull(inbondDet.getInbondType())) ? (heading + inbondDet.getInbondType() + " " + inbondDet.getInbondNumber()) : (heading + inbondDet.getInbondNumber()));
                particularsTable.addCell(makeCellLeftRightBorderPalatinoFclBl("", palatinoRomanSmallFont));
                particularsTable.addCell(makeCellLeftRightBorderPalatinoFclBl("", palatinoRomanSmallFont));
                particularsTable.addCell(makeCellLeftRightBorderPalatinoFclBl("" + inbondDetailsBuilder.toString(), palatinoRomanSmallFont));
                particularsTable.addCell(makeCellLeftRightBorderPalatinoFclBl("", palatinoRomanSmallFont));
                particularsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
            }
        }
        return particularsTable;
    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        PdfPCell cell;
        PdfPTable particularsTable = makeTable(5);
        try {
            particularsTable.setWidths(new float[]{18, 10, 44, 14, 14});
            particularsTable.setWidthPercentage(100);
            cell = makeCellLeftNoBorderFclBL("PARTICULARS FURNISHED BY SHIPPER");
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(5);
            cell.setBorderWidthBottom(0.6f);
            particularsTable.addCell(cell);
            cell = makeCellLeftNoBorderFclBL("MARKS AND NUMBERS");
            cell.setBorderWidthRight(0.6f);
            cell.setBorderWidthBottom(0.6f);
            particularsTable.addCell(cell);
            cell = makeCellLeftNoBorderFclBL("NO.OF PKGS");
            cell.setBorderWidthRight(0.6f);
            cell.setBorderWidthBottom(0.6f);
            particularsTable.addCell(cell);
            cell = makeCellLeftNoBorderFclBL("DESCRIPTION OF PACKAGES AND GOODS");
            cell.setBorderWidthRight(0.6f);
            cell.setBorderWidthBottom(0.6f);
            particularsTable.addCell(cell);
            cell = makeCellLeftNoBorderFclBL("GROSS WEIGHT");
            cell.setBorderWidthRight(0.6f);
            cell.setBorderWidthBottom(0.6f);
            particularsTable.addCell(cell);
            cell = makeCellLeftNoBorderFclBL("VOLUME");
            cell.setBorderWidthBottom(0.6f);
            particularsTable.addCell(cell);
            document.add(particularsTable);
        } catch (DocumentException e) {
            log.info("onStartPage failed on " + new Date(), e);
            throw new ExceptionConverter(e);
        }
    }

    private PdfPTable getMyFooter() throws Exception {
        PdfPCell cell;
        PdfPTable footerTable = makeTable(1);
        PdfPTable bottomTable = makeTable(1);
        PdfPTable particularsFurnishedTable = makeTable(5);
        particularsFurnishedTable.setWidths(new float[]{18, 10, 44, 14, 14});
        particularsFurnishedTable.setWidthPercentage(100);
        BaseFont palationRomanBase = BaseFont.createFont(context + "/ttf/Palatino-Roman.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font palatinoRomanSmallFont = new Font(palationRomanBase, 8, Font.NORMAL, Color.BLACK);
        Font palatinoRomanLargeFont = new Font(palationRomanBase, 10, Font.NORMAL, Color.BLACK);

        Font drawBotTextFont = new Font(Font.HELVETICA, 8, Font.NORMAL, Color.BLACK);
        String client = "";
        if (fclBl.getBillToCode() != null && fclBl.getBillToCode().equalsIgnoreCase("S")) {
            client = "shipper";
        } else if (fclBl.getBillToCode() != null && fclBl.getBillToCode().equalsIgnoreCase("F")) {
            client = "forwarder";
        } else if (fclBl.getBillToCode() != null && fclBl.getBillToCode().equalsIgnoreCase("T")) {
            client = "thirdParty";
        } else if (fclBl.getBillToCode() != null && fclBl.getBillToCode().equalsIgnoreCase("A")) {
            client = "agent";
        } else if (fclBl.getBillToCode() != null && fclBl.getBillToCode().equalsIgnoreCase("A")) {
            client = "consignee";
        } else if (fclBl.getBillToCode() != null && fclBl.getBillToCode().equalsIgnoreCase("N")) {
            client = "notifyParty";
        } else {
            if (simpleRequest.equalsIgnoreCase(PrintReportsConstants.FREIGHT_INVOICE_AGENT)) {
                client = "agent";
            } else if (simpleRequest.equalsIgnoreCase(PrintReportsConstants.FREIGHT_INVOICE_FORWARDER)) {
                client = "forwarder";
            } else if (simpleRequest.equalsIgnoreCase(PrintReportsConstants.FREIGHT_INVOICE_SHIPPER)) {
                client = "shipper";
            } else if (simpleRequest.equalsIgnoreCase(PrintReportsConstants.FREIGHT_INVOICE_THIRDPARTY)) {
                client = "thirdParty";
            } else if (simpleRequest.equalsIgnoreCase(PrintReportsConstants.FREIGHT_INVOICE_CONSIGNEE)) {
                client = "consignee";
            } else if (simpleRequest.equalsIgnoreCase(PrintReportsConstants.FREIGHT_INVOICE_NOTIFYPARTY)) {
                client = "notifyParty";
            }
        }

        if (fclBl.getHouseBl() != null && (fclBl.getHouseBl().equalsIgnoreCase("P-Prepaid")
                || fclBl.getHouseBl().equalsIgnoreCase("P"))) {
            StringBuilder agentDetails = new StringBuilder();
            if ("Yes".equalsIgnoreCase(fclBl.getCollectThirdParty())) {
                agentDetails.append("COLLECT Freight Payable By : ");
            } else {
                agentDetails.append("PREPAID Freight Payable By : ");
            }
            if (client != null && client.equalsIgnoreCase("shipper")) {
                agentDetails.append(fclBl.getShipperName() != null ? fclBl.getShipperName() : "");
            } else if (client != null && client.equalsIgnoreCase("forwarder")) {
                if (null != fclBl.getForwardingAgentName() && !fclBl.getForwardingAgentName().trim().equalsIgnoreCase("NO FF ASSIGNED".trim())
                        && !fclBl.getForwardingAgentName().trim().equalsIgnoreCase("NO FF ASSIGNED / B/L PROVIDED".trim())
                        && !fclBl.getForwardingAgentName().trim().equalsIgnoreCase("NO FRT. FORWARDER ASSIGNED".trim())) {
                    agentDetails.append(fclBl.getForwardingAgentName() != null ? fclBl.getForwardingAgentName() : "");
                } else {
                    agentDetails.append("");
                }
            } else if (client != null && client.equalsIgnoreCase("agent")) {
                agentDetails.append(fclBl.getAgent() != null ? fclBl.getAgent() : "");
            } else if (client != null && client.equalsIgnoreCase("consignee")) {
                agentDetails.append(fclBl.getConsigneeName() != null ? fclBl.getConsigneeName() : "");
            } else if (client != null && client.equalsIgnoreCase("thirdParty")) {
                agentDetails.append(fclBl.getThirdPartyName() != null ? fclBl.getThirdPartyName() : "");
            } else if (client != null && client.equalsIgnoreCase("notifyParty")) {
                agentDetails.append(fclBl.getThirdPartyName() != null ? fclBl.getNotifyPartyName() : "");
            } else if (client != null && client.equalsIgnoreCase("")) {
                agentDetails.append("");
            }
            cell = makeCellLeftNoBorderFclBL(agentDetails.toString());
        } else if (fclBl.getHouseBl() != null && (fclBl.getHouseBl().equalsIgnoreCase("C-Collect")
                || fclBl.getHouseBl().equalsIgnoreCase("C"))) {
            StringBuilder agentDetails = new StringBuilder();
            if (client != null && client.equalsIgnoreCase("shipper")) {
                agentDetails.append("COLLECT Freight Payable By : ");
                agentDetails.append(fclBl.getShipperName() != null ? fclBl.getShipperName() : "");
            } else if (client != null && client.equalsIgnoreCase("forwarder")) {
                agentDetails.append("COLLECT Freight Payable By : ");
                if (null != fclBl.getForwardingAgentName() && !fclBl.getForwardingAgentName().trim().equalsIgnoreCase("NO FF ASSIGNED".trim())
                        && !fclBl.getForwardingAgentName().trim().equalsIgnoreCase("NO FF ASSIGNED / B/L PROVIDED".trim())) {
                    agentDetails.append(fclBl.getForwardingAgentName() != null ? fclBl.getForwardingAgentName() : "");
                } else {
                    agentDetails.append("");
                }
            } else if (client != null && client.equalsIgnoreCase("agent")) {
                agentDetails.append("COLLECT Freight Payable By : ");
                agentDetails.append(fclBl.getAgent() != null ? fclBl.getAgent() : "");
            } else if (client != null && client.equalsIgnoreCase("consignee")) {
                agentDetails.append("COLLECT Freight Payable By : ");
                agentDetails.append(fclBl.getConsigneeName() != null ? fclBl.getConsigneeName() : "");
            } else if (client != null && client.equalsIgnoreCase("notifyParty")) {
                agentDetails.append("COLLECT Freight Payable By : ");
                agentDetails.append(fclBl.getNotifyPartyName() != null ? fclBl.getNotifyPartyName() : "");
            } else if (client != null && client.equalsIgnoreCase("thirdParty")) {
                agentDetails.append("COLLECT Freight Payable By : ");
                agentDetails.append(fclBl.getThirdPartyName() != null ? fclBl.getThirdPartyName() : "");
            } else if (client != null && client.equalsIgnoreCase("")) {
                agentDetails.append("COLLECT Freight Payable By : ");
                agentDetails.append("");
            }
            cell = makeCellLeftNoBorderFclBL(agentDetails.toString());
        } else {
            StringBuilder agentDetails = new StringBuilder();
            if (client != null && client.equalsIgnoreCase("shipper")) {
                agentDetails.append("Freight Payable By : ");
                agentDetails.append(fclBl.getShipperName() != null ? fclBl.getShipperName() : "");
            } else if (client != null && client.equalsIgnoreCase("forwarder")) {
                agentDetails.append("Freight Payable By : ");
                if (null != fclBl.getForwardingAgentName() && !fclBl.getForwardingAgentName().trim().equalsIgnoreCase("NO FF ASSIGNED".trim())
                        && !fclBl.getForwardingAgentName().trim().equalsIgnoreCase("NO FF ASSIGNED / B/L PROVIDED".trim())) {
                    agentDetails.append(fclBl.getForwardingAgentName() != null ? fclBl.getForwardingAgentName() : "");
                } else {
                    agentDetails.append("");
                }
            } else if (client != null && client.equalsIgnoreCase("agent")) {
                agentDetails.append("Freight Payable By : ");
                agentDetails.append(fclBl.getAgent() != null ? fclBl.getAgent() : "");
            } else if (client != null && client.equalsIgnoreCase("cosignee")) {
                agentDetails.append("Freight Payable By : ");
                agentDetails.append(fclBl.getConsigneeName() != null ? fclBl.getConsigneeName() : "");
            } else if (client != null && client.equalsIgnoreCase("notifyParty")) {
                agentDetails.append("Freight Payable By : ");
                agentDetails.append(fclBl.getNotifyPartyName() != null ? fclBl.getNotifyPartyName() : "");
            } else if (client != null && client.equalsIgnoreCase("thirdParty")) {
                agentDetails.append("Freight Payable By : ");
                agentDetails.append(fclBl.getThirdPartyName() != null ? fclBl.getThirdPartyName() : "");
            } else if (client != null && client.equalsIgnoreCase("")) {
                agentDetails.append("Freight Payable By : ");
                agentDetails.append("");
            }
            cell = makeCellLeftNoBorderFclBL(agentDetails.toString());
        }

        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(5);
        particularsFurnishedTable.addCell(cell);

        cell = makeCellLeftNoBorderFclBL("");
        cell.setColspan(5);
        particularsFurnishedTable.addCell(cell);
        //String comment3 = commentsList.get(19) != null ? commentsList.get(19).toString() : "";
        String comment3 = BL100 != null ? BL100 : "";
        cell = makeCellLeftNoBorderPalatinoFclBl(comment3, palatinoRomanSmallFont);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(5);
        particularsFurnishedTable.addCell(cell);

        cell = makeCellLeftNoBorderFclBL("");
        cell.setColspan(5);
        particularsFurnishedTable.addCell(cell);

        if (commentsList != null && commentsList.size() > 0) {
            //String comment1 = commentsList.get(0) != null ? commentsList.get(0).toString() : "";
            String comment1 = BL101 != null ? BL101 : "";
            cell = makeCellLeftNoBorderPalatinoFclBl(comment1, drawBotTextFont);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(5);
            particularsFurnishedTable.addCell(cell);
            //String comment2 = commentsList.get(1) != null ? commentsList.get(1).toString() : "";
            String comment2 = BL102 != null ? BL102 : "";
            cell = makeCellLeftNoBorderPalatinoFclBl(comment2, drawBotTextFont);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(5);
            particularsFurnishedTable.addCell(cell);

        }
        cell = makeCellLeftNoBorderFclBL("");
        cell.setColspan(5);
        cell.setBorderWidthBottom(0.6f);
        particularsFurnishedTable.addCell(cell);
        cell = makeCellLeftNoBorderFclBL("");
        cell.setColspan(5);
        particularsFurnishedTable.addCell(cell);

        List TempList = new ArrayList();
        List chargeList = new ArrayList();
        Set<FclBlCharges> fclChargesSet = fclBl.getFclcharge();
        for (Iterator iter = fclChargesSet.iterator(); iter.hasNext();) {
            FclBlCharges fclBlCharges = (FclBlCharges) iter.next();
            TempList.add(fclBlCharges);
        }
        FclBlBC fclBlBC = new FclBlBC();
        boolean importFlag = "I".equalsIgnoreCase(fclBl.getImportFlag());
        if ("I".equalsIgnoreCase(fclBl.getImportFlag())) {
            chargeList = fclBlBC.consolidateRates(fclBlBC.getSortedList(TempList), messageResources, importFlag);
        } else {
            chargeList = new PrintConfigBC().getExportRatesForFclPrintForBounleOfr(fclBlBC.getSortedList(TempList), messageResources, client);
        }
        if (chargeList.size() > 0) {
            int check = 0;
            String whichList = "";
            List<FclBlCharges> shipperList = new ArrayList<FclBlCharges>();
            List<FclBlCharges> forwarderList = new ArrayList<FclBlCharges>();
            List<FclBlCharges> agentList = new ArrayList<FclBlCharges>();
            List<FclBlCharges> thirdPartyList = new ArrayList<FclBlCharges>();
            List<FclBlCharges> consigneeList = new ArrayList<FclBlCharges>();
            List<FclBlCharges> notifyPartyList = new ArrayList<FclBlCharges>();

            for (Iterator iter = chargeList.iterator(); iter.hasNext();) {
                FclBlCharges fclBlCharges = (FclBlCharges) iter.next();
                if (fclBlCharges != null && fclBlCharges.getBillTo() != null) {
                    String thridPartyName = fclBlCharges.getBillTo();
                    if (thridPartyName.equalsIgnoreCase("Shipper")) {
                        check = check + 1;
                        whichList = "Shipper";
                        shipperList.add(fclBlCharges);
                    } else if (thridPartyName.equalsIgnoreCase("Forwarder")) {
                        check = check + 1;
                        whichList = "Forwarder";
                        forwarderList.add(fclBlCharges);
                    } else if (thridPartyName.equalsIgnoreCase("Agent")) {
                        check = check + 1;
                        whichList = "Agent";
                        agentList.add(fclBlCharges);
                    } else if (thridPartyName.equalsIgnoreCase("ThirdParty")) {
                        check = check + 1;
                        whichList = "ThirdParty";
                        thirdPartyList.add(fclBlCharges);
                    } else if (thridPartyName.equalsIgnoreCase("Consignee")) {
                        check = check + 1;
                        whichList = "Consignee";
                        consigneeList.add(fclBlCharges);
                    } else if (thridPartyName.equalsIgnoreCase("NotifyParty")) {
                        check = check + 1;
                        whichList = "NotifyParty";
                        consigneeList.add(fclBlCharges);
                    }
                }
            }
            String whichListToPass = "";
            if (fclBl.getHouseBl() != null && (fclBl.getHouseBl().equalsIgnoreCase("B-Both") || fclBl.getHouseBl().equalsIgnoreCase("B"))) {
                whichListToPass = "both";
            } else if (fclBl.getBillToCode() != null && fclBl.getBillToCode().equalsIgnoreCase("S")) {
                whichListToPass = "shipper";
            } else if (fclBl.getBillToCode() != null && fclBl.getBillToCode().equalsIgnoreCase("F")) {
                whichListToPass = "forwarder";
            } else if (fclBl.getBillToCode() != null && fclBl.getBillToCode().equalsIgnoreCase("T")) {
                whichListToPass = "thirdParty";
            } else if (fclBl.getBillToCode() != null && fclBl.getBillToCode().equalsIgnoreCase("A")) {
                whichListToPass = "agent";
            } else if (fclBl.getBillToCode() != null && fclBl.getBillToCode().equalsIgnoreCase("C")) {
                whichListToPass = "consignee";
            } else if (fclBl.getBillToCode() != null && fclBl.getBillToCode().equalsIgnoreCase("N")) {
                whichListToPass = "notifyParty";
            } else {
                whichListToPass = "both";
            }
            if (whichListToPass.equalsIgnoreCase("agent")) {
                bottomTable = getBottomTable(fclBl, agentList, "agent", palatinoRomanSmallFont, palatinoRomanLargeFont, commentsList);
            } else if (whichListToPass.equalsIgnoreCase("forwarder")) {
                bottomTable = getBottomTable(fclBl, forwarderList, "forwarder", palatinoRomanSmallFont, palatinoRomanLargeFont, commentsList);
            } else if (whichListToPass.equalsIgnoreCase("shipper")) {
                bottomTable = getBottomTable(fclBl, shipperList, "shipper", palatinoRomanSmallFont, palatinoRomanLargeFont, commentsList);
            } else if (whichListToPass.equalsIgnoreCase("thirdParty")) {
                bottomTable = getBottomTable(fclBl, thirdPartyList, "thirdParty", palatinoRomanSmallFont, palatinoRomanLargeFont, commentsList);
            } else if (whichListToPass.equalsIgnoreCase("consignee")) {
                bottomTable = getBottomTable(fclBl, consigneeList, "consignee", palatinoRomanSmallFont, palatinoRomanLargeFont, commentsList);
            } else if (whichListToPass.equalsIgnoreCase("notifyParty")) {
                bottomTable = getBottomTable(fclBl, consigneeList, "notifyParty", palatinoRomanSmallFont, palatinoRomanLargeFont, commentsList);
            } else if (whichListToPass.equalsIgnoreCase("both")) {
                if (simpleRequest.equalsIgnoreCase(PrintReportsConstants.FREIGHT_INVOICE_AGENT)) {
                    bottomTable = getBottomTable(fclBl, agentList, "agent", palatinoRomanSmallFont, palatinoRomanLargeFont, commentsList);
                } else if (simpleRequest.equalsIgnoreCase(PrintReportsConstants.FREIGHT_INVOICE_FORWARDER)) {
                    bottomTable = getBottomTable(fclBl, forwarderList, "forwarder", palatinoRomanSmallFont, palatinoRomanLargeFont, commentsList);
                } else if (simpleRequest.equalsIgnoreCase(PrintReportsConstants.FREIGHT_INVOICE_SHIPPER)) {
                    bottomTable = getBottomTable(fclBl, shipperList, "shipper", palatinoRomanSmallFont, palatinoRomanLargeFont, commentsList);
                } else if (simpleRequest.equalsIgnoreCase(PrintReportsConstants.FREIGHT_INVOICE_THIRDPARTY)) {
                    bottomTable = getBottomTable(fclBl, thirdPartyList, "thirdParty", palatinoRomanSmallFont, palatinoRomanLargeFont, commentsList);
                } else if (simpleRequest.equalsIgnoreCase(PrintReportsConstants.FREIGHT_INVOICE_CONSIGNEE)) {
                    bottomTable = getBottomTable(fclBl, consigneeList, "consignee", palatinoRomanSmallFont, palatinoRomanLargeFont, commentsList);
                } else if (simpleRequest.equalsIgnoreCase(PrintReportsConstants.FREIGHT_INVOICE_NOTIFYPARTY)) {
                    bottomTable = getBottomTable(fclBl, consigneeList, "notifyParty", palatinoRomanSmallFont, palatinoRomanLargeFont, commentsList);
                }
            }
        } else {
        }
        Phrase phrase = new Phrase();
        phrase.clear();
        PdfPCell footerCell = makeCellLeftNoBorder("");

        footerCell.addElement(particularsFurnishedTable);
        footerCell.addElement(bottomTable);
        footerTable.addCell(footerCell);
        return footerTable;
    }

    public void onEndPage(PdfWriter writer, Document document) {
        try {
            PdfPTable footerTable = this.getMyFooter();
            footerTable.setTotalWidth(document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin());
            footerTable.writeSelectedRows(0, -1, document.leftMargin(), document.bottomMargin(), writer.getDirectContent());
            //this for print page number at the bottom in the format x of y
            PdfContentByte cb = writer.getDirectContent();
            cb.saveState();
            String text = "Page " + writer.getPageNumber() + " of ";
            float textBase = document.bottom() - (document.bottomMargin() - 15);
            float textSize = helv.getWidthPoint(text, 12);
            cb.beginText();
            cb.setFontAndSize(helv, 7);
            cb.setTextMatrix(document.left() + 490, textBase);
            cb.showText(text);
            cb.endText();
            cb.addTemplate(total, document.left() + 470 + textSize, textBase);
            cb.restoreState();
            ///this for the water mark..........................
            BaseFont helv;
            PdfGState gstate;
            Font hellv;
            String waterMark = PrintReportsConstants.FREIGHT_INVOICE;

            try {
                helv = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI,
                        BaseFont.NOT_EMBEDDED);

            } catch (Exception e) {
                log.info("onEndPage failed on " + new Date(), e);
                throw new ExceptionConverter(e);
            }
            gstate = new PdfGState();
            gstate.setFillOpacity(0.10f);
            gstate.setStrokeOpacity(0.3f);
            PdfContentByte contentunder = writer.getDirectContentUnder();
            contentunder.saveState();
            contentunder.setGState(gstate);
            contentunder.beginText();
            contentunder.setFontAndSize(helv, 50);
            contentunder.showTextAligned(Element.ALIGN_CENTER, waterMark,
                    document.getPageSize().getWidth() / 2, document.getPageSize().getHeight() / 2, 45);
            contentunder.endText();
            contentunder.restoreState();
        } catch (Exception e) {
            log.info("onEndPage failed on " + new Date(), e);
            throw new ExceptionConverter(e);
        }
    }

    public void onCloseDocument(PdfWriter writer, Document document) {
        total.beginText();
        total.setFontAndSize(helv, 7);
        total.setTextMatrix(0, 0);
        if (CommonFunctions.isNotNull(manifestRev)) {
            total.showText(String.valueOf(writer.getPageNumber() - 1) + "        (REV: " + manifestRev + ")");
        } else {
            total.showText(String.valueOf(writer.getPageNumber() - 1));
        }
        total.endText();
    }

    public void destroy() {
        document.close();
    }

    public String getBolNo(String bolId) throws Exception {
        String companyCode = new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
        String BolNo = "";
        if (null != bolId && !bolId.equalsIgnoreCase("")) {
            if (-1 != bolId.indexOf("=")) {
                BolNo = bolId.substring(0, bolId.indexOf("="));
            } else {
                BolNo = bolId;
            }
            if ("02".equals(companyCode)) {
                BolNo = (String) messageResources.getMessage("defaultAgent") + " " + BolNo;
            } else {
                BolNo = (String) messageResources.getMessage("defaultAgentforECCI") + " " + BolNo;
            }
        }
        if ("I".equalsIgnoreCase(fclBl.getImportFlag())) {
            return BolNo;
        } else {
            return BolNo.replace(" ", "-");
        }
    }

    public String getAddress(String accoutNo) throws Exception {
        StringBuilder address = new StringBuilder();
        if (null != accoutNo && !"".equalsIgnoreCase(accoutNo)) {
            CustAddressDAO customerDAO = new CustAddressDAO();
            CustAddress custAddress = customerDAO.findByAccountNo(accoutNo);
            if (null != custAddress) {
                address.append((null != custAddress.getCoName() && !custAddress.getCoName().equals("")) ? custAddress.getCoName() : "");
                address.append("\n");
                address.append((null != custAddress.getAddress1() && !custAddress.getAddress1().equals("")) ? custAddress.getAddress1() : "");
                address.append("\n");
                address.append((null != custAddress.getCity1() && !custAddress.getCity1().equals("")) ? custAddress.getCity1() + "," : "");
                address.append((null != custAddress.getState() && !custAddress.getState().equals("")) ? custAddress.getState() + "," : "");
                address.append((null != custAddress.getZip() && !custAddress.getZip().equals("")) ? custAddress.getZip() : "");
                address.append(".");
            }
        }
        return address.toString();
    }

    public String removeUnlocCode(String port) {
        String portName = "";
        if (null != port) {
            if (port.lastIndexOf("(") != -1) {
                portName = port.substring(0, port.lastIndexOf("("));
            } else {
                portName = port;
            }
            int length = portName.length();
            if (portName.length() > 0 && portName.charAt(length - 1) == '/') {
                portName = portName.substring(0, length - 1);
            }
        }
        return portName;
    }

    public String removeUnlocCodeAppendCountryName(String port) throws Exception {
        String portName = "";
        if (null != port) {
            if (port.lastIndexOf("(") != -1 && port.lastIndexOf(")") != -1) {
                String unLocCode = "";
                unLocCode = port.substring(port.lastIndexOf("(") + 1, port.lastIndexOf(")"));
                UnLocationDAO locationDAO = new UnLocationDAO();
                UnLocation location = locationDAO.getUnlocation(unLocCode);
                if (-1 != port.indexOf("/")) {
                    portName = port.substring(0, port.indexOf("/")) + "/" + ((null != location.getCountryId()) ? location.getCountryId().getCodedesc() : "");
                } else {
                    portName = port + location.getUnLocationName();
                }
            }
        }
        return portName;
    }

    public String createBillOfLaddingReport(FclBl bl, String fileName,
            String contextPath, MessageResources messageResources,
            List commentsList, User user, String simpleRequest) throws Exception {
        if (bl != null && bl.getAgentNo() != null) {
            logoS = new TradingPartnerDAO().getLogoStatus(bl.getAgentNo());
        }
        try {
            this.initialize(fileName, contextPath, bl, commentsList, simpleRequest, messageResources, logoS);
            this.printCommands();
            this.createBody(contextPath, bl, commentsList, simpleRequest, messageResources);
            this.destroy();
        } catch (Exception e) {
            log.info("createBillOfLaddingReport failed on " + new Date(), e);
            throw new ExceptionConverter(e);
        }
        return "fileName";
    }

    public String getIssuingTM(FclBl bl) {
        String domesticRoute = "";
        String issuingTerm = "";
        String codeNo = "";
        String code = "";
        String codeVal = "";
        String[] temp;
        String[] temp1;
        if (bl.getDomesticRouting() != null) {
            issuingTerm = bl.getDomesticRouting();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("(");
            temp = issuingTerm.split(",");
            if (!Arrays.equals(temp, null) && temp.length == 2) {
                for (int i = 0; i < temp.length; i++) {
                    if (i == 0) {
                        codeVal = temp[i];
                    } else {
                        String codeandNo = temp[i];
                        temp1 = codeandNo.split("-");
                        for (int j = 0; j < temp1.length; j++) {
                            if (j == 0) {
                                code = temp1[j];
                            } else {
                                codeNo = temp1[j];
                            }
                        }
                    }
                }
            }
            stringBuilder.append(codeNo);
            stringBuilder.append(") ");
            stringBuilder.append(codeVal);
            stringBuilder.append(" -");
            stringBuilder.append(code);
            domesticRoute = stringBuilder.toString();
        }
        return domesticRoute;
    }

    public void printCommands() throws Exception {
        Iterator iter = new GenericCodeDAO().getCommentsListForBlReport();
        Iterator iter1 = new GenericCodeDAO().getCommentsListForFreightInvoiceReport();
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            String code = (String) row[0];
            String codeDesc = (String) row[1];
            if (code != null) {
                if ("BL100".equals(code)) {
                    BL100 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                } else if ("BL101".equals(code)) {
                    BL101 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                } else if ("BL102".equals(code)) {
                    BL102 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                }
            }
        }
        while (iter1.hasNext()) {
            Object[] row = (Object[]) iter1.next();
            String code = (String) row[0];
            String codeDesc = (String) row[1];
            if (code != null) {
                if ("FI100".equals(code)) {
                    FI100 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                } else if ("FI101".equals(code)) {
                    FI101 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                } else if ("FI102".equals(code)) {
                    FI102 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                }
            }
        }
    }

    public String checkPayment(FclBl bl) throws Exception {
        String customerNumber = "";
        String returnString = "";
        if (bl.getBillToCode() != null && bl.getBillToCode().equals("F")) {
            customerNumber = bl.getForwardAgentNo();
        } else if (bl.getBillToCode() != null && bl.getBillToCode().equals("S")) {
            customerNumber = bl.getShipperNo();
        } else if (bl.getBillToCode() != null && bl.getBillToCode().equals("T")) {
            customerNumber = bl.getBillTrdPrty();
        } else if (bl.getBillToCode() != null && bl.getBillToCode().equals("A")) {
            customerNumber = bl.getAgentNo();
        }

        if (customerNumber != null && !customerNumber.equals("")) {
            FclBlBC blBC = new FclBlBC();
            String creditDetail = blBC.validateCustomer(customerNumber, bl.getImportFlag());
            if (creditDetail != null && !creditDetail.equals("")) {
                String[] chargecode = creditDetail.split("==");
                String crditWarning = chargecode[0].substring(3, chargecode[0].indexOf("For the Party "));
                if (crditWarning.equals("In Good Standing ") || crditWarning.equals("Credit Hold ")) {
                    CustomerAccounting customerAccounting = new CustomerAccountingDAO().findByProperty("accountNo", customerNumber);
                    if (customerAccounting != null && (customerAccounting.getCreditRate() != null)) {
                        if ((customerAccounting.getCreditRate().getCodedesc()).equalsIgnoreCase("Net 7 Days")) {
                            returnString = "DUE WITHIN 7 DAYS OF SAILING DATE";
                        } else if ((customerAccounting.getCreditRate().getCodedesc()).equalsIgnoreCase("Net 15 Days")) {
                            returnString = "DUE WITHIN 15 DAYS OF SAILING DATE";
                        } else if ((customerAccounting.getCreditRate().getCodedesc()).equalsIgnoreCase("NET 21 DAYS")) {
                            returnString = "DUE WITHIN 21 DAYS OF SAILING DATE";
                        } else if ((customerAccounting.getCreditRate().getCodedesc()).equalsIgnoreCase("Net 30 Days")) {
                            returnString = "DUE WITHIN 30 DAYS OF SAILING DATE";
                        } else if ((customerAccounting.getCreditRate().getCodedesc()).equalsIgnoreCase("Net 45 Days")) {
                            returnString = "DUE WITHIN 45 DAYS OF SAILING DATE";
                        } else if ((customerAccounting.getCreditRate().getCodedesc()).equalsIgnoreCase("Net 60 Days")) {
                            returnString = "DUE WITHIN 60 DAYS OF SAILING DATE";
                        }
                    }
                } else {
                    returnString = FI102 != null ? FI102 : "";
                }
            }
        }
        return returnString;
    }

    public String trimShipperConsigNotifyForwardAddress(String address) {
        StringBuilder addr = new StringBuilder();
        String[] tempAddr = null;
        Integer len = 0;
        if (null != address && !address.equals("")) {
            tempAddr = address.trim().split("\n");
            len = tempAddr.length;
            for (int i = 0; i < len; i++) {
                if (i < 6) {
                    addr.append(tempAddr[i]);
                }
            }
            return addr.toString().trim();
        }
        return "";
    }

    public int getADDRESS_SIZE() {
        return ADDRESS_SIZE;
    }

    public void setADDRESS_SIZE(int ADDRESS_SIZE) {
        this.ADDRESS_SIZE = ADDRESS_SIZE;
    }

    public int getDESC_SIZE() {
        return DESC_SIZE;
    }

    public void setDESC_SIZE(int DESC_SIZE) {
        this.DESC_SIZE = DESC_SIZE;
    }
}
