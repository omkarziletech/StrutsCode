/**
 *
 */
package com.gp.cong.logisoft.domain;

import java.io.Serializable;

public class PortsTemp implements Auditable, Serializable {

    private static final long serialVersionUID = 1L;
    private String shedulenumber;
    private String piercode;
    private String portname;
    private String eciportcode;
    private String type;
    private String isocode;
    private UnLocation uncitycode;
    private String unCode;
    private String cityname;
    private String countryName;
    private String oceanOriginService;
    private String oceanDestinationService;
    private String stateCode;
    private String controlNo;
    private Integer id;
    private String drABBR;
    private String unLocationCode;
    private String portCity;
    private String start;
    private String omit2LetterCountryCode;

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getControlNo() {
        return controlNo;
    }

    public void setControlNo(String controlNo) {
        this.controlNo = controlNo;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getEciportcode() {
        return eciportcode;
    }

    public void setEciportcode(String eciportcode) {
        this.eciportcode = eciportcode;
    }

    public String getIsocode() {
        return isocode;
    }

    public void setIsocode(String isocode) {
        this.isocode = isocode;
    }

    public String getPiercode() {
        return piercode;
    }

    public void setPiercode(String piercode) {
        this.piercode = piercode;
    }

    public String getPortname() {
        return portname;
    }

    public void setPortname(String portname) {
        this.portname = portname;
    }

    public String getShedulenumber() {
        return shedulenumber;
    }

    public void setShedulenumber(String shedulenumber) {
        this.shedulenumber = shedulenumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UnLocation getUncitycode() {
        return uncitycode;
    }

    public void setUncitycode(UnLocation uncitycode) {
        this.uncitycode = uncitycode;
        if (this.uncitycode != null) {
            this.setUnCode(this.uncitycode.getUnLocationCode());
        }
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public AuditInfo getAuditInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    public Integer getId() {
        return id;
    }

    public String getDrABBR() {
        return drABBR;
    }

    public void setDrABBR(String drABBR) {
        this.drABBR = drABBR;
    }

    public String getUnCode() {
        return unCode;
    }

    public void setUnCode(String unCode) {
        this.unCode = unCode;
    }

    public String getUnLocationCode() {
        return unLocationCode;
    }

    public void setUnLocationCode(String unLocationCode) {
        this.unLocationCode = unLocationCode;
        uncitycode = new UnLocation();
        uncitycode.setUnLocationCode(unLocationCode);
    }

    public String getOceanDestinationService() {
        return oceanDestinationService;
    }

    public void setOceanDestinationService(String oceanDestinationService) {
        this.oceanDestinationService = oceanDestinationService;
    }

    public String getOceanOriginService() {
        return oceanOriginService;
    }

    public void setOceanOriginService(String oceanOriginService) {
        this.oceanOriginService = oceanOriginService;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getPortCity() {
        return portCity;
    }

    public void setPortCity(String portCity) {
        this.portCity = portCity;
    }

    public String getOmit2LetterCountryCode() {
        return omit2LetterCountryCode;
    }

    public void setOmit2LetterCountryCode(String omit2LetterCountryCode) {
        this.omit2LetterCountryCode = omit2LetterCountryCode;
    }
}
