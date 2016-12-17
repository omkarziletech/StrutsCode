package com.gp.cvst.logisoft.struts.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.bc.accounting.AccountPayableBC;
import com.gp.cong.logisoft.domain.User;
import com.gp.cvst.logisoft.struts.form.AccountPayableForm;
import com.logiware.bean.AccountingBean;
import com.logiware.bean.CustomerBean;
import com.logiware.hibernate.dao.AccountingTransactionDAO;
import com.logiware.reports.AccountPayableReportCreator;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.actions.DispatchAction;

public class AccountPayableAction extends DispatchAction {

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	AccountPayableForm accountPayableForm = (AccountPayableForm) form;
	User loginUser = (User) request.getSession().getAttribute("loginuser");
	AccountingTransactionDAO accountingTransactionDAO = new AccountingTransactionDAO();
	String conditions = accountingTransactionDAO.buildAccountPayablesQuery(accountPayableForm, loginUser);
	List<AccountingBean> accountPayables = accountingTransactionDAO.getAccountPayables(conditions);
	boolean checkVendor = CommonUtils.isNotEmpty(accountPayableForm.getVendornumber());
	CustomerBean vendorDetails = accountingTransactionDAO.getCustomerDetailsForAccountPayables(checkVendor,conditions);
	request.setAttribute("accountPayables", accountPayables);
	request.setAttribute("vendorDetails", vendorDetails);
	return mapping.findForward("success");
    }

    public ActionForward print(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	AccountPayableForm accountPayableForm = (AccountPayableForm) form;
        accountPayableForm.setInvoicenumber((null!=accountPayableForm.getInvoicenumber()? accountPayableForm.getInvoicenumber().toUpperCase() : accountPayableForm.getInvoicenumber()));
	User loginUser = (User) request.getSession().getAttribute("loginuser");
	AccountingTransactionDAO accountingTransactionDAO = new AccountingTransactionDAO();
	String conditions = accountingTransactionDAO.buildAccountPayablesQuery(accountPayableForm, loginUser);
	List<AccountingBean> accountPayables = accountingTransactionDAO.getAccountPayables(conditions);
	boolean checkVendor = CommonUtils.isNotEmpty(accountPayableForm.getVendornumber());
	CustomerBean vendorDetails = accountingTransactionDAO.getCustomerDetailsForAccountPayables(checkVendor,conditions);
	String contextPath = this.getServlet().getServletContext().getRealPath("/");
	request.setAttribute("fileName", new AccountPayableReportCreator().createReport(accountPayableForm, accountPayables, vendorDetails, contextPath));
	return mapping.findForward("success");
    }

    public ActionForward showTransactionDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	AccountPayableForm accountPayableForm = (AccountPayableForm) form;
	String vendorNo = request.getParameter("vendorNumber");
	request.setAttribute("charges", new AccountPayableBC().getTransactionLedgerDetailsForPopUp(vendorNo, accountPayableForm.getInvoicenumber(), accountPayableForm.getBlNumber(), accountPayableForm.getTransactionType()));
	return mapping.findForward(CommonConstants.TRANSACTION_DETAILS_PAGE);
    }

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	AccountPayableForm accountPayableForm = (AccountPayableForm) form;
	User loginUser = (User) request.getSession().getAttribute("loginuser");
	String payIndex = accountPayableForm.getPayIndex();
	String holdIndex = accountPayableForm.getHoldIndex();
	String releaseIndex = accountPayableForm.getReleaseIndex();
	if (CommonUtils.isNotEmpty(payIndex)) {
	    payIndex = StringUtils.removeStart(StringUtils.removeEnd(payIndex, ","),",");
	}
	if (CommonUtils.isNotEmpty(holdIndex)) {
	    holdIndex = StringUtils.removeStart(StringUtils.removeEnd(holdIndex, ","),",");
	}
	if (CommonUtils.isNotEmpty(releaseIndex)) {
	    releaseIndex = StringUtils.removeStart(StringUtils.removeEnd(releaseIndex, ","),",");
	}
	accountPayableForm.setFeedbackMessage(new AccountPayableBC().updateTransactionStatus(payIndex, holdIndex, releaseIndex, loginUser));
	accountPayableForm.setShowHold("Yes");
	AccountingTransactionDAO accountingTransactionDAO = new AccountingTransactionDAO();
	String conditions = accountingTransactionDAO.buildAccountPayablesQuery(accountPayableForm, loginUser);
	List<AccountingBean> accountPayables = accountingTransactionDAO.getAccountPayables(conditions);
	boolean checkVendor = CommonUtils.isNotEmpty(accountPayableForm.getVendornumber());
	CustomerBean vendorDetails = accountingTransactionDAO.getCustomerDetailsForAccountPayables(checkVendor,conditions);
	request.setAttribute("accountPayables", accountPayables);
	request.setAttribute("vendorDetails", vendorDetails);
	return mapping.findForward("success");
    }

    public ActionForward refresh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	AccountPayableForm accountPayableForm = new AccountPayableForm();
	request.setAttribute("accountPayableForm", accountPayableForm);
	return mapping.findForward("success");
    }

    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	return refresh(mapping, form, request, response);
    }
}
