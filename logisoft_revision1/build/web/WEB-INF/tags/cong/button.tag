<%--
    Document   : sun
    Created on : Jun 4, 2010, 8:09:44 PM
    Author     : sunil
--%>
<%@taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@attribute name="label"%>
<%@attribute name="styleClass"%>
<%@attribute name="onclick"%>
<%@attribute name="confirmMessage"%>
<%@attribute name="pageChangeFunction"%>

<c:choose>
    <c:when test="${not empty confirmMessage}">
        <c:set  var="confirmClass" value="confirm"/>
    </c:when>
    <c:otherwise>
        <c:set  var="confirmClass" value=""/>
    </c:otherwise>
</c:choose>

<c:choose>
    <c:when test="${not empty pageChangeFunction}">
        <input type="button" value="${label}" onclick='checkPageChange(${pageChangeFunction})' class="${styleClass} ${confirmClass}" message="${confirmMessage}"/>
    </c:when>
    <c:when test="${not empty onclick }">
        <input type="button" value="${label}" onclick="${onclick}" class="${styleClass} ${confirmClass}" message="${confirmMessage}"/>
    </c:when>
    <c:otherwise>
        <input type="button" value="${label}" class="${styleClass} ${confirmClass}" message="${confirmMessage}"/>
    </c:otherwise>
</c:choose>
