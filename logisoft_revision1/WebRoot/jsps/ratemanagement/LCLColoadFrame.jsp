<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String addlclColoadRatesPath=path+"/jsps/ratemanagement/AddLclCoload.jsp";
String addlclColoadRatesTabDetailsPath=path+"/jsps/LCLColoadTabs.jsp";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'LCLColoadFrame.jsp' starting page</title>
	<%@include file="../includes/baseResources.jsp" %>


  </head>
  <frameset rows="50,108" cols="" framespacing="0"" frameborder="NO" border="0">
  <frame src="<%=addlclColoadRatesPath%>" name="topFrame" id="topFrame" title="top" class="tableBorderNew" style="border-bottom:0px;" />
  <frame src="<%=addlclColoadRatesTabDetailsPath%>" name="mainFrame" id="mainFrame" title="bottom" scrolling="no" class="tableBorderNew"/>
</frameset>
  <body>
    This is my JSP page. <br>
  </body>
  <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>
