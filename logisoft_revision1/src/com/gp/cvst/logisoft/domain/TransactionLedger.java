package com.gp.cvst.logisoft.domain;

import java.util.Date;

import com.gp.cong.common.DateUtils;
import com.gp.cvst.logisoft.beans.TransactionBean;

/**
 * TransactionLedger generated by MyEclipse - Hibernate Tools
 */
public class TransactionLedger implements java.io.Serializable {

    // Fields
    private static final long serialVersionUID = 4795349876387496703L;
    private Integer transactionId;
    private String billLaddingNo;
    private String manifestFlag;
    private String noticeNumber;
    private String chargeCode;
    private Date transactionDate;
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
    private Integer chargeId;
    private Integer costId;
    private String readyToPost;
    private String billTo;
    private String customerReferenceNo;
    private String orgTerminal;
    private String destination;
    private String fwdName;
    private String fwdNo;
    private String consName;
    private String consNo;
    private String thirdptyName;
    private String thirdptyNo;
    private String agentName;
    private String agentNo;
    private String creditHold;
    private String shipNo;
    private String shipName;
    private String billingTerminal;
    private String correctionFlag;
    private String dateAsString;
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
    private Integer apBatchId;
    private Integer arBatchId;
    private String docReceipt;
    private String inlvoy;
    private Date postedDate;
    private String apCostKey;
    private String shipmentType;
    private String description;
    private Date createdOn;
    private Integer createdBy;
    private Date updatedOn;
    private Integer updatedBy;
    private String correctionNotice;
    private String blueScreenChargeCode;
    private Date postedToGlDate;
    private Date sailingDate;
    private Double differenceAmount;
    private Double newAmount;
    private String bookingNo;
    private String accrualsCorrectionFlag;
    private String accrualsCorrectionNumber;
    private boolean directGlAccount;
    private String terminal;

    public Double getDifferenceAmount() {
        return differenceAmount;
    }

    public void setDifferenceAmount(Double differenceAmount) {
        this.differenceAmount = differenceAmount;
    }

    public Double getNewAmount() {
        return newAmount;
    }

    public void setNewAmount(Double newAmount) {
        this.newAmount = newAmount;
    }

    public String getCorrectionNotice() {
        return correctionNotice;
    }

    public void setCorrectionNotice(String correctionNotice) {
        this.correctionNotice = correctionNotice;
    }

    // Constructors
    /**
     * @return the apCostKey
     */
    public String getApCostKey() {
        return apCostKey;
    }

    /**
     * @param apCostKey the apCostKey to set
     */
    public void setApCostKey(String apCostKey) {
        this.apCostKey = apCostKey;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShipmentType() {
        return shipmentType;
    }

    public void setShipmentType(String shipmentType) {
        this.shipmentType = shipmentType;
    }

    public Double getBalanceInProcess() {
        return balanceInProcess;
    }

    public void setBalanceInProcess(Double balanceInProcess) {
        this.balanceInProcess = balanceInProcess;
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

    public String getDateAsString() throws Exception {
        if (null != postedDate) {
            return DateUtils.formatDate(postedDate, "MM/dd/yyyy H:mm");
        } else {
            return DateUtils.formatDate(new Date(), "MM/dd/yyyy H:mm");
        }
    }

    public void setDateAsString(String dateAsString) {
        this.dateAsString = dateAsString;
    }

    public String getCorrectionFlag() {
        return correctionFlag;
    }

    public void setCorrectionFlag(String correctionFlag) {
        this.correctionFlag = correctionFlag;
    }

    /**
     * default constructor
     */
    public TransactionLedger() {
    }

    /**
     * minimal constructor
     */
    public TransactionLedger(String glAccountNumber, Double transactionAmt, String custName, String custNo) {
        this.glAccountNumber = glAccountNumber;
        this.transactionAmt = transactionAmt;
        this.custName = custName;
        this.custNo = custNo;
    }

    /**
     * DWR constructor
     */
    public TransactionLedger(String custName, String custNo, String blNumber, String invoiceNumber, String invoiceDate, String costcode, Double transactionAmt) throws Exception {
        this.custName = custName;
        this.custNo = custNo;
        this.transactionAmt = transactionAmt;
        this.billLaddingNo = blNumber;
        this.invoiceNumber = invoiceNumber;
        this.chargeCode = costcode;
        if (null == invoiceDate || invoiceDate.trim().equals("")) {
            this.transactionDate = new Date();
        } else {
            this.transactionDate = DateUtils.parseDate(invoiceDate, "MM/dd/yyyy");
        }
    }

    /*
     * Constructor with argument as TransactionBean Object
     */
    public TransactionLedger(TransactionBean transactionBean) throws Exception {
        this.custName = transactionBean.getCustomer().trim();
        this.custNo = transactionBean.getCustomerNo().trim();
        this.transactionAmt = Double.parseDouble(transactionBean.getAmount());
        this.billLaddingNo = transactionBean.getBillofLadding();
        this.invoiceNumber = transactionBean.getInvoiceOrBl();
        this.chargeCode = transactionBean.getChargeCode();
        if (null == transactionBean.getInvoiceDate() || transactionBean.getInvoiceDate().trim().equals("")) {
            this.postedDate = new Date();
        } else {
            this.postedDate = DateUtils.parseDate(transactionBean.getInvoiceDate(), "MM/dd/yyyy H:mm");
        }
        this.transactionDate = new Date();
        this.sailingDate = new Date();
    }

    public TransactionLedger(TransactionLedger transactionLedger) {

        this.billLaddingNo = transactionLedger.getBillLaddingNo();
        this.chargeCode = transactionLedger.getChargeCode();
        this.transactionDate = transactionLedger.getTransactionDate();
        this.glAccountNumber = transactionLedger.getGlAccountNumber();
        this.transactionAmt = transactionLedger.getTransactionAmt();
        this.currencyCode = transactionLedger.getCurrencyCode();
        this.invoiceNumber = transactionLedger.getInvoiceNumber();
        this.chequeNumber = transactionLedger.getChequeNumber();
        this.subledgerSourceCode = transactionLedger.getSubledgerSourceCode();
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
        this.readyToPost = transactionLedger.getReadyToPost();
        this.billTo = transactionLedger.getBillTo();
        this.customerReferenceNo = transactionLedger.getCustomerReferenceNo();
        this.orgTerminal = transactionLedger.getOrgTerminal();
        this.destination = transactionLedger.getDestination();
        this.fwdName = transactionLedger.getFwdName();
        this.fwdNo = transactionLedger.getFwdNo();
        this.consName = transactionLedger.getConsName();
        this.consNo = transactionLedger.getConsNo();
        this.thirdptyName = transactionLedger.getThirdptyName();
        this.thirdptyNo = transactionLedger.getThirdptyNo();
        this.agentName = transactionLedger.getAgentName();
        this.agentNo = transactionLedger.getAgentNo();
        this.creditHold = transactionLedger.getCreditHold();
        this.shipNo = transactionLedger.getShipNo();
        this.shipName = transactionLedger.getShipName();
        this.billingTerminal = transactionLedger.getBillingTerminal();
        this.costId = transactionLedger.getCostId();
        this.balanceInProcess = transactionLedger.getBalanceInProcess();
        this.docReceipt = transactionLedger.getDocReceipt();
        this.shipmentType = transactionLedger.getShipmentType();
        this.description = transactionLedger.getDescription();
        this.postedDate = transactionLedger.getPostedDate();
        this.apCostKey = transactionLedger.getApCostKey();
        this.blueScreenChargeCode = transactionLedger.getBlueScreenChargeCode();
        this.sailingDate = transactionLedger.getSailingDate();
        this.bookingNo = transactionLedger.getBookingNo();
    }

    public TransactionLedger(Transaction transaction) {

        this.billLaddingNo = transaction.getBillLaddingNo();
        this.chargeCode = transaction.getChargeCode();
        this.postedDate = transaction.getTransactionDate();
        this.transactionDate = new Date();
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
        //this.readyToPost = transaction.get;
        this.billTo = transaction.getBillTo();
        this.customerReferenceNo = transaction.getCustomerReferenceNo();
        this.orgTerminal = transaction.getOrgTerminal();
        this.destination = transaction.getDestination();
        this.fwdName = transaction.getFwdName();
        this.fwdNo = transaction.getFwdNo();
        this.consName = transaction.getConsName();
        this.consNo = transaction.getConsNo();
        this.thirdptyName = transaction.getThirdptyName();
        this.thirdptyNo = transaction.getThirdptyNo();
        this.agentName = transaction.getAgentName();
        this.agentNo = transaction.getAgentNo();
        this.creditHold = transaction.getCreditHold();
        this.balanceInProcess = transaction.getBalanceInProcess();
        this.docReceipt = transaction.getDocReceipt();
        this.bookingNo = transaction.getBookingNo();
    }

    /**
     * full constructor
     */
    public TransactionLedger(String billLaddingNo, String chargeCode, Date transactionDate, String glAccountNumber, Double transactionAmt, String currencyCode, String invoiceNumber, String chequeNumber, String subledgerSourceCode, String journalEntryNumber, String lineItemNumber, String custName, String custNo, String transactionType, Double balance, String blTerms, String subHouseBl, String voyageNo, String containerNo, String masterBl, String vesselNo, String status, Date dueDate, String readyToPost, String billTo, String customerReferenceNo, String originatingTerminal, String destination, String fwdName, String fwdNo, String consName, String consNo, String thirdPtyName, String thirdPtyNo, String agentName, String agentNo, String creditHold, String shipNo, String shipName, String billingTerminal) {
        this.billLaddingNo = billLaddingNo;
        this.chargeCode = chargeCode;
        this.transactionDate = transactionDate;
        this.glAccountNumber = glAccountNumber;
        this.transactionAmt = transactionAmt;
        this.currencyCode = currencyCode;
        this.invoiceNumber = invoiceNumber;
        this.chequeNumber = chequeNumber;
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
        this.readyToPost = readyToPost;
        this.billTo = billTo;
        this.customerReferenceNo = customerReferenceNo;
        this.orgTerminal = originatingTerminal;
        this.destination = destination;
        this.fwdName = fwdName;
        this.fwdNo = fwdNo;
        this.consName = consName;
        this.consNo = consNo;
        this.thirdptyName = thirdPtyName;
        this.thirdptyNo = thirdPtyNo;
        this.agentName = agentName;
        this.agentNo = agentNo;
        this.creditHold = creditHold;
        this.shipNo = shipNo;
        this.shipName = shipName;
        this.billingTerminal = billingTerminal;
    }

    //public TransactionLedger(Transaction transaction){}
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
        return this.transactionDate;
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

    public String getInvoiceNumber() {
        return this.invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getChequeNumber() {
        return this.chequeNumber;
    }

    public void setChequeNumber(String chequeNumber) {
        this.chequeNumber = chequeNumber;
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

    public String getReadyToPost() {
        return this.readyToPost;
    }

    public void setReadyToPost(String readyToPost) {
        this.readyToPost = readyToPost;
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

    public String getOrgTerminal() {
        return orgTerminal;
    }

    public void setOrgTerminal(String orgTerminal) {
        this.orgTerminal = orgTerminal;
    }

    public String getDestination() {
        return this.destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getFwdName() {
        return this.fwdName;
    }

    public void setFwdName(String fwdName) {
        this.fwdName = fwdName;
    }

    public String getFwdNo() {
        return this.fwdNo;
    }

    public void setFwdNo(String fwdNo) {
        this.fwdNo = fwdNo;
    }

    public String getConsName() {
        return this.consName;
    }

    public void setConsName(String consName) {
        this.consName = consName;
    }

    public String getConsNo() {
        return this.consNo;
    }

    public void setConsNo(String consNo) {
        this.consNo = consNo;
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

    public String getAgentName() {
        return this.agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAgentNo() {
        return this.agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo;
    }

    public String getCreditHold() {
        return this.creditHold;
    }

    public void setCreditHold(String creditHold) {
        this.creditHold = creditHold;
    }

    public String getShipNo() {
        return this.shipNo;
    }

    public void setShipNo(String shipNo) {
        this.shipNo = shipNo;
    }

    public String getShipName() {
        return this.shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public String getBillingTerminal() {
        return this.billingTerminal;
    }

    public void setBillingTerminal(String billingTerminal) {
        this.billingTerminal = billingTerminal;
    }

    public Integer getChargeId() {
        return chargeId;
    }

    public void setChargeId(Integer chargeId) {
        this.chargeId = chargeId;
    }

    public Integer getCostId() {
        return costId;
    }

    public void setCostId(Integer costId) {
        this.costId = costId;
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

    public String getDocReceipt() {
        return docReceipt;
    }

    public void setDocReceipt(String docReceipt) {
        this.docReceipt = docReceipt;
    }

    public String getInlvoy() {
        return inlvoy;
    }

    public void setInlvoy(String inlvoy) {
        this.inlvoy = inlvoy;
    }

    public Date getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
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

    public String getManifestFlag() {
        return manifestFlag;
    }

    public void setManifestFlag(String manifestFlag) {
        this.manifestFlag = manifestFlag;
    }

    public String getNoticeNumber() {
        return noticeNumber;
    }

    public void setNoticeNumber(String noticeNumber) {
        this.noticeNumber = noticeNumber;
    }

    public String getBlueScreenChargeCode() {
        return blueScreenChargeCode;
    }

    public void setBlueScreenChargeCode(String blueScreenChargeCode) {
        this.blueScreenChargeCode = blueScreenChargeCode;
    }

    public Date getPostedToGlDate() {
        return postedToGlDate;
    }

    public void setPostedToGlDate(Date postedToGlDate) {
        this.postedToGlDate = postedToGlDate;
    }

    public Date getSailingDate() {
        return sailingDate;
    }

    public void setSailingDate(Date sailingDate) {
        this.sailingDate = sailingDate;
    }

    public String getBookingNo() {
        return bookingNo;
    }

    public void setBookingNo(String bookingNo) {
        this.bookingNo = bookingNo;
    }

    public String getAccrualsCorrectionFlag() {
        return accrualsCorrectionFlag;
    }

    public void setAccrualsCorrectionFlag(String accrualsCorrectionFlag) {
        this.accrualsCorrectionFlag = accrualsCorrectionFlag;
    }

    public String getAccrualsCorrectionNumber() {
        return accrualsCorrectionNumber;
    }

    public void setAccrualsCorrectionNumber(String accrualsCorrectionNumber) {
        this.accrualsCorrectionNumber = accrualsCorrectionNumber;
    }

    public boolean isDirectGlAccount() {
        return directGlAccount;
    }

    public void setDirectGlAccount(boolean directGlAccount) {
        this.directGlAccount = directGlAccount;
    }

    public String getTerminal() {
        return terminal;
}

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

}
