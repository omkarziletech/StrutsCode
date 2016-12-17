<%-- 
    Document   : transactions
    Created on : Oct 13, 2013, 4:31:30 PM
    Author     : Meiyazhakan
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN" "http://www.w3.org/TR/REC-html40/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>      
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://cong.logiwareinc.com/string" prefix="str"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<html>
    <head>
        <title>Transactions</title>
        <link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.js"></script>
        <script type="text/javascript">
            $(document).ready(function() {
                $("[title != '']").not("link").tooltip();
            });
        </script>
    </head>
    <body>
        <table class="table" style="margin: 0;border: none;padding: 0;">
            <tr>
                <th class="toggle">Posted Transactions</th>
            </tr>
            <tr>
                <c:choose>
                    <c:when test="${not empty postedTransactions}">
                        <td>
                            <div class="result-container posted">
                                <table width="100%" cellpadding="0" cellspacing="1" class="display-table">
                                    <thead>
                                        <tr>
                                            <th class="width-200px">Customer Name</th>
                                            <th class="width-145px">Transaction Date</th>
                                            <th class="width-100px">Approval Date</th>
                                            <th class="width-100px">Type</th>
                                            <th class="width-125px">Amount</th>
                                            <th class="width-100px">Batch #</th>
                                            <th class="width-150px">Check #</th>
                                            <th class="width-100px">GL Account</th>
                                            <th class="width-125px">Adj. Date</th>
                                            <th class="width-125px">Adj. Amount</th>
                                            <th class="width-100px">User</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:set var="zebra" value="odd"/>
                                        <c:set var="totalAmount" value="0"/>
                                        <c:set var="totalAdjustment" value="0"/>
                                        <c:forEach var="row" items="${postedTransactions}">
                                            <tr class="${zebra}">
                                                <td><span title="${row.customerName}<br/><b><font color=green>${row.customerNumber}</font></b>"> ${fn:substring(row.customerName,0,20)}</span></td>
                                                <td>${row.transactionDate}</td>
                                                <td>${row.postedDate}</td>
                                                <td>${row.type}</td>
                                                <c:choose>
                                                    <c:when test="${fn:contains(row.transactionAmount, '-')}">
                                                        <td class="amount red">(${fn:replace(row.transactionAmount, '-','')})</td>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <td class="amount black">${row.transactionAmount}</td>
                                                    </c:otherwise>
                                                </c:choose>
                                                <td>${row.batchNumber}</td>
                                                <td>${row.checkNumber}</td>
                                                <td>${row.glAccount}</td>
                                                <td>${row.adjustmentDate}</td>
                                                <c:choose>
                                                    <c:when test="${fn:contains(row.adjustmentAmount, '-')}">
                                                        <td class="amount red">(${fn:replace(row.adjustmentAmount, '-','')})</td>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <td class="amount black">${row.adjustmentAmount}</td>
                                                    </c:otherwise>
                                                </c:choose>
                                                <td>${row.user}</td>
                                            </tr>
                                            <fmt:parseNumber var="amount" type="number" value="${fn:replace(row.transactionAmount, ',', '')}"/>
                                            <c:set var="totalAmount" value="${totalAmount + amount}"/>
                                            <fmt:parseNumber var="adjustment" type="number" value="${fn:replace(row.adjustmentAmount, ',', '')}"/>
                                            <c:set var="totalAdjustment" value="${totalAdjustment + adjustment}"/>
                                            <c:choose>
                                                <c:when test="${zebra=='odd'}">
                                                    <c:set var="zebra" value="even"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:set var="zebra" value="odd"/>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </tbody>
                                    <tfoot>
                                        <tr>
                                            <td colspan="4" class="align-right">Total :&nbsp;</td>
                                            <fmt:formatNumber var="totalAmount" value="${totalAmount}" pattern="###,###,##0.00"/>
                                            <c:choose>
                                                <c:when test="${fn:contains(totalAmount, '-')}">
                                                    <td class="amount red">(${fn:replace(totalAmount, '-','')})</td>
                                                </c:when>
                                                <c:otherwise>
                                                    <td class="amount black">${totalAmount}</td>
                                                </c:otherwise>
                                            </c:choose>
                                            <td colspan="4" class="align-center">+</td>
                                            <fmt:formatNumber var="totalAdjustment" value="${totalAdjustment}" pattern="###,###,##0.00"/>
                                            <c:choose>
                                                <c:when test="${fn:contains(totalAdjustment, '-')}">
                                                    <td class="amount red">(${fn:replace(totalAdjustment, '-','')})</td>
                                                </c:when>
                                                <c:otherwise>
                                                    <td class="amount black">${totalAdjustment}</td>
                                                </c:otherwise>
                                            </c:choose>
                                            <fmt:parseNumber var="totalAmount" type="number" value="${totalAmount}"/>
                                            <fmt:parseNumber var="totalAdjustment" type="number" value="${totalAdjustment}"/>
                                            <fmt:formatNumber var="total" value="${totalAmount + totalAdjustment}" pattern="###,###,##0.00"/>
                                            <c:choose>
                                                <c:when test="${fn:contains(total, '-')}">
                                                    <td class="amount red">(${fn:replace(total, '-','')})</td>
                                                </c:when>
                                                <c:otherwise>
                                                    <td class="amount black">${total}</td>
                                                </c:otherwise>
                                            </c:choose>
                                        </tr>
                                    </tfoot>
                                </table>
                            </div>
                        </td>
                    </c:when>
                    <c:otherwise>
                        <td>
                            <div class="table-banner green">No records found</div>
                        </td>
                    </c:otherwise>
                </c:choose>
        </table>
    </body>
</html>