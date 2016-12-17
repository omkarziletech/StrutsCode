package com.gp.cvst.logisoft.beans;

import java.io.Serializable;

public class comparisonbean implements Serializable {
	private String period;
	private String actuals;
	private String budget;
	private String difference;
	private String percentage;
	private String budgetamount;


	public String getBudgetamount() {
		return budgetamount;
	}
	public void setBudgetamount(String budgetamount) {
		this.budgetamount = budgetamount;
	}
	public String getActuals() {
		return actuals;
	}
	public void setActuals(String actuals) {
		this.actuals = actuals;
	}
	
	public String getBudget() {
		return budget;
	}
	public void setBudget(String budget) {
		this.budget = budget;
	}
	public String getDifference() {
		return difference;
	}
	public void setDifference(String difference) {
		this.difference = difference;
	}
	public String getPercentage() {
		return percentage;
	}
	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}

}
