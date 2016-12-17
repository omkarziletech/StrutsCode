package com.logiware.accounting.thread;

import com.gp.cong.hibernate.HibernateSessionFactory;
import com.gp.cvst.logisoft.domain.ApBatch;
import com.gp.cvst.logisoft.hibernate.dao.ApBatchDAO;
import java.util.Date;
import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.Transaction;
import org.hibernate.type.IntegerType;

/**
 *
 * @author Lakshmi Narayanan
 */
public class ApBatchThread extends Thread {

    private Integer batchId = null;
    private static final Logger log = Logger.getLogger(ApBatchThread.class);

    @Override
    public synchronized void run() {
        Transaction transaction = null;
        try {
            ApBatchDAO apBatchDAO = new ApBatchDAO();
            transaction = apBatchDAO.getCurrentSession().beginTransaction();
            String queryString = "select max(batch_id) as batchId from ap_batch";
            SQLQuery query = apBatchDAO.getCurrentSession().createSQLQuery(queryString);
            query.addScalar("batchId", IntegerType.INSTANCE);
            batchId = (Integer) query.uniqueResult();
            if (batchId == null) {
                batchId = 0;
            }
            ApBatch apBatch = new ApBatch(batchId + 1, "");
            apBatchDAO.save(apBatch);
            transaction.commit();
        } catch (Exception e) {
            log.info("AP Batch Id creation failed on " + new Date(), e);
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
        } finally {
            HibernateSessionFactory.closeSession();
        }
    }

    public Integer getBatchId() throws InterruptedException {
        this.start();
        this.join();
        return batchId;
    }
}
