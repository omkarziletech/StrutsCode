/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.Ports;

/** 
 * MyEclipse Struts
 * Creation date: 01-08-2008
 * 
 * XDoclet definition:
 * @struts.form name="consigneeInformationForm"
 */
public class ConsigneeInformationForm extends ActionForm {
	/*
	 * Generated Methods
	 */

	/** 
	 * Method validate
	 * @param mapping
	 * @param request
	 * @return ActionErrors
	 */
	 
	private String taxExempt;
	private String fedId;
	private String commodityId;
	private String portId;
	private String maxDaysBetVisits;
	private String goalAcct;
	private String salesPCode;
	private String notifyCity;
	private String notifyState;
	private String notifyCountry;
	private String zipCode;
	private String insurance;
	private String notifyParty;
	private String userName;
	private String password;
	private String specialRemarks;
	private String buttonValue;
	/**
	 * @return the commodityId
	 */
	public String getCommodityId() {
		return commodityId;
	}
	/**
	 * @param commodityId the commodityId to set
	 */
	public void setCommodityId(String commodityId) {
		this.commodityId = commodityId;
	}
	/**
	 * @return the fedId
	 */
	public String getFedId() {
		return fedId;
	}
	/**
	 * @param fedId the fedId to set
	 */
	public void setFedId(String fedId) {
		this.fedId = fedId;
	}
	/**
	 * @return the goalAcct
	 */
	public String getGoalAcct() {
		return goalAcct;
	}
	/**
	 * @param goalAcct the goalAcct to set
	 */
	public void setGoalAcct(String goalAcct) {
		this.goalAcct = goalAcct;
	}
	/**
	 * @return the insurance
	 */
	public String getInsurance() {
		return insurance;
	}
	/**
	 * @param insurance the insurance to set
	 */
	public void setInsurance(String insurance) {
		this.insurance = insurance;
	}
	/**
	 * @return the maxDaysBetVisits
	 */
	public String getMaxDaysBetVisits() {
		return maxDaysBetVisits;
	}
	/**
	 * @param maxDaysBetVisits the maxDaysBetVisits to set
	 */
	public void setMaxDaysBetVisits(String maxDaysBetVisits) {
		this.maxDaysBetVisits = maxDaysBetVisits;
	}
	/**
	 * @return the notifyCity
	 */
	public String getNotifyCity() {
		return notifyCity;
	}
	/**
	 * @param notifyCity the notifyCity to set
	 */
	public void setNotifyCity(String notifyCity) {
		this.notifyCity = notifyCity;
	}
	/**
	 * @return the notifyCountry
	 */
	public String getNotifyCountry() {
		return notifyCountry;
	}
	/**
	 * @param notifyCountry the notifyCountry to set
	 */
	public void setNotifyCountry(String notifyCountry) {
		this.notifyCountry = notifyCountry;
	}
	/**
	 * @return the notifyParty
	 */
	public String getNotifyParty() {
		return notifyParty;
	}
	/**
	 * @param notifyParty the notifyParty to set
	 */
	public void setNotifyParty(String notifyParty) {
		this.notifyParty = notifyParty;
	}
	/**
	 * @return the notifyState
	 */
	public String getNotifyState() {
		return notifyState;
	}
	/**
	 * @param notifyState the notifyState to set
	 */
	public void setNotifyState(String notifyState) {
		this.notifyState = notifyState;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the portId
	 */
	public String getPortId() {
		return portId;
	}
	/**
	 * @param portId the portId to set
	 */
	public void setPortId(String portId) {
		this.portId = portId;
	}
	/**
	 * @return the salesPCode
	 */
	public String getSalesPCode() {
		return salesPCode;
	}
	/**
	 * @param salesPCode the salesPCode to set
	 */
	public void setSalesPCode(String salesPCode) {
		this.salesPCode = salesPCode;
	}
	/**
	 * @return the specialRemarks
	 */
	public String getSpecialRemarks() {
		return specialRemarks;
	}
	/**
	 * @param specialRemarks the specialRemarks to set
	 */
	public void setSpecialRemarks(String specialRemarks) {
		this.specialRemarks = specialRemarks;
	}
	/**
	 * @return the taxExempt
	 */
	public String getTaxExempt() {
		return taxExempt;
	}
	/**
	 * @param taxExempt the taxExempt to set
	 */
	public void setTaxExempt(String taxExempt) {
		this.taxExempt = taxExempt;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the zipCode
	 */
	public String getZipCode() {
		return zipCode;
	}
	/**
	 * @param zipCode the zipCode to set
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	/**
	 * @return the buttonValue
	 */
	public String getButtonValue() {
		return buttonValue;
	}
	/**
	 * @param buttonValue the buttonValue to set
	 */
	public void setButtonValue(String buttonValue) {
		this.buttonValue = buttonValue;
	}
	
}