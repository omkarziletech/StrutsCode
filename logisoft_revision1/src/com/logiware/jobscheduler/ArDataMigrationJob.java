package com.logiware.jobscheduler;

import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.logiware.datamigration.LoadOpenArToLogiware;
import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author Lakshmi Narayanan
 */
public class ArDataMigrationJob extends BaseHibernateDAO implements Job {

    private static final Logger log = Logger.getLogger(ArDataMigrationJob.class);

    public void execute(JobExecutionContext jec) throws JobExecutionException {
	try {
	    log.info("Reprocessing AR Data Loading started on " + new Date());
	    new LoadOpenArToLogiware().reprocessAllErrors();
	    log.info("Reprocessing AR Data Loading completed on " + new Date());
	} catch (Exception e) {
	    log.info("Reprocessing AR Data Loading failed on " + new Date(),e);
	}
    }
}
