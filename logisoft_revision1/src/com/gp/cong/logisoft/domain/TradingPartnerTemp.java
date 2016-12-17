package com.gp.cong.logisoft.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.gp.cong.logisoft.struts.form.TradingPartnerForm;

public class TradingPartnerTemp implements Serializable {

    private String accountno;
    private String accountName;
    private String master;
    private String acctType;
    private String AccountPrefix;
    private String Type;
    private Set vendorset;
    private Set paymentset;
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

    /**
     *
     */
    public TradingPartnerTemp() {
    }

    public TradingPartnerTemp(TradingPartnerForm tradingPartnerForm) {
        updateTradingPartner(tradingPartnerForm);


    }

    public void updateTradingPartner(TradingPartnerForm tradingPartnerForm) {
        master = tradingPartnerForm.getMaster();
    }

    public List getMasterList() {
        return masterList;
    }

    public void setMasterList(List masterList) {
        masterList = masterList;
    }

    public Set getPaymentset() {
        return paymentset;
    }

    public void setPaymentset(Set paymentset) {
        this.paymentset = paymentset;
    }

    public Set getVendorset() {
        return vendorset;
    }

    public void setVendorset(Set vendorset) {
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

    public String getAcctType() {
        return acctType;
    }

    public void setAcctType(String acctType) {
        this.acctType = acctType;
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
}
