package com.gp.cvst.logisoft.domain;

import java.util.Date;

import java.io.Serializable;

public class LineItem implements Serializable {

    private String lineItemId;
    private String journalEntryId;
    private String reference;
    private String referenceDesc;
    private String account;
    private String accountDesc;
    private Double debit;
    private Double credit;
    private String currency;
    private FiscalPeriod period;
    private Date date;
    private String status;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccountDesc() {
        return accountDesc;
    }

    public void setAccountDesc(String accountDesc) {
        this.accountDesc = accountDesc;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public Double getDebit() {
        return debit;
    }

    public void setDebit(Double debit) {
        this.debit = debit;
    }

    public String getJournalEntryId() {
        return journalEntryId;
    }

    public void setJournalEntryId(String journalEntryId) {
        this.journalEntryId = journalEntryId;
    }

    public String getLineItemId() {
        return lineItemId;
    }

    public void setLineItemId(String lineItemId) {
        this.lineItemId = lineItemId;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getReferenceDesc() {
        return referenceDesc;
    }

    public void setReferenceDesc(String referenceDesc) {
        this.referenceDesc = referenceDesc;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public FiscalPeriod getPeriod() {
        return period;
    }

    public void setPeriod(FiscalPeriod period) {
        this.period = period;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
