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
 * Creation date: 02-11-2008
 * 
 * XDoclet definition:
 * @struts.form name="relayCodeForm"
 */
public class RelayCodeForm extends ActionForm {
	/*
	 * Generated Methods
	 */

	/** 
	 * Method validate
	 * @param mapping
	 * @param request
	 * @return ActionErrors
	 */
	private String pol;
	private String polText;
	private String pod;
	private String podText;
	private String buttonValue;
	/**
	 * @return the pod
	 */
	public String getPod() {
		return pod;
	}
	/**
	 * @param pod the pod to set
	 */
	public void setPod(String pod) {
		this.pod = pod;
	}
	/**
	 * @return the podText
	 */
	public String getPodText() {
		return podText;
	}
	/**
	 * @param podText the podText to set
	 */
	public void setPodText(String podText) {
		this.podText = podText;
	}
	/**
	 * @return the pol
	 */
	public String getPol() {
		return pol;
	}
	/**
	 * @param pol the pol to set
	 */
	public void setPol(String pol) {
		this.pol = pol;
	}
	/**
	 * @return the polText
	 */
	public String getPolText() {
		return polText;
	}
	/**
	 * @param polText the polText to set
	 */
	public void setPolText(String polText) {
		this.polText = polText;
	}
	/**
	 * @return the buttonValue
	 */
	public String getButtonValue() {
		return buttonValue;
	}
	/**
	 * @param buttonValue the buttonValue to set
	 */
	public void setButtonValue(String buttonValue) {
		this.buttonValue = buttonValue;
	}
}