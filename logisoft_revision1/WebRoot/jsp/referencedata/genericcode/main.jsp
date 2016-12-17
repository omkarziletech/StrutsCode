<%-- 
    Document   : main
    Created on : Apr 17, 2014, 5:51:14 PM
    Author     : Lakshmi Narayanan
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/js/lightbox/lightbox.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.switch.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.util.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
        <script type="text/javascript" src="${path}/js/lightbox/lightbox.v2.3.jquery.js"></script>
        <script type="text/javascript" src="${path}/js/referencedata/genericCode.js"></script>
        <title>Generic Code</title>
    </head>
    <body>
        <%@include file="../../../jsps/preloader.jsp"%>
        <html:form action="/genericCode" type="com.logiware.referencedata.form.GenericCodeForm"
                   name="genericCodeForm" styleId="genericCodeForm" scope="request" method="post">
            <jsp:include page="hidden.jsp"/>
            <table class="table" style="margin: 0">
                <tr>
                    <td>
                        <jsp:include page="header.jsp"/>
                    </td>
                </tr>
                <tr>
                    <th>
                        <c:choose>
                            <c:when test="${not empty genericCodeForm.codeType.description}">${genericCodeForm.codeType.description}</c:when>
                            <c:otherwise>List of Codes</c:otherwise>
                        </c:choose>
                    </th>
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
