package com.gp.cong.logisoft.domain;

import java.util.Date;

/**
 * AbstractUniverseCommodityChrg generated by MyEclipse - Hibernate Tools
 */
public class UniverseCommodityChrg implements java.io.Serializable {

    // Fields    
    private Integer id;
    private Integer universeId;
    private GenericCode chargeCode;
    private GenericCode chargeType;
    private String standard;
    private Double amtPerCft;
    private Double amtPer100lbs;
    private Double amtPerCbm;
    private Double amtPer1000kg;
    private Double amount;
    private Double percentage;
    private Double minAmt;
    private Date effectiveDate;
    private Date changedDate;
    private String whoChanged;
    private String asFrfgted;
    private Double insuranceRate;
    private Double insuranceAmt;
    private String exclude;

    // Constructors
    /**
     * default constructor
     */
    public UniverseCommodityChrg() {
    }

    /**
     * full constructor
     */
    public UniverseCommodityChrg(Integer universeId, GenericCode chargeCode, GenericCode chargeType, String standard, Double amtPerCft, Double amtPer100lbs, Double amtPerCbm, Double amtPer1000kg, Double amount, Double percentage, Double minAmt, Date effectiveDate, Date changedDate, String whoChanged, String asFrfgted, Double insuranceRate, Double insuranceAmt, String exclude) {
        this.universeId = universeId;
        this.chargeCode = chargeCode;
        this.chargeType = chargeType;
        this.standard = standard;
        this.amtPerCft = amtPerCft;
        this.amtPer100lbs = amtPer100lbs;
        this.amtPerCbm = amtPerCbm;
        this.amtPer1000kg = amtPer1000kg;
        this.amount = amount;
        this.percentage = percentage;
        this.minAmt = minAmt;
        this.effectiveDate = effectiveDate;
        this.changedDate = changedDate;
        this.whoChanged = whoChanged;
        this.asFrfgted = asFrfgted;
        this.insuranceRate = insuranceRate;
        this.insuranceAmt = insuranceAmt;
        this.exclude = exclude;
    }

    // Property accessors
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUniverseId() {
        return this.universeId;
    }

    public void setUniverseId(Integer universeId) {
        this.universeId = universeId;
    }

    public String getStandard() {
        return this.standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public Double getAmtPerCft() {
        return this.amtPerCft;
    }

    public void setAmtPerCft(Double amtPerCft) {
        this.amtPerCft = amtPerCft;
    }

    public Double getAmtPer100lbs() {
        return this.amtPer100lbs;
    }

    public void setAmtPer100lbs(Double amtPer100lbs) {
        this.amtPer100lbs = amtPer100lbs;
    }

    public Double getAmtPerCbm() {
        return this.amtPerCbm;
    }

    public void setAmtPerCbm(Double amtPerCbm) {
        this.amtPerCbm = amtPerCbm;
    }

    public Double getAmtPer1000kg() {
        return this.amtPer1000kg;
    }

    public void setAmtPer1000kg(Double amtPer1000kg) {
        this.amtPer1000kg = amtPer1000kg;
    }

    public Double getAmount() {
        return this.amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getPercentage() {
        return this.percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public Double getMinAmt() {
        return this.minAmt;
    }

    public void setMinAmt(Double minAmt) {
        this.minAmt = minAmt;
    }

    public Date getEffectiveDate() {
        return this.effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getChangedDate() {
        return this.changedDate;
    }

    public void setChangedDate(Date changedDate) {
        this.changedDate = changedDate;
    }

    public String getWhoChanged() {
        return this.whoChanged;
    }

    public void setWhoChanged(String whoChanged) {
        this.whoChanged = whoChanged;
    }

    public String getAsFrfgted() {
        return this.asFrfgted;
    }

    public void setAsFrfgted(String asFrfgted) {
        this.asFrfgted = asFrfgted;
    }

    public Double getInsuranceRate() {
        return this.insuranceRate;
    }

    public void setInsuranceRate(Double insuranceRate) {
        this.insuranceRate = insuranceRate;
    }

    public Double getInsuranceAmt() {
        return this.insuranceAmt;
    }

    public void setInsuranceAmt(Double insuranceAmt) {
        this.insuranceAmt = insuranceAmt;
    }

    public String getExclude() {
        return this.exclude;
    }

    public void setExclude(String exclude) {
        this.exclude = exclude;
    }

    public void setChargeCode(GenericCode chargeCode) {
        this.chargeCode = chargeCode;
    }

    public GenericCode getChargeCode() {
        return chargeCode;
    }

    public GenericCode getChargeType() {
        return chargeType;
    }

    public void setChargeType(GenericCode chargeType) {
        this.chargeType = chargeType;
    }
}