package com.logiware.accounting.action;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.User;
import com.logiware.accounting.dao.SubledgerDAO;
import com.logiware.accounting.excel.SubledgerExcelCreator;
import com.logiware.accounting.form.SessionForm;
import com.logiware.accounting.form.SubledgerForm;
import java.io.PrintWriter;
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
public class SubledgerAction extends BaseAction {

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	SubledgerForm subledgerForm = (SubledgerForm) form;
        SessionForm oldSubledgerForm = new SessionForm();
        PropertyUtils.copyProperties(oldSubledgerForm, subledgerForm);
	request.getSession().setAttribute("oldSubledgerForm", oldSubledgerForm);
	new SubledgerDAO().search(subledgerForm);
	return mapping.findForward(SUCCESS);
    }

    public ActionForward sortingAndPaging(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	SessionForm oldSubledgerForm = (SessionForm) request.getSession().getAttribute("oldSubledgerForm");
	SubledgerForm subledgerForm = (SubledgerForm) form;
        PropertyUtils.copyProperties(subledgerForm, oldSubledgerForm);
	subledgerForm.setSortBy(request.getParameter("sortBy"));
	subledgerForm.setOrderBy(request.getParameter("orderBy"));
	subledgerForm.setSelectedPage(Integer.parseInt(request.getParameter("selectedPage")));
	request.setAttribute("subledgerForm", subledgerForm);
	new SubledgerDAO().search(subledgerForm);
	return mapping.findForward(SUCCESS);
    }
    
    public ActionForward clearAll(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	request.setAttribute("subledgerForm", new SubledgerForm());
	request.getSession().removeAttribute("oldSubledgerForm");
	return mapping.findForward(SUCCESS);
    }

    public ActionForward validateArBatches(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	SubledgerForm subledgerForm = (SubledgerForm) form;
	PrintWriter out = response.getWriter();
	out.print(new SubledgerDAO().validateArBatches(subledgerForm));
	out.flush();
	out.close();
	return null;
    }

    public ActionForward validateGlAccounts(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	SubledgerForm subledgerForm = (SubledgerForm) form;
	new SubledgerDAO().search(subledgerForm);
	if (CommonUtils.isNotEmpty(subledgerForm.getResults())) {
	    return mapping.findForward("glAccounts");
	} else {
	    PrintWriter out = response.getWriter();
	    out.print("");
	    out.flush();
	    out.close();
	    return null;
	}
    }

    public ActionForward updateGlAccount(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	SubledgerForm subledgerForm = (SubledgerForm) form;
	new SubledgerDAO().updateGlAccount(subledgerForm.getGlAccount(), subledgerForm.getSubledgerId());
	PrintWriter out = response.getWriter();
	out.print("success");
	out.flush();
	out.close();
	return null;
    }

    public ActionForward post(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	SubledgerForm subledgerForm = (SubledgerForm) form;
	User user = (User) request.getSession().getAttribute("loginuser");
	new SubledgerDAO().post(subledgerForm, user);
	request.setAttribute("message", subledgerForm.getSubledger() + " subledger is posted successfully");
        request.getSession().removeAttribute("oldSubledgerForm");
	return mapping.findForward(SUCCESS);
    }

    public ActionForward createExcel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	SubledgerForm subledgerForm = (SubledgerForm) form;
	new SubledgerDAO().search(subledgerForm);
	String fileName = new SubledgerExcelCreator(subledgerForm).create();
	PrintWriter out = response.getWriter();
	out.print(fileName);
	out.flush();
	out.close();
	return null;
    }
}
