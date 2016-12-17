package com.gp.cong.logisoft.domain;

import java.io.Serializable;
import java.util.*;

public class VoyageExport implements Auditable, Serializable {

    // Fields    
    private Integer Id;
    private String internalVoyage;
    private RefTerminalTemp originTerminal;
    private PortsTemp destinationPort;
    private GenericCode vesselNo;
    private Date deliveryCutOffDate;
    private Date sailDate;
    private CarriersOrLineTemp lineNo;
    private String pierNo;
    private String fligtSsVoyage;
    private Integer voyageStdId;
    private String sslBookingNo;
    private String showSailingSchedule;
    private String time;
    private Integer currentSequenceNo;
    private PortsTemp portOfDischarge;
    private String transShipments;
    private String transitDaysOverride;
    private String agentForVoyage;
    private String truckingInfo;
    //private CarriersOrLineTemp ssLineNo;
    private Set exportSet;
    private String unitType;
    //private CarriersOrLineTemp sslineNo;
    private GenericCode comNum;
    // Constructors
    //closed
    private String closed;
    private Date closedDate;
    private String closedUser;
    private String closedTime;
    private String audited;
    private Date auditedDate;
    private String auditedUser;
    private String auditedTime;
    private String voyageChange;

    public GenericCode getComNum() {
        return comNum;
    }

    public void setComNum(GenericCode comNum) {
        this.comNum = comNum;
    }


    /*public CarriersOrLineTemp getSslineNo() {
     return sslineNo;
     }


     public void setSslineNo(CarriersOrLineTemp sslineNo) {
     this.sslineNo = sslineNo;
     }*/
    public Set getExportSet() {
        return exportSet;
    }

    public void setExportSet(Set exportSet) {
        this.exportSet = exportSet;
    }

    /**
     * default constructor
     */
    public VoyageExport() {
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Date getDeliveryCutOffDate() {
        return deliveryCutOffDate;
    }

    public void setDeliveryCutOffDate(Date deliveryCutOffDate) {
        this.deliveryCutOffDate = deliveryCutOffDate;
    }

    public String getAgentForVoyage() {
        return agentForVoyage;
    }

    public void setAgentForVoyage(String agentForVoyage) {
        this.agentForVoyage = agentForVoyage;
    }

    public Integer getCurrentSequenceNo() {
        return currentSequenceNo;
    }

    public void setCurrentSequenceNo(Integer currentSequenceNo) {
        this.currentSequenceNo = currentSequenceNo;
    }

    public PortsTemp getDestinationPort() {
        return destinationPort;
    }

    public void setDestinationPort(PortsTemp destinationPort) {
        this.destinationPort = destinationPort;
    }

    public String getFligtSsVoyage() {
        return fligtSsVoyage;
    }

    public void setFligtSsVoyage(String fligtSsVoyage) {
        this.fligtSsVoyage = fligtSsVoyage;
    }

    public String getInternalVoyage() {
        return internalVoyage;
    }

    public void setInternalVoyage(String internalVoyage) {
        this.internalVoyage = internalVoyage;
    }

    public CarriersOrLineTemp getLineNo() {
        return lineNo;
    }

    public void setLineNo(CarriersOrLineTemp lineNo) {
        this.lineNo = lineNo;
    }

    public String getPierNo() {
        return pierNo;
    }

    public void setPierNo(String pierNo) {
        this.pierNo = pierNo;
    }

    public PortsTemp getPortOfDischarge() {
        return portOfDischarge;
    }

    public void setPortOfDischarge(PortsTemp portOfDischarge) {
        this.portOfDischarge = portOfDischarge;
    }

    public Date getSailDate() {
        return sailDate;
    }

    public void setSailDate(Date sailDate) {
        this.sailDate = sailDate;
    }

    public String getShowSailingSchedule() {
        return showSailingSchedule;
    }

    public void setShowSailingSchedule(String showSailingSchedule) {
        this.showSailingSchedule = showSailingSchedule;
    }

    public String getSslBookingNo() {
        return sslBookingNo;
    }

    public void setSslBookingNo(String sslBookingNo) {
        this.sslBookingNo = sslBookingNo;
    }


    /*public CarriersOrLineTemp getSsLineNo() {
     return ssLineNo;
     }


     public void setSsLineNo(CarriersOrLineTemp ssLineNo) {
     this.ssLineNo = ssLineNo;
     }*/
    public String getTransitDaysOverride() {
        return transitDaysOverride;
    }

    public void setTransitDaysOverride(String transitDaysOverride) {
        this.transitDaysOverride = transitDaysOverride;
    }

    public String getTransShipments() {
        return transShipments;
    }

    public void setTransShipments(String transShipments) {
        this.transShipments = transShipments;
    }

    public String getTruckingInfo() {
        return truckingInfo;
    }

    public void setTruckingInfo(String truckingInfo) {
        this.truckingInfo = truckingInfo;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public GenericCode getVesselNo() {
        return vesselNo;
    }

    public void setVesselNo(GenericCode vesselNo) {
        this.vesselNo = vesselNo;
    }

    public RefTerminalTemp getOriginTerminal() {
        return originTerminal;
    }

    public void setOriginTerminal(RefTerminalTemp originTerminal) {
        this.originTerminal = originTerminal;
    }

    public Integer getVoyageStdId() {
        return voyageStdId;
    }

    public void setVoyageStdId(Integer voyageStdId) {
        this.voyageStdId = voyageStdId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getClosed() {
        return closed;
    }

    public void setClosed(String closed) {
        this.closed = closed;
    }

    public Date getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(Date closedDate) {
        this.closedDate = closedDate;
    }

    public String getClosedTime() {
        return closedTime;
    }

    public void setClosedTime(String closedTime) {
        this.closedTime = closedTime;
    }

    public String getClosedUser() {
        return closedUser;
    }

    public void setClosedUser(String closedUser) {
        this.closedUser = closedUser;
    }

    public String getAudited() {
        return audited;
    }

    public void setAudited(String audited) {
        this.audited = audited;
    }

    public Date getAuditedDate() {
        return auditedDate;
    }

    public void setAuditedDate(Date auditedDate) {
        this.auditedDate = auditedDate;
    }

    public String getAuditedTime() {
        return auditedTime;
    }

    public void setAuditedTime(String auditedTime) {
        this.auditedTime = auditedTime;
    }

    public String getAuditedUser() {
        return auditedUser;
    }

    public void setAuditedUser(String auditedUser) {
        this.auditedUser = auditedUser;
    }

    public AuditInfo getAuditInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getVoyageChange() {
        return voyageChange;
    }

    public void setVoyageChange(String voyageChange) {
        this.voyageChange = voyageChange;
    }
    /*public CarriersOrLineTemp getSsLineNo() {
     return ssLineNo;
     }


     public void setSsLineNo(CarriersOrLineTemp ssLineNo) {
     this.ssLineNo = ssLineNo;
     }


     public CarriersOrLineTemp getSslineNo() {
     return sslineNo;
     }


     public void setSslineNo(CarriersOrLineTemp sslineNo) {
     this.sslineNo = sslineNo;
     }

     */
    /**
     * minimal constructor
     */
}