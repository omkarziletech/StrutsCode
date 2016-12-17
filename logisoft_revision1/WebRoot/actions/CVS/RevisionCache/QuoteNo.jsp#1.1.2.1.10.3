<%@ page language="java"
	import="java.util.*,org.json.JSONArray,com.gp.cvst.logisoft.util.DBUtil,com.gp.cvst.logisoft.hibernate.dao.QuotationDAO,com.gp.cvst.logisoft.domain.Quotation"
	pageEncoding="ISO-8859-1"%>
<%
	String accNo = "";
	String batchDesc = "";
	String functionName = null;
	if (request.getParameter("tabName") != null) {
		functionName = request.getParameter("tabName");
	}
	if (functionName == null) {
		return;
	}
	if (functionName.equals("FCL_BL")) {
		if (request.getParameter("quote") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("0")) {
			accNo = request.getParameter("quote");
		}
	}

	JSONArray accountNoArray = new JSONArray();
	List accountList = null;
	if (accNo != null && !accNo.trim().equals("")) {
		QuotationDAO batchDAO = new QuotationDAO();
		accountList = batchDAO.getQuoteNo(accNo);
		Iterator iter = accountList.iterator();
		while (iter.hasNext()) {
			Quotation accountDetails = (Quotation) iter.next();
			accountNoArray.put(accountDetails.getQuoteNo().toString());
		}
	}
	out.println(accountNoArray.toString());
%>

