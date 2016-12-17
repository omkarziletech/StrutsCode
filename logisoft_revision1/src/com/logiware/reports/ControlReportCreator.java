package com.logiware.reports;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.reports.ReportFormatMethods;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.bean.AccountingBean;
import com.logiware.form.ControlReportForm;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfDestination;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

import org.apache.log4j.Logger;

/**
 *
 * @author Lakshmi Narayanan
 */
public class ControlReportCreator extends ReportFormatMethods {

    private Document document = null;
    private PdfWriter pdfWriter = null;
    private String contextPath = null;
    private ControlReportForm controlReportForm = null;
    protected PdfTemplate totalNoOfPages = null;
    protected BaseFont fontForFooter = null;
    private static final Logger log = Logger.getLogger(ControlReportCreator.class);

    public ControlReportCreator() {
    }

    public ControlReportCreator(ControlReportForm controlReportForm, String contextPath) {
        this.controlReportForm = controlReportForm;
        this.contextPath = contextPath;
    }

    private void init(ControlReportForm controlReportForm, String contextPath, String fileName) throws Exception {
        this.controlReportForm = controlReportForm;
        this.contextPath = contextPath;
        document = new Document(PageSize.A4.rotate());
        document.setMargins(10, 10, 10, 50);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(fileName));
        pdfWriter.setPageEvent(new ControlReportCreator(controlReportForm, contextPath));
        document.open();
        pdfWriter.setOpenAction(PdfAction.gotoLocalPage(1, new PdfDestination(PdfDestination.XYZ, -1, -1, 0.75f), pdfWriter));
    }

    private PdfPTable createHeaderTable() throws Exception {
        PdfPTable headerTable = new PdfPTable(1);
        headerTable.setWidthPercentage(100);
        String imagePath = LoadLogisoftProperties.getProperty("application.image.logo");
        Image image = Image.getInstance(contextPath + imagePath);
        image.scalePercent(75);
        PdfPCell logoCell = new PdfPCell(image);
        logoCell.setBorder(Rectangle.NO_BORDER);
        logoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        headerTable.addCell(logoCell);
        PdfPCell emptyCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER);
        headerTable.addCell(emptyCell);
        headerTable.addCell(emptyCell);
        String title = "Accruals Control Report";
        if (CommonUtils.isEqualIgnoreCase(controlReportForm.getReportType(), CommonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE)) {
            title = "Account Receivable Control Report";
        }
        PdfPCell titleCell = makeCell(title, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFont1, Rectangle.NO_BORDER, Color.LIGHT_GRAY);
        headerTable.addCell(titleCell);
        headerTable.addCell(emptyCell);
        headerTable.addCell(emptyCell);
        String fromDate = "From Date : " + controlReportForm.getFromDate();
        PdfPCell fromDateCell = makeCell(fromDate, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER);
        String toDate = "To Date: "+controlReportForm.getToDate();
        PdfPCell toDateCell = makeCell(toDate, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER);
        headerTable.addCell(fromDateCell);
        headerTable.addCell(toDateCell);
        headerTable.addCell(emptyCell);
        headerTable.addCell(emptyCell);
        return headerTable;
    }

    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        totalNoOfPages = writer.getDirectContent().createTemplate(100, 100);
        totalNoOfPages.setBoundingBox(new Rectangle(-20, -20, 100, 100));
        try {
            fontForFooter = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
        } catch (Exception e) {
            log.info("onOpenDocument failed on " + new Date(),e);
        }
    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        try {
            document.add(this.createHeaderTable());
        } catch (Exception e) {
            log.info("onStartPage failed on " + new Date(),e);
        }
    }

    private void writeContents(ControlReportForm controlReportForm) throws Exception {
        if (CommonUtils.isNotEmpty(controlReportForm.getNumberOfBluScreenRecords())
                || CommonUtils.isNotEmpty(controlReportForm.getNumberOfLogiwareRecords())) {
            PdfPTable summaryHeadingTable = new PdfPTable(1);
            summaryHeadingTable.setWidthPercentage(100);
            summaryHeadingTable.addCell(makeCell("Summary", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFont1,
                    Rectangle.NO_BORDER, Color.LIGHT_GRAY));
            PdfPCell emptyCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER);
            summaryHeadingTable.addCell(emptyCell);
            summaryHeadingTable.addCell(emptyCell);
            document.add(summaryHeadingTable);
            PdfPTable summaryListTable = new PdfPTable(4);
            summaryListTable.setWidthPercentage(80);
            summaryListTable.addCell(makeCell("", Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, headingFont, Rectangle.NO_BORDER, Color.LIGHT_GRAY));
            summaryListTable.addCell(makeCell("Blue Screen", Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, headingFont, Rectangle.NO_BORDER, Color.LIGHT_GRAY));
            summaryListTable.addCell(makeCell("Logiware", Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, headingFont, Rectangle.NO_BORDER, Color.LIGHT_GRAY));
            summaryListTable.addCell(makeCell("Difference", Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, headingFont, Rectangle.NO_BORDER, Color.LIGHT_GRAY));
            summaryListTable.addCell(makeCell("No. Of Records :", Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
            summaryListTable.addCell(makeCell("" + controlReportForm.getNumberOfBluScreenRecords(),
                    Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
            summaryListTable.addCell(makeCell("" + controlReportForm.getNumberOfLogiwareRecords(),
                    Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
            summaryListTable.addCell(makeCell("" + (controlReportForm.getNumberOfBluScreenRecords() - controlReportForm.getNumberOfLogiwareRecords()),
                    Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
            summaryListTable.addCell(makeCell("Total Amount :", Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
            summaryListTable.addCell(makeCell(NumberUtils.formatNumber(controlReportForm.getTotalAmountInBlueScreen(), "###,###,##0.00"),
                    Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
            summaryListTable.addCell(makeCell(NumberUtils.formatNumber(controlReportForm.getTotalAmountInLogiware(), "###,###,##0.00"),
                    Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
            double difference = controlReportForm.getTotalAmountInBlueScreen() - controlReportForm.getTotalAmountInLogiware();
            summaryListTable.addCell(makeCell(NumberUtils.formatNumber(difference, "###,###,##0.00"),
                    Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
            document.add(summaryListTable);
            int blueScreenItemsSize = 0;
            int logiwareItemsSize = 0;
            double blueScreenTotal = 0d;
            double logiwareTotal = 0d;
            if (CommonUtils.isEqualIgnoreCase(controlReportForm.getReportType(), CommonConstants.TRANSACTION_TYPE_ACCRUALS)) {
                PdfPTable blueScreenAccrualsTable = new PdfPTable(6);
                blueScreenAccrualsTable.setWidthPercentage(100);
                blueScreenAccrualsTable.setWidths(new float[]{30f, 15f, 20f, 12f, 3f, 20f});
                PdfPCell blueScreenAccrualsTitleCell = makeCell("Blue Screen Detail",
                        Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFont1, Rectangle.NO_BORDER, Color.LIGHT_GRAY);
                blueScreenAccrualsTitleCell.setColspan(6);
                blueScreenAccrualsTable.addCell(blueScreenAccrualsTitleCell);
                if (CommonUtils.isNotEmpty(controlReportForm.getBlueScreenAccruals())) {
                    blueScreenItemsSize = controlReportForm.getBlueScreenAccruals().size();
                }
                PdfPCell blueScreenAccrualsFooterCell = makeCell(blueScreenItemsSize + " Blue Screen Accruals missed/conflict in Logiware",
                        Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER);
                blueScreenAccrualsFooterCell.setColspan(6);
                blueScreenAccrualsTable.addCell(blueScreenAccrualsFooterCell);
                if (CommonUtils.isNotEmpty(controlReportForm.getBlueScreenAccruals())) {
                    blueScreenAccrualsTable.addCell(makeCell("Vendor Name", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, headingFont, Rectangle.BOTTOM));
                    blueScreenAccrualsTable.addCell(makeCell("Vendor Number", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, headingFont, Rectangle.BOTTOM));
                    blueScreenAccrualsTable.addCell(makeCell("Invoice/Bl", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, headingFont, Rectangle.BOTTOM));
                    blueScreenAccrualsTable.addCell(makeCell("Amount", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFont, Rectangle.BOTTOM));
                    blueScreenAccrualsTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, headingFont, Rectangle.BOTTOM));
                    blueScreenAccrualsTable.addCell(makeCell("Blue Screen Key", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, headingFont, Rectangle.BOTTOM));
                    for (AccountingBean blueScreenAccrual : controlReportForm.getBlueScreenAccruals()) {
                        blueScreenAccrualsTable.addCell(makeCell(StringUtils.left(blueScreenAccrual.getVendorName(), 35),
                                Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER));
                        blueScreenAccrualsTable.addCell(makeCell(blueScreenAccrual.getVendorNumber(), Element.ALIGN_LEFT,
                                Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER));
                        blueScreenAccrualsTable.addCell(makeCell(blueScreenAccrual.getInvoiceOrBl(), Element.ALIGN_LEFT,
                                Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER));
                        blueScreenAccrualsTable.addCell(makeCell(NumberUtils.formatNumber(blueScreenAccrual.getAmount(), "###,###,##0.00"),
                                Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER));
                        blueScreenAccrualsTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER));
                        blueScreenAccrualsTable.addCell(makeCell(blueScreenAccrual.getApCostKey(), Element.ALIGN_LEFT,
                                Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER));
                        blueScreenTotal += blueScreenAccrual.getAmount();
                    }
                }
                PdfPCell blueScreenTotalCell = makeCell("Total :", Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER);
                blueScreenTotalCell.setColspan(3);
                blueScreenAccrualsTable.addCell(blueScreenTotalCell);
                blueScreenAccrualsTable.addCell(makeCell(NumberUtils.formatNumber(blueScreenTotal, "###,###,##0.00"),
                        Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
                blueScreenAccrualsTable.addCell(makeCell("", Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
                blueScreenAccrualsTable.addCell(makeCell("", Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
                document.add(blueScreenAccrualsTable);

                PdfPTable logiwareAccrualsTable = new PdfPTable(6);
                logiwareAccrualsTable.setWidthPercentage(100);
                logiwareAccrualsTable.setWidths(new float[]{30f, 15f, 20f, 12f, 3f, 20f});
                PdfPCell logiwareAccrualsTitleCell = makeCell("Logiware Detail",
                        Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFont1, Rectangle.NO_BORDER, Color.LIGHT_GRAY);
                logiwareAccrualsTitleCell.setColspan(6);
                logiwareAccrualsTable.addCell(logiwareAccrualsTitleCell);
                if (CommonUtils.isNotEmpty(controlReportForm.getLogiwareAccruals())) {
                    logiwareItemsSize = controlReportForm.getLogiwareAccruals().size();
                }
                PdfPCell logiwareAccrualsFooterCell = makeCell(logiwareItemsSize + " Logiware Accruals missed/conflict in Blue Screen",
                        Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER);
                logiwareAccrualsFooterCell.setColspan(6);
                logiwareAccrualsTable.addCell(logiwareAccrualsFooterCell);
                if (CommonUtils.isNotEmpty(controlReportForm.getLogiwareAccruals())) {
                    logiwareAccrualsTable.addCell(makeCell("Vendor Name", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, headingFont, Rectangle.BOTTOM));
                    logiwareAccrualsTable.addCell(makeCell("Vendor Number", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, headingFont, Rectangle.BOTTOM));
                    logiwareAccrualsTable.addCell(makeCell("Invoice/Bl", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, headingFont, Rectangle.BOTTOM));
                    logiwareAccrualsTable.addCell(makeCell("Amount", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFont, Rectangle.BOTTOM));
                    logiwareAccrualsTable.addCell(makeCell("", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFont, Rectangle.BOTTOM));
                    logiwareAccrualsTable.addCell(makeCell("Blue Screen Key", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, headingFont, Rectangle.BOTTOM));
                    for (AccountingBean logiwareAccrual : controlReportForm.getLogiwareAccruals()) {
                        logiwareAccrualsTable.addCell(makeCell(StringUtils.left(logiwareAccrual.getVendorName(), 35),
                                Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER));
                        logiwareAccrualsTable.addCell(makeCell(logiwareAccrual.getVendorNumber(), Element.ALIGN_LEFT,
                                Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER));
                        logiwareAccrualsTable.addCell(makeCell(logiwareAccrual.getInvoiceOrBl(), Element.ALIGN_LEFT,
                                Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER));
                        logiwareAccrualsTable.addCell(makeCell(NumberUtils.formatNumber(logiwareAccrual.getAmount(), "###,###,##0.00"),
                                Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER));
                        logiwareAccrualsTable.addCell(makeCell("", Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER));
                        logiwareAccrualsTable.addCell(makeCell(logiwareAccrual.getApCostKey(), Element.ALIGN_LEFT,
                                Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER));
                        logiwareTotal += logiwareAccrual.getAmount();
                    }
                }
                PdfPCell logiwareTotalCell = makeCell("Total :", Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER);
                logiwareTotalCell.setColspan(3);
                logiwareAccrualsTable.addCell(logiwareTotalCell);
                logiwareAccrualsTable.addCell(makeCell(NumberUtils.formatNumber(logiwareTotal, "###,###,##0.00"),
                        Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
                logiwareAccrualsTable.addCell(makeCell("", Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
                logiwareAccrualsTable.addCell(makeCell("", Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
                document.add(logiwareAccrualsTable);
            } else {
                PdfPTable blueScreenAccountReceivablesTable = new PdfPTable(4);
                blueScreenAccountReceivablesTable.setWidthPercentage(100);
                blueScreenAccountReceivablesTable.setWidths(new float[]{30f, 20f, 20f, 30f});
                PdfPCell blueScreenAccountReceivableTitleCell = makeCell("Blue Screen Detail",
                        Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFont1, Rectangle.NO_BORDER, Color.LIGHT_GRAY);
                blueScreenAccountReceivableTitleCell.setColspan(4);
                blueScreenAccountReceivablesTable.addCell(blueScreenAccountReceivableTitleCell);
                if (CommonUtils.isNotEmpty(controlReportForm.getBlueScreenAccountReceivables())) {
                    blueScreenItemsSize = controlReportForm.getBlueScreenAccountReceivables().size();
                }
                PdfPCell blueScreenAccountReceivablesFooterCell = makeCell(blueScreenItemsSize + " Blue Screen Account Receivables missed/conflict in Logiware",
                        Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER);
                blueScreenAccountReceivablesFooterCell.setColspan(4);
                blueScreenAccountReceivablesTable.addCell(blueScreenAccountReceivablesFooterCell);
                if (CommonUtils.isNotEmpty(controlReportForm.getBlueScreenAccountReceivables())) {
                    blueScreenAccountReceivablesTable.addCell(makeCell("Vendor Name", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, headingFont, Rectangle.BOTTOM));
                    blueScreenAccountReceivablesTable.addCell(makeCell("Vendor Number", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, headingFont, Rectangle.BOTTOM));
                    blueScreenAccountReceivablesTable.addCell(makeCell("Invoice/Bl", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, headingFont, Rectangle.BOTTOM));
                    blueScreenAccountReceivablesTable.addCell(makeCell("Amount", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFont, Rectangle.BOTTOM));
                    for (AccountingBean blueScreenAccountReceivable : controlReportForm.getBlueScreenAccountReceivables()) {
                        blueScreenAccountReceivablesTable.addCell(makeCell(StringUtils.left(blueScreenAccountReceivable.getVendorName(), 35),
                                Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER));
                        blueScreenAccountReceivablesTable.addCell(makeCell(blueScreenAccountReceivable.getVendorNumber(), Element.ALIGN_LEFT,
                                Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER));
                        blueScreenAccountReceivablesTable.addCell(makeCell(blueScreenAccountReceivable.getInvoiceOrBl(), Element.ALIGN_LEFT,
                                Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER));
                        blueScreenAccountReceivablesTable.addCell(makeCell(NumberUtils.formatNumber(blueScreenAccountReceivable.getAmount(), "###,###,##0.00"),
                                Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER));
                        blueScreenTotal += blueScreenAccountReceivable.getAmount();
                    }
                }
                PdfPCell blueScreenTotalCell = makeCell("Total :", Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER);
                blueScreenTotalCell.setColspan(3);
                blueScreenAccountReceivablesTable.addCell(blueScreenTotalCell);
                blueScreenAccountReceivablesTable.addCell(makeCell(NumberUtils.formatNumber(blueScreenTotal, "###,###,##0.00"),
                        Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
                document.add(blueScreenAccountReceivablesTable);

                PdfPTable logiwareAccountReceivablesTable = new PdfPTable(4);
                logiwareAccountReceivablesTable.setWidthPercentage(100);
                logiwareAccountReceivablesTable.setWidths(new float[]{30f, 20f, 20f, 30f});
                PdfPCell logiwareAccountReceivablesTitleCell = makeCell("Logiware Detail",
                        Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFont1, Rectangle.NO_BORDER, Color.LIGHT_GRAY);
                logiwareAccountReceivablesTitleCell.setColspan(4);
                logiwareAccountReceivablesTable.addCell(logiwareAccountReceivablesTitleCell);
                if (CommonUtils.isNotEmpty(controlReportForm.getLogiwareAccountReceivables())) {
                    logiwareItemsSize = controlReportForm.getLogiwareAccountReceivables().size();
                }
                PdfPCell logiwareAccountReceivablesFooterCell = makeCell(logiwareItemsSize + " Logiware Account Receivables missed/conflict in Blue Screen",
                        Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER);
                logiwareAccountReceivablesFooterCell.setColspan(4);
                logiwareAccountReceivablesTable.addCell(logiwareAccountReceivablesFooterCell);
                if (CommonUtils.isNotEmpty(controlReportForm.getLogiwareAccountReceivables())) {
                    logiwareAccountReceivablesTable.addCell(makeCell("Vendor Name", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, headingFont, Rectangle.BOTTOM));
                    logiwareAccountReceivablesTable.addCell(makeCell("Vendor Number", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, headingFont, Rectangle.BOTTOM));
                    logiwareAccountReceivablesTable.addCell(makeCell("Invoice/Bl", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, headingFont, Rectangle.BOTTOM));
                    logiwareAccountReceivablesTable.addCell(makeCell("Amount", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFont, Rectangle.BOTTOM));
                    for (AccountingBean logiwareAccountReceivable : controlReportForm.getLogiwareAccountReceivables()) {
                        logiwareAccountReceivablesTable.addCell(makeCell(StringUtils.left(logiwareAccountReceivable.getVendorName(), 35),
                                Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER));
                        logiwareAccountReceivablesTable.addCell(makeCell(logiwareAccountReceivable.getVendorNumber(), Element.ALIGN_LEFT,
                                Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER));
                        logiwareAccountReceivablesTable.addCell(makeCell(logiwareAccountReceivable.getInvoiceOrBl(), Element.ALIGN_LEFT,
                                Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER));
                        logiwareAccountReceivablesTable.addCell(makeCell(NumberUtils.formatNumber(logiwareAccountReceivable.getAmount(), "###,###,##0.00"),
                                Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER));
                        logiwareTotal += logiwareAccountReceivable.getAmount();
                    }
                }
                PdfPCell logiwareTotalCell = makeCell("Total :", Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER);
                logiwareTotalCell.setColspan(3);
                logiwareAccountReceivablesTable.addCell(logiwareTotalCell);
                logiwareAccountReceivablesTable.addCell(makeCell(NumberUtils.formatNumber(logiwareTotal, "###,###,##0.00"),
                        Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
                document.add(logiwareAccountReceivablesTable);
            }
            PdfPTable differenceTable = new PdfPTable(5);
            differenceTable.setWidthPercentage(100);
            differenceTable.setHorizontalAlignment(Element.ALIGN_CENTER);
            differenceTable.setWidths(new float[]{32f, 12f, 12f, 12f, 32f});
            PdfPCell differenceTitleCell = makeCell("Difference in Detail",
                    Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFont1, Rectangle.NO_BORDER, Color.LIGHT_GRAY);
            differenceTitleCell.setColspan(5);
            differenceTable.addCell(differenceTitleCell);
            differenceTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
            differenceTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
            differenceTable.addCell(makeCell("No. Of Records", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
            differenceTable.addCell(makeCell("Total Amount", Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
            differenceTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
            differenceTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
            differenceTable.addCell(makeCell("Blue Screen ", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
            differenceTable.addCell(makeCell("" + (blueScreenItemsSize), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
            differenceTable.addCell(makeCell(NumberUtils.formatNumber(blueScreenTotal, "###,###,##0.00"),
                    Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
            differenceTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
            differenceTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
            differenceTable.addCell(makeCell("Logiware ", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
            differenceTable.addCell(makeCell("" + (logiwareItemsSize), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
            differenceTable.addCell(makeCell(NumberUtils.formatNumber(logiwareTotal, "###,###,##0.00"),
                    Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
            differenceTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
            differenceTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
            differenceTable.addCell(makeCell("Difference ", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
            differenceTable.addCell(makeCell("" + (blueScreenItemsSize - logiwareItemsSize),
                    Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
            differenceTable.addCell(makeCell(NumberUtils.formatNumber(blueScreenTotal - logiwareTotal, "###,###,##0.00"),
                    Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
            differenceTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
            PdfPCell endCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.BOTTOM);
            endCell.setColspan(5);
            differenceTable.addCell(endCell);
            document.add(differenceTable);
        }
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();
        cb.saveState();
        String text = "Page " + writer.getPageNumber() + " of ";
        float textBase = document.bottom() - 20;
        float textSize = fontForFooter.getWidthPoint(text, 12);
        cb.beginText();
        cb.setFontAndSize(fontForFooter, 12);
        cb.setTextMatrix(document.right() / 2, textBase);
        cb.showText(text);
        cb.endText();
        cb.addTemplate(totalNoOfPages, document.right() / 2 + textSize, textBase);
        cb.restoreState();
    }

    @Override
    public void onCloseDocument(PdfWriter writer, Document document) {
        totalNoOfPages.beginText();
        totalNoOfPages.setFontAndSize(fontForFooter, 12);
        totalNoOfPages.setTextMatrix(0, 0);
        totalNoOfPages.showText(String.valueOf(writer.getPageNumber() - 1));
        totalNoOfPages.endText();
    }

    public void destroy() {
        document.close();
    }

    public String createReport(ControlReportForm controlReportForm, String contextPath) throws Exception {
        StringBuilder fileName = new StringBuilder(LoadLogisoftProperties.getProperty("reportLocation")).append("/Documents/Accounting/ControlReport/");
        fileName.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
        File file = new File(fileName.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        fileName.append(controlReportForm.getReportType()).append("ControlReport.pdf");
        this.init(controlReportForm, contextPath, fileName.toString());
        this.writeContents(controlReportForm);
        this.destroy();
        return fileName.toString();
    }
}
