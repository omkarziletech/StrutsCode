package com.logiware.accounting.thread;

import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.hibernate.HibernateSessionFactory;
import com.gp.cvst.logisoft.hibernate.dao.BankDetailsDAO;
import java.util.Date;
import org.apache.log4j.Logger;
import org.hibernate.Transaction;

/**
 *
 * @author Lucky
 */
public class CardCountThread extends Thread {

    private static final Logger log = Logger.getLogger(CardCountThread.class);
    private Integer count = null;
    private String bankName = null;
    private String bankAccountNo = null;
    private String paymentMethod = null;

    @Override
    public synchronized void run() {
        Transaction transaction = null;
        try {
            BankDetailsDAO bankDetailsDAO = new BankDetailsDAO();
            transaction = bankDetailsDAO.getCurrentSession().beginTransaction();
            if (ConstantsInterface.PAYMENT_METHOD_ACH_DEBIT.equalsIgnoreCase(paymentMethod)) {
                count = bankDetailsDAO.getAchDebitCount(bankName, bankAccountNo);
                bankDetailsDAO.updateAchDebitCount(bankName, bankAccountNo);
            } else {
                count = bankDetailsDAO.getCreditCardCount(bankName, bankAccountNo);
                bankDetailsDAO.updateCreditCardCount(bankName, bankAccountNo);
            }
            transaction.commit();
        } catch (Exception e) {
            log.info(paymentMethod + " count creation failed on " + new Date(), e);
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
        } finally {
            HibernateSessionFactory.closeSession();
        }
    }

    public Integer getCount(String bankname, String bankAcctNo, String paymentMethod) throws InterruptedException {
        this.bankName = bankname;
        this.bankAccountNo = bankAcctNo;
        this.paymentMethod = paymentMethod;
        this.start();
        this.join();
        return count;
    }
}
