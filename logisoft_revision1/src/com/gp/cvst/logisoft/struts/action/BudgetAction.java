package com.gp.cvst.logisoft.struts.action;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cvst.logisoft.AccountingConstants;
import com.gp.cvst.logisoft.domain.AccountDetails;
import com.gp.cvst.logisoft.domain.Budget;
import com.gp.cvst.logisoft.hibernate.dao.AccountDetailsDAO;
import com.gp.cvst.logisoft.hibernate.dao.BudgetDAO;
import com.gp.cvst.logisoft.hibernate.dao.FiscalPeriodDAO;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jxl.read.biff.BiffException;


import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cvst.logisoft.struts.form.BudgetForm;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.actions.DispatchAction;

public class BudgetAction extends DispatchAction {

    public ActionForward getMainAccountBudget(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws IOException, Exception {
        BudgetForm budgetForm = (BudgetForm) form;
        String mainAccount = null != request.getParameter("mainAccount") ? request.getParameter("mainAccount") : budgetForm.getMainAccount();
        AccountDetails mainAccountDetails = new AccountDetailsDAO().findById(mainAccount);
        budgetForm.setMainAccount(mainAccountDetails.getAccount());
        budgetForm.setMainDescription(mainAccountDetails.getAcctDesc());
        Integer currentYear = null != budgetForm.getMainYear() ? budgetForm.getMainYear() : Calendar.getInstance().get(Calendar.YEAR);
        budgetForm.setMainYear(currentYear);
        String currentBudgetSet = null != budgetForm.getMainBudgetSet() ? budgetForm.getMainBudgetSet() : "1";
        budgetForm.setMainBudgetSet(currentBudgetSet);
        budgetForm.setBudgets(new BudgetDAO().getBudgetsByBudgetSet(budgetForm.getMainAccount(), budgetForm.getMainYear(),null, budgetForm.getMainBudgetSet()));
        return mapping.findForward("success");
    }

    public ActionForward getCopyAccountBudget(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws IOException, Exception {
        BudgetForm budgetForm = (BudgetForm) form;
        AccountDetails copyAccountDetails = new AccountDetailsDAO().findById(budgetForm.getCopyAccount());
        budgetForm.setCopyDescription(copyAccountDetails.getAcctDesc());
        if (CommonUtils.isEqualIgnoreCase(budgetForm.getFiscalSet(), AccountingConstants.FISCAL_SET_BUDGET)) {
            budgetForm.setBudgets(new BudgetDAO().getBudgetsByBudgetSet(budgetForm.getCopyAccount(), budgetForm.getCopyYear(),null, budgetForm.getCopyBudgetSet()));
        } else {
            budgetForm.setBudgets(new BudgetDAO().getBudgetsByActual(budgetForm.getCopyAccount(), budgetForm.getCopyYear(),null, "1"));
        }
        return mapping.findForward("success");
    }

    public ActionForward editBudget(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws IOException, Exception {
        BudgetForm budgetForm = (BudgetForm) form;
        budgetForm.setBudgets(budgetForm.getBudgets());
        return mapping.findForward("success");
    }

    public ActionForward saveBudget(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws IOException, Exception {
        BudgetForm budgetForm = (BudgetForm) form;
        List<Budget> budgets = new ArrayList<Budget>();
        for (Budget budget : budgetForm.getBudgets()) {
            if (null == budget.getId() || budget.getId() == 0) {
                budget.setId(null);
                new BudgetDAO().save(budget);
                budgets.add(budget);
            } else {
                new BudgetDAO().update(budget);
                budgets.add(budget);
            }
        }
        budgetForm.setBudgets(budgets);
        return mapping.findForward("success");
    }

    public ActionForward updateBudget(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws IOException, Exception {
        BudgetForm budgetForm = (BudgetForm) form;
        if (CommonUtils.isEqualIgnoreCase(budgetForm.getBudgetMethod(), AccountingConstants.FIXED_AMOUNT)) {
            for (Budget budget : budgetForm.getBudgets()) {
                budget.setBudgetAmount(Double.parseDouble(budgetForm.getAmount()));
            }
        } else if (CommonUtils.isEqualIgnoreCase(budgetForm.getBudgetMethod(), AccountingConstants.SPREAD_AMOUNT)) {
            Double amount = Double.parseDouble(budgetForm.getAmount());
            amount /= budgetForm.getBudgets().size();
            for (Budget budget : budgetForm.getBudgets()) {
                budget.setBudgetAmount(amount);
            }
        } else if (CommonUtils.isEqualIgnoreCase(budgetForm.getBudgetMethod(), AccountingConstants.BASE_AMOUNT_INCREASE)) {
            Double amount = Double.parseDouble(budgetForm.getAmount());
            Double increaseBy = Double.parseDouble(budgetForm.getIncreaseBy());
            Double increaseAmount = 0d;
            for (Budget budget : budgetForm.getBudgets()) {
                budget.setBudgetAmount(amount + increaseAmount);
                increaseAmount += increaseBy;
            }
        } else if (CommonUtils.isEqualIgnoreCase(budgetForm.getBudgetMethod(), AccountingConstants.BASE_PERCENT_INCREASE)) {
            Double amount = Double.parseDouble(budgetForm.getAmount());
            Double increaseBy = Double.parseDouble(budgetForm.getIncreaseBy());
            Double increasePercentage = 0d;
            for (Budget budget : budgetForm.getBudgets()) {
                budget.setBudgetAmount(amount + increasePercentage);
                increasePercentage += amount * increaseBy / 100;
            }
        }
        budgetForm.setBudgets(budgetForm.getBudgets());
        return mapping.findForward("success");
    }

    public ActionForward copyBudget(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws IOException, Exception {
        BudgetForm budgetForm = (BudgetForm) form;
        List<Budget> mainAccountBudgets = new BudgetDAO().getBudgetsByBudgetSet(budgetForm.getMainAccount(), budgetForm.getMainYear(),null, budgetForm.getMainBudgetSet());
        List<Budget> budgets = budgetForm.getBudgets();
        if (CommonUtils.isNotEmpty(mainAccountBudgets)) {
            for (Budget copyBudget : budgets) {
                for (Budget mainBudget : mainAccountBudgets) {
                    if (mainBudget.getPeriod().equals(copyBudget.getPeriod())) {
                        copyBudget.setId(mainBudget.getId());
                        copyBudget.setAccount(mainBudget.getAccount());
                        break;
                    }
                }
            }
        } else {
            for (Budget copyBudget : budgets) {
                copyBudget.setId(null);
                copyBudget.setAccount(budgetForm.getMainAccount());
            }
        }
        budgetForm.setBudgets(budgets);
        return mapping.findForward("success");
    }

    public ActionForward importBudget(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws IOException, BiffException, Exception {
        BudgetForm budgetForm = (BudgetForm) form;
        Workbook workbook = Workbook.getWorkbook(budgetForm.getBudgetSheet().getInputStream());
        Sheet sheet = workbook.getSheet(0);
        if (null != sheet) {
            if (sheet.getRows() >= 4) {
                List<Budget> budgets = new ArrayList<Budget>();
                for (int i = 4; i < sheet.getRows(); i++) {
                   Cell[] cells = sheet.getRow(i);
                   if(null!=cells){
                       String account = cells[0].getContents();
                       Integer year = Integer.parseInt(cells[1].getContents());
                       String set = cells[2].getContents();
                       String currency = cells[3].getContents();
                       Cell[] periodAmounts = (Cell[])ArrayUtils.subarray(cells, 4, 16);
                       String[] budgetSets = StringUtils.split(set.replaceAll("[^1-9,]", ""),",");
                       for(String budgetSet : budgetSets){
                           for(int period=0;period<periodAmounts.length;period++){
                               Budget budgetLoadFromSheet = new Budget(account,NumberUtils.formatNumber(period+1, "00"),Double.parseDouble(periodAmounts[period].getContents().replaceAll(",", "")),budgetSet,year);
                               List<Budget> budgetList = new BudgetDAO().getBudgetsByBudgetSet(account, year, NumberUtils.formatNumber(period+1, "00"), budgetSet);
                               if(CommonUtils.isNotEmpty(budgetList)){
                                   for(Budget budget : budgetList){
                                       budgetLoadFromSheet.setId(budget.getId());
                                       budgetLoadFromSheet.setEndDate(budget.getEndDate());
                                   }
                               }else{
                                   budgetLoadFromSheet.setEndDate(new FiscalPeriodDAO().getEndDate(year.toString(), budgetLoadFromSheet.getPeriod()));
                               }
                               budgets.add(budgetLoadFromSheet);
                           }
                       }
                   }
                }
               budgetForm.setBudgets(budgets);
            }
        }
        return mapping.findForward("success");
    }

    public ActionForward gotoChartOfAccounts(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        return mapping.findForward("chartOfAccounts");
    }
}
