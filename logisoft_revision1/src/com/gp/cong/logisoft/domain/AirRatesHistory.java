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
public class AirRatesHistory implements Auditable,Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer standardId;
	private Integer airRatesId;
	private GenericCode weightRange;
	private Double generalRate;
	private Double generalMinAmt;
	private Double expressRate;
	private Double expressMinAmt;
	private Double deferredRate;
	private Double deferredMinAmt;
	private Date changedDate;  
	private String whoChanged;
	
	
	
	public Integer getAirRatesId() {
		return airRatesId;
	}
	public void setAirRatesId(Integer airRatesId) {
		this.airRatesId = airRatesId;
	}
	public Date getChangedDate() {
		return changedDate;
	}
	public void setChangedDate(Date changedDate) {
		this.changedDate = changedDate;
	}
	
	public Double getDeferredMinAmt() {
		return deferredMinAmt;
	}
	public void setDeferredMinAmt(Double deferredMinAmt) {
		this.deferredMinAmt = deferredMinAmt;
	}
	public Double getDeferredRate() {
		return deferredRate;
	}
	public void setDeferredRate(Double deferredRate) {
		this.deferredRate = deferredRate;
	}
	public Double getExpressMinAmt() {
		return expressMinAmt;
	}
	public void setExpressMinAmt(Double expressMinAmt) {
		this.expressMinAmt = expressMinAmt;
	}
	public Double getExpressRate() {
		return expressRate;
	}
	public void setExpressRate(Double expressRate) {
		this.expressRate = expressRate;
	}
	public Double getGeneralMinAmt() {
		return generalMinAmt;
	}
	public void setGeneralMinAmt(Double generalMinAmt) {
		this.generalMinAmt = generalMinAmt;
	}
	public Double getGeneralRate() {
		return generalRate;
	}
	public void setGeneralRate(Double generalRate) {
		this.generalRate = generalRate;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getStandardId() {
		return standardId;
	}
	public void setStandardId(Integer standardId) {
		this.standardId = standardId;
	}
	public GenericCode getWeightRange() {
		return weightRange;
	}
	public void setWeightRange(GenericCode weightRange) {
		this.weightRange = weightRange;
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
	
}
