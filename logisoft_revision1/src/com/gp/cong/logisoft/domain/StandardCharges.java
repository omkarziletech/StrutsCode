package com.gp.cong.logisoft.domain;

import java.io.Serializable;
import java.util.Set;

public class StandardCharges implements Auditable, Serializable {

    private Integer id;
    private GenericCode orgTerminal;
    private GenericCode destPort;
    private GenericCode comCode;
    private GenericCode scheduleTerminal;
    private Double maxDocCharge;
    private Double ffCommission;
    private Double blBottomLine;
    private Set airStandardCharges;
    private Set airDocumentCharges;
    private Set airFlightSchedules;
    private Set airWeightRangeSet;
    private Set airCommoditySet;

    public Set getAirCommoditySet() {
        return airCommoditySet;
    }

    public void setAirCommoditySet(Set airCommoditySet) {
        this.airCommoditySet = airCommoditySet;
    }

    public Set getAirWeightRangeSet() {

        return airWeightRangeSet;
    }

    public void setAirWeightRangeSet(Set airWeightRangeSet) {

        this.airWeightRangeSet = airWeightRangeSet;
    }

    public Set getAirDocumentCharges() {
        return airDocumentCharges;
    }

    public void setAirDocumentCharges(Set airDocumentCharges) {
        this.airDocumentCharges = airDocumentCharges;
    }

    public Set getAirFlightSchedules() {
        return airFlightSchedules;
    }

    public void setAirFlightSchedules(Set airFlightSchedules) {
        this.airFlightSchedules = airFlightSchedules;
    }

    public Set getAirStandardCharges() {
        return airStandardCharges;
    }

    public void setAirStandardCharges(Set airStandardCharges) {
        this.airStandardCharges = airStandardCharges;
    }

    public Double getBlBottomLine() {
        return blBottomLine;
    }

    public void setBlBottomLine(Double blBottomLine) {
        this.blBottomLine = blBottomLine;
    }

    public GenericCode getComCode() {
        return comCode;
    }

    public void setComCode(GenericCode comCode) {
        this.comCode = comCode;
    }

    public Double getFfCommission() {
        return ffCommission;
    }

    public void setFfCommission(Double ffCommission) {
        this.ffCommission = ffCommission;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getMaxDocCharge() {
        return maxDocCharge;
    }

    public void setMaxDocCharge(Double maxDocCharge) {
        this.maxDocCharge = maxDocCharge;
    }

    public GenericCode getDestPort() {
        return destPort;
    }

    public void setDestPort(GenericCode destPort) {
        this.destPort = destPort;
    }

    public GenericCode getOrgTerminal() {
        return orgTerminal;
    }

    public void setOrgTerminal(GenericCode orgTerminal) {
        this.orgTerminal = orgTerminal;
    }

    public AuditInfo getAuditInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    public GenericCode getScheduleTerminal() {
        return scheduleTerminal;
    }

    public void setScheduleTerminal(GenericCode scheduleTerminal) {
        this.scheduleTerminal = scheduleTerminal;
    }
}
