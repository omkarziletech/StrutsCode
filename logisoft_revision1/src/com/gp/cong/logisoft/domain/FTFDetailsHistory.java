package com.gp.cong.logisoft.domain;

import java.io.Serializable;
import java.util.Date;

public class FTFDetailsHistory implements Serializable {

    private Integer id;
    private Integer FtfId;
    private Double metric1000kg;
    private Double metricCbm;
    private Double metricOfMinamt;
    private Double english100lb;
    private Double englishCft;
    private Double englishOfMinamt;
    private Double sizeAOf;
    private Double sizeATt;
    private Double sizeBOf;
    private Double sizeBTt;
    private Double sizeCOf;
    private Double sizeCTt;
    private Double sizeDOf;
    private Double sizeDTt;
    private String measureType;
    private Date changedDate;
    private String whoChanged;
    private Date effectiveDate;

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

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

    public Double getEnglish100lb() {
        return english100lb;
    }

    public void setEnglish100lb(Double english100lb) {
        this.english100lb = english100lb;
    }

    public Double getEnglishCft() {
        return englishCft;
    }

    public void setEnglishCft(Double englishCft) {
        this.englishCft = englishCft;
    }

    public Double getEnglishOfMinamt() {
        return englishOfMinamt;
    }

    public void setEnglishOfMinamt(Double englishOfMinamt) {
        this.englishOfMinamt = englishOfMinamt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFtfId() {
        return FtfId;
    }

    public void setFtfId(Integer ftfId) {
        FtfId = ftfId;
    }

    public String getMeasureType() {
        return measureType;
    }

    public void setMeasureType(String measureType) {
        this.measureType = measureType;
    }

    public Double getMetric1000kg() {
        return metric1000kg;
    }

    public void setMetric1000kg(Double metric1000kg) {
        this.metric1000kg = metric1000kg;
    }

    public Double getMetricCbm() {
        return metricCbm;
    }

    public void setMetricCbm(Double metricCbm) {
        this.metricCbm = metricCbm;
    }

    public Double getMetricOfMinamt() {
        return metricOfMinamt;
    }

    public void setMetricOfMinamt(Double metricOfMinamt) {
        this.metricOfMinamt = metricOfMinamt;
    }

    public Double getSizeAOf() {
        return sizeAOf;
    }

    public void setSizeAOf(Double sizeAOf) {
        this.sizeAOf = sizeAOf;
    }

    public Double getSizeATt() {
        return sizeATt;
    }

    public void setSizeATt(Double sizeATt) {
        this.sizeATt = sizeATt;
    }

    public Double getSizeBOf() {
        return sizeBOf;
    }

    public void setSizeBOf(Double sizeBOf) {
        this.sizeBOf = sizeBOf;
    }

    public Double getSizeBTt() {
        return sizeBTt;
    }

    public void setSizeBTt(Double sizeBTt) {
        this.sizeBTt = sizeBTt;
    }

    public Double getSizeCOf() {
        return sizeCOf;
    }

    public void setSizeCOf(Double sizeCOf) {
        this.sizeCOf = sizeCOf;
    }

    public Double getSizeCTt() {
        return sizeCTt;
    }

    public void setSizeCTt(Double sizeCTt) {
        this.sizeCTt = sizeCTt;
    }

    public Double getSizeDOf() {
        return sizeDOf;
    }

    public void setSizeDOf(Double sizeDOf) {
        this.sizeDOf = sizeDOf;
    }

    public Double getSizeDTt() {
        return sizeDTt;
    }

    public void setSizeDTt(Double sizeDTt) {
        this.sizeDTt = sizeDTt;
    }
}
