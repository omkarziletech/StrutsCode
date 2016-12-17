/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.struts.form.lcl;

import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.LclRemarks;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class LclRemarksForm extends LogiwareActionForm {

    private Long id;
    private String methodName;
    private String followupDate;
    private String remarks;
    private String status;
    private String fileNumber;
    private Long fileNumberId;
    private String actions;
    private LclRemarks lclRemarks;
    private Long enteredById;
    private String enteredByName;
    private String priorityView;
    private String loginName;
    private String eventNotes;
    private String moduleId;
    private String userMail;
    private List<GenericCode> eventList;
    private String clntAcctNo;
    private String shpAcctNo;
    private String frwdAcctNo;
    private String consAcctNo;
    private String splNotes;
    private String moduleName;
    private String noteType;
    private String noteDesc;
    private String voidedDatetime;
    private String shpId;
    private String consId;
    private String fwdId;
    private String hold;
    private String previousValue;
    private Long tpId;

    public LclRemarksForm() {
        if (null == lclRemarks) {
            lclRemarks = new LclRemarks();
        }

        if (null == lclRemarks.getLclFileNumber()) {
            lclRemarks.setLclFileNumber(new LclFileNumber());
        }
    }

    public Long getId() {
        return lclRemarks.getId();
    }

    public void setId(Long id) {
        lclRemarks.setId(id);
    }

    public Long getEnteredById() {
        if (lclRemarks.getEnteredBy() != null) {
            Long enter = Long.parseLong(lclRemarks.getEnteredBy().toString());
            return enter;
        } else {
            return null;
        }
    }

    public void setEnteredById(Long enteredById) throws Exception {
        User user = new UserDAO().findById(enteredById.intValue());
        lclRemarks.setEnteredBy(user);
    }

    public String getEnteredByName() {
        if (lclRemarks.getEnteredBy() != null) {
            return lclRemarks.getEnteredBy().getFirstName();
        } else {
            return null;
        }
    }

    public void setEnteredByName(String enteredByName) {
        this.enteredByName = enteredByName;
    }

    public Long getFileNumberId() {
        return lclRemarks.getLclFileNumber().getId();
    }

    public void setFileNumberId(Long fileNumberId) {
        lclRemarks.getLclFileNumber().setId(fileNumberId);
    }

    public String getFollowupDate() {
        String d = DateUtils.parseDateToString(lclRemarks.getFollowupDate());
        return null == d ? "" : d;
    }

    public void setFollowupDate(String followupDate) throws Exception {
        lclRemarks.setFollowupDate(DateUtils.parseDate(followupDate, "dd-MMM-yyyy"));
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public LclRemarks getLclRemarks() {
        return lclRemarks;
    }

    public void setLclRemarks(LclRemarks lclRemarks) {
        this.lclRemarks = lclRemarks;
    }

    public String getRemarks() {
        return lclRemarks.getRemarks();
    }

    public void setRemarks(String remarks) {
        lclRemarks.setRemarks(remarks.toUpperCase());
    }

    public String getFollowupEmail() {
        return lclRemarks.getFollowupEmail();
    }

    public void setFollowupEmail(String followupEmail) {
        lclRemarks.setFollowupEmail(followupEmail);
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getType() {
        return lclRemarks.getType();
    }

    public void setType(String type) {
        lclRemarks.setType(type);
    }

    public String getFileNumber() {
        return lclRemarks.getLclFileNumber().getFileNumber();
    }

    public void setFileNumber(String fileNumber) {
        lclRemarks.getLclFileNumber().setFileNumber(fileNumber);
    }

    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriorityView() {
        return priorityView;
    }

    public void setPriorityView(String priorityView) {
        this.priorityView = priorityView;
    }

    public String getEventNotes() {
        return eventNotes;
    }

    public void setEventNotes(String eventNotes) {
        this.eventNotes = eventNotes;
    }

    public List<GenericCode> getEventList() {
        return eventList;
    }

    public void setEventList(List<GenericCode> eventList) {
        this.eventList = eventList;
    }

    public String getClntAcctNo() {
        return clntAcctNo;
    }

    public void setClntAcctNo(String clntAcctNo) {
        this.clntAcctNo = clntAcctNo;
    }

    public String getShpAcctNo() {
        return shpAcctNo;
    }

    public void setShpAcctNo(String shpAcctNo) {
        this.shpAcctNo = shpAcctNo;
    }

    public String getFrwdAcctNo() {
        return frwdAcctNo;
    }

    public void setFrwdAcctNo(String frwdAcctNo) {
        this.frwdAcctNo = frwdAcctNo;
    }

    public String getConsAcctNo() {
        return consAcctNo;
    }

    public void setConsAcctNo(String consAcctNo) {
        this.consAcctNo = consAcctNo;
    }

    public String getSplNotes() {
        return splNotes;
    }

    public void setSplNotes(String splNotes) {
        this.splNotes = splNotes;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
    

    public String getNoteType() {
        return noteType;
    }

    public void setNoteType(String noteType) {
        this.noteType = noteType;
    }

    public String getNoteDesc() {
        return noteDesc;
    }

    public void setNoteDesc(String noteDesc) {
        this.noteDesc = noteDesc;
    }

    public String getVoidedDatetime() {
        return voidedDatetime;
    }

    public void setVoidedDatetime(String voidedDatetime) {
        this.voidedDatetime = voidedDatetime;
    }

    public String getShpId() {
        return shpId;
    }

    public void setShpId(String shpId) {
        this.shpId = shpId;
    }

    public String getConsId() {
        return consId;
    }

    public void setConsId(String consId) {
        this.consId = consId;
    }

    public String getFwdId() {
        return fwdId;
    }

    public void setFwdId(String fwdId) {
        this.fwdId = fwdId;
    }

    public Long getTpId() {
        return tpId;
    }

    public void setTpId(Long tpId) {
        this.tpId = tpId;
    }

    public String getHold() {
        return hold;
    }

    public void setHold(String hold) {
        this.hold = hold;
    }

    public String getPreviousValue() {
        return previousValue;
    }

    public void setPreviousValue(String previousValue) {
        this.previousValue = previousValue;
    }
    
    
    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
    }
}
