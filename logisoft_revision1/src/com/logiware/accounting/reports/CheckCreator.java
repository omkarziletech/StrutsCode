package com.logiware.accounting.reports;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.hibernate.dao.CustAddressDAO;
import com.gp.cvst.logisoft.util.EnglishNumberToWords;
import com.logiware.accounting.form.ApPaymentForm;
import com.logiware.accounting.model.CostModel;
import com.logiware.accounting.model.InvoiceModel;
import com.logiware.accounting.model.PaymentModel;
import com.logiware.accounting.utils.InvoiceComparator;
import com.logiware.hibernate.dao.AccountingLedgerDAO;
import com.logiware.reports.BaseReportCreator;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author Lakshmi Narayanan
 */
public class CheckCreator extends BaseReportCreator {

    private static final Logger log = Logger.getLogger(CheckCreator.class);

    private ApPaymentForm form;
    private PaymentModel payment;

    public CheckCreator() {
    }

    public CheckCreator(ApPaymentForm form, PaymentModel payment, String contextPath) {
        this.form = form;
        this.payment = payment;
        this.contextPath = contextPath;
    }

    private void init(String checkFileName) throws DocumentException, FileNotFoundException {
        document = new Document(PageSize.LETTER);
        document.setMargins(15, 15, 10, 10);
        writer = PdfWriter.getInstance(document, new FileOutputStream(checkFileName));
        writer.setPdfVersion(PdfWriter.PDF_VERSION_1_7);
        document.open();
    }

    private String createBody(String checkNumber, String checkFileName) throws Exception {
        PdfPTable emptyTable = new PdfPTable(1);
        PdfPCell emptyCell = createEmptyCell(Rectangle.NO_BORDER);
        for (int i = 0; i < 12; i++) {
            emptyTable.addCell(emptyCell);
        }
        document.add(emptyTable);

        PdfPTable checkTable = new PdfPTable(2);
        checkTable.setWidthPercentage(100);
        checkTable.setWidths(new float[]{75, 25});
        checkTable.addCell(emptyCell);
        checkTable.addCell(createTextCell(StringUtils.leftPad(checkNumber + "  ", 20, ""), blackNormalFont10, Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, Rectangle.NO_BORDER));
        for (int i = 0; i < 6; i++) {
            checkTable.addCell(emptyCell);
        }
        checkTable.addCell(emptyCell);
        checkTable.addCell(createTextCell(StringUtils.leftPad(form.getPaymentDate() + "    ", 20, ""), blackNormalFont10, Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, Rectangle.NO_BORDER));
        document.add(checkTable);

        emptyTable = new PdfPTable(1);
        for (int i = 0; i < 2; i++) {
            emptyTable.addCell(emptyCell);
        }
        document.add(emptyTable);

        Double checkAmount = Double.parseDouble(payment.getPaymentAmount().replaceAll(",", ""));
        StringBuilder checkAmountInWords = new StringBuilder();
        String data = String.valueOf(NumberUtils.formatNumber(checkAmount));
        String[] amount = StringUtils.split(data, ".");
        String dollars = amount[0].replaceAll(",", "");
        if (Integer.parseInt(dollars) > 0) {
            checkAmountInWords.append(EnglishNumberToWords.convert(Integer.parseInt(dollars))).append(" DOLLARS ");
        }
        if (amount.length == 2) {
            String cents = amount[1].replaceAll(",", "");
            if (Integer.parseInt(cents) > 0) {
                checkAmountInWords.append(CommonUtils.isNotEmpty(checkAmountInWords) ? " AND " : "").append(cents).append(" CENTS");
            }
        }

        PdfPTable paymentTable = new PdfPTable(4);
        paymentTable.setWidthPercentage(100);
        paymentTable.setWidths(new float[]{5, 65, 10, 20});
        paymentTable.addCell(emptyCell);
        paymentTable.addCell(createTextCell(checkAmountInWords.toString(), blackNormalFont10, Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, Rectangle.NO_BORDER));
        paymentTable.addCell(emptyCell);
        paymentTable.addCell(emptyCell);
        paymentTable.addCell(emptyCell);
        paymentTable.addCell(emptyCell);
        paymentTable.addCell(emptyCell);
        paymentTable.addCell(createTextCell(StringUtils.leftPad("$" + NumberUtils.formatNumber(checkAmount) + "    ", 20, ""), blackNormalFont10, Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, Rectangle.NO_BORDER));
        PdfPCell spaceCell = createEmptyCell(Rectangle.NO_BORDER);
        spaceCell.setColspan(3);
        paymentTable.addCell(spaceCell);
        paymentTable.addCell(spaceCell);
        String checkAddress = new CustAddressDAO().getCheckAddress(payment.getVendorNumber());
        StringBuilder address = new StringBuilder();
        address.append(payment.getVendorName()).append("\n");
        address.append(CommonUtils.isNotEmpty(checkAddress) ? checkAddress : "");
        paymentTable.addCell(emptyCell);
        paymentTable.addCell(createTextCell(address.toString(), blackNormalFont9, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        paymentTable.addCell(emptyCell);
        paymentTable.addCell(emptyCell);
        document.add(paymentTable);

        emptyTable = new PdfPTable(1);
        for (int i = 0; i < 25; i++) {
            emptyTable.addCell(emptyCell);
        }
        document.add(emptyTable);

        PdfPTable vendorTable = new PdfPTable(3);
        vendorTable.setWidthPercentage(100);
        vendorTable.setWidths(new float[]{50, 30, 20});
        vendorTable.addCell(createTextCell("Vendor:(" + payment.getVendorNumber() + ")" + payment.getVendorName(), blackNormalFont7, Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, Rectangle.NO_BORDER));
        vendorTable.addCell(createTextCell("Check #: " + checkNumber, blackNormalFont7, Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, Rectangle.NO_BORDER));
        vendorTable.addCell(createTextCell(form.getPaymentDate(), blackNormalFont7, Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, Rectangle.NO_BORDER));

        PdfPTable invoicesTable = new PdfPTable(3);
        invoicesTable.setWidthPercentage(100);
        invoicesTable.setWidths(new float[]{40, 40, 20});
        emptyCell.setColspan(3);
        invoicesTable.addCell(emptyCell);

        PdfPTable invoicesTable1 = new PdfPTable(3);
        invoicesTable1.setWidthPercentage(100);
        invoicesTable1.setWidths(new float[]{50, 30, 20});

        PdfPTable invoicesTable2 = new PdfPTable(3);
        invoicesTable2.setWidthPercentage(100);
        invoicesTable2.setWidths(new float[]{50, 30, 20});

        int index = 0;
        boolean overflow = false;
        List<String> invoiceNumbers = new ArrayList<String>();
        List<InvoiceModel> invoiceList = form.getInvoiceList();
        Collections.sort(invoiceList, new InvoiceComparator());
        int limit = invoiceList.size() > 30 ? ((invoiceList.size() / 2) + (invoiceList.size() % 2 == 0 ? 0 : 1)) : 15;
        for (InvoiceModel invoice : invoiceList) {
            invoiceNumbers.add(invoice.getInvoiceOrBl());
            if (index < limit) {
                invoicesTable1.addCell(createTextCell(invoice.getInvoiceOrBl(), blackNormalFont7, Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, Rectangle.NO_BORDER));
                invoicesTable1.addCell(createTextCell(invoice.getInvoiceDate(), blackNormalFont7, Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, Rectangle.NO_BORDER));
                invoicesTable1.addCell(createTextCell(invoice.getInvoiceAmount(), blackNormalFont7, Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, Rectangle.NO_BORDER));
            } else {
                invoicesTable2.addCell(createTextCell(invoice.getInvoiceOrBl(), blackNormalFont7, Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, Rectangle.NO_BORDER));
                invoicesTable2.addCell(createTextCell(invoice.getInvoiceDate(), blackNormalFont7, Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, Rectangle.NO_BORDER));
                invoicesTable2.addCell(createTextCell(invoice.getInvoiceAmount(), blackNormalFont7, Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, Rectangle.NO_BORDER));
            }
            index++;
        }
        if (invoiceList.size() > 30) {
            overflow = true;
        } else {
            PdfPCell cell = createTextCell(" ", blackNormalFont7, Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, Rectangle.NO_BORDER);
            cell.setColspan(3);
            for (int l = index; l < 15; l++) {
                invoicesTable1.addCell(cell);
            }
        }
        PdfPCell invoicesTable1Cell = createEmptyCell(Rectangle.NO_BORDER);
        invoicesTable1Cell.addElement(invoicesTable1);
        invoicesTable.addCell(invoicesTable1Cell);

        PdfPCell invoicesTable2Cell = createEmptyCell(Rectangle.NO_BORDER);
        invoicesTable2Cell.addElement(invoicesTable2);
        invoicesTable.addCell(invoicesTable2Cell);

        emptyCell.setColspan(1);
        invoicesTable.addCell(emptyCell);
        invoicesTable.addCell(emptyCell);
        invoicesTable.addCell(emptyCell);
        invoicesTable.addCell(createTextCell(StringUtils.leftPad("$" + NumberUtils.formatNumber(checkAmount) + "  ", 20, ""), blackNormalFont10, Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, Rectangle.NO_BORDER));

        List<CostModel> costsList = new AccountingLedgerDAO().getCostsForInvoices(payment.getVendorNumber(), invoiceNumbers);

        PdfPTable costsTable = new PdfPTable(3);
        costsTable.setWidthPercentage(100);
        costsTable.setWidths(new float[]{40, 40, 20});
        emptyCell.setColspan(3);
        costsTable.addCell(emptyCell);

        PdfPTable costsTable1 = new PdfPTable(4);
        costsTable1.setWidthPercentage(100);
        costsTable1.setWidths(new float[]{40, 20, 20, 20});

        PdfPTable costsTable2 = new PdfPTable(4);
        costsTable2.setWidthPercentage(100);
        costsTable2.setWidths(new float[]{40, 20, 20, 20});
        
        index = 0;
        limit = costsList.size() > 30 ? ((costsList.size() / 2) + (costsList.size() % 2 == 0 ? 0 : 1)) : 15;
        for (CostModel cost : costsList) {
            if (index < limit) {
                costsTable1.addCell(createTextCell(cost.getInvoiceNumber(), blackNormalFont5, Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, Rectangle.NO_BORDER));
                costsTable1.addCell(createTextCell(cost.getCostDate(), blackNormalFont5, Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, Rectangle.NO_BORDER));
                costsTable1.addCell(createTextCell(cost.getCostCode(), blackNormalFont5, Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, Rectangle.NO_BORDER));
                costsTable1.addCell(createTextCell(cost.getCostAmount(), blackNormalFont5, Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, Rectangle.NO_BORDER));
            } else {
                costsTable2.addCell(createTextCell(cost.getInvoiceNumber(), blackNormalFont5, Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, Rectangle.NO_BORDER));
                costsTable2.addCell(createTextCell(cost.getCostDate(), blackNormalFont5, Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, Rectangle.NO_BORDER));
                costsTable2.addCell(createTextCell(cost.getCostCode(), blackNormalFont5, Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, Rectangle.NO_BORDER));
                costsTable2.addCell(createTextCell(cost.getCostAmount(), blackNormalFont5, Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, Rectangle.NO_BORDER));
            }
            index++;
        }
        if (costsList.size() > 30) {
            overflow = true;
        } else {
            PdfPCell cell = createTextCell(" ", blackNormalFont5, Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, Rectangle.NO_BORDER);
            cell.setColspan(4);
            for (int l = index; l < 15; l++) {
                costsTable1.addCell(cell);
            }
        }

        PdfPCell costsTable1Cell = createEmptyCell(Rectangle.NO_BORDER);
        costsTable1Cell.addElement(costsTable1);
        costsTable.addCell(costsTable1Cell);

        PdfPCell costsTable2Cell = createEmptyCell(Rectangle.NO_BORDER);
        costsTable2Cell.addElement(costsTable2);
        costsTable.addCell(costsTable2Cell);

        emptyCell.setColspan(1);
        costsTable.addCell(emptyCell);
        costsTable.addCell(emptyCell);
        costsTable.addCell(emptyCell);
        costsTable.addCell(createTextCell(StringUtils.leftPad("$" + NumberUtils.formatNumber(checkAmount) + "  ", 20, ""), blackNormalFont10, Element.ALIGN_LEFT, Element.ALIGN_JUSTIFIED, Rectangle.NO_BORDER));
        String overflowFileName = null;
        if (overflow) {
            document.add(vendorTable);
            PdfPTable overflowTable = new PdfPTable(1);
            overflowTable.setWidthPercentage(100);
            spaceCell.setColspan(1);
            for (int l = 0; l < 10; l++) {
                overflowTable.addCell(spaceCell);
            }
            overflowTable.addCell(createTextCell("See Attached....", blackNormalFont11, Element.ALIGN_MIDDLE, Element.ALIGN_JUSTIFIED, Rectangle.NO_BORDER));
            for (int l = 0; l < 16; l++) {
                overflowTable.addCell(spaceCell);
            }
            document.add(overflowTable);
            document.close();
            overflowFileName = FilenameUtils.getFullPath(checkFileName) + "Overflow_" + FilenameUtils.getBaseName(checkFileName) + ".pdf";
            document = new Document(PageSize.LETTER);
            document.setMargins(15, 15, 10, 10);
            PdfWriter.getInstance(document, new FileOutputStream(overflowFileName));
            document.open();
        }
        document.add(vendorTable);
        document.add(invoicesTable);
        emptyTable = new PdfPTable(1);
        emptyCell.setColspan(1);
        for (int l = 0; l < (overflow ? 10 : 23); l++) {
            emptyTable.addCell(emptyCell);
        }
        document.add(emptyTable);
        document.add(vendorTable);
        document.add(costsTable);
        return overflowFileName;
    }

    private void destroy() {
        document.close();
    }

    public String[] create(String checkNumber) throws Exception {
        try {
            StringBuilder checkFileName = new StringBuilder();
            checkFileName.append(LoadLogisoftProperties.getProperty("reportLocation"));
            checkFileName.append("/Documents/ApPayment/");
            checkFileName.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
            File file = new File(checkFileName.toString());
            if (!file.exists()) {
                file.mkdirs();
            }
            checkFileName.append(checkNumber).append(".pdf");
            init(checkFileName.toString());
            String overflowFileName = createBody(checkNumber, checkFileName.toString());
            destroy();
            return new String[]{checkFileName.toString(), overflowFileName};
        } catch (Exception e) {
            log.info("createReport failed on " + new Date(), e);
            throw e;
        } finally {
            if (null != document && document.isOpen()) {
                destroy();
            }
        }

    }
}
