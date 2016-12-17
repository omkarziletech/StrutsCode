package com.gp.cong.logisoft.struts.form;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import org.apache.struts.action.ActionForm;

public class PrintConfigForm extends ActionForm {

    private Long id;
    private String screenName;
    private String[] customerContact;
    private String documentType;
    private String documentName;
    private String fileLocation;
    private String pageAction;
    private String fileNumber;
    private String toName;
    private String toFaxNumber;
    private String fromName;
    private String fromFaxNumber;
    private String businessPhone;
    private String homePhone;
    private String subject;
    private String message;
    private String systemRule;
    private String toAddress;
    private String ccAddress;
    private String bccAddress;
    private String emailSubject;
    private String emailMessage;
    private String emailMessageOti;
    private String printerName;
    private boolean email;
    private boolean print;
    private boolean fax;
    private boolean emailMe;
    private boolean emailAll;
    private boolean emailSp;
    private boolean printCheck;
    private String allowPrint = "No";
    private Integer printCopy;
    private String pullEmail;
    private String contactsEmail;
    private String comment;
    private String comments;
    private String releaseOrdrEmailCons;
    private String releaseOrdrEmailCfs;
    private String releaseOrdrEmailNotify;
    private String releaseOrdrEmailNotify2;
    private String releaseOrdrEmailIpi;
    private String releaseOrdrFaxCons;
    private String releaseOrdrFaxCfs;
    private String releaseOrdrFaxNotify;
    private String releaseOrdrFaxNotify2;
    private String releaseOrdrFaxIpi;
    private boolean emailCoverPageOk;
    private String contactId;
    private String accountNo;
    private String fromEmailAddress;
    private String pullEmailUser;
    private String pullEmailDocs;
    private String emailCheckedDoc;
    private String nameFromTerminal;
    private String importflagForExport;
    private String moduleForExport;
    private String screenNameForExport;
    private String printerTray;
    private String postedLclBl;
    private String brand;
    private String brandCompanyName;
    
    private boolean exportFileCob;
    private Long blCorrectedId;
    
    private String issuingTerminal;
    private String terminalPhone;
    private String terminalFax;
    private String userTerminalPhoneNo;
    private String userTerminalFaxNo;
       
    public String getContactsEmail() {
        return contactsEmail;
    }

    public void setContactsEmail(String contactsEmail) {
        this.contactsEmail = contactsEmail;
    }

    public String getPullEmail() {
        return pullEmail;
    }

    public void setPullEmail(String pullEmail) {
        this.pullEmail = pullEmail;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public String getPageAction() {
        return pageAction;
    }

    public void setPageAction(String pageAction) {
        this.pageAction = pageAction;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getToFaxNumber() {
        return toFaxNumber;
    }

    public void setToFaxNumber(String toFaxNumber) {
        this.toFaxNumber = toFaxNumber;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getFromFaxNumber() {
        return fromFaxNumber;
    }

    public void setFromFaxNumber(String fromFaxNumber) {
        this.fromFaxNumber = fromFaxNumber;
    }

    public String getBusinessPhone() {
        return businessPhone;
    }

    public void setBusinessPhone(String businessPhone) {
        this.businessPhone = businessPhone;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSystemRule() {
        return systemRule;
    }

    public void setSystemRule(String systemRule) {
        this.systemRule = systemRule;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
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

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    public String getEmailMessage() throws Exception {
        return emailMessage;
    }

    public void setEmailMessage(String emailMessage) {
        this.emailMessage = emailMessage;
    }

    public boolean isEmail() {
        return email;
    }

    public void setEmail(boolean email) {
        this.email = email;
    }

    public boolean isPrint() {
        return print;
    }

    public void setPrint(boolean print) {
        this.print = print;
    }

    public boolean isFax() {
        return fax;
    }

    public void setFax(boolean fax) {
        this.fax = fax;
    }

    public boolean isEmailMe() {
        return emailMe;
    }

    public void setEmailMe(boolean emailMe) {
        this.emailMe = emailMe;
    }

    public boolean isPrintCheck() {
        return printCheck;
    }

    public void setPrintCheck(boolean printCheck) {
        this.printCheck = printCheck;
    }

    public String[] getCustomerContact() {
        return customerContact;
    }

    public void setCustomerContact(String[] customerContact) {
        this.customerContact = customerContact;
    }

    public String getPrinterName() {
        return printerName;
    }

    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }

    public String getAllowPrint() {
        return allowPrint;
    }

    public void setAllowPrint(String allowPrint) {
        this.allowPrint = allowPrint;
    }

    public Integer getPrintCopy() {
        return printCopy;
    }

    public void setPrintCopy(Integer printCopy) {
        this.printCopy = printCopy;
    }

    public boolean isEmailSp() {
        return emailSp;
    }

    public void setEmailSp(boolean emailSp) {
        this.emailSp = emailSp;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isEmailAll() {
        return emailAll;
    }

    public void setEmailAll(boolean emailAll) {
        this.emailAll = emailAll;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getReleaseOrdrEmailCfs() {
        return releaseOrdrEmailCfs;
    }

    public void setReleaseOrdrEmailCfs(String releaseOrdrEmailCfs) {
        this.releaseOrdrEmailCfs = releaseOrdrEmailCfs;
    }

    public String getReleaseOrdrEmailCons() {
        return releaseOrdrEmailCons;
    }

    public void setReleaseOrdrEmailCons(String releaseOrdrEmailCons) {
        this.releaseOrdrEmailCons = releaseOrdrEmailCons;
    }

    public String getReleaseOrdrEmailIpi() {
        return releaseOrdrEmailIpi;
    }

    public void setReleaseOrdrEmailIpi(String releaseOrdrEmailIpi) {
        this.releaseOrdrEmailIpi = releaseOrdrEmailIpi;
    }

    public String getReleaseOrdrEmailNotify() {
        return releaseOrdrEmailNotify;
    }

    public void setReleaseOrdrEmailNotify(String releaseOrdrEmailNotify) {
        this.releaseOrdrEmailNotify = releaseOrdrEmailNotify;
    }

    public String getReleaseOrdrEmailNotify2() {
        return releaseOrdrEmailNotify2;
    }

    public void setReleaseOrdrEmailNotify2(String releaseOrdrEmailNotify2) {
        this.releaseOrdrEmailNotify2 = releaseOrdrEmailNotify2;
    }

    public String getReleaseOrdrFaxCfs() {
        return releaseOrdrFaxCfs;
    }

    public void setReleaseOrdrFaxCfs(String releaseOrdrFaxCfs) {
        this.releaseOrdrFaxCfs = releaseOrdrFaxCfs;
    }

    public String getReleaseOrdrFaxCons() {
        return releaseOrdrFaxCons;
    }

    public void setReleaseOrdrFaxCons(String releaseOrdrFaxCons) {
        this.releaseOrdrFaxCons = releaseOrdrFaxCons;
    }

    public String getReleaseOrdrFaxIpi() {
        return releaseOrdrFaxIpi;
    }

    public void setReleaseOrdrFaxIpi(String releaseOrdrFaxIpi) {
        this.releaseOrdrFaxIpi = releaseOrdrFaxIpi;
    }

    public String getReleaseOrdrFaxNotify() {
        return releaseOrdrFaxNotify;
    }

    public void setReleaseOrdrFaxNotify(String releaseOrdrFaxNotify) {
        this.releaseOrdrFaxNotify = releaseOrdrFaxNotify;
    }

    public String getReleaseOrdrFaxNotify2() {
        return releaseOrdrFaxNotify2;
    }

    public void setReleaseOrdrFaxNotify2(String releaseOrdrFaxNotify2) {
        this.releaseOrdrFaxNotify2 = releaseOrdrFaxNotify2;
    }

    public boolean isEmailCoverPageOk() {
        return emailCoverPageOk;
    }

    public void setEmailCoverPageOk(boolean emailCoverPageOk) {
        this.emailCoverPageOk = emailCoverPageOk;
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

    public String getFromEmailAddress() {
        return fromEmailAddress;
    }

    public void setFromEmailAddress(String fromEmailAddress) {
        this.fromEmailAddress = fromEmailAddress;
    }

    public String getPullEmailUser() {
        return pullEmailUser;
    }

    public void setPullEmailUser(String pullEmailUser) {
        this.pullEmailUser = pullEmailUser;
    }

    public String getPullEmailDocs() {
        return pullEmailDocs;
    }

    public void setPullEmailDocs(String pullEmailDocs) {
        this.pullEmailDocs = pullEmailDocs;
    }

    public String getEmailCheckedDoc() {
        return emailCheckedDoc;
    }

    public void setEmailCheckedDoc(String emailCheckedDoc) {
        this.emailCheckedDoc = emailCheckedDoc;
    }

    public String getNameFromTerminal() {
        return nameFromTerminal;
    }

    public void setNameFromTerminal(String nameFromTerminal) {
        this.nameFromTerminal = nameFromTerminal;
    }

    public String getImportflagForExport() {
        return importflagForExport;
    }

    public void setImportflagForExport(String importflagForExport) {
        this.importflagForExport = importflagForExport;
    }

    public String getModuleForExport() {
        return moduleForExport;
    }

    public void setModuleForExport(String moduleForExport) {
        this.moduleForExport = moduleForExport;
    }

    public String getScreenNameForExport() {
        return screenNameForExport;
    }

    public void setScreenNameForExport(String screenNameForExport) {
        this.screenNameForExport = screenNameForExport;
    }

    public String getPrinterTray() {
        return printerTray;
    }

    public void setPrinterTray(String printerTray) {
        this.printerTray = printerTray;
    }

    public String getPostedLclBl() {
        return postedLclBl;
    }

    public void setPostedLclBl(String postedLclBl) {
        this.postedLclBl = postedLclBl;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBrandCompanyName() {
        return brandCompanyName;
    }

    public void setBrandCompanyName(String brandCompanyName) {
        this.brandCompanyName = brandCompanyName;
    }

    public boolean isExportFileCob() {
        return exportFileCob;
    }

    public void setExportFileCob(boolean exportFileCob) {
        this.exportFileCob = exportFileCob;
    }

    public Long getBlCorrectedId() {
        return blCorrectedId;
    }

    public void setBlCorrectedId(Long blCorrectedId) {
        this.blCorrectedId = blCorrectedId;
    }

    public String getIssuingTerminal() {
        return issuingTerminal;
    }

    public void setIssuingTerminal(String issuingTerminal) {
        this.issuingTerminal = issuingTerminal;
    }     

    public String getTerminalPhone() {
        return terminalPhone;
    }

    public void setTerminalPhone(String terminalPhone) {
        this.terminalPhone = terminalPhone;
    }

    public String getTerminalFax() {
        return terminalFax;
    }

    public void setTerminalFax(String terminalFax) {
        this.terminalFax = terminalFax;
    }

    public String getUserTerminalPhoneNo() {
        return userTerminalPhoneNo;
    }

    public void setUserTerminalPhoneNo(String userTerminalPhoneNo) {
        this.userTerminalPhoneNo = userTerminalPhoneNo;
    }

    public String getUserTerminalFaxNo() {
        return userTerminalFaxNo;
    }

    public void setUserTerminalFaxNo(String userTerminalFaxNo) {
        this.userTerminalFaxNo = userTerminalFaxNo;
    }
    
}
