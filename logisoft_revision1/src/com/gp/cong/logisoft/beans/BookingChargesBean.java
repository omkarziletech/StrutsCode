/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.beans;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Logiware
 */
public class BookingChargesBean implements Serializable {

    private Long id;
    private String chargeCode;
    private String arBillToParty;
    private BigDecimal totalAmt;
    private BigDecimal paidAmt;
    private BigDecimal balanceAmt;
    private BigDecimal adjustmentAmt;
    private String rateUom;
    private String blueScreencode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

    public BigDecimal getBalanceAmt() {
        return balanceAmt;
    }

    public void setBalanceAmt(BigDecimal balanceAmt) {
        this.balanceAmt = balanceAmt;
    }

    public BigDecimal getPaidAmt() {
        return paidAmt;
    }

    public void setPaidAmt(BigDecimal paidAmt) {
        this.paidAmt = paidAmt;
    }

    public BigDecimal getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(BigDecimal totalAmt) {
        this.totalAmt = totalAmt;
    }

    public String getArBillToParty() {
        return arBillToParty;
    }

    public void setArBillToParty(String arBillToParty) {
        this.arBillToParty = arBillToParty;
    }

    public BigDecimal getAdjustmentAmt() {
        return adjustmentAmt;
    }

    public void setAdjustmentAmt(BigDecimal adjustmentAmt) {
        this.adjustmentAmt = adjustmentAmt;
    }

    public String getRateUom() {
        return rateUom;
    }

    public void setRateUom(String rateUom) {
        this.rateUom = rateUom;
    }

    public String getBlueScreencode() {
        return blueScreencode;
    }

    public void setBlueScreencode(String blueScreencode) {
        this.blueScreencode = blueScreencode;
    }   
    
}
