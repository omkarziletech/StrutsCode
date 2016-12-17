<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
 
<html> 
	<head>
		<title>JSP for SearchJournalEntryForm form</title>
	</head>
	<body>
		<html:form action="/searchJournalEntry" scope="request">
			<html:submit/><html:cancel/>
		</html:form>
	</body>
</html>

