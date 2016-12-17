package com.gp.cong.logisoft.beans;

import java.io.Serializable;

public class SearchUserBean implements Serializable {

    final String DEFALT_MATCH_OPTION = "starts";
    private String loginName;
    private String txtCal;
    private String roleName;
    private String itemname;
    private String programname;
    private String txtItemcreatedon;
    private String firstName;
    private String lastName;
    private String useCase;
    private String match;
    private String userStatus;
    private String limit;
    private String sortBy;
    private String columnName;

    public String getMatch() {
        if (match == null) {
            return DEFALT_MATCH_OPTION;
        }
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getProgramname() {
        return programname;
    }

    public void setProgramname(String programname) {
        this.programname = programname;
    }

    public String getTxtItemcreatedon() {
        return txtItemcreatedon;
    }

    public void setTxtItemcreatedon(String txtItemcreatedon) {
        this.txtItemcreatedon = txtItemcreatedon;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getTxtCal() {
        return txtCal;
    }

    public void setTxtCal(String txtCal) {
        this.txtCal = txtCal;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUseCase() {

        return useCase;
    }

    public void setUseCase(String useCase) {
        if (useCase != null || useCase != "") {
            this.useCase = "";
        }
        this.useCase = useCase;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }
}
