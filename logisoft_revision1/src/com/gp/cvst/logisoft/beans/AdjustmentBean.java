package com.gp.cvst.logisoft.beans;

public class AdjustmentBean implements java.io.Serializable {

	private String chargeCode;
	private String checknumber;
	private Double amount;
	private Double adjamount;
	private Integer batchId;
	private String adjustmentDate;
	private String userName;
		
	public String getAdjustmentDate() {
		return adjustmentDate;
	}
	public void setAdjustmentDate(String adjustmentDate) {
		this.adjustmentDate = adjustmentDate;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Integer getBatchId() {
		return batchId;
	}
	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
	}
	public Double getAdjamount() {
		return adjamount;
	}
	public void setAdjamount(Double adjamount) {
		this.adjamount = adjamount;
	}
	public Double getAmount() {
		
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getChargeCode() {
		return chargeCode;
	}
	public void setChargeCode(String chargeCode) {
		this.chargeCode = chargeCode;
	}
	public String getChecknumber() {
		return checknumber;
	}
	public void setChecknumber(String checknumber) {
		this.checknumber = checknumber;
	}
	 
	
}
