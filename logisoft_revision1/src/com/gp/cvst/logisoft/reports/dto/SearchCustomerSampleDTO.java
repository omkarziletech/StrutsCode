package com.gp.cvst.logisoft.reports.dto;

import java.util.List;

import com.gp.cvst.logisoft.struts.form.AgingReportForm;

public class SearchCustomerSampleDTO {

	private String acctNo;
	private String acctName;
	private String invoiceNum;
	private String invoiceDate;
	private String balance;
	private String aging;
	private String billofladingno;
	private String custAddress;
	private String phone;
	private String fax;
	private String city;
	private String invoiceNo;
	private String amount;
	
	private List addressList;
	private List searchCustomerList;
	private String contextPath;
	private String fileName;
	private Integer transId;
	private AgingReportForm agingReportForm;
	public AgingReportForm getAgingReportForm() {
		return agingReportForm;
	}
	public void setAgingReportForm(AgingReportForm agingReportForm) {
		this.agingReportForm = agingReportForm;
	}
	public Integer getTransId() {
		return transId;
	}
	public void setTransId(Integer transId) {
		this.transId = transId;
	}
	public String getContextPath() {
		return contextPath;
	}
	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getAcctName() {
		return acctName;
	}
	public void setAcctName(String acctName) {
		this.acctName = acctName;
	}
	public String getAcctNo() {
		return acctNo;
	}
	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}
	public String getAging() {
		return aging;
	}
	public void setAging(String aging) {
		this.aging = aging;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public String getInvoiceNum() {
		return invoiceNum;
	}
	public void setInvoiceNum(String invoiceNum) {
		this.invoiceNum = invoiceNum;
	}
	public String getBillofladingno() {
		return billofladingno;
	}
	public void setBillofladingno(String billofladingno) {
		this.billofladingno = billofladingno;
	}
	public String getCustAddress() {
		return custAddress;
	}
	public void setCustAddress(String custAddress) {
		this.custAddress = custAddress;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public List getAddressList() {
		return addressList;
	}
	public void setAddressList(List addressList) {
		this.addressList = addressList;
	}
	public List getSearchCustomerList() {
		return searchCustomerList;
	}
	public void setSearchCustomerList(List searchCustomerList) {
		this.searchCustomerList = searchCustomerList;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	
}
