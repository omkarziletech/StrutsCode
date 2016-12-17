package com.logiware.thread;

import com.gp.cong.hibernate.HibernateSessionFactory;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Transaction;

/**
 *
 * @author Lakshmi Narayanan
 */
public class LclFileNumberThread extends Thread {

    private String fileNumber = null;
    private static final Logger log = Logger.getLogger(LclFileNumberThread.class);

    @Override
    public synchronized void run() {
        Transaction transaction = null;
        try {
            GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
            transaction = genericCodeDAO.getCurrentSession().beginTransaction();
            GenericCode genericCode = genericCodeDAO.findByCodeDesc("LCL File Number");
            String lastFileNumber = genericCode.getCode();
            String letter = StringUtils.substring(lastFileNumber, 0, 1);
            String number = StringUtils.substring(lastFileNumber, 1);
            if (StringUtils.isNumeric(number) && Integer.parseInt(number) < 99999) {
                number = String.valueOf(Integer.parseInt(number) + 1);
            } else {
                if ("Z".equalsIgnoreCase(letter)) {
                    letter = "A";
                } else {
                    letter = String.valueOf((char) (((int) letter.charAt(0)) + 1));
                }
                number = "1";
            }
            fileNumber = letter + StringUtils.leftPad(number, 5, "0");
            genericCode.setCode(fileNumber);
            genericCodeDAO.save(genericCode);
            transaction.commit();
        } catch (Exception e) {
            log.info("LclFileNumber Creation failed on " + new Date(), e);
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
        } finally {
            HibernateSessionFactory.closeSession();
        }
    }

    public String getFileNumber() throws InterruptedException {
        this.start();
        this.join();
        return fileNumber;
    }
}
