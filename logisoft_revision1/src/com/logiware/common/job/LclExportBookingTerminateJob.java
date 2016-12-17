/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.common.job;

import com.gp.cong.hibernate.HibernateSessionFactory;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import java.util.Date;
import org.apache.log4j.Logger;
import org.hibernate.Transaction;
import org.quartz.DisallowConcurrentExecution;
import com.logiware.common.dao.JobDAO;
import com.logiware.common.domain.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import com.logiware.common.dao.PropertyDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import java.util.List;

/**
 *
 * @author aravindhan.v
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class LclExportBookingTerminateJob implements org.quartz.Job, LclCommonConstant {

    private static final Logger log = Logger.getLogger(LclExportBookingTerminateJob.class);

    public void run() throws Exception {
        String expiryCount = new PropertyDAO().getProperty("LclExportBookingExpiryDate");
        List<Object> li = new LCLBookingDAO().getExpiredFileNumberList(expiryCount); 
        if(!li.isEmpty()){
            new LclFileNumberDAO().updateFileIdByList(li, "X");
        }
    }

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        JobDAO jobDAO = new JobDAO();
        Transaction transaction = null;
        try {
            log.info("Booking Terminate Job started on " + new Date());
            transaction = jobDAO.getCurrentSession().getTransaction();
            if (!transaction.isActive()) {
                transaction.begin();
            }
            Job job = jobDAO.findByClassName(LclExportBookingTerminateJob.class.getCanonicalName());
            job.setStartTime(new Date());
            run();
            job.setEndTime(new Date());
            transaction.commit();
            log.info("Booking Terminate Job ended on " + new Date());
        } catch (Exception e) {
            log.info("Booking Terminate Job failed on " + new Date(), e);
            if (null != transaction && transaction.isActive() && jobDAO.getCurrentSession().isConnected() && jobDAO.getCurrentSession().isOpen()) {
                transaction.rollback();
            }
        } finally {
            HibernateSessionFactory.closeSession();
        }
    }
}
