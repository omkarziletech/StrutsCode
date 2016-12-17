package com.logiware.accounting.excel;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.accounting.model.CompanyModel;
import com.logiware.bean.CustomerBean;
import com.logiware.excel.BaseExcelCreator;
import java.io.File;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import com.logiware.bean.AccountingBean;


/**
 *
 * @author venugopal.s
 */
public class AutoStatementExcelCreator extends BaseExcelCreator {

    private List<AccountingBean> transactions;
    private CustomerBean customer;
    private CustomerBean agingBuckets;
    private CompanyModel company;
    private int endColumn;

    public AutoStatementExcelCreator() {
    }

    public AutoStatementExcelCreator(List<AccountingBean> transactions, CustomerBean customer, CompanyModel company, CustomerBean agingBuckets) {
        this.transactions = transactions;
        this.customer = customer;
        this.agingBuckets = agingBuckets;
        this.company = company;
        this.endColumn = 8;
    }

    private void writeSummary(CustomerBean arBuckets) throws Exception {
        XSSFCellStyle cellStyle1 = (XSSFCellStyle) subHeaderOneCellStyleLeftBold;
        cellStyle1.setFillForegroundColor(BG_WHITE);
        subHeaderOneCellStyleLeftBold = (CellStyle) cellStyle1;
        XSSFCellStyle cellStyle2 = (XSSFCellStyle) subHeaderOneCellStyleRightBold;
        cellStyle2.setFillForegroundColor(BG_WHITE);
        subHeaderOneCellStyleRightBold = (CellStyle) cellStyle2;
        //Company Name in header
        createRow();
        createHeaderCell(company.getName(), baseHeaderCellStyleCenterBold);
        mergeCells(rowIndex, rowIndex, 0, endColumn);
        row.setHeightInPoints(20);

        //Company address in header
        StringBuilder address = new StringBuilder();
        address.append(company.getAddress()).append(".");
        address.append(" PHONE: ").append(company.getPhone()).append(".");
        address.append(" FAX: ").append(company.getFax());
        createRow();
        resetColumnIndex();
        createHeaderCell(address.toString(), secondaryHeaderCellStyleCenterNormal);
        mergeCells(rowIndex, rowIndex, 0, endColumn);

        //Report title in header
        createRow();
        resetColumnIndex();
        createHeaderCell("STATEMENT", lightGrayHeaderCellStyleCenterBold);
        mergeCells(rowIndex, rowIndex, 0, endColumn);
        row.setHeightInPoints(20);

        //Customer details in header
        if (null != customer && CommonUtils.isNotEmpty(customer.getCustomerNumber())) {
            endColumn = 2;
            createRow();
            resetColumnIndex();
            int startRowIndex = rowIndex;
            createHeaderCell(customer.getCustomerName(), subHeaderOneCellStyleLeftBold);
            mergeCells(rowIndex, rowIndex, 0, endColumn);
            row.setHeightInPoints(20);
            createRow();
            resetColumnIndex();
            createHeaderCell(customer.getAddress(), cellStyleLeftNormal);
            int endRowIndex = rowIndex + 3;
            mergeCells(rowIndex, endRowIndex, 0, endColumn);
            rowIndex = endRowIndex;
            createRow();
            resetColumnIndex();
            createHeaderCell("Account# : " + customer.getCustomerNumber(), subHeaderOneCellStyleLeftBold);
            mergeCells(rowIndex, rowIndex, 0, endColumn);
            row.setHeightInPoints(20);
            rowIndex = startRowIndex;
            columnIndex = endColumn;
        } else {
            createRow();
            resetColumnIndex();
        }
        //Add AR buckets in header
        int startRowIndex = rowIndex;
        row = sheet.getRow(rowIndex);
        createHeaderCell("ACCOUNT SUMMARY", subHeaderOneCellStyleRightBold);
        createHeaderCell("AMOUNT", subHeaderOneCellStyleRightBold);
        rowIndex++;
        row = sheet.getRow(rowIndex);
        columnIndex -= 2;
        createHeaderCell("CURRENT", subHeaderOneCellStyleRightBold);
        createHeaderCell(arBuckets.getCurrent(), cellStyleRightNormal);
        rowIndex++;
        row = sheet.getRow(rowIndex);
        columnIndex -= 2;
        createHeaderCell("31-60 DAYS", subHeaderOneCellStyleRightBold);
        createHeaderCell(arBuckets.getThirtyOneToSixtyDays(), cellStyleRightNormal);
        rowIndex++;
        row = sheet.getRow(rowIndex);
        columnIndex -= 2;
        createHeaderCell("61-90 DAYS", subHeaderOneCellStyleRightBold);
        createHeaderCell(arBuckets.getSixtyOneToNintyDays(), cellStyleRightNormal);
        rowIndex++;
        row = sheet.getRow(rowIndex);
        columnIndex -= 2;
        createHeaderCell(">90 DAYS", subHeaderOneCellStyleRightBold);
        createHeaderCell(arBuckets.getGreaterThanNintyDays(), cellStyleRightNormal);
        rowIndex++;
        row = sheet.getRow(rowIndex);
        columnIndex -= 2;
        createHeaderCell("TOTAL", subHeaderOneCellStyleRightBold);
        createHeaderCell(arBuckets.getTotal(), cellStyleRightNormal);
        endColumn = 8;
        if (columnIndex != endColumn) {
            int startColumnIndex = columnIndex;
            for (int rowCount = startRowIndex; rowCount <= rowIndex; rowCount++) {
                row = sheet.getRow(rowCount);
                columnIndex = startColumnIndex;
                for (int colCount = startColumnIndex; colCount < endColumn; colCount++) {
                    createCell();
                    cell.setCellValue("");
                    cell.setCellStyle(cellStyleLeftNormal);
                }
            }
        }
        //Statement date in header
        createRow();
        resetColumnIndex();
        String statementDate = "Statement Date : " + DateUtils.formatDate(new Date(), "MM/dd/yyyy");
        createHeaderCell(statementDate, subHeaderOneCellStyleLeftBold);
        mergeCells(rowIndex, rowIndex, 0, endColumn);
    }

    private void writeContent() throws Exception {
        writeSummary(agingBuckets);
        //Content title
        createRow();
        resetColumnIndex();
        createHeaderCell("ACCOUNT DETAIL", lightGrayHeaderCellStyleCenterBold);
        mergeCells(rowIndex, rowIndex, 0, endColumn);
        row.setHeightInPoints(20);
        //Content table header
        createRow();
        resetColumnIndex();
        createHeaderCell("Date", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 10);
        createHeaderCell("Invoice/BL", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 30);
        createHeaderCell("Booking#", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 25);
        createHeaderCell("Customer Reference", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 40);
        createHeaderCell("Invoice Amount", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("Payment/ Adjustment", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("Balance", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        boolean isAgent = false;
        if ((StringUtils.contains(customer.getAccountType(), "V")
                && (CommonUtils.isEqual(customer.getSubType(), "Export Agent") || CommonUtils.isEqual(customer.getSubType(), "Import Agent")))
                || StringUtils.contains(customer.getAccountType(), "E") || StringUtils.contains(customer.getAccountType(), "I")) {
            createHeaderCell("Consignee", tableHeaderCellStyleCenterBold);
            sheet.setColumnWidth(columnIndex, 256 * 40);
            createHeaderCell("Voyage", tableHeaderCellStyleCenterBold);
            sheet.setColumnWidth(columnIndex, 256 * 20);
            isAgent = true;
        } else {
            mergeCells(rowIndex, rowIndex, columnIndex, endColumn);
        }
        double totalInvoiceAmount = 0d;
        double totalPaymentAmount = 0d;
        double totalBalance = 0d;
        for (AccountingBean transaction : transactions) {
            createRow();
            resetColumnIndex();
            createTextCell(transaction.getFormattedDate(), cellStyleLeftNormal);
            createTextCell(transaction.getInvoiceOrBl(), cellStyleLeftNormal);
            createTextCell(transaction.getBookingNumber(), cellStyleLeftNormal);
            createTextCell(transaction.getCustomerReference(), cellStyleLeftNormal);
            createAmountCell(transaction.getFormattedAmount(), cellStyleRightNormal);
            createAmountCell(transaction.getFormattedPayment(), cellStyleRightNormal);
            createAmountCell(transaction.getFormattedBalance(), cellStyleRightNormal);
            totalInvoiceAmount += Double.parseDouble(transaction.getFormattedAmount().replace(",", ""));
            totalPaymentAmount += Double.parseDouble(transaction.getFormattedPayment().replace(",", ""));
            totalBalance += Double.parseDouble(transaction.getFormattedBalance().replace(",", ""));
            if (isAgent) {
                createTextCell(transaction.getConsignee(), cellStyleLeftNormal);
                createTextCell(transaction.getVoyage(), cellStyleLeftNormal);
            } else {
                mergeCells(rowIndex, rowIndex, columnIndex, endColumn);
            }
        }
        createRow();
        resetColumnIndex();
        createTextCell("", cellStyleLeftNormal);
        createTextCell("", cellStyleLeftNormal);
        createTextCell("", cellStyleLeftNormal);
        createTextCell("Grand Total", cellStyleRightBold);
        createAmountCell(totalInvoiceAmount, cellStyleRightBold);
        createAmountCell(totalPaymentAmount, cellStyleRightBold);
        createAmountCell(totalBalance, cellStyleRightBold);
        if (isAgent) {
            createTextCell("", cellStyleLeftNormal);
            createTextCell("", cellStyleLeftNormal);
        } else {
            mergeCells(rowIndex, rowIndex, columnIndex, endColumn);
        }

        //Payment Methods
        createRow();
        resetColumnIndex();
        createTextCell("Payment Methods", lightGrayHeaderCellStyleCenterBold);
        mergeCells(rowIndex, rowIndex, 0, endColumn);
        row.setHeightInPoints(20);

        endColumn = 2;

        //Check payment method
        createRow();
        resetColumnIndex();
        createTextCell("Payment By Check", cellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 0, endColumn);
        StringBuilder checkPayment = new StringBuilder();
        checkPayment.append("PLEASE REMIT PAYMENT TO:");
        checkPayment.append("\n").append(company.getName());
        checkPayment.append("\n").append(company.getAddress());
        createRow();
        resetColumnIndex();
        createTextCell(checkPayment.toString(), cellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 0, endColumn);
        row.setHeightInPoints(100);

        //Wire payment method
        rowIndex--;
        endColumn += 3;
        row = sheet.getRow(rowIndex);
        createTextCell("Electronic Funds Transfer Instructions", cellStyleLeftNormal);
        int startColumnIndex = columnIndex;
        mergeCells(rowIndex, rowIndex, columnIndex, endColumn);
        StringBuilder wirePayment = new StringBuilder();
        wirePayment.append("If paying via wire transfer,please send to:");
        wirePayment.append("\nBank: ").append(company.getBankName());
        wirePayment.append("\nAddress: ").append(company.getBankAddress());
        wirePayment.append("\nABA#: ").append(company.getBankAbaNumber());
        wirePayment.append("\nSwift Code: ").append(company.getBankSwiftCode());
        wirePayment.append("\nAcct. Name: ").append(company.getBankAccountName());
        wirePayment.append("\nAccount#: ").append(company.getBankAccountNumber());
        rowIndex++;
        row = sheet.getRow(rowIndex);
        columnIndex = startColumnIndex - 1;
        createTextCell(wirePayment.toString(), cellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, columnIndex, endColumn);

        //Credit Card payment method
        rowIndex--;
        endColumn = 8;
        row = sheet.getRow(rowIndex);
        createTextCell("Credit Card Payments", cellStyleLeftNormal);
        startColumnIndex = columnIndex;
        mergeCells(rowIndex, rowIndex, columnIndex, endColumn);
        StringBuilder creditCard = new StringBuilder();
        creditCard.append("If paying with Credit Card:");
        creditCard.append("\n").append(company.getBankCreditCard());
        rowIndex++;
        row = sheet.getRow(rowIndex);
        columnIndex = startColumnIndex - 1;
        createTextCell(creditCard.toString(), cellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, columnIndex, endColumn);

        //Add contact details
        createRow();
        resetColumnIndex();
        StringBuilder contactDetails = new StringBuilder();
        contactDetails.append(company.getName());
        contactDetails.append(" Account Contact: ");
        if (null != customer && CommonUtils.isNotEmpty(customer.getCollector())) {
            contactDetails.append(customer.getCollector()).append("(").append(customer.getCollectorEmail()).append(")");
        }
        createTextCell(contactDetails.toString(), cellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 0, endColumn);
    }

    public String create() throws Exception {
        try {
            StringBuilder fileNameBuilder = new StringBuilder();
            fileNameBuilder.append(LoadLogisoftProperties.getProperty("reportLocation")).append("/Documents/CustomerStatement/");
            fileNameBuilder.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
            File file = new File(fileNameBuilder.toString());
            if (!file.exists()) {
                file.mkdirs();
            }
            fileNameBuilder.append(customer.getCustomerNumber()).append(".xlsx");
            init(fileNameBuilder.toString(), "AR Statement");
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
