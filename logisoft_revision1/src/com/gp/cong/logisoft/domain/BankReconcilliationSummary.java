package com.gp.cong.logisoft.domain;

import java.util.Date;

import com.infomata.data.DataRow;
import java.io.Serializable;

public class BankReconcilliationSummary implements Serializable{
	private Integer id;
	private String bankAccountNumber;
	private String bankName;
	private Date reconcileDate;
	private String abaNumber;
	private Double beginningBalance;
	private Double depositTotal;
	private Double checktotal;
	private Double unReportedCredit;
	private Double unReportedDebit;
	
	/**
	 * Default Constructor
	 */
	public BankReconcilliationSummary(){
	}
	
	/**
	 * @param row
	 */
	public BankReconcilliationSummary(DataRow row){
		this.bankAccountNumber = row.getString("Account Num");
		this.reconcileDate = row.getDate("Date", "MM/dd/yy");
		this.abaNumber =  row.getString("ABA Num");
		this.bankName = row.getString("Account Name");
		this.beginningBalance = row.getDouble("Beginning Balance", 0d);
		this.depositTotal = row.getDouble("Deposits and Other Credits",0d);
		this.checktotal = row.getDouble("Checks and Other Debits",0d);
		this.unReportedCredit = row.getDouble("Unreported Debits",0d);
		this.unReportedDebit = row.getDouble("Unreported Credits",0d);
		
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getBankAccountNumber() {
		return bankAccountNumber;
	}
	public void setBankAccountNumber(String bankAccountNumber) {
		this.bankAccountNumber = bankAccountNumber;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public Date getReconcileDate() {
		return reconcileDate;
	}
	public void setReconcileDate(Date reconcileDate) {
		this.reconcileDate = reconcileDate;
	}
	public String getAbaNumber() {
		return abaNumber;
	}
	public void setAbaNumber(String abaNumber) {
		this.abaNumber = abaNumber;
	}
	public Double getBeginningBalance() {
		return beginningBalance;
	}
	public void setBeginningBalance(Double beginningBalance) {
		this.beginningBalance = beginningBalance;
	}
	public Double getDepositTotal() {
		return depositTotal;
	}
	public void setDepositTotal(Double depositTotal) {
		this.depositTotal = depositTotal;
	}
	public Double getChecktotal() {
		return checktotal;
	}
	public void setChecktotal(Double checktotal) {
		this.checktotal = checktotal;
	}
	public Double getUnReportedCredit() {
		return unReportedCredit;
	}
	public void setUnReportedCredit(Double unReportedCredit) {
		this.unReportedCredit = unReportedCredit;
	}
	public Double getUnReportedDebit() {
		return unReportedDebit;
	}
	public void setUnReportedDebit(Double unReportedDebit) {
		this.unReportedDebit = unReportedDebit;
	}
}
