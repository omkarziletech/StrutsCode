/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.struts.form.lcl;

/**
 *
 * @author Logiware
 */
public class LclImportPaymentForm extends LogiwareActionForm {

    private String methodName;
    private String fileNumberId;
    private String fileNumber;
    private String checkNumber;
    private String amount;
    private String paidBy;
    private String paidDate;
    private String bookingAcId;
    private String totalchrgeAmount;
    private String remarks;
    private String paymentType;
    //hidden Values
    private Long bkgTransid;
    private String bookingactaId;
    private String chargesAmount;
    //Storage Method
    private String lastFdDate;
    private String moduleName;
    private String vendorNo;
    private String invoiceNo;
    private boolean formChangeFlag;

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public String getFileNumberId() {
        return fileNumberId;
    }

    public void setFileNumberId(String fileNumberId) {
        this.fileNumberId = fileNumberId;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(String checkNumber) {
        this.checkNumber = checkNumber;
    }

    public String getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(String paidBy) {
        this.paidBy = paidBy;
    }

    public String getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(String paidDate) {
        this.paidDate = paidDate;
    }

    public String getBookingAcId() {
        return bookingAcId;
    }

    public void setBookingAcId(String bookingAcId) {
        this.bookingAcId = bookingAcId;
    }

    public String getTotalchrgeAmount() {
        return totalchrgeAmount;
    }

    public void setTotalchrgeAmount(String totalchrgeAmount) {
        this.totalchrgeAmount = totalchrgeAmount;
    }

    public String getBookingactaId() {
        return bookingactaId;
    }

    public void setBookingactaId(String bookingactaId) {
        this.bookingactaId = bookingactaId;
    }

    public Long getBkgTransid() {
        return bkgTransid;
    }

    public void setBkgTransid(Long bkgTransid) {
        this.bkgTransid = bkgTransid;
    }

    public String getChargesAmount() {
        return chargesAmount;
    }

    public void setChargesAmount(String chargesAmount) {
        this.chargesAmount = chargesAmount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getLastFdDate() {
        return lastFdDate;
    }

    public void setLastFdDate(String lastFdDate) {
        this.lastFdDate = lastFdDate;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getVendorNo() {
        return vendorNo;
    }

    public void setVendorNo(String vendorNo) {
        this.vendorNo = vendorNo;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public boolean isFormChangeFlag() {
        return formChangeFlag;
    }

    public void setFormChangeFlag(boolean formChangeFlag) {
        this.formChangeFlag = formChangeFlag;
    }
}
