<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@include file="../includes/jspVariables.jsp" %>
<%
String path = request.getContextPath();
String loginName=(String)request.getAttribute("loginName");
String password=(String)request.getAttribute("password");

 %>
<html> 
	<head>
	
		<title>JSP for TestUserForm form</title>
		<script language="javascript" src="<%=path%>/js/common.js" ></script>
		 <script>
		 function save1()
		 {
		 document.testUserForm.submit();
		 }
		 </script>
	</head>
	<body>
		<html:form action="/testUser" scope="request">
		<table>
		<tr>
		<td>Login Name</td>
		<td><html:text property="loginName" value="<%=loginName%>"></html:text></td>
		<td>Password</td>
		<td><html:text property="password" value="<%=password%>"></html:text></td>
		 <td width="95"><img src="<%=path%>/img/save.gif" border="0" onclick="save1()"/></td>
		</tr>
		</table>
			
		</html:form>
	</body>
</html>

