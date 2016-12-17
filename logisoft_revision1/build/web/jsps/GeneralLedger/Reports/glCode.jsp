<%-- 
    Document   : glCode
    Created on : Mar 15, 2012, 3:46:13 PM
    Author     : Lakshmi Naryanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<html:form action="/glReports" name="glReportsForm" styleId="glReportsForm" type="com.logiware.form.GlReportsForm" scope="request" method="post">
    <html:hidden property="action" styleId="action"/>
    <table class="table">
	<tr>
	    <th colspan="7">Search Filters</th>
	</tr>
	<tr>
	    <td class="label width-150px">GL Account</td>
	    <td>
		<html:text property="glAccount" styleId="glAccount" styleClass="textbox" maxlength="10"/>
		<input type="hidden" name="glAccountCheck" id="glAccountCheck" value="${glReportsForm.glAccount}" class="hidden"/>
	    </td>
	    <td class="label width-150px">From Date</td>
	    <td>
		<html:text property="fromDate" styleId="fromDateglCode" styleClass="textbox" maxlength="10" onchange="validateDate(this)"/>
		<img src="${path}/img/CalendarIco.gif" alt="From Date" title="From Date" align="top" id="fromDateCalendarglCode" class="calendar-img"/>
	    </td>
	    <td class="label width-150px">To Date</td>
	    <td>
		<html:text property="toDate" styleId="toDateglCode" styleClass="textbox" maxlength="10" onchange="validateDate(this)"/>
		<img src="${path}/img/CalendarIco.gif" alt="To Date" title="To Date" align="top" id="toDateCalendarglCode" class="calendar-img"/>
	    </td>
	    <td class="label">
		<html:checkbox property="showPosted" styleId="showPosted" value="true"/>
		Show Posted
	    </td>
	</tr>
	<tr>
	    <td colspan="7" class="align-center">
		<input type="button" value="Print" class="button" onclick="createPdf('glCode')"/>
		<input type="button" value="Export To Excel" class="button" onclick="createExcel('glCode')"/>
		<input type="button" value="Clear" class="button" onclick="refresh('glCode')"/>
	    </td>
	</tr>
    </table>
</html:form>