/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.lcl.model;

import java.math.BigDecimal;

/**
 *
 * @author Mei
 */
public class UnitSsAutoCostingModel {

    private Long autoCostId;
    private String originName;
    private String fdName;
    private String costCode;
    private String vendorName;
    private String vendorNo;
    private String costType;
    private String rateUom;
    private BigDecimal ratePerUom;
    private String rateAction;
    private String rateCondition;
    private BigDecimal rateConQty;
    private String unitTypeDesc;
    private Integer apCostId;
    private Integer unitTypeId;

    public Long getAutoCostId() {
        return autoCostId;
    }

    public void setAutoCostId(Long autoCostId) {
        this.autoCostId = autoCostId;
    }

    public String getCostCode() {
        return costCode;
    }

    public void setCostCode(String costCode) {
        this.costCode = costCode;
    }

    public String getCostType() {
        return costType;
    }

    public void setCostType(String costType) {
        this.costType = costType;
    }

    public String getFdName() {
        return fdName;
    }

    public void setFdName(String fdName) {
        this.fdName = fdName;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public BigDecimal getRatePerUom() {
        return ratePerUom;
    }

    public void setRatePerUom(BigDecimal ratePerUom) {
        this.ratePerUom = ratePerUom;
    }

    public String getRateUom() {
        return rateUom;
    }

    public void setRateUom(String rateUom) {
        this.rateUom = rateUom;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorNo() {
        return vendorNo;
    }

    public void setVendorNo(String vendorNo) {
        this.vendorNo = vendorNo;
    }

    public String getRateAction() {
        return rateAction;
    }

    public void setRateAction(String rateAction) {
        this.rateAction = rateAction;
    }

    public BigDecimal getRateConQty() {
        return rateConQty;
    }

    public void setRateConQty(BigDecimal rateConQty) {
        this.rateConQty = rateConQty;
    }

    public String getRateCondition() {
        return rateCondition;
    }

    public void setRateCondition(String rateCondition) {
        this.rateCondition = rateCondition;
    }

    public String getUnitTypeDesc() {
        return unitTypeDesc;
    }

    public void setUnitTypeDesc(String unitTypeDesc) {
        this.unitTypeDesc = unitTypeDesc;
    }

    public Integer getApCostId() {
        return apCostId;
    }

    public void setApCostId(Integer apCostId) {
        this.apCostId = apCostId;
    }

    public Integer getUnitTypeId() {
        return unitTypeId;
    }

    public void setUnitTypeId(Integer unitTypeId) {
        this.unitTypeId = unitTypeId;
    }
    
}
