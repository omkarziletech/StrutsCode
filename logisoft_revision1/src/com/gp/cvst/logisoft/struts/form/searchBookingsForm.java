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
 * Creation date: 08-01-2008
 * 
 * XDoclet definition:
 * @struts.form name="SearchBookingsForm"
 */
public class searchBookingsForm extends ActionForm {
	/*
	 * Generated Methods
	 */

	/** 
	 * Method validate
	  * @param mapping
	 * @param request
	 * @return ActionErrors
	 */
	private String bookingno;
	private String plor;
	private String pod;
	private String pol;
	private String plod;
	private String carrierName;
	private String bookingStartdate;
	private String bookinEnddate;
	private String buttonValue;
	private String index;
	private String sslBooking;
	
	
	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getButtonValue() {
		return buttonValue;
	}

	public void setButtonValue(String buttonValue) {
		this.buttonValue = buttonValue;
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

	public String getBookinEnddate() {
		return bookinEnddate;
	}

	public void setBookinEnddate(String bookinEnddate) {
		this.bookinEnddate = bookinEnddate;
	}
 

	public String getBookingno() {
		return bookingno;
	}

	public void setBookingno(String bookingno) {
		this.bookingno = bookingno;
	}

	public String getBookingStartdate() {
		return bookingStartdate;
	}

	public void setBookingStartdate(String bookingStartdate) {
		this.bookingStartdate = bookingStartdate;
	}

	public String getCarrierName() {
		return carrierName;
	}

	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}

	public String getPlod() {
		return plod;
	}

	public void setPlod(String plod) {
		this.plod = plod;
	}

	public String getPlor() {
		return plor;
	}

	public void setPlor(String plor) {
		this.plor = plor;
	}

	public String getPod() {
		return pod;
	}

	public void setPod(String pod) {
		this.pod = pod;
	}

	public String getPol() {
		return pol;
	}

	public void setPol(String pol) {
		this.pol = pol;
	}

	public String getSslBooking() {
		return sslBooking;
	}

	public void setSslBooking(String sslBooking) {
		this.sslBooking = sslBooking;
	}
}