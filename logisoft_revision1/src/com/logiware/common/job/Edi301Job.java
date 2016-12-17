package com.logiware.common.job;

import com.logiware.common.dao.JobDAO;
import com.logiware.common.domain.Job;
import com.logiware.edi.xml.Inttra301Reader;
import java.util.Date;
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
public class Edi301Job implements org.quartz.Job {

    private static final Logger log = Logger.getLogger(Edi301Job.class);

    public static void run() throws Exception {
	try {
	    Inttra301Reader inttra301Reader = new Inttra301Reader();
	    inttra301Reader.Intra301Reader();
	} catch (Exception e) {
	    throw e;
	}
    }

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
	JobDAO dao = new JobDAO();
	Transaction transaction = null;
	try {
	    log.info("EDI 301 Job started on " + new Date());
	    transaction = dao.getCurrentSession().getTransaction();
	    if (!transaction.isActive()) {
		transaction.begin();
	    }
	    Job job = dao.findByClassName(Edi301Job.class.getCanonicalName());
	    job.setStartTime(new Date());
	    run();
	    job.setEndTime(new Date());
	    transaction.commit();
	    log.info("EDI 301 Job ended on " + new Date());
	} catch (Exception e) {
	    log.info("EDI 301 Job failed on " + new Date(), e);
	    try {
		Thread.sleep(5000);
		log.info("EDI 301 Job restarted on " + new Date());
		if (null == transaction || !transaction.isActive()) {
		    transaction = dao.getCurrentSession().getTransaction();
		    transaction.begin();
		} else {
		    transaction = dao.getCurrentSession().getTransaction();
		}
		Job job = dao.findByClassName(Edi301Job.class.getCanonicalName());
		job.setStartTime(new Date());
		run();
		job.setEndTime(new Date());
		transaction.commit();
		log.info("EDI 301 Job ended on " + new Date());
	    } catch (Exception ex) {
		log.info("EDI 301 Job failed again on " + new Date(), ex);
		if (null != transaction && transaction.isActive() && dao.getCurrentSession().isConnected() && dao.getCurrentSession().isOpen()) {
		    transaction.rollback();
		}
	    }
	}
    }
}
