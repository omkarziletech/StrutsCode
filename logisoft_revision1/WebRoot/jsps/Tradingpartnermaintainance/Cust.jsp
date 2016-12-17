<%@ page language="java" import="java.util.*,com.gp.cong.logisoft.domain.User" pageEncoding="ISO-8859-1"%>
<%@include file="../includes/jspVariables.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
User loginuser=new User();
String trade="";
if(session.getAttribute("loginuser")!=null)
{
loginuser=(User)session.getAttribute("loginuser");
session.setAttribute("loginuser",loginuser);
}
if(session.getAttribute("trade")!=null)
{
trade=(String)session.getAttribute("trade");
}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'Cust.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<script type="text/javascript">
self.close();

window.top.location="<%=path%>/jsps/framepage.jsp?search=<%=trade%>";
//document.tabform.location="<%=path%>/jsps/datareference/managePorts.jsp";
</script>
  </head>
  
  <body>
    This is my JSP page. <br>
  </body>
</html>
