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
 * Creation date: 01-11-2008
 * 
 * XDoclet definition:
 * @struts.form name="editAirportConfigForm"
 */
public class EditAirportConfigForm extends ActionForm {
	/*
	 * Generated Methods
	 */
	private String terminalNo;
	private String terminalName;
	private String airPortCode;
	private String airPortCityName;
	private String lineManager;
	private String airPortSplRemarksinEnglish;
	private String airPortSplRemarksinSpanish;
	private String serviceAir;
	private String printOnAirFitSch;
	private String buttonValue;
	private String lclAirBlgoCollect;
	private String flightScheduleRegion;
	private String airportName;
	public String getAirportName() {
		return airportName;
	}

	public void setAirportName(String airportName) {
		this.airportName = airportName;
	}

	public String getFlightScheduleRegion() {
		return flightScheduleRegion;
	}

	public void setFlightScheduleRegion(String flightScheduleRegion) {
		this.flightScheduleRegion = flightScheduleRegion;
	}

	public String getLclAirBlgoCollect() {
		return lclAirBlgoCollect;
	}

	public void setLclAirBlgoCollect(String lclAirBlgoCollect) {
		this.lclAirBlgoCollect = lclAirBlgoCollect;
	}

	public String getAirPortCityName() {
		return airPortCityName;
	}

	public void setAirPortCityName(String airPortCityName) {
		this.airPortCityName = airPortCityName;
	}

	public String getAirPortCode() {
		return airPortCode;
	}

	public void setAirPortCode(String airPortCode) {
		this.airPortCode = airPortCode;
	}

	public String getAirPortSplRemarksinEnglish() {
		return airPortSplRemarksinEnglish;
	}

	public void setAirPortSplRemarksinEnglish(String airPortSplRemarksinEnglish) {
		this.airPortSplRemarksinEnglish = airPortSplRemarksinEnglish;
	}

	public String getAirPortSplRemarksinSpanish() {
		return airPortSplRemarksinSpanish;
	}

	public void setAirPortSplRemarksinSpanish(String airPortSplRemarksinSpanish) {
		this.airPortSplRemarksinSpanish = airPortSplRemarksinSpanish;
	}

	public String getButtonValue() {
		return buttonValue;
	}

	public void setButtonValue(String buttonValue) {
		this.buttonValue = buttonValue;
	}

	public String getLineManager() {
		return lineManager;
	}

	public void setLineManager(String lineManager) {
		this.lineManager = lineManager;
	}

	

	public String getPrintOnAirFitSch() {
		return printOnAirFitSch;
	}

	public void setPrintOnAirFitSch(String printOnAirFitSch) {
		this.printOnAirFitSch = printOnAirFitSch;
	}

	public String getServiceAir() {
		return serviceAir;
	}

	public void setServiceAir(String serviceAir) {
		this.serviceAir = serviceAir;
	}

	public String getTerminalName() {
		return terminalName;
	}

	public void setTerminalName(String terminalName) {
		this.terminalName = terminalName;
	}

	public String getTerminalNo() {
		return terminalNo;
	}

	public void setTerminalNo(String terminalNo) {
		this.terminalNo = terminalNo;
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