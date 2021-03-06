package com.gp.cvst.logisoft.domain;

import com.gp.cong.common.CommonUtils;
import java.util.Date;

import com.gp.cong.logisoft.domain.GenericCode;

/**
 * BookingfclUnits generated by MyEclipse - Hibernate Tools
 */
public class BookingfclUnits implements java.io.Serializable {

    // Fields
    private Integer id;
    private Double rateChangeAmount;
    private Double rateChangeMarkup;
    private String bookingNumber;
    private GenericCode unitType;
    private String numbers;
    private Double rates;
    private Double futureRate;
    private Date efectiveDate;
    private Integer qouteId;
    private String chgCode;
    private Double amount;
    private Double markUp;
    private Double spotRateMarkUp;
    private Integer commcode;
    private String costType;
    private String include;
    private String unitName;
    private String print;
    private Double retail;
    private Double ctc;
    private Double ftf;
    private Double minimum;
    private String accountName;
    private String accountNo;
    private String manualCharges;
    private Double sellRate;
    private Double buyRate;
    private Double profit;
    private GenericCode chargeCode;
    private GenericCode costtype;
    private GenericCode currency1;
    private String ratesdescription;
    private String ChargeCodeDesc;
    private String Currency;
    private String chargeFlag;
    private String newFlag;
    private String approveBl;
    private String invoiceNumber;
    //new fields on  29/7/09
    private String comment;
    private Double adjustment;
    private String adjustmentChargeComments;
    private Date updateOn;
    private String updateBy;
    private String vendorCheckBox;
    private String specialEquipmentUnit;
    private String specialEquipment;
    private String outOfGauge;
    private String standardCharge;
    private String outOfGaugeComment;
    private Double spotRateAmt;
    private String spotRateChk;
    private String standardChk;
    private Boolean cfclFlag =false;

    public String getVendorCheckBox() {
	return vendorCheckBox;
    }

    public void setVendorCheckBox(String vendorCheckBox) {
	this.vendorCheckBox = vendorCheckBox;
    }

    public String getOutOfGaugeComment() {
	return outOfGaugeComment;
    }

    public void setOutOfGaugeComment(String outOfGaugeComment) {
	this.outOfGaugeComment = outOfGaugeComment;
    }

    //private String unitType;
    // Constructors
    public String getApproveBl() {
	return approveBl;
    }

    public void setApproveBl(String approveBl) {
	this.approveBl = approveBl;
    }

    public String getNewFlag() {
	return newFlag;
    }

    public void setNewFlag(String newFlag) {
	this.newFlag = newFlag;
    }

    public String getChargeFlag() {
	return chargeFlag;
    }

    public void setChargeFlag(String chargeFlag) {
	this.chargeFlag = chargeFlag;
    }

    /*public String getUnitType() {
     return unitType;
     }

     public void setUnitType(String unitType) {
     this.unitType = unitType;
     }
     */
    /**
     * default constructor
     */
    public BookingfclUnits() {
    }

    /**
     * minimal constructor
     */
    public BookingfclUnits(String bookingNumber) {
	this.bookingNumber = bookingNumber;
    }

    /**
     * full constructor
     */
    // Property accessors
    public Integer getId() {
	return this.id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getBookingNumber() {
	return this.bookingNumber;
    }

    public void setBookingNumber(String bookingNumber) {
	this.bookingNumber = bookingNumber;
    }

    public String getNumbers() {
	return this.numbers;
    }

    public void setNumbers(String numbers) {
	this.numbers = numbers;
    }

    public Double getRates() {
	return rates;
    }

    public void setRates(Double rates) {
	this.rates = rates;
    }

    public String getRatesdescription() {
	return this.ratesdescription;
    }

    public void setRatesdescription(String ratesdescription) {
	this.ratesdescription = ratesdescription;
    }

    public GenericCode getUnitType() {
	return unitType;
    }

    public void setUnitType(GenericCode unitType) {
	this.unitType = unitType;
    }

    public Double getAmount() {
	return null != amount ? amount : 0d;
    }

    public void setAmount(Double amount) {
	this.amount = amount;
    }

    public String getChgCode() {
	return chgCode;
    }

    public void setChgCode(String chgCode) {
	this.chgCode = chgCode;
    }

    public Integer getCommcode() {
	return commcode;
    }

    public void setCommcode(Integer commcode) {
	this.commcode = commcode;
    }

    public String getCostType() {
	return costType;
    }

    public void setCostType(String costType) {
	this.costType = costType;
    }

    public Double getCtc() {
	return ctc;
    }

    public void setCtc(Double ctc) {
	this.ctc = ctc;
    }

    public Double getFtf() {
	return ftf;
    }

    public void setFtf(Double ftf) {
	this.ftf = ftf;
    }

    public String getInclude() {
	return include;
    }

    public void setInclude(String include) {
	this.include = include;
    }

    public Double getMarkUp() {
	return markUp;
    }

    public void setMarkUp(Double markUp) {
	this.markUp = markUp;
    }

    public Double getMinimum() {
	return minimum;
    }

    public void setMinimum(Double minimum) {
	this.minimum = minimum;
    }

    public String getPrint() {
	return print;
    }

    public void setPrint(String print) {
	this.print = print;
    }

    public Integer getQouteId() {
	return qouteId;
    }

    public void setQouteId(Integer qouteId) {
	this.qouteId = qouteId;
    }

    public Double getRetail() {
	return retail;
    }

    public void setRetail(Double retail) {
	this.retail = retail;
    }

    public String getChargeCodeDesc() {
	return ChargeCodeDesc;
    }

    public void setChargeCodeDesc(String chargeCodeDesc) {
	ChargeCodeDesc = chargeCodeDesc;
    }

    public String getCurrency() {
	return Currency;
    }

    public void setCurrency(String currency) {
	Currency = currency;
    }

    public Double getFutureRate() {
	return futureRate;
    }

    public void setFutureRate(Double futureRate) {
	this.futureRate = futureRate;
    }

    public Date getEfectiveDate() {
	return efectiveDate;
    }

    public void setEfectiveDate(Date efectiveDate) {
	this.efectiveDate = efectiveDate;
    }

    public Double getSellRate() {
	return null != sellRate ? sellRate : 0d;
    }

    public void setSellRate(Double sellRate) {
	this.sellRate = sellRate;
    }

    public Double getBuyRate() {
	return null!=buyRate?buyRate:0d;
    }

    public void setBuyRate(Double buyRate) {
	this.buyRate = buyRate;
    }

    public Double getProfit() {
	return profit;
    }

    public void setProfit(Double profit) {
	this.profit = profit;
    }

    public GenericCode getChargeCode() {
	return chargeCode;
    }

    public void setChargeCode(GenericCode chargeCode) {
	this.chargeCode = chargeCode;
    }

    public GenericCode getCosttype() {
	return costtype;
    }

    public void setCosttype(GenericCode costtype) {
	this.costtype = costtype;
    }

    public GenericCode getCurrency1() {
	return currency1;
    }

    public void setCurrency1(GenericCode currency1) {
	this.currency1 = currency1;
    }

    public String getManualCharges() {
	return manualCharges;
    }

    public void setManualCharges(String manualCharges) {
	this.manualCharges = manualCharges;
    }

    public String getAccountName() {
	return accountName;
    }

    public void setAccountName(String accountName) {
	this.accountName = accountName;
    }

    public String getAccountNo() {
	return accountNo;
    }

    public void setAccountNo(String accountNo) {
	this.accountNo = accountNo;
    }

    public String getUnitName() {
	return unitName;
    }

    public void setUnitName(String unitName) {
	this.unitName = unitName;
    }

    public String getComment() {
	return comment;
    }

    public void setComment(String comment) {
	this.comment = comment;
    }

    public Double getAdjustment() {
	if (CommonUtils.isEqualIgnoreCase(ChargeCodeDesc, "INLAND") || null == adjustment) {
	    return 0d;
	} else {
	    return adjustment;
	}
    }

    public void setAdjustment(Double adjustment) {
	if (CommonUtils.isEqualIgnoreCase(ChargeCodeDesc, "INLAND")) {
	    this.adjustment = 0d;
	} else {
	    this.adjustment = adjustment;
	}
    }

    public Double getRateChangeAmount() {
	return rateChangeAmount;
    }

    public void setRateChangeAmount(Double rateChangeAmount) {
	this.rateChangeAmount = rateChangeAmount;
    }

    public Double getRateChangeMarkup() {
	return rateChangeMarkup;
    }

    public void setRateChangeMarkup(Double rateChangeMarkup) {
	this.rateChangeMarkup = rateChangeMarkup;
    }

    public String getInvoiceNumber() {
	return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
	this.invoiceNumber = invoiceNumber;
    }

    public String getUpdateBy() {
	return updateBy;
    }

    public void setUpdateBy(String updateBy) {
	this.updateBy = updateBy;
    }

    public Date getUpdateOn() {
	return updateOn;
    }

    public void setUpdateOn(Date updateOn) {
	this.updateOn = updateOn;
    }

    public String getOutOfGauge() {
	return outOfGauge;
    }

    public void setOutOfGauge(String outOfGauge) {
	this.outOfGauge = outOfGauge;
    }

    public String getSpecialEquipment() {
	return specialEquipment;
    }

    public void setSpecialEquipment(String specialEquipment) {
	this.specialEquipment = specialEquipment;
    }

    public String getSpecialEquipmentUnit() {
	return specialEquipmentUnit;
    }

    public void setSpecialEquipmentUnit(String specialEquipmentUnit) {
	this.specialEquipmentUnit = specialEquipmentUnit;
    }

    public String getStandardCharge() {
	return standardCharge;
    }

    public void setStandardCharge(String standardCharge) {
	this.standardCharge = standardCharge;
    }

    public String getAdjustmentChargeComments() {
	return adjustmentChargeComments;
    }

    public void setAdjustmentChargeComments(String adjustmentChargeComments) {
	this.adjustmentChargeComments = adjustmentChargeComments;
    }

    public Double getSpotRateAmt() {
        return spotRateAmt;
    }

    public void setSpotRateAmt(Double spotRateAmt) {
        this.spotRateAmt = spotRateAmt;
    }

    public String getSpotRateChk() {
        return null!=spotRateChk?spotRateChk:"off";
    }

    public void setSpotRateChk(String spotRateChk) {
        this.spotRateChk = null!=spotRateChk?spotRateChk:"off";
    }

    public Double getSpotRateMarkUp() {
        return spotRateMarkUp;
    }

    public void setSpotRateMarkUp(Double spotRateMarkUp) {
        this.spotRateMarkUp = spotRateMarkUp;
    }

    public String getStandardChk() {
        return null!=standardChk?standardChk:"off";
    }

    public void setStandardChk(String standardChk) {
        this.standardChk = null!=standardChk?standardChk:"off";
    }

    public Boolean getCfclFlag() {
        return cfclFlag;
    }

    public void setCfclFlag(Boolean cfclFlag) {
        this.cfclFlag = cfclFlag;
    }
}
