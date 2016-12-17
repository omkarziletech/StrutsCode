<%-- 
    Document   : accrualMigrationReprocessLog
    Created on : Nov 16, 2011, 2:52:12 PM
    Author     : lakshh
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<table width="100%" style="background-image: url(${path}/js/greybox/header_bg.gif);border-bottom: 2px solid #AAAAAA;">
    <tbody>
	<tr>
	    <td class="lightBoxHeader">
		<c:if test="${not empty accrualMigrationLog}">
		    Reprocess Log
		</c:if>
	    </td>
	    <td>
		<a id="lightBoxClose" href="javascript: closePopUpDiv();">
		    <img alt="" src="/logisoft/js/greybox/w_close.gif" title="Close" style="border: none;">Close
		</a>
	    </td>
	</tr>
    </tbody>
</table>
<c:choose>
    <c:when test="${not empty accrualMigrationLog}">
	<div class="scrolldisplaytable" style="height: 180px">
	    <table width="100%" cellpadding="0" cellspacing="3" class="displaytagstyleNew" style="border: 1px solid white">
		<thead>
		    <tr style="text-align: center">
			<th>Log Type</th>
			<th>Errors/Warnings</th>
			<th>Reprocessed Date</th>
		    </tr>
		</thead>
		<tbody>
		    <c:set var="zebra" value="odd"/>
		    <c:forEach var="reprocessLog" items="${accrualMigrationLog.accrualMigrationReprocessLogs}">
			<tr class="${zebra}">
			    <td>${reprocessLog.logType}</td>
			    <td>
				<c:choose>
				    <c:when test="${reprocessLog.logType=='warning'}">
					<ul class="warning-log">${fn:replace(reprocessLog.error,'<init>','init')}</ul>
				    </c:when>
				    <c:otherwise>
					<ul class="error-log">${fn:replace(reprocessLog.error,'<init>','init')}</ul>
				    </c:otherwise>
				</c:choose>
			    </td>
			    <td><fmt:formatDate pattern="MM/dd/yyyy HH:mm:ss" value="${reprocessLog.reportedDate}"/></td>
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
		</tbody>
	    </table>
	</div>
    </c:when>
    <c:otherwise>
	<div class="exception-header">Exception occurred:</div>
	<div class="exception-message"><ul class="error-log">${fn:replace(exception,'<init>','init')}</ul></div>
    </c:otherwise>
</c:choose>
