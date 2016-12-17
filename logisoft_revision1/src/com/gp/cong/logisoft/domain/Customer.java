package com.gp.cong.logisoft.domain;

import java.io.Serializable;

public class Customer implements Auditable,Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String accountNo;
	private Integer id;
	private String checkbox;
	private String coName;
	private Integer index;
	private String address1;
	private String address2;
	private Integer country;
	private String city2;
	private String state;
	private String phone;
	private String contactName;
	private String zip;
	private String fax;
	private String email1;
	private String email2;
	private String accountName;
	private GenericCode cuntry;
	private UnLocation city1;
	private String extension;
	private String PortName;
	private String schNum;
	private String Shedulenumber;
	private String primary;
	private String acctname;
	private String masteracctno;
	private String accounttype;
	private String type;
	
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPrimary() {
		return primary;
	}
	public void setPrimary(String primary) {
		this.primary = primary;
	}
	public UnLocation getCity1() {
		return city1;
	}
	public void setCity1(UnLocation city1) {
		this.city1 = city1;
		
	}
	public GenericCode getCuntry() {
		return cuntry;
	}
	public void setCuntry(GenericCode cuntry) {
		this.cuntry = cuntry;
	}
	
	
	
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
	
	public String getCoName() {
		return coName;
	}
	public void setCoName(String coName) {
		this.coName = coName;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	
	
	
	public Integer getCountry() {
		return country;
	}
	public void setCountry(Integer country) {
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
	
	public AuditInfo getAuditInfo() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	public String getCity2() {
		return city2;
	}
	public void setCity2(String city2) {
		this.city2 = city2;
	}

	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getCheckbox() {
		return checkbox;
	}
	public void setCheckbox(String checkbox) {
		this.checkbox = checkbox;
	}
	
	
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId()
	{
		return id;
	}
	public String getPortName() {
		return PortName;
	}
	public void setPortName(String portName) {
		PortName = portName;
	}
	public String getSchNum() {
		return schNum;
	}
	public void setSchNum(String schNum) {
		this.schNum = schNum;
	}
	
	public String getShedulenumber() {
		return Shedulenumber;
	}
	public void setShedulenumber(String shedulenumber) {
		Shedulenumber = shedulenumber;
	}
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}
	public String getAcctname() {
		return acctname;
	}
	public void setAcctname(String acctname) {
		this.acctname = acctname;
	}
	public String getMasteracctno() {
		return masteracctno;
	}
	public void setMasteracctno(String masteracctno) {
		this.masteracctno = masteracctno;
	}
	public String getAccounttype() {
		return accounttype;
	}
	public void setAccounttype(String accounttype) {
		this.accounttype = accounttype;
	}
	
	
	
	
}
