<%-- 
    Document   : apInquiry
    Created on : Sep 26, 2012, 7:21:10 PM
    Author     : Lakshmi Naryanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>AP Inquiry</title>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>   
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
        <script type="text/javascript" src="${path}/js/Accounting/AccountsPayable/apInquiry.js"></script>
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
	<div id="body-container">
	    <html:form action="/apInquiry?accessMode=${accessMode}" name="apInquiryForm"
		       styleId="apInquiryForm" type="com.logiware.accounting.form.ApInquiryForm" scope="request" method="post">
		<html:hidden property="action" styleId="action"/>
		<html:hidden property="limit" styleId="limit"/>
		<html:hidden property="selectedPage" styleId="selectedPage"/>
		<html:hidden property="sortBy" styleId="sortBy"/>
		<html:hidden property="orderBy" styleId="orderBy"/>
		<html:hidden property="status" styleId="status"/>
		<html:hidden property="toggled" styleId="toggled"/>
		<table class="table" style="margin: 0">
		    <tr>
			<td class="label">
			    <label>Vendor Name</label>
			    <html:text property="vendorName" styleId="vendorName" styleClass="textbox"/>
			    <html:text property="vendorNumber" 
				       styleId="vendorNumber" styleClass="textbox readonly" readonly="true" tabindex="-1" maxlength="10"/>
			    <input type="hidden" name="vendorNameCheck" 
				   id="vendorNameCheck" value="${apInquiryForm.vendorName}" class="hidden" maxlength="50"/>
			    <c:if test="${writeMode}">
				<img src="${path}/images/icons/trading_partner.png" title="Go to Trading Partner"
				     class="trading-partner" onclick="gotoTradingPartner('vendorName','vendorNumber');"/>
			    </c:if>
			    <html:checkbox title="Show Disabled" property="disabled" styleId="disabled" value="true" style="vertical-align: top;"/>
			    <input type="button" class="button" value="Search" onclick="searchAll();" tabindex="-1"/>
			    <input type="button" class="button" value="Clear" onclick="clearAll();" tabindex="-1"/>
			    <input type="button" class="button" value="Print" onclick="createPdf();" tabindex="-1"/>
			    <input type="button" class="button" value="Export" onclick="createExcel();" tabindex="-1"/>
			</td>
		    </tr>
		    <c:if test="${not empty apInquiryForm.vendor}">
			<tr>
			    <th class="toggle" onclick="toggle('vendor-container');">Vendor Details</th>
			</tr>
			<tr>
			    <td>
				<div class="vendor-container">
				    <table class="table" style="border: none;margin: 0">
					<tr>
					    <td width="40%" style="vertical-align: top">
						<table class="label" cellspacing="2">
						    <tr>
							<td class="align-right bold">Vendor Name :</td>
							<td>${apInquiryForm.vendor.vendorName}</td>
						    </tr>
						    <tr>
							<td class="align-right bold">Address :</td>
							<c:set var="newline" value="\n"/>
							<td>${fn:replace(apInquiryForm.vendor.address,newline,'<br/>')}</td>
						    </tr>
						    <tr>
							<td class="align-right bold">Contact :</td>
							<td>${apInquiryForm.vendor.contact}</td>
						    </tr>
						    <tr>
							<td class="align-right bold">Phone :</td>
							<td>${apInquiryForm.vendor.phone}</td>
						    </tr>
						    <tr>
							<td class="align-right bold">Fax :</td>
							<td>${apInquiryForm.vendor.fax}</td>
						    </tr>
						    <tr>
							<td class="align-right bold">Email :</td>
							<td>${apInquiryForm.vendor.email}</td>
						    </tr>
						</table>
					    </td>
					    <td width="30%" style="vertical-align: top">
						<table class="label" cellspacing="2">
						    <tr>
							<td class="align-right bold">Vendor Number :</td>
							<td>${apInquiryForm.vendor.vendorNumber}</td>
						    </tr>
						    <tr>
							<td class="align-right bold">Credit Term :</td>
							<td>${apInquiryForm.vendor.creditTerm}</td>
						    </tr>
						    <tr>
							<td class="align-right bold">Credit Limit :</td>
							<c:choose>
							    <c:when test="${fn:contains(apInquiryForm.vendor.creditLimit,'-')}">
								<td class="align-right red">(${fn:replace(apInquiryForm.vendor.creditLimit,'-','')})</td>
							    </c:when>
							    <c:otherwise>
								<td class="align-right black">${apInquiryForm.vendor.creditLimit}</td>
							    </c:otherwise>
							</c:choose>
						    </tr>
						    <tr>
							<td class="align-right bold">OutStanding Receivables :</td>
							<c:choose>
							    <c:when test="${fn:contains(apInquiryForm.vendor.arAmount,'-')}">
								<td class="align-right red">(${fn:replace(apInquiryForm.vendor.arAmount,'-','')})</td>
							    </c:when>
							    <c:otherwise>
								<td class="align-right black">${apInquiryForm.vendor.arAmount}</td>
							    </c:otherwise>
							</c:choose>
						    </tr>
						    <tr>
							<td class="align-right bold">Net Payable Amount :</td>
							<c:choose>
							    <c:when test="${fn:contains(apInquiryForm.vendor.netAmount,'-')}">
								<td class="align-right red">(${fn:replace(apInquiryForm.vendor.netAmount,'-','')})</td>
							    </c:when>
							    <c:otherwise>
								<td class="align-right black">${apInquiryForm.vendor.netAmount}</td>
							    </c:otherwise>
							</c:choose>
						    </tr>
						</table>
					    </td>
					    <td width="30%" style="vertical-align: top">
						<table class="label" cellspacing="2">
                                                    <tr>
							<td class="align-right bold">ECU Designation :</td>
							<td>${apInquiryForm.vendor.ecuDesignation}</td>
						    </tr>
						    <tr>
							<td class="align-right bold">Sub Type :</td>
							<td>${apInquiryForm.vendor.subType}</td>
						    </tr>
						    <tr>
							<td class="align-right bold">Current :</td>
							<c:choose>
							    <c:when test="${fn:contains(apInquiryForm.vendor.age30Amount,'-')}">
								<td class="align-right red">(${fn:replace(apInquiryForm.vendor.age30Amount,'-','')})</td>
							    </c:when>
							    <c:otherwise>
								<td class="align-right black">${apInquiryForm.vendor.age30Amount}</td>
							    </c:otherwise>
							</c:choose>
						    </tr>
						    <tr>
							<td class="align-right bold">31-60 Days :</td>
							<c:choose>
							    <c:when test="${fn:contains(apInquiryForm.vendor.age60Amount,'-')}">
								<td class="align-right red">(${fn:replace(apInquiryForm.vendor.age60Amount,'-','')})</td>
							    </c:when>
							    <c:otherwise>
								<td class="align-right black">${apInquiryForm.vendor.age60Amount}</td>
							    </c:otherwise>
							</c:choose>
						    </tr>
						    <tr>
							<td class="align-right bold">61-90 Days :</td>
							<c:choose>
							    <c:when test="${fn:contains(apInquiryForm.vendor.age90Amount,'-')}">
								<td class="align-right red">(${fn:replace(apInquiryForm.vendor.age90Amount,'-','')})</td>
							    </c:when>
							    <c:otherwise>
								<td class="align-right black">${apInquiryForm.vendor.age90Amount}</td>
							    </c:otherwise>
							</c:choose>
						    </tr>
						    <tr>
							<td class="align-right bold">&gt;90 Days :</td>
							<c:choose>
							    <c:when test="${fn:contains(apInquiryForm.vendor.age91Amount,'-')}">
								<td class="align-right red">(${fn:replace(apInquiryForm.vendor.age91Amount,'-','')})</td>
							    </c:when>
							    <c:otherwise>
								<td class="align-right black">${apInquiryForm.vendor.age91Amount}</td>
							    </c:otherwise>
							</c:choose>
						    </tr>
						    <tr>
							<td class="align-right bold">Total :</td>
							<c:choose>
							    <c:when test="${fn:contains(apInquiryForm.vendor.apAmount,'-')}">
								<td class="align-right red">(${fn:replace(apInquiryForm.vendor.apAmount,'-','')})</td>
							    </c:when>
							    <c:otherwise>
								<td class="align-right black">${apInquiryForm.vendor.apAmount}</td>
							    </c:otherwise>
							</c:choose>
						    </tr>
						</table>
					    </td>
					</tr>
				    </table>
				</div>
			    </td>
			</tr>
		    </c:if>
		    <tr>
			<th class="toggle" onclick="toggle('filter-container');">Search Filters</th>
		    </tr>
		    <tr>
			<td>
			    <c:set var="toggleStyle" value="none"/>
			    <c:if test="${apInquiryForm.toggled}">
				<c:set var="toggleStyle" value="block"/>
			    </c:if>
			    <div style="display: ${toggleStyle}" class="filter-container">
				<table class="table" style="border: none;margin: 0">
				    <tr>
					<td class="label width-125px">Search By</td>
					<td>
					    <html:select property="searchBy" styleId="searchBy"
							 styleClass="dropdown width-125px" onchange="onchangeSearchBy(this);">
						<html:optionsCollection property="searchByOptions"/>
					    </html:select>
					</td>
					<td class="label width-125px">Search Value</td>
					<td class="label">
					    <c:set var="searchByValue" value="block"/>
					    <c:set var="searchByAmount" value="none"/>
					    <c:if test="${arInquiryForm.searchBy == 'invoice_amount' || arInquiryForm.searchBy == 'check_amount'}">
						<c:set var="searchByAmount" value="block"/>
						<c:set var="searchByValue" value="none"/>
					    </c:if>
					    <div id="searchByValue" style="display: ${searchByValue}">
						<c:choose>
						    <c:when test="${not empty apInquiryForm.searchValue}">
							<html:text property="searchValue" styleId="searchValue" styleClass="textbox"/>
						    </c:when>
						    <c:otherwise>
							<html:text property="searchValue" 
								   styleId="searchValue" styleClass="textbox readonly" readonly="true" tabindex="-1"/>
						    </c:otherwise>
						</c:choose>
						<html:checkbox property="fullSearch" styleId="fullSearch" title="Full Search"/>
					    </div>
					    <div id="searchByAmount" style="display: ${searchByAmount}">
						<html:text property="fromAmount" styleId="fromAmount" 
							   style="width: 60px" styleClass="textbox amount" maxlength="11"/> 
						&nbsp;to&nbsp;
						<html:text property="toAmount" styleId="toAmount"
							   style="width: 60px" styleClass="textbox amount" maxlength="11"/> 
					    </div>
					</td>
					<td class="label width-125px">Show Invoices</td>
					<td>
					    <html:select property="showInvoices" styleId="showInvoices" styleClass="dropdown width-125px">
						<html:optionsCollection property="showInvoicesOptions"/>
					    </html:select>
					</td>
					<td class="label width-125px">Show Accruals</td>
					<td>
					    <html:select property="showAccruals" styleId="showAccruals" styleClass="dropdown width-125px">
						<html:optionsCollection property="showAccrualsOptions"/>
					    </html:select>
					</td>
				    </tr>
				    <tr>
					<td class="label width-125px">From Date</td>
					<td>
					    <html:text property="fromDate" styleId="fromDate"
						       styleClass="textbox" maxlength="10" onchange="onchangeDate(this);"/>
					    <img src="${path}/img/CalendarIco.gif"
						 alt="" title="From Date" align="top" id="fromDateCalendar" class="calendar-img"/>
					</td>
					<td class="label width-125px">To Date</td>
					<td>
					    <html:text property="toDate" styleId="toDate" 
						       styleClass="textbox" maxlength="10" onchange="onchangeDate(this);"/>
					    <img src="${path}/img/CalendarIco.gif" 
						 alt="" title="To Date" align="top" id="toDateCalendar" class="calendar-img"/>
					</td>
					<td class="label width-125px">Search Date by</td>
					<td class="label">
					    <html:radio  property="searchDateBy" styleId="searchDateBy" value="invoice_date"/>Invoice Date
					    <html:radio  property="searchDateBy" styleId="searchDateBy" value="payment_date"/>Payment Date
					</td>
					<td class="label width-125px">Show AR</td>
					<td class="label">
					    <html:radio property="ar" styleId="ar" value="Y">Yes</html:radio>
					    <html:radio property="ar" styleId="ar" value="N">No</html:radio>
					</td>
				    </tr>
                                    <tr>
                                        <td class="label width-125px">Show Subsidiairy</td>
					<td class="label">
					    <html:radio property="showSubsidiairy" styleId="showSubsidiairy" value="Y">Yes</html:radio>
					    <html:radio property="showSubsidiairy" styleId="showSubsidiairy" value="N">No</html:radio>
					</td>
                                    </tr>
				    <tr>
					<td colspan="7" class="align-center">
					    <input type="button" value="Search" class="button" onclick="searchByFilters();" tabindex="-1"/>
					    <input type="button" value="Clear" class="button" onclick="clearFilters();" tabindex="-1"/>
					</td>
				    </tr>
				</table>
			    </div>
			</td>
		    </tr>
		    <tr>
			<th>List of Results</th>
		    </tr>
		    <tr>
			<td>
			    <div id="results">
				<c:import url="/jsps/AccountsPayable/apInquiry/apInquiryResults.jsp"/>
			    </div>
			</td>
		    </tr>
		</table>
	    </html:form>
	</div>
    </body>
</html>
