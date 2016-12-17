<%-- 
    Document   : DbMonitor
    Created on : Nov 29, 2012, 4:16:40 PM
    Author     : Lakshmi Narayanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>DB Monitor</title>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>   
	<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
	    <script type="text/javascript">
	    var path = "${path}";
	</script>
	<link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
	<link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
	<script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery-ext.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.js"></script>
	<script type="text/javascript" src="${path}/js/common/dbMonitor.js"></script>
    </head>
    <body>
	<%@include file="../preloader.jsp"%>
	<div id="body-container">
	    <c:if test="${not empty message}">
		<div class="message">${message}</div>
	    </c:if>
	    <c:if test="${not empty error}">
		<div class="error">${error}</div>
	    </c:if>
	    <html:form action="/dbMonitor" name="dbMonitorForm"
		       styleId="dbMonitorForm" type="com.logiware.common.form.DbMonitorForm" scope="request" method="post">
		<html:hidden property="action" styleId="action"/>
		<html:hidden property="limit" styleId="limit"/>
		<html:hidden property="selectedPage" styleId="selectedPage"/>
		<html:hidden property="sortBy" styleId="sortBy"/>
		<html:hidden property="orderBy" styleId="orderBy"/>
		<html:hidden property="id" styleId="id"/>
		<table class="table">
		    <tr>
			<th>
			    <div class="float-left">Process List</div>
			    <div class="float-right">
				<a title="Refresh" href="javascript: void(0)" onclick="refresh()">
				    <img alt="Refresh" title="Refresh" src="${path}/images/icons/refresh_16.png"/>
				</a>
			    </div>
			</th>
		    </tr>
		    <tr>
			<td>
			    <c:choose>
				<c:when test="${not empty dbMonitorForm.processes}">
				    <div id="result-header" class="table-banner green">
					<div class="float-right">
					    <div class="float-left">
						<c:choose>
						    <c:when test="${dbMonitorForm.totalRows>dbMonitorForm.selectedRows}">
							${dbMonitorForm.selectedRows} processes displayed. ${dbMonitorForm.totalRows} processes found.
						    </c:when>
						    <c:when test="${dbMonitorForm.selectedRows>1}">
							${dbMonitorForm.selectedRows} processes found.
						    </c:when>
						    <c:otherwise>1 process found.</c:otherwise>
						</c:choose>
					    </div>
					    <c:if test="${dbMonitorForm.totalPages>1 && dbMonitorForm.selectedPage>1}">
						<a title="First page" href="javascript: gotoPage('1')">
						    <img alt="First page" title="First page" src="${path}/images/first.png"/>
						</a>
						<a title="Previous page" href="javascript: gotoPage('${dbMonitorForm.selectedPage-1}')">
						    <img alt="Previous page" title="Previous page" src="${path}/images/prev.png"/>
						</a>
					    </c:if>
					    <c:if test="${dbMonitorForm.totalPages>1}">
						<select id="selectedPageNo" class="dropdown float-left">
						    <c:forEach begin="1" end="${dbMonitorForm.totalPages}" var="selectedPage">
							<c:choose>
							    <c:when test="${dbMonitorForm.selectedPage!=selectedPage}">
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
					    <c:if test="${dbMonitorForm.totalPages>dbMonitorForm.selectedPage}">
						<a title="Next page" href="javascript: gotoPage('${dbMonitorForm.selectedPage+1}')">
						    <img alt="Next Page" title="Next page" src="${path}/images/next.png"/>
						</a>
						<a title="Last page" href="javascript: gotoPage('${dbMonitorForm.totalPages}')">
						    <img alt="Last Page" title="Last page" src="${path}/images/last.png"/>
						</a>
					    </c:if>
					</div>
				    </div>
				    <div class="result-container">
					<table width="100%" cellpadding="0" cellspacing="1" class="display-table">
					    <thead>
						<tr>
						    <th><a href="javascript:doSort('id')">Id</a></th>
						    <th><a href="javascript:doSort('user')">DB User</a></th>
						    <th><a href="javascript:doSort('host')">Host</a></th>
						    <th><a href="javascript:doSort('db')">DB</a></th>
						    <th><a href="javascript:doSort('command')">Command</a></th>
						    <th><a href="javascript:doSort('time')">Time</a></th>
						    <th><a href="javascript:doSort('state')">State</a></th>
						    <th style="width:300px"><a href="javascript:doSort('info')">Info</a></th>
						    <th><a href="javascript:doSort('user_name')">User Name</a></th>
						    <th><a href="javascript:doSort('connection_time')">Connection Time</a></th>
						    <th><a href="javascript:doSort('path')">Path</a></th>
						    <th><a href="javascript:doSort('action')">Button Action</a></th>
						    <th>Action</th>
						</tr>
					    </thead>
					    <tbody>
						<c:set var="zebra" value="odd"/>
						<c:forEach var="process" items="${dbMonitorForm.processes}" varStatus="status">
						    <tr class="${zebra}">
							<td>${process.id}</td>
							<td>${process.user}</td>
							<td>${process.host}</td>
							<td>${process.db}</td>
							<td>${process.command}</td>
							<td>${process.time}</td>
							<td>${process.state}</td>
							<td>
							    <div style="width: 300px;max-height: 60px;overflow: hidden;">
								<c:choose>
								    <c:when test="${fn:length(process.info)>50}">
									<a href="javascript: void(0);" onclick="showInfo('${process.id}')">
									    ${fn:substring(process.info,0,50)}
									</a>
									<c:set var="process" value="${process}" scope="request"/>
									<c:import url="/jsps/common/dbMonitorInfo.jsp"/>
								    </c:when>
								    <c:otherwise>
									${process.info}
								    </c:otherwise>
								</c:choose>
							    </div>
							</td>
							<td>${process.userName}</td>
							<td>${process.connectionTime}</td>
							<td>${process.path}</td>
							<td>${process.action}</td>
							<td class="align-center">
							    <img title="Kill Process" 
								 src="${path}/img/icons/remove.gif" onclick="killProcess('${process.id}')"/>
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
				    <div class="table-banner green" style="background-color: #D1E6EE;">No processes running</div>
				</c:otherwise>
			    </c:choose>
			</td>
		    </tr>
		</table>
	    </html:form>
	</div>
    </body>
</html>