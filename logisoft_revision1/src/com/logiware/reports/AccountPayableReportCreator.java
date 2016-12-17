package com.logiware.reports;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.reports.ReportFormatMethods;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.struts.form.AccountPayableForm;
import com.logiware.bean.AccountingBean;
import com.logiware.bean.CustomerBean;
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
import java.util.List;

import org.apache.log4j.Logger;

/**
 *
 * @author vellaisamy
 */
public class AccountPayableReportCreator extends ReportFormatMethods implements java.io.Serializable {

    private Document document = null;
    private PdfWriter pdfWriter = null;
    private AccountPayableForm accountPayableForm = null;
    private String contextPath = null;
    private CustomerBean customerBean = null;
    protected PdfTemplate totalNoOfPages = null;
    protected BaseFont fontForFooter = null;
    private Logger log = Logger.getLogger(AccountPayableReportCreator.class);

    public AccountPayableReportCreator() {
    }

    public AccountPayableReportCreator(AccountPayableForm accountPayableForm, String contextPath, CustomerBean customerBean) {
	this.accountPayableForm = accountPayableForm;
	this.contextPath = contextPath;
	this.customerBean = customerBean;
    }

    private void init(AccountPayableForm accountPayableForm, String contextPath, String fileName, CustomerBean customerBean) throws Exception {
	this.accountPayableForm = accountPayableForm;
	this.contextPath = contextPath;
	document = new Document(PageSize.A4.rotate());
	document.setMargins(10, 10, 10, 50);
	pdfWriter = pdfWriter.getInstance(document, new FileOutputStream(fileName));
	pdfWriter.setPageEvent(new AccountPayableReportCreator(accountPayableForm, contextPath, customerBean));
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
	logoCell.setVerticalAlignment(Element.ALIGN_TOP);
	headerTable.addCell(logoCell);
	PdfPCell emptyCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont, Rectangle.NO_BORDER);
	headerTable.addCell(emptyCell);
	headerTable.addCell(emptyCell);
	String title = "Account Payable Report";
	PdfPCell titleCell = makeCell(title, Element.ALIGN_CENTER, Element.ALIGN_TOP, headingFont1, Rectangle.NO_BORDER, Color.LIGHT_GRAY);
	headerTable.addCell(titleCell);
	PdfPTable dateTable = makeTable(1);
	dateTable.setWidthPercentage(100);
	String date = DateUtils.formatDate(new Date(), "MM/dd/yyyy");
	dateTable.addCell(makeCell("Date: " + date, Element.ALIGN_RIGHT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
	PdfPCell dateCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont, Rectangle.NO_BORDER);
	dateCell.addElement(dateTable);
	headerTable.addCell(dateCell);

	PdfPTable vendorDetailsTable = new PdfPTable(6);
	vendorDetailsTable.setWidthPercentage(100);
	vendorDetailsTable.setWidths(new float[]{15, 15, 20, 10, 20, 10});
	String vendorDetails = "Vendor Details";
	PdfPCell vendorDetailsCell = makeCell(vendorDetails, Element.ALIGN_LEFT, Element.ALIGN_TOP, headingFont1, Rectangle.NO_BORDER, Color.LIGHT_GRAY);
	vendorDetailsCell.setColspan(6);
	vendorDetailsTable.addCell(vendorDetailsCell);
	String vendorName = "Vendor Name : " + customerBean.getCustomerName();
	PdfPCell vendorNameCell = makeCell(vendorName, Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER);
	vendorNameCell.setColspan(6);
	vendorDetailsTable.addCell(vendorNameCell);

	vendorDetailsTable.addCell(makeCell("Vendor Number : ", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
	vendorDetailsTable.addCell(makeCell(customerBean.getCustomerNumber(), Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont, Rectangle.NO_BORDER));
	vendorDetailsTable.addCell(makeCell("Net Amount : ", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
	if (customerBean.getTotal().startsWith("-")) {
	    String total = "(" + customerBean.getTotal().replaceAll("-", "") + ")";
	    vendorDetailsTable.addCell(makeCell(total, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, redBoldFont, Rectangle.NO_BORDER));
	} else {
	    vendorDetailsTable.addCell(makeCell(customerBean.getTotal(), Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER));
	}
	vendorDetailsTable.addCell(makeCell("Current : ", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
	if (customerBean.getCurrent().startsWith("-")) {
	    String current = "(" + customerBean.getCurrent().replaceAll("-", "") + ")";
	    vendorDetailsTable.addCell(makeCell(current, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, redBoldFont, Rectangle.NO_BORDER));
	} else {
	    vendorDetailsTable.addCell(makeCell(customerBean.getCurrent(), Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER));
	}
	vendorDetailsTable.addCell(makeCell("Vendor Subtype : ", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
	vendorDetailsTable.addCell(makeCell(customerBean.getSubType(), Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont, Rectangle.NO_BORDER));
	vendorDetailsTable.addCell(makeCell("Outstanding Receivables : ", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
	if (customerBean.getOutstandingRecievables().startsWith("-")) {
	    String outstandingRecievables = customerBean.getOutstandingRecievables().replaceAll("-", "");
	    vendorDetailsTable.addCell(makeCell(outstandingRecievables, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont, Rectangle.NO_BORDER));
	} else {
	    String outstandingRecievables = "(" + customerBean.getOutstandingRecievables() + ")";
	    vendorDetailsTable.addCell(makeCell(outstandingRecievables, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, redBoldFont, Rectangle.NO_BORDER));
	}
	vendorDetailsTable.addCell(makeCell("30-60 Days : ", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
	if (customerBean.getThirtyOneToSixtyDays().startsWith("-")) {
	    String thirtyOneToSixtyDays = "(" + customerBean.getThirtyOneToSixtyDays().replaceAll("-", "") + ")";
	    vendorDetailsTable.addCell(makeCell(thirtyOneToSixtyDays, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, redBoldFont, Rectangle.NO_BORDER));
	} else {
	    vendorDetailsTable.addCell(makeCell(customerBean.getThirtyOneToSixtyDays(), Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER));
	}
	vendorDetailsTable.addCell(makeCell("Vendor Terms : ", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
	vendorDetailsTable.addCell(makeCell(customerBean.getCreditTerm(), Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont, Rectangle.NO_BORDER));
	vendorDetailsTable.addCell(makeCell(" ", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
	vendorDetailsTable.addCell(makeCell(" ", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
	vendorDetailsTable.addCell(makeCell("61-90 Days : ", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
	if (customerBean.getSixtyOneToNintyDays().startsWith("-")) {
	    String sixtyOneToNintyDays = "(" + customerBean.getSixtyOneToNintyDays().replaceAll("-", "") + ")";
	    vendorDetailsTable.addCell(makeCell(sixtyOneToNintyDays, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, redBoldFont, Rectangle.NO_BORDER));
	} else {
	    vendorDetailsTable.addCell(makeCell(customerBean.getSixtyOneToNintyDays(), Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER));
	}
	vendorDetailsTable.addCell(makeCell(" ", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
	vendorDetailsTable.addCell(makeCell(" ", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
	vendorDetailsTable.addCell(makeCell(" ", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
	vendorDetailsTable.addCell(makeCell(" ", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
	vendorDetailsTable.addCell(makeCell(">90 Days : ", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
	if (customerBean.getGreaterThanNintyDays().startsWith("-")) {
	    String greaterThanNintyDays = "(" + customerBean.getGreaterThanNintyDays().replaceAll("-", "") + ")";
	    vendorDetailsTable.addCell(makeCell(greaterThanNintyDays, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, redBoldFont, Rectangle.NO_BORDER));
	} else {
	    vendorDetailsTable.addCell(makeCell(customerBean.getGreaterThanNintyDays(), Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER));
	}
	PdfPCell vendorCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont, Rectangle.NO_BORDER);
	vendorCell.addElement(vendorDetailsTable);
	headerTable.addCell(vendorCell);

	PdfPTable filterTable = new PdfPTable(6);
	filterTable.setWidthPercentage(100);
	filterTable.setWidths(new float[]{20, 10, 20, 10, 20, 10});
	String filterHeader = "Search Criteria";
	PdfPCell filterHeaderCell = makeCell(filterHeader, Element.ALIGN_LEFT, Element.ALIGN_TOP, headingFont1, Rectangle.NO_BORDER, Color.LIGHT_GRAY);
	filterHeaderCell.setColspan(6);
	filterTable.addCell(filterHeaderCell);
	filterTable.addCell(makeCell("Date From : ", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
	filterTable.addCell(makeCell(accountPayableForm.getDatefrom(), Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont, Rectangle.NO_BORDER));
	filterTable.addCell(makeCell("Date To : ", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
	filterTable.addCell(makeCell(accountPayableForm.getDateto(), Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont, Rectangle.NO_BORDER));
	filterTable.addCell(makeCell("Invoice # : ", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
	filterTable.addCell(makeCell(accountPayableForm.getInvoicenumber(), Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont, Rectangle.NO_BORDER));
	filterTable.addCell(makeCell("Show only my A/P Accounts # : ", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
	filterTable.addCell(makeCell(accountPayableForm.getOnlyAP(), Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont, Rectangle.NO_BORDER));
	filterTable.addCell(makeCell("Show only my A/P entries : ", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
	filterTable.addCell(makeCell(accountPayableForm.getShowOnlyMyAPEntries(), Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont, Rectangle.NO_BORDER));
	filterTable.addCell(makeCell("Show Parent/Child Records : ", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
	filterTable.addCell(makeCell(accountPayableForm.getChparent(), Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont, Rectangle.NO_BORDER));
	filterTable.addCell(makeCell("Show A/Rs : ", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
	filterTable.addCell(makeCell(accountPayableForm.getOnlyAR(), Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont, Rectangle.NO_BORDER));
	filterTable.addCell(makeCell("Show on Hold : ", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
	filterTable.addCell(makeCell(accountPayableForm.getShowHold(), Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont, Rectangle.NO_BORDER));
	PdfPCell filterCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont, Rectangle.NO_BORDER);
	filterCell.addElement(filterTable);
	headerTable.addCell(filterCell);

	PdfPTable listHeadingTable = new PdfPTable(12);
	listHeadingTable.setWidthPercentage(100);
	listHeadingTable.setWidths(new float[]{20, 10, 10, 10, 10, 11, 5, 10, 4, 8, 8, 4});
	listHeadingTable.addCell(makeCell("Vendor Name", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
	listHeadingTable.addCell(makeCell("Vendor#", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
	listHeadingTable.addCell(makeCell("Invoice#", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
	listHeadingTable.addCell(makeCell("BL #", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
	listHeadingTable.addCell(makeCell("Invoice Date", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
	listHeadingTable.addCell(makeCell("Terms", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
	listHeadingTable.addCell(makeCell("Credit Hold", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
	listHeadingTable.addCell(makeCell("Amount", Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
	listHeadingTable.addCell(makeCell("Age", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
	listHeadingTable.addCell(makeCell("Check #", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
	listHeadingTable.addCell(makeCell("Due Date", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
	listHeadingTable.addCell(makeCell("Type", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
	PdfPCell listHeadingCell = makeCell("", Element.ALIGN_RIGHT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER);
	listHeadingCell.setBorderWidthTop(.5f);
	listHeadingCell.setBorderWidthBottom(.5f);
	listHeadingCell.setLeading(.5f, .5f);
	listHeadingCell.addElement(listHeadingTable);
	headerTable.addCell(listHeadingCell);
	return headerTable;
    }

    public void onOpenDocument(PdfWriter writer, Document document) {
	totalNoOfPages = writer.getDirectContent().createTemplate(100, 100);
	totalNoOfPages.setBoundingBox(new Rectangle(-20, -20, 100, 100));
	try {
	    fontForFooter = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
	} catch (Exception e) {
	    log.info("Exception in onOpenDocument: " ,e);
	}
    }

    public void onStartPage(PdfWriter writer, Document document) {
	try {
	    document.add(this.createHeaderTable());
	} catch (Exception e) {
	    log.info("Exception in onStartPage: " ,e);
	}
    }

    private void writeContents(List<AccountingBean> accountPayables) throws Exception {
	PdfPTable listContentsTable = new PdfPTable(12);
	listContentsTable.setWidths(new float[]{20, 10, 10, 10, 10, 11, 5, 10, 4, 8, 8, 4});
	listContentsTable.setWidthPercentage(100);
	for (AccountingBean accountPayable : accountPayables) {
	    listContentsTable.addCell(makeCell(accountPayable.getVendorName(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOTTOM));
	    listContentsTable.addCell(makeCell(accountPayable.getVendorNumber(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOTTOM));
	    listContentsTable.addCell(makeCell(accountPayable.getInvoiceNumber(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOTTOM));
	    listContentsTable.addCell(makeCell(accountPayable.getBillOfLadding(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOTTOM));
	    listContentsTable.addCell(makeCell(accountPayable.getFormattedDate(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOTTOM));
	    listContentsTable.addCell(makeCell(accountPayable.getCreditTerms(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOTTOM));
	    listContentsTable.addCell(makeCell(accountPayable.getCreditHold(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOTTOM));
	    if (accountPayable.getFormattedBalance().startsWith("-")
		    && accountPayable.getTransactionType().trim().equalsIgnoreCase(CommonConstants.TRANSACTION_TYPE_ACCOUNT_PAYABLE)) {
		String balance = "(" + accountPayable.getFormattedBalance().replaceAll("-", "") + ")";
		listContentsTable.addCell(makeCell(balance, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, redFont, Rectangle.BOTTOM));
	    } else if (accountPayable.getFormattedBalance().startsWith("-")
		    && accountPayable.getTransactionType().trim().equalsIgnoreCase(CommonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE)) {
		String balance = accountPayable.getFormattedBalance().replaceAll("-", "");
		listContentsTable.addCell(makeCell(balance, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOTTOM));
	    } else if (accountPayable.getTransactionType().trim().equalsIgnoreCase(CommonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE)) {
		String balance = "(" + accountPayable.getFormattedBalance() + ")";
		listContentsTable.addCell(makeCell(balance, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, redFont, Rectangle.BOTTOM));
	    } else {
		listContentsTable.addCell(makeCell(accountPayable.getFormattedBalance(), Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOTTOM));
	    }
	    listContentsTable.addCell(makeCell(accountPayable.getAge().toString(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOTTOM));
	    listContentsTable.addCell(makeCell(accountPayable.getCheckNumber(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOTTOM));
	    listContentsTable.addCell(makeCell(accountPayable.getFormattedDueDate(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOTTOM));
	    listContentsTable.addCell(makeCell(accountPayable.getTransactionType(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOTTOM));
	}
	document.add(listContentsTable);
    }

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

    public String createReport(AccountPayableForm accountPayableForm, List<AccountingBean> accountPayables, CustomerBean customerBean, String contextPath) {
	try {
            String fileName = LoadLogisoftProperties.getProperty("reportLocation") + "/Documents/AccountPayable/" + DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/";
	    File file = new File(fileName);
	    if (!file.exists()) {
		file.mkdirs();
	    }
	    if (CommonUtils.isNotEmpty(accountPayableForm.getVendornumber())) {
		fileName += accountPayableForm.getVendornumber() + ".pdf";
	    } else {
		fileName += "AccountPayable.pdf";
	    }
	    this.init(accountPayableForm, contextPath, fileName, customerBean);
	    this.writeContents(accountPayables);
	    this.destroy();
	    return fileName;
	} catch (Exception e) {
	    log.info("Exception in createReport: " ,e);
	}
	return null;
    }
}
