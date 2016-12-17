<%@ page language="java"
	import="java.util.*,org.json.JSONArray,com.gp.cvst.logisoft.domain.ArBatch,com.gp.cvst.logisoft.hibernate.dao.ArBatchDAO"
	pageEncoding="ISO-8859-1"%>
<%@page import="com.gp.cvst.logisoft.hibernate.dao.ArBatchDAO"%>
<%
	String batchNo = null;
	String functionName = null;

	if (request.getParameter("tabName") != null) {
		functionName = request.getParameter("tabName");
	}
	if (functionName == null) {
		return;
	}
	if (functionName.equals("AR_BATCH")) {
		if (request.getParameter("batchNo") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("0")) {
			batchNo = (String) request.getParameter("batchNo");

		}
	}

	JSONArray batchNoArray = new JSONArray();	
	if (batchNo != null && !batchNo.trim().equals("")) {

		ArBatchDAO arBatchDAO = new ArBatchDAO();
		List list = arBatchDAO.getBatchNumberList(batchNo);	
		Iterator iter = list.iterator();
		try {
			while (iter.hasNext()) {
				ArBatch arBatch = (ArBatch) iter.next();
				if (arBatch != null) {
					batchNoArray.put(arBatch.getBatchId().toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	out.println(batchNoArray.toString());
%>

