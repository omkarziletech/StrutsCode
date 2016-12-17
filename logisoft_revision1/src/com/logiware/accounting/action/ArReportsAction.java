package com.logiware.accounting.action;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.logiware.accounting.dao.ArReportsDAO;
import com.logiware.accounting.excel.ArActivityExcelCreator;
import com.logiware.accounting.excel.ArAgingExcelCreator;
import com.logiware.accounting.excel.ArNotesExcelCreator;
import com.logiware.accounting.excel.DsoExcelCreator;
import com.logiware.accounting.excel.ManualStatementExcelCreator;
import com.logiware.accounting.excel.NoCreditExcelCreator;
import com.logiware.accounting.form.ArReportsForm;
import com.logiware.accounting.model.CompanyModel;
import com.logiware.accounting.reports.ArAgingReportCreator;
import com.logiware.accounting.reports.ArNotesReportCreator;
import com.logiware.accounting.reports.DsoReportCreator;
import com.logiware.accounting.reports.ManualStatementCreator;
import com.logiware.accounting.reports.NoCreditReportCreator;
import com.logiware.accounting.reports.ArDisputeReportCreator;
import com.logiware.accounting.excel.ArDisputeExcelCreator;
import com.logiware.bean.CustomerBean;
import com.logiware.excel.StatementConfigurationExcelCreator;
import com.oreilly.servlet.ServletUtils;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FilenameUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Lakshmi Naryanan
 */
public class ArReportsAction extends BaseAction {

    protected final static String STATEMENT = "statement";
    protected final static String AGING = "aging";
    protected final static String DSO = "dso";
    protected final static String NOTES = "notes";
    protected final static String NO_CREDIT = "noCredit";
    protected final static String ACTIVITY = "Activity";
    protected final static String DISPUTE = "dispute";

    public ActionForward show(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String tabName = request.getParameter("tabName");
        ArReportsForm arReportsForm = new ArReportsForm();
        if (CommonUtils.isEqualIgnoreCase(tabName, STATEMENT)) {
            arReportsForm.setNetsett(YES);
            arReportsForm.setPrepayments(YES);
            arReportsForm.setCreditStatement(true);
            arReportsForm.setCreditInvoice(true);
        } else if (CommonUtils.isEqualIgnoreCase(tabName, AGING)) {
            arReportsForm.setAllCustomers(true);
            arReportsForm.setReportType(SUMMARY);
            arReportsForm.setAgents(YES);
            arReportsForm.setNetsett(YES);
            arReportsForm.setCutOffDate(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
            arReportsForm.setAllPayments(true);
        } else if (CommonUtils.isEqualIgnoreCase(tabName, DSO)) {
            Calendar lastYear = Calendar.getInstance();
            lastYear.add(Calendar.YEAR, -1);
            Calendar currentYear = Calendar.getInstance();
            String dsoPeriod = DateUtils.formatDate(lastYear.getTime(), "MM/dd/yyyy") + " - " + DateUtils.formatDate(currentYear.getTime(), "MM/dd/yyyy");
            String numberOfDays = String.valueOf((currentYear.getTimeInMillis() - lastYear.getTimeInMillis()) / (24 * 60 * 60 * 1000));
            arReportsForm.setDsoPeriod(dsoPeriod);
            arReportsForm.setNumberOfDays(numberOfDays);
        } else if (CommonUtils.isEqualIgnoreCase(tabName, NO_CREDIT)) {
            Calendar lastYear = Calendar.getInstance();
            lastYear.add(Calendar.YEAR, -1);
            arReportsForm.setFromDate(DateUtils.formatDate(lastYear.getTime(), "MM/dd/yyyy"));
            arReportsForm.setToDate(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
        } else if (CommonUtils.isEqualIgnoreCase(tabName, ACTIVITY) || CommonUtils.isEqualIgnoreCase(tabName, DISPUTE)) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -1);
            arReportsForm.setFromDate(DateUtils.formatDate(cal.getTime(), "MM/dd/yyyy"));
            arReportsForm.setToDate(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
        }
        request.setAttribute("arReportsForm", arReportsForm);
        return mapping.findForward(tabName);
    }

    public ActionForward clear(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return show(mapping, form, request, response);
    }

    public ActionForward createPdf(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = response.getWriter();
        ArReportsForm arReportsForm = (ArReportsForm) form;
        String tabName = arReportsForm.getTabName();
        String fileName = null;
        String contextPath = this.servlet.getServletContext().getRealPath("/");
        if (CommonUtils.isEqualIgnoreCase(tabName, STATEMENT)) {
            CustomerBean customer = null;
            if (CommonUtils.isNotEmpty(arReportsForm.getCustomerNumber())) {
                customer = new ArReportsDAO().getCustomerDetails(arReportsForm.getCustomerNumber());
            }
            CompanyModel company = new SystemRulesDAO().getCompanyDetails();
            fileName = new ManualStatementCreator(arReportsForm, customer, company, contextPath).create();
        } else if (CommonUtils.isEqualIgnoreCase(tabName, AGING)) {
            CustomerBean customer = null;
            if (CommonUtils.isNotEmpty(arReportsForm.getCustomerNumber())) {
                customer = new ArReportsDAO().getCustomerDetails(arReportsForm.getCustomerNumber());
            }
            fileName = new ArAgingReportCreator(arReportsForm, customer, contextPath).create();
        } else if (CommonUtils.isEqualIgnoreCase(tabName, DSO)) {
            fileName = new DsoReportCreator(arReportsForm, contextPath).create();
        } else if (CommonUtils.isEqualIgnoreCase(tabName, NOTES)) {
            fileName = new ArNotesReportCreator(arReportsForm, contextPath).create();
        } else if (CommonUtils.isEqualIgnoreCase(tabName, NO_CREDIT)) {
            fileName = new NoCreditReportCreator(arReportsForm, contextPath).create();
        } else if (CommonUtils.isEqualIgnoreCase(tabName, DISPUTE)) {
            fileName = new ArDisputeReportCreator(arReportsForm, contextPath).create();
        }
        out.print(fileName);
        return null;
    }

    public ActionForward printPdf(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fileName = request.getParameter("fileName");
        //Put the pdf file in response as inline document
        response.addHeader("Content-Disposition", "inline; filename=" + FilenameUtils.getName(fileName));
        response.setContentType("application/pdf;charset=utf-8");
        ServletUtils.returnFile(fileName, response.getOutputStream());
        return null;
    }

    public ActionForward createExcel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ArReportsForm arReportsForm = (ArReportsForm) form;
        String tabName = arReportsForm.getTabName();
        String fileName = null;
        if (CommonUtils.isEqualIgnoreCase(tabName, STATEMENT)) {
            CustomerBean customer = null;
            if (CommonUtils.isNotEmpty(arReportsForm.getCustomerNumber())) {
                customer = new ArReportsDAO().getCustomerDetails(arReportsForm.getCustomerNumber());
            }
            CompanyModel company = new SystemRulesDAO().getCompanyDetails();
            fileName = new ManualStatementExcelCreator(arReportsForm, customer, company).create();
        } else if (CommonUtils.isEqualIgnoreCase(tabName, AGING)) {
            CustomerBean customer = null;
            if (CommonUtils.isNotEmpty(arReportsForm.getCustomerNumber())) {
                customer = new ArReportsDAO().getCustomerDetails(arReportsForm.getCustomerNumber());
            }
            fileName = new ArAgingExcelCreator(arReportsForm, customer).create();
        } else if (CommonUtils.isEqualIgnoreCase(tabName, DSO)) {
            fileName = new DsoExcelCreator(arReportsForm).create();
        } else if (CommonUtils.isEqualIgnoreCase(tabName, NOTES)) {
            fileName = new ArNotesExcelCreator(arReportsForm).create();
        } else if (CommonUtils.isEqualIgnoreCase(tabName, NO_CREDIT)) {
            fileName = new NoCreditExcelCreator(arReportsForm).create();
        } else if (CommonUtils.isEqualIgnoreCase(tabName, ACTIVITY)) {
            fileName = new ArActivityExcelCreator(arReportsForm).create();
        } else if (CommonUtils.isEqualIgnoreCase(tabName, DISPUTE)) {
            fileName = new ArDisputeExcelCreator(arReportsForm).create();
        }
        PrintWriter out = response.getWriter();
        out.print(fileName);
        return null;
    }

    public ActionForward createConfigurationExcel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fileName = new StatementConfigurationExcelCreator().create(true);
        PrintWriter out = response.getWriter();
        out.print(fileName);
        return null;
    }

    public ActionForward createExemptfromAutoHoldExcel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fileName = new StatementConfigurationExcelCreator().create(false);
        PrintWriter out = response.getWriter();
        out.print(fileName);
        return null;
    }

    public ActionForward exportExcel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fileName = request.getParameter("fileName");
        //Put the xlsx file in response as attachment
        response.addHeader("Content-Disposition", "attachment; filename=" + FilenameUtils.getName(fileName));
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        ServletUtils.returnFile(fileName, response.getOutputStream());
        return null;
    }
}
