package com.logiware.reports;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.reports.ReportFormatMethods;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.bean.AccountingBean;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
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
import java.util.List;

import org.apache.log4j.Logger;

public class SubledgerHistoryReportCreator extends ReportFormatMethods {

    private Logger log = Logger.getLogger(SubledgerHistoryReportCreator.class);
    private Document document = null;
    private PdfWriter pdfWriter = null;
    private PdfTemplate totalNoOfPages;
    private BaseFont helveticaBold;
    private String contextPath;
    private String journalEntryId;
    private String subledgerType;
    private String period;
    private Font headingFont = new Font(1, 8.0F, 1, Color.BLACK);
    private Font blackFont = new Font(1, 7.0F, 0, Color.BLACK);

    private void init(String fileName, String contextPath, String journalEntryId, String subledgerType, String period) {
        try {
            this.document = new Document(PageSize.A4.rotate());
            this.document.setMargins(10.0F, 10.0F, 20.0F, 30.0F);
            this.pdfWriter = PdfWriter.getInstance(this.document, new FileOutputStream(fileName));
            this.pdfWriter.setPageEvent(new SubledgerHistoryReportCreator(contextPath, journalEntryId, subledgerType, period));
            this.document.open();
            PdfAction action = PdfAction.gotoLocalPage(1, new PdfDestination(0, -1.0F, -1.0F, 0.75F), this.pdfWriter);
            this.pdfWriter.setOpenAction(action);
        } catch (Exception e) {
            this.log.info(e);
        }
    }

    public SubledgerHistoryReportCreator() {
    }

    public SubledgerHistoryReportCreator(String contextPath, String journalEntryId, String subledgerType, String period) {
        this.contextPath = contextPath;
        this.journalEntryId = journalEntryId;
        this.subledgerType = subledgerType;
        this.period = period;
    }

    public void onOpenDocument(PdfWriter pdfWriter, Document document) {
        try {
            this.totalNoOfPages = pdfWriter.getDirectContent().createTemplate(20.0F, 10.0F);
            this.totalNoOfPages.setBoundingBox(new Rectangle(-10.0F, -10.0F, 20.0F, 50.0F));
            this.helveticaBold = BaseFont.createFont("Helvetica-Bold", "Cp1252", false);
        } catch (Exception e) {
            this.log.info(e);
        }
    }

    private PdfPTable createHeader() throws Exception {
        PdfPTable headerTable = new PdfPTable(1);
        headerTable.setWidthPercentage(100.0F);
        String imagePath = LoadLogisoftProperties.getProperty("application.image.logo");
        Image image = Image.getInstance(this.contextPath + imagePath);
        image.scalePercent(75.0F);
        PdfPCell logoCell = new PdfPCell(image);
        logoCell.setBorder(0);
        logoCell.setHorizontalAlignment(1);
        logoCell.setVerticalAlignment(4);
        headerTable.addCell(logoCell);
        String title = "Subledger History Report";
        PdfPCell titleCell = makeCell(title, 1, 4, this.headingFont1, 0, Color.LIGHT_GRAY);
        headerTable.addCell(titleCell);
        PdfPTable jeDetailsTable = new PdfPTable(4);
        jeDetailsTable.setWidthPercentage(100.0F);
        jeDetailsTable.setTotalWidth(new float[]{15.0F, 25.0F, 15.0F, 45.0F});
        jeDetailsTable.addCell(makeCell("Journal Entry:", 0, 4, this.blackBoldFont2, 0));
        jeDetailsTable.addCell(makeCell(this.journalEntryId, 0, 4, this.blackFont, 0));
        jeDetailsTable.addCell(makeCell("Subledger:", 0, 4, this.blackBoldFont2, 0));
        jeDetailsTable.addCell(makeCell(this.subledgerType, 0, 4, this.blackFont, 0));
        jeDetailsTable.addCell(makeCell("Period:", 0, 4, this.blackBoldFont2, 0));
        jeDetailsTable.addCell(makeCell(this.period, 0, 4, this.blackFont, 0));
        jeDetailsTable.addCell(makeCell("Created Date:", 0, 4, this.blackBoldFont2, 0));
        String date = DateUtils.formatDate(new Date(), "MM/dd/yyyy");
        jeDetailsTable.addCell(makeCell(date, 0, 4, this.blackFont, 0));
        PdfPCell jeDetailsCell = makeCell("", 0, 5, this.headingFontForAR, 2);
        jeDetailsCell.addElement(jeDetailsTable);
        headerTable.addCell(jeDetailsCell);
        PdfPCell columnHeaderCell = makeCell("", 0, 5, this.headingFontForAR, 2);
        if (CommonUtils.isEqualIgnoreCase(this.subledgerType, "ACC")) {
            columnHeaderCell.addElement(createACCHeader());
        } else if (CommonUtils.isEqualIgnoreCase(this.subledgerType, "PJ")) {
            columnHeaderCell.addElement(createPJHeader());
        } else if (CommonUtils.isEqualIgnoreCase(this.subledgerType, "CD")) {
            columnHeaderCell.addElement(createCDHeader());
        } else if (CommonUtils.isEqualIgnoreCase(this.subledgerType, "NET SETT")) {
            columnHeaderCell.addElement(createNSHeader());
        } else if (CommonUtils.isEqualIgnoreCase(this.subledgerType, "RCT")) {
            columnHeaderCell.addElement(createRCTHeader());
        } else {
            columnHeaderCell.addElement(createARHeader());
        }
        headerTable.addCell(columnHeaderCell);
        return headerTable;
    }

    private PdfPTable createACCHeader() throws DocumentException {
        PdfPTable columnHeaderTable = new PdfPTable(13);
        columnHeaderTable.setWidthPercentage(100.0F);
        columnHeaderTable.setWidths(new float[]{8.0F, 18.0F, 5.5F, 8.0F, 8.0F, 6.0F, 5.0F, 6.5F, 5.5F, 5.0F, 9.0F, 9.0F, 6.5F});
        columnHeaderTable.addCell(makeCell("Vendor Number", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Vendor Name", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("GL Account", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("BL Number", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Invoice Number", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Voyage", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Charge Code", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Transaction Date", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Reporting Date", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Posted Date", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Debit", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Credit", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Line Item Number", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        return columnHeaderTable;
    }

    private PdfPTable createPJHeader() throws DocumentException {
        PdfPTable columnHeaderTable = new PdfPTable(13);
        columnHeaderTable.setWidthPercentage(100.0F);
        columnHeaderTable.setWidths(new float[]{8.0F, 18.0F, 5.5F, 8.0F, 8.0F, 6.0F, 5.0F, 6.5F, 5.5F, 5.0F, 9.0F, 9.0F, 6.5F});
        columnHeaderTable.addCell(makeCell("Vendor Number", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Vendor Name", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("GL Account", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("BL Number", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Invoice Number", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Voyage", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Charge Code", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Transaction Date", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Reporting Date", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Posted Date", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Debit", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Credit", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Line Item Number", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        return columnHeaderTable;
    }

    private PdfPTable createCDHeader() throws DocumentException {
        PdfPTable columnHeaderTable = new PdfPTable(13);
        columnHeaderTable.setWidthPercentage(100.0F);
        columnHeaderTable.setWidths(new float[]{8.0F, 18.0F, 5.5F, 8.0F, 8.0F, 6.0F, 5.0F, 6.5F, 5.0F, 9.0F, 9.0F, 6.0F, 6.0F});
        columnHeaderTable.addCell(makeCell("Vendor Number", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Vendor Name", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("GL Account", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("BL Number", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Invoice Number", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Voyage", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Charge Code", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Transaction Date", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Posted Date", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Debit", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Credit", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("AP Batch Number", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Line Item Number", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        return columnHeaderTable;
    }

    private PdfPTable createNSHeader() throws DocumentException {
        PdfPTable columnHeaderTable = new PdfPTable(14);
        columnHeaderTable.setWidthPercentage(100.0F);
        columnHeaderTable.setWidths(new float[]{6.5F, 14.0F, 5.0F, 8.0F, 8.0F, 6.0F, 5.0F, 6.5F, 5.0F, 9.0F, 9.0F, 5.0F, 6.5F, 6.5F});
        columnHeaderTable.addCell(makeCell("Vendor Number", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Vendor Name", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("GL Account", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("BL Number", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Invoice Number", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Voyage", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Charge Code", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Transaction Date", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Posted Date", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Debit", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Credit", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("AR Batch Number", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Transaction Type", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Line Item Number", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        return columnHeaderTable;
    }

    private PdfPTable createRCTHeader() throws DocumentException {
        PdfPTable columnHeaderTable = new PdfPTable(14);
        columnHeaderTable.setWidthPercentage(100.0F);
        columnHeaderTable.setWidths(new float[]{6.5F, 14.0F, 5.0F, 8.0F, 8.0F, 6.0F, 5.0F, 6.5F, 5.0F, 9.0F, 9.0F, 5.0F, 6.5F, 6.5F});
        columnHeaderTable.addCell(makeCell("Vendor Number", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Vendor Name", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("GL Account", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("BL Number", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Invoice Number", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Voyage", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Charge Code", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Transaction Date", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Posted Date", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Debit", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Credit", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("AR Batch Number", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Transaction Type", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Line Item Number", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        return columnHeaderTable;
    }

    private PdfPTable createARHeader() throws DocumentException {
        PdfPTable columnHeaderTable = new PdfPTable(12);
        columnHeaderTable.setWidthPercentage(100.0F);
        columnHeaderTable.setWidths(new float[]{8.0F, 19.0F, 5.5F, 8.0F, 8.0F, 8.0F, 5.0F, 7.0F, 5.0F, 10.0F, 10.0F, 6.5F});
        columnHeaderTable.addCell(makeCell("Vendor Number", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Vendor Name", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("GL Account", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("BL Number", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Invoice Number", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Voyage", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Charge Code", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Transaction Date", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Posted Date", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Debit", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Credit", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        columnHeaderTable.addCell(makeCell("Line Item Number", 1, 5, this.headingFont, 15, Color.LIGHT_GRAY));
        return columnHeaderTable;
    }

    public void onStartPage(PdfWriter writer, Document document) {
        try {
            document.add(createHeader());
        } catch (Exception e) {
            this.log.info(e);
        }
    }

    private void writeContents(List<AccountingBean> transactions, String subledgerType) throws DocumentException, Exception {
        if (CommonUtils.isEqualIgnoreCase(subledgerType, "ACC")) {
            writeACCContents(transactions);
        } else if (CommonUtils.isEqualIgnoreCase(subledgerType, "PJ")) {
            writePJContents(transactions);
        } else if (CommonUtils.isEqualIgnoreCase(subledgerType, "CD")) {
            writeCDContents(transactions);
        } else if (CommonUtils.isEqualIgnoreCase(subledgerType, "NET SETT")) {
            writeNSContents(transactions);
        } else if (CommonUtils.isEqualIgnoreCase(subledgerType, "RCT")) {
            writeRCTContents(transactions);
        } else {
            writeARContents(transactions);
        }
    }

    private void writeACCContents(List<AccountingBean> transactions) throws DocumentException, Exception {
        PdfPTable contentsTable = new PdfPTable(13);
        contentsTable.setWidthPercentage(100.0F);
        contentsTable.setWidths(new float[]{8.0F, 18.0F, 5.5F, 8.0F, 8.0F, 6.0F, 5.0F, 6.5F, 5.5F, 5.0F, 9.0F, 9.0F, 6.5F});
        for (AccountingBean transaction : transactions) {
            contentsTable.addCell(makeCell(transaction.getVendorNumber(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getVendorName(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getGlAccount(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getBillOfLadding(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getInvoiceNumber(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getVoyage(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getChargeCode(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getFormattedDate(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getFormattedReportingDate(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getFormattedPostedDate(), 0, 5, this.blackFont, 15));
            String debit = NumberUtils.formatNumber(transaction.getDebitAmount(), "#,###,###,##0.00");
            contentsTable.addCell(makeCell(debit, 2, 5, this.blackFont, 15));
            String credit = NumberUtils.formatNumber(transaction.getCreditAmount(), "#,###,###,##0.00");
            contentsTable.addCell(makeCell(credit, 2, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getLineItemId(), 0, 5, this.blackFont, 15));
        }
        this.document.add(contentsTable);
    }

    private void writePJContents(List<AccountingBean> transactions) throws DocumentException, Exception {
        PdfPTable contentsTable = new PdfPTable(13);
        contentsTable.setWidthPercentage(100.0F);
        contentsTable.setWidths(new float[]{8.0F, 18.0F, 5.5F, 8.0F, 8.0F, 6.0F, 5.0F, 6.5F, 5.5F, 5.0F, 9.0F, 9.0F, 6.5F});
        for (AccountingBean transaction : transactions) {
            contentsTable.addCell(makeCell(transaction.getVendorNumber(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getVendorName(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getGlAccount(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getBillOfLadding(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getInvoiceNumber(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getVoyage(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getChargeCode(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getFormattedDate(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getFormattedReportingDate(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getFormattedPostedDate(), 0, 5, this.blackFont, 15));
            String debit = NumberUtils.formatNumber(transaction.getDebitAmount(), "#,###,###,##0.00");
            contentsTable.addCell(makeCell(debit, 2, 5, this.blackFont, 15));
            String credit = NumberUtils.formatNumber(transaction.getCreditAmount(), "#,###,###,##0.00");
            contentsTable.addCell(makeCell(credit, 2, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getLineItemId(), 0, 5, this.blackFont, 15));
        }
        this.document.add(contentsTable);
    }

    private void writeCDContents(List<AccountingBean> transactions) throws DocumentException, Exception {
        PdfPTable contentsTable = new PdfPTable(13);
        contentsTable.setWidthPercentage(100.0F);
        contentsTable.setWidths(new float[]{8.0F, 18.0F, 5.5F, 8.0F, 8.0F, 6.0F, 5.0F, 6.5F, 5.0F, 9.0F, 9.0F, 6.0F, 6.0F});
        for (AccountingBean transaction : transactions) {
            contentsTable.addCell(makeCell(transaction.getVendorNumber(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getVendorName(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getGlAccount(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getBillOfLadding(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getInvoiceNumber(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getVoyage(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getChargeCode(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getFormattedDate(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getFormattedPostedDate(), 0, 5, this.blackFont, 15));
            String debit = NumberUtils.formatNumber(transaction.getDebitAmount(), "#,###,###,##0.00");
            contentsTable.addCell(makeCell(debit, 2, 5, this.blackFont, 15));
            String credit = NumberUtils.formatNumber(transaction.getCreditAmount(), "#,###,###,##0.00");
            contentsTable.addCell(makeCell(credit, 2, 5, this.blackFont, 15));
            String batchId = null != transaction.getApBatchId() ? transaction.getApBatchId().toString() : "";
            contentsTable.addCell(makeCell(batchId, 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getLineItemId(), 0, 5, this.blackFont, 15));
        }
        this.document.add(contentsTable);
    }

    private void writeNSContents(List<AccountingBean> transactions) throws DocumentException, Exception {
        PdfPTable contentsTable = new PdfPTable(14);
        contentsTable.setWidthPercentage(100.0F);
        contentsTable.setWidths(new float[]{6.5F, 14.0F, 5.0F, 8.0F, 8.0F, 6.0F, 5.0F, 6.5F, 5.0F, 9.0F, 9.0F, 5.0F, 6.5F, 6.5F});
        for (AccountingBean transaction : transactions) {
            contentsTable.addCell(makeCell(transaction.getVendorNumber(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getVendorName(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getGlAccount(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getBillOfLadding(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getInvoiceNumber(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getVoyage(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getChargeCode(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getFormattedDate(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getFormattedPostedDate(), 0, 5, this.blackFont, 15));
            String debit = NumberUtils.formatNumber(transaction.getDebitAmount(), "#,###,###,##0.00");
            contentsTable.addCell(makeCell(debit, 2, 5, this.blackFont, 15));
            String credit = NumberUtils.formatNumber(transaction.getCreditAmount(), "#,###,###,##0.00");
            contentsTable.addCell(makeCell(credit, 2, 5, this.blackFont, 15));
            String batchId = null != transaction.getApBatchId() ? transaction.getApBatchId().toString() : "";
            contentsTable.addCell(makeCell(batchId, 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getTransactionType(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getLineItemId(), 0, 5, this.blackFont, 15));
        }
        this.document.add(contentsTable);
    }

    private void writeRCTContents(List<AccountingBean> transactions) throws DocumentException, Exception {
        PdfPTable contentsTable = new PdfPTable(14);
        contentsTable.setWidthPercentage(100.0F);
        contentsTable.setWidths(new float[]{6.5F, 14.0F, 5.0F, 8.0F, 8.0F, 6.0F, 5.0F, 6.5F, 5.0F, 9.0F, 9.0F, 5.0F, 6.5F, 6.5F});
        for (AccountingBean transaction : transactions) {
            contentsTable.addCell(makeCell(transaction.getVendorNumber(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getVendorName(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getGlAccount(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getBillOfLadding(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getInvoiceNumber(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getVoyage(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getChargeCode(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getFormattedDate(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getFormattedPostedDate(), 0, 5, this.blackFont, 15));
            String debit = NumberUtils.formatNumber(transaction.getDebitAmount(), "#,###,###,##0.00");
            contentsTable.addCell(makeCell(debit, 2, 5, this.blackFont, 15));
            String credit = NumberUtils.formatNumber(transaction.getCreditAmount(), "#,###,###,##0.00");
            contentsTable.addCell(makeCell(credit, 2, 5, this.blackFont, 15));
            String batchId = null != transaction.getApBatchId() ? transaction.getApBatchId().toString() : "";
            contentsTable.addCell(makeCell(batchId, 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getTransactionType(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getLineItemId(), 0, 5, this.blackFont, 15));
        }
        this.document.add(contentsTable);
    }

    private void writeARContents(List<AccountingBean> transactions) throws DocumentException, Exception {
        PdfPTable contentsTable = new PdfPTable(12);
        contentsTable.setWidthPercentage(100.0F);
        contentsTable.setWidths(new float[]{8.0F, 19.0F, 5.5F, 8.0F, 8.0F, 8.0F, 5.0F, 7.0F, 5.0F, 10.0F, 10.0F, 6.5F});
        for (AccountingBean transaction : transactions) {
            contentsTable.addCell(makeCell(transaction.getVendorNumber(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getVendorName(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getGlAccount(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getBillOfLadding(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getInvoiceNumber(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getVoyage(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getChargeCode(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getFormattedDate(), 0, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getFormattedPostedDate(), 0, 5, this.blackFont, 15));
            String debit = NumberUtils.formatNumber(transaction.getDebitAmount(), "#,###,###,##0.00");
            contentsTable.addCell(makeCell(debit, 2, 5, this.blackFont, 15));
            String credit = NumberUtils.formatNumber(transaction.getCreditAmount(), "#,###,###,##0.00");
            contentsTable.addCell(makeCell(credit, 2, 5, this.blackFont, 15));
            contentsTable.addCell(makeCell(transaction.getLineItemId(), 0, 5, this.blackFont, 15));
        }
        this.document.add(contentsTable);
    }

    public void onEndPage(PdfWriter writer, Document document) {
        try {
            PdfContentByte cb = writer.getDirectContent();
            cb.saveState();
            String text = "Page " + writer.getPageNumber() + " of ";
            float textBase = document.bottomMargin() - 20.0F;
            float textSize = this.helveticaBold.getWidthPoint(text, 12.0F);
            cb.beginText();
            cb.setFontAndSize(this.helveticaBold, 12.0F);
            cb.setTextMatrix(document.right() / 2.0F, textBase);
            cb.showText(text);
            cb.endText();
            cb.addTemplate(this.totalNoOfPages, document.right() / 2.0F + textSize, textBase);
            cb.restoreState();
        } catch (Exception e) {
            this.log.info(e);
        }
    }

    public void onCloseDocument(PdfWriter writer, Document document) {
        this.totalNoOfPages.beginText();
        this.totalNoOfPages.setFontAndSize(this.helveticaBold, 12.0F);
        this.totalNoOfPages.setTextMatrix(0.0F, 0.0F);
        this.totalNoOfPages.showText(String.valueOf(writer.getPageNumber() - 1));
        this.totalNoOfPages.endText();
    }

    public void doExit() {
        this.document.close();
    }

    public String createReport(String contextPath, String journalEntryId, String subledgerType, String period, List<AccountingBean> transactions) {
        try {
            StringBuilder fileName = new StringBuilder(LoadLogisoftProperties.getProperty("reportLocation"));
            fileName.append("/Documents/JournalEntry/");
            fileName.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
            File file = new File(fileName.toString());
            if (!file.exists()) {
                file.mkdirs();
            }
            fileName.append("Subledger_History_").append(journalEntryId).append(".pdf");
            init(fileName.toString(), contextPath, journalEntryId, subledgerType, period);
            writeContents(transactions, subledgerType);
            doExit();
            return fileName.toString();  
        } catch (Exception e) {
            this.log.info(e);
        }
        return null;
    }
}