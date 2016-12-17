package com.gp.cong.logisoft.jobscheduler;

import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.accounting.job.EdiInvoiceJob;
import com.logiware.common.job.CtsEdiJob;
import com.logiware.jobscheduler.*;
import java.util.Date;
import javax.servlet.ServletContext;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import org.apache.log4j.Logger;

public class JobScheduler {

    private static final Logger log = Logger.getLogger(JobScheduler.class);
    public static ServletContext servletContext;

    public static void mailShedule() throws Exception {
//	SchedulerFactory factory = new StdSchedulerFactory();
//	Scheduler scheduler;
//	try {
//	    scheduler = factory.getScheduler();
//	    scheduler.start();
//	    JobDetail jobDetail = new JobDetail("MailJob", Scheduler.DEFAULT_GROUP, MailJob.class);
//	    SimpleTrigger simpleTrigger = new SimpleTrigger("MailTrigger", Scheduler.DEFAULT_GROUP, new Date(),
//		    null, SimpleTrigger.REPEAT_INDEFINITELY, 60L * 1000L);
//	    scheduler.scheduleJob(jobDetail, simpleTrigger);
//	} catch (SchedulerException e) {
//	    // TODO Auto-generated catch block
//	    log.info("mailShedule failed on " + new Date(),e);
//	}

    }

    public static void faxAndPrintShedule() throws Exception {
//	SchedulerFactory factory = new StdSchedulerFactory();
//	Scheduler scheduler;
//	try {
//	    scheduler = factory.getScheduler();
//	    scheduler.start();
//	    JobDetail jobDetail = new JobDetail("FaxAndPrintJob", Scheduler.DEFAULT_GROUP, FaxAndPrintJob.class);
//	    SimpleTrigger simpleTrigger = new SimpleTrigger("FaxAndPrintTrigger", Scheduler.DEFAULT_GROUP, new Date(),
//		    null, SimpleTrigger.REPEAT_INDEFINITELY, 60L * 1000L);
//	    scheduler.scheduleJob(jobDetail, simpleTrigger);
//	} catch (SchedulerException e) {
//	    // TODO Auto-generated catch block
//	    log.info("faxAndPrintShedule failed on " + new Date(),e);
//	}

    }

    public static void ediAckShedule() throws Exception {
//	SchedulerFactory factory = new StdSchedulerFactory();
//	Scheduler scheduler;
//	try {
//	    scheduler = factory.getScheduler();
//	    scheduler.start();
//	    JobDetail jobDetail = new JobDetail("EdiAckJob", Scheduler.DEFAULT_GROUP, EdiAckJob.class);
//	    SimpleTrigger simpleTrigger = new SimpleTrigger("EdiAckTrigger", Scheduler.DEFAULT_GROUP, new Date(),
//		    null, SimpleTrigger.REPEAT_INDEFINITELY, 1000L * 60L * 15L);
//	    scheduler.scheduleJob(jobDetail, simpleTrigger);
//	} catch (SchedulerException e) {
//	    log.info("ediAckShedule failed on " + new Date(),e);
//	}
    }

    public static void aesResponseShedule() throws Exception {
//	SchedulerFactory factory = new StdSchedulerFactory();
//	Scheduler scheduler;
//	try {
//	    scheduler = factory.getScheduler();
//	    scheduler.start();
//	    JobDetail jobDetail = new JobDetail("AesResponseJob", Scheduler.DEFAULT_GROUP, AesResponseJob.class);
//	    SimpleTrigger simpleTrigger = new SimpleTrigger("AesResponseTrigger", Scheduler.DEFAULT_GROUP, new Date(),
//		    null, SimpleTrigger.REPEAT_INDEFINITELY, 1000L * 60L * 15L);
//	    scheduler.scheduleJob(jobDetail, simpleTrigger);
//	} catch (SchedulerException e) {
//	    log.info("aesResponseShedule failed on " + new Date(),e);
//	}
    }

    public static void convertBookingToQuote() throws Exception {
//	SchedulerFactory factory = new StdSchedulerFactory();
//	Scheduler scheduler;
//	try {
//	    scheduler = factory.getScheduler();
//	    scheduler.start();
//	    JobDetail jobDetail = new JobDetail("BookingJob", Scheduler.DEFAULT_GROUP, BookingJob.class);
//	    SimpleTrigger simpleTrigger = new SimpleTrigger("BookingJobTrigger", Scheduler.DEFAULT_GROUP, new Date(),
//		    null, SimpleTrigger.REPEAT_INDEFINITELY, 10L * 50L);
//	    scheduler.scheduleJob(jobDetail, simpleTrigger);
//	} catch (SchedulerException e) {
//	    // TODO Auto-generated catch block
//	    log.info("convertBookingToQuote failed on " + new Date(),e);
//	}

    }

    public static void fclBlCorrectionManifestingJob() throws Exception {
//	SchedulerFactory factory = new StdSchedulerFactory();
//	Scheduler scheduler;
//	try {
//	    scheduler = factory.getScheduler();
//	    scheduler.start();
//	    JobDetail jobDetail = new JobDetail("ManifestingJob", Scheduler.DEFAULT_GROUP, FclBlCorrectionManifestingJob.class);
//	    SimpleTrigger simpleTrigger = new SimpleTrigger("ManifestingJobTrigger", Scheduler.DEFAULT_GROUP, new Date(),
//		    null, SimpleTrigger.REPEAT_INDEFINITELY, 10L * 5000L);
//	    scheduler.scheduleJob(jobDetail, simpleTrigger);
//	} catch (SchedulerException e) {
//	    // TODO Auto-generated catch block
//	    log.info("fclBlCorrectionManifestingJob failed on " + new Date(),e);
//	}

    }

    public static void openAllLocksJob() throws Exception {
//	SchedulerFactory factory = new StdSchedulerFactory();
//	Scheduler scheduler;
//	try {
//	    scheduler = factory.getScheduler();
//	    scheduler.start();
//	    JobDetail jobDetail = new JobDetail("OpenAllLocksJob", Scheduler.DEFAULT_GROUP, OpenAllLocksJob.class);
//	    CronTrigger cronTrigger = new CronTrigger("OpenAllLocksJobTrigger", Scheduler.DEFAULT_GROUP, "0 0 0 * * ?");
//	    scheduler.scheduleJob(jobDetail, cronTrigger);
//	} catch (Exception e) {
//	    // TODO Auto-generated catch block
//	    log.info("openAllLocksJob failed on " + new Date(),e);
//	}

    }

    public static void achPaymentJob() throws Exception {
//	SchedulerFactory factory = new StdSchedulerFactory();
//	Scheduler scheduler;
//	try {
//	    scheduler = factory.getScheduler();
//	    scheduler.start();
//	    JobDetail jobDetail = new JobDetail("AchSchedulerJob", Scheduler.DEFAULT_GROUP, AchScheduler.class);
//	    CronTrigger cronTrigger = new CronTrigger("AchSchedulerTrigger", Scheduler.DEFAULT_GROUP, "0 0 17 * * ?");
//	    scheduler.scheduleJob(jobDetail, cronTrigger);
//	} catch (Exception e) {
//	    // TODO Auto-generated catch block
//	    log.info("achPaymentJob failed on " + new Date(),e);
//	}

    }

    public static void arCreditHoldJob() throws Exception {
//	SchedulerFactory factory = new StdSchedulerFactory();
//	Scheduler scheduler;
//	try {
//	    scheduler = factory.getScheduler();
//	    scheduler.start();
//	    JobDetail jobDetail = new JobDetail("ARCreditHoldJob", Scheduler.DEFAULT_GROUP, ARCreditHoldJob.class);
//	    CronTrigger cronTrigger = new CronTrigger("ARCreditHoldTrigger", Scheduler.DEFAULT_GROUP, "0 0 1 * * ?");
//	    scheduler.scheduleJob(jobDetail, cronTrigger);
//	} catch (Exception e) {
//	    // TODO Auto-generated catch block
//	    log.info("arCreditHoldJob failed on " + new Date(),e);
//	}
    }

    public static void assignCollectorToTradingPartner() throws Exception {
//	SchedulerFactory factory = new StdSchedulerFactory();
//	Scheduler scheduler;
//	try {
//	    scheduler = factory.getScheduler();
//	    scheduler.start();
//	    JobDetail jobDetail = new JobDetail("CollectorAssignmentJob", Scheduler.DEFAULT_GROUP, CollectorAssignmentJob.class);
//	    CronTrigger cronTrigger = new CronTrigger("CollectorAssignmentTrigger", Scheduler.DEFAULT_GROUP, "0 30 23 * * ?");
//	    scheduler.scheduleJob(jobDetail, cronTrigger);
//	} catch (Exception e) {
//	    // TODO Auto-generated catch block
//	    log.info("assignCollectorToTradingPartner failed on " + new Date(),e);
//	}
    }

    public static void createscheduler() {
//	try {
//	    SchedulerFactory schedulerFactory = new StdSchedulerFactory();
//	    Scheduler scheduler = schedulerFactory.getScheduler();
//	    JobDetail jd = new JobDetail("job1", "group1", CtsEdiJob.class);
//	    CronTrigger ct = new CronTrigger("cronTrigger", "group2", "0 0/10 * * * ?");
//	    scheduler.scheduleJob(jd, ct);
//	    scheduler.start();
//	} catch (SchedulerException ex) {
//	    log.info("RoleDutyEdi failed on " + new Date(),ex);
//	} catch (Exception e) {
//	}
    }

    public static void sendFollowUpNotes() throws Exception {
//	SchedulerFactory factory = new StdSchedulerFactory();
//	Scheduler scheduler;
//	try {
//	    scheduler = factory.getScheduler();
//	    scheduler.start();
//	    JobDetail jobDetail = new JobDetail("SendFollowUpNotesJob", Scheduler.DEFAULT_GROUP, SendFollowUpNotesJob.class);
//	    CronTrigger cronTrigger = new CronTrigger("SendFollowUpNotesTrigger", Scheduler.DEFAULT_GROUP, "0 0 0 * * ?");
//	    scheduler.scheduleJob(jobDetail, cronTrigger);
//	} catch (Exception e) {
//	    // TODO Auto-generated catch block
//	    log.info("sendFollowUpNotes failed on " + new Date(),e);
//	}
    }

    public static void sendArStatements() throws Exception {
//	SchedulerFactory factory = new StdSchedulerFactory();
//	Scheduler scheduler;
//	try {
//	    scheduler = factory.getScheduler();
//	    scheduler.start();
//	    JobDetail jobDetail = new JobDetail("ArStatementJob", Scheduler.DEFAULT_GROUP, ArStatementJob.class);
//	    CronTrigger cronTrigger = new CronTrigger("ArStatementTrigger", Scheduler.DEFAULT_GROUP, "0 1 0 1,16 * ?");
//	    scheduler.scheduleJob(jobDetail, cronTrigger);
//	} catch (Exception e) {
//	    log.info("sendArStatements failed on " + new Date(),e);
//	}
    }

    public static void reprocessArDataMigration() throws Exception {
//	SchedulerFactory factory = new StdSchedulerFactory();
//	Scheduler scheduler;
//	try {
//	    scheduler = factory.getScheduler();
//	    scheduler.start();
//	    JobDetail jobDetail = new JobDetail("ArDataMigrationJob", Scheduler.DEFAULT_GROUP, ArDataMigrationJob.class);
//	    CronTrigger cronTrigger = new CronTrigger("ArDataMigrationTrigger", Scheduler.DEFAULT_GROUP, "0 0 1 * * ?");
//	    scheduler.scheduleJob(jobDetail, cronTrigger);
//	} catch (Exception e) {
//	    log.info("reprocessArDataMigration failed on " + new Date(),e);
//	}
    }

    public static void reprocessAccrualMigration() throws Exception {
//	SchedulerFactory factory = new StdSchedulerFactory();
//	Scheduler scheduler;
//	try {
//	    scheduler = factory.getScheduler();
//	    scheduler.start();
//	    JobDetail jobDetail = new JobDetail("AccrualMigrationJob", Scheduler.DEFAULT_GROUP, AccrualMigrationJob.class);
//	    CronTrigger cronTrigger = new CronTrigger("AccrualMigrationJobTrigger", Scheduler.DEFAULT_GROUP, "0 0 1 * * ?");
//	    scheduler.scheduleJob(jobDetail, cronTrigger);
//	} catch (Exception e) {
//	    log.info("reprocessAccrualMigration failed on " + new Date(),e);
//	}
    }

    public static void autoReverseToQuote() throws Exception {
//	SchedulerFactory factory = new StdSchedulerFactory();
//	Scheduler scheduler;
//	try {
//	    scheduler = factory.getScheduler();
//	    scheduler.start();
//	    JobDetail jobDetail = new JobDetail("ReverseToQuoteJob", Scheduler.DEFAULT_GROUP, AutoReverseToQuoteJob.class);
//	    CronTrigger cronTrigger = new CronTrigger("ReverseToQuoteTrigger", Scheduler.DEFAULT_GROUP, "0 0 0 * * ?");
//	    scheduler.scheduleJob(jobDetail, cronTrigger);
//	} catch (Exception e) {
//	    log.info("autoReverseToQuote failed on " + new Date(),e);
//	}
    }

    public static void callEdiInvoiceJob() throws Exception {
//	SchedulerFactory factory = new StdSchedulerFactory();
//	Scheduler scheduler;
//	try {
//	    scheduler = factory.getScheduler();
//	    scheduler.start();
//	    JobDetail jobDetail = new JobDetail("EdiInvoiceJob", Scheduler.DEFAULT_GROUP, EdiInvoiceJob.class);
//	    String cronExpression = LoadLogisoftProperties.getProperty("edi.invoice.cronExpression");
//	    CronTrigger cronTrigger = new CronTrigger("EdiInvoiceTrigger", Scheduler.DEFAULT_GROUP, cronExpression);
//	    scheduler.scheduleJob(jobDetail, cronTrigger);
//	} catch (Exception e) {
//	    // TODO Auto-generated catch block
//	    log.info("callEdiInvoiceJob failed on " + new Date(),e);
//	}

    }
}
//7L * 24L * 60L * 60L * 1000L will run in a day
/*
 * Trigger trigger = new SimpleTrigger("trigger1", "group1");
 * //	7(days per week) * 24(hours per day) * 60(minutes per hour) * 60(seconds per minute) * 1000(milliseconds per second)
 * trigger.setRepeatInterval(7L * 24L * 60L * 60L * 1000L);
 *
 * Example SimpleTrigger : Create a simple trigger which fires exactly once, 20 seconds from now:
 * long startTime = System.currentTimeMillis() + (20L*1000L);
 * SimpleTrigger strigger = new SimpleTrigger("mySimpleTrigger", sched.DEFAULT_GROUP, new Date(startTime), null, 0, 0L);
 *
 * Example SimpleTrigger: Create a Simple Trigger that fires quickly and repeats every 10 seconds until 50 seconds from now:
 * long endTime = System.currentTimeMillis() + (50L * 1000L);
 * SimpleTrigger strigger = new SimpleTrigger("mySimpleTrigger", sched.DEFAULT_GROUP, new Date(), new Date(endTime), SimpleTrigger.REPEAT_INDEFINITELY, 10L * 1000L);
 *
 * Example SimpleTrigger: Create a Simple Trigger that fires on February 19 of the year 2007 at accurately 9:15 am, and repeats 10 times with 20 seconds delay between each firing.
 * java.util.Calendar cal = new java.util.GregorianCalendar(2007,cal.FEB, 19);
 * cal.set(cal.HOUR, 9);
 * cal.set(cal.MINUTE, 15);
 * cal.set(cal.SECOND, 0);
 * cal.set(cal.MILLISECOND, 0);
 * Data startTime = cal.getTime();
 * SimpleTrigger trigger = new SimpleTrigger("mySimpleTrigger", sched.DEFAULT_GROUP, startTime, 10, 20L*1000L);
 */
