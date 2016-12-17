package com.logiware.jobscheduler;

import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.logiware.datamigration.LoadAccrualsToLogiware;
import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author Lakshmi Narayanan
 */
public class AccrualMigrationJob extends BaseHibernateDAO implements Job {

    private static final Logger log = Logger.getLogger(AccrualMigrationJob.class);

    public void execute(JobExecutionContext jec) throws JobExecutionException {
	try {
	    log.info("Reprocessing Accrual Migration started on " + new Date());
	    new LoadAccrualsToLogiware().reprocessAllErrors();
	    log.info("Reprocessing Accrual Migration completed on " + new Date());
	} catch (Exception e) {
	    log.info("Reprocessing Accrual Migration failed on " + new Date(),e);
	}
	try {
	    log.info("Loading Missed Accruals started on " + new Date());
	    new LoadAccrualsToLogiware().loadMissingAccruals();
	    log.info("Loading Missed Accruals completed on " + new Date());
	} catch (Exception e) {
	    try {
		new LoadAccrualsToLogiware().loadMissingAccruals();
		log.info("Loading Missed Accruals completed on " + new Date());
	    } catch (Exception ex) {
		ex.printStackTrace();
		log.info("Loading Missed Accruals failed on " + new Date(),e);
	    }
	}
    }
}
