<%@ page
	import="org.json.JSONArray,com.gp.cvst.logisoft.util.DBUtil,com.gp.cvst.logisoft.domain.AccountDetails,com.gp.cvst.logisoft.beans.ChartOfAccountBean"%>
<%@ page import="java.util.*"%>
<%@page import="org.apache.commons.lang3.math.NumberUtils"%>

<%
	List lineItemList = new ArrayList();
	if (session.getAttribute("lineItemList") != null) {
		lineItemList = (List) session.getAttribute("lineItemList");
	}

	String accNo = "";
	String index = "";
	String functionName = null;
	if (request.getParameter("tabName") != null) {
		functionName = request.getParameter("tabName");
	}
	if (functionName == null) {
		return;
	}
	if (functionName.equals("JOURNAL_ENTRY")) {
		if (request.getParameter("index") != null) {
			index = request.getParameter("index");
			accNo = request.getParameter("dispaccount" + index);

		}
	}else if(functionName.equals("AR_APPLY_PAYMENTS") || functionName.equals("ADJUSTMENT")
	                    || functionName.equals("SUBLEDGER")) {
	     if(request.getParameter("index")!= null){
	        index = request.getParameter("index");
	        accNo = request.getParameter("chargeCode"+index);
	     }
	     if(null != request.getParameter("slindex")){ //SUBLEDGER
	        index = request.getParameter("slindex");
	        accNo = request.getParameter("glAccountNo"+index);
	        
	     }	 
	} else if (functionName.equals("ACCOUNT_HISTORY")
			|| functionName.equals("BUDGET")
			|| functionName.equals("FISCAL_COMPARISON")) {
		if (request.getParameter("accountNo") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("0")) {
			accNo = request.getParameter("accountNo");
		}else if (request.getParameter("copyaccount") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("1")) {
			accNo = request.getParameter("copyaccount");
		}
	} else if (functionName.equals("AP_INVOICE")
			|| functionName.equals("AR_INVOICE_GENERATOR")||functionName.equals("BANKACCOUNT")) {
		if (request.getParameter("glAccount") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("0")) {
			accNo = request.getParameter("glAccount");
		}else if(request.getParameter("glAccountNumber")!= null && request.getParameter("from")!= null
				&& request.getParameter("from").equals("0")){
			accNo = request.getParameter("glAccountNumber");	
		}
	} else if (functionName.equals("COA")) {
		if (request.getParameter("acct1") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("0")) {
			accNo = request.getParameter("acct1");
		} else if (request.getParameter("acct") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("1")) {
			accNo = request.getParameter("acct");
		}
	} else if (functionName.equals("GLMAPPING")) {
		if (request.getParameter("glAccount") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("0")) {
			accNo = request.getParameter("glAccount");
		} else if (request.getParameter("endglAccount") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("1")) {
			accNo = request.getParameter("endglAccount");
		}
	}else if (functionName.equals("TRANSACTION_HISTORY")) {
		if (request.getParameter("account1") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("0")) {
			accNo = request.getParameter("account1");
		} else if (request.getParameter("account") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("1")) {
			accNo = request.getParameter("account");
		}
	}else if (functionName.equals("AR_NEW_BATCH")) {
	    if (request.getParameter("GLAccount") != null){
	      accNo = request.getParameter("GLAccount");
	    }
	}else if(functionName.equals("ACCOUNT_RANGE") ){
	    if(request.getParameter("startingAccount")!=null && request.getParameter("from")!=null 
	        && request.getParameter("from").equals("0")){
	         accNo = request.getParameter("startingAccount");
	     } 
	      if(request.getParameter("endingAccount")!=null && request.getParameter("from")!=null 
	        && request.getParameter("from").equals("1")){
	         accNo = request.getParameter("endingAccount");
	     } 
	}
	
	//this is to add '-' to the account no format : ##-####-##
	String output = accNo;
	accNo = accNo.replaceAll("-", "");
	if(NumberUtils.isNumber(accNo)){
		if (accNo.length() < 3) {
			output = accNo;
		} else if (accNo.length() < 7) {
			output = accNo.substring(0, 2) + "-"
					+ accNo.substring(2, accNo.length());
		} else if (accNo.length() < 9) {
			output = accNo.substring(0, 2) + "-" + accNo.substring(2, 6)
					+ "-" + accNo.substring(6, accNo.length());
		}
	accNo = output;
	}
	JSONArray accountNoArray = new JSONArray();
	List accountList = null;
	if (accNo != null && !accNo.trim().equals("")) {
		DBUtil dbUtil = new DBUtil();
		accountList = dbUtil.getAccountNumber(accNo);
		Iterator iter = accountList.iterator();
		while (iter.hasNext()) {
			AccountDetails accountDetails = (AccountDetails) iter
					.next();
			accountNoArray.put(accountDetails.getAccount() + ":-"
					+ accountDetails.getAcctDesc());
		}
	}
	out.println(accountNoArray.toString());
%>