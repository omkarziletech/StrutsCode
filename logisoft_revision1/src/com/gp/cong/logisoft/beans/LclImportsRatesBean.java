/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.beans;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author PALRAJ
 */
public class LclImportsRatesBean implements Serializable {

    private String chargeCode;
    private String chargeDesc;
    private BigDecimal totAmount;
    private BigDecimal minCharge;
    private String chargeType;
    private BigDecimal blpct;
    private BigDecimal lbs;
    private BigDecimal inrate;
    private BigDecimal insamt;
    private BigDecimal cbmrt;
    private BigDecimal kgsrt;
    private BigDecimal cft;
    private BigDecimal flatrt;
    private BigDecimal maximumCharge;
    private String billingType;
    private String actCode;

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

    public String getChargeDesc() {
        return chargeDesc;
    }

    public void setChargeDesc(String chargeDesc) {
        this.chargeDesc = chargeDesc;
    }

    public BigDecimal getTotAmount() {
        return totAmount;
    }

    public void setTotAmount(BigDecimal totAmount) {
        this.totAmount = totAmount;
    }

    public BigDecimal getMinCharge() {
        return minCharge;
    }

    public void setMinCharge(BigDecimal minCharge) {
        this.minCharge = minCharge;
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public BigDecimal getBlpct() {
        return blpct;
    }

    public void setBlpct(BigDecimal blpct) {
        this.blpct = blpct;
    }

    public BigDecimal getLbs() {
        return lbs;
    }

    public void setLbs(BigDecimal lbs) {
        this.lbs = lbs;
    }

    public BigDecimal getInrate() {
        return inrate;
    }

    public void setInrate(BigDecimal inrate) {
        this.inrate = inrate;
    }

    public BigDecimal getInsamt() {
        return insamt;
    }

    public void setInsamt(BigDecimal insamt) {
        this.insamt = insamt;
    }

    public BigDecimal getCbmrt() {
        return cbmrt;
    }

    public void setCbmrt(BigDecimal cbmrt) {
        this.cbmrt = cbmrt;
    }

    public BigDecimal getKgsrt() {
        return kgsrt;
    }

    public void setKgsrt(BigDecimal kgsrt) {
        this.kgsrt = kgsrt;
    }

    public BigDecimal getCft() {
        return cft;
    }

    public void setCft(BigDecimal cft) {
        this.cft = cft;
    }

    public BigDecimal getFlatrt() {
        return flatrt;
    }

    public void setFlatrt(BigDecimal flatrt) {
        this.flatrt = flatrt;
    }

    public BigDecimal getMaximumCharge() {
        return maximumCharge;
    }

    public void setMaximumCharge(BigDecimal maximumCharge) {
        this.maximumCharge = maximumCharge;
    }

    public String getBillingType() {
        return billingType;
    }

    public void setBillingType(String billingType) {
        this.billingType = billingType;
    }

    public String getActCode() {
        return actCode;
    }

    public void setActCode(String actCode) {
        this.actCode = actCode;
    }
}
