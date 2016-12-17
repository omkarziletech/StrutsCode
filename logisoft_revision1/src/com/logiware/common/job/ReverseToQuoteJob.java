package com.logiware.common.job;

import com.gp.cong.hibernate.HibernateSessionFactory;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.domain.Notes;
import com.gp.cong.logisoft.hibernate.dao.NotesDAO;
import com.gp.cvst.logisoft.domain.BookingFcl;
import com.gp.cvst.logisoft.domain.Quotation;
import com.gp.cvst.logisoft.hibernate.dao.BookingFclDAO;
import com.gp.cvst.logisoft.hibernate.dao.QuotationDAO;
import com.logiware.common.dao.JobDAO;
import com.logiware.common.domain.Job;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Transaction;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

/**
 *
 * @author Lakshmi Narayanan
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ReverseToQuoteJob implements org.quartz.Job {

    private static final Logger log = Logger.getLogger(ReverseToQuoteJob.class);

    public static void run() throws Exception {
        BookingFclDAO bookingFclDAO = new BookingFclDAO();
        QuotationDAO quotationDAO = new QuotationDAO();
        NotesDAO notesDAO = new NotesDAO();
        try {
            List fileNolist = bookingFclDAO.getAutoReverseToQuoteFileList();
            for (Object object : fileNolist) {
                if (null != object) {
                    String fileNo = object.toString();
                    BookingFcl bookingFcl = bookingFclDAO.getFileNoObject(fileNo);
                    bookingFcl.setHazmatSet(null);
                    bookingFclDAO.delete(bookingFcl);
                    Quotation quotation = quotationDAO.getFileNoObject(fileNo);
                    if (null != quotation) {
                        quotation.setQuoteFlag("Open");
                        quotation.setFinalized("off");
                        quotation.setBookedBy("");
                        quotation.setBookedDate(null);
                        quotationDAO.update(quotation);
                    }
                    Date date = new Date();
                    Notes notes = new Notes();
                    notes.setModuleId(NotesConstants.FILE);
                    notes.setModuleRefId(null != fileNo ? "" + fileNo : "");
                    notes.setNoteType(NotesConstants.NOTES_TYPE_EVENT);
                    notes.setUpdateDate(date);
                    notes.setUpdatedBy("System");
                    notes.setNoteDesc("Booking has been expired and reverted to Quote");
                    notesDAO.save(notes);
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        JobDAO dao = new JobDAO();
        Transaction transaction = null;
        try {
            log.info("Reverse to Quote Job started on " + new Date());
            transaction = dao.getCurrentSession().getTransaction();
            if (!transaction.isActive()) {
                transaction.begin();
            }
            Job job = dao.findByClassName(ReverseToQuoteJob.class.getCanonicalName());
            job.setStartTime(new Date());
            run();
            job.setEndTime(new Date());
            transaction.commit();
            log.info("Reverse to Quote Job ended on " + new Date());
        } catch (Exception e) {
            log.info("Reverse to Quote Job failed on " + new Date(), e);
            try {
                Thread.sleep(5000);
                log.info("Reverse to Quote Job restarted on " + new Date());
                if (null == transaction || !transaction.isActive()) {
                    transaction = dao.getCurrentSession().getTransaction();
                    transaction.begin();
                } else {
                    transaction = dao.getCurrentSession().getTransaction();
                }
                Job job = dao.findByClassName(ReverseToQuoteJob.class.getCanonicalName());
                job.setStartTime(new Date());
                run();
                job.setEndTime(new Date());
                transaction.commit();
                log.info("Reverse to Quote Job ended on " + new Date());
            } catch (Exception ex) {
                log.info("Reverse to Quote Job failed again on " + new Date(), ex);
                if (null != transaction && transaction.isActive() && dao.getCurrentSession().isConnected() && dao.getCurrentSession().isOpen()) {
                    transaction.rollback();
                }
            } finally {
                HibernateSessionFactory.closeSession();
            }
        }
    }
}
