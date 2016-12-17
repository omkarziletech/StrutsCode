package com.logiware.accounting.model;

import java.io.Serializable;

/**
 *
 * @author Lakshmi Narayanan
 */
public class AccountModel implements Serializable{

    private String account;
    private String description;
    private String debit;
    private String credit;
    private String period;
    private String date;
    private String sourceCode;
    private String reference;
    private String netChange;
    private String normalBalance;
    private String multiCurrency;
    private String status;
    private String accountType;
    private String accountGroup;
    private String controlAccount;
    private String currentPeriod;
    private String currentSelectedPeriod;
    private String currentYear;
    private String priorYearPeriod;
    private String priorYear;
    private String budgetPeriod;
    private String budgetYear;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDebit() {
        return debit;
    }

    public void setDebit(String debit) {
        this.debit = debit;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getNetChange() {
        return netChange;
    }

    public void setNetChange(String netChange) {
        this.netChange = netChange;
    }

    public String getNormalBalance() {
        return normalBalance;
    }

    public void setNormalBalance(String normalBalance) {
        this.normalBalance = normalBalance;
    }

    public String getMultiCurrency() {
        return multiCurrency;
    }

    public void setMultiCurrency(String multiCurrency) {
        this.multiCurrency = multiCurrency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountGroup() {
        return accountGroup;
    }

    public void setAccountGroup(String accountGroup) {
        this.accountGroup = accountGroup;
    }

    public String getControlAccount() {
        return controlAccount;
    }

    public void setControlAccount(String controlAccount) {
        this.controlAccount = controlAccount;
    }

    public String getCurrentPeriod() {
        return currentPeriod;
    }

    public void setCurrentPeriod(String currentPeriod) {
        this.currentPeriod = currentPeriod;
    }

    public String getCurrentSelectedPeriod() {
        return currentSelectedPeriod;
    }

    public void setCurrentSelectedPeriod(String currentSelectedPeriod) {
        this.currentSelectedPeriod = currentSelectedPeriod;
    }

    public String getCurrentYear() {
        return currentYear;
    }

    public void setCurrentYear(String currentYear) {
        this.currentYear = currentYear;
    }

    public String getPriorYearPeriod() {
        return priorYearPeriod;
    }

    public void setPriorYearPeriod(String priorYearPeriod) {
        this.priorYearPeriod = priorYearPeriod;
    }

    public String getPriorYear() {
        return priorYear;
    }

    public void setPriorYear(String priorYear) {
        this.priorYear = priorYear;
    }

    public String getBudgetPeriod() {
        return budgetPeriod;
    }

    public void setBudgetPeriod(String budgetPeriod) {
        this.budgetPeriod = budgetPeriod;
    }

    public String getBudgetYear() {
        return budgetYear;
    }

    public void setBudgetYear(String budgetYear) {
        this.budgetYear = budgetYear;
    }
}
