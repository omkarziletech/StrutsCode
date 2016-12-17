<%-- 
    Document   : job
    Created on : Jun 26, 2013, 10:26:15 PM
    Author     : Lakshmi Narayanan
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://cong.logiwareinc.com/constants" prefix="constant"%>  
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<c:set var="frequencies" value="${constant:getAll('com.logiware.common.constants.Frequency')}"/>
<html>
    <head>
	<title>Job Schedulers</title>
	<link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
	<link type="text/css" rel="stylesheet" href="${path}/js/lightbox/lightbox.css"/>
	<link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.switch.css"/>
	<link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
	<script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery-ext.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery.switch.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
	<script type="text/javascript" src="${path}/js/lightbox/lightbox.v2.3.jquery.js"></script>
	<script type="text/javascript" src="${path}/js/common/job.js"></script>
    </head>
    <body>
	<%@include file="../../jsps/preloader.jsp"%>
	<span class="message">${message}</span>
	<html:form action="/job" type="com.logiware.common.form.JobForm" name="jobForm" styleId="jobForm" scope="request" method="post">
	    <html:hidden property="action" styleId="action"/>
	    <table class="table">
		<tr>
		    <th>
			<div class="float-left">List of Jobs</div>
			<div class="float-right">
			    <a title="Refresh" href="javascript: void(0)" onclick="refresh();">
				<img alt="Refresh" title="Refresh" src="${path}/images/icons/refresh_16.png"/>
			    </a>
			</div>
		    </th>
		</tr>
		<tr>
		    <td>
			<div class="result-container">
			    <table width="100%" cellpadding="0" cellspacing="1" class="display-table">
				<thead>
				    <tr>
					<th>Name</th>
					<th>Frequency</th>
					<th>Day 1</th>
					<th>Day 2</th>
					<th>Hour</th>
					<th>Minute</th>
					<th>Enabled</th>
					<th>Updated By</th>
					<th>Updated Date</th>
					<th>Start Time</th>
					<th>End Time</th>
					<th class="width-100px">Action</th>
				    </tr>
				</thead>
				<tbody>
				    <c:set var="zebra" value="odd"/>
				    <c:forEach var="job" items="${jobForm.jobs}">
					<tr class="${zebra} align-center">
					    <td class="align-left">${job.name}</td>
					    <td>
						<select id="frequencyValue${job.id}" 
							class="dropdown" onchange="onchangeFrequency(this, '${job.id}');">
						    <c:forEach var="frequency" items="${frequencies}">
							<c:choose>
							    <c:when test="${job.frequency.value eq frequency}">
								<option value="${frequency}" selected>${frequency}</option>
							    </c:when>
							    <c:otherwise>
								<option value="${frequency}">${frequency}</option>
							    </c:otherwise>
							</c:choose>
						    </c:forEach>
						</select>
					    </td>
					    <td>
						<c:choose>
						    <c:when test="${job.frequency eq 'MINUTELY' || job.frequency eq 'HOURLY' || job.frequency eq 'DAILY'}">
							<c:set var="readonly" value=" readonly"/>
							<c:set var="disabled" value=" disabled"/>
						    </c:when>
						    <c:otherwise>
							<c:set var="readonly" value=""/>
							<c:set var="disabled" value=""/>
						    </c:otherwise>
						</c:choose>
						<c:set var="endDay" value="31"/>
						<c:if test="${job.frequency eq 'WEEKLY'}">
						    <c:set var="endDay" value="7"/>
						</c:if>
						<c:set var="beginDay" value="1"/>
						<c:if test="${job.day1 == 0}">
						    <c:set var="beginDay" value="0"/>
						</c:if>
						<select id="day1${job.id}" class="dropdown width-40px${readonly}"${disabled}>
						    <c:forEach var="day" begin="${beginDay}" end="${endDay}">
							<c:choose>
							    <c:when test="${day eq job.day1}">
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
						    <c:when test="${job.frequency eq 'TWICE_A_MONTH'}">
							<c:set var="readonly" value=""/>
							<c:set var="disabled" value=""/>
						    </c:when>
						    <c:otherwise>
							<c:set var="readonly" value=" readonly"/>
							<c:set var="disabled" value=" disabled"/>
						    </c:otherwise>
						</c:choose>
						<c:set var="endDay" value="31"/>
						<c:if test="${job.frequency eq 'WEEKLY'}">
						    <c:set var="endDay" value="7"/>
						</c:if>
						<c:set var="beginDay" value="1"/>
						<c:if test="${job.day1 == 0}">
						    <c:set var="beginDay" value="0"/>
						</c:if>
						<select id="day2${job.id}" class="dropdown width-40px${readonly}"${disabled}>
						    <c:forEach var="day" begin="${beginDay}" end="${endDay}">
							<c:choose>
							    <c:when test="${day eq job.day2}">
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
						    <c:when test="${job.frequency eq 'MINUTELY'}">
							<c:set var="readonly" value=" readonly"/>
							<c:set var="disabled" value=" disabled"/>
						    </c:when>
						    <c:otherwise>
							<c:set var="readonly" value=""/>
							<c:set var="disabled" value=""/>
						    </c:otherwise>
						</c:choose>
						<c:set var="begin" value="0"/>
						<c:set var="end" value="23"/>
						<c:if test="${job.frequency eq 'HOURLY'}">
						    <c:set var="begin" value="1"/>
						    <c:set var="end" value="24"/>
						</c:if>
						<select id="hour${job.id}" class="dropdown${readonly}"${disabled}>
						    <c:forEach var="hour" begin="${begin}" end="${end}">
							<c:choose>
							    <c:when test="${hour eq job.hour}">
								<option value="${hour}" selected>${hour}</option>
							    </c:when>
							    <c:otherwise>
								<option value="${hour}">${hour}</option>
							    </c:otherwise>
							</c:choose>
						    </c:forEach>
						</select>
					    </td>
					    <td>
						<c:choose>
						    <c:when test="${job.frequency eq 'HOURLY'}">
							<c:set var="readonly" value=" readonly"/>
							<c:set var="disabled" value=" disabled"/>
						    </c:when>
						    <c:otherwise>
							<c:set var="readonly" value=""/>
							<c:set var="disabled" value=""/>
						    </c:otherwise>
						</c:choose>
						<c:set var="begin" value="0"/>
						<c:set var="end" value="59"/>
						<c:if test="${job.frequency eq 'MINUTELY'}">
						    <c:set var="begin" value="1"/>
						    <c:set var="end" value="60"/>
						</c:if>
						<select id="minute${job.id}" class="dropdown">
						    <c:forEach var="minute" begin="${begin}" end="${end}">
							<c:choose>
							    <c:when test="${minute eq job.minute}">
								<option value="${minute}" selected>${minute}</option>
							    </c:when>
							    <c:otherwise>
								<option value="${minute}">${minute}</option>
							    </c:otherwise>
							</c:choose>
						    </c:forEach>
						</select>
					    </td>
					    <td>
						<c:choose>
						    <c:when test="${job.enabled}">
							<input type="checkbox" id="enabled${job.id}" class="switch display-none" checked/>
						    </c:when>
						    <c:otherwise>
							<input type="checkbox" id="enabled${job.id}" class="switch display-hide"/>
						    </c:otherwise>
						</c:choose>
					    </td>
					    <td>${job.updatedBy}</td>
					    <td><fmt:formatDate value="${job.updatedDate}" pattern="MM/dd/yyyy hh:mm:ss a"/></td>
					    <td><fmt:formatDate value="${job.startTime}" pattern="MM/dd/yyyy hh:mm:ss a"/></td>
					    <td><fmt:formatDate value="${job.endTime}" pattern="MM/dd/yyyy hh:mm:ss a"/></td>
					    <td class="align-left">
						<input type="hidden" id="id${job.id}" value="${job.id}"/>
						<img title="Save Changes" src="${path}/images/icons/save.png" onclick="save('${job.id}');"/>
						<img title="Run Job" src="${path}/images/gears.png" onclick="run('${job.id}', '${job.name}', this);"/>
						<c:if test="${job.name eq 'ACH Transaction'}">
						    <img title="View ACH Transactions" src="${path}/images/icons/preview.png" onclick="preview();"/>
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
		    </td>
		</tr>
	    </table>
	</html:form>
    </body>
</html>
