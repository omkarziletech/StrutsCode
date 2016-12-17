package com.gp.cong.logisoft.beans;

import java.io.Serializable;

public class RoutingOriginBean implements Serializable {

    public RoutingOriginBean(Object[] obj, Double miles) {
        int index = 0;
        trmnum = null == obj[index] ? null : obj[index].toString();
        index++;
        portname = null == obj[index] ? null : obj[index].toString();
        index++;
        statecode = null == obj[index] ? null : obj[index].toString();
        index++;
        if(obj[index]!=null && !obj[index].toString().trim().equals("") && !obj[index].toString().trim().equals("0"))
        {
            unLocationId = Integer.parseInt(obj[index].toString());
        }
        
        this.miles = miles;
    }
    private String trmnum;
    private String portname;
    private String statecode;
    private Double miles;
    private Integer unLocationId;
    private String unLocationcode;

    public Double getMiles() {
        return miles;
    }

    public void setMiles(Double miles) {
        this.miles = miles;
    }

    public String getPortname() {
        return portname;
    }

    public void setPortname(String portname) {
        this.portname = portname;
    }

    public String getStatecode() {
        return statecode;
    }

    public void setStatecode(String statecode) {
        this.statecode = statecode;
    }

    public String getTrmnum() {
        return trmnum;
    }

    public void setTrmnum(String trmnum) {
        this.trmnum = trmnum;
    }

    public Integer getUnLocationId() {
        return unLocationId;
    }

    public void setUnLocationId(Integer unLocationId) {
        this.unLocationId = unLocationId;
    }

    public String getUnLocationcode() {
        return unLocationcode;
    }

    public void setUnLocationcode(String unLocationcode) {
        this.unLocationcode = unLocationcode;
    }
    
    
}
