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
 * Creation date: 02-18-2009
 * 
 * XDoclet definition:
 * @struts.form name="customerAddressForm"
 */
public class CustomerAddressForm extends ActionForm {
	private String custName;

	private String custNo;
	private String buttonValue;
	private String custNameTemp;
	private String contactName;
	private String buttonParameter;
	private String[] selectedCheckBox;
	private String recordId;
	private String index;
	
	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getCustNameTemp() {
		return custNameTemp;
	}

	public void setCustNameTemp(String custNameTemp) {
		this.custNameTemp = custNameTemp;
	}

	public String getButtonValue() {
		return buttonValue;
	}

	public void setButtonValue(String buttonValue) {
		this.buttonValue = buttonValue;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getCustNo() {
		return custNo;
	}

	public void setCustNo(String custNo) {
		this.custNo = custNo;
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

	public String[] getSelectedCheckBox() {
		return selectedCheckBox;
	}

	public void setSelectedCheckBox(String[] selectedCheckBox) {
		this.selectedCheckBox = selectedCheckBox;
	}

	public String getButtonParameter() {
		return buttonParameter;
	}

	public void setButtonParameter(String buttonParameter) {
		this.buttonParameter = buttonParameter;
	}
}