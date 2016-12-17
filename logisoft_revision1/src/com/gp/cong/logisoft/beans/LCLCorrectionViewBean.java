package com.gp.cong.logisoft.beans;

import java.io.Serializable;
import java.util.Date;

public class LCLCorrectionViewBean implements Serializable {

    private Long correctionId;
    private Long noticeNo;
    private String userName;
    private String approval;
    private Date sailDate;
    private String prepaidCollect;
    private String correctionType;
    private String correctionCode;
    private String whoPosted;
    private String whoVoided;
    private String postedDate;
    private String voidedDate;
    private String noticeDate;
    private String posted;
    private String blNo;
    private Long fileId;
    private String fileNo;
    private String status;
    private String fileState;
    private String partyNo;
    private String creditDebit;
    private Integer voidStatus;
    private String currentProfit;
    private String profitAfterCN;

    public Date getSailDate() {
        return sailDate;
    }

    public void setSailDate(Date sailDate) {
        this.sailDate = sailDate;
    }

    public String getApproval() {
        return approval;
    }

    public void setApproval(String approval) {
        this.approval = approval;
    }

    public Long getCorrectionId() {
        return correctionId;
    }

    public void setCorrectionId(Long correctionId) {
        this.correctionId = correctionId;
    }

    public Long getNoticeNo() {
        return noticeNo;
    }

    public void setNoticeNo(Long noticeNo) {
        this.noticeNo = noticeNo;
    }

    public String getPosted() {
        return posted;
    }

    public void setPosted(String posted) {
        this.posted = posted;
    }

    public String getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(String postedDate) {
        this.postedDate = postedDate;
    }

    public String getPrepaidCollect() {
        return prepaidCollect;
    }

    public void setPrepaidCollect(String prepaidCollect) {
        this.prepaidCollect = prepaidCollect;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getWhoPosted() {
        return whoPosted;
    }

    public void setWhoPosted(String whoPosted) {
        this.whoPosted = whoPosted;
    }

    public String getCorrectionCode() {
        return correctionCode;
    }

    public void setCorrectionCode(String correctionCode) {
        this.correctionCode = correctionCode;
    }

    public String getCorrectionType() {
        return correctionType;
    }

    public void setCorrectionType(String correctionType) {
        this.correctionType = correctionType;
    }

    public String getNoticeDate() {
        return noticeDate;
    }

    public void setNoticeDate(String noticeDate) {
        this.noticeDate = noticeDate;
    }

    public String getBlNo() {
        return blNo;
    }

    public void setBlNo(String blNo) {
        this.blNo = blNo;
    }

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVoidedDate() {
        return voidedDate;
    }

    public void setVoidedDate(String voidedDate) {
        this.voidedDate = voidedDate;
    }

    public String getWhoVoided() {
        return whoVoided;
    }

    public void setWhoVoided(String whoVoided) {
        this.whoVoided = whoVoided;
    }

    public Integer getVoidStatus() {
        return voidStatus;
    }

    public void setVoidStatus(Integer voidStatus) {
        this.voidStatus = voidStatus;
    }

    public String getFileState() {
        return fileState;
    }

    public void setFileState(String fileState) {
        this.fileState = fileState;
    }

    public String getPartyNo() {
        return partyNo;
    }

    public void setPartyNo(String partyNo) {
        this.partyNo = partyNo;
    }

    public String getCreditDebit() {
        return creditDebit;
    }

    public void setCreditDebit(String creditDebit) {
        this.creditDebit = creditDebit;
    }

    public String getCurrentProfit() {
        return currentProfit;
    }

    public void setCurrentProfit(String currentProfit) {
        this.currentProfit = currentProfit;
    }

    public String getProfitAfterCN() {
        return profitAfterCN;
    }

    public void setProfitAfterCN(String profitAfterCN) {
        this.profitAfterCN = profitAfterCN;
    }
 }
