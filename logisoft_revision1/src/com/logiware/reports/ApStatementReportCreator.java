package com.logiware.reports;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.reports.ReportFormatMethods;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.gp.cvst.logisoft.struts.form.ApReportsForm;
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
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

import org.apache.log4j.Logger;

/**
 *
 * @author logiware
 */
public class ApStatementReportCreator extends ReportFormatMethods {

    private Logger log = Logger.getLogger(ApStatementReportCreator.class);
    private Document document = null;
    private PdfWriter pdfWriter = null;
    private PdfTemplate totalNoOfPages;
    private BaseFont helv;
    private String companyName = null;
    private String companyAddress = null;
    private String companyPhone = null;
    private String companyFax = null;
    private String contextPath = null;
    private String apSpecialist = null;

    public ApStatementReportCreator() throws Exception {
	SystemRulesDAO systemRulesDAO = new SystemRulesDAO();
	this.companyName = systemRulesDAO.getSystemRulesByCode("CompanyName");
	this.companyAddress = systemRulesDAO.getSystemRulesByCode("CompanyAddress");
	this.companyPhone = systemRulesDAO.getSystemRulesByCode("CompanyPhone");
	this.companyFax = systemRulesDAO.getSystemRulesByCode("CompanyFax");
    }

    public ApStatementReportCreator(String apSpecialist, String contextPath) throws Exception {
	this();
	this.apSpecialist = apSpecialist;
	this.contextPath = contextPath;
    }

    private void init(String fileName, CustomerBean vendor, String contextPath) throws Exception {
	try {
	    document = new Document(PageSize.A4);
	    document.setMargins(10, 10, 20, 80);
	    pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(fileName));
	    StringBuilder apSpecialistDetails = new StringBuilder();
	    if (null != vendor && CommonUtils.isNotEmpty(vendor.getApSpecialist())) {
		apSpecialistDetails.append(vendor.getApSpecialist()).append("(").append(vendor.getEmail()).append(")");
	    }
	    pdfWriter.setPageEvent(new ApStatementReportCreator(apSpecialistDetails.toString(), contextPath));
	    document.open();
	    PdfAction action = PdfAction.gotoLocalPage(1, new PdfDestination(PdfDestination.XYZ, -1, -1, 0.75f), pdfWriter);
	    pdfWriter.setOpenAction(action);
	} catch (Exception e) {
	    log.info("AP Statement not initialized",e);
	    throw e;
	}
    }

    @Override
    public void onOpenDocument(PdfWriter pdfWriter, Document document) {
	log.info("On Open AP Statement Document");
	try {
	    totalNoOfPages = pdfWriter.getDirectContent().createTemplate(20, 10);
	    totalNoOfPages.setBoundingBox(new Rectangle(-10, -10, 20, 50));
	    helv = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
	} catch (Exception e) {
	    log.info("On Open AP Statement Document failed :- ",e);
	}
    }

    private PdfPTable createHeader() throws Exception {
	PdfPTable headerTable = new PdfPTable(1);
	headerTable.setWidthPercentage(100);
	//Company logo in header
	String imagePath = LoadLogisoftProperties.getProperty("application.image.logo");
	Image image = Image.getInstance(contextPath + imagePath);
	image.scalePercent(75);
	PdfPCell logoCell = new PdfPCell(image);
	logoCell.setBorder(Rectangle.NO_BORDER);
	logoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	logoCell.setVerticalAlignment(Element.ALIGN_TOP);
	headerTable.addCell(logoCell);
	//Company Name in header
	headerTable.addCell(makeCell(companyName, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFont, Rectangle.NO_BORDER));
	//Company address in header
	StringBuilder address = new StringBuilder();
	address.append(companyAddress).append(". PHONE: ").append(companyPhone).append(". FAX: ").append(companyFax);
	headerTable.addCell(makeCell(address.toString(), Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER));
	headerTable.addCell(makeCell("Vendor Credit Statement", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFontForAR, Rectangle.BOX, Color.lightGray));
	headerTable.addCell(makeCell("", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFontForAR, Rectangle.NO_BORDER));
	headerTable.addCell(makeCell("", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFontForAR, Rectangle.NO_BORDER));
	return headerTable;
    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
	try {
	    document.add(createHeader());
	} catch (Exception e) {
	    log.info("On Start Page AP Statement Document failed :- ",e);
	}
    }

    private void writeContent(List<AccountingBean> transactions, CustomerBean vendor, Map<String, CustomerBean> agingBuckets,
	    ApReportsForm apReportsForm) throws Exception {
	PdfPCell lineCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.BOTTOM);
	PdfPTable emptyRow = new PdfPTable(1);
	PdfPCell emptyCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER);
	emptyRow.addCell(emptyCell);

	boolean agentFlag = false;
	PdfPTable headerTable = new PdfPTable(3);
	headerTable.setWidthPercentage(100);
	headerTable.setWidths(new float[]{40, 5, 55});
	if (null != vendor) {
	    String customerName = vendor.getCustomerName();
	    String customerNumber = vendor.getCustomerNumber();
	    String address = null != vendor.getAddress() ? vendor.getAddress() : "";
	    if (StringUtils.contains(vendor.getAccountType(), "A")
		    || StringUtils.contains(vendor.getAccountType(), "E") || StringUtils.contains(vendor.getAccountType(), "I")) {
		agentFlag = true;
	    }
	    PdfPTable customerTable = new PdfPTable(1);
	    customerTable.setWidthPercentage(100);
	    customerTable.addCell(makeCell(customerName, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
	    customerTable.addCell(makeCell(address, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont3, Rectangle.NO_BORDER));
	    PdfPTable customerNumberTable = new PdfPTable(2);
	    customerNumberTable.setWidthPercentage(100);
	    customerNumberTable.setWidths(new float[]{20, 70});
	    customerNumberTable.addCell(makeCell("Account#: ", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
	    customerNumberTable.addCell(makeCell(customerNumber, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont3, Rectangle.NO_BORDER));
	    PdfPCell customerNumberCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont3, Rectangle.NO_BORDER);
	    customerNumberCell.addElement(customerNumberTable);
	    customerTable.addCell(customerNumberCell);
	    PdfPCell customerCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont3, Rectangle.BOX);
	    customerCell.addElement(customerTable);
	    headerTable.addCell(customerCell);
	} else {
	    headerTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont3, Rectangle.BOX));
	}
	headerTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont3, Rectangle.NO_BORDER));
	PdfPTable agingTable = new PdfPTable(2);
	agingTable.setWidthPercentage(100);
	agingTable.setTotalWidth(new float[]{50, 50});
	CustomerBean arAgingBuckets = agingBuckets.get("arAgingBuckets");
	CustomerBean apAgingBuckets = agingBuckets.get("apAgingBuckets");
	boolean hasArAging = false;
	double apBalance = 0d;
	double acBalance = 0d;
	if (null != arAgingBuckets) {
	    hasArAging = true;
	    PdfPTable arAgingTable = new PdfPTable(2);
	    arAgingTable.setWidthPercentage(100);
	    arAgingTable.setTotalWidth(new float[]{50, 50});
	    arAgingTable.addCell(makeCell("AR SUMMARY", Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
	    arAgingTable.addCell(makeCell("AMOUNT", Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
	    String ageOne = arAgingBuckets.getCurrent();
	    String ageTwo = arAgingBuckets.getThirtyOneToSixtyDays();
	    String ageThree = arAgingBuckets.getSixtyOneToNintyDays();
	    String ageFour = arAgingBuckets.getGreaterThanNintyDays();
	    String ageTotal = arAgingBuckets.getTotal();
	    arAgingTable.addCell(makeCell("CURRENT", Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont3, Rectangle.NO_BORDER));
	    if (StringUtils.contains(ageOne, "-")) {
		String amount = "$(" + ageOne.replaceAll("-", "") + ")";
		arAgingTable.addCell(makeCell(amount, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, redFont, Rectangle.NO_BORDER));
	    } else {
		arAgingTable.addCell(makeCell("$" + ageOne, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER));
	    }
	    arAgingTable.addCell(makeCell("31-60 DAYS", Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont3, Rectangle.NO_BORDER));
	    if (StringUtils.contains(ageTwo, "-")) {
		String amount = "$(" + ageTwo.replaceAll("-", "") + ")";
		arAgingTable.addCell(makeCell(amount, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, redFont, Rectangle.NO_BORDER));
	    } else {
		arAgingTable.addCell(makeCell("$" + ageTwo, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER));
	    }
	    arAgingTable.addCell(makeCell("61-90 DAYS", Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont3, Rectangle.NO_BORDER));
	    if (StringUtils.contains(ageThree, "-")) {
		String amount = "$(" + ageThree.replaceAll("-", "") + ")";
		arAgingTable.addCell(makeCell(amount, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, redFont, Rectangle.NO_BORDER));
	    } else {
		arAgingTable.addCell(makeCell("$" + ageThree, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER));
	    }
	    arAgingTable.addCell(makeCell(">90 DAYS", Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont3, Rectangle.NO_BORDER));
	    if (StringUtils.contains(ageFour, "-")) {
		String amount = "$(" + ageFour.replaceAll("-", "") + ")";
		arAgingTable.addCell(makeCell(amount, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, redFont, Rectangle.NO_BORDER));
	    } else {
		arAgingTable.addCell(makeCell("$" + ageFour, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER));
	    }
	    arAgingTable.addCell(makeCell("TOTAL", Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont3, Rectangle.NO_BORDER));
	    if (StringUtils.contains(ageTotal, "-")) {
		String amount = "$(" + ageTotal.replaceAll("-", "") + ")";
		arAgingTable.addCell(makeCell(amount, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, redFont, Rectangle.NO_BORDER));
	    } else {
		arAgingTable.addCell(makeCell("$" + ageTotal, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER));
	    }
	    PdfPCell arAgingCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont3, Rectangle.NO_BORDER);
	    arAgingCell.addElement(arAgingTable);
	    agingTable.addCell(arAgingCell);
	} else {
	    agingTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont3, Rectangle.NO_BORDER));
	}
	if (null != apAgingBuckets) {
	    PdfPTable apAgingTable = new PdfPTable(2);
	    apAgingTable.setWidthPercentage(100);
	    apAgingTable.setTotalWidth(new float[]{50, 50});
	    apAgingTable.addCell(makeCell("AP SUMMARY", Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
	    apAgingTable.addCell(makeCell("AMOUNT", Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
	    String ageOne = apAgingBuckets.getCurrent();
	    String ageTwo = apAgingBuckets.getThirtyOneToSixtyDays();
	    String ageThree = apAgingBuckets.getSixtyOneToNintyDays();
	    String ageFour = apAgingBuckets.getGreaterThanNintyDays();
	    String ageTotal = apAgingBuckets.getTotal();
	    apAgingTable.addCell(makeCell("CURRENT", Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont3, Rectangle.NO_BORDER));
	    if (StringUtils.contains(ageOne, "-")) {
		String amount = "$(" + ageOne.replaceAll("-", "") + ")";
		apAgingTable.addCell(makeCell(amount, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, redFont, Rectangle.NO_BORDER));
	    } else {
		apAgingTable.addCell(makeCell("$" + ageOne, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER));
	    }
	    apAgingTable.addCell(makeCell("31-60 DAYS", Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont3, Rectangle.NO_BORDER));
	    if (StringUtils.contains(ageTwo, "-")) {
		String amount = "$(" + ageTwo.replaceAll("-", "") + ")";
		apAgingTable.addCell(makeCell(amount, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, redFont, Rectangle.NO_BORDER));
	    } else {
		apAgingTable.addCell(makeCell("$" + ageTwo, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER));
	    }
	    apAgingTable.addCell(makeCell("61-90 DAYS", Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont3, Rectangle.NO_BORDER));
	    if (StringUtils.contains(ageThree, "-")) {
		String amount = "$(" + ageThree.replaceAll("-", "") + ")";
		apAgingTable.addCell(makeCell(amount, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, redFont, Rectangle.NO_BORDER));
	    } else {
		apAgingTable.addCell(makeCell("$" + ageThree, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER));
	    }
	    apAgingTable.addCell(makeCell(">90 DAYS", Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont3, Rectangle.NO_BORDER));
	    if (StringUtils.contains(ageFour, "-")) {
		String amount = "$(" + ageFour.replaceAll("-", "") + ")";
		apAgingTable.addCell(makeCell(amount, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, redFont, Rectangle.NO_BORDER));
	    } else {
		apAgingTable.addCell(makeCell("$" + ageFour, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER));
	    }
	    apAgingTable.addCell(makeCell("TOTAL", Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont3, Rectangle.NO_BORDER));
	    if (StringUtils.contains(ageTotal, "-")) {
		String amount = "$(" + ageTotal.replaceAll("-", "") + ")";
		apAgingTable.addCell(makeCell(amount, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, redFont, Rectangle.NO_BORDER));
	    } else {
		apAgingTable.addCell(makeCell("$" + ageTotal, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER));
	    }
	    apBalance = Double.parseDouble(apAgingBuckets.getOutstandingPayables().replace(",", ""));
	    acBalance = Double.parseDouble(apAgingBuckets.getOutstandingAccruals().replace(",", ""));
	    if (hasArAging) {
		double arBalance = Double.parseDouble(arAgingBuckets.getTotal().replace(",", ""));
		apAgingTable.addCell(makeCell("AP-AR", Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont3, Rectangle.NO_BORDER));
		String netAmt1 = NumberUtils.formatNumber(arBalance + apBalance, "###,###,##0.00");
		if (StringUtils.contains(netAmt1, "-")) {
		    String amount = "$(" + netAmt1.replaceAll("-", "") + ")";
		    apAgingTable.addCell(makeCell(amount, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, redFont, Rectangle.NO_BORDER));
		} else {
		    apAgingTable.addCell(makeCell("$" + netAmt1, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER));
		}
		apAgingTable.addCell(makeCell("AP-AR-AC", Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont3, Rectangle.NO_BORDER));
		String netAmt2 = NumberUtils.formatNumber(arBalance + apBalance + acBalance, "###,###,##0.00");
		if (StringUtils.contains(netAmt2, "-")) {
		    String amount = "$(" + netAmt2.replaceAll("-", "") + ")";
		    apAgingTable.addCell(makeCell(amount, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, redFont, Rectangle.NO_BORDER));
		} else {
		    apAgingTable.addCell(makeCell("$" + netAmt2, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER));
		}
	    }
	    PdfPCell apAgingCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont3, Rectangle.NO_BORDER);
	    apAgingCell.addElement(apAgingTable);
	    agingTable.addCell(apAgingCell);
	    if (!hasArAging) {
		agingTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont3, Rectangle.NO_BORDER));
	    }
	}
	PdfPCell agingCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont3, Rectangle.BOX);
	agingCell.addElement(agingTable);
	headerTable.addCell(agingCell);
	document.add(headerTable);
	//Statement date and User
	PdfPTable statementTable = new PdfPTable(3);
	statementTable.setWidthPercentage(100);
	statementTable.setWidths(new float[]{30, 6, 64});
	String statementDate = "Statement Date" + " " + DateUtils.formatDate(new Date(), "MM/dd/yyyy");
	statementTable.addCell(makeCell(statementDate, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont3, Rectangle.BOTTOM));
	if (CommonUtils.isNotEmpty(apReportsForm.getApSpecialist())) {
	    statementTable.addCell(makeCell("User :", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont3, Rectangle.BOTTOM));
	    if (CommonUtils.isEqualIgnoreCase(apReportsForm.getApSpecialist(), "ALL")) {
		statementTable.addCell(makeCell(apReportsForm.getApSpecialist(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont3, Rectangle.BOTTOM));
	    } else {
		String userLoginName = new UserDAO().getLoginName(Integer.parseInt(apReportsForm.getApSpecialist()));
		statementTable.addCell(makeCell(null != userLoginName ? userLoginName : "", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont3, Rectangle.BOTTOM));
	    }
	} else {
	    statementTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont3, Rectangle.BOTTOM));
	    statementTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont3, Rectangle.BOTTOM));
	}
	document.add(statementTable);
	//Account detail header
	PdfPTable accountDetailTable = new PdfPTable(1);
	accountDetailTable.setWidthPercentage(100);
	accountDetailTable.addCell(makeCell("ACCOUNT DETAIL", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, headingFont3, Rectangle.NO_BORDER));
	document.add(accountDetailTable);
	document.add(emptyRow);
	//Statement list header
	PdfPTable statementHeaderTable = null;
	if (agentFlag) {
	    if (apReportsForm.isAllCustomers()) {
		statementHeaderTable = new PdfPTable(13);
		statementHeaderTable.setWidths(new float[]{8, 16, 7, 9, 8, 10, 9, 9, 9,7,15, 8, 7});
	    } else {
		statementHeaderTable = new PdfPTable(11);
		statementHeaderTable.setWidths(new float[]{10, 15, 10, 15, 10, 10, 10, 10, 10,7,15});
	    }
	} else {
	    if (apReportsForm.isAllCustomers()) {
		statementHeaderTable = new PdfPTable(11);
		statementHeaderTable.setWidths(new float[]{8, 19, 7, 10, 8, 13, 9, 9, 9,7,15});
	    } else {
		statementHeaderTable = new PdfPTable(9);
		statementHeaderTable.setWidths(new float[]{12, 18, 12, 22, 12, 12, 12,7,15});
	    }
	}
	statementHeaderTable.setWidthPercentage(100);
	if (apReportsForm.isAllCustomers()) {
	    statementHeaderTable.addCell(makeCell("Vendor#", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFontSize9, Rectangle.BOX));
	    statementHeaderTable.addCell(makeCell("Vendor Name", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFontSize9, Rectangle.BOX));
	}
	statementHeaderTable.addCell(makeCell("Date", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFontSize9, Rectangle.BOX));
	statementHeaderTable.addCell(makeCell("Vendor Reference", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFontSize9, Rectangle.BOX));
	statementHeaderTable.addCell(makeCell("Booking#", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFontSize9, Rectangle.BOX));
	statementHeaderTable.addCell(makeCell("Econo Reference", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFontSize9, Rectangle.BOX));
	statementHeaderTable.addCell(makeCell("Invoice Amount", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFontSize9, Rectangle.BOX));
	statementHeaderTable.addCell(makeCell("Payment/ Adjustment", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFontSize9, Rectangle.BOX));
	statementHeaderTable.addCell(makeCell("Balance", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFontSize9, Rectangle.BOX));
	statementHeaderTable.addCell(makeCell("Type", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFontSize9, Rectangle.BOX));
	statementHeaderTable.addCell(makeCell("Ap Specialist", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFontSize9, Rectangle.BOX));
	if (agentFlag) {
	    statementHeaderTable.addCell(makeCell("Consignee", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFontSize9, Rectangle.BOX));
	    statementHeaderTable.addCell(makeCell("Voyage", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFontSize9, Rectangle.BOX));
	}
	document.add(statementHeaderTable);
	PdfPTable statementListTable = null;
	if (agentFlag) {
	    if (apReportsForm.isAllCustomers()) {
		statementListTable = new PdfPTable(11);
		statementListTable.setWidths(new float[]{8, 16, 7, 9, 8, 10, 9, 9, 9, 8, 7});
	    } else {
		statementListTable = new PdfPTable(9);
		statementListTable.setWidths(new float[]{10, 15, 10, 15, 10, 10, 10, 10, 10});
	    }
	} else {
	    if (apReportsForm.isAllCustomers()) {
		statementListTable = new PdfPTable(9);
		statementListTable.setWidths(new float[]{8, 19, 7, 10, 8, 13, 9, 9, 9});
	    } else {
		statementListTable = new PdfPTable(9);
		statementListTable.setWidths(new float[]{12, 18, 12, 22, 12, 12, 12,8,9});
	    }
	}
	statementListTable.setWidthPercentage(100);
	int rowCount = 0;
	boolean isFirstPage = true;
	for (AccountingBean txn : transactions) {
	    if (apReportsForm.isAllCustomers()) {
		statementListTable.addCell(makeCell(txn.getCustomerNumber(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, textFontForPayment, Rectangle.NO_BORDER));
		statementListTable.addCell(makeCell(StringUtils.left(txn.getCustomerName(), 20), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, textFontForPayment, Rectangle.NO_BORDER));
	    }
	    statementListTable.addCell(makeCell(txn.getFormattedDate(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, textFontForPayment, Rectangle.NO_BORDER));
	    statementListTable.addCell(makeCell(txn.getInvoiceOrBl(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, textFontForPayment, Rectangle.NO_BORDER));
	    PdfPCell bookingCell = makeCell(txn.getBookingNumber(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, textFontForPayment, Rectangle.NO_BORDER);
	    bookingCell.setNoWrap(true);
	    statementListTable.addCell(bookingCell);
	    PdfPCell referenceCell = makeCell(txn.getCustomerReference(), Element.ALIGN_MIDDLE, Element.ALIGN_MIDDLE, textFontForPayment, Rectangle.NO_BORDER);
	    referenceCell.setNoWrap(true);
	    statementListTable.addCell(referenceCell);
	    if (txn.getFormattedAmount().startsWith("-")) {
		String amount = "$(" + StringUtils.removeStart(txn.getFormattedAmount(), "-") + ")";
		statementListTable.addCell(makeCell(amount, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, redNormalFont7, Rectangle.NO_BORDER));
	    } else {
		String amount = "$" + txn.getFormattedAmount();
		statementListTable.addCell(makeCell(amount, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, textFontForPayment, Rectangle.NO_BORDER));
	    }
	    if (StringUtils.contains(txn.getFormattedPayment(), "-") || StringUtils.equals(txn.getFormattedPayment(), "0.00")) {
		String payment = "$" + txn.getFormattedPayment().replaceAll("-", "");
		statementListTable.addCell(makeCell(payment, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, textFontForPayment, Rectangle.NO_BORDER));
	    } else {
		String payment = "$(" + txn.getFormattedPayment() + ")";
		statementListTable.addCell(makeCell(payment, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, redNormalFont7, Rectangle.NO_BORDER));
	    }
	    if (txn.getFormattedBalance().startsWith("-")) {
		String balance = "$(" + StringUtils.removeStart(txn.getFormattedBalance(), "-") + ")";
		statementListTable.addCell(makeCell(balance, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, redNormalFont7, Rectangle.NO_BORDER));
	    } else {
		String balance = "$" + txn.getFormattedBalance();
		statementListTable.addCell(makeCell(balance, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, textFontForPayment, Rectangle.NO_BORDER));
	    }
                statementListTable.addCell(makeCell(txn.getTransactionType(), Element.ALIGN_MIDDLE, Element.ALIGN_LEFT, textFontForPayment, Rectangle.NO_BORDER));
                statementListTable.addCell(makeCell(txn.getApSpecialist(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, textFontForPayment, Rectangle.NO_BORDER));
	    // Only for Agent with Customer Type 'A,E,I';
	    if (agentFlag) {
		statementListTable.addCell(makeCell(txn.getConsignee(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, textFontForPayment, Rectangle.NO_BORDER));
		statementListTable.addCell(makeCell(txn.getVoyage(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, textFontForPayment, Rectangle.NO_BORDER));
	    }
	    document.add(statementListTable);
	    statementListTable.deleteBodyRows();
	    if (isFirstPage && ((hasArAging && rowCount == 35) || rowCount == 36)) {
		document.add(emptyRow);
		document.add(emptyRow);
		if (((hasArAging && transactions.size() != 35) || transactions.size() != 36)) {
		    document.newPage();
		    document.add(statementHeaderTable);
		}
		rowCount = 0;
		isFirstPage = false;
	    } else if (rowCount == 50) {
		document.newPage();
		document.add(statementHeaderTable);
		rowCount = 0;
	    }
	    rowCount++;
	}
	PdfPTable lineTable = new PdfPTable(1);
	lineTable.setWidthPercentage(100);
	lineTable.addCell(lineCell);
	document.add(lineTable);
    }

    //This is for Footer and Page X Of Y
    @Override
    public void onEndPage(PdfWriter writer, Document document) {
	try {
	    log.info("In onEndPage, page = " + document.getPageNumber());
	    PdfContentByte cb = writer.getDirectContent();
	    cb.saveState();
	    BaseFont bfHelvNormal = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
	    String lineOnBottom = "________________________________________________________________________________________________________"
		    + "_________________________";
	    cb.beginText();
	    cb.setFontAndSize(bfHelvNormal, 8);
	    cb.setTextMatrix(document.left(), document.bottomMargin() - 20);
	    cb.showText(lineOnBottom);
	    cb.endText();
	    cb.beginText();
	    cb.setFontAndSize(bfHelvNormal, 8);
	    cb.setTextMatrix(document.left(), document.bottomMargin() - 30);
	    cb.showText(companyName + " Account Contact: " + apSpecialist);
	    cb.endText();

	    //This is for Page X Of Y
	    String text = "Page " + writer.getPageNumber() + " of ";
	    cb.beginText();
	    cb.setFontAndSize(bfHelvNormal, 8);
	    cb.setTextMatrix(document.right() - 300, document.bottomMargin() - 50);
	    cb.showText(text);
	    cb.endText();
	    cb.addTemplate(totalNoOfPages, document.right() - 300 + helv.getWidthPoint(text, 8), document.bottomMargin() - 50);
	    cb.restoreState();
	} catch (Exception e) {
	    log.info("On End Page of AP Statement failed :- ",e);
	}
    }

    //This is for caluculating total number of pages.
    @Override
    public void onCloseDocument(PdfWriter writer, Document document) {
	BaseFont bfHelvNormal = null;
	try {
	    bfHelvNormal = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
	    totalNoOfPages.beginText();
	    totalNoOfPages.setFontAndSize(bfHelvNormal, 8);//helv
	    totalNoOfPages.setTextMatrix(0, 0);
	    totalNoOfPages.showText(String.valueOf(document.getPageNumber() - 1));
	    totalNoOfPages.endText();
	} catch (Exception e) {
	    log.info("On Close AP Statement failed :- ",e);
	}
    }

    public void doExit() {
	document.close();
    }

    public String createReport(String contextPath, List<AccountingBean> transactions,
	    CustomerBean vendorDetails, Map<String, CustomerBean> agingBuckets, ApReportsForm apReportsForm) throws Exception {
	try {
	    StringBuilder fileName = new StringBuilder();
	    fileName.append(LoadLogisoftProperties.getProperty("reportLocation")).append("/Documents/ApReports/Statement/");
	    fileName.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
	    File file = new File(fileName.toString());
	    if (!file.exists()) {
		file.mkdirs();
	    }
	    if (apReportsForm.isAllCustomers()) {
		fileName.append("AllCustomer");
	    } else if (CommonUtils.isNotEmpty(apReportsForm.getApSpecialist())) {
		fileName.append("ApSpecialist");
	    } else {
		fileName.append(apReportsForm.getVendorNumber());
	    }
	    fileName.append(".pdf");
	    init(fileName.toString(), vendorDetails, contextPath);
	    writeContent(transactions, vendorDetails, agingBuckets, apReportsForm);
	    doExit();
	    return fileName.toString();
	} catch (Exception e) {
	    log.info("createReport failed :- ",e);
	    return null;
	}
    }
}