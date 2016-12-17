/**
 * 
 */
package com.gp.cong.logisoft.beans;

import java.io.Serializable;

/**
 * @author Yogesh
 *
 */
public class SearchCarriersBean implements Serializable {
	private String carriercode;
	private String carriername;
	private String carriertype;
	private String SCAC;
	private String buttonValue;
	private String eqptType;
	private String spclRate;
	private String commision;
	private String match;
	private String hazardous;
	private String masterAwb2;
	private String masterAwb3;
	private String txtcal;
	private String claimed;
	private String ediCarriers;
	
	public String getEdiCarriers() {
		return ediCarriers;
	}
	public void setEdiCarriers(String ediCarriers) {
		this.ediCarriers = ediCarriers;
	}
	public String getClaimed() {
		return claimed;
	}
	public void setClaimed(String claimed) {
		this.claimed = claimed;
	}
	public String getMasterAwb2() {
		return masterAwb2;
	}
	public void setMasterAwb2(String masterAwb2) {
		this.masterAwb2 = masterAwb2;
	}
	public String getMasterAwb3() {
		return masterAwb3;
	}
	public void setMasterAwb3(String masterAwb3) {
		this.masterAwb3 = masterAwb3;
	}
	public String getTxtcal() {
		return txtcal;
	}
	public void setTxtcal(String txtcal) {
		this.txtcal = txtcal;
	}
	public String getHazardous() {
		return hazardous;
	}
	public void setHazardous(String hazardous) {
		this.hazardous = hazardous;
	}
	public String getButtonValue() {
		return buttonValue;
	}
	public void setButtonValue(String buttonValue) {
		this.buttonValue = buttonValue;
	}
	public String getCarriercode() {
		return carriercode;
	}
	public void setCarriercode(String carriercode) {
		this.carriercode = carriercode;
	}
	public String getCarriername() {
		return carriername;
	}
	public void setCarriername(String carriername) {
		this.carriername = carriername;
	}
	public String getCarriertype() {
		return carriertype;
	}
	public void setCarriertype(String carriertype) {
		this.carriertype = carriertype;
	}
	public String getSCAC() {
		return SCAC;
	}
	public void setSCAC(String scac) {
		SCAC = scac;
	}
	public String getEqptType() {
		return eqptType;
	}
	public void setEqptType(String eqptType) {
		this.eqptType = eqptType;
	}
	public String getSpclRate() {
		return spclRate;
	}
	public void setSpclRate(String spclRate) {
		this.spclRate = spclRate;
	}
	public String getCommision() {
		return commision;
	}
	public void setCommision(String commision) {
		this.commision = commision;
	}
	/**
	 * @return the match
	 */
	public String getMatch() {
		return match;
	}
	/**
	 * @param match the match to set
	 */
	public void setMatch(String match) {
		this.match = match;
	}
	

}
