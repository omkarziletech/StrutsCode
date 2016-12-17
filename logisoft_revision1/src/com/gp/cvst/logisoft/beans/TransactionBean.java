package com.gp.cvst.logisoft.beans;

import com.gp.cong.common.CommonConstants;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.gp.cong.common.NumberUtils;
import com.gp.cvst.logisoft.AccountingConstants;
import com.gp.cvst.logisoft.domain.ArBatch;
import com.gp.cvst.logisoft.domain.CustAddress;
import com.gp.cvst.logisoft.domain.LineItem;
import com.gp.cvst.logisoft.domain.Transaction;
import com.gp.cvst.logisoft.domain.TransactionLedger;
import com.logiware.hibernate.domain.TransactionLedgerHistory;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.util.LabelValueBean;

public class TransactionBean implements Serializable {

    private static final long serialVersionUID = 3803964133822805172L;
    NumberFormat number = new DecimalFormat("###,###,##0.00");
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    private String confirmOnBoard;
    private String importRelease;
    private String customer;
    private String customerNo;
    private String customerReference;
    private String invoiceOrBl;
    private String amount;
    private String invoiceDate;
    private Long age;
    private String pcollect;
    private String bookingComplete;
    private String ccEmail;
    private String balance;
    private String recordType;
    private String transactionId;
    private String credithold;
    private String expandCheck;
    private String collapseCheck;
    private String checkSplEqpUnitsCollapse;
    private String checkSplEqpUnits;
    private String ratesNonRates;
    private String breakBulk;
    private String readyToPostForCost;
    private String carrierPrint;
    private String emailinvoice;
    private String collapseprint;
    private String chequenumber;
    private String batchId;
    private String alternateAgent;
    private String originalBL;
    private String hold;
    private String releaseTransaction;
    private String ssBldestinationChargesPreCol;
    private String aging;
    private String defaultAgent;
    private String soc;
    private String showAR;
    private String originCheck;
    private String polCheck;
    private String podCheck;
    private String destinationCheck;
    private String blterms;
    private String finalized;
    private String subhouseBl;
    private String chargeCode;
    private String commodityPrint;
    private String readyToPost;
    private String masterbl;
    private String showAPAccurals;
    private String voyagenumber;
    private String vesselnumber;
    private String printDesc;
    private String streamShipBL;
    private String houseBL;
    private String glAcctNo;
    private String prepaidToCollect;
    private String billToCode;
    private String transDate;
    private String duedate;
    private String status;
    private CustAddress custAdd;
    private String showAccurals;
    private String acctType;
    private String showPayables;
    private String orinatingTerminal;
    private String destTerminal;
    private String invoiceAmount;
    private String billTo;
    private String checkAmount;
    private String include;
    private String print;
    private String routedAgentCheck;
    private String ldprint;
    private String idinclude;
    private String idprint;
    private String insureinclude;
    private String outofgate;
    private String customertoprovideSED;
    private String deductFFcomm;
    private String localdryage;
    private String intermodel;
    private String inland;
    private String docCharge;
    private String greenDollarDocCharge;
    private String insurance;
    private String insureprint;
    private String cotainernumber;
    private String sourcecode;
    private String hazmat;
    private String specialequipment;
    private String billofLadding;
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
    private String lineitemNo;
    private String journalentryNo;
    private String shippercheck;
    private String forwardercheck;
    private String consigneecheck;
    private String OPrinting;
    private String NPrinting;
    private String BLPrinting;
    private String noOforiginals;
    private String preCarriage;
    private String placeOfReceipt;
    private String loadingPier;
    private String creditTerms;
    private String correctionFlag;
    private Integer bolId;
    private String importsFreightRelease;
    private String fileType;
    private String rampCheck;
    private String fclInttgra;
    private String ediCheckBox;
    private String auditCheckBox;
    private String masterCheckBox;
    private String blColsed;
    private String AES;
    private String subLedgerCode;
    private String thirdParty;
    private String containerNo;
    private String docReceipt;
    private String voyage;
    private String contact;
    private String batchStatus;
    // check register details
    private String bankNumber;
    private String bankName;
    private String bankAccountNumber;
    private String checkDate;
    private String cleared;
    private String clearedDate;
    private String reconciled;
    private String reconciledDate;
    private String confirmationNumber;
    private String voidTransaction;
    private String voidDate;
    private String reprint;
    private String reprintDate;
    private String NSInvoices;
    private String balanceInProcess;
    private String newClient;
    private String originalBlRequired;
    private String destinationChargesPreCol;
    //---BL PRINT OPTIONS---
    private String agentsForCarrier;
    private String printContainersOnBL;
    private String shipperLoadsAndCounts;
    private String noOfPackages;
    private String conturyOfOrigin;
    private String totalContainers;
    private String printPhrase;
    private String alternatePOL;
    private String manifestPrintReport;
    private String directConsignment;
    //--
    private String chkpay;
    // temparary property
    private String pay;
    private String tempBean;
    private String printRemarks;
    private String printGoodsDescription;
    private String vendorAddress;
    private String phone;
    private String fax;
    private String email;
    // This property's are used to Show Debit & Credit in AR Sub-ledger Screen
    private String debit;
    private String credit;
    private String paymentMethod;
    private Date postedDate;
    private String glPeriod;
    private String shipmentType;
    private String billingTerminal;
    private String age0_30Balance;
    private String age31_60Balance;
    private String age61_90Balance;
    private String age91Balance;
    private String apSpecialist;
    private String user;
    private String description;
    private String paidAmt;
    private String adjAmt;
    private String otherinclude;
    private String otherprint;
    private String documentsReceived;
    // for Transaction and TransactionLedger new Fields
    private Date createdOn;
    private Integer createdBy;
    private Date updatedOn;
    private Integer updatedBy;
    private Integer paidBy;
    private Integer approvedBy;
    private String closedDate;
    // for Subledger batchId
    private Integer apBatchId;
    private Integer arBatchId;
    //for Apinvoice Notes
    private String invoiceNotes;
    private String autoDeductFFCommForBL;
    //for Notes
    private String notes;
    private String manifestFlag;
    private String correctionNotice;
    private Double transactionAmount;
    private Double creditAmount;
    private Double debitAmount;
    private Date transactionDate;
    private String blueScreenChargeCode;
    //For Aging
    private String apBatchNo;
    private String arBatchNo;
    //for BL checkboxes that disables dojo 
    private String masterConsigneeCheck;
    private String masterNotifyCheck;
    private String consigneeCheck;
    private String notifyCheck;
    private String ediConsigneeCheck;
    private String ediShipperCheck;
    private String ediNotifyPartyCheck;
    //for Booking checkboxes that disables dojo
    private String shipperTpCheck;
    private String consigneeTpCheck;
    private String spottAddrTpCheck;
    private String truckerTpCheck;
    //new checkbox field in Booking to copy Quote contactName
    private String contactNameCheck;
    private Date postedToGlDate;
    private String houseShipperCheck;
    private String editHouseShipperCheck;
    private String editHouseConsigneeCheck;
    private String editHouseNotifyCheck;
    private String paymentRelease = "N";
    private String bookingNo;
    private String truckerCheckbox;
    private String clientConsigneeCheck;
    private List<LabelValueBean> paymentMethods;
    private String importantDisclosures;
    private String docsInquiries;
    private String changeIssuingTerminal;
    private String printPortRemarks;
    private String timeCheckBox;
    private String checkStandardChargeCollapse;
    private String checkStandardCharge;
    private String costId;
    private String emailFrom;
    private String emailTo;
    private String disputeDate;
    private String resolvedDate;
    private Integer resolutionPeriod;
    //for Ap Adjusted Accruals reports
    private String file;
    private String originalAmount;
    private String differenceAmount;
    private String formattedReportingDate;
    private String formattedPostedDate;
    private String directConsignmntCheck;
    private String bulletRatesCheck;
    private String greenDollarUseTrueCost;
    private String ediCreatedBy;
    private Date ediCreatedOn;
    private String spotRate;
    private String resendCostToBlue;
    private String pierPass;
    private String brand;
    private String chassisCharge;
    public String getTimeCheckBox() {
        return timeCheckBox;
    }

    public void setTimeCheckBox(String timeCheckBox) {
        this.timeCheckBox = timeCheckBox;
    }

    public String getDocsInquiries() {
        return docsInquiries;
    }

    public void setDocsInquiries(String docsInquiries) {
        this.docsInquiries = docsInquiries;
    }

    public String getImportantDisclosures() {
        return importantDisclosures;
    }

    public void setImportantDisclosures(String importantDisclosures) {
        this.importantDisclosures = importantDisclosures;
    }

    public String getPrintPortRemarks() {
        return printPortRemarks;
    }

    public void setPrintPortRemarks(String printPortRemarks) {
        this.printPortRemarks = printPortRemarks;
    }

    public List<LabelValueBean> getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(List<LabelValueBean> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public String getSpottAddrTpCheck() {
        return spottAddrTpCheck;
    }

    public void setSpottAddrTpCheck(String spottAddrTpCheck) {
        this.spottAddrTpCheck = spottAddrTpCheck;
    }

    public String getPaymentRelease() {
        return paymentRelease;
    }

    public void setPaymentRelease(String paymentRelease) {
        this.paymentRelease = paymentRelease;
    }

    public String getHouseShipperCheck() {
        return houseShipperCheck;
    }

    public void setHouseShipperCheck(String houseShipperCheck) {
        this.houseShipperCheck = houseShipperCheck;
    }

    public String getAutoDeductFFCommForBL() {
        return autoDeductFFCommForBL;
    }

    public void setAutoDeductFFCommForBL(String autoDeductFFCommForBL) {
        this.autoDeductFFCommForBL = autoDeductFFCommForBL;
    }

    public String getDocumentsReceived() {
        return documentsReceived;
    }

    public void setDocumentsReceived(String documentsReceived) {
        this.documentsReceived = documentsReceived;
    }

    public String getBillingTerminal() {
        return billingTerminal;
    }

    public void setBillingTerminal(String billingTerminal) {
        this.billingTerminal = billingTerminal;
    }

    public String getShipmentType() {
        return shipmentType;
    }

    public void setShipmentType(String shipmentType) {
        this.shipmentType = shipmentType;
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

    public String getVendorAddress() {
        return vendorAddress;
    }

    public void setVendorAddress(String vendorAddress) {
        this.vendorAddress = vendorAddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTempBean() {
        return tempBean;
    }

    public void setTempBean(String tempBean) {
        this.tempBean = tempBean;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public String getDocReceipt() {
        return docReceipt;
    }

    public void setDocReceipt(String docReceipt) {
        this.docReceipt = docReceipt;
    }

    public String getVoyage() {
        return voyage;
    }

    public void setVoyage(String voyage) {
        this.voyage = voyage;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAuditCheckBox() {
        return auditCheckBox;
    }

    public void setAuditCheckBox(String auditCheckBox) {
        this.auditCheckBox = auditCheckBox;
    }

    public String getBlColsed() {
        return blColsed;
    }

    public void setBlColsed(String blColsed) {
        this.blColsed = blColsed;
    }

    public String getEdiCheckBox() {
        return ediCheckBox;
    }

    public void setEdiCheckBox(String ediCheckBox) {
        this.ediCheckBox = ediCheckBox;
    }

    public String getMasterCheckBox() {
        return masterCheckBox;
    }

    public void setMasterCheckBox(String masterCheckBox) {
        this.masterCheckBox = masterCheckBox;
    }

    public String getAES() {
        return AES;
    }

    public void setAES(String aes) {
        AES = aes;
    }

    public String getFclInttgra() {
        return fclInttgra;
    }

    public void setFclInttgra(String fclInttgra) {
        this.fclInttgra = fclInttgra;
    }

    public String getImportsFreightRelease() {
        return importsFreightRelease;
    }

    public void setImportsFreightRelease(String importsFreightRelease) {
        this.importsFreightRelease = importsFreightRelease;
    }

    public Integer getBolId() {
        return bolId;
    }

    public void setBolId(Integer bolId) {
        this.bolId = bolId;
    }

    public String getCorrectionFlag() {
        return correctionFlag;
    }

    public void setCorrectionFlag(String correctionFlag) {
        this.correctionFlag = correctionFlag;
    }

    public TransactionBean() {
    }

    public TransactionBean(TransactionLedger transactionLedger) {
        this.tempBean = "temp";
        this.customerNo = transactionLedger.getCustNo();
        this.customer = transactionLedger.getCustName();
        if (transactionLedger.getTransactionDate() != null) {
            this.transDate = sdf.format(transactionLedger.getTransactionDate());
        }
        if (transactionLedger.getTransactionDate() != null) {
            this.invoiceDate = sdf.format(transactionLedger.getTransactionDate());
        }
        if (transactionLedger.getTransactionAmt() != null) {
            this.amount = number.format(transactionLedger.getTransactionAmt());
        } else {
            this.amount = "0.00";
        }
        if (transactionLedger.getBalance() != null) {
            this.balance = number.format(transactionLedger.getBalance());
            this.balanceInProcess = balance;
        }
        this.billofLadding = transactionLedger.getBillLaddingNo();
        this.recordType = transactionLedger.getTransactionType();
        this.blterms = transactionLedger.getBlTerms();
        this.subhouseBl = transactionLedger.getSubHouseBl();
        this.masterbl = transactionLedger.getMasterBl();
        this.voyagenumber = transactionLedger.getVoyageNo();
        this.voyage = transactionLedger.getVoyageNo();
        this.vesselnumber = transactionLedger.getVesselNo();
        this.cotainernumber = transactionLedger.getContainerNo();
        this.containerNo = StringUtils.trim(transactionLedger.getContainerNo());
        this.transactionId = transactionLedger.getTransactionId().toString();
        this.billTo = transactionLedger.getBillTo();
        this.consName = transactionLedger.getConsName();
        this.consNo = transactionLedger.getConsNo();
        this.fwdName = transactionLedger.getFwdName();
        this.fwdNo = transactionLedger.getFwdNo();
        this.agentName = transactionLedger.getAgentName();
        this.agentNo = transactionLedger.getAgentNo();
        this.thirdptyName = transactionLedger.getThirdptyName();
        this.thirdptyNo = transactionLedger.getThirdptyNo();
        this.apBatchId = transactionLedger.getApBatchId();
        this.arBatchId = transactionLedger.getArBatchId();
        this.transactionId = "" + transactionLedger.getTransactionId();
        this.chargeCode = transactionLedger.getChargeCode();
        if (null != transactionLedger.getInvoiceNumber()
                && !transactionLedger.getInvoiceNumber().trim().equals("")) {
            this.invoiceOrBl = transactionLedger.getInvoiceNumber();
        } else {
            this.invoiceOrBl = transactionLedger.getBillLaddingNo();
        }
        this.status = transactionLedger.getStatus();
        this.glAcctNo = transactionLedger.getGlAccountNumber();
        this.subLedgerCode = transactionLedger.getSubledgerSourceCode();
        this.shipmentType = transactionLedger.getShipmentType();
        this.orinatingTerminal = transactionLedger.getOrgTerminal();
        this.billingTerminal = transactionLedger.getBillingTerminal();
        this.postedDate = transactionLedger.getPostedDate();
        this.lineitemNo = transactionLedger.getLineItemNumber();
        this.journalentryNo = transactionLedger.getJournalEntryNumber();
        this.bankAccountNumber = transactionLedger.getBankAccountNumber();
        this.creditTerms = transactionLedger.getBlTerms();
        this.customerReference = transactionLedger.getCustomerReferenceNo();
        this.docReceipt = transactionLedger.getDocReceipt();
        this.description = null != transactionLedger.getDescription() ? transactionLedger.getDescription().replace("\r\n", "<br>").replace("\n", "<br>") : "";
        this.createdOn = transactionLedger.getCreatedOn();
        this.createdBy = transactionLedger.getCreatedBy();
        this.updatedOn = transactionLedger.getUpdatedOn();
        this.updatedBy = transactionLedger.getUpdatedBy();
        this.manifestFlag = transactionLedger.getManifestFlag();
        this.correctionNotice = transactionLedger.getCorrectionNotice();
        this.transactionAmount = transactionLedger.getTransactionAmt();
        this.postedToGlDate = transactionLedger.getPostedToGlDate();
        this.sailingDate = transactionLedger.getSailingDate();
        this.bookingNo = transactionLedger.getBookingNo();
    }

    public TransactionBean(Transaction transaction) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        NumberFormat number = new DecimalFormat("###,###,##0.00");
        this.customerNo = transaction.getCustNo();
        this.customer = transaction.getCustName();
        this.status = transaction.getStatus();
        if (transaction.getTransactionDate() != null) {
            this.transDate = sdf.format(transaction.getTransactionDate());
            this.invoiceDate = sdf.format(transaction.getTransactionDate());
        }
        if (transaction.getDueDate() != null) {
            this.duedate = sdf.format(transaction.getDueDate());
        }
        if (transaction.getTransactionAmt() != null) {
            this.amount = number.format(transaction.getTransactionAmt());
        } else {
            this.amount = "0.00";
        }
        if (transaction.getBalance() != null) {
            this.balance = number.format(transaction.getBalance());
        } else {
            this.balance = "0.00";
        }
        this.glAcctNo = transaction.getGlAccountNumber();
        this.chequenumber = transaction.getChequeNumber();
        this.billofLadding = transaction.getBillLaddingNo();
        this.recordType = transaction.getTransactionType();
        this.blterms = transaction.getBlTerms();
        this.subhouseBl = transaction.getSubHouseBl();
        this.masterbl = transaction.getMasterBl();
        this.voyagenumber = transaction.getVoyageNo();
        this.voyage = transaction.getVoyageNo();
        this.vesselnumber = transaction.getVesselNo();
        this.cotainernumber = transaction.getContainerNo();
        this.transactionId = transaction.getTransactionId().toString();
        this.billTo = transaction.getBillTo();
        this.consName = transaction.getConsName();
        this.consNo = transaction.getConsNo();
        this.fwdName = transaction.getFwdName();
        this.fwdNo = transaction.getFwdNo();
        this.agentName = transaction.getAgentName();
        this.agentNo = transaction.getAgentNo();
        this.thirdptyName = transaction.getThirdptyName();
        this.thirdptyNo = transaction.getThirdptyNo();
        this.customerReference = transaction.getCustomerReferenceNo();
        this.shipperName = transaction.getShipperName();
        this.shipperNo = transaction.getShipperNo();
        this.fwdName = transaction.getFwdName();
        this.fwdNo = transaction.getFwdNo();
        this.consNo = transaction.getConsNo();
        this.consName = transaction.getConsName();
        if (transaction.getInvoiceNumber() != null
                && !transaction.getInvoiceNumber().trim().equals("")) {// If invoice number is null We need to place BL No.
            if (AccountingConstants.PRE_PAYMENT.equals(transaction.getInvoiceNumber())) {
                this.invoiceOrBl = transaction.getBillLaddingNo();
            } else {
                this.invoiceOrBl = transaction.getInvoiceNumber();
            }
        } else {
            this.invoiceOrBl = transaction.getBillLaddingNo();
        }
        if (transaction.getBalanceInProcess() == null) {
            transaction.setBalanceInProcess(0d);
        }
        this.balanceInProcess = number.format(transaction.getBalanceInProcess());
        this.credithold = transaction.getCreditHold();
        this.correctionFlag = transaction.getCorrectionFlag();
        this.apBatchId = transaction.getApBatchId();
        this.arBatchId = transaction.getArBatchId();
        if (transaction.getCreditTerms() != null) {
            this.creditTerms = transaction.getCreditTerms().toString();
        }
        this.bankAccountNumber = transaction.getBankAccountNumber();
        this.bankName = transaction.getBankName();
        this.paymentMethod = transaction.getPaymentMethod();
        this.docReceipt = transaction.getDocReceipt();
        this.createdOn = transaction.getCreatedOn();
        this.createdBy = transaction.getCreatedBy();
        this.updatedOn = transaction.getUpdatedOn();
        this.updatedBy = transaction.getUpdatedBy();
        this.manifestFlag = transaction.getManifestFlag();
        this.correctionNotice = transaction.getCorrectionNotice();
        this.bookingNo = transaction.getBookingNo();
    }

    public String getCreditTerms() {
        return creditTerms;
    }

    public void setCreditTerms(String creditTerms) {
        this.creditTerms = creditTerms;
    }

    public String getOPrinting() {
        return OPrinting;
    }

    public void setOPrinting(String printing) {
        OPrinting = printing;
    }

    public String getNPrinting() {
        return NPrinting;
    }

    public void setNPrinting(String printing) {
        NPrinting = printing;
    }

    public String getBLPrinting() {
        return BLPrinting;
    }

    public void setBLPrinting(String printing) {
        BLPrinting = printing;
    }

    public String getNoOforiginals() {
        return noOforiginals;
    }

    public void setNoOforiginals(String noOforiginals) {
        this.noOforiginals = noOforiginals;
    }

    public String getPreCarriage() {
        return preCarriage;
    }

    public void setPreCarriage(String preCarriage) {
        this.preCarriage = preCarriage;
    }

    public String getPlaceOfReceipt() {
        return placeOfReceipt;
    }

    public void setPlaceOfReceipt(String placeOfReceipt) {
        this.placeOfReceipt = placeOfReceipt;
    }

    public String getLoadingPier() {
        return loadingPier;
    }

    public void setLoadingPier(String loadingPier) {
        this.loadingPier = loadingPier;
    }

    public String getShippercheck() {
        return shippercheck;
    }

    public void setShippercheck(String shippercheck) {
        this.shippercheck = shippercheck;
    }

    public String getForwardercheck() {
        return forwardercheck;
    }

    public void setForwardercheck(String forwardercheck) {
        this.forwardercheck = forwardercheck;
    }

    public String getConsigneecheck() {
        return consigneecheck;
    }

    public void setConsigneecheck(String consigneecheck) {
        this.consigneecheck = consigneecheck;
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

    public String getOtherinclude() {
        return otherinclude;
    }

    public void setOtherinclude(String otherinclude) {
        this.otherinclude = otherinclude;
    }

    public String getOtherprint() {
        return otherprint;
    }

    public void setOtherprint(String otherprint) {
        this.otherprint = otherprint;
    }

    public String getBillofLadding() {
        return billofLadding;
    }

    public void setBillofLadding(String billofLadding) {
        this.billofLadding = billofLadding;
    }

    public String getGlAcctNo() {
        return glAcctNo;
    }

    public void setGlAcctNo(String glAcctNo) {
        this.glAcctNo = glAcctNo;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getSourcecode() {
        return sourcecode;
    }

    public void setSourcecode(String sourcecode) {
        this.sourcecode = sourcecode;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBlterms() {
        return blterms;
    }

    public void setBlterms(String blterms) {
        this.blterms = blterms;
    }

    public String getChequenumber() {
        return chequenumber;
    }

    public void setChequenumber(String chequenumber) {
        this.chequenumber = chequenumber;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getCotainernumber() {
        return cotainernumber;
    }

    public void setCotainernumber(String cotainernumber) {
        this.cotainernumber = cotainernumber;
    }

    public String getCredithold() {
        return credithold;
    }

    public void setCredithold(String credithold) {
        this.credithold = credithold;
    }

    public String getEmailinvoice() {
        return emailinvoice;
    }

    public void setEmailinvoice(String emailinvoice) {
        this.emailinvoice = emailinvoice;
    }

    public String getHold() {
        return hold;
    }

    public void setHold(String hold) {
        this.hold = hold;
    }

    public String getMasterbl() {
        return masterbl;
    }

    public void setMasterbl(String masterbl) {
        this.masterbl = masterbl;
    }

    public String getSubhouseBl() {
        return subhouseBl;
    }

    public void setSubhouseBl(String subhouseBl) {
        this.subhouseBl = subhouseBl;
    }

    public String getVesselnumber() {
        return vesselnumber;
    }

    public void setVesselnumber(String vesselnumber) {
        this.vesselnumber = vesselnumber;
    }

    public String getVoyagenumber() {
        return voyagenumber;
    }

    public void setVoyagenumber(String voyagenumber) {
        this.voyagenumber = voyagenumber;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getCustomerReference() {
        return customerReference;
    }

    public void setCustomerReference(String customerReference) {
        this.customerReference = customerReference;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceOrBl() {

        if (invoiceOrBl == null) {
            return "";
        }

        return invoiceOrBl;
    }

    public void setInvoiceOrBl(String invoiceOrBl) {
        this.invoiceOrBl = invoiceOrBl;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public String getDuedate() {
        return duedate;
    }

    public void setDuedate(String duedate) {
        this.duedate = duedate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CustAddress getCustAdd() {
        return custAdd;
    }

    public void setCustAdd(CustAddress custAdd) {
        this.custAdd = custAdd;
    }

    public String getShowAccurals() {
        return showAccurals;
    }

    public void setShowAccurals(String showAccurals) {
        this.showAccurals = showAccurals;
    }

    public String getAcctType() {
        return acctType;
    }

    public void setAcctType(String acctType) {
        this.acctType = acctType;
    }

    public String getShowPayables() {
        return showPayables;
    }

    public void setShowPayables(String showPayables) {
        this.showPayables = showPayables;
    }

    public String getOrinatingTerminal() {
        return orinatingTerminal;
    }

    public void setOrinatingTerminal(String orinatingTerminal) {
        this.orinatingTerminal = orinatingTerminal;
    }

    public String getDestTerminal() {
        return destTerminal;
    }

    public void setDestTerminal(String destTerminal) {
        this.destTerminal = destTerminal;
    }

    public String getBillTo() {
        return billTo;
    }

    public void setBillTo(String billTo) {
        this.billTo = billTo;
    }

    public String getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(String invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getCheckAmount() {
        return checkAmount;
    }

    public void setCheckAmount(String checkAmount) {
        this.checkAmount = checkAmount;
    }

    public String getInclude() {
        return include;
    }

    public void setInclude(String include) {
        this.include = include;
    }

    public String getPrint() {
        return print == null ? "off" : print;
    }

    public void setPrint(String print) {
        this.print = print;
    }

    public String getRoutedAgentCheck() {
        return routedAgentCheck;
    }

    public void setRoutedAgentCheck(String routedAgentCheck) {
        this.routedAgentCheck = routedAgentCheck;
    }

    public String getLdprint() {
        return ldprint;
    }

    public void setLdprint(String ldprint) {
        this.ldprint = ldprint;
    }

    public String getIdinclude() {
        return idinclude;
    }

    public void setIdinclude(String idinclude) {
        this.idinclude = idinclude;
    }

    public String getIdprint() {
        return idprint;
    }

    public void setIdprint(String idprint) {
        this.idprint = idprint;
    }

    public String getInsureinclude() {
        return insureinclude;
    }

    public void setInsureinclude(String insureinclude) {
        this.insureinclude = insureinclude;
    }

    public String getInsureprint() {
        return insureprint;
    }

    public void setInsureprint(String insureprint) {
        this.insureprint = insureprint;
    }

    public String getHazmat() {
        return hazmat;
    }

    public void setHazmat(String hazmat) {
        this.hazmat = hazmat;
    }

    public String getSpecialequipment() {
        return specialequipment;
    }

    public void setSpecialequipment(String specialequipment) {
        this.specialequipment = specialequipment;
    }

    public String getCustomertoprovideSED() {
        return customertoprovideSED;
    }

    public void setCustomertoprovideSED(String customertoprovideSED) {
        this.customertoprovideSED = customertoprovideSED;
    }

    public String getDeductFFcomm() {
        return deductFFcomm;
    }

    public void setDeductFFcomm(String deductFFcomm) {
        this.deductFFcomm = deductFFcomm;
    }

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public String getIntermodel() {
        return intermodel;
    }

    public void setIntermodel(String intermodel) {
        this.intermodel = intermodel;
    }

    public String getLocaldryage() {
        return localdryage;
    }

    public void setLocaldryage(String localdryage) {
        this.localdryage = localdryage;
    }

    public String getOutofgate() {
        return outofgate;
    }

    public void setOutofgate(String outofgate) {
        this.outofgate = outofgate;
    }

    public String getBillToCode() {
        return billToCode;
    }

    public void setBillToCode(String billToCode) {
        this.billToCode = billToCode;
    }

    public String getBookingComplete() {
        return bookingComplete;
    }

    public void setBookingComplete(String bookingComplete) {
        this.bookingComplete = bookingComplete;
    }

    public String getPrepaidToCollect() {
        return prepaidToCollect;
    }

    public void setPrepaidToCollect(String prepaidToCollect) {
        this.prepaidToCollect = prepaidToCollect;
    }

    public String getPrintDesc() {
        return printDesc;
    }

    public void setPrintDesc(String printDesc) {
        this.printDesc = printDesc;
    }

    public String getAging() {
        return aging;
    }

    public void setAging(String aging) {
        this.aging = aging;
    }

    public String getShowAPAccurals() {
        return showAPAccurals;
    }

    public void setShowAPAccurals(String showAPAccurals) {
        this.showAPAccurals = showAPAccurals;
    }

    public String getShowAR() {
        return showAR;
    }

    public void setShowAR(String showAR) {
        this.showAR = showAR;
    }

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

    public String getJournalentryNo() {
        return journalentryNo;
    }

    public void setJournalentryNo(String journalentryNo) {
        this.journalentryNo = journalentryNo;
    }

    public String getLineitemNo() {
        return lineitemNo;
    }

    public void setLineitemNo(String lineitemNo) {
        this.lineitemNo = lineitemNo;
    }

    public String getCommodityPrint() {
        return commodityPrint;
    }

    public void setCommodityPrint(String commodityPrint) {
        this.commodityPrint = commodityPrint;
    }

    public String getCarrierPrint() {
        return carrierPrint;
    }

    public void setCarrierPrint(String carrierPrint) {
        this.carrierPrint = carrierPrint;
    }

    public String getHouseBL() {
        return houseBL;
    }

    public void setHouseBL(String houseBL) {
        this.houseBL = houseBL;
    }

    public String getStreamShipBL() {
        return streamShipBL;
    }

    public void setStreamShipBL(String streamShipBL) {
        this.streamShipBL = streamShipBL;
    }

    public String getReadyToPost() {
        return readyToPost;
    }

    public void setReadyToPost(String readyToPost) {
        this.readyToPost = readyToPost;
    }

    public String getSoc() {
        return soc;
    }

    public void setSoc(String soc) {
        this.soc = soc;
    }

    public String getFinalized() {
        return finalized;
    }

    public void setFinalized(String finalized) {
        this.finalized = finalized;
    }

    public String getPcollect() {
        return pcollect;
    }

    public void setPcollect(String pcollect) {
        this.pcollect = pcollect;
    }

    public String getOriginCheck() {
        return originCheck;
    }

    public void setOriginCheck(String originCheck) {
        this.originCheck = originCheck;
    }

    public String getPolCheck() {
        return polCheck;
    }

    public void setPolCheck(String polCheck) {
        this.polCheck = polCheck;
    }

    public String getPodCheck() {
        return podCheck;
    }

    public void setPodCheck(String podCheck) {
        this.podCheck = podCheck;
    }

    public String getDestinationCheck() {
        return destinationCheck;
    }

    public void setDestinationCheck(String destinationCheck) {
        this.destinationCheck = destinationCheck;
    }

    public String getDefaultAgent() {
        return defaultAgent;
    }

    public void setDefaultAgent(String defaultAgent) {
        this.defaultAgent = defaultAgent;
    }

    public String getReadyToPostForCost() {
        return readyToPostForCost;
    }

    public void setReadyToPostForCost(String readyToPostForCost) {
        this.readyToPostForCost = readyToPostForCost;
    }

    public String getReleaseTransaction() {
        return releaseTransaction;
    }

    public void setReleaseTransaction(String releaseTransaction) {
        this.releaseTransaction = releaseTransaction;
    }

    public String getSubLedgerCode() {
        return subLedgerCode;
    }

    public void setSubLedgerCode(String subLedgerCode) {
        this.subLedgerCode = subLedgerCode;
    }

    public String getThirdParty() {
        return thirdParty;
    }

    public void setThirdParty(String thirdParty) {
        this.thirdParty = thirdParty;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getRampCheck() {
        return rampCheck;
    }

    public void setRampCheck(String rampCheck) {
        this.rampCheck = rampCheck;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
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

    public String getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(String checkDate) {
        this.checkDate = checkDate;
    }

    public String getCleared() {
        return cleared;
    }

    public void setCleared(String cleared) {
        this.cleared = cleared;
    }

    public String getClearedDate() {
        return clearedDate;
    }

    public void setClearedDate(String clearedDate) {
        this.clearedDate = clearedDate;
    }

    public String getReconciled() {
        return reconciled;
    }

    public void setReconciled(String reconciled) {
        this.reconciled = reconciled;
    }

    public String getReconciledDate() {
        return reconciledDate;
    }

    public void setReconciledDate(String reconciledDate) {
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

    public String getVoidDate() {
        return voidDate;
    }

    public void setVoidDate(String voidDate) {
        this.voidDate = voidDate;
    }

    public String getReprint() {
        return reprint;
    }

    public void setReprint(String reprint) {
        this.reprint = reprint;
    }

    public String getReprintDate() {
        return reprintDate;
    }

    public void setReprintDate(String reprintDate) {
        this.reprintDate = reprintDate;
    }

    public String getBatchStatus() {
        return batchStatus;
    }

    public void setBatchStatus(String batchStatus) {
        this.batchStatus = batchStatus;
    }

    public String getNSInvoices() {
        return NSInvoices;
    }

    public void setNSInvoices(String invoices) {
        NSInvoices = invoices;
    }

    public String getBalanceInProcess() {
        return balanceInProcess;
    }

    public void setBalanceInProcess(String balanceInProcess) {
        this.balanceInProcess = balanceInProcess;
    }

    public String getNewClient() {
        return newClient;
    }

    public void setNewClient(String newClient) {
        this.newClient = newClient;
    }

    public String getOriginalBlRequired() {
        return originalBlRequired;
    }

    public void setOriginalBlRequired(String originalBlRequired) {
        this.originalBlRequired = originalBlRequired;
    }

    public String getDestinationChargesPreCol() {
        return destinationChargesPreCol;
    }

    public void setDestinationChargesPreCol(String destinationChargesPreCol) {
        this.destinationChargesPreCol = destinationChargesPreCol;
    }

    public String getChkpay() {
        return chkpay;
    }

    public void setChkpay(String chkpay) {
        this.chkpay = chkpay;
    }

    public String getPrintContainersOnBL() {
        return printContainersOnBL;
    }

    public void setPrintContainersOnBL(String printContainersOnBL) {
        this.printContainersOnBL = printContainersOnBL;
    }

    public String getShipperLoadsAndCounts() {
        return shipperLoadsAndCounts;
    }

    public void setShipperLoadsAndCounts(String shipperLoadsAndCounts) {
        this.shipperLoadsAndCounts = shipperLoadsAndCounts;
    }

    public String getPrintPhrase() {
        return printPhrase;
    }

    public void setPrintPhrase(String printPhrase) {
        this.printPhrase = printPhrase;
    }

    public String getAgentsForCarrier() {
        return agentsForCarrier;
    }

    public void setAgentsForCarrier(String agentsForCarrier) {
        this.agentsForCarrier = agentsForCarrier;
    }

    public String getAlternatePOL() {
        return alternatePOL;
    }

    public void setAlternatePOL(String alternatePOL) {
        this.alternatePOL = alternatePOL;
    }

    public String getManifestPrintReport() {
        return manifestPrintReport;
    }

    public void setManifestPrintReport(String manifestPrintReport) {
        this.manifestPrintReport = manifestPrintReport;
    }

    public String getAlternateAgent() {
        return alternateAgent;
    }

    public void setAlternateAgent(String alternateAgent) {
        this.alternateAgent = alternateAgent;
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

    public String getPrintRemarks() {
        return printRemarks;
    }

    public void setPrintRemarks(String printRemarks) {
        this.printRemarks = printRemarks;
    }

    public String getPrintGoodsDescription() {
        return printGoodsDescription;
    }

    public void setPrintGoodsDescription(String printGoodsDescription) {
        this.printGoodsDescription = printGoodsDescription;
    }

    public String getOriginalBL() {
        return originalBL;
    }

    public void setOriginalBL(String originalBL) {
        this.originalBL = originalBL;
    }

    public String getSsBldestinationChargesPreCol() {
        return ssBldestinationChargesPreCol;
    }

    public void setSsBldestinationChargesPreCol(
            String ssBldestinationChargesPreCol) {
        this.ssBldestinationChargesPreCol = ssBldestinationChargesPreCol;
    }

    public String getCollapseprint() {
        return collapseprint;
    }

    public void setCollapseprint(String collapseprint) {
        this.collapseprint = collapseprint;
    }

    public String getExpandCheck() {
        return expandCheck;
    }

    public void setExpandCheck(String expandCheck) {
        this.expandCheck = expandCheck;
    }

    public String getCollapseCheck() {
        return collapseCheck;
    }

    public void setCollapseCheck(String collapseCheck) {
        this.collapseCheck = collapseCheck;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCcEmail() {
        return ccEmail;
    }

    public void setCcEmail(String ccEmail) {
        this.ccEmail = ccEmail;
    }

    public String getRatesNonRates() {
        return ratesNonRates;
    }

    public void setRatesNonRates(String ratesNonRates) {
        this.ratesNonRates = ratesNonRates;
    }

    public String getBreakBulk() {
        return breakBulk;
    }

    public void setBreakBulk(String breakBulk) {
        this.breakBulk = breakBulk;
    }

    public Date getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }

    public String getGlPeriod() {
        return glPeriod;
    }

    public void setGlPeriod(String glPeriod) {
        this.glPeriod = glPeriod;
    }

    /**
     * @param arBatch
     */
    public TransactionBean(ArBatch arBatch) throws Exception {
        this.paymentMethod = AccountingConstants.DEPOSIT;
        this.batchId = arBatch.getBatchId() == null ? "" : arBatch.getBatchId().toString();
        if (null != arBatch.getTotalAmount() && arBatch.getTotalAmount() > 0) {
            this.debit = NumberUtils.formatNumber(arBatch.getTotalAmount(), "###,###,##0.00");
        } else if (null != arBatch.getTotalAmount()) {
            this.credit = NumberUtils.formatNumber(Math.abs(arBatch.getTotalAmount()), "###,###,##0.00");
        }
        this.transDate = arBatch.getDepositDate() == null ? "" : sdf.format(arBatch.getDepositDate());
        this.bankAccountNumber = arBatch.getBankAccount() == null ? "" : arBatch.getBankAccount();
        this.status = arBatch.getStatus();
    }

    /**
     * @param lineItem
     */
    public TransactionBean(LineItem lineItem) {
        this.amount = lineItem.getDebit() == null ? "" : number.format(lineItem.getDebit());
        this.debit = lineItem.getDebit() == null ? "" : number.format(lineItem.getDebit());
        this.credit = lineItem.getCredit() == null ? "" : number.format(lineItem.getCredit());
        this.transDate = lineItem.getDate() == null ? "" : sdf.format(lineItem.getDate());
        this.bankAccountNumber = "";
        this.paymentMethod = "";
        this.batchId = "";
    }

    public String getAge0_30Balance() {
        return age0_30Balance;
    }

    public void setAge0_30Balance(String age0_30Balance) {
        this.age0_30Balance = age0_30Balance;
    }

    public String getAge31_60Balance() {
        return age31_60Balance;
    }

    public void setAge31_60Balance(String age31_60Balance) {
        this.age31_60Balance = age31_60Balance;
    }

    public String getAge61_90Balance() {
        return age61_90Balance;
    }

    public void setAge61_90Balance(String age61_90Balance) {
        this.age61_90Balance = age61_90Balance;
    }

    public String getAge91Balance() {
        return age91Balance;
    }

    public void setAge91Balance(String age91Balance) {
        this.age91Balance = age91Balance;
    }

    public String getApSpecialist() {
        return apSpecialist;
    }

    public void setApSpecialist(String apSpecialist) {
        this.apSpecialist = apSpecialist;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the paidAmt
     */
    public String getPaidAmt() {
        return paidAmt;
    }

    /**
     * @param paidAmt the paidAmt to set
     */
    public void setPaidAmt(String paidAmt) {
        this.paidAmt = paidAmt;
    }

    /**
     * @return the adjAmt
     */
    public String getAdjAmt() {
        return adjAmt;
    }

    /**
     * @param adjAmt the adjAmt to set
     */
    public void setAdjAmt(String adjAmt) {
        this.adjAmt = adjAmt;
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

    public String getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(String closedDate) {
        this.closedDate = closedDate;
    }

    public String getInvoiceNotes() {
        return invoiceNotes;
    }

    public void setInvoiceNotes(String invoiceNotes) {
        this.invoiceNotes = invoiceNotes;
    }

    public String getConfirmOnBoard() {
        return confirmOnBoard;
    }

    public void setConfirmOnBoard(String confirmOnBoard) {
        this.confirmOnBoard = confirmOnBoard;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getNoOfPackages() {
        return noOfPackages;
    }

    public void setNoOfPackages(String noOfPackages) {
        this.noOfPackages = noOfPackages;
    }

    public String getTotalContainers() {
        return totalContainers;
    }

    public void setTotalContainers(String totalContainers) {
        this.totalContainers = totalContainers;
    }

    public String getConturyOfOrigin() {
        return conturyOfOrigin;
    }

    public void setConturyOfOrigin(String conturyOfOrigin) {
        this.conturyOfOrigin = conturyOfOrigin;
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

    public Double getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(Double creditAmount) {
        this.creditAmount = creditAmount;
    }

    public Double getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(Double debitAmount) {
        this.debitAmount = debitAmount;
    }

    public Double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(Double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getBlueScreenChargeCode() {
        return blueScreenChargeCode;
    }

    public void setBlueScreenChargeCode(String blueScreenChargeCode) {
        this.blueScreenChargeCode = blueScreenChargeCode;
    }

    public String getImportRelease() {
        return importRelease;
    }

    public void setImportRelease(String importRelease) {
        this.importRelease = importRelease;
    }

    public String getConsigneeCheck() {
        return consigneeCheck;
    }

    public void setConsigneeCheck(String consigneeCheck) {
        this.consigneeCheck = consigneeCheck;
    }

    public String getMasterConsigneeCheck() {
        return masterConsigneeCheck;
    }

    public void setMasterConsigneeCheck(String masterConsigneeCheck) {
        this.masterConsigneeCheck = masterConsigneeCheck;
    }

    public String getMasterNotifyCheck() {
        return masterNotifyCheck;
    }

    public void setMasterNotifyCheck(String masterNotifyCheck) {
        this.masterNotifyCheck = masterNotifyCheck;
    }

    public String getNotifyCheck() {
        return notifyCheck;
    }

    public void setNotifyCheck(String notifyCheck) {
        this.notifyCheck = notifyCheck;
    }

    public String getConsigneeTpCheck() {
        return consigneeTpCheck;
    }

    public void setConsigneeTpCheck(String consigneeTpCheck) {
        this.consigneeTpCheck = consigneeTpCheck;
    }

    public String getShipperTpCheck() {
        return shipperTpCheck;
    }

    public void setShipperTpCheck(String shipperTpCheck) {
        this.shipperTpCheck = shipperTpCheck;
    }

    public String getApBatchNo() {
        return apBatchNo;
    }

    public void setApBatchNo(String apBatchNo) {
        this.apBatchNo = apBatchNo;
    }

    public String getArBatchNo() {
        return arBatchNo;
    }

    public void setArBatchNo(String arBatchNo) {
        this.arBatchNo = arBatchNo;
    }

    public String getContactNameCheck() {
        return contactNameCheck;
    }

    public String getTruckerCheckbox() {
        return truckerCheckbox;
    }

    public String getTruckerTpCheck() {
        return truckerTpCheck;
    }

    public void setTruckerTpCheck(String truckerTpCheck) {
        this.truckerTpCheck = truckerTpCheck;
    }

    public void setTruckerCheckbox(String truckerCheckbox) {
        this.truckerCheckbox = truckerCheckbox;
    }

    public void setContactNameCheck(String contactNameCheck) {
        this.contactNameCheck = contactNameCheck;
    }

    public Date getPostedToGlDate() {
        return postedToGlDate;
    }

    public void setPostedToGlDate(Date postedToGlDate) {
        this.postedToGlDate = postedToGlDate;
    }

    public String getBookingNo() {
        return bookingNo;
    }

    public void setBookingNo(String bookingNo) {
        this.bookingNo = bookingNo;
    }

    public String getEdiConsigneeCheck() {
        return ediConsigneeCheck;
    }

    public void setEdiConsigneeCheck(String ediConsigneeCheck) {
        this.ediConsigneeCheck = ediConsigneeCheck;
    }

    public String getEdiNotifyPartyCheck() {
        return ediNotifyPartyCheck;
    }

    public void setEdiNotifyPartyCheck(String ediNotifyPartyCheck) {
        this.ediNotifyPartyCheck = ediNotifyPartyCheck;
    }

    public String getEdiShipperCheck() {
        return ediShipperCheck;
    }

    public void setEdiShipperCheck(String ediShipperCheck) {
        this.ediShipperCheck = ediShipperCheck;
    }

    public String getInland() {
        return inland;
    }

    public void setInland(String inland) {
        this.inland = inland;
    }

    public String getDocCharge() {
        return docCharge;
    }

    public void setDocCharge(String docCharge) {
        this.docCharge = docCharge;
    }

    public String getClientConsigneeCheck() {
        return clientConsigneeCheck;
    }

    public void setClientConsigneeCheck(String clientConsigneeCheck) {
        this.clientConsigneeCheck = clientConsigneeCheck;
    }

    public String getEditHouseShipperCheck() {
        return editHouseShipperCheck;
    }

    public void setEditHouseShipperCheck(String editHouseShipperCheck) {
        this.editHouseShipperCheck = editHouseShipperCheck;
    }

    public String getCheckSplEqpUnits() {
        return checkSplEqpUnits;
    }

    public void setCheckSplEqpUnits(String checkSplEqpUnits) {
        this.checkSplEqpUnits = checkSplEqpUnits;
    }

    public String getCheckSplEqpUnitsCollapse() {
        return checkSplEqpUnitsCollapse;
    }

    public void setCheckSplEqpUnitsCollapse(String checkSplEqpUnitsCollapse) {
        this.checkSplEqpUnitsCollapse = checkSplEqpUnitsCollapse;
    }

    public TransactionBean(TransactionLedgerHistory transactionLedgerHistory) {
        this.customerNo = transactionLedgerHistory.getCustNo();
        this.customer = transactionLedgerHistory.getCustName();
        if (transactionLedgerHistory.getTransactionDate() != null) {
            this.transDate = sdf.format(transactionLedgerHistory.getTransactionDate());
        }
        if (transactionLedgerHistory.getTransactionDate() != null) {
            this.invoiceDate = sdf.format(transactionLedgerHistory.getTransactionDate());
        }
        if (transactionLedgerHistory.getAmount() != null) {
            this.amount = number.format(transactionLedgerHistory.getAmount());
        } else {
            this.amount = "0.00";
        }
        this.balance = amount;
        this.balanceInProcess = amount;
        this.billofLadding = transactionLedgerHistory.getBillOfLadding();
        this.recordType = transactionLedgerHistory.getTransactionType();
        this.voyagenumber = transactionLedgerHistory.getVoyage();
        this.voyage = transactionLedgerHistory.getVoyage();
        this.transactionId = transactionLedgerHistory.getId().toString();
        this.chargeCode = transactionLedgerHistory.getChargeCode();
        if (null != transactionLedgerHistory.getInvoice()
                && !transactionLedgerHistory.getInvoice().trim().equals("")) {
            this.invoiceOrBl = transactionLedgerHistory.getInvoice();
        } else {
            this.invoiceOrBl = transactionLedgerHistory.getBillOfLadding();
        }
        this.status = CommonConstants.STATUS_POSTED_TO_GL;
        this.glAcctNo = transactionLedgerHistory.getGlAccount();
        this.subLedgerCode = transactionLedgerHistory.getSubledger();
        this.postedDate = transactionLedgerHistory.getPostedDate();
        this.lineitemNo = transactionLedgerHistory.getLineItemId();
        this.description = transactionLedgerHistory.getDescription();
        this.createdOn = transactionLedgerHistory.getCreatedDate();
        this.transactionAmount = transactionLedgerHistory.getAmount();
        this.postedToGlDate = transactionLedgerHistory.getPostedToGlDate();
        this.sailingDate = transactionLedgerHistory.getReportingDate();
    }

    public String getCheckStandardCharge() {
        return checkStandardCharge;
    }

    public void setCheckStandardCharge(String checkStandardCharge) {
        this.checkStandardCharge = checkStandardCharge;
    }

    public String getCheckStandardChargeCollapse() {
        return checkStandardChargeCollapse;
    }

    public void setCheckStandardChargeCollapse(String checkStandardChargeCollapse) {
        this.checkStandardChargeCollapse = checkStandardChargeCollapse;
    }

    public String getDirectConsignment() {
        return directConsignment;
    }

    public void setDirectConsignment(String directConsignment) {
        this.directConsignment = directConsignment;
    }

    public String getCostId() {
        return costId;
    }

    public void setCostId(String costId) {
        this.costId = costId;
    }

    public String getEmailFrom() {
        return emailFrom;
    }

    public void setEmailFrom(String emailFrom) {
        this.emailFrom = emailFrom;
    }

    public String getEmailTo() {
        return emailTo;
    }

    public void setEmailTo(String emailTo) {
        this.emailTo = emailTo;
    }

    public String getDisputeDate() {
        return disputeDate;
    }

    public void setDisputeDate(String disputeDate) {
        this.disputeDate = disputeDate;
    }

    public String getResolvedDate() {
        return resolvedDate;
    }

    public void setResolvedDate(String resolvedDate) {
        this.resolvedDate = resolvedDate;
    }

    public Integer getResolutionPeriod() {
        return resolutionPeriod;
    }

    public void setResolutionPeriod(Integer resolutionPeriod) {
        this.resolutionPeriod = resolutionPeriod;
    }

    public String getEditHouseConsigneeCheck() {
        return editHouseConsigneeCheck;
    }

    public void setEditHouseConsigneeCheck(String editHouseConsigneeCheck) {
        this.editHouseConsigneeCheck = editHouseConsigneeCheck;
    }

    public String getEditHouseNotifyCheck() {
        return editHouseNotifyCheck;
    }

    public void setEditHouseNotifyCheck(String editHouseNotifyCheck) {
        this.editHouseNotifyCheck = editHouseNotifyCheck;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(String originalAmount) {
        this.originalAmount = originalAmount;
    }

    public String getDifferenceAmount() {
        return differenceAmount;
    }

    public void setDifferenceAmount(String differenceAmount) {
        this.differenceAmount = differenceAmount;
    }

    public String getFormattedPostedDate() {
        return formattedPostedDate;
    }

    public void setFormattedPostedDate(String formattedPostedDate) {
        this.formattedPostedDate = formattedPostedDate;
    }

    public String getFormattedReportingDate() {
        return formattedReportingDate;
    }

    public void setFormattedReportingDate(String formattedReportingDate) {
        this.formattedReportingDate = formattedReportingDate;
    }

    public String getDirectConsignmntCheck() {
        return directConsignmntCheck;
    }

    public void setDirectConsignmntCheck(String directConsignmntCheck) {
        this.directConsignmntCheck = directConsignmntCheck;
    }

    public String getChangeIssuingTerminal() {
        return changeIssuingTerminal;
    }

    public void setChangeIssuingTerminal(String changeIssuingTerminal) {
        this.changeIssuingTerminal = changeIssuingTerminal;
    }

    public String getGreenDollarDocCharge() {
        return greenDollarDocCharge;
    }

    public void setGreenDollarDocCharge(String greenDollarDocCharge) {
        this.greenDollarDocCharge = greenDollarDocCharge;
    }

    public String getBulletRatesCheck() {
        return bulletRatesCheck;
    }

    public void setBulletRatesCheck(String bulletRatesCheck) {
        this.bulletRatesCheck = bulletRatesCheck;
    }

    public String getGreenDollarUseTrueCost() {
        return greenDollarUseTrueCost;
    }

    public void setGreenDollarUseTrueCost(String greenDollarUseTrueCost) {
        this.greenDollarUseTrueCost = greenDollarUseTrueCost;
    }

    public String getEdiCreatedBy() {
        return ediCreatedBy;
    }

    public void setEdiCreatedBy(String ediCreatedBy) {
        this.ediCreatedBy = ediCreatedBy;
    }

    public Date getEdiCreatedOn() {
        return ediCreatedOn;
    }

    public void setEdiCreatedOn(Date ediCreatedOn) {
        this.ediCreatedOn = ediCreatedOn;
    }

    public String getSpotRate() {
        return null != spotRate ? spotRate : "N";
    }

    public void setSpotRate(String spotRate) {
        this.spotRate = null != spotRate ? spotRate : "N";;
    }

    public String getResendCostToBlue() {
        return resendCostToBlue;
    }

    public void setResendCostToBlue(String resendCostToBlue) {
        this.resendCostToBlue = resendCostToBlue;
    }

    public String getPierPass() {
        return pierPass;
    }

    public void setPierPass(String pierPass) {
        this.pierPass = pierPass;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getChassisCharge() {
        return chassisCharge;
    }

    public void setChassisCharge(String chassisCharge) {
        this.chassisCharge = chassisCharge;
    }
    
}
