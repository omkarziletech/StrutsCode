package com.gp.cvst.logisoft.domain;

import java.util.Date;
import java.util.Set;

/**
 * FclBl generated by MyEclipse Persistence Tools
 */
public class FclBlMarks implements java.io.Serializable {

    // Fields
    private Integer id;
    private String markNo;
    private Integer noOfPkgs;
    private Double weightKgs;
    private Double weightLbs;
    private Double measureCbm;
    private Double measureCft;
    private String descPckgs;
    private String descForMasterBl;
    private String uom;
    private Integer trailerNoId;
    private Double netweightKgs;
    private Double netweightLbs;
    private Set fclMastDescset;
    private Set fclHouseDescset;
    private String houseBlTempString;
    private String masterBlTempString;
    private String copyDescription;
    private String updateBy;
    private Double tareWeightKgs;
    private Double tareWeightLbs;
    private Double bottomLineVgmWeightKgs;
    private Double bottomLineVgmWeightLbs;
    private String verificationSignature;
    private Date verificationDate;

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getCopyDescription() {
        return copyDescription;
    }

    public void setCopyDescription(String copyDescription) {
        this.copyDescription = copyDescription;
    }

    public String getHouseBlTempString() {
        return houseBlTempString;
    }

    public void setHouseBlTempString(String houseBlTempString) {
        this.houseBlTempString = houseBlTempString;
    }

    public String getMasterBlTempString() {
        return masterBlTempString;
    }

    public void setMasterBlTempString(String masterBlTempString) {
        this.masterBlTempString = masterBlTempString;
    }

    public Integer getTrailerNoId() {
        return trailerNoId;
    }

    public void setTrailerNoId(Integer trailerNoId) {
        this.trailerNoId = trailerNoId;
    }

    public String getDescPckgs() {
        return descPckgs;
    }

    public void setDescPckgs(String descPckgs) {
        this.descPckgs = descPckgs;
    }

    public String getMarkNo() {
        return markNo;
    }

    public void setMarkNo(String markNo) {
        this.markNo = markNo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNoOfPkgs() {
        return noOfPkgs;
    }

    public void setNoOfPkgs(Integer noOfPkgs) {
        this.noOfPkgs = noOfPkgs;
    }

    public Double getMeasureCbm() {
        return measureCbm;
    }

    public void setMeasureCbm(Double measureCbm) {
        this.measureCbm = measureCbm;
    }

    public Double getMeasureCft() {
        return measureCft;
    }

    public void setMeasureCft(Double measureCft) {
        this.measureCft = measureCft;
    }

    public Double getWeightKgs() {
        return weightKgs;
    }

    public void setWeightKgs(Double weightKgs) {
        this.weightKgs = weightKgs;
    }

    public Double getWeightLbs() {
        return weightLbs;
    }

    public void setWeightLbs(Double weightLbs) {
        this.weightLbs = weightLbs;
    }

    public Double getNetweightKgs() {
        return netweightKgs;
    }

    public void setNetweightKgs(Double netweightKgs) {
        this.netweightKgs = netweightKgs;
    }

    public Double getNetweightLbs() {
        return netweightLbs;
    }

    public void setNetweightLbs(Double netweightLbs) {
        this.netweightLbs = netweightLbs;
    }

    public String getDescForMasterBl() {
        return descForMasterBl;
    }

    public void setDescForMasterBl(String descForMasterBl) {
        this.descForMasterBl = descForMasterBl;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public Set getFclMastDescset() {
        return fclMastDescset;
    }

    public void setFclMastDescset(Set fclMastDescset) {
        this.fclMastDescset = fclMastDescset;
    }

    public Set getFclHouseDescset() {
        return fclHouseDescset;
    }

    public void setFclHouseDescset(Set fclHouseDescset) {
        this.fclHouseDescset = fclHouseDescset;
    }

    public Double getTareWeightKgs() {
        return tareWeightKgs;
    }

    public void setTareWeightKgs(Double tareWeightKgs) {
        this.tareWeightKgs = tareWeightKgs;
    }

    public Double getTareWeightLbs() {
        return tareWeightLbs;
    }

    public void setTareWeightLbs(Double tareWeightLbs) {
        this.tareWeightLbs = tareWeightLbs;
    }

    public Double getBottomLineVgmWeightKgs() {
        return bottomLineVgmWeightKgs;
    }

    public void setBottomLineVgmWeightKgs(Double bottomLineVgmWeightKgs) {
        this.bottomLineVgmWeightKgs = bottomLineVgmWeightKgs;
    }

    public Double getBottomLineVgmWeightLbs() {
        return bottomLineVgmWeightLbs;
    }

    public void setBottomLineVgmWeightLbs(Double bottomLineVgmWeightLbs) {
        this.bottomLineVgmWeightLbs = bottomLineVgmWeightLbs;
    }

    public String getVerificationSignature() {
        return verificationSignature;
    }

    public void setVerificationSignature(String verificationSignature) {
        this.verificationSignature = verificationSignature;
    }

    public Date getVerificationDate() {
        return verificationDate;
    }

    public void setVerificationDate(Date verificationDate) {
        this.verificationDate = verificationDate;
    }
}
