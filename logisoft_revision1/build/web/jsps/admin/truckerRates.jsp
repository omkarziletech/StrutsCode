<%-- 
    Document   : truckerRates
    Created on : 10 Sep, 2012, 6:07:45 PM
    Author     : Lakshmi Narayanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Trucker Rates</title>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
        <%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
	    <script type="text/javascript">
		var path = "${path}";
        </script>
        <link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
	<link type="text/css" rel="stylesheet" href="${path}/css/jquery.autocomplete.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/fileUpload/enhanced.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery-ext.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.js"></script>
        <script type="text/javascript" src="${path}/js/fileUpload/jquery.fileinput.js"></script>
	<script type="text/javascript" src="${path}/autocompleter/js/jquery.autocomplete.js"></script>
        <script type="text/javascript" src="${path}/js/admin/truckerRates.js"></script>
    </head>
    <body>
        <%@include file="../preloader.jsp"%>
        <div>
            <c:choose>
                <c:when test="${not empty uploadConfirmation && empty failedTruckerFile}">
                    <div style="font-size: medium;color: blue;font-weight: bold">${uploadConfirmation}</div>
                </c:when>
                <c:when test="${not empty uploadConfirmation}">
                    <div style="font-size: medium;color: red;font-weight: bold">${uploadConfirmation}</div>
                </c:when>
            </c:choose>
        </div>
        <html:form action="/truckerRates" name="truckerRatesForm" enctype="multipart/form-data"
                   styleId="truckerRatesForm" type="com.logiware.form.TruckerRatesForm" scope="request" method="post" onsubmit="showPreloading()">
            <html:hidden property="action" styleId="action"/>
            <html:hidden property="action" styleId="action"/>
            <html:hidden property="limit" styleId="limit"/>
            <html:hidden property="selectedPage" styleId="selectedPage"/>
            <html:hidden property="selectedRows" styleId="selectedRows"/>
            <html:hidden property="sortBy" styleId="sortBy"/>
            <html:hidden property="orderBy" styleId="orderBy"/>
            <table class="table" cellpadding="2" cellspacing="4" width="100%">
		<c:if test="${empty truckerRatesForm.truckerRatesList}">
		    <tr>
			<th>Trucker Rates Upload</th>
		    </tr>
		    <tr>
			<td>
			    <div class="float-left">
				<div class="float-left">
				    <html:file property="uploadFile" styleId="uploadFile"/>
				</div>
				<div class="float-left" style="margin: 2px 0 0 10px">
				    <input type="button" value="Upload Rates" class="button" onclick="uploadRates();"/>
				</div>
			    </div>
			</td>
		    </tr>
		</c:if>
		<tr>
		    <th>Search Filters</th>
		</tr>
		<tr>
		    <td>
			<div class="float-left">
			    <div class="float-left">
				<label class="label">Trucker</label>
				<html:text property="truckerRates.truckerName" styleId="truckerName" styleClass="textbox width-175px"/>
				<input type="hidden" name="truckerNameCheck" id="truckerNameCheck" value="${truckerRatesForm.truckerRates.truckerName}"/>
				<html:text property="truckerRates.truckerNumber" styleId="truckerNumber" styleClass="textbox readonly" readonly="true"/>
			    </div>
			    <div class="float-left" style="margin: 0 0 0 10px">
				<label class="label">From Zip</label>
				<html:text property="truckerRates.fromZip" styleId="fromZip" styleClass="textbox width-50px"/>
				<input type="hidden" name="fromZipCheck" id="fromZipCheck" value="${truckerRatesForm.truckerRates.fromZip}"/>
			    </div>
			    <div class="float-left" style="margin: 0 0 0 10px">
				<label class="label">To Port</label>
				<html:text property="truckerRates.toPortCode" styleId="toPortCode" styleClass="textbox width-50px"/>
				<input type="hidden" name="toPortCodeCheck" id="toPortCodeCheck" value="${truckerRatesForm.truckerRates.toPortCode}"/>
				<html:hidden property="truckerRates.toPort" styleId="toPort"/>
			    </div>
			    <div class="float-left" style="margin: 2px 0 0 10px">
				<input type="button" value="Search" class="button" onclick="search();"/>
			    </div>
			    <div class="float-left" style="margin: 1px 0 0 10px">
				<html:checkbox property="errorRates" styleId="errorRates" value="true" onclick="showErrorRates();"/>
			    </div>
			    <div class="float-left" style="margin: 4px 0 0 0">
				<label for="errorRates" class="label">Show Error Rates</label>
			    </div>
			    <c:if test="${not empty truckerRatesForm.truckerRatesList}">
				<div class="float-left" style="margin: 2px 0 0 10px">
				    <input type="button" value="Go back" class="button" onclick="goBack();"/>
				</div>
			    </c:if>
			</div>
		    </td>
		</tr>
                <tr>
                    <th>List of Trucker Rates</th>
                </tr>
                <tr>
                    <td>
                        <div>
                            <c:import url="/jsps/admin/truckerRatesResults.jsp"/>
                        </div>
                    </td>
                </tr>
            </table>
        </html:form>
    </body>
</html>
