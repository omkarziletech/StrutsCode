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
 * Creation date: 05-08-2008
 * 
 * XDoclet definition:
 * @struts.form name="addForm"
 */
public class addForm extends ActionForm {
	/*
	 * Generated Methods
	 */

	/** 
	 * Method validate
	 * @param mapping
	 * @param request
	 * @return ActionErrors
	 */
	private String segcodeId;
	private String id;
	private String buttonValue;
	private String addcode;
	private String adddesc;
	private String asid;
	private String codeid;
	private String segcode;
	private String seglength;
	
	public String getSeglength() {
		return seglength;
	}

	public void setSeglength(String seglength) {
		this.seglength = seglength;
	}

	public String getSegcode() {
		return segcode;
	}

	public void setSegcode(String segcode) {
		this.segcode = segcode;
	}

	public String getAddcode() {
		return addcode;
	}

	public void setAddcode(String addcode) {
		this.addcode = addcode;
	}

	public String getAdddesc() {
		return adddesc;
	}

	public void setAdddesc(String adddesc) {
		this.adddesc = adddesc;
	}

	public String getCodeid() {
		return codeid;
	}

	public void setCodeid(String codeid) {
		this.codeid = codeid;
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

	public String getButtonValue() {
		return buttonValue;
	}

	public void setButtonValue(String buttonValue) {
		this.buttonValue = buttonValue;
	}

	public String getAsid() {
		return asid;
	}

	public void setAsid(String asid) {
		this.asid = asid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSegcodeId() {
		return segcodeId;
	}

	public void setSegcodeId(String segcodeId) {
		this.segcodeId = segcodeId;
	}
	
}