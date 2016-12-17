/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.beans;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Wsadmin
 */
public class MultiQuoteChargesBean implements Serializable {
       
    private Long id;
    private String unitType;
    private String unitNo;
    private String chargeCode;
    private String chargeCodeDesc;
    private BigDecimal amount;
    private String number;
    private String costType;
    private BigDecimal markup;
    private BigDecimal adjustment;
    private String comments;
    private String acctNo;
    private String acctName;
    private String adjustmentComments;
    

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }    
    
    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

    public String getChargeCodeDesc() {
        return chargeCodeDesc;
    }

    public void setChargeCodeDesc(String chargeCodeDesc) {
        this.chargeCodeDesc = chargeCodeDesc;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCostType() {
        return costType;
    }

    public void setCostType(String costType) {
        this.costType = costType;
    }

    public BigDecimal getMarkup() {
        return markup;
    }

    public void setMarkup(BigDecimal markup) {
        this.markup = markup;
    }

    public BigDecimal getAdjustment() {
        return adjustment;
    }

    public void setAdjustment(BigDecimal adjustment) {
        this.adjustment = adjustment;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getAcctNo() {
        return acctNo;
    }

    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    public String getAcctName() {
        return acctName;
    }

    public void setAcctName(String acctName) {
        this.acctName = acctName;
    }

    public String getAdjustmentComments() {
        return adjustmentComments;
    }

    public void setAdjustmentComments(String adjustmentComments) {
        this.adjustmentComments = adjustmentComments;
    }

    public String getUnitNo() {
        return unitNo;
    }

    public void setUnitNo(String unitNo) {
        this.unitNo = unitNo;
    }
    
    
    
}
