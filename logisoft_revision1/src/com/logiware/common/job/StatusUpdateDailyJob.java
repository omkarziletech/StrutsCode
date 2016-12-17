package com.logiware.common.job;

import com.gp.cong.hibernate.HibernateSessionFactory;
import static com.gp.cong.lcl.common.constant.LclCommonConstant.EMAIL_TYPE_E1;
import static com.gp.cong.lcl.common.constant.LclCommonConstant.FAX_TYPE_F1;
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
public class StatusUpdateDailyJob implements org.quartz.Job {

    private static final Logger log = Logger.getLogger(StatusUpdateDailyJob.class);

    public void run() throws Exception {
        new NotificationUtil().sendPickupDateStatusUpdate("daily", EMAIL_TYPE_E1, FAX_TYPE_F1);
    }

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        JobDAO jobDAO = new JobDAO();
        Transaction transaction = null;
        try {
            log.info("Pickup Date Status Update Daily Job started on " + new Date());
            transaction = jobDAO.getCurrentSession().getTransaction();
            if (!transaction.isActive()) {
                transaction.begin();
            }
            Job job = jobDAO.findByClassName(StatusUpdateDailyJob.class.getCanonicalName());
            job.setStartTime(new Date());
            run();
            job.setEndTime(new Date());
            transaction.commit();
            log.info("Pickup Date Status Update Daily Job ended on " + new Date());
        } catch (Exception e) {
            log.info("Pickup Date Status Update Daily Job failed on " + new Date(), e);
            if (null != transaction && transaction.isActive() && jobDAO.getCurrentSession().isConnected() && jobDAO.getCurrentSession().isOpen()) {
                transaction.rollback();
            }
        } finally {
            HibernateSessionFactory.closeSession();
        }
    }
}
