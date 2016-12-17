package com.logiware.common.job;

import com.gp.cong.hibernate.HibernateSessionFactory;
import com.logiware.common.dao.JobDAO;
import com.logiware.common.domain.Job;
import com.logiware.common.utils.NotificationUtil;
import java.util.Date;
import org.apache.log4j.Logger;
import org.hibernate.Transaction;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author venugopal.s
 */
public class StatusUpdateAnytimeJob implements org.quartz.Job {

    private static final Logger log = Logger.getLogger(StatusUpdateAnytimeJob.class);

    public void run() throws Exception {
        new NotificationUtil().sendDrStatusUpdate("ANY TIME");
    }

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        JobDAO jobDAO = new JobDAO();
        Transaction transaction = null;
        try {
            log.info("Status Update Anytime Job started on " + new Date());
            transaction = jobDAO.getCurrentSession().getTransaction();
            if (!transaction.isActive()) {
                transaction.begin();
            }
            Job job = jobDAO.findByClassName(StatusUpdateAnytimeJob.class.getCanonicalName());
            job.setStartTime(new Date());
            run();
            job.setEndTime(new Date());
            transaction.commit();
            log.info("Status Update Anytime Job ended on " + new Date());
        } catch (Exception e) {
            log.info("Status Update Anytime Job failed on " + new Date(), e);
            if (null != transaction && transaction.isActive() && jobDAO.getCurrentSession().isConnected() && jobDAO.getCurrentSession().isOpen()) {
                transaction.rollback();
            }
        } finally {
            HibernateSessionFactory.closeSession();
        }
    }
}
