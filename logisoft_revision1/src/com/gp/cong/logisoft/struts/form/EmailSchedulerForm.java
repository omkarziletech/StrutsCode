/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * MyEclipse Struts Creation date: 12-10-2008
 *
 * XDoclet definition:
 *
 * @struts.form name="emailSchedulerForm"
 */
public class EmailSchedulerForm extends ActionForm {
    /*
     * Generated Methods
     */

    private String sortBy;
    private String startDate;
    private String endDate;
    private String status;
    private String action;
    private String[] mailCheck;
    private String sendStatus;
    private String fileName;
    private String moduleName;
    private String userName;
    private String toEmailOrFax;
    private String emailCheck;
    private String methodName;
    private String emailId;
    private String limit;
    private Boolean isPrintPopUp;
   private String bccAddress;
    private String ccAddress;
    private String fromAddress;
    private String filterByName;

    public String getUserName() {
        return userName;
    }

    public String getToEmailOrFax() {
        return toEmailOrFax;
    }

    public void setToEmailOrFax(String toEmailOrFax) {
        this.toEmailOrFax = toEmailOrFax;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
    }

    /**
     * Method validate
     *
     * @param mapping
     * @param request
     * @return ActionErrors
     */
    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Method reset
     *
     * @param mapping
     * @param request
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        // TODO Auto-generated method stub
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

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String[] getMailCheck() {
        return mailCheck;
    }

    public void setMailCheck(String[] mailCheck) {
        this.mailCheck = mailCheck;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getEmailCheck() {
        return emailCheck;
    }

    public void setEmailCheck(String emailCheck) {
        this.emailCheck = emailCheck;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public Boolean getIsPrintPopUp() {
        return isPrintPopUp;
    }

    public void setIsPrintPopUp(Boolean isPrintPopUp) {
        this.isPrintPopUp = isPrintPopUp;
    }

    public String getBccAddress() {
        return bccAddress;
    }

    public void setBccAddress(String bccAddress) {
        this.bccAddress = bccAddress;
    }


    public String getCcAddress() {
        return ccAddress;
    }

    public void setCcAddress(String ccAddress) {
        this.ccAddress = ccAddress;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getFilterByName() {
        return filterByName;
    }

    public void setFilterByName(String filterByName) {
        this.filterByName = filterByName;
    }
    
}