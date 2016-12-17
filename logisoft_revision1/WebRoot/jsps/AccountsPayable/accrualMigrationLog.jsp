<%-- 
    Document   : accrualMigrationLog
    Created on : Mar 13, 2011, 02:00:00 AM
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
	<title>Accrual Migration Log</title>
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
	<style>
	    .error-log li{
		width: 480px;
		max-height: 100px;
		overflow: hidden;
	    }
	    .warning-log li{
		width: 480px;
		max-height: 100px;
		overflow: hidden;
	    }
	</style>
    </head>
    <body>
	<%@include file="../preloader.jsp"%>
	<c:if test="${not empty pdfFileName}">
	    <script type="text/javascript">
		window.parent.showGreyBox("Control Report","${path}/servlet/FileViewerServlet?fileName=${pdfFileName}");
	    </script>
	</c:if>
	<html:form action="/accrualMigrationLog" name="accrualMigrationLogForm"
		   styleId="accrualMigrationLogForm" type="com.logiware.form.AccrualMigrationLogForm"
		   scope="request" method="post" onsubmit="showPreloading()">
	    <html:hidden property="action" styleId="action"/>
	    <html:hidden property="recordsLimit" styleId="recordsLimit"/>
	    <html:hidden property="currentPageNo" styleId="currentPageNo"/>
	    <html:hidden property="currentNoOfRecords" styleId="currentNoOfRecords"/>
	    <html:hidden property="sortBy" styleId="sortBy"/>
	    <html:hidden property="orderBy" styleId="orderBy"/>
	    <html:hidden property="id" styleId="id"/>
	    <table width="100%" cellpadding="0" cellspacing="3" class="tableBorderNew" id="searchFilters">
		<tr class="tableHeadingNew">
		    <td colspan="10">Search Filters</td>
		</tr>
		<tr class="textLabelsBold">
		    <td>Log type</td>
		    <td>
			<html:select property="logType" styleId="logType" styleClass="dropdown_accounting">
			    <html:option value="error">Error</html:option>
			    <html:option value="processed">Processed</html:option>
			    <html:option value="re-processed">Re-processed</html:option>
			    <html:option value="archived">Archived</html:option>
			</html:select>
		    </td>
		    <td>Search By</td>
		    <td>
			<html:select property="searchBy" styleId="searchBy" styleClass="dropdown_accounting" onchange="onChangeSearchBy(this)">
			    <html:option value="">Select</html:option>
			    <html:option value="amount">Amount</html:option>
			    <html:option value="invoiceNumber">Invoice Number</html:option>
			    <html:option value="blNumber">BL Number</html:option>
			    <html:option value="dockReceipt">Dock Receipt</html:option>
			    <html:option value="voyageNumber">Voyage Number</html:option>
			    <html:option value="containerNumber">Container Number</html:option>
			</html:select>
		    </td>
		    <td>Search Value</td>
		    <td>
			<c:choose>
			    <c:when test="${not empty accrualMigrationLogForm.searchValue}">
                                <html:text property="searchValue" styleId="searchValue" styleClass="textlabelsBoldForTextBox" 
                                           style="text-transform:uppercase;"/>
			    </c:when>
			    <c:otherwise>
				<html:text property="searchValue" styleId="searchValue" 
                                           styleClass="textlabelsBoldForTextBoxDisabledLook" readonly="true" style="text-transform:uppercase;"/>
			    </c:otherwise>
			</c:choose>
		    </td>
		    <td>From Date</td>
		    <td>
			<img src="${path}/img/CalendarIco.gif" alt="Date From" align="top"
			     id="cal1" onmousedown="insertDateFromCalendar(this.id,0);"/>
			<html:text property="fromDate" styleId="txtcal1" styleClass="textlabelsBoldForTextBox" maxlength="10"/>
		    </td>
		    <td>To Date</td>
		    <td>
			<img src="${path}/img/CalendarIco.gif" alt="Date To" align="top"
			     id="cal2" onmousedown="insertDateFromCalendar(this.id,0);"/>
			<html:text property="toDate" styleId="txtcal2" styleClass="textlabelsBoldForTextBox" maxlength="10"/>
		    </td>
		</tr>
		<tr>
		    <td colspan="10" align="center">
			<input type="button" value="Search" class="buttonStyleNew" onclick="search()"/>
			<input type="button" value="Clear" class="buttonStyleNew" onclick="refresh()"/>
			<input type="button" value="Reprocess All Errors" class="buttonStyleNew" onclick="reprocessAllErrors()"/>
			<input type="button" value="Load Missing Accruals" class="buttonStyleNew" onclick="loadMissingAccruals()"/>
		    </td>
		</tr>
	    </table>
	    <div align="right" id="pager" class="pagebanner" style="padding-right: 15px;">
		<div align="right" id="pager" class="pagebanner" style="padding-right: 15px;">
		    <div style="float:right">
			<c:if test="${accrualMigrationLogForm.totalPageNo>0}">
			    <div style="float:left">
				<c:choose>
				    <c:when test="${accrualMigrationLogForm.totalNoOfRecords>accrualMigrationLogForm.currentNoOfRecords}">
					${accrualMigrationLogForm.currentNoOfRecords} out of ${accrualMigrationLogForm.totalNoOfRecords} logs displayed.
				    </c:when>
				    <c:when test="${accrualMigrationLogForm.currentNoOfRecords>1}">
					${accrualMigrationLogForm.currentNoOfRecords} logs displayed.
				    </c:when>
				    <c:otherwise>1 log displayed.</c:otherwise>
				</c:choose>
			    </div>
			    <c:if test="${accrualMigrationLogForm.totalPageNo>1 && accrualMigrationLogForm.currentPageNo>1}">
				<a title="First page" href="javascript: gotoPage('1')">
				    <img alt="First" src="${path}/images/first.png" border="0"/>
				</a>
				<a title="Previous page" href="javascript: gotoPage('${accrualMigrationLogForm.currentPageNo-1}')">
				    <img alt="Previous" src="${path}/images/prev.png" border="0"/>
				</a>
			    </c:if>
			    <c:if test="${accrualMigrationLogForm.totalPageNo>1}">
				<select id="selectedPageNo" class="textlabelsBoldForTextBox" style="float:left;">
				    <c:forEach begin="1" end="${accrualMigrationLogForm.totalPageNo}" var="pageNo">
					<c:choose>
					    <c:when test="${accrualMigrationLogForm.currentPageNo!=pageNo}">
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
			    <c:if test="${accrualMigrationLogForm.totalPageNo>accrualMigrationLogForm.currentPageNo}">
				<a title="Next page" href="javascript: gotoPage('${accrualMigrationLogForm.currentPageNo+1}')">
				    <img alt="Next" src="${path}/images/next.png" border="0"/>
				</a>
				<a title="Last page" href="javascript: gotoPage('${accrualMigrationLogForm.totalPageNo}')">
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
			<c:when test="${accrualMigrationLogForm.orderBy=='desc'}"><c:set var="orderBy" value="asc"/></c:when>
			<c:otherwise><c:set var="orderBy" value="desc"/></c:otherwise>
		    </c:choose>
		    <thead>
			<tr style="text-align: center">
			    <th><a href="javascript:doSort('bluescreenVendor','${orderBy}')">Blue Screen Vendor</a></th>
			    <th><a href="javascript:doSort('vendorNumber','${orderBy}')">Logiware Vendor</a></th>
			    <th><a href="javascript:doSort('invoiceNumber','${orderBy}')">Invoice Number</a></th>
			    <th><a href="javascript:doSort('blNumber','${orderBy}')">BL Number</a></th>
			    <th><a href="javascript:doSort('amount','${orderBy}')">Amount</a></th>
			    <th><a href="javascript:doSort('dockReceipt','${orderBy}')">Dock Receipt</a></th>
			    <th><a href="javascript:doSort('voyageNumber','${orderBy}')">Voyage</a></th>
			    <th><a href="javascript:doSort('containerNumber','${orderBy}')">Container</a></th>
			    <th><a href="javascript:doSort('error','${orderBy}')">Errors/Warnings</a></th>
			    <th><a href="javascript:doSort('reportedDate','${orderBy}')">Reported Date</a></th>
			    <th><a href="javascript:doSort('noOfReprocess','${orderBy}')">No Of Reprocess</a></th>
			    <th>Action</th>
			</tr>
		    </thead>
		    <tbody>
			<c:set var="zebra" value="odd"/>
			<c:choose>
			    <c:when test="${not empty accrualMigrationLogForm.logs}">
				<c:forEach var="log" items="${accrualMigrationLogForm.logs}">
				    <tr class="${zebra}">
					<td class="upperclass">${log.bluescreenVendor}</td>
					<td class="upperclass">${log.vendorNumber}</td>
					<td class="uppercase">${log.invoiceNumber}</td>
					<td class="uppercase">${log.blNumber}</td>
					<c:choose>
					    <c:when test="${log.amount<0}">
						<td class="amount red">
						    <fmt:formatNumber value="${-log.amount}" pattern="(###,###,##0.00)"/>
						</td>
					    </c:when>
					    <c:otherwise>
						<td class="amount black">
						    <fmt:formatNumber value="${log.amount}" pattern="###,###,##0.00"/>
						</td>
					    </c:otherwise>
					</c:choose>
					<td class="uppercase">${log.dockReceipt}</td>
					<td class="uppercase">${log.voyageNumber}</td>
					<td class="uppercase">${log.containerNumber}</td>
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
					<td><fmt:formatDate value="${log.reportedDate}" pattern="MM/dd/yyyy HH:mm:ss"/></td>
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
						<img alt="" src="${path}/images/edit.png" 
						     title="Edit CSV file to fix the error" onclick="showErrorFile(${log.id})"/>
						<img alt="" src="${path}/images/gears.png" 
						     title="Reprocess the CSV file" onclick="reprocessSingleError(${log.id},this)"/>
						<img alt="" src="${path}/images/trash.png" 
						     title="Archive the error log" onclick="archiveLog(${log.id},this)"/>
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
				    <td colspan="12">No logs displayed</td>
				</tr>
			    </c:otherwise>
			</c:choose>
		    </tbody>
		</table>
	    </div>
	</html:form>
	<script type="text/javascript" src="${path}/js/Accounting/AccountsPayable/accrualMigrationLog.js"></script>
    </body>
</html>
