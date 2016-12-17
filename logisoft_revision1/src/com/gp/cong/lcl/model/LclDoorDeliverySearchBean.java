/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.lcl.model;

import java.io.Serializable;

/**
 *
 * @author PALRAJ
 */
public class LclDoorDeliverySearchBean implements Serializable{
    
    private String voyageNo;
    private String polUnCode;
    private String pol;
    private String podUnCode;
    private String pod;
    private String etaDate;
    private String dispoCode;
    private String dispoDesc;
    private String lastFreeDate;
    private Long fileNumberId;
    private String fileNumber;
    private String houseBl;
    private String customsClearanceReceived;
    private String deliveryOrderReceived;
    private String deliveryCommercial;
    private String liftGate;
    private Boolean hazmat;
    private String pickupEstDate;
    private String pickedUpDateTime;
    private String deliveryEstDate;
    private String deliveredDateTime;
    private double buy;
    private double sell;
    private String doorDeliveryStatus;
    private String scacCode;
    private String carrierName;
    private String pickupReferenceNo;
    private String needPod;
    private boolean transshipment;
    private String state;
    private String doorDeliveryDesc;
    private String etaToDate;
    private String zipCode;
    private String city;

    public String getVoyageNo() {
        return voyageNo;
    }

    public void setVoyageNo(String voyageNo) {
        this.voyageNo = voyageNo;
    }

    public String getPolUnCode() {
        return polUnCode;
    }

    public void setPolUnCode(String polUnCode) {
        this.polUnCode = polUnCode;
    }

    public String getPol() {
        return pol;
    }

    public void setPol(String pol) {
        this.pol = pol;
    }

    public String getPodUnCode() {
        return podUnCode;
    }

    public void setPodUnCode(String podUnCode) {
        this.podUnCode = podUnCode;
    }

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

    public String getEtaDate() {
        return etaDate;
    }

    public void setEtaDate(String etaDate) {
        this.etaDate = etaDate;
    }

    public String getDispoCode() {
        return dispoCode;
    }

    public void setDispoCode(String dispoCode) {
        this.dispoCode = dispoCode;
    }

    public String getDispoDesc() {
        return dispoDesc;
    }

    public void setDispoDesc(String dispoDesc) {
        this.dispoDesc = dispoDesc;
    }

    public String getLastFreeDate() {
        return lastFreeDate;
    }

    public void setLastFreeDate(String lastFreeDate) {
        this.lastFreeDate = lastFreeDate;
    }

    public Long getFileNumberId() {
        return fileNumberId;
    }

    public void setFileNumberId(Long fileNumberId) {
        this.fileNumberId = fileNumberId;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public String getHouseBl() {
        return houseBl;
    }

    public void setHouseBl(String houseBl) {
        this.houseBl = houseBl;
    }

    public String getCustomsClearanceReceived() {
        return customsClearanceReceived;
    }

    public void setCustomsClearanceReceived(String customsClearanceReceived) {
        this.customsClearanceReceived = customsClearanceReceived;
    }

    public String getDeliveryOrderReceived() {
        return deliveryOrderReceived;
    }

    public void setDeliveryOrderReceived(String deliveryOrderReceived) {
        this.deliveryOrderReceived = deliveryOrderReceived;
    }

    public String getDeliveryCommercial() {
        return deliveryCommercial;
    }

    public void setDeliveryCommercial(String deliveryCommercial) {
        this.deliveryCommercial = deliveryCommercial;
    }

    public String getLiftGate() {
        return liftGate;
    }

    public void setLiftGate(String liftGate) {
        this.liftGate = liftGate;
    }

    public Boolean getHazmat() {
        return hazmat;
    }

    public void setHazmat(Boolean hazmat) {
        this.hazmat = hazmat;
    }

    public String getPickupEstDate() {
        return pickupEstDate;
    }

    public void setPickupEstDate(String pickupEstDate) {
        this.pickupEstDate = pickupEstDate;
    }

    public String getPickedUpDateTime() {
        return pickedUpDateTime;
    }

    public void setPickedUpDateTime(String pickedUpDateTime) {
        this.pickedUpDateTime = pickedUpDateTime;
    }

    public String getDeliveryEstDate() {
        return deliveryEstDate;
    }

    public void setDeliveryEstDate(String deliveryEstDate) {
        this.deliveryEstDate = deliveryEstDate;
    }

    public String getDeliveredDateTime() {
        return deliveredDateTime;
    }

    public void setDeliveredDateTime(String deliveredDateTime) {
        this.deliveredDateTime = deliveredDateTime;
    }

    public double getBuy() {
        return buy;
    }

    public void setBuy(double buy) {
        this.buy = buy;
    }

    public double getSell() {
        return sell;
    }

    public void setSell(double sell) {
        this.sell = sell;
    }

    public String getDoorDeliveryStatus() {
        return doorDeliveryStatus;
    }

    public void setDoorDeliveryStatus(String doorDeliveryStatus) {
        this.doorDeliveryStatus = doorDeliveryStatus;
    }

    public String getScacCode() {
        return scacCode;
    }

    public void setScacCode(String scacCode) {
        this.scacCode = scacCode;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getPickupReferenceNo() {
        return pickupReferenceNo;
    }

    public void setPickupReferenceNo(String pickupReferenceNo) {
        this.pickupReferenceNo = pickupReferenceNo;
    }

    public String getNeedPod() {
        return needPod;
    }

    public void setNeedPod(String needPod) {
        this.needPod = needPod;
    }

    public boolean isTransshipment() {
        return transshipment;
    }

    public void setTransshipment(boolean transshipment) {
        this.transshipment = transshipment;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDoorDeliveryDesc() {
        return doorDeliveryDesc;
    }

    public void setDoorDeliveryDesc(String doorDeliveryDesc) {
        this.doorDeliveryDesc = doorDeliveryDesc;
    }

    public String getEtaToDate() {
        return etaToDate;
    }

    public void setEtaToDate(String etaToDate) {
        this.etaToDate = etaToDate;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    }
