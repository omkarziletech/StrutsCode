package com.logiware.accounting.form;

import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.domain.User;
import com.logiware.accounting.model.InvoiceModel;
import com.logiware.accounting.model.PaymentModel;
import com.logiware.utils.ListUtils;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Lakshmi Narayanan
 */
public class CheckRegisterForm extends BaseForm {

    private String glAccount;
    private String bankAccount;
    private String reconcileDate;
    private String fromCheckNumber;
    private String toCheckNumber;
    private String amountOperator;
    private String paymentAmount;
    private String batchId;
    private String paymentMethod;
    private String checkNumber;
    private String paymentDate;
    private String ids;
    private List<PaymentModel> paymentList;
    private List<InvoiceModel> invoiceList;
    private User user;

    public String getGlAccount() {
        return glAccount;
    }

    public void setGlAccount(String glAccount) {
        this.glAccount = glAccount;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getReconcileDate() {
        return reconcileDate;
    }

    public void setReconcileDate(String reconcileDate) {
        this.reconcileDate = reconcileDate;
    }

    public String getFromCheckNumber() {
        return fromCheckNumber;
    }

    public void setFromCheckNumber(String fromCheckNumber) {
        this.fromCheckNumber = fromCheckNumber;
    }

    public String getToCheckNumber() {
        return toCheckNumber;
    }

    public void setToCheckNumber(String toCheckNumber) {
        this.toCheckNumber = toCheckNumber;
    }

    public String getAmountOperator() {
        return amountOperator;
    }

    public void setAmountOperator(String amountOperator) {
        this.amountOperator = amountOperator;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(String checkNumber) {
        this.checkNumber = checkNumber;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public List<PaymentModel> getPaymentList() throws Exception {
        if (null == paymentList) {
            paymentList = ListUtils.lazyList(PaymentModel.class);
        }
        return paymentList;
    }

    public void setPaymentList(List<PaymentModel> paymentList) {
        this.paymentList = paymentList;
    }

    public List<InvoiceModel> getInvoiceList() {
        return invoiceList;
    }

    public void setInvoiceList(List<InvoiceModel> invoiceList) {
        this.invoiceList = invoiceList;
    }

    @Override
    public String getStatus() {
        if (null == super.getStatus()) {
            super.setStatus("All");
        }
        return super.getStatus();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String getFromDate() throws Exception {
        if (null == super.getFromDate()) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -1);
            super.setFromDate(DateUtils.formatDate(cal.getTime(), "MM/dd/yyyy"));
        }
        return super.getFromDate();
    }

    @Override
    public String getToDate() throws Exception {
        if (null == super.getToDate()) {
            super.setToDate(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
        }
        return super.getToDate();
    }

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        user = (User) request.getSession().getAttribute("loginuser");
    }
}
