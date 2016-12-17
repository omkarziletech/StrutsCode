/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.reports;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.reports.ReportFormatMethods;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.struts.form.DsoReportForm;
import com.logiware.bean.ReportBean;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfDestination;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

/**
 *
 * @author logiware
 */
public class ArReportsCreator extends ReportFormatMethods {

    private static final Logger log = Logger.getLogger(ArReportsCreator.class);
    Document document = null;
    PdfWriter pdfWriter = null;
    NumberFormat amountFormat = new DecimalFormat("###,###,##0.00");

    private void initialize(String fileName) throws FileNotFoundException, DocumentException {
	document = new Document(PageSize.A4.rotate());
	document.setMargins(10, 10, 20, 20);
	pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(fileName));
	document.open();
	PdfAction action = PdfAction.gotoLocalPage(1, new PdfDestination(PdfDestination.XYZ, -1, -1, 0.75f), pdfWriter);
	pdfWriter.setOpenAction(action);
    }

    private void createBody(DsoReportForm dsoReportForm, List arDsoList, String contextPath) throws DocumentException, IOException, Exception {
	// table for company details and logo
	String imagePath = LoadLogisoftProperties.getProperty("application.image.logo");
	Image image = Image.getInstance(contextPath + imagePath);
	image.scalePercent(75);
	PdfPCell logoCell = new PdfPCell(image);
	logoCell.setBorder(Rectangle.NO_BORDER);
	logoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	logoCell.setVerticalAlignment(Element.ALIGN_TOP);
	PdfPTable logoTable = new PdfPTable(1);
	logoTable.setWidthPercentage(100);
	logoTable.addCell(logoCell);
	document.add(logoTable);
	if (CommonUtils.isEqualIgnoreCase(dsoReportForm.getReportType(), CommonConstants.AR_DSO_REPORT)) {
	    this.createDsoReport(dsoReportForm, arDsoList);
	}
    }

    private void createDsoReport(DsoReportForm dsoReportForm, List<ReportBean> arDsoList) throws DocumentException {
	PdfPTable titleTable = new PdfPTable(1);
	titleTable.setWidthPercentage(100);
	titleTable.setWidths(new float[]{100});
	titleTable.getDefaultCell().setPadding(0);
	titleTable.getDefaultCell().setBorderWidth(0.5f);
	titleTable.getDefaultCell().setBorderWidthLeft(0.0f);
	titleTable.getDefaultCell().setBorderWidthRight(0.0f);
	PdfPCell titleCell = new PdfPCell(new Phrase("DSO", headingFont1));
	titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	titleCell.setVerticalAlignment(Element.ALIGN_TOP);
	titleCell.setPaddingTop(-2);
	titleCell.setPaddingBottom(2);
	titleCell.setBorder(0);
	titleCell.setBackgroundColor(Color.LIGHT_GRAY);
	titleTable.addCell(titleCell);
	titleTable.addCell(makeCell("", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFont, Rectangle.NO_BORDER));
	titleTable.addCell(makeCell("", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFont, Rectangle.NO_BORDER));
	document.add(titleTable);

	PdfPTable header = new PdfPTable(1);
	header.setWidthPercentage(100);
	header.setWidths(new float[]{100});
	header.addCell(makeCellleftNoBorder("DSO For : " + dsoReportForm.getSearchDsoBy()));
	header.addCell(makeCell("", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFont, Rectangle.NO_BORDER));
	header.addCell(makeCell("", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFont, Rectangle.NO_BORDER));
	document.add(header);

	PdfPTable table = new PdfPTable(4);
	table.setWidthPercentage(100);
	table.setWidths(new float[]{25, 25, 25, 25});
	PdfPCell lineCell = makeCell("", Element.ALIGN_CENTER, headingFont, Rectangle.BOTTOM);
	lineCell.setColspan(4);
	table.addCell(makeCell("Total Amount Open Receivables", Element.ALIGN_CENTER, headingFont, Rectangle.BOX, Color.LIGHT_GRAY));
	table.addCell(makeCell("Total Amount Sales", Element.ALIGN_CENTER, headingFont, Rectangle.BOX, Color.LIGHT_GRAY));
	table.addCell(makeCell("Selected Period", Element.ALIGN_CENTER, headingFont, Rectangle.BOX, Color.LIGHT_GRAY));
	table.addCell(makeCell("DSO ratio", Element.ALIGN_CENTER, headingFont, Rectangle.BOX, Color.LIGHT_GRAY));
	Font blackBoldFont2 = new Font(1, 8.0F, 1, Color.BLACK);
	for (ReportBean reportBean : arDsoList) {
	    table.addCell(makeCell(reportBean.getOpenReceivables(), Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.BOTTOM));
	    table.addCell(makeCell(reportBean.getTotalAmount(), Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.BOTTOM));
	    table.addCell(makeCell(reportBean.getNumberOfDays(), Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.BOTTOM));
	    table.addCell(makeCell(reportBean.getDsoRatio(), Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.BOTTOM));
	}
	table.addCell(lineCell);
	document.add(table);
    }

    private void destroy() {
	document.close();
    }

    public String createArReports(DsoReportForm dsoReportForm, List arDsoList, String fileName, String realPath) throws Exception {
	try {
	    this.initialize(fileName);
	    this.createBody(dsoReportForm, arDsoList, realPath);
	    this.destroy();
	} catch (Exception e) {
	    log.info("createArReports failed on " + new Date(),e);
	}
	return fileName;
    }
}
