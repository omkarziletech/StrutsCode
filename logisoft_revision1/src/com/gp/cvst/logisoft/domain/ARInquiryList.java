package com.gp.cvst.logisoft.domain;

import java.io.Serializable;
import java.sql.Date;


public class ARInquiryList implements Serializable {

	
	
	private String billTermas;
	private String invoiceDate;
	private Integer invoiceBill;
	private Date paymentDate;
	private String paymentDays;
	private String ageInDays;
	private char creditHolds;
	private String transactionId;
	private Integer trnNo;
	
	  private String customerName;
      private Integer customerNumber;
      private String invoiceFromDate;
      private String invoiceToDate;
      private Integer invoiceNo=0;
      private Integer invoiceAmt=0;
      private Integer checkNo=0;
      private Integer checkAmt=0;
	
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getAgeInDays() {
		return ageInDays;
	}
	public void setAgeInDays(String ageInDays) {
		this.ageInDays = ageInDays;
	}
	public String getBillTermas() {
		return billTermas;
	}
	public void setBillTermas(String billTermas) {
		this.billTermas = billTermas;
	}
	public char getCreditHolds() {
		return creditHolds;
	}
	public Integer getInvoiceBill() {
		return invoiceBill;
	}
	public void setInvoiceBill(Integer invoiceBill) {
		this.invoiceBill = invoiceBill;
	}
	public String getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	
	public String getPaymentDays() {
		return paymentDays;
	}
	public void setPaymentDays(String paymentDays) {
		this.paymentDays = paymentDays;
	}
	
	public Integer getTrnNo() {
		return trnNo;
	}
	public void setTrnNo(Integer trnNo) {
		this.trnNo = trnNo;
	}
	public Date getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}
	/*public Integer getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(Integer transactionId) {
		this.transactionId = transactionId;
	}*/
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
	public Integer getCustomerNumber() {
		return customerNumber;
	}
	public void setCustomerNumber(Integer customerNumber) {
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
