/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.ratemangement.form;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/** 
 * MyEclipse Struts
 * Creation date: 03-05-2008
 * 
 * XDoclet definition:
 * @struts.form name="airDetailsForm"
 */
public class AirDetailsForm extends ActionForm {
	/*
	 * Generated Methods
	 */
	private Integer  weightRange;
	private Double generalRate;
	private Double generalAmt;
	private Double expressRate;
	private Double expressAmt;
	private Double deferredRate;
	private Double deferredAmt;
	private String buttonValue;
	private Integer index;
	private String changedDate;
	private String whoChanged;
	public String getButtonValue() {
		return buttonValue;
	}

	public void setButtonValue(String buttonValue) {
		this.buttonValue = buttonValue;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Double getDeferredAmt() {
		return deferredAmt;
	}

	public void setDeferredAmt(Double deferredAmt) {
		this.deferredAmt = deferredAmt;
	}

	public Double getDeferredRate() {
		return deferredRate;
	}

	public void setDeferredRate(Double deferredRate) {
		this.deferredRate = deferredRate;
	}

	

	public Double getExpressAmt() {
		return expressAmt;
	}

	public void setExpressAmt(Double expressAmt) {
		this.expressAmt = expressAmt;
	}

	public Double getExpressRate() {
		return expressRate;
	}

	public void setExpressRate(Double expressRate) {
		this.expressRate = expressRate;
	}

	public Double getGeneralAmt() {
		return generalAmt;
	}

	public void setGeneralAmt(Double generalAmt) {
		this.generalAmt = generalAmt;
	}

	public Double getGeneralRate() {
		return generalRate;
	}

	public void setGeneralRate(Double generalRate) {
		this.generalRate = generalRate;
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

	public Integer getWeightRange() {
		return weightRange;
	}

	public void setWeightRange(Integer weightRange) {
		this.weightRange = weightRange;
	}

	
	

	public String getWhoChanged() {
		return whoChanged;
	}

	public void setWhoChanged(String whoChanged) {
		this.whoChanged = whoChanged;
	}

	public String getChangedDate() {
		return changedDate;
	}

	public void setChangedDate(String changedDate) {
		this.changedDate = changedDate;
	}

	

	
}