package com.gp.cong.logisoft.struts.ratemangement.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/** 
 * MyEclipse Struts
 * Creation date: 06-04-2008
 * 
 * XDoclet definition:
 * @struts.form name="editLclDocChargeForm"
 */
public class EditLclDocChargeForm extends ActionForm {
	/*
	 * Generated Methods
	 */
	private String buttonValue;
	private Integer charge;
	private String desc; 
	private Integer index;
	private String amount;
	private String maxDocCharge;
	private String ffCommision;
	private String blBottomLine;
	
	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getBlBottomLine() {
		return blBottomLine;
	}

	public void setBlBottomLine(String blBottomLine) {
		this.blBottomLine = blBottomLine;
	}

	public String getButtonValue() {
		return buttonValue;
	}

	public void setButtonValue(String buttonValue) {
		this.buttonValue = buttonValue;
	}

	public Integer getCharge() {
		return charge;
	}

	public void setCharge(Integer charge) {
		this.charge = charge;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getFfCommision() {
		return ffCommision;
	}

	public void setFfCommision(String ffCommision) {
		this.ffCommision = ffCommision;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getMaxDocCharge() {
		return maxDocCharge;
	}

	public void setMaxDocCharge(String maxDocCharge) {
		this.maxDocCharge = maxDocCharge;
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