/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.struts.form.lcl;

/**
 *
 * @author Administrator
 */
public class LclUnitSsRemarksForm extends LogiwareActionForm {
    // imports and Exports

    private String headerId;
    private String unitSsId;
    private String unitNo;
    private String unitId;
    private String actions;
    private String followUpDate;
    private String remarks;
    private String methodName;
    private String voyageNo;
    //unit ss remarks id
    private Long id;
    private String followUpEmail;
    private String userEmail;//checkBoxValue

    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

    public String getHeaderId() {
        return headerId;
    }

    public void setHeaderId(String headerId) {
        this.headerId = headerId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    @Override
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getFollowUpDate() {
        return followUpDate;
    }

    public void setFollowUpDate(String followUpDate) {
        this.followUpDate = followUpDate;
    }

    public String getUnitNo() {
        return unitNo;
    }

    public void setUnitNo(String unitNo) {
        this.unitNo = unitNo;
    }

    public String getUnitSsId() {
        return unitSsId;
    }

    public void setUnitSsId(String unitSsId) {
        this.unitSsId = unitSsId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVoyageNo() {
        return voyageNo;
    }

    public void setVoyageNo(String voyageNo) {
        this.voyageNo = voyageNo;
    }

    public String getFollowUpEmail() {
        return followUpEmail;
    }

    public void setFollowUpEmail(String followUpEmail) {
        this.followUpEmail = followUpEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
