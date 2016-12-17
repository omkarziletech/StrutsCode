/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.lcl.model;

/**
 *
 * @author Mei
 */
public class LclHazmatModel {

    private String fileNo;
    private String fileId;
    private String bkgHazmatId;
    private String bkgPieceHazmatId;
    private String unHazmatNo;
    private String shippingName;
    private String technicalName;
    private String priclassCode;

    public String getBkgHazmatId() {
        return bkgHazmatId;
    }

    public void setBkgHazmatId(String bkgHazmatId) {
        this.bkgHazmatId = bkgHazmatId;
    }

    public String getBkgPieceHazmatId() {
        return bkgPieceHazmatId;
    }

    public void setBkgPieceHazmatId(String bkgPieceHazmatId) {
        this.bkgPieceHazmatId = bkgPieceHazmatId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    public String getPriclassCode() {
        return priclassCode;
    }

    public void setPriclassCode(String priclassCode) {
        this.priclassCode = priclassCode;
    }

    public String getShippingName() {
        return shippingName;
    }

    public void setShippingName(String shippingName) {
        this.shippingName = shippingName;
    }

    public String getTechnicalName() {
        return technicalName;
    }

    public void setTechnicalName(String technicalName) {
        this.technicalName = technicalName;
    }

    public String getUnHazmatNo() {
        return unHazmatNo;
    }

    public void setUnHazmatNo(String unHazmatNo) {
        this.unHazmatNo = unHazmatNo;
    }
}
