/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.beans;

import com.gp.cong.common.CommonUtils;
import java.io.Serializable;

/**
 *
 * @author Logiware
 */
public class ImportBookingBean implements Serializable {

    private String fileId;
    private String fileNumber;
    private String bookingType;
    private String fdUnCode;
    private String fdName;
    private String fdCode;
    private String dispCode;
    private String dispDesc;
    private String totalPiece;
    private Double totalWeightImperial;
    private Double totalVolumeImperial;
    private Double totalCollect;
    private Double totalIPI;
    private String shipAcct;
    private String shipName;
    private String notyAcct;
    private String notyName;
    private String consAcct;
    private String consName;
    private String bookedBy;
    private boolean transhipment;
    private String amsNo;

    public ImportBookingBean() {
    }

    public ImportBookingBean(Object[] obj) throws Exception {
        int index = 31;
        fileId = null == obj[index] ? null : obj[index].toString();
        index++;
        fileNumber = null == obj[index] ? null : obj[index].toString();
        index++;
        bookingType = null == obj[index] ? null : obj[index].toString();
        index++;
        fdUnCode = null == obj[index] ? null : obj[index].toString();
        index++;
        fdName = null == obj[index] ? null : obj[index].toString();
        index++;
        fdCode = null == obj[index] ? null : obj[index].toString();
        index++;
        dispCode = null == obj[index] ? null : obj[index].toString();
        index++;
        dispDesc = null == obj[index] ? null : obj[index].toString();
        index++;
        totalPiece = null == obj[index] ? null : obj[index].toString();
        index++;
        totalWeightImperial = null == obj[index] ? null : Double.parseDouble(obj[index].toString());
        index++;
        totalVolumeImperial = null == obj[index] ? null : Double.parseDouble(obj[index].toString());
        index++;
        totalCollect = null == obj[index] ? null : Double.parseDouble(obj[index].toString());
        index++;
        totalIPI = null == obj[index] ? null : Double.parseDouble(obj[index].toString());
        index++;
        shipAcct = null == obj[index] ? null : obj[index].toString();
        index++;
        shipName = null == obj[index] ? null : obj[index].toString();
        index++;
        notyAcct = null == obj[index] ? null : obj[index].toString();
        index++;
        notyName = null == obj[index] ? null : obj[index].toString();
        index++;
        consAcct = null == obj[index] ? null : obj[index].toString();
        index++;
        consName = null == obj[index] ? null : obj[index].toString();
        index++;
        bookedBy = null == obj[index] ? null : obj[index].toString();
        index++;
        String importTranshipment = null == obj[index] ? null : obj[index].toString();
        if (CommonUtils.isNotEmpty(importTranshipment) && importTranshipment.equalsIgnoreCase("1")) {
            transhipment = true;
        } else if ("true".equalsIgnoreCase(importTranshipment)) {
            transhipment = true;
        }
        index++;
        amsNo = null == obj[index] ? null : obj[index].toString();
    }

    public String getBookedBy() {
        return bookedBy;
    }

    public void setBookedBy(String bookedBy) {
        this.bookedBy = bookedBy;
    }

    public String getConsAcct() {
        return consAcct;
    }

    public void setConsAcct(String consAcct) {
        this.consAcct = consAcct;
    }

    public String getConsName() {
        return consName;
    }

    public void setConsName(String consName) {
        this.consName = consName;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public String getBookingType() {
        return bookingType;
    }

    public void setBookingType(String bookingType) {
        this.bookingType = bookingType;
    }

    public String getNotyAcct() {
        return notyAcct;
    }

    public void setNotyAcct(String notyAcct) {
        this.notyAcct = notyAcct;
    }

    public String getNotyName() {
        return notyName;
    }

    public void setNotyName(String notyName) {
        this.notyName = notyName;
    }

    public String getShipAcct() {
        return shipAcct;
    }

    public void setShipAcct(String shipAcct) {
        this.shipAcct = shipAcct;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public String getFdCode() {
        return fdCode;
    }

    public void setFdCode(String fdCode) {
        this.fdCode = fdCode;
    }

    public String getFdName() {
        return fdName;
    }

    public void setFdName(String fdName) {
        this.fdName = fdName;
    }

    public String getFdUnCode() {
        return fdUnCode;
    }

    public void setFdUnCode(String fdUnCode) {
        this.fdUnCode = fdUnCode;
    }

    public Double getTotalCollect() {
        return totalCollect;
    }

    public void setTotalCollect(Double totalCollect) {
        this.totalCollect = totalCollect;
    }

    public Double getTotalIPI() {
        return totalIPI;
    }

    public void setTotalIPI(Double totalIPI) {
        this.totalIPI = totalIPI;
    }

    public String getTotalPiece() {
        return totalPiece;
    }

    public void setTotalPiece(String totalPiece) {
        this.totalPiece = totalPiece;
    }

    public Double getTotalVolumeImperial() {
        return totalVolumeImperial;
    }

    public void setTotalVolumeImperial(Double totalVolumeImperial) {
        this.totalVolumeImperial = totalVolumeImperial;
    }

    public Double getTotalWeightImperial() {
        return totalWeightImperial;
    }

    public void setTotalWeightImperial(Double totalWeightImperial) {
        this.totalWeightImperial = totalWeightImperial;
    }

    public String getDispCode() {
        return dispCode;
    }

    public void setDispCode(String dispCode) {
        this.dispCode = dispCode;
    }

    public String getDispDesc() {
        return dispDesc;
    }

    public void setDispDesc(String dispDesc) {
        this.dispDesc = dispDesc;
    }

    public boolean isTranshipment() {
        return transhipment;
    }

    public void setTranshipment(boolean transhipment) {
        this.transhipment = transhipment;
    }

    public String getAmsNo() {
        return amsNo;
    }

    public void setAmsNo(String amsNo) {
        this.amsNo = amsNo;
    }
}
