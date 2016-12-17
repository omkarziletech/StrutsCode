package com.gp.cong.logisoft.domain;

import java.io.Serializable;

public class FclOrgDestMiscData implements Serializable {

    private Integer id;
    private UnLocation originalTerminal;
    private UnLocation destinationPort;
    private String localDrayage;
    private Integer daysInTransit;
    private String remarks;
    private TradingPartnerTemp sslineNo;
    private String poe;

    public String getPoe() {
        return poe;
    }

    public void setPoe(String poe) {
        this.poe = poe;
    }

    public UnLocation getOriginalTerminal() {
        return originalTerminal;
    }

    public void setOriginalTerminal(UnLocation originalTerminal) {
        this.originalTerminal = originalTerminal;
    }

    public UnLocation getDestinationPort() {
        return destinationPort;
    }

    public void setDestinationPort(UnLocation destinationPort) {
        this.destinationPort = destinationPort;
    }

    public String getLocalDrayage() {
        return localDrayage;
    }

    public void setLocalDrayage(String localDrayage) {
        this.localDrayage = localDrayage;
    }

    public Integer getDaysInTransit() {
        return daysInTransit;
    }

    public void setDaysInTransit(Integer daysInTransit) {
        this.daysInTransit = daysInTransit;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TradingPartnerTemp getSslineNo() {
        return sslineNo;
    }

    public void setSslineNo(TradingPartnerTemp sslineNo) {
        this.sslineNo = sslineNo;
    }
}
