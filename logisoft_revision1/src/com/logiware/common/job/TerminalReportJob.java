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
import com.logiware.accounting.excel.ArAgingExcelCreator;
import com.logiware.accounting.form.ArReportsForm;
import com.logiware.accounting.model.TerminalModel;
import com.logiware.common.dao.JobDAO;
import com.logiware.common.domain.Job;
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
 * @author Lakshmi Narayanan
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class TerminalReportJob implements org.quartz.Job, ConstantsInterface {

    private static final Logger log = Logger.getLogger(TerminalReportJob.class);
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
            cal.add(Calendar.DATE, -1);
            int day = cal.get(Calendar.DAY_OF_WEEK);
            if (day == Calendar.SUNDAY) {
                cal.add(Calendar.DATE, -2);
            } else if (day == Calendar.SATURDAY) {
                cal.add(Calendar.DATE, -1);
            }
            String cutOffDate = DateUtils.formatDate(cal.getTime(), "MM/dd/yyyy");
            ArReportsForm arReportsForm = new ArReportsForm();
            arReportsForm.setReportType(DETAIL);
            arReportsForm.setCutOffDate(cutOffDate);
            arReportsForm.setAllPayments(true);
            StringBuilder message = new StringBuilder();
            message.append("Attached list shows no credit shipments that have not been paid yet, as billed by your office.\n");
            message.append("Please review and follow up with your staff to make sure that funds are collected.");
            List<TerminalModel> terminalManagers = new TerminalManagerDao().getTerminalManagers();
            User rick = new UserDAO().getUserInfo("rickp");

            for (TerminalModel terminalModel : terminalManagers) {
                arReportsForm.getTerminalManager().setManagerEmail(terminalModel.getManagerEmail());
                arReportsForm.getTerminalManager().setManagerName(terminalModel.getManagerName());
                arReportsForm.getTerminalManager().setTerminalNumber(terminalModel.getTerminalNumber());
                String fileName = new ArAgingExcelCreator(arReportsForm, null).create();
                if (CommonUtils.isNotEmpty(fileName)) {
                    EmailSchedulerVO email = new EmailSchedulerVO();
                    email.setType(CONTACT_MODE_EMAIL);
                    email.setName("TERMINAL_REPORT");
                    email.setFileLocation(fileName);
                    email.setStatus(EMAIL_STATUS_PENDING);
                    email.setNoOfTries(0);
                    email.setEmailDate(new Date());
                    email.setModuleName("TERMINAL_REPORT");
                    email.setModuleId(terminalModel.getManagerName());
                    email.setUserName(rick.getLoginName());
                    email.setFromName(rick.getFirstName());
                    email.setFromAddress(rick.getEmail());
                    email.setToName(terminalModel.getManagerName());
                    email.setToAddress(terminalModel.getManagerEmail());
                    email.setCcAddress(rick.getEmail());
                    email.setSubject("Outstanding no credit shipments for your terminal");
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
            log.info("Auto Sending Aging Report to Terminal Managers Job started on " + new Date());
            transaction = dao.getCurrentSession().getTransaction();
            if (!transaction.isActive()) {
                transaction.begin();
            }
            Job job = dao.findByClassName(TerminalReportJob.class.getCanonicalName());
            job.setStartTime(new Date());
            run();
            job.setEndTime(new Date());
            dao.getCurrentSession().update(job);
            transaction.commit();
            log.info("Auto Sending Aging Report to Terminal Managers Job ended on " + new Date());
        } catch (Exception e) {
            log.info("Auto Sending Aging Report to Terminal Managers Job failed on " + new Date(), e);
            try {
                Thread.sleep(5000);
                log.info("Auto Sending Aging Report to Terminal Managers Job restarted on " + new Date());
                if (null == transaction || !transaction.isActive()) {
                    transaction = dao.getCurrentSession().getTransaction();
                    transaction.begin();
                } else {
                    transaction = dao.getCurrentSession().getTransaction();
                }
                Job job = dao.findByClassName(TerminalReportJob.class.getCanonicalName());
                job.setStartTime(new Date());
                run();
                job.setEndTime(new Date());
                if (null == transaction || !transaction.isActive()) {
                    transaction = dao.getCurrentSession().getTransaction();
                    transaction.begin();
                }
                transaction.commit();
                log.info("Auto Sending Aging Report to Terminal Managers Job ended on " + new Date());
            } catch (Exception ex) {
                log.info("Auto Sending Aging Report to Terminal Managers Job failed again on " + new Date(), ex);
                if (null != transaction && transaction.isActive() && dao.getCurrentSession().isConnected() && dao.getCurrentSession().isOpen()) {
                    transaction.rollback();
                }
            } finally {
                HibernateSessionFactory.closeSession();
            }
        }
    }
}
