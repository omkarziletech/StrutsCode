package com.logiware.bean;

/**
 *
 * @author lakshh
 */
public class ArBatchBean {

    private String batchId;
    private String depositDate;
    private String batchType;
    private String totalAmount;
    private String appliedAmount;
    private String balanceAmount;
    private String user;
    private String bankAccount;
    private String glAccount;
    private String status;
    private String notes;
    private String date;
    private boolean hasDocuments;
    private boolean uploaded;
    private String clearedDate;

    public String getAppliedAmount() {
	return appliedAmount;
    }

    public void setAppliedAmount(String appliedAmount) {
	this.appliedAmount = appliedAmount;
    }

    public String getBalanceAmount() {
	return balanceAmount;
    }

    public void setBalanceAmount(String balanceAmount) {
	this.balanceAmount = balanceAmount;
    }

    public String getBankAccount() {
	return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
	this.bankAccount = bankAccount;
    }

    public String getBatchId() {
	return batchId;
    }

    public void setBatchId(String batchId) {
	this.batchId = batchId;
    }

    public String getBatchType() {
	return batchType;
    }

    public void setBatchType(String batchType) {
	this.batchType = batchType;
    }

    public String getDepositDate() {
	return depositDate;
    }

    public void setDepositDate(String depositDate) {
	this.depositDate = depositDate;
    }

    public String getGlAccount() {
	return glAccount;
    }

    public void setGlAccount(String glAccount) {
	this.glAccount = glAccount;
    }

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    public String getTotalAmount() {
	return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
	this.totalAmount = totalAmount;
    }

    public String getUser() {
	return user;
    }

    public void setUser(String user) {
	this.user = user;
    }

    public String getNotes() {
	return notes;
    }

    public void setNotes(String notes) {
	this.notes = notes;
    }

    public String getDate() {
	return date;
    }

    public void setDate(String date) {
	this.date = date;
    }

    public boolean isHasDocuments() {
	return hasDocuments;
    }

    public void setHasDocuments(boolean hasDocuments) {
	this.hasDocuments = hasDocuments;
    }

    public boolean isUploaded() {
	return uploaded;
    }

    public void setUploaded(String uploaded) {
	this.uploaded = Boolean.valueOf(uploaded);
    }

    public String getClearedDate() {
	return clearedDate;
    }

    public void setClearedDate(String clearedDate) {
	this.clearedDate = clearedDate;
    }
}
