package com.gp.cong.logisoft.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

public class Role implements Auditable, Serializable {

    private static final long serialVersionUID = 1L;
    private String roleDesc;
    private Integer roleId;
    private String roleCreatedDate;
    private Date roleCreatedOn;
    private Set roleItem;
    private String match;
    private String updatedBy;
    private Date updatedDate;

    public String getRoleCreatedDate() {
        if (roleCreatedOn != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
            return sdf.format(roleCreatedOn);
        }
        return roleCreatedDate;
    }

    public void setRoleCreatedDate(String roleCreatedDate) {
        this.roleCreatedDate = roleCreatedDate;
    }

    public Set getRoleItem() {

        return roleItem;
    }

    public void setRoleItem(Set roleItem) {

        this.roleItem = roleItem;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {

        this.roleDesc = roleDesc;
    }

    public Integer getRoleId() {

        return roleId;


    }

    public void setRoleId(Integer roleId) {

        this.roleId = roleId;
    }

    public Date getRoleCreatedOn() {
        return roleCreatedOn;
    }

    public void setRoleCreatedOn(Date roleCreatedOn) {
        this.roleCreatedOn = roleCreatedOn;
    }

    public AuditInfo getAuditInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getId() {
        // TODO Auto-generated method stub
        return this.getRoleId();
    }

    /**
     * @return the match
     */
    public String getMatch() {
        return match;
    }

    /**
     * @param match the match to set
     */
    public void setMatch(String match) {
        this.match = match;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
}
