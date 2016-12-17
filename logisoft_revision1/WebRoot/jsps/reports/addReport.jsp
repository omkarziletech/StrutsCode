<%-- 
    Document   : addReport
    Created on : Nov 4, 2012, 10:32:21 PM
    Author     : Lakshmi Narayanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>      
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>   
<%@taglib uri="http://cong.logiwareinc.com/constants" prefix="constants"%>  
<c:set var="path" value="${pageContext.request.contextPath}"/>
<c:set var="reportTypes" value="${constants:getAll('com.logiware.common.constants.ReportType')}"/>
<c:set var="scheduleFrequencies" value="${constants:getAll('com.logiware.common.constants.ScheduleFrequency')}"/>
<table class="table" style="margin: 0px;border: none;">
    <thead>
	<tr>
	    <th colspan="6">
		<div class="float-left">Add Report</div>
		<div class="float-right">
		    <a href="javascript: closeAddReport()">
			<img alt="Close Queries" src="${path}/images/icons/close.png"/>
		    </a>
		</div>
	    </th>
	</tr>
    </thead>
    <tbody>
	<tr>
	    <td class="label width-90px">Report Name</td>
	    <td class="width-150px">
		<input type="text" name="report.reportName" id="reportName" class="textbox required"/>
	    </td>
	    <td class="label width-90px">Report Type</td>
	    <td class="width-150px">
		<select name="report.reportTypeValue" id="reportType" class="dropdown width-125px">
		    <c:forEach var="reportType" items="${reportTypes}">
			<option value="${reportType}">${reportType}</option>
		    </c:forEach>
		</select>
	    </td>
	    <td class="label width-125px">Schedule Frequency</td>
	    <td>
		<select name="report.scheduleFrequencyValue" id="scheduleFrequencyValue"
			class="dropdown width-125px" style="width: 145px;max-width: 145px;" onchange="onchangeScheduleFrequency(this)">
		    <c:forEach var="scheduleFrequency" items="${scheduleFrequencies}">
			<option value="${scheduleFrequency}">${scheduleFrequency}</option>
		    </c:forEach>
		</select>
	    </td>
	</tr>
	<tr>
	    <td class="label width-90px">Schedule Day 1</td>
	    <td>
		<select name="report.scheduleDay1" id="scheduleDay1" class="dropdown width-40px readonly" disabled>
		    <c:forEach var="day" begin="1" end="31">
			<option value="${day}">${day}</option>
		    </c:forEach>
		</select>
	    </td>
	    <td class="label width-90px">Schedule Day 2</td>
	    <td>
		<select name="report.scheduleDay2" id="scheduleDay2" class="dropdown width-40px readonly" disabled>
		    <c:forEach var="day" begin="1" end="31">
			<option value="${day}">${day}</option>
		    </c:forEach>
		</select>
	    </td>
	    <td class="label width-90px">Schedule Time</td>
	    <td colspan="2">
		<select id="scheduleHr" class="dropdown">
		    <c:forEach var="hr" begin="0" end="23">
			<c:if test="${hr>=0 && hr<=9}">
			    <c:set var="hr" value="0${hr}"/>
			</c:if>
			<option value="${hr}">${hr}</option>
		    </c:forEach>
		</select>
		:
		<select id="scheduleMin" class="dropdown">
		    <c:forEach var="min" begin="0" end="59">
			<c:if test="${min>=0 && min<=9}">
			    <c:set var="min" value="0${min}"/>
			</c:if>
			<option value="${min}">${min}</option>
		    </c:forEach>
		</select>
		:
		<select id="scheduleSec" class="dropdown">
		    <c:forEach var="sec" begin="0" end="59">
			<c:if test="${sec>=0 && sec<=9}">
			    <c:set var="sec" value="0${sec}"/>
			</c:if>
			<option value="${sec}">${sec}</option>
		    </c:forEach>
		</select>
		<input type="hidden" name="report.scheduleTime" id="scheduleTime" class="textbox required"/>
	    </td>
	</tr>
	<tr>
	    <td class="label width-90px">Sender</td>
	    <td>
		<input type="text" id="senderName" class="textbox required"/>
		<input type="hidden" name="report.sender" id="sender"/>
	    </td>
	    <td class="label width-90px">To/Cc Email</td>
	    <td colspan="3">
		<input type="text" name="report.emailId" id="emailId" class="textbox"/>
	    </td>
	</tr>
	<tr>
	    <td class="label width-90px">Email Body</td>
	    <td colspan="3">
		<textarea name="report.emailBody" id="emailBody"
			  class="textbox required" cols="50" rows="5" style="width:400px;text-transform: none;"></textarea>
	    </td>
	    <td class="label width-90px" colspan="2">
		<input type="checkbox" name="report.header" id="header" checked/>Header
		<br/>
		<input type="checkbox" name="report.enabled" id="enabled" checked/>Enabled
	    </td>
	</tr>
	<tr>
	    <td class="label width-90px">Query 1</td>
	    <td colspan="3">
		<textarea name="report.query1" id="query1"
			  class="textbox required" cols="50" rows="5" style="width:400px;text-transform: none;"></textarea>
	    </td>
	    <td colspan="2" style="color:red;font-size: 11px;font-style: italic;">
		Email column is required<br/>
		<font style="color:green">ex: email_id as email</font>
	    </td>
	</tr>
	<tr>
	    <td class="label width-90px">Query 2</td>
	    <td colspan="3">
		<textarea name="report.query2" id="query2"
			  class="textbox" cols="50" rows="5" style="width:400px;text-transform: none;"></textarea>
	    </td>
	    <td colspan="2" style="color:red;font-size: 11px;font-style: italic;">
		Use quer1.column_name if the query1 columns are used in relation<br/>
		<font style="color:green">ex: query2.email_id = query1.email</font>
	    </td>
	</tr>
	<tr class="align-center">
	    <td colspan="6">
		<input type="button" value="Save" class="button" onclick="saveReport()"/>
		<input type="button" value="Cancel" class="button" onclick="closeAddReport()"/>
	    </td>
	</tr>
    </tbody>
</table>