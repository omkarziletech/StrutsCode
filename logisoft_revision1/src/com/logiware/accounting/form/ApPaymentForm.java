package com.logiware.accounting.form;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.domain.User;
import com.gp.cvst.logisoft.hibernate.dao.BankDetailsDAO;
import com.logiware.accounting.model.InvoiceModel;
import com.logiware.accounting.model.PaymentModel;
import com.logiware.common.model.OptionModel;
import com.logiware.utils.ListUtils;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Lakshmi Narayanan
 */
public class ApPaymentForm extends BaseForm {

    private User user;
    private String bankName;
    private String bankAccount;
    private Integer startingNumber;
    private String paymentDate;
    private String ids;
    private Integer batchId;
    private String batchDesc;
    private List<String> bankNameList;
    private List<OptionModel> bankAccountList;
    private List<PaymentModel> paymentList;
    private List<InvoiceModel> invoiceList;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Integer getStartingNumber() throws Exception {
        if (null == startingNumber && CommonUtils.isNotEmpty(bankAccountList)) {
            startingNumber = new BankDetailsDAO().getStartingNumber(bankNameList.get(0), bankAccountList.get(0).getValue());
        }
        return NumberUtils.isNotZero(startingNumber) ? startingNumber : null;
    }

    public void setStartingNumber(Integer startingNumber) {
        this.startingNumber = startingNumber;
    }

    public String getPaymentDate() throws Exception {
        if (CommonUtils.isEmpty(paymentDate)) {
            paymentDate = DateUtils.formatDate(new Date(), "MM/dd/yyyy");
        }
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

    public Integer getBatchId() {
        return batchId;
    }

    public void setBatchId(Integer batchId) {
        this.batchId = batchId;
    }

    public String getBatchDesc() {
        return batchDesc;
    }

    public void setBatchDesc(String batchDesc) {
        this.batchDesc = batchDesc;
    }

    public List<String> getBankNameList() throws Exception {
        if (null == bankNameList) {
            bankNameList = new BankDetailsDAO().getBankNames(user.getUserId());
        }
        return bankNameList;
    }

    public void setBankNameList(List<String> bankNameList) {
        this.bankNameList = bankNameList;
    }

    public List<OptionModel> getBankAccountList() throws Exception {
        if (null == bankAccountList && CommonUtils.isNotEmpty(bankNameList) && bankNameList.size() == 1) {
            bankAccountList = new BankDetailsDAO().getBankAccounts(bankNameList.get(0), user.getUserId());
        }
        return bankAccountList;
    }

    public void setBankAccountList(List<OptionModel> bankAccountList) {
        this.bankAccountList = bankAccountList;
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
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.user = (User) request.getSession().getAttribute("loginuser");
    }
}
