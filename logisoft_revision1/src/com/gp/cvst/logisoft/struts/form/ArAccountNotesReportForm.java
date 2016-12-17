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
public class ArAccountNotesReportForm extends ActionForm {
    private String customerName;
    private String customerNumber;
    private String notesEnteredBy;
    private String fromDate;
    private String toDate;
    private String action;
    private String reportType;
    private String accountAssignedTo;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getNotesEnteredBy() {
        return notesEnteredBy;
    }

    public void setNotesEnteredBy(String notesEnteredBy) {
        this.notesEnteredBy = notesEnteredBy;
    }
    
    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getAccountAssignedTo() {
        return accountAssignedTo;
    }

    public void setAccountAssignedTo(String accountAssignedTo) {
        this.accountAssignedTo = accountAssignedTo;
    }
    
}
