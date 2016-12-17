<%@ page language="java" import="java.util.*,org.json.JSONArray,com.gp.cvst.logisoft.util.DBUtil,com.gp.cong.logisoft.domain.Item"%>
<%
String functionName = null;
if (request.getParameter("tabName") != null) {
		functionName = request.getParameter("tabName");
	}
	String itemCode=null;
	if(functionName.equals("ITEM_MASTER")){
	if (request.getParameter("itemCode") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("0")) {
			itemCode = request.getParameter("itemCode");
		}
	}
	JSONArray accountNoArray = new JSONArray();
	List accountList = null;
	if (itemCode != null && !itemCode.trim().equals("")) {
		DBUtil dbUtil = new DBUtil();
		accountList = dbUtil.getItemCode(itemCode);
		Iterator iter = accountList.iterator();
		while (iter.hasNext()) {
			Item item = (Item) iter
					.next();
			accountNoArray.put(item.getUniqueCode());
		}
	}
	out.println(accountNoArray.toString());
%>