package com.gp.cvst.logisoft.struts.action;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.domain.RoleDuty;
import java.util.Date;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.Transaction;
import com.gp.cvst.logisoft.struts.form.ArCreditHoldForm;
import com.logiware.bean.AccountingBean;
import com.logiware.hibernate.dao.AccountingTransactionDAO;
import com.logiware.utils.ArCreditHoldUtils;
import com.logiware.utils.AuditNotesUtils;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.actions.DispatchAction;
import org.apache.velocity.util.StringUtils;

public class ArCreditHoldAction extends DispatchAction implements NotesConstants {

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception {
	ArCreditHoldForm arCreditHoldForm = (ArCreditHoldForm) form;
	AccountingTransactionDAO accountingTransactionDAO = new AccountingTransactionDAO();
	RoleDuty roleDuty = (RoleDuty) request.getSession().getAttribute("roleDuty");
        String conditions ="";
        if(null!=arCreditHoldForm && null!=roleDuty){
            conditions = accountingTransactionDAO.buildArCreditHoldQuery(arCreditHoldForm,roleDuty.isCreditHoldOpsUser());
	Integer noOfRecords = accountingTransactionDAO.getNoOfArCreditHoldTransactions(conditions);
	int noOfPages = noOfRecords / arCreditHoldForm.getCurrentPageSize();
	int remainSize = noOfRecords % arCreditHoldForm.getCurrentPageSize();
	if (remainSize > 0) {
	    noOfPages += 1;
	}
	arCreditHoldForm.setNoOfPages(noOfPages);
	arCreditHoldForm.setTotalPageSize(noOfRecords);
	int currentPageSize = arCreditHoldForm.getCurrentPageSize();
	int pageNo = arCreditHoldForm.getPageNo();
	String sortBy = arCreditHoldForm.getSortBy();
	String orderBy = arCreditHoldForm.getOrderBy();
	List<AccountingBean> transactions = accountingTransactionDAO.getArCreditHoldTransactions(conditions, currentPageSize, pageNo, sortBy, orderBy);
	arCreditHoldForm.setNoOfRecords(transactions.size());
	arCreditHoldForm.setTransactions(transactions);
        }
	return mapping.findForward("success");
    }

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	ArCreditHoldForm arCreditHoldForm = (ArCreditHoldForm) form;
	    User loginUser = (User) request.getSession().getAttribute("loginuser");
	    ServletContext servletContext = this.servlet.getServletContext();
	    String imagePath = "http://" + servletContext.getResource(LoadLogisoftProperties.getProperty("application.image.logo")).getPath();
	    StringBuilder message = new StringBuilder();
	    AccountingTransactionDAO accountingTransactionDAO = new AccountingTransactionDAO();
	    if (CommonUtils.isNotEmpty(arCreditHoldForm.getRemoveFromHoldIds())) {
		String[] transactionIds = StringUtils.split(arCreditHoldForm.getRemoveFromHoldIds(), ",");
		int releaseCount = transactionIds.length;
		int emailCount = 0;
		for (String transactionId : transactionIds) {
		    Transaction transaction = accountingTransactionDAO.findById(Integer.parseInt(transactionId));
		    if (transaction.isEmailed()) {
			ArCreditHoldUtils.sendEmail(transaction, loginUser, false, imagePath);
			emailCount++;
		    }
		    transaction.setRemovedFromHold(true);
		    transaction.setEmailed(false);
		    transaction.setCreditHold(CommonConstants.NO);
		    String invoiceOrBl = CommonUtils.isNotEmpty(transaction.getBillLaddingNo()) ? transaction.getBillLaddingNo() : transaction.getInvoiceNumber();
		    StringBuilder desc = new StringBuilder("BL# - ").append(invoiceOrBl).append(" of ");
		    desc.append(transaction.getCustName()).append("(").append(transaction.getCustNo()).append(")");
		    desc.append(" taken off credit hold by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
		    AuditNotesUtils.insertAuditNotes(desc.toString(), AR_INVOICE, transaction.getCustNo() + "-" + invoiceOrBl, "AR Credit Hold", loginUser);
		    accountingTransactionDAO.save(transaction);
		}
		message.append(releaseCount).append(releaseCount == 1 ? " BL is" : " BLs are").append(" released from hold");
		if (emailCount > 0) {
		    message.append(" and ").append(emailCount).append(" release").append(emailCount == 1 ? " email is" : " emails are").append(" sent");
		}
	    }
	    if (CommonUtils.isNotEmpty(arCreditHoldForm.getPutOnHoldIds())) {
		String[] transactionIds = StringUtils.split(arCreditHoldForm.getPutOnHoldIds(), ",");
		int holdCount = transactionIds.length;
		for (String transactionId : transactionIds) {
		    Transaction transaction = accountingTransactionDAO.findById(Integer.parseInt(transactionId));
		    ArCreditHoldUtils.sendEmail(transaction, loginUser, true, imagePath);
		    transaction.setEmailed(true);
		    transaction.setCreditHold(CommonConstants.YES);
		    String invoiceOrBl = CommonUtils.isNotEmpty(transaction.getBillLaddingNo()) ? transaction.getBillLaddingNo() : transaction.getInvoiceNumber();
		    StringBuilder desc = new StringBuilder("BL# - ").append(invoiceOrBl).append(" of ");
		    desc.append(transaction.getCustName()).append("(").append(transaction.getCustNo()).append(")");
		    desc.append(" placed on credit hold by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
		    AuditNotesUtils.insertAuditNotes(desc.toString(), AR_INVOICE, transaction.getCustNo() + "-" + invoiceOrBl, "AR Credit Hold", loginUser);
		    accountingTransactionDAO.save(transaction);
		}
		if (CommonUtils.isNotEmpty(arCreditHoldForm.getRemoveFromHoldIds())) {
		    message.append("<br/>and ");
		}
		message.append(holdCount).append(holdCount == 1 ? " BL is" : " BLs are").append(" placed on hold");
		message.append(" and ").append(holdCount).append(" hold").append(holdCount == 1 ? " email is" : " emails are").append(" sent");
	    }
	    message.append(" successfully");
	    request.setAttribute("message", message.toString());
	return search(mapping, form, request, response);
    }

    public ActionForward refresh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	ArCreditHoldForm arCreditHoldForm = new ArCreditHoldForm();
	RoleDuty roleDuty = (RoleDuty) request.getSession().getAttribute("roleDuty");
	if(!roleDuty.isCreditHoldOpsUser()){
	    User loginUser = (User) request.getSession().getAttribute("loginuser");
	    arCreditHoldForm.setCollectorName(loginUser.getLoginName());
	}
	request.setAttribute("arCreditHoldForm", arCreditHoldForm);
	return mapping.findForward("success");
    }

    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	return refresh(mapping, form, request, response);
    }
}
