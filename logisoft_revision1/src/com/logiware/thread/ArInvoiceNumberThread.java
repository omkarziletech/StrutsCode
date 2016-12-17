package com.logiware.thread;

import com.gp.cong.common.CommonUtils;
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
public class ArInvoiceNumberThread extends Thread {

    private static final Logger log = Logger.getLogger(ArInvoiceNumberThread.class);
    private String invoiceNumber = null;

    public synchronized String generate() {
        Transaction transaction = null;
        String invoiceNumber = null;
        try {
            GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
            transaction = genericCodeDAO.getCurrentSession().beginTransaction();
            GenericCode genericCode = genericCodeDAO.findByCodeDesc("AR Invoice Number");
            String lastInvoiceNumber = genericCode.getCode();
            if (CommonUtils.isNotEmpty(lastInvoiceNumber)) {
                invoiceNumber = StringUtils.leftPad("" + (Long.parseLong(lastInvoiceNumber) + 1), 9, "0");
                genericCode.setCode(invoiceNumber);
                genericCodeDAO.save(genericCode);
            }
            transaction.commit();
        } catch (Exception e) {
            log.info("generate AR Invoice Number failed on " + new Date(), e);
            if (transaction != null && !transaction.isActive()) {
                transaction.rollback();
            }
        }
        return invoiceNumber;
    }

    @Override
    public synchronized void run() {
        invoiceNumber = generate();
    }

    public String getInvoiceNumber() throws InterruptedException {
        this.start();
        this.join();
        return invoiceNumber;
    }

}
