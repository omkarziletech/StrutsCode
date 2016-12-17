package com.gp.cvst.logisoft.domain;

import java.util.Date;
import jxl.Cell;

public class GlMapping implements java.io.Serializable {

    private static final long serialVersionUID = -8726082476061626113L;
    // Fields
    private Integer id;
    private String shipmentType;
    private String chargeCode;
    private String revExp;
    private String transactionType;
    private String glAcct;
    private String suffixValue;
    private String deriveYn;
    private String subLedgerCode;
    private String chargeDescriptions;
    private String blueScreenChargeCode;
    private String suffixAlternate;
    private boolean special;
    private Date createdDate;
    private String createdBy;
    private Date updatedDate;
    private String updatedBy;
    private boolean bluescreenFeedback;
    private boolean destinationServices;
    private boolean blLevelCost;
    // Constructors

    public String getSubLedgerCode() {
        return null != subLedgerCode ? subLedgerCode.toUpperCase() : subLedgerCode;
    }

    public void setSubLedgerCode(String subLedgerCode) {
        this.subLedgerCode = subLedgerCode;
    }

    /** default constructor */
    public GlMapping() {
    }

    /** full constructor from Excel Cell*/
    public GlMapping(Cell rowData[]) {
        this.chargeCode = rowData[0].getContents();
        if (rowData[1].getContents().length() > 50) {
            this.chargeDescriptions = rowData[1].getContents().substring(0, 50);
        } else {
            this.chargeDescriptions = rowData[1].getContents();
        }
        this.glAcct = rowData[2].getContents();
        this.shipmentType = rowData[3].getContents();
        this.subLedgerCode = rowData[4].getContents();
        this.revExp = rowData[5].getContents();
        this.transactionType = rowData[6].getContents();
        this.deriveYn = rowData[7].getContents();
        this.suffixValue = rowData[8].getContents();
    }

    public GlMapping(GlMapping glMapping, Cell rowData[]) {
        this.chargeCode = null != rowData[0].getContents() && !rowData[0].getContents().trim().equals("") ? rowData[0].getContents() : glMapping.getChargeCode();
        this.chargeDescriptions = null != rowData[1].getContents() && !rowData[1].getContents().trim().equals("") ? rowData[1].getContents() : glMapping.getChargeDescriptions();
        this.glAcct = null != rowData[2].getContents() && !rowData[2].getContents().trim().equals("") ? rowData[2].getContents() : glMapping.getGlAcct();
        this.shipmentType = null != rowData[3].getContents() && !rowData[3].getContents().trim().equals("") ? rowData[3].getContents() : glMapping.getShipmentType();
        this.subLedgerCode = null != rowData[4].getContents() && !rowData[4].getContents().trim().equals("") ? rowData[4].getContents() : glMapping.getSubLedgerCode();
        this.revExp = null != rowData[5].getContents() && !rowData[5].getContents().trim().equals("") ? rowData[5].getContents() : glMapping.getRevExp();
        this.transactionType = null != rowData[6].getContents() && !rowData[6].getContents().trim().equals("") ? rowData[6].getContents() : glMapping.getTransactionType();
        this.deriveYn = null != rowData[7].getContents() && !rowData[7].getContents().trim().equals("") ? rowData[7].getContents() : glMapping.getDeriveYn();
        this.suffixValue = null != rowData[8].getContents() && !rowData[8].getContents().trim().equals("") ? rowData[8].getContents() : glMapping.getSuffixValue();
        this.id = glMapping.getId();
    }

    /** full constructor */
    public GlMapping(String shipmentType, String chargeCode, String revExp, String transactionType, String glAcct, String suffixValue, String deriveYn, String chargeDescriptions) {
        this.shipmentType = shipmentType;
        this.chargeCode = chargeCode;
        this.revExp = revExp;
        this.transactionType = transactionType;
        this.glAcct = glAcct;
        this.suffixValue = suffixValue;
        this.deriveYn = deriveYn;
        this.chargeDescriptions = chargeDescriptions;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShipmentType() {
        return null != this.shipmentType ? this.shipmentType.toUpperCase() : this.shipmentType;
    }

    public void setShipmentType(String shipmentType) {
        this.shipmentType = shipmentType;
    }

    public String getChargeCode() {
        return null != this.chargeCode ? this.chargeCode.toUpperCase() : this.chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

    public String getRevExp() {
        return null != this.revExp ? this.revExp.toUpperCase() : this.revExp;
    }

    public void setRevExp(String revExp) {
        this.revExp = revExp;
    }

    public String getTransactionType() {
        return null != this.transactionType ? this.transactionType.toUpperCase() : this.transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getGlAcct() {
        return this.glAcct;
    }

    public void setGlAcct(String glAcct) {
        this.glAcct = glAcct;
    }

    public String getSuffixValue() {
        return null != this.suffixValue ? this.suffixValue.toUpperCase() : this.suffixValue;
    }

    public void setSuffixValue(String suffixValue) {
        this.suffixValue = suffixValue;
    }

    public String getDeriveYn() {
        return null != this.deriveYn ? this.deriveYn.toUpperCase() : this.deriveYn;
    }

    public void setDeriveYn(String deriveYn) {
        this.deriveYn = deriveYn;
    }

    public String getChargeDescriptions() {
        return null != chargeDescriptions ? chargeDescriptions.toUpperCase() : chargeDescriptions;
    }

    public void setChargeDescriptions(String chargeDescriptions) {
        this.chargeDescriptions = chargeDescriptions;
    }

    public String getBlueScreenChargeCode() {
        return null != blueScreenChargeCode ? blueScreenChargeCode.toUpperCase() : blueScreenChargeCode;
    }

    public void setBlueScreenChargeCode(String blueScreenChargeCode) {
        this.blueScreenChargeCode = blueScreenChargeCode;
    }

    public boolean isSpecial() {
        return special;
    }

    public void setSpecial(boolean special) {
        this.special = special;
    }

    public String getSuffixAlternate() {
        return suffixAlternate;
    }

    public void setSuffixAlternate(String suffixAlternate) {
        this.suffixAlternate = suffixAlternate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public boolean isBluescreenFeedback() {
        return bluescreenFeedback;
    }

    public void setBluescreenFeedback(boolean bluescreenFeedback) {
        this.bluescreenFeedback = bluescreenFeedback;
    }
      public boolean isDestinationServices() {
        return destinationServices;
    }

    public void setDestinationServices(boolean destinationServices) {
        this.destinationServices = destinationServices;
    }

    public boolean isBlLevelCost() {
        return blLevelCost;
    }

    public void setBlLevelCost(boolean blLevelCost) {
        this.blLevelCost = blLevelCost;
    }
}
