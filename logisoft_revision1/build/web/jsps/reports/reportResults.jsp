<%-- 
    Document   : reportResults
    Created on : Nov 4, 2012, 12:35:25 PM
    Author     : Lakshmi Narayanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>      
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>   
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>   
<%@taglib uri="http://cong.logiwareinc.com/constants" prefix="constants"%>  
<c:set var="path" value="${pageContext.request.contextPath}"/>
<c:set var="reportTypes" value="${constants:getAll('com.logiware.common.constants.ReportType')}"/>
<c:set var="scheduleFrequencies" value="${constants:getAll('com.logiware.common.constants.ScheduleFrequency')}"/>
<c:choose>
    <c:when test="${not empty reportForm.reports}">
	<div id="result-header" class="table-banner green">
	    <div class="float-right">
		<div class="float-left">
		    <c:choose>
			<c:when test="${reportForm.totalRows>reportForm.selectedRows}">
			    ${reportForm.selectedRows} reports displayed. ${reportForm.totalRows} reports found.
			</c:when>
			<c:when test="${reportForm.selectedRows>1}">${reportForm.selectedRows} reports found.</c:when>
			<c:otherwise>1 report found.</c:otherwise>
		    </c:choose>
		</div>
		<c:if test="${reportForm.totalPages>1 && reportForm.selectedPage>1}">
		    <a title="First page" href="javascript: gotoPage('1')">
			<img alt="First page" title="First page" src="${path}/images/first.png"/>
		    </a>
		    <a title="Previous page" href="javascript: gotoPage('${reportForm.selectedPage-1}')">
			<img alt="Previous page" title="Previous page" src="${path}/images/prev.png"/>
		    </a>
		</c:if>
		<c:if test="${reportForm.totalPages>1}">
		    <select id="selectedPageNo" class="dropdown float-left">
			<c:forEach begin="1" end="${reportForm.totalPages}" var="selectedPage">
			    <c:choose>
				<c:when test="${reportForm.selectedPage!=selectedPage}">
				    <option value="${selectedPage}">${selectedPage}</option>
				</c:when>
				<c:otherwise>
				    <option value="${selectedPage}" selected>${selectedPage}</option>
				</c:otherwise>
			    </c:choose>
			</c:forEach>
		    </select>
		    <a href="javascript: gotoSelectedPage()">
			<img alt="Goto Page" title="Goto Page" src="${path}/images/go.jpg"/>
		    </a>
		</c:if>
		<c:if test="${reportForm.totalPages>reportForm.selectedPage}">
		    <a title="Next page" href="javascript: gotoPage('${reportForm.selectedPage+1}')">
			<img alt="Next Page" title="Next page" src="${path}/images/next.png"/>
		    </a>
		    <a title="Last page" href="javascript: gotoPage('${reportForm.totalPages}')">
			<img alt="Last Page" title="Last page" src="${path}/images/last.png"/>
		    </a>
		</c:if>
	    </div>
	</div>
	<div class="result-container">
	    <table width="100%" cellpadding="0" cellspacing="1" class="display-table">
		<thead>
		    <tr>
			<th><a href="javascript:doSort('reportName')">Report Name</a></th>
			<th><a href="javascript:doSort('reportType')">Report Type</a></th>
			<th><a href="javascript:doSort('scheduleFrequency')">Schedule Frequency</a></th>
			<th><a href="javascript:doSort('scheduleTime')">Schedule Day 1</a></th>
			<th><a href="javascript:doSort('scheduleTime')">Schedule Day 2</a></th>
			<th style="width:150px"><a href="javascript:doSort('scheduleTime')">Schedule Time</a></th>
			<th><a href="javascript:doSort('header')">Header</a></th>
			<th><a href="javascript:doSort('enabled')">Enabled</a></th>
			<th><a href="javascript:doSort('sender')">Sender</a></th>
			<th><a href="javascript:doSort('emailId')">To/Cc Email</a></th>
			<th><a href="javascript:doSort('emailBody')">Email Body</a></th>
			<th style="width:100px">Action</th>
		    </tr>
		</thead>
		<tbody>
		    <c:set var="zebra" value="odd"/>
		    <c:forEach var="report" items="${reportForm.reports}" varStatus="status">
			<tr class="${zebra} align-center">
			    <td>
				<input type="text" id="reportName${status.count}" class="textbox required" value="${report.reportName}"/>
			    </td>
			    <td>
				<select id="reportTypeValue${status.count}" class="dropdown">
				    <c:forEach var="reportType" items="${reportTypes}">
					<c:choose>
					    <c:when test="${report.reportType.value eq reportType}">
						<option value="${reportType}" selected>${reportType}</option>
					    </c:when>
					    <c:otherwise>
						<option value="${reportType}">${reportType}</option>
					    </c:otherwise>
					</c:choose>
				    </c:forEach>
				</select>
			    </td>
			    <td>
				<select id="scheduleFrequencyValue${status.count}" 
					class="dropdown" onchange="onchangeScheduleFrequency(this,'${status.count}')">
				    <c:forEach var="scheduleFrequency" items="${scheduleFrequencies}">
					<c:choose>
					    <c:when test="${report.scheduleFrequency.value eq scheduleFrequency}">
						<option value="${scheduleFrequency}" selected>${scheduleFrequency}</option>
					    </c:when>
					    <c:otherwise>
						<option value="${scheduleFrequency}">${scheduleFrequency}</option>
					    </c:otherwise>
					</c:choose>
				    </c:forEach>
				</select>
			    </td>
			    <td>
				<c:choose>
				    <c:when test="${report.scheduleFrequency eq 'DAILY'}">
					<c:set var="readonly" value=" readonly"/>
					<c:set var="disabled" value=" disabled"/>
				    </c:when>
				    <c:otherwise>
					<c:set var="readonly" value=""/>
					<c:set var="disabled" value=""/>
				    </c:otherwise>
				</c:choose>
				<c:set var="days" value="31"/>
				<c:if test="${report.scheduleFrequency eq 'WEEKLY'}">
				    <c:set var="days" value="7"/>
				</c:if>
				<select id="scheduleDay1${status.count}" class="dropdown width-40px${readonly}"${disabled}>
				    <c:forEach var="day" begin="1" end="${days}">
					<c:choose>
					    <c:when test="${day eq report.scheduleDay1}">
						<option value="${day}" selected>${day}</option>
					    </c:when>
					    <c:otherwise>
						<option value="${day}">${day}</option>
					    </c:otherwise>
					</c:choose>
				    </c:forEach>
				</select>
			    </td>
			    <td>
				<c:choose>
				    <c:when test="${report.scheduleFrequency eq 'TWICE_A_MONTH'}">
					<c:set var="readonly" value=""/>
					<c:set var="disabled" value=""/>
				    </c:when>
				    <c:otherwise>
					<c:set var="readonly" value=" readonly"/>
					<c:set var="disabled" value=" disabled"/>
				    </c:otherwise>
				</c:choose>
				<select id="scheduleDay2${status.count}" class="dropdown width-40px${readonly}"${disabled}>
				    <c:forEach var="day" begin="1" end="31">
					<c:choose>
					    <c:when test="${day eq report.scheduleDay2}">
						<option value="${day}" selected>${day}</option>
					    </c:when>
					    <c:otherwise>
						<option value="${day}">${day}</option>
					    </c:otherwise>
					</c:choose>
				    </c:forEach>
				</select>
			    </td>
			    <td style="width:150px">
				<select id="scheduleHr${status.count}" class="dropdown">
				    <c:forEach var="hr" begin="0" end="23">
					<c:if test="${hr>=0 && hr<=9}">
					    <c:set var="hr" value="0${hr}"/>
					</c:if>
					<c:choose>
					    <c:when test="${hr eq fn:substring(report.scheduleTime, 0, 2)}">
						<option value="${hr}" selected>${hr}</option>
					    </c:when>
					    <c:otherwise>
						<option value="${hr}">${hr}</option>
					    </c:otherwise>
					</c:choose>
				    </c:forEach>
				</select>
				:
				<select id="scheduleMin${status.count}" class="dropdown">
				    <c:forEach var="min" begin="0" end="59">
					<c:if test="${min>=0 && min<=9}">
					    <c:set var="min" value="0${min}"/>
					</c:if>
					<c:choose>
					    <c:when test="${min eq fn:substring(report.scheduleTime, 3, 5)}">
						<option value="${min}" selected>${min}</option>
					    </c:when>
					    <c:otherwise>
						<option value="${min}">${min}</option>
					    </c:otherwise>
					</c:choose>
				    </c:forEach>
				</select>
				:
				<select id="scheduleSec${status.count}" class="dropdown">
				    <c:forEach var="sec" begin="0" end="59">
					<c:if test="${sec>=0 && sec<=9}">
					    <c:set var="sec" value="0${sec}"/>
					</c:if>
					<c:choose>
					    <c:when test="${sec eq fn:substring(report.scheduleTime, 6, 8)}">
						<option value="${sec}" selected>${sec}</option>
					    </c:when>
					    <c:otherwise>
						<option value="${sec}">${sec}</option>
					    </c:otherwise>
					</c:choose>
				    </c:forEach>
				</select>
				<input type="hidden" id="scheduleTime${status.count}" class="textbox required" value="${report.scheduleTime}"/>
			    </td>
			    <td>
				<c:choose>
				    <c:when test="${report.header}">
					<input type="checkbox" id="header${status.count}" checked/>
				    </c:when>
				    <c:otherwise>
					<input type="checkbox" id="header${status.count}"/>
				    </c:otherwise>
				</c:choose>
			    </td>
			    <td>
				<c:choose>
				    <c:when test="${report.enabled}">
					<input type="checkbox" id="enabled${status.count}" checked/>
				    </c:when>
				    <c:otherwise>
					<input type="checkbox" id="enabled${status.count}"/>
				    </c:otherwise>
				</c:choose>
			    </td>
			    <td>
				<input type="text" id="senderName${status.count}" value="${report.senderName}" class="textbox required"/>
				<input type="hidden" id="sender${status.count}" value="${report.sender}"/>
			    </td>
			    <td>
				<textarea id="emailId${status.count}" class="textbox"
					cols="50" rows="3" style="width:150px;text-transform: none;">${report.emailId}</textarea>
			    </td>
			    <td>
				<textarea id="emailBody${status.count}" class="textbox required"
					cols="50" rows="5" style="width:200px;text-transform: none;">${report.emailBody}</textarea>
			    </td>
			    <td style="width:100px">
				<div id="query-container${status.count}" class="static-popup" style="display: none;width: 700px;height: 200px;">
				    <table class="table" style="margin: 0px;border: none;">
					<tr>
					    <th colspan="3">
						<div class="float-left">${fn:toUpperCase(report.reportName)} - Queries</div>
						<div class="float-right">
						    <a href="javascript: closeQueries('${status.count}')">
							<img alt="Close Queries" src="${path}/images/icons/close.png"/>
						    </a>
						</div>
					    </th>
					</tr>
					<tr>
					    <td class="label" style="width:50px !important;">Query 1</td>
					    <td>
						<textarea id="query1${status.count}" class="textbox required"
							  cols="50" rows="5" style="width:400px;text-transform: none;">${report.query1}</textarea>
					    </td>
					    <td style="color:red;font-size: 11px;font-style: italic;">
						Email column is required<br/>
						<font style="color:green">ex: email_id as email</font>
					    </td>
					</tr>
					<tr>
					    <td class="label" style="width:50px !important;">Query 2</td>
					    <td>
						<textarea id="query2${status.count}" class="textbox" cols="50" 
							  rows="5" style="width:400px;text-transform: none;">${report.query2}</textarea>
					    </td>
					    <td style="color:red;font-size: 11px;font-style: italic;">
						Use query1.column_name if the query1 columns are used in relation<br/>
						<font style="color:green">ex: query2.email_id = query1.email</font>
					    </td>
					</tr>
					<tr class="align-center">
					    <td colspan="3">
						<input type="hidden" id="id${status.count}" value="${report.id}"/>
						<input type="button" value="Update" class="button" onclick="updateReport('${status.count}')"/>
						<input type="button" value="Cancel" class="button" onclick="closeQueries('${status.count}')"/>
					    </td>
					</tr>
				    </table>
				</div>
				<img title="Show Queries" src="${path}/images/icons/sql-query.png" onclick="showQueries('${status.count}')"/>
				<img title="Save Changes" src="${path}/images/icons/save.png" onclick="updateReport('${status.count}')"/>
				<img title="Preview Report" src="${path}/images/icons/preview.png" onclick="previewReport('${report.id}')"/>
				<img title="Show Notes" src="${path}/images/notepad_green.png" onclick="showNotes('${report.id}')"/>
				<img title="Delete Report" src="${path}/img/icons/remove.gif"
				     onclick="deleteReport('${report.id}','${report.reportName}')"/>
				<input type="hidden" id="createdBy${status.count}" value="${report.createdBy}"/>
				<fmt:formatDate var="createdDate" value="${report.createdDate}" pattern="MM/dd/yyyy hh:mm:ss a"/>
				<input type="hidden" id="createdDateValue${status.count}" value="${createdDate}"/>
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
	<div class="table-banner green" style="background-color: #D1E6EE;">No reports found</div>
    </c:otherwise>
</c:choose>