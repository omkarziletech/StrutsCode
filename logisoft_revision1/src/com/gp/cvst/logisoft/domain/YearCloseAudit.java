package com.gp.cvst.logisoft.domain;

import java.io.Serializable;
import java.util.Date;

public class YearCloseAudit implements Serializable {

    /**
     * Fields
     */
    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer year;
    private String user;
    private Date currentDate;
    private String reason;

    public YearCloseAudit() {
    }

    public YearCloseAudit(Integer year, String user, Date currentDate, String reason) {
        super();
        this.year = year;
        this.user = user;
        this.currentDate = currentDate;
        this.reason = reason;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
