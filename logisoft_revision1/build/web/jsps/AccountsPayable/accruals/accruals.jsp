<%-- 
    Document   : accruals
    Created on : Jul 11, 2012, 7:21:10 PM
    Author     : Lakshmi Naryanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Accruals</title>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
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
        <script type="text/javascript" src="${path}/js/Accounting/AccountsPayable/accruals.js"></script>
	<script type="text/javascript" src="${path}/js/caljs/calendar.js"></script>
	<script type="text/javascript" src="${path}/js/caljs/lang/calendar-en.js"></script>
	<script type="text/javascript" src="${path}/js/caljs/calendar-setup.js"></script>
	<script type="text/javascript" src="${path}/js/lightbox/lightbox.v2.3.jquery.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery-te.min.js"></script>
	<link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery-te.css"/>
	<c:set var="accessMode" value="1"/>
        <c:set var="writeMode" value="true"/>
        <c:if test="${param.accessMode==0}">
            <c:set var="accessMode" value="0"/>
            <c:set var="writeMode" value="false"/>
        </c:if>
    </head>
    <body>
	<%@include file="../../preloader.jsp"%>
	<div id="message" class="message"></div>
	<div id="error" class="error"></div>
	<div id="autosave-notification" class="error float-right"></div>
	<div id="body-container">
	    <html:form action="/accruals?accessMode=${accessMode}" name="accrualsForm"
		       styleId="accrualsForm" type="com.logiware.accounting.form.AccrualsForm" scope="request" method="post">
		<html:hidden property="firstClear" styleId="firstClear"/>
		<html:hidden property="from" styleId="from"/>
		<html:hidden property="action" styleId="action"/>
		<html:hidden property="sortBy" styleId="sortBy"/>
		<html:hidden property="orderBy" styleId="orderBy"/>
		<html:hidden property="status" styleId="status"/>
		<html:hidden property="toggled" styleId="toggled"/>
		<html:hidden property="arIds" styleId="arIds"/>
		<html:hidden property="accrualIds" styleId="accrualIds"/>
		<html:hidden property="ediInvoiceNumber" styleId="ediInvoiceNumber"/>
		<div style="display: none" id="hiddenIds">
		    <html:hidden property="assignedArIds" styleId="assignedArIds"/>
		    <html:hidden property="disputedArIds" styleId="disputedArIds"/>
		    <html:hidden property="assignedAccrualIds" styleId="assignedAccrualIds"/>
		    <html:hidden property="disputedAccrualIds" styleId="disputedAccrualIds"/>
		    <html:hidden property="inactiveAccrualIds" styleId="inactiveAccrualIds"/>
		    <html:hidden property="activeAccrualIds" styleId="activeAccrualIds"/>
		    <html:hidden property="deleteAccrualIds" styleId="deleteAccrualIds"/>
		    <html:hidden property="undeleteAccrualIds" styleId="undeleteAccrualIds"/>
		</div>
		<html:hidden property="rowsOnly" styleId="rowsOnly"/>
		<table class="table" style="margin: 0">
		    <tr>
			<th>Add/Edit Invoice</th>
		    </tr>
		    <tr>
			<td>
			    <table class="table invoice-container" id="invoice-container" style="border: none;margin: 0">
				<tr>
				    <td class="label width-150px">Vendor Name</td>
				    <td>
					<html:text property="vendorName" styleId="vendorName" styleClass="textbox" maxlength="50"/>
					<input type="hidden" name="vendorNameCheck" 
					       id="vendorNameCheck" value="${accrualsForm.vendorName}" class="hidden" maxlength="50"/>
					<c:if test="${writeMode}">
					    <img src="${path}/images/icons/trading_partner.png"
						 alt="Go to Trading Parter" title="Go to Trading Partner"
						 class="trading-partner" onclick="gotoTradingPartner('vendorName','vendorNumber')"/>
					</c:if>
				    </td>
				    <td class="label width-150px">Vendor Number</td>
				    <td>
					<html:text property="vendorNumber" 
						   styleId="vendorNumber" styleClass="textbox readonly" readonly="true" tabindex="-1" maxlength="10"/>
					<input type="hidden" name="vendorNumberOld" 
					       id="vendorNumberOld" value="${accrualsForm.vendorNumber}" class="hidden" maxlength="10"/>
				    </td>
				    <td class="label width-150px">Invoice Number</td>
				    <td>
					<html:text property="invoiceNumber" styleId="invoiceNumber" styleClass="textbox" maxlength="100"/>
					<input type="hidden" name="invoiceNumberOld" 
					       id="invoiceNumberOld" value="${accrualForm.invoiceNumber}" maxlength="100"/>
				    </td>
				    <td class="label width-150px">
					<html:checkbox property="dispute" styleId="dispute"
						       value="true" tabindex="-1" onclick="doDispute()"/>Dispute
				    </td>
				</tr>
				<tr>
				    <td class="label width-150px">Invoice Amount</td>
				    <td>
					<html:text property="invoiceAmount" styleId="invoiceAmount" styleClass="textbox amount" maxlength="13"/>
				    </td>
				    <td class="label width-150px">Allocated Amount</td>
				    <td>
					<html:text property="allocatedAmount" 
						   styleId="allocatedAmount" styleClass="textbox readonly amount" readonly="true" tabindex="-1"/>
				    </td>
				    <td class="label width-150px">Remaining Amount</td>
				    <td>
					<html:text property="remainingAmount" 
						   styleId="remainingAmount" styleClass="textbox readonly amount" readonly="true" tabindex="-1"/>
				    </td>
				    <td class="label width-150px">
					<c:choose>
					    <c:when test="${accrualsForm.from=='EDI'}">
						<html:checkbox property="reject" styleId="reject"
							       value="true" tabindex="-1" disabled="true"/>Reject
					    </c:when>
					    <c:otherwise>
						<html:checkbox property="reject" styleId="reject"
							       value="true" tabindex="-1" onclick="doReject()"/>Reject
					    </c:otherwise>
					</c:choose>
				    </td>
				</tr>
				<tr>
				    <td class="label width-150px">Invoice Date</td>
				    <td>
					<html:text property="invoiceDate" styleId="invoiceDate" 
						   styleClass="textbox" maxlength="10" onchange="validateDate(this)"/>
					<img src="${path}/img/CalendarIco.gif" alt="Invoice Date"
					     title="Invoice Date" align="top" id="invoiceDateCalendar" class="calendar-img"/>
				    </td>
				    <td class="label width-150px">Terms</td>
				    <td>
					<html:text property="creditDesc" styleId="creditDesc"
						   styleClass="textbox readonly" readonly="true" tabindex="-1"/>
					<html:hidden property="creditTerm" styleId="creditTerm"/>
					<html:hidden property="creditId" styleId="creditId"/>
				    </td>
				    <td class="label width-150px">Due Date</td>
				    <td>
					<html:text property="dueDate" styleId="dueDate" styleClass="textbox readonly" readonly="true" tabindex="-1"/>
				    </td>
				    <td class="label width-150px">
					<html:checkbox property="auto" styleId="auto" value="true" tabindex="-1" onclick="initAutosave()"/>Auto Save
				    </td>
				</tr>
				<tr>
				    <td colspan="7" class="align-center">
					<c:if test="${accrualsForm.from!='EDI' && accrualsForm.from!='SSMasters' || accrualsForm.from=='SSMastersLcl' || accrualsForm.from=='SSMastersAll'}">
					    <input type="button" value="Clear" class="button" onclick="clearAll()" tabindex="-1"/>
					</c:if>
					<input type="button" value="Add Accrual" class="button" onclick="showAddAccrual()" tabindex="-1"/>
					<input type="button" value="Upload Invoice" class="button" onclick="uploadInvoice()" tabindex="-1"/>
					<input type="button" value="Print" class="button" onclick="createPdf()" tabindex="-1"/>
					<input type="button" value="Export" class="button" onclick="createExcel()" tabindex="-1"/>
					<c:if test="${roleDuty.inactivateAccruals}">
					    <input type="button" value="Inactivate Accruals"
						   class="button" onclick="showInactivateAccruals()" tabindex="-1"/>
					</c:if>
					<c:if test="${accrualsForm.from=='EDI' || accrualsForm.from=='SSMasters' || accrualsForm.from=='SSMastersLcl' || accrualsForm.from=='SSMastersAll'}">
					    <input type="button" value="Go Back" class="button" onclick="goBack()" tabindex="-1"/>
					    <input type="hidden" id="fromParams" name="fromParams" value="${fromParams}"/>
					</c:if>
				    </td>
				</tr>
			    </table>
			</td>
		    <tr>
			<th class="toggle" onclick="toggle();">Search Filters</th>
		    </tr>
		    <tr>
			<td>
			    <c:set var="toggleStyle" value="none"/>
			    <c:if test="${accrualForm.toggled}">
				<c:set var="toggleStyle" value="block"/>
			    </c:if>
			    <div id="toggleDiv" style="display: ${toggleStyle}" class="filter-container">
				<table class="table" style="border: none;margin: 0">
				    <tr>
					<td class="label width-150px">Vendor Name</td>
					<td>
					    <html:text property="searchVendorName" styleId="searchVendorName" styleClass="textbox" maxlength="50"/>
					    <input type="hidden" name="searchVendorNameCheck" 
						   id="searchVendorNameCheck" value="${accrualsForm.searchVendorName}" class="hidden"/>
					    <c:if test="${writeMode}">
						<img src="${path}/images/icons/trading_partner.png" 
						     alt="Go to Trading Parter"  title="Go to Trading Partner" 
						     class="trading-partner" onclick="gotoTradingPartner('searchVendorName','searchVendorNumber')"/>
					    </c:if>

					</td>
					<td class="label width-150px">Vendor Number</td>
					<td>
					    <html:text property="searchVendorNumber" 
						       styleId="searchVendorNumber" styleClass="textbox readonly" readonly="true" tabindex="-1"/>
					</td>
					<td class="label width-150px">Show Accruals</td>
					<td class="label">
					    <html:radio property="openAccruals" styleId="openAccruals" value="true">Open</html:radio>
					    <html:radio property="openAccruals" styleId="openAccruals" value="false">All</html:radio>
					</td>
					<td>&nbsp;</td>
				    </tr>
				    <tr>
					<td class="label width-150px">Search By</td>
					<td>
					    <html:select property="searchBy" styleId="searchBy" styleClass="dropdown" onchange="onchangeSearchBy()">
						<html:optionsCollection property="searchByTypes"/>
					    </html:select>
					</td>
					<td class="label width-150px">Search Value</td>
					<td>
					    <c:choose>
						<c:when test="${not empty accrualsForm.searchValue}">
						    <html:text property="searchValue" styleId="searchValue" styleClass="textbox"/>
						</c:when>
						<c:otherwise>
						    <html:text property="searchValue" 
							       styleId="searchValue" styleClass="textbox readonly" readonly="true" tabindex="-1"/>
						</c:otherwise>
					    </c:choose>
					</td>
					<td class="label width-150px">Show AR</td>
					<td class="label">
					    <html:radio property="ar" styleId="ar" value="Y">Yes</html:radio>
					    <html:radio property="ar" styleId="ar" value="N">No</html:radio>
					    <html:radio property="ar" styleId="ar" value="Only">Only</html:radio>
					</td>
					<td>&nbsp;</td>
				    </tr>
				    <tr>
					<td class="label">Hide Accruals older than</td>
					<td class="label width-150px" colspan="6">
					    <html:text property="hideAccruals" styleId="hideAccruals" styleClass="textbox width-30px"/>&nbsp;days
					</td>
				    </tr>
				    <tr>
					<td colspan="7" class="align-center">
					    <input type="button" value="Search"
						   class="button" onclick="searchByFilter()" tabindex="-1"/>
					    <input type="button" value="Clear" class="button" onclick="clearFilter()" tabindex="-1"/>
					</td>
				    </tr>
				</table>
			    </div>
			</td>
		    </tr>
		    <tr>
			<th>
			    <div class="float-left">List of Results</div>
			    <div class=" label float-right">
				Results per page
				<html:select property="limit" styleId="limit" 
					     styleClass="dropdown width-50px" onchange="validateLimit(this)">
				    <html:option value="25">25</html:option>
				    <html:option value="50">50</html:option>
				    <html:option value="100">100</html:option>
				    <html:option value="150">150</html:option>
				    <html:option value="200">200</html:option>
				    <html:option value="250">250</html:option>
				</html:select>
				<input type="button" value="Save" class="button" onclick="save()" tabindex="-1"/>
			    </div>
			</th>
		    </tr>
		    <tr>
			<td>
			    <div id="result-header" class="table-banner green padding-right" style="display: none;"></div>
			    <div id="results">
				<c:import url="/jsps/AccountsPayable/accruals/accrualsResults.jsp"/>
			    </div>
			</td>
		    </tr>
		</table>
		<div id="add-accrual-container" class="static-popup" style="display: none;width: 600px;height: 210px;">
		    <c:import url="/jsps/AccountsPayable/accruals/addAccrual.jsp"/>    
		</div>
		<div id="inactivate-container" class="static-popup" style="display: none;width: 600px;height: 144px;">
		    <c:import url="/jsps/AccountsPayable/accruals/inactivateAccruals.jsp"/>    
		</div>
		<div id="email" class="static-popup" style="display: none;width: 800px;height: 450px;">
		    <c:import url="/jsps/AccountsPayable/accruals/email.jsp"/>    
		</div>
		<div id="reject-comments" class="static-popup" style="display: none;width: 250px;height: 123px;">
		    <c:import url="/jsps/AccountsPayable/accruals/rejectComments.jsp"/>    
		</div>
	    </html:form>
	    <input type="hidden" name="userEmailAddress" id="userEmailAddress" value="${loginuser.email}"/>
	</div>
    </body>
</html>
