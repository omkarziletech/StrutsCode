package com.gp.cong.lcl.webservices.dom;

public class Rates {

    private String error;
    private Origin origin;
    private Destination destination;
    private String miles;
    private Classes classes;
    private Weights weights;
    private String shipmentId;
    private String cubicFeet;

    public Classes getClasses() {
        return classes;
    }

    public void setClasses(Classes classes) {
        this.classes = classes;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Origin getOrigin() {
        return origin;
    }

    public void setOrigin(Origin origin) {
        this.origin = origin;
    }

    public Weights getWeights() {
        return weights;
    }

    public void setWeights(Weights weights) {
        this.weights = weights;
    }

    public String getMiles() {
        return miles;
    }

    public void setMiles(String miles) {
        this.miles = miles;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getCubicFeet() {
        return cubicFeet;
    }

    public void setCubicFeet(String cubicFeet) {
        this.cubicFeet = cubicFeet;
    }
    
}
