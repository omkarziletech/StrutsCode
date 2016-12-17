package com.gp.cong.logisoft.domain;

import java.io.Serializable;
import java.util.Date;

public class FclInbondDetails implements Serializable {

    private Integer id;
    private String inbondPort;
    private String inbondType;
    private String inbondNumber;
    private String fileNumber;
    private Integer bolId;
    private Date inbondDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBolId() {
        return bolId;
    }

    public void setBolId(Integer bolId) {
        this.bolId = bolId;
    }

    public Date getInbondDate() {
        return inbondDate;
    }

    public void setInbondDate(Date inbondDate) {
        this.inbondDate = inbondDate;
    }

    public String getInbondNumber() {
        return null != inbondNumber ? inbondNumber.toUpperCase() : "";
    }

    public void setInbondNumber(String inbondNumber) {
        this.inbondNumber = null != inbondNumber ? inbondNumber.toUpperCase() : "";
    }

    public String getInbondPort() {
        return inbondPort;
    }

    public String getInbondType() {
        return inbondType;
    }

    public void setInbondType(String inbondType) {
        this.inbondType = inbondType;
    }

    public void setInbondPort(String inbondPort) {
        this.inbondPort = inbondPort;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }
}
