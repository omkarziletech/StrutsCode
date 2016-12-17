package com.gp.cong.logisoft.domain;

import java.util.*;

public class VoyageMaster implements java.io.Serializable {

    // Fields    
    private Integer voyageStdId;
    private RefTerminalTemp originTerminal;
    private PortsTemp destinationPort;
    private GenericCode comNum;
    private CarriersOrLineTemp sslineNo;
    private Set exportSet;
    private Set inlandSet;

    // Constructors
    /**
     * default constructor
     */
    public VoyageMaster() {
    }

    /**
     * minimal constructor
     */
    public RefTerminalTemp getOriginTerminal() {
        return originTerminal;
    }

    public void setOriginTerminal(RefTerminalTemp originTerminal) {
        this.originTerminal = originTerminal;
    }

    public PortsTemp getDestinationPort() {
        return destinationPort;
    }

    public void setDestinationPort(PortsTemp destinationPort) {
        this.destinationPort = destinationPort;
    }

    public Integer getVoyageStdId() {
        return voyageStdId;
    }

    public void setVoyageStdId(Integer voyageStdId) {
        this.voyageStdId = voyageStdId;
    }

    public CarriersOrLineTemp getSslineNo() {
        return sslineNo;
    }

    public void setSslineNo(CarriersOrLineTemp sslineNo) {
        this.sslineNo = sslineNo;
    }

    public Set getExportSet() {
        return exportSet;
    }

    public void setExportSet(Set exportSet) {
        this.exportSet = exportSet;
    }

    public Set getInlandSet() {
        return inlandSet;
    }

    public void setInlandSet(Set inlandSet) {
        this.inlandSet = inlandSet;
    }

    public GenericCode getComNum() {
        return comNum;
    }

    public void setComNum(GenericCode comNum) {
        this.comNum = comNum;
    }
}