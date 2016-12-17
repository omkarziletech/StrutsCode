<%-- 
    Document   : scan
    Created on : 21 Sep, 2015, 3:10:46 PM
    Author     : lakshmi
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<html>
    <head>
        <title>Scan</title>
        <link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
        <script type="text/javascript" src="${path}/Resources/dynamsoft.webtwain.initiate.js"></script>
        <script type="text/javascript" src="${path}/Resources/dynamsoft.webtwain.config.js"></script>
        <script type="text/javascript" src="${path}/js/common/scan.js"></script>
    </head>
    <body>
        <form action="${path}/scan.do" name="scanForm" id="scanForm" method="post">
            <input type="button" class="button" value="Scan" onclick="location.reload();"/>
            <input type="button" class="button" value="Go Back" onclick="goBack();"/>
            <div id="dwtcontrolContainer"></div>
            <input type="hidden" name="screenName" id="screenName" value="${documentStoreLog.screenName}"/>
            <input type="hidden" name="documentId" id="documentId" value="${documentStoreLog.documentID}"/>
            <input type="hidden" name="documentName" id="documentName" value="${documentStoreLog.documentName}"/>
            <input type="hidden" name="documentType" id="documentType" value="${documentStoreLog.documentType}"/>
            <input type="hidden" name="comment" id="comment" value="${documentStoreLog.comments}"/>
            <input type="hidden" name="hiddenScreenName" id="hiddenScreenName" value="${scanForm.hiddenScreenName}"/>
            <input type="hidden" name="bookingType" id="bookingType" value="${scanForm.bookingType}"/>
            <c:if test="${not empty documentStoreLog.status}">
                <input type="hidden" name="status" id="status" value="${documentStoreLog.status}"/>
            </c:if>

                <c:choose>
                    <c:when test="${not empty scanForm.hiddenScreenName && scanForm.hiddenScreenName eq 'LCL IMPORTS DR' 
                  && scanForm.bookingType eq 'T' && scanForm.screenName eq 'LCL EXPORTS DR'}">
                        <input type="hidden" name="screenName" id="screenName" value="${scanForm.screenName}"/> 
                    </c:when>
                    <c:otherwise>
                        <input type="hidden" name="screenName" id="screenName" value="${documentStoreLog.screenName}"/>
                    </c:otherwise>
                </c:choose>   
            <input type="hidden" name="action" id="action"/>
        </form>
    </body>
</html>
