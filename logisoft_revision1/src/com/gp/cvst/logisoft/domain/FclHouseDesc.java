package com.gp.cvst.logisoft.domain;

import java.io.Serializable;

public class FclHouseDesc implements Serializable {

    private Integer id;
    private String bolId;
    private String trailerId;
    private String mastDesc;

    public String getBolId() {
        return bolId;
    }

    public void setBolId(String bolId) {
        this.bolId = bolId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMastDesc() {
        return mastDesc;
    }

    public void setMastDesc(String mastDesc) {
        this.mastDesc = mastDesc;
    }

    public String getTrailerId() {
        return trailerId;
    }

    public void setTrailerId(String trailerId) {
        this.trailerId = trailerId;
    }
}
