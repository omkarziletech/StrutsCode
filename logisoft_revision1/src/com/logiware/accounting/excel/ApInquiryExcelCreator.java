package com.logiware.accounting.excel;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.accounting.form.ApInquiryForm;
import com.logiware.accounting.model.ResultModel;
import com.logiware.accounting.model.VendorModel;
import com.logiware.excel.BaseExcelCreator;
import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 *
 * @author Lakshmi Naryanan
 */
public class ApInquiryExcelCreator extends BaseExcelCreator {

    private ApInquiryForm apInquiryForm;

    public ApInquiryExcelCreator() {
    }

    public ApInquiryExcelCreator(ApInquiryForm apInquiryForm) {
	this.apInquiryForm = apInquiryForm;
    }

    private String getSearchBy(String searchBy) {
	if ("invoice_number".equals(searchBy)) {
	    return "Invoice Number";
	} else if ("invoice_amount".equals(searchBy)) {
	    return "Invoice Amount";
	} else if ("drcpt".equals(searchBy)) {
	    return "Dock Receipt";
	} else if ("voyage_no".equals(searchBy)) {
	    return "Voyage";
	} else if ("container_no".equals(searchBy)) {
	    return "Container Number";
	} else if ("booking_no".equals(searchBy)) {
	    return "Booking Number";
	} else if ("bill_ladding_no".equals(searchBy)) {
	    return "House Bill";
	} else if ("sub_house_bl".equals(searchBy)) {
	    return "Sub-House Bill";
	} else if ("master_bl".equals(searchBy)) {
	    return "Master Bill";
	} else if ("check_number".equals(searchBy)) {
	    return "Check Number";
	} else if ("check_amount".equals(searchBy)) {
	    return "Check Amount";
	} else {
	    return "";
	}
    }

    public void writeVendorDetails() throws IOException {
	VendorModel vendor = apInquiryForm.getVendor();
	createRow();
	resetColumnIndex();
	createHeaderCell("Vendor Name : ", lavendarCellStyleRightBold);
	createHeaderCell(vendor.getVendorName(), lavendarCellStyleLeftNormal);
	createHeaderCell("Vendor Number : ", lavendarCellStyleRightBold);
	createHeaderCell(vendor.getVendorNumber(), lavendarCellStyleLeftNormal);
	createHeaderCell("Sub Type : ", lavendarCellStyleRightBold);
	createHeaderCell(vendor.getSubType(), lavendarCellStyleLeftNormal);
	createHeaderCell("", lavendarCellStyleRightBold);
	mergeCells(rowIndex, rowIndex, 6, 14);
	createRow();
	resetColumnIndex();
	createHeaderCell("Address : ", lavendarCellStyleRightBold);
	createHeaderCell(vendor.getAddress(), lavendarCellStyleLeftNormal);
	createHeaderCell("Credit Term : ", lavendarCellStyleRightBold);
	createHeaderCell(vendor.getCreditTerm(), lavendarCellStyleLeftNormal);
	createHeaderCell("Current : ", lavendarCellStyleRightBold);
	createDoubleCell(vendor.getAge30Amount(), lavendarCellStyleLeftNormal);
	createHeaderCell("", lavendarCellStyleRightBold);
	mergeCells(rowIndex, rowIndex, 6, 14);
	createRow();
	resetColumnIndex();
	createHeaderCell("Contact : ", lavendarCellStyleRightBold);
	createHeaderCell(vendor.getContact(), lavendarCellStyleLeftNormal);
	createHeaderCell("Credit Limit : ", lavendarCellStyleRightBold);
	createDoubleCell(vendor.getCreditLimit(), lavendarCellStyleLeftNormal);
	createHeaderCell("30-60 Days : ", lavendarCellStyleRightBold);
	createDoubleCell(vendor.getAge60Amount(), lavendarCellStyleLeftNormal);
	createHeaderCell("", lavendarCellStyleRightBold);
	mergeCells(rowIndex, rowIndex, 6, 14);
	createRow();
	resetColumnIndex();
	createHeaderCell("Phone : ", lavendarCellStyleRightBold);
	createHeaderCell(vendor.getPhone(), lavendarCellStyleLeftNormal);
	createHeaderCell("OutStanding Receivables : ", lavendarCellStyleRightBold);
	createDoubleCell(vendor.getArAmount(), lavendarCellStyleLeftNormal);
	createHeaderCell("60-90 Days : ", lavendarCellStyleRightBold);
	createDoubleCell(vendor.getAge90Amount(), lavendarCellStyleLeftNormal);
	createHeaderCell("", lavendarCellStyleRightBold);
	mergeCells(rowIndex, rowIndex, 6, 14);
	createRow();
	resetColumnIndex();
	createHeaderCell("Fax : ", lavendarCellStyleRightBold);
	createHeaderCell(vendor.getFax(), lavendarCellStyleLeftNormal);
	createHeaderCell("Net Payable Amount : ", lavendarCellStyleRightBold);
	createDoubleCell(vendor.getNetAmount(), lavendarCellStyleLeftNormal);
	createHeaderCell(">91 Days : ", lavendarCellStyleRightBold);
	createDoubleCell(vendor.getAge91Amount(), lavendarCellStyleLeftNormal);
	createHeaderCell("", lavendarCellStyleRightBold);
	mergeCells(rowIndex, rowIndex, 6, 14);
	createRow();
	resetColumnIndex();
	createHeaderCell("Email : ", lavendarCellStyleRightBold);
	createHeaderCell(vendor.getEmail(), lavendarCellStyleLeftNormal);
	createHeaderCell("", lavendarCellStyleRightBold);
	createHeaderCell("", lavendarCellStyleRightBold);
	createHeaderCell("Total : ", lavendarCellStyleRightBold);
	createDoubleCell(vendor.getApAmount(), lavendarCellStyleLeftNormal);
	createHeaderCell("", lavendarCellStyleRightBold);
	mergeCells(rowIndex, rowIndex, 6, 14);
    }

    private void writeHeader() throws Exception {
	createRow();
	createHeaderCell("Ap Inquiry Report", headerCellStyleLeftBold);
	mergeCells(rowIndex, rowIndex, 0, 14);
	row.setHeightInPoints(20);
	createRow();
	resetColumnIndex();
	createHeaderCell("Date : ", subHeaderOneCellStyleLeftBold);
	createHeaderCell(DateUtils.formatDate(new Date(), "MM/dd/yyyy"), subHeaderOneCellStyleLeftNormal);
	mergeCells(rowIndex, rowIndex, 1, 14);
	createRow();
	resetColumnIndex();
	if (CommonUtils.isNotEmpty(apInquiryForm.getSearchBy())) {
	    createHeaderCell("Search By : ", subHeaderOneCellStyleLeftBold);
	    createHeaderCell(getSearchBy(apInquiryForm.getSearchBy()), subHeaderOneCellStyleLeftNormal);
	    createHeaderCell("Search Value : ", subHeaderOneCellStyleLeftBold);
	    if (CommonUtils.in(apInquiryForm.getSearchBy(), "invoice_amount", "check_amount")) {
		StringBuilder searchValue = new StringBuilder();
		if (CommonUtils.isNotEmpty(apInquiryForm.getFromAmount())) {
		    searchValue.append("From : ").append(apInquiryForm.getFromAmount());
		}
		if (CommonUtils.isNotEmpty(apInquiryForm.getToAmount())) {
		    if (CommonUtils.isNotEmpty(apInquiryForm.getFromAmount())) {
			searchValue.append("\n");
		    }
		    searchValue.append("To : ").append(apInquiryForm.getToAmount());
		}
		createHeaderCell(searchValue.toString(), subHeaderOneCellStyleLeftNormal);
	    } else {
		createHeaderCell(apInquiryForm.getSearchValue(), subHeaderOneCellStyleLeftNormal);
	    }
	} else {
	    createHeaderCell("Vendor Name : ", subHeaderOneCellStyleLeftBold);
	    createHeaderCell(apInquiryForm.getVendorName(), subHeaderOneCellStyleLeftNormal);
	    createHeaderCell("Vendor Number : ", subHeaderOneCellStyleLeftBold);
	    createHeaderCell(apInquiryForm.getVendorNumber(), subHeaderOneCellStyleLeftNormal);
	}
	createHeaderCell("", subHeaderOneCellStyleLeftBold);
	mergeCells(rowIndex, rowIndex, 4, 14);
	if (null != apInquiryForm.getVendor()) {
	    writeVendorDetails();
	}
	createRow();
	resetColumnIndex();
	createHeaderCell("Vendor Name", tableHeaderCellStyleCenterBold);
	createHeaderCell("Vendor Number", tableHeaderCellStyleCenterBold);
	createHeaderCell("Invoice/BL", tableHeaderCellStyleCenterBold);
	createHeaderCell("Invoice Date", tableHeaderCellStyleCenterBold);
	createHeaderCell("Due Date", tableHeaderCellStyleCenterBold);
	createHeaderCell("Invoice Amount", tableHeaderCellStyleCenterBold);
	createHeaderCell("Invoice Balance", tableHeaderCellStyleCenterBold);
	createHeaderCell("Check Number", tableHeaderCellStyleCenterBold);
	createHeaderCell("Payment Date", tableHeaderCellStyleCenterBold);
	createHeaderCell("Cleared Date", tableHeaderCellStyleCenterBold);
	createHeaderCell("Dock Receipt", tableHeaderCellStyleCenterBold);
	createHeaderCell("Voyage", tableHeaderCellStyleCenterBold);
	createHeaderCell("Cost Code", tableHeaderCellStyleCenterBold);
	createHeaderCell("Type", tableHeaderCellStyleCenterBold);
	createHeaderCell("Status", tableHeaderCellStyleCenterBold);
    }

    private void writeContent() throws Exception {
	if (CommonUtils.isNotEmpty(apInquiryForm.getResults())) {
	    int rowCount = 0;
	    for (ResultModel accrual : apInquiryForm.getResults()) {
		createRow();
		resetColumnIndex();
		if (rowCount % 2 == 0) {
		    createTextCell(accrual.getVendorName(), tableEvenRowCellStyleLeftNormal);
		    createTextCell(accrual.getVendorNumber(), tableEvenRowCellStyleLeftNormal);
		    createTextCell(accrual.getInvoiceOrBl(), tableEvenRowCellStyleLeftNormal);
		    createTextCell(accrual.getInvoiceDate(), tableEvenRowCellStyleLeftNormal);
		    createTextCell(accrual.getDueDate(), tableEvenRowCellStyleLeftNormal);
		    createAmountCell(accrual.getInvoiceAmount(), tableEvenRowCellStyleRightNormal);
		    createAmountCell(accrual.getInvoiceBalance(), tableEvenRowCellStyleRightNormal);
		    createTextCell(accrual.getCheckNumber(), tableEvenRowCellStyleLeftNormal);
		    createTextCell(accrual.getPaymentDate(), tableEvenRowCellStyleLeftNormal);
		    createTextCell(accrual.getClearedDate(), tableEvenRowCellStyleLeftNormal);
		    createTextCell(accrual.getDockReceipt(), tableEvenRowCellStyleLeftNormal);
		    createTextCell(accrual.getVoyage(), tableEvenRowCellStyleLeftNormal);
		    createTextCell(accrual.getCostCode(), tableEvenRowCellStyleLeftNormal);
		    createTextCell(accrual.getTransactionType(), tableEvenRowCellStyleLeftNormal);
		    createTextCell(accrual.getStatus(), tableEvenRowCellStyleLeftNormal);
		} else {
		    createTextCell(accrual.getVendorName(), tableOddRowCellStyleLeftNormal);
		    createTextCell(accrual.getVendorNumber(), tableOddRowCellStyleLeftNormal);
		    createTextCell(accrual.getInvoiceOrBl(), tableOddRowCellStyleLeftNormal);
		    createTextCell(accrual.getInvoiceDate(), tableOddRowCellStyleLeftNormal);
		    createTextCell(accrual.getDueDate(), tableOddRowCellStyleLeftNormal);
		    createAmountCell(accrual.getInvoiceAmount(), tableOddRowCellStyleRightNormal);
		    createAmountCell(accrual.getInvoiceBalance(), tableOddRowCellStyleRightNormal);
		    createTextCell(accrual.getCheckNumber(), tableOddRowCellStyleLeftNormal);
		    createTextCell(accrual.getPaymentDate(), tableOddRowCellStyleLeftNormal);
		    createTextCell(accrual.getClearedDate(), tableOddRowCellStyleLeftNormal);
		    createTextCell(accrual.getDockReceipt(), tableOddRowCellStyleLeftNormal);
		    createTextCell(accrual.getVoyage(), tableOddRowCellStyleLeftNormal);
		    createTextCell(accrual.getCostCode(), tableOddRowCellStyleLeftNormal);
		    createTextCell(accrual.getTransactionType(), tableOddRowCellStyleLeftNormal);
		    createTextCell(accrual.getStatus(), tableOddRowCellStyleLeftNormal);
		}
		rowCount++;
	    }
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
	    fileNameBuilder.append("ApInquiry.xlsx");
	    init(fileNameBuilder.toString(), "Ap Inquiry Report");
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