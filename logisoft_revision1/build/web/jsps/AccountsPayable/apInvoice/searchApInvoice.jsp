<%-- 
    Document   : searchApInvoice
    Created on : Jun 22, 2012, 1:21:29 PM
    Author     : Lakshmi Naryanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>AP Invoice</title>
	<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
	<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
	    <script type="text/javascript">
	    var path = "${path}";
	</script>
	<link type="text/css" rel="stylesheet" href="${path}/css/jquery.autocomplete.css"/>
	<link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
	<link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
	<link type="text/css" rel="stylesheet" href="${path}/css/cal/skins/aqua/theme.css" title="Aqua"/>
	<link type="text/css" rel="stylesheet" href="${path}/css/cal/calendar-win2k-cold-2.css" title="win2k-cold-2" media="all"/>
	<link type="text/css" rel="stylesheet" href="${path}/js/lightbox/lightbox.css"/>
        <%@include file="../../../WEB-INF/jspf/jquery.jspf" %>
        <script type="text/javascript" src="${path}/js/Accounting/AccountsPayable/apInvoice.js"></script>
	<script type="text/javascript" src="${path}/js/caljs/calendar.js"></script>
	<script type="text/javascript" src="${path}/js/caljs/lang/calendar-en.js"></script>
	<script type="text/javascript" src="${path}/js/caljs/calendar-setup.js"></script>
	<script type="text/javascript" src="${path}/js/lightbox/lightbox.v2.3.jquery.js"></script>
    </head>
    <body>
	<div id="body-container">
	    <%@include file="../../preloader.jsp"%>
	    <div id="message" class="message" style="width: 100%; text-align: center;"></div>
	    <html:form action="/apInvoice" name="apInvoiceForm"
		       styleId="apInvoiceForm" type="com.logiware.accounting.form.ApInvoiceForm" scope="request" method="post">
		<html:hidden property="id" styleId="id"/>
		<html:hidden property="action" styleId="action"/>
		<html:hidden property="recurring" styleId="recurring"/>
		<html:hidden property="limit" styleId="limit"/>
		<html:hidden property="selectedPage" styleId="selectedPage"/>
		<html:hidden property="selectedRows" styleId="selectedRows"/>
		<html:hidden property="sortBy" styleId="sortBy"/>
		<html:hidden property="orderBy" styleId="orderBy"/>
		<div class="search-filters">
		    <table class="table">
			<thead>
			    <tr>
				<th colspan="8">
				    <c:choose>
					<c:when test="${apInvoiceForm.recurring}">Recurring Invoice Search</c:when>
					<c:otherwise>AP Invoice Search</c:otherwise>
				    </c:choose>
				</th>
			    </tr>
			</thead>
			<tbody style="vertical-align: top">
			    <tr>
				<td class="label width-150px">Vendor Name</td>
				<td>
				    <html:text property="vendorName" styleId="vendorName" styleClass="textbox"/>
				    <input type="hidden" name="vendorNameCheck" id="vendorNameCheck" value="${apInvoiceForm.vendorName}" class="hidden"/>
				</td>
				<td class="label width-150px">Vendor Number</td>
				<td>
				    <html:text property="vendorNumber" styleId="vendorNumber" styleClass="textbox readonly" readonly="true" tabindex="-1"/>
				</td>
				<c:choose>
				    <c:when test="${apInvoiceForm.recurring}">
					<td class="label width-150px">Invoice Amount</td>
					<td colspan="3">
					    <html:text property="invoiceAmount" styleId="invoiceAmount" styleClass="textbox amount" maxlength="13"/>
					</td>
				    </c:when>
				    <c:otherwise>
					<td class="label width-150px">Invoice Number</td>
					<td>
					    <html:text property="invoiceNumber" styleId="invoiceNumber" styleClass="textbox" maxlength="50"/>
					</td>
					<td class="label width-150px">Choose Status</td>
					<td>
					    <html:select property="status" styleId="status" styleClass="dropdown">
						<html:option value="">Select</html:option>
						<html:option value="I">In Progress</html:option>
						<html:option value="AP">Posted to AP</html:option>
					    </html:select>
					</td>
				    </c:otherwise>
				</c:choose>
			    </tr>
			    <tr>
				<td colspan="8" align="center">
				    <input type="button" value="Add" class="button" onclick="addInvoice()" tabindex="-1"/>
				    <input type="button" value="Search" class="button" onclick="search()" tabindex="-1"/>
				    <input type="button" value="Clear" class="button" onclick="gotoSearch('${apInvoiceForm.recurring}')" tabindex="-1"/>
				</td>
			    </tr>
			</tbody>
		    </table>
		</div>
		<div id="invoices-container">
		    <table class="table add">
			<thead>
			    <tr>
				<th>List of Invoices</th>
			    </tr>
			</thead>
			<tbody style="vertical-align: top">
			    <tr>
				<td>
				    <c:choose>
					<c:when test="${not empty apInvoiceForm.invoices}">
					    <div align="right" class="table-banner page-banner" style="padding-right: 15px;">
						<div style="float:right">
						    <div style="float:left">
							<c:choose>
							    <c:when test="${apInvoiceForm.totalRows>apInvoiceForm.selectedRows}">
								${apInvoiceForm.selectedRows} out of ${apInvoiceForm.totalRows} invoices displayed.
							    </c:when>
							    <c:when test="${apInvoiceForm.selectedRows>1}">${apInvoiceForm.selectedRows} invoices displayed.</c:when>
							    <c:otherwise>1 invoice displayed.</c:otherwise>
							</c:choose>
						    </div>
						    <c:if test="${apInvoiceForm.totalPages>1 && apInvoiceForm.selectedPage>1}">
							<a title="First page" href="javascript: gotoPage('1')">
							    <img alt="First" src="${path}/images/first.png"/>
							</a>
							<a title="Previous page" href="javascript: gotoPage('${apInvoiceForm.selectedPage-1}')">
							    <img alt="Previous" src="${path}/images/prev.png"/>
							</a>
						    </c:if>
						    <c:if test="${apInvoiceForm.totalPages>1}">
							<select id="selectedPageNo" class="dropdown" style="float:left;">
							    <c:forEach begin="1" end="${apInvoiceForm.totalPages}" var="selectedPage">
								<c:choose>
								    <c:when test="${apInvoiceForm.selectedPage!=selectedPage}">
									<option value="${selectedPage}">${selectedPage}</option>
								    </c:when>
								    <c:otherwise>
									<option value="${selectedPage}" selected>${selectedPage}</option>
								    </c:otherwise>
								</c:choose>
							    </c:forEach>
							</select>
							<a href="javascript: gotoSelectedPage()">
							    <img alt="Go" src="${path}/images/go.jpg"/>
							</a>
						    </c:if>
						    <c:if test="${apInvoiceForm.totalPages>apInvoiceForm.selectedPage}">
							<a title="Next page" href="javascript: gotoPage('${apInvoiceForm.selectedPage+1}')">
							    <img alt="First" src="${path}/images/next.png"/>
							</a>
							<a title="Last page" href="javascript: gotoPage('${apInvoiceForm.totalPages}')">
							    <img alt="Previous" src="${path}/images/last.png"/>
							</a>
						    </c:if>
						</div>
					    </div>
					    <div class="search-results">
						<table width="100%" cellpadding="0" cellspacing="1" class="display-table">
						    <thead>
							<tr>
							    <c:choose>
								<c:when test="${apInvoiceForm.orderBy=='desc'}"><c:set var="orderBy" value="asc"/></c:when>
								<c:otherwise><c:set var="orderBy" value="desc"/></c:otherwise>
							    </c:choose>
							    <th><a href="javascript:doSort('tp.acct_name','${orderBy}')">Vendor Name</a></th>
							    <th><a href="javascript:doSort('tp.acct_no','${orderBy}')">Vendor Number</a></th>
							    <c:choose>
								<c:when test="${apInvoiceForm.recurring}">
								    <th><a href="javascript:doSort('inv.invoice_amount','${orderBy}')">Invoice Amount</a></th>
								</c:when>
								<c:otherwise>
								    <th><a href="javascript:doSort('inv.invoice_number','${orderBy}')">Invoice Number</a></th>
								    <th><a href="javascript:doSort('inv.date','${orderBy}')">Invoice Date</a></th>
								    <th><a href="javascript:doSort('inv.invoice_amount','${orderBy}')">Invoice Amount</a></th>
								    <th><a href="javascript:doSort('inv.status','${orderBy}')">Status</a></th>
								</c:otherwise>
							    </c:choose>
							    <th>Action</th>
							</tr>
						    </thead>
						    <tbody>
							<c:set var="zebra" value="odd"/>
							<c:forEach var="invoice" items="${apInvoiceForm.invoices}">
							    <tr class="${zebra}">
								<td title="${invoice.vendorName}">
								    <c:choose>
									<c:when test="${fn:length(invoice.vendorName)>30}">
									    ${fn:substring(invoice.vendorName,0,30)}...
									</c:when>
									<c:otherwise>${invoice.vendorName}</c:otherwise>
								    </c:choose>
								</td>
								<td>${invoice.vendorNumber}</td>
								<c:choose>
								    <c:when test="${apInvoiceForm.recurring}">
									<c:choose>
									    <c:when test="${fn:startsWith(invoice.invoiceAmount,'-')}">
										<td style="text-align:right;" class="red">(${fn:replace(invoice.invoiceAmount,'-','')})</td>
									    </c:when>
									    <c:otherwise>
										<td style="text-align:right;">${invoice.invoiceAmount}</td>
									    </c:otherwise>
									</c:choose>
								    </c:when>
								    <c:otherwise>
									<td>${invoice.invoiceNumber}</td>
									<td>${invoice.invoiceDate}</td>
									<c:choose>
									    <c:when test="${fn:startsWith(invoice.invoiceAmount,'-')}">
										<td style="text-align:right;" class="red">(${fn:replace(invoice.invoiceAmount,'-','')})</td>
									    </c:when>
									    <c:otherwise>
										<td style="text-align:right;">${invoice.invoiceAmount}</td>
									    </c:otherwise>
									</c:choose>
									<td>${invoice.status}</td>
								    </c:otherwise>
								</c:choose>
								<td>
								    <c:choose>
									<c:when test="${invoice.status=='Posted to AP'}">
									    <img alt="View Invoice" title="View Invoice"
										 src="${path}/images/more_details.png" onclick="editInvoice('${invoice.id}')"/>
									</c:when>
									<c:otherwise>
									    <img alt="Edit Invoice" title="Edit Invoice"
										 src="${path}/img/icons/edit.gif" onclick="editInvoice('${invoice.id}')"/>
									    <img alt="Delete Invoice" title="Delete Invoice"
										 src="${path}/images/trash.png" onclick="deleteInvoice('${invoice.id}')"/>
									</c:otherwise>
								    </c:choose>
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
					    <div class="table-banner green" style="background-color: #D1E6EE;">No Invoices found</div>
					</c:otherwise>
				    </c:choose>
				</td>
			    </tr>
			</tbody>
		    </table>
		</div>
	    </html:form>
	</div>
    </body>
</html>
