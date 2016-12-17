package com.gp.cong.logisoft.beans;

import java.io.Serializable;

public class NonRates implements Serializable {

    private Integer commodity;
    private Integer baseCommodity;
    private String addsub;
    private Double amount;
    private String chargeCode;

    public Integer getCommodity() {
        return commodity;
    }

    public void setCommodity(Integer commodity) {
        this.commodity = commodity;
    }

    public Integer getBaseCommodity() {
        return baseCommodity;
    }

    public void setBaseCommodity(Integer baseCommodity) {
        this.baseCommodity = baseCommodity;
    }

    public String getAddsub() {
        return addsub;
    }

    public void setAddsub(String addsub) {
        this.addsub = addsub;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }
}
