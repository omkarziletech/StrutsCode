package com.logiware.common.job;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.hibernate.HibernateSessionFactory;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cvst.logisoft.beans.TransactionBean;
import com.gp.cvst.logisoft.domain.BankDetails;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.logiware.common.dao.JobDAO;
import com.logiware.common.domain.Job;
import com.logiware.common.model.ProgressBar;
import com.logiware.hibernate.dao.AchProcessDAO;
import com.logiware.hibernate.dao.AchSetUpDAO;
import com.logiware.hibernate.domain.AchProcessHistory;
import com.logiware.hibernate.domain.AchSetUp;
import com.logiware.reports.AchFileCreator;
import com.logiware.security.SftpClient;
import com.logiware.utils.AchSetUpUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletContext;
import org.apache.commons.io.IOUtils;
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
public class AchJob implements org.quartz.Job, ConstantsInterface {

    private static final Logger log = Logger.getLogger(AchJob.class);
    private static final JobDAO dao = new JobDAO();

    public ProgressBar getProgressBar() throws Exception {
        ServletContext servletContext = JobScheduler.servletContext;
        ProgressBar progressBar = (ProgressBar) servletContext.getAttribute("progressBar");
        if (null != progressBar) {
            if (CommonUtils.in(progressBar.getStatus(), "ended", "failed")) {
                servletContext.removeAttribute("progressBar");
            }
            return progressBar;
        } else {
            return null;
        }
    }

    private static void updateProgressBar(String message, Integer percentage) {
        ServletContext servletContext = JobScheduler.servletContext;
        ProgressBar progressBar = (ProgressBar) servletContext.getAttribute("progressBar");
        if (null != progressBar) {
            progressBar.setMessage(progressBar.getMessage() + "<li class='" + (message.contains("failed") ? "no" : "yes") + "'>" + message + "</li>");
        } else {
            progressBar = new ProgressBar();
            progressBar.setMessage("<li class='" + (message.contains("failed") ? "no" : "yes") + "'>" + message + "</li>");
        }
        progressBar.setPercentage(percentage);
        servletContext.setAttribute("progressBar", progressBar);
    }

    private static void updateProgressBar(String message, String status, Integer percentage) {
        ServletContext servletContext = JobScheduler.servletContext;
        ProgressBar progressBar = (ProgressBar) servletContext.getAttribute("progressBar");
        if (null != progressBar) {
            progressBar.setMessage(progressBar.getMessage() + "<li class='" + (message.contains("failed") ? "no" : "yes") + "'>" + message + "</li>");
        } else {
            progressBar = new ProgressBar();
            progressBar.setMessage("<li class='" + (message.contains("failed") ? "no" : "yes") + "'>" + message + "</li>");
        }
        progressBar.setPercentage(percentage);
        progressBar.setStatus(status);
        servletContext.setAttribute("progressBar", progressBar);
    }

    private static void createTextFile(List<TransactionBean> payments, BankDetails bankDetails, AchProcessHistory history) throws Exception {
        InputStream is = null;
        try {
            String achFileName = new AchFileCreator().createAchFile(payments, bankDetails);
            is = new FileInputStream(new File(achFileName));
            Blob blob = dao.getCurrentSession().getLobHelper().createBlob(IOUtils.toByteArray(is));
            history.setAchFile(blob);
            history.setAchFileName(achFileName);
            history.setStatus("Text File Created");
        } catch (IOException e) {
            throw e;
        } finally {
            if (null != is) {
                is.close();
            }
        }
    }

    private static void uploadFile(BankDetails bank, AchProcessHistory history) throws Exception {
        try {
            SystemRulesDAO systemRulesDAO = new SystemRulesDAO();
            AchSetUp ach = bank.getAchSetUp();
            SftpClient sftp = new SftpClient();
            sftp.upload(ach, history);
            String fromName = AchSetUpUtils.getProperty("achController");
            String fromAddress = AchSetUpUtils.getProperty("achControllerEmail");
            String subject = NumberUtils.formatNumber(history.getAmount(), "") + " USD";
            String companyName = systemRulesDAO.getSystemRules("CompanyName");
            String acctNo = systemRulesDAO.getSystemRules("EFTAccountNo");
            String textMessage = "Company : " + companyName + "\nAccount : " + acctNo + "\nFile total : " + subject;
            String htmlMessage = "Company : " + companyName + "<br>Account : " + acctNo + "<br>File total : " + subject;
            EmailSchedulerVO email = new EmailSchedulerVO();
            email.setToName(bank.getBankName());
            email.setToAddress(bank.getBankEmail());
            email.setFromName(fromName);
            email.setFromAddress(fromAddress);
            email.setSubject(subject);
            email.setHtmlMessage(htmlMessage);
            email.setTextMessage(textMessage);
            email.setName("ACHFTPUPLOAD");
            email.setType(CONTACT_MODE_EMAIL);
            email.setStatus(EMAIL_STATUS_PENDING);
            email.setNoOfTries(0);
            email.setEmailDate(new Date());
            email.setModuleName("ACHFTPUPLOAD");
            email.setModuleId("" + ach.getBatchSequence());
            new EmailschedulerDAO().saveEmail(email);
            history.setStatus("FTP Upload");
        } catch (Exception e) {
            throw e;
        }
    }

    private static void run(boolean doUpload, String id) {
        ServletContext servletContext = JobScheduler.servletContext;
        ProgressBar progressBar = (ProgressBar) servletContext.getAttribute("progressBar");
        if (null == progressBar) {
            StringBuilder msg = new StringBuilder();
            int percentage = 1;
            msg.append("ACH Job started");
            updateProgressBar(msg.toString(), percentage);
            boolean failed = false;
            try {
                Thread.sleep(1000);
                AchSetUpDAO achSetUpDAO = new AchSetUpDAO();
                AchProcessDAO achProcessDAO = new AchProcessDAO();
                if (CommonUtils.isNotEmpty(id)) {
                    AchProcessHistory history = achProcessDAO.findAchProcessHistoryById(Integer.parseInt(id));
                    if (CommonUtils.isNotEqual(history.getStatus(), "Completed")) {
                        BankDetails bank = history.getBankDetails();
                        String key = bank.getBankName() + " == " + bank.getBankAcctNo();

                        if (!failed && CommonUtils.isNotEqual(history.getStatus(), "Sent Status Failed")) {
                            msg.delete(0, msg.length());
                            percentage += 1;
                            msg.append("FTP uploading for ").append(key).append(" started");
                            updateProgressBar(msg.toString(), percentage);
                            msg.delete(0, msg.length());
                            percentage += 30;
                            try {
                                uploadFile(bank, history);
                                msg.append("FTP uploading for ").append(key).append(" ended");
                            } catch (Exception e) {
                                msg.append("FTP uploading for ").append(key).append(" failed");
                                log.info(msg.toString() + " on " + new Date(), e);
                                history.setStatus("FTP Failed");
                                failed = true;
                            } finally {
                                updateProgressBar(msg.toString(), percentage);
                            }
                        }

                        if (!failed) {
                            msg.delete(0, msg.length());
                            percentage += 1;
                            msg.append("Marking payments as sent for ").append(key).append(" started");
                            updateProgressBar(msg.toString(), percentage);
                            msg.delete(0, msg.length());
                            percentage = 99;
                            try {
                                int achBatchSequence = bank.getAchSetUp().getBatchSequence() - 1;
                                achSetUpDAO.updateAchPayments(bank.getBankName(), bank.getBankAcctNo(), achBatchSequence, STATUS_SENT);
                                history.setStatus("Completed");
                                history.setEndTime(new Date());
                                msg.append("Marking payments as sent for ").append(key).append(" ended");
                            } catch (Exception e) {
                                msg.append("Marking payments as sent for ").append(key).append(" failed");
                                log.info(msg.toString() + " on " + new Date(), e);
                                throw e;
                            } finally {
                                updateProgressBar(msg.toString(), percentage);
                            }
                        }
                        achProcessDAO.updateAchProcessHistory(history);
                    }
                } else {
                    msg.delete(0, msg.length());
                    percentage += 3;
                    msg.append("Getting all bank accounts with ACH setup from database started");
                    updateProgressBar(msg.toString(), percentage);
                    msg.delete(0, msg.length());
                    percentage += 1;
                    List<BankDetails> banks = null;
                    try {
                        banks = achSetUpDAO.getBankDetailsForAch();
                        msg.append("Getting all bank accounts with ACH setup from database ended");
                    } catch (Exception e) {
                        msg.append("Getting all bank accounts with ACH setup from database failed");
                        log.info(msg.toString() + " on " + new Date(), e);
                        throw e;
                    } finally {
                        updateProgressBar(msg.toString(), percentage);
                    }

                    if (CommonUtils.isNotEmpty(banks)) {
                        int processTime = (((90 / banks.size()) - 3) / 3) - 1;
                        boolean noPayments = true;
                        Thread.sleep(1000);
                        for (BankDetails bank : banks) {
                            String key = bank.getBankName() + " == " + bank.getBankAcctNo();
                            int achBatchSequence = bank.getAchSetUp().getBatchSequence();

                            msg.delete(0, msg.length());
                            percentage += 1;
                            msg.append("Getting payments for ").append(key).append(" started");
                            updateProgressBar(msg.toString(), percentage);
                            msg.delete(0, msg.length());
                            percentage += 1;
                            List<TransactionBean> payments = null;
                            try {
                                payments = achSetUpDAO.getAchPayments(bank.getBankName(), bank.getBankAcctNo());
                                msg.append("Getting payments for ").append(key).append(" ended");
                            } catch (Exception e) {
                                msg.append("Getting payments for ").append(key).append(" failed");
                                log.info(msg.toString() + " on " + new Date(), e);
                                throw e;
                            } finally {
                                updateProgressBar(msg.toString(), percentage);
                            }

                            if (CommonUtils.isNotEmpty(payments)) {
                                noPayments = false;

                                AchProcessHistory history = new AchProcessHistory();
                                history.setStartTime(new Date());
                                Double totalAmount = 0d;
                                for (TransactionBean achPayments : payments) {
                                    totalAmount += Double.parseDouble(achPayments.getAmount().replaceAll(",", ""));
                                }
                                history.setAmount(totalAmount);

                                msg.delete(0, msg.length());
                                percentage += 1;
                                msg.append("Processing payments for ").append(key).append(" started");
                                updateProgressBar(msg.toString(), percentage);

                                msg.delete(0, msg.length());
                                percentage += 1;
                                msg.append("Text file creation for ").append(key).append(" started");
                                updateProgressBar(msg.toString(), percentage);
                                msg.delete(0, msg.length());
                                percentage += processTime;
                                try {
                                    createTextFile(payments, bank, history);
                                    msg.append("Text file creation for ").append(key).append(" ended");
                                } catch (Exception e) {
                                    msg.append("Text file creation for ").append(key).append(" failed");
                                    log.info(msg.toString() + " on " + new Date(), e);
                                    throw e;
                                } finally {
                                    updateProgressBar(msg.toString(), percentage);
                                }

                                msg.delete(0, msg.length());
                                percentage += 1;
                                msg.append("Marking payments as ready to send for ").append(key).append(" started");
                                updateProgressBar(msg.toString(), percentage);
                                msg.delete(0, msg.length());
                                percentage += 1;
                                try {
                                    history.setStatus("Ready to send");
                                    achSetUpDAO.updateAchPayments(bank.getBankName(), bank.getBankAcctNo(), achBatchSequence, STATUS_READY_TO_SEND);
                                    bank.getAchSetUp().setBatchSequence(achBatchSequence + 1);
                                    msg.append("Marking payments as ready to send for ").append(key).append(" ended");
                                } catch (Exception e) {
                                    msg.append("Marking payments as ready to send for ").append(key).append(" failed");
                                    log.info(msg.toString() + " on " + new Date(), e);
                                    throw e;
                                } finally {
                                    updateProgressBar(msg.toString(), percentage);
                                }

                                if (doUpload && !failed) {
                                    msg.delete(0, msg.length());
                                    percentage += 1;
                                    msg.append("FTP uploading for ").append(key).append(" started");
                                    updateProgressBar(msg.toString(), percentage);
                                    msg.delete(0, msg.length());
                                    percentage += processTime;
                                    try {
                                        uploadFile(bank, history);
                                        msg.append("FTP uploading for ").append(key).append(" ended");
                                    } catch (Exception e) {
                                        msg.append("FTP uploading for ").append(key).append(" failed");
                                        log.info(msg.toString() + " on " + new Date(), e);
                                        history.setStatus("FTP Failed");
                                        failed = true;
                                    } finally {
                                        updateProgressBar(msg.toString(), percentage);
                                    }

                                    if (!failed) {
                                        msg.delete(0, msg.length());
                                        percentage += 1;
                                        msg.append("Marking payments as sent for ").append(key).append(" started");
                                        updateProgressBar(msg.toString(), percentage);
                                        msg.delete(0, msg.length());
                                        percentage += 1;
                                        try {
                                            achSetUpDAO.updateAchPayments(bank.getBankName(), bank.getBankAcctNo(), achBatchSequence, STATUS_SENT);
                                            history.setStatus("Completed");
                                            history.setEndTime(new Date());
                                            msg.append("Marking payments as sent for ").append(key).append(" ended");
                                        } catch (Exception e) {
                                            msg.append("Marking payments as sent for ").append(key).append(" failed");
                                            log.info(msg.toString() + " on " + new Date(), e);
                                            history.setStatus("Sent Status Failed");
                                        } finally {
                                            updateProgressBar(msg.toString(), percentage);
                                        }
                                    }
                                }

                                msg.delete(0, msg.length());
                                percentage += 1;
                                msg.append("Processing payments for ").append(key).append(" ended");
                                updateProgressBar(msg.toString(), percentage);

                                history.setBankDetails(bank);
                                achProcessDAO.insertAchProcessHistory(history);
                            }
                        }

                        if (noPayments) {
                            percentage = 99;
                            msg.delete(0, msg.length());
                            msg.append("No ACH Payments available");
                            updateProgressBar(msg.toString(), percentage);
                        }
                    } else {
                        percentage = 99;
                        msg.delete(0, msg.length());
                        msg.append("No Bank accounts setup with ACH");
                        updateProgressBar(msg.toString(), percentage);
                    }
                }
            } catch (Exception e) {
                log.info("ACH Job failed again on " + new Date(), e);
                failed = true;
            } finally {
                msg.delete(0, msg.length());
                percentage = 100;
                msg.append("ACH Job ").append(failed ? "failed" : "ended");
                updateProgressBar(msg.toString(), failed ? "failed" : "ended", percentage);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    log.info("ACH Job failed again on " + new Date(), ex);
                }
            }
        }
    }

    public static void manualRun() throws Exception {
        try {
            ServletContext servletContext = JobScheduler.servletContext;
            ProgressBar progressBar = (ProgressBar) servletContext.getAttribute("progressBar");
            if (null == progressBar) {
                String id = (String) servletContext.getAttribute("achId");
                run(CommonUtils.isNotEmpty(id), id);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private static void autoRun() throws Exception {
        ServletContext servletContext = JobScheduler.servletContext;
        try {
            ProgressBar progressBar = (ProgressBar) servletContext.getAttribute("progressBar");
            if (null != progressBar) {
                if (CommonUtils.in(progressBar.getStatus(), "ended", "failed")) {
                    servletContext.removeAttribute("progressBar");
                    run(true, null);
                }
            } else {
                run(true, null);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        Transaction transaction = null;
        try {
            log.info("ACH Job started on " + new Date());
            transaction = dao.getCurrentSession().getTransaction();
            if (!transaction.isActive()) {
                transaction.begin();
            }
            Job job = dao.findByClassName(AchJob.class.getCanonicalName());
            job.setStartTime(new Date());
            autoRun();
            job.setEndTime(new Date());
            transaction.commit();
            log.info("ACH Job ended on " + new Date());
        } catch (Exception e) {
            log.info("ACH Job failed on " + new Date(), e);
            try {
                Thread.sleep(5000);
                log.info("ACH Job restarted on " + new Date());
                if (null == transaction || !transaction.isActive()) {
                    transaction = dao.getCurrentSession().getTransaction();
                    transaction.begin();
                } else {
                    transaction = dao.getCurrentSession().getTransaction();
                }
                Job job = dao.findByClassName(AchJob.class.getCanonicalName());
                job.setStartTime(new Date());
                autoRun();
                job.setEndTime(new Date());
                transaction.commit();
                log.info("ACH Job ended on " + new Date());
            } catch (Exception ex) {
                log.info("ACH Job failed again on " + new Date(), ex);
                if (null != transaction && transaction.isActive() && dao.getCurrentSession().isConnected() && dao.getCurrentSession().isOpen()) {
                    transaction.rollback();
                }
            } finally {
                HibernateSessionFactory.closeSession();
            }
        }
    }
}
