/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.lcl.model;

/**
 *
 * @author Mei
 */
public class LclCorrectionModel {

    private String fileId;
    private String fileNo;
    private String blNo;
    private String correctionId;
    private String currentProfit;
    private String profitAfterCN;
    private String correctionNo;
    private String createdByUser;
    private String billingType;
    private String createdByDate;
    private String corrStatus;
    private String correctionType;
    private String correcTypeDesc;
    private String correctionCode;
    private String correcCodeDesc;
    private String approvedByUser;
    private String postedDate;
    private Boolean voidStatus;
    private String sailDate;

    public String getApprovedByUser() {
        return approvedByUser;
    }

    public void setApprovedByUser(String approvedByUser) {
        this.approvedByUser = approvedByUser;
    }

    public String getBillingType() {
        return billingType;
    }

    public void setBillingType(String billingType) {
        this.billingType = billingType;
    }

    public String getBlNo() {
        return blNo;
    }

    public void setBlNo(String blNo) {
        this.blNo = blNo;
    }

    public String getCorrStatus() {
        return corrStatus;
    }

    public void setCorrStatus(String corrStatus) {
        this.corrStatus = corrStatus;
    }

    public String getCorrecCodeDesc() {
        return correcCodeDesc;
    }

    public void setCorrecCodeDesc(String correcCodeDesc) {
        this.correcCodeDesc = correcCodeDesc;
    }

    public String getCorrecTypeDesc() {
        return correcTypeDesc;
    }

    public void setCorrecTypeDesc(String correcTypeDesc) {
        this.correcTypeDesc = correcTypeDesc;
    }

    public String getCorrectionCode() {
        return correctionCode;
    }

    public void setCorrectionCode(String correctionCode) {
        this.correctionCode = correctionCode;
    }

    public String getCorrectionId() {
        return correctionId;
    }

    public void setCorrectionId(String correctionId) {
        this.correctionId = correctionId;
    }

    public String getCorrectionNo() {
        return correctionNo;
    }

    public void setCorrectionNo(String correctionNo) {
        this.correctionNo = correctionNo;
    }

    public String getCorrectionType() {
        return correctionType;
    }

    public void setCorrectionType(String correctionType) {
        this.correctionType = correctionType;
    }

    public String getCreatedByDate() {
        return createdByDate;
    }

    public void setCreatedByDate(String createdByDate) {
        this.createdByDate = createdByDate;
    }

    public String getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(String createdByUser) {
        this.createdByUser = createdByUser;
    }

    public String getCurrentProfit() {
        return currentProfit;
    }

    public void setCurrentProfit(String currentProfit) {
        this.currentProfit = currentProfit;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    public String getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(String postedDate) {
        this.postedDate = postedDate;
    }

    public String getProfitAfterCN() {
        return profitAfterCN;
    }

    public void setProfitAfterCN(String profitAfterCN) {
        this.profitAfterCN = profitAfterCN;
    }

    public String getSailDate() {
        return sailDate;
    }

    public void setSailDate(String sailDate) {
        this.sailDate = sailDate;
    }

    public Boolean getVoidStatus() {
        return voidStatus;
    }

    public void setVoidStatus(Boolean voidStatus) {
        this.voidStatus = voidStatus;
    }
}
