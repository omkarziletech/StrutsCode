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
 * Creation date: 10-01-2008
 * 
 * XDoclet definition:
 * @struts.form name="paymentPopupForm"
 */
public class PaymentPopupForm extends ActionForm {
	
	private String paymethod;
	private String bankname;
	private String baddr;
	private String vacctname;
	private String vacctno;
	private String aba;
	private String swift;
	private String remail;
	private String rfax;
	private String payemail;
	private String buttonValue;
	

	public String getButtonValue() {
		return buttonValue;
	}

	public void setButtonValue(String buttonValue) {
		this.buttonValue = buttonValue;
	}

	public String getAba() {
		return aba;
	}

	public void setAba(String aba) {
		this.aba = aba;
	}

	public String getBaddr() {
		return baddr;
	}

	public void setBaddr(String baddr) {
		this.baddr = baddr;
	}

	public String getBankname() {
		return bankname;
	}

	public void setBankname(String bankname) {
		this.bankname = bankname;
	}

	public String getPayemail() {
		return payemail;
	}

	public void setPayemail(String payemail) {
		this.payemail = payemail;
	}

	public String getPaymethod() {
		return paymethod;
	}

	public void setPaymethod(String paymethod) {
		this.paymethod = paymethod;
	}

	public String getRemail() {
		return remail;
	}

	public void setRemail(String remail) {
		this.remail = remail;
	}

	public String getRfax() {
		return rfax;
	}

	public void setRfax(String rfax) {
		this.rfax = rfax;
	}

	public String getSwift() {
		return swift;
	}

	public void setSwift(String swift) {
		this.swift = swift;
	}

	public String getVacctname() {
		return vacctname;
	}

	public void setVacctname(String vacctname) {
		this.vacctname = vacctname;
	}

	public String getVacctno() {
		return vacctno;
	}

	public void setVacctno(String vacctno) {
		this.vacctno = vacctno;
	}

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