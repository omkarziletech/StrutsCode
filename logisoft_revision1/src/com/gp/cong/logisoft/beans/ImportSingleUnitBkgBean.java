/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.beans;

import com.gp.cong.common.CommonUtils;
import java.io.Serializable;

/**
 *
 * @author Mei
 */
public class ImportSingleUnitBkgBean implements Serializable {

    private String headerId;
    private String unitId;
    private String dispCode;
    private String dispDesc;
    private String amsSize;
    private String fileId;
    private String fileNumber;
    private String bookingType;
    private String fdUnCode;
    private String fdCityName;
    private String fdState;
    private String totalPiece;
    private Double totalWeightImperial;
    private Double totalVolumeImperial;
    private Boolean pickUp;
    private String shipAcct;
    private String shipName;
    private String notyAcct;
    private String notyName;
    private String consAcct;
    private String consName;
    private String createdBy;
    private Boolean transhipment;
    private String amsNumbers;
    private String amsScac;
    private String amsPieces;
    private String subhousebl;
    private String originalRel;
    private String freightRel;
    private String payRel;
    private String cargoHold;
    private String cargoOrderRel;
    private String custCRel;
    private String deliveryRel;
    private String releseRel;
    private String etaFd;
    private String deliveryStatus;
    private String agentrelInv;
    private String agentrelnotInv;
    private String arStatus;
    private String consbalamt;
    private String originAgentAcct;
    private String originAgentName;
    private String pickedUp;
    private Boolean expressRelease;
    private Boolean segDR;

    public ImportSingleUnitBkgBean() {
    }

    public ImportSingleUnitBkgBean(Object[] obj) throws Exception {
        int index = 0;
        headerId = null == obj[index] ? null : obj[index].toString();
        index++;
        unitId = null == obj[index] ? null : obj[index].toString();
        index++;
        dispCode = null == obj[index] ? null : obj[index].toString();
        index++;
        dispDesc = null == obj[index] ? null : obj[index].toString();
        index++;
        amsSize = null == obj[index] ? null : obj[index].toString();
        index++;
        fileId = null == obj[index] ? null : obj[index].toString();
        index++;
        fileNumber = null == obj[index] ? null : obj[index].toString();
        index++;
        bookingType = null == obj[index] ? null : obj[index].toString();
        index++;
        fdUnCode = null == obj[index] ? null : obj[index].toString();
        index++;
        fdCityName = null == obj[index] ? null : obj[index].toString();
        index++;
        fdState = null == obj[index] ? null : obj[index].toString();
        index++;
        totalPiece = null == obj[index] ? null : obj[index].toString();
        index++;
        totalWeightImperial = null == obj[index] ? null : Double.parseDouble(obj[index].toString());
        index++;
        totalVolumeImperial = null == obj[index] ? null : Double.parseDouble(obj[index].toString());
        index++;
        String doorDelivery = null == obj[index] ? null : obj[index].toString();
        if (CommonUtils.isNotEmpty(doorDelivery) && doorDelivery.equalsIgnoreCase("1")) {
            pickUp = true;
        } else if ("true".equalsIgnoreCase(doorDelivery)) {
            pickUp = true;
        }
        index++;
        shipAcct = null == obj[index] ? null : obj[index].toString();
        index++;
        shipName = null == obj[index] ? null : obj[index].toString();
        index++;
        notyAcct = null == obj[index] ? null : obj[index].toString();
        index++;
        notyName = null == obj[index] ? null : obj[index].toString();
        index++;
        consAcct = null == obj[index] ? null : obj[index].toString();
        index++;
        consName = null == obj[index] ? null : obj[index].toString();
        index++;
        createdBy = null == obj[index] ? null : obj[index].toString();
        index++;
        String importTranshipment = null == obj[index] ? null : obj[index].toString();
        if (CommonUtils.isNotEmpty(importTranshipment) && importTranshipment.equalsIgnoreCase("1")) {
            transhipment = true;
        } else if ("true".equalsIgnoreCase(importTranshipment)) {
            transhipment = true;
        }
        index++;
        amsNumbers = null == obj[index] ? null : obj[index].toString();
        index++;
        amsScac = null == obj[index] ? null : obj[index].toString();
        index++;
        amsPieces = null == obj[index] ? null : obj[index].toString();
        index++;
        subhousebl = null == obj[index] ? null : obj[index].toString();
        index++;
        originalRel = null == obj[index] ? null : obj[index].toString();
        index++;
        freightRel = null == obj[index] ? null : obj[index].toString();
        index++;
        payRel = null == obj[index] ? null : obj[index].toString();
        index++;
        cargoHold = null == obj[index] ? null : obj[index].toString();
        index++;
        cargoOrderRel = null == obj[index] ? null : obj[index].toString();
        index++;
        custCRel = null == obj[index] ? null : obj[index].toString();
        index++;
        deliveryRel = null == obj[index] ? null : obj[index].toString();
        index++;
        releseRel = null == obj[index] ? null : obj[index].toString();
        index++;
        etaFd = null == obj[index] ? null : obj[index].toString();
        index++;
        deliveryStatus = null == obj[index] ? null : obj[index].toString();
        index++;
        pickedUp = null == obj[index] ? null : obj[index].toString();
        index++;
        agentrelInv = null == obj[index] ? null : obj[index].toString();
        index++;
        agentrelnotInv = null == obj[index] ? null : obj[index].toString();
        index++;
        arStatus = null == obj[index] ? null : obj[index].toString();
        index++;
        consbalamt = null == obj[index] ? null : obj[index].toString();
        index++;
        originAgentAcct = null == obj[index] ? null : obj[index].toString();
        index++;
        originAgentName = null == obj[index] ? null : obj[index].toString();
        index++;
        String expressReleases = null == obj[index] ? null : obj[index].toString();
        if (CommonUtils.isNotEmpty(expressReleases) && "1".equalsIgnoreCase(expressReleases)) {
            expressRelease = true;
        } else if ("true".equalsIgnoreCase(expressReleases)) {
            expressRelease = true;
        }
        index++;
        String segDr = null == obj[index] ? null : obj[index].toString();
        if (CommonUtils.isNotEmpty(segDr)) {
            segDR = true;
        } else  {
            segDR = false;
        }
    }

    public String getHeaderId() {
        return headerId;
    }

    public void setHeaderId(String headerId) {
        this.headerId = headerId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getDispCode() {
        return dispCode;
    }

    public void setDispCode(String dispCode) {
        this.dispCode = dispCode;
    }

    public String getDispDesc() {
        return dispDesc;
    }

    public void setDispDesc(String dispDesc) {
        this.dispDesc = dispDesc;
    }

    public String getAmsSize() {
        return amsSize;
    }

    public void setAmsSize(String amsSize) {
        this.amsSize = amsSize;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public String getBookingType() {
        return bookingType;
    }

    public void setBookingType(String bookingType) {
        this.bookingType = bookingType;
    }

    public String getFdUnCode() {
        return fdUnCode;
    }

    public void setFdUnCode(String fdUnCode) {
        this.fdUnCode = fdUnCode;
    }

    public String getFdCityName() {
        return fdCityName;
    }

    public void setFdCityName(String fdCityName) {
        this.fdCityName = fdCityName;
    }

    public String getFdState() {
        return fdState;
    }

    public void setFdState(String fdState) {
        this.fdState = fdState;
    }

    public String getTotalPiece() {
        return totalPiece;
    }

    public void setTotalPiece(String totalPiece) {
        this.totalPiece = totalPiece;
    }

    public Double getTotalWeightImperial() {
        return totalWeightImperial;
    }

    public void setTotalWeightImperial(Double totalWeightImperial) {
        this.totalWeightImperial = totalWeightImperial;
    }

    public Double getTotalVolumeImperial() {
        return totalVolumeImperial;
    }

    public void setTotalVolumeImperial(Double totalVolumeImperial) {
        this.totalVolumeImperial = totalVolumeImperial;
    }

    public String getShipAcct() {
        return shipAcct;
    }

    public void setShipAcct(String shipAcct) {
        this.shipAcct = shipAcct;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public String getNotyAcct() {
        return notyAcct;
    }

    public void setNotyAcct(String notyAcct) {
        this.notyAcct = notyAcct;
    }

    public String getNotyName() {
        return notyName;
    }

    public void setNotyName(String notyName) {
        this.notyName = notyName;
    }

    public String getConsAcct() {
        return consAcct;
    }

    public void setConsAcct(String consAcct) {
        this.consAcct = consAcct;
    }

    public String getConsName() {
        return consName;
    }

    public void setConsName(String consName) {
        this.consName = consName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Boolean getTranshipment() {
        return transhipment;
    }

    public void setTranshipment(Boolean transhipment) {
        this.transhipment = transhipment;
    }

    public String getAmsNumbers() {
        return amsNumbers;
    }

    public void setAmsNumbers(String amsNumbers) {
        this.amsNumbers = amsNumbers;
    }

    public String getAmsPieces() {
        return amsPieces;
    }

    public void setAmsPieces(String amsPieces) {
        this.amsPieces = amsPieces;
    }

    public String getAmsScac() {
        return amsScac;
    }

    public void setAmsScac(String amsScac) {
        this.amsScac = amsScac;
    }

    public String getOriginalRel() {
        return originalRel;
    }

    public void setOriginalRel(String originalRel) {
        this.originalRel = originalRel;
    }

    public String getFreightRel() {
        return freightRel;
    }

    public void setFreightRel(String freightRel) {
        this.freightRel = freightRel;
    }

    public String getPayRel() {
        return payRel;
    }

    public void setPayRel(String payRel) {
        this.payRel = payRel;
    }

    public String getCargoHold() {
        return cargoHold;
    }

    public void setCargoHold(String cargoHold) {
        this.cargoHold = cargoHold;
    }

    public String getCargoOrderRel() {
        return cargoOrderRel;
    }

    public void setCargoOrderRel(String cargoOrderRel) {
        this.cargoOrderRel = cargoOrderRel;
    }

    public String getCustCRel() {
        return custCRel;
    }

    public void setCustCRel(String custCRel) {
        this.custCRel = custCRel;
    }

    public String getDeliveryRel() {
        return deliveryRel;
    }

    public void setDeliveryRel(String deliveryRel) {
        this.deliveryRel = deliveryRel;
    }

    public String getReleseRel() {
        return releseRel;
    }

    public void setReleseRel(String releseRel) {
        this.releseRel = releseRel;
    }

    public String getEtaFd() {
        return etaFd;
    }

    public void setEtaFd(String etaFd) {
        this.etaFd = etaFd;
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

    public String getConsbalamt() {
        return consbalamt;
    }

    public void setConsbalamt(String consbalamt) {
        this.consbalamt = consbalamt;
    }

    public Boolean getPickUp() {
        return pickUp;
    }

    public void setPickUp(Boolean pickUp) {
        this.pickUp = pickUp;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getSubhousebl() {
        return subhousebl;
    }

    public void setSubhousebl(String subhousebl) {
        this.subhousebl = subhousebl;
    }

    public String getOriginAgentAcct() {
        return originAgentAcct;
    }

    public void setOriginAgentAcct(String originAgentAcct) {
        this.originAgentAcct = originAgentAcct;
    }

    public String getOriginAgentName() {
        return originAgentName;
    }

    public void setOriginAgentName(String originAgentName) {
        this.originAgentName = originAgentName;
    }

    public String getPickedUp() {
        return pickedUp;
    }

    public void setPickedUp(String pickedUp) {
        this.pickedUp = pickedUp;
    }

    public Boolean getExpressRelease() {
        return expressRelease;
    }

    public void setExpressRelease(Boolean expressRelease) {
        this.expressRelease = expressRelease;
    }

    public String getArStatus() {
        return arStatus;
    }

    public void setArStatus(String arStatus) {
        this.arStatus = arStatus;
    }

    public Boolean getSegDR() {
        return segDR;
    }

    public void setSegDR(Boolean segDR) {
        this.segDR = segDR;
    }
}
