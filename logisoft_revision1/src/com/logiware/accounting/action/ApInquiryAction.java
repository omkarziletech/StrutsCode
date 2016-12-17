package com.logiware.accounting.action;

import com.logiware.accounting.dao.AccrualsDAO;
import com.logiware.accounting.dao.ApInquiryDAO;
import com.logiware.accounting.excel.ApInquiryExcelCreator;
import com.logiware.accounting.form.ApInquiryForm;
import com.logiware.accounting.reports.ApInquiryReportCreator;
import com.logiware.excel.ExportNSInvoiceToExcel;
import com.logiware.reports.NSInvoiceReportCreator;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Lakshmi Narayanan
 */
public class ApInquiryAction extends BaseAction {

    private static final String INVOICE_DETAILS = "invoiceDetails";

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	ApInquiryForm apInquiryForm = (ApInquiryForm) form;
	new ApInquiryDAO().search(apInquiryForm);
	return mapping.findForward(SUCCESS);
    }

    public ActionForward clearAll(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	request.setAttribute("apInquiryForm", new ApInquiryForm());
	return mapping.findForward(SUCCESS);
    }

    public ActionForward clearFilters(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	ApInquiryForm apInquiryForm = (ApInquiryForm) form;
	ApInquiryForm newApInquiryForm = new ApInquiryForm();
	newApInquiryForm.setVendorName(apInquiryForm.getVendorName());
	newApInquiryForm.setVendorNumber(apInquiryForm.getVendorNumber());
	newApInquiryForm.setDisabled(apInquiryForm.isDisabled());
	newApInquiryForm.setToggled(apInquiryForm.isToggled());
	request.setAttribute("apInquiryForm", newApInquiryForm);
	return mapping.findForward(SUCCESS);
    }

    public ActionForward showInvoiceDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	ApInquiryForm apInquiryForm = (ApInquiryForm) form;
	String vendorNumber = apInquiryForm.getVendorNumber();
	String invoiceNumber = apInquiryForm.getInvoiceNumber();
	String blNumber = apInquiryForm.getBlNumber();
	String transactionType = apInquiryForm.getTransactionType();
	request.setAttribute(INVOICE_DETAILS, new AccrualsDAO().getInvoiceDetails(vendorNumber, invoiceNumber, blNumber, transactionType));
	return mapping.findForward(INVOICE_DETAILS);
    }

    public ActionForward createNSInvoicePdf(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	String contextPath = this.getServlet().getServletContext().getRealPath("/");
	String batchId = request.getParameter("batchId");
	String fileName = new NSInvoiceReportCreator().createReport(contextPath, batchId);
	PrintWriter out = response.getWriter();
	out.print(fileName);
	out.flush();
	out.close();
	return null;
    }

    public ActionForward createNSInvoiceExcel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	String batchId = request.getParameter("batchId");
	String fileName = new ExportNSInvoiceToExcel().exportToExcel(batchId);
	PrintWriter out = response.getWriter();
	out.print(fileName);
	out.flush();
	out.close();
	return null;
    }

    public ActionForward createPdf(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	ApInquiryForm apInquiryForm = (ApInquiryForm) form;
	String contextPath = this.getServlet().getServletContext().getRealPath("/");
	new ApInquiryDAO().search(apInquiryForm);
	String fileName = new ApInquiryReportCreator(apInquiryForm, contextPath).create();
	PrintWriter out = response.getWriter();
	out.print(fileName);
	out.flush();
	out.close();
	return null;
    }

    public ActionForward createExcel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	ApInquiryForm apInquiryForm = (ApInquiryForm) form;
	new ApInquiryDAO().search(apInquiryForm);
	String fileName = new ApInquiryExcelCreator(apInquiryForm).create();
	PrintWriter out = response.getWriter();
	out.print(fileName);
	out.flush();
	out.close();
	return null;
    }
}
