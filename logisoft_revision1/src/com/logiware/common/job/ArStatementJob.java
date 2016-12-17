package com.logiware.common.job;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.hibernate.HibernateSessionFactory;
import com.gp.cong.logisoft.bc.accounting.CustomerStatementBC;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.logiware.bean.CustomerBean;
import com.logiware.common.dao.JobDAO;
import com.logiware.common.domain.Job;
import com.logiware.hibernate.dao.ArStatementDAO;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Transaction;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

/**
 *
 * @author Lakshmi Narayanan
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ArStatementJob implements org.quartz.Job, ConstantsInterface {

    private static final Logger log = Logger.getLogger(ArStatementJob.class);
    private static final String contextPath = JobScheduler.servletContext.getRealPath("/");
    private static String companyName = null;
    private static final JobDAO dao = new JobDAO();
    private Transaction transaction = null;
    private static final StringBuilder htmlMessageHeader = new StringBuilder();
    private static final StringBuilder htmlMessageFooter = new StringBuilder();
    private static final StringBuilder textMessageHeader = new StringBuilder();

    private static void send(CustomerBean account) throws Exception {
        CustomerStatementBC customerStatementBC = new CustomerStatementBC();
        EmailschedulerDAO emailschedulerDAO = new EmailschedulerDAO();
        String fileLocation = customerStatementBC.createReport(account, contextPath);
        if (null != fileLocation) {
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
            if (CommonUtils.isEqualIgnoreCase(account.getStatementType(), CONTACT_MODE_EMAIL)) {
                if (CommonUtils.isNotEmpty(account.getEmail())) {
                    String toEmailAddress = StringUtils.removeEnd(account.getEmail().replace(';', ','), ",");
                    if (CommonUtils.isNotEmpty(toEmailAddress)) {
                        EmailSchedulerVO email = new EmailSchedulerVO();
                        email.setToName(account.getCustomerNumber());
                        email.setToAddress(toEmailAddress);
                        email.setFromName(fromName);
                        email.setFromAddress(fromAddress);
                        email.setSubject(subject.toString());
                        email.setHtmlMessage(htmlMessage);
                        email.setTextMessage(textMessage);
                        email.setFileLocation(fileLocation);
                        email.setName("ARStatement");
                        email.setType(CONTACT_MODE_EMAIL);
                        email.setStatus(EMAIL_STATUS_PENDING);
                        email.setNoOfTries(0);
                        email.setEmailDate(new Date());
                        email.setModuleName("AR Statement");
                        email.setModuleId(account.getCustomerNumber());
                        email.setUserName("");
                        emailschedulerDAO.save(email);
                    }
                }
            } else {
                String faxAddress = account.getFax();
                if (CommonUtils.isNotEmpty(faxAddress)) {
                    EmailSchedulerVO fax = new EmailSchedulerVO();
                    fax.setToName(account.getCustomerNumber());
                    fax.setToAddress(faxAddress);
                    fax.setFromName(fromName);
                    fax.setFromAddress(fromFax);
                    fax.setSubject(subject.toString());
                    fax.setCoverLetter(textMessage);
                    fax.setHtmlMessage("");
                    fax.setTextMessage(textMessage);
                    fax.setFileLocation(fileLocation);
                    fax.setName("ARStatement");
                    fax.setType(CONTACT_MODE_FAX);
                    fax.setStatus(EMAIL_STATUS_PENDING);
                    fax.setNoOfTries(0);
                    fax.setEmailDate(new Date());
                    fax.setModuleName("AR Statement");
                    fax.setModuleId(account.getCustomerNumber());
                    fax.setUserName("");
                    emailschedulerDAO.save(fax);
                }
            }
            htmlMessageBody.delete(0, htmlMessageBody.length());
            textMessageBody.delete(0, textMessageBody.length());
        }
    }

    public void run(boolean isManual) throws Exception {
        try {
            transaction = dao.getCurrentSession().getTransaction();
            if (!transaction.isActive()) {
                transaction.begin();
            }
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
                companyName = new SystemRulesDAO().getSystemRules("CompanyName");
                htmlMessageHeader.delete(0, htmlMessageHeader.length());
                htmlMessageHeader.delete(0, htmlMessageHeader.length());
                htmlMessageHeader.append("<html>");
                htmlMessageHeader.append("<body>");
                htmlMessageHeader.append("<div>");
                htmlMessageHeader.append("Dear Customer,").append("<br>");
                htmlMessageHeader.append("<div style='padding:10px 20px;'>Attached you will find our statement for your account. ");
                htmlMessageHeader.append("Please review and process the due invoices for payment to ").append(companyName).append(".<br>");
                htmlMessageHeader.append("If you have any questions, please contact:<br>");
                htmlMessageFooter.delete(0, htmlMessageFooter.length());
                htmlMessageFooter.append("</div></div>");
                htmlMessageFooter.append("</body>");
                htmlMessageFooter.append("</html>");
                textMessageHeader.delete(0, textMessageHeader.length());
                textMessageHeader.append("Dear Customer,\n");
                textMessageHeader.append("\tAttached you will find our statement for your account. ");
                textMessageHeader.append("\tPlease review and process due invoices for payment to ").append(companyName).append(" as usual.\n");
                textMessageHeader.append("If you have any questions, please contact:\n");
                for (CustomerBean account : accounts) {
                    send(account);
                    transaction.commit();
                    transaction = dao.getCurrentSession().getTransaction();
                    transaction.begin();
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public void run() throws Exception {
        run(true);
    }

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        try {
            log.info("AR Statement Job started on " + new Date());
            transaction = dao.getCurrentSession().getTransaction();
            if (!transaction.isActive()) {
                transaction.begin();
            }
            Job job = dao.findByClassName(ArStatementJob.class.getCanonicalName());
            job.setStartTime(new Date());
            run(false);
            job.setEndTime(new Date());
            if (null == transaction || !transaction.isActive()) {
                transaction = dao.getCurrentSession().getTransaction();
                transaction.begin();
            } else {
                transaction = dao.getCurrentSession().getTransaction();
            }
            transaction.commit();
            log.info("AR Statement Job ended on " + new Date());
        } catch (Exception e) {
            log.info("AR Statement Job failed on " + new Date(), e);
            try {
                Thread.sleep(5000);
                log.info("AR Statement Job restarted on " + new Date());
                if (null == transaction || !transaction.isActive()) {
                    transaction = dao.getCurrentSession().getTransaction();
                    transaction.begin();
                } else {
                    transaction = dao.getCurrentSession().getTransaction();
                }
                Job job = dao.findByClassName(ArStatementJob.class.getCanonicalName());
                job.setStartTime(new Date());
                run(false);
                job.setEndTime(new Date());
                dao.getCurrentSession().update(job);
                transaction.commit();
                log.info("AR Statement Job ended on " + new Date());
            } catch (Exception ex) {
                log.info("AR Statement Job failed again on " + new Date(), ex);
                if (null != transaction && transaction.isActive() && dao.getCurrentSession().isConnected() && dao.getCurrentSession().isOpen()) {
                    transaction.rollback();
                }
            } finally {
                HibernateSessionFactory.closeSession();
            }
        }
    }
}
