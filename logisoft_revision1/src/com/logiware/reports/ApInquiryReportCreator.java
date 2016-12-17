package com.logiware.reports;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.reports.ReportFormatMethods;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.struts.form.ApInquiryForm;
import com.logiware.bean.AccountingBean;
import com.logiware.bean.CustomerBean;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
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
import org.apache.commons.lang3.StringUtils;

import org.apache.log4j.Logger;

/**
 *
 * @author Lakshmi Narayanan
 */
public class ApInquiryReportCreator extends ReportFormatMethods implements java.io.Serializable {
    private static final Logger log = Logger.getLogger(ApInquiryReportCreator.class);
    private Document document = null;
    private PdfWriter pdfWriter = null;
    private String contextPath = null;
    private ApInquiryForm apInquiryForm = null;
    protected PdfTemplate totalNoOfPages = null;
    protected BaseFont fontForFooter = null;

    public ApInquiryReportCreator() {
    }

    public ApInquiryReportCreator(ApInquiryForm apInquiryForm, String contextPath) {
        this.apInquiryForm = apInquiryForm;
        this.contextPath = contextPath;
    }

    private void init(ApInquiryForm apInquiryForm, String contextPath, String fileName) throws Exception {
        this.apInquiryForm = apInquiryForm;
        this.contextPath = contextPath;
        document = new Document(PageSize.A4.rotate());
        document.setMargins(10, 10, 10, 50);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(fileName));
        pdfWriter.setPageEvent(new ApInquiryReportCreator(apInquiryForm, contextPath));
        document.open();
        pdfWriter.setOpenAction(PdfAction.gotoLocalPage(1, new PdfDestination(PdfDestination.XYZ, -1, -1, 0.75f), pdfWriter));
    }

    private PdfPTable createHeaderTable() throws Exception {
        Font blackBoldFont2 = new Font(Font.HELVETICA, 7, Font.BOLD, Color.BLACK);
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
        String title = "AP Inquiry Report";
        PdfPCell titleCell = makeCell(title, Element.ALIGN_CENTER, Element.ALIGN_TOP, headingFont1, Rectangle.NO_BORDER, Color.LIGHT_GRAY);
        headerTable.addCell(titleCell);
        PdfPTable cutOffTable = makeTable(1);
        cutOffTable.setWidthPercentage(100);
        cutOffTable.addCell(makeCell("Date: " + DateUtils.formatDate(new Date(), "MM/dd/yyyy"), Element.ALIGN_RIGHT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
        PdfPCell cutOffCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont, Rectangle.NO_BORDER);
        cutOffCell.addElement(cutOffTable);
        headerTable.addCell(cutOffCell);
        PdfPTable accountDetailsTable = new PdfPTable(2);
        accountDetailsTable.setWidthPercentage(100);
        accountDetailsTable.setWidths(new float[]{50f, 50f});
        if (CommonUtils.isNotEmpty(apInquiryForm.getVendorNumber())) {
            CustomerBean vendor = apInquiryForm.getVendor();
            PdfPTable customerTable = new PdfPTable(2);
            customerTable.setWidthPercentage(100);
            customerTable.setWidths(new float[]{25f, 75f});
            customerTable.addCell(makeCell("Vendor Name : ", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
            customerTable.addCell(makeCell(vendor.getCustomerName(), Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont, Rectangle.NO_BORDER));
            customerTable.addCell(makeCell("Vendor Number : ", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
            customerTable.addCell(makeCell(vendor.getCustomerNumber(), Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont, Rectangle.NO_BORDER));
            PdfPCell customerCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont, Rectangle.NO_BORDER);
            customerCell.addElement(customerTable);
            accountDetailsTable.addCell(customerCell);
            String address = "";
            if (CommonUtils.isNotEmpty(vendor.getAddress())) {
                address += vendor.getAddress() + ",\n";
            }
            if (CommonUtils.isNotEmpty(vendor.getCity())) {
                address += vendor.getCity() + ",\n";
            }
            if (CommonUtils.isNotEmpty(vendor.getState())) {
                address += vendor.getState() + ",\n";
            }
            if (CommonUtils.isNotEmpty(vendor.getCountry())) {
                address += vendor.getCountry() + ",\n";
            }
            if (CommonUtils.isNotEmpty(vendor.getZipCode())) {
                address += vendor.getZipCode() + ",\n";
            }
            if (CommonUtils.isNotEmpty(vendor.getPhone())) {
                address += vendor.getPhone() + ",\n";
            }
            if (CommonUtils.isNotEmpty(vendor.getFax())) {
                address += vendor.getFax() + ",";
            }
            address = StringUtils.removeEnd(address, ",");
            PdfPTable addressTable = new PdfPTable(2);
            addressTable.setWidthPercentage(100);
            addressTable.setWidths(new float[]{25f, 75f});
            addressTable.addCell(makeCell("Vendor Address : ", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
            addressTable.addCell(makeCell(address, Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont, Rectangle.NO_BORDER));
            PdfPCell addressCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont, Rectangle.NO_BORDER);
            addressCell.addElement(addressTable);
            accountDetailsTable.addCell(addressCell);
        }
        if (CommonUtils.isNotEqual(apInquiryForm.getSearchBy(), "0") && CommonUtils.isNotEmpty(apInquiryForm.getSearchValue())) {
            PdfPTable searchTable = new PdfPTable(2);
            searchTable.setWidthPercentage(100);
            searchTable.setWidths(new float[]{25f, 75f});
            searchTable.addCell(makeCell("Search by : ", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
            searchTable.addCell(makeCell(apInquiryForm.getSearchBy(), Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont, Rectangle.NO_BORDER));
            searchTable.addCell(makeCell("Search Value : ", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
            searchTable.addCell(makeCell(apInquiryForm.getSearchValue(), Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont, Rectangle.NO_BORDER));
            PdfPCell searchCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont, Rectangle.NO_BORDER);
            searchCell.addElement(searchTable);
            accountDetailsTable.addCell(searchCell);
            accountDetailsTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont, Rectangle.NO_BORDER));
        }
        PdfPCell accountDetailsCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont, Rectangle.NO_BORDER);
        accountDetailsCell.addElement(accountDetailsTable);
        headerTable.addCell(accountDetailsCell);
        headerTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont, Rectangle.NO_BORDER));
        PdfPTable listHeadingTable = new PdfPTable(15);
        listHeadingTable.setWidthPercentage(100);
        listHeadingTable.setWidths(new float[]{17, 10, 13, 10, 8, 10, 10, 10, 8, 8, 8, 10, 4, 10, 10});
        listHeadingTable.addCell(makeCell("Vendor Name", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
        listHeadingTable.addCell(makeCell("Vendor#", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
        listHeadingTable.addCell(makeCell("Invoice#", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
        listHeadingTable.addCell(makeCell("Invoice Date", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
        listHeadingTable.addCell(makeCell("Due Date", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
        listHeadingTable.addCell(makeCell("Amount", Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
        listHeadingTable.addCell(makeCell("Balance", Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
        listHeadingTable.addCell(makeCell("Cheque#", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
        listHeadingTable.addCell(makeCell("Dock Receipt", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
        listHeadingTable.addCell(makeCell("Voyage", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
        listHeadingTable.addCell(makeCell("Cost Code", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
        listHeadingTable.addCell(makeCell("Payment Date", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
        listHeadingTable.addCell(makeCell("Type", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
        listHeadingTable.addCell(makeCell("Reference#", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
        listHeadingTable.addCell(makeCell("Status", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.NO_BORDER));
        PdfPCell listHeadingCell = makeCell("", Element.ALIGN_RIGHT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER);
        listHeadingCell.setBorderWidthTop(.5f);
        listHeadingCell.setBorderWidthBottom(.5f);
        listHeadingCell.setLeading(.5f, .5f);
        listHeadingCell.addElement(listHeadingTable);
        headerTable.addCell(listHeadingCell);
        return headerTable;
    }

    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        totalNoOfPages = writer.getDirectContent().createTemplate(100, 100);
        totalNoOfPages.setBoundingBox(new Rectangle(-20, -20, 100, 100));
        try {
            fontForFooter = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
        } catch (Exception e) {
            log.info("onOpenDocument failed on " + new Date(),e);
        }
    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        try {
            document.add(this.createHeaderTable());
        } catch (Exception e) {
            log.info("onStartPage failed on " + new Date(),e);
        }
    }

    private void writeContents(ApInquiryForm apInquiryForm) throws Exception {
        Font blackFont = new Font(Font.HELVETICA, 7, 0, Color.BLACK);
        Font redFont = new Font(Font.HELVETICA, 9, 0, Color.RED);
        if (CommonUtils.isNotEmpty(apInquiryForm.getApInquiryList())) {
            PdfPTable listContentsTable = new PdfPTable(15);
            listContentsTable.setWidths(new float[]{17, 10, 13, 10, 8, 10, 10, 10, 8, 8, 8, 10, 4, 10, 10});
            listContentsTable.setWidthPercentage(100);
            for (AccountingBean transaction : apInquiryForm.getApInquiryList()) {
                String customerName = StringUtils.left(transaction.getCustomerName(), 20);
                listContentsTable.addCell(makeCell(customerName, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOTTOM));
                listContentsTable.addCell(makeCell(transaction.getCustomerNumber(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOTTOM));
                listContentsTable.addCell(makeCell(transaction.getInvoiceOrBl(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOTTOM));
                listContentsTable.addCell(makeCell(transaction.getFormattedDate(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOTTOM));
                listContentsTable.addCell(makeCell(transaction.getFormattedDueDate(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOTTOM));
                if (transaction.getFormattedAmount().contains("-")) {
                    String amount = "(" + transaction.getFormattedAmount().replace("-", "") + ")";
                    listContentsTable.addCell(makeCell(amount, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOTTOM));
                } else {
                    listContentsTable.addCell(makeCell(transaction.getFormattedAmount(),
                            Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOTTOM));
                }
                if (transaction.getFormattedBalance().contains("-")) {
                    String balance = "(" + transaction.getFormattedBalance().replace("-", "") + ")";
                    listContentsTable.addCell(makeCell(balance, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOTTOM));
                } else {
                    listContentsTable.addCell(makeCell(transaction.getFormattedBalance(),
                            Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOTTOM));
                }
                listContentsTable.addCell(makeCell(transaction.getCheckNumber(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOTTOM));
                listContentsTable.addCell(makeCell(transaction.getDockReceipt(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOTTOM));
                listContentsTable.addCell(makeCell(transaction.getVoyage(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOTTOM));
                listContentsTable.addCell(makeCell(transaction.getChargeCode(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOTTOM));
                if (CommonUtils.isEqualIgnoreCase(transaction.getStatus(), CommonConstants.STATUS_PAID)) {
                    listContentsTable.addCell(makeCell(transaction.getFormattedPaymentDate(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOTTOM));
                } else {
                    listContentsTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOTTOM));
                }
                listContentsTable.addCell(makeCell(transaction.getTransactionType(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOTTOM));
                String customerReference = StringUtils.left(transaction.getCustomerReference(), 15);
                listContentsTable.addCell(makeCell(customerReference, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOTTOM));
                String status = transaction.getStatus();
                if (CommonUtils.isEqualIgnoreCase(status, CommonConstants.STATUS_PAID)) {
                    status = "Paid";
                } else if (CommonUtils.isEqualIgnoreCase(status, CommonConstants.STATUS_READY_TO_PAY)) {
                    status = "Ready To Pay";
                } else if (CommonUtils.isEqualIgnoreCase(status, CommonConstants.STATUS_HOLD)) {
                    status = "Hold";
                } else if (CommonUtils.isEqualIgnoreCase(status, CommonConstants.STATUS_REJECT)) {
                    status = "Reject";
                } else if (CommonUtils.isEqualIgnoreCase(status, CommonConstants.STATUS_POSTED_TO_GL)) {
                    status = "Open";
                } else if (CommonUtils.isEqualIgnoreCase(status, CommonConstants.STATUS_ASSIGN)) {
                    status = "Assigned";
                } else if (CommonUtils.isEqualIgnoreCase(status, CommonConstants.STATUS_IN_PROGRESS)) {
                    status = "In Progress";
                }
                listContentsTable.addCell(makeCell(status, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOTTOM));
            }
            document.add(listContentsTable);
        }
    }

    @Override
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

    public String createReport(ApInquiryForm apInquiryForm, String contextPath) {
        try {
            String fileName = LoadLogisoftProperties.getProperty("reportLocation") + "/Documents/ApInquiry/" + DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/";
            File file = new File(fileName);
            if (!file.exists()) {
                file.mkdirs();
            }
            if (CommonUtils.isNotEqual(apInquiryForm.getSearchBy(), "0") && CommonUtils.isNotEmpty(apInquiryForm.getSearchValue())) {
                fileName += "ApInquiry.pdf";
            } else if (CommonUtils.isNotEmpty(apInquiryForm.getVendorNumber())) {
                fileName += apInquiryForm.getVendorNumber() + ".pdf";
            } else {
                fileName += "ApInquiry.pdf";
            }
            this.init(apInquiryForm, contextPath, fileName);
            this.writeContents(apInquiryForm);
            this.destroy();
            return fileName;
        } catch (Exception e) {
            log.info("createReport failed on " + new Date(),e);
            return null;
        }
    }
}
