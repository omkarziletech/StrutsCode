package com.gp.cvst.logisoft.domain;

import java.util.Date;

/**
 * Transaction generated by MyEclipse - Hibernate Tools
 */
public class Transaction implements java.io.Serializable {

    private static final long serialVersionUID = -6943960680252771282L;
    private Integer transactionId;
    private String correctionNotice;
    private String billLaddingNo;
    private String chargeCode;
    private Date transactionDate;
    private Date postedDate;
    private String glAccountNumber;
    private Double transactionAmt;
    private String currencyCode;
    private String invoiceNumber;
    private String chequeNumber;
    private String subledgerSourceCode;
    private String journalEntryNumber;
    private String lineItemNumber;
    private String custName;
    private String custNo;
    private String transactionType;
    private Double balance;
    private String blTerms;
    private String subHouseBl;
    private String voyageNo;
    private String containerNo;
    private String masterBl;
    private String vesselNo;
    private String status;
    private Date dueDate;
    private String billTo;
    private String customerReferenceNo;
    private String destination;
    private String orgTerminal;
    private Date sailingDate;
    private String shipperNo;
    private String shipperName;
    private String fwdName;
    private String fwdNo;
    private String consName;
    private String consNo;
    private String thirdptyName;
    private String thirdptyNo;
    private String agentName;
    private String agentNo;
    private String creditHold;
    private String correctionFlag;
    private Integer creditTerms;
    private String bankNumber;
    private String bankName;
    private String bankAccountNumber;
    private Date checkDate;
    private String cleared;
    private Date clearedDate;
    private String reconciled;
    private Date reconciledDate;
    private String confirmationNumber;
    private String voidTransaction;
    private Date voidDate;
    private String reprint;
    private Date reprintDate;
    private Double balanceInProcess;
    private String bookingNo;
    private String quotationNo;
    private Integer apBatchId;
    private Integer arBatchId;
    private String paymentMethod;
    private String docReceipt;
    private Date createdOn;
    private Integer createdBy;
    private Date updatedOn;
    private Integer updatedBy;
    private Integer paidBy;
    private Integer approvedBy;
    private Date closedDate;
    private Integer achBatchSequence;
    private String manifestFlag;
    private Date paidOn;
    private Integer owner;
    private boolean emailed = false;
    private String sealNo;
    private Date eta;
    private String vesselName;
    private String steamShipLine;
    private boolean removedFromHold;
    private String searchInvoiceNumber;
    private Integer apInvoiceId;
    private String apInvoiceStatus;
    private double apInvoiceAmount;

    public Integer getCreditTerms() {
        return creditTerms;
    }

    public void setCreditTerms(Integer creditTerms) {
        this.creditTerms = creditTerms;
    }

    public String getCorrectionFlag() {
        return correctionFlag;
    }

    public void setCorrectionFlag(String correctionFlag) {
        this.correctionFlag = correctionFlag;
    }

    public String getCreditHold() {
        return creditHold;
    }

    public void setCreditHold(String creditHold) {
        this.creditHold = creditHold;
    }

    /**
     * default constructor
     */
    public Transaction() {
    }

    /**
     * minimal constructor
     */
    public Transaction(String glAccountNumber, Double transactionAmt, String custName, String custNo) {
        this.glAccountNumber = glAccountNumber;
        this.transactionAmt = transactionAmt;
        this.custName = custName;
        this.custNo = custNo;
    }

    public Transaction(Transaction transaction) {
        this.billLaddingNo = transaction.getBillLaddingNo();
        this.chargeCode = transaction.getChargeCode();
        this.transactionDate = transaction.getTransactionDate();
        this.postedDate = transaction.getPostedDate();
        this.glAccountNumber = transaction.getGlAccountNumber();
        this.transactionAmt = transaction.getTransactionAmt();
        this.currencyCode = transaction.getCurrencyCode();
        this.invoiceNumber = transaction.getInvoiceNumber();
        this.chequeNumber = transaction.getChequeNumber();
        this.subledgerSourceCode = transaction.getSubledgerSourceCode();
        this.journalEntryNumber = transaction.getJournalEntryNumber();
        this.lineItemNumber = transaction.getLineItemNumber();
        this.custName = transaction.getCustName();
        this.custNo = transaction.getCustNo();
        this.transactionType = transaction.getTransactionType();
        this.balance = transaction.getBalance();
        this.blTerms = transaction.getBlTerms();
        this.subHouseBl = transaction.getSubHouseBl();
        this.voyageNo = transaction.getVoyageNo();
        this.containerNo = transaction.getContainerNo();
        this.masterBl = transaction.getMasterBl();
        this.vesselNo = transaction.getVesselNo();
        this.status = transaction.getStatus();
        this.dueDate = transaction.getDueDate();
        this.billTo = transaction.getBillTo();
        this.customerReferenceNo = transaction.getCustomerReferenceNo();
        this.destination = transaction.getDestination();
        this.orgTerminal = transaction.getOrgTerminal();
        this.sailingDate = transaction.getSailingDate();
        this.fwdName = transaction.getFwdName();
        this.fwdNo = transaction.getFwdNo();
        this.consName = transaction.getConsName();
        this.consNo = transaction.getConsNo();
        this.thirdptyName = transaction.getThirdptyName();
        this.thirdptyNo = transaction.getThirdptyNo();
        this.agentName = transaction.getAgentName();
        this.agentNo = transaction.getAgentNo();
        this.creditHold = transaction.getCreditHold();
        this.correctionFlag = transaction.getCorrectionFlag();
        this.creditTerms = transaction.getCreditTerms();
        this.balanceInProcess = transaction.getBalanceInProcess();
        this.checkDate = transaction.getCheckDate();
        this.docReceipt = transaction.getDocReceipt();
        this.manifestFlag = transaction.getManifestFlag();
        this.correctionNotice = transaction.getCorrectionNotice();
        this.bookingNo = transaction.getBookingNo();
    }

    public Transaction(TransactionLedger transactionLedger) {
        this.billLaddingNo = transactionLedger.getBillLaddingNo();
        this.chargeCode = transactionLedger.getChargeCode();
        this.transactionDate = transactionLedger.getTransactionDate();
        this.postedDate = transactionLedger.getPostedDate();
        this.glAccountNumber = transactionLedger.getGlAccountNumber();
        this.transactionAmt = transactionLedger.getTransactionAmt();
        this.currencyCode = transactionLedger.getCurrencyCode();
        this.invoiceNumber = transactionLedger.getInvoiceNumber();
        this.chequeNumber = transactionLedger.getChequeNumber();
        this.subledgerSourceCode = transactionLedger.getSubledgerSourceCode();
        this.journalEntryNumber = transactionLedger.getJournalEntryNumber();
        this.lineItemNumber = transactionLedger.getLineItemNumber();
        this.custName = transactionLedger.getCustName();
        this.custNo = transactionLedger.getCustNo();
        this.transactionType = transactionLedger.getTransactionType();
        this.balance = transactionLedger.getBalance();
        this.blTerms = transactionLedger.getBlTerms();
        this.subHouseBl = transactionLedger.getSubHouseBl();
        this.voyageNo = transactionLedger.getVoyageNo();
        this.containerNo = transactionLedger.getContainerNo();
        this.masterBl = transactionLedger.getMasterBl();
        this.vesselNo = transactionLedger.getVesselNo();
        this.status = transactionLedger.getStatus();
        this.dueDate = transactionLedger.getDueDate();
        this.billTo = transactionLedger.getBillTo();
        this.customerReferenceNo = transactionLedger.getCustomerReferenceNo();
        this.destination = transactionLedger.getDestination();
        this.orgTerminal = transactionLedger.getOrgTerminal();
        //this.sailingDate  = transactionLedger.gets;
        this.fwdName = transactionLedger.getFwdName();
        this.fwdNo = transactionLedger.getFwdNo();
        this.consName = transactionLedger.getConsName();
        this.consNo = transactionLedger.getConsNo();
        this.thirdptyName = transactionLedger.getThirdptyName();
        this.thirdptyNo = transactionLedger.getThirdptyNo();
        this.agentName = transactionLedger.getAgentName();
        this.agentNo = transactionLedger.getAgentNo();
        this.creditHold = transactionLedger.getCreditHold();
        this.correctionFlag = transactionLedger.getCorrectionFlag();
        this.checkDate = transactionLedger.getCheckDate();
        this.docReceipt = transactionLedger.getDocReceipt();
        this.manifestFlag = transactionLedger.getManifestFlag();
        this.correctionNotice = transactionLedger.getCorrectionNotice();
        this.bookingNo = transactionLedger.getBookingNo();
    }

    /**
     * full constructor
     */
    public Transaction(String billLaddingNo, String chargeCode, Date transactionDate, String glAccountNumber, Double transactionAmt, String currencyCode, String invoiceNumber, Integer chequeNumber, String subledgerSourceCode, String journalEntryNumber, String lineItemNumber, String custName, String custNo, String transactionType, Double balance, String blTerms, String subHouseBl, String voyageNo, String containerNo, String masterBl, String vesselNo, String status, Date dueDate, String billTo, String customerReferenceNo) {
        this.billLaddingNo = billLaddingNo;
        this.chargeCode = chargeCode;
        this.transactionDate = transactionDate;
        this.glAccountNumber = glAccountNumber;
        this.transactionAmt = transactionAmt;
        this.currencyCode = currencyCode;
        this.invoiceNumber = invoiceNumber;

        this.subledgerSourceCode = subledgerSourceCode;
        this.journalEntryNumber = journalEntryNumber;
        this.lineItemNumber = lineItemNumber;
        this.custName = custName;
        this.custNo = custNo;
        this.transactionType = transactionType;
        this.balance = balance;
        this.blTerms = blTerms;
        this.subHouseBl = subHouseBl;
        this.voyageNo = voyageNo;
        this.containerNo = containerNo;
        this.masterBl = masterBl;
        this.vesselNo = vesselNo;
        this.status = status;
        this.dueDate = dueDate;
        this.billTo = billTo;
        this.customerReferenceNo = customerReferenceNo;
    }

// Property accessors
    public Integer getTransactionId() {
        return this.transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public String getBillLaddingNo() {
        return this.billLaddingNo;
    }

    public void setBillLaddingNo(String billLaddingNo) {
        this.billLaddingNo = billLaddingNo;
    }

    public String getChargeCode() {
        return this.chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getGlAccountNumber() {
        return this.glAccountNumber;
    }

    public void setGlAccountNumber(String glAccountNumber) {
        this.glAccountNumber = glAccountNumber;
    }

    public Double getTransactionAmt() {
        return this.transactionAmt;
    }

    public void setTransactionAmt(Double transactionAmt) {
        this.transactionAmt = transactionAmt;
    }

    public String getCurrencyCode() {
        return this.currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getSubledgerSourceCode() {
        return this.subledgerSourceCode;
    }

    public void setSubledgerSourceCode(String subledgerSourceCode) {
        this.subledgerSourceCode = subledgerSourceCode;
    }

    public String getJournalEntryNumber() {
        return this.journalEntryNumber;
    }

    public void setJournalEntryNumber(String journalEntryNumber) {
        this.journalEntryNumber = journalEntryNumber;
    }

    public String getLineItemNumber() {
        return this.lineItemNumber;
    }

    public void setLineItemNumber(String lineItemNumber) {
        this.lineItemNumber = lineItemNumber;
    }

    public String getCustName() {
        return this.custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustNo() {
        return this.custNo;
    }

    public void setCustNo(String custNo) {
        this.custNo = custNo;
    }

    public String getTransactionType() {
        return this.transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Double getBalance() {
        return this.balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getBlTerms() {
        return this.blTerms;
    }

    public void setBlTerms(String blTerms) {
        this.blTerms = blTerms;
    }

    public String getSubHouseBl() {
        return this.subHouseBl;
    }

    public void setSubHouseBl(String subHouseBl) {
        this.subHouseBl = subHouseBl;
    }

    public String getVoyageNo() {
        return this.voyageNo;
    }

    public void setVoyageNo(String voyageNo) {
        this.voyageNo = voyageNo;
    }

    public String getContainerNo() {
        return this.containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public String getMasterBl() {
        return this.masterBl;
    }

    public void setMasterBl(String masterBl) {
        this.masterBl = masterBl;
    }

    public String getVesselNo() {
        return this.vesselNo;
    }

    public void setVesselNo(String vesselNo) {
        this.vesselNo = vesselNo;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDueDate() {
        return this.dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getBillTo() {
        return this.billTo;
    }

    public void setBillTo(String billTo) {
        this.billTo = billTo;
    }

    public String getCustomerReferenceNo() {
        return this.customerReferenceNo;
    }

    public void setCustomerReferenceNo(String customerReferenceNo) {
        this.customerReferenceNo = customerReferenceNo;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getOrgTerminal() {
        return orgTerminal;
    }

    public void setOrgTerminal(String orgTerminal) {
        this.orgTerminal = orgTerminal;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo;
    }

    public String getConsName() {
        return consName;
    }

    public void setConsName(String consName) {
        this.consName = consName;
    }

    public String getConsNo() {
        return consNo;
    }

    public void setConsNo(String consNo) {
        this.consNo = consNo;
    }

    public String getFwdName() {
        return fwdName;
    }

    public void setFwdName(String fwdName) {
        this.fwdName = fwdName;
    }

    public String getFwdNo() {
        return fwdNo;
    }

    public void setFwdNo(String fwdNo) {
        this.fwdNo = fwdNo;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Date getSailingDate() {
        return sailingDate;
    }

    public void setSailingDate(Date sailingDate) {
        this.sailingDate = sailingDate;
    }

    public String getThirdptyName() {
        return thirdptyName;
    }

    public void setThirdptyName(String thirdptyName) {
        this.thirdptyName = thirdptyName;
    }

    public String getThirdptyNo() {
        return thirdptyNo;
    }

    public void setThirdptyNo(String thirdptyNo) {
        this.thirdptyNo = thirdptyNo;
    }

    public String getChequeNumber() {
        return chequeNumber;
    }

    public void setChequeNumber(String chequeNumber) {
        this.chequeNumber = chequeNumber;
    }

    public String getBankNumber() {
        return bankNumber;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public Date getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

    public String getCleared() {
        return cleared;
    }

    public void setCleared(String cleared) {
        this.cleared = cleared;
    }

    public Date getClearedDate() {
        return clearedDate;
    }

    public void setClearedDate(Date clearedDate) {
        this.clearedDate = clearedDate;
    }

    public String getReconciled() {
        return reconciled;
    }

    public void setReconciled(String reconciled) {
        this.reconciled = reconciled;
    }

    public Date getReconciledDate() {
        return reconciledDate;
    }

    public void setReconciledDate(Date reconciledDate) {
        this.reconciledDate = reconciledDate;
    }

    public String getConfirmationNumber() {
        return confirmationNumber;
    }

    public void setConfirmationNumber(String confirmationNumber) {
        this.confirmationNumber = confirmationNumber;
    }

    public String getVoidTransaction() {
        return voidTransaction;
    }

    public void setVoidTransaction(String voidTransaction) {
        this.voidTransaction = voidTransaction;
    }

    public Date getVoidDate() {
        return voidDate;
    }

    public void setVoidDate(Date voidDate) {
        this.voidDate = voidDate;
    }

    public String getReprint() {
        return reprint;
    }

    public void setReprint(String reprint) {
        this.reprint = reprint;
    }

    public Date getReprintDate() {
        return reprintDate;
    }

    public void setReprintDate(Date reprintDate) {
        this.reprintDate = reprintDate;
    }

    public Double getBalanceInProcess() {
        return balanceInProcess;
    }

    public void setBalanceInProcess(Double balanceInProcess) {
        this.balanceInProcess = balanceInProcess;
    }

    public String getShipperNo() {
        return shipperNo;
    }

    public void setShipperNo(String shipperNo) {
        this.shipperNo = shipperNo;
    }

    public String getShipperName() {
        return shipperName;
    }

    public void setShipperName(String shipperName) {
        this.shipperName = shipperName;
    }

    public String getBookingNo() {
        return bookingNo;
    }

    public void setBookingNo(String bookingNo) {
        this.bookingNo = bookingNo;
    }

    public String getQuotationNo() {
        return quotationNo;
    }

    public void setQuotationNo(String quotationNo) {
        this.quotationNo = quotationNo;
    }

    public Integer getApBatchId() {
        return apBatchId;
    }

    public void setApBatchId(Integer apBatchId) {
        this.apBatchId = apBatchId;
    }

    public Integer getArBatchId() {
        return arBatchId;
    }

    public void setArBatchId(Integer arBatchId) {
        this.arBatchId = arBatchId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDocReceipt() {
        return docReceipt;
    }

    public void setDocReceipt(String docReceipt) {
        this.docReceipt = docReceipt;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Integer getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(Integer paidBy) {
        this.paidBy = paidBy;
    }

    public Integer getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Integer approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Date getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(Date closedDate) {
        this.closedDate = closedDate;
    }

    public Integer getAchBatchSequence() {
        return achBatchSequence;
    }

    public void setAchBatchSequence(Integer achBatchSequence) {
        this.achBatchSequence = achBatchSequence;
    }

    public String getManifestFlag() {
        return manifestFlag;
    }

    public void setManifestFlag(String manifestFlag) {
        this.manifestFlag = manifestFlag;
    }

    public String getCorrectionNotice() {
        return correctionNotice;
    }

    public void setCorrectionNotice(String correctionNotice) {
        this.correctionNotice = correctionNotice;
    }

    public Date getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }

    public Integer getOwner() {
        return owner;
    }

    public void setOwner(Integer owner) {
        this.owner = owner;
    }

    public Date getPaidOn() {
        return paidOn;
    }

    public void setPaidOn(Date paidOn) {
        this.paidOn = paidOn;
    }

    public boolean isEmailed() {
        return emailed;
    }

    public void setEmailed(boolean emailed) {
        this.emailed = emailed;
    }

    public Date getEta() {
        return eta;
    }

    public void setEta(Date eta) {
        this.eta = eta;
    }

    public String getSealNo() {
        return sealNo;
    }

    public void setSealNo(String sealNo) {
        this.sealNo = sealNo;
    }

    public String getSteamShipLine() {
        return steamShipLine;
    }

    public void setSteamShipLine(String steamShipLine) {
        this.steamShipLine = steamShipLine;
    }

    public String getVesselName() {
        return vesselName;
    }

    public void setVesselName(String vesselName) {
        this.vesselName = vesselName;
    }

    public boolean isRemovedFromHold() {
        return removedFromHold;
    }

    public void setRemovedFromHold(boolean removedFromHold) {
        this.removedFromHold = removedFromHold;
    }

    public String getSearchInvoiceNumber() {
        return searchInvoiceNumber;
    }

    public void setSearchInvoiceNumber(String searchInvoiceNumber) {
        this.searchInvoiceNumber = searchInvoiceNumber;
    }

    public Integer getApInvoiceId() {
        return apInvoiceId;
    }

    public void setApInvoiceId(Integer apInvoiceId) {
        this.apInvoiceId = apInvoiceId;
    }

    public String getApInvoiceStatus() {
        return apInvoiceStatus;
    }

    public void setApInvoiceStatus(String apInvoiceStatus) {
        this.apInvoiceStatus = apInvoiceStatus;
    }

    public double getApInvoiceAmount() {
        return apInvoiceAmount;
    }

    public void setApInvoiceAmount(double apInvoiceAmount) {
        this.apInvoiceAmount = apInvoiceAmount;
    }
}