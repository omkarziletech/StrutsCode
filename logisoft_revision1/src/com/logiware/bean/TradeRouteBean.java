/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.bean;

/**
 *
 * @author Balaji.E(Logiware)
 */
public class TradeRouteBean {

    private String portName;
    private String stateCode;
    private String countryName;
    private String unLocationCode;
    private String shedulenumber;

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getUnLocationCode() {
        return unLocationCode;
    }

    public void setUnLocationCode(String unLocationCode) {
        this.unLocationCode = unLocationCode;
    }

    public String getShedulenumber() {
        return shedulenumber;
    }

    public void setShedulenumber(String shedulenumber) {
        this.shedulenumber = shedulenumber;
    }
    
}
