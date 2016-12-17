package com.logiware.excel;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.struts.form.ApReportsForm;
import com.logiware.bean.CustomerBean;
import com.logiware.bean.ReportBean;
import com.logiware.hibernate.dao.ApReportsDAO;
import java.io.File;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;

/**
 *
 * @author Lakshmi Naryanan
 */
public class ApReportsExcelCreator extends BaseExcelCreator implements ConstantsInterface {

    private ApReportsForm apReportsForm;

    public ApReportsExcelCreator() {
    }

    public ApReportsExcelCreator(ApReportsForm apReportsForm) {
        this.apReportsForm = apReportsForm;
    }

    private void writeVendorDetails() throws Exception {
        CustomerBean vendorDetails = new ApReportsDAO().getVendorDetails(apReportsForm.getVendorNumber());
        createRow();
        resetColumnIndex();
        createHeaderCell("Vendor Number : ", subHeaderTwoCellStyleLeftBold);
        createHeaderCell(vendorDetails.getCustomerNumber(), subHeaderTwoCellStyleLeftNormal);
        createHeaderCell("Vendor Name : ", subHeaderTwoCellStyleLeftBold);
        createHeaderCell(vendorDetails.getCustomerName(), subHeaderTwoCellStyleLeftNormal);
        createHeaderCell("Address : ", subHeaderTwoCellStyleLeftBold);
        StringBuilder address = new StringBuilder(null != vendorDetails.getAddress() ? vendorDetails.getAddress() : "");
        if (CommonUtils.isNotEmpty(vendorDetails.getContact())) {
            address.append("\nContact : ").append(vendorDetails.getContact());
        }
        if (CommonUtils.isNotEmpty(vendorDetails.getEmail())) {
            address.append("\nEmail : ").append(vendorDetails.getEmail());
        }
        createHeaderCell(address.toString(), subHeaderTwoCellStyleLeftNormal);
        if (CommonUtils.isEqualIgnoreCase(apReportsForm.getShowDetailOrSummary(), AP_DETAIL_REPORT)) {
            mergeCells(rowIndex, rowIndex, 5, 10);
        } else {
            mergeCells(rowIndex, rowIndex, 5, 7);
        }
        row.setHeightInPoints((4 * sheet.getDefaultRowHeightInPoints()));
        createRow();
        resetColumnIndex();
        createHeaderCell("ECU Designation : ", subHeaderTwoCellStyleLeftBold);
        createHeaderCell(vendorDetails.getEcuDesignation(), subHeaderTwoCellStyleLeftNormal);
        if (CommonUtils.isEqualIgnoreCase(apReportsForm.getShowDetailOrSummary(), AP_DETAIL_REPORT)) {
            mergeCells(rowIndex, rowIndex, 1, 10);
        } else {
            mergeCells(rowIndex, rowIndex, 1, 7);
        }
    }

    private void writeAgingHeader() throws Exception {
        int endColumn = 7;
        if (CommonUtils.isEqualIgnoreCase(apReportsForm.getShowAllCustomer(), YES) || CommonUtils.isEmpty(apReportsForm.getVendorNumber())) {
            endColumn += 3;
        }
        if (CommonUtils.isEqualIgnoreCase(apReportsForm.getShowDetailOrSummary(), AP_DETAIL_REPORT)) {
            endColumn += 3;
        }
        createRow();
        createHeaderCell("AP Aging Report", headerCellStyleCenterBold);
        mergeCells(rowIndex, rowIndex, 0, endColumn);
        row.setHeightInPoints(20);
        createRow();
        resetColumnIndex();
        createHeaderCell("Cut-off Date : " + apReportsForm.getCutOffDate(), subHeaderOneCellStyleLeftBold);
        mergeCells(rowIndex, rowIndex, 0, endColumn);
        if (CommonUtils.isNotEmpty(apReportsForm.getVendorNumber())
                && CommonUtils.isNotEqualIgnoreCase(apReportsForm.getShowAllCustomer(), YES)) {
            writeVendorDetails();
        }
        createRow();
        resetColumnIndex();
        if (CommonUtils.isEqualIgnoreCase(apReportsForm.getShowAllCustomer(), YES) || CommonUtils.isEmpty(apReportsForm.getVendorNumber())) {
            createHeaderCell("Vendor Name", tableHeaderCellStyleCenterBold);
            createHeaderCell("Vendor Number", tableHeaderCellStyleCenterBold);
            createHeaderCell("ECU Designation", tableHeaderCellStyleCenterBold);
        }
        if (CommonUtils.isEqualIgnoreCase(apReportsForm.getShowDetailOrSummary(), AP_DETAIL_REPORT)) {
            createHeaderCell("Invoice Number", tableHeaderCellStyleCenterBold);
            createHeaderCell("Invoice Date", tableHeaderCellStyleCenterBold);
            createHeaderCell("Vendor Reference", tableHeaderCellStyleCenterBold);
        }
        createHeaderCell("Type", tableHeaderCellStyleRightBold);
        createHeaderCell("0-30 days", tableHeaderCellStyleRightBold);
        createHeaderCell("31-60 days", tableHeaderCellStyleRightBold);
        createHeaderCell("61-90 days", tableHeaderCellStyleRightBold);
        createHeaderCell("91-120 days", tableHeaderCellStyleRightBold);
        createHeaderCell("121-180 days", tableHeaderCellStyleRightBold);
        createHeaderCell("181+ days", tableHeaderCellStyleRightBold);
        createHeaderCell("Total", tableHeaderCellStyleRightBold);
    }

    private void writeAdjustedAccrualsHeader() throws Exception {
        createRow();
        createHeaderCell("Adjusted Accruals Report", headerCellStyleCenterBold);
        mergeCells(rowIndex, rowIndex, 0, 7);
        row.setHeightInPoints(20);
        createRow();
        resetColumnIndex();
        createHeaderCell("File", tableHeaderCellStyleCenterBold);
        createHeaderCell("Reporting Date", tableHeaderCellStyleCenterBold);
        createHeaderCell("Posted Date", tableHeaderCellStyleCenterBold);
        createHeaderCell("Vendor Name", tableHeaderCellStyleCenterBold);
        createHeaderCell("Vendor Number", tableHeaderCellStyleCenterBold);
        createHeaderCell("Original Amount", tableHeaderCellStyleCenterBold);
        createHeaderCell("Assigned Amount", tableHeaderCellStyleCenterBold);
        createHeaderCell("Difference", tableHeaderCellStyleCenterBold);
    }

    private void writeVendorHeader() throws Exception {
        createRow();
        createHeaderCell("Vendor Report", headerCellStyleCenterBold);
        mergeCells(rowIndex, rowIndex, 0, 15);
        row.setHeightInPoints(20);
        createRow();
        resetColumnIndex();
        createHeaderCell("Vendor Name", tableHeaderCellStyleCenterBold);
        createHeaderCell("Vendor Number", tableHeaderCellStyleCenterBold);
        createHeaderCell("Sub Type", tableHeaderCellStyleCenterBold);
        createHeaderCell("ECI Vendor", tableHeaderCellStyleCenterBold);
        createHeaderCell("AP Specialist", tableHeaderCellStyleCenterBold);
        createHeaderCell("Payment Method", tableHeaderCellStyleCenterBold);
        createHeaderCell("Tin", tableHeaderCellStyleCenterBold);
        createHeaderCell("W-9", tableHeaderCellStyleCenterBold);
        createHeaderCell("Credit", tableHeaderCellStyleCenterBold);
        createHeaderCell("Credit Terms", tableHeaderCellStyleCenterBold);
        createHeaderCell("Credit Limit", tableHeaderCellStyleCenterBold);
        createHeaderCell("AP Contact", tableHeaderCellStyleCenterBold);
        createHeaderCell("Not Paid", tableHeaderCellStyleCenterBold);
        createHeaderCell("Inactive", tableHeaderCellStyleCenterBold);
        createHeaderCell("Exempt from Inactive", tableHeaderCellStyleCenterBold);
        createHeaderCell("Account Added Date", tableHeaderCellStyleCenterBold);
    }

    private void writeActivityHeader() throws Exception {
        createRow();
        createHeaderCell("Activity Report", headerCellStyleLeftBold);
        int endColumn = 11;
        mergeCells(rowIndex, rowIndex, 0, endColumn);
        row.setHeightInPoints(20);
        createRow();
        resetColumnIndex();
        createHeaderCell("Vendor Number :", subHeaderOneCellStyleLeftBold);
        createHeaderCell(apReportsForm.getVendorNumber(), subHeaderOneCellStyleLeftNormal);
        createHeaderCell("Vendor Name :", subHeaderOneCellStyleLeftBold);
        createHeaderCell(apReportsForm.getVendorName(), subHeaderOneCellStyleLeftNormal);
        createEmptyCell(subHeaderOneCellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 4, endColumn);
        createRow();
        resetColumnIndex();
        createHeaderCell("ECU Designation :", subHeaderOneCellStyleLeftBold);
        createHeaderCell(apReportsForm.getEcuDesignation(), subHeaderOneCellStyleLeftNormal);
        createHeaderCell("User Name :", subHeaderOneCellStyleLeftBold);
        createHeaderCell(apReportsForm.getUserName(), subHeaderOneCellStyleLeftNormal);
        createEmptyCell(subHeaderOneCellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 4, endColumn);
        createRow();
        resetColumnIndex();
        createHeaderCell("Date From :", subHeaderTwoCellStyleLeftBold);
        createHeaderCell(apReportsForm.getFromDate(), subHeaderTwoCellStyleLeftNormal);
        createHeaderCell("Date To :", subHeaderTwoCellStyleLeftBold);
        createHeaderCell(apReportsForm.getToDate(), subHeaderTwoCellStyleLeftNormal);
        createEmptyCell(subHeaderTwoCellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 4, endColumn);
        createRow();
        resetColumnIndex();
        createHeaderCell("User", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 30);
        createHeaderCell("Vendor Name", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 40);
        createHeaderCell("Vendor Number", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("Invoice Number", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 30);
        createHeaderCell("File", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 30);
        createHeaderCell("Accrual Amount", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("Transaction Date", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("Posted Date", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("Difference", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("Paid Date", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("Difference", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("Payment Method", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
    }

    private void writeCheckRegistersHeader() throws Exception {
        createRow();
        createHeaderCell("Check Register Report", headerCellStyleCenterBold);
        int endColumn = 7;
        if (CommonUtils.isEqualIgnoreCase(apReportsForm.getShowDetailOrSummary(), AP_DETAIL_REPORT)) {
            endColumn = 9;
        }
        mergeCells(rowIndex, rowIndex, 0, endColumn);
        row.setHeightInPoints(20);
        createRow();
        resetColumnIndex();
        String status = "ALL";
        if (CommonUtils.isEqualIgnoreCase(apReportsForm.getCheckStatus(), YES)) {
            status = "Cleared";

        } else if (CommonUtils.isEqualIgnoreCase(apReportsForm.getCheckStatus(), NO)) {
            status = "Not Cleared";
        } else if (CommonUtils.isEqualIgnoreCase(apReportsForm.getCheckStatus(), STATUS_VOID)) {
            status = "Voided";
        }
        createHeaderCell("Bank Account :", subHeaderOneCellStyleLeftBold);
        createHeaderCell(apReportsForm.getBankAccount(), subHeaderOneCellStyleLeftNormal);
        createHeaderCell("GL Account :", subHeaderOneCellStyleLeftBold);
        createHeaderCell(apReportsForm.getGlAccount(), subHeaderOneCellStyleLeftNormal);
        createEmptyCell(subHeaderOneCellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 4, endColumn);
        createRow();
        resetColumnIndex();
        createHeaderCell("Check From :", subHeaderTwoCellStyleLeftBold);
        createHeaderCell(apReportsForm.getFromCheck(), subHeaderTwoCellStyleLeftNormal);
        createHeaderCell("Check To :", subHeaderTwoCellStyleLeftBold);
        createHeaderCell(apReportsForm.getToCheck(), subHeaderTwoCellStyleLeftNormal);
        createEmptyCell(subHeaderTwoCellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 4, endColumn);
        createRow();
        resetColumnIndex();
        createHeaderCell("Date From :", subHeaderOneCellStyleLeftBold);
        createHeaderCell(apReportsForm.getFromDate(), subHeaderOneCellStyleLeftNormal);
        createHeaderCell("Date To :", subHeaderOneCellStyleLeftBold);
        createHeaderCell(apReportsForm.getToDate(), subHeaderOneCellStyleLeftNormal);
        createEmptyCell(subHeaderOneCellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 4, endColumn);
        createRow();
        resetColumnIndex();
        createHeaderCell("Payment Method :", subHeaderTwoCellStyleLeftBold);
        createHeaderCell(apReportsForm.getPaymentMethod(), subHeaderTwoCellStyleLeftNormal);
        createHeaderCell("Status :", subHeaderTwoCellStyleLeftBold);
        createHeaderCell(status, subHeaderTwoCellStyleLeftNormal);
        createEmptyCell(subHeaderTwoCellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 4, endColumn);
        createRow();
        resetColumnIndex();
        createHeaderCell("Check Number", tableHeaderCellStyleCenterBold);
        createHeaderCell("Bank Account", tableHeaderCellStyleCenterBold);
        createHeaderCell("Payee Name", tableHeaderCellStyleCenterBold);
        createHeaderCell("Payee Number", tableHeaderCellStyleCenterBold);
        if (CommonUtils.isEqualIgnoreCase(apReportsForm.getShowDetailOrSummary(), AP_DETAIL_REPORT)) {
            createHeaderCell("Invoice Number", tableHeaderCellStyleCenterBold);
        }
        createHeaderCell("Pay Date", tableHeaderCellStyleCenterBold);
        createHeaderCell("Amount", tableHeaderCellStyleCenterBold);
        createHeaderCell("Payment Type", tableHeaderCellStyleCenterBold);
        createHeaderCell("Status", tableHeaderCellStyleCenterBold);
        if (CommonUtils.isEqualIgnoreCase(apReportsForm.getShowDetailOrSummary(), AP_DETAIL_REPORT)) {
            createHeaderCell("Check Total", tableHeaderCellStyleCenterBold);
        }
    }

    private void writeDpoHeader() throws Exception {
        createRow();
        createHeaderCell("DPO Report", headerCellStyleCenterBold);
        int endColumn = 3;
        mergeCells(rowIndex, rowIndex, 0, endColumn);
        row.setHeightInPoints(20);
        String dpoFor = "All Account Payables";
        if (apReportsForm.getSearchDpoBy().equalsIgnoreCase(BY_USER)) {
            dpoFor = "User";
        } else if (apReportsForm.getSearchDpoBy().equalsIgnoreCase(BY_VENDOR)) {
            dpoFor = "Vendor";
        }
        createRow();
        resetColumnIndex();
        createHeaderCell("DPO For :", subHeaderOneCellStyleLeftBold);
        createHeaderCell(dpoFor, subHeaderOneCellStyleLeftNormal);
        createEmptyCell(subHeaderOneCellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 2, endColumn);
        createRow();
        resetColumnIndex();
        createHeaderCell("From Period :", subHeaderTwoCellStyleLeftBold);
        createHeaderCell(apReportsForm.getFromPeriod(), subHeaderTwoCellStyleLeftNormal);
        createEmptyCell(subHeaderTwoCellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 2, endColumn);
        createRow();
        resetColumnIndex();
        createHeaderCell("To Period :", subHeaderOneCellStyleLeftBold);
        createHeaderCell(apReportsForm.getToPeriod(), subHeaderOneCellStyleLeftNormal);
        createEmptyCell(subHeaderOneCellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 2, endColumn);
        createRow();
        resetColumnIndex();
        createHeaderCell("Number Of Days :", subHeaderTwoCellStyleLeftBold);
        createHeaderCell(apReportsForm.getNumberOfDays(), subHeaderTwoCellStyleLeftNormal);
        createEmptyCell(subHeaderTwoCellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 2, endColumn);
        if (apReportsForm.getSearchDpoBy().equalsIgnoreCase(BY_USER)) {
            createRow();
            resetColumnIndex();
            createHeaderCell("User Name :", subHeaderOneCellStyleLeftBold);
            createHeaderCell(apReportsForm.getUserName(), subHeaderOneCellStyleLeftNormal);
            createEmptyCell(subHeaderOneCellStyleLeftNormal);
            mergeCells(rowIndex, rowIndex, 2, endColumn);
        } else if (apReportsForm.getSearchDpoBy().equalsIgnoreCase(BY_VENDOR)) {
            createRow();
            resetColumnIndex();
            createHeaderCell("Vendor Name :", subHeaderOneCellStyleLeftBold);
            createHeaderCell(apReportsForm.getVendorName(), subHeaderOneCellStyleLeftNormal);
            createEmptyCell(subHeaderOneCellStyleLeftNormal);
            mergeCells(rowIndex, rowIndex, 2, endColumn);
            createRow();
            resetColumnIndex();
            createHeaderCell("Vendor Number :", subHeaderTwoCellStyleLeftBold);
            createHeaderCell(apReportsForm.getVendorNumber(), subHeaderTwoCellStyleLeftNormal);
            createEmptyCell(subHeaderTwoCellStyleLeftNormal);
            mergeCells(rowIndex, rowIndex, 2, endColumn);
        }
        createRow();
        resetColumnIndex();
        createHeaderCell("Total Amount Open Payables", tableHeaderCellStyleCenterBold);
        createHeaderCell("Total Amount Cost", tableHeaderCellStyleCenterBold);
        createHeaderCell("Selected Period", tableHeaderCellStyleCenterBold);
        createHeaderCell("DPO ratio", tableHeaderCellStyleCenterBold);
    }

    private void writeDisputedItemsHeader() throws Exception {
        createRow();
        createHeaderCell("Disputed Items Report", headerCellStyleCenterBold);
        int endColumn = 14;
        mergeCells(rowIndex, rowIndex, 0, endColumn);
        row.setHeightInPoints(20);
        createRow();
        resetColumnIndex();
        createHeaderCell("From Date : ", subHeaderOneCellStyleLeftBold);
        createHeaderCell(apReportsForm.getFromDate(), subHeaderOneCellStyleLeftNormal);
        createHeaderCell("To Date : ", subHeaderOneCellStyleLeftBold);
        createHeaderCell(apReportsForm.getToDate(), subHeaderOneCellStyleLeftNormal);
        createEmptyCell(subHeaderOneCellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 4, endColumn);
        createRow();
        resetColumnIndex();
        createHeaderCell("Vendor Name", tableHeaderCellStyleCenterBold);
        createHeaderCell("Vendor Number", tableHeaderCellStyleCenterBold);
        createHeaderCell("Invoice Number", tableHeaderCellStyleCenterBold);
        createHeaderCell("Invoice Date", tableHeaderCellStyleCenterBold);
        createHeaderCell("Invoice Amount", tableHeaderCellStyleCenterBold);
        createHeaderCell("Dock Receipt", tableHeaderCellStyleCenterBold);
        createHeaderCell("Voyage Number", tableHeaderCellStyleCenterBold);
        createHeaderCell("Accrual Amount", tableHeaderCellStyleCenterBold);
        createHeaderCell("AP Specialist", tableHeaderCellStyleCenterBold);
        createHeaderCell("Billing Terminal", tableHeaderCellStyleCenterBold);
        createHeaderCell("User", tableHeaderCellStyleCenterBold);
        createHeaderCell("Disputed Date", tableHeaderCellStyleCenterBold);
        createHeaderCell("Resolved Date", tableHeaderCellStyleCenterBold);
        createHeaderCell("Resolution Period", tableHeaderCellStyleCenterBold);
        createHeaderCell("Comments", tableHeaderCellStyleCenterBold);
    }

    private void writeDisputedEmailLogHeader() throws Exception {
        createRow();
        createHeaderCell("Disputed Email Log Report", headerCellStyleCenterBold);
        mergeCells(rowIndex, rowIndex, 0, 9);
        row.setHeightInPoints(20);
        createRow();
        resetColumnIndex();
        createHeaderCell("Vendor Name", tableHeaderCellStyleCenterBold);
        createHeaderCell("Vendor Number", tableHeaderCellStyleCenterBold);
        createHeaderCell("Invoice Number", tableHeaderCellStyleCenterBold);
        createHeaderCell("From Address", tableHeaderCellStyleCenterBold);
        createHeaderCell("To Address", tableHeaderCellStyleCenterBold);
        createHeaderCell("Cc Address", tableHeaderCellStyleCenterBold);
        createHeaderCell("Bcc Address", tableHeaderCellStyleCenterBold);
        createHeaderCell("Subject", tableHeaderCellStyleCenterBold);
        createHeaderCell("Html Message", tableHeaderCellStyleCenterBold);
        createHeaderCell("Status", tableHeaderCellStyleCenterBold);
    }

    private void writeHeader() throws Exception {
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
        } else if (CommonUtils.isEqualIgnoreCase(apReportsForm.getReportType(), AP_DISPUTED_EMAIL_LOG_REPORT)) {
            writeDisputedEmailLogHeader();
        }
    }

    private void writeDetailedAgingContent(List<ReportBean> transactions, boolean isAllVendors) throws Exception {
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
        int rowCount = 0;
        for (ReportBean transaction : transactions) {
            createRow();
            resetColumnIndex();
            CellStyle textCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleLeftNormal : tableOddRowCellStyleLeftNormal;
            CellStyle doubleCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleRightNormal : tableOddRowCellStyleRightNormal;
            if (isAllVendors) {
                createTextCell(transaction.getVendorName(), textCellStyle);
                createTextCell(transaction.getVendorNumber(), textCellStyle);
                createTextCell(transaction.getEcuDesignation(), textCellStyle);
            }
            createTextCell(transaction.getInvoiceNumber(), textCellStyle);
            createTextCell(transaction.getInvoiceDate(), textCellStyle);
            createTextCell(transaction.getVendorReference(), textCellStyle);
            createTextCell(transaction.getTransactionType(), textCellStyle);
            createDoubleCell(transaction.getAge30Days(), doubleCellStyle);
            createDoubleCell(transaction.getAge60Days(), doubleCellStyle);
            createDoubleCell(transaction.getAge90Days(), doubleCellStyle);
            createDoubleCell(transaction.getAge120Days(), doubleCellStyle);
            createDoubleCell(transaction.getAge180Days(), doubleCellStyle);
            createDoubleCell(transaction.getAge181Days(), doubleCellStyle);
            createDoubleCell(transaction.getAgeTotal(), doubleCellStyle);
            age30Total += NumberUtils.parseNumber(transaction.getAge30Days());
            age60Total += NumberUtils.parseNumber(transaction.getAge60Days());
            age90Total += NumberUtils.parseNumber(transaction.getAge90Days());
            age120Total += NumberUtils.parseNumber(transaction.getAge120Days());
            age180Total += NumberUtils.parseNumber(transaction.getAge180Days());
            age181Total += NumberUtils.parseNumber(transaction.getAge181Days());
            ageTotal += NumberUtils.parseNumber(transaction.getAgeTotal());
            boolean isTotaled = false;
            if (transactions.size() > rowCount + 1) {
                ReportBean nextTransaction = transactions.get(rowCount + 1);
                if (CommonUtils.isNotEqualIgnoreCase(transaction.getVendorNumber(), nextTransaction.getVendorNumber())) {
                    isTotaled = true;
                }
            } else {
                isTotaled = true;
            }
            if (isTotaled) {
                createRow();
                resetColumnIndex();
                if (isAllVendors) {
                    createEmptyCell(lavendarCellStyleRightBold);
                    createEmptyCell(lavendarCellStyleRightBold);
                    createEmptyCell(lavendarCellStyleRightBold);
                }
                createEmptyCell(lavendarCellStyleRightBold);
                createEmptyCell(lavendarCellStyleRightBold);
                createEmptyCell(lavendarCellStyleRightBold);
                createTextCell("Total", lavendarCellStyleRightBold);
                createDoubleCell(age30Total, lavendarCellStyleRightBold);
                createDoubleCell(age60Total, lavendarCellStyleRightBold);
                createDoubleCell(age90Total, lavendarCellStyleRightBold);
                createDoubleCell(age120Total, lavendarCellStyleRightBold);
                createDoubleCell(age180Total, lavendarCellStyleRightBold);
                createDoubleCell(age181Total, lavendarCellStyleRightBold);
                createDoubleCell(ageTotal, lavendarCellStyleRightBold);
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
            rowCount++;
        }
        createRow();
        resetColumnIndex();
        if (isAllVendors) {
            createEmptyCell(lightAshCellStyleRightBold);
            createEmptyCell(lightAshCellStyleRightBold);
            createEmptyCell(lightAshCellStyleRightBold);
        }
        createEmptyCell(lightAshCellStyleRightBold);
        createEmptyCell(lightAshCellStyleRightBold);
        createEmptyCell(lightAshCellStyleRightBold);
        createTextCell("Grand Total", lightAshCellStyleRightBold);
        createDoubleCell(age30GrandTotal, lightAshCellStyleRightBold);
        createDoubleCell(age60GrandTotal, lightAshCellStyleRightBold);
        createDoubleCell(age90GrandTotal, lightAshCellStyleRightBold);
        createDoubleCell(age120GrandTotal, lightAshCellStyleRightBold);
        createDoubleCell(age180GrandTotal, lightAshCellStyleRightBold);
        createDoubleCell(age181GrandTotal, lightAshCellStyleRightBold);
        createDoubleCell(ageGrandTotal, lightAshCellStyleRightBold);
    }

    private void writeSummarizedAgingContent(List<ReportBean> transactions, boolean isAllVendors) throws Exception {
        double age30GrandTotal = 0d;
        double age60GrandTotal = 0d;
        double age90GrandTotal = 0d;
        double age120GrandTotal = 0d;
        double age180GrandTotal = 0d;
        double age181GrandTotal = 0d;
        double ageGrandTotal = 0d;
        int rowCount = 0;
        for (ReportBean transaction : transactions) {
            createRow();
            resetColumnIndex();
            CellStyle textCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleLeftNormal : tableOddRowCellStyleLeftNormal;
            CellStyle doubleCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleRightNormal : tableOddRowCellStyleRightNormal;
            if (isAllVendors) {
                createTextCell(transaction.getVendorName(), textCellStyle);
                createTextCell(transaction.getVendorNumber(), textCellStyle);
                createTextCell(transaction.getEcuDesignation(), textCellStyle);
            }
            createTextCell(transaction.getTransactionType(), textCellStyle);
            createDoubleCell(transaction.getAge30Days(), doubleCellStyle);
            createDoubleCell(transaction.getAge60Days(), doubleCellStyle);
            createDoubleCell(transaction.getAge90Days(), doubleCellStyle);
            createDoubleCell(transaction.getAge120Days(), doubleCellStyle);
            createDoubleCell(transaction.getAge180Days(), doubleCellStyle);
            createDoubleCell(transaction.getAge181Days(), doubleCellStyle);
            createDoubleCell(transaction.getAgeTotal(), doubleCellStyle);
            age30GrandTotal += NumberUtils.parseNumber(transaction.getAge30Days());
            age60GrandTotal += NumberUtils.parseNumber(transaction.getAge60Days());
            age90GrandTotal += NumberUtils.parseNumber(transaction.getAge90Days());
            age120GrandTotal += NumberUtils.parseNumber(transaction.getAge120Days());
            age180GrandTotal += NumberUtils.parseNumber(transaction.getAge180Days());
            age181GrandTotal += NumberUtils.parseNumber(transaction.getAge181Days());
            ageGrandTotal += NumberUtils.parseNumber(transaction.getAgeTotal());
            rowCount++;
        }
        createRow();
        resetColumnIndex();
        if (isAllVendors) {
            createEmptyCell(lightAshCellStyleRightBold);
            createEmptyCell(lightAshCellStyleRightBold);
            createEmptyCell(lightAshCellStyleRightBold);
        }
        createTextCell("Grand Total", lightAshCellStyleRightBold);
        createDoubleCell(age30GrandTotal, lightAshCellStyleRightBold);
        createDoubleCell(age60GrandTotal, lightAshCellStyleRightBold);
        createDoubleCell(age90GrandTotal, lightAshCellStyleRightBold);
        createDoubleCell(age120GrandTotal, lightAshCellStyleRightBold);
        createDoubleCell(age180GrandTotal, lightAshCellStyleRightBold);
        createDoubleCell(age181GrandTotal, lightAshCellStyleRightBold);
        createDoubleCell(ageGrandTotal, lightAshCellStyleRightBold);
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
        int rowCount = 0;
        for (ReportBean adjustedAccrual : adjustedAccruals) {
            createRow();
            resetColumnIndex();
            CellStyle textCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleLeftNormal : tableOddRowCellStyleLeftNormal;
            CellStyle doubleCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleRightNormal : tableOddRowCellStyleRightNormal;
            createTextCell(adjustedAccrual.getFile(), textCellStyle);
            createTextCell(adjustedAccrual.getReportingDate(), textCellStyle);
            createTextCell(adjustedAccrual.getPostedDate(), textCellStyle);
            createTextCell(adjustedAccrual.getVendorName(), textCellStyle);
            createTextCell(adjustedAccrual.getVendorNumber(), textCellStyle);
            createDoubleCell(adjustedAccrual.getOriginalAmount(), doubleCellStyle);
            createDoubleCell(adjustedAccrual.getAssignedAmount(), doubleCellStyle);
            createDoubleCell(adjustedAccrual.getDifferenceAmount(), doubleCellStyle);
            rowCount++;
        }
    }

    private void writeVendorContent() throws Exception {
        List<ReportBean> vendors = new ApReportsDAO().getVendors();
        int rowCount = 0;
        for (ReportBean vendor : vendors) {
            createRow();
            resetColumnIndex();
            CellStyle textCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleLeftNormal : tableOddRowCellStyleLeftNormal;
            CellStyle doubleCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleRightNormal : tableOddRowCellStyleRightNormal;
            createTextCell(vendor.getVendorName(), textCellStyle);
            createTextCell(vendor.getVendorNumber(), textCellStyle);
            createTextCell(vendor.getSubType(), textCellStyle);
            createTextCell(vendor.getEciVendorNumber(), textCellStyle);
            createTextCell(vendor.getApSpecialist(), textCellStyle);
            createTextCell(vendor.getPaymentMethod(), textCellStyle);
            createTextCell(vendor.getTin(), textCellStyle);
            createTextCell(vendor.getW9(), textCellStyle);
            createTextCell(vendor.getCreditStatus(), textCellStyle);
            createTextCell(vendor.getCreditTerms(), textCellStyle);
            createDoubleCell(vendor.getCreditLimit(), doubleCellStyle);
            createTextCell(vendor.getApContact(), textCellStyle);
            createTextCell(vendor.getNotPaid(), textCellStyle);
            createTextCell(vendor.getInactiveAccount(), textCellStyle);
            createTextCell(vendor.getExemptInactivate(), textCellStyle);
            createTextCell(vendor.getAccountAddedDate(), textCellStyle);
            rowCount++;
        }
    }

    private void writeActivityContent() throws Exception {
        List<ReportBean> activities = new ApReportsDAO().getActivities(apReportsForm);
        int rowCount = 0;
        for (ReportBean activity : activities) {
            createRow();
            resetColumnIndex();
            CellStyle textCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleLeftNormal : tableOddRowCellStyleLeftNormal;
            CellStyle doubleCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleRightNormal : tableOddRowCellStyleRightNormal;
            createTextCell(activity.getUserLoginName(), textCellStyle);
            createTextCell(activity.getVendorName(), textCellStyle);
            createTextCell(activity.getVendorNumber(), textCellStyle);
            createTextCell(activity.getInvoiceNumber(), textCellStyle);
            createTextCell(activity.getFile(), textCellStyle);
            createDoubleCell(activity.getAccrualAmount(), doubleCellStyle);
            createTextCell(activity.getTransactionDate(), textCellStyle);
            createTextCell(activity.getPostedDate(), textCellStyle);
            createIntegerCell(activity.getPostedTransactionDifference(), textCellStyle);
            createTextCell(activity.getPaidDate(), textCellStyle);
            createIntegerCell(activity.getPaidPostedDifference(), textCellStyle);
            createTextCell(activity.getPaymentMethod(), textCellStyle);
            rowCount++;
        }
    }

    private void writeDetailedCheckRegistersContent(List<ReportBean> checkRegisters) throws Exception {
        double grandTotal = 0d;
        double checkTotal = 0d;
        int rowCount = 0;
        for (ReportBean checkRegister : checkRegisters) {
            createRow();
            resetColumnIndex();
            CellStyle textCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleLeftNormal : tableOddRowCellStyleLeftNormal;
            CellStyle doubleCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleRightNormal : tableOddRowCellStyleRightNormal;
            createTextCell(checkRegister.getCheckNumber(), textCellStyle);
            createTextCell(checkRegister.getBankAccount(), textCellStyle);
            createTextCell(checkRegister.getVendorName(), textCellStyle);
            createTextCell(checkRegister.getVendorNumber(), textCellStyle);
            createTextCell(checkRegister.getInvoiceNumber(), textCellStyle);
            createTextCell(checkRegister.getPaymentDate(), textCellStyle);
            createDoubleCell(checkRegister.getPaymentAmount(), doubleCellStyle);
            createTextCell(checkRegister.getPaymentMethod(), textCellStyle);
            createTextCell(checkRegister.getStatus(), textCellStyle);
            checkTotal += Double.parseDouble(checkRegister.getPaymentAmount().replace(",", ""));
            boolean isTotaled = false;
            if (checkRegisters.size() > rowCount + 1) {
                ReportBean nextTransaction = checkRegisters.get(rowCount + 1);
                if (CommonUtils.isNotEqualIgnoreCase(checkRegister.getCheckNumber(), nextTransaction.getCheckNumber())) {
                    isTotaled = true;
                }
            } else {
                isTotaled = true;
            }
            if (isTotaled) {
                createDoubleCell(checkTotal, doubleCellStyle);
                grandTotal += checkTotal;
                isTotaled = false;
                checkTotal = 0d;
            } else {
                createEmptyCell(doubleCellStyle);
            }
            rowCount++;
        }
        createRow();
        resetColumnIndex();
        createEmptyCell(darkAshCellStyleRightBold);
        createEmptyCell(darkAshCellStyleRightBold);
        createEmptyCell(darkAshCellStyleRightBold);
        createEmptyCell(darkAshCellStyleRightBold);
        createEmptyCell(darkAshCellStyleRightBold);
        createTextCell("Grand Total", darkAshCellStyleRightBold);
        createDoubleCell(grandTotal, darkAshCellStyleRightBold);
        createEmptyCell(darkAshCellStyleRightBold);
        createEmptyCell(darkAshCellStyleRightBold);
        createDoubleCell(grandTotal, darkAshCellStyleRightBold);
    }

    private void writeSummarizedCheckRegistersContent(List<ReportBean> checkRegisters) throws Exception {
        double grandTotal = 0d;
        int rowCount = 0;
        for (ReportBean checkRegister : checkRegisters) {
            createRow();
            resetColumnIndex();
            CellStyle textCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleLeftNormal : tableOddRowCellStyleLeftNormal;
            CellStyle doubleCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleRightNormal : tableOddRowCellStyleRightNormal;
            createTextCell(checkRegister.getCheckNumber(), textCellStyle);
            createTextCell(checkRegister.getBankAccount(), textCellStyle);
            createTextCell(checkRegister.getVendorName(), textCellStyle);
            createTextCell(checkRegister.getVendorNumber(), textCellStyle);
            createTextCell(checkRegister.getPaymentDate(), textCellStyle);
            createDoubleCell(checkRegister.getPaymentAmount(), doubleCellStyle);
            createTextCell(checkRegister.getPaymentMethod(), textCellStyle);
            createTextCell(checkRegister.getStatus(), textCellStyle);
            grandTotal += Double.parseDouble(checkRegister.getPaymentAmount().replace(",", ""));
            rowCount++;
        }
        createRow();
        resetColumnIndex();
        createEmptyCell(darkAshCellStyleRightBold);
        createEmptyCell(darkAshCellStyleRightBold);
        createEmptyCell(darkAshCellStyleRightBold);
        createEmptyCell(darkAshCellStyleRightBold);
        createTextCell("Grand Total", darkAshCellStyleRightBold);
        createDoubleCell(grandTotal, darkAshCellStyleRightBold);
        createEmptyCell(darkAshCellStyleRightBold);
        createEmptyCell(darkAshCellStyleRightBold);
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
        createRow();
        resetColumnIndex();
        createDoubleCell(dpo.getOpenPayables(), lavendarCellStyleRightNormal);
        createDoubleCell(dpo.getTotalCosts(), lavendarCellStyleRightNormal);
        createIntegerCell(dpo.getNumberOfDays(), lavendarCellStyleRightNormal);
        createDoubleCell(dpo.getDpoRatio(), lavendarCellStyleCenterNormal);
    }

    private void writeDisputedItemsContent() throws Exception {
        List<ReportBean> disputedItems = new ApReportsDAO().getDisputedItems(apReportsForm);
        String invoiceAmount;
        int rowCount = 0;
        boolean isNewVendorNumber = true;
        for (ReportBean disputedItem : disputedItems) {
            createRow();
            resetColumnIndex();
            CellStyle textCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleLeftNormal : tableOddRowCellStyleLeftNormal;
            CellStyle doubleCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleRightNormal : tableOddRowCellStyleRightNormal;
            invoiceAmount = isNewVendorNumber ? disputedItem.getInvoiceAmount() : "";
            if (disputedItems.size() > rowCount + 1) {
                ReportBean nextDisputedItem = disputedItems.get(rowCount + 1);
                if (CommonUtils.isNotEqualIgnoreCase(disputedItem.getVendorNumber(), nextDisputedItem.getVendorNumber())
                        || CommonUtils.isNotEqualIgnoreCase(disputedItem.getInvoiceNumber(), nextDisputedItem.getInvoiceNumber())) {
                    isNewVendorNumber = true;
                } else {
                    isNewVendorNumber = false;
                }
            }
            createTextCell(disputedItem.getVendorName(), textCellStyle);
            createTextCell(disputedItem.getVendorNumber(), textCellStyle);
            createTextCell(disputedItem.getInvoiceNumber(), textCellStyle);
            createTextCell(disputedItem.getInvoiceDate(), textCellStyle);
            createDoubleCell(invoiceAmount, doubleCellStyle);
            createTextCell(disputedItem.getDockReceipt(), textCellStyle);
            createTextCell(disputedItem.getVoyageNumber(), textCellStyle);
            createDoubleCell(disputedItem.getAccrualAmount(), doubleCellStyle);
            String apSpecialist = null != disputedItem.getApSpecialist()? disputedItem.getApSpecialist(): "";
            createTextCell(apSpecialist, textCellStyle);
            createTextCell(disputedItem.getBillingTerminal(), textCellStyle);
            String userName = null != disputedItem.getUserLoginName() ? disputedItem.getUserLoginName() : "";
            createTextCell(userName, textCellStyle);
            createTextCell(disputedItem.getDisputedDate(), textCellStyle);
            createTextCell(disputedItem.getResolvedDate(), textCellStyle);
            createIntegerCell(disputedItem.getResolutionPeriod(), textCellStyle);
            StringBuilder comments = new StringBuilder();
            if (CommonUtils.isNotEmpty(disputedItem.getNotesDescription())) {
                int count = 1;
                for (String comment : StringUtils.splitByWholeSeparator(disputedItem.getNotesDescription(), "<--->")) {
                    comments.append(count).append(") ").append(comment).append("\n");
                    count++;
                }
            }
            createTextCell(comments.toString(), textCellStyle);
            rowCount++;
        }
    }
    
    private void writeDisputedEmailLogContent() throws Exception {
        ApReportsDAO apReportsDAO = new ApReportsDAO();
        boolean isPagination = false;
        String conditions = apReportsDAO.buildDisputedEmailLogQuery(apReportsForm);
        List<ReportBean> disputedEmailLogs = apReportsDAO.getDisputedEmailLogs(conditions, 0, 0, isPagination);
        int rowCount = 0;
        for (ReportBean disputedEmailLog : disputedEmailLogs) {
            createRow();
            resetColumnIndex();
            CellStyle textCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleLeftNormal : tableOddRowCellStyleLeftNormal;
            CellStyle doubleCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleRightNormal : tableOddRowCellStyleRightNormal;
            createTextCell(disputedEmailLog.getVendorName(), textCellStyle);
            createTextCell(disputedEmailLog.getVendorNumber(), textCellStyle);
            createTextCell(disputedEmailLog.getInvoiceNumber(), textCellStyle);
            createTextCell(disputedEmailLog.getFromAddress(), textCellStyle);
            createTextCell(disputedEmailLog.getToAddress(), textCellStyle);
            createTextCell(disputedEmailLog.getCcAddress(), textCellStyle);
            createTextCell(disputedEmailLog.getBccAddress(), textCellStyle);
            createTextCell(disputedEmailLog.getSubject(), textCellStyle);
            createTextCell(disputedEmailLog.getHtmlMessage().replaceAll("<br />", ""), textCellStyle);
            createTextCell(disputedEmailLog.getStatus(), textCellStyle);
            rowCount++;
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
        } else if (CommonUtils.isEqualIgnoreCase(apReportsForm.getReportType(), AP_DISPUTED_EMAIL_LOG_REPORT)) {
            writeDisputedEmailLogContent();
        }
        setColumnAutoSize();
    }

    public String createExcel() throws Exception {
        try {
            StringBuilder fileNameBuilder = new StringBuilder();
            fileNameBuilder.append(LoadLogisoftProperties.getProperty("reportLocation")).append("/Documents/ApReports/");
            fileNameBuilder.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
            File file = new File(fileNameBuilder.toString());
            if (!file.exists()) {
                file.mkdirs();
            }
            fileNameBuilder.append(apReportsForm.getReportType()).append(".xlsx");
            init(fileNameBuilder.toString(), apReportsForm.getReportType());
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
    
    public String createReportForManager() throws Exception {
        try {
            List<ReportBean> disputedItems = new ApReportsDAO().getTrmManagerItems(apReportsForm);
            if (CommonUtils.isNotEmpty(disputedItems)) {
                StringBuilder fileNameBuilder = new StringBuilder();
                fileNameBuilder.append(LoadLogisoftProperties.getProperty("reportLocation")).append("/Documents/ApReports/");
                fileNameBuilder.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
                File file = new File(fileNameBuilder.toString());
                if (!file.exists()) {
                    file.mkdirs();
                }
                fileNameBuilder.append("ApDispute_");
                fileNameBuilder.append(apReportsForm.getManagerName().replace(" ", "_"));
                fileNameBuilder.append("_").append(DateUtils.formatDate(new Date(), "yyyy_MM_dd_kkmmss"));
                fileNameBuilder.append(".xlsx");
                init(fileNameBuilder.toString(), apReportsForm.getReportType());
                writeHeader();
                writeDisputedItemsTrmManager(disputedItems);
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
    
    private void writeDisputedItemsTrmManager(List<ReportBean> disputedItems) throws Exception {
        String invoiceAmount;
        int rowCount = 0;
        boolean isNewVendorNumber = true;
        for (ReportBean disputedItem : disputedItems) {
            createRow();
            resetColumnIndex();
            CellStyle textCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleLeftNormal : tableOddRowCellStyleLeftNormal;
            CellStyle doubleCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleRightNormal : tableOddRowCellStyleRightNormal;
            invoiceAmount = isNewVendorNumber ? disputedItem.getInvoiceAmount() : "";
            if (disputedItems.size() > rowCount + 1) {
                ReportBean nextDisputedItem = disputedItems.get(rowCount + 1);
                if (CommonUtils.isNotEqualIgnoreCase(disputedItem.getVendorNumber(), nextDisputedItem.getVendorNumber())
                        || CommonUtils.isNotEqualIgnoreCase(disputedItem.getInvoiceNumber(), nextDisputedItem.getInvoiceNumber())) {
                    isNewVendorNumber = true;
                } else {
                    isNewVendorNumber = false;
                }
            }
            createTextCell(disputedItem.getVendorName(), textCellStyle);
            createTextCell(disputedItem.getVendorNumber(), textCellStyle);
            createTextCell(disputedItem.getInvoiceNumber(), textCellStyle);
            createTextCell(disputedItem.getInvoiceDate(), textCellStyle);
            createDoubleCell(invoiceAmount, doubleCellStyle);
            createTextCell(disputedItem.getDockReceipt(), textCellStyle);
            createTextCell(disputedItem.getVoyageNumber(), textCellStyle);
            createDoubleCell(disputedItem.getAccrualAmount(), doubleCellStyle);
            String apSpecialist = null != disputedItem.getApSpecialist()? disputedItem.getApSpecialist(): "";
            createTextCell(apSpecialist, textCellStyle);
            createTextCell(disputedItem.getBillingTerminal(), textCellStyle);
            String userName = null != disputedItem.getUserLoginName() ? disputedItem.getUserLoginName() : "";
            createTextCell(userName, textCellStyle);
            createTextCell(disputedItem.getDisputedDate(), textCellStyle);
            createTextCell(disputedItem.getResolvedDate(), textCellStyle);
            createIntegerCell(disputedItem.getResolutionPeriod(), textCellStyle);
            StringBuilder comments = new StringBuilder();
            if (CommonUtils.isNotEmpty(disputedItem.getNotesDescription())) {
                int count = 1;
                for (String comment : StringUtils.splitByWholeSeparator(disputedItem.getNotesDescription(), "<--->")) {
                    comments.append(count).append(") ").append(comment).append("\n");
                    count++;
                }
            }
            createTextCell(comments.toString(), textCellStyle);
            rowCount++;
        }
    }
    
}
