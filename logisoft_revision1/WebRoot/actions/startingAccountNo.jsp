<%@ page import="org.json.JSONArray,com.gp.cvst.logisoft.util.DBUtil,com.gp.cvst.logisoft.domain.AccountDetails,com.gp.cvst.logisoft.beans.ChartOfAccountBean" %>
<%@ page import="java.util.*"%>

<%

	String accNo="";
  if(request.getParameter("acct") != null){
      accNo = request.getParameter("acct"); 
    }else if(request.getParameter("account") != null){
      accNo = request.getParameter("account"); 
    }
    
    JSONArray accountNoArray = new JSONArray();
    List accountList =null;
    if (accNo != null && !accNo.trim().equals("")) {
    	DBUtil dbUtil=new DBUtil();
    	accountList = dbUtil.getAccountNumber(accNo);
    	Iterator iter = accountList.iterator();
    	while(iter.hasNext()){
    		AccountDetails accountDetails = (AccountDetails)iter.next();
    		accountNoArray.put(accountDetails.getAccount());
		 }
    }
    out.println(accountNoArray.toString());

%>