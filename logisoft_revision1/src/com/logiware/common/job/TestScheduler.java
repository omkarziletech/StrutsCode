package com.logiware.common.job;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/**
 *
 * @author Lakshmi Narayanan
 */
public class TestScheduler {

    private static final Logger log = Logger.getLogger(JobScheduler.class);
    private static SchedulerFactory sf = null;

    public void scheduleJob(String cronExpression) throws Exception {
	JobDetail jobDetail = JobBuilder.newJob(TestJob.class)
		.withIdentity("TestJob", "TestJobGroup")
		.build();
	CronTrigger trigger = TriggerBuilder.newTrigger()
		.withIdentity("TestJobTrigger", "TestJobGroup")
		.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
		.build();
	sf.getScheduler().scheduleJob(jobDetail, trigger);
    }

    public void rescheduleJob() throws Exception {
	if (null != sf) {
	    sf.getScheduler().deleteJob(JobKey.jobKey("TestJob", "JobGroup"));
	} else {
	    try {
		sf = new StdSchedulerFactory("quartz.properties");
		sf.getScheduler().start();
	    } catch (Exception e) {
		log.info(e);
	    }
	}
	scheduleJob("0 0/2 * * * ?");
    }

    public void initJobs() throws Exception {
	scheduleJob("0 * * * * ?");
    }

    public void init() {
	try {
	    sf = new StdSchedulerFactory("quartz.properties");
	    sf.getScheduler().start();
	    initJobs();
	} catch (Exception e) {
	    log.info(e);
	}
    }

    public static void main(String[] args) {
	TestScheduler testScheduler = new TestScheduler();
	testScheduler.init();
	try {
	    Thread.sleep(1000 * 60);
	    testScheduler.rescheduleJob();
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
    }
}
