package com.logiware.thread;

import com.gp.cong.hibernate.HibernateSessionFactory;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Transaction;

public class LclVoyageNumberThread extends Thread {

    private static final Logger log = Logger.getLogger(LclVoyageNumberThread.class);
    private String voyageNumber = null;

    public synchronized String generate() {
        Transaction transaction = null;
        String voyageNumber = null;
        try {
            GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
            transaction = genericCodeDAO.getCurrentSession().beginTransaction();
            GenericCode genericCode = genericCodeDAO.findByCodeDesc("Lcl Voyage Number");
            String lastVoyageNumber = genericCode.getCode();
            if (CommonUtils.isNotEmpty(lastVoyageNumber)) {
                voyageNumber = StringUtils.leftPad("" + (Long.parseLong(lastVoyageNumber) + 1), 6, "0");
                genericCode.setCode(voyageNumber);
                genericCodeDAO.save(genericCode);
            }
            transaction.commit();
        } catch (Exception e) {
            log.info("generate Lcl Voyage Number failed on " + new Date(), e);
            if (transaction != null && !transaction.isActive()) {
                transaction.rollback();
            }
        } finally {
            HibernateSessionFactory.closeSession();
        }
        return voyageNumber;
    }

    @Override
    public synchronized void run() {
        voyageNumber = generate();
    }

    public String getVoyageNumber() throws InterruptedException {
        this.start();
        this.join();
        return voyageNumber;
    }
}
