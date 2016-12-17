<%@ page language="java"
	import="java.util.*,org.json.JSONArray,com.gp.cvst.logisoft.util.DBUtil,com.gp.cvst.logisoft.hibernate.dao.CustAddressDAO,com.gp.cvst.logisoft.domain.CustAddress"
	pageEncoding="ISO-8859-1"%>
<%
	String accNo = "";
	String functionName = null;
	if (request.getParameter("tabName") != null) {
		functionName = request.getParameter("tabName");
	}
	if (functionName == null) {
		return;
	}
	if (functionName.equals("BOOKING")) {
		if (request.getParameter("forwarder") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("0")) {
			accNo = request.getParameter("forwarder");
		}
	}
	//accNo = request.getParameter("forwarder");	
	JSONArray accountNoArray = new JSONArray();
	List accountList = null;
	if (accNo != null && !accNo.trim().equals("")) {
		CustAddressDAO batchDAO = new CustAddressDAO();
		accountList = batchDAO.findByAccountNo(null, accNo, null, "On");
		Iterator iter = accountList.iterator();
		while (iter.hasNext()) {
			CustAddress accountDetails = (CustAddress) iter.next();
			accountNoArray.put(accountDetails.getAcctNo().toString());
		}
	}
	out.println(accountNoArray.toString());
%>
