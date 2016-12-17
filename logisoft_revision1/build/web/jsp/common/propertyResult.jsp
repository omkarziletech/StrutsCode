<%-- 
    Document   : propertiesResult
    Created on : 3 Oct, 2013, 2:54:34 PM
    Author     : Balaji.E
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Properties Result</title>
    </head>
    <body>
        <div class="result-container">
            <table width="100%" cellpadding="0" cellspacing="1" class="display-table">
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Description</th>
                        <th>Value</th>
                        <th style="width:100px">Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:set var="zebra" value="odd"/>
                    <c:forEach var="property" items="${properties}">
                        <tr class="${zebra} align-center">
                            <td class="align-left">${fn:escapeXml(property.name)}</td>
                            <td class="align-left">${fn:escapeXml(property.description)}</td>
                            <td class="align-left">
                                <input type="text" style="font-weight:bold;color: black;font-size: 11px;border: 0px solid #DDDDDD;width:750px"
                                       value="${property.value}" id="propertyTextValue${property.id}" maxlength="150" size="100"/>
                            </td>
                            <td style="width:100px">
                                <input type="hidden" id="id${property.id}" value="${property.id}"/>
                                <img title="Save Changes" src="${path}/images/icons/save.png" onclick="save('${property.id}');"/>
                            </td>
                        </tr>
                        <c:choose>
                        <c:when test="${zebra eq 'odd'}">
                            <c:set var="zebra" value="even"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="zebra" value="odd"/>
                        </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </body>
</html>
