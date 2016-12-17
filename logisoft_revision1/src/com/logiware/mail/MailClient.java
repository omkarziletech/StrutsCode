/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.mail;

import com.gp.cong.logisoft.jobscheduler.MailPropertyReader;
import com.gp.cvst.logisoft.beans.MailMessageVO;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Ramasamy D
 */
public class MailClient {

    private static final String MAIL_SMTP_HOST = "mail.smtp.host";
    private static final String MAIL_SMTP_PORT = "mail.smtp.port";
    private static final String MAIL_AUTHENTICATION_ADDRESS = "mail.authentication.address";
    private static final String MAIL_AUTHENTICATION_PASSWORD = "mail.authentication.password";
    private static final String MAIL_QUOTE_FROM = "mail.quote.from";
    private static final String MAIL_SMTP_DSN_NOTIFY = "mail.smtp.dsn.notify";
    private MailPropertyReader mailPropertyReader = new MailPropertyReader();
    private Properties mailProperties = mailPropertyReader.getProperties();

    public void sendMail(String attachPath, MailMessageVO mailMessageVO, String reportName) throws Exception {
        mailProperties = mailPropertyReader.getProperties();
        if (mailProperties == null) {
            return;
        }
        boolean sendMail = false;
        Properties props = new Properties();
        props.put("mail.smtp.host", mailProperties.getProperty(MAIL_SMTP_HOST));
        props.put("mail.smtp.port", mailProperties.getProperty(MAIL_SMTP_PORT));
        props.put("mail.smtp.auth", "true");
        props.put(MAIL_SMTP_DSN_NOTIFY,"FAILURE ORCPT=rfc822;");
        props.put("mail.smtp.dsn.ret", "FULL");
        Authenticator auth = new SMTPAuthenticator();
        Session session = Session.getDefaultInstance(props, auth);
        Message message = new MimeMessage(session);

        //headers
        if (mailMessageVO.getToAddress() != null && !mailMessageVO.getToAddress().equals("")) {
            String[] toAddresses = StringUtils.split(mailMessageVO.getToAddress(), ",");
            if (toAddresses != null && toAddresses.length > 0) {
                for (int i = 0; i < toAddresses.length; i++) {
                    sendMail = true;
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddresses[i]));
                }
            }
        }
        if (mailMessageVO.getBccAddress() != null && !mailMessageVO.getBccAddress().equals("")) {
            String[] bccAddresses = StringUtils.split(mailMessageVO.getBccAddress(), ",");
            if (bccAddresses != null && bccAddresses.length > 0) {
                for (int i = 0; i < bccAddresses.length; i++) {
                    message.addRecipient(Message.RecipientType.BCC, new InternetAddress(bccAddresses[i]));
                }
            }
        }
        if (mailMessageVO.getCcAddress() != null && !mailMessageVO.getCcAddress().equals("")) {
            String[] ccAddresses = StringUtils.split(mailMessageVO.getCcAddress(), ",");
            if (ccAddresses != null && ccAddresses.length > 0) {
                for (int i = 0; i < ccAddresses.length; i++) {
                    message.addRecipient(Message.RecipientType.CC, new InternetAddress(ccAddresses[i]));
                }
            }
        }
        if (mailMessageVO.getFromAddress() != null && !mailMessageVO.getFromAddress().equals("")) {
            message.setFrom(new InternetAddress(mailMessageVO.getFromAddress()));
        } else {
            message.setFrom(new InternetAddress(mailProperties.getProperty(MAIL_QUOTE_FROM),
                    null != mailMessageVO.getFromName() && !mailMessageVO.getFromName().trim().equals("") ? mailMessageVO.getFromName() : ""));
        }

        if (mailMessageVO.getSubject() != null && !mailMessageVO.getSubject().equals("")) {
            message.setSubject(mailMessageVO.getSubject());

        }
        //add related mutip-part for root
        //Open Multipart Mixed
        MimeMultipart multipartMixed = new MimeMultipart("mixed");
        MimeMultipart multipartRoot = new MimeMultipart("related");
        MimeBodyPart contentRoot = new MimeBodyPart();
        MimeBodyPart contentMixed = new MimeBodyPart();
        MimeMultipart multipartAlt = new MimeMultipart("alternative");

        //alternative message
        BodyPart textMessage = new MimeBodyPart();
        if (null != mailMessageVO.getTextMessage() && !mailMessageVO.getTextMessage().equals("")) {
            textMessage.setContent(mailMessageVO.getTextMessage(), "text/plain");
        } else {
            textMessage.setContent("", "text/plain");
        }
        multipartAlt.addBodyPart(textMessage);
        BodyPart htmlMessage = new MimeBodyPart();
        if (mailMessageVO.getHtmlMessage() != null && !mailMessageVO.getHtmlMessage().equals("")) {
            htmlMessage.setContent(mailMessageVO.getHtmlMessage().replace("\n", "<br>"), "text/html");
        } else {
            htmlMessage.setContent("<br>", "text/html");
        }
        multipartAlt.addBodyPart(htmlMessage);
        //Hierarchy
        contentRoot.setContent(multipartAlt);
        multipartRoot.addBodyPart(contentRoot);
        contentMixed.setContent(multipartRoot);
        multipartMixed.addBodyPart(contentMixed);
        //attach a file
        if (attachPath != null && !attachPath.equals("")) {
            String[] multipleAttachmentPath = StringUtils.splitByWholeSeparator(attachPath, ";");
            for (String attachmentPath : multipleAttachmentPath) {
                DataSource fds = new FileDataSource(attachmentPath.trim());
                BodyPart attachment = new MimeBodyPart();
                attachment.setDataHandler(new DataHandler(fds));
                attachment.setFileName(fds.getName());
                attachment.setDisposition("attachment");
                multipartMixed.addBodyPart(attachment);
            }
        }

        //add multipart to the message
        message.setContent(multipartMixed);

        //send message
        
        if(sendMail){
            message.setSentDate(new Date());
            Transport.send(message);
        }

    }

    public static boolean isValidEmailAddress(String emailAddress) {
        String expression = "^[0-9a-z_\\.-]+@(([0-9]{1,3}\\.){3}[0-9]{1,3}|([0-9a-z][0-9a-z-]*[0-9a-z]\\.)+[a-z]{2,3})$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(emailAddress.trim());
        return matcher.matches();
    }

    private class SMTPAuthenticator extends javax.mail.Authenticator {

        public PasswordAuthentication getPasswordAuthentication() {
            String username = mailProperties.getProperty(MAIL_AUTHENTICATION_ADDRESS);
            String password = mailProperties.getProperty(MAIL_AUTHENTICATION_PASSWORD);
            return new PasswordAuthentication(username, password);
        }
    }
    
    public static void main(String[] args) throws Exception {
        MailMessageVO mailMessageVO = new MailMessageVO();
        mailMessageVO.setFromAddress("lakshh2512@gmail.com");
        mailMessageVO.setToAddress("lakshminarayanan.v@logiwareinc.com");
        mailMessageVO.setSubject("Test");
        mailMessageVO.setHtmlMessage("Test Message");
        new MailClient().sendMail(null, mailMessageVO, null);
    }
}
