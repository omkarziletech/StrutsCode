package com.gp.cong.logisoft.bc.accounting;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.reports.FiscalPeriodPdfCreator;
import com.gp.cong.logisoft.reports.IncomeStatementCreator;
import com.gp.cong.logisoft.reports.IncomeStatementExportToExcel;
import com.gp.cong.logisoft.reports.IncomeStatementPdfCreator;
import com.gp.cong.logisoft.reports.TrialBalancePdfCreator;
import com.gp.cong.logisoft.reports.dto.IncomeStatementReportDTO;
import com.gp.cong.logisoft.reports.dto.IncomeStatementReportDTOUtils;
import com.gp.cong.logisoft.reports.dto.TrialReportDTO;
import com.gp.cvst.logisoft.domain.AccountBalance;
import com.gp.cvst.logisoft.domain.AccountDetails;
import com.gp.cvst.logisoft.domain.AccountYearEndBalance;
import com.gp.cvst.logisoft.domain.Batch;
import com.gp.cvst.logisoft.domain.Budget;
import com.gp.cvst.logisoft.domain.FiscalPeriod;
import com.gp.cvst.logisoft.domain.FiscalYear;
import com.gp.cvst.logisoft.domain.JournalEntry;
import com.gp.cvst.logisoft.domain.LineItem;
import com.gp.cvst.logisoft.hibernate.dao.AccountBalanceDAO;
import com.gp.cvst.logisoft.hibernate.dao.AccountDetailsDAO;
import com.gp.cvst.logisoft.hibernate.dao.AccountYearEndBalanceDAO;
import com.gp.cvst.logisoft.hibernate.dao.BatchDAO;
import com.gp.cvst.logisoft.hibernate.dao.BudgetDAO;
import com.gp.cvst.logisoft.hibernate.dao.FiscalPeriodDAO;
import com.gp.cvst.logisoft.hibernate.dao.FiscalYearDAO;
import com.gp.cvst.logisoft.hibernate.dao.JournalEntryDAO;
import com.gp.cvst.logisoft.hibernate.dao.LineItemDAO;
import com.gp.cvst.logisoft.reports.dto.fiscalPeriodDTO;
import com.gp.cvst.logisoft.struts.form.FiscalPeriodForm;
import com.lowagie.text.DocumentException;
import com.gp.cong.common.CommonUtils;
import com.logiware.excel.TrialBalanceExcelCreator;
import com.logiware.hibernate.dao.GlBatchDAO;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Set;
import org.json.JSONObject;

public class FiscalPeriodBC {

    private IncomeStatementReportDTO incomeStatementReportDTO = null;
    /**
     * returns the list of traildto object
     *
     * @param year
     * @param period
     * @return
     */
    EmailschedulerDAO emailschedulerDAO = new EmailschedulerDAO();

    private List<TrialReportDTO> createTrialReportDTO(Integer year, String period, String startingAccount, String endingAccount) throws Exception {
        AccountDetailsDAO accountDetailsDAO = new AccountDetailsDAO();
        AccountYearEndBalanceDAO accountYearEndBalanceDAO = new AccountYearEndBalanceDAO();
        List<TrialReportDTO> trialReportDTOList = new ArrayList<TrialReportDTO>();
        List<AccountDetails> accountDetailsList = null;
        if (!startingAccount.equals("") && !endingAccount.equals("")) {
            accountDetailsList = accountDetailsDAO.getAccountsInTheRange(startingAccount, endingAccount);
        } else {
            accountDetailsList = accountDetailsDAO.getAllAccounts();
        }
        int j = 0;
        String accounts = "";
        for (Iterator<AccountDetails> iterator = accountDetailsList.iterator(); iterator.hasNext();) {
            AccountDetails accountDetails = iterator.next();
            TrialReportDTO trialReportDTO = new TrialReportDTO();
            Integer previousYear = 0;
            String prevPeriod = "";
            previousYear = year - 1;
            if (!CommonUtils.isEqual(period, "AD") && !CommonUtils.isEqual(period, "CL")) {
                prevPeriod = "0" + period;
            }
            AccountYearEndBalance accountYearEndBalance = null;
            //Get Year End Balance
            accountYearEndBalance = accountYearEndBalanceDAO.findAccountYearEndBalanceForAccountAndYear(previousYear, accountDetails.getAccount());
            accounts = accounts + "," + "'" + accountDetails.getAccount() + "'";
            trialReportDTO.setAccountNumber(accountDetails.getAccount());
            trialReportDTO.setAccountDescription(accountDetails.getAcctDesc());
            Double debitMinusCredit = 0d;
            NumberFormat formatter = new DecimalFormat("#00");
            StringBuffer allPeriods = new StringBuffer();
            Integer periods = 12;
            if (!CommonUtils.isEqual(period, "AD") && !CommonUtils.isEqual(period, "CL")) {
                periods = Integer.parseInt(period);
            }
            for (int i = 1; i <= periods; i++) {
                allPeriods.append("'").append(formatter.format(new Integer(i).intValue())).append("',");
            }
            String periodVal = allPeriods.toString();
            if (periodVal.endsWith(",")) {
                periodVal = periodVal.substring(0, periodVal.length() - 1);
            }
            if (CommonUtils.isEqual(period, "AD")) {
                periodVal += ",'" + period + "'";
            } else if (CommonUtils.isEqual(period, "CL")) {
                periodVal += ",'AD','" + period + "'";
            }
            List<Object[]> sumCreditAndDebitList = accountYearEndBalanceDAO.getSumOfDebitAndCreditForAccountYearAndPeriods(accountDetails.getAccount(), year, periodVal);
            if (sumCreditAndDebitList != null && !sumCreditAndDebitList.isEmpty()) {
                Object[] debitCredit = (Object[]) sumCreditAndDebitList.get(0);
                if (debitCredit == null) {
                    debitCredit = new Object[]{0, 0};
                }
                if (debitCredit[0] == null) {
                    debitCredit[0] = 0;
                }
                if (debitCredit[1] == null) {
                    debitCredit[1] = 0;
                }
                debitMinusCredit = new Double(debitCredit[0].toString()) - new Double(debitCredit[1].toString());
            }
            double balance = 0d;

            if (accountYearEndBalance != null && accountYearEndBalance.getClosingBalance() != null) {
                balance = accountYearEndBalance.getClosingBalance() + debitMinusCredit;
            } else {
                balance = debitMinusCredit;
            }

            String month = "";
            if (periods != null) {
                String[] monthName = {"January", "February",
                    "March", "April", "May", "June", "July",
                    "August", "September", "October", "November",
                    "December"
                };
                month = monthName[periods - 1];
            }

            if (balance >= 0) {
                trialReportDTO.setTotalDebit(balance);
            } else {
                trialReportDTO.setTotalCredit((-1) * balance);
            }

            trialReportDTO.setYear(year.toString());
            trialReportDTO.setPeriod(month);
            trialReportDTOList.add(trialReportDTO);
            j++;
        }
        return trialReportDTOList;
    }

    public void createTrailReport(Integer year, String period, String fileName, String contextPath, String startingAccount, String endingAccount) throws Exception {
        List<TrialReportDTO> trialReportDTOList = this.createTrialReportDTO(year, period, startingAccount, endingAccount);
        new TrialBalancePdfCreator().createReport(trialReportDTOList, fileName, contextPath);
    }

    public void createIncomeStatement(Integer year, Integer fromPeriod, Integer toPeriod, String fileName, String contextPath, String buttonValue,
            WritableWorkbook writableWorkbook) throws MalformedURLException, DocumentException, IOException, Exception {
        this.setIncomeStatementReportDTO(new IncomeStatementReportDTO());
        AccountBalanceDAO accountBalanceDAO = new AccountBalanceDAO();
        IncomeStatementPdfCreator incomeStatementPdfCreator = new IncomeStatementPdfCreator();
        //Sales revenue
        //Company Name
        incomeStatementReportDTO.setCompanyName(accountBalanceDAO.getCompanyName());
        //1 1a 2 2a 3 3a
        IncomeStatementReportDTOUtils salesRevenue = new IncomeStatementReportDTOUtils(accountBalanceDAO);
        salesRevenue.execute(year, fromPeriod, toPeriod, ReportConstants.INCOMEGROUP);
        incomeStatementReportDTO.setSalesRevenue(salesRevenue.getCurrentYearIncomeStatementReportDTOList());
        incomeStatementReportDTO.setCurrentYearSalesRevenueTotal(salesRevenue.getCurrentYearTotal());
        //4 4a
        incomeStatementReportDTO.setPreviousYearSalesRevenueTotal(salesRevenue.getPreviousYearTotal());
        //Cost of Goods Sold
        //5 5a 6 6a 7 7a
        IncomeStatementReportDTOUtils costOfGoodsSold = new IncomeStatementReportDTOUtils(accountBalanceDAO);
        costOfGoodsSold.execute(year, fromPeriod, toPeriod, ReportConstants.EXPENSEGROUP1);
        incomeStatementReportDTO.setCostOfGoodsSoldRevenue(costOfGoodsSold.getCurrentYearIncomeStatementReportDTOList());
        incomeStatementReportDTO.setCurrentYearCostOfGoodsSold(costOfGoodsSold.getCurrentYearTotal());
        //8 8a
        incomeStatementReportDTO.setPreviousYearCostOfGoodsSold(costOfGoodsSold.getPreviousYearTotal());
        //Cost of Goods Sold Group2
        //9 9a 10 10a
        IncomeStatementReportDTOUtils costOfGoodsSoldGroup2 = new IncomeStatementReportDTOUtils(accountBalanceDAO);
        costOfGoodsSoldGroup2.execute(year, fromPeriod, toPeriod, ReportConstants.EXPENSEGROUP2);
        incomeStatementReportDTO.setCostOfGoodsSoldRevenueGroup2(costOfGoodsSoldGroup2.getCurrentYearIncomeStatementReportDTOList());
        //11 11a
        incomeStatementReportDTO.setCurrentYearCostOfGoodsSoldGroup2(costOfGoodsSoldGroup2.getCurrentYearTotal());
        incomeStatementReportDTO.setPreviousYearCostOfGoodsSoldGroup2(costOfGoodsSoldGroup2.getPreviousYearTotal());
        //12 12a
        incomeStatementReportDTO.setCurrentYearGrossProfit(incomeStatementReportDTO.getCurrentYearSalesRevenueTotal() - incomeStatementReportDTO.getCurrentYearCostOfGoodsSold());
        incomeStatementReportDTO.setPreviousYearGrossProfit(incomeStatementReportDTO.getPreviousYearSalesRevenueTotal() - incomeStatementReportDTO.getPreviousYearCostOfGoodsSold());
        //13 13a
        Double currentYearGrossMargin = incomeStatementReportDTO.getCurrentYearGrossProfit() * 100 / costOfGoodsSoldGroup2.getCurrentYearTotal();
        Double previousYearGrossMargin = incomeStatementReportDTO.getPreviousYearGrossProfit() * 100 / costOfGoodsSoldGroup2.getPreviousYearTotal();
        incomeStatementReportDTO.setCurrentYearGrossMarginPercentage(currentYearGrossMargin);
        incomeStatementReportDTO.setPreviousYearGrossMarginPercentage(previousYearGrossMargin);
        /*//14 14a
         incomeStatementReportDTO.setCurrentYearExpenseGroup3Sum(accountBalanceDAO.getBalanceForYearPeriodAndGroup(year,fromPeriod,toPeriod,ReportConstants.EXPENSEGROUP3));
         incomeStatementReportDTO.setPreviousYearExpenseGroup3Sum(accountBalanceDAO.getBalanceForYearPeriodAndGroup(year-1,fromPeriod,toPeriod,ReportConstants.EXPENSEGROUP3));
         */
        //14 14a
        incomeStatementReportDTO.setCurrentYearExpenseGroup3Account(accountBalanceDAO.getBalanceForYearPeriodAndGroupAccount(year, fromPeriod, toPeriod, ReportConstants.EXPENSEGROUP3));
        incomeStatementReportDTO.setPreviousYearExpenseGroup3Account(accountBalanceDAO.getBalanceForYearPeriodAndGroupAccount(year - 1, fromPeriod, toPeriod, ReportConstants.EXPENSEGROUP3));
        //14 14a sum
        incomeStatementReportDTO.setCurrentYearExpenseGroup3Sum(accountBalanceDAO.getBalanceForYearPeriodAndGroup(year, fromPeriod, toPeriod, ReportConstants.EXPENSEGROUP3));
        incomeStatementReportDTO.setPreviousYearExpenseGroup3Sum(accountBalanceDAO.getBalanceForYearPeriodAndGroup(year - 1, fromPeriod, toPeriod, ReportConstants.EXPENSEGROUP3));
        //Earnings (Loss) from Operations
        incomeStatementReportDTO.setCurrentEarningOperations(incomeStatementReportDTO.getCurrentYearGrossProfit() - incomeStatementReportDTO.getCurrentYearExpenseGroup3Sum());
        incomeStatementReportDTO.setCurrentEarningOperations(incomeStatementReportDTO.getPreviousYearGrossProfit() - incomeStatementReportDTO.getPreviousYearExpenseGroup3Sum());

        incomeStatementReportDTO.setCurrentYearExpenseGroup4Account(accountBalanceDAO.getBalanceForYearPeriodAndGroupAccount(year, fromPeriod, toPeriod, ReportConstants.EXPENSEGROUP4));
        incomeStatementReportDTO.setPreviousYearExpenseGroup4Account(accountBalanceDAO.getBalanceForYearPeriodAndGroupAccount(year - 1, fromPeriod, toPeriod, ReportConstants.EXPENSEGROUP4));

        incomeStatementReportDTO.setCurrentYearIncomeGroup2Account(accountBalanceDAO.getBalanceForYearPeriodAndGroupAccount(year, fromPeriod, toPeriod, ReportConstants.INCOMEGROUP2));
        incomeStatementReportDTO.setPreviousYearIncomeGroup2Account(accountBalanceDAO.getBalanceForYearPeriodAndGroupAccount(year - 1, fromPeriod, toPeriod, ReportConstants.INCOMEGROUP2));
        incomeStatementReportDTO.setCurrentOtherIncome2Total(accountBalanceDAO.getBalanceForYearPeriodAndGroup(year, fromPeriod, toPeriod, ReportConstants.INCOMEGROUP2));
        incomeStatementReportDTO.setCurrentOtherExpense4Total(accountBalanceDAO.getBalanceForYearPeriodAndGroup(year, fromPeriod, toPeriod, ReportConstants.EXPENSEGROUP4));
        incomeStatementReportDTO.setCurrentOtherIncomeAndExpenseTotal(
                (null != incomeStatementReportDTO.getCurrentOtherIncome2Total() ? incomeStatementReportDTO.getCurrentOtherIncome2Total() : 0d) + (null != incomeStatementReportDTO.getCurrentOtherExpense4Total() ? incomeStatementReportDTO.getCurrentOtherExpense4Total() : 0d));
        incomeStatementReportDTO.setPreviousOtherIncome2Total(accountBalanceDAO.getBalanceForYearPeriodAndGroup(year - 1, fromPeriod, toPeriod, ReportConstants.INCOMEGROUP2));
        incomeStatementReportDTO.setPreviousOtherExpense4Total(accountBalanceDAO.getBalanceForYearPeriodAndGroup(year - 1, fromPeriod, toPeriod, ReportConstants.EXPENSEGROUP4));
        incomeStatementReportDTO.setPreviousOtherIncomeAndExpenseTotal(
                (null != incomeStatementReportDTO.getPreviousOtherIncome2Total() ? incomeStatementReportDTO.getPreviousOtherIncome2Total() : 0d)
                + (null != incomeStatementReportDTO.getPreviousOtherExpense4Total() ? incomeStatementReportDTO.getPreviousOtherExpense4Total() : 0d));
        //15 15a
        incomeStatementReportDTO.setCurrentYearExpenseGroup4Sum(accountBalanceDAO.getBalanceForYearPeriodAndGroup(year, fromPeriod, toPeriod, ReportConstants.EXPENSEGROUP4));
        incomeStatementReportDTO.setPreviousYearExpenseGroup4Sum(accountBalanceDAO.getBalanceForYearPeriodAndGroup(year - 1, fromPeriod, toPeriod, ReportConstants.EXPENSEGROUP4));
        //16 16a
        incomeStatementReportDTO.setCurrentYearExpenseGroup5Sum(accountBalanceDAO.getBalanceForYearPeriodAndGroup(year, fromPeriod, toPeriod, ReportConstants.EXPENSEGROUP5));
        incomeStatementReportDTO.setPreviousYearExpenseGroup5Sum(accountBalanceDAO.getBalanceForYearPeriodAndGroup(year - 1, fromPeriod, toPeriod, ReportConstants.EXPENSEGROUP5));
        //17 17a
        incomeStatementReportDTO.setCurrentYearOtherOperatingExpenses(accountBalanceDAO.getOtherOperatingExpenses(year, fromPeriod, toPeriod, ReportConstants.OTHEREXP));
        incomeStatementReportDTO.setPreviousYearOtherOperatingExpenses(accountBalanceDAO.getOtherOperatingExpenses(year - 1, fromPeriod, toPeriod, ReportConstants.OTHEREXP));
        incomeStatementReportDTO.setCurrentEarningOperations((null != incomeStatementReportDTO.getCurrentYearGrossProfit() ? incomeStatementReportDTO.getCurrentYearGrossProfit() : 0d)
                - (null != incomeStatementReportDTO.getCurrentYearExpenseGroup3Sum() ? incomeStatementReportDTO.getCurrentYearExpenseGroup3Sum() : 0d));
        incomeStatementReportDTO.setPreviousEarningOperations((null != incomeStatementReportDTO.getPreviousYearGrossProfit() ? incomeStatementReportDTO.getPreviousYearGrossProfit() : 0d)
                - (null != incomeStatementReportDTO.getPreviousYearExpenseGroup3Sum() ? incomeStatementReportDTO.getPreviousYearExpenseGroup3Sum() : 0d));
        //18 18a
        //Double currentYearSum = incomeStatementReportDTO.getCurrentYearExpenseGroup3Sum()+incomeStatementReportDTO.getCurrentYearExpenseGroup4Sum()+incomeStatementReportDTO.getCurrentYearExpenseGroup5Sum()+incomeStatementReportDTO.getCurrentYearOtherOperatingExpenses();
        Double currentYearSum = incomeStatementReportDTO.getCurrentYearGrossProfit() - incomeStatementReportDTO.getCurrentYearOtherOperatingExpenses();
        //Double previousYearSum = incomeStatementReportDTO.getPreviousYearExpenseGroup3Sum()+incomeStatementReportDTO.getPreviousYearExpenseGroup4Sum()+incomeStatementReportDTO.getPreviousYearExpenseGroup5Sum()+incomeStatementReportDTO.getPreviousYearOtherOperatingExpenses();
        Double previousYearSum = incomeStatementReportDTO.getPreviousYearGrossProfit() - incomeStatementReportDTO.getPreviousYearOtherOperatingExpenses();
        incomeStatementReportDTO.setCurrentYearTotalOperatingExpenses(currentYearSum);
        incomeStatementReportDTO.setPreviousYearTotalOperatingExpenses(previousYearSum);

        incomeStatementReportDTO.setCurrentNetEarnings(
                (null != incomeStatementReportDTO.getCurrentEarningOperations() ? incomeStatementReportDTO.getCurrentEarningOperations() : 0d) + incomeStatementReportDTO.getCurrentOtherIncomeAndExpenseTotal());

        incomeStatementReportDTO.setPreviousNetEarnings(
                (null != incomeStatementReportDTO.getPreviousEarningOperations() ? incomeStatementReportDTO.getPreviousEarningOperations() : 0d) + incomeStatementReportDTO.getPreviousOtherIncomeAndExpenseTotal());

        Double currentYearOperatingIncome = incomeStatementReportDTO.getCurrentYearGrossProfit() - incomeStatementReportDTO.getCurrentYearTotalOperatingExpenses();
        Double previousYearOperatingIncome = incomeStatementReportDTO.getPreviousYearGrossProfit() - incomeStatementReportDTO.getPreviousYearTotalOperatingExpenses();
        Double currentYearOperatingMargin = (currentYearOperatingIncome * 100) / incomeStatementReportDTO.getCurrentYearGrossProfit();
        Double previousYearOperatingMargin = (previousYearOperatingIncome * 100) / incomeStatementReportDTO.getPreviousYearGrossProfit();
        //19 19a
        incomeStatementReportDTO.setCurrentYearOperatingIncome(currentYearOperatingIncome);
        incomeStatementReportDTO.setPreviousYearOperatingIncome(previousYearOperatingIncome);
        //20 20a
        incomeStatementReportDTO.setCurrentYearOperatingIncomeMargin(currentYearOperatingMargin);
        incomeStatementReportDTO.setPreviousYearOperatingIncomeMargin(previousYearOperatingMargin);
        //21 21a
        incomeStatementReportDTO.setCurrentYearInterestPaid(accountBalanceDAO.getBalanceForYearPeriodAndGroup(year, fromPeriod, toPeriod, ReportConstants.INTERESTPAID));
        incomeStatementReportDTO.setPreviousYearInterestPaid(accountBalanceDAO.getBalanceForYearPeriodAndGroup(year - 1, fromPeriod, toPeriod, ReportConstants.INTERESTPAID));
        //22 22a
        incomeStatementReportDTO.setCurrentYearIncomeBeforeTaxes(incomeStatementReportDTO.getCurrentYearOperatingIncome() - incomeStatementReportDTO.getCurrentYearInterestPaid());
        incomeStatementReportDTO.setPreviousYearIncomeBeforeTaxes(incomeStatementReportDTO.getPreviousYearOperatingIncome() - incomeStatementReportDTO.getPreviousYearInterestPaid());
        //23 23a
        incomeStatementReportDTO.setCurrentYearTaxes(accountBalanceDAO.getBalanceForYearPeriodAndGroup(year, fromPeriod, toPeriod, ReportConstants.TAXES));
        incomeStatementReportDTO.setPreviousYearTaxes(accountBalanceDAO.getBalanceForYearPeriodAndGroup(year - 1, fromPeriod, toPeriod, ReportConstants.TAXES));
        //24 24a
        incomeStatementReportDTO.setCurrentYearNetIncomeFromContinuingOperations(incomeStatementReportDTO.getCurrentYearIncomeBeforeTaxes() - incomeStatementReportDTO.getCurrentYearTaxes());
        incomeStatementReportDTO.setPreviousYearNetIncomeFromContinuingOperations(incomeStatementReportDTO.getPreviousYearIncomeBeforeTaxes() - incomeStatementReportDTO.getPreviousYearTaxes());
        //25 25a
        //to get the requirement
        //26 26a
        incomeStatementReportDTO.setCurrentYearNonRecurringEvents(accountBalanceDAO.getBalanceForYearPeriodAndGroup(year, fromPeriod, toPeriod, ReportConstants.NONRECURRING));
        incomeStatementReportDTO.setPreviousYearNonRecurringEvents(accountBalanceDAO.getBalanceForYearPeriodAndGroup(year - 1, fromPeriod, toPeriod, ReportConstants.NONRECURRING));
        //27 27a
        incomeStatementReportDTO.setCurrentYearNetIncome(incomeStatementReportDTO.getCurrentYearNetIncomeFromContinuingOperations() - incomeStatementReportDTO.getCurrentYearNonRecurringEvents());
        incomeStatementReportDTO.setPreviousYearNetIncome(incomeStatementReportDTO.getPreviousYearNetIncomeFromContinuingOperations() - incomeStatementReportDTO.getPreviousYearNonRecurringEvents());
        //28 28a
        incomeStatementReportDTO.setCurrentYearTaxes(accountBalanceDAO.getBalanceForYearPeriodAndGroup(year, fromPeriod, toPeriod, ReportConstants.DIVIDEND));
        incomeStatementReportDTO.setPreviousYearTaxes(accountBalanceDAO.getBalanceForYearPeriodAndGroup(year - 1, fromPeriod, toPeriod, ReportConstants.DIVIDEND));
        //29 29a
        incomeStatementReportDTO.setCurrentYearNetIncomeAvailableToStockholders(incomeStatementReportDTO.getCurrentYearNetIncome() - incomeStatementReportDTO.getCurrentYearTaxes());
        incomeStatementReportDTO.setPreviousYearNetIncomeAvailableToStockholders(incomeStatementReportDTO.getPreviousYearNetIncome() - incomeStatementReportDTO.getPreviousYearTaxes());
        String beginningMonth = "";
        String endMonth = "";
        if (fromPeriod != null && toPeriod != null) {
            String[] monthName = {"January", "February",
                "March", "April", "May", "June", "July",
                "August", "September", "October", "November",
                "December"
            };
            beginningMonth = monthName[fromPeriod - 1];
            endMonth = monthName[toPeriod - 1];
        }
        incomeStatementReportDTO.setBeginningPeriod(beginningMonth);
        incomeStatementReportDTO.setEndPeriod(endMonth);
        incomeStatementReportDTO.setYear(year.toString());
        if (buttonValue.equals("IncomeStatementReport")) {
            incomeStatementPdfCreator.createReport(incomeStatementReportDTO, fileName, contextPath);
        } else {
            IncomeStatementExportToExcel incomeStatementExportToExcel = new IncomeStatementExportToExcel();
            WritableSheet writableSheet = incomeStatementExportToExcel.getExcelData(writableWorkbook, incomeStatementReportDTO);
        }
    }

    public IncomeStatementReportDTO getIncomeStatementReportDTO() {
        return incomeStatementReportDTO;
    }

    public void setIncomeStatementReportDTO(IncomeStatementReportDTO incomeStatementReportDTO) {
        this.incomeStatementReportDTO = incomeStatementReportDTO;
    }

    public void createFiscalPeriodReport(String year, String outPutFileName, String realPath) throws Exception {
        FiscalPeriodPdfCreator fiscalPeriodPdfCreator = new FiscalPeriodPdfCreator();
        fiscalPeriodDTO periodDTO = new fiscalPeriodDTO();
        FiscalPeriodDAO fiscalPeriodDAO = new FiscalPeriodDAO();
        List periodList = (List) fiscalPeriodDAO.getPeriodList1(year);
        periodDTO.setFileName(outPutFileName);
        periodDTO.setYear(year);
        periodDTO.setRealPath(realPath);
        periodDTO.setPeriodList(periodList);
        fiscalPeriodPdfCreator.createReport(periodDTO);
    }
    // setting budget in budget table.....

    public String setBudget(FiscalPeriodForm fiscalPeriodForm) throws Exception {
        AccountDetailsDAO accountDetailsDAO = new AccountDetailsDAO();
        String result = null;
        BudgetDAO budgetDAO = new BudgetDAO();
        FiscalPeriodDAO fiscalPeriodDAO = new FiscalPeriodDAO();
        List<AccountDetails> accountDetailsList = accountDetailsDAO.getAllAccounts();
        List periodList = (List) fiscalPeriodDAO.getPeriodList1(fiscalPeriodForm.getYear());
        boolean flag = budgetDAO.checkBudgetSet(fiscalPeriodForm.getYear(), fiscalPeriodForm.getCopybudgetset());
        if (!flag) {
            for (int i = 0; i < accountDetailsList.size(); i++) {
                AccountDetails accountDetails = accountDetailsList.get(i);
                for (int j = 0; j < periodList.size(); j++) {
                    Budget budget = new Budget();
                    fiscalPeriodDTO fisPerioddto = (fiscalPeriodDTO) periodList.get(j);
                    if (fisPerioddto.getPeriod() != null && !fisPerioddto.getPeriod().equalsIgnoreCase("AD")) {
                        if (!fisPerioddto.getPeriod().equalsIgnoreCase("CL")) {

                            budget.setAccount(accountDetails.getAccount());
                            budget.setBudgetAmount(0.0);
                            budget.setBudgetSet(fiscalPeriodForm.getCopybudgetset());
                            budget.setPeriod(fisPerioddto.getPeriod());
                            if (fisPerioddto.getYear() != null && !fisPerioddto.getYear().equals("")) {
                                budget.setYear(new Integer(fisPerioddto.getYear()));
                            }
                            budgetDAO.save(budget);
                        }
                    }
                }
            }
        } else {
            result = "Budget already Set for this year....";
        }
        return result;
    }

    public String setBudgetForExcelReport(FiscalPeriodForm fiscalPeriodForm, List periodList) throws Exception {
        AccountDetailsDAO accountDetailsDAO = new AccountDetailsDAO();
        String result = null;
        BudgetDAO budgetDAO = new BudgetDAO();
        List<AccountDetails> accountDetailsList = accountDetailsDAO.getAllAccounts();
        boolean flag = budgetDAO.checkBudgetSet(fiscalPeriodForm.getYear(), fiscalPeriodForm.getCopybudgetset());
        if (!flag) {
            for (int i = 0; i < accountDetailsList.size(); i++) {
                AccountDetails accountDetails = accountDetailsList.get(i);
                for (int j = 0; j < periodList.size(); j++) {
                    Budget budget = new Budget();
                    fiscalPeriodDTO fisPerioddto = (fiscalPeriodDTO) periodList.get(j);
                    if (fisPerioddto.getPeriod() != null && !fisPerioddto.getPeriod().equalsIgnoreCase("AD")) {
                        if (!fisPerioddto.getPeriod().equalsIgnoreCase("CL")) {
                            budget.setAccount(accountDetails.getAccount());
                            if (fisPerioddto.getAmount() != null && !fisPerioddto.getAmount().equals("")) {
                                String amount = fisPerioddto.getAmount();
                                if (fisPerioddto.getAmount().equals(",")) {
                                    amount = amount.replace(",", "");
                                }
                                budget.setBudgetAmount(new Double(amount));
                            }
                            budget.setBudgetSet(fiscalPeriodForm.getCopybudgetset());
                            budget.setPeriod(fisPerioddto.getPeriod());
                            if (fisPerioddto.getYear() != null && !fisPerioddto.getYear().equals("")) {
                                budget.setYear(new Integer(fisPerioddto.getYear()));
                            }
                            budgetDAO.save(budget);
                        }
                    }
                }

            }
        } else {
            result = "Budget already Set for this year....";
        }
        return result;
    }

    public String retrieveValuesFromExcel(File file, FiscalPeriodForm fiscalPeriodForm) throws Exception {
        String result = null;
        FiscalPeriodDAO fiscalPeriodDAO = new FiscalPeriodDAO();
        List periodList = fiscalPeriodDAO.getPeriodList1(fiscalPeriodForm.getYear());
        Workbook workbook = null;
        Sheet sheet = null;
        workbook = Workbook.getWorkbook(file);
        Sheet sheets[] = workbook.getSheets();
        int perodList = 0;
        for (int i = 0; i < sheets.length; i++) {
            sheet = workbook.getSheet(i);
            int rows = sheet.getRows();
            int cols = sheet.getColumns();
            for (int r = 0; r < rows; r++) {
                List periodLIst = new ArrayList();
                for (int c = 0; c < cols; c++) {

                    if (r == 4 && c > 4 && c < 17) {
                        if (perodList < 12 && sheet.getCell(c, r).getContents() != null && !sheet.getCell(c, r).getContents().equals("")) {
                            fiscalPeriodDTO fisPerioddto = (fiscalPeriodDTO) periodList.get(perodList);
                            if (fisPerioddto.getPeriod() != null && !fisPerioddto.getPeriod().equalsIgnoreCase("AD")) {
                                if (!fisPerioddto.getPeriod().equalsIgnoreCase("CL")) {
                                    fisPerioddto.setAmount(sheet.getCell(c, r).getContents().replace(",", ""));
                                    periodLIst.add(fisPerioddto);
                                    perodList++;
                                }
                            }

                        }
                    }
                }// column ended
                if (periodLIst.size() > 11) {
                    result = setBudgetForExcelReport(fiscalPeriodForm, periodLIst);
                }

            }
        }
        return result;
    }

    public String getFiscalYearStatus(String year) throws Exception {
        FiscalYearDAO fiscalYearDAO = new FiscalYearDAO();
        return fiscalYearDAO.getFiscalYearStatus(year);
    }

    public void unlockFiscalPeriod(String id) throws Exception {
        FiscalPeriodDAO fiscalPeriodDAO = new FiscalPeriodDAO();
        fiscalPeriodDAO.unlockFiscalPeriod(id);
    }

    public Batch completeYearClose(String year) throws Exception {
        FiscalPeriodDAO fiscalPeriodDAO = new FiscalPeriodDAO();
        AccountDetailsDAO accountDetailsDAO = new AccountDetailsDAO();
        BatchDAO batchDAO = new BatchDAO();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        JournalEntryDAO journalEntryDAO = new JournalEntryDAO();
        LineItemDAO lineItemDAO = new LineItemDAO();
        final String DESCREPTION = "Year Closing for the year " + year;

        //get all the accounts who have acct_type = "Income Statement"
        List accounts = fiscalPeriodDAO.getAllAccountAndBalancesByAccountTypeAndYear(new Integer(year), "Income Statement");

        //get the total sum accounts who have acct_type = "Income Statement"
        Double amount = fiscalPeriodDAO.getAllAccountBalancesByAccountTypeAndYear(new Integer(year), "Income Statement");

        //Create a JE Batch for the year closing
        Batch jeBatch = new Batch();
        jeBatch.setBatchDesc(DESCREPTION);
        List genericCodeList = genericCodeDAO.findByProperty("code", "GL-JE");
        GenericCode genericCode = null;
        if (!genericCodeList.isEmpty() && genericCodeList.size() > 0) {
            genericCode = (GenericCode) genericCodeList.get(0);
            jeBatch.setSourceLedger(genericCode);
        }
        jeBatch.setType("auto");
        jeBatch.setTotalCredit(amount);
        jeBatch.setTotalDebit(amount);
        jeBatch.setStatus(CommonConstants.STATUS_OPEN);
        jeBatch = batchDAO.saveAndReturn(jeBatch);
        Integer batchId = jeBatch.getBatchId();

        //Create JE Journal Entry for the year closing
        String jeid = batchId + "-" + "001";
        JournalEntry journalentry = new JournalEntry();
        journalentry.setJournalEntryId(jeid);
        journalentry.setJournalEntryDesc(DESCREPTION);
        journalentry.setBatchId(batchId);
        journalentry.setJeDate(new Date());
        String period = year + "CL";
        journalentry.setPeriod(period);
        journalentry.setCredit(amount);
        journalentry.setDebit(amount);
        journalentry.setMemo(DESCREPTION);
        if (null != genericCode) {
            journalentry.setSourceCode(genericCode);
            journalentry.setSourceCodeDesc(genericCode.getCodedesc());
        }
        journalEntryDAO.save(journalentry);

        //Create TWO line item entries for the each of the accounts
        int count = 0;
        NumberFormat formatter = new DecimalFormat("#000");
        LineItem lineItem = null;
        //get current fiscal period
        FiscalPeriod fos = null;
        List fiscalPeriodList = fiscalPeriodDAO.findByProperty("periodDis", year + "CL");
        if (!fiscalPeriodList.isEmpty() && fiscalPeriodList.size() > 0) {
            fos = (FiscalPeriod) fiscalPeriodList.get(0);
        }
        for (Object account : accounts) {
            Object[] jeAccount = (Object[]) account;
            Double amt = (Double) jeAccount[1];
            lineItem = new LineItem();
            lineItem.setLineItemId(jeid + "-" + formatter.format(++count));
            lineItem.setJournalEntryId(jeid);
            lineItem.setReferenceDesc(DESCREPTION);
            lineItem.setPeriod(fos);
            if (null != jeAccount[0]) {
                lineItem.setAccount(jeAccount[0].toString());
            }

            //If the amt had negative value then fill in DEBIT amount otherwise CREDIT amount
            if (amt <= 0) {
                lineItem.setDebit(-(amt));
                lineItem.setCredit(new Double(0));
            } else {
                lineItem.setCredit(amt);
                lineItem.setDebit(new Double(0));
            }
            lineItemDAO.save(lineItem);

            //Make an identical OPPOSITE entry to �Close To Account� for the ACCOUNT in the line_item table
            lineItem = new LineItem();
            lineItem.setLineItemId(jeid + "-" + formatter.format(++count));
            lineItem.setJournalEntryId(jeid);
            lineItem.setReferenceDesc(DESCREPTION);
            lineItem.setPeriod(fos);

            //get the close_to_acct for the ACCOUNT
            AccountDetails accountDetails = accountDetailsDAO.getClosetoAcctList(jeAccount[0].toString());
            String closingAccountNo = "";
            if (accountDetails != null && accountDetails.getAccount() != null) {
                closingAccountNo = accountDetails.getCloseToAcct();
                lineItem.setAccount(closingAccountNo);
            }

            lineItem.setAccount(closingAccountNo);

            //If the amt had negative value then fill in DEBIT amount otherwise CREDIT amount
            if (amt >= 0) {
                lineItem.setDebit(amt);
                lineItem.setCredit(new Double(0));
            } else {
                lineItem.setCredit(-(amt));
                lineItem.setDebit(new Double(0));
            }
            lineItemDAO.save(lineItem);
        }

        return jeBatch;
    }

    public void closeFiscalYear(String year) throws Exception {
        FiscalPeriodDAO fiscalPeriodDAO = new FiscalPeriodDAO();
        FiscalYearDAO fiscalYearDAO = new FiscalYearDAO();
        //close the status of fisical year
        List list = fiscalYearDAO.findByProperty("year", new Integer(year));
        if (list != null && !list.isEmpty()) {
            FiscalYear fiscalYear = (FiscalYear) list.get(0);
            fiscalYear.setActive("Close");
            fiscalYearDAO.update(fiscalYear);
        }

        //update all fiscal perod and set the loked as 'Y'
		  /*In other words we would be �locking� all the periods 
         * and NO MORE JE postings possible from that time onwards.
         */
        List<FiscalPeriod> fiscalPeriods = fiscalPeriodDAO.findByProperty("year", new Integer(year));

        if (null != fiscalPeriods) {

            for (FiscalPeriod fiscalPeriod : fiscalPeriods) {
                fiscalPeriod.setLocked(CommonConstants.YES);
                fiscalPeriodDAO.update(fiscalPeriod);
            }

        }
    }

    public String postBatchAndCloseYear(String batchId, Integer year) throws Exception {
        Batch batch = new GlBatchDAO().findById(Integer.parseInt(batchId));
        if (null == batch || null == batch.getJournalEntrySet() || batch.getJournalEntrySet().isEmpty()) {
            return "Cannot post Batch - contains no line items";
        } else {
            FiscalPeriodDAO fiscalPeriodDAO = new FiscalPeriodDAO();
            AccountBalanceDAO accountBalanceDAO = new AccountBalanceDAO();
            for (JournalEntry journalEntry : (Set<JournalEntry>) batch.getJournalEntrySet()) {
                if (null == journalEntry.getLineItemSet() || journalEntry.getLineItemSet().isEmpty()) {
                    return "Cannot post Batch - contains no line items";
                } else {
                    FiscalPeriod fiscalPeriod = fiscalPeriodDAO.getFiscalPeriod(journalEntry.getPeriod());
                    for (LineItem lineItem : (Set<LineItem>) journalEntry.getLineItemSet()) {
                        AccountBalance accountBalance = accountBalanceDAO.getAccountBalance(lineItem.getAccount(), fiscalPeriod.getYear().toString(), fiscalPeriod.getPeriod());
                        accountBalance.setToatlCredit(accountBalance.getToatlCredit() + lineItem.getCredit());
                        accountBalance.setTotalDebit(accountBalance.getTotalDebit() + lineItem.getDebit());
                        accountBalance.setPeriodBalance(accountBalance.getTotalDebit() - accountBalance.getToatlCredit());
                    }
                }
            }
            batch.setStatus("posted");
            batch.setReadyToPost("yes");
        }
        new AccountYearEndBalanceDAO().setYearEndBalance(year);
        new FiscalYearDAO().closeFiscalYearAndItsPeriods(year);
        return "Year - " + year + " closed successfully";
    }

    public String createIncomeStatement(String fileName, String contextPath, String fromPeriod, String toPeriod, Integer year, String budgetSet) throws Exception {
        return new IncomeStatementCreator().createStatement(fileName, contextPath, fromPeriod, toPeriod, year, budgetSet);
    }

    public void save(EmailSchedulerVO emailSchedulerVO) throws Exception {
        emailschedulerDAO.save(emailSchedulerVO);
    }

    public String getStartDateAndEndDate(Integer id) throws Exception {
        JSONObject jo = new JSONObject();
        FiscalPeriod fiscalPeriod = new FiscalPeriodDAO().findById(id);
        if (null != fiscalPeriod) {
            jo.put("startDate", DateUtils.formatDate(fiscalPeriod.getStartDate(), "MM/dd/yyyy"));
            jo.put("endDate", DateUtils.formatDate(fiscalPeriod.getEndDate(), "MM/dd/yyyy"));
        }
        return jo.toString();
    }

    public Long getNumberOfDays(String fromDateId, String toDateId) throws Exception {
        FiscalPeriod startPeriod = new FiscalPeriodDAO().findById(Integer.parseInt(fromDateId));
        FiscalPeriod endPeriod = new FiscalPeriodDAO().findById(Integer.parseInt(toDateId));
        if (null != startPeriod && null != endPeriod) {
            Calendar start = Calendar.getInstance();
            start.setTime(startPeriod.getStartDate());
            Calendar end = Calendar.getInstance();
            end.setTime(endPeriod.getEndDate());
            return (((end.getTimeInMillis() - start.getTimeInMillis()) / (24 * 60 * 60 * 1000)) + 1);
        }
        return 0l;
    }

    public boolean validatePeriod(Integer id) throws Exception {
        FiscalPeriod fiscalPeriod = new FiscalPeriodDAO().findById(id);
        if (null != fiscalPeriod && CommonUtils.isEqualIgnoreCase(fiscalPeriod.getStatus(), CommonConstants.STATUS_OPEN)) {
            return true;
        }
        return false;
    }

    public boolean checkSubledgersPendingForYearClosing(Integer year) throws Exception {
        return new FiscalPeriodDAO().checkSubledgersPendingForYearClosing(year);
    }

    public String validateInvoiceDate(Date invoiceDate) throws Exception {
        String startDate = DateUtils.formatDate(invoiceDate, "yyyy-MM-dd H:mm");
        String fiscalStatus = "Close";
        for (int i = 0; i < 36; i++) {
            fiscalStatus = new FiscalPeriodDAO().getStatusByStartandEndDate(startDate, startDate);
            GregorianCalendar cal = new GregorianCalendar();
            Calendar current = Calendar.getInstance();
            cal.setTime(DateUtils.parseDate(startDate, "yyyy-MM-dd H:mm"));
            if (null != fiscalStatus && fiscalStatus.trim().equalsIgnoreCase(CommonConstants.STATUS_OPEN)) {
                if (i > 0) {
                    cal.set(Calendar.DAY_OF_MONTH, 1);
                    invoiceDate = cal.getTime();
                }
                break;
            }
            cal.add(Calendar.MONTH, 1);
            if (cal.get(Calendar.YEAR) == current.get(Calendar.YEAR) + 3) {
                break;
            }
            startDate = DateUtils.formatDate(cal.getTime(), "yyyy-MM-dd H:mm");
        }
        return startDate;
    }
}
