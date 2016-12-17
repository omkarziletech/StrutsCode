package com.gp.cong.logisoft.domain;

import java.io.Serializable;

public class RefTerminal implements Auditable, Serializable {

    private static final long serialVersionUID = 1L;
    private String trmnum;
    private String trmnam;
    private String terminalType;
    private String state;
    private GenericCode country;
    private String addres1;
    private String addres2;
    private String zipcde;
    private String phnnum1;
    private String phnnum2;
    private String phnnum3;
    private String extension1;
    private String extension2;
    private String extension3;
    private String faxnum1;
    private String faxnum2;
    private String faxnum3;
    private String faxnum4;
    private String faxnum5;
    private String ovr10kchgcode;
    private String ovr20kchgcode;
    private String printermodel;
    private String actyon;
    private String airsrvc;
    private String govSchCode;
    private String notes;
    private UnLocation unLocation;
    private GenericCode genericCode;
    private GenericCode genericCode1;
    private GenericCode genericCode2;
    private GenericCode genericCode3;
    private GenericCode genericCode4;
    private GenericCode genericCode5;
    private String city1;
    private String codeDesc;
    private String terminalLocation;
    private String ledgerNo;
    private String careof;
    private String scheduleSuffix;
    private String importsContactEmail;
    private String match;
    private String unLocCode;
    private String unLocationCode1;
    private String tpacctno;
    private String exportsBillingTerminalEmail;
    private String docDeptEmail;
    private String zaccount;
    private String docDeptName;
    private String customerServiceName;
    private String customerServiceEmail;
    private String fclExportIssuingTerminal;
    private String intraBookerId;
    private String importsDoorDeliveryEmail;
    private String lclDocDeptName;
    private String lclDocDeptEmail;
    private String lclCustomerServiceName;
    private String lclCustomerServiceEmail;
    
    public String getTpacctno() {
        return tpacctno;
    }

    public void setTpacctno(String tpacctno) {
        this.tpacctno = tpacctno;
    }
    
    public String getUnLocCode() {
        return unLocCode;
    }

    public void setUnLocCode(String unLocCode) {
        this.unLocCode = unLocCode;
    }

    public String getUnLocationCode1() {
        return unLocationCode1;
    }

    public void setUnLocationCode1(String unLocationCode1) {
        this.unLocationCode1 = unLocationCode1;
    }

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public String getTrmnum() {

        return trmnum;
    }

    public void setTrmnum(String trmnum) {

        this.trmnum = trmnum;
    }

    public String getCodeDesc() {

        return codeDesc;
    }

    public void setCodeDesc(String codeDesc) {

        this.codeDesc = codeDesc;
    }

    public GenericCode getGenericCode() {

        return genericCode;
    }

    public void setGenericCode(GenericCode genericCode) {
        this.genericCode = genericCode;

        if (this.genericCode != null) {
            this.setCodeDesc((this.genericCode.getCodedesc()));

        }
    }

    public UnLocation getUnLocation() {

        return unLocation;
    }

    public void setUnLocation(UnLocation unLocation) {

        this.unLocation = unLocation;


    }

    public String getActyon() {
        return actyon;
    }

    public void setActyon(String actyon) {

        this.actyon = actyon;
    }

    public String getAddres1() {
        return addres1;
    }

    public void setAddres1(String addres1) {

        this.addres1 = addres1;
    }

    public String getAddres2() {
        return addres2;
    }

    public void setAddres2(String addres2) {

        this.addres2 = addres2;
    }

    public String getAirsrvc() {
        return airsrvc;
    }

    public void setAirsrvc(String airsrvc) {

        this.airsrvc = airsrvc;
    }

    public String getFaxnum1() {
        return faxnum1;
    }

    public void setFaxnum1(String faxnum1) {

        this.faxnum1 = faxnum1;
    }

    public String getFaxnum2() {
        return faxnum2;
    }

    public void setFaxnum2(String faxnum2) {

        this.faxnum2 = faxnum2;
    }

    public String getFaxnum3() {
        return faxnum3;
    }

    public void setFaxnum3(String faxnum3) {

        this.faxnum3 = faxnum3;
    }

    public String getFaxnum4() {
        return faxnum4;
    }

    public void setFaxnum4(String faxnum4) {

        this.faxnum4 = faxnum4;
    }

    public String getFaxnum5() {
        return faxnum5;
    }

    public void setFaxnum5(String faxnum5) {


        this.faxnum5 = faxnum5;
    }

    public String getGovSchCode() {
        return govSchCode;
    }

    public void setGovSchCode(String govSchCode) {


        this.govSchCode = govSchCode;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {

        this.notes = notes;
    }

    public String getPhnnum1() {
        return phnnum1;
    }

    public void setPhnnum1(String phnnum1) {

        this.phnnum1 = phnnum1;
    }

    public String getPhnnum2() {
        return phnnum2;
    }

    public void setPhnnum2(String phnnum2) {

        this.phnnum2 = phnnum2;
    }

    public String getPhnnum3() {
        return phnnum3;
    }

    public void setPhnnum3(String phnnum3) {

        this.phnnum3 = phnnum3;
    }

    public String getPrintermodel() {
        return printermodel;
    }

    public void setPrintermodel(String printermodel) {

        this.printermodel = printermodel;
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

    public String getTrmnam() {
        return trmnam;
    }

    public void setTrmnam(String trmnam) {

        this.trmnam = trmnam;
    }

    public String getZipcde() {
        return zipcde;
    }

    public void setZipcde(String zipcde) {

        this.zipcde = zipcde;
    }

    public String getCity1() {

        return city1;
    }

    public void setCity1(String city1) {

        this.city1 = city1;
    }

    public GenericCode getGenericCode2() {
        return genericCode2;
    }

    public void setGenericCode2(GenericCode genericCode2) {

        this.genericCode2 = genericCode2;
    }

    public GenericCode getGenericCode3() {
        return genericCode3;
    }

    public void setGenericCode3(GenericCode genericCode3) {

        this.genericCode3 = genericCode3;
    }

    public GenericCode getGenericCode4() {
        return genericCode4;
    }

    public void setGenericCode4(GenericCode genericCode4) {


        this.genericCode4 = genericCode4;
    }

    public GenericCode getGenericCode5() {
        return genericCode5;
    }

    public void setGenericCode5(GenericCode genericCode5) {

        this.genericCode5 = genericCode5;
    }

    public GenericCode getCountry() {
        return country;
    }

    public void setCountry(GenericCode country) {

        this.country = country;
    }

    public GenericCode getGenericCode1() {
        return genericCode1;
    }

    public void setGenericCode1(GenericCode genericCode1) {


        this.genericCode1 = genericCode1;
    }

    public AuditInfo getAuditInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getId() {
        // TODO Auto-generated method stub
        return this.getTrmnum();
    }

    public String getCareof() {
        return careof;
    }

    public void setCareof(String careof) {
        this.careof = careof;
    }

    public String getImportsContactEmail() {
        return importsContactEmail;
    }

    public void setImportsContactEmail(String importsContactEmail) {
        this.importsContactEmail = importsContactEmail;
    }

    public String getLedgerNo() {
        return ledgerNo;
    }

    public void setLedgerNo(String ledgerNo) {
        this.ledgerNo = ledgerNo;
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

    public String getOvr10kchgcode() {
        return ovr10kchgcode;
    }

    public void setOvr10kchgcode(String ovr10kchgcode) {
        this.ovr10kchgcode = ovr10kchgcode;
    }

    public String getOvr20kchgcode() {
        return ovr20kchgcode;
    }

    public void setOvr20kchgcode(String ovr20kchgcode) {
        this.ovr20kchgcode = ovr20kchgcode;
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

    public String getZaccount() {
        return zaccount;
    }

    public void setZaccount(String zaccount) {
        this.zaccount = zaccount;
    }

    public String getDocDeptName() {
        return docDeptName;
    }

    public void setDocDeptName(String docDeptName) {
        this.docDeptName = docDeptName;
    }

    public String getCustomerServiceName() {
        return customerServiceName;
    }

    public void setCustomerServiceName(String customerServiceName) {
        this.customerServiceName = customerServiceName;
    }

    public String getCustomerServiceEmail() {
        return customerServiceEmail;
    }

    public void setCustomerServiceEmail(String customerServiceEmail) {
        this.customerServiceEmail = customerServiceEmail;
    }

    public String getFclExportIssuingTerminal() {
        return fclExportIssuingTerminal;
    }

    public void setFclExportIssuingTerminal(String fclExportIssuingTerminal) {
        this.fclExportIssuingTerminal = fclExportIssuingTerminal;
    }

    public String getIntraBookerId() {
        return intraBookerId;
    }

    public void setIntraBookerId(String intraBookerId) {
        this.intraBookerId = intraBookerId;
    }

    public String getImportsDoorDeliveryEmail() {
        return importsDoorDeliveryEmail;
    }

    public void setImportsDoorDeliveryEmail(String importsDoorDeliveryEmail) {
        this.importsDoorDeliveryEmail = importsDoorDeliveryEmail;
    }

    public String getLclDocDeptName() {
        return lclDocDeptName;
    }

    public void setLclDocDeptName(String lclDocDeptName) {
        this.lclDocDeptName = lclDocDeptName;
    }

    public String getLclDocDeptEmail() {
        return lclDocDeptEmail;
    }

    public void setLclDocDeptEmail(String lclDocDeptEmail) {
        this.lclDocDeptEmail = lclDocDeptEmail;
    }

    public String getLclCustomerServiceName() {
        return lclCustomerServiceName;
    }

    public void setLclCustomerServiceName(String lclCustomerServiceName) {
        this.lclCustomerServiceName = lclCustomerServiceName;
    }

    public String getLclCustomerServiceEmail() {
        return lclCustomerServiceEmail;
    }

    public void setLclCustomerServiceEmail(String lclCustomerServiceEmail) {
        this.lclCustomerServiceEmail = lclCustomerServiceEmail;
    }    
}
