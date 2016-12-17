package com.logiware.accounting.model;

import com.gp.cong.common.CommonUtils;

/**
 *
 * @author Lakshmi Naryanan
 */
public class TradingPartnerModel {

    private String name;
    private String number;
    private String type;
    private String address;
    private String salesCode;
    private boolean disabled;
    private String contact;
    private String email;
    private Integer userId;
    private boolean consigneeOnly;
    private String nameRange;
    private String subType;
    private String bs_ship_fwd_no;
    private String bs_cons_no;
    private String city;
    private String state;
    private String zip;
    private String country;
    private String phone;
    private String fax;
    private String contactName;
    private String clientType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSalesCode() {
        return salesCode;
    }

    public void setSalesCode(String salesCode) {
        this.salesCode = salesCode;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(String disabled) {
        this.disabled = CommonUtils.isEqualIgnoreCase(disabled, "true");
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public boolean isConsigneeOnly() {
        return consigneeOnly;
    }

    public void setConsigneeOnly(boolean consigneeOnly) {
        this.consigneeOnly = consigneeOnly;
    }

    public String getNameRange() {
        return nameRange;
    }

    public void setNameRange(String nameRange) {
        this.nameRange = nameRange;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getBs_ship_fwd_no() {
        return bs_ship_fwd_no;
    }

    public void setBs_ship_fwd_no(String bs_ship_fwd_no) {
        this.bs_ship_fwd_no = bs_ship_fwd_no;
    }

    public String getBs_cons_no() {
        return bs_cons_no;
    }

    public void setBs_cons_no(String bs_cons_no) {
        this.bs_cons_no = bs_cons_no;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

}
