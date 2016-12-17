package com.gp.cvst.logisoft.struts.form;

import org.apache.struts.action.ActionForm;

public class AccountPayableForm extends ActionForm {

    private static final long serialVersionUID = -4162984915698770440L;
    private String action;
    private String vendor;
    private String datefrom;
    private String dateto;
    private String vendornumber;
    private String voyage;
    private String totalamountpaid;
    private String billofladding;
    private String blNumber;
    private String invoicenumber;
    private String transactionType;
    private String invoiceamount;
    private String outstandingrecievables;
    private String dept;
    private String netamount;
    private String current;
    private String thirtydays;
    private String sixtydays;
    private String nintydays;
    private String total;
    private String showOnlyMyAPEntries = "No";
    private String onlyAP = "Yes";
    private String onlyAR = "No";
    private String chparent = "No";
    private String showHold = "Yes";
    private String[] pay;
    private String[] hold;
    private String[] release;
    private String payIndex;
    private String holdIndex;
    private String releaseIndex;
    private String checkType;
    private String feedbackMessage;
    private String pageNo;
    private String totalPages;
    private String sortOrder;
    private String sortBy;

    public String getFeedbackMessage() {
	return feedbackMessage;
    }

    public void setFeedbackMessage(String feedbackMessage) {
	this.feedbackMessage = feedbackMessage;
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

    public String getBillofladding() {
	return billofladding;
    }

    public void setBillofladding(String billofladding) {
	this.billofladding = billofladding;
    }

    public String getAction() {
	return action;
    }

    public void setAction(String action) {
	this.action = action;
    }

    public String getCurrent() {
	return current;
    }

    public void setCurrent(String current) {
	this.current = current;
    }

    public String getDatefrom() {
	return datefrom;
    }

    public void setDatefrom(String datefrom) {
	this.datefrom = datefrom;
    }

    public String getDateto() {
	return dateto;
    }

    public void setDateto(String dateto) {
	this.dateto = dateto;
    }

    public String getDept() {
	return dept;
    }

    public void setDept(String dept) {
	this.dept = dept;
    }

    public String getInvoiceamount() {
	return invoiceamount;
    }

    public void setInvoiceamount(String invoiceamount) {
	this.invoiceamount = invoiceamount;
    }

    public String getInvoicenumber() {
	return invoicenumber;
    }

    public void setInvoicenumber(String invoicenumber) {
	this.invoicenumber = invoicenumber;
    }

    public String getNetamount() {
	return netamount;
    }

    public void setNetamount(String netamount) {
	this.netamount = netamount;
    }

    public String getNintydays() {
	return nintydays;
    }

    public void setNintydays(String nintydays) {
	this.nintydays = nintydays;
    }

    public String getOutstandingrecievables() {
	return outstandingrecievables;
    }

    public void setOutstandingrecievables(String outstandingrecievables) {
	this.outstandingrecievables = outstandingrecievables;
    }

    public String getSixtydays() {
	return sixtydays;
    }

    public void setSixtydays(String sixtydays) {
	this.sixtydays = sixtydays;
    }

    public String getThirtydays() {
	return thirtydays;
    }

    public void setThirtydays(String thirtydays) {
	this.thirtydays = thirtydays;
    }

    public String getTotal() {
	return total;
    }

    public void setTotal(String total) {
	this.total = total;
    }

    public String getTotalamountpaid() {
	return totalamountpaid;
    }

    public void setTotalamountpaid(String totalamountpaid) {
	this.totalamountpaid = totalamountpaid;
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

    public String getVoyage() {
	return voyage;
    }

    public void setVoyage(String voyage) {
	this.voyage = voyage;
    }

    public String getOnlyAP() {
	return onlyAP;
    }

    public void setOnlyAP(String onlyAP) {
	this.onlyAP = onlyAP;
    }

    public String getOnlyAR() {
	return onlyAR;
    }

    public void setOnlyAR(String onlyAR) {
	this.onlyAR = onlyAR;
    }

    public String[] getHold() {
	return hold;
    }

    public void setHold(String[] hold) {
	this.hold = hold;
    }

    public String[] getPay() {
	return pay;
    }

    public void setPay(String[] pay) {
	this.pay = pay;
    }

    public String getChparent() {
	return chparent;
    }

    public void setChparent(String chparent) {
	this.chparent = chparent;
    }

    public String getCheckType() {
	return checkType;
    }

    public void setCheckType(String checkType) {
	this.checkType = checkType;
    }

    public String getShowHold() {
	return showHold;
    }

    public void setShowHold(String showHold) {
	this.showHold = showHold;
    }

    public String getReleaseIndex() {
	return releaseIndex;
    }

    public void setReleaseIndex(String releaseIndex) {
	this.releaseIndex = releaseIndex;
    }

    public String[] getRelease() {
	return release;
    }

    public void setRelease(String[] release) {
	this.release = release;
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

    public String getShowOnlyMyAPEntries() {
	return showOnlyMyAPEntries;
    }

    public void setShowOnlyMyAPEntries(String showOnlyMyAPEntries) {
	this.showOnlyMyAPEntries = showOnlyMyAPEntries;
    }

    public String getPageNo() {
	return pageNo;
    }

    public void setPageNo(String pageNo) {
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

    public String getTotalPages() {
	return totalPages;
    }

    public void setTotalPages(String totalPages) {
	this.totalPages = totalPages;
    }
}
