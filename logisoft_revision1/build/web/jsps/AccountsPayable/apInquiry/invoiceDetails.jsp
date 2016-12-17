<%-- 
    Document   : invoiceDetails
    Created on : Sep 6, 2012, 10:38:38 PM
    Author     : Lakshmi Narayanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://cong.logiwareinc.com/string" prefix="str"%>
<html>
    <head>
	<c:set var="path" value="${pageContext.request.contextPath}"/>
        <title>Invoice Details</title>
	<link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
	<link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
	<script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.js"></script>
	<script type="text/javascript">
	    $(document).ready(function(){
		$("[title != '']").not("link").tooltip();
	    })
	</script>
    </head>
    <body>
	<div class="result-container">
	    <table width="100%" cellpadding="0" cellspacing="1" class="display-table">
		<thead>
		    <tr>
			<th>BL Number</th>
			<th>Dock Receipt</th>
			<th>Voyage</th>
			<th>Container</th>
			<th>Reporting Date</th>
			<th>Posted Date</th>
			<th>Amount</th>
			<th>Cost Code</th>
			<th>GL Account</th>
			<th>Type</th>
		    </tr>
		</thead>
		<tbody>
		    <c:set var="zebra" value="odd"/>
		    <c:forEach var="invoiceDetail" items="${invoiceDetails}">
			<tr class="${zebra}">
			    <td>${invoiceDetail.blNumber}</td>
			    <td>${invoiceDetail.dockReceipt}</td>
			    <td>${invoiceDetail.voyage}</td>
			    <td>
				<label title="${invoiceDetail.container}">${str:abbreviate(invoiceDetail.container,20)}</label>
			    </td>
			    <td>${invoiceDetail.reportingDate}</td>
			    <td>${invoiceDetail.postedDate}</td>
			    <c:choose>
				<c:when test="${fn:contains(invoiceDetail.accruedAmount, '-')}">
				    <td class="amount red">(${fn:replace(invoiceDetail.accruedAmount, '-','')})</td>
				</c:when>
				<c:otherwise>
				    <td class="amount black">${invoiceDetail.accruedAmount}</td>
				</c:otherwise>
			    </c:choose>
			    <td>${invoiceDetail.costCode}</td>
			    <td>${invoiceDetail.glAccount}</td>
			    <td>${invoiceDetail.transactionType}</td>
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
    </body>
</html>
