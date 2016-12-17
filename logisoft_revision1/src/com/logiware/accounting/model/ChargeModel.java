package com.logiware.accounting.model;

import java.math.BigDecimal;

/**
 *
 * @author Lakshmi Narayanan
 */
public class ChargeModel {

    private Integer id;
    private String cnNumber;
    private String billTo;
    private String chargeCode;
    private Double amount;
    private String currency;
    private String comments;
    private String bookingFlag;
    private String readOnlyFlag;
    private String arBillToParty;
    private String blueScreenChargeCode;
    private BigDecimal arAmount;
    private String cnType;
    private String cnCode;
    private String cnConsAcctNo;
    private String cnNotyAcctNo;
    private String cnThirdPartyAcctNo;
    private String correctionNo;
    private BigDecimal oldAmount;
    private BigDecimal newAmount;
    private String custName;
    private String custNumber;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCnNumber() {
        return cnNumber;
    }

    public void setCnNumber(String cnNumber) {
        this.cnNumber = cnNumber;
    }

    public String getBillTo() {
        return billTo;
    }

    public void setBillTo(String billTo) {
        this.billTo = billTo;
    }

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
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

    public String getArBillToParty() {
        return arBillToParty;
    }

    public void setArBillToParty(String arBillToParty) {
        this.arBillToParty = arBillToParty;
    }

    public String getBlueScreenChargeCode() {
        return blueScreenChargeCode;
    }

    public void setBlueScreenChargeCode(String blueScreenChargeCode) {
        this.blueScreenChargeCode = blueScreenChargeCode;
    }

    public BigDecimal getArAmount() {
        return arAmount;
    }

    public void setArAmount(BigDecimal arAmount) {
        this.arAmount = arAmount;
    }

    public String getCnType() {
        return cnType;
    }

    public void setCnType(String cnType) {
        this.cnType = cnType;
    }

    public String getCnCode() {
        return cnCode;
    }

    public void setCnCode(String cnCode) {
        this.cnCode = cnCode;
    }

    public String getCnConsAcctNo() {
        return cnConsAcctNo;
    }

    public void setCnConsAcctNo(String cnConsAcctNo) {
        this.cnConsAcctNo = cnConsAcctNo;
    }

    public String getCnNotyAcctNo() {
        return cnNotyAcctNo;
    }

    public void setCnNotyAcctNo(String cnNotyAcctNo) {
        this.cnNotyAcctNo = cnNotyAcctNo;
    }

    public String getCnThirdPartyAcctNo() {
        return cnThirdPartyAcctNo;
    }

    public void setCnThirdPartyAcctNo(String cnThirdPartyAcctNo) {
        this.cnThirdPartyAcctNo = cnThirdPartyAcctNo;
    }

    public String getCorrectionNo() {
        return correctionNo;
    }

    public void setCorrectionNo(String correctionNo) {
        this.correctionNo = correctionNo;
    }

    public BigDecimal getOldAmount() {
        return oldAmount;
    }

    public void setOldAmount(BigDecimal oldAmount) {
        this.oldAmount = oldAmount;
    }

    public BigDecimal getNewAmount() {
        return newAmount;
    }

    public void setNewAmount(BigDecimal newAmount) {
        this.newAmount = newAmount;
    }

}
