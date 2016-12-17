<%-- 
    Document   : paymentList
    Created on : 23 Jun, 2015, 11:46:19 PM
    Author     : Lakshmi Narayanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>      
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<c:choose>
    <c:when test="${not empty apPaymentForm.paymentList}">
        <div class="table-banner green align-right">${fn:length(apPaymentForm.paymentList)} payable record${fn:length(apPaymentForm.paymentList) > 1 ? 's' : ''} found</div>
        <div class="scrollable-table">
            <div>
                <div>
                    <table>
                        <thead>
                            <tr>
                                <th><div label="Vendor Name"/></th>
                                <th><div label="Vendor Number"/></th>
                                <th><div label="Credit Hold"/></th>
                                <th><div label="Payment Amount"/></th>
                                <th><div label="Payment Method"/></th>
                                <th><div label="Payment Date"/></th>
                                <th><div label="Pay"/></th>
                                <th><div label="Approve"/></th>
                                <th><div label="Action"/></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:set var="zebra" value="odd"/>
                            <c:forEach var="payment" items="${apPaymentForm.paymentList}" varStatus="status">
                                <c:choose>
                                    <c:when test="${payment.status eq 'WFA' && not loginuser.achApprover}">
                                        <tr class="bg-grey">
                                            <td>
                                                ${payment.vendorName}
                                                <input type="hidden" class="vendorName" value="${payment.vendorName}"/>
                                            </td>
                                            <td>
                                                ${payment.vendorNumber}
                                                <input type="hidden" class="vendorNumber" value="${payment.vendorNumber}"/>
                                            </td>
                                            <td>
                                                ${payment.creditHold}
                                                <input type="hidden" class="creditHold" value="${payment.creditHold}"/>
                                            </td>
                                            <td class="align-right ${fn:startsWith(payment.paymentAmount, '-') ? 'red' : 'black'}">
                                                ${fn:startsWith(payment.paymentAmount, '-') ? '('.concat(fn:substringAfter(payment.paymentAmount, '-')).concat(')') : payment.paymentAmount}
                                                <input type="hidden" class="paymentAmount" value="${payment.paymentAmount}"/>
                                            </td>
                                            <td class="align-center">
                                                <select class="dropdown paymentMethod readonly" disabled style="min-width: 85px;">
                                                    <c:forEach var="paymentMethod" items="${fn:split(payment.paymentMethods, ',')}">
                                                        <option value="${paymentMethod}" ${paymentMethod eq payment.paymentMethod ? 'selected' : ''}>${paymentMethod}</option>
                                                    </c:forEach>
                                                </select>
                                            </td>
                                            <td class="align-center">
                                                <input type="text" class="textbox readonly paymentDate align-center width-100px" value="${payment.paymentDate}" readonly/>
                                            </td>
                                            <td class="align-center">
                                                <input type="checkbox" class="pay" checked disabled/>
                                            </td>
                                            <td class="align-center">
                                                <input type="checkbox" class="approve" disabled/>
                                            </td>
                                            <td class="align-center">
                                                <input type="hidden" class="status" value="${payment.status}"/>
                                                <input type="hidden" class="ids" value="${payment.ids}"/>
                                                <img src="${path}/img/icons/showall.gif" title="Show Invoices" onclick="showInvoices('${payment.ids}', 'false', '${payment.vendorNumber}');"/>
                                            </td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <tr class="${zebra}">
                                            <td>
                                                ${payment.vendorName}
                                                <input type="hidden" class="vendorName" value="${payment.vendorName}"/>
                                            </td>
                                            <td>
                                                ${payment.vendorNumber}
                                                <input type="hidden" class="vendorNumber" value="${payment.vendorNumber}"/>
                                            </td>
                                            <td>
                                                ${payment.creditHold}
                                                <input type="hidden" class="creditHold" value="${payment.creditHold}"/>
                                            </td>
                                            <td class="align-right ${fn:startsWith(payment.paymentAmount, '-') ? 'red' : 'black'}">
                                                ${fn:startsWith(payment.paymentAmount, '-') ? '('.concat(fn:substringAfter(payment.paymentAmount, '-')).concat(')') : payment.paymentAmount}
                                                <input type="hidden" class="paymentAmount" value="${payment.paymentAmount}"/>
                                            </td>
                                            <td class="align-center">
                                                <select class="dropdown paymentMethod" style="min-width: 85px;" onchange="validatePay(this)">
                                                    <c:forEach var="paymentMethod" items="${fn:split(payment.paymentMethods, ',')}">
                                                        <option value="${paymentMethod}" ${paymentMethod eq payment.paymentMethod ? 'selected' : ''}>${paymentMethod}</option>
                                                    </c:forEach>
                                                </select>
                                            </td>
                                            <td class="align-center">
                                                <input type="text" class="textbox readonly paymentDate align-center width-100px" value="${payment.paymentDate}" readonly/>
                                            </td>
                                            <td class="align-center">
                                                <c:choose>
                                                    <c:when test="${payment.status eq 'WFA'}">
                                                        <input type="checkbox" class="pay" checked onclick="pay(this);"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <input type="checkbox" class="pay" onclick="pay(this);"/>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td class="align-center">
                                                <c:choose>
                                                    <c:when test="${loginuser.achApprover && payment.status eq 'WFA'}">
                                                        <input type="checkbox" class="approve"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <input type="checkbox" class="approve" disabled/>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td class="align-center">
                                                <input type="hidden" class="status" value="${payment.status}"/>
                                                <input type="hidden" class="ids" value="${payment.ids}"/>
                                                <img src="${path}/img/icons/showall.gif" title="Show Invoices" onclick="showInvoices('${payment.ids}', '${writeMode}', '${payment.vendorNumber}');"/>
                                            </td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
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
            </div>
        </div>
    </c:when>
    <c:otherwise>
        <div class="table-banner green">No payable records found</div>
    </c:otherwise>
</c:choose>