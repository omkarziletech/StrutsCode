package com.logiware.bean;

/**
 *
 * @author Lakshmi Naryanan
 */
public class ReconcileModel {

    private String id;
    private String transactionType;
    private String referenceNumber;
    private String batchId;
    private String transactionDate;
    private String debit;
    private String credit;
    private String status;
    private double amount;

    public double getAmount() {
	return amount;
    }

    public void setAmount(double amount) {
	this.amount = amount;
    }

    public String getBatchId() {
	return batchId;
    }

    public void setBatchId(String batchId) {
	this.batchId = batchId;
    }

    public String getCredit() {
	return credit;
    }

    public void setCredit(String credit) {
	this.credit = credit;
    }

    public String getDebit() {
	return debit;
    }

    public void setDebit(String debit) {
	this.debit = debit;
    }

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public String getReferenceNumber() {
	return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
	this.referenceNumber = referenceNumber;
    }

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    public String getTransactionDate() {
	return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
	this.transactionDate = transactionDate;
    }

    public String getTransactionType() {
	return transactionType;
    }

    public void setTransactionType(String transactionType) {
	this.transactionType = transactionType;
    }
}
