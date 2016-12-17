package com.gp.cong.logisoft.beans;

import java.io.Serializable;
import java.math.BigDecimal;

public class LCLCorrectionChargeBean implements Serializable {

    private Integer chargeId;
    private String chargeCode;
    private String chargeDescriptions;
    private String billToPartyLabel;
    private BigDecimal oldAmount;
    private BigDecimal newAmount;
    private BigDecimal differenceAmount;
    private boolean delete;
    private Long correctionChargeId;
    private Long lclBookingAcId;
    private boolean printOnBl;
    private boolean bundleIntoOf;
    private String ratePerUnitUom;
    private String oldBillToCode;
    private String customerName;
    private boolean manualcharge;

    public BigDecimal getDifferenceAmount() {
        return differenceAmount;
    }

    public void setDifferenceAmount(BigDecimal differenceAmount) {
        this.differenceAmount = differenceAmount;
    }

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

    public String getChargeDescriptions() {
        return chargeDescriptions;
    }

    public void setChargeDescriptions(String chargeDescriptions) {
        this.chargeDescriptions = chargeDescriptions;
    }

    public BigDecimal getNewAmount() {
        return newAmount;
    }

    public void setNewAmount(BigDecimal newAmount) {
        this.newAmount = newAmount;
    }

    public BigDecimal getOldAmount() {
        return oldAmount;
    }

    public void setOldAmount(BigDecimal oldAmount) {
        this.oldAmount = oldAmount;
    }

    public Integer getChargeId() {
        return chargeId;
    }

    public void setChargeId(Integer chargeId) {
        this.chargeId = chargeId;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public Long getCorrectionChargeId() {
        return correctionChargeId;
    }

    public void setCorrectionChargeId(Long correctionChargeId) {
        this.correctionChargeId = correctionChargeId;
    }

    public String getBillToPartyLabel() {
        return billToPartyLabel;
    }

    public void setBillToPartyLabel(String billToPartyLabel) {
        this.billToPartyLabel = billToPartyLabel;
    }

    public Long getLclBookingAcId() {
        return lclBookingAcId;
    }

    public void setLclBookingAcId(Long lclBookingAcId) {
        this.lclBookingAcId = lclBookingAcId;
    }

    public boolean getBundleIntoOf() {
        return bundleIntoOf;
    }

    public void setBundleIntoOf(boolean bundleIntoOf) {
        this.bundleIntoOf = bundleIntoOf;
    }

    public boolean getPrintOnBl() {
        return printOnBl;
    }

    public void setPrintOnBl(boolean printOnBl) {
        this.printOnBl = printOnBl;
    }

    public String getRatePerUnitUom() {
        return ratePerUnitUom;
    }

    public void setRatePerUnitUom(String ratePerUnitUom) {
        this.ratePerUnitUom = ratePerUnitUom;
    }

    public String getOldBillToCode() {
        return oldBillToCode;
    }

    public void setOldBillToCode(String oldBillToCode) {
        this.oldBillToCode = oldBillToCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public boolean isManualcharge() {
        return manualcharge;
    }

    public void setManualcharge(boolean manualcharge) {
        this.manualcharge = manualcharge;
    }
}
