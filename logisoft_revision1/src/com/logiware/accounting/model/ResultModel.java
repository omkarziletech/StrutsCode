package com.logiware.accounting.model;

import java.io.Serializable;

/**
 *
 * @author Lakshmi Naryanan
 */
public class ResultModel implements Serializable {

    private String customerName;
    private String customerNumber;
    private String vendorName;
    private String vendorNumber;
    private String invoiceNumber;
    private String blNumber;
    private String invoiceOrBl;
    private String invoiceDate;
    private String reportingDate;
    private String postedDate;
    private String paymentDate;
    private String invoiceAmount;
    private String accruedAmount;
    private String balance;
    private String balanceInProcess;
    private String transactionType;
    private String status;
    private String chargeCode;
    private String costCode;
    private String glAccount;
    private String bluescreenCostCode;
    private String shipmentType;
    private String checkNumber;
    private String voyage;
    private String container;
    private String dockReceipt;
    private String costId;
    private String notes;
    private String id;
    private String apAmount;
    private String arAmount;
    private String netAmount;
    private String age30Amount;
    private String age60Amount;
    private String age90Amount;
    private String age91Amount;
    private String dueDate;
    private String invoiceBalance;
    private String clearedDate;
    private String reference;
    private boolean overhead;
    private boolean manualNotes;
    private boolean uploaded;
    private String noteModuleId;
    private String noteRefId;
    private String documentId;
    private String subledger;
    private String arBatchId;
    private String apBatchId;
    private String transactionDate;
    private String amount;
    private String debit;
    private String credit;
    private String journalEntryId;
    private String lineItemId;
    private String creditTerms;
    private String creditHold;
    private Integer age;
    private String correctionNotice;
    private String tpName;
    private String tpNumber;
    private String type;
    private String source;
    private String sourceId;
    private String sourceType;
    private String arInvoiceId;
    private boolean includedInBatch;
    private String transactionAmount;
    private String batchNumber;
    private String adjustmentDate;
    private String adjustmentAmount;
    private String user;
    private String vesselNumber;
    private String voyageNumber;
    private String masterBl;
    private String subHouseBl;
    private String amsHouseBl;
    private String blTerms;
    private String containerNumber;
    private String billTo;
    private String terminal;
    private Integer creditDays;
    private boolean deletable;
    private Boolean balanceMatches;
    private boolean blDisputed;
    private String correctionFlag;
    private boolean payCheck;

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

    public String getBlNumber() {
        return blNumber;
    }

    public void setBlNumber(String blNumber) {
        this.blNumber = blNumber;
    }

    public String getInvoiceOrBl() {
        return invoiceOrBl;
    }

    public void setInvoiceOrBl(String invoiceOrBl) {
        this.invoiceOrBl = invoiceOrBl;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getReportingDate() {
        return reportingDate;
    }

    public void setReportingDate(String reportingDate) {
        this.reportingDate = reportingDate;
    }

    public String getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(String postedDate) {
        this.postedDate = postedDate;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(String invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getAccruedAmount() {
        return accruedAmount;
    }

    public void setAccruedAmount(String accruedAmount) {
        this.accruedAmount = accruedAmount;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getBalanceInProcess() {
        return balanceInProcess;
    }

    public void setBalanceInProcess(String balanceInProcess) {
        this.balanceInProcess = balanceInProcess;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

    public String getCostCode() {
        return costCode;
    }

    public void setCostCode(String costCode) {
        this.costCode = costCode;
    }

    public String getGlAccount() {
        return glAccount;
    }

    public void setGlAccount(String glAccount) {
        this.glAccount = glAccount;
    }

    public String getBluescreenCostCode() {
        return bluescreenCostCode;
    }

    public void setBluescreenCostCode(String bluescreenCostCode) {
        this.bluescreenCostCode = bluescreenCostCode;
    }

    public String getShipmentType() {
        return shipmentType;
    }

    public void setShipmentType(String shipmentType) {
        this.shipmentType = shipmentType;
    }

    public String getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(String checkNumber) {
        this.checkNumber = checkNumber;
    }

    public String getVoyage() {
        return voyage;
    }

    public void setVoyage(String voyage) {
        this.voyage = voyage;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public String getDockReceipt() {
        return dockReceipt;
    }

    public void setDockReceipt(String dockReceipt) {
        this.dockReceipt = dockReceipt;
    }

    public String getCostId() {
        return costId;
    }

    public void setCostId(String costId) {
        this.costId = costId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApAmount() {
        return apAmount;
    }

    public void setApAmount(String apAmount) {
        this.apAmount = apAmount;
    }

    public String getArAmount() {
        return arAmount;
    }

    public void setArAmount(String arAmount) {
        this.arAmount = arAmount;
    }

    public String getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(String netAmount) {
        this.netAmount = netAmount;
    }

    public String getAge30Amount() {
        return age30Amount;
    }

    public void setAge30Amount(String age30Amount) {
        this.age30Amount = age30Amount;
    }

    public String getAge60Amount() {
        return age60Amount;
    }

    public void setAge60Amount(String age60Amount) {
        this.age60Amount = age60Amount;
    }

    public String getAge90Amount() {
        return age90Amount;
    }

    public void setAge90Amount(String age90Amount) {
        this.age90Amount = age90Amount;
    }

    public String getAge91Amount() {
        return age91Amount;
    }

    public void setAge91Amount(String age91Amount) {
        this.age91Amount = age91Amount;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getInvoiceBalance() {
        return invoiceBalance;
    }

    public void setInvoiceBalance(String invoiceBalance) {
        this.invoiceBalance = invoiceBalance;
    }

    public String getClearedDate() {
        return clearedDate;
    }

    public void setClearedDate(String clearedDate) {
        this.clearedDate = clearedDate;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public boolean isOverhead() {
        return overhead;
    }

    public void setOverhead(String overhead) {
        this.overhead = Boolean.valueOf(overhead);
    }

    public boolean isManualNotes() {
        return manualNotes;
    }

    public void setManualNotes(String manualNotes) {
        this.manualNotes = Boolean.valueOf(manualNotes);
    }

    public boolean isUploaded() {
        return uploaded;
    }

    public void setUploaded(String uploaded) {
        this.uploaded = Boolean.valueOf(uploaded);
    }

    public String getNoteModuleId() {
        return noteModuleId;
    }

    public void setNoteModuleId(String noteModuleId) {
        this.noteModuleId = noteModuleId;
    }

    public String getNoteRefId() {
        return noteRefId;
    }

    public void setNoteRefId(String noteRefId) {
        this.noteRefId = noteRefId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getSubledger() {
        return subledger;
    }

    public void setSubledger(String subledger) {
        this.subledger = subledger;
    }

    public String getArBatchId() {
        return arBatchId;
    }

    public void setArBatchId(String arBatchId) {
        this.arBatchId = arBatchId;
    }

    public String getApBatchId() {
        return apBatchId;
    }

    public void setApBatchId(String apBatchId) {
        this.apBatchId = apBatchId;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDebit() {
        return debit;
    }

    public void setDebit(String debit) {
        this.debit = debit;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getJournalEntryId() {
        return journalEntryId;
    }

    public void setJournalEntryId(String journalEntryId) {
        this.journalEntryId = journalEntryId;
    }

    public String getLineItemId() {
        return lineItemId;
    }

    public void setLineItemId(String lineItemId) {
        this.lineItemId = lineItemId;
    }

    public String getCreditTerms() {
        return creditTerms;
    }

    public void setCreditTerms(String creditTerms) {
        this.creditTerms = creditTerms;
    }

    public String getCreditHold() {
        return creditHold;
    }

    public void setCreditHold(String creditHold) {
        this.creditHold = creditHold;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getCorrectionNotice() {
        return correctionNotice;
    }

    public void setCorrectionNotice(String correctionNotice) {
        this.correctionNotice = correctionNotice;
    }

    public String getTpName() {
        return tpName;
    }

    public void setTpName(String tpName) {
        this.tpName = tpName;
    }

    public String getTpNumber() {
        return tpNumber;
    }

    public void setTpNumber(String tpNumber) {
        this.tpNumber = tpNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getArInvoiceId() {
        return arInvoiceId;
    }

    public void setArInvoiceId(String arInvoiceId) {
        this.arInvoiceId = arInvoiceId;
    }

    public boolean isIncludedInBatch() {
        return includedInBatch;
    }

    public void setIncludedInBatch(String includedInBatch) {
        this.includedInBatch = Boolean.valueOf(includedInBatch);
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getAdjustmentDate() {
        return adjustmentDate;
    }

    public void setAdjustmentDate(String adjustmentDate) {
        this.adjustmentDate = adjustmentDate;
    }

    public String getAdjustmentAmount() {
        return adjustmentAmount;
    }

    public void setAdjustmentAmount(String adjustmentAmount) {
        this.adjustmentAmount = adjustmentAmount;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getVesselNumber() {
        return vesselNumber;
    }

    public void setVesselNumber(String vesselNumber) {
        this.vesselNumber = vesselNumber;
    }

    public String getVoyageNumber() {
        return voyageNumber;
    }

    public void setVoyageNumber(String voyageNumber) {
        this.voyageNumber = voyageNumber;
    }

    public String getMasterBl() {
        return masterBl;
    }

    public void setMasterBl(String masterBl) {
        this.masterBl = masterBl;
    }

    public String getSubHouseBl() {
        return subHouseBl;
    }

    public void setSubHouseBl(String subHouseBl) {
        this.subHouseBl = subHouseBl;
    }

    public String getAmsHouseBl() {
        return amsHouseBl;
    }

    public void setAmsHouseBl(String amsHouseBl) {
        this.amsHouseBl = amsHouseBl;
    }

    public String getBlTerms() {
        return blTerms;
    }

    public void setBlTerms(String blTerms) {
        this.blTerms = blTerms;
    }

    public String getContainerNumber() {
        return containerNumber;
    }

    public void setContainerNumber(String containerNumber) {
        this.containerNumber = containerNumber;
    }

    public String getBillTo() {
        return billTo;
    }

    public void setBillTo(String billTo) {
        this.billTo = billTo;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public Integer getCreditDays() {
        return creditDays;
    }

    public void setCreditDays(Integer creditDays) {
        this.creditDays = creditDays;
    }

    public boolean isDeletable() {
        return deletable;
    }

    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }

    public boolean isBlDisputed() {
        return blDisputed;
    }

    public void setBlDisputed(boolean blDisputed) {
        this.blDisputed = blDisputed;
    }

    
    public Boolean getBalanceMatches() {
        return balanceMatches;
    }

    public void setBalanceMatches(Boolean balanceMatches) {
        this.balanceMatches = balanceMatches;
    }

    public String getCorrectionFlag() {
        return correctionFlag;
    }

    public void setCorrectionFlag(String correctionFlag) {
        this.correctionFlag = correctionFlag;
    }

    public boolean isPayCheck() {
        return payCheck;
    }

    public void setPayCheck(boolean payCheck) {
        this.payCheck = payCheck;
    }

}
