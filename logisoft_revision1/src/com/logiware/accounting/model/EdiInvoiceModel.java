package com.logiware.accounting.model;

/**
 *
 * @author Lakshmi Naryanan
 */
public class EdiInvoiceModel {

    private Integer id;
    private Integer logId;
    private String vendorName;
    private String vendorNumber;
    private String invoiceNumber;
    private String invoiceDate;
    private String invoiceAmount;
    private String status;
    private boolean overhead;
    private boolean documents;
    private boolean manualNotes;

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public Integer getLogId() {
	return logId;
    }

    public void setLogId(Integer logId) {
	this.logId = logId;
    }

    public String getVendorName() {
	return vendorName;
    }

    public void setVendorName(String vendorName) {
	this.vendorName = vendorName;
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

    public String getInvoiceDate() {
	return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
	this.invoiceDate = invoiceDate;
    }

    public String getInvoiceAmount() {
	return invoiceAmount;
    }

    public void setInvoiceAmount(String invoiceAmount) {
	this.invoiceAmount = invoiceAmount;
    }

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    public boolean isOverhead() {
	return overhead;
    }

    public void setOverhead(String overhead) {
	this.overhead = Boolean.valueOf(overhead);
    }

    public boolean isDocuments() {
	return documents;
    }

    public void setDocuments(String documents) {
	this.documents = Boolean.valueOf(documents);
    }

    public boolean isManualNotes() {
	return manualNotes;
    }

    public void setManualNotes(String manualNotes) {
	this.manualNotes = Boolean.valueOf(manualNotes);
    }
}
