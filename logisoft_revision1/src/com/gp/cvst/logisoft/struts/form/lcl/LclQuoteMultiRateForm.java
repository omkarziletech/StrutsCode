/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.struts.form.lcl;

/**
 *
 * @author Thamizh
 */
public class LclQuoteMultiRateForm extends LogiwareActionForm {

    /*private String trmnum;
    private String trmnam;
    private String state;*/
    private String index;
    //private String distance;
    private String origin;
    private String doorOrigin;
    private String destination;
    private String destinationValue;
    private String fd;
    private Long fileNumberId;
    private String rateType;
    private String zip;
    private String moduleId;
    private Boolean cfcl;

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }
   
    /*
    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getTrmnam() {
        return trmnam;
    }

    public void setTrmnam(String trmnam) {
        this.trmnam = trmnam;
    }

    public String getTrmnum() {
        return trmnum;
    }

    public void setTrmnum(String trmnum) {
        this.trmnum = trmnum;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
*/


    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDoorOrigin() {
        return doorOrigin;
    }

    public void setDoorOrigin(String doorOrigin) {
        this.doorOrigin = doorOrigin;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getFd() {
        return fd;
    }

    public void setFd(String fd) {
        this.fd = fd;
    }

    public Long getFileNumberId() {
        return fileNumberId;
    }

    public void setFileNumberId(Long fileNumberId) {
        this.fileNumberId = fileNumberId;
    }

    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    public String getDestinationValue() {
        return destinationValue;
    }

    public void setDestinationValue(String destinationValue) {
        this.destinationValue = destinationValue;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public Boolean getCfcl() {
        return cfcl;
    }

    public void setCfcl(Boolean cfcl) {
        this.cfcl = cfcl;
    }
 }
