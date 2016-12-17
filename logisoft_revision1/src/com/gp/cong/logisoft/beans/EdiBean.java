package com.gp.cong.logisoft.beans;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import java.io.Serializable;

public class EdiBean implements Serializable {

    private String drNumberTxt;
    private String messageTypeTxt;
    private String ediCompanyTxt;
    private String match;
    private String buttonValue;
    private Integer id;
    private String drNumber;
    private String fileName;
    private String processedDate;
    private String fmtprocessedDate;
    private String status;
    private String ackRecievedDate;
    private String severity;
    private String bookingNumber;
    private String scacCode;
    private String ediCompany;
    private String messageType;
    private String description;
    private String eventName;
    private String docTyp;

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

    public void setProcessedDate(String processDate) throws Exception {
        if (CommonUtils.isNotEmpty(processDate)) {
            this.fmtprocessedDate = DateUtils.formatDate(DateUtils.parseDate(processDate, "yyyyMMddHHmmss"), "yyyy-MMM-dd HH:mm:ss");
        }
        this.processedDate = processDate;

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
            this.ackRecievedDate = DateUtils.formatDate(DateUtils.parseDate(ackRecievedDate, "yyyyMMddHHmmss"), "yyyy-MMM-dd HH:mm:ss");
        } else {
            this.ackRecievedDate = ackRecievedDate;
        }
    }

    public String getSeverity() {
        return null!=severity?severity.toUpperCase():null;
    }

    public void setSeverity(String severity) {
        this.severity = null!=severity?severity.toUpperCase():null;
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

    public String getFmtprocessedDate() {
        return fmtprocessedDate;
    }

    public void setFmtprocessedDate(String fmtprocessedDate) {
        this.fmtprocessedDate = fmtprocessedDate;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDocTyp() {
        return docTyp;
    }

    public void setDocTyp(String docTyp) {
        this.docTyp = docTyp;
    }
    
}
