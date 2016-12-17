package com.gp.cvst.logisoft.domain;

public class Budget implements java.io.Serializable {

    private Integer id;
    private String account;
    private String period;
    private Double budgetAmount;
    private String budgetSet;
    private Integer year;
    private String endDate;

    public Budget() {
    }
    public Budget(String account, String period, Double budgetAmount, String budgetSet, Integer year) {
        this.account = account;
        this.period = period;
        this.budgetAmount = budgetAmount;
        this.budgetSet = budgetSet;
        this.year = year;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPeriod() {
        return this.period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Double getBudgetAmount() {
        return this.budgetAmount;
    }

    public void setBudgetAmount(Double budgetAmount) {
        this.budgetAmount = budgetAmount;
    }

    public String getBudgetSet() {
        return this.budgetSet;
    }

    public void setBudgetSet(String budgetSet) {
        this.budgetSet = budgetSet;
    }

    public Integer getYear() {
        return this.year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
