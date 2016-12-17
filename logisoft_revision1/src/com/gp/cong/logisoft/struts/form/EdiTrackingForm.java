package com.gp.cong.logisoft.struts.form;

import org.apache.struts.action.ActionForm;

public class EdiTrackingForm extends ActionForm{

	private String drNumber;
	private String messageType;
	private String ediCompany;
	private String match;
	private String buttonValue;
	public String getDrNumber() {
		return drNumber;
	}
	public void setDrNumber(String drNumber) {
		this.drNumber = drNumber;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public String getEdiCompany() {
		return ediCompany;
	}
	public void setEdiCompany(String ediCompany) {
		this.ediCompany = ediCompany;
	}
	public String getMatch() {
		return match;
	}
	public void setMatch(String match) {
		this.match = match;
	}
	public String getButtonValue() {
		return buttonValue;
	}
	public void setButtonValue(String buttonValue) {
		this.buttonValue = buttonValue;
	}
}
