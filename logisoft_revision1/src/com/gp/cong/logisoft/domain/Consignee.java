package com.gp.cong.logisoft.domain;

import java.io.Serializable;
import java.util.Set;

public class Consignee implements Auditable,Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String consigNo;
	private String consigName;
	private String coName;
	private String address1;
	private String address2;
	private String state;
	private String phone;
	private String zip;
	private String fax;
	private String email1;
	private String email2;
	private GenericCode country;
	private UnLocation city;
	private Set consigneeInformationSet;
	private Set consigneeContactSet;
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public UnLocation getCity() {
		return city;
	}
	public void setCity(UnLocation city) {
		this.city = city;
	}
	public String getCoName() {
		return coName;
	}
	public void setCoName(String coName) {
		this.coName = coName;
	}
	public String getConsigName() {
		return consigName;
	}
	public void setConsigName(String consigName) {
		this.consigName = consigName;
	}
	public String getConsigNo() {
		return consigNo;
	}
	public void setConsigNo(String consigNo) {
		this.consigNo = consigNo;
	}
	
	public GenericCode getCountry() {
		return country;
	}
	public void setCountry(GenericCode country) {
		this.country = country;
	}
	public String getEmail1() {
		return email1;
	}
	public void setEmail1(String email1) {
		this.email1 = email1;
	}
	public String getEmail2() {
		return email2;
	}
	public void setEmail2(String email2) {
		this.email2 = email2;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	/**
	 * @return the consigneeContact
	 */
	/**
	 * @return the consigneeContactSet
	 */
	public Set getConsigneeContactSet() {
		return consigneeContactSet;
	}
	/**
	 * @param consigneeContactSet the consigneeContactSet to set
	 */
	public void setConsigneeContactSet(Set consigneeContactSet) {
		this.consigneeContactSet = consigneeContactSet;
	}
	/**
	 * @return the consigneeInformationSet
	 */
	public Set getConsigneeInformationSet() {
		return consigneeInformationSet;
	}
	/**
	 * @param consigneeInformationSet the consigneeInformationSet to set
	 */
	public void setConsigneeInformationSet(Set consigneeInformationSet) {
		this.consigneeInformationSet = consigneeInformationSet;
	}
	public AuditInfo getAuditInfo() {
		// TODO Auto-generated method stub
		return null;
	}
	public Object getId() {
		// TODO Auto-generated method stub
	return this.getConsigNo();
	}
	
	
}
