/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.struts.form.lcl;

/**
 *
 * @author Administrator
 */
public class CommodityCodeForm extends LogiwareActionForm {

    private String methodName;
    private String id;
    private String code;
    private Boolean active;
    private String descEn;
    private Boolean hazmat;
    private Boolean highVolumeDiscount;
    private Boolean refrigerationRequired;
    private Boolean defaultErt;
    private String remarks;

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getDefaultErt() {
        return defaultErt;
    }

    public void setDefaultErt(Boolean defaultErt) {
        this.defaultErt = defaultErt;
    }

    public String getDescEn() {
        return descEn;
    }

    public void setDescEn(String descEn) {
        this.descEn = descEn;
    }

    public Boolean getHazmat() {
        return hazmat;
    }

    public void setHazmat(Boolean hazmat) {
        this.hazmat = hazmat;
    }

    public Boolean getHighVolumeDiscount() {
        return highVolumeDiscount;
    }

    public void setHighVolumeDiscount(Boolean highVolumeDiscount) {
        this.highVolumeDiscount = highVolumeDiscount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getRefrigerationRequired() {
        return refrigerationRequired;
    }

    public void setRefrigerationRequired(Boolean refrigerationRequired) {
        this.refrigerationRequired = refrigerationRequired;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
