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
 * Creation date: 11-05-2008
 * 
 * XDoclet definition:
 * @struts.form name="blClauseForm"
 */
public class BlClauseForm extends ActionForm {
	/*
	 * Generated Methods
	 */
private String[] description;
private String[] text;
private String buttonValue;
private String bol;
	public String getBol() {
	return bol;
}

public void setBol(String bol) {
	this.bol = bol;
}

	public String getButtonValue() {
	return buttonValue;
}

public void setButtonValue(String buttonValue) {
	this.buttonValue = buttonValue;
}

public String[] getDescription() {
	return description;
}

public void setDescription(String[] description) {
	this.description = description;
}

public String[] getText() {
	return text;
}

public void setText(String[] text) {
	this.text = text;
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