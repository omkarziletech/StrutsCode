/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.struts.form;
import org.apache.struts.action.ActionForm;

/**
 *
 * @author Logiware
 */
public class RateGridForm extends ActionForm {

    private String origin;
    private String destination;
    private String commodity;
    private String action;
    private String hazardous;
    private String doorOrigin;
    private String zip;
    private String fileNo;
    private String noOfDays;
    private String region;
    private String ratesFrom;
    private String distances;
    private String cityIds;
    private String addSub;
    private String addSubMarkUp;
    private String addSubCostCode;
    private String bulletRates;
    private String fileType;
    // For IMS Quote
    private String emptyLocation;
    private String chargeId;
    private String comment;
    private String sell;
    private String buy;
    private String screenName;
    private String originZip;
    private String truckerName;
    private String truckerNumber;
    private String doorDestination;

    public String getDistances() {
	return distances;
    }

    public void setDistances(String distances) {
	this.distances = distances;
    }

    public String getHazardous() {
	return hazardous;
    }

    public void setHazardous(String hazardous) {
	this.hazardous = hazardous;
    }

    public String getAction() {
	return action;
    }

    public void setAction(String action) {
	this.action = action;
    }

    public String getCommodity() {
	return commodity;
    }

    public void setCommodity(String commodity) {
	this.commodity = commodity;
    }

    public String getDestination() {
	return destination;
    }

    public void setDestination(String destination) {
	this.destination = destination;
    }

    public String getOrigin() {
	return origin;
    }

    public void setOrigin(String origin) {
	this.origin = origin;
    }

    public boolean showHazardous() {
	if ("Y".equalsIgnoreCase(hazardous)) {
	    return true;
	}
	return false;
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

    public String getFileNo() {
	return fileNo;
    }

    public void setFileNo(String fileNo) {
	this.fileNo = fileNo;
    }

    public String getNoOfDays() {
	return noOfDays;
    }

    public void setNoOfDays(String noOfDays) {
	this.noOfDays = noOfDays;
    }

    public String getRegion() {
	return region;
    }

    public void setRegion(String region) {
	this.region = region;
    }

    public String getRatesFrom() {
	return ratesFrom;
    }

    public void setRatesFrom(String ratesFrom) {
	this.ratesFrom = ratesFrom;
    }

    public String getAddSub() {
	return addSub;
    }

    public void setAddSub(String addSub) {
	this.addSub = addSub;
    }

    public String getAddSubCostCode() {
	return addSubCostCode;
    }

    public void setAddSubCostCode(String addSubCostCode) {
	this.addSubCostCode = addSubCostCode;
    }

    public String getAddSubMarkUp() {
	return addSubMarkUp;
    }

    public void setAddSubMarkUp(String addSubMarkUp) {
	this.addSubMarkUp = addSubMarkUp;
    }

    public String getCityIds() {
	return cityIds;
    }

    public void setCityIds(String cityIds) {
	this.cityIds = cityIds;
    }

    public String getEmptyLocation() {
	return emptyLocation;
    }

    public void setEmptyLocation(String emptyLocation) {
	this.emptyLocation = emptyLocation;
    }

    public String getBuy() {
	return buy;
    }

    public void setBuy(String buy) {
	this.buy = buy;
    }

    public String getChargeId() {
	return chargeId;
    }

    public void setChargeId(String chargeId) {
	this.chargeId = chargeId;
    }

    public String getComment() {
	return comment;
    }

    public void setComment(String comment) {
	this.comment = comment;
    }

    public String getSell() {
	return sell;
    }

    public void setSell(String sell) {
	this.sell = sell;
    }

    public String getScreenName() {
	return screenName;
    }

    public void setScreenName(String screenName) {
	this.screenName = screenName;
    }

    public String getOriginZip() {
	return originZip;
    }

    public void setOriginZip(String originZip) {
	this.originZip = originZip;
    }

    public String getTruckerName() {
	return truckerName;
    }

    public void setTruckerName(String truckerName) {
	this.truckerName = truckerName;
    }

    public String getTruckerNumber() {
	return truckerNumber;
    }

    public void setTruckerNumber(String truckerNumber) {
	this.truckerNumber = truckerNumber;
    }

    public String getBulletRates() {
        return bulletRates;
    }

    public void setBulletRates(String bulletRates) {
        this.bulletRates = bulletRates;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getDoorDestination() {
        return doorDestination;
    }

    public void setDoorDestination(String doorDestination) {
        this.doorDestination = doorDestination;
    }

}
