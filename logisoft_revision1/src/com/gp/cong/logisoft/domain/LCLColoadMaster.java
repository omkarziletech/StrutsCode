package com.gp.cong.logisoft.domain;

import java.io.Serializable;
import java.util.Set;

public class LCLColoadMaster implements Serializable {

    private Integer id;
    private String originTerminal;
    private String destinationPort;
    private String commodityCode;
    private String originTerminalName;
    private String destinationPortName;
    private String commodityCodeName;
    private Double maxDocCharge;
    private Double ffCommission;
    private Double blBottomLine;
    private Set lclColoadDetailsSet;
    private Set lclColoadDocumentSet;
    private Set lclColoadStdChgSet;
    private Set lclColoadCommChgSet;

    public Set getLclColoadCommChgSet() {
        return lclColoadCommChgSet;
    }

    public void setLclColoadCommChgSet(Set lclColoadCommChgSet) {
        this.lclColoadCommChgSet = lclColoadCommChgSet;
    }

    public Set getLclColoadDetailsSet() {
        return lclColoadDetailsSet;
    }

    public void setLclColoadDetailsSet(Set lclColoadDetailsSet) {
        this.lclColoadDetailsSet = lclColoadDetailsSet;
    }

    public Set getLclColoadDocumentSet() {
        return lclColoadDocumentSet;
    }

    public void setLclColoadDocumentSet(Set lclColoadDocumentSet) {
        this.lclColoadDocumentSet = lclColoadDocumentSet;
    }

    public Set getLclColoadStdChgSet() {
        return lclColoadStdChgSet;
    }

    public void setLclColoadStdChgSet(Set lclColoadStdChgSet) {
        this.lclColoadStdChgSet = lclColoadStdChgSet;
    }

    public Double getBlBottomLine() {
        return blBottomLine;
    }

    public void setBlBottomLine(Double blBottomLine) {
        this.blBottomLine = blBottomLine;
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

    public String getCommodityCode() {
        return commodityCode;
    }

    public void setCommodityCode(String commodityCode) {
        this.commodityCode = commodityCode;
    }

    public String getCommodityCodeName() {
        return commodityCodeName;
    }

    public void setCommodityCodeName(String commodityCodeName) {
        this.commodityCodeName = commodityCodeName;
    }

    public String getDestinationPort() {
        return destinationPort;
    }

    public void setDestinationPort(String destinationPort) {
        this.destinationPort = destinationPort;
    }

    public String getDestinationPortName() {
        return destinationPortName;
    }

    public void setDestinationPortName(String destinationPortName) {
        this.destinationPortName = destinationPortName;
    }

    public String getOriginTerminal() {
        return originTerminal;
    }

    public void setOriginTerminal(String originTerminal) {
        this.originTerminal = originTerminal;
    }

    public String getOriginTerminalName() {
        return originTerminalName;
    }

    public void setOriginTerminalName(String originTerminalName) {
        this.originTerminalName = originTerminalName;
    }
}
