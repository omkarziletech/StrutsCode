<%-- 
    Document   : reconcileResults
    Created on : Mar 30, 2012, 2:49:33 AM
    Author     : Lakshmi Naryanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://cong.logiwareinc.com/string" prefix="str"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<c:choose>
    <c:when test="${not empty reconcileForm.transactions}">
	<div align="right" class="pagebanner" style="padding-right: 15px;">
	    <div style="float:right">
		<div style="float:left">
		    <c:choose>
			<c:when test="${reconcileForm.selectedRows>1}">${reconcileForm.selectedRows} transactions displayed.</c:when>
			<c:otherwise>1 transaction displayed.</c:otherwise>
		    </c:choose>
		</div>
	    </div>
	    <div class="search-results">
		<table width="100%" cellpadding="0" cellspacing="1" class="display-table">
		    <thead>
			<tr>
			    <c:choose>
				<c:when test="${reconcileForm.orderBy=='desc'}"><c:set var="orderBy" value="asc"/></c:when>
				<c:otherwise><c:set var="orderBy" value="desc"/></c:otherwise>
			    </c:choose>
			    <th><a href="javascript:doSort('transaction_type','${orderBy}')">Transaction Type</a></th>
			    <th><a href="javascript:doSort('reference_number','${orderBy}')">Reference Number</a></th>
			    <th><a href="javascript:doSort('batch_id','${orderBy}')">Batch Id</a></th>
			    <th><a href="javascript:doSort('transaction_date','${orderBy}')">Transaction Date</a></th>
			    <th><a href="javascript:doSort('debit','${orderBy}')">Debit</a></th>
			    <th><a href="javascript:doSort('credit','${orderBy}')">Credit</a></th>
			    <th><a href="javascript:doSort('status','${orderBy}')">Clear</a></th>
			</tr>
		    </thead>
		    <tbody>
			<c:set var="zebra" value="odd"/>
			<c:forEach var="transaction" items="${reconcileForm.transactions}">
			    <tr class="${zebra}">
				<td>${transaction.transactionType}</td>
				<td class="uppercase">${transaction.referenceNumber}</td>
				<td>${transaction.batchId}</td>
				<td>${transaction.transactionDate}</td>
				<c:choose>
				    <c:when test="${fn:startsWith(transaction.debit,'-')}">
					<td style="text-align:right;" class="red">(${fn:replace(transaction.debit,'-','')})</td>
				    </c:when>
				    <c:otherwise>
					<td style="text-align:right;">${transaction.debit}</td>
				    </c:otherwise>
				</c:choose>
				<c:choose>
				    <c:when test="${fn:startsWith(transaction.credit,'-')}">
					<td style="text-align:right;" class="red">(${fn:replace(transaction.credit,'-','')})</td>
				    </c:when>
				    <c:otherwise>
					<td style="text-align:right;">${transaction.credit}</td>
				    </c:otherwise>
				</c:choose>
				<td style="text-align: center">
				    <c:choose>
					<c:when test="${transaction.status=='RIP'}">
					    <input type="checkbox" class="clear"
						   value="${str:removeEnd(transaction.id,',')}" checked onclick="calculateTotals(this)"/>
					</c:when>
					<c:otherwise>
					    <input type="checkbox" class="clear"
						   value="${str:removeEnd(transaction.id,',')}" onclick="calculateTotals(this)"/>
					</c:otherwise>
				    </c:choose>
				    <input type="hidden" class="transactionType" value="${transaction.transactionType}"/>
				    <input type="hidden" class="referenceNumber" value="${transaction.referenceNumber}"/>
				    <input type="hidden" class="batchId" value="${transaction.batchId}"/>
				    <input type="hidden" class="transactionDate" value="${transaction.transactionDate}"/>
				    <input type="hidden" class="debit" value="${transaction.debit}"/>
				    <input type="hidden" class="credit" value="${transaction.credit}"/>
				    <input type="hidden" class="status" value="${transaction.status}"/>
				</td>
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
	    </div>
	</c:when>
	<c:otherwise>
	    <div class="pagebanner green" style="background-color: #D1E6EE;">No transactions found</div>
	</c:otherwise>
    </c:choose>