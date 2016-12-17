package com.logiware.accounting.excel;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.accounting.dao.AccrualsDAO;
import com.logiware.accounting.form.AccrualsForm;
import com.logiware.accounting.model.ResultModel;
import com.logiware.excel.BaseExcelCreator;
import java.io.File;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Lakshmi Naryanan
 */
public class AccrualsExcelCreator extends BaseExcelCreator {
    private AccrualsForm accrualsForm;

    public AccrualsExcelCreator() {
    }

    public AccrualsExcelCreator(AccrualsForm accrualsForm) {
	this.accrualsForm = accrualsForm;
    }

    private String getSearchBy(String searchBy) {
	if ("invoice_number".equals(searchBy)) {
	    return "Invoice Number";
	} else if ("container_no".equals(searchBy)) {
	    return "Container Number";
	} else if ("drcpt".equals(searchBy)) {
	    return "Dock Receipt";
	} else if ("bill_ladding_no".equals(searchBy)) {
	    return "House Bill";
	} else if ("sub_house_bl".equals(searchBy)) {
	    return "Sub-House Bill";
	} else if ("voyage_no".equals(searchBy)) {
	    return "Voyage";
	} else if ("master_bl".equals(searchBy)) {
	    return "Master Bill";
	} else if ("booking_no".equals(searchBy)) {
	    return "Booking Number";
	} else {
	    return "";
	}
    }

    private void writeHeader() throws Exception {
	createRow();
	createHeaderCell("Accruals Report", headerCellStyleLeftBold);
	mergeCells(rowIndex, rowIndex, 0, 13);
	row.setHeightInPoints(20);
	createRow();
	resetColumnIndex();
	createHeaderCell("Date : ", subHeaderOneCellStyleLeftBold);
	createHeaderCell(DateUtils.formatDate(new Date(), "MM/dd/yyyy"), subHeaderOneCellStyleLeftNormal);
	mergeCells(rowIndex, rowIndex, 1, 13);
	createRow();
	resetColumnIndex();
	if (CommonUtils.isNotEmpty(accrualsForm.getSearchBy())) {
	    createHeaderCell("Search By : ", subHeaderOneCellStyleLeftBold);
	    createHeaderCell(getSearchBy(accrualsForm.getSearchBy()), subHeaderOneCellStyleLeftNormal);
	    createHeaderCell("Search Value : ", subHeaderOneCellStyleLeftBold);
	    createHeaderCell(accrualsForm.getSearchValue(), subHeaderOneCellStyleLeftNormal);
	} else {
	    createHeaderCell("Vendor Name : ", subHeaderOneCellStyleLeftBold);
	    String vendorName = CommonUtils.isNotEmpty(accrualsForm.getSearchVendorName()) ? accrualsForm.getSearchVendorName() : accrualsForm.getVendorName();
	    createHeaderCell(vendorName, subHeaderOneCellStyleLeftNormal);
	    createHeaderCell("Vendor Number : ", subHeaderOneCellStyleLeftBold);
	    String vendorNumber = CommonUtils.isNotEmpty(accrualsForm.getSearchVendorNumber()) ? accrualsForm.getSearchVendorNumber() : accrualsForm.getVendorNumber();
	    createHeaderCell(vendorNumber, subHeaderOneCellStyleLeftNormal);
	}
	createHeaderCell("", subHeaderOneCellStyleLeftBold);
	mergeCells(rowIndex, rowIndex, 4, 13);
	createRow();
	resetColumnIndex();
	createHeaderCell("Vendor Name", tableHeaderCellStyleCenterBold);
	createHeaderCell("Vendor Number", tableHeaderCellStyleCenterBold);
	createHeaderCell("Invoice Number", tableHeaderCellStyleCenterBold);
	createHeaderCell("B/L", tableHeaderCellStyleCenterBold);
	createHeaderCell("Container", tableHeaderCellStyleCenterBold);
	createHeaderCell("Voyage", tableHeaderCellStyleCenterBold);
	createHeaderCell("D/R", tableHeaderCellStyleCenterBold);
	createHeaderCell("Reporting Date", tableHeaderCellStyleCenterBold);
	createHeaderCell("Amount", tableHeaderCellStyleCenterBold);
	createHeaderCell("BS Cost Code", tableHeaderCellStyleCenterBold);
	createHeaderCell("Cost Code", tableHeaderCellStyleCenterBold);
	createHeaderCell("Terminal", tableHeaderCellStyleCenterBold);
	createHeaderCell("Type", tableHeaderCellStyleCenterBold);
	createHeaderCell("Status", tableHeaderCellStyleCenterBold);
    }

    private void writeContent() throws Exception {
	List<ResultModel> accruals = new AccrualsDAO().searchForReport(accrualsForm);
	int rowCount = 0;
	for (ResultModel accrual : accruals) {
	    createRow();
	    resetColumnIndex();
	    if (rowCount % 2 == 0) {
		createTextCell(accrual.getVendorName(), tableEvenRowCellStyleLeftNormal);
		createTextCell(accrual.getVendorNumber(), tableEvenRowCellStyleLeftNormal);
		createTextCell(accrual.getInvoiceNumber(), tableEvenRowCellStyleLeftNormal);
		createTextCell(accrual.getBlNumber(), tableEvenRowCellStyleLeftNormal);
		createTextCell(accrual.getContainer(), tableEvenRowCellStyleLeftNormal);
		createTextCell(accrual.getVoyage(), tableEvenRowCellStyleLeftNormal);
		createTextCell(accrual.getDockReceipt(), tableEvenRowCellStyleLeftNormal);
		createTextCell(accrual.getReportingDate(), tableEvenRowCellStyleLeftNormal);
		createAmountCell(accrual.getAccruedAmount(), tableEvenRowCellStyleRightNormal);
		if (CommonUtils.isEqualIgnoreCase(accrual.getTransactionType(), "AR")) {
		    createTextCell("", tableEvenRowCellStyleLeftNormal);
		    createTextCell("", tableEvenRowCellStyleLeftNormal);
		    createTextCell("", tableEvenRowCellStyleLeftNormal);
		} else {
		    createTextCell(accrual.getBluescreenCostCode(), tableEvenRowCellStyleLeftNormal);
		    createTextCell(accrual.getCostCode(), tableEvenRowCellStyleLeftNormal);
		    String terminal = com.logiware.tags.String.lastSubString(accrual.getGlAccount(), "-");
		    createTextCell(terminal, tableEvenRowCellStyleLeftNormal);
		}
		createTextCell(accrual.getTransactionType(), tableEvenRowCellStyleLeftNormal);
		createTextCell(accrual.getStatus(), tableEvenRowCellStyleLeftNormal);
	    } else {
		createTextCell(accrual.getVendorName(), tableOddRowCellStyleLeftNormal);
		createTextCell(accrual.getVendorNumber(), tableOddRowCellStyleLeftNormal);
		createTextCell(accrual.getInvoiceNumber(), tableOddRowCellStyleLeftNormal);
		createTextCell(accrual.getBlNumber(), tableOddRowCellStyleLeftNormal);
		createTextCell(accrual.getContainer(), tableOddRowCellStyleLeftNormal);
		createTextCell(accrual.getVoyage(), tableOddRowCellStyleLeftNormal);
		createTextCell(accrual.getDockReceipt(), tableOddRowCellStyleLeftNormal);
		createTextCell(accrual.getReportingDate(), tableOddRowCellStyleLeftNormal);
		createAmountCell(accrual.getAccruedAmount(), tableOddRowCellStyleRightNormal);
		if (CommonUtils.isEqualIgnoreCase(accrual.getTransactionType(), "AR")) {
		    createTextCell("", tableOddRowCellStyleLeftNormal);
		    createTextCell("", tableOddRowCellStyleLeftNormal);
		    createTextCell("", tableOddRowCellStyleLeftNormal);
		} else {
		    createTextCell(accrual.getBluescreenCostCode(), tableOddRowCellStyleLeftNormal);
		    createTextCell(accrual.getCostCode(), tableOddRowCellStyleLeftNormal);
		    String terminal = com.logiware.tags.String.lastSubString(accrual.getGlAccount(), "-");
		    createTextCell(terminal, tableOddRowCellStyleLeftNormal);
		}
		createTextCell(accrual.getTransactionType(), tableOddRowCellStyleLeftNormal);
		createTextCell(accrual.getStatus(), tableOddRowCellStyleLeftNormal);
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
	    fileNameBuilder.append("Accruals.xlsx");
	    init(fileNameBuilder.toString(), "Accruals Report");
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
