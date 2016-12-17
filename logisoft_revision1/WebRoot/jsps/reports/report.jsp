<%-- 
    Document   : report
    Created on : Nov 3, 2012, 2:47:32 AM
    Author     : Lakshmi Narayanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Report</title>
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
	<script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery-ext.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.js"></script>
	<script type="text/javascript" src="${path}/autocompleter/js/jquery.autocomplete.js"></script>
	<script type="text/javascript" src="${path}/js/reports/report.js"></script>
    </head>
    <body>
	<%@include file="../preloader.jsp"%>
	<div id="body-container">
	    <c:if test="${not empty message}">
		<div class="message">${message}</div>
	    </c:if>
	    <html:form action="/report" name="reportForm"
		       styleId="reportForm" type="com.logiware.common.form.ReportForm" scope="request" method="post">
		<html:hidden property="action" styleId="action"/>
		<html:hidden property="limit" styleId="limit"/>
		<html:hidden property="selectedPage" styleId="selectedPage"/>
		<html:hidden property="sortBy" styleId="sortBy"/>
		<html:hidden property="orderBy" styleId="orderBy"/>
		<input type="hidden" id="reportId"/>
		<table class="table">
		    <tr>
			<th>Reports</th>
		    </tr>
		    <tr>
			<td>
			    <input type="button" class="button" value="Search" onclick="search()"/>
			    <input type="button" class="button" value="Clear"onclick="clearScreen()"/>
			    <input type="button" class="button" value="Add Report" onclick="addReport()"/>
			</td>
		    </tr>
		    <tr>
			<th>List of Reports</th>
		    </tr>
		    <tr>
			<td>
			    <c:import url="/jsps/reports/reportResults.jsp"/>
			</td>
		    </tr>
		</table>
		<div id="add-container" class="static-popup" style="display: none;width: 800px;height: 375px;">
		    <c:import url="/jsps/reports/addReport.jsp"/>    
		</div>
		<div id="edit-container" class="static-popup" style="display: none;width: 700px;height: 200px;">
		</div>
	    </html:form>
	</div>
    </body>
</html>
