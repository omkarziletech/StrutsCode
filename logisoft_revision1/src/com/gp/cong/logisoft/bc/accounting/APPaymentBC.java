package com.gp.cong.logisoft.bc.accounting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.util.LabelValueBean;
import org.directwebremoting.WebContextFactory;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.reports.ApPaymentBatchReportPdfCreator;
import com.gp.cong.logisoft.reports.ApPaymentPdfCreator;
import com.gp.cong.logisoft.util.DBUtil;
import com.gp.cvst.logisoft.beans.TransactionBean;
import com.gp.cvst.logisoft.domain.ApBatch;
import com.gp.cvst.logisoft.domain.BankDetails;
import com.gp.cvst.logisoft.domain.Transaction;
import com.gp.cvst.logisoft.hibernate.dao.ApBatchDAO;
import com.gp.cvst.logisoft.hibernate.dao.BankDetailsDAO;
import com.gp.cvst.logisoft.hibernate.dao.TransactionDAO;
import com.gp.cvst.logisoft.hibernate.dao.TransactionLedgerDAO;
import com.gp.cvst.logisoft.struts.form.APPaymentForm;
import com.logiware.utils.AuditNotesUtils;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.servlet.ServletContext;

/**
 * @author user
 *
 */
public class APPaymentBC {

    TransactionDAO transactionDAO = new TransactionDAO();
    DBUtil dbUtil = null;
    BankDetailsDAO bankDetailsDAO = null;

    /**
     * @param aPPaymentForm
     * @return
     */
    public List getAllCustomer(APPaymentForm aPPaymentForm) throws Exception {
        return transactionDAO.getCustomers(aPPaymentForm.getVendor(), aPPaymentForm.getVendorNumber());
    }

    public List getAllCustomerByUserId(APPaymentForm aPPaymentForm, User loginUser) throws Exception {
        return transactionDAO.getReadyToPayCustomersByUserId(aPPaymentForm.getVendorNumber(), aPPaymentForm.getVendor(), loginUser);
    }

    /**
     * @return
     */
    public List getPamentList() {
        dbUtil = new DBUtil();
        return dbUtil.getPaymentList();
    }

    public List getPaymentList(String vendorName) throws Exception {
        List genericCodeList = new ArrayList();
        TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
        List result = tradingPartnerDAO.getPaymentListByVendorName(vendorName);
        for (Object elements : result) {
            genericCodeList.add(new LabelValueBean(elements.toString(), elements.toString()));
        }
        return genericCodeList;
    }

    /**
     * @param aPPaymentForm
     * @return
     */
    public List getCustometDetailList(APPaymentForm aPPaymentForm) throws Exception {
        List<TransactionBean> custometDetailList = new ArrayList<TransactionBean>();
        return custometDetailList = transactionDAO.getCustomerDetails(aPPaymentForm.getVendorNumber(), aPPaymentForm.getInvoiceNumber());

    }

    public List deductAmount(APPaymentForm aPPaymentForm) throws Exception {
        String transactionId[] = StringUtils.splitPreserveAllTokens(aPPaymentForm.getTotalAmount(), ',');
        for (int i = 0; i < transactionId.length; i++) {
            if (transactionId[i] != null && !transactionId[i].trim().equals("")) {
                Transaction transaction = transactionDAO.findById(new Integer(transactionId[i]));
                transaction.setStatus("RemovedFromPay");
            }
        }
        /*List customerList = getAllCustomer(aPPaymentForm);
        NumberFormat numberFormat=new DecimalFormat("##,###,##0.00");
        for (int i=0;i<customerList.size();i++) {
        TransactionBean transactionBean = (TransactionBean) customerList.get(i);
        if(transactionBean.getCustomerNo()!=null && transactionBean.getCustomerNo().equals(aPPaymentForm.getVendorNumber())){
        if(transactionBean.getAmount()!=null){
        double amount = Double.parseDouble(transactionBean.getAmount().replace(",",""));
        if(aPPaymentForm.getTotalAmount()!=null && !aPPaymentForm.getTotalAmount().equals("")){
        transactionBean.setAmount(numberFormat.format(amount-Double.parseDouble(aPPaymentForm.getTotalAmount().replace(",", ""))));
        customerList.set(i, transactionBean );
        }
        }
        }

        }*/
        return getAllCustomer(aPPaymentForm);

    }

    public String getPaymentsByTransactionId(String transactionIds, boolean canEdit) throws Exception {
        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginuser");
        if (null == loginUser) {
            return CommonConstants.MESSAGE_LOGIN_ERROR;
        }
        if (null != transactionIds && transactionIds.startsWith(",")) {
            transactionIds = transactionIds.substring(1, transactionIds.length());
        }
        if (null != transactionIds && transactionIds.endsWith(",")) {
            transactionIds = transactionIds.substring(0, transactionIds.length() - 1);
        }
        List<TransactionBean> paymentDetailsList = transactionDAO.getPaymentsByTransactionId(transactionIds);
        request.setAttribute("transactionIds", transactionIds);
        request.setAttribute("customersDetailList", paymentDetailsList);
        request.setAttribute("canEdit", canEdit);
        return WebContextFactory.get().forwardToString(
                "/jsps/AccountsPayable/APPaymentDetailsTemplate.jsp");
    }

    public String getPaymentsByInvoiceNumberAndVendorNumber(String invoiceNumber, String vendorNumber) throws Exception {
        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        List<TransactionBean> paymentList = transactionDAO.getPaymentsByInvoiceNumberAndVendorNumber(invoiceNumber, vendorNumber);
        request.setAttribute("customersDetailList", paymentList);
        return WebContextFactory.get().forwardToString(
                "/jsps/AccountsPayable/APPaymentDetailsTemplate.jsp");
    }

    public String getPaymentsByVendorNumber(String vendorNumber) throws Exception {
        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginuser");
        if (null == loginUser) {
            return CommonConstants.MESSAGE_LOGIN_ERROR;
        }
        List<TransactionBean> paymentList = transactionDAO.getPaymentsByVendorNumber(vendorNumber);
        request.setAttribute("customersDetailList", paymentList);
        return WebContextFactory.get().forwardToString(
                "/jsps/AccountsPayable/APPaymentDetailsTemplate.jsp");
    }

    public String getPaymentsByVendorNameAndVendorNumber(String vendorName, String vendorNumber) throws Exception {

        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginuser");
        if (null == loginUser) {
            return CommonConstants.MESSAGE_LOGIN_ERROR;
        }
        List<TransactionBean> listOfCustomer = null;
        if (null != loginUser.getRole() && loginUser.getRole().getRoleDesc().equalsIgnoreCase(CommonConstants.ROLE_NAME_APSPECIALIST)) {
            listOfCustomer = transactionDAO.getCustomersByUserId(vendorName, vendorNumber, loginUser);
        }
        if (null != loginUser.getRole() && loginUser.getRole().getRoleDesc().equalsIgnoreCase(CommonConstants.ROLE_NAME_SUPERVISOR)) {
            listOfCustomer = transactionDAO.getCustomers(vendorName, vendorNumber);
        }
        if (null != loginUser.getRole() && loginUser.getRole().getRoleDesc().equalsIgnoreCase(CommonConstants.ROLE_NAME_ADMIN)) {
            listOfCustomer = transactionDAO.getCustomers(vendorName, vendorNumber);
        }
        List paymentMethodList = new ArrayList();
        for (TransactionBean customer : listOfCustomer) {
            paymentMethodList.add(getPaymentList(customer.getCustomer()));
        }
        request.setAttribute("listOfcustomers", listOfCustomer);
        request.setAttribute("Paymentlist", paymentMethodList);
        return WebContextFactory.get().forwardToString(
                "/jsps/AccountsPayable/APPayamentListTemplate.jsp");
    }

    public String removePayment(String removedId, String transactionIds) throws Exception {
        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginuser");
        if (removedId != null && !removedId.trim().equals("")) {
            Transaction transaction = transactionDAO.findById(new Integer(removedId));
            transaction.setStatus(CommonConstants.STATUS_OPEN);
            if (null != transaction.getTransactionType() && transaction.getTransactionType().trim().equals(CommonConstants.TRANSACTION_TYPE_ACCOUNT_PAYABLE)) {
                transaction.setUpdatedOn(new Date());
                transaction.setUpdatedBy(loginUser.getUserId());
                StringBuilder desc = new StringBuilder("Invoice '").append(transaction.getInvoiceNumber()).append("'");
                desc.append(" of '").append(transaction.getCustName()).append("'");
                desc.append(" for amount '").append(NumberUtils.formatNumber(transaction.getTransactionAmt(), "###,###,##0.00")).append("'");
                desc.append(" is released from ready to pay by ").append(loginUser.getLoginName()).append(" on ");
                desc.append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
                AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.AP_INVOICE, transaction.getCustNo() + "-" + transaction.getInvoiceNumber(), NotesConstants.AP_INVOICE, loginUser);
            } else if (null != transaction.getTransactionType() && transaction.getTransactionType().trim().equals(CommonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE)) {
                transaction.setUpdatedOn(new Date());
                transaction.setUpdatedBy(loginUser.getUserId());
            }
            if (null != transaction) {
                if (null != transactionIds) {
                    if (transactionIds.contains(removedId)) {
                        transactionIds = transactionIds.replaceAll(removedId, "");
                    }
                    if (transactionIds.contains(",,")) {
                        transactionIds = transactionIds.replaceAll(",,", ",");
                    }
                    if (transactionIds.startsWith(",")) {
                        transactionIds = transactionIds.substring(1, transactionIds.length());
                    }
                    if (transactionIds.endsWith(",")) {
                        transactionIds = transactionIds.substring(0, transactionIds.length() - 1);
                    }
                    if (!transactionIds.trim().equals("")) {
                        List<TransactionBean> paymentDetailsList = transactionDAO.getPaymentsByTransactionId(transactionIds);
                        request.setAttribute("transactionIds", transactionIds);
                        request.setAttribute("customersDetailList", paymentDetailsList);
                        return WebContextFactory.get().forwardToString("/jsps/AccountsPayable/APPaymentDetailsTemplate.jsp");
                    }
                }
                return CommonConstants.MESSAGE_SUCCESS;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public String setBatchIdForPayment() throws Exception {
        Integer newApBatchId = 1;
        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        String lastBatchId = lastBatchId = transactionDAO.getLastBatchIdForPayment();
        if (CommonUtils.isNotEmpty(lastBatchId)) {
            newApBatchId = Integer.parseInt(lastBatchId) + 1;
        }
        new ApBatchDAO().save(new ApBatch(newApBatchId, ""));
        request.setAttribute("newApBatchId", "" + newApBatchId);
        return WebContextFactory.get().forwardToString("/jsps/AccountsPayable/APBatchForPaymentTemplate.jsp");
    }

    public String removeBatchIdFromPayment(String apBatchId) throws Exception {
        ApBatchDAO apBatchDAO = new ApBatchDAO();
        ApBatch apBatch = apBatchDAO.findById(apBatchId);
        apBatchDAO.delete(apBatch);
        return "";
    }

    public List getPaymentsByVendor(String vendor, String vendorNumber) throws Exception {
        return transactionDAO.getCustomers(vendor, vendorNumber);
    }

    public List getBankNameList(User loginUser) throws Exception {
        com.gp.cvst.logisoft.util.DBUtil dbUtil = new com.gp.cvst.logisoft.util.DBUtil();
        List bankNameList = dbUtil.getBankAccountList(loginUser.getUserId(), "apPaymentBankName");
        Set set = new HashSet();
        List newList = new ArrayList();
        for (Iterator iter = bankNameList.iterator(); iter.hasNext();) {
            Object element = iter.next();
            if (set.add(element)) {
                newList.add(element);
            }
        }
        bankNameList.clear();
        bankNameList.addAll(newList);
        return bankNameList;
    }

    public List<BankDetails> getBankNameList(User loginUser, String bankName) throws Exception {
        bankDetailsDAO = new BankDetailsDAO();
        return bankDetailsDAO.getBankNamesByUserRole(loginUser, bankName);
    }

    public List getPaymentsByInvoiceNumberAndVendorName(String invoiceNumber, String vendorNumber) throws Exception {
        return transactionDAO.getPaymentsByInvoiceNumberAndVendorNumber(invoiceNumber, vendorNumber);
    }

    public List getAllBankName()throws Exception {
        bankDetailsDAO = new BankDetailsDAO();
        return bankDetailsDAO.getAllBankName();
    }

    public List getAccountNumberListForVendor(String vendorName, String bankName)throws Exception {
        bankDetailsDAO = new BankDetailsDAO();
        List<String> bankAccountNumberList = new ArrayList<String>();
        List<LabelValueBean> list = new ArrayList<LabelValueBean>();
        bankAccountNumberList = bankDetailsDAO.getAccountNumberListForVendor(vendorName, bankName);
        for (Iterator iterator = bankAccountNumberList.iterator(); iterator.hasNext();) {
            String bankAccountNumber = (String) iterator.next();
            list.add(new LabelValueBean(bankAccountNumber, bankAccountNumber));
        }
        return list;
    }

    public List getAccountNumberByBankName(String bankName)throws Exception {
        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginuser");
        List<LabelValueBean> bankAccountNumberList = new ArrayList<LabelValueBean>();
        List<LabelValueBean> list = new ArrayList<LabelValueBean>();
        bankAccountNumberList = new com.gp.cvst.logisoft.util.DBUtil().getBankAccountNoList(loginUser.getUserId(), bankName);
        if (null != bankAccountNumberList) {
            if (bankAccountNumberList.size() >= 1) {
                list.add(new LabelValueBean("Select One", "0"));
                for (LabelValueBean labelValueBean : bankAccountNumberList) {
                    list.add(new LabelValueBean(labelValueBean.getLabel(), labelValueBean.getValue()));
                }
            }
        }
        return list;
    }

    public List<LabelValueBean> getAccountNumberByDefault(User loginUser, String bankName)throws Exception {
        List<LabelValueBean> bankAccountNumberList = new ArrayList<LabelValueBean>();
        List<LabelValueBean> list = new ArrayList<LabelValueBean>();
        bankAccountNumberList = new com.gp.cvst.logisoft.util.DBUtil().getBankAccountNoList(loginUser.getUserId(), bankName);
        if (null != bankAccountNumberList) {
            if (bankAccountNumberList.size() > 1) {
                list.add(new LabelValueBean("Select One", "0"));
                for (LabelValueBean labelValueBean : bankAccountNumberList) {
                    list.add(new LabelValueBean(labelValueBean.getLabel(), labelValueBean.getValue()));
                }
            } else if (bankAccountNumberList.size() == 1) {
                for (LabelValueBean labelValueBean : bankAccountNumberList) {
                    list.add(new LabelValueBean(labelValueBean.getLabel(), labelValueBean.getValue()));
                }
            }
        }
        return list;
    }

    public void insertTransactionLedgerRecordsForMakePayment(String[] ids) throws Exception {
        Transaction transaction = null;
        TransactionLedgerDAO transactionLedgerDAO = new TransactionLedgerDAO();
        if (ids != null) {
            transaction = transactionDAO.findById(new Integer(ids[0]));
        }
        transactionLedgerDAO.insertTransactionLedgerRecordsForMakePayment(transaction);
    }

    public void updateStartingNumber(String bankName, String bankAccountNo, Integer startingNo)throws Exception {
        bankDetailsDAO = new BankDetailsDAO();
        bankDetailsDAO.updateStartingNumber(bankName, bankAccountNo, startingNo);
    }
//		This is for Payment Print

    public String createPaymentReport(String idsForCheckTransaction, String checkNumber, String paymentDate, String fileName, String contextPath) throws IOException, Exception {
        idsForCheckTransaction = StringUtils.removeStart(StringUtils.removeEnd(idsForCheckTransaction, ","), ",");
        String[] ids = StringUtils.splitByWholeSeparator(idsForCheckTransaction, ",");
        List<TransactionBean> paymentList = new ArrayList<TransactionBean>();
        String vendorNumber = null;
        NumberFormat number = new DecimalFormat("###,###,##0.00");
        for (String id : ids) {
            Transaction transaction = transactionDAO.findById(Integer.parseInt(id));
            if (null != transaction) {
                TransactionBean transactionBean = new TransactionBean(transaction);
                vendorNumber = transaction.getCustNo();
                transactionBean.setTransDate(DateUtils.parseDateToString(transaction.getTransactionDate()));
                if (null != transaction.getTransactionType() && transaction.getTransactionType().trim().equals(CommonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE)) {
                    transactionBean.setAmount(number.format((-1) * transaction.getBalance()));
                }
                paymentList.add(transactionBean);
            }
        }
        ApPaymentPdfCreator apPaymentPdfCreator = new ApPaymentPdfCreator();
        return apPaymentPdfCreator.createReport(checkNumber, vendorNumber, fileName, paymentDate, paymentList);
    }

    //This is for Payment Batch Report
    public void createPaymentBatchReport(Integer batchId, String transactionType, String status, String fileName, APPaymentForm apPaymentForm, String realPath) throws Exception {
        List<TransactionBean> batchList = transactionDAO.getPaymentsListByBatchID(batchId, transactionType, status);
        ApPaymentBatchReportPdfCreator apPaymentBatchReportPdfCreator = new ApPaymentBatchReportPdfCreator();
        String batchFileName = apPaymentBatchReportPdfCreator.createBatchReport(batchId, fileName, batchList, apPaymentForm, realPath);
    }

    public String validatePrinters(String bankName, String bankAccountNo) throws Exception {
        bankDetailsDAO = new BankDetailsDAO();
        List<BankDetails> bankList = bankDetailsDAO.getBankDetails(bankName, bankAccountNo);
        String checkPrinter = "";
        String overflowPrinter = "";
        for (BankDetails bankDetails : bankList) {
            checkPrinter = bankDetails.getCheckPrinter();
            overflowPrinter = bankDetails.getOverflowPrinter();
        }
        if (null == checkPrinter || checkPrinter.trim().equals("")) {
            return "checkPrinterError";
        } else if (null == overflowPrinter || overflowPrinter.trim().equals("")) {
            return "overflowPrinterError";
        } else {
            return CommonConstants.SUCCESS;
        }
    }
}
