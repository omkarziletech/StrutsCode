package com.logiware.bean;

import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cvst.logisoft.domain.LineItem;
import org.apache.commons.lang3.StringUtils;

public class LineItemBean {

    private String id;
    private String journalEntryId;
    private String reference;
    private String referenceDescription;
    private String account;
    private String accountDescription;
    private String debit = "0.00";
    private String credit = "0.00";
    private String currency;
    private String date;
    private String status;
    private Double totalDebit;
    private Double totalCredit;
    private String year;
    private String period;

    public LineItemBean() {
    }

    public LineItemBean(Integer suffix, String journalEntryId) {
        StringBuilder lineItemId = new StringBuilder();
        lineItemId.append(journalEntryId).append("-").append(StringUtils.leftPad(suffix.toString(), 3, "0"));
        this.id = lineItemId.toString();
        this.journalEntryId = journalEntryId;
    }

    public LineItemBean(LineItem lineItem)throws Exception {
        this.id = lineItem.getLineItemId();
        this.journalEntryId = lineItem.getJournalEntryId();
        this.reference = lineItem.getReference();
        this.referenceDescription = lineItem.getReferenceDesc();
        this.account = lineItem.getAccount();
        this.accountDescription = lineItem.getAccountDesc();
        this.debit = NumberUtils.formatNumber(lineItem.getDebit(), "#0.00");
        this.credit = NumberUtils.formatNumber(lineItem.getCredit(), "#0.00");
        this.currency = lineItem.getCurrency();
        this.date = DateUtils.formatDate(lineItem.getDate(), "MM/dd/yyyy");
        this.status = lineItem.getStatus();
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccountDescription() {
        return this.accountDescription;
    }

    public void setAccountDescription(String accountDescription) {
        this.accountDescription = accountDescription;
    }

    public String getCurrency() {
        return null == this.currency || this.currency.isEmpty() ? "USD" : this.currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCredit() {
        return this.credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getDebit() {
        return this.debit;
    }

    public void setDebit(String debit) {
        this.debit = debit;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJournalEntryId() {
        return this.journalEntryId;
    }

    public void setJournalEntryId(String journalEntryId) {
        this.journalEntryId = journalEntryId;
    }

    public String getReference() {
        return this.reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getReferenceDescription() {
        return this.referenceDescription;
    }

    public void setReferenceDescription(String referenceDescription) {
        this.referenceDescription = referenceDescription;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getTotalCredit() {
        return this.totalCredit;
    }

    public void setTotalCredit(Double totalCredit) {
        this.totalCredit = totalCredit;
    }

    public Double getTotalDebit() {
        return this.totalDebit;
    }

    public void setTotalDebit(Double totalDebit) {
        this.totalDebit = totalDebit;
    }

    public String getPeriod() {
        return this.period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getYear() {
        return this.year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}