/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.ratemangement.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/** 
 * MyEclipse Struts
 * Creation date: 05-16-2008
 * 
 * XDoclet definition:
 * @struts.form name="retailCSSCForm"
 */
public class RetailCSSCForm extends ActionForm {
	/*
	 * Generated Methods
	 */
	private String buttonValue;
	private String charge;
	private String desc; 
	private Integer index;
	private String chargeType;
	private String standard;
	private String asFreightedCheckBox;
	private String amtPerCft;
	private String amtPer100lbs;
	private String amtPerCbm;
	private String amtPer1000kg;
	private String amount;
	private String percentage;
	private String minAmt;
	private String insuranceRate;
	private String insuranceAmt;
	private String exclude;
	private String txtItemcreatedon;
	private String retailCSSId;
	public String getRetailCSSId() {
		return retailCSSId;
	}

	public void setRetailCSSId(String retailCSSId) {
		this.retailCSSId = retailCSSId;
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
	
	
	

	public String getCharge() {
		return charge;
	}

	public void setCharge(String charge) {
		this.charge = charge;
	}

	public String getChargeType() {
		return chargeType;
	}

	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public String getTxtItemcreatedon() {
		return txtItemcreatedon;
	}

	public void setTxtItemcreatedon(String txtItemcreatedon) {
		this.txtItemcreatedon = txtItemcreatedon;
	}

	

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getAmtPer1000kg() {
		return amtPer1000kg;
	}

	public void setAmtPer1000kg(String amtPer1000kg) {
		this.amtPer1000kg = amtPer1000kg;
	}

	public String getAmtPer100lbs() {
		return amtPer100lbs;
	}

	public void setAmtPer100lbs(String amtPer100lbs) {
		this.amtPer100lbs = amtPer100lbs;
	}

	public String getAmtPerCbm() {
		return amtPerCbm;
	}

	public void setAmtPerCbm(String amtPerCbm) {
		this.amtPerCbm = amtPerCbm;
	}

	public String getAmtPerCft() {
		return amtPerCft;
	}

	public void setAmtPerCft(String amtPerCft) {
		this.amtPerCft = amtPerCft;
	}

	public String getInsuranceAmt() {
		return insuranceAmt;
	}

	public void setInsuranceAmt(String insuranceAmt) {
		this.insuranceAmt = insuranceAmt;
	}

	public String getInsuranceRate() {
		return insuranceRate;
	}

	public void setInsuranceRate(String insuranceRate) {
		this.insuranceRate = insuranceRate;
	}

	public String getMinAmt() {
		return minAmt;
	}

	public void setMinAmt(String minAmt) {
		this.minAmt = minAmt;
	}

	public String getPercentage() {
		return percentage;
	}

	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}

	public String getAsFreightedCheckBox() {
		return asFreightedCheckBox;
	}

	public void setAsFreightedCheckBox(String asFreightedCheckBox) {
		this.asFreightedCheckBox = asFreightedCheckBox;
	}

	public String getExclude() {
		return exclude;
	}

	public void setExclude(String exclude) {
		this.exclude = exclude;
	}

}