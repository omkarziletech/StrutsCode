package com.logiware.accounting.excel;

import com.gp.cong.common.DateUtils;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.accounting.form.CheckRegisterForm;
import com.logiware.accounting.model.InvoiceModel;
import com.logiware.excel.BaseExcelCreator;
import java.io.File;
import java.util.Date;

/**
 *
 * @author Lakshmi Narayanan
 */
public class CheckRegisterExcelCreator extends BaseExcelCreator {

    private CheckRegisterForm form;

    public CheckRegisterExcelCreator() {
    }

    public CheckRegisterExcelCreator(CheckRegisterForm form) {
        this.form = form;
    }

    private void writeHeader() throws Exception {
        createRow();
        createHeaderCell("Payment Detail Report", headerCellStyleLeftBold);
        mergeCells(rowIndex, rowIndex, 0, 1);
        row.setHeightInPoints(20);
        createRow();
        resetColumnIndex();
        createHeaderCell("Date: ", subHeaderOneCellStyleLeftBold);
        createHeaderCell(DateUtils.formatDate(new Date(), "MM/dd/yyyy"), subHeaderOneCellStyleLeftNormal);
        createRow();
        resetColumnIndex();
        createHeaderCell("Pay To: ", subHeaderTwoCellStyleLeftBold);
        createHeaderCell(form.getVendorName() + "(" + form.getVendorNumber() + ")", subHeaderTwoCellStyleLeftNormal);
        createRow();
        resetColumnIndex();
        createHeaderCell("Payment Method: ", subHeaderOneCellStyleLeftBold);
        createHeaderCell(form.getPaymentMethod(), subHeaderOneCellStyleLeftNormal);
        createRow();
        resetColumnIndex();
        createHeaderCell("Payment Date: ", subHeaderTwoCellStyleLeftBold);
        createHeaderCell(form.getPaymentDate(), subHeaderTwoCellStyleLeftNormal);
        createRow();
        resetColumnIndex();
        createHeaderCell("Check #: ", subHeaderOneCellStyleLeftBold);
        createHeaderCell(form.getCheckNumber(), subHeaderOneCellStyleLeftNormal);
        createRow();
        resetColumnIndex();
        createHeaderCell("Invoice/BL", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 40);
        createHeaderCell("Amount", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 60);
        sheet.createFreezePane(0, 7);
    }

    private void writeContent() throws Exception {
        int rowCount = 0;
        for (InvoiceModel invoice : form.getInvoiceList()) {
            createRow();
            resetColumnIndex();
            if (rowCount % 2 == 0) {
                createTextCell(invoice.getInvoiceOrBl(), tableEvenRowCellStyleLeftNormal);
                createAmountCell(invoice.getInvoiceAmount(), tableEvenRowCellStyleRightNormal);
            } else {
                createTextCell(invoice.getInvoiceOrBl(), tableOddRowCellStyleLeftNormal);
                createAmountCell(invoice.getInvoiceAmount(), tableOddRowCellStyleRightNormal);
            }
            rowCount++;
        }
        setColumnAutoSize();
    }

    public String create() throws Exception {
        try {
            StringBuilder fileNameBuilder = new StringBuilder();
            fileNameBuilder.append(LoadLogisoftProperties.getProperty("reportLocation")).append("/Documents/AccountPayable/");
            fileNameBuilder.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
            File file = new File(fileNameBuilder.toString());
            if (!file.exists()) {
                file.mkdirs();
            }
            fileNameBuilder.append("CheckRegister.xlsx");
            init(fileNameBuilder.toString(), "Payment Detail Report");
            writeHeader();
            writeContent();
            writeIntoFile();
            return fileNameBuilder.toString();
        } catch (Exception e) {
            throw e;
        } finally {
            exit();
        }
    }

}
