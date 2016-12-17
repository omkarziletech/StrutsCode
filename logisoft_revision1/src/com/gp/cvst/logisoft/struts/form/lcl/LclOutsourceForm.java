package com.gp.cvst.logisoft.struts.form.lcl;

public class LclOutsourceForm extends LogiwareActionForm {

    private String fileId;
    private String fileNumber;
    private String methodName;
    private String toAddress;
    private String subject;
    private String message;
    private String terminalnum;
    private String emailId;
    private String mailTo;
    private Long unitId;
    private Long ssHeaderId;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public String getMailTo() {
        return mailTo;
    }

    public void setMailTo(String mailTo) {
        this.mailTo = mailTo;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public Long getSsHeaderId() {
        return ssHeaderId;
    }

    public void setSsHeaderId(Long ssHeaderId) {
        this.ssHeaderId = ssHeaderId;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }
}
