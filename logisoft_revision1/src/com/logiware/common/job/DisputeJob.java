package com.logiware.common.job;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.hibernate.HibernateSessionFactory;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cong.logisoft.hibernate.dao.TerminalManagerDao;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cvst.logisoft.struts.form.ApReportsForm;
import com.logiware.accounting.model.TerminalModel;
import com.logiware.common.dao.JobDAO;
import com.logiware.common.domain.Job;
import com.logiware.excel.ApReportsExcelCreator;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

/**
 *
 * @author NambuRajasekar.M
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class DisputeJob implements org.quartz.Job, ConstantsInterface {

    private static final Logger log = Logger.getLogger(DisputeJob.class);
    private static final JobDAO dao = new JobDAO();
    private Transaction transaction = null;

    public void run() throws Exception {
        try {
            transaction = dao.getCurrentSession().getTransaction();
            if (!transaction.isActive()) {
                transaction.begin();
            }
            EmailschedulerDAO emailDAO = new EmailschedulerDAO();
            Calendar cal = Calendar.getInstance();
            String toDate = DateUtils.formatDate(cal.getTime(), "MM/dd/yyyy");
            cal.add(Calendar.DATE, -7);
            String fromDate = DateUtils.formatDate(cal.getTime(), "MM/dd/yyyy");
            StringBuilder message = new StringBuilder();
            message.append("Attached list shows the Disputed items Report");

            List<TerminalModel> terminalManagers = new TerminalManagerDao().getTrmManagersForDisputeJob();
            User kris = new UserDAO().getUserInfo("KLENGELER");
            for (TerminalModel terminalModel : terminalManagers) {
                ApReportsForm apReportsForm = new ApReportsForm();
                apReportsForm.setToDate(toDate);
                apReportsForm.setFromDate(fromDate);
                apReportsForm.setReportType(AP_DISPUTED_ITEMS_REPORT);
                apReportsForm.setTerminalNo(terminalModel.getTerminalNumber());
                apReportsForm.setManagerName(terminalModel.getManagerName());
                String fileName = new ApReportsExcelCreator(apReportsForm).createReportForManager();
                if (CommonUtils.isNotEmpty(fileName)) {
                    EmailSchedulerVO email = new EmailSchedulerVO();
                    email.setType(CONTACT_MODE_EMAIL);
                    email.setName("DISPUTE_REPORT");
                    email.setFileLocation(fileName);
                    email.setStatus(EMAIL_STATUS_PENDING);
                    email.setNoOfTries(0);
                    email.setEmailDate(new Date());
                    email.setModuleName("DISPUTE_REPORT");
                    email.setModuleId(terminalModel.getManagerName());
                    email.setToName(terminalModel.getManagerName());
                    email.setToAddress(terminalModel.getManagerEmail());
                    email.setUserName(kris.getLoginName());
                    email.setFromName(kris.getFirstName());
                    email.setFromAddress(kris.getEmail());
                    email.setCcAddress(kris.getEmail());
                    email.setSubject("Disputed items Report");
                    email.setTextMessage(message.toString());
                    email.setHtmlMessage(message.toString());
                    emailDAO.save(email);
                    transaction.commit();
                    transaction = dao.getCurrentSession().getTransaction();
                    transaction.begin();
                }
            }
        } catch (HibernateException e) {
            throw e;
        }
    }

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        try {
            log.info("Auto Sending Dispute Report to Terminal Managers Job started on " + new Date());
            transaction = dao.getCurrentSession().getTransaction();
            if (!transaction.isActive()) {
                transaction.begin();
            }
            Job job = dao.findByClassName(DisputeJob.class.getCanonicalName());
            job.setStartTime(new Date());
            run();
            job.setEndTime(new Date());
            dao.getCurrentSession().update(job);
            transaction.commit();
            log.info("Auto Sending Dispute Report to Terminal Managers Job ended on " + new Date());
        } catch (Exception e) {
            log.info("Auto Sending Dispute Report to Terminal Managers Job failed on " + new Date(), e);
            try {
                Thread.sleep(5000);
                log.info("Auto Sending Dispute Report to Terminal Managers Job restarted on " + new Date());
                if (null == transaction || !transaction.isActive()) {
                    transaction = dao.getCurrentSession().getTransaction();
                    transaction.begin();
                } else {
                    transaction = dao.getCurrentSession().getTransaction();
                }
                Job job = dao.findByClassName(DisputeJob.class.getCanonicalName());
                job.setStartTime(new Date());
                run();
                job.setEndTime(new Date());
                if (null == transaction || !transaction.isActive()) {
                    transaction = dao.getCurrentSession().getTransaction();
                    transaction.begin();
                }
                transaction.commit();
                log.info("Auto Sending Dispute Report to Terminal Managers Job ended on " + new Date());
            } catch (Exception ex) {
                log.info("Auto Sending Dispute Report to Terminal Managers Job failed again on " + new Date(), ex);
                if (null != transaction && transaction.isActive() && dao.getCurrentSession().isConnected() && dao.getCurrentSession().isOpen()) {
                    transaction.rollback();
                }
            } finally {
                HibernateSessionFactory.closeSession();
            }
        }
    }

}
