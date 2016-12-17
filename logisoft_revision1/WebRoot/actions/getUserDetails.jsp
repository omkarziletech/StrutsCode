<%@ page language="java"
	import="java.util.*,org.json.JSONArray,com.gp.cong.logisoft.hibernate.dao.*,com.gp.cong.logisoft.domain.*;"
	pageEncoding="ISO-8859-1"%>

<%
	String loginName = "";
	String lastName = "";
	String functionName = null;
	String searchByLoginName = "";
	
	if (request.getParameter("tabName") != null) {
		functionName = request.getParameter("tabName");
	}
	if (functionName == null) {
		return;
	}
	if (functionName.equals("WAREHOUSE")) {
		if (request.getParameter("managerName") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("0")) {
			loginName = request.getParameter("managerName");
		}

	} else if (functionName.equals("SEARCH_FILE")) {
		if (request.getParameter("quoteBy") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("0")) {
			searchByLoginName = request.getParameter("quoteBy");
		} else if (request.getParameter("bookedBy") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("1")) {
			searchByLoginName = request.getParameter("bookedBy");
		}
	}else if (functionName.equals("FCLBLCORRECTION")) {
		if (request.getParameter("createdBy") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("0")) {
			searchByLoginName = request.getParameter("createdBy");
		} else if (request.getParameter("approvedBy") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("1")) {
			searchByLoginName = request.getParameter("approvedBy");
		}
	}

	JSONArray accountNoArray = new JSONArray();
	if (loginName != null && !loginName.trim().equals("")) {
		UserDAO userDAO = new UserDAO();
		List userList = (List) userDAO.findLoginName1(loginName,
				lastName);
		Iterator iter = userList.iterator();
		while (iter.hasNext()) {
			User user = (User) iter.next();
			accountNoArray.put(user.getFirstName().concat(
					" " + user.getLastName()));
		}
	} else if (searchByLoginName != null
			&& !searchByLoginName.trim().equals("")) {
		UserDAO userDAO = new UserDAO();
		List userList = (List) userDAO
				.findLoginName2(searchByLoginName);
		Iterator iter = userList.iterator();
		while (iter.hasNext()) {
			User user = (User) iter.next();
			accountNoArray.put(user.getLoginName());
		}
	}
	if ("false".equals(request.getParameter("isDojo"))) {
		StringBuilder buffer = new StringBuilder("<UL>");
		for (int i = 0; i < accountNoArray.length(); i++) {
			buffer.append("<li>");
			buffer.append(accountNoArray.get(i));
			buffer.append("</li>");
		}
		buffer.append("</UL>");
		out.println(buffer.toString());
	} else {
		out.println(accountNoArray.toString());
	}	
%>
