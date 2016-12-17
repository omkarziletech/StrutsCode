package com.gp.cvst.logisoft.struts.form;

import com.gp.cong.common.CommonConstants;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.gp.cvst.logisoft.beans.TransactionBean;

public class AccrualsForm extends ActionForm {

    private static final long serialVersionUID = 1849302556851735367L;
    private String buttonValue;
    private String vendor;
    private String amountallocated;
    private String duedate;
    private String vendornumber;
    private String amountremain;
    private String invoicenumber;
    private String invoicedate;
    private String term = "11344";
    private String invoiceamount;
    private String rejectinvoice;
    private String category;
    private String alternatevendor;
    private String showrejectedaccurals;
    private String number;
    private String showassignaccurals;
    private String inActiveAccruals = CommonConstants.NO;
    private String paidAccrual = CommonConstants.NO;
    private String custname;
    private String[] assign;
    private String[] hold;
    private String payIndex;
    private String holdIndex;
    private String[] reject;
    private String rejectIndex;
    private String rejectedAccruals = CommonConstants.NO;
    private String showAccruals = CommonConstants.YES;
    private String assignedAccruals = CommonConstants.NO;
    private List<TransactionBean> assignedCustomersList;
    private List<TransactionBean> rejectedCustomersList;
    private String docNumber;
    private String accrualsLimit;
    private boolean hideAccruals;
    private String dispute;
    private String inActiveTransactions;
    private String undoInActiveTransactions;
    private String undoInProgressTransactions;
    private String assignedTransactions;
    private String rejectedTransactions;
    private String unRejectedTransactions;
    private String disputedTransactions;
    private String undoDisputedTransactions;
    private String otherVendorTransactions;
    private String accrualsAction;
    private String comments;
    private String customerReferenceNumber;
    private String status;
    private String oldAccrualId;
    private String newAccrualId;
    private String accrualIds;
    private String tempAssignIds;
    private String tempDisputeIds;
    private String tempVoidIds;
    private String shipmentTypes;
    private String chargeCodes;
    private String suffixValues;
    private String blueScreenChargeCode;
    private String glAccount;
    private String fileName;
    private Integer pageNo;
    private Integer currentPageSize;
    private Integer totalPageSize;
    private String sortBy;
    private String sortOrder;
    private String accessMode;
    private String subType;
    private String fileNo;

    public String getDispute() {
        return dispute;
    }

    public void setDispute(String dispute) {
        this.dispute = dispute;
    }

    public String getAccrualsLimit() {
        return accrualsLimit;
    }

    public void setAccrualsLimit(String accrualsLimit) {
        this.accrualsLimit = accrualsLimit;
    }

    public boolean isHideAccruals() {
        return hideAccruals;
    }

    public void setHideAccruals(boolean hideAccruals) {
        this.hideAccruals = hideAccruals;
    }

    public String getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
    }

    public String getRejectedAccruals() {
        return rejectedAccruals;
    }

    public void setRejectedAccruals(String rejectedAccruals) {
        this.rejectedAccruals = rejectedAccruals;
    }

    public String getRejectIndex() {
        return rejectIndex;
    }

    public void setRejectIndex(String rejectIndex) {
        this.rejectIndex = rejectIndex;
    }

    public String[] getReject() {
        return reject;
    }

    public void setReject(String[] reject) {
        this.reject = reject;
    }

    public String getHoldIndex() {
        return holdIndex;
    }

    public void setHoldIndex(String holdIndex) {
        this.holdIndex = holdIndex;
    }

    public String getPayIndex() {
        return payIndex;
    }

    public void setPayIndex(String payIndex) {
        this.payIndex = payIndex;
    }

    public String[] getHold() {
        return hold;
    }

    public void setHold(String[] hold) {
        this.hold = hold;
    }

    public String[] getAssign() {
        return assign;
    }

    public void setAssign(String[] assign) {
        this.assign = assign;
    }

    public String getCustname() {
        return custname;
    }

    public void setCustname(String custname) {
        this.custname = custname;
    }

    public String getAmountallocated() {
        return amountallocated;
    }

    public void setAmountallocated(String amountallocated) {
        this.amountallocated = amountallocated;
    }

    public String getAmountremain() {
        return amountremain;
    }

    public void setAmountremain(String amountremain) {
        this.amountremain = amountremain;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDuedate() {
        return duedate;
    }

    public void setDuedate(String duedate) {
        this.duedate = duedate;
    }

    public String getInvoiceamount() {
        return invoiceamount;
    }

    public void setInvoiceamount(String invoiceamount) {
        this.invoiceamount = invoiceamount;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getRejectinvoice() {
        return rejectinvoice;
    }

    public void setRejectinvoice(String rejectinvoice) {
        this.rejectinvoice = rejectinvoice;
    }

    public String getShowassignaccurals() {
        return showassignaccurals;
    }

    public void setShowassignaccurals(String showassignaccurals) {
        this.showassignaccurals = showassignaccurals;
    }

    public String getPaidAccrual() {
        return paidAccrual;
    }

    public void setPaidAccrual(String paidAccrual) {
        this.paidAccrual = paidAccrual;
    }

    public String getShowrejectedaccurals() {
        return showrejectedaccurals;
    }

    public void setShowrejectedaccurals(String showrejectedaccurals) {
        this.showrejectedaccurals = showrejectedaccurals;
    }

    public String getShowAccruals() {
        return showAccruals;
    }

    public void setShowAccruals(String showAccruals) {
        this.showAccruals = showAccruals;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getVendornumber() {
        return vendornumber;
    }

    public void setVendornumber(String vendornumber) {
        this.vendornumber = vendornumber;
    }

    /**
     * Method validate
     * @param mapping
     * @param request
     * @return ActionErrors
     */
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

    public String getButtonValue() {
        return buttonValue;
    }

    public void setButtonValue(String buttonValue) {
        this.buttonValue = buttonValue;
    }

    public String getInvoicenumber() {
        return invoicenumber;
    }

    public void setInvoicenumber(String invoicenumber) {
        this.invoicenumber = invoicenumber;
    }

    public String getInvoicedate() {
        return invoicedate;
    }

    public void setInvoicedate(String invoicedate) {
        this.invoicedate = invoicedate;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getAlternatevendor() {
        return alternatevendor;
    }

    public void setAlternatevendor(String alternatevendor) {
        this.alternatevendor = alternatevendor;
    }

    public String getAssignedAccruals() {
        return assignedAccruals;
    }

    public void setAssignedAccruals(String assignedAccruals) {
        this.assignedAccruals = assignedAccruals;
    }

    public List<TransactionBean> getAssignedCustomersList() {
        return assignedCustomersList;
    }

    public void setAssignedCustomersList(List<TransactionBean> assignedCustomersList) {
        this.assignedCustomersList = assignedCustomersList;
    }

    public List<TransactionBean> getRejectedCustomersList() {
        return rejectedCustomersList;
    }

    public void setRejectedCustomersList(List<TransactionBean> rejectedCustomersList) {
        this.rejectedCustomersList = rejectedCustomersList;
    }

    public String getAssignedTransactions() {
        return assignedTransactions;
    }

    public void setAssignedTransactions(String assignedTransactions) {
        this.assignedTransactions = assignedTransactions;
    }

    public String getRejectedTransactions() {
        return rejectedTransactions;
    }

    public void setRejectedTransactions(String rejectedTransactions) {
        this.rejectedTransactions = rejectedTransactions;
    }

    public String getUnRejectedTransactions() {
        return unRejectedTransactions;
    }

    public void setUnRejectedTransactions(String unRejectedTransactions) {
        this.unRejectedTransactions = unRejectedTransactions;
    }

    public String getDisputedTransactions() {
        return disputedTransactions;
    }

    public void setDisputedTransactions(String disputedTransactions) {
        this.disputedTransactions = disputedTransactions;
    }

    public String getOtherVendorTransactions() {
        return otherVendorTransactions;
    }

    public void setOtherVendorTransactions(String otherVendorTransactions) {
        this.otherVendorTransactions = otherVendorTransactions;
    }

    public String getAccrualsAction() {
        return accrualsAction;
    }

    public void setAccrualsAction(String accrualsAction) {
        this.accrualsAction = accrualsAction;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCustomerReferenceNumber() {
        return customerReferenceNumber;
    }

    public void setCustomerReferenceNumber(String customerReferenceNumber) {
        this.customerReferenceNumber = customerReferenceNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOldAccrualId() {
        return oldAccrualId;
    }

    public void setOldAccrualId(String oldAccrualId) {
        this.oldAccrualId = oldAccrualId;
    }

    public String getNewAccrualId() {
        return newAccrualId;
    }

    public void setNewAccrualId(String newAccrualId) {
        this.newAccrualId = newAccrualId;
    }

    public String getAccrualIds() {
        return accrualIds;
    }

    public void setAccrualIds(String accrualIds) {
        this.accrualIds = accrualIds;
    }

    public String getTempAssignIds() {
        return tempAssignIds;
    }

    public void setTempAssignIds(String tempAssignIds) {
        this.tempAssignIds = tempAssignIds;
    }

    public String getTempDisputeIds() {
        return tempDisputeIds;
    }

    public void setTempDisputeIds(String tempDisputeIds) {
        this.tempDisputeIds = tempDisputeIds;
    }

    public String getTempVoidIds() {
        return tempVoidIds;
    }

    public void setTempVoidIds(String tempVoidIds) {
        this.tempVoidIds = tempVoidIds;
    }

    public String getChargeCodes() {
        return chargeCodes;
    }

    public void setChargeCodes(String chargeCodes) {
        this.chargeCodes = chargeCodes;
    }

    public String getShipmentTypes() {
        return shipmentTypes;
    }

    public void setShipmentTypes(String shipmentTypes) {
        this.shipmentTypes = shipmentTypes;
    }

    public String getSuffixValues() {
        return suffixValues;
    }

    public void setSuffixValues(String suffixValues) {
        this.suffixValues = suffixValues;
    }

    public String getInActiveTransactions() {
        return inActiveTransactions;
    }

    public void setInActiveTransactions(String inActiveTransactions) {
        this.inActiveTransactions = inActiveTransactions;
    }

    public String getInActiveAccruals() {
        return inActiveAccruals;
    }

    public void setInActiveAccruals(String showInActiveAccruals) {
        this.inActiveAccruals = showInActiveAccruals;
    }

    public String getUndoInActiveTransactions() {
        return undoInActiveTransactions;
    }

    public void setUndoInActiveTransactions(String undoInActiveTransactions) {
        this.undoInActiveTransactions = undoInActiveTransactions;
    }

    public String getUndoInProgressTransactions() {
        return undoInProgressTransactions;
    }

    public void setUndoInProgressTransactions(String undoInProgressTransactions) {
        this.undoInProgressTransactions = undoInProgressTransactions;
    }

    public String getBlueScreenChargeCode() {
        return blueScreenChargeCode;
    }

    public void setBlueScreenChargeCode(String blueScreenChargeCode) {
        this.blueScreenChargeCode = blueScreenChargeCode;
    }

    public String getGlAccount() {
        return glAccount;
    }

    public void setGlAccount(String glAccount) {
        this.glAccount = glAccount;
    }

    public String getUndoDisputedTransactions() {
        return undoDisputedTransactions;
    }

    public void setUndoDisputedTransactions(String undoDisputedTransactions) {
        this.undoDisputedTransactions = undoDisputedTransactions;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    public Integer getTotalPageSize() {
        return totalPageSize;
    }

    public void setTotalPageSize(Integer totalPageSize) {
        this.totalPageSize = totalPageSize;
    }

    public String getAccessMode() {
        return accessMode;
    }

    public void setAccessMode(String accessMode) {
        this.accessMode = accessMode;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }
    
}
