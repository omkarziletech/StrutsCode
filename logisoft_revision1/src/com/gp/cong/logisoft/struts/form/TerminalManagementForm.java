/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.gp.cong.logisoft.domain.RefTerminal;

/** 
 * MyEclipse Struts
 * Creation date: 11-30-2007
 * 
 * XDoclet definition:
 * @struts.form name="terminalManagementForm"
 */
public class TerminalManagementForm extends ActionForm {
	/*
	 * Generated Methods
	 */
  private String terminalId;
  private String terminalName;
  private String city;
  private String terminalType;
  private String buttonValue;
  private String match;
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

public void setTerminalId(String terminalId) {
	this.terminalId = terminalId;
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	

	public String getTerminalName() {
		return terminalName;
	}

	public void setTerminalName(String terminalName) {
		this.terminalName = terminalName;
	}

	public String getTerminalType() {
		return terminalType;
	}

	public void setTerminalType(String terminalType) {
		this.terminalType = terminalType;
	}

	public String getTerminalId() {
		return terminalId;
	}

	
}