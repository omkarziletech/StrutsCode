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
 * Creation date: 06-20-2008
 * 
 * XDoclet definition:
 * @struts.form name="rejectedInvoiceForm"
 */
public class RejectedInvoiceForm extends ActionForm {
	/*
	 * Generated Methods
	 */
	private String buttonValue;
	private String customername;
	private String customerno;
	private String datefrom;
	private String dateto;
	private String vendortType;
	private String voyage;
	private String totalamountpaid;
	private String billofladding;
	private String invoicenumber;
	private String invoiceamount;
	private String costcode;
	private String showparent;
	
	
	public String getShowparent() {
		return showparent;
	}

	public void setShowparent(String showparent) {
		this.showparent = showparent;
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

	public String getBillofladding() {
		return billofladding;
	}

	public void setBillofladding(String billofladding) {
		this.billofladding = billofladding;
	}

	public String getButtonValue() {
		return buttonValue;
	}

	public void setButtonValue(String buttonValue) {
		this.buttonValue = buttonValue;
	}

	public String getCostcode() {
		return costcode;
	}

	public void setCostcode(String costcode) {
		this.costcode = costcode;
	}

	public String getDatefrom() {
		return datefrom;
	}

	public void setDatefrom(String datefrom) {
		this.datefrom = datefrom;
	}

	public String getDateto() {
		return dateto;
	}

	public void setDateto(String dateto) {
		this.dateto = dateto;
	}

	public String getInvoiceamount() {
		return invoiceamount;
	}

	public void setInvoiceamount(String invoiceamount) {
		this.invoiceamount = invoiceamount;
	}

	public String getInvoicenumber() {
		return invoicenumber;
	}

	public void setInvoicenumber(String invoicenumber) {
		this.invoicenumber = invoicenumber;
	}

	public String getTotalamountpaid() {
		return totalamountpaid;
	}

	public void setTotalamountpaid(String totalamountpaid) {
		this.totalamountpaid = totalamountpaid;
	}

	

	public String getCustomername() {
		return customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public String getCustomerno() {
		return customerno;
	}

	public void setCustomerno(String customerno) {
		this.customerno = customerno;
	}

	public String getVendortType() {
		return vendortType;
	}

	public void setVendortType(String vendortType) {
		this.vendortType = vendortType;
	}

	public String getVoyage() {
		return voyage;
	}

	public void setVoyage(String voyage) {
		this.voyage = voyage;
	}
}