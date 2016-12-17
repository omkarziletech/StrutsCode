package com.gp.cvst.logisoft.struts.form;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cvst.logisoft.beans.TransactionBean;
import com.logiware.accounting.form.BaseForm;
import com.logiware.bean.AccountingBean;
import com.logiware.bean.CustomerBean;
import java.util.ArrayList;
import java.util.List;
import org.apache.struts.util.LabelValueBean;

public class ApInquiryForm extends BaseForm implements java.io.Serializable {

    private static final long serialVersionUID = -1928433225255806942L;
    private String searchBy="0";
    private List<LabelValueBean> searchByTypes;
    private String searchValue;
    private Double invoiceAmount;
    private String invoiceOperator = CommonConstants.NOT_EQUAL;
    private List<LabelValueBean> invoiceOperators;
    private String showAssociatedCompanies = CommonConstants.NO;
    private String searchDateBy = CommonConstants.INVOICE_DATE;
    private String showAR = CommonConstants.NO;
    private String showInvoices = CommonConstants.STATUS_OPEN;
    private String showReadyToPayInvoices = CommonConstants.YES;
    private String showDisptuedInvoices = CommonConstants.NO;
    private String showRejectedInvoices = CommonConstants.NO;
    private String showVoidedInvoices = CommonConstants.NO;
    private String showAccruals = CommonConstants.NO;
    private String showAssignedAccruals = CommonConstants.NO;
    private String showInactiveAccruals = CommonConstants.NO;
    private String showInProgress = CommonConstants.NO;
    private boolean filterOpen;
    private List<AccountingBean> apInquiryList;
    private CustomerBean vendor;
    private List<TransactionBean> invoiceOrBlDetails;
    private String blNumber;
    private String transactionType;
    private String fileName;
    private String message;
    private Integer pageNo = 1;
    private Integer currentPageSize = 100;
    private Integer totalPageSize = 0;
    private Integer noOfPages = 0;
    private Integer noOfRecords = 0;
    private String sortBy;
    private String orderBy = "desc";
    private String batchId;

    public List<AccountingBean> getApInquiryList() {
        return apInquiryList;
    }

    public void setApInquiryList(List<AccountingBean> apInquiryList) {
        this.apInquiryList = apInquiryList;
    }

    public CustomerBean getVendor() {
        return vendor;
    }

    public void setVendor(CustomerBean vendor) {
        this.vendor = vendor;
    }

    public Double getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(Double invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getInvoiceOperator() {
        return invoiceOperator;
    }

    public void setInvoiceOperator(String invoiceOperator) {
        this.invoiceOperator = invoiceOperator;
    }

    public List<LabelValueBean> getInvoiceOperators() {
        if (CommonUtils.isEmpty(invoiceOperators)) {
            invoiceOperators = new ArrayList<LabelValueBean>();
            invoiceOperators.add(new LabelValueBean(CommonConstants.EQUAL, CommonConstants.EQUAL));
            invoiceOperators.add(new LabelValueBean(CommonConstants.NOT_EQUAL, CommonConstants.NOT_EQUAL));
            invoiceOperators.add(new LabelValueBean(CommonConstants.GREATER_THAN, CommonConstants.GREATER_THAN));
            invoiceOperators.add(new LabelValueBean(CommonConstants.LESS_THAN, CommonConstants.LESS_THAN));
            invoiceOperators.add(new LabelValueBean(CommonConstants.GREATER_THAN_EQUAL, CommonConstants.GREATER_THAN_EQUAL));
            invoiceOperators.add(new LabelValueBean(CommonConstants.LESS_THAN_EQUAL, CommonConstants.LESS_THAN_EQUAL));
        }
        return invoiceOperators;
    }

    public void setInvoiceOperators(List<LabelValueBean> invoiceOperators) {
        this.invoiceOperators = invoiceOperators;
    }

    public String getSearchBy() {
        return searchBy;
    }

    public List<LabelValueBean> getSearchByTypes() {
        if (CommonUtils.isEmpty(searchByTypes)) {
            searchByTypes = new ArrayList<LabelValueBean>();
            searchByTypes.add(new LabelValueBean("Select One", "0"));
            searchByTypes.add(new LabelValueBean("Invoice Number", CommonConstants.SEARCH_BY_INVOICE_NUMBER));
            searchByTypes.add(new LabelValueBean("Invoice Amount", CommonConstants.SEARCH_BY_INVOICE_AMOUNT));
            searchByTypes.add(new LabelValueBean("Dock Receipt", CommonConstants.SEARCH_BY_DOCK_RECEIPT));
            searchByTypes.add(new LabelValueBean("Voyage", CommonConstants.SEARCH_BY_VOYAGE));
            searchByTypes.add(new LabelValueBean("Container Number", CommonConstants.SEARCH_BY_CONTAINER));
            searchByTypes.add(new LabelValueBean("Booking Number", CommonConstants.SEARCH_BY_BOOKING_NUMBER));
            searchByTypes.add(new LabelValueBean("House Bill", CommonConstants.SEARCH_BY_HOUSE_BILL));
            searchByTypes.add(new LabelValueBean("Sub-House Bill", CommonConstants.SEARCH_BY_SUB_HOUSE_BILL));
            searchByTypes.add(new LabelValueBean("Master Bill", CommonConstants.SEARCH_BY_MASTER_BILL));
            searchByTypes.add(new LabelValueBean("Check Number", CommonConstants.SEARCH_BY_CHECK_NUMBER));
            searchByTypes.add(new LabelValueBean("Check Amount", CommonConstants.SEARCH_BY_CHECK_AMOUNT));
        }
        return searchByTypes;
    }

    public void setSearchByTypes(List<LabelValueBean> searchByTypes) {
        this.searchByTypes = searchByTypes;
    }

    public void setSearchBy(String searchBy) {
        this.searchBy = searchBy;
    }

    public String getSearchDateBy() {
        return searchDateBy;
    }

    public void setSearchDateBy(String searchDateBy) {
        this.searchDateBy = searchDateBy;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    public String getShowAR() {
        return showAR;
    }

    public void setShowAR(String showAR) {
        this.showAR = showAR;
    }

    public String getShowAccruals() {
        return showAccruals;
    }

    public void setShowAccruals(String showAccruals) {
        this.showAccruals = showAccruals;
    }

    public String getShowAssignedAccruals() {
        return showAssignedAccruals;
    }

    public void setShowAssignedAccruals(String showAssignedAccruals) {
        this.showAssignedAccruals = showAssignedAccruals;
    }

    public String getShowAssociatedCompanies() {
        return showAssociatedCompanies;
    }

    public void setShowAssociatedCompanies(String showAssociatedCompanies) {
        this.showAssociatedCompanies = showAssociatedCompanies;
    }

    public String getShowDisptuedInvoices() {
        return showDisptuedInvoices;
    }

    public void setShowDisptuedInvoices(String showDisptuedInvoices) {
        this.showDisptuedInvoices = showDisptuedInvoices;
    }

    public String getShowInProgress() {
        return showInProgress;
    }

    public void setShowInProgress(String showInProgress) {
        this.showInProgress = showInProgress;
    }

    public String getShowInactiveAccruals() {
        return showInactiveAccruals;
    }

    public void setShowInactiveAccruals(String showInactiveAccruals) {
        this.showInactiveAccruals = showInactiveAccruals;
    }

    public String getShowInvoices() {
        return showInvoices;
    }

    public void setShowInvoices(String showInvoices) {
        this.showInvoices = showInvoices;
    }

    public String getShowReadyToPayInvoices() {
        return showReadyToPayInvoices;
    }

    public void setShowReadyToPayInvoices(String showReadyToPayInvoices) {
        this.showReadyToPayInvoices = showReadyToPayInvoices;
    }

    public String getShowRejectedInvoices() {
        return showRejectedInvoices;
    }

    public void setShowRejectedInvoices(String showRejectedInvoices) {
        this.showRejectedInvoices = showRejectedInvoices;
    }

    public String getShowVoidedInvoices() {
        return showVoidedInvoices;
    }

    public void setShowVoidedInvoices(String showVoidedInvoices) {
        this.showVoidedInvoices = showVoidedInvoices;
    }

    public boolean isFilterOpen() {
        return filterOpen;
    }

    public void setFilterOpen(boolean filterOpen) {
        this.filterOpen = filterOpen;
    }

    public List<TransactionBean> getInvoiceOrBlDetails() {
        return invoiceOrBlDetails;
    }

    public void setInvoiceOrBlDetails(List<TransactionBean> invoiceOrBlDetails) {
        this.invoiceOrBlDetails = invoiceOrBlDetails;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public String getBlNumber() {
        return blNumber;
    }

    public void setBlNumber(String blNumber) {
        this.blNumber = blNumber;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

}
