<%-- 
    Document   : invoicePool
    Created on : Mar 7, 2013, 7:30:42 PM
    Author     : Lakshmi Narayanan
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Misc Invoice Pool</title>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>   
	<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
	<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
	    <script type="text/javascript">
		var path = "${path}";
	</script>
	<link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
	<link type="text/css" rel="stylesheet" href="${path}/css/jquery.autocomplete.css"/>
	<link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
	<link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.datepick.css"/>
	<script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery-ext.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery.date.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery.datepick.js"></script>
	<script type="text/javascript" src="${path}/autocompleter/js/jquery.autocomplete.js"></script>
	<script type="text/javascript" src="${path}/js/fcl/invoicePool.js"></script>
	<c:set var="accessMode" value="1"/>
	<c:set var="writeMode" value="true"/>
	<c:if test="${param.accessMode==0}">
	    <c:set var="accessMode" value="0"/>
	    <c:set var="writeMode" value="false"/>
	</c:if>
    </head>
    <body>
        <%@include file="../../jsps/preloader.jsp"%>
	<html:form action="/fcl/invoicePool?accessMode=${accessMode}" name="invoicePoolForm"
		   styleId="invoicePoolForm" type="com.logiware.fcl.form.InvoicePoolForm" scope="request" method="post">
	    <html:hidden property="action" styleId="action"/>
	    <html:hidden property="limit" styleId="limit"/>
	    <html:hidden property="selectedPage" styleId="selectedPage"/>
	    <html:hidden property="sortBy" styleId="sortBy"/>
	    <html:hidden property="orderBy" styleId="orderBy"/>
	    <html:hidden property="importFile" styleId="importFile"/>
	    <c:if test="${not empty message}">
		<div class="message">${message}</div>
	    </c:if>
	    <table class="table" style="margin: 0">
		<tr>
		    <th colspan="8">Search Criteria</th>
		</tr>
		<tr>
		    <td class="label">Show Invoices</td>
		    <td>
			<html:select property="status" styleId="status" styleClass="dropdown" style="text-transform:none;min-width: 127px;">
			    <html:option value="">Select</html:option>
			    <html:option value="Posted">Posted</html:option>
			    <html:option value="Un Posted">Un Posted</html:option>
			</html:select>
		    </td>
		    <td class="label">Order By</td>
		    <td>
			<html:select property="orderByField" styleId="orderByField" 
				     styleClass="dropdown" style="text-transform:none;min-width: 127px;">
			    <html:option value="date">Created Date</html:option>
			    <html:option value="invoice_number">Invoice Number</html:option>
			</html:select>
		    </td>
		    <td class="label">From Date</td>
		    <td>
			<html:text property="fromDate" styleId="fromDate" styleClass="textbox" maxlength="10"/>
		    </td>
		    <td class="label">To Date</td>
		    <td>
			<html:text property="toDate" styleId="toDate" styleClass="textbox" maxlength="10"/>
		    </td>
		</tr>
		<tr>
		    <td class="label">Invoice Number</td>
		    <td>
			<html:text property="invoiceNumber" styleId="invoiceNumber" styleClass="textbox"/>
		    </td>
		    <td class="label">File Number</td>
		    <td>
			<html:text property="fileNumber" styleId="fileNumber" styleClass="textbox"/>
		    </td>
		    <td class="label">Customer Name</td>
		    <td>
			<html:text property="customerName" styleId="customerName" styleClass="textbox"/>
			<html:hidden property="customerNumber" styleId="customerNumber"/>
			<input type="hidden" name=customerNameCheck" id="customerNameCheck" value="${invoicePoolForm.customerName}"/>
		    </td>
		</tr>
		<tr>
		    <td colspan="8" class="align-center">
			<input type="button" value="Search" class="button" onclick="search();"/>
			<input type="button" value="Reset" class="button" onclick="resetAll();"/>
		    </td>
		</tr>
		<tr>
		    <th colspan="8">Invoice Pool List</th>
		</tr>
		<tr>
		    <td colspan="8">
			<div id="results">
			    <c:import url="/jsp/fcl/invoicePoolResults.jsp"/>
			</div>
		    </td>
		</tr>
	    </table>
	</html:form>
    </body>
</html>
