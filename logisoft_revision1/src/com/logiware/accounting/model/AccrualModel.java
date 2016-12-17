package com.logiware.accounting.model;

import java.math.BigInteger;

/**
 *
 * @author Lakshmi Naryanan
 */
public class AccrualModel {

    private String costCode;
    private String glAccount;
    private String blNumber;
    private String containerNumber;
    private String voyageNumber;
    private String dockReceipt;
    private String reportingDate;
    private String amount;
    private String bluescreenCostCode;
    private String shipmentType;
    private String terminal;
    private Integer id;
    private BigInteger costId;

    public String getCostCode() {
        return costCode;
    }

    public void setCostCode(String costCode) {
        this.costCode = costCode;
    }

    public String getGlAccount() {
        return glAccount;
    }

    public void setGlAccount(String glAccount) {
        this.glAccount = glAccount;
    }

    public String getBlNumber() {
        return blNumber;
    }

    public void setBlNumber(String blNumber) {
        this.blNumber = blNumber;
    }

    public String getContainerNumber() {
        return containerNumber;
    }

    public void setContainerNumber(String containerNumber) {
        this.containerNumber = containerNumber;
    }

    public String getVoyageNumber() {
        return voyageNumber;
    }

    public void setVoyageNumber(String voyageNumber) {
        this.voyageNumber = voyageNumber;
    }

    public String getDockReceipt() {
        return dockReceipt;
    }

    public void setDockReceipt(String dockReceipt) {
        this.dockReceipt = dockReceipt;
    }

    public String getReportingDate() {
        return reportingDate;
    }

    public void setReportingDate(String reportingDate) {
        this.reportingDate = reportingDate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBluescreenCostCode() {
        return bluescreenCostCode;
    }

    public void setBluescreenCostCode(String bluescreenCostCode) {
        this.bluescreenCostCode = bluescreenCostCode;
    }

    public String getShipmentType() {
        return shipmentType;
    }

    public void setShipmentType(String shipmentType) {
        this.shipmentType = shipmentType;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigInteger getCostId() {
        return costId;
    }

    public void setCostId(BigInteger costId) {
        this.costId = costId;
    }

}
