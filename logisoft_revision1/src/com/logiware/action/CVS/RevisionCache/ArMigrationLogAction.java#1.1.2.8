package com.logiware.action;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.infomata.data.CSVFormat;
import com.infomata.data.DataFile;
import com.infomata.data.DataRow;
import com.logiware.datamigration.LoadOpenArToLogiware;
import com.logiware.excel.ControlReportExcelCreator;
import com.logiware.form.ArMigrationLogForm;
import com.logiware.form.ControlReportForm;
import com.logiware.hibernate.dao.ArMigrationLogDAO;
import com.logiware.hibernate.dao.ControlReportDAO;
import com.logiware.hibernate.domain.ArMigrationLog;
import com.logiware.reports.ControlReportCreator;
import com.logiware.utils.ExceptionUtils;
import com.oreilly.servlet.ServletUtils;
import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
public class ArMigrationLogAction extends DispatchAction {

    private static String SUCCESS = "success";
    private static String CSV_FILE = "csvFile";
    private static String REPROCESS_LOGS = "reprocessLogs";

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ArMigrationLogForm arMigrationLogForm = (ArMigrationLogForm) form;
        ArMigrationLogDAO arMigrationLogDAO = new ArMigrationLogDAO();
        Criteria criteria = arMigrationLogDAO.createCriteria(arMigrationLogForm);
        int totalNoOfRecords = arMigrationLogDAO.getTotalNoOfRecords(criteria);
        int totalPageNo = (totalNoOfRecords / arMigrationLogForm.getRecordsLimit()) + (totalNoOfRecords % arMigrationLogForm.getRecordsLimit() > 0 ? 1 : 0);
        arMigrationLogForm.setTotalPageNo(totalPageNo);
        int start = arMigrationLogForm.getRecordsLimit() * (arMigrationLogForm.getCurrentPageNo() - 1);
        int end = arMigrationLogForm.getRecordsLimit();
        List<ArMigrationLog> logs = arMigrationLogDAO.getLogs(criteria, arMigrationLogForm.getSortBy(), arMigrationLogForm.getOrderBy(), start, end);
        arMigrationLogForm.setCurrentNoOfRecords(logs.size());
        arMigrationLogForm.setTotalNoOfRecords(totalNoOfRecords);
        arMigrationLogForm.setLogs(logs);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward showReprocessLogs(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ArMigrationLogForm arMigrationLogForm = (ArMigrationLogForm) form;
        try {
            request.setAttribute("arMigrationLog", new ArMigrationLogDAO().findById(arMigrationLogForm.getId()));
        } catch (Exception e) {
            request.setAttribute("exception", ExceptionUtils.getStackTrace(e));
        }
        return mapping.findForward(REPROCESS_LOGS);
    }

    public ActionForward showCsvFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ArMigrationLogForm arMigrationLogForm = (ArMigrationLogForm) form;
        try {
            ArMigrationLog arMigrationLog = new ArMigrationLogDAO().findById(arMigrationLogForm.getId());
            Properties prop = new Properties();
            prop.load(getClass().getResourceAsStream("/com/logiware/datamigration/dbconnection.properties"));
            File file = new File(prop.getProperty("openarError"), arMigrationLog.getFileName().replace(".csv", "") + arMigrationLog.getFileLineNumber() + ".csv");
            Map<String, String> content = new HashMap<String, String>();
            DataFile read = DataFile.createReader("8859_1");
            read.setDataFormat(new CSVFormat());
            read.open(file);
            DataRow row = read.next();
            if (null != row) {
                content.put("trmnum", row.getString(0));
                content.put("actnum", row.getString(1));
                content.put("blkey", row.getString(2));
                content.put("cntnum", row.getString(3));
                content.put("refnum", row.getString(4));
                content.put("unus01", row.getString(5));
                content.put("totdue", row.getString(6));
                content.put("type1", row.getString(7));
                content.put("age", row.getString(8));
                content.put("shtnam", row.getString(9));
                content.put("cornum", row.getString(10));
                content.put("type2", row.getString(11));
                content.put("hstkey", row.getString(12));
                content.put("invkey", row.getString(13));
                content.put("lstupd", row.getString(14));
                content.put("bldate", row.getString(15));
                content.put("cmpnum", row.getString(16));
                content.put("unus02", row.getString(17));
                content.put("selpmt", row.getString(18));
                content.put("seladj", row.getString(19));
                content.put("pdtype", row.getString(20));
                content.put("postim", row.getString(21));
                content.put("depnum", row.getString(22));
            }
            read.close();
            request.setAttribute("id", arMigrationLog.getId());
            request.setAttribute("fileName", arMigrationLog.getFileName());
            request.setAttribute("content", content);
        } catch (Exception e) {
            request.setAttribute("exception", ExceptionUtils.getStackTrace(e));
        }
        return mapping.findForward(CSV_FILE);
    }

    public ActionForward saveCsvFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ArMigrationLogForm arMigrationLogForm = (ArMigrationLogForm) form;
        ArMigrationLog arMigrationLog = new ArMigrationLogDAO().findById(arMigrationLogForm.getId());
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        Properties prop = new Properties();
        prop.load(getClass().getResourceAsStream("/com/logiware/datamigration/dbconnection.properties"));
        File file = new File(prop.getProperty("openarError"), arMigrationLog.getFileName().replace(".csv", "") + arMigrationLog.getFileLineNumber() + ".csv");
        file.setWritable(true, false);
        DataFile write = DataFile.createWriter("8859_1", false);
        write.setDataFormat(new CSVFormat());
        write.open(file);
        DataRow row = write.next();
        row.add(request.getParameter("trmnum"));
        row.add(request.getParameter("actnum"));
        row.add(request.getParameter("blkey"));
        row.add(request.getParameter("cntnum"));
        row.add(request.getParameter("refnum"));
        row.add(request.getParameter("unus01"));
        row.add(request.getParameter("totdue"));
        row.add(request.getParameter("type1"));
        row.add(request.getParameter("age"));
        row.add(request.getParameter("shtnam"));
        row.add(request.getParameter("cornum"));
        row.add(request.getParameter("type2"));
        row.add(request.getParameter("hstkey"));
        row.add(request.getParameter("invkey"));
        row.add(request.getParameter("lstupd"));
        row.add(request.getParameter("bldate"));
        row.add(request.getParameter("cmpnum"));
        row.add(request.getParameter("unus02"));
        row.add(request.getParameter("selpmt"));
        row.add(request.getParameter("seladj"));
        row.add(request.getParameter("pdtype"));
        row.add(request.getParameter("postim"));
        row.add(request.getParameter("depnum"));
        write.close();
        out.print("success");
        out.flush();
        out.close();
        return null;
    }

    public ActionForward reprocessSingleError(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ArMigrationLogForm arMigrationLogForm = (ArMigrationLogForm) form;
        PrintWriter out = response.getWriter();
        try {
            LoadOpenArToLogiware loadOpenArToLogiware = new LoadOpenArToLogiware();
            String reprocessLog = loadOpenArToLogiware.reprocessSingleError(arMigrationLogForm.getId());
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
            new LoadOpenArToLogiware().reprocessAllErrors();
            out.print("success");
            out.flush();
            out.close();
            return null;
        } catch (Exception e) {
            request.setAttribute("exception", ExceptionUtils.getStackTrace(e));
            return mapping.findForward(REPROCESS_LOGS);
        }
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ArMigrationLogForm arMigrationLogForm = (ArMigrationLogForm) form;
        ArMigrationLog arMigrationLog = new ArMigrationLogDAO().findById(arMigrationLogForm.getId());
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        arMigrationLog.delete();
        out.print("success");
        out.flush();
        out.close();
        return null;
    }

    public ActionForward printControlReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ArMigrationLogForm arMigrationLogForm = (ArMigrationLogForm) form;
        String fromDate = DateUtils.formatDate(DateUtils.parseDate(arMigrationLogForm.getFromDate(), "MM/dd/yyyy"), "yyyy-MM-dd");
        String toDate = DateUtils.formatDate(DateUtils.parseDate(arMigrationLogForm.getToDate(), "MM/dd/yyyy"), "yyyy-MM-dd");
        ControlReportForm controlReportForm = new ControlReportForm();
        ControlReportDAO controlReportDAO = new ControlReportDAO();
        controlReportForm.setFromDate(arMigrationLogForm.getFromDate());
        controlReportForm.setToDate(arMigrationLogForm.getToDate());
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
        ArMigrationLogForm arMigrationLogForm = (ArMigrationLogForm) form;
        String fromDate = DateUtils.formatDate(DateUtils.parseDate(arMigrationLogForm.getFromDate(), "MM/dd/yyyy"), "yyyy-MM-dd");
        String toDate = DateUtils.formatDate(DateUtils.parseDate(arMigrationLogForm.getToDate(), "MM/dd/yyyy"), "yyyy-MM-dd");
        ControlReportForm controlReportForm = new ControlReportForm();
        ControlReportDAO controlReportDAO = new ControlReportDAO();
        controlReportForm.setFromDate(arMigrationLogForm.getFromDate());
        controlReportForm.setToDate(arMigrationLogForm.getToDate());
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
        return mapping.findForward(SUCCESS);
    }
}
