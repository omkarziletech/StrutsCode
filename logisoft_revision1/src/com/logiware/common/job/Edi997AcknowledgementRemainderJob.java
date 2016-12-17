package com.logiware.common.job;

import com.gp.cong.hibernate.HibernateSessionFactory;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import static com.gp.cong.lcl.common.constant.LclCommonConstant.EMAIL_TYPE_E2;
import static com.gp.cong.lcl.common.constant.LclCommonConstant.FAX_TYPE_F2;
import com.logiware.common.dao.JobDAO;
import com.logiware.common.domain.Job;
import com.logiware.common.utils.Edi997AcknowledgementRemainderUtil;
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
 * @author User
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class Edi997AcknowledgementRemainderJob implements org.quartz.Job, LclCommonConstant {

    private static final Logger log = Logger.getLogger(DispositionChangeAnytimeJob.class);

    public void run() throws Exception {
        new Edi997AcknowledgementRemainderUtil().sendEdi997AcknowledgementRemainder();
    }

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        JobDAO jobDAO = new JobDAO();
        Transaction transaction = null;
        try {
            log.info("997 Acknowledgement reminder Update Daily Job started on " + new Date());
            transaction = jobDAO.getCurrentSession().getTransaction();
            if (!transaction.isActive()) {
                transaction.begin();
            }
            Job job = jobDAO.findByClassName(DispositionChangeAnytimeJob.class.getCanonicalName());
            job.setStartTime(new Date());
            run();
            job.setEndTime(new Date());
            transaction.commit();
            log.info("997 Acknowledgement reminder Update Daily Job ended on " + new Date());
        } catch (Exception e) {
            log.info("997 Acknowledgement reminder Update Daily Job failed on " + new Date(), e);
            if (null != transaction && transaction.isActive() && jobDAO.getCurrentSession().isConnected() && jobDAO.getCurrentSession().isOpen()) {
                transaction.rollback();
            }
        } finally {
            HibernateSessionFactory.closeSession();
        }
    }
}
