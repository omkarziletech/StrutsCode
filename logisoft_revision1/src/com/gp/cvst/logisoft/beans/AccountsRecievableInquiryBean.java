package com.gp.cvst.logisoft.beans;

import java.io.Serializable;

public class AccountsRecievableInquiryBean implements Serializable{
	  private String customerName;
      private String customerNumber;
      private String invoiceFromDate;
      private String invoiceToDate;
      private Integer invoiceNo=0;
      private Integer invoiceAmt=0;
      private Integer checkNo=0;
      private Integer checkAmt=0;
      
      
	
	public Integer getCheckAmt() {
		return checkAmt;
	}
	public void setCheckAmt(Integer checkAmt) {
		this.checkAmt = checkAmt;
	}
	public Integer getCheckNo() {
		return checkNo;
	}
	public void setCheckNo(Integer checkNo) {
		this.checkNo = checkNo;
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
	public Integer getInvoiceAmt() {
		return invoiceAmt;
	}
	public void setInvoiceAmt(Integer invoiceAmt) {
		this.invoiceAmt = invoiceAmt;
	}
	public String getInvoiceFromDate() {
		return invoiceFromDate;
	}
	public void setInvoiceFromDate(String invoiceFromDate) {
		this.invoiceFromDate = invoiceFromDate;
	}
	public Integer getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(Integer invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getInvoiceToDate() {
		return invoiceToDate;
	}
	public void setInvoiceToDate(String invoiceToDate) {
		this.invoiceToDate = invoiceToDate;
	}

}
