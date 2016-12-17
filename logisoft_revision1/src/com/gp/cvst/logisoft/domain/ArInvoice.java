package com.gp.cvst.logisoft.domain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.gp.cvst.logisoft.struts.form.ARInvoiceForm;

/**
 * ArInvoice entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ArInvoice implements java.io.Serializable {

	// Fields

	private Integer id;
	private String customerName;
	private String accountNumber;
	private String customerType;
	private String contactName;
	private String address;
	private String phoneNumber;
	private String invoiceNumber;
	private String blDrNumber;
	private Date date;
	private String term;
	private Date dueDate;
	private String notes;
	private String description;
	private String glAccount;
	private String manifest;
	private String companyAddress;
	private String companyCity;
	private String companyState;
	private String companyZip;
	private String companyPhone;
	private String companyFax;
	private String invoiceCity;
	private String state;
	private String zip;
	private String companyName;
	private String termForPrint;
	private String creditTermName;
	private Set arinvoiceCharges = new HashSet(0);
	// Constructors

	
	/** default constructor */
	public ArInvoice() {
	}

	/** full constructor */
	public ArInvoice(String customerName, String accountNumber,
			String customerType, String contactName, String address,
			String phoneNumber, String invoiceNumber, String blDrNumber,
			Date date, String term, Date dueDate, String notes,
			Set arinvoiceChargeses) {
		this.customerName = customerName;
		this.accountNumber = accountNumber;
		this.customerType = customerType;
		this.contactName = contactName;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.invoiceNumber = invoiceNumber;
		this.blDrNumber = blDrNumber;
		this.date = date;
		this.term = term;
		this.dueDate = dueDate;
		this.notes = notes;
		this.arinvoiceCharges = arinvoiceChargeses;
	}
	public ArInvoice(ARInvoiceForm aRInvoiceForm)throws Exception{
		updateValues(aRInvoiceForm);
	}
	
	public void updateValues(ARInvoiceForm aRInvoiceForm)throws Exception{
		address=aRInvoiceForm.getAddress();
		accountNumber = aRInvoiceForm.getAccountNumber();
		if(aRInvoiceForm.getArInvoiceId()!=null && !aRInvoiceForm.getArInvoiceId().equals("")){
		id=new Integer(aRInvoiceForm.getArInvoiceId());
		}
		blDrNumber=aRInvoiceForm.getBl_drNumber();
		customerName= aRInvoiceForm.getCusName();
		customerType = aRInvoiceForm.getArCustomertype();
		contactName = aRInvoiceForm.getContactName();
		phoneNumber = aRInvoiceForm.getPhoneNumber();
		invoiceNumber = aRInvoiceForm.getInvoiceNumber();
		blDrNumber = aRInvoiceForm.getBl_drNumber();
		term = aRInvoiceForm.getTerm();
		notes = aRInvoiceForm.getNotes();
		if(aRInvoiceForm.getDate()!=null && aRInvoiceForm.getDate()!="") {
			Date javaDate=null;
				SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yyyy");
				javaDate=sdf.parse(aRInvoiceForm.getDate());
			date = javaDate;
		}
		if(aRInvoiceForm.getDueDate()!=null && aRInvoiceForm.getDueDate()!="") {
			Date javaDate=null;
				SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yyyy");
				javaDate=sdf.parse(aRInvoiceForm.getDueDate());
			dueDate = javaDate;
		}
		
	}
	public void getCustometDetails(CustAddress custAddress){
		customerName = custAddress.getAcctName();
		accountNumber= custAddress.getAcctNo();
		customerType = custAddress.getAcctType();
		address = custAddress.getAddress1();
		contactName = custAddress.getContactName();
		phoneNumber = custAddress.getPhone();
	}
	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCustomerName() {
		return this.customerName;
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
		return this.customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getContactName() {
		return this.contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getInvoiceNumber() {
		return this.invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getBlDrNumber() {
		return this.blDrNumber;
	}

	public void setBlDrNumber(String blDrNumber) {
		this.blDrNumber = blDrNumber;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTerm() {
		return this.term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public Date getDueDate() {
		return this.dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public String getNotes() {
		return this.notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Set getArinvoiceCharges() {
		return this.arinvoiceCharges;
	}

	public void setArinvoiceCharges(Set arinvoiceChargeses) {
		this.arinvoiceCharges = arinvoiceChargeses;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGlAccount() {
		return glAccount;
	}

	public void setGlAccount(String glAccount) {
		this.glAccount = glAccount;
	}

	public String getManifest() {
		return manifest;
	}

	public void setManifest(String manifest) {
		this.manifest = manifest;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public String getCompanyCity() {
		return companyCity;
	}

	public void setCompanyCity(String companyCity) {
		this.companyCity = companyCity;
	}

	public String getCompanyState() {
		return companyState;
	}

	public void setCompanyState(String companyState) {
		this.companyState = companyState;
	}

	public String getCompanyZip() {
		return companyZip;
	}

	public void setCompanyZip(String companyZip) {
		this.companyZip = companyZip;
	}

	public String getCompanyPhone() {
		return companyPhone;
	}

	public void setCompanyPhone(String companyPhone) {
		this.companyPhone = companyPhone;
	}

	public String getCompanyFax() {
		return companyFax;
	}

	public void setCompanyFax(String companyFax) {
		this.companyFax = companyFax;
	}

	public String getInvoiceCity() {
		return invoiceCity;
	}

	public void setInvoiceCity(String invoiceCity) {
		this.invoiceCity = invoiceCity;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCreditTermName() {
		return creditTermName;
	}

	public void setCreditTermName(String creditTermName) {
		this.creditTermName = creditTermName;
	}

	public String getTermForPrint() {
		return termForPrint;
	}

	public void setTermForPrint(String termForPrint) {
		this.termForPrint = termForPrint;
	}

}