package com.gp.cvst.logisoft.struts.form;

import com.gp.cong.common.CommonConstants;
import com.gp.cvst.logisoft.beans.TransactionBean;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

public class ReconcileForm extends ActionForm {

    private static final long serialVersionUID = 7263433570861329789L;
    private String action;
    private String glAccountNumber;
    private String bankAccount;
    private String bankReconcileDate;
    private String bankStatementBalance = "0.00";
    private String glAccountBalance;
    private String lastReconciledDate;
    private String openChecks;
    private String openDeposits;
    private String difference;
    private String cleared = CommonConstants.NO;
    private FormFile bankCSVFile;
    private String transactionIds;
    private String selectedIds;
    private String unSelectedIds;
    private String clearedDates;
    private List<TransactionBean> reconcileTransactions;

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.openChecks = "";
        this.openDeposits = "";
        this.difference = "";
        this.cleared = CommonConstants.NO;
        this.transactionIds = null;
        this.selectedIds = null;
        this.unSelectedIds = null;
        this.clearedDates = null;
        this.reconcileTransactions = null;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public FormFile getBankCSVFile() {
        return bankCSVFile;
    }

    public void setBankCSVFile(FormFile bankCSVFile) {
        this.bankCSVFile = bankCSVFile;
    }

    public String getBankReconcileDate() {
        return bankReconcileDate;
    }

    public void setBankReconcileDate(String bankReconcileDate) {
        this.bankReconcileDate = bankReconcileDate;
    }

    public String getBankStatementBalance() {
        return bankStatementBalance;
    }

    public void setBankStatementBalance(String bankStatementBalance) {
        this.bankStatementBalance = bankStatementBalance;
    }

    public String getCleared() {
        return cleared;
    }

    public void setCleared(String cleared) {
        this.cleared = cleared;
    }

    public String getClearedDates() {
        return clearedDates;
    }

    public void setClearedDates(String clearedDates) {
        this.clearedDates = clearedDates;
    }

    public String getDifference() {
        return difference;
    }

    public void setDifference(String difference) {
        this.difference = difference;
    }

    public String getGlAccountBalance() {
        return glAccountBalance;
    }

    public void setGlAccountBalance(String glAccountBalance) {
        this.glAccountBalance = glAccountBalance;
    }

    public String getGlAccountNumber() {
        return glAccountNumber;
    }

    public void setGlAccountNumber(String glAccountNumber) {
        this.glAccountNumber = glAccountNumber;
    }

    public String getLastReconciledDate() {
        return lastReconciledDate;
    }

    public void setLastReconciledDate(String lastReconciledDate) {
        this.lastReconciledDate = lastReconciledDate;
    }

    public String getOpenChecks() {
        return openChecks;
    }

    public void setOpenChecks(String openChecks) {
        this.openChecks = openChecks;
    }

    public String getOpenDeposits() {
        return openDeposits;
    }

    public void setOpenDeposits(String openDeposits) {
        this.openDeposits = openDeposits;
    }

    public String getTransactionIds() {
        return transactionIds;
    }

    public void setTransactionIds(String transactionIds) {
        this.transactionIds = transactionIds;
    }

    public String getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(String selectedIds) {
        this.selectedIds = selectedIds;
    }

    public String getUnSelectedIds() {
        return unSelectedIds;
    }

    public void setUnSelectedIds(String unSelectedIds) {
        this.unSelectedIds = unSelectedIds;
    }

    public List<TransactionBean> getReconcileTransactions() {
        return reconcileTransactions;
    }

    public void setReconcileTransactions(List<TransactionBean> reconcileTransactions) {
        this.reconcileTransactions = reconcileTransactions;
    }
}
