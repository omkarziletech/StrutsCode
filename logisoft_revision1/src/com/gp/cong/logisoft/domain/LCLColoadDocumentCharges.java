package com.gp.cong.logisoft.domain;

import java.io.Serializable;

public class LCLColoadDocumentCharges implements Serializable {

    private Integer id;
    private Integer lclCoLoadId;
    private GenericCode chargeCode;
    private Double amount;
    private Integer index;

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
