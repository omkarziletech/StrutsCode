<%-- 
    Document   : charges
    Created on : Oct 13, 2013, 3:11:47 PM
    Author     : Lakshmi Narayanan
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN" "http://www.w3.org/TR/REC-html40/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>      
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://cong.logiwareinc.com/string" prefix="str"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<html>
    <head>
        <title>Charges</title>
	<link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
	<link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
	<script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.js"></script>
	<script type="text/javascript">
	    $(document).ready(function() {
		$("[title != '']").not("link").tooltip();
	    });
	</script>
    </head>
    <body>
	<div class="result-container">
	    <table width="100%" cellpadding="0" cellspacing="1" class="display-table" style="table-layout: fixed;">
		<thead>
		    <tr>
			<th class="width-100px">Customer #</th>
			<th class="width-200px">Customer Name</th>
			<th class="width-150px">BL #</th>
			<th class="width-125px">Invoice #</th>
			<th class="width-100px">Date</th>
			<th class="width-100px">Posted Date</th>
			<th class="width-100px">Subledger</th>
			<th class="width-100px">Charge Code</th>
			<th class="width-100px">GL Account</th>
			<th class="width-100px">Amount</th>
		    </tr>
		</thead>
		<tbody>
		    <c:set var="zebra" value="odd"/>
		    <c:set var="total" value="0"/>
		    <c:forEach var="row" items="${charges}">
			<tr class="${zebra}">
			    <td>${row.customerNumber}</td>
			    <td><span title="${row.customerName}">${str:abbreviate(row.customerName, 40)}</span></td>
			    <td>${row.blNumber}</td>
			    <td>${row.invoiceNumber}</td>
			    <td>${row.invoiceDate}</td>
			    <td>${row.postedDate}</td>
			    <td>${row.subledger}</td>
			    <td>${row.chargeCode}</td>
			    <td>${row.glAccount}</td>
			    <c:choose>
				<c:when test="${fn:contains(row.amount, '-')}">
				    <td class="amount red">(${fn:replace(row.amount, '-','')})</td>
				</c:when>
				<c:otherwise>
				    <td class="amount black">${row.amount}</td>
				</c:otherwise>
			    </c:choose>
			</tr>
			<fmt:parseNumber var="amount" type="number" value="${fn:replace(row.amount, ',', '')}"/>
			<c:set var="total" value="${total + amount}"/>
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
		<tfoot>
		    <tr>
			<td colspan="9" class="align-right">Total :&nbsp;</td>
			<fmt:formatNumber var="total" value="${total}" pattern="###,###,##0.00"/>
			<c:choose>
			    <c:when test="${fn:contains(total, '-')}">
				<td class="amount red">(${fn:replace(total, '-','')})</td>
			    </c:when>
			    <c:otherwise>
				<td class="amount black">${total}</td>
			    </c:otherwise>
			</c:choose>
		    </tr>
		</tfoot>
	    </table>
	</div>
    </body>
</html>