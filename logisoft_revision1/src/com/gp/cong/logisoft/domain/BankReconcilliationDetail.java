package com.gp.cong.logisoft.domain;

import com.infomata.data.DataRow;
import java.io.Serializable;

public class BankReconcilliationDetail implements Serializable{
	private Integer summaryId;
	private String actionCode;
	private String actionDescription;
	private String baiCode;
	private Double amount;
	private String serialNumber;
	private String referenceNumber;
	private String details;
	private Integer bankReconcileSummaryId;
	/**
	 * Default Constructor
	 */ 
	public BankReconcilliationDetail(){
		
	}
	/**
	 * @param BankReconcilliationDetail Constructor row
	 */
	public BankReconcilliationDetail(DataRow row){
		actionCode=null;
		actionDescription = row.getString(5 , null);
		baiCode=row.getString(6 , null);
		amount=row.getDouble(7, 0d);
		serialNumber=row.getString( 8, null);
		referenceNumber=row.getString( 9 , null);
		details=row.getString(10 , null);
	}
	public Integer getSummaryId() {
		return summaryId;
	}
	public void setSummaryId(Integer summaryId) {
		this.summaryId = summaryId;
	}
	public String getActionCode() {
		return actionCode;
	}
	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
	}
	public String getActionDescription() {
		return actionDescription;
	}
	public void setActionDescription(String actionDescription) {
		this.actionDescription = actionDescription;
	}
	public String getBaiCode() {
		return baiCode;
	}
	public void setBaiCode(String baiCode) {
		this.baiCode = baiCode;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getReferenceNumber() {
		return referenceNumber;
	}
	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public Integer getBankReconcileSummaryId() {
		return bankReconcileSummaryId;
	}
	public void setBankReconcileSummaryId(Integer bankReconcileSummaryId) {
		this.bankReconcileSummaryId = bankReconcileSummaryId;
	}
}
