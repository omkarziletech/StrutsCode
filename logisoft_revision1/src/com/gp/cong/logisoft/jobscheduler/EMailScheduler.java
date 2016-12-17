package com.gp.cong.logisoft.jobscheduler;

import java.util.Properties;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.HtmlEmail;


import com.gp.cong.logisoft.hibernate.dao.SchedulerDAO;
import com.gp.cvst.logisoft.beans.MailMessageVO;
import org.apache.commons.lang3.StringUtils;

public class EMailScheduler {

    private static final String MAIL_SMTP_HOST = "mail.smtp.host";
    private static final String MAIL_SMTP_PORT = "mail.smtp.port";
    private static final String MAIL_AUTHENTICATION_ADDRESS = "mail.authentication.address";
    private static final String MAIL_AUTHENTICATION_PASSWORD = "mail.authentication.password";
    private static final String MAIL_QUOTE_FROM = "mail.quote.from";
    private static final String MAIL_QUOTE_FROM_NAME = "mail.quote.from.name";
    private static final String MAIL_QUOTE_SUBJECT = "mail.quote.subject";
    private static final String MAIL_QUOTE_TEMPLATE = "mail.quote.template";
    SchedulerDAO schedulerDAO = new SchedulerDAO();

    public void createHtmlEmail(String path, String attachPath, MailMessageVO mailMessageVO, String reportName)throws Exception {

            MailPropertyReader mailPropertyReader = new MailPropertyReader();

            Properties mailProperties = mailPropertyReader.getProperties();

            if (mailProperties == null) {
                return;
            }

            //Create the email message

            HtmlEmail htmlEmail = new HtmlEmail();
            EmailAttachment attachment = new EmailAttachment();
            htmlEmail.setHostName(mailProperties.getProperty(MAIL_SMTP_HOST));

            htmlEmail.setSmtpPort(Integer.parseInt(mailProperties.getProperty(MAIL_SMTP_PORT)));
            htmlEmail.setAuthentication(mailProperties.getProperty(MAIL_AUTHENTICATION_ADDRESS), mailProperties.getProperty(MAIL_AUTHENTICATION_PASSWORD));

            if (mailMessageVO.getToAddress() != null && !mailMessageVO.getToAddress().equals("")) {
                String[] toAddresses = StringUtils.split(mailMessageVO.getToAddress(), ",");
                if (toAddresses != null && toAddresses.length > 0) {
                    for (int i = 0; i < toAddresses.length; i++) {
                        htmlEmail.addTo(toAddresses[i], "");
                    }
                } else {
                    htmlEmail.addTo(mailMessageVO.getToAddress(), "");
                }


            }
            if (mailMessageVO.getBccAddress() != null && !mailMessageVO.getBccAddress().equals("")) {
                String[] bccAddresses = StringUtils.split(mailMessageVO.getBccAddress(), ",");
                if (bccAddresses != null && bccAddresses.length > 0) {
                    for (int i = 0; i < bccAddresses.length; i++) {
                        htmlEmail.addBcc(bccAddresses[i], "");
                    }
                } else {
                    htmlEmail.addBcc(mailMessageVO.getBccAddress(), "");
                }
            }
            if (mailMessageVO.getCcAddress() != null && !mailMessageVO.getCcAddress().equals("")) {
                String[] ccAddresses = StringUtils.split(mailMessageVO.getCcAddress(), ",");
                if (ccAddresses != null && ccAddresses.length > 0) {
                    for (int i = 0; i < ccAddresses.length; i++) {
                        htmlEmail.addCc(ccAddresses[i], "");
                    }
                } else {
                    htmlEmail.addCc(mailMessageVO.getCcAddress(), "");
                }

            }
            String fromAddress = null;
            String fromName = null;
            if (mailMessageVO.getFromAddress() != null && !mailMessageVO.getFromAddress().equals("")) {
                fromAddress = mailMessageVO.getFromAddress();
            } else {
                fromAddress = mailProperties.getProperty(MAIL_QUOTE_FROM);
            }

            if (mailMessageVO.getFromName() != null && !mailMessageVO.getFromName().equals("")) {
                fromName = mailMessageVO.getFromName();
            } else {
                fromName = mailProperties.getProperty(MAIL_QUOTE_FROM_NAME);
            }
            htmlEmail.setFrom(fromAddress, fromName);
            if (mailMessageVO.getSubject() != null && !mailMessageVO.getSubject().equals("")) {
                htmlEmail.setSubject(mailMessageVO.getSubject());
            }




            if (attachPath != null && !attachPath.equals("")) {
                String[] multipleAttachmentPath = StringUtils.split(attachPath, ";");
                for (int i = 0; i < multipleAttachmentPath.length; i++) {
                    attachment.setPath(multipleAttachmentPath[i]);
                    String extension = multipleAttachmentPath[i].substring(multipleAttachmentPath[i].indexOf('.'), multipleAttachmentPath[i].length());
                    String name = multipleAttachmentPath[i].substring(multipleAttachmentPath[i].lastIndexOf('/') + 1, multipleAttachmentPath[i].indexOf('.'));
                    attachment.setName(name + extension);
                    htmlEmail.attach(attachment);
                }
            }
            /*  create a context and add data */
            String htmlMessage = null;
            if (mailMessageVO.getHtmlMessage() != null && !mailMessageVO.getHtmlMessage().equals("")) {
                htmlMessage = mailMessageVO.getHtmlMessage();
            } else {
                htmlMessage = "<br>";
            }

            htmlEmail.setHtmlMsg(htmlMessage);
            String textMessage = null;
            if (null != mailMessageVO.getTextMessage() && !mailMessageVO.getTextMessage().equals("")) {
                textMessage = mailMessageVO.getTextMessage();
            } else {
                textMessage = "";
            }
            if (null != textMessage && !textMessage.equalsIgnoreCase("")) {
                htmlEmail.setTextMsg(textMessage);
            }
            htmlEmail.setSubType("alternative");

            // send the email
            htmlEmail.send();
    }

    public void createHtmlEmail(String path, String attachPath, MailMessageVO mailMessageVO, String reportName, Integer id)throws Exception {
            MailPropertyReader mailPropertyReader = new MailPropertyReader();

            Properties mailProperties = mailPropertyReader.getProperties();

            if (mailProperties == null) {
                return;
            }

            //Create the email message

            HtmlEmail htmlEmail = new HtmlEmail();
            EmailAttachment attachment = new EmailAttachment();
            htmlEmail.setHostName(mailProperties.getProperty(MAIL_SMTP_HOST));
            htmlEmail.setTLS(true);

            htmlEmail.setSmtpPort(Integer.parseInt(mailProperties.getProperty(MAIL_SMTP_PORT)));
            htmlEmail.setAuthentication(mailProperties.getProperty(MAIL_AUTHENTICATION_ADDRESS), mailProperties.getProperty(MAIL_AUTHENTICATION_PASSWORD));

            if (mailMessageVO.getToAddress() != null && !mailMessageVO.getToAddress().equals("")) {
                String[] toAddresses = StringUtils.split(mailMessageVO.getToAddress(), ",");
                if (toAddresses != null && toAddresses.length > 0) {
                    for (int i = 0; i < toAddresses.length; i++) {
                        htmlEmail.addTo(toAddresses[i], "");
                    }
                } else {
                    htmlEmail.addTo(mailMessageVO.getToAddress(), "");
                }


            }
            if (mailMessageVO.getBccAddress() != null && !mailMessageVO.getBccAddress().equals("")) {
                String[] bccAddresses = StringUtils.split(mailMessageVO.getBccAddress(), ",");
                if (bccAddresses != null && bccAddresses.length > 0) {
                    for (int i = 0; i < bccAddresses.length; i++) {
                        htmlEmail.addBcc(bccAddresses[i], "");
                    }
                } else {
                    htmlEmail.addBcc(mailMessageVO.getBccAddress(), "");
                }
            }
            if (mailMessageVO.getCcAddress() != null && !mailMessageVO.getCcAddress().equals("")) {
                String[] ccAddresses = StringUtils.split(mailMessageVO.getCcAddress(), ",");
                if (ccAddresses != null && ccAddresses.length > 0) {
                    for (int i = 0; i < ccAddresses.length; i++) {
                        htmlEmail.addCc(ccAddresses[i], "");
                    }
                } else {
                    htmlEmail.addCc(mailMessageVO.getCcAddress(), "");
                }

            }
            String fromAddress = null;
            String fromName = null;
            if (mailMessageVO.getFromAddress() != null && !mailMessageVO.getFromAddress().equals("")) {
                fromAddress = mailMessageVO.getFromAddress();
            } else {
                fromAddress = mailProperties.getProperty(MAIL_QUOTE_FROM);
            }

            if (mailMessageVO.getFromName() != null && !mailMessageVO.getFromName().equals("")) {
                fromName = mailMessageVO.getFromName();
            } else {
                fromName = mailProperties.getProperty(MAIL_QUOTE_FROM_NAME);
            }
            htmlEmail.setFrom(fromAddress, fromName);
            if (mailMessageVO.getSubject() != null && !mailMessageVO.getSubject().equals("")) {
                htmlEmail.setSubject(mailMessageVO.getSubject());
            }




            if (attachPath != null && !attachPath.equals("")) {
                String[] multipleAttachmentPath = StringUtils.split(attachPath, ";");
                for (int i = 0; i < multipleAttachmentPath.length; i++) {
                    attachment.setPath(multipleAttachmentPath[i]);
                    attachment.setName(reportName + ".pdf");
                    htmlEmail.attach(attachment);
                }
            }
            /*  create a context and add data */
            String htmlMessage = null;
            if (mailMessageVO.getHtmlMessage() != null && !mailMessageVO.getHtmlMessage().equals("")) {
                htmlMessage = mailMessageVO.getHtmlMessage();
            } else {
                htmlMessage = "<br>";
            }

            htmlEmail.setHtmlMsg(htmlMessage);
            String textMessage = null;
            if (null != mailMessageVO.getTextMessage() && !mailMessageVO.getTextMessage().equals("")) {
                textMessage = mailMessageVO.getTextMessage();
            } else {
                textMessage = "";
            }
            htmlEmail.setTextMsg(textMessage);
            // send the email
            htmlEmail.send();

    }
}
