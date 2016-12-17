/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.accounting.reports;

import com.gp.cong.common.DateUtils;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.accounting.dao.ArReportsDAO;
import com.logiware.accounting.form.ArReportsForm;
import com.logiware.bean.ReportBean;
import com.logiware.reports.BaseReportCreator;
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
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Rajesh
 */
public class NoCreditReportCreator extends BaseReportCreator {

    private static final Logger log = Logger.getLogger(NoCreditReportCreator.class);
    private ArReportsForm arReportsForm;

    public NoCreditReportCreator() {
    }

    public NoCreditReportCreator(ArReportsForm arReportsForm, String contextPath) {
	this.arReportsForm = arReportsForm;
	this.contextPath = contextPath;
    }

    private void init(String fileName) throws Exception {
	document = new Document(PageSize.A4);
	document.setMargins(5, 5, 5, 30);
	writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
	writer.setPdfVersion(PdfWriter.PDF_VERSION_1_7);
	writer.setUserunit(1f);
	writer.setPageEvent(new NoCreditReportCreator(arReportsForm, contextPath));
	document.open();
	writer.setOpenAction(PdfAction.gotoLocalPage(1, new PdfDestination(PdfDestination.XYZ, -1, -1, 1f), writer));
    }

    private void writeHeader() throws Exception {
	headerTable = new PdfPTable(1);
	headerTable.setWidthPercentage(100);
	String imagePath = LoadLogisoftProperties.getProperty("application.image.logo");
	Image image = Image.getInstance(contextPath + imagePath);
	image.scalePercent(75);
	PdfPCell logoCell = createImageCell(image);
	headerTable.addCell(logoCell);
	headerTable.addCell(createCell("No credit Report", headerBoldFont15, Element.ALIGN_CENTER, Rectangle.BOX, Color.LIGHT_GRAY));
	headerTable.addCell(createEmptyCell(Rectangle.NO_BORDER));
	headerTable.addCell(createEmptyCell(Rectangle.NO_BORDER));
    }

    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
	pageTemplate = writer.getDirectContent().createTemplate(20, 10);
	pageTemplate.setBoundingBox(new Rectangle(-20, -20, 20, 50));
	try {
	    helvFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
	} catch (Exception e) {
	    log.info("onOpenDocument failed on " + new Date(),e);
	}
    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
	try {
	    writeHeader();
	    document.add(headerTable);
	} catch (Exception e) {
	    log.info("onStartPage failed on " + new Date(),e);
	}
    }

    private void writeContent() throws Exception {
	List<ReportBean> noCreditList = new ArReportsDAO().getNoCreditInvoices(arReportsForm);
	contentTable = new PdfPTable(8);
	contentTable.setWidthPercentage(100);
	contentTable.setWidths(new float[]{12, 22, 10, 10, 15, 10, 10, 10});
	contentTable.addCell(createTextCell("Customer Number", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
	contentTable.addCell(createTextCell("Customer Name", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
	contentTable.addCell(createTextCell("Collector", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
	contentTable.addCell(createTextCell("Billing Terminal", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
	contentTable.addCell(createTextCell("Invoice/BL", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
	contentTable.addCell(createTextCell("Invoice Date", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
	contentTable.addCell(createTextCell("Invoice Amount", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
	contentTable.addCell(createTextCell("Balance", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
	for (ReportBean nocredit : noCreditList) {
	    contentTable.addCell(createTextCell(nocredit.getCustomerNumber(), blackNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createTextCell(nocredit.getCustomerName(), blackNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createTextCell(nocredit.getCollector(), blackNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createTextCell(nocredit.getBillingTerminal(), blackNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createTextCell(nocredit.getInvoiceOrBl(), blackNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createTextCell(nocredit.getInvoiceDate(), blackNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createTextCell(nocredit.getInvoiceAmount(), blackNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createTextCell(nocredit.getBalance(), blackNormalFont7, Rectangle.BOTTOM));
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
	StringBuilder fileNameBuilder = new StringBuilder();
	fileNameBuilder.append(LoadLogisoftProperties.getProperty("reportLocation")).append("/Documents/ArReports/");
        fileNameBuilder.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
	File file = new File(fileNameBuilder.toString());
	if (!file.exists()) {
	    file.mkdirs();
	}
	fileNameBuilder.append("Nocredit.pdf");
	init(fileNameBuilder.toString());
	writeContent();
	exit();
	return fileNameBuilder.toString();
    }
}
