package com.gp.cong.logisoft.domain;

import java.io.Serializable;
import java.util.Date;

public class LCLColoadDocumentChargesHistory implements Serializable {

    private Integer id;
    private Integer lclCoLoadId;
    private GenericCode chargeCode;
    private Double amount;
    private Integer index;
    private Date changedDate;
    private String whoChanged;

    public Date getChangedDate() {
        return changedDate;
    }

    public void setChangedDate(Date changedDate) {
        this.changedDate = changedDate;
    }

    public String getWhoChanged() {
        return whoChanged;
    }

    public void setWhoChanged(String whoChanged) {
        this.whoChanged = whoChanged;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public GenericCode getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(GenericCode chargeCode) {
        this.chargeCode = chargeCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLclCoLoadId() {
        return lclCoLoadId;
    }

    public void setLclCoLoadId(Integer lclCoLoadId) {
        this.lclCoLoadId = lclCoLoadId;
    }
}
