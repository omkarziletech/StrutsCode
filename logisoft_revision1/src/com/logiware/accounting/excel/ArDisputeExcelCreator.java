/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.accounting.excel;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.accounting.dao.ArReportsDAO;
import com.logiware.accounting.form.ArReportsForm;
import com.logiware.bean.ReportBean;
import com.logiware.excel.BaseExcelCreator;
import java.io.File;
import java.util.Date;
import java.util.List;

/**
 *
 * @author venugopal.s
 */
public class ArDisputeExcelCreator extends BaseExcelCreator {

    private ArReportsForm arReportsForm;

    public ArDisputeExcelCreator() {
    }

    public ArDisputeExcelCreator(ArReportsForm arReportsForm) {
        this.arReportsForm = arReportsForm;
    }

    private void writeHeader() throws Exception {
        createRow();
        createHeaderCell("AR Dispute Report", headerCellStyleCenterBold);
        mergeCells(rowIndex, rowIndex, 0, 8);
        row.setHeightInPoints(20);
        if (arReportsForm.isAllCustomers()) {
            createRow();
            resetColumnIndex();
            createHeaderCell("For ALL Customers", subHeaderOneCellStyleLeftBold);
            mergeCells(rowIndex, rowIndex, 0, 8);
        } else if (CommonUtils.isNotEmpty(arReportsForm.getCollector())) {
            createRow();
            resetColumnIndex();
            createHeaderCell("For Collector", subHeaderOneCellStyleLeftBold);
            createHeaderCell(arReportsForm.getCollector(), subHeaderTwoCellStyleLeftNormal);
            mergeCells(rowIndex, rowIndex, 1, 8);
        } else if (CommonUtils.isNotEmpty(arReportsForm.getCustomerNumber())) {
            createRow();
            resetColumnIndex();
            createHeaderCell("Customer Name", subHeaderOneCellStyleLeftBold);
            createHeaderCell(arReportsForm.getCustomerName(), subHeaderOneCellStyleLeftNormal);
            createHeaderCell("Customer Number", subHeaderOneCellStyleLeftBold);
            createHeaderCell(arReportsForm.getCustomerNumber(), subHeaderOneCellStyleLeftNormal);
            mergeCells(rowIndex, rowIndex, 3, 8);
        }
        createRow();
        resetColumnIndex();
        createHeaderCell("Date From", subHeaderTwoCellStyleLeftBold);
        createHeaderCell(arReportsForm.getFromDate(), subHeaderTwoCellStyleLeftNormal);
        createHeaderCell("Date To", subHeaderTwoCellStyleLeftBold);
        createHeaderCell(arReportsForm.getToDate(), subHeaderTwoCellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 3, 8);

        createRow();
        resetColumnIndex();
        createHeaderCell("Customer Number", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("Customer Name", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 45);
        createHeaderCell("Invoice Date", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("Invoice/BL", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 25);
        createHeaderCell("Invoice Amount", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("Invoice Balance", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("Disputed Date", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("Disputed By", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("Disputed To", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 30);

    }

    private void writeContent() throws Exception {
        List<ReportBean> diputeList = new ArReportsDAO().getDisputeList(arReportsForm);
        int rowCount = 0;
        for (ReportBean dispute : diputeList) {
            rowCount++;
            createRow();
            resetColumnIndex();
            if (rowCount % 2 == 0) {
                createTextCell(dispute.getCustomerNumber(), tableEvenRowCellStyleLeftNormal);
                createTextCell(dispute.getCustomerName(), tableEvenRowCellStyleLeftNormal);
                createTextCell(dispute.getInvoiceDate(), tableEvenRowCellStyleLeftNormal);
                createTextCell(dispute.getInvoiceOrBl(), tableEvenRowCellStyleLeftNormal);
                createAmountCell(dispute.getInvoiceAmount(), tableEvenRowCellStyleRightNormal);
                createAmountCell(dispute.getBalance(), tableEvenRowCellStyleRightNormal);
                createTextCell(dispute.getDisputedDate(), tableEvenRowCellStyleLeftNormal);
                createTextCell(dispute.getDisputedUser(), tableEvenRowCellStyleLeftNormal);
                createTextCell(dispute.getDisputeSentUser(), tableEvenRowCellStyleLeftNormal);
            } else {
                createTextCell(dispute.getCustomerNumber(), tableOddRowCellStyleLeftNormal);
                createTextCell(dispute.getCustomerName(), tableOddRowCellStyleLeftNormal);
                createTextCell(dispute.getInvoiceDate(), tableOddRowCellStyleLeftNormal);
                createTextCell(dispute.getInvoiceOrBl(), tableOddRowCellStyleLeftNormal);
                createAmountCell(dispute.getInvoiceAmount(), tableOddRowCellStyleRightNormal);
                createAmountCell(dispute.getBalance(), tableOddRowCellStyleRightNormal);
                createTextCell(dispute.getDisputedDate(), tableOddRowCellStyleLeftNormal);
                createTextCell(dispute.getDisputedUser(), tableOddRowCellStyleLeftNormal);
                createTextCell(dispute.getDisputeSentUser(), tableOddRowCellStyleLeftNormal);

            }
        }

    }

    public String create() throws Exception {
        try {
            StringBuilder fileNameBuilder = new StringBuilder();
            fileNameBuilder.append(LoadLogisoftProperties.getProperty("reportLocation")).append("/Documents/ArReports/");
            fileNameBuilder.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
            File file = new File(fileNameBuilder.toString());
            if (!file.exists()) {
                file.mkdirs();
            }
            fileNameBuilder.append("ArDispute.xlsx");
            init(fileNameBuilder.toString(), "ArDispute");
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
