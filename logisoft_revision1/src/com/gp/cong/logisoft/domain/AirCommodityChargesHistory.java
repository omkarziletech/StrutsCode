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

public class AirCommodityChargesHistory implements Auditable,Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer airCmdId;
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
	private String asFrfgted;
	private Integer standardId;
	
	
	
		
	
	public Integer getStandardId() {
		return standardId;
	}
	public void setStandardId(Integer standardId) {
		this.standardId = standardId;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAsFrfgted() {
		return asFrfgted;
	}
	public void setAsFrfgted(String asFrfgted) {
		this.asFrfgted = asFrfgted;
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
	public Integer getAirCmdId() {
		return airCmdId;
	}
	public void setAirCmdId(Integer airCmdId) {
		this.airCmdId = airCmdId;
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
		return id;
	}

}
