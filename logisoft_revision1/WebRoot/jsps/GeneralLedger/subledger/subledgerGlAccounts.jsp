<%-- 
    Document   : subledgerGlAccounts
    Created on : Dec 26, 2012, 5:03:07 PM
    Author     : Lakshmi Narayanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>      
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>   
<c:set var="path" value="${pageContext.request.contextPath}"/>
<table width="100%" cellpadding="0" cellspacing="1" class="display-table">
    <thead>
	<tr>
	    <th>Subledger</th>
	    <th>AR Batch</th>
	    <th>AP Batch</th>
	    <th>BL</th>
	    <th>Invoice</th>
	    <th>GL Account</th>
	    <th>Charge Code</th>
	    <th>Voyage</th>
	    <th>Transaction Date</th>
	    <th>Reporting Date</th>
	    <th>Posted Date</th>
	    <th>Amount</th>
	    <th>Debit</th>
	    <th>Credit</th>
	    <th>Type</th>
	    <th>Journal Entry</th>
	    <th>Line Item</th>
	</tr>
    </thead>
    <tbody>
	<c:set var="zebra" value="odd"/>
	<c:forEach var="result" items="${subledgerForm.results}" varStatus="status">
	    <tr class="${zebra}">
		<td>${result.subledger}</td>
		<td>${result.arBatchId}</td>
		<td>${result.apBatchId}</td>
		<td>${result.blNumber}</td>
		<td>${result.invoiceNumber}</td>
		<td>
		    <input type="text" id="glAccounts${status.count-1}" value="${result.glAccount}" class="textbox glAccounts"/>
		    <img title="Update GL Account" src="${path}/images/icons/update.png"
				     style="width: 16px;height: 16px;" onclick="updateGlAccount('${result.id}','glAccounts${status.count-1}')"/>
		</td>
		<td>${result.chargeCode}</td>
		<td>${result.voyage}</td>
		<td>${result.transactionDate}</td>
		<td>${result.reportingDate}</td>
		<td>${result.postedDate}</td>
		<c:choose>
		    <c:when test="${fn:contains(result.amount,'-')}">
			<td class="align-right red">(${fn:replace(result.amount,'-','')})</td>
		    </c:when>
		    <c:otherwise>
			<td class="align-right black">${result.amount}</td>
		    </c:otherwise>
		</c:choose>
		<td class="align-right">${result.debit}</td>
		<td class="align-right">${result.credit}</td>
		<td class="align-center">${result.transactionType}</td>
		<td class="align-center">${result.journalEntryId}</td>
		<td class="align-center">${result.lineItemId}</td>
	    </tr>
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
</table>