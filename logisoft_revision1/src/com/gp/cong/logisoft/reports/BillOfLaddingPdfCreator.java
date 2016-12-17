package com.gp.cong.logisoft.reports;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
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
import com.gp.cong.logisoft.domain.FclInbondDetails;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.edi.inttra.HelperClass;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.FclInbondDetailsDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
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
import com.gp.cvst.logisoft.hibernate.dao.FclBlDAO;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.logiware.hibernate.domain.PaymentRelease;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfGState;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

import org.apache.log4j.Logger;

public class BillOfLaddingPdfCreator extends ReportFormatMethods {

    private static final Logger log = Logger.getLogger(BillOfLaddingPdfCreator.class);
    FclBlBC fclBlBC = new FclBlBC();
    Document document = null;
    PdfWriter pdfWriter = null;
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    protected PdfTemplate total;
    protected BaseFont helv;
    private double chargesTotal = 0.00;
    private String simpleRequest = "";
    private String manifested = "";
    private String proof = "";
    private MessageResources messageResources = null;
    HelperClass helperClass = new HelperClass();
    //--------------testing
    private FclBl fclBl = null;
    private Set<FclBlCharges> chargesSet = null;
    private String context = "";
    private String manifestRev = "";
    private List commentsList = null;
    private boolean printCharges = false;
    private int ADDRESS_SIZE = 0;
    private int DESC_SIZE = 0;
    private String logoS = "";
    private static String BL100 = "";
    private static String BL101 = "";
    private static String BL102 = "";
    private static String BL103 = "";
    private static String BL200 = "";
    private static String BL201 = "";
    private static String AN200 = "";
    private static String AN300 = "";
    private static String AN301 = "";
    private static String AN302 = "";
    private static String AN400 = "";
    private static String AN401 = "";
    private static String AN402 = "";
    private static String AN403 = "";
    private static String AN404 = "";
    private static String AN405 = "";
    private static String AN406 = "";
    private static String AN407 = "";
    private static String AN408 = "";
    private static String AN409 = "";
    private static String AN100 = "";
    private static String AN101 = "";
    private static String AN102 = "";
    private static String AN103 = "";
    private static String AN104 = "";
    private static String field1ForBL100 = "";
    private static String field1ForBL101 = "";
    private static String field1ForBL102 = "";
    private static String field1ForBL103 = "";
    private static String companyCode = "";

    public BillOfLaddingPdfCreator() {
    }

    public BillOfLaddingPdfCreator(FclBl bl, List commentList, MessageResources messageResource, String contextPath, String request, String logoStatus) throws Exception {
        fclBl = bl;
        commentsList = commentList;
        manifested = bl.getReadyToPost();
        proof = bl.getProof();
        messageResources = messageResource;
        chargesTotal = 0.00;
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
            if (CommonFunctions.isNotNull(fclbl.getManifestRev()) && "Yes".equalsIgnoreCase(fclbl.getPrintRev())) {
                manifestRev = fclbl.getManifestRev();
            }
        }
    }

    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        total = writer.getDirectContent().createTemplate(100, 100);
        total.setBoundingBox(new Rectangle(-20, -20, 100, 100));
        try {
            helv = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI,
                    BaseFont.NOT_EMBEDDED);
        } catch (Exception e) {
            log.info("onOpenDocument failed on " + new Date(), e);
            throw new ExceptionConverter(e);
        }
    }

    public void initialize(String fileName, FclBl fclBl, String contextPath, List commentsList, MessageResources messageResource, String simpleRequest, boolean importFlag, String logoStatus) throws FileNotFoundException,
            DocumentException {
        try {
            document = new Document(PageSize.A4);
            if ("fclArrivalNotice".equalsIgnoreCase(simpleRequest) || "fclArrivalNoticeNonRated".equalsIgnoreCase(simpleRequest)) {
                document.setMargins(0, 0, 0, 310);
            } else if (CommonUtils.in(simpleRequest, "freightedNonNegotiable", "freightedOriginalBl", "UnmarkedHouseBillofLading")) {
                document.setMargins(0, 0, 0, 320);
            } else if ("freightedMasterBl".equalsIgnoreCase(simpleRequest)) {
                document.setMargins(0, 0, 0, 290);
            } else {
                document.setMargins(0, 0, 0, 270);
            }

            pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(fileName));
            pdfWriter.setPageEvent(new BillOfLaddingPdfCreator(fclBl, commentsList, messageResource, contextPath, simpleRequest, logoStatus));
            document.setHeader(getMyHeader(fclBl, contextPath, messageResource, simpleRequest, importFlag));
            document.open();
//            PdfAction action = PdfAction.gotoLocalPage(1, new PdfDestination(PdfDestination.XYZ, -1, -1, 0.75f),pdfWriter);
//            pdfWriter.setOpenAction(action);
        } catch (Exception e) {
            log.info("initialize failed on " + new Date(), e);
            throw new ExceptionConverter(e);
        }

    }

    public void createBody(String contextPath, FclBl bl, List commentsList,
            String simpleRequest, MessageResources messageResource, boolean importFlag)
            throws MalformedURLException, IOException, DocumentException, Exception {

        BaseFont palationRomanBase = BaseFont.createFont(contextPath + "/ttf/Palatino-Roman.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font palatinoRomanSmallFont = new Font(palationRomanBase, 8, Font.NORMAL, Color.BLACK);
        FclBl fclbl = new FclBlDAO().getOriginalBl(bl.getFileNo());
        if ("fclArrivalNotice".equalsIgnoreCase(simpleRequest) || "fclArrivalNoticeNonRated".equalsIgnoreCase(simpleRequest)) {
            fillMarksArrivalNotice(fclbl, messageResource, palatinoRomanSmallFont);
        } else {
            fillMarksAndContinerInformation(fclbl, messageResource, palatinoRomanSmallFont);
        }
        PdfPTable particularsAesTable = fillAesDetails(fclbl, messageResource, palatinoRomanSmallFont, importFlag, simpleRequest);
//        particularsAesTable.setExtendLastRow(true);
        document.add(particularsAesTable);
        if (CommonUtils.in(simpleRequest, "freightedNonNegotiable", "freightedOriginalBl")) {
            PdfPTable chargesTable = getChargesTable(bl, messageResource, contextPath, simpleRequest);
            chargesTable.setTotalWidth(document.getPageSize().getWidth() / 2 - document.leftMargin() - document.rightMargin() + 56);
            chargesTable.writeSelectedRows(0, -1, document.leftMargin(), document.bottomMargin() - 70, pdfWriter.getDirectContent());
        } else if (simpleRequest.equalsIgnoreCase("UnmarkedHouseBillofLading")) {
            PdfPTable chargesTable = getChargesTable(bl, messageResource, contextPath, simpleRequest);
            chargesTable.setTotalWidth(document.getPageSize().getWidth() / 2 - document.leftMargin() - document.rightMargin() + 56);
            chargesTable.writeSelectedRows(0, -1, document.leftMargin(), document.bottomMargin() - 60, pdfWriter.getDirectContent());
        } else if (simpleRequest.equalsIgnoreCase("freightedMasterBl")) {
            PdfPTable chargesTable = getChargesTable(bl, messageResource, contextPath, simpleRequest);
            chargesTable.setTotalWidth(document.getPageSize().getWidth() / 2 - document.leftMargin() - document.rightMargin() + 56);
            chargesTable.writeSelectedRows(0, -1, document.leftMargin(), document.bottomMargin() - 72, pdfWriter.getDirectContent());
        } else if (simpleRequest.equalsIgnoreCase("fclArrivalNotice")) {
            PdfPTable chargesTable = getChargesTable(bl, messageResource, contextPath, simpleRequest);
            chargesTable.setTotalWidth(document.getPageSize().getWidth() / 2 - document.leftMargin() - document.rightMargin() + 25);
            chargesTable.writeSelectedRows(0, -1, document.leftMargin(), document.bottomMargin() - 55, pdfWriter.getDirectContent());
        }
    }

    public HeaderFooter getMyHeader(FclBl bl, String contextPath, MessageResources messageResources, String simpleRequest, boolean importFlag) {
        try {
            FclBl originalBl = new FclBlDAO().getOriginalBl(bl.getFileNo());
            Phrase phrase = new Phrase(-16);
            PdfPCell cell = null;
            PdfPCell mainCell = null;
            String companyName = "";
            Phrase phraseLogoS = new Phrase();
            PdfPCell footerCellLogoS = null;
            BaseFont palationRomanBase = BaseFont.createFont(contextPath + "/ttf/Palatino-Roman.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font palatinoRomanBigFont = new Font(palationRomanBase, 12, Font.NORMAL, Color.BLACK);
            Font palatinoRomanBigFontBold = new Font(palationRomanBase, 12, Font.BOLD, Color.BLACK);
            Font palatinoRomanSmallFont = new Font(palationRomanBase, 8, Font.NORMAL, Color.BLACK);
            Font palatinoRomanMediumFont = new Font(palationRomanBase, 10, Font.NORMAL, Color.BLACK);
            PdfPTable headingTable = makeTable(3);
            headingTable.setWidthPercentage(100);

            if (!CommonFunctions.isNotNull(originalBl.getReadyToPost())
                    && originalBl.getPreAlert() != null && originalBl.getPreAlert().equalsIgnoreCase("yes") && !"fclArrivalNotice".equalsIgnoreCase(simpleRequest) && !"fclArrivalNoticeNonRated".equalsIgnoreCase(simpleRequest)) {
                cell = makeCellleftNoBorderForHeadingFclBL(PrintReportsConstants.PREALTER);
                cell.setColspan(3);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headingTable.addCell(cell);//1
            }
            if ("fclArrivalNotice".equalsIgnoreCase(simpleRequest) || "fclArrivalNoticeNonRated".equalsIgnoreCase(simpleRequest)) {
                if ((originalBl.getBolId() != null && originalBl.getBolId().indexOf("=") != -1) || "Y".equalsIgnoreCase(originalBl.getCorrectedAfterManifest())) {
                    cell = makeCellCenterNoBorderPalatinoFclBl("CORRECTED", palatinoRomanBigFont);
                    cell.setColspan(3);
                    cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    headingTable.addCell(cell);//2
                }
            } else if (originalBl.getBolId() != null && originalBl.getBolId().indexOf("=") != -1) {
                if (simpleRequest != null && (!simpleRequest.equalsIgnoreCase("UnfreightedMasterBl") || !simpleRequest.equalsIgnoreCase("freightedMasterBl"))) {
                    cell = makeCellCenterNoBorderPalatinoFclBl("CORRECTED", palatinoRomanBigFont);
                    cell.setColspan(3);
                    cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    headingTable.addCell(cell);//2
                }
            }
//            if ("Y".equalsIgnoreCase(logoS)) {
//                companyName = LoadLogisoftProperties.getProperty("application.fclBl.print.companyName");
//                if (companyName.contains("(")) {
//                    phraseLogoS.add(new Phrase(new Chunk(companyName.substring(0, companyName.indexOf("(")), bottom3TextFontHead)));
//                    phraseLogoS.add(new Phrase(new Chunk(" " + companyName.substring(companyName.indexOf("(")), bottomTextFontSub)));
//                } else {
//                    phraseLogoS.add(new Phrase(companyName.toUpperCase(), bottom3TextFontHead));
//                }
//                footerCellLogoS = makeCell(phraseLogoS, Element.ALIGN_LEFT);
//            } else {
//                companyName = LoadLogisoftProperties.getProperty("application.fclBl.print.companyName.econo");
//                phraseLogoS.add(new Phrase(companyName.toUpperCase(), bottom3TextFontHead));
//                footerCellLogoS = makeCell(phraseLogoS, Element.ALIGN_LEFT);
//            }
            companyCode = new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
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
            headingTable.addCell(footerCellLogoS);//3
            if (simpleRequest != null && (simpleRequest.equalsIgnoreCase("UnfreightedNonNegotiable") || simpleRequest.equalsIgnoreCase("freightedNonNegotiable"))) {
                cell = makeCellCenterNoBorderPalatinoFclBl(originalBl.getDockReceipt().equals("Yes") ? ReportConstants.DOCK_RECEIPT : ReportConstants.NONNEGOTIABLE, palatinoRomanBigFontBold);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
            } else if (simpleRequest != null && (simpleRequest.equalsIgnoreCase("UnfreightedMasterBl") || simpleRequest.equalsIgnoreCase("freightedMasterBl"))) {
                if (originalBl.getBolId() != null && originalBl.getBolId().indexOf("=") != -1) {
                    cell = makeCellCenterNoBorderPalatinoFclBl("CORRECTED", palatinoRomanBigFont);
                    cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    headingTable.addCell(cell);
                } else {
                    cell = makeCellleftNoBorderForHeadingFclBL("");
                    cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                }
            } else if (simpleRequest != null && (simpleRequest.equalsIgnoreCase("UnfreightedOriginalBl") || simpleRequest.equalsIgnoreCase("freightedOriginalBl"))) {
                cell = makeCellCenterNoBorderPalatinoFclBl(originalBl.getDockReceipt().equals("Yes") ? ReportConstants.DOCK_RECEIPT : ReportConstants.ORIGINAL, palatinoRomanBigFontBold);
                cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            } else if (simpleRequest != null && (simpleRequest.equalsIgnoreCase("fclArrivalNotice") || "fclArrivalNoticeNonRated".equalsIgnoreCase(simpleRequest))) {
                DateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm");
                Date todaysDate = new Date();
                cell = makeCellLeftNoBorderPalatinoFclBl(format.format(todaysDate), palatinoRomanSmallFont);
                cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            } else if (simpleRequest != null && simpleRequest.equalsIgnoreCase("UnmarkedHouseBillofLading")) {
                cell = makeCellCenterNoBorderPalatinoFclBl("", palatinoRomanBigFontBold);
                cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            }
            headingTable.addCell(cell);
            if (simpleRequest != null && (simpleRequest.equalsIgnoreCase("fclArrivalNotice") || "fclArrivalNoticeNonRated".equalsIgnoreCase(simpleRequest)) && "No".equalsIgnoreCase(originalBl.getReplaceArrival())) {
                cell = makeCellleftNoBorderForHeadingFclBLWithBlack(ReportConstants.ARRIVALNOTICE);
            } else {
                cell = makeCellleftNoBorderForHeadingFclBLWithBlack(ReportConstants.BILLOFLADINGFORREPORT);
            }
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            headingTable.addCell(cell);
            PdfPTable detailsTable = makeTable(2);
            detailsTable.setWidths(new float[]{54, 46});
            detailsTable.setWidthPercentage(100);
            detailsTable.setKeepTogether(true);
            // shipper exporter table
            mainCell = makeCellleftNoBorderWithBoldFont("");
            mainCell.setBorderWidthLeft(0.0f);
            mainCell.setBorderWidthRight(0.6f);
            mainCell.setBorderWidthBottom(0.6f);
            mainCell.setBorderWidthTop(0.6f);
            PdfPTable shipperExporter = makeTable(1);
            shipperExporter.setWidthPercentage(100);
            cell = makeCellLeftNoBorderFclBL("SHIPPER/EXPORTER");
            cell.setLeading(0.5f, 0.5f);
            shipperExporter.addCell(cell);
            String masterShipperAddr = "";
            String houseShipperAddr = "";
            if (simpleRequest != null && (simpleRequest.equalsIgnoreCase("UnfreightedNonNegotiable") || simpleRequest.equalsIgnoreCase("UnfreightedOriginalBl") || simpleRequest.equalsIgnoreCase("freightedOriginalBl") || simpleRequest.equalsIgnoreCase("freightedNonNegotiable")
                    || simpleRequest.equalsIgnoreCase("fclArrivalNotice") || simpleRequest.equalsIgnoreCase("UnmarkedHouseBillofLading") || "fclArrivalNoticeNonRated".equalsIgnoreCase(simpleRequest))) {
                houseShipperAddr = null != bl.getShipperAddress() ? bl.getShipperAddress().trim() : "";
//                houseShipperAddr = trimShipperConsigNotifyForwardAddress(houseShipperAddr);
                if (bl.getBolId().indexOf("==") != -1 && CommonUtils.isEmpty(bl.getShipperAddress())) {
//                    houseShipperAddr = trimShipperConsigNotifyForwardAddress(getAddress(bl.getShipperNo()));
                    houseShipperAddr = getAddress(bl.getShipperNo());
                }
                StringBuilder shipperDetails = new StringBuilder();
                shipperDetails.append(bl.getShipperName() != null ? bl.getShipperName() : "");
                shipperDetails.append("\n");
                shipperDetails.append(houseShipperAddr);
                List l = helperClass.wrapAddress(houseShipperAddr);
                if (l.isEmpty() || l.size() < 5) {
                    ADDRESS_SIZE += 4;
                } else {
                    ADDRESS_SIZE += l.size();
                }
                cell = makeCellLeftNoBorderPalatinoFclBl(shipperDetails.toString(), palatinoRomanMediumFont);
                cell.setExtraParagraphSpace(2f);
                shipperExporter.addCell(cell);
            } else if (simpleRequest != null && (simpleRequest.equalsIgnoreCase("UnfreightedMasterBl") || simpleRequest.equalsIgnoreCase("freightedMasterBl"))) {
                masterShipperAddr = null != originalBl.getHouseShipperAddress() ? originalBl.getHouseShipperAddress().trim() : "";
//                masterShipperAddr = trimShipperConsigNotifyForwardAddress(masterShipperAddr);
                StringBuilder shipperDetails = new StringBuilder();
                shipperDetails.append(originalBl.getHouseShipper() != null ? originalBl.getHouseShipper() : "");
                shipperDetails.append("\n");
                shipperDetails.append(masterShipperAddr);
                List l = helperClass.wrapAddress(shipperDetails.toString());
                if (l.isEmpty() || l.size() < 5) {
                    ADDRESS_SIZE += 4;
                } else {
                    ADDRESS_SIZE += l.size();
                }
                cell = makeCellLeftNoBorderPalatinoFclBl(shipperDetails.toString(), palatinoRomanMediumFont);
                cell.setExtraParagraphSpace(2f);
                shipperExporter.addCell(cell);
            }
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
            cell = makeCellLeftNoBorderFclBL("DOCUMENT NO.");
            cell.setLeading(0.5f, 0.5f);
            documentNoTable.addCell(cell);
            //  String companyCode = new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
            if (simpleRequest != null && (simpleRequest.equalsIgnoreCase("fclArrivalNotice") || "fclArrivalNoticeNonRated".equalsIgnoreCase(simpleRequest))) {
                String fileNo = (String) messageResources.getMessage("fileNumberPrefix") + originalBl.getFileNo().toString();
                cell = makeCellLeftNoBorderPalatinoFclBl(fileNo, palatinoRomanMediumFont);
            } else {
                String BolNo = "";
                if (null != originalBl.getBolId().toString() && !originalBl.getBolId().toString().equalsIgnoreCase("")) {
                    if (-1 != originalBl.getBolId().toString().indexOf("=")) {
                        BolNo = originalBl.getBolId().toString().substring(0, originalBl.getBolId().toString().indexOf("="));
                    } else {
                        BolNo = originalBl.getBolId().toString();
                    }
                    if ("yes".equalsIgnoreCase(originalBl.getOmitTermAndPort())) {
                        if ("02".equals(companyCode)) {
                            BolNo = (String) messageResources.getMessage("defaultAgent") + "-04-" + originalBl.getFileNo();
                        } else {
                            BolNo = (String) messageResources.getMessage("defaultAgentforECCI") + "-04-" + originalBl.getFileNo();
                        }
                    } else {
                        if ("02".equals(companyCode)) {
                            BolNo = (String) messageResources.getMessage("defaultAgent") + "-" + BolNo;
                        } else {
                            BolNo = (String) messageResources.getMessage("defaultAgentforECCI") + "-" + BolNo;
                        }
                    }
                    if ("yes".equalsIgnoreCase(originalBl.getOmit2LetterCountryCode())) {
                        BolNo = BolNo.substring(0, BolNo.indexOf("-") + 1) + BolNo.substring(BolNo.indexOf("-") + 3);
                    }
                }
                cell = makeCellLeftNoBorderPalatinoFclBl(BolNo, palatinoRomanMediumFont);
            }
            cell.setBorderWidthBottom(0.6f);
            documentNoTable.addCell(cell);
            mainCell.addElement(documentNoTable);
            PdfPTable exportReferenceTable = makeTable(1);
            exportReferenceTable.setWidthPercentage(100);
            String exportReferenceMorelength = "";
            if (simpleRequest != null && (simpleRequest.equalsIgnoreCase("fclArrivalNotice") || "fclArrivalNoticeNonRated".equalsIgnoreCase(simpleRequest))) {
                cell = makeCellLeftNoBorderFclBL("MASTER BILL OF LADING");
                cell.setLeading(0.7f, 0.7f);
                exportReferenceTable.addCell(cell);
                cell = makeCellLeftNoBorderPalatinoFclBl(originalBl.getNewMasterBL() != null ? "MBL# " + originalBl.getNewMasterBL() : "", palatinoRomanMediumFont);
                exportReferenceTable.addCell(cell);
                cell = makeCellLeftNoBorderPalatinoFclBl(originalBl.getImportAMSHouseBl() != null ? "AMS HBL# " + originalBl.getImportAMSHouseBl() : "", palatinoRomanMediumFont);
                exportReferenceTable.addCell(cell);
                cell = makeCellLeftNoBorderPalatinoFclBl(originalBl.getImportOrginBlno() != null ? "Sub-House BL# " + originalBl.getImportOrginBlno() : "", palatinoRomanMediumFont);
                exportReferenceTable.addCell(cell);
                Set inbondDetails = originalBl.getFclInbondDetails();
                String inbond = "";
                if (null != originalBl.getFclInbondDetails()) {
                    for (Iterator iterator = inbondDetails.iterator(); iterator.hasNext();) {
                        StringBuilder sb = new StringBuilder();
                        FclInbondDetails fclInbondDetails = (FclInbondDetails) iterator.next();
                        if (CommonUtils.isNotEmpty(fclInbondDetails.getInbondType())) {
                            sb.append(fclInbondDetails.getInbondType()).append(" ");
                        }
                        if (CommonUtils.isNotEmpty(fclInbondDetails.getInbondNumber())) {
                            sb.append(fclInbondDetails.getInbondNumber()).append(" ");
                        }
                        if (null != fclInbondDetails.getInbondDate()) {
                            sb.append("Dated ");
                            sb.append(DateUtils.formatDate(fclInbondDetails.getInbondDate(), "yyyy-MM-dd")).append(" ");
                        }
                        if (CommonUtils.isNotEmpty(fclInbondDetails.getInbondPort())) {
                            sb.append(fclInbondDetails.getInbondPort());
                        }
                        cell = makeCellLeftNoBorderPalatinoFclBl(sb.toString(), palatinoRomanMediumFont);
                        exportReferenceTable.addCell(cell);
                    }
                    cell.setMinimumHeight(40);
                } else {
                    cell = makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanMediumFont);
                    exportReferenceTable.addCell(cell);
                    cell.setMinimumHeight(40);
                }
                mainCell.addElement(exportReferenceTable);
            } else {
                cell = makeCellLeftNoBorderFclBL("EXPORT REFERENCES");
                cell.setLeading(0.7f, 0.7f);
                exportReferenceTable.addCell(cell);
                exportReferenceMorelength = originalBl.getExportReference() != null ? originalBl.getExportReference() : "";
                cell = makeCellLeftNoBorderPalatinoFclBl(exportReferenceMorelength.length() > 250 ? exportReferenceMorelength.substring(0,250) : exportReferenceMorelength, palatinoRomanMediumFont);
                cell.setMinimumHeight(40);
                cell.setExtraParagraphSpace(2f);
                exportReferenceTable.addCell(cell);
                mainCell.addElement(exportReferenceTable);
            }

            detailsTable.addCell(mainCell);
            // Consignee table
            mainCell = makeCellleftNoBorderWithBoldFont("");
            mainCell.setBorderWidthLeft(0.0f);
            mainCell.setBorderWidthRight(0.6f);
            mainCell.setBorderWidthBottom(0.6f);
            mainCell.setBorderWidthTop(0.0f);
            PdfPTable consigneeTable = makeTable(1);
            consigneeTable.setWidthPercentage(100);
            if (importFlag && ("fclArrivalNotice".equalsIgnoreCase(simpleRequest) || "fclArrivalNoticeNonRated".equalsIgnoreCase(simpleRequest))) {
                cell = makeCellLeftNoBorderFclBL("HOUSE CONSIGNEE");
            } else {
                cell = makeCellLeftNoBorderFclBL("CONSIGNEE");
            }
            cell.setLeading(0.5f, 0.5f);
            consigneeTable.addCell(cell);
            String houseConsigneeAddr = "";
            String masterConsigneeAddr = "";
            if (simpleRequest != null && (simpleRequest.equalsIgnoreCase("UnfreightedNonNegotiable") || simpleRequest.equalsIgnoreCase("UnfreightedOriginalBl")
                    || simpleRequest.equalsIgnoreCase("freightedOriginalBl") || simpleRequest.equalsIgnoreCase("freightedNonNegotiable")
                    || simpleRequest.equalsIgnoreCase("fclArrivalNotice") || "fclArrivalNoticeNonRated".equalsIgnoreCase(simpleRequest) || simpleRequest.equalsIgnoreCase("UnmarkedHouseBillofLading"))) {
                houseConsigneeAddr = null != originalBl.getConsigneeAddress() && !originalBl.getConsigneeAddress().trim().equals(",") ? originalBl.getConsigneeAddress().trim() : "";
                houseConsigneeAddr = trimShipperConsigNotifyForwardAddress(houseConsigneeAddr);
                StringBuilder consigneeDetails = new StringBuilder();
                consigneeDetails.append(originalBl.getConsigneeName() != null && !originalBl.getConsigneeName().trim().equals(",") ? originalBl.getConsigneeName().trim() : "");
                consigneeDetails.append("\n");
                consigneeDetails.append(houseConsigneeAddr);
                cell = makeCellLeftNoBorderPalatinoFclBl(consigneeDetails.toString(), palatinoRomanMediumFont);
                cell.setExtraParagraphSpace(2f);
                consigneeTable.addCell(cell);

            } else if (simpleRequest != null && (simpleRequest.equalsIgnoreCase("UnfreightedMasterBl") || simpleRequest.equalsIgnoreCase("freightedMasterBl"))) {
                masterConsigneeAddr = null != originalBl.getHouseConsigneeAddress() && !originalBl.getHouseConsigneeAddress().trim().equals(",") ? originalBl.getHouseConsigneeAddress().trim() : "";
                masterConsigneeAddr = trimShipperConsigNotifyForwardAddress(masterConsigneeAddr);
                StringBuilder consigneeDetails = new StringBuilder();
                consigneeDetails.append(originalBl.getHouseConsigneeName() != null && !originalBl.getHouseConsigneeName().trim().equals(",") ? originalBl.getHouseConsigneeName().trim() : "");
                consigneeDetails.append("\n");
                consigneeDetails.append(masterConsigneeAddr);
                cell = makeCellLeftNoBorderPalatinoFclBl(consigneeDetails.toString(), palatinoRomanMediumFont);
                cell.setExtraParagraphSpace(2f);
                consigneeTable.addCell(cell);
            }
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
            cell.setLeading(0.5f, 0.5f);
            forwardingTable.addCell(cell);
            StringBuilder forwardingAgentDetails = new StringBuilder();
            String forwardAgent = "";
            if (null != bl.getForwardingAgentName() && !bl.getForwardingAgentName().trim().equalsIgnoreCase("NO FF ASSIGNED".trim())
                    && !bl.getForwardingAgentName().trim().equalsIgnoreCase("NO FF ASSIGNED / B/L PROVIDED".trim())
                    && !bl.getForwardingAgentName().trim().equalsIgnoreCase("NO FRT. FORWARDER ASSIGNED".trim())) {
                forwardAgent = null != bl.getForwardingAgent() ? bl.getForwardingAgent().trim() : "";
//                forwardAgent = trimShipperConsigNotifyForwardAddress(forwardAgent);
                if (bl.getBolId().indexOf("==") != -1 && CommonUtils.isEmpty(bl.getForwardingAgent())) {
//                    forwardAgent = trimShipperConsigNotifyForwardAddress(getAddress(bl.getForwardAgentNo()));
                    forwardAgent = getAddress(bl.getForwardAgentNo());
                }
                forwardingAgentDetails.append(bl.getForwardingAgentName() != null ? bl.getForwardingAgentName() : "");
                forwardingAgentDetails.append("\n");
                forwardingAgentDetails.append(forwardAgent);
                List l = helperClass.wrapAddress(forwardingAgentDetails.toString());
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
            cell.setMinimumHeight(40);
            cell.setBorderWidthBottom(0.6f);
            cell.setExtraParagraphSpace(2f);
            cell.setNoWrap(true);
            forwardingTable.addCell(cell);
            mainCell.addElement(forwardingTable);
            PdfPTable pointAndCountryTable = makeTable(1);
            pointAndCountryTable.setWidthPercentage(100);
            cell = makeCellLeftNoBorderFclBL("POINT AND COUNTRY OF ORIGIN");
            cell.setLeading(0.7f, 0.7f);
            pointAndCountryTable.addCell(cell);
            if (null != originalBl.getCountryOfOrigin() && originalBl.getCountryOfOrigin().equalsIgnoreCase("Yes")) {
                cell = makeCellLeftNoBorderPalatinoFclBl(originalBl.getRoutedByAgentCountry() != null ? originalBl.getRoutedByAgentCountry()
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
            cell.setLeading(0.5f, 0.5f);
            notifyPartyTable.addCell(cell);
            String houseNotifyParty = "";
            String masterNotifyParty = "";
            if (simpleRequest != null && (simpleRequest.equalsIgnoreCase("UnfreightedNonNegotiable") || simpleRequest.equalsIgnoreCase("UnfreightedOriginalBl") || simpleRequest.equalsIgnoreCase("freightedOriginalBl") || simpleRequest.equalsIgnoreCase("freightedNonNegotiable") || simpleRequest.equalsIgnoreCase("fclArrivalNotice")
                    || simpleRequest.equalsIgnoreCase("fclArrivalNoticeNonRated") || simpleRequest.equalsIgnoreCase("UnmarkedHouseBillofLading"))) {
                houseNotifyParty = originalBl.getStreamshipNotifyParty() != null ? originalBl.getStreamshipNotifyParty().trim() : "";
//                houseNotifyParty = trimShipperConsigNotifyForwardAddress(houseNotifyParty);
                StringBuilder notifyPartyDetails = new StringBuilder();
                notifyPartyDetails.append(originalBl.getNotifyPartyName() != null ? originalBl.getNotifyPartyName() : "");
                notifyPartyDetails.append("\n");
                notifyPartyDetails.append(houseNotifyParty);
                cell = makeCellLeftNoBorderPalatinoFclBl(notifyPartyDetails.toString(), palatinoRomanMediumFont);
                cell.setExtraParagraphSpace(2f);
                cell.setMinimumHeight(50);
                notifyPartyTable.addCell(cell);
                List l = helperClass.wrapAddress(notifyPartyDetails.toString());
                if (l.isEmpty() || l.size() < 5) {
                    ADDRESS_SIZE += 4;
                } else {
                    ADDRESS_SIZE += l.size();
                }

            } else if (simpleRequest != null && (simpleRequest.equalsIgnoreCase("UnfreightedMasterBl") || simpleRequest.equalsIgnoreCase("freightedMasterBl"))) {
                masterNotifyParty = originalBl.getHouseNotifyParty() != null ? originalBl.getHouseNotifyParty().trim() : "";
//                masterNotifyParty = trimShipperConsigNotifyForwardAddress(masterNotifyParty);
                StringBuilder notifyPartyDetails = new StringBuilder();
                notifyPartyDetails.append(originalBl.getHouseNotifyPartyName() != null ? originalBl.getHouseNotifyPartyName() : "");
                notifyPartyDetails.append("\n");
                notifyPartyDetails.append(masterNotifyParty);
                cell = makeCellLeftNoBorderPalatinoFclBl(notifyPartyDetails.toString(), palatinoRomanMediumFont);
                cell.setExtraParagraphSpace(2f);
                cell.setMinimumHeight(50);
                notifyPartyTable.addCell(cell);
                List l = helperClass.wrapAddress(notifyPartyDetails.toString());
                if (l.isEmpty() || l.size() < 5) {
                    ADDRESS_SIZE += 4;
                } else {
                    ADDRESS_SIZE += l.size();
                }
            }
            if (ADDRESS_SIZE <= 16) {
                DESC_SIZE = 14;
            } else if (ADDRESS_SIZE > 23) {
                DESC_SIZE = 3;
            } else if (ADDRESS_SIZE > 22) {
                DESC_SIZE = 4;
            } else if (ADDRESS_SIZE > 21) {
                DESC_SIZE = 6;
            } else if (ADDRESS_SIZE > 20) {
                DESC_SIZE = 7;
            } else if (ADDRESS_SIZE > 19) {
                DESC_SIZE = 9;
            } else if (ADDRESS_SIZE > 18) {
                DESC_SIZE = 10;
            } else if (ADDRESS_SIZE > 17) {
                DESC_SIZE = 11;
            } else if (ADDRESS_SIZE > 16) {
                DESC_SIZE = 13;
            }
            mainCell.addElement(notifyPartyTable);
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
            cell.setLeading(0.5f, 0.5f);
            domesticTable.addCell(cell);
            if (!"fclArrivalNotice".equalsIgnoreCase(simpleRequest) && !"fclArrivalNoticeNonRated".equalsIgnoreCase(simpleRequest)) {
//                cell = makeCellLeftNoBorderPalatinoFclBl("Issued By  "+getIssuingTM(bl),palatinoRomanMediumFont);
                cell = makeCellLeftNoBorderPalatinoFclBl(originalBl.getDomesticRouting(), palatinoRomanMediumFont);
            } else {
                cell = makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanMediumFont);
            }
            cell.setMinimumHeight(50);
            cell.setExtraParagraphSpace(2f);
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
            if (originalBl.getHblPlaceReceiptOverride().equalsIgnoreCase("Yes")) {
                cell = makeCellLeftNoBorderPalatinoFclBl(originalBl.getHblPlaceReceipt().toUpperCase(), palatinoRomanMediumFont);
            } else {
                if (originalBl.getDoorOriginAsPlorHouse().equalsIgnoreCase("yes")) {
                    if ("fclArrivalNotice".equalsIgnoreCase(simpleRequest) || "fclArrivalNoticeNonRated".equalsIgnoreCase(simpleRequest)) {
                        cell = makeCellLeftNoBorderPalatinoFclBl(removeUnlocCodeAppendCountryName(fclBlBC.getDoorOriginForHouseBlPrint(originalBl)), palatinoRomanMediumFont);
                    } else {
                        cell = makeCellLeftNoBorderPalatinoFclBl(removeUnlocCode(fclBlBC.getDoorOriginForHouseBlPrint(originalBl)), palatinoRomanMediumFont);
                    }
                } else if (originalBl.getDoorOriginAsPlorHouse().equalsIgnoreCase("no")) {
                    cell = makeCellLeftNoBorderPalatinoFclBl(removeUnlocCode(""), palatinoRomanMediumFont);
                }
            }
            cell.setBorderWidthBottom(0.6f);
            cell.setMinimumHeight(15);
            placeofRecieptTable.addCell(cell);
            mainCell.addElement(placeofRecieptTable);

            PdfPTable exportingAndPol = makeTable(2);
            exportingAndPol.setWidths(new float[]{55, 45});
            exportingAndPol.setWidthPercentage(100);
            cell = makeCellLeftNoBorderFclBL("EXPORTING CARRIER(Vessel)(Flag)");
            cell.setLeading(0.7f, 0.7f);
            cell.setBorderWidthRight(0.6f);
            exportingAndPol.addCell(cell);
            // //
            if (simpleRequest != null && (simpleRequest.equalsIgnoreCase("fclArrivalNotice") || "fclArrivalNoticeNonRated".equalsIgnoreCase(simpleRequest))) {
                PdfPCell subCell = makeCellLeftNoBorderFclBL("");
                PdfPTable polETD = makeTable(2);
                polETD.setWidths(new float[]{63, 37});
                polETD.setWidthPercentage(100);
                cell = makeCellLeftNoBorderFclBL("PORT OF LOADING");
                cell.setLeading(0.5f, 0.5f);
                polETD.addCell(cell);
                cell = makeCellRightNoBorderFclBL("ETD");
                cell.setLeading(0.5f, 0.5f);
                polETD.addCell(cell);
                subCell.addElement(polETD);
                exportingAndPol.addCell(subCell);
            } else {
                cell = makeCellLeftNoBorderFclBL("PORT OF LOADING");
                cell.setLeading(0.7f, 0.7f);
                exportingAndPol.addCell(cell);
            }
            StringBuilder vesselVoyage = new StringBuilder();
            if (null == originalBl.getVesselNameCheck()) {
                vesselVoyage.append(originalBl.getVessel() != null ? (originalBl.getVessel().getCodedesc() != null ? originalBl.getVessel().getCodedesc() : "") : "");
            } else {
                vesselVoyage.append(originalBl.getManualVesselName() != null ? (originalBl.getManualVesselName() != null ? originalBl.getManualVesselName() : "") : "");
            }
            vesselVoyage.append(" ");
            vesselVoyage.append("V.");
            vesselVoyage.append(originalBl.getVoyages() != null ? originalBl.getVoyages() : "");
            cell = makeCellLeftNoBorderPalatinoFclBl(vesselVoyage.toString(), palatinoRomanSmallFont);
            cell.setBorderWidthRight(0.6f);
            cell.setBorderWidthBottom(0.6f);
            exportingAndPol.addCell(cell);
            // /
            if (simpleRequest != null && (simpleRequest.equalsIgnoreCase("fclArrivalNotice") || "fclArrivalNoticeNonRated".equalsIgnoreCase(simpleRequest))) {
                PdfPCell subCell = makeCellLeftNoBorderFclBL("");
                PdfPTable polETD = makeTable(2);
                polETD.setWidths(new float[]{60, 40});
                polETD.setWidthPercentage(100);
                String portOfLoading = "";
                if ("yes".equalsIgnoreCase(originalBl.getHblPOLOverride())) {
                    portOfLoading = originalBl.getHblPOL() != null ? originalBl.getHblPOL().toUpperCase() : "";
                } else {
                    portOfLoading = removeUnlocCodeAppendCountryName(originalBl.getPortOfLoading() != null ? originalBl.getPortOfLoading() : "");
                }
                cell = makeCellLeftNoBorderPalatinoFclBl(portOfLoading, palatinoRomanSmallFont);
                polETD.addCell(cell);
                String etd = originalBl.getSailDate() != null ? sdf.format(originalBl.getSailDate()).toString() : "";
                cell = makeCellRightNoBorderPalatinoFclBl(etd, palatinoRomanSmallFont);
                polETD.addCell(cell);
                subCell.addElement(polETD);
                subCell.setBorderWidthBottom(0.6f);
                exportingAndPol.addCell(subCell);
            } else {
                if ("yes".equalsIgnoreCase(originalBl.getHblPOLOverride())) {
                    cell = makeCellLeftNoBorderPalatinoWithUnderline(originalBl.getHblPOL() != null ? originalBl.getHblPOL().toUpperCase()
                            : "", palatinoRomanSmallFont);
                } else {
                    cell = makeCellLeftNoBorderPalatinoFclBl(removeUnlocCode(originalBl.getPortOfLoading() != null ? originalBl.getPortOfLoading() : ""), palatinoRomanSmallFont);
                }
                cell.setBorderWidthBottom(0.6f);
                exportingAndPol.addCell(cell);
            }
            mainCell.addElement(exportingAndPol);

            PdfPTable seaPortFinalyDelivery = makeTable(2);
            seaPortFinalyDelivery.setWidths(new float[]{55, 45});
            seaPortFinalyDelivery.setWidthPercentage(100);
            if ("fclArrivalNotice".equalsIgnoreCase(simpleRequest) || "fclArrivalNoticeNonRated".equalsIgnoreCase(simpleRequest)) {
                PdfPCell subCell = makeCellLeftNoBorderFclBL("");
                PdfPTable podETA = makeTable(2);
                podETA.setWidths(new float[]{70, 30});
                podETA.setWidthPercentage(100);
                cell = makeCellLeftNoBorderFclBL("SEA PORT OF DISCHARGE");
                cell.setLeading(0.5f, 0.5f);
                podETA.addCell(cell);
                cell = makeCellRightNoBorderFclBL("ETA");
                cell.setLeading(0.5f, 0.5f);
                podETA.addCell(cell);
                subCell.addElement(podETA);
                subCell.setBorderWidthRight(0.6f);
                seaPortFinalyDelivery.addCell(subCell);
            } else {
                PdfPCell subCell = makeCellLeftNoBorderFclBL("");
                PdfPTable podETA = makeTable(1);
                podETA.setWidthPercentage(100);
                cell = makeCellLeftNoBorderFclBL("SEA PORT OF DISCHARGE");
                cell.setLeading(0.5f, 0.5f);
                podETA.addCell(cell);
                subCell.addElement(podETA);
                subCell.setBorderWidthRight(0.6f);
                seaPortFinalyDelivery.addCell(subCell);
            }
            if ("fclArrivalNotice".equalsIgnoreCase(simpleRequest) || "fclArrivalNoticeNonRated".equalsIgnoreCase(simpleRequest)) {
                PdfPCell subCell = makeCellLeftNoBorderFclBL("");
                PdfPTable podETA = makeTable(2);
                podETA.setWidths(new float[]{65, 35});
                podETA.setWidthPercentage(100);
                cell = makeCellLeftNoBorderFclBL("FINAL DELIVERY TO");
                cell.setLeading(0.5f, 0.5f);
                podETA.addCell(cell);
                cell = makeCellRightNoBorderFclBL("ETA at FD");
                cell.setLeading(0.5f, 0.5f);
                podETA.addCell(cell);
                subCell.addElement(podETA);
                subCell.setBorderWidthRight(0.6f);
                seaPortFinalyDelivery.addCell(subCell);
            } else {
                PdfPCell subCell = makeCellLeftNoBorderFclBL("");
                PdfPTable podETA = makeTable(1);
                podETA.setWidthPercentage(100);
                cell = makeCellLeftNoBorderFclBL("FINAL DELIVERY TO");
                cell.setLeading(0.5f, 0.5f);
                podETA.addCell(cell);
                subCell.addElement(podETA);
                subCell.setBorderWidthRight(0.6f);
                seaPortFinalyDelivery.addCell(subCell);
            }
            if ("fclArrivalNotice".equalsIgnoreCase(simpleRequest) || "fclArrivalNoticeNonRated".equalsIgnoreCase(simpleRequest)) {
                PdfPCell subCell = makeCellLeftNoBorderFclBL("");
                PdfPTable podETA = makeTable(2);
                podETA.setWidths(new float[]{65, 35});
                podETA.setWidthPercentage(100);
                String portOfDischarge = "";
                if ("yes".equalsIgnoreCase(originalBl.getHblPODOverride())) {
                    portOfDischarge = originalBl.getHblPOD() != null ? originalBl.getHblPOD().toUpperCase() : "";
                } else {
                    portOfDischarge = removeUnlocCodeAppendCountryName(originalBl.getPortofDischarge() != null ? originalBl.getPortofDischarge() : "");
                }
                cell = makeCellLeftNoBorderPalatinoFclBl(portOfDischarge, palatinoRomanSmallFont);
                podETA.addCell(cell);
                String eta = originalBl.getEta() != null ? sdf.format(originalBl.getEta()) : "\n";
                cell = makeCellRightNoBorderPalatinoFclBl(eta, palatinoRomanSmallFont);
                podETA.addCell(cell);
                subCell.addElement(podETA);
                subCell.setBorderWidthRight(0.6f);
                seaPortFinalyDelivery.addCell(subCell);
            } else {
                if ("yes".equalsIgnoreCase(originalBl.getHblPODOverride())) {
                    cell = makeCellLeftNoBorderPalatinoWithUnderline(originalBl.getHblPOD() != null ? originalBl.getHblPOD().toUpperCase()
                            : "", palatinoRomanSmallFont);
                } else {
                    cell = makeCellLeftNoBorderPalatinoFclBl(removeUnlocCode(originalBl.getPortofDischarge() != null ? originalBl.getPortofDischarge() : ""), palatinoRomanSmallFont);
                }
                cell.setBorderWidthRight(0.6f);
                seaPortFinalyDelivery.addCell(cell);
            }
            if ("fclArrivalNotice".equalsIgnoreCase(simpleRequest) || "fclArrivalNoticeNonRated".equalsIgnoreCase(simpleRequest)) {
                PdfPCell fdCell = makeCellLeftNoBorderFclBL("");
                PdfPTable fdEta = makeTable(2);
                fdEta.setWidths(new float[]{65, 35});
                fdEta.setWidthPercentage(100);
                // Get Final delivery based on these prefernces 1. House BL Override value 2. Door Dest 3. Destination
                String finalDelivery = "";
                if (CommonUtils.isNotEmpty(originalBl.getDoorOfOrigin())) {
                    finalDelivery = originalBl.getDoorOfOrigin();
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
                fdEta.addCell(cell);
                String eta = originalBl.getEtaFd() != null ? sdf.format(originalBl.getEtaFd()) : "\n";
                if (!"".equals(finalDelivery)) {
                    cell = makeCellRightNoBorderPalatinoFclBl(eta, palatinoRomanSmallFont);
                    fdEta.addCell(cell);
                    fdCell.addElement(fdEta);
                }
                seaPortFinalyDelivery.addCell(fdCell);
            } else {
                // Get Final delivery based on these prefernces 1. House BL Override value 2. Door Dest 3. Destination
                String finalDelivery = "";
                if (CommonUtils.isNotEmpty(originalBl.getDoorOfDestination())) {
                    finalDelivery = originalBl.getDoorOfDestination();
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
                cell = makeCellLeftNoBorderPalatinoFclBl(null != portName ? portName : "", palatinoRomanSmallFont);
                seaPortFinalyDelivery.addCell(cell);
            }
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
            if ("fclArrivalNotice".equalsIgnoreCase(simpleRequest) || "fclArrivalNoticeNonRated".equalsIgnoreCase(simpleRequest)) {
                cell = makeCellLeftNoBorderFclBL("CARGO LOCATION");
            } else {
                cell = makeCellLeftNoBorderFclBL("ONWARD INLAND ROUTING");
            }

            cell.setLeading(0.5f, 0.5f);
            onwardInlandRouting.addCell(cell);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPTable upperTable = makeTable(1);
            upperTable.setWidthPercentage(100);
            PdfPCell inlandCell = makeCellLeftNoBorderPalatinoFclBl(originalBl.getOnwardInlandRouting() != null ? originalBl.getOnwardInlandRouting()
                    : "", palatinoRomanSmallFont);
            upperTable.addCell(inlandCell);
            cell.addElement(upperTable);

            if (originalBl.getShipperLoadsAndCounts() != null && originalBl.getShipperLoadsAndCounts().equalsIgnoreCase("yes")) {
                PdfPTable innerTable = makeTable(1);
                innerTable.setWidthPercentage(100);
                PdfPCell innerCell = makeCellLeftNoBorderFclBL("");
                Chunk chunkStow = new Chunk(
                        PrintReportsConstants.SHIPPER_LOAD_STOW_COUNT);
                chunkStow.setFont(new Font(Font.HELVETICA, 13, Font.BOLD,
                        Color.BLACK));
                chunkStow.setTextRenderMode(PdfContentByte.TEXT_RENDER_MODE_STROKE,
                        0.3f, Color.black);
                innerCell.addElement(chunkStow);
                innerCell.setHorizontalAlignment(Element.ALIGN_BOTTOM);
                innerCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                innerCell.setPaddingTop(25);
                innerTable.addCell(innerCell);
                cell.addElement(innerTable);
                cell.setNoWrap(true);
                cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
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
        } catch (Exception ex) {
            throw new ExceptionConverter(ex);
        }
    }

    public PdfPTable getChargesTable(FclBl bl, MessageResources messageResources, String contextPath, String simpleRequest) {
        try {
            PdfPCell cell;
            BaseFont palationRomanBase = BaseFont.createFont(contextPath + "/ttf/Palatino-Roman.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font palatinoRomanSmallFont = new Font(palationRomanBase, 8, Font.NORMAL, Color.BLACK);
            NumberFormat numberFormat = new DecimalFormat("###,###,##0.00");
            PdfPTable ratesTable = makeTable(2);
            ratesTable.setWidthPercentage(100);
            if ("fclArrivalNotice".equalsIgnoreCase(simpleRequest)) {
                ratesTable.setWidths(new float[]{50, 50});
            } else {
                ratesTable.setWidths(new float[]{50, 50});
            }
            double total = 0.00;
            double prepaidTotal = 0.00;
            double collectTotal = 0.00;
            List tempList = new ArrayList();
            List chargeListTemp = new ArrayList();
            Set<FclBlCharges> fclChargesSet = bl.getFclcharge();
            for (Iterator iter = fclChargesSet.iterator(); iter.hasNext();) {
                FclBlCharges fclBlCharges = (FclBlCharges) iter.next();
                tempList.add(fclBlCharges);
            }
            boolean importFlag = "I".equalsIgnoreCase(bl.getImportFlag());
            if ("I".equalsIgnoreCase(bl.getImportFlag())) {
                chargeListTemp = fclBlBC.consolidateRates(fclBlBC.getSortedList(tempList), messageResources, importFlag);
            } else {
                if (CommonUtils.in(simpleRequest, "freightedNonNegotiable", "freightedOriginalBl", "UnmarkedHouseBillofLading")) {
                    chargeListTemp = new PrintConfigBC().getExportRatesForFclPrintForBounleOfr(tempList, messageResources, null);
                } else {
                    chargeListTemp = new PrintConfigBC().getExportRatesForFclPrint(fclBlBC.getSortedList(tempList), messageResources);
                }
            }
            List chargeList = fclBlBC.getCorrectedList(chargeListTemp);
            PdfPTable ratesSubTable;
            PdfPTable ratesSubTable1;
            if ("fclArrivalNotice".equalsIgnoreCase(simpleRequest)) {
                ratesSubTable = makeTable(3);
                ratesSubTable1 = makeTable(3);
                ratesSubTable.setWidthPercentage(100);
                ratesSubTable.setWidths(new float[]{62f, 13f, 25f});
                ratesSubTable1.setWidthPercentage(100);
                ratesSubTable1.setWidths(new float[]{62f, 13f, 25f});
            } else {
                ratesSubTable = makeTable(3);
                ratesSubTable1 = makeTable(3);
                ratesSubTable.setWidthPercentage(103);
                ratesSubTable.setWidths(new float[]{59f, 13, 31f});
                ratesSubTable1.setWidthPercentage(103);
                ratesSubTable1.setWidths(new float[]{59f, 13, 31f});
            }

            int subTableSize = 0;
            int collectSize = 0;
            int prepaidSize = 0;
            String billTo = "";
            StringBuilder collectString = new StringBuilder();
            StringBuilder prepaidString = new StringBuilder();
            StringBuilder paymentString = new StringBuilder("");
            boolean billToBoth = false;
            boolean billToConsignee = false;
            if (bl.getHouseBl() != null && (bl.getHouseBl().equalsIgnoreCase("B-Both") || bl.getHouseBl().equalsIgnoreCase("B"))) {
                billToBoth = true;
            }
            if ("Yes".equalsIgnoreCase(bl.getCollectThirdParty())) {
                billToBoth = false;
            }
            for (Iterator iter = chargeList.iterator(); iter.hasNext();) {
                FclBlCharges fclBlCharges = (FclBlCharges) iter.next();
                if (fclBlCharges.getPrintOnBl() != null && !fclBlCharges.getPrintOnBl().equalsIgnoreCase("no")) {
                    if ("fclArrivalNotice".equalsIgnoreCase(simpleRequest)) {
                        if ("Consignee".equalsIgnoreCase(fclBlCharges.getBillTo())) {
                            billToConsignee = true;
                        }
                    }
                }
            }
            //boolean table = true;
            int count = 0;
            boolean subTable = false;
            for (Iterator iter = chargeList.iterator(); iter.hasNext();) {
                FclBlCharges fclBlCharges = (FclBlCharges) iter.next();
                if (fclBlCharges.getPrintOnBl() != null && !fclBlCharges.getPrintOnBl().equalsIgnoreCase("no")) {
                    if ("fclArrivalNotice".equalsIgnoreCase(simpleRequest)) {
                        if ("Consignee".equalsIgnoreCase(fclBlCharges.getBillTo()) || "C-Collect".equalsIgnoreCase(bl.getHouseBl())) {
                            collectSize++;
                            collectTotal = collectTotal + (fclBlCharges.getAmount() != null ? fclBlCharges.getAmount() : 0.00);
                        }
                    } else {
                        if ("Agent".equalsIgnoreCase(fclBlCharges.getBillTo())) {
                            billTo = "COL";
                            collectSize++;
                            collectTotal = collectTotal + (fclBlCharges.getAmount() != null ? fclBlCharges.getAmount() : 0.00);
                        } else {
                            billTo = "PPD";
                            prepaidSize++;
                            prepaidTotal = prepaidTotal + (fclBlCharges.getAmount() != null ? fclBlCharges.getAmount() : 0.00);
                        }
                    }

                    if ("Yes".equalsIgnoreCase(bl.getCollectThirdParty())) {
                        billTo = "COL";
                    }
                    total = total + (fclBlCharges.getAmount() != null ? fclBlCharges.getAmount() : 0.00);
                    if ("fclArrivalNotice".equalsIgnoreCase(simpleRequest)) {
                        if ("Consignee".equalsIgnoreCase(fclBlCharges.getBillTo()) || "C-Collect".equalsIgnoreCase(bl.getHouseBl())) {
                            if (count < 6) {
                                cell = makeCellLeftNoBorderPalatinoFclBl(fclBlCharges.getCharges() != null ? fclBlCharges.getCharges() : "", palatinoRomanSmallFont);
                                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                                cell.setNoWrap(true);
                                ratesSubTable.addCell(cell);
                                cell = makeCellLeftNoBorderPalatinoFclBl("US$", palatinoRomanSmallFont);
                                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                                cell.setNoWrap(true);
                                ratesSubTable.addCell(cell);
                                cell = makeCellLeftNoBorderPalatinoFclBl(fclBlCharges.getAmount() != null ? numberFormat.format(fclBlCharges.getAmount()).toString()
                                        : "0.00", palatinoRomanSmallFont);
                                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                cell.setNoWrap(true);
                                ratesSubTable.addCell(cell);
                                subTableSize++;
                                count++;
                                //table = false;

                            } else if (count > 5) {
                                cell = makeCellLeftNoBorderPalatinoFclBl(fclBlCharges.getCharges() != null ? fclBlCharges.getCharges() : "", palatinoRomanSmallFont);
                                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                                cell.setNoWrap(true);
                                ratesSubTable1.addCell(cell);
                                cell = makeCellLeftNoBorderPalatinoFclBl("US$", palatinoRomanSmallFont);
                                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                                cell.setNoWrap(true);
                                ratesSubTable1.addCell(cell);
                                cell = makeCellLeftNoBorderPalatinoFclBl(fclBlCharges.getAmount() != null ? numberFormat.format(fclBlCharges.getAmount()).toString()
                                        : "0.00", palatinoRomanSmallFont);
                                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                cell.setNoWrap(true);
                                ratesSubTable1.addCell(cell);
                                subTableSize++;
                                count++;
                                //table = true;
                            }
                        }
                    } else {
                        subTableSize++;
                        if (billToBoth) {
                            if (collectSize > 6) {
                                cell = makeCellLeftNoBorderPalatinoFclBl(fclBlCharges.getCharges() != null ? fclBlCharges.getCharges() : "", palatinoRomanSmallFont);
                                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                                cell.setNoWrap(true);
                                ratesSubTable1.addCell(cell);
                                cell = makeCellLeftNoBorderPalatinoFclBl("US$", palatinoRomanSmallFont);
                                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                                cell.setNoWrap(true);
                                ratesSubTable1.addCell(cell);
                                cell = makeCellLeftNoBorderPalatinoFclBl(fclBlCharges.getAmount() != null ? numberFormat.format(fclBlCharges.getAmount()).toString() + " " + billTo
                                        : "0.00", palatinoRomanSmallFont);
                                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                cell.setNoWrap(true);
                                ratesSubTable1.addCell(cell);
                            } else if (prepaidSize > 6) {
                                cell = makeCellLeftNoBorderPalatinoFclBl(fclBlCharges.getCharges() != null ? fclBlCharges.getCharges() : "", palatinoRomanSmallFont);
                                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                                cell.setNoWrap(true);
                                ratesSubTable.addCell(cell);
                                cell = makeCellLeftNoBorderPalatinoFclBl("US$", palatinoRomanSmallFont);
                                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                                cell.setNoWrap(true);
                                ratesSubTable.addCell(cell);
                                cell = makeCellLeftNoBorderPalatinoFclBl(fclBlCharges.getAmount() != null ? numberFormat.format(fclBlCharges.getAmount()).toString() + " " + billTo
                                        : "0.00", palatinoRomanSmallFont);
                                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                cell.setNoWrap(true);
                                ratesSubTable.addCell(cell);
                            } else {
                                if ("COL".equalsIgnoreCase(billTo)) {
                                    cell = makeCellLeftNoBorderPalatinoFclBl(fclBlCharges.getCharges() != null ? fclBlCharges.getCharges() : "", palatinoRomanSmallFont);
                                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                                    cell.setNoWrap(true);
                                    ratesSubTable.addCell(cell);
                                    cell = makeCellLeftNoBorderPalatinoFclBl("US$", palatinoRomanSmallFont);
                                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                                    cell.setNoWrap(true);
                                    ratesSubTable.addCell(cell);
                                    cell = makeCellLeftNoBorderPalatinoFclBl(fclBlCharges.getAmount() != null ? numberFormat.format(fclBlCharges.getAmount()).toString() + " " + billTo
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
                                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                                    cell.setNoWrap(true);
                                    ratesSubTable1.addCell(cell);
                                    cell = makeCellLeftNoBorderPalatinoFclBl(fclBlCharges.getAmount() != null ? numberFormat.format(fclBlCharges.getAmount()).toString() + " " + billTo
                                            : "0.00", palatinoRomanSmallFont);
                                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                    cell.setNoWrap(true);
                                    ratesSubTable1.addCell(cell);
                                }
                            }
                        } else {
                            if (subTableSize < 7) {
                                cell = makeCellLeftNoBorderPalatinoFclBl(fclBlCharges.getCharges() != null ? fclBlCharges.getCharges() : "", palatinoRomanSmallFont);
                                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                                cell.setNoWrap(true);
                                ratesSubTable.addCell(cell);
                                cell = makeCellLeftNoBorderPalatinoFclBl("US$", palatinoRomanSmallFont);
                                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                                cell.setNoWrap(true);
                                ratesSubTable.addCell(cell);
                                cell = makeCellLeftNoBorderPalatinoFclBl(fclBlCharges.getAmount() != null ? numberFormat.format(fclBlCharges.getAmount()).toString() + " " + billTo
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
                                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                                cell.setNoWrap(true);
                                ratesSubTable1.addCell(cell);
                                cell = makeCellLeftNoBorderPalatinoFclBl(fclBlCharges.getAmount() != null ? numberFormat.format(fclBlCharges.getAmount()).toString() + " " + billTo
                                        : "0.00", palatinoRomanSmallFont);
                                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                cell.setNoWrap(true);
                                ratesSubTable1.addCell(cell);
                            }
                        }

                    }

                }
            }
            //      ---------------------charges-----^
            if ("fclArrivalNotice".equalsIgnoreCase(simpleRequest)) {
                if (bl.getHouseBl() != null && (bl.getHouseBl().equalsIgnoreCase("P-Prepaid") || bl.getHouseBl().equalsIgnoreCase("P"))) {
                    if ("Yes".equalsIgnoreCase(bl.getCollectThirdParty())) {
                        collectString.append("COLLECT TOTAL              US$   " + "$").append(numberFormat.format(total).toString());
                        chargesTotal = total;
                    } else {
                        if (collectTotal > 0) {
                            collectString.append("COLLECT TOTAL              US$   " + "$").append(numberFormat.format(collectTotal).toString());
                        }
                    }
                } else if (bl.getHouseBl() != null && (bl.getHouseBl().equalsIgnoreCase("C-Collect") || bl.getHouseBl().equalsIgnoreCase("C"))) {
                    collectString.append("COLLECT TOTAL              US$   " + "$").append(numberFormat.format(collectTotal).toString());
                } else if (bl.getHouseBl() != null && (bl.getHouseBl().equalsIgnoreCase("B-Both") || bl.getHouseBl().equalsIgnoreCase("B"))) {
                    if (collectTotal > 0) {
                        collectString.append("COLLECT TOTAL              US$   " + "$").append(numberFormat.format(collectTotal).toString());
                    }
                } else {
                    collectString.append("TOTAL US$   " + "$").append(numberFormat.format(total).toString());
                    chargesTotal = total;
                }
                if ("Yes".equalsIgnoreCase(bl.getCollectThirdParty())) {
                    collectString = new StringBuilder("");
                    collectString.append("COLLECT TOTAL              US$   " + "$").append(numberFormat.format(total).toString());
                    chargesTotal = total;
                }

            } else {
                if (bl.getHouseBl() != null && (bl.getHouseBl().equalsIgnoreCase("P-Prepaid") || bl.getHouseBl().equalsIgnoreCase("P"))) {
                    if ("Yes".equalsIgnoreCase(bl.getCollectThirdParty())) {
                        collectString.append("COLLECT TOTAL US $   " + "$").append(numberFormat.format(total).toString());
                        chargesTotal = total;
                    } else {
                        if (collectTotal > 0) {
                            collectString.append("COLLECT TOTAL US $   " + "$").append(numberFormat.format(collectTotal).toString());
                        }
                        if (prepaidTotal > 0) {
                            prepaidString.append("PREPAID TOTAL US $   " + "$").append(numberFormat.format(prepaidTotal).toString());
                        }
                        //chargesTotal = collectTotal + prepaidTotal;//
                    }
                } else if (bl.getHouseBl() != null && (bl.getHouseBl().equalsIgnoreCase("C-Collect") || bl.getHouseBl().equalsIgnoreCase("C"))) {
                    collectString.append("COLLECT TOTAL US $   " + "$").append(numberFormat.format(collectTotal).toString());
                } else if (bl.getHouseBl() != null && (bl.getHouseBl().equalsIgnoreCase("B-Both") || bl.getHouseBl().equalsIgnoreCase("B"))) {
                    if (collectTotal > 0) {
                        collectString.append("COLLECT TOTAL US $   " + "$").append(numberFormat.format(collectTotal).toString());
                    }
                    if (prepaidTotal > 0) {
                        prepaidString.append("PREPAID TOTAL US $   " + "$").append(numberFormat.format(prepaidTotal).toString());
                    }
                    //chargesTotal = collectTotal + prepaidTotal;//
                } else {
                    collectString.append("TOTAL US $   " + "$").append(numberFormat.format(total).toString());
                    chargesTotal = total;
                }
                if ("Yes".equalsIgnoreCase(bl.getCollectThirdParty())) {
                    collectString = new StringBuilder("");
                    collectString.append("COLLECT TOTAL US $   " + "$").append(numberFormat.format(total).toString());
                    chargesTotal = total;
                }
            }

            if (prepaidTotal > 0 && collectTotal > 0) {
                billToBoth = true;
            }

            PdfPTable paymentReleaseTable = makeTable(5);
            paymentReleaseTable.setWidthPercentage(100);
            paymentReleaseTable.setWidths(new float[]{31f, 5f, 15f, 4f, 49f});
            double paymentAmount = 0;
            if ("fclArrivalNotice".equalsIgnoreCase(simpleRequest)) {
                Set importReleaseSet = bl.getImportPaymentRelease();
                boolean first = true;
                for (Object object : importReleaseSet) {
                    if (first) {
                        cell = makeCellLeftNoBorderFclBL("Paid Date");
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cell.setNoWrap(true);
                        paymentReleaseTable.addCell(cell);
                        cell = makeCellLeftNoBorderFclBL("Paid Amount");
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cell.setNoWrap(true);
                        cell.setColspan(2);
                        paymentReleaseTable.addCell(cell);
                        cell = makeCellLeftNoBorderFclBL("");
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cell.setNoWrap(true);
                        paymentReleaseTable.addCell(cell);
                        cell = makeCellLeftNoBorderFclBL("Paid By");
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cell.setNoWrap(true);
                        paymentReleaseTable.addCell(cell);
                        first = false;
                    }
                    PaymentRelease paymentRelease = (PaymentRelease) object;
                    String paidDate = paymentRelease.getPaidDate() != null ? sdf.format(paymentRelease.getPaidDate()).toString() : "";
                    cell = makeCellLeftNoBorderPalatinoFclBl(paidDate, palatinoRomanSmallFont);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell.setNoWrap(true);
                    paymentReleaseTable.addCell(cell);

                    cell = makeCellLeftNoBorderPalatinoFclBl("US$", palatinoRomanSmallFont);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell.setNoWrap(true);
                    paymentReleaseTable.addCell(cell);

                    cell = makeCellLeftNoBorderPalatinoFclBl(paymentRelease.getAmount() != null ? numberFormat.format(paymentRelease.getAmount()).toString()
                            : "0.00", palatinoRomanSmallFont);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    cell.setNoWrap(true);
                    paymentReleaseTable.addCell(cell);
                    cell = makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell.setNoWrap(true);
                    paymentReleaseTable.addCell(cell);
                    cell = makeCellLeftNoBorderPalatinoFclBl(paymentRelease.getPaidBy() != null ? paymentRelease.getPaidBy().toUpperCase() : "", palatinoRomanSmallFont);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell.setNoWrap(true);
                    paymentReleaseTable.addCell(cell);
                    paymentAmount = paymentAmount + paymentRelease.getAmount();
                }
                paymentString.append("PAYMENT TOTAL             US$   " + "$").append(numberFormat.format(paymentAmount).toString());
            }
            ratesTable.addCell(ratesSubTable);
            ratesTable.addCell(ratesSubTable1);
            cell = makeCellLeftWithRightBorder("");
            if ("fclArrivalNotice".equalsIgnoreCase(simpleRequest)) {
                if (collectTotal != 0d) {
                    cell = makeCellLeftNoBorderFclBL(collectString.toString());
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell.setColspan(2);
                    ratesTable.addCell(cell);
                }
            }
            cell.setBorderWidthRight(0.0f);
            cell.addElement(paymentReleaseTable);
            cell.setColspan(2);
            ratesTable.addCell(cell);

            if ("fclArrivalNotice".equalsIgnoreCase(simpleRequest)) {
                if (paymentAmount != 0d) {
                    cell = makeCellLeftNoBorderFclBL(paymentString.toString());
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell.setColspan(2);
                    ratesTable.addCell(cell);
                    double due = collectTotal - paymentAmount;
                    cell = makeCellLeftNoBorderFclBL("BALANCE DUE                 US$    $" + numberFormat.format(due).toString());
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell.setColspan(2);
                    ratesTable.addCell(cell);
                }

                chargesTotal = collectTotal;
            } else {
                if (billToBoth) {
                    cell = makeCellLeftNoBorderFclBL(collectString.toString());
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    float paddingTop = 60;
                    if (subTableSize < 7) {
                        for (int i = 1; i < 6; i++) {
                            if (i == subTableSize) {
                                cell.setPaddingTop(paddingTop);
                                break;
                            } else {
                                paddingTop = paddingTop - 10;
                            }
                        }
                    }
                    ratesTable.addCell(cell);
                    cell = makeCellLeftNoBorderFclBL(prepaidString.toString());
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    paddingTop = 60;
                    if (subTableSize < 7) {
                        for (int i = 1; i < 6; i++) {
                            if (i == subTableSize) {
                                cell.setPaddingTop(paddingTop);
                                break;
                            } else {
                                paddingTop = paddingTop - 10;
                            }
                        }
                    }
                    ratesTable.addCell(cell);
                } else {
                    if (collectTotal > 0) {
                        if (subTableSize < 7) {
                            cell = makeCellLeftNoBorderFclBL(collectString.toString());
                            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                            float paddingTop = 60;
                            for (int i = 1; i < 6; i++) {
                                if (i == subTableSize) {
                                    cell.setPaddingTop(paddingTop);
                                    break;
                                } else {
                                    paddingTop = paddingTop - 10;
                                }
                            }
                            cell.setColspan(2);
                            ratesTable.addCell(cell);
                        } else {
                            cell = makeCellLeftNoBorderFclBL(collectString.toString());
                            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            cell.setColspan(2);
                            ratesTable.addCell(cell);
                        }
                    } else {
                        if (subTableSize < 7) {
                            cell = makeCellLeftNoBorderFclBL(prepaidString.toString());
                            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                            float paddingTop = 60;
                            for (int i = 1; i < 6; i++) {
                                if (i == subTableSize) {
                                    cell.setPaddingTop(paddingTop);
                                    break;
                                } else {
                                    paddingTop = paddingTop - 10;
                                }
                            }
                            cell.setColspan(2);
                            ratesTable.addCell(cell);
                        } else {
                            cell = makeCellLeftNoBorderFclBL(prepaidString.toString());
                            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            cell.setColspan(2);
                            ratesTable.addCell(cell);
                        }
                    }

                }
            }
            //---------------------total-----^
            if ("fclArrivalNotice".equalsIgnoreCase(simpleRequest)) {
            } else {
                if (billToBoth) {
                    if (prepaidTotal != 0d) {
                        String totalString = numberFormat.format(prepaidTotal).replaceAll(",", "");
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
                        cell.setNoWrap(true);
                        ratesTable.addCell(cell);
                    }
                    if (collectTotal != 0d) {
                        String totalString1 = numberFormat.format(collectTotal).replaceAll(",", "");
                        int index1 = totalString1.indexOf(".");
                        String beforeDecimal1 = totalString1.substring(0, index1);
                        String afterDecimal1 = totalString1.substring(index1 + 1, totalString1.length());
                        StringBuilder convertedTotal1 = new StringBuilder();
                        convertedTotal1.append(ConvertNumberToWords.convert(Integer.parseInt(beforeDecimal1)));
                        convertedTotal1.append(" DOLLARS AND ");
                        convertedTotal1.append(ConvertNumberToWords.convert(Integer.parseInt(afterDecimal1)));
                        convertedTotal1.append(" CENTS ");
                        cell = makeCellLeftNoBorderPalatinoFclBl(convertedTotal1.toString(), palatinoRomanSmallFont);
                        cell.setColspan(2);
                        cell.setNoWrap(true);
                        ratesTable.addCell(cell);
                    }
                } else {
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
                    cell.setNoWrap(true);
                    ratesTable.addCell(cell);
                }
            }

            return ratesTable;
        } catch (Exception e) {
            log.info("getChargesTable failed on " + new Date(), e);
            throw new ExceptionConverter(e);
        }
    }

    public PdfPTable fillAesDetails(FclBl bl, MessageResources messageResources, Font palatinoRomanSmallFont, boolean importFlag, String simpleRequest)
            throws DocumentException, Exception {
        PdfPTable particularsTable = makeTable(5);
        particularsTable.setWidths(new float[]{18, 10, 44, 14, 14});
        particularsTable.setWidthPercentage(100);
        List inbondList = new FclInbondDetailsDAO().findByProperty("fileNumber", bl.getFileNo());
        List aesList = new FclInbondDetailsDAO().findAesdetails("fileNo", bl.getFileNo());

        if (importFlag && ("fclArrivalNotice".equalsIgnoreCase(simpleRequest) || "fclArrivalNoticeNonRated".equalsIgnoreCase(simpleRequest))) {
            Paragraph arrivalParagraph = new Paragraph();
            StringBuilder sb = new StringBuilder();
            sb.append(AN100 != null ? AN100 : "").append("\n");
            sb.append(AN101 != null ? AN101 : "").append("\n");
            sb.append(AN102 != null ? AN102 : "").append("\n");
            sb.append(AN103 != null ? AN103 : "").append("\n");
            sb.append(AN104 != null ? AN104 : "").append("\n");
            arrivalParagraph.add(new Chunk(AN100 != null ? AN100 : "", palatinoRomanSmallFont));
            arrivalParagraph.add(new Chunk("\n"));
            arrivalParagraph.add(new Chunk(AN101 != null ? AN101 : "", palatinoRomanSmallFont));
            arrivalParagraph.add(new Chunk("\n"));
            arrivalParagraph.add(new Chunk(AN102 != null ? AN102 : "", palatinoRomanSmallFont));
            arrivalParagraph.add(new Phrase("\n"));
            arrivalParagraph.add(new Chunk(AN103 != null ? AN103 : "", palatinoRomanSmallFont));
            arrivalParagraph.add(new Phrase("\n"));
            arrivalParagraph.add(new Chunk(AN104 != null ? AN104 : "", palatinoRomanSmallFont));
            int count = 0;
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
            for (Iterator iter = containerList.iterator(); iter.hasNext();) {
                FclBlContainer fclBlContainer = (FclBlContainer) iter.next();
                List arrayList = fclBlContainer.getFclMarksList();
                for (Iterator iterator1 = arrayList.iterator(); iterator1.hasNext();) {
                    FclBlMarks fclBlmarks = (FclBlMarks) iterator1.next();
                    count++;
                }
            }
            if (count == 2) {
                particularsTable.addCell(makeCellLeftRightBorderPalatinoFclBl("", palatinoRomanSmallFont));
                particularsTable.addCell(makeCellLeftRightBorderPalatinoFclBl("", palatinoRomanSmallFont));
                particularsTable.addCell(makeCellLeftRightBorderPalatinoFclBl("", palatinoRomanSmallFont));
                particularsTable.addCell(makeCellLeftRightBorderPalatinoFclBl("", palatinoRomanSmallFont));
                particularsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
            } else if (count == 1) {
                for (int i = 0; i < 3; i++) {
                    particularsTable.addCell(makeCellLeftRightBorderPalatinoFclBl("", palatinoRomanSmallFont));
                    particularsTable.addCell(makeCellLeftRightBorderPalatinoFclBl("", palatinoRomanSmallFont));
                    particularsTable.addCell(makeCellLeftRightBorderPalatinoFclBl("", palatinoRomanSmallFont));
                    particularsTable.addCell(makeCellLeftRightBorderPalatinoFclBl("", palatinoRomanSmallFont));
                    particularsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
                }
            }
            if (null != bl.getExportReference() && importFlag) {
                String ExportReference = bl.getExportReference().length() > 250 ? bl.getExportReference().substring(0, 250) : bl.getExportReference();
                particularsTable.addCell(makeCellLeftRightBorderPalatinoFclBl("", palatinoRomanSmallFont));
                particularsTable.addCell(makeCellLeftRightBorderPalatinoFclBl("", palatinoRomanSmallFont));
                particularsTable.addCell(makeCellLeftRightBorderPalatinoFclBl("\n" + ExportReference, palatinoRomanSmallFont));
                particularsTable.addCell(makeCellLeftRightBorderPalatinoFclBl("", palatinoRomanSmallFont));
                particularsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
            }

            particularsTable.addCell(makeCellLeftRightBorderPalatinoFclBl("", palatinoRomanSmallFont));
            particularsTable.addCell(makeCellLeftRightBorderPalatinoFclBl("", palatinoRomanSmallFont));
            particularsTable.addCell(makeCellLeftRightBorderPalatinoFclBl(sb.toString(), palatinoRomanSmallFont));
            particularsTable.addCell(makeCellLeftRightBorderPalatinoFclBl("", palatinoRomanSmallFont));
            particularsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
        } else {
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
            if (null != bl.getExportReference() && importFlag) {
                String ExportReference = bl.getExportReference().length() > 250 ? bl.getExportReference().substring(0, 250) : bl.getExportReference() ;
                particularsTable.addCell(makeCellLeftRightBorderPalatinoFclBl("", palatinoRomanSmallFont));
                particularsTable.addCell(makeCellLeftRightBorderPalatinoFclBl("", palatinoRomanSmallFont));
                particularsTable.addCell(makeCellLeftRightBorderPalatinoFclBl("\n" + ExportReference, palatinoRomanSmallFont));
                particularsTable.addCell(makeCellLeftRightBorderPalatinoFclBl("", palatinoRomanSmallFont));
                particularsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
            }
        }

        return particularsTable;
    }

    public PdfPTable fillMarksArrivalNotice(FclBl bl, MessageResources messageResources, Font palatinoRomanSmallFont)
            throws DocumentException, Exception {
        PdfPCell cell = null;
        PdfPTable particularsTable = makeTable(5);
        NumberFormat numberFormat = new DecimalFormat("###,###,##0.000");
        Set<FclBlContainer> containerSet = bl.getFclcontainer();
        HashMap hashMap = new HashMap();
        HashMap<String, Integer> hm = new HashMap<String, Integer>();
        List TempList = new ArrayList();
        List shortedContainerList = new ArrayList();
        List<String> sizeLegentList = new ArrayList<String>();
        List containerList = new ArrayList();
        for (Iterator iter = containerSet.iterator(); iter.hasNext();) {
            FclBlContainer fclBlCont = (FclBlContainer) iter.next();
            if (!"D".equalsIgnoreCase(fclBlCont.getDisabledFlag())) {
                hashMap.put(fclBlCont.getTrailerNoId(), fclBlCont);
                TempList.add(fclBlCont.getTrailerNoId());
                sizeLegentList.add(fclBlCont.getSizeLegend().getCodedesc());
            }
        }
        Collections.sort(TempList);
        for (int i = 0; i < TempList.size(); i++) {
            FclBlContainer fclBlCont = (FclBlContainer) hashMap.get(TempList.get(i));
            containerList.add(fclBlCont);
        }
        if (containerList != null) {
            shortedContainerList = OrderContainerList(containerList);
        }
        for (int i = 0; i < sizeLegentList.size(); i++) {
            if (null != hm.get(sizeLegentList.get(i))) {
                hm.put(sizeLegentList.get(i), hm.get(sizeLegentList.get(i)) + 1);
            } else {
                hm.put(sizeLegentList.get(i), 1);
            }
        }
        int count = 0;
        for (Iterator iter = shortedContainerList.iterator(); iter.hasNext();) {
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
                if (null != hm.get(sizeLegend)) {
                    tempSizeLegened.append(hm.get(sizeLegend));
                    tempSizeLegened.append("X");
                    String tempSize = sizeLegend.substring(index + 1, sizeLegend.length());
                    if (tempSize.equalsIgnoreCase(messageResources.getMessage("container40HC"))) {
                        tempSize = "40" + "'" + "HC";
                    } else if (tempSize.equalsIgnoreCase(messageResources.getMessage("container40NOR"))) {
                        tempSize = "40" + "'" + "NOR";
                    } else {
                        tempSize = tempSize + "'";
                    }
                    tempSizeLegened.append(tempSize);
                    hm.put(sizeLegend, null);
                } else {
                    tempSizeLegened.append("");
                }
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
                if (null != bl.getNoOfPackages() && bl.getNoOfPackages().equalsIgnoreCase("Yes") && CommonUtils.isNotEmpty(tempSizeLegened)) {
                    marksNumber.append(tempSizeLegened.toString());
                    marksNumber.append("\n");
                    if (CommonUtils.isNotEmpty(fclBlContainer.getSpecialEquipment())) {
                        marksNumber.append(fclBlContainer.getSpecialEquipment());
                        marksNumber.append("\n");
                    }
                }
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
            List fclMarksList = new ArrayList();
            if (fclBlContainer.getFclBlMarks() != null) {
                Iterator iterator = fclBlContainer.getFclBlMarks().iterator();
                while (iterator.hasNext()) {
                    FclBlMarks fclBlMarks = (FclBlMarks) iterator.next();
                    fclMarksList.add(fclBlMarks);
                }
                fclBlContainer.setFclMarksList(fclMarksList);
                List arrayList = fclBlContainer.getFclMarksList();
                if (arrayList.isEmpty()) {
                    particularsTable.addCell(makeCellLeftWithRightBorderBold(""));
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
                                particularsTable.addCell(makeCellLeftWithRightBorderBold(marksList.get(0).toString()));
                                marksList.remove(0);
                            } else {
                                particularsTable.addCell(makeCellLeftWithRightBorderBold(""));
                            }
                        }
                        for (int i = 0; i < l.size(); i++) {
                            StringBuilder stcAndPackages = new StringBuilder();
                            if (i == 0) {
                                if (null != bl.getTotalContainers() && bl.getTotalContainers().equalsIgnoreCase("Yes")) {
                                    stcAndPackages.append(null != fclBlmarks.getNoOfPkgs() && fclBlmarks.getNoOfPkgs() != 0 ? fclBlmarks.getNoOfPkgs() : "");
                                    stcAndPackages.append(" ");
                                    stcAndPackages.append(null != fclBlmarks.getUom() ? fclBlmarks.getUom() : "");
                                }
                                if (null != bl.getNoOfPackages() && bl.getNoOfPackages().equalsIgnoreCase("Alt")) {
                                    particularsTable.addCell(makeCellLeftRightBorderPalatinoFclBl(bl.getAlternateNoOfPackages().toUpperCase(), palatinoRomanSmallFont));
                                } else {
                                    particularsTable.addCell(makeCellLeftRightBorderPalatinoFclBl(stcAndPackages.toString(), palatinoRomanSmallFont));
                                }
                                particularsTable.addCell(makeCellLeftRightBorderPalatinoFclBl(l.get(i) != null ? l.get(i).toString() : "", palatinoRomanSmallFont));

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
            particularsTable = makeTable(5);
            particularsTable.setWidths(new float[]{18, 10, 44, 14, 14});
            particularsTable.setWidthPercentage(100);
            count++;
            boolean set = false;
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
            if ("Yes".equalsIgnoreCase(bl.getPrintContainersOnBL())) {
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

    public PdfPTable addAesDetails(FclBl fclBl, PdfPTable particularsTable, Font palatinoRomanSmallFont) {
        if (null != fclBl.getFclAesDetails()) {
            Set aesDetails = fclBl.getFclAesDetails();
            int count = 0;
            boolean printAes = false;
            StringBuilder aes = new StringBuilder("");
            for (Iterator iterator = aesDetails.iterator(); iterator.hasNext();) {
                count++;
                FclAESDetails aesDet = (FclAESDetails) iterator.next();
                if (CommonUtils.isNotEmpty(aesDet.getAesDetails())) {
                    if (count == 1) {
                        aes.append("AES ITN: ").append(aesDet.getAesDetails());
                        printAes = false;
                    } else {
                        aes.append(",AES ITN: ").append(aesDet.getAesDetails());
                        count = 0;
                        printAes = true;
                        aes = new StringBuilder("");
                    }
                } else if (CommonUtils.isNotEmpty(aesDet.getException())) {
                    aes.append("AES: ").append(aesDet.getException());
                    printAes = true;
                    count = 0;
                }
                if (printAes) {
                    particularsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
                    particularsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
                    PdfPCell aesDeta = makeCellLeftNoBorderPalatinoFclBl(aes.toString(), palatinoRomanSmallFont);
                    particularsTable.addCell(aesDeta);
                    particularsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
                    particularsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
                }
            }
            if (count == 1) {
                particularsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
                particularsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
                PdfPCell aesDeta = makeCellLeftNoBorderPalatinoFclBl(aes.toString(), palatinoRomanSmallFont);
                particularsTable.addCell(aesDeta);
                particularsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
                particularsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
            }
        }
        return particularsTable;
    }

    private PdfPTable getMyFooter(FclBl bl, String contextPath) throws Exception {
        try {
            FclBl originalBl = new FclBlDAO().getOriginalBl(bl.getFileNo());
            PdfPTable footerTable = makeTable(1);
            PdfPCell cell = null;
            String brand = "";
            String tariffRemarks = "";
            NumberFormat numberFormat = new DecimalFormat("###,###,##0.00");
            BaseFont palationRomanBase = BaseFont.createFont(contextPath + "/ttf/Palatino-Roman.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font palatinoRomanLargeFont = new Font(palationRomanBase, 10, Font.NORMAL, Color.BLACK);
            Font palatinoRomanSmallFont = new Font(palationRomanBase, 8, Font.NORMAL, Color.BLACK);
            Font bottom1TextFont = new Font(Font.HELVETICA, 7, Font.NORMAL, Color.BLACK);
            Font bottom2TextFont = new Font(Font.HELVETICA, 8, Font.NORMAL, Color.BLACK);
            Font conTextFont = new Font(Font.HELVETICA, 8, Font.BOLD, Color.BLACK);
            PdfPTable particularsFurnishedTable = makeTable(5);
            particularsFurnishedTable.setWidths(new float[]{20, 10, 40, 20, 10});
            particularsFurnishedTable.setWidthPercentage(100);
            if (null != originalBl && null != originalBl.getBrand()) {
                brand = originalBl.getBrand();
            }
            if (brand.equalsIgnoreCase("Econo") && ("03").equals(companyCode)) {
                tariffRemarks = LoadLogisoftProperties.getProperty("Econo.tariffterms");
            } else if (brand.equalsIgnoreCase("OTI") && ("02").equals(companyCode)) {
                tariffRemarks = LoadLogisoftProperties.getProperty("OTI.tariffterms");
            } else if (brand.equalsIgnoreCase("Ecu Worldwide")) {
                tariffRemarks = LoadLogisoftProperties.getProperty("ECU.tariffterms");
            }
            if (simpleRequest != null && (simpleRequest.equalsIgnoreCase("fclArrivalNotice") || "fclArrivalNoticeNonRated".equalsIgnoreCase(simpleRequest))) {
                if ("Yes".equalsIgnoreCase(bl.getOriginalBlRequired())) {
                    cell = makeCellLeftNoBorderFclBL("*** Original BL Required ***");
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setColspan(5);
                    particularsFurnishedTable.addCell(cell);

                } else {
                    cell = makeCellLeftNoBorderFclBL("*** Express Release ***");
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setColspan(5);
                    particularsFurnishedTable.addCell(cell);
                }
                if (simpleRequest != null && (simpleRequest.equalsIgnoreCase("fclArrivalNotice") || "fclArrivalNoticeNonRated".equalsIgnoreCase(simpleRequest))) {
                    if (commentsList != null && commentsList.size() > 0) {
                        PdfPTable comment3Table = makeTable(2);
                        PdfPCell commentsubCell = makeCellLeftNoBorderFclBL("");
                        comment3Table.setWidthPercentage(100);
                        comment3Table.setWidths(new float[]{64, 36});
                        String comment2 = AN300 != null ? AN300 : "";
                        cell = makeCellLeftNoBorderPalatinoFclBl(comment2, palatinoRomanSmallFont);
                        comment3Table.addCell(cell);
                        String consignee = bl.getConsigneeName() != null ? bl.getConsigneeName() : "";
                        cell = makeCellLeftNoBorderPalatinoFclBl(consignee, palatinoRomanSmallFont);
                        cell.setBorderWidthBottom(0.6f);
                        comment3Table.addCell(cell);
                        commentsubCell.addElement(comment3Table);
                        commentsubCell.setColspan(5);
                        particularsFurnishedTable.addCell(commentsubCell);
                        // ///
                        PdfPTable comment4Table = makeTable(3);
                        PdfPCell subCell = makeCellLeftNoBorderFclBL("");
                        comment4Table.setWidthPercentage(100);
                        comment4Table.setWidths(new float[]{15, 42, 43});
                        String comment3 = AN301 != null ? AN301 : "";
                        cell = makeCellLeftNoBorderPalatinoFclBl(comment3, palatinoRomanSmallFont);
                        comment4Table.addCell(cell);
                        String consignee1 = bl.getConsigneeName() != null ? bl.getConsigneeName() : "";
                        cell = makeCellLeftNoBorderPalatinoFclBl(consignee1, palatinoRomanSmallFont);
                        cell.setBorderWidthBottom(0.6f);
                        comment4Table.addCell(cell);
                        String comment4 = AN302 != null ? AN302 : "";
                        cell = makeCellLeftNoBorderPalatinoFclBl(comment4, palatinoRomanSmallFont);
                        comment4Table.addCell(cell);
                        subCell.addElement(comment4Table);
                        subCell.setColspan(5);
                        particularsFurnishedTable.addCell(subCell);
                        // //
                        cell = makeCellLeftNoBorderFclBL("");
                        cell.setColspan(5);
                        particularsFurnishedTable.addCell(cell);
                    }
                }
                cell = makeCellLeftNoBorderFclBL("\n");
                cell.setColspan(5);
                cell.setLeading(0.10f, 0.10f);
                particularsFurnishedTable.addCell(cell);
            } else {
                if (commentsList != null && commentsList.size() > 0) {
                    int countOriginal = 0;
                    int countNonNegotiable = 0;
                    if (bl.getHouseBl() != null && (bl.getHouseBl().equalsIgnoreCase("P-Prepaid") || bl.getHouseBl().equalsIgnoreCase("P"))) {
                        if ("Yes".equalsIgnoreCase(bl.getCollectThirdParty())) {
                            cell = makeCellLeftNoBorderFclBL("FREIGHT COLLECT");
                        } else {
                            cell = makeCellLeftNoBorderFclBL("FREIGHT PREPAID");
                        }
                    } else if (bl.getHouseBl() != null && (bl.getHouseBl().equalsIgnoreCase("C-Collect") || bl.getHouseBl().equalsIgnoreCase("C"))) {
                        cell = makeCellLeftNoBorderFclBL("FREIGHT COLLECT");
                    } else if (bl.getHouseBl() != null && (bl.getHouseBl().equalsIgnoreCase("B-Both") || bl.getHouseBl().equalsIgnoreCase("B"))) {
                        String prepaidOrCollect = "";
                        boolean prepaid = false;
                        boolean collect = false;
                        Set<FclBlCharges> fclChargesSet = bl.getFclcharge();
                        for (FclBlCharges fclBlCharges : fclChargesSet) {
                            if (null != fclBlCharges.getFaeIncentFlag() && fclBlCharges.getFaeIncentFlag().equals("Y") && fclBlCharges.getPrintOnBl().equals("Yes")) {
                                collect = true;
                            } else if (("Agent".equalsIgnoreCase(fclBlCharges.getBillTo()) && null == fclBlCharges.getFaeIncentFlag()) || "Consignee".equalsIgnoreCase(fclBlCharges.getBillTo())) {
                                collect = true;
                            } else {
                                prepaid = true;
                            }
                        }
                        if (prepaid && collect) {
                            prepaidOrCollect = "FREIGHT PREPAID/COLLECT";
                        } else if (prepaid) {
                            prepaidOrCollect = "FREIGHT PREPAID";
                        } else {
                            prepaidOrCollect = "FREIGHT COLLECT";
                        }
                        cell = makeCellLeftNoBorderFclBL(prepaidOrCollect);
                    }
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setColspan(5);
                    particularsFurnishedTable.addCell(cell);

                    cell = makeCellLeftNoBorderFclBL("");
                    cell.setColspan(5);
                    particularsFurnishedTable.addCell(cell);
                    String comment3 = BL100 != null ? BL100 : "";
                    cell = makeCellLeftNoBorderPalatinoFclBl(comment3, palatinoRomanSmallFont);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setColspan(5);
                    if (field1ForBL100.equalsIgnoreCase("BOTH")) {
                        particularsFurnishedTable.addCell(cell);
                    } else if ((simpleRequest.equalsIgnoreCase("freightedOriginalBl") || simpleRequest.equalsIgnoreCase("UnfreightedOriginalBl"))
                            && field1ForBL100.equalsIgnoreCase("ORIGINAL")) {
                        particularsFurnishedTable.addCell(cell);
                    } else if ((simpleRequest.equalsIgnoreCase("freightedNonNegotiable") || simpleRequest.equalsIgnoreCase("UnfreightedNonNegotiable"))
                            && field1ForBL100.equalsIgnoreCase("NON-NEGOTIABLE")) {
                        particularsFurnishedTable.addCell(cell);
                    }
                    //-----------------empty rows----------
                 /*   cell = makeCellLeftNoBorderFclBL(" ");
                     cell.setColspan(5);
                     particularsFurnishedTable.addCell(cell);  */

                    String comment1 = BL101 != null ? BL101 : "";
                    cell = makeCellLeftNoBorderPalatinoFclBl(comment1, palatinoRomanSmallFont);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setColspan(5);
                    if (field1ForBL101.equalsIgnoreCase("BOTH")) {
                        particularsFurnishedTable.addCell(cell);
                    } else if ((simpleRequest.equalsIgnoreCase("freightedOriginalBl") || simpleRequest.equalsIgnoreCase("UnfreightedOriginalBl"))
                            && field1ForBL101.equalsIgnoreCase("ORIGINAL")) {
                        particularsFurnishedTable.addCell(cell);
                    } else if ((simpleRequest.equalsIgnoreCase("freightedNonNegotiable") || simpleRequest.equalsIgnoreCase("UnfreightedNonNegotiable"))
                            && field1ForBL101.equalsIgnoreCase("NON-NEGOTIABLE")) {
                        particularsFurnishedTable.addCell(cell);
                    }
                    String comment2 = BL102 != null ? BL102 : "";
                    cell = makeCellLeftNoBorderPalatinoFclBl(comment2, palatinoRomanSmallFont);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setColspan(5);
                    if (field1ForBL102.equalsIgnoreCase("BOTH")) {
                        particularsFurnishedTable.addCell(cell);
                    } else if ((simpleRequest.equalsIgnoreCase("freightedOriginalBl") || simpleRequest.equalsIgnoreCase("UnfreightedOriginalBl"))
                            && field1ForBL102.equalsIgnoreCase("ORIGINAL")) {
                        particularsFurnishedTable.addCell(cell);
                    } else if ((simpleRequest.equalsIgnoreCase("freightedNonNegotiable") || simpleRequest.equalsIgnoreCase("UnfreightedNonNegotiable"))
                            && field1ForBL102.equalsIgnoreCase("NON-NEGOTIABLE")) {
                        particularsFurnishedTable.addCell(cell);
                    }
                    String comment4 = null != BL103 ? BL103 : "";
                    cell = makeCellLeftNoBorderPalatinoFclBl(comment4, palatinoRomanSmallFont);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setColspan(5);
                    if (field1ForBL103.equalsIgnoreCase("BOTH")) {
                        particularsFurnishedTable.addCell(cell);
                    } else if ((simpleRequest.equalsIgnoreCase("freightedOriginalBl") || simpleRequest.equalsIgnoreCase("UnfreightedOriginalBl"))
                            && field1ForBL103.equalsIgnoreCase("ORIGINAL")) {
                        particularsFurnishedTable.addCell(cell);
                    } else if ((simpleRequest.equalsIgnoreCase("freightedNonNegotiable") || simpleRequest.equalsIgnoreCase("UnfreightedNonNegotiable"))
                            && field1ForBL103.equalsIgnoreCase("NON-NEGOTIABLE")) {
                        particularsFurnishedTable.addCell(cell);
                    }
                    //Adding BL100Series print comments into List
                    List<String> field1ForBlList = new ArrayList<String>();
                    field1ForBlList.add(field1ForBL100);
                    field1ForBlList.add(field1ForBL101);
                    field1ForBlList.add(field1ForBL102);
                    field1ForBlList.add(field1ForBL103);
                    //List end
                    //Counting the Original and Non-Negotiable print comments
                    for (int i = 0; i < field1ForBlList.size(); i++) {
                        if (field1ForBlList.get(i).equalsIgnoreCase("ORIGINAL")) {
                            countOriginal++;
                        } else if (field1ForBlList.get(i).equalsIgnoreCase("NON-NEGOTIABLE")) {
                            countNonNegotiable++;
                        }
                    }
                    //counting end
                    //making space in the Original and Non-Negotiable BL print
                    if (simpleRequest.equalsIgnoreCase("freightedOriginalBl") || simpleRequest.equalsIgnoreCase("UnfreightedOriginalBl")) {
                        cell = makeCellLeftNoBorderFclBL(" ");
                        cell.setColspan(5);
                        for (int i = 0; i < countNonNegotiable; i++) {
                            particularsFurnishedTable.addCell(cell);
                        }
                    } else if (simpleRequest.equalsIgnoreCase("freightedNonNegotiable") || simpleRequest.equalsIgnoreCase("UnfreightedNonNegotiable")) {
                        cell = makeCellLeftNoBorderFclBL(" ");
                        cell.setColspan(5);
                        for (int i = 0; i < countOriginal; i++) {
                            particularsFurnishedTable.addCell(cell);
                        }
                    }
                    //making space end
                }
            }
            cell = makeCellLeftNoBorderFclBL("");
            cell.setColspan(5);
            cell.setBorderWidthBottom(0.6f);
            particularsFurnishedTable.addCell(cell);
            if (!"fclArrivalNotice".equalsIgnoreCase(simpleRequest) && !"fclArrivalNoticeNonRated".equalsIgnoreCase(simpleRequest)) {
                cell = makeCellLeftNoBorderFclBL("");
                cell.setColspan(5);
                cell.setBorderWidthBottom(0.6f);
                particularsFurnishedTable.addCell(cell);
            }
            // RATES,PLEASE CONTACT ,DISCLOSURE AND BY TABLE
            PdfPTable bottomTable = makeTable(2);
            if (!"fclArrivalNotice".equalsIgnoreCase(simpleRequest) && !"fclArrivalNoticeNonRated".equalsIgnoreCase(simpleRequest)) {
                bottomTable.setWidthPercentage(102f);
                bottomTable.setWidths(new float[]{62f, 40f});
            } else {
                bottomTable.setWidthPercentage(102f);
                bottomTable.setWidths(new float[]{55f, 47f});

            }

            // RATES TABLE
            cell = makeCellLeftNoBorderFclBL("");
            cell.setBorderWidthRight(0.6f);
            PdfPTable ratesTable = makeTable(1);
            ratesTable.setWidthPercentage(100);
            // To leave Space for Charges to print
            if (simpleRequest.equalsIgnoreCase("freightedOriginalBl") || simpleRequest.equalsIgnoreCase("freightedMasterBl")
                    || simpleRequest.equalsIgnoreCase("freightedNonNegotiable") || simpleRequest.equalsIgnoreCase("fclArrivalNotice")
                    || simpleRequest.equalsIgnoreCase("UnmarkedHouseBillofLading") || simpleRequest.equalsIgnoreCase("fclArrivalNoticeNonRated")) {
                getChargesTable(bl, messageResources, contextPath, simpleRequest);
                if (CommonUtils.in(simpleRequest, "fclArrivalNotice", "freightedNonNegotiable", "freightedOriginalBl", "UnmarkedHouseBillofLading")) {
                    for (int i = 0; i < 26; i++) {
                        ratesTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
                    }
                } else {
                    for (int i = 0; i < 30; i++) {
                        ratesTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
                    }
                }
            } else {
                ratesTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
            }

            // CONTACT TABLE
            PdfPTable ratesSubTable1 = makeTable(2);
            ratesSubTable1.setWidths(new float[]{50, 50});
            ratesSubTable1.setWidthPercentage(100);
            String billToAcct = "";
            String billToAcctNo = "";
            String Invoice = "";
            //String companyCode = new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
            if ("fclArrivalNotice".equalsIgnoreCase(simpleRequest) || "fclArrivalNoticeNonRated".equalsIgnoreCase(simpleRequest)) {
                ratesSubTable1.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
                ratesSubTable1.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
                ratesSubTable1.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
                ratesSubTable1.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
                ratesSubTable1.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
                ratesSubTable1.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
                ratesSubTable1.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
                ratesSubTable1.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
                ratesSubTable1.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
                ratesSubTable1.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
                ratesSubTable1.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
                ratesSubTable1.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
                ratesSubTable1.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
                ratesSubTable1.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
                if ("F".equalsIgnoreCase(bl.getBillToCode())) {
                    billToAcct = "Forwarder:";
                    billToAcctNo = bl.getForwardAgentNo() + (CommonUtils.isNotEmpty(bl.getForwardingAgentName()) ? " (" + bl.getForwardingAgentName() + ")" : "");
                } else if ("S".equalsIgnoreCase(bl.getBillToCode())) {
                    billToAcct = "Shipper:";
                    billToAcctNo = bl.getShipperNo() + (CommonUtils.isNotEmpty(bl.getShipperName()) ? " (" + bl.getShipperName() + ")" : "");
                } else if ("T".equalsIgnoreCase(bl.getBillToCode())) {
                    billToAcct = "ThirdParty:";
                    billToAcctNo = bl.getBillTrdPrty() + (CommonUtils.isNotEmpty(bl.getThirdPartyName()) ? " (" + bl.getThirdPartyName() + ")" : "");
                } else if ("C".equalsIgnoreCase(bl.getBillToCode())) {
                    billToAcct = "Consignee:";
                    billToAcctNo = bl.getConsigneeNo() + (CommonUtils.isNotEmpty(bl.getConsigneeName()) ? " (" + bl.getConsigneeName() + ")" : "");
                } else {
                    billToAcct = "NotifyParty:";
                    billToAcctNo = bl.getNotifyParty() + (CommonUtils.isNotEmpty(bl.getNotifyPartyName()) ? " (" + bl.getNotifyPartyName() + ")" : "");
                }
                ratesSubTable1.addCell(makeCellLeftNoBorderPalatinoFclBl(billToAcct, palatinoRomanSmallFont));
                Invoice = "Invoice No- ";
            } else {
                if (null != bl && "F".equals(bl.getBillToCode())) {
                    billToAcct = "Forwarder:";
                    billToAcctNo = bl.getForwardAgentNo() + (CommonUtils.isNotEmpty(bl.getForwardingAgentName()) ? " (" + bl.getForwardingAgentName() + ")" : "");
                } else if (null != bl && "S".equals(bl.getBillToCode())) {
                    billToAcct = "Shipper:";
                    billToAcctNo = bl.getShipperNo() + (CommonUtils.isNotEmpty(bl.getShipperName()) ? " (" + bl.getShipperName() + ")" : "");
                } else if (null != bl && "T".equals(bl.getBillToCode())) {
                    billToAcct = "ThirdParty:";
                    billToAcctNo = bl.getBillTrdPrty() + (CommonUtils.isNotEmpty(bl.getThirdPartyName()) ? " (" + bl.getThirdPartyName() + ")" : "");
                } else {
                    billToAcct = "Agent:";
                    billToAcctNo = bl.getAgentNo() + (CommonUtils.isNotEmpty(bl.getAgent()) ? " (" + bl.getAgent() + ")" : "");
                }
                ratesSubTable1.addCell(makeCellLeftNoBorderPalatinoFclBl(billToAcct, palatinoRomanSmallFont));
                Invoice = "Bill of Lading No. ";
            }
            String consigneName = bl.getHouseConsigneeName();
            String notifyPartyName = bl.getHouseNotifyPartyName();
            String defaultAgent = "";
            if ("yes".equalsIgnoreCase(bl.getOmitTermAndPort())) {
                if ("02".equals(companyCode)) {
                    defaultAgent = (String) messageResources.getMessage("defaultAgent") + " 04-" + bl.getFileNo();
                } else {
                    defaultAgent = (String) messageResources.getMessage("defaultAgentforECCI") + " 04-" + bl.getFileNo();
                }
                if ("fclArrivalNotice".equalsIgnoreCase(simpleRequest) || "fclArrivalNoticeNonRated".equalsIgnoreCase(simpleRequest)) {
                    ratesSubTable1.addCell(makeCellRightNoBorderPalatinoFclBl(Invoice + defaultAgent, palatinoRomanSmallFont));
                } else {
                    ratesSubTable1.addCell(makeCellRightNoBorderPalatinoFclBl(Invoice + defaultAgent.replace(" ", "-"), palatinoRomanSmallFont));
                }
            } else {
                if ("02".equals(companyCode)) {
                    defaultAgent = (String) messageResources.getMessage("defaultAgent") + "";
                } else {
                    defaultAgent = (String) messageResources.getMessage("defaultAgentforECCI") + "";
                }
                if ("yes".equalsIgnoreCase(bl.getOmit2LetterCountryCode())) {
                    String text = Invoice + defaultAgent + getBolNo(bl.getBolId());
                    text = text.substring(0, text.indexOf("-") + 1) + text.substring(text.indexOf("-") + 3);
                    ratesSubTable1.addCell(makeCellRightNoBorderPalatinoFclBl(text, palatinoRomanSmallFont));
                } else {
                    if ("fclArrivalNotice".equalsIgnoreCase(simpleRequest) || "fclArrivalNoticeNonRated".equalsIgnoreCase(simpleRequest)) {
                        ratesSubTable1.addCell(makeCellRightNoBorderPalatinoFclBl(Invoice + defaultAgent + " " + getBolNo(bl.getBolId()), palatinoRomanSmallFont));
                    } else {
                        ratesSubTable1.addCell(makeCellRightNoBorderPalatinoFclBl(Invoice + defaultAgent + "-" + getBolNo(bl.getBolId()), palatinoRomanSmallFont));
                    }
                }
            }
            if ("fclArrivalNotice".equalsIgnoreCase(simpleRequest) || "fclArrivalNoticeNonRated".equalsIgnoreCase(simpleRequest)) {
                ratesSubTable1.addCell(makeCellLeftNoBorderPalatinoFclBl((billToAcctNo.length() > 29 ? billToAcctNo.substring(0, 29) + ")" : billToAcctNo), palatinoRomanSmallFont));
            } else {
                ratesSubTable1.addCell(makeCellLeftNoBorderPalatinoFclBl((billToAcctNo.length() > 34 ? billToAcctNo.substring(0, 34) + ")" : billToAcctNo), palatinoRomanSmallFont));
            }
            ratesSubTable1.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
            if ("fclArrivalNotice".equalsIgnoreCase(simpleRequest)) {
                if ("R".equalsIgnoreCase(bl.getRatesNonRates()) || null == bl.getRatesNonRates()) {
                    ratesSubTable1.addCell(makeCellLeftNoBorderFclBL("Bill to Party:"));
                    StringBuilder notifyPartyDetails = new StringBuilder();
                    if ("F".equalsIgnoreCase(bl.getBillToCode())) {
                        StringBuilder contactDetails = new StringBuilder();
                        if (null != bl.getForwardingAgentName() && !bl.getForwardingAgentName().trim().equalsIgnoreCase("NO FF ASSIGNED".trim())
                                && !bl.getForwardingAgentName().trim().equalsIgnoreCase("NO FF ASSIGNED / B/L PROVIDED".trim())) {
                            String forwardingAgent = null != bl.getForwardingAgent() ? bl.getForwardingAgent().trim() : "";
                            forwardingAgent = trimShipperConsigNotifyForwardAddress(forwardingAgent);
                            if (bl.getBolId().indexOf("==") != -1 && CommonUtils.isEmpty(bl.getForwardingAgent())) {
                                forwardingAgent = trimShipperConsigNotifyForwardAddress(getAddress(bl.getForwardAgentNo()));
                            }
                            contactDetails.append(bl.getForwardingAgentName() != null ? bl.getForwardingAgentName() : "");
                            contactDetails.append("\n");
                            contactDetails.append(forwardingAgent);
                        } else {
                            contactDetails.append("");
                        }
                        ratesSubTable1.addCell(makeCellLeftNoBorderFclBL(contactDetails.toString()));
                    } else if ("S".equalsIgnoreCase(bl.getBillToCode())) {
                        StringBuilder contactDetails = new StringBuilder();
                        contactDetails.append(bl.getShipperName() != null ? bl.getShipperName() : "");
                        contactDetails.append("\n");
                        contactDetails.append(bl.getShipperAddress());
                        ratesSubTable1.addCell(makeCellLeftNoBorderFclBL(contactDetails.toString()));
                    } else if ("T".equalsIgnoreCase(bl.getBillToCode())) {
                        StringBuilder contactDetails = new StringBuilder();
                        contactDetails.append(bl.getThirdPartyName() != null ? bl.getThirdPartyName() : "");
                        contactDetails.append("\n");
                        contactDetails.append(getAddress(bl.getBillTrdPrty()));
                        ratesSubTable1.addCell(makeCellLeftNoBorderFclBL(contactDetails.toString()));
                    } else if ("C".equalsIgnoreCase(bl.getBillToCode())) {
                        StringBuilder contactDetails = new StringBuilder();
                        contactDetails.append(bl.getConsigneeName() != null ? bl.getConsigneeName() : "");
                        contactDetails.append("\n");
                        contactDetails.append(bl.getConsigneeAddress() != null ? bl.getConsigneeAddress() : "");
                        ratesSubTable1.addCell(makeCellLeftNoBorderFclBL(contactDetails.toString()));
                    } else if ("N".equalsIgnoreCase(bl.getBillToCode())) {
                        StringBuilder contactDetails = new StringBuilder();
                        contactDetails.append(bl.getNotifyPartyName() != null ? bl.getNotifyPartyName() : "");
                        contactDetails.append("\n");
                        contactDetails.append(getAddress(bl.getNotifyParty()));
                        ratesSubTable1.addCell(makeCellLeftNoBorderFclBL(contactDetails.toString()));
                    } else {
                        //do nothing
                    }
                    ratesSubTable1.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
                    ratesSubTable1.addCell(makeCellLeftNoBorderPalatinoFclBl(notifyPartyDetails.toString(), palatinoRomanSmallFont));
                    //ratesTable.addCell(ratesSubTable1);
                }
            } else if ("fclArrivalNoticeNonRated".equalsIgnoreCase(simpleRequest)) {
                ratesSubTable1.addCell(makeCellLeftNoBorderFclBL(""));
                ratesSubTable1.addCell(makeCellLeftNoBorderFclBL(""));
                // ratesTable.addCell(ratesSubTable1);
            } else {
                if (consigneName != null && !consigneName.equalsIgnoreCase("")) {
                    StringBuilder consigneeDetails = new StringBuilder();
                    StringBuilder BelowConsigneeDetails = new StringBuilder();
                    if (CommonUtils.in(simpleRequest, "freightedNonNegotiable", "UnfreightedNonNegotiable", "freightedOriginalBl", "UnfreightedOriginalBl", "UnmarkedHouseBillofLading")) {
                        ratesSubTable1.addCell(makeCellLeftNoBorderFclBL("Please Contact:"));
                    } else {
                        ratesSubTable1.addCell(makeCellLeftNoBorderFclBL("Bill to Party:"));
                    }
                    consigneeDetails.append(consigneName);
                    consigneeDetails.append("\n");
                    consigneeDetails.append(null != bl.getHouseConsigneeAddress() ? bl.getHouseConsigneeAddress() : "");
                    consigneeDetails.append("\n");
                    ratesSubTable1.addCell(makeCellLeftNoBorderFclBL(consigneeDetails.toString()));
                    if (notifyPartyName != null && !notifyPartyName.equalsIgnoreCase("")) {
                        BelowConsigneeDetails.append(notifyPartyName);
                        BelowConsigneeDetails.append("\n");
                        BelowConsigneeDetails.append(null != bl.getHouseNotifyParty() ? bl.getHouseNotifyParty() : "");
                        BelowConsigneeDetails.append("");
                    }
                    ratesSubTable1.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
                    ratesSubTable1.addCell(makeCellLeftNoBorderPalatinoFclBl(BelowConsigneeDetails.toString(), palatinoRomanSmallFont));
                    //ratesTable.addCell(ratesSubTable1);
                } else {
                    //ratesTable.addCell(ratesSubTable1);
                }
            }
            if ("fclArrivalNotice".equalsIgnoreCase(simpleRequest) || "fclArrivalNoticeNonRated".equalsIgnoreCase(simpleRequest)) {
                ratesSubTable1.addCell(makeCellLeftNoBorderFclBL("Please Contact:"));
                StringBuilder pleaseContactDetails = new StringBuilder();
                pleaseContactDetails.append(bl.getHouseConsigneeName() != null ? bl.getHouseConsigneeName() : "");
                pleaseContactDetails.append("\n");
                pleaseContactDetails.append(bl.getHouseConsigneeAddress() != null ? bl.getHouseConsigneeAddress() : "");
                ratesSubTable1.addCell(makeCellLeftNoBorderFclBL(pleaseContactDetails.toString()));
                //notify party details
                if (notifyPartyName != null && !notifyPartyName.equalsIgnoreCase("")) {
                    StringBuilder notifyPartyDetails = new StringBuilder();
                    ratesSubTable1.addCell(makeCellLeftNoBorder(""));
                    notifyPartyDetails.append(notifyPartyName);
                    notifyPartyDetails.append("\n");
                    notifyPartyDetails.append(null != bl.getHouseNotifyParty() ? bl.getHouseNotifyParty() : "");
                    notifyPartyDetails.append("");
                    ratesSubTable1.addCell(makeCellLeftNoBorder(notifyPartyDetails.toString()));
                }
            }
            ratesTable.addCell(ratesSubTable1);

            cell.addElement(ratesTable);
            bottomTable.addCell(cell);

            // DISCLOSURE TABLE
            PdfPTable disclosureTable = makeTable(1);
            disclosureTable.setWidthPercentage(100);
            PdfPCell maincell = makeCellLeftNoBorderFclBL("");
            if (simpleRequest != null && (simpleRequest.equalsIgnoreCase("fclArrivalNotice") || simpleRequest.equalsIgnoreCase("fclArrivalNoticeNonRated"))) {
                if (commentsList != null && commentsList.size() > 0) {
                    Paragraph arrivalParagraph = new Paragraph();
                    arrivalParagraph.add(new Chunk("ATTENTION:", conTextFont));
                    arrivalParagraph.add(new Phrase("\n"));
                    String comment1 = AN400 != null ? AN400 : "";
                    int index = comment1.indexOf("$");
                    arrivalParagraph.add(new Chunk(comment1.substring(0, index + 1), bottom1TextFont));
                    arrivalParagraph.add(new Chunk(" $" + numberFormat.format(chargesTotal).toString(), conTextFont));
                    arrivalParagraph.add(new Chunk(comment1.substring(index + 1, comment1.length()), conTextFont));
                    arrivalParagraph.add(new Phrase("\n"));
                    arrivalParagraph.add(new Chunk(AN401 != null ? AN401 : "", bottom1TextFont));
                    arrivalParagraph.add(new Phrase("\n"));
                    arrivalParagraph.add(new Chunk(AN402 != null ? AN402 : "", bottom1TextFont));
                    arrivalParagraph.add(new Chunk("\n"));
                    arrivalParagraph.add(new Chunk("\n"));
                    arrivalParagraph.add(new Chunk(AN403 != null ? AN403 : "", bottom1TextFont));
                    arrivalParagraph.add(new Chunk(getBolNo(bl.getBolId().toString()), conTextFont));
                    arrivalParagraph.add(new Chunk("\n"));
                    arrivalParagraph.add(new Chunk("\n"));
                    arrivalParagraph.add(new Chunk(AN404 != null ? AN404 : "", bottom1TextFont));
                    arrivalParagraph.add(new Chunk("\n"));
                    arrivalParagraph.add(new Chunk("\n"));
                    arrivalParagraph.add(new Chunk(AN405 != null ? AN405 : "", conTextFont));
                    arrivalParagraph.add(new Phrase("\n"));
                    arrivalParagraph.add(new Phrase("\n"));
                    arrivalParagraph.add(new Chunk(AN406 != null ? AN406 : "", bottom1TextFont));
                    arrivalParagraph.add(new Phrase("\n"));
                    arrivalParagraph.add(new Phrase("\n"));
                    arrivalParagraph.add(new Chunk(AN407 != null ? AN407 : "", bottom1TextFont));
                    arrivalParagraph.add(new Phrase("\n"));
                    arrivalParagraph.add(new Phrase("\n"));
                    arrivalParagraph.add(new Chunk(AN408 != null ? AN408 : "", bottom1TextFont));
                    arrivalParagraph.setLeading(10);
                    disclosureTable.addCell(arrivalParagraph);
                }
            } else {
                disclosureTable.setWidthPercentage(100);
                cell = makeCellLeftNoBorderFclBL("");
                cell.setColspan(5);
                disclosureTable.addCell(cell);
                cell = makeCellLeftNoBorderFclBL("");
                cell.setColspan(5);
                disclosureTable.addCell(cell);
                cell = makeCellLeftNoBorderFclBL("");
                cell.setColspan(5);
                disclosureTable.addCell(cell);
                cell = makeCellLeftNoBorderFclBL("");
                cell.setColspan(5);
                disclosureTable.addCell(cell);
                if (commentsList != null && commentsList.size() > 0) {
                    //  String comment1 = BL200 != null ? BL200 + "   " : "";
                    StringBuilder comment2 = new StringBuilder();
                    comment2.append(tariffRemarks);
                    comment2.append("..................................................................");
                    comment2.append(BL201 != null ? BL201 : "");
                    comment2.append("\nPLEASE SEE OUR WEBSITE FOR TERMS AND CONDITIONS.");
                    cell = makeCellLeftNoBorderPalatinoFclBl(comment2.toString(), bottom1TextFont);
                    cell.setColspan(5);
                    disclosureTable.addCell(cell);
                    cell = makeCellLeftNoBorderFclBL("");
                    cell.setColspan(5);
                    disclosureTable.addCell(cell);
                    cell = makeCellLeftNoBorderFclBL("");
                    cell.setColspan(5);
                    disclosureTable.addCell(cell);
                    cell = makeCellLeftNoBorderFclBL("");
                    cell.setColspan(5);
                    disclosureTable.addCell(cell);
                    cell = makeCellLeftNoBorderFclBL("");
                    cell.setColspan(5);
                    disclosureTable.addCell(cell);
                    cell = makeCellLeftNoBorderFclBL("");
                    cell.setColspan(5);
                    disclosureTable.addCell(cell);
                    cell = makeCellLeftNoBorderFclBL("");
                    cell.setColspan(5);
                    disclosureTable.addCell(cell);
                    cell = makeCellLeftNoBorderFclBL("");
                    cell.setColspan(5);
                    disclosureTable.addCell(cell);
                    cell = makeCellLeftNoBorderFclBL("");
                    cell.setColspan(5);
                    disclosureTable.addCell(cell);
                    cell = makeCellLeftNoBorderFclBL("");
                    cell.setColspan(5);
                    disclosureTable.addCell(cell);

                }
            }
            // BY TABLE
            if (simpleRequest != null && (simpleRequest.equalsIgnoreCase("fclArrivalNotice") || simpleRequest.equalsIgnoreCase("fclArrivalNoticeNonRated"))) {
                maincell.addElement(disclosureTable);
                bottomTable.addCell(maincell);
            } else {
                cell = makeCellLeftNoBorderFclBL("");
                PdfPTable disclosureSubTable = makeTable(2);
                disclosureSubTable.setWidths(new float[]{7, 50});
                disclosureSubTable.setWidthPercentage(100);
                StringBuilder builder = new StringBuilder();
                if (null != bl.getAgentsForCarrier() && bl.getAgentsForCarrier().equalsIgnoreCase("Yes")) {
                    builder.append("As Agents for the Carrier");
                    builder.append("\n");
                    builder.append(bl.getSslineName());
                    builder.append("\n");
                }
                cell = makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanLargeFont);
                cell = makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanLargeFont);
                disclosureSubTable.addCell(cell);
                cell = makeCellLeftNoBorderPalatinoFclBl(builder.toString(), palatinoRomanLargeFont);
                cell.setNoWrap(true);
                disclosureSubTable.addCell(cell);
                disclosureSubTable.addCell(makeCellRightNoBorderFclBLNormalFont(" "));
                SystemRulesDAO systemRulesDAO = new SystemRulesDAO();
//                String company = LoadLogisoftProperties.getProperty("application.fclBl.print.companyName");
//                String newCompany = LoadLogisoftProperties.getProperty("application.fclBl.print.companyName.econo");
                String company = "";
                String companyBy = "By ";
                /*                if ("A".equalsIgnoreCase(bl.getAgentsForCarrier())) {
                 company = null != company ? company + " As Carrier" : "";
                 } else {
                 company = null != company ? company : "";
                 }*/
//                if ("Y".equalsIgnoreCase(logoS)) {
//                    Phrase phraseY = new Phrase(new Chunk(companyBy, bottom2TextFont));
//                    if (company.contains("(")) {
//                        phraseY.add(new Phrase(new Chunk(company.substring(0, company.indexOf("(")), bottom2TextFont)));
//                        phraseY.add(new Phrase(new Chunk(" " + company.substring(company.indexOf("(")), bottomTextFontSubLin)));
//                    } else {
//                        phraseY.add(new Phrase(company.toUpperCase(), bottom2TextFont));
//                    }
//                    PdfPCell footerCellY = makeCell(phraseY, Element.ALIGN_LEFT);
//                    footerCellY.setBorderWidthBottom(0.6F);
//                    disclosureSubTable.addCell(footerCellY);
                //      By ECONOCARIBE (An ECU-Line Division) As Carrier
//                } else {
//                    Phrase phraseN = new Phrase(new Chunk(companyBy, bottom2TextFont));
//                    phraseN.add(new Phrase(newCompany.toUpperCase(), bottom2TextFont));
//                    PdfPCell footerCellN = makeCell(phraseN, Element.ALIGN_LEFT);
//                    footerCellN.setBorderWidthBottom(0.6F);
//                    disclosureSubTable.addCell(footerCellN);
//                }
                Phrase phraseY = new Phrase(new Chunk(companyBy, bottom2TextFont));
                if (brand.equalsIgnoreCase("Econo")) {
                    company = LoadLogisoftProperties.getProperty("Econo.carrier");
                    if ("A".equalsIgnoreCase(bl.getAgentsForCarrier())) {
                        company = null != company ? company + " As Carrier" : "";
                        phraseY.add(new Phrase(company, bottom2TextFont));
                    } else {
                        phraseY.add(new Phrase(company, bottom2TextFont));
                    }
                    PdfPCell footerCellY = makeCell(phraseY, Element.ALIGN_LEFT);
                    footerCellY.setBorderWidthBottom(0.6F);
                    disclosureSubTable.addCell(footerCellY);
                } else if (brand.equalsIgnoreCase("OTI")) {
                    company = LoadLogisoftProperties.getProperty("OTI.carrier");
                    if ("A".equalsIgnoreCase(bl.getAgentsForCarrier())) {
                        company = null != company ? company + " As Carrier" : "";
                        phraseY.add(new Phrase(company, bottom2TextFont));
                    } else {
                        phraseY.add(new Phrase(company, bottom2TextFont));
                    }
                    PdfPCell footerCellY = makeCell(phraseY, Element.ALIGN_LEFT);
                    footerCellY.setBorderWidthBottom(0.6F);
                    disclosureSubTable.addCell(footerCellY);
                } else if (brand.equalsIgnoreCase("Ecu Worldwide")) {
                    company = LoadLogisoftProperties.getProperty("ECU.carrier");
                    if ("A".equalsIgnoreCase(bl.getAgentsForCarrier())) {
                        company = null != company ? company + " As Carrier" : "";
                        phraseY.add(new Phrase(company, bottom2TextFont));
                    } else {
                        phraseY.add(new Phrase(company, bottom2TextFont));
                    }
                    PdfPCell footerCellY = makeCell(phraseY, Element.ALIGN_LEFT);
                    footerCellY.setBorderWidthBottom(0.6F);
                    disclosureSubTable.addCell(footerCellY);
                }

                //------------------------emptr (Break)
                disclosureSubTable.addCell(makeCellLeftNoBorderFclBL(""));
                disclosureSubTable.addCell(makeCellLeftNoBorderFclBL(""));
                disclosureSubTable.addCell(makeCellLeftNoBorderFclBL(""));
                disclosureSubTable.addCell(makeCellLeftNoBorderFclBL(""));
                disclosureSubTable.addCell(makeCellLeftNoBorderFclBL(""));

                Date sailDate = bl.getSailDate();
                if (sailDate != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                    disclosureSubTable.addCell(makeCellLeftNoBorderPalatinoWithUnderline(sdf.format(
                            sailDate).toString(), bottom2TextFont));
                } else {
                    disclosureSubTable.addCell(makeCellLeftNoBorderPalatinoWithUnderline("", bottom2TextFont));
                }
                //------------------------emptr (Break)
                disclosureSubTable.addCell(makeCellLeftNoBorderFclBL(""));
                disclosureSubTable.addCell(makeCellLeftNoBorderFclBL(""));
                disclosureSubTable.addCell(makeCellLeftNoBorderFclBL(""));
                disclosureSubTable.addCell(makeCellLeftNoBorderFclBL(""));
                disclosureSubTable.addCell(makeCellLeftNoBorderFclBL(""));
                if ("yes".equalsIgnoreCase(fclBl.getPrintAlternatePort())) {
                    cell = makeCellLeftNoBorderPalatinoWithUnderline(bl.getAlternatePort() != null ? removeUnlocCode(bl.getAlternatePort())
                            : "", bottom2TextFont);
                } else {
                    cell = makeCellLeftNoBorderPalatinoWithUnderline(bl.getPortOfLoading() != null ? removeUnlocCode(bl.getPortOfLoading())
                            : "", bottom2TextFont);
                }
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                disclosureSubTable.addCell(cell);
                cell = makeCellCenterNoBorderPalatinoFclBl("PORT", bottom2TextFont);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                disclosureSubTable.addCell(makeCellLeftNoBorderFclBL(""));
                disclosureSubTable.addCell(cell);
                disclosureTable.addCell(disclosureSubTable);
                maincell.addElement(disclosureTable);
                bottomTable.addCell(maincell);
            }

            Phrase phrase = new Phrase();
            phrase.clear();
            PdfPCell footerCell = makeCellLeftNoBorder("");
            footerCell.addElement(particularsFurnishedTable);
            footerCell.addElement(bottomTable);
            footerTable.addCell(footerCell);
            return footerTable;
        } catch (DocumentException e) {
            log.info("getMyFooter failed on " + new Date(), e);
            throw new ExceptionConverter(e);
        } catch (IOException e) {
            log.info("onEndPage failed on " + new Date(), e);
            throw new ExceptionConverter(e);
        }

    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        try {
            PdfPTable footerTable = this.getMyFooter(fclBl, context);
            footerTable.setTotalWidth(document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin());
            footerTable.writeSelectedRows(0, -1, document.leftMargin(), document.bottomMargin(), writer.getDirectContent());
            //---------------
            //this for print page number at the bottom in the format x of y
            PdfContentByte cb = writer.getDirectContent();
            cb.saveState();
            String text = "Page " + writer.getPageNumber() + " of ";
            float textBase = 0;
            if ("fclArrivalNotice".equalsIgnoreCase(simpleRequest) || "fclArrivalNoticeNonRated".equalsIgnoreCase(simpleRequest)) {
                textBase = document.bottom() - (document.bottomMargin() - 2);
            } else {
                textBase = document.bottom() - (document.bottomMargin() - 10);
            }
            //float textBase = document.bottom() - 20;
            float textSize = helv.getWidthPoint(text, 12);
            cb.beginText();
            cb.setFontAndSize(helv, 7);
            cb.setTextMatrix(document.left() + 490, textBase);
            cb.showText(text);
            cb.endText();
            cb.addTemplate(total, document.left() + 468 + textSize, textBase);
            cb.restoreState();

            ///this for the water mark..........................
            BaseFont helv;
            PdfGState gstate;
            Font hellv;
            String waterMark = "";
            fclBl = new FclBlDAO().getOriginalBl(fclBl.getFileNo());
            if (CommonFunctions.isNotNull(manifested)) {
                if (null != fclBl && "Yes".equalsIgnoreCase(fclBl.getCertifiedTrueCopy()) && simpleRequest != null && (simpleRequest.equalsIgnoreCase("UnfreightedOriginalBl") || simpleRequest.equalsIgnoreCase("freightedOriginalBl"))) {
                    waterMark = "CERTIFIED TRUE COPY";
                } else {
                    if (simpleRequest.equalsIgnoreCase("UnfreightedNonNegotiable")) {
                        waterMark = fclBl.getDockReceipt().equals("Yes") ? PrintReportsConstants.DOCK_RECEIPT : PrintReportsConstants.UNFREIGHTED_NONNEG_HOUSE_BL;
                    } else if (simpleRequest.equalsIgnoreCase("UnfreightedOriginalBl")) {
                        waterMark = fclBl.getDockReceipt().equals("Yes") ? PrintReportsConstants.DOCK_RECEIPT : PrintReportsConstants.UNFREIGHTED_ORIGINAL_HOUSE_BL;
                    } else if (simpleRequest.equalsIgnoreCase("freightedOriginalBl")) {
                        waterMark = fclBl.getDockReceipt().equals("Yes") ? PrintReportsConstants.DOCK_RECEIPT : PrintReportsConstants.FREIGHTED_ORIGINAL_HOUSE_BL;
                    } else if (simpleRequest.equalsIgnoreCase("freightedNonNegotiable")) {
                        waterMark = fclBl.getDockReceipt().equals("Yes") ? PrintReportsConstants.DOCK_RECEIPT : PrintReportsConstants.FREIGHTED_NONNEG_HOUSE_BL;
                    } else if (simpleRequest.equalsIgnoreCase("fclArrivalNotice")) {
                        waterMark = PrintReportsConstants.FCLARRIVAL_NOTICE;
                    } else {
                        waterMark = "";
                    }
                }
            } else if (null != proof && proof.equalsIgnoreCase("Yes")) {
                if (!simpleRequest.equalsIgnoreCase("fclArrivalNotice")) {
                    waterMark = fclBl.getDockReceipt().equals("Yes") ? PrintReportsConstants.DOCK_RECEIPT : PrintReportsConstants.PROOF;
                }
            } else {
                waterMark = "";
            }

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
            if (null != proof && proof.equalsIgnoreCase("Yes")) {
                contentunder.setFontAndSize(helv, 60);
            } else {
                contentunder.setFontAndSize(helv, 50);
            }
            contentunder.showTextAligned(Element.ALIGN_CENTER, waterMark,
                    document.getPageSize().getWidth() / 2, document.getPageSize().getHeight() / 2, 45);
            contentunder.endText();
            contentunder.restoreState();
        } catch (Exception e) {
            log.info("onEndPage failed on " + new Date(), e);
            throw new ExceptionConverter(e);
        }
    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        PdfPCell cell;
        PdfPTable particularsTable = makeTable(5);
        try {
            particularsTable.setWidths(new float[]{18, 10, 44, 14, 14});
            particularsTable.setWidthPercentage(100);
            cell = makeCellCenterNoBorderFclBL("PARTICULARS FURNISHED BY SHIPPER");
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(5);
            cell.setBorderWidthBottom(0.6f);
            cell.setLeading(0.10f, 0.10f);
            particularsTable.addCell(cell);
            cell = makeCellCenterNoBorderFclBL("MARKS AND NUMBERS");
            cell.setBorderWidthRight(0.6f);
            cell.setBorderWidthBottom(0.6f);
            cell.setLeading(0.7f, 0.7f);
            particularsTable.addCell(cell);
            cell = makeCellCenterNoBorderFclBL("NO.OF PKGS");
            cell.setBorderWidthRight(0.6f);
            cell.setBorderWidthBottom(0.6f);
            cell.setLeading(0.7f, 0.7f);
            particularsTable.addCell(cell);
            cell = makeCellCenterNoBorderFclBL("DESCRIPTION OF PACKAGES AND GOODS");
            cell.setBorderWidthRight(0.6f);
            cell.setBorderWidthBottom(0.6f);
            cell.setLeading(0.7f, 0.7f);
            particularsTable.addCell(cell);
            cell = makeCellCenterNoBorderFclBL("GROSS WEIGHT");
            cell.setBorderWidthRight(0.6f);
            cell.setBorderWidthBottom(0.6f);
            cell.setLeading(0.7f, 0.7f);
            particularsTable.addCell(cell);
            cell = makeCellCenterNoBorderFclBL("VOLUME");
            cell.setBorderWidthBottom(0.6f);
            cell.setLeading(0.7f, 0.7f);
            particularsTable.addCell(cell);
            document.add(particularsTable);
        } catch (DocumentException ex) {
            log.info("Exception on class BillOfLaddingPdfCreator in method onStartPage" + new Date(), ex);
            throw new ExceptionConverter(ex);
        }
    }

    @Override
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

    public String getAddress(String accoutNo) throws Exception {
        StringBuilder address = new StringBuilder();
        if (null != accoutNo && !"".equalsIgnoreCase(accoutNo)) {
            CustAddressDAO customerDAO = new CustAddressDAO();
            List custAddressList = customerDAO.findByAccountNo(null, accoutNo, null, null);
            if (!custAddressList.isEmpty()) {
                CustAddress custAddress = (CustAddress) customerDAO.findByAccountNo(null, accoutNo, null, null).get(0);
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

    public String getBolNo(String bolId) {
        String BolNo = "";
        if (null != bolId && !bolId.equalsIgnoreCase("")) {
            if (-1 != bolId.indexOf("=")) {
                BolNo = bolId.substring(0, bolId.indexOf("="));
            } else {
                BolNo = bolId;
            }
        }
        return BolNo;
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
            if (CommonUtils.isNotEmpty(portName) && portName.charAt(length - 1) == '/') {
                portName = portName.substring(0, length - 1);
            }
        }
        return portName.toUpperCase();
    }

    public String removeUnlocCodeAppendCountryName(String port) throws Exception {
        String portName = "";
        if (null != port) {
            if (port.lastIndexOf("(") != -1 && port.lastIndexOf(")") != -1) {
                String unLocCode = "";
                unLocCode = port.substring(port.lastIndexOf("(") + 1, port.lastIndexOf(")"));
                if (unLocCode != null && !unLocCode.equals("") && !unLocCode.equals("null")) {
                    UnLocationDAO locationDAO = new UnLocationDAO();
                    UnLocation location = locationDAO.getUnlocation(unLocCode);
                    portName = location.getUnLocationName();
                    if (null != location.getStateId()) {
                        portName += "/" + location.getStateId().getCode();
                    }
                }
                portName = portName.toUpperCase();
            }
        }
        return portName;
    }

    public String createBillOfLaddingReport(FclBl bl, String fileName,
            String contextPath, MessageResources messageResource,
            List commentsList, User user, String simpleRequest, boolean importFlag) throws Exception {
        if (bl.getAgentNo() != null) {
            logoS = new TradingPartnerDAO().getLogoStatus(bl.getAgentNo());
        }
        try {
            this.initialize(fileName, bl, contextPath, commentsList, messageResource, simpleRequest, importFlag, logoS);
            this.printCommands();
            this.createBody(contextPath, bl, commentsList, simpleRequest,
                    messageResource, importFlag);
            this.destroy();
        } catch (Exception e) {
            log.info("createBillOfLaddingReport failed on " + new Date(), e);
            throw new ExceptionConverter(e);
        }
        return "fileName";
    }

    public Integer getChargesSize(FclBl bl, MessageResources messageResources) throws Exception {
        Set<FclBlCharges> chargesSet = bl.getFclcharge();
        List TempList = new ArrayList();
        List chargeListTemp = new ArrayList();
        for (Iterator iter = chargesSet.iterator(); iter.hasNext();) {
            FclBlCharges fclBlCharges = (FclBlCharges) iter.next();
            TempList.add(fclBlCharges);
        }
        boolean importFlag = "I".equalsIgnoreCase(bl.getImportFlag());
        if ("I".equalsIgnoreCase(bl.getImportFlag())) {
            chargeListTemp = fclBlBC.consolidateRates(fclBlBC.getSortedList(TempList), messageResources, importFlag);
        } else {
            chargeListTemp = new PrintConfigBC().getRatesForFclPrint(fclBlBC.getSortedList(TempList), messageResources);
        }
        if (!chargeListTemp.isEmpty()) {
            return chargeListTemp.size();
        } else {
            return 0;
        }
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
        //method getCommentsListForBlReport changed to getCommentsList because of added one more field(field1) in the resultset.
        Iterator iter = new GenericCodeDAO().getCommentsList();
        Iterator iter1 = new GenericCodeDAO().getAllCommentForArrivalNoticeReports();
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            String code = (String) row[0];
            String codeDesc = (String) row[1];
            String field1 = (String) row[2];
            if (code != null) {
                if ("BL100".equals(code)) {
                    BL100 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                    field1ForBL100 = CommonFunctions.isNotNull(field1) ? field1 : "";
                } else if ("BL101".equals(code)) {
                    BL101 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                    field1ForBL101 = CommonFunctions.isNotNull(field1) ? field1 : "";
                } else if ("BL102".equals(code)) {
                    BL102 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                    field1ForBL102 = CommonFunctions.isNotNull(field1) ? field1 : "";
                } else if ("BL103".equals(code)) {
                    BL103 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                    field1ForBL103 = CommonFunctions.isNotNull(field1) ? field1 : "";
                } else if ("BL200".equals(code)) {
                    BL200 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                } else if ("BL201".equals(code)) {
                    BL201 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                }
            }
        }
        while (iter1.hasNext()) {
            Object[] row = (Object[]) iter1.next();
            String code = (String) row[0];
            String codeDesc = (String) row[1];
            if (code != null) {
                if ("AN200".equals(code)) {
                    AN200 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                } else if ("AN300".equals(code)) {
                    AN300 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                } else if ("AN301".equals(code)) {
                    AN301 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                } else if ("AN302".equals(code)) {
                    AN302 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                } else if ("AN400".equals(code)) {
                    AN400 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                } else if ("AN401".equals(code)) {
                    AN401 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                } else if ("AN402".equals(code)) {
                    AN402 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                } else if ("AN403".equals(code)) {
                    AN403 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                } else if ("AN404".equals(code)) {
                    AN404 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                } else if ("AN405".equals(code)) {
                    AN405 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                } else if ("AN406".equals(code)) {
                    AN406 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                } else if ("AN407".equals(code)) {
                    AN407 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                } else if ("AN408".equals(code)) {
                    AN408 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                } else if ("AN409".equals(code)) {
                    AN409 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                } else if ("AN100".equals(code)) {
                    AN100 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                } else if ("AN101".equals(code)) {
                    AN101 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                } else if ("AN102".equals(code)) {
                    AN102 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                } else if ("AN103".equals(code)) {
                    AN103 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                } else if ("AN104".equals(code)) {
                    AN104 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                }
            }
        }
    }

    public List OrderContainerList(List containerList) throws Exception {
        List OrderedContainerList = new ArrayList();
        List<String> legentSize = new ArrayList();
        legentSize = new GenericCodeDAO().getUnitCostTypeListInOrder();
        for (Iterator it = legentSize.iterator(); it.hasNext();) {
            String legentName = (String) it.next();
            for (Iterator iter = containerList.iterator(); iter.hasNext();) {
                FclBlContainer fclBlContainer = (FclBlContainer) iter.next();
                String fcllegentName = fclBlContainer.getSizeLegend().getCodedesc();
                if (legentName.equals(fcllegentName)) {
                    OrderedContainerList.add(fclBlContainer);
                }
            }
        }
        return OrderedContainerList;
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
