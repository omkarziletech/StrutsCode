package com.logiware.hibernate.domain;

import com.gp.cong.common.CommonUtils;
import com.gp.cvst.logisoft.domain.BankDetails;
import java.sql.Blob;
import java.util.Date;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author Lakshminarayanan
 */
public class AchProcessHistory implements java.io.Serializable {

    private static final long serialVersionUID = 5138461612139736104L;

    private Integer processId;
    private Date startTime;
    private Date endTime;
    private String status;
    private Double amount;
    private Blob achFile;
    private Blob encryptedFile;
    private BankDetails bankDetails;
    private String achFileName;
    private String encryptedFileName;

    public Integer getProcessId() {
	return processId;
    }

    public void setProcessId(Integer processId) {
	this.processId = processId;
    }

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    public Date getEndTime() {
	return endTime;
    }

    public void setEndTime(Date endTime) {
	this.endTime = endTime;
    }

    public Date getStartTime() {
	return startTime;
    }

    public void setStartTime(Date startTime) {
	this.startTime = startTime;
    }

    public BankDetails getBankDetails() {
	return bankDetails;
    }

    public void setBankDetails(BankDetails bankDetails) {
	this.bankDetails = bankDetails;
    }

    public Blob getAchFile() {
	return achFile;
    }

    public void setAchFile(Blob achFile) {
	this.achFile = achFile;
    }

    public Blob getEncryptedFile() {
	return encryptedFile;
    }

    public void setEncryptedFile(Blob encryptedFile) {
	this.encryptedFile = encryptedFile;
    }

    public String getAchFileName() {
	return achFileName;
    }

    public void setAchFileName(String achFileName) {
	this.achFileName = achFileName;
    }

    public String getEncryptedFileName() {
	return encryptedFileName;
    }

    public void setEncryptedFileName(String encryptedFileName) {
	this.encryptedFileName = encryptedFileName;
    }

    public Double getAmount() {
	return amount;
    }

    public void setAmount(Double amount) {
	this.amount = amount;
    }

    public String getTextFilename() {
	return CommonUtils.isNotEmpty(achFileName) ? FilenameUtils.getName(achFileName).replace("ach_","") : null;
    }

    public String getEncryptedFilename() {
	return CommonUtils.isNotEmpty(encryptedFileName) ? FilenameUtils.getName(encryptedFileName).replace("ach_","") : null;
    }
}
