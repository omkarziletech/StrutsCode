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
 * Creation date: 06-24-2008
 * 
 * XDoclet definition:
 * @struts.form name="searchSSLineForm"
 */
public class SearchSSLineForm extends ActionForm {
	private String ssLineNumber;
	private String ssLineName;
	private String buttonValue;
	private String index="";
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public String getButtonValue() {
		return buttonValue;
	}
	public void setButtonValue(String buttonValue) {
		this.buttonValue = buttonValue;
	}
	public String getSsLineName() {
		return ssLineName;
	}
	public void setSsLineName(String ssLineName) {
		this.ssLineName = ssLineName;
	}
	public String getSsLineNumber() {
		return ssLineNumber;
	}
	public void setSsLineNumber(String ssLineNumber) {
		this.ssLineNumber = ssLineNumber;
	}
}