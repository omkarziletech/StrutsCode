package com.logiware.lcl.model;

/**
 *
 * @author venugopal.s
 */
public class NotificationModel {

    private Long id;
    private Long fileNumberId;
    private String fileNumber;
    private Integer fromUserId;
    private String toAddress;
    private String fileStatus;
    private String subject;
    private Integer enterByUserId;
    private String docName;
    private String unitNo;
    private String toEmail;
    private Boolean nonRated;
    private Boolean nonUnRated;
    private Boolean freightInvoice;
    private Boolean cob;
    private String bussinessUnit;
    private String fromAddress;
    private String emailBody;
    private String toName;
    private String companyName;
    private String companyLogoPath;
    private String companyWebsiteName;
    private String accountNo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFileNumberId() {
        return fileNumberId;
    }

    public void setFileNumberId(Long fileNumberId) {
        this.fileNumberId = fileNumberId;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public Integer getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Integer fromUserId) {
        this.fromUserId = fromUserId;
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

    public Integer getEnterByUserId() {
        return enterByUserId;
    }

    public void setEnterByUserId(Integer enterByUserId) {
        this.enterByUserId = enterByUserId;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getUnitNo() {
        return unitNo;
    }

    public void setUnitNo(String unitNo) {
        this.unitNo = unitNo;
    }

    public Boolean getCob() {
        return cob;
    }

    public void setCob(Boolean cob) {
        this.cob = cob;
    }

    public Boolean getFreightInvoice() {
        return freightInvoice;
    }

    public void setFreightInvoice(Boolean freightInvoice) {
        this.freightInvoice = freightInvoice;
    }

    public Boolean getNonRated() {
        return nonRated;
    }

    public void setNonRated(Boolean nonRated) {
        this.nonRated = nonRated;
    }

    public Boolean getNonUnRated() {
        return nonUnRated;
    }

    public void setNonUnRated(Boolean nonUnRated) {
        this.nonUnRated = nonUnRated;
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public String getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(String fileStatus) {
        this.fileStatus = fileStatus;
    }

    public String getBussinessUnit() {
        return bussinessUnit;
    }

    public void setBussinessUnit(String bussinessUnit) {
        this.bussinessUnit = bussinessUnit;
    }

    public String getEmailBody() {
        return emailBody;
    }

    public void setEmailBody(String emailBody) {
        this.emailBody = emailBody;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getCompanyLogoPath() {
        return companyLogoPath;
    }

    public void setCompanyLogoPath(String companyLogoPath) {
        this.companyLogoPath = companyLogoPath;
    }

    public String getCompanyWebsiteName() {
        return companyWebsiteName;
    }

    public void setCompanyWebsiteName(String companyWebsiteName) {
        this.companyWebsiteName = companyWebsiteName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

}
