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
 * Creation date: 01-09-2008
 * 
 * XDoclet definition:
 * @struts.form name="editCustomForm"
 */
public class EditCustomForm extends ActionForm {
	/*
	 * Generated Methods
	 */
  private String buttonValue;
  private String name;
  private String accountPrefix;
  private String accountType;
  private String accountno;
  
  
  private String master;
  
  
	public String getMaster() {
	return master;
}

public void setMaster(String master) {
	this.master = master;
}

	public String getAccountno() {
	return accountno;
}

public void setAccountno(String accountno) {
	this.accountno = accountno;
}

	public String getAccountType() {
	return accountType;
}

public void setAccountType(String accountType) {
	this.accountType = accountType;
}

	public String getAccountPrefix() {
	return accountPrefix;
}

public void setAccountPrefix(String accountPrefix) {
	this.accountPrefix = accountPrefix;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
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
}