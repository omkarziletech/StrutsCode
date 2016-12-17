package com.gp.cvst.logisoft.struts.form;

import com.gp.cong.common.CommonUtils;
import com.gp.cvst.logisoft.AccountingConstants;
import com.gp.cvst.logisoft.domain.Budget;
import com.gp.cvst.logisoft.hibernate.dao.FiscalPeriodDAO;
import com.logiware.utils.ListUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.LabelValueBean;

public class BudgetForm extends ActionForm {
    private static final long serialVersionUID = 449276577711144352L;

    private String mainAccount;
    private String copyAccount;
    private String budgetMethod;
    private String mainDescription;
    private String copyDescription;
    private String amount;
    private String mainBudgetSet;
    private String copyBudgetSet;
    private String fiscalSet = AccountingConstants.FISCAL_SET_BUDGET;
    private String increaseBy;
    private Integer mainYear;
    private Integer copyYear;
    private String mainCurrency;
    private String copyCurrency;
    private FormFile budgetSheet;
    private String action;
    private List<LabelValueBean> budgetMethods;
    private List<LabelValueBean> budgetSets;
    private List<LabelValueBean> years;
    private List<LabelValueBean> currencies;
    private List<Budget> budgets;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBudgetMethod() {
        return budgetMethod;
    }

    public void setBudgetMethod(String budgetMethod) {
        this.budgetMethod = budgetMethod;
    }

    public FormFile getBudgetSheet() {
        return budgetSheet;
    }

    public void setBudgetSheet(FormFile budgetSheet) {
        this.budgetSheet = budgetSheet;
    }

    public String getCopyAccount() {
        return copyAccount;
    }

    public void setCopyAccount(String copyAccount) {
        this.copyAccount = copyAccount;
    }

    public String getCopyBudgetSet() {
        return copyBudgetSet;
    }

    public void setCopyBudgetSet(String copyBudgetSet) {
        this.copyBudgetSet = copyBudgetSet;
    }

    public String getCopyCurrency() {
        return copyCurrency;
    }

    public void setCopyCurrency(String copyCurrency) {
        this.copyCurrency = copyCurrency;
    }

    public String getCopyDescription() {
        return copyDescription;
    }

    public void setCopyDescription(String copyDescription) {
        this.copyDescription = copyDescription;
    }

    public Integer getCopyYear() {
        return copyYear;
    }

    public void setCopyYear(Integer copyYear) {
        this.copyYear = copyYear;
    }

    public String getFiscalSet() {
        return fiscalSet;
    }

    public void setFiscalSet(String fiscalSet) {
        this.fiscalSet = fiscalSet;
    }

    public String getIncreaseBy() {
        return increaseBy;
    }

    public void setIncreaseBy(String increaseBy) {
        this.increaseBy = increaseBy;
    }

    public String getMainAccount() {
        return mainAccount;
    }

    public void setMainAccount(String mainAccount) {
        this.mainAccount = mainAccount;
    }

    public String getMainBudgetSet() {
        return mainBudgetSet;
    }

    public void setMainBudgetSet(String mainBudgetSet) {
        this.mainBudgetSet = mainBudgetSet;
    }

    public String getMainCurrency() {
        return mainCurrency;
    }

    public void setMainCurrency(String mainCurrency) {
        this.mainCurrency = mainCurrency;
    }

    public String getMainDescription() {
        return mainDescription;
    }

    public void setMainDescription(String mainDescription) {
        this.mainDescription = mainDescription;
    }

    public Integer getMainYear() {
        return mainYear;
    }

    public void setMainYear(Integer mainYear) {
        this.mainYear = mainYear;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public List<LabelValueBean> getBudgetMethods() {
        if (CommonUtils.isEmpty(budgetMethods)) {
            budgetMethods = new ArrayList<LabelValueBean>();
            budgetMethods.add(new LabelValueBean(AccountingConstants.FIXED_AMOUNT, AccountingConstants.FIXED_AMOUNT));
            budgetMethods.add(new LabelValueBean(AccountingConstants.SPREAD_AMOUNT, AccountingConstants.SPREAD_AMOUNT));
            budgetMethods.add(new LabelValueBean(AccountingConstants.BASE_AMOUNT_INCREASE, AccountingConstants.BASE_AMOUNT_INCREASE));
            budgetMethods.add(new LabelValueBean(AccountingConstants.BASE_PERCENT_INCREASE, AccountingConstants.BASE_PERCENT_INCREASE));
        }
        return budgetMethods;
    }

    public void setBudgetMethods(List<LabelValueBean> budgetMethods) {
        this.budgetMethods = budgetMethods;
    }

    public List<LabelValueBean> getBudgetSets() {
        if (CommonUtils.isEmpty(budgetSets)) {
            budgetSets = new ArrayList<LabelValueBean>();
            budgetSets.add(new LabelValueBean("1", "1"));
            budgetSets.add(new LabelValueBean("2", "2"));
            budgetSets.add(new LabelValueBean("3", "3"));
            budgetSets.add(new LabelValueBean("4", "4"));
        }
        return budgetSets;
    }

    public void setBudgetSets(List<LabelValueBean> budgetSets) {
        this.budgetSets = budgetSets;
    }

    public List<LabelValueBean> getYears() {
        if (CommonUtils.isEmpty(years)) {
            years = new ArrayList<LabelValueBean>();
            List<Integer> fiscalYears = new FiscalPeriodDAO().getAllFiscalYears();
            for (Integer year : fiscalYears) {
                years.add(new LabelValueBean(year.toString(), year.toString()));
            }
        }
        return years;
    }

    public void setYears(List<LabelValueBean> years) {
        this.years = years;
    }

    public List<LabelValueBean> getCurrencies() {
        if (CommonUtils.isEmpty(currencies)) {
            currencies = new ArrayList<LabelValueBean>();
            currencies.add(new LabelValueBean("USD", "USD"));
        }
        return currencies;
    }

    public void setCurrencies(List<LabelValueBean> currencies) {
        this.currencies = currencies;
    }

    public List<Budget> getBudgets()throws Exception {
        if (CommonUtils.isEmpty(budgets)) {
            this.setBudgets(ListUtils.lazyList(Budget.class));
        }
        return budgets;
    }

    public void setBudgets(List<Budget> budgets) {
        this.budgets = budgets;
    }
}
