/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/** 
 * MyEclipse Struts
 * Creation date: 03-11-2008
 * 
 * XDoclet definition:
 * @struts.form name="masterCustomerForm"
 */
public class MasterCustomerForm extends ActionForm {
	/*
	 * Generated Methods
	 */
	 private String accountNo;
     private String name;
     private String coName;
     private String accountType1;
     private String accountType2;
     private String accountType3;
     private String accountType4;
     private String address1;
     private String address2;
     private String country;
     private String city;
     private String state;
     private String phone;
     private String contactName;
     private String zip;
     private String fax;
     private String email1;
     private String email2;
     private String buttonValue;
     private String accountPrefix;
     private String masterAddress;
     private String accountType5;
     private String portName;
     private String schnum;
     
     private String extension;
     
	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getAccountPrefix() {
		return accountPrefix;
	}

	public void setAccountPrefix(String accountPrefix) {
		this.accountPrefix = accountPrefix;
	}

	public String getAccountType1() {
		return accountType1;
	}

	public void setAccountType1(String accountType1) {
		this.accountType1 = accountType1;
	}

	public String getAccountType2() {
		return accountType2;
	}

	public void setAccountType2(String accountType2) {
		this.accountType2 = accountType2;
	}

	public String getAccountType3() {
		return accountType3;
	}

	public void setAccountType3(String accountType3) {
		this.accountType3 = accountType3;
	}

	public String getAccountType4() {
		return accountType4;
	}

	public void setAccountType4(String accountType4) {
		this.accountType4 = accountType4;
	}

	public String getAccountType5() {
		return accountType5;
	}

	public void setAccountType5(String accountType5) {
		this.accountType5 = accountType5;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getButtonValue() {
		return buttonValue;
	}

	public void setButtonValue(String buttonValue) {
		this.buttonValue = buttonValue;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCoName() {
		return coName;
	}

	public void setCoName(String coName) {
		this.coName = coName;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getEmail1() {
		return email1;
	}

	public void setEmail1(String email1) {
		this.email1 = email1;
	}

	public String getEmail2() {
		return email2;
	}

	public void setEmail2(String email2) {
		this.email2 = email2;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getMasterAddress() {
		return masterAddress;
	}

	public void setMasterAddress(String masterAddress) {
		this.masterAddress = masterAddress;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPortName() {
		return portName;
	}

	public void setPortName(String portName) {
		this.portName = portName;
	}

	public String getSchnum() {
		return schnum;
	}

	public void setSchnum(String schnum) {
		this.schnum = schnum;
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

	/** 
	 * Method validate
	 * @param mapping
	 * @param request
	 * @return ActionErrors
	 */
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	/** 
	 * Method reset
	 * @param mapping
	 * @param request
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		// TODO Auto-generated method stub
	}
}