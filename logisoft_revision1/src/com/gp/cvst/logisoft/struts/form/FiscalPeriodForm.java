/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cvst.logisoft.struts.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.gp.cong.logisoft.bc.accounting.ReportConstants;

/**
 * MyEclipse Struts Creation date: 05-22-2008
 *
 * XDoclet definition:
 *
 * @struts.form name="fiscalPeriodForm"
 */
public class FiscalPeriodForm extends ActionForm {
    /*
     * Generated Methods
     */

    /**
     * Method validate
     *
     * @param mapping
     * @param request
     * @return ActionErrors
     */
    private FormFile myFile;
    private String submitValue;
    private String[] x;
    private String copybudgetset;
    private String buttonValue;
    private String fisperiod;
    private String[] fiscalperiod;
    private String[] startingdate;
    private String[] endingdate;
    private String startingdate1;
    private String endingdate1;
    private String month;
    private String year;
    private String adjperiodrate = "Open";
    private String closperiodstatus = "Open";
    private String active = "Open";
    private String active1 = "Close";
    private String adjperiodrate1 = "Open";
    private String closperiodstatus1 = "Close";
    private String id;
    private String status;
    private Integer index;
    private String year1;
    private String toPeriod;
    private String fromPeriod;
    private String startingAccount;
    private String endingAccount;
    private String startingDepartment;
    private String endingDepartment;
    private String consolidatedSatement = ReportConstants.YES;
    private String departmentStatement = ReportConstants.YES;
    private boolean ecuReport;

    public String getEndingAccount() {
        return endingAccount;
    }

    public void setEndingAccount(String endingAccount) {
        this.endingAccount = endingAccount;
    }

    public String getStartingAccount() {
        return startingAccount;
    }

    public void setStartingAccount(String startingAccount) {
        this.startingAccount = startingAccount;
    }

    public String getYear1() {
        return year1;
    }

    public void setYear1(String year1) {
        this.year1 = year1;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getButtonValue() {
        return buttonValue;
    }

    public void setButtonValue(String buttonValue) {
        this.buttonValue = buttonValue;
    }

    public String[] getEndingdate() {
        return endingdate;
    }

    public void setEndingdate(String[] endingdate) {
        this.endingdate = endingdate;
    }

    public String getEndingdate1() {
        return endingdate1;
    }

    public void setEndingdate1(String endingdate1) {
        this.endingdate1 = endingdate1;
    }

    public String[] getFiscalperiod() {
        return fiscalperiod;
    }

    public void setFiscalperiod(String[] fiscalperiod) {
        this.fiscalperiod = fiscalperiod;
    }

    public String getFisperiod() {
        return fisperiod;
    }

    public void setFisperiod(String fisperiod) {
        this.fisperiod = fisperiod;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String[] getStartingdate() {
        return startingdate;
    }

    public void setStartingdate(String[] startingdate) {
        this.startingdate = startingdate;
    }

    public String getStartingdate1() {
        return startingdate1;
    }

    public void setStartingdate1(String startingdate1) {
        this.startingdate1 = startingdate1;
    }

    public String[] getX() {
        return x;
    }

    public void setX(String[] x) {
        this.x = x;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        // TODO Auto-generated method stub
        fiscalperiod = null;
        startingdate = null;
        endingdate = null;

    }

    public String getAdjperiodrate() {
        return adjperiodrate;
    }

    public void setAdjperiodrate(String adjperiodrate) {
        this.adjperiodrate = adjperiodrate;
    }

    public String getClosperiodstatus() {
        return closperiodstatus;
    }

    public void setClosperiodstatus(String closperiodstatus) {
        this.closperiodstatus = closperiodstatus;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getActive1() {
        return active1;
    }

    public void setActive1(String active1) {
        this.active1 = active1;
    }

    public String getAdjperiodrate1() {
        return adjperiodrate1;
    }

    public void setAdjperiodrate1(String adjperiodrate1) {
        this.adjperiodrate1 = adjperiodrate1;
    }

    public String getClosperiodstatus1() {
        return closperiodstatus1;
    }

    public void setClosperiodstatus1(String closperiodstatus1) {
        this.closperiodstatus1 = closperiodstatus1;
    }

    public String getToPeriod() {
        return toPeriod;
    }

    public void setToPeriod(String toPeriod) {
        this.toPeriod = toPeriod;
    }

    public String getFromPeriod() {
        return fromPeriod;
    }

    public void setFromPeriod(String fromPeriod) {
        this.fromPeriod = fromPeriod;
    }

    public String getCopybudgetset() {
        return copybudgetset;
    }

    public void setCopybudgetset(String copybudgetset) {
        this.copybudgetset = copybudgetset;
    }

    public String getSubmitValue() {
        return submitValue;
    }

    public void setSubmitValue(String submitValue) {
        this.submitValue = submitValue;
    }

    public void setMyFile(FormFile myFile) {
        this.myFile = myFile;
    }

    public FormFile getMyFile() {
        return myFile;
    }

    public String getConsolidatedSatement() {
        return consolidatedSatement;
    }

    public void setConsolidatedSatement(String consolidatedSatement) {
        this.consolidatedSatement = consolidatedSatement;
    }

    public String getDepartmentStatement() {
        return departmentStatement;
    }

    public void setDepartmentStatement(String departmentStatement) {
        this.departmentStatement = departmentStatement;
    }

    public String getStartingDepartment() {
        return startingDepartment;
    }

    public void setStartingDepartment(String startingDepartment) {
        this.startingDepartment = startingDepartment;
    }

    public String getEndingDepartment() {
        return endingDepartment;
    }

    public void setEndingDepartment(String endingDepartment) {
        this.endingDepartment = endingDepartment;
    }

    public boolean isEcuReport() {
        return ecuReport;
    }

    public void setEcuReport(boolean ecuReport) {
        this.ecuReport = ecuReport;
    }

}
