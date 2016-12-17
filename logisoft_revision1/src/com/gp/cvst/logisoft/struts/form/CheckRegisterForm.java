package com.gp.cvst.logisoft.struts.form;

import org.apache.struts.action.ActionForm;

public class CheckRegisterForm extends ActionForm {

    private static final long serialVersionUID = 4213408897080114557L;
    private String account;
    private String bankReconcileDate;
    private String startCheckNumber;
    private String endCheckNumber;
    private String invoicenumber;
    private String invoiceAmount;
    private String showStatus;
    private String checks;
    private String invoiceOperator;
    private String batchNumber;
    private String payMethod;
    private String bankAccountNo;
    private String glAccountNo;
    private String vendorName;
    private String vendorNumber;
    private String buttonValue;
    private Integer pageNo;
    private Integer currentPageSize;
    private Integer totalPageSize;
    private String sortBy;
    private String sortOrder;
    private String voidIds;
    private String reprintIds;
    private String transactionIds;
    private String custNo;
    private String paymentDate;
    private String paymentMethod;
    private String checkNo;

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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getBankReconcileDate() {
        return bankReconcileDate;
    }

    public void setBankReconcileDate(String bankReconcileDate) {
        this.bankReconcileDate = bankReconcileDate;
    }

    public String getStartCheckNumber() {
        return startCheckNumber;
    }

    public void setStartCheckNumber(String startCheckNumber) {
        this.startCheckNumber = startCheckNumber;
    }

    public String getEndCheckNumber() {
        return endCheckNumber;
    }

    public void setEndCheckNumber(String endCheckNumber) {
        this.endCheckNumber = endCheckNumber;
    }

    public String getInvoicenumber() {
        return invoicenumber;
    }

    public void setInvoicenumber(String invoicenumber) {
        this.invoicenumber = invoicenumber;
    }

    public String getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(String invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getShowStatus() {
        return showStatus;
    }

    public void setShowStatus(String showStatus) {
        this.showStatus = showStatus;
    }

    public String getChecks() {
        return checks;
    }

    public void setChecks(String checks) {
        this.checks = checks;
    }

    public String getInvoiceOperator() {
        return invoiceOperator;
    }

    public void setInvoiceOperator(String invoiceOperator) {
        this.invoiceOperator = invoiceOperator;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    /**
     * @return the glAccountNo
     */
    public String getGlAccountNo() {
        return glAccountNo;
    }

    /**
     * @param glAccountNo the glAccountNo to set
     */
    public void setGlAccountNo(String glAccountNo) {
        this.glAccountNo = glAccountNo;
    }

    public String getButtonValue() {
        return buttonValue;
    }

    public void setButtonValue(String buttonValue) {
        this.buttonValue = buttonValue;
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

    public Integer getTotalPageSize() {
        return totalPageSize;
    }

    public void setTotalPageSize(Integer totalPageSize) {
        this.totalPageSize = totalPageSize;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getReprintIds() {
        return reprintIds;
    }

    public void setReprintIds(String reprintIds) {
        this.reprintIds = reprintIds;
    }

    public String getVoidIds() {
        return voidIds;
    }

    public void setVoidIds(String voidIds) {
        this.voidIds = voidIds;
    }

    public String getCheckNo() {
        return checkNo;
    }

    public void setCheckNo(String checkNo) {
        this.checkNo = checkNo;
    }

    public String getCustNo() {
        return custNo;
    }

    public void setCustNo(String custNo) {
        this.custNo = custNo;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTransactionIds() {
        return transactionIds;
    }

    public void setTransactionIds(String transactionIds) {
        this.transactionIds = transactionIds;
    }
}
