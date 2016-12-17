<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@include file="includes/jspVariables.jsp" %>
<% 
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

HttpSession sess = ((HttpServletRequest)request).getSession();
sess.invalidate();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'SessionExpired.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  <body>
	<br>
	
			<table width="100%" align=center align=top border=0>
				<tr align=center>
					<td class="successmessage">Authentication Not Found. Please Login and Try Accessing.</td>
				</tr>	
			</table>
			<table width="100%" align=center align=top border=0>
				<tr align=center>
					<td><a href="<%=request.getContextPath()%>/jsps/login.jsp" target="_top">Back to Login Page</a></td>
				</tr>	
			</table>
	</body>
  
  <body>
    This is my JSP page. <br>
  </body>
</html>
