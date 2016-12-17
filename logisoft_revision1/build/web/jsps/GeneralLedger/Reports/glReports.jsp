<%-- 
    Document   : glReports
    Created on : Mar 15, 2012, 3:26:54 PM
    Author     : Lakshmi Naryanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>GL Reports</title>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
	    <script type="text/javascript">
	    var path = "${path}";
	</script>
        <%@include file="../../../WEB-INF/jspf/jquery.jspf" %>
        <script type="text/javascript" src="${path}/js/Accounting/GeneralLedger/glReports.js"></script>
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
		    <li><a href="javascript: void(0)" tabindex="0">Charge Code</a></li>
		    <li><a href="javascript: void(0)" tabindex="1">GL Code</a></li>
		    <li><a href="javascript: void(0)" tabindex="2">Cash</a></li>
		    <li><a href="javascript: void(0)" tabindex="3">FCL P/L</a></li>
		    <li><a href="javascript: void(0)" tabindex="4">ECU Mapping</a></li>
		</ul>
		<div class="pane" id="chargeCode"></div>
		<div class="pane" id="glCode"></div>
		<div class="pane" id="cash"></div>
		<div class="pane" id="fclPl"></div>
		<div class="pane" id="ecuMapping"></div>
	    </div>
	</div>
    </body>
</html>
