package com.gp.cong.logisoft.reports.dto;

public class IncomeStatementRevenueDTO {

	private String accountNumber;
	private Double amount;
	private Double prevYearAmount;
	private String normalBalance;
	private Double currentPeriod;
	private Double prevYearYTDAmount;
	private Double budgetYtd;
	private Double budgetAnnual;
	
	public IncomeStatementRevenueDTO(){
		
	}
	
	public IncomeStatementRevenueDTO(String accountNumber,Double amount,String normalBalance){
		this.accountNumber = accountNumber;
		this.amount = amount;
		this.normalBalance = normalBalance;
	}
	
	public IncomeStatementRevenueDTO(Object accountNumber,Object amount){
		this.accountNumber = accountNumber.toString();
		if(amount!=null){
			this.amount = new Double(amount.toString());
		}
	}
	
	public IncomeStatementRevenueDTO(String accountNumber,Double amount, Double prevYearAmount){
		this.accountNumber = accountNumber;
		this.amount = amount;
		this.prevYearAmount = prevYearAmount;
	}
	
	public IncomeStatementRevenueDTO(Object accountNumber,Object amount, Object prevYearAmount){
		this.accountNumber = accountNumber.toString();
		if(amount!=null){
			this.amount = new Double(amount.toString());
		}
	
		if(prevYearAmount!=null){
			this.prevYearAmount = new Double(prevYearAmount.toString());
		}
	
		
		
	}
	public IncomeStatementRevenueDTO(Object accountNumber,Object amount, Object prevYearAmount, Object preYear,
			Object currPeriodAmt ,Object budgetYtdAmt,	Object budgetYearAmt){
		this.accountNumber = accountNumber.toString();
		if(amount!=null){
			this.amount = new Double(amount.toString());
		}
		if(prevYearAmount!=null){
			this.prevYearAmount = new Double(prevYearAmount.toString());
		}
	    if(null != preYear){
	    	this.prevYearYTDAmount = new Double(preYear.toString());
	    }
		if(null != currPeriodAmt){
			this.currentPeriod = new Double(currPeriodAmt.toString());
		}
		if(null != budgetYtdAmt){
			this.budgetYtd = new Double(budgetYtdAmt.toString());
		}
		if(null != budgetYearAmt){
			this.budgetAnnual = new Double(budgetYearAmt.toString());
		}
		
	}


	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getPrevYearAmount() {
		return prevYearAmount;
	}

	public void setPrevYearAmount(Double prevYearAmount) {
		this.prevYearAmount = prevYearAmount;
	}

	public String getNormalBalance() {
		return normalBalance;
	}

	public void setNormalBalance(String normalBalance) {
		this.normalBalance = normalBalance;
	}
}
