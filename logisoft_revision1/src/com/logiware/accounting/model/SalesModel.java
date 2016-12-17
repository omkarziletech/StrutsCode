package com.logiware.accounting.model;

/**
 *
 * @author Lakshmi Narayanan
 */
public class SalesModel {

    private String managerName;
    private String managerEmail;
    private String rsmEmail;
    private String salesCode;
    private String salesId;

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getManagerEmail() {
        return managerEmail;
    }

    public void setManagerEmail(String managerEmail) {
        this.managerEmail = managerEmail;
    }

    public String getRsmEmail() {
        return rsmEmail;
    }

    public void setRsmEmail(String rsmEmail) {
        this.rsmEmail = rsmEmail;
    }
    
    public String getSalesCode() {
        return salesCode;
    }

    public void setSalesCode(String salesCode) {
        this.salesCode = salesCode;
    }

    public String getSalesId() {
        return salesId;
    }

    public void setSalesId(String salesId) {
        this.salesId = salesId;
    }
}
