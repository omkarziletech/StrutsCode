/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.beans;

/**
 *
 * @author Mei
 */
public class LclImportUnitBkgModel {

    public String fileId;
    public String fileNo;
    public String bookingType;
    public String fdUnloc;
    public String fdName;
    public String originAgentNo;
    public String originAgentName;
    public String shipAcctNo;
    public String shipperName;
    public String consAcctNo;
    public String consigneeName;
    public String notyAcctNo;
    public String notifyName;
    public String totalPiece;
    public String totalweightimperial;
    public String totalvolumeimperial;
    public String collectAmt;
    public String agentReleAmt;
    public String agentNotReleAmt;
    public String doorStatus;
    public String amsDetails;
    public Boolean segFileFlag;
    public String originalRecv;
    public String freightRel;
    public String payRel;
    public String cargohold;
    public String cargoorder;
    public String cusrcRecv;
    public String delivrecv;
    public String relerecv;
    public Boolean expressRelease;
    public String etafd;
    public String pickedUp;//pickUpDatetime
    public String defaultAms;
    private String bookedBy;
    private String subhouseBl;
    private String arInvoiceStatus;
    private String segAmsNo;
    public Boolean hazmat;
    private Double amtTotal;
    private Double paidAmt;
    private Boolean isPaymentType;
    private String totalArBalanceAmount;

    public String getAgentNotReleAmt() {
        return agentNotReleAmt;
    }

    public void setAgentNotReleAmt(String agentNotReleAmt) {
        this.agentNotReleAmt = agentNotReleAmt;
    }

    public String getAgentReleAmt() {
        return agentReleAmt;
    }

    public void setAgentReleAmt(String agentReleAmt) {
        this.agentReleAmt = agentReleAmt;
    }

    public String getAmsDetails() {
        return amsDetails;
    }

    public void setAmsDetails(String amsDetails) {
        this.amsDetails = amsDetails;
    }

    public String getBookingType() {
        return bookingType;
    }

    public void setBookingType(String bookingType) {
        this.bookingType = bookingType;
    }

    public String getCargohold() {
        return cargohold;
    }

    public void setCargohold(String cargohold) {
        this.cargohold = cargohold;
    }

    public String getCargoorder() {
        return cargoorder;
    }

    public void setCargoorder(String cargoorder) {
        this.cargoorder = cargoorder;
    }

    public String getCollectAmt() {
        return collectAmt;
    }

    public void setCollectAmt(String collectAmt) {
        this.collectAmt = collectAmt;
    }

    public String getConsAcctNo() {
        return consAcctNo;
    }

    public void setConsAcctNo(String consAcctNo) {
        this.consAcctNo = consAcctNo;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getCusrcRecv() {
        return cusrcRecv;
    }

    public void setCusrcRecv(String cusrcRecv) {
        this.cusrcRecv = cusrcRecv;
    }

    public String getDefaultAms() {
        return defaultAms;
    }

    public void setDefaultAms(String defaultAms) {
        this.defaultAms = defaultAms;
    }

    public String getDelivrecv() {
        return delivrecv;
    }

    public void setDelivrecv(String delivrecv) {
        this.delivrecv = delivrecv;
    }

    public String getDoorStatus() {
        return doorStatus;
    }

    public void setDoorStatus(String doorStatus) {
        this.doorStatus = doorStatus;
    }

    public String getEtafd() {
        return etafd;
    }

    public void setEtafd(String etafd) {
        this.etafd = etafd;
    }

    public Boolean getExpressRelease() {
        return expressRelease;
    }

    public void setExpressRelease(Boolean expressRelease) {
        this.expressRelease = expressRelease;
    }

    public String getFdName() {
        return fdName;
    }

    public void setFdName(String fdName) {
        this.fdName = fdName;
    }

    public String getFdUnloc() {
        return fdUnloc;
    }

    public void setFdUnloc(String fdUnloc) {
        this.fdUnloc = fdUnloc;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    public String getFreightRel() {
        return freightRel;
    }

    public void setFreightRel(String freightRel) {
        this.freightRel = freightRel;
    }

    public String getNotifyName() {
        return notifyName;
    }

    public void setNotifyName(String notifyName) {
        this.notifyName = notifyName;
    }

    public String getNotyAcctNo() {
        return notyAcctNo;
    }

    public void setNotyAcctNo(String notyAcctNo) {
        this.notyAcctNo = notyAcctNo;
    }

    public String getOriginAgentName() {
        return originAgentName;
    }

    public void setOriginAgentName(String originAgentName) {
        this.originAgentName = originAgentName;
    }

    public String getOriginAgentNo() {
        return originAgentNo;
    }

    public void setOriginAgentNo(String originAgentNo) {
        this.originAgentNo = originAgentNo;
    }

    public String getOriginalRecv() {
        return originalRecv;
    }

    public void setOriginalRecv(String originalRecv) {
        this.originalRecv = originalRecv;
    }

    public String getPayRel() {
        return payRel;
    }

    public void setPayRel(String payRel) {
        this.payRel = payRel;
    }

    public String getPickedUp() {
        return pickedUp;
    }

    public void setPickedUp(String pickedUp) {
        this.pickedUp = pickedUp;
    }

    public String getRelerecv() {
        return relerecv;
    }

    public void setRelerecv(String relerecv) {
        this.relerecv = relerecv;
    }

    public Boolean getSegFileFlag() {
        return segFileFlag;
    }

    public void setSegFileFlag(Boolean segFileFlag) {
        this.segFileFlag = segFileFlag;
    }

    public String getShipAcctNo() {
        return shipAcctNo;
    }

    public void setShipAcctNo(String shipAcctNo) {
        this.shipAcctNo = shipAcctNo;
    }

    public String getShipperName() {
        return shipperName;
    }

    public void setShipperName(String shipperName) {
        this.shipperName = shipperName;
    }

    public String getTotalPiece() {
        return totalPiece;
    }

    public void setTotalPiece(String totalPiece) {
        this.totalPiece = totalPiece;
    }

    public String getTotalvolumeimperial() {
        return totalvolumeimperial;
    }

    public void setTotalvolumeimperial(String totalvolumeimperial) {
        this.totalvolumeimperial = totalvolumeimperial;
    }

    public String getTotalweightimperial() {
        return totalweightimperial;
    }

    public void setTotalweightimperial(String totalweightimperial) {
        this.totalweightimperial = totalweightimperial;
    }

    public String getArInvoiceStatus() {
        return arInvoiceStatus;
    }

    public void setArInvoiceStatus(String arInvoiceStatus) {
        this.arInvoiceStatus = arInvoiceStatus;
    }

    public String getBookedBy() {
        return bookedBy;
    }

    public void setBookedBy(String bookedBy) {
        this.bookedBy = bookedBy;
    }

    public String getSubhouseBl() {
        return subhouseBl;
    }

    public void setSubhouseBl(String subhouseBl) {
        this.subhouseBl = subhouseBl;
    }

    public String getSegAmsNo() {
        return segAmsNo;
    }

    public void setSegAmsNo(String segAmsNo) {
        this.segAmsNo = segAmsNo;
    }

    public Boolean getHazmat() {
        return hazmat;
    }

    public void setHazmat(Boolean hazmat) {
        this.hazmat = hazmat;
    }

    public Double getAmtTotal() {
        return amtTotal;
    }

    public void setAmtTotal(Double amtTotal) {
        this.amtTotal = amtTotal;
    }

    public Double getPaidAmt() {
        return paidAmt;
    }

    public void setPaidAmt(Double paidAmt) {
        this.paidAmt = paidAmt;
    }

   public Boolean getIsPaymentType() {
        return isPaymentType;
    }

    public void setIsPaymentType(Boolean isPaymentType) {
        this.isPaymentType = isPaymentType;
    }

    public String getTotalArBalanceAmount() {
        return totalArBalanceAmount;
    }

    public void setTotalArBalanceAmount(String totalArBalanceAmount) {
        this.totalArBalanceAmount = totalArBalanceAmount;
    }
    
}
