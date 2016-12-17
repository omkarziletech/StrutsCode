<%-- 
    Document   : searchFilters
    Created on : Sep 23, 2013, 5:15:20 PM
    Author     : Lakshmi Narayanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>      
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@taglib uri="http://cong.logiwareinc.com/string" prefix="str"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<c:choose>
    <c:when test="${arInquiryForm.toggled}">
	<c:set var="toggleStyle" value="block"/>
    </c:when>
    <c:otherwise>
	<c:set var="toggleStyle" value="none"/>
    </c:otherwise>
</c:choose>
<div style="display: ${toggleStyle}" class="filter-container">
    <table class="table" style="margin: 0; border-spacing: 0; border: none;">
	<tr>
	    <td style="width: 30%">
		<table class="display-table search-filters" style="border: none; margin: 0;">
		    <tr>
			<th colspan="3">Search By</th>
		    </tr>
		    <c:set var="searchFilters" value="Invoice/BL"/>
		    <c:set var="searchFilters" value="${searchFilters},Dock Receipt"/>
		    <c:set var="searchFilters" value="${searchFilters},Container Number"/>
		    <c:set var="searchFilters" value="${searchFilters},Voyage Number"/>
		    <c:set var="searchFilters" value="${searchFilters},Check Number"/>
		    <c:set var="searchFilters" value="${searchFilters},Check Amount"/>
		    <c:set var="searchFilters" value="${searchFilters},Invoice Amount"/>
		    <c:set var="searchFilters" value="${searchFilters},Invoice Balance"/>
		    <c:set var="searchFilters" value="${fn:split(searchFilters, ',')}"/>
		    <c:set var="amountFilters" value="${fn:split('Check Amount,Invoice Amount,Invoice Balance', ',')}"/>
		    <c:forEach var="searchFilter" items="${searchFilters}" varStatus="row">
			<c:choose>
			    <c:when test="${zebra eq 'odd'}">
				<c:set var="zebra" value="even"/>
			    </c:when>
			    <c:otherwise>
				<c:set var="zebra" value="odd"/>
			    </c:otherwise>
			</c:choose>
			<tr class="${zebra} label">
			    <td style="width: 50px !important;">
				<html:checkbox property="searchFilter" 
					       styleId="searchFilter${row.count}" value="${searchFilter}" styleClass="search-filter"/>
			    </td>
			    <td>
				<label for="searchFilter${row.count}">${searchFilter}</label>
			    </td>
			    <td>
				<c:choose>
				    <c:when test="${arInquiryForm.searchFilter ne searchFilter}">
					<c:set var="visibility" value="hidden"/>
				    </c:when>
				    <c:otherwise>
					<c:set var="visibility" value="visible"/>
				    </c:otherwise>
				</c:choose>
				<span class="search-value" style="visibility: ${visibility}">
				    <c:choose>
					<c:when test="${searchFilter eq 'Check Amount' 
							or searchFilter eq 'Invoice Amount' or searchFilter eq 'Invoice Balance'}">
					    <html:text property="fromAmount${row.count}" styleId="fromAmount${row.count}" styleClass="textbox width-52px fromAmount"/>
					    To
					    <html:text property="toAmount${row.count}" styleId="toAmount${row.count}" styleClass="textbox width-52px toAmount"/>
					</c:when>
					<c:otherwise>
					    <html:text property="searchValue${row.count}" styleId="searchValue${row.count}" styleClass="textbox"/>
					</c:otherwise>
				    </c:choose>
				</span>
			    </td>
			</tr>
		    </c:forEach>
		</table>
	    </td>
	    <td style="width: 40%">
		<table class="display-table" style="border: none; margin: 0;">
		    <tr>
			<th colspan="3">Search By Date</th>
		    </tr>
		    <tr class="label odd">
			<td style="width: 200px !important;">
			    <span class="float-left">
				<html:radio property="searchDate" styleId="searchDate0" value="Invoice Date" styleClass="date-filter"/>
			    </span>
			    <span class="float-left">
				<label for="searchDate0">Invoice Date</label>
			    </span>
			</td>
			<td>From Date</td>
			<td>
			    <html:text property="fromDate" styleId="fromDate" styleClass="textbox"/>
			</td>
		    </tr>
		    <tr class="label even">
			<td>
			    <span class="float-left">
				<html:radio property="searchDate" styleId="searchDate1" value="Payment/Adjustment Date" styleClass="date-filter"/>
			    </span>
			    <span class="float-left">
				<label for="searchDate1">Payment/Adjustment Date</label>
			    </span>
			</td>
			<td>To Date</td>
			<td>
			    <html:text property="toDate" styleId="toDate" styleClass="textbox"/>
			</td>
		    </tr>
		    <tr>
			<td colspan="3">&nbsp;</td>
		    </tr>
		    <tr>
			<td colspan="3">&nbsp;</td>
		    </tr>
		    <tr>
			<td colspan="3">&nbsp;</td>
		    </tr>
		    <tr>
			<td colspan="3" class="align-center">
			    <input type="button" value="Search" class="button" onclick="search();" tabindex="-1"/>
			    <input type="button" value="Clear" class="button" onclick="clearFilters();" tabindex="-1"/>
			</td>
		    </tr>
		</table>
	    </td>
	    <td style="width: 30%">
		<table class="display-table" style="border: none; margin: 0;">
		    <tr>
			<th colspan="2">Show/Hide</th>
		    </tr>
		    <c:set var="showFilters" value="Open Invoices"/>
		    <c:set var="showFilters" value="${showFilters},Paid Invoices"/>
		    <c:set var="showFilters" value="${showFilters},NS Invoices"/>
		    <c:set var="showFilters" value="${showFilters},Credit Hold - Yes"/>
		    <c:set var="showFilters" value="${showFilters},Credit Hold - No"/>
		    <c:set var="showFilters" value="${showFilters},Payables"/>
		    <c:set var="showFilters" value="${showFilters},Accruals"/>
		    <c:set var="showFilters" value="${showFilters},Show Subsidiairy"/>
		    <c:set var="showFilters" value="${fn:split(showFilters, ',')}"/>
		    <c:forEach var="showFilter" items="${showFilters}" varStatus="row">
			<c:choose>
			    <c:when test="${zebra eq 'odd'}">
				<c:set var="zebra" value="even"/>
			    </c:when>
			    <c:otherwise>
				<c:set var="zebra" value="odd"/>
			    </c:otherwise>
			</c:choose>
			<tr class="${zebra} label">
			    <td style="width: 50px !important;">
				<html:multibox property="showFilters" styleId="showFilters${row.count}" value="${showFilter}" styleClass="show-filter"/>
			    </td>
			    <td>
				<span class="float-left">
				    <label for="showFilters${row.count}">${showFilter}</label>
				</span>
				<c:if test="${showFilter eq 'NS Invoices'}">
				    <span class="float-left margin-0-0-0-20">
					<html:checkbox property="nsInvoiceOnly" styleId="nsInvoiceOnly" value="true"/>
				    </span>
				</c:if>
			    </td>
			</tr>
		    </c:forEach>
		</table>
	    </td>
	</tr>
    </table>
</div>