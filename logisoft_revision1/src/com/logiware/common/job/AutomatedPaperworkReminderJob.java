package com.logiware.common.job;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.hibernate.HibernateSessionFactory;
import com.gp.cong.logisoft.bc.accounting.ReportConstants;
import com.gp.cong.logisoft.bc.fcl.BookingFclBC;
import com.gp.cong.logisoft.bc.fcl.QuotationBC;
import com.gp.cong.logisoft.bc.print.PrintReportsConstants;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.BookingFcl;
import com.gp.cvst.logisoft.hibernate.dao.BookingFclDAO;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.logiware.common.dao.JobDAO;
import com.logiware.common.domain.Job;
import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;
import org.hibernate.Transaction;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

/**
 *
 * @author Balaji.E
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class AutomatedPaperworkReminderJob implements org.quartz.Job, ConstantsInterface {

    private static final Logger log = Logger.getLogger(AutomatedPaperworkReminderJob.class);
    private static final String newLine = System.getProperty("line.separator");//This will retrieve line separator dependent on OS.
    private static BookingFcl bookingFcl = new BookingFcl();
    private static final BookingFclDAO bookingFclDao = new BookingFclDAO();
    private static final BookingFclBC bookingFclBC = new BookingFclBC();

    public void run() throws Exception {
        try {

            List automatedPaperWorkReminderList = bookingFclDao.getAutomatedPaperWorkReminderList();
            if (!automatedPaperWorkReminderList.isEmpty()) {
                EmailschedulerDAO emailschedulerDAO = new EmailschedulerDAO();
                String fileNoPrefix = CommonConstants.loadMessageResources().getMessage("fileNumberPrefix");
                String fileNoWithPrefix = "";
                String fileNo = "";
                Iterator itr = automatedPaperWorkReminderList.iterator();
                while (itr.hasNext()) {
                    EmailSchedulerVO mailTransaction = new EmailSchedulerVO();
                    Object[] row = (Object[]) itr.next();
                    fileNoWithPrefix = fileNoPrefix + (String) row[0];
                    fileNo = (String) row[0];
                    String msg = paperworkReminderMsg(fileNo);
                    mailTransaction.setFromAddress((String) row[1]);
                    mailTransaction.setToAddress((String) row[2]);
                    mailTransaction.setSubject("Paperwork Not Received for Booking# " + fileNoWithPrefix + ".");
                    mailTransaction.setHtmlMessage(msg.replace("fileNo", fileNoWithPrefix));
                    mailTransaction.setTextMessage(msg.replace("fileNo", fileNoWithPrefix));
                    mailTransaction.setName("PaperworkReminder");
                    mailTransaction.setType(CONTACT_MODE_EMAIL);
                    mailTransaction.setStatus(EMAIL_STATUS_PENDING);
                    mailTransaction.setFileLocation(printBookingReportForPaperWorkReminder(fileNo));
                    mailTransaction.setNoOfTries(0);
                    mailTransaction.setEmailDate(new Date());
                    mailTransaction.setModuleName("Paperwork Reminder");
                    mailTransaction.setModuleId("PaperworkReminder" + fileNoWithPrefix);
                    emailschedulerDAO.save(mailTransaction);
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public static String paperworkReminderMsg(String fileNo) throws Exception {
        StringBuilder msgBuilder = new StringBuilder();
        msgBuilder.append("Please be advised that we have not received paperwork for Booking# ").append("fileNo");
        msgBuilder.append(newLine);
        msgBuilder.append(newLine);
        msgBuilder.append("Please reply to this email with the corresponding paperwork.");
        msgBuilder.append(newLine);
        msgBuilder.append(newLine);
        msgBuilder.append(newLine);
        msgBuilder.append("Regards");
        msgBuilder.append(newLine);
        String companyCode = new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
        bookingFcl = bookingFclDao.findbyFileNo(fileNo);
        String brand = "";
        String companyName = "";
        if (null != bookingFcl) {
            brand = bookingFcl.getBrand();
        }
        if ("02".equals(companyCode) && brand.equalsIgnoreCase("OTI")) {
            companyName = LoadLogisoftProperties.getProperty("application.OTI.companyname");
            msgBuilder.append(companyName);
        } else if ("03".equals(companyCode) && brand.equalsIgnoreCase("Econo")) {
            companyName = LoadLogisoftProperties.getProperty("application.Econo.companyname");
            msgBuilder.append(companyName);
        } else if (brand.equalsIgnoreCase("Ecu Worldwide")) {
            companyName = LoadLogisoftProperties.getProperty("application.ECU.companyname");
            msgBuilder.append(companyName);
        }
        return msgBuilder.toString();
    }

    public String printBookingReportForPaperWorkReminder(String fileNo) throws Exception {
        MessageResources messageResources = CommonConstants.loadMessageResources();
        bookingFcl = bookingFclDao.findbyFileNo(fileNo);
        String regionRemarks = new QuotationBC().fetchRegionRemarks(bookingFcl.getPortofDischarge(), "booking");
        String outputFileName = LoadLogisoftProperties.getProperty("reportLocation") + "/Documents/" + ReportConstants.BOOKING_CONFIRMATION + "/" + DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/";
        File file = new File(outputFileName);
        if (!file.exists()) {
            file.mkdirs();
        }
        ServletContext servletContext = JobScheduler.servletContext;
        String contextPath = servletContext.getRealPath("/");
        outputFileName = outputFileName + bookingFcl.getFileNo() + ".pdf";
        bookingFclBC.createBookingConfirmationReport(bookingFcl.getBookingId().toString(), outputFileName, contextPath,
                messageResources, PrintReportsConstants.REQUEST_PARAM, regionRemarks, "", "", "", "", null);
        return outputFileName;
    }

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        JobDAO dao = new JobDAO();
        Transaction transaction = null;
        try {
            log.info("Automated Paper Work Reminder Job started on " + new Date());
            transaction = dao.getCurrentSession().getTransaction();
            if (!transaction.isActive()) {
                transaction.begin();
            }
            Job job = dao.findByClassName(AutomatedPaperworkReminderJob.class.getCanonicalName());
            job.setStartTime(new Date());
            run();
            job.setEndTime(new Date());
            transaction.commit();
            log.info("Automated Paper Work Reminder Job ended on " + new Date());
        } catch (Exception e) {
            log.info("Automated Paper Work Reminder Job failed on " + new Date(), e);
            try {
                Thread.sleep(5000);
                log.info("Automated Paper Work Reminder Job restarted on " + new Date());
                if (null == transaction || !transaction.isActive()) {
                    transaction = dao.getCurrentSession().getTransaction();
                    transaction.begin();
                } else {
                    transaction = dao.getCurrentSession().getTransaction();
                }
                Job job = dao.findByClassName(AutomatedPaperworkReminderJob.class.getCanonicalName());
                job.setStartTime(new Date());
                run();
                job.setEndTime(new Date());
                transaction.commit();
                log.info("Automated Paper Work Reminder Job ended on " + new Date());
            } catch (Exception ex) {
                log.info("Automated Paper Work Reminder Job failed again on " + new Date(), ex);
                if (null != transaction && transaction.isActive() && dao.getCurrentSession().isConnected() && dao.getCurrentSession().isOpen()) {
                    transaction.rollback();
                }
            } finally {
                HibernateSessionFactory.closeSession();
            }
        }
    }
}
