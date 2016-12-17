/**
 *
 */
package com.gp.cong.logisoft.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Yogesh
 *
 */
public class RetailCommodityCharges implements Auditable, Serializable {

    private static final long serialVersionUID = 1L;
    private Integer retailCmdId;
    private Integer retailRatesId;
    private GenericCode chargeCode;
    private GenericCode chargeType;
    private String standard;
    private Double amtPerCft;
    private Double amtPer100lbs;
    private Double amtPerCbm;
    private Double amtPer1000kg;
    private Double amount;
    private Double percentage;
    private Double minAmt;
    private Date effectiveDate;
    private Date changedDate;
    private String whoChanged;
    private Double insuranceRate;
    private Double insuranceAmt;
    private String asFreightedCheckBox;
    private Integer index;
    private String flag;
    private String exclude;

    public String getExclude() {
        return exclude;
    }

    public void setExclude(String exclude) {
        this.exclude = exclude;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getAsFreightedCheckBox() {
        return asFreightedCheckBox;
    }

    public void setAsFreightedCheckBox(String asFreightedCheckBox) {
        this.asFreightedCheckBox = asFreightedCheckBox;
    }

    public Double getInsuranceAmt() {
        return insuranceAmt;
    }

    public void setInsuranceAmt(Double insuranceAmt) {
        this.insuranceAmt = insuranceAmt;
    }

    public Double getInsuranceRate() {
        return insuranceRate;
    }

    public void setInsuranceRate(Double insuranceRate) {
        this.insuranceRate = insuranceRate;
    }

    public Integer getRetailCmdId() {
        return retailCmdId;
    }

    public void setRetailCmdId(Integer retailCmdId) {
        this.retailCmdId = retailCmdId;
    }

    public Integer getRetailRatesId() {
        return retailRatesId;
    }

    public void setRetailRatesId(Integer retailRatesId) {
        this.retailRatesId = retailRatesId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getAmtPer1000kg() {
        return amtPer1000kg;
    }

    public void setAmtPer1000kg(Double amtPer1000kg) {
        this.amtPer1000kg = amtPer1000kg;
    }

    public Double getAmtPer100lbs() {
        return amtPer100lbs;
    }

    public void setAmtPer100lbs(Double amtPer100lbs) {
        this.amtPer100lbs = amtPer100lbs;
    }

    public Double getAmtPerCbm() {
        return amtPerCbm;
    }

    public void setAmtPerCbm(Double amtPerCbm) {
        this.amtPerCbm = amtPerCbm;
    }

    public Double getAmtPerCft() {
        return amtPerCft;
    }

    public void setAmtPerCft(Double amtPerCft) {
        this.amtPerCft = amtPerCft;
    }

    public Date getChangedDate() {
        return changedDate;
    }

    public void setChangedDate(Date changedDate) {
        this.changedDate = changedDate;
    }

    public GenericCode getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(GenericCode chargeCode) {
        this.chargeCode = chargeCode;
    }

    public GenericCode getChargeType() {
        return chargeType;
    }

    public void setChargeType(GenericCode chargeType) {
        this.chargeType = chargeType;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Double getMinAmt() {
        return minAmt;
    }

    public void setMinAmt(Double minAmt) {
        this.minAmt = minAmt;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getWhoChanged() {
        return whoChanged;
    }

    public void setWhoChanged(String whoChanged) {
        this.whoChanged = whoChanged;
    }

    public AuditInfo getAuditInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getId() {
        // TODO Auto-generated method stub
        return null;
    }
}
