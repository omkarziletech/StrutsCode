package com.logiware.accounting.excel;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.accounting.form.SubledgerForm;
import com.logiware.accounting.model.ResultModel;
import com.logiware.excel.BaseExcelCreator;
import java.io.File;
import java.util.Date;

/**
 *
 * @author Lakshmi Naryanan
 */
public class SubledgerExcelCreator extends BaseExcelCreator {

    private SubledgerForm subledgerForm;

    public SubledgerExcelCreator() {
    }

    public SubledgerExcelCreator(SubledgerForm subledgerForm) {
	this.subledgerForm = subledgerForm;
    }

    private void writeHeader() throws Exception {
	int endCol = CommonUtils.isEqualIgnoreCase(subledgerForm.getSubledger(), ConstantsInterface.SUB_LEDGER_CODE_PURCHASE_JOURNAL) ? 19 : 18;
	createRow();
	resetColumnIndex();
	createHeaderCell("SubLedger Report", subHeaderOneCellStyleLeftBold);
	mergeCells(rowIndex, rowIndex, 0, 1);
	createHeaderCell("Date : " + DateUtils.formatDate(new Date(), "MM/dd/yyyy"), subHeaderOneCellStyleLeftBold);
	mergeCells(rowIndex, rowIndex, 2, endCol);
	createRow();
	resetColumnIndex();
	createHeaderCell("SubLedger Type : " + subledgerForm.getSubledger(), subHeaderTwoCellStyleLeftBold);
	mergeCells(rowIndex, rowIndex, 0, 1);
	createHeaderCell("Period : " + subledgerForm.getGlPeriod(), subHeaderTwoCellStyleLeftBold);
	mergeCells(rowIndex, rowIndex, 2, endCol);
	createRow();
	resetColumnIndex();
	createHeaderCell("Vendor Name", tableHeaderCellStyleCenterBold, 40);
	createHeaderCell("Vendor Number", tableHeaderCellStyleCenterBold, 15);
	createHeaderCell("Subledger Type", tableHeaderCellStyleCenterBold, 15);
	createHeaderCell("AR Batch Id", tableHeaderCellStyleCenterBold, 12);
	createHeaderCell("AP Batch Id", tableHeaderCellStyleCenterBold, 12);
	createHeaderCell("BL Number", tableHeaderCellStyleCenterBold, 20);
	createHeaderCell("Invoice Number", tableHeaderCellStyleCenterBold, 15);
	createHeaderCell("GL Account", tableHeaderCellStyleCenterBold, 12);
	createHeaderCell("Charge Code", tableHeaderCellStyleCenterBold, 12);
	createHeaderCell("Voyage", tableHeaderCellStyleCenterBold, 12);
	createHeaderCell("Transaction Date", tableHeaderCellStyleCenterBold, 12);
	createHeaderCell("Reporting Date", tableHeaderCellStyleCenterBold, 12);
	createHeaderCell("Posted Date", tableHeaderCellStyleCenterBold, 12);
	createHeaderCell("Amount", tableHeaderCellStyleCenterBold, 10);
	createHeaderCell("Debit", tableHeaderCellStyleCenterBold, 10);
	createHeaderCell("Credit", tableHeaderCellStyleCenterBold, 10);
	createHeaderCell("Record Type", tableHeaderCellStyleCenterBold, 12);
	createHeaderCell("Journal Entry", tableHeaderCellStyleCenterBold, 15);
	createHeaderCell("Line Item", tableHeaderCellStyleCenterBold, 15);
	if (CommonUtils.isEqualIgnoreCase(subledgerForm.getSubledger(), ConstantsInterface.SUB_LEDGER_CODE_PURCHASE_JOURNAL)) {
	    createHeaderCell("Invoice For", tableHeaderCellStyleCenterBold, 30);
	}
    }

    private void writeContent() throws Exception {
	if (CommonUtils.isNotEmpty(subledgerForm.getResults())) {
	    int rowCount = 0;
	    for (ResultModel result : subledgerForm.getResults()) {
		createRow();
		resetColumnIndex();
		if (rowCount % 2 == 0) {
		    createTextCell(result.getVendorName(), tableEvenRowCellStyleLeftNormal);
		    createTextCell(result.getVendorNumber(), tableEvenRowCellStyleLeftNormal);
		    createTextCell(result.getSubledger(), tableEvenRowCellStyleLeftNormal);
		    createTextCell(result.getArBatchId(), tableEvenRowCellStyleLeftNormal);
		    createTextCell(result.getApBatchId(), tableEvenRowCellStyleLeftNormal);
		    createTextCell(result.getBlNumber(), tableEvenRowCellStyleLeftNormal);
		    createTextCell(result.getInvoiceNumber(), tableEvenRowCellStyleLeftNormal);
		    createTextCell(result.getGlAccount(), tableEvenRowCellStyleLeftNormal);
		    createTextCell(result.getChargeCode(), tableEvenRowCellStyleLeftNormal);
		    createTextCell(result.getVoyage(), tableEvenRowCellStyleLeftNormal);
		    createTextCell(result.getTransactionDate(), tableEvenRowCellStyleLeftNormal);
		    createTextCell(result.getReportingDate(), tableEvenRowCellStyleLeftNormal);
		    createTextCell(result.getPostedDate(), tableEvenRowCellStyleLeftNormal);
		    createDoubleCell(result.getAmount(), tableEvenRowCellStyleRightNormal);
		    createDoubleCell(result.getDebit(), tableEvenRowCellStyleRightNormal);
		    createDoubleCell(result.getCredit(), tableEvenRowCellStyleRightNormal);
		    createTextCell(result.getTransactionType(), tableEvenRowCellStyleLeftNormal);
		    createTextCell(result.getJournalEntryId(), tableEvenRowCellStyleLeftNormal);
		    createTextCell(result.getLineItemId(), tableEvenRowCellStyleLeftNormal);
		    if (CommonUtils.isEqualIgnoreCase(subledgerForm.getSubledger(), ConstantsInterface.SUB_LEDGER_CODE_PURCHASE_JOURNAL)) {
			createTextCell(result.getNotes(), tableEvenRowCellStyleLeftNormal);
		    }
		} else {
		    createTextCell(result.getVendorName(), tableOddRowCellStyleLeftNormal);
		    createTextCell(result.getVendorNumber(), tableOddRowCellStyleLeftNormal);
		    createTextCell(result.getSubledger(), tableOddRowCellStyleLeftNormal);
		    createTextCell(result.getArBatchId(), tableOddRowCellStyleLeftNormal);
		    createTextCell(result.getApBatchId(), tableOddRowCellStyleLeftNormal);
		    createTextCell(result.getBlNumber(), tableOddRowCellStyleLeftNormal);
		    createTextCell(result.getInvoiceNumber(), tableOddRowCellStyleLeftNormal);
		    createTextCell(result.getGlAccount(), tableOddRowCellStyleLeftNormal);
		    createTextCell(result.getChargeCode(), tableOddRowCellStyleLeftNormal);
		    createTextCell(result.getVoyage(), tableOddRowCellStyleLeftNormal);
		    createTextCell(result.getTransactionDate(), tableOddRowCellStyleLeftNormal);
		    createTextCell(result.getReportingDate(), tableOddRowCellStyleLeftNormal);
		    createTextCell(result.getPostedDate(), tableOddRowCellStyleLeftNormal);
		    createDoubleCell(result.getAmount(), tableOddRowCellStyleRightNormal);
		    createDoubleCell(result.getDebit(), tableOddRowCellStyleRightNormal);
		    createDoubleCell(result.getCredit(), tableOddRowCellStyleRightNormal);
		    createTextCell(result.getTransactionType(), tableOddRowCellStyleLeftNormal);
		    createTextCell(result.getJournalEntryId(), tableOddRowCellStyleLeftNormal);
		    createTextCell(result.getLineItemId(), tableOddRowCellStyleLeftNormal);
		    if (CommonUtils.isEqualIgnoreCase(subledgerForm.getSubledger(), ConstantsInterface.SUB_LEDGER_CODE_PURCHASE_JOURNAL)) {
			createTextCell(result.getNotes(), tableOddRowCellStyleLeftNormal);
		    }
		}
		rowCount++;
	    }
	}
    }

    public String create() throws Exception {
	try {
	    StringBuilder fileNameBuilder = new StringBuilder();
	    fileNameBuilder.append(LoadLogisoftProperties.getProperty("reportLocation")).append("/Documents/GeneralLedger/");
            fileNameBuilder.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
	    File file = new File(fileNameBuilder.toString());
	    if (!file.exists()) {
		file.mkdirs();
	    }
	    fileNameBuilder.append("Subledger.xlsx");
	    init(fileNameBuilder.toString(), "Subledger Report");
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
