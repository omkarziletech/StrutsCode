package com.gp.cong.logisoft.domain;

import java.io.Serializable;
import java.util.Set;

public class UniversalMaster implements Serializable {

    private Integer id;
    private RefTerminalTemp originTerminal;
    private PortsTemp destinationPort;
    private GenericCode commodityCode;
    private Set UniversalFlat;
    private Set UniversalCommodity;
    private Set UniversalImport;
    private Set UniversalInsurance;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public GenericCode getCommodityCode() {
        return commodityCode;
    }

    public void setCommodityCode(GenericCode commodityCode) {
        this.commodityCode = commodityCode;
    }

    public PortsTemp getDestinationPort() {
        return destinationPort;
    }

    public void setDestinationPort(PortsTemp destinationPort) {
        this.destinationPort = destinationPort;
    }

    public RefTerminalTemp getOriginTerminal() {
        return originTerminal;
    }

    public void setOriginTerminal(RefTerminalTemp originTerminal) {
        this.originTerminal = originTerminal;
    }

    public Set getUniversalCommodity() {
        return UniversalCommodity;
    }

    public void setUniversalCommodity(Set universalCommodity) {
        UniversalCommodity = universalCommodity;
    }

    public Set getUniversalFlat() {
        return UniversalFlat;
    }

    public void setUniversalFlat(Set universalFlat) {
        UniversalFlat = universalFlat;
    }

    public Set getUniversalImport() {
        return UniversalImport;
    }

    public void setUniversalImport(Set universalImport) {
        UniversalImport = universalImport;
    }

    public Set getUniversalInsurance() {
        return UniversalInsurance;
    }

    public void setUniversalInsurance(Set universalInsurance) {
        UniversalInsurance = universalInsurance;
    }
}
