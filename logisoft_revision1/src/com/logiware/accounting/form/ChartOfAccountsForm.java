package com.logiware.accounting.form;

import com.logiware.accounting.dao.ChartOfAccountsDAO;
import com.logiware.accounting.model.AccountModel;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Lakshmi Narayanan
 */
public class ChartOfAccountsForm extends BaseForm {

    private String startAccount;
    private String endAccount;
    private String accountType;
    private String accountGroup;
    private String accountStatus = "Active";
    private List<AccountModel> accounts;
    private String startPeriod;
    private String endPeriod;
    private Map<String, Double> openingBalances;

    public String getStartAccount() {
        return startAccount;
    }

    public void setStartAccount(String startAccount) {
        this.startAccount = startAccount;
    }

    public String getEndAccount() {
        return endAccount;
    }

    public void setEndAccount(String endAccount) {
        this.endAccount = endAccount;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountGroup() {
        return accountGroup;
    }

    public void setAccountGroup(String accountGroup) {
        this.accountGroup = accountGroup;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public List<AccountModel> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AccountModel> accounts) {
        this.accounts = accounts;
    }

    public List<String> getAccountTypes() {
        return new ChartOfAccountsDAO().getAccountTypes();
    }

    public List<String> getAccountGroups() {
        return new ChartOfAccountsDAO().getAccountGroups();
    }

    public String getStartPeriod() {
        return startPeriod;
    }

    public void setStartPeriod(String startPeriod) {
        this.startPeriod = startPeriod;
    }

    public String getEndPeriod() {
        return endPeriod;
    }

    public void setEndPeriod(String endPeriod) {
        this.endPeriod = endPeriod;
    }

    public Map<String, Double> getClosingBalances() {
        return openingBalances;
    }

    public void setClosingBalances(Map<String, Double>  openingBalances) {
        this.openingBalances = openingBalances;
    }
}
