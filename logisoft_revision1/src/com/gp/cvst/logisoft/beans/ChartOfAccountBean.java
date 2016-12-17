package com.gp.cvst.logisoft.beans;

import java.io.Serializable;

public class ChartOfAccountBean implements Serializable {
	private String status;
	private String acct;
	private String desc;
	private String normalbalance;
	private String group;
	private String multicurrency;
	private String buttonValue;
	private String account;
	private String year;
	private String period;
	private String enddate;
	private String periodbalance;
	private String netchange;
	private String date;
	private String sourcecode;
	private String reference;
	private String description;
	private String debit;
	private String credit;
	private String balance;
	private String currency;
	private String sourceamount;
	private String datefrom;
	private String dateto;
	private String acctType;
	private String controlAcct;
	
	
	public String getControlAcct() {
		return controlAcct;
	}

	public void setControlAcct(String controlAcct) {
		this.controlAcct = controlAcct;
	}

	public ChartOfAccountBean(){
		
	}
	
	public ChartOfAccountBean(String period, String sourcecode, String reference, String description, String debit, String credit, String currency, String balance, String sourceamount){		
		super();
		this.period = period;
		this.sourcecode = sourcecode;
		this.reference = reference;
		this.description = description;
		this.debit = debit;
		this.credit = credit;
		this.balance = balance;
		this.currency = currency;
		this.sourceamount = sourceamount;
	}
	public String getAcctType() {
		return acctType;
	}
	public void setAcctType(String acctType) {
		this.acctType = acctType;
	}
	public String getDatefrom() {
		return datefrom;
	}
	public void setDatefrom(String datefrom) {
		this.datefrom = datefrom;
	}
	public String getDateto() {
		return dateto;
	}
	public void setDateto(String dateto) {
		this.dateto = dateto;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getCredit() {
		return credit;
	}
	public void setCredit(String credit) {
		this.credit = credit;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDebit() {
		return debit;
	}
	public void setDebit(String debit) {
		this.debit = debit;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getSourceamount() {
		return sourceamount;
	}
	public void setSourceamount(String sourceamount) {
		this.sourceamount = sourceamount;
	}
	public String getSourcecode() {
		return sourcecode;
	}
	public void setSourcecode(String sourcecode) {
		this.sourcecode = sourcecode;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public String getNetchange() {
		return netchange;
	}
	public void setNetchange(String netchange) {
		this.netchange = netchange;
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
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getAcct() {
		return acct;
	}
	public void setAcct(String acct) {
		this.acct = acct;
	}
	public String getButtonValue() {
		return buttonValue;
	}
	public void setButtonValue(String buttonValue) {
		this.buttonValue = buttonValue;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getMulticurrency() {
		return multicurrency;
	}
	public void setMulticurrency(String multicurrency) {
		this.multicurrency = multicurrency;
	}
	public String getNormalbalance() {
		return normalbalance;
	}
	public void setNormalbalance(String normalbalance) {
		this.normalbalance = normalbalance;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}



	
}
