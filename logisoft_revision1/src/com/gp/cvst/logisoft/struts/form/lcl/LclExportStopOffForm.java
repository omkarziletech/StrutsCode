/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.struts.form.lcl;

public class LclExportStopOffForm  extends LogiwareActionForm{

    private String methodName;
    private String landExitCity;
    private Integer landExitCityId;
    private String unLocationCode;
    private String warehouseName;
    private String warehouseNo;
    private Integer warehouseId;
    private String stopOffETD;
    private String stopOffETA;
    private String stopOffRemarks;
    private String index;
    private String headerId;
    
    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getLandExitCity() {
        return landExitCity;
    }

    public void setLandExitCity(String landExitCity) {
        this.landExitCity = landExitCity;
    }

    public Integer getLandExitCityId() {
        return landExitCityId;
    }

    public void setLandExitCityId(Integer landExitCityId) {
        this.landExitCityId = landExitCityId;
    }

    public String getUnLocationCode() {
        return unLocationCode;
    }

    public void setUnLocationCode(String unLocationCode) {
        this.unLocationCode = unLocationCode;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getStopOffETA() {
        return stopOffETA;
    }

    public void setStopOffETA(String stopOffETA) {
        this.stopOffETA = stopOffETA;
    }

    public String getStopOffETD() {
        return stopOffETD;
    }

    public void setStopOffETD(String stopOffETD) {
        this.stopOffETD = stopOffETD;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getWarehouseNo() {
        return warehouseNo;
    }

    public void setWarehouseNo(String warehouseNo) {
        this.warehouseNo = warehouseNo;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getStopOffRemarks() {
        return stopOffRemarks;
    }

    public void setStopOffRemarks(String stopOffRemarks) {
        this.stopOffRemarks = stopOffRemarks;
    }

    public String getHeaderId() {
        return headerId;
    }

    public void setHeaderId(String headerId) {
        this.headerId = headerId;
    }
}
