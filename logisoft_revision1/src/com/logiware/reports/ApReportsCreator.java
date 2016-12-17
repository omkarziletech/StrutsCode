package com.logiware.reports;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.struts.form.ApReportsForm;
import com.logiware.bean.CustomerBean;
import com.logiware.bean.ReportBean;
import com.logiware.hibernate.dao.ApReportsDAO;
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
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author Lakshmi Naryanan
 */
public class ApReportsCreator extends BaseReportCreator implements ConstantsInterface {

    private static final Logger log = Logger.getLogger(ApReportsCreator.class);
    private ApReportsForm apReportsForm;

    public ApReportsCreator() {
    }

    public ApReportsCreator(ApReportsForm apReportsForm, String contextPath) {
        this.apReportsForm = apReportsForm;
        this.contextPath = contextPath;
    }

    private void init(String fileName) throws Exception {
        document = new Document(PageSize.A4.rotate());
        document.setMargins(5, 5, 5, 30);
        writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
        writer.setPdfVersion(PdfWriter.PDF_VERSION_1_7);
        writer.setUserunit(1f);
        writer.setPageEvent(new ApReportsCreator(apReportsForm, contextPath));
        document.open();
        writer.setOpenAction(PdfAction.gotoLocalPage(1, new PdfDestination(PdfDestination.XYZ, -1, -1, 1f), writer));
    }

    private void writeVendorDetails() throws Exception {
        PdfPTable vendorDetailsTable = new PdfPTable(3);
        vendorDetailsTable.setWidthPercentage(100);
        vendorDetailsTable.setWidths(new float[]{30, 35, 35});
        CustomerBean vendorDetails = new ApReportsDAO().getVendorDetails(apReportsForm.getVendorNumber());
        PdfPTable vendorNumberTable = new PdfPTable(2);
        vendorNumberTable.setWidthPercentage(100);
        vendorNumberTable.setWidths(new float[]{50, 50});
        vendorNumberTable.addCell(createCell("Vendor Number : ", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.NO_BORDER, LIGHT_ASH));
        vendorNumberTable.addCell(createCell(vendorDetails.getCustomerNumber(), blackBoldFont7, Element.ALIGN_LEFT, Rectangle.NO_BORDER, LIGHT_ASH));
        vendorNumberTable.addCell(createCell("ECU Designation : ", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.NO_BORDER, LIGHT_ASH));
        vendorNumberTable.addCell(createCell(vendorDetails.getEcuDesignation(), blackBoldFont7, Element.ALIGN_LEFT, Rectangle.NO_BORDER, LIGHT_ASH));
        PdfPCell vendorNumberCell = createEmptyCell(Rectangle.NO_BORDER);
        vendorNumberCell.addElement(vendorNumberTable);
        vendorDetailsTable.addCell(vendorNumberCell);
        PdfPTable vendorNameTable = new PdfPTable(2);
        vendorNameTable.setWidthPercentage(100);
        vendorNameTable.setWidths(new float[]{30, 70});
        vendorNameTable.addCell(createCell("Vendor Name : ", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.NO_BORDER, LIGHT_ASH));
        vendorNameTable.addCell(createCell(vendorDetails.getCustomerName(), blackBoldFont7, Element.ALIGN_LEFT, Rectangle.NO_BORDER, LIGHT_ASH));
        PdfPCell vendorNameCell = createEmptyCell(Rectangle.NO_BORDER);
        vendorNameCell.addElement(vendorNameTable);
        vendorDetailsTable.addCell(vendorNameCell);
        PdfPTable vendorAddressTable = new PdfPTable(2);
        vendorAddressTable.setWidthPercentage(100);
        vendorAddressTable.setWidths(new float[]{15, 85});
        vendorAddressTable.addCell(createCell("Address : ", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.NO_BORDER, LIGHT_ASH));
        StringBuilder address = new StringBuilder(null != vendorDetails.getAddress() ? vendorDetails.getAddress() : "");
        if (CommonUtils.isNotEmpty(vendorDetails.getContact())) {
            address.append("\nContact : ").append(vendorDetails.getContact());
        }
        if (CommonUtils.isNotEmpty(vendorDetails.getEmail())) {
            address.append("\nEmail : ").append(vendorDetails.getEmail());
        }
        vendorAddressTable.addCell(createCell(address.toString(), blackBoldFont8, Element.ALIGN_LEFT, Rectangle.NO_BORDER, LIGHT_ASH));
        PdfPCell vendorAddressCell = createEmptyCell(Rectangle.NO_BORDER, LIGHT_ASH);
        vendorAddressCell.addElement(vendorAddressTable);
        vendorDetailsTable.addCell(vendorAddressCell);
        PdfPCell vendorDetailsCell = createEmptyCell(Rectangle.BOTTOM, LIGHT_ASH);
        vendorDetailsCell.addElement(vendorDetailsTable);
        headerTable.addCell(vendorDetailsCell);
    }

    private void writeAgingHeader() throws Exception {
        String title = "AP Aging Report";
        PdfPCell titleCell = createCell(title, headerBoldFont15, Element.ALIGN_CENTER, Rectangle.BOTTOM, Color.LIGHT_GRAY);
        headerTable.addCell(titleCell);
        String cutOffDate = "Cut-off Date : " + apReportsForm.getCutOffDate();
        PdfPCell cutOffDateCell = createCell(cutOffDate, headerBoldFont11, Element.ALIGN_RIGHT, Rectangle.BOTTOM);
        headerTable.addCell(cutOffDateCell);
        if (CommonUtils.isNotEmpty(apReportsForm.getVendorNumber())
                && CommonUtils.isNotEqualIgnoreCase(apReportsForm.getShowAllCustomer(), YES)) {
            writeVendorDetails();
        }
    }

    private void writeAdjustedAccrualsHeader() throws Exception {
        String title = "Adjusted Accruals Report";
        PdfPCell titleCell = createCell(title, headerBoldFont15, Element.ALIGN_CENTER, Rectangle.BOTTOM, Color.LIGHT_GRAY);
        headerTable.addCell(titleCell);
    }

    private void writeVendorHeader() throws Exception {
        String title = "Vendor Report";
        PdfPCell titleCell = createCell(title, headerBoldFont15, Element.ALIGN_CENTER, Rectangle.BOTTOM, Color.LIGHT_GRAY);
        headerTable.addCell(titleCell);
    }

    private void writeActivityHeader() throws Exception {
        String title = "Activity Report";
        PdfPCell titleCell = createCell(title, headerBoldFont15, Element.ALIGN_CENTER, Rectangle.BOTTOM, Color.LIGHT_GRAY);
        headerTable.addCell(titleCell);
        PdfPTable filtersTable = new PdfPTable(5);
        filtersTable.setWidthPercentage(100);
        filtersTable.setWidths(new float[]{15, 15, 15, 55});
        filtersTable.addCell(createCell("Vendor Name :", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        filtersTable.addCell(createCell(apReportsForm.getVendorName(), blackNormalFont7, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        filtersTable.addCell(createCell("Vendor Number :", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        filtersTable.addCell(createCell(apReportsForm.getVendorNumber(), blackNormalFont7, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        filtersTable.addCell(createCell("ECU Designation :", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        filtersTable.addCell(createCell(apReportsForm.getEcuDesignation(), blackNormalFont7, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        filtersTable.addCell(createCell("User Name :", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        filtersTable.addCell(createCell(apReportsForm.getUserName(), blackNormalFont7, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        filtersTable.addCell(createCell("Date From :", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        filtersTable.addCell(createCell(apReportsForm.getFromDate(), blackNormalFont7, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        filtersTable.addCell(createCell("Date To :", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        filtersTable.addCell(createCell(apReportsForm.getToDate(), blackNormalFont7, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        PdfPCell filtersCell = createEmptyCell(Rectangle.BOTTOM);
        filtersCell.addElement(filtersTable);
        headerTable.addCell(filtersCell);
    }

    private void writeCheckRegistersHeader() throws Exception {
        String title = "Check Register Report";
        PdfPCell titleCell = createCell(title, headerBoldFont15, Element.ALIGN_CENTER, Rectangle.BOTTOM, Color.LIGHT_GRAY);
        headerTable.addCell(titleCell);
        PdfPTable filtersTable = new PdfPTable(8);
        filtersTable.setWidthPercentage(100);
        filtersTable.setWidths(new float[]{10, 15, 10, 15, 10, 15, 10, 15});
        String status = "ALL";
        if (CommonUtils.isEqualIgnoreCase(apReportsForm.getCheckStatus(), YES)) {
            status = "Cleared";

        } else if (CommonUtils.isEqualIgnoreCase(apReportsForm.getCheckStatus(), NO)) {
            status = "Not Cleared";
        } else if (CommonUtils.isEqualIgnoreCase(apReportsForm.getCheckStatus(), STATUS_VOID)) {
            status = "Voided";
        }
        filtersTable.addCell(createCell("Bank Account :", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        filtersTable.addCell(createCell(apReportsForm.getBankAccount(), blackNormalFont7, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        filtersTable.addCell(createCell("GL Account :", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        filtersTable.addCell(createCell(apReportsForm.getGlAccount(), blackNormalFont7, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        filtersTable.addCell(createCell("Check From :", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        filtersTable.addCell(createCell(apReportsForm.getFromCheck(), blackNormalFont7, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        filtersTable.addCell(createCell("Check To :", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        filtersTable.addCell(createCell(apReportsForm.getToCheck(), blackNormalFont7, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        filtersTable.addCell(createCell("Date From :", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        filtersTable.addCell(createCell(apReportsForm.getFromDate(), blackNormalFont7, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        filtersTable.addCell(createCell("Date To :", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        filtersTable.addCell(createCell(apReportsForm.getToDate(), blackNormalFont7, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        filtersTable.addCell(createCell("Payment Method :", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        filtersTable.addCell(createCell(apReportsForm.getPaymentMethod(), blackNormalFont7, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        filtersTable.addCell(createCell("Status :", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        filtersTable.addCell(createCell(status, blackNormalFont7, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        PdfPCell filtersCell = createEmptyCell(Rectangle.BOTTOM);
        filtersCell.addElement(filtersTable);
        headerTable.addCell(filtersCell);
    }

    private void writeDpoHeader() throws Exception {
        String title = "DPO Report";
        PdfPCell titleCell = createCell(title, headerBoldFont15, Element.ALIGN_CENTER, Rectangle.BOTTOM, Color.LIGHT_GRAY);
        headerTable.addCell(titleCell);
        PdfPTable filtersTable = new PdfPTable(8);
        filtersTable.setWidthPercentage(100);
        filtersTable.setWidths(new float[]{10, 15, 10, 15, 10, 15, 10, 15});
        String dpoFor = "All Account Payables";
        if (apReportsForm.getSearchDpoBy().equalsIgnoreCase(BY_USER)) {
            dpoFor = "User";
        } else if (apReportsForm.getSearchDpoBy().equalsIgnoreCase(BY_VENDOR)) {
            dpoFor = "Vendor";
        }
        filtersTable.addCell(createCell("DPO For :", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        filtersTable.addCell(createCell(dpoFor, blackNormalFont7, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        filtersTable.addCell(createCell("From Period :", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        filtersTable.addCell(createCell(apReportsForm.getFromPeriod(), blackNormalFont7, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        filtersTable.addCell(createCell("To Period :", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        filtersTable.addCell(createCell(apReportsForm.getToPeriod(), blackNormalFont7, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        filtersTable.addCell(createCell("Number Of Days :", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        filtersTable.addCell(createCell(apReportsForm.getNumberOfDays(), blackNormalFont7, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        if (apReportsForm.getSearchDpoBy().equalsIgnoreCase(BY_USER)) {
            filtersTable.addCell(createCell("User Name :", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
            filtersTable.addCell(createCell(apReportsForm.getUserName(), blackNormalFont7, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
            PdfPCell emptyCell = createEmptyCell(Rectangle.NO_BORDER);
            emptyCell.setColspan(6);
            filtersTable.addCell(emptyCell);
        } else if (apReportsForm.getSearchDpoBy().equalsIgnoreCase(BY_VENDOR)) {
            filtersTable.addCell(createCell("Vendor Name :", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
            filtersTable.addCell(createCell(apReportsForm.getVendorName(), blackNormalFont7, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
            filtersTable.addCell(createCell("Vendor Number :", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
            filtersTable.addCell(createCell(apReportsForm.getVendorNumber(), blackNormalFont7, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
            PdfPCell emptyCell = createEmptyCell(Rectangle.NO_BORDER);
            emptyCell.setColspan(4);
            filtersTable.addCell(emptyCell);
        }
        PdfPCell filtersCell = createEmptyCell(Rectangle.BOTTOM);
        filtersCell.addElement(filtersTable);
        headerTable.addCell(filtersCell);

    }

    private void writeDisputedItemsHeader() throws Exception {
        String title = "Disputed Items Report";
        PdfPCell titleCell = createCell(title, headerBoldFont15, Element.ALIGN_CENTER, Rectangle.BOTTOM, Color.LIGHT_GRAY);
        headerTable.addCell(titleCell);
        PdfPTable filtersTable = new PdfPTable(4);
        filtersTable.setWidthPercentage(100);
        filtersTable.setWidths(new float[]{7, 15, 7, 71});
        filtersTable.addCell(createTextCell("From Date : ", blackBoldFont8, Rectangle.NO_BORDER));
        filtersTable.addCell(createTextCell(apReportsForm.getFromDate(), blackNormalFont7, Rectangle.NO_BORDER));
        filtersTable.addCell(createTextCell("To Date : ", blackBoldFont8, Rectangle.NO_BORDER));
        filtersTable.addCell(createTextCell(apReportsForm.getToDate(), blackNormalFont7, Rectangle.NO_BORDER));
        PdfPCell filtersCell = createEmptyCell(Rectangle.BOTTOM);
        filtersCell.addElement(filtersTable);
        headerTable.addCell(filtersCell);
    }

    private void writeHeader() throws Exception {
        headerTable = new PdfPTable(1);
        headerTable.setWidthPercentage(100);
        String imagePath = LoadLogisoftProperties.getProperty("application.image.logo");
        Image image = Image.getInstance(contextPath + imagePath);
        image.scalePercent(75);
        headerTable.addCell(createImageCell(image));
        headerTable.addCell(createEmptyCell(Rectangle.BOTTOM));
        if (CommonUtils.isEqualIgnoreCase(apReportsForm.getReportType(), AP_AGING_REPORT)) {
            writeAgingHeader();
        } else if (CommonUtils.isEqual(apReportsForm.getReportType(), AP_ADJUSTED_ACCRUALS_REPORT)) {
            writeAdjustedAccrualsHeader();
        } else if (CommonUtils.isEqual(apReportsForm.getReportType(), AP_VENDOR_REPORT)) {
            writeVendorHeader();
        } else if (CommonUtils.isEqual(apReportsForm.getReportType(), AP_ACTIVITY_REPORT)) {
            writeActivityHeader();
        } else if (CommonUtils.isEqual(apReportsForm.getReportType(), AP_CHECK_REGISTER_REPORT)) {
            writeCheckRegistersHeader();
        } else if (CommonUtils.isEqual(apReportsForm.getReportType(), AP_DPO_REPORT)) {
            writeDpoHeader();
        } else if (CommonUtils.isEqual(apReportsForm.getReportType(), AP_DISPUTED_ITEMS_REPORT)) {
            writeDisputedItemsHeader();
        }
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

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        try {
            writeHeader();
            document.add(headerTable);
        } catch (Exception e) {
            log.info("onStartPage failed on " + new Date(), e);
        }
    }

    private void writeDetailedAgingContent(List<ReportBean> transactions, boolean isAllVendors) throws Exception {
        if (isAllVendors) {
            contentTable = new PdfPTable(14);
            contentTable.setWidthPercentage(100);
            contentTable.setWidths(new int[]{13, 7, 6, 8, 5, 7, 5, 7, 7, 7, 7, 7, 7, 7});
            contentTable.addCell(createHeaderCell("Vendor Name", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
            contentTable.addCell(createHeaderCell("Vendor Number", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
            contentTable.addCell(createHeaderCell("ECU Designation", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        } else {
            contentTable = new PdfPTable(11);
            contentTable.setWidthPercentage(100);
            contentTable.setWidths(new float[]{10f, 8f, 11f, 6f, 9f, 9f, 9f, 9f, 9f, 9f, 11f});
        }
        contentTable.addCell(createHeaderCell("Invoice Number", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Invoice Date", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Vendor Reference", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Type", blackBoldFont8, Element.ALIGN_CENTER, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("0-30 days", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("31-60 days", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("61-90 days", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("91-120 days", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("121-180 days", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("181+ days", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Total", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.BOTTOM));
        contentTable.setHeaderRows(1);
        double age30GrandTotal = 0d;
        double age60GrandTotal = 0d;
        double age90GrandTotal = 0d;
        double age120GrandTotal = 0d;
        double age180GrandTotal = 0d;
        double age181GrandTotal = 0d;
        double ageGrandTotal = 0d;
        double age30Total = 0d;
        double age60Total = 0d;
        double age90Total = 0d;
        double age120Total = 0d;
        double age180Total = 0d;
        double age181Total = 0d;
        double ageTotal = 0d;
        int rowIndex = 0;
        for (ReportBean transaction : transactions) {
            if (isAllVendors) {
                contentTable.addCell(createTextCell(StringUtils.left(transaction.getVendorName(), 25), blackNormalFont7, Rectangle.BOTTOM));
                contentTable.addCell(createTextCell(transaction.getVendorNumber(), blackNormalFont7, Rectangle.BOTTOM));
                contentTable.addCell(createTextCell(transaction.getEcuDesignation(), blackNormalFont7, Rectangle.BOTTOM));
            }
            contentTable.addCell(createTextCell(transaction.getInvoiceNumber(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(transaction.getInvoiceDate(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(transaction.getVendorReference(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createCell(transaction.getTransactionType(), blackNormalFont7, Element.ALIGN_CENTER, Rectangle.BOTTOM));
            contentTable.addCell(createAmountCell(transaction.getAge30Days(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createAmountCell(transaction.getAge60Days(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createAmountCell(transaction.getAge90Days(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createAmountCell(transaction.getAge120Days(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createAmountCell(transaction.getAge180Days(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createAmountCell(transaction.getAge181Days(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createAmountCell(transaction.getAgeTotal(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
            age30Total += NumberUtils.parseNumber(transaction.getAge30Days());
            age60Total += NumberUtils.parseNumber(transaction.getAge60Days());
            age90Total += NumberUtils.parseNumber(transaction.getAge90Days());
            age120Total += NumberUtils.parseNumber(transaction.getAge120Days());
            age180Total += NumberUtils.parseNumber(transaction.getAge180Days());
            age181Total += NumberUtils.parseNumber(transaction.getAge181Days());
            ageTotal += NumberUtils.parseNumber(transaction.getAgeTotal());
            boolean isTotaled = false;
            if (transactions.size() > rowIndex + 1) {
                ReportBean nextTransaction = transactions.get(rowIndex + 1);
                if (CommonUtils.isNotEqualIgnoreCase(transaction.getVendorNumber(), nextTransaction.getVendorNumber())) {
                    isTotaled = true;
                }
            } else {
                isTotaled = true;
            }
            if (isTotaled) {
                if (isAllVendors) {
                    contentTable.addCell(createEmptyCell(Rectangle.BOTTOM, LAVENDAR));
                    contentTable.addCell(createEmptyCell(Rectangle.BOTTOM, LAVENDAR));
                    contentTable.addCell(createEmptyCell(Rectangle.BOTTOM, LAVENDAR));
                }
                contentTable.addCell(createEmptyCell(Rectangle.BOTTOM, LAVENDAR));
                contentTable.addCell(createEmptyCell(Rectangle.BOTTOM, LAVENDAR));
                contentTable.addCell(createEmptyCell(Rectangle.BOTTOM, LAVENDAR));
                contentTable.addCell(createCell("Total", redBoldFont8, Element.ALIGN_RIGHT, Rectangle.BOTTOM, LAVENDAR));
                contentTable.addCell(createAmountCell(age30Total, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LAVENDAR));
                contentTable.addCell(createAmountCell(age60Total, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LAVENDAR));
                contentTable.addCell(createAmountCell(age90Total, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LAVENDAR));
                contentTable.addCell(createAmountCell(age120Total, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LAVENDAR));
                contentTable.addCell(createAmountCell(age180Total, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LAVENDAR));
                contentTable.addCell(createAmountCell(age181Total, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LAVENDAR));
                contentTable.addCell(createAmountCell(ageTotal, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LAVENDAR));
                age30GrandTotal += age30Total;
                age60GrandTotal += age60Total;
                age90GrandTotal += age90Total;
                age120GrandTotal += age120Total;
                age180GrandTotal += age180Total;
                age181GrandTotal += age181Total;
                ageGrandTotal += ageTotal;
                age30Total = 0d;
                age60Total = 0d;
                age90Total = 0d;
                age120Total = 0d;
                age180Total = 0d;
                age181Total = 0d;
                ageTotal = 0d;
                isTotaled = false;
            }
            rowIndex++;
        }
        if (isAllVendors) {
            contentTable.addCell(createEmptyCell(Rectangle.BOTTOM, LIGHT_ASH));
            contentTable.addCell(createEmptyCell(Rectangle.BOTTOM, LIGHT_ASH));
            contentTable.addCell(createEmptyCell(Rectangle.BOTTOM, LIGHT_ASH));
        }
        PdfPCell grandTotalCell = createCell("Grand Total", redBoldFont8, Element.ALIGN_RIGHT, Rectangle.BOTTOM, LIGHT_ASH);
        grandTotalCell.setColspan(4);
        contentTable.addCell(grandTotalCell);
        contentTable.addCell(createAmountCell(age30GrandTotal, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LIGHT_ASH));
        contentTable.addCell(createAmountCell(age60GrandTotal, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LIGHT_ASH));
        contentTable.addCell(createAmountCell(age90GrandTotal, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LIGHT_ASH));
        contentTable.addCell(createAmountCell(age120GrandTotal, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LIGHT_ASH));
        contentTable.addCell(createAmountCell(age180GrandTotal, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LIGHT_ASH));
        contentTable.addCell(createAmountCell(age181GrandTotal, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LIGHT_ASH));
        contentTable.addCell(createAmountCell(ageGrandTotal, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LIGHT_ASH));
    }

    private void writeSummarizedAgingContent(List<ReportBean> transactions, boolean isAllVendors) throws Exception {
        if (isAllVendors) {
            contentTable = new PdfPTable(11);
            contentTable.setWidthPercentage(100);
            contentTable.setWidths(new float[]{20f, 8f, 9f, 6f, 8f, 8f, 8f, 8f, 8f, 8f, 9f});
            contentTable.addCell(createHeaderCell("Vendor Name", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
            contentTable.addCell(createHeaderCell("Vendor Number", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
            contentTable.addCell(createHeaderCell("ECU Designation", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        } else {
            contentTable = new PdfPTable(8);
            contentTable.setWidthPercentage(100);
            contentTable.setWidths(new float[]{7f, 13f, 13f, 13f, 13f, 13f, 13f, 15f});
        }
        contentTable.addCell(createHeaderCell("Type", blackBoldFont8, Element.ALIGN_CENTER, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("0-30 days", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("31-60 days", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("61-90 days", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("91-120 days", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("121-180 days", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("181+ days", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Total", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.BOTTOM));
        contentTable.setHeaderRows(1);
        double age30GrandTotal = 0d;
        double age60GrandTotal = 0d;
        double age90GrandTotal = 0d;
        double age120GrandTotal = 0d;
        double age180GrandTotal = 0d;
        double age181GrandTotal = 0d;
        double ageGrandTotal = 0d;
        for (ReportBean transaction : transactions) {
            if (isAllVendors) {
                contentTable.addCell(createTextCell(transaction.getVendorName(), blackNormalFont7, Rectangle.BOTTOM));
                contentTable.addCell(createTextCell(transaction.getVendorNumber(), blackNormalFont7, Rectangle.BOTTOM));
                contentTable.addCell(createTextCell(transaction.getEcuDesignation(), blackNormalFont7, Rectangle.BOTTOM));
            }
            contentTable.addCell(createCell(transaction.getTransactionType(), blackNormalFont7, Element.ALIGN_CENTER, Rectangle.BOTTOM));
            contentTable.addCell(createAmountCell(transaction.getAge30Days(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createAmountCell(transaction.getAge60Days(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createAmountCell(transaction.getAge90Days(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createAmountCell(transaction.getAge120Days(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createAmountCell(transaction.getAge180Days(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createAmountCell(transaction.getAge181Days(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createAmountCell(transaction.getAgeTotal(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
            age30GrandTotal += NumberUtils.parseNumber(transaction.getAge30Days());
            age60GrandTotal += NumberUtils.parseNumber(transaction.getAge60Days());
            age90GrandTotal += NumberUtils.parseNumber(transaction.getAge90Days());
            age120GrandTotal += NumberUtils.parseNumber(transaction.getAge120Days());
            age180GrandTotal += NumberUtils.parseNumber(transaction.getAge180Days());
            age181GrandTotal += NumberUtils.parseNumber(transaction.getAge181Days());
            ageGrandTotal += NumberUtils.parseNumber(transaction.getAgeTotal());
        }
        if (isAllVendors) {
            contentTable.addCell(createEmptyCell(Rectangle.BOTTOM, LAVENDAR));
            contentTable.addCell(createEmptyCell(Rectangle.BOTTOM, LAVENDAR));
            contentTable.addCell(createEmptyCell(Rectangle.BOTTOM, LAVENDAR));
        }
        contentTable.addCell(createCell("Grand Total", redBoldFont8, Element.ALIGN_RIGHT, Rectangle.BOTTOM, LAVENDAR));
        contentTable.addCell(createAmountCell(age30GrandTotal, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LAVENDAR));
        contentTable.addCell(createAmountCell(age60GrandTotal, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LAVENDAR));
        contentTable.addCell(createAmountCell(age90GrandTotal, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LAVENDAR));
        contentTable.addCell(createAmountCell(age120GrandTotal, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LAVENDAR));
        contentTable.addCell(createAmountCell(age180GrandTotal, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LAVENDAR));
        contentTable.addCell(createAmountCell(age181GrandTotal, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LAVENDAR));
        contentTable.addCell(createAmountCell(ageGrandTotal, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LAVENDAR));
        PdfPCell listContentCell = createEmptyCell(Rectangle.BOX);
        listContentCell.addElement(contentTable);
        contentTable.addCell(listContentCell);
    }

    private void writeAgingContent() throws Exception {
        List<ReportBean> transactions = new ApReportsDAO().getAgingTransactions(apReportsForm);
        if (CommonUtils.isEqualIgnoreCase(apReportsForm.getShowAllCustomer(), YES) || CommonUtils.isEmpty(apReportsForm.getVendorNumber())) {
            if (CommonUtils.isEqualIgnoreCase(apReportsForm.getShowDetailOrSummary(), AP_DETAIL_REPORT)) {
                writeDetailedAgingContent(transactions, true);
            } else {
                writeSummarizedAgingContent(transactions, true);
            }
        } else {
            if (CommonUtils.isEqualIgnoreCase(apReportsForm.getShowDetailOrSummary(), AP_DETAIL_REPORT)) {
                writeDetailedAgingContent(transactions, false);
            } else {
                writeSummarizedAgingContent(transactions, false);
            }
        }
    }

    private void writeAdjustedAccrualsContent() throws Exception {
        List<ReportBean> adjustedAccruals = new ApReportsDAO().getAdjustedAccruals(apReportsForm);
        contentTable = new PdfPTable(8);
        contentTable.setWidthPercentage(100);
        contentTable.setWidths(new float[]{10, 10, 10, 30, 10, 10, 10, 10});
        contentTable.addCell(createHeaderCell("File", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Reporting Date", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Posted Date", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Vendor Name", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Vendor Number", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Original Amount", blackBoldFont7, Element.ALIGN_RIGHT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Assigned Amount", blackBoldFont7, Element.ALIGN_RIGHT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Difference", blackBoldFont7, Element.ALIGN_RIGHT, Rectangle.BOTTOM));
        contentTable.setHeaderRows(1);
        for (ReportBean adjustedAccrual : adjustedAccruals) {
            contentTable.addCell(createTextCell(adjustedAccrual.getFile(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(adjustedAccrual.getReportingDate(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(adjustedAccrual.getPostedDate(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(adjustedAccrual.getVendorName(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(adjustedAccrual.getVendorNumber(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createAmountCell(adjustedAccrual.getOriginalAmount(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createAmountCell(adjustedAccrual.getAssignedAmount(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createAmountCell(adjustedAccrual.getDifferenceAmount(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
        }
    }

    private void writeVendorContent() throws Exception {
        List<ReportBean> vendors = new ApReportsDAO().getVendors();
        contentTable = new PdfPTable(16);
        contentTable.setWidthPercentage(100);
        contentTable.setTotalWidth(new float[]{14.5f, 7.5f, 6.5f, 5.5f, 7f, 6f, 7f, 4f, 4f, 9f, 6f, 6f, 5f, 6f, 6f, 6f});
        contentTable.addCell(createHeaderCell("Vendor Name", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Vendor Number", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Sub Type", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("ECI Vendor", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("AP Specialist", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Payment Method", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Tin", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("W-9", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Credit", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Credit Terms", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Credit Limit", blackBoldFont7, Element.ALIGN_RIGHT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("AP Contact", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Not Paid", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Inactive", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Exempt from Inactivate", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Account Added Date", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.setHeaderRows(1);
        for (ReportBean vendor : vendors) {
            contentTable.addCell(createTextCell(StringUtils.left(vendor.getVendorName(), 25), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(vendor.getVendorNumber(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(vendor.getSubType(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(vendor.getEciVendorNumber(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(vendor.getApSpecialist(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(vendor.getPaymentMethod(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(vendor.getTin(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(vendor.getW9(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(vendor.getCreditStatus(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(vendor.getCreditTerms(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createAmountCell(vendor.getCreditLimit(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(vendor.getApContact(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(vendor.getNotPaid(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(vendor.getInactiveAccount(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(vendor.getExemptInactivate(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(vendor.getAccountAddedDate(), blackNormalFont7, Rectangle.BOTTOM));
        }
    }

    private void writeActivityContent() throws Exception {
        List<ReportBean> activities = new ApReportsDAO().getActivities(apReportsForm);
        contentTable = new PdfPTable(12);
        contentTable.setWidthPercentage(100);
        contentTable.setTotalWidth(new float[]{10, 18, 8, 10, 10, 8, 8, 6, 5, 6, 5, 6});
        contentTable.addCell(createHeaderCell("User", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Vendor Name", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Vendor Number", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Invoice Number", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("File", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Accrual Amount", blackBoldFont7, Element.ALIGN_RIGHT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Transaction Date", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Posted Date", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Difference", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Paid Date", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Difference", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Payment Method", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.setHeaderRows(1);
        for (ReportBean activity : activities) {
            contentTable.addCell(createTextCell(activity.getUserLoginName(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(activity.getVendorName(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(activity.getVendorNumber(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(activity.getInvoiceNumber(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(activity.getFile(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createAmountCell(activity.getAccrualAmount(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(activity.getTransactionDate(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(activity.getPostedDate(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(activity.getPostedTransactionDifference(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(activity.getPaidDate(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(activity.getPaidPostedDifference(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(activity.getPaymentMethod(), blackNormalFont7, Rectangle.BOTTOM));
        }
    }

    private void writeDetailedCheckRegistersContent(List<ReportBean> checkRegisters) throws Exception {
        contentTable = new PdfPTable(10);
        contentTable.setWidthPercentage(100);
        contentTable.setWidths(new int[]{8, 8, 20, 8, 12, 6, 11, 8, 8, 11});
        contentTable.addCell(createHeaderCell("Check Number", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Bank Account", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Payee Name", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Payee Number", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Invoice Number", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Pay Date", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Amount", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Payment Type", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Status", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Check Total", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.BOTTOM));
        contentTable.setHeaderRows(1);
        double grandTotal = 0d;
        double checkTotal = 0d;
        int rowIndex = 0;
        for (ReportBean checkRegister : checkRegisters) {
            contentTable.addCell(createTextCell(checkRegister.getCheckNumber(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(checkRegister.getBankAccount(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(checkRegister.getVendorName(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(checkRegister.getVendorNumber(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(checkRegister.getInvoiceNumber(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(checkRegister.getPaymentDate(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createAmountCell(checkRegister.getPaymentAmount(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(checkRegister.getPaymentMethod(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(checkRegister.getStatus(), blackNormalFont7, Rectangle.BOTTOM));
            checkTotal += Double.parseDouble(checkRegister.getPaymentAmount().replace(",", ""));
            boolean isTotaled = false;
            if (checkRegisters.size() > rowIndex + 1) {
                ReportBean nextTransaction = checkRegisters.get(rowIndex + 1);
                if (CommonUtils.isNotEqualIgnoreCase(checkRegister.getCheckNumber(), nextTransaction.getCheckNumber())) {
                    isTotaled = true;
                }
            } else {
                isTotaled = true;
            }
            if (isTotaled) {
                contentTable.addCell(createAmountCell(checkTotal, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
                grandTotal += checkTotal;
                isTotaled = false;
                checkTotal = 0d;
            } else {
                contentTable.addCell(createEmptyCell(Rectangle.BOTTOM));
            }
            rowIndex++;
        }
        contentTable.addCell(createEmptyCell(Rectangle.BOTTOM, DARK_ASH));
        contentTable.addCell(createEmptyCell(Rectangle.BOTTOM, DARK_ASH));
        contentTable.addCell(createEmptyCell(Rectangle.BOTTOM, DARK_ASH));
        contentTable.addCell(createEmptyCell(Rectangle.BOTTOM, DARK_ASH));
        contentTable.addCell(createEmptyCell(Rectangle.BOTTOM, DARK_ASH));
        contentTable.addCell(createCell("Grand Total", redBoldFont8, Element.ALIGN_RIGHT, Rectangle.BOTTOM, DARK_ASH));
        contentTable.addCell(createAmountCell(grandTotal, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, DARK_ASH));
        contentTable.addCell(createEmptyCell(Rectangle.BOTTOM, DARK_ASH));
        contentTable.addCell(createEmptyCell(Rectangle.BOTTOM, DARK_ASH));
        contentTable.addCell(createAmountCell(grandTotal, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, DARK_ASH));
    }

    private void writeSummarizedCheckRegistersContent(List<ReportBean> checkRegisters) throws Exception {
        contentTable = new PdfPTable(8);
        contentTable.setWidthPercentage(100);
        contentTable.setWidths(new int[]{10, 10, 25, 10, 10, 15, 10, 10});
        contentTable.addCell(createHeaderCell("Check Number", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Bank Account", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Payee Name", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Payee Number", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Pay Date", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Amount", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Payment Type", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Status", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.setHeaderRows(1);
        double grandTotal = 0d;
        for (ReportBean checkRegister : checkRegisters) {
            contentTable.addCell(createTextCell(checkRegister.getCheckNumber(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(checkRegister.getBankAccount(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(checkRegister.getVendorName(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(checkRegister.getVendorNumber(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(checkRegister.getPaymentDate(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createAmountCell(checkRegister.getPaymentAmount(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(checkRegister.getPaymentMethod(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(checkRegister.getStatus(), blackNormalFont7, Rectangle.BOTTOM));
            grandTotal += Double.parseDouble(checkRegister.getPaymentAmount().replace(",", ""));
        }
        contentTable.addCell(createEmptyCell(Rectangle.BOTTOM, DARK_ASH));
        contentTable.addCell(createEmptyCell(Rectangle.BOTTOM, DARK_ASH));
        contentTable.addCell(createEmptyCell(Rectangle.BOTTOM, DARK_ASH));
        contentTable.addCell(createEmptyCell(Rectangle.BOTTOM, DARK_ASH));
        contentTable.addCell(createCell("Grand Total", redBoldFont8, Element.ALIGN_RIGHT, Rectangle.BOTTOM, DARK_ASH));
        contentTable.addCell(createAmountCell(grandTotal, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, DARK_ASH));
        contentTable.addCell(createEmptyCell(Rectangle.BOTTOM, DARK_ASH));
        contentTable.addCell(createEmptyCell(Rectangle.BOTTOM, DARK_ASH));
    }

    private void writeCheckRegistersContent() throws Exception {
        List<ReportBean> checkRegisters = new ApReportsDAO().getCheckRegisterTransactions(apReportsForm);
        if (CommonUtils.isEqualIgnoreCase(apReportsForm.getShowDetailOrSummary(), AP_DETAIL_REPORT)) {
            writeDetailedCheckRegistersContent(checkRegisters);
        } else {
            writeSummarizedCheckRegistersContent(checkRegisters);
        }
    }

    private void writeDpoContent() throws Exception {
        ReportBean dpo = new ApReportsDAO().getDpo(apReportsForm);
        contentTable = new PdfPTable(4);
        contentTable.setWidthPercentage(100);
        contentTable.setWidths(new float[]{25, 25, 25, 25});
        contentTable.addCell(createHeaderCell("Total Amount Open Payables", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Total Amount Cost", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Selected Period", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("DPO ratio", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createTextCell(dpo.getOpenPayables(), blackNormalFont7, Rectangle.BOTTOM));
        contentTable.addCell(createTextCell(dpo.getTotalCosts(), blackNormalFont7, Rectangle.BOTTOM));
        contentTable.addCell(createTextCell(dpo.getNumberOfDays(), blackNormalFont7, Rectangle.BOTTOM));
        contentTable.addCell(createTextCell(dpo.getDpoRatio(), blackNormalFont7, Rectangle.BOTTOM));
    }

    private void writeDisputedItemsContent() throws Exception {
        List<ReportBean> disputedItems = new ApReportsDAO().getDisputedItems(apReportsForm);
        contentTable = new PdfPTable(15);
        contentTable.setWidthPercentage(100);
        contentTable.setWidths(new float[]{10, 6, 7, 5, 5, 5, 6, 5, 6, 6, 6, 5, 5, 5, 10});
        contentTable.addCell(createHeaderCell("Vendor Name", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Vendor Number", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Invoice Number", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Invoice Date", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Invoice Amount", blackBoldFont7, Element.ALIGN_RIGHT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Dock Receipt", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Voyage Number", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Accrual Amount", blackBoldFont7, Element.ALIGN_RIGHT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("AP Specialist", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Billing Terminal", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("User", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Disputed Date", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Resolved Date", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Resolution Period", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Comments", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.setHeaderRows(1);
        String invoiceAmount = "";
        int border = Rectangle.BOTTOM;
        int rowIndex = 0;
        boolean isNewVendorNumber = true;
        for (ReportBean disputedItem : disputedItems) {
            invoiceAmount = isNewVendorNumber ? disputedItem.getInvoiceAmount() : "";
            if (disputedItems.size() > rowIndex + 1) {
                ReportBean nextDisputedItem = disputedItems.get(rowIndex + 1);
                if (CommonUtils.isNotEqualIgnoreCase(disputedItem.getVendorNumber(), nextDisputedItem.getVendorNumber())
                        || CommonUtils.isNotEqualIgnoreCase(disputedItem.getInvoiceNumber(), nextDisputedItem.getInvoiceNumber())) {
                    border = Rectangle.BOTTOM;
                    isNewVendorNumber = true;
                } else {
                    border = Rectangle.NO_BORDER;
                    isNewVendorNumber = false;
                }
            } else {
                border = Rectangle.BOTTOM;
            }
            contentTable.addCell(createTextCell(StringUtils.left(disputedItem.getVendorName(), 15), blackNormalFont7, border));
            contentTable.addCell(createTextCell(disputedItem.getVendorNumber(), blackNormalFont7, border));
            contentTable.addCell(createTextCell(disputedItem.getInvoiceNumber(), blackNormalFont7, border));
            contentTable.addCell(createTextCell(disputedItem.getInvoiceDate(), blackNormalFont7, border));
            contentTable.addCell(createAmountCell(invoiceAmount, blackNormalFont7, redNormalFont7, border));
            contentTable.addCell(createTextCell(disputedItem.getDockReceipt(), blackNormalFont7, border));
            contentTable.addCell(createTextCell(disputedItem.getVoyageNumber(), blackNormalFont7, border));
            contentTable.addCell(createAmountCell(disputedItem.getAccrualAmount(), blackNormalFont7, redNormalFont7, border));
            String apSpecialist = null != disputedItem.getApSpecialist()? disputedItem.getApSpecialist() : "";
            contentTable.addCell(createTextCell(apSpecialist, blackNormalFont7, border));
            contentTable.addCell(createTextCell(disputedItem.getBillingTerminal(), blackNormalFont7, border));
            String userName = null != disputedItem.getUserLoginName()? disputedItem.getUserLoginName(): "";
            contentTable.addCell(createTextCell(userName, blackNormalFont7, border));
            contentTable.addCell(createTextCell(disputedItem.getDisputedDate(), blackNormalFont7, border));
            contentTable.addCell(createTextCell(disputedItem.getResolvedDate(), blackNormalFont7, border));
            contentTable.addCell(createCell(disputedItem.getResolutionPeriod(), blackNormalFont7, Element.ALIGN_CENTER, border));
            StringBuilder comments = new StringBuilder();
            if (CommonUtils.isNotEmpty(disputedItem.getNotesDescription())) {
                int count = 1;
                for (String comment : StringUtils.splitByWholeSeparator(disputedItem.getNotesDescription(), "<--->")) {
                    comments.append(count).append(") ").append(comment).append("\n");
                    count++;
                }
            }
            contentTable.addCell(createTextCell(comments.toString(), blackNormalFont7, border));
            rowIndex++;
        }
    }

    private void writeContent() throws Exception {
        if (CommonUtils.isEqualIgnoreCase(apReportsForm.getReportType(), AP_AGING_REPORT)) {
            writeAgingContent();
        } else if (CommonUtils.isEqual(apReportsForm.getReportType(), AP_ADJUSTED_ACCRUALS_REPORT)) {
            writeAdjustedAccrualsContent();
        } else if (CommonUtils.isEqual(apReportsForm.getReportType(), AP_VENDOR_REPORT)) {
            writeVendorContent();
        } else if (CommonUtils.isEqual(apReportsForm.getReportType(), AP_ACTIVITY_REPORT)) {
            writeActivityContent();
        } else if (CommonUtils.isEqual(apReportsForm.getReportType(), AP_CHECK_REGISTER_REPORT)) {
            writeCheckRegistersContent();
        } else if (CommonUtils.isEqual(apReportsForm.getReportType(), AP_DPO_REPORT)) {
            writeDpoContent();
        } else if (CommonUtils.isEqual(apReportsForm.getReportType(), AP_DISPUTED_ITEMS_REPORT)) {
            writeDisputedItemsContent();
        }
        if (null != contentTable) {
            document.add(contentTable);
        }
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

    public String createReport() throws Exception {
        StringBuilder fileName = new StringBuilder();
        fileName.append(LoadLogisoftProperties.getProperty("reportLocation")).append("/Documents/ApReports/");
        fileName.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
        File file = new File(fileName.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        fileName.append(apReportsForm.getReportType()).append(".pdf");
        init(fileName.toString());
        writeContent();
        exit();
        return fileName.toString();
    }
}
