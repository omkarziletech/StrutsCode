/*
 * Created on 14/09/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.gp.cong.test;
 
import java.util.Properties;
 
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
 
/**
 * @author gaucho
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class SendMailUsingAuthentication {
 
	private static final String SMTP_HOST_NAME = "gmail-smtp.l.google.com";
 
	private static final String SMTP_AUTH_USER = "from@gmail.com";
 
	private static final String SMTP_AUTH_PWD = "password";
 
	private static final String emailMsgTxt = "Please visit my project at ";
 
	private static final String emailSubjectTxt = "Order Confirmation Subject";
 
	private static final String emailFromAddress = "cejug.classifieds@gmail.com";
 
	// Add List of Email address to who email needs to be sent to
	private static final String[] emailList = { "to@gmail.com" };
 
	public static void main(String args[]) throws Exception {
		SendMailUsingAuthentication smtpMailSender = new SendMailUsingAuthentication();
		smtpMailSender.postMail(emailList, emailSubjectTxt, emailMsgTxt,
				emailFromAddress);
	}
 
	public void postMail(String recipients[], String subject, String message,
			String from) throws MessagingException {
		boolean debug = false;
		java.security.Security
				.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
 
		//Set the host smtp address
		Properties props = new Properties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.starttls.enable","true");
		props.put("mail.smtp.host", SMTP_HOST_NAME);
		props.put("mail.smtp.auth", "true");
 
		Authenticator auth = new SMTPAuthenticator();
		Session session = Session.getDefaultInstance(props, auth);
 
		session.setDebug(debug);
 
		// create a message
		Message msg = new MimeMessage(session);
 
		// set the from and to address
		InternetAddress addressFrom = new InternetAddress(from);
		msg.setFrom(addressFrom);
 
		InternetAddress[] addressTo = new InternetAddress[recipients.length];
		for (int i = 0; i < recipients.length; i++) {
			addressTo[i] = new InternetAddress(recipients[i]);
		}
		msg.setRecipients(Message.RecipientType.TO, addressTo);
		// Setting the Subject and Content Type
		msg.setSubject(subject);
		msg.setContent(message, "text/plain");
		URLName urln = new URLName("smtp", SMTP_HOST_NAME, 587, "", "", "");

		com.sun.mail.smtp.SMTPSSLTransport trans = new com.sun.mail.smtp.SMTPSSLTransport(session, urln);
		trans.sendMessage(msg,addressTo);
		trans.close();
		//Transport.send(msg);
	}
	private class SMTPAuthenticator extends javax.mail.Authenticator {
 
		public PasswordAuthentication getPasswordAuthentication() {
			String username = SMTP_AUTH_USER;
			String password = SMTP_AUTH_PWD;
			return new PasswordAuthentication(username, password);
		}
	}
}

