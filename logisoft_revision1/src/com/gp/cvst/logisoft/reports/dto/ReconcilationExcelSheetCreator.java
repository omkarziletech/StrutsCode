package com.gp.cvst.logisoft.reports.dto;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.ExcelGenerator.BaseExcelGenerator;
import java.util.List;

import com.gp.cong.logisoft.bc.accounting.ReconcileConstants;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.beans.TransactionBean;
import com.gp.cvst.logisoft.domain.BankDetails;
import com.gp.cvst.logisoft.hibernate.dao.BankDetailsDAO;
import com.gp.cvst.logisoft.struts.form.ReconcileForm;
import java.io.File;

import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ReconcilationExcelSheetCreator extends BaseExcelGenerator {

    public void generateExcelSheet(ReconcileForm reconcileForm, List<TransactionBean> checkList, List<TransactionBean> depositList) throws Exception {
        writableSheet = writableWorkbook.createSheet("Reconcile Sheet", 0);
        int row = 0;
        writableSheet.mergeCells(0, row, 4, row);
        writableSheet.addCell(new Label(0, row, ReconcileConstants.HEADER, headerCell));
        row++;
        writableSheet.mergeCells(0, row, 4, row);
        writableSheet.addCell(new Label(0, row, "", headerCell));
        row++;
        writableSheet.mergeCells(0, row, 4, row);
        writableSheet.addCell(new Label(0, row, ReconcileConstants.ACCOUNT_DETAILS, headerCell));
        BankDetailsDAO bankDetailsDAO = new BankDetailsDAO();
        List<BankDetails> bankDetailsList = bankDetailsDAO.findByGLAccountNo(reconcileForm.getGlAccountNumber().trim());
        String bankName = "";
        String bankAcctNo = "";
        String glAcctNo = reconcileForm.getGlAccountNumber();
        for (BankDetails bankDetails : bankDetailsList) {
            bankName = bankDetails.getBankName();
            bankAcctNo = bankDetails.getBankAcctNo();
            glAcctNo = bankDetails.getGlAccountno();
        }
        row++;
        writableSheet.mergeCells(0, row, 2, row);
        writableSheet.mergeCells(3, row, 4, row);
        writableSheet.addCell(new Label(0, row, ReconcileConstants.BANK_NAME, thinBorderCell));
        writableSheet.addCell(new Label(3, row, bankName, thinBorderCellAlignRight));
        row++;
        writableSheet.mergeCells(0, row, 2, row);
        writableSheet.mergeCells(3, row, 4, row);
        writableSheet.addCell(new Label(0, row, ReconcileConstants.BANK_ACCT_NO, thinBorderCell));
        writableSheet.addCell(new Label(3, row, bankAcctNo, thinBorderCellAlignRight));
        row++;
        writableSheet.mergeCells(0, row, 2, row);
        writableSheet.mergeCells(3, row, 4, row);
        writableSheet.addCell(new Label(0, row, ReconcileConstants.GL_ACCOUNT, thinBorderCell));
        writableSheet.addCell(new Label(3, row, glAcctNo, thinBorderCellAlignRight));
        row++;
        writableSheet.mergeCells(0, row, 2, row);
        writableSheet.mergeCells(3, row, 4, row);
        writableSheet.addCell(new Label(0, row, ReconcileConstants.DATE, thinBorderCell));
        writableSheet.addCell(new Label(3, row, reconcileForm.getBankReconcileDate(), thinBorderCellAlignRight));
        Double bankStatement = Double.parseDouble(reconcileForm.getBankStatementBalance().replaceAll(",", ""));
        Double depositsOpen = Double.parseDouble(reconcileForm.getOpenDeposits().replaceAll(",", ""));
        Double checksOpen = Double.parseDouble(reconcileForm.getOpenChecks().replaceAll(",", ""));
        Double balance = bankStatement + depositsOpen - checksOpen;
        row++;
        writableSheet.mergeCells(0, row, 2, row);
        writableSheet.mergeCells(3, row, 4, row);
        writableSheet.addCell(new Label(0, row, ReconcileConstants.BANK_BALANCE, thinBorderCell));
        writableSheet.addCell(new Number(3, row, bankStatement, numberCellFormat));
        row++;
        writableSheet.mergeCells(0, row, 2, row);
        writableSheet.mergeCells(3, row, 4, row);
        writableSheet.addCell(new Label(0, row, ReconcileConstants.DEPOSITS_IN_TRANSIT, thinBorderCell));
        writableSheet.addCell(new Number(3, row, depositsOpen, numberCellFormat));
        row++;
        writableSheet.mergeCells(0, row, 2, row);
        writableSheet.mergeCells(3, row, 4, row);
        writableSheet.addCell(new Label(0, row, ReconcileConstants.OUTSTANDING_CHECKS, thinBorderCell));
        writableSheet.addCell(new Number(3, row, (-1) * checksOpen, numberCellFormat));
        row++;
        writableSheet.mergeCells(0, row, 2, row);
        writableSheet.mergeCells(3, row, 4, row);
        writableSheet.addCell(new Label(0, row, ReconcileConstants.RECONCILED_BANK_BALANCE, thinBorderCell));
        writableSheet.addCell(new Number(3, row, balance, numberCellFormat));
        row++;
        writableSheet.mergeCells(0, row, 4, row);
        writableSheet.addCell(new Label(0, row, ReconcileConstants.DETAILED_LIST_OUTSTANDING_CHECKS, headerCell));
        Double totalCredits = 0d;
        if (null != checkList && !checkList.isEmpty()) {
            for (TransactionBean openChecks : checkList) {
                row++;
                Double credit = (-1) * Double.parseDouble(openChecks.getCredit().replaceAll(",", ""));
                totalCredits += credit;
                writableSheet.mergeCells(0, row, 2, row);
                writableSheet.mergeCells(3, row, 4, row);
                writableSheet.addCell(new Label(0, row, openChecks.getCustomerReference(), thinBorderCell));
                writableSheet.addCell(new Number(3, row, credit, numberCellFormat));
            }
        } else {
            row++;
            writableSheet.mergeCells(0, row, 4, row);
            writableSheet.addCell(new Label(0, row, "", headerCell));
        }
        row++;
        writableSheet.mergeCells(0, row, 2, row);
        writableSheet.mergeCells(3, row, 4, row);
        writableSheet.addCell(new Label(0, row, ReconcileConstants.TOTAL, headerCell));
        writableSheet.addCell(new Number(3, row, totalCredits, numberCellFormat));
        row++;
        writableSheet.mergeCells(0, row, 4, row);
        writableSheet.addCell(new Label(0, row, ReconcileConstants.DETAILED_LIST_DEPOSITS_IN_TRANSIT, headerCell));
        Double totalDebits = 0d;
        if (CommonUtils.isNotEmpty(depositList)) {
            for (TransactionBean openDeposits : depositList) {
                row++;
                Double debit = Double.parseDouble(openDeposits.getDebit().replaceAll(",", ""));
                totalDebits += debit;
                writableSheet.mergeCells(0, row, 2, row);
                writableSheet.mergeCells(3, row, 4, row);
                writableSheet.addCell(new Label(0, row, openDeposits.getCustomerReference(), thinBorderCell));
                writableSheet.addCell(new Number(3, row, debit, numberCellFormat));
            }
        } else {
            row++;
            writableSheet.mergeCells(0, row, 4, row);
            writableSheet.addCell(new Label(0, row, "", headerCell));
        }
        row++;
        writableSheet.mergeCells(0, row, 2, row);
        writableSheet.mergeCells(3, row, 4, row);
        writableSheet.addCell(new Label(0, row, ReconcileConstants.TOTAL, headerCell));
        writableSheet.addCell(new Number(3, row, totalDebits, numberCellFormat));

    }

    public void exportToExcel(ReconcileForm reconcileForm,String fileName, List<TransactionBean> checkList, List<TransactionBean> depositList) throws Exception {
        if (null != reconcileForm) {
            super.init(fileName);
            this.generateExcelSheet(reconcileForm, checkList, depositList);
            super.write();
            super.close();
        }
    }
}
