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
 * Creation date: 03-13-2008
 * 
 * XDoclet definition:
 * @struts.form name="claimDetailsForm"
 */
public class ClaimDetailsForm extends ActionForm {
	/*
	 * Generated Methods
	 */
private String masterAwb1;
private String masterAwb2;
private String masterAwb3;
private String portNo;
private String txtcal;
private String hazardous;
private String buttonValue;
public String getButtonValue() {
	return buttonValue;
}

public void setButtonValue(String buttonValue) {
	this.buttonValue = buttonValue;
}

public String getTxtcal() {
	return txtcal;
}

public void setTxtcal(String txtcal) {
	this.txtcal = txtcal;
}

public String getHazardous() {
	return hazardous;
}

public void setHazardous(String hazardous) {
	this.hazardous = hazardous;
}

public String getMasterAwb1() {
	return masterAwb1;
}

public void setMasterAwb1(String masterAwb1) {
	this.masterAwb1 = masterAwb1;
}

public String getMasterAwb2() {
	return masterAwb2;
}

public void setMasterAwb2(String masterAwb2) {
	this.masterAwb2 = masterAwb2;
}

public String getMasterAwb3() {
	return masterAwb3;
}

public void setMasterAwb3(String masterAwb3) {
	this.masterAwb3 = masterAwb3;
}

public String getPortNo() {
	return portNo;
}

public void setPortNo(String portNo) {
	this.portNo = portNo;
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