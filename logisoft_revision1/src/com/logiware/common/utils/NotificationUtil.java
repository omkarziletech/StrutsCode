package com.logiware.common.utils;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.lcl.dwr.LclPrintUtil;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitDAO;
import com.gp.cong.logisoft.lcl.report.LclReportConstants;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.common.job.JobScheduler;
import com.logiware.lcl.dao.LclNotificationDAO;
import com.logiware.lcl.model.NotificationModel;
import com.logiware.lcl.model.LclNotificationModel;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Transaction;

/**
 *
 * @author Lakshmi Narayanan
 */
public class NotificationUtil implements LclCommonConstant, ConstantsInterface {

    private static final Logger log = Logger.getLogger(NotificationUtil.class);

    public void sendDispositionStatusUpdate(String frequency, String emailCode, String faxCode) throws Exception {
        EmailschedulerDAO emailDAO = new EmailschedulerDAO();
        LclNotificationDAO notificationDAO = new LclNotificationDAO();
        Transaction transaction = notificationDAO.getCurrentSession().getTransaction();
        if (!transaction.isActive()) {
            transaction.begin();
        }
        List<LclNotificationModel> notifications = notificationDAO.getVoyageNotifications(frequency);
        List<String> emails = new ArrayList<String>();
        List<String> faxes = new ArrayList<String>();
        LclPrintUtil lclPrintUtil = new LclPrintUtil();
        String contextPath = JobScheduler.servletContext.getRealPath("/");
        String companyName = "";
        Date today = new Date();
        String reportLocation = LoadLogisoftProperties.getProperty("reportLocation");
        for (LclNotificationModel notification : notifications) {
            try {
                transaction = notificationDAO.getCurrentSession().getTransaction();
                if (!transaction.isActive()) {
                    transaction.begin();
                }
                List<String> fileNumbers = Arrays.asList(notification.getFileNumbers().split(","));
                List<String> fileNumberIds = Arrays.asList(notification.getFileNumberIds().split(","));
                int index = 0;
                for (String fileNumber : fileNumbers) {
                    Long fileNumberId = Long.parseLong(fileNumberIds.get(index));
                    index++;
                    String disposition = notificationDAO.getDisposition(fileNumberId);
                    LclFileNumber lclFileNumber = new LclFileNumberDAO().findById(fileNumberId);
                    if ("ECI".equalsIgnoreCase(lclFileNumber.getBusinessUnit())) {
                        companyName = LoadLogisoftProperties.getProperty("application.Econo.companyname");
                    } else if ("OTI".equalsIgnoreCase(lclFileNumber.getBusinessUnit())) {
                        companyName = LoadLogisoftProperties.getProperty("application.OTI.companyname");
                    } else {
                        companyName = LoadLogisoftProperties.getProperty("application.ECU.companyname");

                    }
                    if (CommonUtils.notIn(disposition, "DATA", "CLSD", "AUDT")) {
                        String contacts = notificationDAO.getContactEmailAndFax(fileNumberId, emailCode, faxCode);
                        if (CommonUtils.isNotEmpty(contacts)) {
                            emails.clear();
                            faxes.clear();
                            for (String contact : contacts.split(",")) {
                                if (RegexUtil.isEmail(contact)) {
                                    emails.add(contact);
                                } else if (RegexUtil.isFax(contact)) {
                                    faxes.add(contact);
                                }
                            }
                            if (CommonUtils.isNotEmpty(emails) || CommonUtils.isNotEmpty(faxes)) {
                                String to = StringUtils.join(ListUtils.union(emails, faxes), ", ");
                                String fileLocation = lclPrintUtil.createImportBkgReport(reportLocation, fileNumberId.toString(), fileNumber, LclReportConstants.DOCUMENT_LCL_ARRIVAL_ADVICE_STATUS, contextPath, to, null);
                                if (CommonUtils.isNotEmpty(fileLocation)) {
                                    if (fileLocation.contains(".pdf")) {
                                        File file = new File(fileLocation);
                                        fileLocation = fileLocation.replace(".pdf", "");
                                        fileLocation += DateUtils.formatDate(new Date(), "_yyyyMMdd_HHmmss") + ".pdf";
                                        file.renameTo(new File(fileLocation));
                                    }
                                    String documentName = notificationDAO.getDocumentName(notification.getSsDetailId(), notification.getUnitId());
                                    for (String toAddress : emails) {
                                        EmailSchedulerVO email = new EmailSchedulerVO();
                                        email.setFromName(notification.getFromName());
                                        email.setFromAddress(notification.getFromAddress());
                                        email.setToAddress(toAddress);
                                        email.setSubject(companyName + " " + documentName + " " + fileNumber + "(" + notification.getUnitNo() + ")");
                                        email.setFileLocation(fileLocation);
                                        email.setStatus(EMAIL_STATUS_PENDING);
                                        email.setTextMessage(NOTIFICATION_TEXT_MESSAGE + "\n" + "LCL " + documentName);
                                        email.setHtmlMessage(NOTIFICATION_TEXT_MESSAGE + "\n" + "LCL " + documentName);
                                        email.setName("Disposition Change");
                                        email.setType(CONTACT_MODE_EMAIL);
                                        email.setModuleName(LclReportConstants.SCREENNAMELCLIMPORTBOOKINGREPORT);
                                        email.setModuleId(fileNumber);
                                        email.setUserName(USER_SYSTEM);
                                        email.setEmailDate(today);
                                        emailDAO.save(email);
                                    }
                                    for (String toAddress : faxes) {
                                        EmailSchedulerVO fax = new EmailSchedulerVO();
                                        fax.setFromName(NOTIFICATION_FROM_NAME);
                                        fax.setToAddress(toAddress);
                                        fax.setSubject(companyName + " " + documentName + " " + fileNumber);
                                        fax.setFileLocation(fileLocation);
                                        fax.setStatus(EMAIL_STATUS_PENDING);
                                        fax.setTextMessage(NOTIFICATION_TEXT_MESSAGE + "\n" + "LCL " + documentName);
                                        fax.setHtmlMessage(NOTIFICATION_TEXT_MESSAGE + "\n" + "LCL " + documentName);
                                        fax.setName("Disposition Change");
                                        fax.setType(CONTACT_MODE_FAX);
                                        fax.setModuleName(LclReportConstants.SCREENNAMELCLIMPORTBOOKINGREPORT);
                                        fax.setModuleId(fileNumber);
                                        fax.setUserName(USER_SYSTEM);
                                        fax.setEmailDate(today);
                                        emailDAO.save(fax);
                                    }
                                }
                            }
                        }
                    }
                }
                transaction = notificationDAO.getCurrentSession().getTransaction();
                notificationDAO.updateVoyageNotification(notification.getId(), frequency);
                transaction.commit();
            } catch (Exception e) {
                log.info("Sending Disposition Status Update failed for DR(s) " + notification.getFileNumbers() + " on " + new Date(), e);
            } finally {
                if (transaction.isActive() && notificationDAO.getCurrentSession().isConnected() && notificationDAO.getCurrentSession().isOpen()) {
                    transaction.rollback();
                }
            }
        }
        transaction = notificationDAO.getCurrentSession().getTransaction();
        if (!transaction.isActive()) {
            transaction.begin();
        }
    }

    public void sendDrStatusUpdate(String frequency) throws Exception {
        String contacts = "", fileNumber = "", fileIdString = "";
        EmailschedulerDAO emailschedulerDAO = new EmailschedulerDAO();
        LclNotificationDAO notificationDAO = new LclNotificationDAO();
        Transaction transaction = notificationDAO.getCurrentSession().getTransaction();
        if (!transaction.isActive()) {
            transaction.begin();
        }
        List<NotificationModel> notifications = notificationDAO.getDrStatusUpdate(frequency);
        List<String> emails = new ArrayList<String>();
        List<String> faxes = new ArrayList<String>();
        LclPrintUtil lclPrintUtil = new LclPrintUtil();
        String contextPath = JobScheduler.servletContext.getRealPath("/");
        String reportLocation = LoadLogisoftProperties.getProperty("reportLocation");
        Date today = new Date();
        for (NotificationModel notification : notifications) {
            try {
                transaction = notificationDAO.getCurrentSession().getTransaction();
                if (!transaction.isActive()) {
                    transaction.begin();
                }
                if (CommonUtils.isNotEmpty(notification.getToAddress())) {
                    String disposition = notificationDAO.getDisposition(notification.getFileNumberId());
                    if (CommonUtils.notIn(disposition, "DATA", "AVAL", "CLSD", "AUDT")) {
                        contacts = notification.getToAddress();
                        emails.clear();
                        faxes.clear();
                        for (String contact : contacts.split(",")) {
                            if (RegexUtil.isEmail(contact)) {
                                emails.add(contact);
                            } else if (RegexUtil.isFax(contact)) {
                                faxes.add(contact);
                            }
                        }
                        if (CommonUtils.isNotEmpty(emails) || CommonUtils.isNotEmpty(faxes)) {
                            UserDAO userdao = new UserDAO();
                            User ownerUser = userdao.getUserInfo(notification.getFromUserId());
                            String ownerUserMail = ownerUser.getEmail() != null ? ownerUser.getEmail() : "";
                            LclFileNumberDAO lclFileNumberDAO = new LclFileNumberDAO();
                            fileIdString = notification.getFileNumberId().toString();
                            fileNumber = lclFileNumberDAO.getFileNumberByFileId(fileIdString);
                            String to = StringUtils.join(ListUtils.union(emails, faxes), ", ");
                            String unitNo = new LclUnitDAO().getUnitNoByFileId(notification.getFileNumberId());
                            String fileLocation = lclPrintUtil.createImportBkgReport(reportLocation, fileIdString, fileNumber,
                                    LclReportConstants.DOCUMENT_LCL_ARRIVAL_ADVICE_STATUS, contextPath, to, null);
                            if (CommonUtils.isNotEmpty(fileLocation)) {
                                if (fileLocation.contains(".pdf")) {
                                    File file = new File(fileLocation);
                                    fileLocation = fileLocation.replace(".pdf", "");
                                    fileLocation += DateUtils.formatDate(new Date(), "_yyyyMMdd_HHmmss") + ".pdf";
                                    file.renameTo(new File(fileLocation));
                                }
                                for (String toAddress : emails) {
                                    EmailSchedulerVO email = new EmailSchedulerVO();
                                    email.setName(LclReportConstants.SCREENNAMELCLIMPORTBOOKINGREPORT);
                                    email.setFileLocation(fileLocation);
                                    email.setType(CONTACT_MODE_EMAIL);
                                    email.setStatus(EMAIL_STATUS_PENDING);
                                    email.setNoOfTries(0);
                                    email.setEmailDate(today);
                                    email.setToAddress(toAddress);
                                    email.setSubject(CommonUtils.isNotEmpty(unitNo) ? notification.getSubject() + "(" + unitNo + ")" : notification.getSubject());
                                    email.setHtmlMessage("Please Find the attachment");
                                    email.setModuleName(LclReportConstants.SCREENNAMELCLIMPORTBOOKINGREPORT);
                                    email.setModuleId(fileNumber);
                                    email.setUserName(USER_SYSTEM);
                                    email.setFromAddress(ownerUserMail);
                                    email.setFromName(ownerUser.getLoginName());
                                    emailschedulerDAO.save(email);
                                }
                                for (String toAddress : faxes) {
                                    EmailSchedulerVO fax = new EmailSchedulerVO();
                                    fax.setName(LclReportConstants.SCREENNAMELCLIMPORTBOOKINGREPORT);
                                    fax.setFileLocation(fileLocation);
                                    fax.setType(CONTACT_MODE_FAX);
                                    fax.setStatus(EMAIL_STATUS_PENDING);
                                    fax.setNoOfTries(0);
                                    fax.setEmailDate(today);
                                    fax.setToAddress(toAddress);
                                    fax.setSubject(notification.getSubject());
                                    fax.setHtmlMessage("Please Find the attachment");
                                    fax.setModuleName(LclReportConstants.SCREENNAMELCLIMPORTBOOKINGREPORT);
                                    fax.setModuleId(fileNumber);
                                    fax.setUserName(USER_SYSTEM);
                                    fax.setFromAddress(ownerUserMail);
                                    fax.setFromName(ownerUser.getLoginName());
                                    emailschedulerDAO.save(fax);
                                }
                            }
                        }
                    }
                }
                transaction = notificationDAO.getCurrentSession().getTransaction();
                notificationDAO.updateDrNotification(notification.getId());
                transaction.commit();
            } catch (Exception e) {
                log.info("Sending Status Update failed for DR " + fileNumber + " " + new Date(), e);
            } finally {
                if (transaction.isActive() && notificationDAO.getCurrentSession().isConnected() && notificationDAO.getCurrentSession().isOpen()) {
                    transaction.rollback();
                }
            }
        }
        transaction = notificationDAO.getCurrentSession().getTransaction();
        if (!transaction.isActive()) {
            transaction.begin();
        }
    }

    public void sendPickupDateStatusUpdate(String frequency, String emailCode, String faxCode) throws Exception {
        EmailschedulerDAO emailschedulerDAO = new EmailschedulerDAO();
        LclNotificationDAO notificationDAO = new LclNotificationDAO();
        LclPrintUtil lclPrintUtil = new LclPrintUtil();
        List<String> emails = new ArrayList<String>();
        List<String> faxes = new ArrayList<String>();
        String contextPath = JobScheduler.servletContext.getRealPath("/");
        String companyName = LoadLogisoftProperties.getProperty("application.email.companyName").toUpperCase();
        String reportLocation = LoadLogisoftProperties.getProperty("reportLocation");
        Date today = new Date();
        Transaction transaction = notificationDAO.getCurrentSession().getTransaction();
        if (!transaction.isActive()) {
            transaction.begin();
        }
        List<NotificationModel> list = notificationDAO.getDrsWithNoPickupDate();
        for (NotificationModel notification : list) {
            try {
                transaction = notificationDAO.getCurrentSession().getTransaction();
                if (!transaction.isActive()) {
                    transaction.begin();
                }
                String contacts = notificationDAO.getContactEmailAndFaxForStatusUpdateDaily(notification.getId(), emailCode, faxCode);
                if (CommonUtils.isNotEmpty(contacts)) {
                    emails.clear();
                    faxes.clear();
                    for (String contact : contacts.split(",")) {
                        if (RegexUtil.isEmail(contact)) {
                            emails.add(contact);
                        } else if (RegexUtil.isFax(contact)) {
                            faxes.add(contact);
                        }
                    }
                    if (CommonUtils.isNotEmpty(emails) || CommonUtils.isNotEmpty(faxes)) {
                        transaction = notificationDAO.getCurrentSession().getTransaction();
                        if (!transaction.isActive()) {
                            transaction.begin();
                        }
                        UserDAO userdao = new UserDAO();
                        String userMail;
                        String fromName;
                        if (CommonUtils.isNotEmpty(notification.getFromUserId())) {
                            User ownerUser = userdao.getUserInfo(notification.getFromUserId());
                            userMail = ownerUser.getEmail() != null ? ownerUser.getEmail() : "";
                            fromName = ownerUser.getLoginName();
                        } else {
                            User fromUser = userdao.getUserInfo(notification.getEnterByUserId());
                            userMail = fromUser.getEmail() != null ? fromUser.getEmail() : "";
                            fromName = fromUser.getLoginName();
                        }
                        LclFileNumber lclFileNumber = new LclFileNumberDAO().findById(notification.getId());
                        if ("ECI".equalsIgnoreCase(lclFileNumber.getBusinessUnit())) {
                            companyName = LoadLogisoftProperties.getProperty("application.Econo.companyname");
                        } else if ("OTI".equalsIgnoreCase(lclFileNumber.getBusinessUnit())) {
                            companyName = LoadLogisoftProperties.getProperty("application.OTI.companyname");
                        } else {
                            companyName = LoadLogisoftProperties.getProperty("application.ECU.companyname");

                        }
                        String to = StringUtils.join(ListUtils.union(emails, faxes), ", ");
                        String fileLocation = lclPrintUtil.createImportBkgReport(reportLocation, notification.getId().toString(),
                                notification.getFileNumber(), LclReportConstants.DOCUMENT_LCL_ARRIVAL_ADVICE_STATUS, contextPath,
                                to, null);
                        if (CommonUtils.isNotEmpty(fileLocation)) {
                            if (fileLocation.contains(".pdf")) {
                                File file = new File(fileLocation);
                                fileLocation = fileLocation.replace(".pdf", "");
                                fileLocation += DateUtils.formatDate(new Date(), "_yyyyMMdd_HHmmss") + ".pdf";
                                file.renameTo(new File(fileLocation));
                            }
                            for (String toAddress : emails) {
                                EmailSchedulerVO email = new EmailSchedulerVO();
                                email.setName(LclReportConstants.SCREENNAMELCLIMPORTBOOKINGREPORT);
                                email.setFileLocation(fileLocation);
                                email.setType(CONTACT_MODE_EMAIL);
                                email.setStatus(EMAIL_STATUS_PENDING);
                                email.setEmailDate(today);
                                email.setToAddress(toAddress);
                                email.setSubject(CommonUtils.isNotEmpty(notification.getUnitNo())
                                        ? companyName + " " + notification.getDocName() + " " + notification.getFileNumber() + " (" + notification.getUnitNo() + ")"
                                        : companyName + " " + notification.getDocName() + " " + notification.getFileNumber());
                                email.setHtmlMessage("Please Find the attachment");
                                email.setModuleName(LclReportConstants.SCREENNAMELCLIMPORTBOOKINGREPORT);
                                email.setModuleId(notification.getFileNumber());
                                email.setUserName(USER_SYSTEM);
                                email.setFromAddress(userMail);
                                email.setFromName(fromName);
                                emailschedulerDAO.save(email);
                            }
                            for (String toAddress : faxes) {
                                EmailSchedulerVO fax = new EmailSchedulerVO();
                                fax.setName(LclReportConstants.SCREENNAMELCLIMPORTBOOKINGREPORT);
                                fax.setFileLocation(fileLocation);
                                fax.setType(CONTACT_MODE_FAX);
                                fax.setStatus(EMAIL_STATUS_PENDING);
                                fax.setEmailDate(today);
                                fax.setToAddress(toAddress);
                                fax.setSubject(companyName + " " + notification.getDocName() + " " + notification.getFileNumber());
                                fax.setHtmlMessage("Please Find the attachment");
                                fax.setModuleName(LclReportConstants.SCREENNAMELCLIMPORTBOOKINGREPORT);
                                fax.setModuleId(notification.getFileNumber());
                                fax.setUserName(USER_SYSTEM);
                                fax.setFromAddress(userMail);
                                fax.setFromName(fromName);
                                emailschedulerDAO.save(fax);
                            }
                        }
                    }
                    if (transaction.isActive() && notificationDAO.getCurrentSession().isConnected() && notificationDAO.getCurrentSession().isOpen()) {
                        transaction.commit();
                    }
                }
            } catch (Exception e) {
                log.info("Sending Pickup Date Status Update failed for DR " + notification.getFileNumber() + " " + new Date(), e);
            } finally {
                if (transaction.isActive() && notificationDAO.getCurrentSession().isConnected() && notificationDAO.getCurrentSession().isOpen()) {
                    transaction.commit();
                }
            }
        }
        transaction = notificationDAO.getCurrentSession().getTransaction();
        if (!transaction.isActive()) {
            transaction.begin();
        }
    }
}
