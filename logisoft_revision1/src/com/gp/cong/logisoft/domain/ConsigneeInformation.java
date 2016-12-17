package com.gp.cong.logisoft.domain;

import java.io.Serializable;

public class ConsigneeInformation implements Auditable,Serializable {
	/**
	 * @author Rohith
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String consigNo;
	private Integer taxExempt;
	private String fedId;
	private GenericCode commodityId;
	private Integer portId;
	private Integer maxDaysBetVisits;
	private String goalAcct;
	private String salesPCode;
	private UnLocation notifyCity;
	private String notifyState;
	private GenericCode notifyCountry;
	private String zipCode;
	private String insurance;
	private String notifyParty;
	private String userName;
	private String password;
	private String specialRemarks;
	/**
	 * @return the commodityId
	 */
	
	/**
	 * @return the consigNo
	 */
	public String getConsigNo() {
		return consigNo;
	}
	/**
	 * @param consigNo the consigNo to set
	 */
	public void setConsigNo(String consigNo) {
		this.consigNo = consigNo;
	}
	/**
	 * @return the fedId
	 */
	public String getFedId() {
		return fedId;
	}
	/**
	 * @param fedId the fedId to set
	 */
	public void setFedId(String fedId) {
		this.fedId = fedId;
	}
	/**
	 * @return the goalAcct
	 */
	public String getGoalAcct() {
		return goalAcct;
	}
	/**
	 * @param goalAcct the goalAcct to set
	 */
	public void setGoalAcct(String goalAcct) {
		this.goalAcct = goalAcct;
	}
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the insurance
	 */
	public String getInsurance() {
		return insurance;
	}
	/**
	 * @param insurance the insurance to set
	 */
	public void setInsurance(String insurance) {
		this.insurance = insurance;
	}
	/**
	 * @return the maxDaysBetVisits
	 */
	public Integer getMaxDaysBetVisits() {
		return maxDaysBetVisits;
	}
	/**
	 * @param maxDaysBetVisits the maxDaysBetVisits to set
	 */
	public void setMaxDaysBetVisits(Integer maxDaysBetVisits) {
		this.maxDaysBetVisits = maxDaysBetVisits;
	}
	
	
	/**
	 * @return the notifyCity
	 */
	public UnLocation getNotifyCity() {
		return notifyCity;
	}
	/**
	 * @param notifyCity the notifyCity to set
	 */
	public void setNotifyCity(UnLocation notifyCity) {
		this.notifyCity = notifyCity;
	}
	/**
	 * @return the notifyCountry
	 */
	public GenericCode getNotifyCountry() {
		return notifyCountry;
	}
	/**
	 * @param notifyCountry the notifyCountry to set
	 */
	public void setNotifyCountry(GenericCode notifyCountry) {
		this.notifyCountry = notifyCountry;
	}
	/**
	 * @return the notifyParty
	 */
	public String getNotifyParty() {
		return notifyParty;
	}
	/**
	 * @param notifyParty the notifyParty to set
	 */
	public void setNotifyParty(String notifyParty) {
		this.notifyParty = notifyParty;
	}
	/**
	 * @return the notifyState
	 */
	public String getNotifyState() {
		return notifyState;
	}
	/**
	 * @param notifyState the notifyState to set
	 */
	public void setNotifyState(String notifyState) {
		this.notifyState = notifyState;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * @return the commodityId
	 */
	public GenericCode getCommodityId() {
		return commodityId;
	}
	/**
	 * @param commodityId the commodityId to set
	 */
	public void setCommodityId(GenericCode commodityId) {
		this.commodityId = commodityId;
	}
	
	public Integer getPortId() {
		return portId;
	}
	public void setPortId(Integer portId) {
		this.portId = portId;
	}
	/**
	 * @return the salesPCode
	 */
	public String getSalesPCode() {
		return salesPCode;
	}
	/**
	 * @param salesPCode the salesPCode to set
	 */
	public void setSalesPCode(String salesPCode) {
		this.salesPCode = salesPCode;
	}
	/**
	 * @return the specialRemarks
	 */
	public String getSpecialRemarks() {
		return specialRemarks;
	}
	/**
	 * @param specialRemarks the specialRemarks to set
	 */
	public void setSpecialRemarks(String specialRemarks) {
		this.specialRemarks = specialRemarks;
	}
	/**
	 * @return the taxExempt
	 */
	public Integer getTaxExempt() {
		return taxExempt;
	}
	/**
	 * @param taxExempt the taxExempt to set
	 */
	public void setTaxExempt(Integer taxExempt) {
		this.taxExempt = taxExempt;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the zipCode
	 */
	public String getZipCode() {
		return zipCode;
	}
	/**
	 * @param zipCode the zipCode to set
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public AuditInfo getAuditInfo() {
		// TODO Auto-generated method stub
		return null;
	}
	public Object getId1() {
		// TODO Auto-generated method stub
	return this.getConsigNo();
	}
	
	
}
