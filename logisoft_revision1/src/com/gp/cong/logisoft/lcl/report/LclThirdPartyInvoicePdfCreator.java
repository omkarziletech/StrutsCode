/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author palraj
 */
package com.gp.cong.logisoft.lcl.report;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.logisoft.beans.BookingChargesBean;
import com.gp.cong.logisoft.beans.ImportsManifestBean;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclBookingImport;
import com.gp.cong.logisoft.domain.lcl.LclBookingPiece;
import com.gp.cong.logisoft.domain.lcl.LclBookingPieceUnit;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.reports.ReportFormatMethods;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.PdfGState;
import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

public class LclThirdPartyInvoicePdfCreator extends ReportFormatMethods {

    private static final Logger log = Logger.getLogger(LclArInvoicePdfCreator.class);
    private LclUtils lclUtils = new LclUtils();
    Document document = null;
    PdfWriter pdfWriter = null;
    protected PdfTemplate total;
    protected BaseFont helv;
    private String contextPath = null;
    private Long fileId;

    public LclThirdPartyInvoicePdfCreator() {
    }

    public LclThirdPartyInvoicePdfCreator(String contextPath, Long fileId) {
        this.contextPath = contextPath;
        this.fileId = fileId;
    }

    public void initialize(String fileName, String contextPath, Long fileId) throws FileNotFoundException,
            DocumentException,
            Exception {
        document = new Document(PageSize.A4);
        document.setMargins(10, 10, 10, 10);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(fileName));
        pdfWriter.setPageEvent(new LclThirdPartyInvoicePdfCreator(contextPath, fileId));
        String companyName = null != LoadLogisoftProperties.getProperty("application.fclBl.print.companyFullName")
                ? LoadLogisoftProperties.getProperty("application.fclBl.print.companyFullName").toUpperCase() : "OTI cargo Inc";
        HeaderFooter footer = new HeaderFooter(
                new Phrase("Thank you for choosing " + companyName + " for all your shipping needs", new Font(Font.HELVETICA, 10, Font.ITALIC, Color.BLACK)), false);
        footer.setBorder(Rectangle.NO_BORDER);
        footer.setAlignment(Element.ALIGN_CENTER);
        document.setFooter(footer);
        document.open();
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

    @Override
    public void onCloseDocument(PdfWriter writer, Document document) {
        total.beginText();
        total.setFontAndSize(helv, 7);
        total.setTextMatrix(0, 0);
        total.showText(String.valueOf(writer.getPageNumber() - 1));
        total.endText();
    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        onStartThirdPartyInvoicePage(writer, document);
    }

    public void onStartThirdPartyInvoicePage(PdfWriter writer, Document document) {
        try {
            String logoImage = "";
            SystemRulesDAO systemRulesDAO = new SystemRulesDAO();
            String companyAddress = systemRulesDAO.getSystemRulesByCode("CompanyAddress");
            String companyPhone = systemRulesDAO.getSystemRulesByCode("CompanyPhone");
            String companyFax = systemRulesDAO.getSystemRulesByCode("CompanyFax");
            Long fileId = LclThirdPartyInvoicePdfCreator.this.fileId;
            PdfPCell cell = new PdfPCell();
            PdfPTable headingMainTable = new PdfPTable(1);
            headingMainTable.setWidthPercentage(100);
            PdfPTable headingTable = new PdfPTable(2);
            headingTable.setWidths(new float[]{25, 75});
            PdfPTable imgTable = new PdfPTable(1);
            imgTable.setWidthPercentage(100);
            String logoStatus = "";
            Image img;
            LclBooking lclBooking = new LCLBookingDAO().getByProperty("lclFileNumber.id", fileId);
            if (lclBooking != null && lclBooking.getSupAcct() != null && lclBooking.getSupAcct().getAccountno() != null) {
                logoStatus = new LCLBookingDAO().getLogoStatus(lclBooking.getSupAcct().getAccountno());
            }
            if ("Y".equalsIgnoreCase(logoStatus)) {
                logoImage = LoadLogisoftProperties.getProperty("application.image.logo");
                img = Image.getInstance(contextPath + logoImage);
                img.scalePercent(45);
            } else {
                logoImage = LoadLogisoftProperties.getProperty("application.image.econo.logo");
                img = Image.getInstance(contextPath + logoImage);
                img.scalePercent(75);
            }
            PdfPCell logoCell = new PdfPCell(img);
            logoCell.setBorder(Rectangle.NO_BORDER);
            logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            logoCell.setVerticalAlignment(Element.ALIGN_LEFT);
            logoCell.setPaddingLeft(-14);
            imgTable.addCell(logoCell);
            PdfPTable addrTable = new PdfPTable(1);
            addrTable.setWidthPercentage(100);
            PdfPTable invoiceFacturaTable = new PdfPTable(3);
            invoiceFacturaTable.setWidthPercentage(100);
            invoiceFacturaTable.setWidths(new float[]{50, 40, 10});
            StringBuilder stringBuilder = new StringBuilder();
            addrTable.addCell(makeCellCenterNoBorderFclBL("MAILING ADDRESS: " + (CommonUtils.isNotEmpty(companyAddress) ? companyAddress.toUpperCase() : "")));
            stringBuilder.append("TEL: ");
            stringBuilder.append(CommonUtils.isNotEmpty(companyPhone) ? companyPhone : "").append(" / ");
            stringBuilder.append("FAX: ");
            stringBuilder.append(CommonUtils.isNotEmpty(companyFax) ? companyFax : "");
            addrTable.addCell(makeCellCenterNoBorderFclBL(stringBuilder.toString()));
            addrTable.addCell(makeCellLeftNoBorderFclBL(""));
            addrTable.addCell(makeCellLeftNoBorderFclBL(""));
            invoiceFacturaTable.addCell(makeCellLeftNoBorderFclBL(""));
            cell = makeCell("THIRD PARTY INVOICE", Element.ALIGN_CENTER, new Font(Font.HELVETICA, 12, Font.BOLD, Color.RED), 0.06f);
            invoiceFacturaTable.addCell(cell);
            invoiceFacturaTable.addCell(makeCellLeftNoBorderFclBL(""));
            cell = new PdfPCell();
            cell.addElement(invoiceFacturaTable);
            cell.setBorder(0);
            addrTable.addCell(cell);
            addrTable.addCell(makeCellLeftNoBorderFclBL(""));
            addrTable.addCell(makeCellLeftNoBorderFclBL(""));

            cell = new PdfPCell();
            cell.addElement(imgTable);
            cell.setBorder(0);
            cell.setPaddingLeft(-15);
            headingTable.addCell(cell);
            cell = new PdfPCell();
            cell.addElement(addrTable);
            cell.setBorder(0);
            headingTable.addCell(cell);
            cell = makeCellLeftNoBorderFclBL("");
            cell.setBorderWidthRight(0.06f);
            cell.setBorderWidthLeft(0.06f);
            cell.setBorderWidthTop(0.06f);
            cell.setBorderWidthBottom(0.0f);
            cell.addElement(headingTable);
            headingMainTable.addCell(cell);
            document.add(headingMainTable);
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    public void createBody(String realPath, String fileId, String fileNumber, String outputFileName, String documentName, User loginUser) throws DocumentException, Exception {

        String pieceCount = "";
        String pieceDesc = "";
        String cube = "";
        String weight = "";
        String unitsNumber = "";
        String containerNo = "";

        StringBuilder thirdPartyAddress = new StringBuilder("");
        StringBuilder eciShipment = new StringBuilder();
        LclUnitSs lclUnitSs = null;

        LclFileNumber lclFileNumber = new LclFileNumberDAO().findById(Long.parseLong(fileId));
        LclBooking lclBooking = new LCLBookingDAO().getByProperty("lclFileNumber.id", Long.parseLong(fileId));
        List<LclBookingPiece> lclBookingPiecesList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", lclBooking.getLclFileNumber().getId());
        //Third party values

        if (lclBooking.getThirdPartyAcct() != null && lclBooking.getThirdPartyAcct().getPrimaryCustAddr() != null && lclBooking.getThirdPartyAcct().getPrimaryCustAddr().getAddress1() != null) {
            thirdPartyAddress.append(lclBooking.getThirdPartyAcct().getPrimaryCustAddr().getAddress1().toUpperCase()).append("\n");
            thirdPartyAddress.append("\n");
        }
        if (lclBooking.getThirdPartyAcct() != null && lclBooking.getThirdPartyAcct().getPrimaryCustAddr() != null && lclBooking.getThirdPartyAcct().getPrimaryCustAddr().getCity2() != null) {
            thirdPartyAddress.append(lclBooking.getThirdPartyAcct().getPrimaryCustAddr().getCity2().toString().toUpperCase()).append(" ");
        }
        if (lclBooking.getThirdPartyAcct() != null && lclBooking.getThirdPartyAcct().getPrimaryCustAddr() != null && lclBooking.getThirdPartyAcct().getPrimaryCustAddr().getState() != null) {
            thirdPartyAddress.append(lclBooking.getThirdPartyAcct().getPrimaryCustAddr().getState().toString().toUpperCase()).append(" ");
        }
        if (lclBooking.getThirdPartyAcct() != null && lclBooking.getThirdPartyAcct().getPrimaryCustAddr() != null && lclBooking.getThirdPartyAcct().getPrimaryCustAddr().getZip() != null) {
            thirdPartyAddress.append(lclBooking.getThirdPartyAcct().getPrimaryCustAddr().getZip());
        }

        //commodidty values
        if (!lclBookingPiecesList.isEmpty()) {
            for (LclBookingPiece lclbookingPiece : lclBookingPiecesList) {
                if (lclbookingPiece.getBookedPieceCount() != null) {
                    pieceCount = lclbookingPiece.getBookedPieceCount().toString();
                }

                if (lclbookingPiece.getPieceDesc() != null) {
                    pieceDesc = lclbookingPiece.getPieceDesc().toUpperCase();
                }

                if (lclbookingPiece.getBookedWeightMetric() != null) {
                    weight = lclbookingPiece.getBookedWeightImperial().toString();
                }

                if (lclbookingPiece.getBookedVolumeMetric() != null) {
                    cube = lclbookingPiece.getBookedVolumeImperial().toString();
                }

                //container values
                if (!lclbookingPiece.getLclBookingPieceUnitList().isEmpty()) {
                    LclBookingPieceUnit lclBookingPieceUnits = lclbookingPiece.getLclBookingPieceUnitList().get(0);
                    if (lclBookingPieceUnits != null) {
                        lclUnitSs = lclBookingPieceUnits.getLclUnitSs();

                        if (CommonFunctions.isNotNull(lclUnitSs) && CommonFunctions.isNotNull(lclUnitSs.getLclUnit()) && CommonFunctions.isNotNull(lclUnitSs.getLclUnit().getUnitNo())) {
                            containerNo = lclUnitSs.getLclUnit().getUnitNo().toUpperCase();
                            unitsNumber = lclUnitSs.getLclUnit().getUnitNo().toUpperCase() + " (" + lclUnitSs.getLclUnit().getUnitType().getDescription().toUpperCase() + " )";
                        }
                    }
                }
            }

        }

        //eciShipment values
        if (lclBooking.getTerminal().getTrmnum() != null) {
            eciShipment.append(lclBooking.getTerminal().getTrmnum()).append("-");
        }

        if (lclBooking.getPortOfLoading() != null && lclBooking.getPortOfLoading().getUnLocationCode() != null) {
            eciShipment.append(lclBooking.getPortOfLoading().getUnLocationCode().toUpperCase()).append("-");
        }

        if (lclBooking.getPortOfDestination() != null && lclBooking.getPortOfDestination().getUnLocationCode() != null) {
            eciShipment.append(lclBooking.getPortOfDestination().getUnLocationCode().toUpperCase()).append("-");
        }

        if (null != lclUnitSs && lclUnitSs.getLclSsHeader() != null && lclUnitSs.getLclSsHeader().getScheduleNo() != null) {
            eciShipment.append(lclUnitSs.getLclSsHeader().getScheduleNo());
        }

        PdfPCell cell = new PdfPCell();
        PdfPTable mainTable = makeTable(2);
        mainTable.setWidthPercentage(100);
        PdfPTable clientPTable = new PdfPTable(2);
        clientPTable.setWidthPercentage(101);
        clientPTable.setWidths(new float[]{10, 90});
        clientPTable.setKeepTogether(true);
        cell = makeCell("TO:", Element.ALIGN_LEFT, headingFontSize8, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthBottom(0.06f);
        clientPTable.addCell(cell);
        cell = makeCell(lclBooking.getThirdPartyAcct().getAccountName(), Element.ALIGN_LEFT, blackFontForFclAr, 0);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthBottom(0.06f);
        clientPTable.addCell(cell);
        cell = makeCell("", Element.ALIGN_CENTER, headingFontSize8, 0);
        cell.setBorderWidthRight(0.06f);
        clientPTable.addCell(cell);

        cell = makeCell(thirdPartyAddress.toString(), Element.ALIGN_LEFT, blackFontForFclAr, 0);
        cell.setBorderWidthRight(0.06f);
        cell.setMinimumHeight(30);
        clientPTable.addCell(cell);
        cell = makeCell("ATTN", Element.ALIGN_CENTER, headingFontSize8, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthBottom(0.06f);
        clientPTable.addCell(cell);
        cell = makeCell("", Element.ALIGN_LEFT, blackFontForFclBl, 0);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthBottom(0.06f);
        clientPTable.addCell(cell);
        cell.addElement(clientPTable);
        cell.setBorder(0);
        cell.setBorderWidthRight(0.0f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthTop(0.0f);
        cell.setBorderWidthBottom(0.0f);
        mainTable.addCell(cell);

        Paragraph p = null;
        PdfPTable invoiceMainTable = new PdfPTable(1);
        invoiceMainTable.setWidthPercentage(101.5f);
        invoiceMainTable.setKeepTogether(true);
        PdfPTable invoicePTable1 = new PdfPTable(4);
        invoicePTable1.setWidths(new float[]{10, 25, 20, 45});
        invoicePTable1.setWidthPercentage(101);
        invoicePTable1.addCell(makeCellLeftNoBorderFclBL(""));
        cell = makeCell("INVOICE NO.", Element.ALIGN_CENTER, headingFontSize8, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthBottom(0.06f);
        invoicePTable1.addCell(cell);
        cell = makeCell("DATE", Element.ALIGN_CENTER, headingFontSize8, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthBottom(0.06f);
        invoicePTable1.addCell(cell);
        cell = makeCell("REFERENCE NO.", Element.ALIGN_CENTER, headingFontSize8, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthBottom(0.06f);
        invoicePTable1.addCell(cell);
        invoicePTable1.addCell(makeCellLeftNoBorderFclBL(""));
        cell = makeCell("IMP-" + fileNumber, Element.ALIGN_CENTER, blackFontForFclBl, 0);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthBottom(0.06f);
        invoicePTable1.addCell(cell);

        cell = makeCell("" + lclFileNumber.getCreatedDatetime(), Element.ALIGN_CENTER, blackFontForFclBl, 0);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthBottom(0.06f);
        invoicePTable1.addCell(cell);
        cell = makeCell("", Element.ALIGN_CENTER, blackFontForFclBl, 0);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthBottom(0.06f);
        invoicePTable1.addCell(cell);
        cell = makeCellLeftNoBorderFclBL("");
        cell.setColspan(4);
        invoicePTable1.addCell(cell);
        cell = new PdfPCell();
        cell.addElement(invoicePTable1);
        cell.setBorder(0);
        invoiceMainTable.addCell(cell);

        PdfPTable invoicePTable2 = new PdfPTable(3);
        invoicePTable2.setWidths(new float[]{10, 40, 50});
        invoicePTable2.setWidthPercentage(101);

        invoicePTable2.addCell(makeCellLeftNoBorderFclBL(""));
        cell = makeCell("BILL TO ACCT NO.", Element.ALIGN_CENTER, headingFontSize8, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthBottom(0.06f);
        invoicePTable2.addCell(cell);
        cell = makeCell("" + lclBooking.getThirdPartyAcct().getAccountno(), Element.ALIGN_CENTER, blackFontForFclBl, 0);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthBottom(0.06f);
        invoicePTable2.addCell(cell);

        cell = new PdfPCell();
        cell.addElement(invoicePTable2);
        cell.setBorder(0);
        invoiceMainTable.addCell(cell);

        cell = new PdfPCell();
        cell.addElement(invoiceMainTable);
        cell.setBorder(0);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthLeft(0.0f);
        cell.setBorderWidthTop(0.0f);
        cell.setBorderWidthBottom(0.0f);
        mainTable.addCell(cell);

        PdfPTable othersTable = makeTable(4);
        othersTable.setWidthPercentage(100f);
        othersTable.setWidths(new float[]{25, 25, 25, 25});
        cell = makeCell("CONTAINER NO.", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthRight(0.06f);
        othersTable.addCell(cell);
        cell = makeCell("", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthRight(0.06f);
        othersTable.addCell(cell);
        cell = makeCell("ORIGIN", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthRight(0.06f);
        othersTable.addCell(cell);
        cell = makeCell("DESTINATION", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthTop(0.06f);
        othersTable.addCell(cell);
        othersTable = fillMarksAndVoyageContinerInformation(othersTable, containerNo, lclUtils.getConcatenatedOriginByUnlocation(lclBooking.getPortOfLoading()), lclUtils.getConcatenatedOriginByUnlocation(lclBooking.getPortOfDestination()));
        cell = makeCell("ECI SHIPMENT FILE NUMBER", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
        cell.setColspan(2);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        othersTable.addCell(cell);

        cell = makeCell("COMMENTS", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
        cell.setColspan(2);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        othersTable.addCell(cell);
        cell = makeCell("", Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        cell.setColspan(2);
        cell.setBorderWidthRight(0.06f);
        othersTable.addCell(cell);
        cell = makeCell("", Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        cell.setColspan(2);
        othersTable.addCell(cell);
        cell = makeCell("" + eciShipment, Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        cell.setColspan(2);
        cell.setBorderWidthRight(0.06f);
        othersTable.addCell(cell);
        cell = makeCell("", Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        cell.setColspan(2);
        othersTable.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(2);
        cell.addElement(othersTable);
        cell.setBorder(0);
        cell.setPadding(0f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthLeft(0.06f);
        mainTable.addCell(cell);

        cell = makeCell("DESCRIPTION / DETAILS", Element.ALIGN_CENTER, headingFont, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthBottom(0.06f);
        cell.setColspan(4);
        mainTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setColspan(4);
        PdfPCell pcell = null;
        PdfPTable ptable = new PdfPTable(4);
        ptable.setWidthPercentage(100f);
        ptable.setWidths(new float[]{0.08f, 0.93f, 0.09f, 5.28f});

        //DockReceipts
        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        ptable.addCell(pcell);
        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        p = new Paragraph(9f, "Dock Receipts", blackFontForFclAr);
        p.setAlignment(Element.ALIGN_RIGHT);
        pcell.addElement(p);
        ptable.addCell(pcell);
        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        p = new Paragraph(9f, "  : ", blackFontForFclAr);
        p.setAlignment(Element.ALIGN_LEFT);
        pcell.addElement(p);
        ptable.addCell(pcell);

        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        p = new Paragraph(9f, "IMP-" + fileNumber, blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_LEFT);
        pcell.addElement(p);
        ptable.addCell(pcell);

        //Origin
        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        ptable.addCell(pcell);
        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        p = new Paragraph(9f, "Description Of Packages And Goods", blackFontForFclAr);
        p.setAlignment(Element.ALIGN_RIGHT);
        pcell.addElement(p);
        ptable.addCell(pcell);

        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        p = new Paragraph(9f, "  : ", blackFontForFclAr);
        p.setAlignment(Element.ALIGN_LEFT);
        pcell.addElement(p);
        ptable.addCell(pcell);

        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        p = new Paragraph(9f, "" + pieceDesc.toUpperCase(), blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_LEFT);
        pcell.addElement(p);
        ptable.addCell(pcell);
        //Destination
        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        ptable.addCell(pcell);
        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        p = new Paragraph(9f, "Pieces", blackFontForFclAr);
        p.setAlignment(Element.ALIGN_RIGHT);
        pcell.addElement(p);
        ptable.addCell(pcell);

        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        p = new Paragraph(9f, "  : ", blackFontForFclAr);
        p.setAlignment(Element.ALIGN_LEFT);
        pcell.addElement(p);
        ptable.addCell(pcell);

        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        p = new Paragraph(9f, "" + pieceCount, blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_LEFT);
        pcell.addElement(p);
        ptable.addCell(pcell);
        //MasterBL
        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        ptable.addCell(pcell);
        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        p = new Paragraph(9f, "Cube", blackFontForFclAr);
        p.setAlignment(Element.ALIGN_RIGHT);
        pcell.addElement(p);
        ptable.addCell(pcell);

        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        p = new Paragraph(9f, "  : ", blackFontForFclAr);
        p.setAlignment(Element.ALIGN_LEFT);
        pcell.addElement(p);
        ptable.addCell(pcell);

        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        p = new Paragraph(9f, "" + cube, blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_LEFT);
        pcell.addElement(p);
        ptable.addCell(pcell);
        //Destination
        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        ptable.addCell(pcell);
        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        p = new Paragraph(9f, "Weight", blackFontForFclAr);
        p.setAlignment(Element.ALIGN_RIGHT);
        pcell.addElement(p);
        ptable.addCell(pcell);

        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        p = new Paragraph(9f, "  : ", blackFontForFclAr);
        p.setAlignment(Element.ALIGN_LEFT);
        pcell.addElement(p);
        ptable.addCell(pcell);

        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        p = new Paragraph(9f, "" + weight, blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_LEFT);
        pcell.addElement(p);
        ptable.addCell(pcell);

        //ContainerNo
        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        ptable.addCell(pcell);
        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        p = new Paragraph(9f, "Unit", blackFontForFclAr);
        p.setAlignment(Element.ALIGN_RIGHT);
        pcell.addElement(p);
        ptable.addCell(pcell);

        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        p = new Paragraph(9f, "  : ", blackFontForFclAr);
        p.setAlignment(Element.ALIGN_LEFT);
        pcell.addElement(p);
        ptable.addCell(pcell);

        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        p = new Paragraph(9f, "" + unitsNumber, blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_LEFT);
        pcell.addElement(p);
        ptable.addCell(pcell);

        cell.addElement(ptable);
        mainTable.addCell(cell);

        //dr level charge
        Paragraph p1 = null;
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setColspan(4);
        PdfPTable ntable = new PdfPTable(3);
        PdfPCell ncell = null;
        ntable.setWidthPercentage(100f);
        ntable.setWidths(new float[]{1f, 1f, 2f});
        int count = 0;
        List<BookingChargesBean> lclBookingAcList = null;
        String[] billToParty;
        billToParty = new String[]{"T"};
        List<String> billToPartyList = Arrays.asList(billToParty);
        lclBookingAcList = new LclCostChargeDAO().findBybookingAcId(lclBooking.getLclFileNumber().getId().toString(),billToPartyList);
        if (lclBookingAcList != null) {
            for (int i = 0; i < lclBookingAcList.size(); i++) {
                BookingChargesBean lclBookingAc = (BookingChargesBean) lclBookingAcList.get(i);
                if (CommonFunctions.isNotNull(lclBookingAc) && lclBookingAc.getArBillToParty().equalsIgnoreCase("T")) {
                    ncell = new PdfPCell();
                    ncell.setBorder(0);
                    if (count == 0) {
                        p1 = new Paragraph(5f, "IMP-" + fileNumber, blackFontForFclAr);
                    } else {
                        p1 = new Paragraph(5f, "", blackFontForFclAr);
                    }
                    p1.setAlignment(Element.ALIGN_LEFT);
                    ncell.addElement(p1);
                    ntable.addCell(ncell);

                    ncell = new PdfPCell();
                    ncell.setBorder(0);
                    ncell.setPaddingLeft(-55f);
                    p1 = new Paragraph(5f, "", blackFontForFclAr);
                    p1.setAlignment(Element.ALIGN_LEFT);
                    ncell.addElement(p1);
                    ntable.addCell(ncell);

                    ncell = new PdfPCell();
                    ncell.setBorder(0);
                    ncell.setPaddingLeft(-35f);
                    if (count == 0) {
                        p1 = new Paragraph(5f, "" + lclBooking.getFinalDestination().getUnLocationName(), blackFontForFclAr);
                    } else {
                        p1 = new Paragraph(5f, "", blackFontForFclAr);
                    }
                    p1.setAlignment(Element.ALIGN_LEFT);
                    ncell.addElement(p1);
                    ntable.addCell(ncell);

                    ncell = new PdfPCell();
                    ncell.setBorder(0);
                    p1 = new Paragraph(5f, "", blackBoldFontSize6);
                    ncell.addElement(p1);
                    ntable.addCell(ncell);

                    ncell = new PdfPCell();
                    ncell.setBorder(0);
                    p1 = new Paragraph(5f, "" + lclBookingAc.getChargeCode().toUpperCase(), blackBoldFontSize6);
                    ncell.addElement(p1);
                    ntable.addCell(ncell);

                    ncell = new PdfPCell();
                    ncell.setBorder(0);
                    p1 = new Paragraph(5f, "$" + NumberUtils.convertToTwoDecimal(lclBookingAc.getTotalAmt().doubleValue()), blackBoldFontSize6);
                    ncell.addElement(p1);
                    ntable.addCell(ncell);
                    count++;
                }
            }
            cell.addElement(ntable);
            mainTable.addCell(cell);
        }

        PdfPTable chargesTable = makeTable(4);
        chargesTable.setWidthPercentage(100.5f);
        chargesTable.setWidths(new float[]{45, 35, 5, 15});
        cell = makeCell("CHARGES", Element.ALIGN_CENTER, headingFont, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        cell.setColspan(4);
        chargesTable.addCell(cell);
        NumberFormat number = new DecimalFormat("###,###,##0.00");
        double totalCharges = 0.00;
        int chargeCount = 0;

        if (lclBookingAcList != null) {
            for (int i = 0; i < lclBookingAcList.size(); i++) {
                BookingChargesBean lclBookingAc = (BookingChargesBean) lclBookingAcList.get(i);
                if (CommonFunctions.isNotNull(lclBookingAc) && lclBookingAc.getArBillToParty().equalsIgnoreCase("T")) {
                    chargeCount++;
                    cell = new PdfPCell();
                    cell.setBorder(0);
                    p = new Paragraph(6f, "", blackFontForFclBl);
                    p.setAlignment(Element.ALIGN_RIGHT);
                    cell.addElement(p);
                    chargesTable.addCell(cell);

                    cell = new PdfPCell();
                    cell.setBorder(8);
                    p = new Paragraph(6f, "" + lclBookingAc.getChargeCode().toUpperCase(), blackFontForFclBl);
                    p.setAlignment(Element.ALIGN_RIGHT);
                    cell.addElement(p);
                    chargesTable.addCell(cell);

                    cell = makeCell("$", Element.ALIGN_CENTER, blackFontForFclBl, 0.06f);
                    if (chargeCount == 1) {
                        cell.setBorderWidthTop(0.0f);
                        cell.setBorderWidthRight(0.0f);
                        cell.setBorderWidthBottom(0.0f);
                    } else {
                        cell.setBorderWidthRight(0.0f);
                        cell.setBorderWidthBottom(0.0f);
                    }
                    chargesTable.addCell(cell);
                    cell = makeCell(number.format(lclBookingAc.getTotalAmt().doubleValue()), Element.ALIGN_RIGHT, blackFontForFclBl, Rectangle.BOX);
                    if (chargeCount == 1) {
                        cell.setBorderWidth(0.0f);
                    } else {
                        cell.setBorderWidthLeft(0.0f);
                        cell.setBorderWidthRight(0.0f);
                        cell.setBorderWidthBottom(0.0f);
                    }
                    chargesTable.addCell(cell);
                    totalCharges += lclBookingAc.getTotalAmt().doubleValue();
                }
            }
        }

        for (int i = 0; i < (14 - chargeCount); i++) {
            chargesTable.addCell(makeCellLeftNoBorderFclBL(""));
            chargesTable.addCell(makeCellRightNoBorderFclBL(""));
            cell = makeCell("", Element.ALIGN_CENTER, blackFontForFclBl, 0.06f);
            cell.setBorderWidthRight(0.0f);
            cell.setBorderWidthBottom(0.0f);
            chargesTable.addCell(cell);
            cell = makeCell("", Element.ALIGN_RIGHT, blackFontForFclBl, 0.06f);
            cell.setBorderWidthLeft(0.0f);
            cell.setBorderWidthRight(0.0f);
            cell.setBorderWidthBottom(0.0f);
            cell.setMinimumHeight(10f);
            chargesTable.addCell(cell);
        }
        cell = makeCell("", Element.ALIGN_LEFT, blackFontForFclBl, 0);
        cell.setBorderWidthTop(0.06f);
        chargesTable.addCell(cell);
        cell = makeCell("INVOICE TOTAL ($-USD)", Element.ALIGN_CENTER, blackFontForFclBl, 0);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthRight(0.06f);
        chargesTable.addCell(cell);
        cell = makeCell("$", Element.ALIGN_CENTER, blackFontForFclBl, 0);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        chargesTable.addCell(cell);
        cell = makeCell("" + NumberUtils.convertToTwoDecimal(totalCharges), Element.ALIGN_RIGHT, blackFontForFclBl, 0);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        chargesTable.addCell(cell);
        cell = makeCell("", Element.ALIGN_CENTER, blackFontForFclBl, 0);
        cell.setColspan(4);
        chargesTable.addCell(cell);
        chargesTable.setKeepTogether(true);
        cell = new PdfPCell();
        cell.setColspan(2);
        cell.addElement(chargesTable);
        cell.setBorder(0);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthLeft(0.06f);
        mainTable.addCell(cell);

        PdfPTable paidTable = makeTable(5);
        paidTable.setWidthPercentage(100.5f);
        paidTable.setWidths(new float[]{45, 25, 10, 5, 15});

        cell = makeCell("", Element.ALIGN_LEFT, blackFontForFclBl, 1);
        paidTable.addCell(cell);
        cell = makeCell("PLEASE PAY THIS AMOUNT ", Element.ALIGN_CENTER, blackFontForFclBl, 0);
        cell.setPaddingLeft(-12f);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthTop(0.06f);
        cell.setColspan(2);
        paidTable.addCell(cell);
        cell = makeCell("$", Element.ALIGN_CENTER, blackFontForFclBl, 0);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthTop(0.06f);
        paidTable.addCell(cell);
        cell = makeCell("" + NumberUtils.convertToTwoDecimal(totalCharges), Element.ALIGN_RIGHT, blackFontForFclBl, 0);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthTop(0.06f);
        paidTable.addCell(cell);
        cell = makeCell("", Element.ALIGN_CENTER, blackFontForFclBl, 0);
        cell.setColspan(5);
        paidTable.addCell(cell);
        //paidTable.setKeepTogether(true);

        cell = new PdfPCell();
        cell.setColspan(2);
        cell.addElement(paidTable);
        cell.setBorder(0);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthLeft(0.06f);
        mainTable.addCell(cell);

        PdfPTable commandTable = new PdfPTable(1);
        commandTable.setWidthPercentage(100);
        cell = makeCell("", Element.ALIGN_CENTER, headingFont, Rectangle.NO_BORDER);
        commandTable.addCell(cell);
        cell = makeCell("", Element.ALIGN_CENTER, headingFont, Rectangle.NO_BORDER);
        commandTable.addCell(cell);
        cell = makeCell("THIS INVOICE IS DUE UPON RECEIPT", Element.ALIGN_CENTER, new Font(Font.HELVETICA, 10, Font.BOLDITALIC, Color.BLACK), Rectangle.NO_BORDER);
        commandTable.addCell(cell);
        cell = makeCell("", Element.ALIGN_CENTER, headingFont, Rectangle.NO_BORDER);
        commandTable.addCell(cell);
        cell = makeCell("", Element.ALIGN_CENTER, headingFont, Rectangle.NO_BORDER);
        commandTable.addCell(cell);
        cell = makeCell("", Element.ALIGN_CENTER, headingFont, Rectangle.NO_BORDER);
        commandTable.addCell(cell);
        cell = makeCell("", Element.ALIGN_CENTER, headingFont, Rectangle.NO_BORDER);
        commandTable.addCell(cell);
        cell = makeCell("", Element.ALIGN_CENTER, headingFont, Rectangle.NO_BORDER);
        commandTable.addCell(cell);
        cell = new PdfPCell();
        cell.setColspan(2);
        cell.addElement(commandTable);
        cell.setBorder(0);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setExtraParagraphSpace(10f);
        mainTable.addCell(cell);

        PdfPTable bankDetailsTable = new PdfPTable(3);
        bankDetailsTable.setWidthPercentage(100.5f);
        bankDetailsTable.setWidths(new float[]{25, 50, 25});
        PdfPTable bankDetails = new PdfPTable(1);
        bankDetails.setWidthPercentage(100f);

        SystemRulesDAO systemRulesDAO = new SystemRulesDAO();
        String eftBank = systemRulesDAO.getSystemRulesByCode("EFTBank");
        String eftBankAddress = systemRulesDAO.getSystemRulesByCode("EFTBankAddress");
        String eftAcctName = systemRulesDAO.getSystemRulesByCode("EFTAcctName");
        String eftAccountNo = systemRulesDAO.getSystemRulesByCode("EFTAccountNo");
        String eftABANo = systemRulesDAO.getSystemRulesByCode("EFTABANo");

        bankDetailsTable.setKeepTogether(true);
        bankDetailsTable.addCell(makeCellLeftNoBorder(""));
        StringBuilder bankHead = new StringBuilder();
        bankHead.append(" PAYMENTS VIA WIRE TRANSFER SHOULD BE SENT ");
        cell = makeCell(bankHead.toString(), Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthTop(0.06f);
        bankDetailsTable.addCell(cell);
        bankDetailsTable.addCell(makeCell("", Element.ALIGN_CENTER, blackBoldFontSize6, 0));

        bankDetailsTable.addCell(makeCell("", Element.ALIGN_CENTER, blackBoldFontSize6, 0));
        cell = makeCell("AS FOLLOWS: ", Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthBottom(0.06f);
        bankDetailsTable.addCell(cell);
        bankDetailsTable.addCell(makeCell("", Element.ALIGN_CENTER, blackBoldFontSize6, 0));
        bankDetails.addCell(makeCell("BANK: " + eftBank, Element.ALIGN_CENTER, blackBoldFontSize6, 0));
        bankDetails.addCell(makeCell(eftBankAddress, Element.ALIGN_CENTER, blackBoldFontSize6, 0));
        bankDetails.addCell(makeCell("ACCT NAME: " + eftAcctName, Element.ALIGN_CENTER, blackBoldFontSize6, 0));
        bankDetails.addCell(makeCell("Acct no.: " + eftAccountNo, Element.ALIGN_CENTER, blackBoldFontSize6, 0));
        bankDetails.addCell(makeCell("ABA Routing no.: " + eftABANo, Element.ALIGN_CENTER, blackBoldFontSize6, 0));

        cell = makeCell("", Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        //  cell.setBorderWidthBottom(0.06f);
        bankDetailsTable.addCell(cell);
        cell = new PdfPCell();
        cell.addElement(bankDetails);
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthRight(0.06f);
        //  cell.setBorderWidthBottom(0.06f);
        bankDetailsTable.addCell(cell);

        cell = makeCell("", Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        // cell.setBorderWidthBottom(0.06f);
        bankDetailsTable.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(2);
        cell.setPadding(0f);
        cell.setBorder(0);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthBottom(0.06f);
        cell.addElement(bankDetailsTable);
        mainTable.addCell(cell);

        document.add(mainTable);
    }

    public PdfPTable fillMarksAndVoyageContinerInformation(PdfPTable particularsTable, String unitNumber, String originValues, String destinationValues)
            throws Exception {
        PdfPCell cell = null;
        cell = makeCellLeftNoBorderFontSize6(unitNumber);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidthRight(0.06f);
        particularsTable.addCell(cell);

        cell = makeCellLeftNoBorderFontSize6("");
        cell.setBorderWidthRight(0.06f);
        particularsTable.addCell(cell);
        cell = makeCellLeftNoBorderFontSize6(originValues.toString().toUpperCase());
        cell.setBorderWidthRight(0.06f);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        particularsTable.addCell(cell);
        cell = makeCellLeftNoBorderFontSize6(destinationValues.toString().toUpperCase());
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        particularsTable.addCell(cell);
        return particularsTable;
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        try {
            //---------------
            //this for print page number at the bottom in the format x of y
            PdfContentByte cb = writer.getDirectContent();
            cb.saveState();
//            String invoiceNO = " Invoice# " + "IMP-"+ lclBooking.getLclFileNumber();
            String text = "Page " + writer.getPageNumber() + " of ";
            float textBase = document.bottom() - (document.bottomMargin() - 30);
            //float textBase = document.bottom() - 20;
            float textSize = helv.getWidthPoint(text, 12);
            cb.beginText();
            cb.setFontAndSize(helv, 7);
            cb.setTextMatrix(document.left(), textBase);
//            cb.showText(invoiceNO);
            cb.setTextMatrix(document.left() + 280, textBase);
            cb.showText(text);
            cb.endText();
            cb.addTemplate(total, document.left() + 260 + textSize, textBase);
            cb.restoreState();
            BaseFont helv;
            PdfGState gstate;
            String waterMark = "";
            gstate = new PdfGState();
            gstate.setFillOpacity(0.10f);
            gstate.setStrokeOpacity(0.3f);
            PdfContentByte contentunder = writer.getDirectContent();
            contentunder.saveState();
            contentunder.setGState(gstate);
            contentunder.beginText();
            try {
                helv = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI,
                        BaseFont.NOT_EMBEDDED);
            } catch (Exception e) {
                log.info("onEndPage failed on " + new Date(), e);
                throw new ExceptionConverter(e);
            }
            contentunder.setFontAndSize(helv, 150);
            contentunder.showTextAligned(Element.ALIGN_CENTER, waterMark,
                    document.getPageSize().getWidth() / 2, document.getPageSize().getHeight() / 2, 45);
            contentunder.endText();
            contentunder.restoreState();
        } catch (Exception e) {
            log.info("onEndPage failed on " + new Date(), e);
            throw new ExceptionConverter(e);
        }
    }

    public void destroy() {
        document.close();
    }

    public String createReport(String realPath, String fileId, String fileNumber, String outputFileName, String documentName, User loginUser) throws Exception {
        this.initialize(outputFileName, realPath, Long.parseLong(fileId));
        this.createBody(realPath, fileId, fileNumber, outputFileName, documentName, loginUser);
        this.destroy();
        return "fileName";
    }
}
