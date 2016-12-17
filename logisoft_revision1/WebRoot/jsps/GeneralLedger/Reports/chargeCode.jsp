<%-- 
    Document   : chargeCode
    Created on : Mar 15, 2012, 3:45:35 PM
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
	    <th colspan="6">Search Filters</th>
	</tr>
	<tr>
	    <td class="label width-125px">Charge Code</td>
	    <td>
		<html:text property="chargeCode" styleId="chargeCodeId" styleClass="textbox" maxlength="50"/>
		<input type="hidden" name="chargeCodeIdCheck" id="chargeCodeIdCheck" value="${glReportsForm.chargeCode}" class="hidden"/>
	    </td>
	    <td class="label width-125px">From Date</td>
	    <td>
		<html:text property="fromDate" styleId="fromDatechargeCode" styleClass="textbox" maxlength="10" onchange="validateDate(this)"/>
		<img src="${path}/img/CalendarIco.gif" alt="From Date" title="From Date" align="top" id="fromDateCalendarchargeCode" class="calendar-img"/>
	    </td>
	    <td class="label width-125px">To Date</td>
	    <td>
		<html:text property="toDate" styleId="toDatechargeCode" styleClass="textbox" maxlength="10" onchange="validateDate(this)"/>
		<img src="${path}/img/CalendarIco.gif" alt="To Date" title="To Date" align="top" id="toDateCalendarchargeCode" class="calendar-img"/>
	    </td>
	</tr>
	<tr>
	    <td colspan="6" align="center">
		<input type="button" value="Print" class="button" onclick="createPdf('chargeCode')"/>
		<input type="button" value="Export To Excel" class="button" onclick="createExcel('chargeCode')"/>
		<input type="button" value="Clear" class="button" onclick="refresh('chargeCode')"/>
	    </td>
	</tr>
    </table>
</html:form>
