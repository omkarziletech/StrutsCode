package com.logiware.action;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.logiware.datamigration.LoadAccrualsToLogiware;
import com.logiware.excel.ControlReportExcelCreator;
import com.logiware.form.AccrualMigrationLogForm;
import com.logiware.form.ControlReportForm;
import com.logiware.hibernate.dao.AccrualMigrationLogDAO;
import com.logiware.hibernate.dao.ControlReportDAO;
import com.logiware.hibernate.domain.AccrualMigrationLog;
import com.logiware.reports.ControlReportCreator;
import com.logiware.utils.ExceptionUtils;
import com.oreilly.servlet.ServletUtils;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.hibernate.Criteria;

/**
 *
 * @author Lakshmi Narayanan
 */
public class AccrualMigrationLogAction extends DispatchAction {

    private static String SUCCESS = "success";
    private static String ERROR_FILE = "errorFile";
    private static String REPROCESS_LOGS = "reprocessLogs";

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        AccrualMigrationLogForm accrualMigrationLogForm = (AccrualMigrationLogForm) form;
        AccrualMigrationLogDAO accrualMigrationLogDAO = new AccrualMigrationLogDAO();
        Criteria criteria = accrualMigrationLogDAO.createCriteria(accrualMigrationLogForm);
        int totalNoOfRecords = accrualMigrationLogDAO.getTotalNoOfRecords(criteria);
        int totalPageNo = (totalNoOfRecords / accrualMigrationLogForm.getRecordsLimit()) + (totalNoOfRecords % accrualMigrationLogForm.getRecordsLimit() > 0 ? 1 : 0);
        accrualMigrationLogForm.setTotalPageNo(totalPageNo);
        int start = accrualMigrationLogForm.getRecordsLimit() * (accrualMigrationLogForm.getCurrentPageNo() - 1);
        int end = accrualMigrationLogForm.getRecordsLimit();
        List<AccrualMigrationLog> logs = accrualMigrationLogDAO.getLogs(criteria, accrualMigrationLogForm.getSortBy(), accrualMigrationLogForm.getOrderBy(), start, end);
        accrualMigrationLogForm.setCurrentNoOfRecords(logs.size());
        accrualMigrationLogForm.setTotalNoOfRecords(totalNoOfRecords);
        accrualMigrationLogForm.setLogs(logs);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward clear(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("accrualMigrationLogForm", new AccrualMigrationLogForm());
        return mapping.findForward(SUCCESS);
    }

    public ActionForward showReprocessLogs(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        AccrualMigrationLogForm accrualMigrationLogForm = (AccrualMigrationLogForm) form;
        request.setAttribute("accrualMigrationLog", new AccrualMigrationLogDAO().findById(accrualMigrationLogForm.getId()));
        return mapping.findForward(REPROCESS_LOGS);
    }

    public ActionForward showErrorFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        AccrualMigrationLogForm accrualMigrationLogForm = (AccrualMigrationLogForm) form;
        AccrualMigrationLog accrualMigrationLog = new AccrualMigrationLogDAO().findById(accrualMigrationLogForm.getId());
        request.setAttribute("id", accrualMigrationLog.getId());
        request.setAttribute("fileName", accrualMigrationLog.getFileName());
        request.setAttribute("accrualMigrationErrorFile", accrualMigrationLog.getAccrualMigrationErrorFile());
        return mapping.findForward(ERROR_FILE);
    }

    public ActionForward updateErrorFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        AccrualMigrationLogForm accrualMigrationLogForm = (AccrualMigrationLogForm) form;
        AccrualMigrationLog accrualMigrationLog = new AccrualMigrationLogDAO().findById(accrualMigrationLogForm.getId());
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        accrualMigrationLogForm.getAccrualMigrationErrorFile().setAccrualMigrationLog(accrualMigrationLog);
        accrualMigrationLogForm.getAccrualMigrationErrorFile().setId(accrualMigrationLog.getAccrualMigrationErrorFile().getId());
        PropertyUtils.copyProperties(accrualMigrationLog.getAccrualMigrationErrorFile(), accrualMigrationLogForm.getAccrualMigrationErrorFile());
        accrualMigrationLog.update();
        out.print("success");
        out.flush();
        out.close();
        return null;
    }

    public ActionForward reprocessSingleError(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        AccrualMigrationLogForm accrualMigrationLogForm = (AccrualMigrationLogForm) form;
        PrintWriter out = response.getWriter();
        try {
            LoadAccrualsToLogiware loadAccrualsToLogiware = new LoadAccrualsToLogiware();
            String reprocessLog = loadAccrualsToLogiware.reprocessSingleError(accrualMigrationLogForm.getId());
            if ("success".equals(reprocessLog)) {
                out.print(reprocessLog);
                out.flush();
                out.close();
                return null;
            } else {
                request.setAttribute("exception", reprocessLog);
                return mapping.findForward(REPROCESS_LOGS);
            }
        } catch (Exception e) {
            request.setAttribute("exception", ExceptionUtils.getStackTrace(e));
            return mapping.findForward(REPROCESS_LOGS);
        }
    }

    public ActionForward reprocessAllErrors(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = response.getWriter();
        try {
            new LoadAccrualsToLogiware().reprocessAllErrors();
            out.print("success");
            out.flush();
            out.close();
            return null;
        } catch (Exception e) {
            request.setAttribute("exception", ExceptionUtils.getStackTrace(e));
            return mapping.findForward(REPROCESS_LOGS);
        }
    }

    public ActionForward loadMissingAccruals(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = response.getWriter();
        try {
            out.print(new LoadAccrualsToLogiware().loadMissingAccruals());
            out.flush();
            out.close();
            return null;
        } catch (Exception e) {
            try {
                out.print(new LoadAccrualsToLogiware().loadMissingAccruals());
                out.flush();
                out.close();
                return null;
            } catch (Exception ex) {
                request.setAttribute("exception", ExceptionUtils.getStackTrace(ex));
                return mapping.findForward(REPROCESS_LOGS);
            }
        }
    }

    public ActionForward archive(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        AccrualMigrationLogForm accrualMigrationLogForm = (AccrualMigrationLogForm) form;
        AccrualMigrationLog accrualMigrationLog = new AccrualMigrationLogDAO().findById(accrualMigrationLogForm.getId());
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        accrualMigrationLog.setLogType("archived");
        out.print("success");
        out.flush();
        out.close();
        return null;
    }

    public ActionForward printControlReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        AccrualMigrationLogForm accrualMigrationLogForm = (AccrualMigrationLogForm) form;
        String fromDate = DateUtils.formatDate(DateUtils.parseDate(accrualMigrationLogForm.getFromDate(), "MM/dd/yyyy"), "yyyy-MM-dd");
        String toDate = DateUtils.formatDate(DateUtils.parseDate(accrualMigrationLogForm.getToDate(), "MM/dd/yyyy"), "yyyy-MM-dd");
        ControlReportForm controlReportForm = new ControlReportForm();
        ControlReportDAO controlReportDAO = new ControlReportDAO();
        controlReportForm.setFromDate(accrualMigrationLogForm.getFromDate());
        controlReportForm.setToDate(accrualMigrationLogForm.getToDate());
        controlReportForm.setReportType(CommonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE);
        controlReportForm.setNumberOfBluScreenRecords(controlReportDAO.getNumberOfBlueScreenAccountReceivables(fromDate, toDate));
        controlReportForm.setNumberOfLogiwareRecords(controlReportDAO.getNumberOfLogiwareAccountReceivables(fromDate, toDate));
        controlReportForm.setTotalAmountInBlueScreen(controlReportDAO.getTotalAmountInBlueScreenAccountReceivables(fromDate, toDate));
        controlReportForm.setTotalAmountInLogiware(controlReportDAO.getTotalAmountInLogiwareAccountReceivables(fromDate, toDate));
        controlReportForm.setBlueScreenAccountReceivables(controlReportDAO.getBlueScreenAccountReceivables(fromDate, toDate));
        controlReportForm.setLogiwareAccountReceivables(controlReportDAO.getLogiwareAccountReceivables(fromDate, toDate));
        request.setAttribute("pdfFileName", new ControlReportCreator().createReport(controlReportForm, this.getServlet().getServletContext().getRealPath("/")));
        return mapping.findForward(SUCCESS);
    }

    public ActionForward exportControlReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        AccrualMigrationLogForm accrualMigrationLogForm = (AccrualMigrationLogForm) form;
        String fromDate = DateUtils.formatDate(DateUtils.parseDate(accrualMigrationLogForm.getFromDate(), "MM/dd/yyyy"), "yyyy-MM-dd");
        String toDate = DateUtils.formatDate(DateUtils.parseDate(accrualMigrationLogForm.getToDate(), "MM/dd/yyyy"), "yyyy-MM-dd");
        ControlReportForm controlReportForm = new ControlReportForm();
        ControlReportDAO controlReportDAO = new ControlReportDAO();
        controlReportForm.setFromDate(accrualMigrationLogForm.getFromDate());
        controlReportForm.setToDate(accrualMigrationLogForm.getToDate());
        controlReportForm.setReportType(CommonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE);
        controlReportForm.setNumberOfBluScreenRecords(controlReportDAO.getNumberOfBlueScreenAccountReceivables(fromDate, toDate));
        controlReportForm.setNumberOfLogiwareRecords(controlReportDAO.getNumberOfLogiwareAccountReceivables(fromDate, toDate));
        controlReportForm.setTotalAmountInBlueScreen(controlReportDAO.getTotalAmountInBlueScreenAccountReceivables(fromDate, toDate));
        controlReportForm.setTotalAmountInLogiware(controlReportDAO.getTotalAmountInLogiwareAccountReceivables(fromDate, toDate));
        controlReportForm.setBlueScreenAccountReceivables(controlReportDAO.getBlueScreenAccountReceivables(fromDate, toDate));
        controlReportForm.setLogiwareAccountReceivables(controlReportDAO.getLogiwareAccountReceivables(fromDate, toDate));
        String excelFileName = new ControlReportExcelCreator().exportToExcel(controlReportForm);
        if (CommonUtils.isNotEmpty(excelFileName)) {
            response.addHeader("Content-Disposition", "attachment; filename=" + FilenameUtils.getName(excelFileName));
            response.setContentType("application/vnd.ms-excel" + ";charset=utf-8");
            ServletUtils.returnFile(excelFileName, response.getOutputStream());
            return null;
        }
        return mapping.findForward(SUCCESS);
    }

    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return clear(mapping, form, request, response);
    }
}
