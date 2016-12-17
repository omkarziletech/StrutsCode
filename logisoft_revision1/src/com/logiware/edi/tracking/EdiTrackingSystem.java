package com.logiware.edi.tracking;

import java.util.Set;

public class EdiTrackingSystem implements java.io.Serializable {

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
    private byte[] xml;
    private String portOfLoad;
    private String portOfDischarge;
    private String placeOfReceipt;
    private String placeOfDelivery;
    private String portOfLoadCity;
    private String portOfDischargeCity;
    private String placeOfReceiptCity;
    private String placeOfDeliveryCity;
    private String bookingNo;
    private String log;
    private byte[] xml997;
    private String ackStatus;
    private String ackCreatedDate;
    private String ediStatus;
    private String transportService;
    private String transactionStatus;
    private String requestor;

    public String getAckCreatedDate() {
        return ackCreatedDate;
    }

    public void setAckCreatedDate(String ackCreatedDate) {
        this.ackCreatedDate = ackCreatedDate;
    }

    public String getAckStatus() {
        return ackStatus;
    }

    public void setAckStatus(String ackStatus) {
        this.ackStatus = ackStatus;
    }

    public byte[] getXml997() {
        return xml997;
    }

    public void setXml997(byte[] xml997) {
        this.xml997 = xml997;
    }

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

    public byte[] getXml() {
        return xml;
    }

    public void setXml(byte[] xml) {
        this.xml = xml;
    }

    public String getBookingNo() {
        return bookingNo;
    }

    public void setBookingNo(String bookingNo) {
        this.bookingNo = bookingNo;
    }

    public String getPlaceOfDelivery() {
        return placeOfDelivery;
    }

    public void setPlaceOfDelivery(String placeOfDelivery) {
        this.placeOfDelivery = placeOfDelivery;
    }

    public String getPlaceOfDeliveryCity() {
        return placeOfDeliveryCity;
    }

    public void setPlaceOfDeliveryCity(String placeOfDeliveryCity) {
        this.placeOfDeliveryCity = placeOfDeliveryCity;
    }

    public String getPlaceOfReceipt() {
        return placeOfReceipt;
    }

    public void setPlaceOfReceipt(String placeOfReceipt) {
        this.placeOfReceipt = placeOfReceipt;
    }

    public String getPlaceOfReceiptCity() {
        return placeOfReceiptCity;
    }

    public void setPlaceOfReceiptCity(String placeOfReceiptCity) {
        this.placeOfReceiptCity = placeOfReceiptCity;
    }

    public String getPortOfDischarge() {
        return portOfDischarge;
    }

    public void setPortOfDischarge(String portOfDischarge) {
        this.portOfDischarge = portOfDischarge;
    }

    public String getPortOfDischargeCity() {
        return portOfDischargeCity;
    }

    public void setPortOfDischargeCity(String portOfDischargeCity) {
        this.portOfDischargeCity = portOfDischargeCity;
    }

    public String getPortOfLoad() {
        return portOfLoad;
    }

    public void setPortOfLoad(String portOfLoad) {
        this.portOfLoad = portOfLoad;
    }

    public String getPortOfLoadCity() {
        return portOfLoadCity;
    }

    public void setPortOfLoadCity(String portOfLoadCity) {
        this.portOfLoadCity = portOfLoadCity;
    }

    public String getEdiStatus() {
        return ediStatus;
    }

    public void setEdiStatus(String ediStatus) {
        this.ediStatus = ediStatus;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getTransportService() {
        return transportService;
    }

    public void setTransportService(String transportService) {
        this.transportService = transportService;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getRequestor() {
        return requestor;
    }

    public void setRequestor(String requestor) {
        this.requestor = requestor;
    }
}
