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
 * @struts.form name="retailDetailsForm"
 */
public class RetailDetailsForm extends ActionForm {
	/*
	 * Generated Methods
	 */
	
	private String rateCbm;
	private String rateCft;
	private String rateKgs;
	private String rateLb;
	private String metricMinamt;
	private String englishMinamt;
	
	private String firstOcean;
	private String firstTt;
	private String secondOcean;
	private String secondTt;
	private String thirdOcean;
	private String thirdTt;
	private String fourthOcean;
	private String fourthTt;
	private String txtItemcreatedon;
	private String buttonValue;
	private Integer index;
	private String metric;
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
	
	
	


	public String getFirstOcean() {
		return firstOcean;
	}

	public void setFirstOcean(String firstOcean) {
		this.firstOcean = firstOcean;
	}

	public String getFirstTt() {
		return firstTt;
	}

	public void setFirstTt(String firstTt) {
		this.firstTt = firstTt;
	}

	public String getFourthOcean() {
		return fourthOcean;
	}

	public void setFourthOcean(String fourthOcean) {
		this.fourthOcean = fourthOcean;
	}

	public String getFourthTt() {
		return fourthTt;
	}

	public void setFourthTt(String fourthTt) {
		this.fourthTt = fourthTt;
	}

	
	public String getSecondOcean() {
		return secondOcean;
	}

	public void setSecondOcean(String secondOcean) {
		this.secondOcean = secondOcean;
	}

	public String getSecondTt() {
		return secondTt;
	}

	public void setSecondTt(String secondTt) {
		this.secondTt = secondTt;
	}

	public String getThirdOcean() {
		return thirdOcean;
	}

	public void setThirdOcean(String thirdOcean) {
		this.thirdOcean = thirdOcean;
	}

	public String getThirdTt() {
		return thirdTt;
	}

	public void setThirdTt(String thirdTt) {
		this.thirdTt = thirdTt;
	}

	

	public String getMetric() {
		return metric;
	}

	public void setMetric(String metric) {
		this.metric = metric;
	}

	
	public String getTxtItemcreatedon() {
		return txtItemcreatedon;
	}

	public void setTxtItemcreatedon(String txtItemcreatedon) {
		this.txtItemcreatedon = txtItemcreatedon;
	}

	public String getEnglishMinamt() {
		return englishMinamt;
	}

	public void setEnglishMinamt(String englishMinamt) {
		this.englishMinamt = englishMinamt;
	}

	public String getMetricMinamt() {
		return metricMinamt;
	}

	public void setMetricMinamt(String metricMinamt) {
		this.metricMinamt = metricMinamt;
	}

	public String getRateCbm() {
		return rateCbm;
	}

	public void setRateCbm(String rateCbm) {
		this.rateCbm = rateCbm;
	}

	public String getRateCft() {
		return rateCft;
	}

	public void setRateCft(String rateCft) {
		this.rateCft = rateCft;
	}

	public String getRateKgs() {
		return rateKgs;
	}

	public void setRateKgs(String rateKgs) {
		this.rateKgs = rateKgs;
	}

	public String getRateLb() {
		return rateLb;
	}

	public void setRateLb(String rateLb) {
		this.rateLb = rateLb;
	}
}