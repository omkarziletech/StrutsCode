/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.form;

import com.logiware.utils.AchSetUpUtils;
import org.apache.struts.upload.FormFile;

/**
 *
 * @author Lakshminarayanan
 */
public class AchSetUpForm extends org.apache.struts.action.ActionForm {

    private static final long serialVersionUID = 2735084847737097602L;
    private String bankId;
    private String fileHeaderRecordTypeCode;
    private String priorityCode;
    private String immediateDestination;
    private String immediateOrigin;
    private String fileIdModifier;
    private String recordSize;
    private String blockingFactor;
    private String formatCode;
    private String immediateDestinationName;
    private String batchHeaderRecordTypeCode;
    private String serviceClassCode;
    private String companyName;
    private String companyIdentification;
    private String standardEntryClass;
    private String companyEntryDescription;
    private String originatorStatusCode;
    private String entryDetailRecordTypeCode;
    private String transactionCode;
    private String addendaRecordIndicator;
    private String ftpHost;
    private String ftpUserName;
    private String ftpPassword;
    private FormFile publicKeyFile;
    private Boolean havePublicKey;
    private String buttonAction;
    private Boolean hasSshPrivateKey;
    private FormFile sshPrivateKeyFile;
    private String sshPassphrase;
    private Integer ftpPort;
    private String ftpDirectory;

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getBlockingFactor() {
        return blockingFactor;
    }

    public void setBlockingFactor(String blockingFactor) {
        this.blockingFactor = blockingFactor;
    }

    public String getFileHeaderRecordTypeCode() {
        return fileHeaderRecordTypeCode;
    }

    public void setFileHeaderRecordTypeCode(String fileHeaderRecordTypeCode) {
        this.fileHeaderRecordTypeCode = fileHeaderRecordTypeCode;
    }

    public String getFileIdModifier() {
        return fileIdModifier;
    }

    public void setFileIdModifier(String fileIdModifier) {
        this.fileIdModifier = fileIdModifier;
    }

    public String getFormatCode() {
        return formatCode;
    }

    public void setFormatCode(String formatCode) {
        this.formatCode = formatCode;
    }

    public String getImmediateDestination() {
        return immediateDestination;
    }

    public void setImmediateDestination(String immediateDestination) {
        this.immediateDestination = immediateDestination;
    }

    public String getImmediateDestinationName() {
        return immediateDestinationName;
    }

    public void setImmediateDestinationName(String immediateDestinationName) {
        this.immediateDestinationName = immediateDestinationName;
    }

    public String getImmediateOrigin() {
        return immediateOrigin;
    }

    public void setImmediateOrigin(String immediateOrigin) {
        this.immediateOrigin = immediateOrigin;
    }

    public String getPriorityCode() {
        return priorityCode;
    }

    public void setPriorityCode(String priorityCode) {
        this.priorityCode = priorityCode;
    }

    public String getRecordSize() {
        return recordSize;
    }

    public void setRecordSize(String recordSize) {
        this.recordSize = recordSize;
    }

    public String getBatchHeaderRecordTypeCode() {
        return batchHeaderRecordTypeCode;
    }

    public void setBatchHeaderRecordTypeCode(String batchHeaderRecordTypeCode) {
        this.batchHeaderRecordTypeCode = batchHeaderRecordTypeCode;
    }

    public String getCompanyEntryDescription() {
        return companyEntryDescription;
    }

    public void setCompanyEntryDescription(String companyEntryDescription) {
        this.companyEntryDescription = companyEntryDescription;
    }

    public String getCompanyIdentification() {
        return companyIdentification;
    }

    public void setCompanyIdentification(String companyIdentification) {
        this.companyIdentification = companyIdentification;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getOriginatorStatusCode() {
        return originatorStatusCode;
    }

    public void setOriginatorStatusCode(String originatorStatusCode) {
        this.originatorStatusCode = originatorStatusCode;
    }

    public String getServiceClassCode() {
        return serviceClassCode;
    }

    public void setServiceClassCode(String serviceClassCode) {
        this.serviceClassCode = serviceClassCode;
    }

    public String getStandardEntryClass() {
        return standardEntryClass;
    }

    public void setStandardEntryClass(String standardEntryClass) {
        this.standardEntryClass = standardEntryClass;
    }

    public String getAddendaRecordIndicator() {
        return addendaRecordIndicator;
    }

    public void setAddendaRecordIndicator(String addendaRecordIndicator) {
        this.addendaRecordIndicator = addendaRecordIndicator;
    }

    public String getEntryDetailRecordTypeCode() {
        return entryDetailRecordTypeCode;
    }

    public void setEntryDetailRecordTypeCode(String entryDetailRecordTypeCode) {
        this.entryDetailRecordTypeCode = entryDetailRecordTypeCode;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public String getButtonAction() {
        return buttonAction;
    }

    public void setButtonAction(String buttonAction) {
        this.buttonAction = buttonAction;
    }

    public String getFtpHost() {
        return ftpHost;
    }

    public void setFtpHost(String ftpHost) {
        this.ftpHost = ftpHost;
    }

    public String getFtpPassword() {
        return ftpPassword;
    }

    public void setFtpPassword(String ftpPassword) {
        this.ftpPassword = ftpPassword;
    }

    public String getFtpUserName() {
        return ftpUserName;
    }

    public void setFtpUserName(String ftpUserName) {
        this.ftpUserName = ftpUserName;
    }

    public FormFile getPublicKeyFile() {
        return publicKeyFile;
    }

    public void setPublicKeyFile(FormFile publicKeyFile) {
        this.publicKeyFile = publicKeyFile;
    }

    public Boolean getHavePublicKey() {
        return havePublicKey;
    }

    public void setHavePublicKey(Boolean havePublicKey) {
        this.havePublicKey = havePublicKey;
    }

    public Boolean getHasSshPrivateKey() {
        return hasSshPrivateKey;
    }

    public void setHasSshPrivateKey(Boolean hasSshPrivateKey) {
        this.hasSshPrivateKey = hasSshPrivateKey;
    }

    public FormFile getSshPrivateKeyFile() {
        return sshPrivateKeyFile;
    }

    public void setSshPrivateKeyFile(FormFile sshPrivateKeyFile) {
        this.sshPrivateKeyFile = sshPrivateKeyFile;
    }

    public String getSshPassphrase() {
        return sshPassphrase;
    }

    public void setSshPassphrase(String sshPassphrase) {
        this.sshPassphrase = sshPassphrase;
    }

    public Integer getFtpPort() {
        return ftpPort;
    }

    public void setFtpPort(Integer ftpPort) {
        this.ftpPort = ftpPort;
    }

    public String getFtpDirectory() {
        return ftpDirectory;
    }

    public void setFtpDirectory(String ftpDirectory) {
        this.ftpDirectory = ftpDirectory;
    }

    public AchSetUpForm() throws Exception {
        this.fileHeaderRecordTypeCode = AchSetUpUtils.getProperty("fileHeaderRecordTypeCode");
        this.priorityCode = AchSetUpUtils.getProperty("priorityCode");
        this.fileIdModifier = AchSetUpUtils.getProperty("fileIdModifier");
        this.recordSize = AchSetUpUtils.getProperty("recordSize");
        this.blockingFactor = AchSetUpUtils.getProperty("blockingFactor");
        this.formatCode = AchSetUpUtils.getProperty("formatCode");
        this.batchHeaderRecordTypeCode = AchSetUpUtils.getProperty("batchHeaderRecordTypeCode");
        this.serviceClassCode = AchSetUpUtils.getProperty("serviceClassCode");
        this.standardEntryClass = AchSetUpUtils.getProperty("standardEntryClass");
        this.originatorStatusCode = AchSetUpUtils.getProperty("originatorStatusCode");
        this.entryDetailRecordTypeCode = AchSetUpUtils.getProperty("entryDetailRecordTypeCode");
        this.transactionCode = AchSetUpUtils.getProperty("transactionCode");
        this.addendaRecordIndicator = AchSetUpUtils.getProperty("addendaRecordIndicator");
        this.ftpHost = AchSetUpUtils.getProperty("ftpHost");
        this.ftpUserName = AchSetUpUtils.getProperty("ftpUserName");
        this.ftpPassword = AchSetUpUtils.getProperty("ftpPassword");
        this.havePublicKey = false;
        this.sshPassphrase = AchSetUpUtils.getProperty("sshPassphrase");
        this.ftpPort = Integer.parseInt(AchSetUpUtils.getProperty("ftpPort"));
        this.ftpDirectory = AchSetUpUtils.getProperty("ftpDirectory");
        this.hasSshPrivateKey = false;
    }
}
