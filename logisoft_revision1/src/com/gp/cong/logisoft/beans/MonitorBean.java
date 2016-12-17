package com.gp.cong.logisoft.beans;

import java.io.Serializable;

public class MonitorBean implements Serializable {
	private String useCaseId;
    private String txtCal;
    private String hours;
    private String minutes;
    private String docSetKeyValue;
    private String flowFrom;
    private String status;
	public String getDocSetKeyValue() {
		return docSetKeyValue;
	}
	public void setDocSetKeyValue(String docSetKeyValue) {
		this.docSetKeyValue = docSetKeyValue;
	}
	public String getFlowFrom() {
		return flowFrom;
	}
	public void setFlowFrom(String flowFrom) {
		this.flowFrom = flowFrom;
	}
	public String getHours() {
		return hours;
	}
	public void setHours(String hours) {
		this.hours = hours;
	}
	public String getMinutes() {
		return minutes;
	}
	public void setMinutes(String minutes) {
		this.minutes = minutes;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTxtCal() {
		return txtCal;
	}
	public void setTxtCal(String txtCal) {
		this.txtCal = txtCal;
	}
	public String getUseCaseId() {
		return useCaseId;
	}
	public void setUseCaseId(String useCaseId) {
		this.useCaseId = useCaseId;
	}
}
