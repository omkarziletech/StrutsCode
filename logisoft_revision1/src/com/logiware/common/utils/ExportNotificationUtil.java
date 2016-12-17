/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.common.utils;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclExportsVoyageNotificationDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsHeaderDAO;
import com.gp.cong.logisoft.jobscheduler.JobScheduler;
import com.gp.cong.logisoft.lcl.report.LclExportVoyageNotificationPdfCreator;
import com.gp.cong.logisoft.lcl.report.LclReportConstants;
import com.gp.cong.logisoft.reports.LclExportVoyageGeneralNotificationPdf;
import com.gp.cong.logisoft.reports.LclExportVoyageNotificationPdf;
import com.gp.cong.logisoft.web.HibernateSessionRequestFilter;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.logiware.common.dao.PropertyDAO;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;
import org.hibernate.Transaction;
import static org.semanticdesktop.aperture.vocabulary.NCAL.contact;

/**
 *
 * @author aravindhan.v
 */
public class ExportNotificationUtil implements LclCommonConstant, LclReportConstants {

    private static final Logger log = Logger.getLogger(NotificationUtil.class);
    private static String basePath = HibernateSessionRequestFilter.basePath;

    public void sendVoyageNotificationCodeI(Long headerId, Long UnitId, String emailCode,
            String faxCode, Long notificationId) throws Exception {
        LclExportsVoyageNotificationDAO lclNotificationDAO = new LclExportsVoyageNotificationDAO();
        Transaction transaction = lclNotificationDAO.getCurrentSession().getTransaction();
        boolean voyageNotificationFlag= Boolean.TRUE;
        if (!transaction.isActive()) {
            transaction.begin();
        }
        try {
            String fileNumbersIds = "";
            if (CommonUtils.isEmpty(UnitId)) {
                fileNumbersIds = lclNotificationDAO.getFileNumbersByHeaderId(headerId);
            } else {
                fileNumbersIds = lclNotificationDAO.getFileNumberByHeaderIdUnitId(headerId, UnitId);
            }
            String contacts = "", fileNumber = "";
            User user = new UserDAO().findById(lclNotificationDAO.getNotificationUser(notificationId));
            String companyCode = new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
            // String voyageRemarks = lclNotificationDAO.subjectforVoyageNotification(notificationId);
            String voyageNumber = new LclExportsVoyageNotificationDAO().getVoyageNumberByNotificationId(notificationId);
            String companyName = companyCode.equalsIgnoreCase("03") ? "ECU Worldwide" : "OTI";
            String subject = companyName + " Voyage Change Notification  (" + voyageNumber + ")";
            LclSsHeader header = new LclSsHeaderDAO().findById(headerId);
            String fromAddress[] = lclNotificationDAO.fromEmailAtRetadd(header.getOrigin().getUnLocationCode(),
                    header.getDestination().getUnLocationCode(), "L");
            String htmlMessage = getEmailContent(user.getFirstName(), user.getFax() != null ? user.getFax() : "");
            String attachFileName = "";
            if (CommonUtils.isNotEmpty(fileNumbersIds)) {
                for (String fileId : fileNumbersIds.split(",")) {
                    transaction = lclNotificationDAO.getCurrentSession().getTransaction();
                    if (!transaction.isActive()) {
                        transaction.begin();
                    }
                    contacts = lclNotificationDAO.getContactEmailAndFaxListByCodeI(Long.parseLong(fileId), emailCode, faxCode, notificationId);
                    LclFileNumber lclfileNumber = new LclFileNumberDAO().findById(Long.parseLong(fileId));
                    if (voyageNotificationFlag) {
                        if (lclNotificationDAO.getInternalEmpStatus(notificationId)) {
                            String toEmail = new PropertyDAO().getProperty(VOYAGENOTFICATIONINTERNALEMP);
                            String company =RegexUtil.isEmail(toEmail) ? toEmail.substring(toEmail.indexOf("@")).replace("@", "") : "";
                                attachFileName = sendVoyageNotificationReport(lclfileNumber, notificationId, company);
                            sendEmailFax(fromAddress[1], fromAddress[0], toEmail, subject, attachFileName, EMAIL_STATUS_PENDING, "",
                                    htmlMessage, "Voyage Notification", "Email", LclReportConstants.LCL_EXPORTUNITS,
                                    voyageNumber, "system", notificationId);
                            voyageNotificationFlag=Boolean.FALSE;
                        }
                    }
                    if (contacts != null) {
                        for (String contact : contacts.split(",#")) {
                            String content = contact.substring(contact.indexOf(":") + 1, contact.length());
                            content = CommonUtils.isNotEmpty(content) ? content.replaceAll(",", "") : content;

                            if (RegexUtil.isEmail(content)) {
                                attachFileName = sendVoyageGeneralNotificationReport(lclfileNumber, notificationId, companyName, contact);
                                sendEmailFax(fromAddress[1], fromAddress[0], content, subject, attachFileName, EMAIL_STATUS_PENDING, "",
                                        htmlMessage, "Voyage Notification", "Email", LclReportConstants.LCL_EXPORTUNITS,
                                        lclfileNumber.getFileNumber(), "system", notificationId);
                            } else if (RegexUtil.isFax(content)) {
                                attachFileName = sendVoyageGeneralNotificationReport(lclfileNumber, notificationId, companyName, contact);
                                sendEmailFax(fromAddress[1], fromAddress[0], content, subject, attachFileName, EMAIL_STATUS_PENDING, "",
                                        htmlMessage, "Voyage Notification", "Fax", LclReportConstants.LCL_EXPORTUNITS,
                                        lclfileNumber.getFileNumber(), "system", notificationId);
                            }
                        }
                    }
                }
            }
            transaction = lclNotificationDAO.getCurrentSession().getTransaction();
            lclNotificationDAO.insertVoyageRemarks(user, notificationId.toString());
            lclNotificationDAO.updateExportsVoyageNotification(notificationId, "complete");
            String attachDocument = voyageNotificationReport(fileNumbersIds, notificationId);
            sendEmailFax(fromAddress[1], fromAddress[0], user.getEmail(), subject, attachDocument, EMAIL_STATUS_PENDING, "",
                    "Voyage Notification", "Voyage Detail Change", "Email",
                    LclReportConstants.LCL_EXPORTUNITS, fileNumber, "system", notificationId);
            transaction.commit();
        } catch (Exception e) {
            log.info("Sending Voyage Notification" + " on " + new Date(), e);
        } finally {
            if (transaction.isActive() && lclNotificationDAO.getCurrentSession().isConnected()
                    && lclNotificationDAO.getCurrentSession().isOpen()) {
                transaction.rollback();
            }
        }
        transaction = lclNotificationDAO.getCurrentSession().getTransaction();
        if (!transaction.isActive()) {
            transaction.begin();
        }
    }

    public String voyageNotificationReport(String fileIds, Long notificationId) throws Exception {
        String contextPath = JobScheduler.servletContext.getRealPath("/");
        String voyageNumber = new LclExportsVoyageNotificationDAO().getVoyageNumberByNotificationId(notificationId);
        String outputFileName = LoadLogisoftProperties.getProperty("reportLocation")
                + "/" + FOLDER_NAME + "/" + MODULENAME + "/" + DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/";
        File file = new File(outputFileName);
        if (!file.exists()) {
            file.mkdirs();
        }
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddhhmmss");
        String date_to_string = dateformat.format(new Date());
        outputFileName = outputFileName + "VoyageNotification(" + voyageNumber + ")" + date_to_string + ".pdf";
        LclExportVoyageNotificationPdfCreator expNotificattionPDf = new LclExportVoyageNotificationPdfCreator();
        expNotificattionPDf.createPdf(contextPath, outputFileName, fileIds, notificationId);
        return outputFileName;
    }

    public String sendEmailFax(String fromName, String fromAddress, String toAddress, String subject, String fileLocation,
            String status, String textMsg, String htmlMsg, String name, String type,
            String moduleName, String moduleId, String user, Long notificationId) throws Exception {
        EmailSchedulerVO email = new EmailSchedulerVO();
        EmailschedulerDAO mailDAO = new EmailschedulerDAO();
        email.setFromName(fromName);
        email.setFromAddress(fromAddress);
        email.setToAddress(toAddress);
        email.setSubject(subject);
        email.setFileLocation(fileLocation);
        email.setStatus(status);
        email.setTextMessage(textMsg);
        email.setHtmlMessage(htmlMsg);
        email.setName(name);
        email.setType(type);
        email.setModuleName(moduleName);
        email.setModuleId(moduleId);
        email.setUserName(user);
        email.setEmailDate(new Date());
        email.setModuleReferenceId(notificationId);
        mailDAO.save(email);
        return "The Content Send Sucessfully";
    }

    public String sendVoyageGeneralNotificationReport(LclFileNumber fileNumber, Long notificationId,
            String companyName, String contact) throws Exception {
        String contextPath = JobScheduler.servletContext.getRealPath("/");
        String voyageNumber = new LclExportsVoyageNotificationDAO().getVoyageNumberByNotificationId(notificationId);
        String outputFileName = LoadLogisoftProperties.getProperty("reportLocation")
                + "/" + FOLDER_NAME + "/" + MODULENAME + "/" + DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/";
        File file = new File(outputFileName);
        if (!file.exists()) {
            file.mkdirs();
        }
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddhhmmss");
        String date_to_string = dateformat.format(new Date());
        contact = contact.replaceAll("[*,]", "");
        outputFileName = outputFileName + "Change VoyageNotification " + voyageNumber + "_" + fileNumber.getFileNumber() + "_"
                + contact.substring(contact.indexOf("$") + 1, contact.indexOf(":")) + "_" + date_to_string + ".pdf";
        LclExportVoyageGeneralNotificationPdf generalNotification = new LclExportVoyageGeneralNotificationPdf();
        generalNotification.createPdf(contextPath, outputFileName, notificationId, companyName, contact, fileNumber);
        return outputFileName;
    }
    public String sendVoyageNotificationReport(LclFileNumber fileNumber, Long notificationId,
            String companyName) throws Exception {
        companyName = CommonUtils.isNotEmpty(companyName) ? companyName.toUpperCase() : "ECUWORLDWIDE.US";
        String contextPath = JobScheduler.servletContext.getRealPath("/");
        String voyageNumber = new LclExportsVoyageNotificationDAO().getVoyageNumberByNotificationId(notificationId);
        String outputFileName = LoadLogisoftProperties.getProperty("reportLocation")
                + "/" + FOLDER_NAME + "/" + MODULENAME + "/" + DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/";
        File file = new File(outputFileName);
        if (!file.exists()) {
            file.mkdirs();
        }
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddhhmmss");
        String date_to_string = dateformat.format(new Date());
        outputFileName = outputFileName + "Voyage Notification " + voyageNumber + "_" + date_to_string + ".pdf";
        LclExportVoyageNotificationPdf voyageNotification = new LclExportVoyageNotificationPdf();
        voyageNotification.createPdf(contextPath, outputFileName, notificationId, companyName, fileNumber);
        return outputFileName;
    }

    public String getEmailContent(String fromName, String faxNumber) throws Exception {
        String code = new SystemRulesDAO().getSystemRules("CompanyCode");
        String companyURL = LoadLogisoftProperties
                .getProperty(code.equalsIgnoreCase("02") ? "application.OTI.website" : "application.ECU.website");
        String companyLogo = basePath + LoadLogisoftProperties
                .getProperty(code.equalsIgnoreCase("03") ? "application.image.logo" : "application.image.econo.logo");
        StringBuilder emailBody = new StringBuilder();
        emailBody.append("<HTML><BODY>");
        emailBody.append("<div style='font-family: sans-serif;'>");
        emailBody.append("<b>Please DO NOT reply to this message, see note 3 below.<b><br>");
        emailBody.append("<a href='http://").append(companyURL).append("' target='_blank'><img src='").append(companyLogo).append("'></a>");
        emailBody.append("<br>");
        emailBody.append("<br>");
        emailBody.append("<b>From Name:</b>").append(null != fromName ? fromName : "").append("<br>");
        emailBody.append("<b>From Fax #:</b>").append(null != faxNumber ? faxNumber : "").append("<br>");
        emailBody.append("<b>From Phone #:</b>").append("").append("<p></p>");
        emailBody.append("<pre>");
        emailBody.append("</pre><p></p>");
        emailBody.append("<br>");
        emailBody.append("<br>");
        emailBody.append("<br>");
        emailBody.append("<b>Did you know?</b><br>");
        emailBody.append("NEED LCL TRANS-ATLANTIC/PACIFIC SERVICES?  WE CAN ASSIST WITH YOUR IMPORT AND<br>");
        emailBody.append("EXPORT NEEDS TO AND FROM ASIA, EUROPE, THE MED, MIDDLE EAST AND AFRICA.<br>");
        emailBody.append("<br>");
        emailBody.append("CALL 1-866-326-6648  OR BOOK ON LINE AT <a href='http://'").append(companyURL).append("' target='_blank'>").append(companyURL).append("</a><br>");
        emailBody.append("<p></p>");
        emailBody.append("<a href='http://www.inttra.com/econocaribe/shipping-instructions?msc=ecbkem' target='_blank'><img src'http://www.econocaribe.com/media/mail/inttra_ad.jpg'></a><p></p>");
        emailBody.append("<b>Helpful Information:</b><br>");
        emailBody.append("1. Open the attached PDF image with Adobe Acrobat Reader. This software can<br>");
        emailBody.append("be downloaded for free, just visit <a href='http://'").append(companyURL).append("' target='_blank'>").append(companyURL).append("</a>.<br>");
        emailBody.append("2. The attached image may contain multiple pages.<br>");
        emailBody.append("3. Please do not reply to this email, it is sent from an automated<br>");
        emailBody.append("system, there will be no response from this address. For assistance contact<br>");
        emailBody.append("your sales representative or your local office at (866) 326-6648.<br>");
        emailBody.append("</b></b>");
        emailBody.append("</div>");
        emailBody.append("</BODY>");
        emailBody.append("</HTML>");
        return emailBody.toString();
    }
}
