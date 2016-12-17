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
 * @author Lakshmi Narayanan
 */
public class ArActivityExcelCreator extends BaseExcelCreator {

    private ArReportsForm arReportsForm;

    public ArActivityExcelCreator() {
    }

    public ArActivityExcelCreator(ArReportsForm arReportsForm) {
	this.arReportsForm = arReportsForm;
    }

    private void writeHeader() throws Exception {
	createRow();
	createHeaderCell("AR Activity Report", headerCellStyleCenterBold);
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
	createHeaderCell("Invoice/BL", tableHeaderCellStyleCenterBold);
	sheet.setColumnWidth(columnIndex, 256 * 25);
	createHeaderCell("Invoice Date", tableHeaderCellStyleCenterBold);
	sheet.setColumnWidth(columnIndex, 256 * 20);
	createHeaderCell("Invoice Amount", tableHeaderCellStyleCenterBold);
	sheet.setColumnWidth(columnIndex, 256 * 20);
	createHeaderCell("Invoice Balance", tableHeaderCellStyleCenterBold);
	sheet.setColumnWidth(columnIndex, 256 * 20);
	createHeaderCell("Date Paid", tableHeaderCellStyleCenterBold);
	sheet.setColumnWidth(columnIndex, 256 * 20);
	createHeaderCell("Subledger", tableHeaderCellStyleCenterBold);
	sheet.setColumnWidth(columnIndex, 256 * 20);
	createHeaderCell("Collector", tableHeaderCellStyleCenterBold);
	sheet.setColumnWidth(columnIndex, 256 * 20);
    }

    private void writeContent() throws Exception {
	List<ReportBean> noCreditList = new ArReportsDAO().getActivityList(arReportsForm);
	int rowCount = 0;
	for (ReportBean nocredit : noCreditList) {
	    rowCount++;
	    createRow();
	    resetColumnIndex();
	    if (rowCount % 2 == 0) {
		createTextCell(nocredit.getCustomerNumber(), tableEvenRowCellStyleLeftNormal);
		createTextCell(nocredit.getCustomerName(), tableEvenRowCellStyleLeftNormal);
		createTextCell(nocredit.getInvoiceOrBl(), tableEvenRowCellStyleLeftNormal);
		createTextCell(nocredit.getInvoiceDate(), tableEvenRowCellStyleLeftNormal);
		createAmountCell(nocredit.getInvoiceAmount(), tableEvenRowCellStyleRightNormal);
		createAmountCell(nocredit.getBalance(), tableEvenRowCellStyleRightNormal);
		createTextCell(nocredit.getPaymentDate(), tableEvenRowCellStyleLeftNormal);
		createTextCell(nocredit.getType(), tableEvenRowCellStyleLeftNormal);//subledger
		createTextCell(nocredit.getCollector(), tableEvenRowCellStyleLeftNormal);
	    } else {
		createTextCell(nocredit.getCustomerNumber(), tableOddRowCellStyleLeftNormal);
		createTextCell(nocredit.getCustomerName(), tableOddRowCellStyleLeftNormal);
		createTextCell(nocredit.getInvoiceOrBl(), tableOddRowCellStyleLeftNormal);
		createTextCell(nocredit.getInvoiceDate(), tableOddRowCellStyleLeftNormal);
		createAmountCell(nocredit.getInvoiceAmount(), tableOddRowCellStyleRightNormal);
		createAmountCell(nocredit.getBalance(), tableOddRowCellStyleRightNormal);
		createTextCell(nocredit.getPaymentDate(), tableOddRowCellStyleLeftNormal);
		createTextCell(nocredit.getType(), tableOddRowCellStyleLeftNormal);//subledger
		createTextCell(nocredit.getCollector(), tableOddRowCellStyleLeftNormal);
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
	    fileNameBuilder.append("ArActivity.xlsx");
	    init(fileNameBuilder.toString(), "ArActivity");
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
