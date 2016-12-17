package com.logiware.accounting.model;

/**
 *
 * @author Lakshmi Narayanan
 */
public class CostModel {

    private Integer id;
    private String vendorNumber;
    private String vendorName;
    private String costCode;
    private Double amount;
    private String invoiceNumber;
    private String currency;
    private String comments;
    private String bookingFlag;
    private String readOnlyFlag;
    private String costDate;
    private String costAmount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVendorNumber() {
        return vendorNumber;
    }

    public void setVendorNumber(String vendorNumber) {
        this.vendorNumber = vendorNumber;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getCostCode() {
        return costCode;
    }

    public void setCostCode(String costCode) {
        this.costCode = costCode;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getBookingFlag() {
        return bookingFlag;
    }

    public void setBookingFlag(String bookingFlag) {
        this.bookingFlag = bookingFlag;
    }

    public String getReadOnlyFlag() {
        return readOnlyFlag;
    }

    public void setReadOnlyFlag(String readOnlyFlag) {
        this.readOnlyFlag = readOnlyFlag;
    }

    public String getCostDate() {
        return costDate;
    }

    public void setCostDate(String costDate) {
        this.costDate = costDate;
    }

    public String getCostAmount() {
        return costAmount;
    }

    public void setCostAmount(String costAmount) {
        this.costAmount = costAmount;
    }

}
