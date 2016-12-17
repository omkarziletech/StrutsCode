package com.logiware.dwr;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.domain.ProcessInfo;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.ProcessInfoDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.AccountingConstants;
import com.gp.cvst.logisoft.domain.ArBatch;
import com.gp.cvst.logisoft.domain.BankDetails;
import com.gp.cvst.logisoft.domain.DocumentStoreLog;
import com.gp.cvst.logisoft.domain.PaymentChecks;
import com.gp.cvst.logisoft.domain.Payments;
import com.gp.cvst.logisoft.domain.Transaction;
import com.gp.cvst.logisoft.domain.TransactionLedger;
import com.gp.cvst.logisoft.hibernate.dao.BankDetailsDAO;
import com.gp.cvst.logisoft.hibernate.dao.DocumentStoreLogDAO;
import com.gp.cvst.logisoft.hibernate.dao.FiscalPeriodDAO;
import com.gp.cvst.logisoft.hibernate.dao.PaymentChecksDAO;
import com.gp.cvst.logisoft.hibernate.dao.PaymentsDAO;
import com.gp.cvst.logisoft.hibernate.dao.TransactionDAO;
import com.logiware.accounting.dao.ApInvoiceDAO;
import com.logiware.accounting.domain.ApInvoice;
import com.logiware.bean.AccountingBean;
import com.logiware.bean.ArBatchBean;
import com.logiware.bean.ArTransactionBean;
import com.logiware.bean.ComparePaymentsBean;
import com.logiware.bean.PaymentBean;
import com.logiware.bean.PaymentCheckBean;
import com.logiware.common.form.FileUploadForm;
import com.logiware.hibernate.dao.AccountingLedgerDAO;
import com.logiware.hibernate.dao.AccountingTransactionDAO;
import com.logiware.hibernate.dao.ArBatchDAO;
import com.logiware.hibernate.dao.ArTransactionHistoryDAO;
import com.logiware.utils.AuditNotesUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.directwebremoting.WebContextFactory;
import org.apache.struts.upload.FormFile;

/**
 * @description ArDwr
 * @author LakshmiNarayanan
 */
public class ArDwr implements Serializable, ConstantsInterface {

    private static final long serialVersionUID = 2104673553314712631L;

    public void showArTransactionHistory(String transactionId, HttpServletRequest request) throws Exception {
        int id = Integer.parseInt(transactionId);
        Transaction transaction = new TransactionDAO().findById(id);
        List<ArTransactionBean> arTransactionHistoryList = new ArTransactionHistoryDAO().getArTransactionHistory(transaction.getCustNo(),
                transaction.getBillLaddingNo(), transaction.getInvoiceNumber());
        List<ArTransactionBean> unpostedPayments = new PaymentsDAO().getUnPostedPayments(id);
        if (CommonUtils.isNotEmpty(transaction.getApInvoiceId())
                && CommonUtils.in(transaction.getApInvoiceStatus(), STATUS_IN_PROGRESS, STATUS_DISPUTE)) {
            ArTransactionBean arTransactionBean = new ArTransactionBean();
            ApInvoice apInvoice = new ApInvoiceDAO().findById(transaction.getApInvoiceId());
            arTransactionBean.setBatchId(null);
            arTransactionBean.setCheckNo(apInvoice.getAccountNumber() + "-" + apInvoice.getInvoiceNumber());
            arTransactionBean.setTransactionDate(apInvoice.getDate());
            if (CommonUtils.isEqualIgnoreCase(transaction.getApInvoiceStatus(), STATUS_IN_PROGRESS)) {
                arTransactionBean.setTransactionAmount(transaction.getApInvoiceAmount());
            } else {
                arTransactionBean.setTransactionAmount(-transaction.getBalance());
            }
            arTransactionBean.setTransactionType("AP INV");
            arTransactionBean.setGlAccount(null);
            if (CommonUtils.isNotEmpty(transaction.getUpdatedBy())) {
                String userName = new UserDAO().getLoginName(transaction.getUpdatedBy());
                arTransactionBean.setUserName(userName);
            }
            unpostedPayments.add(arTransactionBean);
        }
        request.setAttribute("arTransactionHistoryList", arTransactionHistoryList);
        request.setAttribute("unpostedPayments", unpostedPayments);
    }

    public void showInvoiceOrBlDetails(String customerNumber, String invoiceOrBl, HttpServletRequest request) throws Exception {
        List<AccountingBean> charges = new AccountingLedgerDAO().getInvoiceOrBlDetails(customerNumber, invoiceOrBl);
        request.setAttribute("charges", charges);
    }

    public String lockArBatch(String batchId, HttpServletRequest request) throws Exception {
        synchronized (ArDwr.class) {
            User loginUser = (User) request.getSession().getAttribute(LOGIN_USER);
            ProcessInfoDAO processInfoDAO = new ProcessInfoDAO();
            ProcessInfo processInfo = processInfoDAO.getProcessInfo(batchId, "AR_BATCH", "AR_BATCH");
            String result = "success";
            if (null != processInfo) {
                if (!processInfo.getUserid().toString().equals(loginUser.getUserId().toString())) {
                    String loginName = new UserDAO().getLoginName(processInfo.getUserid());
                    result = "This batch is using by " + loginName;
                }
            } else {
                processInfo = new ProcessInfo();
                processInfo.setRecordid(batchId);
                processInfo.setProgramid(Integer.parseInt(batchId));
                processInfo.setUserid(loginUser.getUserId());
                processInfo.setModuleId("AR_BATCH");
                processInfo.setAction("AR_BATCH");
                processInfo.setProcessinfodate(new Date());
                processInfoDAO.save(processInfo);
            }
            return result;
        }
    }

    public String unLockArBatch(String batchId, HttpServletRequest request) throws Exception {
        synchronized (ArDwr.class) {
            User loginUser = (User) request.getSession().getAttribute(LOGIN_USER);
            ProcessInfoDAO processInfoDAO = new ProcessInfoDAO();
            ProcessInfo processInfo = processInfoDAO.getProcessInfo(batchId, "AR_BATCH", "AR_BATCH");
            if (null != processInfo && processInfo.getUserid().toString().equals(loginUser.getUserId().toString())) {
                processInfoDAO.delete(processInfo);
            }
            return "success";
        }
    }

    public String getGLAccountNumber(String bankAccount) throws Exception {
        return new BankDetailsDAO().getGlAccountNo(bankAccount);
    }

    public boolean isPeriodOpenForThisDate(String javaDate) throws Exception {
        String sqlDate = DateUtils.formatDate(DateUtils.parseDate(javaDate, "MM/dd/yyyy"), "yyyy-MM-dd");
        return new FiscalPeriodDAO().isPeriodOpenForThisDate(sqlDate);
    }

    public String isNotReconciledDate(String javaDate, String bankAccount, String glAccount) throws Exception {
        Date selectedDate = DateUtils.parseDate(javaDate, "MM/dd/yyyy");
        BankDetails bankDetails = new BankDetailsDAO().getBank(bankAccount, glAccount);
        if (null != bankDetails && null != bankDetails.getLastReconciledDate()
                && DateUtils.compareTo(selectedDate, bankDetails.getLastReconciledDate()) <= 0) {
            String lastReconciledDate = DateUtils.formatDate(bankDetails.getLastReconciledDate(), "MM/dd/yyyy");
            return "Bank account has been reconciled upto " + lastReconciledDate + ", please select different date";
        } else {
            return "available";
        }
    }

    public void showChecks(String batchId, String canEdit, HttpServletRequest request) throws Exception {
        List<PaymentCheckBean> paymentChecks = new ArBatchDAO().getPaymentChecks(Integer.parseInt(batchId));
        request.setAttribute("batchId", batchId);
        request.setAttribute("canEdit", canEdit.equals("true"));
        request.setAttribute("paymentChecks", paymentChecks);
    }

    public void showPostBatch(String batchId, HttpServletRequest request) throws Exception {
        ArBatchDAO arBatchDAO = new ArBatchDAO();
        int batchid = Integer.parseInt(batchId);
        ArBatchBean arBatch = arBatchDAO.getArBatch(batchid);
        Integer noOfPayments = arBatchDAO.getNoOfPayments(batchid);
        if (noOfPayments > 0) {
            double paymentAmount = arBatchDAO.getPaymentAmountFromPayments(batchid);
            if (arBatch.getBatchType().equals(AccountingConstants.AR_NET_SETT_BATCH)) {
                double balance = Double.parseDouble(arBatch.getAppliedAmount().replace(",", "")) - paymentAmount;
                if (CommonUtils.isEmpty(balance)) {
                    request.setAttribute("isNetSett", true);
                    request.setAttribute("canPost", true);
                    request.setAttribute("customers", new PaymentChecksDAO().getCustomersFromChecks(batchid));
                } else {
                    request.setAttribute("canPost", false);
                    request.setAttribute("error", "Cannot post. Something wrong in the batch as batch applied amount and payment amount not matched.");
                }
            } else {
                double batchBalance = Double.parseDouble(arBatch.getTotalAmount().replace(",", "")) - paymentAmount;
                double appliedBalance = Double.parseDouble(arBatch.getAppliedAmount().replace(",", "")) - paymentAmount;
                double checkAmount = arBatchDAO.getCheckAmountFromChecks(batchid);
                double checkBalance = checkAmount - paymentAmount;
                String unBalancedChecks = arBatchDAO.getUnBalancedChecks(batchid);
                if (CommonUtils.isEmpty(checkBalance) && CommonUtils.isEmpty(batchBalance)
                        && CommonUtils.isEmpty(appliedBalance) && CommonUtils.isEmpty(unBalancedChecks)) {
                    request.setAttribute("canPost", true);
                } else {
                    request.setAttribute("canPost", false);
                    if (CommonUtils.isNotEmpty(unBalancedChecks)) {
                        request.setAttribute("error", "Cannot post. Checks-" + unBalancedChecks + " not allocated in full");
                    } else {
                        request.setAttribute("error", "Cannot post. Something wrong in the batch as batch amount and applied amount not matched.");
                    }
                }
            }
        } else {
            request.setAttribute("error", "Cannot post - no payments included in this batch");
            request.setAttribute("canPost", false);
        }
        request.setAttribute("arBatch", arBatch);
    }

    public boolean voidCheck(String batchId, String checkId, HttpServletRequest request) throws Exception {
        User loginUser = (User) request.getSession().getAttribute(LOGIN_USER);
        new ArBatchDAO().voidCheck(batchId, checkId, loginUser.getUserId());
        return true;
    }

    public String lockTransaction(String batchId, String transactionId, String transactionType, HttpServletRequest request) throws Exception {
        synchronized (ArDwr.class) {
            int id = Integer.parseInt(batchId);
            User loginUser = (User) request.getSession().getAttribute(LOGIN_USER);
            ProcessInfoDAO processInfoDAO = new ProcessInfoDAO();
            ArBatchDAO arBatchDAO = new ArBatchDAO();
            String recordId = transactionId.replace(TRANSACTION_TYPE_ACCOUNT_PAYABLE, "").replace(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE, "").replace(TRANSACTION_TYPE_ACCRUALS, "");
            String batches = arBatchDAO.getBatchWhereInvoiceLocked(id, recordId, transactionType);
            String result = "available";
            if (CommonUtils.isNotEmpty(batches)) {
                result = "This invoice/bl is included in the batch - " + batches;
            } else {
                ProcessInfo processInfo = processInfoDAO.getProcessInfo(transactionId, "AR_BATCH", "INVOICE_BL");
                if (null != processInfo) {
                    if (!processInfo.getProgramid().toString().equals(batchId)) {
                        result = "This invoice/bl is included in the batch - " + processInfo.getProgramid();
                    }
                } else {
                    processInfo = new ProcessInfo();
                    processInfo.setRecordid(transactionId);
                    processInfo.setProgramid(id);
                    processInfo.setUserid(loginUser.getUserId());
                    processInfo.setModuleId("AR_BATCH");
                    processInfo.setAction("INVOICE_BL");
                    processInfo.setProcessinfodate(new Date());
                    processInfoDAO.save(processInfo);
                }
            }
            return result;
        }
    }

    public void unlockTransaction(String batchId, String transactionId) throws Exception {
        synchronized (ArDwr.class) {
            ProcessInfoDAO processInfoDAO = new ProcessInfoDAO();
            ProcessInfo processInfo = processInfoDAO.getProcessInfo(transactionId, "AR_BATCH", "INVOICE_BL");
            if (null != processInfo && processInfo.getProgramid().toString().equals(batchId)) {
                processInfoDAO.delete(processInfo);
            }
        }
    }

    public void addAccrual(String tranasctionId, HttpServletRequest request) throws Exception {
        request.setAttribute("transaction", new ArBatchDAO().getNewlyAddedAcTransaction(tranasctionId));
    }

    public void comparePayments(ComparePaymentsBean after, HttpServletRequest request) throws Exception {
        if (CommonUtils.isNotEmpty(after.getCheckId())) {
            PaymentsDAO paymentsDAO = new PaymentsDAO();
            ComparePaymentsBean before = new ComparePaymentsBean();
            Integer checkId = Integer.parseInt(after.getCheckId());
            PaymentChecks paymentChecks = new PaymentChecksDAO().findById(checkId);
            Integer batchId = after.getBatchId();
            ArBatch arBatch = new ArBatchDAO().findById(batchId);
            String checkNumber = paymentChecks.getCheckNo();
            before.setCheckNumber(checkNumber);
            before.setCustomerNumber(paymentChecks.getCustId());
            before.setCheckTotalAmount(NumberUtils.formatNumber(paymentChecks.getReceivedAmt(), "###,###,##0.00"));
            before.setAppliedAmount(NumberUtils.formatNumber(paymentChecks.getAppliedAmount(), "###,###,##0.00"));
            if (arBatch.getBatchType().equals(AccountingConstants.AR_NET_SETT_BATCH)) {
                after.setCheckBalance(null);
            } else {
                double balance = paymentChecks.getReceivedAmt() - paymentChecks.getAppliedAmount();
                before.setCheckBalance(NumberUtils.formatNumber(balance, "###,###,##0.00"));
            }
            List<Payments> onAccount = paymentsDAO.getPaymentsForCheck(batchId, checkId, "Check", AccountingConstants.ON_ACCOUNT);
            if (null != onAccount && !onAccount.isEmpty()) {
                Payments payments = onAccount.get(0);
                before.setOnAccount(NumberUtils.formatNumber(payments.getPaymentAmt(), "###,###,##0.00"));
            }
            List<Payments> prepaymentList = paymentsDAO.getPaymentsForCheck(batchId, checkId, "Check", AccountingConstants.PRE_PAYMENT);
            if (null != prepaymentList && !prepaymentList.isEmpty()) {
                List<PaymentBean> prepayments = new ArrayList<PaymentBean>();
                for (Payments payments : prepaymentList) {
                    PaymentBean prepayment = new PaymentBean();
                    prepayment.setDocReceipt(payments.getBillLaddingNo());
                    prepayment.setPaidAmount(NumberUtils.formatNumber(payments.getPaymentAmt(), "###,###,##0.00"));
                    prepayment.setNotes(payments.getNotes());
                    prepayments.add(prepayment);
                }
                before.setPrepayments(prepayments);
            }
            List<Payments> chargeCodeList = paymentsDAO.getPaymentsForCheck(batchId, checkId, "Check", AccountingConstants.CHARGE_CODE);
            if (null != chargeCodeList && !chargeCodeList.isEmpty()) {
                List<PaymentBean> chargeCodes = new ArrayList<PaymentBean>();
                for (Payments payments : chargeCodeList) {
                    PaymentBean chargeCode = new PaymentBean();
                    chargeCode.setGlAccount(payments.getChargeCode());
                    chargeCode.setPaidAmount(NumberUtils.formatNumber(payments.getPaymentAmt(), "###,###,##0.00"));
                    chargeCode.setNotes(payments.getNotes());
                    chargeCodes.add(chargeCode);
                }
                before.setChargeCodes(chargeCodes);
            }
            List<Payments> transactions = paymentsDAO.getPaymentsForCheck(batchId, checkId, "P", null);
            AccountingTransactionDAO accountingTransactionDAO = new AccountingTransactionDAO();
            AccountingLedgerDAO accountingLedgerDAO = new AccountingLedgerDAO();
            if (null != transactions && !transactions.isEmpty()) {
                List<PaymentBean> paymentBeans = new ArrayList<PaymentBean>();
                for (Payments payments : transactions) {
                    PaymentBean paymentBean = new PaymentBean();
                    if (TRANSACTION_TYPE_ACCRUALS.equalsIgnoreCase(payments.getTransactionType())) {
                        TransactionLedger accrual = accountingLedgerDAO.findById(payments.getTransactionId());
                        paymentBean.setTransactionType(TRANSACTION_TYPE_ACCRUALS);
                        paymentBean.setCustomerNumber(accrual.getCustNo());
                        if (CommonUtils.isNotEmpty(accrual.getInvoiceNumber())) {
                            paymentBean.setInvoiceOrBl(accrual.getInvoiceNumber());
                        } else {
                            paymentBean.setInvoiceOrBl(accrual.getBillLaddingNo());
                        }
                        paymentBean.setCustomerNumber(accrual.getCustNo());
                        paymentBean.setTransactionAmount(NumberUtils.formatNumber(accrual.getTransactionAmt(), "###,###,##0.00"));
                        paymentBean.setBalanceInProcess(NumberUtils.formatNumber(accrual.getBalanceInProcess(), "###,###,##0.00"));
                    } else {
                        Transaction arOrAp = accountingTransactionDAO.findById(payments.getTransactionId());
                        paymentBean.setTransactionType(arOrAp.getTransactionType());
                        paymentBean.setCustomerNumber(arOrAp.getCustNo());
                        if (CommonUtils.isNotEmpty(arOrAp.getInvoiceNumber())) {
                            paymentBean.setInvoiceOrBl(arOrAp.getInvoiceNumber());
                        } else {
                            paymentBean.setInvoiceOrBl(arOrAp.getBillLaddingNo());
                        }
                        paymentBean.setCustomerNumber(arOrAp.getCustNo());
                        paymentBean.setTransactionAmount(NumberUtils.formatNumber(arOrAp.getTransactionAmt(), "###,###,##0.00"));
                        paymentBean.setBalanceInProcess(NumberUtils.formatNumber(arOrAp.getBalanceInProcess(), "###,###,##0.00"));
                    }
                    paymentBean.setPaidAmount(NumberUtils.formatNumber(payments.getPaymentAmt(), "###,###,##0.00"));
                    paymentBean.setAdjustAmount(NumberUtils.formatNumber(payments.getAdjustmentAmt(), "###,###,##0.00"));
                    paymentBean.setGlAccount(payments.getChargeCode());
                    paymentBeans.add(paymentBean);
                }
                before.setTransactions(paymentBeans);
            }
            request.setAttribute("before", before);
        }
        request.setAttribute("after", after);
    }

    public boolean isAnyArBatchesOpen(String startDate, String endDate) throws Exception {
        String fromDate = DateUtils.formatDate(DateUtils.parseDate(startDate, "MM/dd/yyyy"), "yyyy-MM-dd");
        String toDate = DateUtils.formatDate(DateUtils.parseDate(endDate, "MM/dd/yyyy"), "yyyy-MM-dd");
        StringBuilder query = new StringBuilder("select count(*) from ar_batch where status='Open'");
        query.append(" and deposit_date between '").append(fromDate).append("' and '").append(toDate).append("'");
        Object result = new ArBatchDAO().getCurrentSession().createSQLQuery(query.toString()).uniqueResult();
        return null != result && Integer.parseInt(result.toString()) > 0;
    }

    public String validateGLAccountForAccruals(String arBatchId) {
        return new ArBatchDAO().validateGlAccountForAccruals(arBatchId);
    }

    public void importTemplateAndFindInvoices(FileUploadForm fileUploadForm) throws Exception {
        String customerNumber = fileUploadForm.getCustomerNumber();
        HttpServletRequest request = fileUploadForm.getRequest();
        File template = importTemplate(fileUploadForm.getFile());
        List<PaymentBean> invoices = findInvoices(customerNumber, template);
        request.setAttribute("customerNumber", customerNumber);
        request.setAttribute("invoices", invoices);
    }

    public File importTemplate(FormFile file) throws Exception {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            StringBuilder fileName = new StringBuilder(LoadLogisoftProperties.getProperty("reportLocation"));
            fileName.append("/Documents/ArBatch/Import/");
            fileName.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
            fileName.append(file.getFileName());
            File template = new File(fileName.toString());
            inputStream = file.getInputStream();
            outputStream = FileUtils.openOutputStream(template);
            IOUtils.copy(inputStream, outputStream);
            return template;
        } catch (Exception e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        }
    }

    public List<PaymentBean> findInvoices(String customerNumber, File template) throws Exception {
        InputStream inputStream = null;
        try {
            ArBatchDAO arBatchDAO = new ArBatchDAO();
            inputStream = FileUtils.openInputStream(template);
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            List<PaymentBean> invoices = new ArrayList<PaymentBean>();
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row != null && (row.getLastCellNum() >= 3 && isStringCellNotEmpty(row.getCell(0))
                        && row.getCell(1).getCellType() == 0 ? isNumericCellNotEmpty(row.getCell(1)) : isStringCellNotEmpty(row.getCell(1))
                        && isNumericCellNotEmpty(row.getCell(2)))) {
                    String transactionType = getString(row.getCell(0)).toUpperCase();
                    String invoiceOrBl = "";
                    if (row.getCell(1).getCellType() == 0) {
                        invoiceOrBl = convertDuobleToLongAndLongToString(row.getCell(1));
                    } else {
                        invoiceOrBl = getString(row.getCell(1));
                    }
                    Double paidAmount = getDouble(row.getCell(2));
                    if (CommonUtils.in(transactionType, TRANSACTION_TYPE_ACCOUNT_PAYABLE, TRANSACTION_TYPE_ACCOUNT_RECEIVABLE)) {
                        invoices.add(arBatchDAO.getInvoice(customerNumber, transactionType, invoiceOrBl, paidAmount));
                    }
                }
            }
            return invoices;
        } catch (Exception e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    public boolean isStringCellNotEmpty(Cell cell) {
        return null != cell && CommonUtils.isNotEmpty(cell.getStringCellValue());
    }

    public boolean isNumericCellNotEmpty(Cell cell) {
        return null != cell && CommonUtils.isNotEmpty(cell.getNumericCellValue());
    }

    private String getString(Cell cell) {
        return cell.getStringCellValue().trim();
    }

    private Double getDouble(Cell cell) {
        return Double.valueOf(cell.getNumericCellValue());
    }

    private String convertDuobleToLongAndLongToString(Cell cell) {
        Double d = cell.getNumericCellValue();
        if (d != null) {
            return String.valueOf(d.longValue());
        }
        return "";
    }

    public static void copyNsBatchDocuments(String batchId, String customerNo, String invoiceNumber) throws Exception {
        DocumentStoreLogDAO documentStoreLogDAO = new DocumentStoreLogDAO();
        List<DocumentStoreLog> invoiceDocuments = documentStoreLogDAO.getInvoiceDocuments(customerNo + "-" + invoiceNumber);
        Set<String> fileNames = new HashSet<String>();
        if (null != invoiceDocuments && !invoiceDocuments.isEmpty()) {
            for (DocumentStoreLog document : invoiceDocuments) {
                fileNames.add(document.getFileName());
            }
        }
        List<DocumentStoreLog> nsBatchDocuments = documentStoreLogDAO.getNsBatchDocuments(batchId);
        StringBuilder copyFolderPath = new StringBuilder(LoadLogisoftProperties.getProperty("reportLocation"));
        copyFolderPath.append("/Documents/INVOICE/").append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
        File copyFolder = new File(copyFolderPath.toString());
        if (!copyFolder.exists()) {
            copyFolder.mkdirs();
        }
        for (DocumentStoreLog document : nsBatchDocuments) {
            if (!fileNames.contains(document.getFileName())) {
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
    }

    public boolean saveCustomerReference(Integer transactionId, String customerReference) throws Exception {
        customerReference = null != customerReference ? customerReference.toUpperCase() : "";
        AccountingTransactionDAO accountingTransactionDAO = new AccountingTransactionDAO();
        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        User loginUser = (User) request.getSession().getAttribute("loginuser");
        Transaction transaction = accountingTransactionDAO.findById(transactionId);
        String customerNumber = transaction.getCustNo();
        String invoiceOrBl = CommonUtils.isNotEmpty(transaction.getBillLaddingNo()) ? transaction.getBillLaddingNo() : transaction.getInvoiceNumber();

        StringBuilder queryBuilder = new StringBuilder("update ar_transaction_history");
        queryBuilder.append(" set customer_reference_number='").append(customerReference).append("'");
        queryBuilder.append(" where customer_number='").append(customerNumber).append("'");
        queryBuilder.append(" and (invoice_number='").append(invoiceOrBl).append("'");
        queryBuilder.append(" or bl_number='").append(invoiceOrBl).append("')");
        accountingTransactionDAO.getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();

        queryBuilder = new StringBuilder("update transaction");
        queryBuilder.append(" set customer_reference_no='").append(customerReference).append("'");
        queryBuilder.append(" where cust_no='").append(customerNumber).append("'");
        queryBuilder.append(" and Transaction_type='").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
        queryBuilder.append(" and (Invoice_number='").append(invoiceOrBl).append("'");
        queryBuilder.append(" or Bill_Ladding_No='").append(invoiceOrBl).append("')");
        accountingTransactionDAO.getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();

        StringBuilder desc = new StringBuilder();
        desc.append("Customer Reference updated to --> ").append(customerReference);
        desc.append(" by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
        String moduleRefId = customerNumber + "-" + invoiceOrBl;
        AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.AR_INVOICE, moduleRefId, NotesConstants.AR_INVOICE, loginUser);
        return true;
    }

    public boolean checkDisputeStatus(String transactionId) throws Exception {
        return new ArBatchDAO().checkDisputeStatus(transactionId);
    }

    public String[] billToPartyName(String fileNo, String shipmentType) throws Exception {
        return new ArBatchDAO().billToPartyNameAndNo(fileNo, shipmentType);
    }
}
