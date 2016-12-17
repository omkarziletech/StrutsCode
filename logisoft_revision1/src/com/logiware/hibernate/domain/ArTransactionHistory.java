package com.logiware.hibernate.domain;

import java.util.Date;

/**
 *
 * @author Lakshminarayanan
 */
public class ArTransactionHistory implements java.io.Serializable {

    private static final long serialVersionUID = -8039737966833437594L;
    private Integer id;
    private String customerNumber;
    private String blNumber;
    private String invoiceNumber;
    private Date invoiceDate;
    private Date postedDate;
    private Date transactionDate;
    private Double transactionAmount = 0d;
    private Double adjustmentAmount = 0d;
    private String voyageNumber;
    private String customerReferenceNumber;
    private String transactionType;
    private String glAccountNumber;
    private String checkNumber;
    private Integer arBatchId;
    private Integer apBatchId;
    private String correctionNotice;
    private Date createdDate;
    private String createdBy;

    public Integer getApBatchId() {
	return apBatchId;
    }

    public void setApBatchId(Integer apBatchId) {
	this.apBatchId = apBatchId;
    }

    public Integer getArBatchId() {
	return arBatchId;
    }

    public void setArBatchId(Integer arBatchId) {
	this.arBatchId = arBatchId;
    }

    public String getBlNumber() {
	return blNumber;
    }

    public void setBlNumber(String blNumber) {
	this.blNumber = blNumber;
    }

    public String getCreatedBy() {
	return createdBy;
    }

    public void setCreatedBy(String createdBy) {
	this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
	return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
	this.createdDate = createdDate;
    }

    public String getCustomerNumber() {
	return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
	this.customerNumber = customerNumber;
    }

    public String getCustomerReferenceNumber() {
	return customerReferenceNumber;
    }

    public void setCustomerReferenceNumber(String customerReferenceNumber) {
	this.customerReferenceNumber = customerReferenceNumber;
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public Date getInvoiceDate() {
	return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
	this.invoiceDate = invoiceDate;
    }

    public String getInvoiceNumber() {
	return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
	this.invoiceNumber = invoiceNumber;
    }

    public Date getPostedDate() {
	return postedDate;
    }

    public void setPostedDate(Date postedDate) {
	this.postedDate = postedDate;
    }

    public Double getTransactionAmount() {
	return transactionAmount;
    }

    public void setTransactionAmount(Double transactionAmount) {
	this.transactionAmount = transactionAmount;
    }

    public Double getAdjustmentAmount() {
	return adjustmentAmount;
    }

    public void setAdjustmentAmount(Double adjustmentAmount) {
	this.adjustmentAmount = adjustmentAmount;
    }

    public Date getTransactionDate() {
	return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
	this.transactionDate = transactionDate;
    }

    public String getTransactionType() {
	return transactionType;
    }

    public void setTransactionType(String transactionType) {
	this.transactionType = transactionType;
    }

    public String getGlAccountNumber() {
	return glAccountNumber;
    }

    public void setGlAccountNumber(String glAccountNumber) {
	this.glAccountNumber = glAccountNumber;
    }

    public String getCheckNumber() {
	return checkNumber;
    }

    public void setCheckNumber(String checkNumber) {
	this.checkNumber = checkNumber;
    }

    public String getVoyageNumber() {
	return voyageNumber;
    }

    public void setVoyageNumber(String voyageNumber) {
	this.voyageNumber = voyageNumber;
    }

    public String getCorrectionNotice() {
        return correctionNotice;
    }

    public void setCorrectionNotice(String correctionNotice) {
        this.correctionNotice = correctionNotice;
    }
}
