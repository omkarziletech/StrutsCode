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
 * Creation date: 09-19-2008
 * 
 * XDoclet definition:
 * @struts.form name="EditvendorForm"
 */
public class EditvendorForm extends ActionForm {
	
	
	private String legalname;
	private String dba;
	private String tin;
	private String wfile;
	private String apname;
	private String phone;
	private String fax;
	private String email;
	private String web;
	private String eamanager;
	private String credit;
	private String climit;
	private String cterms;
	private String baccount;
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
	private String deactivate;
	
	
	
	
	public String getAba() {
		return aba;
	}

	public void setAba(String aba) {
		this.aba = aba;
	}

	public String getApname() {
		return apname;
	}

	public void setApname(String apname) {
		this.apname = apname;
	}

	public String getBaccount() {
		return baccount;
	}

	public void setBaccount(String baccount) {
		this.baccount = baccount;
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

	public String getButtonValue() {
		return buttonValue;
	}

	public void setButtonValue(String buttonValue) {
		this.buttonValue = buttonValue;
	}

	public String getClimit() {
		return climit;
	}

	public void setClimit(String climit) {
		this.climit = climit;
	}

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
	}

	public String getCterms() {
		return cterms;
	}

	public void setCterms(String cterms) {
		this.cterms = cterms;
	}

	public String getDba() {
		return dba;
	}

	public void setDba(String dba) {
		this.dba = dba;
	}

	public String getDeactivate() {
		return deactivate;
	}

	public void setDeactivate(String deactivate) {
		this.deactivate = deactivate;
	}

	public String getEamanager() {
		return eamanager;
	}

	public void setEamanager(String eamanager) {
		this.eamanager = eamanager;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getLegalname() {
		return legalname;
	}

	public void setLegalname(String legalname) {
		this.legalname = legalname;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public String getTin() {
		return tin;
	}

	public void setTin(String tin) {
		this.tin = tin;
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

	public String getWeb() {
		return web;
	}

	public void setWeb(String web) {
		this.web = web;
	}

	public String getWfile() {
		return wfile;
	}

	public void setWfile(String wfile) {
		this.wfile = wfile;
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