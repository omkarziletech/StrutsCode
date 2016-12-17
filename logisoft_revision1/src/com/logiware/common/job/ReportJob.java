package com.logiware.common.job;

import com.gp.cong.hibernate.HibernateSessionFactory;
import com.logiware.common.dao.ReportDAO;
import com.logiware.common.domain.Report;
import org.apache.log4j.Logger;
import org.hibernate.Transaction;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

/**
 *
 * @author Lakshmi Narayanan
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ReportJob implements Job {

    private static final Logger log = Logger.getLogger(ReportJob.class);

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        ReportDAO reportDAO = new ReportDAO();
	Transaction transaction = null;
	try {
	    JobDataMap data = jec.getMergedJobDataMap();
	    Integer id = data.getInt("id");
	    transaction = reportDAO.getCurrentSession().getTransaction();
	    if (!transaction.isActive()) {
		transaction.begin();
	    }
	    Report report = reportDAO.findById(id);
	    reportDAO.send(report, JobScheduler.servletContext.getRealPath("/"));
	    transaction.commit();
	} catch (Exception e) {
	    log.info(jec.getJobDetail().getKey().getName() + " failed", e);
	    if (null != transaction && transaction.isActive() && reportDAO.getCurrentSession().isConnected() && reportDAO.getCurrentSession().isOpen()) {
		transaction.rollback();
	    }
	} finally {
            HibernateSessionFactory.closeSession();
        }
    }
}
