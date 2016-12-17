package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cvst.logisoft.struts.form.lcl.LclOutsourceForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.struts.LoadLogisoftProperties;
import javax.servlet.http.HttpSession;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.lcl.LclRemarks;
import com.gp.cong.logisoft.domain.lcl.LclUnit;
import com.gp.cong.logisoft.domain.lcl.LclUnitSsRemarks;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import java.util.Date;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsRemarksDAO;

public class LclOutsourceAction extends LogiwareDispatchAction {

    private static final String REMARKS_TYPE = "Outsource";

    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclOutsourceForm lclOutsourceForm = (LclOutsourceForm) form;
        LclRemarks lclRemarks = null;
        request.setAttribute("fileId", lclOutsourceForm.getFileId());
        request.setAttribute("fileNumber", lclOutsourceForm.getFileNumber());
        if (CommonUtils.isNotEmpty(lclOutsourceForm.getFileId())) {
            lclRemarks = new LclRemarksDAO().executeUniqueQuery("from LclRemarks where lclFileNumber.id=" + lclOutsourceForm.getFileId() + " AND type='Outsource'");
        }
        if (lclRemarks != null) {
            lclOutsourceForm.setMessage(lclRemarks.getRemarks());
            request.setAttribute("flag", true);
        }
        lclOutsourceForm.setMailTo(lclOutsourceForm.getEmailId());
        request.setAttribute("lclOutsourceForm", lclOutsourceForm);
        return mapping.findForward("success");
    }

    public ActionForward sendEmailDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclOutsourceForm lclOutsourceForm = (LclOutsourceForm) form;
        HttpSession session = ((HttpServletRequest) request).getSession();
        User user = (User) session.getAttribute("loginuser");
        User user1 = new UserDAO().findById(user.getUserId());

        String fromName = user1.getFirstName();
        String fromAddress = user1.getEmail();
        String subject = LoadLogisoftProperties.getProperty("outsource.email.subject");
        EmailSchedulerVO emailSchedulerVO = new EmailSchedulerVO();
        emailSchedulerVO.setFileLocation("");
        emailSchedulerVO.setName("LCL Booking");
        emailSchedulerVO.setSubject(subject + "-" + lclOutsourceForm.getFileNumber());
        emailSchedulerVO.setType("Email");
        emailSchedulerVO.setStatus("Pending");
        emailSchedulerVO.setToAddress(user1.getOutsourceEmail());
        emailSchedulerVO.setFromName(fromName);
        emailSchedulerVO.setFromAddress(fromAddress);
        emailSchedulerVO.setEmailDate(new Date());
        emailSchedulerVO.setTextMessage(lclOutsourceForm.getMessage().toUpperCase());
        emailSchedulerVO.setModuleName("LCL Booking");
        emailSchedulerVO.setUserName(user1.getLoginName());
        emailSchedulerVO.setModuleId(lclOutsourceForm.getFileNumber());
        emailSchedulerVO.setHtmlMessage(lclOutsourceForm.getMessage().toUpperCase());
        new EmailschedulerDAO().saveOrUpdate(emailSchedulerVO);
        if (CommonUtils.isNotEmpty(lclOutsourceForm.getMessage())) {
            String remarks = "Outsource-" + lclOutsourceForm.getMessage().toUpperCase();
            if (CommonUtils.isNotEmpty(lclOutsourceForm.getUnitId())) {
                LclUnitSsRemarksDAO remarksDao = new LclUnitSsRemarksDAO();
                remarksDao.insertLclunitRemarks(lclOutsourceForm.getSsHeaderId(), lclOutsourceForm.getUnitId(),
                        REMARKS_TYPE, remarks, user1.getUserId());
            } else {
                LclRemarks lclRemarks = new LclRemarks();
                lclRemarks.setRemarks(remarks);
                lclRemarks.setType(REMARKS_TYPE);
                lclRemarks.setLclFileNumber(new LclFileNumberDAO().findById(Long.parseLong(lclOutsourceForm.getFileId())));
                lclRemarks.setEnteredBy(getCurrentUser(request));
                lclRemarks.setModifiedBy(getCurrentUser(request));
                lclRemarks.setFollowupDate(new Date());
                lclRemarks.setModifiedDatetime(new Date());
                lclRemarks.setEnteredDatetime(new Date());
                lclRemarks.setFollowupEmail(user1.getOutsourceEmail());
                new LclRemarksDAO().save(lclRemarks);

            }
        }
        return mapping.findForward("success");
    }

    public ActionForward openOutsourceEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclOutsourceForm outsourceForm = (LclOutsourceForm) form;
        LclUnitDAO unitDao = new LclUnitDAO();
        LclUnitSsRemarksDAO remarksDao = new LclUnitSsRemarksDAO();
        LclUnitSsRemarks remarks = remarksDao.getRemarks(outsourceForm.getSsHeaderId(), outsourceForm.getUnitId(), REMARKS_TYPE);
        LclUnit unit = unitDao.findById(outsourceForm.getUnitId());
        if (null != remarks) {
            outsourceForm.setMessage(remarks.getRemarks());
            request.setAttribute("flag", true);
        }
        request.setAttribute("unitSourceEmail", true); /* outsource is unit level */
        request.setAttribute("fileNumber", unit.getUnitNo());
        outsourceForm.setMailTo(outsourceForm.getEmailId());
        return mapping.findForward("success");
    }
}
