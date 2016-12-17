package com.logiware.common.job;

import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.hibernate.HibernateSessionFactory;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.logiware.common.dao.JobDAO;
import com.logiware.common.domain.Job;
import com.logiware.common.utils.NotificationUtil;
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
public class DispositionChangeDailyJob implements org.quartz.Job, LclCommonConstant, ConstantsInterface {

    private static final Logger log = Logger.getLogger(DispositionChangeDailyJob.class);

    public void run() throws Exception {
        new NotificationUtil().sendDispositionStatusUpdate("daily", EMAIL_TYPE_E3, FAX_TYPE_F3);
    }

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        JobDAO jobDAO = new JobDAO();
        Transaction transaction = null;
        try {
            log.info("Disposition Change Daily Job started on " + new Date());
            transaction = jobDAO.getCurrentSession().getTransaction();
            if (!transaction.isActive()) {
                transaction.begin();
            }
            Job job = jobDAO.findByClassName(DispositionChangeDailyJob.class.getCanonicalName());
            job.setStartTime(new Date());
            run();
            job.setEndTime(new Date());
            transaction.commit();
            log.info("Disposition Change Daily Job ended on " + new Date());
        } catch (Exception e) {
            log.info("Disposition Change Daily Job failed on " + new Date(), e);
            if (null != transaction && transaction.isActive() && jobDAO.getCurrentSession().isConnected() && jobDAO.getCurrentSession().isOpen()) {
                transaction.rollback();
            }
        } finally {
            HibernateSessionFactory.closeSession();
        }
    }
}
