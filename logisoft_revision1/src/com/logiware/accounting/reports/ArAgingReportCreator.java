package com.logiware.accounting.reports;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.accounting.dao.ArReportsDAO;
import com.logiware.accounting.form.ArReportsForm;
import com.logiware.bean.CustomerBean;
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
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author Lakshmi Naryanan
 */
public class ArAgingReportCreator extends BaseReportCreator {

    private static final Logger log = Logger.getLogger(ArAgingReportCreator.class);
    private ArReportsForm arReportsForm;
    private CustomerBean customer;

    public ArAgingReportCreator() {
    }

    public ArAgingReportCreator(ArReportsForm arReportsForm, CustomerBean customer, String contextPath) {
        this.arReportsForm = arReportsForm;
        this.contextPath = contextPath;
        this.customer = customer;
    }

    private void init(String fileName) throws Exception {
        document = new Document(PageSize.A4.rotate());
        document.setMargins(5, 5, 5, 30);
        writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
        writer.setPdfVersion(PdfWriter.PDF_VERSION_1_7);
        writer.setUserunit(1f);
        writer.setPageEvent(new ArAgingReportCreator(arReportsForm, customer, contextPath));
        document.open();
        writer.setOpenAction(PdfAction.gotoLocalPage(1, new PdfDestination(PdfDestination.XYZ, -1, -1, 1f), writer));
    }

    private void writeHeader() throws Exception {
        headerTable = new PdfPTable(2);
        headerTable.setWidths(new float[]{50, 50});
        headerTable.setWidthPercentage(100);
        String imagePath = LoadLogisoftProperties.getProperty("application.image.logo");
        Image image = Image.getInstance(contextPath + imagePath);
        image.scalePercent(75);
        PdfPCell logoCell = createImageCell(image);
        logoCell.setColspan(2);
        headerTable.addCell(logoCell);
        PdfPCell titleCell = createCell("AR Aging Report", headerBoldFont15, Element.ALIGN_CENTER, Rectangle.BOX, Color.LIGHT_GRAY);
        titleCell.setColspan(2);
        headerTable.addCell(titleCell);
        if (CommonUtils.isNotEmpty(arReportsForm.getCollector())) {
            PdfPCell cell = createCell("For Collector : " + arReportsForm.getCollector(), headerBoldFont11, Element.ALIGN_LEFT, Rectangle.NO_BORDER);
            cell.setColspan(2);
            headerTable.addCell(cell);
        } else if (arReportsForm.isAllCustomers()) {
            PdfPCell cell = createCell("For ALL Customers", headerBoldFont11, Element.ALIGN_LEFT, Rectangle.NO_BORDER);
            cell.setColspan(2);
            headerTable.addCell(cell);
        }
        if (arReportsForm.isAllPayments()) {
            PdfPCell cell = createCell("Includes All Payments Regardless of Date", headerBoldFont11, Element.ALIGN_LEFT, Rectangle.NO_BORDER);
            cell.setColspan(2);
            headerTable.addCell(cell);
        }
        PdfPTable subHeaderTable1 = new PdfPTable(2);
        subHeaderTable1.setWidthPercentage(100);
        subHeaderTable1.setWidths(new float[]{30, 70});
        PdfPTable subHeaderTable2 = new PdfPTable(2);
        subHeaderTable2.setWidthPercentage(100);
        subHeaderTable2.setWidths(new float[]{30, 70});
        subHeaderTable1.addCell(createTextCell("Report Type :", blackBoldFont10, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
        if (CommonUtils.isEqual(arReportsForm.getReportType(), ConstantsInterface.SUMMARY)) {
            subHeaderTable1.addCell(createTextCell("Summary", blackNormalFont10, Rectangle.NO_BORDER));
        } else {
            subHeaderTable1.addCell(createTextCell("Detail", blackNormalFont10, Rectangle.NO_BORDER));
        }

        subHeaderTable2.addCell(createTextCell("Cut-off Date :", blackBoldFont10, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
        subHeaderTable2.addCell(createTextCell(arReportsForm.getCutOffDate(), blackNormalFont10, Rectangle.NO_BORDER));

        if (arReportsForm.isAllCustomers() || CommonUtils.isNotEmpty(arReportsForm.getCollector())) {
            if (CommonUtils.isNotEmpty(arReportsForm.getCustomerFromRange())) {
                subHeaderTable1.addCell(createTextCell("Customer Range :", blackBoldFont10, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
                String customerRange = arReportsForm.getCustomerFromRange();
                if (CommonUtils.isNotEmpty(arReportsForm.getCustomerToRange())) {
                    customerRange += " - " + arReportsForm.getCustomerToRange();
                }
                subHeaderTable1.addCell(createTextCell(customerRange, blackNormalFont10, Rectangle.NO_BORDER));
            }
        } else if (CommonUtils.isNotEmpty(arReportsForm.getCustomerNumber())) {
            subHeaderTable1.addCell(createTextCell("Customer Name :", blackBoldFont10, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
            subHeaderTable1.addCell(createTextCell(customer.getCustomerName(), blackNormalFont10, Rectangle.NO_BORDER));
            subHeaderTable1.addCell(createTextCell("Customer Number :", blackBoldFont10, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
            subHeaderTable1.addCell(createTextCell(customer.getCustomerNumber(), blackNormalFont10, Rectangle.NO_BORDER));
            subHeaderTable1.addCell(createTextCell("Blue Screen Account :", blackBoldFont10, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
            subHeaderTable1.addCell(createTextCell(customer.getBlueScreenAccount(), blackNormalFont10, Rectangle.NO_BORDER));
            subHeaderTable1.addCell(createTextCell("ECU Designation :", blackBoldFont10, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
            subHeaderTable1.addCell(createTextCell(customer.getEcuDesignation(), blackNormalFont10, Rectangle.NO_BORDER));
            subHeaderTable1.addCell(createTextCell("Master Account :", blackBoldFont10, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
            subHeaderTable1.addCell(createTextCell(customer.getMaster(), blackNormalFont10, Rectangle.NO_BORDER));

            subHeaderTable2.addCell(createTextCell("Address :", blackBoldFont10, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
            subHeaderTable2.addCell(createTextCell(customer.getAddress(), blackNormalFont10, Rectangle.NO_BORDER));
            if (CommonUtils.isNotEmpty(customer.getPhone())) {
                subHeaderTable2.addCell(createTextCell("Phone :", blackBoldFont10, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
                subHeaderTable2.addCell(createTextCell(customer.getPhone(), blackNormalFont10, Rectangle.NO_BORDER));
            }
            if (CommonUtils.isNotEmpty(customer.getFax())) {
                subHeaderTable2.addCell(createTextCell("Fax :", blackBoldFont10, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
                subHeaderTable2.addCell(createTextCell(customer.getFax(), blackNormalFont10, Rectangle.NO_BORDER));
            }
            if (CommonUtils.isNotEmpty(customer.getEmail())) {
                subHeaderTable2.addCell(createTextCell("Email :", blackBoldFont10, Element.ALIGN_RIGHT, Rectangle.NO_BORDER));
                subHeaderTable2.addCell(createTextCell(customer.getEmail(), blackNormalFont10, Rectangle.NO_BORDER));
            }
        }

        PdfPCell subHeaderCell1 = createEmptyCell(Rectangle.BOTTOM);
        subHeaderCell1.addElement(subHeaderTable1);
        headerTable.addCell(subHeaderCell1);

        PdfPCell subHeaderCell2 = createEmptyCell(Rectangle.BOTTOM);
        subHeaderCell2.addElement(subHeaderTable2);
        headerTable.addCell(subHeaderCell2);
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

    private void writeSummaryContents(List<ReportBean> transactions) throws Exception {
        contentTable = new PdfPTable(16);
        contentTable.setWidths(new float[]{6, 5, 6, 7, 11, 6, 5, 6, 6, 6, 6, 7, 7, 7, 7, 7});
        contentTable.setWidthPercentage(100);
        contentTable.addCell(createHeaderCell("Master #", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Bluescreen #", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("ECU Designation", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Customer Number", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Customer Name", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Collector", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Sales Code", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Credit Status", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Credit Limit", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("0-30 days", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("31-60 days", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("61-90 days", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("91-120 days", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("121-180 days", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("181+ days", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Total", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.BOTTOM));
        PdfPCell lineCell = createEmptyCell(Rectangle.BOTTOM);
        lineCell.setFixedHeight(1f);
        lineCell.setColspan(16);
        contentTable.addCell(lineCell);
        contentTable.setHeaderRows(2);
        contentTable.setFooterRows(1);
        double age30Days = 0d;
        double age60Days = 0d;
        double age90Days = 0d;
        double age120Days = 0d;
        double age180Days = 0d;
        double age181Days = 0d;
        double ageTotal = 0d;
        for (ReportBean transaction : transactions) {
            contentTable.addCell(createTextCell(transaction.getMasterAccountNumber(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(transaction.getBlueScreenAccount(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(transaction.getEcuDesignation(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(transaction.getCustomerNumber(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(StringUtils.left(transaction.getCustomerName(), 30), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(transaction.getCollector(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(transaction.getSalesCode(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(transaction.getCreditStatus(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(transaction.getCreditLimit(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createAmountCell(transaction.getAge30Days(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createAmountCell(transaction.getAge60Days(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createAmountCell(transaction.getAge90Days(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createAmountCell(transaction.getAge120Days(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createAmountCell(transaction.getAge180Days(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createAmountCell(transaction.getAge181Days(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createAmountCell(transaction.getAgeTotal(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
            age30Days += Double.parseDouble(transaction.getAge30Days().replace(",", ""));
            age60Days += Double.parseDouble(transaction.getAge60Days().replace(",", ""));
            age90Days += Double.parseDouble(transaction.getAge90Days().replace(",", ""));
            age120Days += Double.parseDouble(transaction.getAge120Days().replace(",", ""));
            age180Days += Double.parseDouble(transaction.getAge180Days().replace(",", ""));
            age181Days += Double.parseDouble(transaction.getAge181Days().replace(",", ""));
            ageTotal += Double.parseDouble(transaction.getAgeTotal().replace(",", ""));
        }
        PdfPCell totalCell = createCell("Total", redBoldFont8, Element.ALIGN_RIGHT, Rectangle.BOTTOM, LAVENDAR);
        totalCell.setColspan(9);
        contentTable.addCell(totalCell);
        contentTable.addCell(createAmountCell(age30Days, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LAVENDAR));
        contentTable.addCell(createAmountCell(age60Days, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LAVENDAR));
        contentTable.addCell(createAmountCell(age90Days, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LAVENDAR));
        contentTable.addCell(createAmountCell(age120Days, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LAVENDAR));
        contentTable.addCell(createAmountCell(age180Days, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LAVENDAR));
        contentTable.addCell(createAmountCell(age181Days, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LAVENDAR));
        contentTable.addCell(createAmountCell(ageTotal, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LAVENDAR));
    }

    private void writeDetailContents(List<ReportBean> transactions) throws Exception {
        contentTable = new PdfPTable(20);
        contentTable.setWidths(new float[]{6, 5, 6, 7, 10, 6, 4, 4, 5, 7, 5, 6, 6, 6, 6, 6, 6, 6, 7, 5});
        contentTable.setWidthPercentage(100);
        contentTable.getDefaultCell().setLeading(0.5f, 0f);
        contentTable.addCell(createHeaderCell("Master #", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Blue Screen #", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("ECU Designation", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Customer Number", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Customer Name", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Collector", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Sales Code", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Credit Status", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Billing Terminal", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Invoice-B/L-DR #", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Date", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Ref #", blackBoldFont8, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("0-30 days", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("31-60 days", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("61-90 days", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("91-120 days", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("121-180 days", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("181+ days", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Total", blackBoldFont8, Element.ALIGN_RIGHT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Voyage", blackBoldFont8, Element.ALIGN_CENTER, Rectangle.BOTTOM));
        PdfPCell lineCell = createEmptyCell(Rectangle.BOTTOM);
        lineCell.setFixedHeight(1f);
        lineCell.setColspan(20);
        contentTable.addCell(lineCell);
        contentTable.setHeaderRows(2);
        contentTable.setFooterRows(1);
        double age30Days = 0d;
        double age60Days = 0d;
        double age90Days = 0d;
        double age120Days = 0d;
        double age180Days = 0d;
        double age181Days = 0d;
        double ageTotal = 0d;
        double subAge30Days = 0d;
        double subAge60Days = 0d;
        double subAge90Days = 0d;
        double subAge120Days = 0d;
        double subAge180Days = 0d;
        double subAge181Days = 0d;
        double subAgeTotal = 0d;
        String previousCustomer = null;
        for (ReportBean transaction : transactions) {
            if (null != previousCustomer && CommonUtils.isNotEqualIgnoreCase(previousCustomer, transaction.getCustomerNumber())) {
                contentTable.addCell(lineCell);
                PdfPCell subTotalCell = createCell("Total", redBoldFont8, Element.ALIGN_RIGHT, Rectangle.BOTTOM, LIGHT_ASH);
                subTotalCell.setColspan(12);
                contentTable.addCell(subTotalCell);
                contentTable.addCell(createAmountCell(subAge30Days, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LIGHT_ASH));
                contentTable.addCell(createAmountCell(subAge60Days, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LIGHT_ASH));
                contentTable.addCell(createAmountCell(subAge90Days, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LIGHT_ASH));
                contentTable.addCell(createAmountCell(subAge120Days, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LIGHT_ASH));
                contentTable.addCell(createAmountCell(subAge180Days, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LIGHT_ASH));
                contentTable.addCell(createAmountCell(subAge181Days, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LIGHT_ASH));
                contentTable.addCell(createAmountCell(subAgeTotal, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LIGHT_ASH));
                contentTable.addCell(createEmptyCell(Rectangle.BOTTOM, LIGHT_ASH));
                subAge30Days = 0d;
                subAge60Days = 0d;
                subAge90Days = 0d;
                subAge120Days = 0d;
                subAge180Days = 0d;
                subAge181Days = 0d;
                subAgeTotal = 0d;
            }
            previousCustomer = transaction.getCustomerNumber();
            contentTable.addCell(createTextCell(transaction.getMasterAccountNumber(), blackNormalFont7, Rectangle.NO_BORDER));
            contentTable.addCell(createTextCell(transaction.getBlueScreenAccount(), blackNormalFont7, Rectangle.NO_BORDER));
            contentTable.addCell(createTextCell(transaction.getEcuDesignation(), blackNormalFont7, Rectangle.NO_BORDER));
            contentTable.addCell(createTextCell(transaction.getCustomerNumber(), blackNormalFont7, Rectangle.NO_BORDER));
            contentTable.addCell(createTextCell(StringUtils.left(transaction.getCustomerName(), 15), blackNormalFont7, Rectangle.NO_BORDER));
            contentTable.addCell(createTextCell(transaction.getCollector(), blackNormalFont7, Rectangle.NO_BORDER));
            contentTable.addCell(createTextCell(transaction.getSalesCode(), blackNormalFont7, Rectangle.NO_BORDER));
            contentTable.addCell(createTextCell(transaction.getCreditStatus(), blackNormalFont7, Rectangle.NO_BORDER));
            contentTable.addCell(createTextCell(transaction.getBillingTerminal(), blackNormalFont7, Rectangle.NO_BORDER));
            contentTable.addCell(createTextCell(transaction.getInvoiceOrBl(), blackNormalFont7, Rectangle.NO_BORDER));
            contentTable.addCell(createTextCell(transaction.getInvoiceDate(), blackNormalFont7, Rectangle.NO_BORDER));
            contentTable.addCell(createTextCell(transaction.getCustomerReference(), blackNormalFont7, Rectangle.NO_BORDER));
            contentTable.addCell(createAmountCell(transaction.getAge30Days(), blackNormalFont7, redNormalFont7, Rectangle.NO_BORDER));
            contentTable.addCell(createAmountCell(transaction.getAge60Days(), blackNormalFont7, redNormalFont7, Rectangle.NO_BORDER));
            contentTable.addCell(createAmountCell(transaction.getAge90Days(), blackNormalFont7, redNormalFont7, Rectangle.NO_BORDER));
            contentTable.addCell(createAmountCell(transaction.getAge120Days(), blackNormalFont7, redNormalFont7, Rectangle.NO_BORDER));
            contentTable.addCell(createAmountCell(transaction.getAge180Days(), blackNormalFont7, redNormalFont7, Rectangle.NO_BORDER));
            contentTable.addCell(createAmountCell(transaction.getAge181Days(), blackNormalFont7, redNormalFont7, Rectangle.NO_BORDER));
            contentTable.addCell(createAmountCell(transaction.getAgeTotal(), blackNormalFont7, redNormalFont7, Rectangle.NO_BORDER));
            contentTable.addCell(createTextCell(transaction.getVoyage(), blackNormalFont7, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
            subAge30Days += Double.parseDouble(transaction.getAge30Days().replace(",", ""));
            subAge60Days += Double.parseDouble(transaction.getAge60Days().replace(",", ""));
            subAge90Days += Double.parseDouble(transaction.getAge90Days().replace(",", ""));
            subAge120Days += Double.parseDouble(transaction.getAge120Days().replace(",", ""));
            subAge180Days += Double.parseDouble(transaction.getAge180Days().replace(",", ""));
            subAge181Days += Double.parseDouble(transaction.getAge181Days().replace(",", ""));
            subAgeTotal += Double.parseDouble(transaction.getAgeTotal().replace(",", ""));
            age30Days += Double.parseDouble(transaction.getAge30Days().replace(",", ""));
            age60Days += Double.parseDouble(transaction.getAge60Days().replace(",", ""));
            age90Days += Double.parseDouble(transaction.getAge90Days().replace(",", ""));
            age120Days += Double.parseDouble(transaction.getAge120Days().replace(",", ""));
            age180Days += Double.parseDouble(transaction.getAge180Days().replace(",", ""));
            age181Days += Double.parseDouble(transaction.getAge181Days().replace(",", ""));
            ageTotal += Double.parseDouble(transaction.getAgeTotal().replace(",", ""));
        }
        contentTable.addCell(lineCell);
        PdfPCell subTotalCell = createCell("Total", redBoldFont8, Element.ALIGN_RIGHT, Rectangle.BOTTOM, LIGHT_ASH);
        subTotalCell.setColspan(12);
        contentTable.addCell(subTotalCell);
        contentTable.addCell(createAmountCell(subAge30Days, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LIGHT_ASH));
        contentTable.addCell(createAmountCell(subAge60Days, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LIGHT_ASH));
        contentTable.addCell(createAmountCell(subAge90Days, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LIGHT_ASH));
        contentTable.addCell(createAmountCell(subAge120Days, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LIGHT_ASH));
        contentTable.addCell(createAmountCell(subAge180Days, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LIGHT_ASH));
        contentTable.addCell(createAmountCell(subAge181Days, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LIGHT_ASH));
        contentTable.addCell(createAmountCell(subAgeTotal, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LIGHT_ASH));
        contentTable.addCell(createEmptyCell(Rectangle.BOTTOM, LIGHT_ASH));
        contentTable.addCell(lineCell);
        PdfPCell totalCell = createCell("Grand Total", redBoldFont8, Element.ALIGN_RIGHT, Rectangle.BOTTOM, LAVENDAR);
        totalCell.setColspan(12);
        contentTable.addCell(totalCell);
        contentTable.addCell(createAmountCell(age30Days, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LAVENDAR));
        contentTable.addCell(createAmountCell(age60Days, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LAVENDAR));
        contentTable.addCell(createAmountCell(age90Days, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LAVENDAR));
        contentTable.addCell(createAmountCell(age120Days, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LAVENDAR));
        contentTable.addCell(createAmountCell(age180Days, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LAVENDAR));
        contentTable.addCell(createAmountCell(age181Days, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LAVENDAR));
        contentTable.addCell(createAmountCell(ageTotal, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LAVENDAR));
        contentTable.addCell(createEmptyCell(Rectangle.BOTTOM, LAVENDAR));
    }

    private void writeContent() throws Exception {
        List<ReportBean> transactions = new ArReportsDAO().getAgingTransactions(arReportsForm);
        if (CommonUtils.isEqual(arReportsForm.getReportType(), ConstantsInterface.SUMMARY)) {
            writeSummaryContents(transactions);
        } else {
            writeDetailContents(transactions);
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
        StringBuilder fileNameBuilder = new StringBuilder();
        fileNameBuilder.append(LoadLogisoftProperties.getProperty("reportLocation")).append("/Documents/ArAgingReport/");
        fileNameBuilder.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
        File file = new File(fileNameBuilder.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        if (CommonUtils.isNotEmpty(arReportsForm.getCollector())) {
            fileNameBuilder.append("Collector");
        } else if (arReportsForm.isAllCustomers()) {
            fileNameBuilder.append("AllCustomers");
        } else {
            fileNameBuilder.append(arReportsForm.getCustomerNumber());
        }
        fileNameBuilder.append(".pdf");
        init(fileNameBuilder.toString());
        writeContent();
        exit();
        return fileNameBuilder.toString();
    }
}
