package com.gp.cong.logisoft.domain;

import java.io.Serializable;
import java.util.Date;

public class HistoryRates implements Serializable {

    private Integer id;
    private UnLocation originTerminal;
    private UnLocation destinationPort;
    private String originalRegion;
    private String destinationRegion;
    private GenericCode comNum;
    private String contractName;
    private GenericCode unitType;
    private String costCode;
    private Double expiredAmount;
    private Date expiredDate;

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

    public Double getExpiredAmount() {
        return expiredAmount;
    }

    public void setExpiredAmount(Double expiredAmount) {
        this.expiredAmount = expiredAmount;
    }

    public Date getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
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

    public GenericCode getUnitType() {
        return unitType;
    }

    public void setUnitType(GenericCode unitType) {
        this.unitType = unitType;
    }
}
