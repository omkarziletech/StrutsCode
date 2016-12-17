/**
 * 
 */
package com.gp.cong.logisoft.bc.accounting;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.domain.Notes;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.NotesDAO;
import com.logiware.accounting.domain.ApInvoice;
import com.gp.cvst.logisoft.domain.FclBlCostCodes;
import com.gp.cvst.logisoft.domain.Transaction;
import com.gp.cvst.logisoft.domain.TransactionLedger;
import com.gp.cvst.logisoft.hibernate.dao.AccountDetailsDAO;
import com.gp.cvst.logisoft.hibernate.dao.ApInvoiceDAO;
import com.gp.cvst.logisoft.hibernate.dao.CustAddressDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlCostCodesDAO;
import com.gp.cvst.logisoft.hibernate.dao.FiscalPeriodDAO;
import com.gp.cvst.logisoft.hibernate.dao.TransactionDAO;
import com.gp.cvst.logisoft.hibernate.dao.TransactionLedgerDAO;
import com.gp.cvst.logisoft.struts.form.AccrualsForm;
import com.logiware.utils.AuditNotesUtils;
import java.util.GregorianCalendar;
import com.logiware.hibernate.dao.AccountingLedgerDAO;
import com.logiware.hibernate.dao.ApTransactionHistoryDAO;
import com.logiware.hibernate.domain.ApTransactionHistory;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.commons.beanutils.PropertyUtils;

/**
 * @author Administrator
 *
 */
public class AccountPayableBC implements ConstantsInterface {

    TransactionDAO transactionDAO = new TransactionDAO();
    CustAddressDAO custAddressDAO = new CustAddressDAO();
    ApInvoiceDAO apInvoiceDAO = new ApInvoiceDAO();
    TransactionLedgerDAO transactionLedgerDAO = new TransactionLedgerDAO();

    public List getTransactionLedgerDetailsForPopUp(String vendorNumber, String invoiceNumber, String BLNumber, String type) throws Exception {
	return transactionLedgerDAO.getTransactionLedgerDetailsForPopUp(vendorNumber, invoiceNumber, BLNumber, type);
    }

    public String updateTransactionStatus(String payTransactions, String holdTransactions, String releaseTransactions, User loginUser) throws Exception {
	int totalPaid = 0;
	int totalHold = 0;
	int totalRelease = 0;
	if (CommonUtils.isNotEmpty(payTransactions)) {
	    double APAamount = 0.0;
	    double ARAamount = 0.0;
	    String payList[] = payTransactions.split(",");
	    for (int i = 0; i < payList.length; i++) {
		if (payList[i] != null && !payList[i].trim().equals("")) {
		    Transaction transaction = transactionDAO.findById(new Integer(payList[i]));
		    if (transaction.getTransactionType() != null && transaction.getTransactionType().equals("AR")) {
			ARAamount += transaction.getBalance();
		    }
		    if (transaction.getTransactionType() != null && transaction.getTransactionType().equals("AP")) {
			APAamount += transaction.getBalance();
		    }
		}
	    }
	    if (APAamount < ARAamount) {
		return "The selected AR amount must be less than the AP amount for a vendor, please use the Net Settlement screen to settle this account";
	    } else {
		totalPaid = transactionDAO.paidTransactions(payTransactions, loginUser);
	    }
	}
	if (CommonUtils.isNotEmpty(holdTransactions)) {
	    String[] holdIds = StringUtils.split(holdTransactions, ",");
	    for (String id : holdIds) {
		Transaction holdTransaction = transactionDAO.findById(Integer.parseInt(id));
		holdTransaction.setStatus(STATUS_HOLD);
		holdTransaction.setCreatedOn(new Date());
		holdTransaction.setCreatedBy(loginUser.getUserId());
		StringBuilder desc = new StringBuilder("Invoice '").append(holdTransaction.getInvoiceNumber()).append("'");
		desc.append(" of '").append(holdTransaction.getCustName()).append("'");
		desc.append(" for amount '").append(NumberUtils.formatNumber(holdTransaction.getTransactionAmt(), "###,###,##0.00")).append("'");
		desc.append(" is holded  by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
		AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.AP_INVOICE, holdTransaction.getCustNo() + "-" + holdTransaction.getInvoiceNumber(), NotesConstants.AP_INVOICE, loginUser);
		totalHold++;
	    }
	}
	if (CommonUtils.isNotEmpty(releaseTransactions)) {
	    totalRelease = transactionDAO.releaseTransactions(releaseTransactions, loginUser);
	}
	return totalPaid + " Transaction Paid, " + totalHold + " Transaction Holded and " + totalRelease + " Transaction Released";
    }

    public ApInvoice getInvoiceByInvoiceNumber(String invoiceNumber, String vendorNumber) throws Exception {
	return apInvoiceDAO.findInvoiceByInvoiceNumber(invoiceNumber, vendorNumber);
    }

    public void saveOrUpdateInvoice(AccrualsForm accrualsForm, String status, User loginUser) throws Exception {
	ApInvoice apInvoice = apInvoiceDAO.findInvoiceByInvoiceNumber(accrualsForm.getInvoicenumber(), accrualsForm.getVendornumber());
	Double amount = amount = new Double(accrualsForm.getInvoiceamount().replaceAll(",", ""));
	if (null == apInvoice) {//create new invoice if not exists
	    apInvoice = new ApInvoice();
	    StringBuilder desc = new StringBuilder("Invoice '").append(accrualsForm.getInvoicenumber()).append("'");
	    desc.append(" of '").append(accrualsForm.getVendor()).append("'");
	    desc.append(" for amount '").append(NumberUtils.formatNumber(amount, "###,###,##0.00")).append("'");
	    desc.append(" created by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
	    AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.AP_INVOICE, accrualsForm.getVendornumber() + "-" + accrualsForm.getInvoicenumber(), NotesConstants.AP_INVOICE, loginUser);
	} else if (CommonUtils.isEqualIgnoreCase(apInvoice.getStatus(), STATUS_DISPUTE)) {
	    apInvoice.setResolvedDate(new Date());
	}
	apInvoice.setCustomerName(accrualsForm.getVendor());
	apInvoice.setAccountNumber(accrualsForm.getVendornumber());
	apInvoice.setInvoiceNumber(accrualsForm.getInvoicenumber());
	apInvoice.setInvoiceAmount(amount);
	apInvoice.setTerm(accrualsForm.getTerm());
	apInvoice.setDate(DateUtils.parseDate(accrualsForm.getInvoicedate(), "MM/dd/yyyy"));
	apInvoice.setDueDate(DateUtils.parseDate(accrualsForm.getDuedate(), "MM/dd/yyyy"));
	if (CommonUtils.isEqualIgnoreCase(status, STATUS_DISPUTE)) {
	    StringBuilder desc = new StringBuilder("Invoice '").append(accrualsForm.getInvoicenumber()).append("'");
	    desc.append(" of '").append(accrualsForm.getVendor()).append("'");
	    desc.append(" for amount '").append(NumberUtils.formatNumber(amount, "###,###,##0.00")).append("'");
	    desc.append(" marked as dispute by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
	    AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.AP_INVOICE, accrualsForm.getVendornumber() + "-" + accrualsForm.getInvoicenumber(), NotesConstants.AP_INVOICE, loginUser);
	} else if (CommonUtils.isEqualIgnoreCase(status, "UnDispute")) {
	    StringBuilder desc = new StringBuilder("Invoice '").append(accrualsForm.getInvoicenumber()).append("'");
	    desc.append(" of '").append(accrualsForm.getVendor()).append("'");
	    desc.append(" for amount '").append(NumberUtils.formatNumber(amount, "###,###,##0.00")).append("'");
	    desc.append(" unmarked as dispute by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
	    AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.AP_INVOICE, accrualsForm.getVendornumber() + "-" + accrualsForm.getInvoicenumber(), NotesConstants.AP_INVOICE, loginUser);
	} else if (CommonUtils.isEqualIgnoreCase(status, STATUS_REJECT)) {
	    StringBuilder desc = new StringBuilder("Invoice '").append(accrualsForm.getInvoicenumber()).append("'");
	    desc.append(" of '").append(accrualsForm.getVendor()).append("'");
	    desc.append(" rejected by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
	    AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.AP_INVOICE, accrualsForm.getVendornumber() + "-" + accrualsForm.getInvoicenumber(), NotesConstants.AP_INVOICE, loginUser);
	}
	apInvoice.setStatus(CommonUtils.isNotEqual(status, "UnDispute") ? status : STATUS_OPEN);
	apInvoiceDAO.saveOrUpdate(apInvoice);
    }

    public String saveAccruals(AccrualsForm accrualsForm, HttpServletRequest request, ServletContext servletContext, boolean isReadyToPay) throws Exception {
	HttpSession session = request.getSession();
	User loginUser = (User) session.getAttribute("loginuser");
	String returnMessage = ERROR;
	boolean hasUnDisputed = false;
	if (CommonUtils.isNotEmpty(accrualsForm.getInActiveTransactions())) {
	    updateSelectedAccrualsStatus(accrualsForm.getInActiveTransactions(), null, STATUS_INACTIVE, loginUser);
	    returnMessage = "Success";
	}
	if (CommonUtils.isNotEmpty(accrualsForm.getUndoInActiveTransactions())) {
	    updateSelectedAccrualsStatus(accrualsForm.getUndoInActiveTransactions(), null, "UnInActive", loginUser);
	    returnMessage = "Success";
	}
	if (CommonUtils.isNotEmpty(accrualsForm.getUndoDisputedTransactions())) {
	    updateSelectedAccrualsStatus(accrualsForm.getUndoDisputedTransactions(), null, "UnDispute", loginUser);
	    hasUnDisputed = true;
	    returnMessage = "Success";
	}
	if (CommonUtils.isNotEmpty(accrualsForm.getUndoInProgressTransactions())) {
	    updateSelectedAccrualsStatus(accrualsForm.getUndoInProgressTransactions(), null, "UnInProgress", loginUser);
	    returnMessage = "Success";
	}
	if (CommonUtils.isEqualIgnoreCase(accrualsForm.getAccrualsAction(), "Invoice")) {
	    saveOrUpdateInvoice(accrualsForm, STATUS_OPEN, loginUser);
	} else if (CommonUtils.isNotEmpty(accrualsForm.getInvoicenumber())) {
	    if (CommonUtils.isNotEmpty(accrualsForm.getRejectedTransactions()) && CommonUtils.isEmpty(accrualsForm.getAssignedTransactions())) {
		saveOrUpdateInvoice(accrualsForm, STATUS_VOID, loginUser);
		updateSelectedAccrualsStatus(accrualsForm.getRejectedTransactions(), accrualsForm.getInvoicenumber(), STATUS_VOID, loginUser);
		hasUnDisputed = false;
		returnMessage = "Success";
	    } else if (CommonUtils.isEmpty(accrualsForm.getRejectedTransactions()) && CommonUtils.isEmpty(accrualsForm.getAssignedTransactions())
		    && CommonUtils.isNotEmpty(accrualsForm.getUnRejectedTransactions())) {
		for (String id : StringUtils.split(accrualsForm.getUnRejectedTransactions(), ",")) {
		    TransactionLedger transactionLedger = transactionLedgerDAO.findById(Integer.parseInt(id));
		    if (null != transactionLedger) {
			apInvoiceDAO.updateApInvoiceStatusByInvoiceNumber(transactionLedger.getInvoiceNumber(), transactionLedger.getCustNo(), STATUS_OPEN);
			hasUnDisputed = false;
		    }
		}
		updateSelectedAccrualsStatus(accrualsForm.getUnRejectedTransactions(), accrualsForm.getInvoicenumber(), STATUS_OPEN, loginUser);
		returnMessage = "Success";
	    } else {
		if (CommonUtils.isEqualIgnoreCase(accrualsForm.getAccrualsAction(), STATUS_REJECT)
			&& CommonUtils.isNotEmpty(accrualsForm.getComments())) {
		    saveOrUpdateInvoice(accrualsForm, STATUS_REJECT, loginUser);
		    Notes notes = new Notes();
		    notes.setModuleId(NotesConstants.APCONFIGURATION);
		    notes.setModuleRefId(accrualsForm.getInvoicenumber());
		    notes.setNoteType(NotesConstants.NOTES_TYPE_MANUAL);
		    notes.setNoteDesc(accrualsForm.getComments());
		    notes.setUpdateDate(new Date());
		    notes.setVoidNote(NotesConstants.UNVOID_NOTES);
		    notes.setUpdatedBy(loginUser.getLoginName());
		    new NotesDAO().save(notes);
		    if (CommonUtils.isNotEmpty(accrualsForm.getAssignedTransactions())) {
			updateSelectedAccrualsStatus(accrualsForm.getAssignedTransactions(), accrualsForm.getInvoicenumber(), STATUS_UNASSIGN, loginUser);
		    }
		    returnMessage = "Success";
		    hasUnDisputed = false;
		}
		if (CommonUtils.isEqualIgnoreCase(accrualsForm.getAccrualsAction(), AMOUNT_NOT_EQUAL_TO_ASSIGNED_ACCRUALS_AMOUNT)) {
		    if (CommonUtils.isNotEmpty(accrualsForm.getAssignedTransactions())) {
			updateSelectedAccrualsStatus(accrualsForm.getAssignedTransactions(), accrualsForm.getInvoicenumber(), STATUS_IN_PROGRESS, loginUser);
		    }
		    if (CommonUtils.isEmpty(accrualsForm.getDisputedTransactions())) {
			saveOrUpdateInvoice(accrualsForm, STATUS_IN_PROGRESS, loginUser);
			hasUnDisputed = false;
			returnMessage = "Success";
		    }
		} else if (CommonUtils.isEqualIgnoreCase(accrualsForm.getAccrualsAction(), AMOUNT_EQUAL_TO_ASSIGNED_ACCRUALS_AMOUNT)
			&& CommonUtils.isNotEmpty(accrualsForm.getAssignedTransactions())) {
		    assignAccrualsToAP(accrualsForm, loginUser, isReadyToPay);
		    hasUnDisputed = false;
		    if (null != accrualsForm.getRejectedTransactions() && !accrualsForm.getRejectedTransactions().trim().equals("")) {
			updateSelectedAccrualsStatus(accrualsForm.getRejectedTransactions(), accrualsForm.getInvoicenumber(), STATUS_VOID, loginUser);
		    }
		    if (null != accrualsForm.getUnRejectedTransactions() && !accrualsForm.getUnRejectedTransactions().trim().equals("")) {
			updateSelectedAccrualsStatus(accrualsForm.getUnRejectedTransactions(), accrualsForm.getInvoicenumber(), STATUS_OPEN, loginUser);
		    }
		    if (CommonUtils.isNotEmpty(accrualsForm.getUndoDisputedTransactions())) {
			updateSelectedAccrualsStatus(accrualsForm.getDisputedTransactions(), null, "UnDispute", loginUser);
		    }
		    returnMessage = "Success";
		}
	    }
	}
	if (hasUnDisputed) {
	    saveOrUpdateInvoice(accrualsForm, "UnDispute", loginUser);
	}
	List<String> ids = new ArrayList<String>();
	if (CommonUtils.isNotEmpty(accrualsForm.getInActiveTransactions())) {
	    ids.addAll(Arrays.asList(StringUtils.split(StringUtils.removeEnd(accrualsForm.getInActiveTransactions(), ","), ",")));
	}
	if (CommonUtils.isNotEmpty(accrualsForm.getRejectedTransactions())) {
	    ids.addAll(Arrays.asList(StringUtils.split(StringUtils.removeEnd(accrualsForm.getRejectedTransactions(), ","), ",")));
	}
	if (CommonUtils.isNotEmpty(accrualsForm.getAssignedTransactions())) {
	    ids.addAll(Arrays.asList(StringUtils.split(StringUtils.removeEnd(accrualsForm.getAssignedTransactions(), ","), ",")));
	}
	if (CommonUtils.isNotEmpty(ids)) {
	    new AccountingLedgerDAO().setConvertToApFlagInSSMasterApprovedBL(ids);
	}
	return returnMessage;
    }

    private void assignAccrualsToAP(AccrualsForm accrualsForm, User loginUser, boolean isReadyToPay) throws Exception {
	AccountDetailsDAO accountDetailsDAO = new AccountDetailsDAO();
	ApTransactionHistoryDAO apTransactionHistoryDAO = new ApTransactionHistoryDAO();
	Date invoiceDate = DateUtils.parseDate(accrualsForm.getInvoicedate(), "MM/dd/yyyy H:mm");
	double summaryAmount = 0d;
	Set<String> billOfLadingNumbers = new HashSet<String>();
	String startDate = DateUtils.formatDate(invoiceDate, "yyyy-MM-dd H:mm");
	String fiscalStatus = "Close";
	for (int i = 0; i < 36; i++) {
	    fiscalStatus = new FiscalPeriodDAO().getStatusByStartandEndDate(startDate, startDate);
	    GregorianCalendar cal = new GregorianCalendar();
	    Calendar current = Calendar.getInstance();
	    cal.setTime(DateUtils.parseDate(startDate, "yyyy-MM-dd H:mm"));
	    if (null != fiscalStatus && fiscalStatus.trim().equalsIgnoreCase(STATUS_OPEN)) {
		if (i > 0) {
		    cal.set(Calendar.DAY_OF_MONTH, 1);
		    invoiceDate = cal.getTime();
		}
		break;
	    }
	    cal.add(Calendar.MONTH, 1);
	    if (cal.get(Calendar.YEAR) == current.get(Calendar.YEAR) + 3) {
		break;
	    }
	    startDate = DateUtils.formatDate(cal.getTime(), "yyyy-MM-dd H:mm");
	}
	Double invoiceAmount = new Double(accrualsForm.getInvoiceamount().replaceAll(",", ""));
	for (String accrualId : StringUtils.split(accrualsForm.getAssignedTransactions(), ",")) {
	    TransactionLedger assignedAccrual = transactionLedgerDAO.findById(new Integer(accrualId));
	    if (accountDetailsDAO.validateAccount(assignedAccrual.getGlAccountNumber())) {
		if (CommonUtils.isNotEqualIgnoreCase(accrualsForm.getVendornumber(), assignedAccrual.getCustNo())) {
		    StringBuilder desc = new StringBuilder("Accrual of '").append(assignedAccrual.getCustName()).append(" (").append(assignedAccrual.getCustNo()).append(")'");
		    desc.append(" changed to '").append(accrualsForm.getVendor()).append(" (").append(accrualsForm.getVendornumber()).append(")'");
		    desc.append(" by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
		    AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.ACCRUALS, assignedAccrual.getTransactionId().toString(), NotesConstants.ACCRUALS, loginUser);
		    assignedAccrual.setCustName(accrualsForm.getVendor());
		    assignedAccrual.setCustNo(accrualsForm.getVendornumber());
		}
		assignedAccrual.setInvoiceNumber(accrualsForm.getInvoicenumber());
		assignedAccrual.setSailingDate(null != assignedAccrual.getSailingDate() ? assignedAccrual.getSailingDate() : assignedAccrual.getTransactionDate());
		assignedAccrual.setTransactionDate(DateUtils.parseDate(accrualsForm.getInvoicedate(), "MM/dd/yyyy H:mm"));
		assignedAccrual.setPostedDate(invoiceDate);
		TransactionLedger pjSubledger = new TransactionLedger();
		PropertyUtils.copyProperties(pjSubledger, assignedAccrual);
		pjSubledger.setTransactionType(TRANSACTION_TYPE_ACCOUNT_PAYABLE);
		pjSubledger.setStatus(STATUS_OPEN);
		pjSubledger.setSubledgerSourceCode(SUB_LEDGER_CODE_PURCHASE_JOURNAL);
		pjSubledger.setCreatedOn(new Date());
		pjSubledger.setCreatedBy(loginUser.getUserId());
		transactionLedgerDAO.save(pjSubledger);
		assignedAccrual.setStatus(STATUS_ASSIGN);
		assignedAccrual.setUpdatedOn(new Date());
		assignedAccrual.setUpdatedBy(loginUser.getUserId());
		StringBuilder desc = new StringBuilder("Accrual of ");
		boolean addAnd = false;
		if (CommonUtils.isNotEmpty(assignedAccrual.getBillLaddingNo())) {
		    desc.append("B/L - '").append(assignedAccrual.getBillLaddingNo()).append("'");
		    addAnd = true;
		}
		if (CommonUtils.isNotEmpty(assignedAccrual.getDocReceipt())) {
		    if (addAnd) {
			desc.append(" and ");
		    }
		    desc.append("Doc Receipt - '").append(assignedAccrual.getDocReceipt()).append("'");
		    addAnd = true;
		}
		if (CommonUtils.isNotEmpty(assignedAccrual.getVoyageNo())) {
		    if (addAnd) {
			desc.append(" and ");
		    }
		    desc.append("Voyage - '").append(assignedAccrual.getVoyageNo()).append("'");
		}
		desc.append(" for amount '").append(NumberUtils.formatNumber(assignedAccrual.getTransactionAmt(), "###,###,##0.00")).append("'");
		desc.append(" is assigned to Invoice '").append(assignedAccrual.getInvoiceNumber()).append("'");
		desc.append(" by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
		AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.ACCRUALS, assignedAccrual.getTransactionId().toString(), NotesConstants.ACCRUALS, loginUser);
		if (null != assignedAccrual.getBillLaddingNo() && !assignedAccrual.getBillLaddingNo().trim().equals("")) {
		    billOfLadingNumbers.add(pjSubledger.getBillLaddingNo());
		}
		summaryAmount += assignedAccrual.getTransactionAmt();
		// Update Corresponding Bl Cost Records
		if (CommonUtils.isNotEmpty(assignedAccrual.getCostId())) {
		    FclBlCostCodes fclBlCostCodes = new FclBlCostCodesDAO().findById(assignedAccrual.getCostId());
		    if (null != fclBlCostCodes) {
			fclBlCostCodes.setInvoiceNumber(accrualsForm.getInvoicenumber());
			fclBlCostCodes.setAccName(accrualsForm.getVendor());
			fclBlCostCodes.setAccNo(accrualsForm.getVendornumber());
			fclBlCostCodes.setTransactionType(TRANSACTION_TYPE_ACCOUNT_PAYABLE);
		    }
		}
	    } else {
		throw new Exception("GL Account - " + assignedAccrual.getGlAccountNumber() + " is not a valid one");
	    }
	}
	if (CommonUtils.isEqual(invoiceAmount, summaryAmount)) {
	    String blNumbers = null;
	    for (String blnumber : billOfLadingNumbers) {
		if (null == blNumbers) {
		    blNumbers = blnumber;
		} else {
		    blNumbers += "," + blnumber;
		}
	    }
	    Iterator<String> it = billOfLadingNumbers.iterator();
	    //create ONE summary entry in TRANSACTION table
	    Transaction transaction = new Transaction();
	    transaction.setCustName(accrualsForm.getVendor());
	    transaction.setCustNo(accrualsForm.getVendornumber());
	    //TO-DO - Clarification need for billOfLading Number
	    //transaction.setBillLaddingNo(billLaddingNo);
	    transaction.setCustomerReferenceNo(StringUtils.left(blNumbers, 500));
	    if (it.hasNext()) {
		transaction.setBillLaddingNo(it.next());
	    }
	    if (CommonUtils.isNotEmpty(accrualsForm.getInvoicedate())) {
		transaction.setTransactionDate(DateUtils.parseToDate(accrualsForm.getInvoicedate()));
	    }
	    if (CommonUtils.isNotEmpty(accrualsForm.getDuedate())) {
		transaction.setDueDate(DateUtils.parseToDate(accrualsForm.getDuedate()));
	    }
	    transaction.setPostedDate(invoiceDate);
	    transaction.setTransactionAmt(summaryAmount);
	    transaction.setBalance(summaryAmount);
	    transaction.setBalanceInProcess(summaryAmount);
	    transaction.setBillTo(YES);
	    transaction.setInvoiceNumber(accrualsForm.getInvoicenumber());
	    transaction.setTransactionType(TRANSACTION_TYPE_ACCOUNT_PAYABLE);
	    transaction.setStatus(isReadyToPay ? STATUS_READY_TO_PAY : STATUS_OPEN);
	    transaction.setCreditTerms(Integer.parseInt(accrualsForm.getTerm()));
	    transaction.setCreatedOn(new Date());
	    transaction.setCreatedBy(loginUser.getUserId());
	    transactionDAO.save(transaction);
	    ApTransactionHistory apTransactionHistory = new ApTransactionHistory(transaction);
	    apTransactionHistory.setCreatedBy(loginUser.getLoginName());
	    apTransactionHistoryDAO.save(apTransactionHistory);
	    //set all selected ac type of accruals as assigned
	    saveOrUpdateInvoice(accrualsForm, TRANSACTION_TYPE_ACCOUNT_PAYABLE, loginUser);
	    String glPeriod = DateUtils.formatDate(invoiceDate, "yyyyMM");
	    StringBuilder desc = new StringBuilder("Invoice '").append(accrualsForm.getInvoicenumber()).append("'");
	    desc.append(" of '").append(accrualsForm.getVendor()).append("'");
	    desc.append(" for amount '").append(NumberUtils.formatNumber(invoiceAmount, "###,###,##0.00")).append("'");
	    desc.append(" posted to AP on GL Period '");
	    desc.append(glPeriod).append("' by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
	    AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.AP_INVOICE, accrualsForm.getVendornumber() + "-" + accrualsForm.getInvoicenumber(), NotesConstants.AP_INVOICE, loginUser);
	} else {
	    throw new Exception("Someting wrong. The allocated amount " + summaryAmount + " is not equal to invoice amount " + invoiceAmount);
	}
    }

    private void updateSelectedAccrualsStatus(String accrualsIds, String invoiceNumber, String status, User loginUser) throws Exception {
	if (null != accrualsIds) {
	    FclBlCostCodesDAO fclBlCostCodesDAO = new FclBlCostCodesDAO();
	    String[] ids = StringUtils.split(StringUtils.removeEnd(accrualsIds, ","), ",");
	    if (CommonUtils.isEqualIgnoreCase(status, "UnInActive")
		    || CommonUtils.isEqualIgnoreCase(status, "UnDispute")
		    || CommonUtils.isEqualIgnoreCase(status, "UnInProgress")) {
		String statusDesc = null;
		if (CommonUtils.isEqualIgnoreCase(status, "UnInActive")) {
		    statusDesc = "InActive";
		} else if (CommonUtils.isEqualIgnoreCase(status, "UnDispute")) {
		    statusDesc = "Dispute";
		} else {
		    statusDesc = "In Progress";
		}
		for (String id : ids) {
		    Integer transactionId = new Integer(id);
		    TransactionLedger accrual = transactionLedgerDAO.findById(transactionId);
		    StringBuilder desc = new StringBuilder("Accrual of ");
		    boolean addAnd = false;
		    if (CommonUtils.isNotEmpty(accrual.getBillLaddingNo())) {
			desc.append("B/L - '").append(accrual.getBillLaddingNo()).append("'");
			addAnd = true;
		    }
		    if (CommonUtils.isNotEmpty(accrual.getDocReceipt())) {
			if (addAnd) {
			    desc.append(" and ");
			}
			desc.append("Doc Receipt - '").append(accrual.getDocReceipt()).append("'");
			addAnd = true;
		    }
		    if (CommonUtils.isNotEmpty(accrual.getVoyageNo())) {
			if (addAnd) {
			    desc.append(" and ");
			}
			desc.append("Voyage - '").append(accrual.getVoyageNo()).append("'");
		    }
		    desc.append(" for amount '").append(NumberUtils.formatNumber(accrual.getTransactionAmt(), "###,###,##0.00")).append("'");
		    desc.append(" is unmarked as '").append(statusDesc).append("'");
		    desc.append(" by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
		    AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.ACCRUALS, accrual.getTransactionId().toString(), NotesConstants.ACCRUALS, loginUser);
		    accrual.setInvoiceNumber(null);
		    accrual.setStatus(STATUS_OPEN);
		    accrual.setUpdatedOn(new Date());
		    accrual.setUpdatedBy(loginUser.getUserId());
		    if (CommonUtils.isNotEmpty(accrual.getCostId())) {
			FclBlCostCodes fclBlCostCodes = fclBlCostCodesDAO.findById(accrual.getCostId());
			if (null != fclBlCostCodes) {
			    fclBlCostCodes.setTransactionType(TRANSACTION_TYPE_ACCRUALS);
			}
		    }
		}
	    } else if (CommonUtils.isEqualIgnoreCase(status, STATUS_INACTIVE)
		    || CommonUtils.isEqualIgnoreCase(status, STATUS_DISPUTE)
		    || CommonUtils.isEqualIgnoreCase(status, STATUS_IN_PROGRESS)) {
		String statusDesc = null;
		if (CommonUtils.isEqualIgnoreCase(status, STATUS_INACTIVE)) {
		    statusDesc = "InActive";
		} else if (CommonUtils.isEqualIgnoreCase(status, STATUS_DISPUTE)) {
		    statusDesc = "Dispute";
		} else {
		    statusDesc = "In Progress";
		}
		for (String id : ids) {
		    Integer transactionId = new Integer(id);
		    TransactionLedger accrual = transactionLedgerDAO.findById(transactionId);
		    StringBuilder desc = new StringBuilder("Accrual of ");
		    boolean addAnd = false;
		    if (CommonUtils.isNotEmpty(accrual.getBillLaddingNo())) {
			desc.append("B/L - '").append(accrual.getBillLaddingNo()).append("'");
			addAnd = true;
		    }
		    if (CommonUtils.isNotEmpty(accrual.getDocReceipt())) {
			if (addAnd) {
			    desc.append(" and ");
			}
			desc.append("Doc Receipt - '").append(accrual.getDocReceipt()).append("'");
			addAnd = true;
		    }
		    if (CommonUtils.isNotEmpty(accrual.getVoyageNo())) {
			if (addAnd) {
			    desc.append(" and ");
			}
			desc.append("Voyage - '").append(accrual.getVoyageNo()).append("'");
		    }
		    desc.append(" for amount '").append(NumberUtils.formatNumber(accrual.getTransactionAmt(), "###,###,##0.00")).append("'");
		    desc.append(" is marked as '").append(statusDesc).append("'");
		    if (CommonUtils.isNotEmpty(invoiceNumber)
			    && (CommonUtils.isEqualIgnoreCase(status, STATUS_DISPUTE)
			    || CommonUtils.isEqualIgnoreCase(status, STATUS_IN_PROGRESS))) {
			desc.append(" for Invoice - '").append(invoiceNumber).append("'");
		    }
		    desc.append(" by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
		    AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.ACCRUALS, accrual.getTransactionId().toString(), NotesConstants.ACCRUALS, loginUser);
		    accrual.setInvoiceNumber(invoiceNumber);
		    accrual.setStatus(status);
		    accrual.setUpdatedOn(new Date());
		    accrual.setUpdatedBy(loginUser.getUserId());
		    if (CommonUtils.isNotEmpty(accrual.getCostId())) {
			FclBlCostCodes fclBlCostCodes = fclBlCostCodesDAO.findById(accrual.getCostId());
			if (null != fclBlCostCodes) {
			    fclBlCostCodes.setInvoiceNumber(invoiceNumber);
			    if (CommonUtils.isEqualIgnoreCase(status, STATUS_INACTIVE)) {
				fclBlCostCodes.setTransactionType(TRANSACTION_TYPE_ACCOUNT_PAYABLE);
			    } else if (CommonUtils.isEqualIgnoreCase(status, STATUS_IN_PROGRESS)) {
				fclBlCostCodes.setTransactionType(TRANSACTION_TYPE_IN_PROGRESS);
			    } else if (CommonUtils.isEqualIgnoreCase(status, STATUS_DISPUTE)) {
				fclBlCostCodes.setTransactionType(TRANSACTION_TYPE_DISPUTE);
			    }
			}
		    }
		}
	    } else if (CommonUtils.isEqualIgnoreCase(status, STATUS_ASSIGN)) {
		for (String id : ids) {
		    Integer transactionId = new Integer(id);
		    TransactionLedger accrual = transactionLedgerDAO.findById(transactionId);
		    accrual.setInvoiceNumber(invoiceNumber);
		    accrual.setStatus(STATUS_ASSIGN);
		    StringBuilder desc = new StringBuilder("Accrual of ");
		    boolean addAnd = false;
		    if (CommonUtils.isNotEmpty(accrual.getBillLaddingNo())) {
			desc.append("B/L - '").append(accrual.getBillLaddingNo()).append("'");
			addAnd = true;
		    }
		    if (CommonUtils.isNotEmpty(accrual.getDocReceipt())) {
			if (addAnd) {
			    desc.append(" and ");
			}
			desc.append("Doc Receipt - '").append(accrual.getDocReceipt()).append("'");
			addAnd = true;
		    }
		    if (CommonUtils.isNotEmpty(accrual.getVoyageNo())) {
			if (addAnd) {
			    desc.append(" and ");
			}
			desc.append("Voyage - '").append(accrual.getVoyageNo()).append("'");
		    }
		    desc.append(" for amount '").append(NumberUtils.formatNumber(accrual.getTransactionAmt(), "###,###,##0.00")).append("'");
		    desc.append("is assigned to Invoice '").append(accrual.getInvoiceNumber()).append("'");
		    desc.append(" by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
		    AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.ACCRUALS, accrual.getTransactionId().toString(), NotesConstants.ACCRUALS, loginUser);
		    accrual.setUpdatedOn(new Date());
		    accrual.setUpdatedBy(loginUser.getUserId());
		}
	    } else {
		for (String id : ids) {
		    Integer transactionId = new Integer(id);
		    TransactionLedger transactionLedger = transactionLedgerDAO.findById(transactionId);
		    transactionLedger.setInvoiceNumber(invoiceNumber);
		    transactionLedger.setStatus(status);
		    transactionLedger.setUpdatedOn(new Date());
		    transactionLedger.setUpdatedBy(loginUser.getUserId());
		}
	    }
	}
    }
}
