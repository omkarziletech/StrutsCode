/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.lcl.model;

/**
 *
 * @author Mei
 */
public class LclModel {

    private Long unitSsId;
    private String scheduleNo;
    private String vendorNo;
    private String vendorName;
    private String unitNo;
    private String unitId;
    private Integer dateDiff;
    private String unitTypeId;
    private Long bkgPieceId;
    private String chargeCode;
    private String billToParty;

    public String getScheduleNo() {
        return scheduleNo;
    }

    public void setScheduleNo(String scheduleNo) {
        this.scheduleNo = scheduleNo;
    }

    public Long getUnitSsId() {
        return unitSsId;
    }

    public void setUnitSsId(Long unitSsId) {
        this.unitSsId = unitSsId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorNo() {
        return vendorNo;
    }

    public void setVendorNo(String vendorNo) {
        this.vendorNo = vendorNo;
    }

    public Integer getDateDiff() {
        return dateDiff;
    }

    public void setDateDiff(Integer dateDiff) {
        this.dateDiff = dateDiff;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUnitNo() {
        return unitNo;
    }

    public void setUnitNo(String unitNo) {
        this.unitNo = unitNo;
    }

    public String getUnitTypeId() {
        return unitTypeId;
    }

    public void setUnitTypeId(String unitTypeId) {
        this.unitTypeId = unitTypeId;
    }

    public Long getBkgPieceId() {
        return bkgPieceId;
    }

    public void setBkgPieceId(Long bkgPieceId) {
        this.bkgPieceId = bkgPieceId;
    }

    public String getBillToParty() {
        return billToParty;
    }

    public void setBillToParty(String billToParty) {
        this.billToParty = billToParty;
    }

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }
}
