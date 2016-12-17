package com.logiware.hibernate.domain;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 *
 * @author Lakshmi Narayanan
 */
@Entity
@Table(name = "ach_setup")
public class AchSetUp implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ach_id", nullable = false)
    private Integer achId;
    @Basic(optional = false)
    @Column(name = "file_header_record_type_code", nullable = false, length = 1)
    private String fileHeaderRecordTypeCode;
    @Basic(optional = false)
    @Column(name = "priority_code", nullable = false, length = 2)
    private String priorityCode;
    @Basic(optional = false)
    @Column(name = "immediate_origin", nullable = false, length = 10)
    private String immediateOrigin;
    @Basic(optional = false)
    @Column(name = "file_id_modifier", nullable = false, length = 1)
    private String fileIdModifier;
    @Basic(optional = false)
    @Column(name = "record_size", nullable = false, length = 3)
    private String recordSize;
    @Basic(optional = false)
    @Column(name = "blocking_factor", nullable = false, length = 2)
    private String blockingFactor;
    @Basic(optional = false)
    @Column(name = "format_code", nullable = false, length = 1)
    private String formatCode;
    @Basic(optional = false)
    @Column(name = "batch_header_record_type_code", nullable = false, length = 1)
    private String batchHeaderRecordTypeCode;
    @Basic(optional = false)
    @Column(name = "service_class_code", nullable = false, length = 3)
    private String serviceClassCode;
    @Basic(optional = false)
    @Column(name = "company_name", nullable = false, length = 16)
    private String companyName;
    @Basic(optional = false)
    @Column(name = "company_identification", nullable = false, length = 10)
    private String companyIdentification;
    @Basic(optional = false)
    @Column(name = "standard_entry_class", nullable = false, length = 3)
    private String standardEntryClass;
    @Basic(optional = false)
    @Column(name = "company_entry_description", nullable = false, length = 10)
    private String companyEntryDescription;
    @Basic(optional = false)
    @Column(name = "originator_status_code", nullable = false, length = 1)
    private String originatorStatusCode;
    @Basic(optional = false)
    @Column(name = "entry_detail_record_type_code", nullable = false, length = 1)
    private String entryDetailRecordTypeCode;
    @Basic(optional = false)
    @Column(name = "transaction_code", nullable = false, length = 2)
    private String transactionCode;
    @Basic(optional = false)
    @Column(name = "addenda_record_indicator", nullable = false, length = 1)
    private String addendaRecordIndicator;
    @Column(name = "batch_sequence")
    private Integer batchSequence;
    @Basic(optional = false)
    @Column(name = "ftp_host", nullable = false, length = 30)
    private String ftpHost;
    @Basic(optional = false)
    @Column(name = "ftp_user_name", nullable = false, length = 30)
    private String ftpUserName;
    @Basic(optional = false)
    @Column(name = "ftp_password", nullable = false, length = 30)
    private String ftpPassword;
    @Basic(optional = false)
    @Lob
    @Column(name = "public_key", nullable = false)
    private byte[] publicKey;
    @Basic(optional = false)
    @Lob
    @Column(name = "ssh_private_key", nullable = false)
    private byte[] sshPrivateKey;
    @Column(name = "ssh_passphrase", length = 50)
    private String sshPassphrase;
    @Column(name = "ftp_port")
    private Integer ftpPort;
    @Column(name = "ftp_directory", length = 150)
    private String ftpDirectory;

    public AchSetUp() {
    }

    public AchSetUp(Integer achId) {
        this.achId = achId;
    }

    public AchSetUp(Integer achId, String fileHeaderRecordTypeCode, String priorityCode, String immediateOrigin, String fileIdModifier, String recordSize, String blockingFactor, String formatCode, String batchHeaderRecordTypeCode, String serviceClassCode, String companyName, String companyIdentification, String standardEntryClass, String companyEntryDescription, String originatorStatusCode, String entryDetailRecordTypeCode, String transactionCode, String addendaRecordIndicator, String ftpHost, String ftpUserName, String ftpPassword, byte[] publicKey, byte[] sshPrivateKey) {
        this.achId = achId;
        this.fileHeaderRecordTypeCode = fileHeaderRecordTypeCode;
        this.priorityCode = priorityCode;
        this.immediateOrigin = immediateOrigin;
        this.fileIdModifier = fileIdModifier;
        this.recordSize = recordSize;
        this.blockingFactor = blockingFactor;
        this.formatCode = formatCode;
        this.batchHeaderRecordTypeCode = batchHeaderRecordTypeCode;
        this.serviceClassCode = serviceClassCode;
        this.companyName = companyName;
        this.companyIdentification = companyIdentification;
        this.standardEntryClass = standardEntryClass;
        this.companyEntryDescription = companyEntryDescription;
        this.originatorStatusCode = originatorStatusCode;
        this.entryDetailRecordTypeCode = entryDetailRecordTypeCode;
        this.transactionCode = transactionCode;
        this.addendaRecordIndicator = addendaRecordIndicator;
        this.ftpHost = ftpHost;
        this.ftpUserName = ftpUserName;
        this.ftpPassword = ftpPassword;
        this.publicKey = publicKey;
        this.sshPrivateKey = sshPrivateKey;
    }

    public Integer getAchId() {
        return achId;
    }

    public void setAchId(Integer achId) {
        this.achId = achId;
    }

    public String getFileHeaderRecordTypeCode() {
        return fileHeaderRecordTypeCode;
    }

    public void setFileHeaderRecordTypeCode(String fileHeaderRecordTypeCode) {
        this.fileHeaderRecordTypeCode = fileHeaderRecordTypeCode;
    }

    public String getPriorityCode() {
        return priorityCode;
    }

    public void setPriorityCode(String priorityCode) {
        this.priorityCode = priorityCode;
    }

    public String getImmediateOrigin() {
        return immediateOrigin;
    }

    public void setImmediateOrigin(String immediateOrigin) {
        this.immediateOrigin = immediateOrigin;
    }

    public String getFileIdModifier() {
        return fileIdModifier;
    }

    public void setFileIdModifier(String fileIdModifier) {
        this.fileIdModifier = fileIdModifier;
    }

    public String getRecordSize() {
        return recordSize;
    }

    public void setRecordSize(String recordSize) {
        this.recordSize = recordSize;
    }

    public String getBlockingFactor() {
        return blockingFactor;
    }

    public void setBlockingFactor(String blockingFactor) {
        this.blockingFactor = blockingFactor;
    }

    public String getFormatCode() {
        return formatCode;
    }

    public void setFormatCode(String formatCode) {
        this.formatCode = formatCode;
    }

    public String getBatchHeaderRecordTypeCode() {
        return batchHeaderRecordTypeCode;
    }

    public void setBatchHeaderRecordTypeCode(String batchHeaderRecordTypeCode) {
        this.batchHeaderRecordTypeCode = batchHeaderRecordTypeCode;
    }

    public String getServiceClassCode() {
        return serviceClassCode;
    }

    public void setServiceClassCode(String serviceClassCode) {
        this.serviceClassCode = serviceClassCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyIdentification() {
        return companyIdentification;
    }

    public void setCompanyIdentification(String companyIdentification) {
        this.companyIdentification = companyIdentification;
    }

    public String getStandardEntryClass() {
        return standardEntryClass;
    }

    public void setStandardEntryClass(String standardEntryClass) {
        this.standardEntryClass = standardEntryClass;
    }

    public String getCompanyEntryDescription() {
        return companyEntryDescription;
    }

    public void setCompanyEntryDescription(String companyEntryDescription) {
        this.companyEntryDescription = companyEntryDescription;
    }

    public String getOriginatorStatusCode() {
        return originatorStatusCode;
    }

    public void setOriginatorStatusCode(String originatorStatusCode) {
        this.originatorStatusCode = originatorStatusCode;
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

    public String getAddendaRecordIndicator() {
        return addendaRecordIndicator;
    }

    public void setAddendaRecordIndicator(String addendaRecordIndicator) {
        this.addendaRecordIndicator = addendaRecordIndicator;
    }

    public Integer getBatchSequence() {
        return batchSequence;
    }

    public void setBatchSequence(Integer batchSequence) {
        this.batchSequence = batchSequence;
    }

    public String getFtpHost() {
        return ftpHost;
    }

    public void setFtpHost(String ftpHost) {
        this.ftpHost = ftpHost;
    }

    public String getFtpUserName() {
        return ftpUserName;
    }

    public void setFtpUserName(String ftpUserName) {
        this.ftpUserName = ftpUserName;
    }

    public String getFtpPassword() {
        return ftpPassword;
    }

    public void setFtpPassword(String ftpPassword) {
        this.ftpPassword = ftpPassword;
    }

    public byte[] getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
    }

    public byte[] getSshPrivateKey() {
        return sshPrivateKey;
    }

    public void setSshPrivateKey(byte[] sshPrivateKey) {
        this.sshPrivateKey = sshPrivateKey;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (achId != null ? achId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AchSetUp)) {
            return false;
        }
        AchSetUp other = (AchSetUp) object;
        if ((this.achId == null && other.achId != null) || (this.achId != null && !this.achId.equals(other.achId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.logiware.hibernate.domain.AchSetUp[ achId=" + achId + " ]";
    }

}
