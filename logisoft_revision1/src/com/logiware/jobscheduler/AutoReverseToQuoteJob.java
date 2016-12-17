package com.logiware.jobscheduler;

import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.domain.Notes;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.logisoft.hibernate.dao.NotesDAO;
import com.gp.cvst.logisoft.domain.BookingFcl;
import com.gp.cvst.logisoft.domain.Quotation;
import com.gp.cvst.logisoft.hibernate.dao.BookingFclDAO;
import com.gp.cvst.logisoft.hibernate.dao.QuotationDAO;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import org.hibernate.Transaction;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import org.apache.log4j.Logger;

/**
 *
 * @author Lakshmi Narayanan
 */
public class AutoReverseToQuoteJob extends BaseHibernateDAO implements Job, Serializable {

    private static final Logger log = Logger.getLogger(AutoReverseToQuoteJob.class);
    private static final long serialVersionUID = -5188466178327978542L;

    public void execute(JobExecutionContext jec) {
	Transaction hibernateTransaction = getCurrentSession().beginTransaction();
	try {
	    autoReverseToQuote();
	    hibernateTransaction.commit();
	} catch (Exception ex) {
	    log.info("execute failed on " + new Date(),ex);
	}
    }

    public void autoReverseToQuote() throws Exception {
	List fileNolist = new BookingFclDAO().getAutoReverseToQuoteFileList();
	for (Object object : fileNolist) {
	    if (null != object) {
		String fileNo = object.toString();
		BookingFcl bookingFcl = new BookingFclDAO().getFileNoObject(fileNo);
		bookingFcl.setHazmatSet(null);
		new BookingFclDAO().delete(bookingFcl);
		Quotation quotation = new QuotationDAO().getFileNoObject(fileNo);
		if (null != quotation) {
		    quotation.setQuoteFlag("Open");
		    quotation.setFinalized("off");
		    quotation.setBookedBy("");
		    quotation.setBookedDate(null);
		    new QuotationDAO().update(quotation);
		}
		NotesDAO notesDAO = new NotesDAO();
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

    }

    public void startScheduler() throws Exception {
	autoReverseToQuote();
    }
}
