/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.beans;

import java.util.ArrayList;

/**
 *
 * @author Logiware
 */
public class ImportUnitsBean extends ArrayList<ImportBookingBean> implements Comparable<ImportUnitsBean> {

    private String headerId;
    private String scheduleNo;
    private String originId;
    private String ownerId;
    private String originUnlocCode;
    private String destinationId;
    private String unitSsId;
    private String unitId;
    private String unitNo;
    private String unitstatus;
    private String edi;
    private String description;
    private String remarks;
    private String itNumber;
    private String masterBl;
    private String coloaderAcctNo;
    private String coloaderAcctName;
    private String coloaderAddress;
    private String coloaderCity;
    private String unitWarehsNo;
    private String unitWarehsName;
    private String unitWarehsAddress;
    private String unitWarehsCity;
    private String cfsWarehsNo;
    private String cfsWarehsName;
    private String cfsWarehsAddress;
    private String cfsWarehsCity;
    private String dispoCode;
    private String dispoDesc;
    private String arRedInvoiceId;
    private String ssRemarks;

    public ImportUnitsBean() {
    }

    public ImportUnitsBean(Object[] obj) throws Exception {
        int index = 0;
        headerId = null == obj[index] ? null : obj[index].toString();
        index++;
        scheduleNo = null == obj[index] ? null : obj[index].toString();
        index++;
        originId = null == obj[index] ? null : obj[index].toString();
        index++;
        ownerId = null == obj[index] ? null : obj[index].toString();
        index++;
        originUnlocCode = null == obj[index] ? null : obj[index].toString();
        index++;
        destinationId = null == obj[index] ? null : obj[index].toString();
        index++;
        unitSsId = null == obj[index] ? null : obj[index].toString();
        index++;
        unitId = null == obj[index] ? null : obj[index].toString();
        index++;
        unitNo = null == obj[index] ? null : obj[index].toString();
        index++;
        unitstatus = null == obj[index] ? null : obj[index].toString();
        index++;
        edi = null == obj[index] ? null : obj[index].toString();
        index++;
        description = null == obj[index] ? null : obj[index].toString();
        index++;
        remarks = null == obj[index] ? null : obj[index].toString();
        index++;
        itNumber = null == obj[index] ? null : obj[index].toString();
        index++;
        masterBl = null == obj[index] ? null : obj[index].toString();
        index++;
        coloaderAcctNo = null == obj[index] ? null : obj[index].toString();
        index++;
        coloaderAcctName = null == obj[index] ? null : obj[index].toString();
        index++;
        coloaderAddress = null == obj[index] ? null : obj[index].toString();
        index++;
        coloaderCity = null == obj[index] ? null : obj[index].toString();
        index++;
        unitWarehsNo = null == obj[index] ? null : obj[index].toString();
        index++;
        unitWarehsName = null == obj[index] ? null : obj[index].toString();
        index++;
        unitWarehsAddress = null == obj[index] ? null : obj[index].toString();
        index++;
        unitWarehsCity = null == obj[index] ? null : obj[index].toString();
        index++;
        cfsWarehsNo = null == obj[index] ? null : obj[index].toString();
        index++;
        cfsWarehsName = null == obj[index] ? null : obj[index].toString();
        index++;
        cfsWarehsAddress = null == obj[index] ? null : obj[index].toString();
        index++;
        cfsWarehsCity = null == obj[index] ? null : obj[index].toString();
        index++;
        dispoCode = null == obj[index] ? null : obj[index].toString();
        index++;
        dispoDesc = null == obj[index] ? null : obj[index].toString();
        index++;
        arRedInvoiceId = null == obj[index] ? null : obj[index].toString();
        index++;
        ssRemarks = null == obj[index] ? null : obj[index].toString();
        index++;
    }

    public String getHeaderId() {
        return headerId;
    }

    public void setHeaderId(String headerId) {
        this.headerId = headerId;
    }

    public String getScheduleNo() {
        return scheduleNo;
    }

    public void setScheduleNo(String scheduleNo) {
        this.scheduleNo = scheduleNo;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColoaderAcctName() {
        return coloaderAcctName;
    }

    public void setColoaderAcctName(String coloaderAcctName) {
        this.coloaderAcctName = coloaderAcctName;
    }

    public String getColoaderAcctNo() {
        return coloaderAcctNo;
    }

    public void setColoaderAcctNo(String coloaderAcctNo) {
        this.coloaderAcctNo = coloaderAcctNo;
    }

    public String getMasterBl() {
        return masterBl;
    }

    public void setMasterBl(String masterBl) {
        this.masterBl = masterBl;
    }

    public String getUnitNo() {
        return unitNo;
    }

    public void setUnitNo(String unitNo) {
        this.unitNo = unitNo;
    }

    public String getUnitstatus() {
        return unitstatus;
    }

    public void setUnitstatus(String unitstatus) {
        this.unitstatus = unitstatus;
    }

    public String getEdi() {
        return edi;
    }

    public void setEdi(String edi) {
        this.edi = edi;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getUnitSsId() {
        return unitSsId;
    }

    public void setUnitSsId(String unitSsId) {
        this.unitSsId = unitSsId;
    }

    public String getOriginUnlocCode() {
        return originUnlocCode;
    }

    public void setOriginUnlocCode(String originUnlocCode) {
        this.originUnlocCode = originUnlocCode;
    }

    public String getCfsWarehsName() {
        return cfsWarehsName;
    }

    public void setCfsWarehsName(String cfsWarehsName) {
        this.cfsWarehsName = cfsWarehsName;
    }

    public String getCfsWarehsNo() {
        return cfsWarehsNo;
    }

    public void setCfsWarehsNo(String cfsWarehsNo) {
        this.cfsWarehsNo = cfsWarehsNo;
    }

    public String getDispoCode() {
        return dispoCode;
    }

    public void setDispoCode(String dispoCode) {
        this.dispoCode = dispoCode;
    }

    public String getDispoDesc() {
        return dispoDesc;
    }

    public void setDispoDesc(String dispoDesc) {
        this.dispoDesc = dispoDesc;
    }

    public String getItNumber() {
        return itNumber;
    }

    public void setItNumber(String itNumber) {
        this.itNumber = itNumber;
    }

    public String getUnitWarehsName() {
        return unitWarehsName;
    }

    public void setUnitWarehsName(String unitWarehsName) {
        this.unitWarehsName = unitWarehsName;
    }

    public String getUnitWarehsNo() {
        return unitWarehsNo;
    }

    public void setUnitWarehsNo(String unitWarehsNo) {
        this.unitWarehsNo = unitWarehsNo;
    }

    public String getArRedInvoiceId() {
        return arRedInvoiceId;
    }

    public void setArRedInvoiceId(String arRedInvoiceId) {
        this.arRedInvoiceId = arRedInvoiceId;
    }

    public int compareTo(ImportUnitsBean o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getCfsWarehsAddress() {
        return cfsWarehsAddress;
    }

    public void setCfsWarehsAddress(String cfsWarehsAddress) {
        this.cfsWarehsAddress = cfsWarehsAddress;
    }

    public String getUnitWarehsAddress() {
        return unitWarehsAddress;
    }

    public void setUnitWarehsAddress(String unitWarehsAddress) {
        this.unitWarehsAddress = unitWarehsAddress;
    }

    public String getCfsWarehsCity() {
        return cfsWarehsCity;
    }

    public void setCfsWarehsCity(String cfsWarehsCity) {
        this.cfsWarehsCity = cfsWarehsCity;
    }

    public String getUnitWarehsCity() {
        return unitWarehsCity;
    }

    public void setUnitWarehsCity(String unitWarehsCity) {
        this.unitWarehsCity = unitWarehsCity;
    }

    public String getSsRemarks() {
        return ssRemarks;
    }

    public void setSsRemarks(String ssRemarks) {
        this.ssRemarks = ssRemarks;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getColoaderAddress() {
        return coloaderAddress;
    }

    public void setColoaderAddress(String coloaderAddress) {
        this.coloaderAddress = coloaderAddress;
    }

    public String getColoaderCity() {
        return coloaderCity;
    }

    public void setColoaderCity(String coloaderCity) {
        this.coloaderCity = coloaderCity;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ImportUnitsBean other = (ImportUnitsBean) obj;
        if ((this.getUnitId() == null) ? (other.getUnitId() != null) : !this.getUnitId().equals(other.getUnitId())) {
            return false;
        }
        return true;
    }
}
