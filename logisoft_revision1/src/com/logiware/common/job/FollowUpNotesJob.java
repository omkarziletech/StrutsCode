package com.logiware.common.job;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import static com.gp.cong.common.ConstantsInterface.CONTACT_MODE_EMAIL;
import static com.gp.cong.common.ConstantsInterface.EMAIL_STATUS_PENDING;
import com.gp.cong.common.DateUtils;
import com.gp.cong.hibernate.HibernateSessionFactory;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.domain.Notes;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclRemarks;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cong.logisoft.hibernate.dao.NotesDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO;
import com.logiware.common.dao.JobDAO;
import com.logiware.common.domain.Job;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
public class FollowUpNotesJob implements org.quartz.Job, ConstantsInterface {

    private static final Logger log = Logger.getLogger(FollowUpNotesJob.class);

    public void run() throws Exception {
        try {
            UserDAO userDAO = new UserDAO();
            List<Notes> followUpNotes = new NotesDAO().getFollowUpNotes();
            EmailschedulerDAO emailschedulerDAO = new EmailschedulerDAO();
            boolean canAdd;
            int index = 0;
            StringBuilder htmlMessageHeader = new StringBuilder("<html>");
            htmlMessageHeader.append("<body>");
            htmlMessageHeader.append("<div>");
            htmlMessageHeader.append("<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\" style=\"border:1px solid black\">");
            htmlMessageHeader.append("<thead>");
            htmlMessageHeader.append("<tr style=\"background-color:#8DB7D6\">");
            htmlMessageHeader.append("<th style=\"width:35%;border:1px solid black;word-wrap: break-word\">FollowUp Date</th>");
            htmlMessageHeader.append("<th style=\"width:30%;border:1px solid black;word-wrap: break-word\">Module Id</th>");
            htmlMessageHeader.append("<th style=\"width:35%;border:1px solid black;word-wrap: break-word\">Module Name</th>");
            htmlMessageHeader.append("</tr>");
            htmlMessageHeader.append("</thead>");
            htmlMessageHeader.append("<tbody>");
            StringBuilder htmlMessageBody = new StringBuilder();
            StringBuilder htmlMessageFooter = new StringBuilder("</tbody>");
            htmlMessageFooter.append("</table>");
            htmlMessageFooter.append("</div>");
            htmlMessageFooter.append("</body>");
            htmlMessageFooter.append("</html>");
            StringBuilder textMessage = new StringBuilder("Notes Description\tFollowUp Date");
            boolean isOdd = true;
            for (Notes note : followUpNotes) {
                if ((index + 1) < followUpNotes.size()) {
                    Notes nextNotes = (Notes) followUpNotes.get(index + 1);
                    if (CommonUtils.isEqualIgnoreCase(note.getUpdatedBy(), nextNotes.getUpdatedBy())) {
                        canAdd = false;
                    } else {
                        canAdd = true;
                    }
                } else {
                    canAdd = true;
                }
                htmlMessageBody.append("<tr style=\"background-color:");
                if (isOdd) {
                    htmlMessageBody.append("#D1DBE9");
                    isOdd = false;
                } else {
                    htmlMessageBody.append("#ffffff");
                    isOdd = true;
                }
                htmlMessageBody.append("\">");
                htmlMessageBody.append("<td style=\"width:35%;border:1px solid black;word-wrap: break-word\">");
                htmlMessageBody.append(DateUtils.formatDate(note.getFollowupDate(), "MM/dd/yyyy"));
                htmlMessageBody.append("</td>");
                htmlMessageBody.append("<td style=\"width:30%;border:1px solid black;word-wrap: break-word\">");
                htmlMessageBody.append("04-").append(note.getModuleRefId());
                htmlMessageBody.append("</td>");
                htmlMessageBody.append("<td style=\"width:35%;border:1px solid black;word-wrap: break-word\">");
                htmlMessageBody.append(note.getModuleId());
                htmlMessageBody.append("</td>");
                htmlMessageBody.append("</tr>");
                htmlMessageBody.append("<tr style=\"background-color:");
                if (isOdd) {
                    htmlMessageBody.append("#D1DBE9");
                    isOdd = false;
                } else {
                    htmlMessageBody.append("#ffffff");
                    isOdd = true;
                }
                htmlMessageBody.append("\">");
                htmlMessageBody.append("<td  colspan=\"3\"style=\"width:50%;border:1px solid black;word-wrap: break-word\">");
                htmlMessageBody.append(note.getNoteDesc());
                htmlMessageBody.append("</td>");
                htmlMessageBody.append("</tr>");
                textMessage.append("\n").append(note.getNoteDesc()).append("\t").append(DateUtils.formatDate(note.getFollowupDate(), "MM/dd/yyyy"));
                if (canAdd) {
                    User user = userDAO.findUserName(note.getUpdatedBy());
                    if (null != user) {
                        EmailSchedulerVO email = new EmailSchedulerVO();
                        String htmlMessage = htmlMessageHeader.toString() + htmlMessageBody.toString() + htmlMessageFooter.toString();
                        email.setToName(user.getFirstName());
                        email.setToAddress(user.getEmail());
                        email.setFromName(user.getFirstName());
                        email.setFromAddress(user.getEmail());
                        email.setSubject("FollowUp Notes");
                        email.setHtmlMessage(htmlMessage);
                        email.setTextMessage(textMessage.toString());
                        email.setName("FollowUpNotes");
                        email.setType(CONTACT_MODE_EMAIL);
                        email.setStatus(EMAIL_STATUS_PENDING);
                        email.setNoOfTries(0);
                        email.setEmailDate(new Date());
                        email.setModuleName("FollowUp Notes");
                        email.setModuleId("FollowUpNotes" + note.getId());
                        email.setUserName(user.getLoginName());
                        emailschedulerDAO.save(email);
                    }
                    htmlMessageBody.delete(0, htmlMessageBody.length());
                    textMessage.delete(0, textMessage.length());
                    textMessage.append("Notes Description\tFollowUp Date");
                }
                index++;
            }
            setLclFollowUpNotes();
        } catch (Exception e) {
            throw e;
        }
    }

    public void setLclFollowUpNotes() throws Exception {
        String startDate = DateUtils.formatDate(new Date(), "yyyy-MM-dd 00:00:00");
        String endDate = DateUtils.formatDate(new Date(), "yyyy-MM-dd 23:59:59");
        List<LclRemarks> lclRemarksList = new LclRemarksDAO().getFollowUpNotes(startDate, endDate);
        if (lclRemarksList != null && !lclRemarksList.isEmpty()) {
            EmailschedulerDAO emailschedulerDAO = new EmailschedulerDAO();
            StringBuilder htmlMessageHeader = new StringBuilder("<html>");
            htmlMessageHeader.append("<body>");
            htmlMessageHeader.append("<div>");
            htmlMessageHeader.append("<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\" style=\"border:1px solid black\">");
            htmlMessageHeader.append("<thead>");
            htmlMessageHeader.append("<tr style=\"background-color:#8DB7D6\">");
            htmlMessageHeader.append("<th style=\"width:35%;border:1px solid black;word-wrap: break-word\">FollowUp Date</th>");
            htmlMessageHeader.append("<th style=\"width:30%;border:1px solid black;word-wrap: break-word\">Module Id</th>");
            htmlMessageHeader.append("<th style=\"width:35%;border:1px solid black;word-wrap: break-word\">Module Name</th>");
            htmlMessageHeader.append("</tr>");
            htmlMessageHeader.append("</thead>");
            htmlMessageHeader.append("<tbody>");
            StringBuilder htmlMessageFooter = new StringBuilder("</tbody>");
            htmlMessageFooter.append("</table>");
            htmlMessageFooter.append("</div>");
            htmlMessageFooter.append("</body>");
            htmlMessageFooter.append("</html>");
            List<LclRemarks> remarksList = null;
            Map<String, List<LclRemarks>> lclRemarksMap = new LinkedHashMap();
            for (LclRemarks lclRemarks : lclRemarksList) {
                String[] followUpEmail = lclRemarks.getFollowupEmail().split(",");
                String maillist=null;
                for (String email : followUpEmail) {
                    maillist = email;
                    if (CommonUtils.isNotEmpty(maillist)) {
                        if (lclRemarksMap.containsKey(email)) {
                            remarksList = lclRemarksMap.get(email);
                        } else {
                            remarksList = new ArrayList<LclRemarks>();
                        }
                        remarksList.add(lclRemarks);
                        lclRemarksMap.put(email, remarksList);
                    }
                }
            }
            for (Map.Entry<String, List<LclRemarks>> entry : lclRemarksMap.entrySet()) {
                StringBuilder htmlMessageBody = new StringBuilder();
                StringBuilder textMessage = new StringBuilder();
                StringBuilder remarks = new StringBuilder();
                String firstName = "";
                String fromEmail = "";
                String email = entry.getKey();
                boolean flag = false;
                remarksList = lclRemarksMap.get(email);
                for (LclRemarks lclRemarks : remarksList) {
                    firstName = lclRemarks.getVoidSymbol();
                    fromEmail = lclRemarks.getEmailId();
                    htmlMessageBody.append("<tr style=\"background-color:#D1DBE9\">");
                    htmlMessageBody.append("<td style=\"width:35%;border:1px solid black;word-wrap: break-word\">").append(DateUtils.formatDate(lclRemarks.getFollowupDate(), "dd-MMM-yyyy")).append("</td>");
                    htmlMessageBody.append("<td style=\"width:30%;border:1px solid black;word-wrap: break-word\">").append(lclRemarks.getNoteSymbol()).append("</td>");
                    htmlMessageBody.append("<td style=\"width:35%;border:1px solid black;word-wrap: break-word\">").append(lclRemarks.getUserName()).append("</td>");
                    htmlMessageBody.append("</tr>");
                    htmlMessageBody.append("<tr style=\"background-color:#ffffff\">");
                    htmlMessageBody.append("<td colspan=\"3\" style=\"width:35%;border:1px solid black;word-wrap: break-word\">").append(lclRemarks.getRemarks()).append("</td>");
                    htmlMessageBody.append("</tr>");
                    textMessage.append("\n").append(lclRemarks.getRemarks()).append("\t").append(DateUtils.formatDate(lclRemarks.getFollowupDate(), "dd-MMM-yyyy"));
                    remarks.append(lclRemarks.getRemarks());
                    flag = true;
                }
                if (flag) {
                    EmailSchedulerVO emailSchedulerVO = new EmailSchedulerVO();
                    String htmlMessage = htmlMessageHeader.toString() + htmlMessageBody.toString() + htmlMessageFooter.toString();
                    emailSchedulerVO.setToName(firstName);
                    emailSchedulerVO.setToAddress(email);
                    emailSchedulerVO.setFromName(firstName);
                    emailSchedulerVO.setFromAddress(fromEmail);
                    emailSchedulerVO.setSubject("FollowUp Notes");
                    emailSchedulerVO.setHtmlMessage(htmlMessage);
                    emailSchedulerVO.setTextMessage(textMessage.toString());
                    emailSchedulerVO.setName("FollowUpNotes");
                    emailSchedulerVO.setType(CONTACT_MODE_EMAIL);
                    emailSchedulerVO.setStatus(EMAIL_STATUS_PENDING);
                    emailSchedulerVO.setNoOfTries(0);
                    emailSchedulerVO.setEmailDate(new Date());
                    emailSchedulerVO.setModuleName("FollowUp Notes");
                    emailSchedulerVO.setModuleId("FollowUpNotes");
                    emailSchedulerVO.setUserName(firstName);
                    emailschedulerDAO.save(emailSchedulerVO);
                }
            }
        }
    }

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        JobDAO dao = new JobDAO();
        Transaction transaction = null;
        try {
            log.info("FollowUp Notes Job started on " + new Date());
            transaction = dao.getCurrentSession().getTransaction();
            if (!transaction.isActive()) {
                transaction.begin();
            }
            Job job = dao.findByClassName(FollowUpNotesJob.class.getCanonicalName());
            job.setStartTime(new Date());
            run();
            job.setEndTime(new Date());
            transaction.commit();
            log.info("FollowUp Notes Job ended on " + new Date());
        } catch (Exception e) {
            log.info("FollowUp Notes Job failed on " + new Date(), e);
            try {
                Thread.sleep(5000);
                log.info("FollowUp Notes Job restarted on " + new Date());
                if (null == transaction || !transaction.isActive()) {
                    transaction = dao.getCurrentSession().getTransaction();
                    transaction.begin();
                } else {
                    transaction = dao.getCurrentSession().getTransaction();
                }
                Job job = dao.findByClassName(FollowUpNotesJob.class.getCanonicalName());
                job.setStartTime(new Date());
                run();
                job.setEndTime(new Date());
                transaction.commit();
                log.info("FollowUp Notes Job ended on " + new Date());
            } catch (Exception ex) {
                log.info("FollowUp Notes Job failed again on " + new Date(), ex);
                if (null != transaction && transaction.isActive() && dao.getCurrentSession().isConnected() && dao.getCurrentSession().isOpen()) {
                    transaction.rollback();
                }
            } finally {
                HibernateSessionFactory.closeSession();
            }
        }
    }
}
