/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.thread;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.hibernate.HibernateSessionFactory;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Transaction;

/**
 *
 * @author aravindhan.v
 */
public class LclExportsVoyageNumberThread extends Thread {

    private static final Logger log = Logger.getLogger(LclExportsVoyageNumberThread.class);
    private String voyageNumber = null;
    public String code = null;
  
    public synchronized void run() {
        Transaction transaction = null;
        try {
            GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
            transaction = genericCodeDAO.getCurrentSession().beginTransaction();
            GenericCode genericCode = genericCodeDAO.findByCodeDesc(this.code);
            String lastVoyageNumber = genericCode.getCode();
            if (CommonUtils.isNotEmpty(lastVoyageNumber)) {
                voyageNumber = StringUtils.leftPad("" + (Long.parseLong(lastVoyageNumber) + 1), 
                        lastVoyageNumber.length(), "0");
                genericCode.setCode(voyageNumber);
                genericCodeDAO.save(genericCode);
            }
            transaction.commit();
        } catch (Exception e) {
            log.info("generate Lcl Exports Voyage Number failed on " + new Date(), e);
            if (transaction != null && !transaction.isActive()) {
                transaction.rollback();
            }
        } finally {
            HibernateSessionFactory.closeSession();
        }
    }

    public String getVoyageNumber(String codeDesc) throws InterruptedException {
        code=codeDesc;
        this.start();
        this.join();
       return voyageNumber;
    }
}
