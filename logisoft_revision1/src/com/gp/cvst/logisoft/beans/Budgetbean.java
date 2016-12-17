package com.gp.cvst.logisoft.beans;

import java.io.Serializable;

public class Budgetbean implements Serializable {
private String period;
private String enddate;
private String budgetamount;
private String year;
private String budgetSet;
private String budgetaccount;
 

public String getBudgetamount() {
	return budgetamount;
}
public void setBudgetamount(String budgetamount) {
	this.budgetamount = budgetamount;
}
public String getEnddate() {
	return enddate;
}
public void setEnddate(String enddate) {
	this.enddate = enddate;
}
public String getPeriod() {
	return period;
}
public void setPeriod(String period) {
	this.period = period;
}
public String getYear() {
	return year;
}
public void setYear(String year) {
	this.year = year;
}
public String getBudgetSet() {
	return budgetSet;
}
public void setBudgetSet(String budgetSet) {
	this.budgetSet = budgetSet;
}
public String getBudgetaccount() {
	return budgetaccount;
}
public void setBudgetaccount(String budgetaccount) {
	this.budgetaccount = budgetaccount;
}

}
