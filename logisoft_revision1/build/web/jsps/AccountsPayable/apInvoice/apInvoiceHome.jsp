<%-- 
    Document   : apInvoiceHome
    Created on : Jun 19, 2012, 7:02:21 PM
    Author     : Lakshmi Naryanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>AP Invoice</title>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
	    <script type="text/javascript">
		var path = "${path}";
	</script>
        <%@include file="../../../WEB-INF/jspf/jquery.jspf" %>
        <script type="text/javascript" src="${path}/js/Accounting/AccountsPayable/apInvoice_new.js"></script>
	<script type="text/javascript" src="${path}/js/caljs/calendar.js"></script>
	<script type="text/javascript" src="${path}/js/caljs/lang/calendar-en.js"></script>
	<script type="text/javascript" src="${path}/js/caljs/calendar-setup.js"></script>
        <link type="text/css" rel="stylesheet" href="${path}/css/layout/second-tabs.css"/>
	<link type="text/css" rel="stylesheet" href="${path}/css/jquery.autocomplete.css"/>
	<link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
	<link type="text/css" rel="stylesheet" href="${path}/css/cal/skins/aqua/theme.css" title="Aqua"/>
	<link type="text/css" rel="stylesheet" href="${path}/css/cal/calendar-win2k-cold-2.css" title="win2k-cold-2" media="all"/>
	<script type="text/javascript">
	    var GB_ROOT_DIR = "${path}/js/greybox/";
	</script>
	<script type="text/javascript" src="${path}/js/greybox/AJS.js"></script>
	<script type="text/javascript" src="${path}/js/greybox/AJS_fx.js"></script>
	<script type="text/javascript" src="${path}/js/greybox/gb_scripts.js"></script>
	<link type="text/css" href="${path}/js/greybox/gb_styles.css" rel="stylesheet" media="all" />
    </head>
    <body>
	<div id="body-container">
	    <%@include file="../../preloader.jsp"%>
	    <div id="message" class="message" style="width: 100%; text-align: center;"></div>
	    <div id="child-container">
		<c:import url="/jsps/AccountsPayable/apInvoice/apInvoice.jsp"/>
	    </div>
	</div>
    </body>
</html>
