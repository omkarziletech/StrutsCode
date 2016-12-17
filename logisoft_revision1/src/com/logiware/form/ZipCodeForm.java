/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.logiware.form;

import org.apache.struts.action.ActionForm;

/**
 *
 * @author Shanmugam R
 */
public class ZipCodeForm extends ActionForm {
    private String buttonValue;
    private String countryCode;
    private String city;
    private String county;
    private String state;
    private String zip;
    private String id;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getState() {
        return null != state?state.toUpperCase():"";
    }

    public void setState(String state) {
        this.state = null != state?state.toUpperCase():"";
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getButtonValue() {
        return buttonValue;
    }

    public void setButtonValue(String buttonValue) {
        this.buttonValue = buttonValue;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    
}
