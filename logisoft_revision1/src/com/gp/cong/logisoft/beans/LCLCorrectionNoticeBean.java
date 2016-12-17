package com.gp.cong.logisoft.beans;

import java.io.Serializable;
import java.util.Date;

public class LCLCorrectionNoticeBean implements Serializable {

    private String fileNo;
    private String blNo;
    private String correctionNo;
    private String unitNo;
    private String eciShipmentFileNo;
    private String origin;
    private String destination;
    private Date sailDate;
    private String voyageNumber;
    private String shipperNo;
    private String shipperName;
    private String forwarderNo;
    private String forwarderName;
    private String thirdPartyNo;
    private String thirdPartyName;
    private String agentNo;
    private String agentName;
    private String notifyNo;
    private String notifyName;
    private String consigneeNo;
    private String consigneeName;
    private String billToParty;
    private String billingType;
    private String billingTypeLabel;
    private String customer;
    private String customerAcctNo;
    private String customerLabel;
    private String comments;
    private Integer correctionType;
    private Integer correctionCode;
    private String correctionTypeValue;
    private String correctionCodeValue;
    private String noticeNo;
    private String vesselName;
    private Date correctionDate;
    private String debitOrCreditNote;
    private String strCorrectionType;
    private String billToPartyAddress;
    private Integer voidStatus;
    private String correctionStatus;
    private String enteredBy;
    private String noticeDate;
    private String postedDate;
    private String attn;
    private String currentProfit;
    private String profitAfterCN;
    private String billToCode;
    private String custContact;
    private Long correctedId;

    public String getUnitNo() {
        return unitNo;
    }

    public void setUnitNo(String unitNo) {
        this.unitNo = unitNo;
    }

    public String getEciShipmentFileNo() {
        return eciShipmentFileNo;
    }

    public void setEciShipmentFileNo(String eciShipmentFileNo) {
        this.eciShipmentFileNo = eciShipmentFileNo;
    }

    public String getCorrectionNo() {
        return correctionNo;
    }

    public void setCorrectionNo(String correctionNo) {
        this.correctionNo = correctionNo;
    }

    public String getCorrectionCodeValue() {
        return correctionCodeValue;
    }

    public void setCorrectionCodeValue(String correctionCodeValue) {
        this.correctionCodeValue = correctionCodeValue;
    }

    public String getCorrectionTypeValue() {
        return correctionTypeValue;
    }

    public void setCorrectionTypeValue(String correctionTypeValue) {
        this.correctionTypeValue = correctionTypeValue;
    }

    public String getCorrectionStatus() {
        return correctionStatus;
    }

    public void setCorrectionStatus(String correctionStatus) {
        this.correctionStatus = correctionStatus;
    }

    public Integer getVoidStatus() {
        return voidStatus;
    }

    public void setVoidStatus(Integer voidStatus) {
        this.voidStatus = voidStatus;
    }

    public String getBillToPartyAddress() {
        return billToPartyAddress;
    }

    public void setBillToPartyAddress(String billToPartyAddress) {
        this.billToPartyAddress = billToPartyAddress;
    }

    public String getStrCorrectionType() {
        return strCorrectionType;
    }

    public void setStrCorrectionType(String strCorrectionType) {
        this.strCorrectionType = strCorrectionType;
    }

    public String getDebitOrCreditNote() {
        return debitOrCreditNote;
    }

    public void setDebitOrCreditNote(String debitOrCreditNote) {
        this.debitOrCreditNote = debitOrCreditNote;
    }

    public Date getCorrectionDate() {
        return correctionDate;
    }

    public void setCorrectionDate(Date correctionDate) {
        this.correctionDate = correctionDate;
    }

    public String getVesselName() {
        return vesselName;
    }

    public void setVesselName(String vesselName) {
        this.vesselName = vesselName;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Date getSailDate() {
        return sailDate;
    }

    public void setSailDate(Date sailDate) {
        this.sailDate = sailDate;
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

    public String getForwarderName() {
        return forwarderName;
    }

    public void setForwarderName(String forwarderName) {
        this.forwarderName = forwarderName;
    }

    public String getForwarderNo() {
        return forwarderNo;
    }

    public void setForwarderNo(String forwarderNo) {
        this.forwarderNo = forwarderNo;
    }

    public String getShipperName() {
        return shipperName;
    }

    public void setShipperName(String shipperName) {
        this.shipperName = shipperName;
    }

    public String getShipperNo() {
        return shipperNo;
    }

    public void setShipperNo(String shipperNo) {
        this.shipperNo = shipperNo;
    }

    public String getThirdPartyName() {
        return thirdPartyName;
    }

    public void setThirdPartyName(String thirdPartyName) {
        this.thirdPartyName = thirdPartyName;
    }

    public String getThirdPartyNo() {
        return thirdPartyNo;
    }

    public void setThirdPartyNo(String thirdPartyNo) {
        this.thirdPartyNo = thirdPartyNo;
    }

    public String getVoyageNumber() {
        return voyageNumber;
    }

    public void setVoyageNumber(String voyageNumber) {
        this.voyageNumber = voyageNumber;
    }

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    public String getBlNo() {
        return blNo;
    }

    public void setBlNo(String blNo) {
        this.blNo = blNo;
    }

    public String getBillToParty() {
        return billToParty;
    }

    public void setBillToParty(String billToParty) {
        this.billToParty = billToParty;
    }

    public String getBillingType() {
        return billingType;
    }

    public void setBillingType(String billingType) {
        this.billingType = billingType;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getCustomerLabel() {
        return customerLabel;
    }

    public void setCustomerLabel(String customerLabel) {
        this.customerLabel = customerLabel;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Integer getCorrectionCode() {
        return correctionCode;
    }

    public void setCorrectionCode(Integer correctionCode) {
        this.correctionCode = correctionCode;
    }

    public Integer getCorrectionType() {
        return correctionType;
    }

    public void setCorrectionType(Integer correctionType) {
        this.correctionType = correctionType;
    }

    public String getBillingTypeLabel() {
        return billingTypeLabel;
    }

    public void setBillingTypeLabel(String billingTypeLabel) {
        this.billingTypeLabel = billingTypeLabel;
    }

    public String getCustomerAcctNo() {
        return customerAcctNo;
    }

    public void setCustomerAcctNo(String customerAcctNo) {
        this.customerAcctNo = customerAcctNo;
    }

    public String getNoticeNo() {
        return noticeNo;
    }

    public void setNoticeNo(String noticeNo) {
        this.noticeNo = noticeNo;
    }

    public String getNotifyName() {
        return notifyName;
    }

    public void setNotifyName(String notifyName) {
        this.notifyName = notifyName;
    }

    public String getNotifyNo() {
        return notifyNo;
    }

    public void setNotifyNo(String notifyNo) {
        this.notifyNo = notifyNo;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getConsigneeNo() {
        return consigneeNo;
    }

    public void setConsigneeNo(String consigneeNo) {
        this.consigneeNo = consigneeNo;
    }

    public String getEnteredBy() {
        return enteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        this.enteredBy = enteredBy;
    }

    public String getNoticeDate() {
        return noticeDate;
    }

    public void setNoticeDate(String noticeDate) {
        this.noticeDate = noticeDate;
    }

    public String getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(String postedDate) {
        this.postedDate = postedDate;
    }

    public String getAttn() {
        return attn;
    }

    public void setAttn(String attn) {
        this.attn = attn;
    }

    public String getCurrentProfit() {
        return currentProfit;
    }

    public void setCurrentProfit(String currentProfit) {
        this.currentProfit = currentProfit;
    }

    public String getProfitAfterCN() {
        return profitAfterCN;
    }

    public void setProfitAfterCN(String profitAfterCN) {
        this.profitAfterCN = profitAfterCN;
    }

    public String getBillToCode() {
        return billToCode;
    }

    public void setBillToCode(String billToCode) {
        this.billToCode = billToCode;
    }

    public String getCustContact() {
        return custContact;
    }

    public void setCustContact(String custContact) {
        this.custContact = custContact;
    }

    public Long getCorrectedId() {
        return correctedId;
    }

    public void setCorrectedId(Long correctedId) {
        this.correctedId = correctedId;
    }
}
