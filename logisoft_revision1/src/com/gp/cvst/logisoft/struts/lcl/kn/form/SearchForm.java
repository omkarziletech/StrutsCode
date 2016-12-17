package com.gp.cvst.logisoft.struts.lcl.kn.form;

import com.gp.cvst.logisoft.struts.form.lcl.LogiwareActionForm;

/**
 *
 * @author palraj.p
 */
public class SearchForm extends LogiwareActionForm {

    private String bkgNo;
    private String startDate;
    private String endDate;
    private String action;
    private String bookingId;
    private String sortBy;
    private String searchBy;
    private String searchType;
    private String bookingEnvelopeId;
    private String limitRecord;
    private String startDateHidden;
    private String endDateHidden;
    private String searchStatus;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getBkgNo() {
        return bkgNo;
    }

    public void setBkgNo(String bkgNo) {
        this.bkgNo = bkgNo;
    }

    public String getBookingEnvelopeId() {
        return bookingEnvelopeId;
    }

    public void setBookingEnvelopeId(String bookingEnvelopeId) {
        this.bookingEnvelopeId = bookingEnvelopeId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getLimitRecord() {
        return limitRecord;
    }

    public void setLimitRecord(String limitRecord) {
        this.limitRecord = limitRecord;
    }

    public String getSearchBy() {
        return searchBy;
    }

    public void setSearchBy(String searchBy) {
        this.searchBy = searchBy;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartDateHidden() {
        return startDateHidden;
    }

    public void setStartDateHidden(String startDateHidden) {
        this.startDateHidden = startDateHidden;
    }

    public String getEndDateHidden() {
        return endDateHidden;
    }

    public void setEndDateHidden(String endDateHidden) {
        this.endDateHidden = endDateHidden;
    }

    public String getSearchStatus() {
        return searchStatus;
    }

    public void setSearchStatus(String searchStatus) {
        this.searchStatus = searchStatus;
    }
}
