package com.logiware.jobscheduler;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.bc.accounting.CustomerStatementBC;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cong.logisoft.jobscheduler.JobScheduler;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.logiware.bean.CustomerBean;
import com.logiware.hibernate.dao.ArStatementDAO;
import com.logiware.hibernate.dao.InvalidMailTransactionsDAO;
import com.logiware.hibernate.domain.InvalidMailTransactions;
import com.logiware.mail.MailClient;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletContext;
import org.apache.commons.lang3.StringUtils;

import org.apache.log4j.Logger;
import org.hibernate.Transaction;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

/**
 *
 * @author Lakshmi Narayanan
 */
public class ArStatementJob extends BaseHibernateDAO implements Job, Serializable {

    private static final long serialVersionUID = -5188466178327978542L;
    private static final Logger log = Logger.getLogger(ArStatementJob.class);

    public void execute(JobExecutionContext jec) {
	Transaction hibernateTransaction = null;
	try {
	    hibernateTransaction = getCurrentSession().beginTransaction();
	    sendStatements(false);
	    hibernateTransaction.commit();
	} catch (Exception e) {
	    try {
		if (null == hibernateTransaction || !hibernateTransaction.isActive()) {
		    hibernateTransaction = getCurrentSession().beginTransaction();
		} else {
		    hibernateTransaction = getCurrentSession().getTransaction();
		}
		sendStatements(false);
		hibernateTransaction.commit();
	    } catch (Exception ex) {
		log.info(ex);
	    }
	} finally {
	}
    }

    private void sendStatements(boolean isManual) throws Exception {
	Calendar calendar = Calendar.getInstance();
	int day = calendar.get(Calendar.DAY_OF_MONTH);
	List<CustomerBean> accounts = null;
	if (day == 1) {
	    accounts = new ArStatementDAO().getAccountsForArStatement("ca.schedule_stmt='1st of month'");
	} else if (day == 16) {
	    accounts = new ArStatementDAO().getAccountsForArStatement("ca.schedule_stmt='16th of month'");
	} else if (isManual) {
	    accounts = new ArStatementDAO().getAccountsForArStatement("ca.schedule_stmt='1st of month' or ca.schedule_stmt='16th of month'");
	}
	if (CommonUtils.isNotEmpty(accounts)) {
	    ServletContext servletContext = JobScheduler.servletContext;
	    String companyName = new SystemRulesDAO().getSystemRules("CompanyName");
	    String contextPath = servletContext.getRealPath("/");
	    StringBuilder htmlMessageHeader = new StringBuilder("<html>");
	    htmlMessageHeader.append("<body>");
	    htmlMessageHeader.append("<div>");
	    htmlMessageHeader.append("Dear Customer,").append("<br>");
	    htmlMessageHeader.append("<div style='padding:10px 20px;'>Attached you will find our statement for your account. ");
	    htmlMessageHeader.append("Please review and process the due invoices for payment to ").append(companyName).append(".<br>");
	    htmlMessageHeader.append("If you have any questions, please contact:<br>");
	    StringBuilder htmlMessageFooter = new StringBuilder("</div></div>");
	    htmlMessageFooter.append("</body>");
	    htmlMessageFooter.append("</html>");
	    StringBuilder textMessageHeader = new StringBuilder("Dear Customer,\n");
	    textMessageHeader.append("\tAttached you will find our statement for your account. ");
	    textMessageHeader.append("\tPlease review and process due invoices for payment to ").append(companyName).append(" as usual.\n");
	    textMessageHeader.append("If you have any questions, please contact:\n");
	    EmailschedulerDAO emailschedulerDAO = new EmailschedulerDAO();
	    InvalidMailTransactionsDAO invalidMailTransactionsDAO = new InvalidMailTransactionsDAO();
	    for (CustomerBean account : accounts) {
		StringBuilder subject = new StringBuilder("Statement for ");
		subject.append("\"").append(account.getCustomerName()).append("\"");
		subject.append(" Account No.\"").append(account.getCustomerNumber()).append("\" ");
		subject.append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
		String fromName = account.getCollector();
		String fromAddress = account.getCollectorEmail();
		String fromFax = account.getCollectorFax();
		StringBuilder htmlMessageBody = new StringBuilder();
		htmlMessageBody.append("Collector: ").append(account.getCollector());
		htmlMessageBody.append(" Email: ").append(account.getCollectorEmail());
		StringBuilder textMessageBody = new StringBuilder();
		textMessageBody.append("Collector: ").append(account.getCollector());
		textMessageBody.append("\tEmail: ").append(account.getCollectorEmail());
		String htmlMessage = htmlMessageHeader.toString() + htmlMessageBody.toString() + htmlMessageFooter.toString();
		String textMessage = textMessageHeader.toString() + textMessageBody.toString();
		String fileLocation = new CustomerStatementBC().createReport(account, contextPath);
		if (null != fileLocation) {
		    if (CommonUtils.isEqualIgnoreCase(account.getStatementType(), CommonConstants.CONTACT_MODE_EMAIL)) {
			if (CommonUtils.isNotEmpty(account.getEmail())) {
			    String[] emailAddresses = StringUtils.split(account.getEmail().replace(';', ','), ",");
			    StringBuilder toEmailAddresses = new StringBuilder();
			    StringBuilder inValidEmailAddresses = new StringBuilder();
			    for (String emailAddress : emailAddresses) {
				if (MailClient.isValidEmailAddress(emailAddress)) {
				    toEmailAddresses.append(emailAddress).append(",");
				} else {
				    inValidEmailAddresses.append(emailAddress).append(",");
				}
			    }
			    String toEmailAddress = StringUtils.removeEnd(toEmailAddresses.toString(), ",");
			    String invalidEmailAddress = StringUtils.removeEnd(inValidEmailAddresses.toString(), ",");
			    if (CommonUtils.isNotEmpty(toEmailAddress)) {
				EmailSchedulerVO emailSchedulerVO = new EmailSchedulerVO();
				emailSchedulerVO.setEmailData(account.getCustomerNumber(), toEmailAddress, fromName, fromAddress, null, null, subject.toString(), htmlMessage);
				emailSchedulerVO.setEmailInfo("ARStatement", null, CommonConstants.CONTACT_MODE_EMAIL, 0, new Date(), "AR Statement", "AR Statement", "");
				emailSchedulerVO.setFileLocation(fileLocation);
				emailSchedulerVO.setTextMessage(textMessage);
				emailSchedulerVO.setModuleId(account.getCustomerNumber());
				emailschedulerDAO.save(emailSchedulerVO);
			    }
			    if (CommonUtils.isNotEmpty(invalidEmailAddress)) {
				InvalidMailTransactions invalidMailTransactions = new InvalidMailTransactions();
				invalidMailTransactions.setEmailData(account.getCustomerNumber(), invalidEmailAddress, fromName, fromAddress, null, null, subject.toString(), htmlMessage);
				invalidMailTransactions.setEmailInfo("ARStatement", null, CommonConstants.CONTACT_MODE_EMAIL, 0, new Date(), "AR Statement", "AR Statement", "");
				invalidMailTransactions.setFileLocation(fileLocation);
				invalidMailTransactions.setTextMessage(textMessage);
				invalidMailTransactions.setModuleId(account.getCustomerNumber());
				invalidMailTransactionsDAO.save(invalidMailTransactions);
			    }
			}
		    } else {
			String fax = account.getFax();
			if (CommonUtils.isNotEmpty(fax)) {
			    EmailSchedulerVO emailSchedulerVO = new EmailSchedulerVO();
			    emailSchedulerVO.setEmailInfo("ARStatement", null, CommonConstants.CONTACT_MODE_FAX, 0, new Date(), "AR Statement", "AR Statement", "");
			    emailSchedulerVO.setSubject(subject.toString());
			    emailSchedulerVO.setCoverLetter(textMessage);
			    emailSchedulerVO.setToAddress(fax);
			    emailSchedulerVO.setToName(account.getCustomerNumber());
			    emailSchedulerVO.setFromAddress(fromFax);
			    emailSchedulerVO.setFromName(fromName);
			    emailSchedulerVO.setFileLocation(fileLocation);
			    emailSchedulerVO.setModuleId(account.getCustomerNumber());
			    emailSchedulerVO.setTextMessage(textMessage);
			    emailSchedulerVO.setHtmlMessage("");
			    emailSchedulerVO.setUserName("");
			    emailschedulerDAO.save(emailSchedulerVO);
			}
		    }
		}
		htmlMessageBody = new StringBuilder();
		textMessageBody = new StringBuilder();
	    }
	}
    }

    public void startScheduler() throws Exception {
	sendStatements(true);
    }
}
