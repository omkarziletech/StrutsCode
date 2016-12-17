package com.logiware.accounting.form;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.logiware.bean.ReconcileModel;
import com.logiware.utils.ListUtils;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Lakshmi Narayanan
 */
public class SessionForm implements Serializable {

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
    private String searchFilter;
    private String searchValue1;
    private String searchValue2;
    private String searchValue3;
    private String searchValue4;
    private String fromAmount5 = "0.00";
    private String toAmount5 = "0.00";
    private String fromAmount6 = "0.00";
    private String toAmount6 = "0.00";
    private String fromAmount7 = "0.00";
    private String toAmount7 = "0.00";
    private String searchDate = "Invoice Date";
    private String[] showFilters = {};
    private boolean nsInvoiceOnly;
    private String accountType;
    private String excludeIds;
    private String emailIds;
    private String source;
    private String correctionNotice;
    private String only = "My AP Accounts";
    private String hold = ConstantsInterface.YES;
    private String ar = ConstantsInterface.NO;
    private String payIds;
    private String holdIds;
    private String deleteIds;
    private String subledger;
    private String glPeriod;
    private String searchBy;
    private String searchValue;
    private String glAccount;
    private String fromAmount = "0.00";
    private String toAmount = "0.00";
    private String subledgerId;
    private boolean posted;
    private boolean accruals;
    private String startAccount;
    private String endAccount;
    private String accountGroup;
    private String accountStatus;
    private String bankName;
    private String bankAccount;
    private String reconcileDate;
    private String bankBalance;
    private String glBalance;
    private String lastReconciledDate;
    private String checksOpen;
    private String depositsOpen;
    private String difference;
    private Map<String, String> queries;
    private List<ReconcileModel> transactions;
    private String fileName;
    private String importFileName;
    private String exceptionFileName;

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

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
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

    public String getSearchFilter() {
        return searchFilter;
    }

    public void setSearchFilter(String searchFilter) {
        this.searchFilter = searchFilter;
    }

    public String getSearchValue1() {
        return searchValue1;
    }

    public void setSearchValue1(String searchValue1) {
        this.searchValue1 = searchValue1;
    }

    public String getSearchValue2() {
        return searchValue2;
    }

    public void setSearchValue2(String searchValue2) {
        this.searchValue2 = searchValue2;
    }

    public String getSearchValue3() {
        return searchValue3;
    }

    public void setSearchValue3(String searchValue3) {
        this.searchValue3 = searchValue3;
    }

    public String getSearchValue4() {
        return searchValue4;
    }

    public void setSearchValue4(String searchValue4) {
        this.searchValue4 = searchValue4;
    }

    public String getFromAmount5() {
        return fromAmount5;
    }

    public void setFromAmount5(String fromAmount5) {
        this.fromAmount5 = fromAmount5;
    }

    public String getToAmount5() {
        return toAmount5;
    }

    public void setToAmount5(String toAmount5) {
        this.toAmount5 = toAmount5;
    }

    public String getFromAmount6() {
        return fromAmount6;
    }

    public void setFromAmount6(String fromAmount6) {
        this.fromAmount6 = fromAmount6;
    }

    public String getToAmount6() {
        return toAmount6;
    }

    public void setToAmount6(String toAmount6) {
        this.toAmount6 = toAmount6;
    }

    public String getFromAmount7() {
        return fromAmount7;
    }

    public void setFromAmount7(String fromAmount7) {
        this.fromAmount7 = fromAmount7;
    }

    public String getToAmount7() {
        return toAmount7;
    }

    public void setToAmount7(String toAmount7) {
        this.toAmount7 = toAmount7;
    }

    public String getSearchDate() {
        return searchDate;
    }

    public void setSearchDate(String searchDate) {
        this.searchDate = searchDate;
    }

    public String[] getShowFilters() {
        if (null == showFilters || showFilters.length == 0) {
            showFilters = new String[]{"Open Invoices", "NS Invoices", "Credit Hold - Yes", "Credit Hold - No"};
        }
        return showFilters;
    }

    public void setShowFilters(String[] showFilters) {
        this.showFilters = showFilters;
    }

    public boolean isNsInvoiceOnly() {
        return nsInvoiceOnly;
    }

    public void setNsInvoiceOnly(boolean nsInvoiceOnly) {
        this.nsInvoiceOnly = nsInvoiceOnly;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getExcludeIds() {
        return excludeIds;
    }

    public void setExcludeIds(String excludeIds) {
        this.excludeIds = excludeIds;
    }

    public String getEmailIds() {
        return emailIds;
    }

    public void setEmailIds(String emailIds) {
        this.emailIds = emailIds;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCorrectionNotice() {
        return correctionNotice;
    }

    public void setCorrectionNotice(String correctionNotice) {
        this.correctionNotice = correctionNotice;
    }

    public String getOnly() {
        return only;
    }

    public void setOnly(String only) {
        this.only = only;
    }

    public String getHold() {
        return hold;
    }

    public void setHold(String hold) {
        this.hold = hold;
    }

    public String getAr() {
        return ar;
    }

    public void setAr(String ar) {
        this.ar = ar;
    }

    public String getPayIds() {
        return payIds;
    }

    public void setPayIds(String payIds) {
        this.payIds = payIds;
    }

    public String getHoldIds() {
        return holdIds;
    }

    public void setHoldIds(String holdIds) {
        this.holdIds = holdIds;
    }

    public String getDeleteIds() {
        return deleteIds;
    }

    public void setDeleteIds(String deleteIds) {
        this.deleteIds = deleteIds;
    }

    public String getSubledger() {
        return subledger;
    }

    public void setSubledger(String subledger) {
        this.subledger = subledger;
    }

    public String getGlPeriod() {
        return glPeriod;
    }

    public void setGlPeriod(String glPeriod) {
        this.glPeriod = glPeriod;
    }

    public String getSearchBy() {
        return searchBy;
    }

    public void setSearchBy(String searchBy) {
        this.searchBy = searchBy;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    public String getGlAccount() {
        return glAccount;
    }

    public void setGlAccount(String glAccount) {
        this.glAccount = glAccount;
    }

    public String getFromAmount() {
        return fromAmount;
    }

    public void setFromAmount(String fromAmount) {
        this.fromAmount = fromAmount;
    }

    public String getToAmount() {
        return toAmount;
    }

    public void setToAmount(String toAmount) {
        this.toAmount = toAmount;
    }

    public String getSubledgerId() {
        return subledgerId;
    }

    public void setSubledgerId(String subledgerId) {
        this.subledgerId = subledgerId;
    }

    public boolean isPosted() {
        return posted;
    }

    public void setPosted(boolean posted) {
        this.posted = posted;
    }

    public boolean isAccruals() {
        return accruals;
    }

    public void setAccruals(boolean accruals) {
        this.accruals = accruals;
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

    public String getAccountGroup() {
        return accountGroup;
    }

    public void setAccountGroup(String accountGroup) {
        this.accountGroup = accountGroup;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getReconcileDate() {
        return reconcileDate;
    }

    public void setReconcileDate(String reconcileDate) {
        this.reconcileDate = reconcileDate;
    }

    public String getBankBalance() {
        return bankBalance;
    }

    public void setBankBalance(String bankBalance) {
        this.bankBalance = bankBalance;
    }

    public String getGlBalance() {
        if (CommonUtils.isEmpty(glBalance)) {
            glBalance = "0.00";
        }
        return glBalance;
    }

    public void setGlBalance(String glBalance) {
        this.glBalance = glBalance;
    }

    public String getLastReconciledDate() {
        return lastReconciledDate;
    }

    public void setLastReconciledDate(String lastReconciledDate) {
        this.lastReconciledDate = lastReconciledDate;
    }

    public String getChecksOpen() {
        return checksOpen;
    }

    public void setChecksOpen(String checksOpen) {
        this.checksOpen = checksOpen;
    }

    public String getDepositsOpen() {
        return depositsOpen;
    }

    public void setDepositsOpen(String depositsOpen) {
        this.depositsOpen = depositsOpen;
    }

    public String getDifference() {
        return difference;
    }

    public void setDifference(String difference) {
        this.difference = difference;
    }

    public Map<String, String> getQueries() {
        return queries;
    }

    public void setQueries(Map<String, String> queries) {
        this.queries = queries;
    }

    public List<ReconcileModel> getTransactions() throws Exception {
        if (CommonUtils.isEmpty(transactions)) {
            transactions = ListUtils.lazyList(ReconcileModel.class);
        }
        return transactions;
    }

    public void setTransactions(List<ReconcileModel> transactions) {
        if (CommonUtils.isNotEmpty(this.transactions) && CommonUtils.isNotEmpty(transactions)) {
            transactions.addAll(this.transactions);
        }
        this.transactions = transactions;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getImportFileName() {
        return importFileName;
    }

    public void setImportFileName(String importFileName) {
        this.importFileName = importFileName;
    }

    public String getExceptionFileName() {
        return exceptionFileName;
    }

    public void setExceptionFileName(String exceptionFileName) {
        this.exceptionFileName = exceptionFileName;
    }

}
