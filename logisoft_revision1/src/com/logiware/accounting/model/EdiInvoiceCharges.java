package com.logiware.accounting.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Rajesh
 */
public class EdiInvoiceCharges {

    private Integer id;
    private Integer arGlId;
    private Integer apGlId;
    private String bluCostCode;
    private String bluChargeCode;
    private Long fileId;
    private String vendorNo;
    private String vendorName;
    private String invoiceNo;
    private String eculineDesc;
    private String chargeCode;
    private String costCode;
    private String chargeStatus;
    private String invoiceStatus;
    private Date createdDate;
    private String quantity;
    private String price;
    private String rate;
    private String currency;
    private String arBillToParty;
    private BigDecimal arAmount;
    private BigDecimal apAmount;
    private Boolean containerApproved;
    private Boolean blApproved;
    private String fileContainChargesFlag;
    private Integer chargeId;
    private Integer costId;
    private Integer invoiceId;
    private String fileNo;
    private String chargeFlag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getArGlId() {
        return arGlId;
    }

    public void setArGlId(Integer arGlId) {
        this.arGlId = arGlId;
    }

    public Integer getApGlId() {
        return apGlId;
    }

    public void setApGlId(Integer apGlId) {
        this.apGlId = apGlId;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getVendorNo() {
        return vendorNo;
    }

    public void setVendorNo(String vendorNo) {
        this.vendorNo = vendorNo;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getEculineDesc() {
        return eculineDesc;
    }

    public void setEculineDesc(String eculineDesc) {
        this.eculineDesc = eculineDesc;
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

    public String getChargeStatus() {
        return chargeStatus;
    }

    public void setChargeStatus(String chargeStatus) {
        this.chargeStatus = chargeStatus;
    }

    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

   

    public BigDecimal getArAmount() {
        return arAmount;
    }

    public void setArAmount(BigDecimal arAmount) {
        this.arAmount = arAmount;
    }

    public BigDecimal getApAmount() {
        return apAmount;
    }

    public void setApAmount(BigDecimal apAmount) {
        this.apAmount = apAmount;
    }

    public Boolean getContainerApproved() {
        return containerApproved;
    }

    public void setContainerApproved(Boolean containerApproved) {
        this.containerApproved = containerApproved;
    }

    public Boolean getBlApproved() {
        return blApproved;
    }

    public void setBlApproved(Boolean blApproved) {
        this.blApproved = blApproved;
    }

    public Integer getChargeId() {
        return chargeId;
    }

    public void setChargeId(Integer chargeId) {
        this.chargeId = chargeId;
    }

    public Integer getCostId() {
        return costId;
    }

    public void setCostId(Integer costId) {
        this.costId = costId;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getBluChargeCode() {
        return bluChargeCode;
    }

    public void setBluChargeCode(String bluChargeCode) {
        this.bluChargeCode = bluChargeCode;
    }

    public String getBluCostCode() {
        return bluCostCode;
    }

    public void setBluCostCode(String bluCostCode) {
        this.bluCostCode = bluCostCode;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getArBillToParty() {
        return arBillToParty;
    }

    public void setArBillToParty(String arBillToParty) {
        this.arBillToParty = arBillToParty;
    }

    public String getFileContainChargesFlag() {
        return fileContainChargesFlag;
    }

    public void setFileContainChargesFlag(String fileContainChargesFlag) {
        this.fileContainChargesFlag = fileContainChargesFlag;
    }

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    public String getChargeFlag() {
        return chargeFlag;
    }

    public void setChargeFlag(String chargeFlag) {
        this.chargeFlag = chargeFlag;
    }
}
