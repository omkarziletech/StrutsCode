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
 * Creation date: 03-06-2008
 * 
 * XDoclet definition:
 * @struts.form name="addAirRatesForm"
 */
public class AddAirRatesForm extends ActionForm {
	/*
	 * Generated Methods
	 */
	private String buttonValue;
	private String terminalNumber;
	private String terminalName;
	private String destSheduleNumber;
	private String destAirportname;
	private String comCode;
	
	private String comDescription;
	public String getButtonValue() {
		return buttonValue;
	}

	public void setButtonValue(String buttonValue) {
		this.buttonValue = buttonValue;
	}

	public String getComCode() {
		return comCode;
	}

	public void setComCode(String comCode) {
		this.comCode = comCode;
	}

	public String getComDescription() {
		return comDescription;
	}

	public void setComDescription(String comDescription) {
		this.comDescription = comDescription;
	}

	public String getDestAirportname() {
		return destAirportname;
	}

	public void setDestAirportname(String destAirportname) {
		this.destAirportname = destAirportname;
	}

	public String getDestSheduleNumber() {
		return destSheduleNumber;
	}

	public void setDestSheduleNumber(String destSheduleNumber) {
		this.destSheduleNumber = destSheduleNumber;
	}

	public String getTerminalName() {
		return terminalName;
	}

	public void setTerminalName(String terminalName) {
		this.terminalName = terminalName;
	}

	public String getTerminalNumber() {
		return terminalNumber;
	}

	public void setTerminalNumber(String terminalNumber) {
		this.terminalNumber = terminalNumber;
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