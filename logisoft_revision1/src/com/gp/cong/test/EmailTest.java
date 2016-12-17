package com.gp.cong.test;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailTest {
	public void postMail( String recipients[ ], String subject, String message , String from) throws MessagingException
	{
	    boolean debug = false;

	     //Set the host smtp address
	     Properties props = new Properties();
	     java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
	     props.put("mail.smtp.starttls.enable", "true");   
	     props.put("mail.smtp.host", "smtp.gmail.com");   
	     //props.put("mail.smtp.host", "64.246.24.108");   
	        props.put("mail.transport.protocol", "smtp");   
	        props.put("mail.smtp.auth", "true");   
	        // create some properties and get the default Session
	        Authenticator auth = new SMTPAuthenticator("pradeep.pamuru", "narayan");   
	        Session session = Session.getDefaultInstance(props, auth); 
	        session.setDebug(debug);

	    // create a message
	    Message msg = new MimeMessage(session);

	    // set the from and to address
	    InternetAddress addressFrom = new InternetAddress(from);
	    msg.setFrom(addressFrom);

	    InternetAddress[] addressTo = new InternetAddress[recipients.length]; 
	    for (int i = 0; i < recipients.length; i++)
	    {
	        addressTo[i] = new InternetAddress(recipients[i]);
	    }
	    msg.setRecipients(Message.RecipientType.TO, addressTo);
	   

	    // Optional : You can also set your custom headers in the Email if you Want
	    msg.addHeader("MyHeaderName", "myHeaderValue");

	    // Setting the Subject and Content Type
	    msg.setSubject(subject);
	    msg.setContent(message, "text/plain");
	    Transport.send(msg);
	}
	 private static class SMTPAuthenticator extends javax.mail.Authenticator {   
		    
	        private String username;   
	    
	        private String password;   
	    
	        public SMTPAuthenticator(String username, String password) {   
	            this.username = username;   
	            this.password = password;   
	        }   
	    
	        public PasswordAuthentication getPasswordAuthentication() {   
	            return new PasswordAuthentication(username, password);   
	        }   
	    }   

}
