<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String addftfRatesPath=path+"/jsps/ratemanagement/addFTF.jsp";
String addftfRatesTabDetailsPath=path+"/jsps/FTFTabs.jsp";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'FTFFrame.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  <frameset rows="60,108" cols="" framespacing="0"" frameborder="NO" border="0">
  <frame src="<%=addftfRatesPath%>" name="topFrame" id="topFrame" title="top" />
  <frame src="<%=addftfRatesTabDetailsPath%>" name="mainFrame" id="mainFrame" title="bottom" scrolling="no" />
</frameset>
  <body>
    This is my JSP page. <br>
  </body>
</html>
