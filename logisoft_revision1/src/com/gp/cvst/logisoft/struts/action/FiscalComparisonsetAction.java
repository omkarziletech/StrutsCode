/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cvst.logisoft.struts.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cvst.logisoft.domain.AccountDetails;
import com.gp.cvst.logisoft.hibernate.dao.AccountBalanceDAO;
import com.gp.cvst.logisoft.hibernate.dao.AccountDetailsDAO;
import com.gp.cvst.logisoft.hibernate.dao.BudgetDAO;
import com.gp.cvst.logisoft.struts.form.FiscalComarisonsetForm;

/** 
 * MyEclipse Struts
 * Creation date: 03-11-2008
 * 
 * XDoclet definition:
 * @struts.action input="/jsps/Accounting/FiscalComparisonset.jsp" validate="true"
 */
public class FiscalComparisonsetAction extends Action {
	/*
	 * Generated Methods
	 */

	/** 
	 * Method execute
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		
		
		FiscalComarisonsetForm fcform=(FiscalComarisonsetForm)form;
		String account=fcform.getAccount();
		String desc=fcform.getDesc();
		String amount=fcform.getAmount();
		String fiscalset1=fcform.getFiscalset1();
		String fiscalset2=fcform.getFiscalset2();
		String year1=fcform.getYear1();
		String year2=fcform.getYear2();
		String currency=fcform.getCurrency();
		String currencytype=fcform.getCurrencytype();
		String bs1=fcform.getBudgetset1();
		String bs2=fcform.getBudgetset2();
		String buttonValue=fcform.getButtonValue();
		String forwardName = null;
		if(fiscalset1.equals("Actuals")) {
			bs1=null;
		}
		if(fiscalset2.equals("Actuals")) {
			bs2=null;
		}
		 
		List comparisondetails=null;
		if( buttonValue.equals("go")) {
			if(amount.equals("NetAmt")) {
			 if(fiscalset1.equals("Actuals")&& fiscalset2.equals("Actuals")) {
			  AccountBalanceDAO abdao=new AccountBalanceDAO();
			  comparisondetails=abdao.findforSearch1(account,year1,year2);
			  request.setAttribute("comparisondetails", comparisondetails);
			 } else if(fiscalset1.equals("Budget")&& fiscalset2.equals("Budget")){
				BudgetDAO bdao=new BudgetDAO();
				comparisondetails=bdao.findforBudget(account,year1,year2,bs1,bs2);
				request.setAttribute("comparisondetails", comparisondetails);
				request.setAttribute("fiscalset1", fiscalset1);
			 }else {
		    	BudgetDAO bdao=new BudgetDAO();
				if(bs1!=null) {
				 comparisondetails=bdao.findforBudgetforSet1(account,year1,year2,bs1);
				} 
				if(bs2!=null) {
					comparisondetails=bdao.findforBudgetforSet2(account,year1,year2,bs2);
				}
				request.setAttribute("comparisondetails", comparisondetails);
		     }
			} else {
				 if(fiscalset1.equals("Actuals")&& fiscalset2.equals("Actuals")) {
				  AccountBalanceDAO abdao=new AccountBalanceDAO();
				  comparisondetails=abdao.findforSearch11(account,year1,year2);
				  request.setAttribute("comparisondetails", comparisondetails);
				 }else if(fiscalset1.equals("Budget")&& fiscalset2.equals("Budget")) {
					 	BudgetDAO bdao=new BudgetDAO();
						comparisondetails=bdao.findforBudget2(account,year1,year2,bs1,bs2);
						request.setAttribute("comparisondetails", comparisondetails);
						request.setAttribute("fiscalset1", fiscalset1);
				 } else {
			    	BudgetDAO bdao=new BudgetDAO();
					if(bs1!=null) {
					 comparisondetails=bdao.findforBudgetforSet11(account,year1,year2,bs1);
					} 
					if(bs2!=null) {
						comparisondetails=bdao.findforBudgetforSet21(account,year1,year2,bs2);
					}
					request.setAttribute("comparisondetails", comparisondetails);
			     }
				
			}
			 request.setAttribute("fiscalset1", fiscalset1);
			 request.setAttribute("fiscalset2", fiscalset2);
			 request.setAttribute("amount", amount);
			 request.setAttribute("year1", year1);
			 request.setAttribute("year2", year2);
			 forwardName="success";
		 }else if( buttonValue.equals("loadFiscalComparisionSet")) {
			 AccountDetailsDAO accountDetailsDAO=new AccountDetailsDAO();
			 String accountNo = request.getParameter("accountNo");
			 List accountDetailsList = accountDetailsDAO.findAccoutNo(accountNo);
			 AccountDetails accountDetails = null;
			 if(!accountDetailsList.isEmpty()){
				 accountDetails = (AccountDetails)accountDetailsList.get(0);
				 BudgetDAO bdao=new BudgetDAO();
				 Date d= new Date();
				 int y= d.getYear();
				 y=y+1900;
				 comparisondetails=bdao.findforBudgetforSet2(accountDetails.getAccount(),String.valueOf(y),String.valueOf(y),"1");
				 List aclist=new ArrayList();
				 aclist.add(accountDetails.getAccount());
				 aclist.add(accountDetails.getAcctDesc());
				 request.getSession(true).setAttribute("aclist", aclist);
				 request.setAttribute("year1",String.valueOf(y));
				 request.setAttribute("year2",String.valueOf(y));
			 }
			 
			  
			   
			   
			   request.setAttribute("comparisondetails", comparisondetails);
			   request.setAttribute("fiscalset1","Actuals");
			   request.setAttribute("fiscalset2","Budget");
			  
			   forwardName="success";
		 }
		// TODO Auto-generated method stub
		 return mapping.findForward(forwardName);
		 }
}