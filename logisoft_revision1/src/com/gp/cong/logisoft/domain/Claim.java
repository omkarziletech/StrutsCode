package com.gp.cong.logisoft.domain;

import java.io.Serializable;

public class Claim implements Serializable {

    private Integer id;
    private String airlinecode;
    private String awbno;

    public String getAirlinecode() {
        return airlinecode;
    }

    public void setAirlinecode(String airlinecode) {
        this.airlinecode = airlinecode;
    }

    public String getAwbno() {
        return awbno;
    }

    public void setAwbno(String awbno) {
        this.awbno = awbno;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
