<%@ page language="java" import="java.util.*" import="com.gp.cong.logisoft.util.DBUtil,java.util.List,java.util.ArrayList,com.gp.cong.logisoft.beans.SearchCarriersBean,com.gp.cong.logisoft.domain.CarriersOrLine,com.gp.cong.logisoft.domain.GenericCode" pageEncoding="ISO-8859-1"%>
<%@include file="../includes/jspVariables.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
DBUtil dbUtil=new DBUtil();
String customerDetailsPath=path+"/jsps/Tradingpartnermaintainance/EditCustom.jsp";
String customerTabDetailsPath=path+"/jsps/EditCustomerTabs.jsp";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'EditCustomerFrame.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
   <frameset rows="50,258" cols="" framespacing="0"" frameborder="NO" border="0">
  <frame src="<%=customerDetailsPath%>" name="topFrame" id="topFrame" title="top" scrolling="no"/>
  <frame src="<%=customerTabDetailsPath%>" name="mainFrame" id="mainFrame" title="bottom" scrolling="no" />
</frameset>
<noframes>
  <body>
    This is my JSP page. <br>
  </body>
</html>
