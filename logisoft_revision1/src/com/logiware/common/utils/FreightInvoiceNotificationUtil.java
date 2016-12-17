/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.common.utils;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.lcl.dwr.LclPrintUtil;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.lcl.report.LclReportConstants;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.common.job.JobScheduler;
import com.logiware.lcl.dao.LclNotificationDAO;
import com.logiware.lcl.model.LclNotificationModel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author PALRAJ
 */
public class FreightInvoiceNotificationUtil implements LclReportConstants, ConstantsInterface {

    public void sendFreightInvoicePdf() throws Exception {
        LclNotificationDAO lclNotificationDAO = new LclNotificationDAO();
        String contextPath = JobScheduler.servletContext.getRealPath("/");
        String reportLocation = LoadLogisoftProperties.getProperty("reportLocation");
        Date today = new Date();
        List<String> emails = new ArrayList<String>();
        List<String> faxes = new ArrayList<String>();
        List<LclNotificationModel> freightInvoiceNotification = lclNotificationDAO.getCodekContactDetails();
        if (!freightInvoiceNotification.isEmpty()) {
            for (LclNotificationModel notificationModel : freightInvoiceNotification) {
                String subject = "";
                emails.clear();
                faxes.clear();
                EmailschedulerDAO emailschedulerDAO = new EmailschedulerDAO();
                User user = new UserDAO().getUserInfo(notificationModel.getUserId());
                String fromAddress = LoadLogisoftProperties.getProperty("ImportsCodeKFromAddress");
                subject = this.getSubject(notificationModel.getFileNumberIds()) + " " + FREIGHT_INVOICE + " " + notificationModel.getFileNumbers();
                String fileLocation = new LclPrintUtil().createImportBkgReport(reportLocation, notificationModel.getFileNumberIds(), notificationModel.getFileNumbers(),
                        FREIGHT_INVOICE, contextPath, null, null);
                for (String emailOrFax : notificationModel.getToEmailFax().split(",")) {
                    if (RegexUtil.isEmail(emailOrFax.trim())) {
                        emails.add(emailOrFax);
                    } else if (RegexUtil.isFax(emailOrFax.trim())) {
                        faxes.add(emailOrFax);
                    }
                }
                if (CommonUtils.isNotEmpty(emails) || CommonUtils.isNotEmpty(faxes)) {
                    for (String toEmail : emails) {
                        EmailSchedulerVO email = new EmailSchedulerVO();
                        email.setName(SCREENNAMELCLIMPORTBOOKINGREPORT);
                        email.setFileLocation(fileLocation);
                        email.setType(REPORT_TYPE_EMAIL);
                        email.setStatus(STATUS_PENDING);
                        email.setNoOfTries(0);
                        email.setEmailDate(today);
                        email.setToAddress(toEmail);
                        email.setSubject(subject);
                        email.setHtmlMessage("Attached you will find the invoice for this shipment. Please review it and have payment processed to us in accordance with the payment terms on the invoice");
                        email.setModuleName(SCREENNAMELCLIMPORTBOOKINGREPORT);
                        email.setModuleId(notificationModel.getFileNumbers());
                        email.setUserName(USER_SYSTEM);
                        email.setFromAddress(fromAddress);
                        email.setFromName(user.getLoginName());
                        emailschedulerDAO.save(email);
                    }
                    for (String toFax : faxes) {
                        EmailSchedulerVO fax = new EmailSchedulerVO();
                        fax.setName(SCREENNAMELCLIMPORTBOOKINGREPORT);
                        fax.setFileLocation(fileLocation);
                        fax.setType(CONTACT_MODE_FAX);
                        fax.setStatus(STATUS_PENDING);
                        fax.setNoOfTries(0);
                        fax.setEmailDate(today);
                        fax.setToAddress(toFax);
                        fax.setSubject(subject);
                        fax.setHtmlMessage("Attached you will find the invoice for this shipment. Please review it and have payment processed to us in accordance with the payment terms on the invoice");
                        fax.setModuleName(SCREENNAMELCLIMPORTBOOKINGREPORT);
                        fax.setModuleId(notificationModel.getFileNumbers());
                        fax.setUserName(USER_SYSTEM);
                        fax.setFromAddress(user.getFax());
                        fax.setFromName(user.getLoginName());
                        emailschedulerDAO.save(fax);
                    }
                }
            }
        }
    }
    private String getSubject(String fileId) throws Exception {
        String brand = new LclFileNumberDAO().getBusinessUnit(fileId);
        String property = brand.equalsIgnoreCase("ECI") ? "application.Econo.companyname"
                : brand.equalsIgnoreCase("ECU") ? "application.ECU.companyname" : "application.OTI.companyname";
        String subject = LoadLogisoftProperties.getProperty(property);
        return subject;
    }
}
