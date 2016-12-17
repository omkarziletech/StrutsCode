package com.logiware.accounting.reports;

import com.gp.cong.common.DateUtils;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.accounting.form.CheckRegisterForm;
import com.logiware.accounting.model.InvoiceModel;
import com.logiware.reports.BaseReportCreator;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
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
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;
import org.apache.log4j.Logger;

/**
 *
 * @author Lakshmi Narayanan
 */
public class CheckRegisterReportCreator extends BaseReportCreator {

    private static final Logger log = Logger.getLogger(CheckRegisterReportCreator.class);
    private CheckRegisterForm form;

    public CheckRegisterReportCreator() {
    }

    public CheckRegisterReportCreator(CheckRegisterForm form, String contextPath) {
        this.form = form;
        this.contextPath = contextPath;
    }

    private void init(String fileName) throws DocumentException, FileNotFoundException {
        document = new Document(PageSize.A4.rotate());
        document.setMargins(15, 15, 15, 50);
        writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
        writer.setPdfVersion(PdfWriter.PDF_VERSION_1_7);
        writer.setUserunit(1f);
        writer.setPageEvent(new CheckRegisterReportCreator(form, contextPath));
        document.open();
        writer.setOpenAction(PdfAction.gotoLocalPage(1, new PdfDestination(PdfDestination.XYZ, -1, -1, 0.75f), writer));
    }

    private void writeHeader() throws Exception {
        headerTable = new PdfPTable(1);
        headerTable.setWidthPercentage(100);
        String imagePath = LoadLogisoftProperties.getProperty("application.image.logo");
        Image image = Image.getInstance(contextPath + imagePath);
        image.scalePercent(75);
        PdfPCell logoCell = createImageCell(image);
        headerTable.addCell(logoCell);
        headerTable.addCell(createCell("Payment Detail Report", headerBoldFont15, Element.ALIGN_CENTER, Rectangle.BOX, Color.LIGHT_GRAY));
        headerTable.addCell(createCell("Date: " + DateUtils.formatDate(new Date(), "MM/dd/yyyy"), headerBoldFont11, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        headerTable.addCell(createCell("Pay To: " + form.getVendorName() + "(" + form.getVendorNumber() + ")", headerBoldFont11, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        headerTable.addCell(createCell("Payment Method: " + form.getPaymentMethod(), headerBoldFont11, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        headerTable.addCell(createCell("Payment Date: " + form.getPaymentDate(), headerBoldFont11, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        headerTable.addCell(createCell("Check #: " + form.getCheckNumber(), headerBoldFont11, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
    }

    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        pageTemplate = writer.getDirectContent().createTemplate(20, 10);
        pageTemplate.setBoundingBox(new Rectangle(-20, -20, 20, 50));
        try {
            helvFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
        } catch (Exception e) {
            log.info("onOpenDocument failed on " + new Date(), e);
        }
    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        try {
            writeHeader();
            document.add(headerTable);
        } catch (Exception e) {
            log.info("onStartPage failed on " + new Date(), e);
        }
    }

    private void writeContent() throws Exception {
        contentTable = new PdfPTable(2);
        contentTable.setWidthPercentage(40);
        contentTable.setWidths(new float[]{60, 40});
        contentTable.setHorizontalAlignment(Element.ALIGN_LEFT);
        contentTable.addCell(createHeaderCell("Invoice/BL#", blackBoldFont10, Element.ALIGN_CENTER, Rectangle.BOX));
        contentTable.addCell(createHeaderCell("Amount", blackBoldFont10, Element.ALIGN_CENTER, Rectangle.BOX));
        contentTable.setHeaderRows(1);
        for (InvoiceModel invoice : form.getInvoiceList()) {
            contentTable.addCell(createTextCell(invoice.getInvoiceOrBl(), blackNormalFont10, Rectangle.BOX));
            contentTable.addCell(createAmountCell(invoice.getInvoiceAmount(), blackNormalFont10, redNormalFont6, Rectangle.BOX));
        }
        document.add(contentTable);
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();
        cb.saveState();
        String text = "Page " + writer.getPageNumber() + " of ";
        float textBase = document.bottom() - 20;
        float textSize = helvFont.getWidthPoint(text, 12);
        cb.beginText();
        cb.setFontAndSize(helvFont, 12);
        cb.setTextMatrix((document.right() / 2) - (textSize / 2), textBase);
        cb.showText(text);
        cb.endText();
        cb.addTemplate(pageTemplate, (document.right() / 2) + (textSize / 2), textBase);
        cb.restoreState();
    }

    @Override
    public void onCloseDocument(PdfWriter writer, Document document) {
        pageTemplate.beginText();
        pageTemplate.setFontAndSize(helvFont, 12);
        pageTemplate.setTextMatrix(0, 0);
        pageTemplate.showText(String.valueOf(writer.getPageNumber() - 1));
        pageTemplate.endText();
    }

    private void exit() {
        document.close();
    }

    public String create() throws Exception {
        StringBuilder fileName = new StringBuilder();
        fileName.append(LoadLogisoftProperties.getProperty("reportLocation")).append("/Documents/AccountPayable/");
        fileName.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
        File file = new File(fileName.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        fileName.append("CheckRegister.pdf");
        init(fileName.toString());
        writeContent();
        exit();
        return fileName.toString();
    }

}
