/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.beans;

/**
 *
 * @author vijaygupta.m
 */
public class LclImpAlarmRevenueCostBean {

    private String fileNumber;
    private String revenueAmount;
    private String chargeCode;
    private String CostAmount;
    private String costCode;

    public String getCostAmount() {
        return CostAmount;
    }

    public void setCostAmount(String CostAmount) {
        this.CostAmount = CostAmount;
    }

    public String getRevenueAmount() {
        return revenueAmount;
    }

    public void setRevenueAmount(String revenueAmount) {
        this.revenueAmount = revenueAmount;
    }

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

    public String getCostCode() {
        return costCode;
    }

    public void setCostCode(String costCode) {
        this.costCode = costCode;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }
   
}
