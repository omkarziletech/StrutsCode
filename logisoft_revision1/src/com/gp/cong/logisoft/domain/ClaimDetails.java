package com.gp.cong.logisoft.domain;

import java.io.Serializable;
import java.util.Date;

public class ClaimDetails implements Serializable {

    private Integer id;
    private String carriercode;
    private String masterAwbNo;
    private String portNumber;
    private Date flightDate;
    private String hazardous;
    private String claim;
    private Date claimDate;
    private Integer index;
    private Integer airlineid;

    public Integer getAirlineid() {
        return airlineid;
    }

    public void setAirlineid(Integer airlineid) {
        this.airlineid = airlineid;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getClaim() {
        return claim;
    }

    public void setClaim(String claim) {
        this.claim = claim;
    }

    public Date getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(Date claimDate) {
        this.claimDate = claimDate;
    }

    public Date getFlightDate() {
        return flightDate;
    }

    public void setFlightDate(Date flightDate) {
        this.flightDate = flightDate;
    }

    public String getHazardous() {
        return hazardous;
    }

    public void setHazardous(String hazardous) {
        this.hazardous = hazardous;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(String portNumber) {
        this.portNumber = portNumber;
    }

    public String getCarriercode() {
        return carriercode;
    }

    public void setCarriercode(String carriercode) {
        this.carriercode = carriercode;
    }

    public String getMasterAwbNo() {
        return masterAwbNo;
    }

    public void setMasterAwbNo(String masterAwbNo) {
        this.masterAwbNo = masterAwbNo;
    }
}
