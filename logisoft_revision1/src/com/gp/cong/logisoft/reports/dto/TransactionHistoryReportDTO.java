/**
 * 
 */
package com.gp.cong.logisoft.reports.dto;

import java.util.ArrayList;
import java.util.List;

import com.gp.cvst.logisoft.beans.ChartOfAccountBean;

/**
 * @author Administrator
 *
 */
public class TransactionHistoryReportDTO {
     private String startAccount;
     private String endAccount;
     private String description;
     private String sourceCode;
     private String fromPeriod;
     private String toPeriod;
     private List<ChartOfAccountBean> transactionList;
     
     
	public TransactionHistoryReportDTO() {}
	
	public List<ChartOfAccountBean> getTransactionList() {
		return transactionList;
	}
	public void setTransactionList(List<ChartOfAccountBean> transactionList) {
		this.transactionList = transactionList;
	}
	public String getStartAccount() {
		return startAccount;
	}
	public void setStartAccount(String startAccount) {
		this.startAccount = startAccount;
	}
	public String getEndAccount() {
		return endAccount;
	}
	public void setEndAccount(String endAccount) {
		this.endAccount = endAccount;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSourceCode() {
		return sourceCode;
	}
	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}
	public String getFromPeriod() {
		return fromPeriod;
	}
	public void setFromPeriod(String fromPeriod) {
		this.fromPeriod = fromPeriod;
	}
	public String getToPeriod() {
		return toPeriod;
	}
	public void setToPeriod(String toPeriod) {
		this.toPeriod = toPeriod;
	}
	public TransactionHistoryReportDTO(String startAccount, String endAccount,
			String description, String sourceCode, String fromPeriod,
			String toPeriod, List<ChartOfAccountBean> transactionList) {
		this.startAccount = startAccount;
		this.endAccount = endAccount;
		this.description = description;
		this.sourceCode = sourceCode;
		this.fromPeriod = fromPeriod;
		this.toPeriod = toPeriod;
		this.transactionList = transactionList;
	}
	
	public TransactionHistoryReportDTO(String startAccount, String endAccount,
			String description, String sourceCode, String fromPeriod,
			String toPeriod) {
		this.startAccount = startAccount;
		this.endAccount = endAccount;
		this.description = description;
		this.sourceCode = sourceCode;
		this.fromPeriod = fromPeriod;
		this.toPeriod = toPeriod;
	}
     
}
