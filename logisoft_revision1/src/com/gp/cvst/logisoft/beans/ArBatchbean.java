package com.gp.cvst.logisoft.beans;

import com.gp.cvst.logisoft.domain.ArBatch;
import java.util.Date;

public class ArBatchbean implements java.io.Serializable{

    private String batchNo;
    private Date date;
    private Date depositDate;
    private Double totalAmount;
    private Double appliedAmount;
    private String userId;
    private String bankAcct;
    private String reconciled;
    private String status;
    private String notes;
    private Double balanceAmount;
    private Double onAcctAmount;
    private Double prepaidAmount;
    private String match;
    private String batchType;
    private Double adjustAmount;

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBankAcct() {
        return bankAcct;
    }

    public void setBankAcct(String bankAcct) {
        this.bankAcct = bankAcct;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getReconciled() {
        return reconciled;
    }

    public void setReconciled(String reconciled) {
        this.reconciled = reconciled;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBatchType() {
        return batchType;
    }

    public void setBatchType(String batchType) {
        this.batchType = batchType;
    }

    public Double getAdjustAmount() {
        return adjustAmount;
    }

    public void setAdjustAmount(Double adjustAmount) {
        this.adjustAmount = adjustAmount;
    }

    public Double getAppliedAmount() {
        return appliedAmount;
    }

    public void setAppliedAmount(Double appliedAmount) {
        this.appliedAmount = appliedAmount;
    }

    public Double getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(Double balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public Double getOnAcctAmount() {
        return onAcctAmount;
    }

    public void setOnAcctAmount(Double onAcctAmount) {
        this.onAcctAmount = onAcctAmount;
    }

    public Double getPrepaidAmount() {
        return prepaidAmount;
    }

    public void setPrepaidAmount(Double prepaidAmount) {
        this.prepaidAmount = prepaidAmount;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getDepositDate() {
        return depositDate;
    }

    public void setDepositDate(Date depositDate) {
        this.depositDate = depositDate;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArBatchbean() {
    }

    public ArBatchbean(ArBatch arBatch) {
        this.batchNo = arBatch.getBatchId().toString();
        this.date = arBatch.getDate();
        this.batchType = arBatch.getBatchType();
        this.adjustAmount = arBatch.getTotalAmount();
        this.appliedAmount = arBatch.getAppliedAmount();
        this.totalAmount = arBatch.getTotalAmount();
        this.onAcctAmount = arBatch.getOnAcctAmount();
        this.prepaidAmount = arBatch.getPrepayAmount();
        this.balanceAmount = arBatch.getBalanceAmount();
        this.depositDate = arBatch.getDepositDate();
        this.userId = arBatch.getUserId();
        this.bankAcct = arBatch.getBankAccount();
        this.reconciled = arBatch.getReconciled();
        this.status = arBatch.getStatus();
        this.notes = arBatch.getNotes();
    }
}
