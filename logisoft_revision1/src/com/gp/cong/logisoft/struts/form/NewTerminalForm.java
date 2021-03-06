/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * MyEclipse Struts Creation date: 11-30-2007
 *
 * XDoclet definition:
 *
 * @struts.form name="newTerminalForm"
 */
public class NewTerminalForm extends ActionForm {
    /*
     * Generated Methods
     */

    private String termNo;
    private String terminalLocation;
    private String generalLedger;
    private String careof;
    private String scheduleSuffix;
    private String importsContacts;
    private String name;
    private String terminalType;
    private String country;
    private String city;
    private String state;
    private String addressLine1;
    private String addressLine2;
    private String zip;
    private String phoneNo1;
    private String phoneNo2;
    private String phoneNo3;
    private String faxNo1;
    private String faxNo2;
    private String faxNo3;
    private String faxNo4;
    private String faxNo5;
    private String chargeCode;
    private String brlChargeCode;
    private String ovr10kChgCode;
    private String ovr20kChgCode;
    private String docChargeCode;
    private String printerModel;
    private String acf;
    private String govSchCode;
    private String notes;
    private String buttonValue;
    private String airsrvc;
    private String extension1;
    private String extension2;
    private String extension3;
    private String unLocCode;
    private String unLocationCode1;
    private String acctno;
    private String exportsBillingTerminalEmail;
    private String docDeptEmail;
    private String fclExportIssuingTerminal;
    
    public String getAcctno() {
        return acctno;
    }

    public void setAcctno(String acctno) {
        this.acctno = acctno;
    }

    public String getUnLocationCode1() {
        return unLocationCode1;
    }

    public void setUnLocationCode1(String unLocationCode1) {
        this.unLocationCode1 = unLocationCode1;
    }

    public String getUnLocCode() {
        return unLocCode;
    }

    public void setUnLocCode(String unLocCode) {
        this.unLocCode = unLocCode;
    }

    public String getAirsrvc() {
        return airsrvc;
    }

    public void setAirsrvc(String airsrvc) {
        this.airsrvc = airsrvc;
    }

    public String getButtonValue() {
        return buttonValue;
    }

    public void setButtonValue(String buttonValue) {
        this.buttonValue = buttonValue;
    }

    /**
     * Method validate
     *
     * @param mapping
     * @param request
     * @return ActionErrors
     */
    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Method reset
     *
     * @param mapping
     * @param request
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        // TODO Auto-generated method stub
    }

    public String getAcf() {
        return acf;
    }

    public void setAcf(String acf) {
        this.acf = acf;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getBrlChargeCode() {
        return brlChargeCode;
    }

    public void setBrlChargeCode(String brlChargeCode) {
        this.brlChargeCode = brlChargeCode;
    }

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDocChargeCode() {
        return docChargeCode;
    }

    public void setDocChargeCode(String docChargeCode) {
        this.docChargeCode = docChargeCode;
    }

    public String getFaxNo1() {
        return faxNo1;
    }

    public void setFaxNo1(String faxNo1) {
        this.faxNo1 = faxNo1;
    }

    public String getFaxNo2() {
        return faxNo2;
    }

    public void setFaxNo2(String faxNo2) {
        this.faxNo2 = faxNo2;
    }

    public String getFaxNo3() {
        return faxNo3;
    }

    public void setFaxNo3(String faxNo3) {
        this.faxNo3 = faxNo3;
    }

    public String getFaxNo4() {
        return faxNo4;
    }

    public void setFaxNo4(String faxNo4) {
        this.faxNo4 = faxNo4;
    }

    public String getFaxNo5() {
        return faxNo5;
    }

    public void setFaxNo5(String faxNo5) {
        this.faxNo5 = faxNo5;
    }

    public String getGovSchCode() {
        return govSchCode;
    }

    public void setGovSchCode(String govSchCode) {
        this.govSchCode = govSchCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getOvr10kChgCode() {
        return ovr10kChgCode;
    }

    public void setOvr10kChgCode(String ovr10kChgCode) {
        this.ovr10kChgCode = ovr10kChgCode;
    }

    public String getOvr20kChgCode() {
        return ovr20kChgCode;
    }

    public void setOvr20kChgCode(String ovr20kChgCode) {
        this.ovr20kChgCode = ovr20kChgCode;
    }

    public String getPhoneNo1() {
        return phoneNo1;
    }

    public void setPhoneNo1(String phoneNo1) {
        this.phoneNo1 = phoneNo1;
    }

    public String getPhoneNo2() {
        return phoneNo2;
    }

    public void setPhoneNo2(String phoneNo2) {
        this.phoneNo2 = phoneNo2;
    }

    public String getPhoneNo3() {
        return phoneNo3;
    }

    public void setPhoneNo3(String phoneNo3) {
        this.phoneNo3 = phoneNo3;
    }

    public String getPrinterModel() {
        return printerModel;
    }

    public void setPrinterModel(String printerModel) {
        this.printerModel = printerModel;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getTermNo() {
        return termNo;
    }

    public void setTermNo(String termNo) {
        this.termNo = termNo;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCareof() {
        return careof;
    }

    public void setCareof(String careof) {
        this.careof = careof;
    }

    public String getGeneralLedger() {
        return generalLedger;
    }

    public void setGeneralLedger(String generalLedger) {
        this.generalLedger = generalLedger;
    }

    public String getImportsContacts() {
        return importsContacts;
    }

    public void setImportsContacts(String importsContacts) {
        this.importsContacts = importsContacts;
    }

    public String getScheduleSuffix() {
        return scheduleSuffix;
    }

    public void setScheduleSuffix(String scheduleSuffix) {
        this.scheduleSuffix = scheduleSuffix;
    }

    public String getTerminalLocation() {
        return terminalLocation;
    }

    public void setTerminalLocation(String terminalLocation) {
        this.terminalLocation = terminalLocation;
    }

    public void setExtension1(String extension1) {
        this.extension1 = extension1;
    }

    public String getExtension1() {
        return extension1;
    }

    public void setExtension2(String extension2) {
        this.extension2 = extension2;
    }

    public String getExtension2() {
        return extension2;
    }

    public void setExtension3(String extension3) {
        this.extension3 = extension3;
    }

    public String getExtension3() {
        return extension3;
    }

    public String getExportsBillingTerminalEmail() {
        return exportsBillingTerminalEmail;
    }

    public void setExportsBillingTerminalEmail(String exportsBillingTerminalEmail) {
        this.exportsBillingTerminalEmail = exportsBillingTerminalEmail;
    }

    public String getDocDeptEmail() {
        return docDeptEmail;
    }

    public void setDocDeptEmail(String docDeptEmail) {
        this.docDeptEmail = docDeptEmail;
    }

    public String getFclExportIssuingTerminal() {
        return fclExportIssuingTerminal;
    }

    public void setFclExportIssuingTerminal(String fclExportIssuingTerminal) {
        this.fclExportIssuingTerminal = fclExportIssuingTerminal;
    }
   
}
