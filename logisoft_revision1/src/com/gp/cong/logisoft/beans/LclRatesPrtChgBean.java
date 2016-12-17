/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.beans;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author logiware
 */
public class LclRatesPrtChgBean implements Serializable {

    private String chgCode;
    private BigDecimal ofRate;
    private BigDecimal minChg;
    private String chargeTyp;
    private BigDecimal cbm;
    private BigDecimal lbs;
    private BigDecimal kgs;
    private BigDecimal cft;

    public String getChgCode() {
        return chgCode;
    }

    public void setChgCode(String chgCode) {
        this.chgCode = chgCode;
    }

    public BigDecimal getOfRate() {
        return ofRate;
    }

    public void setOfRate(BigDecimal ofRate) {
        this.ofRate = ofRate;
    }

    public BigDecimal getMinChg() {
        return minChg;
    }

    public void setMinChg(BigDecimal minChg) {
        this.minChg = minChg;
    }

    public BigDecimal getCbm() {
        return cbm;
    }

    public void setCbm(BigDecimal cbm) {
        this.cbm = cbm;
    }

    public BigDecimal getCft() {
        return cft;
    }

    public void setCft(BigDecimal cft) {
        this.cft = cft;
    }

    public BigDecimal getKgs() {
        return kgs;
    }

    public void setKgs(BigDecimal kgs) {
        this.kgs = kgs;
    }

    public BigDecimal getLbs() {
        return lbs;
    }

    public void setLbs(BigDecimal lbs) {
        this.lbs = lbs;
    }

    public String getChargeTyp() {
        return chargeTyp;
    }

    public void setChargeTyp(String chargeTyp) {
        this.chargeTyp = chargeTyp;
    }

    

}
