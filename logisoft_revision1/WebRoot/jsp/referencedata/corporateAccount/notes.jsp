<%-- 
    Document   : notes
    Created on : Oct 5, 2015, 12:58:58 PM
    Author     : Mei
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="../../../jsps/LCL/init.jsp" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Notes</title>
    </head>
    <body>
        <table class="table" style="margin: 0;overflow: hidden">
            <tr class="tableHeadingNew"><td colspan="5">Notes</td></tr>
            <tr>

            </tr>
            <tr>
                <td colspan="3">
                    <table class="dataTable">
                        <thead>
                            <tr>
                                <th></th>
                                <th>Notes</th>
                                <th>Created Date</th>
                                <th>User</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="notes" items="${notesList}">
                                <c:choose>
                                    <c:when test="${zebra eq 'odd'}">
                                        <c:set var="zebra" value="even"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="zebra" value="odd"/>
                                    </c:otherwise>
                                </c:choose>
                                <tr class="${zebra}" style="border-color:white;">
                                    <td>
                                        <img alt="auto Notes" title="Auto Generated Notes" style="vertical-align: middle"
                                             src="${path}/img/icons/redAsterisk.png"/>
                                    </td>
                                    <td><div style="width:300px; white-space: normal">${notes.noteDesc}</div></td>
                                    <td>${notes.updateDate}</td>
                                    <td>${notes.updatedBy}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </td>
            </tr>
        </table>

    </body>
</html>
