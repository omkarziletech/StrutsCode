package com.logiware.accounting.model;

import com.gp.cong.logisoft.domain.User;

/**
 *
 * @author Lakshmi Narayanan
 */
public class InvoiceModel {

    private String customerNumber;
    private String customerName;
    private String billTo;
    private String blNumber;
    private Double amount;
    private boolean creditDebit;
    private String invoiceNumber;
    private String invoiceAmount;
    private String invoiceDate;
    private String dueDate;
    private Integer age;
    private String creditTerms;
    private boolean manualNotes;
    private String noteModuleId;
    private String noteModuleRefId;
    private Integer id;
    private String invoiceOrBl;
    private User paidBy;

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getBillTo() {
        return billTo;
    }

    public void setBillTo(String billTo) {
        this.billTo = billTo;
    }

    public String getBlNumber() {
        return blNumber;
    }

    public void setBlNumber(String blNumber) {
        this.blNumber = blNumber;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public boolean isCreditDebit() {
        return creditDebit;
    }

    public void setCreditDebit(boolean creditDebit) {
        this.creditDebit = creditDebit;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(String invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getCreditTerms() {
        return creditTerms;
    }

    public void setCreditTerms(String creditTerms) {
        this.creditTerms = creditTerms;
    }

    public boolean isManualNotes() {
        return manualNotes;
    }

    public void setManualNotes(boolean manualNotes) {
        this.manualNotes = manualNotes;
    }

    public String getNoteModuleId() {
        return noteModuleId;
    }

    public void setNoteModuleId(String noteModuleId) {
        this.noteModuleId = noteModuleId;
    }

    public String getNoteModuleRefId() {
        return noteModuleRefId;
    }

    public void setNoteModuleRefId(String noteModuleRefId) {
        this.noteModuleRefId = noteModuleRefId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInvoiceOrBl() {
        return invoiceOrBl;
    }

    public void setInvoiceOrBl(String invoiceOrBl) {
        this.invoiceOrBl = invoiceOrBl;
    }

    public User getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(User paidBy) {
        this.paidBy = paidBy;
    }

}
