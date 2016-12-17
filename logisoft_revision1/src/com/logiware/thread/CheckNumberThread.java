package com.logiware.thread;

import com.gp.cong.hibernate.HibernateSessionFactory;
import com.gp.cvst.logisoft.hibernate.dao.BankDetailsDAO;
import java.util.Date;
import org.apache.log4j.Logger;
import org.hibernate.Transaction;

/**
 *
 * @author venugopal.s
 */
public class CheckNumberThread extends Thread {

    private Integer checkNumber = null;
    private static final Logger log = Logger.getLogger(CheckNumberThread.class);
    private String bankName = null;
    private String bankAccountNo = null;

    @Override
    public synchronized void run() {
        Transaction transaction = null;
        try {
            BankDetailsDAO bankDetailsDAO = new BankDetailsDAO();
            transaction = bankDetailsDAO.getCurrentSession().beginTransaction();
            checkNumber = bankDetailsDAO.getStartingNumber(bankName, bankAccountNo);
            bankDetailsDAO.updateStartingNumber(bankName, bankAccountNo, checkNumber + 1);
            transaction.commit();
        } catch (Exception e) {
            log.info("CheckNumber Creation failed on" + new Date(), e);
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
        } finally {
            HibernateSessionFactory.closeSession();
        }
    }

    public Integer getCheckNumber(String bankname, String bankAcctNo) throws InterruptedException {
        bankName = bankname;
        bankAccountNo = bankAcctNo;
        this.start();
        this.join();
        return checkNumber;
    }
}
