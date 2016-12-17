package com.logiware.excel;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.bean.ReconcileModel;
import com.logiware.form.ReconcileForm;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import org.apache.poi.ss.usermodel.CellStyle;

/**
 *
 * @author Lakshmi Naryanan
 */
public class ReconcileExcelCreator extends BaseExcelCreator {

    private ReconcileForm reconcileForm;

    public ReconcileExcelCreator() {
    }

    public ReconcileExcelCreator(ReconcileForm reconcileForm) {
	this.reconcileForm = reconcileForm;
    }

    private void writeHeader() throws IOException {
	createRow();
	createHeaderCell("Bank Reconcilation", headerCellStyleCenterBold);
	mergeCells(rowIndex, rowIndex, 0, 1);
	row.setHeightInPoints(20);
	createRow();
	resetColumnIndex();
	createHeaderCell("Account Details", subHeaderOneCellStyleCenterBold);
	mergeCells(rowIndex, rowIndex, 0, 1);
	row.setHeightInPoints(20);
	createRow();
	resetColumnIndex();
	createHeaderCell("Bank Name : ", subHeaderTwoCellStyleLeftBold);
        sheet.setColumnWidth(columnIndex, 256 * 40);
	createHeaderCell(reconcileForm.getBankName(), subHeaderTwoCellStyleLeftNormal);
        sheet.setColumnWidth(columnIndex, 256 * 20);
	createRow();
	resetColumnIndex();
	createHeaderCell("Bank Account : ", subHeaderOneCellStyleLeftBold);
	createHeaderCell(reconcileForm.getBankAccount(), subHeaderOneCellStyleLeftNormal);
	createRow();
	resetColumnIndex();
	createHeaderCell("GL Account : ", subHeaderTwoCellStyleLeftBold);
	createHeaderCell(reconcileForm.getGlAccount(), subHeaderTwoCellStyleLeftNormal);
	createRow();
	resetColumnIndex();
	createHeaderCell("Reconcile Date : ", subHeaderOneCellStyleLeftBold);
	createHeaderCell(reconcileForm.getReconcileDate(), subHeaderOneCellStyleLeftNormal);
	double bankBalance = Double.valueOf(reconcileForm.getBankBalance().replace(",", ""));
	double depositsOpen = Double.valueOf(reconcileForm.getDepositsOpen().replace(",", ""));
	double checksOpen = Double.valueOf(reconcileForm.getChecksOpen().replace(",", ""));
	double reconciledBankBalance = bankBalance + depositsOpen - checksOpen;
	createRow();
	resetColumnIndex();
	createTextCell("Bank Balance : ", subHeaderTwoCellStyleLeftBold);
	createDoubleCell(bankBalance, subHeaderTwoCellStyleRightNormal);
	createRow();
	resetColumnIndex();
	createHeaderCell("Deposits in transit : ", subHeaderOneCellStyleLeftBold);
	createDoubleCell(depositsOpen, subHeaderOneCellStyleRightNormal);
	createRow();
	resetColumnIndex();
	createTextCell("Outstanding checks : ", subHeaderTwoCellStyleLeftBold);
	createDoubleCell(-checksOpen, subHeaderTwoCellStyleRightNormal);
	createRow();
	resetColumnIndex();
	createHeaderCell("Reconciled Bank balance : ", subHeaderOneCellStyleLeftBold);
	createDoubleCell(reconciledBankBalance, subHeaderOneCellStyleRightNormal);
    }

    private void writeContent(List<ReconcileModel> openChecks,List<ReconcileModel> openDeposits) throws IOException {
	createRow();
	resetColumnIndex();
	createHeaderCell("Detailed list oustanding checks", subHeaderTwoCellStyleCenterBold);
	mergeCells(rowIndex, rowIndex, 0, 1);
	row.setHeightInPoints(20);
	double totalCredit = 0d;
	if(CommonUtils.isNotEmpty(openChecks)){
	    int rowCount = 0;
	    for (ReconcileModel openCheck : openChecks) {
		createRow();
		resetColumnIndex();
		double credit = -Double.parseDouble(openCheck.getCredit().replaceAll(",", ""));
		totalCredit += credit;
		CellStyle textCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleLeftNormal : tableOddRowCellStyleLeftNormal;
		CellStyle doubleCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleRightNormal : tableOddRowCellStyleRightNormal;
		createTextCell(openCheck.getReferenceNumber(), textCellStyle);
		createDoubleCell(credit, doubleCellStyle);
		rowCount++;
	    }
	}else{
	    createRow();
	    resetColumnIndex();
	    createEmptyCell(tableOddRowCellStyleLeftNormal);
	    createEmptyCell(tableOddRowCellStyleLeftNormal);
	}
	createRow();
	resetColumnIndex();
	createTextCell("Total", darkAshCellStyleRightBold);
	createDoubleCell(totalCredit, darkAshCellStyleRightBold);
	createRow();
	resetColumnIndex();
	createHeaderCell("Detailed list Deposits in transit", subHeaderTwoCellStyleCenterBold);
	mergeCells(rowIndex, rowIndex, 0, 1);
	row.setHeightInPoints(20);
	double totalDebit = 0d;
	if(CommonUtils.isNotEmpty(openDeposits)){
	    int rowCount=0;
	    for (ReconcileModel openDeposit : openDeposits) {
		createRow();
		resetColumnIndex();
		double debit = Double.parseDouble(openDeposit.getDebit().replaceAll(",", ""));
		totalDebit += debit;
		CellStyle textCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleLeftNormal : tableOddRowCellStyleLeftNormal;
		CellStyle doubleCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleRightNormal : tableOddRowCellStyleRightNormal;
		createTextCell(openDeposit.getReferenceNumber(), textCellStyle);
		createDoubleCell(debit, doubleCellStyle);
		rowCount++;
	    }
	}else{
	    createRow();
	    resetColumnIndex();
	    createEmptyCell(tableOddRowCellStyleLeftNormal);
	    createEmptyCell(tableOddRowCellStyleLeftNormal);
	}
	createRow();
	resetColumnIndex();
	createTextCell("Total", darkAshCellStyleRightBold);
	createDoubleCell(totalDebit, darkAshCellStyleRightBold);
    }

    public String create(List<ReconcileModel> openChecks,List<ReconcileModel> openDeposits) throws Exception{
	try {
	    String sheetName = "Reconcile";
	    StringBuilder fileName = new StringBuilder();
	    fileName.append(LoadLogisoftProperties.getProperty("reportLocation")).append("/Documents/Reconcile/Export/");
            fileName.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
	    File file = new File(fileName.toString());
	    if (!file.exists()) {
		file.mkdirs();
	    }
	    fileName.append("Reconcile.xlsx");
	    init(fileName.toString(), sheetName);
	    writeHeader();
	    writeContent(openChecks, openDeposits);
	    writeIntoFile();
	    return fileName.toString();
	} catch (Exception e) {
	    throw e;
	} finally {
	    exit();
	}
    }
}
