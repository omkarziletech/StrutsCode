<%-- 
    Document   : payments
    Created on : Mar 04, 2016, 1:32:24 PM
    Author     : Lakshmi Narayanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>      
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>      
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
        <div class="result-container">
            <c:if test="${not empty checks}">
                <div class="scrollable-table" style="margin: 0 0 20px 0 !important">
                    <div>
                        <div>
                            <table>
                                <thead>
                                    <tr>
                                        <th><div label="Check Number"/></th>
                                        <th><div label="Paid By / Paid Date"/></th>
                                        <th><div label="Amount"/></th>
                                        <th><div label="Entered By"/></th>
                                        <th><div label="Payment Type"/></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="check" items="${checks}">
                                        <tr>
                                            <td>${check.checkNumber}</td>
                                            <td>${check.paidBy}</td>
                                            <td class="align-right">${check.amount}</td>
                                            <td>${check.enteredBy}</td>
                                            <td>${check.paymentType}</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table> 
                        </div>
                    </div>
                </div>
            </c:if>
            <c:if test="${not empty payments}">
                <div class="scrollable-table">
                    <div>
                        <div>
                            <table>
                                <thead>
                                    <tr>
                                        <th><div label="Charge Code"/></th>
                                        <th><div label="Charge Amount"/></th>
                                        <th><div label="Amount Paid"/></th>
                                        <th><div label="Balance"/></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="payment" items="${payments}" varStatus="row">
                                        <tr>
                                            <c:choose>
                                                <c:when test="${row.index ne fn:length(payments)-1}">
                                                    <td>${payment.chargeDesc}</td>
                                                    <td class="align-right">${payment.amount}</td>
                                                    <td class="align-right">${payment.payment}</td>
                                                    <td class="align-right">${payment.balance}</td>
                                                </c:when>
                                                <c:otherwise>
                                                    <td class="bold green align-right" style="font-size: medium !important;">TOTAL ($-USD)</td>
                                                    <td class="bold red align-right" style="font-size: medium !important;">${payment.amount}</td>
                                                    <td class="bold red align-right" style="font-size: medium !important;">${payment.payment}</td>
                                                    <td class="bold red align-right" style="font-size: medium !important;">${payment.balance}</td>
                                                </c:otherwise>
                                            </c:choose>
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