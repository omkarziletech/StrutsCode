/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.logiware.ims.client;

/**
 *
 * @author Shan
 */
public class IMSQuote implements Comparable<IMSQuote>{
    private String uniqueId;
    private String originId;
    private String originName;
    private String destinationId;
    private String destinationName;
    private String emptyId;
    private String emptyName;
    private String importExportInd;
    private String containerSizeId;
    private String containerSizeDesc;
    private String containerTypeId;
    private String containerTypeDesc;
    private String viaId;
    private String viaDesc;
    private String mode;
    private String trucker;
    private String truckerName;
    private String quoteNumber;
    private String hazardousFees;
    private String reeferFees;
    private String overWeightFees;
    private String cleanTruckFees;
    private String quoteAmount;
    private String fuelSurcharge;
    private String fuelFees;
    private String basePlusSpecial;
    private String allInRate;
    private String quote2Amt;
    private String fuel2Pct;
    private String allIn2Rate;
    private String base2PlusSpecial;
    private String fuel2Fees;
    private String effectiveDate;
    private String expiryDate;
    private String createdBy;
    private String createdOn;
    private String requestedBy;
    private String requestedByEmail;
    private String requestedByPhone;
    private String requestedByExt;
    private boolean hazardous = false;

    public IMSQuote() {
    }

    public String getAllIn2Rate() {
        return null!=allIn2Rate?allIn2Rate.replace(",", ""):"0";
    }

    public void setAllIn2Rate(String allIn2Rate) {
        this.allIn2Rate = allIn2Rate;
    }

    public String getAllInRate() {
        return null!=allInRate?allInRate.replace(",", ""):"0";
    }

    public void setAllInRate(String allInRate) {
        this.allInRate = allInRate;
    }

    public String getBase2PlusSpecial() {
        return base2PlusSpecial;
    }

    public void setBase2PlusSpecial(String base2PlusSpecial) {
        this.base2PlusSpecial = base2PlusSpecial;
    }

    public String getBasePlusSpecial() {
        return basePlusSpecial;
    }

    public void setBasePlusSpecial(String basePlusSpecial) {
        this.basePlusSpecial = basePlusSpecial;
    }

    public String getCleanTruckFees() {
        return cleanTruckFees;
    }

    public void setCleanTruckFees(String cleanTruckFees) {
        this.cleanTruckFees = cleanTruckFees;
    }

    public String getContainerSizeDesc() {
        return containerSizeDesc;
    }

    public void setContainerSizeDesc(String containerSizeDesc) {
        this.containerSizeDesc = containerSizeDesc;
    }

    public String getContainerSizeId() {
        return containerSizeId;
    }

    public void setContainerSizeId(String containerSizeId) {
        this.containerSizeId = containerSizeId;
    }

    public String getContainerTypeDesc() {
        return containerTypeDesc;
    }

    public void setContainerTypeDesc(String containerTypeDesc) {
        this.containerTypeDesc = containerTypeDesc;
    }

    public String getContainerTypeId() {
        return containerTypeId;
    }

    public void setContainerTypeId(String containerTypeId) {
        this.containerTypeId = containerTypeId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getEmptyId() {
        return emptyId;
    }

    public void setEmptyId(String emptyId) {
        this.emptyId = emptyId;
    }

    public String getEmptyName() {
        return emptyName;
    }

    public void setEmptyName(String emptyName) {
        this.emptyName = emptyName;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getFuel2Fees() {
        return null!=fuel2Fees?fuel2Fees.replace(",", ""):"0";
    }

    public void setFuel2Fees(String fuel2Fees) {
        this.fuel2Fees = fuel2Fees;
    }

    public String getFuel2Pct() {
        return fuel2Pct;
    }

    public void setFuel2Pct(String fuel2Pct) {
        this.fuel2Pct = fuel2Pct;
    }

    public String getFuelFees() {
        return null!=fuelFees?fuelFees.replace(",", ""):"0";
    }

    public void setFuelFees(String fuelFees) {
        this.fuelFees = fuelFees;
    }

    public String getFuelSurcharge() {
        return fuelSurcharge;
    }

    public void setFuelSurcharge(String fuelSurcharge) {
        this.fuelSurcharge = fuelSurcharge;
    }

    public String getHazardousFees() {
        return null!=hazardousFees?hazardousFees.replace(",", ""):"0";
    }

    public void setHazardousFees(String hazardousFees) {
        this.hazardousFees = hazardousFees;
    }

    public String getImportExportInd() {
        return importExportInd;
    }

    public void setImportExportInd(String importExportInd) {
        this.importExportInd = importExportInd;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public String getOverWeightFees() {
        return overWeightFees;
    }

    public void setOverWeightFees(String overWeightFees) {
        this.overWeightFees = overWeightFees;
    }

    public String getQuote2Amt() {
        return null!=quote2Amt?quote2Amt.replace(",", ""):"0";
    }

    public void setQuote2Amt(String quote2Amt) {
        this.quote2Amt = quote2Amt;
    }

    public String getQuoteAmount() {
        return null!=quoteAmount?quoteAmount.replace(",", ""):"0";
    }

    public void setQuoteAmount(String quoteAmount) {
        this.quoteAmount = quoteAmount;
    }

    public String getQuoteNumber() {
        return quoteNumber;
    }

    public void setQuoteNumber(String quoteNumber) {
        this.quoteNumber = quoteNumber;
    }

    public String getReeferFees() {
        return reeferFees;
    }

    public void setReeferFees(String reeferFees) {
        this.reeferFees = reeferFees;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    public String getRequestedByEmail() {
        return requestedByEmail;
    }

    public void setRequestedByEmail(String requestedByEmail) {
        this.requestedByEmail = requestedByEmail;
    }

    public String getRequestedByExt() {
        return requestedByExt;
    }

    public void setRequestedByExt(String requestedByExt) {
        this.requestedByExt = requestedByExt;
    }

    public String getRequestedByPhone() {
        return requestedByPhone;
    }

    public void setRequestedByPhone(String requestedByPhone) {
        this.requestedByPhone = requestedByPhone;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getViaId() {
        return viaId;
    }

    public void setViaId(String viaId) {
        this.viaId = viaId;
    }

    public String getViaDesc() {
        return viaDesc;
    }

    public void setViaDesc(String viaDesc) {
        this.viaDesc = viaDesc;
    }

    public boolean isHazardous() {
        return hazardous;
    }

    public void setHazardous(boolean hazardous) {
        this.hazardous = hazardous;
    }

    public String getTrucker() {
        return trucker;
    }

    public void setTrucker(String trucker) {
        this.trucker = trucker;
    }

    public String getTruckerName() {
        return truckerName;
    }

    public void setTruckerName(String truckerName) {
        this.truckerName = truckerName;
    }
    
    public int compareTo(IMSQuote imsQuote) {
        if (null == imsQuote || null == this || null == imsQuote.getAllIn2Rate() || null == this.getAllIn2Rate()) {
            return 0;
        } else {
            return this.getAllIn2Rate().compareTo(imsQuote.getAllIn2Rate());
        }
    }

}