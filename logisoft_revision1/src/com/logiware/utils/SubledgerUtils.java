package com.logiware.utils;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cvst.logisoft.domain.AccountDetails;
import com.gp.cvst.logisoft.domain.Batch;
import com.gp.cvst.logisoft.domain.FiscalPeriod;
import com.gp.cvst.logisoft.domain.JournalEntry;
import com.gp.cvst.logisoft.domain.LineItem;
import com.gp.cvst.logisoft.hibernate.dao.BatchDAO;
import com.gp.cvst.logisoft.hibernate.dao.FiscalPeriodDAO;
import com.gp.cvst.logisoft.hibernate.dao.SubledgerAcctsDAO;
import com.gp.cvst.logisoft.struts.form.RecieptsLedgerForm;
import com.logiware.accounting.dao.SubledgerDAO;
import com.logiware.accounting.model.SubledgerModel;
import com.logiware.hibernate.domain.TransactionLedgerHistory;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class SubledgerUtils {

    private double totalAmount = 0d;
    private double totalDebit = 0d;
    private double totalCredit = 0d;

    public void postSubledger(RecieptsLedgerForm recieptsLedgerForm, User loginUser) throws Exception {
	String subledgerType = recieptsLedgerForm.getSubLedgerType();
	FiscalPeriod fiscalPeriod = new FiscalPeriodDAO().findById(Integer.parseInt(recieptsLedgerForm.getPeriod()));
	String period = fiscalPeriod.getPeriodDis();
	GenericCode genericCode = new GenericCodeDAO().findByCodeName(subledgerType, Integer.valueOf(33));
	Batch batch = createBatch(period, genericCode);
	JournalEntry journalEntry = createJournalEntry(batch.getBatchId().toString(), period, genericCode);
	String startDate = DateUtils.formatDate(DateUtils.parseDate(recieptsLedgerForm.getStartDate(), "MM/dd/yyyy"), "yyyy-MM-dd 00:00:00");
	String endDate = DateUtils.formatDate(DateUtils.parseDate(recieptsLedgerForm.getEndDate(), "MM/dd/yyyy"), "yyyy-MM-dd 23:59:59");
	String journalEntryId = journalEntry.getJournalEntryId();
	List lineItems = createLineItems(journalEntryId, period, startDate, endDate, genericCode);
	Set lineItemSet = new HashSet(lineItems);
	journalEntry.setLineItemSet(lineItemSet);
	if (StringUtils.equalsIgnoreCase(genericCode.getCode(), "NET SETT")) {
	    journalEntry.setDebit(this.totalDebit);
	    journalEntry.setCredit(this.totalCredit);
	    batch.setTotalDebit(this.totalDebit);
	    batch.setTotalCredit(this.totalCredit);
	} else {
	    journalEntry.setDebit(this.totalAmount);
	    journalEntry.setCredit(this.totalAmount);
	    batch.setTotalDebit(this.totalAmount);
	    batch.setTotalCredit(this.totalAmount);
	}
	Set journalEntries = new HashSet();
	journalEntries.add(journalEntry);
	batch.setJournalEntrySet(journalEntries);
	new BatchDAO().save(batch);
	StringBuilder desc = new StringBuilder("GL Batch '").append(batch.getBatchId()).append("' on ");
	desc.append(subledgerType).append(" Subledger Close for ").append(period);
	desc.append(" added by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
	AuditNotesUtils.insertAuditNotes(desc.toString(), "GL_BATCH", batch.getBatchId().toString(), "GL_BATCH", loginUser);
    }

    private Batch createBatch(String period, GenericCode subledgerCode) throws Exception {
	Batch batch = new Batch();
	batch.setReadyToPost("yes");
	batch.setStatus("Open");
	batch.setSourceLedger(subledgerCode);
	batch.setBatchDesc(subledgerCode.getCode() + " Subledger Close for " + period);
	batch.setTotalCredit(0d);
	batch.setTotalDebit(0d);
	return new BatchDAO().saveAndReturn(batch);
    }

    private JournalEntry createJournalEntry(String batchId, String period, GenericCode subledgerCode) {
	JournalEntry journalEntry = new JournalEntry();
	journalEntry.setBatchId(Integer.valueOf(Integer.parseInt(batchId)));
	journalEntry.setJournalEntryId(batchId + "-001");
	journalEntry.setJournalEntryDesc(subledgerCode.getCode() + " Subledger Close for " + period);
	journalEntry.setPeriod(period);
	journalEntry.setJeDate(new Date());
	journalEntry.setSourceCode(subledgerCode);
	journalEntry.setSourceCodeDesc(subledgerCode.getCode());
	journalEntry.setMemo("");
	journalEntry.setDebit(0d);
	journalEntry.setCredit(0d);
	journalEntry.setSubledgerClose("Y");
	return journalEntry;
    }

    private List<LineItem> createLineItems(String journalEntryId, String period, String startDate, String endDate, GenericCode subledgerCode) throws Exception {
	List<LineItem> lineItems = new ArrayList<LineItem>();
//	SubledgerDAO subledgerDAO = new SubledgerDAO();
//	List<SubledgerModel> subledgers = subledgerDAO.getSubledgers(subledgerCode.getCode(), startDate, endDate);
//	if (CommonUtils.isEmpty(subledgers)) {
//	    throw new Exception("Posting failed. Nothing to post.");
//	}
//	StringBuilder lineItemId;
//	Date today = new Date();
//	Integer index = 1;
//	if (StringUtils.equalsIgnoreCase(subledgerCode.getCode(), "NET SETT")) {
//	    for (SubledgerModel subledger : subledgers) {
//		LineItem lineItem = new LineItem();
//		lineItemId = new StringBuilder();
//		lineItemId.append(journalEntryId).append("-").append(StringUtils.leftPad(index.toString(), 3, "0"));
//		lineItem.setJournalEntryId(journalEntryId);
//		lineItem.setLineItemId(lineItemId.toString());
//		lineItem.setAccount(subledger.getGlAccount());
//		lineItem.setAccountDesc(subledger.getGlAccountDesc());
//		lineItem.setCurrency("USD");
//		lineItem.setReference(subledgerCode.getCode());
//		lineItem.setReferenceDesc(subledgerCode.getCode() + " SubLedger Close for " + period);
//		lineItem.setDate(today);
//		lineItem.setDebit(subledger.getDebit());
//		lineItem.setCredit(subledger.getCredit());
//		this.totalDebit += subledger.getDebit();
//		this.totalCredit += subledger.getCredit();
//		lineItems.add(lineItem);
//		index++;
//		String id = subledger.getId();
//		subledgerDAO.update(subledgerCode.getCode(), journalEntryId, lineItemId.toString(), id);
//		List<TransactionLedgerHistory> histories = subledger.createHistories(lineItemId.toString());
//		for (TransactionLedgerHistory history : histories) {
//		    subledgerDAO.saveHistory(history);
//		}
//	    }
//	    this.totalDebit = Math.abs(this.totalDebit);
//	    this.totalCredit = Math.abs(this.totalCredit);
//	} else {
//	    for (SubledgerModel subledger : subledgers) {
//		LineItem lineItem = new LineItem();
//		lineItemId = new StringBuilder();
//		lineItemId.append(journalEntryId).append("-").append(StringUtils.leftPad(index.toString(), 3, "0"));
//		lineItem.setJournalEntryId(journalEntryId);
//		lineItem.setLineItemId(lineItemId.toString());
//		lineItem.setAccount(subledger.getGlAccount());
//		lineItem.setAccountDesc(subledger.getGlAccountDesc());
//		lineItem.setCurrency("USD");
//		lineItem.setReference(subledgerCode.getCode());
//		lineItem.setReferenceDesc(subledgerCode.getCode() + " SubLedger Close for " + period);
//		lineItem.setDate(today);
//		lineItem.setDebit(subledger.getDebit());
//		lineItem.setCredit(subledger.getCredit());
//		this.totalAmount += subledger.getDebit() - subledger.getCredit();
//		lineItems.add(lineItem);
//		index++;
//		String id = subledger.getId();
//		subledgerDAO.update(subledgerCode.getCode(), journalEntryId, lineItemId.toString(), id);
//		List<TransactionLedgerHistory> histories = subledger.createHistories(lineItemId.toString());
//		for (TransactionLedgerHistory history : histories) {
//		    subledgerDAO.saveHistory(history);
//		}
//	    }
//	    if (index.intValue() > 0) {
//		LineItem lineItem = new LineItem();
//		lineItemId = new StringBuilder();
//		lineItemId.append(journalEntryId).append("-").append(StringUtils.leftPad(index.toString(), 3, "0"));
//		lineItem.setJournalEntryId(journalEntryId);
//		lineItem.setLineItemId(lineItemId.toString());
//		AccountDetails controlAccount = new SubledgerAcctsDAO().getControlAccount(subledgerCode.getCode());
//		lineItem.setAccount(controlAccount.getAccount());
//		lineItem.setAccountDesc(controlAccount.getAcctDesc());
//		lineItem.setCurrency("USD");
//		lineItem.setReference(subledgerCode.getCode());
//		lineItem.setReferenceDesc(subledgerCode.getCode() + " SubLedger Close for " + period);
//		lineItem.setDate(today);
//		if (this.totalAmount > 0d) {
//		    lineItem.setDebit(0d);
//		    lineItem.setCredit(this.totalAmount);
//		} else {
//		    lineItem.setDebit(-this.totalAmount);
//		    lineItem.setCredit(0d);
//		}
//		lineItems.add(lineItem);
//	    }
//	    this.totalAmount = Math.abs(this.totalAmount);
//	}
	return lineItems;
    }
}
