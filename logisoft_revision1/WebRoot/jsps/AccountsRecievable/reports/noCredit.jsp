<%-- 
    Document   : noCredit
    Created on : Apr 29, 2013, 7:48:10 PM
    Author     : Rajesh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<%@taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<html:form action="/arReports" name="arReportsForm" styleId="arReportsForm" type="com.logiware.accounting.form.ArReportsForm" scope="request" method="post">
    <table class="table filters">
        <tr>
	<td class="label width-75px align-right">Date From</td>
	<td class="label width-150px">
	    <html:text property="fromDate" styleId="noCreditFromDate" styleClass="textbox" maxlength="10" onchange="validateDate(this)"/>
	    <img src="${path}/img/CalendarIco.gif" alt="Date From" align="top" id="noCreditFromDateCalendar" class="calendar-img"/>
	</td>
	<td class="label width-75px align-right">Date To</td>
	<td class="label width-150px">
	    <html:text property="toDate" styleId="noCreditToDate" styleClass="textbox" maxlength="10" onchange="validateDate(this)"/>
	    <img src="${path}/img/CalendarIco.gif" alt="Date To" align="top" id="noCreditToDateCalendar" class="calendar-img"/>
	</td>
	<td>
	    <input type="button" value="Print" class="button" onclick="createPdf('noCredit');"/>
	    <input type="button" value="Export To Excel" class="button" onclick="createExcel('noCredit');"/>
	    <input type="button" value="Clear" class="button" onclick="refresh('noCredit');"/>
	</td>
    </tr>
</table>
</html:form>