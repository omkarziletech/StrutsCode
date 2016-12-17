package com.logiware.utils;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.infomata.data.CSVFormat;
import com.infomata.data.DataFile;
import com.infomata.data.DataRow;
import com.logiware.bean.ReconcileModel;
import com.logiware.form.ReconcileForm;
import com.logiware.hibernate.dao.ReconcileDAO;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author Lakshmi Naryanan
 */
public class ReconcileCsvReader {

    public ReconcileForm readCsv(ReconcileForm reconcileForm) throws Exception {
	DataFile csvFile = null;
	DataFile exceptionFile = null;
	try {
	    ReconcileDAO reconcileDAO = new ReconcileDAO();
	    File template = new File(reconcileForm.getImportFileName());
	    String originalFileName = reconcileForm.getImportFileName();
	    StringBuilder exceptionFileName = new StringBuilder();
	    exceptionFileName.append(FilenameUtils.getFullPath(originalFileName));
	    exceptionFileName.append(FilenameUtils.getBaseName(originalFileName));
	    exceptionFileName.append("_exception.");
	    exceptionFileName.append(FilenameUtils.getExtension(originalFileName));
	    File errorFile = new File(exceptionFileName.toString());
	    csvFile = DataFile.createReader("8859_1");
	    csvFile.setDataFormat(new CSVFormat());
	    csvFile.open(template);
	    DataRow row = csvFile.next();
	    if (null != row) {
		boolean isException;
		List<DataRow> zbaRows = new ArrayList<DataRow>();
		List<ReconcileModel> transactions = new ArrayList<ReconcileModel>();
		double zbaAmount = 0d;
		String glAccount = reconcileForm.getGlAccount();
		reconcileForm.setReconcileDate(row.getString(0));
		Double bankBalance = row.getDouble(10);
		reconcileForm.setBankBalance(NumberUtils.formatNumber(bankBalance));
		String reconcileDate = DateUtils.formatDate(DateUtils.parseDate(reconcileForm.getReconcileDate(), "MM/dd/yyyy"), "yyyy-MM-dd 23:59:59");
		csvFile.next();
		for (row = csvFile.next(); null != row; row = csvFile.next()) {
		    isException = false;
		    String transactionType = row.getString(5);
		    Double amount = row.getDouble(7);
		    String referenceNumber = row.getString(8);
		    if (CommonUtils.isEqualIgnoreCase(transactionType, "ZBA Credit")
			    || CommonUtils.isEqualIgnoreCase(transactionType, "ZBA Debit Transfer")) {
			zbaAmount += amount;
			zbaRows.add(row);
		    } else if (CommonUtils.isEqualIgnoreCase(transactionType, "Check Deposit Package")) {
			ReconcileModel transaction = reconcileDAO.getCheckDepositPackage(glAccount, reconcileDate, amount);
			if (null != transaction) {
			    transactions.add(transaction);
			} else {
			    isException = true;
			}
		    } else if (CommonUtils.isEqualIgnoreCase(transactionType, "Check Paid")) {
			ReconcileModel transaction = reconcileDAO.getCheckPaid(glAccount, reconcileDate, referenceNumber, amount);
			if (null != transaction) {
			    transactions.add(transaction);
			} else {
			    isException = true;
			}
		    } else if (CommonUtils.isEqualIgnoreCase(transactionType, "Incoming Wire Transfer")) {
			ReconcileModel transaction = reconcileDAO.getIncomingWireTransfer(glAccount, reconcileDate, amount);
			if (null != transaction) {
			    transactions.add(transaction);
			} else {
			    isException = true;
			}
		    } else if (CommonUtils.isEqualIgnoreCase(transactionType, "Preauth ACH Credit")) {
			ReconcileModel transaction = reconcileDAO.getPreauthAchCredit(glAccount, reconcileDate, amount);
			if (null != transaction) {
			    transactions.add(transaction);
			} else {
			    isException = true;
			}
		    } else if (CommonUtils.isEqualIgnoreCase(transactionType, "Preauthorized ACH Debit")) {
			ReconcileModel transaction = reconcileDAO.getPreauthorizedAchDebit(glAccount, reconcileDate, amount);
			if (null != transaction) {
			    transactions.add(transaction);
			} else {
			    isException = true;
			}
		    } else if (CommonUtils.isEqualIgnoreCase(transactionType, "Outgoing Wire Transfer")) {
			ReconcileModel transaction = reconcileDAO.getOutgoingWireTransfer(glAccount, reconcileDate, amount);
			if (null != transaction) {
			    transactions.add(transaction);
			} else {
			    isException = true;
			}
		    } else if (CommonUtils.isEqualIgnoreCase(transactionType, "Deposited Item Returned")) {
			ReconcileModel transaction = reconcileDAO.getDepositedItemReturned(glAccount, reconcileDate, amount);
			if (null != transaction) {
			    transactions.add(transaction);
			} else {
			    isException = true;
			}
		    } else if (CommonUtils.isEqualIgnoreCase(transactionType, "Individual Loan Payment")) {
			ReconcileModel transaction = reconcileDAO.getIndividualLoanPayment(glAccount, reconcileDate, amount);
			if (null != transaction) {
			    transactions.add(transaction);
			} else {
			    isException = true;
			}
		    } else if (CommonUtils.isEqualIgnoreCase(transactionType, "Ind Loan Deposit")) {
			ReconcileModel transaction = reconcileDAO.getIndLoanDeposit(glAccount, reconcileDate, amount);
			if (null != transaction) {
			    transactions.add(transaction);
			} else {
			    isException = true;
			}
		    }else{
			isException = true;
		    }
		    if (isException) {
			if (null == exceptionFile) {
			    exceptionFile = DataFile.createWriter("8859_1", false);
			    exceptionFile.setDataFormat(new CSVFormat());
			    exceptionFile.open(errorFile);
			}
			DataRow errorRow = exceptionFile.next();
			for (int index = 0; index < row.size(); index++) {
			    errorRow.add(row.getString(index));
			}
		    }
		}
		if (CommonUtils.isNotEmpty(zbaAmount)) {
		    ReconcileModel transaction = reconcileDAO.getZbaTransaction(glAccount, reconcileDate, zbaAmount);
		    if (null != transaction) {
			transactions.add(transaction);
		    } else {
			if (null == exceptionFile) {
			    exceptionFile = DataFile.createWriter("8859_1", false);
			    exceptionFile.setDataFormat(new CSVFormat());
			    exceptionFile.open(errorFile);
			}
			for (DataRow zbaRow : zbaRows) {
			    DataRow errorRow = exceptionFile.next();
			    for (int index = 0; index < zbaRow.size(); index++) {
				errorRow.add(zbaRow.getString(index));
			    }
			}
		    }
		}
		reconcileForm.setTransactions(transactions);
		reconcileForm.setTotalRows(transactions.size());
		reconcileForm.setTotalPages(1);
		reconcileForm.setSelectedRows(transactions.size());
		reconcileForm.setSelectedPage(1);
		reconcileForm.setGlBalance(NumberUtils.formatNumber(reconcileDAO.getGlBalance(reconcileForm)));
		reconcileForm.setDepositsOpen("0.00");
		reconcileForm.setChecksOpen("0.00");
		Double difference = 0d;
		difference += Double.parseDouble(reconcileForm.getBankBalance().replaceAll(",", ""));
		difference -= Double.parseDouble(reconcileForm.getGlBalance().replaceAll(",", ""));
		reconcileForm.setDifference(NumberUtils.formatNumber(difference));
	    }
	    if (null != exceptionFile) {
		reconcileForm.setExceptionFileName(exceptionFileName.toString());
	    }
	    return reconcileForm;
	} catch (Exception e) {
	    throw e;
	} finally {
	    if (null != csvFile) {
		csvFile.close();
	    }
	    if (null != exceptionFile){
		exceptionFile.close();
	    }
	}
    }
}
