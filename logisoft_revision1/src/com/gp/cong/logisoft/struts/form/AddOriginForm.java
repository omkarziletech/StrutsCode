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
 * Creation date: 12-15-2007
 * 
 * XDoclet definition:
 * @struts.form name="addOriginForm"
 */
public class AddOriginForm extends ActionForm {
	/*
	 * Generated Methods
	 */

	/** 
	 * Method validate
	 * @param mapping
	 * @param request
	 * @return ActionErrors
	 */
	private String originCode;
	private String originName;
	private String ttToPol;
	private String cutOffDayOfWeek;
	private String cutOffTime;
	private String buttonValue;
	public String getButtonValue() {
		return buttonValue;
	}
	public void setButtonValue(String buttonValue) {
		this.buttonValue = buttonValue;
	}
	public String getCutOffDayOfWeek() {
		return cutOffDayOfWeek;
	}
	public void setCutOffDayOfWeek(String cutOffDayOfWeek) {
		this.cutOffDayOfWeek = cutOffDayOfWeek;
	}
	public String getCutOffTime() {
		return cutOffTime;
	}
	public void setCutOffTime(String cutOffTime) {
		this.cutOffTime = cutOffTime;
	}
	public String getOriginCode() {
		return originCode;
	}
	public void setOriginCode(String originCode) {
		this.originCode = originCode;
	}
	public String getOriginName() {
		return originName;
	}
	public void setOriginName(String originName) {
		this.originName = originName;
	}
	public String getTtToPol() {
		return ttToPol;
	}
	public void setTtToPol(String ttToPol) {
		this.ttToPol = ttToPol;
	}
	
	
}