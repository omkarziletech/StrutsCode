<%-- 
    Document   : activity
    Created on : Aug 28, 2013, 2:17:17 PM
    Author     : Lakshmi Narayanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<html:form action="/arReports" name="arReportsForm"
	   styleId="activityForm" type="com.logiware.accounting.form.ArReportsForm" scope="request" method="post">
    <table class="table filters">
	<thead>
	    <tr>
		<th colspan="6">Filters</th>
	    </tr>
	</thead>
	<tbody style="vertical-align: top">
	    <tr>
		<td class="label width-150px">Customer Name</td>
		<td>
		    <html:text property="customerName" styleId="activityCustomerName" styleClass="textbox"/>
		    <input type="hidden" name="customerNameCheck" id="activityCustomerNameCheck" value="${arReportsForm.customerName}"/>
		</td>
		<td class="label width-150px">Customer Number</td>
		<td>
		    <html:text property="customerNumber" styleId="activityCustomerNumber" styleClass="textbox readonly" readonly="true" tabindex="-1"/>
		</td>
		<td class="label width-100px">For Collector</td>
		<td>
		    <html:select property="collector" styleId="activityCollector" styleClass="dropdown">
			<html:option value="">Select</html:option>
			<html:option value="ALL">All</html:option>
			<html:optionsCollection property="collectors"/>
		    </html:select>
		</td>
	    </tr>
	    <tr>
		<td class="label width-150px">Date From</td>
		<td class="label">
		    <html:text property="fromDate" styleId="activityFromDate" styleClass="textbox" maxlength="10" onchange="validateDate(this)"/>
		    <img src="${path}/img/CalendarIco.gif" alt="Date From" align="top" id="activityFromDateCalendar" class="calendar-img"/>
		</td>
		<td class="label width-150px">Date To</td>
		<td class="label">
		    <html:text property="toDate" styleId="activityToDate" styleClass="textbox" maxlength="10" onchange="validateDate(this)"/>
		    <img src="${path}/img/CalendarIco.gif" alt="Date To" align="top" id="activityToDateCalendar" class="calendar-img"/>
		</td>
		<td class="label" colspan="2">
		    <html:checkbox property="allCustomers" styleId="activityAllCustomers" value="true" onclick="changeFilter(this)"/>
		    <label for="activityAllCustomers">All Customers</label>
		</td>
	    </tr>
	    <tr>
		<td colspan="6" class="align-center">
		    <input type="button" value="Export To Excel" class="button" onclick="createExcel('activity');"/>
		    <input type="button" value="Clear" class="button" onclick="refresh('activity');"/>
		</td>
	    </tr>
	</tbody>
    </table>
</html:form>