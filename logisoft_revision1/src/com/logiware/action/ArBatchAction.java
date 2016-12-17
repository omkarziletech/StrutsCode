package com.logiware.action;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.domain.ProcessInfo;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.ProcessInfoDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cvst.logisoft.AccountingConstants;
import com.gp.cvst.logisoft.domain.ArBatch;
import com.gp.cvst.logisoft.hibernate.dao.BankDetailsDAO;
import com.gp.cvst.logisoft.hibernate.dao.PaymentChecksDAO;
import com.gp.cvst.logisoft.hibernate.dao.PaymentsDAO;
import com.logiware.accounting.exception.AccountingException;
import com.logiware.bean.AccountingBean;
import com.logiware.bean.ArBatchBean;
import com.logiware.bean.PaymentBean;
import com.logiware.bean.PaymentCheckBean;
import com.logiware.excel.DiscardedInvoicesExcelCreator;
import com.logiware.form.ArBatchForm;
import com.logiware.hibernate.dao.ArBatchDAO;
import com.logiware.reports.ArBatchReportCreator;
import com.logiware.utils.ArBatchUtils;
import com.logiware.utils.AuditNotesUtils;
import com.oreilly.servlet.ServletUtils;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/**
 *
 * @author Lakshmi Narayanan
 */
public class ArBatchAction extends DispatchAction implements ConstantsInterface {

    private static final String ADD_AR_BATCH = "addArBatch";
    private static final String APPLY_PAYMENTS = "applyPayments";

    public ActionForward searchArBatch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ArBatchForm arBatchForm = (ArBatchForm) form;
        User loginUser = (User) request.getSession().getAttribute(LOGIN_USER);
        if (!arBatchForm.isSearchByUser() && CommonUtils.isEmpty(arBatchForm.getUser())) {
            arBatchForm.setUser(loginUser.getLoginName());
        }
        ArBatchDAO arBatchDAO = new ArBatchDAO();
        String conditions = arBatchDAO.buildArBatchSearchQuery(arBatchForm);
        Integer totalPageSize = arBatchDAO.getTotalArBatches(conditions);
        int noOfPages = totalPageSize / arBatchForm.getCurrentPageSize();
        int remainSize = totalPageSize % arBatchForm.getCurrentPageSize();
        if (remainSize > 0) {
            noOfPages += 1;
        }
        int start = (arBatchForm.getCurrentPageSize() * (arBatchForm.getPageNo() - 1));
        int end = arBatchForm.getCurrentPageSize();
        arBatchForm.setNoOfPages(noOfPages);
        arBatchForm.setTotalPageSize(totalPageSize);
        List<ArBatchBean> arBatches = arBatchDAO.getArBatches(arBatchForm.getSortBy(), arBatchForm.getOrderBy(), conditions, start, end);
        arBatchForm.setNoOfRecords(arBatches.size());
        arBatchForm.setArBatches(arBatches);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward printArBatch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ArBatchForm arBatchForm = (ArBatchForm) form;
        String contextPath = this.getServlet().getServletContext().getRealPath("/");
        ArBatchDAO arBatchDAO = new ArBatchDAO();
        Integer batchId = Integer.parseInt(arBatchForm.getSelectedBatchId());
        ArBatchBean arBatchBean = arBatchDAO.getArBatch(batchId);
        List<PaymentCheckBean> checks = arBatchDAO.getPaymentChecks(batchId);
        TreeMap<PaymentCheckBean, List<PaymentBean>> paymentChecks = new TreeMap<PaymentCheckBean, List<PaymentBean>>(new CheckComparator());
        if (CommonUtils.isNotEmpty(checks)) {
            for (PaymentCheckBean paymentCheck : checks) {
                List<PaymentBean> payments = arBatchDAO.getPaymentsForCheck(arBatchForm.getSelectedBatchId(), paymentCheck.getCheckId());
                paymentChecks.put(paymentCheck, payments);
            }
        }
        request.setAttribute("fileName", new ArBatchReportCreator().createReport(contextPath, arBatchBean, paymentChecks));
        return searchArBatch(mapping, form, request, response);
    }

    public ActionForward exportNSInvoice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ArBatchForm arBatchForm = (ArBatchForm) form;
        ArBatchUtils.exportNSInvoice(arBatchForm.getSelectedBatchId(), response);
        return searchArBatch(mapping, form, request, response);
    }

    public ActionForward printNSInvoice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ArBatchForm arBatchForm = (ArBatchForm) form;
        String contextPath = this.getServlet().getServletContext().getRealPath("/");
        ArBatchUtils.printNSInvoice(arBatchForm.getSelectedBatchId(), contextPath, request);
        return searchArBatch(mapping, form, request, response);
    }

    public ActionForward voidArBatch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        synchronized(this){
            ArBatchForm arBatchForm = (ArBatchForm) form;
            User loginUser = (User) request.getSession().getAttribute(LOGIN_USER);
            ArBatchDAO arBatchDAO = new ArBatchDAO();
            arBatchDAO.voidArBatch(arBatchForm.getSelectedBatchId(), loginUser.getUserId());
            String batchType = arBatchDAO.getBatchType(arBatchForm.getSelectedBatchId());
            StringBuilder desc = new StringBuilder(batchType).append(" Batch - ").append(arBatchForm.getSelectedBatchId());
            desc.append(" voided by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
            AuditNotesUtils.insertAuditNotes(desc.toString(),
                    NotesConstants.AR_BATCH, arBatchForm.getSelectedBatchId(), NotesConstants.AR_BATCH, loginUser);
            new ProcessInfoDAO().deleteProcessInfo(loginUser.getUserId(), "AR_BATCH", "INVOICE_BL");
            return searchArBatch(mapping, form, request, response);
        }
    }

    public ActionForward addArBatch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ArBatchForm arBatchForm = (ArBatchForm) form;
        arBatchForm.setBatchId("");
        arBatchForm.setBatchBalance("0.00");
        arBatchForm.setBatchAmount("0.00");
        User loginUser = (User) request.getSession().getAttribute(LOGIN_USER);
        request.setAttribute("bankAccounts", new BankDetailsDAO().getBankAccounts(loginUser.getUserId()));
        arBatchForm.setAddBatchUser(loginUser.getLoginName());
        arBatchForm.setDepositDate(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
        return mapping.findForward(ADD_AR_BATCH);
    }

    public ActionForward editArBatch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ArBatchForm oldArBatchForm = (ArBatchForm) form;        
        ArBatch arBatch = new ArBatchDAO().findById(Integer.parseInt(oldArBatchForm.getSelectedBatchId()));        
        ArBatchForm newArBatchForm = new ArBatchForm(arBatch);
        newArBatchForm.setSearchByUser(oldArBatchForm.isSearchByUser());
        newArBatchForm.setUser(oldArBatchForm.getUser());
        User loginUser = (User) request.getSession().getAttribute(LOGIN_USER);
        request.setAttribute("bankAccounts", new BankDetailsDAO().getBankAccounts(loginUser.getUserId()));
        request.setAttribute("arBatchForm", newArBatchForm);
        return mapping.findForward(ADD_AR_BATCH);
    }

    public ActionForward createOrUpdateArBatch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        synchronized (this) {
            ArBatchForm arBatchForm = (ArBatchForm) form;
            User loginUser = (User) request.getSession().getAttribute(LOGIN_USER);
            String notesInForm = arBatchForm.getNotes();
            String notesInDB = null;
            if (CommonUtils.isNotEmpty(arBatchForm.getBatchId())) {
                ArBatch arBatch = new ArBatchDAO().findById(Integer.parseInt(arBatchForm.getBatchId()));
                notesInDB = arBatch.getNotes();
                arBatch.setTotalAmount(Double.parseDouble(arBatchForm.getBatchAmount()));
                arBatch.setBalanceAmount(Double.parseDouble(arBatchForm.getBatchBalance()));
                arBatch.setDirectGlAccount(arBatchForm.isDirectGlAccount());
                arBatch.setBankAccount(arBatchForm.getBankAccount());
                arBatch.setBankAcctDesc(arBatchForm.getDescription());
                arBatch.setGlAccountNo(arBatchForm.getGlAccount());
                arBatch.setDepositDate(DateUtils.parseDate(arBatchForm.getDepositDate(), "MM/dd/yyyy"));
                arBatch.setNotes(arBatchForm.getNotes());
                new ProcessInfoDAO().deleteProcessInfo(loginUser.getUserId(), "AR_BATCH");
                request.setAttribute("message", "Batch - " + arBatchForm.getBatchId() + " updated successfully");
            } else {
                ArBatch arBatch = new ArBatch(arBatchForm);
                arBatchForm.setBatchId(new ArBatchDAO().saveAndReturnId(arBatch).toString());
                request.setAttribute("message", "Batch - " + arBatchForm.getBatchId() + " saved successfully");
                String batchType = "DEPOSIT";
                if (arBatchForm.isNetsettlement()) {
                    batchType = "NET SETT";
                }
                StringBuilder desc = new StringBuilder(batchType).append(" Batch - ").append(arBatch.getBatchId());
                desc.append(" initially added by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
                AuditNotesUtils.insertAuditNotes(desc.toString(),
                        NotesConstants.AR_BATCH, arBatch.getBatchId().toString(), NotesConstants.AR_BATCH, loginUser);
            }
            if (CommonUtils.isNotEmpty(notesInForm) && CommonUtils.isNotEqualIgnoreCase(notesInForm, notesInDB)) {
                AuditNotesUtils.insertAuditNotes("Notes :" + notesInForm, NotesConstants.AR_BATCH, arBatchForm.getBatchId(), NotesConstants.AR_BATCH, loginUser);
            }
            return searchArBatch(mapping, form, request, response);
        }
    }

    public ActionForward createOrUpdateAndApplyPayments(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        synchronized (this) {
            ArBatchForm arBatchForm = (ArBatchForm) form;
            User loginUser = (User) request.getSession().getAttribute(LOGIN_USER);
            ArBatch arBatch = null;
            String notesInForm = arBatchForm.getNotes();
            String notesInDB = null;
            if (CommonUtils.isNotEmpty(arBatchForm.getBatchId())) {
                arBatch = new ArBatchDAO().findById(Integer.parseInt(arBatchForm.getBatchId()));
                notesInDB = arBatch.getNotes();
                arBatch.setTotalAmount(Double.parseDouble(arBatchForm.getBatchAmount()));
                arBatch.setBalanceAmount(Double.parseDouble(arBatchForm.getBatchBalance()));
                arBatch.setDirectGlAccount(arBatchForm.isDirectGlAccount());
                arBatch.setBankAccount(arBatchForm.getBankAccount());
                arBatch.setBankAcctDesc(arBatchForm.getDescription());
                arBatch.setGlAccountNo(arBatchForm.getGlAccount());
                arBatch.setDepositDate(DateUtils.parseDate(arBatchForm.getDepositDate(), "MM/dd/yyyy"));
                arBatch.setNotes(arBatchForm.getNotes());
                request.setAttribute("message", "Batch - " + arBatchForm.getBatchId() + " updated successfully");
            } else {
                arBatch = new ArBatch(arBatchForm);
                arBatchForm.setBatchId(new ArBatchDAO().saveAndReturnId(arBatch).toString());
                request.setAttribute("message", "Batch - " + arBatchForm.getBatchId() + " saved successfully");
                String batchType = "DEPOSIT";
                if (arBatchForm.isNetsettlement()) {
                    batchType = "NET SETT";
                }
                StringBuilder desc = new StringBuilder(batchType).append(" Batch - ").append(arBatch.getBatchId());
                desc.append(" initially added by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
                AuditNotesUtils.insertAuditNotes(desc.toString(),
                        NotesConstants.AR_BATCH, arBatch.getBatchId().toString(), NotesConstants.AR_BATCH, loginUser);
                ProcessInfo processInfo = new ProcessInfo();
                processInfo.setRecordid(arBatch.getBatchId().toString());
                processInfo.setProgramid(arBatch.getBatchId());
                processInfo.setUserid(loginUser.getUserId());
                processInfo.setModuleId("AR_BATCH");
                processInfo.setAction("AR_BATCH");
                processInfo.setProcessinfodate(new Date());
                new ProcessInfoDAO().save(processInfo);
            }
            arBatchForm.setBatchId(arBatch.getBatchId().toString());
            arBatchForm.setBatchType(arBatch.getBatchType());
            arBatchForm.setBatchAmount(NumberUtils.formatNumber(arBatch.getTotalAmount(), "0.00"));
            arBatchForm.setBatchBalance(NumberUtils.formatNumber(arBatch.getBalanceAmount(), "0.00"));
            if (arBatch.getBatchType().equals(AccountingConstants.AR_NET_SETT_BATCH)) {
                arBatchForm.setCheckNumber("NETSETT-" + arBatch.getBatchId().toString());
            }
            arBatchForm.setCheckTotal("0.00");
            arBatchForm.setCheckApplied("0.00");
            arBatchForm.setCheckBalance("0.00");
            arBatchForm.setNoOfPages(0);
            arBatchForm.setNoOfRecords(0);
            arBatchForm.setSortBy("");
            arBatchForm.setOrderBy("");
            arBatchForm.setPageNo(1);
            if (CommonUtils.isNotEmpty(notesInForm) && CommonUtils.isNotEqualIgnoreCase(notesInForm, notesInDB)) {
                AuditNotesUtils.insertAuditNotes("Notes :" + notesInForm, NotesConstants.AR_BATCH, arBatchForm.getBatchId(), NotesConstants.AR_BATCH, loginUser);
            }
            return mapping.findForward(APPLY_PAYMENTS);
        }
    }

    public ActionForward goBack(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ArBatchForm arBatchForm = (ArBatchForm) form;
        User loginUser = (User) request.getSession().getAttribute(LOGIN_USER);
        if (CommonUtils.isNotEmpty(arBatchForm.getBatchId())) {
            new ProcessInfoDAO().deleteProcessInfo(loginUser.getUserId(), "AR_BATCH");
        }
        arBatchForm.setBatchAmount("0.00");
        return searchArBatch(mapping, form, request, response);
    }

    public ActionForward refreshArBatch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ArBatchForm arBatchForm = new ArBatchForm();
        request.setAttribute("arBatchForm", arBatchForm);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward applyPayments(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ArBatchForm arBatchForm = (ArBatchForm) form;
        ArBatchDAO arBatchDAO = new ArBatchDAO();
        ArBatch arBatch = arBatchDAO.findById(Integer.parseInt(arBatchForm.getSelectedBatchId()));
        ArBatchForm newArBatchForm = new ArBatchForm();
        newArBatchForm.setBatchId(arBatch.getBatchId().toString());
        newArBatchForm.setBatchType(arBatch.getBatchType());
        newArBatchForm.setBatchAmount(NumberUtils.formatNumber(arBatch.getTotalAmount(), "0.00"));
        newArBatchForm.setBatchBalance(NumberUtils.formatNumber(arBatch.getBalanceAmount(), "0.00"));
        newArBatchForm.setUser(arBatchForm.getUser());
        newArBatchForm.setSearchByUser(arBatchForm.isSearchByUser());
        if (arBatch.getBatchType().equals(AccountingConstants.AR_NET_SETT_BATCH)) {
            newArBatchForm.setCheckNumber("NETSETT-" + arBatch.getBatchId().toString());
        }
        request.setAttribute("arBatchForm", newArBatchForm);
        return mapping.findForward(APPLY_PAYMENTS);
    }

    public ActionForward searchApplyPayments(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ArBatchForm arBatchForm = (ArBatchForm) form;
        ArBatchDAO arBatchDAO = new ArBatchDAO();
        String customerNumber = null;
        if (CommonUtils.isNotEmpty(arBatchForm.getOtherCustomerNumber())) {
            customerNumber = arBatchForm.getOtherCustomerNumber();
            arBatchForm.setOtherCustomer(true);
        } else if (CommonUtils.isNotEmpty(arBatchForm.getCustomerNumber())) {
            customerNumber = arBatchForm.getCustomerNumber();
        }
        if (null != customerNumber) {
            TradingPartner tradingPartner = new TradingPartnerDAO().findById(arBatchForm.getCustomerNumber());
            if (null != tradingPartner && CommonUtils.isEqualIgnoreCase(tradingPartner.getType(), "master")) {
                arBatchForm.setMaster(true);
                if (!arBatchForm.isShowAssociatedCompanies()) {
                    arBatchForm.setShowAssociatedCompanies(true);
                }
            } else {
                arBatchForm.setShowAssociatedCompanies(false);
            }
        }
        List<AccountingBean> appliedTransactions = new ArrayList<AccountingBean>();
        StringBuilder arTransactionIds = new StringBuilder();
        StringBuilder apTransactionIds = new StringBuilder();
        StringBuilder acTransactionIds = new StringBuilder();
        for (PaymentBean paymentBean : arBatchForm.getAppliedTransactions()) {
            if (paymentBean.getTransactionId().endsWith(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE)) {
                arTransactionIds.append(paymentBean.getTransactionId().replace(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE, "")).append(",");
                appliedTransactions.add(arBatchDAO.getAppliedArTransaction(paymentBean));
            } else if (paymentBean.getTransactionId().endsWith(TRANSACTION_TYPE_ACCOUNT_PAYABLE)) {
                apTransactionIds.append(paymentBean.getTransactionId().replace(TRANSACTION_TYPE_ACCOUNT_PAYABLE, "")).append(",");
                appliedTransactions.add(arBatchDAO.getAppliedApTransaction(paymentBean));
            } else {
                acTransactionIds.append(paymentBean.getTransactionId().replace(TRANSACTION_TYPE_ACCRUALS, "")).append(",");
                appliedTransactions.add(arBatchDAO.getAppliedAcTransaction(paymentBean));
            }
        }
        arBatchForm.setArTransactionIds(StringUtils.removeEnd(arTransactionIds.toString(), ","));
        arBatchForm.setApTransactionIds(StringUtils.removeEnd(apTransactionIds.toString(), ","));
        arBatchForm.setAcTransactionIds(StringUtils.removeEnd(acTransactionIds.toString(), ","));
        String arQuery = arBatchDAO.buildArQuery(arBatchForm);
        String apQuery = null;
        if ((CommonUtils.isNotEmpty(arBatchForm.getSearchBy()) && CommonUtils.isEmpty(arBatchForm.getSearchValue()))
                || (CommonUtils.isEqual(arBatchForm.getSearchBy(), SEARCH_BY_CHECK_NUMBER)
                && CommonUtils.isEqual(arBatchForm.getSearchBy(), SEARCH_BY_CHECK_AMOUNT))) {
            apQuery = null;
        } else if (arBatchForm.isShowPayables()
                || (CommonUtils.isNotEmpty(arBatchForm.getSearchBy()) && CommonUtils.isNotEmpty(arBatchForm.getSearchValue()))) {
            apQuery = arBatchDAO.buildApQuery(arBatchForm);
        }
        String acQuery = null;
        if ((CommonUtils.isNotEmpty(arBatchForm.getSearchBy()) && CommonUtils.isEmpty(arBatchForm.getSearchValue()))
                || CommonUtils.isEqual(arBatchForm.getSearchBy(), SEARCH_BY_CHECK_NUMBER)
                || CommonUtils.isEqual(arBatchForm.getSearchBy(), SEARCH_BY_CHECK_AMOUNT)) {
            acQuery = null;
        } else if ((CommonUtils.isNotEmpty(arBatchForm.getSearchBy()) && CommonUtils.isNotEmpty(arBatchForm.getSearchValue()))
                || arBatchForm.isShowAccruals() || arBatchForm.isShowAssignedAccruals() || arBatchForm.isShowInactiveAccruals()) {
            acQuery = arBatchDAO.buildAcQuery(arBatchForm);
        }
        List<AccountingBean> applypayments = new ArrayList<AccountingBean>();
        int totalPageSize = arBatchDAO.getTotalApplyPayments(arQuery, apQuery, acQuery);
        if (totalPageSize > 0) {
            applypayments.addAll(arBatchDAO.searchApplyPayments(arBatchForm, arQuery, apQuery, acQuery));
        }
        applypayments.addAll(appliedTransactions);
        arBatchForm.setApplypayments(applypayments);
        arBatchForm.setNoOfRecords(applypayments.size());
        totalPageSize += appliedTransactions.size();
        int currentPageSize = arBatchForm.getCurrentPageSize() + appliedTransactions.size();
        int noOfPages = totalPageSize / currentPageSize;
        int remainSize = totalPageSize % currentPageSize;
        if (remainSize > 0) {
            noOfPages += 1;
        }
        arBatchForm.setNoOfPages(noOfPages);
        arBatchForm.setTotalPageSize(totalPageSize);
        return mapping.findForward(APPLY_PAYMENTS);
    }

    public ActionForward saveImportedInvoices(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        synchronized (this) {
            ArBatchForm arBatchForm = (ArBatchForm) form;
            User loginUser = (User) request.getSession().getAttribute(LOGIN_USER);
            ArBatchDAO arBatchDAO = new ArBatchDAO();
            PaymentsDAO paymentsDAO = new PaymentsDAO();
            PaymentChecksDAO paymentChecksDAO = new PaymentChecksDAO();
            //Get ArBatch from database using batchId
            ArBatch arBatch = arBatchDAO.findById(Integer.parseInt(arBatchForm.getBatchId()));
            String action = "saved";
            if (CommonUtils.isNotEmpty(arBatchForm.getPaymentCheckId())) {
                action = "updated";
                arBatchForm.setPaymentCheckId(ArBatchUtils.updateApplyPayments(arBatchForm, arBatch, loginUser));
            } else {
                arBatchForm.setPaymentCheckId(ArBatchUtils.saveApplyPayments(arBatchForm, arBatch, loginUser));
            }
            arBatch.setAppliedAmount(paymentsDAO.getPaymentAmountFromPayments(arBatch.getBatchId()));
            arBatch.setOnAcctAmount(paymentsDAO.getOnAccountAmountFromPayments(arBatch.getBatchId()));
            arBatch.setPrepayAmount(paymentsDAO.getPrePaymentAmountFromPayments(arBatch.getBatchId()));
            arBatch.setAdjustAmount(paymentsDAO.getAdjustAmountFromPayments(arBatch.getBatchId()));
            double totalCheckAmount = paymentChecksDAO.getCheckAmountFromChecks(arBatch.getBatchId());
            double balance = arBatch.getTotalAmount() - totalCheckAmount;
            if (arBatch.getBatchType().equals(AccountingConstants.AR_CASH_BATCH)) {
                arBatch.setBalanceAmount(balance);
            }
            StringBuilder messageBuilder = new StringBuilder("Batch - ");
            messageBuilder.append(arBatchForm.getBatchId()).append(" updated successfully<br>");
            messageBuilder.append("Check - ").append(arBatchForm.getCheckNumber()).append(" ").append(action).append(" successfully");
            request.setAttribute("message", messageBuilder.toString());
            arBatchForm.setBatchId(arBatch.getBatchId().toString());
            arBatchForm.setBatchType(arBatch.getBatchType());
            arBatchForm.setBatchAmount(NumberUtils.formatNumber(arBatch.getTotalAmount(), "0.00"));
            arBatchForm.setBatchBalance(NumberUtils.formatNumber(arBatch.getBalanceAmount(), "0.00"));
            arBatchForm.setUser(arBatchForm.getUser());
            arBatchForm.setSearchByUser(arBatchForm.isSearchByUser());
            new ProcessInfoDAO().deleteProcessInfo(loginUser.getUserId(), "AR_BATCH", "INVOICE_BL");
            if (CommonUtils.isNotEmpty(arBatchForm.getDiscardedTransactions())) {
                String fileName = new DiscardedInvoicesExcelCreator().createExcel(arBatchForm.getCustomerNumber(), arBatchForm.getDiscardedTransactions());
                request.setAttribute("fileName", fileName);
            }
            arBatchForm.setSelectedBatchId(arBatchForm.getBatchId());
            return editCheck(mapping, arBatchForm, request, response);
        }
    }

    public ActionForward exportDiscardedInvoices(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fileName = request.getParameter("fileName");
        response.addHeader("Content-Disposition", "attachment; filename=" + FilenameUtils.getName(fileName));
        response.setContentType("application/vnd.ms-excel" + ";charset=utf-8");
        ServletUtils.returnFile(fileName, response.getOutputStream());
        return null;
    }

    public ActionForward saveApplyPayments(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        synchronized (this) {
            ArBatchForm arBatchForm = (ArBatchForm) form;
            User loginUser = (User) request.getSession().getAttribute(LOGIN_USER);
            ArBatchDAO arBatchDAO = new ArBatchDAO();
            PaymentsDAO paymentsDAO = new PaymentsDAO();
            PaymentChecksDAO paymentChecksDAO = new PaymentChecksDAO();
            //Get ArBatch from database using batchId
            ArBatch arBatch = arBatchDAO.findById(Integer.parseInt(arBatchForm.getBatchId()));
            String action = "saved";
            if (CommonUtils.isNotEmpty(arBatchForm.getPaymentCheckId())) {
                action = "updated";
                arBatchForm.setPaymentCheckId(ArBatchUtils.updateApplyPayments(arBatchForm, arBatch, loginUser));
            } else {
                arBatchForm.setPaymentCheckId(ArBatchUtils.saveApplyPayments(arBatchForm, arBatch, loginUser));
            }
            arBatch.setAppliedAmount(paymentsDAO.getPaymentAmountFromPayments(arBatch.getBatchId()));
            arBatch.setOnAcctAmount(paymentsDAO.getOnAccountAmountFromPayments(arBatch.getBatchId()));
            arBatch.setPrepayAmount(paymentsDAO.getPrePaymentAmountFromPayments(arBatch.getBatchId()));
            arBatch.setAdjustAmount(paymentsDAO.getAdjustAmountFromPayments(arBatch.getBatchId()));
            double totalCheckAmount = paymentChecksDAO.getCheckAmountFromChecks(arBatch.getBatchId());
            double balance = arBatch.getTotalAmount() - totalCheckAmount;
            if (arBatch.getBatchType().equals(AccountingConstants.AR_CASH_BATCH)) {
                arBatch.setBalanceAmount(balance);
            }
            if (CommonUtils.isEmpty(balance) || arBatch.getBatchType().equals(AccountingConstants.AR_NET_SETT_BATCH)) {
                StringBuilder messageBuilder = new StringBuilder("Batch - ");
                messageBuilder.append(arBatchForm.getBatchId()).append(" updated successfully<br>");
                messageBuilder.append("Check - ").append(arBatchForm.getCheckNumber()).append(" ").append(action).append(" successfully");
                request.setAttribute("message", messageBuilder.toString());
                arBatchForm.setNoOfPages(0);
                arBatchForm.setNoOfRecords(0);
                arBatchForm.setSortBy("");
                arBatchForm.setOrderBy("");
                arBatchForm.setPageNo(1);
                arBatchForm.setBatchAmount("0.00");
                new ProcessInfoDAO().deleteProcessInfo(loginUser.getUserId(), "AR_BATCH");
                return searchArBatch(mapping, form, request, response);
            } else {
                StringBuilder messageBuilder = new StringBuilder("Batch - ");
                messageBuilder.append(arBatchForm.getBatchId()).append(" updated successfully<br>");
                messageBuilder.append("Check - ").append(arBatchForm.getCheckNumber()).append(" ").append(action).append(" successfully");
                request.setAttribute("message", messageBuilder.toString());
                ArBatchForm newArBatchForm = new ArBatchForm();
                newArBatchForm.setBatchId(arBatch.getBatchId().toString());
                newArBatchForm.setBatchType(arBatch.getBatchType());
                newArBatchForm.setBatchAmount(NumberUtils.formatNumber(arBatch.getTotalAmount(), "0.00"));
                newArBatchForm.setBatchBalance(NumberUtils.formatNumber(arBatch.getBalanceAmount(), "0.00"));
                newArBatchForm.setUser(arBatchForm.getUser());
                newArBatchForm.setSearchByUser(arBatchForm.isSearchByUser());
                request.setAttribute("arBatchForm", newArBatchForm);
                new ProcessInfoDAO().deleteProcessInfo(loginUser.getUserId(), "AR_BATCH", "INVOICE_BL");
                return mapping.findForward(APPLY_PAYMENTS);
            }
        }
    }

    public ActionForward autoSaveApplyPayments(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        synchronized (this) {
            PrintWriter out = response.getWriter();
            ArBatchForm arBatchForm = (ArBatchForm) form;
            User loginUser = (User) request.getSession().getAttribute(LOGIN_USER);
            ArBatchDAO arBatchDAO = new ArBatchDAO();
            PaymentsDAO paymentsDAO = new PaymentsDAO();
            PaymentChecksDAO paymentChecksDAO = new PaymentChecksDAO();
            //Get ArBatch from database using batchId
            ArBatch arBatch = arBatchDAO.findById(Integer.parseInt(arBatchForm.getBatchId()));
            if (CommonUtils.isNotEmpty(arBatchForm.getPaymentCheckId())) {
                out.write(ArBatchUtils.updateApplyPayments(arBatchForm, arBatch, loginUser));
            } else {
                out.write(ArBatchUtils.saveApplyPayments(arBatchForm, arBatch, loginUser));
            }
            arBatch.setAppliedAmount(paymentsDAO.getPaymentAmountFromPayments(arBatch.getBatchId()));
            arBatch.setOnAcctAmount(paymentsDAO.getOnAccountAmountFromPayments(arBatch.getBatchId()));
            arBatch.setPrepayAmount(paymentsDAO.getPrePaymentAmountFromPayments(arBatch.getBatchId()));
            arBatch.setAdjustAmount(paymentsDAO.getAdjustAmountFromPayments(arBatch.getBatchId()));
            double totalCheckAmount = paymentChecksDAO.getCheckAmountFromChecks(arBatch.getBatchId());
            double balance = arBatch.getTotalAmount() - totalCheckAmount;
            if (arBatch.getBatchType().equals(AccountingConstants.AR_CASH_BATCH)) {
                arBatch.setBalanceAmount(balance);
            }
            out.flush();
            out.close();
            return null;
        }
    }

    public ActionForward editCheck(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ArBatchForm arBatchForm = (ArBatchForm) form;
        ArBatchDAO arBatchDAO = new ArBatchDAO();
        ArBatch arBatch = arBatchDAO.findById(Integer.parseInt(arBatchForm.getSelectedBatchId()));
        arBatchForm.setBatchId(arBatch.getBatchId().toString());
        arBatchForm.setBatchType(arBatch.getBatchType());
        arBatchForm.setBatchAmount(NumberUtils.formatNumber(arBatch.getTotalAmount(), "0.00"));
        arBatchForm.setBatchBalance(NumberUtils.formatNumber(arBatch.getBalanceAmount(), "0.00"));
        PaymentCheckBean paymentCheck = arBatchDAO.getPaymentCheck(Integer.parseInt(arBatchForm.getPaymentCheckId()));
        arBatchForm.setCheckNumber(paymentCheck.getCheckNumber());
        arBatchForm.setCheckTotal(paymentCheck.getCheckAmount().replace(",", ""));
        arBatchForm.setCheckApplied(paymentCheck.getAppliedAmount().replace(",", ""));
        arBatchForm.setNoOfInvoices(paymentCheck.getNoOfInvoices());
        if (arBatch.getBatchType().equals(AccountingConstants.AR_NET_SETT_BATCH)) {
            arBatchForm.setCheckBalance("0.00");
        } else {
            arBatchForm.setCheckBalance(paymentCheck.getCheckBalance().replace(",", ""));
        }
        arBatchForm.setSortBy("");
        arBatchForm.setOrderBy("");
        arBatchForm.setPageNo(1);
        arBatchForm.setCustomerNumber(paymentCheck.getCustomerNumber());
        arBatchForm.setCustomerName(paymentCheck.getCustomerName());
        if (CommonUtils.isNotEmpty(arBatchForm.getCustomerNumber())) {
            TradingPartner tradingPartner = new TradingPartnerDAO().findById(arBatchForm.getCustomerNumber());
            if (null != tradingPartner && CommonUtils.isEqualIgnoreCase(tradingPartner.getType(), "master")) {
                arBatchForm.setMaster(true);
                if (!arBatchForm.isShowAssociatedCompanies()) {
                    arBatchForm.setShowAssociatedCompanies(true);
                }
            } else {
                arBatchForm.setShowAssociatedCompanies(false);
            }
        }
        PaymentBean appliedOnAccount = arBatchDAO.getAppliedOnAccount(paymentCheck.getCheckId(), arBatchForm.getBatchId());
        if (null != appliedOnAccount) {
            arBatchForm.setOnAccountApplied(true);
            arBatchForm.setAppliedOnAccount(appliedOnAccount);
        }
        List<PaymentBean> appliedPrepayments = arBatchDAO.getAppliedPrepayments(paymentCheck.getCheckId(), arBatchForm.getBatchId());
        if (CommonUtils.isNotEmpty(appliedPrepayments)) {
            arBatchForm.setPrepaymentApplied(true);
            arBatchForm.setAppliedPrepayments(appliedPrepayments);
        }
        List<PaymentBean> appliedGlAccounts = arBatchDAO.getAppliedGlAccounts(paymentCheck.getCheckId(), arBatchForm.getBatchId());
        if (CommonUtils.isNotEmpty(appliedGlAccounts)) {
            arBatchForm.setChargeCodeApplied(true);
            arBatchForm.setAppliedGLAccounts(appliedGlAccounts);
        }
        List<AccountingBean> applypayments = new ArrayList<AccountingBean>();
        applypayments.addAll(arBatchDAO.getAppliedArTransactions(paymentCheck.getCheckId(), arBatchForm.getBatchId()));
        applypayments.addAll(arBatchDAO.getAppliedApTransactions(paymentCheck.getCheckId(), arBatchForm.getBatchId()));
        applypayments.addAll(arBatchDAO.getAppliedAcTransactions(paymentCheck.getCheckId(), arBatchForm.getBatchId()));
        arBatchForm.setApplypayments(applypayments);
        arBatchForm.setNoOfPages(1);
        arBatchForm.setNoOfRecords(applypayments.size());
        arBatchForm.setTotalPageSize(applypayments.size());
        return mapping.findForward(APPLY_PAYMENTS);
    }

    public ActionForward postArBatch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        synchronized (this) {
            ArBatchForm arBatchForm = (ArBatchForm) form;
            User loginUser = (User) request.getSession().getAttribute(LOGIN_USER);
            ArBatchUtils.postArBatch(arBatchForm, request);
            request.setAttribute("message", "Batch - " + arBatchForm.getSelectedBatchId() + " is posted successfully");
            new ProcessInfoDAO().deleteProcessInfo(loginUser.getUserId(), "AR_BATCH");
            return searchArBatch(mapping, form, request, response);
        }
    }

    public ActionForward reversePost(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        synchronized (this) {
            ArBatchForm arBatchForm = (ArBatchForm) form;
            try {
                User loginUser = (User) request.getSession().getAttribute(LOGIN_USER);
                ArBatchUtils.reversePost(arBatchForm.getSelectedBatchId(), loginUser);
                request.setAttribute("message", "Batch - " + arBatchForm.getSelectedBatchId() + " is reversed successfully");
                new ProcessInfoDAO().deleteProcessInfo(loginUser.getUserId(), "AR_BATCH");
                return searchArBatch(mapping, form, request, response);
            } catch (AccountingException e) {
                request.setAttribute("error", "Cannot Reverse the batch - " + arBatchForm.getSelectedBatchId() + " : " + e.getMessage());
                return searchArBatch(mapping, form, request, response);
            } catch (Exception e) {
                throw e;
            }
        }
    }

    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return searchArBatch(mapping, form, request, response);
    }
}

class CheckComparator implements java.util.Comparator<PaymentCheckBean> {

    @Override
    public int compare(PaymentCheckBean o1, PaymentCheckBean o2) {
        Integer i1 = Integer.parseInt(o1.getCheckId());
        Integer i2 = Integer.parseInt(o2.getCheckId());
        return i1.compareTo(i2);
    }
}
