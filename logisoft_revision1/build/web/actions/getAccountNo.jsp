<%@ page language="java"
	import="java.util.*,org.json.JSONArray,com.gp.cong.logisoft.hibernate.dao.CustomerDAO,com.gp.cong.logisoft.domain.CustomerTemp,com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO,com.gp.cong.logisoft.domain.TradingPartner"%>
<%
	String account = "";
	String name = "";
	String type = "mb";
	String functionName = null;
	String bankAccount="";
	if (request.getParameter("tabName") != null) {
		functionName = request.getParameter("tabName");
	}
	
	if (functionName == null) {
		return;
	}
	if (functionName.equals("MASTER_SEARCH_CUSTOMER")
			|| functionName.equals("SEARCH_CUSTOMER")) {
		if (request.getParameter("account") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("0")) {
			account = request.getParameter("account");
			type="mb";
		}else if (request.getParameter("account") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("1")) {
			account = request.getParameter("account");
			type="master";
		}
	}
	else if(functionName.equals("BANKACCOUNT")){
		if (request.getParameter("accountNumber") != null && request.getParameter("from") != null
					&& request.getParameter("from").equals("1")) {		 
			bankAccount = request.getParameter("accountNumber");
		}
	}else if (functionName.equals("FCL_BL_CORRECTION")
			&& request.getParameter("from")!=null 
			&& request.getParameter("from").equalsIgnoreCase("0")) {
			account = request.getParameter("accountNumber");	
	}
	JSONArray accountNoArray = new JSONArray();

	if (account != null && !account.trim().equals("")) {
	TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
		List customerList = tradingPartnerDAO.getAccountNo(account,
				name, null, type);

		try {
			for (Object object : customerList) {
				accountNoArray.put(object.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}else if (bankAccount != null && !bankAccount.trim().equals("")) {
	CustomerDAO customerDAO = new CustomerDAO();
		List customerList = customerDAO.findForManagement(bankAccount,
				name, null, type);

		Iterator iter = customerList.iterator();
		while (iter.hasNext()) {

			CustomerTemp accountDetails = (CustomerTemp) iter.next();
			accountNoArray.put(accountDetails.getAccountNo());
			
		}
	}
	
	if("false".equals(request.getParameter("isDojo"))){
            StringBuilder buffer = new StringBuilder();
            buffer.append("<UL>");
            for(int i =0; i < accountNoArray.length(); i++){
                buffer.append("<li>");
                buffer.append(accountNoArray.get(i));
                buffer.append("</li>");
       }
       buffer.append("</UL>");
       out.println(buffer.toString());
       }else{
           out.println(accountNoArray.toString());
       }
%>

