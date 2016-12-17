package com.logiware.accounting.reports;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.accounting.form.ApInquiryForm;
import com.logiware.accounting.model.ResultModel;
import com.logiware.accounting.model.VendorModel;
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
import org.apache.commons.lang3.StringUtils;

import org.apache.log4j.Logger;

/**
 *
 * @author Lakshmi Narayanan
 */
public class ApInquiryReportCreator extends BaseReportCreator {
private static final Logger log = Logger.getLogger(ApInquiryReportCreator.class);
    private ApInquiryForm apInquiryForm;

    public ApInquiryReportCreator() {
    }

    public ApInquiryReportCreator(ApInquiryForm apInquiryForm, String contextPath) {
	this.apInquiryForm = apInquiryForm;
	this.contextPath = contextPath;
    }

    private void init(String fileName) throws DocumentException, FileNotFoundException {
	document = new Document(PageSize.A4.rotate());
	document.setMargins(15, 15, 15, 30);
	writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
	writer.setPdfVersion(PdfWriter.PDF_VERSION_1_7);
	writer.setUserunit(1f);
	writer.setPageEvent(new ApInquiryReportCreator(apInquiryForm, contextPath));
	document.open();
	writer.setOpenAction(PdfAction.gotoLocalPage(1, new PdfDestination(PdfDestination.XYZ, -1, -1, 0.75f), writer));
    }

    private String getSearchBy(String searchBy) {
	if ("invoice_number".equals(searchBy)) {
	    return "Invoice Number";
	} else if ("invoice_amount".equals(searchBy)) {
	    return "Invoice Amount";
	} else if ("drcpt".equals(searchBy)) {
	    return "Dock Receipt";
	} else if ("voyage_no".equals(searchBy)) {
	    return "Voyage";
	} else if ("container_no".equals(searchBy)) {
	    return "Container Number";
	} else if ("booking_no".equals(searchBy)) {
	    return "Booking Number";
	} else if ("bill_ladding_no".equals(searchBy)) {
	    return "House Bill";
	} else if ("sub_house_bl".equals(searchBy)) {
	    return "Sub-House Bill";
	} else if ("master_bl".equals(searchBy)) {
	    return "Master Bill";
	} else if ("check_number".equals(searchBy)) {
	    return "Check Number";
	} else if ("check_amount".equals(searchBy)) {
	    return "Check Amount";
	} else {
	    return "";
	}
    }

    public void writeVendorDetails() throws Exception {
	PdfPTable vendorTable = new PdfPTable(3);
	vendorTable.setWidthPercentage(100);
	vendorTable.setWidths(new float[]{40, 30, 30});
	VendorModel vendor = apInquiryForm.getVendor();
	PdfPTable vendorTable1 = new PdfPTable(2);
	vendorTable1.setWidthPercentage(100);
	vendorTable1.setWidths(new float[]{20, 80});
	vendorTable1.addCell(createTextCell("Vendor Name : ", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.NO_BORDER, LAVENDAR));
	vendorTable1.addCell(createTextCell(vendor.getVendorName(), blackNormalFont7, Rectangle.NO_BORDER, LAVENDAR));
	vendorTable1.addCell(createTextCell("Address : ", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.NO_BORDER, LAVENDAR));
	vendorTable1.addCell(createTextCell(vendor.getAddress(), blackNormalFont7, Rectangle.NO_BORDER, LAVENDAR));
	vendorTable1.addCell(createTextCell("Contact : ", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.NO_BORDER, LAVENDAR));
	vendorTable1.addCell(createTextCell(vendor.getContact(), blackNormalFont7, Rectangle.NO_BORDER, LAVENDAR));
	vendorTable1.addCell(createTextCell("Phone : ", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.NO_BORDER, LAVENDAR));
	vendorTable1.addCell(createTextCell(vendor.getPhone(), blackNormalFont7, Rectangle.NO_BORDER, LAVENDAR));
	vendorTable1.addCell(createTextCell("Fax : ", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.NO_BORDER, LAVENDAR));
	vendorTable1.addCell(createTextCell(vendor.getFax(), blackNormalFont7, Rectangle.NO_BORDER, LAVENDAR));
	vendorTable1.addCell(createTextCell("Email : ", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.NO_BORDER, LAVENDAR));
	vendorTable1.addCell(createTextCell(vendor.getEmail(), blackNormalFont7, Rectangle.NO_BORDER, LAVENDAR));
	PdfPCell vendorCell1 = createEmptyCell(Rectangle.BOX, LAVENDAR);
	vendorCell1.addElement(vendorTable1);
	vendorTable.addCell(vendorCell1);
	PdfPTable vendorTable2 = new PdfPTable(2);
	vendorTable2.setWidthPercentage(100);
	vendorTable2.setWidths(new float[]{50, 50});
	vendorTable2.addCell(createTextCell("Vendor Number : ", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.NO_BORDER, LAVENDAR));
	vendorTable2.addCell(createTextCell(vendor.getVendorNumber(), blackNormalFont7, Rectangle.NO_BORDER, LAVENDAR));
	vendorTable2.addCell(createTextCell("Credit Term : ", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.NO_BORDER, LAVENDAR));
	vendorTable2.addCell(createTextCell(vendor.getCreditTerm(), blackNormalFont7, Rectangle.NO_BORDER, LAVENDAR));
	vendorTable2.addCell(createTextCell("Credit Limit : ", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.NO_BORDER, LAVENDAR));
	vendorTable2.addCell(createAmountCell(vendor.getCreditLimit(), blackNormalFont7, redNormalFont7, Element.ALIGN_LEFT, Rectangle.NO_BORDER, LAVENDAR));
	vendorTable2.addCell(createTextCell("OutStanding Receivables : ", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.NO_BORDER, LAVENDAR));
	vendorTable2.addCell(createAmountCell(vendor.getArAmount(), blackNormalFont7, redNormalFont7, Element.ALIGN_LEFT, Rectangle.NO_BORDER, LAVENDAR));
	vendorTable2.addCell(createTextCell("Net Payable Amount : ", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.NO_BORDER, LAVENDAR));
	vendorTable2.addCell(createAmountCell(vendor.getNetAmount(), blackNormalFont7, redNormalFont7, Element.ALIGN_LEFT, Rectangle.NO_BORDER, LAVENDAR));
	PdfPCell vendorCell2 = createEmptyCell(Rectangle.BOX, LAVENDAR);
	vendorCell2.addElement(vendorTable2);
	vendorTable.addCell(vendorCell2);
	PdfPTable vendorTable3 = new PdfPTable(2);
	vendorTable3.setWidthPercentage(100);
	vendorTable3.setWidths(new float[]{30, 70});
	vendorTable3.addCell(createTextCell("Sub Type : ", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.NO_BORDER, LAVENDAR));
	vendorTable3.addCell(createTextCell(vendor.getSubType(), blackNormalFont7, Rectangle.NO_BORDER, LAVENDAR));
	vendorTable3.addCell(createTextCell("Current : ", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.NO_BORDER, LAVENDAR));
	vendorTable3.addCell(createAmountCell(vendor.getAge30Amount(), blackNormalFont7, redNormalFont7, Element.ALIGN_LEFT, Rectangle.NO_BORDER, LAVENDAR));
	vendorTable3.addCell(createTextCell("30-60 Days : ", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.NO_BORDER, LAVENDAR));
	vendorTable3.addCell(createAmountCell(vendor.getAge60Amount(), blackNormalFont7, redNormalFont7, Element.ALIGN_LEFT, Rectangle.NO_BORDER, LAVENDAR));
	vendorTable3.addCell(createTextCell("61-90 Days : ", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.NO_BORDER, LAVENDAR));
	vendorTable3.addCell(createAmountCell(vendor.getAge90Amount(), blackNormalFont7, redNormalFont7, Element.ALIGN_LEFT, Rectangle.NO_BORDER, LAVENDAR));
	vendorTable3.addCell(createTextCell(">90 Days : ", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.NO_BORDER, LAVENDAR));
	vendorTable3.addCell(createAmountCell(vendor.getAge91Amount(), blackNormalFont7, redNormalFont7, Element.ALIGN_LEFT, Rectangle.NO_BORDER, LAVENDAR));
	vendorTable3.addCell(createTextCell("Total : ", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.NO_BORDER, LAVENDAR));
	vendorTable3.addCell(createAmountCell(vendor.getApAmount(), blackNormalFont7, redNormalFont7, Element.ALIGN_LEFT, Rectangle.NO_BORDER, LAVENDAR));
	PdfPCell vendorCell3 = createEmptyCell(Rectangle.BOX, LAVENDAR);
	vendorCell3.addElement(vendorTable3);
	vendorTable.addCell(vendorCell3);
	PdfPCell vendorCell = createEmptyCell(Rectangle.BOX, LAVENDAR);
	vendorCell.addElement(vendorTable);
	headerTable.addCell(vendorCell);
    }

    private void writeHeader() throws Exception {
	headerTable = new PdfPTable(1);
	headerTable.setWidthPercentage(100);
	String imagePath = LoadLogisoftProperties.getProperty("application.image.logo");
	Image image = Image.getInstance(contextPath + imagePath);
	image.scalePercent(75);
	PdfPCell logoCell = createImageCell(image);
	headerTable.addCell(logoCell);
	PdfPCell titleCell = createCell("Ap Inquiry Report", headerBoldFont15, Element.ALIGN_CENTER, Rectangle.BOX, Color.LIGHT_GRAY);
	headerTable.addCell(titleCell);
	PdfPCell cell = createCell("Date: " + DateUtils.formatDate(new Date(), "MM/dd/yyyy"), headerBoldFont11, Element.ALIGN_RIGHT, Rectangle.NO_BORDER);
	headerTable.addCell(cell);
	PdfPTable filtersTable = new PdfPTable(4);
	filtersTable.setWidthPercentage(100);
	filtersTable.setWidths(new float[]{9, 20, 9, 62});
	if (CommonUtils.isNotEmpty(apInquiryForm.getSearchBy())) {
	    filtersTable.addCell(createTextCell("Search By : ", blackBoldFont8, Rectangle.NO_BORDER));
	    filtersTable.addCell(createTextCell(getSearchBy(apInquiryForm.getSearchBy()), blackNormalFont7, Rectangle.NO_BORDER));
	    filtersTable.addCell(createTextCell("Search Value : ", blackBoldFont8, Rectangle.NO_BORDER));
	    if (CommonUtils.in(apInquiryForm.getSearchBy(), "invoice_amount", "check_amount")) {
		StringBuilder searchValue = new StringBuilder();
		if (CommonUtils.isNotEmpty(apInquiryForm.getFromAmount())) {
		    searchValue.append("From : ").append(apInquiryForm.getFromAmount());
		}
		if (CommonUtils.isNotEmpty(apInquiryForm.getToAmount())) {
		    if (CommonUtils.isNotEmpty(apInquiryForm.getFromAmount())) {
			searchValue.append("\n");
		    }
		    searchValue.append("To : ").append(apInquiryForm.getToAmount());
		}
		filtersTable.addCell(createTextCell(searchValue.toString(), blackNormalFont7, Rectangle.NO_BORDER));
	    } else {
		filtersTable.addCell(createTextCell(apInquiryForm.getSearchValue(), blackNormalFont7, Rectangle.NO_BORDER));
	    }
	} else {
	    filtersTable.addCell(createTextCell("Vendor Name : ", blackBoldFont8, Rectangle.NO_BORDER));
	    filtersTable.addCell(createTextCell(apInquiryForm.getVendorName(), blackNormalFont7, Rectangle.NO_BORDER));
	    filtersTable.addCell(createTextCell("Vendor Number : ", blackBoldFont8, Rectangle.NO_BORDER));
	    filtersTable.addCell(createTextCell(apInquiryForm.getVendorNumber(), blackNormalFont7, Rectangle.NO_BORDER));
	}
	PdfPCell filtersCell = createEmptyCell(Rectangle.BOTTOM);
	filtersCell.addElement(filtersTable);
	headerTable.addCell(filtersCell);
	if (null != apInquiryForm.getVendor()) {
	    writeVendorDetails();
	}
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
	contentTable = new PdfPTable(15);
	contentTable.setWidthPercentage(100);
	contentTable.setWidths(new float[]{15, 8, 10, 8, 8, 8, 8, 7, 8, 8, 5, 5, 5, 5, 8});
	contentTable.addCell(createHeaderCell("Vendor Name", blackBoldFont6, Element.ALIGN_CENTER, Rectangle.BOX));
	contentTable.addCell(createHeaderCell("Vendor Number", blackBoldFont6, Element.ALIGN_CENTER, Rectangle.BOX));
	contentTable.addCell(createHeaderCell("Invoice/BL", blackBoldFont6, Element.ALIGN_CENTER, Rectangle.BOX));
	contentTable.addCell(createHeaderCell("Invoice Date", blackBoldFont6, Element.ALIGN_CENTER, Rectangle.BOX));
	contentTable.addCell(createHeaderCell("Due Date", blackBoldFont6, Element.ALIGN_CENTER, Rectangle.BOX));
	contentTable.addCell(createHeaderCell("Invoice Amount", blackBoldFont6, Element.ALIGN_CENTER, Rectangle.BOX));
	contentTable.addCell(createHeaderCell("Invoice Balance", blackBoldFont6, Element.ALIGN_CENTER, Rectangle.BOX));
	contentTable.addCell(createHeaderCell("Check Number", blackBoldFont6, Element.ALIGN_CENTER, Rectangle.BOX));
	contentTable.addCell(createHeaderCell("Payment Date", blackBoldFont6, Element.ALIGN_CENTER, Rectangle.BOX));
	contentTable.addCell(createHeaderCell("Cleared Date", blackBoldFont6, Element.ALIGN_CENTER, Rectangle.BOX));
	contentTable.addCell(createHeaderCell("Dock Receipt", blackBoldFont6, Element.ALIGN_CENTER, Rectangle.BOX));
	contentTable.addCell(createHeaderCell("Voyage", blackBoldFont6, Element.ALIGN_CENTER, Rectangle.BOX));
	contentTable.addCell(createHeaderCell("Cost Code", blackBoldFont6, Element.ALIGN_CENTER, Rectangle.BOX));
	contentTable.addCell(createHeaderCell("Type", blackBoldFont6, Element.ALIGN_CENTER, Rectangle.BOX));
	contentTable.addCell(createHeaderCell("Status", blackBoldFont6, Element.ALIGN_CENTER, Rectangle.BOX));
	contentTable.setHeaderRows(1);
	if (CommonUtils.isNotEmpty(apInquiryForm.getResults())) {
	    for (ResultModel accrual : apInquiryForm.getResults()) {
		contentTable.addCell(createTextCell(StringUtils.abbreviate(accrual.getVendorName(), 20), blackNormalFont6, Rectangle.BOTTOM));
		contentTable.addCell(createTextCell(accrual.getVendorNumber(), blackNormalFont6, Rectangle.BOTTOM));
		contentTable.addCell(createTextCell(accrual.getInvoiceOrBl(), blackNormalFont6, Rectangle.BOTTOM));
		contentTable.addCell(createTextCell(accrual.getInvoiceDate(), blackNormalFont6, Rectangle.BOTTOM));
		contentTable.addCell(createTextCell(accrual.getDueDate(), blackNormalFont6, Rectangle.BOTTOM));
		contentTable.addCell(createAmountCell(accrual.getInvoiceAmount(), blackNormalFont6, redNormalFont6, Rectangle.BOTTOM));
		contentTable.addCell(createAmountCell(accrual.getInvoiceBalance(), blackNormalFont6, redNormalFont6, Rectangle.BOTTOM));
		contentTable.addCell(createTextCell(accrual.getCheckNumber(), blackNormalFont6, Rectangle.BOTTOM));
		contentTable.addCell(createTextCell(accrual.getPaymentDate(), blackNormalFont6, Rectangle.BOTTOM));
		contentTable.addCell(createTextCell(accrual.getClearedDate(), blackNormalFont6, Rectangle.BOTTOM));
		contentTable.addCell(createTextCell(accrual.getDockReceipt(), blackNormalFont6, Rectangle.BOTTOM));
		contentTable.addCell(createTextCell(accrual.getVoyage(), blackNormalFont6, Rectangle.BOTTOM));
		contentTable.addCell(createTextCell(accrual.getCostCode(), blackNormalFont6, Rectangle.BOTTOM));
		contentTable.addCell(createTextCell(accrual.getTransactionType(), blackNormalFont6, Element.ALIGN_CENTER, Rectangle.BOTTOM));
		contentTable.addCell(createTextCell(accrual.getStatus(), blackNormalFont6, Rectangle.BOTTOM));
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

    public String create() throws Exception {
	StringBuilder fileName = new StringBuilder();
	fileName.append(LoadLogisoftProperties.getProperty("reportLocation")).append("/Documents/AccountPayable/");
        fileName.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
	File file = new File(fileName.toString());
	if (!file.exists()) {
	    file.mkdirs();
	}
	fileName.append("ApInquiry.pdf");
	init(fileName.toString());
	writeContent();
	exit();
	return fileName.toString();
    }
}
