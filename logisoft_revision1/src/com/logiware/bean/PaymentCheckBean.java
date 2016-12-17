package com.logiware.bean;

/**
 *
 * @author lakshh
 */
public class PaymentCheckBean {

    private String checkId;
    private String checkNumber;
    private String customerNumber;
    private String customerName;
    private String checkAmount;
    private String appliedAmount;
    private String checkBalance;
    private String subType;
    private boolean outOfBalance;
    private Integer noOfInvoices;

    public String getAppliedAmount() {
        return appliedAmount;
    }

    public void setAppliedAmount(String appliedAmount) {
        this.appliedAmount = appliedAmount;
    }

    public String getCheckAmount() {
        return checkAmount;
    }

    public void setCheckAmount(String checkAmount) {
        this.checkAmount = checkAmount;
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public String getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(String checkNumber) {
        this.checkNumber = checkNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getCheckBalance() {
        return checkBalance;
    }

    public void setCheckBalance(String checkBalance) {
        this.checkBalance = checkBalance;
    }

    public boolean isOutOfBalance() {
        return outOfBalance;
    }

    public void setOutOfBalance(boolean outOfBalance) {
        this.outOfBalance = outOfBalance;
    }

    public Integer getNoOfInvoices() {
        return noOfInvoices;
    }

    public void setNoOfInvoices(Integer noOfInvoices) {
        this.noOfInvoices = noOfInvoices;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }
 
}
