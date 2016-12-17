package com.logiware.accounting.excel;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.accounting.dao.ArReportsDAO;
import com.logiware.accounting.form.ArReportsForm;
import com.logiware.bean.CustomerBean;
import com.logiware.bean.ReportBean;
import com.logiware.excel.BaseExcelCreator;
import java.io.File;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;

/**
 *
 * @author Lakshmi Naryanan
 */
public class ArAgingExcelCreator extends BaseExcelCreator implements ConstantsInterface {

    private ArReportsForm arReportsForm;
    private CustomerBean customer;
    private Integer endColumn;
    List<ReportBean> transactions = null;

    public ArAgingExcelCreator() {
    }

    public ArAgingExcelCreator(ArReportsForm arReportsForm, CustomerBean customer) {
        this.arReportsForm = arReportsForm;
        this.customer = customer;
        endColumn = CommonUtils.isEqual(arReportsForm.getReportType(), SUMMARY) ? 15 : 19;
        if (CommonUtils.isNotEmpty(arReportsForm.getSalesManager().getManagerName())) {
            endColumn = 10;
        } else if (CommonUtils.isNotEmpty(arReportsForm.getTerminalManager().getManagerName())) {
            endColumn = 15;
        }
    }

    private void writeSalesReportHeader() throws Exception {
        createRow();
        resetColumnIndex();
        String salesManager = arReportsForm.getSalesManager().getManagerName() + " - " + arReportsForm.getSalesManager().getSalesCode();
        createHeaderCell("For Sales Manager : " + salesManager, subHeaderOneCellStyleLeftBold);
        mergeCells(rowIndex, rowIndex, 0, endColumn);
        if (arReportsForm.isAllPayments()) {
            createRow();
            resetColumnIndex();
            createHeaderCell("Includes All Payments Regardless of Date", subHeaderTwoCellStyleLeftBold);
            mergeCells(rowIndex, rowIndex, 0, endColumn);
        }
        createRow();
        resetColumnIndex();
        createHeaderCell("Report Type :", subHeaderOneCellStyleRightBold);
        createHeaderCell("Summary", subHeaderOneCellStyleLeftNormal);
        createHeaderCell("Cut-off Date :", subHeaderOneCellStyleRightBold);
        createHeaderCell(arReportsForm.getCutOffDate(), subHeaderOneCellStyleLeftNormal);
        createEmptyCell(subHeaderOneCellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 4, endColumn);
        createRow();
        resetColumnIndex();
        createHeaderCell("Bluescreen #", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("Customer Number", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("Customer Name", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 40);
        createHeaderCell("Collector", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("Sales Code", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 15);
        createHeaderCell("46-60 days", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("61-90 days", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("91-120 days", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("121-180 days", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("181+ days", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("Total", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
    }

    private void writeTerminalReportHeader() throws Exception {
        createRow();
        resetColumnIndex();
        String billingTerminal = arReportsForm.getTerminalManager().getManagerName() + " - " + arReportsForm.getTerminalManager().getTerminalNumber();
        createHeaderCell("For Terminal Manager : " + billingTerminal, subHeaderOneCellStyleLeftBold);
        mergeCells(rowIndex, rowIndex, 0, endColumn);
        if (arReportsForm.isAllPayments()) {
            createRow();
            resetColumnIndex();
            createHeaderCell("Includes All Payments Regardless of Date", subHeaderTwoCellStyleLeftBold);
            mergeCells(rowIndex, rowIndex, 0, endColumn);
        }
        createRow();
        resetColumnIndex();
        createHeaderCell("Report Type :", subHeaderOneCellStyleRightBold);
        createHeaderCell("Detail", subHeaderOneCellStyleLeftNormal);
        createHeaderCell("Cut-off Date :", subHeaderOneCellStyleRightBold);
        createHeaderCell(arReportsForm.getCutOffDate(), subHeaderOneCellStyleLeftNormal);
        createEmptyCell(subHeaderOneCellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 4, endColumn);
        createRow();
        resetColumnIndex();
        createHeaderCell("Bluescreen #", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("Customer Number", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("Customer Name", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 40);
        createHeaderCell("Collector", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("Sales Code", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 15);
        createHeaderCell("Billing Terminal", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("Invoice-B/L-DR #", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 30);
        createHeaderCell("Date", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 10);
        createHeaderCell("0-30 days", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("31-45 days", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("46-60 days", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("61-90 days", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("91-120 days", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("121-180 days", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("181+ days", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("Total", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
    }

    private void writeHeader() throws Exception {
        createRow();
        createHeaderCell("AR Aging Report", headerCellStyleLeftBold);
        mergeCells(rowIndex, rowIndex, 0, endColumn);
        row.setHeightInPoints(20);
        if (CommonUtils.isNotEmpty(arReportsForm.getSalesManager().getManagerName())) {
            writeSalesReportHeader();
        } else if (CommonUtils.isNotEmpty(arReportsForm.getTerminalManager().getManagerName())) {
            writeTerminalReportHeader();
        } else {
            if (CommonUtils.isNotEmpty(arReportsForm.getCollector())) {
                createRow();
                resetColumnIndex();
                if (CommonUtils.isEqualIgnoreCase(arReportsForm.getCollector(), ALL)) {
                    createHeaderCell("For Collector : " + ALL, subHeaderOneCellStyleLeftBold);
                } else {
                    String collector = new UserDAO().getLoginName(Integer.parseInt(arReportsForm.getCollector()));
                    createHeaderCell("For Collector : " + collector, subHeaderOneCellStyleLeftBold);
                }
                mergeCells(rowIndex, rowIndex, 0, endColumn);
            } else if (arReportsForm.isAllCustomers()) {
                createRow();
                resetColumnIndex();
                createHeaderCell("For ALL Customers", subHeaderOneCellStyleLeftBold);
                mergeCells(rowIndex, rowIndex, 0, endColumn);
            }
            if (arReportsForm.isAllPayments()) {
                createRow();
                resetColumnIndex();
                createHeaderCell("Includes All Payments Regardless of Date", subHeaderTwoCellStyleLeftBold);
                mergeCells(rowIndex, rowIndex, 0, endColumn);
            }
            createRow();
            resetColumnIndex();
            createHeaderCell("Report Type :", subHeaderOneCellStyleRightBold);
            if (CommonUtils.isEqual(arReportsForm.getReportType(), SUMMARY)) {
                createHeaderCell("Summary", subHeaderOneCellStyleLeftNormal);
            } else {
                createHeaderCell("Detail", subHeaderOneCellStyleLeftNormal);
            }
            createHeaderCell("Cut-off Date :", subHeaderOneCellStyleRightBold);
            createHeaderCell(arReportsForm.getCutOffDate(), subHeaderOneCellStyleLeftNormal);
            createEmptyCell(subHeaderOneCellStyleLeftNormal);
            mergeCells(rowIndex, rowIndex, 4, endColumn);

            if (arReportsForm.isAllCustomers() || CommonUtils.isNotEmpty(arReportsForm.getCollector())) {
                if (CommonUtils.isNotEmpty(arReportsForm.getCustomerFromRange())) {
                    createRow();
                    resetColumnIndex();
                    createHeaderCell("Customer Range :", subHeaderTwoCellStyleRightBold);
                    String customerRange = arReportsForm.getCustomerFromRange();
                    if (CommonUtils.isNotEmpty(arReportsForm.getCustomerToRange())) {
                        customerRange += " - " + arReportsForm.getCustomerToRange();
                    }
                    createHeaderCell(customerRange, subHeaderTwoCellStyleLeftNormal);
                    createEmptyCell(subHeaderTwoCellStyleLeftNormal);
                    mergeCells(rowIndex, rowIndex, 2, endColumn);
                }
            } else if (CommonUtils.isNotEmpty(arReportsForm.getCustomerNumber())) {
                createRow();
                resetColumnIndex();
                createHeaderCell("Customer Name :", subHeaderTwoCellStyleRightBold);
                createHeaderCell(customer.getCustomerName(), subHeaderTwoCellStyleLeftNormal);
                createHeaderCell("Customer Number :", subHeaderTwoCellStyleRightBold);
                createHeaderCell(customer.getCustomerNumber(), subHeaderTwoCellStyleLeftNormal);
                createHeaderCell("Blue Screen Account :", subHeaderTwoCellStyleRightBold);
                createHeaderCell(customer.getBlueScreenAccount(), subHeaderTwoCellStyleLeftNormal);
                createHeaderCell("ECU Designation :", subHeaderTwoCellStyleRightBold);
                createHeaderCell(customer.getEcuDesignation(), subHeaderTwoCellStyleLeftNormal);
                createHeaderCell("Master Account :", subHeaderTwoCellStyleRightBold);
                createHeaderCell(customer.getMaster(), subHeaderTwoCellStyleLeftNormal);
                mergeCells(rowIndex, rowIndex, 9, endColumn);
                if (CommonUtils.isNotEqual(columnIndex, endColumn)) {
                    createEmptyCell(subHeaderTwoCellStyleLeftNormal);
                    mergeCells(rowIndex, rowIndex, 8, endColumn);
                }
                createRow();
                resetColumnIndex();
                createHeaderCell("Address :", subHeaderOneCellStyleRightBold);
                StringBuilder address = new StringBuilder();
                address.append(customer.getAddress());
                if (CommonUtils.isNotEmpty(customer.getPhone())) {
                    address.append("\nPhone : ").append(customer.getPhone());
                }
                if (CommonUtils.isNotEmpty(customer.getFax())) {
                    address.append("\nFax : ").append(customer.getFax());
                }
                if (CommonUtils.isNotEmpty(customer.getEmail())) {
                    address.append("\nEmail : ").append(customer.getEmail());
                }
                createHeaderCell(address.toString(), subHeaderOneCellStyleLeftNormal);
                mergeCells(rowIndex, rowIndex, 1, endColumn);
                row.setHeightInPoints(60);
            }
            createRow();
            resetColumnIndex();
            createHeaderCell("Master #", tableHeaderCellStyleCenterBold);
            sheet.setColumnWidth(columnIndex, 256 * 20);
            createHeaderCell("Bluescreen #", tableHeaderCellStyleCenterBold);
            sheet.setColumnWidth(columnIndex, 256 * 20);
            createHeaderCell("ECU Designation", tableHeaderCellStyleCenterBold);
            sheet.setColumnWidth(columnIndex, 256 * 20);
            createHeaderCell("Customer Number", tableHeaderCellStyleCenterBold);
            sheet.setColumnWidth(columnIndex, 256 * 20);
            createHeaderCell("Customer Name", tableHeaderCellStyleCenterBold);
            sheet.setColumnWidth(columnIndex, 256 * 40);
            createHeaderCell("Collector", tableHeaderCellStyleCenterBold);
            sheet.setColumnWidth(columnIndex, 256 * 20);
            createHeaderCell("Sales Code", tableHeaderCellStyleCenterBold);
            sheet.setColumnWidth(columnIndex, 256 * 15);
            createHeaderCell("Credit Status", tableHeaderCellStyleCenterBold);
            sheet.setColumnWidth(columnIndex, 256 * 20);
            if (CommonUtils.isEqual(arReportsForm.getReportType(), SUMMARY)) {
                createHeaderCell("Credit Limit", tableHeaderCellStyleCenterBold);
                sheet.setColumnWidth(columnIndex, 256 * 20);
                createHeaderCell("0-30 days", tableHeaderCellStyleCenterBold);
                sheet.setColumnWidth(columnIndex, 256 * 20);
                createHeaderCell("31-60 days", tableHeaderCellStyleCenterBold);
                sheet.setColumnWidth(columnIndex, 256 * 20);
                createHeaderCell("61-90 days", tableHeaderCellStyleCenterBold);
                sheet.setColumnWidth(columnIndex, 256 * 20);
                createHeaderCell("91-120 days", tableHeaderCellStyleCenterBold);
                sheet.setColumnWidth(columnIndex, 256 * 20);
                createHeaderCell("121-180 days", tableHeaderCellStyleCenterBold);
                sheet.setColumnWidth(columnIndex, 256 * 20);
                createHeaderCell("181+ days", tableHeaderCellStyleCenterBold);
                sheet.setColumnWidth(columnIndex, 256 * 20);
                createHeaderCell("Total", tableHeaderCellStyleCenterBold);
                sheet.setColumnWidth(columnIndex, 256 * 20);
            } else {
                createHeaderCell("Billing Terminal", tableHeaderCellStyleCenterBold);
                sheet.setColumnWidth(columnIndex, 256 * 20);
                createHeaderCell("Invoice-B/L-DR #", tableHeaderCellStyleCenterBold);
                sheet.setColumnWidth(columnIndex, 256 * 30);
                createHeaderCell("Date", tableHeaderCellStyleCenterBold);
                sheet.setColumnWidth(columnIndex, 256 * 10);
                createHeaderCell("Ref #", tableHeaderCellStyleCenterBold);
                sheet.setColumnWidth(columnIndex, 256 * 40);
                createHeaderCell("0-30 days", tableHeaderCellStyleCenterBold);
                sheet.setColumnWidth(columnIndex, 256 * 20);
                createHeaderCell("31-60 days", tableHeaderCellStyleCenterBold);
                sheet.setColumnWidth(columnIndex, 256 * 20);
                createHeaderCell("61-90 days", tableHeaderCellStyleCenterBold);
                sheet.setColumnWidth(columnIndex, 256 * 20);
                createHeaderCell("91-120 days", tableHeaderCellStyleCenterBold);
                sheet.setColumnWidth(columnIndex, 256 * 20);
                createHeaderCell("121-180 days", tableHeaderCellStyleCenterBold);
                sheet.setColumnWidth(columnIndex, 256 * 20);
                createHeaderCell("181+ days", tableHeaderCellStyleCenterBold);
                sheet.setColumnWidth(columnIndex, 256 * 20);
                createHeaderCell("Total", tableHeaderCellStyleCenterBold);
                sheet.setColumnWidth(columnIndex, 256 * 20);
                createHeaderCell("Voyage", tableHeaderCellStyleCenterBold);
                sheet.setColumnWidth(columnIndex, 256 * 20);
            }
        }
    }

    private void writeSalesReportContents() throws Exception {
        double age60Days = 0d;
        double age90Days = 0d;
        double age120Days = 0d;
        double age180Days = 0d;
        double age181Days = 0d;
        double ageTotal = 0d;
        int rowCount = 0;
        for (ReportBean transaction : transactions) {
            createRow();
            resetColumnIndex();
            CellStyle textCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleLeftNormal : tableOddRowCellStyleLeftNormal;
            CellStyle doubleCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleRightNormal : tableOddRowCellStyleRightNormal;
            createTextCell(transaction.getBlueScreenAccount(), textCellStyle);
            createTextCell(transaction.getCustomerNumber(), textCellStyle);
            createTextCell(transaction.getCustomerName(), textCellStyle);
            createTextCell(transaction.getCollector(), textCellStyle);
            createTextCell(transaction.getSalesCode(), textCellStyle);
            createDoubleCell(transaction.getAge60Days(), doubleCellStyle);
            createDoubleCell(transaction.getAge90Days(), doubleCellStyle);
            createDoubleCell(transaction.getAge120Days(), doubleCellStyle);
            createDoubleCell(transaction.getAge180Days(), doubleCellStyle);
            createDoubleCell(transaction.getAge181Days(), doubleCellStyle);
            createDoubleCell(transaction.getAgeTotal(), doubleCellStyle);
            rowCount++;
            age60Days += Double.parseDouble(transaction.getAge60Days().replace(",", ""));
            age90Days += Double.parseDouble(transaction.getAge90Days().replace(",", ""));
            age120Days += Double.parseDouble(transaction.getAge120Days().replace(",", ""));
            age180Days += Double.parseDouble(transaction.getAge180Days().replace(",", ""));
            age181Days += Double.parseDouble(transaction.getAge181Days().replace(",", ""));
            ageTotal += Double.parseDouble(transaction.getAgeTotal().replace(",", ""));
        }
        createRow();
        resetColumnIndex();
        for (int i = 0; i < 4; i++) {
            createEmptyCell(lavendarCellStyleRightBold);
        }
        createTextCell("Total", lavendarCellStyleRightBold);
        createDoubleCell(age60Days, lavendarCellStyleRightBold);
        createDoubleCell(age90Days, lavendarCellStyleRightBold);
        createDoubleCell(age120Days, lavendarCellStyleRightBold);
        createDoubleCell(age180Days, lavendarCellStyleRightBold);
        createDoubleCell(age181Days, lavendarCellStyleRightBold);
        createDoubleCell(ageTotal, lavendarCellStyleRightBold);
    }

    private void writeTerminalReportContents() throws Exception {
        double age30Days = 0d;
        double age45Days = 0d;
        double age60Days = 0d;
        double age90Days = 0d;
        double age120Days = 0d;
        double age180Days = 0d;
        double age181Days = 0d;
        double ageTotal = 0d;
        int rowCount = 0;
        for (ReportBean transaction : transactions) {
            createRow();
            resetColumnIndex();
            CellStyle textCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleLeftNormal : tableOddRowCellStyleLeftNormal;
            CellStyle doubleCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleRightNormal : tableOddRowCellStyleRightNormal;
            createTextCell(transaction.getBlueScreenAccount(), textCellStyle);
            createTextCell(transaction.getCustomerNumber(), textCellStyle);
            createTextCell(transaction.getCustomerName(), textCellStyle);
            createTextCell(transaction.getCollector(), textCellStyle);
            createTextCell(transaction.getSalesCode(), textCellStyle);
            createTextCell(transaction.getBillingTerminal(), textCellStyle);
            createTextCell(transaction.getInvoiceOrBl(), textCellStyle);
            createTextCell(transaction.getInvoiceDate(), textCellStyle);
            createDoubleCell(transaction.getAge30Days(), doubleCellStyle);
            createDoubleCell(transaction.getAge45Days(), doubleCellStyle);
            createDoubleCell(transaction.getAge60Days(), doubleCellStyle);
            createDoubleCell(transaction.getAge90Days(), doubleCellStyle);
            createDoubleCell(transaction.getAge120Days(), doubleCellStyle);
            createDoubleCell(transaction.getAge180Days(), doubleCellStyle);
            createDoubleCell(transaction.getAge181Days(), doubleCellStyle);
            createDoubleCell(transaction.getAgeTotal(), doubleCellStyle);
            age30Days += Double.parseDouble(transaction.getAge30Days().replace(",", ""));
            age45Days += Double.parseDouble(transaction.getAge45Days().replace(",", ""));
            age60Days += Double.parseDouble(transaction.getAge60Days().replace(",", ""));
            age90Days += Double.parseDouble(transaction.getAge90Days().replace(",", ""));
            age120Days += Double.parseDouble(transaction.getAge120Days().replace(",", ""));
            age180Days += Double.parseDouble(transaction.getAge180Days().replace(",", ""));
            age181Days += Double.parseDouble(transaction.getAge181Days().replace(",", ""));
            ageTotal += Double.parseDouble(transaction.getAgeTotal().replace(",", ""));
            rowCount++;
        }
        createRow();
        resetColumnIndex();
        for (int i = 0; i < 7; i++) {
            createEmptyCell(lavendarCellStyleRightBold);
        }
        createTextCell("Total", lavendarCellStyleRightBold);
        createDoubleCell(age30Days, lavendarCellStyleRightBold);
        createDoubleCell(age45Days, lavendarCellStyleRightBold);
        createDoubleCell(age60Days, lavendarCellStyleRightBold);
        createDoubleCell(age90Days, lavendarCellStyleRightBold);
        createDoubleCell(age120Days, lavendarCellStyleRightBold);
        createDoubleCell(age180Days, lavendarCellStyleRightBold);
        createDoubleCell(age181Days, lavendarCellStyleRightBold);
        createDoubleCell(ageTotal, lavendarCellStyleRightBold);
    }

    private void writeSummaryContents() throws Exception {
        double age30Days = 0d;
        double age60Days = 0d;
        double age90Days = 0d;
        double age120Days = 0d;
        double age180Days = 0d;
        double age181Days = 0d;
        double ageTotal = 0d;
        int rowCount = 0;
        for (ReportBean transaction : transactions) {
            createRow();
            resetColumnIndex();
            CellStyle textCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleLeftNormal : tableOddRowCellStyleLeftNormal;
            CellStyle doubleCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleRightNormal : tableOddRowCellStyleRightNormal;
            createTextCell(transaction.getMasterAccountNumber(), textCellStyle);
            createTextCell(transaction.getBlueScreenAccount(), textCellStyle);
            createTextCell(transaction.getEcuDesignation(), textCellStyle);
            createTextCell(transaction.getCustomerNumber(), textCellStyle);
            createTextCell(transaction.getCustomerName(), textCellStyle);
            createTextCell(transaction.getCollector(), textCellStyle);
            createTextCell(transaction.getSalesCode(), textCellStyle);
            createTextCell(transaction.getCreditStatus(), textCellStyle);
            createDoubleCell(transaction.getCreditLimit(), doubleCellStyle);
            createDoubleCell(transaction.getAge30Days(), doubleCellStyle);
            createDoubleCell(transaction.getAge60Days(), doubleCellStyle);
            createDoubleCell(transaction.getAge90Days(), doubleCellStyle);
            createDoubleCell(transaction.getAge120Days(), doubleCellStyle);
            createDoubleCell(transaction.getAge180Days(), doubleCellStyle);
            createDoubleCell(transaction.getAge181Days(), doubleCellStyle);
            createDoubleCell(transaction.getAgeTotal(), doubleCellStyle);
            rowCount++;
            age30Days += Double.parseDouble(transaction.getAge30Days().replace(",", ""));
            age60Days += Double.parseDouble(transaction.getAge60Days().replace(",", ""));
            age90Days += Double.parseDouble(transaction.getAge90Days().replace(",", ""));
            age120Days += Double.parseDouble(transaction.getAge120Days().replace(",", ""));
            age180Days += Double.parseDouble(transaction.getAge180Days().replace(",", ""));
            age181Days += Double.parseDouble(transaction.getAge181Days().replace(",", ""));
            ageTotal += Double.parseDouble(transaction.getAgeTotal().replace(",", ""));
        }
        createRow();
        resetColumnIndex();
        for (int i = 0; i < 8; i++) {
            createEmptyCell(lavendarCellStyleRightBold);
        }
        createTextCell("Total", lavendarCellStyleRightBold);
        createDoubleCell(age30Days, lavendarCellStyleRightBold);
        createDoubleCell(age60Days, lavendarCellStyleRightBold);
        createDoubleCell(age90Days, lavendarCellStyleRightBold);
        createDoubleCell(age120Days, lavendarCellStyleRightBold);
        createDoubleCell(age180Days, lavendarCellStyleRightBold);
        createDoubleCell(age181Days, lavendarCellStyleRightBold);
        createDoubleCell(ageTotal, lavendarCellStyleRightBold);
    }

    private void writeDetailContents() throws Exception {
        double age30Days = 0d;
        double age60Days = 0d;
        double age90Days = 0d;
        double age120Days = 0d;
        double age180Days = 0d;
        double age181Days = 0d;
        double ageTotal = 0d;
        int rowCount = 0;
        for (ReportBean transaction : transactions) {
            createRow();
            resetColumnIndex();
            CellStyle textCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleLeftNormal : tableOddRowCellStyleLeftNormal;
            CellStyle doubleCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleRightNormal : tableOddRowCellStyleRightNormal;
            createTextCell(transaction.getMasterAccountNumber(), textCellStyle);
            createTextCell(transaction.getBlueScreenAccount(), textCellStyle);
            createTextCell(transaction.getEcuDesignation(), textCellStyle);
            createTextCell(transaction.getCustomerNumber(), textCellStyle);
            createTextCell(transaction.getCustomerName(), textCellStyle);
            createTextCell(transaction.getCollector(), textCellStyle);
            createTextCell(transaction.getSalesCode(), textCellStyle);
            createTextCell(transaction.getCreditStatus(), textCellStyle);
            createTextCell(transaction.getBillingTerminal(), textCellStyle);
            createTextCell(transaction.getInvoiceOrBl(), textCellStyle);
            createTextCell(transaction.getInvoiceDate(), textCellStyle);
            createTextCell(transaction.getCustomerReference(), textCellStyle);
            createDoubleCell(transaction.getAge30Days(), doubleCellStyle);
            createDoubleCell(transaction.getAge60Days(), doubleCellStyle);
            createDoubleCell(transaction.getAge90Days(), doubleCellStyle);
            createDoubleCell(transaction.getAge120Days(), doubleCellStyle);
            createDoubleCell(transaction.getAge180Days(), doubleCellStyle);
            createDoubleCell(transaction.getAge181Days(), doubleCellStyle);
            createDoubleCell(transaction.getAgeTotal(), doubleCellStyle);
            createTextCell(transaction.getVoyage(), textCellStyle);
            age30Days += Double.parseDouble(transaction.getAge30Days().replace(",", ""));
            age60Days += Double.parseDouble(transaction.getAge60Days().replace(",", ""));
            age90Days += Double.parseDouble(transaction.getAge90Days().replace(",", ""));
            age120Days += Double.parseDouble(transaction.getAge120Days().replace(",", ""));
            age180Days += Double.parseDouble(transaction.getAge180Days().replace(",", ""));
            age181Days += Double.parseDouble(transaction.getAge181Days().replace(",", ""));
            ageTotal += Double.parseDouble(transaction.getAgeTotal().replace(",", ""));
            rowCount++;
        }
        createRow();
        resetColumnIndex();
        for (int i = 0; i < 11; i++) {
            createEmptyCell(lavendarCellStyleRightBold);
        }
        createTextCell("Total", lavendarCellStyleRightBold);
        createDoubleCell(age30Days, lavendarCellStyleRightBold);
        createDoubleCell(age60Days, lavendarCellStyleRightBold);
        createDoubleCell(age90Days, lavendarCellStyleRightBold);
        createDoubleCell(age120Days, lavendarCellStyleRightBold);
        createDoubleCell(age180Days, lavendarCellStyleRightBold);
        createDoubleCell(age181Days, lavendarCellStyleRightBold);
        createDoubleCell(ageTotal, lavendarCellStyleRightBold);
        createEmptyCell(lavendarCellStyleRightBold);
    }

    private void writeContent() throws Exception {
        if (CommonUtils.isNotEmpty(arReportsForm.getSalesManager().getManagerName())) {
            writeSalesReportContents();
        } else if (CommonUtils.isNotEmpty(arReportsForm.getTerminalManager().getManagerName())) {
            writeTerminalReportContents();
        } else if (CommonUtils.isEqual(arReportsForm.getReportType(), SUMMARY)) {
            writeSummaryContents();
        } else {
            writeDetailContents();
        }
    }

    public String create() throws Exception {
        try {
            transactions = new ArReportsDAO().getAgingTransactions(arReportsForm);
            if (CommonUtils.isNotEmpty(transactions)) {
                StringBuilder fileNameBuilder = new StringBuilder();
                fileNameBuilder.append(LoadLogisoftProperties.getProperty("reportLocation")).append("/Documents/ArAgingReport/");
                fileNameBuilder.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
                File file = new File(fileNameBuilder.toString());
                if (!file.exists()) {
                    file.mkdirs();
                }
                fileNameBuilder.append("ArAging_");
                if (CommonUtils.isNotEmpty(arReportsForm.getSalesManager().getManagerName())) {
                    fileNameBuilder.append(arReportsForm.getSalesManager().getManagerName().replace(" ", "_"));
                } else if (CommonUtils.isNotEmpty(arReportsForm.getTerminalManager().getManagerName())) {
                    fileNameBuilder.append(arReportsForm.getTerminalManager().getManagerName().replace(" ", "_"));
                } else if (CommonUtils.isNotEmpty(arReportsForm.getCollector())) {
                    fileNameBuilder.append("Collector");
                } else if (arReportsForm.isAllCustomers()) {
                    fileNameBuilder.append("AllCustomers");
                } else {
                    fileNameBuilder.append(arReportsForm.getCustomerNumber());
                }
                if (CommonUtils.isNotEmpty(arReportsForm.getInternationalCollector())) {
                    fileNameBuilder.append("_").append(arReportsForm.getInternationalCollector());
                }
                fileNameBuilder.append("_").append(DateUtils.formatDate(new Date(), "yyyy_MM_dd_kkmmss"));
                fileNameBuilder.append(".xlsx");
                init(fileNameBuilder.toString(), arReportsForm.getReportType());
                writeHeader();
                writeContent();
                writeIntoFile();
                return fileNameBuilder.toString();
            } else {
                return null;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            exit();
        }
    }
}
