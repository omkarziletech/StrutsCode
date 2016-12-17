/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.datamigration;

/**
 *
 * @author Lakshmi Narayanan
 */
public class Subledger {

    private String bluescreenChargeCode;
    private String chargeCode;
    private String glAccount;
    private double amount;
    private String shipmentType;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getBluescreenChargeCode() {
        return bluescreenChargeCode;
    }

    public void setBluescreenChargeCode(String bluescreenChargeCode) {
        this.bluescreenChargeCode = bluescreenChargeCode;
    }

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

    public String getGlAccount() {
        return glAccount;
    }

    public void setGlAccount(String glAccount) {
        this.glAccount = glAccount;
    }

    public String getShipmentType() {
        return shipmentType;
    }

    public void setShipmentType(String shipmentType) {
        this.shipmentType = shipmentType;
    }
}
