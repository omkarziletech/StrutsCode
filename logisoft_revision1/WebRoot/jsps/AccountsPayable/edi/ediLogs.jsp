<%-- 
    Document   : ediLogs
    Created on : May 23, 2012, 11:12:06 PM
    Author     : Lakshmi Naryanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<table width="100%" class="lightbox">
    <thead>
	<tr>
	    <th class="header">EDI Invoice Logs</th>
	    <th>
		<a href="javascript: closePopUpDiv();" class="close">
		    <img alt="" src="${path}/js/greybox/w_close.gif" title="Close" style="border: none;">Close
		</a>
	    </th>
	</tr>
    </thead>
</table>
<c:choose>
    <c:when test="${not empty logs}">
	<div align="right" class="table-banner" style="padding-right: 15px;">
	    <div style="float:right">
		<div style="float:left">
		    <c:choose>
			<c:when test="${fn:length(logs)>1}">
			    ${fn:length(logs)} logs displayed.
			</c:when>
			<c:otherwise>1 log displayed.</c:otherwise>
		    </c:choose>
		</div>
	    </div>
	</div>
	<div class="logs-results">
	    <table width="100%" cellpadding="0" cellspacing="1" class="display-table" id="logs">
		<thead>
		    <tr>
			<th>File Name</th>
			<th>Error</th>
			<th>Action</th>
		    </tr>
		</thead>
		<tbody>
		    <c:set var="zebra" value="odd"/>
		    <c:forEach var="log" items="${logs}" varStatus="index">
			<tr class="${zebra}">
			    <td>${log.fileName}</td>
			    <td>${fn:replace(fn:replace(log.error,'<','&lt;'),'>','&gt;')}</td>
			    <td>
				<img alt="View XML" title="View XML"
				     src="${path}/images/xml.png" onclick="viewXml('${log.id}')"/>
				<img alt="Remove log" title="Remove log"
				     src="${path}/img/icons/remove.gif" onclick="removeLog(this,'${log.id}')"/>
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
		</tbody>
	    </table>
	</div>
    </c:when>
    <c:otherwise>
	<div class="table-banner green" style="background-color: #D1E6EE;">No logs found</div>
    </c:otherwise>
</c:choose>