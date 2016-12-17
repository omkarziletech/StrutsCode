package com.gp.cong.logisoft.domain;

import java.io.Serializable;

public class CustomerAssociation implements Serializable {

    private Integer id;
    private String customerid;
    private String associd;
    private String checkbox;

    public String getCheckbox() {
        return checkbox;
    }

    public String getAssocid() {
        return associd;
    }

    public void setAssocid(String associd) {
        this.associd = associd;
    }

    public void setCheckbox(String checkbox) {
        this.checkbox = checkbox;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
