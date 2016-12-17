<%-- 
    Document   : arMigrationLog
    Created on : Oct 13, 2011, 4:54:15 PM
    Author     : Lakshmi Narayanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
	<%@include file="../includes/jspVariables.jsp"%>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Ar Migration Log</title>
	<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
	<%@include file="../includes/baseResources.jsp"%>
	<%@include file="../includes/resources.jsp"%>
	<script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
	<script type="text/javascript" src="${path}/js/common.js"></script>
	<script type="text/javascript" src="${path}/js/caljs/calendar.js"></script>
	<script type="text/javascript" src="${path}/js/caljs/lang/calendar-en.js"></script>
	<script type="text/javascript" src="${path}/js/caljs/calendar-setup.js"></script>
	<script type="text/javascript" src="${path}/js/caljs/CalendarPopup.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery.date.js"></script>
    </head>
    <body>
	<%@include file="../preloader.jsp"%>
	<c:if test="${not empty pdfFileName}">
	    <script type="text/javascript">
		window.parent.showGreyBox("Control Report","${path}/servlet/FileViewerServlet?fileName=${pdfFileName}");
	    </script>
	</c:if>
	<html:form action="/arMigrationLog?accessMode=${accessMode}" name="arMigrationLogForm" enctype="multipart/form-data"
		   styleId="arMigrationLogForm" type="com.logiware.form.ArMigrationLogForm" scope="request" method="post" onsubmit="showPreloading()">
	    <html:hidden property="action" styleId="action"/>
	    <html:hidden property="recordsLimit" styleId="recordsLimit"/>
	    <html:hidden property="currentPageNo" styleId="currentPageNo"/>
	    <html:hidden property="currentNoOfRecords" styleId="currentNoOfRecords"/>
	    <html:hidden property="sortBy" styleId="sortBy"/>
	    <html:hidden property="orderBy" styleId="orderBy"/>
	    <html:hidden property="id" styleId="id"/>
	    <table width="100%" cellpadding="0" cellspacing="3" class="tableBorderNew" id="searchFilters">
		<tr class="tableHeadingNew">
		    <td colspan="9">Search Filters</td>
		</tr>
		<tr class="textLabelsBold">
		    <td>Log type</td>
		    <td>
			<html:select property="logType" styleId="logType" styleClass="dropdown_accounting">
			    <html:option value="error">Error</html:option>
			    <html:option value="warning">Warning</html:option>
			    <html:option value="processed">Processed</html:option>
			    <html:option value="re-processed">Re-processed</html:option>
			</html:select>
		    </td>
		    <td>Bill of Lading</td>
                    <td><html:text property="billOfLading" styleId="billOfLading" styleClass="textlabelsBoldForTextBox" 
                               style="text-transform:uppercase;"/></td>
		    <td>From Date</td>
		    <td>
			<html:text property="fromDate" styleId="txtcal1" styleClass="textlabelsBoldForTextBox" maxlength="10"/>
			<img src="${path}/img/CalendarIco.gif" alt="Date From" align="top"
			     id="cal1" onmousedown="insertDateFromCalendar(this.id,0);"/>
		    </td>
		    <td>To Date</td>
		    <td>
			<html:text property="toDate" styleId="txtcal2" styleClass="textlabelsBoldForTextBox" maxlength="10"/>
			<img src="${path}/img/CalendarIco.gif" alt="Date To" align="top"
			     id="cal2" onmousedown="insertDateFromCalendar(this.id,0);"/>
		    </td>
		    <td><html:checkbox property="missingCharges" styleId="missingCharges"/>Show Missing Charges</td>
		</tr>
		<tr>
		    <td colspan="9" align="center">
			<input type="button" value="Search" class="buttonStyleNew" onclick="search()"/>
			<input type="button" value="Reprocess All Errors" class="buttonStyleNew" onclick="reprocessAllErrors()"/>
			<input type="button" value="Print Control Report" class="buttonStyleNew" onclick="printControlReport()"/>
			<input type="button" value="Export Control Report" class="buttonStyleNew" onclick="exportControlReport()"/>
		    </td>
		</tr>
	    </table>
	    <div align="right" id="pager" class="pagebanner" style="padding-right: 15px;">
		<div align="right" id="pager" class="pagebanner" style="padding-right: 15px;">
		    <div style="float:right">
			<c:if test="${arMigrationLogForm.totalPageNo>0}">
			    <div style="float:left">
				<c:choose>
				    <c:when test="${arMigrationLogForm.totalNoOfRecords>arMigrationLogForm.currentNoOfRecords}">
					${arMigrationLogForm.currentNoOfRecords} out of ${arMigrationLogForm.totalNoOfRecords} logs displayed.
				    </c:when>
				    <c:when test="${arMigrationLogForm.currentNoOfRecords>1}">${arMigrationLogForm.currentNoOfRecords} logs displayed.</c:when>
				    <c:otherwise>1 log displayed.</c:otherwise>
				</c:choose>
			    </div>
			    <c:if test="${arMigrationLogForm.totalPageNo>1 && arMigrationLogForm.currentPageNo>1}">
				<a title="First page" href="javascript: gotoPage('1')">
				    <img alt="First" src="${path}/images/first.png" border="0"/>
				</a>
				<a title="Previous page" href="javascript: gotoPage('${arMigrationLogForm.currentPageNo-1}')">
				    <img alt="Previous" src="${path}/images/prev.png" border="0"/>
				</a>
			    </c:if>
			    <c:if test="${arMigrationLogForm.totalPageNo>1}">
				<select id="selectedPageNo" class="textlabelsBoldForTextBox" style="float:left;">
				    <c:forEach begin="1" end="${arMigrationLogForm.totalPageNo}" var="pageNo">
					<c:choose>
					    <c:when test="${arMigrationLogForm.currentPageNo!=pageNo}">
						<option value="${pageNo}">${pageNo}</option>
					    </c:when>
					    <c:otherwise>
						<option value="${pageNo}" selected>${pageNo}</option>
					    </c:otherwise>
					</c:choose>
				    </c:forEach>
				</select>
				<a href="javascript: gotoSelectedPage()">
				    <img alt="Go" src="${path}/images/go.jpg" border="0"/>
				</a>
			    </c:if>
			    <c:if test="${arMigrationLogForm.totalPageNo>arMigrationLogForm.currentPageNo}">
				<a title="Next page" href="javascript: gotoPage('${arMigrationLogForm.currentPageNo+1}')">
				    <img alt="Next" src="${path}/images/next.png" border="0"/>
				</a>
				<a title="Last page" href="javascript: gotoPage('${arMigrationLogForm.totalPageNo}')">
				    <img alt="Last" src="${path}/images/last.png" border="0"/>
				</a>
			    </c:if>
			</c:if>
		    </div>
		</div>
	    </div>
	    <div class="scrolldisplaytable" style="height: 100%">
		<table width="100%" cellpadding="0" cellspacing="3" class="displaytagstyleNew" id="log" style="border: 1px solid white">
		    <c:choose>
			<c:when test="${arMigrationLogForm.orderBy=='desc'}"><c:set var="orderBy" value="asc"/></c:when>
			<c:otherwise><c:set var="orderBy" value="desc"/></c:otherwise>
		    </c:choose>
		    <thead>
			<tr style="text-align: center">
			    <th><a href="javascript:doSort('blueScreenAccount','${orderBy}')">Blue Screen Account</a></th>
			    <th><a href="javascript:doSort('customerNumber','${orderBy}')">Logiware Account</a></th>
			    <th><a href="javascript:doSort('blNumber','${orderBy}')">BL Number</a></th>
			    <th><a href="javascript:doSort('invoiceNumber','${orderBy}')">Invoice Number</a></th>
			    <th><a href="javascript:doSort('error','${orderBy}')">Errors/Warnings</a></th>
			    <th><a href="javascript:doSort('reportedDate','${orderBy}')">Reported Date</a></th>
			    <th><a href="javascript:doSort('fileName','${orderBy}')">File Name</a></th>
			    <th><a href="javascript:doSort('noOfReprocess','${orderBy}')">No Of Reprocess</a></th>
			    <th>Action</th>
			</tr>
		    </thead>
		    <tbody>
			<c:set var="zebra" value="odd"/>
			<c:choose>
			    <c:when test="${not empty arMigrationLogForm.logs}">
				<c:forEach var="log" items="${arMigrationLogForm.logs}">
				    <tr class="${zebra}">
					<td>${log.blueScreenAccount}</td>
					<td>${log.customerNumber}</td>
					<td>${log.blNumber}</td>
					<td>${log.invoiceNumber}</td>
					<td>
					    <c:choose>
						<c:when test="${log.logType=='warning'}">
						    <ul class="warning-log">${fn:replace(log.error,'<init>','init')}</ul>
						</c:when>
						<c:otherwise>
						    <ul class="error-log">${fn:replace(log.error,'<init>','init')}</ul>
						</c:otherwise>
					    </c:choose>
					</td>
					<td><fmt:formatDate pattern="MM/dd/yyyy HH:mm:ss" value="${log.reportedDate}"/></td>
					<td>${log.fileName}</td>
					<td style="text-align: center">
					    <c:choose>
						<c:when test="${log.noOfReprocess>0}">
						    <a href="javascript:showReprocessLogs(${log.id})">${log.noOfReprocess}</a>
						</c:when>
						<c:otherwise>
						    ${log.noOfReprocess}
						</c:otherwise>
					    </c:choose>
					</td>
					<td>
					    <c:if test="${log.logType=='error' || log.logType=='warning'}">
						<img alt="" src="${path}/images/edit.png" title="Edit CSV file to fix the error" onclick="showCsvFile(${log.id})"/>
						<img alt="" src="${path}/images/gears.png" title="Reprocess the CSV file" onclick="reprocessSingleError(${log.id},this)"/>
						<img alt="" src="${path}/images/trash.png" title="Remove the error log" onclick="deleteLog(${log.id},this)"/>
					    </c:if>
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
			    </c:when>
			    <c:otherwise>
				<tr>
				    <td colspan="9">No logs displayed</td>
				</tr>
			    </c:otherwise>
			</c:choose>
		    </tbody>
		</table>
	    </div>
	</html:form>
	<script type="text/javascript" src="${path}/js/Accounting/AccountsReceivable/arMigrationLog.js"></script>
    </body>
</html>
