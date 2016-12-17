package com.gp.cvst.logisoft.struts.form;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.DateUtils;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.gp.cong.logisoft.bc.accounting.ReportConstants;
import com.gp.cvst.logisoft.AccountingConstants;
import java.util.Date;

public class AgingReportForm extends ActionForm {

	private String buttonValue;
	private String customerName = "";
	private String customerNumber;
	private String phone;
	private String fax;
	private String address;
	private String invoiceNo;
	private String invoiceDate;
	private String aging;
	private String balance;
	private String amount;
	private String allCustomersCheck = CommonConstants.OFF;
	private String includeCustomerAddrsCheck;
	private String allTerminals;
	private String terminal;
	private String collector;
	private String allCollectors = CommonConstants.OFF;
	private String daysOverDue = AccountingConstants.DAYS_OVER_DUE;
	private String minAmount = AccountingConstants.MIN_AMOUNT;
	private String agents = "agentsIncluded";
	private String agingZero = ReportConstants.AGINGZEERO;
	private String agingThirty = ReportConstants.AGINGTHIRTY;
	private String greaterThanThirty = ReportConstants.AGINGGREATERTHANTHIRTY;
	private String agingSixty = ReportConstants.AGINGSIXTY;
	private String greaterThanSixty = ReportConstants.AGINGGREATERTHANSIXTY;
	private String agingNinty = ReportConstants.AGINGNINTY;
	private String greaterThanNinty = ReportConstants.AGINGGREATERTHANNINTYPLUS;
	private String nintyPlus = ReportConstants.AGINGGREATERTHANNINTYPLUS;
	private String report = ReportConstants.REPORT_TYPE_SUMMARY;
	private String includeNetSettlement = AccountingConstants.YES;
	private String customerRangeFrom;
	private String customerRangeTo;
	private String showShipperConsignee;
	private String dateRangeFrom;
	private String dateRangeTo;
	private String checkType;
	private String credit = CommonConstants.BOTH;
	private String noPaymentDate = CommonConstants.OFF;

	public String getCheckType() {
		return checkType;
	}

	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}

	public String getCustomerRangeFrom() {
		return customerRangeFrom;
	}

	public void setCustomerRangeFrom(String customerRangeFrom) {
		this.customerRangeFrom = customerRangeFrom;
	}

	public String getCustomerRangeTo() {
		return customerRangeTo;
	}

	public void setCustomerRangeTo(String customerRangeTo) {
		this.customerRangeTo = customerRangeTo;
	}

	public String getShowShipperConsignee() {
		return showShipperConsignee;
	}

	public void setShowShipperConsignee(String showShipperConsignee) {
		this.showShipperConsignee = showShipperConsignee;
	}

	public String getDateRangeFrom() {
		return dateRangeFrom;
	}

	public void setDateRangeFrom(String dateRangeFrom) {
		this.dateRangeFrom = dateRangeFrom;
	}

	public String getDateRangeTo() {
		return dateRangeTo;
	}

	public void setDateRangeTo(String dateRangeTo) {
		this.dateRangeTo = dateRangeTo;
	}

	public String getAllCustomersCheck() {
		return allCustomersCheck;
	}

	public void setAllCustomersCheck(String allCustomersCheck) {
		this.allCustomersCheck = allCustomersCheck;
	}

	public String getIncludeCustomerAddrsCheck() {
		return includeCustomerAddrsCheck;
	}

	public void setIncludeCustomerAddrsCheck(String includeCustomerAddrsCheck) {
		this.includeCustomerAddrsCheck = includeCustomerAddrsCheck;
	}

	public String getAllTerminals() {
		return allTerminals;
	}

	public void setAllTerminals(String allTerminals) {
		this.allTerminals = allTerminals;
	}

	public String getAgingZero() {
		return agingZero;
	}

	public void setAgingZero(String agingZero) {
		this.agingZero = agingZero;
	}

	public String getAgingThirty() {
		return agingThirty;
	}

	public void setAgingThirty(String agingThirty) {
		this.agingThirty = agingThirty;
	}

	public String getGreaterThanThirty() {
		return greaterThanThirty;
	}

	public void setGreaterThanThirty(String greaterThanThirty) {
		this.greaterThanThirty = greaterThanThirty;
	}

	public String getAgingSixty() {
		return agingSixty;
	}

	public void setAgingSixty(String agingSixty) {
		this.agingSixty = agingSixty;
	}

	public String getGreaterThanSixty() {
		return greaterThanSixty;
	}

	public void setGreaterThanSixty(String greaterThanSixty) {
		this.greaterThanSixty = greaterThanSixty;
	}

	public String getAgingNinty() {
		return agingNinty;
	}

	public void setAgingNinty(String agingNinty) {
		this.agingNinty = agingNinty;
	}

	public String getGreaterThanNinty() {
		return greaterThanNinty;
	}

	public void setGreaterThanNinty(String greaterThanNinty) {
		this.greaterThanNinty = greaterThanNinty;
	}

	public String getNintyPlus() {
		return nintyPlus;
	}

	public void setNintyPlus(String nintyPlus) {
		this.nintyPlus = nintyPlus;
	}

	public String getIncludeNetSettlement() {
		return includeNetSettlement;
	}

	public void setIncludeNetSettlement(String includeNetSettlement) {
		this.includeNetSettlement = includeNetSettlement;
	}

	public ActionErrors validate(ActionMapping mapping,
		HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	/** 
	 * Method reset
	 * @param mapping
	 * @param request
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		// TODO Auto-generated method stub
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getButtonValue() {
		return buttonValue;
	}

	public void setButtonValue(String buttonValue) {
		this.buttonValue = buttonValue;
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

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAging() {
		return aging;
	}

	public void setAging(String aging) {
		this.aging = aging;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	public String getAgents() {
		return agents;
	}

	public void setAgents(String agents) {
		this.agents = agents;
	}

	public String getCollector() {
		return collector;
	}

	public void setCollector(String collector) {
		this.collector = collector;
	}

	public String getAllCollectors() {
		return allCollectors;
	}

	public void setAllCollectors(String allCollectors) {
		this.allCollectors = allCollectors;
	}

	public String getDaysOverDue() {
		return daysOverDue;
	}

	public void setDaysOverDue(String daysOverDue) {
		this.daysOverDue = daysOverDue;
	}

	public String getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(String minAmount) {
		this.minAmount = minAmount;
	}

	public String getReport() {
		return report;
	}

	public void setReport(String report) {
		this.report = report;
	}

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
	}

	public String getNoPaymentDate() {
		return noPaymentDate;
	}

	public void setNoPaymentDate(String noPaymentDate) {
		this.noPaymentDate = noPaymentDate;
	}
}
