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
 * @author PALRAJ */
public class FclCfclChargeBean implements Serializable{
    private String fileNumber;
    private Long fileId;
    private Double arAmount =0.00;
    private Double apAmount =0.00;
    private Double invoiceAmount;
    private String invoiceNumber;
    private Boolean hazmat;
    private String chargeCode;
    private String containerSize;
    private String customerAcct;
    private String customerName;
    private BigDecimal totalVolumeImperial;
    private Integer totalPieceCount;
    private BigDecimal totalWeightImperial;
    private String ncm;
    private String hsCode;
    

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public Long getFileId() {
        return fileId;
    }   

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }
   
    public Double getArAmount() {
        return arAmount;
    }

    public void setArAmount(Double arAmount) {
        this.arAmount = arAmount;
    }

    public Double getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(Double invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Boolean getHazmat() {
        return hazmat;
    }

    public void setHazmat(Boolean hazmat) {
        this.hazmat = hazmat;
    }

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

    public String getContainerSize() {
        return containerSize;
    }

    public void setContainerSize(String containerSize) {
        this.containerSize = containerSize;
    }

    public Double getApAmount() {
        return apAmount;
    }

    public void setApAmount(Double apAmount) {
        this.apAmount = apAmount;
    }

    public String getCustomerAcct() {
        return customerAcct;
    }

    public void setCustomerAcct(String customerAcct) {
        this.customerAcct = customerAcct;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public BigDecimal getTotalVolumeImperial() {
        return totalVolumeImperial;
    }

    public void setTotalVolumeImperial(BigDecimal totalVolumeImperial) {
        this.totalVolumeImperial = totalVolumeImperial;
    }

    public Integer getTotalPieceCount() {
        return totalPieceCount;
    }

    public void setTotalPieceCount(Integer totalPieceCount) {
        this.totalPieceCount = totalPieceCount;
    }

    public BigDecimal getTotalWeightImperial() {
        return totalWeightImperial;
    }

    public void setTotalWeightImperial(BigDecimal totalWeightImperial) {
        this.totalWeightImperial = totalWeightImperial;
    }

    public String getNcm() {
        return ncm;
    }

    public void setNcm(String ncm) {
        this.ncm = ncm;
    }

    public String getHsCode() {
        return hsCode;
    }

    public void setHsCode(String hsCode) {
        this.hsCode = hsCode;
    }
    
    
}
