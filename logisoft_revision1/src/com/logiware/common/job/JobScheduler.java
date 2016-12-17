package com.logiware.common.job;

import com.gp.cong.common.CommonUtils;
import com.logiware.common.constants.Frequency;
import com.logiware.common.constants.ScheduleFrequency;
import com.logiware.common.dao.JobDAO;
import com.logiware.common.dao.ReportDAO;
import com.logiware.common.domain.Job;
import com.logiware.common.domain.Report;
import java.util.List;
import javax.servlet.ServletContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Transaction;
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
public class JobScheduler {

    private static final Logger log = Logger.getLogger(JobScheduler.class);
    private static SchedulerFactory sf = null;
    public static ServletContext servletContext = null;

    private String createCronExpression(Job job) {
        StringBuilder cronExpression = new StringBuilder();
        cronExpression.append("0 ");  //Seconds
        if (job.getFrequency().equals(Frequency.MINUTELY)) {
            if (job.getMinute() == 1) {
                cronExpression.append("* "); //Minutes - for every minute
            } else {
                cronExpression.append("0/").append(job.getMinute()).append(" "); //Minutes
            }
            cronExpression.append("* ");  //Hours
            cronExpression.append("* ");	 //Day of Month
            cronExpression.append("* ");	 //Month
            cronExpression.append("?");	 //Day of Week
        } else if (job.getFrequency().equals(Frequency.HOURLY)) {
            cronExpression.append("0 "); //Minutes
            cronExpression.append("0/").append(job.getHour()).append(" "); //Hours
            cronExpression.append("* ");	 //Day of Month
            cronExpression.append("* ");	 //Month
            cronExpression.append("?");	 //Day of Week
        } else if (job.getFrequency().equals(Frequency.DAILY)) {
            cronExpression.append(job.getMinute()).append(" "); //Minutes
            cronExpression.append(job.getHour()).append(" "); //Hours
            cronExpression.append("* ");	 //Day of Month
            cronExpression.append("* ");	 //Month
            cronExpression.append("?");	 //Day of Week
        } else if (job.getFrequency().equals(Frequency.WEEKLY)) {
            cronExpression.append(job.getMinute()).append(" "); //Minutes
            cronExpression.append(job.getHour()).append(" "); //Hours
            cronExpression.append("? ");	 //Day of Month
            cronExpression.append("* ");	 //Month
            cronExpression.append(job.getDay1()); //Day of Week
        } else if (job.getFrequency().equals(Frequency.MONTHLY)) {
            cronExpression.append(job.getMinute()).append(" "); //Minutes
            cronExpression.append(job.getHour()).append(" "); //Hours
            cronExpression.append(job.getDay1()).append(" "); //Day of Month
            cronExpression.append("* ");	 //Month
            cronExpression.append("?");	 //Day of Week
        } else if (job.getFrequency().equals(Frequency.TWICE_A_MONTH)) {
            cronExpression.append(job.getMinute()).append(" "); //Minutes
            cronExpression.append(job.getHour()).append(" "); //Hours
            cronExpression.append(job.getDay1()).append(",").append(job.getDay2()).append(" ");	 //Day of Month
            cronExpression.append("* ");	 //Month
            cronExpression.append("?");	 //Day of Week
        }
        return cronExpression.toString();
    }

    public void scheduleJob(Job job) throws Exception {
        String className = job.getClassName();
        JobDetail jobDetail = JobBuilder.newJob((Class<org.quartz.Job>) Class.forName(className))
                .withIdentity(className, "JobGroup")
                .usingJobData("id", job.getId())
                .build();
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("JobTrigger" + job.getId(), "JobGroup")
                .withSchedule(CronScheduleBuilder.cronSchedule(createCronExpression(job)))
                .build();
        sf.getScheduler().scheduleJob(jobDetail, trigger);
    }

    public void deleteJob(Job job) throws Exception {
        JobKey jobKey = JobKey.jobKey(job.getClassName(), "JobGroup");
        if (sf.getScheduler().checkExists(jobKey)) {
            sf.getScheduler().deleteJob(jobKey);
        }
    }

    public void rescheduleJob(Job job) throws Exception {
        deleteJob(job);
        scheduleJob(job);
    }

    private String createCronExpression(Report report) {
        StringBuilder cronExpression = new StringBuilder();
        String[] time = StringUtils.splitByWholeSeparator(report.getScheduleTime(), ":");
        cronExpression.append(time[2]).append(" ");  //Seconds
        cronExpression.append(time[1]).append(" ");  //Minutes
        cronExpression.append(time[0]).append(" ");  //Hours
        if (report.getScheduleFrequency().equals(ScheduleFrequency.DAILY)) {
            cronExpression.append("? ");	 //Day of Month
            cronExpression.append("* ");	 //Month
            cronExpression.append("?");	 //Day of Week
        } else if (report.getScheduleFrequency().equals(ScheduleFrequency.WEEKLY)) {
            cronExpression.append("? ");	 //Day of Month
            cronExpression.append("* ");	 //Month
            cronExpression.append(report.getScheduleDay1());  //Day of Week
        } else if (report.getScheduleFrequency().equals(ScheduleFrequency.MONTHLY)) {
            cronExpression.append(report.getScheduleDay1()).append(" ");  //Day of Month
            cronExpression.append("* ");	 //Month
            cronExpression.append("?");	 //Day of Week
        } else if (report.getScheduleFrequency().equals(ScheduleFrequency.TWICE_A_MONTH)) {
            cronExpression.append(report.getScheduleDay1()).append(",").append(report.getScheduleDay2()).append(" ");  //Day of Month
            cronExpression.append("* ");	 //Month
            cronExpression.append("?");	 //Day of Week
        }
        return cronExpression.toString();
    }

    public void scheduleJob(Report report) throws Exception {
        JobDetail jobDetail = JobBuilder.newJob(ReportJob.class)
                .withIdentity("ReportJob" + report.getId(), "ReportJobGroup")
                .usingJobData("id", report.getId())
                .build();
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("ReportJobTrigger" + report.getId(), "ReportJobGroup")
                .withSchedule(CronScheduleBuilder.cronSchedule(createCronExpression(report)))
                .build();
        sf.getScheduler().scheduleJob(jobDetail, trigger);
    }

    public void deleteJob(Report report) throws Exception {
        JobKey jobKey = JobKey.jobKey("ReportJob" + report.getId(), "ReportJobGroup");
        if (sf.getScheduler().checkExists(jobKey)) {
            sf.getScheduler().deleteJob(jobKey);
        }
    }

    public void rescheduleJob(Report report) throws Exception {
        deleteJob(report);
        scheduleJob(report);
    }

    private void initJobs() throws Exception {
        JobDAO jobDAO = new JobDAO();
        Transaction transaction = jobDAO.getCurrentSession().getTransaction();
        transaction.begin();
        // To Schedule Common Jobs
        List<Job> jobs = jobDAO.getEnabledJobs();
        if (CommonUtils.isNotEmpty(jobs)) {
            for (Job job : jobs) {
                scheduleJob(job);
            }
        }
        // To Schedule Report Jobs
        List<Report> reports = new ReportDAO().getEnabledReports();
        if (CommonUtils.isNotEmpty(reports)) {
            for (Report report : reports) {
                scheduleJob(report);
            }
        }
        transaction.commit();
    }

    public void init() {
        try {
            log.info("Initializing Jobs started");
            sf = (StdSchedulerFactory) servletContext.getAttribute("JobSchedulerFactory");
            if (null == sf) {
                sf = new StdSchedulerFactory("quartz.properties");
            }
            sf.getScheduler().clear();
            if (!sf.getScheduler().isStarted()) {
                sf.getScheduler().start();
            }
            initJobs();
            log.info("Initializing Jobs completed");
        } catch (Exception e) {
            log.info("Initializing Jobs failed", e);
        }
    }

    public void destroy() {
        try {
            log.info("Shutting down Jobs started");
            sf = (StdSchedulerFactory) servletContext.getAttribute("JobSchedulerFactory");
            if (null == sf) {
                sf = new StdSchedulerFactory("quartz.properties");
            }
            if (!sf.getScheduler().isShutdown()) {
                sf.getScheduler().shutdown(true);
            }
            log.info("Shutting down Jobs completed");
        } catch (Exception e) {
            log.info("Shutting down Jobs failed", e);
        }
    }
}
