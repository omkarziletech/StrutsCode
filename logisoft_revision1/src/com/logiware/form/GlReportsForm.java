package com.logiware.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Lakshmi Narayanan
 */
public class GlReportsForm extends ActionForm {

    private String action;
    private String chargeCode;
    private String glAccount;
    private String fromDate;
    private String toDate;
    private boolean showPosted = true;
    private String reportingDate;
    private String tabName;
    private String reportType;
    private String glPeriod;
    private String fileType;
    private String issuingTerminal;
    private String pod;
    private String reportCategory;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getGlAccount() {
        return glAccount;
    }

    public void setGlAccount(String glAccount) {
        this.glAccount = glAccount;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public boolean isShowPosted() {
        return showPosted;
    }

    public void setShowPosted(boolean showPosted) {
        this.showPosted = showPosted;
    }

    public String getReportingDate() {
        return reportingDate;
    }

    public void setReportingDate(String reportingDate) {
        this.reportingDate = reportingDate;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getGlPeriod() {
        return glPeriod;
    }

    public void setGlPeriod(String glPeriod) {
        this.glPeriod = glPeriod;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getIssuingTerminal() {
        return issuingTerminal;
    }

    public void setIssuingTerminal(String issuingTerminal) {
        this.issuingTerminal = issuingTerminal;
    }

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

    public String getReportCategory() {
        return reportCategory;
    }

    public void setReportCategory(String reportCategory) {
        this.reportCategory = reportCategory;
    }

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        showPosted = false;
    }
}
