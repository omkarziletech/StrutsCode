/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.ratemangement.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/** 
 * MyEclipse Struts
 * Creation date: 07-31-2008
 * 
 * XDoclet definition:
 * @struts.form name="editUniversalInsuranceForm"
 */
public class EditUniversalInsuranceForm extends ActionForm  {
	private String amount ;
	private String pervalue;
	private String buttonValue;
	private String index;
	
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getPervalue() {
		return pervalue;
	}
	public void setPervalue(String pervalue) {
		this.pervalue = pervalue;
	}
	public String getButtonValue() {
		return buttonValue;
	}
	public void setButtonValue(String buttonValue) {
		this.buttonValue = buttonValue;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	
}