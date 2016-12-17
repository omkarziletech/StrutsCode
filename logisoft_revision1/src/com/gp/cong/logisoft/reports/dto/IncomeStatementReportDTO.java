package com.gp.cong.logisoft.reports.dto;

import java.util.List;

public class IncomeStatementReportDTO {

	private List<IncomeStatementRevenueDTO> salesRevenue=null;
	private List<IncomeStatementRevenueDTO> costOfGoodsSoldRevenue=null;
	private List<IncomeStatementRevenueDTO> costOfGoodsSoldRevenueGroup2=null;

	private Double currentYearSalesRevenueTotal=0d;
	private Double previousYearSalesRevenueTotal=0d;
	
	private Double currentYearCostOfGoodsSold=0d;
	private Double previousYearCostOfGoodsSold=0d;
	private Double currentYearCostOfGoodsSoldGroup2=0d;
	private Double previousYearCostOfGoodsSoldGroup2=0d;

	private Double currentYearGrossMarginPercentage=0d;
	private Double previousYearGrossMarginPercentage=0d;
	
	private Double currentYearGrossProfit=0d;
	private Double previousYearGrossProfit=0d;
	
	private Double currentYearExpenseGroup3Sum =0d;
	private Double previousYearExpenseGroup3Sum =0d;
	
	private Double currentYearExpenseGroup4Sum =0d;
	private Double previousYearExpenseGroup4Sum =0d;
	
	private Double currentYearExpenseGroup5Sum =0d;
	private Double previousYearExpenseGroup5Sum =0d;
	
	private Double currentYearOtherOperatingExpenses =0d;
	private Double previousYearOtherOperatingExpenses =0d;
	
	private Double currentYearTotalOperatingExpenses =0d;
	private Double previousYearTotalOperatingExpenses =0d;
	
	private Double currentYearOperatingIncome =0d;
	private Double previousYearOperatingIncome =0d;
	
	private Double currentYearOperatingIncomeMargin =0d;
	private Double previousYearOperatingIncomeMargin =0d;
	
	private Double currentYearInterestPaid =0d;
	private Double previousYearInterestPaid =0d;
	private Double currentYearIncomeBeforeTaxes =0d;
	private Double previousYearIncomeBeforeTaxes =0d;
	
	private Double currentYearTaxes =0d;
	private Double previousYearTaxes =0d;
	
	private Double currentYearNetIncomeFromContinuingOperations =0d;
	private Double previousYearNetIncomeFromContinuingOperations =0d;
	
	private Double currentYearNonRecurringEvents = 0d;
	private Double previousYearNonRecurringEvents = 0d;
	
	private Double currentYearNetIncome=0d;
	private Double previousYearNetIncome=0d;
	
	private Double currentYearDividendToStockholders=0d;
	private Double previousYearDividendToStockholders=0d;
	
	private Double currentYearNetIncomeAvailableToStockholders=0d;
	private Double previousYearNetIncomeAvailableToStockholders=0d;
	
	private String beginningPeriod;
	private String endPeriod;
	private String year;
	
	private List currentYearExpenseGroup3Account;
	private List previousYearExpenseGroup3Account;
	
	private List currentYearIncomeGroup2Account;
	private List previousYearIncomeGroup2Account;
	
	private List currentYearExpenseGroup4Account;
	private List previousYearExpenseGroup4Account;
	
	private Double currentOtherIncome2Total = 0d;
	private Double currentOtherExpense4Total = 0d;
	private Double currentNetEarnings = 0d;
	private Double currentOtherIncomeAndExpenseTotal = 0d;
	
	private Double previousOtherIncome2Total = 0d;
	private Double previousOtherExpense4Total = 0d;
	private Double previousNetEarnings = 0d;
	private Double previousOtherIncomeAndExpenseTotal = 0d;
	private String companyName = "";
	private Double currentEarningOperations = 0d;
	private Double previousEarningOperations = 0d;
	
	public Double getCurrentYearIncomeBeforeTaxes() {
		return currentYearIncomeBeforeTaxes;
	}
	public void setCurrentYearIncomeBeforeTaxes(Double currentYearIncomeBeforeTaxes) {
		currentYearIncomeBeforeTaxes = currentYearIncomeBeforeTaxes == null  ? 0d : currentYearIncomeBeforeTaxes;
		this.currentYearIncomeBeforeTaxes = currentYearIncomeBeforeTaxes;
	}
	public Double getCurrentYearInterestPaid() {
		return currentYearInterestPaid;
	}
	public void setCurrentYearInterestPaid(Double currentYearInterestPaid) {
		currentYearInterestPaid = currentYearInterestPaid == null  ? 0d : currentYearInterestPaid;
		this.currentYearInterestPaid = currentYearInterestPaid;
	}
	public Double getPreviousYearIncomeBeforeTaxes() {
		return previousYearIncomeBeforeTaxes;
	}
	public void setPreviousYearIncomeBeforeTaxes(Double previousYearIncomeBeforeTaxes) {
		previousYearIncomeBeforeTaxes = previousYearIncomeBeforeTaxes == null  ? 0d : previousYearIncomeBeforeTaxes;
		this.previousYearIncomeBeforeTaxes = previousYearIncomeBeforeTaxes;
	}
	public Double getPreviousYearInterestPaid() {
		return previousYearInterestPaid;
	}
	public void setPreviousYearInterestPaid(Double previousYearInterestPaid) {
		previousYearInterestPaid = previousYearInterestPaid == null  ? 0d : previousYearInterestPaid;

		this.previousYearInterestPaid = previousYearInterestPaid;
	}
	public Double getCurrentYearGrossMarginPercentage() {
		return currentYearGrossMarginPercentage;
	}
	public void setCurrentYearGrossMarginPercentage(
			Double currentYearGrossMarginPercentage) {
		currentYearGrossMarginPercentage = currentYearGrossMarginPercentage == null  ? 0d : currentYearGrossMarginPercentage;

		this.currentYearGrossMarginPercentage = currentYearGrossMarginPercentage;
	}
	public Double getPreviousYearGrossMarginPercentage() {
		return previousYearGrossMarginPercentage;
	}
	public void setPreviousYearGrossMarginPercentage(
			Double previousYearGrossMarginPercentage) {
		previousYearGrossMarginPercentage = previousYearGrossMarginPercentage == null  ? 0d : previousYearGrossMarginPercentage;

		this.previousYearGrossMarginPercentage = previousYearGrossMarginPercentage;
	}
	public Double getCurrentYearCostOfGoodsSold() {
		return currentYearCostOfGoodsSold;
	}
	public void setCurrentYearCostOfGoodsSold(Double currentYearCostOfGoodsSold) {
		currentYearCostOfGoodsSold = currentYearCostOfGoodsSold == null  ? 0d : currentYearCostOfGoodsSold;

		this.currentYearCostOfGoodsSold = currentYearCostOfGoodsSold;
	}
	public List<IncomeStatementRevenueDTO> getCostOfGoodsSoldRevenue() {
		return costOfGoodsSoldRevenue;
	}
	public void setCostOfGoodsSoldRevenue(
			List<IncomeStatementRevenueDTO> currentYearCostOfGoodsSoldRevenue) {
		this.costOfGoodsSoldRevenue = currentYearCostOfGoodsSoldRevenue;
	}
	public List<IncomeStatementRevenueDTO> getSalesRevenue() {
		return salesRevenue;
	}
	public void setSalesRevenue(
			List<IncomeStatementRevenueDTO> currentYearSalesRevenue) {
		this.salesRevenue = currentYearSalesRevenue;
	}
	public Double getCurrentYearSalesRevenueTotal() {
		return currentYearSalesRevenueTotal;
	}
	public void setCurrentYearSalesRevenueTotal(Double currentYearSalesRevenueTotal) {
		currentYearSalesRevenueTotal = currentYearSalesRevenueTotal == null  ? 0d : currentYearSalesRevenueTotal;

		this.currentYearSalesRevenueTotal = currentYearSalesRevenueTotal;
	}
	public Double getPreviousYearCostOfGoodsSold() {
		return previousYearCostOfGoodsSold;
	}
	public void setPreviousYearCostOfGoodsSold(Double previousYearCostOfGoodsSold) {
		previousYearCostOfGoodsSold = previousYearCostOfGoodsSold == null  ? 0d : previousYearCostOfGoodsSold;

		this.previousYearCostOfGoodsSold = previousYearCostOfGoodsSold;
	}
	
	public Double getPreviousYearSalesRevenueTotal() {
		return previousYearSalesRevenueTotal;
	}
	public void setPreviousYearSalesRevenueTotal(
			Double previousYearSalesRevenueTotal) {
		previousYearSalesRevenueTotal = previousYearSalesRevenueTotal == null  ? 0d : previousYearSalesRevenueTotal;

		this.previousYearSalesRevenueTotal = previousYearSalesRevenueTotal;
	}
	public Double getCurrentYearCostOfGoodsSoldGroup2() {
		return currentYearCostOfGoodsSoldGroup2;
	}
	public void setCurrentYearCostOfGoodsSoldGroup2(
			Double currentYearCostOfGoodsSoldGroup2) {
		currentYearCostOfGoodsSoldGroup2 = currentYearCostOfGoodsSoldGroup2 == null  ? 0d : currentYearCostOfGoodsSoldGroup2;
		this.currentYearCostOfGoodsSoldGroup2 = currentYearCostOfGoodsSoldGroup2;
	}
	public List<IncomeStatementRevenueDTO> getCostOfGoodsSoldRevenueGroup2() {
		return costOfGoodsSoldRevenueGroup2;
	}
	public void setCostOfGoodsSoldRevenueGroup2(
			List<IncomeStatementRevenueDTO> currentYearCostOfGoodsSoldRevenueGroup2) {
		this.costOfGoodsSoldRevenueGroup2 = currentYearCostOfGoodsSoldRevenueGroup2;
	}
	public Double getPreviousYearCostOfGoodsSoldGroup2() {
		return previousYearCostOfGoodsSoldGroup2;
	}
	public void setPreviousYearCostOfGoodsSoldGroup2(
			Double previousYearCostOfGoodsSoldGroup2) {
		previousYearCostOfGoodsSoldGroup2 = previousYearCostOfGoodsSoldGroup2 == null  ? 0d : previousYearCostOfGoodsSoldGroup2;

		this.previousYearCostOfGoodsSoldGroup2 = previousYearCostOfGoodsSoldGroup2;
	}
	
	public Double getCurrentYearGrossProfit() {
		return currentYearGrossProfit;
	}
	public void setCurrentYearGrossProfit(Double currentYearGrossProfit) {
		currentYearGrossProfit = currentYearGrossProfit == null  ? 0d : currentYearGrossProfit;
		this.currentYearGrossProfit = currentYearGrossProfit;
	}
	public Double getPreviousYearGrossProfit() {
		return previousYearGrossProfit;
	}
	public void setPreviousYearGrossProfit(Double previousYearGrossProfit) {
		previousYearGrossProfit = previousYearGrossProfit == null  ? 0d : previousYearGrossProfit;
		this.previousYearGrossProfit = previousYearGrossProfit;
	}
	public Double getCurrentYearExpenseGroup3Sum() {
		return currentYearExpenseGroup3Sum;
	}
	public void setCurrentYearExpenseGroup3Sum(Double currentYearExpenseGroup3Sum) {
		currentYearExpenseGroup3Sum = currentYearExpenseGroup3Sum == null  ? 0d : currentYearExpenseGroup3Sum;
		this.currentYearExpenseGroup3Sum = currentYearExpenseGroup3Sum;
	}
	public Double getCurrentYearExpenseGroup4Sum() {
		return currentYearExpenseGroup4Sum;
	}
	public void setCurrentYearExpenseGroup4Sum(Double currentYearExpenseGroup4Sum) {
		currentYearExpenseGroup4Sum = currentYearExpenseGroup4Sum == null  ? 0d : currentYearExpenseGroup4Sum;
		this.currentYearExpenseGroup4Sum = currentYearExpenseGroup4Sum;
	}
	public Double getCurrentYearExpenseGroup5Sum() {
		return currentYearExpenseGroup5Sum;
	}
	public void setCurrentYearExpenseGroup5Sum(Double currentYearExpenseGroup5Sum) {
		currentYearExpenseGroup5Sum = currentYearExpenseGroup5Sum == null  ? 0d : currentYearExpenseGroup5Sum;
		this.currentYearExpenseGroup5Sum = currentYearExpenseGroup5Sum;
	}
	public Double getPreviousYearExpenseGroup3Sum() {
		return previousYearExpenseGroup3Sum;
	}
	public void setPreviousYearExpenseGroup3Sum(Double previousYearExpenseGroup3Sum) {
		previousYearExpenseGroup3Sum = previousYearExpenseGroup3Sum == null  ? 0d : previousYearExpenseGroup3Sum;

		this.previousYearExpenseGroup3Sum = previousYearExpenseGroup3Sum;
	}
	public Double getPreviousYearExpenseGroup4Sum() {
		return previousYearExpenseGroup4Sum;
	}
	public void setPreviousYearExpenseGroup4Sum(Double previousYearExpenseGroup4Sum) {
		previousYearExpenseGroup4Sum = previousYearExpenseGroup4Sum == null  ? 0d : previousYearExpenseGroup4Sum;

		this.previousYearExpenseGroup4Sum = previousYearExpenseGroup4Sum;
	}
	public Double getPreviousYearExpenseGroup5Sum() {
		return previousYearExpenseGroup5Sum;
	}
	public void setPreviousYearExpenseGroup5Sum(Double previousYearExpenseGroup5Sum) {
		previousYearExpenseGroup5Sum = previousYearExpenseGroup5Sum == null  ? 0d : previousYearExpenseGroup5Sum;
		this.previousYearExpenseGroup5Sum = previousYearExpenseGroup5Sum;
	}
	public Double getCurrentYearOtherOperatingExpenses() {
		return currentYearOtherOperatingExpenses;
	}
	public void setCurrentYearOtherOperatingExpenses(
			Double currentYearOtherOperatingExpenses) {
		currentYearOtherOperatingExpenses = currentYearOtherOperatingExpenses == null  ? 0d : currentYearOtherOperatingExpenses;
		this.currentYearOtherOperatingExpenses = currentYearOtherOperatingExpenses;
	}
	public Double getPreviousYearOtherOperatingExpenses() {
		return previousYearOtherOperatingExpenses;
	}
	public void setPreviousYearOtherOperatingExpenses(
			Double previousYearOtherOperatingExpenses) {
		previousYearOtherOperatingExpenses = previousYearOtherOperatingExpenses == null  ? 0d : previousYearOtherOperatingExpenses;
		this.previousYearOtherOperatingExpenses = previousYearOtherOperatingExpenses;
	}
	public Double getCurrentYearTotalOperatingExpenses() {
		return currentYearTotalOperatingExpenses;
	}
	public void setCurrentYearTotalOperatingExpenses(
			Double currentYearTotalOperatingExpenses) {
		currentYearTotalOperatingExpenses = currentYearTotalOperatingExpenses == null  ? 0d : currentYearTotalOperatingExpenses;
		this.currentYearTotalOperatingExpenses = currentYearTotalOperatingExpenses;
	}
	public Double getPreviousYearTotalOperatingExpenses() {
		return previousYearTotalOperatingExpenses;
	}
	public void setPreviousYearTotalOperatingExpenses(
			Double previousYearTotalOperatingExpenses) {
		previousYearTotalOperatingExpenses = previousYearTotalOperatingExpenses == null  ? 0d : previousYearTotalOperatingExpenses;
		this.previousYearTotalOperatingExpenses = previousYearTotalOperatingExpenses;
	}
	public Double getCurrentYearOperatingIncome() {
		return currentYearOperatingIncome;
	}
	public void setCurrentYearOperatingIncome(Double currentYearOperatingIncome) {
		currentYearOperatingIncome = currentYearOperatingIncome == null  ? 0d : currentYearOperatingIncome;

		this.currentYearOperatingIncome = currentYearOperatingIncome;
	}
	public Double getCurrentYearOperatingIncomeMargin() {
		return currentYearOperatingIncomeMargin;
	}
	public void setCurrentYearOperatingIncomeMargin(
			Double currentYearOperatingIncomeMargin) {
		currentYearOperatingIncomeMargin = currentYearOperatingIncomeMargin == null  ? 0d : currentYearOperatingIncomeMargin;

		this.currentYearOperatingIncomeMargin = currentYearOperatingIncomeMargin;
	}
	public Double getPreviousYearOperatingIncome() {
		return previousYearOperatingIncome;
	}
	public void setPreviousYearOperatingIncome(Double previousYearOperatingIncome) {
		previousYearOperatingIncome = previousYearOperatingIncome == null  ? 0d : previousYearOperatingIncome;

		this.previousYearOperatingIncome = previousYearOperatingIncome;
	}
	public Double getPreviousYearOperatingIncomeMargin() {
		return previousYearOperatingIncomeMargin;
	}
	public void setPreviousYearOperatingIncomeMargin(
			Double previousYearOperatingIncomeMargin) {
		previousYearOperatingIncomeMargin = previousYearOperatingIncomeMargin == null  ? 0d : previousYearOperatingIncomeMargin;
		this.previousYearOperatingIncomeMargin = previousYearOperatingIncomeMargin;
	}
	public Double getCurrentYearTaxes() {
		return currentYearTaxes;
	}
	public void setCurrentYearTaxes(Double currentYearTaxes) {
		currentYearTaxes = currentYearTaxes == null  ? 0d : currentYearTaxes;
		this.currentYearTaxes = currentYearTaxes;
	}
	public Double getPreviousYearTaxes() {
		return previousYearTaxes;
	}
	public void setPreviousYearTaxes(Double previousYearTaxes) {
		previousYearTaxes = previousYearTaxes == null  ? 0d : previousYearTaxes;

		this.previousYearTaxes = previousYearTaxes;
	}
	public Double getCurrentYearNetIncomeFromContinuingOperations() {
		return currentYearNetIncomeFromContinuingOperations;
	}
	public void setCurrentYearNetIncomeFromContinuingOperations(
			Double currentYearNetIncomeFromContinuingOperations) {
		currentYearNetIncomeFromContinuingOperations = currentYearNetIncomeFromContinuingOperations == null  ? 0d : currentYearNetIncomeFromContinuingOperations;
		this.currentYearNetIncomeFromContinuingOperations = currentYearNetIncomeFromContinuingOperations;
	}
	public Double getPreviousYearNetIncomeFromContinuingOperations() {
		return previousYearNetIncomeFromContinuingOperations;
	}
	public void setPreviousYearNetIncomeFromContinuingOperations(
			Double previousYearNetIncomeFromContinuingOperations) {
		previousYearNetIncomeFromContinuingOperations = previousYearNetIncomeFromContinuingOperations == null  ? 0d : previousYearNetIncomeFromContinuingOperations;
		this.previousYearNetIncomeFromContinuingOperations = previousYearNetIncomeFromContinuingOperations;
	}
	public Double getCurrentYearNonRecurringEvents() {
		return currentYearNonRecurringEvents;
	}
	public void setCurrentYearNonRecurringEvents(
			Double currentYearNonRecurringEvents) {
		currentYearNonRecurringEvents = currentYearNonRecurringEvents == null  ? 0d : currentYearNonRecurringEvents;
		this.currentYearNonRecurringEvents = currentYearNonRecurringEvents;
	}
	public Double getPreviousYearNonRecurringEvents() {
		return previousYearNonRecurringEvents;
	}
	public void setPreviousYearNonRecurringEvents(
			Double previousYearNonRecurringEvents) {
		previousYearNonRecurringEvents = previousYearNonRecurringEvents == null  ? 0d : previousYearNonRecurringEvents;
		this.previousYearNonRecurringEvents = previousYearNonRecurringEvents;
	}
	public Double getCurrentYearNetIncome() {
		return currentYearNetIncome;
	}
	public void setCurrentYearNetIncome(Double currentYearNetIncome) {
		currentYearNetIncome = currentYearNetIncome == null  ? 0d : currentYearNetIncome;
		this.currentYearNetIncome = currentYearNetIncome;
	}
	public Double getPreviousYearNetIncome() {
		return previousYearNetIncome;
	}
	public void setPreviousYearNetIncome(Double previousYearNetIncome) {
		previousYearNetIncome = previousYearNetIncome == null  ? 0d : previousYearNetIncome;
		this.previousYearNetIncome = previousYearNetIncome;
	}
	public Double getCurrentYearDividendToStockholders() {
		return currentYearDividendToStockholders;
	}
	public void setCurrentYearDividendToStockholders(
			Double currentYearDividendToStockholders) {
		currentYearDividendToStockholders = currentYearDividendToStockholders == null  ? 0d : currentYearDividendToStockholders;
		this.currentYearDividendToStockholders = currentYearDividendToStockholders;
	}
	public Double getCurrentYearNetIncomeAvailableToStockholders() {
		return currentYearNetIncomeAvailableToStockholders;
	}
	public void setCurrentYearNetIncomeAvailableToStockholders(
			Double currentYearNetIncomeAvailableToStockholders) {
		currentYearNetIncomeAvailableToStockholders = currentYearNetIncomeAvailableToStockholders == null  ? 0d : currentYearNetIncomeAvailableToStockholders;
		this.currentYearNetIncomeAvailableToStockholders = currentYearNetIncomeAvailableToStockholders;
	}
	public Double getPreviousYearDividendToStockholders() {
		return previousYearDividendToStockholders;
	}
	public void setPreviousYearDividendToStockholders(
			Double previousYearDividendToStockholders) {
		previousYearDividendToStockholders = previousYearDividendToStockholders == null  ? 0d : previousYearDividendToStockholders;
		this.previousYearDividendToStockholders = previousYearDividendToStockholders;
	}
	public Double getPreviousYearNetIncomeAvailableToStockholders() {
		return previousYearNetIncomeAvailableToStockholders;
	}
	public void setPreviousYearNetIncomeAvailableToStockholders(
			Double previousYearNetIncomeAvailableToStockholders) {
		previousYearNetIncomeAvailableToStockholders = previousYearNetIncomeAvailableToStockholders == null  ? 0d : previousYearNetIncomeAvailableToStockholders;
		this.previousYearNetIncomeAvailableToStockholders = previousYearNetIncomeAvailableToStockholders;
	}
	public String getBeginningPeriod() {
		return beginningPeriod;
	}
	public void setBeginningPeriod(String beginningPeriod) {

		this.beginningPeriod = beginningPeriod;
	}
	public String getEndPeriod() {
		return endPeriod;
	}
	public void setEndPeriod(String endPeriod) {
		this.endPeriod = endPeriod;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public List getCurrentYearExpenseGroup3Account() {
		return currentYearExpenseGroup3Account;
	}
	public void setCurrentYearExpenseGroup3Account(
			List currentYearExpenseGroup3Account) {
		this.currentYearExpenseGroup3Account = currentYearExpenseGroup3Account;
	}
	public List getPreviousYearExpenseGroup3Account() {
		return previousYearExpenseGroup3Account;
	}
	public void setPreviousYearExpenseGroup3Account(
			List previousYearExpenseGroup3Account) {
		this.previousYearExpenseGroup3Account = previousYearExpenseGroup3Account;
	}
	public List getCurrentYearIncomeGroup2Account() {
		return currentYearIncomeGroup2Account;
	}
	public void setCurrentYearIncomeGroup2Account(
			List currentYearIncomeGroup2Account) {
		this.currentYearIncomeGroup2Account = currentYearIncomeGroup2Account;
	}
	public List getPreviousYearIncomeGroup2Account() {
		return previousYearIncomeGroup2Account;
	}
	public void setPreviousYearIncomeGroup2Account(
			List previousYearIncomeGroup2Account) {
		this.previousYearIncomeGroup2Account = previousYearIncomeGroup2Account;
	}
	public List getCurrentYearExpenseGroup4Account() {
		return currentYearExpenseGroup4Account;
	}
	public void setCurrentYearExpenseGroup4Account(
			List currentYearExpenseGroup4Account) {
		this.currentYearExpenseGroup4Account = currentYearExpenseGroup4Account;
	}
	public List getPreviousYearExpenseGroup4Account() {
		return previousYearExpenseGroup4Account;
	}
	public void setPreviousYearExpenseGroup4Account(
			List previousYearExpenseGroup4Account) {
		this.previousYearExpenseGroup4Account = previousYearExpenseGroup4Account;
	}
	public Double getCurrentOtherIncome2Total() {
		return currentOtherIncome2Total;
	}
	public void setCurrentOtherIncome2Total(Double currentOtherIncome2Total) {
		this.currentOtherIncome2Total = currentOtherIncome2Total;
	}
	public Double getCurrentOtherExpense4Total() {
		return currentOtherExpense4Total;
	}
	public void setCurrentOtherExpense4Total(Double currentOtherExpense4Total) {
		this.currentOtherExpense4Total = currentOtherExpense4Total;
	}
	public Double getCurrentNetEarnings() {
		return currentNetEarnings;
	}
	public void setCurrentNetEarnings(Double currentNetEarnings) {
		this.currentNetEarnings = currentNetEarnings;
	}
	public Double getCurrentOtherIncomeAndExpenseTotal() {
		return currentOtherIncomeAndExpenseTotal;
	}
	public void setCurrentOtherIncomeAndExpenseTotal(
			Double currentOtherIncomeAndExpenseTotal) {
		this.currentOtherIncomeAndExpenseTotal = currentOtherIncomeAndExpenseTotal;
	}
	public Double getPreviousOtherIncome2Total() {
		return previousOtherIncome2Total;
	}
	public void setPreviousOtherIncome2Total(Double previousOtherIncome2Total) {
		this.previousOtherIncome2Total = previousOtherIncome2Total;
	}
	public Double getPreviousOtherExpense4Total() {
		return previousOtherExpense4Total;
	}
	public void setPreviousOtherExpense4Total(Double previousOtherExpense4Total) {
		this.previousOtherExpense4Total = previousOtherExpense4Total;
	}
	public Double getPreviousNetEarnings() {
		return previousNetEarnings;
	}
	public void setPreviousNetEarnings(Double previousNetEarnings) {
		this.previousNetEarnings = previousNetEarnings;
	}
	public Double getPreviousOtherIncomeAndExpenseTotal() {
		return previousOtherIncomeAndExpenseTotal;
	}
	public void setPreviousOtherIncomeAndExpenseTotal(
			Double previousOtherIncomeAndExpenseTotal) {
		this.previousOtherIncomeAndExpenseTotal = previousOtherIncomeAndExpenseTotal;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public Double getCurrentEarningOperations() {
		return currentEarningOperations;
	}
	public void setCurrentEarningOperations(Double currentEarningOperations) {
		this.currentEarningOperations = currentEarningOperations;
	}
	public Double getPreviousEarningOperations() {
		return previousEarningOperations;
	}
	public void setPreviousEarningOperations(Double previousEarningOperations) {
		this.previousEarningOperations = previousEarningOperations;
	}
	
}
