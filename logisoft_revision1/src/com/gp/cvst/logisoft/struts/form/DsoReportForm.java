/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.struts.form;

import org.apache.struts.action.ActionForm;

/**
 *
 * @author logiware
 */
public class DsoReportForm extends ActionForm {

    private String action;
    private String searchDsoBy;
    private String fromPeriod;
    private String fromPeriodId;
    private String toPeriod;
    private String toPeriodId;
    private String numberOfDays;
    private String userName;
    private String userId;
    private String vendorName;
    private String vendorNumber;
    private boolean excel;
    private String reportType;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getSearchDsoBy() {
        return searchDsoBy;
    }

    public void setSearchDsoBy(String searchDsoBy) {
        this.searchDsoBy = searchDsoBy;
    }

    public String getFromPeriod() {
        return fromPeriod;
    }

    public void setFromPeriod(String fromPeriod) {
        this.fromPeriod = fromPeriod;
    }

    public String getFromPeriodId() {
        return fromPeriodId;
    }

    public void setFromPeriodId(String fromPeriodId) {
        this.fromPeriodId = fromPeriodId;
    }

    public String getToPeriod() {
        return toPeriod;
    }

    public void setToPeriod(String toPeriod) {
        this.toPeriod = toPeriod;
    }

    public String getToPeriodId() {
        return toPeriodId;
    }

    public void setToPeriodId(String toPeriodId) {
        this.toPeriodId = toPeriodId;
    }

    public String getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(String numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorNumber() {
        return vendorNumber;
    }

    public void setVendorNumber(String vendorNumber) {
        this.vendorNumber = vendorNumber;
    }

    public boolean isExcel() {
        return excel;
    }

    public void setExcel(boolean excel) {
        this.excel = excel;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }
    
}
