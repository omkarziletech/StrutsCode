<%@ page
	import="org.json.JSONArray,com.gp.cong.logisoft.util.DBUtil,java.util.*"%>
<%@page import="com.gp.cvst.logisoft.hibernate.dao.TransactionDAO"%>
<%@page import="com.gp.cong.common.CommonConstants"%>

<%
	JSONArray checkNumberArray = new JSONArray();
	List checkNumberList = null;
	String checkNumber = "";
	String accountNumber = "%";
	String tabName = request.getParameter("tabName");
	if(null != tabName && tabName.equals("APINQUIRY")){
		accountNumber = request.getParameter("vendorNo");
		checkNumber = request.getParameter("chequeNo");
	}
	if(null == checkNumber && checkNumber.equals("null")) {
		checkNumber = "";
	}
	if(null == accountNumber && accountNumber.equals("null")) {
		accountNumber = "%";
	}
	if (accountNumber != null) {
		TransactionDAO transactionDAO = new TransactionDAO();
		if(null != tabName && tabName.equals("APINQUIRY")){
			checkNumberList = transactionDAO.getCheckNumberByAccountNumberAndStatus(CommonConstants.STATUS_PAID,accountNumber,checkNumber);
		}
		if(null != checkNumberList) {
			Iterator iter = checkNumberList.iterator();
			while (iter.hasNext()) {
				String checkNo = (String) iter.next();
				checkNumberArray.put(checkNo);
			}
		}
	}
	out.println(checkNumberArray.toString());
	
%>