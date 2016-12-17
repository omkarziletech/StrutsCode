<%-- 
    Document   : adjustment
    Created on : Nov 4, 2013, 4:36:24 PM
    Author     : Lakshmi Narayanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>      
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<div class="result-container">
    <div class="adjustedMessage align-center italic" >${message}</div>
    <table class="display-table">
        <thead>
            <tr>
		<th>Customer Name</th>
		<th>Customer Number</th>
		<th>BL Number</th>
		<th>Invoice Number</th>
		<th>Balance</th>
		<th>Suffix</th>
		<th>Action</th>
	    </tr>
	</thead>
	<tbody>
            <input type="hidden" value="${fn:length(noSuffixInvoices)}" id="transactionList">
	    <c:forEach var="invoice" items="${noSuffixInvoices}">
		<c:choose>
		    <c:when test="${zebra eq 'odd'}">
			<c:set var="zebra" value="even"/>
		    </c:when>
		    <c:otherwise>
			<c:set var="zebra" value="odd"/>
		    </c:otherwise>
		</c:choose>
		<tr class="${zebra}">
		    <td>${invoice.custName}</td>
		    <td>${invoice.custNo}</td>
		    <td>${invoice.billLaddingNo}</td>
		    <td>${invoice.invoiceNumber}</td>
		    <c:choose>
			<c:when test="${invoice.balance<0.00}">
			    <td class="amount red">
				(<fmt:formatNumber value="${-invoice.balance}" pattern="0.00"/>)
			    </td>
			</c:when>
			<c:otherwise>
			    <td class="amount black">
				<fmt:formatNumber value="${invoice.balance}" pattern="0.00"/>
			    </td>
			</c:otherwise>
		    </c:choose>
		    <td>
			<input type="text" class="suffix textbox width-35px" value="" maxlength="2"/>
		    </td>
		    <td>
			<img src="${path}/images/icons/adjustment.gif" title="Adjust" onclick="saveAdjustment('${invoice.transactionId}', this)"/>
		    </td>
		</tr>
	    </c:forEach>
	</tbody>
    </table>
</div>