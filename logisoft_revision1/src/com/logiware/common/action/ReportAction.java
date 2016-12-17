package com.logiware.common.action;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.domain.User;
import com.logiware.common.dao.ReportDAO;
import com.logiware.common.domain.Report;
import com.logiware.common.form.ReportForm;
import com.logiware.common.job.JobScheduler;
import com.logiware.common.reports.CobExcelCreator;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Lakshmi Narayanan
 */
public class ReportAction extends BaseAction {

    private static final String COB_REPORT = "cobReport";

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ReportForm reportForm = (ReportForm) form;
        new ReportDAO().search(reportForm);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward clear(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("reportForm", new ReportForm());
        return mapping.findForward(SUCCESS);
    }

    public ActionForward validateQueries(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String query1 = request.getParameter("query1");
        String query2 = request.getParameter("query2");
        PrintWriter out = response.getWriter();
        out.print(new ReportDAO().validateQueries(query1, query2));
        return null;
    }

    public ActionForward saveOrUpdate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ReportForm reportForm = (ReportForm) form;
        Report report = reportForm.getReport();
        User user = (User) request.getSession().getAttribute("loginuser");
        boolean isNewReport = false;
        if (CommonUtils.isNotEmpty(report.getId())) {
            report.setUpdatedBy(user.getLoginName());
            report.setUpdatedDate(new Date());
            request.setAttribute("message", report.getReportName() + " updated successfully");
        } else {
            isNewReport = true;
            report.setCreatedBy(user.getLoginName());
            report.setCreatedDate(new Date());
            request.setAttribute("message", report.getReportName() + " saved successfully");
        }
        new ReportDAO().saveOrUpdate(report);
        if (isNewReport) {
            if (report.isEnabled()) {
                new JobScheduler().scheduleJob(report);
            }
        } else {
            if (report.isEnabled()) {
                new JobScheduler().rescheduleJob(report);
            } else {
                new JobScheduler().deleteJob(report);
            }
        }
        return search(mapping, reportForm, request, response);
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ReportForm reportForm = (ReportForm) form;
        ReportDAO reportDAO = new ReportDAO();
        Report report = reportDAO.findById(reportForm.getReport().getId());
        request.setAttribute("message", report.getReportName() + " deleted successfully");
        reportDAO.delete(report);
        return search(mapping, reportForm, request, response);
    }

    public ActionForward previewReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String contextPath = this.servlet.getServletContext().getRealPath("/");
        ReportForm reportForm = (ReportForm) form;
        new ReportDAO().preview(reportForm, contextPath);
        PrintWriter out = response.getWriter();
        out.print(reportForm.getFileName());
        return null;
    }

    public ActionForward gotoCobReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ReportForm reportForm = (ReportForm) form;
        Calendar cal = Calendar.getInstance();
            if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
            cal.add(Calendar.DATE, -3);
            reportForm.setFromDate(DateUtils.formatDate(cal.getTime(), "MM/dd/yyyy"));
            cal.add(Calendar.DATE, 2);
            reportForm.setToDate(DateUtils.formatDate(cal.getTime(), "MM/dd/yyyy"));
        } else {
            cal.add(Calendar.DATE, -1);
            reportForm.setFromDate(DateUtils.formatDate(cal.getTime(), "MM/dd/yyyy"));
            reportForm.setToDate(DateUtils.formatDate(cal.getTime(), "MM/dd/yyyy"));
        }
        return mapping.findForward(COB_REPORT);
    }

    public ActionForward exportCobReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ReportForm reportForm = (ReportForm) form;
        String fileName = new CobExcelCreator(reportForm).create();
        PrintWriter out = response.getWriter();
        out.print(fileName);
        return null;
    }
}
