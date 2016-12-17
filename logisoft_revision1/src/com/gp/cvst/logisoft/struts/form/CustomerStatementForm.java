package com.gp.cvst.logisoft.struts.form;

import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.logisoft.bc.accounting.ReportConstants;
import com.gp.cvst.logisoft.AccountingConstants;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class CustomerStatementForm extends ActionForm {

    private static final long serialVersionUID = 2568547519686247649L;
    private String buttonValue;
    private String customerName;
    private String customerNumber;
    private String customerAddress;
    private String terminal;
    private String collector;
    private String company = AccountingConstants.COMPANY_NAME;
    private String footerName;
    private String agentsNotInclude;
    private String agentsInclude = "agentsNotInclude";
    private String onlyAgents;
    private String overdue = ReportConstants.AGINGOVERDUE;
    private String minamt = ReportConstants.AGINGGMINIMUMAMOUNT;
    private String agingzeero = ReportConstants.AGINGZEERO;
    private String agingthirty = ReportConstants.AGINGTHIRTY;
    private String greaterthanthirty = ReportConstants.AGINGGREATERTHANTHIRTY;
    private String agingsixty = ReportConstants.AGINGSIXTY;
    private String greaterthansixty = ReportConstants.AGINGGREATERTHANSIXTY;
    private String agingninty = ReportConstants.AGINGNINTY;
    private String greaterthanninty = ReportConstants.AGINGGREATERTHANNINTYPLUS;
    private String nintyplus;
    private String subject;
    private String textArea;
    private String allcollectors;
    private String includeNetSettlement = AccountingConstants.YES;
    private String excludeIds;
    private boolean allCustomers;
    private boolean ecuLineReport;
    private boolean stmtWithCredit = true;
    private boolean includeInvoiceCredit = true;
    private boolean printCoverLetter;
    private boolean includeAP;
    private boolean includeAccruals;
    private String includePrepayment = AccountingConstants.YES;

    public String getAgentsInclude() {
	return agentsInclude;
    }

    public void setAgentsInclude(String agentsInclude) {
	this.agentsInclude = agentsInclude;
    }

    public String getAgentsNotInclude() {
	return agentsNotInclude;
    }

    public void setAgentsNotInclude(String agentsNotInclude) {
	this.agentsNotInclude = agentsNotInclude;
    }

    public String getAgingninty() {
	return agingninty;
    }

    public void setAgingninty(String agingninty) {
	this.agingninty = agingninty;
    }

    public String getAgingsixty() {
	return agingsixty;
    }

    public void setAgingsixty(String agingsixty) {
	this.agingsixty = agingsixty;
    }

    public String getAgingthirty() {
	return agingthirty;
    }

    public void setAgingthirty(String agingthirty) {
	this.agingthirty = agingthirty;
    }

    public String getAgingzeero() {
	return agingzeero;
    }

    public void setAgingzeero(String agingzeero) {
	this.agingzeero = agingzeero;
    }

    public boolean isAllCustomers() {
	return allCustomers;
    }

    public void setAllCustomers(boolean allCustomers) {
	this.allCustomers = allCustomers;
    }

    public String getAllcollectors() {
	return allcollectors;
    }

    public void setAllcollectors(String allcollectors) {
	this.allcollectors = allcollectors;
    }

    public String getButtonValue() {
	return buttonValue;
    }

    public void setButtonValue(String buttonValue) {
	this.buttonValue = buttonValue;
    }

    public String getCollector() {
	return collector;
    }

    public void setCollector(String collector) {
	this.collector = collector;
    }

    public String getCompany() {
	return company;
    }

    public void setCompany(String company) {
	this.company = company;
    }

    public String getCustomerAddress() {
	return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
	this.customerAddress = customerAddress;
    }

    public String getCustomerName() {
	return customerName;
    }

    public void setCustomerName(String customerName) {
	this.customerName = customerName;
    }

    public String getCustomerNumber() {
	return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
	this.customerNumber = customerNumber;
    }

    public boolean isEcuLineReport() {
	return ecuLineReport;
    }

    public void setEcuLineReport(boolean ecuLineReport) {
	this.ecuLineReport = ecuLineReport;
    }

    public String getExcludeIds() {
	return excludeIds;
    }

    public void setExcludeIds(String excludeIds) {
	this.excludeIds = excludeIds;
    }

    public String getFooterName() {
	return footerName;
    }

    public void setFooterName(String footerName) {
	this.footerName = footerName;
    }

    public String getGreaterthanninty() {
	return greaterthanninty;
    }

    public void setGreaterthanninty(String greaterthanninty) {
	this.greaterthanninty = greaterthanninty;
    }

    public String getGreaterthansixty() {
	return greaterthansixty;
    }

    public void setGreaterthansixty(String greaterthansixty) {
	this.greaterthansixty = greaterthansixty;
    }

    public String getGreaterthanthirty() {
	return greaterthanthirty;
    }

    public void setGreaterthanthirty(String greaterthanthirty) {
	this.greaterthanthirty = greaterthanthirty;
    }

    public boolean isIncludeAP() {
	return includeAP;
    }

    public void setIncludeAP(boolean includeAP) {
	this.includeAP = includeAP;
    }

    public boolean isIncludeAccruals() {
	return includeAccruals;
    }

    public void setIncludeAccruals(boolean includeAccruals) {
	this.includeAccruals = includeAccruals;
    }

    public boolean isIncludeInvoiceCredit() {
	return includeInvoiceCredit;
    }

    public void setIncludeInvoiceCredit(boolean includeInvoiceCredit) {
	this.includeInvoiceCredit = includeInvoiceCredit;
    }

    public String getIncludeNetSettlement() {
	return includeNetSettlement;
    }

    public void setIncludeNetSettlement(String includeNetSettlement) {
	this.includeNetSettlement = includeNetSettlement;
    }

    public String getIncludePrepayment() {
	return includePrepayment;
    }

    public void setIncludePrepayment(String includePrepayment) {
	this.includePrepayment = includePrepayment;
    }

    public String getMinamt() {
	return minamt;
    }

    public void setMinamt(String minamt) {
	this.minamt = minamt;
    }

    public String getNintyplus() {
	return nintyplus;
    }

    public void setNintyplus(String nintyplus) {
	this.nintyplus = nintyplus;
    }

    public String getOnlyAgents() {
	return onlyAgents;
    }

    public void setOnlyAgents(String onlyAgents) {
	this.onlyAgents = onlyAgents;
    }

    public String getOverdue() {
	return overdue;
    }

    public void setOverdue(String overdue) {
	this.overdue = overdue;
    }

    public boolean isPrintCoverLetter() {
	return printCoverLetter;
    }

    public void setPrintCoverLetter(boolean printCoverLetter) {
	this.printCoverLetter = printCoverLetter;
    }

    public boolean isStmtWithCredit() {
	return stmtWithCredit;
    }

    public void setStmtWithCredit(boolean stmtWithCredit) {
	this.stmtWithCredit = stmtWithCredit;
    }

    public String getSubject() {
	return subject;
    }

    public void setSubject(String subject) {
	this.subject = subject;
    }

    public String getTerminal() {
	return terminal;
    }

    public void setTerminal(String terminal) {
	this.terminal = terminal;
    }

    public String getTextArea() {
	return textArea;
    }

    public void setTextArea(String textArea) {
	this.textArea = textArea;
    }

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
	allCustomers = false;
	ecuLineReport = false;
	stmtWithCredit = false;
	includeInvoiceCredit = false;
	printCoverLetter = false;
	includeAP = false;
	includeAccruals = false;
    }
}
