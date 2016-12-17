package com.logiware.bean;

import com.logiware.utils.ListUtils;
import java.util.List;

/**
 *
 * @author lakshh
 */
public class ComparePaymentsBean {

    private Integer batchId;
    private String checkId;
    private String checkNumber;
    private String checkTotalAmount;
    private String appliedAmount;
    private String checkBalance;
    private String onAccount;
    private String customerNumber;
    private List<PaymentBean> prepayments;
    private List<PaymentBean> chargeCodes;
    private List<PaymentBean> transactions;

    public Integer getBatchId() {
        return batchId;
    }

    public void setBatchId(Integer batchId) {
        this.batchId = batchId;
    }

    public String getAppliedAmount() {
        return appliedAmount;
    }

    public void setAppliedAmount(String appliedAmount) {
        this.appliedAmount = appliedAmount;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public String getCheckBalance() {
        return checkBalance;
    }

    public void setCheckBalance(String checkBalance) {
        this.checkBalance = checkBalance;
    }

    public String getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(String checkNumber) {
        this.checkNumber = checkNumber;
    }

    public String getCheckTotalAmount() {
        return checkTotalAmount;
    }

    public void setCheckTotalAmount(String checkTotalAmount) {
        this.checkTotalAmount = checkTotalAmount;
    }

    public List<PaymentBean> getChargeCodes() throws Exception{
        if (null == chargeCodes) {
            chargeCodes = ListUtils.lazyList(PaymentBean.class);
        }
        return chargeCodes;
    }

    public void setChargeCodes(List<PaymentBean> chargeCodes) {
        this.chargeCodes = chargeCodes;
    }

    public String getOnAccount() {
        return onAccount;
    }

    public void setOnAccount(String onAccount) {
        this.onAccount = onAccount;
    }

    public List<PaymentBean> getPrepayments() throws Exception{
        if (null == prepayments) {
            prepayments = ListUtils.lazyList(PaymentBean.class);
        }
        return prepayments;
    }

    public void setPrepayments(List<PaymentBean> prepayments) {
        this.prepayments = prepayments;
    }

    public List<PaymentBean> getTransactions() throws Exception {
        if (null == transactions) {
            transactions = ListUtils.lazyList(PaymentBean.class);
        }
        return transactions;
    }

    public void setTransactions(List<PaymentBean> transactions) {
        this.transactions = transactions;
    }
}
