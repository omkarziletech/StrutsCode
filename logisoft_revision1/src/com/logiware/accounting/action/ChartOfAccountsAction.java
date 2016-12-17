package com.logiware.accounting.action;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.logiware.accounting.dao.ChartOfAccountsDAO;
import com.logiware.accounting.form.ChartOfAccountsForm;
import com.logiware.accounting.form.SessionForm;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Lakshmi Narayanan
 */
public class ChartOfAccountsAction extends BaseAction {

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ChartOfAccountsForm chartOfAccountsForm = (ChartOfAccountsForm) form;
        new ChartOfAccountsDAO().search(chartOfAccountsForm);
        SessionForm oldChartOfAccountsForm = new SessionForm();
        PropertyUtils.copyProperties(oldChartOfAccountsForm, chartOfAccountsForm);
        request.getSession().setAttribute("oldChartOfAccountsForm", oldChartOfAccountsForm);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward sortingAndPaging(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ChartOfAccountsForm chartOfAccountsForm = (ChartOfAccountsForm) form;
        SessionForm oldChartOfAccountsForm = (SessionForm) request.getSession().getAttribute("oldChartOfAccountsForm");
        PropertyUtils.copyProperties(chartOfAccountsForm, oldChartOfAccountsForm);
        if(CommonUtils.isNotEmpty(request.getParameter("sortBy"))){
            chartOfAccountsForm.setSortBy(request.getParameter("sortBy"));
            chartOfAccountsForm.setOrderBy(request.getParameter("orderBy"));
            chartOfAccountsForm.setSelectedPage(Integer.parseInt(request.getParameter("selectedPage")));
            chartOfAccountsForm.setToggled(Boolean.valueOf(request.getParameter("toggled")));
            oldChartOfAccountsForm.setSortBy(request.getParameter("sortBy"));
            oldChartOfAccountsForm.setOrderBy(request.getParameter("orderBy"));
            oldChartOfAccountsForm.setSelectedPage(Integer.parseInt(request.getParameter("selectedPage")));
            oldChartOfAccountsForm.setToggled(Boolean.valueOf(request.getParameter("toggled")));
            request.getSession().setAttribute("oldChartOfAccountsForm", oldChartOfAccountsForm);
        }
        new ChartOfAccountsDAO().search(chartOfAccountsForm);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward clearAll(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("chartOfAccountsForm", new ChartOfAccountsForm());
        request.getSession().removeAttribute("oldChartOfAccountsForm");
        return mapping.findForward(SUCCESS);
    }

    public ActionForward searchTransactions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ChartOfAccountsForm chartOfAccountsForm = (ChartOfAccountsForm) form;
        if (CommonUtils.isEmpty(chartOfAccountsForm.getStartPeriod())) {
            String period = DateUtils.formatDate(new Date(), "yyyyMM");
            chartOfAccountsForm.setStartPeriod(period);
        }
        if (CommonUtils.isEmpty(chartOfAccountsForm.getEndPeriod())) {
            chartOfAccountsForm.setEndPeriod(chartOfAccountsForm.getStartPeriod());
        }
        new ChartOfAccountsDAO().searchTransactions(chartOfAccountsForm);
        SessionForm oldChartOfAccountsForm = new SessionForm();
        PropertyUtils.copyProperties(oldChartOfAccountsForm, chartOfAccountsForm);
        request.getSession().setAttribute("oldTransactionHistoryForm", oldChartOfAccountsForm);
        return mapping.findForward("transactionHistory");
    }

    public ActionForward clearTransactions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("chartOfAccountsForm", new ChartOfAccountsForm());
        request.getSession().removeAttribute("oldTransactionHistoryForm");
        return mapping.findForward("transactionHistory");
    }
}
