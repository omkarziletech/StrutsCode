package com.logiware.hibernate.domain;

import com.gp.cvst.logisoft.domain.Transaction;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Lakshmi Naryanan
 */
@Entity
@Table(name = "ap_transaction_history")
public class ApTransactionHistory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "vendor_number")
    private String vendorNumber;
    @Basic(optional = false)
    @Column(name = "invoice_number")
    private String invoiceNumber;
    @Column(name = "invoice_date")
    @Temporal(TemporalType.DATE)
    private Date invoiceDate;
    @Column(name = "posted_date")
    @Temporal(TemporalType.DATE)
    private Date postedDate;
    @Column(name = "transaction_date")
    @Temporal(TemporalType.DATE)
    private Date transactionDate;
    @Column(name = "vendor_reference")
    private String vendorReference;
    @Column(name = "check_number")
    private String checkNumber;
    @Basic(optional = false)
    @Column(name = "amount")
    private Double amount = 0d;
    @Column(name = "transaction_type")
    private String transactionType;
    @Column(name = "ap_batch_id", nullable = true)
    private Integer apBatchId = null;
    @Column(name = "ar_batch_id", nullable = true)
    private Integer arBatchId = null;
    @Column(name = "gl_account")
    private String glAccount;
    @Column(name = "charge_code")
    private String chargeCode;
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Column(name = "created_by")
    private String createdBy;

    public ApTransactionHistory() {
    }

    public ApTransactionHistory(Transaction transaction) {
	this.vendorNumber = transaction.getCustNo();
	this.invoiceNumber = transaction.getInvoiceNumber();
	this.invoiceDate = transaction.getTransactionDate();
	this.postedDate = transaction.getPostedDate();
	this.transactionDate = transaction.getTransactionDate();
	this.vendorReference = transaction.getCustomerReferenceNo();
	this.checkNumber = transaction.getChequeNumber();
	this.amount = transaction.getTransactionAmt();
	this.transactionType = transaction.getTransactionType();
	this.apBatchId = transaction.getApBatchId();
	this.arBatchId = transaction.getArBatchId();
	this.glAccount = transaction.getGlAccountNumber();
	this.chargeCode = transaction.getChargeCode();
	this.createdDate = new Date();
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getVendorNumber() {
	return vendorNumber;
    }

    public void setVendorNumber(String vendorNumber) {
	this.vendorNumber = vendorNumber;
    }

    public String getInvoiceNumber() {
	return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
	this.invoiceNumber = invoiceNumber;
    }

    public Date getInvoiceDate() {
	return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
	this.invoiceDate = invoiceDate;
    }

    public Date getPostedDate() {
	return postedDate;
    }

    public void setPostedDate(Date postedDate) {
	this.postedDate = postedDate;
    }

    public Date getTransactionDate() {
	return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
	this.transactionDate = transactionDate;
    }

    public String getVendorReference() {
	return vendorReference;
    }

    public void setVendorReference(String vendorReference) {
	this.vendorReference = vendorReference;
    }

    public String getCheckNumber() {
	return checkNumber;
    }

    public void setCheckNumber(String checkNumber) {
	this.checkNumber = checkNumber;
    }

    public Double getAmount() {
	return amount;
    }

    public void setAmount(Double amount) {
	this.amount = amount;
    }

    public String getTransactionType() {
	return transactionType;
    }

    public void setTransactionType(String transactionType) {
	this.transactionType = transactionType;
    }

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

    public String getGlAccount() {
	return glAccount;
    }

    public void setGlAccount(String glAccount) {
	this.glAccount = glAccount;
    }

    public String getChargeCode() {
	return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
	this.chargeCode = chargeCode;
    }

    public Date getCreatedDate() {
	return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
	this.createdDate = createdDate;
    }

    public String getCreatedBy() {
	return createdBy;
    }

    public void setCreatedBy(String createdBy) {
	this.createdBy = createdBy;
    }

    @Override
    public int hashCode() {
	int hash = 0;
	hash += (id != null ? id.hashCode() : 0);
	return hash;
    }

    @Override
    public boolean equals(Object object) {
	// TODO: Warning - this method won't work in the case the id fields are not set
	if (!(object instanceof ApTransactionHistory)) {
	    return false;
	}
	ApTransactionHistory other = (ApTransactionHistory) object;
	if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "com.luckyboy.ApTransactionHistory[ id=" + id + " ]";
    }
}
