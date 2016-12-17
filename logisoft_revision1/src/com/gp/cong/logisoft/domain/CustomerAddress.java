package com.gp.cong.logisoft.domain;

import com.gp.cong.logisoft.struts.form.TradingPartnerForm;
import java.io.Serializable;

public class CustomerAddress implements Serializable {

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
	private String unLocCode;
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
	private String password;
	private String subType;
	private String tempTerm;
	private String notifyParty;
	private boolean checkAddress;
        private String updateBy;
        
	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public CustomerAddress() {
	}

	public CustomerAddress(String accountNo, Integer id, String checkbox, String coName, Integer index, String address1, String address2, Integer country, String city2, String state, String phone, String contactName, String zip, String fax, String email1, String email2, String accountName, GenericCode cuntry, UnLocation city1, String extension, String portName, String schNum, String shedulenumber, String primary, String acctname, String masteracctno, String accounttype, String type) {
		this.accountNo = accountNo;
		this.id = id;
		this.checkbox = checkbox;
		this.coName = coName;
		this.index = index;
		this.address1 = address1;
		this.address2 = address2;
		this.country = country;
		this.city2 = city2;
		this.state = state;
		this.phone = phone;
		this.contactName = contactName;
		this.zip = zip;
		this.fax = fax;
		this.email1 = email1;
		this.email2 = email2;
		this.accountName = accountName;
		this.cuntry = cuntry;
		this.city1 = city1;
		this.extension = extension;
		this.PortName = portName;
		this.schNum = schNum;
		this.Shedulenumber = shedulenumber;
		this.primary = primary;
		this.acctname = acctname;
		this.masteracctno = masteracctno;
		this.accounttype = accounttype;
		this.type = type;
	}

	public CustomerAddress(TradingPartnerForm tradingPartnerForm) {
		this.accountNo = (null != tradingPartnerForm.getAccountNo()) ? tradingPartnerForm.getAccountNo().toUpperCase() : tradingPartnerForm.getAccountNo();
		this.coName = (null != tradingPartnerForm.getCoName()) ? tradingPartnerForm.getCoName().toUpperCase() : tradingPartnerForm.getCoName();
		this.address1 = (null != tradingPartnerForm.getAddress1()) ? tradingPartnerForm.getAddress1().toUpperCase() : tradingPartnerForm.getAddress1();
		this.address2 = (null != tradingPartnerForm.getAddress2()) ? tradingPartnerForm.getAddress2().toUpperCase() : tradingPartnerForm.getAddress2();
		this.city2 = (null != tradingPartnerForm.getCity1()) ? tradingPartnerForm.getCity1().toUpperCase() : tradingPartnerForm.getCity1();
		this.state = (null != tradingPartnerForm.getState()) ? tradingPartnerForm.getState().toUpperCase() : tradingPartnerForm.getState();
		this.phone = (null != tradingPartnerForm.getPhone()) ? tradingPartnerForm.getPhone().toUpperCase() : tradingPartnerForm.getPhone();
		this.contactName = (null != tradingPartnerForm.getContactName()) ? tradingPartnerForm.getContactName().toUpperCase() : tradingPartnerForm.getContactName();
		this.zip = (null != tradingPartnerForm.getZip()) ? tradingPartnerForm.getZip().toUpperCase() : tradingPartnerForm.getZip();
		this.fax = (null != tradingPartnerForm.getFax()) ? tradingPartnerForm.getFax().toUpperCase() : tradingPartnerForm.getFax();
		this.email1 = (null != tradingPartnerForm.getEmail1()) ? tradingPartnerForm.getEmail1().toUpperCase() : tradingPartnerForm.getEmail1();
		this.email2 = (null != tradingPartnerForm.getEmail2()) ? tradingPartnerForm.getEmail2().toUpperCase() : tradingPartnerForm.getEmail1();
		this.extension = (null != tradingPartnerForm.getExtension()) ? tradingPartnerForm.getExtension().toUpperCase() : tradingPartnerForm.getExtension();
		this.PortName = (null != tradingPartnerForm.getPortName()) ? tradingPartnerForm.getPortName().toUpperCase() : tradingPartnerForm.getPortName();
		this.schNum = (null != tradingPartnerForm.getSchNum()) ? tradingPartnerForm.getSchNum().toUpperCase() : tradingPartnerForm.getSchNum();
		this.primary = (null != tradingPartnerForm.getPrimary()) ? tradingPartnerForm.getPrimary().toUpperCase() : tradingPartnerForm.getPrimary();
		this.acctname = (null != tradingPartnerForm.getAccountName()) ? tradingPartnerForm.getAccountName().toUpperCase() : tradingPartnerForm.getAccountName();
		this.masteracctno = (null != tradingPartnerForm.getMaster()) ? tradingPartnerForm.getMaster().toUpperCase() : tradingPartnerForm.getMaster();
		this.accounttype = (null != tradingPartnerForm.getAccountType()) ? tradingPartnerForm.getAccountType().toUpperCase() : tradingPartnerForm.getAccountType();
		this.type = (null != tradingPartnerForm.getType()) ? tradingPartnerForm.getType().toUpperCase() : tradingPartnerForm.getType();
		this.notifyParty = (null != tradingPartnerForm.getNotifyParty()) ? tradingPartnerForm.getNotifyParty().toUpperCase() : tradingPartnerForm.getNotifyParty();
		this.unLocCode = (null != tradingPartnerForm.getUnLocCode()) ? tradingPartnerForm.getUnLocCode().toUpperCase() : tradingPartnerForm.getUnLocCode();
		this.checkAddress = tradingPartnerForm.isCheckAddress();

	}

	public TradingPartnerForm setFormValue(CustomerAddress customerAddress, TradingPartnerForm tradingPartnerForm) {
		tradingPartnerForm.setCompanyName(customerAddress.getCoName());
		tradingPartnerForm.setAddress1(customerAddress.getAddress1());
		if (customerAddress.getCity1() != null) {
			tradingPartnerForm.setCity(customerAddress.getCity1().getUnLocationName());
		}
		tradingPartnerForm.setState(customerAddress.getState());
		tradingPartnerForm.setZip(customerAddress.getZip());
		if (customerAddress.getCuntry() != null) {
			tradingPartnerForm.setCountry(customerAddress.getCuntry().getCodedesc());
		}
		tradingPartnerForm.setPhone(customerAddress.getPhone());
		tradingPartnerForm.setFax(customerAddress.getFax());
		tradingPartnerForm.setEmail(customerAddress.getEmail1());
		tradingPartnerForm.setPassword(customerAddress.getPassword());
		tradingPartnerForm.setUnLocCode(customerAddress.getUnLocCode());
		tradingPartnerForm.setCheckAddress(customerAddress.isCheckAddress());
		return tradingPartnerForm;
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

	public String getAccounttype() {
		return accounttype;
	}

	public void setAccounttype(String accounttype) {
		this.accounttype = accounttype;
	}

	public String getAcctname() {
		return acctname;
	}

	public void setAcctname(String acctname) {
		this.acctname = acctname;
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

	public String getCheckbox() {
		return checkbox;
	}

	public void setCheckbox(String checkbox) {
		this.checkbox = checkbox;
	}

	public UnLocation getCity1() {
		return city1;
	}

	public void setCity1(UnLocation city1) {
		this.city1 = city1;
	}

	public String getCity2() {
		return city2;
	}

	public void setCity2(String city2) {
		this.city2 = city2;
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

	public GenericCode getCuntry() {
		return cuntry;
	}

	public void setCuntry(GenericCode cuntry) {
		this.cuntry = cuntry;
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

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getMasteracctno() {
		return masteracctno;
	}

	public void setMasteracctno(String masteracctno) {
		this.masteracctno = masteracctno;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPortName() {
		return PortName;
	}

	public void setPortName(String portName) {
		PortName = portName;
	}

	public String getPrimary() {

		return primary;
	}

	public void setPrimary(String primary) {

		this.primary = primary;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTempTerm() {
		return tempTerm;
	}

	public void setTempTerm(String tempTerm) {
		this.tempTerm = tempTerm;
	}

	public String getNotifyParty() {
		return notifyParty;
	}

	public void setNotifyParty(String notifyParty) {
		this.notifyParty = notifyParty;
	}

	public String getUnLocCode() {
		return unLocCode;
	}

	public void setUnLocCode(String unLocCode) {
		this.unLocCode = unLocCode;
	}

	public boolean isCheckAddress() {
		return checkAddress;
	}

	public void setCheckAddress(boolean checkAddress) {
		this.checkAddress = checkAddress;
	}

        public String getUpdateBy() {
            return updateBy;
        }

        public void setUpdateBy(String updateBy) {
            this.updateBy = updateBy;
        }
        
}
