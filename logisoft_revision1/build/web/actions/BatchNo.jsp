<%@ page language="java"
	import="java.util.*,org.json.JSONArray,com.gp.cvst.logisoft.util.DBUtil,com.gp.cvst.logisoft.hibernate.dao.BatchDAO,com.gp.cvst.logisoft.domain.Batch"
	pageEncoding="ISO-8859-1"%>
<%
	String accNo = null;
	String batchDesc = "";
	String functionName = null;
	if (request.getParameter("tabName") != null) {
		functionName = request.getParameter("tabName");
	}
	if (functionName == null) {
		return;
	}

	if (functionName.equals("BATCHES")
			|| functionName.equals("JOURNAL_ENTRY")) {
			
		if (request.getParameter("batchno") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("0")) {
			accNo = request.getParameter("batchno");
		} else if (request.getParameter("batch") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("1")) {
			accNo = request.getParameter("batch");
		}
	}

	JSONArray accountNoArray = new JSONArray();
	List accountList = null;

	if (accNo != null && !accNo.trim().equals("")) {
		BatchDAO batchDAO = new BatchDAO();			
		accountList = batchDAO.findBatchsearchforDojo(accNo, batchDesc);
		Iterator iter = accountList.iterator();
		while (iter.hasNext()) {
			Batch accountDetails = (Batch) iter.next();
			accountNoArray.put(accountDetails.getBatchId().toString());
		}		
	}

	out.println(accountNoArray.toString());
%>
