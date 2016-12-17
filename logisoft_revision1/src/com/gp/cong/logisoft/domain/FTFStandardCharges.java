package com.gp.cong.logisoft.domain;

import java.io.Serializable;
import java.util.Date;

public class FTFStandardCharges implements Serializable {

    private Integer id;
    private Integer FtfId;
    private GenericCode chargeCode;
    private GenericCode chargeType;
    private String standard;
    private Double amtPerCft;
    private Double amtPer100lbs;
    private Double amtPerCbm;
    private Double amtPer1000Kg;
    private Double amount;
    private Double percentage;
    private Double minAmt;
    private Date effectiveDate;
    private Date changedDate;
    private String whoChanged;
    private String asFrfgted;
    private Double insuranceRate;
    private Double insuranceAmt;
    private Integer index;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Double getInsuranceAmt() {
        return insuranceAmt;
    }

    public void setInsuranceAmt(Double insuranceAmt) {
        this.insuranceAmt = insuranceAmt;
    }

    public Double getInsuranceRate() {
        return insuranceRate;
    }

    public void setInsuranceRate(Double insuranceRate) {
        this.insuranceRate = insuranceRate;
    }

    public String getAsFrfgted() {
        return asFrfgted;
    }

    public void setAsFrfgted(String asFrfgted) {
        this.asFrfgted = asFrfgted;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getAmtPer1000Kg() {
        return amtPer1000Kg;
    }

    public void setAmtPer1000Kg(Double amtPer1000Kg) {
        this.amtPer1000Kg = amtPer1000Kg;
    }

    public Double getAmtPer100lbs() {
        return amtPer100lbs;
    }

    public void setAmtPer100lbs(Double amtPer100lbs) {
        this.amtPer100lbs = amtPer100lbs;
    }

    public Double getAmtPerCbm() {
        return amtPerCbm;
    }

    public void setAmtPerCbm(Double amtPerCbm) {
        this.amtPerCbm = amtPerCbm;
    }

    public Double getAmtPerCft() {
        return amtPerCft;
    }

    public void setAmtPerCft(Double amtPerCft) {
        this.amtPerCft = amtPerCft;
    }

    public Date getChangedDate() {
        return changedDate;
    }

    public void setChangedDate(Date changedDate) {
        this.changedDate = changedDate;
    }

    public GenericCode getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(GenericCode chargeCode) {
        this.chargeCode = chargeCode;
    }

    public GenericCode getChargeType() {
        return chargeType;
    }

    public void setChargeType(GenericCode chargeType) {
        this.chargeType = chargeType;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getMinAmt() {
        return minAmt;
    }

    public void setMinAmt(Double minAmt) {
        this.minAmt = minAmt;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getWhoChanged() {
        return whoChanged;
    }

    public void setWhoChanged(String whoChanged) {
        this.whoChanged = whoChanged;
    }

    public Integer getFtfId() {
        return FtfId;
    }

    public void setFtfId(Integer ftfId) {
        this.FtfId = ftfId;
    }
}
