package com.gp.cong.logisoft.bc.fcl;

import com.gp.cvst.logisoft.struts.form.QuotesForm;
import com.gp.cvst.logisoft.struts.form.SearchQuotationForm;
import java.io.Serializable;

public class QuotationDTO implements Serializable{

    private String noOfDays;
    private String originCheck;
    private String isTerminal;
    private String polCheck;
    private String rampCheck;
    private String placeofReceipt;
    private String podCheck;
    private String finalDestination;
    private String destinationCheck;
    private String portofDischarge;
    private String sscode;
    private String ssline;
    private String commcode;
    private String description;
    private String rampCity;
    private String typeOfMove;
    private String selectedCheck;
    private String selectedOrigin;
    private String selectedDestination;
    private String selectedComCode;
    private String hazmat;
    private String soc;
    private String spclEqpmt;
    private String buttonValue;
    private String imsTrucker;
    private String imsBuy;
    private String imsSell;
    private String imsQuoteNo;
    private String imsLocation;
    private String bulletRatesCheck;

    public String getButtonValue() {
        return buttonValue;
    }

    public void setButtonValue(String buttonValue) {
        this.buttonValue = buttonValue;
    }

    public QuotationDTO(QuotesForm quotesForm) {
        this.imsTrucker = quotesForm.getImsTrucker();
        this.imsBuy = quotesForm.getImsBuy();
        this.imsSell = quotesForm.getImsSell();
        this.imsQuoteNo = quotesForm.getImsQuoteNo();
        this.imsLocation = quotesForm.getImsLocation();
    }

    public QuotationDTO(SearchQuotationForm searchQuotationForm) {
        this.imsTrucker = searchQuotationForm.getImsTrucker();
        this.imsBuy = searchQuotationForm.getImsBuy();
        this.imsSell = searchQuotationForm.getImsSell();
        this.imsQuoteNo = searchQuotationForm.getImsQuoteNo();
        this.imsLocation = searchQuotationForm.getImsLocation();
    }

    public QuotationDTO() {
    }

    public QuotationDTO(String noOfDays, String isTerminal, String placeOfReceipt, String comCode) {
        this.noOfDays = noOfDays;
        this.isTerminal = isTerminal;
        this.placeofReceipt = placeOfReceipt;
        this.commcode = comCode;
    }

    public String getCommcode() {
        return commcode;
    }

    public void setCommcode(String commcode) {
        this.commcode = commcode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDestinationCheck() {
        return destinationCheck;
    }

    public void setDestinationCheck(String destinationCheck) {
        this.destinationCheck = destinationCheck;
    }

    public String getFinalDestination() {
        return finalDestination;
    }

    public void setFinalDestination(String finalDestination) {
        this.finalDestination = finalDestination;
    }

    public String getIsTerminal() {
        return isTerminal;
    }

    public void setIsTerminal(String isTerminal) {
        this.isTerminal = isTerminal;
    }

    public String getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(String noOfDays) {
        this.noOfDays = noOfDays;
    }

    public String getOriginCheck() {
        return originCheck;
    }

    public void setOriginCheck(String originCheck) {
        this.originCheck = originCheck;
    }

    public String getPlaceofReceipt() {
        return placeofReceipt;
    }

    public void setPlaceofReceipt(String placeofReceipt) {
        this.placeofReceipt = placeofReceipt;
    }

    public String getPodCheck() {
        return podCheck;
    }

    public void setPodCheck(String podCheck) {
        this.podCheck = podCheck;
    }

    public String getPolCheck() {
        return polCheck;
    }

    public void setPolCheck(String polCheck) {
        this.polCheck = polCheck;
    }

    public String getPortofDischarge() {
        return portofDischarge;
    }

    public void setPortofDischarge(String portofDischarge) {
        this.portofDischarge = portofDischarge;
    }

    public String getSscode() {
        return sscode;
    }

    public void setSscode(String sscode) {
        this.sscode = sscode;
    }

    public String getSsline() {
        return ssline;
    }

    public void setSsline(String ssline) {
        this.ssline = ssline;
    }

    public String getRampCity() {
        return rampCity;
    }

    public void setRampCity(String rampCity) {
        this.rampCity = rampCity;
    }

    public String getTypeOfMove() {
        return typeOfMove;
    }

    public void setTypeOfMove(String typeOfMove) {
        this.typeOfMove = typeOfMove;
    }

    public String getSelectedCheck() {
        return selectedCheck;
    }

    public void setSelectedCheck(String selectedCheck) {
        this.selectedCheck = selectedCheck;
    }

    public String getSelectedComCode() {
        return selectedComCode;
    }

    public void setSelectedComCode(String selectedComCode) {
        this.selectedComCode = selectedComCode;
    }

    public String getSelectedDestination() {
        return selectedDestination;
    }

    public void setSelectedDestination(String selectedDestination) {
        this.selectedDestination = selectedDestination;
    }

    public String getSelectedOrigin() {
        return selectedOrigin;
    }

    public void setSelectedOrigin(String selectedOrigin) {
        this.selectedOrigin = selectedOrigin;
    }

    public String getHazmat() {
        return hazmat;
    }

    public void setHazmat(String hazmat) {
        this.hazmat = hazmat;
    }

    public String getSoc() {
        return soc;
    }

    public void setSoc(String soc) {
        this.soc = soc;
    }

    public String getSpclEqpmt() {
        return spclEqpmt;
    }

    public void setSpclEqpmt(String spclEqpmt) {
        this.spclEqpmt = spclEqpmt;
    }

    public String getRampCheck() {
        return rampCheck;
    }

    public void setRampCheck(String rampCheck) {
        this.rampCheck = rampCheck;
    }

    public String getImsBuy() {
        return imsBuy;
    }

    public void setImsBuy(String imsBuy) {
        this.imsBuy = imsBuy;
    }

    public String getImsLocation() {
        return imsLocation;
    }

    public void setImsLocation(String imsLocation) {
        this.imsLocation = imsLocation;
    }

    public String getImsQuoteNo() {
        return imsQuoteNo;
    }

    public void setImsQuoteNo(String imsQuoteNo) {
        this.imsQuoteNo = imsQuoteNo;
    }

    public String getImsSell() {
        return imsSell;
    }

    public void setImsSell(String imsSell) {
        this.imsSell = imsSell;
    }

    public String getImsTrucker() {
        return imsTrucker;
    }

    public void setImsTrucker(String imsTrucker) {
        this.imsTrucker = imsTrucker;
    }

    public String getBulletRatesCheck() {
        return bulletRatesCheck;
    }

    public void setBulletRatesCheck(String bulletRatesCheck) {
        this.bulletRatesCheck = bulletRatesCheck;
    }

}
