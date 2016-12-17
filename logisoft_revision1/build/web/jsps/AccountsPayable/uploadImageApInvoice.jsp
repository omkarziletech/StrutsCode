<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
 <%String path = request.getContextPath(); %>
<html> 
<c:choose>
			
			      <c:when test="${uploadImage!=null}">
			     <script>
				   parent.parent.GB_hide();
				  </script>
			      </c:when>
			     <c:otherwise>
						  
		     	 </c:otherwise>
		    </c:choose>
	<head>
		<title>JSP for UploadImageForm form</title>
		<%@include file="../includes/resources.jsp" %>  
	</head>
	
	<body>
		<html:form action="/uploadImage" method="post" enctype="multipart/form-data" scope="request">
		<table class="tableBorderNew">
		<tr class="textlabels">
			<td align="right" id="fileTitle" >
			File Name
			</td>
			<td align="left" id="theFile">
				<html:file property="theFile"/> 
			</td>
		</tr>
		
		</table>
			<html:submit value="Upload" property="upload"/>
			
			<html:cancel/>
		</html:form>
	</body>
</html>

