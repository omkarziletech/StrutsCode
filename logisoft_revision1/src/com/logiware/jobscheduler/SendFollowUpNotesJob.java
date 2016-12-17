package com.logiware.jobscheduler;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.domain.Notes;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cong.logisoft.hibernate.dao.NotesDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Transaction;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

/**
 *
 * @author Lakshminarayanan
 */
public class SendFollowUpNotesJob extends BaseHibernateDAO implements Job, Serializable {

    private static final Logger log = Logger.getLogger(SendFollowUpNotesJob.class);
    private static final long serialVersionUID = -9140422688655077762L;

    public void execute(JobExecutionContext jobExecutionContext) {
        try {
            log.info("Sending FollowUp Notes to Users started on " + new Date());
            Transaction hibernateTransaction = getCurrentSession().beginTransaction();
            UserDAO userDAO = new UserDAO();
            List<Notes> followUpNotes = new NotesDAO().getFollowUpNotes();
            EmailschedulerDAO emailschedulerDAO = new EmailschedulerDAO();
            boolean canAdd = false;
            int index = 0;
            StringBuilder htmlMessageHeader = new StringBuilder("<html>");
            htmlMessageHeader.append("<body>");
            htmlMessageHeader.append("<div>");
            htmlMessageHeader.append("<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\" style=\"border:1px solid black\">");
            htmlMessageHeader.append("<thead>");
            htmlMessageHeader.append("<tr style=\"background-color:#8DB7D6\">");
           // htmlMessageHeader.append("<th style=\"width:50%;border:1px solid black;word-wrap: break-word\">Notes Description</th>");
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
            for (Notes notes : followUpNotes) {
                if ((index + 1) < followUpNotes.size()) {
                    Notes nextNotes = (Notes) followUpNotes.get(index + 1);
                    if (CommonUtils.isEqualIgnoreCase(notes.getUpdatedBy(), nextNotes.getUpdatedBy())) {
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
                //htmlMessageBody.append("<td style=\"width:50%;border:1px solid black;word-wrap: break-word\">").append(notes.getNoteDesc()).append("</td>");
                htmlMessageBody.append("<td style=\"width:35%;border:1px solid black;word-wrap: break-word\">").append(DateUtils.formatDate(notes.getFollowupDate(), "MM/dd/yyyy")).append("</td>");
                htmlMessageBody.append("<td style=\"width:30%;border:1px solid black;word-wrap: break-word\">").append("04-").append(notes.getModuleRefId()).append("</td>");
                htmlMessageBody.append("<td style=\"width:35%;border:1px solid black;word-wrap: break-word\">").append(notes.getModuleId()).append("</td>");
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
                 htmlMessageBody.append("<td  colspan=\"3\"style=\"width:50%;border:1px solid black;word-wrap: break-word\">").append(notes.getNoteDesc()).append("</td>");
                 htmlMessageBody.append("</tr>");
                textMessage.append("\n").append(notes.getNoteDesc()).append("\t").append(DateUtils.formatDate(notes.getFollowupDate(), "MM/dd/yyyy"));
                if (canAdd) {
                    User user = userDAO.findUserName(notes.getUpdatedBy());
                    if (null != user) {
                        EmailSchedulerVO emailSchedulerVO = new EmailSchedulerVO();
                        String htmlMessage = htmlMessageHeader.toString() + htmlMessageBody.toString() + htmlMessageFooter.toString();
                        emailSchedulerVO.setEmailData(user.getFirstName(), user.getEmail(), user.getFirstName(), user.getEmail(), null, null, "FollowUp Notes", htmlMessage);
                        emailSchedulerVO.setEmailInfo("FollowUpNotes", null, CommonConstants.CONTACT_MODE_EMAIL, 0, new Date(), "FollowUp Notes", "FollowUp Notes", user.getLoginName());
                        emailSchedulerVO.setStatus(CommonConstants.EMAIL_STATUS_PENDING);
                        emailSchedulerVO.setTextMessage(textMessage.toString());
                        emailschedulerDAO.save(emailSchedulerVO);
                    }
                    canAdd = false;
                    htmlMessageBody = new StringBuilder();
                    textMessage = new StringBuilder("Notes Description\tFollowUp Date");
                }
                index++;
            }
            hibernateTransaction.commit();
        } catch (Exception e) {
            log.info("Sending FollowUp Notes to Users failed on " + new Date() + " :\n ",e);
        }
    }
}
