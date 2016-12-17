<%-- 
    Document   : arBatchImportPreview
    Created on : Jan 24, 2012, 6:04:28 PM
    Author     : Lakshmi Naryanan
--%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<div style="height: 210px;overflow-y: auto;" id="importedDiv">
    <table class="display-table" id="arBatch">
	<thead>
	    <tr>
		<th>Matches</th>
		<th>Type</th>
		<th>Invoice/BL</th>
		<th>Payment Amount</th>
		<th>Balance</th>
		<th>Comments</th>
	    </tr>
	</thead>
	<tbody>
            <c:forEach var="invoice" items="${invoices}" varStatus="status">
		<tr class="${status.count % 2 eq 0 ? 'odd' : 'even'}">
		    <td>
			<c:choose>
			    <c:when test="${invoice.matches}">
				<img src="${path}/images/fileUpload/success.png"/>
			    </c:when>
			    <c:otherwise>
				<img src="${path}/images/fileUpload/error.png"/>
			    </c:otherwise>
			</c:choose>
			<input type="hidden" class="importMatches" value="${invoice.matches}"/>
			<input type="hidden" class="importTransactionType" value="${invoice.transactionType}"/>
			<input type="hidden" class="importInvoiceOrBl" value="${invoice.invoiceOrBl}"/>
			<input type="hidden" class="importPaidAmount" value="${fn:replace(invoice.paidAmount,',','')}"/>
			<input type="hidden" class="importTransactionId" value="${invoice.transactionId}"/>
			<c:if test="${not empty invoice.transactionId}">
			    <c:set var="balanceInProcess" value="${fn:replace(invoice.balanceInProcess,',','')}"/>
			</c:if>
			<input type="hidden" class="importComments" value="${invoice.comments}"/>
			<input type="hidden" class="importBalanceInProcess" value="${balanceInProcess}"/>
		    </td>
		    <td>${invoice.transactionType}</td>
		    <td>${invoice.invoiceOrBl}</td>
		    <c:choose>
			<c:when test="${fn:startsWith(invoice.paidAmount,'-')}">
			    <td style="text-align:right;" class="red">(${fn:replace(invoice.paidAmount,'-','')})</td>
			</c:when>
			<c:otherwise>
			    <td style="text-align:right;">${invoice.paidAmount}</td>
			</c:otherwise>
		    </c:choose>
		    <c:choose>
			<c:when test="${fn:startsWith(invoice.balanceInProcess,'-')}">
			    <td style="text-align:right;" class="red">(${fn:replace(invoice.balanceInProcess,'-','')})</td>
			</c:when>
			<c:otherwise>
			    <td style="text-align:right;">${invoice.balanceInProcess}</td>
			</c:otherwise>
		    </c:choose>
		    <td>
			<c:choose>
			    <c:when test="${not invoice.matches && not empty invoice.transactionId}">
				<span class="float-left padding-left-right-1px">Payment amount</span> 
				<c:choose>
				    <c:when test="${fn:startsWith(invoice.paidAmount,'-')}">
					<span class="float-left red padding-left-right-1px">(${fn:replace(invoice.paidAmount,'-','')})</span>
				    </c:when>
				    <c:otherwise>
					<span class="float-left padding-left-right-1px">${invoice.paidAmount}</span>
				    </c:otherwise>
				</c:choose>
				<span class="float-left padding-left-right-1px">is not equal to balance</span>
				<c:choose>
				    <c:when test="${fn:startsWith(invoice.balanceInProcess,'-')}">
					<span class="float-left red padding-left-right-1px">(${fn:replace(invoice.balanceInProcess,'-','')})</span>
				    </c:when>
				    <c:otherwise>
					<span class="float-left padding-left-right-1px">${invoice.balanceInProcess}</span>
				    </c:otherwise>
				</c:choose>
			    </c:when>
			    <c:when test="${not invoice.matches}">
				<span class="float-left padding-left-right-1px">Invoice is not found</span>
			    </c:when>
			</c:choose>
		    </td>
		</tr>
	    </c:forEach>
	</tbody>
    </table>
</div>
<div style="text-align: center">
    <input type="button" value="Discard unmatched and Import" class="buttonStyleNew" onclick="saveImportedInvoices()"/>
</div>