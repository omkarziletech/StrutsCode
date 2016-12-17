<%-- 
    Document   : arInquiry
    Created on : Sep 18, 2013, 7:12:32 PM
    Author     : Lakshmi Narayanan
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<c:choose>
    <c:when test="${param.accessMode eq 0}">
	<c:set var="accessMode" value="0" scope="request"/>
	<c:set var="writeMode" value="false" scope="request"/>
    </c:when>
    <c:otherwise>
	<c:set var="accessMode" value="1" scope="request"/>
	<c:set var="writeMode" value="true" scope="request"/>
    </c:otherwise>
</c:choose>
<html>
    <head>
	<title>AR Inquiry</title>
	<link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
	<link type="text/css" rel="stylesheet" href="${path}/js/lightbox/lightbox.css"/>
	<link type="text/css" rel="stylesheet" href="${path}/css/jquery.autocomplete.css"/>
	<link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.switch.css"/>
	<link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
	<link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.datepick.css"/>
	<script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery-ext.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery.date.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery.number.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery.switch.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery.datepick.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
	<script type="text/javascript" src="${path}/js/lightbox/lightbox.v2.3.jquery.js"></script>
	<script type="text/javascript" src="${path}/autocompleter/js/jquery.autocomplete.js"></script>
	<script type="text/javascript" src="${path}/js/Accounting/AccountsReceivable/arInquiry.js"></script>
    </head>
    <body>
	<%@include file="../../../../jsps/preloader.jsp"%>
	<html:form action="/arInquiry?accessMode=${accessMode}" name="arInquiryForm"
		   styleId="arInquiryForm" type="com.logiware.accounting.form.ArInquiryForm" scope="request" method="post">
	    <jsp:include page="hiddenFields.jsp"/>
	    <table class="table" style="margin: 0">
		<tr>
		    <td>
			<jsp:include page="header.jsp"/>
		    </td>
		</tr>
		<c:if test="${not empty arInquiryForm.arSummary}">
		    <tr>
			<th class="toggle" onclick="toggle('customer-container');">Customer Details</th>
		    </tr>
		    <tr>
			<td>
			    <jsp:include page="customerDetails.jsp"/>
			</td>
		    </tr>
		</c:if>
		<tr>
		    <th class="toggle" onclick="toggle('filter-container');">Search Filters</th>
		</tr>
		<tr>
		    <td>
			<jsp:include page="searchFilters.jsp"/>
		    </td>
		</tr>
		<tr>
		    <th>Search Results</th>
		</tr>
		<tr>
		    <td>
			<jsp:include page="searchResults.jsp"/>
		    </td>
		</tr>
	    </table>
	    <div id="hidden-div" class="display-hide"></div>
	</html:form>
    </body>
</html>