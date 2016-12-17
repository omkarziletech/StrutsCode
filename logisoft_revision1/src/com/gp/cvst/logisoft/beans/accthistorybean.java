package com.gp.cvst.logisoft.beans;

import java.io.Serializable;

public class accthistorybean implements Serializable {
 
	private String account;
	private String year;
	private String period;
	private String enddate;
	private String periodbalance;
	private String netchange;
	
	
	
	public String getNetchange() {
		return netchange;
	}
	public void setNetchange(String netchange) {
		this.netchange = netchange;
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
	public String getPeriodbalance() {
		return periodbalance;
	}
	public void setPeriodbalance(String periodbalance) {
		this.periodbalance = periodbalance;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	
}
