package com.gp.cong.logisoft.domain;

import java.io.Serializable;

import java.util.Date;

public class ProcessInfoHistory implements Serializable {

    private Integer userid;
    private Integer programid;
    private String action;
    private Date processinfodate;
    private String editstatus;
    private String deletestatus;
    private String recordid;
    private Integer id;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDeletestatus() {
        return deletestatus;
    }

    public void setDeletestatus(String deletestatus) {
        this.deletestatus = deletestatus;
    }

    public String getEditstatus() {
        return editstatus;
    }

    public void setEditstatus(String editstatus) {
        this.editstatus = editstatus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getProcessinfodate() {
        return processinfodate;
    }

    public void setProcessinfodate(Date processinfodate) {
        this.processinfodate = processinfodate;
    }

    public Integer getProgramid() {
        return programid;
    }

    public void setProgramid(Integer programid) {
        this.programid = programid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getRecordid() {
        return recordid;
    }

    public void setRecordid(String recordid) {
        this.recordid = recordid;
    }
}
