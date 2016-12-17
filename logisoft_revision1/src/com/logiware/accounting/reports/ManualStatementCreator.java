package com.logiware.accounting.reports;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.hibernate.dao.CustomerAccountingDAO;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.accounting.dao.ArReportsDAO;
import com.logiware.accounting.form.ArReportsForm;
import com.logiware.accounting.model.CompanyModel;
import com.logiware.accounting.model.ReportModel;
import com.logiware.bean.CustomerBean;
import com.logiware.bean.ReportBean;
import com.logiware.reports.BaseReportCreator;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
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
import org.apache.commons.lang3.StringUtils;

import org.apache.log4j.Logger;

/**
 *
 * @author Lakshmi Naryanan
 */
public class ManualStatementCreator extends BaseReportCreator {

    private static final Logger log = Logger.getLogger(ManualStatementCreator.class);
    private ArReportsForm arReportsForm;
    private CustomerBean customer;
    private CompanyModel company;
    float availableHeight = 0;
    float adjustedHeight = 0;

    public ManualStatementCreator() {
    }

    public ManualStatementCreator(ArReportsForm arReportsForm, CustomerBean customer, CompanyModel company, String contextPath) {
        this.arReportsForm = arReportsForm;
        this.customer = customer;
        this.company = company;
        this.contextPath = contextPath;
    }

    private void init(String fileName) throws DocumentException, FileNotFoundException {
        document = new Document(PageSize.A4);
        document.setMargins(10, 10, 20, 60);
        writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
        writer.setPdfVersion(PdfWriter.PDF_VERSION_1_7);
        writer.setUserunit(1f);
        writer.setPageEvent(new ManualStatementCreator(arReportsForm, customer, company, contextPath));
        document.open();
        writer.setOpenAction(PdfAction.gotoLocalPage(1, new PdfDestination(PdfDestination.XYZ, -1, -1, 0.75f), writer));
        Rectangle page = document.getPageSize();
        availableHeight = page.getHeight() - document.topMargin() - document.bottomMargin();
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

    private void writeHeader() throws Exception {
        headerTable = new PdfPTable(1);
        headerTable.setWidthPercentage(100);
        String imagePath = LoadLogisoftProperties.getProperty("application.image.dual.logo");
        Image image = Image.getInstance(contextPath + imagePath);
        image.scalePercent(75);
        headerTable.addCell(createImageCell(image));
        //Company Name in header
        headerTable.addCell(createCell(company.getName(), headerBoldFont10, Element.ALIGN_CENTER, Rectangle.NO_BORDER));
        //Company address in header
        StringBuilder address = new StringBuilder();
        address.append(company.getAddress()).append(".");
        address.append(" PHONE: ").append(company.getPhone()).append(".");
        address.append(" FAX: ").append(company.getFax());
        headerTable.addCell(createCell(address.toString(), blackNormalFont9, Element.ALIGN_CENTER, Rectangle.NO_BORDER));
        //Report title in header
        headerTable.addCell(createCell("STATEMENT", headerBoldFont16, Element.ALIGN_CENTER, Rectangle.BOX, Color.LIGHT_GRAY));
        //Two empty lines
        headerTable.addCell(createEmptyCell(Rectangle.NO_BORDER));
        headerTable.addCell(createEmptyCell(Rectangle.NO_BORDER));
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

    private void writeCoverLetter(ReportBean arBuckets) throws DocumentException, Exception {
        PdfPTable coverLetterTable = new PdfPTable(1);
        coverLetterTable.setWidthPercentage(100);

        //Cover Letter top table
        PdfPTable coverLetterTopTable = new PdfPTable(3);
        coverLetterTopTable.setWidthPercentage(100);
        coverLetterTopTable.setWidths(new float[]{48, 2, 48});

        //Empty cell
        PdfPCell emptyCell = createEmptyCell(Rectangle.NO_BORDER);
        //Line cell
        PdfPCell lineCell = createEmptyCell(Rectangle.BOTTOM);

        //Company details in cover letter
        StringBuilder companyDetails = new StringBuilder();
        companyDetails.append(company.getAddress());
        companyDetails.append("\nPhone: ").append(company.getPhone());
        companyDetails.append("\nFax: ").append(company.getFax());
        String email = (null != customer && CommonUtils.isNotEmpty(customer.getCollectorEmail()))
                ? customer.getCollectorEmail() : "payments@econocaribe.com";
        companyDetails.append("\nEmail: ").append(email);
        PdfPTable companyDetailsTable = new PdfPTable(1);
        companyDetailsTable.setWidthPercentage(100);
        companyDetailsTable.addCell(createCell(company.getName(), blackNormalFont8, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        companyDetailsTable.addCell(createCell(companyDetails.toString(), blackNormalFont8, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        PdfPCell companyDetailsCell = createEmptyCell(Rectangle.BOX);
        companyDetailsCell.addElement(companyDetailsTable);
        //Add company details table to cover letter
        coverLetterTopTable.addCell(companyDetailsCell);

        //Empty cell to differentiate company and customer boxes
        coverLetterTopTable.addCell(createEmptyCell(Rectangle.NO_BORDER));

        //Customer details in cover letter
        PdfPTable customerDetailsTable = new PdfPTable(1);
        customerDetailsTable.setWidthPercentage(100);
        customerDetailsTable.addCell(createCell(null != customer && CommonUtils.isNotEmpty(customer.getCustomerName())
                ? customer.getCustomerName() : "", blackNormalFont8, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        customerDetailsTable.addCell(createCell(null != customer && CommonUtils.isNotEmpty(customer.getAddress())
                ? customer.getAddress() : "", blackNormalFont8, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        PdfPCell customerDetailsCell = createEmptyCell(Rectangle.BOX);
        customerDetailsCell.addElement(customerDetailsTable);
        //Add customer details table to cover letter
        coverLetterTopTable.addCell(customerDetailsCell);

        //Add empty row in cover letter
        emptyCell.setColspan(3);
        coverLetterTopTable.addCell(emptyCell);

        //Add Statement date in cover letter
        String statementDate = "Statement Date : " + DateUtils.formatDate(new Date(), "MM/dd/yyyy");
        coverLetterTopTable.addCell(createCell(statementDate, blackBoldFont9, Element.ALIGN_LEFT, Rectangle.NO_BORDER));

        //Empty cell
        coverLetterTopTable.addCell(createEmptyCell(Rectangle.NO_BORDER));

        //Add Customer number in cover letter
        String custAcctNo = null != customer && CommonUtils.isNotEmpty(customer.getCustomerNumber()) ? customer.getCustomerNumber() : "";
        String customerNumber = "Account# : " + custAcctNo;
        coverLetterTopTable.addCell(createCell(customerNumber, blackBoldFont9, Element.ALIGN_LEFT, Rectangle.NO_BORDER));

        //Add cover letter top into cover letter
        PdfPCell coverLetterTopCell = createEmptyCell(Rectangle.NO_BORDER);
        coverLetterTopCell.addElement(coverLetterTopTable);
        coverLetterTable.addCell(coverLetterTopCell);

        //Add a line in cover letter
        lineCell.setColspan(1);
        coverLetterTable.addCell(lineCell);

        //Add a empty row in cover letter
        emptyCell.setColspan(1);
        coverLetterTable.addCell(emptyCell);

        //Add Account summary in cover letter
        PdfPCell accountSummaryCell = createCell("ACCOUNT SUMMARY", headerGreenBoldFont12, Element.ALIGN_LEFT, Rectangle.NO_BORDER);
        coverLetterTable.addCell(accountSummaryCell);

        //Add empty row in cover letter
        emptyCell.setColspan(1);
        coverLetterTable.addCell(emptyCell);

        //Add aging buckets in cover letter
        PdfPTable agingBucketsTable = new PdfPTable(5);
        agingBucketsTable.setWidthPercentage(100);
        agingBucketsTable.setWidths(new float[]{20, 20, 20, 20, 20});
        //Aging header in cover letter
        agingBucketsTable.addCell(createCell("CURRENT", headerBoldFont10, Element.ALIGN_CENTER, Rectangle.BOX));
        agingBucketsTable.addCell(createCell("31-60 DAYS", headerBoldFont10, Element.ALIGN_CENTER, Rectangle.BOX));
        agingBucketsTable.addCell(createCell("61-90 DAYS", headerBoldFont10, Element.ALIGN_CENTER, Rectangle.BOX));
        agingBucketsTable.addCell(createCell(">90 DAYS", headerBoldFont10, Element.ALIGN_CENTER, Rectangle.BOX));
        agingBucketsTable.addCell(createCell("TOTAL\nBALANCE", headerBoldFont10, Element.ALIGN_CENTER, Rectangle.BOX));
        //Aging content in cover letter
        agingBucketsTable.addCell(createAmountCell(arBuckets.getAge30Days(), blackNormalFont9, redNormalFont9, Element.ALIGN_CENTER, Rectangle.BOX));
        agingBucketsTable.addCell(createAmountCell(arBuckets.getAge60Days(), blackNormalFont9, redNormalFont9, Element.ALIGN_CENTER, Rectangle.BOX));
        agingBucketsTable.addCell(createAmountCell(arBuckets.getAge90Days(), blackNormalFont9, redNormalFont9, Element.ALIGN_CENTER, Rectangle.BOX));
        agingBucketsTable.addCell(createAmountCell(arBuckets.getAge91Days(), blackNormalFont9, redNormalFont9, Element.ALIGN_CENTER, Rectangle.BOX));
        agingBucketsTable.addCell(createAmountCell(arBuckets.getAgeTotal(), blackNormalFont9, redNormalFont9, Element.ALIGN_CENTER, Rectangle.BOX));
        //Add customer details table into cover letter
        PdfPCell agingBucketsCell = createEmptyCell(Rectangle.NO_BORDER);
        agingBucketsCell.addElement(agingBucketsTable);
        coverLetterTable.addCell(agingBucketsCell);

        //Add two empty rows in cover letter
        emptyCell.setColspan(1);
        coverLetterTable.addCell(emptyCell);
        coverLetterTable.addCell(emptyCell);

        //Message board in cover letter
        PdfPTable messageBoardTable = new PdfPTable(1);
        messageBoardTable.setWidthPercentage(100);
        String message = CommonUtils.isNotEmpty(arReportsForm.getMessage()) ? arReportsForm.getMessage() : "" + "\n\n";
        messageBoardTable.addCell(createCell("Message Board:   " + message, blackNormalFont10, Element.ALIGN_LEFT, Rectangle.BOX));
        //Add message board table into cover letter
        PdfPCell messageBoardCell = createEmptyCell(Rectangle.NO_BORDER);
        messageBoardCell.addElement(messageBoardTable);
        coverLetterTable.addCell(messageBoardCell);

        //Add two empty rows in cover letter
        emptyCell.setColspan(1);
        coverLetterTable.addCell(emptyCell);
        coverLetterTable.addCell(emptyCell);

        //Comments header in cover letter
        String commentHeader = "-----------------------------------------------------------------------------"
                + "(Cut Here if attaching comments)------------------------------------------------------------------";
        PdfPCell commentHeaderCell = createCell(commentHeader, blackBoldFont9, Element.ALIGN_CENTER, Rectangle.NO_BORDER);
        coverLetterTable.addCell(commentHeaderCell);
        coverLetterTable.addCell(emptyCell);

        //Comments in cover letter
        PdfPTable commentsTable = new PdfPTable(3);
        commentsTable.setWidthPercentage(100);
        commentsTable.setWidths(new float[]{45, 5, 45});
        //Comments content in cover letter
        PdfPTable commentContentTable = new PdfPTable(1);
        commentContentTable.setWidthPercentage(90);
        commentContentTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        commentContentTable.addCell(createCell("Comments (Plese enter your Comments here)", blackNormalFont9, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        PdfPCell commentEmptyCell = createEmptyCell(Rectangle.BOTTOM);
        commentEmptyCell.setPadding(9f);
        commentContentTable.addCell(commentEmptyCell);
        commentContentTable.addCell(commentEmptyCell);
        commentContentTable.addCell(commentEmptyCell);
        commentContentTable.addCell(commentEmptyCell);
        PdfPCell commentContentCell = createEmptyCell(Rectangle.BOX);
        commentContentCell.addElement(commentContentTable);
        commentsTable.addCell(commentContentCell);

        //Empty cell
        commentsTable.addCell(createEmptyCell(Rectangle.NO_BORDER));

        //Wire transfer instruction in cover letter
        PdfPTable wireTransferTable = new PdfPTable(1);
        wireTransferTable.setWidthPercentage(90);
        wireTransferTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        wireTransferTable.addCell(createCell("Wire Transfer Instructions", blackNormalFont9, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        StringBuilder wireInstruction = new StringBuilder();
        wireInstruction.append("If paying via wire transfer, please send to:\n\n");
        wireInstruction.append("Bank: ").append(company.getBankName());
        wireInstruction.append("\nAddress: ").append(company.getBankAddress());
        wireInstruction.append("\nABA#: ").append(company.getBankAbaNumber());
        wireInstruction.append("\nSwift Code: ").append(company.getBankSwiftCode());
        wireInstruction.append("\nAccount Name: ").append(company.getBankAccountName());
        wireInstruction.append("\nAccount#:").append(company.getBankAccountNumber());
        wireTransferTable.addCell(createCell(wireInstruction.toString(), blackNormalFont9, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        PdfPCell wireTransferCell = createEmptyCell(Rectangle.BOX);
        wireTransferCell.addElement(wireTransferTable);
        commentsTable.addCell(wireTransferCell);
        emptyCell.setColspan(3);
        commentsTable.addCell(emptyCell);

        //Add comments table into cover letter
        PdfPCell commentsCell = createEmptyCell(Rectangle.NO_BORDER);
        commentsCell.addElement(commentsTable);
        coverLetterTable.addCell(commentsCell);

        //Comment footer in cover letter
        String commentFooter = "---------------------------------------------------------------------------------------"
                + "(Cut or fold Here)------------------------------------------------------------------------------";
        PdfPCell commentFooterCell = createCell(commentFooter, blackBoldFont9, Element.ALIGN_CENTER, Rectangle.NO_BORDER);
        coverLetterTable.addCell(commentFooterCell);

        //Add empty row in cover letter
        emptyCell.setColspan(1);
        coverLetterTable.addCell(emptyCell);

        //Payment coupon in cover letter
        PdfPTable paymentCouponTable = new PdfPTable(3);
        paymentCouponTable.setWidthPercentage(100);
        paymentCouponTable.setWidths(new float[]{45, 10, 45});

        //Payment coupon header in cover letter
        String paymentCouponHeader = "Payment Coupon \t  (Please include coupon with payment details along with your check)";
        PdfPCell paymentCouponHeaderCell = createCell(paymentCouponHeader, blackTimesBoldFont11, Element.ALIGN_LEFT, Rectangle.NO_BORDER);
        paymentCouponHeaderCell.setColspan(3);
        paymentCouponTable.addCell(paymentCouponHeaderCell);
        emptyCell.setColspan(3);
        paymentCouponTable.addCell(emptyCell);

        //Payment coupon detail1 in cover letter
        PdfPTable paymentCouponDetails1Table = new PdfPTable(1);
        paymentCouponDetails1Table.setWidthPercentage(100);

        //Add statement date into payment coupon details1 table
        paymentCouponDetails1Table.addCell(createCell(statementDate, blackNormalFont9, Element.ALIGN_LEFT, Rectangle.BOTTOM));

        //Add empty cell into payment coupon details1 table
        emptyCell.setColspan(1);
        paymentCouponDetails1Table.addCell(emptyCell);

        //Add account number into payment coupon details1 table
        String accountNumber = "Account number : " + custAcctNo;
        paymentCouponDetails1Table.addCell(createCell(accountNumber, blackNormalFont9, Element.ALIGN_LEFT, Rectangle.BOTTOM));

        //Add empty cell into payment coupon details1 table
        emptyCell.setColspan(1);
        paymentCouponDetails1Table.addCell(emptyCell);

        //Add total balace into payment coupon details1 table
        String totalBalance = "Total Balance : " + arBuckets.getAgeTotal();
        paymentCouponDetails1Table.addCell(createCell(totalBalance, blackNormalFont9, Element.ALIGN_LEFT, Rectangle.BOTTOM));

        //Add empty cell into payment coupon details1 table
        emptyCell.setColspan(1);
        paymentCouponDetails1Table.addCell(emptyCell);

        //Add customer details into payment coupon details1 table
        paymentCouponDetails1Table.addCell(createCell(null != customer && CommonUtils.isNotEmpty(customer.getCustomerName()) ? customer.getCustomerName() : "", blackBoldFont9, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        paymentCouponDetails1Table.addCell(createCell(null != customer && CommonUtils.isNotEmpty(customer.getAddress()) ? customer.getAddress() : "", blackBoldFont9, Element.ALIGN_LEFT, Rectangle.NO_BORDER));

        //Add payment coupon details1 into payment coupon table
        PdfPCell paymentCouponDetails1Cell = createEmptyCell(Rectangle.NO_BORDER);
        paymentCouponDetails1Cell.addElement(paymentCouponDetails1Table);
        paymentCouponTable.addCell(paymentCouponDetails1Cell);

        //Add Empty cell into payment coupon table
        paymentCouponTable.addCell(createEmptyCell(Rectangle.NO_BORDER));

        //Payment coupon detail2 in cover letter
        PdfPTable paymentCouponDetails2Table = new PdfPTable(1);
        paymentCouponDetails2Table.setWidthPercentage(100);

        //Add amount enclosed into payment coupon details2 table
        String amountEnclosed = "Amount Enclosed: $______________________\n ";
        paymentCouponDetails2Table.addCell(createCell(amountEnclosed, blackNormalFont9, Element.ALIGN_LEFT, Rectangle.BOX));

        //Add Empty cell into payment coupon details 2 table
        emptyCell.setColspan(1);
        paymentCouponDetails2Table.addCell(emptyCell);

        //Add change of address mark into payment coupon details2 table
        String changeOfAddressMark = "Please mark box to indicate change of address \n(please attach new address)\n ";
        paymentCouponDetails2Table.addCell(createCell(changeOfAddressMark, blackNormalFont9, Element.ALIGN_LEFT, Rectangle.BOX));

        //Add Empty cell into payment coupon details2 table
        emptyCell.setColspan(1);
        paymentCouponDetails2Table.addCell(emptyCell);

        //Remit details in cover letter
        PdfPTable remitTable = new PdfPTable(1);
        remitTable.setWidthPercentage(100);
        remitTable.addCell(createCell("PLEASE REMIT PAYMENT TO :", blackNormalFont9, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        remitTable.addCell(createCell(company.getName(), blackBoldFont9, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        remitTable.addCell(createCell(companyDetails.toString(), blackBoldFont9, Element.ALIGN_LEFT, Rectangle.NO_BORDER));

        //Add remit table into payment coupon details2 table
        PdfPCell remitCell = createEmptyCell(Rectangle.BOX);
        remitCell.addElement(remitTable);
        paymentCouponDetails2Table.addCell(remitCell);

        //Add payment coupon details2 into payment coupon table
        PdfPCell paymentCouponDetails2Cell = createEmptyCell(Rectangle.NO_BORDER);
        paymentCouponDetails2Cell.addElement(paymentCouponDetails2Table);
        paymentCouponTable.addCell(paymentCouponDetails2Cell);

        //Add payment coupon table into cover letter
        PdfPCell paymentCouponCell = createEmptyCell(Rectangle.NO_BORDER);
        paymentCouponCell.addElement(paymentCouponTable);
        coverLetterTable.addCell(paymentCouponCell);

        //Add cover letter into document
        document.add(coverLetterTable);

        //Add new page
        document.newPage();
    }

    private void writeSummary(ReportBean arBuckets, ReportBean apBuckets) throws DocumentException {
        PdfPTable summaryTable = new PdfPTable(3);
        summaryTable.setWidthPercentage(100);
        summaryTable.setWidths(new float[]{40, 5, 55});
        Rectangle page = document.getPageSize();
        summaryTable.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());
        if (null != customer && CommonUtils.isNotEmpty(customer.getCustomerNumber())) {
            //Add customer details into summary table
            PdfPTable customerTable = new PdfPTable(1);
            customerTable.setWidthPercentage(100);
            customerTable.addCell(createTextCell(customer.getCustomerName(), blackBoldFont9, Rectangle.NO_BORDER));
            customerTable.addCell(createTextCell(customer.getAddress(), blackNormalFont9, Rectangle.NO_BORDER));
            PdfPTable customerNumberTable = new PdfPTable(2);
            customerNumberTable.setWidthPercentage(100);
            customerNumberTable.setWidths(new float[]{20, 70});
            customerNumberTable.addCell(createTextCell("Account#: ", blackBoldFont9, Rectangle.NO_BORDER));
            customerNumberTable.addCell(createTextCell(customer.getCustomerNumber(), blackNormalFont9, Rectangle.NO_BORDER));
            PdfPCell customerNumberCell = createEmptyCell(Rectangle.NO_BORDER);
            customerNumberCell.addElement(customerNumberTable);
            customerTable.addCell(customerNumberCell);
            PdfPCell customerCell = createEmptyCell(Rectangle.BOX);
            customerCell.addElement(customerTable);
            summaryTable.addCell(customerCell);
        } else {
            //Add empty cell into summary table in case statement is not created for individual customer
            summaryTable.addCell(createEmptyCell(Rectangle.NO_BORDER));
        }

        //Add empty cell into differentiate customer details and aging buckets boxes
        summaryTable.addCell(createEmptyCell(Rectangle.NO_BORDER));

        //Aging Buckets
        PdfPTable agingBucketsTable = new PdfPTable(2);
        agingBucketsTable.setWidthPercentage(100);
        agingBucketsTable.setTotalWidth(new float[]{50, 50});
        if (null != apBuckets) {
            // Ap Aging buckets in aging summary
            PdfPTable apAgingTable = new PdfPTable(2);
            apAgingTable.setWidthPercentage(100);
            apAgingTable.setTotalWidth(new float[]{50, 50});
            apAgingTable.addCell(createTextCell("AP SUMMARY", blackBoldFont9, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
            apAgingTable.addCell(createTextCell("AMOUNT", blackBoldFont9, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
            apAgingTable.addCell(createTextCell("CURRENT", blackBoldFont9, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
            apAgingTable.addCell(createAmountCell(apBuckets.getAge30Days(), blackNormalFont9, redNormalFont9, Rectangle.NO_BORDER));
            apAgingTable.addCell(createTextCell("31-60 DAYS", blackBoldFont9, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
            apAgingTable.addCell(createAmountCell(apBuckets.getAge60Days(), blackNormalFont9, redNormalFont9, Rectangle.NO_BORDER));
            apAgingTable.addCell(createTextCell("61-90 DAYS", blackBoldFont9, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
            apAgingTable.addCell(createAmountCell(apBuckets.getAge90Days(), blackNormalFont9, redNormalFont9, Rectangle.NO_BORDER));
            apAgingTable.addCell(createTextCell(">90 DAYS", blackBoldFont9, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
            apAgingTable.addCell(createAmountCell(apBuckets.getAge91Days(), blackNormalFont9, redNormalFont9, Rectangle.NO_BORDER));
            apAgingTable.addCell(createTextCell("TOTAL", blackBoldFont9, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
            apAgingTable.addCell(createAmountCell(apBuckets.getAgeTotal(), blackNormalFont9, redNormalFont9, Rectangle.NO_BORDER));
            //Add Ap Aging table into aging buckets table
            PdfPCell apAgingCell = createEmptyCell(Rectangle.NO_BORDER);
            apAgingCell.addElement(apAgingTable);
            agingBucketsTable.addCell(apAgingCell);
        }
        if (null != arBuckets) {
            //Ar Aging buckets in aging summary
            PdfPTable arAgingTable = new PdfPTable(2);
            arAgingTable.setWidthPercentage(100);
            arAgingTable.setTotalWidth(new float[]{50, 50});
            arAgingTable.addCell(createTextCell("AR SUMMARY", blackBoldFont9, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
            arAgingTable.addCell(createTextCell("AMOUNT", blackBoldFont9, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
            arAgingTable.addCell(createTextCell("CURRENT", blackBoldFont9, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
            arAgingTable.addCell(createAmountCell(arBuckets.getAge30Days(), blackNormalFont9, redNormalFont9, Rectangle.NO_BORDER));
            arAgingTable.addCell(createTextCell("31-60 DAYS", blackBoldFont9, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
            arAgingTable.addCell(createAmountCell(arBuckets.getAge60Days(), blackNormalFont9, redNormalFont9, Rectangle.NO_BORDER));
            arAgingTable.addCell(createTextCell("61-90 DAYS", blackBoldFont9, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
            arAgingTable.addCell(createAmountCell(arBuckets.getAge90Days(), blackNormalFont9, redNormalFont9, Rectangle.NO_BORDER));
            arAgingTable.addCell(createTextCell(">90 DAYS", blackBoldFont9, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
            arAgingTable.addCell(createAmountCell(arBuckets.getAge91Days(), blackNormalFont9, redNormalFont9, Rectangle.NO_BORDER));
            arAgingTable.addCell(createTextCell("TOTAL", blackBoldFont9, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
            arAgingTable.addCell(createAmountCell(arBuckets.getAgeTotal(), blackNormalFont9, redNormalFont9, Rectangle.NO_BORDER));
            if (null != apBuckets) {
                double apBalance = Double.parseDouble(apBuckets.getApBalance().replaceAll("[^\\d.]", ""));
                double acBalance = Double.parseDouble(apBuckets.getAcBalance().replaceAll("[^\\d.]", ""));
                double arBalance = Double.parseDouble(arBuckets.getAgeTotal().replaceAll("[^\\d.]", ""));

                //Add Ar - Ap in aging buckets
                arAgingTable.addCell(createTextCell("AR-AP", blackBoldFont9, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
                String netAmt1 = NumberUtils.formatNumber(arBalance - apBalance);
                String arMinusAp = "$" + (StringUtils.contains(netAmt1, "-") ? ("(" + netAmt1.replace("-", "") + ")") : netAmt1);
                arAgingTable.addCell(createAmountCell(arMinusAp, blackNormalFont9, redNormalFont9, Rectangle.NO_BORDER));

                //Add Ar-Ap-Ac in aging buckets
                arAgingTable.addCell(createTextCell("AR-AP-AC", blackBoldFont9, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
                String netAmt2 = NumberUtils.formatNumber(arBalance - apBalance - acBalance);
                String arMinusApMinusAc = "$" + (StringUtils.contains(netAmt2, "-") ? ("(" + netAmt2.replace("-", "") + ")") : netAmt2);
                arAgingTable.addCell(createAmountCell(arMinusApMinusAc, blackNormalFont9, redNormalFont9, Rectangle.NO_BORDER));
            }
            //Add Ar Aging table into aging buckets
            PdfPCell arAgingCell = createEmptyCell(Rectangle.NO_BORDER);
            arAgingCell.addElement(arAgingTable);
            agingBucketsTable.addCell(arAgingCell);
            if (null == apBuckets) {
                //If not Ap Aging, then add an empty cell into aging buckets
                agingBucketsTable.addCell(createEmptyCell(Rectangle.NO_BORDER));
            }
        }
        //Add Aging buckets into summary table
        PdfPCell agingCell = createEmptyCell(Rectangle.BOX);
        agingCell.addElement(agingBucketsTable);
        summaryTable.addCell(agingCell);

        summaryTable.addCell(createEmptyCell(Rectangle.NO_BORDER));
        summaryTable.addCell(createEmptyCell(Rectangle.NO_BORDER));
        summaryTable.addCell(createEmptyCell(Rectangle.NO_BORDER));
        float summaryHeight = 0;
        for (int rowIndex = 0; rowIndex < summaryTable.getRows().size(); rowIndex++) {
            summaryHeight += summaryTable.getRowHeight(rowIndex);
        }
        availableHeight -= summaryHeight;
        adjustedHeight += summaryHeight;
        //Add summary table into document
        document.add(summaryTable);
    }

    private PdfPTable getPaymentMethodTable() throws DocumentException {
        PdfPCell emptyCell = createEmptyCell(Rectangle.NO_BORDER);

        //Payment methods
        PdfPTable paymentMethodTable = new PdfPTable(5);
        paymentMethodTable.setWidthPercentage(100);
        paymentMethodTable.setWidths(new float[]{30f, 1f, 32.5f, 1f, 35.5f});
        if(company.getName().contains("ECONOCARIBE")){
        Paragraph invoiceEle = new Paragraph();
        Chunk invComments = new Chunk("Please request invoice copies from", headerBoldFont10);
        Chunk invoiceEmail = new Chunk(" invoicereq@docs.ecuworldwide.us", headerBoldFontBlue);
        invComments.setBackground(Color.yellow);
        invoiceEmail.setBackground(Color.yellow);
        invoiceEle.add(invComments);
        invoiceEle.add(invoiceEmail);
        PdfPCell paymentFirstHeaderCell = createCell("", headerBoldFont10, Element.ALIGN_CENTER, Rectangle.NO_BORDER);
        invoiceEle.setAlignment(Element.ALIGN_CENTER);
        paymentFirstHeaderCell.addElement(invoiceEle);
        paymentFirstHeaderCell.setColspan(5);
        paymentMethodTable.addCell(paymentFirstHeaderCell);
        }

        //Empty cell
        emptyCell.setColspan(5);
        paymentMethodTable.addCell(emptyCell);

        //Add Payment Method Header into payment table
        PdfPCell paymentHeaderCell = createCell("PAYMENT METHODS", headerGreenBoldFont12, Element.ALIGN_LEFT, Rectangle.BOTTOM);
        paymentHeaderCell.setColspan(5);
        paymentMethodTable.addCell(paymentHeaderCell);

        //Add Empty line into payment table
        emptyCell.setColspan(5);
        paymentMethodTable.addCell(emptyCell);

        //Check payment method
        PdfPTable checkTable = new PdfPTable(1);
        checkTable.setWidthPercentage(100);
        checkTable.addCell(createCell("Payment By Check", blackNormalFont10, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        checkTable.addCell(createCell("PLEASE REMIT PAYMENT TO:", blackNormalFont8, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        checkTable.addCell(createCell(company.getName(), blackNormalFont8, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        checkTable.addCell(createCell(company.getAddress(), blackNormalFont8, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        //Add Check payment method table into payment method table
        PdfPCell checkCell = createEmptyCell(Rectangle.BOX);
        checkCell.addElement(checkTable);
        paymentMethodTable.addCell(checkCell);
        //Add Empty cell into payment method table
        emptyCell.setColspan(1);
        paymentMethodTable.addCell(emptyCell);

        //Wire payment method
        PdfPTable wireTable = new PdfPTable(1);
        wireTable.setWidthPercentage(100);
        wireTable.addCell(createCell("Electronic Funds Transfer Instructions", blackNormalFont10, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        wireTable.addCell(createCell("If paying via wire transfer, please send to:", blackNormalFont8, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        wireTable.addCell(createCell("Bank: " + company.getBankName(), blackNormalFont8, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        wireTable.addCell(createCell("Address: " + company.getBankAddress(), blackNormalFont8, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        wireTable.addCell(createCell("ABA#: " + company.getBankAbaNumber(), blackNormalFont8, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        wireTable.addCell(createCell("Swift Code: " + company.getBankSwiftCode(), blackNormalFont8, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        wireTable.addCell(createCell("Acct. Name: " + company.getBankAccountName(), blackNormalFont8, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        wireTable.addCell(createCell("Account#: " + company.getBankAccountNumber(), blackNormalFont8, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        //Add Wire payment method table into payment method table
        PdfPCell wireCell = createEmptyCell(Rectangle.BOX);
        wireCell.addElement(wireTable);
        paymentMethodTable.addCell(wireCell);
        //Add empty cell into payment method table
        emptyCell.setColspan(1);
        paymentMethodTable.addCell(emptyCell);

        //Credit payment
        PdfPTable creditTable = new PdfPTable(1);
        creditTable.setWidthPercentage(100);
        creditTable.addCell(createCell("Credit Card Payments", blackNormalFont10, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        creditTable.addCell(createCell("If paying with Credit Card:", blackNormalFont8, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        //Add credit card url into credit card table
        Chunk url = new Chunk(company.getBankCreditCard(), blueNormalFont6);
        url.setUnderline(Color.BLUE, 0.1f, 0.1f, -1.1f, 0.1f, 3);
        PdfPCell urlCell = createEmptyCell(Rectangle.NO_BORDER);
        urlCell.addElement(url);
        creditTable.addCell(urlCell);
        //Add Wire payment method table into payment method table
        PdfPCell creditCell = createEmptyCell(Rectangle.BOX);
        creditCell.addElement(creditTable);
        paymentMethodTable.addCell(creditCell);
        return paymentMethodTable;
    }

    private void writeContents() throws DocumentException, Exception {
        ReportModel statementModel = new ArReportsDAO().getStatementModel(false, arReportsForm);
        ReportBean arBuckets = statementModel.getArBuckets();
        ReportBean apBuckets = statementModel.getApBuckets();
        List<ReportBean> transactions = statementModel.getTransactions();
        if (arReportsForm.isCoverLetter()) {
            writeCoverLetter(arBuckets);
        }
        //Add summary of customer details and aging buckets into document
        writeSummary(arBuckets, apBuckets);
        //Add Statement date into document
        PdfPTable statementTable = new PdfPTable(2);
        statementTable.setWidthPercentage(100);
        statementTable.setTotalWidth(new float[]{50, 50});
        Rectangle page = document.getPageSize();
        statementTable.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());
        String statementDate = "Statement Date : " + DateUtils.formatDate(new Date(), "MM/dd/yyyy");
        // add credit status
        String creditStatus = "";
        statementTable.addCell(createTextCell(statementDate, blackNormalFont10, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        if (null != customer && CommonUtils.isNotEmpty(customer.getCustomerNumber())) {
            creditStatus = new CustomerAccountingDAO().getCreditStatus(customer.getCustomerNumber());
            creditStatus = CommonUtils.isEqualIgnoreCase(creditStatus, "Credit Hold") ? "ACCOUNT PAST DUE"
                    : CommonUtils.in(creditStatus, "No Credit", "Suspended/See Accounting", "Legal/See Accounting")
                            ? "DUE UPON RECEIPT" : "";
        }
        statementTable.addCell(createTextCell(creditStatus, headingFontRed, Element.ALIGN_RIGHT, Rectangle.BOTTOM));
        float statementHeight = 0;
        for (int rowIndex = 0; rowIndex < statementTable.getRows().size(); rowIndex++) {
            statementHeight += statementTable.getRowHeight(rowIndex);
        }
        availableHeight -= statementHeight;
        adjustedHeight += statementHeight;
        document.add(statementTable);

        //Add Account detail header into document
        PdfPTable accountDetailTable = new PdfPTable(1);
        accountDetailTable.setWidthPercentage(100);
        accountDetailTable.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());
        accountDetailTable.addCell(createCell("ACCOUNT DETAIL", headerGreenBoldFont12, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        float accountDetailHeight = 0;
        for (int rowIndex = 0; rowIndex < accountDetailTable.getRows().size(); rowIndex++) {
            accountDetailHeight += accountDetailTable.getRowHeight(rowIndex);
        }
        availableHeight -= accountDetailHeight;
        adjustedHeight += accountDetailHeight;
        document.add(accountDetailTable);

        boolean isAgent = false;
        PdfPTable contentHeaderTable = null;
        if (null != customer && ((StringUtils.contains(customer.getAccountType(), "V")
                && (CommonUtils.isEqual(customer.getSubType(), "Export Agent") || CommonUtils.isEqual(customer.getSubType(), "Import Agent")))
                || StringUtils.contains(customer.getAccountType(), "E") || StringUtils.contains(customer.getAccountType(), "I"))) {
            isAgent = true;
            contentHeaderTable = new PdfPTable(9);
            contentHeaderTable.setWidths(new float[]{8, 14, 9, 18, 10, 11, 10, 11, 9});
            contentTable = new PdfPTable(9);
            contentTable.setWidths(new float[]{8, 14, 9, 18, 10, 11, 10, 11, 9});
        } else {
            contentHeaderTable = new PdfPTable(7);
            contentHeaderTable.setWidths(new float[]{8, 17, 17, 17, 10, 11, 12});
            contentTable = new PdfPTable(7);
            contentTable.setWidths(new float[]{8, 17, 17, 17, 10, 11, 12});
        }
        contentHeaderTable.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());
        contentHeaderTable.setWidthPercentage(100);
        contentTable.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());
        contentTable.setWidthPercentage(100);
        contentHeaderTable.addCell(createTextCell("Date", blackBoldFont9, Element.ALIGN_CENTER, Rectangle.BOX));
        contentHeaderTable.addCell(createTextCell("Invoice/BL", blackBoldFont9, Element.ALIGN_CENTER, Rectangle.BOX));
        contentHeaderTable.addCell(createTextCell("Voyage# / Booking#", blackBoldFont9, Element.ALIGN_CENTER, Rectangle.BOX));
        contentHeaderTable.addCell(createTextCell("Customer Reference", blackBoldFont9, Element.ALIGN_CENTER, Rectangle.BOX));
        contentHeaderTable.addCell(createTextCell("Invoice Amount", blackBoldFont9, Element.ALIGN_CENTER, Rectangle.BOX));
        contentHeaderTable.addCell(createTextCell("Payment/ Adjustment", blackBoldFont9, Element.ALIGN_CENTER, Rectangle.BOX));
        contentHeaderTable.addCell(createTextCell("Balance", blackBoldFont9, Element.ALIGN_CENTER, Rectangle.BOX));
        if (isAgent) {
            contentHeaderTable.addCell(createTextCell("Consignee", blackBoldFont9, Element.ALIGN_CENTER, Rectangle.BOX));
            contentHeaderTable.addCell(createTextCell("Voyage", blackBoldFont9, Element.ALIGN_CENTER, Rectangle.BOX));
        }
        if (CommonUtils.isNotEmpty(transactions)) {
            for (ReportBean transaction : transactions) {
                contentTable.addCell(createTextCell(transaction.getInvoiceDate(), blackNormalFont7, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
                contentTable.addCell(createTextCell(transaction.getInvoiceOrBl(), blackNormalFont7, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
                PdfPCell bookingCell;
                if (CommonUtils.isNotEmpty(transaction.getVoyage())) {
                    bookingCell = createTextCell(transaction.getVoyage(), blackNormalFont7, Element.ALIGN_LEFT, Rectangle.NO_BORDER);
                } else {
                    bookingCell = createTextCell(transaction.getBookingNumber(), blackNormalFont7, Element.ALIGN_LEFT, Rectangle.NO_BORDER);
                }
                bookingCell.setNoWrap(true);
                contentTable.addCell(bookingCell);
                PdfPCell referenceCell = createTextCell(transaction.getCustomerReference(), blackNormalFont7, Element.ALIGN_LEFT, Rectangle.NO_BORDER);
                referenceCell.setNoWrap(true);
                contentTable.addCell(referenceCell);
                contentTable.addCell(createAmountCell(transaction.getInvoiceAmount(), blackNormalFont7, redNormalFont7, Rectangle.NO_BORDER));
                contentTable.addCell(createAmountCell(transaction.getPaymentAmount(), blackNormalFont7, redNormalFont7, Rectangle.NO_BORDER));
                contentTable.addCell(createAmountCell(transaction.getBalance(), blackNormalFont7, redNormalFont7, Rectangle.NO_BORDER));
                // Only for Agents;
                if (isAgent) {
                    contentTable.addCell(createTextCell(transaction.getConsignee(), blackNormalFont7, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
                    contentTable.addCell(createTextCell(transaction.getVoyage(), blackNormalFont7, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
                }
            }
        }
        PdfPCell lineCell = createEmptyCell(Rectangle.BOTTOM);
        lineCell.setColspan(isAgent ? 9 : 7);
        contentTable.addCell(lineCell);
        PdfContentByte canvas = writer.getDirectContent();
        int currentRowStart = 0;
        int currentRow = 0;
        int totalRows = contentTable.getRows().size();
        availableHeight -= 70;
        boolean isFirstPage = true;
        float contentHeight = availableHeight;
        while (true) {
            availableHeight -= 60;
            // how many rows fit the height?
            while (availableHeight > 0 && currentRow < totalRows) {
                availableHeight -= contentTable.getRowHeight(currentRow++);
            }
            // we stop as soon as all the rows are counted
            if (currentRow == totalRows) {
                contentHeaderTable.writeSelectedRows(0, -1, document.leftMargin(), contentHeight, canvas);
                contentTable.writeSelectedRows(currentRowStart, currentRow, document.leftMargin(), contentHeight - 25, canvas);
                break;
            }
            // we draw part the rows that fit the page and start a new page
            contentHeaderTable.writeSelectedRows(0, -1, document.leftMargin(), contentHeight, canvas);
            contentTable.writeSelectedRows(currentRowStart, --currentRow, document.leftMargin(), contentHeight - 25, canvas);
            document.newPage();
            if (isFirstPage) {
                contentHeight += adjustedHeight;
                isFirstPage = false;
            }
            availableHeight = contentHeight;
            currentRowStart = currentRow;
        }
        PdfPTable paymentMethodTable = getPaymentMethodTable();
        paymentMethodTable.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());
        if (paymentMethodTable.getTotalHeight() > availableHeight) {
            document.newPage();
        }
        paymentMethodTable.writeSelectedRows(0, -1, document.leftMargin(), document.bottomMargin() + 145, canvas);
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        try {
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
            StringBuilder contactDetails = new StringBuilder();
            contactDetails.append(company.getName());
            contactDetails.append(" Account Contact: ");
            if (null != customer && CommonUtils.isNotEmpty(customer.getCollector())) {
                contactDetails.append(customer.getCollector()).append("(").append(customer.getCollectorEmail()).append(")");
            }
            cb.showText(contactDetails.toString());
            cb.endText();

            //This is for Page X Of Y
            String text = "Page " + writer.getPageNumber() + " of ";
            float textBase = document.bottom() - 50;
            float textSize = helvFont.getWidthPoint(text, 12);
            cb.beginText();
            cb.setFontAndSize(helvFont, 8);
            cb.setTextMatrix((document.right() / 2) - (textSize / 2), textBase);
            cb.showText(text);
            cb.endText();
            cb.addTemplate(pageTemplate, (document.right() / 2) + (textSize / 6), textBase);
            cb.restoreState();
        } catch (Exception e) {
            log.info("onEndPage failed on " + new Date(), e);
        }
    }

    @Override
    public void onCloseDocument(PdfWriter writer, Document document) {
        pageTemplate.beginText();
        pageTemplate.setFontAndSize(helvFont, 8);
        pageTemplate.setTextMatrix(0, 0);
        pageTemplate.showText(String.valueOf(writer.getPageNumber() - 1));
        pageTemplate.endText();
    }

    private void exit() {
        document.close();
    }

    public String create() throws Exception {
        StringBuilder fileName = new StringBuilder();
        fileName.append(LoadLogisoftProperties.getProperty("reportLocation")).append("/Documents/CustomerStatement/");
        fileName.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
        File file = new File(fileName.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        if (arReportsForm.isAllCustomers()) {
            fileName.append("AllCustomers");
        } else if (CommonUtils.isNotEmpty(arReportsForm.getCollector())) {
            fileName.append("Collector");
        } else {
            fileName.append(arReportsForm.getCustomerNumber());
        }
        fileName.append(".pdf");
        init(fileName.toString());
        writeContents();
        exit();
        return fileName.toString();
    }
}
