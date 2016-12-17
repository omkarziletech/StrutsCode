<%-- 
    Document   : statement
    Created on : Jun 8, 2012, 8:30:44 PM
    Author     : Lakshmi Naryanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<c:choose>
    <c:when test="${arReportsForm.action=='FromArInquiry'}">
	<html>
	    <head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>AR Reports</title>
		<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
		<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
		<script type="text/javascript">
		    var path = "${path}";
		</script>
		<%@include file="../../../WEB-INF/jspf/jquery.jspf" %>
		<script type="text/javascript" src="${path}/js/Accounting/AccountsReceivable/arReports.js"></script>
		<script type="text/javascript" src="${path}/js/caljs/calendar.js"></script>
		<script type="text/javascript" src="${path}/js/caljs/lang/calendar-en.js"></script>
		<script type="text/javascript" src="${path}/js/caljs/calendar-setup.js"></script>
		<link type="text/css" rel="stylesheet" href="${path}/css/layout/second-tabs.css"/>
		<link type="text/css" rel="stylesheet" href="${path}/css/jquery.autocomplete.css"/>
		<link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
		<link type="text/css" rel="stylesheet" href="${path}/css/default/style.css"/>
		<link type="text/css" rel="stylesheet" href="${path}/css/cal/skins/aqua/theme.css" title="Aqua"/>
		<link type="text/css" rel="stylesheet" href="${path}/css/cal/calendar-win2k-cold-2.css" title="win2k-cold-2" media="all"/>
	    </head>
	    <body>
		<div class="body-container" id="statement">
		    <%@include file="../../preloader.jsp"%>
		    <html:form action="/arReports" name="arReportsForm"
			       styleId="statementForm" type="com.logiware.accounting.form.ArReportsForm" scope="request" method="post">
			<html:hidden property="excludeIds" styleId="excludeIds"/>
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
					<html:text property="customerName" 
						   styleId="statementCustomerName" styleClass="textbox readonly" readonly="true" tabindex="-1"/>
				    </td>
				    <td class="label width-150px">Customer Number</td>
				    <td>
					<html:text property="customerNumber" 
						   styleId="statementCustomerNumber" styleClass="textbox readonly" readonly="true" tabindex="-1"/>
				    </td>
				    <td class="label width-200px">
					<html:checkbox property="allCustomers" styleId="statementAllCustomers" 
						       value="true" disabled="true" tabindex="-1"/>All Customers
				    </td>
                                    <td class="label width-220px">
					<html:checkbox property="coverLetter" styleId="statementCoverLetter" value="true"/>Print Cover Letter
				    </td>
				</tr>
				<tr>
				    <td class="label width-150px">For Collector</td>
				    <td>
					<html:select property="collector" 
						     styleId="statementCollector" styleClass="dropdown" disabled="true" tabindex="-1">
					    <html:option value="">Select</html:option>
					    <html:option value="ALL">All</html:option>
					    <html:optionsCollection property="collectors"/>
					</html:select>
				    </td>
				    <td class="label width-150px">Include Agents</td>
				    <td class="label">
					<html:radio property="agents" styleId="statementAgents" value="Y" disabled="true" tabindex="-1">Yes</html:radio>
					<html:radio property="agents" styleId="statementAgents" value="N" disabled="true" tabindex="-1">No</html:radio>
					<html:radio property="agents" styleId="statementAgents" value="only" disabled="true" tabindex="-1">Only</html:radio>
				    </td>
				    <td class="label width-200px">
					<html:checkbox property="eculine" styleId="statementEculine" value="true"/>Ecu Line Format
				    </td>
                                    <td class="label width-220px">
					<html:checkbox property="ap" styleId="statementAp" value="true"/>Include Payables
				    </td>
				</tr>
				<tr>
				    <td rowspan="5" valign="top" class="label">Message</td>
				    <td rowspan="5">
					<html:textarea property="message" styleId="statementMessage" rows="5" cols="20" styleClass="textbox"/>
				    </td>
				    <td class="label width-150px">Include Net Settlement</td>
				    <td class="label">
					<html:radio property="netsett" styleId="statementNetsett" value="Y">Yes</html:radio>
					<html:radio property="netsett" styleId="statementNetsett" value="N">No</html:radio>
					<html:radio property="netsett" styleId="statementNetsett" value="only">Only</html:radio>
				    </td>
				    <td class="label width-200px">
					<html:checkbox property="creditStatement" styleId="statementCreditStatement" value="true"/>Statement with Credit
				    </td>
                                    <td class="label width-220px">
					<html:checkbox property="ac" styleId="statementAc" value="true"/>Include Accruals
				    </td>
				</tr>
				<tr>
				    <td class="label width-150px">Include Prepayments</td>
				    <td class="label">
					<html:radio property="prepayments" styleId="statementPrepayments" value="Y">Yes</html:radio>
					<html:radio property="prepayments" styleId="statementPrepayments" value="N">No</html:radio>
					<html:radio property="prepayments" styleId="statementPrepayments" value="only">Only</html:radio>
				    </td>
				    <td class="label width-200px">
					<html:checkbox property="creditInvoice" styleId="statementCreditInvoice" value="true"/>Include Invoice With Credit
				    </td>
                                    <%--    <td class="label width-220px">
					    <html:checkbox property="notReleased" styleId="statementNotReleased" value="true"/>Include Not Released Import Invoices
                                            </td>   --%>
				</tr>
				<tr>
				    <td colspan="2">
					<input type="button" value="Print" class="button" onclick="createPdf('statement')"/>
					<input type="button" value="Export To Excel" class="button" onclick="createExcel('statement')"/>
					<input type="button" value="Email" class="button" onclick="sendEmail()"/>
				    </td>
				    
				</tr>
			    </tbody>
			</table>
		    </html:form>
		</div>
	    </body>
	</html>
    </c:when>
    <c:otherwise>
	<html:form action="/arReports" name="arReportsForm"
		   styleId="statementForm" type="com.logiware.accounting.form.ArReportsForm" scope="request" method="post">
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
			    <html:text property="customerName" styleId="statementCustomerName" styleClass="textbox"/>
			    <input type="hidden" name="customerNameCheck"
				   id="statementCustomerNameCheck" value="${arReportsForm.customerName}" class="hidden"/>
			</td>
			<td class="label width-150px">Customer Number</td>
			<td>
			    <html:text property="customerNumber" 
				       styleId="statementCustomerNumber" styleClass="textbox readonly" readonly="true" tabindex="-1"/>
			</td>
			<td class="label width-200px">
			    <html:checkbox property="allCustomers" 
					   styleId="statementAllCustomers" value="true" onclick="changeFilter(this)"/>All Customers
			</td>
			<td class="label width-220px">
			    <html:checkbox property="coverLetter" styleId="statementCoverLetter" value="true"/>Print Cover Letter
			</td>
		    </tr>
		    <tr>
			<td class="label width-150px">For Collector</td>
			<td>
			    <html:select property="collector" styleId="statementCollector" styleClass="dropdown">
				<html:option value="">Select</html:option>
				<html:option value="ALL">All</html:option>
				<html:optionsCollection property="collectors"/>
			    </html:select>
			</td>
			<td class="label width-150px">Include Agents</td>
			<td class="label">
			    <html:radio property="agents" styleId="statementAgents" value="Y">Yes</html:radio>
			    <html:radio property="agents" styleId="statementAgents" value="N">No</html:radio>
			    <html:radio property="agents" styleId="statementAgents" value="only">Only</html:radio>
			</td>
			<td class="label width-200px">
			    <html:checkbox property="eculine" styleId="statementEculine" value="true"/>Ecu Line Format
			</td>
			<td class="label width-220px">
			    <html:checkbox property="ap" styleId="statementAp" value="true"/>Include Payables
			</td>
		    </tr>
		    <tr>
			<td rowspan="5" valign="top" class="label">Message</td>
			<td rowspan="5">
			    <html:textarea property="message" styleId="statementMessage" rows="5" cols="20" styleClass="textbox"/>
			</td>
			<td class="label width-150px">Include Net Settlement</td>
			<td class="label">
			    <html:radio property="netsett" styleId="statementNetsett" value="Y">Yes</html:radio>
			    <html:radio property="netsett" styleId="statementNetsett" value="N">No</html:radio>
			    <html:radio property="netsett" styleId="statementNetsett" value="only">Only</html:radio>
			</td>
			<td class="label width-200px">
			    <html:checkbox property="creditStatement" styleId="statementCreditStatement" value="true"/>Statement with Credit
			</td>
			<td class="label width-220px">
			    <html:checkbox property="ac" styleId="statementAc" value="true"/>Include Accruals
			</td>
		    </tr>
		    <tr>
			<td class="label width-150px">Include Prepayments</td>
			<td class="label">
			    <html:radio property="prepayments" styleId="statementPrepayments" value="Y">Yes</html:radio>
			    <html:radio property="prepayments" styleId="statementPrepayments" value="N">No</html:radio>
			    <html:radio property="prepayments" styleId="statementPrepayments" value="only">Only</html:radio>
			</td>
			<td class="label width-200px">
			    <html:checkbox property="creditInvoice" styleId="statementCreditInvoice" value="true"/>Include Invoice With Credit
			</td>
                        <%--   <td class="label width-220px">
                                <html:checkbox property="notReleased" styleId="statementNotReleased" value="true"/>Include Not Released Import Invoices
                             </td>   --%>
		    </tr>
		    <tr>
			<td colspan="2">
			    <input type="button" value="Print" class="button" onclick="createPdf('statement')"/>
			    <input type="button" value="Export To Excel" class="button" onclick="createExcel('statement')"/>
			    <input type="button" value="Email" class="button" onclick="sendEmail()"/>
			    <input type="button" value="Configuration Report" class="button" onclick="createConfigurationExcel()"/>
			    <input type="button" value="Exempt from Auto Hold" class="button" onclick="createExemptfromAutoHoldExcel()"/>
			    <input type="button" value="Clear" class="button" onclick="refresh('statement')"/>
			</td>
		    </tr>
		</tbody>
	    </table>
	</html:form>
    </c:otherwise>
</c:choose>