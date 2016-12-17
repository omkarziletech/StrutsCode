<%@ page
	import="org.json.JSONArray,com.gp.cong.logisoft.util.DBUtil,java.util.*"%>
<%@page import="com.gp.cvst.logisoft.hibernate.dao.TransactionDAO"%>
<%@page import="com.gp.cong.common.CommonConstants"%>

<%
	JSONArray referenceNumberArray = new JSONArray();
	List referenceNumberList = null;
	String referenceNumber = "";
	String accountNumber = "%";
	String tabName = request.getParameter("tabName");
	if(null != tabName && tabName.equals("APINQUIRY")){
		accountNumber = request.getParameter("vendorNo");
		referenceNumber = request.getParameter("referenceNumber");
	}
	if(null == referenceNumber && referenceNumber.equals("null")) {
		referenceNumber = "";
	}
	if(null == accountNumber && accountNumber.equals("null")) {
		accountNumber = "%";
	}
	if (accountNumber != null) {
		TransactionDAO transactionDAO = new TransactionDAO();
		if(null != tabName && tabName.equals("APINQUIRY")){
			referenceNumberList = transactionDAO.getReferenceNumberByAccountNumberAndStatus("",accountNumber,referenceNumber);
		}
		if(null != referenceNumberList) {
			Iterator iter = referenceNumberList.iterator();
			while (iter.hasNext()) {
				String referenceNo = (String) iter.next();
				referenceNumberArray.put(referenceNo);
			}
		}
	}
	out.println(referenceNumberArray.toString());
	
%>