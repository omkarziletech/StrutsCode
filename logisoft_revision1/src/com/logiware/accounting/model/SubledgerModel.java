package com.logiware.accounting.model;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.logiware.hibernate.domain.TransactionLedgerHistory;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SubledgerModel {

    private double debit;
    private double credit;
    private String glAccount;
    private String glAccountDesc;
    private String chargeCode;
    private String amount;

    public SubledgerModel() {
    }

    public SubledgerModel(String chargeCode, String amount) {
	this.chargeCode = chargeCode;
	this.amount = amount;
    }

    public double getCredit() {
	return this.credit;
    }

    public void setCredit(double credit) {
	this.credit = credit;
    }

    public double getDebit() {
	return this.debit;
    }

    public void setDebit(double debit) {
	this.debit = debit;
    }

    public String getGlAccount() {
	return this.glAccount;
    }

    public void setGlAccount(String glAccount) {
	this.glAccount = glAccount;
    }

    public String getGlAccountDesc() {
	return this.glAccountDesc;
    }

    public void setGlAccountDesc(String glAccountDesc) {
	this.glAccountDesc = glAccountDesc;
    }

    public String getChargeCode() {
	return this.chargeCode;
    }

    public void setChargeCode(String chargeCode) {
	this.chargeCode = chargeCode;
    }

    public String getAmount() {
	return this.amount;
    }

    public void setAmount(String amount) {
	this.amount = amount;
    }
}