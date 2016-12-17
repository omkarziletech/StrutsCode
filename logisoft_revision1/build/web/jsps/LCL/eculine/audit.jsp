<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Eculine EDI Notes</title>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
        <%@include file="/jsps/LCL/init.jsp" %>
        <%@include file="/jsps/includes/baseResources.jsp" %>
        <%@include file="/jsps/includes/resources.jsp" %>
        <%@include file="/jsps/includes/jspVariables.jsp" %>
    </head>
    <body>
        <display:table name="${audits}" id="audit"  class="display-table" requestURI="/lclEculineEdi.do">
            <display:column title="" style="white-space: normal">
                <c:if test="${audit.noteType eq 'auto'}">
                    <span class="red" title="Auto Generated Notes">*</span>
                </c:if>
            </display:column>
            <display:column title="Description" style="white-space: normal">
                ${fn:replace(audit.noteDesc,',','<br/>')}
            </display:column>
            <display:column title="Created on" property="updatedDatetime" format="{0,date,dd-MMM-yyyy}" style="white-space: normal">
            </display:column>
            <display:column title="Created By" property="updatedBy.loginName" style="white-space: normal">
            </display:column>
        </display:table>
    </body>
</html>