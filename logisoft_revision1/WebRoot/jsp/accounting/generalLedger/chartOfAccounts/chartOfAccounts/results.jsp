<%-- 
    Document   : results
    Created on : Jun 2, 2014, 6:05:33 PM
    Author     : Lakshmi Narayanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>      
<c:set var="path" value="${pageContext.request.contextPath}"/>
<c:choose>
    <c:when test="${not empty chartOfAccountsForm.accounts}">
        <div id="result-header" class="table-banner green">
            <div class="float-right">
                <div class="float-left">
                    <c:choose>
                        <c:when test="${chartOfAccountsForm.totalRows gt chartOfAccountsForm.selectedRows}">
                            ${chartOfAccountsForm.selectedRows} accounts displayed. ${chartOfAccountsForm.totalRows} accounts found.
                        </c:when>
                        <c:when test="${chartOfAccountsForm.selectedRows gt 1}">${chartOfAccountsForm.selectedRows} accounts found.</c:when>
                        <c:otherwise>1 account found.</c:otherwise>
                    </c:choose>
                </div>
                <c:if test="${chartOfAccountsForm.totalPages gt 1 and chartOfAccountsForm.selectedPage gt 1}">
                    <a href="javascript: paging('1')">
                        <img title="First page" src="${path}/images/first.png"/>
                    </a>
                    <a href="javascript: paging('${chartOfAccountsForm.selectedPage-1}')">
                        <img title="Previous page" src="${path}/images/prev.png"/>
                    </a>
                </c:if>
                <c:if test="${chartOfAccountsForm.totalPages gt 1}">
                    <select id="selectedPageNo" class="dropdown float-left">
                        <c:forEach begin="1" end="${chartOfAccountsForm.totalPages}" var="selectedPage">
                            <c:choose>
                                <c:when test="${chartOfAccountsForm.selectedPage eq selectedPage}">
                                    <option value="${selectedPage}" selected>${selectedPage}</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${selectedPage}">${selectedPage}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                    <a href="javascript: paging()">
                        <img alt="Goto Page" title="Goto Page" src="${path}/images/go.jpg"/>
                    </a>
                </c:if>
                <c:if test="${chartOfAccountsForm.totalPages gt chartOfAccountsForm.selectedPage}">
                    <a href="javascript: paging('${chartOfAccountsForm.selectedPage + 1}')">
                        <img title="Next page" src="${path}/images/next.png"/>
                    </a>
                    <a href="javascript: paging('${chartOfAccountsForm.totalPages}')">
                        <img title="Last page" src="${path}/images/last.png"/>
                    </a>
                </c:if>
            </div>
        </div>
        <div class="result-container" style="width: 100%">
            <table cellpadding="0" cellspacing="1" class="display-table">
                <thead>
                    <tr>
                        <th><a href="javascript: sorting('account')">Account</a></th>
                        <th><a href="javascript: sorting('acct_desc')">Description</a></th>
                        <th><a href="javascript: sorting('normal_balance')">Normal Balance</a></th>
                        <th><a href="javascript: sorting('multi_currency')">Multi Currency</a></th>
                        <th><a href="javascript: sorting('acct_status')">Status</a></th>
                        <th><a href="javascript: sorting('acct_type')">Account Type</a></th>
                        <th><a href="javascript: sorting('acct_group')">Account Group</a></th>
                        <th><a href="javascript: sorting('control_acct')">Control Account</a></th>
                        <c:if test="${writeMode}">
                            <th>Actions</th>
                        </c:if>
                    </tr>
                </thead>
                <tbody>
                    <c:set var="zebra" value="odd"/>
                    <c:choose>
                        <c:when test="${writeMode}">
                            <c:forEach var="row" items="${chartOfAccountsForm.accounts}" varStatus="status">
                                <tr class="${zebra}">
                                    <td>
                                        <a href="javascript:editAccount('${row.account}')">${row.account}</a>
                                    </td>
                                    <td class="red-90">${row.description}</td>
                                    <td>${row.normalBalance}</td>
                                    <td>${row.multiCurrency}</td>
                                    <td>${row.status}</td>
                                    <td>${row.accountType}</td>
                                    <td class="red-90">${row.accountGroup}</td>
                                    <td>${row.controlAccount}</td>
                                    <td>
                                        <img title="Account" src="${path}/images/icons/account.png" onclick="accountHistory('${row.account}');"/>
                                        <img title="Transaction" src="${path}/images/icons/transaction.png" onclick="searchTransactions('${row.account}');"/>
                                        <img title="Comparison" src="${path}/images/icons/comparison.png" onclick="comparison('${row.account}');"/>
                                        <img title="Budget" src="${path}/images/icons/budget.png" onclick="budget('${row.account}');"/>
                                        <img title="Currency" src="${path}/images/icons/currency.png" onclick="currency('${row.account}');" />
                                    </td>
                                </tr>
                                <c:choose>
                                    <c:when test="${status.index % 2 eq 0}">
                                        <c:set var="zebra" value="even"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="zebra" value="odd"/>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="row" items="${chartOfAccountsForm.accounts}" varStatus="status">
                                <tr class="${zebra}">
                                    <td>${row.account}</td>
                                    <td class="red-90">${row.description}</td>
                                    <td>${row.normalBalance}</td>
                                    <td>${row.multiCurrency}</td>
                                    <td>${row.status}</td>
                                    <td>${row.accountType}</td>
                                    <td class="red-90">${row.accountGroup}</td>
                                    <td>${row.controlAccount}</td>
                                </tr>
                                <c:choose>
                                    <c:when test="${status.index % 2 eq 0}">
                                        <c:set var="zebra" value="even"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="zebra" value="odd"/>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </c:when>
    <c:otherwise>
        <div class="table-banner green">No accounts found</div>
    </c:otherwise>
</c:choose>