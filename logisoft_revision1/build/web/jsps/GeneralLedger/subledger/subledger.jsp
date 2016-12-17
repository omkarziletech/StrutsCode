<%-- 
    Document   : subledger
    Created on : Dec 19, 2012, 6:53:08 PM
    Author     : Lakshmi Narayanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Subledger</title>
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
	<script type="text/javascript" src="${path}/js/jquery/jquery.number.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery.caret.min.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery.date.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery.datepick.js"></script>
	<script type="text/javascript" src="${path}/autocompleter/js/jquery.autocomplete.js"></script>
        <script type="text/javascript" src="${path}/js/Accounting/GeneralLedger/subledger.js"></script>
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
	    <html:form action="/subledger?accessMode=${accessMode}" name="subledgerForm"
		       styleId="subledgerForm" type="com.logiware.accounting.form.SubledgerForm" scope="request" method="post">
		<html:hidden property="action" styleId="action"/>
		<html:hidden property="limit" styleId="limit"/>
		<html:hidden property="selectedPage" styleId="selectedPage"/>
		<html:hidden property="sortBy" styleId="sortBy"/>
		<html:hidden property="orderBy" styleId="orderBy"/>
		<html:hidden property="status" styleId="status"/>
		<c:if test="${not empty message}">
		    <div class="message">${message}</div>
		</c:if>
		<table class="table" style="margin: 0">
		    <tr>
			<th>Search Filters</th>
		    </tr>
		    <tr>
			<td>
			    <table class="table filter-container" style="border: none;margin: 0">
				<tr>
				    <td class="label">Subledger</td>
				    <td colspan="5">
					<html:select property="subledger" styleId="subledger" styleClass="dropdown width-125px">
					    <html:optionsCollection property="subledgers"/>
					</html:select>
				    </td>
				</tr>
				<tr>
				    <td class="label width-125px">GL Period</td>
				    <td>
					<html:text property="glPeriod" styleId="glPeriod" styleClass="textbox" maxlength="6"/>
				    </td>
				    <td class="label width-125px">From Date</td>
				    <td>
					<html:text property="fromDate" styleId="fromDate" styleClass="textbox" maxlength="10" title="From Date"/>
				    </td>
				    <td class="label width-125px">To Date</td>
				    <td>
					<html:text property="toDate" styleId="toDate" styleClass="textbox" maxlength="10" title="To Date"/>
				    </td>
				</tr>
				<tr>
				    <td class="label width-125px">Search By</td>
				    <td>
					<html:select property="searchBy" styleId="searchBy" styleClass="dropdown width-125px">
					    <html:optionsCollection property="searchByOptions"/>
					</html:select>
				    </td>
				    <td class="label width-125px">Search Value</td>
				    <td class="label">
					<c:choose>
					    <c:when test="${subledgerForm.searchBy == 'transaction_amt'}">
						<c:set var="searchByAmount" value="block"/>
						<c:set var="searchByValue" value="none"/>
						<c:set var="searchByGlAccount" value="none"/>
					    </c:when>
					    <c:when test="${subledgerForm.searchBy == 'gl_account_number'}">
						<c:set var="searchByGlAccount" value="block"/>
						<c:set var="searchByValue" value="none"/>
						<c:set var="searchByAmount" value="none"/>
					    </c:when>
					    <c:otherwise>
						<c:set var="searchByValue" value="block"/>
						<c:set var="searchByGlAccount" value="none"/>
						<c:set var="searchByAmount" value="none"/>
					    </c:otherwise>
					</c:choose>
					<div id="searchByValue" style="display: ${searchByValue}">
					    <c:choose>
						<c:when test="${not empty subledgerForm.searchValue}">
						    <html:text property="searchValue" styleId="searchValue" styleClass="textbox"/>
						</c:when>
						<c:otherwise>
						    <html:text property="searchValue" 
							       styleId="searchValue" styleClass="textbox readonly" readonly="true" tabindex="-1"/>
						</c:otherwise>
					    </c:choose>
					</div>
					<div id="searchByGlAccount" style="display: ${searchByGlAccount}">
					    <c:choose>
						<c:when test="${not empty subledgerForm.glAccount}">
						    <html:text property="glAccount" styleId="glAccount" styleClass="textbox"/>
						</c:when>
						<c:otherwise>
						    <html:text property="glAccount" 
							       styleId="glAccount" styleClass="textbox readonly" readonly="true" tabindex="-1"/>
						</c:otherwise>
					    </c:choose>
					</div>
					<div id="searchByAmount" style="display: ${searchByAmount}">
					    <html:text property="fromAmount" styleId="fromAmount" 
						       style="width: 60px" styleClass="textbox amount" maxlength="11"/> 
					    &nbsp;to&nbsp;
					    <html:text property="toAmount" styleId="toAmount"
						       style="width: 60px" styleClass="textbox amount" maxlength="11"/> 
					</div>
				    </td>
				    <td class="label width-125px">Show Posted</td>
				    <td class="label">
					<html:radio property="posted" styleId="postedYes" value="true"/>
					<label for="postedYes">Yes</label>
					<html:radio property="posted" styleId="postedNo" value="false"/>
					<label for="postedNo">No</label>
				    </td>
				</tr>
				<tr>
				    <td colspan="6" class="label align-center">
					<input type="button" value="Search" class="button" onclick="search();" tabindex="-1"/>
					<input type="button" value="Clear" class="button" onclick="clearAll();" tabindex="-1"/>
					<input type="button" value="Post" class="button" onclick="validatePost();" tabindex="-1"/>
					<input type="button" value="Export to Excel" class="button" onclick="createExcel();" tabindex="-1"/>
					<span>
					    <html:checkbox property="accruals" styleId="accruals" value="true" style="vertical-align: bottom;"/>
					    Include Accruals
					</span>
				    </td>
				</tr>
			    </table>
			</td>
		    </tr>
		    <tr>
			<th>List of Results</th>
		    </tr>
		    <tr>
			<td>
			    <div id="results">
				<c:import url="/jsps/GeneralLedger/subledger/subledgerResults.jsp"/>
			    </div>
			</td>
		    </tr>
		</table>
	    </html:form>
	    <div id="glAccounts" class="static-popup" style="display: none;width: 800px;height: 450px;"></div>
	</div>
    </body>
</html>