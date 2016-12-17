<%-- 
    Document   : dso
    Created on : Jun 18, 2012, 6:36:03 PM
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
		<td class="label width-150px">DSO to be calculated for</td>
		<td>
		    <html:select property="dsoFilter" styleId="dsoFilter" styleClass="dropdown">
			<html:option value="All Accounts Receivable">All Accounts Receivable</html:option>
			<html:option value="By Collector">By Collector</html:option>
			<html:option value="By Customer">By Customer</html:option>
		    </html:select>
		</td>
		<td class="label width-150px">Customer Name</td>
		<td>
		    <html:text property="customerName" styleId="dsoCustomerName" styleClass="textbox"/>
		    <input type="hidden" name="customerNameCheck" id="dsoCustomerNameCheck" value="${arReportsForm.customerName}" class="hidden"/>
		</td>
		<td class="label width-150px">Customer Number</td>
		<td>
		    <html:text property="customerNumber" styleId="dsoCustomerNumber" styleClass="textbox readonly" readonly="true" tabindex="-1"/>
		</td>
	    </tr>
	    <tr>
		<td class="label width-150px">Collector</td>
		<td>
		    <html:select property="collector" styleId="dsoCollector" styleClass="dropdown">
			<html:option value="">Select</html:option>
			<html:optionsCollection property="collectors"/>
		    </html:select>
		</td>
		<td class="label width-150px">DSO Period</td>
		<td class="label">
		    <html:text property="dsoPeriod" styleId="dsoPeriod" styleClass="textbox readonly" readonly="true" tabindex="-1"/>
		</td>
		<td class="label width-150px">Number of days</td>
		<td class="label">
		    <html:text property="numberOfDays" styleId="numberOfDays" styleClass="textbox readonly" readonly="true" tabindex="-1"/>
		</td>
	    </tr>
	    <tr>
		<td colspan="6" align="center">
		    <input type="button" value="Print" class="button" onclick="createPdf('dso')"/>
		    <input type="button" value="Export To Excel" class="button" onclick="createExcel('dso')"/>
		    <input type="button" value="Clear" class="button" onclick="refresh('dso')"/>
		</td>
	    </tr>
	</tbody>
    </table>
</html:form>