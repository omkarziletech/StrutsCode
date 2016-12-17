package com.gp.cong.logisoft.beans;

import java.io.Serializable;

public class UNLocationBean implements Serializable {

    private String cityValue;
    private String countryValue;
    private Integer unlocationId;
    private Long detailId;
    private boolean addOrRemove=true;

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

}
