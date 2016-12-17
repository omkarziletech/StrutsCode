package com.gp.cong.logisoft.bc.accounting;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.gp.cong.logisoft.domain.BankReconcilliationDetail;
import com.gp.cong.logisoft.domain.BankReconcilliationSummary;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.BankReconcilliationDetailDAO;
import com.gp.cong.logisoft.hibernate.dao.BankReconcilliationSummaryDAO;
import com.gp.cvst.logisoft.AccountingConstants;
import com.gp.cvst.logisoft.beans.TransactionBean;
import com.gp.cvst.logisoft.domain.BankDetails;
import com.gp.cvst.logisoft.domain.FiscalPeriod;
import com.gp.cvst.logisoft.domain.LineItem;
import com.gp.cvst.logisoft.domain.Transaction;
import com.gp.cvst.logisoft.domain.TransactionLedger;
import com.gp.cvst.logisoft.hibernate.dao.AccountBalanceDAO;
import com.gp.cvst.logisoft.hibernate.dao.ArBatchDAO;
import com.gp.cvst.logisoft.hibernate.dao.BankDetailsDAO;
import com.gp.cvst.logisoft.hibernate.dao.FiscalPeriodDAO;
import com.gp.cvst.logisoft.hibernate.dao.LineItemDAO;
import com.gp.cvst.logisoft.hibernate.dao.SubledgerDAO;
import com.gp.cvst.logisoft.reports.dto.ReconcilationExcelSheetCreator;
import com.gp.cvst.logisoft.struts.form.ReconcileForm;
import com.infomata.data.CSVFormat;
import com.infomata.data.DataFile;
import com.infomata.data.DataRow;
import com.logiware.hibernate.dao.AccountingLedgerDAO;
import com.logiware.hibernate.dao.AccountingTransactionDAO;
import com.logiware.utils.AuditNotesUtils;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

import org.apache.log4j.Logger;

/**
 * @author pradeepp
 *
 */
public class APReconcilationBC {

    private static final Logger log = Logger.getLogger(APReconcilationBC.class);

    public void uploadFile(String fileName, String filePath) throws Exception {

	try {
	    File fileToCreate = new File(filePath);

	    DataFile read = DataFile.createReader("8859_1");
	    read.setDataFormat(new CSVFormat());
	    // first line is column header
	    read.containsHeader(true);
	    try {
		read.open(fileToCreate);
		int rowCount = AccountingConstants.CSV_ROW_COUNT;
		Integer bankReconcileSummaryId = null;
		for (DataRow row = read.next(); row != null; row = read.next()) {
		    if (rowCount == 0) {
			// Insert Records In To BankReconcilliationSummary
			bankReconcileSummaryId = this.insertInToBankReconcilliationSummary(row);

		    } else {
			if (rowCount > AccountingConstants.CSV_ROW_COUNT + 2) {
			    // Insert Records In To BankReconcilliationDetails
			    this.insertInToBankReconcilliationDetails(row, bankReconcileSummaryId);
			}
		    }
		    rowCount++;
		}
	    } finally {
		read.close();
		fileToCreate.delete();
	    }

	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    log.info("uploadFile failed on " + new Date(),e);
	}

	// List
	// budgetList=budgetExcelReader.retrieveValuesFromExcel(fileToCreate,bList);

    }

    /**
     * @param row
     */
    private Integer insertInToBankReconcilliationSummary(DataRow row) throws Exception {
	BankReconcilliationSummary bankReconcilliationSummary = new BankReconcilliationSummary(row);
	BankReconcilliationSummaryDAO bankReconcilliationSummaryDAO = new BankReconcilliationSummaryDAO();
	bankReconcilliationSummaryDAO.save(bankReconcilliationSummary);
	return bankReconcilliationSummary.getId();
    }

    /**
     * @param row
     * @param bankReconcileSummaryId
     */
    private void insertInToBankReconcilliationDetails(DataRow row, Integer bankReconcileSummaryId) throws Exception {
	BankReconcilliationDetail bankReconcilliationDetail = new BankReconcilliationDetail(row);
	BankReconcilliationDetailDAO bankReconcilliationDetailDAO = new BankReconcilliationDetailDAO();
	bankReconcilliationDetail.setBankReconcileSummaryId(bankReconcileSummaryId);
	bankReconcilliationDetailDAO.save(bankReconcilliationDetail);
    }

    public void reconcile(ReconcileForm reconcileForm, User loginUser) throws Exception {
	if (null != reconcileForm.getTransactionIds() && null != reconcileForm.getClearedDates()) {
	    AccountingTransactionDAO accountingTransactionDAO = new AccountingTransactionDAO();
	    AccountingLedgerDAO accountingLedgerDAO = new AccountingLedgerDAO();
	    LineItemDAO lineItemDAO = new LineItemDAO();
	    String selectedIds = StringUtils.removeEnd(StringUtils.removeStart(reconcileForm.getTransactionIds(), ":-"), ":-");
	    String selectedDates = StringUtils.removeEnd(StringUtils.removeStart(reconcileForm.getClearedDates(), ":-"), ":-");
	    String[] ids = StringUtils.splitByWholeSeparator(selectedIds, ":-");
	    String[] clearedDates = StringUtils.splitByWholeSeparator(selectedDates, ":-");
	    int i = 0;
	    for (String id : ids) {
		if (id.contains("@" + CommonConstants.MODULE_TRANSACTION_LEDGER)) {
		    id = id.replaceAll("@" + CommonConstants.MODULE_TRANSACTION_LEDGER, "");
		    TransactionLedger transactionLedger = accountingLedgerDAO.findById(Integer.parseInt(id));
		    transactionLedger.setReconciled(CommonConstants.YES);
		    transactionLedger.setReconciledDate(DateUtils.parseToDate(clearedDates[i]));
		    transactionLedger.setCleared(CommonConstants.YES);
		    transactionLedger.setClearedDate(DateUtils.parseToDate(clearedDates[i]));
		} else if (id.contains("@" + CommonConstants.MODULE_TRANSACTION)) {
		    String groupedIds = StringUtils.removeEnd(StringUtils.removeStart(id, ","), ",");
		    String[] transactionIds = StringUtils.splitByWholeSeparator(groupedIds, ",");
		    for (String transactionId : transactionIds) {
			transactionId = transactionId.replaceAll("@" + CommonConstants.MODULE_TRANSACTION, "");
			Transaction transaction = accountingTransactionDAO.findById(Integer.parseInt(transactionId));
			transaction.setStatus(CommonConstants.CLEAR);
			transaction.setCleared(CommonConstants.YES);
			transaction.setClearedDate(DateUtils.parseToDate(clearedDates[i]));
			transaction.setReconciled(CommonConstants.YES);
			transaction.setReconciledDate(DateUtils.parseToDate(clearedDates[i]));
			String vendorNumber = transaction.getCustNo();
			String invoiceNumber = transaction.getInvoiceNumber();
			Date clearedDate = transaction.getClearedDate();
			Integer apBatchId = transaction.getApBatchId();
			accountingTransactionDAO.reconcileApTransaction(vendorNumber, invoiceNumber, clearedDate, apBatchId);
		    }
		} else if (id.contains("@" + CommonConstants.MODULE_LINE_ITEM)) {
		    id = id.replaceAll("@" + CommonConstants.MODULE_LINE_ITEM, "");
		    LineItem lineItem = lineItemDAO.findById(id);
		    lineItem.setStatus(CommonConstants.CLEAR);
		    lineItem.setDate(DateUtils.parseToDate(clearedDates[i]));
		}
		i++;
	    }
	    List<BankDetails> bankDetailses = new BankDetailsDAO().findByGLAccountNo(reconcileForm.getGlAccountNumber().trim());
	    for (BankDetails bankDetails : bankDetailses) {
		bankDetails.setLastReconciledDate(DateUtils.parseDate(reconcileForm.getBankReconcileDate(), "MM/dd/yyyy"));
	    }
	    StringBuilder desc = new StringBuilder("GL Account '").append(reconcileForm.getGlAccountNumber().trim()).append("'");
	    desc.append(" reconciled by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
	    AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.RECONCILATION, reconcileForm.getGlAccountNumber().trim(),
		    NotesConstants.RECONCILATION, loginUser);
	}
    }

    public void downloadFile(ReconcileForm reconcileForm, String fileName) throws Exception {
	List<TransactionBean> openChecksList = new ArrayList<TransactionBean>();
	List<TransactionBean> openDepositsList = new ArrayList<TransactionBean>();
	List<TransactionBean> reconcileList = this.getReconcileTransactions(reconcileForm);
	for (TransactionBean transactionBean : reconcileList) {
	    if (CommonUtils.isNotEqual(transactionBean.getStatus(), CommonConstants.STATUS_RECONCILE_IN_PROGRESS)) {
		if (CommonUtils.isNotEmpty(transactionBean.getCredit())) {
		    openChecksList.add(transactionBean);
		}
		if (CommonUtils.isNotEmpty(transactionBean.getDebit())) {
		    openDepositsList.add(transactionBean);
		}
	    }
	}
	new ReconcilationExcelSheetCreator().exportToExcel(reconcileForm, fileName, openChecksList, openDepositsList);
    }

    public void saveReconcileInProcess(String selectedIds, String unSelectedIds) throws Exception {
	if (CommonUtils.isNotEmpty(selectedIds)) {
	    selectedIds = StringUtils.removeEnd(StringUtils.removeStart(selectedIds, ","), ",");
	    String[] ids = StringUtils.split(selectedIds, ",");
	    for (String id : ids) {
		if (id.endsWith("@" + CommonConstants.MODULE_TRANSACTION)) {
		    id = id.replaceAll("@" + CommonConstants.MODULE_TRANSACTION, "");
		    Transaction transaction = new AccountingTransactionDAO().findById(Integer.parseInt(id));
		    transaction.setReconciled(CommonConstants.STATUS_IN_PROGRESS);
		} else if (id.endsWith("@" + CommonConstants.MODULE_TRANSACTION_LEDGER)) {
		    id = id.replaceAll("@" + CommonConstants.MODULE_TRANSACTION_LEDGER, "");
		    TransactionLedger transactionLedger = new AccountingLedgerDAO().findById(Integer.parseInt(id));
		    transactionLedger.setReconciled(CommonConstants.STATUS_IN_PROGRESS);
		} else if (id.endsWith("@" + CommonConstants.MODULE_LINE_ITEM)) {
		    id = id.replaceAll("@" + CommonConstants.MODULE_LINE_ITEM, "");
		    LineItem lineItem = new LineItemDAO().findById(id);
		    lineItem.setStatus(CommonConstants.STATUS_RECONCILE_IN_PROGRESS);
		}
	    }
	}
	if (CommonUtils.isNotEmpty(unSelectedIds)) {
	    unSelectedIds = StringUtils.removeEnd(StringUtils.removeStart(unSelectedIds, ","), ",");
	    String[] unIds = StringUtils.split(unSelectedIds, ",");
	    for (String unId : unIds) {
		if (unId.endsWith("@" + CommonConstants.MODULE_TRANSACTION)) {
		    unId = unId.replaceAll("@" + CommonConstants.MODULE_TRANSACTION, "");
		    Transaction transaction = new AccountingTransactionDAO().findById(Integer.parseInt(unId));
		    transaction.setReconciled(CommonConstants.NO);
		} else if (unId.endsWith("@" + CommonConstants.MODULE_TRANSACTION_LEDGER)) {
		    unId = unId.replaceAll("@" + CommonConstants.MODULE_TRANSACTION_LEDGER, "");
		    TransactionLedger transactionLedger = new AccountingLedgerDAO().findById(Integer.parseInt(unId));
		    transactionLedger.setReconciled(CommonConstants.NO);
		} else if (unId.endsWith("@" + CommonConstants.MODULE_LINE_ITEM)) {
		    unId = unId.replaceAll("@" + CommonConstants.MODULE_LINE_ITEM, "");
		    LineItem lineItem = new LineItemDAO().findById(unId);
		    lineItem.setStatus(CommonConstants.STATUS_OPEN);
		}
	    }
	}

    }

    public List<TransactionBean> getReconcileTransactions(ReconcileForm reconcileForm) throws Exception {
	Double glBalance = 0d;
	Double openchecks = 0d;
	Double openDeposits = 0d;
	Double difference = 0d;
	Calendar cutOffDate = Calendar.getInstance();
	cutOffDate.setTime(DateUtils.parseDate(reconcileForm.getBankReconcileDate(), "MM/dd/yyyy"));
	Integer period = cutOffDate.get(Calendar.MONTH) + 1;
	String periodDis = DateUtils.formatDate(cutOffDate.getTime(), "yyyyMM");
	FiscalPeriod fiscalPeriod = new FiscalPeriodDAO().getLastClosedPeriod(periodDis);
	Integer lastClosedPeriod = Integer.parseInt(fiscalPeriod.getPeriod());
	Integer lastClosedYear = fiscalPeriod.getYear();
	if (lastClosedPeriod != period) {
	    //Previous Periods Account Balance
	    glBalance += new AccountBalanceDAO().getAccountBalanceForPreviousPeriods(reconcileForm.getGlAccountNumber(), lastClosedPeriod, lastClosedYear);
	    //For start date open period
	    Calendar startDate = Calendar.getInstance();
	    startDate.set(lastClosedYear, lastClosedPeriod, 1);
	    String startPeriod = DateUtils.formatDate(startDate.getTime(), "yyyyMM");
	    String endPeriod = DateUtils.formatDate(cutOffDate.getTime(), "yyyyMM");
	    //Check for Current JEs Posted to GLAccount
	    glBalance += new LineItemDAO().getJEBalanceForGlAccount(reconcileForm.getGlAccountNumber(), startPeriod, endPeriod);
	    //Check for Current SubLedgers Posted to GLAccount
	    glBalance += new SubledgerDAO().getSubLedgerBalanceForGlAccount(reconcileForm.getGlAccountNumber(), startDate.getTime(), cutOffDate.getTime());
	} else {
	    glBalance += new AccountBalanceDAO().getAccountBalanceForPreviousPeriods(reconcileForm.getGlAccountNumber(), lastClosedPeriod, lastClosedYear);
	}

	List<TransactionBean> reconcileTransactions = new ArrayList<TransactionBean>();
	// JournalEntries From GL Batch
	List<TransactionBean> journalEntries = new LineItemDAO().getJournalEntriesForReconcile(reconcileForm);
	if (CommonUtils.isNotEmpty(journalEntries)) {
	    reconcileTransactions.addAll(journalEntries);
	}
	// Open Checks From AP Payment
	List openChecksTransactions = new AccountingTransactionDAO().getOpenChecksForReconcile(reconcileForm);
	if (CommonUtils.isNotEmpty(openChecksTransactions)) {
	    reconcileTransactions.addAll(openChecksTransactions);
	}
	// Assigned Costs From AP Invoice
	List costsAgainstDirectGl = new AccountingLedgerDAO().getCostsAgainstDirectGlForReconcile(reconcileForm);
	if (CommonUtils.isNotEmpty(costsAgainstDirectGl)) {
	    reconcileTransactions.addAll(costsAgainstDirectGl);
	}
	// Open Deposits From AR Batch
	List openDepositTransactions = new ArBatchDAO().getOpenDepositsForReconcile(reconcileForm);
	if (CommonUtils.isNotEmpty(openDepositTransactions)) {
	    reconcileTransactions.addAll(openDepositTransactions);
	}
	for (TransactionBean transactionBean : reconcileTransactions) {
	    if (CommonUtils.isNotEqual(transactionBean.getStatus(), CommonConstants.STATUS_RECONCILE_IN_PROGRESS)) {
		if (CommonUtils.isNotEmpty(transactionBean.getCredit())) {
		    openchecks += Double.parseDouble(transactionBean.getCredit().replaceAll(",", ""));
		}
		if (CommonUtils.isNotEmpty(transactionBean.getDebit())) {
		    openDeposits += Double.parseDouble(transactionBean.getDebit().replaceAll(",", ""));
		}
	    }
	}
	reconcileForm.setGlAccountBalance(NumberUtils.formatNumber(glBalance, "###,###,##0.00"));
	reconcileForm.setOpenChecks(NumberUtils.formatNumber(openchecks, "###,###,##0.00"));
	reconcileForm.setOpenDeposits(NumberUtils.formatNumber(openDeposits, "###,###,##0.00"));
	if (CommonUtils.isNotEmpty(reconcileForm.getBankStatementBalance())) {
	    difference = Double.parseDouble(reconcileForm.getBankStatementBalance().replaceAll(",", ""));
	}
	if (CommonUtils.isNotEmpty(reconcileForm.getGlAccountBalance())) {
	    difference -= Double.parseDouble(reconcileForm.getGlAccountBalance().replaceAll(",", ""));
	}
	if (CommonUtils.isNotEmpty(reconcileForm.getOpenChecks())) {
	    difference -= Double.parseDouble(reconcileForm.getOpenChecks().replaceAll(",", ""));
	}
	if (CommonUtils.isNotEmpty(reconcileForm.getOpenDeposits())) {
	    difference += Double.parseDouble(reconcileForm.getOpenDeposits().replaceAll(",", ""));
	}
	reconcileForm.setDifference(NumberUtils.formatNumber(difference, "###,###,##0.00"));
	List<BankDetails> bankDetailses = new BankDetailsDAO().findByGLAccountNo(reconcileForm.getGlAccountNumber().trim());
	for (BankDetails bankDetails : bankDetailses) {
	    reconcileForm.setLastReconciledDate(DateUtils.formatDate(bankDetails.getLastReconciledDate(), "MM/dd/yyyy"));
	}
	return reconcileTransactions;
    }
}
