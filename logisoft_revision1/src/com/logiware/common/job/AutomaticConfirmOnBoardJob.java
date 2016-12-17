package com.logiware.common.job;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.ConstantsInterface;
import static com.gp.cong.common.ConstantsInterface.CONTACT_MODE_EMAIL;
import static com.gp.cong.common.ConstantsInterface.EMAIL_STATUS_PENDING;
import com.gp.cong.hibernate.HibernateSessionFactory;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlDAO;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.logiware.common.dao.JobDAO;
import com.logiware.common.domain.Job;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Transaction;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

/**
 *
 * @author Balaji.E
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class AutomaticConfirmOnBoardJob implements org.quartz.Job, ConstantsInterface {

    private static final Logger log = Logger.getLogger(AutomaticConfirmOnBoardJob.class);
    private static final String newLine = System.getProperty("line.separator");//This will retrieve line separator dependent on OS.
    private static final FclBlDAO FCL_BL_DAO = new FclBlDAO();

    public void run() throws Exception {
        try {
            List automaticConfirmOnBoardReminderList = FCL_BL_DAO.getAutomatedConfirmOnBoardReminderList();
            if (!automaticConfirmOnBoardReminderList.isEmpty()) {
                EmailschedulerDAO emailschedulerDAO = new EmailschedulerDAO();
                String fileNoPrefix = CommonConstants.loadMessageResources().getMessage("fileNumberPrefix");
                String msg = confirmOnBoardReminderMsg();
                String fileNoWithPrefix = "";
                String emailId = "";
                Iterator itr = automaticConfirmOnBoardReminderList.iterator();
                while (itr.hasNext()) {
                    EmailSchedulerVO mailTransaction = new EmailSchedulerVO();
                    Object[] row = (Object[]) itr.next();
                    fileNoWithPrefix = fileNoPrefix + (String) row[0];
                    emailId = (String) row[2];
                    mailTransaction.setFromAddress(emailId);
                    mailTransaction.setToAddress(emailId);
                    mailTransaction.setSubject("Auto Confirm on Board ERROR - "+(String)row[1]+".");
                    mailTransaction.setHtmlMessage(msg.replace("fileNo", fileNoWithPrefix));
                    mailTransaction.setTextMessage(msg.replace("fileNo", fileNoWithPrefix));
                    mailTransaction.setName("COB Reminder");
                    mailTransaction.setType(CONTACT_MODE_EMAIL);
                    mailTransaction.setStatus(EMAIL_STATUS_PENDING);
                    mailTransaction.setNoOfTries(0);
                    mailTransaction.setEmailDate(new Date());
                    mailTransaction.setModuleName("BL");
                    mailTransaction.setModuleId("ConfirmOnBoardReminder" + fileNoWithPrefix);
                    emailschedulerDAO.save(mailTransaction);
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public static String confirmOnBoardReminderMsg() throws Exception {
        StringBuilder msgBuilder = new StringBuilder();
        msgBuilder.append("Automatic COB for file number ").append("fileNo").append(" has not been received.");
        msgBuilder.append(newLine);
        msgBuilder.append(newLine);
        msgBuilder.append("Please contact the Steamship Line to inquire about this issue.");
        msgBuilder.append(newLine);
        msgBuilder.append(newLine);
        msgBuilder.append(newLine);
        msgBuilder.append("Regards");
        msgBuilder.append(newLine);
        String companyCode = new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
        if ("02".equals(companyCode)) {
            msgBuilder.append("OTI Cargo");
        } else {
            msgBuilder.append("Econocaribe");
        }
        return msgBuilder.toString();
    }

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        JobDAO dao = new JobDAO();
        Transaction transaction = null;
        try {
            log.info("Automatic Confirm on Board Job started on " + new Date());
            transaction = dao.getCurrentSession().getTransaction();
            if (!transaction.isActive()) {
                transaction.begin();
            }
            Job job = dao.findByClassName(AutomaticConfirmOnBoardJob.class.getCanonicalName());
            job.setStartTime(new Date());
            run();
            job.setEndTime(new Date());
            transaction.commit();
            log.info("Automatic Confirm on Board Job ended on " + new Date());
        } catch (Exception e) {
            log.info("Automatic Confirm on Board Job failed on " + new Date(), e);
            try {
                Thread.sleep(5000);
                log.info("Automatic Confirm on Board Job restarted on " + new Date());
                if (null == transaction || !transaction.isActive()) {
                    transaction = dao.getCurrentSession().getTransaction();
                    transaction.begin();
                } else {
                    transaction = dao.getCurrentSession().getTransaction();
                }
                Job job = dao.findByClassName(AutomaticConfirmOnBoardJob.class.getCanonicalName());
                job.setStartTime(new Date());
                run();
                job.setEndTime(new Date());
                transaction.commit();
                log.info("Automatic Confirm on Board Job ended on " + new Date());
            } catch (Exception ex) {
                log.info("Automatic Confirm on Board Job failed again on " + new Date(), ex);
                if (null != transaction && transaction.isActive() && dao.getCurrentSession().isConnected() && dao.getCurrentSession().isOpen()) {
                    transaction.rollback();
                }
            } finally {
                HibernateSessionFactory.closeSession();
            }
        }
    }
}
