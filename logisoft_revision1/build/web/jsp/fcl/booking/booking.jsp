<%-- 
    Document   : booking
    Created on : Mar 16, 2013, 11:01:35 PM
    Author     : Lakshmi Narayanan
--%>
<% request.setAttribute("newLineChar", "\n"); %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>FCL Booking</title>
	<%@include file="../../../WEB-INF/jspf/tags.jspf"%>
	<c:set var="path" value="${pageContext.request.contextPath}" scope="request"/>
	<constants:set className="com.gp.cong.common.ConstantsInterface" var="CommonConstant" scope="request"/>
	<script type="text/javascript">
	    var path = "${path}";
	</script>
	<link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
	<link type="text/css" rel="stylesheet" href="${path}/css/jquery.autocomplete.css"/>
	<link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
	<link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.datepick.css"/>
	<link type="text/css" rel="stylesheet" href="${path}/js/lightbox/lightbox.css"/>
	<link type="text/css" rel="stylesheet" href="${path}/css/layout/second-tabs.css" />
	<script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery-ext.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery.date.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery.datepick.js"></script>
	<script type="text/javascript" src="${path}/autocompleter/js/jquery.autocomplete.js"></script>
	<script type="text/javascript" src="${path}/js/lightbox/lightbox.v2.3.jquery.js"></script>
	<script type="text/javascript" src="${path}/js/tab/tab.js"></script>
	<script type="text/javascript" src="${path}/js/fcl/booking.js"></script>
	<c:set var="accessMode" value="1"/>
	<c:set var="writeMode" value="true"/>
	<c:if test="${param.accessMode==0}">
	    <c:set var="accessMode" value="0"/>
	    <c:set var="writeMode" value="false"/>
	</c:if>
    </head>
    <body>
	<%@include file="../../../jsps/preloader.jsp"%>
	<div id="body-container">
	    <html:form action="/fcl/booking?accessMode=${accessMode}" name="bookingForm"
		       styleId="bookingForm" type="com.logiware.fcl.form.bookingForm" scope="request" method="post">
		<html:hidden property="action" styleId="action"/>
		<c:choose>
		    <c:when test="${not empty bookingForm.booking.quoteBy}">
			<c:set var="qClass" value="readonly" scope="request"/>
			<c:set var="qReadonly" value="true" scope="request"/>
			<c:set var="qDisabled" value="true" scope="request"/>
		    </c:when>
		    <c:otherwise>
			<c:set var="qClass" value="" scope="request"/>
			<c:set var="qReadonly" value="false" scope="request"/>
			<c:set var="qDisabled" value="false" scope="request"/>
		    </c:otherwise>
		</c:choose>
		<c:choose>
		    <c:when test="${bookingForm.booking.bookingComplete == CommonConstant.YES}">
			<c:set var="bcClass" value="readonly" scope="request"/>
			<c:set var="bcReadonly" value="true" scope="request"/>
			<c:set var="bcDisabled" value="true" scope="request"/>
		    </c:when>
		    <c:otherwise>
			<c:set var="bcClass" value="" scope="request"/>
			<c:set var="bcReadonly" value="false" scope="request"/>
			<c:set var="bcDisabled" value="false" scope="request"/>
		    </c:otherwise>
		</c:choose>
		<c:choose>
		    <c:when test="${bookingForm.booking.blFlag == CommonConstant.ON}">
			<c:set var="fClass" value="readonly" scope="request"/>
			<c:set var="fReadonly" value="true" scope="request"/>
			<c:set var="fDisabled" value="true" scope="request"/>
		    </c:when>
		    <c:otherwise>
			<c:set var="fClass" value="" scope="request"/>
			<c:set var="fReadonly" value="false" scope="request"/>
			<c:set var="fDisabled" value="false" scope="request"/>
		    </c:otherwise>
		</c:choose>
		<c:choose>
		    <c:when test="${bookingForm.booking.billToCode == 'F'}">
			<c:set var="billToCodeF" value="mandatory" scope="request"/>
		    </c:when>
		    <c:when test="${bookingForm.booking.billToCode == 'S'}">
			<c:set var="billToCodeS" value="mandatory" scope="request"/>
		    </c:when>
		    <c:when test="${bookingForm.booking.billToCode == 'T'}">
			<c:set var="billToCodeT" value="mandatory" scope="request"/>
		    </c:when>
		    <c:when test="${bookingForm.booking.billToCode == 'A'}">
			<c:set var="billToCodeA" value="mandatory" scope="request"/>
		    </c:when>
		</c:choose>
		<table class="table" style="border:none; border-spacing: 0px;">
		    <tr>
			<td>
			    <c:import url="/jsp/fcl/booking/header.jsp"/>
			</td>
		    </tr>
		    <tr>
			<td>
			    <c:import url="/jsp/fcl/booking/buttons.jsp"/>
			    <div class="float-right label">
				<c:choose>
				    <c:when test="${bookingForm.booking.ratesType == CommonConstant.NO}">
					<label for="nonRatesYes">Non-Rated</label>
					<html:radio property="booking.ratesType" styleId="ratesTypeN" value="N" disabled="true"/>
					<label>Break Bulk</label>
					<html:radio property="booking.breakBulk" styleId="breakBulkYes" value="Y" disabled="true">
					    <label for="breakBulkYes">Y</label>
					</html:radio>
					<html:radio property="booking.breakBulk" styleId="breakBulkNo" value="N" disabled="true">
					    <label for="breakBulkNo">N</label>
					</html:radio>
				    </c:when>
				    <c:otherwise>
					<label for="nonRatesYes">Rated</label>
					<html:radio property="booking.ratesType" styleId="ratesTypeR" value="R" disabled="true" />
				    </c:otherwise>
				</c:choose>
			    </div>
			</td>
		    </tr>
		    <tr>
			<td>
			    <c:import url="/jsp/fcl/booking/information.jsp"/>
			</td>
		    </tr>
		    <tr>
			<td>
			    <c:import url="/jsp/fcl/booking/datesAndBilling.jsp"/>
			</td>
		    </tr>
		    <tr>
			<td>
			    <c:import url="/jsp/fcl/booking/routeInfo.jsp"/>
			</td>
		    </tr>
		    <tr>
			<td style="padding: 0 5px 0 5px">
			    <c:import url="/jsp/fcl/booking/tabs.jsp"/>
			</td>
		    </tr>
		    <tr>
			<td>
			    <c:import url="/jsp/fcl/booking/buttons.jsp"/>
			</td>
		    </tr>
		</table>
	    </html:form>
	</div>
    </body>
</html>