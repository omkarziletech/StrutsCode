<%-- 
    Document   : cash
    Created on : Apr 11, 2012, 4:01:34 PM
    Author     : Lakshmi Narayanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<html:form action="/glReports" name="glReportsForm" styleId="glReportsForm" type="com.logiware.form.GlReportsForm" scope="request" method="post">
    <html:hidden property="action" styleId="action"/>
    <table class="table">
	<tr>
	    <th colspan="2">Search Filters</th>
	</tr>
	<tr>
	    <td class="label">
		Reporting Date
		<html:text property="reportingDate" styleId="reportingDate" styleClass="textbox" maxlength="10" onchange="validateDate(this)"/>
		<img src="${path}/img/CalendarIco.gif" alt="Reporting Date" title="Reporting Date" align="top" id="reportingDateCalendar" class="calendar-img"/>
		<input type="button" value="Print" class="button" onclick="createPdf('cash')"/>
		<input type="button" value="Export To Excel" class="button" onclick="createExcel('cash')"/>
		<input type="button" value="Clear" class="button" onclick="refresh('cash')"/>
	    </td>
	</tr>
    </table>
</html:form>
