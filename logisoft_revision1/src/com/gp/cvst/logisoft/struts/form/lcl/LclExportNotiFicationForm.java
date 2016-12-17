/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.struts.form.lcl;

import java.util.Date;
import org.apache.log4j.Logger;

/**
 *
 * @author aravindhan.v
 */
public class LclExportNotiFicationForm extends LogiwareActionForm {

    private static final Logger log = Logger.getLogger(LclExportNotiFicationForm.class);
    private String methodName;
    private String buttonValue;
    private String unitNo;
    private String voyageNo;
    private Long unitId;
    private Long headerId;
    private String changedFields;
    private String remarks;
    private String fileName;
    private String userName;
    private Long notificationId;
    private Long  userId;
    private Date  enterDateTime;
    private String vessel;
    private String pier;
    private String portLrd;
    private Date sailDate;
    private Date etaDate;
    private String ssLine;
    private String ssVoyage;
    private String noticeStatus;
    private String voyageReason;
    private String voyageComment;
    private String voyageReasonId;
    
    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getButtonValue() {
        return buttonValue;
    }

    public void setButtonValue(String buttonValue) {
        this.buttonValue = buttonValue;
    }

    public String getChangedFields() {
        return changedFields;
    }

    public void setChangedFields(String changedFields) {
        this.changedFields = changedFields;
    }

    public Long getHeaderId() {
        return headerId;
    }

    public void setHeaderId(Long headerId) {
        this.headerId = headerId;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public String getUnitNo() {
        return unitNo;
    }

    public void setUnitNo(String unitNo) {
        this.unitNo = unitNo;
    }

    public String getVoyageNo() {
        return voyageNo;
    }

    public void setVoyageNo(String voyageNo) {
        this.voyageNo = voyageNo;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public Date getEnterDateTime() {
        return enterDateTime;
    }

    public void setEnterDateTime(Date enterDateTime) {
        this.enterDateTime = enterDateTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getEtaDate() {
        return etaDate;
    }

    public void setEtaDate(Date etaDate) {
        this.etaDate = etaDate;
    }

    public String getPier() {
        return pier;
    }

    public void setPier(String pier) {
        this.pier = pier;
    }

    public String getPortLrd() {
        return portLrd;
    }

    public void setPortLrd(String portLrd) {
        this.portLrd = portLrd;
    }

    public Date getSailDate() {
        return sailDate;
    }

    public void setSailDate(Date sailDate) {
        this.sailDate = sailDate;
    }

    public String getSsLine() {
        return ssLine;
    }

    public void setSsLine(String ssLine) {
        this.ssLine = ssLine;
    }

    public String getSsVoyage() {
        return ssVoyage;
    }

    public void setSsVoyage(String ssVoyage) {
        this.ssVoyage = ssVoyage;
    }

    public String getVessel() {
        return vessel;
    }

    public void setVessel(String vessel) {
        this.vessel = vessel;
    }

    public String getNoticeStatus() {
        return noticeStatus;
    }

    public void setNoticeStatus(String noticeStatus) {
        this.noticeStatus = noticeStatus;
    }

    public String getVoyageReason() {
        return voyageReason;
    }

    public void setVoyageReason(String voyageReason) {
        this.voyageReason = voyageReason;
    }

    public String getVoyageComment() {
        return voyageComment;
    }

    public void setVoyageComment(String voyageComment) {
        this.voyageComment = voyageComment;
    }

    public String getVoyageReasonId() {
        return voyageReasonId;
    }

    public void setVoyageReasonId(String voyageReasonId) {
        this.voyageReasonId = voyageReasonId;
    }
    
}
