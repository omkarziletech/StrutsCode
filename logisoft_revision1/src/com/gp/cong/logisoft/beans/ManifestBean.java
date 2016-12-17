/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.beans;

import com.gp.cong.common.CommonUtils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author saravanan
 */
public class ManifestBean implements Serializable {

    private Integer fileNumberId;
    private Long fileId;
    private String fileNo;
    private String blNo;
    private String status;
    private String state;
    private String disposition;
    private String dispositionTooltip;
    private String crtLocation;
    private String crtLocationTooltip;
    private String statusLabel;
    private String className;
    private Integer totalPieceCount;
    private BigDecimal totalWeightImperial;
    private BigDecimal totalVolumeImperial;
    private String totalBilledAmount;
    private String billingType;
    private String orderNo;
    private String receiptNo;
    private String shipperName;
    private String consigneeName;
    private String forwarderName;
    private String origin;
    private String destination;
    private String pol;
    private String pod;
    private String termNo;
    private String terminalLocation;
    private String hotCodes;
    private String hotCodeKey = "";
    private String shortCodeKey = "";
    private String pooPickup;
    private String pickupCity;
    private String originName;
    private String originState;
    private String polName;
    private String polState;
    private String podName;
    private String podCountry;
    private String destinationName;
    private String destinationCountry;
    private String doc;
    private String blInvoiceNo;
    private String bookedVoyageNo;
    private String unitException;
    private Integer postedByUserId;
    private String hazNo;
    private boolean hazmat;
    private Date enteredDatetime;
    private String modifiedBy;
    private BigDecimal colCharge;
    private BigDecimal ppdCharge;
    private String ppdParties;
    private BigDecimal blLbs;
    private BigDecimal blKgs;
    private BigDecimal blCft;
    private BigDecimal blCbm;
    private String hotCodeCount;
    private Boolean isCorrection;
    private Long bkgPieceId;
    private String businessUnit;
    private String terminalEmail;
    private String fileState;
    private String rateType;
    private BigDecimal ffComm;
    private BigDecimal ftfFee;
    private String arInvoiceNumber;
    private String readytoPost;
    private Boolean isNoBLRequired;
    private Integer blCount;
    private Integer drCount;

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Integer getFileNumberId() {
        return fileNumberId;
    }

    public void setFileNumberId(Integer fileNumberId) {
        this.fileNumberId = fileNumberId;
    }

    public String getBlNo() {
        return blNo;
    }

    public void setBlNo(String blNo) {
        this.blNo = blNo;
    }

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCrtLocation() {
        return crtLocation;
    }

    public void setCrtLocation(String crtLocation) {
        this.crtLocation = crtLocation;
    }

    public String getCrtLocationTooltip() {
        return crtLocationTooltip;
    }

    public void setCrtLocationTooltip(String crtLocationTooltip) {
        this.crtLocationTooltip = crtLocationTooltip;
    }

    public String getDisposition() {
        return disposition;
    }

    public void setDisposition(String disposition) {
        this.disposition = disposition;
    }

    public String getDispositionTooltip() {
        return dispositionTooltip;
    }

    public void setDispositionTooltip(String dispositionTooltip) {
        this.dispositionTooltip = dispositionTooltip;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getBillingType() {
        return billingType;
    }

    public void setBillingType(String billingType) {
        this.billingType = billingType;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public Integer getTotalPieceCount() {
        return totalPieceCount;
    }

    public void setTotalPieceCount(Integer totalPieceCount) {
        this.totalPieceCount = totalPieceCount;
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

    public String getTotalBilledAmount() {
        return totalBilledAmount;
    }

    public void setTotalBilledAmount(String totalBilledAmount) {
        this.totalBilledAmount = totalBilledAmount;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getForwarderName() {
        return forwarderName;
    }

    public void setForwarderName(String forwarderName) {
        this.forwarderName = forwarderName;
    }

    public String getShipperName() {
        return shipperName;
    }

    public void setShipperName(String shipperName) {
        this.shipperName = shipperName;
    }

    public String getBlInvoiceNo() {
        return blInvoiceNo;
    }

    public void setBlInvoiceNo(String blInvoiceNo) {
        this.blInvoiceNo = blInvoiceNo;
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

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public String getHotCodes() {
        return hotCodes;
    }

    public void setHotCodes(String hotCodes) {
        this.hotCodes = hotCodes;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public String getOriginState() {
        return originState;
    }

    public void setOriginState(String originState) {
        this.originState = originState;
    }

    public String getPickupCity() {
        return pickupCity;
    }

    public void setPickupCity(String pickupCity) {
        this.pickupCity = pickupCity;
    }

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

    public String getPodCountry() {
        return podCountry;
    }

    public void setPodCountry(String podCountry) {
        this.podCountry = podCountry;
    }

    public String getPodName() {
        return podName;
    }

    public void setPodName(String podName) {
        this.podName = podName;
    }

    public String getPol() {
        return pol;
    }

    public void setPol(String pol) {
        this.pol = pol;
    }

    public String getPolName() {
        return polName;
    }

    public void setPolName(String polName) {
        this.polName = polName;
    }

    public String getPolState() {
        return polState;
    }

    public void setPolState(String polState) {
        this.polState = polState;
    }

    public String getPooPickup() {
        return pooPickup;
    }

    public void setPooPickup(String pooPickup) {
        this.pooPickup = pooPickup;
    }

    public String getTermNo() {
        return termNo;
    }

    public void setTermNo(String termNo) {
        this.termNo = termNo;
    }

    public String getTerminalLocation() {
        return terminalLocation;
    }

    public void setTerminalLocation(String terminalLocation) {
        this.terminalLocation = terminalLocation;
    }

    public String getHotCodeKey() {
        return hotCodeKey;
    }

    public void setHotCodeKey(String hotCodeKey) {
        this.hotCodeKey = hotCodeKey;
    }

    public String getShortCodeKey() {
        if (CommonUtils.isNotEmpty(hotCodeKey)) {
            String[] value = hotCodeKey.split(",");
            if (value.length >= 3) {
                return value[0] + "/" + value[1] + "/" + value[2];
            } else {
                return hotCodeKey.replace(",", "/");
            }
        } else {
            return hotCodeKey;
        }
    }

    public void setShortCodeKey(String shortCodeKey) {
        this.shortCodeKey = shortCodeKey;
    }

    public String getBookedVoyageNo() {
        return bookedVoyageNo;
    }

    public void setBookedVoyageNo(String bookedVoyageNo) {
        this.bookedVoyageNo = bookedVoyageNo;
    }

    public String getUnitException() {
        return unitException;
    }

    public void setUnitException(String unitException) {
        this.unitException = unitException;
    }

    public Integer getPostedByUserId() {
        return postedByUserId;
    }

    public void setPostedByUserId(Integer postedByUserId) {
        this.postedByUserId = postedByUserId;
    }

    public String getHazNo() {
        return hazNo;
    }

    public void setHazNo(String hazNo) {
        this.hazNo = hazNo;
    }

    public boolean isHazmat() {
        return hazmat;
    }

    public void setHazmat(boolean hazmat) {
        this.hazmat = hazmat;
    }

    public Date getEnteredDatetime() {
        return enteredDatetime;
    }

    public void setEnteredDatetime(Date enteredDatetime) {
        this.enteredDatetime = enteredDatetime;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public BigDecimal getColCharge() {
        return colCharge;
    }

    public void setColCharge(BigDecimal colCharge) {
        this.colCharge = colCharge;
    }

    public BigDecimal getPpdCharge() {
        return ppdCharge;
    }

    public void setPpdCharge(BigDecimal ppdCharge) {
        this.ppdCharge = ppdCharge;
    }

    public String getPpdParties() {
        return ppdParties;
    }

    public void setPpdParties(String ppdParties) {
        this.ppdParties = ppdParties;
    }

    public BigDecimal getBlCbm() {
        return blCbm;
    }

    public void setBlCbm(BigDecimal blCbm) {
        this.blCbm = blCbm;
    }

    public BigDecimal getBlCft() {
        return blCft;
    }

    public void setBlCft(BigDecimal blCft) {
        this.blCft = blCft;
    }

    public BigDecimal getBlKgs() {
        return blKgs;
    }

    public void setBlKgs(BigDecimal blKgs) {
        this.blKgs = blKgs;
    }

    public BigDecimal getBlLbs() {
        return blLbs;
    }

    public void setBlLbs(BigDecimal blLbs) {
        this.blLbs = blLbs;
    }

    public String getHotCodeCount() {
        return hotCodeCount;
    }

    public void setHotCodeCount(String hotCodeCount) {
        this.hotCodeCount = hotCodeCount;
    }

    public Boolean getIsCorrection() {
        return isCorrection;
    }

    public void setIsCorrection(Boolean isCorrection) {
        this.isCorrection = isCorrection;
    }

    public Long getBkgPieceId() {
        return bkgPieceId;
    }

    public void setBkgPieceId(Long bkgPieceId) {
        this.bkgPieceId = bkgPieceId;
    }

    public String getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(String businessUnit) {
        this.businessUnit = businessUnit;
    }

    public String getTerminalEmail() {
        return terminalEmail;
    }

    public void setTerminalEmail(String terminalEmail) {
        this.terminalEmail = terminalEmail;
    }

    public String getFileState() {
        return fileState;
    }

    public void setFileState(String fileState) {
        this.fileState = fileState;
    }

    public String getRateType() {
        if ("C".equalsIgnoreCase(rateType)) {
            return "CTC";
        } else if ("F".equalsIgnoreCase(rateType)) {
            return "FTF";
        } else if ("R".equalsIgnoreCase(rateType)) {
            return "Ret";
        }
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    public BigDecimal getFfComm() {
        return ffComm;
    }

    public void setFfComm(BigDecimal ffComm) {
        this.ffComm = ffComm;
    }

    public BigDecimal getFtfFee() {
        return ftfFee;
    }

    public void setFtfFee(BigDecimal ftfFee) {
        this.ftfFee = ftfFee;
    }

    public String getArInvoiceNumber() {
        return arInvoiceNumber;
    }

    public void setArInvoiceNumber(String arInvoiceNumber) {
        this.arInvoiceNumber = arInvoiceNumber;
    }

    public String getReadytoPost() {
        return readytoPost;
    }

    public void setReadytoPost(String readytoPost) {
        this.readytoPost = readytoPost;
    }

    public Boolean getIsNoBLRequired() {
        return isNoBLRequired;
    }

    public void setIsNoBLRequired(Boolean isNoBLRequired) {
        this.isNoBLRequired = isNoBLRequired;
    }

    public Integer getBlCount() {
        return blCount;
    }

    public void setBlCount(Integer blCount) {
        this.blCount = blCount;
    }

    public Integer getDrCount() {
        return drCount;
    }

    public void setDrCount(Integer drCount) {
        this.drCount = drCount;
    }
}
