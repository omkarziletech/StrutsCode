/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.common.job;

import com.gp.cong.hibernate.HibernateSessionFactory;
import org.hibernate.Transaction;
import com.logiware.common.dao.JobDAO;
import com.logiware.common.domain.Job;
import com.logiware.common.utils.LclImportWarehouseRateUtil;
import java.util.Date;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

/**
 *
 * @author Mei
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class LclImportWarehouseRateJob implements org.quartz.Job {

    private static final Logger log = Logger.getLogger(LclImportWarehouseRateJob.class);

    public void run() throws Exception {
        new LclImportWarehouseRateUtil().manifestByWareHsCharge();

    }

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        JobDAO jobDAO = new JobDAO();
        Transaction transaction = null;
        try {
            log.info("Import Warehouse Rate Job started on " + new Date());
            transaction = jobDAO.getCurrentSession().getTransaction();
            if (!transaction.isActive()) {
                transaction.begin();
            }
            Job job = jobDAO.findByClassName(LclImportWarehouseRateJob.class.getCanonicalName());
            job.setStartTime(new Date());
            run();
            job.setEndTime(new Date());
            transaction.commit();
            log.info("Import Warehouse Rate ended on " + new Date());
        } catch (Exception e) {
            log.info("Import Warehouse Rate failed on " + new Date(), e);
            if (null != transaction && transaction.isActive() && jobDAO.getCurrentSession().isConnected() && jobDAO.getCurrentSession().isOpen()) {
                transaction.rollback();
            }
        } finally {
            HibernateSessionFactory.closeSession();
        }
    }
}
