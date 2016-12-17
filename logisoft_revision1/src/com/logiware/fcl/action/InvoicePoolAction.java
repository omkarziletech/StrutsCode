package com.logiware.fcl.action;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.ItemDAO;
import com.gp.cong.logisoft.reports.ArRedInvoicePdfCreator;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.fcl.dao.InvoicePoolDAO;
import com.logiware.fcl.dao.SearchDAO;
import com.logiware.fcl.form.InvoicePoolForm;
import com.logiware.fcl.form.SearchForm;
import com.logiware.hibernate.dao.ArRedInvoiceDAO;
import com.logiware.hibernate.domain.ArRedInvoice;
import java.io.File;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import com.gp.cong.common.DateUtils;
import java.util.Date;

/**
 *
 * @author Lakshmi Narayanan
 */
public class InvoicePoolAction extends BaseAction {

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	InvoicePoolForm invoicePoolForm = (InvoicePoolForm) form;
	request.getSession().setAttribute("oldInvoicePoolForm", invoicePoolForm);
	new InvoicePoolDAO().search(invoicePoolForm);
	return mapping.findForward(SUCCESS);
    }

    public ActionForward gotoPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	InvoicePoolForm invoicePoolForm = (InvoicePoolForm) form;
	InvoicePoolForm oldInvoicePoolForm = (InvoicePoolForm) request.getSession().getAttribute("oldInvoicePoolForm");
	oldInvoicePoolForm.setSortBy(invoicePoolForm.getSortBy());
	oldInvoicePoolForm.setOrderBy(invoicePoolForm.getOrderBy());
	oldInvoicePoolForm.setSelectedPage(invoicePoolForm.getSelectedPage());
	oldInvoicePoolForm.setLimit(invoicePoolForm.getLimit());
	oldInvoicePoolForm.setOrderByField(null);
	new InvoicePoolDAO().search(oldInvoicePoolForm);
	request.setAttribute("invoicePoolForm", oldInvoicePoolForm);
	return mapping.findForward(SUCCESS);
    }

    public ActionForward doSort(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	InvoicePoolForm invoicePoolForm = (InvoicePoolForm) form;
	InvoicePoolForm oldInvoicePoolForm = (InvoicePoolForm) request.getSession().getAttribute("oldInvoicePoolForm");
	oldInvoicePoolForm.setSortBy(invoicePoolForm.getSortBy());
	oldInvoicePoolForm.setOrderBy(invoicePoolForm.getOrderBy());
	oldInvoicePoolForm.setSelectedPage(invoicePoolForm.getSelectedPage());
	oldInvoicePoolForm.setLimit(invoicePoolForm.getLimit());
	oldInvoicePoolForm.setOrderByField(null);
	new InvoicePoolDAO().search(oldInvoicePoolForm);
	request.setAttribute("invoicePoolForm", oldInvoicePoolForm);
	return mapping.findForward(SUCCESS);
    }

    public ActionForward reset(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	request.getSession().removeAttribute("oldInvoicePoolForm");
	InvoicePoolForm invoicePoolForm = (InvoicePoolForm) form;
	InvoicePoolForm newInvoicePoolForm = new InvoicePoolForm();
	newInvoicePoolForm.setImportFile(invoicePoolForm.isImportFile());
	request.setAttribute("invoicePoolForm", newInvoicePoolForm);
	return mapping.findForward(SUCCESS);
    }

    public ActionForward checkLocking(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	InvoicePoolForm invoicePoolForm = (InvoicePoolForm) form;
	User user = (User) request.getSession().getAttribute("loginuser");
	String result = new SearchDAO().checkLocking(invoicePoolForm.getFileNumber(), user.getUserId());
	PrintWriter out = response.getWriter();
	if (CommonUtils.isNotEmpty(result)) {
	    out.print(result);
	} else {
	    String itemDesc = "Quotes, Bookings, and BLs";
	    String uniqueCode = invoicePoolForm.isImportFile() ? "IMP" : "";
	    out.print("available========" + new ItemDAO().getItemId(itemDesc, uniqueCode));
	}
	out.flush();
	out.close();
	return null;
    }

    public ActionForward preview(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	Integer id = Integer.parseInt(request.getParameter("id"));
	ArRedInvoice invoice = new ArRedInvoiceDAO().findById(id);
        String fileLocation = LoadLogisoftProperties.getProperty("reportLocation") + "/Documents/RedInvoice/" + DateUtils.formatDate(new Date(), "yyyy/MM/dd");
	File dir = new File(fileLocation);
	if (!dir.exists()) {
	    dir.mkdirs();
	}
	User user = (User) request.getSession().getAttribute("loginuser");
	String fileName = fileLocation + "/Invoice_" + invoice.getInvoiceNumber() + ".pdf";
	String contextPath = this.servlet.getServletContext().getRealPath("/");
	MessageResources messageResources = CommonConstants.loadMessageResources();
	new ArRedInvoicePdfCreator().createReport(invoice, fileName, contextPath, messageResources, user);
	PrintWriter out = response.getWriter();
	out.print(fileName);
	out.flush();
	out.close();
	return null;
    }
}
