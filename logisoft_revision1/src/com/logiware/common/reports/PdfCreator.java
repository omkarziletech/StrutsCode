package com.logiware.common.reports;

import com.gp.cong.common.DateUtils;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.reports.BaseReportCreator;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.ExceptionConverter;
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
import java.util.List;

import org.apache.log4j.Logger;
/**
 *
 * @author Lakshmi Narayanan
 */
public class PdfCreator extends BaseReportCreator {
private static final Logger log = Logger.getLogger(PdfCreator.class);
    private String reportName;

    public PdfCreator() {
    }

    public PdfCreator(String reportName, String contextPath) {
	this.reportName = reportName;
	this.contextPath = contextPath;
    }

    private void init(String fileName) throws DocumentException, FileNotFoundException {
	document = new Document(PageSize.A4.rotate());
	document.setMargins(15, 15, 15, 30);
	writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
	writer.setPdfVersion(PdfWriter.PDF_VERSION_1_7);
	writer.setUserunit(1f);
	writer.setPageEvent(new PdfCreator(reportName, contextPath));
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
	PdfPCell titleCell = createCell(reportName + " Report", headerBoldFont15, Element.ALIGN_CENTER, Rectangle.BOX, Color.LIGHT_GRAY);
	headerTable.addCell(titleCell);
	PdfPCell cell = createCell("Date: " + DateUtils.formatDate(new Date(), "MM/dd/yyyy"), headerBoldFont11, Element.ALIGN_RIGHT, Rectangle.NO_BORDER);
	headerTable.addCell(cell);
    }

    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
	pageTemplate = writer.getDirectContent().createTemplate(20, 10);
	pageTemplate.setBoundingBox(new Rectangle(-20, -20, 20, 50));
	try {
	    helvFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
	} catch (Exception e) {
	    log.info("onOpenDocument failed on " + new Date(),e);
            throw new ExceptionConverter(e);
	}
    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
	try {
	    writeHeader();
	    document.add(headerTable);
	} catch (Exception e) {
	    log.info("onStartPage failed on " + new Date(),e);
            throw new ExceptionConverter(e);
	}
    }

    private void writeContent(List<String[]> data, boolean isHeader) throws Exception {
	contentTable = new PdfPTable(15);
	contentTable.setWidthPercentage(100);
	if (isHeader) {
	    for (String header : data.get(0)) {
		contentTable.addCell(createHeaderCell(header, blackBoldFont6, Element.ALIGN_CENTER, Rectangle.BOX));
	    }
	    data.remove(0);
	}
	for (String[] row : data) {
	    for (String column : row) {
		contentTable.addCell(createTextCell(column, blackNormalFont6, Rectangle.BOTTOM));
	    }
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

    public String create(boolean isHeader, List<String[]> data) throws Exception {
	StringBuilder fileName = new StringBuilder();
	fileName.append(LoadLogisoftProperties.getProperty("reportLocation"));
	fileName.append("/Reports/");
	fileName.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd/"));
	File file = new File(fileName.toString());
	if (!file.exists()) {
	    file.mkdirs();
	}
	fileName.append(reportName).append("_").append(DateUtils.formatDate(new Date(), "yyyyMMdd_kkmmssSSS")).append(".pdf");
	init(fileName.toString());
	writeContent(data, isHeader);
	exit();
	return fileName.toString();
    }
}
