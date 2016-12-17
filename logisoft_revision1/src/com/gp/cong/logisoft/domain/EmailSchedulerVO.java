/**
 *
 */
package com.gp.cong.logisoft.domain;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.DateUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 *
 */
public class EmailSchedulerVO implements Serializable {

    private Integer id;
    private String name;
    private String fileLocation;
    private String type;
    private String status;
    private Integer noOfTries = 0;
    private Date emailDate;
    private String formatedEmailDate;
    private List<EmailSchedulerVO> emailList;
    private String toName;
    private String toAddress;
    private String fromName;
    private String fromAddress;
    private String ccAddress;
    private String bccAddress;
    private String subject;
    private String htmlMessage;
    private String textMessage;
    private String moduleName;
    private String moduleId;
    private String userName;
    private String tempFileLocation;
    private List<String> fileLocations;
    private boolean multiAttachment;
    private String coverLetter;
    private String printerName;
    private Integer printCopy = 1;
    private String responseCode;
    private String companyName;
    private String contactId;
    private String accountNo;
    private String printerTray;
    private Long moduleReferenceId;

    public EmailSchedulerVO() {
    }

    public void setEmailData(String toName, String toAddress,
            String fromName, String fromAddress, String ccAddress,
            String bccAddress, String subject, String htmlMessage) {
        this.toName = toName;
        this.toAddress = toAddress;
        this.fromName = fromName;
        this.fromAddress = fromAddress;
        this.ccAddress = ccAddress;
        this.bccAddress = bccAddress;
        this.subject = subject;
        this.htmlMessage = htmlMessage;
    }

    public void setEmailInfo(String name, String fileLocation, String type,
            Integer noOfTries, Date emailDate,
            String moduleName, String moduleId, String userName) {
        this.name = name;
        this.fileLocation = fileLocation;
        this.type = type;
        this.status = CommonConstants.EMAIL_STATUS_PENDING;
        if (null != noOfTries && !noOfTries.equals("")) {
            this.noOfTries = noOfTries;
        } else {
            this.noOfTries = 0;
        }
        this.emailDate = emailDate;
        this.moduleName = moduleName;
        this.moduleId = null != moduleId ? moduleId.substring(-1 != moduleId.indexOf("-") ? moduleId.indexOf("-") + 1 : 0) : "";
        this.userName = userName;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getCcAddress() {
        return ccAddress;
    }

    public void setCcAddress(String ccAddress) {
        this.ccAddress = ccAddress;
    }

    public String getBccAddress() {
        return bccAddress;
    }

    public void setBccAddress(String bccAddress) {
        this.bccAddress = bccAddress;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getHtmlMessage() {
        return htmlMessage;
    }

    public void setHtmlMessage(String htmlMessage) {
        this.htmlMessage = htmlMessage;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public void setEmailList(List<EmailSchedulerVO> emailList) {
        this.emailList = emailList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getNoOfTries() {
        return noOfTries;
    }

    public void setNoOfTries(Integer noOfTries) {
        this.noOfTries = noOfTries;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getEmailDate() {
        return emailDate;
    }

    public void setEmailDate(Date emailDate) {
        this.emailDate = emailDate;
    }

    public List getEmailList() {
        return emailList;
    }

    public String getFormatedEmailDate() {
        return formatedEmailDate;
    }

    public void setFormatedEmailDate(String formatedEmailDate) throws Exception {
        formatedEmailDate = DateUtils.formatDate(emailDate, "MM/dd/yyyy hh:mm a");
        this.formatedEmailDate = formatedEmailDate;
    }

    public List<String> getFileLocations() {
        List files = new ArrayList<String>();
        if (null != fileLocation) {
            String[] fileArray = fileLocation.split(";");
            for (int i = 0; i < fileArray.length; i++) {
                files.add(fileArray[i]);
            }
        }
        return files;
    }

    public void setFileLocations(List<String> fileLocations) {
        this.fileLocations = fileLocations;
    }

    public boolean isMultiAttachment() {
        if (getFileLocations().size() > 1) {
            return true;
        }
        return false;
    }

    public void setMultiAttachment(boolean multiAttachement) {
        this.multiAttachment = multiAttachement;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTempFileLocation() {
        return tempFileLocation;
    }

    public void setTempFileLocation(String tempFileLocation) {
        this.tempFileLocation = tempFileLocation;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    public String getPrinterName() {
        return printerName;
    }

    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }

    public Integer getPrintCopy() {
        return printCopy;
    }

    public void setPrintCopy(Integer printCopy) {
        this.printCopy = printCopy;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getPrinterTray() {
        return printerTray;
    }

    public void setPrinterTray(String printerTray) {
        this.printerTray = printerTray;
    }

    public Long getModuleReferenceId() {
        return moduleReferenceId;
    }

    public void setModuleReferenceId(Long moduleReferenceId) {
        this.moduleReferenceId = moduleReferenceId;
    }
}
