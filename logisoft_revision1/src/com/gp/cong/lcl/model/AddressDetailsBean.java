/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.lcl.model;

/**
 *
 * @author lakshh
 */
public class AddressDetailsBean {

    private String acctName;
    private String acctNo;
    private String acctType;
    private String contactName;
    private String phone;
    private String fax;
    private String email;
    private String address;
    private String city;
    private String country;
    private String state;
    private String zip;
    private String subType;
    private String poa;

    public AddressDetailsBean(Object[] obj) {
        int index = 0;
        acctName = null == obj[index] ? null : obj[index].toString();
        index++;
        acctNo = null == obj[index] ? null : obj[index].toString();
        index++;
        acctType = null == obj[index] ? null : obj[index].toString();
        index++;
        contactName = null == obj[index] ? null : obj[index].toString();
        index++;
        phone = null == obj[index] ? null : obj[index].toString();
        index++;
        fax = null == obj[index] ? null : obj[index].toString();
        index++;
        email = null == obj[index] ? null : obj[index].toString();
        index++;
        address = null == obj[index] ? null : obj[index].toString();
        index++;
        city = null == obj[index] ? null : obj[index].toString();
        index++;
        country = null == obj[index] ? null : obj[index].toString();
        index++;
        state = null == obj[index] ? null : obj[index].toString();
        index++;
        zip = null == obj[index] ? null : obj[index].toString();
        index++;
        subType = null == obj[index] ? null : obj[index].toString();
        index++;
        poa = null == obj[index] ? null : obj[index].toString();
    }

    public String getAcctName() {
        return acctName;
    }

    public void setAcctName(String acctName) {
        this.acctName = acctName;
    }

    public String getAcctNo() {
        return acctNo;
    }

    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    public String getAcctType() {
        return acctType;
    }

    public void setAcctType(String acctType) {
        this.acctType = acctType;
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

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPoa() {
        return poa;
    }

    public void setPoa(String poa) {
        this.poa = poa;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
