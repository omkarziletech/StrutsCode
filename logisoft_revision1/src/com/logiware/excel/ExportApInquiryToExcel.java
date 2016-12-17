package com.logiware.excel;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;

import jxl.write.Label;

import com.gp.cong.logisoft.Constants.ApConstants;
import com.gp.cong.logisoft.ExcelGenerator.BaseExcelGenerator;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.struts.form.ApInquiryForm;
import com.logiware.bean.AccountingBean;
import com.logiware.bean.CustomerBean;
import java.io.File;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

public class ExportApInquiryToExcel extends BaseExcelGenerator implements ApConstants {

    private void generateExcelSheet(ApInquiryForm apInquiryForm) throws Exception {
        String sheetName = "ApInquiry";
        writableSheet = writableWorkbook.createSheet(sheetName, 0);
        int row = 0;
        int endCol = 14;
        writableSheet.mergeCells(0, row, endCol, row);
        writableSheet.addCell(new Label(0, row, "AP Inquiry Report", headerCell));
        row++;
        writableSheet.mergeCells(0, row, endCol, row);
        writableSheet.addCell(new Label(0, row, "Date: " + DateUtils.formatDate(new Date(), "MM/dd/yyyy"), headerCell));
        if (CommonUtils.isNotEmpty(apInquiryForm.getVendorNumber())) {
            CustomerBean vendor = apInquiryForm.getVendor();
            row++;
            writableSheet.mergeCells(0, row, endCol, row);
            writableSheet.addCell(new Label(0, row, "Vendor Name : " + vendor.getCustomerName(), headerCell));
            row++;
            writableSheet.mergeCells(0, row, endCol, row);
            writableSheet.addCell(new Label(0, row, "Vendor Number : " + vendor.getCustomerNumber(), headerCell));
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
            row++;
            writableSheet.mergeCells(0, row, endCol, row);
            writableSheet.setRowView(row, 900);
            writableSheet.addCell(new Label(0, row, "Vendor Address : " + address, headerCell));
        }
        if (CommonUtils.isNotEqual(apInquiryForm.getSearchBy(), "0") && CommonUtils.isNotEmpty(apInquiryForm.getSearchValue())) {
            row++;
            writableSheet.mergeCells(0, row, endCol, row);
            writableSheet.addCell(new Label(0, row, "Search by : " + apInquiryForm.getSearchBy(), headerCell));
            row++;
            writableSheet.mergeCells(0, row, endCol, row);
            writableSheet.addCell(new Label(0, row, "Search Value : " + apInquiryForm.getSearchValue(), headerCell));
        }
        row++;
        int col = 0;
        writableSheet.setColumnView(0, 50);
        writableSheet.setColumnView(1, 20);
        for (int current = endCol - 12; current < endCol; current++) {
            writableSheet.setColumnView(current, 30);
        }
        writableSheet.addCell(new Label(col++, row, "Vendor Name ", columnHeaderCell));
        writableSheet.addCell(new Label(col++, row, "Vendor Number ", columnHeaderCell));
        writableSheet.addCell(new Label(col++, row, "Invoice Number ", columnHeaderCell));
        writableSheet.addCell(new Label(col++, row, "Invoice Date ", columnHeaderCell));
        writableSheet.addCell(new Label(col++, row, "Due Date ", columnHeaderCell));
        writableSheet.addCell(new Label(col++, row, "Amount", columnHeaderCell));
        writableSheet.addCell(new Label(col++, row, "Balance", columnHeaderCell));
        writableSheet.addCell(new Label(col++, row, "Cheque Number", columnHeaderCell));
        writableSheet.addCell(new Label(col++, row, "Dock Receipt", columnHeaderCell));
        writableSheet.addCell(new Label(col++, row, "Voyage", columnHeaderCell));
        writableSheet.addCell(new Label(col++, row, "Cost Code", columnHeaderCell));
        writableSheet.addCell(new Label(col++, row, "Payment Date", columnHeaderCell));
        writableSheet.addCell(new Label(col++, row, "Transaction Type", columnHeaderCell));
        writableSheet.addCell(new Label(col++, row, "Reference Number", columnHeaderCell));
        writableSheet.addCell(new Label(col++, row, "Status", columnHeaderCell));

        for (AccountingBean transaction : apInquiryForm.getApInquiryList()) {
            row++;
            col = 0;
            writableSheet.addCell(new Label(col++, row, transaction.getCustomerName(), noBorderCellBlackLeft));
            writableSheet.addCell(new Label(col++, row, transaction.getCustomerNumber(), noBorderCellBlackLeft));
            writableSheet.addCell(new Label(col++, row, transaction.getInvoiceOrBl(), noBorderCellBlackLeft));
            writableSheet.addCell(new Label(col++, row, transaction.getFormattedDate(), noBorderCellBlackLeft));
            writableSheet.addCell(new Label(col++, row, transaction.getFormattedDueDate(), noBorderCellBlackLeft));
            writableSheet.addCell(new jxl.write.Number(col++, row,Double.parseDouble(transaction.getFormattedAmount().replaceAll(",", "")), noBorderNumberCellBlackRight));
            writableSheet.addCell(new jxl.write.Number(col++, row,Double.parseDouble(transaction.getFormattedBalance().replaceAll(",", "")), noBorderNumberCellBlackRight));
            writableSheet.addCell(new Label(col++, row, transaction.getCheckNumber(), noBorderCellBlackLeft));
            writableSheet.addCell(new Label(col++, row, transaction.getDockReceipt(), noBorderCellBlackLeft));
            writableSheet.addCell(new Label(col++, row, transaction.getVoyage(), noBorderCellBlackLeft));
            writableSheet.addCell(new Label(col++, row, transaction.getChargeCode(), noBorderCellBlackLeft));
            if (CommonUtils.isEqualIgnoreCase(transaction.getStatus(), CommonConstants.STATUS_PAID)) {
                writableSheet.addCell(new Label(col++, row, transaction.getFormattedPaymentDate(), noBorderCellBlackLeft));
            } else {
                writableSheet.addCell(new Label(col++, row, "", noBorderCellBlackLeft));
            }
            writableSheet.addCell(new Label(col++, row, transaction.getTransactionType(), noBorderCellBlackLeft));
            writableSheet.addCell(new Label(col++, row, transaction.getCustomerReference(), noBorderCellBlackLeft));
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
            writableSheet.addCell(new Label(col++, row, status, noBorderCellBlackLeft));
        }

    }

    public String exportToExcel(ApInquiryForm apInquiryForm) throws Exception {
        StringBuilder fileName = new StringBuilder(LoadLogisoftProperties.getProperty("reportLocation"));
        fileName.append("/Documents/ApInquiry/");
        fileName.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
        File file = new File(fileName.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        if (CommonUtils.isNotEqual(apInquiryForm.getSearchBy(), "0") && CommonUtils.isNotEmpty(apInquiryForm.getSearchValue())) {
            fileName.append("ApInquiry.xls");
        } else if (CommonUtils.isNotEmpty(apInquiryForm.getVendorNumber())) {
            fileName.append(apInquiryForm.getVendorNumber()).append(".xls");
        } else {
            fileName.append("ApInquiry.xls");
        }
        super.init(fileName.toString());
        this.generateExcelSheet(apInquiryForm);
        super.write();
        super.close();
        return fileName.toString();
    }
}
