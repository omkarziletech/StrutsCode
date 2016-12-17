<%-- 
    Document   : search
    Created on : Jun 2, 2014, 4:28:49 PM
    Author     : Lakshmi Narayanan
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<c:choose>
    <c:when test="${param.accessMode eq 0}">
	<c:set var="accessMode" value="0" scope="request"/>
	<c:set var="writeMode" value="false" scope="request"/>
    </c:when>
    <c:otherwise>
	<c:set var="accessMode" value="1" scope="request"/>
	<c:set var="writeMode" value="true" scope="request"/>
    </c:otherwise>
</c:choose>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
        <script type="text/javascript" src="${path}/js/lightbox/lightbox.v2.3.jquery.js"></script>
        <script type="text/javascript" src="${path}/js/Accounting/GeneralLedger/chartOfAccounts.js"></script>
        <title>Chart Of Accounts</title>
    </head>
    <body>
        <%@include file="../../../../../jsps/preloader.jsp"%>
        <html:form action="/chartOfAccounts?accessMode=${accessMode}" type="com.logiware.accounting.form.ChartOfAccountsForm"
                   name="chartOfAccountsForm" styleId="chartOfAccountsForm" scope="request" method="post">
            <c:choose>
                <c:when test="${chartOfAccountsForm.toggled}">
                    <c:set var="toggleStyle" value="block" scope="request"/>
                </c:when>
                <c:otherwise>
                    <c:set var="toggleStyle" value="none" scope="request"/>
                </c:otherwise>
            </c:choose>
            <jsp:include page="hiddenFields.jsp"/>
            <table class="table" style="margin: 0">
                <tr>
                    <th class="toggle" onclick="toggle('filter-container');">Chart Of Accounts</th>
                </tr>
                <tr>
                    <td>
                        <jsp:include page="filters.jsp"/>
                    </td>
                </tr>
                <tr>
                    <th>List Of Accounts</th>
                </tr>
                <tr>
                    <td>
                        <jsp:include page="results.jsp"/>
                    </td>
                </tr>
            </table>
        </html:form>
    </body>
</html>
