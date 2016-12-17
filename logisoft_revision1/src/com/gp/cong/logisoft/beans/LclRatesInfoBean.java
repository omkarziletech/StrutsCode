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
public class LclRatesInfoBean implements Serializable {

    private BigDecimal engCft;
    private BigDecimal engWgt;
    private BigDecimal metCft;
    private BigDecimal metWgt;
    private BigDecimal ofMin;

    public BigDecimal getEngCft() {
        return engCft;
    }

    public void setEngCft(BigDecimal engCft) {
        this.engCft = engCft;
    }

    public BigDecimal getEngWgt() {
        return engWgt;
    }

    public void setEngWgt(BigDecimal engWgt) {
        this.engWgt = engWgt;
    }

    public BigDecimal getMetCft() {
        return metCft;
    }

    public void setMetCft(BigDecimal metCft) {
        this.metCft = metCft;
    }

    public BigDecimal getMetWgt() {
        return metWgt;
    }

    public void setMetWgt(BigDecimal metWgt) {
        this.metWgt = metWgt;
    }

    public BigDecimal getOfMin() {
        return ofMin;
    }

    public void setOfMin(BigDecimal ofMin) {
        this.ofMin = ofMin;
    }
}
