/**
 * 
 */
package com.gp.cvst.logisoft.domain;

import java.io.Serializable;
/**
 * @author Administrator
 *
 */
public class CustomerStatementPromtMessage implements Serializable {

	private String includeInvoicesWithCredit;
	private String includeNetSettlementInvoices;
	private String includeAP;
	private String includeAccruals;
	
	public CustomerStatementPromtMessage() {}
	
	public CustomerStatementPromtMessage(String includeInvoicesWithCredit,
			String includeNetSettlementInvoices, String includeAP,
			String includeAccruals) {
		this.includeInvoicesWithCredit = includeInvoicesWithCredit;
		this.includeNetSettlementInvoices = includeNetSettlementInvoices;
		this.includeAP = includeAP;
		this.includeAccruals = includeAccruals;
	}
	public String getIncludeInvoicesWithCredit() {
		return includeInvoicesWithCredit;
	}
	public void setIncludeInvoicesWithCredit(String includeInvoicesWithCredit) {
		this.includeInvoicesWithCredit = includeInvoicesWithCredit;
	}
	public String getIncludeNetSettlementInvoices() {
		return includeNetSettlementInvoices;
	}
	public void setIncludeNetSettlementInvoices(String includeNetSettlementInvoices) {
		this.includeNetSettlementInvoices = includeNetSettlementInvoices;
	}
	public String getIncludeAP() {
		return includeAP;
	}
	public void setIncludeAP(String includeAP) {
		this.includeAP = includeAP;
	}
	public String getIncludeAccruals() {
		return includeAccruals;
	}
	public void setIncludeAccruals(String includeAccruals) {
		this.includeAccruals = includeAccruals;
	}
}
