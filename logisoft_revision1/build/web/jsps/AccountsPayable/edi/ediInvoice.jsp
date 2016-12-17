<%-- 
    Document   : ediInvoice
    Created on : Apr 20, 2012, 12:19:51 AM
    Author     : Lakshmi Naryanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Edi Invoice</title>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
        <%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
	<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
	    <script type="text/javascript">
		var path = "${path}";
	</script>
        <%@include file="../../../WEB-INF/jspf/jquery.jspf" %>
        <script type="text/javascript" src="${path}/js/Accounting/AccountsPayable/ediInvoice.js"></script>
	<link type="text/css" rel="stylesheet" href="${path}/css/jquery.autocomplete.css"/>
	<link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
	<link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
    </head>
    <body>
	<div class="body-container">
	    <%@include file="../../preloader.jsp"%>
	    <c:set var="accessMode" value="1"/>
	    <c:set var="canEdit" value="true" scope="request"/>
	    <c:if test="${param.accessMode==0}">
		<c:set var="accessMode" value="0"/>
		<c:set var="canEdit" value="false" scope="request"/>
	    </c:if>
	    <c:if test="${not empty param.message}">
		<c:set var="message" value="${param.message}"/>
	    </c:if>
	    <c:if test="${not empty message}">
		<div class="message">${message}</div>
	    </c:if>
	    <html:form action="/ediInvoice?accessMode=${accessMode}" name="ediInvoiceForm"
		       styleId="ediInvoiceForm" type="com.logiware.accounting.form.EdiInvoiceForm" scope="request" method="post">
		<html:hidden property="action" styleId="action"/>
		<html:hidden property="limit" styleId="limit"/>
		<html:hidden property="selectedPage" styleId="selectedPage"/>
		<html:hidden property="selectedRows" styleId="selectedRows"/>
		<html:hidden property="sortBy" styleId="sortBy"/>
		<html:hidden property="orderBy" styleId="orderBy"/>
		<table class="table search-filters">
		    <thead>
			<tr>
			    <th colspan="8">Search Filters</th>
			</tr>
		    </thead>
		    <tbody>
			<tr>
			    <td class="label">Vendor Name</td>
			    <td>
				<html:text property="vendorName" styleId="vendorName" styleClass="textbox"/>
				<input type="hidden" name="vendorNameCheck" id="vendorNameCheck" value="${ediInvoiceForm.vendorName}" class="hidden"/>
			    </td>
			    <td class="label">Vendor Number</td>
			    <td>
				<html:text property="vendorNumber" styleId="vendorNumber" styleClass="textbox readonly" readonly="true" tabindex="-1"/>
			    </td>
			    <td class="label">Invoice Number</td>
			    <td>
				<html:text property="invoiceNumber" styleId="invoiceNumber" styleClass="textbox"/>
			    </td>
			    <td class="label">Status</td>
			    <td>
				<html:select property="status" styleId="status" styleClass="dropdown">
				    <html:option value="">All</html:option>
				    <html:option value="EDI Open">Open</html:option>
				    <html:option value="EDI Dispute">Dispute</html:option>
				    <html:option value="EDI In Progress">In Progress</html:option>
				    <html:option value="EDI Ready To Post">Ready To Post</html:option>
				    <html:option value="EDI Ready To Post / Fully Mapped">Ready To Post / Fully Mapped</html:option>
				    <html:option value="EDI Posted To AP">Posted To AP</html:option>
				    <html:option value="EDI Duplicate">Duplicate</html:option>
				    <html:option value="EDI Archive">Archive</html:option>
				</html:select>
			    </td>
			</tr>
			<tr>
			    <td colspan="8" align="center">
				<input type="button" value="Search" class="button" onclick="search()"/>
				<input type="button" value="Clear" class="button" onclick="refresh()"/>
				<input type="button" value="Show Reject Logs" class="button" onclick="showLogs()"/>
				<input type="button" value="Update EDI Codes" class="button" onclick="updateEdiCode()"/>
				<c:if test="${loginuser.loginName == 'LAKSHH'}">
				    <input type="button" value="Attach Invoices" class="button" onclick="attachInvoices()"/>
				</c:if>
			    </td>
			</tr>
		    </tbody>
		</table>
		<table class="table">
		    <thead>
			<tr>
			    <th>List of Invoices</th>
			</tr>
		    </thead>
		    <tbody>
			<tr>
			    <td><c:import url="/jsps/AccountsPayable/edi/ediInvoiceResults.jsp"/></td>
			</tr>
		    </tbody>
		</table>
	    </html:form>
	</div>
    </body>
    <div class="static-popup" id="changeVendor" style="width: 420px;height: 60px;display: none;">
	<c:import url="/jsps/AccountsPayable/edi/ediVendor.jsp"/>
	<input type="hidden" id="invoiceId" value=""/>
    </div>
</html>
