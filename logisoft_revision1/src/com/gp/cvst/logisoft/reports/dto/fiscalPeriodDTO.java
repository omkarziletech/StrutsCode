package com.gp.cvst.logisoft.reports.dto;

import java.util.List;

public class fiscalPeriodDTO {

	private String period;
	private String startingdate;
	private String endingdate;
	private String status;
	private String year;
	private String fileName;
	private List periodList;
	private String realPath;
	private String amount;
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
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
	public String getStartingdate() {
		return startingdate;
	}
	public void setStartingdate(String startingdate) {
		this.startingdate = startingdate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public List getPeriodList() {
		return periodList;
	}
	public void setPeriodList(List periodList) {
		this.periodList = periodList;
	}
	public String getRealPath() {
		return realPath;
	}
	public void setRealPath(String realPath) {
		this.realPath = realPath;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
}
