package com.logiware.accounting.reports;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
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

import org.apache.log4j.Logger;
/**
 *
 * @author Lakshmi Naryanan
 */
public class DsoReportCreator extends BaseReportCreator {
private static final Logger log = Logger.getLogger(DsoReportCreator.class);
    private ArReportsForm arReportsForm;

    public DsoReportCreator() {
    }

    public DsoReportCreator(ArReportsForm arReportsForm, String contextPath) {
	this.arReportsForm = arReportsForm;
	this.contextPath = contextPath;
    }

    private void init(String fileName) throws Exception {
	document = new Document(PageSize.A4);
	document.setMargins(5, 5, 5, 30);
	writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
	writer.setPdfVersion(PdfWriter.PDF_VERSION_1_7);
	writer.setUserunit(1f);
	writer.setPageEvent(new DsoReportCreator(arReportsForm, contextPath));
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
	headerTable.addCell(createCell("DSO Report", headerBoldFont15, Element.ALIGN_CENTER, Rectangle.BOX, Color.LIGHT_GRAY));
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
	ReportBean dso = new ArReportsDAO().getDsoModel(arReportsForm);
	contentTable = new PdfPTable(2);
	contentTable.setWidthPercentage(100);
	contentTable.setWidths(new float[]{50, 50});
	contentTable.addCell(createTextCell("DSO For : ", blackBoldFont10, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
	contentTable.addCell(createTextCell(arReportsForm.getDsoFilter(), blackNormalFont10, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
	if (CommonUtils.isEqual(arReportsForm.getDsoFilter(), "By Collector")) {
	    String collector = new UserDAO().getLoginName(Integer.parseInt(arReportsForm.getCollector()));
	    contentTable.addCell(createTextCell("Collector : ", blackBoldFont10, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
	    contentTable.addCell(createTextCell(collector, blackNormalFont10, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
	} else if (CommonUtils.isEqual(arReportsForm.getDsoFilter(), "By Customer")) {
	    contentTable.addCell(createTextCell("Customer Name : ", blackBoldFont10, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
	    contentTable.addCell(createTextCell(arReportsForm.getCustomerName(), blackNormalFont10, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
	    contentTable.addCell(createTextCell("Customer Number : ", blackBoldFont10, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
	    contentTable.addCell(createTextCell(arReportsForm.getCustomerNumber(), blackNormalFont10, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
	}
	contentTable.addCell(createTextCell("Total Amount Open Receivables : ", blackBoldFont10, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
	contentTable.addCell(createTextCell(dso.getOpenReceivables(), blackNormalFont10, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
	contentTable.addCell(createTextCell("Total Amount Sales : ", blackBoldFont10, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
	contentTable.addCell(createTextCell(dso.getSales(), blackNormalFont10, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
	contentTable.addCell(createTextCell("Selected Period : ", blackBoldFont10, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
	contentTable.addCell(createTextCell(arReportsForm.getDsoPeriod(), blackNormalFont10, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
	contentTable.addCell(createTextCell("DSO Ratio : ", blackBoldFont10, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
	contentTable.addCell(createTextCell(dso.getDsoRatio(), blackNormalFont10, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
	PdfPCell lineCell = createEmptyCell(Rectangle.BOTTOM);
	lineCell.setColspan(2);
	contentTable.addCell(lineCell);
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
	fileNameBuilder.append("DSO.pdf");
	init(fileNameBuilder.toString());
	writeContent();
	exit();
	return fileNameBuilder.toString();
    }
}
