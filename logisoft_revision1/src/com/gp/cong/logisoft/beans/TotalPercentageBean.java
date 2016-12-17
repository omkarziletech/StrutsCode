package com.gp.cong.logisoft.beans;

import java.io.Serializable;

public class TotalPercentageBean implements Serializable {

    private String desc;
    private Double minchg;
    private Double totper;
    private String chargeCode;
    private Integer chargeType;
    private String commodityCode;

    public Double getMinchg() {
        return minchg;
    }

    public void setMinchg(Double minchg) {
        this.minchg = minchg;
    }

    public Double getTotper() {
        return totper;
    }

    public void setTotper(Double totper) {
        this.totper = totper;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

    public Integer getChargeType() {
        return chargeType;
    }

    public void setChargeType(Integer chargeType) {
        this.chargeType = chargeType;
    }

    public String getCommodityCode() {
        return commodityCode;
    }

    public void setCommodityCode(String commodityCode) {
        this.commodityCode = commodityCode;
    }
}
