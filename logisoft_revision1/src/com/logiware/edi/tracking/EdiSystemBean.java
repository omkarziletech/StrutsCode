package com.logiware.edi.tracking;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;

public class EdiSystemBean {

    private String drNumberTxt;
    private String messageTypeTxt;
    private String ediCompanyTxt;
    private String match;
    private String buttonValue;
    private Integer id;
    private String drNumber;
    private String fileName;
    private String processedDate;
    private String status;
    private String ackRecievedDate;
    private String severity;
    private String bookingNumber;
    private String scacCode;
    private String ediCompany;
    private String messageType;
    private String description;
    private String portOfLoad;
    private String portOfDischarge;
    private String placeOfReceipt;
    private String placeOfDelivery;
    private String portOfLoadCity;
    private String portOfDischargeCity;
    private String placeOfReceiptCity;
    private String placeOfDeliveryCity;
    private String ackStatus;
    private String ediStatus;
    private String transportService;
    private String transactionStatus;
    private String requestor;

    public String getAckStatus() {
        return ackStatus;
    }

    public void setAckStatus(String ackStatus) {
        this.ackStatus = ackStatus;
    }

    public String getDrNumberTxt() {
        return drNumberTxt;
    }

    public void setDrNumberTxt(String drNumberTxt) {
        this.drNumberTxt = drNumberTxt;
    }

    public String getMessageTypeTxt() {
        return messageTypeTxt;
    }

    public void setMessageTypeTxt(String messageTypeTxt) {
        this.messageTypeTxt = messageTypeTxt;
    }

    public String getEdiCompanyTxt() {
        return ediCompanyTxt;
    }

    public void setEdiCompanyTxt(String ediCompanyTxt) {
        this.ediCompanyTxt = ediCompanyTxt;
    }

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public String getButtonValue() {
        return buttonValue;
    }

    public void setButtonValue(String buttonValue) {
        this.buttonValue = buttonValue;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDrNumber() {
        return drNumber;
    }

    public void setDrNumber(String drNumber) {
        this.drNumber = drNumber;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getProcessedDate() {
        return processedDate;
    }

    public void setProcessedDate(String processedDate) throws Exception {
        if (CommonUtils.isNotEmpty(processedDate)) {
            processedDate = DateUtils.formatDate(DateUtils.parseDate(processedDate, "yyyyMMddHHmmss"), "yyyy-MMM-dd HH:mm:ss");
        }
        this.processedDate = processedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAckRecievedDate() {
        return ackRecievedDate;
    }

    public void setAckRecievedDate(String ackRecievedDate) throws Exception {
        if (CommonUtils.isNotEmpty(ackRecievedDate)) {
            ackRecievedDate = DateUtils.formatDate(DateUtils.parseDate(ackRecievedDate, "yyyyMMddHHmmss"), "yyyy-MMM-dd HH:mm:ss");
        }
        this.ackRecievedDate = ackRecievedDate;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(String bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public String getScacCode() {
        return scacCode;
    }

    public void setScacCode(String scacCode) {
        this.scacCode = scacCode;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getRequestor() {
        return requestor;
    }

    public void setRequestor(String requestor) {
        this.requestor = requestor;
    }

}
