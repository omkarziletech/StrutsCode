package com.gp.cvst.logisoft.domain;

import java.util.Date;

import com.gp.cvst.logisoft.struts.form.AccrualsForm;

/**
 * VendorInvoice generated by MyEclipse - Hibernate Tools
 */
public class VendorInvoice implements java.io.Serializable {

    // Fields    
    private Integer id;
    private String vendorNo;
    private String invoiceNo;
    private Double amount;
    private Date invoiceDate;
    private Double amtAllocated;
    private Double amtRemaining;
    private Date dueDate;
    private String terms;
    private String rejected;

    // Constructors
    /**
     * default constructor
     */
    public VendorInvoice() {
    }

    /**
     * minimal constructor
     */
    public VendorInvoice(String vendorNo, String invoiceNo, Double amount) {
        this.vendorNo = vendorNo;
        this.invoiceNo = invoiceNo;
        this.amount = amount;
    }

    /**
     * full constructor
     */
    public VendorInvoice(String vendorNo, String invoiceNo, Double amount, Date invoiceDate, Double amtAllocated, Double amtRemaining, Date dueDate, String terms, String rejected) {
        this.vendorNo = vendorNo;
        this.invoiceNo = invoiceNo;
        this.amount = amount;
        this.invoiceDate = invoiceDate;
        this.amtAllocated = amtAllocated;
        this.amtRemaining = amtRemaining;
        this.dueDate = dueDate;
        this.terms = terms;
        this.rejected = rejected;
    }

    public VendorInvoice(AccrualsForm accrualsForm) {
        vendorNo = accrualsForm.getVendornumber();
        invoiceNo = accrualsForm.getInvoicenumber();
        amount = new Double(accrualsForm.getInvoiceamount());
        amtAllocated = new Double(accrualsForm.getAmountallocated());
        amtRemaining = new Double(accrualsForm.getAmountremain());
        terms = accrualsForm.getTerm();
        rejected = accrualsForm.getRejectinvoice();
    }
    // Property accessors

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVendorNo() {
        return this.vendorNo;
    }

    public void setVendorNo(String vendorNo) {
        this.vendorNo = vendorNo;
    }

    public String getInvoiceNo() {
        return this.invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public Double getAmount() {
        return this.amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getInvoiceDate() {
        return this.invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Double getAmtAllocated() {
        return this.amtAllocated;
    }

    public void setAmtAllocated(Double amtAllocated) {
        this.amtAllocated = amtAllocated;
    }

    public Double getAmtRemaining() {
        return this.amtRemaining;
    }

    public void setAmtRemaining(Double amtRemaining) {
        this.amtRemaining = amtRemaining;
    }

    public Date getDueDate() {
        return this.dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getTerms() {
        return this.terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getRejected() {
        return this.rejected;
    }

    public void setRejected(String rejected) {
        this.rejected = rejected;
    }
}