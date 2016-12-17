package com.gp.cvst.logisoft.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cvst.logisoft.struts.form.FclBlCorrectionsForm;
import java.io.Serializable;

public class FclBlCorrections implements Comparable<FclBlCorrections>, Serializable {

    private Integer id;
    private Integer fclBlChargeId;
    private GenericCode correctionCode;
    private String accountName;
    private String blNumber;
    private GenericCode correctionType;
    private String pol;
    private String pod;
    private String shipper;
    private String forwarder;
    private String origin;
    private String destination;
    private String rampCity;
    private String chargeCode;
    private String status;
    private Double amount;
    private Double newAmount;
    private Double differeceAmount;
    private String remarks;
    private String accountNumber;
    private String comments;
    private String revrseAccruals;
    private String email;
    private Date date;
    private Date sailDate;
    private String userName;
    private String chargeCodeDescription;
    private String noticeNo;
    private String prepaidCollect;
    private String approval;
    private String isFax;
    private String isPost;
    private String thirdParty;
    private String agent;
    private String voyages;
    private String billToParty;
    private String fileNo;
    private String newBolIdForApprovedBl;
    private String manifest;
    private Date postedDate;
    private String whoPosted;
    private String fax;
    private String originalBillToPartyCorrectionTypeY;
    private String originalCustomerNumberCorrectionTypeY;
    private String originalCustomerNameCorrectionTypeY;
    private Double originalAmountCorrectionTypeY;
    // temp property for credit & debit report
    private String debitOrCreditNote;
    private String toParty;
    private String toPartyNo;
    private String sendCopyTo;
    private String fileType;
    private Integer quoteId;
    private Integer bookingId;
    private Integer bolId;
    //--Memo properties --
    private String creditMemoEmail;
    private String debitMemoEmail;
    private Double currentProfit;
    private Double profitAfterCn;
    //--Memo properties end

    public String getSendCopyTo() {
        return sendCopyTo;
    }

    public void setSendCopyTo(String sendCopyTo) {
        this.sendCopyTo = sendCopyTo;
    }
    private FclBl fclBl;

    public FclBl getFclBl() {
        return fclBl;
    }

    public void setFclBl(FclBl fclBl) {
        this.fclBl = fclBl;
    }

    public Date getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }

    public String getWhoPosted() {
        return whoPosted;
    }

    public void setWhoPosted(String whoPosted) {
        this.whoPosted = whoPosted;
    }
    //temp property for correction type

    public FclBlCorrections() {
    }

    public FclBlCorrections(FclBlCorrectionsForm fclBlCorrectionsForm) throws Exception {
        this.blNumber = fclBlCorrectionsForm.getBlNumber();
        this.accountNumber = fclBlCorrectionsForm.getAccountNumber();
        this.email = fclBlCorrectionsForm.getEmail();
        this.comments = fclBlCorrectionsForm.getComments();
        this.remarks = fclBlCorrectionsForm.getRemarks();
        this.voyages = fclBlCorrectionsForm.getVoyages();
        this.shipper = fclBlCorrectionsForm.getShipper();
        this.forwarder = fclBlCorrectionsForm.getForwarder();
        this.thirdParty = fclBlCorrectionsForm.getThirdParty();
        this.agent = fclBlCorrectionsForm.getAgent();
        this.accountName = fclBlCorrectionsForm.getAccountName();
        this.origin = fclBlCorrectionsForm.getOrigin();
        this.pol = fclBlCorrectionsForm.getPol();
        this.pod = fclBlCorrectionsForm.getPod();
        this.destination = fclBlCorrectionsForm.getDestination();
        this.sendCopyTo = fclBlCorrectionsForm.getSendCopyTo();
        this.rampCity = fclBlCorrectionsForm.getRampCity();
        if (fclBlCorrectionsForm.getFileNo() != null) {
            this.fileNo = fclBlCorrectionsForm.getFileNo();
        }
        if (fclBlCorrectionsForm.getSailDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
            this.sailDate = sdf.parse(fclBlCorrectionsForm.getSailDate());
        }
        this.date = DateUtils.parseToDateForMonthMMMandTime(DateUtils.formatDate(new Date(), "dd-MMM-yyyy HH:mm a"));

        this.prepaidCollect = fclBlCorrectionsForm.getHouseBl();
        //  fclBlCorrections.setApproval(loginName);
        if (fclBlCorrectionsForm.getIsFax() != null && fclBlCorrectionsForm.getIsFax().equalsIgnoreCase("on")) {
            this.isFax = "Y";
        } else {
            this.isFax = "N";
        }
        if (fclBlCorrectionsForm.getIsPost() != null && fclBlCorrectionsForm.getIsPost().equalsIgnoreCase("on")) {
            this.isPost = "Y";
        } else {
            this.isPost = "N";
        }
    }
    private String tempCrType;
    private String tempCrCode;

    public String getVoyages() {
        return voyages;
    }

    public void setVoyages(String voyages) {
        this.voyages = voyages;
    }

    public String getApproval() {
        return approval;
    }

    public void setApproval(String approval) {
        this.approval = approval;
    }

    public String getPrepaidCollect() {
        return prepaidCollect;
    }

    public void setPrepaidCollect(String prepaidCollect) {
        this.prepaidCollect = prepaidCollect;
    }

    public String getNoticeNo() {
        return noticeNo;
    }

    public void setNoticeNo(String noticeNo) {
        this.noticeNo = noticeNo;
    }

    public String getChargeCodeDescription() {
        return chargeCodeDescription;
    }

    public void setChargeCodeDescription(String chargeCodeDescription) {
        this.chargeCodeDescription = chargeCodeDescription;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public GenericCode getCorrectionCode() {
        return correctionCode;
    }

    public void setCorrectionCode(GenericCode correctionCode) {
        this.correctionCode = correctionCode;
    }

    public String getBlNumber() {
        return blNumber;
    }

    public void setBlNumber(String blNumber) {
        this.blNumber = blNumber;
    }

    public GenericCode getCorrectionType() {
        return correctionType;
    }

    public void setCorrectionType(GenericCode correctionType) {
        this.correctionType = correctionType;
    }

    public String getPol() {
        return pol;
    }

    public void setPol(String pol) {
        this.pol = pol;
    }

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

    public String getShipper() {
        return shipper;
    }

    public void setShipper(String shipper) {
        this.shipper = shipper;
    }

    public String getForwarder() {
        return forwarder;
    }

    public void setForwarder(String forwarder) {
        this.forwarder = forwarder;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getRampCity() {
        return rampCity;
    }

    public void setRampCity(String rampCity) {
        this.rampCity = rampCity;
    }

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getRevrseAccruals() {
        return revrseAccruals;
    }

    public void setRevrseAccruals(String revrseAccruals) {
        this.revrseAccruals = revrseAccruals;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getSailDate() {
        return sailDate;
    }

    public void setSailDate(Date sailDate) {
        this.sailDate = sailDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getIsFax() {
        return isFax;
    }

    public void setIsFax(String isFax) {
        this.isFax = isFax;
    }

    public String getIsPost() {
        return isPost;
    }

    public void setIsPost(String isPost) {
        this.isPost = isPost;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getThirdParty() {
        return thirdParty;
    }

    public void setThirdParty(String thirdParty) {
        this.thirdParty = thirdParty;
    }

    public String getTempCrType() {
        return tempCrType;
    }

    public void setTempCrType(String tempCrType) {
        this.tempCrType = tempCrType;
    }

    public String getTempCrCode() {
        return tempCrCode;
    }

    public void setTempCrCode(String tempCrCode) {
        this.tempCrCode = tempCrCode;
    }

    public String getBillToParty() {
        return billToParty;
    }

    public void setBillToParty(String billToParty) {
        this.billToParty = billToParty;
    }

    public Double getDiffereceAmount() {
        return differeceAmount;
    }

    public void setDiffereceAmount(Double differeceAmount) {
        this.differeceAmount = differeceAmount;
    }

    public Double getNewAmount() {
        return newAmount;
    }

    public void setNewAmount(Double newAmount) {
        this.newAmount = newAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    public Integer getFclBlChargeId() {
        return fclBlChargeId;
    }

    public void setFclBlChargeId(Integer fclBlChargeId) {
        this.fclBlChargeId = fclBlChargeId;
    }

    public String getNewBolIdForApprovedBl() {
        return newBolIdForApprovedBl;
    }

    public void setNewBolIdForApprovedBl(String newBolIdForApprovedBl) {
        this.newBolIdForApprovedBl = newBolIdForApprovedBl;
    }

    public String getManifest() {
        return manifest;
    }

    public void setManifest(String manifest) {
        this.manifest = manifest;
    }

    public int compareTo(FclBlCorrections obj) {
        if (this.id > obj.id) {
            return 1;
        } else {
            return 0;
        }
    }

    public String getDebitOrCreditNote() {
        return debitOrCreditNote;
    }

    public void setDebitOrCreditNote(String debitOrCreditNote) {
        this.debitOrCreditNote = debitOrCreditNote;
    }

    public String getToParty() {
        return toParty;
    }

    public void setToParty(String toParty) {
        this.toParty = toParty;
    }

    public String getToPartyNo() {
        return toPartyNo;
    }

    public void setToPartyNo(String toPartyNo) {
        this.toPartyNo = toPartyNo;
    }

    public Double getOriginalAmountCorrectionTypeY() {
        return originalAmountCorrectionTypeY;
    }

    public void setOriginalAmountCorrectionTypeY(Double originalAmountCorrectionTypeY) {
        this.originalAmountCorrectionTypeY = originalAmountCorrectionTypeY;
    }

    public String getOriginalBillToPartyCorrectionTypeY() {
        return originalBillToPartyCorrectionTypeY;
    }

    public void setOriginalBillToPartyCorrectionTypeY(String originalBillToPartyCorrectionTypeY) {
        this.originalBillToPartyCorrectionTypeY = originalBillToPartyCorrectionTypeY;
    }

    public String getOriginalCustomerNameCorrectionTypeY() {
        return originalCustomerNameCorrectionTypeY;
    }

    public void setOriginalCustomerNameCorrectionTypeY(String originalCustomerNameCorrectionTypeY) {
        this.originalCustomerNameCorrectionTypeY = originalCustomerNameCorrectionTypeY;
    }

    public String getOriginalCustomerNumberCorrectionTypeY() {
        return originalCustomerNumberCorrectionTypeY;
    }

    public void setOriginalCustomerNumberCorrectionTypeY(String originalCustomerNumberCorrectionTypeY) {
        this.originalCustomerNumberCorrectionTypeY = originalCustomerNumberCorrectionTypeY;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Integer getBolId() {
        return bolId;
    }

    public void setBolId(Integer bolId) {
        this.bolId = bolId;
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public Integer getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(Integer quoteId) {
        this.quoteId = quoteId;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getCreditMemoEmail() {
        return creditMemoEmail;
    }

    public void setCreditMemoEmail(String creditMemoEmail) {
        this.creditMemoEmail = creditMemoEmail;
    }

    public String getDebitMemoEmail() {
        return debitMemoEmail;
    }

    public void setDebitMemoEmail(String debitMemoEmail) {
        this.debitMemoEmail = debitMemoEmail;
    }

    public Double getCurrentProfit() {
        return null!=currentProfit?currentProfit:currentProfit;
    }

    public void setCurrentProfit(Double currentProfit) {
        this.currentProfit = null!=currentProfit?currentProfit:currentProfit;
    }

    public Double getProfitAfterCn() {
        return null!=profitAfterCn?profitAfterCn:0.00;
    }

    public void setProfitAfterCn(Double profitAfterCn) {
        this.profitAfterCn =null!=profitAfterCn?profitAfterCn:0.00;
    }
}
