<%-- 
    Author     : PALRAJ
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>      
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<html>
    <head>
        <title>Payments</title>
        <link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
    <style type="text/css">
        .scrollable-table{
            min-height: 0px;
        }
        .scrollable-table > div{
            height: auto;
        }
    </style>
</head>
<body>
    <div class="results-container">
        <c:if test="${not empty cfclCharge}">
            <div class="scrollable-table" >
                <div>
                    <div>
                        <table class="display-table">
                            <thead>
                                <tr>
                                    <th><div label="File Number"/></th>
                            <th><div label="Hazmat"/></th>
                            <th><div label="DR Amount"/></th>
                            <th><div label="Invoice Number"/></th>
                            <th><div label="Invoice Amount"/></th>
                            </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="cfclCharge" items="${cfclCharge}">
                                    <tr>
                                        <td>${cfclCharge.fileNumber}</td>
                                        <td>
                                            <c:if test="${cfclCharge.hazmat}">
                                                <img src="${path}/img/icons/danger..png" style="cursor:pointer" width="12" height="12" alt="Haz" title="Hazardous Information"/>   
                                            </c:if>
                                        </td>
                                        <td>${cfclCharge.arAmount}</td>
                                        <td>${cfclCharge.invoiceNumber}</td>
                                        <td>${cfclCharge.invoiceAmount}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table> 
                    </div>
                </div>
            </div>
        </c:if>
    </div>
</body>
</html>