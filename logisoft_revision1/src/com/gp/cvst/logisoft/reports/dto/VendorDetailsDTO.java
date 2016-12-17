/**
 * 
 */
package com.gp.cvst.logisoft.reports.dto;

import com.gp.cvst.logisoft.struts.form.AccountPayableForm;

/**
 * @author Administrator
 *
 */
public class VendorDetailsDTO {
	
	private String vendorName;
	private String vendorNumber;
	private String fromDate;
	private String toDate;
	private String invoiceNumber;
	private String invoiceAmount;
	private String showOnlyAR;
	private String showMyAP;
	private String showParentChild;
	private String voyage;
	private String amountPaid;
	private String netAmount;
	private String billOfLadding;
	private String checkType;
	private String showHold;
	private String showOnlyMyAPEntries;

	
	public String getShowHold() {
		return showHold;
	}

	public void setShowHold(String showHold) {
		this.showHold = showHold;
	}

	public String getCheckType() {
		return checkType;
	}

	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}

	public String getBillOfLadding() {
		return billOfLadding;
	}

	public void setBillOfLadding(String billOfLadding) {
		this.billOfLadding = billOfLadding;
	}

	public VendorDetailsDTO(){}
	
	public VendorDetailsDTO(AccountPayableForm accountPayableForm) {
		this.vendorNumber = accountPayableForm.getVendornumber();
		this.fromDate = accountPayableForm.getDatefrom();
		this.toDate = accountPayableForm.getDateto();
		this.invoiceNumber = accountPayableForm.getInvoicenumber();
		this.invoiceAmount = accountPayableForm.getInvoiceamount();
		this.showOnlyAR = accountPayableForm.getOnlyAR();
		this.showMyAP = accountPayableForm.getOnlyAP();
		this.showParentChild = accountPayableForm.getChparent();
		this.voyage = accountPayableForm.getVoyage();
		this.billOfLadding = accountPayableForm.getBillofladding();
		this.checkType = accountPayableForm.getCheckType();
		this.showHold = accountPayableForm.getShowHold();
		this.showOnlyMyAPEntries = accountPayableForm.getShowOnlyMyAPEntries();
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
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public String getInvoiceAmount() {
		return invoiceAmount;
	}
	public void setInvoiceAmount(String invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}
	public String getShowOnlyAR() {
		return showOnlyAR;
	}
	public void setShowOnlyAR(String showOnlyAR) {
		this.showOnlyAR = showOnlyAR;
	}
	public String getShowMyAP() {
		return showMyAP;
	}
	public void setShowMyAP(String showMyAP) {
		this.showMyAP = showMyAP;
	}
	public String getShowParentChild() {
		return showParentChild;
	}
	public void setShowParentChild(String showParentChild) {
		this.showParentChild = showParentChild;
	}
	public String getVoyage() {
		return voyage;
	}
	public void setVoyage(String voyage) {
		this.voyage = voyage;
	}
	public String getAmountPaid() {
		return amountPaid;
	}
	public void setAmountPaid(String amountPaid) {
		this.amountPaid = amountPaid;
	}
	public String getNetAmount() {
		return netAmount;
	}
	public void setNetAmount(String netAmount) {
		this.netAmount = netAmount;
	}

	public String getShowOnlyMyAPEntries() {
		return showOnlyMyAPEntries;
	}

	public void setShowOnlyMyAPEntries(String showOnlyMyAPEntries) {
		this.showOnlyMyAPEntries = showOnlyMyAPEntries;
	}
	

}
