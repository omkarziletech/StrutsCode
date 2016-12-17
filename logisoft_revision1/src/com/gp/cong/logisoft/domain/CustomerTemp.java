package com.gp.cong.logisoft.domain;

import java.util.Set;

public class CustomerTemp implements java.io.Serializable {

    private String accountNo;
    private String accountName;
    private String accountType;
    private String address1;
    private String city2;
    private String state;
    private Set customerset;
    private Integer id;
    private String zip;
    private String match;
    private String masterAccountNo;
    private String type;
    private String password;
    private String checkbox;
    private String primary;
    private String bankName;
    private String bankAddress;
    private boolean checkAddress;
    private String subType;

    public Set getCustomerset() {
        return customerset;
    }

    public void setCustomerset(Set customerset) {
        this.customerset = customerset;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getCity2() {
        return city2;
    }

    public void setCity2(String city2) {
        this.city2 = city2;
    }

    public String getMasterAccountNo() {
        return masterAccountNo;
    }

    public void setMasterAccountNo(String masterAccountNo) {
        this.masterAccountNo = masterAccountNo;
    }

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public AuditInfo getAuditInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getCheckbox() {
        return checkbox;
    }

    public void setCheckbox(String checkbox) {
        this.checkbox = checkbox;
    }

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public boolean isCheckAddress() {
        return checkAddress;
    }

    public void setCheckAddress(boolean checkAddress) {
        this.checkAddress = checkAddress;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }
}
