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
 * Creation date: 12-06-2007
 * 
 * XDoclet definition:
 * @struts.form name="editManagingCarriersOATForm"
 */
public class EditManagingCarriersOATForm extends ActionForm {
	/*
	 * Generated Methods
	 */
	
	private String carriercode;
	private String carriername;
	private String carrierType;
	private String SCAC;
	private String airabbr;
	private String aircod;
	private String alnact;
	private String acomyn;
	private String commissiontype;
	private String commisionpercentage;
	private String buttonValue;
	private String abbreviation;
	private String fclContactNumber;
	private String ediCarrier;
	public String getEdiCarrier() {
		return ediCarrier;
	}

	public void setEdiCarrier(String ediCarrier) {
		this.ediCarrier = ediCarrier;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public String getFclContactNumber() {
		return fclContactNumber;
	}

	public void setFclContactNumber(String fclContactNumber) {
		this.fclContactNumber = fclContactNumber;
	}

	public String getAcomyn() {
		return acomyn;
	}

	public void setAcomyn(String acomyn) {
		this.acomyn = acomyn;
	}

	public String getAirabbr() {
		return airabbr;
	}

	public void setAirabbr(String airabbr) {
		this.airabbr = airabbr;
	}

	public String getAircod() {
		return aircod;
	}

	public void setAircod(String aircod) {
		this.aircod = aircod;
	}

	public String getAlnact() {
		return alnact;
	}

	public void setAlnact(String alnact) {
		this.alnact = alnact;
	}

	public String getButtonValue() {
		return buttonValue;
	}

	public void setButtonValue(String buttonValue) {
		this.buttonValue = buttonValue;
	}

	public String getCarriercode() {
		return carriercode;
	}

	public void setCarriercode(String carriercode) {
		this.carriercode = carriercode;
	}

	public String getCarriername() {
		return carriername;
	}

	public void setCarriername(String carriername) {
		this.carriername = carriername;
	}

	public String getCarrierType() {
		return carrierType;
	}

	public void setCarrierType(String carrierType) {
		this.carrierType = carrierType;
	}

	public String getCommisionpercentage() {
		return commisionpercentage;
	}

	public void setCommisionpercentage(String commisionpercentage) {
		this.commisionpercentage = commisionpercentage;
	}

	public String getCommissiontype() {
		return commissiontype;
	}

	public void setCommissiontype(String commissiontype) {
		this.commissiontype = commissiontype;
	}

	public String getSCAC() {
		return SCAC;
	}

	public void setSCAC(String scac) {
		SCAC = scac;
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