package com.logiware.hibernate.domain;

import java.io.Serializable;

public class FclBuyOtherCommodity implements Serializable {

    private Integer id;
    private String addSub;
    private Double markUp;
    private Double markUp2;
    private String costCode;
    private String commodityCode;
    private String baseCommodityCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddSub() {
        return addSub;
    }

    public void setAddSub(String addSub) {
        this.addSub = addSub;
    }

    public Double getMarkUp() {
        return markUp;
    }

    public void setMarkUp(Double markUp) {
        this.markUp = markUp;
    }

    public String getCostCode() {
        return costCode;
    }

    public void setCostCode(String costCode) {
        this.costCode = costCode;
    }

    public String getBaseCommodityCode() {
        return baseCommodityCode;
    }

    public void setBaseCommodityCode(String baseCommodityCode) {
        this.baseCommodityCode = baseCommodityCode;
    }

    public String getCommodityCode() {
        return commodityCode;
    }

    public void setCommodityCode(String commodityCode) {
        this.commodityCode = commodityCode;
    }

    public Double getMarkUp2() {
        return markUp2;
    }

    public void setMarkUp2(Double markUp2) {
        this.markUp2 = markUp2;
    }
}
