/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.form;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.gp.cong.logisoft.domain.Genericcodelabels;
import com.gp.cong.logisoft.hibernate.dao.GenericcodelabelsDAO;

/** 
 * MyEclipse Struts
 * Creation date: 02-14-2008
 * 
 * XDoclet definition:
 * @struts.form name="genericCodeForm"
 */
public class GenericCodeForm extends ActionForm {
	/*
	 * Generated Methods
	 */
	  Genericcodelabels gcl=new Genericcodelabels();
	private String buttonValue;
	private String codeTypeId;
	private String codeValue;
	public String getButtonValue() {
		return buttonValue;
	}

	public void setButtonValue(String buttonValue) {
		this.buttonValue = buttonValue;
	}

	public String getCodeTypeId() {
		return codeTypeId;
	}

	public void setCodeTypeId(String codeTypeId) {
		this.codeTypeId = codeTypeId;
	}

	public String getCodeValue() {
		return codeValue;
	}

	public void setCodeValue(String codeValue) {
		this.codeValue = codeValue;
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