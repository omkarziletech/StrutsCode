<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@include file="../includes/jspVariables.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String addExportPath=path+"/jsps/ratemanagement/addFTF.jsp";
String addVoyageTabDetailsPath=path+"/jsps/FTFTabs.jsp";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
  <head>
    <html:base />
    
    <title>voyageFrame.jsp</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  <frameset rows="50,108" cols="" framespacing="0"" frameborder="NO" border="0">
  <frame src="<%=addExportPath%>" name="topFrame" id="topFrame" title="top" />
  <frame src="<%=addVoyageTabDetailsPath%>" name="mainFrame" id="mainFrame" title="bottom" scrolling="no" />
</frameset>
  
  <body>
    This a struts page. <br>
  </body>
   <%@include file="../includes/baseResourcesForJS.jsp" %>
</html:html>
