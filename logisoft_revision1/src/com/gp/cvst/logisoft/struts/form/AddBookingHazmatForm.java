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
 * Creation date: 05-25-2009
 * 
 * XDoclet definition:
 * @struts.form name="addBookingHazmatForm"
 */
public class AddBookingHazmatForm extends ActionForm {
	/*
	 * Generated Methods
	 */
private String[] bookcheckbox;
private String buttonValue;
private String containerId;
private String bolid;
	public String getBolid() {
	return bolid;
}

public void setBolid(String bolid) {
	this.bolid = bolid;
}

public String getContainerId() {
	return containerId;
}

public void setContainerId(String containerId) {
	this.containerId = containerId;
}

	public String getButtonValue() {
	return buttonValue;
}

public void setButtonValue(String buttonValue) {
	this.buttonValue = buttonValue;
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

	public String[] getBookcheckbox() {
		return bookcheckbox;
	}

	public void setBookcheckbox(String[] bookcheckbox) {
		this.bookcheckbox = bookcheckbox;
	}

	

	
}