/**
 *
 */
package com.gp.cong.logisoft.domain;

import java.io.Serializable;
import java.util.Set;

public class Ports implements Auditable, Serializable {

    private static final long serialVersionUID = 1L;
    private String shedulenumber;
    private String piercode;
    private String portname;
    private String eciportcode;
    private String type;
    private String isocode;
    //this we are not using in hbm file
    private UnLocation uncitycode;
    private String unCode;
    private String cityname;
    private String countryName;
    private GenericCode regioncode;
    private Set lclPortConfigSet;
    private Set fclPortConfigSet;
    private Set airPortConfigSet;
    private Set importConfigSet;
    private Set agencyInfoSet;
    private String controlNo;
    private Integer id;
    private String drABBR;
    private String oceanOriginService;
    private String oceanDestinationService;
    private Ports rateFromPierCode;
    private String ratePortName;
    private String rateControlNo;
    private String start;
    private String stateCode;
    private String unLocationCode;
    private String portCity;
    private String engmet;
    private String hscode;
    private String ncmno;
    private String omit2LetterCountryCode;

    public String getNcmno() {
        return ncmno;
    }

    public void setNcmno(String ncmno) {
        this.ncmno = ncmno;
    }

    public String getPortCity() {
        return portCity;
    }

    public void setPortCity(String portCity) {
        this.portCity = portCity;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getRatePortName() {
        return ratePortName;
    }

    public void setRatePortName(String ratePortName) {
        this.ratePortName = ratePortName;
    }

    public String getRateControlNo() {
        return rateControlNo;
    }

    public void setRateControlNo(String rateControlNo) {
        this.rateControlNo = rateControlNo;
    }

    public Ports getRateFromPierCode() {
        return rateFromPierCode;
    }

    public void setRateFromPierCode(Ports rateFromPierCode) {
        this.rateFromPierCode = rateFromPierCode;
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

    public Set getImportConfigSet() {
        return importConfigSet;
    }

    public void setImportConfigSet(Set importConfigSet) {
        this.importConfigSet = importConfigSet;
    }

    public Set getAirPortConfigSet() {
        return airPortConfigSet;
    }

    public void setAirPortConfigSet(Set airPortConfigSet) {
        this.airPortConfigSet = airPortConfigSet;
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

    public GenericCode getRegioncode() {
        return regioncode;
    }

    public void setRegioncode(GenericCode regioncode) {
        this.regioncode = regioncode;
    }
    /*public String getShedulenumber() {
     return shedulenumber;
     }
     public void setShedulenumber(String ) {
     this.shedulenumber = shedulenumber;
     }*/

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

    public Set getLclPortConfigSet() {
        return lclPortConfigSet;
    }

    public void setLclPortConfigSet(Set lclPortConfigSet) {
        this.lclPortConfigSet = lclPortConfigSet;
    }

    public Set getFclPortConfigSet() {
        return fclPortConfigSet;
    }

    public void setFclPortConfigSet(Set fclPortConfigSet) {
        this.fclPortConfigSet = fclPortConfigSet;
    }

    public Set getAgencyInfoSet() {
        return agencyInfoSet;
    }

    public void setAgencyInfoSet(Set agencyInfoSet) {
        this.agencyInfoSet = agencyInfoSet;
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
    /*public Ports getSchNum() {
     return SchNum;
     }
     public void setSchNum(Ports schNum) {
     SchNum = schNum;
     }*/

    public String getShedulenumber() {
        return shedulenumber;
    }

    public void setShedulenumber(String shedulenumber) {
        this.shedulenumber = shedulenumber;
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

    public String getEngmet() {
        return engmet;
    }

    public void setEngmet(String engmet) {
        this.engmet = engmet;
    }

    public String getHscode() {
        return hscode;
    }

    public void setHscode(String hscode) {
        this.hscode = hscode;
    }

    public String getOmit2LetterCountryCode() {
        return omit2LetterCountryCode;
    }

    public void setOmit2LetterCountryCode(String omit2LetterCountryCode) {
        this.omit2LetterCountryCode = omit2LetterCountryCode;
    }
}
