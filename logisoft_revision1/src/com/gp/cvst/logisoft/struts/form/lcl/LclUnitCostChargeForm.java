 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.struts.form.lcl;

/**
 *
 * @author Logiware
 */
public class LclUnitCostChargeForm extends LogiwareActionForm {

    private Long ssdetailId;
    private Long headerId;
    private Long unitSsId;
    private Long unitId;
    private Long unitSSAcId;
    private Long bookingAcId;
    private String unitStatus;
    private String costStatus;
    private String unitNo;
    private String finalAmount;
    private String percentage;
    private String costAmount;
    private String thirdPartyname;
    private String thirdpartyaccountNo;
    private String chargesCode;
    private Integer chargesCodeId;
    private String invoiceNumber;
    private String fileId;
    private String chargesAmount;
    private String message;
    private String groupByInvoiceFlag;
    private String lastInvoiceNumber;
    private String lastVendorName;
    private String lastVendorNumber;
    private Boolean autoCostFlag;
    private Integer cfsWarehouseId;
    private Long unitTypeId;
    private String minimumAmt;
    private String podUnlocCode;
    private String closedTime;
    private String auditedTime;
    private String lclSsAcId;
    private String saveDrCostFlag;
    private String moduleName;
    private Boolean cobStatus;
    private String hazFlag;

    public Long getHeaderId() {
        return headerId;
    }

    public void setHeaderId(Long headerId) {
        this.headerId = headerId;
    }

    public Long getSsdetailId() {
        return ssdetailId;
    }

    public void setSsdetailId(Long ssdetailId) {
        this.ssdetailId = ssdetailId;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public Long getUnitSsId() {
        return unitSsId;
    }

    public void setUnitSsId(Long unitSsId) {
        this.unitSsId = unitSsId;
    }

    public String getCostAmount() {
        return costAmount;
    }

    public void setCostAmount(String costAmount) {
        this.costAmount = costAmount;
    }

    public String getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(String finalAmount) {
        this.finalAmount = finalAmount;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getThirdPartyname() {
        return thirdPartyname;
    }

    public void setThirdPartyname(String thirdPartyname) {
        this.thirdPartyname = thirdPartyname;
    }

    public String getThirdpartyaccountNo() {
        return thirdpartyaccountNo;
    }

    public void setThirdpartyaccountNo(String thirdpartyaccountNo) {
        this.thirdpartyaccountNo = thirdpartyaccountNo;
    }

    public String getChargesCode() {
        return chargesCode;
    }

    public void setChargesCode(String chargesCode) {
        this.chargesCode = chargesCode;
    }

    public Integer getChargesCodeId() {
        return chargesCodeId;
    }

    public void setChargesCodeId(Integer chargesCodeId) {
        this.chargesCodeId = chargesCodeId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getChargesAmount() {
        return chargesAmount;
    }

    public void setChargesAmount(String chargesAmount) {
        this.chargesAmount = chargesAmount;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getUnitNo() {
        return unitNo;
    }

    public void setUnitNo(String unitNo) {
        this.unitNo = unitNo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getUnitSSAcId() {
        return unitSSAcId;
    }

    public void setUnitSSAcId(Long unitSSAcId) {
        this.unitSSAcId = unitSSAcId;
    }

    public Boolean getAutoCostFlag() {
        return autoCostFlag;
    }

    public void setAutoCostFlag(Boolean autoCostFlag) {
        this.autoCostFlag = autoCostFlag;
    }

    public Boolean getCobStatus() {
        return cobStatus;
    }

    public void setCobStatus(Boolean cobStatus) {
        this.cobStatus = cobStatus;
    }

    public Integer getCfsWarehouseId() {
        return cfsWarehouseId;
    }

    public void setCfsWarehouseId(Integer cfsWarehouseId) {
        this.cfsWarehouseId = cfsWarehouseId;
    }

    public Long getUnitTypeId() {
        return unitTypeId;
    }

    public void setUnitTypeId(Long unitTypeId) {
        this.unitTypeId = unitTypeId;
    }

    public String getUnitStatus() {
        return unitStatus;
    }

    public void setUnitStatus(String unitStatus) {
        this.unitStatus = unitStatus;
    }

    public String getCostStatus() {
        return costStatus;
    }

    public void setCostStatus(String costStatus) {
        this.costStatus = costStatus;
    }

    public String getGroupByInvoiceFlag() {
        return groupByInvoiceFlag;
    }

    public void setGroupByInvoiceFlag(String groupByInvoiceFlag) {
        this.groupByInvoiceFlag = groupByInvoiceFlag;
    }

    public String getLastInvoiceNumber() {
        return lastInvoiceNumber;
    }

    public void setLastInvoiceNumber(String lastInvoiceNumber) {
        this.lastInvoiceNumber = lastInvoiceNumber;
    }

    public String getLastVendorName() {
        return lastVendorName;
    }

    public void setLastVendorName(String lastVendorName) {
        this.lastVendorName = lastVendorName;
    }

    public String getLastVendorNumber() {
        return lastVendorNumber;
    }

    public void setLastVendorNumber(String lastVendorNumber) {
        this.lastVendorNumber = lastVendorNumber;
    }

    public String getMinimumAmt() {
        return minimumAmt;
    }

    public void setMinimumAmt(String minimumAmt) {
        this.minimumAmt = minimumAmt;
    }

    public String getPodUnlocCode() {
        return podUnlocCode;
    }

    public void setPodUnlocCode(String podUnlocCode) {
        this.podUnlocCode = podUnlocCode;
    }

    public String getAuditedTime() {
        return auditedTime;
    }

    public void setAuditedTime(String auditedTime) {
        this.auditedTime = auditedTime;
    }

    public String getClosedTime() {
        return closedTime;
    }

    public void setClosedTime(String closedTime) {
        this.closedTime = closedTime;
    }

    public String getLclSsAcId() {
        return lclSsAcId;
    }

    public void setLclSsAcId(String lclSsAcId) {
        this.lclSsAcId = lclSsAcId;
    }

    public Long getBookingAcId() {
        return bookingAcId;
    }

    public void setBookingAcId(Long bookingAcId) {
        this.bookingAcId = bookingAcId;
    }

    public String getSaveDrCostFlag() {
        return saveDrCostFlag;
    }

    public void setSaveDrCostFlag(String saveDrCostFlag) {
        this.saveDrCostFlag = saveDrCostFlag;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getHazFlag() {
        return hazFlag;
    }

    public void setHazFlag(String hazFlag) {
        this.hazFlag = hazFlag;
    }
}
