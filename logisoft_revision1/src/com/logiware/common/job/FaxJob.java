package com.logiware.common.job;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.EmailUtils;
import com.gp.cong.hibernate.HibernateSessionFactory;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.jobscheduler.Fax;
import com.gp.cong.logisoft.lcl.report.LclReportConstants;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.logiware.common.dao.JobDAO;
import com.logiware.common.domain.Job;
import java.io.File;
import java.util.Calendar;
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
public class FaxJob implements org.quartz.Job {

    private static final Logger log = Logger.getLogger(FaxJob.class);
    private static final String nl = System.getProperty("line.separator");

    public void run() throws Exception {
        try {
            EmailschedulerDAO emailschedulerDAO = new EmailschedulerDAO();
            UserDAO userDAO = new UserDAO();
            EmailUtils emailUtils = new EmailUtils();
            SystemRulesDAO systemRulesDAO = new SystemRulesDAO();
            StringBuilder tagFileName = new StringBuilder();
            StringBuilder tagFileContent = new StringBuilder();
            Calendar cal = Calendar.getInstance();
            String tagFileLocation = LoadLogisoftProperties.getProperty("reportLocation");
            tagFileLocation = tagFileLocation + "/Documents/tag_file/" + DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/";
            File dir = new File(tagFileLocation);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String companyCode = systemRulesDAO.getSystemRules("CompanyCode");
            String companyPhone = systemRulesDAO.getSystemRules("CompanyPhone");
            List<EmailSchedulerVO> pendingFaxes = emailschedulerDAO.getPendingFaxes();
            for (EmailSchedulerVO fax : pendingFaxes) {
                if (null != fax.getFileLocation() && fax.getFileLocation().lastIndexOf("/") > -1) {
                    tagFileContent.delete(0, tagFileContent.length());
                    tagFileContent.append("tfn=").append(fax.getToAddress());
                    tagFileContent.append(nl);
                    tagFileContent.append("tnm=").append(CommonUtils.isNotEmpty(fax.getToName()) ? fax.getToName() : "");
                    tagFileContent.append(nl);
                    String pdfFile = fax.getFileLocation().substring(fax.getFileLocation().lastIndexOf("/"), fax.getFileLocation().length());
                    tagFileContent.append("fll=").append(LoadLogisoftProperties.getProperty("faxfile_path")).append(pdfFile);
                    tagFileContent.append(nl);
                    tagFileContent.append("ftp=pdf");
                    tagFileContent.append(nl);
                    tagFileContent.append("fpl=letter");
                    tagFileContent.append(nl);
                    tagFileContent.append("res=fine");
                    tagFileContent.append(nl);
                    tagFileContent.append("pri=m");
                    tagFileContent.append(nl);
                    if (CommonUtils.isEqual(companyCode, "03")) {
                        String userName = fax.getUserName();
                        String terminalNumber = "01";
                        if (CommonUtils.isEqualIgnoreCase(userName, "System")) {
                            if (CommonUtils.isNotEmpty(fax.getModuleId()) && CommonUtils.isNotEmpty(fax.getModuleName())
                                    && CommonUtils.isEqualIgnoreCase(fax.getModuleName(), LclReportConstants.SCREENNAMELCLIMPORTBOOKINGREPORT)) {
                                terminalNumber = new LCLBookingDAO().getTerminalNumberByFileNumber(fax.getModuleId());
                            }
                        } else if (CommonUtils.isNotEmpty(userName) && CommonUtils.isNotEqual(userName, "System")) {
                            User user = userDAO.getUserInfo(userName);
                            if (null != user && null != user.getTerminal() && CommonUtils.isNotEmpty(user.getTerminal().getTrmnum())) {
                                terminalNumber = user.getTerminal().getTrmnum();
                            }
                        }
                        tagFileContent.append("cvr=econo").append(terminalNumber);
                    } else {
                        tagFileContent.append("cvr=oti01");
                    }
                    tagFileContent.append(nl);
                    tagFileContent.append("tco=").append(CommonUtils.isNotEmpty(fax.getCompanyName()) ? fax.getCompanyName() : "");
                    tagFileContent.append(nl);
                    tagFileContent.append("fnm=").append(fax.getFromName());
                    tagFileContent.append(nl);
                    Boolean email = emailUtils.isEmailorFaxChecked(fax.getFromAddress());
                    tagFileContent.append("mad=").append(email ? fax.getFromAddress() : "");
                    tagFileContent.append(nl);
                    tagFileContent.append("ffn=").append(!email ? fax.getFromAddress() : "");
                    tagFileContent.append(nl);
                    tagFileContent.append("sub=").append(fax.getSubject());
                    tagFileContent.append(nl);
                    tagFileContent.append("fvn=").append(companyPhone);
                    tagFileContent.append(nl);
                    tagFileContent.append("ntf=<<EOF\n").append(fax.getTextMessage()).append("\nEOF");
                    cal.setTime(fax.getEmailDate());
                    tagFileName.delete(0, tagFileName.length());
                    tagFileName.append(fax.getUserName()).append("_");
                    tagFileName.append(cal.get(Calendar.YEAR)).append(cal.get(Calendar.MONTH)).append(cal.get(Calendar.DATE)).append("_");
                    tagFileName.append(cal.get(Calendar.HOUR)).append(cal.get(Calendar.MINUTE));
                    tagFileName.append(cal.get(Calendar.SECOND)).append(cal.get(Calendar.MILLISECOND)).append(".txt");
                    Fax.createTagFile(tagFileContent.toString(), tagFileLocation + tagFileName);
                    String responseCode = Fax.sendFax(fax.getFileLocation(), tagFileLocation, tagFileName.toString());
                    if (null == responseCode || responseCode.trim().equals("error")) {
                        emailschedulerDAO.updateStatus(fax.getId(), "Failed", responseCode);
                        log.info("---------------------------Fax Delivery Report----------------------------");
                        log.info("Fax Sending Failed !!!!!");
                        log.info("Fax Sent By User : " + fax.getUserName());
                        log.info("FromName : " + fax.getFromName());
                        log.info("FromAddress : " + fax.getFromAddress());
                        log.info("ToName : " + fax.getToName());
                        log.info("ToAddress : " + fax.getToAddress());
                        log.info("Fax Subject : " + fax.getSubject());
                        log.info("Fax Sent Date : " + fax.getEmailDate());
                        log.info("ModuleId : " + fax.getModuleId());
                        log.info("Current Fax Status Sent Failed !!!");
                    } else {
                        emailschedulerDAO.updateStatus(fax.getId(), "Completed", responseCode);
                        log.info("---------------------------Fax Delivery Report----------------------------");
                        log.info("Fax Sent Successfully !!!!!");
                        log.info("Fax Sent By User : " + fax.getUserName());
                        log.info("FromName : " + fax.getFromName());
                        log.info("FromAddress : " + fax.getFromAddress());
                        log.info("ToName : " + fax.getToName());
                        log.info("ToAddress : " + fax.getToAddress());
                        log.info("Fax Subject : " + fax.getSubject());
                        log.info("Fax Sent Date : " + fax.getEmailDate());
                        log.info("ModuleId : " + fax.getModuleId());
                        log.info("Current Fax Sent Successfully !!!");
                    }
                } else {
                    emailschedulerDAO.updateStatus(fax.getId(), "Failed", "error");
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
            log.info("Fax Job started on " + new Date());
            transaction = new EmailschedulerDAO().getCurrentSession().getTransaction();
            if (!transaction.isActive()) {
                transaction.begin();
            }
            Job job = dao.findByClassName(FaxJob.class.getCanonicalName());
            job.setStartTime(new Date());
            run();
            job.setEndTime(new Date());
            transaction.commit();
            log.info("Fax Job ended on " + new Date());
        } catch (Exception e) {
            log.info("Fax Job failed on " + new Date(), e);
            if (null != transaction && transaction.isActive() && dao.getCurrentSession().isConnected() && dao.getCurrentSession().isOpen()) {
                transaction.rollback();
            }
        } finally {
            HibernateSessionFactory.closeSession();
        }
    }
}
