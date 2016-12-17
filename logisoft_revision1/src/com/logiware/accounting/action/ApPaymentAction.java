package com.logiware.accounting.action;

import com.google.gson.Gson;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.ApBatch;
import com.gp.cvst.logisoft.domain.Transaction;
import com.gp.cvst.logisoft.domain.TransactionLedger;
import com.gp.cvst.logisoft.hibernate.dao.ApBatchDAO;
import com.gp.cvst.logisoft.hibernate.dao.BankDetailsDAO;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.gp.cvst.logisoft.hibernate.dao.TransactionDAO;
import com.logiware.accounting.dao.ApPaymentDAO;
import com.logiware.accounting.dao.FiscalPeriodDAO;
import com.logiware.accounting.exception.AccountingException;
import com.logiware.accounting.form.ApPaymentForm;
import com.logiware.accounting.model.InvoiceModel;
import com.logiware.accounting.model.PaymentModel;
import com.logiware.accounting.reports.CheckCreator;
import com.logiware.accounting.thread.ApBatchThread;
import com.logiware.accounting.thread.CardCountThread;
import com.logiware.accounting.thread.CheckNumberThread;
import com.logiware.common.model.OptionModel;
import com.logiware.hibernate.dao.AccountingLedgerDAO;
import com.logiware.hibernate.dao.ApTransactionHistoryDAO;
import com.logiware.hibernate.dao.ArTransactionHistoryDAO;
import com.logiware.hibernate.domain.ApTransactionHistory;
import com.logiware.hibernate.domain.ArTransactionHistory;
import com.logiware.utils.ArCreditHoldUtils;
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
public class ApPaymentAction extends BaseAction {

    private static final String INVOICES = "invoices";
    private static final String BATCH = "batch";

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ApPaymentForm apPaymentForm = (ApPaymentForm) form;
        new ApPaymentDAO().search(apPaymentForm);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward clearAll(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ApPaymentForm apPaymentForm = new ApPaymentForm();
        apPaymentForm.setUser((User) request.getSession().getAttribute("loginuser"));
        request.setAttribute("apPaymentForm", apPaymentForm);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward getBankAccount(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = null;
        try {
            out = response.getWriter();
            response.setContentType("application/json");
            ApPaymentForm apPaymentForm = (ApPaymentForm) form;
            List<OptionModel> bankAccountList = new BankDetailsDAO().getBankAccounts(apPaymentForm.getBankName(), apPaymentForm.getUser().getUserId());
            out.print(new Gson().toJson(bankAccountList));
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

    public ActionForward getStartingNumber(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = null;
        try {
            out = response.getWriter();
            ApPaymentForm apPaymentForm = (ApPaymentForm) form;
            Integer startingNumber = new BankDetailsDAO().getStartingNumber(apPaymentForm.getBankName(), apPaymentForm.getBankAccount());
            out.print(startingNumber);
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

    public ActionForward validatePaymentDate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = null;
        try {
            out = response.getWriter();
            ApPaymentForm apPaymentForm = (ApPaymentForm) form;
            String result = new FiscalPeriodDAO().validateDate(apPaymentForm.getPaymentDate(), apPaymentForm.getBankName(), apPaymentForm.getBankAccount());
            out.print(result);
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

    public ActionForward validatePrinters(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = null;
        try {
            out = response.getWriter();
            ApPaymentForm apPaymentForm = (ApPaymentForm) form;
            String result = new BankDetailsDAO().validatePrinters(apPaymentForm.getBankName(), apPaymentForm.getBankAccount());
            out.print(result);
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

    public ActionForward showInvoices(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ApPaymentForm apPaymentForm = (ApPaymentForm) form;
        if (CommonUtils.isNotEmpty(apPaymentForm.getIds())) {
            new ApPaymentDAO().setInvoiceList(apPaymentForm);
            request.setAttribute("writeMode", Boolean.valueOf(request.getParameter("writeMode")));
            return mapping.findForward(INVOICES);
        } else {
            PrintWriter out = null;
            try {
                out = response.getWriter();
                out.print("noresult");
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
    }

    public ActionForward removeInvoice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ApPaymentForm apPaymentForm = (ApPaymentForm) form;
        new ApPaymentDAO().removeInvoice(apPaymentForm);
        return showInvoices(mapping, apPaymentForm, request, response);
    }

    public ActionForward showBatch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        synchronized (this) {
            ApPaymentForm apPaymentForm = (ApPaymentForm) form;
            apPaymentForm.setBatchId(new ApBatchThread().getBatchId());
            return mapping.findForward(BATCH);
        }
    }

    private void sendEmail(ApPaymentForm form, HttpServletRequest request, PaymentModel paymentModel, Integer checkNumber, String status) throws Exception {
        UserDAO userDAO = new UserDAO();
        String toAddress;
        if (CommonUtils.isEqualIgnoreCase(status, STATUS_APPROVED)) {
            toAddress = new TradingPartnerDAO().getRemitEmailForPaymentMethod(paymentModel.getPaymentMethod(), paymentModel.getVendorNumber());
            if (CommonUtils.isEmpty(toAddress)) {
                toAddress = form.getUser().getEmail();
            }
        } else {
            toAddress = userDAO.getAchApproverEmails();
        }
        if (CommonUtils.isNotEmpty(toAddress)) {
            String imagePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
            String companyName = LoadLogisoftProperties.getProperty("application.ECU.companyname");
            String companyLogo = imagePath + LoadLogisoftProperties.getProperty("application.image.logo");
            String subject;
            StringBuilder textMessage = new StringBuilder();
            StringBuilder htmlMessage = new StringBuilder();
            htmlMessage.append("<html>");
            htmlMessage.append("<body>");
            htmlMessage.append("<center>");
            htmlMessage.append("<div style='width:600px;background-color:#E6F2FF;border:5px solid #4F8CE5'>");
            htmlMessage.append("<div style='width:600px;'>");
            htmlMessage.append("<img title='").append(companyName).append("' src='").append(companyLogo).append("'/>");
            htmlMessage.append("</div>");
            htmlMessage.append("<div style='width:600px;margin:0 auto;'>");
            htmlMessage.append("<div style='font-weight: bold;'>");
            if (CommonUtils.isEqualIgnoreCase(status, STATUS_APPROVED)) {
                subject = "Disbursment notification from " + companyName + " to " + paymentModel.getVendorName();
                htmlMessage.append("A payment in the amount of $").append(paymentModel.getPaymentAmount());
                htmlMessage.append(" was made by ").append(paymentModel.getPaymentMethod()).append(" on ").append(form.getPaymentDate()).append(".");
                textMessage.append("A payment in the amount of $").append(paymentModel.getPaymentAmount());
                textMessage.append(" was made by ").append(paymentModel.getPaymentMethod()).append(" on ").append(form.getPaymentDate()).append(".");
            } else {
                subject = "ACH payments ready for approval";
                htmlMessage.append("The following ACH payments have been submitted for approval:");
                textMessage.append("The following ACH payments have been submitted for approval:");
            }
            if (CommonUtils.isEqualIgnoreCase(paymentModel.getPaymentMethod(), PAYMENT_METHOD_CHECK)) {
                htmlMessage.append("<br> Vendor : ").append(paymentModel.getVendorName());
                htmlMessage.append("<br> Check# : ").append(checkNumber);
                textMessage.append("\n Vendor : ").append(paymentModel.getVendorName());
                textMessage.append("\n Check# : ").append(checkNumber);
            } else {
                htmlMessage.append("<br> Vendor : ").append(paymentModel.getVendorName());
                textMessage.append("\n Vendor : ").append(paymentModel.getVendorName());
            }
            htmlMessage.append("</div>");
            htmlMessage.append("<table width='600' cellspacing='1' cellpadding='0' border='0'>");
            htmlMessage.append("<tr style='background-color: #11539F;font-weight: bold;font-family : Arial;color:black;'>");
            htmlMessage.append("<td>Invoice/BL</td>").append("<td>Date</td>").append("<td>Amount</td>");
            htmlMessage.append("</tr>");
            textMessage.append("\nInvoice/BL\t\t").append("Date\t\t").append("Amount");
            boolean isOdd = true;
            User paidBy = null;
            for (InvoiceModel invoice : form.getInvoiceList()) {
                htmlMessage.append("<tr style='background-color: #").append(isOdd ? "d1e6ee" : "ffffff").append(";'>");
                isOdd = !isOdd;
                htmlMessage.append("<td>").append(invoice.getInvoiceOrBl()).append("</td>");
                htmlMessage.append("<td>").append(invoice.getInvoiceDate()).append("</td>");
                htmlMessage.append("<td>").append(invoice.getInvoiceAmount()).append("</td>");
                htmlMessage.append("</tr>");
                textMessage.append("\n").append(invoice.getInvoiceOrBl());
                textMessage.append("\t\t").append(invoice.getInvoiceDate());
                textMessage.append("\t\t").append(invoice.getInvoiceAmount());
                paidBy = invoice.getPaidBy();
            }
            htmlMessage.append("<tr>");
            htmlMessage.append("<td colspan='3'>").append(form.getInvoiceList().size()).append(" Invoices Paid").append("</td>");
            htmlMessage.append("</tr>");
            htmlMessage.append("</table>");
            htmlMessage.append("</div>");
            htmlMessage.append("<div><u>International Transportation Specialists</u></div>");
            htmlMessage.append("</div>");
            htmlMessage.append("</body>");
            htmlMessage.append("</center>");
            htmlMessage.append("</html>");
            textMessage.append("\n").append(form.getInvoiceList().size()).append(" Invoices Paid");
            textMessage.append("\n").append("International Transportation Specialists");
            EmailSchedulerVO email = new EmailSchedulerVO();
            if (null != paidBy && CommonUtils.isNotEmpty(paidBy.getEmail())) {
                email.setFromName(paidBy.getLoginName());
                email.setFromAddress(paidBy.getEmail());
            } else {
                email.setFromName(form.getUser().getLoginName());
                email.setFromAddress(form.getUser().getEmail());
            }
            email.setToName(null);
            email.setToAddress(toAddress);
            email.setCcAddress(userDAO.getApSpecialistEmail(paymentModel.getVendorNumber()));
            email.setSubject(subject);
            email.setHtmlMessage(htmlMessage.toString());
            email.setTextMessage(textMessage.toString());
            email.setName("APPAYMENT");
            email.setType(CONTACT_MODE_EMAIL);
            email.setStatus(EMAIL_STATUS_PENDING);
            email.setNoOfTries(0);
            email.setEmailDate(new Date());
            email.setModuleName("AP Payment");
            if (CommonUtils.isEqualIgnoreCase(paymentModel.getPaymentMethod(), PAYMENT_METHOD_CHECK)) {
                email.setModuleId(String.valueOf(checkNumber));
            } else {
                email.setModuleId(String.valueOf(form.getBatchId()));
            }
            email.setUserName(form.getUser().getLoginName());
            new EmailschedulerDAO().save(email);
        }
    }

    private void makeCheckPayment(ApPaymentForm form, HttpServletRequest request, PaymentModel paymentModel, Integer checkNumber, String glAccountNo) throws Exception {
        TransactionDAO transactionDAO = new TransactionDAO();
        AccountingLedgerDAO accountingLedgerDAO = new AccountingLedgerDAO();
        ArTransactionHistoryDAO arTransactionHistoryDAO = new ArTransactionHistoryDAO();
        ApTransactionHistoryDAO apTransactionHistoryDAO = new ApTransactionHistoryDAO();
        String imagePath = "http://" + this.servlet.getServletContext().getResource(LoadLogisoftProperties.getProperty("application.image.logo")).getPath();
        StringBuilder noteDesc = new StringBuilder();
        Date now = new Date();
        Date paymentDate = DateUtils.parseDate(form.getPaymentDate(), "MM/dd/yyyy");
        String currentDate = DateUtils.formatDate(now, "MM/dd/yyyy");
        List<InvoiceModel> invoiceList = new ArrayList<InvoiceModel>();
        Double totalPaymentAmount = 0d;
        for (String id : paymentModel.getIds().split(",")) {
            Transaction invoice = transactionDAO.findById(Integer.parseInt(id));
            if (NumberUtils.isNotZero(invoice.getBalance())) {
                invoice.setStatus(STATUS_PAID);
                invoice.setPaymentMethod(PAYMENT_METHOD_CHECK);
                invoice.setApBatchId(form.getBatchId());
                invoice.setArBatchId(null);
                invoice.setBankAccountNumber(form.getBankAccount());
                invoice.setBankName(form.getBankName());
                invoice.setChequeNumber(String.valueOf(checkNumber));
                invoice.setCheckDate(paymentDate);
                invoice.setPaidBy(form.getUser().getUserId());
                invoice.setPaidOn(now);
                String moduleId;
                Double paymentAmount;
                String invoiceOrBl;
                noteDesc.delete(0, noteDesc.length());
                noteDesc.append("Check Payment for ");
                if (CommonUtils.isEqualIgnoreCase(invoice.getTransactionType(), TRANSACTION_TYPE_ACCOUNT_PAYABLE)) {
                    totalPaymentAmount += invoice.getBalance();
                    moduleId = NotesConstants.AP_INVOICE;
                    paymentAmount = invoice.getBalance();
                    invoiceOrBl = invoice.getInvoiceNumber();
                    noteDesc.append("Invoice '");
                } else {
                    totalPaymentAmount += -invoice.getBalance();
                    moduleId = NotesConstants.AR_INVOICE;
                    paymentAmount = -invoice.getBalance();
                    invoiceOrBl = CommonUtils.isNotEmpty(invoice.getBillLaddingNo()) ? invoice.getBillLaddingNo() : invoice.getInvoiceNumber();
                    noteDesc.append("Invoice/BL '");
                    invoice.setClosedDate(now);
                }
                invoice.setBalance(0d);
                invoice.setBalanceInProcess(0d);
                String moduleRefId = invoice.getCustNo() + "-" + invoiceOrBl;
                noteDesc.append(invoiceOrBl).append("'");
                noteDesc.append(" of Vendor '").append(invoice.getCustName()).append("(").append(invoice.getCustNo()).append(")'");
                noteDesc.append(" for amount '").append(NumberUtils.formatNumber(paymentAmount)).append("'");
                noteDesc.append(" paid by ").append(form.getUser().getLoginName()).append(" on ").append(currentDate);
                AuditNotesUtils.insertAuditNotes(noteDesc.toString(), moduleId, moduleRefId, moduleId, form.getUser());

                InvoiceModel invoiceModel = new InvoiceModel();
                invoiceModel.setInvoiceOrBl(invoiceOrBl);
                invoiceModel.setInvoiceDate(DateUtils.formatDate(invoice.getTransactionDate(), "MM/dd/yyyy"));
                invoiceModel.setInvoiceAmount(NumberUtils.formatNumber(paymentAmount));
                invoiceModel.setPaidBy(form.getUser());
                invoiceList.add(invoiceModel);

                Transaction payment = new Transaction();
                PropertyUtils.copyProperties(payment, invoice);
                payment.setTransactionId(null);
                if (CommonUtils.isEqualIgnoreCase(invoice.getTransactionType(), TRANSACTION_TYPE_ACCOUNT_PAYABLE)) {
                    payment.setBillLaddingNo(null);
                }
                payment.setTransactionType(TRANSACTION_TYPE_PAYAMENT);
                payment.setStatus(STATUS_PAID);
                payment.setPaymentMethod(PAYMENT_METHOD_CHECK);
                payment.setApBatchId(form.getBatchId());
                payment.setBankAccountNumber(form.getBankAccount());
                payment.setBankName(form.getBankName());
                payment.setChequeNumber(String.valueOf(checkNumber));
                payment.setGlAccountNumber(glAccountNo);
                payment.setTransactionDate(paymentDate);
                payment.setPostedDate(paymentDate);
                payment.setReconciled(NO);
                payment.setReconciledDate(null);
                payment.setCleared(NO);
                payment.setClearedDate(null);
                payment.setVoidTransaction(NO);
                payment.setVoidDate(null);
                payment.setReprint(NO);
                payment.setReprintDate(null);
                payment.setBillTo(YES);
                payment.setTransactionAmt(paymentAmount);
                payment.setBalance(0d);
                payment.setBalanceInProcess(0d);
                payment.setCreatedOn(now);
                payment.setCreatedBy(form.getUser().getUserId());
                payment.setUpdatedOn(null);
                payment.setUpdatedBy(null);
                payment.setPaidBy(null);
                payment.setPaidOn(null);
                transactionDAO.save(payment);

                if (CommonUtils.isEqualIgnoreCase(invoice.getTransactionType(), TRANSACTION_TYPE_ACCOUNT_RECEIVABLE)) {
                    ArTransactionHistory arTransactionHistory = new ArTransactionHistory();
                    arTransactionHistory.setCustomerNumber(invoice.getCustNo());
                    arTransactionHistory.setBlNumber(invoice.getBillLaddingNo());
                    arTransactionHistory.setInvoiceNumber(invoice.getInvoiceNumber());
                    arTransactionHistory.setInvoiceDate(invoice.getTransactionDate());
                    arTransactionHistory.setTransactionDate(paymentDate);
                    arTransactionHistory.setPostedDate(paymentDate);
                    arTransactionHistory.setTransactionAmount(paymentAmount);
                    arTransactionHistory.setCustomerReferenceNumber(invoice.getCustomerReferenceNo());
                    arTransactionHistory.setVoyageNumber(invoice.getVoyageNo());
                    arTransactionHistory.setCheckNumber(invoice.getChequeNumber());
                    arTransactionHistory.setArBatchId(null);
                    arTransactionHistory.setApBatchId(form.getBatchId());
                    arTransactionHistory.setTransactionType("AP PY");
                    arTransactionHistory.setCreatedBy(form.getUser().getLoginName());
                    arTransactionHistory.setCreatedDate(now);
                    arTransactionHistoryDAO.save(arTransactionHistory);
                    if (CommonUtils.isEqualIgnoreCase(invoice.getCreditHold(), YES)) {
                        if (invoice.isEmailed()) {
                            ArCreditHoldUtils.sendEmail(invoice, form.getUser(), false, imagePath);
                        }
                        invoice.setRemovedFromHold(true);
                        invoice.setEmailed(false);
                        invoice.setCreditHold(NO);
                        noteDesc.delete(0, noteDesc.length());
                        noteDesc.append("Invoice/BL - ").append(invoiceOrBl).append(" of ");
                        noteDesc.append(invoice.getCustName()).append("(").append(invoice.getCustNo()).append(")");
                        noteDesc.append(" taken off credit hold by ").append(form.getUser().getLoginName()).append(" on ").append(currentDate);
                        AuditNotesUtils.insertAuditNotes(noteDesc.toString(), NotesConstants.AR_INVOICE, moduleRefId, "AR Credit Hold", form.getUser());
                    }

                    TransactionLedger subledger = new TransactionLedger();
                    PropertyUtils.copyProperties(subledger, invoice);
                    subledger.setTransactionAmt(paymentAmount);
                    subledger.setBalance(0d);
                    subledger.setBalanceInProcess(0d);
                    subledger.setGlAccountNumber(LoadLogisoftProperties.getProperty(AR_CONTROL_ACCOUNT));
                    subledger.setTransactionType(TRANSACTION_TYPE_CASH_DEPOSIT);
                    subledger.setStatus(STATUS_PAID);
                    subledger.setCleared(NO);
                    subledger.setBankAccountNumber(form.getBankAccount());
                    subledger.setBankName(form.getBankName());
                    subledger.setChequeNumber(String.valueOf(checkNumber));
                    subledger.setPostedDate(paymentDate);
                    subledger.setCustomerReferenceNo(String.valueOf(form.getBatchId()));
                    subledger.setTransactionDate(now);
                    subledger.setSubledgerSourceCode(SUB_LEDGER_CODE_CASH_DEPOSIT);
                    subledger.setApBatchId(form.getBatchId());
                    subledger.setCreatedOn(now);
                    subledger.setCreatedBy(form.getUser().getUserId());
                    subledger.setUpdatedOn(null);
                    subledger.setUpdatedBy(null);
                    accountingLedgerDAO.save(subledger);
                } else {
                    accountingLedgerDAO.setPaidDateForAccrualCost(invoice.getCustNo(), invoice.getInvoiceNumber(), DateUtils.formatDate(paymentDate, "yyyy-MM-dd"));
                    ApTransactionHistory apTransactionHistory = new ApTransactionHistory(payment);
                    apTransactionHistory.setInvoiceDate(invoice.getTransactionDate());
                    apTransactionHistory.setAmount(-paymentAmount);
                    apTransactionHistory.setCreatedBy(form.getUser().getLoginName());
                    apTransactionHistoryDAO.save(apTransactionHistory);
                }
            } else {
                throw new AccountingException("One or more invoices of " + paymentModel.getVendorNumber() + " is already paid.");
            }
        }
        if (CommonUtils.isNotEqual(totalPaymentAmount, Double.parseDouble(paymentModel.getPaymentAmount().replace(",", "")))) {
            throw new AccountingException("Payment Amount for " + paymentModel.getVendorNumber() + " is not equal to sum of all invoices.");
        }
        form.setInvoiceList(invoiceList);
        noteDesc.delete(0, noteDesc.length());
        noteDesc.append("Check Payment for '").append(checkNumber).append("'");
        noteDesc.append(" of Vendor '").append(paymentModel.getVendorName()).append("(").append(paymentModel.getVendorName()).append(")'");
        noteDesc.append(" for amount '").append(paymentModel.getPaymentAmount()).append("'");
        noteDesc.append(" paid by ").append(form.getUser().getLoginName()).append(" on ").append(currentDate);
        AuditNotesUtils.insertAuditNotes(noteDesc.toString(), NotesConstants.AP_PAYMENT, String.valueOf(checkNumber), NotesConstants.AP_PAYMENT, form.getUser());
        sendEmail(form, request, paymentModel, checkNumber, STATUS_APPROVED);
    }

    private void approveAchOrWirePayment(ApPaymentForm form, HttpServletRequest request, PaymentModel paymentModel, String glAccountNo) throws Exception {
        UserDAO userDAO = new UserDAO();
        TransactionDAO transactionDAO = new TransactionDAO();
        AccountingLedgerDAO accountingLedgerDAO = new AccountingLedgerDAO();
        ArTransactionHistoryDAO arTransactionHistoryDAO = new ArTransactionHistoryDAO();
        ApTransactionHistoryDAO apTransactionHistoryDAO = new ApTransactionHistoryDAO();
        String imagePath = "http://" + this.servlet.getServletContext().getResource(LoadLogisoftProperties.getProperty("application.image.logo")).getPath();
        StringBuilder noteDesc = new StringBuilder();
        Date now = new Date();
        Date paymentDate = DateUtils.parseDate(form.getPaymentDate(), "MM/dd/yyyy");
        String currentDate = DateUtils.formatDate(now, "MM/dd/yyyy");
        List<InvoiceModel> invoiceList = new ArrayList<InvoiceModel>();
        User paidBy = null;
        Date paidDate = null;
        Double totalPaymentAmount = 0d;
        for (String id : paymentModel.getIds().split(",")) {
            Transaction invoice = transactionDAO.findById(Integer.parseInt(id));
            if (NumberUtils.isNotZero(invoice.getBalance())) {
                invoice.setStatus(STATUS_PAID);
                invoice.setPaymentMethod(paymentModel.getPaymentMethod());
                invoice.setApBatchId(form.getBatchId());
                invoice.setArBatchId(null);
                invoice.setBankAccountNumber(form.getBankAccount());
                invoice.setBankName(form.getBankName());
                invoice.setChequeNumber(null);
                paidDate = paymentDate;
                if (null != invoice.getCheckDate()) {
                    paidDate = invoice.getCheckDate();
                }
                invoice.setCheckDate(paymentDate);
                if (!NumberUtils.isNotZero(invoice.getPaidBy())) {
                    invoice.setPaidBy(form.getUser().getUserId());
                }
                String moduleId;
                Double paymentAmount;
                String invoiceOrBl;
                noteDesc.delete(0, noteDesc.length());
                noteDesc.append(paymentModel.getPaymentMethod()).append(" Payment for ");
                if (CommonUtils.isEqualIgnoreCase(invoice.getTransactionType(), TRANSACTION_TYPE_ACCOUNT_PAYABLE)) {
                    totalPaymentAmount += invoice.getBalance();
                    moduleId = NotesConstants.AP_INVOICE;
                    paymentAmount = invoice.getBalance();
                    invoiceOrBl = invoice.getInvoiceNumber();
                    noteDesc.append("Invoice '");
                } else {
                    totalPaymentAmount += -invoice.getBalance();
                    moduleId = NotesConstants.AR_INVOICE;
                    paymentAmount = -invoice.getBalance();
                    invoiceOrBl = CommonUtils.isNotEmpty(invoice.getBillLaddingNo()) ? invoice.getBillLaddingNo() : invoice.getInvoiceNumber();
                    noteDesc.append("Invoice/BL '");
                }

                invoice.setBalance(0d);
                invoice.setBalanceInProcess(0d);
                String moduleRefId = invoice.getCustNo() + "-" + invoiceOrBl;
                noteDesc.append(invoiceOrBl).append("'");
                noteDesc.append(" of Vendor '").append(invoice.getCustName()).append("(").append(invoice.getCustNo()).append(")'");
                noteDesc.append(" for amount '").append(NumberUtils.formatNumber(paymentAmount)).append("'");
                noteDesc.append(" approved by ").append(form.getUser().getLoginName()).append(" on ").append(currentDate);
                AuditNotesUtils.insertAuditNotes(noteDesc.toString(), moduleId, moduleRefId, moduleId, form.getUser());

                InvoiceModel invoiceModel = new InvoiceModel();
                invoiceModel.setInvoiceOrBl(invoiceOrBl);
                invoiceModel.setInvoiceDate(DateUtils.formatDate(invoice.getTransactionDate(), "MM/dd/yyyy"));
                invoiceModel.setInvoiceAmount(NumberUtils.formatNumber(paymentAmount));
                if (null == paidBy) {
                    paidBy = userDAO.getUserInfo(invoice.getPaidBy());
                }
                invoiceModel.setPaidBy(paidBy);
                invoiceList.add(invoiceModel);

                Transaction payment = new Transaction();
                PropertyUtils.copyProperties(payment, invoice);
                payment.setTransactionId(null);
                if (CommonUtils.isEqualIgnoreCase(invoice.getTransactionType(), TRANSACTION_TYPE_ACCOUNT_PAYABLE)) {
                    payment.setBillLaddingNo(null);
                }
                payment.setTransactionType(TRANSACTION_TYPE_PAYAMENT);
                payment.setStatus(STATUS_PAID);
                payment.setPaymentMethod(paymentModel.getPaymentMethod());
                payment.setApBatchId(form.getBatchId());
                payment.setBankAccountNumber(form.getBankAccount());
                payment.setBankName(form.getBankName());
                payment.setChequeNumber(null);
                payment.setGlAccountNumber(glAccountNo);
                payment.setTransactionDate(paymentDate);
                payment.setPostedDate(paymentDate);
                payment.setReconciled(NO);
                payment.setReconciledDate(null);
                payment.setCleared(NO);
                payment.setClearedDate(null);
                payment.setVoidTransaction(NO);
                payment.setVoidDate(null);
                payment.setReprint(NO);
                payment.setReprintDate(null);
                payment.setBillTo(YES);
                payment.setTransactionAmt(paymentAmount);
                payment.setBalance(0d);
                payment.setBalanceInProcess(0d);
                payment.setCreatedOn(now);
                payment.setCreatedBy(form.getUser().getUserId());
                payment.setUpdatedOn(null);
                payment.setUpdatedBy(null);
                payment.setPaidBy(null);
                transactionDAO.save(payment);

                if (CommonUtils.isEqualIgnoreCase(invoice.getTransactionType(), TRANSACTION_TYPE_ACCOUNT_RECEIVABLE)) {
                    ArTransactionHistory arTransactionHistory = new ArTransactionHistory();
                    arTransactionHistory.setCustomerNumber(invoice.getCustNo());
                    arTransactionHistory.setBlNumber(invoice.getBillLaddingNo());
                    arTransactionHistory.setInvoiceNumber(invoice.getInvoiceNumber());
                    arTransactionHistory.setInvoiceDate(invoice.getTransactionDate());
                    arTransactionHistory.setTransactionDate(paymentDate);
                    arTransactionHistory.setPostedDate(paymentDate);
                    arTransactionHistory.setTransactionAmount(paymentAmount);
                    arTransactionHistory.setCustomerReferenceNumber(invoice.getCustomerReferenceNo());
                    arTransactionHistory.setVoyageNumber(invoice.getVoyageNo());
                    arTransactionHistory.setCheckNumber(null);
                    arTransactionHistory.setArBatchId(null);
                    arTransactionHistory.setApBatchId(form.getBatchId());
                    arTransactionHistory.setTransactionType("AP PY");
                    arTransactionHistory.setCreatedBy(form.getUser().getLoginName());
                    arTransactionHistory.setCreatedDate(now);
                    arTransactionHistoryDAO.save(arTransactionHistory);
                    if (CommonUtils.isEqualIgnoreCase(invoice.getCreditHold(), YES)) {
                        if (invoice.isEmailed()) {
                            ArCreditHoldUtils.sendEmail(invoice, form.getUser(), false, imagePath);
                        }
                        invoice.setRemovedFromHold(true);
                        invoice.setEmailed(false);
                        invoice.setCreditHold(NO);
                        noteDesc.delete(0, noteDesc.length());
                        noteDesc.append("Invoice/BL - ").append(invoiceOrBl).append(" of ");
                        noteDesc.append(invoice.getCustName()).append("(").append(invoice.getCustNo()).append(")");
                        noteDesc.append(" taken off credit hold by ").append(form.getUser().getLoginName()).append(" on ").append(currentDate);
                        AuditNotesUtils.insertAuditNotes(noteDesc.toString(), NotesConstants.AR_INVOICE, moduleRefId, "AR Credit Hold", form.getUser());
                    }

                    TransactionLedger subledger = new TransactionLedger();
                    PropertyUtils.copyProperties(subledger, invoice);
                    subledger.setTransactionAmt(paymentAmount);
                    subledger.setBalance(0d);
                    subledger.setBalanceInProcess(0d);
                    subledger.setGlAccountNumber(LoadLogisoftProperties.getProperty(AR_CONTROL_ACCOUNT));
                    subledger.setTransactionType(TRANSACTION_TYPE_CASH_DEPOSIT);
                    subledger.setStatus(STATUS_PAID);
                    subledger.setCleared(NO);
                    subledger.setBankAccountNumber(form.getBankAccount());
                    subledger.setBankName(form.getBankName());
                    subledger.setChequeNumber(null);
                    subledger.setPostedDate(paymentDate);
                    subledger.setCustomerReferenceNo(String.valueOf(form.getBatchId()));
                    subledger.setInvoiceNumber(null);
                    subledger.setTransactionDate(now);
                    subledger.setSubledgerSourceCode(SUB_LEDGER_CODE_CASH_DEPOSIT);
                    subledger.setApBatchId(form.getBatchId());
                    subledger.setCreatedOn(now);
                    subledger.setCreatedBy(form.getUser().getUserId());
                    subledger.setUpdatedOn(null);
                    subledger.setUpdatedBy(null);
                    accountingLedgerDAO.save(subledger);
                } else {
                    accountingLedgerDAO.setPaidDateForAccrualCost(invoice.getCustNo(), invoice.getInvoiceNumber(), DateUtils.formatDate(paidDate, "yyyy-MM-dd"));
                    ApTransactionHistory apTransactionHistory = new ApTransactionHistory(payment);
                    apTransactionHistory.setInvoiceDate(invoice.getTransactionDate());
                    apTransactionHistory.setAmount(-paymentAmount);
                    apTransactionHistory.setCreatedBy(form.getUser().getLoginName());
                    apTransactionHistoryDAO.save(apTransactionHistory);
                }
            } else {
                throw new AccountingException("One or more invoices of " + paymentModel.getVendorNumber() + " is already paid.");
            }
        }
        if (CommonUtils.isNotEqual(totalPaymentAmount, Double.parseDouble(paymentModel.getPaymentAmount().replace(",", "")))) {
            throw new AccountingException("Payment Amount for " + paymentModel.getVendorNumber() + " is not equal to sum of all invoices.");
        }
        form.setInvoiceList(invoiceList);
        String noteRefId = paymentModel.getVendorNumber() + "-" + form.getBatchId();
        noteDesc.delete(0, noteDesc.length());
        noteDesc.append(paymentModel.getPaymentMethod()).append(" Payment - ").append(form.getBatchId());
        noteDesc.append(" of Vendor '").append(paymentModel.getVendorName()).append("(").append(paymentModel.getVendorName()).append(")'");
        noteDesc.append(" for amount '").append(paymentModel.getPaymentAmount()).append("'");
        noteDesc.append(" paid by ").append(paidBy.getLoginName()).append(" on ").append(DateUtils.formatDate(paidDate, "MM/dd/yyyy"));
        AuditNotesUtils.insertAuditNotes(noteDesc.toString(), NotesConstants.AP_PAYMENT, noteRefId, NotesConstants.AP_PAYMENT, form.getUser());
        noteDesc.delete(0, noteDesc.length());
        noteDesc.append(paymentModel.getPaymentMethod()).append(" Payment - ").append(form.getBatchId());
        noteDesc.append(" of Vendor '").append(paymentModel.getVendorName()).append("(").append(paymentModel.getVendorName()).append(")'");
        noteDesc.append(" for amount '").append(paymentModel.getPaymentAmount()).append("'");
        noteDesc.append(" approved by ").append(form.getUser().getLoginName()).append(" on ").append(currentDate);
        AuditNotesUtils.insertAuditNotes(noteDesc.toString(), NotesConstants.AP_PAYMENT, noteRefId, NotesConstants.AP_PAYMENT, form.getUser());
        sendEmail(form, request, paymentModel, null, STATUS_APPROVED);
    }

    private void makeAchOrWirePayment(ApPaymentForm form, HttpServletRequest request, PaymentModel paymentModel) throws Exception {
        TransactionDAO transactionDAO = new TransactionDAO();
        StringBuilder noteDesc = new StringBuilder();
        Date now = new Date();
        String currentDate = DateUtils.formatDate(now, "MM/dd/yyyy");
        List<InvoiceModel> invoiceList = new ArrayList<InvoiceModel>();
        for (String id : paymentModel.getIds().split(",")) {
            Transaction invoice = transactionDAO.findById(Integer.parseInt(id));
            invoice.setStatus(STATUS_WAITING_FOR_APPROVAL);
            invoice.setPaymentMethod(paymentModel.getPaymentMethod());
            invoice.setBankAccountNumber(form.getBankAccount());
            invoice.setBankName(form.getBankName());
            invoice.setChequeNumber(null);
            invoice.setPaidBy(form.getUser().getUserId());
            invoice.setPaidOn(now);
            invoice.setUpdatedBy(form.getUser().getUserId());
            invoice.setUpdatedOn(now);
            String moduleId;
            Double paymentAmount;
            String invoiceOrBl;
            noteDesc.delete(0, noteDesc.length());
            noteDesc.append(paymentModel.getPaymentMethod()).append(" Payment for ");
            if (CommonUtils.isEqualIgnoreCase(invoice.getTransactionType(), TRANSACTION_TYPE_ACCOUNT_PAYABLE)) {
                moduleId = NotesConstants.AP_INVOICE;
                paymentAmount = invoice.getBalance();
                invoiceOrBl = invoice.getInvoiceNumber();
                noteDesc.append("Invoice '");
            } else {
                moduleId = NotesConstants.AR_INVOICE;
                paymentAmount = -invoice.getBalance();
                invoiceOrBl = CommonUtils.isNotEmpty(invoice.getBillLaddingNo()) ? invoice.getBillLaddingNo() : invoice.getInvoiceNumber();
                noteDesc.append("Invoice/BL '");
            }

            String moduleRefId = invoice.getCustNo() + "-" + invoiceOrBl;
            noteDesc.append(invoiceOrBl).append("'");
            noteDesc.append(" of Vendor '").append(invoice.getCustName()).append("(").append(invoice.getCustNo()).append(")'");
            noteDesc.append(" for amount '").append(NumberUtils.formatNumber(paymentAmount)).append("'");
            noteDesc.append(" paid by ").append(form.getUser().getLoginName()).append(" on ").append(currentDate);
            AuditNotesUtils.insertAuditNotes(noteDesc.toString(), moduleId, moduleRefId, moduleId, form.getUser());

            InvoiceModel invoiceModel = new InvoiceModel();
            invoiceModel.setInvoiceOrBl(invoiceOrBl);
            invoiceModel.setInvoiceDate(DateUtils.formatDate(invoice.getTransactionDate(), "MM/dd/yyyy"));
            invoiceModel.setInvoiceAmount(NumberUtils.formatNumber(paymentAmount));
            invoiceModel.setPaidBy(form.getUser());
            invoiceList.add(invoiceModel);
        }
        form.setInvoiceList(invoiceList);
        sendEmail(form, request, paymentModel, null, STATUS_WAITING_FOR_APPROVAL);
    }

    private void disapproveAchOrWirePayment(ApPaymentForm form, PaymentModel paymentModel) throws Exception {
        TransactionDAO transactionDAO = new TransactionDAO();
        StringBuilder noteDesc = new StringBuilder();
        Date now = new Date();
        String currentDate = DateUtils.formatDate(now, "MM/dd/yyyy");
        for (String id : paymentModel.getIds().split(",")) {
            Transaction invoice = transactionDAO.findById(Integer.parseInt(id));
            invoice.setStatus(STATUS_READY_TO_PAY);
            invoice.setPaymentMethod(null);
            invoice.setBankAccountNumber(null);
            invoice.setBankName(null);
            invoice.setPaidBy(null);
            invoice.setPaidOn(null);
            invoice.setUpdatedBy(form.getUser().getUserId());
            invoice.setUpdatedOn(now);
            String moduleId;
            Double paymentAmount;
            String invoiceOrBl;
            noteDesc.delete(0, noteDesc.length());
            noteDesc.append(paymentModel.getPaymentMethod()).append(" Payment for ");
            if (CommonUtils.isEqualIgnoreCase(invoice.getTransactionType(), TRANSACTION_TYPE_ACCOUNT_PAYABLE)) {
                moduleId = NotesConstants.AP_INVOICE;
                paymentAmount = invoice.getBalance();
                invoiceOrBl = invoice.getInvoiceNumber();
                noteDesc.append("Invoice '");
            } else {
                moduleId = NotesConstants.AR_INVOICE;
                invoiceOrBl = CommonUtils.isNotEmpty(invoice.getBillLaddingNo()) ? invoice.getBillLaddingNo() : invoice.getInvoiceNumber();
                paymentAmount = -invoice.getBalance();
                noteDesc.append("Invoice/BL '");
            }

            String moduleRefId = invoice.getCustNo() + "-" + invoiceOrBl;
            noteDesc.append(invoiceOrBl).append("'");
            noteDesc.append(" of Vendor '").append(invoice.getCustName()).append("(").append(invoice.getCustNo()).append(")'");
            noteDesc.append(" for amount '").append(NumberUtils.formatNumber(paymentAmount)).append("'");
            noteDesc.append(" is disapproved by ").append(form.getUser().getLoginName()).append(" on ").append(currentDate);
            AuditNotesUtils.insertAuditNotes(noteDesc.toString(), moduleId, moduleRefId, moduleId, form.getUser());
        }
    }

    private void makeOtherPayment(ApPaymentForm form, HttpServletRequest request, PaymentModel paymentModel, Integer count, String glAccountNo) throws Exception {
        TransactionDAO transactionDAO = new TransactionDAO();
        AccountingLedgerDAO accountingLedgerDAO = new AccountingLedgerDAO();
        ArTransactionHistoryDAO arTransactionHistoryDAO = new ArTransactionHistoryDAO();
        ApTransactionHistoryDAO apTransactionHistoryDAO = new ApTransactionHistoryDAO();
        String imagePath = "http://" + this.servlet.getServletContext().getResource(LoadLogisoftProperties.getProperty("application.image.logo")).getPath();
        StringBuilder noteDesc = new StringBuilder();
        Date now = new Date();
        Date paymentDate = DateUtils.parseDate(form.getPaymentDate(), "MM/dd/yyyy");
        String currentDate = DateUtils.formatDate(now, "MM/dd/yyyy");
        List<InvoiceModel> invoiceList = new ArrayList<InvoiceModel>();
        String checkNumber = paymentModel.getPaymentMethod() + " - " + count;
        Double totalPaymentAmount = 0d;
        for (String id : paymentModel.getIds().split(",")) {
            Transaction invoice = transactionDAO.findById(Integer.parseInt(id));
            if (NumberUtils.isNotZero(invoice.getBalance())) {
                invoice.setStatus(STATUS_PAID);
                invoice.setPaymentMethod(paymentModel.getPaymentMethod());
                invoice.setApBatchId(form.getBatchId());
                invoice.setArBatchId(null);
                invoice.setBankAccountNumber(form.getBankAccount());
                invoice.setBankName(form.getBankName());
                invoice.setChequeNumber(checkNumber);
                invoice.setCheckDate(paymentDate);
                invoice.setPaidBy(form.getUser().getUserId());
                invoice.setPaidOn(now);
                String moduleId;
                Double paymentAmount;
                String invoiceOrBl;
                noteDesc.delete(0, noteDesc.length());
                noteDesc.append(paymentModel.getPaymentMethod()).append(" Payment for ");
                if (CommonUtils.isEqualIgnoreCase(invoice.getTransactionType(), TRANSACTION_TYPE_ACCOUNT_PAYABLE)) {
                    totalPaymentAmount += invoice.getBalance();
                    moduleId = NotesConstants.AP_INVOICE;
                    paymentAmount = invoice.getBalance();
                    invoiceOrBl = invoice.getInvoiceNumber();
                    noteDesc.append("Invoice '");
                } else {
                    totalPaymentAmount += -invoice.getBalance();
                    moduleId = NotesConstants.AR_INVOICE;
                    paymentAmount = -invoice.getBalance();
                    invoiceOrBl = CommonUtils.isNotEmpty(invoice.getBillLaddingNo()) ? invoice.getBillLaddingNo() : invoice.getInvoiceNumber();
                    noteDesc.append("Invoice/BL '");
                }

                invoice.setBalance(0d);
                invoice.setBalanceInProcess(0d);
                String moduleRefId = invoice.getCustNo() + "-" + invoiceOrBl;
                noteDesc.append(invoiceOrBl).append("'");
                noteDesc.append(" of Vendor '").append(invoice.getCustName()).append("(").append(invoice.getCustNo()).append(")'");
                noteDesc.append(" for amount '").append(NumberUtils.formatNumber(paymentAmount)).append("'");
                noteDesc.append(" is paid by ").append(form.getUser().getLoginName()).append(" on ").append(currentDate);
                AuditNotesUtils.insertAuditNotes(noteDesc.toString(), moduleId, moduleRefId, moduleId, form.getUser());

                InvoiceModel invoiceModel = new InvoiceModel();
                invoiceModel.setInvoiceOrBl(invoiceOrBl);
                invoiceModel.setInvoiceDate(DateUtils.formatDate(invoice.getTransactionDate(), "MM/dd/yyyy"));
                invoiceModel.setInvoiceAmount(NumberUtils.formatNumber(paymentAmount));
                invoiceModel.setPaidBy(form.getUser());
                invoiceList.add(invoiceModel);

                Transaction payment = new Transaction();
                PropertyUtils.copyProperties(payment, invoice);
                payment.setTransactionId(null);
                if (CommonUtils.isEqualIgnoreCase(invoice.getTransactionType(), TRANSACTION_TYPE_ACCOUNT_PAYABLE)) {
                    payment.setBillLaddingNo(null);
                }
                payment.setTransactionType(TRANSACTION_TYPE_PAYAMENT);
                payment.setStatus(STATUS_PAID);
                payment.setPaymentMethod(paymentModel.getPaymentMethod());
                payment.setApBatchId(form.getBatchId());
                payment.setBankAccountNumber(form.getBankAccount());
                payment.setBankName(form.getBankName());
                payment.setChequeNumber(checkNumber);
                payment.setGlAccountNumber(glAccountNo);
                payment.setTransactionDate(paymentDate);
                payment.setPostedDate(paymentDate);
                payment.setReconciled(NO);
                payment.setReconciledDate(null);
                payment.setCleared(NO);
                payment.setClearedDate(null);
                payment.setVoidTransaction(NO);
                payment.setVoidDate(null);
                payment.setReprint(NO);
                payment.setReprintDate(null);
                payment.setBillTo(YES);
                payment.setTransactionAmt(paymentAmount);
                payment.setBalance(0d);
                payment.setBalanceInProcess(0d);
                payment.setCreatedOn(now);
                payment.setCreatedBy(form.getUser().getUserId());
                payment.setUpdatedOn(null);
                payment.setUpdatedBy(null);
                payment.setPaidBy(null);
                payment.setPaidOn(null);
                transactionDAO.save(payment);

                if (CommonUtils.isEqualIgnoreCase(invoice.getTransactionType(), TRANSACTION_TYPE_ACCOUNT_RECEIVABLE)) {
                    ArTransactionHistory arTransactionHistory = new ArTransactionHistory();
                    arTransactionHistory.setCustomerNumber(invoice.getCustNo());
                    arTransactionHistory.setBlNumber(invoice.getBillLaddingNo());
                    arTransactionHistory.setInvoiceNumber(invoice.getInvoiceNumber());
                    arTransactionHistory.setInvoiceDate(invoice.getTransactionDate());
                    arTransactionHistory.setTransactionDate(paymentDate);
                    arTransactionHistory.setPostedDate(paymentDate);
                    arTransactionHistory.setTransactionAmount(paymentAmount);
                    arTransactionHistory.setCustomerReferenceNumber(invoice.getCustomerReferenceNo());
                    arTransactionHistory.setVoyageNumber(invoice.getVoyageNo());
                    arTransactionHistory.setCheckNumber(checkNumber);
                    arTransactionHistory.setArBatchId(null);
                    arTransactionHistory.setApBatchId(form.getBatchId());
                    arTransactionHistory.setTransactionType("AP PY");
                    arTransactionHistory.setCreatedBy(form.getUser().getLoginName());
                    arTransactionHistory.setCreatedDate(now);
                    arTransactionHistoryDAO.save(arTransactionHistory);
                    if (CommonUtils.isEqualIgnoreCase(invoice.getCreditHold(), YES)) {
                        if (invoice.isEmailed()) {
                            ArCreditHoldUtils.sendEmail(invoice, form.getUser(), false, imagePath);
                        }
                        invoice.setRemovedFromHold(true);
                        invoice.setEmailed(false);
                        invoice.setCreditHold(NO);
                        noteDesc.delete(0, noteDesc.length());
                        noteDesc.append("Invoice/BL - ").append(invoiceOrBl).append(" of ");
                        noteDesc.append(invoice.getCustName()).append("(").append(invoice.getCustNo()).append(")");
                        noteDesc.append(" is taken off credit hold by ").append(form.getUser().getLoginName()).append(" on ").append(currentDate);
                        AuditNotesUtils.insertAuditNotes(noteDesc.toString(), NotesConstants.AR_INVOICE, moduleRefId, "AR Credit Hold", form.getUser());
                    }

                    TransactionLedger subledger = new TransactionLedger();
                    PropertyUtils.copyProperties(subledger, invoice);
                    subledger.setTransactionAmt(paymentAmount);
                    subledger.setBalance(0d);
                    subledger.setBalanceInProcess(0d);
                    subledger.setGlAccountNumber(LoadLogisoftProperties.getProperty(AR_CONTROL_ACCOUNT));
                    subledger.setTransactionType(TRANSACTION_TYPE_CASH_DEPOSIT);
                    subledger.setStatus(STATUS_PAID);
                    subledger.setCleared(NO);
                    subledger.setBankAccountNumber(form.getBankAccount());
                    subledger.setBankName(form.getBankName());
                    subledger.setChequeNumber(checkNumber);
                    subledger.setPostedDate(paymentDate);
                    subledger.setCustomerReferenceNo(String.valueOf(form.getBatchId()));
                    subledger.setInvoiceNumber(null);
                    subledger.setTransactionDate(now);
                    subledger.setSubledgerSourceCode(SUB_LEDGER_CODE_CASH_DEPOSIT);
                    subledger.setApBatchId(form.getBatchId());
                    subledger.setCreatedOn(now);
                    subledger.setCreatedBy(form.getUser().getUserId());
                    subledger.setUpdatedOn(null);
                    subledger.setUpdatedBy(null);
                    accountingLedgerDAO.save(subledger);
                } else {
                    accountingLedgerDAO.setPaidDateForAccrualCost(invoice.getCustNo(), invoice.getInvoiceNumber(), DateUtils.formatDate(paymentDate, "yyyy-MM-dd"));
                    ApTransactionHistory apTransactionHistory = new ApTransactionHistory(payment);
                    apTransactionHistory.setInvoiceDate(invoice.getTransactionDate());
                    apTransactionHistory.setAmount(-paymentAmount);
                    apTransactionHistory.setCreatedBy(form.getUser().getLoginName());
                    apTransactionHistoryDAO.save(apTransactionHistory);
                }
            } else {
                throw new AccountingException("One or more invoices of " + paymentModel.getVendorNumber() + " is already paid.");
            }
        }
        if (CommonUtils.isNotEqual(totalPaymentAmount, Double.parseDouble(paymentModel.getPaymentAmount().replace(",", "")))) {
            throw new AccountingException("Payment Amount for " + paymentModel.getVendorNumber() + " is not equal to sum of all invoices.");
        }
        form.setInvoiceList(invoiceList);
        String noteRefId = paymentModel.getVendorNumber() + "-" + form.getBatchId();
        noteDesc.delete(0, noteDesc.length());
        noteDesc.append(paymentModel.getPaymentMethod()).append(" Payment - ").append(form.getBatchId());
        noteDesc.append(" of Vendor '").append(paymentModel.getVendorName()).append("(").append(paymentModel.getVendorName()).append(")'");
        noteDesc.append(" for amount '").append(paymentModel.getPaymentAmount()).append("'");
        noteDesc.append(" is paid by ").append(form.getUser().getLoginName()).append(" on ").append(currentDate);
        AuditNotesUtils.insertAuditNotes(noteDesc.toString(), NotesConstants.AP_PAYMENT, noteRefId, NotesConstants.AP_PAYMENT, form.getUser());
        sendEmail(form, request, paymentModel, null, STATUS_APPROVED);
    }

    private void saveBatch(ApPaymentForm form, Double batchAmount, String glAccountNo) throws Exception {
        Date now = new Date();
        Date paymentDate = DateUtils.parseDate(form.getPaymentDate(), "MM/dd/yyyy");
        TransactionLedger subledger = new TransactionLedger();
        subledger.setApBatchId(form.getBatchId());
        subledger.setBankAccountNumber(form.getBankAccount());
        subledger.setBankName(form.getBankName());
        subledger.setTransactionDate(now);
        subledger.setPostedDate(paymentDate);
        subledger.setTransactionAmt(-batchAmount);
        subledger.setBalance(-batchAmount);
        subledger.setBalanceInProcess(-batchAmount);
        subledger.setGlAccountNumber(glAccountNo);
        subledger.setSubledgerSourceCode(SUB_LEDGER_CODE_CASH_DEPOSIT);
        subledger.setTransactionType(TRANSACTION_TYPE_CASH_DEPOSIT);
        subledger.setStatus(STATUS_PAID);
        subledger.setCleared(NO);
        subledger.setCustomerReferenceNo(String.valueOf(form.getBatchId()));
        subledger.setCreatedOn(now);
        subledger.setCreatedBy(form.getUser().getUserId());
        new AccountingLedgerDAO().save(subledger);
        ApBatchDAO apBatchDAO = new ApBatchDAO();
        ApBatch apBatch = apBatchDAO.findById(form.getBatchId());
        apBatch.setBatchDesc(form.getBatchDesc());
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

    public ActionForward makePayment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        synchronized (this) {
            ApPaymentForm apPaymentForm = (ApPaymentForm) form;
            BankDetailsDAO bankDetailsDAO = new BankDetailsDAO();
            ApPaymentDAO apPaymentDAO = new ApPaymentDAO();
            String glAccountNo = bankDetailsDAO.getGlAccountNo(apPaymentForm.getBankName(), apPaymentForm.getBankAccount());
            Double batchAmount = 0d;
            List<Integer> checks = new ArrayList<Integer>();
            List<Integer> overflows = new ArrayList<Integer>();
            String[] printers = null;
            String contextPath = this.getServlet().getServletContext().getRealPath("/");
            try {
                for (PaymentModel paymentModel : apPaymentForm.getPaymentList()) {
                    if (CommonUtils.isEqualIgnoreCase(paymentModel.getPaymentMethod(), PAYMENT_METHOD_CHECK)) {
                        if (CommonUtils.isEqualIgnoreCase(paymentModel.getPay(), "ON")) {
                            Double paymentAmount = apPaymentDAO.getPaymentAmount(paymentModel);
                            if (CommonUtils.isNotEqual(paymentAmount, Double.parseDouble(paymentModel.getPaymentAmount().replace(",", "")))) {
                                throw new AccountingException("Payment Amount for " + paymentModel.getVendorNumber() + " is not equal to sum of all invoices.");
                            }
                            Integer checkNumber = new CheckNumberThread().getCheckNumber(apPaymentForm.getBankName(), apPaymentForm.getBankAccount());
                            makeCheckPayment(apPaymentForm, request, paymentModel, checkNumber, glAccountNo);
                            String[] files = new CheckCreator(apPaymentForm, paymentModel, contextPath).create(String.valueOf(checkNumber));
                            if (null == printers) {
                                printers = bankDetailsDAO.getPrinters(apPaymentForm.getBankName(), apPaymentForm.getBankAccount());
                            }
                            savePrint(files[0], String.valueOf(checkNumber), printers[0], apPaymentForm.getUser());
                            checks.add(checkNumber);

                            if (CommonUtils.isNotEmpty(files[1]) && CommonUtils.isNotEmpty(printers[1])) {
                                savePrint(files[1], String.valueOf(checkNumber), printers[1], apPaymentForm.getUser());
                                overflows.add(checkNumber);
                            }
                            batchAmount += paymentAmount;
                        }
                    } else if (CommonUtils.in(paymentModel.getPaymentMethod(), PAYMENT_METHOD_ACH, PAYMENT_METHOD_WIRE)) {
                        if (CommonUtils.isEqualIgnoreCase(paymentModel.getPay(), "ON")) {
                            if (CommonUtils.isEqualIgnoreCase(paymentModel.getApprove(), "ON")) {
                                Double paymentAmount = apPaymentDAO.getPaymentAmount(paymentModel);
                                if (CommonUtils.isNotEqual(paymentAmount, Double.parseDouble(paymentModel.getPaymentAmount().replace(",", "")))) {
                                    throw new AccountingException("Payment Amount for " + paymentModel.getVendorNumber() + " is not equal to sum of all invoices.");
                                }
                                approveAchOrWirePayment(apPaymentForm, request, paymentModel, glAccountNo);
                                batchAmount += paymentAmount;
                            } else if (CommonUtils.isEqualIgnoreCase(paymentModel.getStatus(), STATUS_READY_TO_PAY)) {
                                makeAchOrWirePayment(apPaymentForm, request, paymentModel);
                            }
                        } else {
                            disapproveAchOrWirePayment(apPaymentForm, paymentModel);
                        }
                    } else {
                        if (CommonUtils.isEqualIgnoreCase(paymentModel.getPay(), "ON")) {
                            Double paymentAmount = apPaymentDAO.getPaymentAmount(paymentModel);
                            if (CommonUtils.isNotEqual(paymentAmount, Double.parseDouble(paymentModel.getPaymentAmount().replace(",", "")))) {
                                throw new AccountingException("Payment Amount for " + paymentModel.getVendorNumber() + " is not equal to sum of all invoices.");
                            }
                            Integer count = new CardCountThread().getCount(apPaymentForm.getBankName(), apPaymentForm.getBankAccount(), paymentModel.getPaymentMethod());
                            makeOtherPayment(apPaymentForm, request, paymentModel, count, glAccountNo);
                            batchAmount += paymentAmount;
                        }
                    }
                }
                if (NumberUtils.isNotZero(batchAmount)) {
                    saveBatch(apPaymentForm, batchAmount, glAccountNo);
                }
                if (CommonUtils.isNotEmpty(checks)) {
                    StringBuilder message = new StringBuilder();
                    message.append("Check - ").append(StringUtils.join(checks, ",")).append(" sent to the Check Printer - ").append(printers[0]).append(" successfully");
                    if (CommonUtils.isNotEmpty(overflows)) {
                        message.append("<br/>Overflow Check - ").append(StringUtils.join(overflows, ",")).append(" sent to the Overflow Printer - ").append(printers[1]).append(" successfully");
                    }
                    request.setAttribute("message", message.toString());
                } else {
                    request.setAttribute("message", "All Payments are processed successfully.");
                }
            } catch (AccountingException e) {
                request.setAttribute("error", e.getMessage());
                bankDetailsDAO.getCurrentSession().getTransaction().rollback();
                bankDetailsDAO.getCurrentSession().getTransaction().begin();
            } catch (Exception e) {
                throw e;
            }
            return search(mapping, apPaymentForm, request, response);
        }
    }
}
