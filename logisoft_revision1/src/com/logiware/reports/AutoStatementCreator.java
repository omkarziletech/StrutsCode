package com.logiware.reports;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.hibernate.dao.CustomerAccountingDAO;
import com.gp.cong.logisoft.reports.ReportFormatMethods;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.logiware.bean.AccountingBean;
import com.logiware.bean.CustomerBean;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
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
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author LakshmiNarayanan
 */
public class AutoStatementCreator extends ReportFormatMethods {

    private final Logger log = Logger.getLogger(AutoStatementCreator.class);
    private Document document = null;
    private PdfWriter pdfWriter;
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
    private CustomerBean customerDetails = null;
    private String contextPath = null;
    float availableHeight = 0;
    float adjustedHeight = 0;

    private void init(String fileName, CustomerBean customerDetails, String contextPath) {
        try {
            document = new Document(PageSize.A4);
            document.setMargins(10, 10, 20, 80);
            pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(fileName));
            pdfWriter.setPageEvent(new AutoStatementCreator(customerDetails, contextPath));
            document.open();
            PdfAction action = PdfAction.gotoLocalPage(1, new PdfDestination(PdfDestination.XYZ, -1, -1, 0.75f), pdfWriter);
            pdfWriter.setOpenAction(action);
            Rectangle page = document.getPageSize();
            availableHeight = page.getHeight() - document.topMargin() - document.bottomMargin();
        } catch (Exception e) {
            log.info("AutoStatement not initialized", e);
        }
    }

    public AutoStatementCreator() throws Exception {
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

    public AutoStatementCreator(CustomerBean customerDetails, String contextPath) throws Exception {
        this();
        this.customerDetails = customerDetails;
        this.contextPath = contextPath;
    }

    @Override
    public void onOpenDocument(PdfWriter pdfWriter, Document document) {
        try {
            totalNoOfPages = pdfWriter.getDirectContent().createTemplate(20, 10);
            totalNoOfPages.setBoundingBox(new Rectangle(-10, -10, 20, 50));
            helv = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
        } catch (Exception e) {
            log.info("On Open AutoStatement Document failed :- ", e);
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
            log.info("On Start Page AutoStatement Document failed :- ", e);
        }
    }

    private void writeContents(List<AccountingBean> transactions, CustomerBean customer, CustomerBean agingBuckets) throws DocumentException, Exception {
        PdfPCell lineCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.BOTTOM);
        PdfPTable emptyRow = new PdfPTable(1);
        PdfPCell emptyCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER);
        emptyRow.addCell(emptyCell);
        boolean flag = false;
        PdfPTable headerTable = new PdfPTable(3);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new float[]{60, 5, 45});
        Rectangle page = document.getPageSize();
        headerTable.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());

        if (null != customer) {
            String customerName = customer.getCustomerName();
            String customerNumber = customer.getCustomerNumber();
            String address = null != customer.getAddress() ? customer.getAddress() : "";
            if (StringUtils.contains(customer.getAccountType(), "A")
                    || StringUtils.contains(customer.getAccountType(), "E") || StringUtils.contains(customer.getAccountType(), "I")) {
                flag = true;
            }
            PdfPTable customerTable = new PdfPTable(1);
            customerTable.setWidthPercentage(100);
            customerTable.addCell(makeCell(customerName, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont3, Rectangle.NO_BORDER));
            customerTable.addCell(makeCell(address, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont3, Rectangle.NO_BORDER));
            PdfPTable customerNumberTable = new PdfPTable(2);
            customerNumberTable.setWidthPercentage(100);
            customerNumberTable.setWidths(new float[]{20, 70});
            customerNumberTable.addCell(makeCell("Account#:", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
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
        agingTable.addCell(makeCell("ACCOUNT SUMMARY", Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
        agingTable.addCell(makeCell("AMOUNT", Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
        String ageOne = agingBuckets.getCurrent();
        String ageTwo = agingBuckets.getThirtyOneToSixtyDays();
        String ageThree = agingBuckets.getSixtyOneToNintyDays();
        String ageFour = agingBuckets.getGreaterThanNintyDays();
        String ageTotal = agingBuckets.getTotal();
        agingTable.addCell(makeCell("CURRENT", Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont3, Rectangle.NO_BORDER));
        if (StringUtils.contains(ageOne, "-")) {
            String amount = "$(" + ageOne.replaceAll("-", "") + ")";
            agingTable.addCell(makeCell(amount, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, redFont, Rectangle.NO_BORDER));
        } else {
            agingTable.addCell(makeCell("$" + ageOne, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER));
        }
        agingTable.addCell(makeCell("31-60 DAYS", Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont3, Rectangle.NO_BORDER));
        if (StringUtils.contains(ageTwo, "-")) {
            String amount = "$(" + ageTwo.replaceAll("-", "") + ")";
            agingTable.addCell(makeCell(amount, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, redFont, Rectangle.NO_BORDER));
        } else {
            agingTable.addCell(makeCell("$" + ageTwo, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER));
        }
        agingTable.addCell(makeCell("61-90 DAYS", Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont3, Rectangle.NO_BORDER));
        if (StringUtils.contains(ageThree, "-")) {
            String amount = "$(" + ageThree.replaceAll("-", "") + ")";
            agingTable.addCell(makeCell(amount, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, redFont, Rectangle.NO_BORDER));
        } else {
            agingTable.addCell(makeCell("$" + ageThree, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER));
        }
        agingTable.addCell(makeCell(">90 DAYS", Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont3, Rectangle.NO_BORDER));
        if (StringUtils.contains(ageFour, "-")) {
            String amount = "$(" + ageFour.replaceAll("-", "") + ")";
            agingTable.addCell(makeCell(amount, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, redFont, Rectangle.NO_BORDER));
        } else {
            agingTable.addCell(makeCell("$" + ageFour, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER));
        }
        agingTable.addCell(makeCell("TOTAL", Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont3, Rectangle.NO_BORDER));
        if (StringUtils.contains(ageTotal, "-")) {
            String amount = "$(" + ageTotal.replaceAll("-", "") + ")";
            agingTable.addCell(makeCell(amount, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, redFont, Rectangle.NO_BORDER));
        } else {
            agingTable.addCell(makeCell("$" + ageTotal, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackFont, Rectangle.NO_BORDER));
        }
        PdfPCell agingCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont3, Rectangle.BOX);
        agingCell.addElement(agingTable);
        headerTable.addCell(agingCell);
        float headerHeight = 0;
        for (int rowIndex = 0; rowIndex < headerTable.getRows().size(); rowIndex++) {
            headerHeight += headerTable.getRowHeight(rowIndex);
        }
        availableHeight -= headerHeight;
        adjustedHeight += headerHeight;
        document.add(headerTable);
        //Statement date
        PdfPTable statementTable = new PdfPTable(2);
        statementTable.setWidthPercentage(100);
        statementTable.setTotalWidth(new float[]{50, 50});
        statementTable.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());
        String statementDate = "Statement Date" + " " + DateUtils.formatDate(new Date(), "MM/dd/yyyy");
        statementTable.addCell(makeCell(statementDate, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont3, Rectangle.NO_BORDER));
        // add credit status
        String creditStatus = "";
        if (null != customer && CommonUtils.isNotEmpty(customer.getCustomerNumber())) {
            creditStatus = new CustomerAccountingDAO().getCreditStatus(customer.getCustomerNumber());
            creditStatus = CommonUtils.isEqualIgnoreCase(creditStatus, "Credit Hold") ? "ACCOUNT PAST DUE"
                    : CommonUtils.in(creditStatus, "No Credit", "Suspended/See Accounting", "Legal/See Accounting")
                            ? "DUE UPON RECEIPT" : "";
        }
        statementTable.addCell(makeCell(creditStatus, Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, headingFontRed, Rectangle.NO_BORDER));

        float statementHeight = 0;
        for (int rowIndex = 0; rowIndex < statementTable.getRows().size(); rowIndex++) {
            statementHeight += statementTable.getRowHeight(rowIndex);
        }
        availableHeight -= statementHeight;
        adjustedHeight += statementHeight;
        document.add(statementTable);
        //Account detail header
        PdfPTable accountDetailTable = new PdfPTable(1);
        accountDetailTable.setWidthPercentage(100);
        accountDetailTable.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());
        accountDetailTable.addCell(makeCell("ACCOUNT DETAIL", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, headingFont3, Rectangle.NO_BORDER));
        float accountDetailHeight = 0;
        for (int rowIndex = 0; rowIndex < accountDetailTable.getRows().size(); rowIndex++) {
            accountDetailHeight += accountDetailTable.getRowHeight(rowIndex);
        }
        availableHeight -= accountDetailHeight;
        adjustedHeight += accountDetailHeight;
        document.add(accountDetailTable);
        //Statement list header
        PdfPTable statementHeaderTable = null;
        if (flag) {
            statementHeaderTable = new PdfPTable(9);
            statementHeaderTable.setWidths(new float[]{8, 11, 10, 18, 10, 11, 12, 11, 9});
        } else {
            statementHeaderTable = new PdfPTable(7);
            statementHeaderTable.setWidths(new float[]{8, 17, 17, 17, 10, 11, 12});
        }
        statementHeaderTable.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());
        statementHeaderTable.setWidthPercentage(100);
        statementHeaderTable.addCell(makeCell("Date", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFont, Rectangle.BOX));
        statementHeaderTable.addCell(makeCell("Invoice/BL", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFont, Rectangle.BOX));
        statementHeaderTable.addCell(makeCell("Booking#", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFont, Rectangle.BOX));
        statementHeaderTable.addCell(makeCell("Customer Reference", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFont, Rectangle.BOX));
        statementHeaderTable.addCell(makeCell("Invoice Amount", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFont, Rectangle.BOX));
        statementHeaderTable.addCell(makeCell("Payment/ Adjustment", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFont, Rectangle.BOX));
        statementHeaderTable.addCell(makeCell("Balance", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFont, Rectangle.BOX));
        if (flag) {
            statementHeaderTable.addCell(makeCell("Consignee", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFont, Rectangle.BOX));
            statementHeaderTable.addCell(makeCell("Voyage", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFont, Rectangle.BOX));
        }
        PdfPTable statementListTable = null;
        if (flag) {
            statementListTable = new PdfPTable(9);
            statementListTable.setWidths(new float[]{8, 11, 10, 18, 10, 11, 12, 11, 9});
        } else {
            statementListTable = new PdfPTable(7);
            statementListTable.setWidths(new float[]{8, 17, 17, 17, 10, 11, 12});
        }
        statementListTable.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());
        statementListTable.setWidthPercentage(100);
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
            if (flag) {
                statementListTable.addCell(makeCell(txn.getConsignee(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, textFontForPayment, Rectangle.NO_BORDER));
                statementListTable.addCell(makeCell(txn.getVoyage(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, textFontForPayment, Rectangle.NO_BORDER));
            }
        }
        statementListTable.addCell(lineCell);
        PdfContentByte canvas = pdfWriter.getDirectContent();
        int currentRowStart = 0;
        int currentRow = 0;
        int totalRows = statementListTable.getRows().size();
        availableHeight -= 70;
        boolean isFirstPage = true;
        float statementListHeight = availableHeight;
        while (true) {
            availableHeight -= 85;
            // how many rows fit the height?
            while (availableHeight > 0 && currentRow < totalRows) {
                availableHeight -= statementListTable.getRowHeight(currentRow++);
            }
            // we stop as soon as all the rows are counted
            if (currentRow == totalRows) {
                statementHeaderTable.writeSelectedRows(0, -1, document.leftMargin(), statementListHeight, canvas);
                statementListTable.writeSelectedRows(currentRowStart, currentRow, document.leftMargin(), statementListHeight - 25, canvas);
                break;
            }
            // we draw part the rows that fit the page and start a new page
            statementHeaderTable.writeSelectedRows(0, -1, document.leftMargin(), statementListHeight, canvas);
            statementListTable.writeSelectedRows(currentRowStart, --currentRow, document.leftMargin(), statementListHeight - 25, canvas);
            document.newPage();
            if (isFirstPage) {
                statementListHeight += adjustedHeight;
                isFirstPage = false;
            }
            availableHeight = statementListHeight;
            currentRowStart = currentRow;
        }
        PdfPTable paymentMethodTable = getPaymentMethodTable();
        paymentMethodTable.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());
        if (paymentMethodTable.getTotalHeight() > availableHeight) {
            document.newPage();
        }
        paymentMethodTable.writeSelectedRows(0, -1, document.leftMargin(), document.bottomMargin() + 145, canvas);
    }

    private PdfPTable getPaymentMethodTable() throws DocumentException, Exception {
        PdfPCell emptyCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER);
        //Payment methods
        PdfPTable paymentMethodTable = new PdfPTable(5);
        paymentMethodTable.setWidthPercentage(100);
        paymentMethodTable.setWidths(new float[]{30.0f, 1.0f, 32.5f, 1.0f, 35.5f});
        if(companyName.contains("ECONOCARIBE")){
        Paragraph invoiceEle = new Paragraph();
        Chunk invComments = new Chunk("Please request invoice copies from", blackBoldFont6);
        Chunk invoiceEmail = new Chunk(" invoicereq@docs.ecuworldwide.us", blackBoldFontBlue);
        invComments.setBackground(Color.yellow);
        invoiceEmail.setBackground(Color.yellow);
        invoiceEle.add(invComments);
        invoiceEle.add(invoiceEmail);
        PdfPCell paymentFirstHeaderCell = makeCell("", Element.ALIGN_CENTER, Element.ALIGN_CENTER, blackBoldFont6, Rectangle.NO_BORDER);
        invoiceEle.setAlignment(Element.ALIGN_CENTER);
        paymentFirstHeaderCell.addElement(invoiceEle);
        paymentFirstHeaderCell.setColspan(5);
        paymentMethodTable.addCell(paymentFirstHeaderCell);
        }
        //Empty cell
        emptyCell.setColspan(5);
        paymentMethodTable.addCell(emptyCell);
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
            StringBuilder collector = new StringBuilder();
            if (CommonUtils.isNotEmpty(customerDetails.getCollector())) {
                collector.append(customerDetails.getCollector()).append("(").append(customerDetails.getCollectorEmail()).append(")");
            }
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
            log.info("On End Page of AutoStatement failed :- ", e);
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
            log.info("On Close AutoStatement failed :- ", e);
        }
    }

    public void doExit() {
        document.close();
    }

    public String createReport(String contextPath, List<AccountingBean> transactions, CustomerBean customerDetails, CustomerBean agingBuckets) {
        try {
            StringBuilder fileName = new StringBuilder(LoadLogisoftProperties.getProperty("reportLocation"));
            fileName.append("/Documents/CustomerStatement/");
            fileName.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
            File file = new File(fileName.toString());
            if (!file.exists()) {
                file.mkdirs();
            }
            fileName.append(customerDetails.getCustomerNumber()).append(".pdf");
            this.customerDetails = customerDetails;
            init(fileName.toString(), customerDetails, contextPath);
            writeContents(transactions, customerDetails, agingBuckets);
            doExit();
            return fileName.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
