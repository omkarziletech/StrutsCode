package com.logiware.bean;

import java.io.Serializable;

/**
 * @since Thursday March 18,2010
 * @author LakshmiNarayanan
 */
public class AccountsBean implements  Serializable{
    private static final long serialVersionUID = 1925900108184082524L;
    private String accountDescription;
    private String account;
    private Double currentYearBalance;
    private Double previousYearBalance;
    private Double currentMonthBalance;
    private Double previousMonthBalance;
    private Double priorYearBalance;
    private Double budgetYtdBalance;
    private Double annualBudgetBalance;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccountDescription() {
        return accountDescription;
    }

    public void setAccountDescription(String accountDescription) {
        this.accountDescription = accountDescription;
    }

    public Double getAnnualBudgetBalance() {
        return annualBudgetBalance;
    }

    public void setAnnualBudgetBalance(Double annualBudgetBalance) {
        this.annualBudgetBalance = annualBudgetBalance;
    }

    public Double getBudgetYtdBalance() {
        return budgetYtdBalance;
    }

    public void setBudgetYtdBalance(Double budgetYtdBalance) {
        this.budgetYtdBalance = budgetYtdBalance;
    }

    public Double getCurrentMonthBalance() {
        return currentMonthBalance;
    }

    public void setCurrentMonthBalance(Double currentMonthBalance) {
        this.currentMonthBalance = currentMonthBalance;
    }

    public Double getCurrentYearBalance() {
        return currentYearBalance;
    }

    public void setCurrentYearBalance(Double currentYearBalance) {
        this.currentYearBalance = currentYearBalance;
    }

    public Double getPreviousMonthBalance() {
        return previousMonthBalance;
    }

    public void setPreviousMonthBalance(Double previousMonthBalance) {
        this.previousMonthBalance = previousMonthBalance;
    }

    public Double getPreviousYearBalance() {
        return previousYearBalance;
    }

    public void setPreviousYearBalance(Double previousYearBalance) {
        this.previousYearBalance = previousYearBalance;
    }

    public Double getPriorYearBalance() {
        return priorYearBalance;
    }

    public void setPriorYearBalance(Double priorYearBalance) {
        this.priorYearBalance = priorYearBalance;
    }
}
