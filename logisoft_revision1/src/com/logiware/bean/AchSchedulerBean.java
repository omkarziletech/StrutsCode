package com.logiware.bean;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cvst.logisoft.domain.BankDetails;
import com.logiware.hibernate.domain.AchProcessHistory;
import java.io.Serializable;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author Lakshminarayanan
 */
public class AchSchedulerBean implements Serializable{
    private static final long serialVersionUID = 4544851506644106478L;
    private Integer processId;
    private String bankName;
    private String bankAcctNo;
    private String bankRoutingNo;
    private String achFileName;
    private String encryptedFileName;
    private String startTime;
    private String endTime;
    private String status;

    public Integer getProcessId() {
        return processId;
    }

    public void setProcessId(Integer processId) {
        this.processId = processId;
    }

    public String getBankAcctNo() {
        return bankAcctNo;
    }

    public void setBankAcctNo(String bankAcctNo) {
        this.bankAcctNo = bankAcctNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankRoutingNo() {
        return bankRoutingNo;
    }

    public void setBankRoutingNo(String bankRoutingNo) {
        this.bankRoutingNo = bankRoutingNo;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }


    public String getAchFileName() {
        return achFileName;
    }

    public void setAchFileName(String fileName) {
        this.achFileName = fileName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEncryptedFileName() {
        return encryptedFileName;
    }

    public void setEncryptedFileName(String encryptedFileName) {
        this.encryptedFileName = encryptedFileName;
    }

    public AchSchedulerBean(){

    }
    public AchSchedulerBean(AchProcessHistory achProcessHistory) throws Exception{
        BankDetails bankDetails = achProcessHistory.getBankDetails();
        this.processId = achProcessHistory.getProcessId();
        this.bankName = bankDetails.getBankName();
        this.bankAcctNo = bankDetails.getBankAcctNo();
        this.bankRoutingNo = bankDetails.getBankRoutingNumber();
        this.achFileName = CommonUtils.isNotEmpty(achProcessHistory.getAchFileName())?FilenameUtils.getName(achProcessHistory.getAchFileName()):"";
        this.encryptedFileName = CommonUtils.isNotEmpty(achProcessHistory.getEncryptedFileName())?FilenameUtils.getName(achProcessHistory.getEncryptedFileName()):"";
        this.startTime = DateUtils.formatDate(achProcessHistory.getStartTime(), "dd-MMM-yyyy HH:mm:ss");
        this.endTime = DateUtils.formatDate(achProcessHistory.getEndTime(), "dd-MMM-yyyy HH:mm:ss");
        this.status = achProcessHistory.getStatus();
    }
}
