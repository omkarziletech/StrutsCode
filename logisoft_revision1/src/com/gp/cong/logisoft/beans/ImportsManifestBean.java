/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.beans;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author saravanan
 */
public class ImportsManifestBean implements Serializable {

    private Long fileId;
    private Long chargeId;
    private Long unitId;
    private Long bookingAcId;
    private String chargeCode;
    private String shipmentType;
    private String glAccount;
    private String agentNo;
    private String agentName;
    private String agentAcctType;
    private String invoiceNo;
    private String billingTerminal;
    private String fileNo;
    private String concatenatedFileNos;
    private String finalDestination;
    private Double totalCharges;
    private String strTotalCharges;
    private BigDecimal totalIPI;
    private String consigneeName;
    private String notifyName;
    private String transShipment;
    private String arrivalNoticeEmail;
    private String arrivalNoticeFax;
    private String className;
    private String destination;
    private String destinationName;
    private String destinationCountry;
    private BigDecimal totalWeightImperial;
    private BigDecimal totalVolumeImperial;
    private String billToParty;
    private String customerNumber;
    private String unitNumber;
    private String basis;
    private String email;
    private String notifyAcct;
    private String notifyEmail;
    private String shipAcct;
    private String shipEmail;
    private String consAcct;
    private String vandorAcct;
    private String transType;
    private String consEmail;
    private String subHouseBl;
    private String pickupCity;
    private String consigneeEmail;
    private String consigneeFax;
    private String notifyFax;
    private String totalCostByInvoiceNo;
    private String agentrelInv;
    private String agentrelnotInv;
    private String createdDate;
    private String postedDate;
    private String createdUser;
    private String description;
    private String invoiceStatus;
    private Long invoiceId;
    private String minimumAmount;
    private BigDecimal apAmount;
    private String fax;

    public String getBasis() {
        return basis;
    }

    public void setBasis(String basis) {
        this.basis = basis;
    }

    public String getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    public String getArrivalNoticeEmail() {
        return arrivalNoticeEmail;
    }

    public void setArrivalNoticeEmail(String arrivalNoticeEmail) {
        this.arrivalNoticeEmail = arrivalNoticeEmail;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    public Double getTotalCharges() {
        return totalCharges;
    }

    public void setTotalCharges(Double totalCharges) {
        this.totalCharges = totalCharges;
    }

    public BigDecimal getTotalIPI() {
        return totalIPI;
    }

    public void setTotalIPI(BigDecimal totalIPI) {
        this.totalIPI = totalIPI;
    }

    public String getTransShipment() {
        return transShipment;
    }

    public void setTransShipment(String transShipment) {
        this.transShipment = transShipment;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getAgentAcctType() {
        return agentAcctType;
    }

    public void setAgentAcctType(String agentAcctType) {
        this.agentAcctType = agentAcctType;
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

    public String getBillingTerminal() {
        return billingTerminal;
    }

    public void setBillingTerminal(String billingTerminal) {
        this.billingTerminal = billingTerminal;
    }

    public Long getChargeId() {
        return chargeId;
    }

    public void setChargeId(Long chargeId) {
        this.chargeId = chargeId;
    }

    public String getGlAccount() {
        return glAccount;
    }

    public void setGlAccount(String glAccount) {
        this.glAccount = glAccount;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getShipmentType() {
        return shipmentType;
    }

    public void setShipmentType(String shipmentType) {
        this.shipmentType = shipmentType;
    }

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDestinationCountry() {
        return destinationCountry;
    }

    public void setDestinationCountry(String destinationCountry) {
        this.destinationCountry = destinationCountry;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public String getStrTotalCharges() {
        return strTotalCharges;
    }

    public void setStrTotalCharges(String strTotalCharges) {
        this.strTotalCharges = strTotalCharges;
    }

    public Long getBookingAcId() {
        return bookingAcId;
    }

    public void setBookingAcId(Long bookingAcId) {
        this.bookingAcId = bookingAcId;
    }

    public BigDecimal getTotalVolumeImperial() {
        return totalVolumeImperial;
    }

    public void setTotalVolumeImperial(BigDecimal totalVolumeImperial) {
        this.totalVolumeImperial = totalVolumeImperial;
    }

    public BigDecimal getTotalWeightImperial() {
        return totalWeightImperial;
    }

    public void setTotalWeightImperial(BigDecimal totalWeightImperial) {
        this.totalWeightImperial = totalWeightImperial;
    }

    public String getBillToParty() {
        return billToParty;
    }

    public void setBillToParty(String billToParty) {
        this.billToParty = billToParty;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getArrivalNoticeFax() {
        return arrivalNoticeFax;
    }

    public void setArrivalNoticeFax(String arrivalNoticeFax) {
        this.arrivalNoticeFax = arrivalNoticeFax;
    }

    public String getNotifyName() {
        return notifyName;
    }

    public void setNotifyName(String notifyName) {
        this.notifyName = notifyName;
    }

    public String getConcatenatedFileNos() {
        return concatenatedFileNos;
    }

    public void setConcatenatedFileNos(String concatenatedFileNos) {
        this.concatenatedFileNos = concatenatedFileNos;
    }

    public String getFinalDestination() {
        return finalDestination;
    }

    public void setFinalDestination(String finalDestination) {
        this.finalDestination = finalDestination;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNotifyAcct() {
        return notifyAcct;
    }

    public void setNotifyAcct(String notifyAcct) {
        this.notifyAcct = notifyAcct;
    }

    public String getNotifyEmail() {
        return notifyEmail;
    }

    public void setNotifyEmail(String notifyEmail) {
        this.notifyEmail = notifyEmail;
    }

    public String getShipAcct() {
        return shipAcct;
    }

    public void setShipAcct(String shipAcct) {
        this.shipAcct = shipAcct;
    }

    public String getShipEmail() {
        return shipEmail;
    }

    public void setShipEmail(String shipEmail) {
        this.shipEmail = shipEmail;
    }

    public String getConsAcct() {
        return consAcct;
    }

    public void setConsAcct(String consAcct) {
        this.consAcct = consAcct;
    }

    public String getConsEmail() {
        return consEmail;
    }

    public void setConsEmail(String consEmail) {
        this.consEmail = consEmail;
    }

    public String getTotalCostByInvoiceNo() {
        return totalCostByInvoiceNo;
    }

    public void setTotalCostByInvoiceNo(String totalCostByInvoiceNo) {
        this.totalCostByInvoiceNo = totalCostByInvoiceNo;
    }

    public String getAgentrelInv() {
        return agentrelInv;
    }

    public void setAgentrelInv(String agentrelInv) {
        this.agentrelInv = agentrelInv;
    }

    public String getAgentrelnotInv() {
        return agentrelnotInv;
    }

    public void setAgentrelnotInv(String agentrelnotInv) {
        this.agentrelnotInv = agentrelnotInv;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(String postedDate) {
        this.postedDate = postedDate;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getSubHouseBl() {
        return subHouseBl;
    }

    public void setSubHouseBl(String subHouseBl) {
        this.subHouseBl = subHouseBl;
    }

    public String getPickupCity() {
        return pickupCity;
    }

    public void setPickupCity(String pickupCity) {
        this.pickupCity = pickupCity;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public String getConsigneeEmail() {
        return consigneeEmail;
    }

    public void setConsigneeEmail(String consigneeEmail) {
        this.consigneeEmail = consigneeEmail;
    }

    public String getConsigneeFax() {
        return consigneeFax;
    }

    public void setConsigneeFax(String consigneeFax) {
        this.consigneeFax = consigneeFax;
    }

    public String getNotifyFax() {
        return notifyFax;
    }

    public void setNotifyFax(String notifyFax) {
        this.notifyFax = notifyFax;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getVandorAcct() {
        return vandorAcct;
    }

    public void setVandorAcct(String vandorAcct) {
        this.vandorAcct = vandorAcct;
    }

    public String getMinimumAmount() {
        return minimumAmount;
    }

    public void setMinimumAmount(String minimumAmount) {
        this.minimumAmount = minimumAmount;
    }

    public BigDecimal getApAmount() {
        return apAmount;
    }

    public void setApAmount(BigDecimal apAmount) {
        this.apAmount = apAmount;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

}
