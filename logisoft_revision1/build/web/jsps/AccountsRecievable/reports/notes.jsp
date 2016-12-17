<%-- 
    Document   : notes
    Created on : Jun 18, 2012, 4:20:23 PM
    Author     : Lakshmi Naryanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<html:form action="/arReports" name="arReportsForm"
	   styleId="arReportsForm" type="com.logiware.accounting.form.ArReportsForm" scope="request" method="post">
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
		    <html:text property="customerName" styleId="notesCustomerName" styleClass="textbox"/>
		    <input type="hidden" name="customerNameCheck" id="notesCustomerNameCheck" value="${arReportsForm.customerName}" class="hidden"/>
		</td>
		<td class="label width-150px">Customer Number</td>
		<td>
		    <html:text property="customerNumber" styleId="notesCustomerNumber" styleClass="textbox readonly" readonly="true" tabindex="-1"/>
		</td>
		<td class="label width-150px">Account Assigned To</td>
		<td>
		    <html:select property="accountAssignedTo" styleId="accountAssignedTo" styleClass="dropdown">
			<html:option value="">Select</html:option>
			<html:optionsCollection property="collectors"/>
		    </html:select>
		</td>
	    </tr>
	    <tr>
		<td class="label width-150px">Notes Entered By</td>
		<td>
		    <html:select property="notesEnteredBy" styleId="notesEnteredBy" styleClass="dropdown">
			<html:option value="">Select</html:option>
			<html:optionsCollection property="collectors"/>
		    </html:select>
		</td>
		<td class="label width-150px">Date From</td>
		<td class="label">
		    <html:text property="fromDate" styleId="notesFromDate" styleClass="textbox" maxlength="10" onchange="validateDate(this)"/>
		    <img src="${path}/img/CalendarIco.gif" alt="Date From" align="top" id="notesFromDateCalendar" class="calendar-img"/>
		</td>
		<td class="label width-150px">Date To</td>
		<td class="label">
		    <html:text property="toDate" styleId="notesToDate" styleClass="textbox" maxlength="10" onchange="validateDate(this)"/>
		    <img src="${path}/img/CalendarIco.gif" alt="Date To" align="top" id="notesToDateCalendar" class="calendar-img"/>
		</td>
	    </tr>
	    <tr>
		<td colspan="6" align="center">
		    <input type="button" value="Print" class="button" onclick="createPdf('notes')"/>
		    <input type="button" value="Export To Excel" class="button" onclick="createExcel('notes')"/>
		    <input type="button" value="Clear" class="button" onclick="refresh('notes')"/>
		</td>
	    </tr>
	</tbody>
    </table>
</html:form>