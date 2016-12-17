package com.logiware.bean;

import com.gp.cong.common.CommonUtils;
import com.gp.cvst.logisoft.domain.Transaction;
import com.gp.cvst.logisoft.domain.TransactionLedger;
import java.math.BigInteger;
import java.util.Date;

public class AccountingBean {

    private String vendorName;
    private String vendorNumber;
    private String subType;
    private String eciVendorNumber;
    private String apSpecialist;
    private String paymentMethod;
    private String tin;
    private String w9;
    private String credit;
    private String creditStatus;
    private String creditTerms;
    private Double creditLimit;
    private String apContact;
    private String customerName;
    private String customerNumber;
    private String customerType;
    private String invoiceNumber;
    private String invoiceOrBl;
    private String billOfLadding;
    private Date invoiceDate;
    private Date transactionDate;
    private Date dueDate;
    private Date paymentDate;
    private Date postedDate;
    private String customerReference;
    private Double balance;
    private Double balanceInProcess;
    private Double age0_30Balance;
    private Double age31_60Balance;
    private Double age61_90Balance;
    private Double age91Balance;
    private Double total;
    private Double amount;
    private Double transactionAmount;
    private Double invoiceAmount;
    private Double paymentAmount;
    private String bankName;
    private String bankAccountNumber;
    private String checkNumber;
    private String glAccountNumber;
    private String apBatchNumber;
    private String arBatchNumber;
    private String transactionType;
    private String status;
    private String transactionId;
    private String creditHold;
    private Integer creditTermsValue;
    private String blTerms;
    private String billTo;
    private String correctionFlag;
    private String correctionNotice;
    private String manifestFlag;
    private Integer age;
    private String noCredit;
    private String pastDue;
    private String overLimit;
    private String email;
    private boolean emailed;
    private String apCostKey;
    private String collector;
    private String voyage;
    private String quoteNumber;
    private String formattedDate;
    private String formattedPostedDate;
    private String formattedReportingDate;
    private String formattedDueDate;
    private String formattedBalance;
    private String formattedBalanceInProcess;
    private String formattedAmount;
    private String formattedPayment;
    private String bookingNumber;
    private String consignee;
    private String subledgerCode;
    private String chargeCode;
    private String formattedDebit;
    private String formattedCredit;
    private boolean excluded;
    private String tradingPartnerName;
    private String tradingPartnerNumber;
    private String paidAmount;
    private String adjustAmount;
    private String glAccount;
    private boolean selected;
    private String paymentId;
    private String savedInBatches;
    private String formattedPaymentDate;
    private String cleared;
    private String clearedDate;
    private String dockReceipt;
    private Integer apBatchId;
    private Integer arBatchId;
    private String invoiceNotes;
    private Double debitAmount;
    private Double creditAmount;
    private String lineItemId;
    private String costId;
    private String notPaid;
    private String inActive;
    private String enterDate;
    private String blueScreenAccount;
    //For SSMastersApproved
    private String moduleName;
    private String fileNo;
    private String sslineName;
    private String sslineNo;
    private String agentName;
    private String agentNo;
    private String etd;
    private String eta;
    private String prepaidOrCollect;
    private String formattedArrivalDate;
    private boolean hasDocuments;
    private boolean manualNotes;
    private String noteModuleId;
    private String noteRefId;
    private String blCreatedBy;
    private String arInvoiceId;
    private String ecuDesignation;
    private String ssMasterBl;
    private BigInteger headerId;
    private BigInteger ssMasterId;
    public AccountingBean() {
    }

    public AccountingBean(Transaction transaction) {
	this.vendorName = transaction.getCustName();
	this.vendorNumber = transaction.getCustNo();
	this.customerName = transaction.getCustName();
	this.customerNumber = transaction.getCustNo();
	this.balance = transaction.getBalance();
	this.balanceInProcess = transaction.getBalanceInProcess();
	this.transactionAmount = transaction.getTransactionAmt();
	this.invoiceAmount = transaction.getTransactionAmt();
	this.amount = transaction.getTransactionAmt();
	this.transactionDate = transaction.getTransactionDate();
	this.dueDate = transaction.getDueDate();
	this.invoiceDate = transaction.getTransactionDate();
	this.invoiceNumber = transaction.getInvoiceNumber();
	this.billOfLadding = transaction.getBillLaddingNo();
	if ((CommonUtils.isEmpty(transaction.getInvoiceNumber())) || (CommonUtils.isEqualIgnoreCase(transaction.getInvoiceNumber(), "PRE PAYMENT"))) {
	    this.invoiceOrBl = transaction.getBillLaddingNo();
	} else {
	    this.invoiceOrBl = transaction.getInvoiceNumber();
	}

	this.transactionType = transaction.getTransactionType();
	this.transactionId = (CommonUtils.isNotEmpty(transaction.getTransactionId()) ? transaction.getTransactionId().toString() : null);
	this.apBatchNumber = (CommonUtils.isNotEmpty(transaction.getApBatchId()) ? transaction.getApBatchId().toString() : null);
	this.arBatchNumber = (CommonUtils.isNotEmpty(transaction.getArBatchId()) ? transaction.getArBatchId().toString() : null);
	this.bankName = transaction.getBankName();
	this.bankAccountNumber = transaction.getBankAccountNumber();
	this.billTo = transaction.getBillTo();
	this.blTerms = transaction.getBlTerms();
	this.checkNumber = transaction.getChequeNumber();
	this.correctionFlag = transaction.getCorrectionFlag();
	this.manifestFlag = transaction.getManifestFlag();
	this.creditHold = transaction.getCreditHold();
	this.creditTermsValue = transaction.getCreditTerms();
	this.creditTerms = (CommonUtils.isNotEmpty(transaction.getCreditTerms()) ? transaction.getCreditTerms().toString() : null);
	this.customerReference = transaction.getCustomerReferenceNo();
	this.glAccountNumber = transaction.getGlAccountNumber();
	this.paymentMethod = transaction.getPaymentMethod();
	this.paymentDate = transaction.getCheckDate();
	this.postedDate = transaction.getPostedDate();
    }

    public AccountingBean(TransactionLedger transactionLedger) {
	this.customerNumber = transactionLedger.getCustNo();
	this.customerName = transactionLedger.getCustName();
	this.transactionDate = transactionLedger.getTransactionDate();
	this.invoiceDate = transactionLedger.getTransactionDate();
	this.transactionAmount = transactionLedger.getTransactionAmt();
	this.balance = transactionLedger.getBalance();
	this.balanceInProcess = transactionLedger.getBalanceInProcess();
	this.billOfLadding = transactionLedger.getBillLaddingNo();
	this.transactionType = transactionLedger.getTransactionType();
	this.blTerms = transactionLedger.getBlTerms();
	this.voyage = transactionLedger.getVoyageNo();
	this.transactionId = transactionLedger.getTransactionId().toString();
	this.billTo = transactionLedger.getBillTo();
	this.apBatchNumber = (null != transactionLedger.getApBatchId() ? transactionLedger.getApBatchId().toString() : null);
	this.arBatchNumber = (null != transactionLedger.getArBatchId() ? transactionLedger.getArBatchId().toString() : null);
	this.chargeCode = transactionLedger.getChargeCode();
	this.invoiceOrBl = (null == transactionLedger.getBillLaddingNo() ? transactionLedger.getInvoiceNumber() : transactionLedger.getBillLaddingNo());
	this.status = transactionLedger.getStatus();
	this.glAccountNumber = transactionLedger.getGlAccountNumber();
	this.subledgerCode = transactionLedger.getSubledgerSourceCode();
	this.postedDate = transactionLedger.getPostedDate();
	this.creditTerms = transactionLedger.getBlTerms();
	this.customerReference = transactionLedger.getCustomerReferenceNo();
	this.manifestFlag = transactionLedger.getManifestFlag();
	this.correctionNotice = transactionLedger.getCorrectionNotice();
    }

    public String getVendorName() {
	return this.vendorName;
    }

    public void setVendorName(String vendorName) {
	this.vendorName = vendorName;
    }

    public String getVendorNumber() {
	return this.vendorNumber;
    }

    public void setVendorNumber(String vendorNumber) {
	this.vendorNumber = vendorNumber;
    }

    public String getSubType() {
	return this.subType;
    }

    public void setSubType(String subType) {
	this.subType = subType;
    }

    public String getEciVendorNumber() {
	return this.eciVendorNumber;
    }

    public void setEciVendorNumber(String eciVendorNumber) {
	this.eciVendorNumber = eciVendorNumber;
    }

    public String getApSpecialist() {
	return this.apSpecialist;
    }

    public void setApSpecialist(String apSpecialist) {
	this.apSpecialist = apSpecialist;
    }

    public String getPaymentMethod() {
	return this.paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
	this.paymentMethod = paymentMethod;
    }

    public String getTin() {
	return this.tin;
    }

    public void setTin(String tin) {
	this.tin = tin;
    }

    public String getW9() {
	return this.w9;
    }

    public void setW9(String w9) {
	this.w9 = w9;
    }

    public String getCredit() {
	return this.credit;
    }

    public void setCredit(String credit) {
	this.credit = credit;
    }

    public String getCreditStatus() {
	return this.creditStatus;
    }

    public void setCreditStatus(String creditStatus) {
	this.creditStatus = creditStatus;
    }

    public String getCreditTerms() {
	return this.creditTerms;
    }

    public void setCreditTerms(String creditTerms) {
	this.creditTerms = creditTerms;
    }

    public Double getCreditLimit() {
	return this.creditLimit;
    }

    public void setCreditLimit(Double creditLimit) {
	this.creditLimit = creditLimit;
    }

    public String getApContact() {
	return this.apContact;
    }

    public void setApContact(String apContact) {
	this.apContact = apContact;
    }

    public String getCustomerName() {
	return this.customerName;
    }

    public void setCustomerName(String customerName) {
	this.customerName = customerName;
    }

    public String getCustomerNumber() {
	return this.customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
	this.customerNumber = customerNumber;
    }

    public String getInvoiceNumber() {
	return this.invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
	this.invoiceNumber = invoiceNumber;
    }

    public String getInvoiceOrBl() {
	return this.invoiceOrBl;
    }

    public void setInvoiceOrBl(String invoiceOrBl) {
	this.invoiceOrBl = invoiceOrBl;
    }

    public String getBillOfLadding() {
	return this.billOfLadding;
    }

    public void setBillOfLadding(String billOfLadding) {
	this.billOfLadding = billOfLadding;
    }

    public Date getInvoiceDate() {
	return this.invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
	this.invoiceDate = invoiceDate;
    }

    public Date getTransactionDate() {
	return this.transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
	this.transactionDate = transactionDate;
    }

    public Date getDueDate() {
	return this.dueDate;
    }

    public void setDueDate(Date dueDate) {
	this.dueDate = dueDate;
    }

    public Date getPaymentDate() {
	return this.paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
	this.paymentDate = paymentDate;
    }

    public Date getPostedDate() {
	return this.postedDate;
    }

    public void setPostedDate(Date postedDate) {
	this.postedDate = postedDate;
    }

    public String getCustomerReference() {
	return this.customerReference;
    }

    public void setCustomerReference(String customerReference) {
	this.customerReference = customerReference;
    }

    public Double getBalance() {
	return this.balance;
    }

    public void setBalance(Double balance) {
	this.balance = balance;
    }

    public Double getBalanceInProcess() {
	return this.balanceInProcess;
    }

    public void setBalanceInProcess(Double balanceInProcess) {
	this.balanceInProcess = balanceInProcess;
    }

    public Double getAge0_30Balance() {
	return this.age0_30Balance;
    }

    public void setAge0_30Balance(Double age0_30Balance) {
	this.age0_30Balance = age0_30Balance;
    }

    public Double getAge31_60Balance() {
	return this.age31_60Balance;
    }

    public void setAge31_60Balance(Double age31_60Balance) {
	this.age31_60Balance = age31_60Balance;
    }

    public Double getAge61_90Balance() {
	return this.age61_90Balance;
    }

    public void setAge61_90Balance(Double age61_90Balance) {
	this.age61_90Balance = age61_90Balance;
    }

    public Double getAge91Balance() {
	return this.age91Balance;
    }

    public void setAge91Balance(Double age91Balance) {
	this.age91Balance = age91Balance;
    }

    public Double getAmount() {
	return this.amount;
    }

    public void setAmount(Double amount) {
	this.amount = amount;
    }

    public Double getTransactionAmount() {
	return this.transactionAmount;
    }

    public void setTransactionAmount(Double transactionAmount) {
	this.transactionAmount = transactionAmount;
    }

    public Double getInvoiceAmount() {
	return this.invoiceAmount;
    }

    public void setInvoiceAmount(Double invoiceAmount) {
	this.invoiceAmount = invoiceAmount;
    }

    public Double getPaymentAmount() {
	return this.paymentAmount;
    }

    public void setPaymentAmount(Double paymentAmount) {
	this.paymentAmount = paymentAmount;
    }

    public String getBankName() {
	return this.bankName;
    }

    public void setBankName(String bankName) {
	this.bankName = bankName;
    }

    public String getBankAccountNumber() {
	return this.bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
	this.bankAccountNumber = bankAccountNumber;
    }

    public String getCheckNumber() {
	return this.checkNumber;
    }

    public void setCheckNumber(String checkNumber) {
	this.checkNumber = checkNumber;
    }

    public String getGlAccountNumber() {
	return this.glAccountNumber;
    }

    public void setGlAccountNumber(String glAccountNumber) {
	this.glAccountNumber = glAccountNumber;
    }

    public String getApBatchNumber() {
	return this.apBatchNumber;
    }

    public void setApBatchNumber(String apBatchNumber) {
	this.apBatchNumber = apBatchNumber;
    }

    public String getArBatchNumber() {
	return this.arBatchNumber;
    }

    public void setArBatchNumber(String arBatchNumber) {
	this.arBatchNumber = arBatchNumber;
    }

    public String getTransactionType() {
	return this.transactionType;
    }

    public void setTransactionType(String transactionType) {
	this.transactionType = transactionType;
    }

    public String getStatus() {
	return this.status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    public String getTransactionId() {
	return this.transactionId;
    }

    public void setTransactionId(String transactionId) {
	this.transactionId = transactionId;
    }

    public String getCustomerType() {
	return this.customerType;
    }

    public void setCustomerType(String customerType) {
	this.customerType = customerType;
    }

    public String getCreditHold() {
	return this.creditHold;
    }

    public void setCreditHold(String creditHold) {
	this.creditHold = creditHold;
    }

    public String getBlTerms() {
	return this.blTerms;
    }

    public void setBlTerms(String blTerms) {
	this.blTerms = blTerms;
    }

    public String getBillTo() {
	return this.billTo;
    }

    public void setBillTo(String billTo) {
	this.billTo = billTo;
    }

    public String getCorrectionFlag() {
	return this.correctionFlag;
    }

    public void setCorrectionFlag(String correctionFlag) {
	this.correctionFlag = correctionFlag;
    }

    public String getCorrectionNotice() {
	return this.correctionNotice;
    }

    public void setCorrectionNotice(String correctionNotice) {
	this.correctionNotice = correctionNotice;
    }

    public Integer getCreditTermsValue() {
	return this.creditTermsValue;
    }

    public void setCreditTermsValue(Integer creditTermsValue) {
	this.creditTermsValue = creditTermsValue;
    }

    public Integer getAge() {
	return this.age;
    }

    public void setAge(Integer age) {
	this.age = age;
    }

    public String getEmail() {
	return this.email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getManifestFlag() {
	return this.manifestFlag;
    }

    public void setManifestFlag(String manifestFlag) {
	this.manifestFlag = manifestFlag;
    }

    public String getNoCredit() {
	return this.noCredit;
    }

    public void setNoCredit(String noCredit) {
	this.noCredit = noCredit;
    }

    public String getOverLimit() {
	return this.overLimit;
    }

    public void setOverLimit(String overLimit) {
	this.overLimit = overLimit;
    }

    public String getPastDue() {
	return this.pastDue;
    }

    public void setPastDue(String pastDue) {
	this.pastDue = pastDue;
    }

    public boolean isEmailed() {
	return this.emailed;
    }

    public void setEmailed(boolean emailed) {
	this.emailed = emailed;
    }

    public String getApCostKey() {
	return this.apCostKey;
    }

    public void setApCostKey(String apCostKey) {
	this.apCostKey = apCostKey;
    }

    public String getCollector() {
	return this.collector;
    }

    public void setCollector(String collector) {
	this.collector = collector;
    }

    public String getVoyage() {
	return this.voyage;
    }

    public void setVoyage(String voyage) {
	this.voyage = voyage;
    }

    public String getQuoteNumber() {
	return this.quoteNumber;
    }

    public void setQuoteNumber(String quoteNumber) {
	this.quoteNumber = quoteNumber;
    }

    public Double getTotal() {
	return this.total;
    }

    public void setTotal(Double total) {
	this.total = total;
    }

    public String getFormattedAmount() {
	return this.formattedAmount;
    }

    public void setFormattedAmount(String formattedAmount) {
	this.formattedAmount = formattedAmount;
    }

    public String getFormattedBalance() {
	return this.formattedBalance;
    }

    public void setFormattedBalance(String formattedBalance) {
	this.formattedBalance = formattedBalance;
    }

    public String getFormattedBalanceInProcess() {
	return this.formattedBalanceInProcess;
    }

    public void setFormattedBalanceInProcess(String formattedBalanceInProcess) {
	this.formattedBalanceInProcess = formattedBalanceInProcess;
    }

    public String getFormattedDate() {
	return this.formattedDate;
    }

    public void setFormattedDate(String formattedDate) {
	this.formattedDate = formattedDate;
    }

    public String getFormattedPayment() {
	return this.formattedPayment;
    }

    public void setFormattedPayment(String formattedPayment) {
	this.formattedPayment = formattedPayment;
    }

    public String getBookingNumber() {
	return this.bookingNumber;
    }

    public void setBookingNumber(String bookingNumber) {
	this.bookingNumber = bookingNumber;
    }

    public String getConsignee() {
	return this.consignee;
    }

    public void setConsignee(String consignee) {
	this.consignee = consignee;
    }

    public String getSubledgerCode() {
	return this.subledgerCode;
    }

    public void setSubledgerCode(String subledgerCode) {
	this.subledgerCode = subledgerCode;
    }

    public String getChargeCode() {
	return this.chargeCode;
    }

    public void setChargeCode(String chargeCode) {
	this.chargeCode = chargeCode;
    }

    public String getFormattedPostedDate() {
	return this.formattedPostedDate;
    }

    public void setFormattedPostedDate(String formattedPostedDate) {
	this.formattedPostedDate = formattedPostedDate;
    }

    public String getFormattedReportingDate() {
	return this.formattedReportingDate;
    }

    public void setFormattedReportingDate(String formattedReportingDate) {
	this.formattedReportingDate = formattedReportingDate;
    }

    public String getFormattedCredit() {
	return this.formattedCredit;
    }

    public void setFormattedCredit(String formattedCredit) {
	this.formattedCredit = formattedCredit;
    }

    public String getFormattedDebit() {
	return this.formattedDebit;
    }

    public void setFormattedDebit(String formattedDebit) {
	this.formattedDebit = formattedDebit;
    }

    public String getFormattedDueDate() {
	return this.formattedDueDate;
    }

    public void setFormattedDueDate(String formattedDueDate) {
	this.formattedDueDate = formattedDueDate;
    }

    public boolean isExcluded() {
	return this.excluded;
    }

    public void setExcluded(boolean excluded) {
	this.excluded = excluded;
    }

    public String getTradingPartnerName() {
	return this.tradingPartnerName;
    }

    public void setTradingPartnerName(String tradingPartnerName) {
	this.tradingPartnerName = tradingPartnerName;
    }

    public String getTradingPartnerNumber() {
	return this.tradingPartnerNumber;
    }

    public void setTradingPartnerNumber(String tradingPartnerNumber) {
	this.tradingPartnerNumber = tradingPartnerNumber;
    }

    public String getAdjustAmount() {
	return this.adjustAmount;
    }

    public void setAdjustAmount(String adjustAmount) {
	this.adjustAmount = adjustAmount;
    }

    public String getGlAccount() {
	return this.glAccount;
    }

    public void setGlAccount(String glAccount) {
	this.glAccount = glAccount;
    }

    public String getPaidAmount() {
	return this.paidAmount;
    }

    public void setPaidAmount(String paidAmount) {
	this.paidAmount = paidAmount;
    }

    public boolean isSelected() {
	return this.selected;
    }

    public void setSelected(boolean selected) {
	this.selected = selected;
    }

    public String getPaymentId() {
	return this.paymentId;
    }

    public void setPaymentId(String paymentId) {
	this.paymentId = paymentId;
    }

    public String getSavedInBatches() {
	return this.savedInBatches;
    }

    public void setSavedInBatches(String savedInBatches) {
	this.savedInBatches = savedInBatches;
    }

    public String getCleared() {
	return this.cleared;
    }

    public void setCleared(String cleared) {
	this.cleared = cleared;
    }

    public String getFormattedPaymentDate() {
	return this.formattedPaymentDate;
    }

    public void setFormattedPaymentDate(String formattedPaymentDate) {
	this.formattedPaymentDate = formattedPaymentDate;
    }

    public String getDockReceipt() {
	return this.dockReceipt;
    }

    public void setDockReceipt(String dockReceipt) {
	this.dockReceipt = dockReceipt;
    }

    public Integer getApBatchId() {
	return this.apBatchId;
    }

    public void setApBatchId(Integer apBatchId) {
	this.apBatchId = apBatchId;
    }

    public Integer getArBatchId() {
	return this.arBatchId;
    }

    public void setArBatchId(Integer arBatchId) {
	this.arBatchId = arBatchId;
    }

    public String getInvoiceNotes() {
	return this.invoiceNotes;
    }

    public void setInvoiceNotes(String invoiceNotes) {
	this.invoiceNotes = invoiceNotes;
    }

    public Double getCreditAmount() {
	return this.creditAmount;
    }

    public void setCreditAmount(Double creditAmount) {
	this.creditAmount = creditAmount;
    }

    public Double getDebitAmount() {
	return this.debitAmount;
    }

    public void setDebitAmount(Double debitAmount) {
	this.debitAmount = debitAmount;
    }

    public String getLineItemId() {
	return this.lineItemId;
    }

    public void setLineItemId(String lineItemId) {
	this.lineItemId = lineItemId;
    }

    public String getCostId() {
	return costId;
    }

    public void setCostId(String costId) {
	this.costId = costId;
    }

    public String getModuleName() {
	return moduleName;
    }

    public void setModuleName(String moduleName) {
	this.moduleName = moduleName;
    }

    public String getFileNo() {
	return fileNo;
    }

    public void setFileNo(String fileNo) {
	this.fileNo = fileNo;
    }

    public String getPrepaidOrCollect() {
	return prepaidOrCollect;
    }

    public void setPrepaidOrCollect(String prepaidOrCollect) {
	this.prepaidOrCollect = prepaidOrCollect;
    }

    public String getSslineName() {
	return sslineName;
    }

    public void setSslineName(String sslineName) {
	this.sslineName = sslineName;
    }

    public String getSslineNo() {
	return sslineNo;
    }

    public void setSslineNo(String sslineNo) {
	this.sslineNo = sslineNo;
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

    public String getEta() {
	return eta;
    }

    public void setEta(String eta) {
	this.eta = eta;
    }

    public String getEtd() {
	return etd;
    }

    public void setEtd(String etd) {
	this.etd = etd;
    }

    public String getClearedDate() {
	return clearedDate;
    }

    public void setClearedDate(String clearedDate) {
	this.clearedDate = clearedDate;
    }

    public String getFormattedArrivalDate() {
	return formattedArrivalDate;
    }

    public void setFormattedArrivalDate(String formattedArrivalDate) {
	this.formattedArrivalDate = formattedArrivalDate;
    }

    public boolean isHasDocuments() {
	return hasDocuments;
    }

    public void setHasDocuments(boolean hasDocuments) {
	this.hasDocuments = hasDocuments;
    }

    public String getEnterDate() {
	return enterDate;
    }

    public void setEnterDate(String enterDate) {
	this.enterDate = enterDate;
    }

    public String getInActive() {
	return inActive;
    }

    public void setInActive(String inActive) {
	this.inActive = inActive;
    }

    public String getNotPaid() {
	return notPaid;
    }

    public void setNotPaid(String notPaid) {
	this.notPaid = notPaid;
    }

    public String getBlueScreenAccount() {
	return blueScreenAccount;
    }

    public void setBlueScreenAccount(String blueScreenAccount) {
	this.blueScreenAccount = blueScreenAccount;
    }

    public boolean isManualNotes() {
	return manualNotes;
    }

    public void setManualNotes(boolean manualNotes) {
	this.manualNotes = manualNotes;
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

    public String getBlCreatedBy() {
	return blCreatedBy;
    }

    public void setBlCreatedBy(String blCreatedBy) {
	this.blCreatedBy = blCreatedBy;
    }

    public String getArInvoiceId() {
        return arInvoiceId;
    }

    public void setArInvoiceId(String arInvoiceId) {
        this.arInvoiceId = arInvoiceId;
    }

    public String getEcuDesignation() {
        return ecuDesignation;
    }

    public void setEcuDesignation(String ecuDesignation) {
        this.ecuDesignation = ecuDesignation;
    }

    public BigInteger getHeaderId() {
        return headerId;
    }

    public void setHeaderId(BigInteger headerId) {
        this.headerId = headerId;
    }

    public BigInteger getSsMasterId() {
        return ssMasterId;
    }

    public void setSsMasterId(BigInteger ssMasterId) {
        this.ssMasterId = ssMasterId;
    }

    public String getSsMasterBl() {
        return ssMasterBl;
    }

    public void setSsMasterBl(String ssMasterBl) {
        this.ssMasterBl = ssMasterBl;
    }

    
    
}