<%-- 
    Document   : aging
    Created on : Jun 14, 2012, 9:30:44 PM
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
		<th colspan="5">Filters</th>
	    </tr>
	</thead>
	<tbody style="vertical-align: top">
	    <tr>
		<td class="label width-150px">Customer Name</td>
		<td>
		    <html:text property="customerName" styleId="agingCustomerName" styleClass="textbox"/>
		    <input type="hidden" name="customerNameCheck" id="agingCustomerNameCheck" value="${arReportsForm.customerName}" class="hidden"/>
		</td>
		<td class="label width-150px">Customer Number</td>
		<td>
		    <html:text property="customerNumber" styleId="agingCustomerNumber" styleClass="textbox readonly" readonly="true" tabindex="-1"/>
		</td>
		<td class="label width-275px">
		    <html:radio property="reportType" styleId="agingReportTypeSummary" value="summary">Summary Report</html:radio>
		    <html:radio property="reportType" styleId="agingReportTypeDetail" value="detail">Detail Report</html:radio>
		</td>
	    </tr>
	    <tr>
		<td class="label width-150px">For Collector</td>
		<td>
		    <html:select property="collector" styleId="agingCollector" styleClass="dropdown">
			<html:option value="">Select</html:option>
			<html:option value="ALL">All</html:option>
			<html:optionsCollection property="collectors"/>
		    </html:select>
		</td>
		<td class="label width-150px">Include Agents</td>
		<td class="label">
		    <html:radio property="agents" styleId="agentsY" value="Y">Yes</html:radio>
		    <html:radio property="agents" styleId="agentsN" value="N">No</html:radio>
		    <html:radio property="agents" styleId="agentsO" value="only">Only</html:radio>
		</td>
		<td class="label width-275px">
		    <html:checkbox property="allCustomers" styleId="agingAllCustomers" value="true"/>All Customers
		    <html:checkbox property="top10Customers" styleId="top10Customers" value="true"/>Top 10 Customers
		</td>
	    </tr>
	    <tr>
		<td class="label width-150px">Customer Range</td>
		<td>
		    <html:text property="customerFromRange" styleId="agingCustomerFromRange" styleClass="textbox" maxlength="6" style="width: 55px"/>
		    -
		    <html:text property="customerToRange" styleId="agingCustomerToRange" styleClass="textbox" maxlength="6" style="width: 55px"/>
		</td>
		<td class="label width-150px">Include Net Settlement</td>
		<td class="label">
		    <html:radio property="netsett" styleId="agingNetsett" value="Y">Yes</html:radio>
		    <html:radio property="netsett" styleId="agingNetsett" value="N">No</html:radio>
		    <html:radio property="netsett" styleId="agingNetsett" value="only">Only</html:radio>
		</td>
		<td class="label width-275px">
		    <html:checkbox property="master" styleId="agingMaster" value="true"/>Master Report
		</td>
	    </tr>
	    <tr>
		<td class="label width-150px">For Sales Manager</td>
		<td>
                    <select name="salesManagers" id="salesManagers" class="dropdown" onchange="setSalesManager();">
                        <option value="">Select</option>
                        <c:forEach var="salesManager" items="${arReportsForm.salesManagers}">
                            <option value="${salesManager.managerName}<<<>>>${salesManager.managerEmail}<<<>>>${salesManager.salesCode}<<<>>>${salesManager.salesId}">${salesManager.managerName}</option>
                        </c:forEach>
                    </select>
                    <html:hidden property="salesManager.managerName" styleId="salesManagerName"/>
                    <html:hidden property="salesManager.managerEmail" styleId="salesManagerEmail"/>
                    <html:hidden property="salesManager.salesCode" styleId="salesManagerSalesCode"/>
                    <html:hidden property="salesManager.salesId" styleId="salesManagerSalesId"/>
		</td>
		<td class="label width-150px">Cut-off Date</td>
		<td>
		    <html:text property="cutOffDate" styleId="agingCutOffDate"
			       styleClass="textbox" maxlength="10" onchange="validateDate(this)"/>
		    <img src="${path}/img/CalendarIco.gif" alt="Cut-off Date" align="top" id="agingCutOffDateCalendar" class="calendar-img"/>
		</td>
		<td class="label width-275px">
		    <html:checkbox property="allPayments" styleId="agingAllPayments" value="true"/>Include payments regardless of payment date
		</td>
	    </tr>
            <tr>
                <td class="label width-150px">For Terminal Manager</td>
		<td>
                    <select name="terminalManagers" id="terminalManagers" class="dropdown" onchange="setTerminalManager();">
                        <option value="">Select</option>
                        <c:forEach var="terminalManager" items="${arReportsForm.terminalManagers}">
                            <option value="${terminalManager.managerName}<<<>>>${terminalManager.managerEmail}<<<>>>${terminalManager.terminalNumber}">${terminalManager.managerName}</option>
                        </c:forEach>
                    </select>
                    <html:hidden property="terminalManager.managerName" styleId="terminalManagerName"/>
                    <html:hidden property="terminalManager.managerEmail" styleId="terminalManagerEmail"/>
                    <html:hidden property="terminalManager.terminalNumber" styleId="terminalManagerTerminalNumber"/>
		</td>
            </tr>
	    <tr>
		<td colspan="5" align="center">
		    <input type="button" value="Print" class="button" onclick="createPdf('aging')" id="agingPrintBtn"/>
		    <input type="button" value="Export To Excel" class="button" onclick="createExcel('aging')"/>
		    <input type="button" value="Clear" class="button" onclick="refresh('aging')"/>
		</td>
	    </tr>
	</tbody>
    </table>
</html:form>