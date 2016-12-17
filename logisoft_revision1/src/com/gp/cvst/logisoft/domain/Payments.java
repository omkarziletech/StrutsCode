package com.gp.cvst.logisoft.domain;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.domain.User;
import com.gp.cvst.logisoft.AccountingConstants;
import java.util.Date;

import com.gp.cvst.logisoft.hibernate.dao.FclBlCostCodesDAO;
import com.logiware.bean.PaymentBean;
import com.logiware.hibernate.dao.AccountingLedgerDAO;
import com.logiware.hibernate.dao.AccountingTransactionDAO;
import com.logiware.utils.AuditNotesUtils;
import org.apache.commons.lang3.StringUtils;

public class Payments implements java.io.Serializable, ConstantsInterface {

    private Integer id;
    private Integer batchId;
    private Date batchDate;
    private String custNo;
    private Date checkDate;
    private Double paymentAmt;
    private String paymentType;
    private String chargeCode;
    private String invoiceNo;
    private String billLaddingNo;
    private Double adjustmentAmt;
    private String check_no;
    private Date adjustmentDate;
    private String userName;
    private Integer paymentCheckId;
    private String notes;
    private Integer transactionId;
    private String transactionType;

    public Payments() {
    }

    public Payments(ArBatch arBatch, PaymentBean paymentBean, PaymentChecks paymentChecks, User loginUser, boolean isNew) throws Exception {
        this.batchId = arBatch.getBatchId();
        this.batchDate = arBatch.getDate();
        if (CommonUtils.isNotEmpty(paymentBean.getTransactionId())) {
            this.paymentType = "P";
            if (paymentBean.getTransactionId().endsWith(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE)) {
                Integer txnId = Integer.parseInt(paymentBean.getTransactionId().replace(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE, ""));
                Transaction transaction = new AccountingTransactionDAO().findById(txnId);
                this.custNo = transaction.getCustNo();
                this.chargeCode = paymentBean.getGlAccount();
                this.paymentAmt = Double.parseDouble(paymentBean.getPaidAmount());
                this.transactionId = transaction.getTransactionId();
                this.transactionType = transaction.getTransactionType();
                this.invoiceNo = StringUtils.left(transaction.getInvoiceNumber(), 30);
                this.billLaddingNo = transaction.getBillLaddingNo();
                this.adjustmentAmt = 0d;
                if (CommonUtils.isNotEmpty(paymentBean.getAdjustAmount())
                        && CommonUtils.isNotEmpty(paymentBean.getGlAccount())) {
                    this.adjustmentAmt = Double.parseDouble(paymentBean.getAdjustAmount());
                }
                this.adjustmentDate = new Date();
                transaction.setBalanceInProcess(transaction.getBalanceInProcess() - this.paymentAmt - this.adjustmentAmt);
                transaction.setUpdatedBy(loginUser.getUserId());
                transaction.setUpdatedOn(new Date());
                if (isNew) {
                    String invoiceOrBl = CommonUtils.isNotEmpty(transaction.getBillLaddingNo()) ? transaction.getBillLaddingNo() : transaction.getInvoiceNumber();
                    String key = transaction.getCustNo() + "-" + invoiceOrBl;
                    StringBuilder desc = new StringBuilder("Invoice/BL '").append(invoiceOrBl).append("'");
                    desc.append(" of '").append(transaction.getCustNo()).append("'");
                    desc.append(" saved in this batch - '").append(arBatch.getBatchId()).append("'");
                    desc.append(" by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
                    AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.AR_INVOICE, key, NotesConstants.AR_INVOICE, loginUser);
                }
            } else if (paymentBean.getTransactionId().endsWith(TRANSACTION_TYPE_ACCOUNT_PAYABLE)) {
                Integer txnId = Integer.parseInt(paymentBean.getTransactionId().replace(TRANSACTION_TYPE_ACCOUNT_PAYABLE, ""));
                Transaction transaction = new AccountingTransactionDAO().findById(txnId);
                this.custNo = transaction.getCustNo();
                this.chargeCode = paymentBean.getGlAccount();
                this.paymentAmt = Double.parseDouble(paymentBean.getPaidAmount());
                this.transactionId = transaction.getTransactionId();
                this.transactionType = transaction.getTransactionType();
                this.invoiceNo = StringUtils.left(transaction.getInvoiceNumber(), 30);
                this.billLaddingNo = transaction.getBillLaddingNo();
                this.adjustmentAmt = 0d;
                this.adjustmentDate = null;
                transaction.setStatus(STATUS_PENDING);
                transaction.setBalanceInProcess(0d);
                transaction.setUpdatedBy(loginUser.getUserId());
                transaction.setUpdatedOn(new Date());
                if (isNew) {
                    String key = transaction.getCustNo() + "-" + transaction.getInvoiceNumber();
                    StringBuilder desc = new StringBuilder("Invoice '").append(transaction.getInvoiceNumber()).append("'");
                    desc.append(" of '").append(transaction.getCustNo()).append("'");
                    desc.append(" saved in this batch - '").append(arBatch.getBatchId()).append("'");
                    desc.append(" by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
                    AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.AP_INVOICE, key, NotesConstants.AP_INVOICE, loginUser);
                }
            } else {
                Integer txnId = Integer.parseInt(paymentBean.getTransactionId().replace(TRANSACTION_TYPE_ACCRUALS, ""));
                TransactionLedger transactionLedger = new AccountingLedgerDAO().findById(txnId);
                this.custNo = transactionLedger.getCustNo();
                this.chargeCode = paymentBean.getGlAccount();
                this.paymentAmt = Double.parseDouble(paymentBean.getPaidAmount());
                this.transactionId = transactionLedger.getTransactionId();
                this.transactionType = transactionLedger.getTransactionType();
                this.invoiceNo = StringUtils.left(transactionLedger.getInvoiceNumber(), 30);
                this.billLaddingNo = transactionLedger.getBillLaddingNo();
                this.adjustmentAmt = 0d;
                this.adjustmentDate = null;
                transactionLedger.setBalanceInProcess(0d);
                transactionLedger.setUpdatedBy(loginUser.getUserId());
                transactionLedger.setUpdatedOn(new Date());
                transactionLedger.setStatus(STATUS_PENDING);
                FclBlCostCodesDAO fclBlCostCodesDAO = new FclBlCostCodesDAO();
                if (CommonUtils.isNotEmpty(transactionLedger.getCostId())) {
                    FclBlCostCodes fclBlCostCodes = fclBlCostCodesDAO.findById(transactionLedger.getCostId());
                    if (null != fclBlCostCodes) {
                        fclBlCostCodes.setInvoiceNumber(transactionLedger.getInvoiceNumber());
                        fclBlCostCodes.setTransactionType(TRANSACTION_TYPE_PENDING);
                    }
                }
                if (isNew) {
                    String key = txnId.toString();
                    StringBuilder desc = new StringBuilder("Accrual of ");
                    boolean addAnd = false;
                    if (CommonUtils.isNotEmpty(transactionLedger.getBillLaddingNo())) {
                        desc.append("B/L - '").append(transactionLedger.getBillLaddingNo().trim()).append("'");
                        addAnd = true;
                    }
                    if (CommonUtils.isNotEmpty(transactionLedger.getDocReceipt())) {
                        if (addAnd) {
                            desc.append(" and ");
                        }
                        desc.append("Doc Receipt - '").append(transactionLedger.getDocReceipt()).append("'");
                        addAnd = true;
                    }
                    if (CommonUtils.isNotEmpty(transactionLedger.getVoyageNo())) {
                        if (addAnd) {
                            desc.append(" and ");
                        }
                        desc.append("Voyage - '").append(transactionLedger.getVoyageNo()).append("'");
                    }
                    desc.append(" of '").append(transactionLedger.getCustNo()).append("'");
                    desc.append(" saved in this batch - '").append(arBatch.getBatchId()).append("'");
                    desc.append(" by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
                    AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.ACCRUALS, key, NotesConstants.ACCRUALS, loginUser);
                }
            }
        } else {
            this.paymentType = "Check";
            this.custNo = paymentChecks.getCustId();
            this.chargeCode = paymentBean.getGlAccount();
            this.paymentAmt = Double.parseDouble(paymentBean.getPaidAmount());
            this.transactionId = null;
            this.transactionType = null;
            this.adjustmentAmt = 0d;
            this.adjustmentDate = null;
            this.billLaddingNo = paymentBean.getDocReceipt();
            this.notes = paymentBean.getNotes();
            if (CommonUtils.isNotEmpty(paymentBean.getDocReceipt())) {
                this.invoiceNo = AccountingConstants.PRE_PAYMENT;
            } else if (CommonUtils.isNotEmpty(paymentBean.getGlAccount())) {
                this.invoiceNo = AccountingConstants.CHARGE_CODE;
            } else {
                this.invoiceNo = AccountingConstants.ON_ACCOUNT;
            }
        }
        this.userName = loginUser.getLoginName();
        this.check_no = paymentChecks.getCheckNo();
        this.paymentCheckId = paymentChecks.getId();
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBatchId() {
        return this.batchId;
    }

    public void setBatchId(Integer batchId) {
        this.batchId = batchId;
    }

    public Date getBatchDate() {
        return this.batchDate;
    }

    public void setBatchDate(Date batchDate) {
        this.batchDate = batchDate;
    }

    public String getCustNo() {
        return this.custNo;
    }

    public void setCustNo(String custNo) {
        this.custNo = custNo;
    }

    public Date getCheckDate() {
        return this.checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

    public Double getPaymentAmt() {
        return this.paymentAmt;
    }

    public void setPaymentAmt(Double paymentAmt) {
        this.paymentAmt = paymentAmt;
    }

    public String getPaymentType() {
        return this.paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getChargeCode() {
        return this.chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getInvoiceNo() {
        return this.invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getBillLaddingNo() {
        return this.billLaddingNo;
    }

    public void setBillLaddingNo(String billLaddingNo) {
        this.billLaddingNo = billLaddingNo;
    }

    public Double getAdjustmentAmt() {
        return this.adjustmentAmt;
    }

    public void setAdjustmentAmt(Double adjustmentAmt) {
        this.adjustmentAmt = adjustmentAmt;
    }

    public String getCheck_no() {
        return check_no;
    }

    public void setCheck_no(String check_no) {
        this.check_no = check_no;
    }

    public Date getAdjustmentDate() {
        return adjustmentDate;
    }

    public void setAdjustmentDate(Date adjustmentDate) {
        this.adjustmentDate = adjustmentDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getPaymentCheckId() {
        return paymentCheckId;
    }

    public void setPaymentCheckId(Integer paymentCheckId) {
        this.paymentCheckId = paymentCheckId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
