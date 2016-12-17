package com.gp.cvst.logisoft.struts.form;

import com.gp.cong.common.CommonUtils;
import com.gp.cvst.logisoft.domain.FiscalPeriod;
import com.gp.cvst.logisoft.hibernate.dao.FiscalPeriodDAO;
import com.logiware.accounting.model.TerminalModel;
import com.logiware.bean.ReportBean;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

public class ApReportsForm extends ActionForm {

    private static final long serialVersionUID = 382L;
    private String action;
    private String vendorName;
    private String vendorNumber;
    private String showAllCustomer;
    private String showDetailOrSummary;
    private String transactionType;
    private String showMaster;
    private String costCode;
    private String glAccount;
    private String cutOffDate;
    private String transactionDate;
    private String divisionOrTerminal;
    private String filterByUser;
    private String endingPeriod;
    private List<LabelValueBean> endingPeriodCollections;
    private String bankAccount;
    private String fromCheck;
    private String toCheck;
    private String fromDate;
    private String toDate;
    private String voidedCheck;
    private String paymentMethod;
    private String checkStatus;
    private String reportType;
    private String showInvoices;
    private String accountFilterType;
    private String timeLapseAmount;
    private String dpo;
    private boolean excel;
    private String searchDateBy;
    private String userName;
    private String userId;
    private String searchDpoBy;
    private String fromPeriod;
    private String toPeriod;
    private String fromPeriodId;
    private String toPeriodId;
    private String numberOfDays;
    private String allBankAccount;
    private TreeMap<Integer, LabelValueBean> tabs;
    private List<ReportBean> disputedEmailLogList;
    private String invoiceNumber;
    private Integer currentPageSize = 100;
    private Integer pageNo = 1;
    private Integer noOfPages = 0;
    private Integer totalPageSize = 0;
    private Integer noOfRecords = 0;
    private String orderBy = "desc";
    private String sortBy;
    private boolean allCustomers;
    private String apSpecialist;
    private String apSpecialistId;
    private String agents = "agentsNotIncluded";
    private boolean includeAR;
    private boolean ecuLineReport;
    private String include;
    private String ecuDesignation;
    private TerminalModel TerminalManager;
    private String terminalNo;
    private String resolveOptions;
    private String managerName;// Dispute Job
    
    public String getAccountFilterType() {
        return accountFilterType;
    }

    public void setAccountFilterType(String accountFilterType) {
        this.accountFilterType = accountFilterType;
    }

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

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getCostCode() {
        return costCode;
    }

    public void setCostCode(String costCode) {
        this.costCode = costCode;
    }

    public String getCutOffDate() {
        return cutOffDate;
    }

    public void setCutOffDate(String cutOffDate) {
        this.cutOffDate = cutOffDate;
    }

    public String getDivisionOrTerminal() {
        return divisionOrTerminal;
    }

    public void setDivisionOrTerminal(String divisionOrTerminal) {
        this.divisionOrTerminal = divisionOrTerminal;
    }

    public String getEndingPeriod() {
        return endingPeriod;
    }

    public void setEndingPeriod(String endingPeriod) {
        this.endingPeriod = endingPeriod;
    }

    public List<LabelValueBean> getEndingPeriodCollections() throws Exception {
        if (CommonUtils.isEmpty(endingPeriodCollections)) {
            endingPeriodCollections = new ArrayList<LabelValueBean>();
            List<FiscalPeriod> fiscalPeriods = new FiscalPeriodDAO().getAllFiscalPeriods();
            for (FiscalPeriod fiscalPeriod : fiscalPeriods) {
                endingPeriodCollections.add(new LabelValueBean(fiscalPeriod.getPeriodDis(), fiscalPeriod.getId().toString()));
            }
        }
        return endingPeriodCollections;
    }

    public void setEndingPeriodCollections(List<LabelValueBean> endingPeriodCollections) {
        this.endingPeriodCollections = endingPeriodCollections;
    }

    public boolean isExcel() {
        return excel;
    }

    public void setExcel(boolean excel) {
        this.excel = excel;
    }

    public String getFilterByUser() {
        return filterByUser;
    }

    public void setFilterByUser(String filterByUser) {
        this.filterByUser = filterByUser;
    }

    public String getFromCheck() {
        return fromCheck;
    }

    public void setFromCheck(String fromCheck) {
        this.fromCheck = fromCheck;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getGlAccount() {
        return glAccount;
    }

    public void setGlAccount(String glAccount) {
        this.glAccount = glAccount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getShowAllCustomer() {
        return showAllCustomer;
    }

    public void setShowAllCustomer(String showAllCustomer) {
        this.showAllCustomer = showAllCustomer;
    }

    public String getShowDetailOrSummary() {
        return showDetailOrSummary;
    }

    public void setShowDetailOrSummary(String showDetailOrSummary) {
        this.showDetailOrSummary = showDetailOrSummary;
    }

    public String getShowInvoices() {
        return showInvoices;
    }

    public void setShowInvoices(String showInvoices) {
        this.showInvoices = showInvoices;
    }

    public String getShowMaster() {
        return showMaster;
    }

    public void setShowMaster(String showMaster) {
        this.showMaster = showMaster;
    }

    public String getTimeLapseAmount() {
        return timeLapseAmount;
    }

    public void setTimeLapseAmount(String timeLapseAmount) {
        this.timeLapseAmount = timeLapseAmount;
    }

    public String getToCheck() {
        return toCheck;
    }

    public void setToCheck(String toCheck) {
        this.toCheck = toCheck;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
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

    public TreeMap<Integer, LabelValueBean> getTabs() {
        return tabs;
    }

    public void setTabs(TreeMap<Integer, LabelValueBean> tabs) {
        this.tabs = tabs;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getVoidedCheck() {
        return voidedCheck;
    }

    public void setVoidedCheck(String voidedCheck) {
        this.voidedCheck = voidedCheck;
    }

    public String getDpo() {
        return dpo;
    }

    public void setDpo(String dpo) {
        this.dpo = dpo;
    }

    public String getSearchDateBy() {
        return searchDateBy;
    }

    public void setSearchDateBy(String searchDateBy) {
        this.searchDateBy = searchDateBy;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSearchDpoBy() {
        return searchDpoBy;
    }

    public void setSearchDpoBy(String searchDpoBy) {
        this.searchDpoBy = searchDpoBy;
    }

    public String getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(String numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public String getFromPeriodId() {
        return fromPeriodId;
    }

    public void setFromPeriodId(String fromPeriodId) {
        this.fromPeriodId = fromPeriodId;
    }

    public String getToPeriodId() {
        return toPeriodId;
    }

    public void setToPeriodId(String toPeriodId) {
        this.toPeriodId = toPeriodId;
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

    public String getAllBankAccount() {
        return allBankAccount;
    }

    public void setAllBankAccount(String allBankAccount) {
        this.allBankAccount = allBankAccount;
    }

    public List<ReportBean> getDisputedEmailLogList() {
        return disputedEmailLogList;
    }

    public void setDisputedEmailLogList(List<ReportBean> disputedEmailLogList) {
        this.disputedEmailLogList = disputedEmailLogList;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Integer getCurrentPageSize() {
        return currentPageSize;
    }

    public void setCurrentPageSize(Integer currentPageSize) {
        this.currentPageSize = currentPageSize;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getNoOfPages() {
        return noOfPages;
    }

    public void setNoOfPages(Integer noOfPages) {
        this.noOfPages = noOfPages;
    }

    public Integer getTotalPageSize() {
        return totalPageSize;
    }

    public void setTotalPageSize(Integer totalPageSize) {
        this.totalPageSize = totalPageSize;
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

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getApSpecialist() {
        return apSpecialist;
    }

    public void setApSpecialist(String apSpecialist) {
        this.apSpecialist = apSpecialist;
    }

    public String getAgents() {
        return agents;
    }

    public void setAgents(String agents) {
        this.agents = agents;
    }

    public boolean isIncludeAR() {
        return includeAR;
    }

    public void setIncludeAR(boolean includeAR) {
        this.includeAR = includeAR;
    }

    public boolean isAllCustomers() {
        return allCustomers;
    }

    public void setAllCustomers(boolean allCustomers) {
        this.allCustomers = allCustomers;
    }

    public String getApSpecialistId() {
        return apSpecialistId;
    }

    public void setApSpecialistId(String apSpecialistId) {
        this.apSpecialistId = apSpecialistId;
    }

    public boolean isEcuLineReport() {
        return ecuLineReport;
    }

    public void setEcuLineReport(boolean ecuLineReport) {
        this.ecuLineReport = ecuLineReport;
    }

    public String getInclude() {
        return include;
    }

    public void setInclude(String include) {
        this.include = include;
    }

    public String getEcuDesignation() {
        return ecuDesignation;
    }

    public void setEcuDesignation(String ecuDesignation) {
        this.ecuDesignation = ecuDesignation;
    }

    public TerminalModel getTerminalManager() {
        return TerminalManager;
    }

    public void setTerminalManager(TerminalModel TerminalManager) {
        this.TerminalManager = TerminalManager;
    }


    public String getTerminalNo() {
        return terminalNo;
    }

    public void setTerminalNo(String terminalNo) {
        this.terminalNo = terminalNo;
    }

    public String getResolveOptions() {
        return resolveOptions;
    }

    public void setResolveOptions(String resolveOptions) {
        this.resolveOptions = resolveOptions;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }
    
    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        allCustomers = false;
        includeAR = false;
        ecuLineReport = false;
    }
}
