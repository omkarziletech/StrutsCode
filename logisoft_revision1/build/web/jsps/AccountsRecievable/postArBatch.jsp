<%-- 
    Document   : postArBatch
    Created on : Jun 6, 2011, 9:01:05 AM
    Author     : lakshh
--%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<table width="100%"
       style="background-image: url(${path}/js/greybox/header_bg.gif);border-bottom: 2px solid #AAAAAA;">
    <tbody>
        <tr>
            <td class="lightBoxHeader">Post Batch - ${arBatch.batchId} </td>
            <td>
                <a id="lightBoxClose" href="javascript: closePostDiv('${arBatch.batchId}')">
                    <img alt="" src="/logisoft/js/greybox/w_close.gif" title="Close" style="border: none;">Close
                </a>
            </td>
        </tr>
    </tbody>
</table>
<c:if test="${not empty error}">
    <div class="error">${error}</div>
</c:if>
<div>
    <table width="100%" cellpadding="0" cellspacing="3" class="displaytagstyleNew" id="paymentChecks" style="border: 1px solid white">
        <thead>
            <tr>
                <th>Batch Number</th>
                <th>User</th>
                <th>Deposit Date</th>
                <th style="text-align:right;">Total Amount</th>
                <th style="text-align:right;">Applied Amount</th>
                <th style="text-align:right;">Balance Amount</th>
                <th>Note</th>
            </tr>
        </thead>
        <tr>
            <td>${arBatch.batchId}</td>
            <td>${arBatch.user}</td>
            <td>${arBatch.depositDate}</td>
            <c:choose>
                <c:when test="${fn:startsWith(arBatch.totalAmount,'-')}">
                    <td style="text-align:right;" class="red">(${fn:replace(arBatch.totalAmount,'-','')})</td>
                </c:when>
                <c:otherwise>
                    <td style="text-align:right;">${arBatch.totalAmount}</td>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${fn:startsWith(arBatch.appliedAmount,'-')}">
                    <td style="text-align:right;" class="red">(${fn:replace(arBatch.appliedAmount,'-','')})</td>
                </c:when>
                <c:otherwise>
                    <td style="text-align:right;">${arBatch.appliedAmount}</td>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${fn:startsWith(arBatch.balanceAmount,'-')}">
                    <td style="text-align:right;" class="red">(${fn:replace(arBatch.balanceAmount,'-','')})</td>
                </c:when>
                <c:otherwise>
                    <td style="text-align:right;">${arBatch.balanceAmount}</td>
                </c:otherwise>
            </c:choose>
            <td style="white-space: pre-wrap;">${arBatch.notes}</td>
        </tr>
        <c:if test="${isNetSett}">
            <tr class="tableHeadingNew">
                <td colspan="7">Net Settlement Batch (Need to create AR Transaction)</td>
            </tr>
            <tr class="textlabelsBold">
                <td>Select Customer</td>
                <td>
                    <select id="customerNumber" name="customerNumber">
                        <c:forEach var="customer" items="${customers}">
                            <option value="${customer.value}">${customer.label}</option>
                        </c:forEach>
                    </select>
                </td>
                <td>
                    Other Customer
                    <input type="checkbox" name="otherCustomer" id="otherCustomer"/>
                </td>
                <td class="otherCustomer" colspan="4" style="display:none">
                    <input type="text" name="otherCustomerName" id="otherCustomerName" class="textlabelsBoldForTextBox" style="text-transform: uppercase"/>
                    <input type="hidden" name="otherCustomerNameValid" id="otherCustomerNameValid"/>
                    <div id="otherCustomerNameChoices" class="autocomplete"></div>
                    <input type="text" name="otherCustomerNumber" id="otherCustomerNumber" class="textlabelsBoldForTextBoxDisabledLook" readonly/>
                </td>
            </tr>
        </c:if>
        <c:if test="${canPost}">
            <tr>
                <td colspan="7" style="text-align:center;">
                    <input type="button" class="buttonStyleNew" value="Post" 
			   onclick="post('${arBatch.batchId}','${arBatch.depositDate}','${arBatch.bankAccount}','${arBatch.glAccount}')"/>
                </td>
            </tr>
        </c:if>
    </table>
</div>
