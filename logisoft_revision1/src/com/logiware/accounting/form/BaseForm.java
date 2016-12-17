package com.logiware.accounting.form;

import com.logiware.accounting.model.ResultModel;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Lakshmi Naryanan
 */
public class BaseForm extends ActionForm {

    private String action;
    private Integer id;
    private String customerName;
    private String customerNumber;
    private String vendorName;
    private String vendorNumber;
    private String invoiceNumber;
    private String invoiceDate;
    private String blNumber;
    private String transactionType;
    private boolean master;
    private String fromDate;
    private String toDate;
    private String tabName;
    private Integer limit = 200;
    private Integer selectedPage = 1;
    private Integer selectedRows = 0;
    private Integer totalPages = 0;
    private Integer totalRows = 0;
    private String sortBy = "invoice_date";
    private String orderBy = "desc";
    private String status;
    private String mode;
    private String toAddress;
    private String ccAddress;
    private String bccAddress;
    private String subject;
    private String body;
    private boolean showConsignee;
    private boolean showAllAccounts;
    private boolean disabled;
    private boolean toggled;
    private boolean fullSearch;
    private String zebra = "odd";
    private List<ResultModel> results;

    public String getAction() {
	return action;
    }

    public void setAction(String action) {
	this.action = action;
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
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

    public String getVendorName() {
	return vendorName;
    }

    public void setVendorName(String vendorName) {
	this.vendorName = vendorName;
    }

    public String getVendorNumber() {
	return vendorNumber;
    }

    public void setVendorNumber(String vendorNumber) {
	this.vendorNumber = vendorNumber;
    }

    public String getInvoiceNumber() {
	return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
	this.invoiceNumber = invoiceNumber;
    }

    public String getInvoiceDate() {
	return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
	this.invoiceDate = invoiceDate;
    }

    public String getBlNumber() {
	return blNumber;
    }

    public void setBlNumber(String blNumber) {
	this.blNumber = blNumber;
    }

    public String getTransactionType() {
	return transactionType;
    }

    public void setTransactionType(String transactionType) {
	this.transactionType = transactionType;
    }

    public boolean isMaster() {
	return master;
    }

    public void setMaster(boolean master) {
	this.master = master;
    }

    public String getFromDate()  throws Exception{
	return fromDate;
    }

    public void setFromDate(String fromDate) throws Exception{
	this.fromDate = fromDate;
    }

    public String getToDate() throws Exception {
	return toDate;
    }

    public void setToDate(String toDate) throws Exception{
	this.toDate = toDate;
    }

    public String getTabName() {
	return tabName;
    }

    public void setTabName(String tabName) {
	this.tabName = tabName;
    }

    public Integer getLimit() {
	return limit;
    }

    public void setLimit(Integer limit) {
	this.limit = limit;
    }

    public Integer getSelectedPage() {
	return selectedPage;
    }

    public void setSelectedPage(Integer selectedPage) {
	this.selectedPage = selectedPage;
    }

    public Integer getSelectedRows() {
	return selectedRows;
    }

    public void setSelectedRows(Integer selectedRows) {
	this.selectedRows = selectedRows;
    }

    public Integer getTotalPages() {
	return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
	this.totalPages = totalPages;
    }

    public Integer getTotalRows() {
	return totalRows;
    }

    public void setTotalRows(Integer totalRows) {
	this.totalRows = totalRows;
    }

    public String getSortBy() {
	return sortBy;
    }

    public void setSortBy(String sortBy) {
	this.sortBy = sortBy;
    }

    public String getOrderBy() {
	return orderBy;
    }

    public void setOrderBy(String orderBy) {
	this.orderBy = orderBy;
    }

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    public String getMode() {
	return mode;
    }

    public void setMode(String mode) {
	this.mode = mode;
    }

    public String getToAddress() {
	return toAddress;
    }

    public void setToAddress(String toAddress) {
	this.toAddress = toAddress;
    }

    public String getCcAddress() {
	return ccAddress;
    }

    public void setCcAddress(String ccAddress) {
	this.ccAddress = ccAddress;
    }

    public String getBccAddress() {
	return bccAddress;
    }

    public void setBccAddress(String bccAddress) {
	this.bccAddress = bccAddress;
    }

    public String getSubject() {
	return subject;
    }

    public void setSubject(String subject) {
	this.subject = subject;
    }

    public String getBody() {
	return body;
    }

    public void setBody(String body) {
	this.body = body;
    }

    public boolean isShowConsignee() {
	return showConsignee;
    }

    public void setShowConsignee(boolean showConsignee) {
	this.showConsignee = showConsignee;
    }

    public boolean isShowAllAccounts() {
	return showAllAccounts;
    }

    public void setShowAllAccounts(boolean showAllAccounts) {
	this.showAllAccounts = showAllAccounts;
    }

    public boolean isDisabled() {
	return disabled;
    }

    public void setDisabled(boolean disabled) {
	this.disabled = disabled;
    }

    public boolean isToggled() {
	return toggled;
    }

    public void setToggled(boolean toggled) {
	this.toggled = toggled;
    }

    public boolean isFullSearch() {
	return fullSearch;
    }

    public void setFullSearch(boolean fullSearch) {
	this.fullSearch = fullSearch;
    }

    public String getZebra() {
	return zebra;
    }

    public void setZebra(String zebra) {
	this.zebra = zebra;
    }

    public List<ResultModel> getResults() {
	return results;
    }

    public void setResults(List<ResultModel> results) {
	this.results = results;
    }

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
	this.disabled = false;
	this.fullSearch = false;
	this.showAllAccounts = false;
    }
}
