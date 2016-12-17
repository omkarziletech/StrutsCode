package com.logiware.common.job;

import com.gp.cong.hibernate.HibernateSessionFactory;
import com.logiware.common.dao.JobDAO;
import com.logiware.common.domain.Job;
import com.logiware.datamigration.LoadOpenArToLogiware;
import java.util.Date;
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
public class ArMigrationJob implements org.quartz.Job {

    private static final Logger log = Logger.getLogger(ArMigrationJob.class);

    public static void run() throws Exception {
	try {
	    new LoadOpenArToLogiware().reprocessAllErrors();
	} catch (Exception e) {
	    throw e;
	}
    }

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
	JobDAO dao = new JobDAO();
	Transaction transaction = null;
	try {
	    log.info("AR Migration Job started on " + new Date());
	    transaction = dao.getCurrentSession().getTransaction();
	    if (!transaction.isActive()) {
		transaction.begin();
	    }
	    Job job = dao.findByClassName(ArMigrationJob.class.getCanonicalName());
	    job.setStartTime(new Date());
	    run();
	    job.setEndTime(new Date());
	    transaction.commit();
	    log.info("AR Migration Job ended on " + new Date());
	} catch (Exception e) {
	    log.info("AR Migration Job failed on " + new Date(), e);
	    try {
		Thread.sleep(5000);
		log.info("AR Migration Job restarted on " + new Date());
		if (null == transaction || !transaction.isActive()) {
		    transaction = dao.getCurrentSession().getTransaction();
		    transaction.begin();
		} else {
		    transaction = dao.getCurrentSession().getTransaction();
		}
		Job job = dao.findByClassName(ArMigrationJob.class.getCanonicalName());
		job.setStartTime(new Date());
		run();
		job.setEndTime(new Date());
		transaction.commit();
		log.info("AR Migration Job ended on " + new Date());
	    } catch (Exception ex) {
		log.info("AR Migration Job failed again on " + new Date(), ex);
		if (null != transaction && transaction.isActive() && dao.getCurrentSession().isConnected() && dao.getCurrentSession().isOpen()) {
		    transaction.rollback();
		}
	    } finally {
                HibernateSessionFactory.closeSession();
            }
	}
    }
}
