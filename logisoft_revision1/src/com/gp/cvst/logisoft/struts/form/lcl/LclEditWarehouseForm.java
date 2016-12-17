package com.gp.cvst.logisoft.struts.form.lcl;

public class LclEditWarehouseForm extends LogiwareActionForm {

    private Long unitId;
    private String warehouseName;
    private String disposition;
    private String location;
    private String arrivedDateTime;
    private String departedDateTime;
    private Integer warehouseId;
    private Long unitWarehouseId;
    private String sealNoIn;
    private String sealNoOut;
    private String strippedDate;
    private Long headerId;
    private String unitNo;

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }
   

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getDisposition() {
        return disposition;
    }

    public void setDisposition(String disposition) {
        this.disposition = disposition;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getArrivedDateTime() {
        return arrivedDateTime;
    }

    public void setArrivedDateTime(String arrivedDateTime) {
        this.arrivedDateTime = arrivedDateTime;
    }

    public String getDepartedDateTime() {
        return departedDateTime;
    }

    public void setDepartedDateTime(String departedDateTime) {
        this.departedDateTime = departedDateTime;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Long getUnitWarehouseId() {
        return unitWarehouseId;
    }

    public void setUnitWarehouseId(Long unitWarehouseId) {
        this.unitWarehouseId = unitWarehouseId;
    }

    public String getSealNoIn() {
        return sealNoIn;
    }

    public void setSealNoIn(String sealNoIn) {
        this.sealNoIn = sealNoIn;
    }

    public String getSealNoOut() {
        return sealNoOut;
    }

    public void setSealNoOut(String sealNoOut) {
        this.sealNoOut = sealNoOut;
    }

    public String getStrippedDate() {
        return strippedDate;
    }

    public void setStrippedDate(String strippedDate) {
        this.strippedDate = strippedDate;
    }

    public Long getHeaderId() {
        return headerId;
    }

    public void setHeaderId(Long headerId) {
        this.headerId = headerId;
    }
   
}
