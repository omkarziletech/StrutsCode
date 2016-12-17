package com.gp.cong.logisoft.reports;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.AccountingConstants;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.gp.cvst.logisoft.struts.form.CustomerStatementForm;
import com.logiware.bean.AccountingBean;
import com.logiware.bean.CustomerBean;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
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
 * @author LakshmiNarayanan
 */
public class ManualStatementCreator extends ReportFormatMethods {

    private Logger log = Logger.getLogger(ManualStatementCreator.class);
    private Document document = null;
    private PdfWriter pdfWriter = null;
    private PdfTemplate totalNoOfPages;
    private BaseFont helv;
    private String companyName = null;
    private String companyAddress = null;
    private String companyPhone = null;
    private String companyFax = null;
    private String eftBank = null;
    private String eftAcctName = null;
    private String eftABANo = null;
    private String eftSwiftCode = null;
    private String eftAccountNo = null;
    private String eftAddress = null;
    private String contextPath = null;
    private String collector = null;

    private void init(String fileName, CustomerBean customer, String contextPath) throws Exception {
	try {
	    document = new Document(PageSize.A4);
	    document.setMargins(10, 10, 20, 80);
	    pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(fileName));
	    StringBuilder collectorDetails = new StringBuilder();
	    if (null != customer && CommonUtils.isNotEmpty(customer.getCollector())) {
		collectorDetails.append(customer.getCollector()).append("(").append(customer.getCollectorEmail()).append(")");
	    }
	    pdfWriter.setPageEvent(new ManualStatementCreator(collectorDetails.toString(), contextPath));
	    document.open();
	    PdfAction action = PdfAction.gotoLocalPage(1, new PdfDestination(PdfDestination.XYZ, -1, -1, 0.75f), pdfWriter);
	    pdfWriter.setOpenAction(action);
	} catch (Exception e) {
	    log.info("ManualStatement not initialized",e);
	    throw e;
	}
    }

    public ManualStatementCreator() throws Exception {
	SystemRulesDAO systemRulesDAO = new SystemRulesDAO();
	this.companyName = systemRulesDAO.getSystemRulesByCode("CompanyName");
	this.companyAddress = systemRulesDAO.getSystemRulesByCode("CompanyAddress");
	this.companyPhone = systemRulesDAO.getSystemRulesByCode("CompanyPhone");
	this.companyFax = systemRulesDAO.getSystemRulesByCode("CompanyFax");
	this.eftBank = systemRulesDAO.getSystemRulesByCode("EFTBank");
	this.eftAcctName = systemRulesDAO.getSystemRulesByCode("EFTAcctName");
	this.eftABANo = systemRulesDAO.getSystemRulesByCode("EFTABANo");
	this.eftSwiftCode = systemRulesDAO.getSystemRulesByCode("EFTSwiftCode");
	this.eftAccountNo = systemRulesDAO.getSystemRulesByCode("EFTAccountNo");
	this.eftAddress = systemRulesDAO.getSystemRulesByCode("EFTBankAddress");
    }

    public ManualStatementCreator(String collector, String contextPath) throws Exception {
	this();
	this.collector = collector;
	this.contextPath = contextPath;
    }

    @Override
    public void onOpenDocument(PdfWriter pdfWriter, Document document) {
	log.info("On Open ManualStatement Document");
	try {
	    totalNoOfPages = pdfWriter.getDirectContent().createTemplate(20, 10);
	    totalNoOfPages.setBoundingBox(new Rectangle(-10, -10, 20, 50));
	    helv = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
	} catch (Exception e) {
	    log.info("On Open ManualStatement Document failed :- ",e);
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
	headerTable.addCell(makeCell("STATEMENT", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFontForAR, Rectangle.BOX, Color.lightGray));
	headerTable.addCell(makeCell("", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFontForAR, Rectangle.NO_BORDER));
	headerTable.addCell(makeCell("", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFontForAR, Rectangle.NO_BORDER));
	return headerTable;
    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
	try {
	    document.add(createHeader());
	} catch (Exception e) {
	    log.info("On Start Page ManualStatement Document failed :- ",e);
	}
    }

    private void writeCoverLetter(CustomerBean customer, Map<String, CustomerBean> agingBuckets, String subject) throws DocumentException, Exception {
	PdfPTable emptyRow = new PdfPTable(1);
	PdfPCell emptyCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER);
	emptyRow.addCell(emptyCell);
	PdfPCell lineCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.BOTTOM);
	PdfPTable coverHeaderTable = new PdfPTable(3);
	coverHeaderTable.setWidthPercentage(100);
	coverHeaderTable.setWidths(new float[]{48, 2, 48});
	//Company details in cover letter
	PdfPTable companyTable = new PdfPTable(1);
	companyTable.setWidthPercentage(100);
	companyTable.addCell(makeCell(companyName, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFontForAR, Rectangle.NO_BORDER));
	StringBuilder companyDetail = new StringBuilder();
	companyDetail.append(companyAddress);
	companyDetail.append("\nPhone: ").append(companyPhone).append(".\nFax: ").append(companyFax);
	String email = CommonUtils.isNotEmpty(customer.getCollectorEmail()) ? customer.getCollectorEmail() : "payments@econocaribe.com";
	companyDetail.append("\nEmail: ").append(email);
	companyTable.addCell(makeCell(companyDetail.toString(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFontForAR, Rectangle.NO_BORDER));
	PdfPCell companyCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, headingFontForAR, Rectangle.BOX);
	companyCell.addElement(companyTable);
	coverHeaderTable.addCell(companyCell);
	//Empty cell
	coverHeaderTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, headingFontForAR, Rectangle.NO_BORDER));
	//Customer details in cover letter
	PdfPTable customerTable = new PdfPTable(1);
	customerTable.setWidthPercentage(100);
	customerTable.addCell(makeCell(customer.getCustomerName(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFontForAR, Rectangle.NO_BORDER));
	StringBuilder customerDetail = new StringBuilder();
	customerDetail.append(customer.getAddress());
//        customerDetail.append("\nPhone: ").append(customer.getPhone()).append(",\nFax: ").append(customer.getFax());
	customerTable.addCell(makeCell(customerDetail.toString(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFontForAR, Rectangle.NO_BORDER));
	PdfPCell customerCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, headingFontForAR, Rectangle.BOX);
	customerCell.addElement(customerTable);
	coverHeaderTable.addCell(customerCell);
	emptyCell.setColspan(3);
	coverHeaderTable.addCell(emptyCell);
	// Statement date
	String statementDate = "Statement Date" + " " + DateUtils.formatDate(new Date(), "MM/dd/yyyy");
	coverHeaderTable.addCell(makeCell(statementDate, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
	//Empty cell
	coverHeaderTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
	//Customer number
	String customerNumber = "Account# : " + customer.getCustomerNumber();
	coverHeaderTable.addCell(makeCell(customerNumber, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
	lineCell.setColspan(3);
	coverHeaderTable.addCell(lineCell);
	emptyCell.setColspan(3);
	coverHeaderTable.addCell(emptyCell);
	PdfPCell accountSummaryCell = makeCell("ACCOUNT SUMMARY", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, headingFont3, Rectangle.NO_BORDER);
	accountSummaryCell.setColspan(3);
	coverHeaderTable.addCell(accountSummaryCell);
	emptyCell.setColspan(3);
	coverHeaderTable.addCell(emptyCell);
	document.add(coverHeaderTable);
	CustomerBean arAgingBuckets = agingBuckets.get("arAgingBuckets");
	String ageOne = arAgingBuckets.getCurrent();
	String ageTwo = arAgingBuckets.getThirtyOneToSixtyDays();
	String ageThree = arAgingBuckets.getSixtyOneToNintyDays();
	String ageFour = arAgingBuckets.getGreaterThanNintyDays();
	String ageTotal = arAgingBuckets.getTotal();
	String total = arAgingBuckets.getTotal();
	PdfPTable agingTable = new PdfPTable(5);
	agingTable.setWidthPercentage(100);
	agingTable.setWidths(new float[]{20, 20, 20, 20, 20});
	//Aging header in cover letter
	agingTable.addCell(makeCell("CURRENT", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFont, Rectangle.BOX));
	agingTable.addCell(makeCell("31-60 DAYS", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFont, Rectangle.BOX));
	agingTable.addCell(makeCell("61-90 DAYS", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFont, Rectangle.BOX));
	agingTable.addCell(makeCell(">90 DAYS", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFont, Rectangle.BOX));
	agingTable.addCell(makeCell("TOTAL\nBALANCE", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFont, Rectangle.BOX));
	//Aging content in cover letter
	if (StringUtils.contains(ageOne, "-")) {
	    agingTable.addCell(makeCell("$(" + ageOne.replaceAll("-", "") + ")", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, redFont, Rectangle.BOX));
	} else if (CommonUtils.isNotEmpty(ageOne)) {
	    agingTable.addCell(makeCell("$" + ageOne, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOX));
	} else {
	    agingTable.addCell(makeCell("", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOX));
	}
	if (StringUtils.contains(ageTwo, "-")) {
	    agingTable.addCell(makeCell("$(" + ageTwo.replaceAll("-", "") + ")", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, redFont, Rectangle.BOX));
	} else if (CommonUtils.isNotEmpty(ageTwo)) {
	    agingTable.addCell(makeCell("$" + ageTwo, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOX));
	} else {
	    agingTable.addCell(makeCell("", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOX));
	}
	if (StringUtils.contains(ageThree, "-")) {
	    agingTable.addCell(makeCell("$(" + ageThree.replaceAll("-", "") + ")", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, redFont, Rectangle.BOX));
	} else if (CommonUtils.isNotEmpty(ageThree)) {
	    agingTable.addCell(makeCell("$" + ageThree, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOX));
	} else {
	    agingTable.addCell(makeCell("", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOX));
	}
	if (StringUtils.contains(ageFour, "-")) {
	    agingTable.addCell(makeCell("$(" + ageFour.replaceAll("-", "") + ")", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, redFont, Rectangle.BOX));
	} else if (CommonUtils.isNotEmpty(ageFour)) {
	    agingTable.addCell(makeCell("$" + ageFour, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOX));
	} else {
	    agingTable.addCell(makeCell("", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOX));
	}
	if (StringUtils.contains(ageTotal, "-")) {
	    agingTable.addCell(makeCell("$(" + ageTotal.replaceAll("-", "") + ")", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, redFont, Rectangle.BOX));
	} else if (CommonUtils.isNotEmpty(ageTotal)) {
	    agingTable.addCell(makeCell("$" + ageTotal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOX));
	} else {
	    agingTable.addCell(makeCell("", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOX));
	}
	emptyCell.setColspan(8);
	agingTable.addCell(emptyCell);
	document.add(agingTable);
	document.add(emptyRow);
	//Message board in cover letter
	PdfPTable messageBoardTable = new PdfPTable(1);
	messageBoardTable.setWidthPercentage(100);
	messageBoardTable.addCell(makeCell("Message Board:   " + (CommonUtils.isNotEmpty(subject) ? subject + "\n\n" : "\n\n"),
		Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont3, Rectangle.BOX));
	document.add(messageBoardTable);
	document.add(emptyRow);
	document.add(emptyRow);
	PdfPTable commentsTable = new PdfPTable(3);
	commentsTable.setWidthPercentage(100);
	commentsTable.setWidths(new float[]{45, 5, 45});
	//Comment header in cover letter
	String commentHeader = "-----------------------------------------------------------------------------"
		+ "(Cut Here if attaching comments)------------------------------------------------------------------";
	PdfPCell commentHeaderCell = makeCell(commentHeader, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER);
	commentHeaderCell.setColspan(3);
	commentsTable.addCell(commentHeaderCell);
	emptyCell.setColspan(3);
	commentsTable.addCell(emptyCell);
	//Comment content in cover letter
	PdfPTable commentContentTable = new PdfPTable(1);
	commentContentTable.setWidthPercentage(90);
	commentContentTable.setHorizontalAlignment(Element.ALIGN_CENTER);
	commentContentTable.addCell(makeCell("Comments (Plese enter your Comments here)",
		Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont3, Rectangle.BOTTOM));
	PdfPCell commentEmptyCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont3, Rectangle.BOTTOM);
	commentEmptyCell.setPadding(9f);
	commentContentTable.addCell(commentEmptyCell);
	commentContentTable.addCell(commentEmptyCell);
	commentContentTable.addCell(commentEmptyCell);
	commentContentTable.addCell(commentEmptyCell);
	PdfPCell commentContentCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont3, Rectangle.BOX);
	commentContentCell.addElement(commentContentTable);
	commentsTable.addCell(commentContentCell);
	//Empty cell
	commentsTable.addCell(makeCell("", Element.ALIGN_RIGHT, Element.ALIGN_TOP, blackFont, Rectangle.NO_BORDER));
	//Wire transfer instruction in cover letter
	PdfPTable wireTransferTable = new PdfPTable(1);
	wireTransferTable.setWidthPercentage(90);
	wireTransferTable.setHorizontalAlignment(Element.ALIGN_CENTER);
	wireTransferTable.addCell(makeCell("Wire Transfer Instructions", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont3, Rectangle.BOTTOM));
	StringBuilder wireInstruction = new StringBuilder("If paying via wire transfer, please send to:\n\n");
	wireInstruction.append("Bank: ").append(eftBank).append("\nAddress: ").append(eftAddress);
	wireInstruction.append("\nABA#: ").append(eftABANo).append("\nSwift Code: ").append(eftSwiftCode);
	wireInstruction.append("\nAccount Name: ").append(eftAcctName).append("\nAccount#:").append(eftAccountNo);
	wireTransferTable.addCell(makeCell(wireInstruction.toString(), Element.ALIGN_LEFT, Element.ALIGN_TOP, blackFont, Rectangle.NO_BORDER));
	PdfPCell wireTransferCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont3, Rectangle.BOX);
	wireTransferCell.addElement(wireTransferTable);
	commentsTable.addCell(wireTransferCell);
	emptyCell.setColspan(3);
	commentsTable.addCell(emptyCell);
	//Comment footer in cover letter
	String commentFooter = "---------------------------------------------------------------------------------------"
		+ "(Cut or fold Here)------------------------------------------------------------------------------";
	PdfPCell commentFooterCell = makeCell(commentFooter, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER);
	commentFooterCell.setColspan(3);
	commentsTable.addCell(commentFooterCell);
	document.add(commentsTable);
	document.add(emptyRow);
	PdfPTable paymentCouponTable = new PdfPTable(3);
	paymentCouponTable.setWidthPercentage(100);
	paymentCouponTable.setWidths(new float[]{45, 10, 45});
	//Payment coupon header in cover letter
	PdfPCell paymentCouponHeaderCell = makeCell("Payment Coupon \t  (Please include coupon with payment details along with your check)",
		Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, customerStatementTimesBoldFont, Rectangle.NO_BORDER);
	paymentCouponHeaderCell.setColspan(3);
	paymentCouponTable.addCell(paymentCouponHeaderCell);
	emptyCell.setColspan(3);
	paymentCouponTable.addCell(emptyCell);
	//Payment coupon detail1 in cover letter
	PdfPTable paymentCouponDetailTable1 = new PdfPTable(1);
	paymentCouponDetailTable1.setWidthPercentage(100);
	paymentCouponDetailTable1.addCell(makeCell("Statement Date : " + DateUtils.formatDate(new Date(), "MM/dd/yyyy"),
		Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont3, Rectangle.BOTTOM));
	emptyCell.setColspan(1);
	paymentCouponDetailTable1.addCell(emptyCell);
	paymentCouponDetailTable1.addCell(makeCell("Account number : " + customer.getCustomerNumber(),
		Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont3, Rectangle.BOTTOM));
	emptyCell.setColspan(1);
	paymentCouponDetailTable1.addCell(emptyCell);
	if (StringUtils.contains(total, "-")) {
	    total = "(" + total.replaceAll("-", "") + ")";
	}
	paymentCouponDetailTable1.addCell(makeCell("Total Balance : $" + total,
		Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont3, Rectangle.BOTTOM));
	emptyCell.setColspan(1);
	paymentCouponDetailTable1.addCell(emptyCell);
	paymentCouponDetailTable1.addCell(makeCell(customer.getCustomerName(), Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
	paymentCouponDetailTable1.addCell(makeCell(customerDetail.toString(), Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
	PdfPCell paymentCouponDetailCell1 = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER);
	paymentCouponDetailCell1.addElement(paymentCouponDetailTable1);
	paymentCouponTable.addCell(paymentCouponDetailCell1);
	//Empty cell
	paymentCouponTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
	//Payment coupon detail2 in cover letter
	PdfPTable paymentCouponDetailTable2 = new PdfPTable(1);
	paymentCouponDetailTable2.setWidthPercentage(100);
	paymentCouponDetailTable2.addCell(makeCell("Amount Enclosed: $______________________\n ",
		Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont3, Rectangle.BOX));
	emptyCell.setColspan(1);
	paymentCouponDetailTable2.addCell(emptyCell);
	paymentCouponDetailTable2.addCell(makeCell("Please mark box to indicate change of address \n(please attach new address)\n ",
		Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont3, Rectangle.BOX));
	emptyCell.setColspan(1);
	paymentCouponDetailTable2.addCell(emptyCell);
	//Remit detail in cover letter
	PdfPTable remitTable = new PdfPTable(1);
	remitTable.setWidthPercentage(100);
	remitTable.addCell(makeCell("PLEASE REMIT PAYMENT TO :", Element.ALIGN_LEFT, Element.ALIGN_TOP, textFontForBatch, Rectangle.NO_BORDER));
	remitTable.addCell(makeCell(companyName, Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
	remitTable.addCell(makeCell(companyDetail.toString(), Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
	PdfPCell remitCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.BOX);
	remitCell.addElement(remitTable);
	paymentCouponDetailTable2.addCell(remitCell);
	PdfPCell paymentCouponDetailCell2 = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER);
	paymentCouponDetailCell2.addElement(paymentCouponDetailTable2);
	paymentCouponTable.addCell(paymentCouponDetailCell2);
	document.add(paymentCouponTable);
	document.newPage();
    }

    private void writeContents(List<AccountingBean> transactions,
	    CustomerBean customer, Map<String, CustomerBean> agingBuckets, boolean writeCoverLetter, String subject) throws DocumentException, Exception {
	PdfPCell lineCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.BOTTOM);
	PdfPTable emptyRow = new PdfPTable(1);
	PdfPCell emptyCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER);
	emptyRow.addCell(emptyCell);
	if (writeCoverLetter) {
	    //Cover letter
	    writeCoverLetter(customer, agingBuckets, subject);
	}
	boolean agentFlag = false;
	PdfPTable headerTable = new PdfPTable(3);
	headerTable.setWidthPercentage(100);
	headerTable.setWidths(new float[]{40, 5, 55});
	if (null != customer) {
	    String customerName = customer.getCustomerName();
	    String customerNumber = customer.getCustomerNumber();
	    String address = null != customer.getAddress() ? customer.getAddress() : "";
	    if (StringUtils.contains(customer.getAccountType(), "A")
		    || StringUtils.contains(customer.getAccountType(), "E") || StringUtils.contains(customer.getAccountType(), "I")) {
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
	CustomerBean apAgingBuckets = agingBuckets.get("apAgingBuckets");
	boolean noApAging = true;
	double apBalance = 0d;
	double acBalance = 0d;
	if (null != apAgingBuckets) {
	    noApAging = false;
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
	    PdfPCell apAgingCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont3, Rectangle.NO_BORDER);
	    apAgingCell.addElement(apAgingTable);
	    agingTable.addCell(apAgingCell);
	    apBalance = Double.parseDouble(apAgingBuckets.getOutstandingPayables().replace(",", ""));
	    acBalance = Double.parseDouble(apAgingBuckets.getOutstandingAccruals().replace(",", ""));
	}
	CustomerBean arAgingBuckets = agingBuckets.get("arAgingBuckets");
	if (null != arAgingBuckets) {
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
	    if (!noApAging) {
		double arBalance = Double.parseDouble(ageTotal.replace(",", ""));
		arAgingTable.addCell(makeCell("AR-AP", Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont3, Rectangle.NO_BORDER));
		String netAmt1 = NumberUtils.formatNumber(arBalance + apBalance, "###,###,##0.00");
		if (StringUtils.contains(netAmt1, "-")) {
		    String amount = "$(" + netAmt1.replaceAll("-", "") + ")";
		    arAgingTable.addCell(makeCell(amount, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, redFont, Rectangle.NO_BORDER));
		} else {
		    arAgingTable.addCell(makeCell("$" + netAmt1, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER));
		}
		arAgingTable.addCell(makeCell("AR-AP-AC", Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont3, Rectangle.NO_BORDER));
		String netAmt2 = NumberUtils.formatNumber(arBalance + apBalance + acBalance, "###,###,##0.00");
		if (StringUtils.contains(netAmt2, "-")) {
		    String amount = "$(" + netAmt2.replaceAll("-", "") + ")";
		    arAgingTable.addCell(makeCell(amount, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, redFont, Rectangle.NO_BORDER));
		} else {
		    arAgingTable.addCell(makeCell("$" + netAmt2, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER));
		}
	    }
	    PdfPCell arAgingCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont3, Rectangle.NO_BORDER);
	    arAgingCell.addElement(arAgingTable);
	    agingTable.addCell(arAgingCell);
	    if (noApAging) {
		agingTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont3, Rectangle.NO_BORDER));
	    }
	} else {
	    agingTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont3, Rectangle.NO_BORDER));
	}
	PdfPCell agingCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont3, Rectangle.BOX);
	agingCell.addElement(agingTable);
	headerTable.addCell(agingCell);
	document.add(headerTable);
	//Statement date
	PdfPTable statementTable = new PdfPTable(1);
	statementTable.setWidthPercentage(100);
	String statementDate = "Statement Date" + " " + DateUtils.formatDate(new Date(), "MM/dd/yyyy");
	statementTable.addCell(makeCell(statementDate, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont3, Rectangle.BOTTOM));
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
	    statementHeaderTable = new PdfPTable(9);
	    statementHeaderTable.setWidths(new float[]{8, 14, 9, 18, 10, 11, 10, 11, 9});
	} else {
	    statementHeaderTable = new PdfPTable(7);
	    statementHeaderTable.setWidths(new float[]{8, 17, 17, 17, 10, 11, 12});
	}
	statementHeaderTable.setWidthPercentage(100);
	statementHeaderTable.addCell(makeCell("Date", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFontSize9, Rectangle.BOX));
	statementHeaderTable.addCell(makeCell("Invoice/BL", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFontSize9, Rectangle.BOX));
	statementHeaderTable.addCell(makeCell("Booking#", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFontSize9, Rectangle.BOX));
	statementHeaderTable.addCell(makeCell("Customer Reference", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFontSize9, Rectangle.BOX));
	statementHeaderTable.addCell(makeCell("Invoice Amount", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFontSize9, Rectangle.BOX));
	statementHeaderTable.addCell(makeCell("Payment/ Adjustment", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFontSize9, Rectangle.BOX));
	statementHeaderTable.addCell(makeCell("Balance", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFontSize9, Rectangle.BOX));
	if (agentFlag) {
	    statementHeaderTable.addCell(makeCell("Consignee", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFontSize9, Rectangle.BOX));
	    statementHeaderTable.addCell(makeCell("Voyage", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFontSize9, Rectangle.BOX));
	}
	document.add(statementHeaderTable);
	PdfPTable statementListTable = null;
	if (agentFlag) {
	    statementListTable = new PdfPTable(9);
	    statementListTable.setWidths(new float[]{8, 14, 9, 18, 10, 11, 10, 11, 9});
	} else {
	    statementListTable = new PdfPTable(7);
	    statementListTable.setWidths(new float[]{8, 17, 17, 17, 10, 11, 12});
	}
	statementListTable.setWidthPercentage(100);
	int rowCount = 0;
	boolean isFirstPage = true;
	for (AccountingBean txn : transactions) {
	    statementListTable.addCell(makeCell(txn.getFormattedDate(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, textFontForPayment, Rectangle.NO_BORDER));
	    statementListTable.addCell(makeCell(txn.getInvoiceOrBl(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, textFontForPayment, Rectangle.NO_BORDER));
	    PdfPCell bookingCell = makeCell(txn.getBookingNumber(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, textFontForPayment, Rectangle.NO_BORDER);
	    bookingCell.setNoWrap(true);
	    statementListTable.addCell(bookingCell);
	    PdfPCell referenceCell = makeCell(txn.getCustomerReference(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, textFontForPayment, Rectangle.NO_BORDER);
	    referenceCell.setNoWrap(true);
	    statementListTable.addCell(referenceCell);
	    if (txn.getFormattedAmount().startsWith("-")) {
		String amount = "$(" + StringUtils.removeStart(txn.getFormattedAmount(), "-") + ")";
		statementListTable.addCell(makeCell(amount, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, redNormalFont7, Rectangle.NO_BORDER));
	    } else {
		String amount = "$" + txn.getFormattedAmount();
		statementListTable.addCell(makeCell(amount, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, textFontForPayment, Rectangle.NO_BORDER));
	    }
	    if (StringUtils.contains(txn.getFormattedPayment(), "-") || StringUtils.equals(txn.getFormattedPayment(), "0.00")) {
		String payment = "$" + txn.getFormattedPayment().replaceAll("-", "");
		statementListTable.addCell(makeCell(payment, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, textFontForPayment, Rectangle.NO_BORDER));
	    } else {
		String payment = "$(" + txn.getFormattedPayment() + ")";
		statementListTable.addCell(makeCell(payment, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, redNormalFont7, Rectangle.NO_BORDER));
	    }
	    if (txn.getFormattedBalance().startsWith("-")) {
		String balance = "$(" + StringUtils.removeStart(txn.getFormattedBalance(), "-") + ")";
		statementListTable.addCell(makeCell(balance, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, redNormalFont7, Rectangle.NO_BORDER));
	    } else {
		String balance = "$" + txn.getFormattedBalance();
		statementListTable.addCell(makeCell(balance, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, textFontForPayment, Rectangle.NO_BORDER));
	    }
	    // Only for Agent with Customer Type 'A,E,I';
	    if (agentFlag) {
		statementListTable.addCell(makeCell(txn.getConsignee(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, textFontForPayment, Rectangle.NO_BORDER));
		statementListTable.addCell(makeCell(txn.getVoyage(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, textFontForPayment, Rectangle.NO_BORDER));
	    }
	    document.add(statementListTable);
	    statementListTable.deleteBodyRows();
	    if (isFirstPage && ((!noApAging && rowCount == 25) || rowCount == 26)) {
		document.add(emptyRow);
		document.add(emptyRow);
		document.add(getPaymentMethodTable());
		if (((!noApAging && transactions.size() != 25) || transactions.size() != 26)) {
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
	if (CommonUtils.isEmpty(transactions) || transactions.size() <= 26) {
	    Rectangle page = document.getPageSize();
	    PdfPTable paymentMethodTable = getPaymentMethodTable();
	    paymentMethodTable.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());
	    paymentMethodTable.writeSelectedRows(0, -1, document.leftMargin(), document.bottomMargin() + 120, pdfWriter.getDirectContent());
	}
    }

    private PdfPTable getPaymentMethodTable() throws DocumentException, Exception {
	PdfPCell emptyCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER);
	//Payment methods
	PdfPTable paymentMethodTable = new PdfPTable(5);
	paymentMethodTable.setWidthPercentage(100);
	paymentMethodTable.setWidths(new float[]{30.0f, 1.0f, 32.5f, 1.0f, 35.5f});
	PdfPCell paymentHeaderCell = makeCell("PAYMENT METHODS", Element.ALIGN_LEFT, Element.ALIGN_TOP, headingFont3, Rectangle.BOTTOM);
	paymentHeaderCell.setColspan(5);
	paymentMethodTable.addCell(paymentHeaderCell);
	emptyCell.setColspan(5);
	paymentMethodTable.addCell(emptyCell);
	//Check payment
	PdfPTable checkTable = new PdfPTable(1);
	checkTable.setWidthPercentage(100);
	checkTable.addCell(makeCell("Payment By Check", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont3, Rectangle.BOTTOM));
	checkTable.addCell(makeCell("PLEASE REMIT PAYMENT TO:", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackFontForAR, Rectangle.NO_BORDER));
	checkTable.addCell(makeCell(companyName, Element.ALIGN_LEFT, Element.ALIGN_TOP, blackFontForAR, Rectangle.NO_BORDER));
	checkTable.addCell(makeCell(companyAddress, Element.ALIGN_LEFT, Element.ALIGN_TOP, blackFontForAR, Rectangle.NO_BORDER));
	PdfPCell checkCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, headingFont3, Rectangle.BOX);
	checkCell.addElement(checkTable);
	paymentMethodTable.addCell(checkCell);
	//Empty cell
	emptyCell.setColspan(1);
	paymentMethodTable.addCell(emptyCell);
	//Wire payment
	PdfPTable wireTable = new PdfPTable(1);
	wireTable.setWidthPercentage(100);
	wireTable.addCell(makeCell("Electronic Funds Transfer Instructions", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont3, Rectangle.BOTTOM));
	wireTable.addCell(makeCell("If paying via wire transfer, please send to:",
		Element.ALIGN_LEFT, Element.ALIGN_TOP, blackFontForAR, Rectangle.NO_BORDER));
	wireTable.addCell(makeCell("Bank: " + eftBank, Element.ALIGN_LEFT, Element.ALIGN_TOP, blackFontForAR, Rectangle.NO_BORDER));
	wireTable.addCell(makeCell("Address: " + eftAddress, Element.ALIGN_LEFT, Element.ALIGN_TOP, blackFontForAR, Rectangle.NO_BORDER));
	wireTable.addCell(makeCell("ABA#:" + eftABANo, Element.ALIGN_LEFT, Element.ALIGN_TOP, blackFontForAR, Rectangle.NO_BORDER));
	wireTable.addCell(makeCell("Swift Code:" + eftSwiftCode, Element.ALIGN_LEFT, Element.ALIGN_TOP, blackFontForAR, Rectangle.NO_BORDER));
	wireTable.addCell(makeCell("Acct. Name: " + eftAcctName, Element.ALIGN_LEFT, Element.ALIGN_TOP, blackFontForAR, Rectangle.NO_BORDER));
	wireTable.addCell(makeCell("Account#:" + eftAccountNo, Element.ALIGN_LEFT, Element.ALIGN_TOP, blackFontForAR, Rectangle.NO_BORDER));
	PdfPCell wireCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, headingFont3, Rectangle.BOX);
	wireCell.addElement(wireTable);
	paymentMethodTable.addCell(wireCell);
	//Empty cell
	emptyCell.setColspan(1);
	paymentMethodTable.addCell(emptyCell);
	//Credit payment
	PdfPTable creditTable = new PdfPTable(1);
	creditTable.setWidthPercentage(100);
	creditTable.addCell(makeCell("Credit Card Payments", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont3, Rectangle.BOTTOM));
	creditTable.addCell(makeCell("If paying with Credit Card:", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackFontForAR, Rectangle.NO_BORDER));
	String creditCard = new SystemRulesDAO().getSystemRulesByCode("CreditCardWeb");
	Font urlFont = headingFontForBLURL;
	urlFont.setSize(6);
	Chunk url = new Chunk(creditCard, headingFontForBLURL);
	url.setUnderline(Color.blue, 0.1f, 0.1f, -1.1f, 0.1f, 3);
	PdfPCell urlCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackFontForAR, Rectangle.NO_BORDER);
	urlCell.addElement(url);
	creditTable.addCell(urlCell);
	PdfPCell creditCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, headingFont3, Rectangle.BOX);
	creditCell.addElement(creditTable);
	paymentMethodTable.addCell(creditCell);
	return paymentMethodTable;
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
	    cb.showText(companyName + " Account Contact: " + collector);
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
	    log.info("On End Page of ManualStatement failed :- ",e);

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

	    log.info("On Close ManualStatement failed :- ",e);
	}
    }

    public void doExit() {
	document.close();
    }

    public String createReport(String contextPath, List<AccountingBean> transactions,
	    CustomerBean customerDetails, Map<String, CustomerBean> agingBuckets, CustomerStatementForm customerStatementForm) {
	try {
	    StringBuilder fileName = new StringBuilder(LoadLogisoftProperties.getProperty("reportLocation"));
	    fileName.append("/Documents/CustomerStatement/");
            fileName.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
	    File file = new File(fileName.toString());
	    if (!file.exists()) {
		file.mkdirs();
	    }
	    if (customerStatementForm.isAllCustomers()) {
		fileName.append("AllCustomer");
	    } else if (CommonUtils.isEqualIgnoreCase(customerStatementForm.getAllcollectors(), AccountingConstants.YES)
		    || CommonUtils.isNotEmpty(customerStatementForm.getCollector())) {
		fileName.append("Collector");
	    } else {
		fileName.append(customerDetails.getCustomerNumber());
	    }
	    fileName.append(".pdf");
	    init(fileName.toString(), customerDetails, contextPath);
	    boolean writeCoverLetter = false;
	    if (customerStatementForm.isPrintCoverLetter()) {
		writeCoverLetter = true;
	    }
	    String subject = customerStatementForm.getSubject();
	    writeContents(transactions, customerDetails, agingBuckets, writeCoverLetter, subject);
	    doExit();
	    return fileName.toString();
	} catch (Exception e) {

	    return null;
	}
    }
}
