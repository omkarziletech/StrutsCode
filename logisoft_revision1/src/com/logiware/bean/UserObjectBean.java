package com.logiware.bean;

import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.domain.RefTerminal;
import java.io.Serializable;
import java.util.Date;

/**
 * @since Thursday March 18,2010
 * @author LakshmiNarayanan
 */
public class UserObjectBean implements  Serializable{
    private static final long serialVersionUID = 192590010818408252L;
    private Integer userId;
    private String firstName;
    private String lastName;
    private String telephone;
    private String address1;
    private String address2;
    private String country;
    private String state;
    private String city;
    private String userIpAddress;
    private RefTerminal terminal;
    private String loginName;
    private String term;
    private Date loginTime;
    private String date;

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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserIpAddress() {
        return userIpAddress;
    }

    public void setUserIpAddress(String userIpAddress) {
        this.userIpAddress = userIpAddress;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public RefTerminal getTerminal() {
        return terminal;
    }

    public void setTerminal(RefTerminal terminal) {
        this.terminal = terminal;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate() throws Exception {
        this.date = DateUtils.formatDate(this.loginTime, "MMM-dd-yyyy hh:mm a");
    }
}
