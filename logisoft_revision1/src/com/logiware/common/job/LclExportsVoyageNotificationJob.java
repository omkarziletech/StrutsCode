/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.common.job;

import com.gp.cong.hibernate.HibernateSessionFactory;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclExportsVoyageNotificationDAO;
import com.logiware.common.dao.JobDAO;
import com.logiware.common.domain.Job;
import com.logiware.common.utils.ExportNotificationUtil;
import com.logiware.lcl.model.LclNotificationModel;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.hibernate.Transaction;

/**
 *
 * @author aravindhan.v
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class LclExportsVoyageNotificationJob implements org.quartz.Job, LclCommonConstant {

    private static final Logger log = Logger.getLogger(LclExportsVoyageNotificationJob.class);

    public void run() throws Exception {
        List<LclNotificationModel> notificationIds = new LclExportsVoyageNotificationDAO().getNotificationIds("pending");
        if (notificationIds != null) {
            for (LclNotificationModel notification : notificationIds) {
                new ExportNotificationUtil().sendVoyageNotificationCodeI(notification.getSsDetailId(), null, 
                        EMAIL_TYPE_E1, FAX_TYPE_F1, notification.getId());
            }
        }
    }

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        JobDAO jobDAO = new JobDAO();
        Transaction transaction = null;
        try {
            log.info("Exports Voyage Notification Job started on " + new Date());
            transaction = jobDAO.getCurrentSession().getTransaction();
            if (!transaction.isActive()) {
                transaction.begin();
            }
            Job job = jobDAO.findByClassName(LclExportsVoyageNotificationJob.class.getCanonicalName());
            job.setStartTime(new Date());
            run();
            job.setEndTime(new Date());
            transaction.commit();
            log.info("Exports Voyage Notification Job ended on " + new Date());
        } catch (Exception ex) {
            log.info("Exports Voyage Notification Job failed on " + new Date(), ex);
            if (null != transaction && transaction.isActive() && jobDAO.getCurrentSession().isConnected() && jobDAO.getCurrentSession().isOpen()) {
                transaction.rollback();
            }
        } finally {
            HibernateSessionFactory.closeSession();
        }
    }
}
