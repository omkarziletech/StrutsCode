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
 * Creation date: 12-26-2007
 * 
 * XDoclet definition:
 * @struts.form name="addConfigForm"
 */
public class AddConfigForm extends ActionForm {
	/*
	 * Generated Methods
	 */
    private String firstName;
    private String lastName;
    private String position;
    private String phone;
    private String fax;
    private String email;
    private String comment;
    private String codea;
    private String codeb;
    private String codee;
    private String codef;
    private String codec;
    private String codeg;
    private String coded;
    private String codeh;
    private String codei;
    private String buttonValue;
    private String extension;
	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
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

	public String getCodea() {
		return codea;
	}

	public void setCodea(String codea) {
		this.codea = codea;
	}

	public String getCodeb() {
		return codeb;
	}

	public void setCodeb(String codeb) {
		this.codeb = codeb;
	}

	public String getCodec() {
		return codec;
	}

	public void setCodec(String codec) {
		this.codec = codec;
	}

	public String getCoded() {
		return coded;
	}

	public void setCoded(String coded) {
		this.coded = coded;
	}

	public String getCodee() {
		return codee;
	}

	public void setCodee(String codee) {
		this.codee = codee;
	}

	public String getCodef() {
		return codef;
	}

	public void setCodef(String codef) {
		this.codef = codef;
	}

	public String getCodeg() {
		return codeg;
	}

	public void setCodeg(String codeg) {
		this.codeg = codeg;
	}

	public String getCodeh() {
		return codeh;
	}

	public void setCodeh(String codeh) {
		this.codeh = codeh;
	}

	public String getCodei() {
		return codei;
	}

	public void setCodei(String codei) {
		this.codei = codei;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}
}