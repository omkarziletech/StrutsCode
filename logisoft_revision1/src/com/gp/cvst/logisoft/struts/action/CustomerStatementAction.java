package com.gp.cvst.logisoft.struts.action;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.gp.cong.logisoft.bc.accounting.CustomerStatementBC;
import com.gp.cvst.logisoft.struts.form.CustomerStatementForm;
import com.oreilly.servlet.ServletUtils;
import org.apache.commons.io.FilenameUtils;

public class CustomerStatementAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	CustomerStatementForm customerStatementForm = (CustomerStatementForm) form;
	String buttonValue = customerStatementForm.getButtonValue();
	request.setAttribute("from", request.getParameter("from"));
	if ("generateStatement".equals(buttonValue)) {
	    String contextPath = this.getServlet().getServletContext().getRealPath("/");
	    request.setAttribute("fileName", new CustomerStatementBC().createStatement(customerStatementForm, contextPath));
	    if (request.getParameter("eStatement") != null) {
		request.setAttribute("eStatement", request.getParameter("eStatement"));
	    }
	    request.setAttribute("customerStatementForm", customerStatementForm);
	} else if ("emailStatement".equals(buttonValue)) {
	    String contextPath = this.getServlet().getServletContext().getRealPath("/");
	    request.setAttribute("statementLocation", new CustomerStatementBC().createStatement(customerStatementForm, contextPath));
	    if (request.getParameter("eStatement") != null) {
		request.setAttribute("eStatement", request.getParameter("eStatement"));
	    }
	    request.setAttribute("customerStatementForm", customerStatementForm);
	} else if (CommonConstants.ESTATEMENT_TO_EXCEL.equals(buttonValue)) {
	    String contextPath = this.getServlet().getServletContext().getRealPath("/");
	    String excelFileName = new CustomerStatementBC().exportToExcel(customerStatementForm, contextPath);
	    if (CommonUtils.isNotEmpty(excelFileName)) {
		response.addHeader("Content-Disposition", "attachment; filename=" + FilenameUtils.getName(excelFileName));
		response.setContentType("application/vnd.ms-excel" + ";charset=utf-8");
		ServletUtils.returnFile(excelFileName, response.getOutputStream());
	    }
	    return null;
	} else if ("configurationReport".equals(buttonValue)) {
	    String excelFileName = new CustomerStatementBC().configurationReport();
	    if (CommonUtils.isNotEmpty(excelFileName)) {
		response.addHeader("Content-Disposition", "attachment; filename=" + FilenameUtils.getName(excelFileName));
		response.setContentType("application/vnd.ms-excel" + ";charset=utf-8");
		ServletUtils.returnFile(excelFileName, response.getOutputStream());
	    }
	    return null;
	} else {
	    customerStatementForm = new CustomerStatementForm();
	    customerStatementForm.setStmtWithCredit(true);
	    customerStatementForm.setIncludeInvoiceCredit(true);
	    request.setAttribute("customerStatementForm", customerStatementForm);
	}
	return mapping.findForward("success");
    }
}
