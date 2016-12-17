/**
 * 
 */
package com.gp.cong.logisoft.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author Yogesh
 *
 */

public class AirWeightRangesRates implements Auditable,Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer airRatesId;
	private GenericCode weightRange;
	private Double generalRate;
	private Double generalMinAmt;
	private Double expressRate;
	private Double expressMinAmt;
	private Double deferredRate;
	private Double deferredMinAmt;
	private Date changedDate;  
	private String changedDateOn;
	private String whoChanged;
	private Integer index;
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
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
	
	
	public GenericCode getWeightRange() {
		return weightRange;
	}
	public void setWeightRange(GenericCode weightRange) {
		this.weightRange = weightRange;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getAirRatesId() {
		return airRatesId;
	}
	public void setAirRatesId(Integer airRatesId) {
		this.airRatesId = airRatesId;
	}
	public String getWhoChanged() {
		return whoChanged;
	}
	public void setWhoChanged(String whoChanged) {
		this.whoChanged = whoChanged;
	}
	public String getChangedDateOn() {
		if(changedDateOn!=null )
		{
			SimpleDateFormat sdf=new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
			return sdf.format(changedDateOn);
		}
		
		return changedDateOn;
	}
	public void setChangedDateOn(String changedDateOn) {
		this.changedDateOn = changedDateOn;
	}
	public AuditInfo getAuditInfo() {
		// TODO Auto-generated method stub
		return null;
	}

}
