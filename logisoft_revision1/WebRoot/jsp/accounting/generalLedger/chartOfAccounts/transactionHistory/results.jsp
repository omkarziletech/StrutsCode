<%-- 
    Document   : results
    Created on : Jun 2, 2014, 9:46:22 PM
    Author     : Lakshmi Narayanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>      
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<c:choose>
    <c:when test="${not empty chartOfAccountsForm.accounts}">
        <div id="result-header" class="table-banner green">
            <div class="float-right">
                <c:choose>
                    <c:when test="${chartOfAccountsForm.totalRows gt 1}">${chartOfAccountsForm.totalRows} records found.</c:when>
                    <c:otherwise>1 record found.</c:otherwise>
                </c:choose>
            </div>
        </div>
        <div class="result-container" style="width: 100%">
            <table cellpadding="0" cellspacing="1" class="display-table">
                <thead>
                    <tr>
                        <th>Account</th>
                        <th>Period</th>
                        <th>Date</th>
                        <th>Source Code</th>
                        <th>Reference</th>
                        <th>Description</th>
                        <th>Debit</th>
                        <th>Credit</th>
                        <th>Net Change</th>
                        <th>Balance</th>
                    </tr>
                </thead>
                <tbody>
                    <c:set var="zebra" value="odd"/>
                    <c:set var="account" value=""/>
                    <c:set var="period" value=""/>
                    <c:set var="totalBalance" value="0.00"/>
                    <c:forEach var="row" items="${chartOfAccountsForm.accounts}" varStatus="status">
                        <c:if test="${account ne row.account or status.index eq 0}">
                            <c:set var="closingBalance" value="${chartOfAccountsForm.closingBalances[row.account]}"/>
                            <fmt:formatNumber var="openingBalance" value="${empty closingBalance ? 0.00 : closingBalance}" pattern="###,###,##0.00"/>
                            <c:choose>
                                <c:when test="${fn:startsWith(openingBalance, '-')}">
                                    <c:set var="openingBalance" value="(${fn:replace(openingBalance, '-', '')})"/>
                                    <c:set var="openingBalanceClass" value="amount red"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="openingBalanceClass" value="amount black"/>
                                </c:otherwise>
                            </c:choose>
                            <tr class="bg-thistle bold">
                                <td colspan="9" class="align-right">Opening Balance</td>
                                <td class="${openingBalanceClass}">${openingBalance}</td>
                                <c:set var="account" value="${row.account}"/>
                                <c:set var="totalBalance" value="${chartOfAccountsForm.closingBalances[row.account]}"/>
                            </tr>
                        </c:if>
                        <fmt:parseNumber var="balance" value="${fn:replace(row.netChange, ',', '')}"/>
                        <c:set var="totalBalance" value="${totalBalance + balance}"/>
                        <fmt:formatNumber var="netBalance" value="${totalBalance}" pattern="###,###,##0.00"/>
                        <c:choose>
                            <c:when test="${fn:startsWith(row.debit, '-')}">
                                <c:set var="debit" value="(${fn:replace(row.debit, '-', '')})"/>
                                <c:set var="debitClass" value="amount red"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="debit" value="${row.debit}"/>
                                <c:set var="debitClass" value="amount black"/>
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${fn:startsWith(row.credit, '-')}">
                                <c:set var="credit" value="(${fn:replace(row.credit, '-', '')})"/>
                                <c:set var="creditClass" value="amount red"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="credit" value="${row.credit}"/>
                                <c:set var="creditClass" value="amount black"/>
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${fn:startsWith(row.netChange, '-')}">
                                <c:set var="netChange" value="(${fn:replace(row.netChange, '-', '')})"/>
                                <c:set var="netChangeClass" value="amount red"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="netChange" value="${row.netChange}"/>
                                <c:set var="netChangeClass" value="amount black"/>
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${fn:startsWith(netBalance, '-')}">
                                <c:set var="netBalance" value="(${fn:replace(netBalance, '-', '')})"/>
                                <c:set var="netBalanceClass" value="amount red"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="netBalanceClass" value="amount black"/>
                            </c:otherwise>
                        </c:choose>
                        <tr class="${zebra}">
                            <td>${row.account}</td>
                            <td>${row.period}</td>
                            <td>${row.date}</td>
                            <td>${row.sourceCode}</td>
                            <td><a href="javascript: journalEntry('${row.reference}')">${row.reference}</a></td>
                            <td>${row.description}</td>
                            <td class="${debitClass}">${debit}</td>
                            <td class="${creditClass}">${credit}</td>
                            <td class="${netChangeClass}">${netChange}</td>
                            <td class="${netBalanceClass}">${netBalance}</td>
                        </tr>
                        <c:if test="${(status.index + 1) lt fn:length(chartOfAccountsForm.accounts)
                                      and row.period ne chartOfAccountsForm.accounts[status.index + 1].period}">
                              <tr class="bg-pale-green bold">
                                  <td colspan="9" class="align-right">Period Closing Balance</td>
                                  <td class="${netBalanceClass}">${netBalance}</td>
                              </tr>
                        </c:if>
                        <c:choose>
                            <c:when test="${status.index % 2 eq 0}">
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
    </c:when>
    <c:otherwise>
        <div class="table-banner green">No transaction history found</div>
    </c:otherwise>
</c:choose>