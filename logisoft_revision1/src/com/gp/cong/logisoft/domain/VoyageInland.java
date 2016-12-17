package com.gp.cong.logisoft.domain;

import java.io.Serializable;
import java.util.*;

public class VoyageInland implements Auditable, Serializable {

    // Fields    
    private Integer Id;
    private Integer voyageId;
    private RefTerminalTemp originTerminal;
    private Date dateTerminalArrival;
    private Integer inlandVoyageNo;
    private Date dateLoaded;
    private Date dateOfDeparture;
    private RefTerminalTemp scheduleDkOrigin;
    private PortsTemp scheduleDkDestination;
    private PortsTemp optItPortNo;
    private GenericCode vesselNo;
    private Date deliveryCutOffDate;
    // private CarriersOrLineTemp sslVoyageNo;
    private String sslVoyageNo;
    //private RefTerminalTemp destTerminal;
    private PortsTemp destTerminal;
    // Constructors
    //closed
    private String audited;
    private String auditedTime;
    private String auditedUser;
    private Date auditedDate;
    private String closed;
    private String closedTime;
    private Date closedDate;
    private String closedUser;

    /**
     * default constructor
     */
    public VoyageInland() {
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {

        Id = id;

    }

    public Date getDateLoaded() {
        return dateLoaded;
    }

    public void setDateLoaded(Date dateLoaded) {
        this.dateLoaded = dateLoaded;
    }

    public Date getDateOfDeparture() {
        return dateOfDeparture;
    }

    public void setDateOfDeparture(Date dateOfDeparture) {
        this.dateOfDeparture = dateOfDeparture;
    }

    public Date getDateTerminalArrival() {
        return dateTerminalArrival;
    }

    public void setDateTerminalArrival(Date dateTerminalArrival) {
        this.dateTerminalArrival = dateTerminalArrival;
    }

    public Date getDeliveryCutOffDate() {
        return deliveryCutOffDate;
    }

    public void setDeliveryCutOffDate(Date deliveryCutOffDate) {
        this.deliveryCutOffDate = deliveryCutOffDate;
    }

    public Integer getInlandVoyageNo() {
        return inlandVoyageNo;
    }

    public void setInlandVoyageNo(Integer inlandVoyageNo) {
        this.inlandVoyageNo = inlandVoyageNo;
    }

    public PortsTemp getOptItPortNo() {
        return optItPortNo;
    }

    public void setOptItPortNo(PortsTemp optItPortNo) {
        this.optItPortNo = optItPortNo;
    }

    /*public CarriersOrLineTemp getSslVoyageNo() {
     return sslVoyageNo;
     }


     public void setSslVoyageNo(CarriersOrLineTemp sslVoyageNo) {
     this.sslVoyageNo = sslVoyageNo;
     }*/
    public PortsTemp getScheduleDkDestination() {
        return scheduleDkDestination;
    }

    public void setScheduleDkDestination(PortsTemp scheduleDkDestination) {
        this.scheduleDkDestination = scheduleDkDestination;
    }

    public RefTerminalTemp getScheduleDkOrigin() {
        return scheduleDkOrigin;
    }

    public void setScheduleDkOrigin(RefTerminalTemp scheduleDkOrigin) {
        this.scheduleDkOrigin = scheduleDkOrigin;
    }

    public GenericCode getVesselNo() {
        return vesselNo;
    }

    public void setVesselNo(GenericCode vesselNo) {
        this.vesselNo = vesselNo;
    }

    public Integer getVoyageId() {
        return voyageId;
    }

    public void setVoyageId(Integer voyageId) {
        this.voyageId = voyageId;
    }

    public RefTerminalTemp getOriginTerminal() {
        return originTerminal;
    }

    public void setOriginTerminal(RefTerminalTemp originTerminal) {
        this.originTerminal = originTerminal;
    }

    public PortsTemp getDestTerminal() {
        return destTerminal;
    }

    public void setDestTerminal(PortsTemp destTerminal) {
        this.destTerminal = destTerminal;
    }

    public String getSslVoyageNo() {
        return sslVoyageNo;
    }

    public void setSslVoyageNo(String sslVoyageNo) {
        this.sslVoyageNo = sslVoyageNo;
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

    public AuditInfo getAuditInfo() {
        // TODO Auto-generated method stub
        return null;
    }
    /*public RefTerminalTemp getDestTerminal() {
     return destTerminal;
     }


     public void setDestTerminal(RefTerminalTemp destTerminal) {
     this.destTerminal = destTerminal;
     }*/
    /**
     * minimal constructor
     */
}