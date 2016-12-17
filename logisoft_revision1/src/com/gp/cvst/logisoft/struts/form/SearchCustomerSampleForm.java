/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cvst.logisoft.struts.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/** 
 * MyEclipse Struts
 * Creation date: 09-04-2008
 * 
 * XDoclet definition:
 * @struts.form name="searchCustomerSample"
 */
public class SearchCustomerSampleForm extends ActionForm {
	/*
	 * Generated Methods
	 */

	private String buttonValue;
	private String customerName;
	private String customerNumber;
	private String accountType1;
	private String accountType2;
	private String accountType3;
	private String collector;
	private String allCustomer="No";
	private String allCollectors="No";
	
	public String getAllCustomer() {
		return allCustomer;
	}

	public void setAllCustomer(String allCustomer) {
		this.allCustomer = allCustomer;
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

	public String getButtonValue() {
		return buttonValue;
	}

	public void setButtonValue(String buttonValue) {
		this.buttonValue = buttonValue;
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

	public String getCollector() {
		return collector;
	}

	public void setCollector(String collector) {
		this.collector = collector;
	}

	public String getAllCollectors() {
		return allCollectors;
	}

	public void setAllCollectors(String allCollectors) {
		this.allCollectors = allCollectors;
	}
}