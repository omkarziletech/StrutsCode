/**
 *
 */
package com.gp.cong.logisoft.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Yogesh
 *
 */
public class RetailWeightRangesRatesHistory implements Auditable, Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer retailRatesId;
    private Double generalRate;
    private Double generalMinAmt;
    private Double expressRate;
    private Double expressMinAmt;
    private Double deferredRate;
    private Double deferredMinAmt;
    private Double firstOcean;
    private Double firstTt;
    private Double secondOcean;
    private Double secondTt;
    private Double thirdOcean;
    private Double thirdTt;
    private Double fourthOcean;
    private Double fourthTt;
    private String measureType;
    private Date effectiveDate;
    private Date changedDate;
    private String whoChanged;

    public Date getChangedDate() {
        return changedDate;
    }

    public void setChangedDate(Date changedDate) {
        this.changedDate = changedDate;
    }

    public String getWhoChanged() {
        return whoChanged;
    }

    public void setWhoChanged(String whoChanged) {
        this.whoChanged = whoChanged;
    }

    public Double getDeferredMinAmt() {
        return deferredMinAmt;
    }

    public void setDeferredMinAmt(Double deferredMinAmt) {
        this.deferredMinAmt = deferredMinAmt;
    }

    public Double getDeferredRate() {
        return deferredRate;
    }

    public void setDeferredRate(Double deferredRate) {
        this.deferredRate = deferredRate;
    }

    public Double getExpressMinAmt() {
        return expressMinAmt;
    }

    public void setExpressMinAmt(Double expressMinAmt) {
        this.expressMinAmt = expressMinAmt;
    }

    public Double getExpressRate() {
        return expressRate;
    }

    public void setExpressRate(Double expressRate) {
        this.expressRate = expressRate;
    }

    public Double getGeneralMinAmt() {
        return generalMinAmt;
    }

    public void setGeneralMinAmt(Double generalMinAmt) {
        this.generalMinAmt = generalMinAmt;
    }

    public Double getGeneralRate() {
        return generalRate;
    }

    public void setGeneralRate(Double generalRate) {
        this.generalRate = generalRate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRetailRatesId() {
        return retailRatesId;
    }

    public void setRetailRatesId(Integer retailRatesId) {
        this.retailRatesId = retailRatesId;
    }

    public AuditInfo getAuditInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getMeasureType() {
        return measureType;
    }

    public void setMeasureType(String measureType) {
        this.measureType = measureType;
    }

    public Double getFirstOcean() {
        return firstOcean;
    }

    public void setFirstOcean(Double firstOcean) {
        this.firstOcean = firstOcean;
    }

    public Double getFirstTt() {
        return firstTt;
    }

    public void setFirstTt(Double firstTt) {
        this.firstTt = firstTt;
    }

    public Double getFourthOcean() {
        return fourthOcean;
    }

    public void setFourthOcean(Double fourthOcean) {
        this.fourthOcean = fourthOcean;
    }

    public Double getFourthTt() {
        return fourthTt;
    }

    public void setFourthTt(Double fourthTt) {
        this.fourthTt = fourthTt;
    }

    public Double getSecondOcean() {
        return secondOcean;
    }

    public void setSecondOcean(Double secondOcean) {
        this.secondOcean = secondOcean;
    }

    public Double getSecondTt() {
        return secondTt;
    }

    public void setSecondTt(Double secondTt) {
        this.secondTt = secondTt;
    }

    public Double getThirdOcean() {
        return thirdOcean;
    }

    public void setThirdOcean(Double thirdOcean) {
        this.thirdOcean = thirdOcean;
    }

    public Double getThirdTt() {
        return thirdTt;
    }

    public void setThirdTt(Double thirdTt) {
        this.thirdTt = thirdTt;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
}
