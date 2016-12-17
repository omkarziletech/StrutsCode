package com.logiware.form;

import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.AccountingConstants;
import com.gp.cvst.logisoft.domain.ArBatch;
import com.logiware.bean.AccountingBean;
import com.logiware.bean.ArBatchBean;
import com.logiware.bean.PaymentBean;
import com.logiware.hibernate.dao.ArBatchDAO;
import com.logiware.utils.ListUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

/**
 *
 * @author Lakshmi Narayanan
 */
public class ArBatchForm extends ActionForm implements ConstantsInterface {

    private static final Logger log = Logger.getLogger(ArBatchForm.class);
    private static final long serialVersionUID = -7685474544904123970L;
    private String action;
    private Integer pageNo = 1;
    private Integer currentPageSize = 100;
    private Integer totalPageSize = 0;
    private Integer noOfPages = 0;
    private Integer noOfRecords = 0;
    private String sortBy;
    private String orderBy;
    private String batchId;
    private boolean netsettlement;
    private String depositDate;
    private String batchAmount = "0.00";
    private String batchBalance = "0.00";
    private boolean directGlAccount;
    private String bankAccount;
    private String glAccount;
    private String description;
    private String notes;
    private String fromDate;
    private String toDate;
    private String status = STATUS_OPEN;
    private String batchType = AccountingConstants.AR_BATCH_BOTH;
    private boolean searchByUser;
    private String user;
    private String addBatchUser;
    private List<ArBatchBean> arBatches;
    private String selectedBatchId;
    private String customerNumber;
    private String customerName;
    private boolean showConsignee;
    private String paymentCheckId;
    private String checkNumber;
    private String noCheck;
    private String checkTotal = "0.00";
    private String checkApplied = "0.00";
    private String checkBalance = "0.00";
    private boolean onAccountApplied;
    private boolean prepaymentApplied;
    private boolean chargeCodeApplied;
    private String otherCustomerName;
    private String otherCustomerNumber;
    private boolean showOtherConsignee;
    private String searchBy;
    private String searchValue;
    private boolean showAssociatedCompanies;
    private boolean showZeroBalanceReceivables;
    private boolean showPayables;
    private boolean showAccruals;
    private boolean showInactiveAccruals;
    private boolean showAssignedAccruals;
    private boolean filterOpen;
    private boolean master;
    private boolean otherCustomer;
    private List<AccountingBean> applypayments;
    private PaymentBean appliedOnAccount;
    private List<PaymentBean> appliedPrepayments;
    private List<PaymentBean> appliedGLAccounts;
    private List<PaymentBean> appliedTransactions;
    private String arTransactionIds;
    private String apTransactionIds;
    private String acTransactionIds;
    private String excludedPaymentIds;
    private String selectedSubType;
    private String changes = "false";
    private Integer noOfInvoices = 0;
    private boolean autosave;
    private boolean disabled1;
    private boolean showAllAccounts1;
    private boolean disabled2;
    private boolean showAllAccounts2;
    private List<PaymentBean> importedTransactions;
    private List<PaymentBean> discardedTransactions;
    private String select="select";

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getBatchAmount() {
        return batchAmount;
    }

    public void setBatchAmount(String batchAmount) {
        this.batchAmount = batchAmount;
    }

    public String getBatchBalance() {
        return batchBalance;
    }

    public void setBatchBalance(String batchBalance) {
        this.batchBalance = batchBalance;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getDepositDate() {
        return depositDate;
    }

    public void setDepositDate(String depositDate) {
        this.depositDate = depositDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDirectGlAccount() {
        return directGlAccount;
    }

    public void setDirectGlAccount(boolean directGlAccount) {
        this.directGlAccount = directGlAccount;
    }

    public String getGlAccount() {
        return glAccount;
    }

    public void setGlAccount(String glAccount) {
        this.glAccount = glAccount;
    }

    public boolean isNetsettlement() {
        return netsettlement;
    }

    public void setNetsettlement(boolean netsettlement) {
        this.netsettlement = netsettlement;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getCurrentPageSize() {
        return currentPageSize;
    }

    public void setCurrentPageSize(Integer currentPageSize) {
        this.currentPageSize = currentPageSize;
    }

    public Integer getNoOfPages() {
        return noOfPages;
    }

    public void setNoOfPages(Integer noOfPages) {
        this.noOfPages = noOfPages;
    }

    public Integer getNoOfRecords() {
        return noOfRecords;
    }

    public void setNoOfRecords(Integer noOfRecords) {
        this.noOfRecords = noOfRecords;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public Integer getTotalPageSize() {
        return totalPageSize;
    }

    public void setTotalPageSize(Integer totalPageSize) {
        this.totalPageSize = totalPageSize;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBatchType() {
        return batchType;
    }

    public void setBatchType(String batchType) {
        this.batchType = batchType;
    }

    public boolean isSearchByUser() {
        return searchByUser;
    }

    public void setSearchByUser(boolean searchByUser) {
        this.searchByUser = searchByUser;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public List<String> getUsers() throws Exception {
        return new ArBatchDAO().getUsers();
    }

    public List<ArBatchBean> getArBatches() {
        return arBatches;
    }

    public void setArBatches(List<ArBatchBean> arBatches) {
        this.arBatches = arBatches;
    }

    public String getSelectedBatchId() {
        return selectedBatchId;
    }

    public void setSelectedBatchId(String selectedBatchId) {
        this.selectedBatchId = selectedBatchId;
    }

    public String getNetSettGlAccount() throws Exception {
        return LoadLogisoftProperties.getProperty("netSettlementGLAccount");
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

    public boolean isShowConsignee() {
        return showConsignee;
    }

    public void setShowConsignee(boolean showConsignee) {
        this.showConsignee = showConsignee;
    }

    public boolean isChargeCodeApplied() {
        return chargeCodeApplied;
    }

    public void setChargeCodeApplied(boolean chargeCodeApplied) {
        this.chargeCodeApplied = chargeCodeApplied;
    }

    public String getPaymentCheckId() {
        return paymentCheckId;
    }

    public void setPaymentCheckId(String paymentCheckId) {
        this.paymentCheckId = paymentCheckId;
    }

    public String getCheckApplied() {
        return checkApplied;
    }

    public void setCheckApplied(String checkApplied) {
        this.checkApplied = checkApplied;
    }

    public String getCheckBalance() {
        return checkBalance;
    }

    public void setCheckBalance(String checkBalance) {
        this.checkBalance = checkBalance;
    }

    public String getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(String checkNumber) {
        this.checkNumber = checkNumber;
    }

    public String getCheckTotal() {
        return checkTotal;
    }

    public void setCheckTotal(String checkTotal) {
        this.checkTotal = checkTotal;
    }

    public String getNoCheck() {
        return noCheck;
    }

    public void setNoCheck(String noCheck) {
        this.noCheck = noCheck;
    }

    public boolean isOnAccountApplied() {
        return onAccountApplied;
    }

    public void setOnAccountApplied(boolean onAccountApplied) {
        this.onAccountApplied = onAccountApplied;
    }

    public String getOtherCustomerName() {
        return otherCustomerName;
    }

    public void setOtherCustomerName(String otherCustomerName) {
        this.otherCustomerName = otherCustomerName;
    }

    public String getOtherCustomerNumber() {
        return otherCustomerNumber;
    }

    public void setOtherCustomerNumber(String otherCustomerNumber) {
        this.otherCustomerNumber = otherCustomerNumber;
    }

    public boolean isPrepaymentApplied() {
        return prepaymentApplied;
    }

    public void setPrepaymentApplied(boolean prepaymentApplied) {
        this.prepaymentApplied = prepaymentApplied;
    }

    public String getAcTransactionIds() {
        return acTransactionIds;
    }

    public void setAcTransactionIds(String acTransactionIds) {
        this.acTransactionIds = acTransactionIds;
    }

    public String getApTransactionIds() {
        return apTransactionIds;
    }

    public void setApTransactionIds(String apTransactionIds) {
        this.apTransactionIds = apTransactionIds;
    }

    public String getArTransactionIds() {
        return arTransactionIds;
    }

    public void setArTransactionIds(String arTransactionIds) {
        this.arTransactionIds = arTransactionIds;
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

    public boolean isShowAccruals() {
        return showAccruals;
    }

    public void setShowAccruals(boolean showAccruals) {
        this.showAccruals = showAccruals;
    }

    public boolean isShowAssociatedCompanies() {
        return showAssociatedCompanies;
    }

    public void setShowAssociatedCompanies(boolean showAssociatedCompanies) {
        this.showAssociatedCompanies = showAssociatedCompanies;
    }

    public boolean isShowAssignedAccruals() {
        return showAssignedAccruals;
    }

    public void setShowAssignedAccruals(boolean showAssignedAccruals) {
        this.showAssignedAccruals = showAssignedAccruals;
    }

    public boolean isShowInactiveAccruals() {
        return showInactiveAccruals;
    }

    public void setShowInactiveAccruals(boolean showInactiveAccruals) {
        this.showInactiveAccruals = showInactiveAccruals;
    }

    public boolean isShowOtherConsignee() {
        return showOtherConsignee;
    }

    public void setShowOtherConsignee(boolean showOtherConsignee) {
        this.showOtherConsignee = showOtherConsignee;
    }

    public boolean isShowPayables() {
        return showPayables;
    }

    public void setShowPayables(boolean showPayables) {
        this.showPayables = showPayables;
    }

    public boolean isShowZeroBalanceReceivables() {
        return showZeroBalanceReceivables;
    }

    public void setShowZeroBalanceReceivables(boolean showZeroBalanceReceivables) {
        this.showZeroBalanceReceivables = showZeroBalanceReceivables;
    }

    public boolean isFilterOpen() {
        return filterOpen;
    }

    public void setFilterOpen(boolean filterOpen) {
        this.filterOpen = filterOpen;
    }

    public List<LabelValueBean> getSearchByTypes() {
        List<LabelValueBean> searchByTypes = new ArrayList<LabelValueBean>();
        searchByTypes.add(new LabelValueBean("Select One", ""));
        searchByTypes.add(new LabelValueBean("Dock Receipt", SEARCH_BY_DOCK_RECEIPT));
        searchByTypes.add(new LabelValueBean("Invoice/BL", SEARCH_BY_INVOICE_BL));
        searchByTypes.add(new LabelValueBean("Voyage", SEARCH_BY_VOYAGE));
        searchByTypes.add(new LabelValueBean("Container No", SEARCH_BY_CONTAINER));
        searchByTypes.add(new LabelValueBean("Cost Code", SEARCH_BY_COST_CODE));
        searchByTypes.add(new LabelValueBean("Check Number", SEARCH_BY_CHECK_NUMBER));
        searchByTypes.add(new LabelValueBean("Check Amount", SEARCH_BY_CHECK_AMOUNT));
        searchByTypes.add(new LabelValueBean("Invoice/BL/DR Amount", SEARCH_BY_INVOICE_BL_DR_AMOUNT));
        return searchByTypes;
    }

    public boolean isMaster() {
        return master;
    }

    public void setMaster(boolean master) {
        this.master = master;
    }

    public boolean isOtherCustomer() {
        return otherCustomer;
    }

    public void setOtherCustomer(boolean otherCustomer) {
        this.otherCustomer = otherCustomer;
    }

    public List<AccountingBean> getApplypayments() {
        return applypayments;
    }

    public void setApplypayments(List<AccountingBean> applypayments) {
        this.applypayments = applypayments;
    }

    public PaymentBean getAppliedOnAccount() {
        if (null == appliedOnAccount) {
            appliedOnAccount = new PaymentBean();
        }
        return appliedOnAccount;
    }

    public void setAppliedOnAccount(PaymentBean appliedOnAccount) {
        this.appliedOnAccount = appliedOnAccount;
    }

    public List<PaymentBean> getAppliedGLAccounts() {
        return appliedGLAccounts;
    }

    public void setAppliedGLAccounts(List<PaymentBean> appliedGLAccounts) {
        this.appliedGLAccounts = appliedGLAccounts;
    }

    public List<PaymentBean> getAppliedPrepayments() {
        return appliedPrepayments;
    }

    public void setAppliedPrepayments(List<PaymentBean> appliedPrepayments) {
        this.appliedPrepayments = appliedPrepayments;
    }

    public List<PaymentBean> getAppliedTransactions() {
        return appliedTransactions;
    }

    public void setAppliedTransactions(List<PaymentBean> appliedTransactions) {
        this.appliedTransactions = appliedTransactions;
    }

    public String getExcludedPaymentIds() {
        return excludedPaymentIds;
    }

    public void setExcludedPaymentIds(String excludedPaymentIds) {
        this.excludedPaymentIds = excludedPaymentIds;
    }

    public String getChanges() {
        return changes;
    }

    public void setChanges(String changes) {
        this.changes = changes;
    }

    public String getAddBatchUser() {
        return addBatchUser;
    }

    public void setAddBatchUser(String addBatchUser) {
        this.addBatchUser = addBatchUser;
    }

    public Integer getNoOfInvoices() {
        return noOfInvoices;
    }

    public void setNoOfInvoices(Integer noOfInvoices) {
        this.noOfInvoices = noOfInvoices;
    }

    public String getSelectedSubType() {
        return selectedSubType;
    }

    public void setSelectedSubType(String selectedSubType) {
        this.selectedSubType = selectedSubType;
    }

    public boolean isAutosave() {
        return autosave;
    }

    public void setAutosave(boolean autosave) {
        this.autosave = autosave;
    }

    public boolean isDisabled1() {
        return disabled1;
    }

    public void setDisabled1(boolean disabled1) {
        this.disabled1 = disabled1;
    }

    public boolean isShowAllAccounts1() {
        return showAllAccounts1;
    }

    public void setShowAllAccounts1(boolean showAllAccounts1) {
        this.showAllAccounts1 = showAllAccounts1;
    }

    public boolean isDisabled2() {
        return disabled2;
    }

    public void setDisabled2(boolean disabled2) {
        this.disabled2 = disabled2;
    }

    public boolean isShowAllAccounts2() {
        return showAllAccounts2;
    }

    public void setShowAllAccounts2(boolean showAllAccounts2) {
        this.showAllAccounts2 = showAllAccounts2;
    }

    public List<PaymentBean> getDiscardedTransactions() {
        return discardedTransactions;
    }

    public void setDiscardedTransactions(List<PaymentBean> discardedTransactions) {
        this.discardedTransactions = discardedTransactions;
    }

    public List<PaymentBean> getImportedTransactions() {
        return importedTransactions;
    }

    public void setImportedTransactions(List<PaymentBean> importedTransactions) {
        this.importedTransactions = importedTransactions;
    }

    public String getSelect() {
        return select;
    }

    public void setSelect(String select) {
        this.select = select;
    }

    public ArBatchForm() throws Exception {
        applypayments = ListUtils.lazyList(AccountingBean.class);
        appliedPrepayments = ListUtils.lazyList(PaymentBean.class);
        appliedGLAccounts = ListUtils.lazyList(PaymentBean.class);
        appliedTransactions = ListUtils.lazyList(PaymentBean.class);
        importedTransactions = ListUtils.lazyList(PaymentBean.class);
        discardedTransactions = ListUtils.lazyList(PaymentBean.class);
    }

    public ArBatchForm(ArBatch arBatch) throws Exception {
        this();
        this.batchId = arBatch.getBatchId().toString();
        this.addBatchUser = arBatch.getUserId();
        this.status = arBatch.getStatus();
        this.batchType = arBatch.getBatchType();
        if (arBatch.getBatchType().equals(AccountingConstants.AR_NET_SETT_BATCH)) {
            this.netsettlement = true;
            this.batchAmount = NumberUtils.formatNumber(arBatch.getTotalAmount(), "0.00");
            this.batchBalance = "0.00";
        } else {
            this.netsettlement = false;
            this.batchAmount = NumberUtils.formatNumber(arBatch.getTotalAmount(), "0.00");
            this.batchBalance = NumberUtils.formatNumber(arBatch.getBalanceAmount(), "0.00");
        }
        this.depositDate = DateUtils.formatDate(arBatch.getDepositDate(), "MM/dd/yyyy");
        this.bankAccount = arBatch.getBankAccount();
        this.description = arBatch.getBankAcctDesc();
        this.glAccount = arBatch.getGlAccountNo();
        this.directGlAccount = arBatch.isDirectGlAccount();
        this.notes = arBatch.getNotes();
        this.autosave = false;
        this.disabled1 = false;
        this.showAllAccounts1 = false;
        this.disabled2 = false;
        this.showAllAccounts2 = false;
    }

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        try {
            netsettlement = false;
            directGlAccount = false;
            searchByUser = false;
            showConsignee = false;
            showOtherConsignee = false;
            onAccountApplied = false;
            prepaymentApplied = false;
            chargeCodeApplied = false;
            otherCustomer = false;
            autosave = false;
            disabled1 = false;
            showAllAccounts1 = false;
            disabled2 = false;
            showAllAccounts2 = false;
            applypayments = ListUtils.lazyList(AccountingBean.class);
            appliedPrepayments = ListUtils.lazyList(PaymentBean.class);
            appliedGLAccounts = ListUtils.lazyList(PaymentBean.class);
            appliedTransactions = ListUtils.lazyList(PaymentBean.class);
            importedTransactions = ListUtils.lazyList(PaymentBean.class);
            discardedTransactions = ListUtils.lazyList(PaymentBean.class);
        } catch (Exception ex) {
            log.info("reset()in ArBatchForm failed on " + new Date(), ex);
        }
    }
}
