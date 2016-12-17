package com.logiware.reports;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.ExcelGenerator.ExcelSheetConstants;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.reports.ReportFormatMethods;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.Transaction;
import com.gp.cvst.logisoft.struts.form.CheckRegisterForm;
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
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

/**
 *
 * @author Lakshmi Narayanan
 */
public class CheckRegisterReportCreator extends ReportFormatMethods implements java.io.Serializable {

    private static final long serialVersionUID = 1671923197902043146L;
    private Document document = null;
    private PdfWriter pdfWriter = null;
    private String contextPath = null;
    private CheckRegisterForm checkRegisterForm = null;
    protected PdfTemplate totalNoOfPages = null;
    protected BaseFont fontForFooter = null;
    private static final Logger log = Logger.getLogger(CheckRegisterReportCreator.class);

    public CheckRegisterReportCreator() {
    }

    public CheckRegisterReportCreator(String contextPath, CheckRegisterForm checkRegisterForm) {
        this.checkRegisterForm = checkRegisterForm;
        this.contextPath = contextPath;
    }

    private void init(String contextPath, CheckRegisterForm checkRegisterForm, String fileName) throws Exception {
        this.contextPath = contextPath;
        document = new Document(PageSize.A4);
        document.setMargins(10, 10, 10, 50);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(fileName));
        pdfWriter.setPageEvent(new CheckRegisterReportCreator(contextPath, checkRegisterForm));
        document.open();
        pdfWriter.setOpenAction(PdfAction.gotoLocalPage(1, new PdfDestination(PdfDestination.XYZ, -1, -1, 0.75f), pdfWriter));
    }

    private PdfPTable createHeaderTable(CheckRegisterForm checkRegisterForm) throws Exception {
        PdfPTable headerTable = new PdfPTable(1);
        headerTable.setWidthPercentage(100);
        String imagePath = LoadLogisoftProperties.getProperty("application.image.logo");
        Image image = Image.getInstance(contextPath + imagePath);
        image.scalePercent(75);
        PdfPCell logoCell = new PdfPCell(image);
        logoCell.setBorder(Rectangle.NO_BORDER);
        logoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        logoCell.setVerticalAlignment(Element.ALIGN_TOP);
        headerTable.addCell(logoCell);
        PdfPCell emptyCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont, Rectangle.NO_BORDER);
        headerTable.addCell(emptyCell);
        headerTable.addCell(emptyCell);
        String title = "Payment Detail Report";
        PdfPCell titleCell = makeCell(title, Element.ALIGN_CENTER, Element.ALIGN_TOP, headingFont1, Rectangle.NO_BORDER, Color.LIGHT_GRAY);
        headerTable.addCell(titleCell);
        PdfPTable dateTable = makeTable(1);
        dateTable.setWidthPercentage(100);
        dateTable.addCell(makeCell("Date: " + DateUtils.formatDate(new Date(), "MM/dd/yyyy"), Element.ALIGN_RIGHT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
        PdfPCell dateCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont, Rectangle.NO_BORDER);
        dateCell.addElement(dateTable);
        headerTable.addCell(dateCell);
        PdfPTable payToTable = makeTable(1);
        payToTable.setWidthPercentage(100);
        TradingPartner tradingPartner = new TradingPartnerDAO().findById(checkRegisterForm.getCustNo());
        payToTable.addCell(makeCell("Pay To: " + tradingPartner.getAccountName() + "(" + tradingPartner.getAccountno() + ")", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
        PdfPCell payToCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont, Rectangle.NO_BORDER);
        payToCell.addElement(payToTable);
        headerTable.addCell(payToCell);
        PdfPTable payMethodTable = makeTable(1);
        payMethodTable.setWidthPercentage(100);
        payMethodTable.addCell(makeCell("Payment Method: " + checkRegisterForm.getPaymentMethod(), Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
        PdfPCell payMethodCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont, Rectangle.NO_BORDER);
        payMethodCell.addElement(payMethodTable);
        headerTable.addCell(payMethodCell);
        PdfPTable checkNoTable = makeTable(1);
        checkNoTable.setWidthPercentage(100);
        checkNoTable.addCell(makeCell("Check #: " + checkRegisterForm.getCheckNo(), Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
        PdfPCell checkNoCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont, Rectangle.NO_BORDER);
        checkNoCell.addElement(checkNoTable);
        headerTable.addCell(checkNoCell);
        PdfPTable checkDateTable = makeTable(1);
        checkDateTable.setWidthPercentage(100);
        checkDateTable.addCell(makeCell("Check Date: " + checkRegisterForm.getPaymentDate(), Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
        PdfPCell checkDateCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont, Rectangle.NO_BORDER);
        checkDateCell.addElement(checkDateTable);
        headerTable.addCell(checkDateCell);
        headerTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont, Rectangle.NO_BORDER));
        PdfPTable listHeadingTable = new PdfPTable(2);
        listHeadingTable.setWidthPercentage(60);
        listHeadingTable.setWidths(new float[]{70, 30});
        listHeadingTable.addCell(makeCell(ExcelSheetConstants.INVOICE_NO, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.BOTTOM));
        listHeadingTable.addCell(makeCell(ExcelSheetConstants.AMOUNT, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.BOTTOM));
        listHeadingTable.setHorizontalAlignment(Rectangle.ALIGN_LEFT);
        PdfPCell listHeadingCell = makeCell("", Element.ALIGN_RIGHT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER);
        listHeadingCell.addElement(listHeadingTable);
        headerTable.addCell(listHeadingCell);
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
            document.add(this.createHeaderTable(this.checkRegisterForm));
        } catch (Exception e) {
            log.info("onStartPage failed on " + new Date(),e);
        }
    }

    private void writeContents(List<Transaction> checkRegisters) throws Exception {
        if (CommonUtils.isNotEmpty(checkRegisters)) {
            PdfPTable listContentsTable = new PdfPTable(2);
            listContentsTable.setWidthPercentage(60);
            listContentsTable.setHorizontalAlignment(Rectangle.ALIGN_LEFT);
            listContentsTable.setWidths(new float[]{70, 30});
            double total = 0d;
            for (Transaction transaction : checkRegisters) {
                listContentsTable.addCell(makeCell(transaction.getInvoiceNumber(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER));
                listContentsTable.addCell(makeCell(NumberUtils.formatNumber(transaction.getTransactionAmt(), "###,###,##0.00"), Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER));
                total += transaction.getTransactionAmt();
            }
            listContentsTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.TOP));
            listContentsTable.addCell(makeCell("Total     "+NumberUtils.formatNumber(total, "###,###,##0.00"), Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.TOP));
            document.add(listContentsTable);
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

    public void createReport(String fileName, CheckRegisterForm checkRegisterForm, List<Transaction> checkRegistgers, String contextPath) throws Exception {
        this.init(contextPath, checkRegisterForm, fileName);
        this.writeContents(checkRegistgers);
        this.destroy();
    }
}
