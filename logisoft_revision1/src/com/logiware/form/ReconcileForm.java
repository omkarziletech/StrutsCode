package com.logiware.form;

import com.gp.cong.common.CommonUtils;
import com.logiware.bean.ReconcileModel;
import com.logiware.utils.ListUtils;
import java.util.List;
import java.util.Map;
import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

/**
 *
 * @author Lakshmi Naryanan
 */
public class ReconcileForm extends ActionForm {

    private String glAccount;
    private String bankName;
    private String bankAccount;
    private String reconcileDate;
    private String bankBalance;
    private String glBalance;
    private String lastReconciledDate;
    private String checksOpen;
    private String depositsOpen;
    private String difference;
    private String action;
    private Integer limit = 200;
    private Integer selectedPage = 1;
    private Integer selectedRows = 0;
    private Integer totalPages = 0;
    private Integer totalRows = 0;
    private String sortBy = "transaction_type";
    private String orderBy = "asc";
    private Map<String, String> queries;
    private List<ReconcileModel> transactions;
    private String fileName;
    private String importFileName;
    private String exceptionFileName;

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

    public String getBankName() {
	return bankName;
    }

    public void setBankName(String bankName) {
	this.bankName = bankName;
    }

    public String getBankBalance() {
	if (CommonUtils.isEmpty(bankBalance)) {
	    bankBalance = "0.00";
	}
	return bankBalance;
    }

    public void setBankBalance(String bankBalance) {
	this.bankBalance = bankBalance;
    }

    public String getChecksOpen() {
	if (CommonUtils.isEmpty(checksOpen)) {
	    checksOpen = "0.00";
	}
	return checksOpen;
    }

    public void setChecksOpen(String checksOpen) {
	this.checksOpen = checksOpen;
    }

    public String getDepositsOpen() {
	if (CommonUtils.isEmpty(depositsOpen)) {
	    depositsOpen = "0.00";
	}
	return depositsOpen;
    }

    public void setDepositsOpen(String depositsOpen) {
	this.depositsOpen = depositsOpen;
    }

    public String getDifference() {
	if (CommonUtils.isEmpty(difference)) {
	    difference = "0.00";
	}
	return difference;
    }

    public void setDifference(String difference) {
	this.difference = difference;
    }

    public String getFileName() {
	return fileName;
    }

    public void setFileName(String fileName) {
	this.fileName = fileName;
    }

    public String getExceptionFileName() {
	return exceptionFileName;
    }

    public void setExceptionFileName(String exceptionFileName) {
	this.exceptionFileName = exceptionFileName;
    }

    public String getGlAccount() {
	return glAccount;
    }

    public void setGlAccount(String glAccount) {
	this.glAccount = glAccount;
    }

    public String getGlBalance() {
	if (CommonUtils.isEmpty(glBalance)) {
	    glBalance = "0.00";
	}
	return glBalance;
    }

    public void setGlBalance(String glBalance) {
	this.glBalance = glBalance;
    }

    public String getImportFileName() {
	return importFileName;
    }

    public void setImportFileName(String importFileName) {
	this.importFileName = importFileName;
    }

    public String getLastReconciledDate() {
	return lastReconciledDate;
    }

    public void setLastReconciledDate(String lastReconciledDate) {
	this.lastReconciledDate = lastReconciledDate;
    }

    public Integer getLimit() {
	return limit;
    }

    public void setLimit(Integer limit) {
	this.limit = limit;
    }

    public String getOrderBy() {
	return orderBy;
    }

    public void setOrderBy(String orderBy) {
	this.orderBy = orderBy;
    }

    public Map<String, String> getQueries() {
	return queries;
    }

    public void setQueries(Map<String, String> queries) {
	this.queries = queries;
    }

    public String getReconcileDate() {
	return reconcileDate;
    }

    public void setReconcileDate(String reconcileDate) {
	this.reconcileDate = reconcileDate;
    }

    public Integer getSelectedPage() {
	return selectedPage;
    }

    public void setSelectedPage(Integer selectedPage) {
	this.selectedPage = selectedPage;
    }

    public Integer getSelectedRows() {
	return selectedRows;
    }

    public void setSelectedRows(Integer selectedRows) {
	this.selectedRows = selectedRows;
    }

    public String getSortBy() {
	return sortBy;
    }

    public void setSortBy(String sortBy) {
	this.sortBy = sortBy;
    }

    public Integer getTotalPages() {
	return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
	this.totalPages = totalPages;
    }

    public Integer getTotalRows() {
	return totalRows;
    }

    public void setTotalRows(Integer totalRows) {
	this.totalRows = totalRows;
    }

    public List<ReconcileModel> getTransactions()throws Exception {
	if (CommonUtils.isEmpty(transactions)) {
	    transactions = ListUtils.lazyList(ReconcileModel.class);
	}
	return transactions;
    }

    public void setTransactions(List<ReconcileModel> transactions) {
	if (CommonUtils.isNotEmpty(this.transactions) && CommonUtils.isNotEmpty(transactions)) {
	    transactions.addAll(this.transactions);
	}
	this.transactions = transactions;
    }
}
