package com.gp.cong.lcl.webservices.dom;

public class Destination {

    private String zipCode = "";
    private String state = "";
    private String city = "";

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return this.zipCode;
    }

    public String getState() {
        return this.state;
    }

    public String getCity() {
        return this.city;
    }
}
