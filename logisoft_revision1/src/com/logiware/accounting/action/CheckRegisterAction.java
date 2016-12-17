package com.logiware.accounting.action;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.Transaction;
import com.gp.cvst.logisoft.domain.TransactionLedger;
import com.gp.cvst.logisoft.hibernate.dao.BankDetailsDAO;
import com.gp.cvst.logisoft.hibernate.dao.TransactionDAO;
import com.logiware.accounting.dao.CheckRegisterDAO;
import com.logiware.accounting.excel.CheckRegisterExcelCreator;
import com.logiware.accounting.exception.AccountingException;
import com.logiware.accounting.form.ApPaymentForm;
import com.logiware.accounting.form.CheckRegisterForm;
import com.logiware.accounting.model.InvoiceModel;
import com.logiware.accounting.model.PaymentModel;
import com.logiware.accounting.reports.CheckCreator;
import com.logiware.accounting.reports.CheckRegisterReportCreator;
import com.logiware.hibernate.dao.AccountingLedgerDAO;
import com.logiware.hibernate.dao.ApTransactionHistoryDAO;
import com.logiware.hibernate.dao.ArTransactionHistoryDAO;
import com.logiware.hibernate.domain.ApTransactionHistory;
import com.logiware.hibernate.domain.ArTransactionHistory;
import com.logiware.utils.AuditNotesUtils;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Lakshmi Narayanan
 */
public class CheckRegisterAction extends BaseAction {

    private static final String INVOICE_DETAILS = "details";

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        CheckRegisterForm checkRegisterForm = (CheckRegisterForm) form;
        new CheckRegisterDAO().search(checkRegisterForm);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward clearAll(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        CheckRegisterForm checkRegisterForm = new CheckRegisterForm();
        request.setAttribute("checkRegisterForm", checkRegisterForm);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward showInvoiceDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        CheckRegisterForm checkRegisterForm = (CheckRegisterForm) form;
        new CheckRegisterDAO().setInvoiceList(checkRegisterForm);
        request.setAttribute("writeMode", Boolean.valueOf(request.getParameter("writeMode")));
        return mapping.findForward(INVOICE_DETAILS);
    }

    public ActionForward createReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = null;
        try {
            out = response.getWriter();
            CheckRegisterForm checkRegisterForm = (CheckRegisterForm) form;
            new CheckRegisterDAO().setInvoiceList(checkRegisterForm);
            String fileName = new CheckRegisterReportCreator(checkRegisterForm, this.servlet.getServletContext().getRealPath("/")).create();
            out.print(fileName);
            return null;
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != out) {
                out.flush();
                out.close();
            }
        }
    }

    public ActionForward createExcel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = null;
        try {
            out = response.getWriter();
            CheckRegisterForm checkRegisterForm = (CheckRegisterForm) form;
            new CheckRegisterDAO().setInvoiceList(checkRegisterForm);
            String fileName = new CheckRegisterExcelCreator(checkRegisterForm).create();
            out.print(fileName);
            return null;
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != out) {
                out.flush();
                out.close();
            }
        }
    }

    public void voidPayment(PaymentModel paymentModel, CheckRegisterForm form) throws Exception {
        TransactionDAO transactionDAO = new TransactionDAO();
        AccountingLedgerDAO accountingLedgerDAO = new AccountingLedgerDAO();
        ArTransactionHistoryDAO arTransactionHistoryDAO = new ArTransactionHistoryDAO();
        ApTransactionHistoryDAO apTransactionHistoryDAO = new ApTransactionHistoryDAO();
        Date now = new Date();
        StringBuilder noteDesc = new StringBuilder();
        String currentDate = DateUtils.formatDate(now, "MM/dd/yyyy");
        for (String id : paymentModel.getIds().split(",")) {
            Transaction payment = transactionDAO.findById(Integer.parseInt(id));
            if (CommonUtils.isNotEqualIgnoreCase(payment.getVoidTransaction(), YES)) {
                String originalIds = transactionDAO.getTransactionIdsForVoid(payment);
                for (String invoiceId : StringUtils.split(StringUtils.removeStart(StringUtils.removeEnd(originalIds, ","), ","), ",")) {
                    Transaction invoice = transactionDAO.findById(Integer.parseInt(invoiceId));
                    String moduleId;
                    Double paymentAmount;
                    String invoiceOrBl;
                    noteDesc.delete(0, noteDesc.length());
                    noteDesc.append(payment.getPaymentMethod().toUpperCase()).append(" Payment for ");
                    if (CommonUtils.isEqualIgnoreCase(invoice.getTransactionType(), TRANSACTION_TYPE_ACCOUNT_PAYABLE)) {
                        moduleId = NotesConstants.AP_INVOICE;
                        paymentAmount = payment.getTransactionAmt();
                        invoiceOrBl = payment.getInvoiceNumber();
                        noteDesc.append("Invoice '");
                    } else {
                        moduleId = NotesConstants.AR_INVOICE;
                        paymentAmount = -payment.getTransactionAmt();
                        invoiceOrBl = CommonUtils.isNotEmpty(payment.getBillLaddingNo()) ? payment.getBillLaddingNo() : payment.getInvoiceNumber();
                        noteDesc.append("Invoice/BL '");
                    }
                    String moduleRefId = payment.getCustNo() + "-" + invoiceOrBl;
                    noteDesc.append(invoiceOrBl).append("'");
                    noteDesc.append(" of Vendor '").append(payment.getCustName()).append("(").append(payment.getCustNo()).append(")'");
                    noteDesc.append(" for amount '").append(NumberUtils.formatNumber(paymentAmount)).append("'");
                    noteDesc.append(" voided by ").append(form.getUser().getLoginName()).append(" on ").append(currentDate);
                    AuditNotesUtils.insertAuditNotes(noteDesc.toString(), moduleId, moduleRefId, moduleId, form.getUser());

                    invoice.setStatus(STATUS_READY_TO_PAY);
                    invoice.setChequeNumber(null);
                    invoice.setBankName(null);
                    invoice.setBankAccountNumber(null);
                    invoice.setApBatchId(null);
                    invoice.setPaymentMethod(null);
                    invoice.setApprovedBy(null);
                    invoice.setPaidBy(null);
                    invoice.setUpdatedOn(now);
                    invoice.setUpdatedBy(form.getUser().getUserId());
                    if (CommonUtils.isEqualIgnoreCase(invoice.getTransactionType(), TRANSACTION_TYPE_ACCOUNT_RECEIVABLE)) {
                        ArTransactionHistory arTransactionHistory = new ArTransactionHistory();
                        arTransactionHistory.setCustomerNumber(invoice.getCustNo());
                        arTransactionHistory.setBlNumber(invoice.getBillLaddingNo());
                        arTransactionHistory.setInvoiceNumber(invoice.getInvoiceNumber());
                        arTransactionHistory.setInvoiceDate(invoice.getTransactionDate());
                        arTransactionHistory.setTransactionDate(now);
                        arTransactionHistory.setPostedDate(now);
                        arTransactionHistory.setTransactionAmount(-payment.getTransactionAmt());
                        arTransactionHistory.setCustomerReferenceNumber(invoice.getCustomerReferenceNo());
                        arTransactionHistory.setVoyageNumber(invoice.getVoyageNo());
                        arTransactionHistory.setCheckNumber(invoice.getChequeNumber());
                        arTransactionHistory.setArBatchId(null);
                        arTransactionHistory.setApBatchId(payment.getApBatchId());
                        arTransactionHistory.setTransactionType("AP PY");
                        arTransactionHistory.setCreatedBy(form.getUser().getLoginName());
                        arTransactionHistory.setCreatedDate(now);
                        arTransactionHistoryDAO.save(arTransactionHistory);

                        TransactionLedger subledger = new TransactionLedger();
                        PropertyUtils.copyProperties(subledger, payment);
                        subledger.setTransactionAmt(-payment.getTransactionAmt());
                        subledger.setBalance(0d);
                        subledger.setBalanceInProcess(0d);
                        subledger.setGlAccountNumber(LoadLogisoftProperties.getProperty(AR_CONTROL_ACCOUNT));
                        subledger.setTransactionType(TRANSACTION_TYPE_CASH_DEPOSIT);
                        subledger.setStatus(STATUS_PAID);
                        subledger.setCleared(NO);
                        subledger.setPostedDate(now);
                        subledger.setTransactionDate(now);
                        subledger.setSubledgerSourceCode(SUB_LEDGER_CODE_CASH_DEPOSIT);
                        subledger.setCreatedOn(now);
                        subledger.setCreatedBy(form.getUser().getUserId());
                        subledger.setUpdatedOn(null);
                        subledger.setUpdatedBy(null);
                        accountingLedgerDAO.save(subledger);
                        invoice.setBalance(invoice.getBalance() - payment.getTransactionAmt());
                        invoice.setBalanceInProcess(invoice.getBalance() - payment.getTransactionAmt());
                        invoice.setClosedDate(null);
                    } else if (CommonUtils.isEqualIgnoreCase(invoice.getTransactionType(), TRANSACTION_TYPE_ACCOUNT_PAYABLE)) {
                        ApTransactionHistory apTransactionHistory = new ApTransactionHistory(payment);
                        apTransactionHistory.setInvoiceDate(invoice.getTransactionDate());
                        apTransactionHistory.setPostedDate(now);
                        apTransactionHistory.setTransactionDate(now);
                        apTransactionHistory.setAmount(payment.getTransactionAmt());
                        apTransactionHistory.setCreatedBy(form.getUser().getLoginName());
                        apTransactionHistoryDAO.save(apTransactionHistory);
                        invoice.setBalance(payment.getTransactionAmt());
                        invoice.setBalanceInProcess(payment.getTransactionAmt());
                    }
                }
                payment.setVoidDate(now);
                payment.setVoidTransaction(YES);
                payment.setUpdatedOn(now);
                payment.setUpdatedBy(form.getUser().getUserId());
            } else {
                String key = PAYMENT_METHOD_CHECK.equalsIgnoreCase(paymentModel.getPaymentMethod()) ? paymentModel.getCheckNumber() : paymentModel.getBatchId();
                noteDesc.delete(0, noteDesc.length());
                noteDesc.append(paymentModel.getPaymentMethod()).append(" Payment - '").append(key).append("'");
                noteDesc.append(" of Vendor '").append(paymentModel.getVendorName()).append("(").append(paymentModel.getVendorNumber()).append(")'");
                throw new AccountingException(noteDesc.toString() + " is already voided.");
            }
        }
        noteDesc.delete(0, noteDesc.length());
        String key = PAYMENT_METHOD_CHECK.equalsIgnoreCase(paymentModel.getPaymentMethod()) ? paymentModel.getCheckNumber() : paymentModel.getBatchId();
        String moduleId = PAYMENT_METHOD_CHECK.equalsIgnoreCase(paymentModel.getPaymentMethod()) ? paymentModel.getCheckNumber() : (paymentModel.getVendorNumber() + "-" + paymentModel.getBatchId());
        noteDesc.append(paymentModel.getPaymentMethod()).append(" Payment - '").append(key).append("'");
        noteDesc.append(" of Vendor '").append(paymentModel.getVendorName()).append("(").append(paymentModel.getVendorNumber()).append(")'");
        noteDesc.append(" for amount '").append(paymentModel.getPaymentAmount()).append("'");
        noteDesc.append(" voided by ").append(form.getUser().getLoginName()).append(" on ").append(currentDate);
        AuditNotesUtils.insertAuditNotes(noteDesc.toString(), NotesConstants.AP_PAYMENT, moduleId, NotesConstants.AP_PAYMENT, form.getUser());

        TransactionLedger subledger = new TransactionLedger();
        subledger.setApBatchId(Integer.parseInt(paymentModel.getBatchId()));
        subledger.setBankAccountNumber(paymentModel.getBankAccount());
        subledger.setBankName(paymentModel.getBankName());
        subledger.setTransactionDate(now);
        subledger.setPostedDate(now);
        Double voidedAmount = Double.parseDouble(paymentModel.getPaymentAmount().replaceAll(",", ""));
        subledger.setTransactionAmt(voidedAmount);
        subledger.setBalance(voidedAmount);
        subledger.setBalanceInProcess(voidedAmount);
        subledger.setGlAccountNumber(paymentModel.getGlAccount());
        subledger.setSubledgerSourceCode(SUB_LEDGER_CODE_CASH_DEPOSIT);
        subledger.setTransactionType(TRANSACTION_TYPE_CASH_DEPOSIT);
        subledger.setStatus(STATUS_PAID);
        subledger.setCleared(NO);
        subledger.setCustomerReferenceNo(paymentModel.getBatchId());
        subledger.setCreatedOn(now);
        subledger.setCreatedBy(form.getUser().getUserId());
        accountingLedgerDAO.save(subledger);
    }

    private void savePrint(String fileLocation, String moduleId, String printerName, User user) throws Exception {
        EmailSchedulerVO print = new EmailSchedulerVO();
        print.setFileLocation(fileLocation);
        print.setStatus(EMAIL_STATUS_PENDING);
        print.setModuleName("APPAYMENT");
        print.setModuleId(moduleId);
        print.setType(CONTACT_MODE_PRINT);
        print.setHtmlMessage("Check Printing");
        print.setSubject("Check Printing");
        print.setUserName(user.getLoginName());
        print.setEmailDate(new Date());
        print.setPrinterName(printerName);
        print.setPrintCopy(1);
        new EmailschedulerDAO().save(print);
    }

    public void reprintPayment(PaymentModel paymentModel, CheckRegisterForm form, String contextPath) throws Exception {
        BankDetailsDAO bankDetailsDAO = new BankDetailsDAO();
        TransactionDAO transactionDAO = new TransactionDAO();
        Date now = new Date();
        StringBuilder noteDesc = new StringBuilder();
        List<InvoiceModel> invoiceList = new ArrayList<InvoiceModel>();
        String currentDate = DateUtils.formatDate(now, "MM/dd/yyyy");
        for (String id : paymentModel.getIds().split(",")) {
            Transaction payment = transactionDAO.findById(Integer.parseInt(id));
            String originalIds = transactionDAO.getTransactionIdsForVoid(payment);
            for (String invoiceId : StringUtils.split(StringUtils.removeStart(StringUtils.removeEnd(originalIds, ","), ","), ",")) {
                Transaction invoice = transactionDAO.findById(Integer.parseInt(invoiceId));
                Double paymentAmount;
                String invoiceOrBl;
                if (CommonUtils.isEqualIgnoreCase(invoice.getTransactionType(), TRANSACTION_TYPE_ACCOUNT_PAYABLE)) {
                    paymentAmount = payment.getTransactionAmt();
                    invoiceOrBl = payment.getInvoiceNumber();
                } else {
                    paymentAmount = -payment.getTransactionAmt();
                    invoiceOrBl = CommonUtils.isNotEmpty(payment.getBillLaddingNo()) ? payment.getBillLaddingNo() : payment.getInvoiceNumber();
                }
                InvoiceModel invoiceModel = new InvoiceModel();
                invoiceModel.setInvoiceOrBl(invoiceOrBl);
                invoiceModel.setInvoiceDate(DateUtils.formatDate(invoice.getTransactionDate(), "MM/dd/yyyy"));
                invoiceModel.setInvoiceAmount(NumberUtils.formatNumber(paymentAmount));
                invoiceModel.setPaidBy(form.getUser());
                invoiceList.add(invoiceModel);
            }
            payment.setReprintDate(now);
            payment.setReprint(YES);
            payment.setUpdatedOn(now);
            payment.setUpdatedBy(form.getUser().getUserId());
        }
        noteDesc.delete(0, noteDesc.length());
        noteDesc.append(PAYMENT_METHOD_CHECK).append(" Payment for '").append(paymentModel.getCheckNumber()).append("'");
        noteDesc.append(" of Vendor '").append(paymentModel.getVendorName()).append("(").append(paymentModel.getVendorNumber()).append(")'");
        noteDesc.append(" for amount '").append(paymentModel.getPaymentAmount()).append("'");
        noteDesc.append(" reprinted by ").append(form.getUser().getLoginName()).append(" on ").append(currentDate);
        AuditNotesUtils.insertAuditNotes(noteDesc.toString(), NotesConstants.AP_PAYMENT, paymentModel.getCheckNumber(), NotesConstants.AP_PAYMENT, form.getUser());
        form.setInvoiceList(invoiceList);
        Integer checkNumber = Integer.parseInt(paymentModel.getCheckNumber());
        ApPaymentForm apPaymentForm = new ApPaymentForm();
        apPaymentForm.setPaymentDate(paymentModel.getPaymentDate());
        apPaymentForm.setInvoiceList(invoiceList);
        String[] files = new CheckCreator(apPaymentForm, paymentModel, contextPath).create(paymentModel.getCheckNumber());
        String[] printers = bankDetailsDAO.getPrinters(paymentModel.getBankName(), paymentModel.getBankAccount());
        savePrint(files[0], paymentModel.getCheckNumber(), printers[0], form.getUser());
        if (CommonUtils.isNotEmpty(files[1]) && CommonUtils.isNotEmpty(printers[1])) {
            savePrint(files[1], String.valueOf(checkNumber), printers[1], form.getUser());
        }
    }

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        CheckRegisterForm checkRegisterForm = (CheckRegisterForm) form;
        String contextPath = this.getServlet().getServletContext().getRealPath("/");
        for (PaymentModel paymentModel : checkRegisterForm.getPaymentList()) {
            if (null != paymentModel.getVoided() && paymentModel.getVoided()) {
                voidPayment(paymentModel, checkRegisterForm);
            } else {
                reprintPayment(paymentModel, checkRegisterForm, contextPath);
            }
        }
        return search(mapping, checkRegisterForm, request, response);
    }
}
