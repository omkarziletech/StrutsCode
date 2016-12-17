/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action;

import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cong.logisoft.jobscheduler.EMailScheduler;
import com.gp.cong.logisoft.struts.form.EmailSchedulerForm;
import com.gp.cvst.logisoft.beans.MailMessageVO;
import com.logiware.common.action.BaseAction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author meiyazhakan.r
 */
public class EmailSchedulersAction extends BaseAction {

    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        EmailSchedulerForm emailSchedulerForm = (EmailSchedulerForm) form;
        emailSchedulerForm.setStatus("Completed");
        emailSchedulerForm.setLimit("250");
        EmailschedulerDAO emailSchedulerDAO = new EmailschedulerDAO();
        request.setAttribute("emailSchedulerList", emailSchedulerDAO.searchEmailSchedular(emailSchedulerForm));
        request.setAttribute("emailSchedulerForm", emailSchedulerForm);
        return mapping.findForward("success");
    }

    public ActionForward searchMail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        EmailSchedulerForm emailSchedulerForm = (EmailSchedulerForm) form;
        EmailschedulerDAO emailSchedulerDAO = new EmailschedulerDAO();
        request.setAttribute("emailSchedulerList", emailSchedulerDAO.searchEmailSchedular(emailSchedulerForm));
        request.setAttribute("emailSchedulerForm", emailSchedulerForm);
        return mapping.findForward("success");
    }

    public ActionForward sendEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        EmailSchedulerForm emailSchedulerForm = (EmailSchedulerForm) form;
        EmailschedulerDAO emailSchedulerDAO = new EmailschedulerDAO();
        EMailScheduler scheduler = new EMailScheduler();
        MailMessageVO mailMessageVO = null;
        String emailCheck[] = emailSchedulerForm.getEmailCheck().split(",");
        if (emailCheck != null) {
            for (int i = 0; i < emailCheck.length; i++) {
                EmailSchedulerVO emailScheduler = emailSchedulerDAO.findById(Integer.parseInt(emailCheck[i]));
                if (emailScheduler != null) {
                    mailMessageVO = new MailMessageVO(emailScheduler.getToName(), emailScheduler.getToAddress(), emailScheduler.getFromName(), emailScheduler.getFromAddress(),
                            emailScheduler.getCcAddress(), emailScheduler.getBccAddress(), emailScheduler.getSubject(), emailScheduler.getHtmlMessage());
                    scheduler.createHtmlEmail(null, emailScheduler.getFileLocation(), mailMessageVO, emailScheduler.getName());
                }
            }
        }
        request.setAttribute("emailSchedulerList", emailSchedulerDAO.searchEmailSchedular(emailSchedulerForm));
        request.setAttribute("emailSchedulerForm", emailSchedulerForm);
        return mapping.findForward("success");
    }

    public ActionForward viewEmailTranscations(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        EmailSchedulerForm emailSchedulerForm = (EmailSchedulerForm) form;
        EmailschedulerDAO emailschedulerDAO = new EmailschedulerDAO();
        EmailSchedulerVO emailSchedulerVO = emailschedulerDAO.findById(Integer.parseInt(emailSchedulerForm.getEmailId()));
        if (emailSchedulerVO.getFileLocation().contains(";")) {
            String[] multiFileLocation = emailSchedulerVO.getFileLocation().split(";");
            request.setAttribute("multiFileLocation", multiFileLocation);
        }
        request.setAttribute("emailSchedulerVO", emailSchedulerVO);
        return mapping.findForward("viewEmailTranscations");
    }
}
