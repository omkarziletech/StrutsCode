<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Eculine EDI Notes</title>
        <%@include file="/jsps/LCL/init.jsp" %>
        <%@include file="/jsps/includes/baseResources.jsp" %>
        <%@include file="/jsps/includes/resources.jsp" %>
        <%@include file="/jsps/includes/jspVariables.jsp" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
    </head>
    <body>
        <div id="search-results" class="head-tag font-14px">
            List of Notes&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
            <c:choose>
                <c:when test="${not empty eculineEdiForm.refNo}">
                    House B/L# <span  class="red"> ${eculineEdiForm.refNo}</span>
                </c:when>
                <c:otherwise>
                    Voyage No# <span  class="red"> ${eculineEdiForm.voyNo}</span>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Container No# <span class="red"> ${eculineEdiForm.containerNo}</span>
                </c:otherwise>
            </c:choose>
        </div><br/>
        <div style="width:99%; float:left; height:280px; overflow-y:scroll; border:1px solid #dcdcdc">
            <display:table name="${eculineEdiNotesList}" id="eculineEdiNotes" class="display-table" requestURI="/lclEculineEdi.do">
                <display:column title="">
                    <c:if test="${eculineEdiNotes.noteType eq 'auto'}"> <span style="color:red;cursor:pointer"  title="Auto Generated Notes">*</span></c:if>
                </display:column>
                <display:column title="Notes">
                    <div style="width:600px; white-space: normal">${fn:replace(eculineEdiNotes.noteDesc,'\\N','<br/>')}</div>
                </display:column>
                <display:column title="Created Date" property="updatedDatetime" format="{0,date,dd-MMM-yyyy}"></display:column>
                <display:column title="User">${eculineEdiNotes.updatedBy.loginName}</display:column>
            </display:table>
        </div>
    </body>
</html>