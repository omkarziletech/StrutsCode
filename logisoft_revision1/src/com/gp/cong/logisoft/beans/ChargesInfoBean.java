package com.gp.cong.logisoft.beans;

import com.gp.cvst.logisoft.domain.GlMapping;
import java.io.Serializable;
import java.math.BigDecimal;

public class ChargesInfoBean implements Serializable {

    private BigDecimal rate;
    private Double measureRate;
    private Double weightRate;
    private BigDecimal minCharge;
    private String chargeCode;
    private GlMapping glMapping;
    private Integer chargeType;
    private String commodityCode;
    private String chargesDesc;
    //private String label1;
    //private String label2;
    private String pcb;
    private BigDecimal ratePerWeightUnit;
    private BigDecimal ratePerWeightUnitDiv;
    private BigDecimal ratePerVolumeUnit;
    private BigDecimal ratePerVolumeUnitDiv;
    private BigDecimal rateFlatMinimum;
    private BigDecimal ratePerUnit;
    private BigDecimal ratePerUnitDiv;
    private String ratePerUnitUom;
    private Long bookingPieceId;
    private String rateUom;

    public String getChargesDesc() {
        return chargesDesc;
    }

    public void setChargesDesc(String chargesDesc) {
        this.chargesDesc = chargesDesc;
    }
    /*
     public String getLabel1() {
     return label1;
     }

     public void setLabel1(String label1) {
     this.label1 = label1;
     }

     public String getLabel2() {
     return label2;
     }

     public void setLabel2(String label2) {
     this.label2 = label2;
     }
     */

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
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

    public Double getMeasureRate() {
        return measureRate;
    }

    public void setMeasureRate(Double measureRate) {
        this.measureRate = measureRate;
    }

    public BigDecimal getMinCharge() {
        return minCharge;
    }

    public void setMinCharge(BigDecimal minCharge) {
        this.minCharge = minCharge;
    }

    public Double getWeightRate() {
        return weightRate;
    }

    public void setWeightRate(Double weightRate) {
        this.weightRate = weightRate;
    }

    public String getPcb() {
        return pcb;
    }

    public void setPcb(String pcb) {
        this.pcb = pcb;
    }

    public BigDecimal getRateFlatMinimum() {
        return rateFlatMinimum;
    }

    public void setRateFlatMinimum(BigDecimal rateFlatMinimum) {
        this.rateFlatMinimum = rateFlatMinimum;
    }

    public BigDecimal getRatePerUnit() {
        return ratePerUnit;
    }

    public void setRatePerUnit(BigDecimal ratePerUnit) {
        this.ratePerUnit = ratePerUnit;
    }

    public String getRatePerUnitUom() {
        return ratePerUnitUom;
    }

    public void setRatePerUnitUom(String ratePerUnitUom) {
        this.ratePerUnitUom = ratePerUnitUom;
    }

    public BigDecimal getRatePerVolumeUnit() {
        return ratePerVolumeUnit;
    }

    public void setRatePerVolumeUnit(BigDecimal ratePerVolumeUnit) {
        this.ratePerVolumeUnit = ratePerVolumeUnit;
    }

    public BigDecimal getRatePerVolumeUnitDiv() {
        return ratePerVolumeUnitDiv;
    }

    public void setRatePerVolumeUnitDiv(BigDecimal ratePerVolumeUnitDiv) {
        this.ratePerVolumeUnitDiv = ratePerVolumeUnitDiv;
    }

    public BigDecimal getRatePerWeightUnit() {
        return ratePerWeightUnit;
    }

    public void setRatePerWeightUnit(BigDecimal ratePerWeightUnit) {
        this.ratePerWeightUnit = ratePerWeightUnit;
    }

    public BigDecimal getRatePerWeightUnitDiv() {
        return ratePerWeightUnitDiv;
    }

    public void setRatePerWeightUnitDiv(BigDecimal ratePerWeightUnitDiv) {
        this.ratePerWeightUnitDiv = ratePerWeightUnitDiv;
    }

    public BigDecimal getRatePerUnitDiv() {
        return ratePerUnitDiv;
    }

    public void setRatePerUnitDiv(BigDecimal ratePerUnitDiv) {
        this.ratePerUnitDiv = ratePerUnitDiv;
    }

    public Long getBookingPieceId() {
        return bookingPieceId;
    }

    public void setBookingPieceId(Long bookingPieceId) {
        this.bookingPieceId = bookingPieceId;
    }

    public GlMapping getGlMapping() {
        return glMapping;
    }

    public void setGlMapping(GlMapping glMapping) {
        this.glMapping = glMapping;
    }

    public String getRateUom() {
        return rateUom;
    }

    public void setRateUom(String rateUom) {
        this.rateUom = rateUom;
    }
}
