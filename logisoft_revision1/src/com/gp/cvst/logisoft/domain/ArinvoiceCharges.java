package com.gp.cvst.logisoft.domain;

import com.gp.cvst.logisoft.struts.form.ARInvoiceForm;

/**
 * ArinvoiceCharges entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ArinvoiceCharges implements java.io.Serializable {

	// Fields

	private Integer id;
	private ArInvoice arInvoice;
	private String chargesCode;
	private Double amount;
	private String quantity;
	private String description;
	private String glAccount;
	private Double rate;
	private String chargesCodeDesc;
	// Constructors

	public String getChargesCodeDesc() {
		return chargesCodeDesc;
	}

	public void setChargesCodeDesc(String chargesCodeDesc) {
		this.chargesCodeDesc = chargesCodeDesc;
	}

	/** default constructor */
	public ArinvoiceCharges() {
	}

	/** full constructor */
	public ArinvoiceCharges(ArInvoice arInvoice, String chargesCode,
			Double amount, String quantity, String description,
			String glAccount) {
		this.arInvoice = arInvoice;
		this.chargesCode = chargesCode;
		this.amount = amount;
		this.quantity = quantity;
		this.description = description;
		this.glAccount = glAccount;
	}
	public ArinvoiceCharges(ARInvoiceForm aRInvoiceForm) {
		chargesCode=aRInvoiceForm.getChargeCode();
		description=aRInvoiceForm.getDescription();
		glAccount=aRInvoiceForm.getGlAccount();	
		quantity=aRInvoiceForm.getQuantity();
		rate= aRInvoiceForm.getRate();
		if(aRInvoiceForm.getQuantity()!=null && !aRInvoiceForm.getQuantity().equals("") &&
				aRInvoiceForm.getRate()!=null){
			amount = Double.parseDouble(aRInvoiceForm.getQuantity())*aRInvoiceForm.getRate();
			
		}
		chargesCodeDesc = aRInvoiceForm.getChargeCodeDesc();
	}
	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ArInvoice getArInvoice() {
		return this.arInvoice;
	}

	public void setArInvoice(ArInvoice arInvoice) {
		this.arInvoice = arInvoice;
	}

	public String getChargesCode() {
		return this.chargesCode;
	}

	public void setChargesCode(String chargesCode) {
		this.chargesCode = chargesCode;
	}

	public Double getAmount() {
		return this.amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getQuantity() {
		return this.quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGlAccount() {
		return glAccount;
	}

	public void setGlAccount(String glAccount) {
		this.glAccount = glAccount;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	
	

}