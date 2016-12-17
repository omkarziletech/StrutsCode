<%-- 
    Document   : arReports
    Created on : Jun 8, 2012, 8:35:15 PM
    Author     : Lakshmi Naryanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>AR Reports</title>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
	<script type="text/javascript">
	    var path = "${path}";
	</script>
        <%@include file="../../../WEB-INF/jspf/jquery.jspf" %>
        <script type="text/javascript" src="${path}/js/Accounting/AccountsReceivable/arReports.js"></script>
	<script type="text/javascript" src="${path}/js/caljs/calendar.js"></script>
	<script type="text/javascript" src="${path}/js/caljs/lang/calendar-en.js"></script>
	<script type="text/javascript" src="${path}/js/caljs/calendar-setup.js"></script>
        <link type="text/css" rel="stylesheet" href="${path}/css/layout/second-tabs.css"/>
	<link type="text/css" rel="stylesheet" href="${path}/css/jquery.autocomplete.css"/>
	<link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
	<link type="text/css" rel="stylesheet" href="${path}/css/cal/skins/aqua/theme.css" title="Aqua"/>
	<link type="text/css" rel="stylesheet" href="${path}/css/cal/calendar-win2k-cold-2.css" title="win2k-cold-2" media="all"/>
    </head>
    <body>
	<div class="body-container">
	    <%@include file="../../preloader.jsp"%>
	    <div id="container">
		<ul class="htabs">
		    <li><a href="javascript: void(0)" tabindex="0">Statement</a></li>
		    <li><a href="javascript: void(0)" tabindex="1">Aging</a></li>
		    <li><a href="javascript: void(0)" tabindex="2">DSO</a></li>
		    <li><a href="javascript: void(0)" tabindex="3">Notes</a></li>
		    <li><a href="javascript: void(0)" tabindex="4">No Credit</a></li>
		    <li><a href="javascript: void(0)" tabindex="5">Activity</a></li>
		    <li><a href="javascript: void(0)" tabindex="6">Dispute</a></li>
		</ul>
		<div class="pane" id="statement"></div>
		<div class="pane" id="aging"></div>
		<div class="pane" id="dso"></div>
		<div class="pane" id="notes"></div>
		<div class="pane" id="noCredit"></div>
		<div class="pane" id="activity"></div>
		<div class="pane" id="dispute"></div>
	    </div>
	</div>
    </body>
</html>
