package com.logiware.bean;

/**
 *
 * @author lakshh
 */
public class PaymentBean {

    private String transactionId;
    private String paymentId;
    private String paidAmount;
    private String adjustAmount;
    private String glAccount;
    private String docReceipt;
    private String notes;
    private String customerName;
    private String customerNumber;
    private String invoiceOrBl;
    private String transactionAmount;
    private String balanceInProcess;
    private String transactionType;
    private boolean matches;
    private String comments;

    public String getAdjustAmount() {
	return adjustAmount;
    }

    public void setAdjustAmount(String adjustAmount) {
	this.adjustAmount = adjustAmount;
    }

    public String getDocReceipt() {
	return docReceipt;
    }

    public void setDocReceipt(String docReceipt) {
	this.docReceipt = docReceipt;
    }

    public String getGlAccount() {
	return glAccount;
    }

    public void setGlAccount(String glAccount) {
	this.glAccount = glAccount;
    }

    public String getNotes() {
	return notes;
    }

    public void setNotes(String notes) {
	this.notes = notes;
    }

    public String getPaidAmount() {
	return paidAmount;
    }

    public void setPaidAmount(String paidAmount) {
	this.paidAmount = paidAmount;
    }

    public String getPaymentId() {
	return paymentId;
    }

    public void setPaymentId(String paymentId) {
	this.paymentId = paymentId;
    }

    public String getTransactionId() {
	return transactionId;
    }

    public void setTransactionId(String transactionId) {
	this.transactionId = transactionId;
    }

    public String getCustomerName() {
	return customerName;
    }

    public void setCustomerName(String customerName) {
	this.customerName = customerName;
    }

    public String getCustomerNumber() {
	return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
	this.customerNumber = customerNumber;
    }

    public String getInvoiceOrBl() {
	return invoiceOrBl;
    }

    public void setInvoiceOrBl(String invoiceOrBl) {
	this.invoiceOrBl = invoiceOrBl;
    }

    public String getTransactionAmount() {
	return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
	this.transactionAmount = transactionAmount;
    }

    public String getTransactionType() {
	return transactionType;
    }

    public void setTransactionType(String transactionType) {
	this.transactionType = transactionType;
    }

    public String getBalanceInProcess() {
	return balanceInProcess;
    }

    public void setBalanceInProcess(String balanceInProcess) {
	this.balanceInProcess = balanceInProcess;
    }

    public boolean isMatches() {
	return matches;
    }

    public void setMatches(boolean matches) {
	this.matches = matches;
    }

    public String getComments() {
	return comments;
    }

    public void setComments(String comments) {
	this.comments = comments;
    }
}
