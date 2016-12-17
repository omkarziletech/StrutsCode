package com.logiware.common.job;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.hibernate.HibernateSessionFactory;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.logiware.accounting.excel.ArAgingExcelCreator;
import com.logiware.accounting.form.ArReportsForm;
import com.logiware.common.dao.JobDAO;
import com.logiware.common.domain.Job;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Transaction;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

/**
 *
 * @author NambuRajasekar
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution

public class AgingSummaryReportJob implements org.quartz.Job, ConstantsInterface {

    private static final Logger log = Logger.getLogger(AgingSummaryReportJob.class);

    public void run() throws Exception {
        ArReportsForm arReportsForm = new ArReportsForm();
        arReportsForm.setAllCustomers(true);
        arReportsForm.setReportType(SUMMARY);
        arReportsForm.setAgents(YES);
        arReportsForm.setNetsett(YES);
        arReportsForm.setCutOffDate(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
        arReportsForm.setAllPayments(true);
        List<User> userList = new UserDAO().getInternalCollections();
        for (User user : userList) {
            String fileName = "";
            if (CommonUtils.isNotEmpty(user.getEmail())) {
                arReportsForm.setInternationalCollector(user.getLoginName());
                arReportsForm.setCollector(String.valueOf(user.getUserId()));
                fileName = new ArAgingExcelCreator(arReportsForm, null).create();
            }
            if (CommonUtils.isNotEmpty(fileName)) {
                EmailSchedulerVO email = new EmailSchedulerVO();
                email.setType(CONTACT_MODE_EMAIL);
                email.setName("AgingSummary Report");
                email.setFileLocation(fileName);
                email.setStatus(EMAIL_STATUS_PENDING);
                email.setNoOfTries(0);
                email.setEmailDate(new Date());
                email.setModuleName("AgingSummary Report");
                email.setModuleId(user.getFirstName() + " " + user.getLastName());
                email.setToName(user.getFirstName() + " " + user.getLastName());
                email.setToAddress(user.getEmail());
                email.setUserName(user.getLoginName());
                email.setFromName(user.getFirstName());
                email.setFromAddress(user.getEmail());
                email.setSubject("AgingSummary DailyReport");
                email.setTextMessage("AgingSummary DailyReport");
                email.setHtmlMessage("Attached list shows the AgingSummary DailyReport");
                new EmailschedulerDAO().save(email);
            }
        }
    }

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        Transaction transaction = null;
        JobDAO jobDAO = new JobDAO();

        try {
            log.info("Aging SummaryReport Job started on " + new Date());
            transaction = jobDAO.getCurrentSession().getTransaction();
            if (!transaction.isActive()) {
                transaction.begin();
            }
            Job job = jobDAO.findByClassName(AgingSummaryReportJob.class.getCanonicalName());
            job.setStartTime(new Date());
            run();
            job.setEndTime(new Date());
            transaction.commit();
            log.info("Aging SummaryReport Job ended on " + new Date());
        } catch (Exception e) {
            log.info("Aging SummaryReport Job failed on " + new Date(), e);
            if (null != transaction && transaction.isActive() && jobDAO.getCurrentSession().isConnected() && jobDAO.getCurrentSession().isOpen()) {
                transaction.rollback();
            }
        } finally {
            HibernateSessionFactory.closeSession();
        }

    }
}
