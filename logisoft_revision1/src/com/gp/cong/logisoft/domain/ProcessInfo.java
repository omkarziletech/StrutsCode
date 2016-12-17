package com.gp.cong.logisoft.domain;

import java.io.Serializable;
import java.util.Date;

public class ProcessInfo implements Serializable {

    private Integer userid;
    private Integer programid;
    private String action;
    private Date processinfodate;
    private String editstatus;
    private String deletestatus;
    private String recordid;
    private String userName;
    private String itemName;
    private String email;
    private String country;
    private String state;
    private String city;
    private String role;
    private String telephone;
    private String moduleId;
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public Date getProcessinfodate() {
        return processinfodate;
    }

    public void setProcessinfodate(Date processinfodate) {

        this.processinfodate = processinfodate;
    }

    public void setAction(String action) {
        this.action = action;
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

    public String getRecordid() {
        return recordid;
    }

    public void setRecordid(String recordid) {
        this.recordid = recordid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }
}
