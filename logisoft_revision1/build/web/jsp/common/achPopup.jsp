<%-- 
    Document   : achPopup
    Created on : Nov 2, 2013, 5:31:44 PM
    Author     : Lakshmi Narayanan
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<html>
    <head>
	<title>ACH Popup</title>
	<link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
	<link type="text/css" rel="stylesheet" href="${path}/js/lightbox/lightbox.css"/>
	<link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
	<link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.datepick.css"/>
	<script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery-ext.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery.date.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery.datepick.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
	<script type="text/javascript" src="${path}/js/lightbox/lightbox.v2.3.jquery.js"></script>
	<script type="text/javascript" src="${path}/js/common/achPopup.js"></script>
    </head>
    <body>
	<%@include file="../../jsps/preloader.jsp"%>
	<html:form action="/job?accessMode=${accessMode}" name="jobForm"
		   styleId="jobForm" type="com.logiware.common.form.JobForm" scope="request" method="post">
	    <html:hidden property="action" styleId="action"/>
	    <div class="message"></div>
	    <table class="table">
		<tr>
		    <th>Search Filters</th>
		</tr>
		<tr>
		    <td>
			<div class="filter-container">
			    <table class="table margin-none border-none">
				<tr class="label">
				    <td>From Date</td>
				    <td>
					<html:text property="fromDate" styleId="fromDate" styleClass="textbox"/>
				    </td>
				    <td>To Date</td>
				    <td>
					<html:text property="toDate" styleId="toDate" styleClass="textbox"/>
				    </td>
				    <td>Status</td>
				    <td>
					<html:select property="status" styleId="status" styleClass="dropdown">
					    <html:option value="">All</html:option>
					    <html:option value="Completed">Completed</html:option>
					    <html:option value="Ready to send">Ready to send</html:option>
					    <html:option value="Failed">Failed</html:option>
					</html:select>
				    </td>
				</tr>
				<tr>
				    <td colspan="6" class="align-center">
					<input type="button" value="Search" class="button" onclick="doSearch();" tabindex="-1"/>
					<input type="button" value="Clear" class="button" onclick="doClear();" tabindex="-1"/>
				    </td>
				</tr>
			    </table>
			</div>
		    </td>
		</tr>
		<tr>
		    <th>Search Results</th>
		</tr>
		<tr>
		    <td>
			<c:choose>
			    <c:when test="${not empty achTransactions}">
				<div class="result-container" style="height: 200px;">
				    <table width="100%" cellpadding="0" cellspacing="1" class="display-table">
					<thead>
					    <tr>
						<th>Bank Name</th>
						<th>Bank Account#</th>
						<th>Bank Routing#</th>
						<th>Start Time</th>
						<th>End Time</th>
						<th>Status</th>
						<th>Text File</th>
						<th>Encrypted File</th>
						<th>Action</th>
					    </tr>
					</thead>
					<tbody>
					    <c:set var="zebra" value="odd"/>
					    <c:forEach var="row" items="${achTransactions}">
						<tr class="${zebra}">
						    <td>${row.bankDetails.bankName}</td>
						    <td>${row.bankDetails.bankAcctNo}</td>
						    <td>${row.bankDetails.bankRoutingNumber}</td>
						    <td><fmt:formatDate value="${row.startTime}" pattern="MM/dd/yyyy hh:mm:ss a"/></td>
						    <td><fmt:formatDate value="${row.endTime}" pattern="MM/dd/yyyy hh:mm:ss a"/></td>
						    <td>${row.status}</td>
						    <td>
							<c:if test="${not empty row.textFilename}">
							    <a href="javascript:showAchFile('${row.processId}', '${row.textFilename}');">${row.textFilename}</a>
							</c:if>
						    </td>
						    <td>
							<c:if test="${not empty row.encryptedFilename}">
							    <a href="javascript:showAchFile('${row.processId}', '${row.encryptedFilename}');">${row.encryptedFilename}</a>
							</c:if>
						    </td>
						    <td class="align-left">
							<c:if test="${row.status ne 'Completed'}">
							    <img title="Re-Run Job" src="${path}/images/gears.png" onclick="rerun('${row.processId}', this);"/>
							</c:if>
						    </td>
						</tr>
						<c:choose>
						    <c:when test="${zebra eq 'odd'}">
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
				<div class="table-banner green">No records found</div>
			    </c:otherwise>
			</c:choose>
		    </td>
		</tr>
	    </table>
	</html:form>
    </body>
</html>
