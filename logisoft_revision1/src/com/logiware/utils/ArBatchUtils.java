package com.logiware.utils;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.bc.notes.NotesBC;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.hibernate.dao.CustomerAccountingDAO;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cong.logisoft.hibernate.dao.TerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingAcTransDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.struts.form.NotesForm;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.AccountingConstants;
import com.gp.cvst.logisoft.domain.ArBatch;
import com.gp.cvst.logisoft.domain.DocumentStoreLog;
import com.gp.cvst.logisoft.domain.FclBlCostCodes;
import com.gp.cvst.logisoft.domain.PaymentChecks;
import com.gp.cvst.logisoft.domain.Payments;
import com.gp.cvst.logisoft.domain.Transaction;
import com.gp.cvst.logisoft.domain.TransactionLedger;
import com.gp.cvst.logisoft.hibernate.dao.AccountDetailsDAO;
import com.gp.cvst.logisoft.hibernate.dao.CustAddressDAO;
import com.gp.cvst.logisoft.hibernate.dao.DocumentStoreLogDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlCostCodesDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlDAO;
import com.gp.cvst.logisoft.hibernate.dao.PaymentChecksDAO;
import com.gp.cvst.logisoft.hibernate.dao.PaymentsDAO;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.gp.cvst.logisoft.hibernate.dao.TransactionDAO;
import com.logiware.accounting.dao.AccrualsDAO;
import com.logiware.accounting.exception.AccountingException;
import com.logiware.bean.PaymentBean;
import com.logiware.bean.PaymentCheckBean;
import com.logiware.excel.ExportNSInvoiceToExcel;
import com.logiware.form.ArBatchForm;
import com.logiware.hibernate.dao.AccountingLedgerDAO;
import com.logiware.hibernate.dao.AccountingTransactionDAO;
import com.logiware.hibernate.dao.ApTransactionHistoryDAO;
import com.logiware.hibernate.dao.ArBatchDAO;
import com.logiware.hibernate.dao.ArTransactionHistoryDAO;
import com.logiware.hibernate.dao.ReconcileDAO;
import com.logiware.hibernate.domain.ApTransactionHistory;
import com.logiware.hibernate.domain.ArTransactionHistory;
import com.logiware.reports.NSInvoiceReportCreator;
import com.oreilly.servlet.ServletUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author lakshh
 */
public class ArBatchUtils implements ConstantsInterface, NotesConstants {

    public static void postArBatch(ArBatchForm arBatchForm, HttpServletRequest request) throws Exception {
        User loginUser = (User) request.getSession().getAttribute("loginuser");
        ArBatchDAO arBatchDAO = new ArBatchDAO();
        ArBatch arBatch = arBatchDAO.findById(Integer.parseInt(arBatchForm.getSelectedBatchId()));
        if (CommonUtils.isEqualIgnoreCase(arBatch.getStatus(), "Open")) {
            if (CommonUtils.isEqual(arBatch.getBatchType(), AccountingConstants.AR_NET_SETT_BATCH)) {
                postNetSettBatch(arBatchForm, arBatch, loginUser);
            }
            //Handling Accruals
            handlingAccruals(arBatch, loginUser);
            //Handling AP Transactions
            handlingApTransactions(arBatch, loginUser);
            //Handling AR Transactions
            handlingArTransactions(arBatch, loginUser, request);
            //Handling Charges/Gl Account
            handlingChargeCodes(arBatch, loginUser);
            //Handling On Accounts
            handlingOnAccounts(arBatch, loginUser);
            //Handling Prepayments
            handlingPrepayments(arBatch, loginUser);
            //Handling Adjust amounts
            handlingAdjustAmounts(arBatch, loginUser);
            if (CommonUtils.isEqual(arBatch.getBatchType(), AccountingConstants.AR_CASH_BATCH)) {
                //Inserting RCT Subledger for whole batch
                insertRCTSubledgerForBatch(arBatch, loginUser);
            }
            arBatch.setStatus(AccountingConstants.BATCH_POSTED_STATUS);
            arBatch.setUsingBy(null);
            arBatchDAO.update(arBatch);
            String batchType = CommonUtils.isEqual(arBatch.getBatchType(), AccountingConstants.AR_NET_SETT_BATCH) ? "NETT SETT" : "DEPOSIT";
            StringBuilder desc = new StringBuilder(batchType).append(" Batch - ").append(arBatch.getBatchId());
            desc.append(" closed by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
            AuditNotesUtils.insertAuditNotes(desc.toString(), AR_BATCH, arBatch.getBatchId().toString(), "ArBatch", loginUser);
            arBatchDAO.getCurrentSession().flush();
            arBatchDAO.getCurrentSession().clear();
        }
    }

    private static void handlingAccruals(ArBatch arBatch, User loginUser) throws Exception {
        ApTransactionHistoryDAO apTransactionHistoryDAO = new ApTransactionHistoryDAO();
        AccountDetailsDAO accountDetailsDAO = new AccountDetailsDAO();
        FclBlCostCodesDAO fclBlCostCodesDAO = new FclBlCostCodesDAO();
        PaymentsDAO paymentsDAO = new PaymentsDAO();
        AccountingTransactionDAO accountingTransactionDAO = new AccountingTransactionDAO();
        AccountingLedgerDAO accountingLedgerDAO = new AccountingLedgerDAO();
        AccrualsDAO accrualsDAO = new AccrualsDAO();
        List<PaymentCheckBean> paymentChecks = new ArBatchDAO().getPaymentChecks(arBatch.getBatchId());
        for (PaymentCheckBean paymentCheck : paymentChecks) {
            List<String> accrualsList = paymentsDAO.getAccruals(arBatch.getBatchId(), paymentCheck.getCheckId());
            for (String accrualsId : accrualsList) {
                String[] accrualsIds = StringUtils.split(accrualsId, ",");
                double summaryAmount = 0d;
                StringBuilder blNumbers = new StringBuilder();
                TransactionLedger pjSubLedger = null;
                TransactionLedger accrual = null;
                for (String id : accrualsIds) {
                    accrual = accountingLedgerDAO.findById(new Integer(id));
                    if (accountDetailsDAO.validateAccount(accrual.getGlAccountNumber())) {
                        double paymentAmount = paymentsDAO.getAccrualPaidAmount(arBatch.getBatchId(), paymentCheck.getCheckId(), accrual.getTransactionId());
                        accrual.setCustName(paymentCheck.getCustomerName());
                        accrual.setCustNo(paymentCheck.getCustomerNumber());
                        accrual.setTransactionAmt(-paymentAmount);// to avoid the transaction amount difference after the accruals is selected to pay
                        summaryAmount += accrual.getTransactionAmt();
                        if (CommonUtils.isEmpty(accrual.getInvoiceNumber())) {
                            accrual.setInvoiceNumber(arBatch.getBatchId().toString());
                        }
                        accrual.setChequeNumber(arBatch.getBatchId().toString());
                        if (CommonUtils.isNotEmpty(accrual.getCostId())) {
                            if (CommonUtils.in(accrual.getShipmentType(), "LCLE", "LCLI")) {
                                Map<Serializable, Serializable> columns = new HashMap<Serializable, Serializable>();
                                if (CommonUtils.isNotEmpty(accrual.getDocReceipt())) {
                                    columns.put("b.invoice_number", accrual.getInvoiceNumber());
                                    columns.put("t.trans_type", TRANSACTION_TYPE_ACCOUNT_PAYABLE);
                                    accrualsDAO.updateLclCost(accrual.getCostId(), columns);
                                } else {
                                    columns.put("lssac.ap_reference_no", accrual.getInvoiceNumber());
                                    columns.put("lssac.ap_trans_type", TRANSACTION_TYPE_ACCOUNT_PAYABLE);
                                    accrualsDAO.updateLclUnitCost(accrual.getCostId(), columns);
                                }
                            } else {
                                FclBlCostCodes fclBlCostCodes = fclBlCostCodesDAO.findById(accrual.getCostId());
                                if (null != fclBlCostCodes) {
                                    fclBlCostCodes.setInvoiceNumber(accrual.getInvoiceNumber());
                                    fclBlCostCodes.setTransactionType(TRANSACTION_TYPE_ACCOUNT_PAYABLE);
                                    fclBlCostCodes.setDatePaid(arBatch.getDepositDate());
                                }
                            }
                        }
                        pjSubLedger = new TransactionLedger();
                        PropertyUtils.copyProperties(pjSubLedger, accrual);
                        pjSubLedger.setTransactionType(TRANSACTION_TYPE_ACCOUNT_PAYABLE);
                        pjSubLedger.setInvoiceNumber(accrual.getInvoiceNumber());
                        pjSubLedger.setStatus(STATUS_OPEN);
                        pjSubLedger.setSubledgerSourceCode(SUB_LEDGER_CODE_PURCHASE_JOURNAL);
                        pjSubLedger.setTransactionDate(arBatch.getDepositDate());
                        pjSubLedger.setPostedDate(arBatch.getDepositDate());
                        pjSubLedger.setArBatchId(arBatch.getBatchId());
                        pjSubLedger.setCreatedOn(new Date());
                        pjSubLedger.setCreatedBy(loginUser.getUserId());
                        pjSubLedger.setJournalEntryNumber(null);
                        pjSubLedger.setLineItemNumber(null);
                        accountingLedgerDAO.save(pjSubLedger);
                        if (CommonUtils.isEqual(arBatch.getBatchType(), AccountingConstants.AR_NET_SETT_BATCH)) {
                            TransactionLedger netSettSubledger = new TransactionLedger();
                            PropertyUtils.copyProperties(netSettSubledger, accrual);
                            netSettSubledger.setTransactionAmt(accrual.getTransactionAmt());
                            netSettSubledger.setBalance(accrual.getTransactionAmt());
                            netSettSubledger.setBalanceInProcess(accrual.getTransactionAmt());
                            netSettSubledger.setSubledgerSourceCode(AccountingConstants.SUBLEDGER_CODE_NETSETT);
                            netSettSubledger.setArBatchId(arBatch.getBatchId());
                            netSettSubledger.setApBatchId(null);
                            netSettSubledger.setTransactionType(AccountingConstants.TRANSACTION_TYPE_PY);
                            netSettSubledger.setTransactionDate(new Date());
                            netSettSubledger.setPostedDate(arBatch.getDepositDate());
                            netSettSubledger.setStatus(AccountingConstants.STATUS_OPEN);
                            netSettSubledger.setGlAccountNumber(LoadLogisoftProperties.getProperty("apControlAccount"));
                            netSettSubledger.setCreatedOn(new Date());
                            netSettSubledger.setCreatedBy(loginUser.getUserId());
                            netSettSubledger.setJournalEntryNumber(null);
                            netSettSubledger.setLineItemNumber(null);
                            accountingLedgerDAO.save(netSettSubledger);
                        } else {
                            TransactionLedger rctSubledger = new TransactionLedger();
                            PropertyUtils.copyProperties(rctSubledger, accrual);
                            rctSubledger.setTransactionAmt(accrual.getTransactionAmt());
                            rctSubledger.setBalance(accrual.getTransactionAmt());
                            rctSubledger.setBalanceInProcess(accrual.getTransactionAmt());
                            rctSubledger.setSubledgerSourceCode(AccountingConstants.AR_BATCH_CLOSING_SUBLEDGER_CODE);
                            rctSubledger.setArBatchId(arBatch.getBatchId());
                            rctSubledger.setApBatchId(null);
                            rctSubledger.setTransactionType(AccountingConstants.TRANSACTION_TYPE_CR);
                            rctSubledger.setGlAccountNumber(LoadLogisoftProperties.getProperty("apControlAccount"));
                            rctSubledger.setStatus(AccountingConstants.STATUS_OPEN);
                            rctSubledger.setTransactionDate(new Date());
                            rctSubledger.setPostedDate(arBatch.getDepositDate());
                            rctSubledger.setCreatedOn(new Date());
                            rctSubledger.setCreatedBy(loginUser.getUserId());
                            rctSubledger.setJournalEntryNumber(null);
                            rctSubledger.setLineItemNumber(null);
                            accountingLedgerDAO.save(rctSubledger);
                        }
                        accrual.setBalance(0d);
                        accrual.setBalanceInProcess(0d);
                        accrual.setStatus(STATUS_ASSIGN);
                        accrual.setPostedDate(arBatch.getDepositDate());
                        accrual.setUpdatedOn(new Date());
                        accrual.setUpdatedBy(loginUser.getUserId());
                        String key = id;
                        StringBuilder desc = new StringBuilder("Accrual of ");
                        boolean addAnd = false;
                        if (CommonUtils.isNotEmpty(accrual.getBillLaddingNo())) {
                            desc.append("B/L - '").append(accrual.getBillLaddingNo().trim()).append("'");
                            addAnd = true;
                        }
                        if (CommonUtils.isNotEmpty(accrual.getDocReceipt())) {
                            desc.append(addAnd ? " and " : "").append("Doc Receipt - '").append(accrual.getDocReceipt()).append("'");
                            addAnd = true;
                        }
                        if (CommonUtils.isNotEmpty(accrual.getVoyageNo())) {
                            desc.append(addAnd ? " and " : "").append("Voyage - '").append(accrual.getVoyageNo()).append("'");
                        }
                        desc.append(" of '").append(accrual.getCustNo()).append("'");
                        desc.append(" for amount - '").append(NumberUtils.formatNumber(accrual.getTransactionAmt())).append("'");
                        desc.append(" is assigned to Invoice - '").append(accrual.getInvoiceNumber()).append("'");
                        desc.append(" in this batch - '").append(arBatch.getBatchId()).append("'");
                        desc.append(" by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
                        AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.ACCRUALS, key, NotesConstants.ACCRUALS, loginUser);
                        if (CommonUtils.isNotEmpty(pjSubLedger.getBillLaddingNo())) {
                            blNumbers.append(pjSubLedger.getBillLaddingNo()).append(",");
                        }
                        accountingLedgerDAO.update(accrual);
                    } else {
                        throw new Exception("GL Account - " + accrual.getGlAccountNumber() + " is not a valid one");
                    }
                }
                //create one invoice entry
                Transaction transaction = new Transaction(accrual);
                transaction.setCustName(accrual.getCustName());
                transaction.setCustNo(accrual.getCustNo());
                transaction.setChargeCode(pjSubLedger.getChargeCode());
                transaction.setGlAccountNumber(pjSubLedger.getGlAccountNumber());
                transaction.setCustomerReferenceNo(StringUtils.left(StringUtils.removeEnd(blNumbers.toString(), ","), 500));
                transaction.setTransactionDate(arBatch.getDepositDate());
                transaction.setPostedDate(arBatch.getDepositDate());
                transaction.setDueDate(arBatch.getDepositDate());
                transaction.setCheckDate(arBatch.getDepositDate());
                transaction.setTransactionAmt(summaryAmount);
                transaction.setBalance(0d);
                transaction.setBalanceInProcess(0d);
                transaction.setBillTo(YES);
                transaction.setInvoiceNumber(accrual.getInvoiceNumber());
                transaction.setTransactionType(TRANSACTION_TYPE_ACCOUNT_PAYABLE);
                transaction.setStatus(STATUS_PAID);
                transaction.setArBatchId(arBatch.getBatchId());
                transaction.setApBatchId(null);
                transaction.setCreatedOn(new Date());
                transaction.setCreatedBy(loginUser.getUserId());
                accountingTransactionDAO.save(transaction);
                //History for invoice entry
                ApTransactionHistory apTransactionHistory = new ApTransactionHistory(transaction);
                apTransactionHistory.setCreatedBy(loginUser.getLoginName());
                apTransactionHistoryDAO.save(apTransactionHistory);
                String glPeriod = DateUtils.formatDate(arBatch.getDepositDate(), "yyyyyMM");
                String key = transaction.getCustNo() + "-" + transaction.getInvoiceNumber();
                StringBuilder desc = new StringBuilder("Invoice '").append(transaction.getInvoiceNumber()).append("'");
                desc.append(" of '").append(transaction.getCustNo()).append("'").append(" posted to AP on GL Period '");
                desc.append(glPeriod).append("' by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
                AuditNotesUtils.insertAuditNotes(desc.toString(), AP_INVOICE, key, AP_INVOICE, loginUser);
                //One payment entry
                Transaction apPayment = new Transaction();
                PropertyUtils.copyProperties(apPayment, transaction);
                apPayment.setTransactionType(TRANSACTION_TYPE_PAYAMENT);
                apPayment.setStatus(STATUS_PAID);
                apPayment.setCleared(NO);
                apPayment.setVoidTransaction(NO);
                apPayment.setTransactionDate(arBatch.getDepositDate());
                apPayment.setPostedDate(arBatch.getDepositDate());
                apPayment.setDueDate(arBatch.getDepositDate());
                apPayment.setBillTo(YES);
                apPayment.setArBatchId(arBatch.getBatchId());
                apPayment.setApBatchId(null);
                apPayment.setPaymentMethod(PAYMENT_METHOD_CHECK.toUpperCase());
                apPayment.setBalance(summaryAmount);
                apPayment.setBalanceInProcess(summaryAmount);
                apPayment.setCreatedOn(new Date());
                apPayment.setCreatedBy(loginUser.getUserId());
                apPayment.setUpdatedOn(null);
                apPayment.setUpdatedBy(null);
                accountingTransactionDAO.save(apPayment);
                //History for payment entry
                apTransactionHistory = new ApTransactionHistory(apPayment);
                apTransactionHistory.setInvoiceDate(transaction.getTransactionDate());
                apTransactionHistory.setAmount(-apPayment.getTransactionAmt());
                apTransactionHistory.setCreatedBy(loginUser.getLoginName());
                apTransactionHistoryDAO.save(apTransactionHistory);
                String batchType = "DEPOSIT";
                if (CommonUtils.isEqual(arBatch.getBatchType(), AccountingConstants.AR_NET_SETT_BATCH)) {
                    batchType = "NET SETT";
                }
                desc = new StringBuilder(batchType).append(" Payment for Invoice '");
                desc.append(apPayment.getInvoiceNumber()).append("' of Vendor '");
                desc.append(apPayment.getCustName()).append("(").append(apPayment.getCustNo()).append(")'");
                desc.append(" paid by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
                AuditNotesUtils.insertAuditNotes(desc.toString(), AP_INVOICE, key, AP_INVOICE, loginUser);
            }
        }
    }

    private static void handlingApTransactions(ArBatch arBatch, User loginUser) throws Exception {
        ApTransactionHistoryDAO apTransactionHistoryDAO = new ApTransactionHistoryDAO();
        PaymentsDAO paymentsDAO = new PaymentsDAO();
        AccountingTransactionDAO accountingTransactionDAO = new AccountingTransactionDAO();
        AccountingLedgerDAO accountingLedgerDAO = new AccountingLedgerDAO();
        List<String> apTransactionsList = paymentsDAO.getApTransactions(arBatch.getBatchId());
        for (String transactionId : apTransactionsList) {
            Transaction transaction = accountingTransactionDAO.findById(Integer.parseInt(transactionId));
            transaction.setArBatchId(arBatch.getBatchId());
            transaction.setApBatchId(null);
            transaction.setChequeNumber(arBatch.getBatchId().toString());
            transaction.setCheckDate(arBatch.getDepositDate());
            if (CommonUtils.isEqual(arBatch.getBatchType(), AccountingConstants.AR_NET_SETT_BATCH)) {
                /*
                 * Insert NetSett subledger with transaction type PY and GL Account= AP control acct
                 */
                TransactionLedger netSettSubledger = new TransactionLedger(transaction);
                netSettSubledger.setStatus(STATUS_OPEN);
                netSettSubledger.setTransactionType(AccountingConstants.TRANSACTION_TYPE_PY);
                netSettSubledger.setGlAccountNumber(LoadLogisoftProperties.getProperty("apControlAccount"));
                netSettSubledger.setSubledgerSourceCode(AccountingConstants.SUBLEDGER_CODE_NETSETT);
                netSettSubledger.setArBatchId(arBatch.getBatchId());
                netSettSubledger.setApBatchId(null);
                netSettSubledger.setCustomerReferenceNo("NET SETT Batch:" + arBatch.getBatchId().toString());
                netSettSubledger.setTransactionDate(new Date());
                netSettSubledger.setPostedDate(arBatch.getDepositDate());
                netSettSubledger.setCreatedOn(new Date());
                netSettSubledger.setCreatedBy(loginUser.getUserId());
                netSettSubledger.setTransactionAmt(transaction.getTransactionAmt());
                netSettSubledger.setBalance(transaction.getTransactionAmt());
                netSettSubledger.setBalanceInProcess(transaction.getTransactionAmt());
                accountingLedgerDAO.save(netSettSubledger);
            } else {
                /*
                 * Insert RCT Subledger with transaction type CR and GL Account= AP control acct
                 */
                TransactionLedger rctSubledger = new TransactionLedger(transaction);
                rctSubledger.setSubledgerSourceCode(AccountingConstants.AR_BATCH_CLOSING_SUBLEDGER_CODE);
                rctSubledger.setArBatchId(arBatch.getBatchId());
                rctSubledger.setApBatchId(null);
                rctSubledger.setTransactionAmt(transaction.getTransactionAmt());
                rctSubledger.setBalance(transaction.getTransactionAmt());
                rctSubledger.setBalanceInProcess(transaction.getTransactionAmt());
                rctSubledger.setTransactionType(AccountingConstants.TRANSACTION_TYPE_CR);
                rctSubledger.setStatus(AccountingConstants.STATUS_OPEN);
                rctSubledger.setGlAccountNumber(LoadLogisoftProperties.getProperty("apControlAccount"));
                rctSubledger.setTransactionDate(new Date());
                rctSubledger.setPostedDate(arBatch.getDepositDate());
                rctSubledger.setCreatedOn(new Date());
                rctSubledger.setCreatedBy(loginUser.getUserId());
                accountingLedgerDAO.save(rctSubledger);
            }
            //Insert Payment Record for Transaction with status Paid
            Transaction apPayment = new Transaction();
            PropertyUtils.copyProperties(apPayment, transaction);
            apPayment.setTransactionType(TRANSACTION_TYPE_PAYAMENT);
            apPayment.setStatus(STATUS_PAID);
            apPayment.setCleared(NO);
            apPayment.setVoidTransaction(NO);
            apPayment.setTransactionDate(arBatch.getDepositDate());
            apPayment.setPostedDate(arBatch.getDepositDate());
            apPayment.setDueDate(arBatch.getDepositDate());
            apPayment.setBillTo(YES);
            apPayment.setArBatchId(arBatch.getBatchId());
            apPayment.setApBatchId(null);
            apPayment.setBalance(transaction.getTransactionAmt());
            apPayment.setBalanceInProcess(transaction.getTransactionAmt());
            apPayment.setCreatedOn(new Date());
            apPayment.setCreatedBy(loginUser.getUserId());
            apPayment.setUpdatedOn(null);
            apPayment.setUpdatedBy(null);
            accountingTransactionDAO.save(apPayment);
            ApTransactionHistory apTransactionHistory = new ApTransactionHistory(apPayment);
            apTransactionHistory.setInvoiceDate(transaction.getTransactionDate());
            apTransactionHistory.setAmount(-apPayment.getTransactionAmt());
            apTransactionHistory.setCreatedBy(loginUser.getLoginName());
            apTransactionHistoryDAO.save(apTransactionHistory);
            String batchType = "DEPOSIT";
            if (CommonUtils.isEqual(arBatch.getBatchType(), AccountingConstants.AR_NET_SETT_BATCH)) {
                batchType = "NET SETT";
            }
            StringBuilder desc = new StringBuilder(batchType).append(" Payment for Invoice '");
            desc.append(apPayment.getInvoiceNumber()).append("' of Vendor '");
            desc.append(apPayment.getCustName()).append("(").append(apPayment.getCustNo()).append(")'");
            desc.append(" paid by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
            String key = apPayment.getCustNo() + "-" + apPayment.getInvoiceNumber();
            AuditNotesUtils.insertAuditNotes(desc.toString(), AP_INVOICE, key, AP_INVOICE, loginUser);
            //Update original records
            transaction.setStatus(STATUS_PAID);
            transaction.setBalance(0d);
            transaction.setBalanceInProcess(0d);
            transaction.setUpdatedOn(new Date());
            transaction.setUpdatedBy(loginUser.getUserId());
            accountingTransactionDAO.update(transaction);
        }
    }

    private static void handlingArTransactions(ArBatch arBatch, User user, HttpServletRequest request) throws Exception {
        StringBuilder imagePath = new StringBuilder(request.getScheme()).append("://");
        imagePath.append(request.getServerName()).append(":").append(request.getServerPort());
        imagePath.append(request.getContextPath()).append(LoadLogisoftProperties.getProperty("application.image.logo"));
        PaymentsDAO paymentsDAO = new PaymentsDAO();
        AccountingTransactionDAO accountingTransactionDAO = new AccountingTransactionDAO();
        ArTransactionHistoryDAO arTransactionHistoryDAO = new ArTransactionHistoryDAO();
        AccountingLedgerDAO accountingLedgerDAO = new AccountingLedgerDAO();
        CustomerAccountingDAO accountingDAO = new CustomerAccountingDAO();
        EmailschedulerDAO emailDAO = new EmailschedulerDAO();
        List<Payments> arTransactionsList = paymentsDAO.getArTransactions(arBatch.getBatchId());
        Set<String> lclFileNos = new HashSet<String>();
        for (Payments payment : arTransactionsList) {
            Transaction ar = accountingTransactionDAO.findById(payment.getTransactionId());
            double paymentAmount = payment.getPaymentAmt() + payment.getAdjustmentAmt();
            ar.setChequeNumber(arBatch.getBatchId().toString());
            ar.setBalance(ar.getBalance() - paymentAmount);
            ArTransactionHistory history = new ArTransactionHistory();
            history.setCustomerNumber(ar.getCustNo());
            history.setBlNumber(ar.getBillLaddingNo());
            history.setInvoiceNumber(ar.getInvoiceNumber());
            history.setInvoiceDate(ar.getTransactionDate());
            history.setTransactionDate(arBatch.getDepositDate());
            history.setPostedDate(arBatch.getDepositDate());
            history.setTransactionAmount(-payment.getPaymentAmt());
            history.setAdjustmentAmount(-payment.getAdjustmentAmt());
            history.setGlAccountNumber(payment.getChargeCode());
            history.setCustomerReferenceNumber(ar.getCustomerReferenceNo());
            history.setVoyageNumber(ar.getVoyageNo());
            history.setCheckNumber(payment.getCheck_no());
            history.setArBatchId(arBatch.getBatchId());
            history.setApBatchId(null);
            history.setTransactionType("AR PY");
            history.setCreatedBy(user.getLoginName());
            history.setCreatedDate(new Date());
            arTransactionHistoryDAO.save(history);
            if (CommonUtils.isEqualIgnoreCase(arBatch.getBatchType(), AccountingConstants.AR_NET_SETT_BATCH)) {
                TransactionLedger netSettSubledger = new TransactionLedger(ar);
                netSettSubledger.setTransactionAmt(payment.getPaymentAmt());
                netSettSubledger.setStatus(STATUS_OPEN);
                netSettSubledger.setGlAccountNumber(arBatch.getGlAccountNo());
                netSettSubledger.setTransactionAmt(-payment.getPaymentAmt());
                netSettSubledger.setBalance(-payment.getPaymentAmt());
                netSettSubledger.setBalanceInProcess(-payment.getPaymentAmt());
                netSettSubledger.setTransactionType(AccountingConstants.TRANSACTION_TYPE_CR);
                netSettSubledger.setSubledgerSourceCode(AccountingConstants.SUBLEDGER_CODE_NETSETT);
                netSettSubledger.setArBatchId(arBatch.getBatchId());
                netSettSubledger.setApBatchId(null);
                netSettSubledger.setCustomerReferenceNo("NET SETT Batch:" + arBatch.getBatchId().toString());
                netSettSubledger.setTransactionDate(new Date());
                netSettSubledger.setPostedDate(arBatch.getDepositDate());
                netSettSubledger.setCreatedOn(new Date());
                netSettSubledger.setCreatedBy(user.getUserId());
                accountingLedgerDAO.save(netSettSubledger);
            }
            String invoiceOrBl = CommonUtils.isNotEmpty(ar.getBillLaddingNo()) ? ar.getBillLaddingNo() : ar.getInvoiceNumber();
            if (invoiceOrBl.contains("IMP-")) {
                lclFileNos.add(ar.getDocReceipt());
            }
            String moduleRefId = ar.getCustNo() + "-" + invoiceOrBl;
            if (CommonUtils.isEqualIgnoreCase(ar.getCreditHold(), YES)) {
                if (ar.isEmailed()) {
                    ArCreditHoldUtils.sendEmail(ar, user, false, imagePath.toString());
                }
                ar.setRemovedFromHold(true);
                ar.setEmailed(false);
                ar.setCreditHold(NO);
                StringBuilder desc = new StringBuilder("BL# - ").append(invoiceOrBl).append(" of ");
                desc.append(ar.getCustName()).append("(").append(ar.getCustNo()).append(")");
                desc.append(" taken off credit hold by ").append(user.getLoginName());
                desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
                AuditNotesUtils.insertAuditNotes(desc.toString(), AR_INVOICE, moduleRefId, "AR Credit Hold", user);
            }
            if ((invoiceOrBl.contains("-04-") || invoiceOrBl.contains("IMP-")) && accountingDAO.isNoCreditAccount(ar.getCustNo())) {
                if (invoiceOrBl.contains("-04-")) {//Fcl
                    String[] opsUser = accountingTransactionDAO.getOpsUserNameEmail(invoiceOrBl);
                    if (null != opsUser) {
                        StringBuilder subject = new StringBuilder();
                        subject.append("Payment posted for ");
                        subject.append(invoiceOrBl).append(" / ").append(ar.getCustName());
                        StringBuilder message = new StringBuilder();
                        message.append("The following payment has been applied to:").append(NEWLINE);
                        message.append("Invoice: ").append(invoiceOrBl).append(NEWLINE);
                        message.append("Amount: ").append(NumberUtils.formatAmount(paymentAmount)).append(NEWLINE);
                        message.append("Check Number: ").append(payment.getCheck_no()).append(NEWLINE);
                        message.append("Customer Number: ").append(ar.getCustNo()).append(NEWLINE);
                        message.append("Customer Name: ").append(ar.getCustName()).append(NEWLINE).append(NEWLINE);
                        message.append("AR invoice balance after payment: ").append(NumberUtils.formatAmount(ar.getBalance()));
                        EmailSchedulerVO email = new EmailSchedulerVO();
                        email.setToName(opsUser[0]);
                        email.setToAddress(opsUser[1]);
                        email.setFromName(user.getFirstName());
                        email.setFromAddress(user.getEmail());
                        email.setSubject(subject.toString());
                        email.setHtmlMessage(message.toString().replace(NEWLINE, "<br/>"));
                        email.setTextMessage(message.toString());
                        email.setName("ARBatch");
                        email.setType(CONTACT_MODE_EMAIL);
                        email.setStatus(EMAIL_STATUS_PENDING);
                        email.setNoOfTries(0);
                        email.setEmailDate(new Date());
                        email.setModuleName("AR Batch");
                        email.setModuleId(moduleRefId);
                        email.setUserName(user.getLoginName());
                        emailDAO.save(email);
                    }
                } else if (invoiceOrBl.contains("IMP-") && ar.getBalance() != 0.00) {// Lcl-Imports
                    String logisoftProperty = LoadLogisoftProperties.getProperty("Auto.Emails.FreightPayments");
                    Date today = new Date();
                    if (logisoftProperty != null) {
                        Long fileId = new LclFileNumberDAO().getFileIdByFileNumber(StringUtils.substringAfter(invoiceOrBl, "IMP-"));
                        LclBooking lclBooking = new LCLBookingDAO().findById(fileId);
                        String terminalLocation = "";
                        if (lclBooking.getTerminal() != null) {
                            RefTerminal terminal = new TerminalDAO().findByTerminalNo(String.valueOf(lclBooking.getTerminal().getTrmnum()));
                            if (terminal != null) {
                                terminalLocation = terminal.getTrmnum() + "-" + terminal.getTerminalLocation();
                            }
                        }
                        StringBuilder subject = new StringBuilder();
                        subject.append("Payment posted for ");
                        subject.append(invoiceOrBl).append(" / ").append(ar.getCustName()).append(" / ").append(terminalLocation);
                        StringBuilder message = new StringBuilder();
                        message.append("The following payment has been applied to:").append(NEWLINE);
                        message.append("Invoice: ").append(invoiceOrBl).append(NEWLINE);
                        message.append("Amount: ").append(NumberUtils.formatAmount(paymentAmount)).append(NEWLINE);
                        message.append("Check Number: ").append(payment.getCheck_no()).append(NEWLINE);
                        message.append("Customer Number: ").append(ar.getCustNo()).append(NEWLINE);
                        message.append("Customer Name: ").append(ar.getCustName()).append(NEWLINE).append(NEWLINE);
                        message.append("AR invoice balance after payment: ").append(NumberUtils.formatAmount(ar.getBalance()));
                        EmailSchedulerVO email = new EmailSchedulerVO();
                        email.setToName(logisoftProperty);
                        email.setToAddress(logisoftProperty);
                        email.setFromName(user.getFirstName());
                        email.setFromAddress(user.getEmail());
                        email.setSubject(subject.toString());
                        email.setHtmlMessage(message.toString().replace(NEWLINE, "<br/>"));
                        email.setTextMessage(message.toString());
                        email.setName("ARBatch");
                        email.setType(CONTACT_MODE_EMAIL);
                        email.setStatus(EMAIL_STATUS_PENDING);
                        email.setNoOfTries(0);
                        email.setEmailDate(today);
                        email.setModuleName("AR Batch");
                        email.setModuleId(moduleRefId);
                        email.setUserName(user.getLoginName());
                        emailDAO.save(email);
                        FileOutputStream fout = null;
                        try {
                            //attach invoice in DR under the MISCELLANEOUS column
                            StringBuilder path = new StringBuilder();
                            String companyName = new SystemRulesDAO().getSystemRulesByCode("CompanyName");
                            path.append(LoadLogisoftProperties.getProperty("reportLocation")).append("/");
                            path.append(companyName).append("/");
                            path.append("LCL IMPORTS DR").append("/");
                            path.append(DateUtils.formatDate(today, "yyyy/MM/dd")).append("/");
                            File dir = new File(path.toString());
                            if (!dir.exists()) {
                                dir.mkdirs();
                            }
                            StringBuilder fileName = new StringBuilder();
                            fileName.append(DateUtils.formatDate(today, "yyyyMMdd_kkmmss"));
                            fileName.append("_").append(user.getLoginName());
                            fileName.append(".txt");
                            File outFile = new File(dir, fileName.toString());
                            fout = new FileOutputStream(outFile);
                            byte[] b = message.toString().getBytes();
                            fout.write(b);
                            fout.flush();
                            DocumentStoreLog document = new DocumentStoreLog();
                            document.setScreenName("LCL IMPORTS DR");
                            document.setDocumentName("MISCELLANEOUS");
                            document.setDocumentID(StringUtils.substringAfter(invoiceOrBl, "IMP-"));
                            document.setFileLocation(path.toString());
                            document.setFileName(fileName.toString());
                            document.setOperation(ConstantsInterface.PAGE_ACTION_ATTACH);
                            document.setDateOprDone(today);
                            document.setComments("Ar Batch Attachment");
                            document.setFileSize("1 KB");
                            new DocumentStoreLogDAO().save(document);

                            //attach invoice in AR under the Action column
                            DocumentStoreLog document1 = new DocumentStoreLog();
                            document1.setScreenName("INVOICE");
                            document1.setDocumentName("INVOICE");
                            document1.setDocumentID(ar.getCustNo() + "-" + invoiceOrBl);
                            StringBuilder path1 = new StringBuilder();
                            path1.append(LoadLogisoftProperties.getProperty("reportLocation")).append("/");
                            path1.append(companyName).append("/");
                            path1.append("INVOICE").append("/");
                            path1.append(DateUtils.formatDate(today, "yyyy/MM/dd")).append("/");
                            File dir1 = new File(path1.toString());
                            if (!dir1.exists()) {
                                dir1.mkdirs();
                            }
                            File outFile1 = new File(dir1, fileName.toString());
                            fout = new FileOutputStream(outFile1);
                            byte[] b1 = message.toString().getBytes();
                            fout.write(b1);
                            document1.setFileLocation(path1.toString());
                            document1.setFileName(fileName.toString());
                            document1.setOperation(ConstantsInterface.PAGE_ACTION_ATTACH);
                            document1.setDateOprDone(today);
                            document1.setComments("Ar Batch Attachment");
                            document1.setFileSize("1 KB");
                            new DocumentStoreLogDAO().save(document1);
                        } catch (Exception e) {
                            throw e;
                        } finally {
                            IOUtils.closeQuietly(fout);
                        }
                    }
                }
            }
            accountingTransactionDAO.update(ar);
        }
        TransactionDAO transactionDAO = new TransactionDAO();
        LclFileNumberDAO lclFileNumberDAO = new LclFileNumberDAO();
        LCLBookingAcTransDAO lCLBookingAcTransDAO = new LCLBookingAcTransDAO();
        for (String lclFileNo : lclFileNos) {
            String totalArBalanceAmount = transactionDAO.getLclARBalance(lclFileNo, lclFileNo);
            if (totalArBalanceAmount.equalsIgnoreCase("0.00")) {
                Long fileNumberId = lclFileNumberDAO.getFileIdByFileNumber(lclFileNo);
                lCLBookingAcTransDAO.updateTransType(fileNumberId, user.getUserId());
            }
        }
    }

    private static void handlingChargeCodes(ArBatch arBatch, User loginUser) throws Exception {
        AccountingLedgerDAO accountingLedgerDAO = new AccountingLedgerDAO();
        List<Payments> chargeCodes = new PaymentsDAO().getChargeCodes(arBatch.getBatchId());
        TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
        if (CommonUtils.isEqual(arBatch.getBatchType(), AccountingConstants.AR_NET_SETT_BATCH)) {
            for (Payments payments : chargeCodes) {
                TransactionLedger chargeCodeSubledger = new TransactionLedger();
                chargeCodeSubledger.setTransactionAmt(-payments.getPaymentAmt());
                chargeCodeSubledger.setBalance(0d);
                chargeCodeSubledger.setBalanceInProcess(0d);
                chargeCodeSubledger.setStatus(AccountingConstants.STATUS_CHARGECODE);
                chargeCodeSubledger.setCustomerReferenceNo("BatchNo:" + arBatch.getBatchId() + " ChkNo:" + "NETSETT-" + arBatch.getBatchId());
                chargeCodeSubledger.setTransactionType("NS");
                TradingPartner tradingPartner = tradingPartnerDAO.findById(payments.getCustNo());
                chargeCodeSubledger.setCustName(tradingPartner.getAccountName());
                chargeCodeSubledger.setCustNo(tradingPartner.getAccountno());
                chargeCodeSubledger.setChargeCode(payments.getChargeCode());
                chargeCodeSubledger.setGlAccountNumber(payments.getChargeCode());
                chargeCodeSubledger.setSubledgerSourceCode(AccountingConstants.SUBLEDGER_CODE_NETSETT);
                chargeCodeSubledger.setArBatchId(arBatch.getBatchId());
                chargeCodeSubledger.setApBatchId(null);
                chargeCodeSubledger.setCurrencyCode(AccountingConstants.CURRENCY_USD);
                chargeCodeSubledger.setDescription(AccountingConstants.CHARGE_CODE);
                chargeCodeSubledger.setInvoiceNumber(payments.getNotes());
                chargeCodeSubledger.setBankAccountNumber(arBatch.getGlAccountNo());
                chargeCodeSubledger.setChequeNumber("NETSETT-" + arBatch.getBatchId().toString());
                chargeCodeSubledger.setTransactionDate(new Date());
                chargeCodeSubledger.setPostedDate(arBatch.getDepositDate());
                chargeCodeSubledger.setCreatedOn(new Date());
                chargeCodeSubledger.setCreatedBy(loginUser.getUserId());
                accountingLedgerDAO.save(chargeCodeSubledger);
            }
        } else {
            for (Payments payments : chargeCodes) {
                TransactionLedger chargeCodeSubledger = new TransactionLedger();
                chargeCodeSubledger.setTransactionDate(new Date());
                chargeCodeSubledger.setPostedDate(arBatch.getDepositDate());
                chargeCodeSubledger.setTransactionAmt(-payments.getPaymentAmt());
                chargeCodeSubledger.setBalance(0d);
                chargeCodeSubledger.setBalanceInProcess(0d);
                chargeCodeSubledger.setStatus(AccountingConstants.STATUS_CHARGECODE);
                chargeCodeSubledger.setCustomerReferenceNo("BatchNo:" + arBatch.getBatchId() + " ChkNo:" + payments.getCheck_no());
                chargeCodeSubledger.setTransactionType(AccountingConstants.TRANSACTION_TYPE_CR);
                TradingPartner tradingPartner = tradingPartnerDAO.findById(payments.getCustNo());
                chargeCodeSubledger.setCustName(tradingPartner.getAccountName());
                chargeCodeSubledger.setCustNo(tradingPartner.getAccountno());
                chargeCodeSubledger.setChargeCode(payments.getChargeCode());
                chargeCodeSubledger.setGlAccountNumber(payments.getChargeCode());
                chargeCodeSubledger.setSubledgerSourceCode(AccountingConstants.AR_BATCH_CLOSING_SUBLEDGER_CODE);
                chargeCodeSubledger.setArBatchId(arBatch.getBatchId());
                chargeCodeSubledger.setApBatchId(null);
                chargeCodeSubledger.setCurrencyCode(AccountingConstants.CURRENCY_USD);
                chargeCodeSubledger.setDescription(AccountingConstants.CHARGE_CODE);
                chargeCodeSubledger.setInvoiceNumber(payments.getNotes());
                chargeCodeSubledger.setBankAccountNumber(arBatch.getGlAccountNo());
                chargeCodeSubledger.setChequeNumber(payments.getCheck_no());
                chargeCodeSubledger.setCreatedOn(new Date());
                chargeCodeSubledger.setCreatedBy(loginUser.getUserId());
                accountingLedgerDAO.save(chargeCodeSubledger);
            }
        }
    }

    private static void handlingOnAccounts(ArBatch arBatch, User loginUser) throws Exception {
        AccountingTransactionDAO accountingTransactionDAO = new AccountingTransactionDAO();
        CustAddressDAO custAddressDAO = new CustAddressDAO();
        AccountingLedgerDAO accountingLedgerDAO = new AccountingLedgerDAO();
        ArTransactionHistoryDAO arTransactionHistoryDAO = new ArTransactionHistoryDAO();
        TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
        PaymentsDAO paymentsDAO = new PaymentsDAO();
        List<Payments> onAccounts = paymentsDAO.getOnAccounts(arBatch.getBatchId());
        for (Payments payments : onAccounts) {
            TradingPartner tradingPartner = tradingPartnerDAO.findById(payments.getCustNo());
            Transaction onAccountTransaction = accountingTransactionDAO.getOnAccountTransaction(payments.getCustNo());
            if (null != onAccountTransaction) {
                onAccountTransaction.setCustomerReferenceNo(StringUtils.left(payments.getCheck_no(), 500));
                onAccountTransaction.setBalance(onAccountTransaction.getBalance() - payments.getPaymentAmt());
                onAccountTransaction.setBalanceInProcess(onAccountTransaction.getBalanceInProcess() - payments.getPaymentAmt());
                onAccountTransaction.setStatus(AccountingConstants.STATUS_OPEN);
                onAccountTransaction.setUpdatedOn(new Date());
                onAccountTransaction.setUpdatedBy(loginUser.getUserId());
                accountingTransactionDAO.update(onAccountTransaction);
            } else {
                onAccountTransaction = new Transaction();
                onAccountTransaction.setTransactionType(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE);
                onAccountTransaction.setBillTo(YES);
                onAccountTransaction.setTransactionAmt(0d);
                onAccountTransaction.setInvoiceNumber(AccountingConstants.ON_ACCOUNT);
                onAccountTransaction.setCustomerReferenceNo(StringUtils.left(payments.getCheck_no(), 500));
                onAccountTransaction.setCustNo(tradingPartner.getAccountno());
                onAccountTransaction.setCustName(tradingPartner.getAccountName());
                onAccountTransaction.setTransactionDate(arBatch.getDepositDate());
                onAccountTransaction.setPostedDate(arBatch.getDepositDate());
                onAccountTransaction.setBalance(-payments.getPaymentAmt());
                onAccountTransaction.setBalanceInProcess(-payments.getPaymentAmt());
                onAccountTransaction.setCurrencyCode(AccountingConstants.CURRENCY_USD);
                onAccountTransaction.setStatus(AccountingConstants.STATUS_OPEN);
                onAccountTransaction.setCreatedOn(arBatch.getDepositDate());
                onAccountTransaction.setCreatedBy(loginUser.getUserId());
                accountingTransactionDAO.save(onAccountTransaction);
            }
            if (CommonUtils.isEqual(arBatch.getBatchType(), AccountingConstants.AR_NET_SETT_BATCH)) {
                TransactionLedger onAccountSubledger = new TransactionLedger();
                onAccountSubledger.setTransactionDate(new Date());
                onAccountSubledger.setPostedDate(arBatch.getDepositDate());
                onAccountSubledger.setTransactionAmt(-payments.getPaymentAmt());
                onAccountSubledger.setBalance(-payments.getPaymentAmt());
                onAccountSubledger.setBalanceInProcess(-payments.getPaymentAmt());
                onAccountSubledger.setStatus(AccountingConstants.STATUS_OPEN);
                onAccountSubledger.setTransactionType(AccountingConstants.TRANSACTION_TYPE_CR);
                onAccountSubledger.setCustName(custAddressDAO.getCustomerName(payments.getCustNo()));
                onAccountSubledger.setCustNo(payments.getCustNo());
                onAccountSubledger.setGlAccountNumber(arBatch.getGlAccountNo());
                onAccountSubledger.setSubledgerSourceCode(AccountingConstants.SUBLEDGER_CODE_NETSETT);
                onAccountSubledger.setCustomerReferenceNo("NETSETT-" + arBatch.getBatchId());
                onAccountSubledger.setArBatchId(arBatch.getBatchId());
                onAccountSubledger.setApBatchId(null);
                onAccountSubledger.setCurrencyCode(AccountingConstants.CURRENCY_USD);
                onAccountSubledger.setInvoiceNumber(AccountingConstants.ON_ACCOUNT);
                onAccountSubledger.setChequeNumber(arBatch.getBatchId().toString());
                onAccountSubledger.setCreatedOn(new Date());
                onAccountSubledger.setCreatedBy(loginUser.getUserId());
                accountingLedgerDAO.save(onAccountSubledger);
            }
            ArTransactionHistory arTransactionHistory = new ArTransactionHistory();
            arTransactionHistory.setCustomerNumber(onAccountTransaction.getCustNo());
            arTransactionHistory.setBlNumber(onAccountTransaction.getBillLaddingNo());
            arTransactionHistory.setInvoiceNumber(onAccountTransaction.getInvoiceNumber());
            arTransactionHistory.setInvoiceDate(onAccountTransaction.getTransactionDate());
            arTransactionHistory.setTransactionDate(arBatch.getDepositDate());
            arTransactionHistory.setPostedDate(arBatch.getDepositDate());
            arTransactionHistory.setTransactionAmount(-payments.getPaymentAmt());
            arTransactionHistory.setCustomerReferenceNumber(onAccountTransaction.getCustomerReferenceNo());
            arTransactionHistory.setVoyageNumber(onAccountTransaction.getVoyageNo());
            arTransactionHistory.setCheckNumber(payments.getCheck_no());
            arTransactionHistory.setArBatchId(arBatch.getBatchId());
            arTransactionHistory.setApBatchId(null);
            arTransactionHistory.setTransactionType("OA INV");
            arTransactionHistory.setCreatedBy(loginUser.getLoginName());
            arTransactionHistory.setCreatedDate(new Date());
            arTransactionHistoryDAO.save(arTransactionHistory);
            payments.setTransactionId(onAccountTransaction.getTransactionId());
            payments.setTransactionType(onAccountTransaction.getTransactionType());
            paymentsDAO.update(payments);
        }
    }

    private static void handlingPrepayments(ArBatch arBatch, User loginUser) throws Exception {
        AccountingTransactionDAO accountingTransactionDAO = new AccountingTransactionDAO();
        AccountingLedgerDAO accountingLedgerDAO = new AccountingLedgerDAO();
        TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
        NotesBC notesBC = new NotesBC();
        ArTransactionHistoryDAO arTransactionHistoryDAO = new ArTransactionHistoryDAO();
        PaymentsDAO paymentsDAO = new PaymentsDAO();
        List<Payments> paymentsList = paymentsDAO.getPrepayments(arBatch.getBatchId());
        for (Payments payments : paymentsList) {
            TradingPartner tradingPartner = tradingPartnerDAO.findById(payments.getCustNo());
            Transaction prepaymentTransaction = accountingTransactionDAO.getPrepaymentTransaction(tradingPartner.getAccountno(), payments.getBillLaddingNo());
            if (null != prepaymentTransaction) {
                prepaymentTransaction.setBalance(prepaymentTransaction.getBalance() - payments.getPaymentAmt());
                prepaymentTransaction.setBalanceInProcess(prepaymentTransaction.getBalanceInProcess() - payments.getPaymentAmt());
                prepaymentTransaction.setStatus(AccountingConstants.STATUS_OPEN);
                prepaymentTransaction.setUpdatedOn(new Date());
                prepaymentTransaction.setUpdatedBy(loginUser.getUserId());
                accountingTransactionDAO.update(prepaymentTransaction);
            } else {
                prepaymentTransaction = new Transaction();
                prepaymentTransaction.setTransactionType(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE);
                prepaymentTransaction.setTransactionAmt(0d);
                prepaymentTransaction.setBillTo(YES);
                prepaymentTransaction.setInvoiceNumber(AccountingConstants.PRE_PAYMENT);
                prepaymentTransaction.setCustomerReferenceNo(StringUtils.left(payments.getCheck_no(), 500));
                String fclOrLclBillLaddingNo = null;
                if (payments.getBillLaddingNo().startsWith("04")) {
                    fclOrLclBillLaddingNo = payments.getBillLaddingNo().substring(2);
                    if (payments.getBillLaddingNo().startsWith("04-")) {
                        String drcpt = payments.getBillLaddingNo().substring(3);
                        fclOrLclBillLaddingNo = drcpt;
                        if (drcpt.contains("-")) {
                            prepaymentTransaction.setDocReceipt(StringUtils.substringBefore(drcpt, "-"));
                        } else {
                            prepaymentTransaction.setDocReceipt(drcpt);
                        }
                    } else {
                        String drcptA = payments.getBillLaddingNo().substring(2);
                        if (drcptA.contains("-")) {
                            prepaymentTransaction.setDocReceipt(StringUtils.substringBefore(drcptA, "-"));
                        } else {
                            prepaymentTransaction.setDocReceipt(drcptA);
                        }
                    }
                } else {
                    prepaymentTransaction.setDocReceipt(payments.getBillLaddingNo());
                    prepaymentTransaction.setBillLaddingNo(payments.getBillLaddingNo());
                }
                if (fclOrLclBillLaddingNo != null) {
                    String bolId = new FclBlDAO().getBillLaddingNo(fclOrLclBillLaddingNo);
                    if (bolId != null) {
                        prepaymentTransaction.setBillLaddingNo(bolId);
                    } else {
                        prepaymentTransaction.setBillLaddingNo(fclOrLclBillLaddingNo);
                    }
                }
                if (prepaymentTransaction.getDocReceipt().length() > 6) {
                    prepaymentTransaction.setDocReceipt(null);
                }
                prepaymentTransaction.setCustNo(tradingPartner.getAccountno());
                prepaymentTransaction.setCustName(tradingPartner.getAccountName());
                prepaymentTransaction.setTransactionDate(arBatch.getDepositDate());
                prepaymentTransaction.setPostedDate(arBatch.getDepositDate());
                prepaymentTransaction.setBalance(-payments.getPaymentAmt());
                prepaymentTransaction.setBalanceInProcess(-payments.getPaymentAmt());
                prepaymentTransaction.setCurrencyCode(AccountingConstants.CURRENCY_USD);
                prepaymentTransaction.setStatus(AccountingConstants.STATUS_OPEN);
                prepaymentTransaction.setCreatedBy(loginUser.getUserId());
                prepaymentTransaction.setCreatedOn(new Date());
                accountingTransactionDAO.save(prepaymentTransaction);
            }
            payments.setTransactionId(prepaymentTransaction.getTransactionId());
            payments.setTransactionType(prepaymentTransaction.getTransactionType());
            paymentsDAO.update(payments);
            String notes = payments.getNotes();
            if (CommonUtils.isNotEmpty(notes)) {
                NotesForm notesForm = new NotesForm();
                notesForm.setModuleId(AR_INVOICE);
                String invoiceOrBl = "";
                if (CommonUtils.isNotEmpty(prepaymentTransaction.getBillLaddingNo())) {
                    invoiceOrBl = prepaymentTransaction.getBillLaddingNo();
                } else {
                    invoiceOrBl = prepaymentTransaction.getInvoiceNumber();
                }
                notesForm.setModuleRefId(prepaymentTransaction.getCustNo() + "-" + invoiceOrBl);
                notesForm.setNotes(notes);
                notesBC.saveNotes(notesForm, payments.getUserName());
            }
            if (CommonUtils.isEqualIgnoreCase(arBatch.getBatchType(), AccountingConstants.AR_NET_SETT_BATCH)) {
                TransactionLedger prepaymentSubledger = new TransactionLedger();
                prepaymentSubledger.setBillLaddingNo(payments.getBillLaddingNo());
                prepaymentSubledger.setTransactionDate(new Date());
                prepaymentSubledger.setPostedDate(arBatch.getDepositDate());
                prepaymentSubledger.setTransactionAmt(-payments.getPaymentAmt());
                prepaymentSubledger.setBalance(-payments.getPaymentAmt());
                prepaymentSubledger.setBalanceInProcess(-payments.getPaymentAmt());
                prepaymentSubledger.setStatus(AccountingConstants.STATUS_OPEN);
                prepaymentSubledger.setTransactionType(AccountingConstants.TRANSACTION_TYPE_CR);
                prepaymentSubledger.setCustNo(tradingPartner.getAccountno());
                prepaymentSubledger.setCustName(tradingPartner.getAccountName());
                prepaymentSubledger.setGlAccountNumber(arBatch.getGlAccountNo());
                prepaymentSubledger.setSubledgerSourceCode(AccountingConstants.SUBLEDGER_CODE_NETSETT);
                prepaymentSubledger.setCustomerReferenceNo("NETSETT-" + arBatch.getBatchId());
                prepaymentSubledger.setArBatchId(arBatch.getBatchId());
                prepaymentSubledger.setApBatchId(null);
                prepaymentSubledger.setCurrencyCode(AccountingConstants.CURRENCY_USD);
                prepaymentSubledger.setInvoiceNumber(AccountingConstants.PRE_PAYMENT);
                prepaymentSubledger.setChequeNumber(arBatch.getBatchId().toString());
                prepaymentSubledger.setCreatedOn(new Date());
                prepaymentSubledger.setCreatedBy(loginUser.getUserId());
                accountingLedgerDAO.save(prepaymentSubledger);
            }
            ArTransactionHistory arTransactionHistory = new ArTransactionHistory();
            arTransactionHistory.setCustomerNumber(prepaymentTransaction.getCustNo());
            arTransactionHistory.setBlNumber(prepaymentTransaction.getBillLaddingNo());
            arTransactionHistory.setInvoiceNumber(prepaymentTransaction.getInvoiceNumber());
            arTransactionHistory.setInvoiceDate(prepaymentTransaction.getTransactionDate());
            arTransactionHistory.setTransactionDate(arBatch.getDepositDate());
            arTransactionHistory.setPostedDate(arBatch.getDepositDate());
            arTransactionHistory.setTransactionAmount(-payments.getPaymentAmt());
            arTransactionHistory.setCustomerReferenceNumber(prepaymentTransaction.getCustomerReferenceNo());
            arTransactionHistory.setVoyageNumber(prepaymentTransaction.getVoyageNo());
            arTransactionHistory.setCheckNumber(payments.getCheck_no());
            arTransactionHistory.setArBatchId(arBatch.getBatchId());
            arTransactionHistory.setApBatchId(null);
            arTransactionHistory.setTransactionType("PP INV");
            arTransactionHistory.setCreatedBy(loginUser.getLoginName());
            arTransactionHistory.setCreatedDate(new Date());
            arTransactionHistoryDAO.save(arTransactionHistory);
        }
    }

    private static void handlingAdjustAmounts(ArBatch arBatch, User loginUser) throws Exception {
        TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
        AccountingLedgerDAO accountingLedgerDAO = new AccountingLedgerDAO();
        List<Payments> adjustments = new PaymentsDAO().getAdjustments(arBatch.getBatchId());
        if (CommonUtils.isEqualIgnoreCase(arBatch.getBatchType(), AccountingConstants.AR_NET_SETT_BATCH)) {
            for (Payments payments : adjustments) {
                TradingPartner tradingPartner = tradingPartnerDAO.findById(payments.getCustNo());
                TransactionLedger adjustmentSubledger = new TransactionLedger();
                adjustmentSubledger.setTransactionAmt(payments.getAdjustmentAmt());
                adjustmentSubledger.setTransactionDate(new Date());
                adjustmentSubledger.setPostedDate(arBatch.getDepositDate());
                adjustmentSubledger.setBalance(0d);
                adjustmentSubledger.setBalanceInProcess(0d);
                adjustmentSubledger.setStatus(AccountingConstants.STATUS_OPEN);
                adjustmentSubledger.setCustomerReferenceNo("BatchNo:" + arBatch.getBatchId() + " Adj For ChkNo:" + payments.getCheck_no());
                adjustmentSubledger.setTransactionType(AccountingConstants.TRANSACTION_TYPE_CR);
                adjustmentSubledger.setCustNo(tradingPartner.getAccountno());
                adjustmentSubledger.setCustName(tradingPartner.getAccountName());
                adjustmentSubledger.setChargeCode(payments.getChargeCode());
                adjustmentSubledger.setGlAccountNumber(payments.getChargeCode());
                adjustmentSubledger.setSubledgerSourceCode(AccountingConstants.SUBLEDGER_CODE_NETSETT);
                adjustmentSubledger.setArBatchId(arBatch.getBatchId());
                adjustmentSubledger.setApBatchId(null);
                adjustmentSubledger.setCurrencyCode(AccountingConstants.CURRENCY_USD);
                adjustmentSubledger.setInvoiceNumber(AccountingConstants.ADJ);
                adjustmentSubledger.setBillLaddingNo(payments.getBillLaddingNo());
                adjustmentSubledger.setChequeNumber(payments.getCheck_no());
                adjustmentSubledger.setCreatedBy(loginUser.getUserId());
                adjustmentSubledger.setCreatedOn(new Date());
                accountingLedgerDAO.save(adjustmentSubledger);
                TransactionLedger offsetAdjustmentSubledger = new TransactionLedger();
                offsetAdjustmentSubledger.setTransactionAmt(-payments.getAdjustmentAmt());
                offsetAdjustmentSubledger.setTransactionDate(new Date());
                offsetAdjustmentSubledger.setPostedDate(arBatch.getDepositDate());
                offsetAdjustmentSubledger.setBalance(0d);
                offsetAdjustmentSubledger.setBalanceInProcess(0d);
                offsetAdjustmentSubledger.setStatus(AccountingConstants.STATUS_OPEN);
                offsetAdjustmentSubledger.setCustomerReferenceNo("BatchNo:" + arBatch.getBatchId() + " Adj For ChkNo:" + payments.getCheck_no());
                offsetAdjustmentSubledger.setTransactionType(AccountingConstants.TRANSACTION_TYPE_CR);
                offsetAdjustmentSubledger.setCustNo(tradingPartner.getAccountno());
                offsetAdjustmentSubledger.setCustName(tradingPartner.getAccountName());
                offsetAdjustmentSubledger.setChargeCode(arBatch.getGlAccountNo());
                offsetAdjustmentSubledger.setGlAccountNumber(arBatch.getGlAccountNo());
                offsetAdjustmentSubledger.setSubledgerSourceCode(AccountingConstants.SUBLEDGER_CODE_NETSETT);
                offsetAdjustmentSubledger.setArBatchId(arBatch.getBatchId());
                offsetAdjustmentSubledger.setApBatchId(null);
                offsetAdjustmentSubledger.setCurrencyCode(AccountingConstants.CURRENCY_USD);
                offsetAdjustmentSubledger.setInvoiceNumber(AccountingConstants.ADJ);
                offsetAdjustmentSubledger.setBillLaddingNo(payments.getBillLaddingNo());
                offsetAdjustmentSubledger.setChequeNumber(payments.getCheck_no());
                offsetAdjustmentSubledger.setCreatedBy(loginUser.getUserId());
                offsetAdjustmentSubledger.setCreatedOn(new Date());
                accountingLedgerDAO.save(offsetAdjustmentSubledger);
            }
        } else {
            for (Payments payments : adjustments) {
                TradingPartner tradingPartner = tradingPartnerDAO.findById(payments.getCustNo());
                TransactionLedger adjustmentSubledger = new TransactionLedger();
                adjustmentSubledger.setTransactionAmt(payments.getAdjustmentAmt());
                adjustmentSubledger.setTransactionDate(new Date());
                adjustmentSubledger.setPostedDate(arBatch.getDepositDate());
                adjustmentSubledger.setBalance(0d);
                adjustmentSubledger.setBalanceInProcess(0d);
                adjustmentSubledger.setStatus(AccountingConstants.STATUS_OPEN);
                adjustmentSubledger.setCustomerReferenceNo("BatchNo:" + arBatch.getBatchId() + " Adj For ChkNo:" + payments.getCheck_no());
                adjustmentSubledger.setTransactionType(AccountingConstants.TRANSACTION_TYPE_CR);
                adjustmentSubledger.setCustNo(tradingPartner.getAccountno());
                adjustmentSubledger.setCustName(tradingPartner.getAccountName());
                adjustmentSubledger.setChargeCode(payments.getChargeCode());
                adjustmentSubledger.setGlAccountNumber(payments.getChargeCode());
                adjustmentSubledger.setSubledgerSourceCode(AccountingConstants.AR_BATCH_CLOSING_SUBLEDGER_CODE);
                adjustmentSubledger.setArBatchId(arBatch.getBatchId());
                adjustmentSubledger.setApBatchId(null);
                adjustmentSubledger.setCurrencyCode(AccountingConstants.CURRENCY_USD);
                adjustmentSubledger.setInvoiceNumber(AccountingConstants.ADJ);
                adjustmentSubledger.setBillLaddingNo(payments.getBillLaddingNo());
                adjustmentSubledger.setChequeNumber(payments.getCheck_no());
                adjustmentSubledger.setCreatedBy(loginUser.getUserId());
                adjustmentSubledger.setCreatedOn(new Date());
                accountingLedgerDAO.save(adjustmentSubledger);
            }
        }
    }

    private static void insertRCTSubledgerForBatch(ArBatch arBatch, User loginUser) throws Exception {
        TransactionLedger rctSubledger = new TransactionLedger();
        rctSubledger.setBalance(0d);
        rctSubledger.setBalanceInProcess(0d);
        rctSubledger.setCurrencyCode("USD");
        rctSubledger.setTransactionType(AccountingConstants.TRANSACTION_TYPE_CR);
        rctSubledger.setGlAccountNumber(arBatch.getGlAccountNo());
        rctSubledger.setTransactionDate(new Date());
        rctSubledger.setPostedDate(arBatch.getDepositDate());
        rctSubledger.setSubledgerSourceCode(AccountingConstants.AR_BATCH_CLOSING_SUBLEDGER_CODE);
        rctSubledger.setArBatchId(arBatch.getBatchId());
        rctSubledger.setApBatchId(null);
        rctSubledger.setCustomerReferenceNo("BatchNo:" + arBatch.getBatchId());
        rctSubledger.setTransactionAmt(arBatch.getAppliedAmount());
        rctSubledger.setStatus(STATUS_OPEN);
        rctSubledger.setCreatedBy(loginUser.getUserId());
        rctSubledger.setCreatedOn(new Date());
        rctSubledger.setDirectGlAccount(arBatch.isDirectGlAccount());
        new AccountingLedgerDAO().save(rctSubledger);
    }

    public static void copyNsBatchDocuments(String batchId, String customerNo, String invoiceNumber) throws Exception {
        DocumentStoreLogDAO documentStoreLogDAO = new DocumentStoreLogDAO();
        List<DocumentStoreLog> nsBatchDocuments = documentStoreLogDAO.getNsBatchDocuments(batchId);
        StringBuilder copyFolderPath = new StringBuilder(LoadLogisoftProperties.getProperty("reportLocation"));
        copyFolderPath.append("/Documents/INVOICE/").append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
        File copyFolder = new File(copyFolderPath.toString());
        if (!copyFolder.exists()) {
            copyFolder.mkdirs();
        }

        for (DocumentStoreLog document : nsBatchDocuments) {
            String originalFolderPath = document.getFileLocation().endsWith("/") ? document.getFileLocation() : (document.getFileLocation() + "/");
            File originalFile = new File(originalFolderPath, document.getFileName());
            if (originalFile.exists()) {
                File copyFile = new File(copyFolderPath.toString(), originalFile.getName());
                InputStream in = null;
                OutputStream out = null;
                try {
                    in = new FileInputStream(originalFile);
                    out = new FileOutputStream(copyFile);
                    byte[] fileContent = IOUtils.toByteArray(in);
                    IOUtils.write(fileContent, out);
                } catch (Exception e) {
                    throw e;
                } finally {
                    IOUtils.closeQuietly(in);
                    IOUtils.closeQuietly(out);
                }
                DocumentStoreLog copyDocument = new DocumentStoreLog();
                copyDocument.setScreenName("INVOICE");
                copyDocument.setDocumentName("INVOICE");
                copyDocument.setDocumentID(customerNo + "-" + invoiceNumber);
                copyDocument.setFileLocation(copyFolderPath.toString());
                copyDocument.setFileName(originalFile.getName());
                copyDocument.setOperation(document.getOperation());
                copyDocument.setDateOprDone(document.getDateOprDone());
                copyDocument.setComments(document.getComments());
                copyDocument.setFileSize(document.getFileSize());
                documentStoreLogDAO.save(copyDocument);
            }
        }
    }

    private static void postNetSettBatch(ArBatchForm arBatchForm, ArBatch arBatch, User loginUser) throws Exception {
        String customerNumber = arBatchForm.getCustomerNumber();
        if (CommonUtils.isNotEmpty(arBatchForm.getOtherCustomerNumber())) {
            customerNumber = arBatchForm.getOtherCustomerNumber();
        }
        String invoiceNumber = AccountingConstants.SUBLEDGER_CODE_NETSETT + arBatch.getBatchId();
        String notes = arBatch.getNotes();
        String referenceNumber = CommonUtils.isNotEmpty(notes) ? StringUtils.left(notes, 200) : ("From NettSettlement " + arBatch.getBatchId());
        TradingPartner tradingPartner = new TradingPartnerDAO().findById(customerNumber);
        String customerName = tradingPartner.getAccountName();
        //NS Invoice
        Transaction transaction = new Transaction();
        transaction.setBalance(arBatch.getAppliedAmount());
        transaction.setBalanceInProcess(arBatch.getAppliedAmount());
        transaction.setTransactionAmt(arBatch.getAppliedAmount());
        transaction.setInvoiceNumber(invoiceNumber);
        transaction.setCustomerReferenceNo(referenceNumber);
        transaction.setTransactionType(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE);
        transaction.setBillTo(YES);
        transaction.setTransactionDate(arBatch.getDepositDate());
        transaction.setPostedDate(arBatch.getDepositDate());
        transaction.setSubledgerSourceCode(AccountingConstants.SUBLEDGER_CODE_NETSETT);
        transaction.setArBatchId(arBatch.getBatchId());
        transaction.setApBatchId(null);
        transaction.setStatus(AccountingConstants.STATUS_OPEN);
        transaction.setCustNo(customerNumber);
        transaction.setCustName(customerName);
        transaction.setCreatedBy(loginUser.getUserId());
        transaction.setCreatedOn(new Date());
        new AccountingTransactionDAO().save(transaction);
        //Ar Transaction History
        ArTransactionHistory arTransactionHistory = new ArTransactionHistory();
        arTransactionHistory.setCustomerNumber(customerNumber);
        arTransactionHistory.setBlNumber(transaction.getBillLaddingNo());
        arTransactionHistory.setInvoiceNumber(invoiceNumber);
        arTransactionHistory.setInvoiceDate(transaction.getTransactionDate());
        arTransactionHistory.setTransactionDate(transaction.getTransactionDate());
        arTransactionHistory.setPostedDate(transaction.getPostedDate());
        arTransactionHistory.setTransactionAmount(transaction.getTransactionAmt());
        arTransactionHistory.setCustomerReferenceNumber(referenceNumber);
        arTransactionHistory.setVoyageNumber(transaction.getVoyageNo());
        arTransactionHistory.setCheckNumber("NETSETT-" + transaction.getArBatchId());
        arTransactionHistory.setArBatchId(transaction.getArBatchId());
        arTransactionHistory.setApBatchId(null);
        arTransactionHistory.setTransactionType("NS INV");
        arTransactionHistory.setCreatedBy(loginUser.getLoginName());
        arTransactionHistory.setCreatedDate(new Date());
        new ArTransactionHistoryDAO().save(arTransactionHistory);
        //NS Subledger
        TransactionLedger nsSubledger = new TransactionLedger();
        nsSubledger.setArBatchId(arBatch.getBatchId());
        nsSubledger.setApBatchId(null);
        nsSubledger.setTransactionType(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE);
        nsSubledger.setTransactionDate(new Date());
        nsSubledger.setPostedDate(arBatch.getDepositDate());
        nsSubledger.setBalance(arBatch.getAppliedAmount());
        nsSubledger.setBalanceInProcess(arBatch.getAppliedAmount());
        nsSubledger.setTransactionAmt(arBatch.getAppliedAmount());
        nsSubledger.setGlAccountNumber(arBatch.getGlAccountNo());
        nsSubledger.setSubledgerSourceCode(AccountingConstants.SUBLEDGER_CODE_NETSETT);
        nsSubledger.setInvoiceNumber(invoiceNumber);
        nsSubledger.setStatus(AccountingConstants.STATUS_OPEN);
        nsSubledger.setCustomerReferenceNo(referenceNumber);
        nsSubledger.setCustName(customerName);
        nsSubledger.setCustNo(customerNumber);
        nsSubledger.setCurrencyCode(AccountingConstants.CURRENCY_USD);
        nsSubledger.setCreatedBy(loginUser.getUserId());
        nsSubledger.setCreatedOn(new Date());
        new AccountingLedgerDAO().save(nsSubledger);
        copyNsBatchDocuments(arBatch.getBatchId().toString(), customerNumber, invoiceNumber);
    }

    public static void exportNSInvoice(String batchId, HttpServletResponse response) throws Exception {
        String fileName = new ExportNSInvoiceToExcel().exportToExcel(batchId);
        if (CommonUtils.isNotEmpty(fileName)) {
            response.addHeader("Content-Disposition", "attachment; filename=" + FilenameUtils.getName(fileName));
            response.setContentType("application/vnd.ms-excel" + ";charset=utf-8");
            ServletUtils.returnFile(fileName, response.getOutputStream());
        }
    }

    public static void printNSInvoice(String batchId, String contextPath, HttpServletRequest request) throws Exception {
        request.setAttribute("fileName", new NSInvoiceReportCreator().createReport(contextPath, batchId));
    }

    public static String saveApplyPayments(ArBatchForm arBatchForm, ArBatch arBatch, User loginUser) throws Exception {
        PaymentsDAO paymentsDAO = new PaymentsDAO();
        PaymentChecksDAO paymentChecksDAO = new PaymentChecksDAO();
        PaymentBean appliedOnAccount = arBatchForm.getAppliedOnAccount();
        List<PaymentBean> appliedPrepayments = arBatchForm.getAppliedPrepayments();
        List<PaymentBean> appliedGLAccounts = arBatchForm.getAppliedGLAccounts();
        List<PaymentBean> appliedTransactions = arBatchForm.getAppliedTransactions();
        List<PaymentBean> importedTransactions = arBatchForm.getImportedTransactions();
        //Create PaymentChecks and set values
        PaymentChecks paymentChecks = new PaymentChecks();
        paymentChecks.setBatchId(arBatch.getBatchId());
        paymentChecks.setBatchDate(arBatch.getDate());
        paymentChecks.setCustId(arBatchForm.getCustomerNumber());
        paymentChecks.setCheckNo(arBatchForm.getCheckNumber());
        paymentChecks.setReceivedAmt(Double.parseDouble(arBatchForm.getCheckTotal()));
        paymentChecks.setAppliedAmount(Double.parseDouble(arBatchForm.getCheckApplied()));
        paymentChecksDAO.save(paymentChecks);

        double onAccountAmount = 0d;
        double prepaymentAmount = 0d;
        double glAccountAmount = 0d;
        double invoiceAmount = 0d;
        if (arBatchForm.isOnAccountApplied()) {
            onAccountAmount += Double.parseDouble(appliedOnAccount.getPaidAmount());
            Payments payments = new Payments(arBatch, appliedOnAccount, paymentChecks, loginUser, false);
            paymentsDAO.save(payments);
        }
        if (arBatchForm.isPrepaymentApplied()) {
            for (PaymentBean appliedPrepayment : appliedPrepayments) {
                if (CommonUtils.isNotEmpty(appliedPrepayment.getPaidAmount())) {
                    prepaymentAmount += Double.parseDouble(appliedPrepayment.getPaidAmount());
                }
                appliedPrepayment.setDocReceipt(appliedPrepayment.getDocReceipt().trim());
                Payments payments = new Payments(arBatch, appliedPrepayment, paymentChecks, loginUser, false);
                paymentsDAO.save(payments);
            }
        }
        if (arBatchForm.isChargeCodeApplied()) {
            for (PaymentBean appliedGlAccount : appliedGLAccounts) {
                if (CommonUtils.isNotEmpty(appliedGlAccount.getPaidAmount())) {
                    glAccountAmount += Double.parseDouble(appliedGlAccount.getPaidAmount());
                }
                Payments payments = new Payments(arBatch, appliedGlAccount, paymentChecks, loginUser, false);
                paymentsDAO.save(payments);
            }
        }
        for (PaymentBean appliedTransaction : appliedTransactions) {
            if (CommonUtils.isNotEmpty(appliedTransaction.getPaidAmount())) {
                invoiceAmount += Double.parseDouble(appliedTransaction.getPaidAmount());
            }
            Payments payments = new Payments(arBatch, appliedTransaction, paymentChecks, loginUser, true);
            paymentsDAO.save(payments);
        }
        for (PaymentBean importedTransaction : importedTransactions) {
            if (CommonUtils.isNotEmpty(importedTransaction.getPaidAmount())) {
                invoiceAmount += Double.parseDouble(importedTransaction.getPaidAmount());
            }
            Payments payments = new Payments(arBatch, importedTransaction, paymentChecks, loginUser, true);
            paymentsDAO.save(payments);
        }
        paymentChecks.setOnAcctOut(onAccountAmount);
        paymentChecks.setPrePmtOut(prepaymentAmount);
        paymentChecks.setChargeCodeOut(glAccountAmount);
        paymentChecks.setInvoiceIn(invoiceAmount);
        paymentChecks.setAppliedAmount(onAccountAmount + prepaymentAmount + glAccountAmount + invoiceAmount);
        paymentChecksDAO.update(paymentChecks);
        return paymentChecks.getId().toString();
    }

    public static String updateApplyPayments(ArBatchForm arBatchForm, ArBatch arBatch, User loginUser) throws Exception {
        ArBatchDAO arBatchDAO = new ArBatchDAO();
        PaymentsDAO paymentsDAO = new PaymentsDAO();
        PaymentChecksDAO paymentChecksDAO = new PaymentChecksDAO();
        AccountingTransactionDAO accountingTransactionDAO = new AccountingTransactionDAO();
        AccountingLedgerDAO accountingLedgerDAO = new AccountingLedgerDAO();
        PaymentBean appliedOnAccount = arBatchForm.getAppliedOnAccount();
        List<PaymentBean> appliedPrepayments = arBatchForm.getAppliedPrepayments();
        List<PaymentBean> appliedGLAccounts = arBatchForm.getAppliedGLAccounts();
        List<PaymentBean> appliedTransactions = arBatchForm.getAppliedTransactions();
        List<PaymentBean> importedTransactions = arBatchForm.getImportedTransactions();
        //Get PaymentChecks from database using checkId and set values
        PaymentChecks paymentChecks = paymentChecksDAO.findById(Integer.parseInt(arBatchForm.getPaymentCheckId()));
        paymentChecks.setCheckNo(arBatchForm.getCheckNumber());
        List<String> savedArItems = arBatchDAO.getArItemsSavedInCheck(arBatchForm.getBatchId(), arBatchForm.getPaymentCheckId());
        List<String> savedApItems = arBatchDAO.getApItemsSavedInCheck(arBatchForm.getBatchId(), arBatchForm.getPaymentCheckId());
        List<String> savedAcItems = arBatchDAO.getAcItemsSavedInCheck(arBatchForm.getBatchId(), arBatchForm.getPaymentCheckId());
        List<String> selectedArItems = new ArrayList<String>();
        List<String> selectedApItems = new ArrayList<String>();
        List<String> selectedAcItems = new ArrayList<String>();
        //Remove all the payments before recreating the new payments
        arBatchDAO.removePayments(arBatchForm.getBatchId(), arBatchForm.getPaymentCheckId(), loginUser.getUserId());
        double onAccountAmount = 0d;
        double prepaymentAmount = 0d;
        double glAccountAmount = 0d;
        double invoiceAmount = 0d;
        if (arBatchForm.isOnAccountApplied()) {
            if (CommonUtils.isNotEmpty(appliedOnAccount.getPaidAmount())) {
                onAccountAmount += Double.parseDouble(appliedOnAccount.getPaidAmount());
            }
            Payments payments = new Payments(arBatch, appliedOnAccount, paymentChecks, loginUser, false);
            paymentsDAO.save(payments);
        }
        if (arBatchForm.isPrepaymentApplied()) {
            for (PaymentBean appliedPrepayment : appliedPrepayments) {
                if (CommonUtils.isNotEmpty(appliedPrepayment.getPaidAmount())) {
                    prepaymentAmount += Double.parseDouble(appliedPrepayment.getPaidAmount());
                }
                Payments payments = new Payments(arBatch, appliedPrepayment, paymentChecks, loginUser, false);
                paymentsDAO.save(payments);
            }
        }
        if (arBatchForm.isChargeCodeApplied()) {
            for (PaymentBean appliedGlAccount : appliedGLAccounts) {
                if (CommonUtils.isNotEmpty(appliedGlAccount.getPaidAmount())) {
                    glAccountAmount += Double.parseDouble(appliedGlAccount.getPaidAmount());
                }
                Payments payments = new Payments(arBatch, appliedGlAccount, paymentChecks, loginUser, false);
                paymentsDAO.save(payments);
            }
        }
        for (PaymentBean appliedTransaction : appliedTransactions) {
            if (CommonUtils.isNotEmpty(appliedTransaction.getPaidAmount())) {
                invoiceAmount += Double.parseDouble(appliedTransaction.getPaidAmount());
            }
            boolean isNew = true;
            if (appliedTransaction.getTransactionId().endsWith(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE)) {
                String id = appliedTransaction.getTransactionId().replace(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE, "");
                if (null != savedArItems && savedArItems.contains(id)) {
                    isNew = false;
                    selectedArItems.add(id);
                }
            } else if (appliedTransaction.getTransactionId().endsWith(TRANSACTION_TYPE_ACCOUNT_PAYABLE)) {
                String id = appliedTransaction.getTransactionId().replace(TRANSACTION_TYPE_ACCOUNT_PAYABLE, "");
                if (null != savedApItems && savedApItems.contains(id)) {
                    isNew = false;
                    selectedApItems.add(id);
                }
            } else if (appliedTransaction.getTransactionId().endsWith(TRANSACTION_TYPE_ACCRUALS)) {
                String id = appliedTransaction.getTransactionId().replace(TRANSACTION_TYPE_ACCRUALS, "");
                if (null != savedAcItems && savedAcItems.contains(id)) {
                    isNew = false;
                    selectedAcItems.add(id);
                }
            }
            Payments payments = new Payments(arBatch, appliedTransaction, paymentChecks, loginUser, isNew);
            paymentsDAO.save(payments);
        }
        for (PaymentBean importedTransaction : importedTransactions) {
            if (CommonUtils.isNotEmpty(importedTransaction.getPaidAmount())) {
                invoiceAmount += Double.parseDouble(importedTransaction.getPaidAmount());
            }
            boolean isNew = true;
            if (importedTransaction.getTransactionId().endsWith(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE)) {
                String id = importedTransaction.getTransactionId().replace(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE, "");
                if (null != savedArItems && savedArItems.contains(id)) {
                    isNew = false;
                    selectedArItems.add(id);
                }
            } else if (importedTransaction.getTransactionId().endsWith(TRANSACTION_TYPE_ACCOUNT_PAYABLE)) {
                String id = importedTransaction.getTransactionId().replace(TRANSACTION_TYPE_ACCOUNT_PAYABLE, "");
                if (null != savedApItems && savedApItems.contains(id)) {
                    isNew = false;
                    selectedApItems.add(id);
                }
            } else if (importedTransaction.getTransactionId().endsWith(TRANSACTION_TYPE_ACCRUALS)) {
                String id = importedTransaction.getTransactionId().replace(TRANSACTION_TYPE_ACCRUALS, "");
                if (null != savedAcItems && savedAcItems.contains(id)) {
                    isNew = false;
                    selectedAcItems.add(id);
                }
            }
            Payments payments = new Payments(arBatch, importedTransaction, paymentChecks, loginUser, isNew);
            paymentsDAO.save(payments);
        }
        if (CommonUtils.isNotEmpty(savedArItems)) {
            for (String id : savedArItems) {
                if (!selectedArItems.contains(id)) {
                    Transaction transaction = accountingTransactionDAO.findById(Integer.parseInt(id));
                    String invoiceOrBl = CommonUtils.isNotEmpty(transaction.getBillLaddingNo()) ? transaction.getBillLaddingNo() : transaction.getInvoiceNumber();
                    String key = transaction.getCustNo() + "-" + invoiceOrBl;
                    StringBuilder desc = new StringBuilder("Invoice/BL '").append(invoiceOrBl).append("'");
                    desc.append(" of '").append(transaction.getCustNo()).append("'");
                    desc.append(" removed from this batch - '").append(arBatch.getBatchId()).append("'");
                    desc.append(" by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
                    AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.AR_INVOICE, key, NotesConstants.AR_INVOICE, loginUser);
                }
            }
        }
        if (CommonUtils.isNotEmpty(savedApItems)) {
            for (String id : savedApItems) {
                if (!selectedApItems.contains(id)) {
                    Transaction transaction = accountingTransactionDAO.findById(Integer.parseInt(id));
                    String key = transaction.getCustNo() + "-" + transaction.getInvoiceNumber();
                    StringBuilder desc = new StringBuilder("Invoice '").append(transaction.getInvoiceNumber()).append("'");
                    desc.append(" of '").append(transaction.getCustNo()).append("'");
                    desc.append(" removed from this batch - '").append(arBatch.getBatchId()).append("'");
                    desc.append(" by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
                    AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.AP_INVOICE, key, NotesConstants.AP_INVOICE, loginUser);
                }
            }
        }
        if (CommonUtils.isNotEmpty(savedAcItems)) {
            for (String id : savedAcItems) {
                if (!selectedAcItems.contains(id)) {
                    TransactionLedger transactionLedger = accountingLedgerDAO.findById(Integer.parseInt(id));
                    String key = id;
                    StringBuilder desc = new StringBuilder("Accrual of ");
                    boolean addAnd = false;
                    if (CommonUtils.isNotEmpty(transactionLedger.getBillLaddingNo())) {
                        desc.append("B/L - '").append(transactionLedger.getBillLaddingNo().trim()).append("'");
                        addAnd = true;
                    }
                    if (CommonUtils.isNotEmpty(transactionLedger.getDocReceipt())) {
                        desc.append(addAnd ? " and " : "").append("Doc Receipt - '").append(transactionLedger.getDocReceipt()).append("'");
                        addAnd = true;
                    }
                    if (CommonUtils.isNotEmpty(transactionLedger.getVoyageNo())) {
                        desc.append(addAnd ? " and " : "").append("Voyage - '").append(transactionLedger.getVoyageNo()).append("'");
                    }
                    desc.append(" of '").append(transactionLedger.getCustNo()).append("'");
                    desc.append(" removed from this batch - '").append(arBatch.getBatchId()).append("'");
                    desc.append(" by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
                    AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.ACCRUALS, key, NotesConstants.ACCRUALS, loginUser);
                }
            }
        }
        paymentChecks.setOnAcctOut(onAccountAmount);
        paymentChecks.setPrePmtOut(prepaymentAmount);
        paymentChecks.setChargeCodeOut(glAccountAmount);
        paymentChecks.setInvoiceIn(invoiceAmount);
        paymentChecks.setReceivedAmt(Double.parseDouble(arBatchForm.getCheckTotal()));
        paymentChecks.setAppliedAmount(onAccountAmount + prepaymentAmount + glAccountAmount + invoiceAmount);
        paymentChecksDAO.update(paymentChecks);
        return paymentChecks.getId().toString();
    }

    private static void reverseArHistories(String batchId, User loginUser, Date postedDate) throws Exception {
        ArTransactionHistoryDAO historyDAO = new ArTransactionHistoryDAO();
        List<ArTransactionHistory> histories = historyDAO.getArTransactionHistories(batchId);
        for (ArTransactionHistory oldHistory : histories) {
            ArTransactionHistory newHistory = new ArTransactionHistory();
            PropertyUtils.copyProperties(newHistory, oldHistory);
            newHistory.setId(null);
            newHistory.setTransactionDate(new Date());
            newHistory.setPostedDate(postedDate);
            newHistory.setTransactionAmount(-oldHistory.getTransactionAmount());
            newHistory.setAdjustmentAmount(-oldHistory.getAdjustmentAmount());
            newHistory.setCreatedBy(loginUser.getLoginName());
            newHistory.setCreatedDate(new Date());
            historyDAO.save(newHistory);
        }
    }

    private static void reverseApHistories(String batchId, User loginUser, Date postedDate) throws Exception {
        ApTransactionHistoryDAO historyDAO = new ApTransactionHistoryDAO();
        List<ApTransactionHistory> histories = historyDAO.getApTransactionHistories(batchId);
        for (ApTransactionHistory oldHistory : histories) {
            ApTransactionHistory newHistory = new ApTransactionHistory();
            PropertyUtils.copyProperties(newHistory, oldHistory);
            newHistory.setId(null);
            newHistory.setTransactionDate(new Date());
            newHistory.setPostedDate(postedDate);
            newHistory.setAmount(-oldHistory.getAmount());
            newHistory.setCreatedBy(loginUser.getLoginName());
            newHistory.setCreatedDate(new Date());
            historyDAO.save(newHistory);
        }
    }

    private static void reverseApPayments(String batchType, String batchId, User loginUser, Date postedDate) throws Exception {
        ArBatchDAO arBatchDAO = new ArBatchDAO();
        AccountingTransactionDAO transactionDAO = new AccountingTransactionDAO();
        List<Transaction> apPayments = arBatchDAO.getApPayments(batchId);
        for (Transaction oldPayment : apPayments) {
            Transaction newPayment = new Transaction();
            PropertyUtils.copyProperties(newPayment, oldPayment);
            newPayment.setTransactionId(null);
            newPayment.setTransactionDate(new Date());
            newPayment.setPostedDate(postedDate);
            newPayment.setTransactionAmt(-oldPayment.getTransactionAmt());
            newPayment.setBalance(-oldPayment.getBalance());
            newPayment.setBalanceInProcess(-oldPayment.getBalanceInProcess());
            newPayment.setCreatedBy(loginUser.getUserId());
            newPayment.setCreatedOn(new Date());
            transactionDAO.save(newPayment);
            StringBuilder desc = new StringBuilder();
            desc.append(batchType).append(" Payment for Invoice - '").append(newPayment.getInvoiceNumber()).append("'");
            desc.append(" of Vendor '").append(newPayment.getCustName()).append("(").append(newPayment.getCustNo()).append(")'");
            desc.append(" reversed by ").append(loginUser.getLoginName());
            desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
            String key = newPayment.getCustNo() + "-" + newPayment.getInvoiceNumber();
            AuditNotesUtils.insertAuditNotes(desc.toString(), AP_INVOICE, key, AP_INVOICE, loginUser);
        }
    }

    private static void reverseSubledgers(String batchId, User loginUser, Date postedDate) throws Exception {
        ArBatchDAO arBatchDAO = new ArBatchDAO();
        AccountingLedgerDAO ledgerDAO = new AccountingLedgerDAO();
        List<TransactionLedger> subledgers = arBatchDAO.getSubledgers(batchId);
        for (TransactionLedger oldSubledger : subledgers) {
            TransactionLedger newSubledger = new TransactionLedger();
            PropertyUtils.copyProperties(newSubledger, oldSubledger);
            newSubledger.setTransactionId(null);
            if (CommonUtils.isEqual(oldSubledger.getStatus(), AccountingConstants.STATUS_CHARGECODEPOSTED)) {
                newSubledger.setStatus(AccountingConstants.STATUS_CHARGECODE);
            } else {
                newSubledger.setStatus(STATUS_OPEN);
            }
            newSubledger.setTransactionDate(new Date());
            newSubledger.setPostedDate(postedDate);
            newSubledger.setTransactionAmt(-oldSubledger.getTransactionAmt());
            newSubledger.setBalance(-oldSubledger.getBalance());
            newSubledger.setBalanceInProcess(-oldSubledger.getBalanceInProcess());
            newSubledger.setCreatedBy(loginUser.getUserId());
            newSubledger.setCreatedOn(new Date());
            ledgerDAO.save(newSubledger);
        }
    }

    private static void reverseArInvoices(String batchId, User loginUser) throws Exception {
        ArBatchDAO arBatchDAO = new ArBatchDAO();
        AccountingTransactionDAO transactionDAO = new AccountingTransactionDAO();
        List<Object> result = arBatchDAO.getArInvoices(batchId);
        for (Object row : result) {
            Object[] col = (Object[]) row;
            Transaction arInvoice = (Transaction) col[0];
            Payments payment = (Payments) col[1];
            arInvoice.setStatus(STATUS_OPEN);
            arInvoice.setBalance(arInvoice.getBalance() + payment.getPaymentAmt() + payment.getAdjustmentAmt());
            arInvoice.setBalanceInProcess(arInvoice.getBalanceInProcess() + payment.getPaymentAmt() + payment.getAdjustmentAmt());
            arInvoice.setUpdatedBy(loginUser.getUserId());
            arInvoice.setUpdatedOn(new Date());
            transactionDAO.update(arInvoice);
        }
    }

    private static void reverseApInvoices(String batchId, User loginUser) throws Exception {
        ArBatchDAO arBatchDAO = new ArBatchDAO();
        AccountingTransactionDAO transactionDAO = new AccountingTransactionDAO();
        List<Object> result = arBatchDAO.getApInvoices(batchId);
        List<Integer> ids = new ArrayList<Integer>();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            Transaction apInvoice = (Transaction) col[0];
            Payments payment = (Payments) col[1];
            apInvoice.setStatus(STATUS_OPEN);
            apInvoice.setBalance(apInvoice.getBalance() - payment.getPaymentAmt());
            apInvoice.setBalanceInProcess(apInvoice.getBalanceInProcess() - payment.getPaymentAmt());
            apInvoice.setCheckDate(null);
            apInvoice.setUpdatedBy(loginUser.getUserId());
            apInvoice.setUpdatedOn(new Date());
            transactionDAO.update(apInvoice);
            ids.add(apInvoice.getTransactionId());
        }
        arBatchDAO.removeApInvoices(batchId, ids.toString().replace("[", "").replace("]", ""));
    }

    private static void reverseAccruals(String batchId, User loginUser) throws Exception {
        ArBatchDAO arBatchDAO = new ArBatchDAO();
        AccountingLedgerDAO ledgerDAO = new AccountingLedgerDAO();
        FclBlCostCodesDAO fclBlCostCodesDAO = new FclBlCostCodesDAO();
        AccrualsDAO accrualsDAO = new AccrualsDAO();
        List<Object> result = arBatchDAO.getAccruals(batchId);
        for (Object row : result) {
            Object[] col = (Object[]) row;
            TransactionLedger accrual = (TransactionLedger) col[0];
            Payments payment = (Payments) col[1];
            accrual.setStatus(STATUS_OPEN);
            accrual.setBalance(accrual.getBalance() - payment.getPaymentAmt());
            accrual.setBalanceInProcess(accrual.getBalanceInProcess() - payment.getPaymentAmt());
            accrual.setUpdatedBy(loginUser.getUserId());
            accrual.setUpdatedOn(new Date());
            ledgerDAO.update(accrual);
            if (CommonUtils.isNotEmpty(accrual.getCostId())) {
                if (CommonUtils.in(accrual.getShipmentType(), "LCLE", "LCLI")) {
                    Map<Serializable, Serializable> columns = new HashMap<Serializable, Serializable>();
                    if (CommonUtils.isNotEmpty(accrual.getDocReceipt())) {
                        columns.put("b.invoice_number", accrual.getInvoiceNumber());
                        columns.put("t.trans_type", TRANSACTION_TYPE_ACCRUALS);
                        accrualsDAO.updateLclCost(accrual.getCostId(), columns);
                    } else {
                        columns.put("lssac.ap_reference_no", accrual.getInvoiceNumber());
                        columns.put("lssac.ap_trans_type", TRANSACTION_TYPE_ACCRUALS);
                        accrualsDAO.updateLclUnitCost(accrual.getCostId(), columns);
                    }
                } else {
                    FclBlCostCodes fclBlCostCodes = fclBlCostCodesDAO.findById(accrual.getCostId());
                    if (null != fclBlCostCodes) {
                        fclBlCostCodes.setInvoiceNumber(accrual.getInvoiceNumber());
                        fclBlCostCodes.setTransactionType(TRANSACTION_TYPE_ACCRUALS);
                        fclBlCostCodes.setDatePaid(null);
                    }
                }
            }
            String key = accrual.getTransactionId().toString();
            StringBuilder desc = new StringBuilder("Accrual of ");
            boolean addAnd = false;
            if (CommonUtils.isNotEmpty(accrual.getBillLaddingNo())) {
                desc.append("B/L - '").append(accrual.getBillLaddingNo().trim()).append("'");
                addAnd = true;
            }
            if (CommonUtils.isNotEmpty(accrual.getDocReceipt())) {
                desc.append(addAnd ? " and " : "").append("Doc Receipt - '").append(accrual.getDocReceipt()).append("'");
                addAnd = true;
            }
            if (CommonUtils.isNotEmpty(accrual.getVoyageNo())) {
                desc.append(addAnd ? " and " : "").append("Voyage - '").append(accrual.getVoyageNo()).append("'");
            }
            desc.append(" of '").append(accrual.getCustNo()).append("'");
            desc.append(" for amount - '").append(NumberUtils.formatNumber(accrual.getTransactionAmt())).append("'");
            desc.append(" reversed in this batch - '").append(batchId).append("'");
            desc.append(" by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
            AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.ACCRUALS, key, NotesConstants.ACCRUALS, loginUser);
        }
    }

    public static void reversePost(String batchId, User loginUser) throws Exception {
        ArBatchDAO arBatchDAO = new ArBatchDAO();
        AccountingTransactionDAO transactionDAO = new AccountingTransactionDAO();
        ArBatch arBatch = arBatchDAO.findById(Integer.parseInt(batchId));
        Date postedDate = new AccrualsDAO().getPostedDate(arBatch.getDepositDate());
        if (CommonUtils.isEqual(arBatch.getBatchType(), AccountingConstants.AR_NET_SETT_BATCH)) {
            Transaction netsettTransaction = transactionDAO.getNetSettTransaction(batchId);
            if (CommonUtils.isNotEqual(arBatch.getAppliedAmount(), netsettTransaction.getBalance())
                    && CommonUtils.isNotEqual(arBatch.getAppliedAmount(), netsettTransaction.getBalanceInProcess())) {
                throw new AccountingException("Netsett invoice has been settled already");
            } else {
                netsettTransaction.setTransactionAmt(0d);
                netsettTransaction.setBalance(0d);
                netsettTransaction.setBalanceInProcess(0d);
            }
        } else {
            boolean isReconciled = new ReconcileDAO().isArBatchReconciled(batchId);
            if (isReconciled) {
                throw new AccountingException("Cash batch has been reconciled already");
            }
        }
        String batchType = CommonUtils.isEqual(arBatch.getBatchType(), AccountingConstants.AR_NET_SETT_BATCH) ? "NETT SETT" : "DEPOSIT";
        reverseArHistories(batchId, loginUser, postedDate);
        reverseApHistories(batchId, loginUser, postedDate);
        reverseApPayments(batchType, batchId, loginUser, postedDate);
        reverseSubledgers(batchId, loginUser, postedDate);
        reverseApInvoices(batchId, loginUser);
        reverseArInvoices(batchId, loginUser);
        reverseAccruals(batchId, loginUser);
        arBatch.setStatus(STATUS_REVERSED);
        arBatchDAO.update(arBatch);
        StringBuilder desc = new StringBuilder(batchType).append(" Batch - ").append(arBatch.getBatchId());
        desc.append(" reversed by ").append(loginUser.getLoginName());
        desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
        AuditNotesUtils.insertAuditNotes(desc.toString(), AR_BATCH, arBatch.getBatchId().toString(), "ArBatch", loginUser);
    }
}
