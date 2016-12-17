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
public class LclBookingCommodityModel {

    private Long id;
    private String pieceDesc;
    private String markNoDesc;
    private boolean hazmat;
    private String personalEffects;
    private boolean refrigerationRequired;
    private boolean isBarrel;
    private String harmonizedCode;
    private Integer bookedPieceCount;
    private BigDecimal bookedWeightImperial;
    private BigDecimal bookedWeightMetric;
    private BigDecimal bookedVolumeImperial;
    private BigDecimal bookedVolumeMetric;
    private Integer actualPieceCount;
    private BigDecimal actualWeightImperial;
    private BigDecimal actualWeightMetric;
    private BigDecimal actualVolumeImperial;
    private BigDecimal actualVolumeMetric;
    private boolean weightVerified;
    private String stdchgRateBasis;
    private Long commodityId;
    private String code;
    private String descEn;
    private Long packageTypeId;
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPieceDesc() {
        return pieceDesc;
    }

    public void setPieceDesc(String pieceDesc) {
        this.pieceDesc = pieceDesc;
    }

    public String getMarkNoDesc() {
        return markNoDesc;
    }

    public void setMarkNoDesc(String markNoDesc) {
        this.markNoDesc = markNoDesc;
    }

    public boolean isHazmat() {
        return hazmat;
    }

    public void setHazmat(boolean hazmat) {
        this.hazmat = hazmat;
    }

    public String getPersonalEffects() {
        return personalEffects;
    }

    public void setPersonalEffects(String personalEffects) {
        this.personalEffects = personalEffects;
    }

    public boolean isRefrigerationRequired() {
        return refrigerationRequired;
    }

    public void setRefrigerationRequired(boolean refrigerationRequired) {
        this.refrigerationRequired = refrigerationRequired;
    }

    public boolean isIsBarrel() {
        return isBarrel;
    }

    public void setIsBarrel(boolean isBarrel) {
        this.isBarrel = isBarrel;
    }

    public String getHarmonizedCode() {
        return harmonizedCode;
    }

    public void setHarmonizedCode(String harmonizedCode) {
        this.harmonizedCode = harmonizedCode;
    }

    public Integer getBookedPieceCount() {
        return bookedPieceCount;
    }

    public void setBookedPieceCount(Integer bookedPieceCount) {
        this.bookedPieceCount = bookedPieceCount;
    }

    public BigDecimal getBookedWeightImperial() {
        return bookedWeightImperial;
    }

    public void setBookedWeightImperial(BigDecimal bookedWeightImperial) {
        this.bookedWeightImperial = bookedWeightImperial;
    }

    public BigDecimal getBookedWeightMetric() {
        return bookedWeightMetric;
    }

    public void setBookedWeightMetric(BigDecimal bookedWeightMetric) {
        this.bookedWeightMetric = bookedWeightMetric;
    }

    public BigDecimal getBookedVolumeImperial() {
        return bookedVolumeImperial;
    }

    public void setBookedVolumeImperial(BigDecimal bookedVolumeImperial) {
        this.bookedVolumeImperial = bookedVolumeImperial;
    }

    public BigDecimal getBookedVolumeMetric() {
        return bookedVolumeMetric;
    }

    public void setBookedVolumeMetric(BigDecimal bookedVolumeMetric) {
        this.bookedVolumeMetric = bookedVolumeMetric;
    }

    public Integer getActualPieceCount() {
        return actualPieceCount;
    }

    public void setActualPieceCount(Integer actualPieceCount) {
        this.actualPieceCount = actualPieceCount;
    }

    public BigDecimal getActualWeightImperial() {
        return actualWeightImperial;
    }

    public void setActualWeightImperial(BigDecimal actualWeightImperial) {
        this.actualWeightImperial = actualWeightImperial;
    }

    public BigDecimal getActualWeightMetric() {
        return actualWeightMetric;
    }

    public void setActualWeightMetric(BigDecimal actualWeightMetric) {
        this.actualWeightMetric = actualWeightMetric;
    }

    public BigDecimal getActualVolumeImperial() {
        return actualVolumeImperial;
    }

    public void setActualVolumeImperial(BigDecimal actualVolumeImperial) {
        this.actualVolumeImperial = actualVolumeImperial;
    }

    public BigDecimal getActualVolumeMetric() {
        return actualVolumeMetric;
    }

    public void setActualVolumeMetric(BigDecimal actualVolumeMetric) {
        this.actualVolumeMetric = actualVolumeMetric;
    }

    public boolean isWeightVerified() {
        return weightVerified;
    }

    public void setWeightVerified(boolean weightVerified) {
        this.weightVerified = weightVerified;
    }

    public String getStdchgRateBasis() {
        return stdchgRateBasis;
    }

    public void setStdchgRateBasis(String stdchgRateBasis) {
        this.stdchgRateBasis = stdchgRateBasis;
    }

    public Long getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(Long commodityId) {
        this.commodityId = commodityId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescEn() {
        return descEn;
    }

    public void setDescEn(String descEn) {
        this.descEn = descEn;
    }

    public Long getPackageTypeId() {
        return packageTypeId;
    }

    public void setPackageTypeId(Long packageTypeId) {
        this.packageTypeId = packageTypeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

   
    

}
