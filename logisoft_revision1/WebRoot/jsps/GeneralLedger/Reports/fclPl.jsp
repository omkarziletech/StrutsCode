<%-- 
    Document   : fclPl
    Created on : Sep 18, 2012, 6:49:34 PM
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
	    <th colspan="6">Search Filters</th>
	</tr>
	<tr>
	    <td class="label width-150px">Report Type</td>
	    <td colspan="5">
		<html:select property="reportType" styleId="reportType" styleClass="dropdown" style="max-width:250px;">
		    <html:option value="Show only items posted in the current period">Show only items posted in the current period</html:option>
		    <html:option value="Show all items regardless of period">Show all items regardless of period</html:option>
		    <html:option value="Show all items by gl period">Show all items by gl period</html:option>
		</html:select>
	    </td>
        </tr> 
        <tr>
	    <td class="label width-150px">GL Period</td>
	    <td>
		<html:text property="glPeriod" styleId="glPeriod" styleClass="textbox"/>
		<input type="hidden" name="glPeriodCheck" id="glPeriodCheck"/>
	    </td>
	    <td class="label width-150px">From Date</td>
	    <td>
		<html:text property="fromDate" styleId="fromDatefclPl" styleClass="textbox" maxlength="10" onchange="validateDate(this)"/>
		<img src="${path}/img/CalendarIco.gif" alt="From Date" title="From Date" align="top" id="fromDateCalendarfclPl" class="calendar-img"/>
	    </td>
	    <td class="label width-150px">To Date</td>
	    <td>
		<html:text property="toDate" styleId="toDatefclPl" styleClass="textbox" maxlength="10" onchange="validateDate(this)"/>
		<img src="${path}/img/CalendarIco.gif" alt="To Date" title="To Date" align="top" id="toDateCalendarfclPl" class="calendar-img"/>
	    </td>
	</tr>
        <tr>
               <td class="label width-150px">File Type</td>
               <td>
                   <html:select property="fileType" styleId="fileType" styleClass="dropdown" style="max-width:125px;">
                       <html:option value="">Select</html:option>
                       <html:option value="Export">Export</html:option>
                       <html:option value="Import">Import</html:option>
                   </html:select>
               </td>
               <td class="label width-150px">Issuing TM</td>
               <td>
                   <html:text property="issuingTerminal" styleId="issuingTerminal" styleClass="textbox"/>
                   <input type="hidden" name="issuingTerminalCheck" id="issuingTerminalCheck" value="${glReportsForm.issuingTerminal}" class="hidden"/>
               </td>
               <td class="label width-150px">POD</td>
               <td>
                   <html:text property="pod" styleId="pod" styleClass="textbox"/>
                   <input type="hidden" name="podCheck" id="podCheck" value="${glReportsForm.pod}" class="hidden"/>
               </td>
        </tr>
	<tr>
	    <td colspan="8" class="align-center">
		<input type="button" value="Print" class="button" onclick="createPdf('fclPl')"/>
		<input type="button" value="Export To Excel" class="button" onclick="createExcel('fclPl')"/>
		<input type="button" value="Clear" class="button" onclick="refresh('fclPl')"/>
	    </td>
	</tr>
    </table>
</html:form>
