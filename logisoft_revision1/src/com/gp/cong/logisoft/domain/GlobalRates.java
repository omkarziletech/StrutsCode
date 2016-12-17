package com.gp.cong.logisoft.domain;

import java.io.Serializable;
import java.util.Date;

public class GlobalRates implements Serializable {

    private Integer id;
    private UnLocation originTerminal;
    private UnLocation destinationPort;
    private String originalRegion;
    private String destinationRegion;
    private GenericCode comNum;
    private String contractName;
    private GenericCode unitType;
    private String costCode;
    private Double futureAmount;
    private Date effectiveDate;
    private String amendmentType;
    private Date expiredDate;
    private String createdBy;
    private Date createdDate;
    private String status;

    public String getAmendmentType() {
        return amendmentType;
    }

    public void setAmendmentType(String amendmentType) {
        this.amendmentType = amendmentType;
    }

    public GenericCode getComNum() {
        return comNum;
    }

    public void setComNum(GenericCode comNum) {
        this.comNum = comNum;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getCostCode() {
        return costCode;
    }

    public void setCostCode(String costCode) {
        this.costCode = costCode;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public UnLocation getDestinationPort() {
        return destinationPort;
    }

    public void setDestinationPort(UnLocation destinationPort) {
        this.destinationPort = destinationPort;
    }

    public String getDestinationRegion() {
        return destinationRegion;
    }

    public void setDestinationRegion(String destinationRegion) {
        this.destinationRegion = destinationRegion;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }

    public Double getFutureAmount() {
        return futureAmount;
    }

    public void setFutureAmount(Double futureAmount) {
        this.futureAmount = futureAmount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOriginalRegion() {
        return originalRegion;
    }

    public void setOriginalRegion(String originalRegion) {
        this.originalRegion = originalRegion;
    }

    public UnLocation getOriginTerminal() {
        return originTerminal;
    }

    public void setOriginTerminal(UnLocation originTerminal) {
        this.originTerminal = originTerminal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public GenericCode getUnitType() {
        return unitType;
    }

    public void setUnitType(GenericCode unitType) {
        this.unitType = unitType;
    }
}
