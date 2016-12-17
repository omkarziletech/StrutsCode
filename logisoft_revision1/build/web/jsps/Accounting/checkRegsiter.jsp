<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@include file="../includes/jspVariables.jsp" %>
<html> 
	
	
	<head>
		<title>JSP for CheckRegsiterForm form</title>
	</head>
	<body>
		<html:form action="/checkRegsiter" scope="request">
			<html:submit/><html:cancel/>
		</html:form>
	</body>
</html>

