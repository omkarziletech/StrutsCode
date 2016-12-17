/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.accounting.excel;

import com.gp.cong.common.DateUtils;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.accounting.dao.ArReportsDAO;
import com.logiware.accounting.form.ArReportsForm;
import com.logiware.bean.ReportBean;
import com.logiware.excel.BaseExcelCreator;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 *
 * @author i3
 */
public class NoCreditExcelCreator extends BaseExcelCreator {

    private ArReportsForm arReportsForm;

    public NoCreditExcelCreator() {
    }

    public NoCreditExcelCreator(ArReportsForm arReportsForm) {
	this.arReportsForm = arReportsForm;
    }

    private void writeHeader() throws IOException {
	createRow();
	createHeaderCell("No credit Report", headerCellStyleCenterBold);
	mergeCells(rowIndex, rowIndex, 0, 7);
	row.setHeightInPoints(20);
	createRow();
	resetColumnIndex();
	createHeaderCell("Customer Number", tableHeaderCellStyleCenterBold);
	sheet.setColumnWidth(columnIndex, 256 * 15);
	createHeaderCell("Customer Name", tableHeaderCellStyleCenterBold);
	sheet.setColumnWidth(columnIndex, 256 * 45);
	createHeaderCell("Collector", tableHeaderCellStyleCenterBold);
	sheet.setColumnWidth(columnIndex, 256 * 15);
	createHeaderCell("Biiling Terminal", tableHeaderCellStyleCenterBold);
	sheet.setColumnWidth(columnIndex, 256 * 15);
	createHeaderCell("Invoice/BL", tableHeaderCellStyleCenterBold);
	sheet.setColumnWidth(columnIndex, 256 * 25);
	createHeaderCell("Invoice Date", tableHeaderCellStyleCenterBold);
	sheet.setColumnWidth(columnIndex, 256 * 15);
	createHeaderCell("Invoice Amount", tableHeaderCellStyleCenterBold);
	sheet.setColumnWidth(columnIndex, 256 * 15);
	createHeaderCell("Balance", tableHeaderCellStyleCenterBold);
	sheet.setColumnWidth(columnIndex, 256 * 15);
    }

    private void writeContent() throws Exception {
	List<ReportBean> noCreditList = new ArReportsDAO().getNoCreditInvoices(arReportsForm);
	int rowCount = 0;
	for (ReportBean nocredit : noCreditList) {
	    rowCount++;
	    createRow();
	    resetColumnIndex();
	    if (rowCount % 2 == 0) {
		createTextCell(nocredit.getCustomerNumber(), tableEvenRowCellStyleLeftNormal);
		createTextCell(nocredit.getCustomerName(), tableEvenRowCellStyleLeftNormal);
		createTextCell(nocredit.getCollector(), tableEvenRowCellStyleLeftNormal);
		createTextCell(nocredit.getBillingTerminal(), tableEvenRowCellStyleLeftNormal);
		createTextCell(nocredit.getInvoiceOrBl(), tableEvenRowCellStyleLeftNormal);
		createTextCell(nocredit.getInvoiceDate(), tableEvenRowCellStyleLeftNormal);
		createAmountCell(nocredit.getInvoiceAmount(), tableEvenRowCellStyleRightNormal);
		createAmountCell(nocredit.getBalance(), tableEvenRowCellStyleRightNormal);
	    } else {
		createTextCell(nocredit.getCustomerNumber(), tableOddRowCellStyleLeftNormal);
		createTextCell(nocredit.getCustomerName(), tableOddRowCellStyleLeftNormal);
                createTextCell(nocredit.getCollector(), tableOddRowCellStyleLeftNormal);
		createTextCell(nocredit.getBillingTerminal(), tableOddRowCellStyleLeftNormal);
		createTextCell(nocredit.getInvoiceOrBl(), tableOddRowCellStyleLeftNormal);
		createTextCell(nocredit.getInvoiceDate(), tableOddRowCellStyleLeftNormal);
		createAmountCell(nocredit.getInvoiceAmount(), tableOddRowCellStyleRightNormal);
		createAmountCell(nocredit.getBalance(), tableOddRowCellStyleRightNormal);
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
	    fileNameBuilder.append("Nocredit.xlsx");
	    init(fileNameBuilder.toString(), "Nocredit");
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
