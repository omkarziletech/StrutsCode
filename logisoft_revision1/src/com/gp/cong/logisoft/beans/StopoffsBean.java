/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.beans;

import java.io.Serializable;
import java.util.Date;

public class StopoffsBean implements Serializable  {

    private String cityValue;
    private String countryValue;
    private Integer unlocationId;
    private Long detailId;
    private boolean addOrRemove = true;
    private Date stdDate;
    private Date staDate;
    private String wareHouse;
    private Long warehouseId;
    private String remarks;


    public String getCityValue() {
        return cityValue;
    }

    public void setCityValue(String cityValue) {
        this.cityValue = cityValue;
    }

    public String getCountryValue() {
        return countryValue;
    }

    public void setCountryValue(String countryValue) {
        this.countryValue = countryValue;
    }

    public Integer getUnlocationId() {
        return unlocationId;
    }

    public void setUnlocationId(Integer unlocationId) {
        this.unlocationId = unlocationId;
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public boolean isAddOrRemove() {
        return addOrRemove;
    }

    public void setAddOrRemove(boolean addOrRemove) {
        this.addOrRemove = addOrRemove;
    }

    public Date getStaDate() {
        return staDate;
    }

    public void setStaDate(Date staDate) {
        this.staDate = staDate;
    }

    public Date getStdDate() {
        return stdDate;
    }

    public void setStdDate(Date stdDate) {
        this.stdDate = stdDate;
    }

    public String getWareHouse() {
        return wareHouse;
    }

    public void setWareHouse(String wareHouse) {
        this.wareHouse = wareHouse;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }
    
}
