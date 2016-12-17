<%@ page language="java"
	import="java.util.*,org.json.JSONArray,com.gp.cong.logisoft.hibernate.dao.CustomerDAO,com.gp.cong.logisoft.domain.CustomerTemp"%>
<%@page import="com.gp.cong.logisoft.hibernate.dao.ItemDAO"%>
<%@page import="com.gp.cong.logisoft.domain.Item"%>
<%
	String functionName = null; 	
	if (request.getParameter("moduleName") != null) {
		functionName = request.getParameter("moduleName");
	}
	JSONArray accountNoArray = new JSONArray();
   
	if (functionName != null && !functionName.trim().equals("")) {
		ItemDAO itemDAO = new ItemDAO();
		List<Item> moduleList = itemDAO.findModuleName(functionName);
		Iterator iter = moduleList.iterator();
		while (iter.hasNext()) {
			Item item = (Item) iter.next();
			accountNoArray.put(item.getItemDesc());
		}
	}
	out.println(accountNoArray.toString());
%>

