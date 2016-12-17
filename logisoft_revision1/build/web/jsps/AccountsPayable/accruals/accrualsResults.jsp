<%-- 
    Document   : accrualsResults
    Created on : Aug 2, 2012, 4:12:41 PM
    Author     : Lakshmi Naryanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>      
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>   
<c:set var="path" value="${pageContext.request.contextPath}"/>
<c:choose>
    <c:when test="${accrualsForm.rowsOnly}">
	<%@include file="accrualsResultBanner.jspf"%>
	<c:if test="${not empty accrualsForm.results}">
	    <c:set var="bgColor" value="white"/>
	    <c:if test="${accrualsForm.action=='addAccrual' || accrualsForm.action=='updateAccrual'}">
		<c:set var="bgColor" value=" lightgreen !important"/>
	    </c:if>
	    <c:forEach var="accrual" items="${accrualsForm.results}">
		<c:choose>
		    <c:when test="${accrual.transactionType=='AR'}">
			<%@include file="arRows.jspf"%>
		    </c:when>
		    <c:otherwise>
			<%@include file="accrualRows.jspf"%>
		    </c:otherwise>
		</c:choose>
	    </c:forEach>
	</c:if>
    </c:when>
    <c:otherwise>
	<c:choose>
	    <c:when test="${not empty accrualsForm.results}">
		<c:set var="bgColor" value="white"/>
		<c:if test="${accrualsForm.action=='addAccrual' || accrualsForm.action=='updateAccrual'}">
		    <c:set var="bgColor" value=" lightgreen !important"/>
		</c:if>
		<div class="result-container">
		    <table width="100%" cellpadding="0" cellspacing="1" class="display-table" id="accruals">
			<thead>
			    <tr>
				<c:choose>
				    <c:when test="${accrualsForm.orderBy=='desc'}">
					<c:set var="orderBy" value="asc"/>
				    </c:when>
				    <c:otherwise>
					<c:set var="orderBy" value="desc"/>
				    </c:otherwise>
				</c:choose>
				<th><a href="javascript:doSort('vendorName')">Vendor Name</a></th>
				<th><a href="javascript:doSort('vendorNumber')">Vendor Number</a></th>
				<th><a href="javascript:doSort('invoiceNumber')">Invoice Number</a></th>
				<th><a href="javascript:doSort('blNumber')">B/L</a></th>
				<th><a href="javascript:doSort('container')">Container</a></th>
				<th><a href="javascript:doSort('voyage')">Voyage</a></th>
				<th><a href="javascript:doSort('dockReceipt')">D/R</a></th>
				<th><a href="javascript:doSort('reportingDate')">Reporting<br/>Date</a></th>
				<th class="width-125px"><a href="javascript:doSort('accruedAmount')">Amount</a></th>
				<th><a href="javascript:doSort('bluescreenCostCode')">BS Cost Code</a></th>
				<th><a href="javascript:doSort('costCode')">Cost Code</a></th>
				<th>Terminal</th>
				<th><a href="javascript:doSort('transactionType')">Type</a></th>
				<th>ASG</th>
				<th>DIS</th>
				<th>INA</th>
				<th>DEL</th>
				<th>Action</th>
			    </tr>
			</thead>
			<tbody>
			    <c:if test="${not empty accrualsForm.results}">
				<%@include file="accrualsResultBanner.jspf"%>
				<c:forEach var="accrual" items="${accrualsForm.results}">
				    <c:choose>
					<c:when test="${accrual.transactionType=='AR'}">
					    <%@include file="arRows.jspf"%>
					</c:when>
					<c:otherwise>
					    <%@include file="accrualRows.jspf"%>
					</c:otherwise>
				    </c:choose>
				</c:forEach>
			    </c:if>
			</tbody>
		    </table>
		</div>
	    </c:when>
	    <c:otherwise>
		<div class="table-banner green" style="background-color: #D1E6EE;">No accruals found</div>
	    </c:otherwise>
	</c:choose>
    </c:otherwise>
</c:choose>