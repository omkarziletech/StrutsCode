package com.logiware.excel;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.bc.accounting.ReportConstants;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.struts.form.AgingReportForm;
import com.logiware.bean.CustomerBean;
import com.logiware.bean.ReportBean;
import com.logiware.hibernate.dao.ArAgingReportDAO;
import java.io.File;
import java.util.Date;
import java.util.List;
import org.apache.poi.ss.usermodel.CellStyle;

/**
 *
 * @author Lakshmi Naryanan
 */
public class ArAgingExcelCreator extends BaseExcelCreator implements ConstantsInterface {

    private AgingReportForm form;
    private int endColumn;

    public ArAgingExcelCreator() {
    }

    public ArAgingExcelCreator(AgingReportForm form) {
        this.form = form;
        if (CommonUtils.isEqual(form.getReport(), ReportConstants.REPORT_TYPE_SUMMARY)) {
            endColumn = CommonUtils.isEqual(form.getAllCustomersCheck(), ON) ? 10 : 7;
        } else {
            endColumn = CommonUtils.isEqual(form.getAllCustomersCheck(), ON) ? 12 : 9;
        }
    }

    private void writeHeader() throws Exception {
        createRow();
        createHeaderCell("AR Aging Report", headerCellStyleCenterBold);
        mergeCells(rowIndex, rowIndex, 0, endColumn);
        row.setHeightInPoints(20);
        if (CommonUtils.isEqual(form.getAllCustomersCheck(), ON)) {
            createRow();
            resetColumnIndex();
            createHeaderCell("For ALL Customers", subHeaderOneCellStyleLeftBold);
            mergeCells(rowIndex, rowIndex, 0, endColumn);
        }
        if (CommonUtils.isEqual(form.getNoPaymentDate(), ON)) {
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
        createHeaderCell(form.getDateRangeTo(), subHeaderOneCellStyleLeftNormal);
        createEmptyCell(subHeaderOneCellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 4, endColumn);

        if (CommonUtils.isEqualIgnoreCase(form.getAllCustomersCheck(), ON)) {
            if (CommonUtils.isNotEmpty(form.getCustomerRangeFrom())) {
                createRow();
                resetColumnIndex();
                createHeaderCell("Customer Range :", subHeaderTwoCellStyleRightBold);
                String customerRange = form.getCustomerRangeFrom();
                if (CommonUtils.isNotEmpty(form.getCustomerRangeTo())) {
                    customerRange += " - " + form.getCustomerRangeTo();
                }
                createHeaderCell(customerRange, subHeaderTwoCellStyleLeftNormal);
                createEmptyCell(subHeaderTwoCellStyleLeftNormal);
                mergeCells(rowIndex, rowIndex, 2, endColumn);
            }
        } else if (CommonUtils.isNotEmpty(form.getCustomerNumber())) {
            CustomerBean customer = new ArAgingReportDAO().getCustomerDetails(form.getCustomerNumber());
            createRow();
            resetColumnIndex();
            createHeaderCell("Customer Name :", subHeaderTwoCellStyleRightBold);
            createHeaderCell(customer.getCustomerName(), subHeaderTwoCellStyleLeftNormal);
            createHeaderCell("Customer Number :", subHeaderTwoCellStyleRightBold);
            createHeaderCell(customer.getCustomerNumber(), subHeaderTwoCellStyleLeftNormal);
            createHeaderCell("Blue Screen Account :", subHeaderTwoCellStyleRightBold);
            createHeaderCell(customer.getBlueScreenAccount(), subHeaderTwoCellStyleLeftNormal);
            createEmptyCell(subHeaderTwoCellStyleLeftNormal);
            mergeCells(rowIndex, rowIndex, 6, endColumn);
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
        if (CommonUtils.isEqual(form.getAllCustomersCheck(), ON)) {
            createHeaderCell("Bluescreen #", tableHeaderCellStyleCenterBold);
            createHeaderCell("Customer Number", tableHeaderCellStyleCenterBold);
            createHeaderCell("Customer Name", tableHeaderCellStyleCenterBold);
        }
        if (CommonUtils.isEqual(form.getReport(), ReportConstants.REPORT_TYPE_SUMMARY)) {
            createHeaderCell("Collector", tableHeaderCellStyleCenterBold);
            createHeaderCell("Credit Status", tableHeaderCellStyleCenterBold);
            createHeaderCell("Credit Limit", tableHeaderCellStyleCenterBold);
            createHeaderCell("0-30 days", tableHeaderCellStyleCenterBold);
            createHeaderCell("31-60 days", tableHeaderCellStyleCenterBold);
            createHeaderCell("61-90 days", tableHeaderCellStyleCenterBold);
            createHeaderCell("91+ days", tableHeaderCellStyleCenterBold);
            createHeaderCell("Total", tableHeaderCellStyleCenterBold);
        } else {
            createHeaderCell("B/L-DR #", tableHeaderCellStyleCenterBold);
            createHeaderCell("Invoice #", tableHeaderCellStyleCenterBold);
            createHeaderCell("Date", tableHeaderCellStyleCenterBold);
            createHeaderCell("Ref #", tableHeaderCellStyleCenterBold);
            createHeaderCell("0-30 days", tableHeaderCellStyleCenterBold);
            createHeaderCell("31-60 days", tableHeaderCellStyleCenterBold);
            createHeaderCell("61-90 days", tableHeaderCellStyleCenterBold);
            createHeaderCell("91+ days", tableHeaderCellStyleCenterBold);
            createHeaderCell("Total", tableHeaderCellStyleCenterBold);
            createHeaderCell("Voyage", tableHeaderCellStyleCenterBold);
        }
    }

    private void writeSummaryContents() throws Exception {
        List<ReportBean> transactions = new ArAgingReportDAO().getSummaryTransactions(form);
        if (CommonUtils.isEqual(form.getAllCustomersCheck(), ON)) {
            double age30Days = 0d;
            double age60Days = 0d;
            double age90Days = 0d;
            double age91Days = 0d;
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
                createTextCell(transaction.getCreditStatus(), textCellStyle);
                createDoubleCell(transaction.getCreditLimit(), doubleCellStyle);
                createDoubleCell(transaction.getAge30Days(), doubleCellStyle);
                createDoubleCell(transaction.getAge60Days(), doubleCellStyle);
                createDoubleCell(transaction.getAge90Days(), doubleCellStyle);
                createDoubleCell(transaction.getAge91Days(), doubleCellStyle);
                createDoubleCell(transaction.getAgeTotal(), doubleCellStyle);
                rowCount++;
                age30Days += Double.parseDouble(transaction.getAge30Days().replace(",", ""));
                age60Days += Double.parseDouble(transaction.getAge60Days().replace(",", ""));
                age90Days += Double.parseDouble(transaction.getAge90Days().replace(",", ""));
                age91Days += Double.parseDouble(transaction.getAge91Days().replace(",", ""));
                ageTotal += Double.parseDouble(transaction.getAgeTotal().replace(",", ""));
            }
            createRow();
            resetColumnIndex();
            for (int i = 0; i < 5; i++) {
                createEmptyCell(lavendarCellStyleRightBold);
            }
            createTextCell("Total", lavendarCellStyleRightBold);
            createDoubleCell(age30Days, lavendarCellStyleRightBold);
            createDoubleCell(age60Days, lavendarCellStyleRightBold);
            createDoubleCell(age90Days, lavendarCellStyleRightBold);
            createDoubleCell(age91Days, lavendarCellStyleRightBold);
            createDoubleCell(ageTotal, lavendarCellStyleRightBold);
        } else {
            int rowCount = 0;
            for (ReportBean transaction : transactions) {
                createRow();
                resetColumnIndex();
                CellStyle textCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleLeftNormal : tableOddRowCellStyleLeftNormal;
                CellStyle doubleCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleRightNormal : tableOddRowCellStyleRightNormal;
                createTextCell(transaction.getCollector(), textCellStyle);
                createTextCell(transaction.getCreditStatus(), textCellStyle);
                createDoubleCell(transaction.getCreditLimit(), doubleCellStyle);
                createDoubleCell(transaction.getAge30Days(), doubleCellStyle);
                createDoubleCell(transaction.getAge60Days(), doubleCellStyle);
                createDoubleCell(transaction.getAge90Days(), doubleCellStyle);
                createDoubleCell(transaction.getAge91Days(), doubleCellStyle);
                createDoubleCell(transaction.getAgeTotal(), doubleCellStyle);
                rowCount++;
            }
        }
    }

    private void writeDetailContents() throws Exception {
        List<ReportBean> transactions = new ArAgingReportDAO().getDetailTransactions(form);
        double age30Days = 0d;
        double age60Days = 0d;
        double age90Days = 0d;
        double age91Days = 0d;
        double ageTotal = 0d;
        if (CommonUtils.isEqual(form.getAllCustomersCheck(), ON)) {
            int rowCount = 0;
            for (ReportBean transaction : transactions) {
                createRow();
                resetColumnIndex();
                CellStyle textCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleLeftNormal : tableOddRowCellStyleLeftNormal;
                CellStyle doubleCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleRightNormal : tableOddRowCellStyleRightNormal;
                createTextCell(transaction.getBlueScreenAccount(), textCellStyle);
                createTextCell(transaction.getCustomerNumber(), textCellStyle);
                createTextCell(transaction.getCustomerName(), textCellStyle);
                createTextCell(transaction.getBlNumber(), textCellStyle);
                createTextCell(transaction.getInvoiceNumber(), textCellStyle);
                createTextCell(transaction.getInvoiceDate(), textCellStyle);
                createTextCell(transaction.getCustomerReference(), textCellStyle);
                createDoubleCell(transaction.getAge30Days(), doubleCellStyle);
                createDoubleCell(transaction.getAge60Days(), doubleCellStyle);
                createDoubleCell(transaction.getAge90Days(), doubleCellStyle);
                createDoubleCell(transaction.getAge91Days(), doubleCellStyle);
                createDoubleCell(transaction.getAgeTotal(), doubleCellStyle);
                createTextCell(transaction.getVoyageNumber(), textCellStyle);
                age30Days += Double.parseDouble(transaction.getAge30Days().replace(",", ""));
                age60Days += Double.parseDouble(transaction.getAge60Days().replace(",", ""));
                age90Days += Double.parseDouble(transaction.getAge90Days().replace(",", ""));
                age91Days += Double.parseDouble(transaction.getAge91Days().replace(",", ""));
                ageTotal += Double.parseDouble(transaction.getAgeTotal().replace(",", ""));
                rowCount++;
            }
            createRow();
            resetColumnIndex();
            for (int i = 0; i < 6; i++) {
                createEmptyCell(lavendarCellStyleRightBold);
            }
            createTextCell("Grand Total", lavendarCellStyleRightBold);
            createDoubleCell(age30Days, lavendarCellStyleRightBold);
            createDoubleCell(age60Days, lavendarCellStyleRightBold);
            createDoubleCell(age90Days, lavendarCellStyleRightBold);
            createDoubleCell(age91Days, lavendarCellStyleRightBold);
            createDoubleCell(ageTotal, lavendarCellStyleRightBold);
            createEmptyCell(lavendarCellStyleRightBold);
        } else {
            int rowCount = 0;
            for (ReportBean transaction : transactions) {
                createRow();
                resetColumnIndex();
                CellStyle textCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleLeftNormal : tableOddRowCellStyleLeftNormal;
                CellStyle doubleCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleRightNormal : tableOddRowCellStyleRightNormal;
                createTextCell(transaction.getBlNumber(), textCellStyle);
                createTextCell(transaction.getInvoiceNumber(), textCellStyle);
                createTextCell(transaction.getInvoiceDate(), textCellStyle);
                createTextCell(transaction.getCustomerReference(), textCellStyle);
                createDoubleCell(transaction.getAge30Days(), doubleCellStyle);
                createDoubleCell(transaction.getAge60Days(), doubleCellStyle);
                createDoubleCell(transaction.getAge90Days(), doubleCellStyle);
                createDoubleCell(transaction.getAge91Days(), doubleCellStyle);
                createDoubleCell(transaction.getAgeTotal(), doubleCellStyle);
                createTextCell(transaction.getVoyageNumber(), textCellStyle);
                age30Days += Double.parseDouble(transaction.getAge30Days().replace(",", ""));
                age60Days += Double.parseDouble(transaction.getAge60Days().replace(",", ""));
                age90Days += Double.parseDouble(transaction.getAge90Days().replace(",", ""));
                age91Days += Double.parseDouble(transaction.getAge91Days().replace(",", ""));
                ageTotal += Double.parseDouble(transaction.getAgeTotal().replace(",", ""));
                rowCount++;
            }
            createRow();
            resetColumnIndex();
            for (int i = 0; i < 3; i++) {
                createEmptyCell(lavendarCellStyleRightBold);
            }
            createTextCell("Grand Total", lavendarCellStyleRightBold);
            createDoubleCell(age30Days, lavendarCellStyleRightBold);
            createDoubleCell(age60Days, lavendarCellStyleRightBold);
            createDoubleCell(age90Days, lavendarCellStyleRightBold);
            createDoubleCell(age91Days, lavendarCellStyleRightBold);
            createDoubleCell(ageTotal, lavendarCellStyleRightBold);
            createEmptyCell(lavendarCellStyleRightBold);
        }
    }

    private void writeContent() throws Exception {
        if (CommonUtils.isEqual(form.getReport(), ReportConstants.REPORT_TYPE_SUMMARY)) {
            writeSummaryContents();
        } else {
            writeDetailContents();
        }
        setColumnAutoSize();
    }

    public String create() throws Exception {
        try {
            StringBuilder fileNameBuilder = new StringBuilder();
            fileNameBuilder.append(LoadLogisoftProperties.getProperty("reportLocation")).append("/Documents/ArAgingReport/");
            fileNameBuilder.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
            File file = new File(fileNameBuilder.toString());
            if (!file.exists()) {
                file.mkdirs();
            }
            if (CommonUtils.isEqual(form.getAllCustomersCheck(), ON)) {
                fileNameBuilder.append("AllCustomers.xlsx");
            } else {
                fileNameBuilder.append(form.getCustomerNumber()).append(".xlsx");
            }
            init(fileNameBuilder.toString(), form.getReport());
            writeHeader();
            writeContent();
            writeIntoFile();
            return fileNameBuilder.toString();
        } catch (Exception e) {
            throw e;
        } finally{
            exit();
        }
    }
}
