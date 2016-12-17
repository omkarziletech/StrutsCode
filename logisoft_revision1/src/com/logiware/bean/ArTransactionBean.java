package com.logiware.bean;

import com.gp.cong.common.CommonUtils;
import com.logiware.hibernate.domain.ArTransactionHistory;
import java.io.Serializable;
import java.util.Date;

/**
 * @description ArTransactionBean
 * @author LakshmiNarayanan
 */
public class ArTransactionBean implements Serializable {

    private static final long serialVersionUID = 6550360763469305226L;
    private Double adjustmentAmount;
    private Date adjustmentDate;
    private String batchId;
    private String checkNo;
    private String glAccount;
    private Double transactionAmount;
    private Date transactionDate;
    private String transactionType;
    private String userName;

    public ArTransactionBean() {
    }

    public ArTransactionBean(ArTransactionHistory arTransactionHistory) {
	if(CommonUtils.isNotEmpty(arTransactionHistory.getAdjustmentAmount())){
	    this.adjustmentAmount = arTransactionHistory.getAdjustmentAmount();
	    this.adjustmentDate = arTransactionHistory.getTransactionDate();
	}
	if(CommonUtils.isNotEmpty(arTransactionHistory.getArBatchId())){
	    this.batchId = arTransactionHistory.getArBatchId().toString();
	}else if(CommonUtils.isNotEmpty(arTransactionHistory.getApBatchId())){
	    this.batchId = arTransactionHistory.getApBatchId().toString();
	}
	this.glAccount = arTransactionHistory.getGlAccountNumber();
	this.transactionAmount = arTransactionHistory.getTransactionAmount();
	this.transactionDate = arTransactionHistory.getTransactionDate();
	this.transactionType = arTransactionHistory.getTransactionType();
    }



    public Double getAdjustmentAmount() {
        return adjustmentAmount;
    }

    public void setAdjustmentAmount(Double adjustmentAmount) {
        this.adjustmentAmount = adjustmentAmount;
    }

    public Date getAdjustmentDate() {
        return adjustmentDate;
    }

    public void setAdjustmentDate(Date adjustmentDate) {
        this.adjustmentDate = adjustmentDate;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getCheckNo() {
        return checkNo;
    }

    public void setCheckNo(String checkNo) {
        this.checkNo = checkNo;
    }

    public String getGlAccount() {
        return glAccount;
    }

    public void setGlAccount(String glAccount) {
        this.glAccount = glAccount;
    }

    public Double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(Double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
