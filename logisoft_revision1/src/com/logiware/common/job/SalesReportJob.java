package com.logiware.common.job;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.hibernate.HibernateSessionFactory;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.logiware.accounting.excel.ArAgingExcelCreator;
import com.logiware.accounting.form.ArReportsForm;
import com.logiware.accounting.model.SalesModel;
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
public class SalesReportJob implements org.quartz.Job, ConstantsInterface {

    private static final Logger log = Logger.getLogger(SalesReportJob.class);
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
            arReportsForm.setReportType(SUMMARY);
            arReportsForm.setCutOffDate(cutOffDate);
            arReportsForm.setAllPayments(true);
            List<SalesModel> salesManagers = new GenericCodeDAO().getSalesManagers();
            User rick = new UserDAO().getUserInfo("rickp");
            StringBuilder message = new StringBuilder();
            message.append("Attached you will find summary AR aging report showing open items at least 45 days old for all accounts assigned to you.\n");
            message.append("Please review and keep in mind that any past due account is subject to credit holds being placed on future shipments.\n");
            message.append("If corrections/adjustments are pending, please process them to avoid potential problems with customers.");
            for (SalesModel salesManager : salesManagers) {
                arReportsForm.setSalesManager(salesManager);
                String fileName = new ArAgingExcelCreator(arReportsForm, null).create();
                if (CommonUtils.isNotEmpty(fileName)) {
                    EmailSchedulerVO email = new EmailSchedulerVO();
                    email.setType(CONTACT_MODE_EMAIL);
                    email.setName("SALES_REPORT");
                    email.setFileLocation(fileName);
                    email.setStatus(EMAIL_STATUS_PENDING);
                    email.setNoOfTries(0);
                    email.setEmailDate(new Date());
                    email.setModuleName("SALES_REPORT");
                    email.setModuleId(salesManager.getManagerName());
                    email.setUserName(rick.getLoginName());
                    email.setFromName(rick.getFirstName());
                    email.setFromAddress(rick.getEmail());
                    email.setToName(salesManager.getManagerName());
                    email.setToAddress(salesManager.getManagerEmail());
                    email.setCcAddress(rick.getEmail() + (CommonUtils.isNotEmpty(salesManager.getRsmEmail()) ? ("," + salesManager.getRsmEmail()) : ""));
                    email.setSubject("Your Sales code weekly AR summary");
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
            log.info("Auto Sending Aging Report to Sales Person Job started on " + new Date());
            transaction = dao.getCurrentSession().getTransaction();
            if (!transaction.isActive()) {
                transaction.begin();
            }
            Job job = dao.findByClassName(SalesReportJob.class.getCanonicalName());
            job.setStartTime(new Date());
            run();
            job.setEndTime(new Date());
            dao.getCurrentSession().update(job);
            transaction.commit();
            log.info("Auto Sending Aging Report to Sales Person Job ended on " + new Date());
        } catch (Exception e) {
            log.info("Auto Sending Aging Report to Sales Person Job failed on " + new Date(), e);
            try {
                Thread.sleep(5000);
                log.info("Auto Sending Aging Report to Sales Person Job restarted on " + new Date());
                if (null == transaction || !transaction.isActive()) {
                    transaction = dao.getCurrentSession().getTransaction();
                    transaction.begin();
                } else {
                    transaction = dao.getCurrentSession().getTransaction();
                }
                Job job = dao.findByClassName(SalesReportJob.class.getCanonicalName());
                job.setStartTime(new Date());
                run();
                job.setEndTime(new Date());
                transaction.commit();
                log.info("Auto Sending Aging Report to Sales Person Job ended on " + new Date());
            } catch (Exception ex) {
                log.info("Auto Sending Aging Report to Sales Person Job failed again on " + new Date(), ex);
                if (null != transaction && transaction.isActive() && dao.getCurrentSession().isConnected() && dao.getCurrentSession().isOpen()) {
                    transaction.rollback();
                }
            } finally {
                HibernateSessionFactory.closeSession();
            }
        }
    }
}
