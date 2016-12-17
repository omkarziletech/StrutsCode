<%--
    Document   : massAdjustment
    Created on : Oct 30, 2012, 12:09:05 am
    Author     : Lakshmi Naryanan
--%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<table class="table" style="margin: 0;border: none;border-spacing: 0;">
    <thead>
	<tr>
	    <th>
		<div class="float-left">Mass Adjustment</div>
		<div class="float-right">
		    <a href="javascript: closeMassAdjustment()">
			<img alt="Close Mass Adjustment" src="${path}/images/icons/close.png"/>
		    </a>
		</div>
	    </th>
	</tr>
    </thead>
    <tbody>
	<tr>
	    <td>
		<div class="result-container" style="height:275px;">
		    <table class="display-table">
			<thead>
			    <tr>
				<th>Customer Name</th>
				<th>Customer Number</th>
				<th>Invoice/BL</th>
				<th>Balance</th>
				<th>Suffix</th>
				<th>Action</th>
			    </tr>
			</thead>
			<tbody>
			    <c:forEach var="arInvoice" items="${noSuffixInvoices}">
				<c:choose>
				    <c:when test="${zebra=='odd'}">
					<c:set var="zebra" value="even"/>
				    </c:when>
				    <c:otherwise>
					<c:set var="zebra" value="odd"/>
				    </c:otherwise>
				</c:choose>
				<tr class="${zebra}">
				    <td>${arInvoice.custName}</td>
				    <td>${arInvoice.custNo}</td>
				    <td>
					<c:choose>
					    <c:when test="${not empty arInvoice.billLaddingNo}">
						${arInvoice.billLaddingNo}
					    </c:when>
					    <c:otherwise>
						${arInvoice.invoiceNumber}
					    </c:otherwise>
					</c:choose>
				    </td>
				    <td class="amount">
					<c:choose>
					    <c:when test="${arInvoice.balance<0.00}">
						<div class="red">
						    (<fmt:formatNumber value="${-arInvoice.balance}" pattern="0.00"/>)
						</div>
					    </c:when>
					    <c:otherwise>
						<fmt:formatNumber value="${arInvoice.balance}" pattern="0.00"/>
					    </c:otherwise>
					</c:choose>
				    </td>
				    <td>
					<input type="text" class="suffix textbox width-35px" value="" maxlength="3"/>
				    </td>
				    <td>
					<img alt="Adjust" src="${path}/img/icons/palette_connection16.gif"
					     title="Adjust" onclick="saveAdjustment('${arInvoice.transactionId}',this)"/>
				    </td>
				</tr>
			    </c:forEach>
			</tbody>
		    </table>
		</div>
	    </td>
	</tr>
    </tbody>
</table>
