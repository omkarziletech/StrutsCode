package com.gp.cong.logisoft.reports.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.gp.cong.logisoft.bc.accounting.ReportConstants;
import com.gp.cvst.logisoft.hibernate.dao.AccountBalanceDAO;

public class IncomeStatementReportDTOUtils {

    private AccountBalanceDAO accountBalanceDAO = null;
    private List<IncomeStatementRevenueDTO> currentYearIncomeStatementReportDTOList = new ArrayList<IncomeStatementRevenueDTO>();
    private Double currentYearTotal = 0d;
    private Double previousYearTotal = 0d;
    String normalBalance = null;

    public IncomeStatementReportDTOUtils(AccountBalanceDAO accountBalanceDAO) {
        this.accountBalanceDAO = accountBalanceDAO;
    }

    /**
     * This is to create the accounts for the current year and previous year.
     * @param year
     * @param fromPeriod
     * @param toPeriod
     * @param period
     */
    public void execute(Integer year, Integer fromPeriod, Integer toPeriod, String period) throws Exception {// period--->group
        List<Object[]> costOfGoodsSoldObjAry = accountBalanceDAO.getAccountAndBalanceForYearPeriodAndGroup(year, fromPeriod, toPeriod, period);
        // Previous Year YTD
        List<Object[]> costOfGoodsSoldPrevYearObjAry = accountBalanceDAO.getAccountAndBalanceForYearPeriodAndGroup(year - 1, fromPeriod, toPeriod, period);
        String group = period;
        // Current Month
        List<Object[]> currentPeriodList = accountBalanceDAO.getAccountAndBalanceForYearEndPeriodAndGroup(year, fromPeriod, toPeriod, group, ReportConstants.INCOME_STATEMENT_CURRENT_PERIOD);
        // Previous Year
        List<Object[]> previousYearList = accountBalanceDAO.getAccountAndBalanceForYearEndPeriodAndGroup(year - 1, fromPeriod, toPeriod, group, ReportConstants.INCOME_STATEMENT_PREVIOUS_YEAR);
        // Budget YTD
        List<Object[]> budgetYtdList = accountBalanceDAO.getAccountAndBalanceFromBudget(year, fromPeriod, toPeriod, group, ReportConstants.INCOME_STATEMENT_BUDGET_YTD, "1");
        // Budget Year
        List<Object[]> budgetYearList = accountBalanceDAO.getAccountAndBalanceFromBudget(year, fromPeriod, toPeriod, group, ReportConstants.INCOME_STATEMENT_BUDGET_ANNUAL, "1");

        HashMap<String, Double> currentPeriodHm = new HashMap<String, Double>();
        HashMap<String, Double> previousYearHm = new HashMap<String, Double>();
        HashMap<String, Double> budgetYtdHm = new HashMap<String, Double>();
        HashMap<String, Double> budgetYearHm = new HashMap<String, Double>();

        HashMap<String, Double> costOfGoodsSoldHM = new HashMap<String, Double>();


        for (Iterator iter = costOfGoodsSoldPrevYearObjAry.iterator(); iter.hasNext();) {
            Object[] accountBalanceAry = (Object[]) iter.next();
            Double balance = 0.0;
            if (!accountBalanceAry[2].equals("Debit")) {
                balance = Double.parseDouble(accountBalanceAry[1].toString());
                balance = -1 * balance;
            } else {
                balance = new Double(accountBalanceAry[1].toString());
            }
            costOfGoodsSoldHM.put(accountBalanceAry[0].toString(), new Double(accountBalanceAry[1].toString()));
            previousYearTotal += balance;
        }
        for (Iterator iterator = currentPeriodList.iterator(); iterator.hasNext();) {
            Object[] accountBalanceAry = (Object[]) iterator.next();
            Double balance = 0.0;
            balance = new Double(accountBalanceAry[1].toString());
            currentPeriodHm.put(accountBalanceAry[0].toString(), balance);
        }
        for (Iterator iterator = previousYearList.iterator(); iterator.hasNext();) {
            Object[] accountBalanceAry = (Object[]) iterator.next();
            Double balance = 0.0;
            balance = new Double(accountBalanceAry[1].toString());
            previousYearHm.put(accountBalanceAry[0].toString(), balance);
        }
        for (Iterator iterator = budgetYtdList.iterator(); iterator.hasNext();) {
            Object[] accountBalanceAry = (Object[]) iterator.next();
            Double balance = 0.0;
            balance = new Double(accountBalanceAry[1].toString());
            budgetYtdHm.put(accountBalanceAry[0].toString(), balance);
        }
        for (Iterator iterator = budgetYearList.iterator(); iterator.hasNext();) {
            Object[] accountBalanceAry = (Object[]) iterator.next();
            Double balance = 0.0;
            balance = new Double(accountBalanceAry[1].toString());
            budgetYearHm.put(accountBalanceAry[0].toString(), balance);

        }

        for (Iterator iter = costOfGoodsSoldObjAry.iterator(); iter.hasNext();) {
            Object[] accountBalanceAry = (Object[]) iter.next();
            Double balance = 0.0;
            if (!accountBalanceAry[2].equals("Debit")) {
                balance = Double.parseDouble(accountBalanceAry[1].toString());
                balance = -1 * balance;
            } else {
                balance = new Double(accountBalanceAry[1].toString());
            }
            currentYearTotal += balance;
        }

        for (Iterator iter = costOfGoodsSoldObjAry.iterator(); iter.hasNext();) {
            Object[] accountBalanceAry = (Object[]) iter.next();
            if (costOfGoodsSoldHM.get(accountBalanceAry[0].toString()) != null) {
                Double balance = 0.0;
                if (!accountBalanceAry[2].equals("Debit")) {
                    balance = Double.parseDouble(accountBalanceAry[1].toString());
                    balance = -1 * balance;
                } else {
                    balance = new Double(accountBalanceAry[1].toString());
                }
                costOfGoodsSoldHM.remove(accountBalanceAry[0].toString());
                currentYearIncomeStatementReportDTOList.add(new IncomeStatementRevenueDTO(accountBalanceAry[0], balance, costOfGoodsSoldHM.get(accountBalanceAry[0].toString()),
                        previousYearHm.get(accountBalanceAry[0].toString()), currentPeriodHm.get(accountBalanceAry[0].toString()), budgetYtdHm.get(accountBalanceAry[0].toString()),
                        budgetYearHm.get(accountBalanceAry[0].toString())));
            //if(accountBalanceAry[0] != null && costOfGoodsSoldHM.get(accountBalanceAry[0].toString())!=null){
            //previousYearTotal += new Double(costOfGoodsSoldHM.get(accountBalanceAry[0].toString()));
            //}
            } else {
                Double balance = 0.0;
                if (!accountBalanceAry[2].equals("Debit")) {
                    balance = Double.parseDouble(accountBalanceAry[1].toString());
                    balance = -1 * balance;
                } else {
                    balance = new Double(accountBalanceAry[1].toString());
                }
                currentYearIncomeStatementReportDTOList.add(new IncomeStatementRevenueDTO(accountBalanceAry[0], balance, costOfGoodsSoldHM.get(accountBalanceAry[0].toString()),
                        previousYearHm.get(accountBalanceAry[0].toString()), currentPeriodHm.get(accountBalanceAry[0].toString()), budgetYtdHm.get(accountBalanceAry[0].toString()),
                        budgetYearHm.get(accountBalanceAry[0].toString())));
            //if(accountBalanceAry[1] != null){
            //currentYearTotal += new Double(accountBalanceAry[1].toString());
            //}
            }
        }

        for (Iterator iter = costOfGoodsSoldHM.keySet().iterator(); iter.hasNext();) {
            String account = (String) iter.next();
            currentYearIncomeStatementReportDTOList.add(new IncomeStatementRevenueDTO(account, costOfGoodsSoldHM.get(account.toString())));
        //previousYearTotal += new Double(costOfGoodsSoldHM.get(account));
        }

    }

    /**
     * @return
     */
    public List<IncomeStatementRevenueDTO> getCurrentYearIncomeStatementReportDTOList() {
        return currentYearIncomeStatementReportDTOList;
    }

    /**
     * @return
     */
    public Double getCurrentYearTotal() {
        return currentYearTotal;
    }

    /**
     * @return
     */
    public Double getPreviousYearTotal() {
        return previousYearTotal;
    }
}
