/**
 * 
 */
package com.gp.cvst.logisoft.beans;

import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import java.io.Serializable;

/**
 * @author Administrator
 *
 */
public class MailMessageVO implements Serializable {
	
	 private String toName;
	 private String toAddress;
	 private String fromName;
	 private String fromAddress;
	 private String ccAddress;
	 private String bccAddress;
	 private String subject;
	 private String htmlMessage;
         private String textMessage;
	 
	 public MailMessageVO(){}
	 
	public MailMessageVO(String toAddress, String ccAddress,
			String bccAddress, String subject, String htmlMessage) {
		this.toAddress = toAddress;
		this.ccAddress = ccAddress;
		this.bccAddress = bccAddress;
		this.subject = subject;
		this.htmlMessage = htmlMessage;
	}

	public MailMessageVO(String toName, String toAddress, String fromName,
			String fromAddress, String ccAddress, String bccAddress,
			String subject, String htmlMessage) {
		this.toName = toName;
		this.toAddress = toAddress;
		this.fromName = fromName;
		this.fromAddress = fromAddress;
		this.ccAddress = ccAddress;
		this.bccAddress = bccAddress;
		this.subject = subject;
		this.htmlMessage = htmlMessage;
	}
        public MailMessageVO(EmailSchedulerVO emailSchedulerVO) {
		this.toName = emailSchedulerVO.getToName();
		this.toAddress = emailSchedulerVO.getToAddress();
		this.fromName = emailSchedulerVO.getFromName();
		this.fromAddress = emailSchedulerVO.getFromAddress();
		this.ccAddress = emailSchedulerVO.getCcAddress();
		this.bccAddress = emailSchedulerVO.getBccAddress();
		this.subject = emailSchedulerVO.getSubject();
		this.htmlMessage = emailSchedulerVO.getHtmlMessage();
                this.textMessage = emailSchedulerVO.getTextMessage();
	}
	public String getToName() {
		return toName;
	}
	public void setToName(String toName) {
		this.toName = toName;
	}
	public String getToAddress() {
            return null!=toAddress?toAddress.replace(';', ','):toAddress;
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
		return null!=ccAddress?ccAddress.replace(';', ','):ccAddress;
	}
	public void setCcAddress(String ccAddress) {
		this.ccAddress = ccAddress;
	}
	public String getBccAddress() {
		return null!=bccAddress?bccAddress.replace(';', ','):bccAddress;
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
	public void setHtmlMessage(String message) {
		this.htmlMessage = message;
	}

        public String getTextMessage() {
                return textMessage;
        }
        public void setTextMessage(String textMessage) {
                this.textMessage = textMessage;
        }
}
