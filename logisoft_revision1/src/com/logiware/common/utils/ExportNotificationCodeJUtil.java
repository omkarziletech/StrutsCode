/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.common.utils;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cong.logisoft.hibernate.dao.PrintConfigDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclConsolidateDAO;
import com.gp.cong.logisoft.lcl.report.LclBLPdfConfirmOnBoardNotice;
import com.gp.cong.logisoft.lcl.report.LclBLPdfCreator;
import com.gp.cong.logisoft.lcl.report.LclPdfCreator;
import com.gp.cong.logisoft.lcl.report.LclReportConstants;
import com.gp.cong.logisoft.web.HibernateSessionRequestFilter;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.common.job.JobScheduler;
import com.logiware.lcl.dao.ExportNotificationDAO;
import com.logiware.lcl.model.NotificationModel;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Transaction;

/**
 *
 * @author Mei
 */
public class ExportNotificationCodeJUtil implements LclCommonConstant, ConstantsInterface {

    private static final Logger log = Logger.getLogger(ExportNotificationCodeJUtil.class);
    private static String contextPath = JobScheduler.servletContext.getRealPath("/");
    private static String basePath = HibernateSessionRequestFilter.basePath;
    public Date now = new Date();

    public void sendBkgContactDispoNotification() throws Exception {
        ExportNotificationDAO notificationDAO = new ExportNotificationDAO();
        List<NotificationModel> dispoNotificationList = notificationDAO.getJobNotification("Pending", "Disposition", true);
        if (null != dispoNotificationList && !dispoNotificationList.isEmpty()) {
            EmailschedulerDAO emailDAO = new EmailschedulerDAO();
            String documentName = LclReportConstants.SCREENNAME_BOOKINGREPORT;
            LCLBookingDAO bookingDAO = new LCLBookingDAO();
            String outputFileName = LoadLogisoftProperties.getProperty("reportLocation");
            for (NotificationModel dispoBkg : dispoNotificationList) {
                try {
                    LclBooking lclBooking = bookingDAO.getByProperty("lclFileNumber.id", dispoBkg.getFileNumberId());
                    String fileNumber = lclBooking.getLclFileNumber().getFileNumber();
                    bookingDAO.getCurrentSession().evict(lclBooking);
                    String bussinessUnit = dispoBkg.getBussinessUnit();
                    String companyName = LoadLogisoftProperties.getProperty(bussinessUnit.equalsIgnoreCase("ECU") ? "application.ECU.companyname"
                            : bussinessUnit.equalsIgnoreCase("OTI") ? "application.OTI.companyname" : "application.Econo.companyname").toUpperCase();
                    String fromAddress = notificationDAO.getFromAddress(dispoBkg.getFileNumberId());
                    notificationDAO.setEmailDetails(bussinessUnit, dispoBkg);
                    String attachFile = outputFileName + "/Documents/LCL/" + documentName
                            + "/BookingConfirmationWithoutRate/" + DateUtils.formatDate(now, "yyyy/MM/dd");
                    File file = new File(attachFile);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    attachFile = attachFile + "/LCL_Booking_" + fileNumber + appendDateTimeSec() + ".pdf";
                    LclPdfCreator lclPdfCreator = new LclPdfCreator(lclBooking);
                    lclPdfCreator.createPdf(contextPath, attachFile, "Booking Confirmation Without Rate", null);
                    String contactData[] = dispoBkg.getToAddress().split("##");
                    for (String contact : contactData) {
                        String mailData[] = contact.split("-->");
                        if (mailData.length == 3) {
                            String emailBody = notificationDAO.emailBodyAppend(mailData[0],
                                    mailData[1], basePath, dispoBkg.getCompanyLogoPath(), dispoBkg.getCompanyWebsiteName());

                            if (RegexUtil.isEmail(mailData[2])) {
                                saveMailTransacations(emailDAO, fromAddress, mailData[2], attachFile, CONTACT_MODE_EMAIL,
                                        now, companyName, fileNumber, documentName, emailBody, dispoBkg.getId());
                            } else if (RegexUtil.isFax(mailData[2])) {
                                saveMailTransacations(emailDAO, fromAddress, mailData[2], attachFile, CONTACT_MODE_FAX,
                                        now, companyName, fileNumber, documentName, emailBody, dispoBkg.getId());
                            }
                        }
                    }
                    notificationDAO.updateNotification(dispoBkg.getId(), "Completed");
                } catch (Exception e) {
                    notificationDAO.updateNotification(dispoBkg.getId(), "Failed");
                    log.info("Sending Booking Contacts Update failed for DR(s) " + dispoBkg.getFileNumber() + " on " + now, e);
                } finally {

                }
            }
        }
    }

    public void saveMailTransacations(EmailschedulerDAO emailDAO, String fromAddress, String toEmail, String outputFileName,
            String emailType, Date today, String companyName, String fileNumber, String documentName,
            String emailBody, Long noticationId) throws Exception {
        EmailSchedulerVO email = new EmailSchedulerVO();
        email.setFromName("");
        email.setFromAddress(fromAddress);
        email.setToAddress(toEmail);
        email.setSubject(companyName + " " + fileNumber + " DOCK RECEIPT");
        email.setFileLocation(outputFileName);
        email.setStatus(EMAIL_STATUS_PENDING);
        email.setTextMessage(EXP_NOTIFICATION_TEXT_MESSAGE + "\n" + "LCL DOCK RECEIPT");
        email.setHtmlMessage(emailBody);
        email.setName("Disposition Change");
        email.setType(emailType);
        email.setModuleName(documentName);
        email.setModuleId(fileNumber);
        email.setUserName(USER_SYSTEM);
        email.setEmailDate(today);
        email.setModuleReferenceId(noticationId);
        emailDAO.save(email);
    }

    public void sendBlNotification() throws Exception {
        ExportNotificationDAO notificationDAO = new ExportNotificationDAO();
        Transaction transaction = notificationDAO.getCurrentSession().getTransaction();
        if (!transaction.isActive()) {
            transaction.begin();
        }
        List<NotificationModel> notificationList = notificationDAO.getJobNotification("Pending", "Disposition", false);
        if (null != notificationList && !notificationList.isEmpty()) {
            String reportLocationPath = LoadLogisoftProperties.getProperty("reportLocation");
            for (NotificationModel notification : notificationList) {
                try {
                    transaction = notificationDAO.getCurrentSession().getTransaction();
                    if (!transaction.isActive()) {
                        transaction.begin();
                    }
                    String sentEmailsDetails = "";
                    String bkgAcctNo = notificationDAO.getBkgAcctNo(notification.getFileNumberId(), false);
                    List<String> bkgAcctNoList = new ArrayList<String>(Arrays.asList(bkgAcctNo.split(",")));
                    String fromAddress = notificationDAO.getFromAddress(notification.getFileNumberId());
                    notification.setFromAddress(fromAddress);
                    notificationDAO.setEmailDetails(notification.getBussinessUnit(), notification);
                    String bussinessUnit = notification.getBussinessUnit();
                    String companyName = LoadLogisoftProperties.getProperty(bussinessUnit.equalsIgnoreCase("ECU") ? "application.ECU.companyname"
                            : bussinessUnit.equalsIgnoreCase("OTI") ? "application.OTI.companyname" : "application.Econo.companyname").toUpperCase();
                    if ("Posting".equalsIgnoreCase(notification.getFileStatus())) {
                        sentEmailsDetails = setPostContacts(notificationDAO, notification, bkgAcctNoList, companyName, reportLocationPath);
                    }
                    if ("Manifest".equalsIgnoreCase(notification.getFileStatus())) {
                        sentEmailsDetails = setManifestContacts(notificationDAO, notification, bkgAcctNoList, companyName, reportLocationPath);
                    }
                    if ("COB".equalsIgnoreCase(notification.getFileStatus())) {
                        sentEmailsDetails = setCOBContacts(notificationDAO, notification, bkgAcctNoList, companyName, reportLocationPath);
                    }
                    if ("Changes".equalsIgnoreCase(notification.getFileStatus())) {
                        sentEmailsDetails = setBLChangesContacts(notificationDAO, notification, bkgAcctNoList, companyName, reportLocationPath);
                    }
                    transaction = notificationDAO.getCurrentSession().getTransaction();
                    notificationDAO.updateNotification(notification.getId(), sentEmailsDetails, "Completed");
                    transaction.commit();
                } catch (Exception e) {
                    transaction = notificationDAO.getCurrentSession().getTransaction();
                    notificationDAO.updateNotification(notification.getId(), "Failed");
                    transaction.commit();
                    log.info("Sending BL's Update failed for DR(s) " + notification.getFileNumber() + " on " + now, e);
                } finally {
                    if (transaction.isActive() && notificationDAO.getCurrentSession().isConnected()
                            && notificationDAO.getCurrentSession().isOpen()) {
                        transaction.rollback();
                    }
                }
            }
        }
        transaction = notificationDAO.getCurrentSession().getTransaction();
        if (!transaction.isActive()) {
            transaction.begin();
        }
    }

    public String setPostContacts(ExportNotificationDAO notificationDAO, NotificationModel notification,
            List<String> bkgAcctNoList, String companyName, String reportLocationPath) throws Exception {
        String sentEmailsDetails = "";
        List<NotificationModel> contactsList = new ArrayList<NotificationModel>();
        NotificationModel BkgContactModel = notificationDAO
                .getBookingContact(notification.getFileNumberId());
        if (null != BkgContactModel) {
            String Email = "";
            boolean isSystemContact = notificationDAO
                    .getSystemContact(BkgContactModel.getAccountNo(), BkgContactModel.getToName());
            if (isSystemContact) {
                NotificationModel bkgContacts = notificationDAO
                        .getCodeJBkgContacts(notification.getFileNumberId(), notification.getFileStatus());
                if (null != bkgContacts) {
                    contactsList.add(bkgContacts);
                    Email = bkgContacts.getToEmail();
                }
            } else {
                NotificationModel bkgNewContacts = notificationDAO.getBkgNewContact(notification.getFileNumberId(), "post");
                if (null != bkgNewContacts) {
                    contactsList.add(bkgNewContacts);
                    Email = bkgNewContacts.getToEmail();
                }
            }
            List<NotificationModel> acctNoContactList = notificationDAO
                    .getCodeJPostingContactList(true, bkgAcctNoList, Email, false);
            contactsList.addAll(acctNoContactList);
            String consAcctNo = new LCLBookingDAO().getExportBookingColumnValue("cons_acct_no",
                    notification.getFileNumberId().toString());
            if (CommonUtils.isNotEmpty(consAcctNo)) {
                List<String> consAcctNoList = new ArrayList<>();
                consAcctNoList.add(consAcctNo);
                acctNoContactList = notificationDAO.getCodeJPostingContactList(true, consAcctNoList, Email, true);
                contactsList.addAll(acctNoContactList);
            }
        } else {
            List<NotificationModel> acctNoContactList = notificationDAO.getCodeJPostingContactList(false, bkgAcctNoList, "", false);
            contactsList.addAll(acctNoContactList);
            String consAcctNo = new LCLBookingDAO().getExportBookingColumnValue("cons_acct_no",
                    notification.getFileNumberId().toString());
            if (CommonUtils.isNotEmpty(consAcctNo)) {
                List<String> consAcctNoList = new ArrayList<>();
                consAcctNoList.add(consAcctNo);
                acctNoContactList = notificationDAO.getCodeJPostingContactList(false, consAcctNoList, "", true);
                contactsList.addAll(acctNoContactList);
            }
        }
        if (!contactsList.isEmpty()) {
            sentEmailsDetails = setBlEmailByPost(contactsList, notification, companyName, reportLocationPath);
        }
        return sentEmailsDetails;
    }

    public String setBlEmailByPost(List<NotificationModel> contactsList, NotificationModel notification,
            String companyName, String reportLocationPath) throws Exception {
        File file = new File(reportLocationPath + "/Documents/LCL/LclBl/CodeJ_Job/" + DateUtils.formatDate(now, "yyyy/MM/dd")
                + "/" + notification.getFileStatus() + "/");
        if (!file.exists()) {
            file.mkdirs();
        }
        StringBuilder emailAppend = new StringBuilder();
        LclBLPdfCreator lclBLPdfCreator = new LclBLPdfCreator();
        String ratedCompanyName = "", unRatedCompanyName = "", ratedmsg = "", unRatedmsg = "",
                ratedOutPutFileName = "", unRatedOutputFileName = "";
        Boolean ratedFlag = true, unRatedFlag = true;
        for (NotificationModel contacts : contactsList) {
            String emailBody = new ExportNotificationDAO().emailBodyAppend(contacts.getToName(), contacts.getCompanyName(),
                    basePath, notification.getCompanyLogoPath(), notification.getCompanyWebsiteName());
            notification.setEmailBody(emailBody);
            String toEmailOrFax = "", toEmailOrFaxMode = "";
            if (contacts.getNonRated() && ratedFlag) {
                lclBLPdfCreator.setPrintdocumentName("Bill Of Lading");
                ratedmsg = "Bill Of Lading Rated(Non Negotiable)";
                ratedCompanyName = companyName + " " + notification.getFileNumber() + " Bill Of Lading Rated(Non Negotiable)";
                ratedOutPutFileName = reportLocationPath + "/Documents/LCL/LclBl/CodeJ_Job/" + DateUtils.formatDate(now, "yyyy/MM/dd") + "/" + notification.getFileStatus()
                        + "/BillofLading_Rated_" + notification.getFileNumber() + appendDateTimeSec() + ".pdf";
                lclBLPdfCreator.createReportJob(contextPath, ratedOutPutFileName, "Bill Of Lading",
                        notification.getFileNumberId().toString(), true);
                ratedFlag = false;
            }
            if (contacts.getNonUnRated() && unRatedFlag) {
                unRatedmsg = "Bill Of Lading UnRated";
                unRatedCompanyName = companyName + " " + notification.getFileNumber() + " Bill Of Lading UnRated";
                unRatedOutputFileName = reportLocationPath + "/Documents/LCL/LclBl/CodeJ_Job/" + DateUtils.formatDate(now, "yyyy/MM/dd")
                        + "/" + notification.getFileStatus() + "/BillofLading_UnRated_" + notification.getFileNumber() + appendDateTimeSec() + ".pdf";
                lclBLPdfCreator.createReportJob(contextPath, unRatedOutputFileName, "Bill Of Lading",
                        notification.getFileNumberId().toString(), false);
                unRatedFlag = false;
            }
            if (RegexUtil.isEmail(contacts.getToEmail())) {
                toEmailOrFax = contacts.getToEmail();
                toEmailOrFaxMode = CONTACT_MODE_EMAIL;
            } else if (RegexUtil.isFax(contacts.getToEmail())) {
                toEmailOrFax = contacts.getToEmail();
                toEmailOrFaxMode = CONTACT_MODE_FAX;
            }
            if (contacts.getNonRated() && !"".equalsIgnoreCase(toEmailOrFax)) {
                saveMailBlTransacations(notification.getFromAddress(), toEmailOrFax, ratedOutPutFileName, ratedmsg, toEmailOrFaxMode,
                        ratedCompanyName, notification.getFileNumber(), notification.getFileStatus(),
                        notification.getEmailBody(), notification.getId());
            }
            if (contacts.getNonUnRated() && !"".equalsIgnoreCase(toEmailOrFax)) {
                saveMailBlTransacations(notification.getFromAddress(), toEmailOrFax, unRatedOutputFileName, unRatedmsg, toEmailOrFaxMode,
                        unRatedCompanyName, notification.getFileNumber(), notification.getFileStatus(),
                        notification.getEmailBody(), notification.getId());
            }
            toEmailOrFax = "";
            toEmailOrFaxMode = "";
            emailAppend.append(contacts.getToName()).append("-->").append(contacts.getCompanyName()).append("-->").append(contacts.getToEmail()).append(",");
        }
        return emailAppend.toString();
    }

    public String setCOBContacts(ExportNotificationDAO notificationDAO, NotificationModel notification,
            List<String> bkgAcctNoList, String companyName, String reportLocationPath) throws Exception {
        String sentEmails = "";
        List<NotificationModel> contactsList = new ArrayList<NotificationModel>();
        NotificationModel BkgContactModel = notificationDAO
                .getBookingContact(notification.getFileNumberId());
        if (null != BkgContactModel) {
            String Email = "";
            boolean isSystemContact = notificationDAO
                    .getSystemContact(BkgContactModel.getAccountNo(), BkgContactModel.getToName());
            if (isSystemContact) {
                NotificationModel bkgContacts = notificationDAO
                        .getCodeJBkgContacts(notification.getFileNumberId(), notification.getFileStatus());
                if (null != bkgContacts) {
                    contactsList.add(bkgContacts);
                    Email = bkgContacts.getToEmail();
                }
            } else {
                NotificationModel bkgNewContacts = notificationDAO.getBkgNewContact(notification.getFileNumberId(), "cob");
                if (null != bkgNewContacts) {
                    contactsList.add(bkgNewContacts);
                    Email = bkgNewContacts.getToEmail();
                }
            }
            List<NotificationModel> acctNoContactList = notificationDAO
                    .getCodeJCobContactList(true, bkgAcctNoList, Email, false);
            contactsList.addAll(acctNoContactList);
            String consAcctNo = new LCLBookingDAO().getExportBookingColumnValue("cons_acct_no",
                    notification.getFileNumberId().toString());
            if (CommonUtils.isNotEmpty(consAcctNo)) {
                List<String> consAcctNoList = new ArrayList<>();
                consAcctNoList.add(consAcctNo);
                acctNoContactList = notificationDAO.getCodeJCobContactList(true, consAcctNoList, Email, true);
                contactsList.addAll(acctNoContactList);
            }
        } else {
            List<NotificationModel> acctNoContactList = notificationDAO.getCodeJCobContactList(false, bkgAcctNoList, "", false);
            contactsList.addAll(acctNoContactList);
            String consAcctNo = new LCLBookingDAO().getExportBookingColumnValue("cons_acct_no",
                    notification.getFileNumberId().toString());
            if (CommonUtils.isNotEmpty(consAcctNo)) {
                List<String> consAcctNoList = new ArrayList<>();
                consAcctNoList.add(consAcctNo);
                acctNoContactList = notificationDAO.getCodeJCobContactList(false, consAcctNoList, "", true);
                contactsList.addAll(acctNoContactList);
            }
        }
        sentEmails = setBlEmailByCOB(contactsList, notification, companyName, reportLocationPath);
        return sentEmails;
    }

    public String setBlEmailByCOB(List<NotificationModel> contactsList, NotificationModel notification,
            String companyName, String reportLocationPath) throws Exception {
        File file = new File(reportLocationPath + "/Documents/LCL/LclBl/CodeJ_Job/"
                + DateUtils.formatDate(now, "yyyy/MM/dd") + "/" + notification.getFileStatus() + "/");
        if (!file.exists()) {
            file.mkdirs();
        }
        StringBuilder sentEmailsAppend = new StringBuilder();
        String ratedCompanyName = "", unRatedCompanyName = "", freightCompanyName = "", cobCompanyMsg = "", ratedmsg = "", unRatedmsg = "",
                ratedOutPutFileName = "", unRatedOutputFileName = "",
                freightInvOutputFileName = "", freightMsg = "", cobMsg = "", cobOutputFileName = "";
        Boolean ratedFlag = true, cobFlag = true, unRatedFlag = true;
        LclBLPdfCreator lclBLPdfCreator = new LclBLPdfCreator();
        String[] consolidateFile = new LclConsolidateDAO().getParentConsolidateFile(notification.getFileNumberId().toString());
        if (consolidateFile != null && consolidateFile[0] != null) {
            notification.setFileNumberId(Long.parseLong(consolidateFile[0]));
        }
        String billToParty = new PrintConfigDAO().getBillToPartyListForExport(notification.getFileNumberId());
        if (null != billToParty && !"".equalsIgnoreCase(billToParty)) {
            for (String party : billToParty.split(",")) {
                List<NotificationModel> freightContactList = new ArrayList<NotificationModel>();
                String exportBilltoParty = party.contains("Forwarder")
                        ? "F" : party.contains("Agent") ? "A" : party.contains("Shipper")
                                        ? "S" : party.contains("Third Party") ? "T" : "";
                String columnName = party.contains("Forwarder")
                        ? "fwd_acct_no" : party.contains("Agent") ? "agent_acct_no" : party.contains("Shipper")
                                        ? "ship_acct_no" : party.contains("Third Party") ? "third_party_acct_no" : "";
                String columnName1 = party.contains("Forwarder")
                        ? "fwd_contact_id" : party.contains("Agent") ? "agent_contact_id" : party.contains("Shipper")
                                        ? "ship_contact_id" : party.contains("Third Party") ? "third_party_contact_id" : "";
                List<NotificationModel> freightBlContactList = new ExportNotificationDAO().getCodeJFreightContactList(notification.getFileNumberId(),
                        columnName, columnName1, true);
                if (!freightBlContactList.isEmpty()) {
                    freightContactList.addAll(freightBlContactList);
                }
//                if (!"A".equalsIgnoreCase(exportBilltoParty) && CommonUtils.isEmpty(freightBlContactList)) {
//                    NotificationModel freightNewBkgContacts = new ExportNotificationDAO().getBkgNewContactByFreight(notification.getFileNumberId());
//                    if (null != freightNewBkgContacts) {
//                        freightContactList.add(freightNewBkgContacts);
//                    }
//                }
                if (!freightContactList.isEmpty()) {
                    lclBLPdfCreator.setExportBilltoParty(exportBilltoParty);
                    freightCompanyName = companyName + " " + notification.getFileNumber() + " LCL Freight Invoice(" + party + ")";
                    freightInvOutputFileName = reportLocationPath + "/Documents/LCL/LclBl/CodeJ_Job/" + DateUtils.formatDate(now, "yyyy/MM/dd")
                            + "/" + notification.getFileStatus() + "/FreightInvoice_" + party + "_" + notification.getFileNumber() + appendDateTimeSec() + ".pdf";
                    lclBLPdfCreator.createReportJob(contextPath, freightInvOutputFileName, "LCL Freight Invoice",
                            notification.getFileNumberId().toString(), true);
                    String toEmailOrFax = "", emailOrFaxMode = "";
                    for (NotificationModel freightCon : freightContactList) {
                        String emailBody = new ExportNotificationDAO().emailBodyAppend(freightCon.getToName(), freightCon.getCompanyName(),
                                basePath, notification.getCompanyLogoPath(), notification.getCompanyWebsiteName());
                        notification.setEmailBody(emailBody);
                        if (RegexUtil.isEmail(freightCon.getToEmail())) {
                            toEmailOrFax = freightCon.getToEmail();
                            emailOrFaxMode = CONTACT_MODE_EMAIL;
                        } else if (RegexUtil.isFax(freightCon.getToEmail())) {
                            toEmailOrFax = freightCon.getToEmail();
                            emailOrFaxMode = CONTACT_MODE_FAX;
                        }
                        if (!"".equalsIgnoreCase(toEmailOrFax)) {
                            saveMailBlTransacations(notification.getFromAddress(), toEmailOrFax, freightInvOutputFileName, freightMsg,
                                    emailOrFaxMode, freightCompanyName, notification.getFileNumber(),
                                    notification.getFileStatus(), notification.getEmailBody(), notification.getId());
                        }
                        toEmailOrFax = "";
                        emailOrFaxMode = "";
                        sentEmailsAppend.append(freightCon.getToName()).append("-->").append(freightCon.getCompanyName()).append("-->").append(freightCon.getToEmail()).append(",");
                    }
                }
            }

        }
        lclBLPdfCreator.setExportBilltoParty("");
        if (!contactsList.isEmpty()) {
            for (NotificationModel contacts : contactsList) {
                String emailBody = new ExportNotificationDAO().emailBodyAppend(contacts.getToName(), contacts.getCompanyName(),
                        basePath, notification.getCompanyLogoPath(), notification.getCompanyWebsiteName());
                notification.setEmailBody(emailBody);
                String toEmailOrFax = "", emailOrFaxMode = "";
                if (contacts.getNonRated() && ratedFlag) {
                    lclBLPdfCreator.setPrintdocumentName("Bill Of Lading");
                    ratedmsg = "Bill Of Lading Rated(Non Negotiable)";
                    ratedCompanyName = companyName + " " + notification.getFileNumber() + " Bill Of Lading Rated(Non Negotiable)";
                    ratedOutPutFileName = reportLocationPath + "/Documents/LCL/LclBl/CodeJ_Job/"
                            + DateUtils.formatDate(now, "yyyy/MM/dd") + "/" + notification.getFileStatus()
                            + "/BillofLading_Rated_" + notification.getFileNumber() + appendDateTimeSec() + ".pdf";
                    lclBLPdfCreator.createReportJob(contextPath, ratedOutPutFileName, "Bill Of Lading",
                            notification.getFileNumberId().toString(), true);
                    ratedFlag = false;
                }
                if (contacts.getNonUnRated() && unRatedFlag) {
                    unRatedmsg = "Bill Of Lading UnRated";
                    unRatedCompanyName = companyName + " " + notification.getFileNumber() + " Bill Of Lading UnRated";
                    unRatedOutputFileName = reportLocationPath + "/Documents/LCL/LclBl/CodeJ_Job/" + DateUtils.formatDate(now, "yyyy/MM/dd") + "/"
                            + notification.getFileStatus() + "/BillofLading_UnRated_" + notification.getFileNumber() + appendDateTimeSec() + ".pdf";
                    lclBLPdfCreator.createReportJob(contextPath, unRatedOutputFileName, "Bill Of Lading",
                            notification.getFileNumberId().toString(), false);
                    unRatedFlag = false;
                }

                if (contacts.getCob() && cobFlag) {
                    freightMsg = "Confirm On Board";
                    cobCompanyMsg = companyName + " " + notification.getFileNumber() + " Confirm On Board";
                    cobOutputFileName = reportLocationPath + "/Documents/LCL/LclBl/CodeJ_Job/" + DateUtils.formatDate(now, "yyyy/MM/dd") + "/" + notification.getFileStatus()
                            + "/ConfirmOnBoardNotice_" + notification.getFileNumber() + appendDateTimeSec() + ".pdf";
                    new LclBLPdfConfirmOnBoardNotice().createReport(contextPath, notification.getFileNumberId().toString(), cobOutputFileName);
                    cobFlag = false;
                }

                if (RegexUtil.isEmail(contacts.getToEmail())) {
                    toEmailOrFax = contacts.getToEmail();
                    emailOrFaxMode = CONTACT_MODE_EMAIL;
                } else if (RegexUtil.isFax(contacts.getToEmail())) {
                    toEmailOrFax = contacts.getToEmail();
                    emailOrFaxMode = CONTACT_MODE_FAX;
                }
                if (contacts.getNonRated() && CommonUtils.isNotEmpty(ratedOutPutFileName) && !"".equalsIgnoreCase(toEmailOrFax)) {
                    saveMailBlTransacations(notification.getFromAddress(), toEmailOrFax, ratedOutPutFileName, ratedmsg, emailOrFaxMode,
                            ratedCompanyName, notification.getFileNumber(), notification.getFileStatus(),
                            notification.getEmailBody(), notification.getId());
                }
                if (contacts.getNonUnRated() && CommonUtils.isNotEmpty(unRatedOutputFileName) && !"".equalsIgnoreCase(toEmailOrFax)) {
                    saveMailBlTransacations(notification.getFromAddress(), toEmailOrFax, unRatedOutputFileName, unRatedmsg, emailOrFaxMode,
                            unRatedCompanyName, notification.getFileNumber(), notification.getFileStatus(),
                            notification.getEmailBody(), notification.getId());
                }
                if (contacts.getCob() && CommonUtils.isNotEmpty(cobOutputFileName) && !"".equalsIgnoreCase(toEmailOrFax)) {
                    saveMailBlTransacations(notification.getFromAddress(), toEmailOrFax, cobOutputFileName, cobMsg, emailOrFaxMode,
                            cobCompanyMsg, notification.getFileNumber(), notification.getFileStatus(),
                            notification.getEmailBody(), notification.getId());
                }
                toEmailOrFax = "";
                emailOrFaxMode = "";
                sentEmailsAppend.append(contacts.getToName()).append("-->").append(contacts.getCompanyName()).append("-->").append(contacts.getToEmail()).append(",");
            }
        }
        return sentEmailsAppend.toString();
    }

    public void saveMailBlTransacations(String fromAddres, String toEmailOrFax, String outputFileName, String msgType,
            String emailOrFaxMode, String subjectName, String fileNumber, String fileStatus, String emailBody, Long noticationId) throws Exception {
        log.info("Bill Of Ladding Status" + fileStatus);
        EmailSchedulerVO email = new EmailSchedulerVO();
        email.setFromName("");
        email.setFromAddress(fromAddres);
        email.setToAddress(toEmailOrFax);
        email.setSubject(subjectName);
        email.setFileLocation(outputFileName);
        email.setStatus(EMAIL_STATUS_PENDING);
        email.setTextMessage(EXP_NOTIFICATION_CODJ_MESSAGE + "\n" + msgType);
        email.setHtmlMessage(emailBody);
        email.setName("LCLBL " + fileStatus);
        email.setType(emailOrFaxMode);
        email.setModuleName("LCLBL " + fileStatus);
        email.setModuleId(fileNumber);
        email.setUserName(USER_SYSTEM);
        email.setEmailDate(now);
        email.setModuleReferenceId(noticationId);
        new EmailschedulerDAO().save(email);
    }

    public String setManifestContacts(ExportNotificationDAO notificationDAO, NotificationModel notification,
            List<String> bkgAcctNoList, String companyName, String reportLocationPath) throws Exception {
        List<NotificationModel> contactsList = new ArrayList<NotificationModel>();
        String sentEmailAppend = "";
        NotificationModel BkgContactModel = notificationDAO
                .getBookingContact(notification.getFileNumberId());
        if (null != BkgContactModel) {
            String Email = "";
            boolean isSystemContact = notificationDAO
                    .getSystemContact(BkgContactModel.getAccountNo(), BkgContactModel.getToName());
            if (isSystemContact) {
                NotificationModel bkgContacts = notificationDAO
                        .getCodeJBkgContacts(notification.getFileNumberId(), notification.getFileStatus());
                if (null != bkgContacts) {
                    contactsList.add(bkgContacts);
                    Email = bkgContacts.getToEmail();
                }
            } // no need to check new contacts for manifest
            List<NotificationModel> acctNoContactList = notificationDAO
                    .getCodeJManifestContactList(true, bkgAcctNoList, Email, false);
            contactsList.addAll(acctNoContactList);
            String consAcctNo = new LCLBookingDAO().getExportBookingColumnValue("cons_acct_no",
                    notification.getFileNumberId().toString());
            if (CommonUtils.isNotEmpty(consAcctNo)) {
                List<String> consAcctNoList = new ArrayList<>();
                consAcctNoList.add(consAcctNo);
                acctNoContactList = notificationDAO.getCodeJManifestContactList(true, consAcctNoList, Email, true);
                contactsList.addAll(acctNoContactList);
            }
        } else {
            List<NotificationModel> acctNoContactList = notificationDAO.getCodeJManifestContactList(false, bkgAcctNoList, "", false);
            contactsList.addAll(acctNoContactList);
            String consAcctNo = new LCLBookingDAO().getExportBookingColumnValue("cons_acct_no",
                    notification.getFileNumberId().toString());
            if (CommonUtils.isNotEmpty(consAcctNo)) {
                List<String> consAcctNoList = new ArrayList<>();
                consAcctNoList.add(consAcctNo);
                acctNoContactList = notificationDAO.getCodeJManifestContactList(false, consAcctNoList, "", true);
                contactsList.addAll(acctNoContactList);
            }
        }
        sentEmailAppend = setBlEmailByManifest(contactsList, notification, companyName, reportLocationPath);
        return sentEmailAppend;
    }

    public String setBlEmailByManifest(List<NotificationModel> contactsList,
            NotificationModel notification, String companyName, String reportLocationPath) throws Exception {
        File file = new File(reportLocationPath + "/Documents/LCL/LclBl/CodeJ_Job/"
                + DateUtils.formatDate(now, "yyyy/MM/dd") + "/" + notification.getFileStatus() + "/");
        if (!file.exists()) {
            file.mkdirs();
        }
        String ratedCompanyName = "", unRatedCompanyName = "", freightCompanyName = "", ratedmsg = "", unRatedmsg = "",
                ratedOutPutFileName = "", unRatedOutputFileName = "", freightInvOutputFileName = "", freightMsg = "";
        Boolean ratedFlag = true, unRatedFlag = true;
        StringBuilder sentEmailsAppend = new StringBuilder();
        LclBLPdfCreator lclBLPdfCreator = new LclBLPdfCreator();
        String[] consolidateFile = new LclConsolidateDAO().getParentConsolidateFile(notification.getFileNumberId().toString());
        if (consolidateFile != null && consolidateFile[0] != null) {
            notification.setFileNumberId(Long.parseLong(consolidateFile[0]));
        }
        // code for sending freight invoice 
//        String billToParty = new PrintConfigDAO().getBillToPartyListForExport(notification.getFileNumberId());       
//        if (null != billToParty && !"".equalsIgnoreCase(billToParty)) {
//            for (String party : billToParty.split(",")) {
//                List<NotificationModel> freightContactList = new ArrayList<NotificationModel>();
//                String exportBilltoParty = party.contains("Forwarder")
//                        ? "F" : party.contains("Agent") ? "A" : party.contains("Shipper")
//                                        ? "S" : party.contains("Third Party") ? "T" : "";
//                String columnName = party.contains("Forwarder")
//                        ? "fwd_acct_no" : party.contains("Agent") ? "agent_acct_no" : party.contains("Shipper")
//                                        ? "ship_acct_no" : party.contains("Third Party") ? "third_party_acct_no" : "";
//                String columnName1 = party.contains("Forwarder")
//                        ? "fwd_contact_id" : party.contains("Agent") ? "agent_contact_id" : party.contains("Shipper")
//                                        ? "ship_contact_id" : party.contains("Third Party") ? "third_party_contact_id" : "";
//
//                List<NotificationModel> freightInvContacts
//                        = new ExportNotificationDAO().getCodeJFreightContactList(notification.getFileNumberId(),
//                                columnName, columnName1, false);
//
//                if (!freightInvContacts.isEmpty()) {
//                    freightContactList.addAll(freightInvContacts);
//                }
////                if (!"A".equalsIgnoreCase(exportBilltoParty) && freightContactList.isEmpty()) {
////                    NotificationModel freightNewBkgContacts = new ExportNotificationDAO().getBkgNewContactByFreight(notification.getFileNumberId());
////                    if (null != freightNewBkgContacts) {
////                        freightContactList.add(freightNewBkgContacts);
////                    }
////                }
//                if (!freightContactList.isEmpty()) {
//                    lclBLPdfCreator.setExportBilltoParty(exportBilltoParty);
//                    freightCompanyName = companyName + " " + notification.getFileNumber() + " LCL Freight Invoice(" + party + ")";
//                    freightInvOutputFileName = reportLocationPath + "/Documents/LCL/LclBl/CodeJ_Job/"
//                            + DateUtils.formatDate(now, "yyyy/MM/dd") + "/" + notification.getFileStatus()
//                            + "/FreightInvoice_" + party + "_" + notification.getFileNumber() + appendDateTimeSec() + ".pdf";
//                    lclBLPdfCreator.createReportJob(contextPath, freightInvOutputFileName, "LCL Freight Invoice",
//                            notification.getFileNumberId().toString(), true);
//                    String toEmailOrFax = "", emailOrFaxMode = "";
//                    for (NotificationModel freightInv : freightContactList) {
//                        String emailBody = new ExportNotificationDAO().emailBodyAppend(freightInv.getToName(), freightInv.getCompanyName(),
//                                basePath, notification.getCompanyLogoPath(), notification.getCompanyWebsiteName());
//                        notification.setEmailBody(emailBody);
//                        if (RegexUtil.isEmail(freightInv.getToEmail())) {
//                            toEmailOrFax = freightInv.getToEmail();
//                            emailOrFaxMode = CONTACT_MODE_EMAIL;
//                        } else if (RegexUtil.isFax(freightInv.getToEmail())) {
//                            toEmailOrFax = freightInv.getToEmail();
//                            emailOrFaxMode = CONTACT_MODE_FAX;
//                        }
//                        if (!"".equalsIgnoreCase(toEmailOrFax)) {
//                            saveMailBlTransacations(notification.getFromAddress(), toEmailOrFax, freightInvOutputFileName, freightMsg, emailOrFaxMode,
//                                    freightCompanyName, notification.getFileNumber(), notification.getFileStatus(),
//                                    notification.getEmailBody(), notification.getId());
//                        }
//                        toEmailOrFax = "";
//                        emailOrFaxMode = "";
//                        sentEmailsAppend.append(freightInv.getToName()).append("-->").append(freightInv.getCompanyName()).append("-->").append(freightInv.getToEmail()).append(",");
//                    }
//                }
//                lclBLPdfCreator.setExportBilltoParty("");
//            }
//        }
        
        
        
        lclBLPdfCreator.setExportBilltoParty("");
        if (!contactsList.isEmpty()) {
            for (NotificationModel contacts : contactsList) {
                String emailBody = new ExportNotificationDAO().emailBodyAppend(contacts.getToName(), contacts.getCompanyName(),
                        basePath, notification.getCompanyLogoPath(), notification.getCompanyWebsiteName());
                notification.setEmailBody(emailBody);
                String toEmailOrFax = "", emailOrFaxMode = "";
                if (contacts.getNonRated() && ratedFlag) {
                    lclBLPdfCreator.setPrintdocumentName("Bill Of Lading");
                    ratedmsg = "Bill Of Lading Rated(Non Negotiable)";
                    ratedCompanyName = companyName + " " + notification.getFileNumber() + " Bill Of Lading Rated(Non Negotiable)";
                    ratedOutPutFileName = reportLocationPath + "/Documents/LCL/LclBl/CodeJ_Job/"
                            + DateUtils.formatDate(now, "yyyy/MM/dd") + "/" + notification.getFileStatus()
                            + "/BillofLading_Rated_" + notification.getFileNumber() + appendDateTimeSec() + ".pdf";
                    lclBLPdfCreator.createReportJob(contextPath, ratedOutPutFileName, "Bill Of Lading",
                            notification.getFileNumberId().toString(), true);
                    ratedFlag = false;
                }
                if (contacts.getNonUnRated() && unRatedFlag) {
                    unRatedmsg = "Bill Of Lading UnRated";
                    unRatedCompanyName = companyName + " " + notification.getFileNumber() + " Bill Of Lading UnRated";
                    unRatedOutputFileName = reportLocationPath + "/Documents/LCL/LclBl/CodeJ_Job/" + DateUtils.formatDate(now, "yyyy/MM/dd") + "/" + notification.getFileStatus()
                            + "/BillofLading_UnRated_" + notification.getFileNumber() + appendDateTimeSec() + ".pdf";
                    lclBLPdfCreator.createReportJob(contextPath, unRatedOutputFileName, "Bill Of Lading",
                            notification.getFileNumberId().toString(), false);
                    unRatedFlag = false;
                }

                if (RegexUtil.isEmail(contacts.getToEmail())) {
                    toEmailOrFax = contacts.getToEmail();
                    emailOrFaxMode = CONTACT_MODE_EMAIL;
                } else if (RegexUtil.isFax(contacts.getToEmail())) {
                    toEmailOrFax = contacts.getToEmail();
                    emailOrFaxMode = CONTACT_MODE_FAX;
                }
                if (contacts.getNonRated() && CommonUtils.isNotEmpty(ratedOutPutFileName) && !"".equalsIgnoreCase(toEmailOrFax)) {
                    saveMailBlTransacations(notification.getFromAddress(), toEmailOrFax, ratedOutPutFileName, ratedmsg, emailOrFaxMode,
                            ratedCompanyName, notification.getFileNumber(), notification.getFileStatus(),
                            notification.getEmailBody(), notification.getId());
                }
                if (contacts.getNonUnRated() && CommonUtils.isNotEmpty(unRatedOutputFileName) && !"".equalsIgnoreCase(toEmailOrFax)) {
                    saveMailBlTransacations(notification.getFromAddress(), toEmailOrFax, unRatedOutputFileName, unRatedmsg, emailOrFaxMode,
                            unRatedCompanyName, notification.getFileNumber(), notification.getFileStatus(),
                            notification.getEmailBody(), notification.getId());
                }
                toEmailOrFax = "";
                emailOrFaxMode = "";
                sentEmailsAppend.append(contacts.getToName()).append("-->").append(contacts.getCompanyName()).append("-->").append(contacts.getToEmail()).append(",");
            }
        }
        return sentEmailsAppend.toString();
    }

    public String setBLChangesContacts(ExportNotificationDAO notificationDAO, NotificationModel notification,
            List<String> bkgAcctNoList, String companyName, String reportLocationPath) throws Exception {
        List<NotificationModel> contactsList = new ArrayList<NotificationModel>();
        String sentEmails = "";

        NotificationModel BkgContactModel = notificationDAO
                .getBookingContact(notification.getFileNumberId());
        if (null != BkgContactModel) {
            String Email = "";
            boolean isSystemContact = notificationDAO
                    .getSystemContact(BkgContactModel.getAccountNo(), BkgContactModel.getToName());
            if (isSystemContact) {
                NotificationModel bkgContacts = notificationDAO
                        .getCodeJBkgContacts(notification.getFileNumberId(), notification.getFileStatus());
                if (null != bkgContacts) {
                    contactsList.add(bkgContacts);
                    Email = bkgContacts.getToEmail();
                }
            } // no need to check new contacts for Bl Changes
            List<NotificationModel> acctNoContactList = notificationDAO
                    .getCodeJChangesContactList(true, bkgAcctNoList, Email, false);
            contactsList.addAll(acctNoContactList);
            String consAcctNo = new LCLBookingDAO().getExportBookingColumnValue("cons_acct_no",
                    notification.getFileNumberId().toString());
            if (CommonUtils.isNotEmpty(consAcctNo)) {
                List<String> consAcctNoList = new ArrayList<>();
                consAcctNoList.add(consAcctNo);
                acctNoContactList = notificationDAO.getCodeJChangesContactList(true, consAcctNoList, Email, true);
                contactsList.addAll(acctNoContactList);
            }
        } else {
            List<NotificationModel> acctNoContactList = notificationDAO.getCodeJChangesContactList(false, bkgAcctNoList, "", false);
            contactsList.addAll(acctNoContactList);
            String consAcctNo = new LCLBookingDAO().getExportBookingColumnValue("cons_acct_no",
                    notification.getFileNumberId().toString());
            if (CommonUtils.isNotEmpty(consAcctNo)) {
                List<String> consAcctNoList = new ArrayList<>();
                consAcctNoList.add(consAcctNo);
                acctNoContactList = notificationDAO.getCodeJChangesContactList(false, consAcctNoList, "", true);
                contactsList.addAll(acctNoContactList);
            }
        }
        if (!contactsList.isEmpty()) {
            sentEmails = setBLChangesContacts(contactsList, notification, companyName, reportLocationPath);
        }
        return sentEmails;
    }

    public String setBLChangesContacts(List<NotificationModel> contactsList,
            NotificationModel notification, String companyName, String reportLocationPath) throws Exception {
        String outputFileName = LoadLogisoftProperties.getProperty("reportLocation");
        File file = new File(outputFileName + "/Documents/LCL/LclBl/CodeJ_Job/" + DateUtils.formatDate(now, "yyyy/MM/dd")
                + "/" + notification.getFileStatus() + "/");
        if (!file.exists()) {
            file.mkdirs();
        }
        StringBuilder sentEmailsAppend = new StringBuilder();
        LclBLPdfCreator lclBLPdfCreator = new LclBLPdfCreator();
        String ratedCompanyName = "", unRatedCompanyName = "", ratedmsg = "", unRatedmsg = "",
                ratedOutPutFileName = "", unRatedOutputFileName = "";
        Boolean ratedFlag = true, unRatedFlag = true;
        for (NotificationModel contacts : contactsList) {
            String emailBody = new ExportNotificationDAO().emailBodyAppend(contacts.getToName(), contacts.getCompanyName(),
                    basePath, notification.getCompanyLogoPath(), notification.getCompanyWebsiteName());
            notification.setEmailBody(emailBody);
            System.out.println("BL Changes EMail----->" + contacts.getToEmail());
            String toEmailOrFax = "", emailOrFaxMode = "";
            if (contacts.getNonRated() && ratedFlag) {
                lclBLPdfCreator.setPrintdocumentName("Bill Of Lading");
                ratedmsg = "Bill Of Lading Rated(Non Negotiable)";
                ratedCompanyName = companyName + " " + notification.getFileNumber() + " Bill Of Lading Rated(Non Negotiable)";
                ratedOutPutFileName = outputFileName + "/Documents/LCL/LclBl/CodeJ_Job/"
                        + DateUtils.formatDate(now, "yyyy/MM/dd") + "/" + notification.getFileStatus()
                        + "/BillofLading_Rated_" + notification.getFileNumber() + appendDateTimeSec() + ".pdf";
                lclBLPdfCreator.createReportJob(contextPath, ratedOutPutFileName, "Bill Of Lading",
                        notification.getFileNumberId().toString(), true);
                ratedFlag = false;
            }
            if (contacts.getNonUnRated() && unRatedFlag) {
                unRatedmsg = "Bill Of Lading UnRated";
                unRatedCompanyName = companyName + " " + notification.getFileNumber() + " Bill Of Lading UnRated";
                unRatedOutputFileName = outputFileName + "/" + "Documents/LCL/LclBl/CodeJ_Job/" + DateUtils.formatDate(now, "yyyy/MM/dd") + "/"
                        + notification.getFileStatus() + "/BillofLading_UnRated_" + notification.getFileNumber() + appendDateTimeSec() + ".pdf";
                lclBLPdfCreator.createReportJob(contextPath, unRatedOutputFileName, "Bill Of Lading",
                        notification.getFileNumberId().toString(), false);
                unRatedFlag = false;
            }
            if (RegexUtil.isEmail(contacts.getToEmail())) {
                toEmailOrFax = contacts.getToEmail();
                emailOrFaxMode = CONTACT_MODE_EMAIL;
            } else if (RegexUtil.isFax(contacts.getToEmail())) {
                toEmailOrFax = contacts.getToEmail();
                emailOrFaxMode = CONTACT_MODE_FAX;
            }
            if (contacts.getNonRated() && !"".equalsIgnoreCase(toEmailOrFax)) {
                saveMailBlTransacations(notification.getFromAddress(), toEmailOrFax, ratedOutPutFileName, ratedmsg, emailOrFaxMode, ratedCompanyName,
                        notification.getFileNumber(), notification.getFileStatus(),
                        notification.getEmailBody(), notification.getId());
            }
            if (contacts.getNonUnRated() && !"".equalsIgnoreCase(toEmailOrFax)) {
                saveMailBlTransacations(notification.getFromAddress(), toEmailOrFax, unRatedOutputFileName, unRatedmsg, emailOrFaxMode,
                        unRatedCompanyName, notification.getFileNumber(), notification.getFileStatus(),
                        notification.getEmailBody(), notification.getId());
            }
            toEmailOrFax = "";
            sentEmailsAppend.append(contacts.getToName()).append("-->").append(contacts.getCompanyName()).append("-->").append(contacts.getToEmail()).append(",");
        }
        return sentEmailsAppend.toString();
    }

    public String appendDateTimeSec() {
        SimpleDateFormat dateformat = new SimpleDateFormat("_yyyyMMdd_HHmmss");
        return dateformat.format(new Date());
    }
}
