package com.logiware.common.job;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.hibernate.HibernateSessionFactory;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cvst.logisoft.beans.MailMessageVO;
import com.logiware.common.dao.JobDAO;
import com.logiware.common.domain.Job;
import com.logiware.mail.MailClient;
import java.io.File;
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
public class EmailJob implements org.quartz.Job {

    private static final Logger log = Logger.getLogger(EmailJob.class);
    private static final JobDAO jobDAO = new JobDAO();
    private static final UserDAO userDAO = new UserDAO();
    private static final String newLine = System.getProperty("line.separator");//This will retrieve line separator dependent on OS.

    public void run() throws Exception {
        try {
            EmailschedulerDAO emailschedulerDAO = new EmailschedulerDAO();
            List<EmailSchedulerVO> pendingEmails = emailschedulerDAO.getPendingEmails();
            MailClient mailClient = new MailClient();
            for (EmailSchedulerVO email : pendingEmails) {
                MailMessageVO mailMessage = new MailMessageVO(email);
                try {
                    mailClient.sendMail(email.getFileLocation(), mailMessage, email.getName());
                    emailschedulerDAO.updateStatus(email, ConstantsInterface.EMAIL_STATUS_COMPLETED);
                    log.info("---------------------------Email Delivery Report----------------------------");
                    log.info("Email Sent Successfully !!!!!");
                    log.info("Email Sent By User : " + email.getUserName());
                    log.info("FromAddress : " + email.getFromAddress());
                    log.info("ToAddress : " + email.getToAddress());
                    if (CommonUtils.isNotEmpty(email.getCcAddress())) {
                        log.info("Cc Address : " + email.getCcAddress());
                    }
                    if (CommonUtils.isNotEmpty(email.getBccAddress())) {
                        log.info("Bcc Address : " + email.getBccAddress());
                    }
                    log.info("Email Subject : " + email.getSubject());
                    log.info("Email Sent Date : " + email.getEmailDate());
                    log.info("ModuleId : " + email.getModuleId());
                    log.info("Current Email Sent Successfully !!!");
                } catch (Exception e) {
                    log.info("---------------------------Email Failed Report----------------------------");
                    log.info("Email Sending Failed !!!!!");
                    log.info("Email Sent By User : " + email.getUserName());
                    log.info("From Address : " + email.getFromAddress());
                    log.info("To Address : " + email.getToAddress());
                    if (CommonUtils.isNotEmpty(email.getCcAddress())) {
                        log.info("Cc Address : " + email.getCcAddress());
                    }
                    if (CommonUtils.isNotEmpty(email.getBccAddress())) {
                        log.info("Bcc Address : " + email.getBccAddress());
                    }
                    log.info("Email Subject : " + email.getSubject());
                    log.info("Email Date : " + email.getEmailDate());
                    log.info("Module Name : " + email.getModuleName());
                    log.info("Current Email Status Sent Failed !!!");
                    log.info("Email Sending failed on" + new Date(), e);
                    emailschedulerDAO.updateStatus(email, ConstantsInterface.STATUS_FAILED);
                    try {
                        User user = userDAO.getUserInfo(email.getUserName());
                        if (null != user && CommonUtils.isNotEmpty(user.getEmail())) {
                            StringBuilder msg = new StringBuilder();
                            msg.append("---------------------------Email Failed Report----------------------------").append(newLine);
                            msg.append("Email Sent By User : ").append(email.getUserName()).append(newLine);
                            msg.append("From Address : ").append(email.getFromAddress()).append(newLine);
                            msg.append("To Address : ").append(email.getToAddress()).append(newLine);
                            if (CommonUtils.isNotEmpty(email.getCcAddress())) {
                                msg.append("Cc Address : ").append(email.getCcAddress()).append(newLine);
                            }
                            if (CommonUtils.isNotEmpty(email.getBccAddress())) {
                                msg.append("Bcc Address : ").append(email.getBccAddress()).append(newLine);
                            }
                            msg.append("Email Subject : ").append(email.getSubject()).append(newLine);
                            msg.append("Email Date : ").append(email.getEmailDate()).append(newLine);
                            msg.append("Module Name : ").append(email.getModuleName()).append(newLine);
                            StringBuilder attachments = new StringBuilder();
                            if (CommonUtils.isNotEmpty(email.getFileLocation())) {
                                String[] files = StringUtils.splitByWholeSeparator(email.getFileLocation(), ";");
                                for (String file : files) {
                                    if (new File(file).exists()) {
                                        attachments.append(file).append(";");
                                    } else {
                                        msg.append("File Not Found : ").append(file).append(newLine);
                                    }
                                }
                            }
                            msg.append("---------------------------Email Failed Report----------------------------").append(newLine);
                            if (CommonUtils.isNotEmpty(email.getTextMessage())) {
                                msg.append(email.getTextMessage());
                            }
                            MailMessageVO failedMailMessage = new MailMessageVO();
                            failedMailMessage.setToName(user.getFirstName());
                            failedMailMessage.setToAddress(user.getEmail());
                            failedMailMessage.setFromName(email.getFromName());
                            failedMailMessage.setFromAddress(email.getFromAddress());
                            failedMailMessage.setSubject("Failed: " + email.getFromAddress());
                            failedMailMessage.setTextMessage(msg.toString());
                            failedMailMessage.setHtmlMessage(msg.toString());
                            mailClient.sendMail(StringUtils.removeEnd(attachments.toString(), ";"), failedMailMessage, email.getName());
                        }
                    } catch (Exception ex) {
                        log.info("Email Sending Failed Notice failed on" + new Date(), ex);
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        Transaction transaction = null;
        try {
            log.info("Email Job started on " + new Date());
            transaction = new EmailschedulerDAO().getCurrentSession().getTransaction();
            if (!transaction.isActive()) {
                transaction.begin();
            }
            Job job = jobDAO.findByClassName(EmailJob.class.getCanonicalName());
            job.setStartTime(new Date());
            run();
            job.setEndTime(new Date());
            transaction.commit();
            log.info("Email Job ended on " + new Date());
        } catch (Exception e) {
            log.info("Email Job failed on " + new Date(), e);
            if (null != transaction && transaction.isActive() && jobDAO.getCurrentSession().isConnected() && jobDAO.getCurrentSession().isOpen()) {
                transaction.rollback();
            }
        } finally {
            HibernateSessionFactory.closeSession();
        }
    }
}
