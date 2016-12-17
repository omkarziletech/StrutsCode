package com.gp.cong.logisoft.domain;

import com.gp.cong.hibernate.Domain;
import com.gp.cong.logisoft.hibernate.dao.CorporateAccount;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.gp.cong.logisoft.struts.form.TradingPartnerForm;
import java.util.Iterator;

public class TradingPartner extends Domain {

    private String accountno;
    private String accountName;
    private String master;
    private String acctType;
    private String AccountPrefix;
    private String Type;
    private Set generalInformation;
    private Set accounting;
    private Set customerContact;
    private Set associateCustomer;
    private Set customerAddressSet;
    private Set vendorset;
    private Set<PaymentMethod> paymentset;
    private String address;
    private String city;
    private String state;
    private String hold;
    private String password;
    private List masterList;
    private String subType;
    private String eciAccountNo;
    private String vendorShipperFrtfwdNo;
    private String sslineNumber;
    private String ECIFWNO;
    private String ECIVENDNO;
    private String eciCarrier;
    private Date enterDate;
    private String disabled;
    private Date disabledTime;
    private String tempFormattedDate;
    private String inActive;
    private Integer portNumber;
    private String taxExempt;
    private String federalId;
    private String notifyParty;
    private String notifyPartyAddress;
    private String notifyPartyCity;
    private String notifyPartyState;
    private String notifyPartyCountry;
    private String notifyPartyPostalCode;
    private Integer arBatchLockUser;
    private String updateBy;
    private UnLocation customerLocation;
    private String forwardAccount;
    private GeneralInformation generalInfo;
    private CustomerAddress custAddr;
    private CustomerContact custContact;
    private String contact;
    private String email;
    private String fax;
    private Double flatFee;
    private Double lineMarkUp;
    private Double fuelMarkUp;
    private Double minAmount;
    private String ctsUID;
    private String ecuDesignation;
    private String ecuReportingType;
    private String firmsCode;
    private String ecuLogo;
    private CorporateAccount corporateAccount;
    private String searchAcctName;
    private String brandPreference;

    public String getDisabled() {
        return disabled;
    }

    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }

    public Date getDisabledTime() {
        return disabledTime;
    }

    public void setDisabledTime(Date disabledTime) {
        this.disabledTime = disabledTime;
    }

    public Date getEnterDate() {
        return enterDate;
    }

    public void setEnterDate(Date enterDate) {
        this.enterDate = enterDate;
    }

    public String getECIFWNO() {
        return ECIFWNO;
    }

    public void setECIFWNO(String ecifwno) {
        ECIFWNO = ecifwno;
    }

    public String getECIVENDNO() {
        return ECIVENDNO;
    }

    public void setECIVENDNO(String ecivendno) {
        ECIVENDNO = ecivendno;
    }

    /**
     *
     */
    public TradingPartner() {
    }

    public TradingPartner(String accountno) {
        this.accountno = accountno;
    }

    public TradingPartner(TradingPartnerForm tradingPartnerForm) {
        updateTradingPartner(tradingPartnerForm);

    }

    public void updateTradingPartner(TradingPartnerForm tradingPartnerForm) {
        master = tradingPartnerForm.getMaster();
    }

    public List getMasterList() {
        return masterList;
    }

    public void setMasterList(List masterList) {
        this.masterList = masterList;
    }

    public Set<PaymentMethod> getPaymentset() {
        return paymentset;
    }

    public void setPaymentset(Set<PaymentMethod> paymentset) {
        this.paymentset = paymentset;
    }

    public Set<Vendor> getVendorset() {
        return vendorset;
    }

    public void setVendorset(Set<Vendor> vendorset) {
        this.vendorset = vendorset;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getHold() {
        return hold;
    }

    public void setHold(String hold) {
        this.hold = hold;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountno() {
        return accountno;
    }

    public void setAccountno(String accountno) {
        this.accountno = accountno;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getAccountPrefix() {
        return AccountPrefix;
    }

    public void setAccountPrefix(String accountPrefix) {
        AccountPrefix = accountPrefix;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public Set<CustomerAccounting> getAccounting() {
        return accounting;
    }

    public void setAccounting(Set<CustomerAccounting> accounting) {
        this.accounting = accounting;
    }

    public Set getAssociateCustomer() {
        return associateCustomer;
    }

    public void setAssociateCustomer(Set associateCustomer) {
        this.associateCustomer = associateCustomer;
    }

    public Set getCustomerContact() {
        return customerContact;
    }

    public void setCustomerContact(Set customerContact) {
        this.customerContact = customerContact;
    }

    public Set<GeneralInformation> getGeneralInformation() {
        return generalInformation;
    }

    public void setGeneralInformation(Set<GeneralInformation> generalInformation) {
        this.generalInformation = generalInformation;
    }

    public String getAcctType() {
        return acctType;
    }

    public void setAcctType(String acctType) {
        this.acctType = acctType;
    }

    public Set<CustomerAddress> getCustomerAddressSet() {
        return customerAddressSet;
    }

    public void setCustomerAddressSet(Set<CustomerAddress> customerAddressSet) {
        this.customerAddressSet = customerAddressSet;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getEciAccountNo() {
        return eciAccountNo;
    }

    public void setEciAccountNo(String eciAccountNo) {
        this.eciAccountNo = eciAccountNo;
    }

    public String getVendorShipperFrtfwdNo() {
        return vendorShipperFrtfwdNo;
    }

    public void setVendorShipperFrtfwdNo(String vendorShipperFrtfwdNo) {
        this.vendorShipperFrtfwdNo = vendorShipperFrtfwdNo;
    }

    public String getSslineNumber() {
        return sslineNumber;
    }

    public void setSslineNumber(String sslineNumber) {
        this.sslineNumber = sslineNumber;
    }

    public String getEciCarrier() {
        return eciCarrier;
    }

    public void setEciCarrier(String eciCarrier) {
        this.eciCarrier = eciCarrier;
    }

    public String getTempFormattedDate() {
        return tempFormattedDate;
    }

    public void setTempFormattedDate(String tempFormattedDate) {
        this.tempFormattedDate = tempFormattedDate;
    }

    public String getInActive() {
        return inActive;
    }

    public void setInActive(String inActive) {
        this.inActive = inActive;
    }

    public String getFederalId() {
        return federalId;
    }

    public void setFederalId(String federalId) {
        this.federalId = federalId;
    }

    public String getNotifyParty() {
        return notifyParty;
    }

    public void setNotifyParty(String notifyParty) {
        this.notifyParty = notifyParty;
    }

    public String getNotifyPartyAddress() {
        return notifyPartyAddress;
    }

    public void setNotifyPartyAddress(String notifyPartyAddress) {
        this.notifyPartyAddress = notifyPartyAddress;
    }

    public String getNotifyPartyCity() {
        return notifyPartyCity;
    }

    public void setNotifyPartyCity(String notifyPartyCity) {
        this.notifyPartyCity = notifyPartyCity;
    }

    public String getNotifyPartyCountry() {
        return notifyPartyCountry;
    }

    public void setNotifyPartyCountry(String notifyPartyCountry) {
        this.notifyPartyCountry = notifyPartyCountry;
    }

    public String getNotifyPartyPostalCode() {
        return notifyPartyPostalCode;
    }

    public void setNotifyPartyPostalCode(String notifyPartyPostalCode) {
        this.notifyPartyPostalCode = notifyPartyPostalCode;
    }

    public String getNotifyPartyState() {
        return notifyPartyState;
    }

    public void setNotifyPartyState(String notifyPartyState) {
        this.notifyPartyState = notifyPartyState;
    }

    public Integer getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(Integer portNumber) {
        this.portNumber = portNumber;
    }

    public String getTaxExempt() {
        return taxExempt;
    }

    public void setTaxExempt(String taxExempt) {
        this.taxExempt = taxExempt;
    }

    public Integer getArBatchLockUser() {
        return arBatchLockUser;
    }

    public void setArBatchLockUser(Integer arBatchLockUser) {
        this.arBatchLockUser = arBatchLockUser;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public UnLocation getCustomerLocation() {
        return customerLocation;
    }

    public void setCustomerLocation(UnLocation customerLocation) {
        this.customerLocation = customerLocation;
    }

    public String getForwardAccount() {
        return forwardAccount;
    }

    public void setForwardAccount(String forwardAccount) {
        this.forwardAccount = forwardAccount;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getCtsUID() {
        return ctsUID;
    }

    public void setCtsUID(String ctsUID) {
        this.ctsUID = ctsUID;
    }

    public Double getFlatFee() {
        return flatFee;
    }

    public void setFlatFee(Double flatFee) {
        this.flatFee = flatFee;
    }

    public Double getFuelMarkUp() {
        return fuelMarkUp;
    }

    public void setFuelMarkUp(Double fuelMarkUp) {
        this.fuelMarkUp = fuelMarkUp;
    }

    public Double getLineMarkUp() {
        return lineMarkUp;
    }

    public void setLineMarkUp(Double lineMarkUp) {
        this.lineMarkUp = lineMarkUp;
    }

    public Double getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(Double minAmount) {
        this.minAmount = minAmount;
    }

    public String getEcuDesignation() {
        return ecuDesignation;
    }

    public void setEcuDesignation(String ecuDesignation) {
        this.ecuDesignation = ecuDesignation;
    }

    public String getEcuReportingType() {
        return ecuReportingType;
    }

    public void setEcuReportingType(String ecuReportingType) {
        this.ecuReportingType = ecuReportingType;
    }

    public String getFirmsCode() {
        return firmsCode;
    }

    public void setFirmsCode(String firmsCode) {
        this.firmsCode = firmsCode;
    }

    public String getEcuLogo() {
        return ecuLogo;
    }

    public void setEcuLogo(String ecuLogo) {
        this.ecuLogo = ecuLogo;
    }

    public GeneralInformation getGeneralInfo() {
        if (null != this.generalInformation && !this.generalInformation.isEmpty()) {
            GeneralInformation g = (GeneralInformation) this.generalInformation.iterator().next();
            return g;
        }
        return generalInfo;
    }

    public CustomerAddress getCustAddr() {
        if (null != this.customerAddressSet && !this.customerAddressSet.isEmpty()) {
            CustomerAddress c = (CustomerAddress) this.customerAddressSet.iterator().next();
            return c;
        }
        return custAddr;
    }

    public CustomerAddress getPrimaryCustAddr() {
        if (null != this.customerAddressSet) {
            Iterator iterator = customerAddressSet.iterator();
            while (iterator.hasNext()) {
                CustomerAddress c = (CustomerAddress) iterator.next();
                if ("on".equalsIgnoreCase(c.getPrimary())) {
                    return c;
                }
            }
        }
        return null;
    }

    public CustomerContact getCustContact() {
        if (null != this.customerContact && !this.customerContact.isEmpty()) {
            CustomerContact d = (CustomerContact) this.customerContact.iterator().next();
            return d;
        }
        return custContact;
    }

    public CorporateAccount getCorporateAccount() {
        return corporateAccount;
    }

    public void setCorporateAccount(CorporateAccount corporateAccount) {
        this.corporateAccount = corporateAccount;
    }

    public String getSearchAcctName() {
        return searchAcctName;
    }

    public void setSearchAcctName(String searchAcctName) {
        this.searchAcctName = searchAcctName;
    }

    public String getBrandPreference() {
        return brandPreference;
    }

    public void setBrandPreference(String brandPreference) {
        this.brandPreference = brandPreference;
    }
    
}
