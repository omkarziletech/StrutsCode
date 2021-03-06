/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.common.job;

import com.gp.cong.hibernate.HibernateSessionFactory;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import org.hibernate.Transaction;
import com.logiware.common.dao.JobDAO;
import com.logiware.common.domain.Job;
import com.logiware.common.utils.ExportNotificationCodeJUtil;
import java.util.Date;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author Mei
 */
public class NotificationLclBlCodeJJob implements org.quartz.Job, LclCommonConstant {

    private static final Logger log = Logger.getLogger(NotificationLclBlCodeJJob.class);

    public void run() throws Exception {
        new ExportNotificationCodeJUtil().sendBlNotification();
    }

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        JobDAO jobDAO = new JobDAO();
        Transaction transaction = null;
        try {
            log.info("Code J Notifications for B/Ls" + new Date());
            transaction = jobDAO.getCurrentSession().getTransaction();
            if (!transaction.isActive()) {
                transaction.begin();
            }
            Job job = jobDAO.findByClassName(NotificationLclBlCodeJJob.class.getCanonicalName());
            job.setStartTime(new Date());
            run();
            job.setEndTime(new Date());
            transaction.commit();
            log.info("Code J Notifications for B/Ls " + new Date());
        } catch (Exception e) {
            log.info("Code J Notifications for B/Ls" + new Date(), e);
            if (null != transaction && transaction.isActive() && jobDAO.getCurrentSession().isConnected() && jobDAO.getCurrentSession().isOpen()) {
                transaction.rollback();
            }
        } finally {
            HibernateSessionFactory.closeSession();
        }
    }
}
