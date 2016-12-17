package com.logiware.accounting.action;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.domain.User;
import com.gp.cvst.logisoft.domain.FiscalPeriod;
import com.gp.cvst.logisoft.domain.FiscalYear;
import com.logiware.accounting.dao.FiscalPeriodDAO;
import com.logiware.accounting.excel.IncomeStatementExcelCreator;
import com.logiware.accounting.excel.TrialBalanceExcelCreator;
import com.logiware.accounting.form.FiscalPeriodForm;
import com.logiware.accounting.reports.IncomeStatementCreator;
import com.logiware.accounting.reports.TrialBalanceReportCreator;
import com.logiware.utils.AuditNotesUtils;
import java.io.IOException;
import java.io.PrintWriter;
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
public class FiscalPeriodAction extends BaseAction {

    public ActionForward searchYear(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FiscalPeriodForm fpform = (FiscalPeriodForm) form;
        FiscalPeriodDAO fiscalPeriodDAO = new FiscalPeriodDAO();
        if (CommonUtils.isEmpty(fpform.getFiscalYear().getYear())) {
            fpform.setFiscalYear(fiscalPeriodDAO.getCurrentYear());
        } else {
            fpform.setFiscalYear(fiscalPeriodDAO.getFiscalYear(fpform.getFiscalYear().getYear()));
        }
        fpform.setBudgetSet(fiscalPeriodDAO.getBudgetSet(fpform.getFiscalYear().getYear()));
        fpform.setFiscalPeriods(fiscalPeriodDAO.getAllFiscalPeriods(fpform.getFiscalYear().getYear()));
        return mapping.findForward(SUCCESS);
    }

    public ActionForward createYear(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FiscalPeriodForm fpform = (FiscalPeriodForm) form;
        FiscalPeriodDAO fiscalPeriodDAO = new FiscalPeriodDAO();
        fiscalPeriodDAO.createYear(fpform.getFiscalYear().getYear(), fpform.getFiscalYear().getStartPeriod(), fpform.getFiscalYear().getEndPeriod());
        request.setAttribute("message", "Year - " + fpform.getFiscalYear().getYear() + " is created successfully.");
        return searchYear(mapping, form, request, response);
    }

    public ActionForward validateClosingYear(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FiscalPeriodDAO fiscalPeriodDAO = new FiscalPeriodDAO();
        PrintWriter out = null;
        try {
            out = response.getWriter();
            Integer year = Integer.parseInt(request.getParameter("year"));
            FiscalYear fiscalYear = fiscalPeriodDAO.getFiscalYear(year);
            if (CommonUtils.isNotEqualIgnoreCase(fiscalYear.getActive(), STATUS_OPEN)) {
                out.print("Year - " + year + " is closed already.");
            } else {
                boolean hasOpenPeriods = fiscalPeriodDAO.hasOpenPeriods(year);
                if (hasOpenPeriods) {
                    out.print("Some periods are still open for the year - " + year + ". Please close them to close the year.");
                } else {
                    boolean hasOpenSubledgers = fiscalPeriodDAO.hasOpenSubledgers(fiscalYear);
                    if (hasOpenSubledgers) {
                        out.print("Some subledgers are still open for the year - " + year + ". Please post them to close the year.");
                    } else {
                        out.print("success");
                    }
                }
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (null != out) {
                out.flush();
                out.close();
            }
        }
        return null;
    }

    public ActionForward createJournalEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FiscalPeriodDAO fiscalPeriodDAO = new FiscalPeriodDAO();
        User user = (User) request.getSession().getAttribute("loginuser");
        PrintWriter out = null;
        try {
            Integer year = Integer.parseInt(request.getParameter("year"));
            FiscalYear fiscalYear = fiscalPeriodDAO.getFiscalYear(year);
            out = response.getWriter();
            String batchId = fiscalPeriodDAO.createJournalEntry(fiscalYear, user.getLoginName());
            out.print(batchId);
        } catch (IOException e) {
            throw e;
        } finally {
            if (null != out) {
                out.flush();
                out.close();
            }
        }
        return null;
    }

    public ActionForward createBudget(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FiscalPeriodDAO fiscalPeriodDAO = new FiscalPeriodDAO();
        PrintWriter out = null;
        try {
            Integer year = Integer.parseInt(request.getParameter("year"));
            Integer budgetSet = Integer.parseInt(request.getParameter("budgetSet"));
            fiscalPeriodDAO.createBudget(year, budgetSet);
            out = response.getWriter();
            out.print("Budget Set - " + budgetSet + " is created for Year - " + year + " successfully.");
        } catch (IOException e) {
            throw e;
        } finally {
            if (null != out) {
                out.flush();
                out.close();
            }
        }
        return null;
    }

    public ActionForward openPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FiscalPeriodDAO fiscalPeriodDAO = new FiscalPeriodDAO();
        PrintWriter out = null;
        try {
            String period = request.getParameter("period");
            FiscalPeriod fiscalPeriod = fiscalPeriodDAO.getFiscalPeriod(period);
            fiscalPeriod.setStatus("Open");
            fiscalPeriodDAO.getCurrentSession().update(fiscalPeriod);
            User user = (User) request.getSession().getAttribute("loginuser");
            StringBuilder desc = new StringBuilder();
            desc.append("Fiscal Period - ").append(period);
            desc.append(" is reopened by ").append(user.getLoginName());
            desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
            AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.FISCAL_PERIOD, period, NotesConstants.FISCAL_PERIOD, user);
            out = response.getWriter();
            out.print("Period - " + period + " is reopened successfully.");
        } catch (IOException e) {
            throw e;
        } finally {
            if (null != out) {
                out.flush();
                out.close();
            }
        }
        return null;
    }

    public ActionForward closePeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FiscalPeriodDAO fiscalPeriodDAO = new FiscalPeriodDAO();
        PrintWriter out = null;
        try {
            String period = request.getParameter("period");
            FiscalPeriod fiscalPeriod = fiscalPeriodDAO.getFiscalPeriod(period);
            fiscalPeriod.setStatus("Close");
            fiscalPeriodDAO.getCurrentSession().update(fiscalPeriod);
            User user = (User) request.getSession().getAttribute("loginuser");
            StringBuilder desc = new StringBuilder();
            desc.append("Fiscal Period - ").append(period);
            desc.append(" is closed by ").append(user.getLoginName());
            desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
            AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.FISCAL_PERIOD, period, NotesConstants.FISCAL_PERIOD, user);
            out = response.getWriter();
            out.print("Period - " + period + " is closed successfully.");
        } catch (IOException e) {
            throw e;
        } finally {
            if (null != out) {
                out.flush();
                out.close();
            }
        }
        return null;
    }

    public ActionForward createTrialBalance(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = null;
        try {
            out = response.getWriter();
            String startPeriod = request.getParameter("startPeriod");
            String endPeriod = request.getParameter("endPeriod");
            boolean ecuFormat = Boolean.parseBoolean(request.getParameter("ecuFormat"));
            String fileType = request.getParameter("fileType");
            if (CommonUtils.isEqualIgnoreCase(fileType, "pdf")) {
                String contextPath = this.getServlet().getServletContext().getRealPath("/");
                String filePath = new TrialBalanceReportCreator(startPeriod, endPeriod, ecuFormat, contextPath).create();
                out.print(filePath);
            } else {
                String filePath = new TrialBalanceExcelCreator(startPeriod, endPeriod, ecuFormat).create();
                out.print(filePath);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != out) {
                out.flush();
                out.close();
            }
        }
        return null;
    }

    public ActionForward createIncomeStatement(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = null;
        try {
            out = response.getWriter();
            String startPeriod = request.getParameter("startPeriod");
            String endPeriod = request.getParameter("endPeriod");
            String fileType = request.getParameter("fileType");
            if (CommonUtils.isEqualIgnoreCase(fileType, "pdf")) {
                String contextPath = this.getServlet().getServletContext().getRealPath("/");
                String filePath = new IncomeStatementCreator(startPeriod, endPeriod, 1, contextPath).create();
                out.print(filePath);
            } else {
                String filePath = new IncomeStatementExcelCreator(startPeriod, endPeriod, 1).create();
                out.print(filePath);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != out) {
                out.flush();
                out.close();
            }
        }
        return null;
    }

    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return searchYear(mapping, form, request, response);
    }

}
