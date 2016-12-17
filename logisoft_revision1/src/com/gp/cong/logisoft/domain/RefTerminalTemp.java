package com.gp.cong.logisoft.domain;

import java.io.Serializable;

public class RefTerminalTemp implements Auditable, Serializable {

    private static final long serialVersionUID = 1L;
    private String trmnum;
    private String trmnam;
    private String terminalType;
    private String match;
    private GenericCode genericCode;
    private String city1;
    private String codeDesc;
    private String unLocCode;
    private String unLocationCode1;
    private String terminalLocation;
    private String docDeptEmail;
    private String docDeptName;
    private String customerServiceName;
    private String customerServiceEmail;
    private String intraBookerId;
    private String importsDoorDeliveryEmail;
    private String lclDocDeptName;
    private String lclDocDeptEmail;
    private String lclCustomerServiceName;
    private String lclCustomerServiceEmail;

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

    public String getCity1() {

        return city1;
    }

    public void setCity1(String city1) {

        this.city1 = city1;
    }

    public AuditInfo getAuditInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getId() {
        // TODO Auto-generated method stub
        return this.getTrmnum();
    }

    public String getTerminalLocation() {
        return terminalLocation;
    }

    public void setTerminalLocation(String terminalLocation) {
        this.terminalLocation = terminalLocation;
    }

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public String getDocDeptEmail() {
        return docDeptEmail;
    }

    public void setDocDeptEmail(String docDeptEmail) {
        this.docDeptEmail = docDeptEmail;
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
