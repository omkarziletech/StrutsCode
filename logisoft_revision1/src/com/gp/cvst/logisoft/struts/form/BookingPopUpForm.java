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
 * Creation date: 09-04-2008
 * 
 * XDoclet definition:
 * @struts.form name="bookingPopUpForm"
 */
public class BookingPopUpForm extends ActionForm {

private String[] sellrate;
private String[] buyRate;
private String[] comments;
private String buttonValue;
private String[]  chargecode;
	
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


	public String getButtonValue() {
		return buttonValue;
	}

	public void setButtonValue(String buttonValue) {
		
		this.buttonValue = buttonValue;
	}

	public String[] getBuyRate() {
		return buyRate;
	}

	public void setBuyRate(String[] buyRate) {
		this.buyRate = buyRate;
	}

	public String[] getComments() {
		return comments;
	}

	public void setComments(String[] comments) {
		this.comments = comments;
	}

	public String[] getSellrate() {
		return sellrate;
	}

	public void setSellrate(String[] sellrate) {
		this.sellrate = sellrate;
	}

	public String[] getChargecode() {
		return chargecode;
	}

	public void setChargecode(String[] chargecode) {
		this.chargecode = chargecode;
	}

	
}