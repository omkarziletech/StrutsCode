package com.logiware.accounting.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Lakshmi Naryanan
 */
@Entity
@Table(name = "ap_invoice")
@org.hibernate.annotations.DynamicInsert(true) 
@org.hibernate.annotations.DynamicUpdate(true)
public class ApInvoice implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "customer_name")
    private String customerName;
    @Column(name = "account_number")
    private String accountNumber;
    @Column(name = "customer_type")
    private String customerType;
    @Column(name = "contact_name")
    private String contactName;
    @Column(name = "address")
    private String address;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "invoice_number")
    private String invoiceNumber;
    @Column(name = "search_invoice_number")
    private String searchInvoiceNumber;
    @Column(name = "bl_dr_number")
    private String blDrNumber;
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;
    @Column(name = "term")
    private String term;
    @Column(name = "due_date")
    @Temporal(TemporalType.DATE)
    private Date dueDate;
    @Column(name = "notes")
    private String notes;
    @Column(name = "reject_invoice")
    private String rejectInvoice;
    @Column(name = "start_period")
    private String startPeriod;
    @Column(name = "end_period")
    private String endPeriod;
    @Column(name = "invoice_amount")
    private double invoiceAmount;
    @Column(name = "status")
    private String status;
    @Column(name = "ready_to_post")
    private String readyToPost;
    @Column(name = "dispute_date")
    @Temporal(TemporalType.DATE)
    private Date disputeDate;
    @Column(name = "resolved_date")
    @Temporal(TemporalType.DATE)
    private Date resolvedDate;
    @Column(name = "user_id")
    private Integer userId;
    @Lob
    @Column(name = "from_address")
    private String fromAddress;
    @Lob
    @Column(name = "to_address")
    private String toAddress;
    @Basic(optional = false)
    @Column(name = "direct_gl_account")
    private boolean directGlAccount;
    @Basic(optional = false)
    @Column(name = "recurring")
    private boolean recurring;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "apInvoice", fetch = FetchType.LAZY)
    private List<ApInvoiceLineItem> lineItems;

    public ApInvoice() {
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getCustomerName() {
	return customerName;
    }

    public void setCustomerName(String customerName) {
	this.customerName = customerName;
    }

    public String getAccountNumber() {
	return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
	this.accountNumber = accountNumber;
    }

    public String getCustomerType() {
	return customerType;
    }

    public void setCustomerType(String customerType) {
	this.customerType = customerType;
    }

    public String getContactName() {
	return contactName;
    }

    public void setContactName(String contactName) {
	this.contactName = contactName;
    }

    public String getAddress() {
	return address;
    }

    public void setAddress(String address) {
	this.address = address;
    }

    public String getPhoneNumber() {
	return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
	this.phoneNumber = phoneNumber;
    }

    public String getInvoiceNumber() {
	return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
	this.invoiceNumber = invoiceNumber;
    }

    public String getSearchInvoiceNumber() {
	return searchInvoiceNumber;
    }

    public void setSearchInvoiceNumber(String searchInvoiceNumber) {
	this.searchInvoiceNumber = searchInvoiceNumber;
    }

    public String getBlDrNumber() {
	return blDrNumber;
    }

    public void setBlDrNumber(String blDrNumber) {
	this.blDrNumber = blDrNumber;
    }

    public Date getDate() {
	return date;
    }

    public void setDate(Date date) {
	this.date = date;
    }

    public String getTerm() {
	return term;
    }

    public void setTerm(String term) {
	this.term = term;
    }

    public Date getDueDate() {
	return dueDate;
    }

    public void setDueDate(Date dueDate) {
	this.dueDate = dueDate;
    }

    public String getNotes() {
	return notes;
    }

    public void setNotes(String notes) {
	this.notes = notes;
    }

    public String getRejectInvoice() {
	return rejectInvoice;
    }

    public void setRejectInvoice(String rejectInvoice) {
	this.rejectInvoice = rejectInvoice;
    }

    public String getStartPeriod() {
	return startPeriod;
    }

    public void setStartPeriod(String startPeriod) {
	this.startPeriod = startPeriod;
    }

    public String getEndPeriod() {
	return endPeriod;
    }

    public void setEndPeriod(String endPeriod) {
	this.endPeriod = endPeriod;
    }

    public double getInvoiceAmount() {
	return invoiceAmount;
    }

    public void setInvoiceAmount(double invoiceAmount) {
	this.invoiceAmount = invoiceAmount;
    }

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    public String getReadyToPost() {
	return readyToPost;
    }

    public void setReadyToPost(String readyToPost) {
	this.readyToPost = readyToPost;
    }

    public Date getDisputeDate() {
	return disputeDate;
    }

    public void setDisputeDate(Date disputeDate) {
	this.disputeDate = disputeDate;
    }

    public Date getResolvedDate() {
	return resolvedDate;
    }

    public void setResolvedDate(Date resolvedDate) {
	this.resolvedDate = resolvedDate;
    }

    public String getFromAddress() {
	return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
	this.fromAddress = fromAddress;
    }

    public String getToAddress() {
	return toAddress;
    }

    public void setToAddress(String toAddress) {
	this.toAddress = toAddress;
    }

    public boolean isDirectGlAccount() {
	return directGlAccount;
    }

    public void setDirectGlAccount(boolean directGlAccount) {
	this.directGlAccount = directGlAccount;
    }

    public boolean isRecurring() {
	return recurring;
    }

    public void setRecurring(boolean recurring) {
	this.recurring = recurring;
    }

    public List<ApInvoiceLineItem> getLineItems() {
	return lineItems;
    }

    public void setLineItems(List<ApInvoiceLineItem> lineItems) {
	this.lineItems = lineItems;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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
	if (!(object instanceof ApInvoice)) {
	    return false;
	}
	ApInvoice other = (ApInvoice) object;
	if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "com.logiware.accounting.domain.ApInvoice[ id=" + id + " ]";
    }
}
