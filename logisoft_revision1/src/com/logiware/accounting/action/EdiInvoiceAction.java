package com.logiware.accounting.action;

import com.logiware.accounting.form.EdiInvoiceForm;
import com.logiware.accounting.utils.EdiInvoiceUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/**
 *
 * @author Lakshmi Naryanan
 */
public class EdiInvoiceAction extends DispatchAction {

    private static final String SUCCESS = "success";
    private static final String ACCRUALS = "accruals";
    private static final String LOGS = "logs";

    public ActionForward clear(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	request.setAttribute("ediInvoiceForm", new EdiInvoiceForm());
	return mapping.findForward(SUCCESS);
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	EdiInvoiceForm ediInvoiceForm = (EdiInvoiceForm) form;
	EdiInvoiceUtils.search(ediInvoiceForm);
	return mapping.findForward(SUCCESS);
    }

    public ActionForward showLogs(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	EdiInvoiceUtils.showLogs(request);
	return mapping.findForward(LOGS);
    }

    public ActionForward removeLog(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	EdiInvoiceUtils.removeLog(request, response);
	return null;
    }

    public ActionForward viewEdiFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	EdiInvoiceUtils.viewEdiFile(request, response);
	return null;
    }

    public ActionForward showAccruals(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	EdiInvoiceForm ediInvoiceForm = (EdiInvoiceForm) form;
	EdiInvoiceUtils.showAccruals(ediInvoiceForm, request);
	return mapping.findForward(ACCRUALS);
    }

    public ActionForward updateVendor(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	EdiInvoiceForm ediInvoiceForm = (EdiInvoiceForm) form;
	EdiInvoiceUtils.updateVendor(ediInvoiceForm, request, response);
	return null;
    }

    public ActionForward updateAccrual(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	EdiInvoiceForm ediInvoiceForm = (EdiInvoiceForm) form;
	EdiInvoiceUtils.updateAccrual(ediInvoiceForm, request, response);
	return null;
    }

    public ActionForward removeAccrual(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	EdiInvoiceUtils.removeAccrual(request, response);
	return null;
    }

    public ActionForward printInvoice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	EdiInvoiceForm ediInvoiceForm = (EdiInvoiceForm) form;
	String contextPath = this.servlet.getServletContext().getRealPath("/");
	EdiInvoiceUtils.printInvoice(ediInvoiceForm, contextPath, response);
	return null;
    }

    public ActionForward postToAp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	EdiInvoiceForm ediInvoiceForm = (EdiInvoiceForm) form;
	String contextPath = this.servlet.getServletContext().getRealPath("/");
	boolean success = EdiInvoiceUtils.postToAp(ediInvoiceForm, contextPath, request, response);
	if (success) {
	    return null;
	} else {
	    return mapping.findForward(ACCRUALS);
	}
    }

    public ActionForward deriveGlAccount(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	String account = request.getParameter("account");
	String suffix = request.getParameter("suffix");
	String shipmentType = request.getParameter("shipmentType");
	String terminal = request.getParameter("terminal");
	EdiInvoiceUtils.deriveGlAccount(account, suffix, shipmentType, terminal, response);
	return null;
    }

    public ActionForward archiveInvoice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	EdiInvoiceUtils.archiveInvoice(request, response);
	return null;
    }

    public ActionForward updateEdiCode(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	EdiInvoiceUtils.updateEdiCode();
	return null;
    }

    public ActionForward updateInvoiceAmount(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	EdiInvoiceUtils.updateInvoiceAmount();
	return null;
    }

    public ActionForward attachInvoices(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	EdiInvoiceForm ediInvoiceForm = (EdiInvoiceForm) form;
	String contextPath = this.servlet.getServletContext().getRealPath("/");
	EdiInvoiceUtils.attachInvoices(ediInvoiceForm, contextPath, request, response);
	return null;
    }

    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	return clear(mapping, form, request, response);
    }
}
