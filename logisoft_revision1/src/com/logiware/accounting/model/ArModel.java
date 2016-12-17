package com.logiware.accounting.model;

/**
 *
 * @author Lakshmi Narayanan
 */
public class ArModel {

    private String customerNumber;
    private String customerName;
    private Double amount;
    private String billToParty;
    private String blNumber;
    private String creditDebitNoteType;

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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getBillToParty() {
        return billToParty;
    }

    public void setBillToParty(String billToParty) {
        this.billToParty = billToParty;
    }

    public String getBlNumber() {
        return blNumber;
    }

    public void setBlNumber(String blNumber) {
        this.blNumber = blNumber;
    }

    public String getCreditDebitNoteType() {
        return creditDebitNoteType;
    }

    public void setCreditDebitNoteType(String creditDebitNoteType) {
        this.creditDebitNoteType = creditDebitNoteType;
    }

}
