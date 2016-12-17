package com.gp.cvst.logisoft.beans;

import java.io.Serializable;

public class FiscalPeriodBean implements Serializable {
	
	private String period;
	private String staringdate;
	private String endingdate;
	private String status;
	private String year;
	private String periodesc;
	
	
	public FiscalPeriodBean(){
		
	}

	public FiscalPeriodBean(String period, String staringdate, String endingdate, String status, String year, String periodesc) {
		super();
		this.period = period;
		this.staringdate = staringdate;
		this.endingdate = endingdate;
		this.status = status;
		this.year = year;
		this.periodesc = periodesc;
	}
	
	public String getPeriodesc() {
		return periodesc;
	}
	public void setPeriodesc(String periodesc) {
		this.periodesc = periodesc;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getEndingdate() {
		return endingdate;
	}
	public void setEndingdate(String endingdate) {
		this.endingdate = endingdate;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public String getStaringdate() {
		return staringdate;
	}
	public void setStaringdate(String staringdate) {
		this.staringdate = staringdate;
	}

	
	
}