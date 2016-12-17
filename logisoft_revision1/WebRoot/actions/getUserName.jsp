<%@ page language="java"
	import="java.util.*,org.json.JSONArray,com.gp.cong.logisoft.hibernate.dao.CustomerDAO,com.gp.cong.logisoft.domain.CustomerTemp"%>
<%@page import="com.gp.cong.logisoft.hibernate.dao.ItemDAO"%>
<%@page import="com.gp.cong.logisoft.domain.Item,com.gp.cong.logisoft.domain.User"%>
<%@page import="com.gp.cong.logisoft.hibernate.dao.UserDAO"%>
<%@page import="com.gp.cong.struts.LoadLogisoftProperties"%>
<%@page import="com.gp.cong.common.CommonConstants"%>
<%
	String userName = null; 
	String roleName = null;
	String type = null;
	type = request.getParameter("type");
	if (request.getParameter("userName") != null) {
		userName = request.getParameter("userName");
	}else if(request.getParameter("apSpecialist") != null) {
		userName = request.getParameter("apSpecialist");
	}else if(request.getParameter("fromUser") != null && "fromUser".equals(type)) {
		userName = request.getParameter("fromUser");
	}else if(request.getParameter("toUser") != null && "toUser".equals(type)) {
		userName = request.getParameter("toUser");
	}
	if (request.getParameter("roleName") != null) {
		roleName = request.getParameter("roleName");
	}
	JSONArray accountNoArray = new JSONArray();
	if(userName != null && !userName.trim().equals("") && roleName != null && !roleName.trim().equals("")) {
		UserDAO userDAO = new UserDAO();
		List<User> moduleList = userDAO.findUserByNameAndRole(userName,roleName);
		Iterator iter = moduleList.iterator();
		while (iter.hasNext()) {
			User user = (User) iter.next();
			accountNoArray.put(user.getLoginName());
		}
	}else if (userName != null && !userName.trim().equals("")) {
		UserDAO userDAO = new UserDAO();
		List<User> moduleList = userDAO.findByUserName(userName);
		Iterator iter = moduleList.iterator();
		while (iter.hasNext()) {
			User user = (User) iter.next();
			if(null != request.getParameter("apSpecialist")) {
				if(null!=user.getRole() && !user.getRole().equals("")){
					LoadLogisoftProperties loadLogisoftProperties = new LoadLogisoftProperties();
					if(null!=user.getRole().getRoleDesc() 
					&& user.getRole().getRoleDesc().equalsIgnoreCase(CommonConstants.ROLE_NAME_APSPECIALIST)){
							accountNoArray.put(user.getLoginName()
								+ ":-   "+user.getRole().getRoleDesc());
					}
				}
			}
			else{
				accountNoArray.put(user.getLoginName());
			}
		}
	}
	out.println(accountNoArray.toString());
%>

