package com.logiware.action;

import com.gp.cong.common.ConstantsInterface;
import com.logiware.excel.GlReportsExcelCreator;
import com.logiware.form.GlReportsForm;
import com.logiware.reports.GlReportsCreator;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/**
 *
 * @author Lakshmi Narayanan
 */
public class GlReportsAction extends DispatchAction implements ConstantsInterface {

    public ActionForward show(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	String tabName = request.getParameter("tabName");
	request.setAttribute("glReportsForm", new GlReportsForm());
	return mapping.findForward(tabName);
    }

    public ActionForward clear(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	return show(mapping, form, request, response);
    }

    public ActionForward createPdf(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	PrintWriter out = response.getWriter();
	    GlReportsForm glReportsForm = (GlReportsForm) form;
	    String contextPath = this.servlet.getServletContext().getRealPath("/");
	    String fileName = new GlReportsCreator(glReportsForm, contextPath).create();
	    out.print(fileName);
	    return null;
    }

    public ActionForward createExcel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	    GlReportsForm glReportsForm = (GlReportsForm) form;
	    String fileName = new GlReportsExcelCreator(glReportsForm).create();
	    PrintWriter out = response.getWriter();
	    out.print(fileName);
	    return null;
    }

    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	return mapping.findForward(SUCCESS);
    }

    
}
