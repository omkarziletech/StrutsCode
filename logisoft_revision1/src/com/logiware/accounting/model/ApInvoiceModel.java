package com.logiware.accounting.model;

/**
 *
 * @author Lakshmi Naryanan
 */
public class ApInvoiceModel {

    private String id;
    private String vendorName;
    private String vendorNumber;
    private String invoiceNumber;
    private String invoiceDate;
    private String invoiceAmount;
    private String status;
    private boolean exactMatch;

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
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

    public boolean isExactMatch() {
	return exactMatch;
    }

    public void setExactMatch(String exactMatch) {
	this.exactMatch = Boolean.valueOf(exactMatch);
    }
}
