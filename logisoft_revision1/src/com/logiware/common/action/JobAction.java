package com.logiware.common.action;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.domain.User;
import com.logiware.common.constants.Frequency;
import com.logiware.common.dao.JobDAO;
import com.logiware.common.domain.Job;
import com.logiware.common.form.JobForm;
import com.logiware.common.job.AchJob;
import com.logiware.common.job.JobScheduler;
import com.logiware.common.model.ProgressBar;
import com.logiware.hibernate.dao.AchProcessDAO;
import com.logiware.hibernate.domain.AchProcessHistory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Lakshmi Narayanan
 */
public class JobAction extends BaseAction {

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        JobForm jobForm = (JobForm) form;
        User user = (User) request.getSession().getAttribute("loginuser");
        Job job = new JobDAO().findById(jobForm.getId());
        job.setFrequency(Frequency.fromString(jobForm.getFrequency()));
        job.setDay1(jobForm.getDay1());
        job.setDay2(jobForm.getDay2());
        job.setHour(jobForm.getHour());
        job.setMinute(jobForm.getMinute());
        job.setEnabled(jobForm.isEnabled());
        job.setUpdatedBy(user.getLoginName());
        job.setUpdatedDate(new Date());
        if (job.isEnabled()) {
            new JobScheduler().rescheduleJob(job);
        } else {
            new JobScheduler().deleteJob(job);
        }
        request.setAttribute("message", job.getName() + " job is updated successfully");
        return mapping.findForward(SUCCESS);
    }

    public ActionForward run(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = null;
        try {
            String submittedDate = request.getParameter("date");
            HttpSession session = request.getSession();
            if (CommonUtils.isNotEqual(submittedDate, (String) session.getAttribute("submittedDate"))) {
                session.setAttribute("submittedDate", submittedDate);
                out = response.getWriter();
                JobForm jobForm = (JobForm) form;
                Job job = new JobDAO().findById(jobForm.getId());
                job.setStartTime(new Date());
                Class clasz = Class.forName(job.getClassName());
                Class noparams[] = {};
                Method method = clasz.getDeclaredMethod(CommonUtils.isEqual(job.getName(), "ACH Transaction") ? "manualRun" : "run", noparams);
                method.invoke(clasz.newInstance());
                job.setEndTime(new Date());
                StringBuilder result = new StringBuilder();
                result.append(DateUtils.formatDate(job.getStartTime(), "MM/dd/yyyy hh:mm:ss a")).append("===");
                result.append(DateUtils.formatDate(job.getEndTime(), "MM/dd/yyyy hh:mm:ss a")).append("===");
                result.append(job.getName()).append(" job is executed successfully");
                out.print(result);
            }
            return null;
        } catch (Exception e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    public ActionForward rerun(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = null;
        try {
            out = response.getWriter();
            Job job = new JobDAO().findByClassName(AchJob.class.getCanonicalName());
            job.setStartTime(new Date());
            String achId = request.getParameter("achId");
            JobScheduler.servletContext.setAttribute("achId", achId);
            AchJob.manualRun();
            job.setEndTime(new Date());
            AchProcessDAO achProcessDAO = new AchProcessDAO();
            achProcessDAO.getCurrentSession().flush();
            AchProcessHistory history = achProcessDAO.findAchProcessHistoryById(Integer.parseInt(achId));
            JobScheduler.servletContext.removeAttribute("achId");
            if (CommonUtils.in(history.getStatus(), "Encryption Failed", "FTP Failed")) {
                out.print(history.getStatus());
            } else {
                out.print("ACH File is sent to the bank successfully");
            }
            out.flush();
            return null;
        } catch (Exception e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    public ActionForward showProgressBar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProgressBar progressBar = new AchJob().getProgressBar();
        request.setAttribute("progressBar", progressBar);
        return mapping.findForward("progressBar");
    }

    public ActionForward updateProgressBar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = null;
        try {
            out = response.getWriter();
            ProgressBar progressBar = new AchJob().getProgressBar();
            if (null != progressBar) {
                out.print(progressBar.getMessage() + "===" + progressBar.getPercentage());
            } else {
                out.print("");
            }
            return null;
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != out) {
                out.flush();
                IOUtils.closeQuietly(out);
            }
        }
    }

    public ActionForward searchAch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<String> properties = new ArrayList<String>();
        List<String> operators = new ArrayList<String>();
        List<Object> values = new ArrayList<Object>();
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String status = request.getParameter("status");
        if (CommonUtils.isNotEmpty(startDate)) {
            properties.add("startTime");
            operators.add(">=");
            values.add(DateUtils.parseDate(startDate, "MM/dd/yyyy"));
        }
        if (CommonUtils.isNotEmpty(endDate)) {
            properties.add("endTime");
            operators.add("<=");
            values.add(DateUtils.parseDate(endDate, "MM/dd/yyyy"));
        }
        if (CommonUtils.isNotEmpty(status)) {
            properties.add("status");
            operators.add("like");
            values.add(status);
        }
        List<AchProcessHistory> achTransactions = new AchProcessDAO().findByProperties(properties, operators, values);
        request.setAttribute("achTransactions", achTransactions);
        return mapping.findForward("achPopup");
    }

    public ActionForward clearAch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("jobForm", new JobForm());
        return mapping.findForward("achPopup");
    }

    public ActionForward showAchFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        InputStream is = null;
        OutputStream os = null;
        try {
            String achId = request.getParameter("achId");
            String type = request.getParameter("type");
            AchProcessHistory history = new AchProcessDAO().findAchProcessHistoryById(Integer.parseInt(achId));
            String fileName = CommonUtils.isEqual(type, "pgp") ? history.getEncryptedFileName() : history.getAchFileName();
            response.addHeader("Content-Disposition", "inline; filename=" + FilenameUtils.getName(fileName));
            response.setContentType("text/plain;charset=utf-8");
            File file = new File(fileName);
            is = new FileInputStream(file);
            os = response.getOutputStream();
            IOUtils.copy(is, os);
            os.flush();
            return null;
        } catch (Exception e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(os);
        }
    }
}
