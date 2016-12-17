package com.gp.cong.logisoft.domain;

import java.util.Date;
import java.util.Set;

public class LogFileEdi implements java.io.Serializable {

    private Integer id;
    private String filename;
    private String processedDate;
    private String status;
    private String description;
    private String ediCompany;
    private String messageType;
    private String drnumber;
    private Set ackname;
    private String match;
    private String trackingStatus;
    private String bookingNumber;
    private String ackStatus;
    private String fileNo;
    private Date ackReceivedDate;
    private String eventCode;
    private String eventName;
    private String vesselName;
    private String referenceNumber;
    private String voyageNo;
    private String transactionStatus;
    private String shipmentId;
    private String docType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getProcessedDate() {
        return processedDate;
    }

    public void setProcessedDate(String processedDate) {
        this.processedDate = processedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEdiCompany() {
        return ediCompany;
    }

    public void setEdiCompany(String ediCompany) {
        this.ediCompany = ediCompany;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getDrnumber() {
        return drnumber;
    }

    public void setDrnumber(String drnumber) {
        this.drnumber = drnumber;
    }

    public Set getAckname() {
        return ackname;
    }

    public void setAckname(Set ackname) {
        this.ackname = ackname;
    }

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public String getTrackingStatus() {
        return trackingStatus;
    }

    public void setTrackingStatus(String trackingStatus) {
        this.trackingStatus = trackingStatus;
    }

    public Date getAckReceivedDate() {
        return ackReceivedDate;
    }

    public void setAckReceivedDate(Date ackReceivedDate) {
        this.ackReceivedDate = ackReceivedDate;
    }

    public String getAckStatus() {
        return ackStatus;
    }

    public void setAckStatus(String ackStatus) {
        this.ackStatus = ackStatus;
    }

    public String getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(String bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getVesselName() {
        return vesselName;
    }

    public void setVesselName(String vesselName) {
        this.vesselName = vesselName;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getVoyageNo() {
        return voyageNo;
    }

    public void setVoyageNo(String voyageNo) {
        this.voyageNo = voyageNo;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }
    
}
