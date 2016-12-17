package com.gp.cvst.logisoft.domain;

import com.gp.cong.common.CommonUtils;
import java.util.Set;


import com.gp.cong.logisoft.domain.User;
import com.gp.cvst.logisoft.struts.form.BankAccountForm;
import com.logiware.hibernate.domain.AchSetUp;
import java.util.Date;

public class BankDetails implements java.io.Serializable {

    private static final long serialVersionUID = -2194321894610025567L;
    private Integer id;
    private String bankAcctNo;
    private String bankName;
    private String bankAddress;
    private String glAccountno;
    private String acctNo;
    private String acctName;
    private String loginName;
    private Integer startingSerialNo;
    private String aba;
    private String swiftCode;
    private String wireTransferFlag;
    private String bankRoutingNumber;
    private String checkPrinter;
    private String overflowPrinter;
    private Date lastReconciledDate;
    private String bankEmail;
    private Set<User> users;
    private AchSetUp achSetUp;
    private Integer achDebitCount;
    private Integer creditCardCount;
    private Double bankBalance;

    /** default constructor */
    public BankDetails() {
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    /** full constructor */
    public BankDetails(BankAccountForm bankAccountForm) {
        this.bankAcctNo = bankAccountForm.getBankAccountNumber();
        this.bankName = bankAccountForm.getBankName();
        this.bankAddress = bankAccountForm.getBankAddress();
        this.glAccountno = bankAccountForm.getGlAccountNumber();
        this.bankRoutingNumber = bankAccountForm.getBankRoutingNumber();
        this.loginName = bankAccountForm.getLoginName();
        this.acctNo = bankAccountForm.getAccountNumber();
        this.acctName = bankAccountForm.getAccountName();
        if (bankAccountForm.getStartingNumber() != null && !bankAccountForm.getStartingNumber().equals("")) {
            startingSerialNo = new Integer(bankAccountForm.getStartingNumber());
        }
        if (bankAccountForm.getBankAccountId() != null && !bankAccountForm.getBankAccountId().equals("")) {
            this.id = new Integer(bankAccountForm.getBankAccountId());
        }
        this.checkPrinter = CommonUtils.isNotEqual(bankAccountForm.getCheckPrinter(), "0") ? bankAccountForm.getCheckPrinter() : null;
        this.overflowPrinter = CommonUtils.isNotEqual(bankAccountForm.getOverflowPrinter(), "0") ? bankAccountForm.getOverflowPrinter() : null;
        this.bankBalance = null != bankAccountForm.getBankBalance() ? Double.parseDouble(bankAccountForm.getBankBalance()) : 0.00;
    }

    public String getAcctName() {
        return acctName;
    }

    public void setAcctName(String acctName) {
        this.acctName = acctName;
    }

    public String getAcctNo() {
        return acctNo;
    }

    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getBankRoutingNumber() {
        return bankRoutingNumber;
    }

    public void setBankRoutingNumber(String bankRoutingNumber) {
        this.bankRoutingNumber = bankRoutingNumber;
    }

    public Integer getStartingSerialNo() {
        return startingSerialNo;
    }

    public void setStartingSerialNo(Integer startingSerialNo) {
        this.startingSerialNo = startingSerialNo;
    }

    // Property accessors
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBankAcctNo() {
        return this.bankAcctNo;
    }

    public void setBankAcctNo(String bankAcctNo) {
        this.bankAcctNo = bankAcctNo;
    }

    public String getBankName() {
        return this.bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAddress() {
        return this.bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getGlAccountno() {
        return glAccountno;
    }

    public void setGlAccountno(String glAccountno) {
        this.glAccountno = glAccountno;
    }

    public String getAba() {
        return aba;
    }

    public void setAba(String aba) {
        this.aba = aba;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public String getWireTransferFlag() {
        return wireTransferFlag;
    }

    public void setWireTransferFlag(String wireTransferFlag) {
        this.wireTransferFlag = wireTransferFlag;
    }

    public String getCheckPrinter() {
        return checkPrinter;
    }

    public void setCheckPrinter(String checkPrinter) {
        this.checkPrinter = checkPrinter;
    }

    public String getOverflowPrinter() {
        return overflowPrinter;
    }

    public void setOverflowPrinter(String overflowPrinter) {
        this.overflowPrinter = overflowPrinter;
    }

    public AchSetUp getAchSetUp() {
        return achSetUp;
    }

    public void setAchSetUp(AchSetUp achSetUp) {
        this.achSetUp = achSetUp;
    }

    public Date getLastReconciledDate() {
        return lastReconciledDate;
    }

    public void setLastReconciledDate(Date lastReconciledDate) {
        this.lastReconciledDate = lastReconciledDate;
    }

    public String getBankEmail() {
        return bankEmail;
    }

    public void setBankEmail(String bankEmail) {
        this.bankEmail = bankEmail;
    }

    public Integer getAchDebitCount() {
        return achDebitCount;
    }

    public void setAchDebitCount(Integer achDebitCount) {
        this.achDebitCount = achDebitCount;
    }

    public Integer getCreditCardCount() {
        return creditCardCount;
    }

    public void setCreditCardCount(Integer creditCardCount) {
        this.creditCardCount = creditCardCount;
    }

    public Double getBankBalance() {
        return bankBalance;
    }

    public void setBankBalance(Double bankBalance) {
        this.bankBalance = bankBalance;
    }
}
