<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@include file="../includes/jspVariables.jsp" %>
 
<html> 
	<head>
		<title>JSP for EditCustomerConfigForm form</title>
	</head>
	<body>
		<html:form action="/editCustomerConfig" scope="request">
			<html:submit/><html:cancel/>
		</html:form>
	</body>
</html>

