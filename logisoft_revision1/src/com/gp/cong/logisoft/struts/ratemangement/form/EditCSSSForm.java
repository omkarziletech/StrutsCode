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
 * Creation date: 05-12-2008
 * 
 * XDoclet definition:
 * @struts.form name="editCSSSForm"
 */
public class EditCSSSForm extends ActionForm {
	/*
	 * Generated Methods
	 */
	private String buttonValue;
	private Integer charge;
	private String desc; 
	private Integer index;
	private String chargeType;
	private String standard;
	private String asFrfgted;
	private Double amtPerCft;
	private Double amtPer100lbs;
	private Double amtPerCbm;
	private Double amtPer1000kg;
	private Double amount;
	private String percentage;
	private Double minAmt;
	private Double insuranceRate;
	private Double insuranceAmt;
	private String txtItemcreatedon;
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getAmtPer1000kg() {
		return amtPer1000kg;
	}

	public void setAmtPer1000kg(Double amtPer1000kg) {
		this.amtPer1000kg = amtPer1000kg;
	}

	public Double getAmtPer100lbs() {
		return amtPer100lbs;
	}

	public void setAmtPer100lbs(Double amtPer100lbs) {
		this.amtPer100lbs = amtPer100lbs;
	}

	public Double getAmtPerCbm() {
		return amtPerCbm;
	}

	public void setAmtPerCbm(Double amtPerCbm) {
		this.amtPerCbm = amtPerCbm;
	}

	public Double getAmtPerCft() {
		return amtPerCft;
	}

	public void setAmtPerCft(Double amtPerCft) {
		this.amtPerCft = amtPerCft;
	}

	

	public String getAsFrfgted() {
		return asFrfgted;
	}

	public void setAsFrfgted(String asFrfgted) {
		this.asFrfgted = asFrfgted;
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

	public Double getInsuranceAmt() {
		return insuranceAmt;
	}

	public void setInsuranceAmt(Double insuranceAmt) {
		this.insuranceAmt = insuranceAmt;
	}

	public Double getInsuranceRate() {
		return insuranceRate;
	}

	public void setInsuranceRate(Double insuranceRate) {
		this.insuranceRate = insuranceRate;
	}

	public Double getMinAmt() {
		return minAmt;
	}

	public void setMinAmt(Double minAmt) {
		this.minAmt = minAmt;
	}

	
	public String getPercentage() {
		return percentage;
	}

	public void setPercentage(String percentage) {
		this.percentage = percentage;
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