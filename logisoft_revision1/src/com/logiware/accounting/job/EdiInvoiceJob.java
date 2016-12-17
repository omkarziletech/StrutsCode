package com.logiware.accounting.job;

import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.logisoft.jobscheduler.JobScheduler;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.common.constants.Company;
import com.logiware.accounting.dao.EdiInvoiceDAO;
import com.logiware.accounting.utils.EdiInvoiceUtils;
import com.logiware.datamigration.FilenameFilter;
import java.io.File;
import java.util.Date;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.hibernate.Transaction;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author Lakshmi Naryanan
 */
public class EdiInvoiceJob extends BaseHibernateDAO implements Job {

    private static final Logger log = Logger.getLogger(EdiInvoiceJob.class);

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
	    Transaction transaction = getCurrentSession().beginTransaction();
	    String source = LoadLogisoftProperties.getProperty("edi.invoice.source");
	    String today = "/" + DateUtils.formatDate(new Date(), "yyyy/MM/dd");
	    String archive = LoadLogisoftProperties.getProperty("edi.invoice.archive") + today;
	    File[] xmlFiles = new File(source).listFiles(new FilenameFilter("xml"));
	    if (null != xmlFiles && xmlFiles.length > 0) {
		ServletContext servletContext = JobScheduler.servletContext;
		String contextPath = servletContext.getRealPath("/");
		File archiveFolder = new File(archive);
		if (!archiveFolder.exists()) {
		    archiveFolder.mkdirs();
		}
		createInvoice(xmlFiles, Company.ECU_LINE, contextPath, archiveFolder);
	    }

	    File[] txtFiles = new File(source).listFiles(new FilenameFilter("txt"));
	    if (null != txtFiles && txtFiles.length > 0) {
		ServletContext servletContext = JobScheduler.servletContext;
		String contextPath = servletContext.getRealPath("/");
		File archiveFolder = new File(archive);
		if (!archiveFolder.exists()) {
		    archiveFolder.mkdirs();
		}
		createInvoice(txtFiles, Company.MAERSK_LINE, contextPath, archiveFolder);
	    }
	    if (null != transaction && transaction.isActive()) {
		transaction.commit();
	    }
	} catch (Exception e) {
	    log.info("Creating Edi Invoices from xml files failed on " + new Date(), e);
	}
    }

    private void setInvoiceStatus() {
	try {
	    Transaction transaction = getCurrentSession().beginTransaction();
	    ServletContext servletContext = JobScheduler.servletContext;
	    String contextPath = servletContext.getRealPath("/");
	    new EdiInvoiceDAO().setInvoiceStatus(contextPath);
	    if (null != transaction && transaction.isActive()) {
		transaction.commit();
	    }
	} catch (Exception e) {
	    log.info("Creating Edi Invoices from xml files failed on " + new Date(), e);
	}
    }

    public void execute(JobExecutionContext jec) throws JobExecutionException {
	createInvoiceFromXml();
	setInvoiceStatus();
    }

    public void start() {
	createInvoiceFromXml();
	setInvoiceStatus();
    }
}
