package com.gp.cvst.logisoft.domain;

import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cvst.logisoft.AccountingConstants;
import com.logiware.form.ArBatchForm;
import java.util.Date;

/**
 * ArBatch generated by MyEclipse - Hibernate Tools
 */
public class ArBatch implements java.io.Serializable,ConstantsInterface {

    // Fields
    private Integer batchId;
    private String userId;
    private Date date;
    private Date depositDate;
    private Double totalAmount;
    private String bankAccount;
    private String notes;
    private String status;
    private String reconciled;
    private Double appliedAmount=0d;
    private Double balanceAmount=0d;
    private Double onAcctAmount=0d;
    private Double prepayAmount=0d;
    private String bankAcctDesc;
    private String glAccountNo;
    private String batchType;
    private Double adjustAmount=0d;
    private Integer usingBy;
    private boolean directGlAccount;

    // Constructors
    public String getBankAcctDesc() {
        return bankAcctDesc;
    }

    public void setBankAcctDesc(String bankAcctDesc) {
        this.bankAcctDesc = bankAcctDesc;
    }

    /** default constructor */
    public ArBatch() {
    }

    public ArBatch(ArBatchForm arBatchForm) throws Exception {
        this.userId = arBatchForm.getAddBatchUser();
        this.date = new Date();
        this.status = STATUS_OPEN;
        if(arBatchForm.isNetsettlement()){
            this.totalAmount = 0d;
            this.batchType = AccountingConstants.AR_NET_SETT_BATCH;
        }else{
            this.batchType = AccountingConstants.AR_CASH_BATCH;
            this.totalAmount = Double.parseDouble(arBatchForm.getBatchAmount());
            this.balanceAmount = Double.parseDouble(arBatchForm.getBatchBalance());
        }
        this.depositDate = DateUtils.parseDate(arBatchForm.getDepositDate(),"MM/dd/yyyy");
        this.bankAccount = arBatchForm.getBankAccount();
        this.bankAcctDesc = arBatchForm.getDescription();
        this.glAccountNo = arBatchForm.getGlAccount();
        this.directGlAccount = arBatchForm.isDirectGlAccount();
        this.notes = arBatchForm.getNotes();
        this.reconciled = NO;
        this.usingBy = null;
    }

    // Property accessors
    public Integer getBatchId() {
        return this.batchId;
    }

    public void setBatchId(Integer batchId) {
        this.batchId = batchId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDepositDate() {
        return this.depositDate;
    }

    public void setDepositDate(Date depositDate) {
        this.depositDate = depositDate;
    }

    public Double getTotalAmount() {
        return this.totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getBankAccount() {
        return this.bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReconciled() {
        return this.reconciled;
    }

    public void setReconciled(String reconciled) {
        this.reconciled = reconciled;
    }

    public Double getAppliedAmount() {
        return this.appliedAmount;
    }

    public void setAppliedAmount(Double appliedAmount) {
        this.appliedAmount = appliedAmount;
    }

    public Double getBalanceAmount() {
        return this.balanceAmount;
    }

    public void setBalanceAmount(Double balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public Double getOnAcctAmount() {
        return this.onAcctAmount;
    }

    public void setOnAcctAmount(Double onAcctAmount) {
        this.onAcctAmount = onAcctAmount;
    }

    public Double getPrepayAmount() {
        return this.prepayAmount;
    }

    public void setPrepayAmount(Double prepayAmount) {
        this.prepayAmount = prepayAmount;
    }

    public String getGlAccountNo() {
        return glAccountNo;
    }

    public void setGlAccountNo(String glAccountNo) {
        this.glAccountNo = glAccountNo;
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

    public Integer getUsingBy() {
        return usingBy;
    }

    public void setUsingBy(Integer usingBy) {
        this.usingBy = usingBy;
    }

    public boolean isDirectGlAccount() {
        return directGlAccount;
    }

    public void setDirectGlAccount(boolean directGlAccount) {
        this.directGlAccount = directGlAccount;
    }
}
