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
 * Creation date: 01-24-2008
 * 
 * XDoclet definition:
 * @struts.form name="warehouseCodeForm"
 */
public class ItemCodeForm extends ActionForm {
	/*
	 * Generated Methods
	 */
private String itemName;
private String buttonValue;
	public String getButtonValue() {
	return buttonValue;
}

public void setButtonValue(String buttonValue) {
	this.buttonValue = buttonValue;
}




	/**
 * @return the itemName
 */
public String getItemName() {
	return itemName;
}

/**
 * @param itemName the itemName to set
 */
public void setItemName(String itemName) {
	this.itemName = itemName;
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