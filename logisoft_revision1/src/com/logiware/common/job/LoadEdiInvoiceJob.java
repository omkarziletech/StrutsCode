package com.logiware.common.job;

import com.gp.cong.common.DateUtils;
import com.gp.cong.hibernate.HibernateSessionFactory;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.accounting.dao.EdiInvoiceDAO;
import com.logiware.accounting.utils.EdiInvoiceUtils;
import com.logiware.common.constants.Company;
import com.logiware.common.dao.JobDAO;
import com.logiware.common.domain.Job;
import com.logiware.datamigration.FilenameFilter;
import java.io.File;
import java.util.Date;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
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
public class LoadEdiInvoiceJob implements org.quartz.Job {

    private static final Logger log = Logger.getLogger(LoadEdiInvoiceJob.class);
    private static final String contextPath = JobScheduler.servletContext.getRealPath("/");
    private static final JobDAO dao = new JobDAO();
    private Transaction transaction = null;

    private void createInvoice(File[] files, Company company, String contextPath, File archiveFolder) throws Exception {
        for (File file : files) {
            try {
                EdiInvoiceUtils.createInvoiceFromXml(file, company, contextPath);
                File archiveFile = new File(archiveFolder, file.getName());
                if (archiveFile.exists()) {
                    archiveFile.delete();
                }
                file.renameTo(archiveFile);
            } catch (Exception e) {
                throw e;
            }
        }
    }

    private void createInvoiceFromXml() {
        try {
            log.info("Creating Edi Invoices from xml files started on " + new Date());
            String source = LoadLogisoftProperties.getProperty("edi.invoice.source");
            String today = "/" + DateUtils.formatDate(new Date(), "yyyy/MM/dd");
            String archive = LoadLogisoftProperties.getProperty("edi.invoice.archive") + today;
            File[] xmlFiles = new File(source).listFiles(new FilenameFilter("xml"));
            if (null != xmlFiles && xmlFiles.length > 0) {
                File archiveFolder = new File(archive);
                if (!archiveFolder.exists()) {
                    archiveFolder.mkdirs();
                }
                createInvoice(xmlFiles, Company.ECU_LINE, contextPath, archiveFolder);
            }

            File[] txtFiles = new File(source).listFiles(new FilenameFilter("txt"));
            if (null != txtFiles && txtFiles.length > 0) {
                File archiveFolder = new File(archive);
                if (!archiveFolder.exists()) {
                    archiveFolder.mkdirs();
                }
                createInvoice(txtFiles, Company.MAERSK_LINE, contextPath, archiveFolder);
            }
            log.info("Creating Edi Invoices from xml files ended on " + new Date());
        } catch (Exception e) {
            log.info("Creating Edi Invoices from xml files failed on " + new Date(), e);
        }
    }

    private void setInvoiceStatus() {
        try {
            log.info("Updating EDI Invoice Status started on " + new Date());
            new EdiInvoiceDAO().setInvoiceStatus(contextPath);
            log.info("Updating EDI Invoice Status ended on " + new Date());
        } catch (Exception e) {
            log.info("Updating EDI Invoice Status failed on " + new Date(), e);
        }
    }

    public void run() throws Exception {
        try {
            transaction = dao.getCurrentSession().getTransaction();
            if (!transaction.isActive()) {
                transaction.begin();
            }
            createInvoiceFromXml();
            transaction = dao.getCurrentSession().getTransaction();
            if (!transaction.isActive()) {
                transaction.begin();
            }
            transaction.commit();
            transaction = dao.getCurrentSession().getTransaction();
            transaction.begin();
        } catch (HibernateException e) {
            if (null != transaction && transaction.isActive() && dao.getCurrentSession().isConnected() && dao.getCurrentSession().isOpen()) {
                transaction.rollback();
            }
            throw e;
        }
        try {
            transaction = dao.getCurrentSession().getTransaction();
            if (!transaction.isActive()) {
                transaction.begin();
            }
            setInvoiceStatus();
            transaction = dao.getCurrentSession().getTransaction();
            if (transaction.isActive()) {
                transaction.commit();
            }
            transaction = dao.getCurrentSession().getTransaction();
            transaction.begin();
        } catch (HibernateException e) {
            if (null != transaction && transaction.isActive() && dao.getCurrentSession().isConnected() && dao.getCurrentSession().isOpen()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        try {
            log.info("Load EDI Invoice Job started on " + new Date());
            transaction = dao.getCurrentSession().getTransaction();
            if (!transaction.isActive()) {
                transaction.begin();
            }
            Job job = dao.findByClassName(LoadEdiInvoiceJob.class.getCanonicalName());
            job.setStartTime(new Date());
            run();
            job.setEndTime(new Date());
            if (null == transaction || !transaction.isActive()) {
                transaction = dao.getCurrentSession().getTransaction();
                transaction.begin();
            } else {
                transaction = dao.getCurrentSession().getTransaction();
            }
            log.info("Load EDI Invoice Job ended on " + new Date());
        } catch (Exception e) {
            log.info("Load EDI Invoice Job failed on " + new Date(), e);
            try {
                Thread.sleep(5000);
                log.info("Load EDI Invoice Job restarted on " + new Date());
                if (null == transaction || !transaction.isActive()) {
                    transaction = dao.getCurrentSession().getTransaction();
                    transaction.begin();
                } else {
                    transaction = dao.getCurrentSession().getTransaction();
                }
                Job job = dao.findByClassName(LoadEdiInvoiceJob.class.getCanonicalName());
                job.setStartTime(new Date());
                run();
                job.setEndTime(new Date());
                transaction.commit();
                log.info("Load EDI Invoice Job ended on " + new Date());
            } catch (Exception ex) {
                log.info("Load EDI Invoice Job failed again on " + new Date(), ex);
                if (null != transaction && transaction.isActive() && dao.getCurrentSession().isConnected() && dao.getCurrentSession().isOpen()) {
                    transaction.rollback();
                }
            } finally {
                HibernateSessionFactory.closeSession();
            }
        }
    }
}
