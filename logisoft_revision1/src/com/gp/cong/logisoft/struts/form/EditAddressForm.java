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
 * Creation date: 07-25-2008
 * 
 * XDoclet definition:
 * @struts.form name="editAddressForm"
 */
public class EditAddressForm extends ActionForm {
	/*
	 * Generated Methods
	 */
private String coName;
private String phone;
private String address1;
private String extension;
private String city;
private String state;
private String country;
private String zip;
private String contactName;
private String fax;
private String buttonValue;
private String primary;
private String email1;
private String email2;

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

	public String getPrimary() {
	return primary;
}

public void setPrimary(String primary) {
	this.primary = primary;
}

	public String getAddress1() {
	return address1;
}

public void setAddress1(String address1) {
	this.address1 = address1;
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

public String getExtension() {
	return extension;
}

public void setExtension(String extension) {
	this.extension = extension;
}

public String getFax() {
	return fax;
}

public void setFax(String fax) {
	this.fax = fax;
}

public String getPhone() {
	return phone;
}

public void setPhone(String phone) {
	this.phone = phone;
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